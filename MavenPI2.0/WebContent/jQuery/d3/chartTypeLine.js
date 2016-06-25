//Line
//Smooth Line
//Multi Measure Line
//Multi Measure Smooth Line
//Cumulative Line
//StackedBarH-%
//GroupedStackedH-Bar
//GroupedStackedH-Bar%
//GroupedHorizontal-Bar

function buildLine(div, data, columns, measureArray, divWid, divHgt,flag){
 if (typeof data!== 'undefined' && data.length==1) {
return buildBar(div, data, columns, measureArray, divWid, divHgt)
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
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
        divHgt -=15;
    }
    var customTicks = 5;

//var colIds = chartData[div]["viewIds"];
    var autoRounding1;
    if(fromoneview!='null'&& fromoneview=='true'){

      }else{
//           if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
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
    var isDashboard = parent.$("#isDashboard").val();
//   var fun = "drillWithinchart(this.id,\""+div+"\")";

 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
	}else{
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
            left: 50
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .78;
}else{
     margin = {
        top: 20,
        right: 00,
        bottom: botom,
            left: 55
    };
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
        var yAxis = d3.svg.trendaxis()
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
var offset=0;        
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
       offset=15;
    }        
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
var divId=''

if(flag==='tile'){
    divId='openTableTile';
}
else{
    divId=div;
}

    svg = d3.select("#" + divId)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left+28)+" "+(divHgt+40)+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" +(margin.left +15)+ "," + (margin.top+offset) + ")");

    svg.append("g")
    .append("svg:svg")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right);




    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
    svg.append("g")
            .attr("class", "y axis");

 var target = "";
              if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
              target = chartData[div]["targetLine"];
               svg.append("text")
              .text(target)
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(target))-17)
               .attr("style","font-size:8px");
  
             svg.append("text")       
            .attr("x", (width)*0.82)
            .attr("y", y(parseInt(target))-9)
            .attr("style","font-size:8px")
            .text("("+measureArray[0]+")");
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
//                var target = "";
                if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                target = chartData[div]["targetLine"];
                return y(parseInt(target));
                }
            });




    var min1 = [];
    var flag = "";

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
var multiple=0.8;
if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
    multiple=1;
}
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
        }
    }else{
    for (var key in data) {
        min1.push(data[key][measure1]);

    }
    }
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
     if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }else{
        minVal = 0;
       }
}
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
    svg = d3.select("#" + divId).select("g");
     if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){

                      if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
              }
           else{
                   if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
                }
              }
           else{
           flag =  measure1;
           }
          return flag
    });
}

//if(typeof displayX !=="undefined" && displayX == "Yes"){
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
        if(typeof displayX !=="undefined" && displayX == "Yes"){
                return buildXaxisFilter(div,d,i)
                }
            })
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -6;
        }else {
            return -10;
        }
    })
.style('font-size',function(d,i) {
                return font1(div,d,i);
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
            return -3;
        }else {
            return 5;
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
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
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
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
}

    if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
        svg.append("line")
        .attr("x1",0)
        .attr("y1",y(0))//so that the line passes through the y 0
        .attr("x2",width)
        .attr("y2",y(0))//so that the line passes through the y 0
        .style("stroke", "black");
        svg.append("g")
        .attr("class", ".x axis")
        .attr("transform", "translate(0," + height + ")")
    }

   var path = svg.append("path")
            .data(data)
            .attr("d", valueline(data))
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
           // Label Text
           var c=0;
           svg.selectAll("labelText")
             .data(data).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("x", function(d){
                        var len=Object.keys(data).length;
                       c++;
                       if(c==len)
                       {
                            return x(d[columns[0]]) - 32;
                       }
                       else
                       {
                            return x(d[columns[0]]) - 25;
                       }
                   } )
                   .attr("y", function(d){ // add by mayank sharma
if(typeof chartData[div]["LabelPos"]!="undefined" && chartData[div]["LabelPos"]!= "" && chartData[div]["LabelPos"] === "Top"){
                            return   y(d[measure1])-15 ;
                        }else{
                      return y(d[measure1]) + 8;
//                         return y(d[measureArray[1]])+25;
                        }
                   })

      .attr("dy", ".35em")
            .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                        if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                        }
                    }else{
                     var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }   else {return "";}
                })
                .attr("fill", function(d,i){
                    var LabFtColor=[];
   for(var c in measureArray){
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[c]]!='undefined' && chartData[div]["LabFtColor"][measureArray[c]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[c]]                   
                          }else {
                        LabFtColor = "#000000";
                               }
        return LabFtColor;
    }
})
.style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
                });

if (typeof chartData[div]["lineSymbol1"] === "undefined" || chartData[div]["lineSymbol1"] === "Circle") {   // add by mayank sh.         
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
                        radius = "8";
                    }else{
            return radius = "4"
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
                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                show_details(d, columns, measureArray, this,div);
                }
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}else{
    var rectangle = svg.selectAll("rect")
    .data(data)
    .enter().append("rect")
    .attr("width", function(d){
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
            return "10";
        }else{
            return  "8";
        }
    })
    .attr("height", function(d){
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
            return "10";
        }else{
            return  "8";
        }
    })
    .attr("x", function(d) {
        return x(d[columns[0]]);
    })
    .attr("y", function(d) {
        return y(d[measure1])-2;
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
            colorShad =  getDrawColor(div, parseInt(0));
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
        colorShad =  getDrawColor(div, parseInt(0));
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
    colorShad =  getDrawColor(div, parseInt(0));
        return colorShad;
    })
    .style("stroke-width", "2px")
    .attr("onclick", fun)
    .dblTap(function(e,id) {
        drillFunct(id);
    }) 
    .on("mouseover", function(d, i) {
        if(fromoneview!='null'&& fromoneview=='true'){
            show_detailsoneview(d, columns, measureArray, this,chartData,div1);
        }else{
            show_details(d, columns, measureArray, this,div);
        }
    })
    .on("mouseout", function(d, i) {
        hide_details(d, i, this);
    });
} // end by mayank sh.
//   var target="";         
    if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==''){
 var path =   svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
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
           })

     	  var path =   svg.append("path")
            .data(data)
            .attr("d", Averageline(data))
            .attr("fill", "transparent")
            .attr("x", (width)*0.85)
            .style("z-index", "9999")
            .style("stroke-width", "1.5px")
            .style("stroke", "black");
     }   
            }
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
var count=0;
var boxW=width + margin.left + margin.right;
var boxH=divHgt;
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
var rectW=30+(len*7)+85;
rectW = rectW<170?170:rectW; 
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectW=(measureName.length)*7+130;
    }
    else{
    rectW=(measureName.length+columns[0].length)*7+130;
}
}
var viewByHgt=15;
var rectH=10+17+viewByHgt;   // buildline
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17;
}
else{ 
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+17;
    }
    else{
    rectH=10+17+viewByHgt;
}
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=10;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "topright" ){
    rectX=boxW-rectW;
}
else if(chartData[div]["lbPosition"] === 'bottomcenter' || chartData[div]["lbPosition"] === "topcenter" ) {
    rectX=boxW/2-rectW/2;
}

var rectY=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
   rectY=boxH-(boxH*1.03)-20;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.27);
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
if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
             
         }
         else{   
   svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
  }
  }
        else{  
            if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
             
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
        }
        
//for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
         var measureName='';
  var offset1=0;
   var offset2=0;
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset1=0;
           }
           else{
           offset1=(columns[0].length*6.5+20);
           }
           offset2=-24;
       }
       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset2=-24;
           }
           else{
               offset2=0;
           }
       }
   svg.append("g")
            .attr("class", "y axisline123")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", 'black')
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!='' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
            measureName=chartData[div]["measureAlias"][measureArray[0]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[0]);
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
                    length=20;
                }
                else{
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

            .attr("x",rectX+10+offset1)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(0)))
            svg.append("g")
            .append("line")
            .attr("x1",rectX+10+offset1-3)
            .attr("y1",(rectY+12+offset2+viewByHgt+count*15)+(rectsize/2))
            .attr("x2",rectX+10+offset1+rectsize+3)
            .attr("y2",rectY+12+offset2+viewByHgt+count*15+rectsize/2)
            .style("stroke",getDrawColor(div, parseInt(0)))
            .style("stroke-width","2px")
              count++
//   }
   }
   }
   
   function buildMultiMeasureTrLine(div, data, columns, measureArray, divWid, divHgt,flag,divHtmlId) {
var chartData = JSON.parse(parent.$("#chartData").val());
if(chartData[div]["chartType"]==='Trend-KPI'){
    if(typeof chartData[div]["currentMeasures"]!=='undefined'){
        measureArray=chartData[div]["currentMeasures"];
    }
}
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
var chartData = JSON.parse(parent.$("#chartData").val());
     if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
         var len=measureArray.length;
         var transposeMeasureArray=[];
         var transposeData=[];
         for(var i=0; i<len;i++){
             var map={};
             map[columns[0]]=measureArray[i];
             for(var j in data){
                 map[data[j][columns[0]]]=data[j][measureArray[i]]
             }
             transposeData.push(map);
         }
         for(var i in data){
             transposeMeasureArray.push(data[i][columns[0]]);
         }
         measureArray=transposeMeasureArray;
         data=transposeData;
     }
var color = d3.scale.category10();
 var fromoneview=parent.$("#fromoneview").val();
  var widthdr=divWid
    var divHgtdr=divHgt
    if(fromoneview!='null'&& fromoneview=='true'){
       divWid=divWid-80;
    }
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
var regid=dashletid.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
var olap=dashletid.substring(0, 9);
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
            left: 60
    };
    width = divWid; //- margin.left - margin.right

    }else{
          margin = {
        top: 20,
        right: 10,
        bottom: botom,
            left: 60
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
var multiple=0.87; 
 if(fromoneview!='null'&& fromoneview=='true'){
       for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
}
}
 for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
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
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
}
}
}}else{
  for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
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
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var min = parseFloat(minimumValue(data, measureArray[i]))*multiple;
if(i==0)  {

minVal=parseFloat(min);
}


else if(i>0 && ( min<minVal)){
minVal = min;
}
}}
}
}else{
    for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var min = parseFloat(minimumValue(data, measureArray[i]))*multiple;
if(i==0)  {

minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}
    }
}
}

    var x = d3.scale.ordinal().rangePoints([0, width], .2);
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
//
       var divId;
 if(typeof divHtmlId!='undefined' && divHtmlId!==''){
    divId=divHtmlId;
}
  if(chartData[div]["chartType"]==="Influencers-Impact-Analysis"||chartData[div]["chartType"]==="Influencers-Impact-Analysis2"){
    var svg = d3.select("#" + divId)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 80 )+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left-10) + "," + (margin.top+offset-30) + ")");

svg.append("g")
    .append("svg:svg")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right+17.5);
  }
  else if(chartData[div]["chartType"]==="Veraction-Combo1"){
        var svg = d3.select("#" + divId)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 33 )+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left-15) + "," + (margin.top+offset) + ")");

svg.append("g")
    .append("svg:svg")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right+17.5);
  }
            else{
    var svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 33 )+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");

svg.append("g")
    .append("svg:svg")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right+17.5);
  }

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");

 var len = 0;
var yvalue = -(height*.24);
var dyvalue = ".41em";
var count = 0;
var transform = "";
var fontSSize=parseInt(($("#" + div).width())*.002)/2;
if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
     }

if(fontSSize>.5 ){
fontSSize=9;
}else if( fontSSize<=.5 ){
fontSSize=8;
}
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
}
else if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Top")){
    len=width*.05;
    step=width/5;
    yvalue=-20;
}
if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    svg.append("line")
    .attr("x1",0)
    .attr("y1",y(0))//so that the line passes through the y 0
    .attr("x2",width)
    .attr("y2",y(0))//so that the line passes through the y 0
    .style("stroke", "black");
    svg.append("g")
    .attr("class", ".x axis")
    .attr("transform", "translate(0," + height + ")")
}
var labelsArr=[];
for(var i in measureArray){
    if(i==0){
        if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
            continue;
        }
    }else{
        if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
            continue;
        }
    }
    var measureName='';
    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAlias"][measureArray[i]];
    }else{
        measureName=checkMeasureNameForGraph(measureArray[i]);
    }
    labelsArr.push(measureName);
    totalCharacters+=measureName.length;
}
if(typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]!="" && chartData[div]["displayLegends"]!="Yes"){}
else{
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
var boxW=width + margin.left + margin.right;
    var showViewBy=false;
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        showViewBy=false;
    }
    else{
        showViewBy=true;
    }
    var totalCharacters=0;
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
var rectW=boxW-100;
var rectX=10;
var rectH=15;
var i=0;
var currY=-23;
var currX=15;
var rowCount=1;
var maxX=currX;
svg.append("rect")
    .attr("id",div+"_mainRect")
    .attr("x",6)
    .attr("y",currY-15)
    .attr("width",rectW)
    .attr("height",17)
    .attr("fill",backColor)
    .attr("rx",10)
    .attr("ry",10)
    .style("stroke-dasharray", ("3, "+border))
    .style("stroke", "grey");
if(showViewBy){
    svg.append("text")
    .attr("x",currX)
    .attr("y",currY-4)
    .text(columns[0]);
    currX+=(columns[0].length*7);
    currX+=10;
    if(currX>maxX){
        maxX=currX;
    }
}
for(i=0;i<labelsArr.length;i++){
    if(currX+(labelsArr[i].length*7)+20>rectW){
        rowCount++;
        currY+=14;
        currX=15;
    }
    svg.append("rect")
    .attr("x",currX)
    .attr("y",currY-12)
    .attr("width",8)
    .attr("height",8)
    .style("fill",function(){
                return getDrawColor(div, parseInt(i))
                });
    svg.append("g")
            .append("line")
            .attr("x1",currX-3)
            .attr("y1",(currY-12)+(8/2))
            .attr("x2",currX+8+3)
            .attr("y2",(currY-12)+(8/2))
            .style("stroke",function(){
                return getDrawColor(div, parseInt(i))
                })
            .style("stroke-width","2px")            
    svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+i).append("text")
    .text(function(){
        var length=0;
        if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
            length=80;
        }
        else{
            length=chartData[div]["measureLabelLength"][measureArray[i]];
        }
        if(labelsArr[i].length>length){
            return labelsArr[i].substring(0, length)+"..";
        }else {
            return labelsArr[i];
        }        
    })
    .attr("id",measureArray[i])
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
    .attr("x",currX+12)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+(17*measureArray.length);
    }
    else{
    rectH=10+(17*measureArray.length)+viewByHgt;
}
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
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, ")+border)
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
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
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        viewByHgt=0;    
    }
        var measureName2='';
   svg.append("g")
            .attr("class", "y axis")
            .append("text")
            .attr("id", measureArray[i])
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
            .on("mouseover",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);

                selectedBar.style("fill", drillShade);
            })
            .on("mouseout",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);
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
            .append("rect")

            .attr("x",rectX+10)
            .attr("y",(rectY+12+viewByHgt+count*15))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
            svg.append("g")
            .append("line")
            .attr("x1",rectX+10-3)
            .attr("y1",(rectY+12+viewByHgt+count*15)+(rectsize/2))
            .attr("x2",rectX+10+rectsize+3)
            .attr("y2",(rectY+12+viewByHgt+count*15)+(rectsize/2))
            .style("stroke",function(){
                return getDrawColor(div, parseInt(i))
                })
            .style("stroke-width","2px")
              count++
   }
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
    svg = d3.select("#" + div).select("g");
//    Added by shivam
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
           flag =  '';
           var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
                    length=20;
           }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[0]];
                }
                if(labelsArr[0].length>length){
                    flag= labelsArr[0].substring(0, length)+"..";
                }else {
                    flag= labelsArr[0];
          }
           }
          return flag
             });
             
} 
             if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     }
//if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{


    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
        if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i)
                }
            })
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return -10;
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
            return 2;
        }else {
            return 5;
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

    var colorArr = [];
    colorMap = {};
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
                        valueLen=addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div)).length;
                        return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
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
     if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
        if(i==0){
        if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
        continue;
    }}else{
    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
        continue;
    }}      
    }
        j = i; //end by mayank sh.
        var path=svg.append("path")
                .data(data)
                .attr("d", valueline(data))
                .attr("fill", getDrawColor(div, parseInt(i)))
                 .style("stroke-width",function(d){
               if(chartData[div]["chartType"]==="Influencers-Impact-Analysis" ||chartData[div]["chartType"]==="Influencers-Impact-Analysis2"|| chartData[div]["chartType"]==="Veraction-Combo1")
               {
                return "1px"   
               }else{
                   return "3px"
               } 
            })

                 .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,2";
        }
        else{
            return "1,0";
        }} // for dash line by mynk sh.
            ) .style("stroke", getDrawColor(div, parseInt(i)));
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
                   } )
                   .attr("y", function(d){  // add by mayank sharma
                        if (typeof chartData[div]["dLabelDisplay"]!= "undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]]=== "OnBottom") {
                             return y(d[measureArray[i]])+12;
                        }else{
                             return y(d[measureArray[i]])-12;
                        }
                    })
                   .attr("onclick", hide)

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
       .attr("fill", function(d,i){
                    var LabFtColor="";
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[i]]!='undefined' && chartData[div]["LabFtColor"][measureArray[i]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[i]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   
 });


      if (typeof chartData[div]["lineSymbol1"] === "undefined" || chartData[div]["lineSymbol1"] === "Circle") {   // add by mayank sh.         
       var blueCircles = svg.selectAll("dot")
                .data(data)
                .enter()
                .append("circle")
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
                .attr("class",function(d){
                    return "bars-Bubble-index-" + measureArray[i].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
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
                   colorShad = getDrawColor(div, parseInt(i));
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
                .style("stroke", getDrawColor(div, parseInt(i)))
                .attr("color_value",function(d,i){
                    var colorShad;
                    var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
        }else{
                        colorShad = getDrawColor(div, parseInt(i));
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
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]] + "#"+measureArray[i];
                })
                .attr("onclick", fun)
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
                    msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(measureValue);
                }
            else{
                 msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
            }
                var content = "";
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
                    content += "<span class=\"name\"><font color='blue'><b> " + columnName + "<b></font></span><br/>";
                }
                else{
                }
////                chartData[div]["tooltipType"][measureArray[i]]="Default";
//                 show_details(d, columns, measureArray, this,div);
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });

}else{ 
    var rectangle =svg.selectAll(".state")  // add by mayank sharma
    .data(data)
    .enter()
    .append("rect")
    .attr("class", "bar")
    .attr("width", function(d){
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
            return "10";
        }else{
            return  "8";
        }
            })
    .attr("height", function(d){
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
            return "10";
        }else{
            return  "8";
        }
    })
    .attr("x", function(d) {
        return x(d[columns[0]]);
    })
    .attr("y", function(d) {
        return y(d[measureArray[i]])-3;
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
                   colorShad = getDrawColor(div, parseInt(i));
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
                .style("stroke", getDrawColor(div, parseInt(i)))
                .attr("color_value",function(d,i){
                    var colorShad;
                    var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                        colorShad = getDrawColor(div, parseInt(i));
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
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]] + "#"+measureArray[i];
                })
                .attr("onclick", fun)
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
                    msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(measureValue);
                }
            else{
                 msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
            }
                var content = "";
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
                    content += "<span class=\"name\"><font color='blue'><b> " + columnName + "<b></font></span><br/>";
                }
                else{
                }
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event); 
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
} // end by mayank sh.
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
function buildSmoothLine(div, data, columns, measureArray, divWid, divHgt,appenddiv1) {
    
     var appendDiv = "";
if(div.indexOf("SmoothLDiv_")!=-1||div.indexOf("TrDivTop3_")!=-1){
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
if (typeof data!== 'undefined' && data.length==1) {
return buildBar(div, data, columns, measureArray, divWid, divHgt)
}
var color = d3.scale.category10();
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
    var chartMap = {};
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
        var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
        divHgt -=15;
    }
    var customTicks = 5;
    var autoRounding1;
    if(fromoneview!='null'&& fromoneview=='true'){
      }else{
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
      }
    var isDashboard = parent.$("#isDashboard").val();
    //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
	}else{ 
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
var olap=div.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
    }
    var botom = 70;
    var j = 0;
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
            left: 50
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .78;
}else{
     margin = {
        top: 20,
        right: 00,
        bottom: botom,
            left: 50
    };
    width = parseFloat(divWid), //- margin.left - margin.right
            height = parseFloat(divHgt)* .75;
    }
var multiple=.87;
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
//    if(chartData[div]["chartType"]==="Transportation-Spend-Variance"){
//       var x = d3.scale.ordinal()
//    .rangeRoundBands([0, width], .50);
//    }  
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1)*multiple;

    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
//    alert("minvalue="+parseFloat(minVal))
//     alert("maxvalue="+parseFloat(max))

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
var offset=0;        
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
       offset=15;
    }        
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}

if(chartData[div]["chartType"]==="Score-vs-Targets"){
    svg = d3.select("#" + appendDiv)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left+28)+" "+(divHgt*1.3)+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left*.96) + "," + ((margin.top+offset)*.25) + ")");
}
else if(chartData[div]["chartType"]==="Transportation-Spend-Variance"){
    svg = d3.select("#" + appendDiv)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left+28)+" "+(divHgt*1.26)+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left*.96) + "," + ((margin.top+offset)*.4) + ")");
}else{
    svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left+28)+" "+(divHgt+40)+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left+15) + "," + (margin.top+offset) + ")");
    }
    svg.append("g")
    .append("svg:svg")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right);

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
    svg.append("g")
            .attr("class", "y axis");
 var target = "";
              if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
              target = chartData[div]["targetLine"];
               svg.append("text")
              .text(target)
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(target))-18)

               svg.append("text")
              .attr("x", (width)*0.82)
              .attr("y", y(parseInt(target))-9)
              .attr("style","font-size:8px")
              .text("("+measureArray[0]+")");
            } //end by mayank sh.(08/6/15) for targt line
 
    if(chartData[div]["chartType"]==="Transportation-Spend-Variance"){
           svg.append("rect")
            .attr("style", "overflow:scroll")
            .attr("x",0)
            .attr("y",height/4)
            .attr("width",width)
            .attr("height", height/2)
//            .attr("height", x.rangeBand())
            .attr("fill","#EDF6E1")
    }
//    alert(x.rangeBand())
  var valuelineH = d3.svg.line()
            .x(function(d,i) {
               if(i==0){
               return x(d[columns[0]]) - x(0);
               }else{
                   return x(d[columns[0]]) + x(i.length);
               }
            })
            .y(function(d) {
//                var target = "";
                if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                target = chartData[div]["targetLine"];
                return y(parseInt(target));
                }
            });


    var min1 = [];
    var flag = "";

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
var multiple=0.87;
if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
    multiple=1;
}
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
     if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }else{
        minVal = 0;
       }
}
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
    svg = d3.select("#" + div).select("g");
     if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){

                      if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
              }
           else{
                   if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
                }
              }
           else{
           flag =  measure1;
           }
          return flag
             });}


    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
        if(typeof displayX !=="undefined" && displayX == "Yes"){
        return buildXaxisFilter(div,d,i);
        }
            })
           .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -6;
        }else {
            return -10;
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
            return -3;
        }else {
            return 5;
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
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
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
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
}
if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
        svg.append("line")
        .attr("x1",0)
        .attr("y1",y(0))//so that the line passes through the y 0
        .attr("x2",width)
        .attr("y2",y(0))//so that the line passes through the y 0
        .style("stroke", "black");
        svg.append("g")
        .attr("class", ".x axis")
        .attr("transform", "translate(0," + height + ")")
    }

   var path = svg.append("path")
            .data(data)
            .attr("d", valueline(data))
            .attr("fill", "transparent")
            .style("z-index", "0")
            .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,3";
        }
        else{
            return "1,0";
        }}
            )  // for dash line by mynk sh.
            .style("stroke", function(d, i) {
               var colorShad;
                   colorShad = getDrawColor(div, parseInt(0));
                return colorShad;
            }) .style("stroke-width",function(d){
               if(chartData[div]["chartType"]==="Score-vs-Targets")
               {
                return "1px"   
               }else{
                   return "3px"
               } 
            });

            var totalLength = path.node().getTotalLength();

    path
      .attr("stroke-dasharray", totalLength + " " + totalLength)
      .attr("stroke-dashoffset", totalLength)
      .transition()
        .duration(2000)
        .ease("linear")
        .attr("stroke-dashoffset", 0);
           var c=0;
           svg.selectAll("labelText")
             .data(data)
             .enter()
             .append("text")
                   .attr("x", function(d){
                        var len=Object.keys(data).length;
                       c++;
                       if(c==len)
                       {
                            return x(d[columns[0]]) - 34;
                       }
                       else
                       {
                            return x(d[columns[0]]) - 25;
                       }
                   } ).attr("y", function(d){ // add by mayank sharma
    if(typeof chartData[div]["LabelPos"]!="undefined" && chartData[div]["LabelPos"]!= "" && chartData[div]["LabelPos"] === "Top"){
        return   y(d[measure1])-15 ;
                        }else{
        return y(d[measure1]) + 8;
                        }
                   })

      .attr("dy", ".35em")
            .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                        if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                        }
                    }else{
                     var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }   else {return "";}
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
              .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
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
                        radius = "8";
                    }else{
                        return radius = "1"
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
                   colorShad = getDrawColor(div, parseInt(0));
               }
                return colorShad;
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
                return colorShad;
            })
            .style("stroke", function(d, i) {
               var colorShad;
                   colorShad = getDrawColor(div, parseInt(0));
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
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

 if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==''){
 var path =   svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
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
            .style("stroke-width", "1.5px")
            .style("stroke", "black");
     }   
            }

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
var count=0;
var boxW=width + margin.left + margin.right;
var boxH=divHgt;
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
var rectW=30+(len*7)+85;
rectW = rectW<170?170:rectW; 
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectW=(measureName.length)*7+130;
    }
    else{
    rectW=(measureName.length+columns[0].length)*7+130;
}
}
var viewByHgt=15;
var rectH=10+17+viewByHgt;   // buildline
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17;
}
else{
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+17;
    }
    else{
    rectH=10+17+viewByHgt;
}
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=10;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "topright" ){
    rectX=boxW-rectW;
}
else if(chartData[div]["lbPosition"] === 'bottomcenter' || chartData[div]["lbPosition"] === "topcenter" ) {
    rectX=boxW/2-rectW/2;
}

var rectY=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
   rectY=boxH-(boxH*1.03)-20;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.27);
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
             
         }
         else{  
      svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
  }}
        else{   
if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
             
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
}    
//for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
         var measureName='';
  var offset1=0;
   var offset2=0;
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset1=0;
           }
           else{
           offset1=(columns[0].length*6.5+20);
           }
           offset2=-24;
       }
       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               offset2=-24;
           }
           else{
               offset2=0;
           }
       }
   svg.append("g")
            .attr("class", "y axisline123")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", 'black')
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!='' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
            measureName=chartData[div]["measureAlias"][measureArray[0]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[0]);
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
                    length=20;
                }
                else{
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

            .attr("x",rectX+10+offset1)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(0)))
            svg.append("g")
            .append("line")
            .attr("x1",rectX+10+offset1-3)
            .attr("y1",(rectY+12+offset2+viewByHgt+count*15)+(rectsize/2))
            .attr("x2",rectX+10+offset1+rectsize+3)
            .attr("y2",rectY+12+offset2+viewByHgt+count*15+rectsize/2)
            .style("stroke",getDrawColor(div, parseInt(0)))
            .style("stroke-width","2px")		
              count++
//   }
   }
}

function buildMultiAxisLine(div, data, columns, measureArray, divWid, divHgt) {
    var measure1 = measureArray[0];
    var measure2;
    if (measureArray.length === 1) {
        measure2 = measureArray[0];
    }
    else {
        measure2 = measureArray[1];
    }
    var autoRounding1;
    var autoRounding2;
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }

    if (columnMap[measure2] !== undefined && columnMap[measure2] !== "undefined" && columnMap[measure2]["rounding"] !== "undefined") {
        autoRounding2 = columnMap[measure2]["rounding"];
    }
    else {
        autoRounding2 = "1d";
    }
    var margin = {
        top: 20,
        right: 80,
        bottom: 55,
        left: 80
    },
    width = divWid, //- margin.left - margin.right
            height = divHgt * .8;
    var fun = "drillWithinchart(this.id)";
    var botom = 150;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
        botom = 70;
    }
    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);
    var max = maximumValue(data, measure1);
    var min1 = minimumValue(data, measure1);
    var min2 = minimumValue(data, measure2);
    var y0 = d3.scale.linear().domain([min1, max]).range([height, 0]);
    max = maximumValue(data, measure2);
    var y1 = d3.scale.linear().domain([min2, max]).range([height, 0]);

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    // create left yAxis
    var yAxisLeft, yAxisRight;
    if (isFormatedMeasure) {
        yAxisLeft = d3.svg.axis().scale(y0).ticks(4).orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition);
                });

        yAxisRight = d3.svg.axis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition);
                });

    } else {
        yAxisLeft = d3.svg.axis().scale(y0).ticks(4).orient("left")
                .tickFormat(function(d, i) {
                    return autoFormating(d, autoRounding1);
                });

        yAxisRight = d3.svg.axis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d, i) {
                    return autoFormating(d, autoRounding2);
                });
    }
    svg = d3.select("body").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    //d3.tsv("data.tsv", type, function(error, data) {
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    //  y0.domain([0, d3.max(data, function(d) { return d[measureArray[0]]; })]);

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
                if (d.length < 13)
                    return d;
                else
                    return d.substring(0, 10) + "..";
            }).attr('transform', 'rotate(-25)');

    svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft)
            .append("text")
            .attr("y", 10)
            .attr("dy", "-2em")
            .attr("fill", "steelblue")
            .style("text-anchor", "middle")
            .style("font-weight", "bold")
            //          .attr("transform", "rotate(-90)")
            .text(measure1);

    svg.append("g")
            .attr("class", "y axis axisRight")
            .attr("transform", "translate(" + (width) + ",0)")
            .call(yAxisRight)
            .append("text")
            .attr("y", 10)
            .attr("dy", "-2em")
            .attr("dx", "2em")
            .attr("fill", "rgb(102, 124, 38)")
            .style("text-anchor", "middle")
            .style("font-weight", "bold")
            //          .attr("transform", "rotate(-90)")
            //          .attr()
            .text(measure2);
    //    bars = svg.selectAll(".bar").data(data).enter();
    var valueline0 = d3.svg.area().interpolate("monotone")
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y0(d[measureArray[0]]);
            });
    var valueline1 = d3.svg.area().interpolate("monotone")
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y1(d[measureArray[1]]);
            });
    var line = d3.svg.line();
    svg.append("path")
            .data(data)
            .attr("d", valueline0(data)).call(yAxisLeft)
            .attr("fill", color(0)).style("stroke-width", "3px")
            .style("stroke", color(0));
    //	 colorArr.push(color(0))
    var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return x(d[columns[0]]);
            })
            .attr("cy", function(d) {
                return y0(d[measureArray[0]]);
            })
            .style("fill", "white")
            .style("stroke", color(0))
            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[0]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    svg.append("path")
            .data(data)
            .attr("d", valueline1(data))
            .attr("fill", color(1)).style("stroke-width", "3px")
            .style("stroke", color(1)).call(yAxisRight);
    //	 colorArr.push(color(0))
    var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return x(d[columns[0]]);
            })
            .attr("cy", function(d) {
                return y1(d[measureArray[1]]);
            })
            .style("fill", "white")
            .style("stroke", color(1))
            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[1]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}

function buildMultiMeasureSmoothLine(div, data, columns, measureArray, divWid, divHgt) {
      
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
var chartData = JSON.parse(parent.$("#chartData").val());
     if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
         var len=measureArray.length;
         var transposeMeasureArray=[];
         var transposeData=[];
         for(var i=0; i<len;i++){
             var map={};
             map[columns[0]]=measureArray[i];
             for(var j in data){
                 map[data[j][columns[0]]]=data[j][measureArray[i]]
             }
             transposeData.push(map);
         }
         for(var i in data){
             transposeMeasureArray.push(data[i][columns[0]]);
         }
         measureArray=transposeMeasureArray;
         data=transposeData;
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
var regid=dashletid.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
var olap=dashletid.substring(0, 9);
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
            left: 60
    };
    width = divWid; //- margin.left - margin.right

    }else{
          margin = {
        top: 20,
        right: 10,
        bottom: botom,
            left: 60
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
var multiple = .87;
 if(fromoneview!='null'&& fromoneview=='true'){
       for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
}
}
 for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
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
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
}
}
}}else{
  for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}
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
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var min = parseFloat(minimumValue(data, measureArray[i]))*multiple;
if(i==0)  {

minVal=parseFloat(min);
}


else if(i>0 && ( min<minVal)){
minVal = min;
}
}}
}
}else{
    for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var min = parseFloat(minimumValue(data, measureArray[i]))*multiple;
if(i==0)  {

minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}
    }
}
}
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
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
    var svg = d3.select("#" + div)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 33 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
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
var labelsArr=[];
for(var i in measureArray){
    if(i==0){
            if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
                continue;
            }
        }else{
            if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
                continue;
            }
        }
    var measureName='';
    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAlias"][measureArray[i]];
    }else{
        measureName=checkMeasureNameForGraph(measureArray[i]);
    }
    labelsArr.push(measureName);
    totalCharacters+=measureName.length;
}
if(typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]!="" && chartData[div]["displayLegends"]!="Yes"){}
else{
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
var boxW=width + margin.left + margin.right;
    var showViewBy=false;
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        showViewBy=false;
    }
    else{
        showViewBy=true;
    }
//    if(showViewBy){
//        labelsArr.push(columns[0]);
//    }
    var totalCharacters=0;
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
var rectW=boxW-100;
var rectX=10;
var rectH=15;
var i=0;
var currY=-23;
var currX=15;
var rowCount=1;
var maxX=currX;
svg.append("rect")
    .attr("id",div+"_mainRect")
    .attr("x",6)
    .attr("y",currY-15)
    .attr("width",rectW)
    .attr("height",17)
    .attr("fill",backColor)
    .attr("rx",10)
    .attr("ry",10)
    .style("stroke-dasharray", ("3, "+border))
    .style("stroke", "grey");
if(showViewBy){
    svg.append("text")
    .attr("x",currX)
    .attr("y",currY-4)
    .text(columns[0]);
    currX+=(columns[0].length*7);
    currX+=10;
    if(currX>maxX){
        maxX=currX;
    }
}
for(i=0;i<labelsArr.length;i++){
    if(currX+(labelsArr[i].length*7)+20>rectW){
        rowCount++;
        currY+=14;
        currX=15;
    }
    svg.append("rect")
    .attr("x",currX)
    .attr("y",currY-12)
    .attr("width",8)
    .attr("height",8)
    .style("fill",function(){
                return getDrawColor(div, parseInt(i))
                });
    svg.append("g")
            .append("line")
            .attr("x1",currX-3)
            .attr("y1",(currY-12)+(8/2))
            .attr("x2",currX+8+3)
            .attr("y2",(currY-12)+(8/2))
            .style("stroke",function(){
                return getDrawColor(div, parseInt(i))
                })            
    svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+i).append("text")
    .text(function(){
        var length=0;
        if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
            length=80;
        }
        else{
            length=chartData[div]["measureLabelLength"][measureArray[i]];
        }
        if(labelsArr[i].length>length){
            return labelsArr[i].substring(0, length)+"..";
        }else {
            return labelsArr[i];
        }
    })
    .attr("x",currX+12)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+(17*measureArray.length);
    }
    else{
    rectH=10+(17*measureArray.length)+viewByHgt;
}
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
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
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        viewByHgt=0;    
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
            svg.append("g")
            .append("line")
            .attr("x1",rectX+10-3)
            .attr("y1",(rectY+12+viewByHgt+count*15)+(rectsize/2))
            .attr("x2",rectX+10+rectsize+3)
            .attr("y2",(rectY+12+viewByHgt+count*15)+(rectsize/2))
            .style("stroke",function(){
                return getDrawColor(div, parseInt(i))
                })
            .style("stroke-width","2px")
              count++
   }
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
     
    svg = d3.select("#" + div).select("g");
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
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
           flag =  '';
           var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
                    length=20;
           }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[0]];
                }
                if(labelsArr[0].length>length){
                    flag= labelsArr[0].substring(0, length)+"..";
                }else {
                    flag= labelsArr[0];
          }
           }
          return flag
             });
    }
             if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     }


    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .text(function(d,i) {
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i)
}
            })
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return -10;
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
            return 2;
        }else {
            return 5;
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
                        valueLen=addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div)).length;
                        return addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
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
    if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    svg.append("line")
    .attr("x1",0)
    .attr("y1",y(0))//so that the line passes through the y 0
    .attr("x2",width)
    .attr("y2",y(0))//so that the line passes through the y 0
    .style("stroke", "black");
    svg.append("g")
    .attr("class", ".x axis")
    .attr("transform", "translate(0," + height + ")")
}
   //add by mayank sh. for by default display one measure on chart
    for (var i = 0; i < measureArray.length; i++) {
     if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
        if(i==0){
        if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
        continue;
    }}else{
    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
        continue;
    }}      
    }
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
                   } )
                   .attr("y", function(d){   // add by mayank sharma
                        if (typeof chartData[div]["dLabelDisplay"]!= "undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]]=== "OnBottom") {
                             return y(d[measureArray[i]])+12;
                        }else{
                             return y(d[measureArray[i]])-12;
                        }
                    })
                    .attr("onclick", hide)

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

       .attr("fill", function(d,i){
                    var LabFtColor="";
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[i]]!='undefined' && chartData[div]["LabFtColor"][measureArray[i]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[i]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   
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
                        return radius = "1";
                    }
                    return radius;
            }) 
                .attr("cx", function(d) {
                    return x(d[columns[0]]);
                })
                .attr("cy", function(d) {
                    return y(d[measureArray[i]]);
                })
//                .style("fill", getDrawColor(div, parseInt(i)))
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
//                        if(d[measure1] < 550){
//                   colorShad = color(1);
//               }else {
                   colorShad = getDrawColor(div, parseInt(i));;
               }

                return colorShad;


            })
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
        
                .on("mouseover", function(d, i) {
                                      var msrData;

var idArr = $(this).attr("id");
        var columnName = idArr.split(":")[0];
        var measureArr = idArr.split(":")[1];
      
        var measureName = measureArr.split("#")[1];
        var measureValue = measureArr.split("#")[0];
        
     
            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
//                    msrData = addCommas(measureValue);
                    msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
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
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
                    content += "<span class=\"name\"><font color='blue'><b> " + columnName + "<b></font></span><br/>";
                }
                else{
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + columnName + "</span><br/>";
                }
                show_details(d, columns, measureArray, this,div);
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + "</span><br/>";//Added by shivam
//                count++;
//                return tooltip.showTooltip(content, d3.event); //m
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
            .style("stroke-width", "1.5px")
            .style("stroke", "black");
    }
            }
    }
    
     function buildLineComp(div, data, columns, measureArray, divWid, divHgt,graphData,axismin,axismax) {
//    divHgt=450;

 if (typeof data!== 'undefined' && data.length==1 && typeof graphData=="undefined") {

return buildBar(div, data, columns, measureArray, divWid, divHgt)

}

var color = d3.scale.category10();
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
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
        divHgt -=15;
    }
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
   var fun = "drillWithinchart(this.id,\""+div+"\")";
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
            left: 60
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .78;
}else{
     margin = {
        top: 20,
        right: 00,
        bottom: botom,
            left: 60
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .75;
    }

    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = axismax;
    var minVal = axismin;
    


    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]).clamp(true);
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

//      var yAxis1 = d3.svg.axis1()
//            .scale(y)
//            .tickFormat(function(d, i) {
//                measArr.push(d);
//                return "";
//            });
    // line 1

//    var valueline = d3.svg.area().interpolate("monotone")
//            .x(function(d) {
//                return x(d[columns[0]]);
//            })
//            .y(function(d) {
//                return y(d[measure1]);
//            });

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
 
var offset=0;        
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
       offset=15;
    }        
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
  var  svg = d3.select("#" + div)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgt+40)+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom+17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");

    svg.append("g")
    .append("svg:svg")
//    .style("margin-left", "3em")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right);



//for (var j=0; j <= width; j=j+150) {
//    svg.append("svg:line")
////    .attr("transform", "translate(0," + width + ",0)")
//        .attr("x1", j)
//        .attr("y1", 0)
//        .attr("x2", j)
//        .attr("y2", height)
//        .style("stroke", "#A69D9D")
//        .style("stroke-width", .5)
//        .style("z-index", "9999");
//};

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
    svg.append("g")
            .attr("class", "y axis");
            // .append("text")
            // .attr("transform", "rotate(-90)")
            // .attr("y", -75)
            // .attr("fill","steelblue")
            // .attr("dy", ".41em")
            // .style("text-anchor", "end");

    var min1 = [];
    var flag = "";

    x.domain(data.map(function(d) {
     
        return d[columns[0]];
    }));
   // var max = 0;
//    if(fromoneview!='null'&& fromoneview=='true'){
//        minVal = minimumValue(data, measure1);
//
//for (var key in data) {
//        min1.push(data[key][measure1]);
//
//    }
//    }else{
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
//if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
//    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
//    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
//}else{
//    for (var key in data) {
//        min1.push(data[key][measure1]);
//
//    }
//}}else{
//    for (var key in data) {
//        min1.push(data[key][measure1]);
//
//    }}
// if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
//  if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="") {
//  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
// }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default"){
//   minVal = 0;
// }
// else{
//    minVal = minimumValue(data, measure1);
//}
//}else{
// minVal = 0;
//// minVal = minimumValue(data, measure1);
//}
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
    svg = d3.select("#" + div).select("g");
     if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){

                      if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
              }
           else{
                   if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       flag = numberFormat(d,yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             flag = addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                        }
                }
              }
           else{
           flag =  measure1;
           }
          return flag
             });}

if(typeof displayX !=="undefined" && displayX == "Yes"){
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i) //line
            })
             .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return -10;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            if(parseInt(i % 2) ==0){
            return  10;
            }else{
            return 30;   
            }
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 2;
        }else {
            return 5;
        }
    })  
    .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return  "";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "rotate(-85)";
        }else {
            return "rotate(-25)";
        }
    })      
    .style('text-anchor',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return  "middle";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "end";
        }else {
            return "end";
        }
    })
    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
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
var legends=[];
for(var m=0;m<graphData.length;m++){
    var data = graphData[m]["comp"+(m+1)];

   var valueline = d3.svg.area()
            .x(function(d) {

                return x(d[columns[0]]);
            })
            .y(function(d) {

                return y(d[measure1]);
            });
    var path=svg.append("path")
                .data(data)
                .attr("d", valueline(data))
                .attr("fill", "transparent")
                 .style("stroke-width", "3px")
                 .style("z-index","1000")
                 .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
            //alert(chartData[div]["lineType"]);
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,2";
        }
        else{
            return "1,0";
        }} // for dash line by mynk sh.s
            ) .style("stroke", getDrawColor(div, parseInt(m)));
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
           // Label Text
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
            .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                        if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                        }
                    }else{
                     var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }   else {return "";}
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
                        return radius = "4"
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
                return d[columns[0]] + ":" + d[measure1]+"#"+m;
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
                   colorShad = color(m);
               }

                return "white";


            })
            .style("stroke", function(d, i) {
               var colorShad;
//               if(d[measure1] < 550){
//                   colorShad = color(1);
//               }else {
                   colorShad = color(m);
//               }

                return colorShad;

            })
            .style("stroke-width", "2px")

            .attr("onclick", fun)

            .on("mouseover", function(d, i) {
                var content = "";
               var  msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div));
               var idArr = $(this).attr("id");
             
        var measureArr = idArr.split(":")[1];
      
        var mValue = measureArr.split("#")[1];
         var timeComparisonArrayNew=[];
     
     timeComparisonArrayNew.push("MOM","MOYM","QOQ", "QOYQ","YOY","MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD");
                var key = (chartData[div]["comparedValue"]);
//                alert(Object.keys(chartData[div]["comparedValue"]))
//                var keyElement = Object.keys(chartData[div]["timeComparison"]);
//             var mapValue1 = [];
                var counter = 0;
//                alert(JSON.stringify(keyElement))
                for(var t in timeComparisonArrayNew){
//                    for(var z in keyElement){
                        
                  if(timeComparisonArrayNew===chartData[div]["timeComparison"][timeComparisonArrayNew[t]]){
                      counter ++;
                    } 
//                  }
                }
                var mapValue = [];
                try{
//                mapValue.push(chartData[div]["comparedValue"][key]);
                mapValue = chartData[div]["comparedValue"][key];
                }
                catch(e){}
               
               if(typeof chartData[div]["timeComparison"] !=="undefined" && chartData[div]["timeComparison"][mValue][Object.keys(chartData[div]["timeComparison"][mValue])].toString() ===Object.keys(chartData[div]["timeComparison"][mValue])[0].toString()){
                 mapValue = chartData[div]["timeComparison"][mValue][Object.keys(chartData[div]["timeComparison"][mValue])].toString();
//          content += "<span style='margin-left:15%;color:blue' class=\"name\">" + mapValue + "</span><br/>";    
          }else{
//              if(mValue!=0){
//              content += "<span style='margin-left:15%;color:blue' class=\"name\"></span><br/>"; 
//              }else{ 
              content += "<span style='margin-left:15%;color:blue' class=\"name\"></span><br/>"; 
          }
//               alert(JSON.stringify(chartData[div]["comparedValue"]))
//               alert(JSON.stringify(chartData[div]["comparedValue"][key]))
//               alert(JSON.stringify(chartData[div]["comparedValue"][key][m-1]))
             
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span style='margin-left:15%;color:blue' class=\"name\">" + mapValue[parseInt(mValue)] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
//                if(fromoneview!='null'&& fromoneview=='true'){
//                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
//                }else{
//                show_details(d, columns, measureArray, this,div);
//                }
//                show_details(d, columns, measureArray, this,div);
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
           })

     	  var path =   svg.append("path")
            .data(data)
            .attr("d", Averageline(data))
            .attr("fill", "transparent")
            .attr("x", (width)*0.85)
            .style("z-index", "9999")
            .style("stroke-width", "1.5px")
            .style("stroke", "black");
     }   
            }
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
var labelsArr=[];
var appliedTimeComp=[];
var labelsArr=[];
    try{
        appliedTimeComp=chartData[div]["timeComparison"];
    }
    catch(e){
    }
    
    for(var i in appliedTimeComp){
        if(appliedTimeComp[i][Object.keys(appliedTimeComp[i])[0]]===Object.keys(appliedTimeComp[i])[0]){
            labelsArr.push(Object.keys(appliedTimeComp[i])[0]);
        }
    }
    var comparedValue=[];
    try{
        comparedValue=chartData[div]["comparedValue"][Object.keys(chartData[div]["comparedValue"])[0]];
    }
    catch(e){}
    for(var i in comparedValue){
        labelsArr.push(comparedValue[i]);
    }   
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            var boxW=(width + margin.left + margin.right);
    var showViewBy=false;
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        showViewBy=false;
    }
    else{
        showViewBy=true;
    }
    
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
var rectW=boxW-100;
var rectX=10;
var rectH=15;
var i=0;
var currY=-13;
var currX=15;
var rowCount=1;
var maxX=currX;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("rect")
    .attr("id",div+"_mainRect")
    .attr("x",6)
    .attr("y",currY-15)
    .attr("width",rectW)
    .attr("height",17)
    .attr("fill",backColor)
    .attr("rx",10)
    .attr("ry",10)
    .style("stroke-dasharray", ("3, "+border))
    .style("stroke", "grey");
}
if(showViewBy){
    svg.append("text")
    .attr("x",currX)
    .attr("y",currY-4)
    .text(columns[0]);
    currX+=(columns[0].length*7);
    currX+=10;
    if(currX>maxX){
        maxX=currX;
    }
}
for(i=0;i<labelsArr.length;i++){
    if(currX+(labelsArr[i].length*7)+20>rectW){
        rowCount++;
        currY+=14;
        currX=15;
    }
    svg.append("rect")
    .attr("x",currX)
    .attr("y",currY-12)
    .attr("width",8)
    .attr("height",8)
    .style("fill",function(){
                return getDrawColor(div, parseInt(i))
                });
    svg.append("text")
    .text(labelsArr[i])
    .attr("x",currX+15)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+25;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
   else 
    {
            var boxW=(width + margin.left + margin.right);
var boxH=(height + margin.top + margin.bottom -15);
//var rectW=150+boxW*0.17;
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
    rectW=boxW-15;
}
else{
    rectW=30+(len1*7)+85;
}

rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=0;  // stackedbar
rectH=10+(17*labelsArr.length);

var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=10;
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

var rectY=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)-25;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)+15;
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
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    offset1=-10;
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
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, "+border))
//            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
            .attr("x", rectX+offset1)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}
 else{           
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
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
}
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    var xStep=rectW/3.3;
    var yStep=15;
        
for(var i=0;i<=measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
         var measureName='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+20+(xStep*(i%3)))
            .attr("y",(rectY+13+(yStep*parseInt(i/3))))
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
            measureName=chartData[div]["measureAlias"][measureArray[i-1]];
            if(measureName==='undefined'){
                measureName=measureArray[i-1];
            }
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i-1]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i-1]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i-1]];
                }  
              
                if(measureName.length>length){
                    return measureName.substring(0, length)+"..";
                }else {
                    return measureName;
          }
           }).attr("svg:title",function(d){
               if(i==0){
                   return columns[0];
               }
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
       if(i!=0){
           svg.append("g")
            .append("rect")
            .attr("x",rectX+3+(xStep*(i%3)))
            .attr("y",(rectY+5+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i-1)))
}  
              count++
   }
}
else{
count=0;
for(var i in  labelsArr){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               viewByHgt=0;
           }
        var measureName='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+25)
            .attr("y",(rectY+20+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
                return labelsArr[i];
            }).attr("svg:title",function(d){
               return labelsArr[i];
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
            .attr("y",(rectY+12+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
              count++
//   }
   }
   }
}
    }
   }
   
// add by mayank sharma
   function buildHBarPercent(div,data, columns, measureArray,wid,hgt) {
    var colorSet = d3.scale.category10();
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
    var measure = measureArray[0];
    var chartMap = {};
    var measArr = [];
    var autoRounding;
    var fromoneview=parent.$("#fromoneview").val();
  var dashletid;
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
div=div1;
     }
   var chartData = JSON.parse(parent.$("#chartData").val());
    var customTicks = 5;
// if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
//     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
// }
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
//  var fun = "drillWithinchart(this.id,\""+div+"\")"; //ss
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
    }else {
        drillWithinchart(id1,div);
    }
            
        }
           
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150;
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        hgt-=20
    }
    var prop = graphProp(div);
    var yAxis;
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    var margin = {top: 25,right: 12,bottom: 10,left: 25 };
   }else{
    var margin = {top: 5, right: 40, bottom: 10,left: 78 };
   }
   
    
    var x = d3.scale.ordinal()
    .rangeRoundBands([0, hgt*.85], .1);
       
    
   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    if(wid > 800){
    var y = d3.scale.linear()
              .rangeRound([0, wid*.84], .1, .1);
              var y1 = d3.scale.linear()
              .rangeRound([0, wid*.83], .1, .1);
    }else
        {
               var y = d3.scale.linear()
              .rangeRound([0, wid*.72], .1, .1); 
               var y1 = d3.scale.linear()
              .rangeRound([0, wid*.71], .1, .1);
        
        }
}else{
     if(wid > 800){
     var y = d3.scale.linear()
             .rangeRound([0, wid*.80], .1, .1);
    var y1 = d3.scale.linear()
             .rangeRound([0, wid*.79], .1, .1);
     }else
         {
              var y = d3.scale.linear()
             .rangeRound([0, wid*.72], .1, .1);
              var y1 = d3.scale.linear()
             .rangeRound([0, wid*.71], .1, .1);        
        }
  } 
            
    if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
        make_x_axis = function() {
            return d3.svg.gridaxis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.gridaxis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }else{
        make_x_axis = function() {
            return d3.svg.axis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.axis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }
    if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
        var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }else{
        var xAxis = d3.svg.axis()
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
        .scale(y1)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d, i) {
            if(typeof displayX !=="undefined" && displayX =="Yes"){
                if(yAxisFormat==""){
                    return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div))+"%";
                } else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div)+"%";;
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
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=20;
    }
    
    var svg = d3.select("#"+div)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(wid+7)+" "+(hgt+3)+" ")
    .classed("svg-content-responsive", true)
    .append("svg:g")
    .attr("transform", function(d){
         if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
            return "translate(" + (30) + "," + (35) + ")";
         }else{
   return "translate(" + (margin.left-6) + "," + (margin.left*.45) + ")";
             
         }
       })
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
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
    
    var tmp = color;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        return key !== columns[0];
    }));
    data.forEach(function(d) {
        var y0 = 0;
		if(typeof d.val!="undefined" && d.val!=""){

}else{
        d.val = tmp.domain().map(function(name) {
            return {
                name: name,
                viewBy: d[columns[0]],
                value: d[name],
                y0: y0,
                y1: y0 += +d[name]
            };
        });
}
		if(typeof d.total!="undefined" && d.total!=""){

}else{
        d.total = d.val[d.val.length - 1].y1;
}
	});
    
    var col = [];
    data.forEach(function(d) {
        col.push(d[columns[0]]);
    });
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
        
    var max = 0;
    var minVal = 0;
    
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}

if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
        if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["axisMax"]!="0" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
            max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
        }else{
            minVal=0;
            max = maximumValue(data, "total");
            svg.call(yAxis1);
            var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
            if(measArr[0]<0){
                minVal = measArr[0] + diffFactor;
            } else{
                minVal = measArr[0] + diffFactor;
                if(measArr[0]>=0 && minVal<0){
                    minVal=0;
                }
            }}
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
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
        if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
            }else{
                minVal = measArr[0] + diffFactor;
                if(measArr[0]>=0 && minVal<0){
                    minVal=0;
                }
            }}
    }else{
        minVal=0;
        max = maximumValue(data, "total");

        svg.call(yAxis1);
        var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
        if(measArr[0]<0){
            minVal = measArr[0] + diffFactor;
        }else{
            minVal = measArr[0] + diffFactor;
            if(measArr[0]>=0 && minVal<0){
                minVal=0;
            }}
    }
    y.domain([parseFloat(minVal), parseFloat(max)]);
    y1.domain([0, 100]);


if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
        svg.append("g")
        .attr("class", "grid11")
        .attr("transform", function(d) {
            return "translate(25," + (hgt*.85) + ")"
        })
        .call(make_y_axis()
            .tickSize(-wid*.48, 0, 0)
            .tickFormat("")
            )
    }

svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(23,-5)")
        .call(xAxis)
        .selectAll('text')
        .style('text-anchor', 'end')  
        .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
        .text(function(d,i) {
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
        
    }else{   
            return buildXaxisFilter(div,d,i);
    }    
        }) 
        .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return "rotate(0)";
        }else {
            return "rotate(-30)";
        }
    })
 .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return -5;
        }else {
            return -5;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return 0;
        }else {
            return -5;
        }
    })
        .append("svg:title")
        .text(function(d) {          
            return d;
        }); 
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}
    else{            
        svg.append("g")
        .attr("class", "y axis")
        .attr("transform", "translate(25," + (hgt*.85) + ")")
        .call(yAxis)
        .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "10")
        .style("text-anchor", "middle") 
        .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
        .attr('transform', 'rotate(-30)')
        .append("svg:title")
        .text(function(d) {
            return d;
        });
    }

    var state = svg.selectAll(".state")
            .data(data)
            .enter().append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
                return "translate(23," + x(d[columns[0]])+")";
            });
var count = 0;
var colIds=[];
var measureSum=[];
for(var i in data){
    measureSum[i]=0;
    for(var j in measureArray){
        measureSum[i] += parseInt(data[i][measureArray[j]]);
    }
}
var maxSum=measureSum[0];
for(var i in measureSum){
    if(measureSum[i]>maxSum){
        maxSum=measureSum[i];
    }
}
var counter=0,counter1=0;
colIds = chartData[div]["viewIds"];
state.selectAll("rect")
.data(function(d) {
    return d.val;
})
.enter().append("rect")
.attr("height",x.rangeBand()*.80)  
.attr("x", function(d,i) {
    var per=d.y0/measureSum[counter]*100;
    per=per.toFixed(0);
    if(i==measureArray.length-1){
        counter++;
    }
    return y(maxSum*per/100);
})
.attr("width", function(d,i) {
    var per=d.y0/measureSum[counter1]*100;
    per=per.toFixed(0);
    var per1=d.y1/measureSum[counter1]*100;
    per1=per1.toFixed(0);
    if(i==measureArray.length-1){
        counter1++;
    }
    return  y(maxSum*per1/100)-y(maxSum*per/100);
}) .attr("y", "-5")
.style("fill", function(d,i) {
    var drilledvalue;
    try {
        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
    } catch (e) {
    }
    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.viewBy) !== -1) {
        return drillShade;
    }
    return getDrawColor(div, parseInt(i));
})
.attr("id", function(d, i) {
    return d.viewBy;
})
.attr("onclick", fun)
.dblTap(function(e,id) {
    drillFunct(id);
}) 
.on("mouseover", function(d, i) {
    var msrData;
    if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
        msrData = addCurrencyType(div, getMeasureId(d["name"])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
    }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
        msrData = addCurrencyType(div, getMeasureId(d["name"])) + addCommas(d.value);
    }else{
        if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[d["name"]]!='undefined'){
            yAxisFormat=numberFormatArr[d["name"]];
        }else{
            yAxisFormat="";
        }
        if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[d["name"]]!='undefined'){
            yAxisRounding=numberRoundingArr[d["name"]];
        }else{
            yAxisRounding="0";
        }
        msrData = addCurrencyType(div, getMeasureId(d["name"])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
    }
    var content = "";
//    content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " by " + d.viewBy + " </span><br/>";//Added by shivam
    content = show_stackDetail(div,d,data,chartData,measureArray,content,i,msrData);
    count++;
    return tooltip.showTooltip(content, d3.event);
})
.on("mouseout", function(d, i) {
    hide_details(d, i, this);
}).attr("index_value", function(d, i) {
    return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
})
.attr("color_value", function(d, i) {
    var drilledvalue;
    try {
        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
    } catch (e) {
    }
    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.viewBy) !== -1) {
        return drillShade;
    }
    return getDrawColor(div, parseInt(i));
})
.attr("class", function(d, i) {
    return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+"-"+div;
});
             var sum =[] ;
                  for(var z=0;z<measureArray.length;z++){
                  var sum1 = d3.sum(data, function(d,i) {
                     return d[measureArray[z]];
                    });
                    sum.push(sum1);
                  }
 counter=0;                 
 counter1=0;
state.selectAll("rect1")
            .data(function(d) {
                return d.val;
            })
            .enter()
            .append("text")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
               return (chartData[div]["labelFSize"]+"px");
          }else{
              return "10px";
              }
            })
           .attr("transform",function(d,i) {
                    var yValue =x(d.x);
                     var startVal=y(d.y0);
                     var per=d.y0/measureSum[counter]*100;
                per=per.toFixed(0);
                yValue = y(maxSum*per/100);
                    per=d.y1/measureSum[counter]*100;
                per=per.toFixed(0);
                
                if(i==measureArray.length-1){
                    counter++;
                }
                startVal = y(maxSum*per/100);
                  if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom"){
                    return "translate(" + (yValue+3) + ","+(x.rangeBand()-7)/2+")";
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop"){
                    return "translate(" + (startVal+3) + ","+(x.rangeBand()-7)/2+")";  // add by mayank sharma
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBar"){
                    return "translate(" + (startVal-27) + ","+(x.rangeBand()-7)/2+")";  // add by mayank sharma
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop-Bar"){
                   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes") {
                        if(wid > 800){ 
                   return "translate(" + (wid*.90)+ ","+(x.rangeBand()-7)/2+")";
                    }else {
                    return "translate(" + (wid*.78)+ ","+(x.rangeBand()-7)/2+")";
               }
                   }
                   if(wid > 800){ 
                   return "translate(" + (wid*.84)+ ","+(x.rangeBand()-7)/2+")";
                    }else {
                    return "translate(" + (wid*.78)+ ","+(x.rangeBand()-7)/2+")";
               }
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom-Bar"){
                       if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
                    if(wid > 800){ 
                        return "translate(" + (-wid*.04) + ","+(x.rangeBand()-7)/2+")";}
                    else{
                       return "translate(" + (-wid*.06) + ","+(x.rangeBand()-7)/2+")"; }
                   }else{
                    return "translate(" + (-wid*.06) + ", "+(x.rangeBand()-7)/2+")";
               }}else{
                     return "translate(" +((yValue+((startVal-yValue)/2))-10)+ ","+(x.rangeBand()-7)/2+")";
               }
                })
            .text(function(d,i){
                var percentageValue=(d.value/measureSum[counter1])*100;
                if(i==measureArray.length-1){
                    counter1++;
                }
                
                  if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[d["name"]]!='undefined'){
                yAxisFormat=numberFormatArr[d["name"]];
        }else{
                yAxisFormat="";
            }
            if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[d["name"]]!='undefined'){
                yAxisRounding=numberRoundingArr[d["name"]];
        }else{
                yAxisRounding="0";
            }
                        
                 if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//                    if(typeof displayType=="undefined" || displayType==="Absolute"){
//                        if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
//                        if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
//                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
////                           return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
//                }else {
//                    return "";
//                                } 
//                               }else {
////                   return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
//                }
//                    }else{
//                       if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
//                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
//                             var percentage = (d.value / parseFloat(sum[i])) * 100;
//                    return percentage.toFixed(1) + "%";
//                }else {
//                    return "";
//                                }
//                            }else {
//                    var percentage1 = (d.value / parseFloat(sum[i])) * 100;
//                    return percentage1.toFixed(1) + "%";
//                            } }
//                        }else {
//                        if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='%-wise'){
                        if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                    var percentage1 = percentageValue;
                    if(percentage1<5){
                        return "";
                    }
                    return percentage1.toFixed(1) + "%";
                        }else {
                                    return "";
                }
                            }else {
                                var percentage1 = percentageValue;
                                if(percentage1<5){
                                    return "";
                                }
                                return percentage1.toFixed(1) + "%";
            }
//                        }
//                        else{
//                            if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
//                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
//                                    return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
//                                }else {
//                                    return "";
//            }
//                            }else {
//                                return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
//                            }} 
//                    }
                            }else {
                    return "";
                }
               })
              .attr("fill", function(d,i){
                    var LabFtColor="";
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[i]]!='undefined' && chartData[div]["LabFtColor"][measureArray[i]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[i]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   
 })
                .append("svg:title")
        .text(function(d) {
            return  d.viewBy;
               });
//for(var i=0;i<measureArray.length;i++){
//     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
//     var sum = d3.sum(data, function(d) {
//        return d[measureArray[i]];
//    });
//    var avg = sum/data.length;
//     svg.append("text")
////              .text(avg)
//               .text(addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
//              .attr("x", (width)*0.85)
//              .attr("y", y(parseInt(avg))-5);
//    
//      var Averageline = d3.svg.line()
//            .x(function(d,i) {
//        return x(d[columns[0]])
//            })
//            .y(function(d) {
//                return y(parseInt(avg));   
//           } )
//           
//     	  var path =   svg.append("path")
//            .data(data)
//            .attr("d", Averageline(data))
//            .attr("fill", "transparent")
//            .attr("x", (width)*0.85)
//            .style("z-index", "9999")
//            .style("stroke-width", "1px")
//            .style("stroke", "black");
//     }    
//            }
// // defineTargetline
//     for(var i=0;i<measureArray.length;i++){   // stackebar targetline
//        var target = "";
//        var labelLen=0,valueLen=0;
//        labelLen=measureArray[i].length;
//        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
//            target = chartData[div]["defineTargetline"][measureArray[i]];
//           svg.append("text")
//            .text(function(d){
//                if(yAxisFormat==""){
//                        valueLen=addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div)).length;
//                        return addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
//                    }
//            else{
//                        valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
//                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
//                }
//            })       
//            .attr("x", (width)*.90)
//            .attr("y", y(parseInt(target))-12)
//            .attr("style","font-size:8px");
//            
//             svg.append("text")       
//            .attr("x", (width-labelLen*5.2))
//            .attr("y", y(parseInt(target))-5)
//            .attr("style","font-size:8px")
//            .text("("+measureArray[i]+")");
//        }
//        var valuelineH = d3.svg.line()
//        .x(function(d,i) {
//            if(i==0){
//                return x(d[columns[0]]);
//            }else{
//                return x(d[columns[0]]) + x(i.length);
//            }
//        })
//        .y(function(d) {
//            if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
//                target = chartData[div]["defineTargetline"][measureArray[i]];
//                return y(parseInt(target));
//            }
//        });
//        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
//            var path = svg.append("path")
//            .data(data)
//            .attr("d", valuelineH(data))
//            .attr("fill", "transparent")
//            .attr("x", (width)*.95)
//            .style("z-index", "9999")
//            .style("stroke-width", "1px")
//            .style("stroke", "black");
//        }
//    }       
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{  
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    var boxW=wid;
    var labelsArr=[],showViewBy=false;
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        showViewBy=false;
    }
    else{
        showViewBy=true;
    }
//    if(showViewBy){
//        labelsArr.push(columns[0]);
//    }
    var totalCharacters=0;
    for(var i in measureArray){
        var measureName='';
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);
        }
        labelsArr.push(measureName);
        totalCharacters+=measureName.length;
    }
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
var rectW=boxW-100;
var rectX=10;
var rectH=15;
var i=0;
var currY=-23;
var currX=15;
var rowCount=1;
var maxX=currX;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("rect")
    .attr("id",div+"_mainRect")
    .attr("x",6)
    .attr("y",currY-7)
    .attr("width",rectW)
    .attr("height",17)
    .attr("fill",backColor)
    .attr("rx",10)
    .attr("ry",10)
    .style("stroke-dasharray", ("3, "+border))
    .style("stroke", "grey");
}
if(showViewBy){
    svg.append("text")
    .attr("x",currX)
    .attr("y",currY+3)
    .text(columns[0]);
    currX+=(columns[0].length*7);
    currX+=10;
    if(currX>maxX){
        maxX=currX;
    }
}
for(i=0;i<labelsArr.length;i++){
    if(currX+(labelsArr[i].length*7)+20>rectW){
        rowCount++;
        currY+=14;
        currX=15;
    }
    svg.append("rect")
    .attr("x",currX)
    .attr("y",currY-4)
    .attr("width",8)
    .attr("height",8)
    .style("fill",function(){
                return getDrawColor(div, parseInt(i))
                });
    svg.append("text")
    .text(function(){
        var length=0;
        if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
            length=80;
        }
        else{
            length=chartData[div]["measureLabelLength"][measureArray[i]];
        }
        if(labelsArr[i].length>length){
            return labelsArr[i].substring(0, length)+"..";
        }else {
            return labelsArr[i];
        }
    })
    .attr("id",measureArray[i])
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"-"+div;
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"-"+div;
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
    .attr("x",currX+12)
    .attr("y",currY+3);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
else{
count=0;           
var boxW=wid;
var boxH=hgt + margin.top + margin.bottom+30;
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
var len=maxMeasure.length;
if(columns[0].length+2>len){
    len=columns[0].length+2;
}
var rectW=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectW=boxW-15;
}
else{
    rectW=30+(len*7)+85
}
rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=0;  // stackedbar
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectH=17*(parseInt((measureArray.length)/3)+1);
}
else{
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+(17*measureArray.length);
    }
    else{
    rectH=10+(17*measureArray.length)+viewByHgt;
}
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=10;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright" ){
    rectX=boxW-rectW-10;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}
var rectY=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)-30;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.27)-(6*measureArray.length);
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
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    offset1=-10;
}
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg
//.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("id",div+"_mainRect")
            .attr("style","margin-right:10")
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, "+border))
//            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
            .attr("x", rectX+offset1)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}
 else{      
if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}
else{
    
      svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+8)
            .attr("style","font-size:10px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);           
}
}
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    var xStep=rectW/3.3;
    var yStep=15;
    
for(var i=0;i<=measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
               var measureName='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+20+(xStep*(i%3)))
            .attr("y",(rectY+13+(yStep*parseInt(i/3))))
            .attr("fill", function(d){
                if(i==0){
                    return 'black';
                }
                else{
                getDrawColor(div, parseInt(i-1))
                }
                })
            .text(function(d){
            if(i==0){
                return columns[0];
            }
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i-1]]!='undefined' && chartData[div]["measureAlias"][measureArray[i-1]]!=''){
            measureName=chartData[div]["measureAlias"][measureArray[i-1]];
            if(measureName==='undefined'){
                measureName=measureArray[i-1];
            }
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i-1]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i-1]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i-1]];
                }
                if(measureName.length>length){
                    return measureName.substring(0, length)+"..";
                }else {
                    return measureName;
          }
           }).attr("svg:title",function(d){
               if(i==0){
                   return columns[0];
               }
               return measureName;
           })
           var rectsize;
           if(wid<hgt){
              rectsize = parseInt(wid/25);
           }
           else{
              rectsize = parseInt(hgt/25);
           }
           rectsize=rectsize>10?10:rectsize;
   if(i!=0){
   svg.append("g")
            .append("rect")
            .attr("x",rectX+3+(xStep*(i%3)))
            .attr("y",(rectY+5+(yStep*parseInt(i/3))))
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
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               viewByHgt=0;
           }
           var rectsize;
           if(wid<hgt){
              rectsize = parseInt(wid/25);
           }
           else{
              rectsize = parseInt(hgt/25);
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
           
               var measureName='';
   svg
   //.append("g")
            .append("text")
            .attr("class", "y axis")
            .attr("id", measureArray[i])
            .on("mouseover",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"-"+div;
                var selectedBar = d3.selectAll(barSelector);

                selectedBar.style("fill", drillShade);
            })
            .on("mouseout",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"-"+div;
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);
            })
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]==='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i]];
                }
                if(measureName.length>length){
                    return measureName.substring(0, length)+"..";
                }else {
                    return measureName;
          }
           }).attr("svg:title",function(d){
               return measureName;
           })

              count++
   }}}
}
//    showLegends1(measureArray, localColorMap, width, svg);
}  // end by mayank sharma

// add by mayank sharma
function buildGroupedstackedHBar(div,data, columns, measureArray,wid,hgt) {
var colorSet = d3.scale.category10();
     var chartData = JSON.parse(parent.$("#chartData").val());
     var mainWidth = wid-10;
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
    var innerRecords=tooltipData[0].length;
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
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=15;
    }
    var prop = graphProp(div);
    var yAxis;
//    if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes") {
//    var yAxis;
//    var margin = {
//        top: 10,
//        right: (wid*.26),
//        bottom:(hgt*.3),
//        left: 50
//    },
//    width = wid -50 - margin.left - margin.right,
//    height = hgt - margin.top - margin.bottom;
//}else{
//        var yAxis;
//    var margin = {
//        top: 10,
//        right: (wid*.26),
//        bottom:(hgt*.4),
//        left: 50
//    },
//    width = wid -140 - margin.left - margin.right,
//    height = hgt - margin.top - margin.bottom;
//}
                  var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.1;
    }
    
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
    if(wid > 590){
    var y = d3.scale.linear()
    .rangeRound([0, wid*.90], .1, .1);
    }else{
    var y = d3.scale.linear()
    .rangeRound([0, wid*.70], .1, .1);  
    }
    }else{     
         if(wid > 700){ 
    var y = d3.scale.linear()
                .rangeRound([0, wid*1.0], .1, .1);
    }else{
    var y = d3.scale.linear()
                .rangeRound([0, wid*.85], .1, .1);  
            }
    }
    var displayLegends = chartData[div]["displayLegends"];
        if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
        if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
        if(wid > 700 && wid< 850){ 
            var y = d3.scale.linear()
            .rangeRound([0, wid*.80], .1, .1);
        }else if(wid>850){
           var y = d3.scale.linear()
            .rangeRound([0, wid*.90], .1, .1);
        }else{
            var y = d3.scale.linear()
    .rangeRound([0, wid*.70], .1, .1);  
    } 
        }else{
            if(wid > 700){ 
                var y = d3.scale.linear()
                .rangeRound([0, wid*1.0], .1, .1);
            }else{
                var y = d3.scale.linear()
                .rangeRound([0, wid*.85], .1, .1);  
    }
        }
    }else{
        if(wid > 700){ 
                var y = d3.scale.linear()
                .rangeRound([0, wid*1.0], .1, .1);
            }else{
                var y = d3.scale.linear()
                .rangeRound([0, wid*.85], .1, .1);  
            }
    }
            
            
            if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){ 
   var x = d3.scale.ordinal()
    .rangeRoundBands([0, (hgt*.85)], .1);
}else{
     x = d3.scale.ordinal()
    .rangeRoundBands([0, (hgt*.85)], .1);
  }  //end  by mayank sh. for display properties
  
  if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    var margin = {top: 5,right: 15,bottom: 10,left: 25};
   }else{
    margin = {top: 5, right: 20, bottom: 10,left: 90 };
   }

        if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
        make_x_axis = function() {
            return d3.svg.gridaxis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.gridaxis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }else{
        make_x_axis = function() {
            return d3.svg.axis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.axis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }

if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
        var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }else{
        var xAxis = d3.svg.axis()
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
            return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
            .append("svg")
            .attr("id","svg_"+div)
             .attr("viewBox", "-25 -20 "+(mainWidth*1.14)+" "+(hgt+47)+" ")
             .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + (margin.left*.55) + "," + (margin.top+12)+ ")");
            
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
        for(var j in data){
            if(data[j][columns[0]]===viewByValue){
                data[j]["val"][0]["y0"]=y0;
                data[j]["val"][0]["y1"]=y1;
                y0=y1;
                innRecCount++;
                if(innRecCount<innRecordCount){
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
    }else{
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
    }else{
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
    }else{
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
    }else{
       minVal = measArr[0] + diffFactor;
       }
    }

   y.domain([parseFloat(minVal), parseFloat(max)]);
   
    svg.append("g")
           .attr("class", "x axis")
           .attr("transform", "translate(23,-5)")
           .call(xAxis)
           .selectAll('text')
           .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
           .text(function(d,i) {
             if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{   
             return buildXaxisFilter(div,d,i);
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
             if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
             return 0;
             }else {
             return -5;
             }
             })
           .attr('transform',function(d,i){
             if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
             return "rotate(0)";
             }else {
             return "rotate(-30)";
             }
             })
           .style("text-anchor", "end") 
           .append("svg:title")
           .text(function(d) {          
            return d;
            });
   
   
   if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){} else{            
        svg.append("g")
        .attr("class", "y axis")
        .attr("transform", "translate(23," + (hgt*.88) + ")")
//        .attr("transform", "translate(23,-30)")
        .call(yAxis)
        .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "5")
        .style("text-anchor", "middle") 
        .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
        .text(function(d,i) {
            if(typeof displayX !=="undefined" && displayX=="Yes"){
                if(yAxisFormat==""){
                    return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
   
   
   
   if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
        svg.append("g")
        .attr("class", "grid11")
        .attr("transform", function(d) {
            return "translate(23," + (hgt*.86) + ")"
//            return "translate(23,-5)"
        })
        .call(make_y_axis()
            .tickSize((-wid), 0, 0)
//            .tickSize((wid*.52), 0, 0)
            .tickFormat("")
            )
    }
    
   
        
var colIds=[];
colIds = chartData[div]["viewIds"];
     var state = svg.selectAll(".state")
            .data(data)
            .enter()
            .append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
                 return "translate(0," + (x(d[columns[0]])-8)+")";
            });
   var counter=0;
   var index=0;
    state.selectAll("rect")
            .data(function(d) {
                return d.val;
            })
            .enter()
            .append("rect")
            .attr("height", x.rangeBand()*.95)
            .attr("x", function(d) {
             return y(d.y0)+23;
             })
            .attr("width", function(d) {
                return y(d.y1)-y(d.y0); 
            })
            .attr("y","5")
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
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
            })
            .on("mouseover", function(d, i) {
                var barId=$(this).attr("id");
                barId=barId.split(":")[barId.split(":").length-1]
                 var bar = d3.select(this);
                 var keys1 =  Object.keys(chartColorMap);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;


                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition,div);
                }
                else {
                    msrData = addCommas(d.value);
                }
                var content = "";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[d.index][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " +msrData+ "</span><br/>";
//                for(var j in tooltipData[barId]){  // update by mayank sharma
//                    content += "<span class=\"value\"> <b>" +addCommas(numberFormat((tooltipData[barId][j][Object.keys(tooltipData[barId][j])[0]]),yAxisFormat,yAxisRounding,div))+ "</b> "+ measureArray[0]+" : "+Object.keys(tooltipData[barId][j])[0]+"</span><br/>";
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
                    var yvalue =(x(d[columns[0]])+(x.rangeBand())/2)+2;
                    var xvalue = y(d.y1);
                    if(typeof chartData[div]["LabelPos"]==='undefined' || chartData[div]["LabelPos"]==='Top'){
                        return "translate(" + xvalue + ", " + (yvalue) + ")";
                    }else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Center'){
                        return "translate(" +(23+(y(d.y0)+(y(d.y1)-y(d.y0))/2))+ ", " + (yvalue) + ")";
                    }else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Bottom'){
                        return "translate(" +(y(d.y0)+40)+ ", " + (yvalue) + ")";
                    }
                    return "translate(" + xvalue + ", " + (yvalue) + ")";
                })
                .style("text-anchor","middle")
            .text(function(d,i)
                {
                    if((y(d.y1) - y(d.y0))<40){  
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
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){ 

                        if(typeof displayType=="undefined" || displayType==="Absolute"){
                            if(typeof chartData[div]["hideLabel"] != "undefined"){
                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][i] == "Yes"){
                                    return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));

                                }else {
                                    return "";
                                }
                            }else {
                            return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                        }
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
		var rectsize = parseInt(hgt/35);
//                rectsize=rectsize<5?5:rectsize;
//                rectsize=rectsize>9?9:rectsize;
                rectsize=rectsize>10?10:rectsize;
                var legendLength;
                var keys1 =  Object.keys(chartColorMap);
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(keys1.length<15?keys1.length:15); 
                      }
  if(legendLength>=15){
        yvalue = parseInt((hgt * .09));
             yLegendvalue = parseInt((hgt * .017));
             rectyvalue = parseInt((hgt * .05));
           
}
        else{
           if (legendLength>=10){
                 yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+25;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+7);
            }
            else if (legendLength>=6){
                yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+40;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+17);
            }
            else{
            yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+45;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+20);
        }
    }

  if(fontsize1>14){
                  fontsize1 = 13;
                }else if(fontsize1<9){
                  fontsize1 = 9;
                }
                
 if (fontsize>14){
     fontsize = 14;
   }else if(fontsize<9){
                  fontsize = 9;
                }
        var count = 0;
        if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
        var transform =

            svg.append("g")
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + (mainWidth*.90)  + "," + (yLegendvalue*.25) + ")")
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
            yvalue = parseInt(yvalue+hgt*0.075)
            rectyvalue = parseInt(rectyvalue+hgt*0.075)
            }
             if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
                  var defaultwidth="";
                  if(wid >750  && wid < 850){
                    defaultwidth=mainWidth*.90;
                  }else if(wid > 850){
                      defaultwidth=mainWidth*.95;
                }else{
                     defaultwidth= mainWidth*.80;
                }
             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("y","-10")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (defaultwidth)   + "," + (yvalue)*.70 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                  return  chartColorMap[keys1[m]];
            })
            svg.append("g")
            .append("text")
            .attr("y","-10")
            .attr("transform", "translate(" + ((defaultwidth)+16)   + "," + (yvalue+14)*.70 + ")")
            .attr("fill", function(){
                if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return chartColorMap[keys1[m]];
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
                  if(keys1[m].length>11){
                    return keys1[m].substring(0, 11)+"..";
                     }else{
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
//                  }else{
//             svg.append("g")
//            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("y","-10")
//            .attr("style", "overflow:scroll")
//            .attr("transform", "translate(" + (mainWidth*.80)   + "," + (yvalue)*.70 + ")")
//            .attr("width", rectsize)
//            .attr("height", rectsize)
//            .attr("fill", function(){
//                  return  chartColorMap[keys1[m]];
//            })
//            svg.append("g")
//            .append("text")
//            .attr("y","-10")
//            .attr("transform", "translate(" + ((mainWidth*.80)+15)   + "," + (yvalue+14)*.70 + ")")
//            .attr("fill", function(){
//                if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
//              if(chartData[div]["colorLegend"]=="Black") {
//                  return "#000";
//              } else{
//                  return chartColorMap[keys1[m]];
//              }
//                }else{
//                 return  "#000";
//                }
//            })
//            .attr("style", "font-size:"+fontsize1+"px")
//            .attr("id",function(){
//                return keys1[m];
//            } )
//            .text(function(){
//                if(keys1[m]!="undefined"){
//                  if(keys1[m].length>11){
//                    return keys1[m].substring(0, 11)+"..";
//                     }else{
//                    return keys1[m];
//                    }
//                }else{
//                     return  "";
//                 }
//           })
//           .attr("svg:title",function(d){
//               return keys1[m];
//           })
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id,div)
//            })
//           .on("mouseout", function(d, j) {
//            setMouseOutEvent(this.id,div)
//             })
//              count++
//                  }
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
                var keys1 =  Object.keys(chartColorMap);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
}
                else{

                    legendLength=(keys1.length<15?keys1.length:15); 
                }

               if(legendLength>7 && legendLength<10){

        yvalue = parseInt((hgt * .37));
        yLegendvalue = parseInt((hgt * .3));
        rectyvalue = parseInt((hgt * .33));
}else if(legendLength>=10){

        yvalue = parseInt((hgt * .09));
        yLegendvalue = parseInt((hgt * .017));
        rectyvalue = parseInt((hgt * .05));
}
else{

     yvalue = parseInt((hgt * .42));
        yLegendvalue = parseInt((hgt * .35));
        rectyvalue = parseInt((hgt * .38));
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
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }

var count = 0;

 
if(wid<hgt){
                yvalue = parseInt(hgt*.10);
                rectyvalue = parseInt((hgt*0.91 ));
            }else{
                yvalue = parseInt(hgt*1.15);
                rectyvalue = parseInt((hgt*1.05 ));
            }
var widthvalue = parseInt((wid *.01));// for all rect and text align
            var widthRectvalue = parseInt((wid *.1)-(wid*.018));
            var startX=widthvalue;
            yvalue*=.82;
var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/14;
chartWid*=.75;
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
            .attr("transform", "translate(" + (widthvalue)  + "," + (yvalue) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
              .attr("fill", function(){
                return chartColorMap[keys1[m]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthvalue+20)  + "," + (yvalue+9) + ")")
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

// add by mayank sharma
function buildGStackedHPercentage(div,data, columns, measureArray,wid,hgt){
    var colorSet = d3.scale.category10();
    d3.selection.prototype.dblTap = function(callback) {
        var last = 0;
        return this.each(function() {
            d3.select(this).on("touchstart", function(e) {
                if ((d3.event.timeStamp - last) < 500) {
                    return callback(e,this.id);
                }
                last = d3.event.timeStamp;
            });
        });
    } 
    var chartData = JSON.parse(parent.$("#chartData").val());
    var mainWidth = wid;
    var measure = measureArray[0];
    var chartColorMap = {};
    var measArr = [];
    var autoRounding;
    var customTicks = 5;
    var max = maximumValue(data, measure);
    var dataLine = [];
    for(var l=0;l<(data.length );l++){
        var colMap={};
        if(typeof colMap[data[l][columns[0]]]=="undefined" && dataLine.indexOf(data[l][columns[0]]) ===-1){
            colMap[columns[0]] = data[l][columns[0]];
            colMap[measure] = data[l][measure];
        }
        dataLine.push(colMap);
    }

    var summArr=[];
    for(var i in dataLine){
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
    var innRecordCount;
    if(typeof chartData[div]["innerRecords"]!=='undefined'){
        innRecordCount=chartData[div]["innerRecords"];
    }else{
        innRecordCount=5;
    }
    var measSum=[];
    var c=0;
    for(var i in viewByValues){
        var viewByValue=viewByValues[i];
        var y0=0,y1=measValues[c==0?0:++c];
        var innRecCount=0;
        var f=getViewByValueCount(viewByValue,data,columns);
        for(var j in data){
            if(data[j][columns[0]]===viewByValue){
                y0=y1;
                innRecCount++;
                if(innRecCount<innRecordCount && innRecCount<f){
                    y1=parseInt(y1)+parseInt(measValues[++c]);
                }
            }
        }
        measSum.push(y1);
    }
    if(fromoneview!='null'&& fromoneview=='true'){
    }else{
        if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
            customTicks = chartData[div]["yaxisrange"]["axisTicks"];
        }
        if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
            autoRounding = columnMap[measure]["rounding"];
        } else {
            autoRounding = "1d";
        }
    }

var isDashboard = parent.$("#isDashboard").val();
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
    }else {
        drillWithinchart(id1,div);
    }   
}

var offset=0;
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    offset=15;
}
var prop = graphProp(div);
var yAxis;
var range="";
if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
    range=0.65;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.90;
}else{ 
    range=0.1;
}
    
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
    if(wid > 590){
        var y = d3.scale.linear()
        .rangeRound([0, wid*.90], .1, .1);
    }else{
        var y = d3.scale.linear()
        .rangeRound([0, wid*.70], .1, .1);  
    }
}else{     
    if(wid > 700){ 
        var y = d3.scale.linear()
        .rangeRound([0, wid*1.0], .1, .1);
    }else{
        var y = d3.scale.linear()
        .rangeRound([0, wid*.85], .1, .1);  
    }
}
var displayLegends = chartData[div]["displayLegends"];
if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
    if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
        if(wid > 700 && wid< 850){ 
            var y = d3.scale.linear()
            .rangeRound([0, wid*.79], .1, .1);
        }else if(wid>850){
            var y = d3.scale.linear()
            .rangeRound([0, wid*.90], .1, .1);
        }else{
            var y = d3.scale.linear()
            .rangeRound([0, wid*.70], .1, .1);  
        } 
    }else{
        if(wid > 700){ 
            var y = d3.scale.linear()
            .rangeRound([0, wid*1.0], .1, .1);
        }else{
            var y = d3.scale.linear()
            .rangeRound([0, wid*.85], .1, .1);  
        }
    }
}else{
    if(wid > 700){ 
        var y = d3.scale.linear()
        .rangeRound([0, wid*1.0], .1, .1);
    }else{
        var y = d3.scale.linear()
        .rangeRound([0, wid*.85], .1, .1);  
    }
}
            
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){ 
    var x = d3.scale.ordinal()
    .rangeRoundBands([0, (hgt*.85)], range);
}else{
    x = d3.scale.ordinal()
    .rangeRoundBands([0, (hgt*.85)], range);
}  //end  by mayank sh. for display properties
  
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    var margin = {
        top: 5,
        right: 15,
        bottom: 10,
        left: 25
    };
}else{
    margin = {
        top: 5, 
        right: 20, 
        bottom: 10,
        left: 90
    };
}

if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
    make_x_axis = function() {
        return d3.svg.gridaxis()
        .scale(x)
        .orient("left")
        .ticks(5)
    }
    make_y_axis = function() {
        return d3.svg.gridaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
    }
}else{
    make_x_axis = function() {
        return d3.svg.axis()
        .scale(x)
        .orient("left")
        .ticks(5)
    }
    make_y_axis = function() {
        return d3.svg.axis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
    }
}

if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    var xAxis = d3.svg.trendaxis()
    .scale(x)
    .orient("left");
}else{
    var xAxis = d3.svg.axis()
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
        return d + "%";
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
.append("svg")
.attr("id","svg_"+div)
.attr("viewBox", "-25 -20 "+(mainWidth*1.14)+" "+(hgt+47)+" ")
.classed("svg-content-responsive", true)
.append("g")
.attr("transform", "translate(" + (margin.left*.55) + "," + (margin.top+12)+ ")");
            
var tmp = colorSet;
tmp.domain(d3.keys(data[0]).filter(function(key) {
    if(typeof columns[1] === "undefined" || columns[1] === ""){
        return key === columns[0];
    }else {
        return key === columns[1];
    }
}));
var maxBlockHeight={};
data.forEach(function(d,i) {
    var y0 = 0;
    var colName=data[i][columns[0]];
    var totalVal = 1;
    for(var k in summArr){
        if(summArr[k][columns[0]] == colName){
            totalVal = summArr[k][measureArray[0]];
        }
    }
    var v=(parseFloat(hgt) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]);
    if(typeof maxBlockHeight[colName]==='undefined' || v>maxBlockHeight[colName]){
        maxBlockHeight[colName]=v;
    }
});  
data.forEach(function(d,i) {
    var y0 = 0;
    d.val = tmp.domain().map(function(name) {
        var colName=data[i][columns[0]];
        var totalVal = 1;
        for(var k in summArr){
            if(summArr[k][columns[0]] == colName){
                totalVal = summArr[k][measureArray[0]];
            }
        }
        return {
            name: d[name],
            name1:d[columns[0]],
            viewBy:name,
            value: d[measureArray[0]],
            y0: y0,
            y1: y0 += ((parseFloat(hgt) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]])*(100/maxBlockHeight[colName])),
            index:i
        };
    });
    if(typeof d.val.length !=="undefined" || d.val.length !==""){
        d.total = d.val[d.val.length - 1].y1;
    }
});
c=0;
for(i in viewByValues){
    viewByValue=viewByValues[i];
    y0=0,y1=measValues[c==0?0:++c];
    innRecCount=0;
    f=getViewByValueCount(viewByValue,data,columns);
    for(j in data){
        if(data[j][columns[0]]===viewByValue){
            data[j]["val"][0]["y0"]=y0;
            data[j]["val"][0]["y1"]=y1/measSum[i]*100;
            y0=y1/measSum[i]*100;
            innRecCount++;
            if(innRecCount<innRecordCount && innRecCount<f){
                y1=parseInt(y1)+parseInt(measValues[++c]);
            }
        }
    }
}
for(i in viewByValues){
    viewByValue=viewByValues[i];
    for(j in data){
        if(data[j][columns[0]]===viewByValue){
            data[j]["total"]=100;
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
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" &&  chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
        max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
    }else{
        minVal=0;
        max = maximumValue(data, "total");
        svg.call(yAxis1);
        var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
        if(measArr[0]<0){
            minVal = measArr[0] + diffFactor;
        }else{
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
    }else{
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
        }else{
            minVal = measArr[0] + diffFactor;
            if(measArr[0]>=0 && minVal<0){
                minVal=0;
            }
        }
    }
}else{
    max = maximumValue(data, "total");
    svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
    if(measArr[0]<0){
        minVal = measArr[0] + diffFactor;
    }else{
        minVal = measArr[0] + diffFactor;
        if(measArr[0]>=0 && minVal<0){
            minVal=0;
        }
    }
    if (data.length > 1) {
        minVal = minimumValue(data, measArr) * .8;
    }else{
        minVal = 0;
    }
}
y.domain([0, 100]);
   
svg.append("g")
.attr("class", "x axis")
.attr("transform", "translate(23,-5)")
.call(xAxis)
.selectAll('text')
.style('font-size',function(d,i) {
                return font2(div,d,i);
            })
.text(function(d,i) {
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{   
        return buildXaxisFilter(div,d,i);
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
    if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
        return 0;
    }else {
        return -5;
    }
})
.attr('transform',function(d,i){
    if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
        return "rotate(0)";
    }else {
        return "rotate(-30)";
    }
})
.style("text-anchor", "end") 
.append("svg:title")
.text(function(d) {          
    return d;
});  
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}
else{            
    svg.append("g")
    .attr("class", "y axis")
    .attr("transform", "translate(23," + (hgt*.88) + ")")
    .call(yAxis)
    .selectAll('text')
    .attr("dy", ".35em")
    .attr("y", "5")
    .style("text-anchor", "middle") 
    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .append("svg:title")
    .text(function(d) {
        return d;
    });
}
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
    svg.append("g")
    .attr("class", "grid11")
    .attr("transform", function(d) {
        return "translate(23," + (hgt*.86) + ")"
    })
    .call(make_y_axis()
        .tickSize((-wid), 0, 0)
        .tickFormat("")
        )
} 
var state = svg.selectAll(".state")
.data(data)
.enter().append("g")
.attr("class", "g")
.attr("transform", function(d) {
    return "translate(0," + (x(d[columns[0]])-8)+")";
});
state.selectAll("rect")
.data(function(d) {
    return d.val;
})
.enter()
.append("rect")
.attr("height", x.rangeBand()*.95)
.attr("x", function(d) {
    return y(d.y0)+23;
})
.attr("width", function(d) {
    return y(d.y1)-y(d.y0); 
})
.attr("y","5")
.style("fill", function(d,i) {
    if(typeof chartColorMap[d.name]==="undefined"){
            chartColorMap[d.name]=getDrawColor(div, d.index) ;
            return getDrawColor(div, d.index);
    }else{
        return chartColorMap[d.name];
    }
})
.attr("id", function(d) {
    return data[d.index][columns[0]]+":"+d.name + ":" + d.value;
})
.attr("onclick", fun)
.dblTap(function(e,id) {
    drillFunct(id);
}) 
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
})
.attr("class", function(d, i) {
    return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
})
.on("mouseover", function(d, i) {
    var bar = d3.select(this);
    var indexValue = bar.attr("index_value");
    var barSelector = "." + "bars-Bubble-" + indexValue+div;
    var selectedBar = d3.selectAll(barSelector);
    selectedBar.style("fill", drillShade);
    var msrData;
    if (isFormatedMeasure) {
        msrData = numberFormat(d.value, round, precition,div);
    }else {
        msrData = addCommas(d.value);
    }
    var content = "";
    content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[d.index][columns[0]] + "</span><br/>";
    content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
    content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
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
graphProp(div);
            
var txtHgt=12;
state.selectAll("rect1")
            .data(function(d) {
                return d.val;
            })
            .enter()
            .append("text")
                .attr("transform",function(d) {
                    var yvalue =x.rangeBand()/2;
                    var xValue = y(d.y1);
                    if(typeof chartData[div]["LabelPos"]==='undefined' || chartData[div]["LabelPos"]==='Top'){
                        yvalue =x.rangeBand()/2;
                        xValue = y(d.y1);
                    }
                    else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Center'){
                        yvalue =x.rangeBand()/2;
                        xValue = y(d.y1)+(y(d.y0) - y(d.y1))/2+(txtHgt*1.8);
                    }
                    else if(typeof chartData[div]["LabelPos"]!=='undefined' && chartData[div]["LabelPos"]==='Bottom'){
                        yvalue =x.rangeBand()/2;
                        xValue = y(d.y1)+(y(d.y0) - y(d.y1))+(txtHgt*4);
                    }
                    return "translate(" + xValue + ", " + (yvalue+9) + ")";
                })
                .style("text-anchor","middle")
            .text(function(d,i)
                {
                    if((y(d.y1) - y(d.y0))<40){  
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
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){ 
                                var percentage = (d.value / parseFloat(groupedSum)) * 100;
                                return percentage.toFixed(1) + "%";

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
		var rectsize = parseInt(hgt/35);
                rectsize=rectsize>10?10:rectsize;
                var legendLength;
                var keys1 =  Object.keys(chartColorMap);
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(keys1.length<15?keys1.length:15); 
                      }
  if(legendLength>=15){
        yvalue = parseInt((hgt * .09));
             yLegendvalue = parseInt((hgt * .017));
             rectyvalue = parseInt((hgt * .05));
}else{
           if (legendLength>=10){
                 yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+25;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+7);
            } else if (legendLength>=6){
                yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+40;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+17);
            }else{
            yvalue = parseInt((hgt / 2)-(legendLength/2)*(hgt*.06))+45;
            rectyvalue = parseInt((hgt / 2-(legendLength/2)*(hgt*.06))+20);
        }
    }
  if(fontsize1>14){
                  fontsize1 = 13;
                }else if(fontsize1<9){
                  fontsize1 = 9;
                }
 if (fontsize>14){
     fontsize = 14;
   }else if(fontsize<9){
                  fontsize = 9;
                }
        var count = 0;
        if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
        var transform =
            svg.append("g")
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + (mainWidth*.90)  + "," + (yLegendvalue*.25) + ")")
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
            yvalue = parseInt(yvalue+hgt*0.075)
            rectyvalue = parseInt(rectyvalue+hgt*0.075)
            }
             if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
                  var defaultwidth="";
                  if(wid >750  && wid < 850){
                    defaultwidth=mainWidth*.91;
                  }else if(wid > 850){
                      defaultwidth=mainWidth*.95;
                }else{
                     defaultwidth= mainWidth*.80;
                }
             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("y","-10")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (defaultwidth)   + "," + (yvalue)*.70 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                  return  chartColorMap[keys1[m]];
            })
            svg.append("g")
            .append("text")
            .attr("y","-10")
            .attr("transform", "translate(" + ((defaultwidth)+16)   + "," + (yvalue+14)*.70 + ")")
            .attr("fill", function(){
                if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return chartColorMap[keys1[m]];
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
                  if(keys1[m].length>11){
                    return keys1[m].substring(0, 11)+"..";
                     }else{
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
    }else{
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
                var keys1 =  Object.keys(chartColorMap);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
             } else{
                    legendLength=(keys1.length<15?keys1.length:15); 
                }
               if(legendLength>7 && legendLength<10){

        yvalue = parseInt((hgt * .37));
        yLegendvalue = parseInt((hgt * .3));
        rectyvalue = parseInt((hgt * .33));
}else if(legendLength>=10){
        yvalue = parseInt((hgt * .09));
        yLegendvalue = parseInt((hgt * .017));
        rectyvalue = parseInt((hgt * .05));
}else{
     yvalue = parseInt((hgt * .42));
        yLegendvalue = parseInt((hgt * .35));
        rectyvalue = parseInt((hgt * .38));
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
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
var count = 0;
if(wid<hgt){
                yvalue = parseInt(hgt*.10);
                rectyvalue = parseInt((hgt*0.91 ));
            }else{
                yvalue = parseInt(hgt*1.15);
                rectyvalue = parseInt((hgt*1.05 ));
            }
var widthvalue = parseInt((wid *.01));// for all rect and text align
            var widthRectvalue = parseInt((wid *.1)-(wid*.018));
            var startX=widthvalue;
            yvalue*=.82;
var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/14;
chartWid*=.75;
       for(var m=0;m<(legendLength);m++){ ////length
           var charactercount = keys1[m].length;
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (widthvalue)  + "," + (yvalue) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
              .attr("fill", function(){
                return chartColorMap[keys1[m]];
            })
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthvalue+20)  + "," + (yvalue+9) + ")")
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
                    return keys1[m];
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
              }else{
                  yvalue +=20;
                  widthvalue=startX;
}
}
}
}
} // end by mayank sharma

// add by mayank sharma
function buildGroupedHorizontalBar(div,data, columns, measureArray,divWdt,divHgt,divAppend) {
    d3.selection.prototype.dblTap = function(callback) {
        var last = 0;
        return this.each(function() {
            d3.select(this).on("touchstart", function(e) {
                if ((d3.event.timeStamp - last) < 500) {
                    return callback(e,this.id);
                }
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
    var chartData = JSON.parse(parent.$("#chartData").val());
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
    var chartColorMap = {};
    var msr = [];
    msr = data;
    var chartMap = {};
    var colorGroup = {};
    var measure1 = measureArray[0];
    if (columns.length < 2) {
        columns.push(columns[0]);
    }
    var minVal = minimumValue(data, measure1);
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
        }
    }

    var fun="";
    hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
    if(hasTouch){
        fun="";
    }else{
        fun = "drillWithinchart(this.id,\""+div+"\")";
    }
    function drillFunct(id1){
        drillWithinchart(id1,div);
    }
 
    var data1 = {};
    var data2 = {};
    var xdata = [];
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
        if(divWdt > 590){
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*.90], .1, .1);
        }else{
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*.70], .1, .1);  
        }
    }else{     
        if(divWdt > 700){ 
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*1.0], .1, .1);
        }else{
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*.85], .1, .1);  
        }
    }
    var displayLegends = chartData[div]["displayLegends"];
    if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
        if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
            if(divWdt > 700 && divWdt< 850){ 
                var y = d3.scale.linear()
                .rangeRound([0, divWdt*.80], .1, .1);
            }else if(divWdt>850){
                var y = d3.scale.linear()
                .rangeRound([0, divWdt*.90], .1, .1);
            }else{
                var y = d3.scale.linear()
                .rangeRound([0, divWdt*.70], .1, .1);  
            } 
        }else{
            if(divWdt > 700){ 
                var y = d3.scale.linear()
                .rangeRound([0, divWdt], .1, .1);
            }else{ 
                var y = d3.scale.linear()
                .rangeRound([0, divWdt*.85], .1, .1);  
            }
        }
    }else{
        if(divWdt > 700){ 
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*1.0], .1, .1);
        }else{
            var y = d3.scale.linear()
            .rangeRound([0, divWdt*.85], .1, .1);  
        }
    }
            
            
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){ 
        var x = d3.scale.ordinal()
        .rangeRoundBands([0, (divHgt*.82)], .1);
    }else{
        x = d3.scale.ordinal()
        .rangeRoundBands([0, (divHgt*.85)], .1);
    }  //end  by mayank sh. for display properties
  
    var x1 = d3.scale.ordinal(); 
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
        var margin = {
            top: 5,
            right: 15,
            bottom: 10,
            left: 25
        };
    }else{
        margin = {
            top: 5, 
            right: 20, 
            bottom: 10,
            left: 90
        };
    }

    if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
        make_x_axis = function() {
            return d3.svg.gridaxis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.gridaxis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }else{
        make_x_axis = function() {
            return d3.svg.axis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.axis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }

    if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
        var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }else{
        var xAxis = d3.svg.axis()
        .scale(x)
        .orient("left");
    }

    if (isFormatedMeasure) {
        var  yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d) {
            return d;
        });
    } else {
        var yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d, i) {
            return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
    .append("svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "-25 -20 "+(mainWidth*1.14)+" "+(divHgt+47)+" ")
    .classed("svg-content-responsive", true)
    .append("g")
    .attr("transform", "translate(" + (margin.left*.55) + "," + (margin.top+12)+ ")");

    var localColorMap = {};
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    
    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayXLine"]==="undefined" || chartData[div]["displayXLine"]==="" || chartData[div]["displayXLine"]==="Yes"){  
  svg.append("line")  //add by mayank sh.
        .attr("x1",function(d){
            if(divWdt > 700 && divWdt< 850){ 
                return divWdt*.80;
    }else if(divWdt>850){
        return divWdt*.90;
    }else{
        return divWdt*.70;
    }
  })
        .attr("y1",divHgt*.85)
        .attr("x2",23)
        .attr("y2",divHgt*.85)
        .style("stroke", "#E1DEDE");
 }else{}
}else{
        svg.append("g")
        .attr("class", "grid11")
        .attr("transform", function(d) {
            return "translate(23," + (divHgt*.86) + ")"
        })
        .call(make_y_axis()
            .tickSize((-divWdt), 0, 0)
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
                }else {
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
                        return {
                            name: name, 
                            value: +d[name], 
                            parentName: d[colName[0]]
                        };
                    });
                }else {
                    d.ages = [];
                }
            });
        }
        x.domain(data.map(function(d, i) {
            return d[columns[0]];
        }));
        x1.domain(ageNames).rangeRoundBands([0, x.rangeBand()]);
        for (var i = 0; i < ageNames.length; i++) {
            ageNames1[ageNames[i]] = ageNames[i];
            groupNames[ageNames[i]]=ageNames[i];
        }
        var yVal = 0;

        if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
            if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
                max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
            }else{
            }
        }else{
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
            if (minVal < 0) {
                yVal = minVal;
            }
        }
        if(yVal.length>1){
            y.domain([yVal, max]);
        }else{
            y.domain([parseFloat(minVal), parseFloat(max)]);
        }
        var col = [];
        data.forEach(function(d) {
            col.push(d[columns[0]]);
        });

        if (count < 1) {
            svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(23,-5)")
            .call(xAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            .text(function(d,i) {
                if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{   
                    return buildXaxisFilter(div,d,i);
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
                if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
                    return 0;
                }else {
                    return -5;
                }
            })
            .attr('transform',function(d,i){
                if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
                    return "rotate(0)";
                }else {
                    return "rotate(-30)";
                }
            })
            .style("text-anchor", "end") 
            .append("svg:title")
            .text(function(d) {          
                return d;
            });
        }
   
        if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){} else{ 
            if (count < 1) {
                svg.append("g")
                .attr("class", "y axis")
                .attr("transform", "translate(23," + (divHgt* .88 ) + ")")
                .call(yAxis)
                .selectAll('text')
                .attr("dy", ".35em")
                .attr("y", "5")
                .style("text-anchor", "middle") 
                .style('font-size',function(d,i) {
                return font1(div,d,i);
                })
                .text(function(d,i) {
                    if(typeof displayX !=="undefined" && displayX=="Yes"){
                        if(yAxisFormat==""){
                            return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
                count++;
            }
        } 
   
        var Brands = svg.selectAll(".Category")
        .data(data)
        .enter().append("g")
        .attr("class", "g")
        .attr("id", keys[k])
        .attr("transform", function(d) {
            return "translate(0," + (x(d[columns[0]])-8)+")";
        });
        Brands.selectAll(".gClass-" + keys[k].replace(/[^a-zA-Z0-9]/g, '', 'gi'))
        .data(function(d) {
            val = d.ages.filter(function(d) {
                return d.value !== 0;
            });
            return val;
        })
        .enter()
        .append("g")
        .attr("class", "gClass-" + keys[k].replace(/[^a-zA-Z0-9]/g, '', 'gi'))
        .append("rect")
        .attr("height", x1.rangeBand())
        .attr("y", function(d) {
            return x1(d.name);
        })
        .attr("x", 23)
        .attr("rx", barRadius)
        .attr("width", function(d) {
            var wid2="";
           if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
           if(divWdt >750  && divWdt < 850){
                wid2=divWdt*.80;
            }else if(divWdt > 850){
                wid2=divWdt*.90;
            }else{
                wid2= divWdt*.70;
            }
           }else{
               if(divWdt > 700){ 
                wid2= divWdt;
            }else{ 
                wid2= divWdt*.85;
            }
           }
            return ((wid2) - y(d.value));
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
                }else{
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
                }else{
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
                msrData = addCommas(d.value);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                msrData = addCommas(d.value);
            }else{
                msrData = addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }
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
    }
    
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
            if(legendLength>=15){
                yvalue = parseInt((divHgt * .09));
                yLegendvalue = parseInt((divHgt * .017));
                rectyvalue = parseInt((divHgt * .05));
            }else{
                if (legendLength>=10){
                    yvalue = parseInt((divHgt / 2)-(legendLength/2)*(divHgt*.06))+25;
                    rectyvalue = parseInt((divHgt / 2-(legendLength/2)*(divHgt*.06))+7);
                }else if (legendLength>=6){
                    yvalue = parseInt((divHgt / 2)-(legendLength/2)*(divHgt*.06))+40;
                    rectyvalue = parseInt((divHgt / 2-(legendLength/2)*(divHgt*.06))+17);
                }else{
                    yvalue = parseInt((divHgt / 2)-(legendLength/2)*(divHgt*.06))+45;
                    rectyvalue = parseInt((divHgt / 2-(legendLength/2)*(divHgt*.06))+20);
                }
            }

            if(fontsize1>14){
                fontsize1 = 10;
            }else if(fontsize1<7){
                fontsize1 = 7;
            }
            if(fontsize>16){
                fontsize = 12;
            }else if(fontsize1<7){
                fontsize = 9;
            }
            if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
            }

            var legendWid="";
                if(divWdt >750  && divWdt < 850){
                    legendWid=divWdt*.90;
                }else if(divWdt > 850){
                    legendWid=divWdt*.95;
                }else{
                    legendWid= divWdt*.80;
                }
                
            var count = 0;
            if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
                var transform =
                svg.append("g")
                .append("text")
                .attr("style","margin-right:10")
                .attr("style", "font-size:"+fontsize+"px")
                .attr("transform", "translate(" +(legendWid-7)  + "," + (yvalue*.70-18) + ")")
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
                    yvalue = parseInt(yvalue+divHgt*0.075)
                    rectyvalue = parseInt(rectyvalue+divHgt*0.075)
                }
                
                if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
                    svg.append("g")
                    .append("rect")
                    .attr("style","margin-right:10")
                    .attr("transform", transform)
                    .attr("style", "overflow:scroll")
                    .attr("transform", "translate(" + (legendWid-7)  + "," + (yvalue*.70-8)+ ")")
                    .attr("width", rectsize)
                    .attr("height", rectsize)
                    .attr("fill", function(){
                        return colorGroup[keys1[m]];
                    })
                }
               
                svg.append("g")
                .append("text")
                .attr("transform", "translate(" +(legendWid+9)  + "," + (yvalue*.70) + ")")
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
        }else{
            legendLength=(keys1.length<15?keys1.length:15); 
        }
        if(legendLength>7 && legendLength<10){
            yvalue = parseInt((divHgt * .37));
            yLegendvalue = parseInt((divHgt * .3));
            rectyvalue = parseInt((divHgt * .33));
        }else if(legendLength>=10){
            yvalue = parseInt((divHgt * .09));
            yLegendvalue = parseInt((divHgt * .017));
            rectyvalue = parseInt((divHgt * .05));
        }else{
            yvalue = parseInt((divHgt * .42));
            yLegendvalue = parseInt((divHgt * .35));
            rectyvalue = parseInt((divHgt * .38));
        }
        if(fontsize1>14){
            fontsize1 = 10;
        }else if(fontsize1<7){
            fontsize1 = 7;
        }
        if(fontsize>15){
            fontsize = 12;
        }else if(fontsize1<7){
            fontsize = 9;
        }
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }

        var count = 0;
        if(divWdt<divHgt){
            yvalue = parseInt(divHgt*.10);
            rectyvalue = parseInt((divHgt*0.91 ));
        }else{
            yvalue = parseInt(divHgt*1.15);
            rectyvalue = parseInt((divHgt*1.05 ));
        }
        var widthvalue = parseInt((divWdt *.01));
        var widthRectvalue = parseInt((divWdt *.1)-(divWdt*.018));
        var startX=widthvalue;
        yvalue*=.82;
        var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/14;
        chartWid*=.75;
 
        for(var m=0;m<(legendLength);m++){ 
            var charactercount = keys1[m].length;
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (widthvalue-20)  + "," + (yvalue-10) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                return colorGroup[keys1[m]];
            })
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + widthvalue  + "," + (yvalue) + ")")
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
            var step=0;
            step+=30;
            step+=(keys1[m].length*6.5)
            if(widthvalue+step<chartWid){
                widthvalue += step;
            }else{
                yvalue +=20;
                widthvalue=startX;
            }
        }
    }
    }else{
    }
}

function multiMeasureHorizontalBar(div, data, columns, measureArray, divWid, divHgt) {
         var chartData = JSON.parse(parent.$("#chartData").val());
     if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]=='Y'){
         var len=measureArray.length;
         var transposeMeasureArray=[];
         var transposeData=[];
         for(var i=0; i<len;i++){
             var map={};
             map[columns[0]]=measureArray[i];
             for(var j in data){
                 map[data[j][columns[0]]]=data[j][measureArray[i]]
             }
             transposeData.push(map);
         }
         for(var i in data){
             transposeMeasureArray.push(data[i][columns[0]]);
         }
         measureArray=transposeMeasureArray;
         data=transposeData;
     }
      var color = d3.scale.category10();
            d3.selection.prototype.dblTap = function(callback) {
      var last = 0;
      return this.each(function() {
        d3.select(this).on("touchstart", function(e) {
            if ((d3.event.timeStamp - last) < 500) {
              return callback(e,this.id);
            }
            last = d3.event.timeStamp;
        });
      });
    }
     var fromoneview=parent.$("#fromoneview").val()
if(fromoneview!='null'&& fromoneview=='true'){
}else{}
    var measure1 = measureArray[0];
    var measure2;
    if (measureArray.length === 1) {
        measure2 = measureArray[0];
    } else {
        measure2 = measureArray[1];
    }
     var chartData = JSON.parse(parent.$("#chartData").val());
     var dashletid;
 var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1;
}else{
var prop = graphProp(div);
}
 var customTicks = 5;
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var autoRounding1;
    var autoRounding2;
    if(fromoneview!='null'&& fromoneview=='true'){
    }else{
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
    if (columnMap[measure2] !== undefined && columnMap[measure2] !== "undefined" && columnMap[measure2]["rounding"] !== "undefined") {
        autoRounding2 = columnMap[measure2]["rounding"];
    }
    else {
        autoRounding2 = "1d";
    }
    }
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        divHgt-=20
    }
    var yAxis;
   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    var margin = {top: 5,right: 12,bottom: 10,left:30 };
   }else{
    var margin = {top: 5, right: 20, bottom: 10,left: 130 };
   }
var lbOffset=25;
var leftM=0,rightM=0,topM=0,bottomM=0;
if(chartData[div]["labelPosition"]==='Right'){
            rightM=25;
        }
        if(chartData[div]["labelPosition"]==='Left'){
            leftM=15;
        }
        margin={
          top:margin.top+topM,
          right:margin.right+rightM,
          left:margin.left+leftM,
          bottom:margin.bottom+bottomM
        };
        var width = divWid - 10;
     var height = divHgt;
     var chartMap = {};
//    var divWid = width-10;
//    var divHgt = height;
    var measArr = [];
    var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){ fun="";
        }else{
             fun = "drillWithinchart(this.id,\""+div+"\")";
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }}
    function drillFunct(id1){
        if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        drillChartInDashBoard(id1,div);
    }else {
        drillWithinchart(id1,div);
     } }
 var hide = "hideLabels(this.id,\""+div+"\")";
     if(fromoneview!='null'&& fromoneview=='true'){
var regid=dashletid.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();
 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
var olap=dashletid.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }}
     var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
var x = d3.scale.ordinal()
    .rangeRoundBands([0, divHgt*.83], range);
    
    var max = 0;
var minVal = 0;
var multiple=0;
if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    multiple=1;
}
else{
    multiple=0.8;
}
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){

if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));
var min = parseFloat(minimumValue(data, measureArray[i]));


if(i==0)  {

max=parseFloat(max1);
minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
else if(i>0 && ( min<minVal)){

minVal = min;
}
}
}
}}
//end of max
else{

for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){

var max1 = parseFloat(maximumValue(data, measureArray[i]));
var min = parseFloat(minimumValue(data, measureArray[i]));


if(i==0)  {

max=parseFloat(max1);
minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
else if(i>0 && ( min<minVal)){

minVal = min;
}
}
}
}

//end of default max
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""  && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""){
   minVal = 0;
 }else{
  for(var i in measureArray){

if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {
max=parseFloat(max1);
minVal=parseFloat(min);
}
else if(i>0 && (max1 > max)){
max = max1;
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}
   }
}
}
// end of min
else{
for(var i in measureArray){
if(((typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined")&&i==0 )||( typeof chartData[div]["createBarLine"] !== "undefined" && typeof chartData[div]["createBarLine"][measureArray[i]] !== "undefined" && chartData[div]["createBarLine"][measureArray[i]] === "Yes")){
var max1 = parseFloat(maximumValue(data, measureArray[i]));
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {
max=parseFloat(max1);
minVal=parseFloat(min);
}
else if(i>0 && (max1 > max)){
max = max1;
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}
}
if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }else{
        minVal = 0;
    }
}
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    if(divWid > 590){ 
    var y = d3.scale.linear()
    .domain([parseFloat(minVal), parseFloat(max)])
    .rangeRound([0, divWid*.93], .1, .1);
}else{
    var y = d3.scale.linear()
    .domain([parseFloat(minVal), parseFloat(max)])
    .rangeRound([0, divWid*.70], .1, .1);  
    }
}else{ 
    if(divWid > 590){
     var y = d3.scale.linear()
     .domain([parseFloat(minVal), parseFloat(max)])
    .rangeRound([0, divWid*.79], .1, .1);
        }else{
        var y = d3.scale.linear()
        .domain([parseFloat(minVal), parseFloat(max)])
    .rangeRound([0, divWid*.70], .1, .1);
    }
  }  //end  by mayank sh. for display properties
  if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
        make_x_axis = function() {
            return d3.svg.gridaxis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.gridaxis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }else{
        make_x_axis = function() {
            return d3.svg.axis()
            .scale(x)
            .orient("left")
            .ticks(5)
        }
        make_y_axis = function() {
            return d3.svg.axis()
            .scale(y)
            .orient("bottom")
            .ticks(customTicks)
        }
    }
    if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
        var xAxis = d3.svg.trendaxis()
        .scale(x)
        .orient("left");
    }else{
        var xAxis = d3.svg.axis()
        .scale(x)
        .orient("left");
    }
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
        });
    } else {
        yAxis = d3.svg.trendaxis()
        .scale(y)
        .orient("bottom")
        .ticks(customTicks)
        .tickFormat(function(d, i) {
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                if(yAxisFormat==""){
                    return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=20;
    }
    var svg = d3.select("#"+div)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt-15)+" ")
    .classed("svg-content-responsive", true)
    .append("svg:g")
    .attr("transform", "translate(" + margin.left*.55 + "," + (margin.top+22) + ")");
  
  var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
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
  
  if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
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
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{            
        svg.append("g")
        .attr("class", "y axis")
        .attr("transform", "translate(23," + (divHgt*.86) + ")")
        .call(yAxis)
        .selectAll('text')
        .attr("dy", ".35em")
        .attr("y", "0")
        .style("text-anchor", "middle") 
        .style("font-size"," 9px")
        .text(function(d,i) {
            if(typeof displayX !=="undefined" && displayX=="Yes"){
                if(yAxisFormat==""){
                    return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
        .attr("transform", "translate(23,-5)")
        .call(xAxis)
        .selectAll('text')
        .style("font-size"," 9.5px")
        .text(function(d,i) {
    if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{   
            return buildXaxisFilter(div,d,i);
    }    
        }) 
        .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return "rotate(0)";
        }else {
            return "rotate(-30)";
        }
    })
 .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return -5;
        }else {
            return -5;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return 0;
        }else {
            return -5;
        }
    }).style("text-anchor", "end") 
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
        .attr("transform", "translate(23,-5)")
    }
for(var k=0;k<measureArray.length;k++){
   if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
   if(k==0){
    if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "Yes")) {
        continue;
    }}else{
    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "No") {
        continue;
    }} //end by mayank sh.
}
var measureSum=[];
for(var i in data){
    measureSum[i]=0;
    for(var j in measureArray){
        measureSum[i] += parseInt(data[i][measureArray[j]]);
    }
}
var counter1=0;
var measNo=measureArray.length;
    bars = svg.selectAll(".bar"+k).data(data).enter();
    bars.append("rect")
            .attr("class", "bar")
            .attr("y", function(d) {
                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo*k));
            })
            .attr("height", x.rangeBand() / measNo)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[k]]+":"+k;
            })
            .attr("onclick", fun)
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .attr("x", function(d) {
                if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
                    if(y(0) > y(d[measureArray[k]]))
                return y(d[measureArray[k]]);
                    else
                        return  y(0)+23;
                } else{
                    return "23";
                }
            })
            .attr("width", function(d, i, j) {
                if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
                    return Math.abs(y(0) - y(d[measureArray[k]]));
                }else{ 
                return y(d[measureArray[k]]);
                }
            })
            .attr("fill", function(d, i) {
            return getDrawColor(div, parseInt(k));
            }).attr("class", function(d, i) {
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
                    return "bars-Bubble-index-" + measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                } else{
                return "bars-Bubble-index-" +div+measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                }
            }).attr("index_value", function(d, i) {
                return "index-";
            })
            .on("mouseover", function(d, i) {
                var currMeasure = JSON.stringify($(this).attr("index_value"));
                currMeasure=currMeasure.replace("index-","")
                prevColor = color(i);
            if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var index=parseInt(this.id.split(":")[2]);
                    var barSelector = "." + "bars-Bubble-" + indexValue+div+measureArray[index].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                 var chartData = JSON.parse(parent.$("#chartData").val());
                    if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]==='N'){
                        show_details(d, columns, measureArray, this,div);
                }else{
                        d3.select(this).attr("stroke", "grey");
                        var content = "";
            if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
                            content += "</span><span class=\"name\"><font color='blue'><b>" + d[columns[0]] + "</b></font></span><br/>";
            }
    for (var i = 0; i < measureArray.length; i++) {
        if(measureArray[i].replace(/[^a-zA-Z0-9]/g, '', 'gi')!=currMeasure.replace(/[^a-zA-Z0-9]/g, '', 'gi')){
            continue;
        }
        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d[measureArray[i]], round, precition);
        }else {
            msrData = addCommas(d[measureArray[i]]);
        }
               content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>"; 
            }
                        return tooltip.showTooltip(content, d3.event);
              }
            }
            })
            .on("mouseout", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var index=parseInt(this.id.split(":")[2]);
                    var barSelector = "." + "bars-Bubble-" + indexValue+div+measureArray[index].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                     }
                hide_details(d, i, this);
            })
            }

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
    else{  
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    var boxW=width;
    var labelsArr=[],showViewBy=false;
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        showViewBy=false;
    }else{
        showViewBy=true;
    }
    var totalCharacters=0;
    for(var i in measureArray){
        var measureName='';
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);
        }
        labelsArr.push(measureName);
        totalCharacters+=measureName.length;
    }
var border=0;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
    border=4;
}
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
var rectW=boxW-100;
var rectX=10;
var rectH=15;
var i=0;
var currY=-23;
var currX=15;
var rowCount=1;
var maxX=currX;
if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
svg.append("rect")
    .attr("id",div+"_mainRect")
    .attr("x",6)
    .attr("y",currY+8)
    .attr("width",rectW)
    .attr("height",17)
    .attr("fill",backColor)
    .attr("rx",10)
    .attr("ry",10)
    .style("stroke-dasharray", ("3, "+border))
    .style("stroke", "grey");
}
if(showViewBy){
    svg.append("text")
    .attr("x",currX)
    .attr("y",currY+18)
    .text(columns[0]);
    currX+=(columns[0].length*7);
    currX+=10;
    if(currX>maxX){
        maxX=currX;
    }
}
for(i=0;i<labelsArr.length;i++){
    if(currX+(labelsArr[i].length*7)+20>rectW){
        rowCount++;
        currY+=14;
        currX=15;
    }
    svg.append("rect")
    .attr("x",currX)
    .attr("y",currY+11)
    .attr("width",8)
    .attr("height",8)
    .style("fill",function(){
                return getDrawColor(div, parseInt(i))
                });
    svg.append("text")
    .text(function(){
        var length=0;
        if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
            length=80;
        }else{
            length=chartData[div]["measureLabelLength"][measureArray[i]];
        }
        if(labelsArr[i].length>length){
            return labelsArr[i].substring(0, length)+"..";
        }else {
            return labelsArr[i];
        }
    })
    .attr("id",measureArray[i])
   .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
    .attr("x",currX+12)
    .attr("y",currY+18);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }else{
        var count=0;           
        var boxW=width;
        var boxH=height + margin.top + margin.bottom+30;
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
        var len=maxMeasure.length;
        if(columns[0].length+2>len){
            len=columns[0].length+2;
        }
        var rectW=0;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            rectW=boxW-15;
        }else{
            rectW=30+(len*7)+85
        }
        rectW = rectW<170?170:rectW;
        var viewByHgt=15;
        var rectH=0;  // horizontal stackedbar
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            rectH=17*(parseInt((measureArray.length)/3)+1);
}else{
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        rectH=10+(17*measureArray.length);
    }else{
            rectH=10+(17*measureArray.length)+viewByHgt;
        }
}
        var rectX;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            rectX=10;
        }else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright" ){
            rectX=boxW-rectW-10;
        }else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
            rectX=5;
        }else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
            rectX=boxW/2-rectW/2;
        }
        var rectY=0;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            rectY=boxH-(boxH*1.03)-30;
        }else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
            rectY=-5;
        }else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
            rectY=boxH-rectH-(boxH*0.27)-(6*measureArray.length);
        }
        var backColor;
        if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
            backColor=chartData[div]["lbColor"];
        }else{
            backColor="none";
        }
        var offset1=0;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            offset1=-10;
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
            .attr("x", rectX+offset1)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
        }
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}else{      
if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}else{
            svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+8)
            .attr("style","font-size:10px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);           
        }
        }
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            var xStep=rectW/3.3;
            var yStep=15;
            for(var i=0;i<=measureArray.length;i++){
                if(fromoneview!='null'&& fromoneview=='true'){
                    div=div1
                }
                var measureName='';
                svg.append("g")
                .attr("class", "y axis")
                .attr("id", "measure"+count)
                .append("text")
                .attr("x",rectX+20+(xStep*(i%3)))
                .attr("y",(rectY+13+(yStep*parseInt(i/3))))
                .attr("fill", function(d){
                    if(i==0){
                        return 'black';
                    }else{
                        getDrawColor(div, parseInt(i-1))
                    }
                })
                .text(function(d){
                    if(i==0){
                        return columns[0];
                    }
                    if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
                        return '';
                    }
                    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i-1]]!='undefined' && chartData[div]["measureAlias"][measureArray[i-1]]!=''){
                        measureName=chartData[div]["measureAlias"][measureArray[i-1]];
                        if(measureName==='undefined'){
                            measureName=measureArray[i-1];
                        }
                    }else{
                    measureName=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
                }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i-1]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i-1]] === "20") {
                    length=20;
                } else{
                    length=chartData[div]["measureLabelLength"][measureArray[i-1]];
                }
                if(measureName.length>length){
                    return measureName.substring(0, length)+"..";
                }else {
                    return measureName;
                }
                }).attr("svg:title",function(d){
                if(i==0){
                    return columns[0];
                }
                return measureName;
            })
            var rectsize;
            if(width<height){
                rectsize = parseInt(width/25);
            }else{
                rectsize = parseInt(height/25);
            }
            rectsize=rectsize>10?10:rectsize;
            if(i!=0){
                svg.append("g")
                .append("rect")
                .attr("x",rectX+3+(xStep*(i%3)))
                .attr("y",(rectY+5+(yStep*parseInt(i/3))))
                .attr("width", rectsize)
                .attr("height", rectsize)
                .attr("fill", getDrawColor(div, parseInt(i-1)))
            }
            count++
            }
    }else{
        for(var i in  measureArray){
            if(fromoneview!='null'&& fromoneview=='true'){
                div=div1
            }
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               viewByHgt=0;
           }
            var measureName='';
            svg.append("g")
            .attr("class", "y axis")
            .append("text")
            .attr("id", measureArray[i])
            .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]==='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
                    return '';
                }
                if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
                    measureName=chartData[div]["measureAlias"][measureArray[i]];
                }else{
                    measureName=checkMeasureNameForGraph(measureArray[i]);
                }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
                    length=20;
                }else{
                    length=chartData[div]["measureLabelLength"][measureArray[i]];
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
            .attr("x",rectX+10)
            .attr("y",(rectY+12+viewByHgt+count*15))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
            count++
   }}}
        }
        for(var z=0;z<measureArray.length;z++){
    if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
        if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[z]] === "undefined" || chartData[div]["createBarLine"][measureArray[z]] === "Yes")) {
            continue;
        } 
    }
    var sum = d3.sum(data, function(d,i) {
        return d[measureArray[z]];
    });
    svg.selectAll("labelText")
    .data(data).enter().append("text")
    .attr("transform", function(d) {
        var xvalue = parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measureArray.length*z)) +(x.rangeBand()/measureArray.length/2)+3;// x(d[measureArray[0]]);
        var yValue = (y(d[measureArray[z]])) < 15 ? y(d[measureArray[z]]) + 14 : y(d[measureArray[z]]) +37;
        if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom"){
            return "translate(-40," + xvalue + ")"; 
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBar"){
            return "translate("+(yValue-30)+"," + xvalue + ")";  
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnCenter"){
            return "translate(" + ((yValue/2)+2) + ","+xvalue+")";  
        }else{
            return "translate(" + (yValue) + ", " + xvalue + ")";
        }
    })
    .style("text-anchor", "middle")
    .attr("class", "valueLabel")
    .style("font-size",function(d, i){
        if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
            return (chartData[div]["labelFSize"]+"px");
        }else{
            return "8px";
        }
    })
    .style("transform-origin","bottom left 0px;font-weight:bold")
    .attr("onclick", hide)
    .attr("index_value", function(d, i) {
        return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
    .attr("id", function(d) {
        return d[columns[0]] + ":" + d[measureArray[z]];
    })
    .attr("fill", function(d,i){
        var LabFtColor="";
        if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[z]]!='undefined' && chartData[div]["LabFtColor"][measureArray[z]]!='undefined'){
            LabFtColor= chartData[div]["LabFtColor"][measureArray[z]]                   
        }else {
            LabFtColor = "#000000";
        }
        return LabFtColor;   
    })
    .text(function(d){
        var percentageValue=(d[measureArray[z]]/measureSum[counter1])*100;
        if(i==measureArray.length-1){
            counter1++;
        }
        if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[z]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[z]];
        }else{
            yAxisFormat="";
        }
        if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[z]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[z]];
        }else{
            yAxisRounding="0";
        } 
         var percentage = percentageValue;
         if (percentage <= 5.0){
                       return "";
                     }else{ 
        if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    if(typeof chartData[div]["dataDisplayArr"]=== "undefined" || typeof chartData[div]["dataDisplayArr"][measureArray[z]] == "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
               
               if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='Absolute'){
                       return addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                        }else {
                            return percentage.toFixed(1) + "%";
                        }
                 } else{
                            return "";
                        }
                    }else{
                       return "";  
                    }
                     }
        
    })
    .on("mouseover", function(d, i) {
        var meas = [];
        meas.push(measureArray[z]);
        if(fromoneview!='null'&& fromoneview=='true'){
            show_detailsoneview(d, columns, measureArray, this,chartData,div1);
        }else{
            show_details(d, columns, measureArray, this,div);
        }
    })
    .on("mouseout", function(d, i) {
        hide_details(d, i, this);
    });
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
        }
    }
for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
              .text(addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.9)
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
     }     }
//Added by Ashutosh
var slidercount=0;
if(typeof chartData[div]["dataSlider"]!=='undefined' && chartData[div]["dataSlider"]!=='undefined' && chartData[div]["dataSlider"]!==""){

var dataSlider=chartData[div]["dataSlider"];
var meassureIds = chartData[div]["meassureIds"];
var meassures=chartData[div]["meassures"];
var dataSliderMinMaxValue=chartData[div]["dataSliderMinMaxValue"];
$("#"+div).append("<div id='slideingdiv"+div+"'style='width: 95%;height: 70px;margin-top: -5%'>");
for (var l in dataSlider){

    if(dataSlider[l]==='Yes'){
        slidercount=slidercount+1;
        getSlider1(div,data,divWid, divHgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

    }
    
}    

}
    }
    