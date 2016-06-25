function buildBar(div, data, columns, measureArray,wid,hgt,divAppend) {
 var customTicks = 5;
// data=JSON.parse('[{"Category":"Computer Hardware","Gross Sales":"1000"},{"Category":"Cameras","Gross Sales":"3000"},{"Category":"Mobile Phones","Gross Sales":"-2000"},{"Category":"Footwear","Gross Sales":"-4000"},{"Category":"Clothing","Gross Sales":"2000"},{"Category":"Watches","Gross Sales":"-1500"}]');
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
var divWid=wid-30;
var chartData = JSON.parse(parent.$("#chartData").val());
var div1=parent.$("#chartname").val()
   var divHgt=hgt;
   var dashletid=div;
     var fromoneview=parent.$("#fromoneview").val()
var colIds= [];
if(fromoneview!='null'&& fromoneview=='true'){
    colIds = chartData[div1]["viewIds"];
    divHgt=hgt;
    divWid=wid;
    div=div1;
}else{
    colIds = chartData[div]["viewIds"];

 }
 if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
//       hgt -=25;
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
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var prop = graphProp(div);
}
    var autoRounding1;

        autoRounding1 = "1d";

//    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
var regid=dashletid.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
 var olap=dashletid.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
    }

if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
//add condition by mayank sh. for hidden white space
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

   var width = divWid;
    var height = 0;
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
            height = divHgt*.78 ; //- margin.top - margin.bottom
}else{
            height = divHgt *.73; //- margin.top - margin.bottom

}  //end condition by mayank sh. for hidden white space


    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
//          Added by shivam  
            
    var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.1;
    }
   var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range, range);
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
    var xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");
}else{
   var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
}
    var    yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });

        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
//                 .ticks(percent)
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
    var offset=0;        
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
       offset=25;
    }        
    var svg = d3.select("#" + dashletid)
            .append("svg")
            .attr("id", "svg_" + dashletid)
             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+65)+" ")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");

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
//                return colorShad;
            })
            .attr("stop-opacity", 1);

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    var multiple=0.8;
    if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
        multiple=1;
    }
    var max = 0;
    if(fromoneview!='null'&& fromoneview=='true'){
  max = maximumValue(data, measure1);
  if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
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
    max = maximumValue(data, measure1);
}}else{
    max = maximumValue(data, measure1);
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
   minVal = 0;
 }else{
    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }}
}else{
          if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }else{
        minVal = 0;
       }
    }
    }

    y.domain([parseFloat(minVal), parseFloat(max)]);
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayYLine"]==="undefined" || chartData[div]["displayYLine"]==="" || chartData[div]["displayYLine"]==="Yes"){  
  svg.append("line")
        .attr("x1",0)
        .attr("y1",0)
        .attr("x2",0)
        .attr("y2",(divHgt*.75))
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
            if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height*1) + ")")
            .call(xAxis)
            .selectAll('text')
    .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal"){
            return  3;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return -5;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            if(parseInt(i % 2) ==0){
            return  10;
            }else{
            return 30;   
        }
        } else if(chartData[div]["legendPrintType"] === "Horizontal") {
           return  10; 
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 2;
        }else {
            return -2;
        }
    })
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i);
            })
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .attr('transform',function(d,i) {
                return transformation(div,d,i);
    })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
    })
    .append("svg:title")
    .text(function(d) {
        return d;
    });
            }
    svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            .attr("y", 0)
            .attr("x", -9)
            .attr("dy", ".32em")
            .style("text-anchor", "end");

   //add by mayank sh.(08/6/15) for targt line
   var target = "";
              if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
              target = chartData[div]["targetLine"];
               svg.append("text")
              .text(target)
              .attr("x", (width)*0.88)
              .attr("y", y(parseInt(target))-12)
               .attr("style","font-size:8px");
               
              svg.append("text")
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(target))-5)
               .attr("style","font-size:8px")
               .text("("+measureArray[0]+")");
            } //end by mayank sh.(08/6/15) for targt line
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
    var maxHgt=0;
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
                            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[columns[0]]) !== -1) {
                        return drillShade;
                    }

			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
               targetLine = "";
                return colorfill;
			
                      
//                       return getDrawColor(div, parseInt(i));
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

			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			
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

//                    return color(0);
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
                if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
                    if(y(0) > y(d[measure1]))
                return y(d[measure1]);
                    else
                        return y(0);
                }
                else{
                    return y(d[measure1]);
                }
            })
            .attr("height", function(d) {
                if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
                    if(Math.abs(y(0) - y(d[measure1]))>maxHgt){
                        maxHgt=Math.abs(y(0) - y(d[measure1]));
                    }
                    return Math.abs(y(0) - y(d[measure1]));
                    
                }else{
                    if(height - y(d[measure1])>maxHgt){
                        maxHgt=height - y(d[measure1]);
                }
                return height - y(d[measure1]);
                }
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
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
//                    var colorValue = prevColor;
                    selectedBar.style("fill", colorValue);
                }
//                }
                hide_details(d, i, this);
            });
             //target Line
  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==''){
 var path =   svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
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
  
 
   for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
              .text(addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
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

//         svg.append("text")
//              .text(avg)
//              .attr("x", (width)*0.95)
//              .attr("y", y(parseInt(avg))-5)
//               alert(avg) 
//var totalLength = path.node().getTotalLength();
//
//    path
//      .attr("stroke-dasharray", totalLength + " " + totalLength)
//      .attr("stroke-dashoffset", totalLength)
//      .transition()
//        .duration(2000)
//        .ease("linear")
//        .attr("stroke-dashoffset", 0);
//});
//    }
   
        
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

            svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                     return "translate(" + (xvalue) + ", " + (height-4) + ")rotate(" + -90 + ")";// add by mayank sh. width chang  for text size
                })
//                .attr("text-anchor", "middle")

//                .attr("class", "valueLabel")
                .attr("style", "font-size:"+font+"px")
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
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
                });
        }
    if (x.rangeBand() >= 20) {
        svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//             Added by Shivam
                     var yValue=0;   
                     if(typeof chartData[div]["LabelPos"]==="undefined" || chartData[div]["LabelPos"]==="Top"){
                    yValue = (y(d[measureArray[0]])) < 15 ? y(d[measureArray[0]]) + 14 : y(d[measureArray[0]]);
                     }
                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bar-Center"){
                      yValue = height+((y(d[measureArray[0]])) < 15 ? (-height/2) : (y(d[measureArray[0]])-height)/2)+7;
                   } 
                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="OnRight-Bar"){
                      yValue = y(d[measureArray[0]]) + 15;
                   } 
                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                      yValue = maxHgt/2;
                   } 
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
						   yValue = height+(height/30+2);  
                  }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
						   yValue = height-2;  
                     }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
						    return "translate(" + xvalue + ", " + (yValue-2) + ")";
                     }
//                  
                    return "translate(" + xvalue + ", " + (yValue-3) + ")";
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
var rectH=0;   // buildbar
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
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
    rectY=boxH-(boxH*1.03)-20;
}
else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)+5;
}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.25);
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
               .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
       }
       }
       else{
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}
           else 
               {
         svg.append("g")
            .attr("id", "viewBylbl")
            .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
       }
       }
//for(var i in  
//measureArray){
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

            .attr("x",rectX+offset1+10)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(0)))
              count++
}

//var dataSlider=chartData[div]["dataSlider"]
//for (var l in dataSlider){
//alert(l);
//    
//}
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
        getSlider1(div,data,wid,hgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}
    
}    

}

}

function buildHorizontalBar(div,data, columns, measureArray,width,height,drillId){
    var chartData = JSON.parse(parent.$("#chartData").val());
     var appendDiv = "";
if(div.indexOf("Top1Div_")!=-1|| div.indexOf("Top2Div_")!=-1 || div.indexOf("Top3Div_")!=-1){
appendDiv = div;
var splitDiv = div.split("_");
div = splitDiv[1];
}else{
appendDiv = div;
}
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
    if(chartData[div]["chartType"]==="Combo-Horizontal"){
      var fun = "drillWithinchart(this.id,\""+div+"\",'comboDrill',\""+drillId+"\")"; //ss  
    }else{
    var fun = "drillWithinchart(this.id,\""+div+"\")"; //ss
    }
    
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
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.90], .1, .1);
    }else{
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.70], .1, .1);  
    }
    }else{     
      if(divWid > 590){ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.80], .1, .1);
    }else{
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.70], .1, .1);  
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
    
    var svg = d3.select("#"+appendDiv)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt-20)+" ")
    .classed("svg-content-responsive", true)
    .append("svg:g")
    .attr("transform", function(d){
        
    if(chartData[div]["chartType"]==="Combo-Horizontal"){
    return "translate(" + margin.left*.43 + "," + (margin.top+5) + ")";
    }else{
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    return "translate(" + margin.left*.76 + "," + (margin.top+5) + ")";
}else{
    return "translate(" + margin.left*.50 + "," + (margin.top+5) + ")";
    }
    }
    })        
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
    y.domain([parseFloat(minVal), parseFloat(max)]);
    
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
        .text(function(d,i) {
           if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{ 
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
        
    var maxWid=0;    
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
            .attr("width", function(d) {
                if(containsNegativeValue(data, measureArray, 'single','n',chartData[div])){
                    if(Math.abs(y(0) - y(d[measure]))>maxWid){
                        maxWid=Math.abs(y(0) - y(d[measure]));
                    }
                    return Math.abs(y(0) - y(d[measure]));
                }else{
                    if(y(d[measure])>maxWid){
                        maxWid=y(d[measure]);
                }
      return y(d[measure]);
                }
    })
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
        return d[columns[0]] + ":" + colIds[0];
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
            if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{   
         bar=svg.selectAll("rect1")
          .data(data)
          .enter()
                .append("text")
                .attr("transform", function(d) {
                    var yValue = (x(d[columns[0]])+(x.rangeBand())/2)+offset; 
                    var xvalue= (y(d[measure])) < 430 ? y(d[measure]) + 40 : y(d[measure]) + 40; 
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
                .attr("text-anchor", "middle")
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
                { var noOfRecords=data.length;
                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
                      if (!( noOfRecords <= 35)) {
                          return "";
                      }else{
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
                      }
//                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//                   if(typeof displayType=="undefined" || displayType==="Absolute"){
//                    }else{
//                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
//                    return percentage.toFixed(1) + "%";
//                }
//                }else {
//                return "";
//                }
               });
             }// end by mayank sharma for show table
        
        // add  for labels by mayank sh.
         bar=svg.selectAll("rect1")
          .data(data)
          .enter()
                .append("text")
                .attr("transform", function(d) {
                    var yValue = (x(d[columns[0]])+(x.rangeBand())/2)+offset; 
                    var xvalue= (y(d[measure])) < 430 ? y(d[measure]) + 40 : y(d[measure]) + 40; 
                     
                      
                    if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                          return "translate(" + maxWid/2 + ", " + (yValue) + ")"; 
                   }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bar-Center"){
                          return "translate(" + xvalue/2 + ", " + (yValue) + ")";
                   }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
                     return "translate(8, " + (yValue) + ")";      
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
                     return "translate(40, " + (yValue) + ")";      
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="OnRight-Bar"){
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
if(chartData[div]["chartType"]==="Combo-Horizontal"){
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
    rectW=(measureName.length+columns[0].length)*7+130;
    }else{
        rectW=(measureName.length)*7+130;
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
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
    rectH=10+17+viewByHgt;
    }else{
        rectH=10+17;
}
}
var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectX=23;
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
         if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
         svg.append("g")
            .attr("id", "viewBylbl")
               .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:9px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);  
     }else{
       }
       }else{
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
         svg.append("g")
            .attr("id", "viewBylbl")
            .attr("class", "valueLabel")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:9px")
            .attr("y",(rectY+20+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
           }else{
       }
       }
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
        var measureName='';
   var offset1=0;
   var offset2=0;
       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
           offset1=(columns[0].length*6.5+20);
           }else{
               offset1=0;
           }
           offset2=-24;
       }
       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='Y'){
               offset2=0;
           }else{
               offset2=-24;
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
}else{
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
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

  if (chartData[div]["chartType"] === "Combo-Horizontal"){ }else{
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
        getSlider1(div,data,width,height,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}
    
}    
}

}
} 

//function buildNegativeBar(div, data, columns, measureArray,wid,hgt,divAppend) {
// var customTicks = 5;
//var color = d3.scale.category10();
//var divWid=wid-30;
//var chartData = JSON.parse(parent.$("#chartData").val());
//var div1=parent.$("#chartname").val()
//   //   var divHgt=500;
//   
//   var divHgt=hgt;
//   var dashletid=div;
//     var fromoneview=parent.$("#fromoneview").val()
//var colIds= [];
//if(fromoneview!='null'&& fromoneview=='true'){
//    colIds = chartData[div1]["viewIds"];
//    divHgt=hgt;
//    divWid=wid;
//}else{
//    colIds = chartData[div]["viewIds"];
// }
//
//if(fromoneview!='null'&& fromoneview=='true'){
//    div=div1;
//}
//if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
//       hgt -=25;
//   }
//   var fromone=parent.$("#fromoneview").val();
//    if(fromone!=null && fromone=='true'){
////divWid = wid-20;
////   divHgt = hgt+60;
//    }
//    var chartMap = {};
//    var measure1 = measureArray[0];
//    var minVal = 0;
////    var chartData = JSON.parse($("#chartData").val());
//    if(fromoneview!='null'&& fromoneview=='true'){
//     var prop = graphProp(div1);
//
//}else{
//
//if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
//     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
// }
//    var prop = graphProp(div);
//}
//    var autoRounding1;
//
//        autoRounding1 = "1d";
//
//    var fun = "drillWithinchart(this.id,\""+div+"\")";
//    if(fromoneview!='null'&& fromoneview=='true'){
//var regid=dashletid.replace("chart","");
//        var repId = parent.$("#graphsId").val();
//    var repname = parent.$("#graphName").val();
//      var oneviewid= parent.$("#oneViewId").val();
//
// fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
// var olap=dashletid.substring(0, 9);
//if(olap=='olapchart'){
//fun = "viewAdhoctypes(this.id)";
//    }
//    }
//    var botom = 50;
//
////add condition by mayank sh. for hidden white space
//var margin = { top: 10, right: 12,  bottom: 30, left: 70 };
//if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
//
//
//    margin = { top:12, right: 20,  bottom: 30, left: 16  };
//}
//
//   var width = divWid;
//    var height = 0;
//if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
//            height = divHgt*.78 ; //- margin.top - margin.bottom
//}else{
//            height = divHgt *.73; //- margin.top - margin.bottom
//
//}  //end condition by mayank sh. for hidden white space
//
//
//    var barPadding = 4;
////    var formatPercent = d3.format(".0%");
////    Added by shivam   
//     var range="";
//    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
//     range=0.6;
//}else if(chartData[div]["barsize"] === "ExtraThin"){
//    range=0.83;
//}else{ 
//    range=0.1;
//    }
//   var x = d3.scale.ordinal()
//                .rangeRoundBands([0, width], range, range);
////   var x = d3.scale.ordinal()
////            .rangeRoundBands([0, width], .1, .1);
//    var y = d3.scale.linear()
//            .range([height, 0]);
//    var measArr = [];
//    // add by mayank sh. for axis line 
//     if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
//            make_x_axis = function() {
//    return d3.svg.gridaxis()
//        .scale(x)
//         .orient("bottom")
//         .ticks(5)
//}
//    make_y_axis = function() {
//    return d3.svg.gridaxis()
//        .scale(y)
//        .orient("left")
//        .ticks(customTicks)
//}
//}else{
//       make_x_axis = function() {
//    return d3.svg.axis()
//        .scale(x)
//         .orient("bottom")
//         .ticks(5)
////         .ticks(percent)
//}
//
// make_y_axis = function() {
//    return d3.svg.axis()
//        .scale(y)
//        .orient("left")
//        .ticks(customTicks)
////        .ticks(percent)
//}
//}
//if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
//    var xAxis = d3.svg.trendaxis()
//            .scale(x)
//            .orient("bottom");
//}else{
//   var xAxis = d3.svg.axis()
//            .scale(x)
//            .orient("bottom");
//} // end by mayank sh. for axis line 
//
//    var    yAxis = d3.svg.axis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d) {
//                    if(typeof displayY !=="undefined" && displayY =="Yes"){
//                  if(yAxisFormat==""){
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
//                    }
//            else{
//                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
//                }
//                }else {
//                    return "";
//                }
//                });
//
//        yAxis = d3.svg.trendaxis()
//                .scale(y)
//                .orient("left")
//                 .ticks(customTicks)
////                 .ticks(percent)
//                .tickFormat(function(d, i) {
//                   if(typeof displayY !=="undefined" && displayY =="Yes"){
//                     if(yAxisFormat==""){
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
//                    }
//            else{
//                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
//                }
//                   }else {
//                       return "";
//                   }
////                    return addCommas(d);
//                });
////    }
//    var yAxis1 = d3.svg.axis1()
//            .scale(y)
//            .tickFormat(function(d, i) {
//                measArr.push(d);
//                return "";
//            });
//            var offset=0;        
//    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]==='top'){
//       offset=25;
//    } 
//    //    .tickFormat(formatPercent);
//    var svg = d3.select("#" + dashletid)
////    var svg = d3.select("#Hchart1")
//   //    added by manik
//            // .append("div")
//            // .classed("svg-container", true)
//            .append("svg")
//            .attr("id", "svg_" + dashletid)
////             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom+60)+" ")
////.attr("preserveAspectRatio", "xMinyMin")
////            .attr("width", width + margin.left + margin.right)
////            .attr("height", height + margin.top + margin.bottom +40 )
//            .append("g")
//            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");
//
//    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
//            .append("svg:linearGradient")
//            .attr("id", function(d) {
////                return "gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//            .attr("x1", "0%")
//            .attr("y1", "30%")
//            .attr("x2", "50%")
//            .attr("y2", "30%")
//            .attr("spreadMethod", "pad")
//            .attr("gradientTransform", "rotate(0)");
//    colorMap = {};
//    gradient.append("svg:stop")
//            .attr("offset", "0%")
//            .attr("stop-color", function(d, i) {
//                var colorShad;
////                if (isShadedColor) {
////                    colorShad = color(d[shadingMeasure]);
////                } else if (conditionalShading) {
//////                    return getConditionalColor("steelblue", d[conditionalMeasure]);
////                    return getConditionalColor(color(i), d[conditionalMeasure]);
////                } else {
////                    var drilledvalue;
////                    try {
////                        drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
////                    } catch (e) {
////                    }
////                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
////                        colorShad = drillShade;
////                    } else {
//////                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//////                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//////                        } else {
////                        colorShad = color(i);
//////                            colorShad = "steelblue";
//////                        }
////                    }
////                }
////                chartMap[d[columns[0]]] = colorShad;
////                colorMap[i] = d[columns[0]] + "__" + colorShad;
//                return colorShad="steelblue";
//            })
//            .attr("stop-opacity", 1);
//    gradient.append("svg:stop")
//            .attr("offset", "9%")
//            .attr("stop-color", "rgb(240,240,240)")
//            .attr("stop-opacity", 1);
//    gradient.append("svg:stop")
//            .attr("offset", "80%")
//            .attr("stop-color", function(d, i) {
//                var colorShad;
////                if (isShadedColor) {
////                    colorShad = color(d[shadingMeasure]);
////                } else if (conditionalShading) {
//////                    return getConditionalColor("steelblue", d[conditionalMeasure]);
////                    return getConditionalColor(color(i), d[conditionalMeasure]);
////                } else {
////                    var drilledvalue;
////                    try {
////                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
////                    } catch (e) {
////                    }
////                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[colIds[0]]) !== -1) {
////                        colorShad = drillShade;
////                    } else {
////                        if(fromoneview!='null'&& fromoneview=='true'){
////                             colorShad = "steelblue";
////                        }else{
////                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
////                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
////                        } else {
////                            colorShad = color(i);
//////                            colorShad = "steelblue";
////                        }
////                    }
////                    }
////                }
//////                colorMap[i] = d[columns[0]] + "__" + colorShad;
//////colorShad="steelblue";
////                return colorShad;
//            })
//            .attr("stop-opacity", 1);
////    parent.$("#colorMap").val(JSON.stringify(colorMap));
//
////    svg.append("svg:rect")
////            .attr("width", width)
////            .attr("height", height)
////            .attr("onclick", "reset()")
////            .attr("class", "background");
////    var max = maximumValue(data, measure1);
//var x0 = Math.max(-d3.min(data), d3.max(data));
//     x.domain(data.map(function(d) { return d[columns[0]]; }));
//     y.domain([d3.min(data, function(d) { return d[measure1]; }), d3.max(data, function(d) { return d[measure1]; })]);
////    x.domain(data.map(function(d) {
////        return d[columns[0]];
////    }));
//
//
//    var max = 0;
//    if(fromoneview!='null'&& fromoneview=='true'){
//  max = maximumValue(data, measure1);
//  if (data.length > 1) {
//        minVal = minimumValue(data, measure1) * .8;
//    }
//    var yMin = 0;
//    yMin = minVal;
//    y.domain([yMin, max]);
//    svg.call(yAxis1);
//    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
//     if(measArr[0]<0){
//    minVal = measArr[0] + diffFactor;
//    }
//    else{
//       minVal = measArr[0] + diffFactor;
//       if(measArr[0]>=0 && minVal<0){
//           minVal=0;
//       }
//    }
//    }else{
//if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
//                      
//    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
//    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
//}else{
//    max = maximumValue(data, measure1);
//}}else{
//    max = maximumValue(data, measure1);
//}
// if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
// if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
//  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
// }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]=="Default" ){
//   minVal = 0;
// }else{
//    if (data.length > 1) {
//        minVal = minimumValue(data, measure1) ;
//    }}
//}else{
////    if (data.length > 1) {
////        minVal = minimumValue(data, measure1) * .8;
////    }
////    var yMin = 0;
////    yMin = minVal;
////    y.domain([yMin, max]);
////    svg.call(yAxis1);
////    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
////     if(measArr[0]<0){
////    minVal = measArr[0] + diffFactor;
////    }
////    else{
////       minVal = measArr[0] + diffFactor;
////       if(measArr[0]>=0 && minVal<0){
////           minVal=0;
////       }
////    }
//         if (data.length > 1) {
//        minVal = minimumValue(data, measure1) * .8;
//    }else{
//        minVal = 0;
//       }
//    }
//    }
//
//    y.domain([parseFloat(minVal), parseFloat(max)]);
////    for (var j=0; j <= height; j=j+50) {
////    svg.append("svg:line")
////        .attr("x1", 0)
////        .attr("y1", j)
////        .attr("x2", width)
////        .attr("y2", j)
////        .style("stroke", "#A69D9D")
////        .style("stroke-width", .5)
////        .style("z-index", "9999");
////};
// if(fromoneview!='null'&& fromoneview=='true'){
//     div=div1;
// }
//if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
//    svg.append("g")
//        .attr("class", "grid11")
//        .call(make_y_axis()
//            .tickSize(-width, 0, 0)
//            .tickFormat("")
//        )
//            }
//            if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
//    svg.append("g")
//            .attr("class", ".x axis")
//            .attr("transform", "translate(0," + (height*1) + ")")
//            .call(xAxis)
//            .selectAll('text')
//            .text(function(d,i) {
//                return buildXaxisFilter(div,d,i);
//            })
//            .attr('x',function(d,i){  // add by mayank sharma
//        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
//            return  5;
//        } else if (chartData[div]["legendPrintType"] === "Horizontal") {
//            return 3;
//        }else if (chartData[div]["legendPrintType"] === "Vertical") {
//            return -5;
//        }else {
//            return 0;
//        }
//    })
//    .attr("style","font-size:11px")
//    .attr('y',function(d,i){
//        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
//            if(parseInt(i % 2) ==0){
//            return  10;
//            }else{
//            return 30;   
//            }
//        } else if (chartData[div]["legendPrintType"] === "Horizontal") {
//            return 10;
//        }else if (chartData[div]["legendPrintType"] === "Vertical") {
//            return 2;
//        }else {
//            return 9;
//        }
//    })  
//    .attr('transform',function(d,i){
//        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
//            return  "";
//        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
//            return "";
//        }else if (chartData[div]["legendPrintType"] === "Vertical") {
//            return "rotate(-85)";
//        }else {
//            return "rotate(-35)";
//        }
//    })      
//    .style('text-anchor',function(d,i){
//        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
//            return  "middle";
//        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
//            return "middle";
//        }else if (chartData[div]["legendPrintType"] === "Vertical") {
//            return "end";
//        }else {
//            return "end";
//        }
//    })
//            .append("svg:title").text(function(d) {
//        return d;
//    });
//            }
//    svg.append("g")
//            .attr("class", "y axis")
//            .call(yAxis)
//            .append("text")
////            .attr("transform", "rotate(-90)")
////            .attr("y", 6)
//            .attr("dy", ".71em")
//            .style("text-anchor", "end");
//
//   //add by mayank sh.(08/6/15) for targt line
//   var target = "";
//              if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//              target = chartData[div]["targetLine"];
//               svg.append("text")
//              .text(target)
//              .attr("x", (width)*0.95)
//              .attr("y", y(parseInt(target))-5)
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
////                var target = "";
//                if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                target = chartData[div]["targetLine"];
//                return y(parseInt(target));
//                }
//            });
//
//
// var bars=svg.selectAll(".bar")
//            .data(data)
//            .enter().append("g")
//            .attr("class", "bar")
//            .append("rect");
//            bars.attr("rx", barRadius)
//            .attr("fill", function(d,i) {
//                 var drilledvalue;
//                 var targetLine;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
//                        return drillShade;
//                    }
//                if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]=='N')
//                {
//                  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                      targetLine = chartData[div]["targetLine"];
//                       return targetColor(d,measure1,targetLine);
//                  } else {
////                      
//var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
//                      targetLine = "";
//                return colorfill;
//                  }
//               //"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//
//
//           }
//                else
//                {
//                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                      targetLine = chartData[div]["targetLine"];
//                       return targetColor(d,measure1,targetLine);
//                  } else {
//                      targetLine = "";
//                      return color(0);
//                  }
//
//                    return color(0);
//                }
//            })
//             .attr("color_value", function(d,i){
//                   var drilledvalue;
//                 var targetLine;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
//                        return drillShade;
//                    }
//                if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]=='N')
//                {
//                  if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                      targetLine = chartData[div]["targetLine"];
//                       return targetColor(d,measure1,targetLine);
//                  } else {
//                      targetLine = "";
//
//var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
//                return colorfill;
//                  }
//               //"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//
//
//           }
//                else
//                {
//                     if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
//                      targetLine = chartData[div]["targetLine"];
//                       return targetColor(d,measure1,targetLine);
//                  } else {
//                      targetLine = "";
//                      return color(0);
//                  }
//
//                    return color(0);
//                }
//             })
//            .attr("index_value", function(d, i) {
//                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//            .attr("class", function(d, i) {
//                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
//            })
//            .attr("id", function(d) {
//                return d[columns[0]] + ":" + d[measure1];
//            })
//            .attr("onclick", fun)
//            .attr("x", function(d) {
//                return x(d[columns[0]]);
//            })
//            .attr("width",0)
//            .transition()
//            .duration(2000)//1 second
//            .attr("width", x.rangeBand())
//            .attr("y", function(d) {
//                return y(Math.max(0,(d[measure1])));
//            })
//            .attr("height", function(d) {
//              return Math.abs(y(d[measure1]) - y(minVal)) ;
////                return height - y(d[measure1]);
//            });
//            bars.on("mouseover", function(d, i) {
////                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
////                {
//                prevColor = color(i);
//
////                }
////                if (toolTipType === "customize") {
////                    for (var num in toolTipData) {
////                        if (d[columns[0]] === toolTipData[num][columns[0]]) {
////                            break;
////                        }
////                    }
////                } else {bu
//if(fromoneview!='null'&& fromoneview=='true'){
//                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
//                }else{
//                       var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
//                    var selectedBar = d3.selectAll(barSelector);
//                    selectedBar.style("fill", drillShade);
//                    show_details(d, columns, measureArray, this,div);
//                }
////                    show_details(d, columns, measureArray, this,div);
////                }
//            })
//            .on("mouseout", function(d, i) {
//                if(fromoneview!='null'&& fromoneview=='true'){
//                }else{
////                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
////                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
//                    var selectedBar = d3.selectAll(barSelector);
//
//                    var colorValue = selectedBar.attr("color_value");
////                    var colorValue = prevColor;
//                    selectedBar.style("fill", colorValue);
//                }
////                }
//                hide_details(d, i, this);
//            });
//             //target Line
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
////    svg.selectAll(".text")
////            .data(data)
////            .enter()
////            .append("text")
////            .attr("class", "text")  //  MISSING FROM CODE IN PAGE DISPLAY
////            .attr("x", function(d, i) {
////                return i * (width / data.length) + (width / data.length - barPadding) / 2;
////            })
////            .attr("y", function(d) {
////
////                return  y(d[measure1]) + 12;  //15 is now 14
////
////            });
////    d3.select("#download").on("click", function() {
////        d3.select(this)
////                .attr("href", 'data:application/octet-stream;base64,' + btoa(encodeURIComponent(d3.select("#chart0").html())))
////                .attr("download", "viz.svg");
////    });
////
////    if ((typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) || parent.$("#isMap").val() === "MapTable") {
////    }
////    else {
////        buildCircledrill(height);
////    }
//
//    var sum = d3.sum(data, function(d) {
//        return d[measureArray[0]];
//    });
//// Added by shivam
//
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
//     	  var path =   svg.append("path")
//            .data(data)
//            .attr("d", Averageline(data))
//            .attr("fill", "transparent")
//            .attr("x", (width)*0.85)
//            .style("z-index", "9999")
//            .style("stroke-width", "1px")
//            .style("stroke", "black");
//     }     }
//
//    
//    
//    if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
//        {
//            var font=12;
//             font=width/39.2;
//            font=font>12?12:font;
//            font=font<7?7:font;
//            // add by maynk sh. for fontsize
//
//             if(typeof chartData[div]["fontsize"]!=="undefined"){
//                      font =  chartData[div]["fontsize"];
//             }// end by maynk sh. for fontsize
//
//            svg.selectAll(".bar")
//                .append("svg:text")
//                .attr("transform", function(d) {
//                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
////        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
//                     return "translate(" + (xvalue) + ", " + (height) + ")rotate(" + -90 + ")";// add by mayank sh. width chang  for text size
//                })
////                .attr("text-anchor", "middle")
//
////                .attr("class", "valueLabel")
//                .attr("style", "font-size:"+font+"px")
//                .attr("index_value", function(d, i) {
//                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("id", function(d) {
//                    return d[columns[0]] + ":" + d[measure1];
//                })
//                .attr("fill", function(d,i){
////            Added by shivam
//             var AxisNameColor="";
//                      if (typeof chartData[div]["AxisNameColor"]!=="undefined") 
//                     {
//          AxisNameColor=chartData[div]["AxisNameColor"];
//        } else{
//            AxisNameColor="#d8d47d";
//        }return AxisNameColor;
//                })
//                .text(function(d)
//                {
//                        return d[columns[0]];
//                });
//        }
//    if (x.rangeBand() >= 20) {
//        svg.selectAll(".bar")
//                .append("svg:text")
//    .attr("transform", function(d) {   // add by shivam
//                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//                     var yValue=0;   
//                     if(typeof chartData[div]["LabelPos"]==="undefined" || chartData[div]["LabelPos"]==="Top"){
//                    yValue = (y(d[measureArray[0]])) < 15 ? y(d[measureArray[0]]) + 14 : y(d[measureArray[0]]);
//					  
//        }
//                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
//                      yValue = height+((y(d[measureArray[0]])) < 15 ? (-height/2) : (y(d[measureArray[0]])-height)/2)+7;
//                   } 
//                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
//						 yValue = height+(height/30+2);   
//                     }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
//						    yValue = height-2;  
//                     }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
//						    return "translate(" + xvalue + ", " + (yValue-2) + ")";  
//                     }
////            
//                    return "translate(" + xvalue + ", " + (yValue-3) + ")";
//                })
//                .attr("text-anchor", "middle")
//                .attr("class", "valueLabel")
//                .style("font-size",function(d, i){
//              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
//                 return (chartData[div]["labelFSize"]+"px");
//          }else{
//                 return "10px";
//              }
//            })
//                .attr("onclick", fun)
//                .attr("index_value", function(d, i) {
//                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("id", function(d) {
//                    return d[columns[0]] + ":" + d[measure1];
//                })
//                .attr("fill", function(d,i){
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
//                {
//                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//                  
//                    if(typeof displayType=="undefined" || displayType==="Absolute"){
//
//                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
//                    }else{
//                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
//                    return percentage.toFixed(1) + "%";
//                }
//            }else {return "";}
////        return "" + autoFormating(d[measureArray[0]]);
//                }).on("mouseover", function(d, i) {
//            if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//            {
//                var bar = d3.select(this);
//                var indexValue = bar.attr("index_value");
//                var barSelector = "." + "bars-Bubble-" + indexValue;
//                var selectedBar = d3.selectAll(barSelector);
//                selectedBar.style("fill", drillShade);
//            }
////            if (toolTipType === "customize") {
////                for (var num in toolTipData) {
////                    if (d[columns[0]] === toolTipData[num][columns[0]]) {
////                        show_details(toolTipData[num], custToolTipViewBy, custToolTipMeasure, this);
////                        break;
////                    }
////                }
////            } else {
//if(fromoneview!='null'&& fromoneview=='true'){
//                     show_details(d, columns, measureArray, this);
//                }else{
//                show_details(d, columns, measureArray, this);
//                }
////            }
//        })
//                .on("mouseout", function(d, i) {
//                    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                    {
//                        var bar = d3.select(this);
//                        var indexValue = bar.attr("index_value");
//                        var barSelector = "." + "bars-Bubble-" + indexValue;
//                        var selectedBar = d3.selectAll(barSelector);
//
//                        var colorValue = selectedBar.attr("color_value");
//                        selectedBar.style("fill", colorValue);
//                    }
//                    hide_details(d, i, this);
//                });
//    }
//if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
//else{ 
//count=0;
//var boxW=(width + margin.left + margin.right);
//var boxH=(height + margin.top + margin.bottom+30);
////var rectW=150+boxW*0.17;
//var measureName='';
//if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
//    measureName=chartData[div]["measureAlias"][measureArray[0]];
//}else{
//    measureName=checkMeasureNameForGraph(measureArray[0]);
//}
//var len=measureName.length;
//if(columns[0].length+2>len){
//    len=columns[0].length+2;
//}
//var rectW=0;
//if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//        rectW=(measureName.length)*7+130;
//    }
//    else{
//    rectW=(measureName.length+columns[0].length)*7+130;
//}
//}
//else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomleft" || chartData[div]["lbPosition"] === "bottomcenter"){
//    rectW=30+(len*7)+85;
//}
//rectW = rectW<170?170:rectW;
//var viewByHgt=15;
//var rectH=0;   // buildbar
//if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
//    rectH=17;
//}
//else{ 
//    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//        rectH=10+17;
//    }
//    else{
//    rectH=10+17+viewByHgt;
//}
//}
//var rectX;
//if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//    rectX=10;
//}
//else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
//    rectX=boxW-rectW;
//}
//else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
//    rectX=5;
//}
//else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
//    rectX=boxW/2-rectW/2;
//}
//var rectY=-5;
//if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
//    rectY=boxH-(boxH*1.03)-20;
//}
//else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
//    rectY=boxH-(boxH*1.03)+5;
//}
//else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
//    rectY=boxH-rectH-(boxH*0.25);
//}
//var backColor;
//if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
//    backColor=chartData[div]["lbColor"];
//}
//else{
//    backColor="none";
//}
////alert((boxH-(boxH*.98)-5)+":"+boxW);
//var border=0;
//if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
//    border=4;
//}
//if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
//svg.append("g")
//         //   .attr("class", "y axis")
//            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("style", "overflow:scroll")
//
////            .attr("x",rectlen)
////            .attr("y",rectyvalue)
//            .style("stroke", "grey")
//            .style("stroke-dasharray", ("3, "+border))
////            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
//            .attr("x", rectX)
//            .attr("y", rectY)
//            .attr("width", (rectW-85))
//            .attr("height", rectH)
//            .attr("rx", 10)         // set the x corner curve radius
//            .attr("ry", 10)
//            .attr("fill", backColor);
//}
//       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//         if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//             
//         }
//         else{
//         svg.append("g")
//            .attr("id", "viewBylbl")
//            .append("text")
//            .attr("x",rectX+10)
//            .attr("style","font-size:10px")
//            .attr("y",(rectY+12+count*15))
//            .attr("fill", 'black')
//            .text(columns[0]);  
//       }
//       }
//       else{
//           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//    }
//    else{
//         svg.append("g")
//            .attr("id", "viewBylbl")
//            .append("text")
//            .attr("x",rectX+10)
//            .attr("style","font-size:10px")
//            .attr("y",(rectY+20+count*15))
//            .attr("fill", 'black')
//            .text(columns[0]);
//       }
//       }
////for(var i in  measureArray){
//if(fromoneview!='null'&& fromoneview=='true'){
//div=div1
//     }
//        var measureName='';
//   var offset1=0;
//   var offset2=0;
//       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//                offset1=0
//           }
//           else{
//                offset1=(columns[0].length*7+20);
//           }
//           offset2=-24;
//       }
//       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
//           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//               offset2=-24;
//           }
//           else{
//               offset2=0;
//           }
//       }
//   svg.append("g")
//            .attr("id", "measure"+count)
//            .append("text")
//            .attr("x",rectX+offset1+25)
//            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
//            .attr("fill", 'black')
//            .text(function(d){
//        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
//            return '';
//        }
//     
//        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[0]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!== measureArray[0]){
//       
//            measureName=chartData[div]["measureAlias"][measureArray[0]];
//        }else{
//            measureName=checkMeasureNameForGraph(measureArray[0]);
//           
//        }
//            var length=0;
//                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[0]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[0]] === "20") {
//                    length=20;
//                }
//                else{
//                    length=chartData[div]["measureLabelLength"][measureArray[0]];
//                }
//                if(measureName.length>length){
//                    return measureName.substring(0, length)+"..";
//                }else {
//                    return measureName;
//          }
//           }).attr("svg:title",function(d){
//               return measureName;
//           })
//           var rectsize;
//           if(width<height){
//              rectsize = parseInt(width/25);
//           }
//           else{
//              rectsize = parseInt(height/25);
//           }
//           rectsize=rectsize>10?10:rectsize;
//           svg.append("g")
//         //   .attr("class", "y axis")
//            .append("rect")
////            .attr("style","margin-right:10")
////            .attr("style", "overflow:scroll")
//
//            .attr("x",rectX+offset1+10)
//            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
////            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
//            .attr("width", rectsize)
//            .attr("height", rectsize)
//            .attr("fill", getDrawColor(div, parseInt(0)))
//              count++
//}
//}

function buildstackedBar(div,data, columns, measureArray,wid,hgt) {
if (typeof data!== 'undefined' && data.length==1) { 
        return buildMultiAxisBar(div, data, columns, measureArray, wid, hgt)
}
    var colorSet = d3.scale.category10();
    //data=JSON.parse('[{"Time":"Jan-2010","Gross Sales":"6849869.0","Discount Amount":"-1326676.7900000014","Net Sales":"5523165.0"},{"Time":"Feb-2010","Gross Sales":"8412504.0","Discount Amount":"1298215.7400000007","Net Sales":"7114262.0"},{"Time":"Mar-2010","Gross Sales":"12604729","Discount Amount":"2319563.669999996","Net Sales":"10285116"},{"Time":"Apr-2010","Gross Sales":"9819086.0","Discount Amount":"1440832.9000000006","Net Sales":"8378214.0"},{"Time":"May-2010","Gross Sales":"10137405","Discount Amount":"1913717.3999999997","Net Sales":"8223655.0"},{"Time":"Jun-2010","Gross Sales":"10607765","Discount Amount":"1563209.13","Net Sales":"9044520.0"},{"Time":"Jul-2010","Gross Sales":"9833995.0","Discount Amount":"1748566.7900000005","Net Sales":"8085389.0"},{"Time":"Aug-2010","Gross Sales":"11453123","Discount Amount":"1826053.48","Net Sales":"9627030.0"},{"Time":"Sep-2010","Gross Sales":"15499859","Discount Amount":"2423335.6799999992","Net Sales":"13076457"},{"Time":"Oct-2010","Gross Sales":"13291994","Discount Amount":"2287947.4399999976","Net Sales":"11004011"},{"Time":"Nov-2010","Gross Sales":"11403678","Discount Amount":"1650813.0699999994","Net Sales":"9752821.0"},{"Time":"Dec-2010","Gross Sales":"10166414","Discount Amount":"1895993.6700000004","Net Sales":"8270389.0"},{"Time":"Jan-2011","Gross Sales":"10512312","Discount Amount":"1636936.4299999988","Net Sales":"8875346.0"},{"Time":"Feb-2011","Gross Sales":"11921873","Discount Amount":"2136875.1500000004","Net Sales":"9784946.0"},{"Time":"Mar-2011","Gross Sales":"10972181","Discount Amount":"1689906.4899999986","Net Sales":"9282249.0"},{"Time":"Apr-2011","Gross Sales":"10407897","Discount Amount":"1846773.2600000014","Net Sales":"8561071.0"},{"Time":"May-2011","Gross Sales":"12978219","Discount Amount":"1968810.5399999989","Net Sales":"11009371"},{"Time":"Jun-2011","Gross Sales":"8787876.0","Discount Amount":"1518023.570000001","Net Sales":"7269832.0"},{"Time":"Jul-2011","Gross Sales":"12874663","Discount Amount":"1932678.4799999995","Net Sales":"10941933"},{"Time":"Aug-2011","Gross Sales":"9531908.0","Discount Amount":"1748616.0700000017","Net Sales":"7783259.0"},{"Time":"Sep-2011","Gross Sales":"9238575.0","Discount Amount":"1351165.7400000007","Net Sales":"7887383.0"},{"Time":"Oct-2011","Gross Sales":"10626055","Discount Amount":"1824601.550000001","Net Sales":"8801431.0"},{"Time":"Nov-2011","Gross Sales":"10974172","Discount Amount":"1718535.7099999997","Net Sales":"9255598.0"},{"Time":"Dec-2011","Gross Sales":"10624954","Discount Amount":"1963206.4499999983","Net Sales":"8661719.0"}]')
for(var i in data){
    for(var j in measureArray){
        if(parseInt(data[i][measureArray[j]])<0){
            data[i][measureArray[j]]=0;
        }
    }
}    
for(var i in data){
    for(var j in measureArray){
        if(parseInt(data[i][measureArray[j]])<0){
            data[i][measureArray[j]]=0;
        }
    }
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
    
    
//    wid=parseFloat($(window).width())*(.35);
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
    var margin = {
        top: 5,
        right: 25,
        bottom: (hgt*.23),
        left: 70},

    width = wid - margin.left - margin.right,
            height = hgt -margin.top- margin.bottom-15;

    //    Added by shivam

    var ratio="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     ratio=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    ratio=0.83;
}else{ 
    ratio=0.1;
    }
//   if(typeof chartData[div]["barsize"]!=="undefined" && chartData[div]["barsize"]==="Yes"){
//
//        ratio=0.6
//    }
    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width+15], ratio); // add width by mynk sh.  //edit by shivam
    //.rangeRoundBands([0, width+15], .1);
   //add condition by mayank sh. for display properties
 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
    var y = d3.scale.linear()
            .rangeRound([height+20, 0]);
 }else{
    var y = d3.scale.linear()
            .rangeRound([height-5, 0]);
        }
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = { top: 8,  right:-15,  bottom: (hgt+20), left: 30  };
     width = wid *.9;
}else{
     margin = { top: 5, right: 25, bottom: (hgt*.2), left: 70};
  }  //end  by mayank sh. for display properties
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
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
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
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
    var localColorMap = {};
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=20;
    }
    var svg = d3.select("#"+div)
            .append("svg")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(wid)+" "+(height + margin.top + margin.bottom+60)+" ")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+15+offset) + ")");

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
                return "gradient" + (d).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
                if (typeof centralColorMap[d.toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d.toLowerCase()];
                } else {
                    colorShad = color(i);
                }
                if (typeof localColorMap[d] === "undefined" || localColorMap[d] === "undefined") {
                    localColorMap[d] = colorShad;
                }
                if (typeof chartMap[d] === "undefined") {
                    chartMap[d] = colorShad;
                    colorMap[i] = d + "__" + colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
            
    var tmp = color;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        return key !== columns[0];
    }));
    data.forEach(function(d,i) {
        var y0 = 0;
        d.val = tmp.domain().map(function(name) {
            return {
                name: name,
                viewBy: d[columns[0]],
                value: d[name],
                y0: y0,
                y1: y0 += +d[name]
            };
        });
        d.total = d.val[d.val.length - 1].y1;
    });
    var col = [];
    data.forEach(function(d) {
        col.push(d[columns[0]]);
    });

//    data = data.sort(function(a, b) {
//        return b["total"] - a["total"];
//    });
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    var max = 0;
var minVal = 0;
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }
//          if (data.length > 1) {
//        minVal = minimumValue(data, measureArray[0]) * .8;
//        alert(minVal)
//    }else{
//        minVal = 0;
//       }
       }



   y.domain([parseFloat(minVal), parseFloat(max)]);

if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayYLine"]==="undefined" || chartData[div]["displayYLine"]==="" || chartData[div]["displayYLine"]==="Yes"){   
  svg.append("line")
        .attr("x1",0)
        .attr("y1",0)
        .attr("x2",0)
        .attr("y2",(hgt*.75))
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
//add condition by mayank sh. for display properties

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
               if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i);
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
            return 20;   
            }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 2;
        }else {
            return 5;
        }
    })  
     .style('font-size',function(d,i) {
                return font1(div,d,i);
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
//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}
else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            .attr("y", 0)
            .attr("x", -9)
            .attr("dy", ".32em")
            .style("text-anchor", "end")
              var ht = parseFloat(hgt)* .65;
          }  //end  by mayank sh. for display properties

    var state = svg.selectAll(".state")
            .data(data)
            .enter().append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
               return "translate(" + (x(d[columns[0]])-x(d.x)) + ",0)";
}else if(chartData[div]["barsize"] === "ExtraThin"){
          return "translate(" + (x(d[columns[0]])-x(d.x)) + ",0)";
    }else{
          return "translate(" + (x(d[columns[0]])-x(d.x)) + ",0)";
    }
            });
var count = 0;
var colIds=[];
colIds = chartData[div]["viewIds"];
    state.selectAll("rect")
            .data(function(d) {
                return d.val;
            })
            .enter().append("rect")
            .attr("width", x.rangeBand())
            .attr("y", function(d) {
                return y(d.y1);
            })
            .attr("height", function(d) {
                return y(d.y0) - y(d.y1);
            })
            .attr("x", function(d) {
                return x(d.x);
            })
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
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .on("mouseover", function(d, i) {
                var msrData;
            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(d.value);
                }
            else{
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
                 msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";//Added by shivam
                
       /*Added by Ashutosh*/      
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
//  for(var i=0;i<measureArray.length;i++){
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
                    var xvalue =x(d.x)+x.rangeBand()/2;
//                    alert(x(d.x));
                    var yValue = y(d.y1);
                  if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom"){
                      return "translate(" + (xvalue) + ", " + ((y(d.y0))-3)+ ")";
               }   //Added by Shivam// update by mayank sharma
                  if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom-Bar"){
                       if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
                   return "translate(" + (xvalue) + ", " + (height+(height/6)+2)+ ")";    
                   }else{
                      return "translate(" + (xvalue) + ", " + (height+(height/30)+2)+ ")";
               }}  
                else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop"){
                     return "translate(" + (xvalue) + ", " + (yValue-3) + ")"; 
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBar"){
                   return "translate(" + (xvalue) + ", " + (yValue+8) + ")"; 
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop-Bar"){
                     return "translate(" + (xvalue) + ", -2 )"; 
               }else{
       return "translate(" + (xvalue) + ", " + ((yValue+((y(d.y0)-yValue)/2))+4) + ")";
               }
                })
                .style("text-anchor","middle")
            .text(function(d,i){
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
                var percentage = (d.value / parseFloat(sum[i])) * 100;   
                if(percentage<5){
                    return "";
                }else{
                 if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                  if(typeof chartData[div]["dataDisplayArr"] === "undefined" || typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                        
                        if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
                           return addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                    }else{
                    return percentage.toFixed(1) + "%";
                                }
                               }else{
                                 return "";
                             }
                 }else{
                                 return "";
                             }
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
//           }
for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
//              .text(avg)
               .text(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
        return x(d[columns[0]])
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
 // defineTargetline
     for(var i=0;i<measureArray.length;i++){   // stackebar targetline
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                        return addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
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
                return x(d[columns[0]]);
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
            var path = svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .attr("x", (width)*.95)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
        }
    }       
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
count=0;           
var boxW=wid;
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
    
      svg
         .append("text")
     // .append("g")
            .attr("id", "viewBylbl")
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
   svg
   .append("g")
            .append("text")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
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
for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               viewByHgt=0;
           }
           
           var rectsize;
           if(width<height){
              rectsize = parseInt(width/25);
           }
           else{
              rectsize = parseInt(height/25);
           }
           rectsize=rectsize>10?10:rectsize;
           svg
           //.append("g")
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
            .attr("class", "y axis")
            .append("text")
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
        getSlider1(div,data,wid,hgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}
    
}    

}
}

function buildstackedBarLine(div,data, columns, measureArray,wid,hgt) {

    var colorSet = d3.scale.category10();
//    wid=parseFloat($(window).width())*(.35);
    var measure = measureArray[0];
        var measureLength = measureArray.length -1;

    var chartMap = {};
    var measArr = [];
    var autoRounding;
    var fromoneview=parent.$("#fromoneview").val();
  var dashletid;
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
div=div1;
 y2AxisRounding=0;
     }
   var chartData = JSON.parse(parent.$("#chartData").val());
   if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        hgt-=20
    }
    var customTicks = 5;
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
  }
    var customTicks2 = 5;
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks1"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks1"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks2 = chartData[div]["yaxisrange"]["axisTicks1"];
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
    var fun = "drillWithinchart(this.id,\""+div+"\")";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150;
    var prop = graphProp(div);
    var yAxis;
    var margin = {
        top: 5,
        right: 20,
        bottom: (hgt*.2),
        left: 78},

    width = wid - margin.left - margin.right,
            height = hgt -margin.top- margin.bottom-15;

  var max1 = maximumValue(data, measureArray[measureLength]);
//    var minVal1 = minimumValue(data, measureArray[measureLength]);
var y1 = d3.scale.linear()
.domain([0, max1])
.range([height, 0]);

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
                .rangeRoundBands([0,  width+15], range);


//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, width+15], .1); // add width by mynk sh.
   //add condition by mayank sh. for display properties
 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
    var y = d3.scale.linear()
            .rangeRound([height+3, 0]);
 }else{
    var y = d3.scale.linear()
            .rangeRound([height-2, 0]);
        }
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = { top: 8,  right: 8,  bottom: (hgt+20), left: 40  };
//     width = wid*.8;
}else{
     margin = { top: 5, right: 20, bottom: (hgt*.2), left: 78};
  }  //end  by mayank sh. for display properties

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
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
//                    return autoFormating(d, autoRounding);
                    //            return d;
                });
    }
 var yAxisRight =  d3.svg.trendaxis()
                .scale(y1)
                .orient("right")
                .ticks(customTicks2)
                .tickFormat(function(d, i) {
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,y2AxisFormat,y2AxisRounding,div));
                    }
            else{
                    return numberFormat(d,y2AxisFormat,y2AxisRounding,div);
                }
                }else {
                    return "";
                }
//                    return autoFormating(d, autoRounding);
                    //            return d;
                });   
    
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
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=40;
    }
    var svg = d3.select("#"+div)
   //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(wid +40)+" "+(height + margin.top + margin.bottom+48)+" ")
//            .attr("width",wid)
//            .attr("height", height + margin.top + margin.bottom)
//            .attr("height", hgt-50 )
            .append("g")
            .attr("transform", "translate(" + (margin.left-18) + "," + (margin.top+offset-5) + ")");

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
//                return "gradient" + (d).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
//                if (typeof centralColorMap[d.toLowerCase()] !== "undefined") {
//                    colorShad = centralColorMap[d.toLowerCase()];
//                } else {
//                    colorShad = color(i);
//                }
//                if (typeof localColorMap[d] === "undefined" || localColorMap[d] === "undefined") {
//                    localColorMap[d] = colorShad;
//                }
//                if (typeof chartMap[d] === "undefined") {
//                    chartMap[d] = colorShad;
//                    colorMap[i] = d + "__" + colorShad;
//                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var tmp = color;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        return key !== columns[0];
    }));

    data.forEach(function(d) {
        var y0 = 0;
        d.val = tmp.domain().map(function(name) {
            return {
                name: name,
                viewBy: d[columns[0]],
                value: d[name],
                y0: y0,
                y1: y0 += +d[name]
            };
        });
        d.total = d.val[d.val.length - 1].y1;
    });
    var col = [];
    data.forEach(function(d) {
        col.push(d[columns[0]]);
    });

//    data = data.sort(function(a, b) {
//        return b["total"] - a["total"];
//    });
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

    var max = 0;
var minVal = 0;
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
    }
    else{
       minVal = measArr[0] + diffFactor;
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }

   }
}else{
 //   minVal=0;
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
//           if (data.length > 1) {
//        minVal = minimumValue(data, measArr) * .8;
//    }else{
//        minVal = 0;
//       }
       }



   y.domain([parseFloat(minVal), parseFloat(max)]);

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
//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}
else{
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i);
            })
             .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .attr('transform',function(d,i) {
                return transformation(div,d,i);
            })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
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
            .append("svg:title").text(function(d) {
        return d;
    });
}
//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}
else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .attr("y", 0)
            .attr("x", -9)
            .attr("dy", ".32em")
            .style("text-anchor", "end")
              var ht = parseFloat(hgt)* .65;
          }  //end  by mayank sh. for display properties
if(typeof chartData[div]["displayY1"]!="undefined" && chartData[div]["displayY1"]!="" && chartData[div]["displayY1"]!="Yes"){

}else{  //add condition by mayank sh. for display properties
    svg.append("g")
            .attr("class", "y axis axisRight")
            .attr("transform", "translate(" + (width +20) + ",0)")
            .call(yAxisRight)
            .append("text")
            .attr("y", 10)
            .attr("dy", "-2em")
            .attr("dx", "-6em")
            .attr("fill", "rgb(102, 124, 38)")
            .style("text-anchor", "middle")
            .style("font-weight", "bold");
//            .text(measure2);
}

//    for (var j=0; j <= ht; j=j+50) {
//    svg.append("svg:line")
//        .attr("x1", 0)
//        .attr("y1", j)
//        .attr("x2", width)
//        .attr("y2", j)
//        .style("stroke", "#A69D9D")
//        .style("stroke-width", .5)
//        .style("z-index", "9999");
//};
    var state = svg.selectAll(".state")
            .data(data)
            .enter().append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
               return "translate(" + (x(d[columns[0]])-x(d.x)) + ",0)";
            });
var count = 0;
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
            .attr("x", function(d) {
                return x(d.x);
            })
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
                return col[i];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition,div);
//                     alert("111");
                }
                else {
//                    msrData = addCommas(d.value);
                  msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                }

                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + "</span><br/>";
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
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            });
    
    var valueline = d3.svg.line()
.x(function(d) {
    return (x(d[columns[0]])+x.rangeBand()/2); // add by mayank sh.
    })
    .y(function(d) {
        
   
    return y1(d[measureArray[measureLength]])
    });

var sum =[] ;
for(var z=0;z<measureArray.length;z++){
    var sum1 = d3.sum(data, function(d,i) {
        return d[measureArray[z]];
    });
    sum.push(sum1);
}

state.selectAll("rect1")
.data(function(d) {
    return d.val;
})
.enter()
.append("text")
.attr("transform",function(d,i) {
    var xvalue =x(d.x)+x.rangeBand()/2;
    var yValue = y(d.y1); 
    if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom"){
          return "translate(" + (xvalue) + ", " + ((y(d.y0))-2)+ ")";
               }   //Added by Shivam // update by mayank sh.
                else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnCenter"){
                    return "translate(" + (xvalue) + ", " + ((yValue+((y(d.y0)-yValue)/2))+3) + ")"; 
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop-Bar"){
                    return "translate(" + (xvalue) + ", -2)"; 
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom-Bar"){
                     if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
                   return "translate(" + (xvalue) + ", " + (height+(height/28)+2)+ ")";    
                   }else{
                      return "translate(" + (xvalue) + ", " + (height+(height/30)+2)+ ")";
               }}
               else{
             return "translate(" + (xvalue) + ", " + yValue + ")";
                    
    }
})
 .style("text-anchor","middle")
.text(function(d,i){
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
        if(typeof displayType=="undefined" || displayType==="Absolute"){
            if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
                if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                    if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                        return addCurrencyType(div, measureArray[z])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                    }else {
                        return "";
                    } 
                }else {
                    return addCurrencyType(div, measureArray[z])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                }
            }else{
                if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                    if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                        var percentage = (d.value / parseFloat(sum[i])) * 100;
                        if(percentage<5){
                            return "";
                        }
                        return percentage.toFixed(1) + "%";
                    }else {
                        return "";
                    }
                }else {
                    var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                    if(percentage1<5){
                            return "";
                        }
                    return percentage1.toFixed(1) + "%";
                }
            }
        }else {
            if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='%-wise'){
                if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                    if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                        var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                        if(percentage1<5){
                            return "";
                        }
                        return percentage1.toFixed(1) + "%";
                    }else {
                        return "";
                    }
                }else {
                    var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                    if(percentage1<5){
                            return "";
                        }
                    return percentage1.toFixed(1) + "%";
                }
            }
            else{
                if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                    if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                        return addCurrencyType(div, measureArray[z])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                    }else {
                        return "";
                    }
                }else {
                    return addCurrencyType(div, measureArray[z])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
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

 svg.append("path")
        .data(data)
     .attr("class", "lineDual")
     .attr("d", valueline(data))
//     .attr("transform", "translate(" + 15 + "," + 0 + ")") 
                .style("stroke", getDrawColor(div, parseInt(6)))
     .attr("id", function(d,i){return d[columns[0]] + ":" + d[measureArray[measureLength]];})
     .attr("fill", "transparent")
     .attr("stroke-width", "3")
     .on("click",fun);

  var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return (x(d[columns[0]])+x.rangeBand()/2); // add by mayank sh.
            })
            .attr("cy", function(d) {
                return y1(d[measureArray[measureLength]]);
            })
            .style("fill", function(d,i){
               if (typeof chartData[div]["lineSymbol"] === "undefined" || chartData[div]["lineSymbol"] === "Hollow") {  
                return getDrawColor(div, parseInt(6));
        }else{
            return "white";
        }
            })
            .style("stroke", function(d, i) {
                  return getDrawColor(div, parseInt(6));
            })


            .style("stroke-width", "1px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[measureLength]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
              
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d[measureArray[measureLength]], round, precition);
                }
                else {
                    msrData = addCurrencyType(div, measureArray[measureLength])+addCommas(d[measureArray[measureLength]]);
                }

                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[measureLength] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                count++;
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
            
    for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
//              .text(avg)
               .text(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
        return x(d[columns[0]])
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
            
//      Added by shivam
      
            for(var i=0;i<measureArray.length;i++){   // stackebar targetline
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                        return addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
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
                return x(d[columns[0]]);
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
            var path = svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .attr("x", (width)*.95)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
        }
    }  
//  var sum = d3.sum(data, function(d) {
//        return d[measLine[s]];
//    });

//             //Graph Label
//            for(var z=0;z<measureArray.length;z++){
//                 var sum = d3.sum(data, function(d,i) {
//                     return d[measureArray[z]];
//                    });
//
//             svg.selectAll("labelText")
//             .data(function(d) {
//                return data;
//            }).enter().append("text")
////            .attr("d", valueline(data))
//                  .attr("transform", function(d,i) {
//                   var xvalue = x(d[columns[0]]) + parseFloat(x.rangeBand() / measureArray.length*z) + 17 ;// x(d[measureArray[0]]);
//
//                    var yValue = (y(d[measureArray[z]])) < 250 ? y(d[measureArray[z]]) -4 : y(d[measureArray[z]]) / x(d[columns[0]])  * z -200 ;
//
//                    return "translate(" + xvalue + ", " + (yValue) + ")";
//                })
//                .attr("text-anchor", "middle")
//                .attr("class", "valueLabel")
////                .attr("font-size","8px")
//                .style("font-size", function(d){
////                    return ""+parseInt(10-z)+"px"
//                    return ""+parseInt(8)+"px"
//                })
//                .style("transform-origin","bottom left 0px;")
////                .attr("onclick", hide)
//                .attr("index_value", function(d, i) {
//                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .attr("id", function(d) {
//                    return d[columns[0]] + ":" + d[measureArray[z]];
//                })
//                .attr("fill", function(d,i){
//                    if(color(i)=="#000000" || color(i) == "#3F3F3F"){
//                        if(parseFloat(d[measureArray[z]]) >= max){
//                            return "white";
//                        }
//                    }
//
//                    else {
//                    return labelColor;
//                    }
//                })
//                .text(function(d)
//                {
//                 if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//
//                    if(typeof displayType=="undefined" || displayType==="Absolute"){
//                        if(typeof chartData[div]["hideLabel"] != "undefined"){
//                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][z] == "Yes"){
//                           return addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding));
//
//                }else {
//                    return "";
//                }}else {
//
//                   return addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding));
//                }
////                       return numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding);
//                    }else{
//                       if(typeof chartData[div]["hideLabel"] != "undefined"){
//                        if(typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"][z] == "Yes"){
//                             var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
//                    return percentage.toFixed(1) + "%";
//
//                }else {
//                    return "";
//                }}else {
//                    var percentage1 = (d[measureArray[z]] / parseFloat(sum)) * 100;
//                    return percentage1.toFixed(1) + "%";
//                }
//            }
//            }else {return "";}
//               })
//         .on("mouseover", function(d, i) {
//
//                var meas = [];
//                meas.push(measureArray[z]);
//                if(fromoneview!='null'&& fromoneview=='true'){
//                     show_detailsoneview(d, columns, measureArray, this,chartData,div1);
//                }else{
//                show_details(d, columns, measureArray, this,div);
//                }
//            })
//            .on("mouseout", function(d, i) {
//
//                hide_details(d, i, this);
//            });
//            }
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{ 
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
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
    .attr("x",currX+15)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
   else 
    {
count=0;            
var boxW=wid;
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
rectW = rectW<150?150:rectW;

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
    rectY=boxH-(boxH*1.03)-30;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.28)-(3*measureArray.length);
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
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[0]]!=''){
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
}}
//    showLegends1(measureArray, localColorMap, width, svg);
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
        getSlider1(div,data,wid,hgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}

}    

}
}

function buildOverlaid(div, data, columns, measureArray,width,height){
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
 
var dashletid;
var chartData = JSON.parse(parent.$("#chartData").val());
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    height -= 25; 
}
 var colIds= [];
  var widthdr=width-10;
    var divHgtdr=height
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
// var fun = "drillWithinchart(this.id,\""+div+"\")";
 
 
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
var regid=dashletid.replace("chart","");
        var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
      var oneviewid= parent.$("#oneViewId").val();

 fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
 var olap=dashletid.substring(0, 9);
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
    
//    Added by shivam
            var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range);

//var x = d3.scale.ordinal()
//    .rangeRoundBands([0, width], .2);

var y = d3.scale.linear().domain([minVal1, max1]).range([height, 0]);
//var y1 = d3.scale.linear().domain([minVal2, max2]).range([height, 0]);
//var y = d3.scale.linear()
//    .rangeRound([height, 0]);

//add condition by mayank sh. for hidden white space
var margin = {top: 18, right: 15, bottom: 70, left: 65 };
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = { top: 20,  right: 18,  bottom: 10, left: 20 };
}else{
     margin = {top: 18, right: 15, bottom: 70, left: 65 };
}
 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
          height   = divHgtdr*.90  ;
          var y = d3.scale.linear().domain([minVal1, max1]).range([height+5, 0]);
}else{
    height   = divHgtdr*.76  ;
}
//end condition by mayank sh. for hidden white space


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
//var yCost = d3.scale.linear()
//     .rangeRound([height, 0]);

//var color = d3.scale.ordinal()
//    .range(["#d0743c", "#ff8c00"]);





//    .tickFormat(d3.format(".2s"));

if (isFormatedMeasure) {
     var   yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d) {

                    return numberFormat(d, round, precition,div);
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });
    }
//    var    yAxisRight = d3.svg.trendaxis()
//                .scale(y)
//                .orient("right")
//                .tickFormat(function(d, i) {
//         if(typeof displayY !=="undefined" && displayY =="Yes"){
//                  if(yAxisFormat==""){
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
//                    }
//            else{
//                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
//                }
//                }else {
//                    return "";
//                }
//                });

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
    .x(function(d) {return x(d[columns[0]]) + x.rangeBand() / 2;})
    .y(function(d) {return y(d[measure2]);});
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
var offset=0;
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top'){
     offset=25;
 }   
var svg = d3.select("#" + div)
            .append("svg")
    .attr("id", "svg_" + div)
        .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgtdr+50)+" ")
            .classed("svg-content-responsive", true)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

     y = d3.scale.linear().domain([minVal1, max1]).range([height, 0]).clamp(true);
//     y = d3.scale.linear().domain([minVal2, max2]).range([height, 0]);

      svg.call(yAxis1);
    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
  var  minVal = measArr[0] + diffFactor;
    y.domain([minVal, max1]);

    
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
}//add condition by mayank sh. for display properties

  //add condition by mayank sh. for display properties
svg.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(0," + height + ")")
      .call(xAxis)
    .selectAll("text")
        .attr("dy", ".71em")
         .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
            .attr('transform',function(d,i) {
                return transformation(div,d,i);
            })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
            })
        .text(function(d,i) {
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i);
}    
            })
        .attr('x',function(d,i){   // add by mayank sharma
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
            return 2;
        }else {
            return 9;
        }
    })  


  svg.append("g")
      .attr("class", "y axis")
      .call(yAxis)
      .selectAll("text")
        .attr("dy", ".32em")
        .attr("y", 0)
        .attr("x", -9)
         .style('font-size',function(d,i) {
                return font2(div,d,i);
            });

      
    for(var i=0;i<measureArray.length;i++){  // overlaid bar
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                        return addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
                    }
            else{
                        valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
                }
            })       
            .attr("x",(width)*.90)
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

var maxHgt=0;
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
               if(height - y(d[measure1[0]])>maxHgt){
                        maxHgt=height - y(d[measure1[0]]);
                    }
                return height - y(d[measure1[0]]);
            }) 
            .attr("onclick", fun)
            
            
	//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})
            
      .style("fill", getDrawColor(div, 0))
      .attr("color_value", getDrawColor(div, 0))
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
                    var msrData;

            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));//Added by shivam
//                    alert("3");
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCommas(data[i][measureArray[0]]);
//               alert("1");

                }
            else{
                 msrData = addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));
//                 alert("2");
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
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[0] + " <b>:</b> " + data[i][columns[0]] + " </span><br/>";//Added by shivam
//           content = show_stackDetail(div,d,data,chartData,measureArray,content,i,msrData);
//           return tooltip.showTooltip(content, d3.event);
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
//                }
                hide_details(d, i, this);
            })
//            .on("mouseout", function(d, i) {
//                hide_details(d, i, this);
//            });


    if(typeof chartData[div]["innerLabels"]!="undefined" && chartData[div]["innerLabels"]!="" && chartData[div]["innerLabels"]==="Y")
        {//Added by shivam
            var font=12;
             font=width/39.2;
            font=font>12?12:font;
            font=font<7?7:font;
            // add by maynk sh. for fontsize

             if(typeof chartData[div]["fontsize"]!=="undefined"){
                      font =  chartData[div]["fontsize"];
             }// end by maynk sh. for fontsize

            svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                     return "translate(" + (xvalue+2) + ", " + (height-4) + ")rotate(" + -90 + ")";// add by mayank sh. width chang  for text size
                })
//                .attr("text-anchor", "middle")

//                .attr("class", "valueLabel")
                .attr("style", "font-size:"+font+"px")
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
                .attr("fill", function(d,i){
//           Added by shivam 
             var AxisNameColor="";
                      if (typeof chartData[div]["AxisNameColor"]!=="undefined") {
          AxisNameColor=chartData[div]["AxisNameColor"];
        } else{
            AxisNameColor="#d8d47d";
        }return AxisNameColor;
                })
                .text(function(d)
                {
                        return d[columns[0]];
                });
        }


  // Add the valueline path.
  svg.append("path")
        .data(data)
     .attr("class", "line1")
     .attr("d", valueline(data))
//      .style("fill", getDrawColor(div, 0))
//     .style("stroke", getDrawColor)
     .style("stroke", getDrawColor(div, 6))//add by myk sh. for line color
     .attr("id", function(d,i){return d[columns[0]] + ":" + d[measure2];})
     .attr("fill", "transparent")
     .style("stroke-width", "3")
     .on("click",fun)
	//Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})

     var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return x(d[columns[0]]) + x.rangeBand() / 2;
            })
            .attr("cy", function(d) {
                return y(d[measure2]);
            })
            .style("fill", getDrawColor(div, 1))
            .style("stroke", function(d, i) {
                  return getDrawColor(div, 1);
            })
//                var colorShad;
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
//                }
//                else {
//                    var drilledvalue = parent.$("#drilledValue").val();
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
//                        colorShad = drillShade;
//                    } else {
//                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                        } else {
//                            colorShad = "steelblue";
//                        }
////                return "steelblue";
//                    }
//                }
//                if (typeof chartMap[d[columns[0]]] === "undefined") {
//                    chartMap[d[columns[0]]] = colorShad;
//                    colorMap[i] = d[columns[0]] + "__" + colorShad;
//                }

            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure2];
            })
            .attr("onclick", fun)
    
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .on("mouseover", function(d, i) {
                var measureName;
                if(typeof measureArray[1]!='undefined'){
                    measureName=measureArray[1];
                }
                else{
                    measureName=measureArray[0];
                }
                
                  if(fromoneview!='null'&& fromoneview=='true'){
                      show_details(d, columns, measureArray, this,div1);
                }else{
                var msrData;

            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCommas(numberFormat(data[i][measureName],yAxisFormat,yAxisRounding,div));//Added by shivam
//                    alert("3");
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCommas(data[i][measureName]);
//               alert("1");

                }
            else{
                 msrData = addCommas(numberFormat(data[i][measureName],yAxisFormat,yAxisRounding,div));
//                 alert("2");
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
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + data[i][columns[0]] + " </span><br/>";//Added by shivam
           return tooltip.showTooltip(content, d3.event);
                }
//                show_details(d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
            var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
            if (x.rangeBand() >= 20) {
        svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
//       Added by Shivam
                     var yValue=0;   
                     if(typeof chartData[div]["LabelPos"]==="undefined" || chartData[div]["LabelPos"]==="Top"){
                    yValue = (y(d[measureArray[0]])) < 15 ? y(d[measureArray[0]]) + 14 : y(d[measureArray[0]]);

                     }
                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                      yValue = maxHgt/2;
                   } 
                    else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bar-Center"){
                      yValue = height+((y(d[measureArray[0]])) < 15 ? (-height/2) : (y(d[measureArray[0]])-height)/2)+7;
                   } 
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
						   yValue = height+(height/30+2);  
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
						   yValue = height-2;  
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
						  return "translate(" + xvalue + ", " + (yValue-2) + ")";
                     }
//                  
                    return "translate(" + xvalue + ", " + (yValue-3) + ")";
                })
                .attr("text-anchor", "middle")
                .attr("class", "valueLabel")
                .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
            })
                .attr("onclick", fun)
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
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
                     var msrData;

            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));//Added by shivam
//                    alert("3");
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCommas(data[i][measureArray[0]]);
//               alert("1");

                }
            else{
                 msrData = addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));
//                 alert("2");
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
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[0] + " <b>:</b> " + data[i][columns[0]] + " </span><br/>";//Added by shivam
           return tooltip.showTooltip(content, d3.event);
           
//                show_details(d, columns, measureArray, this,div);
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

for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
//              .text(avg)  // add by mayank sharma
                 .text(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
        return x(d[columns[0]])
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

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    var boxW=width + margin.left + margin.right;;
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
    if(i==1){
        svg.append("g")
        .append("line")
        .attr("x1",currX-3)
        .attr("y1",(currY-12)+(8/2))
        .attr("x2",currX+8+3)
        .attr("y2",(currY-12)+(8/2))
        .style("stroke",function(){
            return getDrawColor(div, parseInt(i))
        })
        .style("Stroke-width","2px");
    }
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
    .attr("x",currX+12)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
    if(i==1){
        break;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);
d3.select("#"+div+"_mainRect").attr("width",maxX); 
    }
else{
 var count=0;
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
var len=maxMeasure.length;
if(columns[0].length+2>len){
    len=columns[0].length+2;
}

var rectW=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectW=boxW-40;
}
else{
    rectW=30+(len*7)+85;
}
rectW = rectW<150?150:rectW;

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
    rectX=0;
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
    rectY=boxH-(boxH*1.03)-32;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.28)-(3*measureArray.length);
}

var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
}
//alert((boxH-(boxH*.98)-5)+":"+boxW);

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
      if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"]!=='' && chartData[div]["lbPosition"] === "top"){
      }
      else{
        if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        }else{
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
if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"]!=='' && chartData[div]["lbPosition"] === "top"){
    var xStep=rectW/3.3;
    var yStep=15;
    
for(var i=0 ;i <= measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
}
        var measureName1='';
   svg.append("g")
            .attr("class", "y axis")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+20+(xStep*(i%3)))
            .attr("y",(rectY+12+(yStep*parseInt(i/3))))
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
            measureName1=chartData[div]["measureAlias"][measureArray[i-1]];
            if(measureName1==='undefined'){
                measureName1=measureArray[i-1];
            }
        }else{
            measureName1=checkMeasureNameForGraph(measureArray[i-1]);  // dual axis bar
        }
          
           var length=0;  // add for measure length by mayank sh.
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
               if(i==0){
                   return columns[0];
               }
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
            .attr("x",rectX+7+(xStep*(i%3)))
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
   svg.append("g")
            .attr("class", "y axis21312")
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
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!=''){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=measureArray[i];
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
            if(i==1){
                svg.append("g")
                .append("line")
                .attr("x1",rectX+10-3)
                .attr("y1",(rectY+12+viewByHgt+count*15)+(rectsize/2))
                .attr("x2",rectX+10+rectsize+3)
                .attr("y2",(rectY+12+viewByHgt+count*15)+(rectsize/2))
                .style("stroke",function(){
                    return getDrawColor(div, parseInt(i))
                })
                .style("Stroke-width","2px");
            }
              count++
   if(i==1){
        break;
    }
    }}}}

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
        getSlider1(div,data,width,height,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}

}    

}
}

    function buildMultiAxisBar(div, data, columns, measureArray, divWid, divHgt) {
  
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
//     divHgt *=0.8

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


   var fromoneview=parent.$("#fromoneview").val()
if(fromoneview!='null'&& fromoneview=='true'){
}
else{
//divWid=parseFloat($(window).width())*(.40);
}
    var measure1 = measureArray[0];
    var measure2;
    if (measureArray.length === 1) {
        measure2 = measureArray[0];
    } else {
        measure2 = measureArray[1];
    }
     var chartData = JSON.parse(parent.$("#chartData").val());
   
var dashletid;
var prop = graphProp(div);
 var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1;
var colIds=chartData[div1]["viewIds"];
}else{
var prop = graphProp(div);
   var colIds=chartData[div]["viewIds"];
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
        divHgt-=30
    }
    var margin = {
        top: 20,
        right: 80,
//        bottom: 55,
        bottom: (divHgt * .3),
        left: 72
    };
var lbOffset=25;
var leftM=0,rightM=0,topM=0,bottomM=0;
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){
    topM=-1*lbOffset;
}
else{
    topM=-10;
}
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
    var marginleft1 = 50; // add by mynk sh.
    var width = divWid - 70;
     var height = divHgt * .8;
//     var height = divHgt - margin.top - margin.bottom;
//      var fun = "drillWithinchart(this.id,\""+div+"\")";

 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
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

       var hide = "hideLabels(this.id,\""+div+"\")";
    
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
// Added by shivam
     var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range); 
    
var max = 0;
var minVal = 0;
var multiple=0;
if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    multiple=1;
}else{
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

//}
//end of default min
if(data.length==1){
    minVal=0;
}
var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);


//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
   y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);
   y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);
}
    var yAxisLeft, yAxisRight;
    if (isFormatedMeasure) {
        yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });
    } else {
        yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d, i) {
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d, i) {
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });
    }
//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
          height   = divHgt * .80  ;
 }else{
          height   = divHgt*.80  ;
}
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]=="No"){
     margin = { top: 25,  right: 00,  bottom: divHgt*.46, left: 47  };
}
 //end condition by mayank sh. for hidden white space

   if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_x_axis = function() {
    return d3.svg.gridaxis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}

 make_y_axis = function() {
    return d3.svg.gridaxis()
            .scale(y0)
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
        .scale(y0)
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
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
 var offset=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top' ){
     offset=20;
 }
 
    var svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid+20+rightM)+" "+(height + margin.top + margin.bottom -15)+" ")
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");


    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

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
        )}
//add condition by mayank sh. for display properties
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
             .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .attr('transform',function(d,i) {
                return transformation(div,d,i);
            })
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
            })
            .text(function(d,i) {
    if(typeof chartData[div]["displayX"]==="undefined" || chartData[div]["displayX"]==="" || chartData[div]["displayX"]=="Yes"){
return buildXaxisFilter(div,d,i);
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
    });

//add condition by mayank sh. for display properties
    if(typeof chartData[div]["displayY"]==="undefined" || chartData[div]["displayY"]==="" || chartData[div]["displayY"]=="Yes"){
    svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
        }//end by mynk sh.

if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    svg.append("line")
    .attr("x1",0)
    .attr("y1",y0(0))//so that the line passes through the y 0
    .attr("x2",width)
    .attr("y2",y0(0))//so that the line passes through the y 0
    .style("stroke", "black");
    svg.append("g")
    .attr("class", ".x axis")
    .attr("transform", "translate(0," + height + ")")
}

 for(var i=0;i<measureArray.length;i++){   // smooth line
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                        return addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
                    }
            else{
                        valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
                }
            })       
            .attr("x", (width)*.90)
            .attr("y", y0(parseInt(target))-12)
            .attr("style","font-size:8px");
            
             svg.append("text")       
            .attr("x", (width-labelLen*5.2))
            .attr("y", y0(parseInt(target))-5)
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
                return y0(parseInt(target));
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



var len = 0;
var yvalue = -75;
var dyvalue = ".41em";
var count = 0;
var transform = "";
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
var fontSSize=parseInt(($("#div" + div).width())*.004)/2;

if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(fontSSize>.5 ){
fontSSize=9;
}else if( fontSSize<=.5 ){
fontSSize=6;
}
var step=0;
if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Left"){
    transform="rotate(270)";
    len=height*1.3;
    step=height/2.5;
    yvalue=-70;
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
    yvalue=(height + margin.top + margin.bottom+ 17.5 )-((height + margin.top + margin.bottom+ 17.5 )/2.85);
}
else if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Top")){
    len=width*.01;
    step=width/5;
    yvalue=-5;
}

//add by mayank sh. for by default display one measure on chart
for(var k=0;k<measureArray.length;k++){
    if(typeof chartData[div]["chartType"]!="undefined" && (chartData[div]["chartType"]==="StackedBar")){} else{
   if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
   if(k==0){
    if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "Yes")) {
        continue;
    }}else{
    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "No") {
        continue;
    }} //end by mayank sh.
}
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
            .attr("x", function(d) {
                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo*k));
            })
            .attr("width", x.rangeBand() / measNo)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[k]]+":"+k;
            })
            .attr("onclick", fun)
    
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("y", function(d) {
                if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
                    if(y0(0) > y0(d[measureArray[k]]))
                return y0(d[measureArray[k]]);
                    else
                        return y0(0);
                }
                else{
                    return y0(d[measureArray[k]]);
                }
            })
            .attr("height", function(d, i, j) {
                if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
                    return Math.abs(y0(0) - y0(d[measureArray[k]]));
                }
                else{
                return height - y0(d[measureArray[k]]);
                }
            })
            .attr("fill", function(d,i) {
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
		return getDrawColor(div, parseInt(k));
                }

            })
//            .attr("fill", function(d, i) {
//
//return getDrawColor(div, parseInt(k));
//            })
            .attr("class", function(d, i) {
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
                    return "bars-Bubble-index-" + measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                }
                else{
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
                }
            else{

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
        }
        else {
            msrData = addCurrencyType(div, measureArray[i])+addCommas(d[measureArray[i]]);
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
//                }
                hide_details(d, i, this);
            })

//            .on("mouseover", function(d, i) {
//
//                var meas = [];
//                meas.push(measureArray[k]);
//                show_details(d, columns, measureArray, this,div);
//            })
//            .on("mouseout", function(d, i) {
//
//                hide_details(d, i, this);
//            });
            }

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            var boxW=(divWid+20+rightM);
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
    .attr("id",measureArray[i].replace(/[^a-zA-Z0-9]/g, '', 'gi'))
    .attr("x",currX+15)
    .attr("y",currY-4)
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    });
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
   else 
    {
            var boxW=(divWid+20+rightM);
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
            .attr("transform", transform)
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
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
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
            .on("mouseover",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);

                selectedBar.style("fill", drillShade);
            })
            .on("mouseout",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);
            });
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
    }
            //Graph Label
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
                   var xvalue = parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measureArray.length*z)) +(x.rangeBand()/measureArray.length/2);// x(d[measureArray[0]]);
        var yValue = (y0(d[measureArray[z]])) < 15 ? y0(d[measureArray[z]]) + 14 : y0(d[measureArray[z]]) -18;
           if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom"){
              return "translate(" + xvalue + ", " + (height*.95) + ") rotate(270)"; // add by mayank sh. for label display
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnTop-Bar"){
           return "translate(" + xvalue + ", -2) rotate(270)";  
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom-Bar"){
           return "translate(" + xvalue + ","+(height+22)+") rotate(270)";  
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnCenter"){
           return "translate(" + xvalue + ","+(height+((y0(d[measureArray[z]])-height)/2))+") rotate(270)";  
                     }else{
          return "translate(" + xvalue + ", " + (yValue+2) + ") rotate(270)";
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
                 if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
         if (percentage <= 5.0){
                       return "";
                     }else{ 
                    if(typeof chartData[div]["dataDisplayArr"]=== "undefined" || typeof chartData[div]["dataDisplayArr"][measureArray[z]] == "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='Absolute'){
                           return addCurrencyType(div, measureArray[z])+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                }else {
                            return percentage.toFixed(1) + "%";
                        }
                    }else{
                    return "";
                        }
                    } }else{
                            return "";
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
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
              .text(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.9)
              .attr("y", y0(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
         if(i==0){
               return x(d[columns[0]]) - x(0);
               }else{
                   return x(d[columns[0]]) + x(i.length);
               }
            })
            .y(function(d) {
                return y0(parseInt(avg));   
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
                     
                     
function buildGroupedstackedBarLine(div,data, columns, measureArray,wid,hgt) {
    var colorSet = d3.scale.category10();
    
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
    
    var dataLine = [];
var measure1 = measureArray[1];
if(measure1 ==="undefined"){
    measure1 = measureArray[0];
}
//    for(var i =0; i<(data.length < recordLine ? data.length : recordLine);i++){
//        dataLine.push(data[i]);
//    }
    graphProp(div);
//    alert(JSON.stringify(data))
//    
//    var test1 = [];
//    for(var i=0;i<data.length;i++){
//        var test = {};
//        alert(data[i][columns[0]])
//        if(data[i][columns[0]].trim()=="KIT"){
//        test[columns[0]] = data[i][measureArray[1]];
//    test1.push(test);
//        }
//    }
//    alert(JSON.stringify(test1))
 for(var l=0;l<(data.length );l++){
    var colMap={};

     if(typeof colMap[data[l][columns[0]]]=="undefined" && dataLine.indexOf(data[l][columns[0]]) ===-1){

colMap[columns[0]] = data[l][columns[0]];
colMap[measure1] = data[l][measure1];

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
 
     var chartData = JSON.parse(parent.$("#chartData").val());
     var mainWidth = wid;
//    wid=parseFloat($(window).width())*(.75);;
    var measure = measureArray[0];
   
  
    var chartColorMap = {};
    var measArr = [];
    var autoRounding;
     var customTicks = 5;

   if(fromoneview!='null'&& fromoneview=='true'){
      }else{
    var customTicks = 5; 
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
        autoRounding = columnMap[measure]["rounding"];
    } else {
        autoRounding = "1d";
    }}

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
    
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150;
    var yAxis;
    var yAxisRight;
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
var minVal1 = minimumValue(summArr, measure1);
var max1 = maximumValue(summArr, measure1);
    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);
    var x1 = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

    var y = d3.scale.linear()
            .rangeRound([height, 0]);
 var y1 = d3.scale.linear().domain([minVal1, max1]).range([height, 0]);
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
            return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
//                    return autoFormating(d, autoRounding);
                    //            return d;
                });
                
                 yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding2);
//                    return addCommas(d);
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
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
             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(hgt )+" ")
//         .attr("viewBox", "0 0 "+(mainWidth*1.8)+" "+(hgt)+" ")
//            .attr("width",mainWidth*1.8)
//            .attr("height", hgt )
            .attr("style","float:left")

            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top+ ")");
    var tmp = colorSet;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        if(typeof columns[1] === "undefined" || columns[1] === ""){
        return key === columns[0];
        }else {
        return key === columns[1];
        }
    }));

    data.forEach(function(d,i) {
        var y0 = 0;
        d.val = tmp.domain().map(function(name) {
            return {
                name: d[name],
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
    var col = [];
    data.forEach(function(d) {
        col.push(d[columns[1]]);
    });

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    x1.domain(summArr.map(function(d) {
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
 //   minVal=0;
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
   if (data.length > 1) {
        minVal = minimumValue(data, measArr) * .8;
    }else{
        minVal = 0;
    }
    }
   y.domain([parseFloat(minVal), parseFloat(max)]);

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
            return 20;   
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
            .text(function(d) {
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                if (d.length < 1)    //grupstckedbar
                    return d;
                else
	  if (typeof chartData[div]["editXLable"] !== "undefined" && chartData[div]["editXLable"] !== "") {
                  return d.substring(0, chartData[div]["editXLable"]);
		}else{
                    return d.substring(0, 10) + "..";}
    }
            })
     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            .text("" + columns[0] + "");
              var ht = parseFloat(hgt)* .65;
     }
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
   svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-(width), 0, 0)
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
            .attr("x", function(d) {
                return x(d.x);
            })
            .style("fill", function(d,i) {
            if(typeof chartColorMap[d.name]==="undefined"){
            chartColorMap[d.name]=colorSet(d.index);
            return colorSet(d.index);
            }else{
             return chartColorMap[d.name];
            }
            })
            .attr("id", function(d) {
                    return data[d.index][columns[0]]+":"+d.name + ":" + d.value;
                })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("index_value", function(d, i) {
        return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
            .attr("color_value", function(d, i) {
                if(typeof chartColorMap[d.name]==="undefined"){
            chartColorMap[d.name]=colorSet(d.index);
            return colorSet(d.index);
            }else{
             return chartColorMap[d.name];
            }
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                }
                else {
                    msrData = addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(d.value);
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

                   var yvalue=0;
		var rectyvalue=0;
		var yLegendvalue=0;
		var rectyvalue1=0;
		var len = parseInt(mainWidth-150);
		var rectlen = parseInt(mainWidth-200);
                
                var fontsize = parseInt(mainWidth/45);
		var fontsize1 = parseInt(mainWidth/52);
		var rectsize = parseInt(height/30);
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
               if(keys1.length>7 && keys1.length<12){

        yvalue = parseInt((height * .17));
        yLegendvalue = parseInt((height * .10));
        rectyvalue = parseInt((height * .15));
}else if(keys1.length>13){

        yvalue = parseInt((height * .09));
        yLegendvalue = parseInt((height * .017));
        rectyvalue = parseInt((height * .07));
}
else{

     yvalue = parseInt((height * .47));
        yLegendvalue = parseInt((height * .4));
        rectyvalue = parseInt((height * .45));
}
  if(fontsize1>13){
                  fontsize1 = 13;
                }else if(fontsize1<9){
                  fontsize1 = 9;
                }
                
 if (fontsize>13){
     fontsize = 13;
   }else if(fontsize<9){
                  fontsize = 9;
                }
        var count = 0;
        var transform =

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
.attr("transform", "translate(" + (mainWidth*.74-60)  + "," + (yLegendvalue+10) + ")")

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
            .attr("transform", "translate(" + (mainWidth*0.745-55)   + "," + (rectyvalue+height/38) + ")")
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
            .attr("transform", "translate(" + (mainWidth*0.77-60)   + "," + (yvalue+height/30) + ")")
            .attr("fill", function(){
                 return chartColorMap[keys1[m]]
            })
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(){
                return keys1[m];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(){

                if(keys1[m]!="undefined"){
if(keys1[m].length>25){
                    return keys1[m].substring(0, 25);
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
            }}
        
          var valueline = d3.svg.line()
            .x(function(d) {
                
                return x1(d[columns[0]]) + x.rangeBand()/ 2;
            })
            .y(function(d) {

                return y1(d[measure1]);
            });
  var path = svg.append("path")
            .data(summArr)
            .attr("d", valueline(summArr))
            .attr("fill", "transparent")
            .style("z-index", "0")
            .style("stroke-dasharray",function(d){    // for dash line by mynk sh.
            //alert(chartData[div]["lineType"]);
        if(typeof chartData[div]["lineType"]!=="undefined" && chartData[div]["lineType"]==="dashed-Line"){
            return "6,3";
        }
        else{
            return "1,0";
        }}
            )  // for dash line by mynk sh.
            .attr("stroke", function(d, i) {

                  return color(0);


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
 var sum1 = d3.sum(data, function(d) {
        return d[measure1];
    });
//var c=0;
           svg.selectAll("labelText")
             .data(summArr).enter().append("text")
//            .attr("d", valueline(data))
                   .attr("x", function(d){
//                        var len=Object.keys(data).length;
//                       c++;
//                       if(c==len)
//                       {
//                            return x1(d[columns[0]]) - 40;
//                       }
//                       else
//                       {
                            return x1(d[columns[0]]) + x.rangeBand()/2;
//                       }
                   } ).attr("y", function(d){
                       return y1(d[measure1]) +8;
                   })

      .attr("dy", ".35em")
            .text(function(d)
                {
                  
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){

                        if(typeof chartData[div]["showPercent"] !=="undefined" && chartData[div]["showPercent"] =="Y"){
                       return numberFormat(d[measure1],yAxisFormat,yAxisRounding,div) + "%";
                        }else {
                             return numberFormat(d[measure1],yAxisFormat,yAxisRounding,div);
                        }
                    }else{
                     var percentage = (d[measure1] / parseFloat(sum1)) * 100;
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
            .data(summArr)
            .enter().append("circle")
            .attr("r", function(d){
                var radius;
//                var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
//                        radius = "8";
//                    }else{
                   radius = "4"
//                    }
                    return radius;
            })
            .attr("cx", function(d) {
                return x1(d[columns[0]]) + x.rangeBand()/2;
            })
            .attr("cy", function(d) {
                return y1(d[measure1]);
            })
             .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .style("fill", function(d,i){
                var colorShad;
//                var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
//                        colorShad = drillShade;
//                    }else{
//                        if(d[measure1] < 550){
//                   colorShad = color(1);
//               }else {
                   colorShad = color(0);
//               }

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
               var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += d.data.label + ": <span style='color:orangered'>" + d.data.score + DATA"</span>";
                   content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
//                content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " + addCommas(d.data.weight) + "</span><br/>";
                content += "<span class=\"name\"> " + measure1 + ":</span><span class=\"value\"> " + addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(d[measure1]) + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
                
//                show_details(d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
         
}              

function buildGroupedstackedBarPerCent(div,data, columns, measureArray,wid,hgt) {
    var colorSet = d3.scale.category10();
    
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
    
     var chartData = JSON.parse(parent.$("#chartData").val());
     var mainWidth = wid;
//    wid=parseFloat($(window).width())*(.75);;
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
    }
    else{
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
    }}

    var isDashboard = parent.$("#isDashboard").val();
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
    }else {
        drillWithinchart(id1,div);
        }   
	}

  
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150
    
//Added by shivam
if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes") {
     if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    var yAxis;
    var margin = {
        top: 20,
//        right: 0,
        right: (wid*.23),
    //    bottom: 70,
		bottom:(hgt*.3),
        left: 50
    },
    width = wid -50 - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom-30;
}else{
   var yAxis;
    var margin = {
        top: 20,
//        right: 0,
        right: (wid*.10),
    //    bottom: 70,
		bottom:(hgt*.3),
        left: 50
    },
    width = wid -50 - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom-30;
}
} else{
   var yAxis;
    var margin = {
        top: 20,
//        right: 0,
        right: (wid*.10),
    //    bottom: 70,
		bottom:(hgt*.3),
        left: 50
    },
    width = wid -50 - margin.left - margin.right,
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
                .rangeRoundBands([0, width], range);

//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, width], .1);

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
                   
//                alert(d)
//                var axisValue =0;    
//                axisValue = parseInt(max /100);
//            return numberFormat((d /axisValue),"","0",div) + "%";
//                    return autoFormating(d, autoRounding);
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
    
    if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes"){
        if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    var svg = d3.select("#"+div)
            .append("svg")
            .attr("id","svg_"+div)
             .attr("viewBox", "0 0 "+((width*.76) + margin.left+40 + margin.right)+" "+(hgt )+" ")
            .attr("style","float:left")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    }    else{
         var svg = d3.select("#"+div)
            .append("svg")
            .attr("id","svg_"+div)
.attr("viewBox", "0 0 "+(width+85)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")             
            .attr("style","float:left")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    }
    }else{
         var svg = d3.select("#"+div)
            .append("svg")
            .attr("id","svg_"+div)
.attr("viewBox", "0 0 "+(width+65)+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")             
            .attr("style","float:left")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    }
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
        var v=(parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]);
        if(typeof maxBlockHeight[colName]==='undefined' || v>maxBlockHeight[colName]){
            maxBlockHeight[colName]=v;
        }
//alert(JSON.stringify(d)+"::"+i+"::"+(parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]))    
//alert((parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]))    
        
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
//alert(JSON.stringify(d)+"::"+i+"::"+(parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]))    
//alert((parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]]))    
            return {
                name: d[name],
                name1:d[columns[0]],
                viewBy:name,
                value: d[measureArray[0]],
                y0: y0,
//                y1: y0 += (((parseFloat(d[measureArray[0]])/parseFloat(totalVal)) * height) ),
                y1: y0 += ((parseFloat(height) / parseFloat(totalVal)) * parseFloat(d[measureArray[0]])*(100/maxBlockHeight[colName])),
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
 //   minVal=0;
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
 if (data.length > 1) {
        minVal = minimumValue(data, measArr) * .8;
    }else{
        minVal = 0;
    }
    }
   y.domain([0, 100]);

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
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                 return buildXaxisFilter(div,d,i)
    }
            })
     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
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
            .tickSize(-(width), 0, 0)
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
            .attr("x",0)  // Edit by shivam
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
            .attr("id", function(d) {
                    return data[d.index][columns[0]]+":"+d.name + ":" + d.value;
                })
            .attr("onclick", fun)
    //Added by shivam
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
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                }
                else {
                    msrData = addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(d.value);
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
//                    if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[d["name"]]!='undefined'){
//                        yAxisFormat=numberFormatArr[d["name"]];
//                    }
//                    else{
//                        yAxisFormat="";
//                    }
//                    if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[d["name"]]!='undefined'){
//                        yAxisRounding=numberRoundingArr[d["name"]];
//                    }
//                    else{
//                        yAxisRounding="0";
//                    }
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
              return "9px";
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
		var fontsize1 = parseInt(mainWidth/60);
		var rectsize = parseInt(height/35);
                rectsize=rectsize>10?10:rectsize;
                var legendLength;
                var keys1 =  Object.keys(chartColorMap);
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

   if(fontsize1>11){
                  fontsize1 = 10;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
  if(fontsize>12){
                  fontsize = 11;
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
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
.attr("transform", "translate(" + (mainWidth*.74-50)  + "," + (yLegendvalue) + ")")

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
            if (width>600){
             if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
//            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (mainWidth*0.745-55)   + "," + (rectyvalue-4)*.89 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                  return  chartColorMap[keys1[m]];
            })
             }
            }
else{
     if(columns[0]!==columns[1] && typeof columns[1]!="undefined" && columns[1] != null){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
//            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (mainWidth*0.745-77)   + "," + (rectyvalue-4)*.89 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
                  return  chartColorMap[keys1[m]];
            })
             }
}
if(width>600){
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + (mainWidth*0.745-40)   + "," + (yvalue-8)*.89 + ")")
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
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(){
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
         //   .style("font-size",""+fontsize+"")
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
  
}else{
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + (mainWidth*0.745-64)   + "," + (yvalue-8)*.89 + ")")
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
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(){
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
        var fontsize1 = parseInt(mainWidth/60);
        var rectsize = parseInt(height/33);
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
            yvalue*=1.09;
var chartWid=(mainWidth+ margin.left + margin.right)-(mainWidth+ margin.left + margin.right)/12;
 chartWid*=.75;
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
                return chartColorMap[keys1[m]];
            })

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthvalue-5)  + "," + (yvalue-1) + ")")
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

function buildDualAxisBar(div, data, columns, measureArray, divWid, divHgt,flag,divHtmlId) {

//Added by shivam
// if (typeof data!== 'undefined' && data.length==1) {
//return buildMultiAxisBar(div, data, columns, measureArray, divWid, divHgt)
//// 
//}
      var color = d3.scale.category10();
var customTicks = 5;
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
    
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var customTicks2 = 5;
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks1"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks1"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks2 = chartData[div]["yaxisrange"]["axisTicks1"];
 }
 if(typeof customTicks2=='undefined' || customTicks2==='undefined'){
     customTicks2=5;
 }
 var measLength1= chartData[chartData[div]["measures1"]];
 var measLength2 = chartData[chartData[div]["measures2"]];
 if(typeof chartData[div]["measures1"]!= "undefined" && typeof measLength1!=="undefined" && measLength1.length>0){
 measure1 = chartData[div]["measures1"][0];

 for(var i in chartData[div]["measures1"]){

 measuretemp1.push(chartData[div]["measures1"][i])
 temp.push(chartData[div]["measures1"][i])

 }

 }
 else{

  measure1 = measureArray[0];
 }
 if(typeof chartData[div]["measures2"] != "undefined" && typeof measLength2!=="undefined" && measLength2.length>0){
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
    }else{
        drillWithinchart(id1,div);
        }   
	}

    var hide = "hideLabels(this.id,\""+div+"\")";
    
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
//Added by shivam    
   var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range);

//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, width], .2);

     var max = 0;
var minVal = 0 ;
var y0;
var y1;
//    if(typeof chartData[div]["measures1"]!= "undefined" ){
//measureArray 1
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
if(measuretemp1.length>0){
for(var i in measuretemp1){
var max1 = parseFloat(maximumValue(data, measuretemp1[i]));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
}
}else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measure1));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
}
}}
}
else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measure1));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
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
var min = parseFloat(minimumValue(data, measure1));
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
if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }else{
minVal = 0;
}
}
//add condition by mayank sh. for hidden white space
 y0 = d3.scale.linear().domain([minVal, max]).range([height, 0]).clamp(true);
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
    y0 = d3.scale.linear().domain([minVal, max]).range([height+25, 0]).clamp(true);
}
  //end  by mayank sh. for hidden white space
// end measureArray 1

 
//measureArray 2

if(typeof chartData[div]["y1axisrange"]!="undefined"&& chartData[div]["y1axisrange"]!="") {
    if(typeof chartData[div]["y1axisrange"]["axisMax1"]!="undefined" && chartData[div]["y1axisrange"]["axisMax1"]!="" && chartData[div]["y1axisrange"]["axisMax1"]!=0) {
    max = parseFloat(chartData[div]["y1axisrange"]["axisMax1"]);
}else{
if(measuretemp2.length>0){
for(var i in measuretemp2){
var max1 = parseFloat(maximumValue(data, measuretemp2[i]));
var min = parseFloat(minimumValue(data, measuretemp2[i]));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
}
}else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measure2));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
}
}}
}
else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measure2));
if(i==0)  {
max=parseFloat(max1);
}
else if(i>0 && (max1 > max)){
max = max1;
}
}
   }
//end of default max

 if(typeof chartData[div]["y1axisrange"]!="undefined" && chartData[div]["y1axisrange"]!="") {
 if(typeof chartData[div]["y1axisrange"]["axisMin1"]!="undefined" && chartData[div]["y1axisrange"]["Y1axisRangeType"]!="MinMax" && chartData[div]["y1axisrange"]["Y1axisRangeType"]!="Default" && chartData[div]["y1axisrange"]["axisMin1"]!="") {
  minVal = parseFloat(chartData[div]["y1axisrange"]["axisMin1"]);
 }else if(chartData[div]["y1axisrange"]["Y1axisRangeType"]=="Default" ){
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
if (data.length > 1) {
        minVal = minimumValue(data, measure2) * .8;
    }else{
minVal = 0;
}
}
//add condition by mayank sh. for hidden white space
y1 = d3.scale.linear().domain([minVal, max]).range([height, 0]).clamp(true);
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
    y1 = d3.scale.linear().domain([minVal, max]).range([height+25, 0]).clamp(true);
}

// end measureArray 2

 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
                height   = divHgt*.86;
 }

 if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]=="No"){
     margin = { top: 10,  right: 18,  bottom: 30, left: 20  };
}else{
    margin = {top: 25, right: 65, bottom: 30, left: 47 };
  }

  if(typeof chartData[div]["displayY1"]!="undefined" && chartData[div]["displayY1"]!="" && chartData[div]["displayY1"]=="No"){
     margin = { top: 10,  right: 18,  bottom: 30, left: 20  };
}else{
    margin = {top: 25, right: 70, bottom: 30, left: 47 };
  }
//end condition by mayank sh. for hidden white space
 if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
make_x_axis = function() {
    return d3.svg.trendaxis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}
 make_y_axis = function() {
    return d3.svg.trendaxis()
        .scale(y0)
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
        .scale(y0)
        .orient("left")
        .ticks(customTicks)
}
}
 make_y2_axis = function() {
    return d3.svg.axis()
        .scale(y1)
        .orient("right")
        .ticks(customTicks2)
}
if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
   var xAxis = d3.svg.tinaxis()
            .scale(x)
            .orient("bottom");
           }else{
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
            }

    // create left yAxis
    var yAxisLeft, yAxisRight;
    if (isFormatedMeasure) {
        yAxisLeft = d3.svg.tinaxis().scale(y0) .ticks(customTicks).orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });

        yAxisRight = d3.svg.tinaxis().scale(y1).ticks(customTicks2).orient("right")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });
    } else {
        yAxisLeft = d3.svg.tinaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding1);
//                return addCommas(d);
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });

        yAxisRight = d3.svg.tinaxis().scale(y1).ticks(customTicks2).orient("right")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding2);
//                    return addCommas(d);
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,y2AxisFormat,y2AxisRounding,div));
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
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top' ){
     offset=height/11;
 }
 if(chartData[div]["chartType"]==="Influencers-Impact-Analysis2"){
       var divId;
 if(typeof divHtmlId!='undefined' && divHtmlId!==''){
    divId=divHtmlId;
}
  var  svg = d3.select("#" + divId)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "-20 0 "+(divWid + margin.left + (margin.right+40))+" "+(height + margin.top + margin.bottom +height)*.89+" ")
            .classed("svg-content-responsive", true)
			//.attr("width", divWid+20)
           // .attr("height", height + margin.top + margin.bottom +5)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + (margin.left +10)+ "," + ((margin.top+offset+10)*2) + ")");
 } else if(chartData[div]["chartType"]==="Veraction-Combo3"){
       var divId;
 if(typeof divHtmlId!='undefined' && divHtmlId!==''){
    divId=divHtmlId;
}
  var  svg = d3.select("#" + divId)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "-20 70 "+(divWid + margin.left + (margin.right+40))+" "+(height + margin.top + margin.bottom +height)*.89+" ")
            .classed("svg-content-responsive", true)
			//.attr("width", divWid+20)
           // .attr("height", height + margin.top + margin.bottom +5)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + (margin.left +10)+ "," + ((margin.top+offset+10)*2) + ")");
 }else{
  var  svg = d3.select("#" + div)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "-20 0 "+(divWid + margin.left + (margin.right+40))+" "+(height + margin.top + margin.bottom +height/3)+" ")
            .classed("svg-content-responsive", true)
			//.attr("width", divWid+20)
           // .attr("height", height + margin.top + margin.bottom +5)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + (margin.left +10)+ "," + (margin.top+offset+10) + ")");
 }
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

    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

if(fromoneview!='null'&& fromoneview=='true'){
div=parent.$("#chartname").val()
 }


 //add condition by mayank sh. for display properties
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
        )}

if(typeof chartData[div]["GridLines1"]!="undefined" && chartData[div]["GridLines1"]!="" && chartData[div]["GridLines1"]!="Yes"){}else{
svg.append("line")
        .attr("x1",width)
        .attr("y1",height)
        .attr("x2",width)
        .attr("y2",0)
        .style("stroke", "#E1DEDE");
}
//add condition by mayank sh. for display properties

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
                if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
           }else{
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
    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .attr('transform',function(d,i) {
                return transformation(div,d,i);
    })      
    .style('text-anchor',function(d,i) {
                return textAnchor(div,d,i);
    });

if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
}else{ //add condition by mayank sh. for display properties
   svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
}
if(typeof chartData[div]["displayY1"]!="undefined" && chartData[div]["displayY1"]!="" && chartData[div]["displayY1"]!="Yes"){
}else{  //add condition by mayank sh. for display properties
    svg.append("g")
            .attr("class", "y axis axisRight")
            .attr("transform", "translate(" + (width) + ",0)")
            .call(yAxisRight)
            .selectAll('text')
            .attr("y",0)
            .attr("x",9)
            .attr("dy", ".32em")
            .style('text-anchor','start')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
} //end  by mayank sh. for hidden white space

    for(var i=0;i<measureArray.length;i++){ // dual axis bar
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
           svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                        valueLen=(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                        return addCurrencyType(div, measureArray[i])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
                    }
            else{
                        valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
                }
            })       
            .attr("x", (width)*.95)
            .attr("y", y0(parseInt(target))-12)
            .attr("style","font-size:10px");

             svg.append("text")       
            .attr("x", (width-labelLen*6.2))
            .attr("y", y0(parseInt(target))-5)
            .attr("style","font-size:10px")
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
                return y0(parseInt(target));
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
//              .text(avg)
               .text(addCurrencyType(div, measureArray[i])+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.90)
              .attr("y", y0(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
        return x(d[columns[0]])
            })
            .y(function(d) {
                return y0(parseInt(avg));   
           } )
           
     	  var path =   svg.append("path")
            .data(data)
            .attr("d", Averageline(data))
            .attr("fill", "transparent")
            .attr("x", (width)*0.95)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
     }    
            }
var count = 0;
var measBar = [];
var measLine = [];
var chartList = [];
var chartListSize = chartData[div]["chartList"];
if(typeof chartData[div]["chartList"]!="undefined" && chartListSize.length>0){
    for(var l in chartData[div]["chartList"]){
      if(chartData[div]["chartList"][l]==="Line"){
      count++;
      measLine.push(measureArray[l])
      chartList.push(chartData[div]["chartList"][l])
  }
else{
    measBar.push(measureArray[l])
    chartList.push(chartData[div]["chartList"][l])
}
}}

var measNo1=measureArray.length;
measNo1 = (parseFloat(measNo1)-parseFloat(count));

    if(typeof chartData[div]["chartList"]!="undefined" && chartListSize.length>0){
    for(var j=0;j<measBar.length;j++){
   bars = svg.selectAll(".bar"+j).data(data).enter();
    bars.append("g")
            .attr("class", "bar"+j)
            .append("rect")
            .attr("class", function(d,i){
                return "bars-Bubble-index-" +div+ measBar[j].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })
            .attr("x", function(d) {

                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*j));
            })
            .attr("width", x.rangeBand() / measNo1)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measBar[j]]+":"+measBar[j];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("y", function(d) {

                if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measBar[j])!=-1){
                return y0(d[measBar[j]]);
                }else{
                return y1(d[measBar[j]]);
                }
            })
            .attr("height", function(d, i, m) {
                 if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measBar[j])!=-1){
                return height - y0(d[measBar[j]]);
                }else{
                return height - y1(d[measBar[j]]);
                }
            })

            .style("fill", function(d,i){
                
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   // alert(JSON.stringify(drilledvalue))
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                   colorShad = getDrawColor(div, parseInt(j));
               }

                return colorShad;


            })
            .attr("color_value",function(d,i){
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   // alert(JSON.stringify(drilledvalue))
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                   colorShad = getDrawColor(div, parseInt(j));
               }
            
                return colorShad;
            })
            .on("mouseover", function(d, i) {
                var msrData;
                var currentMeas='';
                currentMeas=$(this).attr("id").split(":")[2];
//                 msrData = addCommas(d[currentMeas]);
                  msrData = addCurrencyType(div, getMeasureId(currentMeas))+addCommas(numberFormat(d[currentMeas],yAxisFormat,yAxisRounding,div));// Added by shivam
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
//                content += "<span class=\"name\"> " + currentMeas + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + ":</span><span style=\"font-family:helvetica;\" class=\"value\"> " + currentMeas + " <b>:</b> " + d[columns[0]] + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event);
                
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
            var sum = d3.sum(data, function(d) {
        return d[measBar[j]];
    });
    
                if (x.rangeBand() >= 20) {
                    
        /*Modify By Ashutosh*/        
        svg.selectAll(".bar"+j)
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]])+parseFloat(x.rangeBand() /2);// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
  if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measBar[j])!=-1){
                    var yValue = (y0(d[measBar[j]])) < 15 ? y0(d[measBar[j]]) + 14 : y0(d[measBar[j]]);
  }else{
                  var yValue = (y1(d[measBar[j]])) < 15 ? y1(d[measBar[j]]) + 14 : y1(d[measBar[j]]);
  }
                if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                    return "translate(" + xvalue + ", " + (height+((y0(d[measBar[j]])-height)/2))+ ")";
                }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
                    return "translate(" + xvalue + ", " + (height-5) + ")";
                }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
                    return "translate("+xvalue+", " + (height+10) + ")";      
                }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
                    return "translate("+xvalue+", -2)"; 
                }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="OnRight-Bar"){
                    yValue = yValue < 20 ? yValue+5 : yValue+20;
                    return "translate(" + xvalue + ", " + (yValue) + ")";
                }else{
                    return "translate(" + xvalue + ", " + (yValue) + ")";
                }
                    
                })
                .attr("text-anchor", "middle")
                .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
            })
                .attr("class", "valueLabel")
//                .attr("onclick", fun)
//        //Added by shivam
//	.dblTap(function(e,id) {
//		drillFunct(id);
//	}) 
        
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
                })
                .attr("fill", function(d,i){
                    var LabFtColor=[];
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measBar[j]]!='undefined' && chartData[div]["LabFtColor"][measBar[j]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measBar[j]]                   
                          }else {
                        LabFtColor = "#000000";
            }
                    return LabFtColor;   
                })
                .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
 if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measBar[j]]=='undefined' || chartData[div]["dataLabelType"][measBar[j]]=='Absolute'){
 if(typeof chartData[div]["dataDisplayArr"]==='undefined'|| typeof chartData[div]["dataDisplayArr"][measBar[j]] === "undefined" || chartData[div]["dataDisplayArr"][measBar[j]] == "Yes"){ 
     return addCommas(numberFormat(d[measBar[j]],yAxisFormat,yAxisRounding,div));
                    }else{
                      return "";   
                    }
                     }else{
 if(typeof chartData[div]["dataDisplayArr"]==='undefined'|| typeof chartData[div]["dataDisplayArr"][measBar[j]] === "undefined" || chartData[div]["dataDisplayArr"][measBar[j]] == "Yes"){ 
                    var percentage = (d[measBar[j]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }else{
                return "";
                }
                    }
                    }else{
                      return "";   
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
  for(var s=0;s<measLine.length;s++){
 // if(chartList[s]!="Bar"){
 var valueline = d3.svg.line()
    .x(function(d) {return x(d[columns[0]])+ x.rangeBand() / 2;})
    .y(function(d) {
        if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measLine[s])!=-1){
return y0(d[measLine[s]])
}
   else{
    return y1(d[measLine[s]])}
   });

//  var valuelineH = d3.svg.line()
//            .x(function(d,i) {
//               if(i==0){
//               return x(d[columns[0]]) - x(0);
//               }else{
//                   return x(d[columns[0]]) + x(i.length);
//               }
//            })
//            .y(function(d) {
//                var target = "400";
//                return y1(d[measLine[measLine.length-1]]);
//                
//            });
//var line..vv..
//    line = svg.selectAll(".line"+s).data(data).enter();
  if(typeof chartData[div]["DualAxisProp"]!="undefined" && chartData[div]["DualAxisProp"]!="" && chartData[div]["DualAxisProp"]=="withoutLine"){}else{
    svg.append("path")
        .data(data)
     .attr("class", "line1")
     .attr("d", valueline(data))
     .style("stroke", getDrawColor(div, parseInt(s+6)))//add by myk sh. for line color
     .attr("id", function(d,i){return d[columns[0]] + ":" + d[measure2];})
     .attr("fill", "transparent")
     .style("stroke-width", "3")
     .on("click",fun)
	.dblTap(function(e,id) {
		drillFunct(id);
	})
   }

  var sum = d3.sum(data, function(d) {
        return d[measLine[s]];
    });
svg.selectAll("labelText")
             .data(data).enter().append("text")
              .attr("id", function(d,i){
                       return d[columns[0]] + ":" + d[measLine[s]]+":"+measLine[s];
                   })
                   .attr("x", function(d){
                       return x(d[columns[0]]) +15;
                   } ).attr("y", function(d){
                        if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measLine[s])!=-1){
                           return y0(d[measLine[s]]) -10;
                        }else{

                       return y1(d[measLine[s]]) -10;
                        }
                   })

      .attr("dy", ".35em")
      .text(function(d,i) {
            if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
 if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measLine[s]]=='undefined' || chartData[div]["dataLabelType"][measLine[s]]=='Absolute'){
 if(typeof chartData[div]["dataDisplayArr"]==='undefined'|| typeof chartData[div]["dataDisplayArr"][measLine[s]] === "undefined" || chartData[div]["dataDisplayArr"][measLine[s]] == "Yes"){
                             return addCommas(numberFormat(d[measLine[s]],yAxisFormat,yAxisRounding,div));
                     }else{
                      return "";   
                        }
                    }else{
 if(typeof chartData[div]["dataDisplayArr"]==='undefined'|| typeof chartData[div]["dataDisplayArr"][measLine[s]] === "undefined" || chartData[div]["dataDisplayArr"][measLine[s]] == "Yes"){ 
                    var percentage = (d[measLine[s]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }else{
                return "";
                }
                    }
                    }else{
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
           .attr("fill", function(d,i){
                    var LabFtColor=[];
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measLine[s]]!='undefined' && chartData[div]["LabFtColor"][measLine[s]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measLine[s]]                   
                          }else {
                        LabFtColor = "#000000";
                               }
                    return LabFtColor;   
 });
if(typeof chartData[div]["DualAxisProp"]!="undefined" && chartData[div]["DualAxisProp"]!="" && chartData[div]["DualAxisProp"]=="withoutLine"){
var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("rect")
//            .attr("r", 4)
            .attr("x", function(d) {
              if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "ExtraThin") {  
                return x(d[columns[0]]) + x.rangeBand() / 2;
              }else{
                return  x(d[columns[0]]) + x.rangeBand() / 2;
              }
            })
            .attr("y", function(d) {
                if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measLine[s])!=-1){
                           return y0(d[measLine[s]]) ;
                        }else{

                return y1(d[measLine[s]]);
                        }

            })
            .attr("class",function(d,i){
                return "bars-Bubble-index-" +div+ measLine[s].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })
            .attr("width","15")
            .attr("height","15")
            .style("fill", function(d,i){
                return getDrawColor(div, parseInt(s+6));
            })
            .attr("color_value",function(d,i){
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
    
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .on("mouseover", function(d, i) {
                var msrData;
                var currentMeas='';
                currentMeas=$(this).attr("id").split(":")[2];
//                 msrData = addCommas(d[currentMeas]);
                  msrData = addCurrencyType(div, getMeasureId(currentMeas))+addCommas(numberFormat(d[currentMeas],yAxisFormat,yAxisRounding,div));// Added by shivam
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
//                content += "<span class=\"name\"> " + currentMeas + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + ":</span><span style=\"font-family:helvetica;\" class=\"value\"> " + currentMeas + " <b>:</b> " + d[columns[0]] + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}else{
    var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
              if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "ExtraThin") {  
                return x(d[columns[0]]) + x.rangeBand() / 2;
              }else{
                return  x(d[columns[0]]) + x.rangeBand() / 2;
              }
            })
            .attr("cy", function(d) {
                  if(typeof chartData[div]["measures1"]!="undefined" && chartData[div]["measures1"].indexOf(measLine[s])!=-1){
                           return y0(d[measLine[s]]) ;
                        }else{

                return y1(d[measLine[s]]);
                        }

            })
            .style("fill", function(d,i){
                return getDrawColor(div, parseInt(s+6));
            })
            .attr("color_value",function(d,i){
                return getDrawColor(div, parseInt(s+6));
            })
            .style("stroke", function(d, i) {
                  return getDrawColor(div, parseInt(s+6));
            })
            .attr("class",function(d,i){
                return "bars-Bubble-index-" +div+ measLine[s].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })

            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measLine[s]]+":"+measLine[s];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .on("mouseover", function(d, i) {
                 var msrData;
                var currentMeas='';
                currentMeas=$(this).attr("id").split(":")[2];
//                 msrData = addCommas(d[currentMeas]);
                 msrData = addCurrencyType(div, getMeasureId(currentMeas))+addCommas(numberFormat(d[currentMeas],yAxisFormat,yAxisRounding,div));// Added by shivam
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
//                content += "<span class=\"name\"> " + currentMeas + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + ":</span><span style=\"font-family:helvetica;\" class=\"value\"> " + currentMeas + " <b>:</b> " + d[columns[0]] + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}
     
}
    }


//}
        else{
            for(var k=0;k<measureArray.length;k++){
    bars = svg.selectAll(".bar"+k).data(data).enter();
    bars.append("rect")
            .attr("class", function(d,i){
                return "bars-Bubble-index-" +div+ measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })
            .attr("x", function(d) {

                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*k));
            })
            .attr("width", x.rangeBand() / measNo1)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[k]]+":"+measureArray[k];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("y", function(d) {
                return y0(d[measureArray[k]]);
            })
            .attr("height", function(d, i, j) {
                return height - y0(d[measureArray[k]]);
            })
            .attr("fill", function(d, i) {

            return getDrawColor(div, parseInt(k));
            })
            .attr("color_value",function(d,i){
                return getDrawColor(div, parseInt(k));
            })
            .on("mouseover", function(d, i) {
var msrData;
                var currentMeas='';
                currentMeas=$(this).attr("id").split(":")[2];
//                 msrData = addCommas(d[currentMeas]);
                  msrData = addCurrencyType(div, getMeasureId(currentMeas))+addCommas(numberFormat(d[currentMeas],yAxisFormat,yAxisRounding,div));// Added by shivam

                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + ":</span><span style=\"font-family:helvetica;\" class=\"value\"> " + currentMeas + " <b>:</b> " + d[columns[0]] + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event);
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
var boxW=divWid + margin.left + margin.right;
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
    .text(columns[0])
    .style("font-size","11px");
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
                var lineIndex=measLine.indexOf(measureArray[i]);
                if(lineIndex !=-1){
                    return getDrawColor(div, parseInt(lineIndex+6))
                }
                else{
                return getDrawColor(div, parseInt(i))
                    }
                });
    var lineIndex=measLine.indexOf(measureArray[i]);
    if(lineIndex !=-1){
        svg.append("g")
        .append("line")
        .attr("x1",currX-3)
        .attr("y1",(currY-12)+(8/2))
        .attr("x2",currX+8+3)
        .attr("y2",(currY-12)+(8/2))
        .style("stroke",function(){
            return getDrawColor(div, parseInt(lineIndex+6))
        })
        .style("stroke-width","2px")
    }   
    svg.append("text")
    .text(labelsArr[i])
    .attr("id",measureArray[i])
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
    .attr("x",currX+12)
    .attr("y",currY-4)
    .attr("fill", "black")
     .style("stroke",function(d,i){
         return "";
     })
    .style("font-size","11px");
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
else{
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
    rectY=boxH-rectH-(boxH*0.1)-(3*measureArray.length);
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
    var lineCount=0;
    var barCount=0;
for(var i=0;i<measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
          var measureName='';
   svg.append("g")
//            .attr("class", "y axis")
            .append("text")
            .attr("x",rectX+25+(xStep*(i%3)))
            .attr("y",(rectY+13+offset1+(yStep*parseInt(i/3))))
            .attr("fill",function(d){
//                if(i==0){
//                    return 'black';
//                }
//                else{
                getDrawColor(div, parseInt(i-1))
//                }
                })
            .style("font-size","11px")
            .text(function(d){
//                if(i==0){
//                return columns[0];
//            }
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
            if(measureName==='undefined'){
                measureName=measureArray[i];
            }
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
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
//   if(i!=0){
   svg.append("g")
            .append("rect")
            .attr("x",rectX+10+(xStep*(i%3)))
            .attr("y",(rectY+5+offset1+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(d){
                var x=0;
                for(x=0;x<measLine.length;x++){
                    if(measLine[x]==measureArray[i]){
                        break;
                    }
                }
                if(x===measLine.length){
                    return getDrawColor(div, parseInt((barCount++)))
                }
                else{
                    return getDrawColor(div, parseInt((lineCount++))+6)
                }
            })
//   }
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
//            .attr("class", "y axis")
//            .attr("id", "measure"+count)
            .append("text")
            .attr("id", measureArray[i])
            .attr("x",rectX+25)
            .attr("y",(rectY+20+offset1+viewByHgt+count*15))
            .style("font-size","11px")
            .attr("fill", 'black')
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!=='undefined' && chartData[div]["measureAlias"][measureArray[i]]!=='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
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
               return measureName;
           })
            .on("mouseover",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);

                selectedBar.style("fill", drillShade);
            })
            .on("mouseout",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
         //   .attr("class", "y axis")
            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("style", "overflow:scroll")

            .attr("x",rectX+10)
            .attr("y",(rectY+12+offset1+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(d){
                    var lineIndex=measLine.indexOf(measureArray[i]);
                    if(lineIndex !=-1){
                        return getDrawColor(div, parseInt(lineIndex+6))
                    }
                else{
                        return getDrawColor(div, parseInt(i))
                }
            })
            var lineIndex=measLine.indexOf(measureArray[i]);
    if(lineIndex !=-1){
            svg.append("g")
            .append("line")
            .attr("x1",rectX+10-3)
            .attr("y1",(rectY+12+offset1+viewByHgt+count*15)+(rectsize/2))
            .attr("x2",rectX+10+rectsize+3)
            .attr("y2",(rectY+12+offset1+viewByHgt+count*15)+(rectsize/2))
            .style("stroke",function(){
                return getDrawColor(div, parseInt(lineIndex+6))
                })
            .style("stroke-width","2px")
    }
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
//
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
        getSlider1(div,data,divWid,divHgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}

}    

}

}

function buildDualAxisGroup(div, data, columns, measureArray, divWid, divHgt) {
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
      
var dashletid;
//divWid=parseFloat($(window).width())*(.45);
 var chartData = JSON.parse(parent.$("#chartData").val());


var dataList = [];



var measure1 ;
var measure2;
var temp = [];
var measuretemp1 = [];
var measuretemp2 = [];
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
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var customTicks2 = 8;
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks1"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks1"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
//    var fun = "drillWithinchart(this.id,\""+div+"\")";

 //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
        }
	else{
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
    var hide = "hideLabels(this.id,\""+div+"\")";
   
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
//            var data = [{"Auto Type":"Total TGT","Amount":"294148","Quantity":"372"},
//{"Auto Type":"YTD TGT","Change Amount":"142367","Change Quantity":"200"},
//{"Auto Type":"YTD ACH","Change% Amount":"100816","Change% Quantity":"300"},
//];


var counter = 0;
var limit = measuretemp1.length < measuretemp2.length ? measuretemp1.length : measuretemp2.length;
//var groupDataLength = limit < data.length ? limit : data.length;
for(var l=0;l<limit;l++ ){
    var dataMap = {};
  
    if(data.length >1){
  dataMap[columns[0]] = data[l][columns[0]];
      
  dataMap[measuretemp1[counter]] = data[l][measuretemp1[counter]];
  dataMap[measuretemp2[counter]] = data[l][measuretemp2[counter]];
  }else {
    dataMap[columns[0]] = measuretemp1[counter];
      
  dataMap[measuretemp1[counter]] = data[0][measuretemp1[counter]];
  dataMap[measuretemp2[counter]] = data[0][measuretemp2[counter]];  
  }
 if(counter == limit-1){
     counter = 0;
 }else {
     counter ++;
 }

    
dataList.push(dataMap);
}

data = dataList;
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
if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }else{
minVal = 0;
}
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
    if(typeof chartData[div]["yaxisrange"]["axisMax1"]!="undefined" && chartData[div]["yaxisrange"]["axisMax1"]!="" && chartData[div]["yaxisrange"]["axisMax1"]!=0) {
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,y2AxisFormat,y2AxisRounding,div));
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
 if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top' ){
     offset=height/11;
 }
  var  svg = d3.select("#" + div)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +height/6)+" ")
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
            .style('text-anchor', 'end')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i)
//                if (d.length < 13)
//                    return d;
//                else
//                    return d.substring(0, 10) + "..";
            }).attr('transform', 'rotate(-25)');
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
    chartList.push(chartData[div]["chartList"][l])
}
}}

var measNo1=measureArray.length;
measNo1 = (parseFloat(measNo1)-parseFloat(count));

    if(typeof chartData[div]["chartList"]!="undefined"){
//bar
    for(var j=0;j<measBar.length;j++){
   bars = svg.selectAll(".bar"+j).data(data).enter();
    bars.append("g")
            .attr("class", "bar"+j)
            .append("rect")
            .attr("class", "bar")
            .attr("x", function(d) {

//                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo1*j));
                return parseFloat(x(d[columns[0]]));
            })
            .attr("width", x.rangeBand() )
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measBar[j]];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("y", function(d) {
                return y0(d[measBar[j]]);
            })
            .attr("height", function(d, i, m) {
                return height - y0(d[measBar[j]]);
            })

            .style("fill", function(d,i){
                
                var colorShad;
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                   // alert(JSON.stringify(drilledvalue))
                   if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue == d[columns[0]]) {
                        colorShad = drillShade;
                    }else{
                   colorShad = getDrawColor(div, parseInt(0));
               }

                return colorShad;


            })
            
//            .attr("fill", function(d, i) {
//            return getDrawColor(div, parseInt(j));
//            })
            
//            .attr("fill", function(d, i) {
//            return getDrawColor(div, parseInt(j));
//            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//                    selectedBar.style("fill", drillShade);
//                }
                var name = checkMeasureNameForGraph(measureArray[i]);
                
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
                    content += "<span class=\"name\"> " + name + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measuretemp1[i]))+addCommas(d[measuretemp1[i]]) + "</span><br/>";
                }
                else{
                content += "<span class=\"name\"> " + measuretemp1[i] + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measuretemp1[i]))+addCommas(d[measuretemp1[i]]) + "</span><br/>";
                }
                return tooltip.showTooltip(content, d3.event);
//            }
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
//                    alert(JSON.stringify(d));
//                    alert(dataDisplay+":"+displayType);
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                       return addCurrencyType(div, getMeasureI(measureArray[j]))+addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,div));
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
    var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                content += "<span class=\"name\"> " + measuretemp1[i] + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measuretemp1[i]))+addCommas(d[measuretemp1[i]]) + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
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
//  for(var s=0;s<measLine.length;s++){
 // if(chartList[s]!="Bar"){

  var sum = d3.sum(data, function(d,i) {
        return d[measLine[i]];
    });
svg.selectAll("labelText")
             .data(function(d,i){
            
         return data;
     }).enter().append("text")
              .attr("id", function(d,i){
                 
                       return d[columns[0]] + ":" + d[measLine[i]];
                   })
//            .attr("d", valueline(data))
                   .attr("x", function(d){
                       return x(d[columns[0]]) +15;
                   } ).attr("y", function(d,i){
                       return y1(d[measLine[i]]) -10;
                   })

      .attr("dy", ".35em").text(function(d,i)
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
                       return addCurrencyType(div, getMeasureId(measLine[i]))+addCommas(numberFormat(d[measLine[i]],yAxisFormat,yAxisRounding,div)) + "%";
                        }else {
                             return addCurrencyType(div, getMeasureId(measLine[i]))+addCommas(numberFormat(d[measLine[i]],yAxisFormat,yAxisRounding,div));
                        }
                    }else{
                    var percentage = (d[measLine[i]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";
                }
            }else {return "";}
           }).attr("fill",function(d){
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
            .enter().append("rect")
//            .attr("r", 4)
            .attr("x", function(d) {
                return x(d[columns[0]])+30;
            })
            .attr("y", function(d,i) {
                return y1(d[measLine[i]]);
            })
            .attr("width","25")
            .attr("height","25")
            .style("fill", function(d,i){
                return getDrawColor(div, parseInt(1));
            })
            .style("stroke", function(d, i) {
                  return getDrawColor(div, parseInt(1));
            })


            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measLine[i]];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .on("mouseover", function(d, i) {
                   var content = "";
                   var name = checkMeasureNameForGraph(measureArray[i]);
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d[columns[0]] + "</span><br/>";
                if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
                    content += "<span class=\"name\"> " + name + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measuretemp2[i]))+addCommas(d[measuretemp2[i]]) + "</span><br/>";
                }
                else{
                content += "<span class=\"name\"> " + measuretemp2[i] + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measuretemp2[i]))+addCommas(d[measuretemp2[i]]) + "</span><br/>";
                }
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
}
//    }


//}
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
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
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
if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] === "top"){
var boxW=divWid + margin.left + margin.right;
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
    .text(columns[0])
    .style("font-size","11px");
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
    .attr("x",currX+12)
    .attr("y",currY-4)
    .style("font-size","11px");
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
else{
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
    var lineCount=0;
    var barCount=0;
for(var i=0;i<measureArray.length;i++){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
          var measureName='';
   svg.append("g")
//            .attr("class", "y axis")
            .append("text")
            .attr("x",rectX+25+(xStep*(i%3)))
            .attr("y",(rectY+13+offset1+(yStep*parseInt(i/3))))
            .attr("fill",function(d){
//                if(i==0){
//                    return 'black';
//                }
//                else{
                getDrawColor(div, parseInt(i-1))
//                }
                })
            .style("font-size","11px")
            .text(function(d){
//                if(i==0){
//                return columns[0];
//            }
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
            if(measureName==='undefined'){
                measureName=measureArray[i];
            }
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
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
//   if(i!=0){
   svg.append("g")
            .append("rect")
            .attr("x",rectX+10+(xStep*(i%3)))
            .attr("y",(rectY+5+offset1+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(d){
                var x=0;
                for(x=0;x<measLine.length;x++){
                    if(measLine[x]==measureArray[i]){
                        break;
                    }
                }
                if(x===measLine.length){
                    return getDrawColor(div, parseInt((barCount++)))
                }
                else{
                    return getDrawColor(div, parseInt((lineCount++))+6)
                }
            })
//   }
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
//            .attr("class", "y axis")
//            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+25)
            .attr("y",(rectY-3+offset1+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!=='undefined' && chartData[div]["measureAlias"][measureArray[i]]!=='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
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
            .attr("fill", function(d){
                var x=0;
                for(x=0;x<measLine.length;x++){
                    if(measLine[x]==measureArray[i]){
                        break;
                    }
                }
                if(x===measLine.length){
                    return getDrawColor(div, parseInt((barCount++)))
                }
                else{
                    return getDrawColor(div, parseInt((lineCount++))+6)
                }
            })
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

function buildBarAdvance2(div,divId, data, columns, measures,wid,hgt) {
var chartData = JSON.parse(parent.$("#chartData").val());
var hgt1 = hgt;
var translatevalue = 0;
//added by shivam
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
for(var measureloop = 0;measureloop<2;measureloop++ ){
        var measureArray = [];
        if(measureloop==0){
          hgt = hgt1 - 300;
          translatevalue = 60;
       }else{
           hgt = hgt1-100;
            translatevalue = 0;
       }
        measureArray.push(measures[measureloop])

     if(measureloop == 0 && chartData[div]["chartTypeBarTrend"]=="trend"){
         buildAdvanceLine(div,divId, data, columns, measures, wid, hgt);
     }else{

var customTicks = 5;

var color = d3.scale.category10();
var divWid;
if($("#chartType").val()=="Pie-Dashboard"){

divWid =wid;
}else{
divWid=parseFloat($(window).width())*(.70);
}
var chartData = JSON.parse(parent.$("#chartData").val());

var div1=parent.$("#chartname").val()
   var divHgt=hgt;
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
	else{fun
            fun = "drillWithinchart(this.id,\""+div+"\")";
	
	}function drillFunct(id1){
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
        top: 20,
        right: 00,
        bottom: botom,
        left: 70
    };
    if(parent.$("#chartType").val()=="combo-chart"){
    width = divWid *.50; //- margin.left - margin.right
            height = divHgt * .58;
    }else {
         width = divWid*1.1; //- margin.left - margin.right
            height = divHgt * .7;
    }
            //- margin.top - margin.bottom
    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
   var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);
    y = d3.scale.linear()
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
    var xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");
            }else{
   var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
}
//    xAxis = d3.svg.axis()
//            .scale(x)
//            .orient("bottom");
//    if (isFormatedMeasure) {
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
            .attr("height", height + margin.top + margin.bottom +40 )
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + translatevalue + ")");


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

    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" ) ) {
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
 if(measureloop!=0){
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
                return buildXaxisFilterAdvance(div,d,i);
//               if(typeof displayX !=="undefined" && displayX =="Yes"){
//                if (d.length < 15){
//                    return d;
//                }
//                else {
//                    return d.substring(0, 15) + "..";
//                }
//               }else {
//                   return "";
//               }
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
            var font=12;
            font=width/51.2;
            font=font>12?12:font;
            font=font<7?7:font;
            // add by maynk sh. for Afontsize
             if(typeof chartData[div]["Afontsize"]!=="undefined"){
                      font =  chartData[div]["Afontsize"];
             }// end by maynk sh. for Afontsize
            svg.selectAll(".bar")
                .append("svg:text")
                .attr("transform", function(d) {
                    var xvalue = x(d[columns[0]]) + x.rangeBand() / 2;// x(d[measureArray[0]]);
//        var yValue=(height-y(d[measureArray[0]]))>12?y(d[measureArray[0]])+10:y(d[measureArray[0]]);
                    return "translate(" + (xvalue-width/105) + ", " + (height+(height/30)) + ")rotate(" + -90 + ")";
                })
//                .attr("text-anchor", "middle")

                .attr("class", "valueLabel")
                .attr("style", "font-size:"+font+"px")
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
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
                .attr("index_value", function(d, i) {
                    return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measure1];
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

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{ 
           count=0;
var boxW=(width + margin.left + margin.right);
var boxH=(height + margin.top + margin.bottom+30);
//var rectW=150+boxW*0.17;
var measureName='';

    measureName=checkMeasureNameForGraph(measureArray[0]);

var len=measureName.length;
if(columns[0].length+2>len){
    len=columns[0].length+2;
}
var rectW=(measureName.length+columns[0].length)*7+130;
var viewByHgt=15;
var rectH=17;
var rectY = 0;
if(measureloop!=0){
 rectY=boxH-(boxH*1.03)+20;
}else{
 rectY=boxH-(boxH*1.03)-20;
 }
var rectX=boxW-rectW;

var backColor;
if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
    backColor=chartData[div]["lbColor"];
}
else{
    backColor="none";
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
            .attr("x", rectX)
            .attr("y", rectY)
            .attr("width", (rectW-85))
            .attr("height", rectH)
            .attr("rx", 10)         // set the x corner curve radius
            .attr("ry", 10)
            .attr("fill", backColor);
}


         svg.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+12+count*15))
            .attr("fill", 'black')
            .text(columns[0]);


//for(var i in  measureArray){

        var measureName='';
   var offset1=0;
   var offset2=0;

           offset1=(columns[0].length*6.5+20);
           offset2=-24;

   svg.append("g")
            .attr("id", "measure"+count)
            .append("text")
            .attr("x",rectX+offset1+25)
            .attr("y",(rectY+20+offset2+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(0)))
            .text(function(d){

            measureName=checkMeasureNameForGraph(measureArray[0]);


        var length=20;

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

            .attr("x",rectX+offset1+10)
            .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
            .attr("width", "10")
            .attr("height", "10")
            .attr("fill", getDrawColor(div, parseInt(0)))
              count++
}
}
//var htmlvar = '';
//htmlvar += "<table>"
//htmlvar += "<tr>"
//htmlvar += "<td></td>"
//htmlvar += "</tr>"
//htmlvar += "</table>"
 }
 }
 
  function buildGroupedMultiMeasureBar(div,data, columns, measureArray,divWdt,divHgt,divAppend) {
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

    var fun = "drillWithinchart(this.id,\""+div+"\")";

//    var data1 = {};
//    var data2 = {};
//    var xdata = [];
//    var margin = {
//        top: 10,
////        right: 0,
//        right: (mainWidth*.05),
////        bottom: 70,
//        bottom: (divHgt*.1),
//        left: 90
//    },
//    width = divWdt - 50 - margin.left - margin.right,
//            height = divHgt  - margin.top - margin.bottom;

if (typeof chartData[div]["displayLegends"] === "undefined" || chartData[div]["displayLegends"] === "Yes"){
     if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
//    alert("1");
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
    width = ((mainWidth- margin.left - margin.right)*1.07)-20,
            height = divHgt  - margin.top - margin.bottom;
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
            height = divHgt-25  - margin.top - margin.bottom;
}

}else{
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
// 
//             var range =.1;
//             if(typeof chartData[div]["barsize"]!=="undefined" && chartData[div]["barsize"]==="Yes"){
//             range = .6 
//    }
//             var x = d3.scale.ordinal()
//                .rangeRoundBands([0, width+35], range);
    var x0 = d3.scale.ordinal()
            .rangeRoundBands([0, width+35], .1);

    var x1 = d3.scale.ordinal();

    var y = d3.scale.linear()
            .range([height, 10]);
if(typeof chartData[div]["displayXLine"]!="undefined" && chartData[div]["displayXLine"]!="" && chartData[div]["displayXLine"]!="Yes"){
    var xAxis = d3.svg.trendaxis()
            .scale(x0)
            .orient("bottom");
}else{
    var xAxis = d3.svg.axis()
            .scale(x0)
            .orient("bottom");
}
    var yAxis;
         if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_y_axis = function() {
    return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
         }else{
            make_y_axis = function() {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                   }else {
                       return "";
                   }
                });
    }
    var localColorMap = {};
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    var svg = d3.select("#"+div)
            .append("svg")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(mainWidth+ margin.left + margin.right)+" "+(divHgt+47)+" ")
            .attr("style","float:left")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+10) + ")");
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
            .tickSize(-width-35, 0, 0)
            .tickFormat("")
        )}
    var mdata = [];
    var keys = [];
    var max;
    var len=0;
    var counter=0,measCount=0;
    var groupedTotal = {};
    var indiviTotal = {};
    for(var k=0;k<measureArray.length;k++){
    data.forEach(function(d, j) {
//        if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10) {
        if (keys.indexOf(d[columns[0]]) === -1 ) {
            keys.push(d[columns[0]]);
        }
//        if (typeof groupedTotal[d[columns[0]]] === "undefined") {
//            groupedTotal[d[columns[0]]] = 0;
//        }
        if (typeof groupedTotal[d[colName[0]]] === "undefined") {
            groupedTotal[d[colName[0]]] = 0;
        }
        try {
//            groupedTotal[d[columns[0]]] = parseFloat(d[measureArray[0]]) + parseFloat(groupedTotal[d[columns[0]]]);
            groupedTotal[d[colName[0]]] = parseFloat(d[measureArray[k]]) + parseFloat(groupedTotal[d[colName[0]]]);
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
            indiviTotal[d[colName[1]]] = parseFloat(d[measureArray[k]]) + parseFloat(indiviTotal[d[colName[1]]]);
        } catch (e) {
        }
    });
 }
    var numberOfColumns = keys.length;
    for(var k=0;k<measureArray.length;k++){
    for (var j = 0; j < keys.length; j++) {
        data2 = {};
        data2[colName[0]] = keys[j];
        var count = 0;
        for (var i = 0; i < data.length; i++) {
            if (keys[j] === data[i][colName[0]]) {
//                alert(measureArray[k]);
                data2[data[i][colName[1]]] = data[i][measureArray[k]];
                count++;
                if (i === 0) {
                    max = data[i][measureArray[k]];
                }
                else {
                    if (max < parseFloat(data[i][measureArray[k]])) {
                        max = data[i][measureArray[k]];
                    }
                }
            }
        }//
        mdata.push(data2);
    }
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
    len=Object.keys(data[0]).length-1;
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
                if (i%keys.length === k) {
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
        x1.domain(ageNames).rangeRoundBands([0, x0.rangeBand()/measureArray.length]);
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
        y.domain([yVal, max]);
}else{
     y.domain([parseFloat(minVal), parseFloat(max)]);
}







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
                    .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  -2;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return -2;
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
            return 0;
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
    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
                    .text(function(d,i) {
                        return buildXaxisFilter(div,d,i);
                    });
        }
}

if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
        if (count < 1) {
            svg.append("g")
                    .attr("class", "y axis")
                    .call(yAxis)
                    .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
            count++;
              }
}

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
                .attr("x", function(d,i) {
                    var groupIndex=parseInt(counter/len)%measureArray.length
                    if(parseInt(counter++/len)%measureArray.length==0){
                        return x1(d.name);
                    }
                    else{
                        return x1(d.name)+(x0.rangeBand()/measureArray.length)*groupIndex;
                    }
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
//                            colorCount++;
                         colorGroup[d.parentName]=getDrawColor(div,colorCount);
                        groupColor = getDrawColor(div,colorCount);
                         return getDrawColor(div,colorCount++);
                        }
                    }
                        else{

                        if(typeof colorGroup[d.name]!="undefined" ){
                            return colorGroup[d.name]
                        }else{
//                            colorCount++;
                        colorGroup[d.name]=getDrawColor(div,colorCount);
                         groupColor = getDrawColor(div,colorCount);
                        return getDrawColor(div,colorCount++);
                        }
                    }
                    }

//                    return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d,i) {
                    var measIndex=parseInt(measCount++/len)%measureArray.length;
                        return d.parentName+":"+d.name + ":" + d.value+":"+measIndex;
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
//                            colorCount++;
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
                .on("mouseover", function(d, i, j) {

                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;


                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                    var msrData;
					 if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(d.value);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                        msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(d.value);

                }
            else{

                 msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }
                     //   msrData = addCommas(d.value);
//                    }
                    var content = "";
                    content += "<span class=\"name\">" + colName[0] + ":</span><span class=\"value\"> " + d.parentName + "</span><br/>";
                    if(typeof d.name!="undefined" && typeof colName[1] !="undefined"){
                    content += "<span class=\"name\">" + colName[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                }
                    content += "<span class=\"name\">" + measureArray[bar.attr("id").split(":")[3]] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
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
                //alert(chartData[div]["legendNo"]);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }
                else{
                    legendLength=(keys1.length<15?keys1.length:15); 
                }
//               if(legendLength>7 && legendLength<10){
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
             yvalue = parseInt((height * .11));
        yLegendvalue = parseInt((height * .017));
        rectyvalue = parseInt((height * .05));
           
}
else{
           if (legendLength>=10){
                 yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+32;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+7);
}
            else if (legendLength>=6){
                yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+45;
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))+17);
            }
            else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06)+50;
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
//.attr("transform", "translate(" +(mainWidth- margin.left - margin.right)  + "," + yLegendvalue + ")")
.attr("transform", "translate(" +((mainWidth- margin.left - margin.right+30)*1.07)  + "," + yLegendvalue + ")")

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
//            .attr("transform", "translate(" + (mainWidth- margin.left - margin.right )  + "," + (yvalue-9) + ")")
         .attr("transform", "translate(" + ((mainWidth- margin.left - margin.right+30 )*1.07)  + "," + (yvalue)*.80 + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
//            .attr("fill", color(i))
            .attr("fill", function(){
//                for(var m=0;m<keys1.length;m++){
                return colorGroup[keys1[m]];
//                }
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
//            .attr("transform", "translate(" +(mainWidth- margin.left - margin.right+15)  + "," + yvalue + ")")
              .attr("transform", "translate(" +((mainWidth- margin.left - margin.right+47)*1.07)  + "," + (yvalue+12)*.80 + ")")
//            .attr("fill", color(i))

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
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return keys1[m];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){

                if(keys1[m]!="undefined"){
if(keys1[m].length>15){
                    return keys1[m].substring(0, 15)+"...";
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
else{
    
        var yvalue=0;
        var rectyvalue=0;
        var yLegendvalue=0;
        var rectyvalue1=0;
        var len = parseInt(width-150);
        var rectlen = parseInt(width-200);
        var fontsize = parseInt(width/45);
        var fontsize1 = parseInt(width/52);
        var rectsize = parseInt(height/30);
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
var chartWid=(width+ margin.left + margin.right)-(width+ margin.left + margin.right)/12;
  chartWid*=.75;
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
                 return colorGroup[keys1[m]]
            })
//            .attr("fill", function(){
//             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
//              if(chartData[div]["colorLegend"]=="Black") {
//                  return "#000";
//              } else{
//                  return  color(i);
//              }
//             }else{
//                 return  "#000";
//             }
//            })
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
  
count=0;
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
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
//if(columns[0].length+2>len1){
//    len1=columns[0].length+2;
//}

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
    rectH=17*(parseInt((measureArray.length)/3)+1);
}
else{
    rectH=10+(17*measureArray.length)+viewByHgt;
}

var rectX;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
      rectX=35;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright" ){
    rectX=boxW-rectW-20;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}

var rectY=-22;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    rectY=-22;
}
else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)-5;
}
else if(chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.2);
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
    
for(var i=0;i<measureArray.length;i++){
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
            .attr("fill",getDrawColor(div, 0))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!=''){
            measureName1=chartData[div]["measureAlias"][measureArray[i]];
            if(measureName1==='undefined'){
                measureName1=measureArray[i];
            }
        }else{
            measureName1=checkMeasureNameForGraph(measureArray[i]);  // dual axis bar
        }
                var length=0;
                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measureArray[i]] === "undefined" || chartData[div]["measureLabelLength"][measureArray[i]] === "20") {
                    length=20;
                }
                else{
                    length=chartData[div]["measureLabelLength"][measureArray[i]];
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
   svg.append("g")
            .append("rect")
            .attr("x",rectX-33+(xStep*(i%3)))
            .attr("y",(rectY+15+(yStep*parseInt(i/3))))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, 0))
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

}

function buildMultiMeasureLayeredGroupedBar(div,data, columns, measureArray,divWdt,divHgt,divAppend) {
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

    var fun = "drillWithinchart(this.id,\""+div+"\")";

    var data1 = {};
    var data2 = {};
    var xdata = [];
    var margin = {
        top: 10,
//        right: 0,
        right: (mainWidth*.05),
//        bottom: 70,
        bottom: (divHgt*.1),
        left: 90
    },
    width = divWdt - 50 - margin.left - margin.right,
            height = divHgt  - margin.top - margin.bottom;

    var x0 = d3.scale.ordinal()
            .rangeRoundBands([0, width+35], .1);

    var x1 = d3.scale.ordinal();

    var y = d3.scale.linear()
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
    var yAxis;
         if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_y_axis = function() {
    return d3.svg.gridaxis()
        .scale(y)
        .orient("left")
        .ticks(customTicks)
}
         }else{
            make_y_axis = function() {
    return d3.svg.axis()
        .scale(y)
        .orient("left")
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
                        return addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                   }else {
                       return "";
                   }
                });
    }
    var localColorMap = {};
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    var svg = d3.select("#"+div)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(mainWidth+ margin.left + margin.right)+" "+(divHgt+30)+" ")
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
        )}
    var mdata = [];
    var keys = [];
    var max;
    var len=0;
    var counter=0,measCount=0,colorIndex=0;
    var groupedTotal = {};
    var indiviTotal = {};
    for(var k=0;k<measureArray.length;k++){
    data.forEach(function(d, j) {
//        if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10) {
        if (keys.indexOf(d[columns[0]]) === -1 ) {
            keys.push(d[columns[0]]);
        }
//        if (typeof groupedTotal[d[columns[0]]] === "undefined") {
//            groupedTotal[d[columns[0]]] = 0;
//        }
        if (typeof groupedTotal[d[colName[0]]] === "undefined") {
            groupedTotal[d[colName[0]]] = 0;
        }
        try {
//            groupedTotal[d[columns[0]]] = parseFloat(d[measureArray[0]]) + parseFloat(groupedTotal[d[columns[0]]]);
            groupedTotal[d[colName[0]]] = parseFloat(d[measureArray[k]]) + parseFloat(groupedTotal[d[colName[0]]]);
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
            indiviTotal[d[colName[1]]] = parseFloat(d[measureArray[k]]) + parseFloat(indiviTotal[d[colName[1]]]);
        } catch (e) {
        }
    });
 }
    var numberOfColumns = keys.length;
    for(var k=0;k<measureArray.length;k++){
    for (var j = 0; j < keys.length; j++) {
        data2 = {};
        data2[colName[0]] = keys[j];
        var count = 0;
        for (var i = 0; i < data.length; i++) {
            if (keys[j] === data[i][colName[0]]) {
//                alert(measureArray[k]);
                data2[data[i][colName[1]]] = data[i][measureArray[k]];
                count++;
                if (i === 0) {
                    max = data[i][measureArray[k]];
                }
                else {
                    if (max < parseFloat(data[i][measureArray[k]])) {
                        max = data[i][measureArray[k]];
                    }
                }
            }
        }//
        mdata.push(data2);
    }
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
    len=Object.keys(data[0]).length-1;
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
                if (i%keys.length === k) {
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
                    .attr("transform", "translate(0," + height + ")")
                    .call(xAxis)
                    .selectAll('text')
                    .attr("transform", "rotate(-20)")
                    .style("text-anchor", "end")
                    .text(function(d,i) {
                        return buildXaxisFilter(div,d,i);
//                        if (d.length < 10)
//                            return d;
//                        else
//                          return d.substring(0, 10) + "..";
                    });
        }

        if (count < 1) {
            svg.append("g")
                    .attr("class", "y axis")
                    .call(yAxis)
                    .append("text")
                    .attr("transform", "rotate(-90)")
                    .attr("y", 6)
                    .attr("dy", ".71em")
                    .style("text-anchor", "end")
                    .text("" + "");//measureArray[0] +
            count++;
              }
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
                .attr("x", function(d,i) {
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
                        var c=parseInt(colorIndex++/len)%measureArray.length;
//                        alert(colorIndex);
                        return color(c);
//                        if(typeof columnsVar[1]=="undefined" || columns[1] == null){
//                        if(typeof colorGroup[d.parentName]!="undefined" ){
//                            return colorGroup[d.parentName]
//                        }else{
//                            colorCount++;
//                        colorGroup[d.parentName]=color(colorCount);
//                         groupColor = color(colorCount);
//                        return color(colorCount);
//                        }
//                    }
//                        else{
//
//                        if(typeof colorGroup[d.name]!="undefined" ){
//                            return colorGroup[d.name]
//                        }else{
//                            colorCount++;
//                        colorGroup[d.name]=color(colorCount);
//                         groupColor = color(colorCount);
//                        return color(colorCount);
//                        }
//                    }
                    }

//                    return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("id", function(d,i) {
                    var measIndex=parseInt(measCount++/len)%measureArray.length;
                        return d.parentName+":"+d.name + ":" + d.value+":"+measIndex;
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
                            colorCount++;
                        colorGroup[d.parentName]=color(colorCount);
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
                .attr("class", function(d, i) {

                    if(typeof d.name =="undefined" || d.name == "undefined"){
                    return "bars-Bubble-index-" + d.parentName.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
                    }else {
                          return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
                    }
                })
                .attr("onclick", fun)
                .on("mouseover", function(d, i, j) {


                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;


                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);

                    var msrData;
					 if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(d.value);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                        msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(d.value);

                }
            else{

                 msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }
                     //   msrData = addCommas(d.value);
//                    }
                    var content = "";
                    content += "<span class=\"name\">" + colName[0] + ":</span><span class=\"value\"> " + d.parentName + "</span><br/>";
                    if(typeof d.name!="undefined" && typeof colName[1] !="undefined"){
                    content += "<span class=\"name\">" + colName[1] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
                }
                    content += "<span class=\"name\">" + measureArray[bar.attr("id").split(":")[3]] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
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
                //alert(chartData[div]["legendNo"]);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                   // alert('if');
                   // alert(legendLength);
                }
                else{
                   // alert("else");
                   // alert(legendLength);
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
                 var displayLegends = chartData[div]["displayLegends"];
                        if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){

        var count = 0;
        var transform =

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
//.attr("transform", "translate(" + mainWidth*1.52  + "," + yLegendvalue + ")")
.attr("transform", "translate(" +(mainWidth- margin.left - margin.right)  + "," + yLegendvalue + ")")

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
           for(var m=0;m<(legendLength);m++){ ////length
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
            .attr("transform", "translate(" + (mainWidth- margin.left - margin.right )  + "," + (yvalue-9) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
//            .attr("fill", color(i))
            .attr("fill", function(){
//                for(var m=0;m<keys1.length;m++){
                return colorGroup[keys1[m]];
//                }
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
            .attr("transform", "translate(" +(mainWidth- margin.left - margin.right+15)  + "," + yvalue + ")")
//            .attr("fill", color(i))

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
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return keys1[m];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){

                if(keys1[m]!="undefined"){
if(keys1[m].length>15){
                    return keys1[m].substring(0, 15)+"...";
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
    rectW=(measureName.length)*7+130;
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
    rectX=boxW-rectW-(boxW*0.01)-20;
}
else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
    rectX=boxW-rectW-(boxW*0.01)-20;
}
else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
    rectX=5;
}
else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
    rectX=boxW/2-rectW/2;
}
var rectY=-5;
if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
    rectY=boxH-(boxH*1.03)+10;
}
else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
    rectY=boxH-(boxH*1.03)+10;
}
else if(chartData[div]["lbPosition"] === 'bottomright' || chartData[div]["lbPosition"] === "bottomcenter" || chartData[div]["lbPosition"] === "bottomleft"){
    rectY=boxH-rectH-(boxH*0.18);
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
//         svg.append("g")
//            .attr("id", "viewBylbl")
//            .append("text")
//            .attr("x",rectX+10)
//            .attr("style","font-size:10px")
//            .attr("y",(rectY+12+count*15))
//            .attr("fill", 'black')
//            .text(columns[0]);  
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
            .attr("x",rectX+25)
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
           if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}
           else
           {
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
           }
              count++
}
}

function buildhorizontalstackedBar(div,data, columns, measureArray,width,height){
    var colorSet = d3.scale.category10();
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
    var margin = {top: 5,right: 12,bottom: 10,left:30 };
   }else{
    var margin = {top: 5, right: 20, bottom: 10,left: 130 };
   }
    
    var x = d3.scale.ordinal()
    .rangeRoundBands([0, divHgt*.83], .1);
    
   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
    if(divWid > 590){ 
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.93], .1, .1);
}else{
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.70], .1, .1);  
    }
}else{ 
    if(divWid > 590){
     var y = d3.scale.linear()
    .rangeRound([0, divWid*.79], .1, .1);
        }else{
        var y = d3.scale.linear()
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
    .attr("transform", "translate(" + margin.left*.55 + "," + (margin.top+12) + ")");
            
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
    .append("svg:linearGradient")
    .attr("id", function(d) {
//        return "gradient" + (d).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
//        if (typeof centralColorMap[d.toLowerCase()] !== "undefined") {
//            colorShad = centralColorMap[d.toLowerCase()];
//        } else {
//            colorShad = color(i);
//        }
//        if (typeof localColorMap[d] === "undefined" || localColorMap[d] === "undefined") {
//            localColorMap[d] = colorShad;
//        }
//        if (typeof chartMap[d] === "undefined") {
//            chartMap[d] = colorShad;
//            colorMap[i] = d + "__" + colorShad;
//        }
        return colorShad;
    })
    .attr("stop-opacity", 1);
            
    var tmp = color;
    tmp.domain(d3.keys(data[0]).filter(function(key) {
        return key !== columns[0];
    }));
    data.forEach(function(d) {
        var y0 = 0;
        d.val = tmp.domain().map(function(name) {
            return {
                name: name,
                viewBy: d[columns[0]],
                value: d[name],
                y0: y0,
                y1: y0 += +d[name]
            };
        });
        d.total = d.val[d.val.length - 1].y1;
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
        if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
        .attr("transform", "translate(23,-5)")
        .call(xAxis)
        .selectAll('text')
//        .style('text-anchor', 'end')  
        .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
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
    }).style("text-anchor", "end") 
        .append("svg:title")
        .text(function(d) {          
            return d;
        }); 

    var state = svg.selectAll(".state")
    .data(data)
    .enter().append("g")
    .attr("class", "rect1")
    .attr("transform", function(d) {
        return "translate(0," + (x(d[columns[0]])-8)+")";
    });
        
    var count = 0;
var colIds=[];
colIds = chartData[div]["viewIds"];
    state.selectAll("rect")
    .data(function (d) {
        return d.val;
    })
    .enter()
    .append('rect')
    .attr("x", function(d) {
        return y(d.y0)+23;
    })
    .attr("y", "5")
    .attr('width', function (d) {
        return y(d.y1)-y(d.y0); 
    })
    .attr("height", x.rangeBand()*.90)  
    .attr("fill", function(d,i) {
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
    .attr("color_value", function(d,i) {
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
    .on("mouseover", function(d, i) {
        var msrData;
        if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
//            msrData = addCommas(d.value);
              msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
        }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
            msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(d.value);
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
            msrData = addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
        }
      
        var content = "";
//        content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
//        content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + ":</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + "</span><br/>";//Added by shivam
        content = show_stackDetail(div,d,data,chartData,measureArray,content,i,msrData);
        count++;
        return tooltip.showTooltip(content, d3.event);
    })
    .on("mouseout", function(d, i) {
        hide_details(d, i, this);
    }).attr("index_value", function(d, i) {
        return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
                
    state.selectAll("rect1")
    .data(function(d) {
        return d.val;
    })
    .enter()
    .append("text")
  .style("text-anchor", "middle")
    .attr("transform",function(d,i) {
        var xvalue =y(d.y1);
        var yValue = (x(d[columns[0]])+(x.rangeBand()*.95)/2);
        if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop"){
                return "translate(" + (xvalue+40) + ", " + (yValue)+ ")";
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnCenter"){
            return "translate(" + (23+(y(d.y0)+((y(d.y1)-y(d.y0))/2))) + ", " + yValue+ ")";      
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom"){ 
            return "translate(" + (y(d.y0)+40) + ", " + yValue+ ")";
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBar"){ 
             return "translate(" + (xvalue) + ", " + (yValue)+ ")";
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop-Bar"){
             if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     if(divWid < 600){
                         return "translate("+divWid*.87+", " + (yValue) + ")"; 
                     }else{
                     return "translate("+divWid*.91+", " + (yValue) + ")";      
                     }
                     }else{
                          if(divWid < 600){
                     return "translate("+divWid*.80+", " + (yValue) + ")";      
                     }else{
                        return "translate("+divWid*.85+", " + (yValue) + ")"; 
                     }
                     }
        }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom-Bar"){
            return "translate(" + (y(d.y0)) + ", " + yValue+ ")";
        }
       return "translate(" + (23+(y(d.y0)+((y(d.y1)-y(d.y0))/2))) + ", " + yValue+ ")";      
       
    })
//    .style("font-size","8px")
    .text(function(d,i){
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
            if(typeof displayType=="undefined" || displayType==="Absolute"){
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            return addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));   
                        }else {
                            return "";
                        } 
                    }else {
                        return addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                    }
                }else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            var percentage = (d.value / parseFloat(sum[i])) * 100;
                            if(percentage<5){
                                return "";
                            }
                            return percentage.toFixed(1) + "%";
                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                            if(percentage1<5){
                                return "";
                            }                        
                        return percentage1.toFixed(1) + "%";
                    }
                }
            }else {
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='%-wise'){
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                            if(percentage1<5){
                                return "";
                            }                            
                            return percentage1.toFixed(1) + "%";
                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                            if(percentage1<5){
                                return "";
                            }                        
                        return percentage1.toFixed(1) + "%";
                    }
                }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                            return addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(d.name))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                    }
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
    .attr("fill", function(d,i){
                    var LabFtColor="";
 if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[i]]!='undefined' && chartData[div]["LabFtColor"][measureArray[i]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[i]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   
 });     
           
    for(var i=0;i<measureArray.length;i++){   // horizontal stackebar targetline
        var target = "";
        var labelLen=0,valueLen=0;
        labelLen=measureArray[i].length;
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
            target = chartData[div]["defineTargetline"][measureArray[i]];
            svg.append("text")
            .text(function(d){
                if(yAxisFormat==""){
                    valueLen=(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                    return addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
                }else{
                    valueLen=(addCurrencyType(div, getMeasureId(measureArray[i]))+numberFormat(target,yAxisFormat,yAxisRounding,div)).length;
                    return numberFormat(target,yAxisFormat,yAxisRounding,div);
                }
            })       
            .attr("y", (width-labelLen*13.9))
            .attr("x", y(parseInt(target))+18)
            .attr("style","font-size:7px");
            
            svg.append("text")       
            .attr("y", (width-labelLen*13.3))
            .attr("x", y(parseInt(target))+10)
            .attr("style","font-size:7px")
            .text("("+measureArray[i]+")");
        }
        var valuelineH = d3.svg.line()
        .y(function(d,i) {
            if(i==0){
                return x(d[columns[0]]);
            }else{
                return x(d[columns[0]]) + x(i.length)+20;
            }
        })
        .x(function(d) {
            if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
                target = chartData[div]["defineTargetline"][measureArray[i]];
                return y(parseInt(target));
            }
        });
        if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){
            var path = svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .attr("y", (width-labelLen*7.2))
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
        }
    }
  if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
    else{  
if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
    var boxW=width;
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
//    .style("fill",function(){
//                return getDrawColor(div, parseInt(i))
//                })
   .on("mouseover",function(d,i){
//         prevColor = color(i);
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
    .attr("y",currY+18);
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
        }
        else{
            rectW=30+(len*7)+85
        }
        rectW = rectW<170?170:rectW;
        var viewByHgt=15;
        var rectH=0;  // horizontal stackedbar
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
            svg
            .append("rect")
            .attr("id",div+"_mainRect")
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
            var rectsize;
            if(width<height){
                rectsize = parseInt(width/25);
            }
            else{
                rectsize = parseInt(height/25);
            }
            rectsize=rectsize>10?10:rectsize;
            svg
            .append("rect")
            .attr("x",rectX+10)
            .attr("y",(rectY+12+viewByHgt+count*15))
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", getDrawColor(div, parseInt(i)))
            var measureName='';
            svg.append("text")
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
        getSlider1(div,data,width,height,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}
    
}    

}
    }
// end by mayank sh.

// added by mayank 
 function buildMultiLayeredChart(div, data, columns, measureArray, divWid, divHgt) {
     var color = d3.scale.category10();
//     divHgt *=0.8
   var fromoneview=parent.$("#fromoneview").val()
if(fromoneview!='null'&& fromoneview=='true'){
}
else{
//divWid=parseFloat($(window).width())*(.40);
}
    var measure1 = measureArray[0];
    var measure2;
    if (measureArray.length === 1) {
        measure2 = measureArray[0];
    } else {
        measure2 = measureArray[1];
    }
     var chartData = JSON.parse(parent.$("#chartData").val());
   
var dashletid;
//var prop = graphProp(div);
 var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
     div=div1;
//colIds=chartData[div1]["viewIds"];
}else{
var prop = graphProp(div);
//    colIds=chartData[div]["viewIds"];
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
        divHgt-=30
    }
    var margin = {
        top: 20,
        right: 80,
//        bottom: 55,
        bottom: (divHgt * .3),
        left: 72
    };
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
    var marginleft1 = 50; // add by mynk sh.
    var width = divWid - 70;
     var height = divHgt * .8;
//     var height = divHgt - margin.top - margin.bottom;
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
//    Added by shivam
    var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range); 
    
//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, width], .2);
var max = 0;
var minVal = 0;
// if(fromoneview!='null'&& fromoneview=='true'){
//for(var i in measureArray){
//
//
//var max1 = parseFloat(maximumValue(data, measureArray[i]));
//var min = parseFloat(minimumValue(data, measureArray[i]));
//
//
//if(i==0)  {
//
//max=parseFloat(max1);
//minVal=parseFloat(min);
//}
//
//else if(i>0 && (max1 > max)){
//
//max = max1;
//}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}
//
//}
//for(var i in measureArray){
//
//
//var max1 = parseFloat(maximumValue(data, measureArray[i]));
//var min = parseFloat(minimumValue(data, measureArray[i]));
//
//
//if(i==0)  {
//
//max=parseFloat(max1);
//minVal=parseFloat(min);
//}
//
//else if(i>0 && (max1 > max)){
//
//max = max1;
//}
//else if(i>0 && ( min<minVal)){
//
//minVal = min;
//}
//
//}
// }else{
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){


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
}}
//end of max
else{

for(var i in measureArray){


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

//end of default max
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""  && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""){
   minVal = 0;
 }else{
  for(var i in measureArray){


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
// end of min
else{

for(var i in measureArray){


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
if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }else{
        minVal = 0;
    }
}

//}
//end of default min
var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);

////add condition by mayank sh. for hidden white space
//var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
//var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);

//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
   y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);
   y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);
}

//if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
//   var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+50, 0]);
//}else{
//var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+35, 0]);
//}
//if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
//var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+50, 0]);
//}else{
//    var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+35, 0]);
//} //end  by mayank sh. for hidden white space


    // create left yAxis
    var yAxisLeft, yAxisRight;
    if (isFormatedMeasure) {
        yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d) {
                    return numberFormat(d, round, precition,div);
                });
    } else {
        yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding1);
//                return addCommas(d);
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });

        yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
                .tickFormat(function(d, i) {
//                    return autoFormating(d, autoRounding2);
//                    return addCommas(d);
      if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                });
    }
//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
          height   = divHgt * .79  ;
 }else{
          height   = divHgt*.80  ;
}
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]=="No"){
     margin = { top: 25,  right: 00,  bottom: divHgt*.46, left: 47  };
}
//}else{
//    margin = {top: 20, right: 80, bottom: (divHgt * .5), left: 72};
//  }
 //end condition by mayank sh. for hidden white space

   if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_x_axis = function() {
    return d3.svg.gridaxis()
        .scale(x)
         .orient("bottom")
         .ticks(5)
}

 make_y_axis = function() {
    return d3.svg.gridaxis()
        .scale(y0)
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
        .scale(y0)
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
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
 var offset=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top' ){
     offset=20;
 }
    var svg = d3.select("#" + div)
			//    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid+20+rightM)+" "+(height + margin.top + margin.bottom -15)+" ")
         //   .attr("width", divWid+20)
        //    .attr("height", height + margin.top + margin.bottom +5)
            .append("g")
            .attr("class", "graph")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");


    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

//    svg.append("g")
//        .attr("class", "grid")
//        .attr("transform", "translate(0," + height + ")")
//        .call(make_x_axis()
//            .tickSize(-height, 0, 0)
//            .tickFormat("")
//        )
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
        )}
//add condition by mayank sh. for display properties
    if(typeof chartData[div]["displayX"]==="undefined" || chartData[div]["displayX"]==="" || chartData[div]["displayX"]=="Yes"){
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
return buildXaxisFilter(div,d,i);
            })
            .attr('x',function(d,i){  // add by mayank sharma for legend print type
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  5;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 5;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -5;
        }else {
            return 0;
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
            return 25;   
            }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 2;
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
        }

//add condition by mayank sh. for display properties
    if(typeof chartData[div]["displayY"]==="undefined" || chartData[div]["displayY"]==="" || chartData[div]["displayY"]=="Yes"){
    svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
        }//end by mynk sh.

var len = 0;
var yvalue = -75;
var dyvalue = ".41em";
var count = 0;
var transform = "";
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
var fontSSize=parseInt(($("#div" + div).width())*.004)/2;

if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(fontSSize>.5 ){
fontSSize=9;
}else if( fontSSize<=.5 ){
fontSSize=6;
}
var step=0;
if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Left"){
    transform="rotate(270)";
    len=height*1.3;
    step=height/2.5;
    yvalue=-70;
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
    yvalue=(height + margin.top + margin.bottom+ 17.5 )-((height + margin.top + margin.bottom+ 17.5 )/2.85);
//    alert(height + margin.top + margin.bottom+ 17.5 );
//    yvalue=58;
}
else if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Top")){
    len=width*.01;
    step=width/5;
    yvalue=-5;
}
// for(var i in  measureArray){
////    if(count % 2==0){
////        if(len==0){
//////            len=50;
////            len=0;
////            yvalue=500;
////        dyvalue = -150;
////        }else{
////        len += ($("#" + div).width())*.10;
//////		len +=150;
////        yvalue=500;
////        dyvalue = -150;
////     }
////    }else {
////       len +=($("#" + div).width())*.10;
//////		len +=150;
////        yvalue=518;
////         dyvalue = -150;
////     }
//     if(fromoneview!='null'&& fromoneview=='true'){
// div=div1;
//}
//  svg.append("g")
////  .attr("transform", "translate(0,0)")
////            .call(yAxisRight)
//            .append("text")
//            .attr("transform", transform)
//            .attr("x",(-len+(step*(count))))
//     //       .attr("x", lennn+measureArray[i].length)
//            .attr("y",yvalue)
//            .attr("fill", getDrawColor(div, parseInt(i)))
////            .style("stroke", color(i))
////            .attr("dy",dyvalue )
////            .style("text-anchor", "middle")
////            .style("font-weight", "bold")
//            .style("font-size", fontSSize+"px")
//            //          .attr("transform", "rotate(-90)")
//            //          .attr()
//                  .text(function(d){
//                  if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
//            return '';
//        }
//                if(measureArray[i].length>25){
//                    return measureArray[i].substring(0, 25);
//                }else {
//                    return measureArray[i];
//          }
//           }).attr("svg:title",function(d){
//               return measureArray[i];
//           })
//              count++
//
//
//   }

//add by mayank sh. for by default display one measure on chart
for(var k=0;k<measureArray.length;k++){

var measNo=measureArray.length;

    bars = svg.selectAll(".bar"+k).data(data).enter();
    bars.append("rect")
            .attr("class", "bar")
            .attr("x", function(d) {
                return parseFloat(x(d[columns[0]]));
            })
            .attr("width", x.rangeBand() )
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[k]] + "#"+measureArray[k];
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
            }).attr("class", function(d, i) {
                return "bars-Bubble-index-" +div+measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            }).attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
                   msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(measureValue);
//               alert("2");

                }
            else{

                 msrData = addCurrencyType(div, chartData[div]["meassureIds"])+addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
//                 alert("3");
            }
                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + columnName + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData +"</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + "</span><br/>";//Added by shivam
//                content = show_stackDetail(div,this,data,chartData,measureArray,content,i,msrData);
                count++;
                return tooltip.showTooltip(content, d3.event); //m
            })
            .on("mouseout", function(d, i) {
                if(fromoneview!='null'&& fromoneview=='true'){

                     }else{
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
//                    var selectedBar = d3.selectAll(barSelector);
//                    var colorValue = selectedBar.attr("color_value");
//                    selectedBar.style("fill", colorValue);
                     }
//                }
                hide_details(d, i, this);
            })

//            .on("mouseover", function(d, i) {
//
//                var meas = [];
//                meas.push(measureArray[k]);
//                show_details(d, columns, measureArray, this,div);
//            })
//            .on("mouseout", function(d, i) {
//
//                hide_details(d, i, this);
//            });
            }

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
            var boxW=(divWid+20+rightM);
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
    .attr("id",measureArray[i])
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    })
    .attr("x",currX+15)
    .attr("y",currY-4);
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
   else 
    {
        var boxW=(divWid+20+rightM);
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
    rectY=boxH-(boxH*1.03)-25;
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
            .attr("transform", transform)
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
            .attr("x",rectX+25)
            .attr("y",(rectY+20+viewByHgt+count*15))
            .attr("fill", getDrawColor(div, parseInt(i)))
            .text(function(d){
        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
            return '';
        }
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!='' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=checkMeasureNameForGraph(measureArray[i]);
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
               return measureName;
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
}}
            //Graph Label
            for(var z=0;z<measureArray.length;z++){
                if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[z]] === "undefined" || chartData[div]["createBarLine"][measureArray[z]] === "Yes")) {
                    continue;
                } 
                 var sum = d3.sum(data, function(d,i) {
                     return d[measureArray[z]];
                    });

             svg.selectAll("labelText")
             .data(data).enter().append("text")
//            .attr("d", valueline(data))
                  .attr("transform", function(d) {
                   var xvalue = x(d[columns[0]]) + parseFloat(x.rangeBand() / measureArray.length*z) + 10 ;// x(d[measureArray[0]]);
//                   var xvalue = parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measureArray.length*z)) +(x.rangeBand()/measureArray.length/2);
                    var yValue = (y0(d[measureArray[z]])) < 15 ? y0(d[measureArray[z]]) + 14 : y0(d[measureArray[z]]) -10;
                 if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom"){
              return "translate(" + xvalue + ", " +height+(height/30)+ ") rotate(270)";  
                     }
                  return "translate(" + xvalue + ", " + yValue + ") rotate(270)"; 
                })
                .attr("text-anchor", "middle")
                .attr("class", "valueLabel")
//                .attr("font-size","8px")
                .style("font-size", function(d){
//                    return ""+parseInt(10-z)+"px"
                    return ""+parseInt(9)+"px"
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
                    
        if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[z]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[z]];
        }
        else{
            yAxisFormat="";
        }
        if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[z]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[z]];
        }
        else{
            yAxisRounding="0";
        } 
        
                 if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='Absolute'){
                        if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                         
                           return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                }else {
                    return "";
                        }
                    }else {
                   return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                }
                }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                            var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
                            return percentage.toFixed(1) + "%";

                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d[measureArray[z]] / parseFloat(sum)) * 100;
                        return percentage1.toFixed(1) + "%";
                    }
                }
                    }else{
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='%-wise'){
                        
                       if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                             var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
                    return percentage.toFixed(1) + "%";

                }else {
                    return "";
                        }
                    }else {
                    var percentage1 = (d[measureArray[z]] / parseFloat(sum)) * 100;
                    return percentage1.toFixed(1) + "%";
                }
            }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                            
                            return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));

                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                    }
                }
            }
        }else {
            return "";
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
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
              .text(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.9)
              .attr("y", y0(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
         if(i==0){
               return x(d[columns[0]]) - x(0);
               }else{
                   return x(d[columns[0]]) + x(i.length);
               }
            })
            .y(function(d) {
                return y0(parseInt(avg));   
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
					  
                     }
// end by mayank    

function buildCumulativeBar(div, data, columns, measureArray, divWid, divHgt){
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

    var fromoneview=parent.$("#fromoneview").val()
    if(fromoneview!='null'&& fromoneview=='true'){
    }
    else{
    }
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
        divHgt-=30
    }
    var margin = {
        top: 20,
        right: 80,
        bottom: (divHgt * .3),
        left: 72
    };
//    var leftM=0,rightM=0,topM=0,bottomM=0;
var lbOffset=25;
    var leftM=0,rightM=0,topM=0,bottomM=0;
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){
    topM=-1*lbOffset;
}
else{
    topM=-10;
}
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
    var marginleft1 = 50; // add by mynk sh.
    var width = divWid - 70;
    var height = divHgt * .8;
    //Added by shivam
    var fun="";
    hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
    if(hasTouch){
        fun="";
    }
    else{
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
        }
    }
    
//    Added by shivam
     var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.2;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, width], range);
    
//    var x = d3.scale.ordinal()
//    .rangeRoundBands([0, width], .2);
    var max = 0;
    var minVal = 0;
    if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
        if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
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
            }}
            }
        }
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
    }}
    }
//end of default max

var multiple=0;
if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    multiple=1;
}
else{
    multiple=0.8;
}
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
        }}
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
    }}
    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * multiple;
    }else{
        minVal = 0;
    }
}
var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
    y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);   //edit by shivam 
    y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height+5, 0]);   //edit by shivam
}
// create left yAxis
var yAxisLeft, yAxisRight;
if (isFormatedMeasure) {
    yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
    .tickFormat(function(d) {
        return numberFormat(d, round, precition,div);
    });

    yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
    .tickFormat(function(d) {
        return numberFormat(d, round, precition,div);
    });
} else {
    yAxisLeft = d3.svg.tildaxis().scale(y0).ticks(customTicks).orient("left")
    .tickFormat(function(d, i) {
        if(yAxisFormat==""){
            return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
        }
        else{
            return numberFormat(d,yAxisFormat,yAxisRounding,div);
        }
    });

    yAxisRight = d3.svg.trendaxis().scale(y1).ticks(6).orient("right")
    .tickFormat(function(d, i) {
        if(yAxisFormat==""){
            return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
        }
        else{
            return numberFormat(d,yAxisFormat,yAxisRounding,div);
        }
    });
}
//add condition by mayank sh. for hidden white space
if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]=="No"){
    height   = divHgt * .80  ;
}else{
    height   = divHgt*.80  ;
}
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]=="No"){
    margin = {
        top: 25,  
        right: 00,  
        bottom: divHgt*.46, 
        left: 47
    };
}
//}else{
//    margin = {top: 20, right: 80, bottom: (divHgt * .5), left: 72};
//  }
//end condition by mayank sh. for hidden white space

if(typeof chartData[div]["displayYLine"]!="undefined" && chartData[div]["displayYLine"]!="" && chartData[div]["displayYLine"]!="Yes"){
    make_x_axis = function() {
        return d3.svg.gridaxis()
        .scale(x)
        .orient("bottom")
        .ticks(5)
    }

    make_y_axis = function() {
        return d3.svg.gridaxis()
        .scale(y0)
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
        .scale(y0)
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
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
var offset=0;
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"]==='top' ){
    offset=20;
}
var svg = d3.select("#" + div)
.append("svg")
.attr("id", "svg_" + div)
.attr("viewBox", "0 0 "+(divWid+20+rightM)+" "+(height + margin.top + margin.bottom -15)+" ")
.append("g")
.attr("class", "graph")
.attr("transform", "translate(" + margin.left + "," + (margin.top+offset) + ")");


x.domain(data.map(function(d) {
    return d[columns[0]];
}));

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
//add condition by mayank sh. for display properties
    svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + height + ")")
    .call(xAxis)
    .selectAll('text')
    .style('font-size',function(d,i) {
                return font1(div,d,i);
            })
    .text(function(d,i) {
if(typeof chartData[div]["displayX"]==="undefined" || chartData[div]["displayX"]==="" || chartData[div]["displayX"]=="Yes"){
        return buildXaxisFilter(div,d,i);
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
            return 25;   
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
    });


//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayY"]==="undefined" || chartData[div]["displayY"]==="" || chartData[div]["displayY"]=="Yes"){
    svg.append("g")
    .attr("class", "y axis axisLeft")
    .attr("transform", "translate(0,0)")
    .call(yAxisLeft)
    .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
}//end by mynk sh.

if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
    svg.append("line")
    .attr("x1",0)
    .attr("y1",y0(0))//so that the line passes through the y 0
    .attr("x2",width)
    .attr("y2",y0(0))//so that the line passes through the y 0
    .style("stroke", "black");
    svg.append("g")
    .attr("class", ".x axis")
    .attr("transform", "translate(0," + height + ")")
}

for(var i=0;i<measureArray.length;i++){   // smooth line
    var target = "";
    var labelLen=0,valueLen=0;
    labelLen=measureArray[i].length;
    if(typeof chartData[div]["defineTargetline"]!=="undefined" && chartData[div]["defineTargetline"] !="" && typeof chartData[div]["defineTargetline"][measureArray[i]]!=="undefined" && chartData[div]["defineTargetline"][measureArray[i]] !=""){      
        target = chartData[div]["defineTargetline"][measureArray[i]];
        svg.append("text")
        .text(function(d){
            if(yAxisFormat==""){
                valueLen=(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div))).length;
                return addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(target,yAxisFormat,yAxisRounding,div));
            }
            else{
                valueLen=numberFormat(target,yAxisFormat,yAxisRounding,div).length;
                return numberFormat(target,yAxisFormat,yAxisRounding,div);
            }
        })       
        .attr("x", (width)*.90)
        .attr("y", y0(parseInt(target))-12)
        .attr("style","font-size:8px");
            
        svg.append("text")       
        .attr("x", (width-labelLen*5.2))
        .attr("y", y0(parseInt(target))-5)
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
            return y0(parseInt(target));
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
var len = 0;
var yvalue = -75;
var dyvalue = ".41em";
var count = 0;
var transform = "";
if(fromoneview!='null'&& fromoneview=='true'){
    div=dashletid;
}
var fontSSize=parseInt(($("#div" + div).width())*.004)/2;

if(fromoneview!='null'&& fromoneview=='true'){
    div=div1;
}
if(fontSSize>.5 ){
    fontSSize=9;
}else if( fontSSize<=.5 ){
    fontSSize=6;
}
var step=0;
if(typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Left"){
    transform="rotate(270)";
    len=height*1.3;
    step=height/2.5;
    yvalue=-70;
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
    yvalue=(height + margin.top + margin.bottom+ 17.5 )-((height + margin.top + margin.bottom+ 17.5 )/2.85);
}
else if(typeof chartData[div]["labelPosition"] === 'undefined' || (typeof chartData[div]["labelPosition"] !== 'undefined' && chartData[div]["labelPosition"]=="Top")){
    len=width*.01;
    step=width/5;
    yvalue=-5;
}
//add by mayank sh. for by default display one measure on chart
for(var k=0;k<measureArray.length;k++){
    if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
        if(k==0){
            if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "Yes")) {
                continue;
            }
        }else{
        if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "No") {
            continue;
        }
    } //end by mayank sh.
}
var measNo=measureArray.length;
bars = svg.selectAll(".bar"+k).data(data).enter();
bars.append("rect")
.attr("class", "bar")
.attr("x", function(d) {
    return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo*k));
})
.attr("width", x.rangeBand() / measNo)
.attr("rx", barRadius)
.attr("id", function(d) {
    return d[columns[0]] + ":" + d[measureArray[k]]+":"+k;
})
.attr("onclick", fun)
//Added by shivam
.dblTap(function(e,id) {
    drillFunct(id);
}) 
    
.attr("y", function(d) {
    if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
        if(y0(0) > y0(d[measureArray[k]]))
    return y0(d[measureArray[k]]);
        else
            return y0(0);
    }
    else{
        return y0(d[measureArray[k]]);
    }
})
.attr("height", function(d, i, j) {
    if(containsNegativeValue(data, measureArray, 'multi','y',chartData[div])){
        return Math.abs(y0(0) - y0(d[measureArray[k]]));
    }
    else{
    return height - y0(d[measureArray[k]]);
    }
})
.attr("fill", function(d, i) {

    return getDrawColor(div, parseInt(k));
}).attr("class", function(d, i) {
    if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
        return "bars-Bubble-index-" + measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
    }
    else{
        return "bars-Bubble-index-" +div+measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi');;
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
//         chartData[div]["tooltipType"][measureArray[i]]="Default";
        window.com= true;
            show_details(d, columns, measureArray, this,div);
        }
        else{

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
                }
                else {
                    msrData = addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(d[measureArray[i]]);
                }
                content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>"; 
            }
            return tooltip.showTooltip(content, d3.event);
        }
    }
  window.com= false;})
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
    //                }
    hide_details(d, i, this);
})
}

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{
if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    var boxW=(divWid+20+rightM);
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
    .attr("y",currY-2)
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
    .attr("y",currY+8)
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
    .attr("y",currY)
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
    .attr("id",measureArray[i].replace(/[^a-zA-Z0-9]/g, '', 'gi'))
    .attr("x",currX+15)
    .attr("y",currY+8)
    .on("mouseover",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);

        selectedBar.style("fill", drillShade);
    })
    .on("mouseout",function(d,i){
        var measureName =this.id;
        var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
        var selectedBar = d3.selectAll(barSelector);
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    });
    currX=currX+(labelsArr[i].length*7)+15;
    if(currX>maxX){
        maxX=currX;
    }
}
d3.select("#"+div+"_mainRect").attr("height",rowCount*15);    
d3.select("#"+div+"_mainRect").attr("width",maxX);    
    }
   else 
    {
    var boxW=(divWid+20+rightM);
    var boxH=(height + margin.top + margin.bottom -15);
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
        rectY=boxH-(boxH*1.03)-25;
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
        .attr("transform", transform)
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
    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){}
    else{           
        if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}
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
        if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
            viewByHgt=0;
        }
        for(var i in  measureArray){
            if(fromoneview!='null'&& fromoneview=='true'){
                div=div1
            }
            var measureName='';
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
            .on("mouseover",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);

                selectedBar.style("fill", drillShade);
            })
            .on("mouseout",function(d,i){
                var measureName =this.id;
                var barSelector = "." + "bars-Bubble-index-"+div+measureName.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var selectedBar = d3.selectAll(barSelector);
                var colorValue = selectedBar.attr("color_value");
                selectedBar.style("fill", colorValue);
            });
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
            count++
        }
    }
}
}
//Graph Label
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
    .attr("transform", function(d) { //add by mayank sh.
        var xvalue = parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measureArray.length*z)) +(x.rangeBand()/measureArray.length/2);// x(d[measureArray[0]]);
        var yValue = (y0(d[measureArray[z]])) < 15 ? y0(d[measureArray[z]]) + 14 : y0(d[measureArray[z]]) -18;
        if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom"){
              return "translate(" + xvalue + ", " + (height*.92) + ") rotate(270)"; // add by mayank sh. for label display
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnTop-Bar"){
           return "translate(" + xvalue + ", 0) rotate(270)";  
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnBottom-Bar"){
           return "translate(" + xvalue + ","+(height+22)+") rotate(270)";  
          }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnCenter"){
           return "translate(" + xvalue + ","+(height*.75)+") rotate(270)";  
        }else{
          return "translate(" + xvalue + ", " + (yValue+2) + ") rotate(270)";
        }
    })
    .attr("text-anchor", "middle")
    .attr("class", "valueLabel")
    .style("font-size", function(d){
        return ""+parseInt(9)+"px"
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
                    
        if(typeof numberFormatArr!='undefined' && typeof numberFormatArr[measureArray[z]]!='undefined'){
            yAxisFormat=numberFormatArr[measureArray[z]];
        }
        else{
            yAxisFormat="";
        }
        if(typeof numberRoundingArr!='undefined' && typeof numberRoundingArr[measureArray[z]]!='undefined'){
            yAxisRounding=numberRoundingArr[measureArray[z]];
        }
        else{
            yAxisRounding="0";
        } 
        
        if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){

            if(typeof displayType=="undefined" || displayType==="Absolute"){
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='Absolute'){
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                         
                            return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                    }
                }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                            var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
                            return percentage.toFixed(1) + "%";

                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d[measureArray[z]] / parseFloat(sum)) * 100;
                        return percentage1.toFixed(1) + "%";
                    }
                }
            }else{
                if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[z]]=='undefined' || chartData[div]["dataLabelType"][measureArray[z]]=='%-wise'){
                        
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                            var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
                            return percentage.toFixed(1) + "%";

                        }else {
                            return "";
                        }
                    }else {
                        var percentage1 = (d[measureArray[z]] / parseFloat(sum)) * 100;
                        return percentage1.toFixed(1) + "%";
                    }
                }
                else{
                    if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                        if(typeof chartData[div]["dataDisplayArr"][measureArray[z]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[z]] == "Yes"){
                            
                            return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));

                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[z]))+addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                    }
                }
            }
        }else {
            return "";
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
    if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
        var sum = d3.sum(data, function(d) {
            return d[measureArray[i]];
        });
        var avg = sum/data.length;
        svg.append("text")
        .text(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
        .attr("x", (width)*0.9)
        .attr("y", y0(parseInt(avg))-5);
    
        var Averageline = d3.svg.line()
        .x(function(d,i) {
            if(i==0){
                return x(d[columns[0]]) - x(0);
            }else{
                return x(d[columns[0]]) + x(i.length);
            }
        })
        .y(function(d) {
            return y0(parseInt(avg));   
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

function buildstackedBarPercent(div,data, columns, measureArray,wid,hgt) {
    var colorSet = d3.scale.category10();
    
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
    
    
//    wid=parseFloat($(window).width())*(.35);
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
    var margin = {
        top: 5,
        right: 25,
        bottom: (hgt*.23),
        left: 70},

    width = wid - margin.left - margin.right,
            height = hgt -margin.top- margin.bottom-15;
//     Added by shivam        
 var range="";
    if(typeof chartData[div]["barsize"]!="undefined" && chartData[div]["barsize"]!="" && chartData[div]["barsize"]=== "Thin") {
     range=0.6;
}else if(chartData[div]["barsize"] === "ExtraThin"){
    range=0.83;
}else{ 
    range=0.1;
    }
    var x = d3.scale.ordinal()
                .rangeRoundBands([0, width+15], range);
//    var x = d3.scale.ordinal()
//            .rangeRoundBands([0, width+15], .1); 
   //add condition by mayank sh. for display properties
 if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
    var y = d3.scale.linear()
            .rangeRound([height+20, 0]);
 }else{
    var y = d3.scale.linear()
            .rangeRound([height-5, 0]);
        }
         var y1 = d3.scale.linear()
            .rangeRound([height, 0]);
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){
     margin = { top: 8,  right:-15,  bottom: (hgt+20), left: 30  };
     width = wid *.9;
}else{
     margin = { top: 5, right: 25, bottom: (hgt*.2), left: 70};
  }  //end  by mayank sh. for display properties
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
                .scale(y1)
                .orient("left")
                .ticks(customTicks)
                .tickFormat(function(d, i) {
            if(typeof displayY !=="undefined" && displayY =="Yes"){
                  if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div)) +"%";
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div) +"%";
                }
                }else {
                    return "";
                }
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
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
    var localColorMap = {};
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
        offset=20;
    }
    var svg = d3.select("#"+div)
   //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//             .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(height + margin.top + margin.bottom +40)+" ")
             .attr("id", "svg_" + div)
             .attr("viewBox", "0 0 "+(wid)+" "+(height + margin.top + margin.bottom+60)+" ")
//            .attr("width",wid)
//            .attr("height", height + margin.top + margin.bottom)
//            .attr("height", hgt-50 )
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + (margin.top+15+offset) + ")");

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(measureArray).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
                return "gradient" + (d).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
                if (typeof centralColorMap[d.toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d.toLowerCase()];
                } else {
                    colorShad = color(i);
                }
                if (typeof localColorMap[d] === "undefined" || localColorMap[d] === "undefined") {
                    localColorMap[d] = colorShad;
                }
                if (typeof chartMap[d] === "undefined") {
                    chartMap[d] = colorShad;
                    colorMap[i] = d + "__" + colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
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

//    data = data.sort(function(a, b) {
//        return b["total"] - a["total"];
//    });
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

    var max = 0;
var minVal = 0;
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="MinMax" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["axisMax"]!="0" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
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
       if(measArr[0]>=0 && minVal<0){
           minVal=0;
       }
    }
//          if (data.length > 1) {
//        minVal = minimumValue(data, measureArray[0]) * .8;
//        alert(minVal)
//    }else{
//        minVal = 0;
//       }
       }



   y.domain([parseFloat(minVal), parseFloat(max)]);
   y1.domain([0, 100]);

    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){ 
 if(typeof chartData[div]["displayYLine"]==="undefined" || chartData[div]["displayYLine"]==="" || chartData[div]["displayYLine"]==="Yes"){   
  svg.append("line")
        .attr("x1",0)
        .attr("y1",0)
        .attr("x2",0)
        .attr("y2",(hgt*.75))
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
//add condition by mayank sh. for display properties

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .selectAll('text')
            .text(function(d,i) {
               if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i);
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
            return 20;   
            }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return 2;
        }else {
            return 5;
        }
    })  
    .style('font-size',function(d,i) {
                return font1(div,d,i);
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
//add condition by mayank sh. for display properties
if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}
else{
     svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .selectAll('text')
            .attr("y",0)
            .attr("x",-9)
            .attr("dy", ".32em")
            .style('text-anchor','end')
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
              var ht = parseFloat(hgt)* .65;
          }  //end  by mayank sh. for display properties

    var state = svg.selectAll(".state")
            .data(data)
            .enter().append("g")
            .attr("class", "g")
            .attr("transform", function(d) {
                return "translate(" + (x(d[columns[0]])-x(d.x))  + ",0)";
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
            .attr("width", x.rangeBand())
//            .attr("rx", 3)
            .attr("y", function(d,i) {
                var per=d.y1/measureSum[counter]*100;
                per=per.toFixed(0);
                
                if(i==measureArray.length-1){
                    counter++;
                }
                return y(maxSum*per/100);
            })
            .attr("height", function(d,i) {
                var per=d.y0/measureSum[counter1]*100;
                per=per.toFixed(0);
                var per1=d.y1/measureSum[counter1]*100;
                per1=per1.toFixed(0);
                
                if(i==measureArray.length-1){
                    counter1++;
                }
                return y(maxSum*per/100) - y(maxSum*per1/100);
            })
//            .attr("x",0) //Edit by shivam
     .attr("x", function(d) {
                return x(d.x);
            })
            .style("fill", function(d,i) {
//                return "green";//color(j);//"url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
         var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.viewBy) !== -1) {
                        return drillShade;
                    }
//            return colorSet(i);

return getDrawColor(div, parseInt(i));

            })
            .attr("id", function(d, i) {
                return d.viewBy;
            })
            .attr("onclick", fun)
    
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
            .on("mouseover", function(d, i) {
                var msrData;

            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                    msrData = addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){
               msrData = addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(d.value);
                }
            else{
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
                 msrData = addCurrencyType(div, getMeasureId(d["name"]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
            }

                var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.viewBy + "</span><br/>";
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";//Added by shivam
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
//            return colorSet(i);

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
//  for(var i=0;i<measureArray.length;i++){
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
//                    var xvalue =x(d.x);
                     var xvalue =x(d.x)+x.rangeBand()/2;
                    var yValue = y(d.y1);
                    var startVal=y(d.y0);
                    var per=d.y1/measureSum[counter]*100;
                per=per.toFixed(0);
                yValue = y(maxSum*per/100);
                    per=d.y0/measureSum[counter]*100;
                per=per.toFixed(0);
                
                if(i==measureArray.length-1){
                    counter++;
                }
                startVal = y(maxSum*per/100);
                  if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom"){
                      return "translate(" + (xvalue) + ", " + (startVal-3)+ ")";
               }   //Added by Shivam// update by mayank sharma
                  if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBottom-Bar"){
                       if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){
                   return "translate(" + (xvalue) + ", " + (height+(height/6)+2)+ ")";    
                   }else{
                      return "translate(" + (xvalue) + ", " + (height+(height/30)+2)+ ")";
               }}  
                else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop"){
                   return "translate(" + (xvalue) + ", " + (yValue-3) + ")";
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnBar"){
                   return "translate(" + (xvalue) + ", " + (yValue+10) + ")";
               }else if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[i]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[i]] === "OnTop-Bar"){
                     return "translate(" + (xvalue) + ", -2 )"; 
               }else{
         return "translate(" + (xvalue) + ", " + ((yValue+((startVal-yValue)/2))+4) + ")"; 
               }
                })
                 .style("text-anchor","middle")
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
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='Absolute'){
                        if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                           return addCurrencyType(div, getMeasureId(d["name"]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                }else {
                    return "";
                                } 
                               }else {
                   return addCurrencyType(div, getMeasureId(d["name"]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                }
                    }else{
                       if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                             var percentage = percentageValue;
                             if(percentage<5){
                                 return "";
                             }
                    return percentage.toFixed(1) + "%";
                }else {
                    return "";
                                }
                            }else {
                    var percentage1 = (d.value / parseFloat(sum[i])) * 100;
                    return percentage1.toFixed(1) + "%";
                            } }
                        }else {
                        if(typeof chartData[div]["dataLabelType"]==='undefined' || typeof chartData[div]["dataLabelType"][measureArray[i]]=='undefined' || chartData[div]["dataLabelType"][measureArray[i]]=='%-wise'){
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
                            } }
                        else{
                            if(typeof chartData[div]["dataDisplayArr"] != "undefined"){
                                if(typeof chartData[div]["dataDisplayArr"][measureArray[i]] === "undefined" || chartData[div]["dataDisplayArr"][measureArray[i]] == "Yes"){
                                    return addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                                }else {
                                    return "";
            }
                            }else {
                                return addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
                            }} 
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
//           }
for(var i=0;i<measureArray.length;i++){
     if(typeof chartData[div]["measureAvg"] === "undefined" || typeof chartData[div]["measureAvg"][measureArray[i]] === "undefined" || chartData[div]["measureAvg"][measureArray[i]] === "No"){}else{
     var sum = d3.sum(data, function(d) {
        return d[measureArray[i]];
    });
    var avg = sum/data.length;
     svg.append("text")
//              .text(avg)
               .text(addCurrencyType(div, getMeasureId(measureArray[i]))+addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
              .attr("x", (width)*0.85)
              .attr("y", y(parseInt(avg))-5);
    
      var Averageline = d3.svg.line()
            .x(function(d,i) {
        return x(d[columns[0]])
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
 // defineTargetline
     for(var i=0;i<measureArray.length;i++){   // stackebar targetline
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
                return x(d[columns[0]]);
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
            var path = svg.append("path")
            .data(data)
            .attr("d", valuelineH(data))
            .attr("fill", "transparent")
            .attr("x", (width)*.95)
            .style("z-index", "9999")
            .style("stroke-width", "1px")
            .style("stroke", "black");
        }
    }       
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
count=0;           
var boxW=wid;
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
for(var i in  measureArray){
if(fromoneview!='null'&& fromoneview=='true'){
div=div1
     }
     if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
               viewByHgt=0;
           }
           var rectsize;
           if(width<height){
              rectsize = parseInt(width/25);
           }
           else{
              rectsize = parseInt(height/25);
           }
           rectsize=rectsize>10?10:rectsize;
           svg
           //.append("g")
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
        getSlider1(div,data,wid,hgt,minimumValue(data, meassures[meassureIds.indexOf(l)]),maximumValue(data, meassures[meassureIds.indexOf(l)]),l,measureArray[meassureIds.indexOf(l)],slidercount);
        window.measureCount=window.measureCount+1;     

}
    
}    

}
}
function getViewByValueCount(val,data,columns){
    var count=0;
    for(var i in data){
        if(data[i][columns[0]]===val){
            count++;
        }
    }
    return count;
}

function font1(div,d,i){
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["xAxisFont"]!=='undefined' &&  chartData[div]["xAxisFont"]!=="Select"){
              return (chartData[div]["xAxisFont"]+"px");
          }else{
                 return "10px";
              }
          
}
function font2(div,d,i){
    var chartData = JSON.parse(parent.$("#chartData").val());
    if(typeof chartData[div]["yAxisFont"]!=='undefined' &&  chartData[div]["yAxisFont"]!=="Select"){
              return (chartData[div]["yAxisFont"]+"px");
          }else{
                 return "10px";
              }
          
}
function transformation(div,d,i){
    var chartData = JSON.parse(parent.$("#chartData").val());
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "rotate(-85)";
        }else {
            return "rotate(-30)";
        }
          
}
function textAnchor(div,d,i){
    var chartData = JSON.parse(parent.$("#chartData").val());
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "middle";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "middle";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "end";
        }else {
            return "end";
        }
          
}