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
//}
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
        c = parseInt(hex.substr(i * 2, 2), 16);
        c = Math.round(Math.min(Math.max(0, c + (c * lum)), 255)).toString(16);
        rgb += ("00" + c).substr(c.length);
    }
    return rgb;
}

function setColumnMap(columMap) {
    columnMap = JSON.parse(columMap);
}


function buildBar5(divappend,div, data,chartData, columns, measureArray,wid,hgt) {
//alert("colll "+columns[0])
//alert("colll "+JSON.stringify(data))
//alert("bbbb "+data.length)
var color = d3.scale.category10();
//var divWid=parseInt($(window).width())*(.35);
var divWid=wid;
//var chartData = JSON.parse(parent.$("#chartData").val());
var colIds = chartData[div]["viewIds"];
//   var divHgt=500;
   var divHgt=hgt;
    var chartMap = {};
    var measure1 = measureArray[0];
    var minVal = 0;
//    var chartData = JSON.parse($("#chartData").val());
    var prop = graphProp(chartData,div);

    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }
    var autoRounding1;
//    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
//        autoRounding1 = columnMap[measure1]["rounding"];
//    } else {
        autoRounding1 = "1d";
//    }
//    var isDashboard = parent.$("#isDashboard").val();
//    var chartType = parent.$("#chartType").val();
//    if (chartType === "dashboard") {
//        isDashboard = true;
//    }
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var botom = 50;
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }
    var margin = {
        top: 10,
        right: 00,
        bottom: botom,
        left: 70
    },
    width = divWid- margin.left - margin.right , //- margin.left - margin.right
//            height = divHgt * .6; //- margin.top - margin.bottom
            height = divHgt *.7; //- margin.top - margin.bottom
    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
   var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);
    y = d3.scale.linear()
            .range([height, 0]);
    var measArr = [];
    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
//    xAxis = d3.svg.axis()
//            .scale(x)
//            .orient("bottom");
//    if (isFormatedMeasure) {
        yAxis = d3.svg.axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
//                    if(typeof displayY !=="undefined" && displayY =="Yes"){
//                  if(yAxisFormat==""){
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                        return addCommas((d));
//                    }
//            else{
//                    return numberFormat(d,yAxisFormat,yAxisRounding);
//                }
//                }else {
//                    return "";
//                }
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
                .tickFormat(function(d, i) {
//                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return addCommas(d);
                } 
//                   }else {
//                       return "";
//                   }
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
    svg = d3.select("#" + divappend).append("svg")
            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom +40 )
            .attr("height", divHgt  )
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
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(i);
//                            colorShad = "steelblue";
                        }
                    }
                }
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//colorShad="steelblue";
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    parent.$("#colorMap").val(JSON.stringify(colorMap));

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var max = maximumValue(data, measure1);
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    var yMin = 0;
    yMin = minVal;
    y.domain([yMin, max]);
    svg.call(yAxis1);
    var diffFactor = parseInt(measArr[0] - parseInt(measArr[1]));
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
    for (var j=0; j <= height; j=j+50) {
    svg.append("svg:line")
        .attr("x1", 0)
        .attr("y1", j)
        .attr("x2", width)
        .attr("y2", j)
        .style("stroke", "#A69D9D")
        .style("stroke-width", .5)
        .style("z-index", "9999");
};

    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height + 3) + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
//               if(typeof displayX !=="undefined" && displayX =="Yes"){
                if (d.length < 15){
                    return d;
                }
                else {
                    return d.substring(0, 15) + "..";
                }
//               }else {
//                   return "";
//               }
                
            })
            .attr('transform', 'rotate(-45)')
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
            .enter().append("g")
            .attr("class", "bar")
            .append("rect")
            .attr("rx", barRadius)
            .attr("fill", function(d,i) {
//                 var drilledvalue;
//                    try {
//                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                    } catch (e) {
//                    }
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
//                        return drillShade;
//                    }

                return color(i);//"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
            .attr("x", function(d) {
                return x(d[columns[0]]);
            })
            .attr("width", x.rangeBand())
            .attr("y", function(d) {
                return y(d[measure1]);
            })
            .attr("height", function(d) {
                return height - y(d[measure1]);
            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    
//                }
//                if (toolTipType === "customize") {
//                    for (var num in toolTipData) {
//                        if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                            break;
//                        }
//                    }
//                } else {

                    show_details(chartData,d, columns, measureArray, this,div);
//                }
            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
//                }
                hide_details(d, i, this);
            });
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
                .attr("fill", labelColor)
                .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding);
                    }else{
                    var percentage = (d[measureArray[0]] / parseInt(sum)) * 100;
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

}

function buildBarPortal(div, data, columns, measureArray,wid,hgt) {
var color = d3.scale.category10();
var divWid=wid;
//var chartData = JSON.parse(parent.$("#chartData").val());
//var colIds = chartData[div]["viewIds"];
var colIds = "";
   var divHgt=hgt;
    var chartMap = {};
    var measure1 = measureArray[0];
    var minVal = 0;
//    var chartData = JSON.parse($("#chartData").val());
//    var prop = graphProp(div);

    if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }
    var autoRounding1;
//    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
//        autoRounding1 = columnMap[measure1]["rounding"];
//    } else {
        autoRounding1 = "1d";
//    }
//    var isDashboard = parent.$("#isDashboard").val();
//    var chartType = parent.$("#chartType").val();
//    if (chartType === "dashboard") {
//        isDashboard = true;
//    }
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var botom = 50;
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }
    var margin = {
        top: 10,
        right: 00,
        bottom: botom,
        left: 70
    },
    width = divWid, //- margin.left - margin.right
            height = divHgt * .6; //- margin.top - margin.bottom
    var barPadding = 4;
//    var formatPercent = d3.format(".0%");
  var x = d3.scale.ordinal()
            .rangeRoundBands([0, width], .1, .1);
    y = d3.scale.linear()
            .range([height, 0]);
    var measArr = [];
    xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
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
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding);
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
        yAxis = d3.svg. axis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d, i) {
                   if(typeof displayY !=="undefined" && displayY =="Yes"){
                     if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding);
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
    svg = d3.select("#" + div).append("svg")
            .attr("width", width )
            .attr("height", height + margin.top + margin.bottom +40 )
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
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(i);
//                            colorShad = "steelblue";
                        }
                    }
                }
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//colorShad="steelblue";
                return colorShad;
            })
            .attr("stop-opacity", 1);
//    parent.$("#colorMap").val(JSON.stringify(colorMap));

//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
    var max = maximumValue(data, measure1);
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
    var yMin = 0;
    yMin = minVal;
    y.domain([yMin, max]);
    svg.call(yAxis1);
    var diffFactor = parseInt(measArr[0] - parseInt(measArr[1]));
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
    for (var j=0; j <= height; j=j+50) {
    svg.append("svg:line")
        .attr("x1", 0)
        .attr("y1", j)
        .attr("x2", width)
        .attr("y2", j)
        .style("stroke", "#A69D9D")
        .style("stroke-width", .5)
        .style("z-index", "9999");
};

    svg.append("g")
            .attr("class", ".x axis")
            .attr("transform", "translate(0," + (height + 3) + ")")
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
               if(typeof displayX !=="undefined" && displayX =="Yes"){
                if (d.length < 15){
                    return d;
                }
                else {
                    return d.substring(0, 15) + "..";
                }
               }else {
                   return "";
               }
                
            })
            .attr('transform', 'rotate(-45)')
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
            .enter().append("g")
            .attr("class", "bar")
            .append("rect")
            .attr("rx", barRadius)
            .attr("fill", function(d,i) {
                 var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }

                return color(i);//"url(#gradientBar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
            .attr("x", function(d) {
                return x(d[columns[0]]);
            })
            .attr("width", x.rangeBand())
            .attr("y", function(d) {
                return y(d[measure1]);
            })
            .attr("height", function(d) {
                return height - y(d[measure1]);
            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
//                }
//                if (toolTipType === "customize") {
//                    for (var num in toolTipData) {
//                        if (d[columns[0]] === toolTipData[num][columns[0]]) {
//                            break;
//                        }
//                    }
//                } else {

//                    show_details(d, columns, measureArray, this,div);
//                }
            })
            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
//                }
                hide_details(d, i, this);
            });
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
                .attr("fill", labelColor)
                .text(function(d)
                {
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding);
                    }else{
                    var percentage = (d[measureArray[0]] / parseInt(sum)) * 100;
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

}
function buildMultiMeasureTrLine5(divappend,div, data,chartData, columns, measureArray, divWid, divHgt) {
 var color = d3.scale.category10();
//     divWid=parseInt($(window).width())*(.35);
//alert(JSON.stringify(chartData[div]["yAxisFormat"]))
    var measure1 = measureArray[0];
    var autoRounding1;
    var measArr = [];
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
//       var chartData = JSON.parse($("#chartData").val());
//       var colIds = chartData[div]["viewIds"];
       var prop = graphProp(chartData,div);

    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
    },
//    width = parseInt(divWid)-94, //- margin.left - margin.right
//    height = parseInt(divHgt)* .55;
 width = divWid- margin.left - margin.right, //- margin.left - margin.right
            height = parseInt(divHgt)* .65;
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);
    var y = d3.scale.linear().domain([minVal, max]).range([height, 0]);
    // Axis setting
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
//                    return numberFormat(d, round, precition);
                        return addCommas(d);
                });

    }
    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d, i) {
                     if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding);
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
    var svg = d3.select("#" + divappend).append("svg")
            .attr("width", width + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
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

for (var j=0; j <= height; j=j+50) {
    svg.append("svg:line")
        .attr("x1", 0)
        .attr("y1", j)
        .attr("x2", width)
        .attr("y2", j)
        .style("stroke", "#A69D9D")
        .style("stroke-width", .5)
        .style("z-index", "9999");
};
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");

var len = 0;
var yvalue = -75;
var dyvalue = ".41em";
var count = 0;
var transform = "";
 for(var i in  measureArray){
//     alert(measureArray[i])
    if(count % 2==0){
        if(len==0){
            len=100;
            yvalue=400;
        dyvalue = -150;
        }else{
        len += 100;
//        yvalue=500;
        yvalue=height*.9;
        dyvalue = -150;
     }
    }else {
        len +=100;
//        yvalue=518;
        yvalue=400;
         dyvalue = -150;
     }
   svg.append("g")
            .attr("class", "y axis")
            .append("text")
            .attr("transform", transform)
            .attr("x",len)
            .attr("y",yvalue)
            .attr("fill", color(i))
//            .style("stroke", color(i))
            .attr("dy",dyvalue )
            .style("text-anchor", "end")
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(measureArray[i].length>25){
                    return measureArray[i].substring(0, 25);
                }else {
//                   alert("mmm "+measureArray[i])
                   return measureArray[i];
          }
           })
              count++


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
    y.domain([0, Math.max.apply(Math, min1)]);
     svg.call(yAxis1);
    var diffFactor = parseInt(measArr[0] - parseInt(measArr[1]));
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


    svg = d3.select("#" + divappend).select("g");
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
              }
           else{
                    flag = numberFormat(d,yAxisFormat,yAxisRounding);
                }
              }
           else{
           flag =  measure1;
           }
          return flag
             });

    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d) {
                if(typeof d!="undefined"){
                if (d.length < 13)
                    return d;
                else
                    return d.substring(0, 10) + "..";
            }
            return "";
            })
            .attr('transform', 'rotate(-25)')
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
    // Add the line 1
    for (var i = 0; i < measureArray.length; i++) {
        j = i;
        svg.append("path")
                .data(data)
                .attr("d", valueline(data))
                .attr("fill", color(i)).style("stroke-width", "3px")
                .style("stroke", color(i));
        colorArr.push(color(i));
        if (typeof chartMap[measureArray[i]] === "undefined") {
            chartMap[measureArray[i]] = color(i);
            colorMap[i] = measureArray[i] + "__" + color(i);
        }
        parent.$("#colorMap").val(JSON.stringify(colorMap));
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
                .style("fill", color(i))
                .style("stroke", color(i))
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]];
                })
                .attr("onclick", fun)
                .on("mouseover", function(d, i) {
                    show_details(chartData,d, columns, measureArray, this,div);
                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
    }
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
//    var ageNames1 = [];
//    showLegends2(measureArray, colorArr, width, svg);
    }
function buildLine5(divappend,div, data,chartData, columns, measureArray, divWid, divHgt) {
//    divHgt=450;
var color = d3.scale.category10();
 var measArr = [];
    var measure1 = measureArray[0];
//    alert("measure1 "+measure1)
//    divWid=parseInt($(window).width())*(.35);
    var chartMap = {};
//        var chartData = JSON.parse(parent.$("#chartData").val());
var colIds = chartData[div]["viewIds"];
//var colIds = chartData["chart1"]["viewIds"];
    var autoRounding1;
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
    var isDashboard = parent.$("#isDashboard").val();
   var fun = "drillWithinchart(this.id,\""+div+"\")";
    var botom = 70;
    var j = 0;
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//        botom = 70;
//    }
var prop = graphProp(chartData,div)
    var margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 80
    },
    width = parseInt(divWid)-85, //- margin.left - margin.right
            height = parseInt(divHgt)* .65;
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);
    var y = d3.scale.linear().domain([minVal, max]).range([height, 0]);

    // Axis setting
    var xAxis = d3.svg.axis()
            .scale(x)
            .orient("bottom");
    if (isFormatedMeasure) {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d) {
                   if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
//                    return numberFormat(d,yAxisFormat,yAxisRounding);
return addCommas(d)
                }
                });

    }
    else {
        yAxis = d3.svg.trendaxis()
                .scale(y)
                .orient("left")
                .tickFormat(function(d, i) {
                    if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return addCommas(d)
//                    (d,yAxiddCommassFormat,yAxisRounding);
//                    return numberFormat(d,yAxisFormat,yAxisRounding);
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

    var valueline = d3.svg.line()
            .x(function(d) {
                return x(d[columns[0]]);
            })
            .y(function(d) {
                return y(d[measure1]);
            });

    svg = d3.select("#" + divappend).append("svg")
            .attr("width", width + margin.left + margin.right)
//            .attr("width", width )
            .attr("height", height + margin.top + margin.bottom+17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
//    svg.append("svg:rect")
//            .attr("width", width)
//            .attr("height", height)
//            .attr("onclick", "reset()")
//            .attr("class", "background");

    svg.append("g")
    .append("svg:svg")
//    .style("margin-left", "3em")
    .attr("width", width-margin.left-margin.right)
    .attr("height", height-margin.left-margin.right);


for (var j=0; j <= height; j=j+50) {
    svg.append("svg:line")
        .attr("x1", 0)
        .attr("y1", j)
        .attr("x2", width)
        .attr("y2", j)
        .style("stroke", "#A69D9D")
        .style("stroke-width", .5)
        .style("z-index", "9999");
};

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
            .style("text-anchor", "end")
            .text("" + measure1 + "");

    var min1 = [];
    var flag = "";
    for (var key in data) {
        min1.push(data[key][measure1]);

    }
    x.domain(data.map(function(d) {
        return d[columns[0]];
    }));
     y.domain([minVal, Math.max.apply(Math, min1)]);

      svg.call(yAxis1);
    var diffFactor = parseInt(measArr[0] - parseInt(measArr[1]));
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
    svg = d3.select("#" + divappend).select("g");
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
              }
           else{
                    flag = addCommas(d)
//                    ,yAxisFormat,yAxisRounding);
//                    flag = numberFormat(d,yAxisFormat,yAxisRounding);
                }
              }
           else{
           flag =  measure1;
           }
          return flag
             });

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

    svg.append("path")
            .data(data)
            .attr("d", valueline(data))
            .attr("fill", "transparent")
            .style("z-index", "0")
            .attr("stroke", function(d, i) {
                var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
                }
                else {
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = "steelblue";
                        }
//                return "steelblue";
                    }
                }
                chartMap[d[columns[0]]] = colorShad;
                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return color(0);
            })
            .style("stroke-width", "3px")
            .style("stroke", color(0));
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
            .style("fill", "steelblue")
            .style("stroke", function(d, i) {
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
//                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                            colorShad = centralColorMap[d[colIds[0]].toString().toLowerCase()];
//                        } else {
//                            colorShad = color(i);
                            colorShad = "steelblue";
//                        }
//                    }
//                }
                return colorShad;
            })
            .style("stroke-width", "2px")
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measure1];
            })
            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                show_details(chartData,d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                hide_details(d, i, this);
            });
//    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
}
function buildHalfDonut5(divappend,div, data,chartData, columns, measureArray, divWidth, divHeight) {
//    var rad=(divWidth/(3.5))*1.5
    var rad=rad=divHeight*.65;
    var color = d3.scale.category10();
//   divWidth=parseInt($(window).width())*(.35);
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
//var chartData = JSON.parse($("#chartData").val());
//var colIds = chartData[div]["viewIds"];
var colIds = chartData[div]["viewIds"];
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var width = divWidth;
    var margintop;
    var height = Math.min(divWidth, divHeight);
//    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var margintop;
    pi = Math.PI;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
        margintop = "30px";
        var radius = rad;
        height = Math.min(width, height);
        width = Math.min(width, height);
        radius = (Math.min(width, height) / 2.7);
    }
    else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
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
            }).startAngle(-90 * (pi/180))
        .endAngle(90 * (pi/180));
    var svg = d3.select("#" + divappend).append("svg:svg")
            .datum(data)
            .attr("width", divWidth)
//            .attr("height", height-150);
            .attr("height", height*.8);

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
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                } else
                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                } else {
                    colorShad = color(i);
                }
                chartMap[d[columns[0]]] = colorShad;
                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", divWidth)
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
//            .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
            .attr("transform", "translate(" + width / 2 + "," + height*.6 + ")");
    arcs.append("path")
            .attr("fill", function(d) {
                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                }
                var columnList = [];
                columnList.push(columns[0]);
                show_details(chartData,d.data, columnList, measureArray, this,div);

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

    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("style", "font-family: lucida grande")
            .attr("style", "font-size: 10px")
            .attr("transform", function(d) { //set the label's origin to the center of the arc
                //we have to make sure to set these before calling arc.centroid
                d.outerRadius = radius; // Set Outer Coordinate
                var a = angle(d);
                if (a < -90) {
                    a = a + 180;
                    d.innerRadius = radius / 2;
                } else {
                    d.innerRadius = radius / 1.3;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .text(function(d) {

                var percentage = (d.value / parseInt(sum)) * 100;
                return percentage.toFixed(1) + "%"

            });
    var center_group = svg.append("svg:g")
            .attr("class", "ctrGroup")
            .attr("transform", "translate(" + (width / 2) + "," + (width / 2) + ")");
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
var fwidt=($("#"+divappend).width()),
fontSize1 = (fwidt*.022);
      var html ="<div align='center' style='height:"+(divHeight*.2)+"px;width:"+width+"px;overflow:auto'><table style=''><tr style=\'white-space: nowrap;\'>";
     for(var m=0;m<(data.length < 5 ? data.length : 5);m++){
//     for(var m=0;m<data.length;m++){
    html +="<td style='width:"+data[m][columns[0]].length+"px;whitespace:nowrap;font-size:"+fontSize1+"'><canvas width='6' height='6' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+";white-space:nowrap'> "+data[m][columns[0]]+"</span> </td>";
    }
    html +="</tr><table></div>";
    $("#"+divappend).append(html);
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
} 
function buildPie5(divappend,div, data,chartData, columns, measureArray,wid,hgt) {
//alert("col  "+JSON.stringify(data))
//alert("ddd "+data.length)
     var color = d3.scale.category10();
    var divWidth, divHeight, rad;

    var w = $(window).width()/2+"px";
    divWidth=wid;
//     divWid=parseInt($(window).width())*(.35);
     divWid=wid;
//    divHeight=400;
    divHeight=hgt;
//    rad=divHeight*.50;
    rad=(Math.min((divWidth*.6), divHeight))*.50;
    var autoRounding1 = "1d";
//var chartData = JSON.parse(parent.$("#chartData").val());
var colIds = chartData[div]["viewIds"];

    var prop = graphProp(chartData,div);
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
//    var width = Math.min(divWidth, divHeight);
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
    svg = d3.select("#" + divappend).append("svg:svg")
            .datum(data)
            .attr("width", width*.6)
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
            .attr("onclick", fun);

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
                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(chartData,d.data, columnList, measureArray, this,div);

            })
            .on("mouseout", function(d, i) {
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", color(i));
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
            .style("fill", function(d,i){
                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
                return "white"
            else
                return "black"
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
//                var percentage = (d.value / parseInt(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof dataDisplay !=="undefined" && dataDisplay === "Yes")  {
if (typeof displayType !== "undefined" && displayType === "Absolute") {
//                    if (isFormatedMeasure) {
//                        return numberFormat(d.data[measureArray[0]], round, precition);
//                    }
//                    else {
//                        return autoFormating(d.data[measureArray[0]], autoRounding1);
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding);
////                    }
                }
                else {
                   var percentage = (d.value / parseInt(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
            }else {
                var percentage1 = (d.value / parseInt(sum)) * 100;
                return percentage1.toFixed(1) + "%";
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


     var html ="";
//var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var colName=columns[0];
//            if(viewOvIds.indexOf(columns[0])!="undefined"){
//                colName=viewOvName[viewOvIds.indexOf(columns[0])];
//            }
//    if(typeof displayLegend !=="undefined" && displayLegend =="Yes"){
    if(columns[0].length > 15){
          html ="<div style='height:"+divHeight+"px;width:"+width*.38+"px;float:right;overflow-y:auto'><table style='float:left;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span title='"+columns[0]+"' style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0].substring(0,15)+"..</span></td></tr>";
     }else {
          html ="<div style='height:"+divHeight+"px;float:right;overflow-y:auto'><table style='float:left;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
    }

//if()
//alert("ddd "+data.length)
var fwidt=($("#"+divappend).height()/(data.length+2)),
fontSize1 = (fwidt*.5);
    for(var m=0;m<data.length;m++){
        if(data[m][columns[0]].length > 13 ) {
            if(typeof colorLegend !=="undefined" && colorLegend =="Default")
    html +="<tr style='height:"+fwidt+"px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span title='"+data[m][columns[0]]+"' style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 13)+"..</span> </td></tr>";
    else
    html +="<tr style='height:"+fwidt+"px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span title='"+data[m][columns[0]]+"' style='margin-left:5px;color:black'> "+data[m][columns[0]].substring(0, 13)+"..</span> </td></tr>";
    }
else {
     if(typeof colorLegend !=="undefined" && colorLegend =="Default")
    html +="<tr style='height:"+fwidt+"px;white-space:nowrap;font-size:"+fontSize1+"px'><td style='whitespace:nowrap'><canvas width='"+fontSize1/2+"' height='"+fontSize1/2+"' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
   else
   html +="<tr style='height:"+fwidt+"5px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:black'> "+data[m][columns[0]]+"</span> </td></tr>";
}
}
    html +="</table></td></tr></table></div>";
//    } else {
    //        html ="";
    //    }
//    $("#legendsTable").append(html);
    $("#"+divappend).append(html);
 
}
function buildHorizontalBar5(divappend,div, data,chartData, columns, measureArray, divWid, divHgt) {

//   alert(columns)
     var color = d3.scale.category10();
    var chartMap = {};
//var chartData = JSON.parse(parent.$("#chartData").val());
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
//     divWid=parseInt($(window).width())*(.35);
    var wid = divWid;
    var hgt = divHgt;
    var w = wid - 200;
    var h = hgt-50 ;
     var prop = graphProp(chartData,div);
    //    var color = d3.scale.linear()
    //    .domain([d3.min(data, function(d) {
    //        return d[measureArray[0]];
    //    }), d3.max(data, function(d) {
    //        return d[measureArray[0]];
    //    })])
    //    .range(["red", "green"])
    //    .interpolate(d3.interpolateHsl);
    var max = maximumValue(data, measureArray[0]),
            num_ticks = 1,
            left_margin = 100,
            right_margin = 100,
            top_margin = 30,
            bottom_margin = 0;
    //    color1 = function(id) {
    //        return 'steelblue'
    //    };
 
  
var colIds = chartData[div]["viewIds"];
    var measArr = [];
//    xAxis = d3.svg.trendaxis()
//            .scale(x)
//            .orient("bottom");
    var x = d3.scale.linear()
            .domain([0, max])
//            .range([0, w - (left_margin + right_margin)]),
            .range([0, w + 50]),
      y = d3.scale.ordinal()
            .domain(d3.range(data.length))
            .rangeBands([bottom_margin, h - top_margin], .5);
  var xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");

//    yAxis = d3.svg.trendaxis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d, i) {
//                    return addCommas(d);
//                });
    var chart_top = h - y.rangeBand() / 2 - top_margin;
    var chart_bottom = bottom_margin + y.rangeBand() / 2;
    var chart_left = left_margin;
    var vis = d3.select("#"+divappend).append("svg:svg")
            .attr("width", divWid)
            .attr("height", divHgt)
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
                var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
                } else {
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = "steelblue";
                        }
//                return "steelblue";
                    }
                }
                chartMap[d[columns[0]]] = colorShad;
                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);



    parent.$("#colorMap").val(JSON.stringify(colorMap));
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
                } else {
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]].trim()) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(i);
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


     for (var j=150; j < divWid; j=j+50) {
        vis.append("svg:line")
//    .attr("transform", "translate(0," + w + ",0)")
        .attr("x1", j)
        .attr("y1", chart_bottom)
        .attr("x2", j)
        .attr("y2", chart_top)
        .style("stroke", "#A69D9D")
        .style("stroke-width", 1)

//        .style("z-index", "9999");
};


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
//            .attr("transform", "translate(100," +w+ ")")
            .attr("transform", "translate(100," +h+ ")")
            .attr("x", 5)
           .call(xAxis)

            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
//            if(typeof displayX !=="undefined" && displayX=="Yes"){
//             if(yAxisFormat==""){
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
//                    }
//            else{
                    return addCommas(d)
//                    ,yAxisFormat,yAxisRounding);
//                }
//            }else{
//                return "";
//            }
            })
            .attr('transform', 'rotate(-45)')

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
            .attr("width", function(d) {
                return (x(d[measureArray[0]]))

            })


            .attr("height", y.rangeBand()+7)
            .attr("fill", function(d,i) {
               var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }

                return color(i);
//               return "url(#gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("stroke", function(d) {
                return "url(#gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
             .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[0]];
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })

            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                 var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(chartData,d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                 var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", color(i));
                hide_details(d, i, this);

            })

    var labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("class", "x.axis")
            .attr("x", 5)
            .attr("y", 30)
            .text(function(d, i) {
//                if(typeof displayY !=="undefined" && displayY =="Yes"){
                if (data[i][columns[0]].length < 12)
                    return data[i][columns[0]];
                else
                    return  data[i][columns[0]].substring(0, 12) + "..";
//                }else {
//                    return "";
//                }
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

    var bbox = labels.node().getBBox();
    vis.selectAll(".label")
            .attr("transform", function(d) {
                return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 4) + ")";
            });
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    labelColor = "black";

    var rightOffset = wid + 160;
    labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("transform", function(d) {
                var xvalue = (x(d[measureArray[0]]) - 80) < 20 ? x(d[measureArray[0]]) + 119 : x(d[measureArray[0]]) + 119;
                return "translate(" + xvalue + ", " + (y.rangeBand() / 2) + ")";
            })
            .attr("text-anchor", "middle")
            .attr("class", "valueLabel")
            .attr("fill", labelColor)
            .text(function(d)
            {
               if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding);
                    }else{
                    var percentage = (d[measureArray[0]] / parseInt(sum)) * 100;
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

 
}
function bubble5(divappend,div, data0,chartData, columns, measures, divWidth, divHeight, layout) {
     var color = d3.scale.category10();
//     divWidth=parseInt($(window).width())*(.35);
     divWidth=divWidth;
    var nodes = [];
    var data = [];
    var measure1 = measures[0];

//    var chartData = JSON.parse($("#chartData").val());
var colIds = chartData[div]["viewIds"];
//var colIds = chartData["chart1"]["viewIds"];
    var chartMap = {};
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    svg = d3.select("#" + divappend).append("svg")
            .attr("width", divWidth - layout)
            .attr("height", divHeight  )
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
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                    colorShad = drillShade;
                } else {
                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
                    } else {
                        colorShad = color(i);
                    }
                }
                if (typeof chartMap[d.name] === "undefined") {
                    colorMap[i] = d.name + "__" + colorShad;
                    chartMap[d.name] = colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));

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
                    return parseInt(d.r)/2;
                }else {
                return d.r;
                }

            })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("fill", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("id", function(d) {
                return d.name + ":" + d.value;
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
                d3.select(this).attr("stroke", "grey");
                var content = "";
                for (var no = 0; no < measures.length; no++) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.value, round, precition);
                    }
                    else {
                        if (no === 0) {
                            msrData = addCommas(d.value);
                        } else {
                            msrData = addCommas(d["value" + no]);
                        }

                    }
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
            .attr("fill", labelColor)
            .text(function(d) {
                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
                    return d.name.substring(0, d.r / 5);
                }
                else {
                    return d.name1.substring(0, d.r / 5);
                }
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

}



function bubblePortal(div, data0, columns, measures, divWidth, divHeight, layout) {

//     alert("uuuuw"+divWidth)
//     alert("uuuuh"+divHeight)
     var color = d3.scale.category10();
   //  divWidth=parseInt($(window).width())*(.35);
    var nodes = [];
    var data = [];
    var measure1 = measures[0];
//    var chartData = JSON.parse($("#chartData").val());
//var colIds = chartData[div]["viewIds"];
var colIds = "";
    var chartMap = {};
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
    var barsWidthTotal = $(window).width() -    400;
    var legendOffset = 60;
    var legendBulletOffset = 130;
    var legendTextOffset = 20;
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    svg = d3.select("#" + div).append("svg")
//            .attr("width", divWidth - layout)
            .attr("width", divWidth)
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
//    .attr("stop-color", function(d, i) {
//        var colorShad;
//        var drilledvalue = parent.$("#drilledValue").val();
//        if (isShadedColor) {
//            colorShad=color(d[shadingMeasure]);
//        }else if(conditionalShading){
//            return getConditionalColor(color(i),d[conditionalMeasure]);
//        }else if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi')) !== -1) {
//            colorShad=drillShade;
//        } else{
//            colorShad=color(i);
//        }
//        colorMap[i]=d.name+"__"+colorShad;
//        return colorShad;
//    })
            .attr("stop-color", function(d, i) {
                var colorShad;
//        var drilledvalue = parent.$("#drilledValue").val().split(",");
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                    colorShad = drillShade;
                } else {
                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
                    } else {
                        colorShad = color(i);
                    }
                }
                if (typeof chartMap[d.name] === "undefined") {
                    colorMap[i] = d.name + "__" + colorShad;
                    chartMap[d.name] = colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", divWidth)
//            .attr("height", divHeight - 50)
////            .attr("style", "margin-top:3px;")
//            .attr("onclick", "reset()")
//            .attr("class", "background");
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
                    return parseInt(d.r)/2;
                }else {
                return d.r;
                }

            })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.name.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("fill", function(d, i) {
                return "url(#gradient" + (d.name).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("id", function(d) {
                return d.name + ":" + d.value;
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
                d3.select(this).attr("stroke", "grey");
                var content = "";
                for (var no = 0; no < measures.length; no++) {
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(d.value, round, precition);
                    }
                    else {
                        if (no === 0) {
                            msrData = addCommas(d.value);
                        } else {
                            msrData = addCommas(d["value" + no]);
                        }

                    }
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
            .attr("fill", labelColor)
            .text(function(d) {
                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
                    return d.name.substring(0, d.r / 5);
                }
                else {
                    return d.name1.substring(0, d.r / 5);
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
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
}

function buildPie1(div, data, columns, measureArray,wid,hgt) {
    
     var color = d3.scale.category10();
    var divWidth, divHeight, rad;
    
//    var w = $(window).width()/2+"px";
    divWidth=wid;
//     divWid=parseInt($(window).width())*(.35);
     divWid=wid;
    divHeight=hgt;
    rad=divHeight*.50;
    var autoRounding1 = "1d";
//var chartData = JSON.parse(parent.$("#chartData").val());
//var colIds = chartData[div]["viewIds"];
var colIds ="";
//    var prop = graphProp(div);
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
            .attr("width", width*.7)
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
            .attr("onclick", fun);

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
                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(d.data, columnList, measureArray, this,div);

            })
            .on("mouseout", function(d, i) {
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", color(i));
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
            .style("fill", function(d,i){
                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
                return "white"
            else
                return "black"
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
//                var percentage = (d.value / parseInt(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof dataDisplay !=="undefined" && dataDisplay === "Yes")  {
if (typeof displayType !== "undefined" && displayType === "Absolute") {
//                    if (isFormatedMeasure) {
//                        return numberFormat(d.data[measureArray[0]], round, precition);
//                    }
//                    else {
//                        return autoFormating(d.data[measureArray[0]], autoRounding1);
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding);
////                    }
                }
                else {
                   var percentage = (d.value / parseInt(sum)) * 100;
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


     var html ="";
//var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//    var measName = JSON.parse(parent.$("#measure").val());
//    var measIds = JSON.parse(parent.$("#measureIds").val());
    var colName=columns[0];
//            if(viewOvIds.indexOf(columns[0])!="undefined"){
//                colName=viewOvName[viewOvIds.indexOf(columns[0])];
//            }
    if(typeof displayLegend !=="undefined" && displayLegend =="Yes"){
    if(columns[0].length > 15){
          html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span title='"+columns[0]+"' style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0].substring(0,15)+"..</span></td></tr>";
     }else {
          html ="<div style='height:"+height+"px;float:right;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.25+"px;'><tr><td><span style='font-size:14px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:5px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
    }

//if()
    for(var m=0;m<data.length;m++){
        if(data[m][columns[0]].length > 13 ) {
            if(typeof colorLegend !=="undefined" && colorLegend =="Default") 
    html +="<tr style='height:25px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span title='"+data[m][columns[0]]+"' style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 13)+"..</span> </td></tr>";
    else
    html +="<tr style='height:25px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span title='"+data[m][columns[0]]+"' style='margin-left:5px;color:black'> "+data[m][columns[0]].substring(0, 13)+"..</span> </td></tr>";
    }
else {
     if(typeof colorLegend !=="undefined" && colorLegend =="Default") 
    html +="<tr style='height:25px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
   else
   html +="<tr style='height:25px;white-space:nowrap'><td style='whitespace:nowrap'><canvas width='12' height='12' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:black'> "+data[m][columns[0]]+"</span> </td></tr>";
}
}
    html +="</table></td></tr></table></div>";
    } else {
        html ="";
    }
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








function hide_details(data, i, element) {
    d3.select(element).attr("stroke", "");
    return tooltip.hideTooltip();
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

function buildHorizontalBar(div, data, columns, measureArray, divWid, divHgt) {
   
     var color = d3.scale.category10();
    var chartMap = {};
var chartData = JSON.parse(parent.$("#chartData").val());
    var isDashboard = parent.$("#isDashboard").val();
    var fun = "drillWithinchart(this.id,\""+div+"\")";

    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
     divWid=parseInt($(window).width())*(.35);
    var wid = divWid;
    var hgt = divHgt;
    var w = wid - 150;
    var h = hgt - 100;
     var prop = graphProp(div);
    //    var color = d3.scale.linear()
    //    .domain([d3.min(data, function(d) {
    //        return d[measureArray[0]];
    //    }), d3.max(data, function(d) {
    //        return d[measureArray[0]];
    //    })])
    //    .range(["red", "green"])
    //    .interpolate(d3.interpolateHsl);
    var max = maximumValue(data, measureArray[0]),
            num_ticks = 1,
            left_margin = 100,
            right_margin = 100,
            top_margin = 30,
            bottom_margin = 0;
    //    color1 = function(id) {
    //        return 'steelblue'
    //    };
 
  
var colIds = chartData[div]["viewIds"];
    var measArr = [];
//    xAxis = d3.svg.trendaxis()
//            .scale(x)
//            .orient("bottom");
    var x = d3.scale.linear()
            .domain([0, max])
//            .range([0, w - (left_margin + right_margin)]),
            .range([0, w + 50]),
      y = d3.scale.ordinal()
            .domain(d3.range(data.length))
            .rangeBands([bottom_margin, h - top_margin], .5);
   xAxis = d3.svg.trendaxis()
            .scale(x)
            .orient("bottom");

//    yAxis = d3.svg.trendaxis()
//                .scale(y)
//                .orient("left")
//                .tickFormat(function(d, i) {
//                    return addCommas(d);
//                });
    var chart_top = h - y.rangeBand() / 2 - top_margin;
    var chart_bottom = bottom_margin + y.rangeBand() / 2;
    var chart_left = left_margin;
    var vis = d3.select("#"+div).append("svg:svg")
            .attr("width", divWid)
            .attr("height", divHgt-50)
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
                var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
                } else {
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = "steelblue";
                        }
//                return "steelblue";
                    }
                }
                chartMap[d[columns[0]]] = colorShad;
                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);



    parent.$("#colorMap").val(JSON.stringify(colorMap));
    gradient.append("svg:stop")
            .attr("offset", "9%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "80%")
            .attr("stop-color", function(d, i) {
                var colorShad;
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor("steelblue", d[conditionalMeasure]);
                } else {
                    var drilledvalue = parent.$("#drilledValue").val();
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.trim().length > 0 && drilledvalue.indexOf(d[columns[0]].trim()) !== -1) {
                        colorShad = drillShade;
                    } else {
                        if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                            colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                        } else {
                            colorShad = color(i);
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


     for (var j=150; j < divWid; j=j+50) {
        vis.append("svg:line")
//    .attr("transform", "translate(0," + w + ",0)")
        .attr("x1", j)
        .attr("y1", chart_bottom)
        .attr("x2", j)
        .attr("y2", chart_top)
        .style("stroke", "#A69D9D")
        .style("stroke-width", 1)

//        .style("z-index", "9999");
};


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
            .attr("transform", "translate(100," +w+ ")")
            .attr("x", 5)
           .call(xAxis)

            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
            if(typeof displayX !=="undefined" && displayX=="Yes"){
             if(yAxisFormat==""){
                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
                    }
            else{
                    return numberFormat(d,yAxisFormat,yAxisRounding);
                }
            }else{
                return "";
            }
            })
            .attr('transform', 'rotate(-45)')

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
            .attr("width", function(d) {
                return (x(d[measureArray[0]]))

            })


            .attr("height", y.rangeBand()+7)
            .attr("fill", function(d,i) {
               var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    }

                return color(i);
//               return "url(#gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
            .attr("stroke", function(d) {
                return "url(#gradientHbar_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
            })
             .attr("index_value", function(d, i) {
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[0]];
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
            })

            .attr("onclick", fun)
            .on("mouseover", function(d, i) {
                 var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                show_details(d, columns, measureArray, this,div);
            })
            .on("mouseout", function(d, i) {
                 var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);

                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", color(i));
                hide_details(d, i, this);

            })

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

    var bbox = labels.node().getBBox();
    vis.selectAll(".label")
            .attr("transform", function(d) {
                return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 4) + ")";
            });
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    labelColor = "black";

    var rightOffset = wid + 160;
    labels = vis.selectAll("g.bar")
            .append("svg:text")
            .attr("transform", function(d) {
                var xvalue = (x(d[measureArray[0]]) - 80) < 20 ? x(d[measureArray[0]]) + 119 : x(d[measureArray[0]]) + 119;
                return "translate(" + xvalue + ", " + (y.rangeBand() / 2) + ")";
            })
            .attr("text-anchor", "middle")
            .attr("class", "valueLabel")
            .attr("fill", labelColor)
            .text(function(d)
            {
               if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
                    
                    if(typeof displayType=="undefined" || displayType==="Absolute"){
                        
                       return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding);
                    }else{
                    var percentage = (d[measureArray[0]] / parseInt(sum)) * 100;
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
            htmlstr += "<tr class=\"tdClass1\"><td align='left'>" + name.trim() + "</td>";
        }
        else {
            htmlstr += "<tr class=\"tdClass2\"><td align='left'>" + name.trim() + "</td>";
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


function buildTable(chartId, dataPath, viewbys, measures, divwidth, divHght) {
    var measure = measureArray[0];
    var autoRounding;
    if (columnMap[measure] !== "undefined" && columnMap[measure] !== undefined && columnMap[measure]["rounding"] !== "undefined") {
        autoRounding = columnMap[measure]["rounding"];
    } else {
        autoRounding = "1d";
    }
    d3.json(dataPath, function(error, data0) {

        var mindata = [];
        var trWidth = divwidth / 8;
        var html = "";
        html = html + "<div id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; overflow-y: auto;overflow-x: hidden\">";
        html = html + "<div>";
        html = html + "<table id=\"chartTable" + chartId + "\" width=\"" + (divwidth - 10) + "\" align=\"center\" style=\"height: 25;border-collapse:collapse;font-size:10px;\">";
        html = html + "<tr><th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=\"25px\">S.no</td>";
        html = html + "<th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=" + trWidth + ">" + viewbys[0] + "</td>";
        html = html + "<th  style=\"background-color: red; background: linear-gradient(to bottom, #D5E3E4 0%, #FFFFF0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;\" width=" + trWidth + ">" + measures[0] + "</td><tr>";
        data0.forEach(function(d, i) {
            var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "Gainsboro";
            } else {
                color = "GhostWhite";
            }
            //                  if(i<=6)
            //                  {
            html = html + "<tr><td  style=\"background-color:" + color + ";\" width=25px>" + (i + 1) + "</td>";
            html = html + "<td  style=\"background-color:" + color + ";\" width=" + trWidth + " >" + d[viewbys[0]] + "</td>";
            html = html + "<td  style=\"background-color:" + color + ";\" width=" + trWidth + ">" + autoFormating(d[measure], autoRounding) + "</td>";
            html = html + "<td  width=\"10px\">&nbsp;</td><tr>";
            //                  }

        });
        html = html + "</table></div></div>";

        for (var i = 0; i < data0.length; i++) {
            if (i < 15) {
                mindata.push(data0[i]);
            } else {
                break;
            }
        }
        //background: linear-gradient(to bottom, #D5E3E4 0%, #CCDEE0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent

        $("#" + chartId).html(html);

    });
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

function mouseHoverEvent(content) {
    var svgContent = d3.selectAll("." + content);
    d3.selectAll("." + content).attr("fill", drillShade);
}
function mouseOutEvent(content) {
    var svgContent = d3.selectAll("." + content);
    d3.selectAll("." + content).attr("fill", drillShade);
}




function mapPopUpDiv(id, name) {
    if (id !== "false") {
        mapPopUp(id, name);
    }
}










function setMouseOverEvent(id) {
    d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", drillShade);
    var chartType = JSON.parse(parent.$("#chartList").val());
    if (chartType[0] === 'Split-Graph') {
        synchronizedMouseOverSplit(id);
    }
}
function setMouseOutEvent(id) {
    try {
        var chartColorValue = d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).attr("color_value");
    } catch (e) {
    }
    try {
        d3.selectAll(".bars-Bubble-index-" + id.replace(/[^a-zA-Z0-9]/g, '', 'gi')).style("fill", chartColorValue);
    } catch (e) {
    }

}

function isShortForm(d, countryName) {
    var flag = false;
    if ((d.properties.name === "United States" && (countryName.toLowerCase().trim() === "united states of america" || countryName.toLowerCase().trim() === "usa")) || (d.properties.name === "United Arab Emirates" && (countryName.toLowerCase().trim() === "uae")) || (d.properties.name === "Germany" && (countryName.toLowerCase().trim() === "ger"))
            || (d.properties.name === "Sri Lanka" && (countryName.toLowerCase().trim() === "srl")) || (d.properties.name === "Thailand" && (countryName.toLowerCase().trim() === "thl"))) {
        flag = true;
    }
    return flag;
}

var colVal;




function maximumValue(data, measure) {
//   alert(JSON.stringify(data[0]["Gross Sales"]))
//  alert(JSON.stringify(data))
//   alert("ll"+measure)
//   alert("llen "+data.length)
   var max;
    for (var j = 0; j < data.length; j++) {

        if (j === 0) {
            max = data[j][measure];
        } else {
//            alert(parseInt(data[j][measure]))
            if (max < parseInt(data[j][measure])) {
                max = data[j][measure];
            }
        }
//        alert("mm"+max)
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
    if (x2 === ".00") {
        x2 = "";
    }
    return x1 + x2;
}

function drillWithinchart(id,div){
   
    parent.drillWithinchart1(id,div)
}


function buildHalfDonut1(div, data, columns, measureArray, divWidth, divHeight, rad) {
     var color = d3.scale.category10();
//   divWidth=parseInt($(window).width())*(.35);
//    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
//    var chartType = parent.$("#chartType").val();
    var chartType ="";
    if (chartType === "dashboard") {
        isDashboard = true;
    }
//var chartData = JSON.parse($("#chartData").val());
//var colIds = chartData[div]["viewIds"];
var colIds = "";
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        fun = "drillChartInDashBoard(this.id,'" + div + "')";
    }
    var width = divWidth;
    var margintop;
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var margintop;
    pi = Math.PI;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
        margintop = "30px";
        var radius = rad;
        height = Math.min(width, height);
        width = Math.min(width, height);
        radius = (Math.min(width, height) / 2.7);
    }
    else if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
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
            }).startAngle(-90 * (pi/180))
        .endAngle(90 * (pi/180));
    var svg = d3.select("#" + div).append("svg:svg")
            .datum(data)
            .attr("width", divWidth)
            .attr("height", height-150);

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
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
                    colorShad = drillShade;
                } else
                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
                } else {
                    colorShad = color(i);
                }
                chartMap[d[columns[0]]] = colorShad;
                colorMap[i] = d[columns[0]] + "__" + colorShad;
                return colorShad;
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
//            .attr("width", divWidth)
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
            .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
    arcs.append("path")
            .attr("fill", function(d) {
                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
                {
                    var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                }
                var columnList = [];
                columnList.push(columns[0]);
                show_details(d.data, columnList, measureArray, this,div);

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
//    arcs.filter(function(d) {
//        return d.endAngle - d.startAngle > 0.2;
//    })
//            .append("svg:text")
//            .attr("dy", ".35em")
//            .attr("text-anchor", "start")
//            .attr("style", "font-family: lucida grande")
//            .attr("style", "font-size: 12px")
//            .attr("transform", function(d) {
//                d.outerRadius = radius;
//               var a = angle(d);
//                if(a < -90 ){
//                    d.innerRadius = radius / 3;
//                    var a = a+180;
//             }
//                else{
//                     d.innerRadius = radius / 1.1;
//               }
//                 return "translate(" + arcFinal.centroid(d) + ")rotate(" +a + ")";
//            })
//            .text(function(d) {
//
//                var percentage = (d.value / parseInt(sum)) * 100;
//                return percentage.toFixed(1) + "%"
//
//            });
//    .text(function(d) {
//        if (parent.$("#isDashboard").val() === "true" || (typeof parent.$("#dashBoardType").val() !== "undefined" && parent.$("#dashBoardType").val() === "drilldash") || parent.$("#chartType").val() === 'KPIBar') {
//            //            var percentage = (d.value / sum) * 100;
//            var percentage = (d.value /parseInt(parent.$("#sumValue").val())) * 100;
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
            .attr("transform", function(d) { //set the label's origin to the center of the arc
                //we have to make sure to set these before calling arc.centroid
                d.outerRadius = radius; // Set Outer Coordinate
                var a = angle(d);
                if (a < -90) {
                    a = a + 180;
                    d.innerRadius = radius / 2;
                } else {
                    d.innerRadius = radius / 1.3;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .text(function(d) {

                var percentage = (d.value / parseInt(sum)) * 100;
                return percentage.toFixed(1) + "%"

            });
    var center_group = svg.append("svg:g")
            .attr("class", "ctrGroup")
            .attr("transform", "translate(" + (width / 2) + "," + (width / 2) + ")");
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

      var html ="<div style='height:100px;width:500px;overflow:auto'><table style=''><tr style=\'white-space: nowrap;\'>";
     for(var m=0;m<(data.length < 5 ? data.length : 5);m++){
//     for(var m=0;m<data.length;m++){
    html +="<td style='width:"+data[m][columns[0]].length+"px;whitespace:nowrap;'><canvas width='6' height='6' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;color:"+color(m)+";white-space:nowrap'> "+data[m][columns[0]]+"</span> </td>";
    }
    html +="</tr><table></div>";
    $("#"+div).append(html);
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
}

function buildHalfPie5(divappend,div, data,chartData, columns, measureArray,wid,hgt) {
//    alert("dataP1 "+JSON.stringify(data))
//    alert("datalen "+data.length)
//    alert("columns11 "+columns[0])
//    alert("measureArray "+measureArray)
//columns[0] = columns[4];

//d3.json("NewLogin/json/GrossMarginAnalysis_63.json", function (error,data) {
//
//alert("data"+JSON.stringify(data["chart1"]))
//alert("data"+JSON.stringify(data["chart1"][1]["Store"]))

   var color = d3.scale.category10();
    var divWidth, divHeight, rad;
    var w = $(window).width()/2+"px";
    divWidth=wid;
     divWid=wid;
    divHeight=hgt;
    rad=(Math.min((divWidth), divHeight))*.50;
//    rad=divHeight*.65;
//    rad=divHeight;
    var autoRounding1 = "1d";
//var chartData = JSON.parse($("#chartData").val());
//var colIds = "";
var colIds = chartData[div]["viewIds"];
//    divWidth=$("#"+div).width();
//    divHeight=$("#"+div).height();

    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    
    var pi = Math.PI;
    var fun = "drillWithinchart(this.id,\""+divappend+"\")";
    var divnum = parseInt(div.replace("chart", "", "gi"));
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
//  alert("sum "+sum)
    var drillStates = parent.$("#drillStatus").val();
//    var chartDiv;
//    if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
//        chartDiv = JSON.parse(parent.$("#chartData").val());
//        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
//            fun = "drillWithinchart(this.id)";
//        }
//        else {
//            fun = "";
//        }
//    }
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var margintop;
    
     if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
        height = divHeight * 1.8;
        margintop = "120px";
        radius = rad;
    }
    else {
        height = divHeight;
        margintop = "0px";
        radius = rad ;
    }
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie() //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            }).startAngle(-90 * (pi/180))
        .endAngle(90 * (pi/180));
    svg = d3.select("#" + divappend).append("svg:svg")
            .datum(data)
//            .attr("width", width*.7)
//            .attr("height", height-150)
            .attr("width", width)
            .attr("height", height*.7)
            .attr("style", margintop);
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            //    .attr("id", function(d) {return color(d.name);)
            .attr("id", function(d) {
//                alert(d)
//                alert(JSON.stringify(d))
//                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                return "gradient111" ;
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

                return color(i);

            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            // .attr("class", "arc")
            .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];

            })

//            .attr("transform", "translate(" + width / 2.8 + "," + height / 2 + ")")
            .attr("transform", "translate(" + width /2 + "," + height * .65 + ")")
            .attr("onclick", fun);

    arcs.append("path")
            .attr("fill", function(d,i) {
                
                return color(i);//"url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
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
                show_details(chartData,d.data, columnList, measureArray, this,div);

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
       arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })

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
               
                if (a < -90) {
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

                   var percentage = (d.value / parseInt(sum)) * 100;
                return percentage.toFixed(1) + "%"

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
    var fwidt=$("#"+divappend).width(),
fontSize1 = (fwidt*.022);
    var html ="<div align='center' style='height:"+(hgt*.2)+"px;width:"+wid+"px;overflow:auto'><table  style='width:"+wid+"px;'><tr style=\'white-space: nowrap;\'>";
     for(var m=0;m<(data.length < 5 ? data.length : 5);m++){
//     for(var m=0;m<data.length;m++){
    html +="<td style='width:"+(data[m][columns[0]].length-5)+"px;whitespace:nowrap; font-size: "+(fontSize1)+"px ;'><canvas width='6' height='6' style='margin-right:5px;background:" + color(m) + "'></canvas><span style='color:"+color(m)+";white-space:nowrap'> "+data[m][columns[0]]+"</span> </td>";
    }
    html +="</tr><table></div>";
    $("#"+divappend).append(html);
//});
}







function numberFormat(value,symbol,precion){
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
            temp = setFormat(doubleVal,precion)+ "K";
        }
    }else if(symbol=="M"){
        doubleVal = (num / getPowerOfTen(6));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion)+ "M";
        }
    }else if(symbol=="" || symbol=="Absolute"){
        doubleVal = num;
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion);
        }
    }else if(symbol=="L"){
        doubleVal = (num / getPowerOfTen(5));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion)+ "L";
        }
    }else if(symbol=="Cr"){
        doubleVal = (num / getPowerOfTen(7));
        if(setFormat(doubleVal,precion) ==check){
            temp=value;
        }else{
            temp = setFormat(doubleVal,precion)+ "Cr";
        }
    }
    return temp;
}
function getPowerOfTen(num){
    var bd = 1;
    for (var i = 0; i <
        parseInt(num); i++) {
        bd = bd * 10;
    }
    return bd;
}
function setFormat(value,prcesion){
    var precison = parseInt(prcesion);
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

function graphProp(chartData,div){
//    var chartData = JSON.parse($("#chartData").val());
    
//    var dataDisplay,displayType,yAxisRounding,yAxisFormat;
    if(typeof chartData[div]["dataDisplay"]!=="undefined"){
        dataDisplay=chartData[div]["dataDisplay"];
    }else{
       dataDisplay="Yes";
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
    if(typeof chartData[div]["displayLegend"]!=="undefined"){
        displayLegend=chartData[div]["displayLegend"];
    }else{
       displayLegend="Yes";
    }
    if(typeof chartData[div]["colorLegend"]!=="undefined"){
        colorLegend=chartData[div]["colorLegend"];
    }else{
       colorLegend="Default";
    }
}
function buildCircularProgress5(divappend,chartId, data,chartData, columns, measureArray){

//alert("data ")
//var data = graphData[chartId];

//    var chartData = JSON.parse(parent.$("#chartData").val());
            var measures = chartData[chartId]["meassures"];
//        alert("measures "+measures)

    var aggregation = chartData[chartId]["aggregation"];
//     var sum = d3.sum(data, function(d) {
//        return d[measures];
//    });
var sum=0;

$.each(data, function (d) {
sum += parseInt(data[d][measureArray[0]]);
});
    var vall=sum/data.length;
    if (aggregation == "AVG" || aggregation == "avg") {
        sum = (sum / data.length);
    }
    var format = "";
    if(typeof chartData[chartId]["yAxisFormat"]!="undefined"){
        format=chartData[chartId]["yAxisFormat"];

    }
    if (aggregation == "AVG" || aggregation == "avg") {
        sum=numberFormat(sum,"","0");
    }
    else{
    sum=numberFormat(sum,format,"0");
    }
//    alert("aggregation "+aggregation)
//    alert("val "+vall)
//    alert("sum "+sum)




    if(chartId=="chart1"){
    sum="33";
    }
    else if(chartId=="chart2"){
sum="34";
    }
    else if(chartId=="chart3"){
sum="82";
    }
    else {
sum="28.47";
    }



//$("#" + chartId).html("");
// var chartnum = chartId.replace(/\D/g,'');
//           var Kpheight=$("#divchart"+chartnum).height();
var $container = $("#" + divappend),
        idval = 2 * Math.PI,
//        width = $container.parent().width(),
//        height = (($container.parent().height())-20),
        width = $container.width(),
        height = (($container.height())),
        outerRadius = Math.min(width,(height-20))/2,
        innerRadius = (outerRadius/5)*4,
        fontSize = (Math.min(width,height)/4);
//alert("width "+width);
//alert("height "+height);
    var arc = d3.svg.arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius)
        .startAngle(0);
    var arc1 = d3.svg.arc()
        .innerRadius(innerRadius/3)
        .outerRadius(outerRadius*1.1)
        .startAngle(0);
$("#" + chartId).html("");
    var svg = d3.select("#" + divappend).append("svg")
        .attr("width", '100%')
        .attr("height", '100%')
//        .attr('viewBox','0 0 '+Math.min(width,height) +' '+Math.min(width,height) )
        .attr('viewBox','0 0 '+Math.min(width) +' '+Math.min(height) )
        .attr('preserveAspectRatio','xMinYMin')
        .append("g")
//        .attr("transform", "translate(" + Math.min(width,height) / 2 + "," + Math.min(width,height) / 2 + ")");
//        .attr("transform", "translate(" + Math.min(width,height) / 2 + "," + Math.min(width,height) / 2 + ")");
        .attr("transform", "translate(" + width / 2 + "," + (height-20)/ 2 + ")")

    var text = svg.append("text")
        .text('0%')
        .attr("text-anchor", "middle")
        .style("font-size",fontSize+'px')
        .attr("dy",fontSize/3)
        .attr("dx",2);

    var background = svg.append("path")
        .datum({endAngle: idval})
        .style("fill", "skyblue")
//        .style("fill", "#7cc35f")
        .attr("d", arc);

    var foreground = svg.append("path")
        .datum({endAngle: 0 * idval})
//        .style("fill", "#57893e")
        .style("fill", "#3366CC")
        .attr("d", arc1);
//alert("idval "+idval)
//    setInterval(function() {
      foreground.transition()
          .duration(750)
//          .call(arcTween, Math.random() * idval);
//          .call(arcTween, sum);
          .call(arcTween, sum * idval);
//    }, 1500);

    function arcTween(transition, newAngle) {

        transition.attrTween("d", function(d) {

            var interpolate = d3.interpolate(d.endAngle, newAngle);
//alert(interpolate)
            return function(t) {

                d.endAngle = interpolate(t)/100;

//                text.text(Math.round((d.endAngle/idval)*100)+'%');
                text.text(Math.round((d.endAngle/idval)*100)+'%');
//                text.text('ffffff%');
//                text.text(sum);
//                if (aggregation == "AVG" || aggregation == "avg") {
//        text.text(sum+" %");
//    }else{
//        text.text(sum);
//    }

                return arc(d);
            };
        });
    }

//$("#" + chartId).html(svg);



}







