// India-Map
// US-City-Map            // US-Map
// Combo-India-Map       // India-City-Map
// Australia-Map        // Australia-City-Map
// Combo-US-Map        // Combo-US-City-Map
// Combo-Aus-State-Map// Combo-Aus-City-Map
// Andhra-Pradesh     //Telangana
var uscity={};

function buildDistrictMap1(div, data, columns, measureArray, divWid, divHgt) {
var color = d3.scale.category12();
//    var maindiv = divWid;
    var DELAY = 300, clicks = 0, timer = null;
    var chartMapData = data[div]
    var chart12 = data[div];
    var districtData = data[div];
//    var chartMapData = data[div];
    graphProp(div);
    var fun = "clicked(this.id,'"+div+"')";
    var chartData = JSON.parse($("#chartData").val());
    columns = chartData[div]["viewBys"];
    var mapColumns = chartData[div]["viewBys"];
    measureArray = chartData[div]["meassures"];
    var mapColumnsDistrict = chartData[div]["viewBys"];
    
    var measureArrayDistrict = chartData[div]["meassures"];
    var colIds = chartData[div]["viewIds"];
    
//    $("#" + div).append("<div id ='mapDiv" + div + "' style = 'height:100%;float:right;width:100%;'>");
    var margin = {
        top: 10,
        right: 12,
        bottom: 30,
        left: 70
    };
    divWid = screen.width * .5;
//    divHgt = $("#" + div).height() *1.2;
    divHgt = screen.height*.5;
//    active = d3.select(null);
//    alert(divWid)
//    alert(divHgt)
    var colorFlag = true;
    var color1 = getDrawColor(div, parseInt(0));
    var measure = measureArray[0];
    var isShadedColor = true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap = chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if (typeof colorMap !== "undefined") {
    keysMap = Object.keys(colorMap)
    }
    if (typeof colorMap == "undefined" || keysMap.length == 0) {
        colorMap = {};
        colorMap["measure"] = measureArray[0];
        colorMap[measureArray[0]] = color1;
    }// add for color scale
    var shadingMeasure = "";
    var logicalMap = chartData[div]["logicalColorDiv"];
    var logicalParameters = {};
    
    if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
    } else {
            var map = colorMap;
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
    var ht= divHgt*1.17;
//var offset=5;
//if(ht<divWid){
//if (screen.width == 1024 && screen.height == 768) {
//        var projection = d3.geo.mercator()
//        .scale(2000)
//        .translate([divWid / 3, ht / 2]);
//    
//   
////    var projection = d3.geo.mercator()
////            .scale(4500)
////            .translate([divWid / 2, ht / 1.5]);
//}else{
//alert(divWid)
    var projection = d3.geo.mercator()
            .scale(divWid*6)
            .translate([divWid / 1.85, divHgt / 1.5]);
//}

    var projection1 = d3.geo.mercator()
            .scale(divWid*6);

    var path1 = d3.geo.path()
    .projection(projection1);

    var path = d3.geo.path()
    .projection(projection);

    var svg = d3.select("#" + div)
    .append("svg")
            .attr("viewBox", "0 0 " + (divWid + margin.left + margin.right) + " " + (ht) + " ")
    .style("float", "left");

    var g = svg.append("g")
            .attr("transform", "translate(" + (divWid * (-1.5)) + " " + (divHgt * .60) + ") ")
    .style("stroke-width", "1.5px");

    d3.json("JS/states-tel.json", function(error, json) {
      g.selectAll("path")
        .data(json.features)
        .enter()
        .append("path")
        .attr("d", path)
        .attr("fill", function(d, i) {
            var currName = (d.id).toUpperCase();
           var colorShad = "#f2f2f2"; 
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                var dataValue=d["id"]; // update by mayank sharma
                var index=0;
                for(index in data[div]){
                    if(data[div][index][columns[0]]===dataValue){
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,index,color)
                return colorfill;
                        break;
                    }
                }
                
            }
            return colorShad;
        })
          .attr("color_value", function(d, i) {
            var currName = (d.id).toUpperCase();
            var colorShad = "#f2f2f2"; ;
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                var dataValue=d["id"]; 
                var index=0;
                for(index in data[div]){
                    if(data[div][index][columns[0]]===dataValue){
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,index,color)
                return colorfill;
                        break;
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
            return d.id;
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
                        for (var t = 0; t < measureArray.length; t++) {
                            if (parent.isFormatedMeasure) {
                                msrData = numberFormat(chartMapData[l][measureArray[t]], round, precition)
                            }else {
                                msrData = addCommas(chartMapData[l][measureArray[t]]);
                            }
                            content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                        }
                        return tooltip.showTooltip(content, d3.event);
                    }else{
                        msrData = "";
                    }
                }
            }
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
        
        .on("click",function(d,i){ 
            clicks++;  
            if(clicks === 1) {
                var currName = (d.id).toUpperCase();
                timer = setTimeout(function() {
                    drillWithinchart(currName,div)
                    clicks = 0;  
                }, DELAY);
            } else {
                clearTimeout(timer); 
                clicks = 0;    
            }
        })
        
        
        g = svg.append("g")  // add for state name
        .attr("transform", "translate(" + (divWid * (-1.5)) + " " + (divHgt * .60) + ") ")
        .style("stroke-width", "1.5px");
        g.selectAll("path")
            .data(json.features)
            .enter()
            .append("text")
            .attr("x", function(d,i) { 
                 return path.centroid(d)[0]-5;   
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1]+5;
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size","10px")
            .style("font-weight","bold")
            .text(function(d,i){
                var currName = (d.name1).toUpperCase();
             for (var k = 0; k < chartMapData.length; k++) {
                   if (currName.toLowerCase() == chartMapData[k][mapColumns[0]].toLowerCase()) {
                        return currName;
                    }
                }
                return d.name1;
            })
        
        
         });
          function clicked(d,i) { 
        showState(this.id, d);
    }
    if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
        var html = "<div id='legendsScale1" + div + "' class='legend2' style='float:right;width:200px;margin-right: 15%;margin-top:-35%;'></div>";
    } else {
        html = "<div id='legendsScale1" + div + "' class='legend2' style='float:right;margin-right: 15%;margin-top:-35%;'></div>";
}
    $('#' + div).append(html);
    var svg1 = d3.select("#legendsScale1" + div)
        .append("svg")
        .attr("width", "auto")
        .attr("height", "100%");
                  
if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){  
     var logicalParameters=chartData[div]["logicalParameters"]; 
        for(var i in logicalParameters){
            var value='', value2=''; 
            if(i==0){
                value = "High";
                value2='';
                var widthvalue = "10";
                var widthRectvalue = "20";
            }else if(i==1){
                value = "Mid";
                value2="1";
                widthvalue = "30";
                widthRectvalue = "40";
        }else{
                value = "Low";
                value2="2";
                widthvalue = "50";
                widthRectvalue = "60";
        }
            var conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            svg1.append("g")
        .append("rect")
        .attr("width", 24)
        .attr("height", 15)
            .attr("transform", "translate(9," + (widthvalue) + ")")
        .attr("stroke", "black")
        .attr("dy", ".35em")
            .style("fill", conditionalMap["logical"+value+"color"]);
        
            svg1.append("g")
            .append("text")
            .attr("dy", ".35em")
            .attr("transform", "translate(35," + (widthRectvalue) + ")") 
            .text(function(){
                if (conditionalMap["condition"+value2+""] === "<>") {
                    return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                } else {
                    return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
                }
            });
            $("#legendsScale1"+div).css("margin-right", "-2%");
                 }
        }else{  
//            var colorMap = JSON.parse(parent.$("#measureColor").val());
            var height = $("#legendsScale1"+div).height() - 10;
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);
                       
            var gradient = svg1.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "70%")
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

            svg1.append("g")
            .append("rect")
            .attr("width", 8)
            .attr("height", "80%")
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")")
            .attr("x", 0)
            .attr("y", 15);
            svg1.append("g")
            .append("text")
            .attr("x", 10)
            .attr("y", (height-10))
            .attr("dy", ".35em")
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div))); 
            svg1.append("g")
            .append("text")
            .attr("x", 10)
            .attr("y", 20)
            .attr("dy", ".35em")
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div))); 
            $("#legendsScale1"+div).css("width", "10%");
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
            k = 2.3;
            y = y + parseInt(y * (40 / 100)) + 150;
            centered = d;
        } else {
            x = divWid / 2;
            y = divHgt / 2;
            k = 1;
            centered = null;
        }
        tooltip.hideTooltip();
        $("#" + div).html("");
        
   var html1 = "<span style='float:right;margin-top:"+divHgt*.10+"px;margin-right:"+divHgt*.20+"px;;color:blue' id='stName'>"+state+"</span>"
        $("#" + div).append(html1);
        var html = "";
        var svgcanvas;

        var data = chart12[0][columns];
        if (parent.$("#isShaded").val() == "true") {
            map = d3.select('#maptDiv'+div).append("svg:svg")
            .attr("width", divWid * 1.7)
            .attr("height", divHgt);
            svgcanvas = map.append("g")
            .attr("width", divWid * 1.7)
            .attr("height", divHgt);
            $('#'+div).append(html);
            $("#legendsScale").show();
        } else { 
            map = d3.select('#'+div).append("svg:svg")
            .attr("width", divWid * 2)
            .attr("height", divHgt);
            svgcanvas = map.append("g")
            .attr("width", divWid * 2)
            .attr("height", divHgt);
        }
        
        var svgcanvas1 = svgcanvas.append("g")
        .attr("id", "places1");
        var  data1 = [];
        var cordMap = {};
        var count = -1;
        var currNameDistrict ;
        var htmlframe = "<iframe id='maps' frameborder='0' style='margin-top:-80px' width='600px' height='550px'></iframe>";
        $('#'+div).attr("style", "display:block");
        $("#"+div).append(htmlframe);
        $("#maps").attr("src", "svg/" + state.toLowerCase() + ".svg").load(function() {
            var $map = $(this).contents();
            var titles = $map.find('path').map(function() {
                var parenttitle = $(this).parent().attr('title') || '';
                var stateMap = {};
                stateMap["title"] = $(this).attr('title');
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
                    svgG.append("text")
                    .style("text-anchor","middle")
                    .attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")")
                    .text(function(d,j){
                        return data1[counter]["title"];
                    })
                    .style("font-size","7px");
                    var district = $(this).attr('title');
                    $(this).attr('fill', function(){
                        count++;
                        var colorShad;
                        var drilledvalue;
                        try {
                            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        } catch (e) {
                        }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                            colorShad = drillShade;
                        } else {
                            if(districtData!="undefined" && districtData!=""){
                                for(var i in districtData){
                                    if(district.toLowerCase()== districtData[i][mapColumnsDistrict[0]].toLowerCase()){ 
                                        return color(i);
                                    }else{
                                       return color(i);
                                    }
                                }
                            }
                        }
                        return colorShad;
                    })
                });
            $(this).attr('onclick', function(){
                var districtArray = [];
                $map.find('g').each(function() {
                    $(this).find('path').each(function(d,i) {
                        var district = $(this).attr('title');
                        districtArray.push(district)
                        $(this).on("click", function(){
                            })
                    })
                })
            })
            })
        var abc = state.toLowerCase();
        });
    function show_detailsOfMap(d, columnVal, measureArray, con, i, data) {
        var content;
        content = "<span class=\"name\">" + columnVal + ":</span><span class=\"value\"> " + d + "</span><br/>";
        var msrData;
        var val;
        for (var l = 0; l < data.length; l++) {
            if (d.toLowerCase() == data[l][columnVal].toLowerCase() || (d.toLowerCase() == "chatisgarh" && data[l][columnVal].toLowerCase() == "chhattisgarh")) {
                for (var t = 0; t < measureArray.length; t++) {
                    if (parent.isFormatedMeasure) {
                        msrData = numberFormat(data[l][measureArray[t]], round, precition)
                    }else {
                        msrData = addCommas(data[l][measureArray[t]]);
                    }
                    content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                }
                return tooltip.showTooltip(content, d3.event);
            }else{
                msrData = "";
            }

        }
    }

}
}

function buildUStateMap(div, data, columns, measureArray, wid, hgt, drillId) {
    hgt = $("#" + div).height();
    wid = $("#" + div).width();
    var chartData = JSON.parse($("#chartData").val());
   var appendDiv = "";
   graphProp(div);
if(div.indexOf("USdiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}
  
    var color = d3.scale.category12();
 if(chartData[div]["chartType"]=== "Combo-US-Map"){  
    var chartMapData = data;
 }else{
    var chartMapData = data[div];
 }
 if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
    var fun = "clicked(this.id,'"+div+"','comboDrill','"+drillId+"')";
 }else{
    var fun = "clicked(this.id,'"+div+"')";
 }

    if (chartData[div]["chartType"] === "Combo-US-Map") {
        $("#USdiv_" + div).append("<div id ='USdiv1USdiv_" + div + "' style = 'height:auto;float:right;width:100%;'>");
    }else{
    $("#"+div).append("<div id ='USdiv1"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }

    active = d3.select(null);
    
    var colIds = chartData[div]["viewIds"];
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
       colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
        } else {
            var map = colorMap;
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
    var conditionalMap = {};
    
    if (chartData[div]["chartType"] === "Combo-US-Map") {
        wid = screen.width * .5;
        hgt = screen.height * .65;
    var projection = d3.geo.albersUsa()
    .scale(700)
         .translate([wid / 4, hgt / 2.5]);
    var projection1 = d3.geo.albersUsa()
    .scale(800);
    var path1 = d3.geo.path()
    .projection(projection1);
//    if (screen.width == 1024 && screen.height == 768) {
//        projection = d3.geo.albersUsa()
//        .scale(640)
//                    .translate([wid / 3, hgt / 3]);
//    }
    } else {

    var projection = d3.geo.albersUsa()
        .scale(hgt * 1.52)
        .translate([wid / 4, hgt / 2.9]);
    var projection1 = d3.geo.albersUsa()
        .scale(hgt * 1.52);
    var path1 = d3.geo.path()
    .projection(projection1);
    }
    var path = d3.geo.path()
    .projection(projection);

    if (chartData[div]["chartType"] === "Combo-US-Map") {
        var svg = d3.select("#USdiv1USdiv_" + div)
    .append("svg")
                .attr("viewBox", "0 10 " + (wid * .92) + " " + (hgt * .95) + " ")
    .style("float", "left");
    } else {
        var svg = d3.select("#USdiv1" + div)
    .append("svg")
                .attr("viewBox", "0 -20 " + (wid * .92) + " " + (hgt * .95) + " ")
    .style("float", "left");
}

    var g = svg.append("g")
            .attr("transform", "translate(" + (wid * .21) + "," + (hgt * .05) + ")")
    .style("stroke-width", "1.5px");
    d3.json("JS/us-States.json", function(error, us) {
      g.selectAll("path")
        .data(us.features)
        .enter()
        .append("path")
        .attr("d", path)
        .attr("id",function(d,i){
            var currName = (d.properties.name).toUpperCase();
            for (var l = 0; l < chartMapData.length; l++) {
                if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
                    return chartMapData[l][columns[0]]+":"+colIds[0];
                }
            }
            return "";
        })
        .attr("fill", function(d, i) {
            var currName = (d.properties.name).toUpperCase();
            if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
            var colorShad = "#f2f2f2";
            }else{
             colorShad = "#f2f2f2";
            return colorShad;
            }
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                for (var k = 0; k < chartMapData.length; k++) {
                    if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                        var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,k,color)
                        return colorfill;
                    }
                }
            }
            return colorShad;
        })
          .attr("color_value", function(d, i) {
            var currName = (d.properties.name).toUpperCase();
            if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
            var colorShad = "#f2f2f2";
            }else{
             colorShad = "#f2f2f2";
            return colorShad;
            }
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                for (var k = 0; k < chartMapData.length; k++) {
                    if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                        var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,k,color)
                        return colorfill;
                    }
                }
            }
            return colorShad;
        })
        .attr("stroke", "#000")
        .attr("onclick", fun)
        .on("mouseover", function(d, i) {
           if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
           for (var k = 0; k < chartMapData.length; k++) {
                if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                    show_detailsUSMAP(chartMapData[k], columns, measureArray, this);
                }
            }
           }else{
               return "";
           }
        })
        .on("mouseout", function(d, i) {
            if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
            hide_details(d, i, this);
           }else{
               return "";
           }
        });
       
    
       g = svg.append("g")
                .attr("transform", "translate(" + (wid * .21) + "," + (hgt * .05) + ")")
        .style("stroke-width", "1.5px");
        g.selectAll("path")
            .data(us.features)
            .enter()
            .append("text")
                .attr("x", function(d, i) {
            return path.centroid(d)[0] - 10;
            })
                .attr("y", function(d, i) {
            return path.centroid(d)[1] + 2;
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
            } else {
                       lableColor = "#000000";
            }
                               return lableColor;
            })
            .style("font-size", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-US-Map"){
            return "12px";
        }else{
            return "8px";
        }
            })
            .text(function(d,i){
               var currName = (d.name1);
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.properties.name;
           })
        
       
        if (typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
        } else {
       g = svg.append("g")
                    .attr("transform", "translate(" + (wid * .21) + ",0)")
        .style("stroke-width", "1.5px");
        d3.json("JS/us-States.json", function(error, us) {
            g.selectAll("path")
            .data(us.features)
            .enter()
            .append("rect")
            .attr("x", function(d,i) { 
                return path.centroid(d)[0]+2;
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1]-20;
            })
            .attr("width",10)
            .attr("height",30)
            .attr("id",function(d,i){
                var currName = (d.properties.name).toUpperCase();
                for (var l = 0; l < chartMapData.length; l++) {
                    if (currName.toLowerCase() == chartMapData[l][columns[0]].toLowerCase()) {
                        return chartMapData[l][columns[0]]+":"+colIds[0];
                    }
                }
                return "";
            })
            .attr("fill", function(d, i) {
                var currName = (d.properties.name).toUpperCase();
                var colorShad = "#f2f2f2";
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                    colorShad = drillShade;
                } else { 
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,k,color)
                            return colorfill;
                        }
                    }
                }
                return colorShad;
            })
            .attr("color_value", function(d, i) {
                var currName = (d.properties.name).toUpperCase();
                var colorShad = "#f2f2f2";
                var drilledvalue;
                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                    colorShad = drillShade;
                } else { 
                    for (var k = 0; k < chartMapData.length; k++) {
                        if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,k,color)
                            return colorfill;
                        }
                    }
                }
                return colorShad;
            })
            .attr("stroke",function(d,i){
                for (var k = 0; k < chartMapData.length; k++) {
                    if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                        return "grey";
                    }
                }
                return "transparent";
            })
            .style("opacity",function(d,i){
                for (var k = 0; k < chartMapData.length; k++) {
                    if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                        return "0.85";
                    }
                }
                return "-.1";
            })
            .style("stroke-width", ".5px")
        .attr("onclick", fun)
        .on("mouseover", function(d, i) {
            for (var k = 0; k < chartMapData.length; k++) {
                if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                    show_detailsUSMAP(chartMapData[k], columns, measureArray, this);
                }
            }
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        });
        })
    }
                
      
                var yvalue=0;
		var rectyvalue=0;
                var legendLength;
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
 }else{
                    legendLength="3"; 
                }
            var rect2 = parseInt(hgt*.85); 
            var widthvalue =((hgt/2)-(rect2 /2.7));
            
      if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){ 
            if(wid<hgt){
                yvalue = parseInt(hgt*0.9);
                rectyvalue = parseInt((hgt*0.89 ));
    }else{
                yvalue = parseInt(hgt*1.05);
                rectyvalue = parseInt((hgt*1.030 ));
 }      
                  
            var widthRectvalue2 = parseInt((wid *.1)-(wid*.018));
               var logicalParameters=chartData[div]["logicalParameters"]; 
            var count=0;         
                 for(var i in logicalParameters){
                 var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                 widthRectvalue2 = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                 widthRectvalue2 = parseInt(widthRectvalue2 + (wid * .18))
                 }else{
                     value = "Low";
                     value2="2";
                 widthRectvalue2 = parseInt(widthRectvalue2 + (wid * .18))
                 }
                
                    if (count % 5 == 0) {
                    widthRectvalue2 = parseInt((wid * .25) - (wid * .018));
                    if (wid < hgt) {
                        yvalue = parseInt(yvalue - wid * .06)
                        rectyvalue = parseInt(rectyvalue - wid * .06)
                    } else {
                        yvalue = parseInt(yvalue - hgt * .06)
                        rectyvalue = parseInt(rectyvalue - hgt * .06)
                    }
                    }
            conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            
            svg.append("g")
        .append("rect")
                .attr("width", 24)
        .attr("height", 15)
           .attr("transform", "translate(" + widthRectvalue2  + ","+(rectyvalue*.82)+")")
                .attr("stroke", "black")
        .attr("dy", ".35em")
                .style("fill", conditionalMap["logical"+value+"color"]);
                                    
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthRectvalue2+30)  + ","+(yvalue*.82+5)+")")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-US-Map"){
            return "12px";
             }else{
            return "10px";
            }
            })
                .text(function(){
                    if (conditionalMap["condition"+value2+""] === "<>") {
                        return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                    } else {
                        return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
                    }
                });
           count++;
            }
        }else{  
            
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);
            var rectsize = parseInt(wid*.85);
            var widval =((wid/2)-(rectsize /3));
              if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
                  if(wid<hgt){
                yvalue = parseInt(hgt*0.9);
                rectyvalue = parseInt((hgt*0.89 ));
            }else{
                yvalue = parseInt(hgt*1.05);
                rectyvalue = parseInt((hgt*1.030 ));
            }
            var widthval = parseInt((wid * .25));//.1
             var widthRectvalue = parseInt((wid * .25) - (wid * .018));
            if (i != 0) {    
                widthval = parseInt(widthval + (wid * .18))
                widthRectvalue = parseInt(widthRectvalue + (wid * .18))
                       
                    if (count % 5 == 0) {
                    widthval = parseInt((wid * .25));
                    widthRectvalue = parseInt((wid * .25) - (wid * .018));
            
                    if (wid < hgt) {
                        yvalue = parseInt(yvalue - wid * .06)
                        rectyvalue = parseInt(rectyvalue - wid * .06)
                    } else {
                        yvalue = parseInt(yvalue - hgt * .06)
                        rectyvalue = parseInt(rectyvalue - hgt * .06)
                    }

                    }
              }
                  
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "75%")
            .attr("y1", "30%")
            .attr("x2", "0%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
              }else{
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "30%")
            .attr("y1", "85%")
            .attr("x2", "30%")
            .attr("y2", "0%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
              }

            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", colorMap[shadingMeasure])
            .attr("stop-opacity", 1);
            gradient.append("svg:stop")
            .attr("offset", "100%")
            .attr("stop-color", "rgb(230,230,230)")
            .attr("stop-opacity", 1);

             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style","margin-top:-3%")
            .attr("style", "overflow:scroll")
            .attr("transform", function(d){
        if(chartData[div]["chartType"]=== "Combo-US-Map"){
                  return "translate(" + (widval)  + "," + (rectyvalue*.80) + ")";
        }else{
                  return "translate(" + ( wid*.85)  + "," + (widthvalue) + ")";
        }
            })
            .attr("height", function(d){
        if(chartData[div]["chartType"]=== "Combo-US-Map"){
               return "10px";
        }else{
               return "40%";
        }
            })
            .attr("width", function(d){
              if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
               var rectwid = parseInt(wid*.85);
               return rectwid*.40;
       }else{
               return "8px";
     }
            })
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")");
 
             svg.append("g")
            .append("text")
            .attr("transform", function(d){
              if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
                  return "translate(" + (widval)  + "," + (rectyvalue*.80+20) + ")";
                          }else{
                  return "translate(" + (wid*.87)  + "," + (widthvalue+10)+ ")";
                               }
            })
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-US-Map"){
            return "12px";
        }else{
            return "10px";
        }
            })
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div)));
                    
            svg.append("g")
                .append("text")
            .attr("x", function(d){
              if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
                  return (widval+rectsize*.37); 
        }else{  
                  return wid*.87;
            }
            })
            .attr("y", function(d){
              if(chartData[div]["chartType"]=== "Combo-US-Map"){ 
                  return (rectyvalue*.80+20);
            }else{
                  return "60%";
            }
            })
//            .attr("dy", ".35em")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-US-Map"){
            return "12px";
            }else{
            return "10px";
            }
            })
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div)));
        }



    });

    clicked = function(id,divId,flag2,drillId) {
        drillWithinchart1(id,divId,flag2,drillId)
    }

    function reset() {
        active.classed("active", false);
        active = d3.select(null);

        g.transition()
        .duration(750)
        .style("stroke-width", "1.5px")
        .attr("transform", "");
    }
    function show_detailsUSMAP(data, columns, measureArray){
        var content="";
        for(var i in measureArray){ 
            content += "<span style=\"font-family:helvetica;\" class=\"name\">" +addCommas(data[measureArray[i]])+ "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ data[columns[0]] +"</span><br/>";
                }
                return tooltip.showTooltip(content, d3.event);
            }
        }



function buildComboIndiaMap(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
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
    htmlvar += "<div id='kpiPieDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='kpiDiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
            if(typeof uscity["kpiDiv_"+div]==="undefined" || window.drillID!=="kpiDiv_"+div)
             uscity["kpiDiv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="kpiDiv_"+div){
                 data3 = JSON.parse(uscity["kpiDiv_"+div]);
             }
            parent.$("#driver").val('');
            buildComboDistrictMap1("kpiDiv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"kpiDiv_"+div)

        }
    });
//
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
        if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)

                    }
});
//     
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
         if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
    }
})
    
}
//
function buildComboDistrictMap1(div,data,columns,measureArray,divWid,divHgt,drillId){
     var appendDiv = "";
if(div.indexOf("kpiDiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]
}else{
appendDiv = div
}
    var color = d3.scale.category12();
    var DELAY = 300, clicks = 0, timer = null;  // for drill add by mayank sharma
    var chart12 = data;
    var districtData = data;
    var chartMapData = data;
    var chartData = JSON.parse(parent.$("#chartData").val());
    

    columns = chartData[div]["viewBys"];
    var mapColumns = chartData[div]["viewBys"];  // update by mayank sharma
    measureArray = chartData[div]["meassures"];
    var mapColumnsDistrict = chartData[div]["viewBys"];
    graphProp(div);
    var measureArrayDistrict = chartData[div]["meassures"];
    var colIds = chartData[div]["viewIds"];
    var w = divWid+30;
    var h = 550;
    var proj = d3.geo.mercator();
    var path = d3.geo.path().projection(proj);
    var t = proj.translate(); // the projection's default translation
    var s = proj.scale() // the projection's default scale

//    var fun = "drillWithinchart(this.id,\""+div+"\",'comboDrill',\""+drillId+"\")";  
     var fun = "clicked(this.id,\""+div+"\",'comboDrill',\""+drillId+"\")"; // for drill add by mayank sharma
    var districtmapchartData = data;
    
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var measure= measureArray[0];
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
        colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    var conditionalMeasure = "";
    var conditionalMap = {};
    var conditionalShading = false;
    if (parent.$("#isShaded").val() == "true" || colorFlag) {
        if (parent.$("#shadeType").val() == "conditional") {
            conditionalShading = true;
            conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
            conditionalMeasure = $("#conditionalMeasure").val();
        } else if (parent.$("#shadeType").val() == "standard") {
        } else {
            var map =colorMap;
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
    } else {
        if (parent.isCustomColors) {
            color = d3.scale.ordinal().range(parent.customColorList);
        }
    }
    
    var html1 = "<div id = 'mapDivkpiDiv_"+div+"' style='height:auto;overflow-y:auto;float:left;width:auto;'></div>";
    $("#kpiDiv_"+div).append(html1);
    var map = d3.select("#mapDivkpiDiv_"+div)
    .append("svg")
     .attr("id", "svg_kpiDiv_" + div)
    .attr("width", (w+20) )
    .attr("height", (divHgt+55))
    .style("margin-top", "-40px")
    .style("margin-left", "-50px")
            
    .call(initialize);
    var india = map.append("svg:g")
    .attr("id", "india");
    d3.json("JS/states-tel.json", function (json) {
        india.selectAll("path")
        .data(json.features)
        .enter()
        .append("path")
        .attr("d", path)
//        .on("dblclick", clicked)
        .attr("fill", function(d,i){
            var currName = (d.id).toUpperCase();
            var colorShad;
            var drilledvalue;
//            $("#viewMeasureBlock").hide();
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                var dataValue=d["id"]; // update by mayank sharma
                var index=0;
                for(index in data){
                    if(data[index][columns[0]]===dataValue){
                    
                        break;
                    }
                }
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,index,color)
                return colorfill;
            }
            return colorShad;
        })
        .attr("color_value", function(d,i){ // add by mayank sharma
            var currName = (d.id).toUpperCase();
            var colorShad;
            var drilledvalue;
//            $("#viewMeasureBlock").hide();
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else { 
                var dataValue=d["id"]; 
                var index=0;
                for(index in data){
                    if(data[index][columns[0]]===dataValue){
                        break;
                    }
                }
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,index,color)
                return colorfill;
            }
            return colorShad;
        })
        .attr("onclick", fun)
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
                for (var l = 0; l < data.length; l++) {
                    if (currName.toLowerCase() == data[l][mapColumns[0]].toLowerCase() || (currName.toLowerCase() == "chatisgarh" && data[l][mapColumns[0]].toLowerCase() == "chhattisgarh")) {
                        for (t = 0; t < measureArray.length; t++) {
                            if (parent.isFormatedMeasure) {
                                msrData = numberFormat(data[l][measureArray[t]], round, precition)
            }else {
                                msrData = addCommas(data[l][measureArray[t]]);
                    }
                            content += "<span class=\"name\">" + measureArray[t] + ":</span><span class=\"value\"> " + msrData + "</span><br/>";
                }
                        return tooltip.showTooltip(content, d3.event);
                    }else{
                        msrData = "";
            }
        }
    }
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
        .attr("id",function(d,i){
            var currName = (d.id).toUpperCase();
            for (var l = 0; l < data.length; l++) {
                if (currName.toLowerCase() == data[l][mapColumns[0]].toLowerCase()) {
                    return data[l][mapColumns[0]]+":"+colIds[0];
}
            }
            return d.id;
        })
//        .on("click",function(d,i){  // update by mayank sharma
//            clicks++;  
//            if(clicks === 1) {
//                var currName = (d.id).toUpperCase();
//                timer = setTimeout(function() {
//                    drillWithinchart(currName,div)
//                    clicks = 0;  
//                }, DELAY);
//            } else {
//                clearTimeout(timer); 
//                clicks = 0;    
//            }
//        })
        
      var  g = map.append("g")  // add for state name
//                .attr("transform", "translate(" + (divWid * (-1.5)) + " " + (divHgt * .65) + ") ")
        .style("stroke-width", "1.5px");
        g.selectAll("path")
            .data(json.features)
            .enter()
            .append("text")
            .attr("x", function(d,i) { 
                 return path.centroid(d)[0];   
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1];
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size","8px")
            .style("font-weight","bold")
            .text(function(d,i){
                var currName = (d.name1).toUpperCase();
             for (var k = 0; k < chartMapData.length; k++) {
                   if (currName.toLowerCase() == chartMapData[k][mapColumns[0]].toLowerCase()) {
                        return currName;
                    }
                }
                return d.name1;
            })
    });

 clicked = function(id,div,flag2,drillId) {
        drillWithinchart1(id,div,flag2,drillId)
    }
//    function clicked(d,i) { // update by mayank sharma
//        showState(this.id, d);
//    }
//    var width = $(window).width()-70;
//    var height = $(window).height();
//    var projection = d3.geo.mercator()
//    //            .center([-20, 60])
//                .scale(750).translate([width/2.5, height/1.8]);
    function initialize() {
        proj.scale(3850);
        proj.translate([-660, 470]);
//        proj.translate([-divWid*.85, divHgt]);
    }
   if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
       var html = "<div id='legendsScalekpiDiv_"+div+"' class='legend2' style='float:right;margin-right:25%;margin-top:-40%;'></div>"; // add by mayank sharma
$('#kpiDiv_'+div).append(html);
        var svg1 = d3.select("#legendsScalekpiDiv_"+div)
                  .append("svg")
        .attr("width", "auto")
                  .attr("height", "100%");
 }else{
       var html = "<div id='legendsScalekpiDiv_"+div+"' class='legend2' style='float:right;margin-right:25%;margin-top:-40%;'></div>"; // add by mayank sharma
       $('#kpiDiv_'+div).append(html);
        var svg1 = d3.select("#legendsScalekpiDiv_"+div)
        .append("svg")
        .attr("width", "auto")
        .attr("height", "100%");
    }
                  
     if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
     var logicalParameters=chartData[div]["logicalParameters"]; 
        for(var i in logicalParameters){
            var value='', value2=''; 
            if(i==0){
                value = "High";
                value2='';
                var widthvalue = "10";
                var widthRectvalue = "20";
            }else if(i==1){
                value = "Mid";
                value2="1";
                widthvalue = "30";
                widthRectvalue = "40";
        }else{
                value = "Low";
                value2="2";
                widthvalue = "50";
                widthRectvalue = "60";
        }
            var conditionalMap = logicalParameters[i];
        var keys = Object.keys(conditionalMap);
        svg1.append("g")
        .append("rect")
                                    .attr("width", 24)
        .attr("height", 15)
            .attr("transform", "translate(9," + (widthvalue) + ")")
                                    .attr("stroke", "black")
        .attr("dy", ".35em")
            .style("fill", conditionalMap["logical"+value+"color"]);
                                    
            svg1.append("g")
            .append("text")
                                    .attr("dy", ".35em")
            .attr("transform", "translate(35," + (widthRectvalue) + ")") 
            .text(function(){
                if (conditionalMap["condition"+value2+""] === "<>") {
                    return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                                        } else {
                    return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
                                        }
                                    });
//            $("#legendsScale"+div).css("margin-right", "-2%");
                        }
                  }else{  
//                        var colorMap = JSON.parse(parent.$("#measureColor").val());
                        var height = $("#legendsScale"+div).height() - 10;
                        var shadingMeasure = colorMap["measure"];
                        var max = maximumValue(chart12, shadingMeasure);
                        var min = minimumValue(chart12, shadingMeasure);
                       
                        var gradient = svg1.append("svg:defs")
                                .append("svg:linearGradient")
                                .attr("id", "gradientWrdMapLegend1"+div+"")
                                .attr("x1", "0%")
                                .attr("y1", "30%")
                                .attr("x2", "70%")
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

                        svg1.append("g")
                            .append("rect")
                            .attr("width", 10)
                            .attr("height", "60%")
                            .attr("fill", "url(#gradientWrdMapLegend1"+div+")")
                            .attr("x", 0)
                            .attr("y", 15);
                        svg1.append("g")
                            .append("text")
                            .attr("x", 10)
                            .attr("y", "60%")
                            .attr("dy", ".35em")
                             .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div)));
                        svg1.append("g")
                            .append("text")
                            .attr("x", 10)
                            .attr("y", 25)
                            .attr("dy", ".35em")
                             .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div)));
//                        $("#legendsScale"+div).css("width", "10%");
                    }

}


function buildAustraliaMap(div,data,columns,measureArray,divWid,divHgt,drillId) {
    divHgt=$("#"+div).height();
    divWid=$("#"+div).width();
     var chartData = JSON.parse(parent.$("#chartData").val());
    var appendDiv = "";
    graphProp(div);
    if(div.indexOf("AusStatediv_")!=-1){
        appendDiv = div
        var splitDiv = div.split("_");
         div = splitDiv[1]
       }else{
        appendDiv = div
     }
    
    var color = d3.scale.category12();
var maindivWid=divWid;
    var chart12 = data[div];
 if(chartData[div]["chartType"]=== "Combo-Aus-State"){  
    var chartMapData = data;
 }else{
   var chartMapData = data[div];
 }
    graphProp(div);
    var mapColumns = chartData[div]["viewBys"];
    measureArray = chartData[div]["meassures"];
    
if(chartData[div]["chartType"]=== "Combo-Aus-State"){  
     var fun = "drillWithinchart(this.id,\""+div+"\",'comboDrill',\""+drillId+"\")";
 }else{
     var fun = "drillWithinchart(this.id,\""+div+"\")";
 }
//    measureArrayDistrict = chartData[div]["meassures"];
    var colIds = chartData[div]["viewIds"];

if(chartData[div]["chartType"]=== "Combo-Aus-State"){  
    $("#AusStatediv_"+div).append("<div id ='mapdiv1AusStatediv_"+div+"' style = 'height:auto;float:right;width:100%;'>");
}else{
    $("#"+div).append("<div id ='mapdiv1"+div+"' style = 'height:auto;float:right;width:100%;'>");
}
    active = d3.select(null);
var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var measure= measureArray[0];
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
        colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    var logicalMap=chartData[div]["logicalColorDiv"];
    var logicalParameters = {};
  if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){   
    }else{  
            var map = colorMap;
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
        
    var h = divHgt;
    if(chartData[div]["chartType"]=== "Combo-Aus-State"){ 
    var w = divWid,
    h = divHgt;
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.1)
    .origin([135, -30])
    .translate([w*.45, h*.45]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.1);
    var path1 = d3.geo.path()
    .projection(projection1);
}else{
    if(maindivWid<520){
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.5)
    .origin([135, -21])
    .translate([divWid / 4, divHgt / 3.2]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.5);
    var path1 = d3.geo.path()
    .projection(projection1);
}else{
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.5)
    .origin([135, -20])
    .translate([divWid / 4, divHgt / 3.2]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.5);
    var path1 = d3.geo.path()
    .projection(projection1);
}
}
    var path = d3.geo.path()
    .projection(projection);
    
    
    if(chartData[div]["chartType"]=== "Combo-Aus-State"){  
      var svg = d3.select("#mapdiv1AusStatediv_"+div)
    .append("svg")
    .attr("viewBox", "0 0 "+(divWid*.92)+" "+(divHgt*.95)+" ")
    .style("float", "left"); 
    var g = svg.append("g")
    .style("stroke-width", "1.5px"); 
    }else{
    if(maindivWid<520){
    var svg = d3.select("#mapdiv1"+div)
    .append("svg")
    .attr("viewBox", "0 15 "+(divWid*.92)+" "+(divHgt*.95)+" ")
//    .attr("viewBox", "0 20 "+(divWid*.60)+" "+(divHgt*.95)+" ")
    .style("float", "left");

    var g = svg.append("g")
    .attr("transform","translate(" +(divWid*.23)+" 0) ")
    .style("stroke-width", ".2px");
    }else{
        var svg = d3.select("#mapdiv1"+div)
    .append("svg")
    .attr("viewBox", "0 15 "+(divWid*.92)+" "+(divHgt*.95)+" ")
    .style("float", "left");

    var g = svg.append("g")
    .attr("transform","translate(" +(divWid*.18)+" 0) ")
    .style("stroke-width", ".2px");
    }
    }
    d3.json("JS/australia.json", function(collection) {
      g.selectAll("path")
        .data(collection.features)
        .enter()
        .append("path")
        .attr("d", path)
         .attr("id",function(d,i){
           var currName = d.properties["STATE_NAME"];
            return currName+":"+colIds[0];
        })
         .attr("onclick", fun)
        .attr("fill", function(d,i){
            var currName = d.properties["STATE_NAME"];
            var colorShad="#f2f2f2";
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
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l,color)
                return colorfill;
            }
                 }
                 }
            return colorShad;
        })
        .attr("color_value", function(d,i){
            var currName = d.properties["STATE_NAME"];
            var colorShad="#f2f2f2";
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
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l,color)
                return colorfill;
            }
                 }
                 }
            return colorShad;
        })
        .on("mouseover", function(d, i) {
            var content;
        var currName = d.properties["STATE_NAME"];
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\">"+currName+"</span><br/>";
        var msrData ="";
        for(var e=0; e< measureArray.length;e++){
//        for (var l = 0; l < chartMapData.length; l++) {
            msrData=addCommas(chartMapData[i][measureArray[e]])
            content += "<span class=\"name\">" + measureArray[e] + ": </span><span class=\"value\">"+msrData+"</span><br/>";
//}
        }
                return tooltip.showTooltip(content, d3.event);
        })
                                    .on("mouseout", function(d, i) {
                                        hide_details(d, i, this);
                                    })
                                
//                                d3.json("JS/australia.json", function(collection) {
      g = svg.append("g")
    .attr("transform","translate(" +(divWid*.18)+" 0) ")
    .style("stroke-width", ".2px");                              
          g.selectAll("path")
            .data(collection.features)
            .enter()
            .append("text")
    .attr("id",function(d,i){ 
           var currName = d.properties["STATE_NAME"];
            return currName+":"+colIds[0];
        })
            .attr("x", function(d,i) { 
               return path.centroid(d)[0]-10;
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1];
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                       lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-Aus-State"){
            return "12px";
        }else{
            return "8px";
        }
            })
            .style("font-weight","bold")
            .text(function(d){
               var currName = d.name1;
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.properties["STATE_NAME"];
           })
        });                                
                                    
                                    
                var yvalue=0;
		var rectyvalue=0;
                var legendLength;
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                 }else{
                    legendLength="3"; 
            }
               var rect2 = parseInt(divHgt*.85); 
               var widthvalue3 =((divHgt/2)-(rect2 /3));
 
      if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
            if(divWid<divHgt){
                yvalue = parseInt(divHgt*0.9);
                rectyvalue = parseInt((divHgt*0.89 ));
      }else{
                yvalue = parseInt(divHgt*1.05);
                rectyvalue = parseInt((divHgt*1.030 ));
      }
                  
            var widthRectvalue = parseInt((divWid *.1)-(divWid*.018));
               var logicalParameters=chartData[div]["logicalParameters"]; 
            var count=0;         
                 for(var i in logicalParameters){
               if(chartData[div]["chartType"]=== "Combo-Aus-State"){
                 var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                     var widthvalue = parseInt((divWid*.1)-(divWid*.055));
                      var widthRectvalue = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                     widthvalue = parseInt(widthvalue+(divWid*.23));
                     widthRectvalue = parseInt(widthRectvalue + (divWid*.23));
                 }else{
                     value = "Low";
                     value2="2";
                     widthvalue = parseInt(widthvalue+(divWid*.23));
                     widthRectvalue = parseInt(widthRectvalue+(divWid*.23));
                 }
           }else{
              var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                     var widthvalue = parseInt((divWid*.1)-(divWid*.055));
                      var widthRectvalue = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                     widthvalue = parseInt(widthvalue+(divWid*.18));
                     widthRectvalue = parseInt(widthRectvalue + (divWid*.18));
                 }else{
                     value = "Low";
                     value2="2";
                     widthvalue = parseInt(widthvalue+(divWid*.18));
                     widthRectvalue = parseInt(widthRectvalue+(divWid*.18));
                 } 
           }
                    if (count % 5 == 0) {
                    if (divWid < divHgt) {
                        yvalue = parseInt(yvalue - divWid * .06)
                        rectyvalue = parseInt(rectyvalue - divHgt * .06)
                    } else {
                        yvalue = parseInt(yvalue - divHgt * .06)
                        rectyvalue = parseInt(rectyvalue - divHgt * .06)
                    }
                    }
           var conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            
            svg.append("g")
        .append("rect")
        .attr("width", 24)
           .attr("height", function(d){
              if(chartData[div]["chartType"]=== "Combo-Aus-State"){
            return "12px";
             }else{
            return "15px";
            }
             })
           .attr("transform", function(d){
    if(chartData[div]["chartType"]=== "Combo-Aus-State"){
        return "translate(" + (widthRectvalue+20)  + ","+(rectyvalue*.82-10)+")";
    }else{
         return "translate(" + (widthRectvalue+50)  + ","+(rectyvalue*.82)+")";
    }})
        .attr("stroke", "black")
           .style("margin-left","15%")
        .attr("dy", ".35em")
           .style("fill", conditionalMap["logical"+value+"color"]);
                                    
            svg.append("g")
            .append("text")
           .style("margin-left","15%")
    .attr("transform", function(d){
    if(chartData[div]["chartType"]=== "Combo-Aus-State"){
        return "translate(" + (widthRectvalue+50)  + ","+(rectyvalue*.82)+")";
    }else{
         return "translate(" + (widthvalue+50)  + ","+(yvalue*.82+5)+")";
    }})
            .style("font-size","10px")
                .text(function(){
                    if (conditionalMap["condition"+value2+""] === "<>") {
                        return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                    } else {
                        return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
                    }
    });
           count++;
     }
        }else{   
            
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);
            var rectsize = parseInt(divWid*.85);
            var widval =((divWid/2)-(rectsize /3));
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "30%")
            .attr("y1", "85%")
            .attr("x2", "30%")
            .attr("y2", "0%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
    
            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", colorMap[shadingMeasure])
            .attr("stop-opacity", 1);
            gradient.append("svg:stop")
            .attr("offset", "100%")
            .attr("stop-color", "rgb(230,230,230)")
            .attr("stop-opacity", 1);
            
             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style","margin-top:-3%")
            .attr("style", "overflow:scroll")
            .attr("transform", function(d){
                  return "translate(" + ( divWid*.80)  + "," + (widthvalue3) + ")";
             })
            .attr("height", "40%")
            .attr("width", "9px")
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")");
            
             svg.append("g")
            .append("text")
            .attr("transform", function(d){
                            if(chartData[div]["chartType"]=== "Combo-Aus-State"){
                  return "translate(" + (divWid*.82+4)  + "," + (widthvalue3+10) + ")";
                        }else{
                  return "translate(" + (divWid*.82+2)  + "," + (widthvalue3+10)+ ")";
               }
             })
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-Aus-State"){
            return "8px";
             }else{
                            return "10px";
                        }
                })
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div)));
    
            svg.append("g")
            .append("text")
            .attr("x", function(d){
                            if(chartData[div]["chartType"]=== "Combo-Aus-State"){
                  return (divWid*.82+4); 
                        }else{
                  return (divWid*.82+2);
               }
             })
            .attr("y", "60%")
            .attr("dy", ".35em")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-Aus-State"){
            return "8px";
             }else{
                            return "10px";
                        }
                })
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div)));
        }
    
}


function buildAusCityMap(div, data, columns, measureArray,divWid,divHgt,drillId) {
   divHgt=$("#"+div).height();
    divWid=$("#"+div).width();
    var chartData = JSON.parse($("#chartData").val());
    var appendDiv = "";
    graphProp(div);
    if(div.indexOf("AusCitydiv_")!=-1){
        appendDiv = div
        var splitDiv = div.split("_");
         div = splitDiv[1]
       }else{
        appendDiv = div
     }
    var color = d3.scale.category12();
    var maindivWid=divWid;
    if(chartData[div]["chartType"]=== "Combo-Aus-City"){  
    var chartMapData = data;
 }else{
    var chartMapData = data[div];
 }
   if(chartData[div]["chartType"]=== "Combo-Aus-City"){  
    var fun = "clicked(this.id,'"+div+"','comboDrill','"+drillId+"')";
 }else{
    var fun = "clicked(this.id,'"+div+"')";
 }
 
     if(chartData[div]["chartType"]=== "Combo-Aus-City"){  
    $("#AusCitydiv_"+div).append("<div id ='cityAusCitydiv_"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }else{
    $("#"+div).append("<div id ='city"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }
    var colIds = chartData[div]["viewIds"];
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var measure= measureArray[0];
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
        colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    var conditionalMeasure = "";
    var conditionalMap = {};
    var conditionalShading = false;
    if (parent.$("#isShaded").val() == "true" || colorFlag) {
        if (parent.$("#shadeType").val() == "conditional") {
            conditionalShading = true;
            conditionalMap = JSON.parse(parent.$("#conditionalMap").val());
            conditionalMeasure = $("#conditionalMeasure").val();
        } else if (parent.$("#shadeType").val() == "standard") {
        } else {
            var map = colorMap;
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
    
     
    active = d3.select(null);
    
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var min, max;
    max = maximumValue(chartMapData, measureArray[0]);
    min = minimumValue(chartMapData, measureArray[0]);
    var pointsize = d3.scale.linear()
    .domain([max, min])
    .range([18, 6]);
    parent.$("#sumValue").val(sum);
    
   var h = divHgt;
   
if(chartData[div]["chartType"]=== "Combo-Aus-City"){ 
        var w = divWid,
    h = divHgt;
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.1)
    .origin([135, -30])
    .translate([w*.45, h*.45]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.1);
    var path1 = d3.geo.path()
    .projection(projection1);
}else{
    if(maindivWid<520){
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.5)
    .origin([135, -21])
    .translate([divWid / 4, divHgt / 3.2]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.5);
    var path1 = d3.geo.path()
    .projection(projection1);
    }else{
    var projection = d3.geo.azimuthal()
    .scale(divHgt*1.5)
    .origin([135, -20])
    .translate([divWid / 4, divHgt / 3.2]);
    var projection1 = d3.geo.azimuthal()
    .scale(divHgt*1.5);
    var path1 = d3.geo.path()
    .projection(projection1);
}  
}
    var path = d3.geo.path()
    .projection(projection);
    
    if(chartData[div]["chartType"]=== "Combo-Aus-City"){
    var svg = d3.select("#cityAusCitydiv_"+div)
    .append("svg")
    .attr("viewBox", "0 0 "+(divWid*.92)+" "+(divHgt*.95)+" ")
    .style("float", "left");
    var g = svg.append("g")
    .style("stroke-width", "1.5px"); 
}else{
    if(maindivWid<520){
    var svg = d3.select("#city"+div)
    .append("svg")
    .attr("viewBox", "0 15 "+(divWid*.92)+" "+(divHgt*.95)+" ")
    .style("float", "left");
    
    g = svg.append("g")
    .attr("transform","translate(" +(divWid*.18)+" 0) ")
    .style("stroke-width", ".2px");
    }else{
         var svg = d3.select("#city"+div)
    .append("svg")
    .attr("viewBox", "0 15 "+(divWid*.92)+" "+(divHgt*.95)+" ")
    .style("float", "left");
     
    g = svg.append("g")
    .attr("transform","translate(" +(divWid*.18)+" 0) ")
    .style("stroke-width", ".2px");
    } }
    
    d3.json("JS/australia.json", function(collection) {
        g.selectAll("path")
        .data(collection.features)
        .enter()
        .append("path")
        .attr("d", path)
        .attr("fill", function(d, i) {
            var colorShad = "#f2f2f2";
            return colorShad;
        })
        .attr("stroke", "#000")
        .on("mouseover", function(d, i) {  
            for (var k = 0; k < data.length; k++) {
                show_detailsAUS(d);
            }
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
        
        g = svg.append("g")
           .attr("transform", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-Aus-City"){
                 }else{
                      if(maindivWid<520){
    return "translate(" +(divWid*.18)+" 0) "
                      }else{
                return "translate(" +(divWid*.18)+" 0) "
                 }  }
                 })
        .style("stroke-width", "1.5px");

        g.selectAll("path")
            .data(collection.features)
            .enter()
            .append("text")
            .attr("x", function(d,i) { 
               return path.centroid(d)[0]-10;
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1];
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                       lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-Aus-City"){
            return "12px";
        }else{
            return "8px";
        }
            })
            .style("font-weight","bold")
            .text(function(d,i){
               var currName = (d.name1);
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.properties["STATE_NAME"];
           })
        });

    d3.json("JS/ausCities.json", function(cities) {
        g.selectAll("circle")
        .data(cities)
        .enter()
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
                    return pointsize(chartMapData[l2][measureArray[0]]);
                }
            }
            return 0;
        })
        .attr("class", function(d) {
            var c1 = (d.properties.name);
            for (var l1 = 0; l1 < chartMapData.length; l1++) { 
                if (typeof (d.properties.name) != "undefined" && chartMapData[l1][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) { 
                    return "place";
                }
            }
            return "place1";
        })
        .attr("id", function(d, j) {
            var c1 = (d.properties.name);
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                    return chartMapData[l2][columns[0]] + ":" + colIds[0];
                }
            }
            return c1;
        })
        .attr("onclick", fun)
        .attr("fill", function(d,i) {
            var colorShad="";
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else {
                for (var l2 = 0; l2 < chartMapData.length; l2++) { 
                    if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                        return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l2,color)
                    }
                }
            }
            return colorShad;
        })
        .attr("color_value", function(d,i) {
            var colorShad="";
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else {
                for (var l2 = 0; l2 < chartMapData.length; l2++) {
                    if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                        return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l2,color)
                    }
                }
            }
            return colorShad;
        })
        .style("opacity", ".8")
        .style("stroke-width", "1px")
        .style("stroke", "white")
        .on("mouseover", function(d, i) {
            var content;
            var currName = d.properties.name;
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\">" + currName + "</span><br/>";
            var msrData = "";
            for(var e=0; e< measureArray.length;e++){
            for (var k = 0; k < chartMapData.length; k++) {
                if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
            msrData = addCommas(chartMapData[k][measureArray[e]])
            content += "<span class=\"name\">" + measureArray[e] + ": </span><span class=\"value\">" + msrData + "</span><br/>";
                }
            }
        }
            return tooltip.showTooltip(content, d3.event);
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })

                var yvalue=0;
		var rectyvalue=0;
                var legendLength;
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
 }else{
                    legendLength="3"; 
                }
               var rect2 = parseInt(divHgt*.85); 
               var widthvalue3 =((divHgt/2)-(rect2 /3));
            
      if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
            if(divWid<divHgt){
                yvalue = parseInt(divHgt*0.9);
                rectyvalue = parseInt((divHgt*0.89 ));
      }else{
                yvalue = parseInt(divHgt*1.05);
                rectyvalue = parseInt((divHgt*1.030 ));
 }      
                  
            var widthRectvalue = parseInt((divWid *.1)-(divWid*.018));
               var logicalParameters=chartData[div]["logicalParameters"]; 
            var count=0;         
                 for(var i in logicalParameters){
               if(chartData[div]["chartType"]=== "Combo-Aus-City"){
                 var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                     var widthvalue = parseInt((divWid*.1)-(divWid*.055));
                      var widthRectvalue = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                     widthvalue = parseInt(widthvalue+(divWid*.23));
                     widthRectvalue = parseInt(widthRectvalue + (divWid*.23));
                 }else{
                     value = "Low";
                     value2="2";
                     widthvalue = parseInt(widthvalue+(divWid*.23));
                     widthRectvalue = parseInt(widthRectvalue+(divWid*.23));
                 }
           }else{
              var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                     var widthvalue = parseInt((divWid*.1)-(divWid*.055));
                      var widthRectvalue = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                     widthvalue = parseInt(widthvalue+(divWid*.18));
                     widthRectvalue = parseInt(widthRectvalue + (divWid*.18));
                 }else{
                     value = "Low";
                     value2="2";
                     widthvalue = parseInt(widthvalue+(divWid*.18));
                     widthRectvalue = parseInt(widthRectvalue+(divWid*.18));
                 } 
           }
                    if (count % 5 == 0) {
                    if (divWid < divHgt) {
                        yvalue = parseInt(yvalue - divWid * .06)
                        rectyvalue = parseInt(rectyvalue - divHgt * .06)
                    } else {
                        yvalue = parseInt(yvalue - divHgt * .06)
                        rectyvalue = parseInt(rectyvalue - divHgt * .06)
                    }
                    }
            var conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            
            svg.append("g")
        .append("rect")
                .attr("width", 24)
           .attr("height", function(d){
              if(chartData[div]["chartType"]=== "Combo-Aus-City"){
            return "12px";
             }else{
            return "15px";
            }
             })
           .attr("transform", function(d){
    if(chartData[div]["chartType"]=== "Combo-Aus-City"){
        return "translate(" + (widthRectvalue+20)  + ","+(rectyvalue*.82-10)+")";
    }else{
         return "translate(" + (widthRectvalue+50)  + ","+(rectyvalue*.82)+")";
    }})
                .attr("stroke", "black")
           .style("margin-left","15%")
        .attr("dy", ".35em")
                .style("fill", conditionalMap["logical"+value+"color"]);

            svg.append("g")
            .append("text")
           .style("margin-left","15%")
    .attr("transform", function(d){
                            if(chartData[div]["chartType"]=== "Combo-Aus-City"){
        return "translate(" + (widthRectvalue+50)  + ","+(rectyvalue*.82)+")";
                        }else{
         return "translate(" + (widthvalue+50)  + ","+(yvalue*.82+5)+")";
    }})
            .style("font-size","10px")
            .text(function(){
              if (conditionalMap["condition"+value2+""] === "<>") {
               return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                    } else {
                        return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
    }
                });
           count++;
            }
        }else{  
            
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);
            var rectsize = parseInt(divWid*.85);
            var widval =((divWid/2)-(rectsize /3));
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "30%")
            .attr("y1", "85%")
            .attr("x2", "30%")
            .attr("y2", "0%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");

            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", colorMap[shadingMeasure])
            .attr("stop-opacity", 1);
            gradient.append("svg:stop")
            .attr("offset", "100%")
            .attr("stop-color", "rgb(230,230,230)")
            .attr("stop-opacity", 1);
            
             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style","margin-top:-3%")
            .attr("style", "overflow:scroll")
            .attr("transform", function(d){
                  return "translate(" + ( divWid*.80)  + "," + (widthvalue3) + ")";
             })
            .attr("height", "40%")
            .attr("width", "9px")
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")");
            
             svg.append("g")
            .append("text")
            .attr("transform", function(d){
              if(chartData[div]["chartType"]=== "Combo-Aus-City"){ 
                  return "translate(" + (divWid*.82+4)  + "," + (widthvalue3+10) + ")";
               }else{
                  return "translate(" + (divWid*.82+2)  + "," + (widthvalue3+10)+ ")";
               }
             })
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-Aus-City"){
            return "8px";
             }else{
            return "10px";
            }
            })
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div)));
    
            svg.append("g")
            .append("text")
            .attr("x", function(d){
              if(chartData[div]["chartType"]=== "Combo-Aus-City"){ 
                  return (divWid*.82+4); 
               }else{
                  return (divWid*.82+2);
               }
             })
            .attr("y", "61%")
//            .attr("dy", ".35em")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-Aus-City"){
            return "8px";
             }else{
            return "10px";
             }
            })
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div)));
        }
    });

    clicked = function(id,divId,flag2,drillId) {
        drillWithinchart1(id,divId,flag2,drillId)
    }

    function reset() {
        active.classed("active", false);
        active = d3.select(null);
        g.transition()
        .duration(750)
        .style("stroke-width", "1.5px")
        .attr("transform", "");
    }
                }

//add by mayank sharma
function buildIndiaCityMap(div,data,columns,measureArray,divWid,divHgt,drillId){
    var chartData = JSON.parse($("#chartData").val());
    var appendDiv = "";
    graphProp(div);
    if(div.indexOf("indiaCityDiv_")!=-1){
        appendDiv = div
        var splitDiv = div.split("_");
         div = splitDiv[1]
       }else{
        appendDiv = div
     }
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
    
    if(chartData[div]["chartType"]=== "Combo-IndiaCity"){
    var fun = "clicked(this.id,'"+div+"','comboDrill','"+drillId+"')";
    }else{
   fun = "clicked(this.id,'"+div+"')";
    }
    var mainDive=divWid;
    var color = d3.scale.category12();
//    var chart12 = data[div];
var color = d3.scale.category12();
 if(chartData[div]["chartType"]=== "Combo-IndiaCity"){  
    var chartMapData = data;
 }else{
   chartMapData = data[div];
 }
     graphProp(div);
    columns = chartData[div]["viewBys"];
    var mapColumns = chartData[div]["viewBys"];
    measureArray = chartData[div]["meassures"];
    var colIds = chartData[div]["viewIds"];
    
    if(chartData[div]["chartType"]=== "Combo-IndiaCity"){  
    $("#indiaCityDiv_"+div).append("<div id ='citydivindiaCityDiv_"+div+"' style = 'height:90%;float:left;width:auto;'>");
    }else{
    $("#"+div).append("<div id ='citydiv"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }
    
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var measure= measureArray[0];
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
        colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    var logicalMap=chartData[div]["logicalColorDiv"];
    var logicalParameters = {};
    
  if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){   
    }else{  
            var map = colorMap;
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
    
        var margin = {
        top: 10,
        right: 12,
        bottom: 30,
        left: 70
    };
    divWid = screen.width * .5;
    divHgt = screen.height * .5;
    active = d3.select(null);
var ht= divHgt*1.17;
if(chartData[div]["chartType"]=== "Combo-IndiaCity"){  
   var w = divWid+30;
    var h = 550;
    var proj = d3.geo.mercator();
    var path = d3.geo.path().projection(proj);
    var t = proj.translate(); // the projection's default translation
    var s = proj.scale()
}else{
    var projection = d3.geo.mercator()
            .scale(divWid*5.9)
            .translate([divWid / 1.85, divHgt / 1.5]);
    var projection1 = d3.geo.mercator()
            .scale(divWid*5.9);
    var path1 = d3.geo.path()
    .projection(projection1);
    var path = d3.geo.path()
    .projection(projection);
}
    var fromoneview=parent.$("#fromoneview").val()
    var districtmapchartData = data;

    if(chartData[div]["chartType"]=== "Combo-IndiaCity"){
       var map = d3.select("#citydivindiaCityDiv_" + div)
       .append("svg")
     .attr("id", "svg_indiaCityDiv_" + div)
    .attr("width", (w+20) )
    .attr("height", (divHgt+55))
    .style("margin-top", "-40px")
    .style("margin-left", "-50px")
            
    .call(initialize);
    }else{
     map = d3.select("#citydiv" + div)
    .append("svg:svg")
     .attr("viewBox", "0 0 " + (divWid + margin.left + margin.right) + " " + (ht) + " ")
     .style("float", "left");
    }
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
    
    function drawStates1() {
        var str="features";
        d3.json("JS/states-tel.json", function (json) {
            indiaPlaces.selectAll("path")

            .data(json.features)
            .enter()
            .append("path")
            .attr("d", path)
            .attr("transform", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-IndiaCity"){
            return "";
        }else{
             return "translate(" + (divWid * (-1.5)) + " " + (divHgt * .60) + ")"    
            }
            })
            .attr("fill", function(d, i) {
                var colorShad = "#f2f2f2";
                return colorShad;
            })
            .attr("stroke", "#000")
            .attr("id",function(d,i){
                var currName = (d.id).toUpperCase();
                for (var l = 0; l < chartMapData.length; l++) {
                    if (currName.toLowerCase() == chartMapData[l][mapColumns[0]].toLowerCase()) {
                        return chartMapData[l][mapColumns[0]]+":"+colIds[0];
                    }
                }
                return "";
            })

           var g = map.append("g")
             .attr("transform", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-IndiaCity"){
            return "";
        }else{
             return "translate(" + (divWid * (-1.5)) + " " + (divHgt * .60) + ")"    
            }
            })
        .style("stroke-width", "1.5px");
    
        g.selectAll("path")
            .data(json.features)
            .enter()
            .append("text")
            .attr("x", function(d,i) { 
               return path.centroid(d)[0]-5;
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1];
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                       lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size","10px")
            .style("font-weight","bold")
            .text(function(d,i){
               var currName = (d.name1);
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.id;
           })
        });
    }
     function initialize() {
        proj.scale(3850);
        proj.translate([-660, 470]);
    }
    
    function donothing(){}
    function drawIndiaPlaces1(data) {
        subData=data;
        var min1=[];
        for(var key in chartMapData) {
            min1.push(chartMapData[key][measureArray[0]]);
        }
        var minRange=4;
        if(chartMapData.length==1){
            minRange=12;
        }
        var pointsize = d3.scale.linear()
        .domain([Math.min.apply(Math, min1),Math.max.apply(Math, min1)])
        .range([minRange,12]);

        var str="features";
        india.selectAll("path.place")
        .data(data)
        .enter()
        .append("path")
         .attr("transform", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-IndiaCity"){
            return "";
        }else{
             return "translate(" + (divWid * (-1.5)) + " " + (divHgt * .60) + ")"    
            }
            })
        .attr("d", path.pointRadius(function(d,i) {
            var c1=(d.properties.name);
            for(var l=0;l<chartMapData.length;l++){
                if(typeof c1!="undefined" && chartMapData[l][mapColumns[0]].toLowerCase()==c1.toLowerCase()){
                    return (pointsize(chartMapData[l][measureArray[0]]));
                }
            }
            return 0;
        }))
        .attr("id", function(d,j) {
            var c1=(d.properties.name);
            for(var l=0;l<chartMapData.length;l++){
                if(typeof c1!="undefined" &&  chartMapData[l][mapColumns[0]].toLowerCase()==c1.toLowerCase()){
                    return chartMapData[l][mapColumns[0]]+ ":" + colIds[0];
                }
            }
            return c1;
        })
        .attr("fill", function(d,i) {
            var currName = (d["properties"]["name"]);
            var colorShad="";
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else {
                for (var l2 = 0; l2 < chartMapData.length; l2++) { 
                    if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === currName.toLowerCase()) {
                      return getcolorValueFunction(div,chartData,drillShade,chartMapData,columns,measureArray,l2,color)
                    }
                }
            }
            return colorShad;
        })
        .attr("color_value", function(d,i) {
            var currName = (d["properties"]["name"]);
            var colorShad="";
            var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && currName ==drilledvalue) {
                colorShad = drillShade;
            } else {
                for (var l2 = 0; l2 < chartMapData.length; l2++) {
                    if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === currName.toLowerCase()) {
                        return getcolorValueFunction(div,chartData,drillShade,chartMapData,columns,measureArray,l2,color)
                    }
                }
            }
            return colorShad;
        })
        .style("opacity", ".8")
        .attr("onclick", fun)
//        .dblTap(function(e,id) {
//            drillFunct(id);
//        }) 
        .on("mouseover", function(d, i) {
            d3.select(this).attr("stroke", "grey");
        var content;
        var count = 0;
         var title;
            title = columns;
        for (var j = 0; j < chartMapData.length; j++) {
            content = "<span class=\"name\">" + columns[0] + ": </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
            for (var m in measureArray) {
                if (chartMapData[j][columns[0]].toLowerCase() === (d.properties.name).toLowerCase()) {
                    content += "<span class=\"name\">" + measureArray[m] + ": </span><span class=\"value\"> " + addCommas(chartMapData[j][measureArray[m]]) + "</span><br/>";
                    count++;
                }else if (isShortForm(d, chartMapData[j][columns[0]])) {
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
        })
        .on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
        
        
       if(chartData[div]["chartType"]=== "Combo-IndiaCity"){  
         html = "<div id='legScale1indiaCityDiv_" + div + "' class='legend2' style='float:right;margin-right:25%;margin-top:-40%;'></div>";
      $('#indiaCityDiv_'+div).append(html);
        var svg1 = d3.select("#legScale1indiaCityDiv_"+div)
        .append("svg")
        .attr("width", "auto")
        .attr("height", "100%");
        }else{
     if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
        var html = "<div id='legScale1" + div + "' class='legend2' style='float:right;width:200px;margin-right: 15%;margin-top:-35%;'></div>";
    } else {
        html = "<div id='legScale1" + div + "' class='legend2' style='float:right;margin-right: 15%;margin-top:-35%;'></div>";
}   
        $('#'+div).append(html);
        var svg1 = d3.select("#legScale1"+div)
        .append("svg")
        .attr("width", "auto")
        .attr("height", "100%");
       }
    
       
    
if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){  
     var logicalParameters=chartData[div]["logicalParameters"]; 
        for(var i in logicalParameters){
            var value='', value2=''; 
            if(i==0){
                value = "High";
                value2='';
                var widthvalue = "10";
                var widthRectvalue = "20";
            }else if(i==1){
                value = "Mid";
                value2="1";
                widthvalue = "30";
                widthRectvalue = "40";
            }else{
                value = "Low";
                value2="2";
                widthvalue = "50";
                widthRectvalue = "60";
            }
            var conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            svg1.append("g")
            .append("rect")
            .attr("width", 24)
            .attr("height", 15)
            .attr("transform", "translate(9," + (widthvalue) + ")")
            .attr("stroke", "black")
            .attr("dy", ".35em")
            .style("fill", conditionalMap["logical"+value+"color"]);
    
            svg1.append("g")
            .append("text")
            .attr("dy", ".35em")
            .attr("transform", "translate(35," + (widthRectvalue) + ")") 
            .text(function(){
                if (conditionalMap["condition"+value2+""] === "<>") {
                    return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
                } else {
                    return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
}
            });
            $("#legScale1"+div).css("margin-right", "-2%");
                 }
        }else{  
//            var colorMap = JSON.parse(parent.$("#measureColor").val());
            var height = $("#legScale1"+div).height() - 10;
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);

            var gradient = svg1.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "0%")
            .attr("y1", "30%")
            .attr("x2", "70%")
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

            svg1.append("g")
            .append("rect")
            .attr("width", 8)
            .attr("height", "80%")
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")")
            .attr("x", 0)
            .attr("y", 15);
            svg1.append("g")
            .append("text")
            .attr("x", 10)
            .attr("y", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-IndiaCity"){  
            return "85%";
        }else{
        return (height-10);
        }
            })
            .attr("dy", ".35em")
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div))); 
            svg1.append("g")
            .append("text")
            .attr("x", 10)
            .attr("y", 20)
            .attr("dy", ".35em")
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div))); 
            $("#legScale1"+div).css("width", "10%");
        }
    }
    
clicked = function(id,div,flag2,drillId) {
        drillWithinchart1(id,div,flag2,drillId)
    }
    
}


function buildKPIDashboard(chartId, data, compData, divwidth, divHght) {
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    var measureName = chartDetails["meassures"];
// aa   var measureIds = chartDetails["meassureIds"];
    var compareList = chartData[chartId]["customTimeComparisons"];
//    var comparedMesure = chartDetails["comparedMeasure"];
    var defaultMeasures = chartDetails["defaultMeasures"];
     var trWidth = divwidth / 8;
     graphProp(chartId);
     var yAxisFormat = "";
   var yAxisRounding = 0;
    if (typeof chartData[chartId]["yAxisFormat"] != "undefined" && chartData[chartId]["yAxisFormat"] != "") {
      yAxisFormat = chartData[chartId]["yAxisFormat"];
    } else {
   yAxisFormat = "Auto";
  }
    if (typeof chartData[chartId]["rounding"] != "undefined" && chartData[chartId]["rounding"] != "") {
      yAxisRounding = chartData[chartId]["rounding"];
    } else {
        yAxisRounding = 1;
  }
 
    var divHght1 = divHght - 17;
    var htmlstr = "";
    htmlstr = htmlstr + "<ul id='comparableMeasures" + chartId + "' class='dropdown-menu'></ul><div class='hiddenscrollbar' id=\"" + chartId + "tablediv\"  style=\"height:" + divHght1 + "px; width:100%;overflow: hidden;\">";
    htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:" + divHght1 + "px;width:100%;float:left;margin-top:auto;padding-right: 15px;overflow-x:hidden;'>";
     
      htmlstr = htmlstr + "<table id=\"chartTableKPI" + chartId + "\" class='table table-stripped table-condensed display' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
      htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
htmlstr += "<th width=5></th>"
    htmlstr += "<th width=\"" + trWidth + "\" style='font-weight:bold;text-align:left'>KPI</th>";
    for (var i in compareList) {
     
        htmlstr += "<th><span class='dropdown' style='cursor:pointer;float:left;font-weight:bold;text-align:left' ><tspan  id='" + compareList[i]+":"+ "' data-toggle='dropdown' class='dropdown-toggle'  onclick='openComprableMeasures(\"" + chartId + "\",this.id,event)'  src='' alt='Options'  >";
        try {
            htmlstr += compData[i].replace(measureName[0], "").replace("(", "").replace(")", "") + "</tspan></span></th>";
 }
        catch (e) {
 }
    }
htmlstr += "</tr>";
htmlstr += "</thead>";
var count = 0;
    var comparisonMeasureMap = chartData[chartId]["comparisonMeasureMap"];//
//    try {
//        alert();
//        alert(JSON.stringify(comparisonMeasureMap));
//    }
//    catch (e) {
//    }
var arrowList=[];
    for (var k = 0; k < defaultMeasures.length; k++) {
data.forEach(function(d, i) {
htmlstr += "<tr>";
htmlstr += "<td width=5></td>";
            htmlstr += "<td width=\"" + trWidth + "\" style='font-weight:bold;text-align:left'>";
            htmlstr += defaultMeasures[k] + "</td>";
            for (var l = 0; l < compareList.length; l++) {
                var arrow = '', key = '';
                if (typeof comparisonMeasureMap !== 'undefined' && typeof comparisonMeasureMap[compareList[l]] !== 'undefined') {
                    var t = comparisonMeasureMap[compareList[l]];
                    var compIndex = compareList.indexOf(t);
                    key = compData[compareList.length * k + compIndex];
                }
                else {
                    if (compareList[l] === 'MTD') {
                        var compIndex = compareList.indexOf('PYMTD');
                        if (compIndex !== -1) {
                            key = compData[compareList.length * k + compIndex];
                        }
                        else {
                            compIndex = compareList.indexOf('PMTD');
                            if (compIndex !== -1) {
                                key = compData[compareList.length * k + compIndex];
                            }
                        }
                    }
                    else if (compareList[l] === 'QTD') {
                        var compIndex = compareList.indexOf('PYQTD');
                        if (compIndex !== -1) {
                            key = compData[compareList.length * k + compIndex];
                        }
                        else {
                            compIndex = compareList.indexOf('PQTD');
                            if (compIndex !== -1) {
                                key = compData[compareList.length * k + compIndex];
                            }
                        }
                    }
                    else if (compareList[l] === 'YTD') {
                        var compIndex = compareList.indexOf('PYTD');
                        if (compIndex !== -1) {
                            key = compData[compareList.length * k + compIndex];
                        }
                    }
                }
                if (key !== '') {
                    var current = parseFloat(data[i][compData[count]]);
                    var prior = parseFloat(data[i][key]);
                    var changePercent = (current - prior) / prior * 100;
                    if (changePercent < 0) {
                        arrow = ' <div id="arrowSymbol'+(l)+(k)+'" style="color:red">&#8595;</div>';
                    }
                    else {
                        arrow = ' <div id="arrowSymbol'+(l)+(k)+'" style="color:green">&#8593;</div>';
                    }
                    if(arrowList.indexOf(compareList[l])===-1){
                        arrowList.push(compareList[l]);
                    }
                }
                var gtValue = addCommas(numberFormat(data[i][compData[count]], yAxisFormat, yAxisRounding, chartId));
                htmlstr += "<td width=\"" + trWidth + "\">";
                if(arrow!==''){
                    htmlstr += "<div style='float:left' class='arrow"+(l)+"'>"+ gtValue +"</div>"+ arrow + "</td>";
                }
                else{
                    htmlstr += "<div style='float:left'>"+ gtValue +"</div>"+ arrow + "</td>";
                }
 count++;   
}
htmlstr += "</tr>";
});
}
      htmlstr += "</table>";
      htmlstr += "</div></div>";
      
     $("#" + chartId).html(htmlstr); 
     adjustArrows(chartId,arrowList,compareList);
    var table = $('#chartTableKPI' + chartId).dataTable({
//          "filter":true,
        "iDisplayLength": 12,
          "bPaginate": false,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
        "scrollY": "" + divHght * .80 + "px"
//          "order": [[ 1, "desc" ]]  
          });
}


function buildComboUSMap(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
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
    htmlvar += "<div id='kpiPieDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='USdiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
             if(typeof uscity["USdiv_"+div]==="undefined" || window.drillID!=="USdiv_"+div)
             uscity["USdiv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="USdiv_"+div){
                 data3 = JSON.parse(uscity["USdiv_"+div]);
             }
            parent.$("#driver").val('');
            buildUStateMap("USdiv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"USdiv_"+div)
var appendDiv = "";
if(div.indexOf("USdiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
         if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
        if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
    }
})
}

function buildUSCityMap(div, data, columns, measureArray,chWid,chHgt,drillId) {
    chWid = $("#" + div).width();
    chHgt = $("#" + div).height();
   var chartData = JSON.parse($("#chartData").val());
    var appendDiv = "";
    graphProp(div);
    if(div.indexOf("USCitydiv_")!=-1){
        appendDiv = div
        var splitDiv = div.split("_");
         div = splitDiv[1]
       }else{
        appendDiv = div
     }
    var color = d3.scale.category12();
 if(chartData[div]["chartType"]=== "Combo-USCity-Map"){  
    var chartMapData = data;
 }else{
    var chartMapData = data[div];
 }
 var h=chHgt;
 if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
    var fun = "clicked(this.id,'"+div+"','comboDrill','"+drillId+"')";
 }else{
    var fun = "clicked(this.id,'"+div+"')";
 }

    if(chartData[div]["chartType"]=== "Combo-USCity-Map"){  
    $("#USCitydiv_"+div).append("<div id ='mapdivUSCitydiv_"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }else{
    $("#"+div).append("<div id ='mapdiv"+div+"' style = 'height:auto;float:right;width:100%;'>");
    }
    
var colIds = chartData[div]["viewIds"];
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

    
    active = d3.select(null);
    
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"];// add for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
     }
    if(typeof colorMap=="undefined" || keysMap.length==0){ 
        colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
     }// add for color scale
    var shadingMeasure = "";
    if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
        } else {
            var map =colorMap;
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
    var conditionalMap = {};
    
    if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
    chWid = screen.width*.5;
    chHgt = screen.height*.65;
    var projection = d3.geo.albersUsa()
    .scale(700)
    .translate([chWid / 4, chHgt / 2.5]);
    var projection1 = d3.geo.albersUsa()
    .scale(800);
    var path1 = d3.geo.path()
    .projection(projection1);
    }else{
    var projection = d3.geo.albersUsa()
        .scale(chHgt * 1.52)
        .translate([chWid / 4, chHgt / 2.9]);
    var projection1 = d3.geo.albersUsa()
        .scale(chHgt * 1.52);
    var path1 = d3.geo.path()
    .projection(projection1);

    }
    var path = d3.geo.path()
    .projection(projection);

if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
    var svg = d3.select("#mapdivUSCitydiv_"+div)
    .append("svg")
     .attr("viewBox", "0 10 " + (chWid * .92) + " " + (chHgt * .95) + " ")
    .style("float", "left");
}else{
    var svg = d3.select("#mapdiv"+div)
    .append("svg")
    .attr("viewBox", "0 -20 " + (chWid * .92) + " " + (chHgt * .95) + " ")
    .style("float", "left");
} 

    var g = svg.append("g")
    .attr("transform", "translate(" + (chWid * .21) + "," + (chHgt * .05) + ")")
    .style("stroke-width", "1.5px");
             
    d3.json("JS/us-States.json", function(error, us) {
        g.selectAll("path")
        .data(us.features)
        .enter().append("path")
        .attr("d", path)
        .attr("fill", function(d, i) {
            var colorShad = "#f2f2f2";
            return colorShad;
        })
        .attr("stroke", "#000")
//        .on("mouseover", function(d, i) {  
//            for (var k = 0; k < data.length; k++) {
//                show_detailsUS(d);
//            }
//        })
//        .on("mouseout", function(d, i) {
//            hide_details(d, i, this);
//        })
        g = svg.append("g")
        .attr("transform", "translate(" + (chWid * .21) + "," + (chHgt * .05) + ")")
        .style("stroke-width", "1.5px");
        g.selectAll("path")
            .data(us.features)
            .enter()
            .append("text")
            .attr("x", function(d,i) { 
               return path.centroid(d)[0]-10;
            })
            .attr("y", function(d,i) {
                return path.centroid(d)[1]+2;
            })
            .style("fill",function(d,i){
                var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else{
                       lableColor = "#000000";
                               }
                               return lableColor;
            })
            .style("font-size", function(d,i){
        if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
            return "12px"
        }else{
            return "8px"
        }
            })
//            .style("font-weight","bold")
            .text(function(d,i){
               var currName = (d.name1);
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.properties.name;
           })
        });
    d3.json("JS/usCities.json", function(cities) {
        svg.selectAll('.cities')
        .data(cities)
        .enter()
        .append('path')
        .attr("transform", "translate(" + (chWid * .21) + "," + (chHgt * .05) + ")")
        .attr('d', path.pointRadius(function(d, i) {
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                if (typeof (d.properties.name) != "undefined" && chartMapData[l2][columns[0]].toLowerCase() == d.properties.name.toLowerCase()) {
                    return pointsize(chartMapData[l2][measureArray[0]]);
                }
            }
            return 0;
        }))
        .attr("class", function(d) {
            var c1 = (d.properties.name);
            for (var l1 = 0; l1 < chartMapData.length; l1++) {
                if (typeof (d.properties.name) != "undefined" && chartMapData[l1][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                    return "place";
                }
            }
            return "place1";
        })
        .attr("id", function(d, j) {
            var c1 = (d.properties.name);
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                    return chartMapData[l2][columns[0]] + ":" + colIds[0];
                }
            }
            return c1;
        })
        .attr("onclick", fun)
        .attr("fill", function(d,i) {
             var colorShad="";
             var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else {
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                    return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l2,color)
                }
            }
            }
            return colorShad;
        })
        .attr("color_value", function(d,i) {
             var colorShad="";
             var drilledvalue;
            try {
                drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
            } catch (e) {
            }
            if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && d.properties.name ==drilledvalue) {
                colorShad = drillShade;
            } else {
            for (var l2 = 0; l2 < chartMapData.length; l2++) {
                if (typeof d.properties.name != "undefined" && chartMapData[l2][columns[0]].toLowerCase() === d.properties.name.toLowerCase()) {
                    return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,l2,color)
                }
            }
            }
            return colorShad;
        })
        .style("opacity", ".8")
        .style("stroke-width", "1px")
        .style("stroke", "white")
        .on("mouseover", function(d, i) {
            var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                    show_details(d, columns, measureArray, this,div);
                    
            for (var k = 0; k < chartMapData.length; k++) {
                if (d.properties.name.toLowerCase() === chartMapData[k][columns[0]].toLowerCase()) {
                    show_details(d, columns, measureArray, this, i)
                }
            }
                   
        })
        .on("mouseout", function(d, i) {
            var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
                    
            hide_details(d, i, this);
        })

var yvalue=0;
		var rectyvalue=0;
                var legendLength;
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }else{
                    legendLength="3"; 
                }
            var rect2 = parseInt(chHgt*.85); 
            var widthvalue =((chHgt/2)-(rect2 /2.7));
            
if((typeof chartData[div]["Pattern"]!="undefined" && chartData[div]["Pattern"]!="") &&(chartData[div]["Pattern"]=="Logical")){
            if(chWid<chHgt){
                yvalue = parseInt(chHgt*0.9);
                rectyvalue = parseInt((chHgt*0.89 ));
            }else{
                yvalue = parseInt(chHgt*1.05);
                rectyvalue = parseInt((chHgt*1.030 ));
            }    
            
            var widthRectvalue2 = parseInt((chWid *.1)-(chWid*.018));
            var logicalParameters=chartData[div]["logicalParameters"]; 
            var count=0;         
           for(var i in logicalParameters){
                 var value='', value2=''; 
                 if(i==0){
                     value = "High";
                     value2='';
                 widthRectvalue2 = 0;
                 }else if(i==1){
                     value = "Mid";
                     value2="1";
                 widthRectvalue2 = parseInt(widthRectvalue2 + (chWid * .18))
                 }else{
                     value = "Low";
                     value2="2";
                 widthRectvalue2 = parseInt(widthRectvalue2 + (chWid * .18))
                 }
                
                    if (count % 5 == 0) {
                    widthRectvalue2 = parseInt((chWid * .25) - (chWid * .018));
                    if (chWid < chHgt) {
                        yvalue = parseInt(yvalue - chWid * .06)
                        rectyvalue = parseInt(rectyvalue - chWid * .06)
                    } else {
                        yvalue = parseInt(yvalue - chHgt * .06)
                        rectyvalue = parseInt(rectyvalue - chHgt * .06)
                    }
                    }
            conditionalMap = logicalParameters[i];
            var keys = Object.keys(conditionalMap);
            
            svg.append("g")
           .append("rect")
           .attr("width", 24)
           .attr("height", 15)
           .attr("transform", "translate(" + widthRectvalue2  + ","+(rectyvalue*.82)+")")
           .attr("stroke", "black")
           .attr("dy", ".35em")
           .style("fill", conditionalMap["logical"+value+"color"]);
                                    
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + (widthRectvalue2+30)  + ","+(yvalue*.82+5)+")")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
            return "12px";
             }else{
            return "10px";
            }
            })
            .text(function(){
              if (conditionalMap["condition"+value2+""] === "<>") {
               return addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div)) +conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Min"],yAxisFormat,yAxisRounding,div));
              } else {
               return conditionalMap["condition"+value2+""] + addCommas(numberFormat(conditionalMap[""+value+"Max"],yAxisFormat,yAxisRounding,div));
              }
            });
           count++;
            }
        }else{
            
            var shadingMeasure = colorMap["measure"];
            var max = maximumValue(chartMapData, shadingMeasure);
            var min = minimumValue(chartMapData, shadingMeasure);
            var rectsize = parseInt(chWid*.85);
            var widval =((chWid/2)-(rectsize /3));
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
                  if(chWid<chHgt){
                yvalue = parseInt(chHgt*0.9);
                rectyvalue = parseInt((chHgt*0.89 ));
            }else{
                yvalue = parseInt(chHgt*1.05);
                rectyvalue = parseInt((chHgt*1.030 ));
            }
            var widthval = parseInt((chWid * .25));//.1
             var widthRectvalue = parseInt((chWid * .25) - (chWid * .018));
            if (i != 0) {    
                widthval = parseInt(widthval + (chWid * .18))
                widthRectvalue = parseInt(widthRectvalue + (chWid * .18))
                
                    if (count % 5 == 0) {
                    widthval = parseInt((chWid * .25));
                    widthRectvalue = parseInt((chWid * .25) - (chWid * .018));

                    if (chWid < chHgt) {
                        yvalue = parseInt(yvalue - chWid * .06)
                        rectyvalue = parseInt(rectyvalue - chWid * .06)
                    } else {
                        yvalue = parseInt(yvalue - chHgt * .06)
                        rectyvalue = parseInt(rectyvalue - chHgt * .06)
                    }

                    }
              }
                  
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "75%")
            .attr("y1", "30%")
            .attr("x2", "0%")
            .attr("y2", "30%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
              }else{
               var gradient = svg.append("svg:defs")
            .append("svg:linearGradient")
            .attr("id", "gradientWrdMapLegend1"+div+"")
            .attr("x1", "30%")
            .attr("y1", "85%")
            .attr("x2", "30%")
            .attr("y2", "0%")
            .attr("spreadMethod", "pad")
            .attr("gradientTransform", "rotate(0)");
              }

            gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", colorMap[shadingMeasure])
            .attr("stop-opacity", 1);
            gradient.append("svg:stop")
            .attr("offset", "100%")
            .attr("stop-color", "rgb(230,230,230)")
            .attr("stop-opacity", 1);
    
             svg.append("g")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("style","margin-top:-3%")
            .attr("style", "overflow:scroll")
            .attr("transform", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
                  return "translate(" + (widval)  + "," + (rectyvalue*.80) + ")";
               }else{
                  return "translate(" + ( chWid*.85)  + "," + (widthvalue) + ")";
               }
             })
            .attr("height", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
               return "10px";
              }else{
               return "40%";
              }
             })
            .attr("width", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
               var rectwid = parseInt(chWid*.85);
               return rectwid*.40;
              }else{
               return "10px";
              }
            })
            .attr("fill", "url(#gradientWrdMapLegend1"+div+")");
    
             svg.append("g")
            .append("text")
            .attr("transform", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
                  return "translate(" + (widval)  + "," + (rectyvalue*.80+20) + ")";
               }else{
                  return "translate(" + (chWid*.87)  + "," + (widthvalue+10)+ ")";
               }
             })
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
            return "12px";
             }else{
            return "10px";
            }
            })
            .text(addCommas(numberFormat(min,yAxisFormat,yAxisRounding,div)));
    
            svg.append("g")
            .append("text")
            .attr("x", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
                  return (widval+rectsize*.37); 
               }else{
                  return chWid*.87;
               }
             })
            .attr("y", function(d){
              if(chartData[div]["chartType"]=== "Combo-USCity-Map"){ 
                  return (rectyvalue*.80+20);
               }else{
                  return "60%";
               }
             })
            .attr("dy", ".35em")
            .style("font-size", function(d,i){
             if(chartData[div]["chartType"]=== "Combo-USCity-Map"){
            return "12px";
             }else{
            return "10px";
             }
            })
            .text(addCommas(numberFormat(max,yAxisFormat,yAxisRounding,div)));
        }

    });

    clicked = function(id,divId,flag2,drillId) {
        drillWithinchart1(id,divId,flag2,drillId)
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
//    function show_detailsUS(d) {
//        var content;
//        var count = 0;
//        content = "<span class=\"name\">State : </span><span class=\"value\"> " + d.properties.name + "</span><br/>";
//        return tooltip.showTooltip(content, d3.event);
//    }
    }

function buildComboUSCity(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewOvName =JSON.parse(parent.$("#viewby").val());
//    alert(window.drillID)
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
    htmlvar += "<div id='kpiUSDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='USCitydiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
//    if(window.drillID==="USCitydiv_"+div){
////        parent.$("#driver").val(div);
//    }
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
         
            if(typeof uscity["USCitydiv_"+div]==="undefined" || window.drillID!=="USCitydiv_"+div)
             uscity["USCitydiv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="USCitydiv_"+div){
                 data3 = JSON.parse(uscity["USCitydiv_"+div]);
             }
            parent.$("#driver").val('');
            buildUSCityMap("USCitydiv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"USCitydiv_"+div)
var appendDiv = "";
if(div.indexOf("USCitydiv_")!=-1){
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
       
         if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
          if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
}
})
}

function buildComboAusState(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
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
    htmlvar += "<div id='AusDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='AusStatediv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
             if(typeof uscity["AusStatediv_"+div]==="undefined" || window.drillID!=="AusStatediv_"+div)
             uscity["AusStatediv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="AusStatediv_"+div){
                 data3 = JSON.parse(uscity["AusStatediv_"+div]);
             }
            parent.$("#driver").val('');
            buildAustraliaMap("AusStatediv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"AusStatediv_"+div)
var appendDiv = "";
if(div.indexOf("AusStatediv_")!=-1){
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
        if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
         if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
    }
})
}

function buildComboAusCity(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
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
    htmlvar += "<div id='AuscityDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='AusCitydiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
            if(typeof uscity["AusCitydiv_"+div]==="undefined" || window.drillID!=="AusCitydiv_"+div)
             uscity["AusCitydiv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="AusCitydiv_"+div){
                 data3 = JSON.parse(uscity["AusCitydiv_"+div]);
             }
            parent.$("#driver").val('');
            buildAusCityMap("AusCitydiv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"AusCitydiv_"+div)
var appendDiv = "";
if(div.indexOf("AusCitydiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
        if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
        if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
    }
})
}

function buildAndhraPradesh(div,data,columns,measureArray,divWid,divHgt){
var chartData = JSON.parse($("#chartData").val());
    var color = d3.scale.category12();
    var districtData = data[div];
     var chart12 = data[div];
    var fun = "clicked(this.id,'"+div+"')";
    var colIds = chartData[div]["viewIds"];
    graphProp(div)
    var w = divWid;
    var h = 550;
    var proj = d3.geo.mercator();
    var path = d3.geo.path()
    .projection(proj);
    var t = proj.translate();
    var s = proj.scale()
    
var html1 = "<div id = 'andhra"+div+"' style='height:"+divHgt+"px;float:left;width:"+divWid+"px;'></div>";
$("#"+div).append(html1);
var map = d3.select("#andhra"+div);
var chart123=[];
    for(i=0;i<10;i++){
        chart123.push(chart12[i]);
    }
 var centered;
        var x, y, k;
        if (data && centered !== data) {  
            var centroid = path.centroid(data);
             x = divWid / 2;
            y = divHgt / 2;
            k = 2.3;
            y = y + parseInt(y * 40 )+ 150;
            centered = data;
        } else {
            x = divWid / 2;
            y = divHgt / 2;
            k = 1;
            centered = data;
        }
        tooltip.hideTooltip();
        $("#andhra" + div).html("");
        
//   var html1 = "<span style='float:right;margin-right:"+divHgt*.20+"px;;color:blue' id='stName'>Andhra pradesh</span>"
//        $("#andhra" + div).append(html1);
        var html = "";
        var svgcanvas1 = map.append("g")
        .attr("id", "places1");
        var  data1 = [];
        var cordMap = {};
        var count = -1;
        var currNameDistrict ;
        var htmlframe = "<iframe id='dist1"+div+"' frameborder='0' style='' width='"+divWid+"px' height='"+divHgt+"px'></iframe>";
        $('#'+div).attr("style", "display:block");
        $("#andhra"+div).append(htmlframe);
        $("#dist1"+div+"").attr("src", "svg/andhra pradesh.svg").load(function() {
           var $map = $(this).contents();
            var titles = $map.find('path').map(function() {
//                var parenttitle = $(this).parent().attr('title') || '';
               var stateMap = {};
                stateMap["name"] = $(this).attr('name');
                data1.push(stateMap);
                return '0\t' +  stateMap["name"] + '\t' + $(this).attr('title');
            }).get();
            $(this).attr("stroke", "steelblue")
            .attr("stroke-width", ".1")
            $map.find('g').each(function() {
                
                var svgG=d3.select(this);
                d3.select(this).selectAll("path")
                
                .each(function(d, i){
                    var counter = i;
                    
                    var bbox = this.getBBox();
                    svgG.append("text")
                    .style("text-anchor","middle")
                    .attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")")
                    .text(function(d,j){
                        return data1[counter]["name"];
                    })
                    .style("font-size","5px");
                    var district = $(this).attr('name');
                   $(this).attr('fill', function(){
                        count++;
                        var colorShad = "#f2f2f2"; 
                        var drilledvalue;
                        try {
                            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        } catch (e) {
                        }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                            colorShad = drillShade;
                        } else {
                                for(var i in districtData){
                                    if(district.toLowerCase()== districtData[i][columns[0]].toLowerCase()){ 
                                       return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                                    }
                                }
                        }
                        return colorShad;
                    })
                   $(this).attr('stroke',"#bcbdbc")
                    $(this).attr('color_value', function(){
                        count++;
                        var colorShad = "#f2f2f2"; 
                        var drilledvalue;
                        try {
                            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        } catch (e) {
                        }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                            colorShad = drillShade;
                        } else {
                                for(var i in districtData){
                                    if(district.toLowerCase()== districtData[i][columns[0]].toLowerCase()){ 
                                       return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                                }
                            }
                        }
                        return colorShad;
                    })
                    $(this).on("mouseover", function(event){ 
//                        alert(event.pageX)
//                        alert(event.pageY)
                    var content;
                    var stateMap = {};
                    stateMap["title"] = $(this).attr('title');
         content = "<span  class=\"name\">" + columns[0] + "</span><span class=\"value\"> " + stateMap["title"]+ "</span><br/>";
         content += "<span  class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + addCommas(numberFormat(districtData[i][measureArray[0]],yAxisFormat,yAxisRounding,div)) + "</span><br/>";
              return tooltip.showTooltip2(content,event);
        })
         $(this).on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
                });
            $(this).attr('onclick', fun)
            })
        });
          clicked = function(id,divId) {
        drillWithinchart1(id,divId)
    }
}

function buildTelangana(div,data,columns,measureArray,divWid,divHgt){
 var chartData = JSON.parse($("#chartData").val());
    var color = d3.scale.category12();
    var districtData = data[div];
     var chart12 = data[div];
    var fun = "clicked(this.id,'"+div+"')";
    var colIds = chartData[div]["viewIds"];
    graphProp(div)
//    var w = divWid;
//    var h = divHgt;
    
    var proj = d3.geo.mercator();
    var path = d3.geo.path()
    .projection(proj);
    var t = proj.translate();
    var s = proj.scale()
     
   
var html1 = "<div id = 'telangana"+div+"' style='height:"+divHgt+"px;float:left;width:"+divWid+"px;'></div>";
$("#"+div).append(html1);
var map = d3.select("#telangana"+div)
var chart123=[];
    for(i=0;i<10;i++){
        chart123.push(chart12[i]);
    }
 var centered;
        var x, y, k;
        if (data && centered !== data) {  
            var centroid = path.centroid(data);
             x = divWid / 2.5;
            y = divHgt / 2.5;
            k = 2.3;
            y = y + parseInt(y * (40 / 50)) + 150;
            centered = data;
        } else {
            x = divWid / 2.5;
            y = divHgt / 2.5;
            k = 1;
            centered = null;
        }
        tooltip.hideTooltip();
        $("#telangana" + div).html("");
        
//   var html1 = "<span style='float:right;margin-right:"+divHgt*.20+"px;;color:blue' id='stName'>Telangana</span>"
//        $("#telangana" + div).append(html1);
        var html = "";
        var svgcanvas1 = map.append("g")
        .attr("id", "places1");
        var  data1 = [];
        var cordMap = {};
        var count = -1;
        var currNameDistrict ;
        var htmlframe = "<iframe id='dist"+div+"' frameborder='0' style='' width='"+divWid+"px' height='"+divHgt+"px'></iframe>";
        $('#'+div).attr("style", "display:block");
        $("#telangana"+div).append(htmlframe);
        $("#dist"+div+"").attr("src", "svg/telangana.svg").load(function() {
            var $map = $(this).contents();
            var titles = $map.find('path').map(function() {
//                var parenttitle = $(this).parent().attr('title') || '';
               var stateMap = {};
                stateMap["name"] = $(this).attr('name');
                data1.push(stateMap);
                return '0\t' + stateMap["title"] + '\t' + $(this).attr('name');
            }).get();
            $(this).attr("stroke", "steelblue")
            .attr("stroke-width", ".1")
            $map.find('g').each(function() {
                
                var svgG=d3.select(this);
                d3.select(this).selectAll("path")
                
                .each(function(d, i){
                    var counter = i;
                    
                    var bbox = this.getBBox();
                    svgG.append("text")
                    .style("text-anchor","middle")
                    .attr("transform", "translate(" + (bbox.x + bbox.width / 2)  + "," + (bbox.y + bbox.height / 2) + ")")
                    .text(function(d,j){
                        return data1[counter]["name"];
                    })
                    .style("font-size","5px");
                    var district = $(this).attr('name');
                    $(this).attr('fill', function(){
                        count++;
                        var colorShad = "#f2f2f2"; 
                        var drilledvalue;
                        try {
                            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        } catch (e) {
                        }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                            colorShad = drillShade;
                        } else {
                                for(var i in districtData){
                                    if(district.toLowerCase()== districtData[i][columns[0]].toLowerCase()){ 
                                       return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                                    }
                                }
                        }
                        return colorShad;
                    })
                  $(this).attr('stroke',"#bcbdbc")
                    $(this).attr('color_value', function(){
                        count++;
                        var colorShad = "#f2f2f2"; 
                        var drilledvalue;
                        try {
                            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        } catch (e) {
                        }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && district ==drilledvalue) {
                            colorShad = drillShade;
                        } else {
                                for(var i in districtData){
                                    if(district.toLowerCase()== districtData[i][columns[0]].toLowerCase()){ 
                                       return getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                                }
                            }
                        }
                        return colorShad;
                    })
                     .on("mouseover", function(event) { 
           var content;
           var stateMap = {};
                stateMap["title"] = $(this).attr('title');
        content = "<span class=\"name\">" + columns[0] + "</span><span class=\"value\"> " + stateMap["title"]+ "</span><br/>";
         content += "<span class=\"name\">" + measureArray[0] + ":</span><span class=\"value\"> " + addCommas(numberFormat(districtData[i][measureArray[0]],yAxisFormat,yAxisRounding,div)) + "</span><br/>";
              return tooltip.showTooltip2(content,event);
        })
         $(this).on("mouseout", function(d, i) {
            hide_details(d, i, this);
        })
                });
            })
        });
        clicked = function(id,divId) {
        drillWithinchart1(id,divId)
    }

    
}

function openComprableMeasures(chartId, id, event) {
    id=id.split(":")[0];
    var chartData = JSON.parse(parent.$("#chartData").val());
    var compareList = chartData[chartId]["customTimeComparisons"];
    var html = "";
    var comparisonMeasureMap={};
    if(typeof chartData[chartId]["comparisonMeasureMap"]!=='undefined'){
        comparisonMeasureMap=chartData[chartId]["comparisonMeasureMap"];
    }
    if (id === 'MTD') {
        var compIndex = compareList.indexOf('PYMTD');
        if (compIndex !== -1) {
            if(typeof comparisonMeasureMap[id]==='undefined' || comparisonMeasureMap[id]==='PYMTD'){
                html += "<li><a class='' id='PYMTD::'  style=\"cursor: pointer;background-color:#808080\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PYMTD</a></li>";
            }
            else{
                html += "<li><a class='' id='PYMTD::'  style=\"cursor: pointer\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PYMTD</a></li>";
            }
        }
        compIndex = compareList.indexOf('PMTD');
        if (compIndex !== -1) {
            if(comparisonMeasureMap[id]==='PMTD'){
                html += "<li><a class='' id='PMTD::'  style=\"cursor: pointer;background-color:#808080\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PMTD</a></li>";
            }
            else{
                html += "<li><a class='' id='PMTD::'  style=\"cursor: pointer\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PMTD</a></li>";
            }
        }
    }
    else if (id === 'QTD') {
        var compIndex = compareList.indexOf('PYQTD');
        if (compIndex !== -1) {
            if(typeof comparisonMeasureMap[id]==='undefined' || comparisonMeasureMap[id]==='PYQTD'){
                html += "<li><a class='' id='PYQTD::'  style=\"cursor: pointer;background-color:#808080\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PYQTD</a></li>";
            }
            else{
                html += "<li><a class='' id='PYQTD::'  style=\"cursor: pointer\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PYQTD</a></li>";
            }
        }
        compIndex = compareList.indexOf('PQTD');
        if (compIndex !== -1) {
            if(comparisonMeasureMap[id]==='PQTD'){
                html += "<li><a class='' id='PQTD::'  style=\"cursor: pointer;background-color:#808080\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PQTD</a></li>";
            }
            else{
                html += "<li><a class='' id='PQTD::'  style=\"cursor: pointer\" onclick='changeComparableMeasure(this.id,\"" + chartId + "\",\""+id+"\")'>PQTD</a></li>";
            }
        }
    }
    if (html !== '') {
        var posX = $("#" + chartId).offset().left, posY = $("#" + chartId).offset().top;
        parent.$("#comparableMeasures" + chartId).html(html);
        parent.$("#comparableMeasures" + chartId).toggle();
        parent.$("#comparableMeasures" + chartId).css("left", event.pageX - posX);
        parent.$("#comparableMeasures" + chartId).css("top", event.pageY - posY + 20);
    }
}
function changeComparableMeasure(id,chartId,measure){
    id=id.split("::")[0];
    parent.$("#comparableMeasures" + chartId).hide();
    var chartData=JSON.parse(parent.$("#chartData").val());
    var comparisonMeasureMap={};
    if(typeof chartData[chartId]["comparisonMeasureMap"]!=='undefined'){
        comparisonMeasureMap=chartData[chartId]["comparisonMeasureMap"];
    }
    comparisonMeasureMap[measure]=id;
    chartData[chartId]["comparisonMeasureMap"]=comparisonMeasureMap;
    $("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async: false,
        type: "POST",
        data:
                $('#graphForm').serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(),
        success: function(data) {
            generateSingleChart(data, chartId);
        }
    });
}
function adjustArrows(chartId,arrowList,compareList){
    for(var i in arrowList){
        var index=compareList.indexOf(arrowList[i]);
        var className='arrow'+index;
        var elements=$("."+className);
        var maxWidth=0,maxMeasureIndex=0;
        for(var j in elements){
            var wid=0;
            try{
            wid=parseInt($(elements[j]).css("width").replace("px",""));
            }catch(e){}
            if(wid>maxWidth){
                maxMeasureIndex=j;
                maxWidth=wid;
            }
        }
        for(var j in elements){
//            alert("#arrowSymbol"+(i)+(j));
            $("#arrowSymbol"+(index)+(j)).css("margin-left",maxWidth+3+"px");
        }
    }
}

function buildComboIndiaCity(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
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
    htmlvar += "<div id='kpiPieDiv_"+div+"' style='float:left;width:"+divWidth*.41+"px;height:"+divHeight+"px'>";
    htmlvar += "<div id='Top1Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.20+"px;height:"+divHeight+"px'></div>";
    htmlvar += "</div>"
    htmlvar += "<div id='indiaCityDiv_"+div+"' style='border-left:1px solid #d1d1d1;float:right;width:"+divWidth*.57+"px;height:"+divHeight+"px'></div>";

    $("#"+div).html("");
    $("#"+div).html(htmlvar)

    var flag = 'viewByChange';
    
    var columnMeasures = [];
    var columnPieMeasureValues = [];
        for(var i in measureArray){
        if(measureArray[i]!=null && measureArray[i].indexOf(measureArray[0])==-1){
            columnMeasures.push(measureArray[i])
            columnPieMeasureValues.push(KPIresult[i])
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
    chartData[div]["records"]='30';
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async:false,
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
        success: function(data21){
            if(typeof uscity["indiaCityDiv_"+div]==="undefined" || window.drillID!=="indiaCityDiv_"+div)
             uscity["indiaCityDiv_"+div] = data21;
            var data3 = JSON.parse(data21);
             if(window.drillID==="indiaCityDiv_"+div){
                 data3 = JSON.parse(uscity["indiaCityDiv_"+div]);
             }
            parent.$("#driver").val('');
            buildIndiaCityMap("indiaCityDiv_"+div, JSON.parse(data3["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.57, divHeight,"indiaCityDiv_"+div)
var appendDiv = "";
if(div.indexOf("indiaCityDiv_")!=-1){
appendDiv = div;
var splitDiv = div.split("_");
div = splitDiv[1];
}else{
appendDiv = div;
}
        }
    });
//
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
    async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data12){
        if(typeof uscity["Top2Div_"+div]==="undefined" || window.drillID!=="Top2Div_"+div)
        uscity["Top2Div_"+div] = data12;
        var data2 = JSON.parse(data12);
         if(window.drillID==="Top2Div_"+div){
                 data2 = JSON.parse(uscity["Top2Div_"+div]);
   }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top2Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill2",'',"Top2Div_"+div)

                    }
});
//     
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
chartData[div]["records"]='5';
parent.$("#chartData").val(JSON.stringify(chartData));
$.ajax({
     async:false,
    type:"POST",
    data:parent.$("#graphForm").serialize(),
    url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div,
    success: function(data){
         if(typeof uscity["Top1Div_"+div]==="undefined" || window.drillID!=="Top1Div_"+div)
        uscity["Top1Div_"+div] = data;
        var data1 = JSON.parse(data);
         if(window.drillID==="Top1Div_"+div){
                 data1 = JSON.parse(uscity["Top1Div_"+div]);
    }
            parent.$("#driver").val('');
        buildTopAnalysis1("Top1Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.20, divHeight,KPIresult,"drill1",'',"Top1Div_"+div)
    }
})
    
}


function buildGroupedMap(div, data, columns, measureArray, wid, hgt, drillId) {
    hgt = $("#" + div).height();
    wid = $("#" + div).width();
//    alert(data.length)
    var chartData = JSON.parse($("#chartData").val());
  
    var color = d3.scale.category12();
//    var chartMapData = data[div];
    var fun = "clicked(this.id,'"+div+"')";
    
    $("#"+div).append("<div id ='USdiv1"+div+"' style = 'height:auto;float:right;width:100%;'>");

    active = d3.select(null);
    
    var colIds = chartData[div]["viewIds"];
    var colName=chartData[div]["viewBys"]
    var measure1 = measureArray[0];
    graphProp(div);
    
    var mdata = [];
    var keys = [];
    var keys2 = [];
    var keys3 = [];
    var max;
    var groupedTotal = {};
    var indiviTotal = {};
    data.forEach(function(d, j) {
        if (keys.indexOf(d[columns[0]]) === -1 ) {
            keys.push(d[columns[0]]);
        }
        if (keys2.indexOf(d[columns[1]]) === -1 ) {
            keys2.push(d[columns[1]]);
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
//    alert(keys.length)
    
    var innRecordCount;
    if(typeof chartData[div]["innerRecords"]!=='undefined'){
        innRecordCount=chartData[div]["innerRecords"];
    }
    else{
        innRecordCount=5;
    }
    
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
//    alert(JSON.stringify(mdata))
    
      var dataList = []; 
    for(var k=0;k<mdata.length;k++){
    var mapName = {};
         var listName = [];
        var listValue = [];
        var dataKey = Object.keys(mdata[k]);
        mapName[dataKey[0]] = mdata[k][dataKey[0]];
        mapName[dataKey[1]] = mdata[k][dataKey[1]];
        
     dataList.push(mapName);   
    }
    
//    for(var m in dataList){
// alert(JSON.stringify(dataList))
//    }
    data=dataList;
    var colorFlag = true;
    var color1=getDrawColor(div, parseInt(0));
    var isShadedColor=true;
    $("#isShaded").val("true");
    $("#shadeType").val("gradient");
    var colorMap=chartData[div]["gradientLogicalMap"]; // for color scale
    var keysMap;
    if(typeof colorMap!=="undefined"){
    keysMap = Object.keys(colorMap)
    }
    if(typeof colorMap=="undefined" || keysMap.length==0){
       colorMap={};
       colorMap["measure"]=measureArray[0];
       colorMap[measureArray[0]]=color1;
    }// add for color scale
    var shadingMeasure = "";
    if ((typeof chartData[div]["Pattern"] != "undefined" && chartData[div]["Pattern"] != "") && (chartData[div]["Pattern"] == "Logical")) {
        } else {
            var map = colorMap;
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
    var conditionalMap = {};
    

    var projection = d3.geo.albersUsa()
    .scale(hgt * 1.5)
    .translate([wid / 4, hgt / 3.5]);

    var projection1 = d3.geo.albersUsa()
    .scale(hgt * 1.5);

    var path1 = d3.geo.path()
    .projection(projection1);
    
    var path = d3.geo.path()
    .projection(projection);

     var svg = d3.select("#USdiv1" + div)
    .append("svg")
    .attr("viewBox", "0 -20 " + (wid * .92) + " " + (hgt * .95) + " ")
    .style("float", "left");

    var g = svg.append("g")
    .attr("transform", "translate(" + (wid * .21) + "," + (hgt * .05) + ")")
    .style("stroke-width", "1.5px");
    
    d3.json("JS/us-States.json", function(error, us) {
      g.selectAll("path")
        .data(us.features)
        .enter()
        .append("path")
        .attr("d", path)
        .attr("id",function(d,i){
            var currName = (d.properties.name).toUpperCase();
            for (var l = 0; l < data.length; l++) {
                if (currName.toLowerCase() == data[l][columns[0]].toLowerCase()) {
                    return data[l][columns[0]]+":"+colIds[0];
                }
            }
            return "";
        })
        .attr("fill", function(d, i) {
     var currName = (d.properties.name).toUpperCase();
            for (var l = 0; l < data.length; l++) {
                if (currName.toLowerCase() == data[l][columns[0]].toLowerCase()) {
                    return getDrawColor(div,keys2.indexOf(Object.keys(data[l])[1]));
                }
            }
            return "white";
        })
        .attr("color_value", function(d, i) {
     var currName = (d.properties.name).toUpperCase();
            for (var l = 0; l < data.length; l++) {
                if (currName.toLowerCase() == data[l][columns[0]].toLowerCase()) {
                    return getDrawColor(div,keys2.indexOf(Object.keys(data[l])[1]));
                }
            }
            return "white";
        })
        .attr("stroke", "#f2f2f2")
        .attr("onclick", fun)
        .on("mouseover", function(d, i) {
           if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
           for (var k = 0; k < data.length; k++) {
                if (d.properties.name.toLowerCase() === data[k][columns[0]].toLowerCase()) {
                    show_detailsUSMAP(data[k], columns, measureArray, this,div);
                }
            }
           }else{
               return "";
           }
        })
        .on("mouseout", function(d, i) {
            if(typeof chartData[div]["usBar"] === "undefined" || chartData[div]["usBar"] === "Fill") {
            hide_details(d, i, this);
           }else{
               return "";
           }
        });
       
    
       g = svg.append("g")
                .attr("transform", "translate(" + (wid * .21) + "," + (hgt * .05) + ")")
        .style("stroke-width", "1.5px");
        g.selectAll("path")
            .data(us.features)
            .enter()
            .append("text")
                .attr("x", function(d, i) {
            return path.centroid(d)[0] - 10;
            })
                .attr("y", function(d, i) {
            return path.centroid(d)[1] + 2;
            })
            .style("fill",function(d,i){
            var lableColor="";
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
            } else {
                       lableColor = "#000000";
            }
                               return lableColor;
            })
            .style("font-size", function(d,i){
            return "8px";
            })
            .text(function(d,i){
               var currName = (d.name1);
                        return currName;
            })
            .attr("svg:title",function(d,i){
               return d.properties.name;
           })
            });
            
               
       
    var colName=chartData[div]["viewBys"];
//           var displayLength = chartData[div]["displayLegends"]
            var yvalue=0;
            var rectyvalue=0;
            var fontsize1 = parseInt(wid/50);
            var rectsize = parseInt(wid/60);
            var legendLength;

             var count = 0;
             var transform = "";
             if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                }else{
                     legendLength=(data.length<3?data.length:3);
//                     legendLength=(data.length<5?data.length:5);
                }
                
            if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
                
            if(wid<hgt){
                yvalue = parseInt(hgt*0.9);
                rectyvalue = parseInt((hgt*0.89 ));
            }else{
                yvalue = parseInt(hgt*1.05);
                rectyvalue = parseInt((hgt*1.030 ));
            }
            
             var widthvalue = parseInt((wid * .25));//.1
             var widthRectvalue = parseInt((wid * .25) - (wid * .018));
//        for (var i = 0; i < (data.length<5?data.length:5); i++) { 
        for (var i = 0; i < (data.length<3?data.length:3); i++) { 
//        if ( Object.keys(data)[1] > 0) { 
            if (i != 0) {    
                widthvalue = parseInt(widthvalue + (wid * .18))
                widthRectvalue = parseInt(widthRectvalue + (wid * .18))
                
                    if (count % 5 == 0) {
                    widthvalue = parseInt((wid * .25));
                    widthRectvalue = parseInt((wid * .25) - (wid * .018));

                    if (wid < hgt) {
                        yvalue = parseInt(yvalue - wid * .06)
                        rectyvalue = parseInt(rectyvalue - wid * .06)
                    } else {
                        yvalue = parseInt(yvalue - hgt * .06)
                        rectyvalue = parseInt(rectyvalue - hgt * .06)
                    }

                    }
              }
              
            if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){ 
         svg.append("g")
        .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + (widthRectvalue)  + "," + (rectyvalue*.80) + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(d){
                    return getDrawColor(div,keys2.indexOf(Object.keys(data[i])[1]));
            })
               
               
            svg.append("g")
            .append("text")
            .attr("transform", "translate(" +(widthvalue)  + "," + (yvalue*.81) + ")")
            .attr("fill","#000")
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
                return Object.keys(data[i])[1];
            } )
            .text(function(d) {
              return Object.keys(data[i])[1];
            })
           .attr("svg:title",function(d){
               return Object.keys(data[i])[1];
           })
           .on("mouseover", function(d, j) {
            setMouseOverEvent(this.id,div)
                    })
           .on("mouseout", function(d, j) {

            setMouseOutEvent(this.id,div)
                    })
              count++
}
//            }
                     }
           
   

    clicked = function(id,divId,flag2,drillId) {
        drillWithinchart1(id,divId,flag2,drillId)
    }

    function reset() {
        active.classed("active", false);
        active = d3.select(null);

        g.transition()
        .duration(750)
        .style("stroke-width", "1.5px")
        .attr("transform", "");
    }
    function show_detailsUSMAP(d, columns, measureArray, element,div){
      d3.select(element).attr("stroke", "grey");
    var content = "";
        graphProp(div);
    var  msrData = addCommas(numberFormat(d[Object.keys(d)[1]],yAxisFormat,yAxisRounding,div));
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + columns[0] + "</span> : <span style=\"font-family:helvetica;\" class=\"value\"> " + d[columns[0]] + " </span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + columns[1] + "</span> : <span style=\"font-family:helvetica;\" class=\"value\"> " + Object.keys(d)[1] + " </span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + measureArray[0] + "</span> : <span style=\"font-family:helvetica;\" class=\"value\"> " + msrData + " </span><br/>";
    return tooltip.showTooltip(content, d3.event);
}
}



function buildStandardKPI1(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
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
       tileChartsComp1(appendDiv, data, columns, measureArray, divWidth, divHeight,KPIResult);
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
                            showData1 =      addCurrencyType(div, getMeasureId(measureArray[0])) + (numberFormat(showData,yAxisFormat,yAxisRounding,div));
                    }
            else{
                            showData1 =      addCurrencyType(div, getMeasureId(measureArray[0])) + (numberFormat(showData,yAxisFormat,yAxisRounding,div));
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
colorPicker = "#127ba5";
}
if(typeof chartData[div]["lFilledFont"]!="undefined" && chartData[div]["lFilledFont"]!=""){
  measureColor = chartData[div]["lFilledFont"];
}else{
measureColor = "#696969";
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
     var h= divHeight*.86;
  html+="<div id='mainDiv"+div+"' style='width:"+divWidth+";height:"+divHeight+";  background-color:#FFF';cursor:pointer;float:left;>";
  if(div==="chart1"){
    html+="<div  class='tabUL1' onclick='callInitializeChart(\""+div+"\")' id='innerDiv"+div+"' style='border-bottom:5px solid #127ba5;width:"+divWidth+"px;height:"+(h)+"px; background-color:#FFF';cursor:pointer;float:left;>";
  }else{
  html+="<div  class='tabUL1' onclick='callInitializeChart(\""+div+"\")' id='innerDiv"+div+"' style='width:"+divWidth+"px;height:"+(h)+"px; background-color:#FFF';cursor:pointer;float:left;>";
  }
  html+="<table  style='float:left;width:"+divWidth+"px;height:"+divHeight+"px;'>";
  html+="<tr>";

// if(chartData[div]["chartType"]==='Combo-Analysis'){
////alert(showData1)
//if(typeof showData1!="undefined" && showData1!=""){
//if(suffix.trim()==""){ 
//   html+="<td  style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//}else{ 
//   html+="<td   style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//} 
//}else{
//html+="<td   style='color:rgb(212 212 212)' ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>0</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
//
//}
// }else{
//if(suffix.trim()==""){ 
//alert(showData1)
    html+="<td style='width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;text-align: center;cursor:pointer;text-justify: inter-word;'><span  onclick='openTableDiv(\""+div+"\")' style =' font-size:22px;line-height:50px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Helvetica' class='onHoverGreen'>"+showData1+"</span></td>";
//   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' style='float:left;width:"+divWidth+"px;height:"+divHeight/2+"px;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' ><span style =' font-size:18px;line-height:30px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica' class='onHoverGreen'>"+showData1+suffix+"</span></td>";
//   html+="<span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span>";
//   html+="<span id='"+div+"span' style ='line-height:50px;font-weight: bold; font-size:"+(gtFontSize*1.2+10)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+showData1+suffix+"</span>";
//   html+="<span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2+10)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
//}else{ 
//    html+="<td  onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' style='width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;text-align: center;cursor:pointer;text-justify: inter-word;'><span onclick='openTableDiv(\""+div+"\")' style =' font-size:22px;line-height:50px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family:Helvetica' class='onHoverGreen'>"+showData1+"</span></td>";
//   html+="<td onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")' style='float:left;width:"+divWidth+"px;height:"+divHeight/2+"px;border-style: solid;border-color:#D1D1D1;color:rgb(212 212 212);border-top: 0px double;border-width: 0px 0px 1px;' >";
//   html+="<span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span>";
//   html+="<span id='"+div+"span' style ='line-height:50px;font-weight: bold; font-size:"+(gtFontSize*1.2+10)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;color:"+colorPicker+";font-family: Helvetica;' class='onHoverGreen'>"+showData1.toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+suffix+"</span>";
//   html+="<span id='"+div+"span' style ='font-weight: bold; font-size:"+(gtFontSize*1.2+10)+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
//}
//}
//html+="<tr>";
// html+="<td ><img  onclick='trendChartdiv(\""+div+"\")' id='trendChart' class='trendChart1' style='float:right;margin-right: -10%;margin-top: -13%;width:30px;height:30px' src = 'images/icons/tiletrend.png'></td>";
//  html+="</tr>";

  html+="</tr>";
  html+="<tr>";
 if(fromoneview!='null'&& fromoneview=='true'){
div=div1;
     }
  if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  html+="<td onclick='callInitializeChart(\""+div+"\")' style='width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;text-align: center;cursor:pointer;text-justify: inter-word;'><span style =' font-size:18px;line-height:30px; font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica' class='onHoverGreen'>"+measureAliaskpi+"</span></td>";
  }else{
  html+="<td onclick='callInitializeChart(\""+div+"\")' style='width:"+divWidth+"px;height:"+divHeight/2+"px;float:left;cursor:pointer;text-justify: inter-word;'><span style =' font-size:18px;line-height:30px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica' class='onHoverGreen'>"+measureAliaskpi+"</span></td>";
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
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,chartData[div]["meassures"] , 590, 620)
    $("#openTableTile").dialog('open');
}

}



function tileChartsComp1(div, data, columns, measureArray, divWidth, divHeight,KPIResult){
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
                  if(yAxisFormat==""){
                            showData1.push(addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormat(showData[i],yAxisFormat,yAxisRounding,div));
                     }else{
                            showData1.push(addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormat(showData[i],yAxisFormat,yAxisRounding,div));
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
measureColor = "#6F6969";
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
     var h= divHeight*.86;
  html+="<div id='mainDiv"+div+"' style='width:"+divWidth+";height:"+divHeight+"; background-color:#FFF'>";
  html+="<div class='tabUL1' id='innerDiv"+div+"' style='width:"+divWidth+"px;height:"+h+"px; background-color:#FFF'>";
  html+="<table style='float:left;margin-top: '>";
  // 1rd row
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
   html+="<td style='color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/3+"px;float:left;text-justify: inter-word;'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+showData1[0]+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span>"+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"</span></td>";
}else{
   html+="<td style='color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/3+"px;float:left;text-justify: inter-word;'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+showData1[0].toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span></span></td>";
}
}else{
html+="<td style='color:"+colorPicker+";width:"+divWidth+"px;height:"+divHeight/3+"px;float:left;text-justify: inter-word;'><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>0</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;font-family: Helvetica;' class='onHoverGreen'>"+suffix+"<span style='font-family: Helvetica;font-size:34px' class='onHoverGreen'>"+arrow+"</span></span></td>";

}
}else{
if(suffix.trim()==""){
   html+="<td style='width:"+divWidth+"px;height:"+divHeight/4+"px;text-align: center;float:left;text-justify: inter-word;' onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")'  >\n\
<span id='"+div+"span' style ='width:"+divWidth+"px;height:"+divHeight/4+"px;float:left;font-weight: bold; font-size:16px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+showData1[0]+arrow+"("+changePercent+"%)"+suffix+"</span>";
//<span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1[0]+"</span>\n\
//<span style='font-family: Helvetica;font-size:"+comparativeFontSize+"px'>"+arrow+"("+changePercent+"%)</span>"+"</span>\n\
//<span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
}else{
   html+="<td style='width:"+divWidth+"px;height:"+divHeight/4+"px;float:left;text-justify: inter-word;' onclick='openTableDiv(\""+div+"\")' onmouseover='fontSizeIncrease(\""+gtFontSize+"\",\""+div+"\")' onmouseout='fontSizeDecrease(\""+gtFontSize+"\",\""+div+"\")'  ><span id='"+div+"span' style ='font-weight: bold; font-size:"+prefixFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+prefix+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+gtFontSize*1.2+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+showData1[0].toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span id='"+div+"span' style ='font-weight: bold; font-size:"+Suffixfontsize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"<span style='font-family: Helvetica;font-size:"+comparativeFontSize+"px'>"+arrow+"("+changePercent+"%)</span></span></td>";
}}
  html+="</tr>";
  // 2nd row
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
  html+="<td style='width:"+divWidth+"px;height:"+divHeight/4+"px;text-align: center;float:left;color:"+colorPicker+";' ><span id='"+div+"span' style =' font-size:13px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>("+prefix+addCurrencyType(div, getMeasureId(measureArray[0]))+numberFormat(kpiResultArr[1],yAxisFormat,yAxisRounding,div,"\"suffix\"")+comparison+")</span></td>";
//<span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormat(kpiResultArr[1],yAxisFormat,yAxisRounding,div,"\"suffix\"")+comparison+")</span>\n\
//<span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+"</span></td>";
         
     }
     else{
  html+="<td style='width:"+divWidth+"px;height:"+divHeight/4+"px;float:left;color:"+colorPicker+";' ><span id='"+div+"span' style =' font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>("+prefix+"</span>\n\
<span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+addCurrencyType(div, getMeasureId(measureArray[0])) + numberFormatKPIChart(kpiResultArr[1],yAxisFormat,yAxisRounding,div,"\"suffix\"").toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span>\n\
<span id='"+div+"span' style ='font-size:"+comparativeFontSize+"px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+colorPicker+";font-family: Helvetica;'>"+suffix+comparison+")</span></td>";
    
     }
  html+="</tr>";    
  }
  
  // 3rd row
  if(chartData[div]["chartType"]==='Combo-Analysis'){
 html += "<tr style='border-style: solid;border-color:#D1D1D1;color:#D1D1D1;border-top: 0px double;border-width: 1px 0px 0px;'>";  
}else{
     html += "<tr style='border-style: solid;border-color:#D1D1D1;color:#D1D1D1;border-top: 0px double;border-width: 1px 0px 0px;'>";
  }
     if(typeof chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="undefined" && chartData[div]["KPIName"]!="" && chartData[div]["KPIName"]!=div  ){
  html+="<td onclick='callInitializeChart(\""+div+"\")' style='width:"+divWidth+"px;height:"+divHeight/3+"px;text-align: center;float:left;cursor:pointer;text-justify: inter-word;'><span style =' font-size:15px;line-height:30px;font-kerning: auto;font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+chartData[div]["KPIName"]+"</span></td>";
  }else{
  html+="<td onclick='callInitializeChart(\""+div+"\")' style='width:"+divWidth+"px;height:"+divHeight/3+"px;float:left;cursor:pointer;text-justify: inter-word;'><span style =' font-size:15px;line-height:30px;font-kerning: auto; text-align: justify;text-justify: inter-word; font-synthesis: weight style;font-variant: normal;color:"+measureColor+";font-family:Helvetica'>"+measureArray[0]+"</span></td>";
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
    buildWorldMapTable(div+"Tile",tableData, tableData[div],chartData[div]["viewBys"] ,measureArray , 590, 620)
    $("#openTableTile").dialog('open');
}
$("#suffix"+div).css("font-size", ""+comparativeFontSize+"px")
$("#suffix"+div).css("font-weight", "")
}


$(document).ready(function() {
    $("body").on("click", ".tabUL1", function() {
$(".tabUL1").css("border", "none");
        $(this).css("border-bottom", "5px solid #127ba5");
    });
});
