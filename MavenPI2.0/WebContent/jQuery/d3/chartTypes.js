var tooltip = CustomTooltip("my_tooltip", "auto");
var wid = $(window).width();
var hgt = $(window).height();
var round;
var precition;
var isFormatedMeasure = false;
var isDouble = false;
var isShadedColor = false;
var drillValues=[];
var drillClick=[];

var x;
var y;
var xAxis;
var yAxis;
var svg;
var splitby;
var drilledElement;
var columnMap = {};
var chartType;
try {
    chartType = JSON.parse(parent.$("#chartList").val());
} catch (e) {
}
var dashboardType;
var yAxisFontSize = ".12em";
var labelColor = "black";
var barRadius = 0;
var colorMap = {};
var color = d3.scale.category10();
var drillShade = "#91FF00";//"rgb(200,200,200)";//"#91FF00"FFD47D"85FFEF"
var centralColorMap = {};
//if (parent.isCustomColors) {
//    color = d3.scale.ordinal().range(parent.customColorList);
//}c
var kpiColor1 = ["rgba(117, 216, 117, 1)", "rgba(240, 191, 21, 1)", "#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d"];
function setMeasureFormate(rou, prec) {
    isFormatedMeasure = true;
    round = rou;
    precition = prec;
}
var shadingMeasure;
var conditionalShading = false;
var conditionalMap = {};
var conditionalMeasure;
var dataDisplay,displayType,yAxisRounding,yAxisFormat,displayX,displayY,displayLegend;
var numberRoundingArr={},numberFormatArr={};

function setColor(color1, data, measure, shadingType) {
    if (shadingType === "conditional") {
        conditionalShading = true;
        conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
        conditionalMeasure = measure;
    }
    else if (shadingType === "standard") {
        color = d3.scale.ordinal().range(data);
    } else {
        shadingMeasure = measure;
        var color2 = ColorLuminance(color1, 1);
        isShadedColor = true;
        var measureData = [];
        for (var i = 0; i < data.length; i++) {
            measureData.push(data[i][measure]);
        }
        color = d3.scale.linear()
                .domain([Math.min.apply(Math, measureData)-(Math.max.apply(Math, measureData)*.10), Math.max.apply(Math, measureData)])
                //        .range([color2, color1]);
                .range(["rgb(230,230,230)", color1]);

    }
}

function ColorLuminance(hex, lum) {

    // validate hex string
    hex = String(hex).replace(/[^0-9a-f]/gi, '');
    if (hex.length < 6) {
        hex = hex[0] + hex[0] + hex[1] + hex[1] + hex[2] + hex[2];
    }
    lum = lum || 0;

    // convert to decimal and change luminosity
    var rgb = "#", c, i;
    for (i = 0; i < 3; i++) {
        c = parseFloat(hex.substr(i * 2, 2), 16);
        c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
        rgb += ("00" + c).substr(c.length);
    }
    return rgb;
}

function setColumnMap(columMap) {
    columnMap = JSON.parse(columMap);
}





function bubble(div, data0, columns, measures, divWidth, divHeight, layout,KPIResult) {
     var color = d3.scale.category10();
    //data0=JSON.parse('[{"Category":"Computer Hardware","Gross Sales":"100.0"},{"Category":"Cameras","Gross Sales":"200.0"},{"Category":"Mobile Phones","Gross Sales":"400.0"},{"Category":"Footwear","Gross Sales":"-200.0"},{"Category":"Clothing","Gross Sales":"-300.0"},{"Category":"Watches","Gross Sales":"500.0"}]');
    var tempChartData=[];
    for(var i in data0){
        var dataMap={};
        var keys1=Object.keys(data0[i]);
        for(var k in keys1){
            dataMap[keys1[k]]=data0[i][keys1[k]];
        }
        tempChartData.push(dataMap);
    }
	var chartData = JSON.parse(parent.$("#chartData").val());
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
    var nodes = [];
    var data = [];
    var dashletid;
    var measure1 = measures[0];
    var length = measures.length;
	var data = [];
	var chartData = JSON.parse(parent.$("#chartData").val());
if(typeof chartData[div]["dataTranspose"]!="undefined" && chartData[div]["dataTranspose"]!="" && chartData[div]["dataTranspose"]=="Yes"){
for (var k=0;k<length;k++){
    var dataMap = {};
   dataMap[columns[0]]=measures[k];
   dataMap[measures[0]] = KPIResult[k];
data.push(dataMap);
}
data0 = data
 }
var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
colIds=chartData[div1]["viewIds"];
}else{
//    var prop = graphProp(div);
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
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(10);
    svg = d3.select("#" + div)
       //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWidth - layout)+" "+(divHeight  - 20 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", divWidth - layout)
//            .attr("height", divHeight  - 50)
//    var gradient = svg.append("svg:defs").selectAll("radialGradient").data(data).enter()
//            .append("svg:radialGradient")
//            .attr("id", function(d) {
//                if (d !== "undefined" && typeof d !== "undefined") {
//                    return "gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                }
//                else {
//                    return "gradient";
//                }
//            })
//            .attr("fx", "2%")
//            .attr("fy", "2%")
//            .attr("r", "50%")
//            .attr("spreadMethod", "pad");
//
//    gradient.append("svg:stop")
//            .attr("offset", "0%")
//            .attr("stop-color", "rgb(240,240,240)")
//            .attr("stop-opacity", 1);
//    gradient.append("svg:stop")
//            .attr("offset", "80%")
//
//            .attr("stop-color", function(d, i) {
//                var colorShad;
////        var drilledvalue = parent.$("#drilledValue").val().split(",");
////                var drilledvalue;
//                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                } catch (e) {
//                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
//                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
//                    colorShad = drillShade;
//                } else {
//                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
//                    } else {
//                        colorShad = color(i);
//                    }
//                }
//                if (typeof chartMap[d.name] === "undefined") {
//                    colorMap[i] = d.name + "__" + colorShad;
//                    chartMap[d.name] = colorShad;
//                }
//                return colorShad;
//            })
//            .attr("stop-opacity", 1);
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
 var prop = graphProp(div);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", divWidth)
//            .attr("height", divHeight - 50)
////            .attr("style", "margin-top:3px;")
//            .attr("onclick", "reset()")
//            .attr("class", "background");
var sum = d3.sum(data0, function(d) {
        return d[measures[0]];
    });

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
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
     return  "translate(" + d.x + ", " + (top+offset) + ")";
 }else{        return  "translate(" + (d.x-95) + ", " + (top+offset) +  ")";
            }
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
            .attr("fill", function(d,i) {
                var drilledvalue;
                 try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                } catch (e) {
                }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data0[i][columns[0]]) !== -1) {
                        return drillShade;
                    }else{
                        if(tempChartData[i][measures[0]]<0){
                       return "#FF4C4C"; 
                    }
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
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
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data0[i][columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                if(tempChartData[i][measures[0]]<0){
                       return "#FF4C4C"; 
                    }
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
                return colorfill;
                }
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
                prevColor = getDrawColor(div, parseInt(i));
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
                var content = "";
                for (var no = 0; no < measures.length; no++) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.value, round, precition);
//                        alert("a");
                    }
                    else {
                        if (no === 0) {
//                            msrData = addCommas(d.value);
                          msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));//Added by shivam
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
            }).attr("onclick", fun);
    var max = maximumValue(data0, measures[0]);
    var min = minimumValue(data0, measures[0]);
    var temp = {};
    temp["min"] = min;
    temp["max"] = max;
    node.append("text")
            .attr("text-anchor", "middle")
            .attr("class","gFontFamily")
            .attr("y", "-10")
            .attr("dy", ".2em")
            .attr("fill",  function(d, i){
               var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size",function(d, i){
             var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["13", "8"]);
             var fontSize = scale(data0[i][measures[0]]);
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return fontSize+"px";
              }
            })
           .text(function(d,i){
             var percentage = (d.value / parseFloat(sum)) * 100;
             if(d.r<=19){
                return "";
             }else{
                  if (typeof chartData[div]["rounding"] === "undefined" || chartData[div]["rounding"] === "0") {
               return percentage.toFixed(1) + "%";
                  }else  if (chartData[div]["rounding"] === "2"){
                      return percentage.toFixed(2) + "%";
                  }else{
                  return percentage.toFixed(1) + "%";
             }
             }
            });

            node.append("text")
            .attr("text-anchor", "middle")
            .attr("class","gFontFamily")
            .attr("dy", "1.2em")
            .attr("y", "-8")
            .attr("fill",  function(d, i){
             var lableColor;
             if (typeof chartData[div]["labelColor"]!=="undefined") {
                lableColor = chartData[div]["labelColor"];
             }else {
                lableColor = "#000000";
             }
                return lableColor;
             })
             .style("font-size",function(d, i){
             var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["13", "8"]);
             var fontSize = scale(data0[i][measures[0]]);
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return fontSize+"px";
              }
            })
            .style("font-weight", "bold")
            .text(function(d,i){
                var absValue=tempChartData[i][measures[0]];
             if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
                if(d.r<=25){
                return "";
             }else{
                 return chartData[div]["Prefix"]+ numberFormat(absValue,yAxisFormat,yAxisRounding,div);
             }
             }else{
             if(d.r<=25){
                return "";
             }else{
             return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d.value,yAxisFormat,yAxisRounding,div));
             }
             }
            });

            node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy",function(d,i){
              if(d.r<=19){
                return "1.2em";
             }else{
                 return "2.3em"; }
            })
            .attr("y", "-6")
            .attr("class","gFontFamily")
            .attr("fill",  function(d, i){
             var lableColor;
             if (typeof chartData[div]["labelColor"]!=="undefined") {
                lableColor = chartData[div]["labelColor"];
             }else {
                lableColor = "#000000";
             }
                return lableColor;
             })
             .style("font-size",function(d, i){
             var scale = d3.scale.linear().domain([temp["max"], temp["min"]]).range(["11", "7"]);
             var fontSize = scale(data0[i][measures[0]]);
            if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return fontSize+"px";
              }
            })
            .text(function(d) {
               var diam=d.r*2;
                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
                    return d.name.substring(0, d.r / 5);
                }
                else {
                    if(d.name1.length < diam || d.name1.length>20 ){  
                    return d.name1.substring(0, d.r / 5);
                    }else{
                    return d.name1;
                }
                }
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
                    //            return color(i)
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
    // add by mayank sharma for show table
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{
  var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var widthvalue=0;
		var widthRectvalue=0;
		var rectyvalue1=0;
		var len = parseInt(divWidth-150);
		var rectlen = parseInt(divWidth-200);
		var fontsize = parseInt(divWidth*.02);
		var fontsize1 = parseInt(divWidth*.018);
		var rectsize = parseInt(divWidth/60);
                rectsize=rectsize>8?8:rectsize;
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data0.length<15 ? data0.length : 15);
                      }
    if(legendLength>12){
        yvalue = parseInt(divHeight / 7);
        rectyvalue = parseInt((divHeight / 7)-10);
    }else{
        yvalue = parseInt(divHeight / 2)-(legendLength/2)*(divHeight*.06);
        rectyvalue = parseInt((divHeight / 2)-(legendLength/2)*(divHeight*.06)-10);
    }
        widthvalue = parseInt((divWidth *.1));
        widthRectvalue = parseInt((divWidth *.1)-(divWidth*.018));
        $("#txt"+div).css("font-size",fontsize1+"px");
        $("#lbl"+div).css("font-size",fontsize1+"px");
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
        var count = 0;
        var transform = "";

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength=="Yes")){
                var startY=0;
                    if(legendLength>12){
                        startY=(divHeight / 11)
                    }else{
                        startY=(divHeight / 2-(legendLength/2)*(divHeight*.06))-(divWidth*.035)
                    }
                    svg.append("g")
            .append("text")
            .attr("style","margin-right:10")
             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + divWidth*.68  + "," + parseInt(startY) + ")")
            .attr("fill", "Black")
            .text(function(d){
//             if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
              return columns[0];
//              }else{
//              return "";
//              }
            })
            .attr("svg:title",function(d){
               return columns[0];
           })
                 for(var i=0;i<legendLength ;i++){
                     if(data0[i][measures[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+divHeight*.06)
            rectyvalue = parseInt(rectyvalue+divHeight*.06)
            }
            svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + divWidth*.65  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
                return colorfill;
			})

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + divWidth*.68  + "," + yvalue + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data0,columns,measures,i,color)
                return colorfill;
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return data0[i][columns[0]];
            } )
            .text(function(d){
                if(data0[i][columns[0]].length>25){
                    return data0[i][columns[0]].substring(0, 25);
                }else {
                    return data0[i][columns[0]];
          }
           })
           .attr("svg:title",function(d){
               return data0[i][columns[0]];
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
}// end by mayank sharma for show table

//if(typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]!="" && chartData[div]["displayLegends"]!="Yes"){}
//else{
//    count=0;
//    var boxW=(divWidth - layout);
//    var boxH=(divHeight  - 50 );
//    //var rectW=150+boxW*0.17;
//    var measureName='';
//    if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measures[0]]!='undefined' && chartData[div]["measureAlias"][measures[0]]!== measures[0]){
//        measureName=chartData[div]["measureAlias"][measures[0]];
//    }else{
//        measureName=checkMeasureNameForGraph(measures[0]);
//    }
//    var len=measureName.length;
//    if(columns[0].length+2>len){
//        len=columns[0].length+2;
//    }
//    var rectW=0;
//    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//        rectW=(measureName.length)*7+130;
//    }
//    else{
//    rectW=(measureName.length+columns[0].length)*7+130;
//}
//}
////    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
////        rectW=(measureName.length+columns[0].length)*7+130;
////    }
////    else if(chartData[div]["lbPosition"] === "topright" || chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomright" || chartData[div]["lbPosition"] === "bottomleft" || chartData[div]["lbPosition"] === "bottomcenter"){
////        rectW=30+(len*7)+85;
////    }
//    rectW = rectW<170?170:rectW;
//    var viewByHgt=15;
//    var rectH=0;   // buildbar
//if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
//        rectH=17;
//}
//else{
//    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//        rectH=10+17;
//    }
//    else{
//    rectH=10+17+viewByHgt;
//}
//}
//    var rectX;
//    if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"|| chartData[div]["lbPosition"] === "bottomcenter"){
//        rectX=boxW-rectW-20;
//    }
////    else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
////        rectX=boxW-rectW;
////    }
////    else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
////        rectX=5;
////    }
//////    else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
////        rectX=boxW-rectW-20;
//////    }
//    var rectY=-5;
//    if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
//        rectY=boxH-(boxH*1.03)+10;
//    }
//    else if(chartData[div]["lbPosition"] === 'bottomcenter'){
//        rectY=boxH-15;
//    }
//    var backColor;
//    if(typeof chartData[div]["lbColor"]!='undefined' && chartData[div]["lbColor"]!=''){
//        backColor=chartData[div]["lbColor"];
//    }
//    else{
//        backColor="none";
//    }
//    //alert((boxH-(boxH*.98)-5)+":"+boxW);
//var border=0;
//if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted'){
//    border=4;
//}
//if(typeof chartData[div]["legendBoxBorder"]=='undefined' || chartData[div]["legendBoxBorder"]=='Dotted' || chartData[div]["legendBoxBorder"]=='Solid'){
//    svg.append("g")
//    //   .attr("class", "y axis")
//    .append("rect")
//    .attr("style","margin-right:10")
//    .attr("style", "overflow:scroll")
//
//    //            .attr("x",rectlen)
//    //            .attr("y",rectyvalue)
//    .style("stroke", "grey")
//    .style("stroke-dasharray", ("3, "+border))
//    //            .attr("transform", "translate(" + width*.25  + "," + height*0.25 + ")")
//    .attr("x", rectX)
//    .attr("y", rectY)
//    .attr("width", (rectW-85))
//    .attr("height", rectH)
//    .attr("rx", 10)         // set the x corner curve radius
//    .attr("ry", 10)
//    .attr("fill", backColor);
//}
// if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//         if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){} else{
//        svg.append("g")
//        .attr("id", "viewBylbl")
//               .attr("class", "valueLabel")
//        .append("text")
//        .attr("x",rectX+10)
//        .attr("style","font-size:10px")
//        .attr("y",(rectY+12+count*15))
//        .attr("fill", 'black')
//        .text(columns[0]);
//       }}
//       else{
//if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}else {
//        svg.append("g")
//        .attr("id", "viewBylbl")
//        .append("text")
//        .attr("x",rectX+10)
//        .attr("style","font-size:10px")
//        .attr("y",(rectY+12+count*15))
//        .attr("fill", 'black')
//        .text(columns[0]);
//    }}
////    else{
////        svg.append("g")
////        .attr("id", "viewBylbl")
////        .append("text")
////        .attr("x",rectX+10)
////        .attr("style","font-size:10px")
////        .attr("y",(rectY+20+count*15))
////        .attr("fill", 'black')
////        .text(columns[0]);
////    }
//    //for(var i in  measureArray){
//    if(fromoneview!='null'&& fromoneview=='true'){
//        div=div1
//    }
//    var measureName='';
//    var offset1=0;
//    var offset2=0;
//       if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
//           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//               offset1=0;
//           }
//           else{
//        offset1=(columns[0].length*6.5+20);
//           }
//        offset2=-24;
//       }
//       else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
//           if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
//               offset2=-24;
//           }
//           else{
//               offset2=0;
//           }
//       }
//    svg.append("g")
//    .attr("id", "measure"+count)
//    .append("text")
//    .attr("x",rectX+offset1+25)
//    .attr("y",(rectY+20+offset2+viewByHgt+count*15))
//    .attr("fill", "black")
//    .text(function(d){
//        if(count>=3 &&(typeof chartData[div]["labelPosition"]!=='undefined' && (chartData[div]["labelPosition"]==='Left' || chartData[div]["labelPosition"]==='Right'))){
//            return '';
//        }
//
//        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measures[0]]!='undefined' && chartData[div]["measureAlias"][measures[0]]!== measures[0]){
//
//            measureName=chartData[div]["measureAlias"][measures[0]];
//        }else{
//            measureName=checkMeasureNameForGraph(measures[0]);
//
//        }
//        var length=0;
//                if (typeof chartData[div]["measureLabelLength"] === "undefined" || typeof chartData[div]["measureLabelLength"][measures[0]] === "undefined" || chartData[div]["measureLabelLength"][measures[0]] === "20") {
//                    length=20;
//                }
//                else{
//                    length=chartData[div]["measureLabelLength"][measures[0]];
//                }
//        if(measureName.length>length){
//            return measureName.substring(0, length)+"..";
//        }else {
//            return measureName;
//        }
//    }).attr("svg:title",function(d){
//        return measureName;
//    })
//    var rectsize;
//    if(divWidth<divHeight){
//        rectsize = parseInt(divWidth/25);
//    }
//    else{
//        rectsize = parseInt(divHeight/25);
//    }
//    rectsize=rectsize>10?10:rectsize;
//    svg.append("g")
//    //   .attr("class", "y axis")
//    .append("rect")
//    //            .attr("style","margin-right:10")
//    //            .attr("style", "overflow:scroll")
//
//    .attr("x",rectX+offset1+10)
//    .attr("y",(rectY+12+offset2+viewByHgt+count*15))
//    //            .attr("transform", "translate(" + width*.68  + "," + rectyvalue + ")")
//    .attr("width", rectsize)
//    .attr("height", rectsize)
//    .attr("fill", getDrawColor(div, parseInt(0)))
//    count++
//}

//end comment by shivam
//var rectsize;
//    if(divWidth<divHeight){
//        rectsize = parseInt(divWidth/25);
//    }
//    else{
//        rectsize = parseInt(divHeight/25);
//    }
var legendLength;
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }else{
                    legendLength=(data.length<15 ? data.length : 15);
                }
                var startY=0;
                    if(legendLength>12){
                        startY=(divHeight / 4)-(divWidth*.035)
                    }else{
                        startY=(divHeight / 2-(legendLength/2)*(divHeight*.06))-(divWidth*.035)
                    }
 if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{
             svg.append("g")    // add by mayank sharma for show table
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("class","gFontFamily")
//          .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
            .attr("transform", "translate(" + divWidth*.85  + "," + parseInt(startY) + ")")
            .attr("fill", "#696969")
            .text(function(d){
              return measures[0]
            })

            if(legendLength>12){//
          var  yvalue = parseInt(divHeight / 4);
           var rectyvalue = parseInt((divHeight / 4)-10);

}
        else{
            yvalue = parseInt(divHeight / 2)-(legendLength/2)*(divHeight*.06);
            rectyvalue = parseInt((divHeight / 2-(legendLength/2)*(divHeight*.06))-10);

        }
            var count= 0;

//           var  yvalue = parseInt(divHeight *0.2);
//            var rectyvalue = parseInt((divHeight *0.21)-10);
            for(var j=0;j<legendLength ;j++){
               if(data0[j][measures[0]]>0){
                if(j!=0){
            yvalue = parseInt(yvalue+divHeight*.06)
            rectyvalue = parseInt(rectyvalue+divHeight*.06)
             }
             svg.append("g")
            .append("text")
            .attr("class","gFontFamily")
            .attr("transform", "translate(" + divWidth*.85  + "," + (yvalue) + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#696969";
              }else{
                return  getDrawColor(div, parseInt(j));
              }}else{
               return  "#696969";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return measures[0]
            })
            .text(function(d,i){
               return circularChartsTable(div,data0,measures,j);
            })
              count++
         }
       } }// end by mayank sharma for show table
}
function buildoneviewPie(div, data, columns, measureArray,wid,hgt,regid,oneviewid,repname,repId) {
//var svg;
    var divWidth, divHeight, rad;
    var w = $(window).width()/2+"px";

    divWidth=wid;
    divHeight=hgt;
    rad=divHeight*.55;
    var autoRounding1 = "1d";
var chartData = JSON.parse(parent.$("#chartData").val());
var chartname = parent.$("#chartname").val();
//    divWidth=$("#"+div).width();
//    divHeight=$("#"+div).height();
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }

    var fun = "drillWithinchart11(this.id,\""+div+"\",\""+wid+"\",\""+hgt+"\",\""+regid+"\",\""+oneviewid+"\",\""+repname+"\",\""+repId+"\",\""+chartname+"\",'null','null')";
    if(div=='OLAPGraphRegion'){
fun = "viewAdhoctypes(this.id)";
    }
    var divnum = parseInt(div.replace("chart", "", "gi"));
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
    svg = d3.select("#" + div).append("svg:svg")
            .datum(data)
            .attr("width", divWidth*.7)
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
//                var colorShad;
//                var drilledvalue;
//                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
//                } catch (e) {
//                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    return getConditionalColor(color(i), d[conditionalMeasure]);
////                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
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
//                            colorShad = color(i);
//                        }
//                    }
//                    colorShad = color(i);
//                }
//                else {
//                    if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                    } else {
//color = d3.scale.category10();
                        var color = d3.scale.category10()
                return color(i);
//                        colorShad = "blue";
//                    }
//                }
//                chartMap[d[columns[0]]] = colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);
            var color = d3.scale.category10()
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width",divWidth)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            // .attr("class", "arc")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })

            .attr("transform", "translate(" + width / 3.2 + "," + height / 2 + ")")
            .attr("onclick", fun);

    arcs.append("path")
            .attr("fill", function(d,m) {

                return color(m);
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
                show_details(d.data, columnList, measureArray, this);

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
//                var percentage = (d.value / parseInt(sum)) * 100;
//                return percentage.toFixed(1) + "%";
//if (typeof chartData[div]["valueOf"] !== "undefined" && chartData[div]["valueOf"] === "Absolute") {
//                    if (isFormatedMeasure) {
//                        return numberFormat(d.data[measureArray[0]], round, precition);
//                    }
//                    else {
//                        return autoFormating(d.data[measureArray[0]], autoRounding1);
////                    }
//                }
//                else {
                   var percentage = (d.value / parseInt(sum)) * 100;
                return percentage.toFixed(1) + "%"
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
   if(columns[0].length > 15){
          html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0].substring(0,15)+"..</span></td></tr>";
     }else {
          html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
    }

//if()
    for(var m=0;m<data.length;m++){
        if(data[m][columns[0]].length > 16 ) {
    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 16)+"..</span> </td></tr>";
    }
else {
    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
}
}
    html +="</table></td></tr></table></div>";
//    $("#legendsTable").append(html);
    $("#"+div).append(html);
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



function editRemarks(div)
{
    var txtBox=$("#txt"+div);
    var lblBox=$("#lbl"+div);

//    var icon=$("#icon"+div);
    txtBox.show();
    lblBox.hide();
//    icon.show();
}
function displayText(txtBoxId,chartId)
{
//    alert(chartId);
    var txtBox=$("#"+txtBoxId);
    var text=$(txtBox).val();
    var chartData = JSON.parse($("#chartData").val());
//    alert(chartId);
//    alert("text :"+text);
    chartData[chartId]["remarks"] = text;
    parent.$("#chartData").val(JSON.stringify(chartData));
    txtBox.hide();
    var lblBox=$("#lbl"+chartId);
    lblBox.show();
    var h=$("#a_"+chartId).height();
    var w=$("#a_"+chartId).width();
    lblBox.html(text+"<a id='a_"+chartId+"' style='float:right;margin-right:20px;width:16px;' onclick='editRemarks(\""+chartId+"\")' class='ui-icon ui-icon-pencil'></a>");
    $("#a_"+chartId).height(h);
    $("#a_"+chartId).width(w);
}



//function buildWordCloud(data, columns, measureArray) {
//    var chartMap = {};
//    var isDashboard = parent.$("#isDashboard").val();
//    var fun = "drillWithinchart(this.id)";
//
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//    }
//    var max_amount;
//    var fill = d3.scale.category10();
//    //    data=data.sort(function(a, b) {
//    //                        return a[measureArray[0]] - b[measureArray[0]];
//    //                    });
////    max_amount = d3.max(data, function(d) {
////        return parseFloat(d[measureArray[0]]);
////    });
//    max_amount = maximumValue(data, measureArray[0]);
//    var min = minimumValue(data, measureArray[0]);
//    var wid = $(window).width();
//    var hgt = $(window).height();
//    var digonal = Math.sqrt(wid * wid + hgt * hgt);
//    var maxSize = digonal * .04;
//    var minSize = maxSize;
//    var dtList = [];
////    if (data.length < 40)
////    {
//    var max = 10 / data.length * 60;
////      var maxRange=max<35?35:max;
//    var maxRange = max < 100 ? 100 : max;
////      var minRange=(maxRange-20)<0?10:15;
//    var minRange = (maxRange - 20) < 0 ? 10 : 50;
//
//    //this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([digonal*.2/data.length, digonal*.5/data.length]);
//    this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([minSize / 2, maxSize * 2]);
////    } else {
////        this.radius_scale = d3.scale.pow().exponent(0.5).domain([0, max_amount]).range([15, 90]);
////    }
////    if(data.length<40){
////        this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([digonal*.2/data.length, digonal*.5/data.length]);
////    }else{
////        var max=60/data.length*180;
////        var maxRange=max<15?15:max;
////        var minRange=(maxRange-20)<0?5:10;
//////        this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([5, 60/data.length*100]);
////        this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([minRange, maxRange]);
////    }
////    this.radius_scale = d3.scale.pow().exponent(0.5).domain([0, max_amount]).range([0, 50]);
////    for(var m in data){
////        var map = {};
////        map["text"] = data[m][columns[0]];
////        map["size"] = this.radius_scale(parseFloat(data[m][measureArray[0]]));
////        for (var no = 0; no < measureArray.length; no++) {
////            map["value" + no] = data[m][measureArray[no]];
////        }
////        map["x"] = (wid * (Math.random() + .5)) >> 1;
////        map["y"] = (hgt * (Math.random() + .5)) >> 1;
////        dtList.push(map);
////    }
//    d3.layout.cloud().size([wid, hgt])
//            .words(data.map(function(d) {
//
//                var map = {};
//                map["text"] = d[columns[0]];
//                map["size"] = this.radius_scale(parseFloat(d[measureArray[0]]));
//                for (var no = 0; no < measureArray.length; no++) {
//                    map["value" + no] = d[measureArray[no]];
//                }
//                return map;
//            }))
//            .padding(1)
//            .rotate(function() {
//                return 0;
//            })
//            .font("Impact")
//            .fontSize(function(d) {
//                return d.size;
//            })
//            .on("end", draw)
//            .start();
//
//    function draw(words) {
//        var svg = d3.select("body").append("svg")
//                .attr("width", wid)
//                .attr("height", hgt)
//                .attr("style", "margin-top:0px")
//                .append("g")
//                .attr("transform", "translate(" + wid / 2 + "," + hgt / 2 + ")");
//        svg.append("svg:rect")
//                .attr("width", wid)
//                .attr("height", hgt)
//                .attr("onclick", "reset()")
//                .attr("class", "background");
//        svg.selectAll("text")
//                .data(words)
//                .enter().append("text")
//                .style("font-size", function(d) {
//                    return d.size - 2 + "px";
//                })
//                .style("font-family", "Impact")
//                .style("fill", function(d, i) {
//                    var colorShad;
//                    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.text.toString().toLowerCase()];
//                    } else {
//                        colorShad = fill(i);
//                    }
//                    chartMap[d.text] = colorShad;
//                    colorMap[i] = d.text + "__" + colorShad;
//                    return colorShad;
////            return fill(i);
//                })
//                .attr("color_value", function(d, i) {
//                    var colorShad;
//                    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.text.toString().toLowerCase()];
//                    } else {
//                        colorShad = fill(i);
//                    }
//                    chartMap[d.text] = colorShad;
//                    colorMap[i] = d.text + "__" + colorShad;
//                    return colorShad;
////            return fill(i);
//                })
//                .attr("text-anchor", "middle")
//                .attr("id", function(d) {
//                    return d.text + ":" + d.value;
//                })
//
//                .attr("onclick", fun)
//                .on("mouseover", function(d, i) {
//                    var circle = d3.select(this);
//                    circle.transition()
//                            .duration(100).style("opacity", 1).style("z-index", -1).style("position", "absolute")
//                            .style("font-size", function(d) {
//                                if (parseFloat(d.size) < 0) {
//                                    return 20 + "px";
//                                } else {
//                                    return d.size + 20 + "px";
//                                }
//
//                            });
//                    var content = "";
//                    var msrData;
//                    content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.text + "</span><br/>";
//                    for (var no = 0; no < measureArray.length; no++) {
//                        if (isFormatedMeasure) {
//                            msrData = numberFormat(d["value" + no], round, precition);
//                        }
//                        else {
//                            msrData = addCommas(d["value" + no]);
//                        }
//                        content += "<span class=\"name\">" + measureArray[no] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
//                    }
//                    return tooltip.showTooltip(content, d3.event);
//                })
//                .on("mouseout", function(d) {
//                    var circle = d3.select(this);
//                    circle.transition()
//                            .style("font-size", function(d) {
//                                return d.size - 2 + "px";
//                            });
//                    hide_details(d, i, this);
//                })
//                .attr("transform", function(d) {
//                    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
//                })
//                .attr("class", function(d, i) {
//                    return "bars-Bubble-index-" + d.text.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                })
//                .text(function(d) {
//                    if(d.text.length<16)
//                    return d.text;
//                else
//                   return d.text.substring(0, 15) + "..";
//                });
//        parent.$("#colorMap").val(JSON.stringify(colorMap));
//    }
//}
//
function buildWordCloud(div, data, columns, measureArray, divWid, divHgt) {
    var chartMap = {};

//    var data = [{"Auto Type":"KIT","Amount":"58998412.0"},
//        {"Auto Type":"Clustering","Amount":"37175389.0"},
//        {"Auto Type":"Segmentation","Amount":"18213772.0"},
//        {"Auto Type":"Market Basket Analysis","Amount":"12958875.0"},
//        {"Auto Type":"Churn Analysis","Amount":"9692442.0"},
//        {"Auto Type":"Factor Analysis","Amount":"5890174.0"},
//        {"Auto Type":"Logistic Regression","Amount":"4837185.0"},
//        {"Auto Type":"Classification & Regression Tree","Amount":"4422285.0"},
//        {"Auto Type":"Multivariate Regression","Amount":"2327236.0"},
//        {"Auto Type":"Covariance Analysis","Amount":"2216211.0"},
//        {"Auto Type":"Basic Time Series Analysis","Amount":"1912058.0"},
//        {"Auto Type":"Advanced Time Series Analysis","Amount":"1909595.0"},
//        {"Auto Type":"ARIMA","Amount":"1909595.0"},
//        {"Auto Type":"Principle Component Analysis","Amount":"1909595.0"},
//        {"Auto Type":"ANOVA","Amount":"1909595.0"},
//        {"Auto Type":"CHAID","Amount":"1909595.0"},
//        {"Auto Type":"Hidden Markov Model","Amount":"1909595.0"},
//        {"Auto Type":"RFM Analysis","Amount":"1909595.0"},
//        {"Auto Type":"Discriminant Analysis","Amount":"1909595.0"},
//        {"Auto Type":"Risk Analytics","Amount":"1909595.0"},
//        {"Auto Type":"Neural Network","Amount":"1909595.0"},
//        {"Auto Type":"Latent Structure Analysis","Amount":"1909595.0"},
//        {"Auto Type":"Loyalty Scoring Analysis","Amount":"1909595.0"},
//        {"Auto Type":"Canonical Analysis","Amount":"1909595.0"},
//        {"Auto Type":"Anomaly Detection","Amount":"1909595.0"},
//        {"Auto Type":"Relevance Vector Machine","Amount":"1909595.0"},
//        {"Auto Type":"Bayesian Analysis","Amount":"1909595.0"},
//
//];

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

    divWid=parseFloat($(window).width())*(.35);
     var fromoneview=parent.$("#fromoneview").val()
    var chartData = JSON.parse(parent.$("#chartData").val());
 var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){

colIds=chartData[div1]["viewIds"];
}else{

    colIds=chartData[div]["viewIds"];
}
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
    } else {
        drillWithinchart(id1,div);
    }
    }




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
    var max_amount;
    var fill = d3.scale.category10();
    max_amount = maximumValue(data, measureArray[0]);
    var min = minimumValue(data, measureArray[0]) / 2;
    var wid = divWid;
    var hgt = divHgt;
    var digonal = Math.sqrt(wid * wid + hgt * hgt);
    var maxSize = digonal * .094;
    var minSize = maxSize * .1;
    this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([minSize, maxSize]);
    d3.layout.cloud().size([wid, hgt])
            .words(data.map(function(d) {
                    var map = {};
                    map["text"] = d[columns[0]];
                    map["size"] = this.radius_scale(parseFloat(d[measureArray[0]]));
                for (var no = 0; no < measureArray.length; no++) {
                    map["value" + no] = d[measureArray[no]];
                }
                    return map;
            }))
            .padding(1)
            .rotate(function(d, i) {
//                    if (i % 2 === 0) {
                return 0;
//                    } else {
//                        return -90;
//                    }
            })
            .font("Impact")
            .fontSize(function(d) {
                return d.size;
            })
            .on("end", draw)
            .start();
    function draw(words) {
        svg = d3.select("#" + div).append("svg:svg")
                .attr("width", wid)
                .attr("height", hgt)
                .attr("id", "svg_" + div)
                .attr("style", "margin-top:0px")
                .append("g")
                .attr("id", "svg_" + div)
                .attr("transform", "translate(" + wid / 2 + "," + hgt / 2 + ")");
//        svg.append("svg:rect")
//                .attr("width", wid)
//                .attr("height", hgt)
//                .attr("onclick", "reset()")
//                .attr("class", "background");
if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
        svg.selectAll("text")
                .data(words)
                .enter().append("text")
                .style("font-size", function(d) {
                    return d.size - 2 + "px";
                })
                .style("font-family", "Impact")
                .style('cursor', 'pointer')
                .style("fill", function(d, i) {
                    var colorShad;
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d.text) !== -1) {
                        colorShad = drillShade;
                    } else {
                    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
                        colorShad = centralColorMap[d.text.toString().toLowerCase()];
                    }else {
                        colorShad = getDrawColor(div, parseInt(i));
                    }
                    }
                    chartMap[d.text] = colorShad;
                    colorMap[i] = d.text + "__" + colorShad;
                    return colorShad;
                })
                .attr("color_value", function(d, i) {
                    var colorShad;
                    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
                        colorShad = centralColorMap[d.text.toString().toLowerCase()];
                    } else {
                        colorShad = getDrawColor(div, parseInt(i));
                    }
                    chartMap[d.text] = colorShad;
//                    colorMap[i] = d.text + "__" + colorShad;
                    return colorShad;
                })
                .attr("text-anchor", "middle")
                .attr("id", function(d) {
                    return d.text + ":" + d.value;
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
                    content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.text + "</span><br/>";
                    for (var no = 0; no < measureArray.length; no++) {
                        if (isFormatedMeasure) {
                            msrData = numberFormat(d["value" + no], round, precition);
                        }
                        else {
						 if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
                            msrData = addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(d["value" + no]);
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(d["value" + no]);

                        }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(numberFormat(d["value" + no],yAxisFormat,yAxisRounding,div));
            }

                        }
                        content += "<span class=\"name\">" + measureArray[no] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                    }
                    return tooltip.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d) {
                    var circle = d3.select(this);
                    circle.transition()
                            .style("font-size", function(d) {
                                return d.size - 2 + "px";
                            });
                    hide_details(d,  this);
                })
                    .attr("transform", function(d) {
                    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
                })
                .attr("class", function(d, i) {
                    return "bars-Bubble-index-" + d.text.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                })
                .text(function(d) {
                    if(d.text.length<16)
                    return d.text;
                else
                    return d.text.substring(0, 15) + "..";
                });
        parent.$("#colorMap").val(JSON.stringify(colorMap));
    }
}


function buildMultiMeasureBubble(data, paramlength, columns, measures) {
    var measureArray = [];
    if (measures.length > 3) {
        for (var no = 0; no < 3; no++) {
            measureArray[no] = measures[no];
        }
        paramlength = 3;
    }
    else {
        measureArray = measures;
    }
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var max_amount;
    var margin = {
        top: 0,
        right: 0,
        bottom: 0,
        left: 0
    },
    width = wid - margin.left - margin.right,
            height = hgt - margin.top - margin.bottom;
    max_amount = d3.max(data, function(d) {
        return parseFloat(d.value);
    });
    if (data.length < 50) {
        this.radius = d3.scale.pow().exponent(0.1).domain([0, max_amount]).range([2, 55]);
    } else {
        this.radius = d3.scale.pow().exponent(0.1).domain([0, max_amount]).range([2, 35]);
    }
    var n = data.length,
            m = paramlength,
            padding = 12,
            x = d3.scale.ordinal().domain(d3.range(m)).rangePoints([0, width], 1);
    // cluster coloring
    var clusterColours = [];
    var getClusterColour = function(measureName) {
        for (var i = 0; i < clusterColours.length; ++i) {
            if (clusterColours[i].name === measureName) {
                return clusterColours[i].colour;
            }
        }

        var colour = {
            name: measureName,
            colour: color(clusterColours.length)
        };

        clusterColours.push(colour);

        return colour.colour;
    };

    // nodes & svg's
    var nodes = d3.range(n).map(function(a, b, c, d, e) {
        var node = data[a];
        return {
            data: node,
            color: getClusterColour(node.measures),
            radius: this.radius(node.value),
            cx: x(node.viewby),
            cy: height / 2.5
        };
    });

    function svg() {
        this.style("left", function(d) {
            return d.x + "px";
        });
    }

    // defining the position
    function position() {
        this.style("left", function(d) {
            return d.x + "px";
        })

                .style("width", function(d) {
                    return Math.max(0, d.dx - 1) + "px";
                })
                .style("height", function(d) {
                    return Math.max(0, d.dy - 1) + "px";
                });
    }

    var force = d3.layout.force()
            .nodes(nodes)
            .size([width, height])
            .gravity(-0.01)
            .charge(0)
            .on("tick", tick)
            .start();
    var title = {};
    if (measureArray.length === 1) {
        title[measureArray[0]] = width / 2;
    }
    else if (measureArray.length === 2) {
        title[measureArray[0]] = 250;
        title[measureArray[1]] = width - 250;
    } else {
        title[measureArray[0]] = 160;
        title[measureArray[1]] = width / 2;
        title[measureArray[2]] = width - 160;
    }
    var title_data = d3.keys(title);
    var svg = d3.select("body").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("style", "margin-top:30px;")
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var years = svg.selectAll(".measures").data(title_data)
            .enter().append("text").attr("class", "years")
            .attr("x", function(d) {
                return title[d];
            }).attr("y", 10).attr("text-anchor", "middle").text(function(d) {
        return d;
    })
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 11px")
            .attr("fill", "Black");


    var circle = svg.selectAll("circle")
            .data(nodes)
            .enter().append("circle")
            .attr("class", function(d) {
                return "index" + d.data.measures.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("r", function(d) {
                return d.radius;
            })
            .style("fill", function(d) {
                return d.color;
            })
            .call(force.drag)
            .attr("id", function(d) {
                return d.data.measures;
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {

                d3.selectAll(".index" + d.data.measures.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", drillShade);
                // d3.selectAll("."+d.data.supplier.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("stroke", "grey");
                // d3.selectAll("."+d.data.supplier.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("stroke-width", 3);
                // d3.select(this).attr("stroke", "grey");
                var content;
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.data.value, round, precition);
                }
                else {
                    msrData = addCommas(d.data.value);
                }
                content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.data.measures + "</span><br/>";
                content += "<span class=\"name\">" + d.data.viewby + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d) {
                d3.selectAll(".index" + d.data.measures.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", function(d) {
                    return d.color;
                });
                d3.selectAll(".index" + d.data.measures.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("stroke", function(d) {
                    return d.color;
                });
                hide_details(d, i, this);
            });
    circle.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "8pt")
            .attr("fill", "Black")
            .text(function(d) {
                return d.data.measures.substring(0, d.r / 5);
            });

    function tick(e) {
        circle
                .each(gravity(.1 * e.alpha))
                .each(collide(0.001))
                .attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                });
    }

    // Move nodes toward cluster focus.
    function gravity(alpha) {
        return function(d) {
            d.y += (d.cy - d.y) * alpha;
            d.x += (d.cx - d.x) * alpha;
        };
    }

    // Resolve collisions between nodes.
    function collide(alpha) {
        var quadtree = d3.geom.quadtree(nodes);
        return function(d) {
            var r = d.radius + radius.domain()[1] + padding,
                    nx1 = d.x - r,
                    nx2 = d.x + r,
                    ny1 = d.y - r,
                    ny2 = d.y + r;
            quadtree.visit(function(quad, x1, y1, x2, y2) {
                if (quad.point && (quad.point !== d)) {
                    var x = d.x - quad.point.x,
                            y = d.y - quad.point.y,
                            l = Math.sqrt(x * x + y * y),
                            r = d.radius + quad.point.radius + (d.color !== quad.point.color) * padding;
                    if (l < r) {
                        l = (l - r) / l * alpha;
                        d.x -= x *= l;
                        d.y -= y *= l;
                        quad.point.x += x;
                        quad.point.y += y;
                    }
                }
                return x1 > nx2
                        || x2 < nx1
                        || y1 > ny2
                        || y2 < ny1;
            });
        };
    }
}
function show_detailsoneview(d, columns, measureArray, element,chartData,div) {
    d3.select(element).attr("stroke", "grey");
    var content = "";
    var title;
    var chartDetails ="";
    var colNameVal ="";
//    var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var value = measureArray[0];
    title = columns;
//    var chartData = JSON.parse(parent.$("#chartData").val());alert(parent.$("#chartData").val())
//  div = parent.$("#chartname").val();
    if(typeof chartData !=="undefined" && chartData !==""){
         chartDetails = chartData[div];
//           colNameVal = viewOvName[viewOvIds.indexOf(chartDetails["viewBys"][0])];
           colNameVal = chartDetails["viewBys"][0];
    }



   for (var i = 0; i < columns.length; i++) {

            content += "<span class=\"name\">" + colNameVal + ":</span><span class=\"value\"> " + d[columns[i]] + "</span><br/>";

    }
    for (var i = 0; i < measureArray.length; i++) {

        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d[measureArray[i]], round, precition);
        }
        else {
            msrData = addCommas(d[measureArray[i]]);
        }

            content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";

    }
    tooltip = CustomTooltip("my_tooltip", "auto");
    return tooltip.showTooltip(content, d3.event);
}





function hide_details(data, i, element) {
//    d3.select(element).attr("stroke", "");
    return tooltip.hideTooltip();
}

function hide_details1(data, i, element) {
    d3.select(element).attr("stroke", "");
    return tooltip1.hideTooltip();
}

function show_details(d, columns, measureArray, element,div) {
//    d3.select(element).attr("stroke", "grey");
    var content = "";
    var chartData = JSON.parse(parent.$("#chartData").val());
    
    
    for (var i = 0; i < measureArray.length; i++) {
        var name = checkMeasureNameForGraph(measureArray[i]);
        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d[measureArray[i]], round, precition);
        }
        else {
            msrData = addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
        }
        if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
            var meaName=measureArray[i];
            if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
            content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData +" (" +((d[measureArray[i]] / parseFloat(window.abSum)) * 100).toFixed(1) + "%) </span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray[i] + "</span><span class=\"value\"> " + msrData + "</span><br/>";
            }
    }
        else {
            var meaName=measureArray[i];
            try{
           if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !=="" && chartData!=="undefined"  && typeof (chartData[div]["tooltipType"][measureArray[i]])!=="undefined"){
              if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="With%"){
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" +((d[measureArray[i]] / parseFloat(window.abSum)) * 100).toFixed(1) + "% </span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";
            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default-With%"){
                 content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData +" (" +((d[measureArray[i]] / parseFloat(window.abSum)) * 100).toFixed(1) + "%) </span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";
            }else{
              content += "<span style=\"font-family:helvetica;\" class=\"name\">" +msrData+ "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";  
            }
                
            }else {
//               content += "<span class=\"name\">" + measureArray[i] + "</span><span class=\"value\"> " + msrData + "</span><br/>";
                 content += "<span style=\"font-family:helvetica;\" class=\"name\">" +msrData+ "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";  
            }
	}catch(err){
		 content += "<span style=\"font-family:helvetica;\" class=\"name\">" +msrData+ "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";  
        }
        }
        if(window.com){ content= "";
          content += "<span style=\"font-family:helvetica;\" class=\"name\">" +msrData+ "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";    
    }
    }
    return tooltip.showTooltip(content, d3.event);
}
function show_details1(d, columns, measureArray, element,div) {
    d3.select(element).attr("stroke", "grey");
    var content = "";
    var title;
    var chartDetails ="";
    var colNameVal ="";
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var value = measureArray[0];
    title = columns;
    var chartData = JSON.parse(parent.$("#chartData").val());
//    if(typeof chartData !=="undefined" && chartData !==""){
//         chartDetails = chartData[div];
//           colNameVal = viewOvName[viewOvIds.indexOf(chartDetails["viewIds"][0])];
//    }
colNameVal=columns[0];


//    var measNameVal;
//    for(var h=0;h<chartDetails["meassureIds"].length;h++){
//        var measV=[];
//        measV.push(chartDetails["meassureIds"].indexOf(chartDetails["meassureIds"][h]));
////    measNameVal = chartDetails["meassures"][chartDetails["meassureIds"].indexOf(chartDetails["meassureIds"][h])];
//    }
//    chartDetails["meassures"]=measV;
   for (var i = 0; i < columns.length; i++) {
        if (typeof columnMap[columns[i]] !== "undefined" && columnMap[columns[i]]["displayName"] !== "undefined") {
            var colName=columns[i];
//            if(typeof nameMap[columns[0]]!="undefined" || nameMap[viewOvIds[viewOvName.indexOf(columns[0])]]){
//                colName=nameMap[viewOvIds[viewOvName.indexOf(columns[0])]];
//            }

            content += "<span class=\"name\">" + colNameVal + ":</span><span class=\"value\"> " + d[columns[i]] + "</span><br/>";
        }
        else {
            var colName=columns[i];
//             if(typeof nameMap[columns[0]]!="undefined"){
//                colName=nameMap[viewOvIds[viewOvName.indexOf(columns[0])]];
//            }
            content += "<span class=\"name\">" + colNameVal + ":</span><span class=\"value\"> " + d[columns[i]] + "</span><br/>";
        }
    }
    for (var i = 0; i < measureArray.length; i++) {

        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d[measureArray[i]], round, precition);
        }
        else {
            msrData = addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(d[measureArray[i]]);
        }
        if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
            var meaName=measureArray[i];
//            if(measIds.indexOf(columns[i])!="undefined"){
//                meaName=measName[measIds.indexOf(measureArray[i])];
//            }
            content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
        }
        else {
            var meaName=measureArray[i];
//            if(measIds.indexOf(columns[i])!="undefined"){
//                meaName=measName[measIds.indexOf(measureArray[i])];
//            }
            content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
        }
    }
    return tooltip1.showTooltip(content, d3.event);
}

function synchronizedMouseOver() {
    var bar = d3.select(this);
    var indexValue = bar.attr("index_value");

    var barSelector = "." + "bars-Bubble-" + indexValue;
    var selectedBar = d3.selectAll(barSelector);
    selectedBar.style("fill", drillShade);

    var bulletSelector = "." + "bars-Bubble-legendBullet-" + indexValue;
    var selectedLegendBullet = d3.selectAll(bulletSelector);
    selectedLegendBullet.style("fill", drillShade);

    var textSelector = "." + "bars-Bubble-legendText-" + indexValue;
    var selectedLegendText = d3.selectAll(textSelector);
    selectedLegendText.style("fill", drillShade);
}

function synchronizedMouseOverLegends(indexValue) {
    var barSelector = "." + "bars-Bubble-" + indexValue;
    var selectedBar = d3.selectAll(barSelector);
    selectedBar.style("fill", drillShade);

    var bulletSelector = "." + "bars-Bubble-legendBullet-" + indexValue;
    var selectedLegendBullet = d3.selectAll(bulletSelector);
    selectedLegendBullet.style("fill", drillShade);

//    var textSelector = "." + "bars-Bubble-legendText-" + indexValue;
//    var selectedLegendText = d3.selectAll(textSelector);
//    selectedLegendText.style("fill", drillShade);
}
;

function synchronizedMouseOut() {
    var bar = d3.select(this);
    var indexValue = bar.attr("index_value");
    var barSelector = "." + "bars-Bubble-" + indexValue;
    var selectedBar = d3.selectAll(barSelector);
    try {
        var colorValue = selectedBar.attr("color_value");
        selectedBar.style("fill", colorValue);
    } catch (e) {
    }

    var bulletSelector = "." + "bars-Bubble-legendBullet-" + indexValue;
    var selectedLegendBullet = d3.selectAll(bulletSelector);
    try {
        colorValue = selectedLegendBullet.attr("color_value");
        selectedLegendBullet.style("fill", colorValue);
    } catch (e) {
    }

    var textSelector = "." + "bars-Bubble-legendText-" + indexValue;
    var selectedLegendText = d3.selectAll(textSelector);
    selectedLegendText.style("fill", colorValue);
//    $("#tabDiv").hide();
}
;
function synchronizedMouseOutLegends(indexValue) {
    var barSelector = "." + "bars-Bubble-" + indexValue;
    var selectedBar = d3.selectAll(barSelector);
    var colorValue = selectedBar.attr("color_value");
    selectedBar.style("fill", colorValue);

    var bulletSelector = "." + "bars-Bubble-legendBullet-" + indexValue;
    var selectedLegendBullet = d3.selectAll(bulletSelector);
    var colorValue = selectedLegendBullet.attr("color_value");
    selectedLegendBullet.style("fill", colorValue);

    var textSelector = "." + "bars-Bubble-legendText-" + indexValue;
    var selectedLegendText = d3.selectAll(textSelector);
    selectedLegendText.style("fill", colorValue);
}
;

function buildDashboard4(div,data) {

    parent.advanceData = data; // add by mynk sh.
    var color = d3.scale.category12();
var chartData = JSON.parse(parent.$("#chartData").val());
        var charts = Object.keys(chartData);
        var width = $(window).width();
        var divHeight = $(window).height();
        var divContent = "";
        var widths = [];
        var heights = [];
        var chartId = div;  // add by mynk sh.
        divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
        if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "combo-chart"){  // add by mynk sh.
            heights.push(divHeight/1.5);
            heights.push(divHeight/1.2);
            width*=0.7;
            divContent += "<div id='chartCom1' style='float: left; display: block; width: 50%;'>";  // add by mynk sh. for combo chart
            divContent += "<div id='Graphdiv1' style='display:block;float:left;width:100%;'>";
            divContent+="<table style='width:100%'><tr style='width:100%'><td style='width:1%;float:right;  margin-right:5%'><div class='dropdown' style='cursor:pointer'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16'   src='images/chart1.png' alt='Options'   ></img></span>"+
        "<ul id='chartTypesZ"+chartId+"' class='dropdown-menu'>";
        var graphTypes = Object.keys(parent.graphsName);
        for(var i=0;i<parent.advanceChartGroups.length;i++)
        {
            divContent+="<li id='chartGroupsZ"+chartId+i+"' class='dropdown-submenu pull-left'><a onmouseover='openChartGroup1("+i+",\""+chartId+"\",\"Z\")' >"+parent.advanceChartGroups[i]+"</a>";
        }
        chartId = "chart2"
        divContent+="</ul></td></tr></table>";
            divContent +="</div></div>";
            divContent += "<div id='chartCom2' style='float: left; display: block; width: 50%;'>";// chng
            divContent += "<div id='Graphdiv2' style='display:block;float:left;width:100%;'>";
            divContent+="<table style='width:100%'><tr style='width:100%'><td style='width:1%;float:right; margin-right:5%'><div class='dropdown' style='cursor:pointer'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16'  src='images/chart1.png' alt='Options'   ></img></span>"+
        "<ul id='chartTypesY"+chartId+"' class='dropdown-menu'>";
        var graphTypes = Object.keys(parent.graphsName);
        for(var i=0;i<parent.advanceChartGroups.length;i++)
        {
            divContent+="<li id='chartGroupsY"+chartId+i+"' class='dropdown-submenu pull-left'><a onmouseover='openChartGroup1("+i+",\""+chartId+"\",\"Y\")' >"+parent.advanceChartGroups[i]+"</a>";
        }
        divContent+="</ul></td></tr></table>";

            divContent +="</div></div>";// end  by mynk sh. for combo chart
        }
        else{
        switch (charts.length) {
            case 1:
                {
                    widths.push(width / 1.2);
                    divHeight = $(window).height();
                    divContent += "<div id='chartAd1' style='float:left;'></div>";
                    break;
                }
            case 2:
                {
                    widths.push(width / 1.2);
                    widths.push(width / 1.2);
//                    if (chartList[0] === 'Bubble-Scatter' || chartList[1] === 'Bubble-Scatter' || chartList[0] === 'Bar' || chartList[1] === 'Bar') {
//                        divHeight = $(window).height() / 2.5;
////                        if(chartList[0]=="Bar" && chartList[1] !== 'Bubble-Scatter'){
////                        divContent += "<div id='chart1' ></div></td></tr><tr><td><div id='chart2' ></div>";
////                    }
//                        if (chartList[0] === 'Bubble-Scatter') {
//                            divContent += "<div id='chart1' ></div></td></tr><tr><td><div id='chart2' style='margin-top:-100px'></div>";
//                        }
//                        else {
//                            divContent += "<div id='chart1' ></div></td></tr><tr><td><div id='chart2' style=''></div>";
//                        }
//                    }
//                    else {
                        divHeight = $(window).height();
                        divContent += "<div id='chartAd1' style='float:left;'></div></td></tr><tr><td><div id='chartAd2' style='float:left'></div>";
//                    }
                    break;
                }
            default :
                {
                    widths.push(width / 1.2);
                    widths.push(width / 1.2);
                    divHeight = $(window).height();
                    divContent += "<div id='chartAd1' style='float:left;'></div></td></tr><tr><td><div id='chartAd2' style='float:left'></div>";
                    break;
                }
        }
        }

        $("#"+div).html(divContent);

        for (var ch in charts) {
            var chartDetails = chartData[charts[ch]];
var chartType;
var chartList=[];
//           if(typeof chartList[ch] !== "undefined"){
//           chartType = chartList[ch];
//           }
//           else{
             chartList.push("Bubble");

           chartType = "Bubble";
//             parent.$("#chartList").val(JSON.stringify(chartList));
//           }

//            var dataPath = tarPath + "/" + charts[ch] + ".json";
            var chartId = "chart" + (parseInt(ch) + 1);
            var bubbleDivId = "chartAd" + (parseInt(ch) + 1);
            if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "combo-chart"){
                buildDashlet(chartDetails, ch, width, heights[ch], chartType, chartId,bubbleDivId, chartList);
            }
            else{
            buildDashlet(chartDetails, ch, widths[ch], divHeight, chartType, chartId,bubbleDivId, chartList);
            }

        }

//    }

    function buildDashlet(chartDetails, chartIndex,  divWidth, divHeight, chart, chartId,bubbleDivId, chartList) {
        var viewbys = [];
        var viewbyVals = chartDetails.viewBys;
        viewbys.push(viewbyVals[0]);
        var measures = chartDetails.meassures;
        if (chart === "Bubble") {


                var data3 = data[chartId];

                var mindata2 = [];
                var rows = 15;
                for (var i = 0; i < data3.length; i++) {
                    if (i < 15) {
                        mindata2.push(data3[i]);
                    } else {
                        break;
                    }
                }
                var sdata3 = mindata2;
                var nodes = [];
                if (data3.length > 500) {
                    rows = 500;
                } else {
                    rows = data3.length;
                }
                data3.forEach(function(d, i) {
                    if (i < rows && d[measures[0]] >= 0) {
                        var node = {};
                        node["name"] = d[viewbys[0]];

                        if (viewbys.length > 1) {
                            node["name1"] = d[viewbys[1]];
                        } else {
                            node["name1"] = d[viewbys[0]];
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

               try
               {
                if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "combo-chart"){
                    if(chartIndex==0){
                   var divId = "chartCom1";
                    var records=15;// add for no of records by mynk sh.
                     var currData = [];

                     for(var h=0; h<(data3.length < records ? data3.length : records); h++){

                            currData.push(data3[h]);
                             }  //end for no of records

                        buildBarAdvance(chartId,divId,currData,viewbys,measures,200,100);
                    }
                    else{
                         var divId = "chartCom2";
                        buildComboPie(chartId,divId,data3,viewbys,measures,divWidth*0.6,divHeight * .5);
                    }
                }
                else{
                  bubbleDb(chartId,bubbleDivId, data3, viewbys, measures, divWidth * .5, divHeight * .8, 0);
                }

               }catch (e){

               }
//                });
//            }
            }
        }
    }



function circlePacking(div, data, columns, measureArray, width, height) {
    var chartMap = {};
    var w = wid - 50,
            h = hgt - 50,
            r = h - 5,
            x = d3.scale.linear().range([0, r]),
            y = d3.scale.linear().range([0, r]),
            node,
            root;

    var pack = d3.layout.pack()
            .size([r, r])
            .value(function(d) {
                return d.size;
            });

    var vis = d3.select("#"+div).insert("svg:svg", "h2")
            .attr("width", w)
            .attr("height", h)
            .append("svg:g")
            .attr("transform", "translate(" + (w - r) / 2 + "," + (h - r) / 2 + ")");

    node = root = data;
    var nodes = pack.nodes(root);
    vis.selectAll("circle")
            .data(nodes)
            .enter().append("svg:circle")
            .attr("class", function(d) {
                return d.children ? "parent" : "child";
            })
            .attr("cx", function(d) {
                return d.x;
            })
            .attr("fill", function(d, i) {
                var colorShad;
                if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d.name.toString().toLowerCase()];
                } else {
                    colorShad = "#ccc";
                }
                chartMap[d.name] = colorShad;
                colorMap[i] = d.name + "__" + colorShad;
                return colorShad;
            })
            .attr("cy", function(d) {
                return d.y;
            })
            .attr("r", function(d) {
                return d.r;
            })
            .on("click", function(d) {
                return zoom(node === d ? root : d);
            })
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition);
                }
                else {
                    msrData = addCommas(parseFloat(d.value).toFixed(0));
                }
                var content;
                content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    parent.$("#colorMap").val(JSON.stringify(colorMap));

    vis.selectAll("text")
            .data(nodes)
            .enter().append("svg:text")
            .attr("class", function(d) {
                return d.children ? "parent" : "child";
            })
            .attr("x", function(d) {
                return d.x;
            })
            .attr("y", function(d) {
                return d.y;
            })
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .style("opacity", function(d) {
                return d.r > 25 ? 1 : 0;
            })
            .text(function(d) {
                return d.name;
            });

    d3.select(window).on("click", function() {
        zoom(root);
    });

    function zoom(d, i) {
        var k = r / d.r / 2;
        x.domain([d.x - d.r, d.x + d.r]);
        y.domain([d.y - d.r, d.y + d.r]);

        var t = vis.transition()
                .duration(d3.event.altKey ? 7500 : 750);

        t.selectAll("circle")
                .attr("cx", function(d) {
                    return x(d.x);
                })
                .attr("cy", function(d) {
                    return y(d.y);
                })
                .attr("r", function(d) {
                    return k * d.r;
                });

        t.selectAll("text")
                .attr("x", function(d) {
                    return x(d.x);
                })
                .attr("y", function(d) {
                    return y(d.y);
                })
                .style("opacity", function(d) {
                    return k * d.r > 25 ? 1 : 0;
                });

        node = d;
        d3.event.stopPropagation();
    }

}

function buildcoffeeWheel(div, data, columns, measureArray) {
    var chartMap = {};
    var width = wid - 300;
    height = hgt-150,
            radius = Math.min(width, height - 40) / 2;

    var x = d3.scale.linear()
            .range([0, 2 * Math.PI]);

    var y = d3.scale.sqrt()
            .range([0, radius]);
    var svg = d3.select("#"+div).append("svg")
            .attr("width", width)
            .attr("height", height - 10)
            .attr("style", "margin-left:120px")
            .append("g")
            .attr("transform", "translate(" + width / 2 + "," + (height / 2 + 10) + ")");

    var color = d3.scale.category10();
    var partition = d3.layout.partition()
            .value(function(d) {
                return d.size;
            });
    var parentColorMap = {};
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(partition.nodes(data)).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
//                return "gradient" + ((d.children ? d : d.parent).name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
return d;
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
            .attr("offset", "50%")
            .attr("stop-color", function(d, i) {
                var colorShad;
                if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d.name.toString().toLowerCase()];
                } else {
                    colorShad = color(i);
                }

                if (typeof parentColorMap[((d.children ? d : d.parent).name)] === "undefined") {
                    parentColorMap[((d.children ? d : d.parent).name)] = colorShad;
                }
                if (typeof chartMap[d.name] === "undefined") {
                    chartMap[((d.children ? d : d.parent).name)] = parentColorMap[((d.children ? d : d.parent).name)];
                    colorMap[i] = d.name + "__" + parentColorMap[((d.children ? d : d.parent).name)];
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
    var arc = d3.svg.arc()
            .startAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x)));
            })
            .endAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx)));
            })
            .innerRadius(function(d) {
                return Math.max(0, y(d.y));
            })
            .outerRadius(function(d) {
                return Math.max(0, y(d.y + d.dy));
            });

    var g = svg.selectAll("g")
            .data(partition.nodes(data))
            .enter().append("g");

    var path = g.append("path")
            .attr("d", arc)
            .style("fill", function(d,i) {
                return color(i)
//                return "url(#gradient" + ((d.children ? d : d.parent).name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
//            .attr("color_value", function(d, i) {
//                return "url(#gradient" + ((d.children ? d : d.parent).name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
////            return fill(i);
//            })
//            .attr("class", function(d, i) {
//                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
//            })
            .on("click", click)
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition);
                }
                else {
                    msrData = addCommas(d.value);
                }
                var content;
                content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var text = g.append("text")
            .attr("transform", function(d) {
                return "rotate(" + computeTextRotation(d) + ")";
            })
            .attr("x", function(d) {
                return y(d.y);
            })
            .attr("dx", "6") // margin
            .attr("dy", ".35em") // vertical-align
            .text(function(d) {
                if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
                    if (d.length < 14)
                        return d.name;
                    else
                        return d.name.substring(0, 7) + "..";
                }
            });

    function click(d) {
        // fade out all text elements
        text.transition().attr("opacity", 0);

        path.transition()
                .duration(750)
                .attrTween("d", arcTween(d))
                .each("end", function(e, i) {
                    // check if the animated element's data e lies within the visible angle span given in d
                    if (e.x >= d.x && e.x < (d.x + d.dx)) {
                        // get a selection of the associated text element
                        var arcText = d3.select(this.parentNode).select("text");
                        // fade in the text element and recalculate positions
                        arcText.transition().duration(750)
                                .attr("opacity", 1)
                                .attr("transform", function() {
                                    return "rotate(" + computeTextRotation(e) + ")";
                                })
                                .attr("x", function(d) {
                                    return y(d.y);
                                })
                                .text(function(d) {
                                    if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
                                        if (d.length < 14)
                                            return d.name;
                                        else
                                            return d.name.substring(0, 7) + "..";
                                    }
                                });
                    }
                });
    }

    d3.select(self.frameElement).style("height", height + "px");

    // Interpolate the scales!
    function arcTween(d) {
        var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
                yd = d3.interpolate(y.domain(), [d.y, 1]),
                yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
        return function(d, i) {
            return i
                    ? function(t) {
                        return arc(d);
                    }
            : function(t) {
                x.domain(xd(t));
                y.domain(yd(t)).range(yr(t));
                return arc(d);
            };
        };
    }

    function computeTextRotation(d) {
        return (x(d.x + d.dx / 2) - Math.PI / 2) / Math.PI * 180;
    }


}

function buildcoffeeWheelLegend(div, data, columns, measureArray) {
    var width = wid - 500,
            height = hgt,
            radius = Math.min(width, height - 58) / 2;

    var x = d3.scale.linear()
            .range([0, 2 * Math.PI]);

    var y = d3.scale.sqrt()
            .range([0, radius]);
    var b = {
        w: 75,
        h: 30,
        s: 3,
        t: 10
    };
    var totalSize = 0;
    //    var totalSize=0;
    //    var divcontent=
    initializeBreadcrumbTrail();
    colorMap = {};
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var divcontent = " <div id='main'><div id='sequence'></div><div id='chart'><div id='explanation' style='visibility: hidden'><span id='percentage'></span><br/></div></div></div>";
    $("body").append(divcontent);
    var svg = d3.select("#chart").append("svg").attr("id", "coff")
            .attr("width", width)
            .attr("height", height - 5)
            .attr("style", "margin-left:120px")
            .append("g")
            .attr("transform", "translate(" + width / 2 + "," + (height / 2 + 10) + ")");


    var partition = d3.layout.partition()
            .value(function(d) {
                return d.size;
            });

    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(partition.nodes(data)).enter()
            .append("svg:linearGradient")
            .attr("id", function(d) {
                return "gradient" + ((d.children ? d : d.parent).name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
            .attr("offset", "50%")
            .attr("stop-color", function(d, i) {
                return color((d.children ? d : d.parent).name);
            })
            .attr("stop-opacity", 1);

    var arc = d3.svg.arc()
            .startAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x)));
            })
            .endAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx)));
            })
            .innerRadius(function(d) {
                return Math.max(0, y(d.y));
            })
            .outerRadius(function(d) {
                return Math.max(0, y(d.y + d.dy));
            });

    var g = svg.selectAll("g")
            .data(partition.nodes(data))
            .enter().append("g");

    var path = g.append("path")
            .attr("d", arc)
            .style("fill", function(d) {
                return "url(#gradient" + ((d.children ? d : d.parent).name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .on("click", click)
            .on("mouseover", mouseover)
            .on("mouseout", function(d) {
                //        $("#breadcrum").empty();
                hide_details(d, i, this);
            });
    d3.select("#coff").on("mouseleave", function(d, i) {
        d3.selectAll("path")
                .transition()
                .duration(100)
                .style("opacity", 1)
                .each("end", function() {
                    d3.select(this).on("mouseover", mouseover);
                });
        //        $("#sequence").empty();

    });

    var text = g.append("text")
            .attr("transform", function(d) {
                return "rotate(" + computeTextRotation(d) + ")";
            })
            .attr("x", function(d) {
                return y(d.y);
            })
            .attr("dx", "6") // margin
            .attr("dy", ".35em") // vertical-align
            .text(function(d) {
                if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
                    if (d.length < 14)
                        return d.name;
                    else
                        return d.name.substring(0, 7) + "..";
                }
            });
    //    totalSize = path.node().__data__.value;
    function mouseover(d) {
        var percentage = (100 * d.value / totalSize).toPrecision(3);
        var percentageString = percentage + "%";
        if (percentage < 0.1) {
            percentageString = "< 0.1%";
        }
        $("#sequence").empty();
        d3.select("#percentage")
                .text(percentageString);

        d3.select("#explanation")
                .style("visibility", "");
        var sequenceArray = getAncestors(d);
        updateBreadcrumbs(sequenceArray, percentageString);

        // Fade all the segments.
        d3.selectAll("path")
                .style("opacity", 0.3);

        // Then highlight only those that are an ancestor of the current segment.
        svg.selectAll("path")
                .filter(function(node) {
                    return (sequenceArray.indexOf(node) >= 0);
                })
                .style("opacity", 1);
        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d.value, round, precition);
        }
        else {
            msrData = addCommas(d.value);
        }
        var content;
        content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
        content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
        return tooltip.showTooltip(content, d3.event);

    }
    function click(d) {
        // fade out all text elements
        text.transition().attr("opacity", 0);

        path.transition()
                .duration(750)
                .attrTween("d", arcTween(d))
                .each("end", function(e, i) {
                    // check if the animated element's data e lies within the visible angle span given in d
                    if (e.x >= d.x && e.x < (d.x + d.dx)) {
                        // get a selection of the associated text element
                        var arcText = d3.select(this.parentNode).select("text");
                        // fade in the text element and recalculate positions
                        arcText.transition().duration(750)
                                .attr("opacity", 1)
                                .attr("transform", function() {
                                    return "rotate(" + computeTextRotation(e) + ")";
                                })
                                .attr("x", function(d) {
                                    return y(d.y);
                                })
                                .text(function(d) {
                                    if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
                                        if (d.length < 14)
                                            return d.name;
                                        else
                                            return d.name.substring(0, 7) + "..";
                                    }
                                });
                    }
                });
    }

    d3.select(self.frameElement).style("height", height + "px");

    // Interpolate the scales!
    function arcTween(d) {
        var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
                yd = d3.interpolate(y.domain(), [d.y, 1]),
                yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
        return function(d, i) {
            return i
                    ? function(t) {
                        return arc(d);
                    }
            : function(t) {
                x.domain(xd(t));
                y.domain(yd(t)).range(yr(t));
                return arc(d);
            };
        };
    }

    function computeTextRotation(d) {
        return (x(d.x + d.dx / 2) - Math.PI / 2) / Math.PI * 180;
    }

    function getAncestors(node) {
        var path = [];
        var current = node;
        while (current.parent) {
            path.unshift(current);
            current = current.parent;
        }
        return path;
    }
    function initializeBreadcrumbTrail() {
        // Add the svg area.
        var trail = d3.select("#sequence").append("svg:svg")
                .attr("width", width)
                .attr("height", 50)
                .attr("id", "trail");
        // Add the label at the end, for the percentage.
        trail.append("svg:text")
                .attr("id", "endlabel")
                .style("fill", "#000");
    }
    function breadcrumbPoints(d, i) {
        var points = [];
        points.push("0,0");
        points.push(b.w + ",0");
        points.push(b.w + b.t + "," + (b.h / 2));
        points.push(b.w + "," + b.h);
        points.push("0," + b.h);
        if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
            points.push(b.t + "," + (b.h / 2));
        }
        return points.join(" ");
    }
    function updateBreadcrumbs(nodeArray, percentageString) {
        var g = d3.select("#trail")
                .selectAll("g")
                .data(nodeArray, function(d) {
                    return d.name + d.depth;
                });
        var svg1 = d3.select("#sequence").append("svg").attr("id", "breadcrum");
        for (var i = 0; i < nodeArray.length; i++) {
            var ind = (i * 90) + 30;
            if (i === 0) {
                ind = 40;
            }
            if (nodeArray[i].name.length > 7) {
                ind = (i * 90) + 40;
            }
            svg1.append("svg:polygon")
                    .attr("points", breadcrumbPoints)
                    .attr("transform", function(d) {
                        return "translate(" + i * 90 + ", 0)";
                    })
                    .style("fill", function(d) {
                        return "url(#gradient" + (nodeArray[i].name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                    });
            var text = svg1
                    .append("text")
                    .text(function(d) {
                        if (nodeArray[i].name.length < 9)
                            return  nodeArray[i].name;
                        else
                            return  nodeArray[i].name.substring(0, 8) + "..";
                    })
                    .attr("dx", ind)
                    .attr("dy", 20)
                    .attr("text-anchor", "middle")
                    .style("font-family", "sans-serif")
                    .style("stroke", "white")
                    .style("stroke-width", 0)
                    .style("font-size", "11px")
                    .style("fill", "white");
        }

    }
    totalSize = path.node().__data__.value;
}





function buildcollapsibleTree(json, columns, measureArray) {
    var localColorMap = {};
    var partition = d3.layout.partition()
            .value(function(d) {
                return d.size;
            });
    partition(json);
    var w = wid - 200,
            h = wid,
            i = 0,
            barHeight = 20,
            barWidth = w * .8,
            duration = 10,
            root;

    var tree = d3.layout.tree()
            .size([h, 100]);

    var diagonal = d3.svg.diagonal()
            .projection(function(d) {
                return [d.y, d.x];
            });

    colorMap = {};
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var vis = d3.select("body").append("svg:svg")
            .attr("width", w)
            .attr("height", h)
            .append("svg:g")
            .attr("transform", "translate(20,30)");
    json.x0 = 0;
    json.y0 = 0;
    function collapse(d) {
        if (d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }

    json.children.forEach(collapse);
    update(root = json);


    function update(source) {
        // Compute the flattened node list. TODO use d3.layout.hierarchy.
        var nodes = tree.nodes(root);

        // Compute the "layout".
        nodes.forEach(function(n, i) {
            n.x = i * barHeight;
        });

        var node = vis.selectAll("g.node")
                .data(nodes, function(d) {
                    return d.id || (d.id = i++);
                });

        var nodeEnter = node.enter().append("svg:g")
                .attr("class", "node")
                .attr("transform", function(d) {
                    return "translate(" + source.y0 + "," + source.x0 + ")";
                })
                .style("opacity", 1e-6);

        // Enter any new nodes at the parent's previous position.
        nodeEnter.append("svg:rect")
                .attr("y", -barHeight / 2)
                .attr("height", barHeight)
                .attr("width", barWidth)
                .style("fill", color)
                .on("click", click).on("mouseover", function(d, i) {
            var msrData;
            if (isFormatedMeasure) {
                msrData = numberFormat(d.value, round, precition);
            }
            else {
                msrData = addCommas(d.value);
            }
            var content;
            content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
        nodeEnter.append("svg:text")
                .attr("dy", 3.5)
                .attr("dx", 5.5)
                .text(function(d) {
                    return d.name;
                });

        // Transition nodes to their new position.
        nodeEnter.transition()
                .duration(duration)
                .attr("transform", function(d) {
                    return "translate(" + d.y + "," + d.x + ")";
                })
                .style("opacity", 1);

        node.transition()
                .duration(duration)
                .attr("transform", function(d) {
                    return "translate(" + d.y + "," + d.x + ")";
                })
                .style("opacity", 1)
                .select("rect")
                .style("fill", color);

        // Transition exiting nodes to the parent's new position.
        node.exit().transition()
                .duration(duration)
                .attr("transform", function(d) {
                    return "translate(" + source.y + "," + source.x + ")";
                })
                .style("opacity", 1e-6)
                .remove();

        var link = vis.selectAll("path.link")
                .data(tree.links(nodes), function(d) {
                    return d.target.id;
                });

        // Enter any new links at the parent's previous position.
        link.enter().insert("svg:path", "g")
                .attr("class", "link")
                .attr("d", function(d) {
                    var o = {
                        x: source.x0,
                        y: source.y0
                    };
                    return diagonal({
                        source: o,
                        target: o
                    });
                })
                .transition()
                .duration(duration)
                .attr("d", diagonal);

        // Transition links to their new position.
        link.transition()
                .duration(duration)
                .attr("d", diagonal);

        // Transition exiting nodes to the parent's new position.
        link.exit().transition()
                .duration(duration)
                .attr("d", function(d) {
                    var o = {
                        x: source.x,
                        y: source.y
                    };
                    return diagonal({
                        source: o,
                        target: o
                    });
                })
                .remove();

        // Stash the old positions for transition.
        nodes.forEach(function(d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }

    // Toggle children on click.
    function click(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        update(d);
    }

    function color(d) {
        return d._children ? "#3182bd" : d.children ? "#c6dbef" : "#fd8d3c";
    }

}

function buildzoomableWheel(root, columns, measureArray) {

    var div = d3.select("body").append("div")
            .attr("class", "tooltip")
            .style("opacity", 0);
    var width = wid - 500,
            height = hgt - 30,
            radius = Math.min(width, height - 50) / 2;

    var x = d3.scale.linear()
            .range([0, 2 * Math.PI]);

    var y = d3.scale.sqrt()
            .range([0, radius]);
    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height)
            .attr("style", "margin-left:120px")
            .append("g")
            .attr("transform", "translate(" + width / 2 + "," + (height / 2 + 10) + ")");

    var partition = d3.layout.partition()
            .value(function(d) {
                return d.size;
            });
    var sum = d3.sum(partition.nodes(root), function(d) {
        return d.size;
    });
    var arc = d3.svg.arc()
            .startAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x)));
            })
            .endAngle(function(d) {
                return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx)));
            })
            .innerRadius(function(d) {
                return Math.max(0, y(d.y));
            })
            .outerRadius(function(d) {
                return Math.max(0, y(d.y + d.dy));
            });
    var g = svg.selectAll("g")
            .data(partition.nodes(root))
            .enter().append("g");

    var path = g.append("path")
            .attr("d", arc)
            .style("fill", function(d) {
                return color((d.children ? d : d.parent).name);
            })
            .on("click", click)
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition);
                }
                else {
                    msrData = addCommas(d.value);
                }
                var content;
                content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    var text = g.append("text")
            .attr("transform", function(d) {
                return "rotate(" + computeTextRotation(d) + ")";
            })
            .attr("x", function(d) {
                return y(d.y);
            })
            .attr("dx", "6") // margin
            .attr("dy", ".35em") // vertical-align
            .text(function(d) {
//                if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
//                    var percentage = (d.value / sum) * 100;
//                    return percentage.toFixed(1) + "%";
//                }
        return d;
            });

    function click(d) {
        text.transition().attr("opacity", 0);

        path.transition()
                .duration(750)
                .attrTween("d", arcTween(d))
                .each("end", function(e, i) {
                    if (e.x >= d.x && e.x < (d.x + d.dx)) {
                        var arcText = d3.select(this.parentNode).select("text");
                        var sum1 = d3.sum(partition.nodes(root), function(d) {
                            return d.size;
                        });
                        arcText.transition().duration(750)
                                .attr("opacity", 1)
                                .attr("transform", function() {
                                    return "rotate(" + computeTextRotation(e) + ")";
                                })
                                .attr("x", function(d) {
                                    return y(d.y);
                                })
                                //.text(function(d) {if((Math.PI, x(d.x + d.dx))-(Math.PI, x(d.x))> 0.07){if (d.length < 14)return d.name;else return d.name.substring(0, 7) + "..";}})
                                .text(function(d) {
                                    if ((Math.PI, x(d.x + d.dx)) - (Math.PI, x(d.x)) > 0.07) {
                                        var percentage = (d.value / sum1) * 100;
                                        return percentage.toFixed(1) + "%";
                                    }
                                });
                    }
                });
    }
    d3.select(self.frameElement).style("hgt", hgt + "px");

    // Interpolate the scales!
    function arcTween(d) {
        var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
                yd = d3.interpolate(y.domain(), [d.y, 1]),
                yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
        return function(d, i) {
            return i
                    ? function(t) {
                        return arc(d);
                    }
            : function(t) {
                x.domain(xd(t));
                y.domain(yd(t)).range(yr(t));
                return arc(d);
            };
        };
    }
    function computeTextRotation(d) {
        return (x(d.x + d.dx / 2) - Math.PI / 2) / Math.PI * 180;
    }
}


function buildXaxisFilter(div, d,i) {
   // alert("buildXaxisFilter=="+d)
    var chartData = JSON.parse(parent.$("#chartData").val());
	var XLable='';
     var k=1;
     if(typeof chartData[div]["XaxisRange"]!="undefined" && chartData[div]["XaxisRange"]!=""){
          k= parseInt(chartData[div]["XaxisRange"]);
          k++;
     }
     if(typeof d!="undefined"){
                if ( i%k==0){
           if(d.length < 13){
                   if (typeof chartData[div]["editXLable"] !== "undefined" && chartData[div]["editXLable"] !== "") {

      return d.substring(0, chartData[div]["editXLable"]);
    }else{
      return d.substring(0, 12);
 }
            }else if (typeof chartData[div]["editXLable"] !== "undefined" && chartData[div]["editXLable"] !== "") {

      return d.substring(0, chartData[div]["editXLable"]);
    }else{
     return d.substring(0, 12) + "..";}
    }else
       return ""; }
            }



function buildArea(div, data, columns, measureArray, divWid, divHgt) {
    var color = d3.scale.category10();
     var widthdr=divWid
    var divHgtdr=divHgt
      var div1=parent.$("#chartname").val()
      var dashletid;
  var fromoneview=parent.$("#fromoneview").val();
     if(fromoneview!='null'&& fromoneview=='true'){
divWid=divWid* .8;
dashletid=div;
div=div1;
     }else{
//   divWid=parseFloat($(window).width())*(.35);
     }
    var measure1 = measureArray[0];
    var chartMap = {};
    var minVal = minimumValue(data, measure1);
    var autoRounding1;
    var measArr = [];
    var chartData = JSON.parse(parent.$("#chartData").val());
    var customTicks = 5;
if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);

}else{
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var prop = graphProp(div);
}
   if(fromoneview!='null'&& fromoneview=='true'){

      }else{

    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
      }
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";
    var botom = 150;
    var j = 0;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
        botom = 75;
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
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
    }
    var margin = {
        top: 5,
        right: 00,
        bottom: botom,
        left: 72
    },
    width = divWid, //- margin.left - margin.right
            height = divHgt * .90;

    var x = d3.scale.ordinal().rangePoints([0, width], .5);
    var y = d3.scale.linear().rangeRound([height, 0]);

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
//    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .ticks(customTicks)
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });

//    } else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .ticks(customTicks)
                .tickFormat(function(d, i) {
                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                   }
                   }else {
                       return "";
                   }
                });
 var yAxis1 = d3.svg.axis1()
            .scale(y)
            .ticks(customTicks)
            .tickFormat(function(d, i) {
                measArr.push(d);

                return "";
            });

    var valueline = d3.svg.area().interpolate("monotone")
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y0(height)
            .y1(function(d) {
                return y(d[measure1]);
            });

if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
 var svg = d3.select("#" + div).append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid + margin.left+15)+" "+(divHgt+35)+" ")
//            .attr("width", divWid + margin.left+15)
//            .attr("height", divHgt - 15)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");


    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
    svg.append("g")
            .attr("class", "y axis");
//            .append("text")
//            .attr("transform", "rotate(-90)")
//            .attr("y", 0)
//            .attr("dy", ".71em")
//            .style("text-anchor", "end")
//            .text("" + measure1 + "");

    var min1 = [];
    for (var key in data) {
        min1.push(data[key][measure1]);

    }
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));

   var max = 0;
   if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
//   if(fromoneview!='null'&& fromoneview=='true'){
//  max = maximumValue(data, measure1);
//  if (data.length > 1) {
//        minVal = minimumValue(data, measure1) * .8;
//    }
//    var yMin = 0;
//    yMin = minVal;
//    y.domain([yMin, max]);
//    svg.call(yAxis1);
//    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
//    if(measArr[0]<0){
//    minVal = measArr[0] + diffFactor;
//    }
//    else{
//       minVal = measArr[0] + diffFactor;
//       if(measArr[0]>=0 && minVal<0){
//           minVal=0;
//       }
//    }
//      }else{
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
    max = maximumValue(data, measure1);
}}else{
    max = maximumValue(data, measure1);
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {
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
    }
//}

    y.domain([parseFloat(minVal), parseFloat(max)]);

if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}

    svg = d3.select("#" + div)
    .select("g");

     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .style('text-anchor','end')
            .style('dy','.32em')
            .style('y',0)
            .style('x',-9)
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            })
     }
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
             .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
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
            return -10;
        }else {
            return 9;
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
            .text(function(d,i) {
      if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i);
      }
            })
            .append("svg:title").text(function(d) {
        return d;
    });

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

    svg.append("path")
            .data(data)
            .attr("d", valueline(data))
//            .attr("fill", color(0))
            .attr("fill", function(d,i) {
                 var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }

			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color(0))
                return colorfill;
           })
            .style("stroke-width", "3px")

            .style("stroke",  function(d,i) {
                 var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }

			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color(0))
                return colorfill;
           });

    var blueCircles = svg.selectAll("dot")
            .data(data)
            .enter().append("circle")
            .attr("r", 4)
            .attr("cx", function(d) {
                return x(d[columns[0]]);
            })
            .attr("cy", function(d) {
                return y(d[measure1]);
            })
            .style("fill", "white")
            .style("stroke",  function(d,i) {
           var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                        }

			var colorfill = color(0);
                return colorfill;
            })
            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .attr("onclick", fun)
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
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }

}

function buildMultiMeasureArea(div, data, columns, measureArray, divWid, divHgt) {
    var measure1 = measureArray[0];
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

      var widthdr=divWid
    var divHgtdr=divHgt
    var dashletid;
    var div1=parent.$("#chartname").val()
     var fromoneview=parent.$("#fromoneview").val();
      if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
  divWid=parseInt($(window).width())*(.35);
      }else{
//     divWid=parseInt($(window).width())*(.35);
      }
     var measArr = [];
      var max = maximumValue(data, measure1);
      var minVal = minimumValue(data, measure1);
    var autoRounding1;
     var chartData = JSON.parse(parent.$("#chartData").val());
    var customTicks = 5;
if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);

}else{
if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="" && chartData[div]["yaxisrange"]["axisTicks"]!="undefined" && chartData[div]["yaxisrange"]["axisTicks"]!="" && (typeof parent.$("#drills").val()=="undefined" || parent.$("#drills").val()=="" )) {
     customTicks = chartData[div]["yaxisrange"]["axisTicks"];
 }
    var prop = graphProp(div);
}

  if(fromoneview!='null'&& fromoneview=='true'){

     }else{
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
     }

    var isDashboard = parent.$("#isDashboard").val();
//    var fun = "drillWithinchart(this.id,\""+div+"\")";


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
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
    }
    var botom = 150;
    var j = 0;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
        botom = 75;
    }
    var margin = {
        top: 5,
        right: 00,
        bottom: divHgt*.02,
        left: 72
    },
    width = divWid, //- margin.left - margin.right
            height = divHgt * .90;
    var x = d3.scale.ordinal().rangePoints([0, width], .5);
    var y = d3.scale.linear().rangeRound([height, 0]);
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
//    if (isFormatedMeasure) {
       var yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d) {
                    if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
                }else {
                    return "";
                }
                });

//    } else {
      var  yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                 .ticks(customTicks)
                .tickFormat(function(d, i) {
                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                   }
                   }else {
                       return "";
                   }
                });
//    }

//    var yAxis1 = d3.svg.axis1()
//            .scale(y)
//            .tickFormat(function(d, i) {
//                measArr.push(d);
//
//                return "";
//            });
// line 1
    var ageNames = d3.keys(data[0]).filter(function(key, i) {
        return key !== columns[i];
    });
    var valueline = d3.svg.area().interpolate("monotone")
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y0(height)
            .y1(function(d) {
                return y(d[measureArray[j]]);
            });
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}

    svg = d3.select("#" + div).append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWid + margin.left+15)+" "+(divHgt+30)+" ")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}

    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
         svg.append("g")
            .attr("class", "y axis");


    var min1 = [];
    for (var key in data) {
        for (var meas in measureArray) {
            min1.push(data[key][measureArray[meas]]);
        }
    }
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
var max1 = [];
 var max = 0;
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {

    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{

   for (var key in data) {
        for (var meas in measureArray) {
            max1.push(data[key][measureArray[meas]]);
        }
    }
}}else{

   for (var key in data) {
        for (var meas in measureArray) {
           max1.push(data[key][measureArray[meas]]);
        }
    }
}
 if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
 if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default") {

  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
 }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""){
   minVal = 0;
 }else{

    for (var key in data) {
        for (var meas in measureArray) {
            max1.push(data[key][measureArray[meas]]);
        }
    }}
}else{

    for (var key in data) {
        for (var meas in measureArray) {
            max1.push(data[key][measureArray[meas]]);
        }
    }
    }
//}
    if(max1.length>1){
         y.domain([0, Math.max.apply(Math, max1)]);
    }else{
    y.domain([parseFloat(minVal), parseFloat(max)]);
}
//    y.domain([0, Math.max.apply(Math, min1)]);
//      svg.call(yAxis1);
//    var diffFactor = parseFloat(measArr[0] - parseFloat(measArr[1]));
//    minVal = measArr[0] + diffFactor;
//    y.domain([minVal, max]);
if(fromoneview!='null'&& fromoneview=='true'){
 div=dashletid;
}
    svg = d3.select("#" + div)
    .select("g");

 if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){}else{
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
            .style('text-anchor','end')
            .style('dy','.32em')
            .style('y',0)
            .style('x',-9)
            .style('font-size',function(d,i) {
                return font2(div,d,i);
            });
 }

    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  0;
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 0;
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
            return 30;
            }
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return 10;
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return -10;
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
            .text(function(d,i) {
   if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{
                return buildXaxisFilter(div,d,i);
   }
            })
            .append("svg:title").text(function(d) {
        return d;
    });
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
    if(fromoneview!='null'&& fromoneview=='true'){
 div=div1;
}
    var colorArr = [];
    for (var i = 0; i < measureArray.length; i++) {
        j = i;
        svg.append("path")
                .data(data)
                .attr("d", valueline(data))
                .attr("fill", getDrawColor(div, i)).style("stroke-width", "3px")
                .style("stroke", getDrawColor(div, i));
        colorArr.push(getDrawColor(div, i));

        var blueCircles = svg.selectAll("dot")
                .data(data)
                .enter().append("circle")
                .attr("r", 4)
                .attr("cx", function(d) {
                    return x(d[columns[0]]);
                })
                .attr("cy", function(d) {
                    return y(d[measureArray[i]]);
                })
                .style("fill", "white")
                .style("stroke", getDrawColor(div, i))
                .style("stroke-width", "2px")
                .attr("opacity", 1)
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]];
                })
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
//                    show_details(d, columns, measureArray, this,div);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    }

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
    }
    else {
//        buildCircledrill(height);
    }
    var ageNames1 = [];
    for (var i = 0; i < ageNames.length; i++) {
        ageNames1.push(ageNames[i]);
    }
    showLegends2(ageNames1, colorArr, width, svg);
}

function buildOverAllBubble(data, columns, measureArray) {
    var fun = "drillWithinchart(this.id)";
    var isDashboard = parent.$("#isDashboard").val();
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var max_amount;
    var wid = $(window).width();
    var hgt = $(window).height();

    var margin = {
        top: 0,
        right: 0,
        bottom: 10,
        left: 0
    },
    width = wid,
            height = hgt;
    var center = {
        x: width / 2,
        y: height / 2
    }, damper = 0.1;
    max_amount = d3.max(data, function(d) {
        return parseFloat(d[measureArray[0]]);
    });
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var ran = ((sum / data.length) * 100) / max_amount;
    if (ran < 20) {
        ran = ran + 20;
    }
    this.radius = d3.scale.pow().exponent(0.1).domain([0, max_amount]).range([2, ran]);

    var padding = 12;
    var nodes = [];
    // nodes & svg's
    data.forEach(function(d) {
        var node;
        node = {
            data: d,
            radius: radius(d[measureArray[0]]),
            cx: Math.random() * 900,
            cy: Math.random() * 800
        };
        return nodes.push(node);
    });

    function svg() {
        this.style("left", function(d) {
            return d.x + "px";
        });
    }

    // defining the position
    function position() {
        this.style("left", function(d) {
            return d.x + "px";
        })

                .style("width", function(d) {
                    return Math.max(0, d.dx - 1) + "px";
                })
                .style("height", function(d) {
                    return Math.max(0, d.dy - 1) + "px";
                });
    }

    var force = d3.layout.force()
            .nodes(nodes)
            .size([width, height])
            .gravity(-0.01)
            .charge(function(d) {
                return -Math.pow(d.radius, 2.0) / 8;
            }
            )
            .friction(0.9)
            .on("tick", function(e) {
                return circle.each(move_towards_center(e.alpha)).attr("cx", function(d) {
                    return d.x;
                }).attr("cy", function(d) {
                    return d.y;
                });
            })
            .start(function() {
                return this.force = d3.layout.force().nodes(this.nodes).size([this.width, this.height]);
            });

    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("style", "margin-top:30px;")
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var circle = svg.selectAll("circle")
            .data(nodes)
            .enter().append("circle")
            .attr("id", function(d) {
                return d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            });
    circle.transition().duration(2000).attr("r", function(d) {
        return d.radius;
    });
    circle = svg.selectAll("circle")
            .style("fill", function(d) {
                if (d.data[columns[0]] === "negative") {
                    return "#d84b2a";
                } else if (d.data[columns[0]] === "neutral") {
                    return "#D1D0CE";
                } else
                    return "rgb(116, 196, 118)";
            })
            //.attr("stroke-width", 3).attr("stroke", "grey")
            .call(force.drag)
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {

                d3.selectAll("#" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", drillShade);
                // d3.selectAll("#"+d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("stroke", "grey");
                // d3.selectAll("#"+d.data[columns[0]].replace(" ","","gi")).style("stroke-width", 3);
                //d3.select(this).attr("stroke", "grey");
                var content;
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.data[measureArray[0]], round, precition);
                }
                else {
                    msrData = addCommas(d.data[measureArray[0]]);
                }
                content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.data[columns[0]] + "</span><br/>";
                content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d) {
                d3.selectAll("#" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", function(d) {
                    if (d.data[columns[0]] === "negative") {
                        return "#d84b2a";
                    } else if (d.data[columns[0]] === "neutral") {
                        return "#D1D0CE";
                    } else
                        return "rgb(116, 196, 118)";
                });
                d3.selectAll("#" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("stroke", "grey");
                hide_details(d, i, this);
            });
    circle.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "8pt")
            .attr("fill", "Black")
            .text(function(d) {
                return d.data[columns[0]].substring(0, d.r / 5);
            });
    function move_towards_center(alpha) {
        return function(d) {
            d.x = d.x + (center.x - d.x) * (damper + 0.02) * alpha;
            return d.y = d.y + (center.y - d.y) * (damper + 0.02) * alpha;
        };
    }
    ;
    function tick(e) {
        circle
                .each(gravity(.4 * e.alpha))
                .each(collide(0.002))
                .attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                });
    }

    // Move nodes toward cluster focus.
    function gravity(alpha) {
        return function(d) {
            d.y += (d.cy - d.y) * alpha;
            d.x += (d.cx - d.x) * alpha;
        };
    }

    // Resolve collisions between nodes.
    function collide(alpha) {
        var quadtree = d3.geom.quadtree(nodes);
        return function(d) {
            var r = d.radius + radius.domain()[1] + padding,
                    nx1 = d.x - r,
                    nx2 = d.x + r,
                    ny1 = d.y - r,
                    ny2 = d.y + r;
            quadtree.visit(function(quad, x1, y1, x2, y2) {
                if (quad.point && (quad.point !== d)) {
                    var x = d.x - quad.point.x,
                            y = d.y - quad.point.y,
                            l = Math.sqrt(x * x + y * y),
                            r = d.radius + quad.point.radius + (d.color !== quad.point.color) * padding;
                    if (l < r) {
                        l = (l - r) / l * alpha;
                        d.x -= x *= l;
                        d.y -= y *= l;
                        quad.point.x += x;
                        quad.point.y += y;
                    }
                }
                return x1 > nx2
                        || x2 < nx1
                        || y1 > ny2
                        || y2 < ny1;
            });
        };
    }
}



function buildDotImage(data, columns, measureArray) {
    var measure1 = measureArray[0];
    var autoRounding1;
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
    var wid = $(window).width() - 200;
    var hgt = $(window).height();
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var margin = {
        top: 40,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - 50 - margin.left - margin.right,
            height = hgt - margin.top - margin.bottom;
    var barPadding = 4;
    var formatPercent = d3.format(".0%");

    x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);

    y = d3.scale.linear()
            .range([height, 0]);

    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    //    yAxis = d3.svg.axis()
    //    .scale(y)
    //    .orient("left")
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
                .tickFormat(function(d, i) {
                    return autoFormating(d, autoRounding1);
                    //            return d;
                });
    }
    //    .tickFormat(formatPercent);
    svg = d3.select("body").append("svg")
            .attr("id", "svg_" + div)
            .attr("width", width + margin.left + margin.right + 30)
            .attr("height", height - 50 + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientViewBy")
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
            .attr("stop-color", "#79BEDB")
            .attr("stop-opacity", 1);

    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientAvg")
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
            .attr("stop-color", "#C0C0C0")
            .attr("stop-opacity", 1);

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var max = maximumValue(data, measure1);
    var min = minimumValue(data, measure1);
    if (min === max) {
        min = min / 2;
        max = max + min / 2;
    }
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    y.domain([min, max]);
    svg.append("g")
            .attr("class", "x axis")
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



    //    svg.selectAll("circle1")
    //    .data(data)
    //    .enter()
    //    .append("circle")
    //    .attr("cx", function(d) {
    //        return x(d[columns[0]]) + 45;
    //    })
    //    .attr("cy", function(d) {
    //        return y(d[measure1]);
    //    })
    //    .attr("r", 30)
    //    .attr("fill", "url(#gradientViewBy)")
    //    .attr("id", function(d) {
    //        return d[columns[0]]
    //    })
    //    .attr("onclick", fun)
    //    .on("mouseover", function(d, i) {
    //        show_details(d, columns, measureArray, this)
    //    })
    //    .on("mouseout", function(d, i) {
    //        hide_details(d, i, this);
    //    });




    svg.selectAll("circle1")
            .data(data)
            .enter().append("image")
            //            .attr('x',-9)
            //            .attr('y',-12)
            .attr("x", function(d) {
                return x(d[columns[0]]);
            })
            .attr("y", function(d) {
                return y(d[measure1]) - 40;
            })
            .attr('width', 40)
            .attr('height', 40)
            .attr('index_value', i)
            .attr("xlink:href", function(d) {
                //                if(i%2===0)
                //            return "logos/bear.jpg";
                //        else if(i%3===0)
                //            return "logos/jr.jpg";
                //        else
                return "logos/" + "Absolut1" + ".jpg";
            }).attr("alt", function(d) {
        return d[columns[0]];
    })
            .attr("id", function(d) {
                return d[columns[0]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });

    var len = data.length;
    var sum = d3.sum(data, function(d) {
        return d[measure1];
    });
    var Avg = sum / len;

    svg.append("circle")
            .attr("cx", (width + margin.left + margin.right) / 2)
            .attr("cy", y(Avg))
            .attr("r", 30)
            .attr("fill", "url(#gradientAvg)")
            .attr("opacity", .7)
            .on("mouseover", function(d, i) {
                var content;
                content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[columns[0]] + "</span><br/>";
                content = "<span class=\"name\"> AVG:</span><span class=\"value\"> " + Math.round(Avg) + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });


    svg.append("text").attr("class", "text1")
            .text(function() {
                if (isFormatedMeasure) {
                    return numberFormat(Avg, round, precition);
                }
                else {
                    return autoFormating(Avg, autoRounding1);
                }
                Math.round(Avg);
            })
            .attr("x", (width + margin.left + margin.right) / 2)
            .attr("y", y(Avg))
            .attr("font-size", "7px")
            .attr("fill", "green")
            .attr("text-anchor", "middle");



    svg.selectAll(".text")
            .data(data)
            .enter()
            .append("text").attr("class", "text")
            .text(function(d) {
                if (isFormatedMeasure) {
                    return numberFormat(d[measure1], round, precition);
                }
                else {
                    return autoFormating(d[measure1], autoRounding1);
                }
                //        return Math.round(d[measureArray[0]]);
            })
            //.text(function(d) {return d[measureArray[0]] + "," + d[1];})
            .attr("x", function(d) {
                return x(d[columns[0]]) + 20;
            })
            .attr("y", function(d) {
                return y(d[measure1]) + 10;
            })
            //.attr("font-family", "sans-serif")
            .attr("font-size", "8px")
            .attr("fill", "black")
            .on("mouseover", function(d, i) {
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });


}

function buildcircle(data, columns, measureArray) {
    var measure1 = measureArray[0];
    var autoRounding1;
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
    var wid = $(window).width() - 200;
    var hgt = $(window).height();
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var margin = {
        top: 40,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - 50 - margin.left - margin.right,
            height = hgt - margin.top - margin.bottom;
    var barPadding = 4;
    var formatPercent = d3.format(".0%");

    x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);

    y = d3.scale.linear()
            .range([height, 0]);

    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");

    //    yAxis = d3.svg.axis()
    //    .scale(y)
    //    .orient("left")
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
                .tickFormat(function(d, i) {
                    return autoFormating(d, autoRounding1);
                    //            return d;
                });
    }
    //    .tickFormat(formatPercent);
    svg = d3.select("body").append("svg")
            .attr("width", width + margin.left + margin.right + 30)
            .attr("height", height - 50 + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    var stopColor = "#79BEDB";
    var avgStopColor = "#C0C0C0";
    var chartMap = {};
    colorMap = {};
    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientViewBy")
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
            .attr("stop-color", stopColor)
            .attr("stop-opacity", 1);

    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientAvg")
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
            .attr("stop-color", avgStopColor)
            .attr("stop-opacity", 1);

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var max = maximumValue(data, measure1);
    var min = minimumValue(data, measure1);
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    y.domain([min, max]);
    svg.append("g")
            .attr("class", "x axis")
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



    svg.selectAll("circle1")
            .data(data)
            .enter()
            .append("circle")
            .attr("cx", function(d) {
                return x(d[columns[0]]) + 45;
            })
            .attr("cy", function(d) {
                return y(d[measure1]);
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("r", 30)
            .attr("fill", "url(#gradientViewBy)")
            .attr("id", function(d, i) {
                if (typeof chartMap[d[columns[0]]] === "undefined") {
                    colorMap[i] = d[columns[0]] + "__" + stopColor;
                    chartMap[d[columns[0]]] = stopColor;
                }

                return d[columns[0]];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                show_details(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    var len = data.length;
    var sum = d3.sum(data, function(d) {
        return d[measure1];
    });
    var Avg = sum / len;

    svg.append("circle")
            .attr("cx", (width + margin.left + margin.right) / 2)
            .attr("cy", y(Avg))
            .attr("r", 30)
            .attr("id", function() {
                if (typeof chartMap["Average"] === "undefined") {
                    colorMap[i] = "Average__" + avgStopColor;
                    chartMap["Average"] = avgStopColor;
                }
                return "Average";
            })
            .attr("fill", "url(#gradientAvg)")
            .on("mouseover", function(d, i) {
                var content;
                content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[columns[0]] + "</span><br/>";
                content = "<span class=\"name\"> AVG:</span><span class=\"value\"> " + Math.round(Avg) + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    svg.append("text").attr("class", "text1")
            .text(function() {
                if (isFormatedMeasure) {
                    return numberFormat(Avg, round, precition);
                }
                else {
                    return autoFormating(Avg, autoRounding1);
                }
                Math.round(Avg);
            })
            .attr("x", (width + margin.left + margin.right) / 2)
            .attr("y", y(Avg))
            .attr("font-size", "7px")
            .attr("fill", "green")
            .attr("text-anchor", "middle");
    svg.selectAll(".text")
            .data(data)
            .enter()
            .append("text").attr("class", "text")
            .text(function(d) {
                if (isFormatedMeasure) {
                    return numberFormat(d[measure1], round, precition);
                }
                else {
                    return autoFormating(d[measure1], autoRounding1);
                }
                //        return Math.round(d[measureArray[0]]);
            })
            //.text(function(d) {return d[measureArray[0]] + "," + d[1];})
            .attr("x", function(d) {
                return x(d[columns[0]]) + 45;
            })
            .attr("y", function(d) {
                return y(d[measure1]);
            })
            //.attr("font-family", "sans-serif")
            .attr("font-size", "8px")
            .attr("fill", "black");


}

function buildsplit(div,dataChart, columns, measureArray,height) {
    $("#viewMeasureBlock").css('display','none');
    var data12=dataChart["chart1"];

    var BubbleChart, root,
            __bind = function(fn, me) {
                return function() {
                    return fn.apply(me, arguments);
                };
            };



    $("#legendchart").show();

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
            this.width = $(window).width() - 150;
            this.height = $(window).height()-50;


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
            var radScale = 50;
            this.radius_scale = d3.scale.pow().exponent(0.5).domain([0, max_amount]).range([2, radScale]);
            this.create_nodes();
            this.create_vis();
        }
        var legends = [];
        BubbleChart.prototype.create_nodes = function() {
            var _this = this;
            this.data.forEach(function(d, i) {
                if (i < 500) {
//                    if (legends.indexOf(d[columns[0]]) === -1)
//                        legends.push(d[columns[0]]);
                    var node;
                    node = {
                        id: d[measureArray[0]],
                        radius: _this.radius_scale(parseFloat(d[measureArray[0]])),
                        value: d[measureArray[0]],
                        measure: measureArray[0].replace("_", "-", "gi"),
                        name: d[columns[0]],
                        group: d[columns[0]],
                        year: d[columns[1]],
                        x: Math.random() * 700,
                        y: Math.random() * 800
                    };
                    return _this.nodes.push(node);
                }
            });
            return this.nodes.sort(function(a, b) {
                return b.value - a.value;
            });
        };
        data12.forEach(function(d, i) {
            if (legends.indexOf(d[columns[0]]) === -1)
                        legends.push(d[columns[0]]);
        });

        BubbleChart.prototype.create_vis = function() {
            var that,
                    _this = this;
            this.vis = d3.select("#"+div)
            .append("svg")
            .attr("width", this.width*.82)
            .attr("height", this.height)
            .style("float", "left")
            .style("margin-left","-150px")// "-50px")
            .attr("id", "svg_vis");

            var gradient = this.vis.append("svg:defs").selectAll("radialGradient").data(this.data).enter()
                    .append("svg:radialGradient")
                    //    .attr("id", function(d) {return color(d.name);)
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
                        } else {
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
//                return _this.fill_color(d[columns[0]]);
                    })
                    .attr("stop-opacity", 1);
            parent.$("#legends").val(JSON.stringify(chartMap));
            var gradient = this.vis.append("svg:defs").selectAll("radialGradient").data(this.data).enter()
                    .append("svg:radialGradient")
                    //    .attr("id", function(d) {return color(d.name);)
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
//            var node = this.vis.enter().append("g")
            this.circles = this.vis.selectAll("circle").data(this.nodes
//            , function(d) {
//                return d.id;
//            }
                    );//.append("g")
            that = this;

            this.circles.enter()
            .append("circle")
            .attr("fill", function(d) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("stroke-width", 2)
            .attr("stroke", function(d) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("id", function(d) {
                return d.name+ ":" + d.id + "_" + d.measure + "_" + d.year;
            })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
                    .attr("index_value", function(d, i) {
                        return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
                    .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.year.replace(/[^a-zA-Z0-9]/g, '', 'gi')+"chart1";
                    })
                    .on("mouseover", function(d, i) {
                d3.select(this)
                .attr("stroke", "#FF0000");
                        that.show_details(d, i, this);
                    })
                    .on("mouseout", function(d, i) {
                d3.select(this)
//                .attr("stroke", function(d) {
//                    return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//                });
              .attr("stroke", " ");
                        that.hide_details(d, i, this);
                    });
//            var svg1 = d3.select("#legendchart").append("svg")
//                    .attr("width", "100%")
//                    .attr("height", "100%");
//
////            node.append("text")
////            .attr("text-anchor", "middle")
////            .attr("dy", ".3em")
////            .attr("font-family", "Verdana")
////            .attr("font-size", "8pt")
////            .attr("fill", labelColor)
////            .text(function(d) {
////            return d.year.substring(0, d.radius / 5);
////            });
//            var legend = svg1.selectAll(".legend");
////                    .data(legends)
////                    .enter().append("g")
////                    .attr("class", "legend")
////                    .attr("transform", function(d, i) {
////                        var j = i;
////                        return "translate(10," + j * 20 + ")";
////
////                    });
//            legend.append("circle")
//                    .attr("width", 12)
//                    .attr("cy", 9)
//                    .style("fill", function(d, i) {
//                        var colorShad;
//                        if (typeof centralColorMap[d.toString().toLowerCase()] !== "undefined") {
//                            colorShad = centralColorMap[d.toString().toLowerCase()];
//                        } else {
//                            colorShad = _this.fill_color(i);
//                        }
//                        colorShad = legendColorMap[d.toString()];
//                        return colorShad;
//                    }).attr("index_value", function(d, i) {
//                return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//
//                    .attr("class", function(d, i) {
//                        return "bars-Bubble-legendBullet-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                    })
//                    .attr("r", 5)
//                    .attr("color_value", function(d, i) {
//                        var colorShad;
////                if(typeof centralColorMap[d.toString().toLowerCase()]!=="undefined"){
////                colorShad=centralColorMap[d.toString().toLowerCase()];
////              }else{
////                colorShad=_this.fill_color(i);
////              }
//                        colorShad = legendColorMap[d.toString()];
//                        return colorShad;
//                    }) // Bar fill color...
//                    .on('mouseover', synchronizedMouseOver)
//                    .on("mouseout", synchronizedMouseOut);
//
//            legend.append("text")
//                    .attr("x", 20)
//                    .attr("y", 9)
//                    .attr("dy", ".35em")
//                    .text(function(d) {
//                        return d;
//                    })
//                    .style("fill", function(d) {
//                        var colorShad;
////                if(typeof cent    ralColorMap[d.toString().toLowerCase()]!=="undefined"){
////                colorShad=centralColorMap[d.toString().toLowerCase()];
////              }else{
////                colorShad=_this.fill_color(i);
////              }
//                        colorShad = legendColorMap[d.toString()];
//                        if (typeof chartMap[d[columns[0]]] === "undefined") {
//                            chartMap[d[columns[0]]] = colorShad;
//                            colorMap[i] = d[columns[0]] + "__" + colorShad;
//                        }
//                        return colorShad;
//                    })
//                    .attr("index_value", function(d, i) {
//                        return "index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                    })
//
//                    .attr("class", function(d, i) {
//                        return "bars-Bubble-legendBullet-index-" + d.replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                    })
//                    .attr("r", 5)
//                    .attr("color_value", function(d, i) {
//                        var colorShad;
////                if(typeof centralColorMap[d.toString().toLowerCase()]!=="undefined"){
////                colorShad=centralColorMap[d.toString().toLowerCase()];
////              }else{
////                colorShad=_this.fill_color(i);
////              }
//                        colorShad = legendColorMap[d.toString()];
//                        return colorShad;
//                    })
//                    .on('mouseover', synchronizedMouseOverSplit)
//                    .on("mouseout", synchronizedMouseOut);


            return this.circles.transition().duration(2000)
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
            .size([this.width, this.height]);
        };

        BubbleChart.prototype.display_group_all = function() {
            var _this = this;
            this.force.gravity(this.layout_gravity).charge(this.charge).friction(0.9).on("tick", function(e) {
                return _this.circles.each(_this.move_towards_center(e.alpha))
                .attr("cx", function(d) {
                    //                    return d.x = Math.max(d.radius, Math.min(_this.width*.4 - d.radius, d.x));
                    return d.x;
                })
                        .attr("cy", function(d) {
                    //                            return d.y = Math.max(d.radius, Math.min(_this.height - d.radius, d.y));
                    return d.y ;
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
                    //                    return d.x = Math.max(d.radius, Math.min(_this.width*.4 - d.radius, d.x));
                    return d.x ;
                })
                .attr("cy", function(d) {
                    //                            return d.y = Math.max(d.radius, Math.min(_this.height - d.radius, d.y));
                    return d.y ;
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
            d3.select(element).attr("stroke", "black");
            content = "<span class=\"name\">" + columns[0] + "</span><span class=\"value\"> " + data.name + "</span><br/>";
            content += "<span class=\"name\">" + columns[1] + "</span><span class=\"value\"> " + data.year + "</span><br/>";
            content += "<span class=\"name\">" + measureArray[0] + "</span><span class=\"value\"> " + (addCommas(data.value)) + "</span><br/>";
            return tooltip.showTooltip(content, d3.event);
        };

        BubbleChart.prototype.hide_details = function(data, i, element) {
//            var _this = this;
//            d3.select(element).attr("stroke", function(d) {
//                return d3.rgb(_this.fill_color(d.group)).darker();
//            });
            return tooltip.hideTooltip();
        };

        return BubbleChart;

    })();

    root = typeof exports !== "undefined" && exports !== null ? exports : this;
        var chart, render_vis,
                _this = this;
        chart = null;
        render_vis = function(csv) {
            chart = new BubbleChart(data12);
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
            }
            else {
                return root.display_all();
            }
        };
        render_vis();
//    alert(JSON.stringify(dataChart))
    var colName=columns[1];
    var html='';
//    alert("html")
    html +="<div class='hiddenscrollbar' style=''>";
    html +="<div class='innerhiddenscrollbar' style='white-space:nowrap;width:150px;height:400px;float:left;margin-left:80px;margin-top:15px;overflow-y:auto'>";//float: right;margin-left: 120px;margin-top: 40px;overflow-y: scroll;
    html +="<table style='float:left;height=400px'>";
    html +="<tr style='height:25px'><td style='height:25px'><span style='white-space:nowrap;color:black;font-size:12px;margin-left:20px;text-decoration:none' >"+columns[1]+"</span></td></tr>";
    var colMap={};

    for(var m=0;m<dataChart["chart1"].length;m++){
        colMap[dataChart["chart1"][m][columns[1]]]=dataChart["chart1"][m][columns[1]];
}
    for(var i=0;i<Object.keys(colMap).length;i++){
        html +="<tr style='height:25px'><td style='style='height:25px''>";
        if(Object.keys(colMap)[i].length > 11){
            html +="<canvas  width='7' height='7' style='margin-left:5px;margin-right:5px;background:" + color(i) + "'></canvas>";
            html +="<span  id='"+Object.keys(colMap)[i]+"'  onmouseover='setMouseOverEvent(this.id,\""+"chart1"+"\")' onmouseout='setMouseOutEvent(this.id,\""+"chart1"+"\")' style='margin-left:5px;font-size: 9px;color:"+color(i)+"'> "+Object.keys(colMap)[i].substring(0,11)+"..</span></td></tr>";
        }else{
            html +="<canvas  width='7' height='7' style='margin-left:5px;margin-right:5px;background:" + color(i) + "'></canvas>";
            html +="<span   id='"+Object.keys(colMap)[i]+"'  onmouseover='setMouseOverEvent(this.id,\""+"chart1"+"\")' onmouseout='setMouseOutEvent(this.id,\""+"chart1"+"\")' style='margin-left:5px;font-size: 9px;color:"+color(i)+"'> "+Object.keys(colMap)[i]+"</span></td></tr>";
        }
    }
    html +="</table></div></div>";
    $("#"+div).append(html);

}

function synchronizedMouseOverSplit(id1) {
    var id = id1.replace(/[^a-zA-Z0-9]/g, '', 'gi');
    var indexValue = "index-" + id;
    var barSelector = "." + "bars-Bubble-" + indexValue;
    var selectedBar = d3.selectAll(".bars-Bubble-index-" + indexValue.replace(/[^a-zA-Z0-9]/g, '', 'gi'));
//    var selectedBar = d3.selectAll(barSelector);
    var ids = $(barSelector).map(function() {
        return this.id;
    }).toArray();
    var tabWidth = $(window).width() * 150 / $(window).width();
    var htmlstr = "<table class=\"tBody\" style=' align:left; margin-top:2%;margin-left:0%;width:" + tabWidth + "px;position:absolute'>";
    var tabcount = 0; //margin-left:-2200%
    //                htmlstr+=colarr[0]
    for (var i = 0; i < ids.length; i++) {
        var name = ids[i].split("_", 4)[3];
        var measure = ids[i].split("_", 4)[2];
        var value = ids[i].split("_", 4)[1];
        if (tabcount === 0) {
            htmlstr += "<tr class=\"thClass\" style='font-size:90%' align='center'><td>" + indexValue.replace("index-", "") + "</td><td>" + measure + "</td>";
        }
        tabcount++;
        if (tabcount % 2 === 1) {
//            htmlstr += "<tr class=\"tdClass1\"><td align='left'>" + name.trim() + "</td>";
        }
        else {
//            htmlstr += "<tr class=\"tdClass2\"><td align='left'>" + name.trim() + "</td>";
        }
        var toData;
        if (isFormatedMeasure) {
            toData = numberFormat(value, round, precition);
        }
        else {
            toData = addCommas(value);
        }
        htmlstr += "<td align='right'>" + toData + "</td></tr>";
    }
    htmlstr += "</table>";
    $("#tabDiv").html(htmlstr);
    $("#tabDiv").show();

    selectedBar.style("fill", drillShade);

    var bulletSelector = "." + "bars-Bubble-legendBullet-" + indexValue;
//    var selectedLegendBullet = d3.selectAll(bulletSelector);
    var selectedLegendBullet = d3.selectAll(".bars-Bubble-legendBullet-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi'));
    selectedLegendBullet.style("fill", drillShade);
//    selectedLegendBullet.attr("stroke", "black");

    var textSelector = "." + "bars-Bubble-legendText-" + indexValue;
    var selectedLegendText = d3.selectAll(".bars-Bubble-legendText-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi'));
//    var selectedLegendText = d3.selectAll(textSelector);
    selectedLegendText.style("fill", drillShade);
}



function buildKPI(data) {
    document.body.style.width = '100%';
    var tooltip = CustomTooltip("my_tooltip", "auto");
    var wid = $(window).width() - 200;
    var hgt = $(window).height();
    var nodes = [];


//    var jsonFile1;
//    if (parent.isInsight === true) {
//        jsonFile1 = parent.name + "_temp.json";
//    }
//    else {
//        jsonFile1 = parent.$("#tarPath").val().replace("tmp", "temp");
//    }

//    d3.json(jsonFile1, function(error, data) {
    var svg;
    var svg1;

    var margin = {
        top: 20,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - 150 - margin.left - margin.right,
            height = hgt - 70 - margin.top - margin.bottom;
    document.getElementById('kpiRegion').style.display = "";
    svg = d3.select("#kpiRegion").append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height - 50 + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var measures = JSON.parse(parent.$("#measure").val());
    var measureList = measures.toString().split(",");
    var htmlstr = "";
    htmlstr += "<table  style='border-bottom:0px dotted grey;padding-bottom:10px;'><tr class=\"KpiSection\" style='font-size:90%' align='left'>";
    for (var i = 0; i < measureList.length; i++) {
        var autoRounding;
        if (columnMap[measureList[i]] !== "undefined" && columnMap[measureList[i]] !== undefined && columnMap[measureList[i]]["rounding"] !== "undefined") {
            autoRounding = columnMap[measureList[i]]["rounding"];
        } else {
            autoRounding = "1d";
        }

        var drillFunction;
        var aggTypes = JSON.parse(parent.$("#aggregations").val());
        if (aggTypes[i] === "COUNT_DISTINCT") {
            drillFunction = "";
        }
        else {
            drillFunction = "resetKPIMeasure(this.id)";
        }
        var c = measureList[i];

        var kpiVal = d3.sum(data, function(d) {
            return d[c];
        });

        var circleColor = "";
        var circleColor = "";
        htmlstr += "<td width='20%' style='max-width:20%' class='kpiTable'>";
//if (isFormatedMeasure) {
//htmlstr +="<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>"+numberFormat(data[0][c], round, precition)+"</td></tr>";
//}
//else{
//    htmlstr +="<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>"+autoFormating(data[0][c], autoRounding)+"</td></tr>";
//}

        if (i === 0) {
            htmlstr += "<table style='border-left: 4px solid rgba(203, 142, 96, 1);' id='" + measureList[i] + "' onclick='resetKPIMeasure(this.id)'>";
            htmlstr += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgba(203, 142, 96, 1);'>" + addCommas(kpiVal) + "</td></tr>";
            htmlstr += "<tr><td style='font-size: .9em;font-weight:bold;color:rgba(203, 142, 96, 1);'>" + measureList[i] + "</td></tr>";
        }
        else {
            htmlstr += "<table style='border-left: 4px solid rgb(64,65,104);' id='" + measureList[i] + "' onclick='resetKPIMeasure(this.id)'>";
            htmlstr += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>" + addCommas(kpiVal) + "</td></tr>";
            htmlstr += "<tr><td style='font-size: .9em;font-weight:bold;color:rgb(130,130,130);'>" + measureList[i] + "</td></tr>";
        }
        htmlstr += "</table></td>";

    }
    htmlstr += "</tr><table>";
    $("#kpiRegion").html(htmlstr);


//        var viewby = [];
//        viewby.push(parent.paramarray[0]);
//        if (parent.isInsight === true || parent.changeViewby === true) {
//            parent.changeViewby = false;
//
//            tarfile = parent.name + "_tmp.json";
//
//            parent.$("#viewby").val(JSON.stringify(viewby))
//            if (parent.isInsight === true) {
//                parent.$("#tarPath").val(tarfile);
//            }
//            parent.isInsight = false;
//            $.ajax({
//                type: 'POST',
//                data: parent.$("#reportForm").serialize(),
//                url: parent.nodeListner + 'xtendReport',
//                success: function(response) {
//                    getChart();
//                },
//                error: function(jqXHR, textStatus, errorThrown) {
//                }
//
//            })
//        }
//        else {
    getChart();
//        }

//    });
    function getChart() {
//        var columns = JSON.parse(parent.$("#viewby").val());
        var measureArray = [];
        var measure = JSON.parse(parent.$("#measure").val());
        for (var i = 0; i < measure.length; i++) {
            measureArray.push(measure[i]);
        }
        var data2;

//        var jsonfile = "";
//        if (parent.isInsight === true) {
//            jsonFile = parent.name + "_tmp.json";
//        }
//        else {
//            jsonFile = parent.$("#tarPath").val();
//        }
//        parent.isInsight = false;
//        d3.json(jsonFile, function(error, data1) {
        var mindata = [];
        for (var i = 0; i < data.length; i++) {
            if (i < 15) {
                mindata.push(data[i]);
            } else {
                break;
            }
        }
        data2 = mindata;

        var margin = {
            top: 20,
            right: 00,
            bottom: 50,
            left: 120
        },
        width = wid - 150 - margin.left - margin.right,
                height = hgt - 70 - margin.top - margin.bottom;
        buildBar("GraphRegion", data2, columns, measureArray, width, height * .8);

//        });
    }
    parent.$("#image").hide();
    parent.document.getElementById("mainSection").style.visibility = "visible";

}
var datavar;

function resetKPIMeasure1(id) {
    var measures = JSON.parse(parent.$("#measure").val());
    var measureList = measures.toString().split(",");
    var elemIndex = measureList.indexOf(id);
    var currElem = measureList[0];
    measureList[0] = id;
    measureList[elemIndex] = currElem;
    parent.$("#measure").val(JSON.stringify(measureList));
    parent.$("#image").show();
    parent.document.getElementById("mainSection").style.visibility = "hidden";
    var data = JSON.parse(parent.reportData);
    buildKPIRegions(data);
    parent.$("#image").hide();
    parent.document.getElementById("mainSection").style.visibility = "visible";
}
function buildKPIRegions(div,data,columns,measures,colIds,measureIds) {
    var fun = "drillKPn Ichart(this.id)";
    var totalValList = [];
    document.body.style.width = '100%';
//    var tooltip = CustomTooltip("my_tooltip", "auto");
    var wid = $(window).width() - 200;
    var hgt = $(window).height();
    var nodes = [];
//    var jsonFile1;
//    if (parent.isKPI === true) {
//        jsonFile1 = parent.name + "_temp.json";
//    }
//    else {
//        jsonFile1 = parent.$("#tarPath").val() + "/gt.json";
//    }
//    d3.json(jsonFile1, function(error, data) {
    var svg;
    var margin = {
        top: 20,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - 150 - margin.left - margin.right,
            height = hgt - 70 - margin.top - margin.bottom;
//    document.getElementById('kpiRegion').style.display = "";
//    var measures = JSON.parse(parent.$("#measure").val());
    var measureList = measures.toString().split(",");
    svg = d3.select("#"+div).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", "124").append("svg:g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    ;
    for (var i = 0; i < measureList.length; i++) {
        var circleColor = "";
        if (i === 0)
            circleColor = "#7A5030";
        //                circleColor = "rgb(100,20,120)";
        else
            circleColor = "green";

//        svg.append("radialGradient")
//                .attr("id", "gradient" + (measureList[i]))
//                .attr("fx", "2%")
//                .attr("fy", "2%")
//                .attr("r", "50%")
//                .attr("spreadMethod", "pad")
//                .selectAll("stop")
//                .data([
//                    {
//                        offset: "0%",
//                        color: "rgb(240,240,240)"
//                    },
//                    {
//                        offset: "80%",
//                        color: circleColor
//                    }
//                ])
//                .enter().append("stop")
//                .attr("offset", function(d) {
//                    return d.offset;
//                })
//                .attr("stop-color", function(d) {
//                    return d.color;
//                });
                    }
//    var chartData = JSON.parse(parent.$("#chartData").val());
    var htmlstr = "";
    htmlstr += "<table width='97%'  style='border-bottom:0px dotted grey;padding-bottom:10px;'><tr id='kpiHeader' class=\"KpiSection\" style='font-size:90%' align='left'>";
    for (var i = 0; i < (measureList.length < 5 ? measureList.length : 5); i++) {
        var c = measureList[i];
        var meas1 = measureIds.toString().split(",")[i];
        var kpiVal = d3.sum(data["chart1"], function(d) {
            return d[c];
        });
    var measIds = JSON.parse(parent.$("#measureIds").val());
        var aggTypes = JSON.parse(parent.$("#aggregation").val());
//         var aggTypes = JSON.parse(parent.$("#aggregations").val());
            if(aggTypes[measIds.indexOf(meas1)].toLowerCase()=="avg"){
               kpiVal=kpiVal/data["chart1"].length;
            }
        if (typeof data["XtendGT"] !== "undefined") {
            kpiVal = data["XtendGT"][0][c];
        }

        var autoRounding;
        if (columnMap[measureList[i]] !== "undefined" && columnMap[measureList[i]] !== undefined && columnMap[measureList[i]]["rounding"] !== "undefined") {
            autoRounding = columnMap[measureList[i]]["rounding"];
        }
        else {
            autoRounding = "1d";
        }
        totalValList.push(data["chart1"][c]);
        var drillFunction;

//        if (aggTypes[i] === "COUNT_DISTINCT") {
//            drillFunction = "";
//        }
//        else {
            drillFunction = "resetKPIMeasure(this.id)";
//        }
//            if (i === 0) {
//               htmlstr += "<td class='verticalDiv' style='font-size:11px;'>Measures</td>"
//            }

//if (isFormatedMeasure) {
//htmlstr +="<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>"+numberFormat(data[0][c], round, precition)+"</td></tr>";
//}
//else{
//    htmlstr +="<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>"+autoFormating(data[0][c], autoRounding)+"</td></tr>";
//}
        if (i === 0) {
            htmlstr += "<td width='20%'  class='kpiTable' style='background:rgb(245,245,245);'>";
            htmlstr += "<table style='border-left: 4px solid rgba(203, 142, 96, 1);' id='" + measureList[i] + "' onclick='changeMeasureArray(\""+i+"\")'>";
            htmlstr += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgba(203, 142, 96, 1);'>" + addCommas(kpiVal) + "</td></tr>";
            htmlstr += "<tr><td style='font-size: .9em;font-weight:bold;color:rgba(203, 142, 96, 1);'>" + measureList[i] + "</td></tr>";
        }
        else {
            htmlstr += "<td width='20%' max-width='20%' class='kpiTable'>";
            htmlstr += "<table style='border-left: 4px solid rgb(64,65,104);' id='" + measureList[i] + "' onclick='changeMeasureArray(\""+i+"\")'>";
            htmlstr += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>" + addCommas(kpiVal) + "</td></tr>";
            htmlstr += "<tr><td style='font-size: .9em;font-weight:bold;color:rgb(130,130,130);'>" + measureList[i] + "</td></tr>";
        }
        htmlstr += "</table></td>";
    }
    if (measureList.length > 5) {
        htmlstr += "<td><img width='25' align='right' style='cursor: pointer;' height='20' name='next' title='Next' alt'' src='images/right.png'  id='5'  onclick='getNextMeasure(this.id,this.name)'></td>";
    }
    htmlstr += "</tr></table>";
    $("#"+div).html(htmlstr);
    getNextMeasure = function(idMeasure, prevOrNext) {
        var htmlHead = "";
        var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
        for (var i = idMeasure; i < (measureList.length < parseFloat(idMeasure + 5) ? measureList.length : parseFloat(idMeasure + 5)); i++) {
            var c = measureList[i];
            var kpiVal = d3.sum(data["chart1"], function(d) {
                return d[c];
            });
            var aggTypes = JSON.parse(parent.$("#aggregation").val());
            if(aggTypes[measIds.indexOf(measureIds[i])].toLowerCase()=="avg"){
               kpiVal=kpiVal/data["chart1"].length;
            }
//            if (typeof data["XtendGT"] !== "undefined") {
//                kpiVal = data["XtendGT"][0][c];
//            }
            var autoRounding;
            if (columnMap[measureList[i]] !== "undefined" && columnMap[measureList[i]] !== undefined && columnMap[measureList[i]]["rounding"] !== "undefined") {
                autoRounding = columnMap[measureList[i]]["rounding"];
            }
            else {
                autoRounding = "1d";
            }
            totalValList.push(data["chart1"][c]);
            var drillFunction;

            if (aggTypes[i] === "COUNT_DISTINCT") {
                drillFunction = "";
            }
            else {
                drillFunction = "resetKPIMeasure(this.id)";
            }
            if (i === 0) {
                htmlHead += "<td width='20%'  class='kpiTable' style='background:rgb(245,245,245);'>";
                htmlHead += "<table style='border-left: 4px solid rgba(203, 142, 96, 1);' id='" + measureList[i] + "' onclick='changeMeasureArray(\""+i+"\")'>";
                htmlHead += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgba(203, 142, 96, 1);'>" + addCommas(kpiVal) + "</td></tr>";
                htmlHead += "<tr><td style='font-size: .9em;font-weight:bold;color:rgba(203, 142, 96, 1);'>" + measureList[i] + "</td></tr>";
            } else {
                htmlHead += "<td width='20%' max-width='20%' class='kpiTable'>";
                htmlHead += "<table style='border-left: 4px solid rgb(64,65,104);' id='" + measureList[i] + "' onclick='changeMeasureArray(\""+i+"\")'>";
                htmlHead += "<tr><td style='font-size: 1.2em;font-weight: bold;color:rgb(64,65,104);'>" + addCommas(kpiVal) + "</td></tr>";
                htmlHead += "<tr><td style='font-size: .9em;font-weight:bold;color:rgb(130,130,130);'>" + measureList[i] + "</td></tr>";
            }
            htmlHead += "</table></td>";
        }
        if (measureList.length > parseFloat(idMeasure + 5)) {
            htmlHead += "<td><img width='25' align='right' style='cursor: pointer;' height='20' name='next' title='Next' alt'' src='images/right.png'  id='" + parseFloat(idMeasure + 5) + "'  onclick='getNextMeasure(this.id,this.name)'></td>";
        }
        if (idMeasure >= 5) {
            htmlHead += "<td><img width='25' align='right' style='cursor: pointer;' height='20' name='prev' title='Next' alt'' src='images/left.png'  id='" + parseFloat(idMeasure - 5) + "'  onclick='getNextMeasure(this.id,this.name)'></td>";
        }
        $("#kpiHeader").html(htmlHead);
    };
//        var viewby = [];
//        viewby.push(parent.paramarray[0]);
//        if (parent.isKPI === true) {
//            tarfile = parent.name + "_tmp";
//            var chartType = 'KPI';
//            parent.$("#chartType").val(chartType);
//            parent.$("#viewby").val(JSON.stringify(viewby));
//            parent.$("#tarPath").val(tarfile);
//            $.ajax({
//                type: 'POST',
//                data: parent.$("#reportForm").serialize(),
//                url: parent.nodeListner + 'xtendReport',
//                success: function(response) {
//                    getKPIChart();
//                },
//                error: function(jqXHR, textStatus, errorThrown) {
//                }
//
//            });
//            parent.isKPI = false;
//        }
//        else {
    getKPIChart();
//        }

//    });
    var margin = {
        top: 20,
        right: 00,
        bottom: 150,
        left: 120
    },
    width = wid - 150 - margin.left - margin.right,
            height = hgt - 70 - margin.top - margin.bottom;
    function getKPIChart() {
        var fun = "drillKPIchart(this.id)";
//        var div = d3.select("#"+div).append("div").attr("width", width + margin.left + margin.right)
//                .attr("height", height - 50 + margin.top + margin.bottom);
//        var columns = JSON.parse(parent.$("#groupbys").val());
//        var measures = JSON.parse(parent.$("#measure").val());
        var measureList = measures.toString().split(",");
        height = "124";
        var htmlstr1 = "";
        htmlstr1 += "<table  width='98%'>";
        for (var k = 0; k < columns.length; k++) {

//            $.ajax({
//                type: "GET",
//                url: parent.$("#tarPath").val().replace(".json", "", "gi") + "/KPI" + k + ".json",
//                data: data,
//                async: false,
//                dataType: "json",
//                success: function(data) {
//                    window.datavar = data;
//                }
//            });
            datavar = [];
            if(Object.keys(data).indexOf(("chart"+parseFloat(k+1)))!=-1){
            datavar = data["chart"+parseFloat(k+1)];
            datavar = datavar.sort(function(a, b) {
                return b[measureList[0]] - a[measureList[0]];
            });

            htmlstr1 += "<tr class=\"KpiSection\" id='kpitab_" + columns[k].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' style='width:20%;border-rigth:2px solid grey;font-size:90%' align='left' >";
                }
            for (var i = 0; i < (datavar.length < 5 ? datavar.length : 5); i++) {
                var othersList = 0;
                var circleRadius = "65";

                var c = measureList[0];
                var autoRounding;
                if (columnMap[c] !== "undefined" && columnMap[c] !== undefined && columnMap[c]["rounding"] !== "undefined") {
                    autoRounding = columnMap[c]["rounding"];
                }
                else {
                    autoRounding = "1d";
                }
                if (i === 0) {
                    htmlstr1 += "<td class='verticalDiv' width='15%' max-width='1%' style='font-size:11px;'>" + columns[k] + "</td>";
                }
//                circleRadius = parseFloat(circleRadius) - 5;
                htmlstr1 += "<td style='border-bottom:1px solid rgb(220,220,220);padding-top:20px;padding-bottom:5px' width='20%' max-width='20%' class='kpiTable'>";
                htmlstr1 += "<table  id='" + colIds[k] + ":" + datavar[i][columns[k]] + "' style='border-left: 4px solid " + kpiColor1[i % 2] + ";' onclick='" + fun + "'>";
//  if (isFormatedMeasure) {
//htmlstr1 +="<tr><td style='font-size: 1.2em;font-weight: bold;color:"+kpiColor1[i%2]+"'>"+numberFormat(datavar[i][c], round, precition)+"</td></tr>";
//  }
//  else{
//  htmlstr1 +="<tr><td style='font-size: 1.2em;font-weight: bold;color:"+kpiColor1[i%2]+"'>"+autoFormating(datavar[i][c], autoRounding)+"</td></tr>";
//  }
                htmlstr1 += "<tr><td style='font-size: 1.2em;font-weight: bold;color:" + kpiColor1[i % 2] + "'>" + addCommas(datavar[i][c]) + "</td></tr>";
                htmlstr1 += "<tr><td style='font-size: .9em;color:rgb(150,150,150);'>" + datavar[i][columns[k]] + "</td></tr>";
                htmlstr1 += "</table></td>";
            }
            if (datavar.length > 5) {
                htmlstr1 += "<td><img width='25' align='right' style='cursor: pointer;' height='20' name='5' title='Next' alt'' src='images/right.png'  id='" + k + "'  onclick='getNextKpi(this.id,this.name)'></td>";
            }
            htmlstr1 += "</tr>";
        }
        htmlstr1 += "</table>";
        $("#"+div).append(htmlstr1);
    }
    getNextKpi = function(id, next) {
        var m = id;
//        columns = JSON.parse(parent.$("#groupbys").val());
        var datavarNext = data["chart"+parseFloat(parseFloat(m)+1)];
//        var columnMap = JSON.parse(parent.$("#columnMap").val());
//        if (typeof columnMap[columns[m]] !== "undefined" && typeof columnMap[columns[m]]["dataType"] !== "undefined" && ((columnMap[columns[m]]["dataType"] === "date" || columnMap[columns[m]]["dataType"] === "DATE" || columnMap[columns[m]]["dataType"] === "month" || columnMap[columns[m]]["dataType"] === "qtr" || columnMap[columns[m]]["dataType"] === "QTR" || columnMap[columns[m]]["dataType"] === "monthyear" || columnMap[columns[0]]["dataType"] === "year"))) {
//            if (columnMap[columns[0]]["dataType"] === "year") {
//                datavarNext = datavarNext.sort(function(a, b) {
//                    var firstYear = a[columns[0]].split("-", 2)[0];
//                    var secondYear = b[columns[0]].split("-", 2)[0];
//                    return firstYear - secondYear;
//                });
//            } else if (columnMap[columns[m]]["dataType"] === "qtr" || columnMap[columns[m]]["dataType"] === "QTR") {
//                datavarNext = showOrdered("Alphabetic", "Ascend", "all", datavarNext, false);
//            } else {
//                datavarNext = datavarNext.sort(function(a, b) {
//                    return (moment(a[columns[m]], columnMap[columns[m]]["dateFormat"].toUpperCase()) - moment(b[columns[m]], columnMap[columns[m]]["dateFormat"].toUpperCase()));
//                });
//            }
//        }
        var htmlstrKpi = "";

        if (datavarNext.length > next) {
            for (var i = next; i < (datavarNext.length < (parseFloat(next) + 5) ? datavarNext.length : (parseFloat(next) + 5)); i++) {

                var c = measureList[0];
                var autoRounding;
                if (columnMap[c] !== "undefined" && columnMap[c] !== undefined && columnMap[c]["rounding"] !== "undefined") {
                    autoRounding = columnMap[c]["rounding"];
                }
                else {
                    autoRounding = "1d";
                }
                if (i === next) {
                    htmlstrKpi += "<td class='verticalDiv' width='15%' max-width='15%'  style='font-size:11px;'>" + columns[m] + "</td>";
                }
                htmlstrKpi += "<td style='border-bottom:1px solid rgb(220,220,220);padding-top:20px;padding-bottom:5px' width='20%' max-width='20%' class='mpiTable'>";
                htmlstrKpi += "<table  id='" + colIds[m] + ":" + datavarNext[i][columns[m]] + "' style='border-left: 4px solid " + kpiColor1[i % 2] + ";' onclick='" + fun + "'>";
                htmlstrKpi += "<tr><td style='font-size: 1.2em;font-weight: bold;color:" + kpiColor1[i % 2] + "'>" + addCommas(datavarNext[i][c]) + "</td></tr>";
                htmlstrKpi += "<tr><td style='font-size: .9em;color:rgb(150,150,150);'>" + datavarNext[i][columns[m]] + "</td></tr>";
                htmlstrKpi += "</table></td>";
            }
        }
        htmlstrKpi += "<td><table>";
//      htmlstrKpi += "<td><a href='#' id='"+m+"' onclick='getPrevKpi(this.id)'>Prev</a></td>";
        if (datavarNext.length > (parseFloat(next) + 5)) {
            htmlstrKpi += "<tr><td><img width='25' align='right' style='cursor: pointer;' height='20' name='" + (parseFloat(next) + 5) + "' title='Next' alt'' src='images/right.png'  id='" + m + "'  onclick='getNextKpi(this.id,this.name)'></td></tr>";
        }
        htmlstrKpi += "<tr><td><img width='25' align='right' style='cursor: pointer;' height='20' name='" + (parseFloat(next)) + "' title='Previous' alt'' src='images/left.png'  id='" + m + "'  onclick='getPrevKpi(this.id,this.name)'></td></tr>";
        htmlstrKpi += "</table></td>";
        $("#kpitab_" + columns[m].replace(/[^a-zA-Z0-9]/g, '', 'gi')).html(htmlstrKpi);
    };

    getPrevKpi = function(id, prev) {
        var m = id;
//        columns = JSON.parse(parent.$("#groupbys").val());
       var datavarNext = data["chart"+parseFloat(parseFloat(m)+1)];
//        var columnMap = JSON.parse(parent.$("#columnMap").val());
//        if (typeof columnMap[columns[m]] !== "undefined" && typeof columnMap[columns[m]]["dataType"] !== "undefined" && ((columnMap[columns[m]]["dataType"] === "date" || columnMap[columns[m]]["dataType"] === "DATE" || columnMap[columns[m]]["dataType"] === "month" || columnMap[columns[m]]["dataType"] === "qtr" || columnMap[columns[m]]["dataType"] === "QTR" || columnMap[columns[m]]["dataType"] === "monthyear" || columnMap[columns[0]]["dataType"] === "year"))) {
//            if (columnMap[columns[0]]["dataType"] === "year") {
//                datavarNext = datavarNext.sort(function(a, b) {
//                    var firstYear = a[columns[0]].split("-", 2)[0];
//                    var secondYear = b[columns[0]].split("-", 2)[0];
//                    return firstYear - secondYear;
//                });
//            } else if (columnMap[columns[m]]["dataType"] === "qtr" || columnMap[columns[m]]["dataType"] === "QTR") {
//                datavarNext = showOrdered("Alphabetic", "Ascend", "all", datavarNext, false);
//            } else {
//                datavarNext = datavarNext.sort(function(a, b) {
//                    return (moment(a[columns[m]], columnMap[columns[m]]["dateFormat"].toUpperCase()) - moment(b[columns[m]], columnMap[columns[m]]["dateFormat"].toUpperCase()));
//                });
//            }
//        }
        var htmlstrKpi = "";
        if (datavarNext.length > (parseFloat(prev) - 5)) {
            for (var i = (parseFloat(prev) - 5); i < (datavarNext.length < prev ? datavarNext.length : prev); i++) {
                var c = measureList[0];
                var autoRounding;
                if (columnMap[c] !== "undefined" && columnMap[c] !== undefined && columnMap[c]["rounding"] !== "undefined") {
                    autoRounding = columnMap[c]["rounding"];
                }
                else {
                    autoRounding = "1d";
                }
                if (i === (parseFloat(prev) - 5)) {
                    htmlstrKpi += "<td class='verticalDiv' width='15%' max-width='15%' style='font-size:11px;'>" + columns[m] + "</td>";
                }
                htmlstrKpi += "<td style='border-bottom:1px solid rgb(220,220,220);padding-top:20px;padding-bottom:5px' width='20%' max-width='20%' class='mpiTable'>";
                htmlstrKpi += "<table  id='" + colIds[m] + ":" + datavarNext[i][columns[m]] + "' style='border-left: 4px solid " + kpiColor1[i % 2] + ";' onclick='" + fun + "'>";
                htmlstrKpi += "<tr><td style='font-size: 1.2em;font-weight: bold;color:" + kpiColor1[i % 2] + "'>" + addCommas(datavarNext[i][c]) + "</td></tr>";
                htmlstrKpi += "<tr><td style='font-size: .9em;color:rgb(150,150,150);'>" + datavarNext[i][columns[m]] + "</td></tr>";
                htmlstrKpi += "</table></td>";
            }
        }
        htmlstrKpi += "<td><table>";
        htmlstrKpi += "<tr><td><img width='25' align='right' style='cursor: pointer;' height='20' name='" + prev + "' title='Next' alt'' src='images/right.png'  id='" + m + "'  onclick='getNextKpi(this.id,this.name)'></td></tr>";
        if (prev > 5) {
            htmlstrKpi += "<tr><td><img width='25' align='right' style='cursor: pointer;' height='20' name='" + (parseFloat(prev) - 5) + "' title='Next' alt'' src='images/left.png'  id='" + m + "'  onclick='getPrevKpi(this.id,this.name)'></td></tr>";
        }
        htmlstrKpi += "</table></td>";
        $("#kpitab_" + columns[m].replace(/[^a-zA-Z0-9]/g, '', 'gi')).html(htmlstrKpi);
    };
//    parent.$("#image").hide();
//    parent.document.getElementById("mainSection").style.visibility = "visible";
}
function buildDrill(width, height) {
//    if (parent.drillList.length > 0) {
//        var cx;
//        var cx1;
//        var measure = JSON.parse(parent.$("#measure").val())[0];
//        var autoRounding;
//        if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
//            autoRounding = columnMap[measure]["rounding"];
//        }
//        else {
//            autoRounding = "1d";
//        }
//        for (var i = 0; i < parent.drillList.length; i++) {
//            var nameVal = parent.drillList[i];
//            if (!(nameVal.length < 10)) {
//                nameVal = nameVal.substring(0, 9) + "..";
//            }
//            if (i === 0) {
//                cx = "60";
//                cx1 = 60;
//            }
//            else {
//                cx = parseFloat(cx) + 100;
//                cx1 = cx;
//            }
//            group1 = svg.append("svg:g")
//                    .attr("stroke", "white")
//                    .attr("stroke-width", 3)
//                    .attr("fill", "green");
//            circle1 = group1.append("svg:circle")
//                    .attr("class", "circledrill")
//                    .attr("cx", cx)
//                    .attr("cy", height * .90)
//                    .attr("r", "40")
//                    .attr("id", parent.idValList[i])
//                    .attr("onclick", "reset1(this.id)");
//
//            if (isFormatedMeasure) {
//                group1.append("g:text")
//                        .attr("dx", cx1)
//                        .attr("dy", height * .93)
//                        .attr("text-anchor", "middle")
//                        .text(numberFormat(parent.drillValList[i], round, precition))
//                        .style("stroke", "white")
//                        .style("stroke-width", 0)
//                        .style("font-size", "12px")
//                        .style("fill", "white");
//            } else {
//                group1.append("g:text")
//                        .attr("dx", cx1)
//                        .attr("dy", height * .90)
//                        .attr("text-anchor", "middle")
//                        .text(autoFormating(parent.drillValList[i], autoRounding))
//                        .style("stroke", "white")
//                        .style("stroke-width", 0)
//                        .style("font-size", "12px")
//                        .style("fill", "white");
//            }
//
//            group1.append("g:text")
//                    .attr("dx", cx1)
//                    .attr("dy", height)
//                    .attr("text-anchor", "middle")
//                    .text(nameVal)
//                    .style("stroke", "black")
//                    .style("stroke-width", 0)
//                    .style("font-size", "11px")
//                    .style("fill", "black")
//                    .style("font-family: lucida grande");
//        }
//    }
}


function buildstackedBarH(div,data, columns, measureArray,wid,hgt){

     wid=parseFloat($(window).width())*(.35);
    var measure = measureArray[0];
    var chartMap = {};
    var autoRounding;
    if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
        autoRounding = columnMap[measure]["rounding"];
    } else {
        autoRounding = "1d";
    }
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
//    var wid = $(window).width() - 200;
//    var hgt = $(window).height() - 150;
    var yAxis;
    var margin = {
        top: 5,
        right: 20,
        bottom: 30,
//        left: 40
        left: 75
    },
    width = wid - margin.left - margin.right,
            height = hgt - margin.top - margin.bottom-110;

    var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1);

    var y = d3.scale.linear()
            .rangeRound([height, 0]);
    var yAxis = d3.svg.axis()
            .scale(x)
            .orient("left");

//    if (isFormatedMeasure) {
//        yAxis = d3.svg.axis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d) {
//                    return d;
////                    return numberFormat(d, round, precition);
//                });
//
//    } else {
//        yAxis = d3.svg.axis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d, i) {
//            return addCommas(d);
////                    return autoFormating(d, autoRounding);
//                    //            return d;
//                });
//    }
    var localColorMap = {};
    var svg = d3.select("#"+div).append("svg")
//            .attr("width", width + margin.left + margin.right)
            .attr("id", "svg_" + div)
            .attr("width",wid)
            .attr("height", height + margin.top + margin.bottom)
            .attr("height", hgt-50 )
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

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
    y.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    var totalMax = maximumValue(data, "total");
    y.domain([0, totalMax]);
    svg.append("g")
             .attr("class", "y axis")
            .call(yAxis)
            .append("text")
            .attr("transform", "rotate(-90)")
            .attr("y", -1)
            .attr("dy", ".91em")
            .style("text-anchor", "end")
            .text(function(d) {
                if (d.length < 12)
                    return d;
                else
                    return d.substring(0, 8) + "..";
            });


//    svg.append("g")
//            .attr("class", "y axis")
//            .call(yAxis)
//            .append("text")
//            .attr("transform", "rotate(-90)")
//            .attr("y", -1)
//            .attr("dy", ".91em")
//            .style("text-anchor", "end")
//            .text(columns[0]);
//            var ht = parseFloat(hgt)* .65;
//             for (var j=0; j <= ht; j=j+50) {
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
                return "translate(" + x(d[columns[0]]) + ",0)";
            });

    state.selectAll("rect")
            .data(function(d) {
                return d.val;
            })
            .enter().append("rect")
            .attr("width", x.rangeBand())
            .attr("rx", 3)
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
//                return "green";//color(j);//"url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            return color(i);
            })
            .attr("id", function(d, i) {
                return col[i];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition);
                }
                else {
                    msrData = addCommas(d.value);
                }
                var content = "";
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[i][columns[0]] + "</span><br/>";
                content += "<span class=\"name\"> " + d.name + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            }).attr("index_value", function(d, i) {
        return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            });
//    showLegends1(measureArray, localColorMap, width, svg);
var chartData = JSON.parse(parent.$("#chartData").val());
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

function buildZoomabletree(div, data, columns, measureArray,divWidth,divHeight) {
    var drillValues = "";
    try {
        var temp = JSON.parse(parent.$("#drills").val());
        var keys = Object.keys(temp);
        for (var key in keys) {
            drillValues += temp[keys[key]] + " > ";
        }
    } catch (e) {
    }
    var root, node;
    var fun = "drillWithinchart(this.id)";
    node = root = data;
    var wid = divWidth;
    var hgt = divHeight;
    var margin = {
        top: 10,
        right: 0,
        bottom: 0,
        left: 0
    },
    width = wid - 100,
            height = hgt - 150,
            formatNumber = d3.format(",d"),
            transitioning;
    var color = d3.scale.category10();
    var x = d3.scale.linear()
            .domain([0, width])
            .range([0, width]);
    var y = d3.scale.linear()
            .domain([0, height])
            .range([0, height]);
    var treemap = d3.layout.treemap()
            .round(false)
            .size([width, height])
            .sticky(true)
            .value(function(d) {
                return d.size;
            });
    var svg = d3.select("#"+div).append("div")
            .attr("class", "chart")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.bottom + margin.top)
            .style("margin-left", -margin.left + "px")
            .style("margin.right", -margin.right + "px")
            .append("svg:svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.bottom + margin.top)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
            .style("shape-rendering", "crispEdges");
    var grandparent = svg.append("g")
            .attr("class", "grandparent");
    grandparent.append("rect")
            .attr("y", -margin.top)
            .attr("width", width)
            .attr("height", margin.top);
    grandparent.append("text")
            .attr("x", 6)
            .attr("y", -margin.top)
            .attr("dy", ".75em");
    initialize(root);
    accumulate(root);
    layout(root);
    display(root);
    function initialize(root) {
        root.x = root.y = 0;
        root.dx = width;
        root.dy = height;
        root.depth = 0;
    }
    function accumulate(d) {
        return d.children
                ? d.size = d.children.reduce(function(p, v) {
                    return p + accumulate(v);
                }, 0)
                : d.size;
    }
    function layout(d) {
        if (d.children) {
            treemap.nodes({
                children: d.children
            });
            d.children.forEach(function(c) {
                c.x = d.x + c.x * d.dx;
                c.y = d.y + c.y * d.dy;
                c.dx *= d.dx;
                c.dy *= d.dy;
                c.parent = d;
                layout(c);
            });
        }
    }
    function display(d) {
        grandparent
                .datum(d.parent)
                .attr("onclick", "reset()")
                .select("text")
                .text(
                        drillValues + columns[0]
                        );
        var g1 = svg.insert("g", ".grandparent")
                .datum(d)
                .attr("class", "depth");
        var g = g1.selectAll("g")
                .data(d.children)
                .enter().append("g");
        g.filter(function(d) {
            return d.children;
        })
                .classed("children", true);
        g.selectAll(".child")
                .data(function(d) {
                    return d.children || [d];
                })
                .enter().append("rect")
                .attr("class", "child")
                .call(rect);
        var chartMap = {};
        colorMap = {};
        g.append("rect")
                .style("fill", function(d, i) {
                    var colorShad = color(i);
//                    if (typeof chartMap[d.name] === "undefined") {
//                        chartMap[d.name] = colorShad;
//                        colorMap[i] = d.name + "__" + colorShad;
//                    }
                    return colorShad;
                }).attr("id", function(d) {
            return d.name + ":" + d[measureArray[0]];
        })
                .attr("color_value", function(d, i) {
                    var colorShad = color(i);
                    if (typeof chartMap[d.name] === "undefined") {
                        chartMap[d.name] = colorShad;
                        colorMap[i] = d.name + "__" + colorShad;
                    }
                    return colorShad;
//            return fill(i);
                })

                .attr("class", function(d, i) {
                    return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
                })
                .attr("onclick", fun)
                .on("mouseover", function(d, i) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.value, round, precition);
                    }
                    else {
                        msrData = addCommas(d.value);
                    }
                    var content;
                    content = "<span class=\"name\"> " + d.parent.name + "</span><br/>";
                    content += "<span class=\"value\"> " + d.name + "</span><br/>";
                    content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                    return tooltip.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                })
                .call(rect);
        parent.$("#colorMap").val(JSON.stringify(colorMap));
        g.append("text")
                .attr("dy", ".75em")
                .text(function(d) {
                    return d.name;
                })
                .call(text);

        function transition(d) {
            if (transitioning || !d)
                return;
            transitioning = true;

            var g2 = display(d),
                    t1 = g1.transition().duration(750),
                    t2 = g2.transition().duration(750);

            // Update the domain only after entering new elements.
            x.domain([d.x, d.x + d.dx]);
            y.domain([d.y, d.y + d.dy]);

            // Enable anti-aliasing during the transition.
            svg.style("shape-rendering", null);

            // Draw child nodes on top of parent nodes.
            svg.selectAll(".depth").sort(function(a, b) {
                return a.depth - b.depth;
            });

            // Fade-in entering text.
            g2.selectAll("text").style("fill-opacity", 0);

            // Transition to the new view.
            t1.selectAll("text").call(text).style("fill-opacity", 0);
            t2.selectAll("text").call(text).style("fill-opacity", 1);
            t1.selectAll("rect").call(rect);
            t2.selectAll("rect").call(rect);

            // Remove the old node when the transition is finished.
            t1.remove().each("end", function() {
                svg.style("shape-rendering", "crispEdges");
                transitioning = false;
            });
        }

        return g;
    }

    function text(text) {
        text.attr("x", function(d) {
            return x(d.x) + 6;
        })
                .attr("y", function(d) {
                    return y(d.y) + 6;
                })
                .style("opacity", function(d) {
                    d.w = this.getComputedTextLength();
                    return x(d.x + d.dx) - x(d.x) > d.w ? 1 : 0;
                });
    }

    function rect(rect) {
        rect.attr("x", function(d) {
            return x(d.x);
        })
                .attr("y", function(d) {
                    return y(d.y);
                })
                .attr("width", function(d) {
                    return x(d.x + d.dx) - x(d.x);
                })
                .attr("height", function(d) {
                    return y(d.y + d.dy) - y(d.y);
                });
    }

    function name(d) {
        return d.parent
                ? name(d.parent) + "." + d.name
                : d.name;
    }
}

function buildZoomabletreeMulti(div, data, columns, measureArray,divWidth,divHeight) {
    var wid;
    var hgt = divHeight +30;
    var old_dname='';
    var m=0;
    graphProp(div);
 var chartData = JSON.parse(parent.$("#chartData").val());
     var color = d3.scale.category10();
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var drillValues = "";
    try {
        var temp = JSON.parse(parent.$("#drills").val());
        var keys = Object.keys(temp);
        for (var key in keys) {
            drillValues += temp[keys[key]] + " > ";
        }
    } catch (e) {
    }
    if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){
    wid = divWidth;
}
else{
    wid = divWidth*.85;
}
    var w = wid+1,
            h = hgt - 50,
            x = d3.scale.linear().range([0, w]),
            y = d3.scale.linear().range([0, h]),
            root,
            node;

    var treemap = d3.layout.treemap()
            .round(false)
            .size([w, h])
            .sticky(true)
            .value(function(d) {
                if(chartData[div]["equalInterval"]!=="undefined" && chartData[div]["equalInterval"]=="Yes"){
                return d.size;
                    
                }else{
                    return d.rectValue;
                }
            });

     var color = d3.scale.category10();

if (typeof chartData[div]["legendLocation"] === "undefined" || chartData[div]["legendLocation"] === "Right"){
    var margin = {
        top: 10,
        right: (divWidth*.08),
        bottom: (divHeight*.1),
        left: 50
    },
    width = ((divWidth- margin.left - margin.right)*1.07)-40,
            height = divHeight  - margin.top - margin.bottom;
}else{
    var margin = {
        top: 10,
        right: (divWidth*.08),
        bottom: (divHeight*.1),
        left: 50
    },
    width = ((divWidth- margin.left - margin.right)*1.13)+40,
            height = ((divHeight  - margin.top - margin.bottom))*.9;
}
//    var divcontent = "";
    var col2 = "";
    if (typeof columns[1] !== "undefined") {
        col2 = columns[1];
    }
//    divcontent += "<div id='resetDiv' style='width:" + w + "px;margin:.04% auto auto;'><sapn onclick='reset()'  class='depth'>" +
//            drillValues + columns[0] + "_" + col2 + "</span></div>";
//    $("#"+div).append(divcontent);

    var svg = d3.select("#"+div).append("div")
            .attr("class", "chart")
            .style("width", w + "px")
            .style("height", h + "px")
            .style("margin-top", "0px")
            .style("float", "left")


            .append("svg:svg")
            .attr("width", divWidth)
            .attr("height", h)
            .append("svg:g")
            .attr("transform", "translate(.5,.5)");


    node = root = data;

    var nodes = treemap.nodes(root)
            .filter(function(d) {
                return !d.children;
            });

    //node = root;
//    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(nodes).enter()
//            .append("svg:linearGradient")
//            .attr("id", function(d) {
//                return "gradient" + (d.parent.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//            .attr("x1", "0%")
//            .attr("y1", "30%")
//            .attr("x2", "50%")
//            .attr("y2", "30%")
//            .attr("spreadMethod", "pad")
//            .attr("gradientTransform", "rotate(27)");
//
//    gradient.append("svg:stop")
//            .attr("offset", "0%")
//            .attr("stop-color", function(d, i) {
//                return color(d.parent.name);
//            });
//    gradient.append("svg:stop")
//            .attr("offset", "30%")
//            .attr("stop-color", "rgb(240,240,240)")
//            .attr("stop-opacity", 1);
//    gradient.append("svg:stop")
//            .attr("offset", "37%")
//            .attr("stop-color", function(d, i) {
//                return color(d.parent.name);
//            });
//    gradient.append("svg:stop")
//            .attr("offset", "100%")
//            .attr("stop-color", function(d, i) {
//                return color(d.parent.name);
//            })
//            .attr("stop-opacity", 1);
var mapKey = {};
for(var k in nodes){
 mapKey[nodes[k].parent.name]="Color";
}
var pre_m=0;
var productKey =mapKey;
    var cell = svg.selectAll("g")
            .data(nodes)
            .enter()
            .append("svg:g")
            .attr("class", "cell")
            .attr("id", function(d) {
                return d.parent.name + ":" + d.name;
            })
            .attr("transform", function(d) {
                return "translate(" + d.x + "," + d.y + ")";
            })
            .attr("onclick", fun)//function(d) {
            //        return zoom(node === d.parent ? root : d.parent);
            //    })
            .on("mouseover", function(d, i) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(d.value, round, precition);
                }
                else {
                    msrData = addCommas(d.value);
                }
                var content = "";
                content += "<span class=\"name\">" + columns[0] + "</span><span class=\"value\"> " + d.parent.name + "</span><br/>";
                content += "<span class=\"name\">" + columns[1] + "</span><span class=\"value\"> " + d.name + "</span><br/>";
                content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
    var chartMap = {};
    var colorMap = {};
    cell.append("svg:rect")
            .attr("width", function(d) {
                return d.dx - 1;
            })
            .attr("height", function(d) {
                return d.dy - 1;
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.parent.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .style("fill", function(d, i) {
         
         /*Added by Ashutosh*/
            if(productKey[d.parent.name]==="Color"){
                        m=m+1;
                        delete productKey[d.parent.name];
                        }

            var colorShad=getDrawColor(div,m-1);
                if (typeof chartMap[d.parent.name] === "undefined") {
                    chartMap[d.parent.name] = colorShad;
                    colorMap[i] = d.parent.name + "__" + colorShad;
                }
                return colorShad;
            })
            .attr("color_value", function(d, i) {
                var colorShad = color(d.parent.name);
                if (typeof chartMap[d.parent.name] === "undefined") {
                    chartMap[d.parent.name] = colorShad;
                    colorMap[i] = d.parent.name + "__" + colorShad;
                }
                return colorShad;
//            return fill(i);
            });
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    cell.append("svg:text")
            .attr("x", function(d) {
                return d.dx / 2;
            })
            .attr("y", function(d) {
                return d.dy / 2;
            })
            .attr("dy", ".35em")
            .attr("text-anchor", "middle")
            .attr("fill","#d8d47d")
            .text(function(d) {
                return d.name;
            })
            .style("opacity", function(d) {
                d.w = this.getComputedTextLength();
                return d.dx > d.w ? 1 : 0;
            });
//            alert(JSON.stringify(chartMap))
    cell.append("svg:text")
            .attr("x", function(d) {
                return d.dx / 2;
            })
            .attr("y", function(d) {
                return d.dy / 1.65;
            })
            .attr("dy", ".71em")
            .attr("text-anchor", "middle")
            .attr("fill","#d8d47d")
            .style("font-weight","bold")
            .text(function(d,i) {
               if(d.dy <=33){
                    return  "";
               }else{
               return  addCommas(numberFormat(d.rectValue,yAxisFormat,yAxisRounding,div))
               }
            })
            .style("opacity", function(d) {
                d.w = this.getComputedTextLength();
                return d.dx > d.w ? 1 : 0;
    });



    function zoom(d) {
        var kx = w / d.dx, ky = h / d.dy;
        x.domain([d.x, d.x + d.dx]);
        y.domain([d.y, d.y + d.dy]);

        var t = svg.selectAll("g.cell").transition()
                .duration(d3.event.altKey ? 7500 : 750)
                .attr("transform", function(d) {
                    return "translate(" + x(d.x) + "," + y(d.y) + ")";
                });

        t.select("rect")
                .attr("width", function(d) {
                    return kx * d.dx - 1;
                })
                .attr("height", function(d) {
                    return ky * d.dy - 1;
                });

        t.select("text")
                .attr("x", function(d) {
                    return kx * d.dx / 2;
                })
                .attr("y", function(d) {
                    return ky * d.dy / 2;
                })
                .style("opacity", function(d) {
                    return kx * d.dx > d.w ? 1 : 0;
                });

        node = d;
        d3.event.stopPropagation();
    }
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
else{   //var displayLegends = chartData[div]["displayLegends"];
            var yvalue=0;
            var rectyvalue=0;
            var yLegendvalue=0;
            var rectyvalue1=0;
            var len = parseInt(divWidth-150);
            var rectlen = parseInt(divWidth-200);
            var fontsize = parseInt(divWidth/45);
            var fontsize1 = parseInt(divWidth/50);
            var rectsize = parseInt(divWidth/60);
            rectsize=rectsize>10?10:rectsize;
            var legendLength;
            var keys1=Object.keys(chartMap);
            if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                legendLength=chartData[div]["legendNo"];
            }else{
                legendLength=(keys1.length<12?keys1.length:12);
}
            if(legendLength>12){//
            yvalue = parseInt(height / 8);
            rectyvalue = parseInt((height / 8)-10);

}
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);

            }

             if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
            if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
            }

            if(typeof chartData[div]["showViewBy"]!='undefined' && chartData[div]["showViewBy"]=='Y'){
                var transform =
                svg.append("g")
                .append("text")
                .attr("style","margin-right:10")
                .attr("style", "font-size:"+fontsize+"px")
                .attr("transform", "translate(" +((divWidth- margin.left - margin.right+28)*1.07)  + "," + (yLegendvalue+13) + ")")
                .attr("fill", "Black")
                .text(function(d){
                    return columns[0];
                })
                .attr("svg:title",function(d){
                    return columns[0];
                })
            }
             var colMap={};
             var count = 0;

            for(var i=0;i<legendLength;i++){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.06)
            rectyvalue = parseInt(rectyvalue+height*.06)
            }
                        svg.append("g")
                        .append("rect")
                        .attr("style","margin-right:10")
                        .attr("transform", transform)
                        .attr("style", "overflow:scroll")
                        .attr("transform", "translate(" + (divWidth*.86)  + "," + (rectyvalue+15)+ ")")
                        .attr("width", rectsize)
                        .attr("height", rectsize)
                        .attr("fill", function(d){
                            return chartMap[keys1[i]];
                        })
                    svg.append("g")
                    .append("text")
                    .attr("transform", "translate(" +(divWidth*.88)  + "," + (yvalue+14) + ")")
                    .attr("fill", function(d,i){
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
                        return  keys1[i];
                    } )
                    .text(function(d){
                       return keys1[i];
                    })
                    .attr("svg:title",function(d){
                        return  keys1[i];
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

function buildCircledrill(height) {
    var measure = JSON.parse(parent.$("#measure").val())[0];
    var autoRounding;
    if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
        autoRounding = columnMap[measure]["rounding"];
    } else {
        autoRounding = "1d";
    }
    if (parent.drillList.length > 0) {
        for (var i = 0; i < parent.drillList.length; i++) {
            var nameVal = parent.drillList[i];
            if (!(nameVal.length < 10)) {
                nameVal = nameVal.substring(0, 9) + "..";
            }
            group1 = svg.append("svg:g")
                    .attr("stroke", "white")
                    .attr("stroke-width", 3)
                    .attr("fill", "green");
            circle1 = group1.append("svg:circle")
                    .attr("class", "circledrill")
                    .attr("cx", i * 5 * "20")
                    .attr("cy", height * 1.28)
                    .attr("r", "40")
                    .attr("id", parent.idValList[i])
                    .attr("onclick", "reset1(this.id)");
            if (isFormatedMeasure) {
                group1.append("g:text")
                        .attr("dx", i * 5 * "20")
                        .attr("dy", height * 1.29)
                        .attr("text-anchor", "middle")
                        .text(numberFormat(parent.drillValList[i], round, precition))
                        .style("stroke", "white")
                        .style("stroke-width", 0)
                        .style("font-size", "12px")
                        .style("fill", "white")
                        .style("font-family: lucida grande");
            } else {
                group1.append("g:text")
                        .attr("dx", i * 5 * "20")
                        .attr("dy", height * 1.29)
                        .attr("text-anchor", "middle")
                        .text(autoFormating(parent.drillValList[i], autoRounding))
                        .style("stroke", "white")
                        .style("stroke-width", 0)
                        .style("font-size", "12px")
                        .style("fill", "white")
                        .style("font-family: lucida grande");
            }
            group1.append("g:text")
                    .attr("dx", i * 5 * "20")
                    .attr("dy", height * 1.44)
                    .attr("text-anchor", "middle")
                    .text(nameVal)
                    .style("stroke", "white")
                    .style("stroke-width", 0)
                    .style("font-size", "11px")
                    .style("fill", "black")
                    .style("font-family: lucida grande");

        }
    }
}



function showLegends(ageNames1, color, width, svg, height) {
    var divHtml = "";
    //  var divHtml="<table style='margin-left:20px;'><tr><td>";
    for (var k = 0; (k < ageNames1.length) && (k < 10); k++) {
        var legendColor = color(ageNames1[k]);
        if (typeof centralColorMap[ageNames1[k].toString().toLowerCase()] !== "undefined") {
            legendColor = centralColorMap[ageNames1[k].toString().toLowerCase()];
        } else {
            legendColor = color(k);
        }

        //divHtml+="<canvas id='myCanvas' width='12' height='12' style='margin-left:15px;background:"+legendColor+"' onmouseove=''></canvas><span onmouseove='' style='border:2px solid #357EC7;margin-left:20px;padding:5px 10px 5px 10px;border-radius:7px;color:#357EC7;font-weight:400'>"+ageNames1[k]+"</span>";
        divHtml += "<span id='index-" + ageNames1[k].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '') + "' style='border:2px solid " + legendColor + ";background:" + legendColor + ";white-space:nowrap;margin-left:20px;padding:1px 3px 1px 3px;border-radius:10px;color:#fff;font-weight:600'  onmouseover='synchronizedMouseOverLegends(this.id)'  onmouseout='synchronizedMouseOutLegends(this.id)'>" + ageNames1[k] + "</span>";
        //if(k!==0 && k%8===0){
        // divHtml+="</td></tr><tr><td style='padding-top:15px'>"
        //}
    }
    // divHtml+="</td></tr><table>"
    $("#legendchart2").html(divHtml);
    $("#legendchart2").show();

}
function showLegends2(ageNames1, color1, width, svg, height) {
    var divHtml = "";
    for (var k = 0; (k < ageNames1.length) && (k < 10); k++) {
        var legendColor = color1[k];
        divHtml += "<span id='index-" + ageNames1[k].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '') + "' style='border:2px solid " + legendColor + ";background:" + legendColor + ";white-space:nowrap;margin-left:20px;padding:1px 3px 1px 3px;border-radius:10px;color:#fff;font-weight:600'  onmouseover='synchronizedMouseOverLegends(this.id)'  onmouseout='synchronizedMouseOutLegends(this.id)'>" + ageNames1[k] + "</span>";
    }
    $("#legendchart2").html(divHtml);
    $("#legendchart2").show();

}
function showLegends1(ageNames1, colorMap, width, svg) {
    var divHtml = ""; //"<table style='margin-left:20px;text-align:left;align:left'><tr><td>";
    $("#legendchart2").attr("style", "margin-top:10px;");
    for (var k = 0; (k < ageNames1.length) && (k < 10); k++) {
        var legendColor = colorMap[ageNames1[k]];
        divHtml += "<span id='index-" + ageNames1[k].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '') + "' style='border:2px solid " + legendColor + ";background:" + legendColor + ";white-space: pre;  margin-left:20px;padding:1px 3px 1px 3px;border-radius:10px;color:#fff;font-weight:600'  onmouseover='synchronizedMouseOverLegends(this.id)'  onmouseout='synchronizedMouseOutLegends(this.id)'>" + ageNames1[k] + "</span>";
    }
    $("#legendchart2").html(divHtml);
    $("#legendchart2").show();

}

function builddrillPie() {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var tarPath = parent.$("#tarPath").val();
    var chartList = [];
    chartList.push("Pie");
    parent.$("#image").hide();
    parent.document.getElementById("mainSection").style.visibility = "visible";
    var charts = Object.keys(chartData);
    var width = $(window).width();
    var divHeight = $(window).height();
    var divContent = "";
    var widths = [];
    divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
    if (chartList.length === 2) {
        isDouble = true;
    }
    switch (charts.length) {
        case 1:
            {
                widths.push(width / 1.2);
                divHeight = $(window).height();
                divContent += "<div id='chart1' style='float:left;'></div>";
                break;
            }
        case 2:
            {
                isDouble = true;
                widths.push(width / 1.2);
                widths.push(width / 1.2);
                divHeight = $(window).height();
                divContent += "<div id='chart1' style='float:left;'></div></td></tr><tr><td><div id='chart2' style='float:left'></div>";
                break;
            }
        case 3 :
            {
                isDouble = false;
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                divHeight = $(window).height() / 1.5;
                divContent += "<div style='margin-left:13%'><div id='chart1' style='height:30%;float:left;'></div>" +
                        "<div id='chart2'style='float:left; width:50%;'></div><div id='chart3' style='float:left;width:50%;  '></div></div>";
                break;
            }
        case 4 :
            {
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                divHeight = $(window).height() / 1.5;
                divContent += "<div style='margin-left:13%'><div id='chart1' style='float:left;'></div><div id='chart2'style='float:left;'></div>" +
                        "<div id='chart3' style='float:left;margin-top: '></div><div id='chart4' style='float:left;margin-top:'></div></div>";
                break;
            }
        case 5 :
            {
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                widths.push(width / 1.5);
                divHeight = $(window).height() / 1.5;
                divContent += "<div style='margin-left:13%'><div id='chart1' style='float:left;'></div></td><td ><div id='chart2'style='float:left;'></div>" +
                        "<div id='chart3' style='float:left;margin-top: '></div></td><td><div id='chart4' style='float:left;margin-top:'></div><div id='chart5' style='float:left; margin-top:-7%'></div></div>";
                break;
            }
        default :
            {
                //            widths.push(width/1.5);
                //            widths.push(width/1.5);
                //            widths.push(width/1.5);
                //            widths.push(width/1.5);
                //            widths.push(width/1.5);
                //            widths.push(width/1.5);
                divHeight = $(window).height() / 1.5;
                divContent += "<div style='margin-left:13%'><div id='chart1' style='float:left;'></div></td><td ><div id='chart2'style='float:left;'></div>" +
                        "<div id='chart3' style='float:left;margin-top: '></div></td><td><div id='chart4' style='float:left;margin-top:'></div><div id='chart5' style='float:left; margin-top:-7% '></div><div id='chart6' style='float:left;margin-top:-7%' ></div></div>";
                break;
            }

    }

    $("body").html(divContent);
    var chartList = ["Pie", "Pie", "pie", "Pie"];
    var type = JSON.parse(parent.$("#chartList").val());
    var count = 0;
    for (var ch in charts) {
        widths.push(width / 1.5);
        if (charts.length > 6) {
            var diff = charts.length - 6;
            if (diff - 1 === ch || diff - 1 > ch) {
            }
            else {
                count++;
                var chartDetails = chartData[charts[ch]];
                var chartDetails1 = chartData[charts[0]];
                var chartType = chartList[ch];
                var dataPath;
                if (ch === 0) {
                    dataPath = tarPath;
                } else {
                    dataPath = parent.$("#reportName").val() + "/" + "chart" + ch + ".json";
                }
                var chartId = "chart" + (count);
                buildDashlet(chartDetails, ch, dataPath, widths[ch], divHeight, type, chartId, chartList);
            }
        }
        else {
            var chartDetails = chartData[charts[ch]];
            var chartDetails1 = chartData[charts[0]];
            var chartType = chartList[ch];
            var dataPath;
            if (ch === 0) {
                dataPath = tarPath;
            } else {
                dataPath = parent.$("#reportName").val() + "/" + "chart" + ch + ".json";
            }
            var chartId = "chart" + (parseFloat(ch) + 1);
            buildDashlet(chartDetails, ch, dataPath, widths[ch], divHeight, type, chartId, chartList);
        }

    }

}

function buildNetwork(data, columns, measureArray) {
    var links = [];
    var minList = [];
    var valMap = {};
    var tarList = [];
    for (var i = 0; i < data.length; i++) {
        var datamap = {};
        datamap["source"] = data[i][Object.keys(data[i])[0]];
        tarList.push(data[i][Object.keys(data[i])[0]]);
        datamap["target"] = data[i][Object.keys(data[i])[0]] + "*_" + data[i][Object.keys(data[i])[1]];
        //        datamap["targetVal"]=data[i][Object.keys(data[i])[0]]+"_"+data[i][Object.keys(data[i])[1]];
        datamap["value"] = data[i][measureArray[0]];
        valMap[data[i][Object.keys(data[i])[0]] + "*_" + data[i][Object.keys(data[i])[1]]] = data[i][measureArray[0]];
        minList.push(data[i][measureArray[0]]);
        links.push(datamap);
    }
    for (var i = 0; i < links.length; i++) {
        var prevVal = valMap[links[i][Object.keys(links[i])[0]]];
        if (typeof prevVal === "undefined") {
            valMap[links[i][Object.keys(links[i])[0]]] = links[i]["value"];
        }
        else {
            valMap[links[i][Object.keys(links[i])[0]]] = parseFloat(prevVal) + parseFloat(links[i]["value"]);
        }
    }
    var measureArray1 = [];
    var columns1 = [];
    columns1.push("source");
    measureArray1.push("value");

    var nodes = {};
    var strokWid1 = d3.scale.linear()
            .domain([Math.min.apply(Math, minList), Math.max.apply(Math, minList)])
            .range([3, 12]);
    //    var strokWid1 = d3.scale.linear()
    //    .domain([Math.min.apply(Math, minList),Math.max.apply(Math, minList)])
    //    .range([3,8]);
    links.forEach(function(link) {
        link.source = nodes[link.source] ||
                (nodes[link.source] = {
                    name: link.source
                });
        link.target = nodes[link.target] ||
                (nodes[link.target] = {
                    name: link.target
                });
        link.value = +link.value;
    });

    var width = $(window).width(),
            height = $(window).height();

    var force = d3.layout.force()
            .nodes(d3.values(nodes))
            .links(links)
            .size([width, height])
            .linkDistance(100)
            .charge(-140)
            .on("tick", tick)
            .start();
    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);

    // build the arrow.
    svg.append("svg:defs").selectAll("marker")
            .data(["end"])      // Different link/path types can be defined here
            .enter().append("svg:marker")    // This section adds in the arrows
            .attr("id", String)
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 15)
            .attr("refY", -1.5)
            .attr("markerWidth", 6)
            .attr("markerHeight", 6)
            .attr("orient", "auto")
            .append("svg:path")
            .attr("d", "M0,-5L10,0L0,5");
    // add the links and the arrows
    var path = svg.append("svg:g").selectAll("path")
            .data(force.links())
            .enter().append("svg:path")
            //    .attr("class", function(d) { return "link " + d.type; })
            .attr("class", "link")
            .attr("marker-end", "url(#end)");

    // define the nodes
    measureArray = [];
    columns = [];
    columns.push("name");
    measureArray.push("value");
    var node = svg.selectAll(".node")
            .data(force.nodes())
            .enter().append("g")
            .attr("class", "node")
            .attr("name", function(d) {
                if (tarList.indexOf(d.name) === -1)
                    return d.name + "_ch";
                else
                    return d.name + "_per";
            })
            .on("click", click)
            .on("dblclick", dblclick)
            .on("mouseover", function(d, i) {
                show_detailsN(d, columns, measureArray, this);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            })
            .call(force.drag);

    // add the nodes
    node.append("circle")
            .attr("r", function(d) {
                if (tarList.indexOf(d.name) === -1)
                    return strokWid1(valMap[d.name]);
                else
                    return 12;
            })
            .attr("fill", function(d) {
                if (tarList.indexOf(d.name) === -1)
                    return "balck";
                else
                    return "white";
            })
            .attr("id", function(d) {
                return strokWid1(valMap[d.name]);
            })
            .attr("name", function(d) {
                if (tarList.indexOf(d.name) === -1)
                    return d.name + "_ch";
                else
                    return d.name + "_per";
            })
            .attr("color_value", function(d, i) {
                if (tarList.indexOf(d.name) === -1)
                    return "balck";
                else
                    return "white";
            })
            .style("stroke", "black")
            .style("stroke-width", ".5px");

    // add the text
    node.append("text")
            .attr("x", 12)
            .attr("dy", ".35em")
            .text(function(d) {
                if (d.name.indexOf("*_") === -1)
                {
                    return d.name;
                }
                else {
                    var nameVal = (d.name).toString().split("*_");
                    return nameVal[1];
                }
            });

    // add the curvy lines
    function tick() {
        path.attr("d", function(d) {
            var dx = d.target.x - d.source.x,
                    dy = d.target.y - d.source.y,
                    dr = Math.sqrt(dx * dx + dy * dy);
            return "M" +
                    d.source.x + "," +
                    d.source.y + "A" +
                    dr + "," + dr + " 0 0,1 " +
                    d.target.x + "," +
                    d.target.y;
        });

        node
                .attr("transform", function(d) {
                    return "translate(" + d.x + "," + d.y + ")";
                });
    }
    function click() {
        d3.select(this).select("text").transition()
                .duration(750)
                .attr("x", 22)
                .style("fill", "steelblue")
                .style("stroke", "lightsteelblue")
                .style("stroke-width", ".5px")
                .style("font", "20px sans-serif");
        d3.select(this).select("circle").transition()
                .duration(750)
                //        .attr("r", 16)
                .style("fill", "lightsteelblue");
    }

    // action to take on mouse double click
    function dblclick() {
        d3.select(this).select("circle").transition()
                .duration(750)
                //        .attr("r", 6)
                .style("fill", this.color_value);
        d3.select(this).select("text").transition()
                .duration(750)
                .attr("x", 12)
                .style("stroke", "none")
                .style("fill", "black")
                .style("stroke", "none")
                .style("font", "10px sans-serif");
    }
    function show_detailsN(d, columns, measureArray, element) {
        d3.select(element).attr("stroke", "grey");
        var content = "";
        var title;
        title = columns;
        for (var i = 0; i < columns.length; i++) {
            var colName;
            if (typeof columnMap[columns[i]] !== "undefined" && columnMap[columns[i]]["displayName"] !== "undefined") {
                if (d[columns[i]].indexOf("*_") === -1)
                {
                    colName = d[columns[i]];
                }
                else {
                    var nameVal = (d[columns[i]]).toString().split("*_");
                    colName = nameVal[1];
                }
                content += "<span class=\"name\">" + columnMap[columns[i]]["displayName"] + ":</span><span class=\"value\"> " + colName + "</span><br/>";
            } else {
                if (d[columns[i]].indexOf("*_") === -1)
                {
                    colName = d[columns[i]];
                }
                else {
                    var nameVal = (d[columns[i]]).toString().split("*_");
                    colName = nameVal[1];
                }
                content += "<span class=\"name\">" + columns[i] + ":</span><span class=\"value\"> " + colName + "</span><br/>";
            }
        }
        for (var i = 0; i < measureArray.length; i++) {
            var msrData;
            if (isFormatedMeasure) {
                msrData = numberFormat(valMap[d[columns[i]]], round, precition);
            }
            else {
                msrData = addCommas(valMap[d[columns[i]]]);
            }
            if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            } else {
                content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            }
        }
        return tooltip.showTooltip(content, d3.event);
    }

}
function buildDendrogram(div,root, columns, measureArray) {
    var partition = d3.layout.partition()
            .value(function(d) {
                return d.size;
            });
    partition(root);
    var count = 0;
    var rootList = [];
    var rootValList = {};
    var rootValList1 = {};
    var rootMap = {};
    //var partition = d3.layout.partition()
    //    .value(function(d) {
    //        return d.size;
    //    });

    recurse(null, root);
    function recurse(name, node) {
        if (node.children) {
            if (typeof name !== "undefined" && name !== null) {
                var parent_category = name;
            }
            rootList.push(node.name);
            rootValList1[node.name] = node.length;
            var valList = [];
            var totVal = 0;
            node.children.forEach(function(child) {
                if (typeof rootMap[node.name] === "undefined") {
                    valList.push(child);
                    rootMap[node.name] = valList;
                }
                else {
                    valList = rootMap[node.name];
                    valList.push(child);
                    rootMap[node.name] = valList;
                }
                if (!child.children) {
                    totVal = parseFloat(totVal) + parseFloat(child["size"]);
                    rootValList[node.name] = totVal;
                }
                recurse(node.name, child);
            });
        }
        else {
            count++;
        }
    }
    var min1 = [];
    for (var key in rootValList) {
        min1.push(rootValList[key]);
    }
    var pointsize = d3.scale.linear()
            .domain([Math.min.apply(Math, min1), Math.max.apply(Math, min1)])
            .range([8, 20]);
    var margin = {
        top: 0,
        right: 20,
        bottom: 20,
        left: 20
    },
    width = $(window).width() - margin.right - margin.left,
            height = $(window).height() - margin.top - margin.bottom;

    var i = 0,
            duration = 750;
    //    root;

    var tree = d3.layout.tree()
            .size([height, width]);

    var diagonal = d3.svg.diagonal()
            .projection(function(d) {
                return [d.y, d.x];
            });

    var svg = d3.select("#"+div).append("svg")
            .attr("width", width + margin.right + margin.left)
            .attr("height", height + margin.top + margin.bottom)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    root.x0 = height / 2;
    root.y0 = 0;

    function collapse(d) {
        if (d.children) {
            d._children = d.children;
            d._children.forEach(collapse);
            d.children = null;
        }
    }

    root.children.forEach(collapse);
    update(root);

    d3.select(self.frameElement).style("height", "800px");

    function update(source) {
        var nodes = tree.nodes(root).reverse(),
                links = tree.links(nodes);
        nodes.forEach(function(d) {
            d.y = d.depth * 180;
        });
        var node = svg.selectAll("g.node")
                .data(nodes, function(d) {
                    return d.id || (d.id = ++i);
                });
        var nodeEnter = node.enter().append("g")
                .attr("class", "node")
                .attr("name", function(d) {
                    return d._children ? "parent" : "child";
                })
                .attr("transform", function(d) {
                    return "translate(" + source.y0 + "," + source.x0 + ")";
                })
                .on("mouseover", function(d, i) {
                    var content;
                    //            if(!d.name===""){
                    content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
                    //            }
                    //            else{
                    //                content = "";
                    //            }
                    //            var snook=0;
                    //            for(var i=0;i<rootList.length;i++){
                    //                if(rootList[i]===d.name){
                    //                    snook++;
                    //                }
                    //            }
                    //            var nodeName = d3.select(this);
                    //            var indexName = nodeName.attr("name");
                    //            if(d.name===""){}
                    //            else if(indexName==="child"){
                    //                content += "<span class=\"name\">"+measureArray[0]+":</span><span class=\"value\"> " + msrData + "</span><br/>";
                    //            }
                    //
                    //            else{
                    var msrData1;
                    if (isFormatedMeasure) {
                        msrData1 = numberFormat(d.value, round, precition);
                    }
                    else {
                        msrData1 = addCommas(d.value);
                    }
                    //                if(typeof rootValList[d.name]=== "undefined" || rootValList[d.name]=== "undefined"){
                    //                    content += "<span class=\"name\">Total :</span><span class=\"value\"> " + d.value + "</span><br/>";
                    //                }
                    //                else{
                    content += "<span class=\"name\">" + measureArray[0] + " :</span><span class=\"value\"> " + msrData1 + "</span><br/>";
                    //                }
                    //            }
                    return tooltip.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                })
                .on("click", click);

        nodeEnter.append("circle")
                .attr("r", 1e-6)
                .style("fill", function(d) {
                    return d._children ? "lightsteelblue" : "#fff";
                }).style("stroke", function(d) {
            return d._children ? "lightsteelblue" : "lightsteelblue";
        });
        ;

        nodeEnter.append("text")
                .attr("x", function(d) {
                    return d.children || d._children ? -10 : 10;
                })
                .attr("dy", ".35em")
                .attr("text-anchor", function(d) {
                    return d.children || d._children ? "end" : "start";
                })
                .text(function(d) {
                    return d.name;
                })
                .style("fill-opacity", 1e-6);
        var nodeUpdate = node.transition()
                .duration(duration)
                .attr("transform", function(d) {
                    return "translate(" + d.y + "," + d.x + ")";
                });

        nodeUpdate.select("circle")
                .attr("r", 4.5)
                .style("fill", function(d) {
                    return d._children ? "lightsteelblue" : "#fff";
                }).style("stroke", function(d) {
            return d._children ? "lightsteelblue" : "lightsteelblue";
        });

        nodeUpdate.select("text")
                .style("fill-opacity", 1);
        var nodeExit = node.exit().transition()
                .duration(duration)
                .attr("transform", function(d) {
                    return "translate(" + source.y + "," + source.x + ")";
                })
                .remove();

        nodeExit.select("circle")
                .attr("r", 1e-6);

        nodeExit.select("text")
                .style("fill-opacity", 1e-6);
        var link = svg.selectAll("path.link")
                .data(links, function(d) {
                    return d.target.id;
                });
        link.enter().insert("path", "g")
                .attr("class", "link")
                .attr("d", function(d) {
                    var o = {
                        x: source.x0,
                        y: source.y0
                    };
                    return diagonal({
                        source: o,
                        target: o
                    });
                });
        link.transition()
                .duration(duration)
                .attr("d", diagonal);
        link.exit().transition()
                .duration(duration)
                .attr("d", function(d) {
                    var o = {
                        x: source.x,
                        y: source.y
                    };
                    return diagonal({
                        source: o,
                        target: o
                    });
                })
                .remove();
        nodes.forEach(function(d) {
            d.x0 = d.x;
            d.y0 = d.y;
        });
    }
    function click(d) {
        if (d.children) {
            d._children = d.children;
            d.children = null;
        } else {
            d.children = d._children;
            d._children = null;
        }
        update(d);
    }
    function getAncestors1(node) {
        var path = [];
        var current = node;
        while (current.children) {
            path.unshift(current);
            current = current.children;
        }
        return path;
    }
}
function buildNetworkChart(data, columns, measureArray) {
    var width = $(window).width() - 50;
    var height = $(window).height();
    var color = d3.scale.category10();

    var radius = d3.scale.sqrt()
            .range([0, 6]);

    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);

    var force = d3.layout.force()
            .size([width + 150, height])
            .charge(-120)
            .linkDistance(function(d) {
                return radius(d.source.size) + radius(d.target.size) + 20;
            });
    var graph = [];
    graph = data;
    var data = graph;
    var mdata = [];
    var keys = [];
    var data2 = {};
    var minList = [];
    var singleList = [];

    var max;
    data.forEach(function(d, j) {
        if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10)
            keys.push(d[columns[0]]);
    });
    for (var j = 0; j < keys.length; j++) {
        data2 = {};
        var count = 0;
        data2[columns[0]] = keys[j];
        var countTot = 0;
        for (var i = 0; i < data.length; i++) {
            if (keys[j] === data[i][columns[0]]) {
                //data2[data[i][columns[1]]]=data[i][measureArray[0]];

                countTot = parseFloat(countTot) + parseFloat(data[i][measureArray[0]]);
                count++;
                minList.push(data[i][measureArray[0]]);
                if (i === data.length - 1) {
                    data2[measureArray[0]] = countTot;
                }
                if (i === 0) {
                    max = data[i][measureArray[0]];
                } else {
                    if (max < parseFloat(data[i][measureArray[0]])) {
                        max = data[i][measureArray[0]];
                    }
                }
            }
        }
        //singleList[keys[j]]=countTot;
        mdata.push(data2);
    }
    for (var i = 0; i < mdata.length; i++) {
        graph.push(mdata[i]);
    }
    var strokWid = d3.scale.linear()
            .domain([Math.min.apply(Math, minList), Math.max.apply(Math, minList)])
            .range([.5, 4]);
    force
            .nodes(graph)
            .size([width, height])
            .on("tick", tick)
            .start();


    var node = svg.selectAll(".node")
            .data(graph)
            .enter().append("g")
            .attr("class", function(d) {
                if (Object.keys(d).indexOf(columns[1]) !== -1) {
                    return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                } else {
                    return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";
                }
            })
            .attr("id", function(d) {
                if (Object.keys(d).indexOf(columns[1]) !== -1) {
                    return d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                } else {
                    return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";
                }
            });
    //      .call(force.drag);

    node.append("circle")
            .attr("r", function(d) {
                if (Object.keys(d).indexOf(columns[1]) !== -1) {
                    return "6";
                } else {
                    return "20";
                }
            })
            .style("fill", function(d) {
                return color(d[columns[0]]);
            });
    var count = 0;
    var delay = 3000;
    for (var sec = 0; sec < 10000; sec++)
        ;
    setTimeout(function() {
        for (i in data) {
            var keyVal = Object.keys(data[i]);
            var ids = (data[i][keyVal[1]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + (data[i][keyVal[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var ids1 = (data[i][keyVal[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";

            if (document.getElementById(ids).getAttribute("transform") !== null) {
                var startpt = document.getElementById(ids).getAttribute("transform");
                var endpt = document.getElementById(ids1).getAttribute("transform");
                var startArr = startpt.replace("translate(", "", "gi").replace(")", "", "gi").toString().split(",");
                var endtArr = endpt.replace("translate(", "", "gi").replace(")", "", "gi").toString().split(",");
                var x1 = startArr[0];
                var y1 = startArr[1];
                var x2 = endtArr[0];
                var y2 = endtArr[1];
                svg.append("svg:line")
                        .attr("x1", x1)
                        .attr("y1", y1)
                        .attr("x2", x2)
                        .attr("y2", y2)
                        .transition()
                        .duration(2000)
                        .style("stroke", "rgb(6,120,155)")
                        .style("stroke-width", strokWid(data[i][measureArray[0]]));
            }
        }
    }, delay);

    function tick() {
        node.attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        })
                .on("mouseover", function(d) {
                    if (Object.keys(d).indexOf(columns[1]) !== -1) {
                        show_detailstip(d, columns, measureArray, this);
                    } else {
                        var colarr = [];
                        colarr.push(columns[0]);
                        show_details(d, colarr, measureArray, this);
                    }
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    }
    //});

    function show_detailstip(d, columns, measureArray, element) {
        d3.select(element).attr("stroke", "grey");
        var content = "";
        var title;
        var value = measureArray[0];
        title = columns;
        for (var i = 0; i < columns.length; i++) {
            if (typeof columnMap[columns[i]] !== "undefined" && columnMap[columns[i]]["displayName"] !== "undefined") {
                content += "<span class=\"name\">" + columnMap[columns[i]]["displayName"] + ":</span><span class=\"value\"> " + d[columns[i]] + "</span><br/>";
            } else {
                content += "<span class=\"name\">" + columns[i] + ":</span><span class=\"value\"> " + d[columns[i]] + "</span><br/>";
            }
        }
        for (var i = 0; i < measureArray.length; i++) {

            var msrData;
            if (isFormatedMeasure) {
                msrData = numberFormat(valMap[d[columns[i]]], round, precition);
            }
            else {
                msrData = addCommas(valMap[d[columns[i]]]);
            }
            if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            } else {
                content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
            }
        }
        return tooltip.showTooltip(content, d3.event);
    }
}

function buildKPIBar(div,dataMap,columns,measures1,dataId,colIds,measureIds) {
    $("#content_2").html("");
    $("#content_1").hide();//hh
    var measures = measures1.toString().split(",");
    var wid = $(window).width() - 200;
    var hgt = $(window).height() + (($(window).height() * 30) / 100);
    var svg;
    var svg1;
    var chartData = JSON.parse($("#chartData").val());
    var chartViews = Object.keys(chartData);
    var chartColumn = [];

    chartColumn.push(chartData[dataId]["viewBys"][0]);
var data = dataMap[dataId];
    var margin = {
        top: 20,
        right: 00,
        bottom: 150,
        left: 120
    };
    //
          var groupBy = [];
var dimensions = chartData[dataId]["dimensions"];
  var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var groupIds = [];
    for(var s=0;s<dimensions.length;s++){
        groupBy.push(viewOvName[viewOvIds.indexOf(dimensions[s])]);
    }
    var groupByNew = columns;//JSON.parse(parent.$("#groupbys").val());

//        if (typeof parent.$("#orderViewBy").val() !== "undefined" && parent.$("#orderViewBy").val() !== "")
//            groupBy = JSON.parse(parent.$("#orderViewBy").val());
//        else
//    groupBy = columns;


    var divContent = "";
    divContent += "<div id='viewList' class=\"tablediv1\" width='100%' style='margin-left: 0%; text-align: left;margin-left:10%' height=''>";
//    divContent += "<table><tr height='20px';>";
    divContent += "<table>";
    divContent += "<tr>";
    divContent += "<td><strong class='' style='font-size:15px;padding-right:10px;color:#BEA30C;'>Parameters</strong></td>";
    divContent += "</tr>";

    for (var p = 0; p < groupBy.length; p++)
    {
        if (chartColumn[0] === groupBy[p]) {
//            divContent += "<td class='mapViewBy1' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</td>";
            divContent += "<tr height='20px';><td><a class='mapViewBy1' href='#foo' style='color:#fff' ><span height='20px' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</span></a></td></tr>";
        }
        else {
            divContent += "<tr height='20px';><td><a class='mapViewBys' href='#foo' style='color:#fff' ><span height='20px' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</span></a></td></tr>";
//            divContent += "<td class='mapViewBy' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</td>";
        }
    }
//    divContent += "</tr></table>";
    divContent += "</table>";
    divContent += "</div>";
    $("#kpiRegion").css("height", "30px");
    $("#kpiRegion").css("style", "border-bottom:1.5px solid #ffffff");
//    $("#"+div).append(divContent);
    $("#content_2").append(divContent);
//    $("#content_2").show("drop",500);
    var divs = "<div id='chartAd1' style='width:86%;float:left;'></div><div id='MeasureKPI' style='display:block;width:25%;margin-top:2%;float:left'></div>";
$("#"+div).append(divs);

//    var measuresNew = JSON.parse(parent.$("#measure").val());
//    var measureListNew = measuresNew.toString().split(",");
//    var measures = [];
//        if (typeof parent.$("#orderMeasure").val() !== "undefined" && parent.$("#orderMeasure").val() !== "")
//            measures = JSON.parse(parent.$("#orderMeasure").val());
//        else
    var measureListNew = measures;


    //        var measures=JSON.parse(parent.$("#orderMeasure").val());
    var measureList = measures.toString().split(",");
    var measureDiv = "";//<table><tr>";
   var measureListNew = measures;


    //        var measures=JSON.parse(parent.$("#orderMeasure").val());
    var measureList = measures.toString().split(",");
    var measureDiv = "";//<table><tr>";
    measureDiv += "<div id='measureList' class=\"measureDiv\" width='100%' style='margin-left: 0%; text-align: left;margin-left:10%;margin-top:15px' height=''>";
    measureDiv += "<table>";
    measureDiv += "<tr>";
    measureDiv += "<td><strong class='' style='font-size:15px;padding-right:10px;color:#BEA30C;'>Measures</strong></td>";
    measureDiv += "</tr>";
    for (var i = 0; i < measures.length; i++) {
        var autoRounding;
//            if (columnMap[measureList[i]] !== "undefined" && columnMap[measureList[i]] !== undefined && columnMap[measureList[i]]["rounding"] !== "undefined") {
//                autoRounding = columnMap[measureList[i]]["rounding"];
//            } else {
        autoRounding = "1d";
//            }

        var drillFunction;
//        var aggTypes = JSON.parse(parent.$("#aggregations").val());
//        if (aggTypes[i] === "COUNT_DISTINCT") {
//            drillFunction = "";
//        }
//        else {
            drillFunction = "changeMeasureArray(this.id)";
//        }
        var c = measures[i];
//        if (i === 0) {
//            measureDiv += "<table style=''><tr>";
//        }
//        if (i % 3 === 0) {
//            measureDiv += "</tr><tr>";
//        }
       var displayName ="";


        displayName = measures[i];
         if (measures[0] === measures[i]) {
//            divContent += "<td class='mapViewBy1' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</td>";
            measureDiv += "<tr height='20px';><td><a class='mapViewBy1'  title='" + displayName + "' ><span class='mview' height='20px' id='" + i + "' onclick='" + drillFunction + "'>" + displayName + "</span></a></td></tr>";
        }
        else {
            measureDiv += "<tr height='20px';><td><a class='mapViewBys'  title='" + displayName + "' ><span class='mview' height='20px' id='" + i + "' onclick='" + drillFunction + "'>" + displayName + "</span></a></td></tr>";
//            divContent += "<td class='mapViewBy' onclick='changeGroup(\"" + chartViews[p] + "\")'>" + groupBy[p] + "</td>";
        }

//        measureDiv += "<td style='padding: 9px 9px 9px 9px;'><table><tr valign='bottom'><td class='mViewBy'>" + displayName + "</td></tr>";
//        if (measures[0] === measures[i]) {
//                measureDiv += "<tr><td class='measureclass'><a class='measViewBy1' title='" + displayName + "' href='#foo' ><span height='30px' class='mview' id='" + i + "' onclick='" + drillFunction + "'>" +addCommas(data[0][c]) + "</span></a>";
//        }
//        else {
//                measureDiv += "<tr><td class='measureclass'><a class='measViewBy' title='" + displayName + "' href='#foo' ><span height='30px' class='mview' id='" + i + "' onclick='" + drillFunction + "'>" + addCommas(data[0][c]) + "</span></a>";
//        }
//        measureDiv += "</td></tr></table></td>";
//        if (i === measures.length - 1) {
//            measureDiv += "</tr></table>";
//        }

 }
  measureDiv += "</table>";
    measureDiv += "</div>";
    $("#content_2").append(measureDiv);
    $("#content_2").show("drop",500);
    getChart(data,columns,measures);
    function getChart(data,columns,measures) {
        var groupb = columns;
        var view = [];
        var divId = "chartAd1";
        view.push(groupb[0]);
        var measureArray = [];
        var measure = measures;
        for (var i = 0; i < measure.length; i++) {
            measureArray.push(measure[i]);
        }
        var data2;
        var mindata = [];
        var noOfRecords = 15;
        if(typeof chartData[dataId]["records"]!=='undefined')
        {
            noOfRecords=chartData[dataId]["records"];
        }

        for (var i = 0; i < data.length; i++) {
            if (i < noOfRecords) {
                mindata.push(data[i]);
            }   else {
                break;
            }
        }
        if(typeof chartData[dataId]["records"]!=='undefined' && chartData[dataId]["records"]=='All'){
            mindata = data;
        }
        data2 = mindata;
        var margin = {
            top: 20,
            right: 00,
            bottom: 50,
            left: 100
        },
        width = wid - 50 - margin.left - margin.right,
                height = hgt - 10 - margin.top - margin.bottom;
            if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "KPI-Bar"){
            buildBarAdvance("chart1",divId, data2, chartColumn, measures, wid, height);
            }
            else if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "Advance-Pie") {
                buildAdvancePie("chart1",divId, data2, chartColumn, measures, width, height * .55);
            }
            else if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "Bar-Dashboard") {
//
//      added by shivam
//                    var width = $(window).width;
//        var height = $(window).height;
//alert(screen.width)
if (screen.width > 1000)
    {
        height=height*.55;
            }
    else {
        height=height*.70;
    }
//alert(height)
                buildBarAdvance2("chart1",divId, data2, chartColumn, measures, width, height);
            }
            else if(JSON.parse($("#visualChartType").val())[$("#currType").val()] == "Trend-Dashboard") {
                $("#viewMeasureBlock").hide();
                var hgtRatio=0;
                if(measureArray.length<=6){
                    hgtRatio=0.55;
                }
                else{
                    hgtRatio=0.50;
                }
                buildAdvanceLine("chart1",divId, data2, chartColumn, measures, width, height * hgtRatio);
            }else {
                  buildAdvanceHorizontal("chart1",divId, data2, chartColumn, measures, width, height * .75);
            }

$("#MeasureKPI").attr("style", "display:block")
    }
    if($("#chartType").val()!='Trend-Dashboard'){
   measureButton(dataMap);
}
}


function buildNetworkChart1() {

    var count = 0;
    var rootList = [];
    var rootValList = {};
    var rootMap = {};
    var width = $(window).width();
    var height = $(window).height(),
            root;

    var force = d3.layout.force().linkDistance(50)
            .size([width, height])
            .on("tick", tick);

    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);

    var link = svg.selectAll(".link"),
            node = svg.selectAll(".node");
    //parent.$("#tarPath").val
    d3.json(parent.$("#tarPath").val(), function(json) {
        root = json;
        update();
    });

    function update() {

        recurse(null, root);
        function recurse(name, node) {
            if (node.children) {
                if (typeof name !== "undefined" && name !== null) {
                    var parent_category = name;
                }
                rootList.push(node.name);
                var valList = [];
                var totVal = 0;
                node.children.forEach(function(child) {
                    if (typeof rootMap[node.name] === "undefined") {
                        valList.push(child);
                        rootMap[node.name] = valList;
                    }
                    else {
                        valList = rootMap[node.name];
                        valList.push(child);
                        rootMap[node.name] = valList;
                    }
                    if (!child.children) {
                        totVal = parseFloat(totVal) + parseFloat(child["size"]);
                        rootValList[node.name] = totVal;
                    }
                    recurse(node.name, child);
                });
            }
            else {
                count++;
            }
        }
        var min_amount = d3.min(rootValList, function(d) {
            return parseFloat(d);
        });
        var min1 = [];
        for (var key in rootValList) {
            min1.push(rootValList[key]);
        }
        var pointsize = d3.scale.linear()
                .domain([Math.min.apply(Math, min1), Math.max.apply(Math, min1)])
                .range([8, 20]);
        var distancesize = d3.scale.linear()
                .domain([Math.min.apply(Math, min1), Math.max.apply(Math, min1)])
                .range([90, 20]);
        var nodes = flatten(root),
                links = d3.layout.tree().links(nodes);
        //force.linkDistance(function(d) {
        //  return d.weight * 2;
        //});
        // Restart the force layout.
        force
                .nodes(nodes)
                .links(links).linkDistance(function(d, j) {
            var snook = 0;
            for (var i = 0; i < rootList.length; i++) {
                if (rootList[i] === d.source.name) {
                    snook++;
                }
            }

            if (snook === 0) {
                return  90;
            } else if (d.source.name === "") {
                return   90;
            }
            else {
                return 90;//pointsize(rootValList[d.name]);
            }//return 90;
        }).gravity(.01)
                .charge(-50).alpha(0.01)
                .size([width, height])
                .start();

        // Update the linksÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¾ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¾ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¦
        link = link.data(links, function(d) {
            return d.target.id;
        });

        // Exit any old links.
        link.exit().remove();

        // Enter any new links.
        link.enter().insert("line", ".node")
                .attr("class", "link")
                .attr("x1", function(d) {
                    return d.source.x;
                })
                .attr("y1", function(d) {
                    return d.source.y;
                })
                .attr("x2", function(d) {
                    return d.target.x;
                })
                .attr("y2", function(d) {
                    return d.target.y;
                });

        node = node.data(nodes, function(d) {
            return d.id;
        }).style("fill", color);

        // Exit any old nodes.
        node.exit().remove();

        // Enter any new nodes.

        node.enter().append("circle")
                .attr("class", "node")
                .attr("cx", function(d) {
                    return d.x;
                })
                .attr("cy", function(d) {
                    return d.y;
                })
                .attr("r", function(d) {
                    var snook = 0;
                    for (var i = 0; i < rootList.length; i++) {
                        if (rootList[i] === d.name) {
                            snook++;
                        }
                    }

                    if (snook === 0) {
                        return  4.5;
                    } else if (d.name === "") {
                        return "";
                    }
                    else {
                        return pointsize(rootValList[d.name]);
                    }
                })
                .on("mouseover", function(d, i) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.sizes, round, precition);
                    }
                    else {
                        msrData = addCommas(d.size);
                    }
                    var content;
                    if (!d.name === "") {
                        content = "<span class=\"name\"></span><span class=\"value\"> " + d.name + "</span><br/>";
                    }
                    else {
                        content = "";
                    }
                    var snook = 0;
                    for (var i = 0; i < rootList.length; i++) {
                        if (rootList[i] === d.name) {
                            snook++;
                        }
                    }
                    if (snook === 0) {
                        content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                    }
                    else if (d.name === "") {
                    }
                    else {
                        var msrData1;
                        if (isFormatedMeasure) {
                            msrData1 = numberFormat((rootValList[d.name]), round, precition);
                        }
                        else {
                            msrData1 = addCommas(rootValList[d.name]);
                        }
                        content += "<span class=\"name\">Total :</span><span class=\"value\"> " + msrData1 + "</span><br/>";
                    }
                    return tooltip.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                })

                .style("fill", color)
                .on("click", click)
                .call(force.drag);

    }

    function tick() {
        link.attr("x1", function(d) {
            return d.source.x;
        })
                .attr("y1", function(d) {
                    return d.source.y;
                })
                .attr("x2", function(d) {
                    return d.target.x;
                })
                .attr("y2", function(d) {
                    return d.target.y;
                });

        node.attr("cx", function(d) {
            return d.x;
        })
                .attr("cy", function(d) {
                    return d.y;
                });
    }
    var color1 = d3.scale.category10();
    function color(d) {
        return d._children ? "green" : d.children ? "green" : color1(d.name);
    }

    // Toggle children on click.
    function click(d) {
        if (!d3.event.defaultPrevented) {
            if (d.children) {
                d._children = d.children;
                d.children = null;
            } else {
                d.children = d._children;
                d._children = null;
            }
            update();
        }
    }

    // Returns a list of all nodes under the root.
    function flatten(root) {
        var nodes = [], i = 0;

        function recurse(node) {
            if (node.children)
                node.children.forEach(recurse);
            if (!node.id)
                node.id = ++i;
            nodes.push(node);
        }

        recurse(root);
        return nodes;
    }
}

function buildHubspoke1(data, columns, measureArray) {
    var width = $(window).width() - 50;
    var height = $(window).height();
    var color = d3.scale.category10();

    var radius = d3.scale.sqrt()
            .range([0, 6]);
    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);



    d3.json(parent.$("#tarPath").val(), function(graph) {
        var data = graph;
        var mdata = [];
        var keys = [];
        var data2 = {};
        var minList = [];
        var minMap = {};
        var max;
        data.forEach(function(d, j) {
            if (j < 40) {
                if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10)
                    keys.push(d[columns[0]]);
            }
        });
        var ctData = {};
        var count = 0;
        for (var j = 0; j < keys.length; j++) {
            data2 = {};
            //            var count=0;
            data2[columns[0]] = keys[j];
            var countTot = 0;
            for (var i = 0; i < data.length; i++) {
                //                count++;
                //                if(count<30){
                if (keys[j] === data[i][columns[0]]) {
                    countTot = parseFloat(countTot) + parseFloat(data[i][measureArray[0]]);
                    count++;
                    minList.push(data[i][measureArray[0]]);
                    if (i === data.length - 1) {
                        // data2[measureArray[0]]=countTot;
                    }
                    if (i === 0) {
                        max = data[i][measureArray[0]];
                    } else {
                        if (max < parseFloat(data[i][measureArray[0]])) {
                            max = data[i][measureArray[0]];
                        }
                    }
                    //                }
                }
                minMap[keys[j]] = countTot;
            }
            mdata.push(data2);
        }
        for (var i = 0; i < mdata.length; i++) {
            graph.push(mdata[i]);
        }
        var gravity;
        if (graph.length < 30) {
            gravity = 0.04;
        }
        else if (graph.length < 70) {
            gravity = 0.1;
        }
        else if (graph.length < 160) {
            gravity = 0.3;
        }
        else if (graph.length < 200) {
            gravity = 0.5;
        }
        else {
            gravity = 0.2;
        }
        var force = d3.layout.force()
                .size([width + 150, height])
                .charge(-120).gravity(gravity).alpha(-5)
                .linkDistance(function(d) {
                    return radius(d.source.size) + radius(d.target.size) + 20;
                });
        var strokWid = d3.scale.linear()
                .domain([Math.min.apply(Math, minList), Math.max.apply(Math, minList)])
                .range([.5, 4]);
        force
                .nodes(graph).alpha(-5)
                .size([width, height])
                .on("tick", tick)
                .start();


        var node = svg.selectAll(".node")
                .data(graph)
                .enter().append("g")
                .attr("class", function(d) {
                    if (Object.keys(d).indexOf(columns[1]) !== -1) {
                        return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    } else {
                        return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";
                    }
                })
                .attr("id", function(d) {
                    if (Object.keys(d).indexOf(columns[1]) !== -1) {
                        return d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    } else {
                        return d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";
                    }
                });
        //      .call(force.drag);

        node.append("circle")
                .attr("r", function(d) {
                    if (Object.keys(d).indexOf(columns[1]) !== -1) {
                        return "6";
                    } else {
                        return "20";
                    }
                })

                .style("fill", function(d) {
                    return color(d[columns[0]]);
                });
        var count = 0;
        var delay = 4000;
        for (var sec = 0; sec < 10000; sec++)
            ;
        setTimeout(function() {

            //your code to be executed after 4 seconds

            for (i in data) {
                var keyVal = Object.keys(data[i]);
                var ids = (data[i][keyVal[1]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_" + (data[i][keyVal[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
                var ids1 = (data[i][keyVal[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_par";

                if (document.getElementById(ids).getAttribute("transform") !== null) {
                    var startpt = document.getElementById(ids).getAttribute("transform");
                    var endpt = document.getElementById(ids1).getAttribute("transform");
                    var startArr = startpt.replace("translate(", "", "gi").replace(")", "", "gi").toString().split(",");
                    var endtArr = endpt.replace("translate(", "", "gi").replace(")", "", "gi").toString().split(",");
                    var x1 = startArr[0];
                    var y1 = startArr[1];
                    var x2 = endtArr[0];
                    var y2 = endtArr[1];
                    svg.append("svg:line")
                            .attr("x1", x1)
                            .attr("y1", y1)
                            .attr("x2", x2)
                            .attr("y2", y2)
                            .transition()
                            .duration(2000)
                            .style("stroke", "rgb(6,120,155)")
                            .style("stroke-width", strokWid(data[i][measureArray[0]]));
                }
            }
        }, delay);
        //}
        //  node.append("text")
        //      .attr("dy", ".35em")
        //      .attr("text-anchor", "middle")
        //      .text(function(d) { return d[columns[0]]; });

        function tick() {
            node.attr("transform", function(d) {
                return "translate(" + d.x + "," + d.y + ")";
            })
                    .on("mouseover", function(d) {
                        if (Object.keys(d).indexOf(columns[1]) !== -1) {
                            show_details(d, columns, measureArray, this);
                        } else {
                            var colarr = [];
                            colarr.push(columns[0]);
                            //              d3.select(element).attr("stroke", "grey");
                            var content = "";
                            var title;
                            title = colarr;
                            for (var i = 0; i < colarr.length; i++) {
                                if (typeof columnMap[colarr[i]] !== "undefined" && columnMap[colarr[i]]["displayName"] !== "undefined") {
                                    content += "<span class=\"name\">" + columnMap[colarr[i]]["displayName"] + ":</span><span class=\"value\"> " + d[colarr[i]] + "</span><br/>";
                                } else {
                                    content += "<span class=\"name\">" + colarr[i] + ":</span><span class=\"value\"> " + d[colarr[i]] + "</span><br/>";
                                }
                            }
                            for (var i = 0; i < 1; i++) {
                                var msrData;
                                if (isFormatedMeasure) {
                                    msrData = numberFormat(minMap[d[colarr[i]]], round, precition);
                                }
                                else {
                                    msrData = addCommas(minMap[d[colarr[i]]]);
                                }
                                if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                                    content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                                } else {
                                    content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                                }
                            }
                            return tooltip.showTooltip(content, d3.event);
                            //            show_details(d,colarr,measureArray,this)
                        }
                    })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    });
        }
    });
}

function getOs() {
    var osty;
    $.ajax({
        type: 'POST',
        async: false,
        url: parent.nodeListner + 'getOSType',
        success: function(response) {
            osty = response;
        },
        error: function(jqXHR, textStatus, errorThrown) {
        }
    });
    return osty;
}
function buildHubspoke(data, columns, measureArray) {
    var width = $(window).width() - 50;
    var height = $(window).height();
    var color = d3.scale.category10();
    var radius = d3.scale.sqrt().range([0, 6]);
    var divhtml = "<div id='chartdiv' style='width:60%;margin-left:0px;margin-top:0px;float:left'></div><div id='datadiv' ></div>";
    $("body").append(divhtml);
    var svg = d3.select("#chartdiv").append("svg")
            .attr("width", width)
            .attr("height", height);
    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientSource")
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
            .attr("stop-color", "orange")
            .attr("stop-opacity", 1);

    var gradient = svg.append("svg:defs")
            .append("svg:radialGradient")
            .attr("id", "gradientDestination")
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
            .attr("stop-color", "#92cd00")
            .attr("stop-opacity", 1);

    d3.select("body").append("div").attr("id", "locdiv").style("float", "left");
//    d3.json(parent.$("#tarPath").val(), function(graph) {
//        var data = graph;
    var keys = [];
    var data2 = {};
    var minList = [];
    var minMap = {};
    var max;
    data.forEach(function(d, j) {
        if (j < 40) {
            if (keys.indexOf(d[columns[0]]) === -1 && keys.length < 10)
                keys.push(d[columns[0]]);
        }
    });

    var fromMap = {};
    var toMap = {};
    var fromData = [];
    var toData = [];
    var newList = data;
    if (columns.length !== 1) {
        for (var k = 0; k < data.length; k++) {
            var currdata = newList[k];
            if (typeof fromMap[currdata[columns[0]]] === "undefined" && currdata[columns[0]] !== "undefined" && typeof currdata[measureArray[0]] !== "undefined" && currdata[measureArray[0]] !== 'undefined') {
                var valIn1 = [];
                valIn1.push(currdata[measureArray[0]]);
                fromMap[currdata[columns[0]]] = valIn1;
            }
            else if (typeof currdata[columns[0]] !== "undefined" && currdata[columns[0]] !== "undefined" && typeof currdata[measureArray[0]] !== "undefined" && currdata[measureArray[0]] !== 'undefined') {
                var valIn = [];
                var prevVal = fromMap[currdata[columns[0]]];
                var neVal = parseFloat(currdata[measureArray[0]]) + parseFloat(prevVal);
                valIn.push(neVal);
                fromMap[currdata[columns[0]]] = valIn;
            }
        }
        for (var i = 0; i < data.length; i++) {
            var currdata = newList[i];
            if (typeof toMap[currdata[columns[1]]] === "undefined" && currdata[columns[1]] !== "undefined" && typeof currdata[measureArray[0]] !== "undefined" && currdata[measureArray[0]] !== 'undefined') {
                var valIn1 = [];
                valIn1.push(currdata[measureArray[0]]);
                toMap[currdata[columns[1]]] = valIn1;
            }
            else if (typeof currdata[columns[1]] !== "undefined" && currdata[columns[1]] !== "undefined" && typeof currdata[measureArray[0]] !== "undefined" && currdata[measureArray[0]] !== 'undefined') {
                var valIn = [];
                var prevVal = toMap[currdata[columns[1]]];
                var neVal = parseFloat(currdata[measureArray[0]]) + parseFloat(prevVal);
                valIn.push(neVal);
                toMap[currdata[columns[1]]] = valIn;
            }
        }
    }
    fromData.push(fromMap);
    toData.push(toMap);
    minList = [];
    var objKey = Object.keys(fromMap);
    for (var j = 0; j < objKey.length; j++) {
        minList.push(fromMap[objKey[j]]);
    }
    var minList1 = [];
    var objKey1 = Object.keys(toMap);
    for (var j = 0; j < objKey1.length; j++) {
        minList1.push(toMap[objKey1[j]]);
    }

    var prevMap1 = {};
    var count = 0;
    var count1 = 0;
    var minranege;
    var maxranege;
    var minranege1;
    var maxranege1;
    var node = svg.selectAll(".node");
    var mulFact;
    var mulFact1;
    if (objKey.length < 7) {
        mulFact = 70;
        minranege = 15;
        maxranege = 25;
    }
    else if (objKey.length < 12) {
        mulFact = 45;
        minranege = 9;
        maxranege = 18;
    }
    else if (objKey.length < 18) {
        mulFact = 25;
        minranege = 5;
        maxranege = 15;
    }
    else {
        mulFact = 25;
        minranege = 5;
        maxranege = 15;
    }
    if (objKey1.length < 7) {
        mulFact1 = 70;
        minranege1 = 15;
        maxranege1 = 25;
    }
    else if (objKey1.length < 12) {
        mulFact1 = 45;
        minranege1 = 9;
        maxranege1 = 18;
    }
    else if (objKey1.length < 18) {
        mulFact1 = 25;
        minranege1 = 5;
        maxranege1 = 15;
    }
    else {
        mulFact1 = 15;
        minranege1 = 5;
        maxranege1 = 5;
    }

    var strokWid = d3.scale.linear()
            .domain([Math.min.apply(Math, minList), Math.max.apply(Math, minList)])
            .range([minranege, maxranege]);
    var strokWid1 = d3.scale.linear()
            .domain([Math.min.apply(Math, minList1), Math.max.apply(Math, minList1)])
            .range([minranege1, maxranege1]);
    for (var i = 0; i < objKey.length; i++) {
        if (i === 20) {
            break;
        }

        count++;
        var yVal = count * mulFact;
        group1 = svg.append("svg:g")
                .attr("stroke", "white")
                .attr("stroke-width", 3);
        circle1 = group1.append("svg:circle")
                .attr("r", strokWid(fromMap[objKey[i]])).attr("cx", 100).attr("cy", yVal)
                .attr("class", (objKey[i] + "_parent").replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, ''))
                .attr("name", objKey[i])
                .style("fill", "url(#gradientSource)").on("mouseover", function(d) {//#cccc99
            var bar = d3.select(this);
            var nameValue = bar.attr("name");
            var name, val1 = 0;
            for (var l = 0; l < objKey.length; l++) {
                if (objKey[l] === nameValue) {
                    name = objKey[l];
                    if (typeof objKey[l] !== "undefined") {
                        val1 = fromMap[objKey[l]];
                    }
                }
            }
            var colarr = [];
            colarr.push(columns[0]);
            var content = "";
            for (var i = 0; i < colarr.length; i++) {
                if (typeof columnMap[colarr[i]] !== "undefined" && columnMap[colarr[i]]["displayName"] !== "undefined") {
                    content += "<span class=\"name\">" + columnMap[colarr[i]]["displayName"] + ":</span><span class=\"value\"> " + name + "</span><br/>";
                } else {
                    content += "<span class=\"name\">" + colarr[i] + ":</span><span class=\"value\"> " + data[i][colarr[i]] + "</span><br/>";
                }
            }
            for (var i = 0; i < 1; i++) {
                var msrData;
                if (isFormatedMeasure) {
                    msrData = numberFormat(val1, round, precition);
                }
                else {
                    msrData = addCommas(val1);
                }
                if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                    content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                } else {
                    content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                }
            }

            var lineclass = d3.selectAll("." + name);
            d3.selectAll("." + name.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')).style("stroke", "darkblue");
            var htmlstr = "<table table class=\"tBody\" align= 'right' width='30%' >";
            var tabcount = 0;
            //                htmlstr+=colarr[0]
            for (var i = 0; i < data.length; i++) {
                if (data[i][columns[0]] === name) {
                    if (tabcount === 0) {
                        htmlstr += "<tr class=\"thClass\" align='center'><td align='center'>" + data[i][columns[0]] + "&nbsp(" + columns[1] + ")-</td><td align='center'>" + measureArray[0] + "</td>";
                    }
                    tabcount++;
                    if (tabcount % 2 === 1) {
                        htmlstr += "<tr class=\"tdClass1\"><td align='left'>" + data[i][columns[1]] + "</td>";
                    }
                    else {
                        htmlstr += "<tr class=\"tdClass2\"><td align='left'>" + data[i][columns[1]] + "</td>";
                    }
                    var toData;
                    if (isFormatedMeasure) {
                        toData = numberFormat(data[i][measureArray[0]], round, precition);
                    }
                    else {
                        toData = addCommas(data[i][measureArray[0]]);
                    }
                    htmlstr += "<td align='right'>" + toData + "</td></tr>";
                }
            }
            htmlstr += "</table>";
            $("#datadiv").html(htmlstr);
            return tooltip.showTooltip(content, d3.event);
        })
                .on("mouseout", function(d, i) {
                    var arcText = d3.select(this).attr("name");
                    d3.selectAll("." + arcText.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')).style("stroke", "lightgrey");
                    hide_details(d, i, this);
                });
    }
    for (var i = 0; i < data.length; i++) {
        if (typeof prevMap1[[data[i][columns[1]]]] === "undefined") {
            if (count1 === 35) {
                break;
            }
            count1++;
            var yVal1 = count1 * mulFact1;
            prevMap1[[data[i][columns[1]]]] = [data[i][columns[1]]];
            group1 = svg.append("svg:g")
                    .attr("stroke", "white")
                    .attr("stroke-width", 3);
            circle1 = group1.append("svg:circle")
                    .attr("r", strokWid1(toMap[[data[i][columns[1]]]])).attr("cx", 480).attr("cy", yVal1)
                    .attr("class", ([data[i][columns[1]]] + "_child").replace(/[^a-zA-Z0-9]/g, '', 'gi'))
                    .attr("name", [data[i][columns[1]]])
                    .style("fill", "url(#gradientDestination)")
                    .on("mouseover", function(d) {
                        var bar = d3.select(this);
                        var nameValue = bar.attr("name");
                        var name, val1 = 0;
                        for (var l = 0; l < data.length; l++) {
                            if (data[l][columns[1]] === nameValue) {
                                name = data[l][columns[1]];
                                if (typeof data[l][measureArray[0]] !== "undefined") {
                                    val1 = parseFloat(val1) + parseFloat(data[l][measureArray[0]]);
                                }
                            }
                        }
                        var colarr = [];
                        colarr.push(columns[0]);
                        colarr.push(columns[1]);
                        var content = "";
                        var title;
                        title = colarr;
                        //    for(var i=0;i<colarr.length;i++){
                        if (typeof columnMap[colarr[i]] !== "undefined" && columnMap[colarr[1]]["displayName"] !== "undefined") {
                            content += "<span class=\"name\">" + columnMap[colarr[1]]["displayName"] + ":</span><span class=\"value\"> " + name + "</span><br/>";
                        } else {
                            content += "<span class=\"name\">" + colarr[1] + ":</span><span class=\"value\"> " + name + "</span><br/>";
                        }
                        //    }
                        for (var i = 0; i < 1; i++) {

                            var msrData;
                            if (isFormatedMeasure) {
                                msrData = numberFormat(val1, round, precition);
                            }
                            else {
                                msrData = addCommas(val1);
                            }
                            if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                                content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            } else {
                                content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                        }
                        var htmlstr = "<table table class=\"tBody\" align= 'right' width='30%' >";
                        var tabcount = 0;
                        for (var i = 0; i < data.length; i++) {
                            if (data[i][columns[1]] === name) {
                                if (tabcount === 0) {
                                    htmlstr += "<tr class=\"thClass\" align='center'><td align='center'>" + data[i][columns[1]] + "(" + columns[0] + ")-</td><td align='center'>" + measureArray[0] + " </td>";
                                }
                                tabcount++;
                                if (tabcount % 2 === 1)
                                    htmlstr += "<tr class=\"tdClass1\"><td>" + data[i][columns[0]] + "</td>";
                                else
                                    htmlstr += "<tr class=\"tdClass2\"><td>" + data[i][columns[0]] + "</td>";
                                var toData;
                                if (isFormatedMeasure) {
                                    toData = numberFormat(data[i][measureArray[0]], round, precition);
                                }
                                else {
                                    toData = addCommas(data[i][measureArray[0]]);
                                }
                                htmlstr += "<td align='right'>" + toData + "</td></tr>";
                            }
                        }
                        htmlstr += "</table>";
                        $("#datadiv").html(htmlstr);
                        return tooltip.showTooltip(content, d3.event);
                    })
                    .on("mouseout", function(d, i) {
                        hide_details(data[i], i, this);
                    });
        }
    }
    var count = 0;
    for (var sec = 0; sec < 10000; sec++)
        ;
    for (var k = 0; k < data.length; k++) {
        var ids = (data[k][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')) + "_parent";
        var ids1 = (data[k][columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')) + "_child";
        var x1 = svg.selectAll("." + ids).attr("cx");
        var y1 = svg.selectAll("." + ids).attr("cy");
        if (svg.selectAll("." + ids1) !== "") {
            var x2 = svg.selectAll("." + ids1).attr("cx");
            var y2 = svg.selectAll("." + ids1).attr("cy");
            var line = svg.append("svg:line")
                    .attr("x1", x1)
                    .attr("y1", y1)
                    .attr("x2", x2)
                    .attr("y2", y2)
                    .attr("class", data[k][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, ''))
                    .style("stroke", "lightgrey")
                    .style("stroke-width", 1);
        }
    }
    function tick() {
        node.attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        })
                .on("mouseover", function(d) {
                    if (Object.keys(d).indexOf(columns[1]) !== -1) {
                        show_details(d, columns, measureArray, this);
                    } else {
                        var colarr = [];
                        colarr.push(columns[0]);
                        var content = "";
                        var title;
                        var value = measureArray[0];
                        title = colarr;
                        for (var i = 0; i < colarr.length; i++) {
                            if (typeof columnMap[colarr[i]] !== "undefined" && columnMap[colarr[i]]["displayName"] !== "undefined") {
                                content += "<span class=\"name\">" + columnMap[colarr[i]]["displayName"] + ":</span><span class=\"value\"> " + d[colarr[i]] + "</span><br/>";
                            } else {
                                content += "<span class=\"name\">" + colarr[i] + ":</span><span class=\"value\"> " + d[colarr[i]] + "</span><br/>";
                            }
                        }
                        for (var i = 0; i < 1; i++) {

                            var msrData;
                            if (isFormatedMeasure) {
                                msrData = numberFormat(minMap[d[colarr[i]]], round, precition);
                            }
                            else {
                                msrData = addCommas(minMap[d[colarr[i]]]);
                            }
                            if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
                                content += "<span class=\"name\">" + columnMap[measureArray[i]]["displayName"] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            } else {
                                content += "<span class=\"name\">" + measureArray[i] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                            }
                        }
                        var htmlstr = "";
                        htmlstr += colarr[0];
                        $("#locdiv").html(htmlstr);
                        return tooltip.showTooltip(content, d3.event);
                    }
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    }
}
function mouseHoverEvent(content) {
    var svgContent = d3.selectAll("." + content);
    d3.selectAll("." + content).attr("fill", drillShade);
}
function mouseOutEvent(content) {
    var svgContent = d3.selectAll("." + content);
    d3.selectAll("." + content).attr("fill", drillShade);
}



function buildWorldMapBubble(data, columns, measureArray) {
    //   var tooltip = CustomTooltip("my_tooltip", "auto")
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
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));
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
            //              else if(typeof data[j][measureArray[0]]!=="undefined"){
            //                  content += "<span class=\"name\">"+measureArray[0]+": </span><span class=\"value\">  --</span><br/>";
            //              }
            //              return tooltip.showTooltip(content, d3.event);
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
    var width = $(window).width();
    var height = $(window).height();

    //    var color = d3.scale.category10();
    var projection = d3.geo.mercator()
            .center([-20, 60])
            .scale(150);
    var path = d3.geo.path()
            .projection(projection);

    var graticule = d3.geo.graticule();

    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);
    var g = svg.append("g");


    //var objKeys = Object.keys(data);
    d3.json("json/world-population.geo.json", function(error, topology) {
        var min1 = [];
        for (var key in data) {
            min1.push(data[key][measureArray[0]]);
        }
        //        var minRange;
        var repName = parent.$("#reportName").val();
        var colorApp = d3.scale.linear()
                .domain(
                        [0, Math.max.apply(Math, min1)]
                        //    [d3.min(data, function(d) {
                        //        return (d[measure]);
                        //    }),d3.max(data, function(d) {
                        //        return d[measure];
                        //    })]
                        )
                .range(["#d8f781", "#74df00"]);
        var pointsize = d3.scale.linear()
                .domain([0, Math.max.apply(Math, min1)])
                .range([3, 9]);
        g.selectAll("path")
                .data(topojson.feature(topology, topology.objects.countries).features)
                .enter()
                .append("path")
                .attr("d", path)// for(var i=0;i<Object.keys(data).length;i++){if(if)}
                .style("fill", function(d, i) {
                    var count = 0;
                    //            for(var j=0;j<data.length;j++){
                    //                if(data[j][columns[0]].toLowerCase()===(d.properties.name).toLowerCase()){
                    //                    return color(d.properties.name);
                    //                }
                    //            }
                    return "white";
                }).style("stroke", "rgb(100,100,100)").on("mouseover", function(d, i) {
            show_details(d);
        })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
        g.selectAll("text")
                .data(topojson.feature(topology, topology.objects.countries).features)
                .enter()
                .append('svg:circle')
                .attr("cx", function(d) {
                    return path.centroid(d)[0];
                })
                .attr("cy", function(d) {
                    return  path.centroid(d)[1];
                })
                .attr('r', function(d, i) {
                    for (var j = 0; j < data.length; j++) {
                        if (d.properties.name !== 'Norway') {
                            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                return pointsize(data[j][measureArray[0]]);
                            }
                            else if (isShortForm(d, data[j][columns[0]])) {
                                return pointsize(data[j][measureArray[0]]);
                            }
                        }
                    }
                    return "0";
                }).attr("fill", function(d, i) {
            for (var j = 0; j < data.length; j++) {
                if (d.properties.name !== 'Norway') {
                    if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                        if (isShadedColor) {
                            return color(data[j][shadingMeasure]);
                            //                    }else if(conditionalShading){
                            //                        return getConditionalColor(colorApp(data[j][conditionalMeasure]),data[j][conditionalMeasure]);
                        } else {
                            return colorApp(data[j][measureArray[0]]);
                        }
                    }
                    else if (isShortForm(d, data[j][columns[0]])) {
                        if (isShadedColor) {
                            return color(data[j][shadingMeasure]);
                            //                    }else if(conditionalShading){
                            //                        return getConditionalColor(colorApp(data[j][conditionalMeasure]),data[j][conditionalMeasure]);
                        } else {
                            return colorApp(data[j][measureArray[0]]);
                        }
                    }
                }
            }
            return "white";
        }).style("stroke", function(d, i) {
            for (var j = 0; j < data.length; j++) {
                if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    return  "rgb(100,100,100)";
                }
                else if (isShortForm(d, data[j][columns[0]])) {
                    return  "rgb(100,100,100)";
                }
            }
            return "white";
        }).on("mouseover", function(d, i) {
            show_details(d);
        })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                }).attr("id", function(d, i) {
            for (var j = 0; j < data.length; j++) {
                if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    return  data[j][columns[0]];
                }
                else if (isShortForm(d, data[j][columns[0]])) {
                    return  data[j][columns[0]];
                }
            }
            return "";
        })
                .attr("onclick", "mapPopUp(this.id,\"" + repName + "\")");
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
function mapPopUpDiv(id, name) {
    if (id !== "false") {
        mapPopUp(id, name);
    }
}

function buildWorldMapCityBubble(data, columns, measureArray) {
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
    parent.$("#maxMap").val(JSON.stringify(maxMap));
    parent.$("#minMap").val(JSON.stringify(minMap));
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
    var width = $(window).width();
    var height = $(window).height();

    var projection = d3.geo.mercator()
            .center([-20, 60])
            .scale(150);
    var path = d3.geo.path()
            .projection(projection);
    var svg = d3.select("body").append("svg")
            .attr("width", width)
            .attr("height", height);
    var g = svg.append("g");
    d3.json("json/world-population.geo.json", function(error, topology) {
        g.selectAll("path")
                .data(topojson.feature(topology, topology.objects.countries).features)
                .enter()
                .append("path")
                .attr("d", path)
                .style("fill", function(d, i) {
                    return "white";
                }).style("stroke", "rgb(100,100,100)").on("mouseover", function(d, i) {
            show_details(d);
        })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
        d3.json("json/coordinate.json", function(data1) {
            var path1 = d3.geo.path().projection(projection).pointRadius(function(d) {
                return d.coordinates / 50;
            });
            g.selectAll("path.place")
                    .data(data1)
                    .enter().append("path")
                    .attr("d", path1.pointRadius(1.2))
                    .attr("class", function(d) {
                        for (var j = 0; j < data.length; j++) {
                            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                return "place_" + d.properties.name;
                            }
                        }
                        return "place1";
                    })
                    .attr("fill", function(d) {
                        for (var j = 0; j < data.length; j++) {
                            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                return "lightgrey";
                            }
                        }
                    })
                    .on("mouseover", function(d) {
                        for (var j = 0; j < data.length; j++) {
                            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                                show_details(d);
                            }
                        }
                    }).on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
        });
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

function buildImageCloud(data, columns, measureArray) {
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id)";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var max_amount;
    var fill = d3.scale.category10();
    //    data=data.sort(function(a, b) {
    //                        return a[measureArray[0]] - b[measureArray[0]];
    //                    });
    max_amount = d3.max(data, function(d) {
        return parseFloat(d[measureArray[0]]);
    });
    var min_amount = d3.min(data, function(d) {
        return parseFloat(d[measureArray[0]]);
    });
    var wid = $(window).width() - 50;
    var hgt = $(window).height() - 50;
    this.radius_scale = d3.scale.pow().exponent(0.5).domain([min_amount, max_amount]).range([30, 100]);
    d3.layout.cloud().size([wid, hgt])
            .words(data.map(function(d) {

                var map = {};
                map["text"] = d[columns[0]];
                map["size"] = this.radius_scale(parseFloat(d[measureArray[0]]));
                for (var no = 0; no < measureArray.length; no++) {
                    map["value" + no] = d[measureArray[no]];
                }
                return map;
            }))
            .padding(5)
            .rotate(function() {
                return 0;
            })
            .font("Impact")
            .fontSize(function(d) {
                return d.size;
            })
            .on("end", draw)
            .start();

    function draw(words) {
        var svg = d3.select("body").append("svg")
                .attr("width", wid)
                .attr("height", hgt)
                .attr("style", "margin-top:0px")
                .append("g")
                .attr("transform", "translate(" + wid / 2 + "," + hgt / 2 + ")");
//        svg.append("svg:rect")
//                .attr("width", wid)
//                .attr("height", hgt)
//                .attr("onclick", "reset()")
//                .attr("class", "background");
        for (var i = 0; i < words.length; i++) {
            var wordWidth, wordHeight;
            wordWidth = (words[i].size) * 2.3;
            wordHeight = wordWidth / 2;
            svg.append("svg:image")
                    .attr('x', -9)
                    .attr('y', -12)
                    .attr('width', wordWidth)
                    .attr('height', wordHeight)
                    .attr('index_value', i)
                    .attr("xlink:href", function(d) {
                        //                if(i%2===0)
                        //            return "logos/bear.jpg";
                        //        else if(i%3===0)
                        //            return "logos/jr.jpg";
                        //        else
                        return "logos/" + words[i].text + ".jpg";
                    }).attr("alt", words[i].text)
                    .attr("transform", function(d) {
                        return "translate(" + [words[i].x, words[i].y] + ")rotate(" + words[i].rotate + ")";
                    }).attr("id", function(d) {
                return words[i].text + ":" + words[i].value;
            }).on("mouseover", function(d) {
                var circle = d3.select(this);
                var currWidth = parseFloat(circle.attr("width")) + 50;
                var currHeight = parseFloat(circle.attr("height")) + 50;
                var i = circle.attr("index_value");
                //             circle.attr("width","190")
                //            .attr("width","190").style("opacity", 1).style("z-index", -1).style("position", "absolute")
                var content = "";
                var msrData;
                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + words[i].text + "</span><br/>";
                for (var no = 0; no < measureArray.length; no++) {
                    if (isFormatedMeasure) {
                        msrData = numberFormat(words[i].value, round, precition);
                    }
                    else {
                        msrData = addCommas(words[i]["value" + no]);
                    }
                    content += "<span class=\"name\">" + measureArray[no] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                }
                return tooltip.showTooltip(content, d3.event);
            }).on("mouseout", function(d) {
                //            var circle = d3.select(this);
                //            circle.transition()
                //            .style("font-size", function(d) {
                //                return d.size + "px";
                //            })
                hide_details(d, i, this);
            }).attr("onclick", fun);
            //            .append("svg:title")
            //              .text(function(d){
            //                return "Root Name:";
            //            })
            //        .on("mouseover", function(d, i) {
            //            var circle = d3.select(this);
            //            circle.transition()
            //            .duration(100).style("opacity", 1).style("z-index", -1).style("position", "absolute")
            //            .style("font-size", function(d) {
            //                return d.size + 20 + "px";
            //            })
            //        });
        }
        //        svg.selectAll("text").append("text")
        //            .attr('width', 20)
        //            .attr('height', 24)

        //        svg.selectAll("text")
        //        .data(words)
        //        .enter().append("text")
        //            .attr('width', 20)
        //            .attr('height', 24)
        //        .attr("xlink:href", function(d) {
        //            return "Test.png";
        //        })
        //        .style("font-size", function(d) {
        //            return d.size - 2 + "px";
        //        })
        //        .style("font-family", "Impact")
        //        .style("fill", function(d, i) {
        //            return fill(i);
        //        })
        //        .attr("text-anchor", "middle")
        //        .attr("id", function(d) {
        //            return d.text + ":" + d.value;
        //        })
        //
        //        .attr("onclick", fun)
        //        .on("mouseover", function(d, i) {
        //            var circle = d3.select(this);
        //            circle.transition()
        //            .duration(100).style("opacity", 1).style("z-index", -1).style("position", "absolute")
        //            .style("font-size", function(d) {
        //                return d.size + 20 + "px";
        //            })
        //            var content = "";
        //            var msrData;
        //            content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.text + "</span><br/>";
        //            for (var no = 0; no < measureArray.length; no++) {
        //                if (isFormatedMeasure) {
        //                    msrData = numberFormat(d["value" + no], round, precition)
        //                }
        //                else {
        //                    msrData = addCommas(d["value" + no]);
        //                }
        //                content += "<span class=\"name\">" + measureArray[no] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
        //            }
        //            return tooltip.showTooltip(content, d3.event);
        //        })
        //        .on("mouseout", function(d) {
        //            var circle = d3.select(this);
        //            circle.transition()
        //            .style("font-size", function(d) {
        //                return d.size + "px";
        //            })
        //            hide_details(d, i, this);
        //        })
        //        .attr("transform", function(d) {
        //            return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
        //        })
        //        .text(function(d) {
        //            return d.text;
        //        });
    }
}







function setMouseOverEvent(id,div) {
//alert(id)
d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div)
.style("fill", drillShade)
 .attr("stroke", " ");
//    v
//    ar chartType = JSON.parse(parent.$("#chartList").val());
//    if (chartType[0] === 'Split-Graph') {
        synchronizedMouseOverSplit(id);
//    }
}
function setMouseOutEvent(id,div) {
    try {
        var chartColorValue = d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div).attr("color_value");
    } catch (e) {
    }
    try {
        d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div).style("fill", chartColorValue);
    } catch (e) {
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
//
//function buildmultiMeasureDashboard() {
//
//    var chartData = JSON.parse(parent.$("#chartData").val());
//    var chartDetails = {};
//    chartDetails = chartData["chart1"];
//    var columns = chartDetails.viewBys;
//    var measureArray = chartDetails.meassures;
//    var divContent = "";
//    divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
//    divContent += "<div id='chart1' style='float:left;'></div>";
//    $("body").html(divContent);
//    var repData = JSON.parse(parent.reportData);
//    var displayData = repData["chart1"];
//    var dataTable = "";
//    dataTable += "<table style='width:100%'>";
//    var width = $(window).width() * 2 / measureArray.length;
//    var minMaxMap = {};
//    for (var i = 0; i < displayData.length; i++) {
//        for (var k = 0; k < measureArray.length; k++) {
//            var max = maximumValue(displayData, measureArray[k]);
//            var min = minimumValue(displayData, measureArray[k]);
//            var temp = {};
//            temp["min"] = min;
//            temp["max"] = max;
//            minMaxMap[measureArray[k]] = temp;
//        }
//    }
//    for (var i = 0; i < displayData.length; i++) {
//
//        dataTable += "<tr><td width='100px'>" + displayData[i][columns[0]] + "</td>";
//        for (var k = 0; k < measureArray.length; k++) {
//            var scale = d3.scale.linear().domain([minMaxMap[measureArray[k]]["min"], minMaxMap[measureArray[k]]["max"]]).range(["98%", "1%"]);
////    dataTable +="<td>"+displayData[i][measureArray[k]]+"</td>";
//                    dataTable += "<td><canvas id='myCanvas' width='" + scale(displayData[i][measureArray[k]]) + "' height='18' style='margin-left:5px;margin-right:5px;background:lightgreen' onmouseove=''></canvas></td>";
//        }
//        dataTable += "</tr>";
//
//    }
//    dataTable += "</table>";
//    $("#chart1").html(dataTable);
//    var canvasHtml = "<canvas id='myCanvas' width='12' height='12' style='margin-left:5px;margin-right:5px;background:lightgreen' onmouseove=''></canvas>";
//    var html = "<table>";
//    html += "<tr><td>" + canvasHtml + "</td></tr>";
//    html += "</table>";
//}
var colVal;
function buildmultiMeasureDashboard(div,repData,columnsView) {
//    var columnsView = JSON.parse(parent.$("#viewby").val());
var color = d3.scale.category12();
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = {};
    var chartViews = Object.keys(chartData);
    chartDetails = chartData[chartViews[0]];
    var columns = chartDetails.viewBys;
    var measureArray = chartDetails.meassures;
    var divContent = "";
    divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
    divContent += "<div id='chartAd1' style='float:left;'></div>";
    $("#"+div).html(divContent);
    var width = $(window).width();
//    var repData = JSON.parse(parent.reportData);
    var displayData = repData["chart1"];
    if(typeof OPtionVar!="undefined"){
        displayData=repData[OPtionVar];
    }
    var dataTable = "";

    dataTable += "<table>";
    dataTable += "<tr><td><div><select name='operators' id='' onchange='changeGroup(this.value)'>";
    for (var j = 0; j < chartViews.length; j++) {
        if(columnsView[j]!=null){
        if(typeof colVal!="undefined" && columnsView[j]==colVal){
            dataTable += "<option value='" + chartViews[j] + "' selected>" + columnsView[j] + "</option>";
        }
//        if (columnsView[0] === chartViews[j]) {
//            dataTable += "<option value='" + chartViews[j] + "' selected>" + columnsView[j] + "</option>";
         else {
            dataTable += "<option value='" + chartViews[j] + "'>" + columnsView[j] + "</option>";
        }
    }
    }
    var columnInd = columns[0];
    if(typeof colVal!="undefined"){
        columnInd=colVal;
    }
    var minMaxMap = {};
    for (var k = 0; k < measureArray.length; k++) {
        dataTable += "<td align='left'><span style='font-size:12px;font-weight:bold;cursor:pointer;' onclick='changeMeasureArray(\""+k+"\")'>" + measureArray[k] + "</span></td>";
        var max = maximumValue(displayData, measureArray[k]);
        var min = minimumValue(displayData, measureArray[k]);
        var temp = {};
        temp["min"] = min;
        temp["max"] = max;
        minMaxMap[measureArray[k]] = temp;
    }
    dataTable += "<tr ><td" +
//          " colSpan='"+(measureArray.length+1)+"' style='border-top:1px #ccc solid'"+
            " height='10px'></td></tr>";
    var wid = parseFloat(width) * (80 / 100) - 140;
    var tdwid = wid / (measureArray.length);
    for (var i = 0; i < displayData.length; i++) {

        dataTable += "<tr style='text-align:left'><td style='width:100px'><span style='font-size:10px;font-weight:bold'>" + displayData[i][columnInd] + "</span></td>";
        for (var k = 0; k < measureArray.length; k++) {
            var scale = d3.scale.linear().domain([minMaxMap[measureArray[k]]["max"], minMaxMap[measureArray[k]]["min"]]).range([tdwid, "1%"]);
//    dataTable +="<td>"+displayData[i][measureArray[k]]+"</td>";
            var wid = scale(displayData[i][measureArray[k]]);
            if (parseFloat(wid.replace("%", "")) < 1) {
                wid = 1+"%";
            }
            if (parseFloat(wid.replace("%", "")) > 100) {
                wid = 100+"%";
            }
            dataTable += "<td><canvas id='" + displayData[i][columnInd] + "_" + measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[k] + ":" + addCurrencyType(div, getMeasureId(measureArray[k])) + addCommas(displayData[i][measureArray[k]]) + "' width='" + wid + "' height='18' style='margin-left:5px;margin-right:5px;background:" + color(k) + "' onmouseove=''></canvas><span style='font-size:10px;float:'>" + addCurrencyType(div, getMeasureId(measureArray[k])) + addCommas(displayData[i][measureArray[k]]) + "</span></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
        }
        dataTable += "</tr>";

    }
    dataTable += "</table>";
    $("#chartAd1").html(dataTable);
//    for (var i = 0; i < displayData.length; i++) {
//        for (var k = 0; k < measureArray.length; k++) {
//            var c = $("#" + displayData[i][columnInd] + "_" + measureArray[k].replace(/[^a-zA-Z0-9]/g, '', 'gi'));
//            var ctx = c.getContext("2d");
//            ctx.font = "30px Arial";
//            ctx.fillText(autoFormating(displayData[i][measureArray[k]]), 0, 0);
//        }
//    }
//    var canvasHtml = "<canvas id='myCanvas' width='12' height='12' style='margin-left:5px;margin-right:5px;background:-webkit-gradient(linear, left top, left bottom,from(#ccc), to(#000))' onmouseove=''></canvas>";
//    var html="<table>";
//    html+="<tr><td>"+canvasHtml+"</td></tr>";
//    html+="</table>";q


}

function changeGroup(id) {
      var chartData = JSON.parse($("#chartData").val());
   var measures=[];
   OPtionVar=id;
   colVal=chartData[id]["viewBys"][0];
                        var measuresIds=[];
                        var columns = [];
                        var columnsIds = [];
       for(var ch in chartData){
                        measures=[];
                        measuresIds=[];
                        columns.push(chartData[ch]["viewBys"][0]);
                        columnsIds.push(chartData[ch]["viewIds"][0]);
                        measures.push(chartData[ch]["meassures"]);
                        measuresIds.push(chartData[ch]["meassureIds"]);
                    }
                    $("#Hchart1").html('');
                    var chartType = JSON.parse($("#visualChartType").val())[$("#currType").val()];

                    if(chartType !=="KPI-Bar" && chartType !=="Advance-Pie" && chartType!=="Advance-Horizontal" && chartType!=="Trend-Dashboard" && chartType!=="Bar-Dashboard"){
                    buildmultiMeasureDashboard("Hchart1",measureData,columns)
                }else {
                    buildKPIBar("Hchart1",measureData,columns,measures,id)
                }
}
//function buildUSMap(div,data,columns,measureArray) {
////    div="vis";
////    $("body").style.width = "38%";
////    $("body").css("display", "block");
////    $("body").css("margin-top", "-54%");
////    if (screen.width == 1024 && screen.height == 768) {
////        $("body").css("margin-top", "-71%");
////    }
////    $("body").css("float", "right");
////    d3.select("body").append("div").attr("id", "chartDiv").attr("width", "100%").attr("height", "50%");
////    var sum = d3.sum(data, function(d) {
////        return d[measureArray[0]];
////    });
////    parent.$("#sumValue").val(sum);
////    buildPie("chartDiv", data, columns, measureArray, $(window).width() * .39, $(window).height() * .49, Math.min($(window).width() * .39));
////    d3.select("#legendchart2").append("div")
////            .attr("id", "tableDiv")
////            .attr("width", "100%")
////            .attr("height", "50%")
////            .style("margin-right", "4%")
////            .style("overflow", "auto");
//    var htmlstr = "<table class=\"tBody\"  width='100%' >";
////    for(var ii in data){
//
//    htmlstr += "<tr class=\"thClass\" align='center'>";
//    for (var j in columns) {
//        htmlstr += "<td>" + columns[j] + "</td>"
//    }
//    for (var j in measureArray) {
//        htmlstr += "<td>" + measureArray[j] + "</td>"
//    }
//    htmlstr += "</tr>";
//    var kpicount = 0;
//    var avgData = [];
//    for (var j in data) {
//        avgData.push(data[j]);
//        kpicount++;
//        if (kpicount % 2 == 1) {
//            htmlstr += "<tr class=\"tdClass1\">";
//            for (var k in columns) {
//                htmlstr += "<td class='tdClass' align='left'>" + data[j][columns[k]] + "</td>";
//                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
//                    var measVal = parseFloat(data[j][measureArray[n]]).toFixed(2);
//                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//                }
//            }
//            htmlstr += "</tr>";
//        }
//        else {
//            htmlstr += "<tr class=\"tdClass2\">";
//            for (var k in columns) {
//                htmlstr += "<td class='tdClass' align='left'>" + data[j][columns[k]] + "</td>";
//                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
//                    var measVal = parseFloat(data[j][measureArray[n]]).toFixed(2);
//                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//                }
//            }
//            htmlstr += "</tr>";
//        }
//
//    }
//
////    }
//    htmlstr += "</table>";
//    $("#tableDiv").html(htmlstr);
//    width = $(window).width();
//    height = $(window).height();
//    active = d3.select(null);
////var projection =  d3.geo.mercator().scale(900)
////            .translate([width / 2, height / 2.3]);
//    var projection = d3.geo.albersUsa()
//            .scale(900)
//            .translate([width / 2, height / 2.3]);
//    var projection1 = d3.geo.albersUsa()
////    .center([98, 23.5])
//            .scale(900);
//
//    var path1 = d3.geo.path()
//            .projection(projection1);
//    if (screen.width == 1024 && screen.height == 768) {
//        projection = d3.geo.albersUsa()
//                .scale(650)
//                .translate([width / 2, height / 3]);
//    }
//
//    var path = d3.geo.path()
//            .projection(projection);
//
//    var svg = d3.select("#" + div).append("svg")
//            .attr("width", width)
//            .attr("height", height);
//
////    svg.append("rect")
////            .attr("class", "background")
////            .attr("width", width)
////            .attr("height", height)
////            .on("click", reset);
//
//    var g = svg.append("g")
//            .style("stroke-width", "1.5px");
//    d3.json("JS/us-States.json", function(error, us) {
//        g.selectAll("path")
//                .data(us.features)
//                .enter().append("path")
//                .attr("d", path)
//                .attr("fill", function(d, i) {
//                    var colorShad = "white";
//                    for (var k = 0; k < data.length; k++) {
//                        if (d.properties.name.toLowerCase() === data[k][columns[0]].toLowerCase()) {
//                            if (isShadedColor) {
//                                colorShad = color(data[k][shadingMeasure]);
//                            } else if (conditionalShading) {
////                    return getConditionalColor("steelblue", d[conditionalMeasure]);
//                    return getConditionalColor(color(i), data[k][conditionalMeasure]);
//                            }
//                            else {
//                                colorShad = color(i);
//                            }
//                        }
//                    }
//                    return colorShad;
//                })
//                .attr("stroke", "#000")
//                .on("click", clicked)
//                .on("mouseover", function(d, i) {
//                    for (var k = 0; k < data.length; k++) {
//                        if (d.properties.name.toLowerCase() === data[k][columns[0]].toLowerCase()) {
//                            show_details(data[k], columns, measureArray, this);
//                        }
//                    }
//                })
//                .on("mouseout", function(d, i) {
//                    hide_details(d, i, this);
//                });
//    });
//
//
//    function clicked(d) {
//        if (active.node() === this)
//            return reset();
//        active.classed("active", false);
//        active = d3.select(this).classed("active", true);
//
//        var bounds = path.bounds(d),
//                dx = bounds[1][0] - bounds[0][0],
//                dy = bounds[1][1] - bounds[0][1],
//                x = (bounds[0][0] + bounds[1][0]) / 2,
//                y = (bounds[0][1] + bounds[1][1]) / 2,
//                scale = .9 / Math.max(dx / width, dy / height),
//                translate = [width / 2 - scale * x, height / 2 - scale * y];
//
//        g.transition()
//                .duration(750)
//                .style("stroke-width", 1.5 / scale + "px")
//                .attr("transform", "translate(" + translate + ")scale(" + scale + ")");
//    }
//
//    function reset() {
//        active.classed("active", false);
//        active = d3.select(null);
//
//        g.transition()
//                .duration(750)
//                .style("stroke-width", "1.5px")
//                .attr("transform", "");
//    }
//}
//function buildUSMapCity(div, data, columns, measureArray) {
////    $("#" + div).css("width", "100%");
//    var sum = d3.sum(data, function(d) {
//        return d[measureArray[0]];
//    });
//    var min, max;
//    min = minimumValue(data, measureArray[0]);
//    max = maximumValue(data, measureArray[0]);
//    var pointsize = d3.scale.linear()
//            .domain([min, max])
//            .range([4, 10]);
//    parent.$("#sumValue").val(sum);
//    buildPieInDB1("chartDiv", data, columns, measureArray, $(window).width() * .39, $(window).height() * .49, Math.min($(window).width() * .39));
////    d3.select("#legendchart2").append("div")
////            .attr("id", "tableDiv")
////            .attr("width", "100%")
////            .attr("height", "50%")
////            .style("margin-right", "4%")
////            .style("overflow", "auto");
//    var htmlstr = "<table class=\"tBody\"  width='100%' >";
////    for(var ii in data){
//
//    htmlstr += "<tr class=\"thClass\" align='center'>";
//    for (var j in columns) {
//        htmlstr += "<td>" + columns[j] + "</td>"
//    }
//    for (var j in measureArray) {
//        htmlstr += "<td>" + measureArray[j] + "</td>"
//    }
//    htmlstr += "</tr>";
//    var kpicount = 0;
//    var avgData = [];
//    for (var j in data) {
//        avgData.push(data[j]);
//        kpicount++;
//        if (kpicount % 2 == 1) {
//            htmlstr += "<tr class=\"tdClass1\">";
//            for (var k in columns) {
//                htmlstr += "<td class='tdClass' align='left'>" + data[j][columns[k]] + "</td>";
//                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
//                    var measVal = parseFloat(data[j][measureArray[n]]).toFixed(2);
//                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//                }
//            }
//            htmlstr += "</tr>";
//        }
//        else {
//            htmlstr += "<tr class=\"tdClass2\">";
//            for (var k in columns) {
//                htmlstr += "<td class='tdClass' align='left'>" + data[j][columns[k]] + "</td>";
//                for (var n = 0; n < (measureArray.length <= 4 ? measureArray.length : 4); n++) {
//                    var measVal = parseFloat(data[j][measureArray[n]]).toFixed(2);
//                    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//                }
//            }
//            htmlstr += "</tr>";
//        }
//
//    }
//    htmlstr += "</table>";
//    $("#tableDiv").html(htmlstr);
//    width = $(window).width();
//    height = $(window).height();
//    active = d3.select(null);
//    var projection = d3.geo.albersUsa()
//            .scale(900)
//            .translate([width / 2, height / 2.3]);
//    var projection1 = d3.geo.albersUsa()
////    .center([98, 23.5])
//            .scale(900);
//
//    var path1 = d3.geo.path()
//            .projection(projection1);
//    if (screen.width == 1024 && screen.height == 768) {
//        projection = d3.geo.albersUsa()
//                .scale(650)
//                .translate([width / 2, height / 3]);
//    }
//
//    var path = d3.geo.path()
//            .projection(projection);
//
//    var svg = d3.select("#" + div).append("svg")
//            .attr("width", width)
//            .attr("height", height);
//
//    var g = svg.append("g")
//            .style("stroke-width", "1.5px");
//    d3.json("JS/us-States.json", function(error, us) {
//        g.selectAll("path")
//                .data(us.features)
//                .enter().append("path")
//                .attr("d", path)
//                .attr("fill", function(d, i) {
//                    var colorShad = "white";
//                    return colorShad;
//                })
//                .attr("stroke", "#000")
////                .on("click", clicked)
//                .on("mouseover", function(d, i) {
//                    for (var k = 0; k < data.length; k++) {
//                        show_detailsUS(d);
//                    }
//                })
//                .on("mouseout", function(d, i) {
//                    hide_details(d, i, this);
//                });
//    });
//
//    d3.json("JS/usCities.json", function(cities) {
//        svg.selectAll('.cities')
//                .data(cities)
//                .enter()
//                .append('path')
//                .attr('d', path.pointRadius(function(d, i) {
//                    for (var l2 = 0; l2 < data.length; l2++) {
//                        if (typeof (d.properties.name) != "undefined" && data[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
//                            return pointsize(data[l2][measureArray[0]]);
//                        }
//                    }
//                    return 0;
//                }))
////      .attr('class', 'cities')
//                .attr("class", function(d) {
//                    var c1 = (d.properties.name);
//                    for (var l1 = 0; l1 < data.length; l1++) {
//                        if (typeof (d.properties.name) != "undefined" && data[l1][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
//                            return "place";
//                        }
//                    }
//                    return "place1";
//                })
//                .attr("id", function(d, j) {
//                    var c1 = (d.properties.name);
//                    for (var l2 = 0; l2 < data.length; l2++) {
//                        if (typeof d.properties.name != "undefined" && data[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
//                            return data[l2][columns[0]] + ":" + columns[0] + ":map";
//                        }
//                    }
//                    return c1;
//                })
////        .attr("onclick", fun)
//                .attr("fill", function(d) {
//                    for (var l2 = 0; l2 < data.length; l2++) {
//                        if (typeof d.properties.name != "undefined" && data[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
//                            if (data[l2][measureArray[0]] == max) {
//                                return "red";
//                            } else {
//                                return "darkgreen";
//                            }
//                        }
//                    }
//                })
//                .style("opacity", ".9")
//                .on("mouseover", function(d, i) {
//                    show_details(d, columns, measureArray, this, i)
//                })
//                .on("mouseout", function(d, i) {
//                    hide_details(d, i, this);
//                })
//
//    });
//
//    function clicked(d) {
//        if (active.node() === this)
//            return reset();
//        active.classed("active", false);
//        active = d3.select(this).classed("active", true);
//
//        var bounds = path.bounds(d),
//                dx = bounds[1][0] - bounds[0][0],
//                dy = bounds[1][1] - bounds[0][1],
//                x = (bounds[0][0] + bounds[1][0]) / 2,
//                y = (bounds[0][1] + bounds[1][1]) / 2,
//                scale = .9 / Math.max(dx / width, dy / height),
//                translate = [width / 2 - scale * x, height / 2 - scale * y];
//
//        g.transition()
//                .duration(750)
//                .style("stroke-width", 1.5 / scale + "px")
//                .attr("transform", "translate(" + translate + ")scale(" + scale + ")");
//    }
//
//    function reset() {
//        active.classed("active", false);
//        active = d3.select(null);
//
//        g.transition()
//                .duration(750)
//                .style("stroke-width", "1.5px")
//                .attr("transform", "");
//    }
//    function show_details(d) {
//        var content;
//        var count = 0;
//        for (var j = 0; j < data.length; j++) {
//            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            for (var m in measureArray) {
//                if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
//                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + data[j][measureArray[m]] + "</span><br/>";
//                    count++;
//
//                }
//                else if (isShortForm(d, data[j][columns[0]])) {
//                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + data[j][measureArray[m]] + "</span><br/>";
//                    count++;
//
//                }
//            }
//            if (count > 0) {
//                return tooltip.showTooltip(content, d3.event);
//            }
//
//        }
//        if (count === 0) {
//            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
//            return tooltip.showTooltip(content, d3.event);
//        }
//    }
//    function show_detailsUS(d) {
//        var content;
//        var count = 0;
////        for (var j = 0; j < data.length; j++) {
//        content = "<span class=\"name\">State : </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
////            if (data[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
////                content += "<span class=\"name\">State: </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
////                count++;
//        return tooltip.showTooltip(content, d3.event);
////            }
////            else if (isShortForm(d, data[j][columns[0]])) {
////                content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\"> " + data[j][measureArray[0]] + "</span><br/>";
////                count++;
////                return tooltip.showTooltip(content, d3.event);
////            }
////        }
////        if (count === 0) {
////            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
////            content += "<span class=\"name\">" + measureArray[0] + ": </span><span class=\"value\">  --</span><br/>";
////            return tooltip.showTooltip(content, d3.event);
////        }
//    }
//    function hide_details() {
//        return tooltip.hideTooltip();
//    }
//}

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

function addCommasKPI(nStr)
{
    var splitStr=nStr.toString().split(' ');
    var numberFormat='';
    if(typeof splitStr[1]!=='undefined' && splitStr[1]!==''){
       numberFormat=splitStr[1];
    }
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
    if(x2.trim()==''){
    return x1 + x2.toString();
}
    else{
        return x1 + x2.toString()+" "+numberFormat;
    }
}
function addCommas(nStr)
{
    var currencySymbol = "";
    var currType = parent.$("#currencyType").val();
    if(typeof currType!=="undefined"){
        currencySymbol = currType;
    }
    var decimalPlace = 2;
    var suffix='';
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    while (rgx.test(x1)) {
        x1 = x1.replace(rgx, '$1' + ',' + '$2');
    }
    suffix=x2;
    if(typeof suffix.split(" ")[1]!=='undefined'){
        suffix=suffix.split(" ")[1];
    }
    else{
        suffix='';
    }
    x2 = x2.substring(0, decimalPlace + 1);
    if (x2 === ".00" || x2 === ".0") {
        x2 = "";
    }
    if((parseInt(x2))==0){
                  x2 = "";
                }
    return  x1 + x2.toString()+suffix;
}
function drillWithinchart(id,div,flag,drillId,measureId){
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
}
else {
 parent.drillWithinchart1(id,div,flag,drillId,measureId);
}
}



function buildmapTest(div,data,columns,measureArray,divWid,divHgt){

     var w = divWid;
var h = divHgt;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale
var map = d3.select("#"+div).append("svg:svg")
.attr("width", w)
.attr("height", h)
// .call(d3.behavior.zoom().on("zoom", redraw))
.call(initialize);
var india = map.append("svg:g")
.attr("id", "india");
d3.json("JS/states-tel.json", function (json) {
india.selectAll("path")
.data(json.features)
.enter().append("path")
.attr("d", path);
});
function initialize() {
proj.scale(6700);
proj.translate([-1240, 720]);
}
}
function buildmapTest1(div,data,columns,measureArray,divWid,divHgt){
    var nameMap = {
                "ANDAMAN  AND  NICOBAR ISLANDS": "ANDAMAN  AND  NICOBAR ISLANDS",
                "ANDAMAN AND NICOBAR ISLANDS": "ANDAMAN AND NICOBAR ISLANDS",
                "ANDAMAN  &  NICOBAR ISLANDS": "ANDAMAN  &  NICOBAR ISLANDS",
                "ANDAMAN AND NICOBAR": "ANDAMAN  &  NICOBAR ISLANDS",
                "ANDHRA PRADESH": "ANDHRA PRADESH",
                "ARUNACHAL PRADESH": "ARUNACHAL PRADESH",
                "AP": "ANDHRA PRADESH",
                "ASSAM": "ASSAM",
                "BIHAR": "BIHAR",
                "CHANDIGARH": "CHANDIGARH",
                "CHHATTISGARH": "CHHATTISGARH",
                "CHATISGARH": "CHHATTISGARH",
                "CHHATISGARH": "CHHATTISGARH",
                "CHATTISGARH": "CHHATTISGARH",
                "DELHI": "DELHI",
                "DAMAN AND DIU": "DAMAN AND DIU",
                "DAMAN & DIU": "DAMAN AND DIU",
                "DADAR": "DADAR",
                "NEW DELHI": "DELHI",
                "GOA": "GOA",
                "GUJARAT": "GUJARAT",
                "GUJRAT": "GUJARAT",
                "HARYANA": "HARYANA",
                "HIMACHAL PRADESH": "HIMACHAL PRADESH",
                "HP": "HIMACHAL PRADESH",
                "JAMMU AND KASHMIR": "JAMMU AND KASHMIR",
                "JAMMU  AND  KASHMIR": "JAMMU  AND  KASHMIR",
                "JAMMU & KASHMIR": "JAMMU AND KASHMIR",
                "JAMMU KASHMIR": "JAMMU AND KASHMIR",
                "JHARKHAND": "JHARKHAND",
                "KARNATAKA": "KARNATAKA",
                "KERALA": "KERALA",
                "KERLA": "KERALA",
                "LAKSHADWEEP": "LAKSHADWEEP",
                "MADHYA PRADESH": "MADHYA PRADESH",
                "MP": "MADHYA PRADESH",
                "MAHARASHTRA": "MAHARASHTRA",
                "MANIPUR": "MANIPUR",
                "MEGHALAYA": "MEGHALAYA",
                "MIZORAM": "MIZORAM",
                "NAGALAND": "NAGALAND",
                "ORISSA": "ORISSA",
                "ODISHA": "ORISSA",
                "ORRISA": "ORISSA",
                "PONDICHERRY": "PONDICHERRY",
                "PUNJAB": "PUNJAB",
                "RAJASTHAN": "RAJASTHAN",
                "RJ": "RAJASTHAN",
                "RAJ": "RAJASTHAN",
                "SIKKIM": "SIKKIM",
                "TAMIL NADU": "TAMIL NADU",
                "TN": "TAMIL NADU",
                "TELANGANA": "TELANGANA",
                "TRIPURA": "TRIPURA",
                "UTTAR PRADESH": "UTTAR PRADESH",
                "UP": "UTTAR PRADESH",
                "UTTARANCHAL": "UTTARANCHAL",
                "UTTARAKHAND": "UTTARANCHAL",
                "WEST BENGAL": "WEST BENGAL",
                "WB": "WEST BENGAL"
            };

            var hexColor = ["#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d"]
//            parent.$("#image").hide();
//            parent.document.getElementById("mainSection").style.visibility = "visible";
            var columns = JSON.parse(parent.$("#viewby").val())
            var measureArray = JSON.parse(parent.$("#measure").val());
//            if (parent.isformatedMeasure) {
//                setMeasureFormate(parent.$("#NbrFormatId").val(), parent.$("#roundId").val());
//            }
            var isInDrill = true;
            var shadingMeasure = "";
            var conditionalMeasure = "";
            var conditionalMap = {};

//            parent.toggleParamDIv();
            var w = 600;
            var h = 600;
            var proj = d3.geo.mercator();
            var path = d3.geo.path().projection(proj).pointRadius(function(d) {
                return d.coordinates / 50;
            });
            var divView;
            var t = proj.translate(); // the projection's default translation
            var s = proj.scale(); // the projection's default scale
            var map = d3.select("#"+div).append("svg:svg")
                    .attr("width", w)
                    .attr("height", h)
                    .call(initialize);

            var india = map.append("svg:g")
                    .attr("id", "india");//.attr("viewBox","50 200 950 1100");india.attr("viewBox","50 200 950 1100");
            var data1 = [];
            //    d3.json(parent.$("#tarPath").val()+"/"+columns[0]+".json", function(data1){
//            data1 = [];
            var color = d3.scale.category10();

            function show_detailsPie(d, columns, measureArray, element, j) {
                d3.select(element).attr("stroke", "grey");
                var content;
                var title;
                title = columns;
                var currName = nameMap[(d.id).toUpperCase()];
                for (var i = 0; i < columns.length; i++) {
                    content = "<span class=\"name\">" + columns[i] + ":</span><span class=\"value\"> " + currName + "</span><br/>";
                    break;
                }
                for (var i = 0; i < measureArray.length; i++) {
                    var msrData;


                    for (var l = 0; l < data1.length; l++) {
                        if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
                            for (t = 0; t < measureArray.length; t++) {
                                if (parent.isFormatedMeasure) {
                                    msrData = numberFormat(data1[l][measureArray[t]], round, precition,div)
                                }
                                else {
                                    msrData = addCommas(data1[l][measureArray[t]]);
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

            function show_detailsOfMap(d, columnVal, measureArray, con, i, data) {
                var content;
                content = "<span class=\"name\">" + columnVal + ":</span><span class=\"value\"> " + d + "</span><br/>";
                var msrData;
                var val;
                for (var l = 0; l < data.length; l++) {
//                                    if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
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
            }

            function hide_details(data, i, element) {
                d3.select(element).attr("stroke", "black")
                return tooltip.hideTooltip();
            }
            var fun = "drillWithinchart(this.id)";
            var a = 1;
            var str = "features";
            buildMap();
//            $("#chartDiv").html("");
            try {
                var sum = d3.sum(data1, function(d) {
                    return d[measureArray[0]];
                });
                changeViewOfKPI = function(view) {
                    var col = [];
                    col.push(view);
                    getKPIData(col);
                }
                var col1 = [];
                col1.push(columns[0]);
                getKPIData(col1);
            } catch (e) {
            };


            india.call(d3.behavior.zoom().on("zoom", redraw))
            function redraw() {
                d3.select("#viewport").attr("transform",
                        "translate(" + d3.event.translate + ")"
                        + " scale(" + d3.event.scale + ")");
            }
            var states = india.append("g")
                    .attr("id", "states")
                    .attr("class", "Blues")
            d3.json("wealth.json", function(json) {
                data = json;
                states.selectAll("path")
                        .attr("class", quantize);
            });

            function quantize(d) {
                return "q" + Math.min(8, ~~(data[d.id] * 9 / 12)) + "-9";
            }
            function buildMap() {
                d3.json("JS/states-tel.json", function(json) {
                    india.selectAll("path")
                            .data(json[str])
                            .enter().append("path")
                            .attr("d", path)
//                            .attr("class",function(d, i) {
//                                var currName = nameMap[(d.id).toUpperCase()];
//                                for (var l = 0; l < data1.length; l++) {
//                                    if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
//                                        return data1[l][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                                    }
//                                }
//                                return "";
//                            })
                            .attr("fill", function(d, i) {
//                                var currName = nameMap[(d.id).toUpperCase()];
//                                for (var l = 0; l < data1.length; l++) {
//                                    if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
//                                        var colorShad;
//                                        var strVal = data1[l][measureArray[0]];
//                                        if (isShadedColor) {
//                                            strVal = data1[l][shadingMeasure];
//                                            colorShad = color(strVal);
//                                        } else if (conditionalShading) {
//                                            return getConditionalColorIndia(color(l), data1[l][conditionalMeasure]);
//                                        } else {
//                                            colorShad = color(l);
//                                        }
//                                        colorVal = colorShad;
//                                        colVal = data1[l][columns[0]];
//                                        return colorShad;
//
//                                    }
//                                }
//                                colVal = currName;
//                                colorVal = "#fff";
                                return "#fff";
                            })
//                            .attr("class", colVal.replace(/[^a-zA-Z0-9]/g, '', 'gi'))
//                            .attr("id", function(d, i) {
//                                var currName = nameMap[(d.id).toUpperCase()];
//                                for (var l = 0; l < data1.length; l++) {
//                                    if (currName.toLowerCase() == data1[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data1[l][columns[0]].toLowerCase() == "chhattisgarh")) {
//                                        return data1[l][columns[0]] + ":" + columns[0] + ":map";
//                                    }
//                                }
//                                return "";
//                            })
//                            .attr("color_value", colorVal)
//                            .on("mouseover", function(d, i) {
//                                show_detailsPie(d, columns, measureArray, this, i)
//                            })
//                            .on("mouseout", function(d, i) {
//                                hide_details(d, i, this);
//                            })
//                            .attr("onclick", fun)
//                            .on("dblclick", function(d, i) {
//                                showState(nameMap[d.id.toUpperCase()], d);
//                            });
                });

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
        alert(e);
    }
    return min;
}

                var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:35%;margin-left:-20%;'></div>";
                    $('body').append(html);
                    $("#legends2").show();
                    if (parent.$("#isShaded").val() == "true") {
                    map = d3.select("#chart1").append("svg:svg")
                            .attr("width", w * 1.7)
                            .attr("height", h);
                    svgcanvas = map.append("g")
                            .attr("width", w * 1.7)
                            .attr("height", h);
                     var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:35%;margin-left:-20%;'></div>";
                    $('body').append(html);
                    var svg1 = d3.select("#legends2").append("svg")
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
                                            return conditionalMap[no]["limit1"] + conditionalMap[no]["operator"] + conditionalMap[no]["limit2"];
                                        } else {
                                            return conditionalMap[no]["operator"] + conditionalMap[no]["limit1"];
                                        }
                                    });
//                                        .attr("fill", conditionalMap[no]["color"]);
                        }
                        $("#legends2").css("width", w * .15);
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
                                .attr("dy", ".35em").text(autoFormating(min));
                        svg1.append("g").append("text").attr("x", 10)
                                .attr("y", 25)
                                .attr("dy", ".35em").text(autoFormating(max));


                        $("#legends2").css("width", "8%");
                    }
                }
            }

            function initialize() {
                var width = $(window).width();
                var height = $(window).height();
                if (screen.width == 1024 && screen.height == 768) {
                    proj.scale(width * height / 100);
                    proj.translate([-870, 540]);
                } else {
                    proj.scale(6300);
                    proj.translate([-1180, 700]);
                }
            }



            function backToChart() {
                $('#chart1').empty();
                $('#legends').hide();
                parent.$("#image").show();
                parent.document.getElementById("mainSection").style.visibility = "hidden";
                parent.$("#mapView").val("No");
                parent.$("#filters").val([]);
                parent.$("#drills").val([]);
                parent.updateChart();
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
            function tableHover(id) {
                d3.selectAll("#" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_id").style("fill", "#91FF00");
            }

            function hoverOut(id) {
                var chartColorValue = d3.selectAll("#" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_id").attr("color_value");
                d3.selectAll("#" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_id").style("fill", chartColorValue);
            }
            function tableHover1(id) {
                d3.selectAll("." + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", "#91FF00");
            }

            function hoverOut1(id) {
                var chartColorValue = d3.selectAll("#" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).attr("color_value");
                d3.selectAll("." + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", chartColorValue);
            }
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
                temp=value+ " B";
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    temp = setFormat(doubleVal,precion)+ " B";
                }else{
                    temp = setFormat(doubleVal,precion)+ " B";
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
                    temp = setFormat(doubleVal,precion)+ " Crs";
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
                    temp = setFormat(doubleVal,precion)+ " M";
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
                    temp = setFormat(doubleVal,precion)+ " Lakhs";
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
                    temp = setFormat(doubleVal,precion)+ " K";
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
            temp=value;
        }else{
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                temp = setFormat(doubleVal,precion)+ " Lakhs";
            }else{
                temp = setFormat(doubleVal,precion);
            }
        }
    }else if(symbol=="Cr"){
        doubleVal = (num / getPowerOfTen(7));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            temp = setFormat(doubleVal,precion)+ " Crs";
             }else{
                temp = setFormat(doubleVal,precion);
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
function numberFormatKPIChart(value,symbol,precion,div,suffix){
    var id='';
    if(typeof suffix!='undefined'){
        id="suffix"+div
    }
    var  doubleVal = "";
    var isNegativeValue=false;
   var chartData = JSON.parse(parent.$("#chartData").val());
   if(chartData[div]["chartType"]==='Combo-Analysis'){
       chartData[div]["kpiSubData"] = chartData[div]["kpiSubData"]*1.2;
    }
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
//        Added by shivam
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!="" && chartData[div]["kpiSubData"]!="undefined"){
                temp = addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'>  K</span>";
             }else{
               temp = addCommas(setFormat(doubleVal,precion))+ " K";
            }
            }else{
                  temp =  addCommas(setFormat(doubleVal,precion));
        }
        }
    }else if(symbol=="M"){
        doubleVal = (num / getPowerOfTen(6));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!="" && chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> M</span>";
            }else{
               temp =  addCommas(setFormat(doubleVal,precion))+ "M";
            }
            }else{
                 temp =  addCommas(setFormat(doubleVal,precion));
        }
        }
    }else if(symbol=="" || symbol=='Auto'){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion);
        }
        if(parseFloat(temp)>=1000000000 && (typeof chartData[div]["roundingType"]==='undefined' || chartData[div]["roundingType"]==="MB")){
            doubleVal = (num / getPowerOfTen(9));
            if(setFormat(doubleVal,precion) ==check){
                temp=value;
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                    if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!="" && chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> B</span>";
            }else{
                    temp =  addCommas(setFormat(doubleVal,precion))+ "B";
            }
                   // temp = setFormat(doubleVal,precion)+ "Billions";
                }else{
                    temp =  addCommas(setFormat(doubleVal,precion));
                }
            }
        }
        else if(parseFloat(temp)>=10000000 && (typeof chartData[div]["roundingType"]!=='undefined' && chartData[div]["roundingType"]==="LCr")){
            doubleVal = (num / getPowerOfTen(7));
            if(setFormat(doubleVal,precion) ==check){
                temp=value;
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
               if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!="" && chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> Crs</span>";
                }else{
               temp =  addCommas(setFormat(doubleVal,precion))+ "Crs";
            }
              //      temp = setFormat(doubleVal,precion)+ " Crores";
                }else{
                    temp =  addCommas(setFormat(doubleVal,precion));
                }
            }
        }
        else if(parseFloat(temp)>=1000000 && (typeof chartData[div]["roundingType"]==='undefined' || chartData[div]["roundingType"]==="MB")){
            doubleVal = (num / getPowerOfTen(6));
            if(setFormat(doubleVal,precion) ==check){
                temp=value;
            }else{
                if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                  if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!="" && chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> M</span>";
                }else{
               temp =  addCommas(setFormat(doubleVal,precion))+ "M";
            }
                }else{
                    temp = addCommas(setFormat(doubleVal,precion));
                }
            }
        }
        else if(parseFloat(temp)>=100000 && (typeof chartData[div]["roundingType"]!=='undefined' && chartData[div]["roundingType"]==="LCr")){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
               if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
             if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!=""&& chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> Lakhs</span>";
               }else{
               temp =  addCommas(setFormat(doubleVal,precion))+ "Lakhs";
            }
               }else{
                    temp =  addCommas(setFormat(doubleVal,precion));
        }
        }
        }
        else if(parseFloat(temp)>=1000){
            doubleVal = (num / getPowerOfTen(3));
            if(setFormat(doubleVal,precion) ==check){
                temp=value;
            }else{
                if(typeof div==='undefined'|| (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                      if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!=""&& chartData[div]["kpiSubData"]!="undefined"){
                temp =  addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'>  K</span>";
                }else{
               temp =  addCommas(setFormat(doubleVal,precion))+ " K";
            }
              //      temp = setFormat(doubleVal,precion)+ " '000";
                }else{
                    temp =  addCommas(setFormat(doubleVal,precion));
                }
            }
        }
    }
    else if(symbol==='Absolute'){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = addCommas(setFormat(doubleVal,precion));
        }
    }
    else if(symbol=="L"){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
                if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!=""&& chartData[div]["kpiSubData"]!="undefined"){
                temp = addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> 'Lakhs</span>";
            }else{
                temp = addCommas(setFormat(doubleVal,precion))+ " Lakhs";
            } }else{
                temp = addCommas(setFormat(doubleVal,precion));
            }
        }
    }else if(symbol=="Cr"){
        doubleVal = (num / getPowerOfTen(7));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
             if(typeof div==='undefined' || (typeof chartData[div]["appendNumberFormat"]==="undefined" || chartData[div]["appendNumberFormat"] ==="Y")){
            if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"]!=""&& chartData[div]["kpiSubData"]!="undefined"){
                temp = addCommas(setFormat(doubleVal,precion))+ "<span id='"+id+"' style='font-family: Helvetica;font-size:"+chartData[div]["kpiSubData"]+"px'> 'Crores</span>";
            }else{
            temp = addCommas(setFormat(doubleVal,precion))+ " Crores";
            }
             }else{
                temp = addCommas(setFormat(doubleVal,precion));
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
function getPowerOfTen(num){
    var bd = 1;
    for (var i = 0; i <
        parseFloat(num); i++) {
        bd = bd * 10;
    }
    return bd;
}
function setFormat(value,prcesion){
    var precison = parseFloat(prcesion);
    var number = parseFloat(value);
    var finalValue = ""
    if(precison==0){
        finalValue =  Math.round(number);
    }else if(precison==1){
        finalValue = (Math.round(number*10))/10;
    }else if(precison==2){
        finalValue = (Math.round(number*100))/100;
    }else if(precison==3){
        finalValue = (Math.round(number*1000))/1000;
    }else if(precison==4){
        finalValue = (Math.round(number*10000))/10000;
    }
    return finalValue;
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
     if(typeof chartData[div]["rounding1"]!=="undefined"){
        y2AxisRounding=chartData[div]["rounding1"];
    }else{
       y2AxisRounding=0;
    }
    if(typeof chartData[div]["yAxisFormat"]!=="undefined"){
        yAxisFormat=chartData[div]["yAxisFormat"];
    }else{
       yAxisFormat="";
    }
    if(typeof chartData[div]["numberFormat"]!=="undefined"){
        numberFormatArr=chartData[div]["numberFormat"];
    }else{
       numberFormatArr="";
    }
    if(typeof chartData[div]["numberRounding"]!=="undefined"){
        numberRoundingArr=chartData[div]["numberRounding"];
    }else{
       numberRoundingArr="";
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
//    if(typeof chartData[div]["hideLabel"]!=="undefined"){
//
//        hideLabel=chartData[div]["hideLabel"];
//    }else{
//        for(var i=0; i<measure.length;i++){
//       hideLabel[i] = "Yes";
//        }
//    }
}


function buildmapTestWithChart(div,data,columns,measureArray,divWid,divHgt){
    var color = d3.scale.category12();
var chart12 = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());
 columns = chartData["chart1"]["viewBys"];
var mapColumns = chartData["chart1"]["viewBys"];
var colIds = chartData["chart1"]["viewIds"];
measureArray = chartData["chart1"]["meassures"];
 if(screen.width<1000){
var w = divWid*4;
 }else{
 w = divWid;

 }
var h = divHgt;
//var w = divWid;
//var h = divHgt;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale
var fun = "drillWithinchart(this.id,\"chart1\")";
// For conditional and gradient

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







var map = d3.select("#"+div).append("svg:svg")

.attr("width", w)
.attr("height", h)
.style("margin-top","15px")
// .call(d3.behavior.zoom().on("zoom", redraw))
.call(initialize);
var india = map.append("svg:g")
//.attr("id", "india");
d3.json("JS/states-tel.json", function (json) {
//d3.json("JS/states-tel.json", function (json) {
india.selectAll("path")

.data(json.features)
.enter().append("path")
.attr("d", path)
.attr("class",function(d,i){
var currName = (d.id).toUpperCase();
for (var l = 0; l < chart12.length; l++) {
//alert("currName"+currName+"chart12[l][columns[0]]"+chart12[l][columns[0]]+"l"+l)
if (currName.toLowerCase() == chart12[l][columns[0]].toLowerCase()) {
    return chart12[l][columns[0]].toLowerCase();
}else{
return chart12[l][columns[0]].toLowerCase();
}
}
})
//.attr("onclick", fun)
.attr("fill", function(d,i){
var currName = d.id;
//for (var l = 0; l < chartMapData.length; l++) {
//if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
//    return color(i);
//}
//}
//return "white";
var colorShad;
var drilledvalue;

                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }

 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                    colorShad = drillShade;
                }

                            for (var l in chartMapData) {
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
}).on("mouseover", function(d, i) {
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
return "";
}).attr("onclick", fun);

 var html = "<div id='legends2' class='legend2' style='float:left;align:rigth;overflow: visible;margin-top:-17%;margin-left:32%;'></div>";
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


                        $("#legends2").css("width", "8%");
                    }


});

function initialize() {
    $("#viewMeasureBlock").hide();
    if(screen.width<1000){
//alert("hiii");
proj.scale(4000);
proj.translate([-550, 550]);
    }else{
proj.scale(5200);
proj.translate([-950, 580]);
}
}
var tabDiv="chart_2";

if(typeof $("#tabDiv").val()!="undefined" && $("#tabDiv").val()!=""){
   tabDiv =$("#tabDiv").val();
}
//function

var htmlDiv = "<div id='pieWithMap' style = 'height:auto;overflow-y:auto;margin-top:0px;margin-right:20px;float:right;width:49%;'></div>";
$("#"+div).append(htmlDiv);
pieWithTable1=function(id){pieWithTable(id);}
pieWithTable(tabDiv);
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
  if(screen.width<1000){

buildPieMap(tabDivVal,pieDivId,daatPie, columns, measureArray,divWid*.3,divHgt*.6);
  }else{
buildPieMap(tabDivVal,pieDivId,daatPie, columns, measureArray,divWid*.5,divHgt*.5);
}
}
}
function buildPieMap(div,pieDivId, data, columns, measureArray,wid,hgt) {
    var color = d3.scale.category12();
    var divWidth, divHeight, rad;

    var w = wid;
    divWidth=wid;
//     divWid=parseFloat($(window).width())*(.35);
    divHeight=hgt;
    rad=divHeight*.55;
var chartData = JSON.parse($("#chartData").val());
var colIds=chartData[div]["viewIds"];
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
    svg = d3.select("#" + pieDivId).append("svg:svg")
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
            .attr("onclick", fun);

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
                return color(i);}//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
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
    $("#"+pieDivId).append(html);
}


function buildmapTest(div,data,columns,measureArray,divWid,divHgt){
     var fun = "drillWithinchart(this.id,\"chart1\")";
     var color = d3.scale.category10();
var chart12 = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());
columns = chartData["chart1"]["viewBys"];
mapColumns = chartData["chart1"]["viewBys"];
measureArray = chartData["chart1"]["meassures"];
var colIds = chartData["chart1"]["viewIds"];
var w = divWid;
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
var india = map.append("svg:g")
.attr("id", "india");
d3.json("JS/states-tel.json", function (json) {
//d3.json("JS/states-tel.json", function (json) {
india.selectAll("path")

.data(json.features)
.enter().append("path")
.attr("d", path)
.attr("fill", function(d,i){
var currName = (d.id);
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
.attr("id",function(d,i){
        var currName = (d.id).toUpperCase();
for (var l = 0; l < chartMapData.length; l++) {
if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
    return chartMapData[l][mapColumns[0]]+":"+chartMapData[l][mapColumns[0]];
}
}
return "";
}).attr("onclick", fun);
});
function initialize() {
proj.scale(5700);
proj.translate([-1024, 650]);
}
if(data["chart2"]!= "undefined"){
    chart12 = data["chart2"];
    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}
var htmlstr="";
htmlstr += "<div style = 'height:auto;overflow-y:auto;margin-top:20px;float:right;width:45%;'>";
htmlstr +="<table style='width:100%'><tr><td>"
htmlstr+="</tr></td>"
htmlstr+="<tr><td><div id = 'mapTable' style ='overflow-y:auto;margin-right:10px'>"
htmlstr += "<table style='width:100%' class = 'tBody' style = 'float:right'>";
htmlstr += "<tr class=\"thClass\" align='center'>";
htmlstr += "<th >"+columns[0]+"</th>";
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
   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
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
htmlstr += "</div>";
$("#" + div).append(htmlstr);
var chart123=[];
for(i=0;i<10;i++){
   chart123.push(chart12[i]);
}
}

function bubbleDb(div,bubbleDivId, data0, columns, measures, divWidth, divHeight, layout) {
    var html = "<div id='bubbleDb' style='display:none'></div>";
    $("#"+bubbleDivId).append(html);
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
    }

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
        colIds = chartData[div]["viewIds"];
    }else{
        colIds = chartData[div]["viewIds"];
    }
    var chartMap = {};
//    var fun = "drillWithinchart(this.id,\""+div+"\")";
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

    var isDashboard = parent.$("#isDashboard").val();
    var chartType = parent.$("#chartType").val();
    var nodes = [];
//data0.forEach(function(d, i) {
//if (i < data0.length && d[measures[0]] >= 0) {
//var node = {};
//node["name"] = d[columns[0]];
//
//if (columns.length > 1) {
//node["name1"] = d[columns[1]];
//} else {
//node["name1"] = d[columns[0]];
//}
//for (var no = 0; no < measures.length; no++) {
//if (no === 0) {
//node["value"] = d[measures[no]];
//} else {
//node["value" + no] = d[measures[no]];
//}
//}
//return nodes.push(node);
//}
//});
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
    svg = d3.select("#" + bubbleDivId).append("svg")
            .attr("width", divWidth - layout)
            .attr("height", divHeight  - 50)
            .attr("id", "bubble");
    var gradient = svg.append("svg:defs").selectAll("radialGradient").data(data).enter()
            .append("svg:radialGradient")
            .attr("id", function(d) {
                if (d !== "undefined" && typeof d !== "undefined") {
                    return "gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
//Added By Ram
                if (typeof chartMap[d.name] === "undefined") {
                    colorMap[i] = d.name + "__" + colorShad;
                    chartMap[d.name] = colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    var  html1 ="<input type='hidden' id='legends"+chartIndex+"' name='legends' />";
//   $("#legendsDiv").append(html1);
//    parent.$("#legends"+chartIndex).val(JSON.stringify(chartMap));
//End Ram code
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
                return "translate(" + d.x + ", " + top + ")";
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
//                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
var colorShad;
//        var drilledvalue = parent.$("#drilledValue").val().split(",");
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
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
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
            .attr("id", function(d) {
                return d.name1 + ":" + d.value + ":" + measures[0];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})

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
                        msrData = addCurrencyType(div, getMeasureId(measure1)) + addCommas(msrData);
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
                hide_details(d, i, this);
            });

    node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "8pt")
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
            .text(function(d) {
//                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
//                    return d.name.substring(0, d.r / 5);
//                }
//                else {
                    return d.name1.substring(0, d.r / 5);
//                }
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
}
function buildDashboardScatter(div,data) {
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
    }

var chartData = JSON.parse(parent.$("#chartData").val());
        var charts = Object.keys(chartData);
        var width = $(window).width();
        var divHeight = $(window).height();
        var divContent = "";
        var widths = [];
        divContent += "<div class='tooltip' id='my_tooltip' style='display: none'></div>";
        switch (charts.length) {
            case 1:
                {
                    widths.push(width / 1.2);
                    divHeight = $(window).height();
                    divContent += "<div id='chartAd1' style='float:left;'></div>";
                    break;
                }
            case 2:
                {
                    widths.push(width / 1.2);
                    widths.push(width / 1.2);
                        divHeight = $(window).height() / 2.5;
                        divContent += "<div id='chartAd1' ></div></td></tr><tr><td><div id='chartAd2' ></div>";
                    break;
                }
            default :
                {
                    widths.push(width / 1.2);
                    widths.push(width / 1.2);
                        divHeight = $(window).height() / 2.5;
                        divContent += "<div id='chartAd1' ></div></td></tr><tr><td><div id='chartAd2' ></div>";
                    break;
                }
        }
        $("#"+div).html(divContent);

        for (var ch in charts) {
            if(ch<2){
            var chartDetails = chartData[charts[ch]];
var chartType;
var chartList=[];
             chartList.push("Bubble-Scatter");
             chartType = "Bubble-Scatter";
            var chartId = "chart" + (parseFloat(ch) + 1);
            var divId = "chartAd" + (parseFloat(ch) + 1);
            try{
            buildDashlet1(chartDetails, ch, widths[ch], divHeight, chartType, chartId,divId, chartList);
            }
            catch(e){}
        }
        }

    function buildDashlet1(chartDetails, chartIndex,  divWidth, divHeight, chart, chartId,divId, chartList) {
        var viewbys = [];
        var viewbyVals = chartDetails.viewBys;
        viewbys.push(viewbyVals[0]);
        var measures = chartDetails.meassures;
                var data3 = data[chartId];
               buildScatterPlot(chartId,divId, data3, viewbys, measures, $(window).width() / 1.1, divHeight * 1.1);
    }
}

function buildScatterPlot(div,divId, data, columns, measureArray, width, height){
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

    var chartData = JSON.parse(parent.$("#chartData").val());
    if(measureArray.length<2){
        measureArray[1]= measureArray[0];
    }
    var colIds = chartData["chart1"]["viewIds"];
    var margin = {top: 20, right: 130, bottom: 30, left: 100},
    width = width - margin.left - margin.right,
    height = height - margin.top - margin.bottom;
var xValue = function(d) { return d[measureArray[1]];},
    xScale = d3.scale.linear().range([0, width]),
    xMap = function(d) { return xScale(xValue(d));},
    xAxis = d3.svg.axis().scale(xScale).orient("bottom");

// setup y
var yValue = function(d) { return d[measureArray[0]];},
    yScale = d3.scale.linear().range([height, 0]),
    yMap = function(d) { return yScale(yValue(d));},
    yAxis = d3.svg.axis().scale(yScale).orient("left");
    color = d3.scale.category12();

var svg = d3.select("#"+divId).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
  .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

var tooltip = d3.select("#"+divId).append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

  data.forEach(function(d) {
    d[measureArray[1]] = +d[measureArray[1]];
    d[measureArray[0]] = +d[measureArray[0]];
  });

  xScale.domain([d3.min(data, xValue)-1, d3.max(data, xValue)+1]);
  yScale.domain([d3.min(data, yValue)-1, d3.max(data, yValue)+1]);

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

  // y-axis
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

  // draw dots
  svg.selectAll(".dot")
      .data(data)
    .enter().append("circle")
      .attr("class", "dot")
      .attr("r", 10)
      .attr("cx", xMap)
      .attr("cy", yMap)
      .style("fill", function(d,i) {

var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                    colorShad = drillShade;
                } else {
//                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
//                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
//                    } else {
                        colorShad = color(i);
                    }
//                }


return colorShad;
//return color(i);

})
        //  return color(i);
    //  })
      .attr("id",function(d,i) {return d[columns[0]]+":"+d[measureArray[0]];})
      .on("mouseover", function(d) {
          show_details(d, columns, measureArray, this,div);
      })
      .on("mouseout", function(d,i) {
          hide_details(d, i, this);
      }).attr("onclick", fun)
      //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	});

  // draw legend
//  var legend = svg.selectAll(".legend")
//      .data(color.domain())
//    .enter().append("g")
//      .attr("class", "legend")
//      .attr("transform", function(d, i) { return "translate(0," + i * 20 + ")"; });
//
//  // draw legend colored rectangles
//  legend.append("rect")
//      .attr("x", width - 18)
//      .attr("width", 18)
//      .attr("height", 18)
//      .style("fill", color);
//
//  // draw legend text
//  legend.append("text")
//      .attr("x", width - 24)
//      .attr("y", 9)
//      .attr("dy", ".35em")
//      .style("text-anchor", "end")
//      .text(function(d) { return d;})
//});

}



  function buildCityMap(div,data,columns,measureArray,divWid,divHgt){

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

// var fun = "drillWithinchart(this.id,\"chart1\")";
  //Added by shivam
	var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
	if(hasTouch){
		fun="";
        }
	else{
            fun = "drillWithinchart(this.id,\"chart1\")";
	}
        function drillFunct(id1){
//            alert("hello");
            drillWithinchart(id1,chart1);
	}

  var data1 = [];
  data1 = data["chart1"];
     var color = d3.scale.category12();
var chart12 = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());
columns = chartData["chart1"]["viewBys"];
mapColumns = chartData["chart1"]["viewBys"];
measureArray = chartData["chart1"]["meassures"];
var colIds = chartData["chart1"]["viewIds"];
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
    if(screen.width<1000)
    {
        proj.scale(3500);
proj.translate([-680, 400]);
    }
    else{
proj.scale(5000);
proj.translate([-900, 580]);
}

}
if(data["chart2"]!= "undefined"){
    chart12 = data["chart2"];
    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}
var daatPie=[];
for(i=0;i<(chart12.length < 10 ? chart12.length : 10);i++){
   daatPie.push(chart12[i]);
}
var htmlstr="";
htmlstr += "<div id='pieWithMap' style = 'height:550px;overflow-y:auto;margin-top:20px;margin-right:20px;float:right;width:35%;'>";
//htmlstr +="<table style='width:100%'><tr><td>"
//htmlstr+="<div id='piechart'><div>"
//htmlstr+="</tr></td>"
//htmlstr+="<tr><td><div id = 'mapTable' style ='overflow-y:auto;height:250px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'tBody' style = 'float:right'>";
//htmlstr += "<tr class=\"thClass\" align='center'>";
//htmlstr += "<th >"+columns[0]+"</th>";
//for(var i in measureArray){
//htmlstr += "<th>"+measureArray[i]+"</th>";
//}
//htmlstr += "</tr>";
//
// var kpicount = 0;
//var avgData = [];
//for (var j in chart12) {
//
//avgData.push(chart12[j]);
//kpicount++;
//if (kpicount % 2 == 1) {
// htmlstr += "<tr class=\"tdClass1\">";
//   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
//for (var n = 0; n < measureArray.length; n++) {
//    var measVal = parseFloat(chart12[j][measureArray[n]]).toFixed(2);
//    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//}
//htmlstr += "</tr>";
//}
//else {
// htmlstr += "<tr class=\"tdClass1\">";
//for (var k in columns) {
//   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='" + chart12[j][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
//}
//for (var n = 0; n < measureArray.length; n++) {
//    var measVal = parseFloat(chart12[j][measureArray[n]]).toFixed(2);
//    htmlstr += "<td class='tdClass' align='right'>" + addCommas(measVal) + "</td>";
//}
//htmlstr += "</tr>";
//}
//}
//htmlstr += "</table></div></td></tr></table>";
htmlstr += "</div>";
$("#" + div).append(htmlstr);

buildDashboardTable("chart2","pieWithMap", chart12, columns, measureArray, 300, 700)
  $("#viewMeasureBlock").css('display','none');
//buildPieMap("piechart",daatPie, columns, measureArray,300,300,140);
var chart123=[];
for(i=0;i<10;i++){
   chart123.push(chart12[i]);
}
$("#viewMeasureBlock").css('display','none');
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
        for(var key in data1) {

            min1.push(data1[key][measureArray[0]]);
        }

        var minRange=1;
        if(data1.length==1){
            minRange=12;
        }
        var pointsize = d3.scale.linear()
        .domain([Math.min.apply(Math, min1),Math.max.apply(Math, min1)])
        .range([minRange,10]);
//        var fun = "drillWithinchart(this.id,\"chart1\")";

    //Added by shivam
	var fun="";
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





function buildWaterfall(div, currData, columns, measureArray,wid,hgt){

     var yScale = d3.scale.linear();
    var waterfallData = [];
    var margin = 35;
   var color = d3.scale.category10();
var divWid=parseFloat($(window).width())*(.35);
var chartData = JSON.parse(parent.$("#chartData").val());
var colIds = chartData[div]["viewIds"];

var divHgt=500;
 height = divHgt * .6;
var height = hgt;
var width = divWid;
    var xs = currData;
    var data = [];
    var n = xs.length;
    var xDelta = divWid / n;

    var svg = d3.select("#"+div).append("svg")
        .attr("width", divWid + margin + margin)
        .attr("height", 400)
        .append("g")
        .attr("transform", "translate(" + margin + "," + margin + ")");

    // X-axis
    var x = d3.scale.ordinal()
        .rangeRoundBands([0, width], 0.05)
        .domain(xs.map(function (d) {
            return d[columns[0]];
        }));

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom");

    // Y-axis
    var y = d3.scale.linear()
        .range([height, 0]);

  var yAxis = d3.svg.axis()
        .scale(y)
        .ticks(4)
        .orient("left");

    // Trend line
    var line = d3.svg.line()
        .x(function (d, i) {
            return i * xDelta + xDelta * .5 + margin / 2;
        })
        .y(function (d) {
            return (d[measureArray[0]] < 0) ? height - yScale(d.cumulativeSum - d[measureArray[0]]) : height - yScale(d.cumulativeSum);
        });

//    calculateScale();

var max = 0, min = 0, cumSum = 0;
        xs.forEach(function (d) {
            cumSum += d[measureArray[0]];
            if (cumSum < min) {
                min = cumSum;
            }
            if (cumSum > max) {
                max = cumSum;
            }
            data.push({value: d[measureArray[0]], cumulativeSum: cumSum});
        });
        y.domain([min, max]);
        yScale.domain([min, max]).range([0, height]);
//    calculateBars();
 svg.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("class", function (d) {
//                return (d.value < 0) ? "negative" : "positive";
            })
            .attr("x", function (d, i) {
                return i * xDelta + margin;
            })
            .attr("y", function (d, i) {
                return (d.value < 0) ? height - yScale(d.cumulativeSum - d.value) : height - yScale(d.cumulativeSum);
//                return y(d[measureArray[0]]);
            })
            .attr("width", function (d) {
                return xDelta;
            })
            .attr("height", function (d) {
                return yScale(Math.abs(d.value));
            })
            .attr("fill",function(d,i){
                return color(i)
            });


 svg.append("g")
            .attr("class", "axis")
            .attr("transform", "translate(" + margin + "," + height + ")")
            .call(xAxis);

        svg.append("g")
            .attr("class", "axis")
            .call(yAxis);

        svg.append("path")
            .datum(data)
            .attr("class", "trend-line")
            .attr("fill","none")
            .attr("stroke","rgb(0,138,0)")
            .attr("stroke-width","1.5px")
//            .attr("stroke-dasharray")
            .attr("d", line);
}

function hideLabels(id,div){
     parent.hideLabelSection(id,div);
}


function buildmapState(div,data,columns,measureArray,divWid,divHgt){
     var fun = "drillWithinchart(this.id,\"chart1\")";
     var color = d3.scale.category10();
var chart12 = data["chart1"];
var chartMapData = data["chart1"];
var chartData = JSON.parse(parent.$("#chartData").val());
columns = chartData["chart1"]["viewBys"];
mapColumns = chartData["chart1"]["viewBys"];
measureArray = chartData["chart1"]["meassures"];
var colIds = chartData["chart1"]["viewIds"];
var w = divWid;
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
.attr("id",function(d,i){
        var currName = (d.id).toUpperCase();
for (var l = 0; l < chartMapData.length; l++) {
if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
    return chartMapData[l][mapColumns[0]]+":"+chartMapData[l][mapColumns[0]];
}
}
return "";
}).attr("onclick", fun);
});
function initialize() {
proj.scale(5700);
proj.translate([-1024, 650]);
}
if(data["chart2"]!= "undefined"){
    chart12 = data["chart2"];
    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}
var htmlstr="";
htmlstr += "<div style = 'height:auto;overflow-y:auto;margin-top:20px;float:right;width:45%;'>";
htmlstr +="<table style='width:100%'><tr><td>"
htmlstr+="</tr></td>"
htmlstr+="<tr><td><div id = 'mapTable' style ='overflow-y:auto;margin-right:10px'>"
htmlstr += "<table style='width:100%' class = 'tBody' style = 'float:right'>";
htmlstr += "<tr class=\"thClass\" align='center'>";
htmlstr += "<th >"+columns[0]+"</th>";
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
   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
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
htmlstr += "</div>";
$("#" + div).append(htmlstr);
var chart123=[];
for(i=0;i<10;i++){
   chart123.push(chart12[i]);
}
}


function buildDistrictMap(div,data,columns,measureArray,divWid,divHgt){
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
var w = divWid;
var h = 550;
var proj = d3.geo.mercator();
var path = d3.geo.path().projection(proj);
var t = proj.translate(); // the projection's default translation
var s = proj.scale() // the projection's default scale

var html1 = "<div id = 'mapDiv"+div+"' style='height:auto;overflow-y:auto;float:left;width:53%;'></div>";
$("#"+div).append(html1);
var map = d3.select("#mapDiv"+div).append("svg:svg")
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
$("#viewMeasureBlock").hide();
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

   showState(this.id, d);
    });
});

function initialize() {
proj.scale(5300);
proj.translate([-1000, 600]);
}

var htmlstr1="";
htmlstr1 += "<div id ='mapTable"+div+"'  style = 'height:500px;overflow-y:auto;margin-top:20px;float:right;width:45%;'></div>";
if(data["chart2"]!= "undefined"){
    chart12 = data["chart2"];
    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}
$("#mapTable" + div).append(htmlstr1);
var htmlstr = mapTableFunction(chart12,columns,measureArray);

$("#" + div).append(htmlstr1);
$("#mapTable" + div).append(htmlstr);


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
            $("#mapDiv" + div).html("");

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

                           showDistrictTable(district,districtData,"districtClick")
                       })

                      })
                 })
                  showDistrictTable(districtArray,districtData,"stateClick")
 })

                    })


                    var abc = state.toLowerCase();
});
function  showDistrictTable(districtArray,districtData,flag){


     if(typeof districtmapchartData["chart3"]!= "undefined" && flag=="stateClick1"){
    chart12 = districtmapchartData["chart3"];

    columns = chartData["chart3"]["viewBys"];
    measureArray = chartData["chart3"]["meassures"];
}else{
      var districtLevelData = [];
      for(var i in districtData){
          if(flag!="districtClick"){
    for(var k in districtArray){

   if(districtArray[k].toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){

       districtLevelData.push(districtData[i])
   }}}else{
if(districtArray.toLowerCase()== districtData[i][chartData["chart2"]["viewBys"][0]].toLowerCase()){

       districtLevelData.push(districtData[i])
   }
   }
      }

     chart12 = districtLevelData;

    columns = chartData["chart2"]["viewBys"];
    measureArray = chartData["chart2"]["meassures"];
}


$("#mapTable" + div).html("");
var htmlstr = mapTableFunction(chart12,columns,measureArray);
$("#mapTable" + div).append(htmlstr);
 }
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
             function mapTableFunction(chart12,columns,measureArray){
                var htmlstr="";
htmlstr +="<table style='width:100%'><tr><td>"
htmlstr+="</tr></td>"
htmlstr+="<tr><td><div id = 'mapTable' style ='overflow-y:auto;margin-right:10px'>"
htmlstr += "<table style='width:100%' class = 'table table-condensed table-bordered' style = 'float:right'>";
htmlstr += "<tr class=\"thClass\" align='center'>";
htmlstr += "<th >"+columns[0]+"</th>";
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
   htmlstr += "<td class='tdClass' align='left' onclick='getState(this.id)' id='' onmouseover='tableHover1(this.id)' onmouseout='hoverOut1(this.id)'>" + chart12[j][columns[0]] + "</td>";
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
return htmlstr;
  }
}
function backToChart(ch,data){
   var  chartData = parent.$("#chartData").val();
     chartTypeFunction(ch,chartData["chart1"]["chartType"],chartData["chart1"]["Name"]);
}
    function getDrawColor(chartId,i)
{
    var chartData = JSON.parse(parent.$("#chartData").val());
    var index="color"+(i+1);
    colors = d3.scale.category10();
    var colorArray=[];
    for(var j=0;j<100;j++)
    {
        colorArray[j]=colors(j);

    }
    var colorValue;
//    for(var j=0;j<5;j++)
//    {
//        alert("***"+colors(j)+":"+JSON.stringify(colors));
//    }
    if(typeof chartData[chartId]["customColors"]==='undefined' || typeof chartData[chartId]["customColors"][index]==='undefined')
    {
        colorValue = colorArray[i];
//          return colors(i);
    }
    else
    {
        colorValue = chartData[chartId]["customColors"][index];
    }
    return colorValue;
}

function getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color){
if(chartData[div]["chartType"]==="Bubble" || chartData[div]["chartType"]==="TopBottom-Chart" || chartData[div]["chartType"]==="US-State-Map" || chartData[div]["chartType"]==="Tree-Map"|| chartData[div]["chartType"]==="India-Map"
|| chartData[div]["chartType"]==="Australia-State-Map" || chartData[div]["chartType"]==="Australia-City-Map" || chartData[div]["chartType"]==="IndiaCity-Map" || chartData[div]["chartType"]==="Combo-IndiaCity" || chartData[div]["chartType"]==="Combo-India-Map" || chartData[div]["chartType"]==="US-City-Map"
|| chartData[div]["chartType"]==="Combo-US-Map" || chartData[div]["chartType"]==="Combo-USCity-Map" ||chartData[div]["chartType"]==="Combo-Aus-State"|| chartData[div]["chartType"]==="Combo-Aus-City"
|| chartData[div]["chartType"]==="Telangana"  || chartData[div]["chartType"]==="Andhra-Pradesh"){
        
					if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="single")){
					var drawColor=getDrawColor(div, parseInt(0));
                    return drawColor;
        }else if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Multi")){
					var drawColor=getDrawColor(div, parseInt(i));
                    return drawColor;
        }else if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){ 
            return getConditionalColor2(div,chartData,drillShade,data,columns,measureArray,i,color);
        }else {
				var measureData = [];
                                var length=data.length<chartData[div]["records"]?data.length:chartData[div]["records"];   // add by mayank sharma for gradient
                                if(typeof chartData[div]["others"]!="undefined" && chartData[div]["others"]==="Y"){
                                    length++;
                                }
                                if(typeof chartData[div]["gtGraph"]!="undefined" && chartData[div]["gtGraph"]==="Y"){
                                    length++;
                                }
                                      for (var j= length; j >0; j--) {
                                measureData.push(j);
                                   }   // end by mayank sharma for gradient
				var drawColor=getDrawColor(div, parseInt(0));
                                color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", drawColor]);
                               return color(measureData[i]);
					}
     }else{
         if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="single")){
					var drawColor=getDrawColor(div, parseInt(0));
                    return drawColor;
        }else if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Gradient")){
				var measureData = [];
				 var length=data.length<chartData[div]["records"]?data.length:chartData[div]["records"];   // add by mayank sharma for gradient
                                if(typeof chartData[div]["others"]!="undefined" && chartData[div]["others"]==="Y"){
                                    length++;
                                }
                                if(typeof chartData[div]["gtGraph"]!="undefined" && chartData[div]["gtGraph"]==="Y"){
                                    length++;
                                }
                                      for (var j= length; j >0; j--) {
                                        measureData.push(j);
                                   }   // end by mayank sharma for gradient
				var drawColor=getDrawColor(div, parseInt(0));
                                color = d3.scale.linear()
                                .domain([0, Math.max.apply(Math, measureData)])
                                .range(["rgb(240,240,240)", drawColor]);
                               return color(measureData[i]);
        }else if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
            return getConditionalColor2(div,chartData,drillShade,data,columns,measureArray,i,color);
        }else{
					var drawColor=getDrawColor(div, parseInt(i));
                    return drawColor;
					}
}
}

function circularChartsTable(div,data0, measureArray,j) {
    var content = "";
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData["chart1"];
    var msrData;
//    var Prefix="";
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
    if(typeof chartData[div]["Prefix"]!="undefined" && chartData[div]["Prefix"]!="" ){
   msrData = chartData[div]["Prefix"]+addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data0[j][measureArray[0]],yAxisFormat,yAxisRounding,div)); 
    }else{
     msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data0[j][measureArray[0]],yAxisFormat,yAxisRounding,div)); 
    }
//    msrData = addCommas(numberFormat(data0[j][measureArray[0]],yAxisFormat,yAxisRounding,div));


   content +="<tr id=\"circularTable\" class=\"value\"> " + msrData + "</tr>";
    //    }
    return msrData;
}

/*Added by Ashutosh*/
function show_stackDetail(div,d,data,chartData,measureArray,content,i,msrData){
     var sum =[] ;
                  for(var z=0;z<measureArray.length;z++){
                  var sum1 = d3.sum(data, function(d,i) {
                     return d[measureArray[z]];
                    });
                    sum.push(sum1);
                  }
                  var measureName=d.name;
                  var measureValue=d.value;
                  var columnName=d.viewBy;
               
            var percentage = (measureValue/ parseFloat(sum[i])) * 100;
                
         //      alert((chartData[div]["tooltipType"][measureArray[i]]).toString());
            if(typeof (chartData[div]["tooltipType"])!=="undefined"){
             if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="With%"){
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> "+percentage.toFixed(1) + "%</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + " </span><br/>";
            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default-With%"){
                 content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "("+percentage.toFixed(1) + "%)</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + " </span><br/>";
            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default"){
                  content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + " </span><br/>";
            }
            }else{
              content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + " </span><br/>";
            }
            return content;
}
//function show_CumulativeDetail(div,d,data,chartData,measureArray,content,z){
//    var i = z;
//    var sum = d3.sum(data, function(d,i) {
//        return d[measureArray[z]];
//    });
//   var msrData;
//     if (isFormatedMeasure) {
//            msrData = numberFormat(d[measureArray[i]], round, precition);
//        }
//        else {
//            msrData = addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
//        }
//    var percentage = (d[measureArray[z]] / parseFloat(sum)) * 100;
//    if(typeof (chartData[div]["tooltipType"])!=="undefined"){
//             if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="With%"){
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> "+percentage.toFixed(1) + "%</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";
//            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default-With%"){
//                 content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "("+percentage.toFixed(1) + "%)</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";
//            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default"){
//                  content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";
//            }
//            }else{
//              content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + d.name + " <b>:</b> " + d.viewBy + " </span><br/>";
//            }
//            return content;
//}

function getSum(chartData,currData,measureArray,chartId){
    var measureArray=chartData[chartId]["meassures"];
        var sum = d3.sum(currData, function(d) {
        return d[measureArray[0]];
    });
  window.abSum= sum;
//  alert("window.abSumaaaa"+window.abSum);
}
