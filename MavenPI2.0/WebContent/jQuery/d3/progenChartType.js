var globalData;
function buildOverlaidBubble(div, data, columns, measureArray,width,height){
 var color = d3.scale.category10();
var dashletid;
var chartData = JSON.parse(parent.$("#chartData").val());
 var colIds= [];
  var widthdr=width-10;
    var divHgtdr=height;
 var fromoneview=parent.$("#fromoneview").val()
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
dashletid=div;
//Commented by Ram
//width=parseFloat($(window).width())*(.35);
colIds=chartData[div1]["viewIds"];
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
    //Commented by Ram
//width=parseFloat($(window).width())*(.35);
  }
    var customTicks = 5;


height=height*.67;
var measure1 =[];
var measure2 =[];
var measArr = [];
measure1.push(measureArray[0]);
 var fun = "drillWithinchart(this.id,\""+div+"\")";
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
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
    }
if(measureArray.length>1)
{
measure2.push(measureArray[1]);
}
else{
measure2.push(measureArray[0]);
}
 var autoRounding1;
    var autoRounding2;
         if(fromoneview!='null'&& fromoneview=='true'){
         }else{
             if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="") {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
         }
    var max1 = maximumValue(data, measure1);
var max2 = maximumValue(data, measure2);
    var minVal1 = minimumValue(data, measure1);
    var minVal2 = minimumValue(data, measure2);
var x = d3.scale.ordinal()
    .rangeRoundBands([0, width], .2);

var y = d3.scale.linear().domain([minVal1, max1]).range([height, 0]);
var y1 = d3.scale.linear().domain([minVal2, max2]).range([height, 0]);
//var y = d3.scale.linear()
//    .rangeRound([height, 0]);

//add condition by mayank sh. for hidden white space
var margin = {top: 20, right: 12, bottom: 80, left: 70 };
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = { top: 2,  right: 18,  bottom: 20, left: 20  };
}
 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
          height   = divHgtdr*.90  ;
}else{
    height   = divHgtdr*.73  ;
}
//end condition by mayank sh. for hidden white space


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
var yCost = d3.scale.linear()
     .rangeRound([height, 0]);

//var color = d3.scale.ordinal()
//    .range(["#d0743c", "#ff8c00"]);

var xAxis =d3.svg.axis()
    .scale(x)
    .orient("bottom");




//    .tickFormat(d3.format(".2s"));

if (isFormatedMeasure) {
     var   yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d) {

                    return numberFormat(d, round, precition);
                });

    }
    else {

       var    yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
          if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });
    }
    var    yAxisRight = d3.svg.trendaxis()
                .scale(y1)
                .orient("right")
                .tickFormat(function(d, i) {
          if(typeof displayY1 !=="undefined" && displayY1 =="Yes"){
                  if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });

     var yAxis1 = d3.svg.axis1()
            .scale(y)
            .tickFormat(function(d, i) {
                measArr.push(d);

                return "";
            });

//var yCostAxis = d3.svg.trendaxis()
//    .scale(yCost)
//    .orient("right").tickFormat(function(d, i) {
//        return d;
////                    return autoFormating(d, autoRounding1);
//                });
//    .tickFormat(d3.format(".0f"));

// Define the line
var valueline = d3.svg.line()
    .x(function(d) {return x(d[columns[0]]) +15})
    .y(function(d) {return yCost(d[measure2[0]]);});
//    .interpolate("monotone");
if(fromoneview!='null'&& fromoneview=='true'){
div=dashletid;
}
//var svg = d3.select("#"+div).append("svg")
//    .attr("id", "svg_" + div)
//    .attr("width", width + margin.left + margin.right + 46)
//    .attr("height", height + margin.top + margin.bottom)
//  .append("g")
//    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

// Aedded by Ram for Overlaid
var svg = d3.select("#" + div)
            .append("svg")
    .attr("id", "svg_" + div)
        //    .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height)+" ")
        .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgtdr)+" ")
            .classed("svg-content-responsive", true)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//d3.json("../json/Bubble.json", function(error, data) {
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

     y = d3.scale.linear().domain([minVal1, max1]).range([height, 0]);
     yCost = d3.scale.linear().domain([minVal2, max2]).range([height, 0]);

      svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
  var  minVal = measArr[0] + diffFactor;
    y.domain([minVal, max1]);
//    y.domain([0, 8000000]);
//    yCost.domain([0, 13103254]);

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
}//add condition by mayank sh. for display properties

if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
  //add condition by mayank sh. for display properties
svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
    .selectAll("text")
        .attr("y", 9)
        .attr("x", 0)
        .attr("dy", ".71em")
         .text(function(d,i) {
                return buildXaxisFilter(div,d,i);

//                if (d.length < 15){
//                    return d;
//                }
//                else {
//                    return d.substring(0, 8) + "..";
//                }

            })
        .attr("transform", "rotate(-45)")
        .style("text-anchor", "end"); }

if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis);
}
//    .append("text")
//            .attr("y", 10)
//            .attr("dy", "-2em")
//            .attr("dx", "2em")
//            .attr("fill", getDrawColor(div, 1))
//            .style("text-anchor", "middle")
//            .style("font-weight", "bold")
//            .text(measure1);
//}
//  svg.append("g")
//      .attr("class", "y axis")
//      .attr("transform", "translate(" + width + ",0)")
//      .call(yAxisRight)
//    .append("text")
//            .attr("y", 10)
//            .attr("dy", "-2em")
//            .attr("dx", "-6em")
//            .attr("fill", getDrawColor(div, 0))
//            .style("text-anchor", "middle")
//            .style("font-weight", "bold")
//            .text(measure2);

svg.selectAll(".property").data(data)
            .enter().append("g")
            .attr("class", "bar")
            .append("rect")
      .attr("width", x.rangeBand() )
      .attr("id", function(d,i){return d[columns[0]] + ":" + d[measure1];})
      .attr("x", function(d) {
                return x(d[columns[0]]);
            })
      .attr("y", function(d) {
                return y(d[measure1[0]]);
            }).attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("height", function(d) {
                return height - y(d[measure1[0]]);
            }) .attr("onclick", fun)
      .style("fill", getDrawColor(div, 1))
       .on("mouseover", function(d, i) {
//             show_details(d, columns, measureArray, this,div);
//            })  .on("mouseover", function(d, i) {
//
                prevColor = color(1);

if(fromoneview!='null'&& fromoneview=='true'){
                    show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
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
                    selectedBar.style("fill", getDrawColor(div, 1));
                     }
//                }
                hide_details(d, i, this);
            })



     var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
             .attr("class", "circle")
            .attr("r", function(d){
                return x.rangeBand() /2;
            })
            .attr("cx", function(d) {
                return x(d[columns[0]]) + x.rangeBand() /2 ;
            })
            .attr("cy", function(d,i) {
                return y(d[measure2]) - x.rangeBand() /2 - (i * (x.rangeBand() /2 * .01)) ;
            })
            .style("fill", getDrawColor(div, 0))
            .style("stroke", function(d, i) {
                  return getDrawColor(div, 0);
            })
            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure2];
            })
            .attr("onclick", fun)
          
            .on("mouseover", function(d, i) {
                  if(fromoneview!='null'&& fromoneview=='true'){
                      show_details(d, columns, measureArray, this,div1);
                }else{
                show_details(d, columns, measureArray, this,div);
                }
//                show_details(d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
            
            var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var textLabel = blueCircles.append("g");
      textLabel.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("x",function(d){
                return 0;
            })
            .attr("y", function(d){
                return 0;
            })
            .attr("font-family", "Verdana")
            .attr("font-size", "10pt")
            .attr("fill",  function(d, i){
                var lableColor;
                if (typeof chartData[div]["labelColor"]!=="undefined") {
                    lableColor = chartData[div]["labelColor"];
                //                               return "white";
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
            .text(function(d) {
                   if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                    }else{
                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }else {return "";}
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
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
                .attr("fill", function(d,i){
                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
                        if(parseFloat(d[measure1]) >= max){
                            return "white";
                        }
                    }

                    else {
                    return labelColor;
                    }
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
if(fromoneview!='null'&& fromoneview=='true'){
                     show_details(d, columns, measureArray, this,div);
                }else{
                show_details(d, columns, measureArray, this,div);
                }
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

//});
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{ 
count=0;
var boxW=width + margin.left + margin.right;
var boxH=height + margin.top + margin.bottom+ 17.5 ;
var rectW=150+boxW*0.17;
var viewByHgt=15;
var rectH=10+(17*measureArray.length)+viewByHgt;
var rectX=boxW-rectW;
var rectY=boxH-(boxH*1.03)-5;
var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
//alert((boxH-(boxH*.98)-5)+":"+boxW);
svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .style("stroke", "grey")
            .style("stroke-dasharray", ("3, 4"))
//            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
            .attr("x", rectX)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
      svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+25)
            .attr("style","font-size:10px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);      
for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
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
         var measureName='';
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!=''){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=measureArray[i];
        }
                if(measureName>20){
                    return measureName.substring(0, 20)+"..";
                }else {
                    return measureName;
          }
           }).attr("svg:title",function(d){
               return measureArray[i];
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
   }}
   }


function buildDualAxisBarNew(div, data, columns, measureArray, divWid, divHgt,KPIresult) {
      var color = d3.scale.category10();
var dashletid;
//divWid=parseFloat($(window).width())*(.45);
 
var measure1 ;
var measure2;
var temp = [];
var measuretemp1 = [];
var measuretemp2 = [];
 var chartData = JSON.parse(parent.$("#chartData").val());
  var colIds= [];
 var fromoneview=parent.$("#fromoneview").val();
   var div1=parent.$("#chartname").val()
    if(fromoneview!='null'&& fromoneview=='true'){
  dashletid=div;
  div=div1;
    }else{
      
       var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];

    }
    var customTicks = 5;
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var customTicks2 = 8;
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisTicks1"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks1"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks2 = chartData[div]["yaxisrange"]["axisTicks1"];
 }
 if(typeof chartData[div]["measures1"]!= "undefined"){
 measure1 = chartData[div]["measures1"][0];

 for(var i in chartData[div]["measures1"]){

 measuretemp1.push(chartData[div]["measures1"][i])
 temp.push(chartData[div]["measures1"][i])

 }

 }
 else{

  measure1 = measureArray[0];
 }

 if(typeof chartData[div]["measures2"] != "undefined"){
 measure2 = chartData[div]["measures2"][0];
 for(var t in chartData[div]["measures2"]){
 measuretemp2.push(chartData[div]["measures2"][t])
 temp.push(chartData[div]["measures2"][t])

 }
 }
 else if(measureArray.length === 1){
  measure2 = measureArray[0];
 } else {
        measure2 = measureArray[1];
    }

if(temp!=""){
   measureArray = [];
   for(var u in temp){
     measureArray.push(temp[u]);
   }

}
  if(fromoneview!='null'&& fromoneview=='true'){
      var prop = graphProp(div1);
      if(typeof chartData[div]["rounding1"]!=="undefined"){
        y2AxisRounding=chartData[div]["rounding1"];
  }else{
       y2AxisRounding=0;
    }
  }else{
var prop = graphProp(div);

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
  }
    var margin = {
        top: 25,
        right: 80,
        bottom: 55,
        left: 47
    };
    var marginleft1 = 70;
    var width = divWid ;
     var height = divHgt- margin.top - margin.bottom ;
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var hide = "hideLabels(this.id,\""+div+"\")";
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
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
    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .2);

     var max = 0;
var minVal = 0 ;
var y0;
var y1;
//    if(typeof chartData[div]["measures1"]!= "undefined" ){

if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
if(measuretemp1.length>0){
for(var i in measuretemp1){


var max1 = parseFloat(maximumValue(data, measuretemp1[i]));
//var min = parseFloat(minimumValue(data, measuretemp1[i]));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}

}
}else{
  for(var i in measureArray){


var max1 = parseFloat(maximumValue(data, measure1));
//var min = parseFloat(minimumValue(data, measure1));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}

}
}}
}
//end of max for meas1
else{
  for(var i in measureArray){


var max1 = parseFloat(maximumValue(data, measure1));
//var min = parseFloat(minimumValue(data, measure1));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}

}
   }
//end of default max
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisMin"]!="") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }else{
  for(var i in measureArray){


//var max1 = parseFloat(maximumValue(data, measure1));
var min = parseFloat(minimumValue(data, measure1));


if(i==0)  {

//max=parseFloat(max1);
minVal=parseFloat(min);
}

//else if(i>0 && (max1 > max)){
//
//max = max1;
//}
else if(i>0 && ( min<minVal)){

minVal = min;
}

}
}
}
else{

minVal = 0;
}
//add condition by mayank sh. for hidden white space
 y0 = d3.scale.linear().domain([minVal, max]).range([height, 0]);

if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
    y0 = d3.scale.linear().domain([minVal, max]).range([height+56, 0]);
}
  //end  by mayank sh. for hidden white space
//
//measurearaary 2

if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax1"]!="undefined" && chartData[div]["yaxisrange"]["axisMax1"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax1"]);
}else{
if(measuretemp2.length>0){
for(var i in measuretemp2){


var max1 = parseFloat(maximumValue(data, measuretemp2[i]));
var min = parseFloat(minimumValue(data, measuretemp2[i]));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}

}
}else{
  for(var i in measureArray){


var max1 = parseFloat(maximumValue(data, measure2));
//var min = parseFloat(minimumValue(data, measure1));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}

}
}}
}
//end of max for meas1
else{

  for(var i in measureArray){


var max1 = parseFloat(maximumValue(data, measure2));
//var min = parseFloat(minimumValue(data, measure1));


if(i==0)  {

max=parseFloat(max1);
//minVal=parseFloat(min);
}

else if(i>0 && (max1 > max)){

max = max1;
}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}

}
   }
//end of default max

 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin1"]!="undefined" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && chartData[div]["yaxisrange"]["axisMin1"]!="") {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin1"]);

 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;

 }else{

 for(var i in measureArray){
var min = parseFloat(minimumValue(data, measure2));
if(i==0)  {
minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){

minVal = min;
}

}
}
}
    else{

minVal = 0;
}

//add condition by mayank sh. for hidden white space
y1 = d3.scale.linear().domain([minVal, max]).range([height, 0]);

if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
    y1 = d3.scale.linear().domain([minVal, max]).range([height+56, 0]);
}


 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
                height   = divHgt*.94;
 }

 if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]=="No"){
     margin = { top: 0,  right: 18,  bottom: 20, left: 20  };
}else{
    margin = {top: 25, right: 65, bottom: 30, left: 47 };
  }

  if(typeof chartData[div]["displayY1"]!="undefined" && chartData[div]["displayY1"]!="" && chartData[div]["displayY1"]=="No"){
     margin = { top: 0,  right: 18,  bottom: 20, left: 20  };
}else{
    margin = {top: 25, right: 80, bottom: 30, left: 35 };
  }
//end condition by mayank sh. for hidden white space


    make_x_axis = function() {
    return d3.svg.axis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}

 make_y_axis = function() {
    return d3.svg.axis()
        .scale(y0)
        .orient("left")
        .ticks(customTicks)
}
 make_y2_axis = function() {
    return d3.svg.axis()
        .scale(y1)
        .orient("left")
        .ticks(customTicks2)
}
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    // create left yAxis
    var yAxisLeft, yAxisRight;
    if (isFormatedMeasure) {
        yAxisLeft = d3.svg.tildaxis().scale(y0) .ticks(customTicks).orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(customTicks).orient("right")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });
    } else {
        yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding1);
//                return addCommas(d);
      if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(customTicks2).orient("right")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding2);
//                    return addCommas(d);
      if(yAxisFormat==""){
                        return addCommas(numberFormat(d,y2AxisFormat,y2AxisRounding,div));
                    }
            else{
                    return numberFormat(d,y2AxisFormat,y2AxisRounding,div);
                }
                });
    }


//    for(var k=0;k<measureArray.length;k++){
//var measNo=measureArray.length;
 if(fromoneview!='null'&& fromoneview=='true'){
div=dashletid
 }
 var offset=0;
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
     offset=height/11;
 }
  var  svg = d3.select("#" + div)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +height/3)+" ")
            .classed("svg-content-responsive", true)
			//.attr("width", divWid+20)
           // .attr("height", height + margin.top + margin.bottom +5)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + (margin.left +10)+ "," + (margin.top+offset) + ")");

    var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientM1")
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "50%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "steelblue")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", "steelblue")
            .attr("stop-opacity", 1);

    var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientM2")
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "50%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(102, 124, 38)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", "rgb(102, 124, 38)")
            .attr("stop-opacity", 1);

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
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
if(fromoneview!='null'&& fromoneview=='true'){
div=parent.$("#chartname").val()
 }


 //add condition by mayank sh. for display properties
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
     svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )}

//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
}else{
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i)
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
            return  10;
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
    });
            }

if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
}else{ //add condition by mayank sh. for display properties
   svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft)
            .append("text")
            .attr("x",0)
            .attr("y", 10)
            .attr("dy", "-2em")
            .attr("fill", "steelblue")
            .style("text-anchor", "middle")
            .style("font-weight", "bold")
}

if(typeof chartData[div]["displayY1"]!="undefined" && chartData[div]["displayY1"]!="" && chartData[div]["displayY1"]!="Yes"){

}else{  //add condition by mayank sh. for display properties
    svg.append("g")
            .attr("class", "y axis axisRight")
            .attr("transform", "translate(" + (width) + ",0)")
            .call(yAxisRight)
            .append("text")
            .attr("y", 10)
            .attr("dy", "-2em")
            .attr("dx", "-6em")
            .attr("fill", "rgb(102, 124, 38)")
            .style("text-anchor", "middle")
            .style("font-weight", "bold");
//            .text(measure2);
} //end  by mayank sh. for hidden white space

//var len = 0;
//var yvalue = -75;
//var dyvalue = ".41em";
//var counts = 0;
//var transform = "";
// for(var i in  measureArray){
//    if(counts % 2==0){
//        if(len==0){
//            len=50;
//            yvalue=500;
//        dyvalue = -150;
//        }else{
//        len += 150;
//        yvalue=500;
//        dyvalue = -150;
//     }
//    }else {
//        len +=150;
//        yvalue=518;
//         dyvalue = -150;
//     }
//  svg.append("g")
////  .attr("transform", "translate(0,0)")
////            .call(yAxisRight)
//            .append("text")
//            .attr("transform", transform)
//            .attr("x",len + measureArray[i].length)
//            .attr("y",150)
//            .attr("fill", getDrawColor(div, parseInt(i)))
////            .style("stroke", color(i))
//            .attr("dy",dyvalue )
//            .style("text-anchor", "middle")
//            .style("font-weight", "bold")
//            //          .attr("transform", "rotate(-90)")
//            //          .attr()
//                  .text(function(d){
//                if(measureArray[i].length>25){
//                    return measureArray[i].substring(0, 25);
//                }else {
//                    return measureArray[i];
//          }
//           })
//              counts++
//
//
//   }
var count = 0;
var measBar = [];
var measLine = [];
var chartList = [];
if(typeof chartData[div]["chartList"]!="undefined"){
    for(var l in chartData[div]["chartList"]){
      if(chartData[div]["chartList"][l]==="Line"){
      count++;
      measLine.push(measureArray[l])
      chartList.push(chartData[div]["chartList"][l])
  }
else{
    measBar.push(measureArray[l])
}
}}

var measNo1=measureArray.length;
measNo1 = (parseFloat(measNo1)-parseFloat(count));

    if(typeof chartData[div]["chartList"]!="undefined"){

    for(var j=0;j<measBar.length;j++){
   bars = svg.selectAll(".bar"+j).data(data).enter();
    bars.append("g")
            .attr("class", "bar"+j)
            .append("rect")
            .attr("class", "bar")
            .attr("x", function(d) {

                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*j));
            })
            .attr("width", x.rangeBand() / measNo1)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measBar[j]];
            })
            .attr("onclick", fun)
            .attr("y", function(d) {
                return y0(d[measBar[j]]);
            })
            .attr("height", function(d, i, m) {
                return height - y0(d[measBar[j]]);
            })
            .attr("fill", function(d, i) {

                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                   colorShad = getDrawColor(div, parseInt(j));
               }

                return colorShad;


            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//                    selectedBar.style("fill", drillShade);
//                }
                var meas = [];
                meas.push(measBar[j]);
                if(fromoneview!='null'&& fromoneview=='true'){
show_detailsoneview(d, columns, measBar, this,chartData,div1)
 }else{
                show_details(d, columns, measBar, this,div);
 }
            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//
//                    var colorValue = selectedBar.attr("color_value");
//                    selectedBar.style("fill", colorValue);
//                }
                hide_details(d, i, this);
            });
            var sum = d3.sum(data, function(d) {
        return d[measureArray[1]];
    });
                if (x.rangeBand() >= 20) {
        svg.selectAll(".bar"+j)
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*j);// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                    var yValue = (y0(d[measBar[j]])) < 15 ? y0(d[measBar[j]]) + 14 : y0(d[measBar[j]]);
                    return "translate(" + xvalue + ", " + (yValue) + ")";
                })
                .attr("text-anchor", "right")
                .attr("class", "valueLabel")
                .attr("onclick", fun)
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
                .attr("fill", function(d,i){
                    var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
//                               return "white";
                          }else {
                               lableColor = "#000000";
//                               return "black";
                               }
                               return lableColor;
                })
                .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                       return addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,div));
                    }else{
                    var percentage = (d[measureArray[j]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }else {return "";}
//        return "" + autoFormating(d[measureArray[0]]);
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
//            if (toolTipType === "customize") {
//                for (var num in toolTipData) {
//                    if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                        show_details(toolTipData[num], custToolTipViewBy, custToolTipMeasure, this);
//                        break;
//                    }
//                }
//            } else {
if(fromoneview!='null'&& fromoneview=='true'){
                     show_details(d, columns, measureArray, this);
                }else{
                show_details(d, columns, measureArray, this);
                }
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
            }
  for(var s=0;s<measLine.length-1;s++){
  if(chartList[s]!="Bar"){
 var valueline = d3.svg.line()
    .x(function(d) {return x(d[columns[0]])+30;})
    .y(function(d) {
        var target =0;
       
        
    if(d[measLine[s]].split(".")[0] == 0){
                   target = KPIresult[measureArray.length-1];
                }else{
                target = d[measLine[s]];    
                }
               
return y1(target);});

//  var valuelineH = d3.svg.line()
//            .x(function(d,i) {
//               if(i==0){
//               return x(d[columns[0]]) - x(0);
//               }else{
//                   return x(d[columns[0]]) + x(i.length);
//               }
//            })
//            .y(function(d,i) {
//                var target = 0;
//               
//                alert("ssass: "+d[measLine[s]].split(".")[0])
//               
//                if(d[measLine[s]].split(".")[0] == 0){
//                   target = KPIresult[5];
//                }
//                alert(target)
//                return y1(target);
//                
//            });
//var line..vv..
//    line = svg.selectAll(".line"+s).data(data).enter();
var path =    svg.append("path")
        .data(data)
     .attr("class", "lineDual")
     .attr("d", valueline(data))
//     attr("fill", color(i)).style("stroke-width", "3px")
                .style("stroke", getDrawColor(div, parseInt(s+6)))
     .attr("id", function(d,i){return d[columns[0]] + ":" + d[measLine[s]];})
     .attr("fill", "transparent")
     .attr("stroke-width", "3")
     .on("click",fun);
   
//  var path =   svg.append("path")
//            .data(data)
//            .attr("d", valuelineH(data))
//            .attr("fill", "transparent")
//            .style("z-index", "9999")
//            .style("stroke-width", "1.5px")
//            .style("stroke", "black");
var totalLength = path.node().getTotalLength();
//
    path
      .attr("stroke-dasharray", totalLength + " " + totalLength)
      .attr("stroke-dashoffset", totalLength)
      .transition()
        .duration(2000)
        .ease("linear")
        .attr("stroke-dashoffset", 0);

  var sum = d3.sum(data, function(d) {
        return d[measLine[s]];
    });
svg.selectAll("labelText")
             .data(data).enter().append("text")
              .attr("id", function(d,i){
                       return d[columns[0]] + ":" + d[measLine[s]];
                   })
//            .attr("d", valueline(data))
                   .attr("x", function(d){
                       return x(d[columns[0]]) +15;
                   } ).attr("y", function(d){
                       var target =0;
    if(d[measLine[s]].split(".")[0] == 0){
                   target = KPIresult[measureArray.length-1];
                }else{
                target = d[measLine[s]] -10;    
                }
return y1(target);;
                   })

      .attr("dy", ".35em").text(function(d)
                {
//                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//
//                    if(typeof displayType=="undefined" || displayType==="Absolute"){
//                        if(typeof chartData[div]["hideLabel"] != "undefined"){..
//                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][s] == "Yes"){
//                           return numberFormat(d[measLine[s]],yAxisFormat,yAxisRounding);
//
//                }else {
//                    return "";
//                }}else {
//                           return numberFormat(d[measLine[s]],yAxisFormat,yAxisRounding);
//                }
////                       return numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding);
//                    }else{
//                       if(typeof chartData[div]["hideLabel"] != "undefined"){
//                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][s] == "Yes"){
//                    var percentage = (d[measLine[s]] / parseFloat(sum)) * 100;
//                    return percentage.toFixed(1) + "%";
//
//                }else {
//                    return ""
//                    ;
//                }}else {
//                    var percentage1 = (d[measLine[s]] / parseFloat(sum)) * 100;
//                    return percentage1.toFixed(1) + "%";
//                }
//            }
//            }else {return "";}
            if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                      if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       return addCommas(numberFormat(d[measLine[s]],yAxisFormat,yAxisRounding,div)) + "%";
                        }else {
                             return addCommas(numberFormat(d[measLine[s]],yAxisFormat,yAxisRounding,div));
                        }
                    }else{
                    var percentage = (d[measLine[s]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
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
           })

               .attr("onclick",hide);

     var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return x(d[columns[0]])+30;
            })
            .attr("cy", function(d) {
                  var target =0;
    if(d[measLine[s]].split(".")[0] == 0){
                   target = KPIresult[measureArray.length-1];
                }else{
                target = d[measLine[s]];    
                }
return y1(target);
              
            })
            .style("fill", function(d,i){
                return getDrawColor(div, parseInt(s+6));
            })
            .style("stroke", function(d, i) {
                  return getDrawColor(div, parseInt(s+6));
            })


            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measLine[s]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                 if(fromoneview!='null'&& fromoneview=='true'){
show_detailsoneview(d, columns, measLine, this,chartData,div1);
 }else{
                show_details(d, columns, measLine, this,div);
 }
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}
    }


}
        else{
            for(var k=0;k<measureArray.length;k++){
    bars = svg.selectAll(".bar"+k).data(data).enter();
    bars.append("rect")
            .attr("class", "bar")
            .attr("x", function(d) {

                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*k));
            })
            .attr("width", x.rangeBand() / measNo1)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[k]];
            })
            .attr("onclick", fun)
            .attr("y", function(d) {
                return y0(d[measureArray[k]]);
            })
            .attr("height", function(d, i, j) {
                return height - y0(d[measureArray[k]]);
            })
            .attr("fill", function(d, i) {

            return getDrawColor(div, parseInt(k));
            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//                    selectedBar.style("fill", drillShade);
//                }
                var meas = [];
                meas.push(measureArray[k]);
                 if(fromoneview!='null'&& fromoneview=='true'){
show_detailsoneview(d, columns, measureArray, this,chartData,div1);
 }else{
                show_details(d, columns, measureArray, this,div);
 }
            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//
//                    var colorValue = selectedBar.attr("color_value");
//                    selectedBar.style("fill", colorValue);
//                }
                hide_details(d, i, this);
            });
            }
        }
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            var boxW=(divWid + margin.left + margin.right);
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
svg.append("rect")
    .attr("id","mainRect")
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
    svg.append("text")
    .text(labelsArr[i])
    .attr("x",currX+15)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#mainRect").attr("height",rowCount*15);    
d3.select("#mainRect").attr("width",maxX);    
    }
   else 
    {
var boxW=divWid + margin.left + margin.right;
var boxH=height + margin.top + margin.bottom;
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
    rectW=30+(len1*7)+85;
}

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
    rectY=boxH-(boxH*1.03)-35;
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
var offset1=20;
if(typeof chartData[div]["chartList"]!=='undefined'){
    offset1=0;
}
if(typeof chartData[div]["lbPosition"] === 'undefined' || chartData[div]["lbPosition"]==='top'){
   if(typeof chartData[div]["chartList"]==='undefined'){
        offset1=0;
   }
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
if(typeof chartData[div]["lbPosition"] === 'undefined' || chartData[div]["lbPosition"]==='top' ){
}
else{
    var offset2=offset1;
    if(typeof chartData[div]["chartList"]!=='undefined'){
        offset2=17;
    }
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
    }
    else{
        
      svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:12px")
            .attr("y",(rectY+offset2))
            .attr("fill", 'black')
            .text(columns[0]);      
}
}
if(typeof chartData[div]["lbPosition"] === 'undefined' || chartData[div]["lbPosition"]==='top' ){
    var xStep=rectW/3.3;
    var yStep=15;
    
for(var i=0;i<=measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
          var measureName='';
   svg.append("g")
            .attr("class", "y axis")
            .append("text")
            .attr("x",rectX+25+(xStep*(i%3)))
            .attr("y",(rectY+13+offset1+(yStep*parseInt(i/3))))
            .attr("fill",function(d){
                if(i==0){
                    return 'black';
                }
                else{
                getDrawColor(div, parseInt(i-1))
                }
                } )
            .style("font-size","11px")
            .text(function(d){
                if(i==0){
                return columns[0];
            }
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i-1]]!='undefined' && chartData[div]["measureAlias"][measureArray[i-1]]!='' && chartData[div]["measureAlias"][measureArray[i-1]]!== measureArray[i-1]){
            measureName=chartData[div]["measureAlias"][measureArray[i-1]];
            if(measureName==='undefined'){
                measureName=measureArray[i-1];
            }
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
        }
                if(measureName.length>20){
                    return measureName.substring(0, 20)+"..";
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
            .attr("x",rectX+10+(xStep*(i%3)))
            .attr("y",(rectY+5+offset1+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
   }
              count++
   }
}
else{
if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
    viewByHgt=0;
}   
for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+25)
            .attr("y",(rectY-3+offset1+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!=''){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
        }
                if(measureName.length>20){
                    return measureName.substring(0, 20)+"..";
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

            .attr("x",rectX+10)
            .attr("y",(rectY-11+offset1+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
              count++
   }
}
}
}
//            var htmlstr = "";
//htmlstr += "<table>";
//htmlstr += "<tr>";
//htmlstr += "<td onclick='editNewCharts(\"" + div + "\",\""+columns+"\",\""+measureArray+"\",\""+measure1+"\",\""+measure2+"\")'>edit Measures </td>";
//htmlstr += "</tr>";
//htmlstr += "</table>";
//$('#'+div).append(htmlstr)


}

function buildOverlayDonut(div,divId, data, columns, measureArray, divWidth, divHeight, rad) {
    var color = d3.scale.category10();
     if(divId == "GraphDonut"){
        divId = div;
    }else{
        divId = divId;
    }
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    rad=(Math.min((divWidth*.7), divHeight))*.50;
        var chartData = JSON.parse(parent.$("#chartData").val());
 var width1 = divWidth;
    var widthdr=divWidth
    var divHgtdr=divHeight
    var fromoneview= parent.$("#fromoneview").val();
//        var chartData = JSON.parse(parent.$("#chartData").val());
var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
divWidth=divWidth;
}else{
    var prop = graphProp(div);

//    divWidth=parseFloat($(window).width())*(.35);
}
//    divWidth=parseFloat($(window).width())*(.35);
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();
 var regid=div.replace("Dashlets-","");
  fun = "drillWithinchart11(this.id,\""+div+"\",\""+widthdr+"\",\""+divHgtdr+"\",\""+regid+"\",\""+oneviewid+"\",\""+repname+"\",\""+repId+"\",\""+div1+"\",'null','null')";
    if(div=='OLAPGraphRegion'){
fun = "viewAdhoctypes(this.id)";
    }
    }
    var width = divWidth;
    var margintop;
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var margintop;
var radius;
     if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "0px";

        radius = rad;
    }
    else {
        height = divHeight;
        margintop = "-60px";
        radius = rad / 1.2;
    }
    //Math.min(width, height) / 3 ;
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var pie = d3.layout.pie() //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
    var svg = d3.select("#" + divId)
           //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")

            //.attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width)+" "+(height )+" ")
            .classed("svg-content-responsive", true)
            .datum(data)
//            .attr("width", width-150)
//            .attr("height", height-50)
            .attr("style", margintop);
//            alert(div);
//    var remarks;
//    if(typeof chartData[div]["remarks"]!=="undefined" && chartData[div]["remarks"]!=="" )
//    {
//        remarks=chartData[div]["remarks"];
//
//    }
//    else
//    {
//        remarks='No remarks for this chart';
//    }
//
//    var div1=svg.append("foreignObject")
//        .attr("width", width)
//        .attr("height", 35)
//        .attr('x', 0)
//        .attr( 'y', height-45)
//        .append("xhtml:body")
//        .attr('xmlns','http://www.w3.org/1999/xhtml')
//        .html("<input id='txt"+div+"' type='text' style='float:left;height: "+height/20+"px;width: "+(width-40)+"px;display:none' value='"+remarks+"'/><label id='lbl"+div+"'  style='background-color:#F2F2F2;color:black;font-size:10px;text-align:left;height: "+height/20+"px;width: "+(width)+"px; ' value='"+remarks+"'><a style='margin-right:20px;float:right;width:16px;' onclick='editRemarks(\""+div+"\")' class='ui-icon ui-icon-pencil'></a>"+remarks+"</label>")
//        .on("change", function() {displayText('txt'+div,div)});
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
//        var drilledvalue = parent.$("#drilledValue").val().split(",");
                var drilledvalue = "";
                var colorShad;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
                } catch (e) {
                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                }
//                else
//                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                }
            else {
                    colorShad = color(i);
                }
//                chartMap[d[columns[0]]] = colorShad;
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", width-180)
//            .attr("height", height-50)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("class", "arc")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
            .attr("onclick", fun)
            .attr("transform", "translate(" + width / 3 + "," + height / 2.4 + ")");
    arcs.append("path")
            .attr("fill", function(d,i) {
var colorShad;
             var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                }
                else{
                    colorShad=color(i);
                }
                return colorShad;
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
var colorShad;
             var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                }
                else{
                    colorShad=color(i);
                }
                return colorShad;
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
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
//                }

                show_details(d.data, columnList, measureArray, this,div);
}

            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
if(fromoneview!='null'&& fromoneview=='true'){}else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", color(i));
}
//                }
                hide_details(d, i, this);
            })
            .transition()
            .ease("bounce")
            //.delay(function(d, i) { return 500 + i * 50; })
            .duration(2000)
            .attrTween("d", tweenDonut);
    function angle(d) {
        var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
        return a;
    }

    //    if(typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"  )){
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
//    .text(function(d) {
//        if (parent.$("#isDashboard").val() === "true" || (typeof parent.$("#dashBoardType").val() !== "undefined" && parent.$("#dashBoardType").val() === "drilldash") || parent.$("#chartType").val() === 'KPIBar') {
//            //            var percentage = (d.value / sum) * 100;
//            var percentage = (d.value /parseFloat(parent.$("#sumValue").val())) * 100;
//            return percentage.toFixed(1) + "%";
//        }
//        else {
//            if (parent.$("#legends").val() === "No") {
//                return "";
//            }
//            else{
////                if (parent.$("#legends").val() === "Side") {
//                var legends = [];
//                data.forEach(function(d) {
//                    if (legends.indexOf(d[columns[0]]) === -1)
//                        legends.push(d[columns[0]])
//                })
//                svg.selectAll(".circle")
//                .data(legends).enter().append("svg:circle") // Append circle elements
//                .attr("cx", width - 190)
//                .attr("cy", function(d, i) {
//                    return (90 + i * 20 + 5);
//                })
//                .attr("stroke-width", ".5")
//                .style("fill", function(d, i) {
//                    var colorShad;
//                    if(typeof centralColorMap[d.toString().toLowerCase()]!=="undefined"){
//                colorShad=centralColorMap[d.toString().toLowerCase()];
//              }else{
//                colorShad=color(i);
//              }
//                    return colorShad;
//                }) // Bar fill color
//                .attr("index_value", function(d, i) {
//                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//
//                .attr("class", function(d, i) {
//                    return "bars-Bubble-legendBullet-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("r", 5)
//                .attr("color_value", function(d, i) {
//                    var colorShad;
//                    if(typeof centralColorMap[d.toString().toLowerCase()]!=="undefined"){
//                colorShad=centralColorMap[d.toString().toLowerCase()];
//              }else{
//                colorShad=color(i);
//              }
//                    return colorShad;
//                })
//                svg.selectAll("g.legend")
//                .data(legends) // Instruct to bind dataSet to text elements
//                .enter().append("g") // Append legend elements
//                //.attr("xlink:href", function(d) { return d.link; })
//                .append("text")
//                .attr("text-anchor", "left")
//                .attr("x", width - 180)
//                .attr("y", function(d, i) {
//                    return 90 + i * 20 + 7;
//                })
//                //.attr("dx", 0)
//                //.attr("dy", "1em") // Controls padding to place text above bars
//                .text(function(d) {
//                    return d
//                })
//                .style("fill", function(d, i) {
//                    var colorShad;
//                    if(typeof centralColorMap[d.toString().toLowerCase()]!=="undefined"){
//                colorShad=centralColorMap[d.toString().toLowerCase()];
//              }else{
//                colorShad=color(i);
//              }
//                    return colorShad;
//                })
//                .attr("id", function(d, i) {
//                    return d;
//                })
//                .attr("color", function(d, i) {
//                    return d;
//                })
//                .attr("index_value", function(d, i) {
//                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("class", function(d, i) {
//                    return "bars-Bubble-legendText-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .on('mouseover', synchronizedMouseOver)
//                .on("mouseout", synchronizedMouseOut);
//            }
////            else {
////                return d.data[columns[0]];
////            }
//
//        }
//    });

    //    }
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 10px")
            .style("fill", function(d,i){
                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
                return "white"
            else
                return "black"
            })
            .attr("transform", function(d) { //set the label's origin to the center of the arc
                //we have to make sure to set these before calling arc.centroid
                d.outerRadius = radius; // Set Outer Coordinate
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    d.innerRadius = radius / 3;
                } else {
                    d.innerRadius = radius / 1.1;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if (typeof displayType !== "undefined" && displayType === "Absolute") {

 if(yAxisFormat==""){
                        return addCommas(numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
////                    }
                }
                else {
                var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
            });
    var center_group = svg.append("svg:g")
            .attr("class", "ctrGroup")
            .attr("transform", "translate(" + (width / 3) + "," + (height / 2) + ")");
    var pieLabel = center_group.append("svg:text")
            .attr("dy", "-2em")
            .attr("text-anchor", "middle")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 11px")
            .text("" + measureArray[0] + "");
    center_group.append("svg:text")
            .attr("dy", "-1em")
            .attr("text-anchor", "middle")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 10px")
            .text(function(d) {
                if (typeof parent.$("#sumValue").val() !== "undefined" && parent.$("#sumValue").val() !== "") {
                    return "" + addCommas(JSON.parse(parent.$("#sumValue").val()).toFixed(0)) + "";
                } else {
                    return "";
                }
            });
    var transformWord = "";
    if (isDouble === false) {

        transformWord = "rotate(-90, 50,60)";

    }
    else {
        transformWord = "rotate(-90, 110,110)";
    }
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
    function tweenDonut(b) {
        b.innerRadius = radius * .6;
        var i = d3.interpolate({
            startAngle: 0,
            endAngle: 0
        }, b);
        return function(t) {
            return arc(i(t));
        };
    }
    ;
    if ((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) || parent.$("#isMap").val() === "MapTable") {
    }
    else {
        buildDrill(width, height);
    }
if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
if(typeof div !="undefined" && div !="" && div !="chart0" && typeof chartData[div] !="undefined"){

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
	yvalue = parseInt(height / 4);

        rectyvalue = parseInt((height / 4)-10);

        var count = 0;
        var transform = "";
 if(typeof displayLength!="undefined" && displayLength!=""&& displayLength!="Yes"){}
 else
     {
if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength!="None")){
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

               for(var i=0;i<(data.length<10 ? data.length : 10) ;i++){
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
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", color(i))
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
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", color(i))
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
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", color(i))
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
//
//               for(var i=0;i<(data.length<10 ? data.length : 10) ;i++){
//                     if(data[i][measureArray[0]]>0){
//                if(i!=0){
//            yvalue = parseInt(yvalue+height*.06)
//            rectyvalue = parseInt(rectyvalue+height*.06)
//            }
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("transform", transform)
//            .attr("style", "overflow:scroll")
//
////            .attr("x",rectlen)
////            .attr("y",rectyvalue)
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
//            .attr("width", rectsize)
//            .attr("height", rectsize)
//            .attr("fill", color(i))
////            .style("stroke", color(i))
//          //  .attr("dy",dyvalue )
//
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//          //  .attr("style","margin-right:10")
//
//          //  .attr("transform", transform)
////            .attr("x",len)
////            .attr("y",yvalue)
//            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
//            .attr("fill", color(i))
//            .attr("style", "font-size:"+fontsize1+"px")
////            .style("stroke", color(i))
//        //    .attr("dy",dyvalue )
//            .attr("id",function(d){
//                return d[i][columns[0]];
//            } )
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//                if(data[i][columns[0]].length>25){
//                    return data[i][columns[0]].substring(0, 25);
//                }else {
//                    return data[i][columns[0]];
//          }
//           })
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id)
//                    })
//              count++
//               }
//            }
               }
            }
}
//     var ht = divHeight-50;
//    var html="";
//    var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
////    var measName = JSON.parse(parent.$("#measure").val());
////    var measIds = JSON.parse(parent.$("#measureIds").val());
//    var colName=columns[0];
//            if(viewOvIds.indexOf(columns[0])!="undefined"){
//                colName=viewOvName[viewOvIds.indexOf(columns[0])];
//            }
//       if(typeof displayLegend !=="undefined" && displayLegend =="Yes"){
//    if(columns[0].length > 15){
//        html +="<div style='height:"+ht+"px;float:right;overflow-y:auto'><table style='height:"+ht+"px;float:right;'><tr><td><table style='height:auto;float:right;width:"+divWidth*.3+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span title='"+columns[0]+"' style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0].substring(0,15)+"..</span></td></tr>";
//    }else {
//     html +="<div style='height:"+ht+"px;float:right;overflow-y:auto'><table style='height:"+ht+"px;float:right;'><tr><td><table style='height:auto;float:right;width:"+divWidth*.3+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
//    }
//
////if()
//    for(var m=0;m<data.length;m++){
//        if(data[m][columns[0]].length > 15){
//            if(typeof colorLegend !=="undefined" && colorLegend == "Default")
//    html +="<tr style='height:25px;white-space:nowrap'><td style='white-space:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 15)+"</span> </td></tr>";
//    else
//    html +="<tr style='height:25px;white-space:nowrap'><td style='white-space:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:black'> "+data[m][columns[0]].substring(0, 15)+"</span> </td></tr>";
//    }else {
//        if(typeof colorLegend !=="undefined" && colorLegend == "Default")
//            html +="<tr style='height:25px;white-space:nowrap'><td style='white-space:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
//            else
//            html +="<tr style='height:25px;white-space:nowrap'><td style='white-space:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:black'> "+data[m][columns[0]]+"</span> </td></tr>";
//    }
//    }
//    html +="</table></td></tr></table></div>";
//       }else {
//           html ="";
//       }
////    $("#legendsTable").append(html);
//    $("#"+div).append(html);
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
       }

       function bubbleDbMulti(div, data0, columns, measures, divWidth, divHeight, layout) {
    var html = "<div id='bubbleDb' style='display:none'></div>";
    $("#viewMeasureBlock").css('height','20px');
     var color = d3.scale.category12();
     var chartData = JSON.parse($("#chartData").val());
     var chartDetails = chartData["chart1"];
     if($("#chartType").val().toLowerCase() != "bubble-dashboard"){
     divWidth=parseFloat($(window).width())*(.55);
divHeight=parseFloat($(window).height())*(.85);
     }else {
           divWidth=parseFloat($(window).width())*(.35);
     }
var nodes = [];
    var data = [];
    layout=50;
    var measure1 = measures[0];
    var chartData = JSON.parse($("#chartData").val());
    var colIds;
    if(div=="Hchart1"){
        colIds = chartData["chart1"]["viewIds"];
    }else{
        colIds = chartData[div]["viewIds"];
    }
    var chartMap = {};
    var fun = "drillWithinchart1(this.id,\""+div+"\")";
    var isDashboard = parent.$("#isDashboard").val();
    var chartType = parent.$("#chartType").val();
var measureMap = {};
 var rows =500;
                data0.forEach(function(d, i) {
//                    if (i < rows && d[measures[0]] >= 0) {
                    if (i < rows ) {
                        var node = {};
                        node["name"] = d[columns[0]];

                        if (columns.length > 1) {
                            node["name1"] = d[columns[1]];
                        } else {
                            node["name1"] = d[columns[0]];
                        }
                        for (var no = 0; no < measures.length; no++) {
                            if (no == 0 && d[measures[no]]>=0) {
                                node["value"] = d[measures[no]];
                                measureMap[measures[no]] = "value";
                            } else {
                                if(d[measures[no]] >=0){
                                node["value" + no] = d[measures[no]];
                                measureMap[measures[no]] = "value" + no;
                                }
                            }
                        }
                    }
                    return nodes.push(node)
                });
data = nodes;
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var barsWidthTotal = $(window).width() - 400;
    var legendOffset = 60;
    var legendBulletOffset = 130;
    var legendTextOffset = 20;
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
    .size([divWidth - layout-10, divHeight - layout-10]) //-100
            .padding(1);
    svg = d3.select("#" + div).append("svg")
    .attr("width", divWidth - layout-50)
            .attr("height", divHeight  - 50)
    .style("float", "left")
    .style("margin-left","40px")// "-50px")
            .attr("id", "bubble");
    
    var gradient = svg.append("svg:defs")
    .selectAll("radialGradient")
    .data(data).enter()
            .append("svg:radialGradient")
            .attr("id", function(d) {
                if (d !== "undefined" && typeof d !== "undefined") {
//                    return "gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
                }
                else {
                    return "gradient";
                }
            })
            .attr("fx", "2%")
            .attr("fy", "2%")
            .attr("r", "50%")
            .attr("spreadMethod", "pad");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
//        var drilledvalue = parent.$("#drilledValue").val().split(",");

                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                    colorShad = drillShade;
                } else {
//                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
//                    } else {
                        colorShad = color(i);
//                    }
                }
//                if (typeof chartMap[d.name] === "undefined") {
//                    colorMap[i] = d.name + "__" + colorShad;
//                    chartMap[d.name] = colorShad;
//                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
            //Added by Ram
           var legends='';
           var arr=[];
            for(var k in columns){
                chartMap={};
                for(var i=0;i<data0.length;i++)
                {
                    if ( arr.indexOf(data0[i][columns[k]]) == -1){
                        chartMap[data0[i][columns[k]]]=color(i);
                    }
                }
                 var  html1 ="<input type='hidden' id='legends"+k+"' name='legends' />";
                 $("#legendsDiv").append(html1);
                parent.$("#legends"+(k)).val(JSON.stringify(chartMap));
            }
// End Ram code
//    parent.$("#legends").val(JSON.stringify(chartMap));
    var selection = svg.selectAll("g.node")
            .data(bubble_layout.nodes({
                children: data
            }).filter(function(d) {
                return !d.children;
            }));

    var node = selection.enter()
    .append("g")
            .attr("class", "node")
            .attr("transform", function(d) {
        var top = d.y-15;
                return "translate(" + d.x + ", " + top + ")";
    })
    .filter(function(d) {
        return d.value;
    });
    node.append("circle")
            .attr("r", function(d) {
                if(data.length==1){
                    return parseFloat(d.r)/2;
                }else {
                return d.r;
                }
            })
    //    .attr("color_value", function(d, i) {
    //          var colorShad;
    //        var drilledvalue;
    //        try {
    //            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
    //        } catch (e) {
    //        }
    //        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
    //            colorShad = drillShade;
    //        } else {
    //            colorShad = color(i);
    //        }
    //        return colorShad;
    //    })
            .attr("index_value", function(d, i) {
        return "index-" + d.name1.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
        return "bars-Bubble-index-" + d.name1.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"chart1";
            })
            .attr("fill", function(d, i) {
                var colorShad;
//        var drilledvalue = parent.$("#drilledValue").val().split(",");

                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                    colorShad = drillShade;
                } else {
//                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
//                    } else {
                        colorShad = color(i);
//                    }
                }
//                if (typeof chartMap[d.name] === "undefined") {
//                    colorMap[i] = d.name + "__" + colorShad;
//                    chartMap[d.name] = colorShad;
//                }
                return colorShad;
            })
    .attr("id", function(d,i) {
        return d.name + ":" + d.value + ":" + measures[0];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace("_", "-");
                    try {
                        var selectedBar = d3.selectAll(barSelector);
                        selectedBar.style("fill", drillShade);
                    } catch (e) {
                    }
                }
        d3.select(this)
        .attr("fill", function(d) {
            return  drillShade;
        })
        .attr("stroke", "grey");
        
                var content = "";
                for (var no = 0; no < measures.length; no++) {
   var msrData;
    if(typeof chartDetails["rounding"]!=="undefined"){
        yAxisRounding=chartDetails["rounding"];
    }else{
       yAxisRounding=0;
    }
    if(typeof chartDetails["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartDetails["yAxisFormat"];
    }else{
       yAxisFormat="";
    }
                        msrData = numberFormat(d.value, yAxisFormat, yAxisRounding,div);
                        msrData = addCommas(msrData);
                    if (typeof columnMap[columns[0]] !== "undefined" && columnMap[column[0]]["displayName"] !== "undefined" && typeof columnMap[measure1] !== "undefined" && columnMap[measure1]["displayName"]) {
                        if (no === 0) {
                            content += "<span class=\"name\">" + columnMap[columns[0]]["displayName"] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columnMap[columns[1]]["displayName"] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + columnMap[measures[no]]["displayName"] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                    } else {
                        if (no === 0) {
                            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + measures[no] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                    }
                }
                return tooltip.showTooltip(content, d3.event);
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
        d3.select(this)
        .attr("fill", function(d) {
            return  color(i);
        })
        .attr("stroke", "grey")
                hide_details(d, i, this);
            });

    node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "8pt")
            .attr("fill", labelColor)
            .text(function(d) {
        return d.name.substring(0, d.r / 5);
            });
//            svg.append("g")
//            .attr("class", "y axis")
//            .append("text")
//            .attr("transform", "rotate(360)")
//            .attr("x", 80)
//            .attr("y", 30)
//            .attr("fill","black")
//            .attr("dx", ".71em")
//            .attr("style","font-size:11px;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif;")
//            .style("text-anchor", "end")
//            .text("" + measure1 + "");
    if (columns.length > 1) {
        var legends = [];
        bubble_layout.nodes({
            children: data
        }).filter(function(d) {
            return !d.children;
        })
                .filter(function(d) {
                    return !d.children;
                }).forEach(function(d) {
            if (legends.indexOf(d.name) === -1)
                legends.push(d.name);
        });

        svg.selectAll(".circle")
//                .data(legends).enter().append("svg:circle") // Append circle elements
                .attr("cx", barsWidthTotal + legendBulletOffset)
                .attr("cy", function(d, i) {
                    return legendOffset + i * 20 + 5;
                })
                .attr("stroke-width", ".5")
                .style("fill", function(d, i) {
                    return chartMap[d];
        }) 
                .attr("index_value", function(d, i) {
                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })

                .attr("class", function(d, i) {
                    return "bars-Bubble-legendBullet-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("r", 5)
                .attr("color_value", function(d, i) {
                    return chartMap[d];
        })
        .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id)
        })
        .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id)
        });
                
        svg.selectAll("g.legend")
//                .data(legends) // Instruct to bind dataSet to text elements
//                .enter().append("g") // Append legend elements
                //.attr("xlink:href", function(d) { return d.link; })
                .append("text")
                .attr("text-anchor", "left")
                .attr("x", barsWidthTotal + legendBulletOffset + legendTextOffset)
                .attr("y", function(d, i) {
                    return legendOffset + i * 20 - 5;
                })
                .attr("dx", 0)
                .attr("dy", "1em") // Controls padding to place text above bars
                .text(function(d) {
                    return d;
                })
                .style("fill", function(d, i) {
            return d;
                })
                .attr("id", function(d, i) {
                    return d;
                })
                .attr("color", function(d, i) {
                    return d;
                })
                .attr("index_value", function(d, i) {
                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("class", function(d, i) {
                    return "bars-Bubble-legendText-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
        .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id)
        })
        .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id)
        })
                .attr("onclick", "legendDrill(this.id)");
    }



    var colName=columns[1];
    html +="<div class='hiddenscrollbar' style=''>";
    html +="<div class='innerhiddenscrollbar' style='white-space:nowrap;width:150px;height:"+divHeight*.80+"px;float:left;margin-left:120px;margin-top:15px;overflow-y:auto'>";//float: right;margin-left: 120px;margin-top: 40px;overflow-y: scroll;
    html +="<table style='float:right;' height='"+divHeight*.05+"'>";
    html +="<tr style='height:25px'><td style='height:25px'><span style='white-space:nowrap;color:black;font-size:12px;margin-left:20px;text-decoration:none' >"+columns[1]+"</span></td></tr>";
    
    var colMap={};
    for(var m=0;m<data0.length;m++){
        colMap[data0[m][columns[1]]]=data0[m][columns[1]];
}
    for(var i=0;i<Object.keys(colMap).length;i++){
        html +="<tr style='height:25px'><td style='style='height:25px''>";
        if(Object.keys(colMap)[i].length > 11){
            html +="<canvas  width='7' height='7' style='margin-left:5px;margin-right:5px;background:" + color(i) + "'></canvas>";
            html +="<span  id='"+Object.keys(colMap)[i]+"'   onmouseover='setMouseOverEvent(this.id,\""+"chart1"+"\")' onmouseout='setMouseOutEvent(this.id,\""+"chart1"+"\")' style='margin-left:5px;font-size: 9px;color:"+color(i)+"'> "+Object.keys(colMap)[i].substring(0,11)+"..</span></td></tr>";
        }else{
            html +="<canvas  width='7' height='7' style='margin-left:5px;margin-right:5px;background:" + color(i) + "'></canvas>";
            html +="<span   id='"+Object.keys(colMap)[i]+"'  onmouseover='setMouseOverEvent(this.id,\""+"chart1"+"\")' onmouseout='setMouseOutEvent(this.id,\""+"chart1"+"\")' style='margin-left:5px;font-size: 9px;color:"+color(i)+"'> "+Object.keys(colMap)[i]+"</span></td></tr>";    
        }
    } 
    html +="</table></div></div>";
    $("#"+div).append(html);

    }

    
    function buildUSMap(div,data,columns,measureArray) {
 var color = d3.scale.category12();
var chartMapData = data["chart1"]
var fun = "clicked(this.id,\"chart1\")";
  var chartData = JSON.parse($("#chartData").val());
if(typeof data["chart2"]!="undefined" && data["chart2"]!="" ){
   var chartMapDataTable = data["chart2"]
   var tabColumns = chartData["chart2"]["viewBys"]
}else{
  var chartMapDataTable = data["chart1"]
  var tabColumns = chartData[div]["viewBys"]
}
     $("#"+div).append("<div id ='KPIdiv' style = 'height:100px;overflow-y:auto;margin-top:20px;border:2px inset grey;float:left;width:30%;'>");
    $("#"+div).append("<div id ='mapdiv' style = 'height:auto;overflow-y:auto;margin-top:2%;float:right;width:65%;'>");
    $("#"+div).append("<div id ='tableDiv' class='hiddenscrollbar' style = 'margin-top:20px;float:left;width:32%;'>");
    KPIMapDefaultDiv()
    kpiMapTableDiv(div,chartMapDataTable,tabColumns,measureArray)
   width = $(window).width()*.6;
//    width = 1000;
    height = $(window).height();
    active = d3.select(null);

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
                

//var projection =  d3.geo.mercator().scale(900)
//            .translate([width / 2, height / 2.3]);
    var projection = d3.geo.albersUsa()
            .scale(900)
            .translate([width / 2, height / 2.3]);
    var projection1 = d3.geo.albersUsa()
//    .center([98, 23.5])
            .scale(900);

    var path1 = d3.geo.path()
            .projection(projection1);
    if (screen.width == 1024 && screen.height == 768) {
        projection = d3.geo.albersUsa()
                .scale(650)
                .translate([width / 2, height / 3]);
    }

    var path = d3.geo.path()
            .projection(projection);

    var svg = d3.select("#mapdiv" ).append("svg")
//    var svg = d3.select("#"+div).append("svg")
            .attr("width", width)
            .attr("height", height)
             .style("float", "right");

//    svg.append("rect")
//            .attr("class", "background")
//            .attr("width", width)
//            .attr("height", height)
//            .on("click", reset);

    var g = svg.append("g")
            .style("stroke-width", "1.5px");
    d3.json("JS/us-States.json", function(error, us) {
      g.selectAll("path")
                .data(us.features)
                .enter().append("path")
                .attr("d", path)
                .attr("id",function(d,i){
        var currName = (d.properties.name).toUpperCase();
     
for (var l = 0; l < chartMapData.length; l++) {
if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
 return chartMapData[l][columns[0]]+":"+chartMapData[l][columns[0]];
}
}
return "";
})
                .attr("fill", function(d, i) {
                    var colorShad = "white";
                   for (var k = 0; k < chartMapData.length; k++) {
                       if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                            var strVal = chartMapData[k][measureArray[0]];
                           if (isShadedColor) {
                              var map = JSON.parse(parent.$("#measureColor").val());
                                            shadingMeasure = map["measure"];
                                        strVal = chartMapData[k][shadingMeasure];
                                        colorShad = color(strVal);
                            } else if (conditionalShading) {
//                    return getConditionalColor("steelblue", d[conditionalMeasure]);
                    return getConditionalColor(color(i), chartMapData[k][conditionalMeasure]);
                            }
                            else {
                                colorShad = color(i);
                            }
                        }
                    }
                    return colorShad;
                })
                .attr("stroke", "#000")
                .attr("onclick", fun)
                .on("mouseover", function(d, i) {
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                            show_details(chartMapData[k], columns, measureArray, this);
                            KPIMapDiv(chartMapData[k], columns, measureArray, this);
                        }
                    }
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                    KPIMapDefaultDiv()
                });
                
                
                 var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-33%;margin-left:90%;'></div>";
                    $('#'+div).append(html);
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legends2").append("svg")
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
                        $("#legends2").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else if (parent.$("#shadeType").val() === "gradient") {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legends2").height() - 10;
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


                        $("#legends2").css("width", "8%");
                    }
            
                
                
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
    function KPIMapDiv(data, columns, measureArray,current){
   var html = ""
  
      html += "<div style = 'width:60%;height:100%; float:left'>";
     html += "<div style = 'width:100%;height:30%; float:left'>";
     html += "<span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 26px;margin-left: 10%;color: gray;'>"+data[columns]+"</span>";
     html += "</div>";
     html += "<div style = 'width:100%;height:50%; float:left;margin-top:8%'>";
     html += "<table style = 'width:100%;height:100%; float:left'>";
     
     for(var i in measureArray){
     html += "<tr>"
     html += "<td><span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 12px;margin-left: 10%;color: gray;'>"+measureArray[i]+":</span></td>";
     html += "<td><span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 12px;margin-left: 10%;color: gray;'>"+addCommas(data[measureArray[i]])+"</span></td>";
     html += "</tr>"
}
     html += "</table>";
     html += "</div>";
      html += "</div>";
      html += "<div style = 'width:30%;height:100%; float:right'>";
      html += "<img class='' style='width:100%;height:100%' src = 'images/mapkpi/"+data[columns].toLowerCase()+".jpg'>";
      html += "</div>";
      $("#KPIdiv").html(html)
    }
    function KPIMapDefaultDiv(){
        var kpidiv ="";
        kpidiv += "<div style = 'margin-top: 10%;width:50%; float:left'>";
        kpidiv += "<span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 20px;color: gray;'>UNITED STATES</span>";
        kpidiv += "</div>";
        kpidiv += "<div style = 'width:40%;height:100%; float:right'>";
      kpidiv += "<img class='' style='width:100%;height:100%' src = 'images/mapkpi/us.jpg'>";
      kpidiv += "</div>";
      $("#KPIdiv").html(kpidiv)
    }
    function kpiMapTableDiv(div,chartMapDataTable,tabColumns,measureArray){

       var htmlstr = "";
        htmlstr = "<div class = 'innerhiddenscrollbar' style=''>";
         htmlstr += "<table class=\"tBody table table-condensed table-bordered\"  width='100%' >";

//    for(var ii in data){
    htmlstr += "<tr class=\"thClass\" align='center'>";
    for (var j in tabColumns) {
        htmlstr += "<td>" + tabColumns[j] + "</td>"
    }
    for (var j in measureArray) {
        htmlstr += "<td>" + measureArray[j] + "</td>"
    }
    htmlstr += "</tr>";
    var kpicount = 0;
    var avgData = [];
    for (var j in chartMapDataTable) {
        avgData.push(chartMapDataTable[j]);
        kpicount++;
        if (kpicount % 2 == 1) {
            htmlstr += "<tr class=\"tdClass1\">";
            for (var k in tabColumns) {
             htmlstr += "<td class='tdClass' align='left'>" + chartMapDataTable[j][tabColumns[k]] + "</td>";
                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
                    var measVal = parseFloat(chartMapDataTable[j][measureArray[n]]).toFixed(2);
                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
                }
            }
            htmlstr += "</tr>";
        }
        else {
            htmlstr += "<tr class=\"tdClass2\">";
            for (var k in columns) {
                htmlstr += "<td class='tdClass' align='left'>" + chartMapDataTable[j][tabColumns[k]] + "</td>";
                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
                    var measVal = parseFloat(chartMapDataTable[j][measureArray[n]]).toFixed(2);
                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
                }
            }
            htmlstr += "</tr>";
        }

    }

//    }
    htmlstr += "</table>";
    htmlstr += "</div>";
     $("#tableDiv").html(htmlstr);
    
    }
}


function buildUSMapCity(div, data, columns, measureArray) {
var chId=div.replace("H","");
var color = d3.scale.category12();
var chartMapData = data["chart1"]
var fun = "clicked(this.id,\"chart1\")";
 var chartData = JSON.parse($("#chartData").val());
    if(typeof data["chart2"]!="undefined" && data["chart2"]!="" ){
   var chartMapDataTable = data["chart2"]
   var tabColumns = chartData["chart2"]["viewBys"]
}else{
  var chartMapDataTable = data["chart1"]
  var tabColumns = chartData[chId]["viewBys"]
 
}
    $("#"+div).append("<div id ='KPIdiv' style = 'height:100px;overflow-y:auto;margin-top:20px;border:2px inset grey;float:left;width:30%;'>");
    $("#"+div).append("<div id ='mapdiv' style = 'height:auto;margin-top:-5%;float:right;width:65%;'>");
     $("#"+div).append("<div id ='tableDiv' class='hiddenscrollbar' style = 'margin-top:20px;float:left;width:32%;'>");
    KPIMapDefaultDiv()
    kpiMapTableDiv(div,chartMapDataTable,tabColumns,measureArray)

    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var min, max;
    min = minimumValue(chartMapData, measureArray[0]);
    max = maximumValue(chartMapData, measureArray[0]);
    var pointsize = d3.scale.linear()
            .domain([min, max])
            .range([4, 10]);
    parent.$("#sumValue").val(sum);

    width = $(window).width()*.6;
    height = $(window).height();
    active = d3.select(null);
    var projection = d3.geo.albersUsa()
            .scale(900)
            .translate([width / 2, height / 2.3]);
    var projection1 = d3.geo.albersUsa()
//    .center([98, 23.5])
            .scale(900);

    var path1 = d3.geo.path()
            .projection(projection1);
    if (screen.width == 1024 && screen.height == 768) {
        projection = d3.geo.albersUsa()
                .scale(650)
                .translate([width / 2, height / 3]);
    }

    var path = d3.geo.path()
            .projection(projection);

    var svg = d3.select("#mapdiv" ).append("svg")
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
                .attr("fill", function(d) {
                    for (var l2 = 0; l2 < chartMapData.length; l2++) {
                        if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                            if (chartMapData[l2][measureArray[0]] == max) {
                                return "#99FF00";
                            } else {
                                return "darkgreen";
                            }
                        }
                    }
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
     function KPIMapDiv(data, columns, measureArray,current){
   var html = ""

      html += "<div style = 'width:60%;height:100%; float:left'>";
     html += "<div style = 'width:100%;height:30%; float:left'>";
     html += "<span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 26px;margin-left: 10%;color: gray;'>"+data[columns]+"</span>";
     html += "</div>";
     html += "<div style = 'width:100%;height:50%; float:left;margin-top:8%'>";
     html += "<table style = 'width:100%;height:100%; float:left'>";

     for(var i in measureArray){
     html += "<tr>"
     html += "<td><span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 12px;margin-left: 10%;color: gray;'>"+measureArray[i]+":</span></td>";
     html += "<td><span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 12px;margin-left: 10%;color: gray;'>"+addCommas(data[measureArray[i]])+"</span></td>";
     html += "</tr>"
}
     html += "</table>";
     html += "</div>";
      html += "</div>";
//      html += "<div style = 'width:30%;height:100%; float:right'>";
//      html += "<img class='' style='width:100%;height:100%' src = 'images/mapkpi/"+data[columns].toLowerCase()+".jpg'>";
//      html += "</div>";
      $("#KPIdiv").html(html)
    }
    function KPIMapDefaultDiv(){
        var kpidiv ="";
        kpidiv += "<div style = 'margin-top: 10%;width:50%; float:left'>";
        kpidiv += "<span style ='font-family: arial,sans-serif-light,sans-serif;font-size: 20px;color: gray;'>UNITED STATES</span>";
        kpidiv += "</div>";
//        kpidiv += "<div style = 'width:40%;height:100%; float:right'>";
//      kpidiv += "<img class='' style='width:100%;height:100%' src = 'images/mapkpi/us.jpg'>";
//      kpidiv += "</div>";
      $("#KPIdiv").html(kpidiv)
    }
    function kpiMapTableDiv(div,chartMapDataTable,tabColumns,measureArray){

       var htmlstr = "";
        htmlstr += "<div class = 'innerhiddenscrollbar' style=''>";
         htmlstr += "<table class=\"tBody table table-condensed table-bordered\"  width='100%' >";

//    for(var ii in data){
    htmlstr += "<tr class=\"thClass\" align='center'>";
    for (var j in tabColumns) {
        htmlstr += "<td>" + tabColumns[j] + "</td>"
    }
    for (var j in measureArray) {
        htmlstr += "<td>" + measureArray[j] + "</td>"
    }
    htmlstr += "</tr>";
    var kpicount = 0;
    var avgData = [];
    for (var j in chartMapDataTable) {
        avgData.push(chartMapDataTable[j]);
        kpicount++;
        if (kpicount % 2 == 1) {
            htmlstr += "<tr class=\"tdClass1\">";
            for (var k in tabColumns) {
             htmlstr += "<td class='tdClass' align='left'>" + chartMapDataTable[j][tabColumns[k]] + "</td>";
                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
                    var measVal = parseFloat(chartMapDataTable[j][measureArray[n]]).toFixed(2);
                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
                }
            }
            htmlstr += "</tr>";
        }
        else {
            htmlstr += "<tr class=\"tdClass2\">";
            for (var k in columns) {
                htmlstr += "<td class='tdClass' align='left'>" + chartMapDataTable[j][tabColumns[k]] + "</td>";
                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
                    var measVal = parseFloat(chartMapDataTable[j][measureArray[n]]).toFixed(2);
                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
                }
            }
            htmlstr += "</tr>";
        }

    }

//    }
    htmlstr += "</table>";
    htmlstr += "</div>";
     $("#tableDiv").html(htmlstr);

    }
}

function buildQuickBar(div, data, columns, measureArray,wid,hgt) {
    
 var customTicks = 5;
var color = d3.scale.category10();
var divWid=wid;
var chartData = JSON.parse(parent.$("#chartData").val());
var div1=parent.$("#chartname").val()
   //   var divHgt=500;
   var divHgt=hgt;
   var dashletid=div;
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
//   divHgt = hgt+60;
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


    var autoRounding1;

        autoRounding1 = "1d";

    var fun = "openQuickForward(this.id,\""+div+"\",$(this).attr('x'),$(this).attr('y'))";
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
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }

var margin = {};
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = {
        top: 10,
        right: 30,
//        bottom: botom,

        bottom: divHgt *.3,
        left: 0
    };
}else{
    margin = {
        top: 10,
        right: 30,
//        bottom: botom,

        bottom: divHgt *.3,
        left: 70
    };
}
  var width = divWid;
    var height = 0;
//   percent = d3.format('%'),
//            height = divHgt * .6; //- margin.top - margin.bottom
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
   height = divHgt*.8 ; //- margin.top - margin.bottom
}else{
            height = divHgt *.7; //- margin.top - margin.bottom
}

        
    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
   var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);
    var y = d3.scale.linear()
            .range([height, 0]);
    var measArr = [];
            make_x_axis = function() {
    return d3.svg.axis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
//         .ticks(percent)
}

 make_y_axis = function() {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
//        .ticks(percent)
}

  var  xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
//    xAxis = d3.svg.axis()
//            .scale(x)
//            .orient("bottom");
//    if (isFormatedMeasure) {
    var    yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
//                 .ticks(percent)
                .tickFormat(function(d, i) {
                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
//    var svg = d3.select("#" + divAppend)
    var svg = d3.select("#H"+div)
   //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("id", "svg_" + divAppend)
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+30)+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom+17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
                return "gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
////                            colorShad = "steelblue";
//                        }
//                    }
//                    }
//                }
////                colorMap[i] = d[columns[0]] + "__" + colorShad;
////colorShad="steelblue";
//                return colorShad;
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
            if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height*1) + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
             
                if (d.length < 15){
                    return d;
                }
                else {
                    return d.substring(0, 8) + "..";
                }

            })

            .attr('transform', 'rotate(-45)')
            .append("svg:title").text(function(d) {
        return d;
    });
            }
    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end");

  var valuelineH = d3.svg.line()
            .x(function(d,i) {
             
               if(i==0){
               return x(d[columns[0]]) - x(0);
               }else{
                   return x(d[columns[0]]) + x(i.length);
               }
            })
            .y(function(d) {
                var target = "";
                if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                target = chartData[div]["targetLine"];
                return y(parseInt(target));
                }
            });
            
           
 var bars=svg.selectAll(".bar")
            .data(data)
            .enter().append("g")
            .attr("class", "bar")
            .append("rect");
            bars.attr("rx", barRadius)
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
                       return targetColor(d,measure1,targetLine);
                  } else {
                      targetLine = "";
                       return getDrawColor(div, parseInt(i));
                  } 
               //"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                   
                  
           }
                else
                {
                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure1,targetLine);
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
                       return targetColor(d,measure1,targetLine);
                  } else {
                      targetLine = "";
                       return getDrawColor(div, parseInt(i));
                  } 
               //"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                   
                  
           }
                else
                {
                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                      targetLine = chartData[div]["targetLine"];
                       return targetColor(d,measure1,targetLine);
                  } else {
                      targetLine = "";
                      return color(0);
                  } 
                    
                    return color(0);
                }
             })
            .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .attr("onclick", fun)
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
            });
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
//                } else {bu
if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    show_details1(d, columns, measureArray, this,div);
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
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
//                    var colorValue = prevColor;
                    selectedBar.style("fill", colorValue);
                }
//                }
                hide_details1(d, i, this);
            });
             //target Line
//  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==''){
// var path =   svg.append("path")
//            .data(data)
//            .attr("d", valuelineH(data))
//            .attr("fill", "transparent")
//            .style("z-index", "9999")
//            .style("stroke-width", "1.5px")
//            .style("stroke", "black");
// 
// var totalLength = path.node().getTotalLength();
//
//    path
//      .attr("stroke-dasharray", totalLength + " " + totalLength)
//      .attr("stroke-dashoffset", totalLength)
//      .transition()
//        .duration(2000)
//        .ease("linear")
//        .attr("stroke-dashoffset", 0);
//  }
  
  var test=svg.append("foreignObject")
        .attr("width", width)
        .attr("height", height)
        .style("display","none")
        .attr("x", function(d) {
                return x(data[columns[0]]);
            })
            .attr("y", function(d) {
               
                return "0";
            }) .attr("id", function(d) {
                return "foreignObj"+div;
            })
        .append("xhtml:body")
        .attr('xmlns','http://www.w3.org/1999/xhtml')
        .html("<div id='callQuickDiv"+div+"' width=20 height=20></div>");
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
    if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
        {
            var font=12;
            font=width/51.2;
            font=font>12?12:font;
            font=font<7?7:font;
            svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                    return "translate(" + (xvalue-width/105) + ", " + (height+(height/30)) + ")rotate(" + -90 + ")";
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
                .attr("fill", function(d,i){
                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
                        if(parseFloat(d[measure1]) >= max){
                            return "white";
                        }
                    }

                    else {
                    return labelColor;
                    }
                })
                .text(function(d)
                {
                        return d[columns[0]];
                });
        }
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
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
                .attr("fill", function(d,i){
                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
                        if(parseFloat(d[measure1]) >= max){
                            return "white";
                        }
                    }

                    else {
                    return labelColor;
                    }
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
if(fromoneview!='null'&& fromoneview=='true'){
                     show_details(d, columns, measureArray, this);
                }else{
                show_details(d, columns, measureArray, this);
                }
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

}
function buildKPITable(chartId,data, viewbys, measures, divwidth, divHght,KPIresult) {
//    alert("SSSS");
    chartId = chartId.replace("Tile","");
    graphProp(chartId);
       var yAxisFormat = "";
   var yAxisRounding = 0;
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var chartData = JSON.parse(parent.$("#chartData").val());
    var dimensionId = chartData[chartId]["dimensions"];
    var viewBys = [];
    var specialCharacter ="%";
    DataSum_measureArray=[];
    //    for GTcalculate
    //    for(var m in measures){
    //        DataSum_measureArray[m]=0;
    //        for(var d in data){
    //            DataSum_measureArray[m]+= parseFloat(data[d][measures[m]]);
    //        }
    //        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
    //    }
    var html = "";
    html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght*0.8 + "px; overflow-y: auto;overflow-x: hidden\">";
    html = html + "<div style='height:400px'>";
    html += "<table id=\"chartTable" + chartId + "\" class='table table-condensed ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
    html += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
    html += "<th ></th>";
    html += "<th style='white-space:nowrap;font-weight:bold;text-align:left' ><strong>Measure</strong></th>"
    html += "<th style='white-space:nowrap;font-weight:bold;text-align:left' ><strong>Value</strong></th>"
    html = html + "</tr>";
    var DataSum=[];
    for(var m in measures){
        DataSum[m]=0;
        for(var i in data){
            DataSum[m]+= parseFloat(data[i][measures[m]]);
        }
    }
    for(var b=0;b<measures.length;b++){
        var num = (b + 1) % 2;
        var color;
        if (num === 0) {
            color = "white";
        } else {
            color = "white";
        }
        if(measures[0].indexOf("Change%")!=-1){
            var current = 0;
            var prior = 0;
            var nameArr = [];

            var nameArr2 = "";
            nameArr =  measures[b].split(/[ ,]+/);
            for(var k =1;k<nameArr.length;k++){
                if(nameArr.length>2){
                    if(k==1){
                        nameArr2 =  nameArr[k]
                    }else{

                        nameArrCurrent = nameArr2.concat(nameArr[k]);
                        nameArr2 = nameArrCurrent

                    }
                }else{
                    nameArrCurrent = nameArr[k]
                }
            }
            nameArrPrior = "Prior "+nameArrCurrent;

            for(var l in data){

                for(var p in measures){
                    if(nameArrCurrent.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().split(" ").join("")){
                        current += parseFloat(data[l][measures[p]])
                    }
                    else if(nameArrPrior.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().split(" ").join("")){
                        prior += parseFloat(data[l][measures[p]])
                    }
                }
            }
            if(typeof current!="undefined" && current!="" && typeof prior!="undefined" && prior!=""){
                showData = parseFloat(((current-prior)/prior)*100)
                perFlag = "true";
            }else{
                showData =   parseFloat(DataSum[b]/(data.length));
            }
        }
        else if(measures[b].indexOf(specialCharacter)!=-1 ||measures[b].indexOf("Percent")!=-1 ||measures[b].indexOf("percent")!=-1 ||measures[b].indexOf("PERCENT")!=-1) {
            showData =   parseFloat(DataSum[b]/(data.length));
            perFlag = "true";
        }else{
          if(chartData[chartId]["aggregation"][b]=="AVG" || chartData[chartId]["aggregation"][b]=="avg"){
                 showData = parseFloat(DataSum[b])/data.length;
            perFlag = "false";

            absoluteNumber= DataSum[b]/data.length;
            }
           else{
            showData = parseFloat(DataSum[b]);
            perFlag = "false";
            absoluteNumber= DataSum[b];
        }
        }
//        alert(yAxisFormat);

//  if(typeof chartData[chartId]["numberFormat"]!= "undefined" && chartData[chartId]["yAxisFormat"]!= ""){
//      yAxisFormat = chartData[chartId]["yAxisFormat"];
//  }else{
//   yAxisFormat = "";
//  }
//  if(typeof chartData[chartId]["rounding"]!= "undefined" && chartData[chartId]["rounding"]!= ""){
//      yAxisRounding = chartData[chartId]["rounding"];
//  }else{
//   yAxisRounding =0;
//  }
//        alert(yAxisRounding);
if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measures[b]]!='undefined'){
                yAxisFormat=numberFormatArr[measures[b]];
  }
            else{
                yAxisFormat="";
  }
            if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measures[b]]!='undefined'){
                yAxisRounding=numberRoundingArr[measures[b]];
            }
            else{
                yAxisRounding="0";
            }
        if(typeof yAxisFormat!='undefined' && yAxisFormat===""){

                    showData1 =      addCommas(numberFormat1(KPIresult[b],yAxisFormat,yAxisRounding,chartId));
                    }
            else{
             showData1 =        numberFormat1(KPIresult[b],yAxisFormat,yAxisRounding,chartId);
                }
        divHght = divHght-50;
        if(typeof dimensionId !=="undefined"){
            for(var m=0;m<dimensionId.length;m++){
                viewBys.push(viewOvName[viewOvIds.indexOf(dimensionId[m])])
            }
        }else {
            viewBys = viewbys;
        }
//        var colorValue = d3.scale.category10();
        var drawColor=getDrawColor("chart1", parseInt(b)); 
        html += "<tr style=\"background-color:" + color + ";white-space:nowrap;\">";
        html += "<td  style=\"background-color:" + color + ";\" width=25px>";
        html += '<svg width="25" height="15"><circle class="" cx="15" cy="10" r="5" index_value="" color_value="'+drawColor+'" stroke="" stroke-width="3" fill="'+drawColor+'" /></svg>';
        html += "</td>";
        html += "<td style=\"background-color:white;color:black\">"+measures[b]+"</td>";
        html += "<td style=\"background-color:white;color:black\">"+showData1+"</td></tr>";
    }
    html += "</table>";
    html += "</div></div>";
    $("#" + chartId).html(html);
}
function buildExpTable(chartId,data, viewbys, measures, divwidth, divHght,KPIresult) {
    chartId = chartId.replace("Tile","");
    var yAxisFormat = "";
    var yAxisRounding = 0;
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var chartData = JSON.parse(parent.$("#chartData").val());
    var dimensionId = chartData[chartId]["dimensions"];
    var viewBys = [];
    var specialCharacter ="%";
    DataSum_measureArray=[];
    //    for GTcalculate
    //    for(var m in measures){
    //        DataSum_measureArray[m]=0;
    //        for(var d in data){
    //            DataSum_measureArray[m]+= parseFloat(data[d][measures[m]]);
    //        }
    //        DataSum_measureArray[m]=Math.round((DataSum_measureArray[m] + 0.00001) * 100) / 100;
    //    }
    var html = "";
    html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght*0.8 + "px; overflow-y: auto;overflow-x: hidden\">";
    html = html + "<div style='height:"+(divHght*0.6)+"px'>";
    html += "<table id=\"chartTable" + chartId + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
    html += "<tr align='left' style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black;white-space:nowrap;\">";
//    html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>S.no</strong></th>"
    html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><span align='center'><strong>Measure</strong></span></th>"
    html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>Value</strong></th>"
    html += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"></th>"
    html = html + "</tr>";
    var DataSum=[];
    for(var m in measures){
        DataSum[m]=0;
        for(var i in data){
            DataSum[m]+= parseFloat(data[i][measures[m]]);
        }
    }
    for(var b=0;b<measures.length;b++){
        var num = (b + 1) % 2;
        var color;
        if (num === 0) {
            color = "rgba(240, 245, 248, 1)";
        } else {
            color = "white";
        }
        if(measures[0].indexOf("Change%")!=-1){
            var current = 0;
            var prior = 0;
            var nameArr = [];

            var nameArr2 = "";
            nameArr =  measures[b].split(/[ ,]+/);
            for(var k =1;k<nameArr.length;k++){
                if(nameArr.length>2){
                    if(k==1){
                        nameArr2 =  nameArr[k]
                    }else{

                        nameArrCurrent = nameArr2.concat(nameArr[k]);
                        nameArr2 = nameArrCurrent

                    }
                }else{
                    nameArrCurrent = nameArr[k]
                }
            }
            nameArrPrior = "Prior "+nameArrCurrent;

            for(var l in data){

                for(var p in measures){
                    if(nameArrCurrent.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().split(" ").join("")){
                        current += parseFloat(data[l][measures[p]])
                    }
                    else if(nameArrPrior.toString().toUpperCase().replace(" ", "") == measures[p].toString().toUpperCase().split(" ").join("")){
                        prior += parseFloat(data[l][measures[p]])
                    }
                }
            }
            if(typeof current!="undefined" && current!="" && typeof prior!="undefined" && prior!=""){
                showData = parseFloat(((current-prior)/prior)*100)
                perFlag = "true";
            }else{
                showData =   parseFloat(DataSum[b]/(data.length));
            }
        }
        else if(measures[b].indexOf(specialCharacter)!=-1 ||measures[b].indexOf("Percent")!=-1 ||measures[b].indexOf("percent")!=-1 ||measures[b].indexOf("PERCENT")!=-1  ) {
            showData =   parseFloat(DataSum[b]/(data.length));
            perFlag = "true";
        }
        else{
           
            if(chartData[chartId]["aggregation"][b]=="AVG" || chartData[chartId]["aggregation"][b]=="avg"){
                 showData = parseFloat(DataSum[b])/data.length;
            perFlag = "false";

            absoluteNumber= DataSum[b]/data.length;
            }
           else{
                showData = parseFloat(DataSum[b]);
            perFlag = "false";

            absoluteNumber= DataSum[b];
           }
        }
        if(typeof chartData[chartId]["yAxisFormat"]!= "undefined" && chartData[chartId]["yAxisFormat"]!= ""){
      yAxisFormat = chartData[chartId]["yAxisFormat"];
  }else{
   yAxisFormat = "";
  }
  if(typeof chartData[chartId]["rounding"]!= "undefined" && chartData[chartId]["rounding"]!= ""){
      yAxisRounding = chartData[chartId]["rounding"];
  }else{
   yAxisRounding =0;
  }
        //        alert(yAxisFormat);
        if(typeof yAxisFormat!='undefined' && yAxisFormat===""){

            showData1 =      addCommas(numberFormat1(KPIresult[b],yAxisFormat,yAxisRounding,chartId));
        }
        else{
            showData1 = numberFormat1(KPIresult[b],yAxisFormat,yAxisRounding,chartId);
        }
        divHght = divHght-50;
        if(typeof dimensionId !=="undefined"){
            for(var m=0;m<dimensionId.length;m++){
                viewBys.push(viewOvName[viewOvIds.indexOf(dimensionId[m])])
            }
        }else {
            viewBys = viewbys;
        }
        var ctxPath=parent.document.getElementById("h").value;
        html += "<tr style=\"background-color:" + color + ";white-space:nowrap;\">";
//        html += "<td style=\"background-color:"+color+";color:black\">"+(b+1)+"</td>";
        html += "<td style=\"background-color:"+color+";color:black\">"+measures[b]+"</td>";
        html += "<td style=\"background-color:"+color+";color:black\">"+showData1+"</td>";
        if(typeof chartData[chartId]["targetLine"]==='undefined' || chartData[chartId]["targetLine"]===''){
            html += "<td style=\"background-color:"+color+";color:black\" width='20px'>--</td>";
        }
        else{
            var targetValue=chartData[chartId]["targetLine"];
            var imageName; 
            if(showData>targetValue){
                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='happy_face.png';
            }
                else{
                    imageName='green.png';
                }
                html += "<td style=\"background-color:"+color+";color:black\" width='20px'><img src='"+ctxPath+"/images/"+imageName+"' width='100%' style='float:right;'></td>";
            }
            else if(showData==targetValue){
                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='normal_face.png';
            }
            else{
                    imageName='amber.png';
            }
                html += "<td style=\"background-color:"+color+";color:black\" width='20px'><img src='"+ctxPath+"/images/"+imageName+"' width='100%' style='float:right;'></td>";
        }
            else{
                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='sad_face.png';
                }
                else{
                    imageName='red.png';
                }                
                html += "<td style=\"background-color:"+color+";color:black\" width='20px'><img src='"+ctxPath+"/images/"+imageName+"' width='100%' style='float:right;'></td>";
            }
        }

        html += "</tr>"
    }
    html += "</table>";
    html += "</div></div>";
    $("#" + chartId).html(html);
}

  function buildTable(chartId,data, currdata, columns, measureArray, divwidth, divHght,KPIresult) {
  
    var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
     var chartData = JSON.parse(parent.$("#chartData").val());
//  var colIds = chartData[chartId]["viewIds"];
  
//    Added by shivam
        if(typeof chartData[chartId]["Pattern"]!='undefined' && chartData[chartId]["Pattern"]==='Multi'){
         chartData[chartId]["Pattern"]="Normal";
     }
     parent.$("#chartData").val(JSON.stringify(chartData));
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
  dimensionId = chartData[div1]["dimensions"];
  div=chartId
  chartId=div1;
  dimensionId = chartData[div1]["dimensions"];
  var prop = graphProp(chartId);
}else{
    colIds = chartData[chartId]["viewIds"];
    div=chartId
    dimensionId = chartData[chartId]["dimensions"];
     var prop = graphProp(chartId);
}
//  alert(tabDivVal)
//       var prop = graphProp(chartId);
        var trWidth = divwidth / 8;
//   $("#pieWithMap").html("");
//if(data[tabDivVal]!== "undefined"){
//    chart12 = data[tabDivVal];
    columns = chartData[chartId]["viewBys"];
measureArray = chartData[chartId]["meassures"];
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
   var divHght1=divHght-17;
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
        htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"height:"+divHght1+"px;overflow: hidden; \">";
        }else{
             htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"height:"+divHght1+"px; width:100%;overflow: hidden;\">";
        }
        htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;overflow-x:hidden;'>";
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

          htmlstr += "<td >"+addCommas(d[measureArray[b]])+"</td>";

     });
        htmlstr +="</strong></td>"
         htmlstr += "</tr>";
        }
         htmlstr += "</table>";
        }//Transpose of Table
        else{

//htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;overflow-y:auto;overflow-x:none;margin-top:20px;float:right;width:100%;'>";
    if(typeof chartData[chartId]["tableBorder"]!=="undefined" && chartData[chartId]["tableBorder"]=="Y"){
        htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-stripped table-condensed table-bordered display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }else{
//        
   htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-stripped table-condensed display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }      
          
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
//htmlstr += "<th ></th>";

 if(typeof chartData[chartId]["tableWithSymbol"]!=="undefined" && chartData[chartId]["tableWithSymbol"]=="Y" && columns.length>1){
    try {
   htmlstr += "<th style='white-space:nowrap;font-weight:bold;font-size:small;text-align:left' >";
   var columnsTxt='';
        for(var z=0;z<columns.length;z++){
            columnsTxt+=columns[z];
            if(z!==columns.length-1){
                columnsTxt +=",";
            }
    }
    htmlstr += columnsTxt+"</th>"
    } catch (e) {
 }
 }else {
    for(var q in columns){
htmlstr += "<th style='font-weight:bold;text-align:left;font-size:small;' >"+columns[q]+"</th>";
} 
 }


for(var i in measureArray){  // add by mayank sh.
var measureName='';
    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAlias"][measureArray[i]];
    }else{
        measureName=measureArray[i];
    }
//htmlstr += "<th style='white-space:nowrap;font-weight:bold;font-size: 10px;text-align:left'>"+measureName+"</th>";
var nameAlign='left';
if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measureArray[i]] !== "undefined") {
    nameAlign=chartData[chartId]["measNameAlign"][measureArray[i]];
}
if(measureName.length > 15){
htmlstr += "<th title='"+measureName+"' style='white-space:nowrap;font-size: 10px;font-weight:bold;cursor:pointer;text-align:"+nameAlign+"'>"+measureName.substring(0,15)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;font-size: 10px;text-align:"+nameAlign+"'>"+measureName+"</th>";
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
            var color="";
//            if (num === 0) {
//                color = "white";
//            } else {
//                color = "white";
//            }
//              
//Added by shivam
 var colorfi  = getcolorValueFunction(chartId,chartData,drillShade,data[chartId],columns,measureArray,i,color) 
 
 var TableFontColor="";
 if(typeof chartData[chartId]["TableFontColor"]!=="undefined" && chartData[chartId]["TableFontColor"] !=""){
            TableFontColor=chartData[chartId]["TableFontColor"];
  color = TableFontColor;
    }
 
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            htmlstr = htmlstr + "<tr>"  ;
//            htmlstr += "<td  style=\"background-color:" + color + ";\" width=25px>"
//            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="#A9A9A5" /></svg>';
//            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              if(typeof chartData[chartId]["tableWithSymbol"]!=="undefined" && chartData[chartId]["tableWithSymbol"]=="Y" && columns.length>1){
                     try{
                         
                     if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {
  htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"white-space:nowrap;font-size:small;background-color:" + drillShade + ";color:black\" width=" + trWidth + ">";
               }else{
  htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"white-space:nowrap;font-size:small;background-color:" + color + ";\" width=" + trWidth + " >";     
               }
   var columnsTxt1='';
        for(var k=0;k<columns.length;k++){
            columnsTxt1+=d[columns[k]];
            if(k!==columns.length-1){
                columnsTxt1 +="-";
            }
    }
    htmlstr += columnsTxt1+"</td>"
                     } catch (e) {
                     }
               } else{
                 
                 for(var l=0;l<viewBys.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {

                           htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"white-space:nowrap;font-size:small;background-color:" + drillShade + ";color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                    
                       if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){                
                      
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"white-space:nowrap;font-size:small;background-color:" + colorfi + ";\" width=" + trWidth + " >" + d[columns[l]] + "</td>";
                   }else{
                        
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"white-space:nowrap;font-size:small;background-color:;\" width=" + trWidth + " >" + d[columns[l]] + "</td>";
               }
//                  var colorfi  = getcolorValueFunction(chartId,chartData,drillShade,data[chartId],columns,measureArray,i,color)
                    }  
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
            var valueAlign='left';
            if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measureArray[j]] !== "undefined") {
                valueAlign=chartData[chartId]["measValueAlign"][measureArray[j]];
            }
            if(yAxisRounding>0){
               if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){  
            if(typeof d[measureArray[j]]=="undefined" || d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:"+colorfi+";text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + 0 + "</td>";
            }
            else{
                htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:"+colorfi+";text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }
            }else{
                if(typeof d[measureArray[j]]=="undefined" || d[measureArray[j]]===""){
                     htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:;text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + 0 + "</td>";
                }
                else{
                  htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:;text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
                } 
            }
            }
            else{
                 if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){  
            if(typeof d[measureArray[j]]=="undefined" || d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:" +colorfi+ ";text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + 0 + "</td>";
            }else{
                htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:" +colorfi+ ";text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }
        }else{
            if(typeof d[measureArray[j]]=="undefined" || d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:;text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + 0 + "</td>";
            }else{
           htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:;text-align:"+valueAlign+";color:black;\" width=" + trWidth + ">" +addCurrencyType(chartId, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }        
            }  
//            htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:"+valueAlign+";color:black;font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
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
                                htmlstr += "<td style=\"font-size:small;background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
                            else if(parseInt(d[targetValue[0]])==targetValue[1]){
                                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='normal_face.png';
            }
                else{
                    imageName='amber.png';
                }
                                htmlstr += "<td style=\"font-size:small;background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
            else{
                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
                    imageName='sad_face.png';
            }
                else{
                    imageName='red.png';
                }
                                htmlstr += "<td style=\"font-size:small;background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
            }
        }
                    }
                }    
        }

            htmlstr = htmlstr + "</tr>";
        });    
                var colspan = columns.length +1;
if(typeof chartData[chartId]["showGT"]!=='undefined' && chartData[chartId]["showGT"]!=='' && chartData[chartId]["showGT"]==='Y'){   
    var viwbytdforGT="";  
    for(var l=0;l<viewBys.length-1;l++){
            viwbytdforGT+="<td style=\"background-color:\" width=" + trWidth + "></td>";
        }
        htmlstr +="<tfoot>";
          htmlstr += "<tr style='background-color:#D1D1D7;color:black'>";
          htmlstr += "<td colspan='"+ colspan  +"' style=\"font-size:small;background-color:\" width=" + trWidth + "><strong>Grand Total</strong></td>";
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

 var valueAlign='left';
            if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measureArray[no]] !== "undefined") {
                valueAlign=chartData[chartId]["measValueAlign"][measureArray[no]];
          }

          htmlstr = htmlstr + "<td  style=\"font-size:small;background-color:" + color + ";text-align:left\" width=" + trWidth + " >" +addCurrencyType(chartId, getMeasureId(measureArray[no]))+addCommas(KPIresult[no])+ "</td>";
//     alert(addCommas(numberFormat(KPIresult[no],yAxisFormat,yAxisRounding,chartId)))      
//    alert(KPIresult[no])
//          }    
}
            htmlstr += "</tr>";
            htmlstr +="</tfoot>";
        }
        
            

htmlstr += "</table></div></td></tr></table>";
        }
//htmlstr += "</div>";
  if(chartIdTile==div+"Tile" ){
        $("#openTableTile").html(htmlstr)
    }else if($("#chartType").val()=="India-Map-Dashboard" || $("#chartType").val()=="India-Map-With-Trend"){
     $("#part3").html(htmlstr);
    }else{
        $("#" + div).html(htmlstr);
    }
     var table = $('#chartTable'+div).dataTable( {
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": true,
           "dom": 'T<"clear">rtp',
//           "fixedColumns":   {
//            leftColumns: 1,
//            rightColumns: 1
//        },
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
          "scrollY": ""+divHght *.80+"px"
//          "order": [[ 1, "desc" ]]  
          } );
//chartTable
 }

function buildCrossTable(chartId,data,  columns, measureArray, divwidth, divHght){

  var chartData = JSON.parse(parent.$("#chartData").val());
 graphProp(chartId);
//        var trWidth = divwidth / 8;
        var divHght1=divHght-17;
        var crossTabHeaderList = chartData[chartId]["crossTabFinalOrder"];
        var crossTabMap = chartData[chartId]["crossTableHeaderMap"];
        var headerKeys = Object.keys(chartData[chartId]["crossTableHeaderMap"]);
        var columnViewBy = chartData[chartId]["colViewBys"];
        if(typeof columnViewBy=="undefined"){
            columnViewBy = columns[0];
        }
              var colSpan = measureArray.length;
 
        var list = [];
        for(var k in crossTabHeaderList){
            for(var i in headerKeys){
                if(headerKeys[i]==crossTabHeaderList[k]){
                list.push(headerKeys[i]);
}
            }
        }
 var htmlstr = "";
      htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + chartId + "tablediv\"  style=\"height:"+divHght1+"px; width:100%;overflow: hidden;\">";
     htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;overflow-x:hidden;'>";
        
      htmlstr = htmlstr + "<table id=\"chartTableCross" + chartId + "\" class='table table-stripped table-condensed display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
      htmlstr += "<thead>";
        
      htmlstr +="<tr>";
      htmlstr +="<td style='font-weight:bold;text-align:center'>"+columnViewBy[0]+"</td>";
    var columnNameMap = {};
    for(var k in crossTabHeaderList){
         columnNameMap[crossTabMap[list[k]][0]] = crossTabMap[list[k]][0];
         
}
      var columnKeys = Object.keys(columnNameMap);
      for(var m in columnKeys){
          htmlstr +="<td colspan='"+colSpan+"' style='font-weight:bold;text-align:center'>";
          htmlstr +=columnKeys[m]+"</td>";
            }
            
      htmlstr +="</tr>";
      htmlstr +="<tr>";
      htmlstr +="<td style='font-weight:bold;text-align:center'>"+columns[0]+"</td>"
      for(var k in crossTabHeaderList){
          var measureName = crossTabMap[list[k]][1];
            
          htmlstr +="<td style='font-weight:bold;text-align:center'>"
          htmlstr +=measureName+"</td>"
      }
      htmlstr +="</tr>";
      htmlstr += "</thead>";
      htmlstr += "<tbody>";
      for(var i=0;i<data.length;i++){
      htmlstr +="<tr>";
      var rowDataName = data[i][columns[0]];
     htmlstr += "<td>";
     htmlstr += rowDataName+"</td>";
     for(var m=0;m<list.length;){
      for(var q in measureArray){
         var measureValue = data[i][measureArray[q]+"_"+list[m]];
         htmlstr += "<td style='text-align:center'>";
               
//      htmlstr += addCommas(numberFormat(measureValue, yAxisFormat, yAxisRounding,chartId))+"</td>";
      htmlstr += addCommas(measureValue)+"</td>";
      m++;
           }
      
           }
      htmlstr +="</tr>";    
    }
     
      htmlstr += "</tbody>";
       htmlstr += "</table>"; 
      htmlstr += "</div></div>";
        
        
$("#"+chartId).html(htmlstr);
var table = $('#chartTableCross'+chartId).dataTable({
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": true,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
          "scrollY": ""+divHght1 *.80+"px"
//          "order": [[ 1, "desc" ]]  
          });

}



    function bubbleDbMultiView(div, data0, columns, measures, divWidth, divHeight, layout) {
    var html = "<div id='bubbleDb' style='display:none'></div>";
    var columnsVar = columns;
    var colorCount=0;
    $("#"+div).append(html);
     var color = d3.scale.category12();
     var chartData = JSON.parse($("#chartData").val());
     var chartDetails = chartData["chart1"];
    
var nodes = [];
    var data = [];
    var drillShade = "#91FF00";
    layout=50;
    var measure1 = measures[0];
    var chartData = JSON.parse($("#chartData").val());
    var colIds;
    if(div=="Hchart1"){
        colIds = chartData["chart1"]["viewIds"];
    }else{
        colIds = chartData[div]["viewIds"];
    }
    var chartMap = {};
    var fun = "drillWithinchart1(this.id,\""+div+"\")";
    var isDashboard = parent.$("#isDashboard").val();
    var chartType = parent.$("#chartType").val();
    var nodes = [];
     var margin = {
      top:0,
//        right: 0,
        right: (divWidth*.10),
//        bottom: 70,
        bottom: (divHeight),
        left: 30
    };
    var colorGroup = {};

var nodes =[];
var measureMap = {};
 var rows =500;
                data0.forEach(function(d, i) {
//                    if (i < rows && d[measures[0]] >= 0) {
                    if (i < rows ) {
                        var node = {};
                        node["name"] = d[columns[0]];

                        if (columns.length > 1) {
                            node["name1"] = d[columns[1]];
                        } else {
                            node["name1"] = d[columns[0]];
                        }
                        for (var no = 0; no < measures.length; no++) {
                            if (no == 0 && d[measures[no]]>=0) {
                                node["value"] = d[measures[no]];
                                measureMap[measures[no]] = "value";
                            } else {
                                if(d[measures[no]] >=0){
                                node["value" + no] = d[measures[no]];
                                measureMap[measures[no]] = "value" + no;
                                }
                            }
                        }
                    }
                    return nodes.push(node)
                });
data = nodes;
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var barsWidthTotal = $(window).width() - 400;
    var legendOffset = 60;
    var legendBulletOffset = 130;
    var legendTextOffset = 20;
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    svg = d3.select("#" + div) .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWidth - layout)+" "+(divHeight  - 50 )+" ")
            .classed("svg-content-responsive", true)
            .attr("id", "bubble");
            //Added by Ram
           var legends='';
           var arr=[];
            for(var k in columns){
                chartMap={};
                for(var i=0;i<data0.length;i++)
                {
                    if ( arr.indexOf(data0[i][columns[k]]) == -1){
                        chartMap[data0[i][columns[k]]]=color(i);
                    }
                }
                 var  html1 ="<input type='hidden' id='legends"+k+"' name='legends' />";
                 $("#legendsDiv").append(html1);
                parent.$("#legends"+(k)).val(JSON.stringify(chartMap));
            }
// End Ram code
//    parent.$("#legends").val(JSON.stringify(chartMap));
    var selection = svg.selectAll("g.node")
            .data(bubble_layout.nodes({
                children: data
            }).filter(function(d) {
                return !d.children;
            }));

    var node = selection.enter().append("g")
            .attr("class", "node")
            .attr("transform", function(d) {
                var top = d.y-20;
                return "translate(" + (d.x-divWidth*.10) + ", " + top + ")";
            }).filter(function(d) {
        return d.value;
    });
    node.append("circle")
            .attr("r", function(d) {
                if(data.length==1){
                    return parseFloat(d.r)/2;
                }else {
                return d.r;
                }
            })
            .attr("color_value", function(d, i) {
                    
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
                        if(typeof colorGroup[d.name1]!="undefined" ){
                            return colorGroup[d.name1]
                        }else{
                            
                        colorGroup[d.name1]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount++);
                }
                    }
                        else{

                        if(typeof colorGroup[d.name]!="undefined" ){
                            return colorGroup[d.name]
                        }else{
                            colorCount++;
                        colorGroup[d.name]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                        }
                    }
                    }

            })
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .attr("fill", function(d, i) {
              
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
                        if(typeof colorGroup[d.name1]!="undefined" ){
                            return colorGroup[d.name1]
                        }else{
                            colorCount++;
                        colorGroup[d.name1]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                }
                    }
                        else{

                        if(typeof colorGroup[d.name]!="undefined" ){
                            return colorGroup[d.name]
                        }else{
                            colorCount++;
                        colorGroup[d.name]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                        }
                    }
                    }
            })
            .attr("id", function(d) {
             
                return d.name + ":" + d.name1 + ":" + measures[0];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace("_", "-")+div;
                    try {
                        var selectedBar = d3.selectAll(barSelector);
                        selectedBar.style("fill", drillShade);
                    } catch (e) {
                    }
                }
                d3.select(this).attr("stroke", "grey");
                var content = "";
                for (var no = 0; no < measures.length; no++) {
   var msrData;
    if(typeof chartDetails["rounding"]!=="undefined"){
        yAxisRounding=chartDetails["rounding"];
    }else{
       yAxisRounding=0;
    }
    if(typeof chartDetails["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartDetails["yAxisFormat"];
    }else{
       yAxisFormat="";
    }
                        msrData = numberFormat(d.value, yAxisFormat, yAxisRounding,div);
                        msrData = addCommas(msrData);
                    if (typeof columnMap[columns[0]] !== "undefined" && columnMap[column[0]]["displayName"] !== "undefined" && typeof columnMap[measure1] !== "undefined" && columnMap[measure1]["displayName"]) {
                        if (no === 0) {
                            content += "<span class=\"name\">" + columnMap[columns[0]]["displayName"] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columnMap[columns[1]]["displayName"] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + columnMap[measures[no]]["displayName"] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                    } else {
                        if (no === 0) {
                            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + measures[no] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                    }
                }
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                }
                hide_details(d, i, this);
            });

    node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "8pt")
            .attr("fill", function(d, i){
               if (typeof chartData[div]["lableColor"]!=="undefined" && chartData[div]["lableColor"] !== "default") {
                              lableColor = "#FFFFFF";
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
            .text(function(d) {
//                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
//                    return d.name.substring(0, d.r / 5);
//                }
//                else {
                    return d.name1.substring(0, d.r / 5);
//                }
            });

    if (columns.length > 1) {
        var legends = [];
        bubble_layout.nodes({
            children: data
        }).filter(function(d) {
            return !d.children;
        })
                .filter(function(d) {
                    return !d.children;
                }).forEach(function(d) {
            if (legends.indexOf(d.name) === -1)
                legends.push(d.name);
        });

        svg.selectAll(".circle")
//                .data(legends).enter().append("svg:circle") // Append circle elements
                .attr("cx", barsWidthTotal + legendBulletOffset)
                .attr("cy", function(d, i) {
                    return legendOffset + i * 20 + 5;
                })
                .attr("stroke-width", ".5")
                .style("fill", function(d, i) {
                    return chartMap[d];
                    //            return color(i)
                }) // Bar fill color
                .attr("index_value", function(d, i) {
                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })

                .attr("class", function(d, i) {
                    return "bars-Bubble-legendBullet-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("r", 5)
                .attr("color_value", function(d, i) {
                    return chartMap[d];
                    //            return color(d);
                }) // Bar fill color...
                .on('mouseover', synchronizedMouseOver)
                .on("mouseout", synchronizedMouseOut);
        svg.selectAll("g.legend")
//                .data(legends) // Instruct to bind dataSet to text elements
//                .enter().append("g") // Append legend elements
                //.attr("xlink:href", function(d) { return d.link; })
                .append("text")
                .attr("text-anchor", "left")
                .attr("x", barsWidthTotal + legendBulletOffset + legendTextOffset)
                .attr("y", function(d, i) {
                    return legendOffset + i * 20 - 5;
                })
                .attr("dx", 0)
                .attr("dy", "1em") // Controls padding to place text above bars
                .text(function(d) {
                    return d;
                })
                .style("fill", function(d, i) {
                    return chartMap[d];
                    ;
                })
                .attr("id", function(d, i) {
                    return d;
                })
                .attr("color", function(d, i) {
                    return d;
                })
                .attr("index_value", function(d, i) {
                    return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("class", function(d, i) {
                    return "bars-Bubble-legendText-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .on('mouseover', synchronizedMouseOver)
                .on("mouseout", synchronizedMouseOut)
                .attr("onclick", "legendDrill(this.id)");
    }
  var displayLength = chartData[div]["displayLegends"]
        var height=divHeight-50;
        var width=divWidth - layout;
        var yvalue=0;
        var rectyvalue=0;
        var rectyvalue1=0;
        var len = parseInt(width-150);
        var rectlen = parseInt(width-200);
        var fontsize = parseInt(width/45);
        var fontsize1 = parseInt(width/50);
        var rectsize = parseInt(width/60);
        var legendsMap ={};
                for(var i=0;i<data0.length ;i++){
                    legendsMap[data0[i][columns[0]]]=data0[i][columns[0]]
}
                var len;
                var keys=Object.keys(legendsMap);
                var keys1=Object.keys(colorGroup);
               
                len=keys.length;
                var legendLength;
                //alert(chartData[div]["legendNo"]);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    len=chartData[div]["legendNo"];
                   // alert('if');

                }
                else{
                   // alert("else");
                   // alert(legendLength);
                    len=(keys.length<15 ? keys.length : 15);
                }
//                alert(legendLength);
                if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
            yvalue = parseInt(divHeight *0.2);

        rectyvalue = parseInt((divHeight *0.21)-10);
//        $("#a_"+div).height(fontsize1*1.5);
//        $("#a_"+div).width(fontsize1*1.5);

        var count = 0;
        var transform = "";
 if(typeof displayLength!="undefined" && displayLength!=""&& displayLength!="Yes"){}
 else
     {

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength!="None")){

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
            .attr("fill", "Black")
            .text(function(d){
              return columns[0]
            })
                            for(var i=0;i<len ;i++){
                     if(data0[i][measures[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
           return colorGroup[keys1[i]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.73  + "," + (yvalue+8) + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  getDrawColor(div, parseInt(i));
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys[i]
            } )
            .text(function(d){
                if(keys[i].length>25){
                    return keys[i].substring(0, 25)+"..";
                }else {
                    return keys[i];
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
            if(typeof displayLength!="undefined" && displayLength!="None" && displayLength!="All"){
               for(var i=0;i<len;i++){
                     if(data[i][measures[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("width", rectsize)
            .attr("height", rectsize)
            

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys[i];
            } )
            .text(function(d){
                if(keys[i].length>25){
                    return keys[i].substring(0, 25)+"..";
                }else {
                    return keys[i];
          }
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
               }}
            }else if(typeof displayLength!="undefined" && displayLength=="All"){
               for(var i=0;i<len;i++){
                      if(data[i][measures[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("width", rectsize)
            .attr("height", rectsize)
            
            
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return keys[i];
            } )
            .text(function(d){
                if(keys[i].length>25){
                    return keys[i].substring(0, 25)+"..";
                }else {
                    return keys[i];
          }
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
            }}
            }else if(typeof displayLength!="undefined" && displayLength=="None"){

            }else{
}
     }
}

function buildDistrictIndiaMap(div,data,columns,measures,divWid,divHgt){
   var chartData = JSON.parse(parent.$("#chartData").val());
   $("#viewMeasureBlock").hide();
   columns = chartData["chart1"]["viewBys"];
   measures = chartData["chart1"]["meassures"];
//     alert(JSON.stringify(data))
divWid = $(window).width()*.50;
//        var columns = JSON.parse(parent.$("#viewby").val());
//            var measures = JSON.parse(parent.$("#measure").val());

            var data1 = data["chart1"];

            var color = d3.scale.category10();
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

                    } else {
                        var map = JSON.parse(parent.$("#measureColor").val());
                        shadingMeasure = map["measure"];
                        isShadedColor = true;
                        var measureData = [];
                        for (var i = 0; i < data1.length; i++) {
                            measureData.push(data1[i][shadingMeasure]);
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
                if (parent.$("#isShaded").val() == "true") {
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
                        for (var i = 0; i < data1.length; i++) {
                            measureData.push(data1[i][shadingMeasure]);
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



var htmlFrame = "";
htmlFrame = "<iframe id='maps' frameborder='0' width='"+divWid+"' height='600px' style='float:left'></iframe>";

htmlFrame += "<div id ='districtTableDiv' align='right' style='overflow: auto; float: right; border: 0px solid; width:40%;height:450px; margin-top: 30px'> </div>";
$("#"+div).append(htmlFrame);
$('#maps').attr('src', 'JS/districts.svg').load(function(data) {

//           $('#map').attr('src', 'JS/districts.svg').load(function() {
//        d3.json("JS/districts.svg", function (json) {
                var $map = $(this).contents();
                $(this).attr("stroke", "black");
                var titles = $map.find('path').map(function() {
                    var parenttitle = $(this).parent().attr('title') || '';
                    $(this).on("click", fun)
                            .on("mouseover", function(d, i) {
                                show_details($(this).attr('title'), columns, measures, this, i)
                            })
                            .on("mouseout", function(d, i) {
                                hide_details(this);
                            });
                    return '0\t' + parenttitle + '\t' + $(this).attr('title');
                }).get();
                titles.sort();
                var $input = $('textarea');
                $input.val(titles.join('\n'));
function fun(id){
    $('#districtTableDiv').html('');
   var id1 = parseInt($(this).attr("name"));
    var htmlvar ="";
                    htmlvar += "<table  width=100% class='table table-bordered table-condensed'  style='overflow: auto; font-size:12px;padding:3px 4px 5px 3px;width:100%;'><thead>";
                    htmlvar += "<tr align='left' class='thClass2'>";
     htmlvar += "<td class='tdClass'>State Name:</td>";
//       htmlvar += "</tr>";
//       htmlvar += "<tr class='tdClass1'>";
     htmlvar += "<td align='center' class='tdClass'>"+$(this).parent().attr('title')+"</td>";
     htmlvar += "</tr>";
                    htmlvar += "<tr class='thClass2'>";
     htmlvar += "<td  class='tdClass'>"+columns[0]+":</td>";
//       htmlvar += "</tr>";
//       htmlvar += "<tr class='tdClass1'>";
     htmlvar += "<td align='center' class='tdClass'>"+data1[id1][columns[0]]+"</td>";
     htmlvar += "</tr>";



     for(var m=0;m<measures.length;m++){
                    htmlvar += "<tr  align='left' class='thClass2'>";
         htmlvar += "<td class='tdClass'>"+measures[m]+":</td>";
//         htmlvar+= "</tr>";
//         htmlvar+= "<tr class='tdClass1'>";
        htmlvar+= "<td  align='center' class='tdClass'>"+addCommas(data1[id1][measures[m]])+"</td>";
         htmlvar+= "</tr>";
                        }
    htmlvar+= "</table>";
                    $('#districtTableDiv').html(htmlvar);
                }
                function show_details(d, columns, measures, element, j) {
                    d3.select(element).attr("stroke", "grey");
                    var content;
                    var title;
                    title = columns;
                    var currName = d.toUpperCase();
                    for (var i = 0; i < columns.length; i++) {
                        content = "<span class=\"name\">" + columns[i] + ":</span><span class=\"value\"> " + currName + "</span><br/>";
                        break;
                    }
                    for (var i = 0; i < measures.length; i++) {
                        var msrData;
                        for (var l = 0; l < data1.length; l++) {
                            if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
                                for (var t = 0; t < measures.length; t++) {
                                    if (parent.isFormatedMeasure) {
                                        msrData = numberFormat(data1[l][measures[t]], round, precition)
                                    }
                                    else {
                                        msrData = addCommas(data1[l][measures[t]]);
                                    }
                                    content += "<span class=\"name\">" + measures[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
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

                function hide_details(element) {
                    d3.select(element).attr("stroke", "black")
                    return tooltip.hideTooltip();
                }


                function drawcolor() {

                    function maximumValue(data, measure) {
    var max;
    for (var j = 0; j < data.length; j++) {

        if (j === 0) {
            max = data[j][measure];
        } else {
            if (max < parseInt(data[j][measure])) {
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
            if (max > parseInt(data[j][measure])) {
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
                if (min > parseInt(data[k][measure])) {
                    min = data[k][measure];
                }
            }
        }
    } catch (e) {
        alert(e);
    }
    return min;
}

                     var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:32%;margin-left:-30%;'></div>";
                    $('#'+div).append(html);
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legends2").append("svg")
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
                        $("#legends2").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else if (parent.$("#shadeType").val() === "gradient") {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legends2").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                        var max = maximumValue(data1, shadingMeasure);
                        var min = minimumValue(data1, shadingMeasure);
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


                        $("#legends2").css("width", "8%");
                    }


                    $map.find('g').each(function() {
                        var state = $(this).attr('title') || '';
                        $(this).find('path').each(function() {
                            var district = $(this).attr('title');
                            var v = "";
                            for (var j = 0; j < data1.length; j++) {
                                if (district.toLowerCase() === (data1[j][columns[0]]).toLowerCase()) {
                                    v = data1[j][measures[0]];
                                    var colorVal;
                                    $(this).attr('fill', function(){
                                        var colorShad;
                                    var strVal = data1[j][measures[0]];
                                    if (isShadedColor) {
                                        strVal = data1[j][shadingMeasure];
                                        colorShad = color(strVal);
                                    } else if (conditionalShading) {
                                        conditionalMeasure = $("#conditionalMeasure").val();
                                        return getConditionalColorIndia(color(j), data1[j][conditionalMeasure]);
                                    } else {
                                        colorShad = color(j);
                                    }
                                    colorVal=colorShad;
                                    return colorShad;
                                    }).attr('id', district.replace(/[^a-zA-Z0-9]/g, '', 'gi'))
                                    .attr('name', j)
                                            .attr("class" ,j)
                                            .attr("title",
                                          function(){
                                              return district+": "+addCommas(v)+" \n"+$(this).parent().attr('title');
                                          }
                                    )
                                    .attr("color_value",colorVal);
                                    //                       $(this).attr('onload',fun1)
                                    //                       $(this).attr("onclick", fuj);
                                    //                       $(this).on("dblclick", function(d, i) {
                                    //                         districtFilterTable(this.id);
                                    //                       });

                                    //                          return "districtTable(this.id)";


                                    //                       $(this).find('path').attr('onclick',districtFilterTable(data1[j]));
                                }
                            }
                                        $(this).mouseenter(function() {
                                                show_details(district, columns, measures, this)
                                            });

                                            $(this).mouseleave(function() {
                                             hide_details(this);
                                            })
                        })
                    })

                }
                ;
                drawcolor();
    districtTable();

        function districtTable()
   {
//    alert(districtTableValue);
    var htmlvar ="";
     var tabCount = 0;
                    htmlvar += "<table id ='districtTable' width=100% class='table table-bordered table-condensed'  style='overflow: auto; font-size:12px;padding:3px 4px 5px 3px;width:100%;'><thead>";
                    htmlvar += "<tr align='left' class='thClass'>";
//                    htmlvar += "<th>State Name</th>";
    for(var i=0;i<columns.length;i++){
    htmlvar += "<th align='left'>"+columns[i]+"</th>";
                    }
    for(var j=0;j<measures.length;j++){
    htmlvar += "<th align='left'>"+measures[j]+"</th>";
                    }
                    htmlvar += "</tr></thead>";



//        for(var l in data1 ){
    for(var k=0;k<data1.length;k++){
           tabCount++;

           if(tabCount%2 ==1){

        htmlvar += "<tr id = 'trID'  align='left' class='tdClass1'>";
//        var head = $("#map").contents().find("path");
//head.each(function(i, obj) {
//    if(this.id==data1[k][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')){
//      htmlvar += "<td align='center' class='tdClass'>"+$(this).parent().attr('title')+"</td>";
//    }
//});
//        htmlvar += "<td align='center' class='tdClass'>"+$(this).parent().attr('title')+"</td>";
       for(var l=0;l<columns.length;l++){
           var idVal=data1[k][columns[l]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
    htmlvar+= "<td  align='left' class='tdClass' id='"+idVal+"' onmouseover='tableHover(this.id)' onmouseout='hoverOut(this.id)'>"+data1[k][columns[l]]+"</td>";
                        }
       for(var m=0;m<measures.length;m++){
    htmlvar+= "<td  align='left' class='tdClass'>"+addCommas(data1[k][measures[m]])+"</td>";
                        }
      htmlvar+= "</tr>";
                    }
           else{

        htmlvar += "<tr id = 'trID'  align='left' class='tdClass2'>";
//                var head = $("#map").contents().find("path");
//head.each(function(i, obj) {
//    if(this.id==data1[k][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')){
//      htmlvar += "<td align='center' class='tdClass'>"+$(this).parent().attr('title')+"</td>";
//    }
//});
       for(var l=0;l<columns.length;l++){
           var idVal=data1[k][columns[l]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
        htmlvar+= "<td  align='left' class='tdClass' id='"+idVal+"' onmouseover='tableHover(this.id)' onmouseout='hoverOut(this.id)'>"+data1[k][columns[l]]+"</td>";
                        }
       for(var m=0;m<measures.length;m++){
        htmlvar+= "<td  align='left' class='tdClass'>"+addCommas(data1[k][measures[m]])+"</td>";
                        }
        htmlvar+= "</tr>";
                    }
                }
//            }


    htmlvar+= "</table>";
                    $('#districtTableDiv').html(htmlvar);

                }
            });
            var svg = d3.select('body').select("svg:svg").call(zoom);
            svg.call(zoom);
            var zoom = d3.behavior.zoom()
                    .on("zoom", function() {
                        g.attr("transform", "translate(" +
                                d3.event.translate.join(",") + ")scale(" + d3.event.scale + ")");
                        g.selectAll("path")
                                .attr("d", path.projection(projection));
                    });

        function tableHover(id) {
var head = $("#map").contents().find("path");
head.each(function(i, obj) {
    if(this.id===id.replace(/[^a-zA-Z0-9]/g, '', 'gi')){
        $(this).css('fill',"#91ff00")
    }
});
            }

            function hoverOut(id) {
    var head = $("#map").contents().find("path");
    head.each(function(i, obj) {
    if(this.id==id.replace(/[^a-zA-Z0-9]/g, '', 'gi')){
        $(this).css('fill',$(this).attr("color_value"))
    }
        });
            }

 }

 function LiquidFilledGauge(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
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
   if(showData>0 && colorPicker=="#091A9C"){
        colorPicker = "#091A9C";

    }else if(showData<=0 && colorPicker=="#091A9C"){
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
        left: 20
    }
     if(fromoneview!='null'&& fromoneview=='true'){
         div=dashletid;
     }
 svg = d3.select("#" + div).append("svg")
//            .attr("width", divWidth )
//            .attr("height", divHeight)
              .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(divWidth*1.3)+" "+(divHeight*1.3 )+" ")
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
       .attr("id","liquidChart"+div);
       var fontsize = "30";
       if(typeof chartData[div]["kpiGTFont"]!=="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
           fontsize = chartData[div]["kpiGTFont"];
        }else {
           fontsize = "30";
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
      if(fromoneview!='null'&& fromoneview=='true'){
         div=dashletid;
     }
      
          loadLiquidFillGauge("liquidChart"+div,textData,KPIResult1,config,divWidth,divHeight,fontsize,measureArray,div)



}
function loadLiquidFillGauge(elementId, value,value1, config,divWidth,divHeight,fontsize,measureArray,div) {
 
   var chartData = JSON.parse(parent.$("#chartData").val());
    var gauge = d3.select("#" + elementId);
//    var chartData = JSON.parse($("#chartData").val());
    var radius = Math.min(parseInt(divWidth), parseInt(divHeight))/2;
    var locationX = parseInt(divWidth)/2 - radius;
    var locationY = parseInt(divHeight)/2 - radius;
    var fillPercent = Math.max(config.minValue, Math.min(config.maxValue, value))/config.maxValue;

    var waveHeightScale;
    if(config.waveHeightScaling){
        waveHeightScale = d3.scale.linear()
            .range([0,config.waveHeight,0])
            .domain([0,50,100]);
    } else {
        waveHeightScale = d3.scale.linear()
            .range([config.waveHeight,config.waveHeight])
            .domain([0,100]);
    }
    var lFilledFont;
    var percentText;
    var textPixels = (config.textSize*radius/2);
    var textFinalValue = parseFloat(value1).toFixed(2);
    var textStartValue = config.valueCountUp?config.minValue:textFinalValue;
    if(textFinalValue>100){
      percentText = config.displayPercent?"":"";   
    }else{
     percentText = config.displayPercent?"%":"";
    }
    var circleThickness = config.circleThickness * radius;
    var circleFillGap = config.circleFillGap * radius;
    var fillCircleMargin = circleThickness + circleFillGap;
    var fillCircleRadius = radius - fillCircleMargin;
    var waveHeight = fillCircleRadius*waveHeightScale(fillPercent*100);

    var waveLength = fillCircleRadius*2/config.waveCount;
    var waveClipCount = 1+config.waveCount;
    var waveClipWidth = waveLength*waveClipCount;

    // Rounding functions so that the correct number of decimal places is always displayed as the value counts up.
    var textRounder = function(value){ return Math.round(value); };
    if(parseFloat(textFinalValue) != parseFloat(textRounder(textFinalValue))){
        textRounder = function(value){ return parseFloat(value).toFixed(1); };
    }
    if(parseFloat(textFinalValue) != parseFloat(textRounder(textFinalValue))){
        textRounder = function(value){ return parseFloat(value).toFixed(2); };
    }

    // Data for building the clip wave area.
    var data = [];
    for(var i = 0; i <= 40*waveClipCount; i++){
        data.push({x: i/(40*waveClipCount), y: (i/(40))});
    }

    // Scales for drawing the outer circle.
    var gaugeCircleX = d3.scale.linear().range([0,2*Math.PI]).domain([0,1]);
    var gaugeCircleY = d3.scale.linear().range([0,radius]).domain([0,radius]);

    // Scales for controlling the size of the clipping path.
    var waveScaleX = d3.scale.linear().range([0,waveClipWidth]).domain([0,1]);
    var waveScaleY = d3.scale.linear().range([0,waveHeight]).domain([0,1]);

    // Scales for controlling the position of the clipping path.
    var waveRiseScale = d3.scale.linear()
        // The clipping area size is the height of the fill circle + the wave height, so we position the clip wave
        // such that the it will won't overlap the fill circle at all when at 0%, and will totally cover the fill
        // circle at 100%.
        .range([(fillCircleMargin+fillCircleRadius*2+waveHeight),(fillCircleMargin-waveHeight)])
        .domain([0,1]);
    var waveAnimateScale = d3.scale.linear()
        .range([0, waveClipWidth-fillCircleRadius*2]) // Push the clip area one full wave then snap back.
        .domain([0,1]);

    // Scale for controlling the position of the text within the gauge.
    var textRiseScaleY = d3.scale.linear()
        .range([fillCircleMargin+fillCircleRadius*2,(fillCircleMargin+textPixels*0.7)])
        .domain([0,1]);

    // Center the gauge within the parent SVG.
    var gaugeGroup = gauge.append("g")
        .attr('transform','translate('+locationX+','+locationY+')');

    // Draw the outer circle.
    var gaugeCircleArc = d3.svg.arc()
        .startAngle(gaugeCircleX(0))
        .endAngle(gaugeCircleX(1))
        .outerRadius(gaugeCircleY(radius))
        .innerRadius(gaugeCircleY(radius-circleThickness));
    gaugeGroup.append("path")
        .attr("d", gaugeCircleArc)
        .style("fill", config.circleColor)
        .attr('transform','translate('+radius+','+radius+')');

    // Text where the wave does not overlap.
    var text1 = gaugeGroup.append("text")
        .text(textRounder(textStartValue) + percentText)
        .attr("class", "liquidFillGaugeText")
        .attr("text-anchor", "middle")
        .style("font-size", fontsize+"px")
        .style("fill", config.textColor)
        .style("font-family","Helvetica")
        .style("font-weight","bold")
        .attr('transform','translate('+radius+','+textRiseScaleY(config.textVertPosition)+')')
        .on("mouseover", function(d, i) {
                show_detailsKPI(d, chartData[div]["viewBys"], measureArray, this,div,value1);
                }
            ).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });



    // The clipping wave area.
    var clipArea = d3.svg.area()
        .x(function(d) { return waveScaleX(d.x); } )
        .y0(function(d) { return waveScaleY(Math.sin(Math.PI*2*config.waveOffset*-1 + Math.PI*2*(1-config.waveCount) + d.y*2*Math.PI));} )
        .y1(function(d) { return (fillCircleRadius*2 + waveHeight); } );
    var waveGroup = gaugeGroup.append("defs")
        .append("clipPath")
        .attr("id", "clipWave" + elementId);
    var wave = waveGroup.append("path")
        .datum(data)
        .attr("d", clipArea);

    // The inner circle with the clipping wave attached.
    var fillCircleGroup = gaugeGroup.append("g")
        .attr("clip-path", "url(#clipWave" + elementId + ")");
    fillCircleGroup.append("circle")
        .attr("cx", radius)
        .attr("cy", radius)
        .attr("r", fillCircleRadius)
        .style("fill", config.waveColor);

    // Text where the wave does overlap.
    var text2 = fillCircleGroup.append("text")
        .text(textRounder(textStartValue) + percentText)
        .attr("class", "liquidFillGaugeText")
        .attr("text-anchor", "middle")
        .style("font-size", fontsize+"px")
        .style("fill", config.textColor)
        .style("font-family","Helvetica")
        .style("font-weight","bold")
//        .style("fill", "#ECE334")
        .style("fill", function(d, i){
         if(typeof chartData[div]["lFilledFont"]!=="undefined" && chartData[div]["lFilledFont"] !=""){
          lFilledFont= chartData[div]["lFilledFont"];
         }else{
             lFilledFont= "#ECE334"  ;
         }
         return lFilledFont;
        })
        .attr('transform','translate('+radius+','+textRiseScaleY(config.textVertPosition)+')')
         .on("click",onMouseClick)
        .on("mouseover", function(d, i) {
                show_detailsKPI(d, chartData[div]["viewBys"], measureArray, this,div,value1);
                }
            ).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
 function onMouseClick(d) {

             if (typeof  openTableDiv == "function") {
                 openTableDiv.call();
                
            }
        }



    // Make the value count up.
    if(config.valueCountUp){
        var textTween = function(){
            var i = d3.interpolate(this.textContent, textFinalValue);
            return function(t) { this.textContent = textRounder(i(t)) + percentText; }
        };
        text1.transition()
            .duration(config.waveRiseTime)
            .tween("text", textTween);
        text2.transition()
            .duration(config.waveRiseTime)
            .tween("text", textTween);
    }

    // Make the wave rise. wave and waveGroup are separate so that horizontal and vertical movement can be controlled independently.
    var waveGroupXPosition = fillCircleMargin+fillCircleRadius*2-waveClipWidth;
    if(config.waveRise){
        waveGroup.attr('transform','translate('+waveGroupXPosition+','+waveRiseScale(0)+')')
            .transition()
            .duration(config.waveRiseTime)
            .attr('transform','translate('+waveGroupXPosition+','+waveRiseScale(fillPercent)+')')
            .each("start", function(){ wave.attr('transform','translate(1,0)'); }); // This transform is necessary to get the clip wave positioned correctly when waveRise=true and waveAnimate=false. The wave will not position correctly without this, but it's not clear why this is actually necessary.
    } else {
        waveGroup.attr('transform','translate('+waveGroupXPosition+','+waveRiseScale(fillPercent)+')');
    }

    if(config.waveAnimate) animateWave();

    function animateWave() {
        wave.transition()
            .duration(config.waveAnimateTime)
            .ease("linear")
            .attr('transform','translate('+waveAnimateScale(1)+',0)')
            .each("end", function(){
                wave.attr('transform','translate('+waveAnimateScale(0)+',0)');
                animateWave(config.waveAnimateTime);
            });
    }
}
function liquidFillGaugeDefaultSettings(){
    return {
        minValue: 0, // The gauge minimum value.
        maxValue: 100, // The gauge maximum value.
        circleThickness: 0.05, // The outer circle thickness as a percentage of it's radius.
        circleFillGap: 0.05, // The size of the gap between the outer circle and wave circle as a percentage of the outer circles radius.
        circleColor: "#178BCA", // The color of the outer circle.
        waveHeight: 0.05, // The wave height as a percentage of the radius of the wave circle.
        waveCount: 1, // The number of full waves per width of the wave circle.
        waveRiseTime: 1000, // The amount of time in milliseconds for the wave to rise from 0 to it's final height.
        waveAnimateTime: 2000, // The amount of time in milliseconds for a full wave to enter the wave circle.
        waveRise: true, // Control if the wave should rise from 0 to it's full height, or start at it's full height.
        waveHeightScaling: true, // Controls wave size scaling at low and high fill percentages. When true, wave height reaches it's maximum at 50% fill, and minimum at 0% and 100% fill. This helps to prevent the wave from making the wave circle from appear totally full or empty when near it's minimum or maximum fill.
        waveAnimate: true, // Controls if the wave scrolls or is static.
        waveColor: "#178BCA", // The color of the fill wave.
        waveOffset: 0, // The amount to initially offset the wave. 0 = no offset. 1 = offset of one full wave.
        textVertPosition: .5, // The height at which to display the percentage text withing the wave circle. 0 = bottom, 1 = top.
        textSize: 1, // The relative height of the text to display in the wave circle. 1 = 50%
        valueCountUp: true, // If true, the displayed value counts up from 0 to it's final value upon loading. If false, the final value is displayed.
        displayPercent: true, // If true, a % symbol is displayed after the value.
        textColor: "#091A9C", // The color of the value text when the wave does not overlap it.
        waveTextColor: "#A4DBf8" // The color of the value text when the wave overlaps it.
    };
}


function buildAdvancePie(div,divId, data, columns, measureArray,wid,hgt) {
    var color = d3.scale.category12();
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
    
    var divWidth, divHeight, rad;
    var w = $(window).width() * .75;
    divWidth=$(window).width() * .75;
//     divWid=parseFloat($(window).width())*(.35);
    divHeight=hgt;
    rad=divHeight * .55;
var chartData = JSON.parse($("#chartData").val());
var colIds=chartData[div]["viewIds"];
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
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie() //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
    svg = d3.select("#" + divId).append("svg:svg")
            .datum(data)
            .attr("width", height)
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
                        drills = JSON.parse(parent.$("#drills")
                        .val());
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

//                chartMap[d[columns[0]]] = colorShad;
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

            .attr("transform", "translate(" + height / 2 + "," + height / 2 + ")")
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) ;

    arcs.append("path")
            .attr("fill", function(d,i) {
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse($("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
//                return color(i);}//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
var colorfill = getDrawColor(div, parseInt(i))
                return colorfill;
                }
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);
                show_details(d.data, columnList, measureArray, this,"chart1");

            })
            .on("mouseout", function(d, i) {
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
            .attr("fill", function(d, i){
            var lableColors;
                   if (typeof chartData[div]["labelColors"]!=="undefined") {
                              lableColors = chartData[div]["labelColors"];
                          }else {
                               lableColors = "#000000";
                               }
                               return lableColors;
             })
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
            .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
//if(typeof dataDisplay !=="undefined" && dataDisplay === "Yes")  {
//if (typeof displayType !== "undefined" && displayType === "Absolute") {
//                    if (isFormatedMeasure) {
//                        return numberFormat(d.data[measureArray[0]], round, precition);
//                    }
//                    else {
//                        return autoFormating(d.data[measureArray[0]], autoRounding1);
//                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding);
////                    }
//                }
//                else {
                if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes')
                    {
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                    }
                    else
                        {
                            return '';
                        }
//                }
//            }else {
//                return "";
//            }
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


     var html ="";
//var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var colName=columns[0];
//            if(viewOvIds.indexOf(columns[0])!="undefined"){
//                colName=viewOvName[viewOvIds.indexOf(columns[0])];
//            }
     if(columns[0].length > 11){
          html ="<div style='white-space:nowrap;height:"+height+"px;float:right;margin-right:100px;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:10px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:20px;text-decoration:none' >"+columns[0].substring(0,11)+"..</span></td></tr>";
     }else {
          html ="<div style='white-space:nowrap;height:"+height+"px;float:right;margin-right:100px;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:10px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:20px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
    }

    for(var m=0;m<data.length;m++){
        if(data[m][columns[0]].length > 12 ) {
    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='8' height='8' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;font-size: 9px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 12)+"..</span> </td></tr>";
    }
else {
    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='8' height='8' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;font-size: 9px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
}
}
    html +="</table></td></tr></table></div>";
    $("#"+divId).append(html);
}


function buildAdvanceHorizontal(div,divId, data, columns, measureArray, divWid, divHgt) {

var fromoneview=parent.$("#fromoneview").val();
var dashletid;
     var color = d3.scale.category10();
    var chartMap = {};
var chartData = JSON.parse(parent.$("#chartData").val());
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
//     divWid=parseFloat($(window).width())*(.35);
    var wid = divWid;
    var hgt = divHgt;
//    var w = wid - 150;
//    var h = hgt - 100;
    var w = wid ;
    var h = hgt - 100;
    var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
dashletid=div;
divHgt=divHgt+50
}else{
       var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}

    var max = maximumValue(data, measureArray[0]),
            num_ticks = 1,
            left_margin = 100,
            right_margin = 100,
            top_margin = 30,
            bottom_margin = 0;
    //    color1 = function(id) {
    //        return 'steelblue'
    //    };

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
                    } else if (parent.$("#shadeType").val() == "gradient"){
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
//var colIds = chartData[div]["viewIds"];
    var measArr = [];
//    xAxis = d3.svg.trendaxis()
//            .scale(x)
//            .orient("bottom");
    var x = d3.scale.linear()
            .domain([0, max])
            .range([0, w - (left_margin + right_margin)]),
//            .range([0, w + 50]),
      y = d3.scale.ordinal()
            .domain(d3.range(data.length))
            .rangeBands([10, h+top_margin-10], .4);
  var xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");

//    yAxis = d3.svg.trendaxis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d, i) {
//                    return addCommas(d);
//                });
    var chart_top = h - y.rangeBand() / 2 ;
    var chart_bottom = bottom_margin + y.rangeBand() / 2;
    var chart_left = left_margin;
  
    var vis = d3.select("#"+divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg:svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt+10 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", divWid)
//            .attr("height", divHgt-50)
            .append("svg:g")
            .attr("id", "barchart")
            .attr("class", "barchart");




    var gradient = vis.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
                return "gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "50%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(90)");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", function(d, i) {
//                           var colorShad;
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


 if(fromoneview!='null'&& fromoneview=='true'){}else{
    parent.$("#colorMap").val(JSON.stringify(colorMap));}
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
//                 var colorShad;
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
//                return colorShad;
            })
            .attr("stop-opacity", 1);
     if(fromoneview!='null'&& fromoneview=='true'){
  }else{
    parent.$("#colorMap").val(JSON.stringify(colorMap));
  }
//    vis.append("svg:rect")
//            .attr("width", divWid)
//            .attr("height", h+50)
//            .attr("style", "margin-top:30px;")
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var rules = vis.selectAll("g.rule")
            .data(x.ticks(num_ticks))
            .enter()
            .append("svg:g")
            .attr("transform", function(d)
            {
                return "translate(" + (chart_left + x(d)) + ")";
            });
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
     for (var j=150; j < divWid*.9; j=j+50) {
        vis.append("svg:line")
//    .attr("transform", "translate(0," + w + ",0)")
        .attr("x1", j)
        .attr("y1", chart_bottom+10)
        .attr("x2", j)
        .attr("y2", chart_top+10)
        .style("stroke", "#A69D9D")
        .style("stroke-width", 1)

//        .style("z-index", "9999");
};
}


    rules.append("svg:line")
            .attr("class", "tick")
            .attr("y1", chart_top)
            .attr("y2", chart_top + 4)
            .attr("stroke", "black");


    rules.append("svg:text")
            .attr("class", "tick_label")
            .attr("text-anchor", "middle")
            .attr("y", chart_top)
            .text(function(d)
            {

                return d[measureArray[0]];
            });
    var bbox = vis.selectAll(".tick_label").node().getBBox();
    vis.selectAll(".tick_label")
            .attr("transform", function(d)
            {
                return "translate(0," + (bbox.height) + ")";
            });

    var bars = vis.selectAll("g.bar")
            .data(data)
            .enter()
            .append("svg:g")
            .attr("class", "bar")
            .attr("transform", function(d, i) {
                return "translate(0, " + y(i) + ")";
            });

   vis.append("g")
            .attr("class", ".x axis")
//            .attr("transform", "translate(100," +(w)+ ")")
            .attr("transform", "translate(100," +(h+top_margin )+ ")")
            .attr("x", 5)
           .call(xAxis)

            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
            if(typeof displayX !=="undefined" && displayX=="Yes"){
             if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
            }else{
                return "";
            }
            })
            .attr('transform', 'rotate(-35)')

            .append("svg:title").text(function(d) {
        return d;
    });

//    svg.append("g")
//            .attr("class", "y axis")
//            .call(yAxis)
//            .append("text")
//            .attr("transform", "rotate(-90)")
//            .attr("y", 6)
//            .attr("dy", ".71em")
//            .style("text-anchor", "end");


    bars.append("svg:rect")
            .attr("x", right_margin)
            .attr("width",0)
            .transition()
            .duration(2000)//1 second   
            .attr("width", function(d) {
                return (x(d[measureArray[0]]))

            })


            .attr("height", y.rangeBand())
            .attr("fill", function(d,i) {
                var colorShad;
                var strVal = data[i][measureArray[0]];
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
                         var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
//                            colorShad = "steelblue";
                        }
                    }
                    }
                }
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//colorShad="steelblue";
                return colorShad;
            })
            .attr("stroke", function(d) {
//                return "url(#gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
             .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[0]];
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
            })
            .attr("color_value", function(d,i){
                 var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
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
                            colorShad = color(i);

//                            colorShad = "steelblue";
                        }
                    }
                    }
                }
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//colorShad="steelblue";
                return colorShad;
            })

            .attr("onclick", fun);
             bars.on("mouseover", function(d, i) {
                 if(fromoneview!='null'&& fromoneview=='true'){
show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                 }else{
                 var bar = d3.select(this);
                    var barSelector = "." + "bars-Bubble-" + "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
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
                   var barSelector = "." + "bars-Bubble-" + "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                 }
                hide_details(d, i, this);

            });

    var labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("class", "x.axis")
            .attr("x", 5)
            .attr("y", 30)
            .text(function(d, i) {
                if(typeof displayY !=="undefined" && displayY =="Yes"){
                if (data[i][columns[0]].length < 12)
                    return data[i][columns[0]];
                else
                    return  data[i][columns[0]].substring(0, 12) + "..";
                }else {
                    return "";
                }
            })
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 11px")
            .attr('transform', 'rotate(-20)');

//    var labels1 = vis.selectAll("g.bar")
//            .append("svg:text")
//            .attr("class", ".x.axis")
//
//            .text(function(d, i) {
//                if (data[i][measureArray[0]].length < 12)
//                    return data[i][measureArray[0]];
//                else
//                    return  data[i][measureArray[0]].substring(0, 12) + "..";
//            })
//            .attr("style", "font-family: lucida grande")
//            .attr("style", "font-size: 11px");
//            .attr('transform', 'rotate(-20)');
    if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
        {
            var font=14;
            var h1=$("#"+div).height();
            font=h1/30.2;
            font=font>14?14:font;
            font=font<10?10:font;
            // add by maynk sh. for Afontsize
             if(typeof chartData[div]["Afontsize"]!=="undefined"){
                      font =  chartData[div]["Afontsize"];
                     
             }// end by maynk sh. for Afontsize
            
            labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("transform", function(d) {
                return "translate(" +(100+bbox.width/100)+ ", " + (y.rangeBand() / 2+(bbox.height/5)) + ")";
            })
            .attr("style","font-size:"+font+"px;transform-origin: bottom left 0px;")
            .attr("class", "valueLabel")
//            .attr("fill", labelColor)
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
    var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-34%;margin-left:87%;'></div>";
                    $('#'+div).append(html);
//                    html += "<div id='legends' class='legend1' style='float:left;align:rigth;overflow: visible;margin-top:30%;margin-left:-50%;'></div>";
//                    $('body').append(html);
                    var svg1 = d3.select("#legends2").append("svg")
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
                        $("#legends2").css("width", 500 * .15);
//                            $("#legends").css("margin", "3% 84% auto");
                    } else if (parent.$("#shadeType").val() === "gradient") {
                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legends2").height() - 10;
                        var shadingMeasure = colorMap["measure"];
                        var max = maximumValue(data, shadingMeasure);
                        var min = minimumValue(data, shadingMeasure);
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


                        $("#legends2").css("width", "8%");
                    }
   
   
   var bbox = labels.node().getBBox();
    vis.selectAll(".label")
            .attr("transform", function(d) {
                return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 4) + ")";
            });
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
//    labelColor = "black";

    var rightOffset = wid + 160;
    labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("transform", function(d) {
                var xvalue = (x(d[measureArray[0]]) - 80) < 20 ? x(d[measureArray[0]]) + 119 : x(d[measureArray[0]]) + 119;
                return "translate(" + xvalue + ", " + (y.rangeBand() / 2) + ")";
            })
            .attr("text-anchor", "middle")
            .attr("class", "valueLabel")
//            .attr("fill", labelColor)

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


    bbox = labels.node().getBBox();
    vis.selectAll(".NetMarginAmt")
            .attr("transform", function(d)
            {
                return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 2) + ")";
            });
//    vis.append("svg:line")
//            .attr("class", "axes")
//            .attr("x1", chart_left)
//            .attr("x2", chart_left)
//            .attr("y1", chart_bottom)
//            .attr("y2", chart_top)
//            .attr("stroke", "black");

           vis.append("svg:g")
            .attr("class", "x axis")
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("x", -75)
            .attr("fill","steelblue")
            .attr("dx", ".71em")
            .style("text-anchor", "end")
            .text("" + measureArray[0] + "");

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{    
var count=0;  
var boxW=divWid;
var boxH=divHgt*.9;
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
else{
    rectW=30+(len*7)+85;
}
rectW = rectW<170?170:rectW;
var viewByHgt=15;
var rectH=0;  // horizontalbar
if(typeof chartData[div]["lbPosition"]==='undefined'  || chartData[div]["lbPosition"] === "top"){
    rectH=17;
}
else{
    rectH=10+17+viewByHgt;
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=boxW-rectW-20;
}
else if ( chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "bottomright" ){
    rectX=boxW-rectW+(rectW/4)
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else{
    rectX=boxW/2-rectW/2;
}
var rectY;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)+20;
}
else if(chartData[div]["lbPosition"] === 'bottomleft' || chartData[div]["lbPosition"] === 'bottomcenter' || chartData[div]["lbPosition"] === "bottomright" ){
    rectY=boxH-rectH-(boxH*0.2);
}
else{
    rectY=10;
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
vis.append("g")
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
     vis.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+13+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
        }
        else{
            vis.append("g")
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
var offset1=0,offset2=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           offset1=(columns[0].length*6.5+20);
           offset2=-23;
       }
   vis.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(0)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        var measureName='';
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!='' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
            measureName=chartData[div]["measureAlias"][measureArray[0]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[0]);
        }
//var length=0;
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
               return measureArray[0];
           })
           var rectsize;
           if(divWid<divHgt){// stacked
              rectsize = parseInt(divWid/25);
           }
           else{
              rectsize = parseInt(divHgt/25);
           }
           rectsize=rectsize>10?10:rectsize;
           vis.append("g")
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

 function buildQuickLine(div, data, columns, measureArray, divWid, divHgt,columnId) {
//    divHgt=450;
//var color = d3.scale.category10();
 var measArr = [];
    var measure1 = measureArray[0];
    var fromoneview=parent.$("#fromoneview").val();
    var widthdr=divWid
    var divHgtdr=divHgt

    var chartMap = {};
    var sum = d3.sum(data, function(d) {
        return parseFloat(d[measureArray[0]]);
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
    if(typeof window.idArr !=="undefined"){
          if(typeof idArr !=="undefined"){
        $("#spanChartNameDate"+div).text(window.idArr.split(":")[0]);
    }
    }
    
  var fun = "openQuickForward(this.id,\""+div+"\",$(this).attr('cx'),$(this).attr('cy'))";
    if(fromoneview!='null'&& fromoneview=='true'){
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();
 var regid=div.replace("Dashlets-","");
  fun = "drillWithinchart11(this.id,\""+div+"\",\""+widthdr+"\",\""+divHgtdr+"\",\""+regid+"\",\""+oneviewid+"\",\""+repname+"\",\""+repId+"\",\""+div1+"\",'null','null')";
    if(div=='OLAPGraphRegion'){
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
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}
    var margin = {};

    var width = 0;
    var height = 0;

     margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 80
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .75;


    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max =0;
    var minVal = 0;



    var y = d3.scale.linear().range([height, 0]);
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
    // Axis setting

    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d) {
                   if(yAxisFormat==""){
                       return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
//                     if(typeof displayY !=="undefined" && displayY =="Yes"){
                    if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
//                     }
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



//    svg = d3.select("#" + div)
    var svg = d3.select("#"+div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgt)+" ")
            .classed("svg-content-responsive", true)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    svg.append("g")
    .append("svg:svg")
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
            .attr("class", "y axis")
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", -75)
            .attr("fill","steelblue")
            .attr("dy", ".41em")
            .style("text-anchor", "end");
//            .text("" + measure1 + "");

    var min1 = [];
    var flag = "";

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
   // var max = 0;
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
var min = parseFloat(minimumValue(data, measureArray[i]));
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
y.domain([parseFloat(minVal), parseFloat(max)]).clamp(true);
//    svg = d3.select("#" + div).select("g");
    svg = d3.select("#"+div).select("g");
      if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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

//if(typeof displayX !=="undefined" && displayX == "Yes"){
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
                if (d.length < 14)
                    return d;
                else
                    return d.substring(0, 13) + "..";

            })
            .attr('transform', 'rotate(-25)')
            .append("svg:title").text(function(d) {
        return d;
    });
//}
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
for(var k in measureArray){
var valueline = d3.svg.area()
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y(d[measureArray[k]]);
            });

var path =  svg.append("path")
            .data(data)
            .attr("d", valueline(data))
            .attr("fill", "transparent")
            .style("z-index", "0")
             .style("stroke", function(d) {
                var colorShad;
    colorShad = getDrawColor(div, parseInt(k)); 
                return colorShad;
            }) .style("stroke-width", "3px");

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
                return y(d[measureArray[k]]);
            })
             .attr("id", function(d) {
                return d[columns[0]] + ":" + columnId[0];
            })
            .attr("name", function(d,i){
                return d[columns[0]] + ":" + d[measureArray[k]] + "#"+measureArray[k]
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
            colorShad = getDrawColor(div, parseInt(k));
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
        colorShad = getDrawColor(div, parseInt(k));
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
    colorShad = getDrawColor(div, parseInt(k));
                return colorShad;
            })
            .style("stroke-width", "2px")
            .attr("onclick", fun)
//	.dblTap(function(e,id) {
//		drillFunct(id);
//	}) 
            .on("mouseover", function(d, i) {
                var msrData;
var idArr = $(this).attr("name");
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
  try{
  var totalLength = path.node().getTotalLength();

    path
      .attr("stroke-dasharray", totalLength + " " + totalLength)
      .attr("stroke-dashoffset", totalLength)
      .transition()
        .duration(2000)
        .ease("linear")
        .attr("stroke-dashoffset", 0);
           // Label Text
           svg.selectAll("labelText")
             .data(data).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("x", function(d){
                       return x(d[columns[0]]) - 15;
                   } ).attr("y", function(d){
                       return y(d[measureArray[k]]) + 10;
                   })
          
      .attr("dy", ".35em")
            .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                       return numberFormat(d[measureArray[k]],yAxisFormat,yAxisRounding,div);
                }else{
                    var percentage = (d[measureArray[k]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }   else {return "";}
            });
}catch(e){}

   
}
svg.append("foreignObject")
        .attr("width", width)
        .attr("height", height)
        .style("display","none")
        .attr("x", function(d) {
                return x(data[columns[0]]);
            })
            .attr("y", function(d) {
               
                return "0";
            }) .attr("id", function(d) {
                return "foreignObj"+div;
            })
        .append("xhtml:body")
        .attr('xmlns','http://www.w3.org/1999/xhtml')
        .html("<div id='callQuickDiv"+div+"' width=20 height=20></div>");
        
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
}
 function buildTreeMapSingle(div, data0, columns, measures, divWidth, divHeight,KPIResult) {
     var color = d3.scale.category10();
    var chartData = JSON.parse(parent.$("#chartData").val());
//    data0=JSON.parse('[{"Category":"Computer Hardware","Gross Sales":"100.0"},{"Category":"Cameras","Gross Sales":"200.0"},{"Category":"Mobile Phones","Gross Sales":"400.0"},{"Category":"Footwear","Gross Sales":"-200.0"},{"Category":"Clothing","Gross Sales":"-300.0"},{"Category":"Watches","Gross Sales":"500.0"}]');
    var tempChartData=[];
    for(var i in data0){
        var dataMap={};
        var keys1=Object.keys(data0[i]);
        for(var k in keys1){
            dataMap[keys1[k]]=data0[i][keys1[k]];
        }
        tempChartData.push(dataMap);
    }
var absoluteValue = chartData[div]["absoluteValue"];
    if(typeof absoluteValue !=="undefined" && absoluteValue==="Yes"){
  data0 = getAbsoluteValue(data0,columns,measures);
    }
//     divWidth=parseFloat($(window).width())*(.35);

      var fromoneview=parent.$("#fromoneview").val()
        var widthdr=divWidth
    var divHgtdr=divHeight
       if(fromoneview!='null'&& fromoneview=='true'){
divWidth=divWidth;
//divHeight=divHeight+50;
      }
      var length = measures.length;
var data = [];
if(typeof chartData[div]["dataTranspose"]!="undefined" && chartData[div]["dataTranspose"]!="" && chartData[div]["dataTranspose"]=="Yes"){
for (var k=0;k<length;k++){
    var dataMap = {};
   dataMap[columns[0]]=measures[k];
   dataMap[measures[0]] = KPIResult[k];
data.push(dataMap);
}
data0 = data
}
    var nodes = [];
 //   var data = [];
    var dashletid;
    var measure1 = measures[0];

var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
colIds=chartData[div1]["viewIds"];
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
}
    var chartMap = {};
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
    }
    var isDashboard = parent.$("#isDashboard").val();
    var chartType = parent.$("#chartType").val();
    var nodes = [];
data0.forEach(function(d, i) {
if (i < data0.length && d[measures[0]] >= 0) {
var node = {};
node["name"] = d[columns[0]];

if (columns.length > 1) {
node["name1"] = d[columns[1]];
} else {
node["name1"] = d[columns[0]];
}
for (var no = 0; no < measures.length; no++) {
if (no === 0) {
node["value"] = d[measures[no]];
} else {
node["value" + no] = d[measures[no]];
}
}
return nodes.push(node);
}
});
data = nodes;

    if (chartType === "dashboard") {
        isDashboard = true;
    }
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//    }
    if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        if (div === "chart1") {
            var tempcolumn = columns;
            columns = [];
            columns.push(tempcolumn[0]);
        }
        var tempmeasure = measure;
        measures = [];
        measures.push(tempmeasure[0]);
    }
    var barsWidthTotal = $(window).width() - 400;
    var legendOffset = 60;
    var legendBulletOffset = 130;
    var legendTextOffset = 20;
    var lableColor='';
    var bubble_layout = d3.layout.treemap()
            .size([divWidth, divHeight-20]) //-100
    svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWidth)+" "+(divHeight )+" ")
            .classed("svg-content-responsive", true)
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var selection = svg.selectAll("g.node")
            .data(bubble_layout.nodes({
                children: data
            }).filter(function(d) {
                return !d.children;
            }));
            var offset=0;
            if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
                offset=10;
            }
    var node = selection.enter().append("g")
            .attr("class", "node")
            .attr("transform", function(d,i){
                var top = d.y-20;
        return  "translate(" + (d.x) + ", " + (top+offset+10) +  ")";        
            })
            .filter(function(d) {
        return d.value;
    })
    ;
    node.append("rect")
            .attr("width", function(d) {
                return Math.max(0, d.dx ) + "px";
            })
            .style("stroke-width","0px")
            .attr("height", function(d) {
                return Math.max(0, d.dy ) + "px";
            })
             .style("fill", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
            })
            .style("fill-opacity","1")
             .attr("color_value", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })

            .attr("id", function(d) {
                return d.name + ":" + d.value;
            })

             .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                prevColor = getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)

//                }
//                if (toolTipType === "customize") {
//                    for (var num in toolTipData) {
//                        if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                            break;
//                        }
//                    }
//                } else {
                     if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d, columns, measures, this,chartData,div1);
                     }else{
                   var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                        var selectedBar = d3.selectAll(barSelector);
                        selectedBar.style("fill", drillShade);
                    show_details(d, columns, measures, this,div);
                     }
//                    } catch (e) {
//                    }
//                }
//                d3.select(this).attr("stroke", "grey");
                var content = "";
                for (var no = 0; no < measures.length; no++) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.value, round, precition);
//                        alert("a");
                    }
                    else {
                        if (no === 0) {
//                            msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(d.value);
                          msrData = addCurrencyType(div, getMeasureId(measures[no]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
                        } else {
//                            msrData = addCommas(d["value" + no]);
                           msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(numberFormat(d["value" + no],yAxisFormat,yAxisRounding,div));//Added by shivam
                        }

                    }
                     if(fromoneview!='null'&& fromoneview=='true'){


 if (no === 0) {
                            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + measures[no] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                     }else{
                    if (typeof columnMap[columns[0]] !== "undefined" && columnMap[column[0]]["displayName"] !== "undefined" && typeof columnMap[measure1] !== "undefined" && columnMap[measure1]["displayName"]) {
                        if (no === 0) {
                            content += "<span class=\"name\">" + columnMap[columns[0]]["displayName"] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columnMap[columns[1]]["displayName"] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span class=\"name\">" + columnMap[measures[no]]["displayName"] + "</span><span class=\"value\"> " + (msrData) + "</span><br/>";
                    } else {
                        if (no === 0) {
//                            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                            if (columns.length > 1) {
                                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
                            }
                        }
                        content += "<span style=\"font-family:helvetica;\" class=\"name\">" + (msrData) + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measures[no] + "</span><b>:</b><span style=\"font-family:helvetica;\" class=\"value\">" + d.name + "</span></br>";//Added by mayank sh.
                    }
                }
                }
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
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
            }).attr("onclick", fun);
var sum = d3.sum(data, function(d) {
        return d.value;
    });
    var max = maximumValue(data0, measures[0]);
    var min = minimumValue(data0, measures[0]);
    var temp = {};
    temp["min"] = min;
    temp["max"] = max;
        addLabels=function(txt){
            node.append("text")
            .attr("width", function(d,i){
             var wid=Math.max(0, d.dx);   
             return (wid)+"px";
            })
            .attr("height", function(d,i){
             var hgt=Math.max(0, d.dy);   
             return (hgt)+"px";
            })
            .attr("x", function(d,i){
             var wid=Math.max(0, d.dx);   
             return (wid/2)+"px"
            })
            .attr("y", function(d,i){
             var offset=0;
             if(txt==='abs'){
                 offset=0;
             }
             else if(txt==='cont'){
                 if(Math.max(0, d.dy)<60){
                     offset=-10;  //add by mayank sharma
                 } else{
                    offset=-16;
                 }
             }
             else{
                 if(Math.max(0, d.dy)<60){
                     offset=8;
                 }
                 else{
                    offset=16;
                 }
             }
             var hgt=Math.max(0, d.dy);   
             return (hgt/2+offset)+"px"
            })
            .attr("class", "valueLabel")
            .style("font-size",function(d, i){
             var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["13", "8"]);
             var fontSize = scale(data0[i][measures[0]]);
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return fontSize+"px";
                    }
            })
            .style("font-weight",function(d,i){
                if(txt==='abs'){
                    return "bold";
                }
                else if(txt==='cont'){
                    return "";
                }
                else{
                    return "";
                }
            })
            .style("text-anchor","middle")
            .text(function(d,i){
                var absValue=tempChartData[i][measures[0]];
                 var wid1=Math.max(0, d.dx);     //add by mayank sharma
             var hgt1=Math.max(0, d.dy);
              var percentage = (d.value / parseFloat(sum)) * 100;
                if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    if(txt==='cont'){
                       if(wid1 < 45 || hgt1 <= 35 || percentage<2){ 
                            return "";
                        }else{
                        percentage = percentage.toFixed(1) + "%";
                        return percentage;
                    }}
                    else if(txt==='abs'){
                     if( wid1 < 45 || hgt1 <= 40 || percentage<2.5){ 
                            return "";
                        }else{
                        return addCurrencyType(div, chartData[div]["meassureIds"]) + numberFormat(absValue,yAxisFormat,yAxisRounding,div);
                    }}
                    else{
                    if(wid1 <= 40 || hgt1 <= 30 || percentage<2){   
                        return "";
                       }else if( wid1 <= 100 || hgt1 <= 40 || percentage<4 || d.name.length>20){
                        return  d.name.substring(0, d.dy / 20);
                       }else{
                        return d.name;
                        }
                    }
                }else{
                    return ""; 
                }
            })
                        .attr("fill",  function(d, i){
               var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            });
        }
        addLabels('abs');
        addLabels('cont');
        addLabels('name');  // update by mayank sharma
                }
   
function buildScatter(div, data, columns, measureArray, width, height) {
//      added by shivam     
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
    var chartMap = {};
    var noOfMeasure = measureArray.length;
    if (measureArray.length === 1) {
        measureArray.push(measureArray[0]);
    }
    var measure1 = measureArray[0];
    var measure2;
    if (measureArray.length === 1) {
        measure2 = measureArray[0];
    }else{
        measure2 = measureArray[1];
    }
    var autoRounding1;
    var autoRounding2;
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    }else {
        autoRounding1 = "1d";
    }
    if (columnMap[measure2] !== undefined && columnMap[measure2] !== "undefined" && columnMap[measure2]["rounding"] !== "undefined") {
        autoRounding2 = columnMap[measure2]["rounding"];
    }else {
        autoRounding2 = "1d";
    }
    var isDashboard = parent.$("#isDashboard").val();
//    var chartType = parent.$("#chartType").val();
//    var chartTypeList = JSON.parse(parent.$("#chartList").val());

//    var chartType = chartTypeList[0];
    if (chartType === "dashboard") {
        isDashboard = true;
    }
//     var fun = "drillWithinchart(this.id,\""+div+"\")";


    //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
	}else{
            fun = "drillWithinchart(this.id,\""+div+"\")";
    var botom = 70;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
        botom = 70;
    }
    }
	function drillFunct(id1){
    var botom = 70;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        drillChartInDashBoard(id1,div);
        botom = 70;
    }else {
        drillWithinchart(id1,div);
        }    
	}


    var margin = {
        t: 20,
        r: 00,
        b: botom,
        l: 80
    },
            w = parseFloat(width)-70, //- margin.left - margin.right
            h = parseFloat(height)* .65;
        var x = d3.scale.ordinal().rangePoints([0, w], .2);
        var max = maximumValue(data, measure1);
        var minVal = minimumValue(data, measure1);
     var y = d3.scale.linear().domain([minVal, max]).range([h, 0]);
//   w = parseFloat(divWid)-70
////    w = width, //- margin.left - margin.right
////            h = height;
//    x = d3.scale.linear().range([30, w - 90]),
//            y = d3.scale.linear().range([h - 60, 30]),
            r = 20;
    var svg = d3.select("#" + div).append("svg")
            .attr("width", w + margin.l + margin.r)
            .attr("height", h + margin.t + margin.b);

    var gradient = svg.append("svg:defs").selectAll("radialGradient").data(data).enter()
            .append("svg:radialGradient")
            .attr("id", function(d) {
                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("fx", "2%")
            .attr("fy", "2%")
            .attr("r", "50%")
            .attr("spreadMethod", "pad");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
                var drilledvalue = parent.$("#drilledValue").val();
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                    colorShad = drillShade;
                } else {
                    if (chartType === "Scatter") {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(0);
                        }
                    }
                    if (chartType === "Multi-View-Scatter" || chartType === "Bubble-Scatter") {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(i);
                        }
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(0);
                        }
                    }

                }
                if (typeof chartMap[d[columns[0]]] === "undefined") {
                    chartMap[d[columns[0]]] = colorShad;
                    colorMap[i] = d[columns[0]] + "__" + colorShad;
                }

                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", wid)
//            .attr("height", hgt)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var xAxis;
    if (isFormatedMeasure) {
        xAxis = d3.svg.axis()
                .scale(x)
                .ticks(10)
                .tickSubdivide(true)
                .tickSize(4, 0, 0)
                .orient("bottom")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition);
                     return d;
                });
    }
    else {
        xAxis = d3.svg.axis()
                .scale(x)
                .ticks(10)
                .tickSubdivide(true)
                .tickSize(4, 0, 0)
                .orient("bottom")
                .tickFormat(function(d) {
//                    return autoFormating(d, autoRounding1);
                return d;
                });
    }

    var yAxis;
    if (isFormatedMeasure) {
        yAxis = d3.svg.axis()
                .scale(y)
                .ticks(5)
                .tickSubdivide(true)
                .tickSize(6, 0, 0)
                .orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition);
                     return d;
                });
    } else {
        yAxis = d3.svg.axis()
                .scale(y)
                .ticks(5)
                .tickSubdivide(true)
                .tickSize(6, 0, 0)
                .orient("left")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding2);
                      return d;
                });
    }
    var isNegative = false;
    var max = maximumValue(data, measure1);
    var max1 = maximumValue(data, measure2);
    var min = minimumValue(data, measure1);
    if (min === max) {
        min = parseFloat(max) - parseFloat(max) / 2;
        max = parseFloat(max) + parseFloat(max) / 2;
    } else {
        min = min * .8;
    }

    var min1 = minimumValue(data, measure2);
    if (min1 === max1) {
        min1 = parseFloat(max1) - parseFloat(max1) / 2;
        max1 = parseFloat(max1) + parseFloat(max1) / 2;
    } else {
        min1 = min1 * .8;       //to shift axis from minimum value
    }
    var radiusmax;
    if (measureArray.length >= 3) {
        radiusmax = maximumValue(data, measureArray[2]);
    }
    else
        radiusmax = max1;
    if (radiusmax <= 0) {
        isNegative = true;
    }
    if (isNegative && measureArray.length >= 3) {
        radiusmax = maximumValueNegative(data, measureArray[2]);
    }
    else if (isNegative) {
        radiusmax = maximumValueNegative(data, measureArray[1]);
    }
    var maxRadius = 40, padding = 10;
    var radius_scale = d3.scale.sqrt()
            .domain([0, radiusmax])
            .range([0, maxRadius]);

    if (chartType === "Scatter") {
        r = 5;
    }

    x.domain([min, max]);
    y.domain([min1, max1]);

    // style the circles, set their locations based on data
    var elem = svg.selectAll("g myCircleText")
            .data(data);
    var elemEnter = elem.enter()
            .append("g")

            .attr("transform", function(d) {
                return "translate(" + x + ",70)";
            })



    /*Create the circle for each block */
    var circles = elemEnter.append("circle")
            .attr("id", function(d) {
                return d[columns[0]];
            })
            .attr("class", function(d) {//index-"+ageNames1[k].replace(" ","","gi").replace(/[^\w\s]/gi, '')+"
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
                var drilledvalue = parent.$("#drilledValue").val();
                if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                    return drillShade;
                }
//    else
//        if (chartType === "Scatter") {
//            return color(0);
//        }
//        if (chartType === "Multi-View-Scatter" || chartType === "Bubble-Scatter") {
//            return color(d[columns[0]]);
//        }
                else
                    return "url(#gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr({
                cx: function(d) {
                    return x(+d[measureArray[0]]) + 40;
                },
                cy: function(d) {
                    return y(+d[measureArray[1]]) + 25;
                },
                r: function(d) {
                    if (chartType === "Multi-View-Scatter" || chartType === "Bubble-Scatter") {
                        if (measureArray.length >= 3) {
                            if (!isNegative && parseFloat(d[measureArray[2]]) <= 0) {
                                return 0;
                            }
                            else {
                                return radius_scale(parseFloat(d[measureArray[2]]));
                            }
                        }
                        else {
                            if (!isNegative && parseFloat(d[measureArray[1]]) <= 0) {
                                return 0;
                            }
                            else {
                                return radius_scale(parseFloat(d[measureArray[1]]));
                            }
                        }
                    } else {
                        return r;
                    }
                }



            })
            .style("fill", function(d) {
                return "url(#gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })

            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}); 
    
    /* Create the text for each block */
    if (chartType === "Bubble-Scatter" || chartType === "Multi-View-Scatter" || typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        elemEnter.append("text")
                .attr({
                    dx: function(d) {
                        return x(+d[measureArray[0]]) + 25;
                    },
                    dy: function(d) {
                        return y(+d[measureArray[1]]) + 25;
                    }
                })
                .text(function(d) {
                    if (chartType === "Multi-View-Scatter") {
                        if (measureArray.length >= 3) {
                            if (!isNegative && parseFloat(d[measureArray[2]]) <= 0) {
                                return "";
                            }
                            else
                                return d[columns[1]].substring(0, radius_scale(parseFloat(d[measureArray[2]])) / 7);
                        } else {
                            if (!isNegative && parseFloat(d[measureArray[1]]) <= 0) {
                                return "";
                            }
                            else
                                return d[columns[1]].substring(0, radius_scale(parseFloat(d[measureArray[1]])) / 7);
                        }
                    }
                    else {
                        if (measureArray.length >= 3 && !isNegative && parseFloat(d[measureArray[2]]) <= 0 && chartType === "Bubble-Scatter") {
                            return "";
                        }
                        else if (!isNegative && parseFloat(d[measureArray[2]]) <= 0 && chartType === "Bubble-Scatter") {
                            return "";
                        }
                        else {
                            return d[columns[0]];
                        }
                    }
                })
                .attr("class", function(d, i) {
                    return "bars-Bubble-legendText-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .style("fill", "Black")
                .attr("style", "font-family: lucida grande")
                .attr("style", "font-size: 10px")
                .attr("text-anchor", "start");
    }
    // .style("text-anchor", "middle")
    // run the mouseon/out functions
    circles.on("mouseover", function(d, i) {
        var circle = d3.select(this);
        // var circle1=d3.selectAll("."+d[columns[0]].replace(" ","","gi"));
        circle.transition()
                .duration(500).style("opacity", 10);
        //elemEnter

        d3.select(this).style("stroke-width", 3);
        d3.select(this).style("stroke", "grey");
        // d3.selectAll("g." + d[columns[0]] + " text")
        d3.select(this).style("stroke-width", 3);
        d3.select(this).style("stroke", "grey");
        d3.select(this).attr("r", 35);
        //        d3.selectAll("g." + d[columns[0]] + " text")
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).style("stroke-width", 3);
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).style("stroke", "grey");
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).attr("r", 35);
        if (noOfMeasure === 1) {
            var temp = [];
            temp.push(measureArray[0]);
            show_details(d, columns, temp, this);
        } else {
            show_details(d, columns, measureArray, this);
        }
    });

    circles.on("mouseout", function(d, i) {
        var circle = d3.select(this);
        circle.transition();
        d3.select(this).style("background-color", "white");
        d3.select(this).style("stroke-width", 1);
        d3.select(this).style("stroke", "gray");
        // d3.selectAll("g." + d[columns[0]] + " text")
        d3.select(this).style("stroke-width", 1);
        d3.select(this).style("stroke", "gray");
        if (chartType === "Multi-View-Scatter" || chartType === "Bubble-Scatter") {
            if (measureArray.length >= 3)
                d3.select(this).attr("r", radius_scale(parseFloat(d[measureArray[2]])));
            else
                d3.select(this).attr("r", radius_scale(parseFloat(d[measureArray[1]])));
        }
        else {
            d3.select(this).attr("r", r);
        }
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).style("stroke-width", 1);
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).style("stroke", "gray");
        //        d3.selectAll("."+d[columns[0]].replace(" ","","gi")).attr("r", r);
        hide_details(d, i, this);
    });


//    $(".circles").tipsy({
//        gravity: 's'
//    });


    // draw axes and axis labels
			 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
    svg.append("g")
            .attr("class", "x.axis")
            .attr("transform", "translate(40," + (h - 60 + margin.t) + ")")
            .transition().duration(300)
//            .attr("text").style("transform","rotate(25deg)")
            .ease("quad")
            .call(xAxis)
         }    
           svg.append("text")
            .attr("class", "x label")
            .attr("text-anchor", "end")
            .attr("x", w)
            .attr("y", h )
            .text("" + measureArray[0] + "");

if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    svg.append("g")
            .attr("class", "y axis")
            .attr("transform", "translate(" + margin.l + "," + margin.t + ")")
            .transition().duration(1000)
            .call(yAxis);
}


    svg.append("text")
            .attr("class", "y label")
            .attr("text-anchor", "end")
            .attr("x", -20)
            .attr("y", 25)
            .attr("dy", ".75em")
            .attr("transform", "rotate(-90)")
            .text("" + measureArray[1] + "");
            d3.select(this).style("margin-", 3);
    var legends = [];
    data.forEach(function(d) {
        if (legends.indexOf(d[columns[0]]) === -1)
            legends.push(d[columns[0]]);
    });
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
    }
    else if (chartType === "Bubble-Scatter") {
        showLegends(legends, color, width, height);
    }
    if (chartType === "Multi-View-Scatter") {//  || chartType==="Bubble-Scatter"
//        showLegends(legends, color, width, height);
        //        svg.selectAll(".circle")
        //        .data(legends).enter().append("svg:circle") // Append circle elements
        //        .attr("cx", width-20)
        //        .attr("cy", function(d, i) {
        //            return (60 + i*20 + 5);
        //        } )
        //        .attr("stroke-width", ".5")
        //        .style("fill", function(d, i) {
        //            return color(d)
        //        }) // Bar fill color
        //        .attr("index_value", function(d, i) {
        //            return "index-" + d.replace(" ","","gi");
        //        })
        //
        //        .attr("class", function(d, i) {
        //            return "bars-Bubble-legendBullet-index-" + d.replace(" ","","gi");
        //        })
        //        .attr("r", 5)
        //        .attr("color_value", function(d, i) {
        //            return color(d);
        //        }) // Bar fill color...
        //        //        .on('mouseover', synchronizedMouseOver)
        //        //        .on("mouseout", synchronizedMouseOut);
        //        svg.selectAll("g.legend")
        //        .data(legends) // Instruct to bind dataSet to text elements
        //        .enter().append("g") // Append legend elements
        //        //.attr("xlink:href", function(d) { return d.link; })
        //        .append("text")
        //        .attr("text-anchor", "left")
        //        .attr("x", width-10)
        //        .attr("y", function(d, i) {
        //            return 60 + i*20 +7;
        //        } )
        //        //.attr("dx", 0)
        //        //.attr("dy", "1em") // Controls padding to place text above bars
        //        .text(function(d) {
        //            return d
        //        })
        //        .style("fill",function(d, i) {
        //            return color(d)
        //        })
        //        .attr("id", function(d, i) {
        //            return d;
        //        })
        //        .attr("color", function(d, i) {
        //            return d;
        //        })
        //        .attr("index_value", function(d, i) {
        //            return "index-" + d.replace(" ","","gi");
        //        })
        //        .attr("class", function(d, i) {
        //            return "bars-Bubble-legendText-index-" + d.replace(" ","","gi");
        //        })
        ////            .on('mouseover', synchronizedMouseOver)
        ////            .on("mouseout", synchronizedMouseOut)
    }

}

function buildBarAndPieComb(data, columns, measureArray, divId) {
    var formatAsPercentage = d3.format("%"),
            formatAsPercentage1Dec = d3.format(".1%"),
            formatAsInteger = d3.format(",");

    /*
     ############# PIE CHART ###################
     -------------------------------------------
     */
    var formatedMeasure = false;
    var margin = {
        top: 0,
        right: 0,
        bottom: 0,
        left: 0
    },
    width = wid - margin.left - margin.right - 650,
            height = hgt - margin.top - margin.bottom - 200;

    radius = Math.min(width, height) / 2;
    var div = d3.select("#" + divId);
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var datasetBarChart = data;
    var pie;
    if (measureArray.lenght > 1) {
        pie = d3.layout.pie() //this will create arc data for us given a list of values
                .value(function(d) {
                    return d[measureArray[1]];
                });
    } else {
        var pie = d3.layout.pie() //this will create arc data for us given a list of values
                .value(function(d) {
                    return d[measureArray[0]];
                });
    }
    var svg;
    var arcs;

    d3.select("body").append("div").attr("id", "pieChart");
    build();
    function build() {
        svg = d3.select("#pieChart").append("svg")
                .datum(data)
                .attr("width", width)
                .attr("height", height + 20)
                .append("g")
                .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
        arcs = svg.selectAll("g.arc")
                .data(pie)
                .enter().append("g")
                .attr("class", "arc");
        arcs.append("path")
                .attr("id", function(d, i) {
                    return i;
                })
                .attr("fill", "Lightgrey")
                .transition()
                .ease("bounce")
                .duration(2000)
                .attrTween("d", tweenPie)
                .transition()
                .ease("elastic")
                .delay(function(d, i) {
                    return 2000 + i * 50;
                });

        arcs.on("mouseover", function(d, i) {

            d3.select(this).select("path").attr("id", i).attr("fill", "#00AA44");
            d3.select("body").selectAll("rect").attr("fill", function(d, j) {
                if (i === j) {
                    return "#00AA44";
                } else {
                    return "Darkblue";
                }
            });
            var content;
            var msrData;
            if (isFormatedMeasure) {
                msrData = numberFormat(d.data[measureArray[0]], round, precition,div);
            }
            else {
                msrData = addCommas(d.data[measureArray[0]]);
            }
            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.data[columns[0]] + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            if (measureArray.lenght > 1) {
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.data[measureArray[1]], round, precition,div);
                }
                else {
                    msrData = addCommas(d.data[measureArray[1]]);
                }
                content += "<span class=\"name\">" + measureArray[1] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            }
            return tooltip.showTooltip(content, d3.event);
        })
                .on("mouseout", function(d, i) {
                    d3.select(this).select("path").attr("id", i).attr("fill", "Lightgrey");
                    d3.select("body").selectAll("rect").attr("fill", "steelblue");

                });

        var outerRadius = Math.min(width, height) / 2,
                innerRadius = outerRadius * .999,
                // for animation
                innerRadiusFinal = outerRadius * .5;
        var arcFinal = d3.svg.arc().innerRadius(innerRadiusFinal).outerRadius(outerRadius);
        arcs.filter(function(d) {
            return d.endAngle - d.startAngle > .2;
        })
                .append("svg:text")
                .attr("dy", ".55em")
                .attr("text-anchor", "middle")
                .attr("transform", function(d) {
                    return "translate(" + arcFinal.centroid(d) + ")rotate(" + angle(d) + ")";
                })
                //.text(function(d) { return formatAsPercentage(d.value); })
                .text(function(d) {
                    if (d.data[columns[0]].length < 8)
                        return d.data[columns[0]];
                    else
                        return  d.data[columns[0]].substring(0, 7) + "..";
                })
                .attr("style", "font-size: 8pt; font-family: Calibri, sans-serif")
                .append("title");

    }

    function angle(d) {
        var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
        return a > 90 ? a - 180 : a;
    }

    function angle1(d) {
        var a = (d.startAngle + d.endAngle) * 45 / Math.PI - 45;
        return a > 45 ? a - 90 : a;
    }

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


    var x;
    var y;
    var xAxis;
    var yAxis;

    var margin = {
        top: 20,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - margin.left - margin.right - 450,
            height = hgt - 70 - margin.top - margin.bottom;
    var barPadding = 4;
    var formatPercent = d3.format(".0%");

    x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);

    y = d3.scale.linear()
            .range([height, 0]);

    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
    if (isFormatedMeasure) {
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition);
                });
    } else {
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    return autoFormating(d);
                });
        //    .tickFormat(formatPercent);
    }


    svg = d3.select("#barChart").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var max = maximumValue(data, measureArray[0]);
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    y.domain([0, max]);
    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height + 3) + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
                if (d.length < 13)
                    return d;
                else
                    return d.substring(0, 10) + "..";
            })
            .attr('transform', 'rotate(-25)')
            .append("svg:title").text(function(d) {
        return d;
    });

    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", 6)
            .attr("dy", ".71em")
            .style("text-anchor", "end");

    svg.selectAll(".bar")
            .data(data)
            .enter().append("rect")
            .attr("class", "bar")
            .attr("rx", barRadius)
            .attr("fill", "steelblue")
            .attr("x", function(d) {
                return x(d[columns[0]]);
            })
            .attr("width", x.rangeBand())
            .attr("y", function(d) {
                return y(d[measureArray[0]]);
            })
            .attr("height", function(d) {
                return height - y(d[measureArray[0]]);
            })
            .on("mouseover", function(d, i) {
                d3.select("#pieChart").selectAll("path").attr("fill", function(d, j) {
                    if (i === j) {
                        return "#00AA44";
                    } else {
                        return "Lightgrey";
                    }
                });
                d3.select("#barChart").selectAll("rect").attr("fill", function(d, j) {
                    if (i === j) {
                        return "#00AA44";
                    } else {
                        return "Darkblue";
                    }
                });
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                d3.select("#pieChart").selectAll("path").attr("fill", "Lightgrey");
                d3.select("#barChart").selectAll("rect").attr("fill", "Darkblue");
                hide_details(d, i, this);
            });
    svg.selectAll(".text")
            .data(data)
            .enter()
            .append("text")
            .attr("class", "text")  //  MISSING FROM CODE IN PAGE DISPLAY
            .attr("x", function(d, i) {
                return i * (width / data.length) + (width / data.length - barPadding) / 2;
            })
            .attr("y", function(d) {

                return  y(d[measureArray[0]]) + 12;  //15 is now 14

            });


}

function buildColumnDonut(div,divId, data, columns, measureArray, divWidth, divHeight, rad,KpiResult) {
    var color = d3.scale.category10();
     if(divId == "GraphDonut"){
        divId = div;
    }else{
        divId = divId;
    }
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var length = measureArray.length;
data = [];
for (var k=0;k<length;k++){
    var dataMap = {};
   dataMap[columns[0]]=measureArray[k];
   dataMap[measureArray[0]] = KpiResult[k];
data.push(dataMap);
}
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
       //      added by manik      
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
    rad=(Math.min((divWidth*.7), divHeight))*.50;
        var chartData = JSON.parse(parent.$("#chartData").val());
        var colIds= [];
        var width1 = divWidth;
    var widthdr=divWidth
    var divHgtdr=divHeight
    var dashletid;
    var fromoneview= parent.$("#fromoneview").val();
//        var chartData = JSON.parse(parent.$("#chartData").val());
var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     colIds=chartData[div]["viewIds"];
     dashletid=div;
divWidth=divWidth;
 var legendAlign;
if(typeof chartData[div1]["legendLocation"]==='undefined' || chartData[div1]["legendLocation"]==='Right')
{
    legendAlign='Right';
}
else
{
    legendAlign='Bottom';
}
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
        var legendAlign;
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Right')
{
    legendAlign='Right';
}else if(chartData[div]["legendLocation"]==='Outer' ){
    legendAlign='Outer';
    
}else
{
    legendAlign='Bottom';
}
}

    
var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
//	alert("hastuch1 : "+hasTouch)
	
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
        
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    
    
    
    
    
    
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
    var width = divWidth;
    var margintop;
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var margintop;
var radius;
     if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "0px";

        radius = rad;
    }
    else {
        height = divHeight;
        margintop = "-60px";
        radius = rad / 1.2;
    }
    var wDiv=3,hDiv=2.4;
    if(legendAlign==='Bottom')
    {
        wDiv=2;
        hDiv=2.9;
        if(width>height)
        {
            radius*=0.8;
        }
    }
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]=='Outer'){
        radius *=0.8
    }
    
    //Math.min(width, height) / 3 ;
    var arcFinal = d3.svg.arc()
    .innerRadius(radius-40)
    .outerRadius(radius);
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
       var topMargin;
            if(legendAlign=='Right')
                {
                    topMargin=-15;
                }else if(legendAlign=='Outer'){
                    topMargin=-15;
                }else{
                    topMargin=-15;
                }   
            
    var lableColor;
    var svg = d3.select("#" + divId)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ") //edit by shivam
            .classed("svg-content-responsive", true)
            .datum(data)
            .attr("style", margintop);
            
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
//        var drilledvalue = parent.$("#drilledValue").val().split(",");
                var drilledvalue = "";
                var colorShad;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
                } catch (e) {
                }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                }
            else {
                    colorShad = getDrawColor(div, parseInt(i));
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("class", "arc")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
           .attr("transform",function(d){
              if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" &&  legendAlign!=='Outer')||legendAlign==='Bottom'){
            return "translate(" + width / wDiv + "," + height / hDiv + ")";
             }else if(legendAlign==='Outer'){
            return "translate(" + width *.50 + "," + height / hDiv + ")";
             }else{
                 if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                    return "translate(" + width / wDiv + "," + height / hDiv + ")";
                }else{  
              return "translate(" + width / 3.5 + "," + height / hDiv + ")";
             }
             }
           });
            
    arcs.append("path")
            .attr("fill", function(d,i) {

             var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                    colorShad = drillShade;
                }
                else{
				var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
                 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
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
      var percentage = (d.value / parseFloat(sum)) * 100;
var content = "";
        var msrData;
  msrData = addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));
//               content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + "</span><br/>";//Added by shivam
                    
             if(typeof (chartData[div]["tooltipType"])!=="undefined"){
             if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="With%"){
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> "+percentage.toFixed(1) + "%</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default-With%"){
                 content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "("+percentage.toFixed(1) + "%)</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i]  + " </span><br/>";
            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default"){
                  content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
            }
            }else{
              content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
            }
                    
                        return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
if(fromoneview!='null'&& fromoneview=='true'){}else{
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
            .attrTween("d", tweenDonut);
            
    function angle(d) {
        var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
        return a;
    }
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
    
 if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){
  arcs.append("text")
            .attr("dy", ".2em")
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
            if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                return (chartData[div]["labelFSize"]+"px");
            }else{
                return parseInt(width/45)+"px";
            }
           })
           .style("fill",  "#696969")
            .attr("transform", function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    d.outerRadius = radius / 5; // Set Outer Coordinate
                    d.innerRadius = radius / 1.2;
                }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+80;
                }
            }else {
                d.outerRadius = radius+20; // Set Outer Coordinate
                d.innerRadius = radius+20;
            }
            return "translate(" + arc.centroid(d) + ")";
           })
            .text(function(d,i) {
           if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                if(data[i][columns[0]].toString().length>4){
                    return data[i][columns[0]].substring(0, 4)+"..";
                }else{
                    return data[i][columns[0]];
                }}else{ 
                if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes")  {
    var percentage = (d.value / parseFloat(sum)) * 100;
   if(percentage<=8){
                return ""; 
   }else{
if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                return percentage.toFixed(1) + "%";
                }else {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
            } }else {
                    return "";
                }
            }
        });
            arcs.append("text")
            .attr("dy", "1.4em")
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
            if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                return (chartData[div]["labelFSize"]+"px");
            }else{
                return parseInt(width/45)+"px";
            }
           })
           .style("fill",  "#696969")
            .attr("transform", function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    d.outerRadius = radius / 5; // Set Outer Coordinate
                    d.innerRadius = radius / 1.2;
                }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+80;
                }
            }else{
                d.outerRadius = radius+20; // Set Outer Coordinate
                d.innerRadius = radius+20;
            }
            return "translate(" + arc.centroid(d) + ")";
           })
            .text(function(d,i) {
            var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage<=8){
              return "";
          } else{
            if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
              if(data[i][columns[0]].toString().length>4){
                return data[i][columns[0]].substring(0, 4)+"..";
             }else{
                return data[i][columns[0]];
            }}else{
              if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes")  {
               if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else {
                return data[i][columns[0]];
              }}
             }}
           });
}   
           arcs.append("text")
            .attr("dy", ".35em")
            .attr("text-anchor", "start")
            .attr("class","gFontFamily")
            .attr("style", "font-size: 12px")
            .attr("transform", function(d) {
                return "translate(" + arcFinal.centroid(d) + ")rotate(" + angle(d) + ")";
            });
        var textAnchor="end";
        if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
            textAnchor="middle";
        }
        var text;
        text=arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", textAnchor)
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
            })
            .style("fill", function(d,i){
               var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return "#000000";
                    }
                var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#d8d47d";
                               }
                               return lableColor;
            })
            .attr("transform", function(d) { //set the label's origin to the center of the arc
                d.outerRadius = radius; // Set Outer Coordinate
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    d.innerRadius = radius / 1.2;
                    }else{
                    d.innerRadius = radius / 2.3; 
                    }
                } else {
                    d.innerRadius = radius / 1.3; 
                }
                var invertAngle=0;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    if(a>90){
                        invertAngle=180;
                    }
                }
                var rotateAngle=0;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    rotateAngle=90;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + (a+rotateAngle+invertAngle) + ")";
            })
            .text(function(d,i) {
if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    if(data[i][columns[0]].toString().length>4){
        return data[i][columns[0]].substring(0, 4)+"..";
    }else{
        return data[i][columns[0]];
    }
}else{
    var percentage = (d.value / parseFloat(sum)) * 100;
if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
 if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){
if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else {
                return percentage.toFixed(1) + "%";
                }
 }else{
//if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
if (typeof displayType !== "undefined" && displayType === "Absolute") {
 if(yAxisFormat==""){
                        return addCommas(numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
                }else {
                return percentage.toFixed(1) + "%";
                }
               }
               }else {
                return "";
            }
            }
            });
if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithvalcont")){
    text.append("tspan")
        .attr("dy", "1.2em")
        .attr("x",-3)
        .text(function(d){
            return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
        });
}
if(typeof displayType!='undefined' && (displayType === "%withabsname" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    text.append("tspan")
        .attr("dy", "1.2em")
        .attr("x",0)
        .text(function(d,i){
            if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
if (typeof displayType !== "undefined" && displayType === "Absolute") {
 if(yAxisFormat==""){
                        return addCommas(numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
                }
                else {
                var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
}
        });
}
   var center_group = svg.append("svg:g");
    if(legendAlign==='Right')
    {
            center_group.attr("class", "ctrGroup")
            .attr("transform", function(d,i){
               if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                  return  "translate(" + (width / 3) + "," + (height / 2) + ")";
               }else{
                  return  "translate(" + (width / 3.5) + "," + (height / 2) + ")";        
             }
             })
    }else{
            center_group.attr("class", "ctrGroup")
            .attr("transform", "translate(" + (width / 2) + "," + (height / 2.3) + ")");
    }

            var words=measureArray[0].split(" ");
            for(var i=0;i<words.length;i++){
                var pieLabel = center_group.append("svg:text")
                .attr("dy", "-"+((words.length-i-1)*1.2+1.5)+"em")
                .attr("text-anchor", "middle")
                .attr("class","gFontFamily")
                .attr("style", "font-size: 11px")
                .text("" + words[i] + "");
            }
    center_group.append("svg:text")
            .attr("dy", "-1em")
            .attr("text-anchor", "middle")
            .attr("class","gFontFamily")
            .attr("style", "font-size: 10px")
            .text(function(d) {
                if (typeof parent.$("#sumValue").val() !== "undefined" && parent.$("#sumValue").val() !== "") {
                    return "" + addCommas(JSON.parse(parent.$("#sumValue").val()).toFixed(0)) + "";
                } else {
                    return "";
                }
            });
    var transformWord = "";
    if (isDouble === false) {
        transformWord = "rotate(-90, 50,60)";
    }else {
        transformWord = "rotate(-90, 110,110)";
    }

    function tweenDonut(b) {
        b.innerRadius = radius * .6;
        var i = d3.interpolate({
            startAngle: 0,
            endAngle: 0
        }, b);
        return function(t) {
            return arc(i(t));
        };
    };
    if ((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) || parent.$("#isMap").val() === "MapTable") {
    }
    else {
        buildDrill(width, height);
    }

if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){}else{     
if(divWidth < 175 || divHeight < 175){
 }else{
if(typeof div !="undefined" && div !="" && div !="chart0" && typeof chartData[div] !="undefined"){

var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width/45);
		var fontsize1;
                if(width<height)
                {
                    fontsize1= parseInt(width/40);
                }
                else
                {
                    fontsize1= parseInt(width/50);
                }
		var rectsize = parseInt(width/60);
                 if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data.length<15 ? data.length : 15); 
                      }

        if(legendAlign==='Right')////
        {
	if(legendLength>12){
            yvalue = parseInt(height / 4);
            rectyvalue = parseInt((height / 4)-10);
            
        }
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2)-(legendLength/2)*(height*.06)-10);
        }
        }
        else
        {
            if(width<height)
            {
                yvalue = parseInt(height*0.8);
                rectyvalue = parseInt((height*0.79 ));
            }
            else
            {
                yvalue = parseInt(height*0.88);
                rectyvalue = parseInt((height*0.86 ));
            }
        }
        var count = 0;
        var transform = "";
 if(typeof displayLength!="undefined" && displayLength!=""&& displayLength!="Yes"){}
 else
     {
if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength!="None")){
    if(legendAlign=='Right')
    {
        var startY=0;
                    if(legendLength>12){
                        startY=(height / 4)-(width*.035)
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }
            svg.append("g")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
           .attr("transform", function(d,i){
           if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                    return "translate(" + width*.68  + "," + parseInt(startY) + ")";
                }else{  
              return "translate(" + width*.58  + "," + parseInt(startY) +")";
                    }
             })
            .attr("fill", "Black")
            .text(function(d){
              if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
              return columns[0];
              }else{
              return "";
              }
            })
            .attr("svg:title",function(d){
               return columns[0];
           })
               for(var i=0;i<legendLength ;i++){
                     if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
            svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", function(d,i){
           if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                    return "translate(" + width*.68  + "," + rectyvalue + ")";
                }else{  
              return "translate(" + width*.58  + "," + rectyvalue +")";
                    }
             })
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
                        
            svg.append("g")
            .append("text")
            .attr("transform", function(d,i){
           if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                    return "translate(" + width*.73  + "," + yvalue + ")";
                }else{  
              return "translate(" + width*.61  + "," + yvalue +")";
                    }
             })
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                  return  getDrawColor(div, parseInt(i));
              }
             }else{
                 return  "#000";
             }
            })    // end @@
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
            
// add by mayank sharma for show table     
            if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{   
//             svg.append("g")   
//            .append("text")
//            .attr("style","margin-right:10")
//            .attr("style", "font-size:"+fontsize+"px")
//            .attr("transform", "translate(" + width*.80  + "," + parseInt(startY) + ")")
//            .attr("fill", "#544F4F")
//            .text(function(d){
//              return measureArray[0]
//            })
            if(legendLength>12){
            yvalue = parseInt(height / 4);
            rectyvalue = parseInt((height / 4)-10);
        }else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);
}
             var count=0; 
            for(var j=0;j<legendLength ;j++){
               if(data[j][measureArray[0]]>0){
                if(j!=0){  
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
             }
             svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.82  + "," + (yvalue) + ")")
            .attr("fill",  "#544F4F")
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return measureArray[0]
            })
            .text(function(d,i){
               return circularChartsTable(div,data,measureArray,j);
            })
              count++   
         }
       } }// end by mayank sharma for show table
}
    else
    {
                   var widthvalue = parseInt((width *.1));
            var widthRectvalue = parseInt((width *.1)-(width*.018));
                       for(var i=0;i<legendLength;i++){
                     if(data[i][measureArray[0]]>0){
                 var charactercount = data[i][columns[0]].length;
              if(i!=0){
               widthvalue = parseInt(widthvalue +  (width*.18) )
               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
               if(count%5==0){
        widthvalue = parseInt((width *.1));
        widthRectvalue = parseInt((width *.1)-(width*.018));
             if(width<height){
              yvalue = parseInt(yvalue-width*.06)
         rectyvalue = parseInt(rectyvalue-width*.06)
             }else{
                yvalue = parseInt(yvalue-height*.06)
         rectyvalue = parseInt(rectyvalue-height*.06)
             }

     }
    }
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", color(i))

            svg.append("g")
            .append("text")
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
                return d[i][columns[0]];
            } )
            .text(function(d){
                if(data[i][columns[0]].length>14){
                    return data[i][columns[0]].substring(0, 14)+"..";
                }else {
                    return data[i][columns[0]];
          }
           })
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
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
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + width*.73  + "," + yvalue + ")")
            .attr("fill", getDrawColor(div, parseInt(i)))
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
            .text(function(d){
                if(data[i][columns[0]].length>25){
                    return data[i][columns[0]].substring(0, 25);
                }else {
                    return data[i][columns[0]];
          }
           })
           .attr("svg:title",function(d){
               return data[i][columns[0]];
           })
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
 }
}
   }
   function isShortForm(d, countryName) {
    var flag = false;
    if ((d.properties.name === "United States" && (countryName.toLowerCase().trim() === "united states of america" || countryName.toLowerCase().trim() === "usa")) || (d.properties.name === "United Arab Emirates" && (countryName.toLowerCase().trim() === "uae")) || (d.properties.name === "Germany" && (countryName.toLowerCase().trim() === "ger"))
       || (d.properties.name === "China" && (countryName.toLowerCase().trim() === "chn")) || (d.properties.name === "Canada" && (countryName.toLowerCase().trim() === "can"))     || (d.properties.name === "Sri Lanka" && (countryName.toLowerCase().trim() === "srl")) || (d.properties.name === "Thailand" && (countryName.toLowerCase().trim() === "thl"))) {
        flag = true;
    }
    return flag;
}

function addCurrencyType(chartId, measureId){
    var chartData=JSON.parse($("#chartData").val());
//    alert("Map : "+JSON.stringify(chartData[chartId]["currencySymbol"]));
    var symbol='';
//    try{
//        alert(chartData[chartId]["currencySymbol"][measureId])
//    }
//    catch(e){
//        
//    }
    if(typeof chartData[chartId]["currencySymbol"]==='undefined' || typeof chartData[chartId]["currencySymbol"][measureId]==='undefined' || chartData[chartId]["currencySymbol"][measureId]==='undefined'){
        symbol='';
if(typeof chartData[chartId]["currencySymbol"]!=='undefined'){
            var elementIds=Object.keys(chartData[chartId]["currencySymbol"]);
            for(var i in elementIds){
                if(typeof measureId!="undefined" && measureId.indexOf(elementIds[i])!=-1){
		
                    symbol=chartData[chartId]["currencySymbol"][elementIds[i]];
    }
            }
        }
    }
    else{
        symbol=chartData[chartId]["currencySymbol"][measureId];
    }
    return symbol;
}
function getMeasureId(measureName){
    var allMeasures=JSON.parse(parent.$("#measure").val());
    var allMeasureIds=JSON.parse(parent.$("#measureIds").val());
    return allMeasureIds[allMeasures.indexOf(measureName)];
}
  
function buildVeractionCombo(i,div, data, columns, measureArray, divWidth, divHeight,KPIresult,divHtmlId,itr){
   var appendDiv = "";
   appendDiv = i

    var html="";
    html +="<div id='"+div+"_KPIDiv"+itr+"' style='width:100%;height:20%'>";
    html +="<table>"
        var chartData=JSON.parse(parent.$("#chartData").val());
        for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
      }
    html +="</table>"
    html +="</div>";
    html +="<div id='TableDiv"+itr+"_"+div+"' style='width:100%;height:80%;float:right;'>";
    html +="</div>";
    $("#"+appendDiv).html(html);
//    for(i in measureArray){

     tileChartsCombo( div,data, chartData[div]["viewBys"],chartData[div]["meassures"],divWidth*0.2,divHeight/3.2,KPIresult[i],div+"_"+measureArray[i].replace(/ /g,''),i,div+"_KPIDiv"+itr);
//    }
     var createBarLine={};
     for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
    var data1={};
    data1[div]=data;
    buildMultiMeasureTrendCombo("TableDiv"+itr+"_"+div, data, columns, measureArray, divWidth-(divWidth*.80), divHeight/4.5);
}

function buildTrSpendVariance(div, data, columns, measureArray, divWidth, divHeight,KPIresult){

    var html="";
//    html +="<div id='"+div+"_KPIDiv' style='float:right;margin-right:20px;margin-top:50px'>";
    html +="<table>"
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
      html +="<tr>"
      html += "<td>";
      html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
      html += "</div>";
      html += "</td>";
      html +="</tr>"
    }
      html +="</table>"
//      html +="</div>";
      html +="<div id='"+div+"SkpiDiv_' style='width:30%;height:86%;margin-top:5%;float:left'>";
      html +="</div>";
       html +="<div id='"+div+"SkpiDiv1_' style='width:18%;height:16%;float:right'>";
      html +="</div>";
      html +="<div id='"+div+"SmoothLDiv_' style='width:70%;height:75%;float:left'>";
      html +="</div>";
      
      $("#"+div).html(html);
//      buildSmoothLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
      buildSmoothLine( div,data, chartData[div]["viewBys"],chartData[div]["meassures"],divWidth*0.6,divHeight/1.2,div+"SmoothLDiv_")
         var createBarLine={};
         for(i=0;i<measureArray.length;i++){
            createBarLine[measureArray[i]]= "Yes";
    }
      chartData[div]["createBarLine"]=createBarLine;
      parent.$("#chartData").val(JSON.stringify(chartData));
      var data1={};
      data1[div]=data;
      
        var measureList = [];
        var kpiList = [];
      for(var l=0;l<3;l++){
      kpiList.push(KPIresult[l])     
      measureList.push(measureArray[l])
}
   buildStackedKPINewDash(div, data, columns, measureList, divWidth, divHeight*.9,kpiList,flag,div+"SkpiDiv_")
   var measureList1 = [];
    var kpiList1 = [];
      kpiList1.push(KPIresult[0])     
   measureList1.push(measureArray[0])
   buildStackedKPINewDash(div, data, columns, measureList1, divWidth, divHeight*.9,kpiList1,flag,div+"SkpiDiv1_")

//       buildStackedKPINewDash(div, data, columns, measureArray, divWidth, divHeight*.9,KPIresult,flag,div+"SkpiDiv_")
//       buildStackedKPINewDash(div, data, columns, measureArray, divWidth, divHeight*.9,KPIresult,flag,div+"SkpiDiv_")

}

function buildVarianceByMode(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
//alert("calling")
    var html="<div id='mainvarDiv_"+div+"' style='width:100%;float:left;height:100%;'>";
             html += "<div id='mainSubDiv1_"+div+"' style='width:90%;float:left;margin-left:5%;height:8%'>"
                html += "<a href='#' class='myButton' onclick='drillWithinchart1(this.id,\""+div+"\")' style='float:right'>VIEW ANALYZE</a>";
             html+="</div>";
           
             html += "<div id='mainSubDiv2_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='mainSubDiv3_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='mainSubDiv4_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='mainSubDiv5_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='mainSubDiv6_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='mainSubDiv7_"+div+"' style='width:90%;float:left;margin-left:5%;height:15%;'></div>";
        html+="</div>";
  
    $("#"+div).html(html);

}

function buildAnalysisKpi(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
//alert("calling")
var rectwidth=15;
    var html="<div id='mainAnalyze_"+div+"' style='width:100%;float:left;height:100%;'>";
//             html += "<div id='mainSubAnalyze1_"+div+"' style='width:100%;float:left;height:21%;border-bottom: 2px solid #eee;'></div>";
//             html += "<div id='mainSubAnalyze2_"+div+"' style='width:100%;float:left;height:25%;border-bottom: 2px solid #eee;'></div>";
//             html += "<div id='mainSubAnalyze3_"+div+"' style='width:100%;float:left;height:25%;border-bottom: 2px solid #eee;'></div>";
//             html += "<div id='mainSubAnalyze4_"+div+"' style='width:100%;float:left;height:25%;'></div>";
 html += "<svg height='100%' width='100%'>";
         
    for(var i=0;i<measureArray.length;i++ ){
             html += "<circle cx='4%' cy='"+(rectwidth)+"%' r='1%' fill='rgb(161, 161, 161)' />";
            rectwidth+=14;
    }
           html += "<text x='6%' y='17%' fill='#696969' style='font-size:120%'>This Month's Score is 170pts from hitting the Target and is within Lower range of the Benchmark.</text>";
           html += "<text x='6%' y='31%' fill='#696969' style='font-size:120%'>Current Average Score is 60pts from hitting the Target and is within the Middle range of the Benchmark.</text>";
           html += "<text x='6%' y='45%' fill='#696969' style='font-size:120%'>Current Average Cost per Shipment is 10% higher than 2015 Average.</text>";
           html += "<text x='6%' y='59%' fill='#696969' style='font-size:120%'>Current Average Cost per Weight is 8% higher than 2015 Average.</text>";
           html += "<text x='6%' y='73%' fill='#696969' style='font-size:120%'>Current Average Spend is 6% more than Target Spend of 2016.</text>";
           html += "<text x='6%' y='87%' fill='#696969' style='font-size:120%'>Current Average Volume is 5% less than Target Volume of 2016.</text>";
    
         html+="</svg>"
         html+="</div>";
  
    $("#"+div).html(html);

}

//function tileChartsCombo(div, data, columns, measureArray, divWidth, divHeight,KPIResult,divId,measIndex,appenddiv1){
////  if()
//   var chartData = JSON.parse(parent.$("#chartData").val());
//   var kpiResultArr=[];
//   var appendDiv = "";
//if(div.indexOf("kpiDiv_")!=-1){
//appendDiv = div
//var splitDiv = div.split("_");
//
//div = splitDiv[1]
//
//}else{
//    if(typeof appenddiv1!="undefined" && appenddiv1!=""){
//   appendDiv =   appenddiv1   
//    }else{
//        
//appendDiv = div
//    }
//
//}
////alert(appendDiv)
//if(typeof divId!='undefined' && divId!=='' && (typeof appenddiv1=="undefined" || appenddiv1=="") ){
//    appendDiv=divId;
//}
// if(typeof chartData[div]["enableComparison"]!=='undefined' && chartData[div]["enableComparison"]==='true'){
//       tileChartsComp(appendDiv, data, columns, measureArray, divWidth, divHeight,KPIResult);
//       return;
//   }
//   var showData = 0;
//   var showData1 = 0;
//   var absoluteNumber = 0;
//   var perFlag = "";
//   var dashledid;
//   var fromoneview=parent.$("#fromoneview").val();
//   var div1=parent.$("#chartname").val();
//     if(fromoneview!='null'&& fromoneview=='true'){
//dashledid=div;
//div=div1;
//     }
//   var imageTag =chartData[div]["filename"];
//    var specialCharacter ="%";
//    var colorPicker = "";
//    var measureColor = "";
//    var measureAliaskpi = "";
//   var DataSum = 0;
//   //font size start
//   var gtFontSize = 0;
//   var infoFontSize = 0;
//   var btFontSize = 0;
//   //font size end
//   var tableData = {};
//    var nameArrCurrent = "";
//    var nameArrPrior = "";
// 
//
//var lineData={};
// var prefix = "";
// var suffix = "";
// var prefixFontSize = "";
// var suffixFontSize = "";
//
//
//showData = KPIResult;
//   var yAxisFormat = "";
//   var yAxisRounding = 0;
//  if(typeof chartData[div]["yAxisFormat"]!= "undefined" && chartData[div]["yAxisFormat"]!= ""){
//      yAxisFormat = chartData[div]["yAxisFormat"];
//  }else{
//   yAxisFormat = "Auto";
//  }
//  if(typeof chartData[div]["rounding"]!= "undefined" && chartData[div]["rounding"]!= ""){
//      yAxisRounding = chartData[div]["rounding"];
//  }else{
//   yAxisRounding =1;
//  }
//
//                  if(yAxisFormat==""){
////                      if(yAxisRounding>0){
////                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
////                      }
////                      else{
//                            showData1 =      (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
////                      }
//
//                    }
//            else{
////             if(yAxisRounding>0){
////                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
////                      }
////                      else{
//                            showData1 =      (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
////                      }
//
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
//colorPicker = "#696969";
//}
//if(typeof chartData[div]["selectedMeasures"]==='undefined'){
//    colorPicker=getDrawColor(div, measIndex);
//}
//else{
//    measIndex=chartData[div]["selectedMeasures"].indexOf(measureArray[0]);
//    if(measIndex==-1){
//        colorPicker="gray";
//    }
//    else{
//        colorPicker=getDrawColor(div, measIndex);
//    }
//}
//if(typeof chartData[div]["lFilledFont"]!="undefined" && chartData[div]["lFilledFont"]!=""){
//  measureColor = chartData[div]["lFilledFont"];
//}else{
//measureColor = "#A1A1A1";
//} 
//if(typeof chartData[div]["measureAliaskpi"]!=="undefined" && chartData[div]["measureAliaskpi"]!==measureArray[0]){
//                  measureAliaskpi=chartData[div]["measureAliaskpi"];
//            }else{
//                    measureAliaskpi=measureArray[0];
//            }
//
//
// if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
//        
//             gtFontSize=divHeight*.13;
////             font=width/39.2;
//            gtFontSize=gtFontSize>45?45:gtFontSize;
//            gtFontSize=gtFontSize<35?35:gtFontSize;
//            // add by maynk sh. for fontsize
//      
//             if(typeof chartData[div]["kpiGTFont"]!=="undefined"){
//                      gtFontSize =  chartData[div]["kpiGTFont"];
//             }
// }else{
//if(divHeight<divWidth){
//  gtFontSize = divHeight*.13
////  infoFontSize = divHeight*.068
//}else{
//   gtFontSize = divWidth*.1
////    infoFontSize = divWidth*.058
//}
// }
// if(appendDiv.indexOf("kpiDiv_")!=-1){
//     gtFontSize = divHeight*.5
// }
// 
// chartData[div]["kpiGTFont"]=gtFontSize;
// 
// if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
//            btFontSize=divHeight*.13;
//            btFontSize=btFontSize>45?45:btFontSize;
//            btFontSize=btFontSize<35?35:btFontSize;
//     
//             if(typeof chartData[div]["kpiSubData"]!=="undefined"){
//                      btFontSize =  chartData[div]["kpiSubData"];
//             }
// }else{
//if(divHeight<divWidth){
//  btFontSize = divHeight*.13
//}else{
//   btFontSize = divWidth*.1
//}
// }
// if(appendDiv.indexOf("kpiDiv_")!=-1){
//     btFontSize = divHeight*.5
// }
// if(chartData[div]["chartType"]==='Combo-Analysis'){
// chartData[div]["kpiSubData"]=gtFontSize*1.2;
// }else{
// chartData[div]["kpiSubData"]=btFontSize;
// }
// 
//  if(typeof chartData[div]["kpiFont"]!="undefined" && chartData[div]["kpiFont"] !== '' && chartData[div]["kpiFont"] !=="4"){
//        
//             infoFontSize=divHeight*.068;
////             font=width/39.2;
//            infoFontSize=infoFontSize>45?45:infoFontSize;
//            infoFontSize=infoFontSize<35?35:infoFontSize;
//            // add by maynk sh. for fontsize
//      
//             if(typeof chartData[div]["kpiFont"]!=="undefined"){
//                      infoFontSize =  chartData[div]["kpiFont"];
//             }
// }else{
//if(divHeight<divWidth){
////  gtFontSize = divHeight*.13
//  infoFontSize = divHeight*.068
//
//}else{
////   gtFontSize = divWidth*.1
//if(appendDiv.indexOf("kpiDiv_")!=-1){
//      infoFontSize = divWidth*.1
//}else{ infoFontSize = divWidth*.058
//}
//   
//}
// }
// if(appendDiv.indexOf("kpiDiv_")!=-1){
//    infoFontSize = divWidth*.058
//}
//
//chartData[div]["kpiFont"]=infoFontSize;
//        
//      
//   $("#chartData").val(JSON.stringify(chartData));
//   if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
//    prefixFontSize = chartData[div]["Prefixfontsize"];
//}else{
//    prefixFontSize = gtFontSize;
//}
//if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
//    Suffixfontsize = chartData[div]["Suffixfontsize"];
//}else{
//    Suffixfontsize = infoFontSize;
//}
//if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
//    prefix = chartData[div]["Prefix"];
//}else{
//    prefix = "";
//}
//if(typeof chartData[div]["Suffix"]!="undefined" && chartData[div]["Suffix"]!="" ){
//    suffix = chartData[div]["Suffix"];
//}else{
//    suffix = "";
//}
//if(typeof divId!='undefined' && divId!==''){
//    gtFontSize='14';
//    prefixFontSize='14';
//    Suffixfontsize='14';
//    infoFontSize='14';
//}
////alert(gtFontSize);
////alert(prefixFontSize);
////alert(Suffixfontsize);
//    parent.$("#chartData").val(JSON.stringify(chartData)); 
//  var html = "";
//   if(fromoneview!='null'&& fromoneview=='true'){
//div=dashledid;
//     }
//  html+="<div id='mainDiv"+div+":"+measureArray[0]+"' style='width:95%;height:auto;  background-color:#FFF'>";
////  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
////  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";
//var backColor1='#FFF';
////if(chartData[div]["chartType"]==='Trend-Table-Combo'){
////        var selectedMeasures=chartData[div]["selectedMeasures"];
////        if(typeof selectedMeasures!=='undefined' && selectedMeasures.indexOf(measureArray[0])!==-1){
////            backColor1='gray';
////        }
////}
//  html+="<div id='innerDiv"+div+":"+measureArray[0]+"' style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+backColor1+"'>";
//  html+="<table style='float:left;margin-top: '>";
//  html+="<tr>";
//
// if(chartData[div]["chartType"]==='Combo-Analysis'){
//if(suffix.trim()==""){
//   html+="<td  style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//}else{ 
//   html+="<td   style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//} 
// }else{
//if(suffix.trim()==""){
//   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)' style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//}else{
//   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)'  style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//}
//}
////html+="<tr>";
//// html+="<td ><img  onclick='trendChartdiv(\""+div+"\")' id='trendChart' class='trendChart1' style='float:right;margin-right: -10%;margin-top: -13%;width:30px;height:30px' src = 'images/icons/tiletrend.png'></td>";
////  html+="</tr>";
//
//  html+="</tr>";
//  html+="<tr>";
// if(fromoneview!='null'&& fromoneview=='true'){
//div=div1;
//     }
//  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
//  }else{
//  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
//  }
//  html+="</tr>";
//  
//   
// 
//  html+="</table>";
//  html+="</div>";
//  html+="</div>";
//if(fromoneview!='null'&& fromoneview=='true'){
//div=dashledid;
//     }
//$("#"+appendDiv).html(html);
//openTableDiv = function(div,measureName){
//    var chartData=JSON.parse(parent.$("#chartData").val());
//    var chartMeasures=chartData[div]["meassures"];
//    var chartMeasureIds=chartData[div]["meassureIds"];
////    if(typeof chartData[div]["selectedMeasures"]==='undefined'){
//    var selectedMesures=chartData[div]["selectedMeasures"];
//    if(typeof selectedMesures==='undefined'){
//        selectedMesures=[];
//    }//
//    if(typeof chartData[div]["kpiMultiSelect"]!=='undefined' && chartData[div]["kpiMultiSelect"]==="Yes"){
//        if(selectedMesures.indexOf(measureName)==-1){
//            selectedMesures.push(measureName);
//        }
//        else{
//            selectedMesures.splice(selectedMesures.indexOf(measureName),1);
//        }
//    }
//    else{
//        selectedMesures=[];
//        selectedMesures.push(measureName);
//    }
//    
//    if(selectedMesures.length==0){
//        for(i in chartMeasures){
//            selectedMesures.push(chartMeasures[i]);
//        }
//    }
//    
//        chartData[div]["selectedMeasures"]=selectedMesures;
//        
////        chartData[div]["selectedMeasures"].push(measureName);
////    }
////    try{
////    alert("Selected meassure:"+chartData[div]["selectedMeasures"]);
////}
////catch(e){}
//    var tempMeasures=[];
//    var tempMeasureIds=[];
//    tempMeasures.push(measureName);
//    tempMeasureIds.push(chartMeasureIds[chartMeasures.indexOf(measureName)]);
//    for(var i in chartMeasures){
//        if(chartMeasures[i]!==measureName){
//            tempMeasures.push(chartMeasures[i]);
//            tempMeasureIds.push(chartMeasureIds[chartMeasures.indexOf(chartMeasures[i])]);
//        }
//    }
//    chartData[div]["meassures"]=tempMeasures;
//    chartData[div]["meassureIds"]=tempMeasureIds;
//    parent.$("#chartData").val(JSON.stringify(chartData));
//////    alert(JSON.stringify({"data":data}));
////    var map1={};
////    map1[div]=chartData[div]["trendChartData"];;
////    var map2={};
////    map2["data"]=JSON.stringify(JSON.stringify(map1));
//$.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
//        function(data){
//            var resultset = data
//
////                    var chartFlag = "false";
////                 var GTdiv = [];
////                 for(var t in chartData){
////                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
////                 chartFlag = "true"
////                 GTdiv.push(t)
////        }
////}
////            if(chartFlag!="false"){
////            var GTData = JSON.parse(data)
////            for(var k in GTdiv){
////            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
////            }
////            parent.$("#chartData").val(JSON.stringify(chartData));
////            }
//
//               var chartData = JSON.parse($("#chartData").val());
//            for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash"  ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//
//                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
//                }
//            }
////            $.ajax({
////            type:"POST",
////            data:parent.$("#graphForm").serialize(),
////            url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+$("#graphsId").val()+"&measId="+tempMeasureIds+"&aggType="+chartData["chart1"]["aggregation"]+"&measName="+JSON.stringify(encodeURIComponent(tempMeasures))+"&chartId=chart1",
////            //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
////            success: function(data){
////                alert(data)
////                //                                parent.$("#chartData").val(JSON.stringify(prevChartData));
////                chartData["chart1"]["GTValueList"] = JSON.parse(data);
////                parent.$("#chartData").val(JSON.stringify(chartData));
////            }
////        });
//            generateSingleChart(resultset,div);
//        });
////    var chartData=
////    $("#openTableTile").dialog({
////        autoOpen: false,
////        height: 440,
////        width: 620,
////        position: 'justify',
////        modal: true,
////        resizable:true,
////        title:'Data Table'
////    });
////    $("#openTableTile").html("");
////    tableData[div] = data[div];
////    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
////    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
////    $("#openTableTile").dialog('open');
//}
////$("#"+div).html(html);
//trendChartdiv = function(div){
//$("#openTableTile").dialog({
//         autoOpen: false,
//         height: 440,
//         width: 620,
//         position: 'justify',
//         modal: true,
//         resizable:true,
//        title:'Trend Chart'
//    });
//    $("#openTableTile").html("");
//    lineData[div] = data[div];
//    var divId='tile';
//buildLine(div, lineData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"], 500, 390,divId)
//    $("#openTableTile").dialog('open');
//}
//}
//htmlvar += "<div id='kpiPieDiv_"+div+"' style='float:left;width:"+divWidth*.38+"px;height:"+divHeight+"px'>";
//htmlvar += "<div id='kpiDiv_"+div+"' style='border-right:1px solid #d1d1d1;float:left;width:"+divWidth*.38+"px;height:"+(divHeight)*.2+"px'></div>";
//htmlvar += "<div id='colPieDiv_"+div+"' style='float:left;border-right:1px solid #d1d1d1;width:"+divWidth*.38+"px;height:"+(divHeight)*.75+"px'></div>";
//htmlvar += "</div>"

function buildModelVarinceDash(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
    
// alert("fcall")  
 var chartcount=8;
 var htmlvar = "";
 
 htmlvar +="<div id='_DashDiv' style='width:100%;height:100%'>";
 for(var i=0;i<chartcount;i++){
     if(chartcount==1){
     htmlvar += "<div id='DivChart1_"+i+div+"' style='width:99%;float:left;height:100%;border:1px solid'></div>";
     }
    else if (chartcount==4){
 htmlvar += "<div id='DivChart4_"+i+div+"' style='width:49%;float:left;height:49%;border:1px solid'></div>";
 }else if(chartcount==6){
     htmlvar += "<div id='DivChart6_"+i+div+"' style='width:33%;float:left;height:50%;border:1px solid'></div>";
 }else if(chartcount==3){
     htmlvar += "<div id='DivChart3_"+i+div+"' style='width:33%;float:left;height:100%;border:1px solid'></div>";
 }else if(chartcount==5){
     htmlvar += "<div id='DivChart5_"+i+div+"' style='width:33%;float:left;height:50%;border:1px solid'></div>";
 }else if(chartcount==8){
     htmlvar += "<div id='DivChart7_"+i+div+"' style='width:24%;float:left;height:50%;border:6px solid rgb(236, 239, 241);'></div>";
 }
        else{
    htmlvar += "<div id='DivChart2_"+i+div+"' style='width:49%;float:left;height:100%;border:1px solid'></div>"; 
 }
 
    
    }
 htmlvar += "</div>";
    
 $("#"+div).html(htmlvar);
 for(var i=0;i<chartcount;i++){
// //     if (chartcount==4){
 buildVeractionCombo("DivChart7_"+i+div,div, data, columns, measureArray, divWidth, divHeight,KPIresult,'',i)
// buildVeractionCombo("DivTops_"+div, data, columns, measureArray, divWidth, divHeight,KPIresult)
// buildDial("DialDivTop1_"+div,data, columns, measureArray[0],divWidth*.23,divHeight*.21,KPIresult[0]);
//}
}
}

function buildInImpactAnalysis(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
var html="";

    html+="<div style='width:100%;height:100%;float:left;'>";
    html +="<div id='"+div+"_KPIDiv' style='width:98%;height:8%;float:left;margin-left:2%;border-bottom: 2px solid #eee'>";
    html +="<table>"
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
    }
    html +="</table>"
    html +="</div>";
    var rectHeight=0;
    graphProp(div);
    var rectwidth=1;
    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:32%;float:left;'>";
    html +="<svg width='100%' height='100%'>";
    for(var i=0;i<measureArray.length;i++ ){
//        if(i==0){
            
        html +="<rect x='"+(rectwidth)+"%' y='28%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#C70500'></rect>";
        html+= "<text x='"+(rectwidth)+"%' y='50%' style='font-size:18px; font-family:helvatica;' fill='white' > "+addCommas(numberFormat(KPIresult[i],yAxisFormat,yAxisRounding,div))+"</text>";
        html+= "<text x='"+(rectwidth)+"%' y='60%' style='font-size:100%; font-family:helvatica;' fill='white' > "+measureArray[i]+"</text>";
//        }
//        else{
//            
//        html +="<rect x='"+(rectwidth)+"%' y='28%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#cc0000'></rect>";
//        html+= "<text x='"+(rectwidth)+"%' y='50%' style='font-size:100%' fill='white' > "+KPIresult[i]+"</text>";
//        html+= "<text x='"+(rectwidth)+"%' y='60%' style='font-size:100%' fill='white' > "+measureArray[i]+"</text>";
//        }
        rectwidth+=14;
    }
//    html +="<div id='"+div+"_TrendDiv' style='width:"+(divWidth-(divWidth*0.01))+"px;,height:"+(divHeight)+"px;'>";
//    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:30%;float:left;'>";
//    html +="<svg width='100%' height='100%'>";
//    html +="<rect x='4%' y='28%' width='13%' height='26%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#cc0000'></rect>";
////    html +="<line x1='3.9%' y1='54%' x2='17%' y2='54%' style='stroke:#ff0000;stroke-width:2' />";
//    html +="<rect x='17.3%' y='41%' width='13%' height='26%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#ffcc00'></rect>";
////    html +="<line x1='17.3%' y1='41%' x2='30.4%' y2='41%' style='stroke:#cccc00;stroke-width:2' />";
//    html +="<rect x='30.6%' y='14%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";
////    html +="<line x1='30.5%' y1='54%' x2='43.6%' y2='54%' style='stroke:#ff0000;stroke-width:2' />";
//    html +="<rect x='43.9%' y='41%' width='13%' height='50%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#669900'></rect>";
////    html +="<line x1='43.8%' y1='41%' x2='57%' y2='41%' style='stroke:#009900;stroke-width:2' />";
//    html +="<rect x='57.2%' y='22%' width='13%' height='32%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";
////    html +="<line x1='57.1%' y1='54%' x2='70.2%' y2='54%' style='stroke:#ff0000;stroke-width:2' />";
//    html +="<rect x='70.5%' y='41%' width='13%' height='32%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#669900'></rect>";
////    html +="<line x1='70.4%' y1='41%' x2='83.6%' y2='41%' style='stroke:#009900;stroke-width:2' />";
//    html +="<rect x='83.8%' y='29%' width='13%' height='25%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";
////    html +="<line x1='83.7%' y1='54%' x2='96.8%' y2='54%' style='stroke:#ff0000;stroke-width:2' />";
    html +="</svg>";
    html +="</div>";
//    html +="<div id='"+div+"_TableDivData' style='width:100%;height:0px;float:left;display:none;background-color:#E0E0E0;margin-left:1%;margin-top:-1%'>";
//    html +="</div>";
    html +="<div id='"+div+"_TableDiv' style='width:100%;height:52%;float:left;'>";
    html +="</div>";
    html +="<div id='"+div+"_BottomDiv' style='width:100%;height:8%;float:left;'>";
    html +="</div>";
    html +="</div>";
   
    $("#"+div).html(html);
    
    var KPIHtml = "";
    for(var i=0;i< measureArray.length;i++ ){
        if(i<2){
    KPIHtml +="<div id='KPI_div_"+i+"_"+div+"' style='float:right;margin-right:2%;width:15%;height:100%;'>";
    KPIHtml +="<svg style='float:left;width:10%;height:100%;'>";
    KPIHtml += "<circle cx='60%' cy='44%' r='14%' fill='"+getDrawColor(div, parseInt(i))+"' />";
    KPIHtml +="</svg>";
    KPIHtml +="<div style='width:90%;height:100%;margin-top:10%;float:left; font-family:helvatica; color:#202020'> "+measureArray[i]+"</div>"
    KPIHtml +="</div>";
    } }
    $("#"+div+"_BottomDiv").append(KPIHtml);
    
    var KPIHtml = "";
    for(var i=0;i<measureArray.length;i++ ){
        if(i<2){
    KPIHtml +="<div id='KPI_div_"+i+"_"+div+"' style='float:right;margin-right:2%;width:20%;height:90%;'>";
    KPIHtml +="<div style='width:100%;height:50%; font-size:13px; font-family:helvatica; font-weight:bold; color:rgb(161, 161, 161);'>"+columns[0]+"</div>"
    KPIHtml +="<div style='width:100%;height:50%; font-family:helvatica;font-size:12px; color:#202020'>"+addCommas(numberFormat(KPIresult[i],yAxisFormat,yAxisRounding,div))+" / "+measureArray[i]+"</div>"
    KPIHtml +="</div>";
        } }
    $("#"+div+"_KPIDiv").append(KPIHtml);
    
    var createBarLine={};
    for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));

    var data1={};
    data1[div]=data;
    
   var measureList = [];
   for(var l=0;l<2;l++){     
   measureList.push(measureArray[l])
}
 buildMultiMeasureTrLine(div, data, columns,measureList,divWidth-(divWidth*0.2), divHeight/2.1,"",div+"_TableDiv");
}
//var clickCount="";
//function showDrillTableDiv(id,eId,classn){
//   ( $("#"+eId).attr("class"))
//    
//    if(clickCount==0){
//        clickCount=1;
//        $("#"+eId).css("fill", "#ffe6e6");
//        $("#"+eId).addClass("red_Hide");
//        $("#"+id).css("display", "block");
//        $("#"+id).css("cursor", "pointer");
//        $("#"+id).prev().animate({height:'-=15%'},"slow");
//        $("#"+id).animate({height:'+=25%'},"slow");
//        $("#"+id).next().animate({height:'-=10%'},"slow");
//    }else{
//        $(".red_Hide").css("fill", "#ffffff");
//         $("#"+eId).css("fill", "#ffffff");
//         $("#"+id).css("display", "none");
//        $("#"+id).prev().animate({height:'+=15%'},"slow");
//        $("#"+id).animate({height:'-=30%'},"slow");
//        $("#"+id).next().animate({height:'+=13%'},"slow");
////        $("#"+eId).css("fill", "red");
//         clickCount=0;
//    }               
//}
function buildInImpactAnalysis2(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
//    alert("hellofun")
var html="";

    html+="<div style='width:100%;height:100%;'>";
    html +="<div id='"+div+"_KPIDiv' style='width:98%;height:6%;float:left;margin-left:1%;border-bottom: 2px solid #eee'>";
    html +="<table>"
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
    }
    html +="</table>"
    html +="</div>";
//    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:18%;float:left;border-bottom:2px solid #eee'>";
    var rectHeight=0;
    var rectwidth=1;
    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:18%;float:left;border-bottom:2px solid #eee'>";
    html +="<svg width='100%' height='100%'>";
    for(var i=0;i<measureArray.length;i++ ){
//        if(i==0){

        html +="<rect x='"+(rectwidth)+"%' y='28%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#C70500'></rect>";
        html+= "<text x='"+(rectwidth)+"%' y='50%' style='font-size:18px; font-family:helvatica;' fill='white' > "+addCommas(numberFormat(KPIresult[i],yAxisFormat,yAxisRounding,div))+"</text>";
        html+= "<text x='"+(rectwidth)+"%' y='60%' style='font-size:100%; font-family:helvatica;' fill='white' > "+measureArray[i]+"</text>";
        rectwidth+=14;
    }
//    html +="<svg width='100%' height='100%'>";
//    html +="<rect x='4%' y='28%' width='13%' height='26%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#cc0000'></rect>";
////    html +="<line x1='3.9%' y1='54%' x2='17%' y2='54%' style='stroke:#ff0000;stroke-width:2' />";
//    html +="<rect x='17.3%' y='41%' width='13%' height='26%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#ffcc00'></rect>";
//    html +="<rect x='30.6%' y='14%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";
//    html +="<rect x='43.9%' y='41%' width='13%' height='50%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#669900'></rect>";
//    html +="<rect x='57.2%' y='22%' width='13%' height='32%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";
//    html +="<rect x='70.5%' y='41%' width='13%' height='32%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#669900'></rect>";
//    html +="<rect x='83.8%' y='29%' width='13%' height='25%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");'  fill='#cc0000'></rect>";

    html +="</svg>";
    html +="</div>";
    html +="<div id='"+div+"_DualDivData' style='width:100%;height:27%;float:left;'>";
    html +="<div id='"+div+"_DualSubData1' style='width:49%;height:100%;float:left;border-right:2px solid #eee'></div>";
    html +="<div id='"+div+"_DualSubData2' style='width:49%;height:100%;float:left;'></div>";
    html +="</div>";
    html +="<div id='"+div+"_TableDivData' style='width:100%;height:23%;float:left;'>";
     html +="<div id='"+div+"_TableSubData1' style='width:44%;height:100%;float:left;margin-left:3%;padding-right:2%;border-right:2px solid #eee'></div>";
     html +="<div id='"+div+"_TableSubData2' style='width:46%;height:100%;float:left;margin-left:3%;'></div>";
    html +="</div>";
    html +="<div id='"+div+"_LineDiv' style='width:100%;height:23%;float:left;'>";
    html +="</div>";
    html +="<div id='"+div+"_BottomDiv' style='width:100%;height:6%;float:left;'>";
    html +="</div>";
    html +="</div>";
   
    $("#"+div).html(html);
    
    var KPIHtml = "";
    for(var i=0;i< measureArray.length;i++ ){
        if(i<2){
    KPIHtml +="<div id='KPI_div_"+i+"_"+div+"' style='float:right;margin-right:2%;width:15%;height:100%;'>";
    KPIHtml +="<svg style='float:left;width:10%;height:100%;'>";
    KPIHtml += "<circle cx='60%' cy='44%' r='14%' fill='"+getDrawColor(div, parseInt(i))+"' />";
    KPIHtml +="</svg>";
    KPIHtml +="<div style='width:90%;height:100%;margin-top:10%;float:left; font-family:helvatica; color:#202020'> "+measureArray[i]+"</div>"
    KPIHtml +="</div>";
    } }
    $("#"+div+"_BottomDiv").append(KPIHtml);
    
    var KPIHtml = "";
    for(var i=0;i< measureArray.length;i++ ){
        if(i<2){
    KPIHtml +="<div id='KPI_div_"+i+"_"+div+"' style='float:right;margin-right:2%;width:20%;height:90%;'>";
    KPIHtml +="<div style='width:100%;height:50%; font-size:13px; font-family:helvatica; font-weight:bold; color:rgb(161, 161, 161);'>"+columns[0]+"</div>"
    KPIHtml +="<div style='width:100%;height:50%; font-family:helvatica;font-size:12px;color:#202020'>"+addCommas(numberFormat(KPIresult[i],yAxisFormat,yAxisRounding,div))+" / "+measureArray[i]+"</div>"
    KPIHtml +="</div>";
    } }
    $("#"+div+"_KPIDiv").append(KPIHtml);
    
    var createBarLine={};
    for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
    
    
    var data1={};
    data1[div]=data;
    
    var measureList = [];
    for(var l=0;l<2;l++){     
    measureList.push(measureArray[l])
   }
    buildTableComboChart(div,data1, data, chartData[div]["viewBys"],measureList,divWidth-(divWidth*0.2), divHeight/4,KPIresult,div+"_TableSubData2");
    buildTableComboChart(div,data1, data, chartData[div]["viewBys"], measureList,divWidth-(divWidth*0.2), divHeight/4,KPIresult,div+"_TableSubData1");
    

    buildMultiMeasureTrLine(div, data, columns, measureList, divWidth-(divWidth*0.2), divHeight/5,"",div+"_LineDiv");
    
    buildDualAxisBar(div, data, columns, measureList, divWidth-(divWidth*0.2), divHeight/2.5,"",div+"_DualSubData1");
    buildDualAxisBar(div, data, columns, measureList, divWidth-(divWidth*0.2), divHeight/2.5,"",div+"_DualSubData2");
}

function buildVeractionCombo3(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
var html="";
//    var html="<div id='DualDiv_"+div+"' style='width:23%;float:left;height:95%;border-style: solid;border-width:2px;border-color:#eee;margin-top: 10px;'>";
//             html += "<div id='DualDivTop1_"+div+"' style='width:100%;float:left;height:20%;border-bottom: 2px solid #eee;'></div>";
//             html += "<div id='DualDivTop2_"+div+"' style='width:100%;float:left;height:20%;border-bottom: 2px solid #eee;'></div>";
//             html += "<div id='DualDivTop3_"+div+"' style='width:100%;float:left;height:60%;'></div>";
//        html+="</div>";
    html+="<div style='width:100%;height:100%;'>";
//    html +="<div id='"+div+"_KPIDiv' style='width:98%;height:12%;float:left;margin-left:2%;border-bottom: 2px solid #eee'>";
    html +="<table>"
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
    }
    html +="</table>"
//    html +="</div>";
//    html +="<div id='"+div+"_TrendDiv' style='width:"+(divWidth-(divWidth*0.01))+"px;,height:"+(divHeight)+"px;'>";
 html +="<div id='"+div+"_TopDiv' style='width:100%;height:15%;float:left;'>";
    html +="</div>";
    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:75%;float:left;'>";
      html +="</div>";
    html +="<div id='"+div+"_BottomDiv' style='width:100%;height:10%;float:left;border-top:2px solid; border-color:rgb(238, 238, 238);'>";
      html +="</div>";
   html +="</div>";
    $("#"+div).html(html);
    var KPIHtml = "";
    for(var i=0;i< measureArray.length;i++ ){
    KPIHtml +="<div id='KPI_div_"+i+"_"+div+"' style='float:left;margin-right:2%;width:15%;height:100%;'>";
    KPIHtml +="<svg style='float:left;width:10%;height:100%;'>";
    KPIHtml += "<circle cx='60%' cy='44%' r='14%' fill='"+getDrawColor(div, parseInt(i))+"' />";
    KPIHtml +="</svg>";
    KPIHtml +="<div style='width:90%;height:100%;margin-top:10%;float:left; font-family:helvatica; color:#202020'> "+measureArray[i]+"</div>"
    KPIHtml +="</div>";
    }
    $("#"+div+"_BottomDiv").append(KPIHtml);
//    alert(chartData[div]["yAxisFormat"])
    graphProp(div);
    var KPIHtml = "";
    KPIHtml +="<div id='KPI_div_1"+i+"_"+div+"' style='float:left;margin-left:5%;margin-top:2%;height:100%;'>";
    var currncyFormatType=changeFormatInDetail(yAxisFormat);
    KPIHtml +="<div style='width:100%;margin-top:10%; font-family:helvatica; color:#202020'> "+measureArray[0]+" <br> <font color='#8BC34A'>("+currncyFormatType+")</font></div>"
    KPIHtml +="</div>";
    $("#"+div+"_TopDiv").append(KPIHtml);
    var currncyFormatType1=changeFormatInDetail(y2AxisFormat);
    var KPIHtml = "";
    KPIHtml +="<div id='KPI_div_2"+i+"_"+div+"' style='float:right;margin-right:5%;margin-top:2%;height:100%;'>";
    KPIHtml +="<div style='width:100%;margin-top:10%; font-family:helvatica; color:#202020'> "+measureArray[3]+"  <br> <font color='#8BC34A'>("+currncyFormatType1+")</font></div>"
    KPIHtml +="</div>";
    $("#"+div+"_TopDiv").append(KPIHtml);
   
    var createBarLine={};
    for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
//    buildTableTrendCombo_LineChart(div, data, columns, measureArray, divWidth-(divWidth*0.2), divHeight/2.2,"",div+"_TrendDiv");
    var data1={};
    data1[div]=data;
    buildDualAxisBar(div, data, columns, measureArray, divWidth-(divWidth*0.2), divHeight/1.5,"",div+"_TrendDiv")
//   buildDial("DualDivTop1_"+div,data, columns, measureArray[0],divWidth*.23,divHeight*.21,KPIresult[0]);
//   buildDial("DualDivTop2_"+div,data, columns, measureArray[1],divWidth*.23,divHeight*.21,KPIresult[1]);
   
}
function changeFormatInDetail(yAxisFormat){
    if(yAxisFormat=='K'){
        return "Thousand";
    }else if(yAxisFormat=='M'){
        return "Million";
    }else if(yAxisFormat=='B'){
        return "Billion";
    }else if(yAxisFormat=='Cr'){
        return "Crore";
    }else if(yAxisFormat=='L'){
        return "Lakh";
    }else{
        return yAxisFormat;
    }
}
function buildScoreTargetCombo(div, data, columns, measureArray, divWidth, divHeight,KPIresult){

    
//        var rectHeight=0;
//    var rectwidth=4;
//    html +="<div id='"+div+"_TrendDiv' style='width:100%;height:18%;float:left;border-bottom:2px solid #eee'>";
//    html +="<svg width='100%' height='100%'>";
//    for(var i=0;i<measureArray.length;i++ ){
////        if(i==0){
//            
//        html +="<rect x='"+(rectwidth)+"%' y='28%' width='13%' height='40%' style='cursor:pointer' onclick='drillWithinchart1(this.id,\""+div+"\");' fill='#cc0000'></rect>";
//        html+= "<text x='"+(rectwidth)+"%' y='50%' style='font-size:100%; font-family:helvatica;' fill='white' > "+KPIresult[i]+"</text>";
//        html+= "<text x='"+(rectwidth)+"%' y='60%' style='font-size:100%; font-family:helvatica;' fill='white' > "+measureArray[i]+"</text>";
//        rectwidth+=14;
//    }
    var rectwidth=24;
    var html="<div id='MainTrDiv_"+div+"' style='width:100%;float:left;height:100%;margin-top: 9%;'>";
             html += "<div id='TrDivTop2_"+div+"' style='width:100%;float:left;height:9%;'></div>";
             html += "<div id='TrDivTop1_"+div+"' style='width:100%;float:left;height:9%;'></div>";
             html += "<div id='TrDivTop3_"+div+"' style='width:100%;float:left;height:21%;'></div>";
             html += "<div id='TrDivTop4_"+div+"' style='width:100%;float:left;height:20%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='TrDivTop5_"+div+"' style='width:100%;float:left;margin-top:7%;border-bottom: 2px solid #eee;'></div>";
             html += "<div id='TrDivTop6_"+div+"' style='width:100%;float:left;height:30%;'>";
             html += "<svg height='100%' width='100%'>";
       
  
 for(var i=0;i<measureArray.length;i++ ){
             html += "<circle cx='8%' cy='"+(rectwidth)+"%' r='4%' fill='#669900' />";
            rectwidth+=18;
    }
           html += "<text x='15%' y='25%' fill='#696969' style='font-size:120%'>Total Variance</text>";
           html += "<text x='15%' y='43%' fill='#696969' style='font-size:120%'>Biggest Positive Contribution is from <tspan y='51%' x='15%'>Zone/Distance</tspan></text>";
           html += "<text x='15%' y='61%' fill='#696969' style='font-size:120%'>Biggest Negative Contribution is from <tspan y='70%' x='15%'>weight/Dimension</tspan></text>";
           html += "<text x='15%' y='80%' fill='#696969' style='font-size:120%'>Total Rate</text>";
             html += "</svg>"; 
         html += "</div>";
        html+="</div>";
        
    html +="<table>";
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
    }
    html +="</table>"
    $("#"+div).html(html);
    buildSmoothLine( div,data, chartData[div]["viewBys"],chartData[div]["meassures"],divWidth*0.6,divHeight/7,"TrDivTop3_"+div)
    var createBarLine={};
    for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
//    buildTableTrendCombo_LineChart(div, data, columns, measureArray, divWidth-(divWidth*0.2), divHeight/2.2,"",div+"_TrendDiv");
    var data1={};
    data1[div]=data;
    
   buildStackedKPINew(div, data, columns, measureArray, divWidth, divHeight*.9,KPIresult,flag,"TrDivTop4_"+div)
   buildDialNew("TrDivTop1_"+div,data, columns, measureArray[0],divWidth*.23,divHeight*.21,KPIresult[0]);
   buildDialNew("TrDivTop2_"+div,data, columns, measureArray[1],divWidth*.23,divHeight*.21,KPIresult[1]);
   var measureList = [];
   var kpiList = [];
   for(var l=0;l<2;l++){
  kpiList.push(KPIresult[l])     
   measureList.push(measureArray[l])
   }
   buildStackedKPINew(div, data, columns, measureList, divWidth, divHeight*.9,kpiList,flag,"TrDivTop5_"+div)
   
   var measureList1 = [];
    var kpiList1 = [];
      kpiList1.push(KPIresult[0])     
   measureList1.push(measureArray[0])
   buildStackedKPINew(div, data, columns, measureList1, divWidth, divHeight*.9,kpiList1,flag,"TrDivTop1_"+div)
   
var measureList2 = [];
    var kpiList2 = [];
      kpiList2.push(KPIresult[1])     
   measureList2.push(measureArray[1])
   buildStackedKPINew(div, data, columns, measureList2, divWidth, divHeight*.9,kpiList2,flag,"TrDivTop2_"+div)
}

function buildSplitBubble(div, data, columns, measureArray, divWidth, divHeight, layout){
    var chartData = JSON.parse(parent.$("#chartData").val());
    var color = d3.scale.category10();
    var colIds;
    var colName;
    var dashletid;
    var count = 0;
    var colorCount=0
    var columnsVar = columns;
    var colorGroup = {};
    var circles1;
    var div1=parent.$("#chartname").val()
    if(fromoneview!='null'&& fromoneview=='true'){
        var prop = graphProp(div1);
        dashletid=div;
        div=div1
        colIds=chartData[div1]["viewIds"];
        colName=chartData[div]["viewBys"];
    }else{
        colIds = chartData[div]["viewIds"];
        colName=chartData[div]["viewBys"];
        var prop = graphProp(div);
    }
    var BubbleChart, root,
            __bind = function(fn, me) {
                return function() {
                    return fn.apply(me, arguments);
                };
            };

    $("#legendchart").show();
    var margin = {
        top: 10, 
        right: 12,  
        bottom: 30, 
        left: 70
    };
    var legendColorMap = {};
    var divhtml = "<div id='tabDiv' width='50%' style='float:left;margin-left:0%;width:2%'></div>";
    $("body").append(divhtml);
    BubbleChart = (function() {
        var chartMap = {};
        function BubbleChart(data) {
            this.hide_details = __bind(this.hide_details, this);

            this.show_details = __bind(this.show_details, this);

            this.hide_years = __bind(this.hide_years, this);

            this.display_years = __bind(this.display_years, this);

            this.move_towards_year = __bind(this.move_towards_year, this);

            this.display_by_year = __bind(this.display_by_year, this);

            this.move_towards_center = __bind(this.move_towards_center, this);

            this.display_group_all = __bind(this.display_group_all, this);

            this.start = __bind(this.start, this);
            this.create_vis = __bind(this.create_vis, this);
            this.create_nodes = __bind(this.create_nodes, this);
            var max_amount;
            this.data = data;
            this.width = divWidth;
            this.height = divHeight-50;
            this.center = {
                x: this.width/2 ,
                y: this.height / 2.4
            };
            this.layout_gravity = -0.01;
            this.damper = 0.1;
            this.vis = null;
            this.nodes = [];
            this.force = null;
            this.circles = null;
            this.fill_color = d3.scale.category12();
            max_amount = d3.max(this.data, function(d) {
                return parseFloat(d[measureArray[0]]);
            });
            var radScale = 38;
            this.radius_scale = d3.scale.pow().exponent(0.5).domain([0, max_amount]).range(["2", radScale]);
            this.create_nodes();
            this.create_vis();
        }
        var legends = [];
        BubbleChart.prototype.create_nodes = function() {
            var _this = this;
            this.data.forEach(function(d, i) {
                if (i < 500) {
                    var node;
                    node = {
                        id: d[measureArray[0]],
                        radius: _this.radius_scale(parseFloat(d[measureArray[0]])),
                        value: d[measureArray[0]],
                        measure: measureArray[0].replace("_", "-", "gi"),
                        name: d[columns[0]],
                        group: d[columns[0]],
                        year: d[columns[1]],
                        x: Math.random(),
                        y: Math.random()
                    };
                    return _this.nodes.push(node);
                }
            });
            return this.nodes.sort(function(a, b) {
                return b.value - a.value;
            });
        };
        data.forEach(function(d, i) {
            if (legends.indexOf(d[columns[0]]) === -1)
                legends.push(d[columns[0]]);
        });
        BubbleChart.prototype.create_vis = function() {
            var that,
            _this = this;
            this.vis = d3.select("#"+div)
            .append("svg")
            .attr("viewBox", "0 -20 "+(divWidth)+" "+(divHeight + margin.top + margin.bottom)+" ")
            .append("g")
            .attr("id", "svg_vis");
            
            var gradient = this.vis.append("svg:defs").selectAll("radialGradient").data(this.data).enter()
            .append("svg:radialGradient")
            .attr("id", function(d) {
                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("fx", "5%")
            .attr("fy", "5%")
            .attr("r", "50%")
            .attr("spreadMethod", "pad");

            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
            
            gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                }else{
                    colorShad = _this.fill_color(i);
                }
                if (typeof legendColorMap[d[columns[0]].toString()] === "undefined") {
                    legendColorMap[d[columns[0]].toString()] = colorShad;
                }
                if (typeof chartMap[d[columns[0]]] === "undefined") {
                    chartMap[d[columns[0]]] = colorShad;
                    colorMap[i] = d[columns[0]] + "__" + colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
            parent.$("#legends").val(JSON.stringify(chartMap));
            var gradient = this.vis.append("svg:defs").selectAll("radialGradient").data(this.data).enter()
            .append("svg:radialGradient")
            .attr("id", function(d) {
                return "gradientDrill";
            })
            .attr("fx", "5%")
            .attr("fy", "5%")
            .attr("r", "50%")
            .attr("spreadMethod", "pad");

            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
            
            gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", drillShade)
            .attr("stop-opacity", 1);
           var node = this.vis.append("g") 
            this.circles = this.vis.selectAll("g.node")
            .data(this.nodes);
            that = this;

            circles1= this.circles.enter()
            .append("g").attr("class", "node");
            circles1.append("circle")
            .attr("fill", function(d,i) {
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
            .attr("color_value", function(d,i) {
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
            .attr("stroke-width", 2)
            //            .attr("stroke", function(d) {
            //                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            //            })
            .attr("id", function(d) {
                return d.name+ ":" + d.id + "_" + d.measure;
            })
            .attr("opacity",".7")
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
                prevColor = getDrawColor(div, parseInt(i));
                if(fromoneview!='null'&& fromoneview=='true'){
                    show_detailsoneview(d, columns, measureArray, this,chartData,div1);
                }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    that.show_details(d, columns, measureArray, this,div);
                }
            })
            .on("mouseout", function(d, i) {
                var bar = d3.select(this);
                var indexValue = bar.attr("index_value");
                var barSelector = "." + "bars-Bubble-" + indexValue+div;
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);
                that.hide_details(d, i, this);
            })
            
            circles1.append("text")
            .attr("text-anchor", "middle")
            .attr("class", "gFontFamily")
            .style("font-size", '11px')
            .text(function(d, i) {
                if(d.radius<15){
                    return "";
                }
                try{
                if(d.year.length>d.radius*2/8){
                    return d.year.substring(0,parseInt(d.radius*2/8))+"..";
                }
                return d.year;
            } catch (e) {
                    }
            });

    var displayLength = chartData[div]["displayLegends"]
            var yvalue=0;
            var rectyvalue=0;
		var rectyvalue1=0;
		var len = parseInt(divWidth-150);
		var rectlen = parseInt(divWidth-200);
            var fontsize = parseInt(divWidth/45);
            var fontsize1 = parseInt(divWidth/50);
            var rectsize = parseInt(divWidth/60);
            var legendLength;
//           var colName=columns[0];

            var transform="";
            var startY=0;
             if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }else{
                    legendLength=(data.length<12 ? data.length : 12); 
                }
            if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
            if(legendLength>12){
                startY=(divHeight / 4)-(divWidth*.035)
            }else{
                startY=(divHeight / 2-(legendLength/2)*(divHeight*.06))-(divWidth*.035)
            }
            
            node.append("g")
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", function(d,i){
              return  "translate(" + divWidth*.80  + "," + parseInt(startY) +")";
            })
            .attr("fill", "Black")
            .text(function(d){
              if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
              return columns[0];
              }else{
              return "";
              }
            })
             .attr("svg:title",function(d){
               return columns[0];
           })
           
        if(legendLength>12){
                yvalue = parseInt(divHeight / 8);
                rectyvalue = parseInt((divHeight / 8)-5);
            }else{
                yvalue = parseInt(divHeight / 2)-(legendLength/2)*(divHeight*.06);
                rectyvalue = parseInt((divHeight / 2-(legendLength/2)*(divHeight*.06))-5);
        }
           

            var colMap={};
             var count = 0;
             
            for(var m=0;m<data.length;m++){
        colMap[data[m][columns[0]]]=data[m][columns[0]];
            }      
//    Object.keys(colMap)==12;
            for(var i=0;i<legendLength ;i++){
             if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+divHeight*.06)
            rectyvalue = parseInt(rectyvalue+divHeight*.06)
            }
        
           node.append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("opacity",".7")
            .attr("style", "overflow:scroll")
            .attr("transform", function(d,i){
            return  "translate(" + divWidth*.80  + "," + (rectyvalue-12) + ")";
            })
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
            
           node.append("text")
            .attr("text-anchor", "start")
            .attr("transform", "translate(" + divWidth*.82  + "," + (yvalue-11) + ")")
            .attr("dy", ".3em")
            .attr("id",function(d){
                return Object.keys(colMap)[i];
            })
            .attr("font-family", "Verdana")
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
            .text(function(d) {
             return Object.keys(colMap)[i]    
            })
            .attr("svg:title",function(d){
               return Object.keys(colMap)[i];
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
           })
           .on("mouseout", function(d, j) {
            setMouseOutEvent(this.id,div)
           })
           count++;
}
    }
            
            
            return this.circles.selectAll("circle").transition().duration(500)
            .attr("r", function(d) {
                return d.radius;
            })
            
        };

        BubbleChart.prototype.charge = function(d) {
            return -Math.pow(d.radius, 2.0) / 8;
        };

        BubbleChart.prototype.start = function() {
            return this.force = d3.layout.force()
            .nodes(this.nodes)
            .size([this.width*.5, this.height*.5]);
        };

        BubbleChart.prototype.display_group_all = function() {
            var _this = this;
            this.force.gravity(this.layout_gravity).charge(this.charge).friction(0.9).on("tick", function(e) {
                return _this.circles.each(_this.move_towards_center(e.alpha))
                .attr("transform", function(d) {
          return "translate(" + (d.x - 70) + "," + (d.y + 10) + ")";
                });
            });
            this.force.start();
            return this.hide_years();
        };

        BubbleChart.prototype.move_towards_center = function(alpha) {
            var _this = this;
            return function(d) {
                d.x = d.x + (_this.center.x - d.x) * (_this.damper + 0.02) * alpha;
                return d.y = d.y + (_this.center.y - d.y) * (_this.damper + 0.02) * alpha;
            };
        };
        
        BubbleChart.prototype.display_by_year = function() {
            var _this = this;
            this.force.gravity(this.layout_gravity).charge(this.charge).friction(0.9).on("tick", function(e) {
                return _this.circles.each(_this.move_towards_year(e.alpha))
                .attr("cx", function(d) {
                    return d.x-70 ;
                })
                .attr("cy", function(d) {
                    return d.y+10 ;
                });
            });
            this.force.start();
            return this.hide_years();
        };

        BubbleChart.prototype.move_towards_year = function(alpha) {
            var _this = this;
            return function(d, i) {
                var target;
                target = _this.year_centers[d.year];
                if (typeof target !== "undefined") {
                    d.x = d.x + (target.x - d.x) * (_this.damper + 0.02) * alpha * 1.1;
                    return d.y = d.y + (target.y - d.y) * (_this.damper + 0.02) * alpha * 1.1;
                }
            };
        };

        BubbleChart.prototype.display_years = function() {
            var years, years_data,
            _this = this;
            var allValuesArray = [];
            var group_labels = [];
            var yea = {};
            var mul = 1;
            var years_x = {};
            var position = 4;
            var x_position = 160;
            data.forEach(function(d) {
                if (allValuesArray.indexOf(d[columns[1]]) === -1)
                    allValuesArray.push(d[columns[1]]);
            });
            var hgt = _this.height;
            var wid = _this.width;
            var total_slots = allValuesArray.length;
            var x_position = 160;
            allValuesArray.forEach(function(d, i) {
                if (i < 3) {
                    if (position === 0) {
                        position = 2;
                        mul = 1.5;
                    }
                    x_position = wid / position;
                    group_labels[i] = mul * x_position;
                    years_x[allValuesArray[i]] = group_labels[i];
                    position = position - 2;

                }
            });

            years_data = d3.keys(years_x);
            years = this.vis.selectAll(".years").data(years_data);
            return years.enter().append("text").attr("class", "years").attr("x", function(d) {
                return years_x[d];
            }).attr("y", 40).attr("text-anchor", "middle").text(function(d) {
                return d;
            });
        };

        BubbleChart.prototype.hide_years = function() {
            var years;
            return years = this.vis.selectAll(".years").remove();
        };
           
        BubbleChart.prototype.show_details = function(data, i, element) { 
            var content;
            if(columns== columns[0]){ 
                content = "<span class=\"name\">" + columns[0] + "</span><span class=\"value\"> " + data.name + "</span><br/>";
            }else{ 
                content = "<span class=\"name\">" + columns[0] + "</span><b>:</b><span class=\"value\"> " + data.name + "</span><br/>";
                content += "<span class=\"name\">" + columns[1] + "</span><b>:</b><span class=\"value\"> "+data.year+"</span><br/>";
            } 
            content += "<span class=\"name\">" + measureArray[0] + "</span><b>:</b><span class=\"value\"> " + addCommas(numberFormat(data.value,yAxisFormat,yAxisRounding,div))+ "</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        };
            
        BubbleChart.prototype.hide_details = function(data, i, element) {
            return tooltip.hideTooltip();
        };

        return BubbleChart;
    })();

    root = typeof exports !== "undefined" && exports !== null ? exports : this;
    var chart, render_vis,
    _this = this;
    chart = null;
    render_vis = function(csv) {
        chart = new BubbleChart(data);
        chart.start();
        return root.display_all();
    };
    root.display_all = function() {
        return chart.display_group_all();
    };
    root.display_year = function(splitby1) {
        splitby = splitby1;
        return chart.display_by_year(splitby1);
    };
    root.toggle_view = function(view_type, splitby) {
        if (view_type === 'year') {
            return root.display_year(splitby);
        }else {
            return root.display_all();
        }
    };
    render_vis();
} 


    

function buildScatterXY(div,divId, data, columns, measureArray, width, height,layout){
   var fun = "drillWithinchart(this.id,\""+div+"\")";
    var tempChartData=[];  // add for negtive data
    for(var i in data){
        var dataMap={};
        var keys1=Object.keys(data[i]);
        for(var k in keys1){
            dataMap[keys1[k]]=data[i][keys1[k]];
        }
        tempChartData.push(dataMap);
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    var absoluteValue = chartData[div]["absoluteValue"];
    if(typeof absoluteValue !=="undefined" && absoluteValue==="Yes"){
        data = getAbsoluteValue(data,columns,measureArray);
    }  // end for negtive data
    
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(measureArray.length<2){
        measureArray[1]= measureArray[0];
    }
    if(measureArray.length<3){
        measureArray[2]= measureArray[1];
    }
    var dashletid;
    var colName;
    var colIds;
    var div1=parent.$("#chartname").val()
    if(fromoneview!='null'&& fromoneview=='true'){
        var prop = graphProp(div1);
        dashletid=div;
        div=div1
        colIds=chartData[div1]["viewIds"];
        colName=chartData[div]["viewBys"];
    }else{
        colIds = chartData[div]["viewIds"];
        colName=chartData[div]["viewBys"];
        var prop = graphProp(div);
    }
//    var colIds = chartData["chart1"]["viewIds"];
    var chartDetails = chartData["chart1"];
    if(typeof chartDetails["rounding"]!=="undefined"){
        yAxisRounding=chartDetails["rounding"];
    }else{
        yAxisRounding=1;
    }
    if(typeof chartDetails["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartDetails["yAxisFormat"];
    }else{
        yAxisFormat="";
    }
    var margin = {
        top: 35,
        right: 10,
        bottom: 40,
        left: 80
    },
    width = width - margin.left - margin.right,
    height = height - margin.top - margin.bottom;

    var xValue = function(d){ 
        return d[measureArray[1]]; 
    },
    x = d3.scale.linear()
    .range([0, width*.98]),
    xMap = function(d,i) { 
        return x(xValue(d));
    },
    make_x_axis = function() {
        return d3.svg.gridaxis()
        .scale(x)
        .orient("bottom")
        },
         
    xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .tickFormat(function(d) {
        //        if(typeof displayX !=="undefined" && displayX =="Yes"){
        //            if(yAxisFormat==""){
        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
    //            }else{
    //                return numberFormat(d,yAxisFormat,yAxisRounding,div);
    //            }
    //        }else {
    //            return "";
    //        }
    });
    //    xAxis = d3.svg.axis()
    //    .scale(xScale)
    //    .orient("bottom");

// setup y
    var yValue = function(d){
        return d[measureArray[0]];
    },
    y = d3.scale.linear()
    .range([height*.98, 0]),
    yMap = function(d,i){
        return y(yValue(d));
    },
    make_y_axis = function() {
        return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
    },
    yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(function(d) {
        //        if(typeof displayY !=="undefined" && displayY =="Yes"){
        //            if(yAxisFormat==""){
        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
    //            }else{
    //                return numberFormat(d,yAxisFormat,yAxisRounding,div);
    //            }
    //        }else {
    //            return "";
    //        }
    });
    
    var zValue = function(d){ 
        return d[measureArray[2]];
    },
    zScale = d3.scale.linear()
    .range([height*.98, 0]),
    zMap = function(d) {
        return zScale(zValue(d));
    };
    
   var color = d3.scale.category10();

var svg = d3.select("#"+divId) .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
            .classed("svg-content-responsive", true)
  .append("g")
    .attr("transform", "translate(" + (margin.left*.7) + "," + (margin.top+3) + ")");

var tooltip = d3.select("#"+divId).append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

  data.forEach(function(d) {
      d[measureArray[2]] = +d[measureArray[2]]
    d[measureArray[1]] = +d[measureArray[1]];
    d[measureArray[0]] = +d[measureArray[0]];
  });

    x.domain([d3.min(data, xValue)-1, d3.max(data, xValue)+1]);
    y.domain([d3.min(data, yValue)-1, d3.max(data, yValue)+1]);

  if(typeof chartData[divId]["displayX"]!="undefined" && chartData[divId]["displayX"]!="" && chartData[divId]["displayX"]!="Yes"){}else{
    svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
    .append("text")
      .attr("class", "label")
      .attr("x", width)
      .attr("y", -6)
      .style("text-anchor", "end")
      .text(measureArray[1]);
}
  // y-axis
  if(typeof chartData[divId]["displayY"]!="undefined" && chartData[divId]["displayY"]!="" && chartData[divId]["displayY"]!="Yes"){}else{
  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("class", "label")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text(measureArray[0]);
}
    var max = maximumValue(data, measureArray[2]);
    var min = minimumValue(data, measureArray[2]);
    var temp = {};
    temp["min"] = min;
    temp["max"] = max; 
    svg.selectAll(".circle")
      .data(data)
    .enter().append("circle")
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
    .attr("r", function(d,i){
        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
        var radius = scale(data[i][measureArray[2]]);
        return radius
    })
    .attr("cx", xMap)
    .attr("cy", yMap)
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
}

    function buildTrendTableCombo(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
    var html="";
    html +="<div id='"+div+"_KPIDiv' style='float:left;margin-left:10px;margin-top:20px'>";
    html +="<table>"
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var i in measureArray){
        html +="<tr>"
        html += "<td>";
        html += "<div id='"+div+"_"+measureArray[i].replace(/ /g,'')+"' >";
        html += "</div>";
        html += "</td>";
        html +="</tr>"
    }
    html +="</table>"
    html +="</div>";
    html +="<div id='"+div+"_TrendDiv' style='width:"+(divWidth-(divWidth*0.2)-10)+"px;float:left'>";
    html +="</div>";
    html +="<div id='"+div+"_TableDiv' style='width:100%;float:right'>";
    html +="</div>";
    $("#"+div).html(html);
    for(i in measureArray){
        tileChartsCombo( div,data, chartData[div]["viewBys"], [measureArray[i]],divWidth*0.2,divHeight/3.2,KPIresult[i],div+"_"+measureArray[i].replace(/ /g,''),i);
    }
    var createBarLine={};
    for(i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
    buildTableTrendCombo_LineChart(div, data, columns, measureArray, divWidth-(divWidth*0.2), divHeight/2.2,"",div+"_TrendDiv");
    var data1={};
    data1[div]=data;
    buildTableComboChart(div,data1, data, chartData[div]["viewBys"], chartData[div]["meassures"],divWidth,divHeight-(divHeight/2.2)-10,KPIresult,div+"_TableDiv");
}


function buildVeractionCombo1(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var html="";
      html +="<div id='kpiDiv_"+div+"' style='width:26%;height:26%;float:right;margin-top:2%;'></div>";
      html +="<div id='"+div+"_lineDiv' style='width:100%;height:72%;float:right'></div>";
      $("#"+div).html("");
      $("#"+div).html(html)
    tileChartsCombo( "kpiDiv_"+div,data, chartData[div]["viewBys"],measureArray,divWidth*.22,divHeight*.24,KPIresult);
         var createBarLine={};
         for(i=0;i<measureArray.length;i++){
            createBarLine[measureArray[i]]= "Yes";
    }
      chartData[div]["createBarLine"]=createBarLine;
      parent.$("#chartData").val(JSON.stringify(chartData));
      var data1={};
      data1[div]=data;
      buildMultiMeasureTrLine(div, data, columns, measureArray, divWidth-(divWidth*0.15), divHeight/1.8,"",div+"_lineDiv");
}


function tileChartsCombo(div, data, columns, measureArray, divWidth, divHeight,KPIResult,divId,measIndex){
//    alert("1234bb")
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
if(typeof divId!='undefined' && divId!==''){
    appendDiv=divId;
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

                  if(yAxisFormat==""){
//                      if(yAxisRounding>0){
//                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//                      }
//                      else{
                            showData1 =      (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
//                      }

                    }
            else{
//             if(yAxisRounding>0){
//                            showData1 =      numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div);
//                      }
//                      else{
                            showData1 =      (numberFormatKPIChart(showData,yAxisFormat,yAxisRounding,div));
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
if(typeof chartData[div]["selectedMeasures"]==='undefined'){
    colorPicker=getDrawColor(div, measIndex);
}
else{
    measIndex=chartData[div]["selectedMeasures"].indexOf(measureArray[0]);
    if(measIndex==-1){
        colorPicker="gray";
    }
    else{
        colorPicker=getDrawColor(div, measIndex);
    }
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
if(typeof divId!='undefined' && divId!==''){
    gtFontSize='14';
    prefixFontSize='14';
    Suffixfontsize='14';
    infoFontSize='14';
}
//alert(gtFontSize);
//alert(prefixFontSize);
//alert(Suffixfontsize);
    parent.$("#chartData").val(JSON.stringify(chartData)); 
  var html = "";
   if(fromoneview!='null'&& fromoneview=='true'){
div=dashledid;
     }
  html+="<div id='mainDiv"+div+":"+measureArray[0]+"' style='width:95%;height:auto;  background-color:#FFF'>";
//  html+="<div  style='width:"+divWidth*.9582+"px;height:"+divHeight*.85+"px; background-color:"+colorPicker+"'>";
//  html+="<div  style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+colorPicker+"'>";
var backColor1='#FFF';
//if(chartData[div]["chartType"]==='Trend-Table-Combo'){
//        var selectedMeasures=chartData[div]["selectedMeasures"];
//        if(typeof selectedMeasures!=='undefined' && selectedMeasures.indexOf(measureArray[0])!==-1){
//            backColor1='gray';
//        }
//}
  html+="<div id='innerDiv"+div+":"+measureArray[0]+"' style='width:"+divWidth*.8+"px;height:"+divHeight*.4+"px; background-color:"+backColor1+"'>";
  html+="<table style='float:left;margin-top: '>";
  html+="<tr>";

 if(chartData[div]["chartType"]==='Combo-Analysis'){
if(suffix.trim()==""){
   html+="<td  style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}else{ 
   html+="<td   style='border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
} 
 } else if(chartData[div]["chartType"]==='Veraction-Combo1'){
if(suffix.trim()==""){
   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)' style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2)/2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
 }else{
   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)'  style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2)/2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}
}else{
if(suffix.trim()==""){
   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)' style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}else{
   html+="<td id='"+measureArray[0]+"' onclick='openTableDiv(\""+div+"\",this.id)'  style='cursor:pointer;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
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
      if(chartData[div]["chartType"]==='Veraction-Combo1'){
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+(infoFontSize+8)+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
  }else{
  html+="<td style='text-align: left;text-justify: inter-word;'><span style =' font-size:"+infoFontSize+"px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureAliaskpi+"</span></td>";
  }
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
openTableDiv = function(div,measureName){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var chartMeasures=chartData[div]["meassures"];
    var chartMeasureIds=chartData[div]["meassureIds"];
//    if(typeof chartData[div]["selectedMeasures"]==='undefined'){
    var selectedMesures=chartData[div]["selectedMeasures"];
    if(typeof selectedMesures==='undefined'){
        selectedMesures=[];
    }//
    if(typeof chartData[div]["kpiMultiSelect"]!=='undefined' && chartData[div]["kpiMultiSelect"]==="Yes"){
        if(selectedMesures.indexOf(measureName)==-1){
            selectedMesures.push(measureName);
        }
        else{
            selectedMesures.splice(selectedMesures.indexOf(measureName),1);
        }
    }
    else{
        selectedMesures=[];
        selectedMesures.push(measureName);
    }
    
    if(selectedMesures.length==0){
        for(i in chartMeasures){
            selectedMesures.push(chartMeasures[i]);
        }
    }
    
        chartData[div]["selectedMeasures"]=selectedMesures;
        
//        chartData[div]["selectedMeasures"].push(measureName);
//    }
//    try{
//    alert("Selected meassure:"+chartData[div]["selectedMeasures"]);
//}
//catch(e){}
    var tempMeasures=[];
    var tempMeasureIds=[];
    tempMeasures.push(measureName);
    tempMeasureIds.push(chartMeasureIds[chartMeasures.indexOf(measureName)]);
    for(var i in chartMeasures){
        if(chartMeasures[i]!==measureName){
            tempMeasures.push(chartMeasures[i]);
            tempMeasureIds.push(chartMeasureIds[chartMeasures.indexOf(chartMeasures[i])]);
        }
    }
    chartData[div]["meassures"]=tempMeasures;
    chartData[div]["meassureIds"]=tempMeasureIds;
    parent.$("#chartData").val(JSON.stringify(chartData));
////    alert(JSON.stringify({"data":data}));
//    var map1={};
//    map1[div]=chartData[div]["trendChartData"];;
//    var map2={};
//    map2["data"]=JSON.stringify(JSON.stringify(map1));
$.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
        function(data){
            var resultset = data

//                    var chartFlag = "false";
//                 var GTdiv = [];
//                 for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//                 chartFlag = "true"
//                 GTdiv.push(t)
//        }
//}
//            if(chartFlag!="false"){
//            var GTData = JSON.parse(data)
//            for(var k in GTdiv){
//            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
//            }
//            parent.$("#chartData").val(JSON.stringify(chartData));
//            }

               var chartData = JSON.parse($("#chartData").val());
            for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash"  ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){

                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                }
            }
//            $.ajax({
//            type:"POST",
//            data:parent.$("#graphForm").serialize(),
//            url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+$("#graphsId").val()+"&measId="+tempMeasureIds+"&aggType="+chartData["chart1"]["aggregation"]+"&measName="+JSON.stringify(encodeURIComponent(tempMeasures))+"&chartId=chart1",
//            //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
//            success: function(data){
//                alert(data)
//                //                                parent.$("#chartData").val(JSON.stringify(prevChartData));
//                chartData["chart1"]["GTValueList"] = JSON.parse(data);
//                parent.$("#chartData").val(JSON.stringify(chartData));
//            }
//        });
            generateSingleChart(resultset,div);
        });
//    var chartData=
//    $("#openTableTile").dialog({
//        autoOpen: false,
//        height: 440,
//        width: 620,
//        position: 'justify',
//        modal: true,
//        resizable:true,
//        title:'Data Table'
//    });
//    $("#openTableTile").html("");
//    tableData[div] = data[div];
//    buildTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
//    $("#openTableTile").dialog('open');
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
function trendComboDrill(idArr,div){
    var nextViewById='';
    var chartData = JSON.parse($("#chartData").val());
    chartData[div]["drillType"] = 'within';
    $("#drilltype").val('within');
    $("#chartData").val(JSON.stringify(chartData));
    drillWithinTable(idArr,div,'',nextViewById);
}
function trendComboDrillLine(idArr,div){
    var measureName=idArr.split("#")[1];
    var measureId='';
    var globalMeasures = JSON.parse(parent.$("#measure").val());
    var globalMeasureIds = JSON.parse(parent.$("#measureIds").val());
    measureId=globalMeasureIds[globalMeasures.indexOf(measureName)];
//    getPrior(div, measureNameId)
    var chartData = JSON.parse($("#chartData").val());
    chartData[div]["drillType"] = 'single';
//    chartData[div]["viewBys"] = 'Category';
//    chartData[div]["viewIds"] = '13917';
//    chartData[div]["dimensions"] = ["13917","TIME","191648","13912"];
    $("#drilltype").val('single');
    $("#chartData").val(JSON.stringify(chartData));
    drillWithinchart(idArr,div,'comboDrill','',measureId);
}

function buildTableTrendCombo_LineChart(div, data, columns, measureArray, divWid, divHgt, flag,divHtmlId) {

      //      added by shivam     
//        data=JSON.parse('[{"Category":"Computer Hardware","Gross Sales":"-100.0","Net Sales":"200.0","Discount Amount":"150.36"},{"Category":"Cameras","Gross Sales":"200.0","Net Sales":"250.0","Discount Amount":"250.3"},{"Category":"Mobile Phones","Gross Sales":"-300.0","Net Sales":"100.0","Discount Amount":"-300.81"}]')
var chartData = JSON.parse(parent.$("#chartData").val());
//try{
//    alert("S:"+chartData[div]["selectedMeasures"]);
//}
//catch(e){}
data=chartData[div]["trendChartData"];
if(chartData[div]["dimensions"].indexOf("TIME")!==-1){
    columns=[];
    columns.push("Time");
}
if(typeof chartData[div]["selectedMeasures"]!=='undefined'){
    measureArray=[];
    for(var i in chartData[div]["selectedMeasures"]){
        measureArray.push(chartData[div]["selectedMeasures"][i])
    }
}
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
//if (typeof data!== 'undefined' && data.length==1) {
//return buildMultiAxisBar(div, data, columns, measureArray, divWid, divHgt)
//
//}
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
var allViewByIds = JSON.parse(parent.$("#viewbyIds").val());
 var allViewByNames = JSON.parse(parent.$("#viewby").val());
  var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1
colIds=chartData[div1]["viewIds"];
}else{
       var prop = graphProp(div);
    colIds=[allViewByIds[allViewByNames.indexOf(Object.keys(data[0])[0])]];
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
            fun = "trendComboDrillLine(this.id,\""+div+"\")";
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
var min = parseFloat(minimumValue(data, measureArray[i]));
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
                        return addCommas(d);
                });

    }
    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
                     if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
     offset=0;
 }  
 var divId;
 if(typeof divHtmlId!='undefined' && divHtmlId!==''){
    divId=divHtmlId;
}
    var svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom )+" ")
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
var currY=0;
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
if(chartData[div]["chartType"]!=='Trend-Analysis-Chart'){
svg.append("foreignObject")
    .attr("width", 100)
    .attr("height", 20)
    .attr("x",width-100)
    .attr("y","-15px")
    .append("xhtml:body")
    .style("font", "14px 'Helvetica Neue'")
    .html(function(d) {
        if(typeof chartData[div]["kpiMultiSelect"]!=='undefined' && chartData[div]["kpiMultiSelect"]==='Yes'){
            return "<form><span>Multi Select</span><span style='padding: 5px'><input id='"+div+"multiSelect' onclick='toggleMultiSelect(this.id,\""+div+"\")' type=checkbox id=check class=tick/ checked></span></form>";
        }
        else{
            return "<form><span>Multi Select</span><span style='padding: 5px'><input id='"+div+"multiSelect' onclick='toggleMultiSelect(this.id,\""+div+"\")' type=checkbox id=check class=tick/></span></form>";
        }
    })
}
if(fromoneview!='null'&& fromoneview=='true'){
div=dashletid;
     }
    svg = d3.select("#" + div).select("g");
//    Added by shivam
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
            .attr("style","font-size:11px")
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
    .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "rotate(-85)";
        }else {
            return "rotate(-30)";
        }
    })      
    .style('text-anchor',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "middle";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "middle";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "end";
        }else {
            return "end";
        }
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
                    return "";
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

                            return addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
                }else {
                    return "";
                }
                    }else {
                        return addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
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
                            
                            return addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));

                        }else {
                            return "";
                        }
                    }else {
                        return addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
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
                        if(i==0){
                            radius = "8";
                        }
                        else{
                        radius = "4";
                        }
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
                        if(i==0){
                            colorShad = drillShade;
                        }
                        else{
                            colorShad = getDrawColor(div, parseInt(i));;
                        }
                    }else{
                   colorShad = getDrawColor(div, parseInt(i));
               }
               if (typeof chartData[div]["lineSymbol"] === "undefined" || chartData[div]["lineSymbol"] === "Hollow") {  
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        if(i==0){
                            return drillShade;
                        }
                        else{
                            return "white";
                        }
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
                    msrData = addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCommas(measureValue);
                }
            else{
                 msrData = addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
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
                    msrData = addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCommas(measureValue);
                }
            else{
                 msrData = addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
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
              .text(addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
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
    
       function buildTableComboChart(chartId,data, currdata, columns, measureArray, divwidth, divHght,KPIresult,divHtmlId) {
    var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
     var chartData = JSON.parse(parent.$("#chartData").val());
     var drillFunction='drillWithinchart'
     if(chartData[chartId]["chartType"]==='Trend-Table-Combo' || chartData[chartId]["chartType"]==='Trend-Analysis-Chart'){
         drillFunction='showViewByList';
     }
//  var colIds = chartData[chartId]["viewIds"];
  
//    Added by shivam
        if(typeof chartData[chartId]["Pattern"]!='undefined' && chartData[chartId]["Pattern"]==='Multi'){
         chartData[chartId]["Pattern"]="Normal";
     }
     parent.$("#chartData").val(JSON.stringify(chartData));
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
  dimensionId = chartData[div1]["dimensions"];
  div=chartId
  chartId=div1;
  dimensionId = chartData[div1]["dimensions"];
  var prop = graphProp(chartId);
}else{
    colIds = chartData[chartId]["viewIds"];
    div=chartId
    dimensionId = chartData[chartId]["dimensions"];
     var prop = graphProp(chartId);
}
//  alert(tabDivVal)
//       var prop = graphProp(chartId);
        var trWidth = divwidth / 8;
//   $("#pieWithMap").html("");
//if(data[tabDivVal]!== "undefined"){
//    chart12 = data[tabDivVal];
    columns = chartData[chartId]["viewBys"];
    
if(chartData[chartId]["chartType"]==='Influencers-Impact-Analysis2'){
    
}
else{
measureArray = chartData[chartId]["meassures"];
}
if(chartData[chartId]["chartType"]==='Trend-Table-Combo' || chartData[chartId]["chartType"]==='Trend-Analysis-Chart'){
        columns.splice(1,columns.length-1);
    }
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
   var divHght1=divHght;
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
if(chartData[chartId]["chartType"]==='Trend-Table-Combo' || chartData[chartId]["chartType"]==='Trend-Analysis-Chart'){
        viewBys.splice(1,viewBys.length-1);
    }        
//    alert(viewBys);
var htmlstr="";

// var html = "";
        if(data[chartId].length == 1){
        htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"height:"+divHght1+"px;overflow: hidden; \">";
        }else{
             htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"height:"+divHght1+"px; width:100%;overflow: hidden;\">";
        }
        htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;'>";
        if(data[chartId].length == 1 && chartData[chartId]["chartType"]!=='Trend-Table-Combo'){
        htmlstr += "<table id=\"chartTable" + div + "\" class='table table-condensed table-bordered ' width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: auto;border-collapse:collapse;font-size:10px;\">";
        var rowspan = columns.length;
             for(var a=0;a<viewBys.length;a++){
             htmlstr += "<tr style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black;white-space:nowrap;\">";
              htmlstr += "<th  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\"><strong>"+columns[a]+"</strong></th>"
      data[chartId].forEach(function(d, i) {


         htmlstr = htmlstr + "<th id='"+d[columns[a]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+div+"\",event)'  style=\"background:linear-gradient(to bottom, #8C8C8C , #E6E6E6 100%) repeat scroll 0 0 transparent; color:black\" width=" + trWidth + " >" + d[viewBys[a]] + "</th>";
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

          htmlstr += "<td >"+addCommas(d[measureArray[b]])+"</td>";

     });
        htmlstr +="</strong></td>"
         htmlstr += "</tr>";
        }
         htmlstr += "</table>";
        }//Transpose of Table
        else{

//htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;overflow-y:auto;overflow-x:none;margin-top:20px;float:right;width:100%;'>";
    if(typeof chartData[chartId]["tableBorder"]!=="undefined" && chartData[chartId]["tableBorder"]=="Y"){
        htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-stripped table-condensed table-bordered display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }else{
//        
   htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-stripped table-condensed display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }      
          
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
//htmlstr += "<th ></th>";

 if(typeof chartData[chartId]["tableWithSymbol"]!=="undefined" && chartData[chartId]["tableWithSymbol"]=="Y" && columns.length>1){
    try {
   htmlstr += "<th style='font-weight:bold;text-align:left' >";
   var columnsTxt='';
        for(var z=0;z<columns.length;z++){
            columnsTxt+=columns[z];
            if(z!==columns.length-1){
                columnsTxt +=",";
            }
    }
    htmlstr += columnsTxt+"</th>"
    } catch (e) {
 }
 }else {
    for(var q in columns){
htmlstr += "<th style='font-weight:bold;text-align:left' >"+columns[q]+"</th>";
} 
 }


for(var i in measureArray){  // add by mayank sh.
var measureName='';
    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAlias"][measureArray[i]];
    }else{
        measureName=measureArray[i];
    }
//htmlstr += "<th style='white-space:nowrap;font-weight:bold;font-size: 10px;text-align:left'>"+measureName+"</th>";
var nameAlign='left';
if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measureArray[i]] !== "undefined") {
    nameAlign=chartData[chartId]["measNameAlign"][measureArray[i]];
}
if(measureName.length > 15){
htmlstr += "<th title='"+measureName+"' style='white-space:nowrap;font-size: 10px;font-weight:bold;cursor:pointer;text-align:"+nameAlign+"'>"+measureName.substring(0,15)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;font-size: 10px;text-align:"+nameAlign+"'>"+measureName+"</th>";
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
            var color="";
//            if (num === 0) {
//                color = "white";
//            } else {
//                color = "white";
//            }
//              
//Added by shivam
 var colorfi  = getcolorValueFunction(chartId,chartData,drillShade,data[chartId],columns,measureArray,i,color) 
 
 var TableFontColor="";
 if(typeof chartData[chartId]["TableFontColor"]!=="undefined" && chartData[chartId]["TableFontColor"] !=""){
            TableFontColor=chartData[chartId]["TableFontColor"];
  color = TableFontColor;
    }
 
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            htmlstr = htmlstr + "<tr>"  ;
//            htmlstr += "<td  style=\"background-color:" + color + ";\" width=25px>"
//            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="#A9A9A5" /></svg>';
//            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              if(typeof chartData[chartId]["tableWithSymbol"]!=="undefined" && chartData[chartId]["tableWithSymbol"]=="Y" && columns.length>1){
                     try{
                         
                     if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1 && chartData[chartId]["chartType"]!=='Trend-Table-Combo') {
  htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+chartId+"\",event)'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + ">";
               }else{
  htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+chartId+"\",event)'  style=\"background-color:" + color + ";cursor:pointer;color:"+TableFontColor+"\" width=" + trWidth + " >";     
               }
   var columnsTxt1='';
        for(var k=0;k<columns.length;k++){
            columnsTxt1+=d[columns[k]];
            if(k!==columns.length-1){
                columnsTxt1 +="-";
            }
    }
    htmlstr += columnsTxt1+"</td>"
                     } catch (e) {
                     }
               } else{
                 
                 for(var l=0;l<viewBys.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1 && chartData[chartId]["chartType"]!=='Trend-Table-Combo') {

                           htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+chartId+"\",event)'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                    
                       if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){                
                      
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+chartId+"\",event)'  style=\"background-color:" + colorfi + ";cursor:pointer;color:"+TableFontColor+"\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
                   }else{
                        
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='"+drillFunction+"(this.id,\""+chartId+"\",event)'  style=\"background-color:;cursor:pointer;color:"+TableFontColor+"\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
               }
//                  var colorfi  = getcolorValueFunction(chartId,chartData,drillShade,data[chartId],columns,measureArray,i,color)
                    }  
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
            var valueAlign='left';
            if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measureArray[j]] !== "undefined") {
                valueAlign=chartData[chartId]["measValueAlign"][measureArray[j]];
            }
            if(yAxisRounding>0){
               if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){  
            if(d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"background-color:"+colorfi+";text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + 0 + "</td>";
            }
            else{
                htmlstr = htmlstr + "<td  style=\"background-color:"+colorfi+";text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }
            }else{
                if(d[measureArray[j]]===""){
                     htmlstr = htmlstr + "<td  style=\"background-color:;text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + 0 + "</td>";
                }
                else{
                  htmlstr = htmlstr + "<td  style=\"background-color:;text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
                } 
            }
            }
            else{
                 if(typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="" && chartData[div]["Pattern"]!="Normal"){  
            if(d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"background-color:" +colorfi+ ";text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + 0 + "</td>";
            }else{
                htmlstr = htmlstr + "<td  style=\"background-color:" +colorfi+ ";text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }
        }else{
            if(d[measureArray[j]]===""){
                htmlstr = htmlstr + "<td  style=\"background-color:;text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + 0 + "</td>";
            }else{
           htmlstr = htmlstr + "<td  style=\"background-color:;text-align:"+valueAlign+";color:"+TableFontColor+";font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }        
            }  
//            htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:"+valueAlign+";color:black;font-size:smaller\" width=" + trWidth + ">" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
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
        
          htmlstr += "<tr style='background-color:#D1D1D7;color:black'>";
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
         htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left\" width=" + trWidth + " >" +addCommas(KPIresult[no])+ "</td>";
          }
            htmlstr += "</tr>";
        }
        
            

htmlstr += "</table></div></td></tr></table>";
        }
//htmlstr += "</div>";
    if(typeof divHtmlId!=='undefined' && divHtmlId!==''){
        $("#" + divHtmlId).html(htmlstr);
    }
    else{
    
  if(chartIdTile==div+"Tile" ){
        $("#openTableTile").html(htmlstr)
    }else if($("#chartType").val()=="India-Map-Dashboard" || $("#chartType").val()=="India-Map-With-Trend"){
     $("#part3").html(htmlstr);
    }else{
        $("#" + div).html(htmlstr);
    }
    }
     var table = $('#chartTable'+div).dataTable( {
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": true,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
          "scrollY": ""+divHght *.90+"px"
//          "order": [[ 1, "desc" ]]  
          } );
//chartTable
 }
 
 function showViewByList(idArr,div,event){
    var allViewBys  = JSON.parse(parent.$("#viewby").val());
    var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
    var chartData=JSON.parse(parent.$("#chartData").val());
    var chartDims=chartData[div]["dimensions"];
    chartData[div]["drillType"] = 'within';
    $("#drilltype").val('within');
    $("#chartData").val(JSON.stringify(chartData));
    var html='<ul>';
    for(var i in chartDims){
        html+="<li id='"+chartDims[i]+"' style='padding:2px;font-size:9px;border-bottom: 0.1px inset;cursor:pointer' onclick='drillWithinTable(\""+idArr+"\",\""+div+"\",\"\",this.id)'>"+allViewBys[allViewIds.indexOf(chartDims[i])];
        html+="</li>";
    }
    html+='</ul>';
    $("#viewByList").html(html);
    $("#viewByList").toggle().css( {
        position:"absolute", 
        top:event.pageY+10, 
        left: event.pageX+20
    });
}

function toggleMultiSelect(id,chartId){
    var chartData=JSON.parse(parent.$("#chartData").val());
    if ($('#'+id).is(":checked"))
    {
        chartData[chartId]["kpiMultiSelect"]="Yes"
    }
    else{
        chartData[chartId]["kpiMultiSelect"]="No";
    }
    parent.$("#chartData").val(JSON.stringify(chartData));
}

    function buildTrendTableChart(div, data, columns, measureArray, divWidth, divHeight,KPIresult){
    var html="";
    html +="<div id='"+div+"_TrendDiv' style='width:60%;float:left'>";
    html +="</div>";
    html +="<div id='"+div+"_TableDiv' style='width:40%;float:right'>";
    html +="</div>";
    $("#"+div).html(html);
//    for(i in measureArray){
//        tileChartsCombo( div,data, chartData[div]["viewBys"], [measureArray[i]],divWidth*0.2,divHeight/3.2,KPIresult[i],div+"_"+measureArray[i].replace(/ /g,''),i);
//    }
    var createBarLine={};
    for(var i=0;i<measureArray.length;i++){
        createBarLine[measureArray[i]]= "Yes";
    }
    var chartData=JSON.parse(parent.$("#chartData").val());
    chartData[div]["createBarLine"]=createBarLine;
    parent.$("#chartData").val(JSON.stringify(chartData));
    buildTableTrendCombo_LineChart(div, data, columns, measureArray, divWidth*0.6-10, divHeight+(divHeight*0.15),"",div+"_TrendDiv");
    var data1={};
    data1[div]=data;
    showTopRecords(div, data, chartData[div]["viewBys"], chartData[div]["meassures"],divWidth*0.4-10, divHeight,div+"_TableDiv");
//    buildTableComboChart(div,data1, data, chartData[div]["viewBys"], chartData[div]["meassures"],divWidth*0.4-10, divHeight,KPIresult,div+"_TableDiv");
}

function showTopRecords(div,data, columns, measureArray, divWidth, divHeight,divHtmlId) {
    var html="";
    html="<ul id='viewBysList_" + div + "' class='dropdown-menu'></ul><div id='topRecords"+div+"' style='border:1px solid #cccccc;width:"+divWidth+"px;height:"+(divHeight*0.8)+"px'>";
    html+="<div style='cursor:pointer;text-align:left;margin-top:5px;font-size:12px;color:#6ac8e8;font-weight:bold;margin-left:10px;width:"+divWidth+"px;height:30px'>";
    var drilledvalue;
    try {
        var chartData = JSON.parse(parent.$("#chartData").val());
        drilledvalue = JSON.parse(parent.$("#drills").val());
    } catch (e) {
    }
    if (typeof drilledvalue !== 'undefined' && Object.keys(drilledvalue).length > 0) {
        globalData=data;
        var order="<span id='"+div+"_order' onclick='listOrders(\""+div+"\",this.id,event,"+divWidth+","+divHeight+")'>";
        if(typeof chartData[div]["topChartOrder"]==='undefined' ||  chartData[div]["topChartOrder"]==="top"){
            order += 'Top';
        }
        else{
            order += 'Bottom'
        }
        order += "</span>";
        html =html + order + " <span id='"+div+"_viewBy' onclick='listViewBys(\""+div+"\",this.id,event)'>"+columns[0] + "</span> in "+ drilledvalue[Object.keys(drilledvalue)[0]];
    }
    html+="</div>";
    graphProp(div);
    var tileHeight=0,tileWidth=0;
    if(data.length<4){
        tileWidth=100;
    }
    else{
        tileWidth=45;//box-shadow: 10px 10px 5px #888888
    }
    tileHeight=33;
    var color;
    color=getDrawColor(div,0);
    var beg=0,end=0;
    if(typeof chartData[div]["topChartOrder"]==='undefined' || chartData[div]["topChartOrder"]==='top'){
        if(data.length>6){
            end=5;
        }
        else{
            end=data.length-1;
        }
    }
    else{
        if(data.length>6){
            beg=data.length-6;
        }
        end=data.length-1;
    }
    for(var i=beg;i<=end;i++){
        html+="<div style='margin-left:7px;text-align:left;float:left;width:"+tileWidth+"%;height:"+tileHeight+"%'>";
        html+="<span style='font-size:19px;color:"+color+";width:100%;height:50%'>"+data[i][columns[0]];
        html+="</span></br>";
        html+="<span style='font-size:14px;color:grey;width:100%;height:50%'>"+addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));
        html+="</span>";
        html+="</div>";
    }
    html+="</div>";
    $("#"+divHtmlId).html(html);
}
function listViewBys(div,elId,event){
    var allViewBys  = JSON.parse(parent.$("#viewby").val());
    var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
    var chartData=JSON.parse(parent.$("#chartData").val());
    var chartDims=chartData[div]["dimensions"];
    var viewBy='';
    var data=chartData[div]["trendChartData"];
    var html='';
    for(var i in chartDims){
        viewBy=allViewBys[allViewIds.indexOf(chartDims[i])];
        if(viewBy===Object.keys(data[0])[0] || viewBy===chartData[div]["viewBys"][0]){
            continue;
        }
        html+="<li><a id='"+viewBy+":"+chartDims[i]+"' style='padding:2px;font-size:11px;border-bottom: 0.1px inset;cursor:pointer' onclick='changeViewBys(this.id,\""+div+"\",\"\",\"trendChart\")'>"+allViewBys[allViewIds.indexOf(chartDims[i])]+"</a>";
        html+="</li>";
    }
    if (html !== '') {
        var posX = $("#" + div).offset().left, posY = $("#" + div).offset().top;
        parent.$("#viewBysList_" + div).html(html);
        parent.$("#viewBysList_" + div).toggle();
        parent.$("#viewBysList_" + div).css("left", event.pageX - posX);
        parent.$("#viewBysList_" + div).css("top", event.pageY - posY + 20);
}
}
function listOrders(div,elId,event,wid,hgt){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var html='';
    if(typeof chartData[div]["topChartOrder"]==='undefined' || chartData[div]["topChartOrder"]==='top'){
        html+="<li><a id='top' style='background-color:#808080;padding:2px;font-size:11px;border-bottom: 0.1px inset;cursor:pointer' onclick='setOrder(this.id,\""+div+"\","+wid+","+hgt+")'>Top</a>";
        html+="<li><a id='bottom' style='padding:2px;font-size:11px;border-bottom: 0.1px inset;cursor:pointer' onclick='setOrder(this.id,\""+div+"\","+wid+","+hgt+")'>Bottom</a>";
    }
    else{
        html+="<li><a id='top' style='padding:2px;font-size:11px;border-bottom: 0.1px inset;cursor:pointer' onclick='setOrder(this.id,\""+div+"\","+wid+","+hgt+")'>Top</a>";
        html+="<li><a id='bottom' style='background-color:#808080;padding:2px;font-size:11px;border-bottom: 0.1px inset;cursor:pointer' onclick='setOrder(this.id,\""+div+"\","+wid+","+hgt+")'>Bottom</a>";
    }
    
    html+="</li>";
    
    if (html !== '') {
        var posX = $("#" + div).offset().left, posY = $("#" + div).offset().top;
        parent.$("#viewBysList_" + div).html(html);
        parent.$("#viewBysList_" + div).toggle();
        parent.$("#viewBysList_" + div).css("left", event.pageX - posX);
        parent.$("#viewBysList_" + div).css("top", event.pageY - posY + 20);
    }
}
function setOrder(id, div, wid, hgt) {
    var columns = [];
    var meassures = [];
    var chartData = JSON.parse(parent.$("#chartData").val());
    columns.push(chartData[div]["viewBys"][0]);
    for (var i in chartData[div]["meassures"]) {
        meassures.push(chartData[div]["meassures"][i]);
    }
    chartData[div]["topChartOrder"]=id;
    parent.$("#chartData").val(JSON.stringify(chartData));
    showTopRecords(div,globalData, columns, meassures, parseInt(wid), parseInt(hgt), div+"_TableDiv")
}
function buildScatterXYAnalysis(div,divId, data, columns, measureArray, width, height,gtValue){
    var chartData = JSON.parse(parent.$("#chartData").val());
   var fun = "drillWithinchart(this.id,\""+div+"\")";
    var tempChartData=[];  // add for negtive data
    for(var i in data){
        var dataMap={};
        var keys1=Object.keys(data[i]);
        for(var k in keys1){
            dataMap[keys1[k]]=data[i][keys1[k]];
        }
        tempChartData.push(dataMap);
    }
    alert("filters:"+JSON.stringify(chartData[div]["filters"]));
    alert("trend:"+JSON.stringify(chartData[div]["trendChartData"]));
    var mdata = [];
    var keys = [];
    var tempKeys1=[];
    var max;
    var colName = chartData[div]["viewBys"];
    var measure1 = measureArray[0];
    var groupedTotal = {};
    var indiviTotal = {};
    data.forEach(function(d, j) {
//        if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10) {
        if (keys.indexOf(d[columns[0]]) === -1 ) {
            keys.push(d[columns[0]]);
        }
        if (tempKeys1.indexOf(d[columns[1]]) === -1 ) {
            tempKeys1.push(d[columns[1]]);
        }
//        if (typeof groupedTotal[d[columns[0]]] === "undefined") {
//            groupedTotal[d[columns[0]]] = 0;
//        }
        if (typeof groupedTotal[d[colName[0]]] === "undefined") {
            groupedTotal[d[colName[0]]] = 0;
        }
        try {
//            groupedTotal[d[columns[0]]] = parseFloat(d[measureArray[0]]) + parseFloat(groupedTotal[d[columns[0]]]);
            groupedTotal[d[colName[0]]] = parseFloat(d[measureArray[0]]) + parseFloat(groupedTotal[d[colName[0]]]);
        } catch (e) {
        }
        if (typeof indiviTotal[d[colName[1]]] === "undefined") {
            indiviTotal[d[colName[1]]] = 0;
        }
//        if (typeof indiviTotal[d[columns[1]]] === "undefined") {
//            indiviTotal[d[columns[1]]] = 0;
//        }
        try {
//            indiviTotal[d[columns[1]]] = parseFloat(d[measureArray[0]]) + parseFloat(indiviTotal[d[columns[1]]]);
            indiviTotal[d[colName[1]]] = parseFloat(d[measureArray[0]]) + parseFloat(indiviTotal[d[colName[1]]]);
        } catch (e) {
        }
    });
//    var numberOfColumns = keys.length;
    for (var j = 0; j < keys.length; j++) {
       var data2 = {};
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
     
    var absoluteValue = chartData[div]["absoluteValue"];
    if(typeof absoluteValue !=="undefined" && absoluteValue==="Yes"){
        data = getAbsoluteValue(data,columns,measureArray);
    }  // end for negtive data
    var splitMap ={};
    alert(JSON.stringify(mdata))
    for(var z in mdata){
       var splitKeys = Object.keys(mdata[z]);
       var splitList =[];
       for(var x in splitKeys){
           if(x!=0)
           splitList.push(splitKeys[x]);
       }
       splitMap[mdata[z][splitKeys[0]]]=splitList;
        
    }     
//    var chartData = JSON.parse(parent.$("#chartData").val());
    if(measureArray.length<2){
        measureArray[1]= measureArray[0];
    }
    if(measureArray.length<3){
        measureArray[2]= measureArray[1];
    }
    var dashletid;
    var colName;
    var colIds;
    var div1=parent.$("#chartname").val()
    if(fromoneview!='null'&& fromoneview=='true'){
        var prop = graphProp(div1);
        dashletid=div;
        div=div1
        colIds=chartData[div1]["viewIds"];
        colName=chartData[div]["viewBys"];
    }else{
        colIds = chartData[div]["viewIds"];
        colName=chartData[div]["viewBys"];
        var prop = graphProp(div);
    }
//    var colIds = chartData["chart1"]["viewIds"];
    var chartDetails = chartData["chart1"];
    if(typeof chartDetails["rounding"]!=="undefined"){
        yAxisRounding=chartDetails["rounding"];
    }else{
        yAxisRounding=1;
    }
    if(typeof chartDetails["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartDetails["yAxisFormat"];
    }else{
        yAxisFormat="";
    }
    var margin = {
        top: 35,
        right: 10,
        bottom: 40,
        left: 80
    },
    width = width - margin.left - margin.right,
    height = height - margin.top - margin.bottom;

    var xValue = function(d){ 
        return d[measureArray[1]]; 
    },
    x = d3.scale.linear()
    .range([0, width*.98]),
    xMap = function(d,i) { 
        return x(xValue(d));
    },
    make_x_axis = function() {
        return d3.svg.gridaxis()
        .scale(x)
        .orient("bottom")
        },
         
    xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .tickFormat(function(d) {
        //        if(typeof displayX !=="undefined" && displayX =="Yes"){
        //            if(yAxisFormat==""){
        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
    //            }else{
    //                return numberFormat(d,yAxisFormat,yAxisRounding,div);
    //            }
    //        }else {
    //            return "";
    //        }
    });
    //    xAxis = d3.svg.axis()
    //    .scale(xScale)
    //    .orient("bottom");

// setup y
    var yValue = function(d){
        return d[measureArray[0]];
    },
    y = d3.scale.linear()
    .range([height*.98, 0]),
    yMap = function(d,i){
        return y(yValue(d));
    },
    make_y_axis = function() {
        return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
    },
    yAxis = d3.svg.axis()
    .scale(y)
    .orient("left")
    .tickFormat(function(d) {
        //        if(typeof displayY !=="undefined" && displayY =="Yes"){
        //            if(yAxisFormat==""){
        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
    //            }else{
    //                return numberFormat(d,yAxisFormat,yAxisRounding,div);
    //            }
    //        }else {
    //            return "";
    //        }
    });
    
    var zValue = function(d){ 
        return d[measureArray[2]];
    },
    zScale = d3.scale.linear()
    .range([height*.98, 0]),
    zMap = function(d) {
        return zScale(zValue(d));
    };
    
   var color = d3.scale.category10();

var svg = d3.select("#"+divId) .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
            .classed("svg-content-responsive", true)
  .append("g")
    .attr("transform", "translate(" + (margin.left*.7) + "," + (margin.top+3) + ")");

var tooltip = d3.select("#"+divId).append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

  data.forEach(function(d) {
      d[measureArray[2]] = +d[measureArray[2]]
    d[measureArray[1]] = +d[measureArray[1]];
    d[measureArray[0]] = +d[measureArray[0]];
  });
//	if(div=="chart1"){
//		x.domain([-25,5]);
//    y.domain([35000,200000]);
//	}else if(div=="chart2"){
//	x.domain([5,30]);
//    y.domain([35000,200000]);	
//	}else if(div=="chart3"){
//		x.domain([5,30]);
//    y.domain([0,35000]);
//	}else if(div=="chart4"){
//		x.domain([-25,5]);
//    y.domain([0,35000]);
//	}else{
    x.domain([d3.min(data, xValue)-1, d3.max(data, xValue)+1]);
    y.domain([d3.min(data, yValue)-1, d3.max(data, yValue)+1]);
//	}

  if(typeof chartData[divId]["displayX"]!="undefined" && chartData[divId]["displayX"]!="" && chartData[divId]["displayX"]!="Yes"){}else{
    svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
    .append("text")
      .attr("class", "label")
      .attr("x", width)
      .attr("y", -6)
      .style("text-anchor", "end")
      .text(measureArray[1]);
}
  // y-axis
  if(typeof chartData[divId]["displayY"]!="undefined" && chartData[divId]["displayY"]!="" && chartData[divId]["displayY"]!="Yes"){}else{
  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
    .append("text")
      .attr("class", "label")
      .attr("transform", "rotate(-90)")
      .attr("y", 6)
      .attr("dy", ".71em")
      .style("text-anchor", "end")
      .text(measureArray[0]);
}
    var max = maximumValue(data, measureArray[2]);
    var min = minimumValue(data, measureArray[2]);
    var temp = {};
    var tempKeys=Object.keys(splitMap);
    temp["min"] = min;
    temp["max"] = max; 
    alert("keyssssss: "+keys)
    alert(tempKeys)
     alert(tempKeys1)
    svg.selectAll(".circle")
      .data(data)
    .enter().append("circle")
    .filter(function(d){
        return d[columns[0]]===tempKeys[0];
    })
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
    .attr("r", function(d,i){
        
//        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
//        var radius = scale(data[i][measureArray[2]]);
        return 6;
    })
    .attr("cx", xMap)
    .attr("cy", yMap)
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
    //-------------------------------//
    if(typeof tempKeys!=="undefined" && typeof tempKeys[1]!=="undefined"){
    svg.selectAll(".cir")
      .data(data)
    .enter().append("rect")
    .filter(function(d){
        return d[columns[0]]===tempKeys[1];
    })
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("width","10").attr("height","10")
//    .attr("r", function(d,i){
////        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
////        var radius = scale(data[i][measureArray[2]]);
//        return 10;
//    })
    .attr("x", xMap)
    .attr("y", yMap)
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
    }
    //-------------------------------------//
 if(typeof tempKeys !=="undefined" && typeof tempKeys[2] !=="undefined"){  
   
    svg.selectAll(".cir")
      .data(data)
    .enter().append("ellipse")
    .filter(function(d){
        return d[columns[0]]===tempKeys[2];
    })
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("width","10").attr("height","10")
//    .attr("r", function(d,i){
////        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
////        var radius = scale(data[i][measureArray[2]]);
//        return 10;
//    })
    .attr("cx", xMap)
    .attr("cy", yMap).attr("rx","8").attr("ry","4")
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
}
    //-----------------------------------------------//
    if(typeof tempKeys !=="undefined" && typeof tempKeys[3] !=="undefined"){
     svg.selectAll(".cir")
      .data(data)
    .enter().append("path")
    .filter(function(d){
        return d[columns[0]]===tempKeys[3];
    })
            .attr("d",d3.svg.symbol().size(70).type(function(d){
//                if(d[columns[0]]===tempKeys[3]){
                    return "cross"
//                }else if(d[columns[0]]===tempKeys[4]){
//                    return "diamond";
//                }else{
//                    if(d[columns[0]]!==tempKeys[0] && d[columns[0]]!==tempKeys[1] && d[columns[0]]!==tempKeys[2])
//                    return "triangle-up"
//                }
                        }))
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("width","10").attr("height","10")
//    .attr("r", function(d,i){
////        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
////        var radius = scale(data[i][measureArray[2]]);
//        return 10;
//    })
    .attr("transform", function(d,i) {
                return "translate(" + x(xValue(d)) + "," + y(yValue(d)) + ")"; })
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
}
    
    //----------------------------------------//
    if(typeof tempKeys !=="undefined" && typeof tempKeys[4] !=="undefined"){
    svg.selectAll(".cir")
      .data(data)
    .enter().append("path")
    .filter(function(d){
        return d[columns[0]]===tempKeys[4];
    })
            .attr("d",d3.svg.symbol().size(70).type(function(d){
//                if(d[columns[0]]===tempKeys[3]){
                    return "diamond"
//                }else if(d[columns[0]]===tempKeys[4]){
//                    return "diamond";
//                }else{
//                    if(d[columns[0]]!==tempKeys[0] && d[columns[0]]!==tempKeys[1] && d[columns[0]]!==tempKeys[2])
//                    return "triangle-up"
//                }
                        }))
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("width","10").attr("height","10")
//    .attr("r", function(d,i){
////        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
////        var radius = scale(data[i][measureArray[2]]);
//        return 10;
//    })
    .attr("transform", function(d,i) {
                return "translate(" + x(xValue(d)) + "," + y(yValue(d)) + ")"; })
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
}
    //==============================//
    if(typeof tempKeys !=="undefined" && typeof tempKeys[5] !=="undefined"){
    svg.selectAll(".cir")
      .data(data)
    .enter().append("path")
    .filter(function(d){
        return d[columns[0]]===tempKeys[5];
    })
            .attr("d",d3.svg.symbol().size(70).type(function(d){
//                if(d[columns[0]]===tempKeys[3]){
                    return "triangle-up"
//                }else if(d[columns[0]]===tempKeys[4]){
//                    return "diamond";
//                }else{
//                    if(d[columns[0]]!==tempKeys[0] && d[columns[0]]!==tempKeys[1] && d[columns[0]]!==tempKeys[2])
//                    return "triangle-up"
//                }
                        }))
      .attr("index_value", function(d, i) {
                return "index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            }).attr("width","10").attr("height","10")
//    .attr("r", function(d,i){
////        var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["38", "12"]);
////        var radius = scale(data[i][measureArray[2]]);
//        return 10;
//    })
    .attr("transform", function(d,i) {
                return "translate(" + x(xValue(d)) + "," + y(yValue(d)) + ")"; })
    .attr("opacity",".6")
            .attr("fill", function(d,i) {
        var drilledvalue;
        try {
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
            }
//            alert("fill: "+JSON.stringify(d))
            
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color)
            return colorfill;
        }
      })
    .attr("color_value", function(d,i) {
var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            drillClick = drilledvalue;
                } catch (e) {
                }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[i][columns[0]]) !== -1) {
            return drillShade;
        }else{
            if(tempChartData[i][measureArray[2]]<0){  // add
                return "#FF4C4C"; 
                    }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,tempKeys1.indexOf(d[columns[1]]),color);
            return colorfill;
        }
})

    .attr("id",function(d,i) {
        return d[columns[0]]+":"+d[measureArray[0]];
    })
      .on("mouseover", function(d,i) {
          var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.attr("fill", drillShade);
          show_details2(d, columns, measureArray, this,div);
                
      })
      .on("mouseout", function(d,i) {
         if(fromoneview!='null'&& fromoneview=='true'){
                     }else{
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.attr("fill", colorValue);
                     }
         hide_details(d, i, this);
    })
    .attr("onclick", fun);
    }
//    svg.append("g")
//.append("rect")
//           .attr("style","margin-right:10")
//            .attr("style", "overflow:scroll")
//
////            .attr("x",rectlen)
////            .attr("y",rectyvalue)
//            .style("stroke", "grey")
//            .style("stroke-dasharray", ("3, 4"))
////            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
//            .attr("x", width*.10)
//            .attr("y", -height*.02)
//            .attr("width", (100))
//            .attr("height", 20)
//            .attr("rx", 10)         // set the x corner curve radius
//            .attr("ry", 10)
//            .attr("fill", "#d1d1d1");
    svg.append("g")
        .append("text")
            .attr("transform", "translate(" + (width*.11)  + "," + height*.02 + ")")
            .attr("style","font-size:10px")
            .style("font-size","11px")
    .style("font-family","helvetica")
            .attr("fill","black")
            .text(function (d) {
                var change = parseFloat(gtValue[2]) - parseFloat(gtValue[5]);
//                var change = "h";
                return measureArray[2] + ": " + addCommas(numberFormat(gtValue[2], yAxisFormat, yAxisRounding, div)) + " / " + addCommas(numberFormat(gtValue[5], yAxisFormat, yAxisRounding, div))+ " (" +addCommas(numberFormat(change, yAxisFormat, yAxisRounding, div))+" )";
            });
}





function buildTopAnalysis1(div, data, columns, measureArray, divWidth, divHeight,KPIresult,drillReportType,viewByIconMap,drillId) {
var appendDiv = "";
if(div.indexOf("Top1Div_")!=-1 ||div.indexOf("Top2Div_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}
    var chartData = JSON.parse(parent.$("#chartData").val());
    graphProp(div);
	
    var noOfRecords=chartData[div]["records"];
    var dataArray = [];
    var fontSize1=0;
    var fontSize2=0;
    var fontSize3=0;
    var fontSize4=0;
    var prefixFontSize=0;
    var Suffixfontsize=0;
    var measureColor="";
    var colorPicker="";
    var combofontSize1=$("#div"+div).height()/13;
    combofontSize1=combofontSize1>18?18:combofontSize1;
    var combofontSize2=combofontSize2/1.8;
    var columnsIds;
     columnsIds=chartData[div]["viewIds"];
    
    if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
        fontSize1= chartData[div]["kpiGTFont"];
    }else{
         fontSize1=$("#div"+div).height()/13;
    fontSize1=fontSize1>18?18:fontSize1;
    }
    chartData[div]["kpiGTFont"]=fontSize1;
    
    if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
        fontSize2= chartData[div]["kpiSubData"];
    }else{
         fontSize2=$("#div"+div).height()/13;
    fontSize2=fontSize2>18?18:fontSize2;
    }
    chartData[div]["kpiSubData"]=fontSize2;
    
    if(typeof chartData[div]["kpiFont"]!="undefined" && chartData[div]["kpiFont"] !== '' && chartData[div]["kpiFont"] !=="4"){
        fontSize3= chartData[div]["kpiFont"];
    }else{
         fontSize3=$("#div"+div).height()/13;
    fontSize3=fontSize3>12?12:fontSize3;
    }
    chartData[div]["kpiFont"]=fontSize3;
    
    if(typeof chartData[div]["comparativeValue"]!="undefined" && chartData[div]["comparativeValue"] !== '' && chartData[div]["comparativeValue"] !=="4"){
        fontSize4= chartData[div]["comparativeValue"];
    }else{  
         fontSize4=$("#div"+div).height()/13;
    fontSize4=fontSize4>18?18:fontSize4;
    }
    chartData[div]["comparativeValue"]=fontSize4;
    
    if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[div]["Prefixfontsize"];
    }else{
    prefixFontSize = $("#div"+div).height()/13;
    prefixFontSize=prefixFontSize>18?18:prefixFontSize;
    }
    
    if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
    }else{
    Suffixfontsize = $("#div"+div).height()/13;
    Suffixfontsize=Suffixfontsize>18?18:Suffixfontsize;
    }
    
    if(typeof chartData[div]["colorPicker"]!="undefined" && chartData[div]["colorPicker"]!=""){
    colorPicker = chartData[div]["colorPicker"];
    }else{
    colorPicker = "#696969";
    }
    
    if(typeof chartData[div]["lFilledFont"]!="undefined" && chartData[div]["lFilledFont"]!=""){
    measureColor = chartData[div]["lFilledFont"];
    }else{
    measureColor = "#545252";
    } 
    
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
    if(typeof chartData[div]["fontSize1"]!=='undefined' && chartData[div]["fontSize1"]!==''){
        combofontSize1=chartData[div]["fontSize1"];
        combofontSize2=combofontSize1/1.8;
    }
   
    $("#chartData").val(JSON.stringify(chartData));
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    
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
        yAxisRounding =2;
        chartData[div]["rounding"]="2";
        parent.$("#chartData").val(JSON.stringify(chartData));
    }

    DataSum_measureArray=[];
    graphProp(div);
    
    var measure2=[];
    var comparison=''
    if(typeof chartData[div]["enableComparison"]!=='undefined' && chartData[div]["enableComparison"]==='true' && typeof measureArray[1]!='undefined'){
        measure2=measureArray[1].split(" ");
    comparison=measure2[measure2.length-1]
    comparison =" | "+comparison;
    }
    var prefix='',suffix='';
    if(typeof chartData[div]["Prefix"]!=='undefined' && chartData[div]["Prefix"]!=='undefined' && chartData[div]["Prefix"].trim()!==''){
        prefix=chartData[div]["Prefix"];
    }
    if(typeof chartData[div]["Suffix"]!=='undefined' && chartData[div]["Suffix"]!=='undefined' && chartData[div]["Suffix"].trim()!==''){
        suffix=chartData[div]["Suffix"];
    }
    var htmlstr = "";
    var measureCount=3;
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashledid
    }
    var labelName='';
    labelName += measureArray[0]+" by Top ";
    var labelName1 = columns[0];
    var titleFontSize='12';
    if(typeof chartData[div]["titleFontSize"]!='undefined' && chartData[div]["titleFontSize"]!=''){
        titleFontSize=chartData[div]["titleFontSize"];
    }
    var titleColor='#6AC8E8';
    if(typeof chartData[div]["titleColor"]!=="undefined" && chartData[div]["titleColor"] !=""){
        titleColor=chartData[div]["titleColor"];
    }
//    var order='';
//     var drilledvalue;
//    try {
//        var chartData = JSON.parse(parent.$("#chartData").val());
//        drilledvalue = JSON.parse(parent.$("#drills").val());
//    } catch (e) {
//    }
//   if (typeof drilledvalue !== 'undefined' && Object.keys(drilledvalue).length > 0) {
//        globalData=data;
//        if(typeof chartData[div]["topChartOrder"]==='undefined' ||  chartData[div]["topChartOrder"]==="top"){
//            order += 'Top';
//        }else{
//            order += 'Bottom'
//        }
//    }
    if(typeof KPIresult!='undefined' && KPIresult==='worldMap'){
        htmlstr +="<div class='maindiv' style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;padding-top:8px;width:80%;margin-left:50px;color:height:auto;text-align:left;float:top;color: #808080;font-weight:bold'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+(fontSize1+2)+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'></span></div>";
    }
    else{
        htmlstr +="<div id='viewBysList_" + div + "'  style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px;margin-top:7px;margin-botton:6px;text-align:left'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;padding-top:8px;width:80%;color:height:auto;text-align:left;float:top;color: "+titleColor+";font-weight:bold;margin-left:13px'><span id='"+div+"_order'  style = 'line-height: -moz-block-height;font-weight:bold;cursor:pointer;font-size:"+titleFontSize+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'  onclick='listOrders(\""+div+"\",this.id,event,"+divWidth+","+divHeight+")'>"+labelName+"</span><span id='"+div+"_viewBy'  style = 'line-height: -moz-block-height;font-weight:bold;cursor:pointer;font-size:"+titleFontSize+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;' onclick='listViewBys(\""+div+"\",this.id,event)' >"+labelName1+"</span></div>";
    }
    var noOfRecords=chartData[div]["records"];
    if(noOfRecords<data.length){
        rowCount=noOfRecords<3?noOfRecords:3;
    }
    else{
        rowCount=data.length<3?data.length:3;
    }
        
        wDiv=parseInt((measureCount)/rowCount);
        tileWidth=$("#"+appendDiv).width()/wDiv-9;
        tileHeight=($("#"+appendDiv).height()-25)/(measureCount+0.3)-10;
		  var dataRecordsSize = data.length
                  var dataCounter=0;
        
        for(var i=0;i<(dataRecordsSize<3 ? dataRecordsSize:3);i++){
            for(var j=0;j<Math.ceil((data.length<noOfRecords?data.length:noOfRecords)/3);j++){
                var upArrow='&nbsp;&#65514;'
                var downArrow='&nbsp;&#65516;'
                var arrow=''
                var change=0;
                if(dataCounter>=(data.length<noOfRecords?data.length:noOfRecords)){
                     continue;
                }
                if(data[dataCounter][measureArray[0]]>data[dataCounter][measureArray[1]]){
                    arrow=upArrow;
                }
                else{
                    arrow=downArrow;
                }
                var changePercent=0;
                var current=parseInt(data[dataCounter][measureArray[0]]);
                var prior=parseInt(data[dataCounter][measureArray[1]]);
                changePercent=(current-prior)/(prior)*100;
                changePercent=changePercent.toFixed(1);
                var changePercentHtml='';
                if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                if(typeof chartData[div]["changPercentArrow"]!="undefined" && chartData[div]["changPercentArrow"]!="" && chartData[div]["changPercentArrow"]!="Without-Arrow"){
                    changePercentHtml=arrow+"("+changePercent+"%)";
                   }else{  
                       changePercentHtml= ''; 
                }
                }
                if(chartData[div]["chartType"]==="World-Top-Analysis" || chartData[div]["chartType"]==="Combo-Analysis"){
                    changePercentHtml = '';
                }
                changePercentHtml= '';
                var dataIndex=j*3+i+1;
                if(dataIndex>data.length){
                    continue;
                }
        var padding=0;
    
        if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true' && typeof measureArray[1]!='undefined'){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                padding=$("#"+appendDiv).height()*0.03;
                    padding=i==0?(padding-4):padding;
        }
        else{
                padding=$("#"+appendDiv).height()*0.05;
                    padding=i==0?(padding-8):padding;
        }
        }
        else{
                padding=$("#"+appendDiv).height()*0.05;
                padding=i==0?(padding-12):padding;            
        }
                var data1=data[dataCounter][measureArray[0]],data2;
                if(typeof data[dataCounter][measureArray[1]]==='undefined'){
                data2=data1;
            }
            else{
                    data2=data[dataCounter][measureArray[1]];
            }
            if(chartData[div]["chartType"]==='Combo-Analysis'){
            var ctxPath=parent.document.getElementById("h").value;
            var iconPath='';
            if(typeof viewByIconMap[data[dataCounter][columns[0]]]!=='undefined'){
                iconPath="<img src='"+ctxPath+"/images/"+viewByIconMap[data[dataCounter][columns[0]]]+"' width='35px' height='25px' style='margin-top:5px;float:left'>"
            } 
	    else{
	    	iconPath="<img src='"+ctxPath+"/images/ViewByIcons/Blank.png' width='35px' height='25px' style='margin-top:5px;float:left'>"

	    } 	  

             var dataLength=0;
             if(noOfRecords<data.length){
                 dataLength=noOfRecords;
             }
             else{
                 dataLength=data.length;
             }
            if(suffix.trim()==""){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                        if(data.length==1){
                            tileWidth=tileWidth*2.5;
                        }
                        htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom; color:grey;'><span>"+data[dataCounter][columns[0]]+"</span></div>";
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                    htmlstr += "<div style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:grey'>("+prefix+ addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")</div></div></div>";
                }else{
                    htmlstr += "</div></div>";    
                }
                }else{
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                        if(data.length==1){
                            tileWidth=tileWidth*2.5;
                        }
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom; color:grey;'><span style='white-space: nowrap;' >"+data[dataCounter][columns[0]]+" | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0])+ addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")"+"</span></div></div></div>";
            }else{
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom; color:grey'><span style='white-space: nowrap;' >"+data[dataCounter][columns[0]]+"</span></div></div></div>";
            }}}else{
                    htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:100%;height: 80%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:grey'><span  style='white-space: nowrap;'>"+data[dataCounter][columns[0]]+" | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+comparison+")"+"</span></div></div></div>";
            }
            }else{
            if(suffix.trim()==""){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                        htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span  style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom;color:"+measureColor+"' ><span >"+data[dataCounter][columns[0]]+"</span></div>";
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                    htmlstr += "<div style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+fontSize4+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+";'>("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+suffix+comparison+")</div></div></div>";
                }
                else{
                    htmlstr += "</div></div>";    
                    }
                }
                else{
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space:nowrap;cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"' ><span  style='white-space: nowrap;' >"+data[dataCounter][columns[0]]+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px' > | ("+prefix+ addCurrencyType(div, chartData[div]["meassureIds"]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+suffix+comparison+")</span></div></div></div>";
            }
                    else{
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div class='measName' id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"' ><span >"+data[dataCounter][columns[0]]+"</span></div></div></div>";
            }
            }
            }
            else{
                    htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+divWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div style='white-space:nowrap;background-color:#fff;font-style:bold;cursor:pointer;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'>"+data[dataCounter][columns[0]]+"<span style='color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + (numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+suffix+comparison+")</span></div></div></div>";
            }
            }
            dataCounter++;
        }
        }
    $("#"+appendDiv).append(htmlstr);
    if($(".imgdiv").width() > $(".imgdiv").height()){
        $(".imgdiv>img").height($(".imgdiv").height()-10);
    }
    else{
        $(".imgdiv>img").width($(".imgdiv").width());
    }
    $(".imgdiv").mouseenter(function(){
        $(this).children().children().children().show();
    });
    $(".imgdiv").mouseleave(function(){
        $(this).children().children().children().hide();
    });

}
