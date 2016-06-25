var tileLightColor=["#759BA6"];
var tileDarkColor=["#084C61"];
var tooltip1 = CustomTooltip("my_tooltip1", "auto");
var dataDisplay,displayType,yAxisRounding,yAxisFormat,displayX,displayY,displayLegend;
var selectedViewbyIndex=0;


function buildGroupedstackedBar(div,data, columns, measureArray,wid,hgt) {
    var colorSet = d3.scale.category10();
     var chartData = JSON.parse(parent.$("#chartData").val());
     var mainWidth = wid;
    var viewByVal=[];
    for(var i in data){
        var val=data[i][columns[0]];
        if(viewByVal.indexOf(val)==-1){
            viewByVal.push(val);
        }
    }
    var tooltipData=[];
    for(i in viewByVal){
        var viewByValue2=[];
        for(var j in data){
            if(data[j][columns[0]]===viewByVal[i]){
                var map={};
                map[data[j][columns[1]]]=data[j][measureArray[0]];
                viewByValue2.push(map);
            }
        }
        tooltipData.push(viewByValue2);
    }
//    var innerRecords=tooltipData[0].length;
//    wid=parseFloat($(window).width())*(.75);;
    var measure = measureArray[0];
    graphProp(div);
    var chartColorMap = {};
    var measArr = [];
    var autoRounding;
     var customTicks = 5;
     var dataLine=[];
for(var l=0;l<(data.length );l++){
    var colMap={};

     if(typeof colMap[data[l][columns[0]]]=="undefined" && dataLine.indexOf(data[l][columns[0]]) ===-1){

colMap[columns[0]] = data[l][columns[0]];
colMap[measure] = data[l][measure];

     }
    dataLine.push(colMap);
 }
var xvalue=wid-270+((1100-wid)*0.08);
 var summArr=[];
 var viewByValues=[];
 for(var i in dataLine){
     var value1=dataLine[i][columns[0]];
     if(viewByValues.indexOf(value1)==-1){
         viewByValues.push(value1);
     }
 }
    var measValues=[];
    for(i in viewByValues){
        for(var j in data){
            if(data[j][columns[0]]===viewByValues[i]){
                measValues.push(data[j][measureArray[0]]);
            }
        }
    }
 for(i in dataLine){
     var temp=dataLine[i];
    var keys= Object.keys(temp);
    var val=temp[keys[0]];
    var k=0;
    for(k=0; k<summArr.length;k++){
        var temp2=summArr[k];
        var keys2=Object.keys(temp2);
        if(val===temp2[keys2[0]]){
            break;
        }
    }
    if(k!=summArr.length){
        continue;
    }
    var sum=0;
    var singleMap={};
    for(var j in dataLine){
        var temp1=dataLine[j];
        var keys1= Object.keys(temp1);
        if(val===temp1[keys1[0]]){
            sum += parseFloat(temp1[keys1[1]]);
        }
    }
    singleMap[keys[0]]=temp[keys[0]];
    singleMap[keys[1]]=sum;
    summArr.push(singleMap);
 }
   if(fromoneview!='null'&& fromoneview=='true'){
      }else{
              if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
        autoRounding = columnMap[measure]["rounding"];
    } else {
        autoRounding = "1d";
    }}

    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150;
if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes") {
if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    var yAxis;
    var margin = {
        top: 10,
//        right: 0,
        right: (wid*.26),
    //    bottom: 70,
		bottom:(hgt*.3),
        left: 50
    },
    width = wid -50 - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom;
}else{
        var yAxis;
    var margin = {
        top: 10,
//        right: 0,
        right: (wid*.26),
    //    bottom: 70,
		bottom:(hgt*.4),
        left: 50
    },
    width = wid -140 - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom;

}
}else{
        var yAxis;
    var margin = {
        top: 10,
//        right: 0,
        right: (wid*.26),
    //    bottom: 70,
		bottom:(hgt*.4),
        left: 50
    },
    width = wid -140 - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom;
}
    //Added by shivam
                  var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.1;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, (xvalue*1.07)-(1100/75)], range);

//
//    var range =.1;
//             if(typeof chartData[div]["barsize"]!=="undefined" && chartData[div]["barsize"]==="Yes"){
//             range = .6 
//    }
//    var x = d3.scale.ordinal()
//                .rangeRoundBands([0, (xvalue*1.07)-(1100/75)], range);

//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, (xvalue*1.07)-(1100/75)], .1);

    var y = d3.scale.linear()
            .rangeRound([height, 0]);

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
                    return d;
//                    return numberFormat(d, round, precition);
                });

    } else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .ticks(customTicks)
                .tickFormat(function(d, i) {
            return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
//                    return autoFormating(d, autoRounding);
                    //            return d;
                });
    }
 var yAxis1 = d3.svg.axis1()
            .scale(y)
            .tickFormat(function(d, i) {
                measArr.push(d);
                return "";
            });

    var localColorMap = {};
    var svg = d3.select("#"+div)
   //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
            .attr("id","svg_"+div)
             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(hgt *.87)+" ")
//         .attr("viewBox", "0 0 "+(mainWidth*1.8)+" "+(hgt)+" ")
//            .attr("width",mainWidth*1.8)
//            .attr("height", hgt )
            .attr("style","float:left")

            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var tmp = colorSet;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        if(typeof columns[1] === "undefined" || columns[1] === ""){
        return key === columns[0];
        }else {
        return key === columns[1];
        }
    }));
    var innRecordCount;
    if(typeof chartData[div]["innerRecords"]!=='undefined'){
        innRecordCount=chartData[div]["innerRecords"];
    }
    else{
        innRecordCount=5;
    }
    data.forEach(function(d,i) {
            var y0=0;
        d.val = tmp.domain().map(function(name) {
            return {
                name: d[name],
                name1:d[columns[0]],
                viewBy:name,
                value: d[measureArray[0]],
                y0: y0,
                y1: y0 += +d[measureArray[0]],
                index:i
            };
        });
        if(typeof d.val.length !=="undefined" || d.val.length !==""){
        d.total = d.val[d.val.length - 1].y1;
        }
    });
    var measSum=[];
    var c=0;
    for(var i in viewByValues){
        var viewByValue=viewByValues[i];
        var y0=0,y1=measValues[c==0?0:++c];
        var innRecCount=0;
        var f=getViewByValueCount(viewByValue, data, columns)
        for(var j in data){
            if(data[j][columns[0]]===viewByValue){
                data[j]["val"][0]["y0"]=y0;
                data[j]["val"][0]["y1"]=y1;
                y0=y1;
                innRecCount++;
                if(innRecCount<innRecordCount && innRecCount<f){
                    y1=parseInt(y1)+parseInt(measValues[++c]);
                }
            }
        }
        measSum.push(y1);
    }
    for(var i in viewByValues){
        var viewByValue=viewByValues[i];
        for(var j in data){
            if(data[j][columns[0]]===viewByValue){
                data[j]["total"]=measSum[i];
            }
        }
    }
    var col = [];
    data.forEach(function(d) {
        col.push(d[columns[1]]);
    });

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

    var max = 0;
var minVal = 0;

if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
 minVal=0;
   max = maximumValue(data, "total");

     svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));

    if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }

}}else{

    minVal=0;
   max = maximumValue(data, "total");

     svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));

    if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }

}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["axisMin"]!="") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }else{
   minVal=0;
   max = maximumValue(data, "total");

     svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));

    if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }

   }
}else{
     minVal=0;
   max = maximumValue(data, "total");

     svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));

    if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
//       if( minVal<0){
//           minVal=0;
//       }
       }
//  minVal=0;
    }


   y.domain([parseFloat(minVal), parseFloat(max)]);
   if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  -5;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return -5;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return 0;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            if(parseInt(i % 2) ==0){
            return  10;
            }else{
            return 30;   
        }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -2;
        }else {
            return 10;
        }
    })
     .attr('transform',function(d,i) {
                return transformation(div,d,i);
    })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
    })
    .style('font-size',function(d,i) {
                return font1(div,d,i);
                })
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i);
            })
   }
   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll("text")
            .attr("y", 0)
            .attr("x", -9)
            .attr("dy", ".32em")
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            .style("text-anchor", "end")
              var ht = parseFloat(hgt)* .65;
   }
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayYLine"]==="undefined" || chartData[div]["displayYLine"]==="" || chartData[div]["displayYLine"]==="Yes"){  
  svg.append("line")
        .attr("x1",0)
        .attr("y1",0)
        .attr("x2",0)
        .attr("y2",(height))
        .style("stroke", "#E1DEDE");
 }else{}
}else{
   svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-((xvalue*1.07)-(1100/75)), 0, 0)
            .tickFormat("")
        )
}
    var state = svg.selectAll(".state")
            .data(data)
            .enter().append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
                return "translate(" + x(d[columns[0]]) + ",0)";
            });
   var counter=0;
   var index=0;
    state.selectAll("rect")
            .data(function(d) {
                return d.val;
            })
            .enter().append("rect")
            .attr("width", x.rangeBand())
//            .attr("rx", 3)
            .attr("y", function(d) {
                return y(d.y1);
            })
            .attr("height", function(d) {
                return y(d.y0) - y(d.y1);
            })
             .attr("x",0)    // Edit by shivam
//            .attr("x", function(d) {
//                return x(d.x);
//            })
            .style("fill", function(d,i) {
            if(typeof chartColorMap[d.name]==="undefined"){
            chartColorMap[d.name]=getDrawColor(div, d.index) ;
            return getDrawColor(div, d.index);
            }else{
             return chartColorMap[d.name];
            }
            })
            .attr("id", function(d,i) {
                    return data[d.index][columns[0]]+":"+d.name + ":" + d.value+":"+viewByVal.indexOf(d.name1);
                })
            .attr("onclick", fun)
            .attr("index_value", function(d, i) {
        return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
            .attr("color_value", function(d, i) {
                if(typeof chartColorMap[d.name]==="undefined"){
            chartColorMap[d.name]=getDrawColor(div, d.index);
            return getDrawColor(div, d.index);
            }else{
             return chartColorMap[d.name];
            }
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
            })
            .on("mouseover", function(d, i) {
                var barId=$(this).attr("id");
                barId=barId.split(":")[barId.split(":").length-1]
                 var bar = d3.select(this);
                 var keys1 =  Object.keys(chartColorMap);
//                 alert(keys1)
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;


                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition,div);
                }
                else {
                    msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d.value);
                }
                var content = "";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[d.index][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " +msrData+ "</span><br/>";
//                for(var j in tooltipData[barId]){ // update by mayank sharma
//                    content += "<span class=\"value\"> <b>" + addCommas(numberFormat((tooltipData[barId][j][Object.keys(tooltipData[barId][j])[0]]),yAxisFormat,yAxisRounding,div))+ "</b> "+ measureArray[0]+" <b>:</b> "+Object.keys(tooltipData[barId][j])[0]+"</span><br/>";
//                }
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                 var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                     var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                hide_details(d, i, this);
            });
var txtHgt=12;
state.selectAll("rect1")
            .data(function(d) {
                return d.val;
            })
            .enter()
            .append("text")
            .attr("transform",function(d) {
                    var xvalue =x.rangeBand()/2;
                    var yValue = y(d.y1);
                    if(typeof chartData[div]["LabelPos"]==='undefined' || chartData[div]["LabelPos"]==='Top'){
                        xvalue =x.rangeBand()/2;
                        yValue = y(d.y1);
                    }
                    else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Center'){
                        xvalue =x.rangeBand()/2;
                        yValue = y(d.y1)+(y(d.y0) - y(d.y1))/2-txtHgt/2;
                    }
                    else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Bottom'){
                        xvalue =x.rangeBand()/2;
                        yValue = y(d.y1)+(y(d.y0) - y(d.y1))-txtHgt;
                    }
                    return "translate(" + xvalue + ", " + (yValue+10) + ")";
                })
                .style("text-anchor","middle")
            .text(function(d,i)
                {
                    if((y(d.y0) - y(d.y1))<13){
                        return "";
                    }
                    var value=d["name1"];
                    var groupedSum=0;
                    for(var j in summArr){
                        if(summArr[j][columns[0]]==value){
							groupedSum=summArr[j][measureArray[0]];
							break;
                    }
                    }
                    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[d["name"]]!='undefined'){
                        yAxisFormat=numberFormatArr[d["name"]];
                    }
                    else{
                        yAxisFormat="";
                    }
                    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[d["name"]]!='undefined'){
                        yAxisRounding=numberRoundingArr[d["name"]];
                    }
                    else{
                        yAxisRounding="0";
                    }
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                        if(typeof displayType=="undefined" || displayType==="Absolute"){
                            if(typeof chartData[div]["hideLabel"] != "undefined"){
                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][i] == "Yes"){
                                    return addCurrencyType(div, getMeasureId(d["name"])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));

                                }else {
                                    return "";
                                }
                            }else {
                            return addCurrencyType(div, getMeasureId(d["name"])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                        }
                    //                       return numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding);
                    }else{
                        if(typeof chartData[div]["hideLabel"] != "undefined"){
                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][i] == "Yes"){
                                var percentage = (d.value / parseFloat(groupedSum)) * 100;
                                return percentage.toFixed(1) + "%";

                            }else {
                                return "";
                            }
                        }else {
                        var percentage1 = (d.value / parseFloat(groupedSum)) * 100;
                        return percentage1.toFixed(1) + "%";
                    }
                }
            }else {
                return "";
            }
               })
               .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
               return (chartData[div]["labelFSize"]+"px");
          }else{
              return "10px";
              }
            })
               .attr("fill",function(d){
               var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.viewBy) !== -1) {
                        return "#000000";
                    }
                   var lableColor="";
                    if (typeof chartData[div]["labelColor"]!=="undefined") {
                        lableColor = chartData[div]["labelColor"];
                    }else {
                        lableColor = "#d8d47d";
                    }
                    return lableColor;
               });

               
               var displayLegends = chartData[div]["displayLegends"];
  if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
  if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){

                   var yvalue=0;
		var rectyvalue=0;
		var yLegendvalue=0;
		var rectyvalue1=0;
		var len = parseInt(mainWidth-150);
		var rectlen = parseInt(mainWidth-200);
                
                var fontsize = parseInt(mainWidth/45);
                var fontsize1 = parseInt(mainWidth/50);
		var rectsize = parseInt(mainWidth/55);
                rectsize=rectsize<5?5:rectsize;
                rectsize=rectsize>9?9:rectsize;
                
//		var rectsize = parseInt(mainWidth/58);
                
                
//		var fontsize = parseInt(height/20);
//		var fontsize1 = parseInt(height/32);
//		var rectsize = parseInt(height/30);
                var keys1 =  Object.keys(chartColorMap);
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(keys1.length<15?keys1.length:15); 
                      }

//    if(legendLength>7 && keys1.length<10){
//        yvalue = parseInt((height * 0.37));
//        yLegendvalue = parseInt(height * 0.37 -15);
//        rectyvalue = parseInt(height * .3);
//    }else if(legendLength>=10){
//
//        yvalue = parseInt((height * .09));
//        yLegendvalue = parseInt(height * .09 -15);
//        rectyvalue = parseInt(height * .05);
//}
//else{
//        yvalue = parseInt((height * .42));
//        yLegendvalue = parseInt(height * .42 -15);
//        rectyvalue = parseInt(height * .38);
//}
   if(legendLength>=15){
             yvalue = parseInt((height * .09));
             yLegendvalue = parseInt((height * .017));
             rectyvalue = parseInt((height * .05));

}
else{
           if (legendLength>=10){
                 yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+10;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+7);
}
            else if (legendLength>=6){
                yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+20;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+17);
            }
            else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+20;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+20);
        }
    }

  if(fontsize1>11){
                  fontsize1 = 12;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
                
 if (fontsize>12){
     fontsize = 12;
   }else if(fontsize<7){
                  fontsize = 7;
                }
                
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
                
        var count = 0;
        if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
        var transform =
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
.attr("transform", "translate(" + (xvalue*1.07)  + "," + (yLegendvalue+4) + ")")

            .attr("fill", "Black")
        .text(function(d){
    if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
              return columns[1] ;
    }else{
                 return ""
    }
            })
            .attr("svg:title",function(d){
               return columns[1];
           })
        }
           for(var m=0;m<legendLength;m++){
            if(m!=0){
            yvalue = parseInt(yvalue+height*0.075)
            rectyvalue = parseInt(rectyvalue+height*0.075)
            }
             if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
//            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + (xvalue*1.07)   + "," + (yvalue)*.9 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                  return  chartColorMap[keys1[m]];
            })

//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )
      //         }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + ((xvalue*1.07)+15)   + "," + (yvalue+9)*.9 + ")")
            .attr("fill", function(){
                if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                 return chartColorMap[keys1[m]]
                }
             }else{
                 return  "#000";
                }
            })
            .attr("style", "font-size:"+fontsize1+"px")

            .attr("id",function(){
                return keys1[m];
            } )

            .text(function(){

                if(keys1[m]!="undefined"){
if(keys1[m].length>15){
                    return keys1[m].substring(0, 14)+"..";
    }else {
                    return keys1[m];
    }
     }else{
return  "";
     }
           })
           .attr("svg:title",function(d){
               return keys1[m];
           })
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
            }
           }
        }
  
  else{
        var yvalue=0;
        var rectyvalue=0;
        var yLegendvalue=0;
        var rectyvalue1=0;
        var len = parseInt(mainWidth-150);
        var rectlen = parseInt(mainWidth-200);
        var fontsize = parseInt(mainWidth/45);
        var fontsize1 = parseInt(mainWidth/50);
        var rectsize = parseInt(height/30);
        rectsize=rectsize>10?10:rectsize;
         var legendLength;
                var keys1 =  Object.keys(chartColorMap);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }
                else{

                    legendLength=(keys1.length<15?keys1.length:15); 
                }

               if(legendLength>7 && legendLength<10){

        yvalue = parseInt((height * .37));
        yLegendvalue = parseInt((height * .3));
        rectyvalue = parseInt((height * .33));
}else if(legendLength>=10){

        yvalue = parseInt((height * .09));
        yLegendvalue = parseInt((height * .017));
        rectyvalue = parseInt((height * .05));
}
else{

     yvalue = parseInt((height * .42));
        yLegendvalue = parseInt((height * .35));
        rectyvalue = parseInt((height * .38));
}
  if(fontsize1>11){
                  fontsize1 = 11;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
  if(fontsize>11){
                  fontsize = 11;
                }else if(fontsize1<7){
                  fontsize = 9;
                }
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }

var count = 0;

 
if(width<height){
                yvalue = parseInt(height*.10);
                rectyvalue = parseInt((height*0.91 ));
            }else{
                yvalue = parseInt(height*1.15);
                rectyvalue = parseInt((height*1.05 ));
            }
var widthvalue = parseInt((width *.01));// for all rect and text align
            var widthRectvalue = parseInt((width *.1)-(width*.018));
            var startX=widthvalue;
            yvalue*=1.06;
var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/14;
//alert(chartWid)
 chartWid*=.65;
// svg.append("g")
//           .append("text")
//            .attr("style","margin-right:10")
//             .attr("style", "font-size:"+fontsize+"px")
//.attr("transform", "translate(" + (widthvalue-20)  + "," + (yvalue-18) + ")")
//            .attr("fill", "Black")
//        .text(function(d){
//    if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
//              return colName[1] ;
//    }else{
//                 return ""
//    }
//            })
//            .attr("svg:title",function(d){
//               return colName[1];
//           })


       for(var m=0;m<(legendLength);m++){ ////length
           var charactercount = keys1[m].length;
           
         
           
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (widthvalue-15)  + "," + (yvalue-8) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
              .attr("fill", function(){
                return chartColorMap[keys1[m]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
//             .attr("fill", function(){
//                 return chartColorMap[keys1[m]]
//            })
             .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  color(i);
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys1[m];
            } )
            .text(function(d){
                if(keys1[m]!="undefined"){
//if(keys1[m].length>11){
//                    return keys1[m].substring(0, 11)+"..";
//    }else {
                    return keys1[m];
//    }
     }else{
return  "";
     }
           })
           .attr("svg:title",function(d){
               return keys1[m];
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {
            setMouseOutEvent(this.id,div)
                    })
              count++
              var step=0;
              step+=30;
              step+=(keys1[m].length*5)
              if(widthvalue+step<chartWid){
                widthvalue += step;
              }
              else{
                  yvalue +=20;
                  widthvalue=startX;



}
}
}
}
}
  
  
  
//               var html="";
//    if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
//     html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='color:black;font-size:14px;text-decoration:none' >"+columns[1]+"</span></td></tr>";
//    }else{
//         html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='color:black;font-size:14px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
//    }
//var keys =  Object.keys(chartColorMap);
//    for(var m=0;m<keys.length;m++){
//    if(keys[m].length > 15){
//    html +="<tr style='height:25px'><td><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + chartColorMap[keys[m]] + "'></canvas><span title='"+keys[m]+"' style='margin-left:5px;color:"+chartColorMap[keys[m]]+"'> "+keys[m].substring(0, 8)+"..</span> </td></tr>";
//    }else {
//       html +="<tr style='height:25px'><td><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + chartColorMap[keys[m]] + "'></canvas><span style='margin-left:5px;color:"+chartColorMap[keys[m]]+"'> "+keys[m]+"</span> </td></tr>";
//    }
//}
//    html +="</table></td></tr></table></div>";
//     if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
//    $("#"+div).append(html);
//     }else{
//         $("#"+div).append();
//     }
//

function buildGroupedLine(div,data, columns, measureArray,divWid,divHgt) {
//   var color = d3.scale.category10();

var divId;
 if (typeof data!== 'undefined' && (data.length==5 || data.length<5)) {

return buildGroupedBar(div, data, columns, measureArray, divWid, divHgt, div);

}
if($("#chartType").val()=="India-Map-Dashboard"){
    divId = "section2";
}else {
    divId = div;
}
 var fromoneview=parent.$("#fromoneview").val()
 var columnsVar = columns;
 var measArr = [];
 var colMap={};
 var columnData = {};
  var colorCount=0;
 for(var l=0;l<data.length;l++){

     if(typeof columnData[data[l][columns[1]]]=="undefined"){
            var chartDataList = [];
         var map = {};
         map[columns[0]]=data[l][columns[0]];
         map[columns[1]]=data[l][columns[1]];
//         for(var t=0;t<measureArray.length;t++){
         map[measureArray[0]]=data[l][measureArray[0]];
//         }
         chartDataList.push(map);
         columnData[data[l][columns[1]]]=chartDataList;
     }
     else{
         var chartDataList = columnData[data[l][columns[1]]];
         var map = {};
         map[columns[0]]=data[l][columns[0]];
         map[columns[1]]=data[l][columns[1]];
//         for(var t=0;t<measureArray.length;t++){
         map[measureArray[0]]=data[l][measureArray[0]];
//         }
         chartDataList.push(map);
         columnData[data[l][columns[1]]]=chartDataList;

     }
     if(typeof colMap[data[l][columns[0]]]=="undefined"){
         colMap[data[l][columns[0]]]=data[l][columns[0]];
     }
 }

     var color = d3.scale.category10();

//     divWid=parseFloat($(window).width())*(.35);

    var measure1 = measureArray[0];
    var autoRounding1;
    var measArr = [];
     var chartData = JSON.parse(parent.$("#chartData").val());
var colName = chartData[div]["viewBys"];
      var customTicks = 10;
     if(fromoneview!='null'&& fromoneview=='true'){

     }else{
         if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
     }


  var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
}else{
       var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}
    var colorGroup={};
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var hide = "hideLabels(this.id,\""+div+"\")"
     if(fromoneview!='null'&& fromoneview=='true'){
var regid=div.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "oneviewdrillWithinchart1(this.id)";
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
   var margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 80
    };
    var height;
var  width = parseFloat(divWid); //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
  if($("#chartType").val()=="India-Map-Dashboard"){
   height =    divHgt * .78;
  }else{
      
  height = parseFloat(divHgt)* .78;
  }
 var max = maximumValue(data, measureArray[0]);
    var minVal = minimumValue(data, measureArray[0]);
if(parseFloat(minVal) > 0){
    minVal = 0;
}

    var x = d3.scale.ordinal().rangePoints([0, width -100], .2);
    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
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
                });
    }
//    var valueline = d3.svg.area().interpolate("monotone")
    var valueline ;//= d3.svg.area()
//            .x(function(d) {
//                return x(d[columns[1]]);
//            })
//            .y(function(d) {
//                return y(d[measureArray[j]]);
//            });
var svg;
if($("#chartType").val()=="India-Map-Dashboard"){
 svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + divId)
//            .attr("viewBox", "0 0 "+(width+120 )+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
//            .classed("svg-content-responsive", true)
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + 150 )
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
}else{
    if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes"){
        if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width+120)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    }else{
                  svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 10 "+(width+40)+" "+((height+50) + margin.top + margin.bottom )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")"); 
    }
    }else
        {
          svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");   
}
}
  svg.append("g")
    .append("svg:svg")
//    .style("margin-left", "3em")
    .attr("width", width-margin.left-margin.right)
//    .attr("height", height-margin.left-margin.right);
    .attr("height",function(d){
        if($("#chartType").val()=="India-Map-Dashboard"){
            return height;
        }else {
            return height-margin.left-margin.righ;
        }
    });

 svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
    svg.append("g")
            .attr("class", "y axis");
    var min1 = [];
    var flag = "";
    for (var key in data) {
        for (var meas in measureArray) {
            min1.push(data[key][measureArray[meas]]);
        }
    }
//    x.domain(data.map(function(d) {
//        return d[columns[1]];
//    }));


      svg = d3.select("#" + divId).select("g");
       if(fromoneview!='null'&& fromoneview=='true'){
           div=div1;
       }
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
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
           flag =  measure1;
           }
          return flag
             });}
//if(typeof displayX !=="undefined" && displayX == "Yes"){

//}

    var colorArr = [];
    var colorMap = {};
    var chartMap = {};
    var perTotal;
    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayYLine"]==="undefined" || chartData[div]["displayYLine"]==="" || chartData[div]["displayYLine"]==="Yes"){  
  svg.append("line")
        .attr("x1",0)
        .attr("y1",0)
        .attr("x2",0)
        .attr("y2",(height))
        .style("stroke", "#E1DEDE");
 }else{}
}else{
    svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width + 100, 0, 0)
            .tickFormat("")
        )
    }

   var listMap=[];
   for(var colCol in colMap){
       listMap.push(colMap[colCol])
   }
//    for (var i = 0; i < columnData.length; i++) {
var i=0;
for(var col in columnData){

    var lineData = columnData[col];
    if(i==0){

        valueline = d3.svg.area()
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y(d[measureArray[0]]);
            });

            x.domain(
            listMap.map(
            function(d) {
        return d;

}
)
);
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i);
            })
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  -2;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return -2;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -10;
        }else {
            return 0;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            if(parseInt(i % 2) ==0){
            return  10;
            }else{
            return 30;   
            }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 0;
        }else {
            return 9;
        }
    })
       .attr('transform',function(d,i) {
                return transformation(div,d,i);
    })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
    })
            .append("svg:title").text(function(d) {
        return d;
    });

}
    }
        j = i;
  //      alert("linedata: "+JSON.stringify(lineData))
        svg.append("path")
                .data(lineData)
                .attr("d", valueline(lineData))
                .attr("fill", getDrawColor(div, parseInt(i)))
                .style("stroke-width", "3px")
                .style("stroke", getDrawColor(div, parseInt(i)));
        colorArr.push(getDrawColor(div, parseInt(i)));
        if (typeof chartMap[measureArray[0]] === "undefined") {
            chartMap[measureArray[0]] = color(i);
            colorMap[i] = measureArray[0] + "__" + color(i);
        }
        parent.$("#colorMap").val(JSON.stringify(colorMap));
     //LabelText

     var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
        svg.selectAll("labelText")
             .data(lineData).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("id", function(d,i){
                       return d[columns[1]] + ":" + d[measureArray[0]];
                   })
                   .attr("x", function(d){
                       return x(d[columns[0]]) - 15;
                   } ).attr("y", function(d){
                       return y(d[measureArray[0]]) + 10;
                   }).attr("onclick", hide)

      .attr("dy", ".35em")
//      Added by shivam
      .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "8px";
              }
            })
            .text(function(d)
    
                {
                    //aaaa
                      if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        if(typeof chartData[div]["hideLabel"] != "undefined"){
                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][i] == "Yes"){
                           return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);

                }else {
                    return "";
                }}else {
                           return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
//                       return numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding);
                    }else{
                       if(typeof chartData[div]["hideLabel"] != "undefined"){
                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][i] == "Yes"){
                             var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";

                }else {
                    return "";
                }}else {
                    var percentage1 = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage1.toFixed(1) + "%";
                }
            }
            }else {return "";}
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
                .data(lineData)
                
                .enter().append("circle")
                .attr("r", 4)
                .attr("cx", function(d) {
                    return x(d[columns[0]]);
                })
                .attr("cy", function(d) {
                    return y(d[measureArray[0]]);
                })
                 .attr("class", function(d, i) {
                 
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                 })
                 .attr("color_value", function(d, i) {
                 var drawColor=getDrawColor(div, parseInt(i));
               //  alert(drawColor)
                 return drawColor;
                })
                 .attr("fill", function(){
                 return getDrawColor(div, parseInt(i));
                })
                .style("stroke", getDrawColor(div, parseInt(i)))
                
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    colorGroup[d[columns[1]]]=getDrawColor(div, parseInt(i));
//                    return d[columns[1]] + ":" + d[measureArray[0]];
return d[columns[1]]+div; 
                })
                
                .on("mouseover", function(d) {
//                    alert(JSON.stringify(d));
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                

                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                    var msrData;
					 if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d[measureArray[0]]);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                        msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d[measureArray[0]]);

                }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div));
            }
                     //   msrData = addCommas(d.value);
//                    }
                    var content = "";
                    content += "<span class=\"name\">" + colName[0] + ":</span><span class=\"value\"> " + d[colName[0]] + "</span><br/>";
//                    if(typeof d.name!="undefined" && typeof colName[1] !="undefined"){
                    content += "<span class=\"name\">" + colName[1] + ":</span><span class=\"value\"> " + d[colName[1]] + "</span><br/>";
//                }
                    content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                    return tooltip.showTooltip(content, d3.event);
                
                })
                .on("mouseout", function(d, i) {
                  
                    hide_details(d, i, this);
                });
                i++;
    }
    
              var displayLegends = chartData[div]["displayLegends"];
              if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
              if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    
     var yvalue=0;
        var rectyvalue=0;
        var yLegendvalue=0;
        var rectyvalue1=0;
        var len = parseInt(width-150);
        var rectlen = parseInt(width-200);
        var fontsize = parseInt(width/45);
        var fontsize1 = parseInt(width/50);
        var rectsize = parseInt(width/60);
         rectsize=rectsize>10?10:rectsize;
           var legendLength;
                var keys1 =  Object.keys(colorGroup);
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(keys1.length<15?keys1.length:15); 
                      }

//               if(keys1.length>7 && keys1.length<10){
//
//        yvalue = parseInt((height * .37));
//        yLegendvalue = parseInt((height * .3));
//        rectyvalue = parseInt((height * .33));
//}else if(legendLength>=10){
//
//        yvalue = parseInt((height * .09));
//        yLegendvalue = parseInt((height * .017));
//        rectyvalue = parseInt((height * .05));
//}
//else{
//
//     yvalue = parseInt((height * .42));
//        yLegendvalue = parseInt((height * .35));
//        rectyvalue = parseInt((height * .38));
//}
   if(legendLength>=15){
        yvalue = parseInt((height * .09));
        yLegendvalue = parseInt((height * .017));
        rectyvalue = parseInt((height * .05));
           
}
else{
           if (legendLength>=10){
                 yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+19;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+7);
            }
            else if (legendLength>=6){
                yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+29;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+17);
            }
            else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+32;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+20);
        }
    }

  if(fontsize1>15){
                  fontsize1 = 13;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
  if(fontsize>15){
                  fontsize = 15;
                }else if(fontsize1<7){
                  fontsize = 9;
                }
           if($("#chartType").val()=="India-Map-Dashboard"){
           fontsize1 = 10;
           rectsize = 8;
       }
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
//                 var displayLegends = chartData[div]["displayLegends"];
//                        if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){

        var count = 0;
        if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
        var transform =

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
//.attr("transform", "translate(" + mainWidth*1.52  + "," + yLegendvalue + ")")
.attr("transform", "translate(" +(width- margin.left - margin.right-13)  + "," + yLegendvalue + ")")

            .attr("fill", "Black")
        .text(function(d){
    if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
              return colName[1] ;
    }else{
                 return ""
    }
            })
             .attr("svg:title",function(d){
               return  colName[1];
           })
        }
           for(var m=0;m<legendLength;m++){
             //  alert("yes=="+m)
            if(m!=0){
            yvalue = parseInt(yvalue+height*0.075)
            rectyvalue = parseInt(rectyvalue+height*0.075)
            }
               if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
//            .attr("transform", "translate(" + mainWidth*1.48  + "," + rectyvalue + ")")
            .attr("transform", "translate(" + (width- margin.left - margin.right-10)  + "," + (rectyvalue*.85) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
//            .attr("fill", color(i))
            .attr("fill", function(){
                return colorGroup[keys1[m]];
            })
            
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )
               }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
//            .attr("transform", "translate(" + mainWidth*1.52  + "," + yvalue + ")")
            .attr("transform", "translate(" +(width- margin.left - margin.right+10)  + "," + (rectyvalue+11)*.85 + ")")
//            .attr("fill", color(i))

            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
             if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                return colorGroup[keys1[m]];
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return keys1[m];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){

                if(keys1[m]!="undefined"){
if(keys1[m].length>15){
                    return keys1[m].substring(0, 14)+"...";
    }else {
                    return keys1[m];
    }
     }else{
return  "";
     }
           })
           .attr("svg:title",function(d){
               return  keys1[m];
           })
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
            
                    })
              count++;
}
}         
else{
    
        var yvalue=0;
        var rectyvalue=0;
        var yLegendvalue=0;
        var rectyvalue1=0;
        var len = parseInt(width-150);
        var rectlen = parseInt(width-200);
        var fontsize = parseInt(width/45);
        var fontsize1 = parseInt(width/52);
        var rectsize = parseInt(height/20);
        rectsize=rectsize>10?10:rectsize;
         var legendLength;
                var keys1 =  Object.keys(colorGroup);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
}
                else{

                    legendLength=(keys1.length<15?keys1.length:15); 
                }

               if(legendLength>7 && legendLength<10){

        yvalue = parseInt((height * .37));
        yLegendvalue = parseInt((height * .3));
        rectyvalue = parseInt((height * .33));
}else if(legendLength>=10){

        yvalue = parseInt((height * .09));
        yLegendvalue = parseInt((height * .017));
        rectyvalue = parseInt((height * .05));
}
else{

     yvalue = parseInt((height * .42));
        yLegendvalue = parseInt((height * .35));
        rectyvalue = parseInt((height * .38));
}
  if(fontsize1>11){
                  fontsize1 = 11;
                }else if(fontsize1<9){
                  fontsize1 = 9;
                }
                
 if (fontsize>11){
     fontsize = 11;
   }else if(fontsize<9){
                  fontsize = 9;
                }
//          Added by shivam

                 if(fontsize>15){
                  fontsize = 15;
                }else if(fontsize1<7){
                  fontsize = 9;
                }
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }

var count = 0;

 
if(width<height){
                yvalue = parseInt(height*.10);
                rectyvalue = parseInt((height*0.91 ));
            }else{
                yvalue = parseInt(height*1.15);
                rectyvalue = parseInt((height*1.05 ));
            }
var widthvalue = parseInt((width *.01));// for all rect and text align
           var widthRectvalue = parseInt((width *.1)-(width*.018));
//           alert(widthRectvalue)
            var startX=widthvalue;
            yvalue*=1.03;
var chartWid=(width+ margin.left + margin.right)-(width+ margin.left + margin.right)/14;
  chartWid*=.92;
// svg.append("g")
//           .append("text")
//            .attr("style","margin-right:10")
//             .attr("style", "font-size:"+fontsize+"px")
//.attr("transform", "translate(" + (widthvalue-20)  + "," + (yvalue-9)  + ")")
//            .attr("fill", "Black")
//        .text(function(d){
//    if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
//              return columns[1] ;
//    }else{
//                 return ""
//    }
//            })
//            .attr("svg:title",function(d){
//               return columns[1];
//           })


       for(var m=0;m<(legendLength);m++){ ////length
           var charactercount = keys1[m].length;
           
         
           
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (widthvalue-20)  + "," + (yvalue-9) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
              .attr("fill", function(){
                return colorGroup[keys1[m]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthvalue-5)  + "," + yvalue + ")")
    .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return colorGroup[keys1[m]];
              }
             }else{
                 return  "#000";
             }
            })

            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys1[m];
            } )
            .text(function(d){
                if(keys1[m]!="undefined"){
//if(keys1[m].length>11){
//                    return keys1[m].substring(0, 11)+"..";
//    }else {
                    return keys1[m];
//    }
     }else{
return  "";
     }
           })
           .attr("svg:title",function(d){
               return keys1[m];
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {
            setMouseOutEvent(this.id,div)
                    })
              count++
              var step=0;
              step+=30;
              step+=(keys1[m].length*5)
              if(widthvalue+step<chartWid){
                widthvalue += step;
              }
              else{
                  yvalue +=20;
                  widthvalue=startX;
}
}
}
}
}



function maximumValue(data, measure) {
    var max;
    for (var j = 0; j < data.length; j++) {

        if (j === 0) {
            max = data[j][measure];
        } else {
            if (max < parseFloat(data[j][measure])) {
                max = data[j][measure];
            }
        }
    }
    return max;
}
function maximumValueNegative(data, measure) {
    var max;
    for (var j = 0; j < data.length; j++) {

        if (j === 0) {
            max = data[j][measure];
        } else {
            if (max > parseFloat(data[j][measure])) {
                max = data[j][measure];
            }
        }
    }
    return max;
}
function minimumValue(data, measure) {
    var min;
    try {
        for (var k = 0; k < data.length; k++) {
            if (k === 0) {
                min = data[k][measure];
            } else {
                if (min > parseFloat(data[k][measure])) {
                    min = data[k][measure];
                }
            }
        }
    } catch (e) {
    }
    return min;
}

function addCommas(nStr)
{
    var decimalPlace = 2;
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    x2 = x2.substring(0, decimalPlace + 1);
    if (x2 === ".00" || x2 === ".0") {
        x2 = "";
    }
    if((parseInt(x2))==0){
                  x2 = "";
                }
    return x1 + x2.toString();
}

//function drillWithinchart(id,div){
//
//    parent.drillWithinchart1(id,div)
//}



function buildOverLayedChart(data,div) {
    overLayData = data;
    var divContent = "";
    var cuts = 14;
    var noOfRecords = 10;
    var divHeight = $(window).height()*1.2;
    var divWidth = $(window).width();

    var chartData = JSON.parse($("#chartData").val());
    var columns = chartData["chart1"]["viewBys"];
    var measure = chartData["chart1"]["meassures"];
    divHeight = divHeight * .40;
    overAll = data["chart2"];
    var sum = d3.sum(overAll, function(d) {
        return d[measure[0]];
    });
    parent.$("#sumValue").val(sum.toFixed(0));
    var columns1 = [];
    columns1.push(columns[1]);
    divWidth = divWidth / 3.3;
    cutData = data["chart1"]["cuts"];
    var objKeys = Object.keys(cutData);
    var noOfCuts = objKeys.length < cuts ? objKeys.length : cuts;
    divContent += "<div  style='width:100%'><table style='width:100%'><tr>";
    for (var k = 0; k < noOfCuts + 1; k++) {
        divContent += "<td class='DroppableDiv' id='divchart" + (k) + "' style='overflow:auto;position:relative;width: 32.8%;height:" + divHeight + ";float:left;border-right:1 solid rgb(200,200,200);'>" +
                "<table id='tablechart" + (k) + "' align='left' class=''>" +
                "<tr><td>" +
//                "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart" + (k) + "' onclick='openCharts(this.name,\"" + divWidth + "\",\"" + divHeight + "\",\"" + keyVal + "\")' />" +
//                "<img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator' onclick='buildTabelForOverLay(\"" + chartId + "\",\"" + dataPath + "\",\"" + keyVal + "\",\"" + columns1 + "\",\"" + measure + "\",\"table" + (k) + "\")' />" +
                "</td><td align='right' class='headDb'>";
        if (k === 0) {
            divContent += "<span>Total</span>";
        } else {
            divContent += "<span>" + objKeys[k - 1] + "</span>";
        }

        divContent += "</td></tr></table>";
        if(k==0){
        divContent += "<div id='chart" + (k) + "' style='width:99.5%;height:" + divHeight + ";float:left;border-bottom:#cccccc 1px solid;border-right:#cccccc 1px solid;'></div>";
        }else {
        divContent += "<div id='chartAd" + (k) + "' style='width:99.5%;height:" + divHeight + ";float:left;border-bottom:#cccccc 1px solid;border-right:#cccccc 1px solid;'></div>";
        }
        divContent += "<div id='table" + (k) + "' ></div></td>";
        if ((k + 1) % 3 === 0) {
            divContent += "</tr><tr>";
        }
    }
    divContent += "</tr></table></div>";
    $("#"+div).append(divContent);
    var minData = [];
    for (var no = 0; no < (overAll.length < noOfRecords ? overAll.length : noOfRecords); no++) {
        minData.push(overAll[no]);
    }
    buildOverlayDonut("chart0","chart0", minData, columns1, measure, divWidth, divHeight, (Math.min(divWidth, divHeight) / 2), "Total");
    for (var i = 0; i < noOfCuts; i++) {
        minData = [];
        for (var no = 0; no < (cutData[objKeys[i]].length < noOfRecords ? cutData[objKeys[i]].length : noOfRecords); no++) {
            minData.push(cutData[objKeys[i]][no]);
        }
        var sum = d3.sum(cutData[objKeys[i]], function(d) {
            return d[measure[0]];
        });
        parent.$("#sumValue").val(sum.toFixed(0));
        buildOverlayDonut("chart" + (i + 1),"chartAd" + (i + 1), minData, columns1, measure, divWidth, divHeight, (Math.min(divWidth, divHeight) / 2), objKeys[i]);
    }
}


function buildMultiKpiDb(div,data) {
var chartData = JSON.parse(parent.$("#chartData").val());
        var charts = Object.keys(chartData);
        var width = $(window).width();
        var divHeight = $(window).height();
        var divContent = "";
        var widths = [];
        divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
for (var ch in charts) {
                    widths.push(width / 1.15);
                    divHeight = $(window).height()*.8;
                    if(charts.length==1){
                    divContent += "<div id='chartAdv"+(parseFloat(ch) + 1)+"' style='float:center;'></div>";
                }
                    else{
                    divContent += "<div id='chartAdv"+(parseFloat(ch) + 1)+"' style='float:left;'></div>";
                }
                }
         $("#"+div).html(divContent);
        for (var ch in charts) {
            var chartDetails = chartData[charts[ch]];
var chartType;
var chartList=[];
             chartList.push("Pie");
             chartType = "Pie";
            var chartId = "chart" + (parseFloat(ch) + 1);
            var divId = "chartAdv" + (parseFloat(ch) + 1);
            buildDashletKPI(chartDetails, ch, widths[ch], divHeight, chartType, chartId,divId, chartList,data);

        }
        }
    function buildDashletKPI(chartDetails, chartIndex,  divWidth, divHeight, chart, chartId,divId, chartList,data) {
        var viewbys = [];
        var viewbyVals = chartDetails.viewBys;
        viewbys.push(viewbyVals[0]);
        var measures = chartDetails.meassures;
                var data3 = [];
                for(var h=0;h<(data[chartId].length < 10 ? data[chartId].length : 10);h++){
                    data3.push(data[chartId][h])
                }
               buildPieMulti(chartId,divId, data3, viewbys, measures, divWidth * .5, divHeight * .8, 300);
        }

// for world map animation flight
function buildWorldMapLinked(div,mapData, columns, measureArray) {
    var data = mapData["chart1"];
//    $("#legendchart").css("opacity", ".8");
//    $("#legendchart").attr("onmouseover", "$('#legendchart').css('opacity','1')");
//    $("#legendchart").attr("onmouseout", "$('#legendchart').css('opacity','.8')");
    var maxMap = {};
    var minMap = {};
    for (var num = 0; num < measureArray.length; num++) {
        var measureData = [];
        for (var key in data) {
            measureData.push(data[key][measureArray[num]]);
        }
        maxMap[measureArray[num]] = Math.max.apply(Math, measureData);
        minMap[measureArray[num]] = Math.min.apply(Math, measureData);
    }
//    parent.$("#maxMap").val(JSON.stringify(maxMap));
//    parent.$("#minMap").val(JSON.stringify(minMap));
    function show_details(d) {
        var content;
        var count = 0;
        for (var j = 0; j < data.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            }
            else if (isShortForm(d, data[j][columns[0]])) {
                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
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
    var dataMap = {};
    var width = $(window).width();
    var height = $(window).height();

    var projection = d3.geo.mercator()
            .center([-20, 60])
            .scale(150);
    var path = d3.geo.path()
            .projection(projection);
    var divhtml = "<div id='tabDiv' width='50%' style='float:left;margin-left:-480%;width:2%'></div>";
//    $("body").append(divhtml);
    $("#legendchart").append(divhtml);
    var svg = d3.select("#"+div).append("svg")
            .attr("width", width)
            .attr("height", height);
    var g = svg.append("g");
    d3.json("JS/world-population.geo.json", function(error, topology) {
        g.selectAll("path")
                .data(topojson.feature(topology, topology.objects.countries).features)
                .enter()
                .append("path")
                .attr("d", path)
                .attr("class", "countries")
                .style("stroke", "rgb(100,100,100)");

        d3.json("JS/coordinate.json", function(data1) {
            var path1 = d3.geo.path().projection(projection).pointRadius(function(d) {

                return d.coordinates / 50;
            });
            g.selectAll("path.place")
                    .data(data1)
                    .enter().append("path")
                    .attr("d", path1.pointRadius(3))
                    .attr("class", function(d) {
                        for (var j = 0; j < data.length; j++) {
                            if (data[j][columns[0]].toLowerCase() == (d.properties.name).toLowerCase()) {
                                return "place_" + d.properties.name;
                            }
                            else if (data[j][columns[1]].toLowerCase() == (d.properties.name).toLowerCase()) {
                                return "place_" + d.properties.name;
                            }
                        }
                        return "place1";
                    })
                    .attr("id", function(d) {
                        for (var j = 0; j < data.length; j++) {
                            if (data[j][columns[0]].toLowerCase() == (d.properties.name).toLowerCase()) {
                                dataMap[(d.properties.name).toLowerCase()] = d.coordinates;
                                return (d.properties.name).toLowerCase();
                            }
                            else if (data[j][columns[1]].toLowerCase() == (d.properties.name).toLowerCase()) {
                                dataMap[(d.properties.name).toLowerCase()] = d.coordinates;
                                return (d.properties.name).toLowerCase();
                            }
                        }
                        dataMap[(d.properties.name).toLowerCase()] = d.coordinates;
                        return "place1";
                    })

                    .attr("fill", function(d, m) {
                        if (m == data1.length - 1) {
                            showUp();
                        }
                        for (var jn = 0; jn < data.length; jn++) {
                            if (data[jn][columns[0]].toLowerCase() == (d.properties.name).toLowerCase()) {
                                return "#036";
                            }
                        }
                    })
                    .on("mouseover", function(d) {
                        return showLines(this.id);

                    }).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

        });
    });

    function showLines(val) {
        if (val != "place1") {
            svg.selectAll(".route").style("display", "none");
            d3.selectAll(".plane").attr("d", "");
            for (var h = 0; h < data.length; h++) {
                if (val.toLowerCase() == data[h][columns[0]].toLowerCase()) {
                    fly(data[h][columns[0]].toLowerCase(), data[h][columns[1]].toLowerCase());
                }
            }
            svg.selectAll("#" + val.toLowerCase() + "_line").style("display", "");
//        var content;
//        content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + val.toUpperCase() + "</span><br/>";
//        for(var i=0;i<data.length;i++){
//            if(data[i][columns[0]].toLowerCase()==val.toLowerCase()){
//            content += "<span class=\"name\">" + data[i][columns[1]] + ": </span><span class=\"value\"> " + data[i][measureArray[0]] + "</span><br/>";
//        }
//        }
//        return tooltip.showTooltip(content, d3.event);

            var tabWidth = $(window).width() * 150 / $(window).width();
            var htmlstr = "<table class=\"tBody\" style=' align:left; margin-top:10%;margin-left:-2300%;width:" + tabWidth + "px'>";
            var tabcount = 0;
            for (var i = 0; i < data.length; i++) {
                if (data[i][columns[0]].toLowerCase() == val.toLowerCase()) {
                    var name = data[i][columns[1]];
                    var measure = measureArray[0];
                    var value = data[i][measureArray[0]];
                    if (tabcount === 0) {
                        htmlstr += "<tr class=\"thClass\" style='font-size:90%' align='center'><td colspan='2'>" + data[i][columns[0]] + "</td></tr>";
                        htmlstr += "<tr class=\"thClass\" style='font-size:90%' align='center'><td>" + columns[1] + "</td><td>" + measure + "</td></tr>";
                    }
                    tabcount++;
//                    if (tabcount % 2 === 1) {
//                        htmlstr += "<tr class=\"tdClass1\"><td align='left'>" + name + "</td>";
//                    }
//                    else {
                    htmlstr += "<tr class=\"tdClass2\"><td align='left'>" + name + "</td>";
//                    }
                    var toData;
                    if (isFormatedMeasure) {
                        toData = numberFormat(value, round, precition);
                    }
                    else {
                        toData = addCommas(value);
                    }
                    htmlstr += "<td align='right'>" + toData + "</td></tr>";
                }
            }
            htmlstr += "</table>";
            $("#tabDiv").html(htmlstr);
            $("#legendchart").show();
        }
    }
    function transition(plane, route) {
        var l = route.node().getTotalLength();
        plane.transition()
                .duration(l * 800)
                .attrTween("transform", delta(plane, route.node()));
    }

    function delta(plane, path) {
        var l = path.getTotalLength();
        return function(i) {
            return function(t) {
                var p = path.getPointAtLength(t * l);

                var t2 = Math.min(t + 0.05, 1);
                var p2 = path.getPointAtLength(t2 * l);

                var x = p2.x - p.x;
                var y = p2.y - p.y;
                var r = 90 - Math.atan2(-y, x) * 180 / Math.PI;

                var s = Math.min(Math.sin(Math.PI * t) * 0.7, 0.3);

                return "translate(" + p.x + "," + p.y + ") scale(" + s + ") rotate(" + r + ")";
            };
        };
    }
    function fly(origin, destination) {
        if (typeof dataMap[origin] != "undefined" && typeof dataMap[origin] != "" && typeof dataMap[destination] != "undefined" && typeof dataMap[destination] != "") {
            var route = g.append("path")
                    .datum({type: "LineString", coordinates: [dataMap[origin], dataMap[destination]]})
                    .attr("class", "route")
                    .attr("d", path);

            var plane = g.append("path")
                    .attr("class", "plane")
                    .attr("fill", "yellow")
                    .attr("stroke", "red")
                    .attr("transform", "translate(-100, -100)")
                    .attr("d", "m25.21488,3.93375c-0.44355,0 -0.84275,0.18332 -1.17933,0.51592c-0.33397,0.33267 -0.61055,0.80884 -0.84275,1.40377c-0.45922,1.18911 -0.74362,2.85964 -0.89755,4.86085c-0.15655,1.99729 -0.18263,4.32223 -0.11741,6.81118c-5.51835,2.26427 -16.7116,6.93857 -17.60916,7.98223c-1.19759,1.38937 -0.81143,2.98095 -0.32874,4.03902l18.39971,-3.74549c0.38616,4.88048 0.94192,9.7138 1.42461,13.50099c-1.80032,0.52703 -5.1609,1.56679 -5.85232,2.21255c-0.95496,0.88711 -0.95496,3.75718 -0.95496,3.75718l7.53,-0.61316c0.17743,1.23545 0.28701,1.95767 0.28701,1.95767l0.01304,0.06557l0.06002,0l0.13829,0l0.0574,0l0.01043,-0.06557c0,0 0.11218,-0.72222 0.28961,-1.95767l7.53164,0.61316c0,0 0,-2.87006 -0.95496,-3.75718c-0.69044,-0.64577 -4.05363,-1.68813 -5.85133,-2.21516c0.48009,-3.77545 1.03061,-8.58921 1.42198,-13.45404l18.18207,3.70115c0.48009,-1.05806 0.86881,-2.64965 -0.32617,-4.03902c-0.88969,-1.03062 -11.81147,-5.60054 -17.39409,-7.89352c0.06524,-2.52287 0.04175,-4.88024 -0.1148,-6.89989l0,-0.00476c-0.15655,-1.99844 -0.44094,-3.6683 -0.90277,-4.8561c-0.22699,-0.59493 -0.50356,-1.07111 -0.83754,-1.40377c-0.33658,-0.3326 -0.73578,-0.51592 -1.18194,-0.51592l0,0l-0.00001,0l0,0z")
                    .attr("stroke", "red");
            transition(plane, route);
        }
    }
    function showUp() {
        svg.selectAll(".place1").attr("d", "0");
//        for(var h=0;h<data.length;h++){
//             fly(data[h][columns[0]].toLowerCase(),data[h][columns[1]].toLowerCase());
//    var name=svg.selectAll("#"+data[h][columns[0]]);
//                    var name1=svg.selectAll("#"+data[h][columns[1]]);
//                    var sArr=name.attr("d").toString().split(",");
//                        var dArr=name1.attr("d").toString().split(",");
//                        var x1=sArr[0].replace("M","","gi");
//                        var y1=sArr[1].replace("m","","gi");
//                        var x2=dArr[0].replace("M","","gi");
//                        var y2=dArr[1].replace("m","","gi");
//                        var angles = [0,45,90,135];
//                        var g1 = g.append("svg:line")
//                        .attr("x1", x1)
//                        .attr("y1", y1)
//                        .attr("x2", x2)
//                        .attr("y2", y2)
//                        .attr("class", "all_lines")
//                        .attr("color_value", h)
//                        .attr("index_value", "index_"+h)
//                        .attr("id", data[h][columns[0]].toLowerCase()+"_line")
//                        .style("display", "none")
//                        .transition()
//                        .duration(200)
//                        .style("stroke", "green");
//                .attr("d", function(d) {
//                var dx = x2 - x1,
//                dy = y2 - y1,
//                dr = Math.sqrt(dx * dx + dy * dy);
//                return "M" + x1 + "," + y1 + "A" + dr + "," + dr + " 0 0,1 " + x2 + "," + y2;
//                })
//                        .attr("refx", 10)
//                        .attr("refy", 10)
//                        .attr("fill", "green")
//                        .attr("class", "lineSegment")

//                             .on("mouseover",
//                     function(){ return getDetails();})

//                var origion = {};
//            var destination = {};
//            var lineData = [];
//            var x = d3.scale.ordinal().rangePoints([0, width], .2);
//            var y = d3.scale.linear().rangeRound([height, 0]);
//            origion["x"] = x1;
//            origion["y"] = y1;
//            destination["x"] = x2;
//            destination["y"] = y2;
//            lineData.push(origion);
//            lineData.push(destination);
//            var linefunction = d3.svg.line()
//                    .x(function(d) {
//                        return d.x;
//                    })
//                    .y(function(d) {
//                        return d.y;
//                    })
//                    .interpolate("monotone");
//            g.append("path")
//                    .attr("d", linefunction(lineData))
//                    .attr("stroke-width", 2)
//                    .style("fill", "blue")
//                    .style("stroke", "blue");

//        var valueline = d3.svg.area().interpolate("monotone")
//            .x(function(d) {
//                return x(d.x);
//            })
//            .y(function(d) {
//                return y(d.y);
//            });

//   var plane = g1.append("path")
//                   .attr("class", "plane")
//                   .attr("d", "m25.21488,3.93375c-0.44355,0 -0.84275,0.18332 -1.17933,0.51592c-0.33397,0.33267 -0.61055,0.80884 -0.84275,1.40377c-0.45922,1.18911 -0.74362,2.85964 -0.89755,4.86085c-0.15655,1.99729 -0.18263,4.32223 -0.11741,6.81118c-5.51835,2.26427 -16.7116,6.93857 -17.60916,7.98223c-1.19759,1.38937 -0.81143,2.98095 -0.32874,4.03902l18.39971,-3.74549c0.38616,4.88048 0.94192,9.7138 1.42461,13.50099c-1.80032,0.52703 -5.1609,1.56679 -5.85232,2.21255c-0.95496,0.88711 -0.95496,3.75718 -0.95496,3.75718l7.53,-0.61316c0.17743,1.23545 0.28701,1.95767 0.28701,1.95767l0.01304,0.06557l0.06002,0l0.13829,0l0.0574,0l0.01043,-0.06557c0,0 0.11218,-0.72222 0.28961,-1.95767l7.53164,0.61316c0,0 0,-2.87006 -0.95496,-3.75718c-0.69044,-0.64577 -4.05363,-1.68813 -5.85133,-2.21516c0.48009,-3.77545 1.03061,-8.58921 1.42198,-13.45404l18.18207,3.70115c0.48009,-1.05806 0.86881,-2.64965 -0.32617,-4.03902c-0.88969,-1.03062 -11.81147,-5.60054 -17.39409,-7.89352c0.06524,-2.52287 0.04175,-4.88024 -0.1148,-6.89989l0,-0.00476c-0.15655,-1.99844 -0.44094,-3.6683 -0.90277,-4.8561c-0.22699,-0.59493 -0.50356,-1.07111 -0.83754,-1.40377c-0.33658,-0.3326 -0.73578,-0.51592 -1.18194,-0.51592l0,0l-0.00001,0l0,0z");


//            }

        function getDetails() {
            var bar = d3.select(this);
            var col = bar.attr("index_value").replace("index_", "", "gi");
            var content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + data[col][columns[0]] + "</span><br/>";
            content += "<span class=\"name\">" + columns[1] + ": </span><span class=\"value\"> " + data[col][columns[1]] + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[col][measureArray[0]] + "</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        }
    }
    var zoom = d3.behavior.zoom()
            .on("zoom", function() {
                g.attr("transform", "translate(" +
                        d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
                g.selectAll("path")
                        .attr("d", path.projection(projection));

                svg.selectAll(".place1").attr("d", "0");
            });
    svg.call(zoom);
    d3.select(self.frameElement).style("height", height + "px");
}

function buildMultiViewWordCloud(div,repData,columns, measureArray,colIds) {
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
    } ;
    
//    "chart1":[{"Auto Type":"CON","Quantity":"211171.0"},{"Auto Type"
//     repData ={"chart1":[{"Auto Type":"KIT","Amount":"100.0"},
//        {"Auto Type":"Clustering","Amount":"100.0"},
//        {"Auto Type":"Segmentation","Amount":"100.0"},
//        {"Auto Type":"Market Basket Analysis","Amount":"105.0"},
//        {"Auto Type":"Churn Analysis","Amount":"102.0"},
//        {"Auto Type":"Factor Analysis","Amount":"103.0"},
//        {"Auto Type":"Logistic Regression","Amount":"100.0"},
//        {"Auto Type":"Classification & Regression Tree","Amount":"107.0"},
//        {"Auto Type":"Multivariate Regression","Amount":"106.0"},
//        {"Auto Type":"Covariance Analysis","Amount":"102.0"},
//        {"Auto Type":"Basic Time Series Analysis","Amount":"109.0"},
//        {"Auto Type":"Advanced Time Series Analysis","Amount":"108.0"},
//        {"Auto Type":"ARIMA","Amount":"103.0"},
//        {"Auto Type":"Principle Component Analysis","Amount":"105.0"},
//        {"Auto Type":"ANOVA","Amount":"106.0"},
//        {"Auto Type":"CHAID","Amount":"108.0"},
//        {"Auto Type":"Hidden Markov Model","Amount":"107.0"},
//        {"Auto Type":"RFM Analysis","Amount":"103.0"},
//        {"Auto Type":"Discriminant Analysis","Amount":"105.0"},
//        {"Auto Type":"Risk Analytics","Amount":"106.0"},
//        {"Auto Type":"Neural Network","Amount":"108.0"},
//        {"Auto Type":"Latent Structure Analysis","Amount":"107.0"},
//        {"Auto Type":"Loyalty Scoring Analysis","Amount":"103.0"},
//        {"Auto Type":"Canonical Analysis","Amount":"108.0"},
//        {"Auto Type":"Anomaly Detection","Amount":"110.0"},
//        {"Auto Type":"Relevance Vector Machine","Amount":"107.0"},
//        {"Auto Type":"Bayesian Analysis","Amount":"106.0"},
//    
//]};
    measureArray=measureArray.toString().split(",");
    var isDashboard = parent.$("#isDashboard").val();
//    var fun = "drillKPIchart(this.id)";
//    
    //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
             fun = "drillKPIchart(this.id)";
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
	}function drillFunct(id1){
         if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
            drillChartInDashBoard(id1,div);
    } else {
       drillKPIchart(id1,div);
    }
    }
//    
//    var repData = JSON.parse(parent.reportData);
   
    var wid = $(window).width()*.83;
    var hgt = $(window).height();
    d3.select("#"+div).append("svg")
            .attr("width", wid)
            .attr("height", hgt)
            .attr("style", "margin-top:0px")
            .attr("id", "first");
    var max_amount;
    var fill = d3.scale.category12();
    d3.loadData = function() {
        var loadedCallback = null;
        var toload = {};
            var data = {};
        var loaded = function(name, d) {
            delete toload[name];
            data[name] = d;
            return notifyIfAll();
        };
        var notifyIfAll = function() {
            if ((loadedCallback !== null) && d3.keys(toload).length === 0) {
                loadedCallback(data);
            }
        };
        var loader = {
            json: function(name, url) {
                toload[name] = url;
                d3.json(url, function(d) {
                    return loaded(name, d);
                });
                return loader;
            },
            csv: function(name, url) {
                toload[name] = url;
                d3.csv(url, function(d) {
                    return loaded(name, d);
                });
                return loader;
            },
            onload: function(callback) {
                loadedCallback = callback;
                notifyIfAll();
            }
        };
        return loader;
    };
    var minMaxMap = {};
    var gdata = [];
    var count = 0;
    var limit = 500 / columns.length;
    for (var i = 0; i < columns.length; i++) {
        var data = {};
        data[columns[i]] = repData["chart"+parseFloat(parseFloat(i)+1)];
        for (var d in data) {
            var json = {};
            json["min"] = minimumValue(data[d], measureArray[0]);
            json["max"] = maximumValue(data[d], measureArray[0]);
            if (json["max"] === json["min"]) {
                json["min"] = 0;
            }
            minMaxMap[d] = json;
            for (var id in data[d]) {
//                if (true) {
                if (count < limit) {
                    gdata.push(data[d][id]);
                    count++;
                } else {
                    count = 0;
                    break;
                }
            }
        }
        count = 0;
    }
    setTimeout(function() {
        cloudData(gdata, "V0");
    }, 0);
    function cloudData(data, key) {
        max_amount = maximumValue(data, measureArray[0]);
        var digonal = Math.sqrt(wid * wid + hgt * hgt);
        var maxSize = digonal * .047;
        var minSize = maxSize * .2;
        this.radius_scale = {};
        for (var i = 0; i < columns.length; i++) {
            this.radius_scale[columns[i]] = d3.scale.pow().exponent(0.5).domain([minMaxMap[columns[i]]["min"], minMaxMap[columns[i]]["max"]]).range([minSize, maxSize]);//minMaxMap[columns[i]]["min"]
        }
//        var data = [];
//        var data2 = JSON.parse(parent.reportData);
//        for (key in data2) {
//            var tempData = data2[key];
//            for (var m = 0; m < tempData.length; m++) {
//                data.push(tempData[m]);
//            }
//        }
        d3.layout.cloud().size([wid, hgt])
                .words(data
                        .map(function(data) {
                            var keys = Object.keys(data);
                            var viewVal;
                            var viewIdVal;
                            var map = {};
                            for (var j = 0; j < keys.length; j++) {
                                for (num in columns) {
                                    if (keys[j] === columns[num]) {
                                        viewVal = columns[num];
                                        viewIdVal = colIds[num];
                                    }
                                }
                            }
                            for (var l = 0; l < columns.length; l++) {
                                map["text"] = data[viewVal];
                                map["viewby"] = viewVal;
                                map["viewId"] = viewIdVal;
                                var a = this.radius_scale[viewVal](parseFloat(data[measureArray[0]]));
                                map["size"] = a < 0 ? 2 : a;
                                for (var no = 0; no < measureArray.length; no++) {
                                    map["value" + no] = data[measureArray[no]];
                                }
                            }
                            return map;
                        }))
                .padding(1)
                .rotate(function(d, i) {
                    if (i % 2 === 0) {
                        return 0;
                    } else {
                        return -90;
                    }
                })
                .font("Impact")
                .fontSize(function(d) {
                    return d.size;
                })
                .on("end", draw)
                .start();
        function draw(words) {
            colorMap = {};
            var chartMap = {};
            d3.select("#first").append("svg")
                    .attr("width", wid)
                    .attr("height", hgt)
                    .append("g")
                    .attr("transform", "translate(" + wid / 2 + "," + hgt / 2 + ")")
                    .selectAll("text")
                    .data(words)
                    .enter().append("text")
                    .style("font-size", function(d) {
                        return (d.size) + "px";
                    })
                    .style("font-family", "Impact")
                    .style("fill", function(k, i) {
                        var colorShad;
                        var drilledvalue;// = parent.$("#drills").val().split(",");
                        try{
                            var drilledvalueMap=JSON.parse(parent.$("#drills").val());
                          drilledvalue=drilledvalueMap[k["viewId"]];
                        }catch(e){}
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(k.text) !== -1) {
                            colorShad = drillShade;
                        } else {
                            colorShad = fill(i);
                        }
//                        if (typeof chartMap[k.text] === "undefined") {
//                            colorMap[i] = k.text + "__" + colorShad;
//                            chartMap[k.text] = colorShad;
//                        }
                        return colorShad;
                    })
//                    .attr("color_value", function(k, i) {
//                        var colorShad;
//                        var drilledvalue = parent.$("#wordclouddrilledValue").val().split(",");
//                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(k.text) !== -1) {
//                            colorShad = drillShade;
//                        } else {
//                            colorShad = fill(i);
//                        }
//                        return colorShad;
//                    })
                    .attr("text-anchor", "middle")
                    .attr("id", function(d) {
                        return colIds[columns.indexOf(d.viewby)] + ":" + d.text;
                    })
                    .attr("onclick", fun)
            
	//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            
                    .on("mouseover", function(d, i) {
                        var circle = d3.select(this);
                        circle.transition()
                                .duration(100).style("opacity", 1).style("z-index", -1).style("position", "absolute")
                                .style("font-size", function(d) {
                                    if (parseFloat(d.size) < 0) {
                                        return 20 + "px";
                                    } else {
                                        return d.size + 20 + "px";
                                    }
                                });
                        var content = "";
                        var msrData;
                        content += "<span class=\"name\">" + d.viewby + ":</span><span class=\"value\"> " + d.text + "</span><br/>";
                        for (var no = 0; no < measureArray.length; no++) {
                            if (isFormatedMeasure) {
                                msrData = numberFormat(d["value" + no], round, precition);
                            }
                            else {
                                msrData = addCommas(d["value" + no]);
                            }
                            content += "<span class=\"name\">" + measureArray[no] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                        }
                        return tooltip.showTooltip(content, d3.event);
                    })
                    .on("mouseout", function(d) {
                        var circle = d3.select(this);
                        circle.transition()
                                .style("font-size", function(d) {
                                    return d.size + "px";
                                });
                        hide_details(d, i, this);
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.text.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
                    .attr("transform", function(d) {
                        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                    })
                    .text(function(d) {
                        return d.text;
                    });
//            parent.$("#colorMap").val(JSON.stringify(colorMap));
        }
    }

}

//radial chart
function radialProgressBar(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
 var DataSum = 0;
 var showData = 0;
 var specialCharacter ="%";
 var perFlag = "";
 var textsizevalue= 0;
 var textxvalue= 0;
 var chartData = JSON.parse(parent.$("#chartData").val());
 var divSize = 0 ;
 var absoluteNumber = 0;
 var KPIResult1 = 0;
 var colorpicker ="";
 var labelMeasure = "";
 var nameArrCurrent = "";
 var nameArrPrior = "";
if(divWidth>divHeight){
 divSize =  divHeight;
}else{
    divSize =divWidth;
}
  var fromoneview=parent.$("#fromoneview").val()
  if(typeof chartData[div]["MaxRange"]!=="undefined" && chartData[div]["MaxRange"] !=""){
    KPIResult1 = (KPIResult/chartData[div]["MaxRange"])*100;

  }else{
   KPIResult1 =  KPIResult;
  }
 if (measureArray[0].indexOf(specialCharacter)!=-1 ||measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1) {

   showData = parseFloat(KPIResult1);
      perFlag = "false";
      absoluteNumber= parseFloat(KPIResult);
   }else{
     showData = 100;
      perFlag = "false";
      absoluteNumber= parseFloat(KPIResult);

 }
 if(showData<0){
   showData = showData*(-1);

 }
//
// if(absoluteNumber.toString().length>6 && perFlag == "false" ){
//  textsizevalue = parseInt(divSize*.08)
//  textxvalue =  parseInt(divSize*.5*.1);
//
// }else{
//   textsizevalue = parseInt(divSize*.09)
//   textxvalue =  parseInt(divSize*.5*.3);
// }

 if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== ''){
     textsizevalue=chartData[div]["kpiGTFont"];
 }else{
 if(absoluteNumber.toString().length>6 && perFlag == "false" ){
  textsizevalue = parseInt(divSize*.08)
     }else {
         textsizevalue = parseInt(divSize*.09)
     }
 }
 chartData[div]["kpiGTFont"]=textsizevalue;
 
  if(typeof chartData[div]["kpiFont"]!=="undefined" && chartData[div]["kpiFont"] !== ''){
     textxvalue=chartData[div]["kpiFont"];
 }else {
     if(absoluteNumber.toString().length>6 && perFlag == "false" ){
  textxvalue =  parseInt(divSize*.5*.1);
     }else {
   textxvalue =  parseInt(divSize*.5*.3);
 }
 }
 chartData[div]["kpiFont"]=textxvalue;
 $("#chartData").val(JSON.stringify(chartData));

var maximumvalue = parseInt(maximumValue(data, measureArray[0]));
var minimumvalue = parseInt(minimumValue(data, measureArray[0]));
var minValue= 0;
var maxValue= 100;

var dashledid=div;
 var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
         dashledid=div;
         div=div1;
     }
     
var tableData = {};
tableData[div] = data;
     
chartData[div]["TargetMax"] = maximumvalue;
chartData[div]["TargetMin"] = minimumvalue;

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
 labelMeasure = chartData[div]["KPIName"];
 if(typeof chartData[div]["Target"]!="undefined" && chartData[div]["Target"]!=""){
//        minValue = chartData[div]["TargetMin"],
//        maxValue = chartData[div]["TargetMax"];
        showData = parseFloat((absoluteNumber/chartData[div]["Target"])*100);
        perFlag = "true";
}
if(typeof chartData[div]["colorPicker"]!=="undefined" && chartData[div]["colorPicker"]!==""){
 colorpicker = chartData[div]["colorPicker"];

}else{
    if(absoluteNumber>0){
   colorpicker = "#38d976";

}else{
  colorpicker = "#eb1337";
}

}
if(fromoneview!='null'&& fromoneview=='true'){
         div=dashledid;
     }
//       $("#" + div).html(div);
    
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
var rp1 = radialProgress(document.getElementById(div),divWidth,divHeight,perFlag,textsizevalue,minValue,maxValue,measureArray,textxvalue,divSize,absoluteNumber,colorpicker,labelMeasure,div)
                .label(chartData[div]["KPIName"])
                .diameter(divWidth*1.2)
                .value(showData)
                .render();


}
function radialProgress(parentdiv,divWidth,divHeight,perFlag,textsizevalue,minValue,maxValue,measureArray,textxvalue,divSize,absoluteNumber,colorpicker,labelMeasure,div) {
var chartData = JSON.parse(parent.$("#chartData").val());
 var radius = (Math.min(parseInt(divWidth), parseInt(divHeight))/2);
 var   config = liquidFillGaugeDefaultSettings();
 var circleThickness = config.circleThickness * radius;
    var circleFillGap = config.circleFillGap * radius;
    var fillCircleMargin = circleThickness + circleFillGap;
    var fillCircleRadius = radius - fillCircleMargin;
     var textPixels = (config.textSize*radius/2);
    var _data=null,
    _duration= 1000,
        _selection,
        _margin = {top:0, right:0, bottom:30, left:25},
        __width = divWidth,
        __height = divHeight-50,
        _diameter,
        _label="",
        _fontSize=0;
        var labelxvalue = 0;
        var labelyvalue = 0;
        var labelsizevalue = parseInt(divSize*.08)
        var labelSubstring1 = "";
        var labelSubstring2 = "";
        var labelxvalue1 = 0;
        var labelyvalue1 = 0;
        var textVertPosition = .5;
       if(labelMeasure.toString().length>11 && labelMeasure.length<20 ){

           labelxvalue =  parseInt(divSize*.01);
           labelyvalue =  parseInt(divSize*.5*1.5);
       }else if(labelMeasure.toString().length>19){
         var labelsizevalue = parseInt(divSize*.06)
       labelSubstring1 =  labelMeasure.substring(0,16);
       labelSubstring2 =  labelMeasure.substring(17,labelMeasure.toString().length);
       if(labelSubstring2.toString().length<10){

           labelxvalue1 =  parseInt(divSize*.05);
         labelyvalue1 =  parseInt(divSize*.5*1.65);
       }else{
//
           labelxvalue1 =  parseInt(divSize*.01);
         labelyvalue1 =  parseInt(divSize*.5*1.65);
       }
         labelxvalue =  parseInt(divSize*.01);
         labelyvalue =  parseInt(divSize*.5*1.5);
//
//
//            var labelxvalue1 = 0;
//            labelxvalue1 =  parseInt(divSize*.09);
//           labelyvalue =  parseInt(divSize*.5*1.5);
//
//        labelxvalue = -(labelxvalue1);
       }
       else{

        labelxvalue =  parseInt(divSize*.09);
        labelyvalue =  parseInt(divSize*.5*1.5);
       }
    var _mouseClick;

    var _value= 0,
      _minValue = minValue,
      _maxValue = maxValue;

    var  _currentArc= 0, _currentArc2= 0, _currentValue=0;

    var _arc = d3.svg.arc()
        .startAngle(0 * (Math.PI/180)); //just radians
var radialDefault="";

 var margin = {
        top: 20,
        right: 80,
        bottom: 70,
        left: 80
    },
//    var _arc2 = d3.svg.arc()
//        .startAngle(0 * (Math.PI/180))
//        .endAngle(0); //just radians


    _selection=d3.select(parentdiv);

 var textRiseScaleY = d3.scale.linear()
        .range([fillCircleMargin+fillCircleRadius*2,(fillCircleMargin+textPixels*0.7)])
        .domain([0,1]);
    function component() {

        _selection.each(function (data) {

            // Select the svg element, if it exists.
            var svg = d3.select(this).selectAll("svg").data([data]);

            var enter = svg.enter().append("svg").attr("class","radial-svg").append("g")

            measure();

            svg.attr("width", "100%")
                .attr("height", "100%" )
                 .attr("id", "svg_" + div)
                .attr("transform", function(d){
                      if(window.chrome){   
                       return "translate(" + divWidth*.12 + "," + divSize*.07 + ")" ;
                      }else{   
                         return "translate(" + divWidth*.27 + "," + divSize*.07 + ")" ;
                      }
                })

            var background = enter.append("g").attr("class","component")
                .attr("cursor","pointer")
                .on("click",onMouseClick);


            _arc.endAngle(360 * (Math.PI/180))

//            background.append("rect")
//                .attr("class","background")
//                .attr("width", divSize*.9)
//                .attr("height", divSize*.9)

            background.append("path")
                .attr("transform", "translate(" + divSize*.3 + "," + divSize*.6/2 + ")")
                .attr("d", _arc)
                 .style("fill", function(d, i){
//                 .style("fill", function(d, i){
                  if(typeof chartData[div]["radialDefaultColor"]!=="undefined" && chartData[div]["radialDefaultColor"]!=""){
                      radialDefault = chartData[div]["radialDefaultColor"];
                  }else{
                       radialDefault="#99CCFF";
                  }
                  return radialDefault;
              });

//       if(labelMeasure.toString().length>19){
//          background.append("text")
//                .attr("class", "label")
//                .attr("transform", "translate(" +(labelxvalue) + "," + labelyvalue + ")")
//                 .style("font-size",labelsizevalue+"px")
//
//                 .style("font-family","Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif")
//                .text(labelSubstring1);
//
//            background.append("text")
//                .attr("class", "label")
//                .attr("transform", "translate(" +(labelxvalue1) + "," + labelyvalue1 + ")")
//                 .style("font-size",labelsizevalue+"px")
//
//                 .style("font-family","Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif")
//                .text(labelSubstring2);
//       }else{
//          background.append("text")
//                .attr("class", "label")
//                .attr("transform", "translate(" +(labelxvalue) + "," + labelyvalue + ")")
//                 .style("font-size",labelsizevalue+"px")
//
//                 .style("font-family","Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif")
//                .text(_label);
//       }


           var g = svg.select("g")
                .attr("transform", function(d){
                     if(window.chrome){   
                     return "translate(" + divSize*.51 + "," + divSize*.13 + ")";
                     }else{   
                     return "translate(" + divSize*.03 + "," + divSize*.06 + ")";   
                     }
                })
                
             
            _arc.endAngle(_currentArc);
        //    enter.append("g").attr("class", "arcs") .attr("style", "stroke-weight: 0.1;fill: steelblue;");
 //Added by Ram
         enter.append("g").attr("class", "arcs") .attr("style", "stroke-weight: 0.1;fill: "+colorpicker+";");
            var path = svg.select(".arcs").selectAll(".arc").data(data);
            path.enter().append("path")
                .attr("class","arc")
                .attr("style", "stroke-weight: 0.1;fill:"+colorpicker+" ;")
                .attr("style"," transition:bounce:out")
                .attr("transform", "translate(" + divSize*.3 + "," + divSize*.6/2 + ")")
                .attr("d", _arc);

            //Another path in case we exceed 100%
//            var path2 = svg.select(".arcs").selectAll(".arc2").data(data);
//            path2.enter().append("path")
//                .attr("class","arc2")
//                .attr("transform", "translate(" + _width/2 + "," + _width/2 + ")")
//                .attr("d", _arc2);


            enter.append("g").attr("class", "labels");
            var label = svg.select(".labels").selectAll(".label").data(data);
            label.enter().append("text")
                .attr("class","label")
//                .attr("y",divSize*.28+_fontSize/3)
//                .attr("x",textxvalue)

                .attr("cursor","pointer")
                .attr("text-anchor", "middle")
                // .attr("x",(3*_fontSize/2))
                .text(function (d) {return Math.round((_value-_minValue)/(_maxValue-_minValue)*100) + "%"})
               
                .style("font-size",textsizevalue+"px")
                .style("font-family","Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif")
                .style("font-weight","Bold")
                .style("fill", function(d, i) {
                var lFilledFont="";
                  if(typeof chartData[div]["lFilledFont"]!=="undefined" && chartData[div]["lFilledFont"] !=""){
                         lFilledFont = chartData[div]["lFilledFont"];
                    }else{
                       return "#ECE334";
                    } return lFilledFont; })
                .attr('transform','translate('+radius*.55+','+textRiseScaleY(textVertPosition)*.6+')')
                .on("click",onMouseClick)
                .on("mouseover", function(d, i) {
                show_detailsKPI(d, chartData[div]["viewBys"], measureArray, this,div,absoluteNumber);
                }
            ).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

            path.exit().transition().duration(500).attr("x",1000).remove();


            layout(svg);

            function layout(svg) {

                var ratio=(_value-_minValue)/(_maxValue-_minValue);
                var endAngle=Math.min(360*ratio,360);
                endAngle=endAngle * Math.PI/180;

                path.datum(endAngle);
                path.transition().duration(_duration)
                    .attrTween("d", arcTween);

                if (ratio > 1) {
//                    path2.datum(Math.min(360*(ratio-1),360) * Math.PI/180);
//                    path2.transition().delay(_duration).duration(_duration)
//                        .attrTween("d", arcTween2);
                }

                label.datum(Math.round(ratio*100));
                label.transition().duration(_duration)
                    .tween("text",labelTween);

            }

        });

        function onMouseClick(d) {
            if (typeof _mouseClick == "function") {
                _mouseClick.call();
            }
             if (typeof  openTableDiv == "function") {
                 openTableDiv.call();
                
        }
    }
    }

    function labelTween(a) {
        var i = d3.interpolate(_currentValue, a);
        _currentValue = i(0);

        return function(t) {

            _currentValue = i(t);
            if(perFlag == "true"){
               this.textContent = Math.round(i(t)) + "%";
            }else{
                this.textContent = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(Math.round(absoluteNumber)) + "";

            }

        }
    }

    function arcTween(a) {
        var i = d3.interpolate(_currentArc, a);

        return function(t) {
            _currentArc=i(t);
            return _arc.endAngle(i(t))();
        };
    }

    function arcTween2(a) {
        var i = d3.interpolate(_currentArc2, a);

        return function(t) {
      //      return _arc2.endAngle(i(t))();
        };
    }


    function measure() {
        _width=_diameter - _margin.right - _margin.left - _margin.top - _margin.bottom;
        _height=_width;
        _fontSize=_width*.2;
        _arc.outerRadius(divSize*.6/1.7);
        _arc.innerRadius(divSize*.6/2 *1);
//        _arc2.outerRadius(_width/2 * .85);
//        _arc2.innerRadius(_width/2 * .85 - (_width/2 * .15));
    }


    component.render = function() {
        measure();
        component();
        return component;
    }

    component.value = function (_) {
        if (!arguments.length) return _value;
        _value = [_];
        _selection.datum([_value]);
        return component;
    }


    component.margin = function(_) {
        if (!arguments.length) return _margin;
        _margin = _;
        return component;
    };

    component.diameter = function(_) {
        if (!arguments.length) return _diameter
        _diameter =  _;
        return component;
    };

    component.minValue = function(_) {
        if (!arguments.length) return _minValue;
        _minValue = _;
        return component;
    };

    component.maxValue = function(_) {
        if (!arguments.length) return _maxValue;
        _maxValue = _;
        return component;
    };

    component.label = function(_) {
        if (!arguments.length) return _label;
        _label = _;
        return component;
    };

    component._duration = function(_) {
        if (!arguments.length) return _duration;
        _duration = _;
        return component;
    };

    component.onClick = function (_) {
        if (!arguments.length) return _mouseClick;
        _mouseClick=_;
        return component;
    }

    return component;

}
//function tileCharts(div, data, columns, measureArray, divWidth, divHeight){
//    var tableData = data;
//   var chartData = JSON.parse(parent.$("#chartData").val());
//   var showData = 0;
//   var showData1 = 0;
//   var absoluteNumber = 0;
//   var perFlag = "";
//    var specialCharacter ="%";
//    var colorPicker = "";
//   var DataSum = 0;
//   //font size start
//   var gtFontSize = 0;
//   var infoFontSize = 0;
//   //font size end
//
//   for(var i in data){
//
// DataSum+= parseFloat(data[i][measureArray[0]]);
// if (measureArray[0].indexOf(specialCharacter)!=-1) {
//
//  showData =   parseFloat(DataSum/(data.length));
//
//  perFlag = "true";
//}else{
//     showData = parseFloat(DataSum);
//      perFlag = "false";
//      absoluteNumber= DataSum;
// }
//   }
//   var yAxisFormat = "";
//   var yAxisRounding = 0;
//  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
//      yAxisFormat = chartData[div]["yAxisFormat"];
//  }else{
//   yAxisFormat = "";
//  }
//  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
//      yAxisRounding = chartData[div]["rounding"];
//  }else{
//   yAxisRounding =0;
//  }
//
//                  if(yAxisFormat==""){
//
//                    showData1 =      addCommas(numberFormat1(showData,yAxisFormat,yAxisRounding,div));
//                    }
//            else{
//             showData1 =        numberFormat1(showData,yAxisFormat,yAxisRounding,div);
//                }
//
//
//if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
//
//for(var i in measureArray){
//    if(chartData[div]["KPIName"]!=measureArray[i]){
//}else{
//     chartData[div]["KPIName"] = measureArray[0];
//}
//}}
//else{
//
//chartData[div]["KPIName"] = measureArray[0];
//
//}
//if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
//  colorPicker = chartData[div]["colorPicker"];
//}else{
//    colorPicker = "#36C";
//}
//if(divHeight<divWidth){
//  gtFontSize = divHeight*.13
//  infoFontSize = divHeight*.068
//}else{
//   gtFontSize = divWidth*.1
//    infoFontSize = divWidth*.058
//}
//
//parent.$("#chartData").val(JSON.stringify(chartData));
//  var html = "";
//  html+="<div id='mainDiv"+div+"' style='width:95%;height:90%;  background-color:#FFF'>";
////  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
////  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";
//  html+="<div id='innerDiv"+div+"' style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
//  html+="<table style='float:left;margin-top: '>";
//  html+="<tr>";
//
//
//   html+="<td onclick='openTableDiv()' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1+"</span></td>";
//
//  html+="</tr>";
//  html+="<tr>";
//
//  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[div]["KPIName"]+"</span></td>";
//  }else{
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+measureArray[0]+"</span></td>";
//  }
//  html+="</tr>";
//  html+="</table>";
//  html+="</div>";
//  html+="</div>";
//
//  $("#"+div).html(html)
// openTableDiv = function(){
//
//        $("#openTableTile").dialog({
//         autoOpen: false,
//         height: 440,
//         width: 620,
//         position: 'justify',
//         modal: true,
//         resizable:true,
//        title:'Graph Properties'
//    });
//   $("#openTableTile").html("");
//
// //  $("#openTableTile").dialog('open');
//   var htmlstr = "";
//   htmlstr += "<table>";
//
//   htmlstr += "<tr>";
//
//   htmlstr += "<td>"+columns+"</td>";
//
//   htmlstr += "<td>"+measureArray[0]+"</td>";
//   htmlstr += "</tr>";
//
//   for(var k in tableData){
//   htmlstr += "<tr>";
//   for(var j in columns){
//
//   htmlstr += "<td>"+tableData[k][columns[j]]+"</td>";
//}
//   htmlstr += "<td>"+tableData[k][measureArray[0]]+"</td>";
//
//htmlstr += "</tr>";
// }
//   htmlstr += "</table>";
//
//$("#openTableTile").append(htmlstr)
//$("#openTableTile").dialog('open');
// }
//}
function fontSizeIncrease(fontSize,div){
$("#"+div+"span").css("font-size",fontSize*1.4+"px")

$("#mainDiv"+div).css("background-color","#e6e6e6")
$("#innerDiv"+div).css("background-color","#e6e6e6")
$("#innerDiv1"+div).css("background-color","#e6e6e6")
$("#innerDiv2"+div).css("background-color","#e6e6e6")
$("#innerDiv3"+div).css("background-color","#e6e6e6")
$("#"+div+"span").css("text-decoration","underline")
//$("#innerDiv3"+div).css("background-color","#62f0cf")


}
function fontSizeDecrease(fontSize,div){
   $("#mainDiv"+div).css("background-color","#fff")
$("#innerDiv"+div).css("background-color","#fff")
$("#innerDiv1"+div).css("background-color","#fff")
$("#innerDiv2"+div).css("background-color","#fff")
$("#innerDiv3"+div).css("background-color","#fff")
$("#"+div+"span").css("text-decoration","none")
$("#"+div+"span").css("font-size",fontSize+"px")

}
function numberFormat1(value,symbol,precion,div){
    var chartData = JSON.parse(parent.$("#chartData").val());
    var  doubleVal = "";
    var num = parseFloat(value);
    var check="0.";
    for(var i=0;i<precion;i++){
        check += "0";
    }
    var temp = "";
    if (symbol=="K"){
        doubleVal = (num / getPowerOfTen(3));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
              if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!=""){
               temp = "<span style='font-size:"+chartData[div]["Prefixfontsize"]+"px'>"+chartData[div]["Prefix"]+ "</span>" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
              }
              }else{
            if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else  if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !=""){
               temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ "K";
            } else if(typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = setFormat(doubleVal,precion)+ ""+chartData[div]["Suffix"];
            }else{
              temp = setFormat(doubleVal,precion)+ "K";
            }
              }
        }
    }else if(symbol=="M"){
        doubleVal = (num / getPowerOfTen(6));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else  if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !=""){
               temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ "M";
            } else if(typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else{
              temp = setFormat(doubleVal,precion)+ "M";
            }

        }
    }else if(symbol=="" || symbol=="Absolute"){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else  if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !=""){
               temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion);
            } else if(typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else{
              temp = setFormat(doubleVal,precion);
            }

        }
    }else if(symbol=="L"){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else  if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !=""){
               temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ "L";
            } else if(typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else{
              temp = setFormat(doubleVal,precion)+ "L";
            }

        }
    }else if(symbol=="Cr"){
        doubleVal = (num / getPowerOfTen(7));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !="" && typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = chartData[div]["Prefix"]+ "" +setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else  if(typeof chartData[div]["Prefix"]!=="undefined" && chartData[div]["Prefix"] !=""){
               temp = chartData[div]["Prefix"]+ " " +setFormat(doubleVal,precion)+ "Cr";
            } else if(typeof chartData[div]["Suffix"]!=="undefined" && chartData[div]["Suffix"] !=""){
                temp = setFormat(doubleVal,precion)+ " "+chartData[div]["Suffix"];
            }else{
              temp = setFormat(doubleVal,precion)+ "Cr";
            }

        }
    }
    return temp;
}
//function buildTable(chartId,data, currdata, viewbys, measures, divwidth, divHght) {
//
//    var chartIdTile = chartId;
//     chartId = chartId.replace("Tile","");
//   var data0 = currdata;
//   var specialCharacter ="%";
//   var nameArrCurrent ="%";
//   var nameArrPrior ="";
//   var chartData = JSON.parse(parent.$("#chartData").val());
//   var  colIds = chartData[chartId]["viewIds"];
//   var k = chartId.replace("chart","");
//   var tableId = "table" + (parseInt(k) + 1);
//   var divHeight = $(window).height();
//        var trWidth = divwidth / 8;
//        divHght = divHght-50;
//        var html = "";
//        html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; overflow-y: auto;overflow-x: hidden\">";
//        html = html + "<div style='height:400px'>";
//        html = html + "<table id=\"chartTable" + chartId + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
////        html = html + "<tr><th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=\"25px\">S.no</td>";
//        html = html + "<tr style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><th  width=\"25px\"><strong>S.no</strong></th>";
////        html = html + "<th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=" + trWidth + ">" + viewbys[0] + "</td>";
//        html = html + "<th   width=" + trWidth + "><strong>" + viewbys[0] + "</strong></th>";
//         for(var j=0;j<measures.length;j++){
////        html = html + "<th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=" + trWidth + ">" + measures[j] + "</td>";
//        html = html + "<th   width=" + trWidth + "><strong>" + measures[j] + "</strong></th>";
//         }
//         data0.forEach(function(d, i) {
//            var num = (i + 1) % 2;
//            var color;
//            if (num === 0) {
//                color = "rgba(240, 245, 248, 1)";
//            } else {
//                color = "white";
//            }
//            //                  if(i<=6)
//            //                  {
//            html = html + "<tr><td  style=\"background-color:" + color + ";\" width=25px>" + (i + 1) + "</td>";
//             var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewbys[0]]) !== -1) {
//
//                           html = html + "<td id='"+d[viewbys[0]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[viewbys[0]] + "</td>";
//
//               }else{
//                               html = html + "<td id='"+d[viewbys[0]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[viewbys[0]] + "</u></td>";
//               }
//            for(var j=0;j<measures.length;j++){
//            html = html + "<td  style=\"background-color:" + color + ";text-align:right;color:black;font-size:smaller\" width=" + trWidth + ">" + addCommas(d[measures[j]]) + "</td>";
//            }
//
//            html = html + "</tr>";
//
//        });
////        html += "<tr style='background-color:#8C8C8C;color:black'>";
//        html += "<tr style='background-color:#BAB4B4;color:black'>";
//        html += "<td colspan='2' style=\"background-color:\" width=" + trWidth + "><strong>Sub Total</strong></td>";
//        for( var k=0;k< measures.length;k++){
//            var st=0;
//            var st1=0;
//            var changeMeasure="";
//            $.each(data0, function(d){
//                 st += parseFloat(data0[d][measures[k]]);
//                 st1 += parseFloat(data0[d][measures[k]]);
//                 if(measures[k].indexOf("Change%")!=-1){
//                changeMeasure = measures[k];
//                 }
//            });if(changeMeasure.indexOf("Change%")!=-1){
//    var current = 0;
//    var prior = 0;
//    var nameArr = [];
//
//    var nameArr2 = "";
//    nameArr =  changeMeasure.split(/[ ,]+/);
//
// for(var k =1;k<nameArr.length;k++){
// if(nameArr.length>2){
// if(k==1){
//     nameArr2 =  nameArr[k]
//   }else{
//     nameArrCurrent = nameArr2.concat(nameArr[k]);
//   }
// }else{
//    nameArrCurrent = nameArr[k]
// }
// }
// nameArrPrior = "Prior "+nameArrCurrent;
//for(var l in data0){
//
//    for(var p in measures){
//
//    if(nameArrCurrent.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().replace(" ", "")){
//
//     current += parseFloat(data0[l][measures[p]])
//   }
//   else if(nameArrPrior.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().replace(" ", "")){
//
//     prior += parseFloat(data0[l][measures[p]])
//   }
//    }
//  }
//
// if(current!="undefined" && current!="" && prior!="undefined" && prior!=""){
//     st = parseFloat(((current-prior)/prior)*100)
//
// }else{
//     st =   parseFloat(st1/(data0.length));
//
// }
// }
//else if (measures[k].indexOf(specialCharacter)!=-1 || measures[k].indexOf("Percent")!=-1 ||measures[k].indexOf("percent")!=-1 ||measures[k].indexOf("PERCENT")!=-1) {
//                st =   parseFloat(st/(data0.length))
//                 }
//            st =st.toFixed(2);
//            st.replace(".00", "");
//         html = html + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >"+addCommas(st)+"</td>";
//
//        }
//         html += "</tr>";
//          html += "<tr style='background-color:#BAB4B4;color:black'>";
//          html += "<td colspan='2' style=\"background-color:\" width=" + trWidth + "><strong>Grand Total</strong></td>";
//        // for calculating Gt
//          for (var no = 0; no < measures.length; no++) {
//        var sum1=0;
//        $.each(data[chartId], function (d) {
//            sum1 += parseFloat(data[chartId][d][measures[no]]);
//        });
//        if (measures[no].indexOf(specialCharacter)!=-1 || measures[no].indexOf("Percent")!=-1 ||measures[no].indexOf("percent")!=-1 ||measures[no].indexOf("PERCENT")!=-1) {
//                sum1 =   parseFloat(sum1/(data[chartId].length))
//                 }
//        sum1 = sum1.toFixed(2);
//        sum1.replace(".00", "");
//         html = html + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >" +addCommas(sum1)+ "</td>";
//          }
//            html += "</tr>";
//        html = html + "</table></div></div>";
//    if(chartIdTile==chartId+"Tile" ){
//        $("#openTableTile").html(html)
//    }else{
//        $("#" + chartId).html(html);
//    }
//
//
//
////    });
//}

function buildTableOld13July(chartId,data, currdata, viewbys, measures, divwidth, divHght,KPIresult) {
  
    var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
   var data0 = currdata;
   var specialCharacter ="%";
   var nameArrCurrent ="%";
   var nameArrPrior ="";
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
   var chartData = JSON.parse(parent.$("#chartData").val());
   var  colIds = chartData[chartId]["viewIds"];
   var dimensionId = chartData[chartId]["dimensions"];
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
               viewBys = viewbys;
           }  
        
        var html = "";
        if(data[chartId].length == 1){
        html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; overflow-y: hidden;overflow-x: auto\">";
        }else{
             html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; overflow-y: auto;overflow-x: hidden\">";
        }
        html = html + "<div style='height:400px'>";
        if(data[chartId].length == 1){
        html += "<table id=\"chartTable" + chartId + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
        var rowspan = viewbys.length;
             for(var a=0;a<viewBys.length;a++){
             html += "<tr style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black;white-space:nowrap;\">";
              html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>"+viewbys[a]+"</strong></th>"
      data[chartId].forEach(function(d, i) {


         html = html + "<th id='"+d[viewbys[a]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\" width=" + trWidth + " >" + d[viewBys[a]] + "</th>";
        });
                 html = html + "</tr>";
             }

        for(var b=0;b<measures.length;b++){
             var num = (b + 1) % 2;
            var color;
            if (num === 0) {
                color = "rgba(240, 245, 248, 1)";
            } else {
                color = "white";
            }
         html += "<tr style=\"background-color:" + color + ";white-space:nowrap;\">";
         html += "<td style=\"background-color:#A6BEE9;color:black\"><strong>"+measures[b];
      data[chartId].forEach(function(d, i) {

          html += "<td >"+addCurrencyType(chartId, getMeasureId(measures[b])) + addCommas(d[measures[b]])+"</td>";

     });
        html +="</strong></td>"
         html += "</tr>";
        }
         html += "</table>";
        }//Transpose of Table
        else{

        html = html + "<table id=\"chartTable" + chartId + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
        html = html + "<tr align='left' style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><th  width=\"25px\"><strong>S.no</strong></th>";
        for(var k=0;k<viewBys.length;k++){

        html = html + "<th   width=" + trWidth + "><strong>" + viewBys[k] + "</strong></th>";
        }
         for(var j=0;j<measures.length;j++){
        html = html + "<th   width=" + trWidth + "><strong>" + measures[j] + "</strong></th>";
        if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
                        html += "<th width='5%' style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"></th>"
         }
                    else{
                        if(j==measures.length-1){
        html += "<th width='5%' style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"></th>"
                        }
                    }
         }
        }
         data[chartId].forEach(function(d, i) {
            
            var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "rgba(240, 245, 248, 1)";
            } else {
                color = "white";
            }
            rowCount++;
            html = html + "<tr><td  style=\"background-color:" + color + ";\" width=25px>" + (i + 1) + "</td>";
             var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    for(var l=0;l<viewBys.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {

                           html = html + "<td id='"+d[viewbys[l]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[viewBys[l]] + "</td>";

               }else{
                  
                               html = html + "<td id='"+d[viewbys[l]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[viewBys[l]] + "</u></td>";
               }
                    }
                    var emojiWdith=divwidth/27;
                    emojiWdith=emojiWdith>25?25:emojiWdith;
            for(var j=0;j<measures.length;j++){
            html = html + "<td  style=\"background-color:" + color + ";text-align:right;color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(chartId, getMeasureId(measures[j])) + addCommas(numberFormat(d[measures[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
                if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
                        var targetValue=chartData[chartId]["emojiValue"];
                        var ctxPath=parent.document.getElementById("h").value;
                        //                alert(measures[j]+":"+d[measures[j]]+":"+targetValue[j]);
                        //                            alert(measures[i]+":"+d[measures[i]]+":"+targetValue[i]);
                        if(parseInt(d[measures[j]])>targetValue[j]){
                            html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/happy_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
                        else if(parseInt(d[measures[j]])==targetValue[i]){
                            html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/normal_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
        else{
                            html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/sad_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
                        }
                    }
                    else{
                        if(j==measures.length-1){
                            
                            var targetValue=chartData[chartId]["emojiAbsValue"];
            var ctxPath=parent.document.getElementById("h").value;
                            if(parseInt(d[targetValue[0]])>targetValue[1]){
                                html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/happy_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
                            else if(parseInt(d[targetValue[0]])==targetValue[1]){
                                html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/normal_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
            else{
                                html += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/sad_face.png' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
        }
                    }
                }
            }

            html = html + "</tr>";

        });
                  var colspan = viewbys.length +1;
    // add by mynk sh. Condition for ST
        if(typeof chartData[chartId]["showST"]!=='undefined' && chartData[chartId]["showST"]!=='' && chartData[chartId]["showST"]==='Y'){
//        html += "<tr style='background-color:#8C8C8C;color:black'>";
        html += "<tr style='background-color:#BAB4B4;color:black'>";
        html += "<td colspan='" +colspan + "' style=\"background-color:\" width=" + trWidth + "><strong>Sub Total</strong></td>";
        for( var k=0;k< measures.length;k++){
            var st=0;
            var st1=0;
            var changeMeasure="";
            $.each(data0, function(d){
                 st += parseFloat(data0[d][measures[k]]);
                 st1 += parseFloat(data0[d][measures[k]]);
                 if(measures[k].indexOf("Change%")!=-1){
                changeMeasure = measures[k];
                 }
            });
            if(changeMeasure.indexOf("Change%")!=-1){
    var current = 0;
    var prior = 0;
    var nameArr = [];

    var nameArr2 = "";
    nameArr =  changeMeasure.split(/[ ,]+/);

 for(var k =1;k<nameArr.length;k++){
 if(nameArr.length>2){
 if(k==1){
     nameArr2 =  nameArr[k]
   }else{
     nameArrCurrent = nameArr2.concat(nameArr[k]);
   }
 }else{
    nameArrCurrent = nameArr[k]
 }
 }
 nameArrPrior = "Prior "+nameArrCurrent;
for(var l in data0){

    for(var p in measures){

    if(nameArrCurrent.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().replace(" ", "")){

     current += parseFloat(data0[l][measures[p]])
   }
   else if(nameArrPrior.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().replace(" ", "")){

     prior += parseFloat(data0[l][measures[p]])
   }
    }
  }

 if(current!="undefined" && current!="" && prior!="undefined" && prior!=""){
     st = parseFloat(((current-prior)/prior)*100)

 }else{
     st =   parseFloat(st1/(data0.length));

 }
 }
else if (measures[k].indexOf(specialCharacter)!=-1 || measures[k].indexOf("Percent")!=-1 ||measures[k].indexOf("percent")!=-1 ||measures[k].indexOf("PERCENT")!=-1) {
                st =   parseFloat(st/(data0.length))
                 }
            st =st.toFixed(2);
            st.replace(".00", "");
            if(chartData[chartId]["aggregation"][k]=='avg' || chartData[chartId]["aggregation"][k]=='AVG'){
                st/=rowCount;
            }
//            var st=numberFormat1(st,yAxisFormat,yAxisRounding,chartId);
//            if(chartData[chartId]["aggregation"]=="avg" || chartData[chartId]["aggregation"]=="AVG"){
//                st=st/rowCount;
//            }
         html = html + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >"+addCommas(st)+"</td>";

        }
         html += "</tr>";
    }
   // add by mynk sh. Condition for GT 
      if(typeof chartData[chartId]["showGT"]!=='undefined' && chartData[chartId]["showGT"]!=='' && chartData[chartId]["showGT"]==='Y'){   
        
          html += "<tr style='background-color:#BAB4B4;color:black'>";
          html += "<td colspan='"+ colspan  +"' style=\"background-color:\" width=" + trWidth + "><strong>Grand Total</strong></td>";
        // for calculating Gt
          for (var no = 0; no < measures.length; no++) {
        var sum1=0;
        $.each(data[chartId], function (d) {
            sum1 += parseFloat(data[chartId][d][measures[no]]);
        });
        if (measures[no].indexOf(specialCharacter)!=-1 || measures[no].indexOf("Percent")!=-1 ||measures[no].indexOf("percent")!=-1 ||measures[no].indexOf("PERCENT")!=-1) {
                sum1 =   parseFloat(sum1/(data[chartId].length))
                 }
        sum1 = sum1.toFixed(2);
        sum1.replace(".00", "");
        if(chartData[chartId]["aggregation"][no]=='avg' || chartData[chartId]["aggregation"][no]=='AVG'){
        sum1/=rowCount;
          }
         html = html + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >" +addCurrencyType(chartId, getMeasureId(measures[no])) + addCommas(sum1)+ "</td>";
          }
            html += "</tr>";
        }
       
       html = html + "</tr>"; 
        html = html + "</table>";
        }// end by mynk sh. Condition for GT

        html += "</div></div>";

    if(chartIdTile==chartId+"Tile" ){
        $("#openTableTile").html(html)
    }else{
        $("#" + chartId).html(html);
    }

}

function buildTransposeTable(chartId,data, currdata, viewbys, measures, divwidth, divHght){
var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
   var data0 = currdata;
   var specialCharacter ="%";
   var nameArrCurrent ="%";
   var nameArrPrior ="";
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
   var chartData = JSON.parse(parent.$("#chartData").val());
   var  colIds = chartData[chartId]["viewIds"];
   var dimensionId = chartData[chartId]["dimensions"];
   var k = chartId.replace("chart","");
   var tableId = "table" + (parseInt(k) + 1);
   var divHeight = $(window).height();
   var viewBys = [];
   var prop=graphProp(chartId);
        var trWidth = divwidth / 8;
        divHght = divHght-50;
        if(typeof dimensionId !=="undefined"){
        for(var m=0;m<dimensionId.length;m++){
                            viewBys.push(viewOvName[viewOvIds.indexOf(dimensionId[m])])
                        }
                        }else{
                            viewBys = viewbys;
                        }
        var html = "";
        var rowspan = viewbys.length;
             html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; overflow-y: hidden;overflow-x: auto\">";
        html = html + "<div style='height:400px'>";
        html += "<table id=\"chartTable" + chartId + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";

             for(var a=0;a<viewBys.length;a++){


             html += "<tr style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black;text-align: left;white-space:nowrap;\">";
               html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>"+viewbys[a]+"</strong></th>"
      data[chartId].forEach(function(d, i) {


         html = html + "<th id='"+d[viewbys[a]]+":"+d[measures[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\" width=" + trWidth + " >" + d[viewBys[a]] + "</th>";
        });
                 html = html + "</tr>";
             }

        for(var b=0;b<measures.length;b++){
             var num = (b + 1) % 2;
            var color;
            if (num === 0) {
                color = "rgba(240, 245, 248, 1)";
            } else {
                color = "white";
            }
         html += "<tr style=\"background-color:" + color + ";white-space:nowrap;\">";
         html += "<td style=\"background-color:#A6BEE9;color:black\"><strong>"+checkMeasureNameForGraph(measures[b]);
      data[chartId].forEach(function(d, i) {

          html += "<td >"+addCurrencyType(chartId, getMeasureId(measures[b])) + addCommas(numberFormat(d[measures[b]],yAxisFormat,yAxisRounding,chartId))+"</td>";

     });
        html +="</strong></td>"
         html += "</tr>";

        }
         html += "</table>";
           html += "</div></div>";

    if(chartIdTile==chartId+"Tile" ){
        $("#openTableTile").html(html)
    }else{
        $("#" + chartId).html(html);
    }

}

function buildDialNew(div,data, columns, measureArray,wid,hgt,KPIResult){
//    alert("newww")
var specialCharacter = "%";
var htmlDiv ="";

      var appendDiv = "";
      if(div.indexOf("DialDivTop1_")!=-1 ||div.indexOf("DialDivTop2_")!=-1||div.indexOf("DualDivTop1_")!=-1||div.indexOf("DualDivTop2_")!=-1||div.indexOf("TrDivTop1_")!=-1||div.indexOf("TrDivTop2_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}

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
var dashletid=div;
var div1=parent.$("#chartname").val()
var fromoneview=parent.$("#fromoneview").val()
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
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
                   sum2 =  numberFormat(KPIResult,yAxisFormat,yAxisRounding,div)
                 }
        sum1 = sum1.toFixed(2);
//        sum1.replace(".00", "");
//dialTargetValue = parseInt(sum1);
//var fontsize=wid*.03;
//if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== ''){
//    fontsize=chartData[div]["kpiGTFont"];
//}else {
//    fontsize = wid *.03;
//}
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
htmlDiv += "<div id='dialDiv"+div+"' style='width:"+wid+"px;height:"+hgt*.75+"px'></div>";
//  if (measureArray[0].indexOf(specialCharacter)!=-1 || measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1 ||chartData[div]["aggregation"][0]=="AVG" || chartData[div]["aggregation"][0]=="avg") {
//sum2 += "%";
//  }
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
if(typeof chartData[div]["dialMeasureSuffix"]!="undefined" && chartData[div]["dialMeasureSuffix"]!=""){
sum2=(sum2+" "+chartData[div]["dialMeasureSuffix"]);
}else{
sum2=(sum2);
  }
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
//     $("#"+appendDiv).append(htmlDiv);
//    var name = "Pi";
var name = appendDiv;
//    var name = "dialDiv"+div;
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
    var confingMinRed =  parseInt(config.min);
    var confingMaxRed =  parseInt(config.min+ range*0.2);
 var confingMinOrange =  parseInt(config.min + range*0.2);
 var confingMaxOrange =  parseInt(config.min + range*0.4);
   var confingMinPink =  parseInt(config.min + range*0.4);
   var confingMaxPink =  parseInt(config.min + range*0.6);
   var confingMinBlue =  parseInt(config.min + range*0.6);
   var confingMaxBlue =  parseInt(config.min + range*0.8);
  var confingMinGreen =  parseInt(config.min + range*0.8);
  var confingMaxGreen =  parseInt(config.max);
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
 if(typeof chartData[div]["dialValues"] !="undefined" && chartData[div]["dialValues"] !=""){
if(typeof chartData[div]["dialValues"]["minGreeen"] !="undefined" && chartData[div]["dialValues"]["minGreeen"] !=""){
    confingMinRed = chartData[div]["dialValues"]["minGreeen"];
}else{
    confingMinRed =  config.min;
}
if(typeof chartData[div]["dialValues"]["maxGreeen"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"] !=""){
confingMaxRed = chartData[div]["dialValues"]["maxGreeen"];
}else{

    confingMaxRed =   parseInt(config.min+ range*0.2) ;
}
if(typeof chartData[div]["dialValues"]["minOrange"] !="undefined" && chartData[div]["dialValues"]["minOrange"] !=""){
confingMinOrange = chartData[div]["dialValues"]["minOrange"];
}else{
confingMinOrange =  parseInt(config.min + range*0.2) ;
}
if(typeof chartData[div]["dialValues"]["maxOrange"] !="undefined" && chartData[div]["dialValues"]["maxOrange"] !=""){
confingMaxOrange = chartData[div]["dialValues"]["maxOrange"];
}else{
confingMaxOrange =  parseInt(config.min + range*0.4) ;
}
if(typeof chartData[div]["dialValues"]["minPink"] !="undefined" && chartData[div]["dialValues"]["minPink"] !=""){
confingMinPink = chartData[div]["dialValues"]["minPink"];
}else{
confingMinPink =  parseInt(config.min + range*0.4) ;
}
if(typeof chartData[div]["dialValues"]["maxPink"] !="undefined" && chartData[div]["dialValues"]["maxPink"] !=""){
confingMaxPink = chartData[div]["dialValues"]["maxPink"];
}else{
confingMaxPink =  parseInt(config.min + range*0.6) ;
}
if(typeof chartData[div]["dialValues"]["minBlue"] !="undefined" && chartData[div]["dialValues"]["minBlue"] !=""){
confingMinBlue = chartData[div]["dialValues"]["minBlue"];
}else{
confingMinBlue =  parseInt(config.min + range*0.6) ;
}
if(typeof chartData[div]["dialValues"]["maxBlue"] !="undefined" && chartData[div]["dialValues"]["maxBlue"] !=""){
confingMaxBlue = chartData[div]["dialValues"]["maxBlue"];
}else{
confingMaxBlue =  parseInt(config.min + range*0.8) ;
}
if(typeof chartData[div]["dialValues"]["minRed"] !="undefined" && chartData[div]["dialValues"]["minRed"] !=""){
confingMinGreen = chartData[div]["dialValues"]["minRed"];
}else{
confingMinGreen =  parseInt(config.min + range*0.8) ;
}
if(typeof chartData[div]["dialValues"]["maxRed"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"] !=""){
confingMaxGreen = chartData[div]["dialValues"]["maxRed"];
}else{
confingMaxGreen =  config.max ;
}}
var gauges=[];
if(typeof chartData[div]["dialType"]==='undefined' || chartData[div]["dialType"]==='std'){
                                config.redZones = [{ from: confingMinRed, to: confingMaxRed }];
                                config.pinkZones = [{ from: confingMinPink, to: confingMaxPink }];
				config.yellowZones = [{ from: confingMinOrange, to: confingMaxOrange }];
                                config.blueZones = [{ from: confingMinBlue, to: confingMaxBlue }];
                                config.greenZones = [{ from: confingMinGreen, to: confingMaxGreen }];
}
else{
                                config.greenZones = [{ from: confingMinRed, to: confingMaxRed }];
				config.yellowZones = [{ from: confingMinOrange, to: confingMaxOrange }];
                                config.blueZones = [{ from: confingMinBlue, to: confingMaxBlue }];
                                config.pinkZones = [{ from: confingMinPink, to: confingMaxPink }];
                                config.redZones = [{ from: confingMinGreen, to: confingMaxGreen }];
}

//				gauges[name] = new Gauge(name + "GaugeContainer", config);
confingMinRed=parseInt(confingMinRed);
confingMaxGreen=parseInt(confingMaxGreen);
dialTargetValue=dialTargetValue<confingMinRed?confingMinRed:dialTargetValue;
dialTargetValue=dialTargetValue>confingMaxGreen?confingMaxGreen:dialTargetValue;

		gauges[name] = new Gauge(name , config ,dialTargetValue,"dialDiv" ,sum2,div);
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
        buildTable(dashletid+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }else{
    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
}
    $("#openTableTile").dialog('open');
}

}

function buildDial(div,data, columns, measureArray,wid,hgt,KPIResult){
var specialCharacter = "%";
var htmlDiv ="";

      var appendDiv = "";
      if(div.indexOf("DialDivTop1_")!=-1 ||div.indexOf("DialDivTop2_")!=-1||div.indexOf("DualDivTop1_")!=-1||div.indexOf("DualDivTop2_")!=-1||div.indexOf("TrDivTop1_")!=-1||div.indexOf("TrDivTop2_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}

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
var dashletid=div;
var div1=parent.$("#chartname").val()
var fromoneview=parent.$("#fromoneview").val()
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
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
                   sum2 =  numberFormat(KPIResult,yAxisFormat,yAxisRounding,div)
                 }
        sum1 = sum1.toFixed(2);
//        sum1.replace(".00", "");
//dialTargetValue = parseInt(sum1);
//var fontsize=wid*.03;
//if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== ''){
//    fontsize=chartData[div]["kpiGTFont"];
//}else {
//    fontsize = wid *.03;
//}
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
htmlDiv += "<div id='dialDiv"+div+"' style='width:"+wid+"px;height:"+hgt*.75+"px'></div>";
//  if (measureArray[0].indexOf(specialCharacter)!=-1 || measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1 ||chartData[div]["aggregation"][0]=="AVG" || chartData[div]["aggregation"][0]=="avg") {
//sum2 += "%";
//  }
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
if(typeof chartData[div]["dialMeasureSuffix"]!="undefined" && chartData[div]["dialMeasureSuffix"]!=""){
sum2=(sum2+" "+chartData[div]["dialMeasureSuffix"]);
}else{
sum2=(sum2);
  }
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
//     $("#"+appendDiv).append(htmlDiv);
//    var name = "Pi";
var name = appendDiv;
//    var name = "dialDiv"+div;
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
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
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

		gauges[name] = new Gauge(name , config ,dialTargetValue,"dialDiv" ,sum2,div);
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
        buildTable(dashletid+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }else{
    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
}
    $("#openTableTile").dialog('open');
}

}

function updateGauges()
			{
                            function updateGauges()
            {
                for (var key in gauges)
                {
                    var value = getRandomValue(gauges[key])
                    gauges[key].redraw(value);
                }
            }
//				for (var key in gauges)
//				{
//
//					var value = parseInt()
//
//                                        gauges[key].redraw(value);
//				}
			}
 function getRandomValue(gauge)
			{
				var overflow = 0; //10;

				return gauge.config.min - overflow + (gauge.config.max - gauge.config.min + overflow*2) *  Math.random();
			}
// function initialize()
//			{
////				createGauges();
//				setInterval(updateGauges, 5000);
//			}
 function Gauge(placeholderName, configuration, dialTargetValue,div,sum2,chartId){
	this.placeholderName = placeholderName;
	 var pi = Math.PI;
	var self = this; // for internal d3 functions
	this.configure = function(configuration)
	{
		this.config = configuration;
		this.config.size = this.config.size * 0.9;
		this.config.raduis = this.config.size * 0.97 / 2;
		this.config.cx = this.config.size / 2;
		this.config.cy = this.config.size / 2;
		this.config.min = undefined != configuration.min ? configuration.min : 0;
		this.config.max = undefined != configuration.max ? configuration.max : 100;
		this.config.range = this.config.max - this.config.min;
		this.config.majorTicks = configuration.majorTicks || 5;
		this.config.minorTicks = configuration.minorTicks || 2;
		this.config.greenColor 	= configuration.greenColor || "#109618";
                if(chartData[chartId]["chartType"]==="Score-vs-Targets"){
                    this.config.yellowColor = configuration.yellowColor || "#F1662D";
                }else{
		this.config.yellowColor = configuration.yellowColor || "#FF9900";
                }
		this.config.redColor 	= configuration.redColor || "#DC3912";
                this.config.pinkColor 	= configuration.pinkColor || "#F8B823";
                this.config.blueColor 	= configuration.blueColor || "#C9E131";
		this.config.transitionDuration = configuration.transitionDuration || 500;
	}
         var chartData = JSON.parse(parent.$("#chartData").val());
	this.render = function()
	{
		this.body = d3.select("#" + this.placeholderName)
							.append("svg:svg")
                                                        .attr("id", "svg_" + div)
							.attr("class", "gauge")
							.attr("width", this.config.size)
								.attr("height", this.config.size)
                                                        .style("float", function(d){
                                                    if(chartData[chartId]["chartType"]==="Score-vs-Targets"){
                                                        return  "left";
                                                    }else{
                                                         return  "";
                                                    }
                                                        })
                                                            .style("margin-top", function(d){
                                                    if(chartData[chartId]["chartType"]==="Score-vs-Targets"){
                                                        return  "-4%";
                                                    }else{
                                                         return  "";
                                                    }
                                                         })

		for (var index in this.config.greenZones){
			this.drawBand(this.config.greenZones[index].from, this.config.greenZones[index].to, self.config.greenColor);
		}
		for (var index in this.config.yellowZones){
			this.drawBand(this.config.yellowZones[index].from, this.config.yellowZones[index].to, self.config.yellowColor);
		}
                for (var index in this.config.pinkZones){
			this.drawBand(this.config.pinkZones[index].from, this.config.pinkZones[index].to, self.config.pinkColor);
		}
                for (var index in this.config.blueZones){
			this.drawBand(this.config.blueZones[index].from, this.config.blueZones[index].to, self.config.blueColor);
		}
		for (var index in this.config.redZones){
			this.drawBand(this.config.redZones[index].from, this.config.redZones[index].to, self.config.redColor);
		}
		if (undefined != this.config.label){
		}

		var fontSize = Math.round(this.config.size / 18);
		var majorDelta = this.config.range / (this.config.majorTicks - 1);
		for (var major = this.config.min; major <= this.config.max; major += majorDelta)
		{
			var minorDelta = majorDelta / this.config.minorTicks;
			for (var minor = major + minorDelta; minor < Math.min(major + majorDelta, this.config.max); minor += minorDelta)
			{
				var point1 = this.valueToPoint(minor, 0.75);
				var point2 = this.valueToPoint(minor, 0.85);

				this.body.append("svg:line")
							.attr("x1", point1.x)
							.attr("y1", point1.y)
							.attr("x2", point2.x)
							.attr("y2", point2.y)
							.style("stroke", "#666")
							.style("stroke-width", "1px");
			}

			var point1 = this.valueToPoint(major, 0.7);
			var point2 = this.valueToPoint(major, 0.85);

			this.body.append("svg:line")
						.attr("x1", point1.x)
						.attr("y1", point1.y)
						.attr("x2", point2.x)
						.attr("y2", point2.y)
						.style("stroke", "#333")
						.style("stroke-width", "2px");
//                       var chartData = JSON.parse(parent.$("#chartData").val());
			if (major == this.config.min || major == this.config.max)
			{
				var point = this.valueToPoint(major, 0.83);

				this.body.append("svg:text")
				 			.attr("x", point.x)
				 			.attr("y", point.y)
				 			.attr("dy", fontSize / 2)
				 			.attr("text-anchor", major == this.config.min ? "start" : "end")
				 			.text(function(){
                                                            return major;
                                                    })
				 			.style("font-size", fontSize + "px")
							.style("fill", "#333")
							.style("stroke-width", "0px");
			}
		}

		var pointerContainer = this.body.append("svg:g").attr("class", "pointerContainer");
		var midValue = (this.config.min + this.config.max) / 2;
		var pointerPath = this.buildPointerPath(midValue);
		var pointerLine = d3.svg.line()
									.x(function(d) { return d.x })
									.y(function(d) { return d.y })
									.interpolate("basis");

		pointerContainer.selectAll("path")
							.data([pointerPath])
							.enter()
								.append("svg:path")
									.attr("d", pointerLine)
									.style("fill", "#dc3912")
									.style("stroke", "#c63310")
									.style("fill-opacity", 0.7)

		pointerContainer.append("svg:circle")
							.attr("cx", this.config.cx)
							.attr("cy", this.config.cy)
							.attr("r", 0.12 * this.config.raduis)
							.style("fill", "#4684EE")
							.style("stroke", "#666")
							.style("opacity", 1);
                this.body.append("svg:text")
				 			.attr("x", this.config.cx)
				 			.attr("y", this.config.cy+(this.config.size/5.5))
				 			.attr("dy", fontSize / 2)
				 			.attr("text-anchor", "middle")
                                                        .style("fill",function(d, i) {
                                                           var dialcolor="";
                                                           if(typeof chartData[chartId]["colorPicker"]!="undefined" && chartData[chartId]["colorPicker"]!=""){
                                                             dialcolor = chartData[chartId]["colorPicker"];
                                                           }else {
                                                             dialcolor = "#333";
                                                           } return dialcolor;
                                                         })
				 			.text(function(){
                                                            return sum2;
                                                    })
				 			.style("font-size", function(d, i) {
                                                          if(typeof chartData[chartId]["kpiGTFont"]!=="undefined" && chartData[chartId]["kpiGTFont"] !=""){
                                                           return (chartData[chartId]["kpiGTFont"]+"px");
                                                         }else{
                                                          return fontSize+"px";
                                                         }
                                                        })
							.style("stroke-width", "0px")
                                                        .on("mouseover", function(d, i) {
                                                        if($("#chartType").val()=="Multi-Dashboard"){
                                                            chartId = "chart1";
                                                        }
                show_detailsKPI(d, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], this,chartId,sum2);
                }
            ).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });                                        
		var fontSize = Math.round(this.config.size / 16);
		pointerContainer.selectAll("text")
							.data([midValue])
							.enter()

		this.redraw(this.config.min);
	}

	this.buildPointerPath = function(value)
	{
		var delta = this.config.range / 13;

		var head = valueToPoint(value, 0.85);
		var head1 = valueToPoint(value - delta, 0.12);
		var head2 = valueToPoint(value + delta, 0.12);

		var tailValue = value - (this.config.range * (1/(270/360)) / 2);
		var tail = valueToPoint(tailValue, 0.28);
		var tail1 = valueToPoint(tailValue - delta, 0.12);
		var tail2 = valueToPoint(tailValue + delta, 0.12);

		return [head, head1, tail2, tail, tail1, head2, head];

		function valueToPoint(value, factor)
		{
			var point = self.valueToPoint(value, factor);
			point.x -= self.config.cx;
			point.y -= self.config.cy;
			return point;
		}
	}

	this.drawBand = function(start, end, color)
	{
		if (0 >= end - start) return;
                var chartData = JSON.parse(parent.$("#chartData").val());
		this.body.append("svg:path")
					.style("fill", color)
					.attr("d", d3.svg.arc()
						.startAngle(this.valueToRadians(start))
						.endAngle(this.valueToRadians(end))
//						.startAngle(-90 * (pi/180))
//                                                   .endAngle(90 * (pi/180))
                                                .innerRadius(0.65 * this.config.raduis)
						.outerRadius(0.85 * this.config.raduis))
					.attr("transform", function() { return "translate(" + self.config.cx + ", " + self.config.cy + ") rotate(270)" })
                                        .on("mouseover", function(d, i) {
                                         var selectedColor=hexc($(this).css('fill'));
                show_detailsKPI(d, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], this,chartId,sum2,selectedColor);
	}
            ).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
	}

	this.redraw = function(value, transitionDuration)
	{

		var pointerContainer = this.body.select(".pointerContainer");

		pointerContainer.selectAll("text").text(dialTargetValue+"%");

		var pointer = pointerContainer.selectAll("path");

		pointer.transition()
					.duration(undefined != transitionDuration ? transitionDuration : this.config.transitionDuration)
					//.delay(0)
					//.ease("linear")
					//.attr("transform", function(d)

					.attrTween("transform", function()
					{
						var pointerValue = dialTargetValue;
						if (value > self.config.max) pointerValue = self.config.max + 0.02*self.config.range;
						else if (value < self.config.min) pointerValue = self.config.min - 0.02*self.config.range;
						var targetRotation = (self.valueToDegrees(pointerValue) - 90);
						var currentRotation = self._currentRotation || targetRotation;
						self._currentRotation = targetRotation;

						return function(step)
						{
							var rotation = currentRotation + (targetRotation-currentRotation)*step;
							return "translate(" + self.config.cx + ", " + self.config.cy + ") rotate(" + rotation + ")";
						}
					});
	}

	this.valueToDegrees = function(value)
	{
		// thanks @closealert
		//return value / this.config.range * 270 - 45;
		return value / this.config.range * 270 - (this.config.min / this.config.range * 270 + 45);
	}

	this.valueToRadians = function(value)
	{
		return this.valueToDegrees(value) * Math.PI / 180;
	}

	this.valueToPoint = function(value, factor)
	{
		return { 	x: this.config.cx - this.config.raduis * factor * Math.cos(this.valueToRadians(value)),
					y: this.config.cy - this.config.raduis * factor * Math.sin(this.valueToRadians(value)) 		};
	}
        this.configure(configuration);

        function onMouseClick(d) {
            
             if (typeof  openTableDiv == "function") {
                 openTableDiv.call();
                
            }
        }

	// initialization
	this.configure(configuration);
}

function graphProp(div){
    var chartData = JSON.parse(parent.$("#chartData").val());
    try{
    var measure = chartData[div]["meassures"];

//    var dataDisplay,displayType,yAxisRounding,yAxisFormat;
    if(typeof chartData[div]["dataDisplay"]!=="undefined"){
        dataDisplay=chartData[div]["dataDisplay"];
    }else{
       dataDisplay="No";
    }
    if(typeof chartData[div]["valueOf"]!=="undefined"){
        displayType=chartData[div]["valueOf"];
    }else{
       displayType="Absolute";
    }
    if(typeof chartData[div]["rounding"]!=="undefined"){
        yAxisRounding=chartData[div]["rounding"];
    }else{
       yAxisRounding=0;
    }
    if(typeof chartData[div]["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartData[div]["yAxisFormat"];
    }else{
       yAxisFormat="";
    }
    if(typeof chartData[div]["y2AxisFormat"]!=="undefined"){
        y2AxisFormat=chartData[div]["y2AxisFormat"];
    }else{
       y2AxisFormat="";
    }
    if(typeof chartData[div]["displayX"]!=="undefined"){
        displayX=chartData[div]["displayX"];
    }else{
       displayX="Yes";
    }
    if(typeof chartData[div]["displayY"]!=="undefined"){
        displayY=chartData[div]["displayY"];
    }else{
       displayY="Yes";
    }
    if(typeof chartData[div]["displayY1"]!=="undefined"){
        displayY1=chartData[div]["displayY1"];
    }else{
       displayY1="Yes";
    }
    if(typeof chartData[div]["displayLegend"]!=="undefined"){
        displayLegend=chartData[div]["displayLegend"];
    }else{
       displayLegend="Yes";
    }
    if(typeof chartData[div]["colorLegend"]!=="undefined"){
        colorLegend=chartData[div]["colorLegend"];
    }else{
       colorLegend="Black";
    }
    }catch(e){}

    function getPowerOfTen(num){
    var bd = 1;
    for (var i = 0; i <
        parseInt(num); i++) {
        bd = bd * 10;
    }
    return bd;
}
function numberFormat(value,symbol,precion,div){
    var  doubleVal = "";
    var isNegativeValue=false;
   var chartData = JSON.parse(parent.$("#chartData").val());
    var num = parseFloat(value);
    if(num<0){
        num*=-1;
        isNegativeValue=true;
    }
    var check="0.";
    for(var i=0;i<precion;i++){
        check += "0";
    }
    var temp = "";
    if(symbol=="K"){
        doubleVal = (num / getPowerOfTen(3));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof div==='undefined'|| (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            temp = setFormat(doubleVal,precion)+ " K";
             }else{
                  temp = setFormat(doubleVal,precion);
        }
        }
    }else if(symbol=="M"){
        doubleVal = (num / getPowerOfTen(6));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            temp = setFormat(doubleVal,precion)+ " M";
            }else{
                 temp = setFormat(doubleVal,precion);
        }
        }
    }else if(symbol=="" || symbol=='Auto'){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion);
        }
        if(parseFloat(temp)>=10000000000 && (typeof chartData[div]["roundingType"]==='undefined' || chartData[div]["roundingType"]==="MB")){
            doubleVal = (num / getPowerOfTen(9));
            if(setFormat(doubleVal,precion) ==check){
                temp=value + "B";
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    temp = setFormat(doubleVal,precion)+ "B";
                }else{
                    temp = setFormat(doubleVal,precion) + "B";
                }
            }
        }
        else if(parseFloat(temp)>=100000000 && (typeof chartData[div]["roundingType"]!=='undefined' && chartData[div]["roundingType"]==="LCr")){
            doubleVal = (num / getPowerOfTen(7));
            if(setFormat(doubleVal,precion) ==check){
                temp=value+ " Crs";
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    temp = setFormat(doubleVal,precion)+ " Crs";
                }else{
                    temp = setFormat(doubleVal,precion) + " Crs";
                }
            }
        }
        else if(parseFloat(temp)>=1000000 && (typeof chartData[div]["roundingType"]==='undefined' || chartData[div]["roundingType"]==="MB")){
            doubleVal = (num / getPowerOfTen(6));
            if(setFormat(doubleVal,precion) ==check){
                temp=value+ " M";
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    temp = setFormat(doubleVal,precion)+ " M";
                }else{
                    temp = setFormat(doubleVal,precion) + " M";
                }
            }
        }
        else if(parseFloat(temp)>=1000000 && (typeof chartData[div]["roundingType"]!=='undefined' && chartData[div]["roundingType"]==="LCr")){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value+ " Lakhs";
        }else{
               if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            temp = setFormat(doubleVal,precion)+ " Lakhs";
               }else{
                    temp = setFormat(doubleVal,precion) + " Lakhs";
        }
        }
        }
        else if(parseFloat(temp)>=10000){
            doubleVal = (num / getPowerOfTen(3));
            if(setFormat(doubleVal,precion) ==check){
                temp=value+ " K";
            }else{
                if(typeof div==='undefined'|| (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    temp = setFormat(doubleVal,precion)+ " K";
                }else{
                    temp = setFormat(doubleVal,precion)+ " K" ;
                }
            }            
        }
    }
    else if(symbol==='Absolute'){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion);
        }
    }
    else if(symbol=="L"){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value+ " Lakhs";
        }else{
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                temp = setFormat(doubleVal,precion)+ " Lakhs";
            }else{
                temp = setFormat(doubleVal,precion)+ " Lakhs";
            }
        }
    }else if(symbol=="Cr"){
        doubleVal = (num / getPowerOfTen(7));
        if(setFormat(doubleVal,precion) ==check){
            temp=value+ " Crs";
        }else{
             if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            temp = setFormat(doubleVal,precion)+ " Crs";
             }else{
                temp = setFormat(doubleVal,precion)+ " Crs";
    }
        }
    }
    if(isNegativeValue){
        return "-"+temp;
    }
    else{
    return temp;
}
}
//    if(typeof chartData[div]["hideLabel"]!=="undefined"){
//
//        hideLabel=chartData[div]["hideLabel"];
//    }else{
//        for(var i=0; i<measure.length;i++){
//       hideLabel[i] = "Yes";
//        }
//    }
}


function buildPieMulti(div,divId, data, columns, measureArray,wid,hgt) {
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

     var color = d3.scale.category10();
    var divWidth, divHeight, rad;

    var w = $(window).width()/2+"px";
    divWidth=wid;
     divWid=parseFloat($(window).width())*(.35);
    divHeight=400;
    rad=divHeight*.30;
    var autoRounding1 = "1d";
var chartData = JSON.parse(parent.$("#chartData").val());
var div1=parent.$("#chartname").val()
var fromoneview=parent.$("#fromoneview").val()
var colIds= [];
if(fromoneview!='null'&& fromoneview=='true'){
    colIds = chartData[div1]["viewIds"];
    divHeight=hgt;
    divWidth=divWidth-30;
    divWid=divWidth;
    rad=divWidth*.30;
     if(divWidth>=500 && divHeight<=300){
         divWidth=divWidth-25;

rad=divWidth*.25;
     }
}else{
     divHeight=400;
     if (screen.width<1000){
    rad=divHeight*.35;}
else {
    rad=divHeight*.50;
}
    colIds = chartData[div]["viewIds"];
     divWid=parseInt($(window).width())*(.35);
}
if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);

}else{
    var prop = graphProp(div);
}
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
//    var fun = "drillWithinchart(this.id,\""+div+"\")";
 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\""+div+"\")";
	}
        function drillFunct(id1){
        drillWithinchart(id1,div);    
	}
 if(fromoneview!='null'&& fromoneview=='true'){
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();
 var regid=div.replace("Dashlets-","");
  fun = "drillWithinchart11(this.id,\""+div+"\",\""+wid+"\",\""+hgt+"\",\""+regid+"\",\""+oneviewid+"\",\""+repname+"\",\""+repId+"\",\""+div1+"\",'null','null')";
    if(div=='OLAPGraphRegion'){
fun = "viewAdhoctypes(this.id)";
    }
    }
//    var divnum = parseInt(div.replace("chart", "", "gi"));
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
    var margintop;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
        //        margintop = "30px";
        radius = rad;
        height = Math.min(width, height);
        width = Math.min(width, height);
        if(screen.width<1000){
             radius = (Math.min(width, height) / 3.8);
        }
        else{
        radius = (Math.min(width, height) / 2.7);
    }
       
    }
    else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "120px";
        radius = rad;
    }
    else {
        height = divHeight;
        margintop = "0px";
        radius = rad / 1.2;
        if(fromoneview!='null'&& fromoneview=='true'){
            if(divWidth<=500){
margintop="margin-left:3.5em";
if(typeof displayLegend !=="undefined" && displayLegend =="No"){
    margintop="margin-left:6.5em;margin-top:0.5em";
    }
        }else{
if(typeof displayLegend !=="undefined" && displayLegend =="No"){
    margintop="margin-left:10.5em;margin-top:0.5em";
        }
        }
    }
    }
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie() //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
    svg = d3.select("#" + divId).append("svg:svg")
            .datum(data)
            .attr("width", width*.9)
            .attr("height", height)
            .attr("style", margintop);
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
                var colorShad;
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[colIds[0]]) !== -1) {
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
                            colorShad = color(i);
                        }
                    }
                    colorShad = color(i);
                }
                else {
//                    if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                    } else {
                        colorShad = color(i);
//                        colorShad = "blue";
//                    }
                }
                chartMap[d[columns[0]]] = colorShad;
                return color(i);
//                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", width*.7)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            // .attr("class", "arc")
            .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];

            })

            .attr("transform", "translate(" + width / 3.5 + "," + height / 2 + ")")
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) ;

    arcs.append("path")
            .attr("fill", function(d,i) {
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                return color(i);}//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                return color(i);}
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);

              if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columnList, measureArray, this,div);
                }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
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
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill",colorValue);
                 }
                hide_details(d, i, this);
            })
            .transition()
            .ease("bounce")
            .duration(2000)
            .attrTween("d", tweenPie);
    function angle(d) {
        return (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
    }
    //    if((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"  )) && isDouble===false ){
    //    }else{
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "start")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 12px")
            .attr("transform", function(d) {
                return "translate(" + arcFinal.centroid(d) + ")rotate(" + angle(d) + ")";
            });
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 10px")
//            .style("fill", function(d,i){
//                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
//                return "white"
//            else
//                return "black"
//            })
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius / 6;
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius / 2;
                }
                // Set Inner Coordinate
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .attr("fill", function(d, i){
            var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {
                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                               }
                               return lableColors;
             })
            .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof chartData[div]["dataDisplay"] ==="undefined" || chartData[div]["dataDisplay"] === "Yes")  {
if (typeof displayType !== "undefined" && displayType === "Absolute") {

                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);

                }
                else {
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
            }else {
                return "";
            }
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
         var yvalue=0;
		var rectyvalue=0;
		var yLegendvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
    		var fontsize = parseInt(width/45);
		var fontsize1 = parseInt(width/50);
		var rectsize = parseInt(width/60);
               if(data.length>7 && data.length<12){
yvalue = parseInt((height / 3)-30);
        yLegendvalue = parseInt((height / 3)-50);
        rectyvalue = parseInt((height / 3)-40);
}else if(data.length>13){
		yvalue = parseInt((height / 3)-90);
        yLegendvalue = parseInt((height / 3)-110);
        rectyvalue = parseInt((height / 3)-100);
}
else{
	yvalue = parseInt(height / 3);
        yLegendvalue = parseInt((height / 3)-20);
        rectyvalue = parseInt((height / 3)-10);
}
        var count = 0;
        var transform = "";
if(typeof displayLegend !=="undefined" && displayLegend =="Yes"){


            svg.append("g")
           .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
.attr("transform", "translate(" + width*.65  + "," + yLegendvalue + ")")

            .attr("fill", "Black")
.text(function(d){
              return columns[0]
            })

           for(var i in  data){
            if(i!=0){
            yvalue = parseInt(yvalue+25)
            rectyvalue = parseInt(rectyvalue+25)
    }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + width*.60  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", color(i))
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.65  + "," + yvalue + ")")
            .attr("fill", function(){
           if(fromoneview!='null'&& fromoneview=='true'){
                 div=div1;
             }
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  color(i);
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
     }else {
                    return data[i][columns[0]];
    }
           })
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id)
                    })
              count++
}
    }


//    if ((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) || parent.$("#isMap").val() === "MapTable") {
//    }
//    else if (parent.$("#dashBoardType").val() === "drilldash") {
//    }
//    else {
//        buildDrill(width, height);
//    }
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
}


function buildBarAdvance(div,divId, data, columns, measureArray,wid,hgt) {
 var customTicks = 5;
var record = 15;
var currData = [];
var propHtml = "";
propHtml += "<div id='propHtml' style='width:100%;height:"+hgt *.01+"px;'>";
propHtml += "<img id='' style=\"cursor: pointer\" align='right' class='ui-icon ui-icon-pencil' name='chart1' onclick='editDash(\""+div+"\")'/>";
propHtml += "</div>";
$("#"+divId).append(propHtml);
if($("#chartType").val()==="India-Map-With-Trend" || $("#chartType").val()==="India-Map-Dashboard"){
    for(var k=0;k<(data.length < record ? data.length : record);k++){
        currData.push(data[k]);
    }
    data = currData;
}

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

var divWid;
if($("#chartType").val()=="Pie-Dashboard" || $("#chartType").val()=="Multi-Dashboard" || $("#chartType").val()=="India-Map-Dashboard" || $("#chartType").val()=="India-Map-With-Trend" || $("#chartType").val()=="Time-Dashboard"){

divWid =wid;    
}else{
    
divWid=parseFloat($(window).width())*(.70);
}
var divHgt = 450;
if($("#chartType").val()=="Multi-Dashboard" || $("#chartType").val()=="India-Map-Dashboard" || $("#chartType").val()=="India-Map-With-Trend" || $("#chartType").val()=="Time-Dashboard" ){
      $("#isShaded").val("false");
        $("#shadeType").val('');
      
      divHgt=hgt;
}else {
     divHgt=500;
}
var chartData = JSON.parse(parent.$("#chartData").val());

var div1=parent.$("#chartname").val()
  
   var fromoneview=parent.$("#fromoneview").val()
var colIds= [];
if(fromoneview!='null'&& fromoneview=='true'){
    colIds = chartData[div1]["viewIds"];
    divHgt=hgt;
    divWid=wid;
}else{
    colIds = chartData[div]["viewIds"];
 }

   var fromone=parent.$("#fromoneview").val();
    if(fromone!=null && fromone=='true'){
//divWid = wid-20;
   divHgt = hgt+60;
    }
    var chartMap = {};
    var measure1 = measureArray[0];
    var minVal = 0;
//    var chartData = JSON.parse($("#chartData").val());
    if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);

}else{

         if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var prop = graphProp(div);
}

var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};
            var isShadedColor = false;
            var conditionalShading = false;
             if (parent.$("#isShaded").val() == "true") {
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalShading = true;
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
//                        conditionalMeasure = conditionalMap["measure"];
                        conditionalMeasure = $("#conditionalMeasure").val();
                    } else if (parent.$("#shadeType").val() == "standard") {

                    } else if (parent.$("#shadeType").val() == "gradient") {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];
                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < data.length; i++) {
                            measureData.push(data[i][shadingMeasure]);
                        }
                        color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", map[map["measure"]]]);
                           
                    }
                    else
                    {
                          isShadedColor = false;
             conditionalShading = false;
                    }
                } else {
//                    if (parent.isCustomColors) {
//                        color = d3.scale.ordinal().range(parent.customColorList);
//                    }
            color=d3.scale.category10();    
                    }
                    
                  
    var autoRounding1;

        autoRounding1 = "1d";

//    var fun = "drillWithinchart(this.id,\""+div+"\")";
 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\""+div+"\")";
	
	}
        function drillFunct(id1){
        drillWithinchart(id1,div);   
	}
	

     if(fromoneview!='null'&& fromoneview=='true'){
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();
 var regid=div.replace("Dashlets-","");
  fun = "drillWithinchart11(this.id,\""+div+"\",\""+wid+"\",\""+hgt+"\",\""+regid+"\",\""+oneviewid+"\",\""+repname+"\",\""+repId+"\",\""+div1+"\",'null','null')";
    if(div=='OLAPGraphRegion'){
fun = "viewAdhoctypes(this.id)";
    }
    }
    var botom = 50;
    var width =0;
    var height =0;
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }
    var margin = {
        top: 10,
        right: 00,
        bottom: botom,
        left: 70
    };
    if(parent.$("#chartType").val()=="combo-chart"){
    width = divWid *.50; //- margin.left - margin.right
            height = divHgt * .58;
    }else if($("#chartType").val()=="Multi-Dashboard"){
        height = divHgt;
        width = divWid;
        }else {
         width = divWid; //- margin.left - margin.right
            height = divHgt * .65;
    }    
            //- margin.top - margin.bottom
    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
   var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);
    var y = d3.scale.linear()
            .range([height, 0]);
    var measArr = [];
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
    xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");
}else{
    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
 
}
//    if (isFormatedMeasure) {
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });
//
//    }
//    else {
//        yAxis = d3.svg.trendaxis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d) {
//                    return numberFormat(d, round, precition);
//                });
//    }
//    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                   }else {
                       return "";
                   }
//                    return addCommas(d);
                });
//    }
    var yAxis1 = d3.svg.axis1()
            .scale(y)
            .tickFormat(function(d, i) {
                measArr.push(d);
                return "";
            });
            
    //    .tickFormat(formatPercent);
    svg = d3.select("#" + divId).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom +60 )
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+20) + ")");

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
//                return "gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "50%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
    colorMap = {};
    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", function(d, i) {
                var colorShad;
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
////                    return getConditionalColor("steelblue", d[conditionalMeasure]);
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else {
//                    var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
//                    } catch (e) {
//                    }
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
//                        colorShad = drillShade;
//                    } else {
////                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
////                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
////                        } else {
//                        colorShad = color(i);
////                            colorShad = "steelblue";
////                        }
//                    }
//                }
//                chartMap[d[columns[0]]] = colorShad;
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad="steelblue";
            })
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
////                    return getConditionalColor("steelblue", d[conditionalMeasure]);
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else {
//                    var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[colIds[0]]) !== -1) {
//                        colorShad = drillShade;
//                    } else {
//                        if(fromoneview!='null'&& fromoneview=='true'){
//                             colorShad = "steelblue";
//                        }else{
//                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
//                        } else {
//                            colorShad = color(i);
//
////                            colorShad = "steelblue";
//                        }
//                    }
//                    }
//                }
////                colorMap[i] = d[columns[0]] + "__" + colorShad;
////colorShad="steelblue";
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    parent.$("#colorMap").val(JSON.stringify(colorMap));

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
//    var max = maximumValue(data, measure1);
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));


    var max = 0;
    if(fromoneview!='null'&& fromoneview=='true'){
  max = maximumValue(data, measure1);
  if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }
    var yMin = 0;
    yMin = minVal;
    y.domain([yMin, max]);
    svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
     if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }
    }else{
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {

    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
    max = maximumValue(data, measure1);
}}else{
    max = maximumValue(data, measure1);
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""){
   minVal = 0;
 }else{
    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }}
}else{
    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }
    var yMin = 0;
    yMin = minVal;
    y.domain([yMin, max]);
    svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
     if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }
    minVal=0;
    }
}

    y.domain([parseFloat(minVal), parseFloat(max)]);
//    for (var j=0; j <= height; j=j+50) {
//    svg.append("svg:line")
//        .attr("x1", 0)
//        .attr("y1", j)
//        .attr("x2", width)
//        .attr("y2", j)
//        .style("stroke", "#A69D9D")
//        .style("stroke-width", .5)
//        .style("z-index", "9999");
//};
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
    svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
            }
    
    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height + 3) + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
//                return buildXaxisFilterAdvance(div,d,i);
               if(typeof displayX !=="undefined" && displayX =="Yes"){
                if (d.length <= 13){
                    return d;
                }
                else {
                    return d.substring(0, 13) + "..";
                }
               }else {
                   return "";
               }
            })
            .attr('transform', 'rotate(-25)')
            .append("svg:title").text(function(d) {
        if (d.length < 15){
                    return d;
                }
                else {
//                    return d.substring(0, 15) + "..";
                    return d;
                }
    });

    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end");

  var bars=svg.selectAll(".bar")
            .data(data)
            .enter().append("g")
            .attr("class", "bar")
            .append("rect");
            bars.attr("rx", barRadius)
            .attr("fill", function(d,i) {
                 var colorShad;
               var strVal = data[i][measure1];
                if (isShadedColor) {
                  strVal = data[i][shadingMeasure];
                   colorShad = color(strVal);
                } else if (conditionalShading) {
//                    return getConditionalColor("steelblue", d[conditionalMeasure]);
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else {
                    var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[colIds[0]]) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if(fromoneview!='null'&& fromoneview=='true'){
                             colorShad = "steelblue";
                        }else{
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
                        } else {
//                            colorShad = color(i);
colorShad = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                

//                            colorShad = "steelblue";
//                        }
                    }
                    }
                }
                }
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//colorShad="steelblue";
                return colorShad;
            })
            // .attr("color_value", "steelblue")
            .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("x", function(d) {
                return x(d[columns[0]]);
            })
             .attr("width",0)
            .transition()
            .duration(2000)//1 second
            .attr("width", x.rangeBand())
            .attr("y", function(d) {
                return y(d[measure1]);
            })
            .attr("height", function(d) {
                return height - y(d[measure1]);
            })
           bars.on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                prevColor = color(i);

//                }
//                if (toolTipType === "customize") {
//                    for (var num in toolTipData) {
//                        if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                            break;
//                        }
//                    }
//                } else {
if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measureArray, this,div);
                }else{
                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    show_details(d, columns, measureArray, this,div);
                }
//                    show_details(d, columns, measureArray, this,div);
//                }
            })
            .on("mouseout", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){
                }else{
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
//                    var colorValue = prevColor;
                    selectedBar.style("fill", colorValue);
                }
//                }
                hide_details(d, i, this);
            });
            if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
                {
            var font=14;
            font=width/51.2;
            font=font>14?14:font;
            font=font<10?10:font;
            // add by maynk sh. for Afontsize
             if(typeof chartData[div]["Afontsize"]!=="undefined"){
                      font =  chartData[div]["Afontsize"];
             }// end by maynk sh. for Afontsize
            svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                    return "translate(" + (xvalue-width/105) + ", " + (height) + ")rotate(" + -90 + ")";
                })
//                .attr("text-anchor", "middle")

                .attr("class", "valueLabel")
                .attr("style", "font-size:"+font+"px; transform-origin: bottom left 0px;")
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
//                .attr("fill", function(d,i){
//                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
//                        if(parseFloat(d[measure1]) >= max){
//                            return "white";
//                        }
//                    }
//
//                    else {
//                    return labelColor;
//                    }
//                })
.attr("fill", function(d, i){
            var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {

                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                    }
                               return lableColors; 
                })
                .text(function(d)
                {
                        return d[columns[0]];
                });
        }
//    svg.selectAll(".text")
//            .data(data)
//            .enter()
//            .append("text")
//            .attr("class", "text")  //  MISSING FROM CODE IN PAGE DISPLAY
//            .attr("x", function(d, i) {
//                return i * (width / data.length) + (width / data.length - barPadding) / 2;
//            })
//            .attr("y", function(d) {
//
//                return  y(d[measure1]) + 12;  //15 is now 14
//
//            });
//    d3.select("#download").on("click", function() {
//        d3.select(this)
//                .attr("href", 'data:application/octet-stream;base64,' + btoa(encodeURIComponent(d3.select("#chart0").html())))
//                .attr("download", "viz.svg");
//    });
//
//    if ((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) || parent.$("#isMap").val() === "MapTable") {
//    }
//    else {
//        buildCircledrill(height);
//    }

    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    if (x.rangeBand() >= 20) {
        svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                    var yValue = (y(d[measureArray[0]])) < 15 ? y(d[measureArray[0]]) + 14 : y(d[measureArray[0]]);
                    return "translate(" + xvalue + ", " + (yValue) + ")";
                })
                .attr("text-anchor", "middle")
                .attr("class", "valueLabel")
                .attr("onclick", fun)
        //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
        
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
//                .attr("fill", function(d,i){
//                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
//                        if(parseFloat(d[measure1]) >= max){
//                            return "white";
//                        }
//                    }
//
//                    else {
//                    return labelColor;
//                    }
//                })
                  .attr("fill", function(d, i){
                   var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {

                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                    }
                               return lableColors;
                })
                .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                    }else{
                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }else {return "";}
//        return "" + autoFormating(d[measureArray[0]]);
                }).on("mouseover", function(d, i) {
            if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
            {
                var bar = d3.select(this);
                var indexValue = bar.attr("index_value");
                var barSelector = "." + "bars-Bubble-" + indexValue;
                var selectedBar = d3.selectAll(barSelector);
                selectedBar.style("fill", drillShade);
            }
//            if (toolTipType === "customize") {
//                for (var num in toolTipData) {
//                    if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                        show_details(toolTipData[num], custToolTipViewBy, custToolTipMeasure, this);
//                        break;
//                    }
//                }
//            } else {
                show_details(d, columns, measureArray, this);
//            }
        })
                .on("mouseout", function(d, i) {
                    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
                    {
                        var bar = d3.select(this);
                        var indexValue = bar.attr("index_value");
                        var barSelector = "." + "bars-Bubble-" + indexValue;
                        var selectedBar = d3.selectAll(barSelector);

                        var colorValue = selectedBar.attr("color_value");
                        selectedBar.style("fill", colorValue);
                    }
                    hide_details(d, i, this);
                });
    }
//     var html ="<table style=''><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='color:black;font-size:14px;margin-left:40px;text-decoration:underline' >"+columns[0]+"</span></td></tr><tr>";
     var html ="<table style=''><tr>";
     for(var m=0;m<(data.length < 5 ? data.length : 5);m++){
    html +="<td><canvas width='6' height='6' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td>";
    }
    html +="</tr><table>";
//    $("#"+div).append(html);
//    try{
//    parent.legendMap[div] = chartMap;
//}catch(e){}
   if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
    }  
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{ 
count=0;
var boxW=(width + margin.left + margin.right);
var boxH=(height + margin.top + margin.bottom+30);
//var rectW=150+boxW*0.17;
var measureName='';
if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
    measureName=chartData[div]["measureAlias"][measureArray[0]];
}else{
    measureName=checkMeasureNameForGraph(measureArray[0]);
}
var len=measureName.length;
if(columns[0].length+2>len){
    len=columns[0].length+2;
}
var rectW=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectW=(measureName.length+columns[0].length)*7+130;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomleft" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectW=30+(len*7)+85;
}
rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=0;   // buildbar
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17;
}
else{ 
    rectH=10+17+viewByHgt;
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=boxW-rectW;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
    rectX=boxW-rectW;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}
var rectY=-5;
if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)-10;
}
else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){

}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
//alert((boxH-(boxH*.98)-5)+":"+boxW);
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, "+border))
//            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
            .attr("x", rectX)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}            
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
         svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
       }
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
//for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
        var measureName='';
   var offset1=0;
   var offset2=0;
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           offset1=(columns[0].length*6.5+20);
           offset2=-24;
       }
   svg.append("g")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(0)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
     
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
       
            measureName=chartData[div]["measureAlias"][measureArray[0]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[0]);
           
        }
//        var length=0;
//                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
//                    length=20;
//                }
//                else{
//                    length=chartData[div]["measureLabelLength"][measureArray[0]];
//                }
//                if(measureName.length>length){
//                    return measureName.substring(0, length)+"..";
//                }else {
                    return measureName;
//          }
           }).attr("svg:title",function(d){
               return measureName;
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

            .attr("x",rectX+offset1+10)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(0)))
              count++
}
}

function tileCharts(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
//  if()
   var chartData = JSON.parse(parent.$("#chartData").val());
   var kpiResultArr=[];
  
      var appendDiv = "";
if(div.indexOf("kpiDiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
appendDiv = div

}
 if(typeof chartData[div]["enableComparison"]!=='undefined' && chartData[div]["enableComparison"]==='true'){
       tileChartsComp(appendDiv, data, columns, measureArray, divWidth, divHeight,KPIResult);
       return;
   }
   var showData = 0;
   var showData1 = 0;
   var absoluteNumber = 0;
   var perFlag = "";
   var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
   var imageTag =chartData[div]["filename"];
    var specialCharacter ="%";
    var colorPicker = "";
    var measureColor = "";
    var measureAliaskpi = "";
   var DataSum = 0;
   //font size start
   var gtFontSize = 0;
   var infoFontSize = 0;
   var btFontSize = 0;
   //font size end
   var tableData = {};
    var nameArrCurrent = "";
    var nameArrPrior = "";
 

var lineData={};
 var prefix = "";
 var suffix = "";
 var prefixFontSize = "";
 var suffixFontSize = "";


showData = KPIResult;
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
if(chartData[div]["chartType"]==='Combo-Analysis' && typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"]!="" ){
chartData[div]["kpiSubData"] = chartData[div]["kpiGTFont"]
}
$("#chartData").val(JSON.stringify(chartData));
                  if(yAxisFormat==""){
//                      if(yAxisRounding>0){
//                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//                      }
//                      else{
                            showData1 =      addCurrencyType(div, getMeasureId(measureArray[0])) + (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
//                      }

                    }
            else{
//             if(yAxisRounding>0){
//                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//                      }
//                      else{
                            showData1 =      addCurrencyType(div, getMeasureId(measureArray[0])) + (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
//                      }

                }


if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){

for(var i in measureArray){
    if(chartData[div]["KPIName"]!=measureArray[i]){
}else{
     chartData[div]["KPIName"] = measureArray[0];
}
}}
else{

chartData[div]["KPIName"] = measureArray[0];

}
if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
  colorPicker = chartData[div]["colorPicker"];
}else{
colorPicker = "#696969";
}
if(typeof chartData[div]["lFilledFont"]!="undefined" && chartData[div]["lFilledFont"]!=""){
  measureColor = chartData[div]["lFilledFont"];
}else{
measureColor = "#A1A1A1";
} 
if(typeof chartData[div]["measureAliaskpi"]!=="undefined" && chartData[div]["measureAliaskpi"]!==measureArray[0]){
                  measureAliaskpi=chartData[div]["measureAliaskpi"];
            }else{
                    measureAliaskpi=measureArray[0];
            }


 if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
        
             gtFontSize=divHeight*.13;
//             font=width/39.2;
            gtFontSize=gtFontSize>45?45:gtFontSize;
            gtFontSize=gtFontSize<35?35:gtFontSize;
            // add by maynk sh. for fontsize
      
             if(typeof chartData[div]["kpiGTFont"]!=="undefined"){
                      gtFontSize =  chartData[div]["kpiGTFont"];
             }
 }else{
if(divHeight<divWidth){
  gtFontSize = divHeight*.13
//  infoFontSize = divHeight*.068
}else{
   gtFontSize = divWidth*.1
//    infoFontSize = divWidth*.058
}
 }
 if(appendDiv.indexOf("kpiDiv_")!=-1){
     gtFontSize = divHeight*.5
 }
 
 chartData[div]["kpiGTFont"]=gtFontSize;
 
 if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
            btFontSize=divHeight*.13;
            btFontSize=btFontSize>45?45:btFontSize;
            btFontSize=btFontSize<35?35:btFontSize;
     
             if(typeof chartData[div]["kpiSubData"]!=="undefined"){
                      btFontSize =  chartData[div]["kpiSubData"];
             }
 }else{
if(divHeight<divWidth){
  btFontSize = divHeight*.13
}else{
   btFontSize = divWidth*.1
}
 }
 if(appendDiv.indexOf("kpiDiv_")!=-1){
     btFontSize = divHeight*.5
 }
 if(chartData[div]["chartType"]==='Combo-Analysis'){
 chartData[div]["kpiSubData"]=gtFontSize*1.2;
 }else{
 chartData[div]["kpiSubData"]=btFontSize;
 }
 
  if(typeof chartData[div]["kpiFont"]!="undefined" && chartData[div]["kpiFont"] !== '' && chartData[div]["kpiFont"] !=="4"){
        
             infoFontSize=divHeight*.068;
//             font=width/39.2;
            infoFontSize=infoFontSize>45?45:infoFontSize;
            infoFontSize=infoFontSize<35?35:infoFontSize;
            // add by maynk sh. for fontsize
      
             if(typeof chartData[div]["kpiFont"]!=="undefined"){
                      infoFontSize =  chartData[div]["kpiFont"];
             }
 }else{
if(divHeight<divWidth){
//  gtFontSize = divHeight*.13
  infoFontSize = divHeight*.068

}else{
//   gtFontSize = divWidth*.1
if(appendDiv.indexOf("kpiDiv_")!=-1){
      infoFontSize = divWidth*.1
}else{ infoFontSize = divWidth*.058
}
   
}
 }
 if(appendDiv.indexOf("kpiDiv_")!=-1){
    infoFontSize = divWidth*.058
}

chartData[div]["kpiFont"]=infoFontSize;
        
      
   $("#chartData").val(JSON.stringify(chartData));
   if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[div]["Prefixfontsize"];
}else{
    prefixFontSize = gtFontSize;
}
if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
}else{
    Suffixfontsize = infoFontSize;
}
if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
    prefix = chartData[div]["Prefix"];
}else{
    prefix = "";
}
if(typeof chartData[div]["Suffix"]!="undefined" && chartData[div]["Suffix"]!="" ){
    suffix = chartData[div]["Suffix"];
}else{
    suffix = "";
}
    parent.$("#chartData").val(JSON.stringify(chartData)); 
  var html = "";
   if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
     }
  html+="<div id='mainDiv"+div+"' style='width:95%;height:auto;  background-color:#FFF'>";
//  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
//  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";
  html+="<div id='innerDiv"+div+"' style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
  html+="<table style='float:left;margin-top: '>";
  html+="<tr>";

 if(chartData[div]["chartType"]==='Combo-Analysis'){
//alert(showData1)
if(typeof showData1!="undefined" && showData1!=""){
if(suffix.trim()==""){ 
   html+="<td  style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}else{ 
   html+="<td   style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
} 
 }else{
html+="<td   style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>0</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";

}
 }else{
if(suffix.trim()==""){
   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
}else{
   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
}
}
//html+="<tr>";
// html+="<td ><img  onclick='trendChartdiv(\""+div+"\")' id='trendChart' class='trendChart1' style='float:right;margin-right: -10%;margin-top: -13%;width:30px;height:30px' src = 'images/icons/tiletrend.png'></td>";
//  html+="</tr>";

  html+="</tr>";
  html+="<tr>";
 if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     }
  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
  }else{
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
  }
  html+="</tr>";
  
   
 
  html+="</table>";
  html+="</div>";
  html+="</div>";
if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
     }
  $("#"+appendDiv).html(html);
openTableDiv = function(div){
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
    tableData[div] = data[div];
//    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    $("#openTableTile").dialog('open');
}
//$("#"+div).html(html);
trendChartdiv = function(div){
$("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Trend Chart'
    });
    $("#openTableTile").html("");
    lineData[div] = data[div];
    var divId='tile';
buildLine(div, lineData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"], 500, 390,divId)
    $("#openTableTile").dialog('open');
}
}

function KPIDash(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
   var chartData = JSON.parse(parent.$("#chartData").val());
    data = data["chart1"]
   var prefix = "";
 var suffix = "";
 var prefixFontSize = "";
 var suffixFontSize = "";
   var showData = 0;
   var showData1 = 0;
   var absoluteNumber = 0;
   var perFlag = "";
   var dashledid;
     var fromoneview=parent.$("#fromoneview").val()
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
   var imageTag =chartData[div]["filename"];
    var specialCharacter ="%";
    var colorPicker = "";
   var DataSum = 0;
   //font size start
   var gtFontSize = 0;
   var infoFontSize = 0;
   var nameArrCurrent = "";
   var nameArrPrior = "";
   var current = "";
   var prior = "";
   //font size end
   var tableData = {};

//chartData[div][div] = tableData ;

   for(var i in data){

 DataSum+= parseFloat(data[i][measureArray[0]]);
   }

   // for change %
 if(typeof chartData[div]["refElementInfo"]!="undefined" && chartData[div]["refElementInfo"]!="" ){

     var keys = Object.keys(chartData[div]["refElementInfo"])
    for(var key in keys){
    if(keys[key]==="4"){
    var innerKeys = Object.keys(chartData[div]["refElementInfo"][keys[key]]);
     if(measureArray[0].indexOf(specialCharacter)===-1){

      if(measureArray[0]!=[chartData[div]["refElementInfo"][keys[key]][innerKeys[4]]]) {
        if(measureArray[0].indexOf(specialCharacter)!=-1 ||measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1) {
           showData =   parseFloat(DataSum/(data.length));

  perFlag = "true";
 }else{
 showData = 100;
      perFlag = "false";
      absoluteNumber= parseFloat(DataSum);
 }
 }

    }else{

    for(var ikeys in innerKeys){
      if(innerKeys[ikeys]==="1"){
         for(var i in data){
          current +=   parseFloat(data[i][chartData[div]["refElementInfo"][keys[key]][innerKeys[ikeys]]]);
   }
   }
      else if(innerKeys[ikeys]==="2"){
         for(var i in data){
          prior +=   parseFloat(data[i][chartData[div]["refElementInfo"][keys[key]][innerKeys[ikeys]]]);
    }
  }
    }
     if(typeof current!="undefined" && current!="" && typeof prior!="undefined" && prior!="" ){
     showData = parseFloat(((current-prior)/prior)*100)
         perFlag = "true";
    }
    }
    }else{
      if(measureArray[0].indexOf(specialCharacter)!=-1 ||measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1) {

  showData =   parseFloat(DataSum/(data.length));

  perFlag = "true";
 }else{
 showData = 100;
      perFlag = "false";
      absoluteNumber= parseFloat(DataSum);
 }
 }

    }

 }

// if(measureArray[0].indexOf("Change%")!=-1){
//    var current = 0;
//    var prior = 0;
//    var nameArr = [];
//
//    var nameArr2 = "";
//    nameArr =  measureArray[0].split(/[ ,]+/);
//for(var k =1;k<nameArr.length;k++){
// if(nameArr.length>2){
// if(k==1){
//     nameArr2 =  nameArr[k]
//   }else{
//
//     nameArrCurrent = nameArr2.concat(nameArr[k]);
//     nameArr2 = nameArrCurrent
//
//   }
// }else{
//    nameArrCurrent = nameArr[k]
// }
// }
// nameArrPrior = "Prior "+nameArrCurrent;
//
//for(var l in data){
//
//    for(var p in measureArray){
// if(nameArrCurrent.toString().toUpperCase().replace(" ", "") == measureArray[p].toString().toUpperCase().split(" ").join("")){
//
//     current += parseFloat(data[l][measureArray[p]])
//
//   }
//   else if(nameArrPrior.toString().toUpperCase().replace(" ", "") == measureArray[p].toString().toUpperCase().split(" ").join("")){
//prior += parseFloat(data[l][measureArray[p]])
//
//   }
//    }
//  }
//  if(typeof current!="undefined" && current!="" && typeof prior!="undefined" && prior!=""){
//     showData = parseFloat(((current-prior)/prior)*100)
//
//  perFlag = "true";
//
// }else{
//  showData =   parseFloat(DataSum/(data.length));
// }
// }
else if(measureArray[0].indexOf(specialCharacter)!=-1 ||measureArray[0].indexOf("Percent")!=-1 ||measureArray[0].indexOf("percent")!=-1 ||measureArray[0].indexOf("PERCENT")!=-1) {

  showData =   parseFloat(DataSum/(data.length));

  perFlag = "true";
}else{
     showData = parseFloat(DataSum);
      perFlag = "false";
      absoluteNumber= DataSum;
 }

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
                  if(yAxisFormat==""){
//        if(yAxisRounding>0){
//            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//        }
//        else{
                            showData1 =      addCommasKPI(numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
//        }

                    }
            else{
//        if(yAxisRounding>0){
//            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//        }
//        else{
                            showData1 =      addCommasKPI(numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
//        }

                }

if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){

for(var i in measureArray){
    if(chartData[div]["KPIName"]!=measureArray[i]){
}else{
     chartData[div]["KPIName"] = measureArray[0];
}
        }
        }
else{

chartData[div]["KPIName"] = measureArray[0];

}
if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
  colorPicker = chartData[div]["colorPicker"];
}else{
    colorPicker = "#36C";
}

 if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== ''){
        
//             gtFontSize=divHeight*.13;
//              gtFontSize = divWidth*.1
//             font=width/39.2;
            gtFontSize=gtFontSize>45?45:gtFontSize;
            gtFontSize=gtFontSize<35?35:gtFontSize;
            // add by maynk sh. for fontsize
      
             if(typeof chartData[div]["kpiGTFont"]!=="undefined"){
                      gtFontSize =  chartData[div]["kpiGTFont"];
             }
 }else{
if(divHeight<divWidth){
  gtFontSize = divHeight*.13
//  infoFontSize = divHeight*.068
}else{
   gtFontSize = divWidth*.1
//    infoFontSize = divWidth*.058
}
 }
chartData[div]["kpiGTFont"]=gtFontSize;
  if(typeof chartData[div]["kpiFont"]!="undefined" || chartData[div]["kpiFont"] === ''){
        
//             infoFontSize=divHeight*.068;
//              infoFontSize = divWidth*.058
//             font=width/39.2;
            infoFontSize=infoFontSize>45?45:infoFontSize;
            infoFontSize=infoFontSize<35?35:infoFontSize;
            // add by maynk sh. for fontsize
      
             if(typeof chartData[div]["kpiFont"]!=="undefined"){
                      infoFontSize =  chartData[div]["kpiFont"];
             }
 }else{
if(divHeight<divWidth){
//  gtFontSize = divHeight*.13
  infoFontSize = divHeight*.068
}else{
//   gtFontSize = divWidth*.1
    infoFontSize = divWidth*.058
}
 }
 chartData[div]["kpiFont"]=infoFontSize;
 $("#chartData").val(JSON.stringify(chartData));

//if(divHeight<divWidth){
//  gtFontSize = divHeight*.13
//  infoFontSize = divHeight*.068
//}else{
//   gtFontSize = divWidth*.1
//    infoFontSize = divWidth*.058
//}
if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[div]["Prefixfontsize"];
}else{
    prefixFontSize = infoFontSize;
}
if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
}else{
    Suffixfontsize = infoFontSize;
}
if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
    prefix = chartData[div]["Prefix"];
}else{
    prefix = "";
}
if(typeof chartData[div]["Suffix"]!="undefined" && chartData[div]["Suffix"]!="" ){
    suffix = chartData[div]["Suffix"];
}else{
    suffix = "";
}

var numbersubString1 = "";
var numbersubString2 = "";
var subString1 = "";
var subString2 = "";
var subString3 = "";
if(showData1.length.toString()>6){
 numbersubString1 = showData1.substring(0, 6);
 numbersubString2 = showData1.substring(6,showData1.length );
 gtFontSize = gtFontSize*.8;
}
if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
    if(chartData[div]["KPIName"].length>130){
      subString1 =   chartData[div]["KPIName"].substring(0,60);
      subString2 =   chartData[div]["KPIName"].substring(60,130);
      subString3 =   chartData[div]["KPIName"].substring(130,chartData[div]["KPIName"].length.toString());
    }
    if(chartData[div]["KPIName"].length>60 && chartData[div]["KPIName"].length<130){
    subString1 =   chartData[div]["KPIName"].substring(0,60);
    subString2 =   chartData[div]["KPIName"].substring(60,chartData[div]["KPIName"].length.toString());
    }
    else {

    }
}


parent.$("#chartData").val(JSON.stringify(chartData));
if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
}
  var html = "";
  html+="<div id='mainDiv"+div+"' style='width:95%;height:auto;  background-color:#FFF'>";
//  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
//  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";

  // upper part start
//  html+="<div id='innerDiv1"+div+"' style='margin-top:%; float:left;width:"+divWidth*.95+"px;height:"+divHeight*.25+"px; background-color:#FFF'>";
//  html+="<table style='float:left; '>";
//  html+="<tr>";
//
//  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
//
//  if(chartData[div]["KPIName"].length>60){
//
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+subString1+"</span></td>";
//     }else{
//   html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
//     }
//}else{
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
//  }
//  html+="</tr>";
//  html+="</table>";
//  html+="</div>";

// upper part end
// Figures (nuumbers) part start
//  html+="<div id='innerDiv"+div+"' style='margin-top:20%; float:left;width:"+divWidth*.35+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
    if(typeof chartData[div]["valueTilealign"]==="undefined" || chartData[div]["valueTilealign"]==="Center"){
    html+="<div id='innerDiv"+div+"' style='margin-top:20%; float:left;width:"+divWidth*.35+"px;background-color:#FFF'>";
 }else if(chartData[div]["valueTilealign"]==="Top"){
  html+="<div id='innerDiv"+div+"' style='margin-top:2%; float:left;width:"+divWidth*.35+"px;background-color:#FFF'>";
 }else{
    html+="<div id='innerDiv"+div+"' style='margin-top:35%; float:left;width:"+divWidth*.35+"px;background-color:#FFF'>";
}// add by mayank sharma
  html+="<table style='float:left; '>";
   if(typeof imageTag!="undefined"){
    html+="<tr>";
//    html+="<td ><img class='imageStyle' style='width:70px' src = 'images/mobile/"+imageTag+"'></td>";
    html+="<td ><img class='' style='width:70px' src = 'images/mobile/"+imageTag+"' onclick='showCharts()'></td>";
  html+="</tr>";
  }
  html+="<tr>";

if(showData1.length.toString()>6){
   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*.8+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+numbersubString1+"</span></td></tr>";
   html+="<tr><td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*.8+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+numbersubString2+"</span></td>";
}else{
     if(typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] =="Y"){
    if(suffix.trim()==""){
    html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+suffix+"</span></td>";
    }else{
    html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+suffix+"</span></td>";
    }
     } else{
       if(suffix.trim()==""){
    html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
    }else{
    html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
    }  
     }
   //html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+showData1+"</span></td>";
}
  html+="</tr>";


  html+="</table>";
  html+="</div>";

// Figures (nuumbers) part end
// centre part start
if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
}
// add by mayank sharma
if(typeof chartData[div]["Tilealign"]==="undefined" || chartData[div]["Tilealign"]==="Top"){
   html+="<div id='innerDiv2"+div+"' style='margin-top:2%;margin-right:5%;float:right;width:"+divWidth*.48+"px;background-color:#FFF'>";
 }else if(chartData[div]["Tilealign"]==="Center"){
   html+="<div id='innerDiv2"+div+"' style='margin-top:20%;margin-right:5%;float:right;width:"+divWidth*.48+"px;background-color:#FFF'>";
}else{
    html+="<div id='innerDiv2"+div+"' style='margin-top:50%;margin-right:5%;float:right;width:"+divWidth*.48+"px;background-color:#FFF'>";
  }

// if(typeof chartData[div]["Tilealign"]!="undefined" && chartData[div]["Tilealign"]!="" && chartData[div]["Tilealign"]!="Top"){
//    html+="<div id='innerDiv2"+div+"' style='margin-top:20%;margin-right:5%;float:right;width:"+divWidth*.48+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
//}else{
//  html+="<div id='innerDiv2"+div+"' style='margin-right:5%;float:right;width:"+divWidth*.48+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
//  }
  html+="<table style='float:left; '>";
  html+="<tr>";

  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  if(chartData[div]["KPIName"].length>60){
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[div]["KPIName"]+"</span></td>";
  }
  else if(chartData[div]["KPIName"].length<60){
    html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[div]["KPIName"]+"</span></td>";
  }
  else{
    html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+measureArray[0]+"</span></td>";
  }
}else{
  html+="<td style='margin-top:20%;text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+measureArray[0]+"</span></td>";
  }
  html+="</tr>";
  html+="</table>";
  html+="</div>";
  html+="</div>";
if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
}
  $("#"+div).html(html);

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
   tableData[div] = data[div];
   if(fromoneview!='null'&& fromoneview=='true'){
        buildTable(dashledid+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }else{
    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
   }
    $("#openTableTile").dialog('open');
}
}


//function openTableDiv1(div){
//  var chartData = JSON.parse(parent.$("#chartData").val());
//$("#openTableTile").dialog({
//         autoOpen: false,
//         height: 440,
//         width: 620,
//         position: 'justify',
//         modal: true,
//         resizable:true,
//        title:'Graph Properties'
//    });
//   $("#openTableTile").html("");
//
// //  $("#openTableTile").dialog('open');
//
////$("#openTableTile").append(htmlstr)
////buildTable(div+"Tile",chartData[div][div], chartData[div][div][div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//
//$("#openTableTile").dialog('open');
// }


 

function buildEmojiChart(chartId,data, viewbys, measures, divwidth, divHght,KPIresult){
    var html = "";
    var chartData = JSON.parse(parent.$("#chartData").val());
    var prefix = "";
 var suffix = "";
 var prefixFontSize = "";
 var suffixFontSize = "";
 var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=chartId;
chartId=div1;
     }

 var tableData = {};
tableData[chartId] = data;

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
    var gt=addCurrencyType(chartId, chartData[chartId]["meassureIds"][0]) + addCommas(numberFormat(KPIresult,yAxisFormat,yAxisRounding,chartId));
    var ctxPath = parent.document.getElementById("h").value;
    var height=$("#"+chartId).height();
    var width=$("#"+chartId).width();
    height=height<width?height:width;
    
   var fontSize;
    if(typeof chartData[chartId]["kpiFont"]!=="undefined" && chartData[chartId]["kpiFont"] !== ''){
fontSize=chartData[chartId]["kpiFont"];}
 else{
     
     fontSize=width*.05;
    fontSize=fontSize>20?20:fontSize;
 }
   chartData[chartId]["kpiFont"]=fontSize; 
   
   var fontSize1;
    if(typeof chartData[chartId]["kpiGTFont"]!=="undefined" && chartData[chartId]["kpiGTFont"] !== ''){
fontSize1=chartData[chartId]["kpiGTFont"];}
 else{
     
     fontSize1=width*.05;
    fontSize1=fontSize1>20?20:fontSize1;
 }
 chartData[chartId]["kpiGTFont"]=fontSize1;
 $("#chartData").val(JSON.stringify(chartData)); //emojicharts1
 
 var colorPicker;
 var measureColor;
 
 if(typeof chartData[chartId]["colorPicker"]!="undefined" && chartData[chartId]["colorPicker"]!=""){
  colorPicker = chartData[chartId]["colorPicker"];
}else{
    colorPicker = "#696969";
}
if(typeof chartData[chartId]["lFilledFont"]!="undefined" && chartData[chartId]["lFilledFont"]!=""){
  measureColor = chartData[chartId]["lFilledFont"];
}else{
measureColor = "#A1A1A1";
}
var measureAliaskpi = "";
if(typeof chartData[chartId]["measureAliaskpi"]!=="undefined" && chartData[chartId]["measureAliaskpi"]!==measures[0]){
                  measureAliaskpi=chartData[chartId]["measureAliaskpi"];
            }else{
                    measureAliaskpi=measures[0];
            }
 if(typeof chartData[chartId]["Prefixfontsize"]!="undefined" && chartData[chartId]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[chartId]["Prefixfontsize"];
}else{
    prefixFontSize = fontSize;
}
if(typeof chartData[chartId]["Suffixfontsize"]!="undefined" && chartData[chartId]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[chartId]["Suffixfontsize"];
}else{
    Suffixfontsize = fontSize;
}
if(typeof chartData[chartId]["Prefix"]!="undefined" && chartData[chartId]["Prefix"]!="" ){
    prefix = chartData[chartId]["Prefix"];
}else{
    prefix = "";
}
if(typeof chartData[chartId]["Suffix"]!="undefined" && chartData[chartId]["Suffix"]!="" ){
    suffix = chartData[chartId]["Suffix"];
}else{
    suffix = "";
}
    parent.$("#chartData").val(JSON.stringify(chartData));
 
    var lowMin=0,lowMax=0,medMin=0,medMax=0,highMin=0,highMax=0; 
    if(typeof chartData[chartId]["dialValues"] !="undefined" && chartData[chartId]["dialValues"] !=""){
        if(typeof chartData[chartId]["dialValues"]["minGreeen"] !="undefined" && chartData[chartId]["dialValues"]["minGreeen"] !=""){
            lowMin = chartData[chartId]["dialValues"]["minGreeen"];
        }
        if(typeof chartData[chartId]["dialValues"]["maxGreeen"] !="undefined" && chartData[chartId]["dialValues"]["maxGreeen"] !=""){
            lowMax = chartData[chartId]["dialValues"]["maxGreeen"];
        }
        if(typeof chartData[chartId]["dialValues"]["minOrange"] !="undefined" && chartData[chartId]["dialValues"]["minOrange"] !=""){
            medMin = chartData[chartId]["dialValues"]["minOrange"];
        }
        if(typeof chartData[chartId]["dialValues"]["maxOrange"] !="undefined" && chartData[chartId]["dialValues"]["maxOrange"] !=""){
            medMax = chartData[chartId]["dialValues"]["maxOrange"];
        }
        if(typeof chartData[chartId]["dialValues"]["minRed"] !="undefined" && chartData[chartId]["dialValues"]["minRed"] !=""){
            highMin = chartData[chartId]["dialValues"]["minRed"];
        }
        if(typeof chartData[chartId]["dialValues"]["maxRed"] !="undefined" && chartData[chartId]["dialValues"]["maxGreeen"] !=""){
            highMax = chartData[chartId]["dialValues"]["maxRed"];
        }
    }
 var imageName="normal_emoji.png";
    var dataInt=parseInt(data);
    if(dataInt<lowMax){
        imageName="sad_emoji.png";
    }
    else if(dataInt>=medMin && dataInt<medMax){
        imageName="normal_emoji.png";
    }
    else if(dataInt>=highMin ){
        imageName="happy_emoji.png";
    }
    var smaller=width<height?width:height;
    html +=  "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght*0.9 + "px; overflow-y: auto;overflow-x: hidden\">";
    if(typeof chartData[chartId]["emojiPosition"]==='undefined' || chartData[chartId]["emojiPosition"]==='top'){
        html +=  "<div style='width:100%;height:60%'>";
    }
    else{
        html +=  "<div style='float:left;width:60%;height:100%;margin-top:"+(height/10)+"px'>";
    }
    html +=  "<img src='"+ctxPath+"/images/"+imageName+"' width='"+(smaller*0.65)+"px'>";
    html += "</div>";
    if(typeof chartData[chartId]["emojiPosition"]=='undefined' || chartData[chartId]["emojiPosition"]==='top'){
        html += "<div style='width:100%;height:40%'>";
    }
    else{
        html += "<div style='float:right;width:40%;height:100%;margin-top:"+(height/2-(height/10))+"px'>";
    }
    
  html+="<table style='margin-top: '>";
 
 
 html += "<td><span style='word-wrap: break-word;font-size:"+fontSize+"px;font-weight:bold;color:#555555;color:"+measureColor+";' align='center'>"+measureAliaskpi+" :</span><span id='"+chartId+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;color:"+colorPicker+";font-synthesis: weight style;font-variant: normal;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+prefix+"</span></td>";
  if(typeof chartData[chartId]["appendNumberFormat"]==="undefined" || chartData[chartId]["appendNumberFormat"] =="Y"){
   if(suffix==""){ 
   html+="<td onclick='openTableDiv(\""+chartId+"\")' onmouseover='fontSizeIncrease(\""+fontSize1+"\",\""+chartId+"\")' onmouseout='fontSizeDecrease(\""+fontSize1+"\",\""+chartId+"\")' ><span style='word-wrap: break-word;font-size:"+fontSize1+"px;font-weight:bold;color:#555555;color:"+colorPicker+";' align='center'>"+gt+"</span><span id='"+chartId+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;color:"+colorPicker+";font-synthesis: weight style;font-variant: normal;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+suffix+"</span></td>";
   }else{
       html+="<td onclick='openTableDiv(\""+chartId+"\")' onmouseover='fontSizeIncrease(\""+fontSize1+"\",\""+chartId+"\")' onmouseout='fontSizeDecrease(\""+fontSize1+"\",\""+chartId+"\")' ><span style='word-wrap: break-word;font-size:"+fontSize1+"px;font-weight:bold;color:#555555;color:"+colorPicker+";' align='center'>"+gt.replace(/[^0-9\.]/g, '')+"</span><span id='"+chartId+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;color:"+colorPicker+";font-synthesis: weight style;font-variant: normal;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+suffix+"</span></td>";
   }
  }else{
    if(suffix==""){ 
   html+="<td onclick='openTableDiv(\""+chartId+"\")' onmouseover='fontSizeIncrease(\""+fontSize1+"\",\""+chartId+"\")' onmouseout='fontSizeDecrease(\""+fontSize1+"\",\""+chartId+"\")' ><span style='word-wrap: break-word;font-size:"+fontSize1+"px;font-weight:bold;color:#555555;color:"+colorPicker+";' align='center'>"+gt+"</span><span id='"+chartId+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;color:"+colorPicker+";font-synthesis: weight style;font-variant: normal;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
   }else{
       html+="<td onclick='openTableDiv(\""+chartId+"\")' onmouseover='fontSizeIncrease(\""+fontSize1+"\",\""+chartId+"\")' onmouseout='fontSizeDecrease(\""+fontSize1+"\",\""+chartId+"\")' ><span style='word-wrap: break-word;font-size:"+fontSize1+"px;font-weight:bold;color:#555555;color:"+colorPicker+";' align='center'>"+gt.replace(/[^0-9\.]/g, '')+"</span><span id='"+chartId+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;color:"+colorPicker+";font-synthesis: weight style;font-variant: normal;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></span></td>";
   }   
  }
    

  html+="</table>";
    html += "</div>";
    html += "</div>";
    
  
    $("#" + chartId).html(html);
    
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
        buildTable(dashledid+"Tile",tableData, tableData[chartId],chartData[chartId]["viewBys"] ,chartData[chartId]["meassures"] , 590, 620)
   }else{
    buildTable(chartId+"Tile",tableData, tableData[chartId],chartData[chartId]["viewBys"] ,chartData[chartId]["meassures"] , 590, 620)
}
    $("#openTableTile").dialog('open');
}

    
}

function buildComboPie(div,divId, data1, columns, measureArray,wid,hgt) {
      var records=12;// add for no of records by mynk sh.
      var data = [];
      
      for(var h=0; h<(data1.length < records ? data1.length : records); h++){
              
                    data.push(data1[h]);
                }  //end for no of records

    var dashletid;
// if(fromoneview!='null'&& fromoneview=='true'){
var chartData = JSON.parse(parent.$("#chartData").val());
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
     
     var counter=0;
   if(typeof chartData["chart1"]["customColors"]!=='undefined')
    {
        var topColors=chartData["chart1"]["customColors"];
        for (var key in topColors) {
            if (topColors.hasOwnProperty(key)) {
//                alert(key + " -> " + topColors[key]);
//                color(counter)=topColors[key];
            }
            counter++;
        }
    }
    var divWidth,divWid, divHeight, rad;
  divWidth=wid;
divWid=wid;
 divHeight=hgt-20;
    rad=(Math.min((divWidth*.7), divHeight))*.55;
//    rad=(Math.min(divWidth, divHeight))*.50;
    var autoRounding1 = "1d";
var chartData = JSON.parse(parent.$("#chartData").val());
var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
var legendAlign;
if(typeof chartData[div1]["legendLocation"]==='undefined' || chartData[div1]["legendLocation"]==='Right')
{
    legendAlign='Right';
}
else
{
    legendAlign='Bottom';
}
dashletid=div;
}else{
       var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];

var legendAlign;
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Right')
{
    legendAlign='Right';
}    
else
{
    legendAlign='Bottom';
}
}

    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
//    var fun = "drillWithinchart(this.id,\""+div+"\")";
    //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
	}else{
             fun = "drillWithinchart(this.id,\""+div+"\")";
	 if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
            fun = "drillChartInDashBoard(this.id,'" + div + "')";
	}
        }
        function drillFunct(id1){
            if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
            drillChartInDashBoard(id1,div);
    } else {
        drillWithinchart(id1,div);
        }
	}
	

    var divnum = parseFloat(div.replace("chart", "", "gi"));
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    
 if(fromoneview!='null'&& fromoneview=='true'){
var regid=div.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "oneviewdrillWithinchart1(this.id)";
var olap=div.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
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
    var margintop;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
        //        margintop = "30px";
        radius = rad;
        height = Math.min(width, height);
        width = Math.min(width, height);
        radius = (Math.min(width, height) / 2.7);
    }
    else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "120px";
        radius = rad;
    }
    else {
        height = divHeight;
        margintop = "0px";
        radius = rad / 1.2;
    }
    if(legendAlign==='Bottom')
    {
        if(width>height)
        {
            radius*=0.9;
        }
    }
    radius*=1.25;
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie() //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
            var topMargin;
            if(legendAlign=='Right')
                {
                    topMargin=0; 
                }
            else
                {
                    topMargin=divHeight/9;
                }
    var svg = d3.select("#" + divId)
               //    added by manik
            // .append("div")
//            .classed("svg-container", true)
            // .attr("width", "100%")
            // .attr("height", "100%")
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
//            .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ")
//            .classed("svg-content-responsive", true)
            .datum(data)
            .attr("width", function(){
                    if(chartType=="Pie-Dashboard"){
                      return  width-200;
                    }else {
                        return width;
                    }
            })
            .attr("height", height*1.3)
            .attr("style", function(){
                 if(chartType=="Pie-Dashboard"){
                      return  ""
                    }else {
                        return margintop;
                    }
            });
//            var remarks;
//
//            if(typeof chartData[div]["remarks"]!=="undefined" && chartData[div]["remarks"]!="" )
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
//        .attr("width", width)
//        .attr("height", remarkHeight/19)
//        .attr('x', 0)
//        .attr( 'y', remarkHeight-(remarkHeight/19)-26)
//        .append("xhtml:body")
//        .attr('xmlns','http://www.w3.org/1999/xhtml')
//        .html("<input id='txt"+div+"' type='text' style='float:left;height: "+height/20+"px;width: "+(width-40)+"px;display:none' value='"+remarks+"'/><label id='lbl"+div+"'  style='background-color:#F2F2F2;color:black;font-size:10px;text-align:left;height: "+height/20+"px;width: "+width+"px; ' value='"+remarks+"'><a id='a_"+div+"' style='margin-right:20px;float:right;width:16px;' onclick='editRemarks(\""+div+"\")' class='ui-icon ui-icon-pencil'></a>"+remarks+"</label>")
//        .on("change", function() {displayText('txt'+div,div)});
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
//    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
//            .append("svg:linearGradient")
//            //    .attr("id", function(d) {return color(d.name);)
//            .attr("id", function(d) {
//
//                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//            .attr("x1", "0%")
//            .attr("y1", "0%")
//            .attr("x2", "50%")
//            .attr("y2", "100%")
//            .attr("spreadMethod", "reflect");
//
//    gradient.append("svg:stop")
//            .attr("offset", "0%")
//            .attr("stop-color", "rgb(240,240,240)")
//            .attr("stop-opacity", 1);
//    gradient.append("svg:stop")
//            .attr("offset", "60%")
//            .attr("stop-color", function(d, i) {
//                var colorShad;
//                var drilledvalue;
//                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                } catch (e) {
//                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[colIds[0]]) !== -1) {
//                    colorShad = drillShade;
//                }
//                else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
//                    var drills = {};
//                    if (parent.$("#drills").val() !== "") {
//                        drills = JSON.parse(parent.$("#drills").val());
//                    }
//                    var keysOfdata = (Object.keys(drills));
//                    var len = keysOfdata.length;
//                    for (var j = divnum - 1; j < len; j++) {
//                        if (drills[keysOfdata[j]].indexOf(d[columns[0]]) !== -1) {
//                            colorShad = drillShade;
//                        }
//                        else {
//                            colorShad = getDrawColor(div, parseInt(i));
//                        }
//                    }
//                    colorShad = getDrawColor(div, parseInt(i));
//                }
//                else {
////                    if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
////                        colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
////                    } else {
//                        colorShad = getDrawColor(div, parseInt(i));
////                        colorShad = "blue";
////                    }
//                }
//                chartMap[d[columns[0]]] = colorShad;
//                return getDrawColor(div, parseInt(i));
////                return colorShad;
//            })
//            .attr("stop-opacity", 1);
   // parent.$("#colorMap").val(JSON.stringify(colorMap));
var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            // .attr("class", "arc")
            .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];

            })
             .attr("transform",function(d){
                
              if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None")||legendAlign==='Bottom'){
            return "translate(" + width / 2 + "," + height / 2 + ")";

             }else{
              return "translate(" + width / 3 + "," + height / 1.5 + ")";
             }
             })

            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	});

    arcs.append("path")
            .attr("fill", function(d,i) {
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{

                    var drawColor=getDrawColor(div, parseInt(i));
//                    alert(div+"####"+drawColor);
                    return drawColor;
                }

            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                var drawColor=getDrawColor(div, parseInt(i));
                    return drawColor;
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);
                 if(fromoneview!='null'&& fromoneview=='true'){
                 show_detailsoneview(d.data, columnList, measureArray, this,chartData,div1);
                 }else{
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                 }
                show_details(d.data, columnList, measureArray, this,div);

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
                hide_details(d, i, this);
            })
            .transition()
            .ease("bounce")
            .duration(2000)
            .attrTween("d", tweenPie);
    function angle(d) {
        return (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
    }
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "start")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 12px")
            .attr("transform", function(d) {
                return "translate(" + arcFinal.centroid(d) + ")rotate(" + angle(d) + ")";
            });
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("style", "font-family: lucida grande")
           //            .attr("style", "font-size: 10px")
            .attr("style", "font-size:"+ parseInt(width/45)+"px")
//            .style("fill", function(d,i){
//                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
//                return "white"
//            else
//                return "black"
//            })
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius / 6;
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius / 2;
                }
                // Set Inner Coordinate
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .attr("fill", function(d, i){
            var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {
                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                               }
                               return lableColors;
             })
            .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes")  {
if (typeof displayType !== "undefined" && displayType === "Absolute") {

                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);

                }
                else {
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
            }else {
                return "";
            }
            });

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
    if(chartType!=="Pie-Dashboard"){
var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width/45);
		var fontsize1 = parseInt(width/50);
		var rectsize = parseInt(width/60);
                if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
        if(legendAlign==='Right')
        {   
	yvalue = parseInt(height / 4);

        rectyvalue = parseInt((height / 4)-10);
        }    
        else
        {
            if(width<height)
            {
                yvalue = parseInt(height*0.9);
                rectyvalue = parseInt((height*0.89 ));    
            }
            else
            {
                yvalue = parseInt(height*1.05);
                rectyvalue = parseInt((height*1.030 ));    
            }
        }

        $("#txt"+div).css("font-size",fontsize1+"px");
        $("#lbl"+div).css("font-size",fontsize1+"px");
//        $("#a_"+div).height(fontsize1*1.5);
//        $("#a_"+div).width(fontsize1*1.5);

        var count = 0;
        var transform = "";
 if(typeof displayLength!="undefined" && displayLength!=""&& displayLength!="Yes"){}
 else
     {

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength!="None")){
            if(legendAlign==='Right')
                {
                    
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
//            .attr("x",rectlen)
//            .attr("y",rectyvalue1)
            .attr("fill", "Black")

//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

//            .text("" + measureArray[i] + "");
            .text(function(d){
              return columns[0]
            })
                            for(var i=0;i<(data.length<15 ? data.length : 15) ;i++){
                     if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
               }
            }
}
                else
                   {
                   var widthvalue = parseInt((width *.1));
            var widthRectvalue = parseInt((width *.1)-(width*.018));
                       for(var i=0;i<(data.length<25 ? data.length : 25) ;i++){
                     if(data[i][measureArray[0]]>0){
                 var charactercount = data[i][columns[0]].length;
              if(i!=0){

               widthvalue = parseInt(widthvalue +  (width*.18) )
               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
               if(count%5==0){
        widthvalue = parseInt((width *.1));
        widthRectvalue = parseInt((width *.1)-(width*.018));
             if(width<height){
//                  yvalue = parseInt(height*0.9);
//            rectyvalue = parseInt((height*0.85 ));   
              yvalue = parseInt(yvalue-width*.06)
         rectyvalue = parseInt(rectyvalue-width*.06)
             }else{
                yvalue = parseInt(yvalue-height*.06)
         rectyvalue = parseInt(rectyvalue-height*.06)
             }

     }
    }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
          //  .attr("style","margin-right:10")
         //   .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", color(i))
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
            .attr("fill", function(){

             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  color(i);
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")

            .attr("id",function(d){
            //    return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>9){
                    return data[i][columns[0]].substring(0, 9)+"..";
                }else {
                    return data[i][columns[0]];
          }
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
               }
            }
                   }
}
            if(typeof displayLength!="undefined" && displayLength!="None" && displayLength!="All"){
               for(var i=0;i<displayLength;i++){
                     if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id)
                    })
              count++
               }}
            }else if(typeof displayLength!="undefined" && displayLength=="All"){
               for(var i in  data){
                      if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id)
                    })
              count++
            }}
            }else if(typeof displayLength!="undefined" && displayLength=="None"){

            }else{

}
     }
     }
    if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
    }
     
}

function buildStackedKPI(div, data, columns, measureArray, divWidth, divHeight,KPIresult,flag,appenddiv1){
   var appendDiv = "";
   window.t="header::header";
if(div.indexOf("SkpiDiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
    if(typeof appenddiv1!="undefined" && appenddiv1!=""){
   appendDiv =   appenddiv1   
    }else{
        
appendDiv = div
    }

}
//alert(appendDiv)
//if(typeof flag!='undefined' && flag!=='' && (typeof appenddiv1=="undefined" || appenddiv1=="") ){  
//    appendDiv=flag;
//}  // comment by mayank sh for trend kpi chart

    var onClickFunction='openTableDiv1';
    if(typeof flag!='undefined' && flag==='trendKPI'){
        onClickFunction='updateTrend';
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true' && chartData[div]["comparisonType"]!='Custom'){
        buildStackedKPIComp(div, data, columns, measureArray, divWidth, divHeight,KPIresult)
        return;
    }
    var dataArray = [];
    var fontSize1;
    var fontSize2;
    var preSuffData =[];
 var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
     graphProp(div);
 var tableData = {};
tableData[div] = data;
//    if(divWidth<divHeight){
//        fontSize1=divWidth/25;
//    }
//    else{
//        fontSize1=divHeight/25;
//    }
//    
//    if(fontSize1<10)
//    {
//        fontSize1=10;
//            
//    }
//    if(fontSize1>30){
//        fontSize1=30;
//    }
//    fontSize2=fontSize1;
    
//   for(var j in measureArray){
//    if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[j]]!='undefined' && chartData[div]["measureNameSize"][measureArray[j]]!='undefined'){
//        fontSize1=chartData[div]["measureNameSize"][measureArray[j]];
//    }else {
          if(divWidth<divHeight){
        fontSize1=divWidth/25;
    }
    else{
        fontSize1=divHeight/25;
    }
    
    if(fontSize1<10)
    {
        fontSize1=10;
            
    }
    if(fontSize1>30){
        fontSize1=30;
    }
//    chartData[div]["measureNameSize"]=fontSize1;
//    }

//    if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' ){
//        fontSize2=chartData[div]["kpiGTFont"];
//    }else {
          if(divWidth<divHeight){
        fontSize2=divWidth/25;
    }
    else{
        fontSize2=divHeight/25;
    }

    if(fontSize2<10)
    {
        fontSize2=10;

    }
    if(fontSize2>30){
        fontSize2=30;
    }
//    chartData[div]["kpiGTFont"]=fontSize2;
//    }
    $("#chartData").val(JSON.stringify(chartData));
    var currentMeasure=chartData[div]["currentMeasures"];
// fontSize2=fontSize1;
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate
    for(var m in measureArray){
        DataSum_measureArray[m]=0;
        for(var d in data){
            DataSum_measureArray[m]+= parseFloat(data[d][measureArray[m]]);
        //            alert(parseFloat(data[d][measureArray[m]]));
        }
        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
        dataArray.push( DataSum_measureArray[m])
    }
    //    endof GT claculat
    //    alert(DataSum_measureArray);
    var tileWidth,tileHeight,rowCount,wDiv;
     var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[div]["yAxisFormat"];
  }else{
   yAxisFormat = "";
  }
  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
      yAxisRounding = chartData[div]["rounding"];
  }else{
   yAxisRounding =0;
  }
 //....
  DataSum_measureArray=[];
for(var k in KPIresult){
    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[k]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[k]];
    }
    else{
        yAxisFormat="Auto";
    }
    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[k]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[k]];
    }
    else{
        yAxisRounding="1";
    }
                  if(yAxisFormat==""){

                    DataSum_measureArray1 =      addCurrencyType(div, getMeasureId(measureArray[k])) + addCommas(numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div));
                    }
            else{
             DataSum_measureArray1 =        addCurrencyType(div, getMeasureId(measureArray[k])) + numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div);
                }

    DataSum_measureArray.push(DataSum_measureArray1)

}
//    DataSum_measureArray= KPIresult;
    var color1,color2;
    if(typeof chartData[div]["stackLight"]!=='undefined' && chartData[div]["stackLight"]!==''){
        color1=chartData[div]["stackLight"]
    }
    else{
        color1=tileLightColor[0];
    }
    if(typeof chartData[div]["stackDark"]!=='undefined' && chartData[div]["stackDark"]!==''){
        color2=chartData[div]["stackDark"]
    }
    else{
        color2=tileDarkColor[0];
    }
for(var q in chartData[div]["meassures"]){
    var prefix = "";
    var suffix = "";
    var prefixFontSize = "";
    var suffixFontSize = "";
    if(typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" && typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" ){
    prefixFontSize = chartData[div]["PrefixfontsizeList"][q];
}else{
    prefixFontSize = fontSize2;
}
if(typeof chartData[div]["SuffixfontsizeList"]!="undefined" && chartData[div]["SuffixfontsizeList"]!="" &&  typeof chartData[div]["SuffixfontsizeList"][q]!="undefined" && chartData[div]["SuffixfontsizeList"][q]!="" ){
    Suffixfontsize = chartData[div]["SuffixfontsizeList"][q];
}else{
    Suffixfontsize = fontSize2;
}
if(typeof chartData[div]["PrefixList"]!="undefined" && chartData[div]["PrefixList"]!="" && typeof chartData[div]["PrefixList"][q]!="undefined" && chartData[div]["PrefixList"][q]!="" ){
    prefix = chartData[div]["PrefixList"][q];
}else{
    prefix = "";
}
if(typeof chartData[div]["SuffixList"]!="undefined" && chartData[div]["SuffixList"]!="" && typeof chartData[div]["SuffixList"][q]!="undefined" && chartData[div]["SuffixList"][q]!="" ){
    suffix = chartData[div]["SuffixList"][q];
}else{
    suffix = "";
}
if(typeof DataSum_measureArray[q]!="undefined" && DataSum_measureArray[q]!=""){

}else{
DataSum_measureArray[q] =0;
}
if(typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] =="Y"){
if(suffix==""){
preSuffData.push("<span style='font-family:inherit;font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-family:inherit;font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");
}else{
preSuffData.push("<span style='font-family:inherit;font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-family:inherit;font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");   
   }
}else{
  if(suffix==""){
preSuffData.push("<span style='font-family:inherit;font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-family:inherit;font-size:"+Suffixfontsize+"px'></span>");
}else{
preSuffData.push("<span style='font-family:inherit;font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-family:inherit;font-size:"+Suffixfontsize+"px'></span>");   
   }
}
   }
       
   var font1=[],font2=[];
   for(var i1 in measureArray){
       if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[i1]]!='undefined' && chartData[div]["measureNameSize"][measureArray[i1]]!='undefined'){
           font1[i1]=chartData[div]["measureNameSize"][measureArray[i1]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font1[i1]=14;
           }
           else{
           font1[i1]=16;
       }
   }
   }
   for(var i2 in measureArray){
       if(typeof chartData[div]["measureValueSize"]!='undefined' && typeof chartData[div]["measureValueSize"][measureArray[i2]]!='undefined' && chartData[div]["measureValueSize"][measureArray[i2]]!='undefined'){
           font2[i2]=chartData[div]["measureValueSize"][measureArray[i2]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font2[i2]=20;
           }
           else{
           font2[i2]=25;
       }
   }
   }
   var measureAlias=[];
   for(var a in measureArray){
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[a]]!='undefined' && chartData[div]["measureAlias"][measureArray[a]]!== measureArray[a]){
            measureAlias[a]=chartData[div]["measureAlias"][measureArray[a]];
}else{
            measureAlias[a]=measureArray[a];
        }
    }
   var dataColor=[];
   for(var b in measureArray){
       if(typeof chartData[div]["dataColor"]!='undefined' && typeof chartData[div]["dataColor"][measureArray[b]]!='undefined' && chartData[div]["dataColor"][measureArray[b]]!='undefined'){
           dataColor[b]=chartData[div]["dataColor"][measureArray[b]];
}else{
            dataColor[b]="#696969";
        }
    }
   var measureColor=[];
   for(var c in measureArray){
       
   if(typeof chartData[div]["measureColor"]!='undefined' && typeof chartData[div]["measureColor"][measureArray[c]]!='undefined' && chartData[div]["measureColor"][measureArray[c]]!='undefined'){          
            measureColor[c]=chartData[div]["measureColor"][measureArray[c]];
}else{
    if(typeof currentMeasure!=='undefined' && currentMeasure.length!=0){
        var colorIndex=currentMeasure.indexOf(measureArray[c]);
        if(colorIndex==-1){
      measureColor[c]="#A1A1A1";
        }
        else{
            measureColor[c]=getDrawColor(div,colorIndex);
    }
    }
    else{
      measureColor[c]="#A1A1A1";
    }
        }
    }
     
    
    var htmlstr = "";
    var measureCount=measureArray.length;
    if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid
    }

    if(measureCount==1){
        tileWidth=$("#"+div).width()-9;
        tileHeight=divHeight-20;
        for(var i=0;i<measureCount;i++){
            if ( tileWidth> 80)
            {
                htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
            }
            else {
                 htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div style='background-color:white;width:100%;height:80%;float:top;margin-top: 10%;'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
             }
           
//          
//            htmlstr +="<div class='maindiv'  style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div class='editdiv'  style='top:10px;float:left;'><a title='change image' href='#'onclick='addImageTag1(chart1)'><img style='position:absolute;display:none' src='images/icons/edit.png' height='15px' width='15px'/></a></div><img src='images/icons/measure2.png'/></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:"+datacolor[i]+";'><div style='background-color:#fff;font-style:bold;font-size:"+font2[i]+"px;line-height: 2;text-align:center;width:100%;height:48%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+font1[i]+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measurecolor[i]+"  ;'>"+measureAlias[i]+"</span></div></div>";
        }
    }
    else if(measureCount%2==0){
        rowCount=measureCount>2?2:1;
        wDiv=measureCount/rowCount;
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=divHeight/(rowCount)-20;
        for(var i=0;i<measureCount;i++){
            var borderCss='';
            if(i<measureCount/2 && measureCount>2){
                borderCss=';border-bottom:1px dotted #d1d1d1';
    }
//             if(i<=1 && measureCount==4){
//               htmlstr +="<div style='width:"+tileWidth+"px;border-bottom:1px dotted #d1d1d1; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+datacolor[i]+";font-family: Helvetica;float:bottom'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:50%;font-family: Helvetica;color:"+measurecolor[i]+";'>"+measureAlias[i]+"</span></div>";
////             htmlstr +="<div class='maindiv'  style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div style='width:70%;;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color: #696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
//             }else{
        if (tileWidth> 80)
            {
                htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight+"px"+borderCss+";background-color:white;display:block;float:left;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
//             }
}
            else{
                 htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
    }
        }
    }
    else{
        rowCount=measureCount>2?2:1;
        wDiv=parseInt((measureCount)/rowCount);
        if(measureCount==3){
            wDiv=2;
        }
        else if(measureCount==9){
            wDiv=3;
        }
        tileWidth=$("#"+div).width()/wDiv-9;
        var x=1;
        if(measureCount==3){
            tileHeight=divHeight/(rowCount)-10;
        }
        else{
            x=0;
            tileHeight=divHeight/(rowCount+1)*0.95-10;
        }
        
        for(var i=0;i<measureCount-x;i++){
            if (tileWidth> 80){
                  htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight+"px;border-bottom:1px dotted #d1d1d1;background-color:white;display:block;float:left;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span ></div>";
        }
            else{
                  htmlstr +="<div class='gFontLight' style='cursor:pointer;width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span ></div>";
    }
        }
        if(x==1){
            if (tileWidth> 80){
                 htmlstr +="<div  class='gFontLight' style='cursor:pointer;width:"+($("#"+div).width()-15)+"px; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
        }
           else{
                 htmlstr +="<div  class='gFontLight' style='cursor:pointer;width:"+($("#"+div).width()-15)+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='background-color:white;width:100%;height:40%;float:top;margin-top: 5%;'><div class='gFontLight' style='background-color:white;font-family: LatoWebLight;display:flex;align-items:center;justify-content:center;font-style:normal;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;font-weight: normal;'>"+preSuffData[i]+"</div></div><span onclick='drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureArray[i])+"\")' style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
        }
        }
//        htmlstr +="<div  class='maindiv'  style='width:"+($("#"+div).width()-12)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'  style='background-color:; width:30%;height:100%;float:left;'></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:#696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
    }
//    $("#"+div).append(htmlstr);        
    $("#"+appendDiv).append(htmlstr);
//Added by shivam
openTableDiv1 = function(div,count){
$("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Data Table'
    });
//    $("#openTableTile").html("");
////    alert(JSON.stringify(data))
//    tableData[div] = data;
////    alert(div)
////    alert(JSON.stringify(tableData))
////    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    $("#openTableTile").dialog('open');
//}
    $("#openTableTile").html("");
    tableData[div] = data;
    var measureArr = []
//    alert(JSON.stringify(Data));
    measureArr.push(chartData[div]["meassures"][count])
//    alert(JSON.stringify(tableData))
//    alert(measureArr)
//    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,measureArr , 590, 620)
    $("#openTableTile").dialog('open');
}

    }
function buildStackedKPINew(div, data, columns, measureArray, divWidth, divHeight,KPIresult,flag,appenddiv1){

   var appendDiv = "";
if(div.indexOf("SkpiDiv_")!=-1||div.indexOf("TrDivTop4_")!=-1||div.indexOf("TrDivTop5_")!=-1||div.indexOf("TrDivTop1_")!=-1||div.indexOf("TrDivTop2_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
    if(typeof appenddiv1!="undefined" && appenddiv1!=""){
   appendDiv =   appenddiv1   
    }else{
        
appendDiv = div
    }

}
//alert(appendDiv)
if(typeof flag!='undefined' && flag!=='' && (typeof appenddiv1=="undefined" || appenddiv1=="") ){
    appendDiv=flag;
}
    var onClickFunction='openTableDiv1';
    if(typeof flag!='undefined' && flag==='trendKPI'){
        onClickFunction='updateTrend';
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true' && chartData[div]["comparisonType"]!='Custom'){
        buildStackedKPIComp(div, data, columns, measureArray, divWidth, divHeight,KPIresult)
        return;
    }
    var dataArray = [];
    var fontSize1;
    var fontSize2;
    var preSuffData =[];
 var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
     graphProp(div);
 var tableData = {};
tableData[div] = data;
//    if(divWidth<divHeight){
//        fontSize1=divWidth/25;
//    }
//    else{
//        fontSize1=divHeight/25;
//    }
//    
//    if(fontSize1<10)
//    {
//        fontSize1=10;
//            
//    }
//    if(fontSize1>30){
//        fontSize1=30;
//    }
//    fontSize2=fontSize1;
    
//   for(var j in measureArray){
//    if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[j]]!='undefined' && chartData[div]["measureNameSize"][measureArray[j]]!='undefined'){
//        fontSize1=chartData[div]["measureNameSize"][measureArray[j]];
//    }else {
          if(divWidth<divHeight){
        fontSize1=divWidth/25;
    }
    else{
        fontSize1=divHeight/25;
    }
    
    if(fontSize1<10)
    {
        fontSize1=10;
            
    }
    if(fontSize1>30){
        fontSize1=30;
    }
//    chartData[div]["measureNameSize"]=fontSize1;
//    }

//    if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' ){
//        fontSize2=chartData[div]["kpiGTFont"];
//    }else {
          if(divWidth<divHeight){
        fontSize2=divWidth/25;
    }
    else{
        fontSize2=divHeight/25;
    }

    if(fontSize2<10)
    {
        fontSize2=10;

    }
    if(fontSize2>30){
        fontSize2=30;
    }
//    chartData[div]["kpiGTFont"]=fontSize2;
//    }
    $("#chartData").val(JSON.stringify(chartData));
// fontSize2=fontSize1;
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate
    for(var m in measureArray){
        DataSum_measureArray[m]=0;
        for(var d in data){
            DataSum_measureArray[m]+= parseFloat(data[d][measureArray[m]]);
        //            alert(parseFloat(data[d][measureArray[m]]));
        }
        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
        dataArray.push( DataSum_measureArray[m])
    }
    //    endof GT claculat
    //    alert(DataSum_measureArray);
    var tileWidth,tileHeight,rowCount,wDiv;
     var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[div]["yAxisFormat"];
  }else{
   yAxisFormat = "";
  }
  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
      yAxisRounding = chartData[div]["rounding"];
  }else{
   yAxisRounding =0;
  }
 //....
  DataSum_measureArray=[];
for(var k in KPIresult){
    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[k]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[k]];
    }
    else{
        yAxisFormat="Auto";
    }
    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[k]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[k]];
    }
    else{
        yAxisRounding="1";
    }
                  if(yAxisFormat==""){

                    DataSum_measureArray1 =      addCurrencyType(div, getMeasureId(measureArray[k])) + addCommas(numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div));
                    }
            else{
             DataSum_measureArray1 =        addCurrencyType(div, getMeasureId(measureArray[k])) + numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div);
                }

    DataSum_measureArray.push(DataSum_measureArray1)

}
//    DataSum_measureArray= KPIresult;
    var color1,color2;
    if(typeof chartData[div]["stackLight"]!=='undefined' && chartData[div]["stackLight"]!==''){
        color1=chartData[div]["stackLight"]
    }
    else{
        color1=tileLightColor[0];
    }
    if(typeof chartData[div]["stackDark"]!=='undefined' && chartData[div]["stackDark"]!==''){
        color2=chartData[div]["stackDark"]
    }
    else{
        color2=tileDarkColor[0];
    }
for(var q in chartData[div]["meassures"]){
    var prefix = "";
    var suffix = "";
    var prefixFontSize = "";
    var suffixFontSize = "";
    if(typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" && typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" ){
    prefixFontSize = chartData[div]["PrefixfontsizeList"][q];
}else{
    prefixFontSize = fontSize2;
}
if(typeof chartData[div]["SuffixfontsizeList"]!="undefined" && chartData[div]["SuffixfontsizeList"]!="" &&  typeof chartData[div]["SuffixfontsizeList"][q]!="undefined" && chartData[div]["SuffixfontsizeList"][q]!="" ){
    Suffixfontsize = chartData[div]["SuffixfontsizeList"][q];
}else{
    Suffixfontsize = fontSize2;
}
if(typeof chartData[div]["PrefixList"]!="undefined" && chartData[div]["PrefixList"]!="" && typeof chartData[div]["PrefixList"][q]!="undefined" && chartData[div]["PrefixList"][q]!="" ){
    prefix = chartData[div]["PrefixList"][q];
}else{
    prefix = "";
}
if(typeof chartData[div]["SuffixList"]!="undefined" && chartData[div]["SuffixList"]!="" && typeof chartData[div]["SuffixList"][q]!="undefined" && chartData[div]["SuffixList"][q]!="" ){
    suffix = chartData[div]["SuffixList"][q];
}else{
    suffix = "";
}
if(typeof DataSum_measureArray[q]!="undefined" && DataSum_measureArray[q]!=""){

}else{
DataSum_measureArray[q] =0;
}
if(typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] =="Y"){
if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");   
   }
}else{
  if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'></span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'></span>");   
   }
}
   }
       
   var font1=[],font2=[];
   for(var i1 in measureArray){
       if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[i1]]!='undefined' && chartData[div]["measureNameSize"][measureArray[i1]]!='undefined'){
           font1[i1]=chartData[div]["measureNameSize"][measureArray[i1]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font1[i1]=14;
           }
           else if(chartData[div]["chartType"]==="Score-vs-Targets"){
                  font1[i1]=13; 
               }else{
           font1[i1]=18;
               }
            }
   }
   for(var i2 in measureArray){
       if(typeof chartData[div]["measureValueSize"]!='undefined' && typeof chartData[div]["measureValueSize"][measureArray[i2]]!='undefined' && chartData[div]["measureValueSize"][measureArray[i2]]!='undefined'){
           font2[i2]=chartData[div]["measureValueSize"][measureArray[i2]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font2[i2]=20;
           }
           else if(chartData[div]["chartType"]==="Score-vs-Targets"){
           font2[i2]=17;
       }else{
          font2[i2]=25; 
       }
   }
   }
   var measureAlias=[];
   for(var a in measureArray){
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[a]]!='undefined' && chartData[div]["measureAlias"][measureArray[a]]!== measureArray[a]){
            measureAlias[a]=chartData[div]["measureAlias"][measureArray[a]];
}else{
            measureAlias[a]=measureArray[a];
        }
    }
   var dataColor=[];
   for(var b in measureArray){
       if(typeof chartData[div]["dataColor"]!='undefined' && typeof chartData[div]["dataColor"][measureArray[b]]!='undefined' && chartData[div]["dataColor"][measureArray[b]]!='undefined'){
           dataColor[b]=chartData[div]["dataColor"][measureArray[b]];
}else{
            dataColor[b]="#585858";
        }
    }
   var measureColor=[];
   for(var c in measureArray){
   if(typeof chartData[div]["measureColor"]!='undefined' && typeof chartData[div]["measureColor"][measureArray[c]]!='undefined' && chartData[div]["measureColor"][measureArray[c]]!='undefined'){          
            measureColor[c]=chartData[div]["measureColor"][measureArray[c]];
}else{
      measureColor[c]="#A1A1A1";
        }
    }
     
    
    var htmlstr = "";
    var measureCount=measureArray.length;
    if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid
    }

    if(measureCount==1){
        tileWidth=$("#"+div).width()-9;
        tileHeight=divHeight-20;
        for(var i=0;i<measureCount;i++){
            if ( tileWidth> 80)
            {
                htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50%;background-color:white;display:block;float:left;'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+(font2[i]+8)+"px;line-height: -moz-block-height;text-align:left;margin-left:18%;width:100%;height:100%;color:rgb(161, 161, 161);float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;margin-left:20%;margin-top:3%;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
            }
            else {
                htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50;background-color:white;display:block;'><div style='background-color:white;width:100%;height:100%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+(font2[i]+8)+"px;line-height: -moz-block-height;text-align:left;margin-left:18%;width:100%;height:100%;color:rgb(161, 161, 161);float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;margin-left:20%;margin-top:3%;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
             }
           
//          
//            htmlstr +="<div class='maindiv'  style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div class='editdiv'  style='top:10px;float:left;'><a title='change image' href='#'onclick='addImageTag1(chart1)'><img style='position:absolute;display:none' src='images/icons/edit.png' height='15px' width='15px'/></a></div><img src='images/icons/measure2.png'/></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:"+datacolor[i]+";'><div style='background-color:#fff;font-style:bold;font-size:"+font2[i]+"px;line-height: 2;text-align:center;width:100%;height:48%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+font1[i]+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measurecolor[i]+"  ;'>"+measureAlias[i]+"</span></div></div>";
        }
    }
    else if(measureCount%2==0){
        rowCount=measureCount>2?2:1;
        wDiv=measureCount/rowCount;
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=divHeight/(rowCount)-20;
        for(var i=0;i<measureCount;i++){
//            var borderCss='';
            if(i<measureCount/2 && measureCount>2){
//                borderCss=';border-bottom:1px dotted #d1d1d1';
    }
//             if(i<=1 && measureCount==4){
//               htmlstr +="<div style='width:"+tileWidth+"px;border-bottom:1px dotted #d1d1d1; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+datacolor[i]+";font-family: Helvetica;float:bottom'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:50%;font-family: Helvetica;color:"+measurecolor[i]+";'>"+measureAlias[i]+"</span></div>";
////             htmlstr +="<div class='maindiv'  style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div style='width:70%;;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color: #696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
//             }else{
        if (tileWidth> 80)
            {
                htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:47%; height:50%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;font-family:helvatica;margin-left:6%;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:6%;word-wrap: break-word;width:100%;height:50%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
//             }
}
            else{
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:47%; height:50%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;font-family:helvatica;margin-left:6%;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:6%;word-wrap: break-word;width:100%;height:50%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
    }
        }
    }
    else{
        rowCount=measureCount>2?2:1;
        wDiv=parseInt((measureCount)/rowCount);
        if(measureCount==3){
            wDiv=2;
        }
        else if(measureCount==9){
            wDiv=3;
        }
        tileWidth=$("#"+div).width()/wDiv-9;
        var x=1;
        if(measureCount==3){
            tileHeight=divHeight/(rowCount)-10;
        }
        else{
            x=0;
            tileHeight=divHeight/(rowCount+1)*0.95-10;
        }
        
        for(var i=0;i<measureCount-x;i++){
            if (tileWidth> 80){
                  htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span ></div>";
        }
            else{
                  htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span ></div>";
    }
        }
        if(x==1){
            if (tileWidth> 80){
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
        }
           else{
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:50%; height:50%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
        }
        }
//        htmlstr +="<div  class='maindiv'  style='width:"+($("#"+div).width()-12)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'  style='background-color:; width:30%;height:100%;float:left;'></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:#696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
    }
//    $("#"+div).append(htmlstr);        
    $("#"+appendDiv).append(htmlstr);
//Added by shivam
//openTableDiv1 = function(div,count){
//$("#openTableTile").dialog({
//         autoOpen: false,
//         height: 440,
//         width: 620,
//         position: 'justify',
//         modal: true,
//         resizable:true,
//        title:'Data Table'
//    });
////    $("#openTableTile").html("");
//////    alert(JSON.stringify(data))
////    tableData[div] = data;
//////    alert(div)
//////    alert(JSON.stringify(tableData))
//////    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
////    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
////    $("#openTableTile").dialog('open');
////}
//    $("#openTableTile").html("");
//    tableData[div] = data;
//    var measureArr = []
////    alert(JSON.stringify(Data));
//    measureArr.push(chartData[div]["meassures"][count])
////    alert(JSON.stringify(tableData))
////    alert(measureArr)
////    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,measureArr , 590, 620)
//    $("#openTableTile").dialog('open');
//}

    }
    function buildStackedKPINewDash(div, data, columns, measureArray, divWidth, divHeight,KPIresult,flag,appenddiv1){
   var appendDiv = "";
if(div.indexOf("SkpiDiv_")!=-1||div.indexOf("SkpiDiv1_")!=-1||div.indexOf("TrDivTop4_")!=-1||div.indexOf("TrDivTop5_")!=-1||div.indexOf("TrDivTop1_")!=-1||div.indexOf("TrDivTop2_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
    if(typeof appenddiv1!="undefined" && appenddiv1!=""){
   appendDiv =   appenddiv1   
    }else{
        
appendDiv = div
    }

}
//alert(appendDiv)
if(typeof flag!='undefined' && flag!=='' && (typeof appenddiv1=="undefined" || appenddiv1=="") ){
    appendDiv=flag;
}
    var onClickFunction='openTableDiv1';
    if(typeof flag!='undefined' && flag==='trendKPI'){
        onClickFunction='updateTrend';
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true' && chartData[div]["comparisonType"]!='Custom'){
        buildStackedKPIComp(div, data, columns, measureArray, divWidth, divHeight,KPIresult)
        return;
    }
    var dataArray = [];
    var fontSize1;
    var fontSize2;
    var preSuffData =[];
 var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
     graphProp(div);
 var tableData = {};
tableData[div] = data;
//    if(divWidth<divHeight){
//        fontSize1=divWidth/25;
//    }
//    else{
//        fontSize1=divHeight/25;
//    }
//    
//    if(fontSize1<10)
//    {
//        fontSize1=10;
//            
//    }
//    if(fontSize1>30){
//        fontSize1=30;
//    }
//    fontSize2=fontSize1;
    
//   for(var j in measureArray){
//    if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[j]]!='undefined' && chartData[div]["measureNameSize"][measureArray[j]]!='undefined'){
//        fontSize1=chartData[div]["measureNameSize"][measureArray[j]];
//    }else {
          if(divWidth<divHeight){
        fontSize1=divWidth/25;
    }
    else{
        fontSize1=divHeight/25;
    }
    
    if(fontSize1<10)
    {
        fontSize1=10;
            
    }
    if(fontSize1>30){
        fontSize1=30;
    }
//    chartData[div]["measureNameSize"]=fontSize1;
//    }

//    if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' ){
//        fontSize2=chartData[div]["kpiGTFont"];
//    }else {
          if(divWidth<divHeight){
        fontSize2=divWidth/25;
    }
    else{
        fontSize2=divHeight/25;
    }

    if(fontSize2<10)
    {
        fontSize2=10;

    }
    if(fontSize2>30){
        fontSize2=30;
    }
//    chartData[div]["kpiGTFont"]=fontSize2;
//    }
    $("#chartData").val(JSON.stringify(chartData));
// fontSize2=fontSize1;
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate
    for(var m in measureArray){
        DataSum_measureArray[m]=0;
        for(var d in data){
            DataSum_measureArray[m]+= parseFloat(data[d][measureArray[m]]);
        //            alert(parseFloat(data[d][measureArray[m]]));
        }
        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
        dataArray.push( DataSum_measureArray[m])
    }
    //    endof GT claculat
    //    alert(DataSum_measureArray);
    var tileWidth,tileHeight,rowCount,wDiv;
     var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[div]["yAxisFormat"];
  }else{
   yAxisFormat = "";
  }
  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
      yAxisRounding = chartData[div]["rounding"];
  }else{
   yAxisRounding =0;
  }
 //....
  DataSum_measureArray=[];
for(var k in KPIresult){
    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[k]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[k]];
    }
    else{
        yAxisFormat="Auto";
    }
    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[k]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[k]];
    }
    else{
        yAxisRounding="1";
    }
                  if(yAxisFormat==""){

                    DataSum_measureArray1 =      addCurrencyType(div, getMeasureId(measureArray[k])) + addCommas(numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div));
                    }
            else{
             DataSum_measureArray1 =        addCurrencyType(div, getMeasureId(measureArray[k])) + numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div);
                }

    DataSum_measureArray.push(DataSum_measureArray1)

}
//    DataSum_measureArray= KPIresult;
    var color1,color2;
    if(typeof chartData[div]["stackLight"]!=='undefined' && chartData[div]["stackLight"]!==''){
        color1=chartData[div]["stackLight"]
    }
    else{
        color1=tileLightColor[0];
    }
    if(typeof chartData[div]["stackDark"]!=='undefined' && chartData[div]["stackDark"]!==''){
        color2=chartData[div]["stackDark"]
    }
    else{
        color2=tileDarkColor[0];
    }
for(var q in chartData[div]["meassures"]){
    var prefix = "";
    var suffix = "";
    var prefixFontSize = "";
    var suffixFontSize = "";
    if(typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" && typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" ){
    prefixFontSize = chartData[div]["PrefixfontsizeList"][q];
}else{
    prefixFontSize = fontSize2;
}
if(typeof chartData[div]["SuffixfontsizeList"]!="undefined" && chartData[div]["SuffixfontsizeList"]!="" &&  typeof chartData[div]["SuffixfontsizeList"][q]!="undefined" && chartData[div]["SuffixfontsizeList"][q]!="" ){
    Suffixfontsize = chartData[div]["SuffixfontsizeList"][q];
}else{
    Suffixfontsize = fontSize2;
}
if(typeof chartData[div]["PrefixList"]!="undefined" && chartData[div]["PrefixList"]!="" && typeof chartData[div]["PrefixList"][q]!="undefined" && chartData[div]["PrefixList"][q]!="" ){
    prefix = chartData[div]["PrefixList"][q];
}else{
    prefix = "";
}
if(typeof chartData[div]["SuffixList"]!="undefined" && chartData[div]["SuffixList"]!="" && typeof chartData[div]["SuffixList"][q]!="undefined" && chartData[div]["SuffixList"][q]!="" ){
    suffix = chartData[div]["SuffixList"][q];
}else{
    suffix = "";
}
if(typeof DataSum_measureArray[q]!="undefined" && DataSum_measureArray[q]!=""){

}else{
DataSum_measureArray[q] =0;
}
if(typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] =="Y"){
if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");   
   }
}else{
  if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'></span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].toString().replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'></span>");   
   }
}
   }
       
   var font1=[],font2=[];
   for(var i1 in measureArray){
       if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[i1]]!='undefined' && chartData[div]["measureNameSize"][measureArray[i1]]!='undefined'){
           font1[i1]=chartData[div]["measureNameSize"][measureArray[i1]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font1[i1]=14;
           }
           else if(chartData[div]["chartType"]==="Score-vs-Targets"){
                  font1[i1]=15; 
               }else{
           font1[i1]=14;
               }
            }
   }
   for(var i2 in measureArray){
       if(typeof chartData[div]["measureValueSize"]!='undefined' && typeof chartData[div]["measureValueSize"][measureArray[i2]]!='undefined' && chartData[div]["measureValueSize"][measureArray[i2]]!='undefined'){
           font2[i2]=chartData[div]["measureValueSize"][measureArray[i2]];
       }
       else{
           if(chartData[div]["chartType"]==='Trend-KPI'){
                font2[i2]=20;
           }
           else if(chartData[div]["chartType"]==="Score-vs-Targets"){
           font2[i2]=18;
       }else{
          font2[i2]=18; 
       }
   }
   }
   var measureAlias=[];
   for(var a in measureArray){
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[a]]!='undefined' && chartData[div]["measureAlias"][measureArray[a]]!== measureArray[a]){
            measureAlias[a]=chartData[div]["measureAlias"][measureArray[a]];
}else{
            measureAlias[a]=measureArray[a];
        }
    }
   var dataColor=[];
   for(var b in measureArray){
       if(typeof chartData[div]["dataColor"]!='undefined' && typeof chartData[div]["dataColor"][measureArray[b]]!='undefined' && chartData[div]["dataColor"][measureArray[b]]!='undefined'){
           dataColor[b]=chartData[div]["dataColor"][measureArray[b]];
}else{
            dataColor[b]="#696969";
        }
    }
   var measureColor=[];
   for(var c in measureArray){
   if(typeof chartData[div]["measureColor"]!='undefined' && typeof chartData[div]["measureColor"][measureArray[c]]!='undefined' && chartData[div]["measureColor"][measureArray[c]]!='undefined'){          
            measureColor[c]=chartData[div]["measureColor"][measureArray[c]];
}else{
      measureColor[c]="#A1A1A1";
        }
    }
     
    
    var htmlstr = "";
    var measureCount=measureArray.length;
    if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid
    }

    if(measureCount==1){
        tileWidth=$("#"+div).width()-9;
        tileHeight=divHeight-20;
        for(var i=0;i<measureCount;i++){
            if ( tileWidth> 80)
            {
                htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:100%; height:100%;background-color:white;display:block;float:left;'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;word-wrap: break-word;width:100%;height:50%;font-family: Helvatica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
            }
            else {
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:100%; height:100%;background-color:white;display:block;'><div style='background-color:white;width:100%;height:100%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;word-wrap: break-word;width:100%;height:50%;font-family: Helvatica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
             }
           
//          
//            htmlstr +="<div class='maindiv'  style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div class='editdiv'  style='top:10px;float:left;'><a title='change image' href='#'onclick='addImageTag1(chart1)'><img style='position:absolute;display:none' src='images/icons/edit.png' height='15px' width='15px'/></a></div><img src='images/icons/measure2.png'/></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:"+datacolor[i]+";'><div style='background-color:#fff;font-style:bold;font-size:"+font2[i]+"px;line-height: 2;text-align:center;width:100%;height:48%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+font1[i]+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measurecolor[i]+"  ;'>"+measureAlias[i]+"</span></div></div>";
        }
    }
    else if(measureCount%2==0){
        rowCount=measureCount>2?2:1;
        wDiv=measureCount/rowCount;
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=divHeight/(rowCount)-20;
        for(var i=0;i<measureCount;i++){
//            var borderCss='';
            if(i<measureCount/2 && measureCount>2){
//                borderCss=';border-bottom:1px dotted #d1d1d1';
    }
//             if(i<=1 && measureCount==4){
//               htmlstr +="<div style='width:"+tileWidth+"px;border-bottom:1px dotted #d1d1d1; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+datacolor[i]+";font-family: Helvetica;float:bottom'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:50%;font-family: Helvetica;color:"+measurecolor[i]+";'>"+measureAlias[i]+"</span></div>";
////             htmlstr +="<div class='maindiv'  style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div style='width:70%;;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color: #696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
//             }else{
        if (tileWidth> 80)
            {
                htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:lfet;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";

}
            else{
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
    }
        }
    }
    else{
        rowCount=measureCount>2?2:1;
        wDiv=parseInt((measureCount)/rowCount);
        if(measureCount==3){
            wDiv=2;
        }
        else if(measureCount==9){
            wDiv=3;
        }
        tileWidth=$("#"+div).width()/wDiv-9;
        var x=1;
        if(measureCount==3){
            tileHeight=divHeight/(rowCount)-10;
        }
        else{
            x=0;
            tileHeight=divHeight/(rowCount+1)*0.95-10;
        }
        
        for(var i=0;i<measureCount-x;i++){
            if (tileWidth> 80){
                  htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;margin-left:12%;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:12%;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
        }
            else{
                  htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;margin-left:12%;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:12%;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
    }
        }
        if(x==1){
            if (tileWidth> 80){
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;float:left;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;margin-left:12%;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:12%;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
        }
           else{
                 htmlstr +="<div onclick='"+onClickFunction+"(\""+div+"\",\""+i+"\")' class='gFontLight' style='cursor:pointer;width:55%; height:28%;background-color:white;display:block;'><div class='gFontLight' style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div class='gFontLight' style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:left;margin-left:12%;font-family:helvatica;width:100%;height:100%;color:"+dataColor[i]+";float:bottom;'>"+preSuffData[i]+"</div></div><div style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:left;font-family:helvatica;margin-left:12%;word-wrap: break-word;width:100%;height:100%;color:"+measureColor[i]+";'>"+measureAlias[i]+"</div></div>";
        }
        }
//        htmlstr +="<div  class='maindiv'  style='width:"+($("#"+div).width()-12)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'  style='background-color:; width:30%;height:100%;float:left;'></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:#696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
    }
//    $("#"+div).append(htmlstr);        
    $("#"+appendDiv).append(htmlstr);
    }


// add for combo chart by mynk sh.
function buildAdvanceLine(div,div11, data, columns, measureArray, divWid, divHgt) {
var offset=0;
if($("#chartType").val()==='Trend-Dashboard'){
    offset=10;
    divHgt-=offset;
    $("#chartAd1").append(kpiTable(measureArray,div));
}    
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

 var measArr = [];
    var measure1 = measureArray[0];
    var fromoneview=parent.$("#fromoneview").val();
    var widthdr=divWid
    var divHgtdr=divHgt
    var dashletid;
      if(fromoneview!='null'&& fromoneview=='true'){
//divWid=divWid-90
dashletid=div;
      }
//      else{
//    divWid=parseInt($(window).width())*(.35);
//      }
    var chartMap = {};
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
        var chartData = JSON.parse(parent.$("#chartData").val());
    var customTicks = 5;

//var colIds = chartData[div]["viewIds"];
    var autoRounding1;
    if(fromoneview!='null'&& fromoneview=='true'){

      }else{
           if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
      }
     
    var isDashboard = parent.$("#isDashboard").val();
//   var fun = "drillWithinchart(this.id,\""+div+"\")";
//Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
	     fun = "drillWithinchart(this.id,\""+div+"\")";
	}
        function drillFunct(id1){
           drillWithinchart(id1,div); 
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
    var botom = 70;
    var j = 0;
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }
var colIds= [];

     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
div=div1;
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}
    var margin = {};

    var width = 0;
    var height = 0;
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
      margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 00
    };
    width = parseFloat(divWid)+200, //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .78;
}else{
    if($("#chartType").val()=='Bar-Dashboard'){
     margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 70
    };
    }else{
         margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 80
    };
    }
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .75;
    }
    
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);



    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
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
                   if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{

                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });

    }
    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
                     if(typeof displayY !=="undefined" && displayY =="Yes"){
                    if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                     }else {
                         return "";
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

    var valuelineH = d3.svg.line()
            .x(function(d) {
               
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y(550);
            });
            
    var valueline = d3.svg.line()
            .x(function(d) {
               
                return x(d[columns[0]]);
            })
            .y(function(d) {
               
                return y(d[measure1]);
            });
            
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
//alert(JSON.stringify(div))
//alert(JSON.stringify(div1))
if($("#chartType").val()=='Bar-Dashboard'){
    width=width+14
    svg = d3.select("#" + div11)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("id", "svg_" + div11)
//            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgt)+" ")
//            .classed("svg-content-responsive", true)
            .attr("width", width + margin.left + margin.right)

            .attr("height", height + margin.top +17.5)

            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
}
else{
    svg = d3.select("#" + div11)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("id", "svg_" + div11)
//            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgt)+" ")
//            .classed("svg-content-responsive", true)
            .attr("width", width + margin.left + margin.right)

            .attr("height", height + margin.top +margin.bottom +17.5)

            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
}
    svg.append("g")
    .append("svg:svg")
//    .style("margin-left", "3em")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right);

  if($("#chartType").val()!='Bar-Dashboard'){
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
//            .style("stroke-dasharray", ("3,3"))
//            .call(xAxis);
  }
    svg.append("g")
            .attr("class", "y axis")
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", -75)
            .attr("fill","steelblue")
//            .style("stroke-dasharray", ("3, 3"))
//            .call(yAxis)
            .attr("dy", ".41em")
            .style("text-anchor", "end")
            .text("" + measure1 + "");

    var min1 = [];
    var flag = "";
    
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
 
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
    for (var key in data) {
        min1.push(data[key][measure1]);

    }
}}else{
    for (var key in data) {
        min1.push(data[key][measure1]);

    }}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
  if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default"){
   minVal = 0;  
 }
 else{
    minVal = minimumValue(data, measure1);
}
}else{
 minVal = 0;
// minVal = minimumValue(data, measure1);
}
//    }
  if(min1.length>1){
     y.domain([minVal, Math.max.apply(Math, min1)]);
}else{
     y.domain([parseFloat(minVal), parseFloat(max)]);
}
      svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
 if(measArr[0]<0){
    minVal = measArr[0] + diffFactor;
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }
    y.domain([minVal, max]);
    if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
    svg = d3.select("#" + div11).select("g");
     if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
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
//           flag =  measure1;
           }
          return flag
             });}
             
if(typeof displayX !=="undefined" && displayX == "Yes"){
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
                if(typeof displayX !=="undefined" && displayX == "Yes"){
                if (d.length < 18)
                    return d;
                else
                    return d.substring(0, 18) + "..";
            }else {
                return "";
            }
            })
            .attr('transform', 'rotate(-25)')
            .append("svg:title").text(function(d) {
        return d;
    });
}
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
     svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
}
//target Line
//      svg.append("path")
//            .data(data)
//            .attr("d", valuelineH(data))
//            .attr("fill", "transparent")
//            .style("z-index", "0")
//            .style("stroke-width", "1.5px")
//            .style("stroke", "black");
    
    
   var path = svg.append("path")
            .data(data)
            .attr("d", valueline(data))
            .attr("fill", "transparent")
//            .style("stroke-dasharray", ("3, 3"))  // <== This line here!!
//           .attr("d", valueline(data))
            .style("z-index", "0")
              .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
        if(typeof chartData[div]["trlineType"]!=="undefined" && chartData[div]["trlineType"]==="dashed-Line"){
            return "6,3";
        }
        else{
            return "1,0";
        }}
            )
            .attr("stroke", function(d, i) {
//              for(var j=0;j<data.length;j++){
//              if(data[j][measure1] < 550){
//                   return color(1);
//               }else {
                  return color(0);
//               }
//                }
               
            })
            .style("stroke-width", "3px");
            var totalLength = path.node().getTotalLength();
         
  path
      .attr("stroke-dasharray", totalLength + " " + totalLength)
      .attr("stroke-dashoffset", totalLength)
      .transition()
        .duration(2000)
        .ease("linear")
        .attr("stroke-dashoffset", 0);
//
//           // Label Text
           var c=0;
           svg.selectAll("labelText")
             .data(data).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("x", function(d){
                        var len=Object.keys(data).length;
                       c++;
                       if(c==len)
                       {
                            return x(d[columns[0]]) - 40;
                       }
                       else
                       {
                            return x(d[columns[0]]) - 25;
                       }
                   } ).attr("y", function(d){
                       return y(d[measure1]) + 10;
                   })

      .attr("dy", ".35em")
      .attr("fill", function(d, i){
            var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {
                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                               }
                               return lableColors;
             })
            .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                    }else{
                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }else {return "";}
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
                        radius = "5";
                    }else{
                        return radius = "3.5"
                    }
                    return radius;
            })
            .attr("cx", function(d) {
                return x(d[columns[0]]);
            })
            .attr("cy", function(d) {
                return y(d[measure1]);
            })
             .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .style("fill", function(d,i){
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
//                        if(d[measure1] < 550){
//                   colorShad = color(1);
//               }else {
                   colorShad = color(0);
               }
                
                return colorShad;
                  
            })
            .style("stroke", function(d, i) {
               var colorShad;
//               if(d[measure1] < 550){
//                   colorShad = color(1);
//               }else {
                   colorShad = color(0);
//               }
                
                return colorShad;
               
            })
            .style("stroke-width", "2px")

            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .on("mouseover", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                show_details(d, columns, measureArray, this,div);
                }
//                show_details(d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

var rectX=width*.8+ margin.left + margin.right; 
//svg.append("g")
//            .attr("id", "viewBylbl")
//            .append("text")
//            .attr("x",rectX+12)
//            .attr("style","font-size:12px")
//            .attr("y","15")
//            .attr("fill", function(d){
//            var lableColors;
//                   if (typeof chartData[div]["labelColors"]!=="undefined") {
//                              lableColors = chartData[div]["labelColors"];
//                          }else {
//                               lableColors = "#000000";
//                               }
//                               return lableColors;
//             })
//            .text(columns[0]);
            
 if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
    }
}   // end for combo chart by mynk sh.


function buildPieTableDashboard(div,data){
 
var chartData = JSON.parse(parent.$("#chartData").val());
        $("#viewMeasureBlock").html('');
//        var charts = Object.keys(chartData);
        var charts = ["chart1","chart2"];
        var width = $(window).width();
        var divHeight = $(window).height();
        var divContent = "";
        var measureList = chartData["chart1"]["meassures"];
        var viewBys  = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
//        var columnList = chartData["ch;
        var widths = [];
        var columns = chartData["chart1"]["viewBys"];
    var columnId = chartData["chart1"]["dimensions"];
    var columnName = [];
        var html = "";
      
       html += '<ul id="quickTabs">';
//       if(graphViewFlag){
      var  chartId = "chart1";
       for(var i in columnId){
    columnName.push(viewBys[viewIds.indexOf(columnId[i])]);
//      Added by Shivam
                if(i===selectedViewbyIndex){
            html += "<li id='menu"+i+"' style='border-bottom:2px solid #DC143C'><a  id='"+columnName[i]+":"+columnId[i]+"' name='tab"+parseInt(i+1)+"' style=\"cursor: pointer\" onclick='changeViewBys(this.id,\""+chartId+"\",\""+i+"\")'>" + columnName[i] + "</a></li>";
                }
                else{
      
            html += "<li id='menu"+i+"'><a  id='"+columnName[i]+":"+columnId[i]+"' name='tab"+parseInt(i+1)+"' style=\"cursor: pointer\" onclick='changeViewBys(this.id,\""+chartId+"\",\""+i+"\")'>" + columnName[i] + "</a></li>";
                }
       
    }
//    }
//       else {
//       for(var i=0; i<measureList.length; i++){
//    html += '<li><a href="#" id="'+parseInt(i)+'" name="tab'+parseInt(i+1)+'" onclick="changeMeasureArray(this.id)">'+measureList[i]+'</a></li>';
//       }
//           
//       }
       var chartId = "chart1";
       html += "<li style='float:right'>";
//       html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img>"+
//                "</div>";
       html += "</li>";
       html += '</ul>';
$("#viewMeasureBlock").html(html);
        
           $("#content").find("[id^='tab']").hide(); // Hide all content
    $("#quickTabs li:first").attr("id","current"); // Activate the first tab
    $("#content #tab1").fadeIn(); // Show first tab's content
    
    $('#quickTabs a').click(function(e) {
        e.preventDefault();
        if ($(this).closest("li").attr("id") == "current"){ //detection for current tab
         return;       
        }
        else{             
          $("#content").find("[id^='tab']").hide(); // Hide all content
          $("#quickTabs li").attr("id",""); //Reset id's
          $(this).parent().attr("id","current"); // Activate this
          $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
        }
    });
        divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
//        divContent += "<div id='Graphdiv1' style='display:block;float:left;width:100%;'>";
//            divContent+="<table style='width:100%'><tr style='width:100%'><td style='width:1%;float:right;  margin-right:1%'><div class='dropdown' style='cursor:pointer'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16'   src='images/chart1.png' alt='Options'   ></img></span>"+
//        "<ul id='chartTypesZ"+chartId+"' class='dropdown-menu'>";
//        var graphTypes = Object.keys(parent.graphsName);
//        for(var i=0;i<parent.advanceChartGroups.length;i++)
//        {
//            divContent+="<li id='chartGroupsZ"+chartId+i+"' class='dropdown-submenu pull-left'><a onmouseover='openChartGroup1("+i+",\""+chartId+"\",\"Z\")' >"+parent.advanceChartGroups[i]+"</a>";
//        }
////        chartId = "chart2"
//        divContent+="</ul></td></tr></table></div>";
        switch (charts.length) {
            case 1:
                {
                    widths.push(width / 1.2);
                    divHeight = $(window).height();
                    divContent += "<div id='chartAd1' style='float:left;width:45%'></div>";
                     divContent += "<div id='breadcrumpPieDashboard' style='float:left;width:100%'></div>";

                    break;
                }
            case 2:
                {
                    widths.push(width / 1.2);
//                    widths.push(width / 1.2);
                         divHeight = $(window).height()/2.5;
                         if(chartData["chart1"]["chartType"]=='Vertical-Bar' || chartData["chart1"]["chartType"]=='Line'){
                            divContent += "<div id='chartAd1' style='float:left;width:100%'></div></td></tr><tr><td><div id='chartAd2' style='margin-left:10%;float:left;width:100%;'></div>";
                         }else{
                        divContent += "<div id='chartAd1' style='float:left;width:45%'></div></td></tr><tr><td><div id='chartAd2' style='float:left;width:50%;margin-left:5%'></div>";
                         }
                        divContent += "<div id='breadcrumpPieDashboard' style='float:left;width:100%'></div>";
                    break;
                }
            default :
                {
                    widths.push(width / 1.2);
                    widths.push(width / 1.2);
                    divHeight = $(window).height();
                    divContent += "<div id='chartAd1' style='float:left;width:45%'></div></td></tr><tr><td><div id='chartAd2' style='float:left;width:50%;margin-left:5%'></div>";
                     divContent += "<div id='breadcrumpPieDashboard' style='float:left;width:100%'></div>";
                    break;
                }
        }
        $("#"+div).html(divContent);
      
         var htmlvar ="";
        htmlvar +="<table style='float: left;margin-left: 20px;'>";
	htmlvar +="<tr>";
        htmlvar +="<td><img src= 'images/drillupgrey.png'onclick='drillUp(\"" + chartId + "\")' style='width:15px;height:15px'></td>";
      
       for(var i in parent.breadcrumpid){
          htmlvar +="<td><span id ='"+parent.breadcrumpid[i]+"'  style='font-family: Times New Roman,Times,serif;font-size: small;color: gray;'>"+parent.breadcrumpid[i].split(":")[0]+"</span></td>";
          htmlvar +="<td><img src= 'images/arrow-right.png' style='width:10px;height:10px'></td>";
        }
	htmlvar +="</tr>";
	htmlvar +="</table>";
$("#breadcrumpPieDashboard").append(htmlvar);
        for (var ch=0;ch<2; ch ++) {
            if(ch<2){
            var chartDetails = chartData[charts[0]];
var chartType;
var chartList=[];
             chartList.push("Pie-Table");
             chartType = "Pie-Table";
//            var chartId = "chart" + (parseFloat(ch) + 1);
            var chartId = "chart1";
            var divId = "chartAd" + (parseFloat(ch) + 1);
            try{
          
            if(ch == 0){
            buildDashletPie(chartDetails, ch, widths[ch], divHeight, chartType, chartId,divId, chartList);
            }else {
                buildDashletTable(chartDetails, ch, widths[ch], divHeight, chartType, chartId,divId, chartList);
            }    
        }
            catch(e){}
        }
        }

    function buildDashletPie(chartDetails, chartIndex,  divWidth, divHeight, chart, chartId,divId, chartList) {
        var viewbys = [];
        var viewbyVals = chartDetails.viewBys;
        viewbys.push(viewbyVals[0]);
        var measures = chartDetails.meassures;
                var data3 = data[chartId];
               
              parent.advanceData = data;
             var chartData = JSON.parse(parent.$("#chartData").val());
             var chartType = chartData[chartId]["chartType"];
          
    advanceTypeFunction(chartId,chartType,"pie");
//               buildComboPie(chartId,divId, data3, viewbys, measures, divWidth * .5, divHeight * .5);
    }   
    function buildDashletTable(chartDetails, chartIndex,  divWidth, divHeight, chart, chartId,divId, chartList) {

    var viewbys = [];
        var viewbyVals = chartDetails.viewBys;
        viewbys.push(viewbyVals[0]);
        var measures = chartDetails.meassures;
       
                var data3 = data["chart1"];
                var htmlDiv = "<div id='pieWithMap' ></div>";
$("#"+divId).append(htmlDiv);
               buildDashboardTable(chartId,divId, data3, viewbys, measures, divWidth * .5, divHeight * .5);
    }   
}

function  buildDashboardTable(tabDivVal,divId, data, columns, measureArray, divWidth, divHeight){
  var chartData = JSON.parse(parent.$("#chartData").val());
  var colIds = chartData[tabDivVal]["viewIds"];
//  alert(tabDivVal)
       var prop = graphProp(tabDivVal);
        var trWidth = divWidth / 8;
   $("#pieWithMap").html("");
//if(data[tabDivVal]!== "undefined"){
//    chart12 = data[tabDivVal];
    columns = chartData[tabDivVal]["viewBys"];
measureArray = chartData[tabDivVal]["meassures"];
//}

var allColumns = [];
for(var ch in chartData){
   allColumns.push(chartData[ch]["viewBys"][0]);
}
var htmlstr="";
htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;margin-top:20px;float:right;width:102%;'>";
               
htmlstr +="<table style='width:100%'>"
htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='height:350px;width:85%'>"
htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
htmlstr += "<th ></th>";
if(columns[0].length > 20){
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[0].substring(0, 20)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[0]+"</th>";
}
var colorCSS;
for(var i in measureArray){
if(i==0){
    colorCSS=';background-color:#B64747';
}    
else{
    colorCSS='';
}
if(measureArray[i].length > 15){
htmlstr += "<th id='"+i+"' onclick='changeMeasureArray(this.id)' title='"+measureArray[i]+"' style='cursor:pointer;white-space:nowrap;font-weight:bold;cursor:pointer;text-align:left"+colorCSS+"'>"+measureArray[i].substring(0,15)+"..</th>";
}else {
htmlstr += "<th id='"+i+"' onclick='changeMeasureArray(this.id)' style='white-space:nowrap;font-weight:bold;cursor:pointer;text-align:left"+colorCSS+"'>"+measureArray[i]+"</th>";
}

}
htmlstr += "</tr>";
  var colorValue = d3.scale.category10();
data.forEach(function(d, i) {
           var drawColor=getDrawColor("chart1", parseInt(i));    
         var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "white";
            } else {
                color = "white";
            }
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            htmlstr = htmlstr + "<tr><td  style=\"background-color:" + color + ";\" width=25px>"  ;
            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="'+colorValue(i)+'" /></svg>';
            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {

                           htmlstr = htmlstr + "<td id='"+d[columns[0]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+tabDivVal+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[0]] + "</td>";

               }else{
                  
//                               htmlstr = htmlstr + "<td id='"+d[columns[0]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+tabDivVal+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[columns[0]] + "</u></td>";
                               htmlstr = htmlstr + "<td id='"+d[columns[0]]+":"+d[measureArray[0]]+"' class='"+classValue+"' index_value='"+indexValue+"' color_value='"+drawColor+"' onmouseover='tableGraphShow(\""+d[columns[0]] +"\")' onmouseout='tableGraphHide(\""+d[columns[0]] +"\")' onclick='drillWithinchart(this.id,\""+tabDivVal+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " >" + d[columns[0]] + "</td>";
               }
            
//                    var emojiWdith=divwidth/27;
//                    emojiWdith=emojiWdith>25?25:emojiWdith;
            for(var j=0;j<measureArray.length;j++){
            htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left;color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(tabDivVal, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,tabDivVal)) + "</td>";
              
            }

            htmlstr = htmlstr + "</tr>";

        
            
});
htmlstr += "</table></div></td></tr></table>";
//htmlstr += "</div>";
$("#pieWithMap").html(htmlstr);
}

function tableGraphShow(d){


 var barSelector = "." + "bars-Bubble-index-" + d.replace(/\s/g, "").trim()+"chart1";
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
}


function tableGraphHide(d){
//    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-index-" + d.replace(/\s/g, "").trim()+"chart1";
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
    
}


 







function buildDistrictMapMini(div,divId,data,columns,measureArray,divWid,divHgt){
     var color = d3.scale.category12();
var fun = "drillWithinchart(this.id,\"chart1\")";
     var districtmapchartData = data;
    
var chart12 = data["chart1"];
var districtData = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());


columns = chartData["chart1"]["viewBys"];
mapColumns = chartData["chart1"]["viewBys"];
measureArray = chartData["chart1"]["meassures"];
mapColumnsDistrict = chartData["chart2"]["viewBys"];
measureArrayDistrict = chartData["chart2"]["meassures"];
var colIds = chartData["chart1"]["viewIds"];
var w = divWid;
var h = 550;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale

var html1 = "<div id = 'mapDiv"+divId+"' style='height:auto;width:93%;float:right'></div>";
$("#"+divId).append(html1);
var map = d3.select("#mapDiv"+divId).append("svg:svg")
.attr("width", w)
.attr("height", h)
.style("margin-top","15px")
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
var currName = (d.id).toUpperCase();
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
if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
//     colorShad = color(i);
    return color(i);
}else{
    colorShad = "white";
}
}
   }


return colorShad;
})
.attr("index_value", function(d, i) {
   
                return "index-" + (d.id).toUpperCase().replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                var drawColor=getDrawColor(div, parseInt(i));
                    return drawColor;
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + (d.id).toUpperCase().replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
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
                                    msrData = numberFormat(chartMapData[l][measureArray[t]], round, precition,div)
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

   showState(this.id, d);
});
});

function initialize() {
proj.scale(4000);
proj.translate([-600, 450]);
}

//var htmlstr1="";
//htmlstr1 += "<div id ='mapTable"+div+"'  style = 'height:500px;overflow-y:auto;margin-top:20px;float:right;width:45%;'></div>";
if(typeof data["chart2"]!=='undefined'){
    chart12 = data["chart2"];
    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}
    function showState(state12, d) {
        var chartData = JSON.parse($("#chartData").val());
            div="chart1";
            var drillValues = [];
            var drills = {};
            var dimensions = chartData["chart1"]["dimensions"];
            var viewOvName = JSON.parse(parent.$("#viewby").val());
            var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
            var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][0]));

            var drillValues1 = [];
            drillValues.push(state12.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            var view = [];
            var chartId = ["chart1","chart2"];
            var viewName = [];
            if(typeof dimensions[currId+1]!="undefined" ){
                viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])]);
                view.push(dimensions[currId+1]);
            }else{
                $("#loading").hide();
                alert("Please Select More Parameters");
                return "";
            }
            for(var chId in chartId){
            chartData[chartId[chId]]["viewIds"]=view;
            chartData[chartId[chId]]["viewBys"]=viewName;
            }
            
            $("#chartData").val(JSON.stringify(chartData));
            $("#drills").val(JSON.stringify(drills));
            $.ajax({
                async:false,
                type:"POST",
                data: $("#graphForm").serialize(),
                url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(),
                success: function(data){
                    chart12=JSON.parse(data)["chart1"];
                    districtData=chart12;
                }
           });
           mapColumnsDistrict=chartData["chart1"]["viewBys"];
//            });
     var state1 = state12.split(":")
     var state = state1[0];
    var x, y, k;
                if (d && centered !== d) {
                    var centroid = path.centroid(d);
                    x = centroid[0];
                    y = centroid[1];
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
                div="chartAd1";
            $("#mapDiv" + div).html("");

                var html1 = "<img width='35' align='left' style='cursor: pointer;' height='35' onclick='getAdvanceVisuals(\""+parent.$("#graphsId").val()+"\")' class='ui-icon ui-icon-circle-arrow-w' id='mapIcon' alt='dgt' title='Back To Chart'/>\n\
                                <span id='stName'>"+state+"</span>"
               $("#mapDiv" + div).append(html1);
                var html = "";
                var svgcanvas;
                var data = chart12[0][columns];
                if (parent.$("#isShaded").val() == "true") {
                    map = d3.select('#maptDiv'+div).append("svg:svg")
                            .attr("width", w * 1.7)
                            .attr("height", h);
                    svgcanvas = map.append("g")
                            .attr("width", w * 1.7)
                            .attr("height", h);
                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:35%;margin-left:-55%;'></div>";
                    $('#mapDiv'+div).append(html);
                    $("#legends").show();
                    var svg1 = d3.select("#legends").append("svg")
                            .attr("width", "100%")
                            .attr("height", "100%");
                    if (parent.$("#shadeType").val() == "conditional") {
                        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
                        var selectedMeasure = conditionalMap["measure"];
                        var keys = Object.keys(conditionalMap);
                        svg1.append("g").append("text").attr("x", 20)
                                .attr("y", 9)
                                .attr("dy", ".35em").text(selectedMeasure);
                        for (var no = 0; no < (keys.length - 1); no++) {
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
                                            return autoFormating(conditionalMap[no]["limit1"]) + conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit2"]);
                                        } else {
                                            return conditionalMap[no]["operator"] + autoFormating(conditionalMap[no]["limit1"]);
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legends").css("width", w * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else if (parent.$("#shadeType").val() === "gradient") {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legends").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                        var max = maximumValue(data, shadingMeasure);
                        var min = minimumValue(data, shadingMeasure);
                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend")
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
                                .attr("fill", "url(#gradientWrdMapLegend)")
                                .attr("x", 0)
                                .attr("y", 15);
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", height)
                                .attr("dy", ".35em").text(autoFormating(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(autoFormating(max));


                        $("#legends").css("width", "8%");
                    }
                } else {
                    map = d3.select('#maptDiv'+div).append("svg:svg")
                            .attr("width", w * 2)
                            .attr("height", h);
                    svgcanvas = map.append("g")
                            .attr("width", w * 2)
                            .attr("height", h);
                }
               var svgcanvas1 = svgcanvas.append("g")
                        .attr("id", "places1");

              var  data1 = [];
                var cordMap = {};
                var count = -1;
                var currNameDistrict ;
                var htmlframe = "<iframe id='maps' frameborder='0' width='600px' height='600px'></iframe>";
                $("#chart2").attr("style", "display:block");
                $("#mapDiv"+div).append(htmlframe);
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
                    $map.find('g').each(function() {
                     var svgG=d3.select(this);
                     d3.select(this).selectAll("path").each(function(d, i){
                        var counter = i;
                                    var bbox = this.getBBox();
                        svgG.append("text").style("text-anchor","middle").attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")").text(function(d,j){
                            return data1[counter]["title"];
                        }).style("font-size","7px");
                      var district = $(this).attr('title');
                        $(this).attr('fill', function(){
                          count++;

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
//                      alert(district);
                      districtArray.push(district)
                       $(this).on("click", function(){
                          drillWithinchart(district.toUpperCase(),"chart1");
//                           showDistrictTable(district,districtData,"districtClick")
                       })

                      })
                 })
//                  showDistrictTable(districtArray,districtData,"stateClick")
 })

                    })


                    var abc = state.toLowerCase();
});

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
                                    msrData = numberFormat(data[l][measureArray[t]], round, precition,div)
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
buildDashboardTable("chart2","chartAd2", chart12, chartData["chart2"]["viewBys"], chartData["chart2"]["measures"], 200, 200);

            }
//$("#mapTable" + div).append(htmlstr1);
//var htmlstr = mapTableFunction(chart12,columns,measureArray);

//$("#" + div).append(htmlstr1);
//$("#mapTable" + div).append(htmlstr);


//var chart123=[];
//for(i=0;i<10;i++){
//   chart123.push(chart12[i]);
//}
 var centered;

   
    if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
    }         
}

function sideIconTab(){
    var divID = "chart1";
    var reportId = $("#graphsId").val();
    var corr = "Correlation";
    var chartData = JSON.parse($("#chartData").val());
     var measureName =   chartData[divID]["meassures"];
     var measureId =   chartData[divID]["meassureIds"];
        var chartName = "PieTable";
var iconHtml = "";
iconHtml += "<div style='float:left;width:30px'>";

//added by shivam
if(screen.width<1000){
iconHtml += "<div style='float:left;height:200px;width:30px;margin-top:-155px'>";
} else
    {
iconHtml += "<div style='float:left;height:200px;width:30px;margin-top:60px'>";
    }
iconHtml += "<span style='text-decoration: none'><img width='16' id='Vertical-Bar' height='16' title='Bar' style='margin-left:20%;cursor:pointer;' src='images/bar_icon.png' alt='Bar' onclick='advanceTypeFunction(\""+divID+"\",this.id,\""+chartName+"\",\"PieDB\")'></img></span>";
iconHtml += "<br>";
iconHtml += "<br>";
iconHtml += "<span style='text-decoration: none'><img width='16' height='16' id='Pie' title='Pie' style='margin-left:20%;cursor:pointer;' src='images/pie-icon.png' alt='Pie' onclick='advanceTypeFunction(\""+divID+"\",this.id,\""+chartName+"\",\"PieDB\")'></img></span>";
iconHtml += "<br>";
iconHtml += "<br>";
iconHtml += "<span style='text-decoration: none'><img width='16' height='16' id='Line' title='Trend' style='margin-left:20%;cursor:pointer;' src='images/trend_line.png' alt='Line' onclick='advanceTypeFunction(\""+divID+"\",this.id,\""+chartName+"\",\"PieDB\")'></img></span>";
iconHtml += "<br>";
iconHtml += "<br>";
iconHtml += "<span style='text-decoration: none'><img width='16' height='16' id='India-Map' title='India Map' style='margin-left:20%;cursor:pointer;' src='images/indiaMap_icon.jpg' alt='Map' onclick='advanceTypeFunction(\""+divID+"\",this.id,\""+chartName+"\",\"PieDB\")'></img></span>";
iconHtml += "<br>";
iconHtml += "<br>";
iconHtml += "<div class='dropdown' style='cursor:pointer;z-index:11;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16' title='Correlation' style='margin-left:20%;cursor:pointer;' title='Correlation' src='images/correlation_icon.png' alt='Correlation'></img></span>" +
     "<ul id='corrMeasure"+divID+"' class='dropdown-menu'>";
      for(var k=1;k<measureId.length;k++){
          iconHtml+="<li id='\""+measureId[k]+"\"' name='\""+measureName[k]+"\"' onclick='getCorrelationGraph(\""+measureId[0]+"\",\""+measureId[k]+"\",\""+measureName[0]+"\",\""+measureName[k]+"\",\""+reportId+"\",\""+corr+"\")' ><a >"+measureName[k]+"</a>";
      } 
 iconHtml += "</ul>";
 iconHtml+="</div>";
iconHtml += "<br>";
var statVariable = "deviation";
iconHtml += "<span style='text-decoration: none'><img width='20' height='16' style='margin-left:20%;cursor:pointer;' title='Standard Deviation' src='images/deviation_icon.gif' alt='Standard Deviation' onclick='getStatistics(\""+measureId[0]+"\",\""+measureName[0]+"\",\""+reportId+"\",\""+statVariable+"\")'></img></span>";
iconHtml += "<br>";
iconHtml += "<br>";
statVariable = "mean";
iconHtml += "<span style='text-decoration: none'><font width='20' height='16' style='margin-left:20%;cursor:pointer;color:black;font-weight:bold' title='Mean'  onclick='getStatistics(\""+measureId[0]+"\",\""+measureName[0]+"\",\""+reportId+"\",\""+statVariable+"\")'>Me</font></span>";
iconHtml += "<br>";
iconHtml += "<br>";
statVariable = "median";
iconHtml += "<span style='text-decoration: none'><font width='20' height='16' style='margin-left:20%;cursor:pointer;color:black;font-weight:bold' title='Median' src='images/deviation_icon.gif' alt='Median' onclick='getStatistics(\""+measureId[0]+"\",\""+measureName[0]+"\",\""+reportId+"\",\""+statVariable+"\")'>Med</font></span>";
iconHtml += "<br>";
iconHtml += "<br>";
statVariable = "variance";
iconHtml += "<span style='text-decoration: none'><font width='20' height='16' style='margin-left:20%;cursor:pointer;color:black;font-weight:bold' title='Variance' src='images/deviation_icon.gif' alt='Variance' onclick='getStatistics(\""+measureId[0]+"\",\""+measureName[0]+"\",\""+reportId+"\",\""+statVariable+"\")'>Var</font></span>";
iconHtml += "</div></div>";
$("#chartAd1").append(iconHtml);
}

var measureIndex=0;
//add by mynk sh.
function  bulletHorizontal(div, data, columns, measureArray, width, height, KPIResult1){
 window.t="header::header";    
var measureIndex=0;
var margin = {top: 20, right: 15, bottom: 20, left: 10};
width = width - margin.left - margin.right;
var bullethgt = 0;
var KPIResult = KPIResult1;
bullethgt =(height/(measureArray.length))*.9 ;
height=height/12;
var height1=height/12;
var bulletHorizontal = [];
graphProp(div);
var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
   var dashletid=div;

var chartData=JSON.parse(parent.$("#chartData").val());
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}

    var font1=[],font2=[],font3=[],dataColor=[],measureColor=[],measureAlias=[];
    for(var x in measureArray){
        if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[x]]!='undefined' && chartData[div]["measureNameSize"][measureArray[x]]!='undefined'){
            font1[x]=chartData[div]["measureNameSize"][measureArray[x]];
 }else{
     if( width<height){
                font1[x]=height/5;
            }else{
                font1[x]=width*.02;
            }}}
    for(var y in measureArray){
        if(typeof chartData[div]["measureValueSize"]!='undefined' && typeof chartData[div]["measureValueSize"][measureArray[y]]!='undefined' && chartData[div]["measureValueSize"][measureArray[y]]!='undefined'){
            font2[y]=chartData[div]["measureValueSize"][measureArray[y]];
        }else{
            if( width<height){
                font2[y]=height/5;
            }else{
                font2[y]=width*.02;
            }}}
    for(var z in measureArray){
        if(typeof chartData[div]["ticksValueSize"]!='undefined' && typeof chartData[div]["ticksValueSize"][measureArray[z]]!='undefined' && chartData[div]["ticksValueSize"][measureArray[z]]!='undefined'){
            font3[z]=chartData[div]["ticksValueSize"][measureArray[z]];
        }else{
            if( width<height){
                font3[z]=height/5;
            }else{
                font3[z]=width*.02;
            }}}
    for(var a in measureArray){
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[a]]!='undefined' && chartData[div]["measureAlias"][measureArray[a]]!== measureArray[a]){
            measureAlias[a]=chartData[div]["measureAlias"][measureArray[a]];
           }else{
            measureAlias[a]=measureArray[a];
        }}
   for(var b in measureArray){
       if(typeof chartData[div]["dataColor"]!='undefined' && typeof chartData[div]["dataColor"][measureArray[b]]!='undefined' && chartData[div]["dataColor"][measureArray[b]]!='undefined'){
           dataColor[b]=chartData[div]["dataColor"][measureArray[b]];
          }else{
            dataColor[b]="#696969";
        }}
   for(var c in measureArray){
   if(typeof chartData[div]["measureColor"]!='undefined' && typeof chartData[div]["measureColor"][measureArray[c]]!='undefined' && chartData[div]["measureColor"][measureArray[c]]!='undefined'){          
            measureColor[c]=chartData[div]["measureColor"][measureArray[c]];
     }else{
      measureColor[c]="#A1A1A1";
        }}
   
    
for(var i in KPIResult){
//  if(KPIResult[i]<100){
    bulletHorizontal.push(KPIResult[i])
//  }else{
//    bulletHorizontal.push(100)
//  }
}
var arr = [];

var mapArra = {};
var colorvalue= [];
//var max1 = parseFloat(maximumValue(data, measuretemp1[i]));
for (var i=0; i < measureArray.length; i++){
 var high,low,target,mid;
 //alert(chartData[div]["bulletParameters"]);
 if(typeof chartData[div]["bulletParameters"]!='undefined' && typeof chartData[div]["bulletParameters"][i]!="undefined" && typeof chartData[div]["bulletParameters"][i][measureArray[i]]!='undefined' ){
     if(typeof chartData[div]["bulletParameters"][i][measureArray[i]]["low"]!='undefined' ){
        low=chartData[div]["bulletParameters"][i][measureArray[i]]["low"];
      }else{
        low=bulletHorizontal[i]*.33;
     }
     if(typeof chartData[div]["bulletParameters"][i][measureArray[i]]["mid"]!=='undefined' ){
        mid=chartData[div]["bulletParameters"][i][measureArray[i]]["mid"];
     }else{
        mid=bulletHorizontal[i]*.66;
     }
      if(typeof chartData[div]["bulletParameters"][i][measureArray[i]]["high"]!=='undefined' ){
        high=chartData[div]["bulletParameters"][i][measureArray[i]]["high"];
     }else{
        high=bulletHorizontal[i]*1.5;
     }
     if(typeof chartData[div]["bulletParameters"][i][measureArray[i]]["target"]!=='undefined' ){
        target=chartData[div]["bulletParameters"][i][measureArray[i]]["target"];
     }else{
        target=bulletHorizontal[i]*.75;
    }
 }else{
      low=bulletHorizontal[i]*.33;
      mid=bulletHorizontal[i]*.66;
      high=bulletHorizontal[i]*1.5;
      target=bulletHorizontal[i]*.75;
 }

if(bulletHorizontal[i]>parseInt(target)){
  if(typeof chartData[div]["bulletAbovecolor"]!=='undefined' && chartData[div]["bulletAbovecolor"]!==''){
  colorvalue.push(chartData[div]["bulletAbovecolor"])
  }else{
  colorvalue.push("green")
  }

}else{
 if(typeof chartData[div]["bulletBelowcolor"]!=='undefined' && chartData[div]["bulletBelowcolor"]!==''){
  colorvalue.push(chartData[div]["bulletBelowcolor"])
  }else{
  colorvalue.push("red")
}

}

mapArra["title"] = measureArray[i];

mapArra["ranges"] = [parseInt(low),parseInt(mid),parseInt(high)];
//mapArra["ranges"] = [50,100,150];
mapArra["measures"] = [bulletHorizontal[i]];
mapArra["markers"] = [parseInt(target)];
//mapArra["markers"] = [170];
arr.push(mapArra);
bulletFunction(arr)
}
function bulletFunction(data){
   
var chart = bullet()
    .width(width)
    .height(bullethgt-margin.top-margin.bottom);
    if(fromoneview!='null'&& fromoneview=='true'){
div=dashletid
}
  var svg = d3.select("#" + div).selectAll("svg")
      .data(data)
    .enter().append("svg")
      .attr("class", "bullet")
      .attr("id",function(d,i){return div+"_"+measureArray[i]} )
       .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(bullethgt )+" ")
//      .attr("width", width + margin.left + margin.right)
//      .attr("height", bullethgt )
    .append("g")
      .attr("transform", "translate("+margin.left+"," + margin.top + ")")
      .call(chart)
     .on("mouseover",function(d){
             var columnList = [];


                columnList.push(columns[0]);
//              showtooltip(KPIResult, columnList, measureArray,this.id,this,div);
          });
  var functionDrilltoReports="drilltoReports(\""+window.t+"\",\""+div+"\",\""+getMeasureId(measureAlias[i])+"\")";
  var title = svg.append("g")
      .attr("transform", "translate(0,-5)")
      .style("word-wrap","break-word");
  title.append("text")
      .attr("class", "title")
      .style("font-size",font1[i]+"px")
      .style("fill",measureColor[i])
      .style("cursor","pointer")
      .text(function(d,i) {
        return measureAlias[i];
        })
      .attr("onclick", functionDrilltoReports);  

var title = svg.append("g")
      .style("text-anchor", "end")
      .attr("transform", "translate("+width+",-5)");

  title.append("text")
      .attr("class", "title")
      .style("font-size",font2[i]+"px")
      .style("fill",dataColor[i])
      .text(function(d,i) { return  addCommas(numberFormat(bulletHorizontal[i],yAxisFormat,yAxisRounding,div)) });
      // Update the range rects.

  d3.selectAll("button").on("click", function() {
    svg.datum(randomize).call(chart.duration(1000)); // TODO automatic transition
  });

//});
function randomize(d) {
  if (!d.randomizer) d.randomizer = randomizer(d);
  d.ranges = d.ranges.map(d.randomizer);
  d.markers = d.markers.map(d.randomizer);
  d.measures = d.measures.map(d.randomizer);
  return d;
}

function randomizer(d) {
  var k = d3.max(d.ranges) * .2;
  return function(d) {
    return Math.max(0, d + k * (Math.random() - .5));
  };
}
 function bullet() {
// alert("hii") 
 var orient = "left", // TODO top & bottom
      reverse = false,
      duration = 1200,
      ranges = bulletRanges,
      markers = bulletMarkers,
      measures = bulletMeasures,
      width = 380,
      height = bullethgt,
      tickFormat = null;
//alert("hiii")
  // For each small multipleÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦
  function bullet(g) {
      var rangecolor = [];
	  if(typeof chartData[div]["bulletLowcolor"]!="undefined" && chartData[div]["bulletLowcolor"]!=""){
	  rangecolor.push(chartData[div]["bulletLowcolor"])
	  }else{
	  rangecolor.push("#EEE")
	  }
      if(typeof chartData[div]["bulletMediumcolor"]!="undefined" && chartData[div]["bulletMediumcolor"]!=""){
	  rangecolor.push(chartData[div]["bulletMediumcolor"])
	  }else{
	  rangecolor.push("#DDD")
	  }
	  if(typeof chartData[div]["bulletHighcolor"]!="undefined" && chartData[div]["bulletHighcolor"]!=""){
	  rangecolor.push(chartData[div]["bulletHighcolor"])
	  }else{
	  rangecolor.push("#CCC")
	  }
    g.each(function(d, i) {
      var rangez = ranges.call(this, d, i).slice().sort(d3.descending),
          markerz = markers.call(this, d, i).slice().sort(d3.descending),
          measurez = measures.call(this, d, i).slice().sort(d3.descending),
          g = d3.select(this);

      // Compute the new x-scale.
      var x1 = d3.scale.linear()
          .domain([0, Math.max(rangez[0], markerz[0], measurez[0])])
          .range(reverse ? [width, 0] : [0, width]);

      // Retrieve the old x-scale, if this is an update.
      var x0 = this.__chart__ || d3.scale.linear()
          .domain([0, Infinity])
          .range(x1.range());

      // Stash the new scale.
      this.__chart__ = x1;

      // Derive width-scales from the x-scales.
      var w0 = bulletWidth(x0),
          w1 = bulletWidth(x1);

      // Update the range rects.
      var range = g.selectAll("rect.range")
          .data(rangez);

      range.enter().append("rect")
          .attr("class", function(d, i) { return "range s" + i; })
          .attr("id",function(d,i){
                var high,low,mid;
                if(typeof chartData[div]["bulletParameters"]!='undefined' && typeof chartData[div]["bulletParameters"][measureIndex]!="undefined" && typeof chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]!='undefined' ){
                    if(typeof chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["low"]!='undefined' ){
                        low=chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["low"];
                    }else{
                        low=bulletHorizontal[measureIndex]*.33;
                    }
                    if(typeof chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["mid"]!=='undefined' ){
                        mid=chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["mid"];
                    }else{
                        mid=bulletHorizontal[measureIndex]*.66;
                    }
                    if(typeof chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["high"]!=='undefined' ){
                        high=chartData[div]["bulletParameters"][measureIndex][measureArray[measureIndex]]["high"];
                    }else{
                        high=bulletHorizontal[measureIndex]*1.5;
                    }
                }else{
                    low=bulletHorizontal[measureIndex]*.33;
                    mid=bulletHorizontal[measureIndex]*.66;
                    high=bulletHorizontal[measureIndex]*1.5;
                }
                var label='',value='';
                if(i==0){
                    label='High';
                    value=high;
                }
                else if(i==1){
                    label='Medium';
                    value=mid;
                }
                else if(i==2){
                    label='Low';
                    value=low;
                }
              return label+":"+value;
          })
          .attr("width", w0)
          .attr("height", height)
          .style("fill", function(d, i){
              return rangecolor[i];
          })

//          .attr("onmouseover",function(){
//             var columnList = [];
//                columnList.push(columns[0]);
//                show_details(d.data, columnList, measureArray, this,"chart1");
//          })
          .attr("x", reverse ? x0 : 0)
          .attr("width", w1)
          range.on("mouseover",function(d,i){
              var id=this.id;
              var name,value;
              name=id.split(":")[0];
              value=addCurrencyType(div, getMeasureId(name)) + addCommas(id.split(":")[1]);
              var content = "<span class=\"name\">" + name + ":</span><span class=\"value\"> " + value + "</span><br/>";
              return tooltip.showTooltip(content, d3.event);
          })
          .on("mouseout",function(d,i){
              hide_details(d, i, this);
          })
      // Update the measure rects.
      var measure = g.selectAll("rect.measure")
          .data(measurez);

      measure.enter().append("rect")
          .attr("class", function(d, i) { return "measure s" + i; })
          .attr("id",function(d,i){
              return measureArray[measureIndex]+":"+measurez;
          })
          .attr("width", w0)
          .attr("height", height / 2)
          .attr("x", reverse ? x0 : 0)
          .attr("y", height / 3)
        .transition()
          .duration(duration)
          .attr("width", w1)
          .attr("x", reverse ? x1 : 0);

      measure.transition()
          .duration(duration)
          .attr("width", w1)
          .attr("height", height / 3)
          .attr("x", reverse ? x1 : 0)
          .attr("y", height / 3)
          .style("fill", colorvalue[i])
      measure.on("mouseover",function(){
          var name=this.id.split(":")[0];
          var value=this.id.split(":")[1];
          value=addCurrencyType(div, getMeasureId(name)) + addCommas(value);
          var content = "<span class=\"name\">" + name + ":</span><span class=\"value\"> " + value + "</span><br/>";
          return tooltip.showTooltip(content, d3.event);   
          })
          .on("mouseout",function(d,i){
              hide_details(d, i, this);
          });    
          
//            .on("mouseover", function(d, i) {
//                alert(d+":"+chartData[div]["viewBys"]+":"+measureArray+":"+this+":"+div+":"+bulletHorizontal[i])
//                show_detailsKPI(d, chartData[div]["viewBys"], measureArray, this,div,bulletHorizontal[i]);
//                }
//            ).on("mouseout", function(d, i) {
//                hide_details(d, i, this);
//            })
      // Update the marker lines.
      var marker = g.selectAll("line.marker")
          .data(markerz);
      marker.enter().append("line")
          .attr("class", "marker")
          .attr("x1", x0)
          .attr("x2", x0)
          .attr("y1", height / 6)
          .attr("y2", height * 5 / 6)
          .attr("id",(measureIndex++)+":"+markerz)
//        .transition()
//          .duration(duration)
          .attr("x1", x1)
          .attr("x2", x1)
          .on("mouseover", function() {
               var splitId=this.id.split(":");
                var measureName=measureArray[parseInt(splitId[0])];
                var name="Target("+measureName+")";
                var value=addCurrencyType(div, getMeasureId(measureName)) + addCommas(splitId[1]);
                var content = "";
                content += "<span class=\"name\">" + name + ":</span><span class=\"value\"> " + value + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);    
                })
                .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

      marker
          .attr("x1", x1)
          .attr("x2", x1)
          .attr("y1", height / 6)
          .attr("y2", height * 5 / 6);

      // Compute the tick format.
      graphProp(div)
      var format = tickFormat || x1.tickFormat(7);
      // Update the tick groups.
      var tick = g.selectAll("g.tick")
          .data(x1.ticks(8), function(d) {
            return this.textContent || addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
          });

      // Initialize the ticks with the old scale, x0.
      var tickEnter = tick.enter().append("g")
          .attr("class", "tick")
          .attr("transform", bulletTranslate(x0,(bullethgt*.9)))
          .style("opacity", 1e-6);

      tickEnter.append("line")
          .attr("y1", height)
          .attr("y2", height * 7 / 6);



      tickEnter.append("text")
      .attr("text-anchor", "middle")
          .style("font-size",font3[i]+"px")
           .text(function(d){
               return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
           });        

      // Transition the entering ticks to the new scale, x1.
      tickEnter
          .attr("transform", bulletTranslate(x1,(bullethgt*.9)-margin.top))
          .style("opacity", 1);

      // Transition the updating ticks to the new scale, x1.
      var tickUpdate = tick
          .attr("transform", bulletTranslate(x1,(bullethgt*.9)-margin.top))
          .style("opacity", 1);

      tickUpdate.select("line")
          .attr("y1", bullethgt)
          .attr("y2", bullethgt * 7 / 6);

//      tickUpdate.select("text")
//          .attr("y", height * 7 / 6);

      // Transition the exiting ticks to the new scale, x1.
      tick.exit().transition()
          .duration(duration)
          .attr("transform", bulletTranslate(x1,height))
          .style("opacity", 1e-6)
          .remove();
    });
    d3.timer.flush();
  }

  // left, right, top, bottom
  bullet.orient = function(x) {
    if (!arguments.length) return orient;
    orient = x;
    reverse = orient == "right" || orient == "bottom";
    return bullet;
  };

  // ranges (bad, satisfactory, good)
  bullet.ranges = function(x) {
    if (!arguments.length) return ranges;
    ranges = x;
    return bullet;
  };

  // markers (previous, goal)
  bullet.markers = function(x) {
    if (!arguments.length) return markers;
    markers = x;
    return bullet;
  };

  // measures (actual, forecast)
  bullet.measures = function(x) {
    if (!arguments.length) return measures;
    measures = x;
    return bullet;
  };

  bullet.width = function(x) {
    if (!arguments.length) return width;
    width = x;
    return bullet;
  };

  bullet.height = function(x) {
    if (!arguments.length) return height;
    height = x;
    return bullet;
  };

  bullet.tickFormat = function(x) {
    if (!arguments.length) return tickFormat;
    tickFormat = x;
    return bullet;
  };

  bullet.duration = function(x) {
    if (!arguments.length) return duration;
    duration = x;
    return bullet;
  };

 return bullet;
};

function bulletRanges(d) {
  return d.ranges;
}

function bulletMarkers(d) {
  return d.markers;
}

function bulletMeasures(d) {
  return d.measures;
}

function bulletTranslate(x,height11) {

  return function(d) {

    return "translate(" + x(d) + ","+height11+")";
  };
}

function bulletWidth(x) {
  var x0 = x(0);
  return function(d) {
    return Math.abs(x(d) - x0);
  };
}
}
    }
  function showtooltip(d, columnList, measureArray,i,element,div) {
        var content = "";
        var count = 0;
       content += "<span class=\"name\">" + measureArray[i] + ": </span><span class=\"value\"> " + d[i] + "</span><br/>";
                count++;
               return tooltip.showTooltip(content, d3.event);



    }





function show_detailsKPI(d, columns, measureArray, element,div,txtvalue,color) {
    measureArray=measureArray.toString().substring(0,measureArray.toString().indexOf(",")===-1?measureArray.toString().length:measureArray.toString().indexOf(","))
    d3.select(element).attr("stroke", "grey");
    var content = "";
    var title;
    var chartDetails ="";
    var colNameVal ="";
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var value = measureArray;
    title = columns;
    var chartData = JSON.parse(parent.$("#chartData").val());

    if(typeof chartData[div]["dataDisplay"]!=="undefined"){
        dataDisplay=chartData[div]["dataDisplay"];
    }else{
       dataDisplay="No";
    }
    if(typeof chartData[div]["valueOf"]!=="undefined"){
        displayType=chartData[div]["valueOf"];
    }else{
       displayType="Absolute";
    }
    if(typeof chartData[div]["rounding"]!=="undefined"){
        yAxisRounding=chartData[div]["rounding"];
    }else{
       yAxisRounding=0;
    }
     if(typeof chartData[div]["rounding1"]!=="undefined"){
        y2AxisRounding=chartData[div]["rounding1"];
    }else{
       y2AxisRounding=0;
    }
    if(typeof chartData[div]["yAxisFormat"]!=="undefined"){
           // alert(JSON.stringify(yAxisFormat));
      yAxisFormat=chartData[div]["yAxisFormat"];

    }else{
         yAxisFormat= "";
      //alert(JSON.stringify(yAxisFormat));
}
    if(typeof chartData[div]["y2AxisFormat"]!=="undefined"){
        y2AxisFormat=chartData[div]["y2AxisFormat"];
    }else{
       y2AxisFormat="";
    }
    if(typeof chartData[div]["displayX"]!=="undefined"){
        displayX=chartData[div]["displayX"];
    }else{
       displayX="Yes";
    }
    if(typeof chartData[div]["displayY"]!=="undefined"){
        displayY=chartData[div]["displayY"];
    }else{
       displayY="Yes";
    }
    if(typeof chartData[div]["displayY1"]!=="undefined"){
        displayY1=chartData[div]["displayY1"];
    }else{
       displayY1="Yes";
    }
    if(typeof chartData[div]["displayLegend"]!=="undefined"){
        displayLegend=chartData[div]["displayLegend"];
    }else{
       displayLegend="Yes";
    }
    if(typeof chartData[div]["colorLegend"]!=="undefined"){
        colorLegend=chartData[div]["colorLegend"];
    }else{
       colorLegend="Black";
    }

colNameVal=columns[0];


        var name = checkMeasureNameForGraph(measureArray);
//        newMeasureArray.push(name)
        var msrData;
        if (isFormatedMeasure) {
            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
            msrData = numberFormat(txtvalue, round, precition);
        }
  else{
            msrData = numberFormat(txtvalue, round, precition,yAxisFormat,yAxisRounding,div);
        }
        } else {
            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
            msrData = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(txtvalue);
            }else{
                 msrData = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(txtvalue,yAxisFormat,yAxisRounding,div));
        }
        }

        if (typeof columnMap[measureArray] !== "undefined" && columnMap[measureArray]["displayName"] !== "undefined") {
            var meaName=measureArray;
//            if(measIds.indexOf(columns[i])!="undefined"){
//                meaName=measName[measIds.indexOf(measureArray[i])];
//            }
            if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
            content += "<span class=\"name\">" + name + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray + ":</span><span class=\"value\"> " + msrData + "</span><br/>"; 
            }
    }
        else {
            var meaName=measureArray;
//            if(measIds.indexOf(columns[i])!="undefined"){
//                meaName=measName[measIds.indexOf(measureArray[i])];
//            }
           if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
            content += "<span class=\"name\">" + name + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray + ":</span><span class=\"value\"> " + msrData + "</span><br/>"; 
            }
        }
var confingMinRed=-50,confingMaxRed=-20,confingMinOrange=-20,confingMaxOrange=14,confingMinGreen=14,confingMaxGreen=25;
if(chartData[div]["chartType"]==='Dial-Gauge' && typeof color!='undefined' && color!=''){
    
            if(typeof chartData[div]["dialValues"] !="undefined" && chartData[div]["dialValues"] !=""){
if(typeof chartData[div]["dialValues"]["minGreeen"] !="undefined" &&chartData[div]["dialValues"]["minGreeen"]!='undefined' && chartData[div]["dialValues"]["minGreeen"] !=""){
    confingMinRed = chartData[div]["dialValues"]["minGreeen"];
}else{
    confingMinRed = -50;
}
if(typeof chartData[div]["dialValues"]["maxGreeen"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"]!='undefined' && chartData[div]["dialValues"]["maxGreeen"] !=""){
confingMaxRed = chartData[div]["dialValues"]["maxGreeen"];
}else{

    confingMaxRed =   -20 ;
}
if(typeof chartData[div]["dialValues"]["minOrange"] !="undefined" && chartData[div]["dialValues"]["minOrange"]!='undefined' && chartData[div]["dialValues"]["minOrange"] !=""){
confingMinOrange = chartData[div]["dialValues"]["minOrange"];
}else{
confingMinOrange =  -20 ;
}
if(typeof chartData[div]["dialValues"]["maxOrange"] !="undefined" && chartData[div]["dialValues"]["maxOrange"]!='undefined' && chartData[div]["dialValues"]["maxOrange"] !=""){
confingMaxOrange = chartData[div]["dialValues"]["maxOrange"];
}else{
confingMaxOrange =  14 ;
}
if(typeof chartData[div]["dialValues"]["minRed"] !="undefined" && chartData[div]["dialValues"]["minRed"]!='undefined' && chartData[div]["dialValues"]["minRed"] !=""){
confingMinGreen = chartData[div]["dialValues"]["minRed"];
}else{
confingMinGreen =  14 ;
}
if(typeof chartData[div]["dialValues"]["maxRed"] !="undefined" && chartData[div]["dialValues"]["maxGreeen"]!=='undefined' && chartData[div]["dialValues"]["maxRed"] !=""){
confingMaxGreen = chartData[div]["dialValues"]["maxRed"];
}else{
confingMaxGreen =  25 ;
}}
//alert(confingMinRed)
//alert(confingMaxRed)
if(typeof chartData[div]["chartType"]==='undefined' || chartData[div]["chartType"]==='std'){
}
else{
}
            if(color=='#109618'){//green
               if(typeof chartData[div]["dialType"]==='undefined' || chartData[div]["dialType"]==='std'){
                    content += "<span class=\"name\">Min :</span><span class=\"value\"> " + confingMinGreen + "</span><br/>";
                    content += "<span class=\"name\">Max :</span><span class=\"value\"> " + confingMaxGreen + "</span><br/>";
                }
                else{
                    content += "<span class=\"name\">Min :</span><span class=\"value\"> " + confingMinRed + "</span><br/>";
                    content += "<span class=\"name\">Max :</span><span class=\"value\"> " + confingMaxRed + "</span><br/>";
                }
            }
            else if(color=='#ff9900'){//amber
                content += "<span class=\"name\">Min :</span><span class=\"value\"> " + confingMinOrange + "</span><br/>";
                content += "<span class=\"name\">Max :</span><span class=\"value\"> " + confingMaxOrange + "</span><br/>";
            }
            else{
                 if(typeof chartData[div]["dialType"]==='undefined' || chartData[div]["dialType"]==='std'){
                    content += "<span class=\"name\">Min :</span><span class=\"value\"> " + confingMinRed + "</span><br/>";
                    content += "<span class=\"name\">Max :</span><span class=\"value\"> " + confingMaxRed + "</span><br/>";
                }
                else{
                    content += "<span class=\"name\">Min :</span><span class=\"value\"> " + confingMinGreen + "</span><br/>";
                    content += "<span class=\"name\">Max :</span><span class=\"value\"> " + confingMaxGreen + "</span><br/>";
                }
                
            }    
        }
    return tooltip.showTooltip(content, d3.event);
}
function tileChartsComp(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
    var kpiResultArr=[];
    var changePercent=0;
    changePercent=(KPIResult[0]-KPIResult[1])/(KPIResult[1])*100;
   changePercent=changePercent.toFixed(1);
//    changePercent ="+changePercent+";
    var appendDiv = "";
if(div.indexOf("kpiDiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}
   var chartData = JSON.parse(parent.$("#chartData").val());
    if(chartData[div]["chartType"]==='Combo-Analysis'){
        changePercent='';
    }
   if(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true'){
       for(var i in KPIResult){
           kpiResultArr.push(KPIResult[i]);
       }
   }else{
       kpiResultArr.push(KPIResult);
   }
   var chartData = JSON.parse(parent.$("#chartData").val());
   var showData = [];
   var showData1 = [];
   var absoluteNumber = 0;
   var perFlag = "";
   var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
	if(typeof chartData[div]["filename"]!="undefined" && chartData[div]["filename"]!=""){ 
   var imageTag =chartData[div]["filename"];
   }else{
   var imageTag ="";
   }
    var specialCharacter ="%";
    var colorPicker = "";
   var DataSum = 0;
   var gtFontSize = 0;
   var infoFontSize = 0;
   var btFontSize = 0;
   var comparativeFontSize = 0;
   var tableData = {};
    var nameArrCurrent = "";
    var nameArrPrior = "";
 

var lineData={};
 var prefix = "";
 var suffix = "";
 var prefixFontSize = "";
 var suffixFontSize = "";
for(var i in kpiResultArr){
showData[i] = kpiResultArr[i];
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
if(chartData[div]["chartType"]==='Combo-Analysis' && typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"]!="" ){
chartData[div]["kpiSubData"] = chartData[div]["kpiGTFont"]
}
$("#chartData").val(JSON.stringify(chartData));
//alert(getMeasureId(measureArray[0]));
                  if(yAxisFormat==""){
                            showData1.push(addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormatKPIChart(showData[i],yAxisFormat,yAxisRounding,div));
}
            else{
                            showData1.push(addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormatKPIChart(showData[i],yAxisFormat,yAxisRounding,div));
                }
}

if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){

for(var i in measureArray){
    if(chartData[div]["KPIName"]!=measureArray[i]){
}else{
     chartData[div]["KPIName"] = measureArray[0];
}
}}
else{
chartData[div]["KPIName"] = measureArray[0];

}
 var colorPicker;
 var measureColor;
if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
  colorPicker = chartData[div]["colorPicker"];
}else{
colorPicker = "#696969";
}
if(typeof chartData[div]["lFilledFont"]!="undefined" && chartData[div]["lFilledFont"]!=""){
  measureColor = chartData[div]["lFilledFont"];
}else{
measureColor = "#A1A1A1";
}


 if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
             gtFontSize=divHeight*.13;
            gtFontSize=gtFontSize>45?45:gtFontSize;
            gtFontSize=gtFontSize<35?35:gtFontSize;
            // add by maynk sh. for fontsize
             if(typeof chartData[div]["kpiGTFont"]!=="undefined"){
                      gtFontSize =  chartData[div]["kpiGTFont"];
             }
 }else{
if(divHeight<divWidth){
  gtFontSize = divHeight*.13
}else{
   gtFontSize = divWidth*.1
}
 }
 chartData[div]["kpiGTFont"]=gtFontSize;
  if(typeof chartData[div]["kpiFont"]!="undefined" && chartData[div]["kpiFont"] !== '' && chartData[div]["kpiFont"] !=="4"){
             infoFontSize=divHeight*.068;
            infoFontSize=infoFontSize>45?45:infoFontSize;
            infoFontSize=infoFontSize<35?35:infoFontSize;
            // add by maynk sh. for fontsize
             if(typeof chartData[div]["kpiFont"]!=="undefined"){
                      infoFontSize =  chartData[div]["kpiFont"];
             }
 }else{
if(divHeight<divWidth){
  infoFontSize = divHeight*.068
}else{
    infoFontSize = divWidth*.058
}
 }
chartData[div]["kpiFont"]=infoFontSize;
//Added by shivam
 if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
             btFontSize=divHeight*.13;
            btFontSize=btFontSize>45?45:btFontSize;
            btFontSize=btFontSize<35?35:btFontSize;
            // add by maynk sh. for fontsize
             if(typeof chartData[div]["kpiSubData"]!=="undefined"){
                      btFontSize =  chartData[div]["kpiSubData"];
             }
 }else{
if(divHeight<divWidth){
  btFontSize = divHeight*.13
}else{
    btFontSize = divWidth*.1
}
 }
chartData[div]["kpiSubData"]=btFontSize;

if(typeof chartData[div]["comparativeValue"]!="undefined" && chartData[div]["comparativeValue"] !== '' && chartData[div]["comparativeValue"] !=="4"){
             comparativeFontSize=divHeight*.068;
            comparativeFontSize=comparativeFontSize>45?45:comparativeFontSize;
            comparativeFontSize=comparativeFontSize<35?35:comparativeFontSize;
            // add by maynk sh. for fontsize
             if(typeof chartData[div]["comparativeValue"]!=="undefined"){
                      comparativeFontSize =  chartData[div]["comparativeValue"];
             }}else{
if(divHeight<divWidth){
  comparativeFontSize = divHeight*.068
}else{
    comparativeFontSize = divWidth*.058
}}
chartData[div]["comparativeValue"]=comparativeFontSize;

   $("#chartData").val(JSON.stringify(chartData));
   if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[div]["Prefixfontsize"];
}else{
   prefixFontSize = gtFontSize;
}
if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
}else{
    chartData[div]["Suffixfontsize"]=parseInt(gtFontSize*1.2);
    Suffixfontsize = infoFontSize;
}
if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
    prefix = chartData[div]["Prefix"];
}else{
    prefix = "";
}
if(typeof chartData[div]["Suffix"]!="undefined" && chartData[div]["Suffix"]!="" ){
    suffix = chartData[div]["Suffix"];
}else{
    suffix = "";
}
    parent.$("#chartData").val(JSON.stringify(chartData)); 
  var html = "";
   if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
     }
  html+="<div id='mainDiv"+div+"' style='width:95%;height:auto;  background-color:#FFF'>";
//  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
//  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";
  html+="<div id='innerDiv"+div+"' style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:#FFF'>";
  html+="<table style='float:left;margin-top: '>";
  html+="<tr id=' ::header' style='cursor:pointer;' onclick='drillWithinchart(this.id,\""+div+"\")'>";
var borderWidth=0;
if(kpiResultArr.length==1){
    borderWidth=1;
}
var upArrow='&nbsp;&#65514;'
var downArrow='&nbsp;&#65516;'
var arrow=''
var change=0;
if(parseInt(kpiResultArr[0])>parseInt(kpiResultArr[1])){
    
    arrow=upArrow;
}
else{
    arrow=downArrow;
}
if(chartData[div]["chartType"]==='Combo-Analysis'){
//alert(showData1[0])
if(typeof showData1[0]!="undefined" && showData1[0]!=""){
if(suffix.trim()==""){
   html+="<td style='color:"+colorPicker+";'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+showData1[0]+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span>"+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
}else{
   html+="<td style='color:"+colorPicker+";'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+showData1[0].toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span></span></td>";
}
}else{
html+="<td style='color:"+colorPicker+";'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>0</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span></span></td>";

}
}else{
if(suffix.trim()==""){
   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")'  ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1[0]+"<span style='font-family: Helvetica;font-size:"+comparativeFontSize+"px'>"+arrow+"("+changePercent+"%)</span>"+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}else{
   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")'  ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1[0].toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"<span style='font-family: Helvetica;font-size:"+comparativeFontSize+"px'>"+arrow+"("+changePercent+"%)</span></span></td>";
}}
//html+="<tr>";
// html+="<td ><img  onclick='trendChartdiv(\""+div+"\")' id='trendChart' class='trendChart1' style='float:right;margin-right: -10%;margin-top: -13%;width:30px;height:30px' src = 'images/icons/tiletrend.png'></td>";
//  html+="</tr>";

  html+="</tr>";
  html+="<tr>";
  
  var comparison=''
  if(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true' && typeof measureArray[1]!='undefined'){
      comparison=" | "+measureArray[1].split(" ")[measureArray[1].split(" ").length-1].replace("(","").replace(")","");
  }
  if(!(typeof chartData[div]["enableComparison"]!='undefined' && chartData[div]["enableComparison"]==='true')){
 if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     } 
     if(chartData[div]["chartType"]==='Combo-Analysis'){
  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Helvetica'>"+chartData[div]["KPIName"]+"</span></td>";
  }else{
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Helvetica'>"+measureArray[0]+"</span></td>";
  }
     }
  html+="</tr>";
  }
  else{
  if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     } 
     if(suffix.trim()==""){
  html+="<td style='color:"+colorPicker+";' ><span id='"+div+"span' style =' font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>("+prefix+"</span><span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormatKPIChart(kpiResultArr[1],yAxisFormat,yAxisRounding,div,"\"suffix\"")+comparison+")</span><span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
         
     }
     else{
  html+="<td style='color:"+colorPicker+";' ><span id='"+div+"span' style =' font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>("+prefix+"</span><span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormatKPIChart(kpiResultArr[1],yAxisFormat,yAxisRounding,div,"\"suffix\"").toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+comparison+")</span></td>";
    
     }
  html+="</tr>";    
  }
  if(chartData[div]["chartType"]==='Combo-Analysis'){
 html += "<tr style='border-style: solid;border-color:#D1D1D1;color:#D1D1D1;border-top: 0px double;border-width: 1px 0px 0px;'>";  
}else{
     html += "<tr style='border-style: solid;border-color:#D1D1D1;color:#D1D1D1;border-top: 0px double;border-width: 1px 0px 0px;'>";
  }
     if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+chartData[div]["KPIName"]+"</span></td>";
  }else{
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureArray[0]+"</span></td>";
  }
  html+="</tr>";
   
  html+="</table>";
  html+="</div>";
  html+="</div>";
if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
     }
  $("#"+appendDiv).html(html);
openTableDiv = function(div){
$("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Data Table'
    });
    parent.$("#chartData").val(JSON.stringify(chartData));
    $("#openTableTile").html("");
    tableData[div] = data[div];
    for(var i in tableData[div]){
        tableData[div][i]["Change %"]=((tableData[div][i][measureArray[0]]-tableData[div][i][measureArray[1]])/tableData[div][i][measureArray[1]]*100).toString();
    }
    if(measureArray.indexOf("Change %")==-1){
    measureArray.push("Change %");
        
    }
    //(KPIResult[0]-KPIResult[1])/(KPIResult[1])*100;
//    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,measureArray , 590, 620)
    $("#openTableTile").dialog('open');
}
//$("#"+div).html(html);
trendChartdiv = function(div){
$("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Trend Chart'
    });
    $("#openTableTile").html("");
    lineData[div] = data[div];
    var divId='tile';
buildLine(div, lineData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"], 500, 390,divId)
    $("#openTableTile").dialog('open');
}
$("#suffix"+div).css("font-size", ""+comparativeFontSize+"px")
$("#suffix"+div).css("font-weight", "")
}

function buildStackedKPIComp(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
var comparison1=measureArray[0].split(" ")[measureArray[0].split(" ").length-1];
    var comparison2=measureArray[1].split(" ")[measureArray[1].split(" ").length-1];
    var chartData = JSON.parse(parent.$("#chartData").val());
    measureArray=chartData[div]["meassures"];
    var dataArray = [];
    var fontSize1;
    var fontSize2;
    var preSuffData =[];
 var dashledid;
   var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val();
     if(fromoneview!='null'&& fromoneview=='true'){
dashledid=div;
div=div1;
     }
     graphProp(div);
 var tableData = {};
tableData[div] = data;
//    if(divWidth<divHeight){
//        fontSize1=divWidth/25;
//    }
//    else{
//        fontSize1=divHeight/25;
//    }
//    
//    if(fontSize1<10)
//    {
//        fontSize1=10;
//            
//    }
//    if(fontSize1>30){
//        fontSize1=30;
//    }
//    fontSize2=fontSize1;
    
//   for(var j in measureArray){
//    if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[j]]!='undefined' && chartData[div]["measureNameSize"][measureArray[j]]!='undefined'){
//        fontSize1=chartData[div]["measureNameSize"][measureArray[j]];
//    }else {
          if(divWidth<divHeight){
        fontSize1=divWidth/25;
    }
    else{
        fontSize1=divHeight/25;
    }
    
    if(fontSize1<10)
    {
        fontSize1=10;
            
    }
    if(fontSize1>30){
        fontSize1=30;
    }
//    chartData[div]["measureNameSize"]=fontSize1;
//    }

//    if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' ){
//        fontSize2=chartData[div]["kpiGTFont"];
//    }else {
          if(divWidth<divHeight){
        fontSize2=divWidth/25;
    }
    else{
        fontSize2=divHeight/25;
    }

    if(fontSize2<10)
    {
        fontSize2=10;

    }
    if(fontSize2>30){
        fontSize2=30;
    }
//    chartData[div]["kpiGTFont"]=fontSize2;
//    }
    $("#chartData").val(JSON.stringify(chartData));
// fontSize2=fontSize1;
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate
    for(var m in measureArray){
        DataSum_measureArray[m]=0;
        for(var d in data){
            DataSum_measureArray[m]+= parseFloat(data[d][measureArray[m]]);
        //            alert(parseFloat(data[d][measureArray[m]]));
        }
        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
        dataArray.push( DataSum_measureArray[m])
    }
    //    endof GT claculat
    //    alert(DataSum_measureArray);
    var tileWidth,tileHeight,rowCount,wDiv;
     var yAxisFormat = "";
   var yAxisRounding = 0;
  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[div]["yAxisFormat"];
  }else{
   yAxisFormat = "";
  }
  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
      yAxisRounding = chartData[div]["rounding"];
  }else{
   yAxisRounding =0;
  }
 //....
  DataSum_measureArray=[];
for(var k in KPIresult){
    var index=k%(measureArray.length/2);
    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[index]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[index]];
    }
    else{
        yAxisFormat="Auto";
    }
    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[index]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[index]];
    }
    else{
        yAxisRounding="1";
    }
                  if(yAxisFormat==""){

                    DataSum_measureArray1 = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div));
                    }
            else{
             DataSum_measureArray1 =      addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormat(KPIresult[k],yAxisFormat,yAxisRounding,div);
                }

    DataSum_measureArray.push(DataSum_measureArray1)

}
//    DataSum_measureArray= KPIresult;
//    var color1,color2;
//    if(typeof chartData[div]["stackLight"]!=='undefined' && chartData[div]["stackLight"]!==''){
//        color1=chartData[div]["stackLight"]
//    }
//    else{
//        color1=tileLightColor[0];
//    }
//    if(typeof chartData[div]["stackDark"]!=='undefined' && chartData[div]["stackDark"]!==''){
//        color2=chartData[div]["stackDark"]
//    }
//    else{
//        color2=tileDarkColor[0];
//    }
for(var q in KPIresult){
    index=k%(measureArray.length/2);
    var prefix = "";
    var suffix = "";
    var prefixFontSize = "";
    var suffixFontSize = "";
    if(typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" && typeof chartData[div]["PrefixfontsizeList"]!="undefined" && chartData[div]["PrefixfontsizeList"]!="" ){
    prefixFontSize = chartData[div]["PrefixfontsizeList"][index];
}else{
    prefixFontSize = fontSize2;
}
if(typeof chartData[div]["SuffixfontsizeList"]!="undefined" && chartData[div]["SuffixfontsizeList"]!="" &&  typeof chartData[div]["SuffixfontsizeList"][index]!="undefined" && chartData[div]["SuffixfontsizeList"][index]!="" ){
    Suffixfontsize = chartData[div]["SuffixfontsizeList"][index];
}else{
    Suffixfontsize = fontSize2;
}
if(typeof chartData[div]["PrefixList"]!="undefined" && chartData[div]["PrefixList"]!="" && typeof chartData[div]["PrefixList"][index]!="undefined" && chartData[div]["PrefixList"][index]!="" ){
    prefix = chartData[div]["PrefixList"][index];
}else{
    prefix = "";
}
if(typeof chartData[div]["SuffixList"]!="undefined" && chartData[div]["SuffixList"]!="" && typeof chartData[div]["SuffixList"][index]!="undefined" && chartData[div]["SuffixList"][index]!="" ){
    suffix = chartData[div]["SuffixList"][index];
}else{
    suffix = "";
}
if(typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] =="Y"){
if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'>"+suffix+"</span>");   
   }
}else{
  if(suffix==""){
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q]+"<span style='font-size:"+Suffixfontsize+"px'></span>");
}else{
preSuffData.push("<span style='font-size:"+prefixFontSize+"px'>"+prefix+"</span>"+DataSum_measureArray[q].replace(/[^0-9\.]/g, '')+"<span style='font-size:"+Suffixfontsize+"px'></span>");   
   }
}
   }
       
   var font1=[],font2=[],compFont=[];
   for(var i1 in measureArray){
       if(typeof chartData[div]["measureNameSize"]!='undefined' && typeof chartData[div]["measureNameSize"][measureArray[i1]]!='undefined' && chartData[div]["measureNameSize"][measureArray[i1]]!='undefined'){
           font1[i1]=chartData[div]["measureNameSize"][measureArray[i1]];
       }
       else{
           font1[i1]=18;
       }
   }
   for(var i2 in measureArray){
       if(typeof chartData[div]["measureValueSize"]!='undefined' && typeof chartData[div]["measureValueSize"][measureArray[i2]]!='undefined' && chartData[div]["measureValueSize"][measureArray[i2]]!='undefined'){
           font2[i2]=chartData[div]["measureValueSize"][measureArray[i2]];
       }
       else{
           font2[i2]=25;
       }
   }
   for(var i2 in measureArray){
       if(typeof chartData[div]["comparativeValueFont"]!='undefined' && typeof chartData[div]["comparativeValueFont"][measureArray[i2]]!='undefined' && chartData[div]["comparativeValueFont"][measureArray[i2]]!='undefined'){
           compFont[i2]=chartData[div]["comparativeValueFont"][measureArray[i2]];
       }
       else{
//           if(typeof compFont[i2]==='undefined'){
//               compFont[i2]=font2[i2]*.5;
//               chartData[div]["comparativeValueFont"][measureArray[i2]]=compFont[i2];
//               parent.$("chartData").val(JSON.stringify(chartData));
//           }else{
           compFont[i2]=18;
//       }
       }
   }
   var measureAlias=[];
   for(var a in measureArray){
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[a]]!='undefined' && chartData[div]["measureAlias"][measureArray[a]]!== measureArray[a]){
            measureAlias[a]=chartData[div]["measureAlias"][measureArray[a]];
}else{
            measureAlias[a]=measureArray[a];
        }
    }
   var dataColor=[];
   for(var b in measureArray){
       if(typeof chartData[div]["dataColor"]!='undefined' && typeof chartData[div]["dataColor"][measureArray[b]]!='undefined' && chartData[div]["dataColor"][measureArray[b]]!='undefined'){
           dataColor[b]=chartData[div]["dataColor"][measureArray[b]];
}else{
            dataColor[b]="#696969";
        }
    }
   var measureColor=[];
   for(var c in measureArray){
   if(typeof chartData[div]["measureColor"]!='undefined' && typeof chartData[div]["measureColor"][measureArray[c]]!='undefined' && chartData[div]["measureColor"][measureArray[c]]!='undefined'){          
            measureColor[c]=chartData[div]["measureColor"][measureArray[c]];
}else{
      measureColor[c]="#A1A1A1";
        }
    }
   var comparativeValueFont=[];
   for(var d in measureArray){
   if(typeof chartData[div]["comparativeValueFont"]!='undefined' && typeof chartData[div]["comparativeValueFont"][measureArray[d]]!='undefined' && chartData[div]["comparativeValueFont"][measureArray[d]]!='undefined'){          
            comparativeValueFont[d]=chartData[div]["comparativeValueFont"][measureArray[d]];
}else{
      comparativeValueFont[d]=fontSize2;
        }
    }
     
    
    var htmlstr = "";
    var measureCount=measureArray.length/2;
    if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid
    }

    if(measureCount==1){
        tileWidth=$("#"+div).width()-9;
        tileHeight=$("#"+div).height()-20;
        for(var i=0;i<measureCount;i++){
            if ( tileWidth> 80)
            {
                htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
            }
            else {
                 htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div style='background-color:white;width:100%;height:100%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
             }
           
//          
//            htmlstr +="<div class='maindiv'  style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div class='editdiv'  style='top:10px;float:left;'><a title='change image' href='#'onclick='addImageTag1(chart1)'><img style='position:absolute;display:none' src='images/icons/edit.png' height='15px' width='15px'/></a></div><img src='images/icons/measure2.png'/></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:"+datacolor[i]+";'><div style='background-color:#fff;font-style:bold;font-size:"+font2[i]+"px;line-height: 2;text-align:center;width:100%;height:48%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+font1[i]+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measurecolor[i]+"  ;'>"+measureAlias[i]+"</span></div></div>";
        }
    }
    else if(measureCount%2==0){
        index=0;
        if(k==1){
            index=0;
        }
        else{
            index=k/2;
        }
        rowCount=measureCount>2?2:1;
        wDiv=measureCount/rowCount;
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=$("#"+div).height()/(rowCount)-20;
        for(var i=0;i<measureCount;i++){
            var upArrow='&nbsp;&#65514;'
            var downArrow='&nbsp;&#65516;'
            var arrow=''
            var priorMeasure=measureArray[i+measureArray.length/2];
            var timePeriod=priorMeasure.substring(priorMeasure.indexOf("(")+1,priorMeasure.indexOf(")"));
            if(KPIresult[i]>KPIresult[i+measureArray.length/2]){
    
                arrow=upArrow;
            }
            else{
                arrow=downArrow;
            }
            var changePercent=0;
            changePercent=(KPIresult[i]-KPIresult[i+measureArray.length/2])/(KPIresult[i+measureArray.length/2])*100;
            changePercent=changePercent.toFixed(1);
            var borderCss='';
            if(i<measureCount/2 && measureCount>2){
                borderCss=';border-bottom:1px dotted #d1d1d1';
    }
//             if(i<=1 && measureCount==4){
//               htmlstr +="<div style='width:"+tileWidth+"px;border-bottom:1px dotted #d1d1d1; height:"+tileHeight+"px;background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+datacolor[i]+";font-family: Helvetica;float:bottom'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:50%;font-family: Helvetica;color:"+measurecolor[i]+";'>"+measureAlias[i]+"</span></div>";
////             htmlstr +="<div class='maindiv'  style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'   style='background-color:; width:30%;height:100%;float:left;'><div style='width:70%;;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color: #696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
//             }else{
        if (tileWidth> 80)
            {
//                 htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px"+borderCss+";background-color:white;display:block;float:left;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
//             }
                htmlstr += "<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px"+borderCss+";background-color:white;display:block;float:left;margin:3px'>";
                htmlstr += "<table style='border-collapse: separate;border-spacing:2px;margin-top:"+(parseInt(tileHeight*15)/100)+"px'>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+arrow+"<span style='font-size:"+(font2[i]/2)+"px;font-weight:bold;'>("+changePercent+"%)</span>";
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: normal;'>"+measureArray[i]
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-size:"+compFont[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>("+preSuffData[i+measureArray.length/2]+" | "+timePeriod+")"
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "</table>"
                
//                htmlstr += "<div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:33%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'><span style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</span></div>";
//                htmlstr += "<span style='background-color:white;font-style:bold;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+measureArray[i]+"</span>";
//                htmlstr += "<div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:33%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>ccccccccc</div>";
                htmlstr += "</div>";
}
            else{
                 htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'><div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:100%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span></div>";
    }
        }
    }
    else{
        rowCount=measureCount>2?2:1;
        wDiv=parseInt((measureCount)/rowCount);
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=$("#"+div).height()/(rowCount+1)-10;
        for(var i=0;i<measureCount-1;i++){
            var upArrow='&nbsp;&#65514;'
            var downArrow='&nbsp;&#65516;'
            var arrow=''
            priorMeasure=measureArray[i+measureArray.length/2];
            timePeriod=priorMeasure.substring(priorMeasure.indexOf("(")+1,priorMeasure.indexOf(")"));
            if(KPIresult[i]>KPIresult[i+measureArray.length/2]){
    
                arrow=upArrow;
            }
            else{
                arrow=downArrow;
            }
            var changePercent=0;
            changePercent=(KPIresult[i]-KPIresult[i+measureArray.length/2])/(KPIresult[i+measureArray.length/2])*100;
            changePercent=changePercent.toFixed(1);
            if (tileWidth> 80){
                    htmlstr += "<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px;border-bottom:1px dotted #d1d1d1;background-color:white;display:block;float:left;margin:3px'>";
                  htmlstr += "<table style='border-collapse: separate;border-spacing:2px;margin-top:"+(parseInt(tileHeight*15)/100)+"px'>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+arrow+"<span style='font-size:"+(font2[i]/2)+"px;font-weight:bold;'>("+changePercent+"%)</span>";
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: normal;'>"+measureArray[i]
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+compFont[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>("+preSuffData[i+measureArray.length/2]+" | "+timePeriod+")"
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "</table>"
                htmlstr += "</div>"
//                  htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px;border-bottom:1px dotted #d1d1d1;background-color:white;display:block;float:left;margin:3px'>
//                  <div style='background-color:white;width:100%;height:50%;float:top;margin-top: 0px;'><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:60%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div><div style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;width:100%;height:40%;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+"</div></div><span style = 'font-style:bold;font-size:"+font1[i]+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:"+measureColor[i]+";'>"+measureAlias[i]+"</span ></div>";
        }
            else{
                  htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'>"
                htmlstr += "<table style='border-collapse: separate;border-spacing:2px;margin-top:"+(parseInt(tileHeight*15)/100)+"px'>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+arrow+"<span style='font-size:"+(font2[i]/2)+"px;font-weight:bold;'>("+changePercent+"%)</span>";
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: normal'>"+measureArray[i]
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+compFont[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>("+preSuffData[i+measureArray.length/2]+" | "+timePeriod+")"
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "</table>"
                htmlstr += "</div>"
    }
        }
            
            if(KPIresult[i]>KPIresult[i+measureArray.length/2]){
    
                arrow=upArrow;
            }
            else{
                arrow=downArrow;
            }
            changePercent=0;
            changePercent=(KPIresult[i]-KPIresult[i+measureArray.length/2])/(KPIresult[i+measureArray.length/2])*100;
            changePercent=changePercent.toFixed(1);
            priorMeasure=measureArray[i+measureArray.length/2];
            timePeriod=priorMeasure.substring(priorMeasure.indexOf("(")+1,priorMeasure.indexOf(")"));
            if (tileWidth> 80){
                 htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:white;display:block;margin:3px'>"
                htmlstr += "<table style='border-collapse: separate;border-spacing:2px;margin-top:"+(parseInt(tileHeight*15)/100)+"px'>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+arrow+"<span style='font-size:"+(font2[i]/2)+"px;font-weight:bold;'>("+changePercent+"%)</span>";
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+measureArray[i]
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+compFont[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>("+preSuffData[i+measureArray.length/2]+" | "+timePeriod+")"
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "</table>"
                htmlstr += "</div>"
        }
           else{
                 htmlstr +="<div onclick='openTableDiv1(\""+div+"\",\""+i+"\")' style='width:"+$("#"+div).width()-15+"px; height:"+tileHeight*.40+"px;background-color:white;display:block;margin:3px'>"
                htmlstr += "<table style='border-collapse: separate;border-spacing:2px;margin-top:"+(parseInt(tileHeight*15)/100)+"px'>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font2[i]+"px;line-height: -moz-block-height;text-align:center;color:"+dataColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+preSuffData[i]+arrow+"<span style='font-size:"+(font2[i]/2)+"px;font-weight:bold;'>("+changePercent+"%)</span>";
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+font1[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>"+measureArray[i]
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "<tr>"
                htmlstr += "<td style='background-color:white;font-style:bold;font-size:"+compFont[i]+"px;line-height: -moz-block-height;text-align:center;color:"+measureColor[i]+";font-family: Helvetica;float:bottom;font-weight: bold;'>("+preSuffData[i+measureArray.length/2]+" | "+timePeriod+")"
                htmlstr += "</td>"
                htmlstr += "</tr>"
                htmlstr += "</table>"
                htmlstr += "</div>"
        }
//        htmlstr +="<div  class='maindiv'  style='width:"+($("#"+div).width()-12)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div class='imgdiv'  style='background-color:; width:30%;height:100%;float:left;'></div><div style='width:70%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:48%;float:top;color:#696969;'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: 2;text-align:center;width:100%;height:50%;font-family: Helvetica; float:bottom'>"+preSuffData[i]+"</div></div><hr style='border-top:#d1d1d1;width:80%'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: 1.5;text-align:center;word-wrap: break-word;width:100%;height:100%;font-family: Helvetica;color:#A1A1A1;'>"+measureArray[i]+"</span></div></div>";
    }
              
    $("#"+div).append(htmlstr);
//Added by shivam
openTableDiv1 = function(div,count){
$("#openTableTile").dialog({
         autoOpen: false,
         height: 440,
         width: 620,
         position: 'justify',
         modal: true,
         resizable:true,
        title:'Data Table'
    });
//    $("#openTableTile").html("");
////    alert(JSON.stringify(data))
//    tableData[div] = data;
////    alert(div)
////    alert(JSON.stringify(tableData))
////    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    $("#openTableTile").dialog('open');
//}
    $("#openTableTile").html("");
    tableData[div] = data;
    var measureArr = [];
//    alert(JSON.stringify(Data));
    measureArr.push(chartData[div]["meassures"][count])
//    alert(JSON.stringify(tableData))
//    alert(measureArr)
//    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,measureArr , 590, 620)
    $("#openTableTile").dialog('open');
}

    }

function buildTrendWithKPIChart(div, data, columns, measureArray, divWidth, divHeight,KPIresult,flag){
$("#"+div).html('');
 var isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/));
 if(isAtLeastIE11)
     {
  $("#"+div).css({'height':'55%'});
     }
    buildMultiMeasureTrLine(div, data, columns, measureArray, divWidth, divHeight*0.45);
    buildStackedKPI(div, data, columns, measureArray, divWidth, divHeight*0.45,KPIresult,flag)
}
