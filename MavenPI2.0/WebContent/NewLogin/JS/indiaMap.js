//function buildmapTest(div,data,columns,measureArray,divWid,divHgt){

var colorList = ["#F20000", "#FF4040", "#FFC200", "#FFAF0F", "#FFAF0F", "#FFE019", "#FFE019", "#22F23E", "#38C70C", "#33B50B"];
//added by manik
var tooltip1 = CustomTooltip1("my_tooltip1", "auto");
var tooltip2 = CustomTooltip2("my_tooltip2", "auto");
var centralColorMap = {};
var columnMap = {};
var isFormatedMeasure = false;
var labelColor = "black";
var drillValues=[];
var shadingMeasure;
var isShadedColor = false;
var conditionalShading = false;
var conditionalMap = {};
var conditionalMeasure;
var colorMap = {};
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

function CustomTooltip1(tooltipId, width) {

//    var tooltipId = tooltipId;
    $("body").append("<div class='tooltip' id='" + tooltipId + "'></div>");

    if (width) {

        $("#" + tooltipId).css("width", width);
    }

    hideTooltip();

    function showTooltip(content, event) {
        $("#" + tooltipId).html(content);
        $("#" + tooltipId).show();
        updatePshowTooltiposition(event);
    }


    function hideTooltip() {
        $("#" + tooltipId).hide();
    }

    function updatePosition(event) {
        var ttid = "#" + tooltipId;
        var xOffset = 20;
        var yOffset = 10;

        var ttw = $(ttid).width();
        var tth = $(ttid).height();
        var wscrY = $(window).scrollTop();
        var wscrX = $(window).scrollLeft();
        var curX = (document.all) ? event.clientX + wscrX : event.pageX;
        var curY = (document.all) ? event.clientY + wscrY : event.pageY;
        var ttleft = ((curX - wscrX + xOffset * 2 + ttw) > $(window).width()) ? curX - ttw - xOffset * 2 : curX + xOffset;
        if (ttleft < wscrX + xOffset) {
            ttleft = wscrX + xOffset;
        }
        var tttop = ((curY - wscrY + yOffset * 2 + tth) > $(window).height()) ? curY - tth - yOffset * 2 : curY + yOffset;
        if (tttop < wscrY + yOffset) {
            tttop = curY + yOffset;
        }
        $(ttid).css('top', tttop + 'px').css('left', ttleft + 'px');
    }

    return {
        showTooltip: showTooltip,
        hideTooltip: hideTooltip,
        updatePosition: updatePosition
    }
}
function CustomTooltip2(tooltipId, width) {
   
//    var tooltipId = tooltipId;
    $("body").append("<div class='tooltip' id='" + tooltipId + "'></div>");

    if (width) {

        $("#" + tooltipId).css("width", width);
    }

    hideTooltip();

    function showTooltip(content, event) {
        $("#" + tooltipId).html(content);
        $("#" + tooltipId).show();
        updatePshowTooltiposition(event);
    }


    function hideTooltip() {
        $("#" + tooltipId).hide();
    }

    function updatePosition(event) {
        var ttid = "#" + tooltipId;
        var xOffset = 20;
        var yOffset = 10;

        var ttw = $(ttid).width();
        var tth = $(ttid).height();
        var wscrY = $(window).scrollTop();
        var wscrX = $(window).scrollLeft();
        var curX = (document.all) ? event.clientX + wscrX : event.pageX;
        var curY = (document.all) ? event.clientY + wscrY : event.pageY;
        var ttleft = ((curX - wscrX + xOffset * 2 + ttw) > $(window).width()) ? curX - ttw - xOffset * 2 : curX + xOffset;
        if (ttleft < wscrX + xOffset) {
            ttleft = wscrX + xOffset;
        }
        var tttop = ((curY - wscrY + yOffset * 2 + tth) > $(window).height()) ? curY - tth - yOffset * 2 : curY + yOffset;
        if (tttop < wscrY + yOffset) {
            tttop = curY + yOffset;
        }
        $(ttid).css('top', tttop + 'px').css('left', ttleft + 'px');
    }

    return {
        showTooltip: showTooltip,
        hideTooltip: hideTooltip,
        updatePosition: updatePosition
    }
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
function buildIndiaMapPar(div,columns,measures,w,h){
 
   //  var fun = "drillWithinchart1(this.id,\"chart1\")";
   //  var color = d3.scale.category10();
//var chart12 = data["chart1"];
//var chartMapData = data["chart1"];
//var chartData = JSON.parse(parent.$("#chartData").val());
//columns = chartData["chart1"]["viewBys"];
//mapColumns = chartData["chart1"]["viewBys"];
//measureArray = chartData["chart1"]["meassures"];
//var colIds = chartData["chart1"]["viewIds"];

//var w = 700;
//var h = 600;
var mapData ;
var corpFlag = false;
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
d3.json("json/indiaMap.json", function (json) {
drawIndiaMapPar(json)
})
drawIndiaMapPar = function(mapData){
   // alert("india map par"+JSON.stringify(mapData))
  var maxColor = "#1DA10E";
var minColor = "#99FF91";
        var measureData = [];
        for(var i = 0; i < mapData.length; i++)
        {

            measureData.push(mapData[i][measures[2]]);
        }

var sum = d3.sum(mapData, function(d) {
            return d[measures[2]];
        });
var color = d3.scale.linear()
.domain([Math.min.apply(Math, measureData), Math.max.apply(Math, measureData)])
.range([minColor, maxColor]);
d3.json("json/states-tel.json", function (json) {
//d3.json("json/states-tel.json", function (json) {
india.selectAll("path")

.data(json.features)
.enter().append("path")
.attr("d", path)
.attr("fill", function(d, i) {
                    var currName = (d.id);


                    for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {

                            var color1;
                            if(corpFlag){
                              color1 = "blue";
                            }else{
                                if(mapData[l][measures[2]]==0){
                                    color1="#006615";
                                }else{

                                color1 = getConditionalColor(mapData[l][measures[2]], sum);

                            }
                            }
                            return color1;
                        }
                    }
                    if(corpFlag){
                        return "#fff";
                    }else{
                    return "#006615";
                }
                })

				.attr("id", function(d, i) {
                    var currName = (d.id);
                    for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {
                            return mapData[l][columns[0]];
                        }
                    }
                    return d.id;
                })
                .attr("cursor", "pointer")
    .on("mouseover", function(d, i) {
                    var content = "";
                    var currName = d.id;
                    var noSchoolflag=true;
                    content += "<span class=\"name\"></span><span class=\"name\"> " + currName + "</span><br/>";
                    for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {
//                            for(var mes in measures){

                                //content += "<span class=\"name\">Total Schools: </span><span class=\"value\"> : " + addCommas(mapData[l][measures[0]]) + "</span><br/>";
                            if(!corpFlag){
                                //content += "<span class=\"name\">Supported Schools: </span><span class=\"value\"> : " + addCommas(mapData[l][measures[1]]) + "</span><br/>";
                                if(mapData[l][measures[2]]==0){

//                                    content += "<span class=\"value\"> Toilets Pledged: </span><span class=\"name\">"+ addCommas(mapData[l][measures[3]]) +"</span>";
                                    content += "<span class=\"value\"> Toilets Pledged: </span><span class=\"name\">"+ addCommas(mapData[l]["TOTAL_TOILET_1"]) +"</span>";
                                }else{

                                    content += "<span class=\"value\">Total Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOTAL_TOILET_1"]) + "</span><br/>";
                                    content += "<span class=\"value\">Supported Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOILET_SUPP"]) + "</span><br/>";
                                content += "<span class=\"value\">Remaining Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOILET_REM"]) + "</span><br/>";
//                                    content += "<span class=\"value\">Total Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[0]]) + "</span><br/>";
//                                    content += "<span class=\"value\">Supported Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[1]]) + "</span><br/>";
//                                content += "<span class=\"value\">Remaining Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[2]]) + "</span><br/>";
                            }
                            }
//                            }

                            noSchoolflag=false;
                        }
                    }
                    if(noSchoolflag && !corpFlag){
                        content += "<span class=\"name\">No School Available.</span><br/>";
                    }
                    return tooltip1.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    return tooltip1.hideTooltip();
                })
});
}


function initialize() {
proj.scale(5700);
proj.translate([-1024, 650]);
}

}
function buildIndiaMap(div,columns,measures,w,h){

//var w = 700;
//var h = 600;
var mapData = [] ;
var corpFlag = false;
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
var maxColor = "#1DA10E";
var minColor = "#99FF91";
var india = map.append("svg:g")
.attr("id", "india");
d3.json("json/indiamap2.json", function (json) {
   
    drawMap(json)
    })

   var drawMap = function(mapData){
 //alert(JSON.stringify(mapData))
        var measureData = [];
        for(var i = 0; i < mapData.length; i++)
        {
             measureData.push(mapData[i][measures[3]]);
        }

var sum = d3.sum(mapData, function(d) {
            return d[measures[3]];
        });
var color = d3.scale.linear()
.domain([Math.min.apply(Math, measureData), Math.max.apply(Math, measureData)])
.range([minColor, maxColor]);
d3.json("json/states-tel.json", function (json) {

india.selectAll("path")

.data(json.features)
.enter().append("path")
.attr("d", path)
.attr("fill", function(d, i) {
                    var currName = (d.id);
                        for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {

                            var color1;
                            if(corpFlag){
                              color1 = "blue";
                            }else{
                                if(mapData[l][measures[3]]==0){
                                    color1="#006615";
                                }else{

                                color1 = getConditionalColor(mapData[l][measures[3]], sum);

                            }
                            }
                            return color1;
                        }
                    }
                    if(corpFlag){
                        return "#fff";
                    }else{
                    return "#006615";
                }
                })
.attr("id", function(d, i) {
                    var currName = (d.id);
                    for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {
                            return mapData[l][columns[0]];
                        }
                    }
                    return d.id;
                })
                .attr("cursor", "pointer")
    .on("mouseover", function(d, i) {
                    var content = "";
                    var currName = d.id;
                    var noSchoolflag=true;
                    content += "<span class=\"name\"></span><span class=\"name\"> " + currName + "</span><br/>";
                    for (var l = 0; l < mapData.length; l++) {
                        if (currName.toLowerCase() == mapData[l][columns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && mapData[l][columns[0]].toLowerCase() == "chhattisgarh")) {
//                            for(var mes in measures){

                                //content += "<span class=\"name\">Total Schools: </span><span class=\"value\"> : " + addCommas(mapData[l][measures[0]]) + "</span><br/>";
                            if(!corpFlag){
                                //content += "<span class=\"name\">Supported Schools: </span><span class=\"value\"> : " + addCommas(mapData[l][measures[1]]) + "</span><br/>";
                                if(mapData[l][measures[2]]==0){

//                                    content += "<span class=\"value\"> Toilets Pledged: </span><span class=\"name\">"+ addCommas(mapData[l][measures[3]]) +"</span>";
                                    content += "<span class=\"value\"> Toilets Pledged: </span><span class=\"name\">"+ addCommas(mapData[l]["TOTAL_TOILET_1"]) +"</span>";
                                }else{

                                    content += "<span class=\"value\">Total Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOTAL_TOILET_1"]) + "</span><br/>";
                                    content += "<span class=\"value\">Supported Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOILET_SUPP"]) + "</span><br/>";
                                content += "<span class=\"value\">Remaining Toilets: </span><span class=\"name\"> " + addCommas(mapData[l]["TOILET_REM"]) + "</span><br/>";
//                                    content += "<span class=\"value\">Total Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[0]]) + "</span><br/>";
//                                    content += "<span class=\"value\">Supported Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[1]]) + "</span><br/>";
//                                content += "<span class=\"value\">Remaining Schools: </span><span class=\"name\"> " + addCommas(mapData[l][measures[2]]) + "</span><br/>";
                            }
                            }
//                            }

                            noSchoolflag=false;
                        }
                    }
                    if(noSchoolflag && !corpFlag){
                        content += "<span class=\"name\">No School Available.</span><br/>";
                    }
                    return tooltip2.showTooltip(content, d3.event);
                })
                .on("mouseout", function(d, i) {
                    return tooltip2.hideTooltip();
                })

});

}
function initialize() {
proj.scale(5700);
proj.translate([-1024, 650]);
}

}

function getConditionalColor(meassurVal, sum) {

    var percent = parseInt(meassurVal) / parseInt(sum) * 100;
    if (percent > 10) {
        return colorList[0];
    }
    if (percent <= 10 && percent > 9) {
        return colorList[1];
    }
    if (percent <= 9 && percent > 8) {
        return colorList[2];
    }
    if (percent <= 8 && percent > 7) {
        return colorList[3];
    }
    if (percent <= 7 && percent > 6) {
        return colorList[4];
    }
    if (percent <= 6 && percent > 5) {
        return colorList[5];
    }
    if (percent <= 5 && percent > 3) {
        return colorList[6];
    }
    if (percent <= 3 && percent > 2) {
        return colorList[7];
    }
    if (percent <= 2 && percent > 1) {
        return colorList[8];
    }
    if (percent <= 1 && percent >= 0) {
        return colorList[9];
    }

    return colorList[9];
}

function buildWordCloud(div, columns, measureArray,divWid,divHgt) {
 var chartMap = {};
var fun = "drillWithinchart(this.id,\""+div+"\")";
    var max_amount;
    d3.json("json/wordcloud.json", function (json) {
  buildwordcloudsv(json)

    })

buildwordcloudsv = function(data){

    var fill = d3.scale.category10();
    max_amount = maximumValue(data, measureArray[0]);
    var min = minimumValue(data, measureArray[0]) ;
    var wid = divWid;
    var hgt = divHgt;
    var digonal = Math.sqrt(wid * wid + hgt * hgt);
    var maxSize = digonal * .04;
    var minSize = maxSize ;
    var max = 10 / data.length * 60;
    var maxRange = max < 100 ? 100 : max;
    var minRange = (maxRange - 20) < 0 ? 10 : 50;
    this.radius_scale = d3.scale.pow().exponent(0.5).domain([min, max_amount]).range([minSize/2, maxSize*2]);
    d3.layout.cloud().size([wid, hgt])
    .words(data.map(function(d) {
    var map = {};
    map["text"] = d[columns[0]];
    map["size"] = this.radius_scale(parseInt(d[measureArray[0]]));
    for (var no = 0; no < measureArray.length; no++) {
    map["value" + no] = d[measureArray[no]];
    }
    return map;
    }))
    .padding(1)
    .rotate(function(d, i) {
    return 0;
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
    .attr("viewBox", "1 1 "+wid+" "+hgt)
    .attr("preserveAspectRatio","xMinYMin meet")
    .attr("version","1.1")
    .attr("id", "svg_" + div)
    .attr("style", "margin-top:0px")
    
    .append("g")
    .attr("id", "svg_" + div)
    .attr("transform", "translate(" + wid / 2 + "," + hgt / 2 + ")");
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
    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
    colorShad = centralColorMap[d.text.toString().toLowerCase()];
    } else {
    colorShad = fill(i);
    }

    chartMap[d.text] = colorShad;
    return colorShad;
    })
    .attr("color_value", function(d, i) {
    var colorShad;
    if (typeof centralColorMap[d.text.toString().toLowerCase()] !== "undefined") {
    colorShad = centralColorMap[d.text.toString().toLowerCase()];
    } else {
    colorShad = fill(i);
    }
    chartMap[d.text] = colorShad;
    return colorShad;
    })
    .attr("text-anchor", "middle")
    .attr("id", function(d) {
    return d.text ;
    })
  //  .attr("onclick", fun)
//    .on("mouseover", function(d, i) {
//    var circle = d3.select(this);
//    circle.transition()
//    .duration(100).style("opacity", 1).style("z-index", -1).style("position", "absolute")
//    .style("font-size", function(d) {
//    if (parseInt(d.size) < 0) {
//    return 20 + "px";
//    } else {
//    return d.size + 20 + "px";
//    }
//    });
//    var content = "";
//    var msrData;
//    content += "<span class=\"value\"> " + d.text + "</span><br/>";
//    for (var no = 0; no < measureArray.length; no++) {
//    if (isFormatedMeasure) {
//    msrData = numberFormat(d["value" + no], round, precition);
//    }
//    else {
//    msrData = addCommas(d["value" + no]);
//    }
//    content += "<span class=\"value\">Schools Supported:</span><span class=\"name\"> " + msrData + "</span><br/>";
//    }
//    return tooltip.showTooltip(content, d3.event);
//    })
//    .on("mouseout", function(d) {
//    var circle = d3.select(this);
//    circle.transition()
//    .style("font-size", function(d) {
//    return d.size - 2 + "px";
//    });
////    hide_details(d,  this);
//    return tooltip.hideTooltip();
//    })
    .attr("transform", function(d) {
    return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
    })
    .attr("class", function(d, i) {
    return "bars-Bubble-index-" + d.text.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
    .text(function(d) {
    return d.text;
//    if(d.text.length<16)
//    return d.text;
//    else
//    return d.text.substring(0, 15) + "..";
    });
  //  parent.$("#colorMap").val(JSON.stringify(colorMap));
    }
    }
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
    }
    return min;
    }

function drillWithinchart(corpId){
 
   window.location.href = "http://125.63.72.116:8085/swachhvidhyalaya/corpNext?corpName=" + corpId;
}

function bubble1(div, data0, columns, measures, divWidth, divHeight, layout) {
        alert("dat0  "+data0)
//d3.json("NewLogin/json/wordcloud.json", function (json) {
//
//  buildBubble(json);
//
//    })
//  buildBubble = function(data0){
 
     var color = d3.scale.category10();
//     divWidth=parseInt($(window).width())*(.35);
    var nodes = [];
    var data = [];
    var layout = 10;
    var measure1 = measures[0];
  //  var chartData = JSON.parse($("#chartData").val());
//var colIds = chartData[div]["viewIds"];
    var chartMap = {};
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    var isDashboard = parent.$("#isDashboard").val();
//    var chartType = parent.$("#chartType").val();
   var chartType ="";
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

//    if (chartType === "dashboard") {
//        isDashboard = true;
//    }
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//    }
//    if (parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
//        if (div === "chart1") {
//            var tempcolumn = columns;
//            columns = [];
//            columns.push(tempcolumn[0]);
//        }
//        var tempmeasure = measure;
//        measures = [];
//        measures.push(tempmeasure[0]);
//    }
    var barsWidthTotal = $(window).width() - 400;
    var legendOffset = 60;
    var legendBulletOffset = 130;
    var legendTextOffset = 20;
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    svg = d3.select("#" + div).append("svg")
            .attr("width", divWidth )
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
//                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                } catch (e) {
//                }
                if (isShadedColor) {
                    colorShad = color(d[shadingMeasure]);
                } else if (conditionalShading) {
                    return getConditionalColor(color(i), d[conditionalMeasure]);
                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
            //        colorShad = drillShade;
                } else {
                    if (typeof centralColorMap[d.name.toString().toLowerCase()] !== "undefined") {
                        colorShad = centralColorMap[d.name.toString().toLowerCase()];
                    } else {
                        colorShad = color(i);
                    }
                }
                if (typeof chartMap[d.name] === "undefined") {
           //         colorMap[i] = d.name + "__" + colorShad;
                    chartMap[d.name] = colorShad;
                }
                return colorShad;
            })
            .attr("stop-opacity", 1);
 //   parent.$("#colorMap").val(JSON.stringify(colorMap));
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

        //    .attr("onclick", fun)
//            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue.replace(/[^a-zA-Z0-9]/g, '', 'gi').replace("_", "-");
//                    try {
//                        var selectedBar = d3.selectAll(barSelector);
//                        selectedBar.style("fill", drillShade);
//                    } catch (e) {
//                    }
//                }
//                d3.select(this).attr("stroke", "grey");
//                var content = "";
//                for (var no = 0; no < measures.length; no++) {
//                    var msrData;
//                    if (isFormatedMeasure) {
//                        msrData = numberFormat(d.value, round, precition);
//                    }
//                    else {
//                        if (no === 0) {
//                            msrData = addCommas(d.value);
//                        } else {
//                            msrData = addCommas(d["value" + no]);
//                        }
//
//                    }
//                    if (typeof columnMap[columns[0]] !== "undefined" && columnMap[column[0]]["displayName"] !== "undefined" && typeof columnMap[measure1] !== "undefined" && columnMap[measure1]["displayName"]) {
//                        if (no === 0) {
//                            content += "<span class=\"name\">" + columnMap[columns[0]]["displayName"] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
//                            if (columns.length > 1) {
//                                content += "<span class=\"name\">" + columnMap[columns[1]]["displayName"] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
//                            }
//                        }
//                        content += "<span class=\"name\">" + columnMap[measures[no]]["displayName"] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
//                    } else {
//                        if (no === 0) {
//                            content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.name + "</span><br/>";
//                            if (columns.length > 1) {
//                                content += "<span class=\"name\">" + columns[1] + ":</span><span class=\"value\"> " + d.name1 + "</span><br/>";
//                            }
//                        }
//                        content += "<span class=\"name\">" + measures[no] + ":</span><span class=\"value\"> " + (msrData) + "</span><br/>";
//                    }
//                }
//                return tooltip1.showTooltip(content, d3.event);
//            })
//            .on("mouseout", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
//                    var bar = d3.select(this);
//                    var indexValue = bar.attr("index_value");
//                    var barSelector = "." + "bars-Bubble-" + indexValue;
//                    var selectedBar = d3.selectAll(barSelector);
//                    var colorValue = selectedBar.attr("color_value");
//                    selectedBar.style("fill", colorValue);
//                }
//                hide_details(d, i, this);
//            });

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
//                .on('mouseover', synchronizedMouseOver)
//                .on("mouseout", synchronizedMouseOut);
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
//                .on('mouseover', synchronizedMouseOver)
//                .on("mouseout", synchronizedMouseOut)
         //       .attr("onclick", "legendDrill(this.id)");
    }
//}
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
}

function buildPieMap(div,  columns, measureArray,wid,hgt) {


   d3.json("json/wordcloud.json", function (json) {
  buildPie(json)

    })
   buildPie = function(data){

   var color = d3.scale.category10();
    var divWidth, divHeight, rad;

    var w = wid;
    divWidth=wid;
//     divWid=parseInt($(window).width())*(.35);
    divHeight=hgt;
    
    rad=divHeight*.55;
    var autoRounding1 = "1d";
//var chartData = JSON.parse($("#chartData").val());
//    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
   // var chartType = parent.$("#chartType").val();
    var chartType ;
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
            .attr("width", height)
            .attr("height", height)
            .attr("style", margintop)
            .attr("style","margin-left:15%");
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            //    .attr("id", function(d) {return color(d.name);)
            .attr("id", function(d) {
                return "gradient" + (d[columns[0]]);
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
 //   parent.$("#colorMap").val(JSON.stringify(colorMap));
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
         //   .attr("onclick", fun);

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
                return "index-" + d.data[columns[0]];
            })
            .attr("color_value", function(d, i) {
                return "url(#gradient" + (d.data[columns[0]]);
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]];
            })
//            .on("mouseover", function(d, i) {
//                var columnList = [];
//                columnList.push(columns[0]);
//                show_details(d.data, columnList, measureArray, this,"chart1");
//
//            })
//            .on("mouseout", function(d, i) {
//                hide_details(d, i, this);
//            })
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
                   var percentage = (d.value / parseInt(sum)) * 100;
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


//     var html ="";
////var viewOvName = JSON.parse(parent.$("#viewby").val());
////    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
////    var measName = JSON.parse(parent.$("#measure").val());
////    var measIds = JSON.parse(parent.$("#measureIds").val());
//    var colName=columns[0];
////            if(viewOvIds.indexOf(columns[0])!="undefined"){
////                colName=viewOvName[viewOvIds.indexOf(columns[0])];
////            }
//     if(columns[0].length > 11){
//          html ="<div style='white-space:nowrap;height:"+height+"px;float:right;margin-right:100px;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:10px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:20px;text-decoration:none' >"+columns[0].substring(0,11)+"..</span></td></tr>";
//     }else {
//          html ="<div style='white-space:nowrap;height:"+height+"px;float:right;margin-right:100px;overflow-y:auto'><table style='float:right;' height='"+height+"'><tr><td><table style='height:auto;float:right;width:"+width*.15+"px;'><tr><td><span style='font-size:10px;margin-left:2px;color:black;float:left;' class=''></span><span style='white-space:nowrap;color:black;font-size:14px;margin-left:20px;text-decoration:none' >"+columns[0]+"</span></td></tr>";
//    }
//
//    for(var m=0;m<data.length;m++){
//        if(data[m][columns[0]].length > 12 ) {
//    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='8' height='8' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;font-size: 9px;color:"+color(m)+"'> "+data[m][columns[0]].substring(0, 12)+"..</span> </td></tr>";
//    }
//else {
//    html +="<tr style='height:25px'><td style='whitespace:nowrap'><canvas width='8' height='8' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;font-size: 9px;color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td></tr>";
//}
//}
//    html +="</table></td></tr></table></div>";
//    $("#"+div).append(html);
//}
   }
}

function buildPie3D(div,  columns, measureArray, width, height, radius, chartType) {

    d3.json("json/wordcloud.json", function (json) {
  buildPie3d(json)

    })

 buildPie3d = function(data)   {
    var fun = "drillWithinchart(this.id,\""+div+"\")";
    width=parseInt($(window).width())*(.35);
    var chartData ;
 //   var chartData = JSON.parse(parent.$("#chartData").val());

  var prop = graphProp1(div);
    !function() {
        var Donut3D = {};

        function pieTop(d, rx, ry, ir) {
            if (d.endAngle - d.startAngle === 0)
                return "M 0 0";
            var sx = rx * Math.cos(d.startAngle),
                    sy = ry * Math.sin(d.startAngle),
                    ex = rx * Math.cos(d.endAngle),
                    ey = ry * Math.sin(d.endAngle);

            var ret = [];
            ret.push("M", sx, sy, "A", rx, ry, "0", (d.endAngle - d.startAngle > Math.PI ? 1 : 0), "1", ex, ey, "L", ir * ex, ir * ey);
            ret.push("A", ir * rx, ir * ry, "0", (d.endAngle - d.startAngle > Math.PI ? 1 : 0), "0", ir * sx, ir * sy, "z");
            return ret.join(" ");
        }

        function pieOuter(d, rx, ry, h) {
            var startAngle = (d.startAngle > Math.PI ? Math.PI : d.startAngle);
            var endAngle = (d.endAngle > Math.PI ? Math.PI : d.endAngle);

            var sx = rx * Math.cos(startAngle),
                    sy = ry * Math.sin(startAngle),
                    ex = rx * Math.cos(endAngle),
                    ey = ry * Math.sin(endAngle);

            var ret = [];
            ret.push("M", sx, h + sy, "A", rx, ry, "0 0 1", ex, h + ey, "L", ex, ey, "A", rx, ry, "0 0 0", sx, sy, "z");
            return ret.join(" ");
        }

        function pieInner(d, rx, ry, h, ir) {
            var startAngle = (d.startAngle < Math.PI ? Math.PI : d.startAngle);
            var endAngle = (d.endAngle < Math.PI ? Math.PI : d.endAngle);

            var sx = ir * rx * Math.cos(startAngle),
                    sy = ir * ry * Math.sin(startAngle),
                    ex = ir * rx * Math.cos(endAngle),
                    ey = ir * ry * Math.sin(endAngle);

            var ret = [];
            ret.push("M", sx, sy, "A", ir * rx, ir * ry, "0 0 1", ex, ey, "L", ex, h + ey, "A", ir * rx, ir * ry, "0 0 0", sx, h + sy, "z");
            return ret.join(" ");
        }

        function getPercent(d) {

            if (typeof displayType !== "undefined" && displayType === "Absolute") {
               
               //         return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding);
                }
                else {
            return (d.endAngle - d.startAngle > 0.2 ?
                    Math.round(1000 * (d.endAngle - d.startAngle) / (Math.PI * 2)) / 10 + '%' : '');
        }

        }

        Donut3D.transition = function(id, data, rx, ry, h, ir) {
            function arcTweenInner(a) {
                var i = d3.interpolate(this._current, a);
                this._current = i(0);
                return function(t) {
                    return pieInner(i(t), rx + 0.5, ry + 0.5, h, ir);
                };
            }
            function arcTweenTop(a) {
                var i = d3.interpolate(this._current, a);
                this._current = i(0);
                return function(t) {
                    return pieTop(i(t), rx, ry, ir);
                };
            }
            function arcTweenOuter(a) {
                var i = d3.interpolate(this._current, a);
                this._current = i(0);
                return function(t) {
                    return pieOuter(i(t), rx - .5, ry - .5, h);
                };
            }
            function textTweenX(a) {
                var i = d3.interpolate(this._current, a);
                this._current = i(0);
                return function(t) {
                    return 0.6 * rx * Math.cos(0.5 * (i(t).startAngle + i(t).endAngle));
                };
            }
            function textTweenY(a) {
                var i = d3.interpolate(this._current, a);
                this._current = i(0);
                return function(t) {
                    return 0.6 * rx * Math.sin(0.5 * (i(t).startAngle + i(t).endAngle));
                };
            }

            var _data = d3.layout.pie().sort(null).value(function(d) {
                return d.value;
            })(data);

            d3.select("#" + id).selectAll(".innerSlice").data(_data)
                    .transition().duration(750).attrTween("d", arcTweenInner);

            d3.select("#" + id).selectAll(".topSlice").data(_data)
                    .transition().duration(750).attrTween("d", arcTweenTop);

            d3.select("#" + id).selectAll(".outerSlice").data(_data)
                    .transition().duration(750).attrTween("d", arcTweenOuter);

            d3.select("#" + id).selectAll(".percent").data(_data).transition().duration(750)
                    .attrTween("x", textTweenX).attrTween("y", textTweenY).style("fill", function(d,i){
                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
                return "white"
            else
                return "black"
            }).text(getPercent);
        };

        Donut3D.draw = function(id, data, x /*center x*/, y/*center y*/,
                rx/*radius x*/, ry/*radius y*/, h/*height*/, ir/*inner radius*/) {

            var _data = d3.layout.pie().sort(null).value(function(d) {
                return d[measureArray[0]];
            })(data);

            var slices = d3.select("#" + id).append("g").attr("transform", "translate(" + x + "," + y/1.75 + ")")
                    .attr("class", "slices");

            slices.selectAll(".innerSlice").data(_data).enter().append("path").attr("class", "innerSlice")
                    .style("fill", function(d, i) {
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                        return d3.hsl(color(i)).darker(0.7);
                    })
                    .attr("d", function(d) {
                        return pieInner(d, rx + 0.5, ry + 0.5, h, ir);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
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
                 //   .attr("onclick", fun);

            slices.selectAll(".topSlice").data(_data).enter().append("path").attr("class", "topSlice")
                    .style("fill", function(d, i) {
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                        return color(i);
                    })
                    .style("stroke", function(d, i) {
                        return color(i);
                    })
                    .attr("d", function(d) {
                        return pieTop(d, rx, ry, ir);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
//                    .on("mouseover", function(d, i) {
//                        show_details(d.data, columns, measureArray, this);
//                    })
//                    .on("mouseout", function(d, i) {
//                        hide_details(d, i, this,div);
//                    })
                   .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("color_value", function(d, i) {
                        return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
              //      .attr("onclick", fun);

            slices.selectAll(".outerSlice").data(_data).enter().append("path").attr("class", "outerSlice")
                    .style("fill", function(d, i) {
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                        return d3.hsl(color(i)).darker(0.7);
                    })
                    .attr("d", function(d) {
                        return pieOuter(d, rx - .5, ry - .5, h);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
//                    .on("mouseover", function(d, i) {
//                        show_details(d.data, columns, measureArray, this,div);
//                    })
//                    .on("mouseout", function(d, i) {
//                        hide_details(d, i, this);
            //        })
                    .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("color_value", function(d, i) {
                        return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
         //           .attr("onclick", fun);

            slices.selectAll(".percent").data(_data).enter().append("text").attr("class", "percent")
                    .attr("x", function(d) {
                        return 0.6 * rx * Math.cos(0.5 * (d.startAngle + d.endAngle));
                    })
                    .attr("y", function(d) {
                        return 0.6 * ry * Math.sin(0.5 * (d.startAngle + d.endAngle));
                    }).style("fill", function(d,i){
                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
                return "white"
            else
                return "black"
            })
                    .text(getPercent).each(function(d) {
                this._current = d;
            }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
//                    .on("mouseover", function(d, i) {
//                        show_details(d.data, columns, measureArray, this);
//                    })
//                    .on("mouseout", function(d, i) {
//                        hide_details(d, i, this);
//                    })
                    .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("color_value", function(d, i) {
                        return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
         //           .attr("onclick", fun);
        };

        this.Donut3D = Donut3D;
    }();
    var gIds = div+"g";
    var color = d3.scale.category10();
    var svg = d3.select("#"+div).append("svg").attr("width", width).attr("height", height).attr("style","margin-top:10%");
    svg.append("g").attr("id", gIds);
    if (chartType === "Donut") {
        Donut3D.draw(gIds, data, width / 2, height / 2, radius * 1.3, radius / (2), 40, 0.4);
    }
    else {
        Donut3D.draw(gIds, data, width / 2, height / 2, radius * 1.3, radius / (2), 40, 0);
    }
//     if(typeof displayLegend !=="undefined" && displayLegend =="Yes"){
//    var html ="<table style='width:70%;height:80px'><tr style='vertical-align:baseline'>";
//     for(var m=0;m<(data.length < 5 ? data.length : 5);m++){
//    if(typeof colorLegend !=="undefined" && colorLegend == "Default")
//    html +="<td style='white-space:nowrap'><canvas width='9' height='9' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;white-space:nowrap; color:"+color(m)+"'> "+data[m][columns[0]]+"</span> </td>";
//    else
//    html +="<td style='white-space:nowrap'><canvas width='9' height='9' style='margin-left:5px;margin-right:5px;background:" + color(m) + "'></canvas><span style='margin-left:5px;white-space:nowrap; color:black'> "+data[m][columns[0]]+"</span> </td>";
//    }
//    html +="</tr><table>";
//     }else{
//       var  html ="";
//     }
//    $("#"+div).append(html);
//}
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
function graphProp1(div){
  //  var chartData = JSON.parse($("#chartData").val());
   var chartData  ;

//    var dataDisplay,displayType,yAxisRounding,yAxisFormat;
    if(chartData!=="undefined"){
        dataDisplay=chartData;
    }else{
       dataDisplay="Yes";
    }
    if(typeof chartData!=="undefined"){
        displayType=chartData;
    }else{
       displayType="Absolute";
    }
    if(typeof chartData!=="undefined"){
        yAxisRounding=chartData;
    }else{
       yAxisRounding=0;
    }
    if(typeof chartData!=="undefined"){
        yAxisFormat=chartData;
    }else{
       yAxisFormat="";
    }
    if(typeof chartData!=="undefined"){
        displayX=chartData;
    }else{
       displayX="Yes";
    }
    if(typeof chartData!=="undefined"){
        displayY=chartData;
    }else{
       displayY="Yes";
    }
    if(typeof chartData!=="undefined"){
        displayLegend=chartData;
    }else{
       displayLegend="Yes";
    }
    if(typeof chartData!=="undefined"){
        colorLegend=chartData;
    }else{
       colorLegend="Default";
    }
}

function buildMultiMeasureLine(div, columns, measureArray, divWid, divHgt) {

       d3.json("json/wordcloud.json", function (json) {
        var records = 10;
        var currData = [];
        for(var i=0; i< (json.length < records ? json.length:records);i++){
           
           currData.push(json[i]);
        }


buildMultiMeasureLine1(currData)

    })
    buildMultiMeasureLine1 = function(data){
    
     var color = d3.scale.category10();
     divWid=parseInt($(window).width())*(.35);
    var measure1 = measureArray[0];
    var autoRounding1;
    var measArr = [];
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
   //    var chartData = JSON.parse($("#chartData").val());
//       var colIds = chartData[div]["viewIds"];
     var chartData ;
      var colIds;
       var prop = graphProp1(div);

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
 width = divWid, //- margin.left - margin.right
            height = parseInt(divHgt)* .65;
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);
    var y = d3.scale.linear().domain([minVal, max]).range([height, 0]);
    // Axis setting
    var xAxis = d3.svg.trendaxis()
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
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
              //          return numberFormat(d,yAxisFormat,yAxisRounding);
               return addCommas(d);
              return
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
           
    var svg = d3.select("#" + div).append("svg")
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
            yvalue=500;
        dyvalue = -150;
        }else{
        len += 100;
        yvalue=500;
        dyvalue = -150;
     }
    }else {
        len +=100;
        yvalue=518;
         dyvalue = -150;
     }
   svg.append("g")
            .attr("class", "y axis")
            .append("text")
        //    .attr("transform", transform)
            .attr("x",len)
            .attr("y",yvalue)
            .attr("fill", color(i))
//            .style("stroke", color(i))
            .attr("dy",dyvalue )
            .style("text-anchor", "end")
            .attr("class", "a")
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(measureArray[i].length>25){
                    return measureArray[i].substring(0, 25);
                }else {
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


    svg = d3.select("#" + div).select("g");
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(d);
//                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
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
          //      .attr("onclick", fun)
//                .on("mouseover", function(d, i) {
//                    show_details(d, columns, measureArray, this,div);
//                })
//                .on("mouseout", function(d, i) {
//                    hide_details(d, i, this);
//                });
    }
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
//    var ageNames1 = [];
//    showLegends2(measureArray, colorArr, width, svg);
  
}
}
function buildMultiMeasureTRLine(div, columns, measureArray, divWid, divHgt) {

       d3.json("json/wordcloud.json", function (json) {
        var records = 10;
        var currData = [];
        for(var i=0; i< (json.length < records ? json.length:records);i++){

           currData.push(json[i]);
        }


buildMultiMeasureLine2(currData)

    })
    buildMultiMeasureLine2 = function(data){

     var color = d3.scale.category10();
     divWid=parseInt($(window).width())*(.35);
    var measure1 = measureArray[0];
    var autoRounding1;
    var measArr = [];
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
   //    var chartData = JSON.parse($("#chartData").val());
//       var colIds = chartData[div]["viewIds"];
     var chartData ;
      var colIds;
       var prop = graphProp1(div);

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
 width = divWid, //- margin.left - margin.right
            height = parseInt(divHgt)* .65;
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);
    var y = d3.scale.linear().domain([minVal, max]).range([height, 0]);
    // Axis setting
    var xAxis = d3.svg.trendaxis()
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
//                        return addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
              //          return numberFormat(d,yAxisFormat,yAxisRounding);
               return addCommas(d);
              return
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

    var svg = d3.select("#" + div).append("svg")
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
            yvalue=500;
        dyvalue = -150;
        }else{
        len += 100;
        yvalue=500;
        dyvalue = -150;
     }
    }else {
        len +=100;
        yvalue=518;
         dyvalue = -150;
     }
   svg.append("g")
            .attr("class", "y axis")
            .append("text")
        //    .attr("transform", transform)
            .attr("x",len)
            .attr("y",yvalue)
            .attr("fill", color(i))
//            .style("stroke", color(i))
            .attr("dy",dyvalue )
            .style("text-anchor", "end")
            .attr("class", "a")
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(measureArray[i].length>25){
                    return measureArray[i].substring(0, 25);
                }else {
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


    svg = d3.select("#" + div).select("g");
    d3.transition(svg).select('.y.axis')
            .call(yAxis)
            .selectAll('text')
             .text(function(d) {
             if(typeof d != "undefined" ){
              if(yAxisFormat==""){
                        flag = addCommas(d);
//                        flag = addCommas(numberFormat(d,yAxisFormat,yAxisRounding));
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
            //    .attr("onclick", fun)
//                .on("mouseover", function(d, i) {
//                    show_details(d, columns, measureArray, this,div);
//                })
//                .on("mouseout", function(d, i) {
//                    hide_details(d, i, this);
//                });
    }
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//    }
//    else {
//        buildCircledrill(height);
//    }
//    var ageNames1 = [];
//    showLegends2(measureArray, colorArr, width, svg);

}
}

function buildText(div,columns,measureArray){

d3.json("json/wordcloud.json", function(json){
  buildtext1(json)  ;
})
buildtext1 = function(data){
    var sum1=0;
    
    for(var i in data){
    sum1 += parseInt(data[i][measureArray]);
    }
    var htmlstr ="";
      
    htmlstr+= "<div style='margin-right:4%;float:right;width:40%' >";
    htmlstr+= "<table  style ='margin-right: 3%;margin-top:70%;float:right' >";
    htmlstr+= "<tr>";
    htmlstr+= "<td  style ='float:right'><h4 style='color:green;'><font  face='WildWest'>This is the Overall Enrollment of boys and girls</h4></td>";
    htmlstr+= "</tr>";
    htmlstr+= "</table>";
    htmlstr+= "</div>";
     htmlstr+= "<div style='margin-left:3%;float:left;width:40%' >";
     htmlstr+= "<table style='margin-top:100%'>";
     htmlstr+= "<tr>";
     htmlstr+= "<td><h1  style='color:green;'><font size='6' face='WildWest'>"+sum1+"</h1></td>";
    htmlstr+= "</tr>";
    htmlstr+= "</div>";


    $('#'+div).html(htmlstr);
}
}

