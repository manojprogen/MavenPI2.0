/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Mayank
 */
var comboHBar={};
function buildGroupedBar(div,data, columns, measureArray,divWdt,divHgt,divAppend) {
 var chartData = JSON.parse(parent.$("#chartData").val());
 var groupedBarWithValue = chartData[div]["groupedBarWith"];
 var lineFlag = false;
 var dualFlag = false;
 var gtFlag = false;
 var lineData;
if(typeof groupedBarWithValue!=="undefined" && ( 
    groupedBarWithValue["Over Layered Line"]=="Over Layered Line" || 
    groupedBarWithValue["Dual Axis"]=="Dual Axis" ||  groupedBarWithValue["GT"]=="GT")){
    lineFlag = true;
    if(groupedBarWithValue["Dual Axis"]=="Dual Axis"){
        dualFlag=true;
    }
    if(groupedBarWithValue["GT"]=="GT"){
        gtFlag=true;
    }
    
    }
if(lineFlag){
    
lineData = getDataForSingleView(div);
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

    var columnsVar = columns;
    var mainWidth = divWdt
var fromoneview=parent.$("#fromoneview").val();
var dashletid=div;
var colIds;
var colName;
     var color = d3.scale.category10();
  var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1
colIds=chartData[div1]["viewIds"];
colName = chartData[div]["viewBys"];
}else{
 colIds = chartData[div]["viewIds"];
 colName = chartData[div]["viewBys"];
    var prop = graphProp(div);
}
var msr = [];
msr = data;
//divWdt=parseFloat($(window).width())*(.75);
    var chartMap = {};
    var colorGroup = {};
    var measure1 = measureArray[0];
    if (columns.length < 2) {
        columns.push(columns[0]);
    }
    var minVal = minimumValue(data, measure1);
    if(lineFlag){
    // for line
    var lineMeas = [];
    for(var k in measureArray){
       if(k==0){
           
       }else{
           
       lineMeas.push(measureArray[k]);
       }
    }
    // for line
//    var minVal1 = minimumValue(lineData, lineMeas);
    var minVal1 = 0;
    var maxVal1 = maximumValue(lineData,lineMeas);
    for(var i in lineMeas){
var max1 = parseFloat(maximumValue(lineData, lineMeas[i]));
if(i==0)  {
maxVal1=parseFloat(max1);
}
else if(i>0 && (max1 > maxVal1)){
maxVal1 = max1;
}
}
}
    //end
    var autoRounding1;
     var customTicks = 5;

   if(fromoneview!='null'&& fromoneview=='true'){

      }else{

if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }}

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
//        Added by shivam
if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes"){
  if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    var data1 = {};
    var data2 = {};
    var xdata = [];
    var margin = {
        top: 10,
//        right: 0,
        right: (mainWidth*.08),
//        bottom: 70,
        bottom: (divHgt*.1),
        left: 50
    },
    width = ((mainWidth- margin.left - margin.right)*1.07)-40,
            height = divHgt  - margin.top - margin.bottom;
}else{
          var data1 = {};
    var data2 = {};
    var xdata = [];
    var margin = {
        top: 10,
//        right: 0,
        right: (mainWidth*.08),
//        bottom: 70,
        bottom: (divHgt*.1),
        left: 50
    },
    width = ((mainWidth- margin.left - margin.right)*1.13)+40,
            height = ((divHgt  - margin.top - margin.bottom))*.9;
}
}
else{
//    alert("2");
      var data1 = {};
    var data2 = {};
    var xdata = [];
    var margin = {
        top: 10,
//        right: 0,
        right: (mainWidth*.08),
//        bottom: 70,
        bottom: (divHgt*.1),
        left: 50
    },
    width = ((mainWidth- margin.left - margin.right)*1.13)+40,
            height = divHgt  - margin.top - margin.bottom;
}
//Added by shivam

//        var range =.1;
//             if(typeof chartData[div]["barsize"]!=="undefined" && chartData[div]["barsize"]==="Yes"){
//             range = .6
//    }
//             var x = d3.scale.ordinal()
//                .rangeRoundBands([0, width+35], range);

    var x0 = d3.scale.ordinal()
            .rangeRoundBands([0, width+35], .1);

    var x1 = d3.scale.ordinal();

    var y = d3.scale.linear()
            .range([height, 0]);
            
    var y1 = d3.scale.linear()
            .range([height, 0]);
if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
    var xAxis = d3.svg.trendaxis()
            .scale(x0)
            .orient("bottom");
}else{
    var xAxis = d3.svg.axis()
            .scale(x0)
            .orient("bottom");
}
    var yAxis,yAxis1;
         if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_y_axis = function() {
    return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
    make_y_axis1 = function() {
    return d3.svg.gridaxis()
        .scale(y1)
        .orient("right")
        .ticks(customTicks)
}
         }else{
            make_y_axis = function() {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
            make_y_axis1 = function() {
    return d3.svg.axis()
        .scale(y1)
        .orient("right")
        .ticks(customTicks)
}
         }
    // .tickFormat(d3.format(".2s"));
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                  .ticks(customTicks)
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });

    } else {
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
                });
                if(dualFlag){
                yAxis1 = d3.svg.trendaxis()
                .scale(y1)
                .orient("right")
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
                });
                }
    }
    var localColorMap = {};
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    svg = d3.select("#"+div)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(mainWidth+ margin.left + margin.right)+" "+(divHgt+47)+" ")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("width", mainWidth*1.8)
//            .attr("height", divHgt)
            .attr("style","float:left")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//      for (var j=0; j <= height; j=j+50) {
//    svg.append("svg:line")
//        .attr("x1", 0)
//        .attr("y1", j)
//        .attr("x2", width)
//        .attr("y2", j)
//        .style("stroke", "#A69D9D")
//        .style("stroke-width", .5)
//        .style("z-index", "9999");
//};
if(fromoneview!='null'&& fromoneview=='true'){
        div=div1;
    }
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
 svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width-35, 0, 0)
            .tickFormat("")
        )
        
    }
    var mdata = [];
    var keys = [];
    var max;
    var groupedTotal = {};
    var indiviTotal = {};
    data.forEach(function(d, j) {
        if (keys.indexOf(d[columns[0]]) === -1 ) {
            keys.push(d[columns[0]]);
        }
        if (typeof groupedTotal[d[colName[0]]] === "undefined") {
            groupedTotal[d[colName[0]]] = 0;
        }
        try {
            groupedTotal[d[colName[0]]] = parseFloat(d[measureArray[0]]) + parseFloat(groupedTotal[d[colName[0]]]);
        } catch (e) {
        }
        if (typeof indiviTotal[d[colName[1]]] === "undefined") {
            indiviTotal[d[colName[1]]] = 0;
        }
        try {
            indiviTotal[d[colName[1]]] = parseFloat(d[measureArray[0]]) + parseFloat(indiviTotal[d[colName[1]]]);
        } catch (e) {
        }
    });
    var numberOfColumns = keys.length;
    for (var j = 0; j < keys.length; j++) {
        data2 = {};
        var count = 0;
        data2[colName[0]] = keys[j];
        for (var i = 0; i < data.length; i++) {

            if (keys[j] === data[i][colName[0]]) {
                data2[data[i][colName[1]]] = data[i][measure1];
                count++;
                if (i === 0) {
                    max = data[i][measure1];
                }
                else {
                    if (max < parseFloat(data[i][measure1])) {
                        max = data[i][measure1];
                    }
                }
            }
        }
        mdata.push(data2);
    }

    var uncheckList = [];
    var val = [];
    var val1 = [];
    var val2 = [];
    var ageNames1 = {};
    data = mdata;
    var count = 0;
    var colorCount=0
    var groupNames = {};
    for (var k = 0; k < keys.length; k++) {
        var Names = [];


        val2 = d3.keys(data[k]).filter(function(key) {
            return key !== columns[0];
        });
        Names = val2.filter(function(d) {
            return d !== "ages";
        });
        if (Names.indexOf(data[k]) === -1)
            ageNames = Names;
        if (ageNames) {
            data.forEach(function(d, i) {
                if (i === k) {
                    d.ages = ageNames.map(function(name, i) {
                        return {name: name, value: +d[name], parentName: d[colName[0]]};
                    });
                }
                else {
                    d.ages = [];
                }
            });
        }
        x0.domain(data.map(function(d, i) {
            return d[columns[0]];
        }));
        x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()]);
        for (var i = 0; i < ageNames.length; i++) {
            ageNames1[ageNames[i]] = ageNames[i];
            groupNames[ageNames[i]]=ageNames[i];
        }
        var yVal = 0;



if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
//    for (var key in data) {
//        min1.push(data[key][measure1]);
//
//    }
}}else{
//    for (var key in data) {
//        min1.push(data[key][measure1]);
//
//    }
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["axisMin"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }else{

        if (minVal < 0) {
            yVal = minVal;
        }
}
}else{
//    if (data.length > 1) {
//        minVal = minimumValue(data, measure1) * .8;
//    }else{
//        minVal = 0;
//       }
 if (minVal < 0) {
            yVal = minVal;
        }
    }
  if(yVal.length>1){
        y.domain([yVal, max]).clamp(true);
}else{
     y.domain([parseFloat(minVal), parseFloat(max)]).clamp(true);
}
// for line
y1.domain([parseFloat(minVal1), parseFloat(maxVal1)]).clamp(true);






        var col = [];
        data.forEach(function(d) {
            col.push(d[columns[0]]);
        });

        if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
        if (count < 1) {
            svg.append("g")
                    .attr("class", "x axis")
                    .attr("transform", "translate(0," + height + ")")
                    .call(xAxis)
                    .selectAll('text')
                    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
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
                    .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "rotate(-90)";
        }else {
            return "rotate(-30)";
        }
    })
                    .style('text-anchor',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "middle";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return  "middle";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "end";
        }else {
            return "end";
        }
    })
                    .text(function(d,i) {
                        return buildXaxisFilter(div,d,i);
//                        if (d.length < 10)
//                            return d;
//                        else
//                          return d.substring(0, 10) + "..";
                    });
        }
    }
//
        if (count < 1) {
            svg.append("g")
                    .attr("class", "y axis")
                    .call(yAxis)
                    .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "0")
        .attr("x", "-9")
                    .style("text-anchor", "end")
        .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
            if(dualFlag){
            // for line 
          svg.append("g")
                    .attr("class", "y axis right")
                    .attr("transform", "translate(" + (width+45) + ",0)")
                    .call(yAxis1)
                    .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "0")
        .attr("x", "9")
                    .style("text-anchor", "end")
        .style('font-size',function(d,i) {
                return font2(div,d,i);
            })  
           // end
           }
            count++;
            
              }
        
         
         
      // end        
              
        var Brands = svg.selectAll(".Category")
                .data(data)
                .enter().append("g")
                .attr("class", "g")
                .attr("id", keys[k])
                .attr("transform", function(d) {
                    return "translate(" + x0(d[colName[0]]) + ",0)";
                });
        Brands.selectAll(".gClass-" + keys[k].replace(/[^a-zA-Z0-9]/g, '', 'gi'))
                .data(function(d) {
                    val = d.ages.filter(function(d) {
                        return d.value !== 0;
                    });
                    return val;
                })
                .enter().append("g").attr("class", "gClass-" + keys[k].replace(/[^a-zA-Z0-9]/g, '', 'gi')).append("rect")
                .attr("width", x1.rangeBand())
                .attr("x", function(d) {
                    return x1(d.name);
                })
                .attr("y", function(d) {
                    return y(d.value);
                })
                .attr("rx", barRadius)
                .attr("height", function(d) {
                    return height - y(d.value);
                })
                .style("fill", function(d,i) {
                       var drillColumns;
                       var drilledvalue;
                         if(typeof colIds[1]!="undefined"){
                           drillColumns = colIds[1];
                         }else{
                          drillColumns = colIds[0];
                         }
                     try{
                         drilledvalue=JSON.parse(parent.$("#drills").val())[drillColumns];
                     }catch(e){}
//                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    } else{
                        if(typeof columnsVar[1]=="undefined" || columns[1] == null){
                        if(typeof colorGroup[d.parentName]!="undefined" ){
                            return colorGroup[d.parentName]
                        }else{
                        colorGroup[d.parentName]=getDrawColor(div,colorCount);
                         groupColor = getDrawColor(div,colorCount);
                        return getDrawColor(div,colorCount++);
                        }
                    }
                        else{

                        if(typeof colorGroup[d.name]!="undefined" ){
                            return colorGroup[d.name]
                        }else{
                        colorGroup[d.name]=getDrawColor(div,colorCount);
                         groupColor = getDrawColor(div,colorCount);
                        return getDrawColor(div,colorCount++);
                        }
                    }
                    }

//                    return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d.parentName+":"+d.name + ":" + d.value;
                })
                .attr("index_value", function(d, i) {
                    return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("color_value", function(d, i) {
                     var drilledvalue;
                     var drillColumns;
                         if(typeof colIds[1]!="undefined"){
                           drillColumns = colIds[1];
                         }else{
                          drillColumns = colIds[0];
                         }
                     try{
                         drilledvalue=JSON.parse(parent.$("#drills").val())[drillColumns];
                     }catch(e){}
//                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    } else{
                        if(typeof columnsVar[1]=="undefined" || columns[1] == null){
                        if(typeof colorGroup[d.parentName]!="undefined" ){
                            return colorGroup[d.parentName]
                        }else{
                        colorGroup[d.parentName]=getDrawColor(div,colorCount);
                        groupColor = getDrawColor(div,colorCount);
                        return getDrawColor(div,colorCount++);
                        }
                    }
                        else{

                        if(typeof colorGroup[d.name]!="undefined" ){
                            return colorGroup[d.name]
                        }else{
                        colorGroup[d.name]=getDrawColor(div,colorCount);
                        groupColor = getDrawColor(div,colorCount);
                        return getDrawColor(div,colorCount++);
                        }
                    }
                    }

                })
                .attr("class", function(d, i) {

                    if(typeof d.name =="undefined" || d.name == "undefined"){
                    return "bars-Bubble-index-" + d.parentName.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
                    }else {
                          return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
                    }
                })
                .attr("onclick", fun)
        //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})

                .on("mouseover", function(d, i, j) {


                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;


                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                    var msrData;
					 if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d.value);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                        msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d.value);

                }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }
                     //   msrData = addCommas(d.value);
//                    }
                    var content = "";
                    content += "<span class=\"name\">" + colName[0] + ":</span><span class=\"value\"> " + d.parentName + "</span><br/>";
                    if(typeof d.name!="undefined" && typeof colName[1] !="undefined"){
                    content += "<span class=\"name\">" + colName[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                }
                    content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
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


//        var legend = svg.selectAll(".legend")
//                .data(ageNames.slice().reverse())
//                .enter().append("g")
//                .attr("class", "legend")
//                .attr("transform", function(d, i) {
//                    return "translate(0," + i * 20 + ")";
//                });
//        if (count < 1) {
//            legend.append("rect")
//                    .attr("x", width - 18)
//                    .attr("width", 18)
//                    .attr("height", 18).attr("rx", barRadius)
//                    .style("fill", function(d) {
//                        return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                    });
//
//            legend.append("text")
//                    .attr("x", width - 24)
//                    .attr("y", 9)
//                    .attr("dy", ".35em")
//                    .style("text-anchor", "end")
//                    .text(function(d) {
//                        return d;
//                    });
//        }

    }
    
    // for line
var lineMeasure = [];
var completeLineData = [];
var indexList = [];
for(var q in lineData){
    for(var t=0;t<keys.length;t++){
        
        if(lineData[q][columns[0]]==keys[t]){
            indexList.push(t.toString());
        }
    }
}
for(var w in indexList){
    completeLineData.push(lineData[indexList.indexOf(w.toString())]);
}
lineData = completeLineData;
var innerRecord = chartData[div]["innerRecords"];
if(typeof innerRecord =="undefined"){
    innerRecord = 5;
}
var loopLength = 0;
lineMeasure.push(measureArray[0]);
for(var k in measureArray){
   if(k==0 && measureArray.length>1){
       lineMeasure = [];
   }else{
       if(dualFlag){
           
       lineMeasure.push(measureArray[k]);
       }else{
           if(gtFlag){
               lineMeasure = [];
                lineMeasure.push(measureArray[0]);
           }else{
               
           lineMeasure.push(measureArray[1]);
           }
       }
   }
    }
for(var m in lineMeasure){
var valueline = d3.svg.line()
            .x(function(d) {


                return x0(d[columns[0]]) + (x0.rangeBand()-(x1.rangeBand()*(parseInt(innerRecord)/2)));
            })
            .y(function(d) {
                if(dualFlag){
                return y1(d[lineMeasure[m]]);
                    
                }else{
                    return y(d[lineMeasure[m]]);
                }
            });

 var path = svg.append("path")
            .data(lineData)
            .attr("d", function(d){
                return valueline(lineData)
            
            })
            .attr("fill", "transparent")
            .style("z-index", "0")
            .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
            //alert(chartData[div]["lineType"]);
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,3";
        }
        else{
            return "1,0";
    }
}
            )  // for dash line by mynk sh.
            .style("stroke", function(d, i) {
               var colorShad;
    colorShad = getDrawColor(div, parseInt(0)); 
                return colorShad;
            }) .style("stroke-width", "3px");
try {
            var totalLength = path.node().getTotalLength();

    path
      .attr("stroke-dasharray", totalLength + " " + totalLength)
      .attr("stroke-dashoffset", totalLength)
      .transition()
        .duration(2000)
        .ease("linear")
        .attr("stroke-dashoffset", 0);
}catch(e){
   
}

var blueCircles = svg.selectAll("dot")
            .data(lineData)
            .enter().append("circle")
            .attr("r", function(d){
                var radius;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        radius = "8";
                    }else{
            return radius = "4"
                    }
                    return radius;
            })
            .attr("cx", function(d) {
//                return x0(d[columns[0]]) + (x0.rangeBand()-43);
                return x0(d[columns[0]]) + (x0.rangeBand()-(x1.rangeBand()*(parseInt(innerRecord)/2)));
            })
            .attr("cy", function(d) {
                if(dualFlag){
                return y1(d[lineMeasure[m]]);
                    
                }else{
                    return y(d[lineMeasure[m]]);
                }
            })
             .attr("id", function(d) {
                return d[columns[0]] + ":" + d[lineMeasure[m]];
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
            colorShad = getDrawColor(div, parseInt(0));
               }
        if (typeof chartData[div]["lineSymbol"] === "undefined" || chartData[div]["lineSymbol"] === "Hollow") {  
           if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        return drillShade;
            }else{
            return "white";
        }}else{
                return colorShad;
                    }
            })
            .style("color_value", function(d,i){
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
        }else{
        colorShad = getDrawColor(div, parseInt(0));
               }
        if (typeof chartData[div]["lineSymbol"] === "undefined" || chartData[div]["lineSymbol"] === "Hollow") {  
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        return drillShade;
            }else{
            return "white";
        }}else{
                return colorShad;
        }
            })
            .style("stroke", function(d, i) {
               var colorShad;
    colorShad = getDrawColor(div, parseInt(0));
                return colorShad;
            })
            .style("stroke-width", "2px")
            .attr("onclick", fun)
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .on("mouseover", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, lineMeasure, this,chartData,div1);
                }else{
                show_details(d, columns, lineMeasure, this,div);
                }
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
        }    
// end of line
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
        var rectsize = parseInt(mainWidth/60);
        rectsize=rectsize>10?10:rectsize;
         var legendLength;
                var keys1 =  Object.keys(colorGroup);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }
                else{
                    legendLength=(keys1.length<15?keys1.length:15);
                }

// if(keys1.length>7 && keys1.length<10){
//
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
                 yvalue = parseInt((height / 2)-(legendLength/2)*(height*.06))+25;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+7);
            }
            else if (legendLength>=6){
                yvalue = parseInt((height / 2)-(legendLength/2)*(height*.06))+40;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+17);
            }
            else{
            yvalue = parseInt((height / 2)-(legendLength/2)*(height*.06))+45;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+20);
        }
    }

  if(fontsize1>14){
                  fontsize1 = 13;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
  if(fontsize>16){
                  fontsize = 15;
                }else if(fontsize1<7){
                  fontsize = 9;
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
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
.attr("transform", "translate(" +((mainWidth- margin.left - margin.right+28)*1.07)  + "," + (yLegendvalue+13) + ")")

            .attr("fill", "Black")
        .text(function(d){
    if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
              return colName[1] ;
    }else{
                 return ""
    }
            })
            .attr("svg:title",function(d){
               return colName[1];
           })
}
           for(var m=0;m<(legendLength);m++){ ////length
            if(m!=0){
            yvalue = parseInt(yvalue+height*0.075)
            rectyvalue = parseInt(rectyvalue+height*0.075)
            }
               if(width>600){
               if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (((mainWidth-5)- margin.left - margin.right+30 )*1.07)  + "," + (yvalue*.80 )+ ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
//            .attr("fill", color(i))
            .attr("fill", function(){
                return colorGroup[keys1[m]];
            })
               }
               }else{
            if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (((mainWidth-15)- margin.left - margin.right+30 )*1.07)  + "," + (yvalue*.80 )+ ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
//            .attr("fill", color(i))
            .attr("fill", function(){
                return colorGroup[keys1[m]];
            })
               } 
               }
                   if(width>600){
             svg.append("g")
            .append("text")
            .attr("transform", "translate(" +(((mainWidth-5)- margin.left - margin.right+47)*1.07)  + "," + (yvalue+12)*.80 + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  colorGroup[keys1[m]];
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys1[m];
            } )
          .text(function(){

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
               return keys1[m];
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
           }else{
             svg.append("g")
            .append("text")
            .attr("transform", "translate(" +(((mainWidth-13)- margin.left - margin.right+47)*1.07)  + "," + (yvalue+12)*.80 + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  colorGroup[keys1[m]];
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys1[m];
            } )
          .text(function(){

                if(keys1[m]!="undefined"){
if(keys1[m].length>13){
                    return keys1[m].substring(0, 12)+"...";
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
        var rectsize = parseInt(mainWidth/60);
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
  if(fontsize1>14){
                  fontsize1 = 13;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
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
            var startX=widthvalue;
            yvalue*=1.05;
var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/14;
 
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
            .attr("transform", "translate(" + (widthvalue-15)  + "," + (yvalue-10) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
              .attr("fill", function(){
                return colorGroup[keys1[m]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthvalue +5) + "," + yvalue + ")")
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
              step+=(keys1[m].length*6.5)
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

function buildComboHorizontal(div, data,columns, measureArray,divWidth,divHeight){
var chartData = JSON.parse(parent.$("#chartData").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewOvName =JSON.parse(parent.$("#viewby").val());
    parent.$("#driver").val('');
    var column2 = [];
    var column2Id = [];
    var dimensions2 = chartData[div]["dimensions"];
    for(var i in chartData[div]["dimensions"]){
        column2.push(viewOvName[viewOvIds.indexOf(dimensions2[i])]); 
        column2Id.push(viewOvIds[viewOvIds.indexOf(dimensions2[i])]); 
    }
    chartData[div]["viewBys"] = column2[0];
    chartData[div]["viewIds"] = column2Id[0];
    parent.$("#chartData").val(JSON.stringify(chartData));

    var htmlvar = ""
    htmlvar += "<div id='comboHorizontal_"+div+"' style='float:left;width:"+divWidth+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.33+"px;height:"+divHeight*.98+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='border-left:1px solid #d1d1d1;float:left;width:"+divWidth*.33+"px;height:"+divHeight*.98+"px'></div>";
    htmlvar += "<div id='Top3Div_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.33+"px;height:"+divHeight*.98+"px'></div>";
    htmlvar += "</div>"

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    var columnMeasures = [];
//    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
//            columnPieMeasureValues.push(KPIresult[i])
        }
    }
    var viewList1 = [];
    var viewIdList1 = [];
    try{
        viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
        viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);
    }catch(e){
        viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
        viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);  
    }
    chartData[div]["viewBys"] = viewList1;
    chartData[div]["viewIds"] = viewIdList1;
    chartData[div]["comboType"] = "Drill1";
//    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
            if(typeof comboHBar["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
             comboHBar["Top1Div_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="Top1Div_"+div){
                 data3 = JSON.parse(comboHBar["Top1Div_"+div]);
             }
            parent.$("#driver").val('');
            buildHorizontalBar("Top1Div_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.33, divHeight*.98,"Top1Div_"+div)
var appendDiv = "";
if(div.indexOf("Top1Div_")!=-1){
appendDiv = div;
var splitDiv = div.split("_");
div = splitDiv[1];
}else{
appendDiv = div;
}
        }
    });
var viewList2 = [];
var viewIdList2 = [];
try{
    viewList2.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
    viewIdList2.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);
}catch(e){
    viewList2.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
    viewIdList2.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);  
        }
chartData[div]["viewBys"] = viewList2;
chartData[div]["viewIds"] = viewIdList2;
chartData[div]["comboType"] = "Drill1";
//chartData[div]["records"]='3';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
        if(typeof comboHBar["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        comboHBar["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(comboHBar["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildHorizontalBar("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.33, divHeight*.98,"Top2Div_"+div)
   }
});
var viewList = [];
var viewIdList = [];
try{
    viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[2])]);
    viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[2])]);
}catch(e){
    viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
    viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);  
                }
chartData[div]["viewBys"] = viewList;
chartData[div]["viewIds"] = viewIdList;
chartData[div]["comboType"] = "Drill2";
//chartData[div]["records"]='3';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
        if(typeof comboHBar["Top3Div_"+div]==="undefined" || window.drillID!=="Top3Div_"+div)
        comboHBar["Top3Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top3Div_"+div){
                 data1 = JSON.parse(comboHBar["Top3Div_"+div]);
    }
            parent.$("#driver").val('');
        buildHorizontalBar("Top3Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.33, divHeight*.98,"Top3Div_"+div)
    }
})
}




/*Added by Ashutosh*/
function buildComboMeasureHBar(div, data, columns, measureArray, divWidth, divHeight){
var chartData = JSON.parse(parent.$("#chartData").val());
var measureDiv={};
    measureDiv.count=0;
    measureDiv.Width=[];
    measureDiv.Height=[];
var htmlvar = ""
    htmlvar += "<div id='ComboMeasureHDiv_"+div+"' style='float:left;width:"+divWidth+"px;height:"+divHeight+"px'>";
    for(var i=0; i<measureArray.length;i++){
        if(measureArray.length<=3){
            //    alert(divWidth/measureArray.length);
            if(divWidth>divHeight){
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth/measureArray.length+"px;height:"+divHeight+"px'></div>";
                measureDiv.Width.push(divWidth*.98/measureArray.length);
                measureDiv.Height.push(divHeight);
            }else{
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth+"px;height:"+divHeight/measureArray.length+"px'></div>";    
                measureDiv.Width.push(divWidth);
                measureDiv.Height.push(divHeight/measureArray.length);
            }
        }else if(measureArray.length<=5){
            if(i<3){
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth/3+"px;height:"+divHeight/2+"px;margin-top:0%'></div>";
                measureDiv.Width.push((divWidth/3)+100);
                measureDiv.Height.push((divHeight/2)+80);
                measureDiv.count++;
                }else if(i<=5){
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth/(measureArray.length-measureDiv.count)+"px;height:"+divHeight/2+"px;margin-top:0%'></div>";    
                measureDiv.Width.push((divWidth/(measureArray.length-measureDiv.count))+250);
                measureDiv.Height.push((divHeight/2)+80);
                }
        }else if(measureArray.length<=6){
            if(divWidth>divHeight){
                if(i<3){
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth/3+"px;height:"+divHeight/2+"px;margin-top:0%'></div>";
                measureDiv.Width.push((divWidth/3)+100);
                measureDiv.Height.push((divHeight/2)+50);
                measureDiv.count++;
                }else if(i<6){
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth/(measureArray.length-measureDiv.count)+"px;height:"+divHeight/2+"px;margin-top:0%'></div>";    
                measureDiv.Width.push((divWidth/(measureArray.length-measureDiv.count))+100);
                measureDiv.Height.push((divHeight/2)+50);
                }
            }else{
                htmlvar += "<div id='MeasureHDiv_"+i+"' style='float:left;width:"+divWidth+"px;height:"+divHeight/measureArray.length+"px'></div>";    
                measureDiv.Width.push(divWidth);
                measureDiv.Height.push(divHeight/measureArray.length);
               }
             
        }
    }
//    alert(htmlvar);

//    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
//    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
//    htmlvar += "<div id='USCitydiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    htmlvar += "</div>"
//    $("#ComboMeasureHDiv_"+div).html("");
    $("#"+div).html(htmlvar);
     for(var k=0; k<measureArray.length;k++){
//        if(sortAllMeasures==='Yes'){
//            var swapped;
//            do {
//                swapped = false;
//                for (var i = 0; i < data.length - 1; i++) {
//                    if (parseInt((data[i])[measureArray[k].split()]) < parseInt((data[i+1])[measureArray[k].split()])) {
//                        var temp = data[i];
//                        data[i] = data[i + 1];
//                        data[i + 1] = temp;
//                        swapped = true;
//                    }
//                }
//            } while (swapped);
//        }
    
//         console.log("DATA2"+JSON.stringify(data));
         buildComboHorizontalBar(div,data, columns, measureArray[k].split(),measureDiv.Width[k],measureDiv.Height[k],"MeasureHDiv_"+k);
//         alert(div+":"+data+":"+columns+":"+measureArray[k]+":"+measureDiv.Width[k]+":"+measureDiv.Height[k]+":"+"MeasureHDiv_"+k);
     }
}

function buildComboHorizontalBar(div,data, columns, measureArray,width,height,measureHDiv){
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["tableWithSymbol"]!=='undefined' && chartData[div]["tableWithSymbol"]==='Y' && columns.length>1){
        var tempData=[];
        for(var i in data){
            var tempMap={};
            var viewByValue='';
            for(var j=0;j<(columns.length<2?columns.length:2);j++){
                viewByValue+=data[i][columns[j]];
                if(j==0){
                    viewByValue+="-";
                }
            }
            tempMap[columns[0]]=viewByValue;
            for(var j in measureArray){
                tempMap[measureArray[j]]=data[i][measureArray[j]];
            }
            tempData.push(tempMap);
        }
        data=tempData;
    }
  var color = d3.scale.category10();
    var measure = measureArray[0];
    var chartMap = {};
    var divWid = width-10;
    var divHgt = height;
    var measArr = [];
    var fromoneview=parent.$("#fromoneview").val();
    var dashletid;
    var div1=parent.$("#chartname").val()
    if(fromoneview!='null'&& fromoneview=='true'){
        dashletid=div;
        div=div1;
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
	var colIds= [];
     colIds = chartData[div]["viewIds"];
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=15;
    }
    var customTicks = 5;
    if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
        customTicks = chartData[div]["yaxisrange"]["axisTicks"];
    }
    if(fromoneview!='null'&& fromoneview=='true'){
    }else{
        if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
            autoRounding = columnMap[measure]["rounding"];
        } else {
            autoRounding = "1d";
        }
    }
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")"; //ss
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        height-=20
    }
    var prop = graphProp(div);
    var yAxis;
    
  if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    var margin = {top: 5,right: 15,bottom: 10,left: 25};
   }else{
    margin = {top: 5, right: 20, bottom: 10,left: 90 };
   }
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
    if(divWid > 590){
     if((measureHDiv.split("_"))[1]==="0"){
        var y = d3.scale.linear()
    .rangeRound([0, divWid*.90], .1, .1);
     }
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.90+20], .1, .1);
    }else{
        if((measureHDiv.split("_"))[1]==="0"){
            var y = d3.scale.linear()
    .rangeRound([0, divWid*.70], .1, .1); 
        }
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.70+80], .1, .1);  
    }
    }else{     
      if(divWid > 590){
        if((measureHDiv.split("_"))[1]==="0"){
        var y = d3.scale.linear()
    .rangeRound([0, divWid*.80], .1, .1);
        } 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.90+20], .1, .1);
    }else{
        if((measureHDiv.split("_"))[1]==="0"){
            var y = d3.scale.linear()
    .rangeRound([0, divWid*.70], .1, .1);
        }
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.70+80], .1, .1);  
    } 
    }
}else{
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
    if(divWid > 590){ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.78], .1, .1);
    }else{ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.68], .1, .1);  
    }
}else{ 
    if(divWid > 590){ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.78], .1, .1);
    }else{ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.68], .1, .1);  
    }
    }
}  
   if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){ 
   var x = d3.scale.ordinal()
    .rangeRoundBands([0, (divHgt*.85)-offset], .1);
}else{
     x = d3.scale.ordinal()
    .rangeRoundBands([0, (divHgt*.85)-offset], .1);
  }  //end  by mayank sh. for display properties
    
    if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
       make_y_axis = function() {
            return d3.svg.gridaxis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
        make_x_axis = function() {
            return d3.svg.gridaxis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
    }else{
        make_y_axis = function() {
            return d3.svg.axis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
        make_x_axis = function() {
            return d3.svg.axis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
    }
    if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
       var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }else{
       var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d) {
            return d;
        });
    } else {
        yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d, i) {
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                if(yAxisFormat==""){
                    return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                } else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
            }else {
                return "";
            }
        });
    }
    var yAxis1 = d3.svg.axis1()
    .scale(y)
    .tickFormat(function(d, i) {
        measArr.push(d);
        return "";
    });
    
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    var localColorMap = {};
    if(typeof measureHDiv!=="undefined"&&measureHDiv!=="undefined"){
    var svg = d3.select("#"+measureHDiv)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt-20)+" ")
    .classed("svg-content-responsive", true)
    .style("border-right","1px dotted #6F6E6E")
    .style("border-bottom","1px dotted #6F6E6E")
    .append("svg:g")
    .attr("transform", function(d){
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
//    return "translate(" + margin.left*.76 + "," + (margin.top+5) + ")";
            if((measureHDiv.split("_"))[1]==="0" || (measureHDiv.split("_"))[1]==="3"){
//                    return "translate(" + margin.left*.76 + "," + (margin.top+5) + ")";
                    return "translate(" + margin.left*.50 + "," + (margin.top+5) + ")";
                }else{
                    return "translate(" + 0 + "," + (margin.top+5) + ")";
                }
}else{
    return "translate(" + margin.left*.50 + "," + (margin.top+5) + ")";
    }
    })    
    }else{
        var svg = d3.select("#"+div)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt-20)+" ")
    .classed("svg-content-responsive", true)
    .append("svg:g")
    .attr("transform", function(d){
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    return "translate(" + margin.left*.76 + "," + (margin.top+5) + ")";
}else{
    return "translate(" + margin.left*.50 + "," + (margin.top+5) + ")";
    }
    })
    }
            
    var gradient = svg.append("svg:defs")
    .selectAll("linearGradient")
    .data(measureArray)
    .enter()
    .append("svg:linearGradient")
    .attr("id", function(d) {
    })
    .attr("x1", "0%")
    .attr("y1", "30%")
    .attr("x2", "50%")
    .attr("y2", "30%")
    .attr("spreadMethod", "pad")
    .attr("gradientTransform", "rotate(0)");

    gradient.append("svg:stop")
    .attr("offset", "0%")
    .attr("stop-color", function(d, i) {
        return color(i);
    })
    .attr("stop-opacity", 1);
            
    gradient.append("svg:stop")
    .attr("offset", "12%")
    .attr("stop-color", "rgb(240,240,240)")
    .attr("stop-opacity", 1);
            
    gradient.append("svg:stop")
    .attr("offset", "60%")
    .attr("stop-color", function(d, i) {
        var colorShad;
        return colorShad;
    })
    .attr("stop-opacity", 1);
    
     x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    
    var multiple=0.8;  //add by mayank sharma for nagetiv chart
    if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
        multiple=1;
    }
     var max = 0;
     var minVal = 0;
    if(fromoneview!='null'&& fromoneview=='true'){
  max = maximumValue(data, measure);
  if (data.length > 1) {
        minVal = minimumValue(data, measure) * multiple;
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
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
    max = maximumValue(data, measure);
}}else{
    max = maximumValue(data, measure);
}

 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }else{
    if (data.length > 1) {
        minVal = minimumValue(data, measure) * multiple;
    }}
}else{
          if (data.length > 1) {
        minVal = minimumValue(data, measure) * multiple;
    }else{
        minVal = 0;
       }
    }
    }
    y.domain([parseFloat(minVal), parseFloat(max)]).clamp(true);
    
    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){
   if(typeof chartData[div]["displayXLine"]==="undefined" || chartData[div]["displayXLine"]==="" || chartData[div]["displayXLine"]==="Yes"){  
       svg.append("line")
        .attr("x1",23)
        .attr("y1",(divHgt*.84))
        .attr("x2",function(d){
             if(divWid > 590){
                 return (divWid*.90)
             }else{
                 return (divWid*.80)
             }
        })
        .attr("y2",(divHgt*.84))
        .style("stroke", "#E1DEDE");
    }else{}
    }else{
        svg.append("g")
        .attr("class", "grid11")
        .attr("transform", function(d) {
            return "translate(23," + (divHgt*.84) + ")"
        })
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
            )
    }
    
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}
    else{            
        svg.append("g")
        .attr("class", "y axis")
        .attr("transform", "translate(23," + (divHgt*.86) + ")")
        .call(yAxis)
        .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "0")
        .style("text-anchor", "middle") 
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
        .text(function(d,i) {
            if(typeof displayX !=="undefined" && displayX=="Yes"){
                if(yAxisFormat==""){
                    return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
            }else{
                return "";
            }
        })
        .append("svg:title")
        .text(function(d) {
            return d;
        });
    }
      
        svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(23,"+((-5)+offset)+")")
        .call(xAxis)
        .selectAll('text')
        .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
           
        .text(function(d,i) { //alert((measureHDiv.split("_"))[1]);
           if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else if((measureHDiv.split("_"))[1]==="0" || (measureHDiv.split("_"))[1]==="3"){ 
           return buildXaxisFilter(div,d,i);
          }
        }) 
.attr('transform',function(d,i){
           if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
             return "rotate(0)";
         }else{
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return "rotate(0)";
        }else {
            return "rotate(-30)";
        }
         }
    })
 .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return -2;
        }else {
            return -5;
        }
    })
    .attr('y',function(d,i){
         if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
             return 0;
         }else{
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return 0;
        }else {
            return -5;
        }
         }
    })
        .append("svg:title")
        .text(function(d) {          
            return d;
        });    
        
         if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
        svg.append("line")
        .attr("x1",23)
        .attr("y1",0)//so that the line passes through the y 0
        .attr("x2",23)
        .attr("y2",(divHgt*.86-7))//so that the line passes through the y 0
        .style("stroke", "#E1DEDE");
        svg.append("g")
        .attr("class", ".x axis")
        .attr("transform", "translate(0," + height + ")")
    }
        
        
        var bars = svg.selectAll(".state")
    .data(data)
    .enter()
    .append("rect")
    bars.attr("rx", barRadius)
    .attr("class", "bar")
    .attr("transform", function(d) {
        return "translate(0," + (x(d[columns[0]])-8+offset)+")";
    })
    .attr("y",  "5")
    .attr("x", function(d) {
                if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
                    if((y(0)) > y(d[measure])){
                return y(d[measure])+23;
                    } else{ 
                         return y(0)+23;
                }}else{
                    return "23";
                }
            })
               //bar animation
            .attr("width", 0).transition()
			.duration(1500)
			.delay(function (d, i) {
				return i * 50;
			})  
            .attr("width", function(d) {
                if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
                    return Math.abs(y(0) - y(d[measure]));
                }
                else{
      return y(d[measure]);
                }
    })
 
//    .attr("height", 0)
//			.transition()
//			.duration(2000)
//			.delay(function (d, i) {
//				return i * 50;
//			})
    .attr("height", x.rangeBand()*.90)  
    .attr("fill", function(d,i) {
                 var drilledvalue;
                 var targetLine;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }
                if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]=='N')
                {
                  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure,targetLine);
                  } else {
                            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[columns[0]]) !== -1) {
                        return drillShade;
                    }
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
               targetLine = "";
                return colorfill;
                  }
           }else{
                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure,targetLine);
                  } else {
                      targetLine = "";
                      return color(0);
                  }
                    return color(0);
                }
            })
             .attr("color_value", function(d,i){
                   var drilledvalue;
                 var targetLine;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }
                if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]=='N')
                {
                  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure,targetLine);
                  } else {
                      targetLine = "";

			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			
                  }
           }
                else
                {
                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure,targetLine);
                  } else {
                      targetLine = "";
                      return color(0);
                  }
                }
             })
   
    .attr("id", function(d, i) {
        return d[columns[0]] + ":" + d[measure];
    })
    .attr("onclick", fun)
    .attr("index_value", function(d, i) {
        return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })

    .attr("class", function(d, i) {
        return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
    })
    bars.on("mouseover", function(d, i) {
                prevColor = color(i);
if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    show_details(d, columns, measureArray, this,div);
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
                hide_details(d, i, this);
            });
            
             var sum = d3.sum(data, function(d) {
       return d[measureArray[0]];
    });
    
    // add by mayank sharma for show table  
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{   
 svg.append("text")
            .attr("id", "viewBylbl")
               .attr("class", "valueLabel")
              .attr("text-anchor", "middle")
            .attr("transform", function(d) {
                    var yValue = "15";
                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return "translate("+divWid*.925+", " + (yValue) + ")";      
                     }else{
                          if(divWid < 600){
                     return "translate("+divWid*.847+", " + (yValue) + ")";      
                     }else{
                        return "translate("+divWid*.90+", " + (yValue) + ")"; 
                     }
                     }
                })
            .attr("fill", "#544F4F")
            .text(function(d){
              var noOfRecords=data.length;
              if (!( noOfRecords <= 35)) {
                          return "";
                      }else{
             return measureArray[0]
              }
            });
 }
//            if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{   
//         bar=svg.selectAll("rect1")
//          .data(data)
//          .enter()
//                .append("text")
//                .attr("transform", function(d) { 
//                    var yValue = (x(d[columns[0]])+(x.rangeBand())/2)+offset; 
//                    var xvalue= (y(d[measure])) < 430 ? y(d[measure]) + 40 : y(d[measure]) + 40; 
//                    
//                    
//                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
//                     return "translate("+divWid*.925+", " + (yValue) + ")";      
//                     }else{
//                          if(divWid < 600){
//                     return "translate("+divWid*.847+", " + (yValue) + ")";      
//                     }else{
//                        return "translate("+divWid*.90+", " + (yValue) + ")"; 
//                     }
//                     }
//                     
//                })
//                .attr("text-anchor", "middle")
//                .attr("index_value", function(d, i) {
//                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("id", function(d) {
//                    return d[columns[0]] + ":" + d[measure];
//                })
//                .style("font-size",function(d, i){
//                 if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
//                 return (chartData[div]["labelFSize"]+"px");
//                }else{
//                 return "10px";
//                }
//               })
//               .attr("fill", function(d,i){
//                    var LabFtColor=[];
//   for(var c in measureArray){
// if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[c]]!='undefined' && chartData[div]["LabFtColor"][measureArray[c]]!='undefined'){
//                LabFtColor= chartData[div]["LabFtColor"][measureArray[c]]                   
//                    }else {
//                        LabFtColor = "#000000";
//                        }
//                    return LabFtColor;   }
// })
//                .text(function(d)
//                { var noOfRecords=data.length;
//                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
//                      if (!( noOfRecords <= 35)) {
//                          return "";
//                      }else{
//                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
//                      }
////                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
////                   if(typeof displayType=="undefined" || displayType==="Absolute"){
////                    }else{
////                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
////                    return percentage.toFixed(1) + "%";
////                }
////                }else {
////                return "";
////                }
//               });
//             }// end by mayank sharma for show table
        
        // add  for labels by mayank sh.
         bar=svg.selectAll("rect1")
          .data(data)
          .enter()
                .append("text")
                .attr("transform", function(d) {
                    var yValue = (x(d[columns[0]])+(x.rangeBand())/2)+offset; 
                    var xvalue= (y(d[measure])) < 430 ? y(d[measure]) + 40 : y(d[measure]) + 40; 
//                     alert(d[measure]+": "+max);
                    
                      
                    if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                          return "translate(" + xvalue/2 + ", " + (yValue) + ")";
                   }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
                     return "translate(8, " + (yValue) + ")";      
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
                     return "translate(40, " + (yValue) + ")";      
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="OnRight-Bar"){
//                    return "translate(" + y(d[measure]) + ", " + (yValue) + ")";
                        if((measureHDiv.split("_"))[1]==="0"){
                        if(parseFloat(d[measure])>=max){ 
                        return "translate(" + (y(d[measure])-20) + ", " + (yValue) + ")";
                        }
                    }
                        return "translate(" + y(d[measure]) + ", " + (yValue) + ")";
                     }
                       if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return "translate("+divWid*.91+", " + (yValue) + ")";      
                     }else{
                          if(divWid < 600){
                     return "translate("+divWid*.80+", " + (yValue) + ")";      
                     }else{
                        return "translate("+divWid*.85+", " + (yValue) + ")"; 
                     }
                     }
                     }else if(typeof chartData[div]["LabelPos"]==="undefined" || chartData[div]["LabelPos"]==="Top"){
                     return "translate(" + xvalue + ", " + (yValue) + ")";
                     }
                })
                .attr("text-anchor", "middle")
                .attr("class", "valueLabel")
                .attr("onclick", fun)
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure];
                })
                .style("font-size",function(d, i){
                 if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
                }else{
                 return "10px";
                }
               })
               .attr("fill", function(d,i){
                    var LabFtColor=[];
   for(var c in measureArray){
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[c]]!='undefined' && chartData[div]["LabFtColor"][measureArray[c]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[c]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   }
 })
                .text(function(d)
                {
                    var noOfRecords=data.length;
                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                   if (!( noOfRecords <= 35)){
                      return "";  
                    }else{
   if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Center"||chartData[div]["LabelPos"]==="Bottom-Bar" ||chartData[div]["LabelPos"]==="OnRight-Bar")){
       if (( percentage <= 5.0)){
           return "";
       }
   }                      
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                   if(typeof displayType=="undefined" || displayType==="Absolute"){
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                    }else{
                    return percentage.toFixed(1) + "%";
                }
                }else {
                return "";
                }
                    }
               })
                .on("mouseover", function(d, i) {
                 if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
               {
                var bar = d3.select(this);
                var indexValue = bar.attr("index_value");
                var barSelector = "." + "bars-Bubble-" + indexValue;
                var selectedBar = d3.selectAll(barSelector);
                selectedBar.style("fill", drillShade);
               }
              if(fromoneview!='null'&& fromoneview=='true'){
                     show_details(d, columns, measureArray, this);
                }else{
                show_details(d, columns, measureArray, this);
                }
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
                });  // end  for labels by mayank sh.
    
    
          if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
        { 
            var font=12;
            font=width/39.2;
            font=font>12?12:font;
            font=font<7?7:font;
            // add by maynk sh. for fontsize
             if(typeof chartData[div]["fontsize"]!=="undefined"){
                      font =  chartData[div]["fontsize"];
             }// end by maynk sh. for fontsize
 
   bar=svg.selectAll("rect1")
  .data(data)
  .enter()
  .append("text")
//  .attr("class", "valueLabel")
                .attr("transform", function(d,i) {
                   var yValue = (x(d[columns[0]])+(x.rangeBand())/2)+offset; 
//                   var yValue = (x(d[columns[0]])+(x.rangeBand()-13));
                
                     return "translate(30, " + (yValue-1) + ")";
                })
                .attr("style", "font-size:"+font+"px")
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure];
                })
                .attr("fill", function(d,i){
                   var AxisNameColor="";
                      if (typeof chartData[div]["AxisNameColor"]!=="undefined"){
          AxisNameColor=chartData[div]["AxisNameColor"];
        } else{
            AxisNameColor="#d8d47d";
        }return AxisNameColor;
                })
                .text(function(d)
                {
                        return d[columns[0]];
                })
        }      
      
//       var target = "";
//              if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//              target = chartData[div]["targetLine"];
//               svg.append("text")
//              .text(target)
//              .attr("x", (width)*0.88)
//              .attr("y", y(parseInt(target))-12)
//               .attr("style","font-size:8px");
//               
//              svg.append("text")
//              .attr("x", (width)*0.85)
//              .attr("y", y(parseInt(target))-5)
//               .attr("style","font-size:8px")
//               .text("("+measureArray[0]+")");
//            } //end by mayank sh.(08/6/15) for targt line
//  var valuelineH = d3.svg.line()
//            .x(function(d,i) {
//               if(i==0){
//               return x(d[columns[0]]) - x(0);
//               }else{
//                   return x(d[columns[0]]) + x(i.length);
//               }
//            })
//            .y(function(d) {
//                if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                target = chartData[div]["targetLine"];
//                return y(parseInt(target));
//                }
//            });
//            
//            if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==''){
// var path =   bars.append("path")
//            .data(data)
//            .attr("d", valuelineH(data))
//            .attr("fill", "transparent")
//            .style("z-index", "9999")
//            .style("stroke-width", "1.5px")
//            .style("stroke", "black");
// var totalLength = path.node().getTotalLength();
//    path
//      .attr("stroke-dasharray", totalLength + " " + totalLength)
//      .attr("stroke-dashoffset", totalLength)
//      .transition()
//        .duration(2000)
//        .ease("linear")
//        .attr("stroke-dashoffset", 0);
//  }
//  
//   var sum = d3.sum(data, function(d) {
//        return d[measureArray[0]];
//    });
//   for(var i=0;i<measureArray.length;i++){
//     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
//     var sum = d3.sum(data, function(d) {
//        return d[measureArray[i]];
//    });
//    var avg = sum/data.length;
//     svg.append("text")
//              .text(addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
//              .attr("x", (width)*0.9)
//              .attr("y", y(parseInt(avg))-5);
//    
//      var Averageline = d3.svg.line()
//            .x(function(d,i) {
//         if(i==0){
//               return x(d[columns[0]]) - x(0);
//               }else{
//                   return x(d[columns[0]]) + x(i.length);
//               }
//            })
//            .y(function(d) {
//                return y(parseInt(avg));   
//           } )
//           
//           var path =   svg.append("path")
//            .data(data)
//            .attr("d", Averageline(data))
//            .attr("fill", "transparent")
//            .attr("x", (width)*0.85)
//            .style("z-index", "9999")
//            .style("stroke-width", "1px")
//            .style("stroke", "black");
//     }  
// }
if(typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]!="" && chartData[div]["displayLegends"]!="Yes"){}
else{ 
var count=0;
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectW=(measureName.length)*7+130;
    }
    else{
    rectW=(measureName.length+columns[0].length)*7+130;
}
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomleft" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectW=30+(len*7)+85;
}
rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=0;  
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17;
}else{ 
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+17;
    }else{
    rectH=10+17+viewByHgt;
}
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=50;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
   rectX=divWid*.65
    }else{  
    rectX=divWid*.55;
    }
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=10;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}
var rectY=-5;
if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)+5;
}
else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)+5;
}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.30);
}
    

var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}else{
    backColor="none";
}
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style", "overflow:scroll")
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, "+border))
            .attr("x", rectX)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}            
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
         if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
         }else{
         svg.append("g")
            .attr("id", "viewBylbl")
               .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:9px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
       }
       }else{
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}else{
         svg.append("g")
            .attr("id", "viewBylbl")
            .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:9px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
       }
       }
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
        var measureName='';
   var offset1=0;
   var offset2=0;
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset1=0;
           }else{
           offset1=(columns[0].length*6.5+20);
           }
           offset2=-24;
       }
       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset2=-24;
           }else{
               offset2=0;
           }
       }
   svg.append("g")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", 'black')
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
            measureName=chartData[div]["measureAlias"][measureArray[0]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[0]);
        }
        var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
                    length=20;
                }else{
                    length=chartData[div]["measureLabelLength"][measureArray[0]];
                }
                if(measureName.length>length){
                    return measureName.substring(0, length)+"..";
                }else {
                    return measureName;
          }
           }).attr("svg:title",function(d){
               return measureName;
           })
           var rectsize;
           if(width<height){
              rectsize = parseInt(width/25);
           }else{
              rectsize = parseInt(height/25);
           }
           rectsize=rectsize>10?10:rectsize;
           svg.append("g")
            .append("rect")
            .attr("x",rectX+offset1+10)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(0)))
              count++
}   
} 


function buildKPIViewDashboard(chartId,data,divwidth,divHght){
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    var measureName = chartDetails["meassures"];
//    var measureIds = chartDetails["meassureIds"];
    var compareList = chartData[chartId]["customTimeComparisons"];
//    var comparedMesure = chartDetails["comparedMeasure"];
    var defaultMeasures = chartDetails["defaultMeasures"];
    var compData = chartDetails["comparedMeasure"];
    var columns = chartDetails["viewBys"];
     var trWidth = divwidth / 8;
     graphProp(chartId);
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
 
    var divHght1=divHght-17;
    var htmlstr = "";
      htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + chartId + "tablediv\"  style=\"height:"+divHght1+"px; width:100%;overflow: hidden;\">";
     htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;overflow-x:hidden;'>";
     
      htmlstr = htmlstr + "<table id=\"chartTableKPI" + chartId + "\" class='table table-stripped table-condensed display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
      htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//htmlstr += "<th width=5></th>"
htmlstr += "<th width=\""+trWidth+"\" style='font-weight:bold;text-align:center'>"+columns[0]+"</th>"
htmlstr += "<th width=\""+trWidth+"\" style='font-weight:bold;text-align:left'>Measures</th>";
 for(var i in compareList){
     
 htmlstr += "<th style='font-weight:bold;text-align:center' >";
 try{
htmlstr += compData[i].replace(measureName[0], "").replace("(","").replace(")","")+ "</th>";
 }
 catch(e){}
 }
htmlstr += "</tr>";
htmlstr += "</thead>";
//for(var k=0;k<defaultMeasures.length;k++){
var rowspan = defaultMeasures.length;
//var map = {};
//data[chartId].forEach(function(d, i) {
//    map[d[columns[0]]] = columns[0];
//});
data[chartId].forEach(function(d, i) {
var count = 0;
var columnName = "";
    for(var k=0;k<defaultMeasures.length;k++){
htmlstr += "<tr>";
var columnValue = d[columns[0]];
if(columnName == "" || columnName!==columnValue){
htmlstr += "<td rowspan='"+rowspan+"' nowrap style='font-weight:bold;text-align:left'>";
    
htmlstr += columnValue+"</td>";
columnName = columnValue;
}
htmlstr += "<td nowrap style='font-weight:bold;text-align:left'>";
var measureName = defaultMeasures[k];
htmlstr += measureName+"</td>";

        for(var m in compareList){
       htmlstr += "<td width=\""+trWidth+"\" style='text-align:center'>";     
        var value = addCommas(numberFormat(d[compData[count]],yAxisFormat,yAxisRounding,chartId));
        htmlstr += value+"</td>";   
        count++;
        }
  htmlstr += "</tr>";      
    }

});

      htmlstr += "</table>";
      htmlstr += "</div></div>";
      
     $("#" + chartId).html(htmlstr); 
        var table = $('#chartTableKPI'+chartId).dataTable({
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": false,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
          "scrollY": ""+divHght *.80+"px"
//          "order": [[ 1, "desc" ]]  
          });
}


function buildHistogram(div,data,columns,measureArray,divWid,divHgt){
//var list = [10,20,10,40,20];
   var chartData = JSON.parse(parent.$("#chartData").val());     
//// Generate a Bates distribution of 10 random variables.
//var values = d3.range(1000).map(d3.random.bates(list));
//alert(JSON.stringify(values))
//// A formatter for counts.
//var formatCount = d3.format(",.0f");
var width = divWid;
var height = divHgt;
  var margin = {
        top: 10, 
        right: 12,  
        bottom: 30, 
        left: 70
    };
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
        margin = {
            top: 10, 
            right: 12,  
            bottom: 30, 
            left: 20
        }; 
}else{
        margin = {
            top: 10, 
            right: 12,  
            bottom: 30, 
            left: 70
        }; 
}
var color = d3.scale.category10();
graphProp(div);
var minVal = minimumValue(data, measureArray[0]);
var maxVal = maximumValue(data,measureArray[0]);
  var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], .1, .1);
//   var y = d3.scale.linear()
//            .range([height, 0]);

// Generate a histogram using twenty uniformly-spaced bins.
//data = [{"name": "Hi", "age":25},
//        {"name": "Hi", "age":10},
//        {"name": "Hi", "age":28},
//        {"name": "Hi", "age":10},
//        {"name": "Hi", "age":25},
//        {"name": "Hi", "age":28},
//        {"name": "Hi", "age":27},
//        {"name": "Hi", "age":28},
//        {"name": "Hi", "age":29},
//        {"name": "Hi", "age":30},
//        {"name": "Hi", "age":30},
//        {"name": "Hi", "age":12},
//        {"name": "Hi", "age":9},
//        {"name": "Hi", "age":8},
//        {"name": "Hi", "age":15},
//        {"name": "Hi", "age":30},
//        {"name": "Hi", "age":12},
//        {"name": "Hi", "age":10},
//        {"name": "Hi", "age":9},
//        {"name": "Hi", "age":8},
//        {"name": "Hi", "age":17},
//        {"name": "Hi", "age":31},
//        {"name": "Hi", "age":19}  
//       ];
var measure1 = measureArray[0];        
var map = data.map(function(d){ return parseFloat(d[measureArray[0]]); })
alert(JSON.stringify(map))
var histogram = d3.layout.histogram()
                  .bins(parseInt(maxVal))(map)

var y = d3.scale.linear()
    .domain([parseFloat(minVal), parseFloat(maxVal)])
    .range([height, 0]);

var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom");
 var offset=0;        
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
       offset=25;
    }   
    var svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom)+" ")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");

var map = data.map(function(i){ return i.age; })

var histogram = d3.layout.histogram()
                  .bins(5)(map)

var width=800; height=500;

//var canvas = d3.select("#"+div).append("svg")
//               .attr("width", width)
//               .attr("heihgt", height)
               //.append("g").attr("transform", "translate(50, 50)")
var bars = svg.selectAll(".bar")
                 .data(histogram)
                 .enter()
        .append("rect")
    .attr("x", function(d){ return d.x*20; })
    .attr("y", 0)
    .attr("width", function(d){ return d.dx*20-10 })
    .attr("height", function(d){ return d.y*20; })
    .attr("fill", "steelblue");
}