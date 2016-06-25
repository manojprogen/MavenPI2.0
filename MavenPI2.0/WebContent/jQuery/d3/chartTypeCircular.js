    function buildPie(div, data, columns, measureArray,wid,hgt) {
//alert(hgt)
//      added by manik      
    var tempChartData=[];
    for(var i in data){
        var dataMap={};
        var keys1=Object.keys(data[i]);
        for(var k in keys1){
            dataMap[keys1[k]]=data[i][keys1[k]];
        }
        tempChartData.push(dataMap);
    }
    var tempChartData2=[];
    for(i in data){
        var measValue=data[i][measureArray[0]];
        try{
            if(measValue<0){
                continue;
            }
            else{
                var dataMap2={};
                keys1=Object.keys(data[i]);
                for(k in keys1){
                    dataMap2[keys1[k]]=data[i][keys1[k]];
                }
                tempChartData2.push(dataMap2);
            }
        }
        catch(ex){
            dataMap2={};
            keys1=Object.keys(data[i]);
            for(k in keys1){
                dataMap2[keys1[k]]=data[i][keys1[k]];
            }
            tempChartData2.push(dataMap2);
        }
    }
    data=tempChartData2;
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

  var fromoneview=parent.$("#fromoneview").val();
    var dashletid;
// if(fromoneview!='null'&& fromoneview=='true'){
var chartData = JSON.parse(parent.$("#chartData").val());
     var color = d3.scale.category10();
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
    var divWidth, divHeight, rad;
  divWidth=wid;
divWid=wid;
 divHeight=hgt-20;
    rad=(Math.min((divWidth*.66), divHeight))*.52;
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
else if(chartData[div]["legendLocation"]==='onpie' ){
    legendAlign='onpie';
    
}else if(chartData[div]["legendLocation"]==='Outer' ){
    legendAlign='onpie';
    
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
    // var fun = "drillWithinchart(this.id,\""+div+"\")";
    var divnum = parseFloat(div.replace("chart", "", "gi"));
    var sum = d3.sum(tempChartData, function(d) {
        return d[measureArray[0]];
    });
	
	// added by manik for touch
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
	
	 if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            fun = "drillWithinchart(this.id)";
        }
        else {
            fun = "";
        }
    }
	}
	
	function drillFunct(id1){
	// alert("hi....");
	if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        drillChartInDashBoard(id1,div);
    } else if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            drillWithinchart(id1);
        }
        else {
            
        }
    }else{
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
    var drillStates = parent.$("#drillStatus").val();
    var chartDiv;
   
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
    var oldRadius=0;
    if(fromoneview!='null'&& fromoneview=='true'){
        div=div1;
    }
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]=='onpie'){
        oldRadius=radius;
        radius *=0.8
    }
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]=='Outer'){
        oldRadius=radius;
        radius *=0.8
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
             
                return d[measureArray[0]];
            });
            var topMargin;
            if(legendAlign=='Right' || legendAlign=='onpie'||legendAlign=='Outer')
                {
                    topMargin=0;
                }
            else
                {
                    topMargin=divHeight/9;
                }
    var svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ")
            .classed("svg-content-responsive", true)
            .datum(data)
            .attr("style", margintop);
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
    var lableColor;
var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
             .attr("transform",function(d){
              if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" && legendAlign!=='onpie'&& legendAlign!=='Outer')||legendAlign==='Bottom'){
            return "translate(" + width / 2 + "," + height / 2 + ")";
             }
             else if(legendAlign==='onpie' || legendAlign==='Outer'){
            return "translate(" + width / 2 + "," + height / 2 + ")";
             }
             else{
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
     return  "translate(" + width / 3 + "," + height / 2 + ")";
 }else{        return  "translate(" + width / 3.5 + "," + height / 2 + ")";        
             }
         }
             })
            .attr("onclick", fun)
			.dblTap(function(e,id) {

			drillFunct(id);
			})
			;
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
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }

            })
            .attr("index_value", function(d, i) {
         
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d,i) {
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
            	var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }

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
    var textAnchor="end";
        if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
            textAnchor="middle";
        }
    var text=arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
    
            .append("svg:text")
            .attr("dy", ".35em")
            .attr("text-anchor", textAnchor)
            .attr("class","gFontFamily")
           //            .attr("style", "font-size: 10px")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return parseInt(width/48)+"px";
              }
            })
            .style("fill", function(d, i){
               var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return "#000000";
                    }
               if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#d8d47d";
                               }
                               return lableColor;
            })
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }
                    else{
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius / 6;
                    }
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius / 2;
                }
                // Set Inner Coordinate
                var invertAngle=0;
                if(a>90){
                    if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
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
 if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]==='Outer'){
     if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
     if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else{  
                   var percentage = (d.value / parseFloat(sum)) * 100;
                   return percentage.toFixed(1) + "%";
                }}else {
      return "";
            }
 }else{
if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    if(data[i][columns[0]].toString().length>4){
        return data[i][columns[0]].substring(0, 4)+"..";
    } else{
        return data[i][columns[0]];
    }
}else{
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

if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]==='Outer'){
     oldRadius*=1.2;    
        var cx=[];
        var sx=[];
        var ox=[];
        var cy=[];
        var sy=[];
        var oy=[];
        svg.selectAll("g.arc")
        .data(pie)
        .enter()
        .append("text")
//        .attr("dy",function(d) {
//            var a = angle(d);
//            if (a > 90) {
//                a = a + 180;
//                return "-.5em" }else{
//                return ".2em"
//                }
//           })
           .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "end" }else{
                return "start"
                }
           })
        .attr("x", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cx =(width/2)+ Math.cos(a) * (radius );
            cx[i]=d.cx;
            return d.x =(width/2)+ Math.cos(a) * (radius + 15);
        })
        .attr("y", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cy = (height/2)- Math.sin(a) * (radius );
            cy[i]=d.cy;
            return d.y =(height/2)- Math.sin(a) * (radius + 35);
        })
        .style("fill","#A1A1A1")
        .style("font-size","10px")
        .text(function(d,i) {
           var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage<=4.5){
              return "";
          } else{
            if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else {
                return data[i][columns[0]];
            }
          } 
        })
        .each(function(d,i) {
            var bbox = this.getBBox();
            d.sx = d.x - bbox.width/2 - 2;
            d.ox = d.x + bbox.width/2 + 2;
            d.sy = d.oy = d.y + 5;
            sx[i]=d.sx;
            ox[i]=d.ox;
            sy[i]=d.sy;
            oy[i]=d.oy;
        });
        svg.selectAll("g.arc")
        .data(pie)
        .enter()
        .append("text")
//        .attr("dy",function(d) {
//            var a = angle(d);
//            if (a > 90) {
//                a = a + 180;
//                return "-.5em" }else{
//                return ".2em"
//                }
//           })
           .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "end" }else{
                return "start"
                }
           })
        .attr("x", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cx =(width/2)+ Math.cos(a) * (radius );
            cx[i]=d.cx;
            return d.x =(width/2)+ Math.cos(a) * (radius + 15);
        })
        .attr("y", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cy = (height/2)- Math.sin(a) * (radius );
            cy[i]=d.cy;
            return d.y =(height/1.85)- Math.sin(a) * (radius + 35);
        })
        .style("fill","#757575")
        .style("font-size","12px")
        .text(function(d,i){
var percentage = (d.value / parseFloat(sum)) * 100;
if(percentage<=4.5){
              return "";
          } else{
if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise") {
                return percentage.toFixed(1) + "%";
                }else {  
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
            }
        })
        .each(function(d,i) {
            var bbox = this.getBBox();
            d.sx = d.x - bbox.width/2 - 2;
            d.ox = d.x + bbox.width/2 + 2;
            d.sy = d.oy = d.y + 5;
            sx[i]=d.sx;
            ox[i]=d.ox;
            sy[i]=d.sy;
            oy[i]=d.oy;
        });
        
 }
else{
    
if(wid < 175 || hgt < 175){
 }else{
 
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]==='onpie'){
        oldRadius*=1.2;    
        var cx=[];
        var sx=[];
        var ox=[];
        var cy=[];
        var sy=[];
        var oy=[];
        svg.selectAll("g.arc")
        .data(pie)
        .enter()
        .append("text")
        .attr("text-anchor", "middle")
        .attr("x", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cx =(width/2)+ Math.cos(a) * (radius );
            cx[i]=d.cx;
            return d.x =(width/2)+ Math.cos(a) * (radius + 15);
        })
        .attr("y", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cy = (height/2)- Math.sin(a) * (radius );
            cy[i]=d.cy;
            return d.y =(height/2)- Math.sin(a) * (radius + 35);
        })
        .text(function(d,i) {
            if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else {
                return data[i][columns[0]];
            }
        })
        .each(function(d,i) {
            var bbox = this.getBBox();
            d.sx = d.x - bbox.width/2 - 2;
            d.ox = d.x + bbox.width/2 + 2;
            d.sy = d.oy = d.y + 5;
            sx[i]=d.sx;
            ox[i]=d.ox;
            sy[i]=d.sy;
            oy[i]=d.oy;
        });
        svg.append("defs").append("marker")
        .attr("id", "circ")
        .attr("markerWidth", 6)
        .attr("markerHeight", 6)
        .attr("refX", 3)
        .attr("refY", 3)
        .append("circle")
        .attr("cx", 3)
        .attr("cy", 3)
        .attr("r", 3);
        svg.selectAll("path.pointer").data(pie).enter()
        .append("path")
        .attr("class", "pointer")
        .style("fill", "none")
        .style("stroke", "grey")
        .attr("marker-end", "url(#circ)")
        .attr("d", function(d,i) {
            if(cx[i] > ox[i]) {
                return "M" + sx[i] + "," + sy[i] + "L" + ox[i] + "," + oy[i] + " " + cx[i] + "," + cy[i];
            } else {
                return "M" + ox[i] + "," + oy[i] + "L" + sx[i] + "," + sy[i] + " " + cx[i] + "," + cy[i];
            }
        });
    }
    else{
//dddd
var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width/45);
		var fontsize1 = parseInt(width/50);
		var rectsize = parseInt(width/60);
                var legendLength;
                //alert(chartData[div]["legendNo"]);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                   // alert('if');
                    
                }
                else{
                   // alert("else");
                   // alert(legendLength);
                    legendLength=(data.length<12 ? data.length : 12); 
                }
//                alert(legendLength);
                if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
        if(legendAlign==='Right')
        {
        if(legendLength>12){//
            yvalue = parseInt(height / 8);
            rectyvalue = parseInt((height / 8)-10);
            
        }
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);
            
        }
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
                    var startY=0;
                    if(legendLength>12){
                        startY=(height / 4)-(width*.035)
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
             .attr("transform", function(d,i){
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
     return  "translate(" + width*.68  + "," + parseInt(startY) +")";
 }else{        return  "translate("  + width*.58  + "," + parseInt(startY) + ")";        
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
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
.attr("transform", function(d,i){
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    if(wid < 395){ 
                return  "translate(" + width*.69  + "," + (rectyvalue+19) + ")";
        }else{
            return  "translate(" + width*.69  + "," + (rectyvalue+15) + ")";
        }
 }else{        return  "translate(" + width*.58  + "," + rectyvalue + ")";        
            }
            })
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
   .attr("transform", function(d,i){
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
     return  "translate(" + width*.73  + "," + (yvalue+15) + ")";//edit by shivam
 }else{        return  "translate(" + width*.61  + "," + yvalue + ")";        
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
            })
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
            .text(function(d){
                if(data[i][columns[0]].length>22){
                    return data[i][columns[0]].substring(0, 22)+"..";
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
             svg.append("g")   
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.80  + "," + parseInt(startY) + ")")
            .attr("fill", "#544F4F")
            .text(function(d){
              return measureArray[0]
            })
        
             
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
                       for(var i=0;i<legendLength ;i++){
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
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
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
//                for(var i=0;i<(data.length<10 ? data.length : 10) ;i++){
//                      if(data[i][measureArray[0]]>0){
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
}
}


}
function buildDonut(div,divId, data, columns, measureArray, divWidth, divHeight, rad) {
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
//                var drilledvalue = "";
                var colorShad;
//                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[columns[0]];
//                } catch (e) {
//                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
//                } else
//                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
//                    colorShad = drillShade;
//                }
//                else
//                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                }
//            else {
//                    colorShad = getDrawColor(div, parseInt(i));
//                }
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
            .dblTap(function(e,id) {

			drillFunct(id);
			})
//            .attr("transform", "translate(" + width / wDiv + "," + height / hDiv + ")")
           .attr("transform",function(d){
              if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" &&  legendAlign!=='Outer')||legendAlign==='Bottom'){
            return "translate(" + width / wDiv + "," + height / hDiv + ")";
             }else if(legendAlign==='Outer'){
            return "translate(" + width *.50 + "," + height / hDiv + ")";
             }else{
 if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
     return  "translate(" + width / wDiv + "," + height / hDiv + ")";
 }else{        return  "translate(" + width / 3.5 + "," + height / hDiv + ")";        
             }
             }
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
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                }
                else{
				var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }

            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
//                if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
//                {
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
            //.delay(function(d, i) { return 500 + i * 50; })
            .duration(2000)
            .attrTween("d", tweenDonut)
//           .dblTap(function(e,id) {
//
//			drillFunct(id);
//			})
                        ;
            
    function angle(d) {
        var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
        return a;
    }
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
    
 if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){
  arcs.append("text")
            .attr("dy",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "-.5em" }else{
                return ".2em"
                }
           })
           .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "end" }else{
                return "start"
                }
           })
            .style("font-size",function(d, i){
            if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                return (chartData[div]["labelFSize"]+"px");
            }else{
                return "11px";
            }
           })
            .style("fill", function(d, i){
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                lableColor = chartData[div]["labelColor"];
            }else {
                lableColor = "#000000";
            }
            return lableColor;
           })
            .attr("transform", function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    d.outerRadius = radius / 5; // Set Outer Coordinate
                    d.innerRadius = radius / 1.2;
                }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+70;
                }
            }else {
                d.outerRadius = radius+20; // Set Outer Coordinate
                d.innerRadius = radius+40;
            }
            return "translate(" + arc.centroid(d) + ")";
           })
            .text(function(d,i) {
           var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage<=4.5){
              return "";
          } else{
           if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                if(data[i][columns[0]].toString().length>4){
                    return data[i][columns[0]].substring(0, 4)+"..";
                }else{
                    return data[i][columns[0]];
                }}else{ 
                if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
     if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){ 
                        return percentage.toFixed(1) + "%";
                    }else {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }}else {
                    return "";
                }
            }}
        });
            arcs.append("text")
            .attr("dy",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return ".75em" }else{
                return "1.4em"
                }
           })
           .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "end" }else{
                return "start"
                }
           })
            .style("font-size",function(d, i){
            if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                return (chartData[div]["labelFSize"]+"px");
            }else{
                return "11px";
            }
           })
            .style("fill", function(d, i){
            if (typeof chartData[div]["labelColor"]!=="undefined") {
                lableColor = chartData[div]["labelColor"];
            }else {
                lableColor = "#000000";
            }
            return lableColor;
           })
            .attr("transform", function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                    d.outerRadius = radius / 5; // Set Outer Coordinate
                    d.innerRadius = radius / 1.2;
                }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+70;
                }
            }else{
                d.outerRadius = radius+20; // Set Outer Coordinate
                d.innerRadius = radius+40;
            }
            return "translate(" + arc.centroid(d) + ")";
           })
            .text(function(d,i) {
            var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage<=4.5){
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
                    d.innerRadius = radius / 2.3;  //edit by shivam
                    }
                } else {
                    d.innerRadius = radius / 1.3;  // edit by shivam
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
    if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){ 
if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
     if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else{  
                   var percentage = (d.value / parseFloat(sum)) * 100;
                   return percentage.toFixed(1) + "%";
                }}else { 
                return "";
            }
 }else{
if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
if (typeof displayType !== "undefined" && displayType === "Absolute") {
 if(yAxisFormat==""){
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div));
                    }else{
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
                }else {
                var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
               }else {
                return "";
               }
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div));
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
                    return "" + addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(JSON.parse(parent.$("#sumValue").val()).toFixed(0)) + "";
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
                      legendLength=(data.length<12 ? data.length : 12); 
                      }// add by mayank sharma for donut


        if(legendAlign==='Right')////
        {
        if(legendLength>12){//
            yvalue = parseInt(height / 8);
            rectyvalue = parseInt((height / 8)-10);
            
        }
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);
            
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
//        rectyvalue = parseInt((height / 4)-10);
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
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
           .attr("transform", function(d,i){
               if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                  return  "translate(" + width*.68  + "," + parseInt(startY) + ")";
               }else{
                  return  "translate(" + width*.58  + "," + parseInt(startY) + ")";        
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
                  return  "translate(" + width*.68  + "," + (rectyvalue-10) + ")";//edit by shivam
               }else{
                  return  "translate(" + width*.58  + "," + rectyvalue + ")";        
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
                  return  "translate(" + width*.73  + "," + (yvalue-10) + ")";//edit by shivam
               }else{
                  return  "translate(" + width*.61  + "," + yvalue + ")";        
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
             svg.append("g")   
            .append("text")
            .attr("style","margin-right:10")
            .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.87  + "," + parseInt(startY) + ")")
            .attr("fill", "#544F4F")
            .text(function(d){
               if (measureArray[0].length < 10){
                    return measureArray[0];
		}else{
                    return measureArray[0].substring(0, 10) + "..";}
            })
             
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
            .attr("transform", "translate(" + width*.88  + "," + (yvalue) + ")")
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
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
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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

 }
}

   }
   
   function buildDonut3D(div, data, columns, measureArray, width, height, radius, chartType) {
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
    };
   
	var fun="";
//    var fun = "drillWithinchart(this.id,\""+div+"\")";
//Added by shivam
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
        
var dashletid;
    var chartData = JSON.parse(parent.$("#chartData").val());
    var legendAlign;
    var fromoneview=parent.$("#fromoneview").val();
    var colIds= [];
     var drilledvalue;
       var widthd=width;
        var widthdr=width
    var divHgtdr=height
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
           width=widthd;
dashletid=div
 colIds = chartData[div1]["viewIds"];
     var prop = graphProp(div1);
 if(typeof chartData[div1]["legendLocation"]==='undefined' || chartData[div1]["legendLocation"]==='Bottom')
    {
        legendAlign='Bottom';
    }
    else
    {
        legendAlign='Right';
    }
}else{
    var prop = graphProp(div);
colIds = chartData[div]["viewIds"];

    if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Bottom')
    {
        legendAlign='Bottom';
    }
    else
    {
        legendAlign='Right';
    }
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
            if(typeof chartData[div]["dataDisplay"]==='undefined'||dataDisplay==='Yes')
                {
            if (typeof displayType !== "undefined" && displayType === "Absolute") {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div,div);
                }
                else {
            return (d.endAngle - d.startAngle > 0.2 ?
                    Math.round(1000 * (d.endAngle - d.startAngle) / (Math.PI * 2)) / 10 + '%' : '');
        }

        }
            else
                {
                    return '';
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
            var lableColor;
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
                    .attrTween("x", textTweenX).attrTween("y", textTweenY)
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
            .text(getPercent);
        };

        Donut3D.draw = function(id, data, x /*center x*/, y/*center y*/,
                rx/*radius x*/, ry/*radius y*/, h/*height*/, ir/*inner radius*/) {

            var _data = d3.layout.pie().sort(null).value(function(d) {
                return d[measureArray[0]];
            })(data);
            var slices;
            if(legendAlign==='Right')
            {
                slices = d3.select("#" + id).append("g").attr("transform", "translate(" + (x-width/10) + "," + y/1.25 + ")")
                    .attr("class", "slices");
            }
            else
            {
                slices = d3.select("#" + id).append("g").attr("transform", "translate(" + x + "," + y/1.75 + ")")
                .attr("class", "slices");
            }

            slices.selectAll(".innerSlice").data(_data).enter().append("path").attr("class", "innerSlice")
                    .style("fill", function(d, i) {

                try {
                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) != -1) {
                        return drillShade;
                    }
                     if(fromoneview!='null'&& fromoneview=='true'){
                         div=div1;
                     }
                       var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;

                        return d3.hsl(colorfill).darker(0.7);
                    })
                    .attr("d", function(d) {
                        return pieInner(d, rx + 0.5, ry + 0.5, h, ir);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
                    .on("mouseover", function(d, i) {
                        if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
                }else{
                        show_details(d.data, columns, measureArray, this,div);
                }
//                        show_details(d.data, columns, measureArray, this,div);
                    })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
                    }).attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
                    .attr("color_value", function(d, i) {
                          if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                      var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                    }).attr("onclick", fun)
                    .dblTap(function(e,id) {
		drillFunct(id);
                    }) 
                    ;

            slices.selectAll(".topSlice").data(_data).enter().append("path").attr("class", "topSlice")

                    .style("stroke", function(d, i) {
                        return getDrawColor(div, parseInt(i));
                    })
                    .attr("d", function(d) {
                        return pieTop(d, rx, ry, ir);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            }) .attr("fill", function(d, i) {
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                }else{
                 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
//                        var drawColor=getDrawColor(div, parseInt(i));
//                        return drawColor;
                    })

                    .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("color_value", function(d, i) {
                          if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                      var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                    }).attr("onclick", fun)
                    .dblTap(function(e,id) {
		drillFunct(id);
                    }) 
                     .on("mouseover", function(d, i) {
                         if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
                }else{
                        var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                        show_details(d.data, columns, measureArray, this,div);
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
                        hide_details(d, i, this,div);
                    });

            slices.selectAll(".outerSlice").data(_data).enter().append("path").attr("class", "outerSlice")

                    .attr("d", function(d) {
                        return pieOuter(d, rx - .5, ry - .5, h);
                    })
                    .each(function(d) {
                        this._current = d;
                    }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })  .attr("fill", function(d, i) {
                        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                      var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            })

                    .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
                    .attr("color_value", function(d, i) {
                         if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                      var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                    })
                    .attr("class", function(d, i) {
                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                    }).attr("onclick", fun)
                    .dblTap(function(e,id) {
		drillFunct(id);
                    }) 
                    .on("mouseover", function(d, i) {
                         if(fromoneview!='null'&& fromoneview=='true'){
                     show_detailsoneview(d.data, columns, measureArray, this,chartData,div1);
                }else{
                        var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                        show_details(d.data, columns, measureArray, this,div);
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

            slices.selectAll(".percent")
            .data(_data).enter()
            .append("text")
            .attr("class", "percent")
            .attr("text-anchor", "middle")   // edit by shivam
                    .attr("x", function(d) {
                        return 0.6 * rx * Math.cos(0.5 * (d.startAngle + d.endAngle));
                    })
                    .attr("y", function(d) {
                        return 0.6 * ry * Math.sin(0.5 * (d.startAngle + d.endAngle));
                    })
                    .style("font-size",function(d, i){
              var fontlabel;
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 fontlabel = (chartData[div]["labelFSize"]+"px")
          }else{
                 fontlabel= "10px";
              }
                return fontlabel;
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
                    .text(getPercent).each(function(d) {
                this._current = d;
            }).attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
                    .on("mouseover", function(d, i) {
                           if(fromoneview!='null'&& fromoneview=='true'){
                    show_details(d.data, columns, measureArray, this);
                }else{
                        show_details(d.data, columns, measureArray, this);
                }
                    })
                    .on("mouseout", function(d, i) {
                        hide_details(d, i, this);
            })
//                    .attr("index_value", function(d, i) {
//                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//            })
//                    .attr("color_value", function(d, i) {
//                        return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//                    })
//                    .attr("class", function(d, i) {
//                        return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
//                    })
                    .attr("onclick", fun)
                    .dblTap(function(e,id) {
		drillFunct(id);
                    }) 
                    ;
        };

        this.Donut3D = Donut3D;
    }();
    if(fromoneview!='null'&& fromoneview=='true'){
                         div=dashletid;
                     }
    var gIds = div+"g";
    var lableColor;
    var color = d3.scale.category10();
    var svg = d3.select("#"+div)
     //    added by manik
            // .append("div")
//            .classed("svg-container", true)
            // .attr("width", "100%")
            // .attr("height", "100%")
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width)+" "+(height )+" ");
//            var remarks='';
//    if(typeof chartData[div]["remarks"]!=="undefined" && chartData[div]["remarks"]!=="" )
//    {
//        remarks=chartData[div]["remarks"];
//
//    }
//    else
//    {
//        remarks='No remarks for this chart';
//    }
//    var remarkHeight=$("#svg_"+div).height/19
//    var div1=svg.append("foreignObject")
//        .attr("width", width)
//        .attr("height", 35)
//        .attr('x', 0)
//        .attr( 'y', height-45)
//        .append("xhtml:body")
//        .attr('xmlns','http://www.w3.org/1999/xhtml')
//        .html("<input id='txt"+div+"' type='text' style='float:left;height: "+height/20+"px;width: "+(width-40)+"px;display:none' value='"+remarks+"'/><label id='lbl"+div+"'  style='background-color:#F2F2F2;color:black;font-size:10px;text-align:left;height: "+height/20+"px;width: "+(width)+"px; ' value='"+remarks+"'><a id='a_"+div+"' style='margin-right:20px;float:right;width:16px;' onclick='editRemarks(\""+div+"\")' class='ui-icon ui-icon-pencil'></a>"+remarks+"</label>")
//        .on("change", function() {displayText('txt'+div,div)});
//            .classed("svg-content-responsive", true)
//    .attr("width", width)
//    .attr("height", height);
    svg.append("g").attr("id", gIds);

    var radiusValue = 0;
    var thickness = 0;
if(width<height){

  radiusValue = width*.32;
  thickness = width*.075;

}else{

   radiusValue = height*.4;
   thickness = height*.115;
}
    if (chartType === "Donut")
    {
        if(legendAlign==='Right')
        {
//            alert("#####")
            if(width<height)
            {
                radiusValue=(width*0.78)/2-width/15;
                Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue , radiusValue / (2), thickness, 0.4);
            }
            else
            {
                radiusValue=(height*1.2)/2-height/15;
                if(radiusValue*2>width*0.78)
                {
                     radiusValue=(width*0.78)/2-width/15;
                }
                Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue , radiusValue / (2), thickness, 0.4);
            }
        }
        else
        {
            if(radiusValue * 1.3*2>width)
            {
                radiusValue-=10;
            }
        Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue * 1.3, radiusValue / (2), thickness, 0.4);
        }

//        Donut3D.draw(gIds, data, width / 2, height / 2, radius * 1.3, radius / (2), 40, 0.4);
    }
    else
    {
        if(legendAlign==='Right')
        {
            if(width<height)
            {
                radiusValue=(width*0.78)/2-width/15;
                if(radiusValue*2>width*0.78)
                {
                     radiusValue=(width*0.78)/2-width/15;
                }
                Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue, radiusValue / (2), thickness, 0);
            }
            else
            {
                radiusValue=(height*1.2)/2-height/15;
                if(radiusValue*2>width*0.78)
                {
                     radiusValue=(width*0.78)/2-width/15;
                }
                Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue, radiusValue / (2), thickness, 0);
            }
        }
        else
        {
                if(radiusValue*2>width*0.78)
                {
                     radiusValue=(width*0.78)/2-width/15;
                }
        Donut3D.draw(gIds, data, width / 2, height / 2, radiusValue * 1.3, radiusValue / (2), thickness, 0);
        }
//        Donut3D.draw(gIds, data, width / 2, height / 2, radius * 1.3, radius / (2), 40, 0);
    }
    
   if(width < 175 || height < 175){
 }else{ 
    
     if(fromoneview!='null'&& fromoneview=='true'){
                 div=div1;
             }
      var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var widthvalue=0;
		var widthRectvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width*.02);
		var fontsize1 = parseInt(width*.02);
		var rectsize = parseInt(width/60);
                var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data.length<12 ? data.length : 12); //edit by shivam
                      }
//	yvalue = parseInt(height*.64);
        if(width<height){

     fontsize = parseInt(width*.023);
	 fontsize1 = parseInt(width*.023);
}else{

     fontsize = parseInt(height*.03);
	 fontsize1 = parseInt(height*.03);
}
     if(legendAlign==='Bottom')
    {
 yvalue = parseInt(height*.80);
    rectyvalue = parseInt((height *.78));
    }
    else
    {
        if(legendLength>12){
            yvalue = parseInt(height / 6);
            rectyvalue = parseInt((height / 6)-10);
        }
        else{
            yvalue = parseInt(height / 2-(legendLength/2)*(height*.06));
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);
        }
    }
//        rectyvalue = parseInt((height *.64)-7);
        widthvalue = parseInt((width *.1));
        widthRectvalue = parseInt((width *.1)-(width*.018));
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
        $("#txt"+div).css("font-size",fontsize1+"px");
        $("#lbl"+div).css("font-size",fontsize1+"px");
//        $("#a_"+div).height(fontsize1*1.5);
//        $("#a_"+div).width(fontsize1*1.5);

        var count = 0;
        var transform = "";

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength=="Yes")){
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//            .attr("style","margin-right:10")
//
//             .attr("style", "font-size:"+fontsize+"px")
//            .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
////            .attr("x",rectlen)
////            .attr("y",rectyvalue1)
//            .attr("fill", "Black")
//
////            .style("stroke", color(i))
//          //  .attr("dy",dyvalue )
//
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//              return columns[0]
//            })
                if(legendAlign==='Bottom')
                {
                for(var i=0;i<legendLength ;i++){
                 var charactercount = data[i][columns[0]].length;

               if(data[i][measureArray[0]]>0){
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
var drawColor=getDrawColor(div, parseInt(i));
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
            .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")

            .attr("id",function(d){
                return data[i][columns[0]] ;
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
                else
                {
                   var startY=0;
                   if(legendLength>12){
                        startY=(height / 8);
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }
                            svg.append("g")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.78  + "," + parseInt(startY) + ")")
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

                for(var i=0; i<legendLength; i++){

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
                        .attr("transform", "translate(" + width*.78  + "," + rectyvalue + ")")
                        .attr("width", rectsize)
                        .attr("height", rectsize)
                        .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})

                        svg.append("g")
                        .append("text")
                        .attr("transform", "translate(" + width*.81  + "," + (yvalue-1) + ")")//edit by shivam
                        .attr("fill", function(){
             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
              }
             }else{
                 return  "#000";
             }
            })    // end @@
                        .attr("style", "font-size:"+fontsize1+"px")
                        .attr("id",function(d){
                              return data[i][columns[0]];
                        } )
                        .text(function(d){
                            if(data[i][columns[0]].length>19){
                                return data[i][columns[0]].substring(0, 19)+"..";
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
                 var charactercount = data[i][columns[0]].length;
                   if(data[i][measureArray[0]]>0){
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
    var drawColor=getDrawColor(div, parseInt(i));
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
            } )
            .text(function(d){
                if(data[i][columns[0]].length>9){
                    return data[i][columns[0]].substring(0, 9)+"..";
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
            }else if(typeof displayLength!="undefined" && displayLength=="All"){
               for(var i in  data){
              var charactercount = data[i][columns[0]].length;
              if(data[i][measureArray[0]]>0){
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
            var drawColor=getDrawColor(div, parseInt(i));
            svg.append("g")
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})

            svg.append("g")
            .append("text")
            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
            .attr("style", "font-size:"+fontsize1+"px")
            .attr("id",function(d){
            } )
            .text(function(d){
                if(data[i][columns[0]].length>9){
                    return data[i][columns[0]].substring(0, 9)+"..";
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
               }}

            }else if(typeof displayLength!="undefined" && displayLength=="None"){

     }else{
               }
}
}

function buildHalfPie(div, data, columns, measureArray,wid,hgt) {
// added by shivam     
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

     var color = d3.scale.category10();
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
     var prop = graphProp(div1);
colIds=chartData[div1]["viewIds"];
dashletid=div1;
var legendAlign;
if(typeof chartData[div1]["legendLocation"]==='undefined'|| chartData[div1]["legendLocation"]==='Bottom')
{
    legendAlign='Bottom';
}
else
{
    legendAlign='Right';
}
}else{
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];
var legendAlign;
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Bottom')
{
    legendAlign='Bottom';
}else if(chartData[div]["legendLocation"]==='Outer'){
    legendAlign='Bottom';
}
else
{
    legendAlign='Right';
}
}
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
    }
    var pi = Math.PI;
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
         if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            fun = "drillWithinchart(this.id)";
        }
        else {
            fun = "";
        }
    }
        }
        function drillFunct(id1){
           if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        drillChartInDashBoard(id1,div);
         }else if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            fun = "drillWithinchart(this.id)";
        }
        else {
            fun = "";
        }
    }else {
        drillWithinchart(id1,div);
    }  
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
    var divnum = parseFloat(div.replace("chart", "", "gi"));
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
   
    var drillStates = parent.$("#drillStatus").val();
    var chartDiv;
    
    var height = Math.min(divWidth, divHeight);
    var width = Math.min(divWidth, divHeight);
    var width = divWidth;
    var width2;
    var margintop;
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true"))
    {
        height = divHeight * 1.7;
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
              margintop = "margin-left:6em";
                  radius = rad;
                  width2=width;
         }else{
             width2=width*.7;
         }

    }


    var outerRadius = 0;
if(widthRadius<heightRadius){
  if(parseInt(heightRadius-widthRadius)<(100)){
     outerRadius = parseInt(widthRadius*.25);
  }
  else{
    outerRadius = parseInt(widthRadius*.27);
} }
else{
    if(parseInt(widthRadius-heightRadius)<(100)){
      outerRadius = parseInt(heightRadius*.31);
  }
  else{
     outerRadius = parseInt(heightRadius*.33);
}
}
if(legendAlign==='Right')
{
    outerRadius *= 0.9;
}
if(outerRadius*2>width*0.62)
{
    outerRadius=((width*0.63)/2)*0.9;
}
    var arc = d3.svg.arc()
            .outerRadius(outerRadius*1.3);
//    var arcFinal = d3.svg.arc().innerRadius(radius).outerRadius(radius);
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            })
            .startAngle(-90 * (pi/180))
        .endAngle(90 * (pi/180));
       
        var align;
            if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){ 
                    align=-widthRadius*.03;
                }else{ 
                    align=widthRadius*.03;
                } 
       var lableColor;
    var svg = d3.select("#" + div).append("svg:svg")
            .datum(data)
            .attr("id", "svg_" + div)
             .attr("viewBox", "0 "+align+" "+(widthRadius*.8)+" "+(heightRadius*.8)+" ")
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
                            colorShad = getDrawColor(div, parseInt(i));
                        }
                    }
                    colorShad = getDrawColor(div, parseInt(i));
                }
                else {
                        colorShad = getDrawColor(div, parseInt(i));
                }
                chartMap[d[columns[0]]] = colorShad;
                return getDrawColor(div, parseInt(i));
            })
            .attr("stop-opacity", 1);
    parent.$("#colorMap").val(JSON.stringify(colorMap));
    
    var wDiv=2.5;
    if(legendAlign==='Right')
    {
        wDiv=3.45;
    }
    if(legendAlign==='Outer')
    {
        wDiv=2.5;
    }
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
            .attr("transform", function(d,i){
if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" && legendAlign!=='Outer')||legendAlign==='Bottom'){
            return  "translate(" + width / wDiv + "," + height / 1.8 + ")"
             }
             else if(legendAlign==='Outer'){
           return  "translate(" + width / wDiv + "," + height / 1.8 + ")"
             }   else{
          return  "translate(" + width / wDiv + "," + height / 1.8 + ")"
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

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d,i) {
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];

                    } catch (e) {
                    }
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
                 
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
//
                var columnList = [];
                columnList.push(columns[0]);
                prevColor = getDrawColor(div,parseInt(i));

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
    function angle(d) {
        return (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
    }
    arcs.filter(function(d) {
        return d.endAngle - d.startAngle > 0.2;
    })
           .append("svg:text")
            .attr("dy",function(d) {
                return ".35em"
              })
            .attr("text-anchor", "end")
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "9px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
    if (a < -90) {
                    a = a + 180;
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius / 5;
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius / 2.2;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .style("fill", function(d, i){
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
            .text(function(d) {   // half pie
       if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){           
                var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
            }else{
                if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
                if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                       return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else{  
                   return percentage.toFixed(1) + "%";  
                }}else {
                return "";
            } }
            }else{
             if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
               if(typeof chartData[div]["valueOf"]==='undefined'||chartData[div]["valueOf"]==='Absolute'){
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
               }else{
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%"
               }}else{
                        return '';
                    }
            }
            })
            
 if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){             //add by mayank sharma 
           arcs.append("svg:text")
            .attr("dy","1.2em") 
            .attr("text-anchor",  function(d,i) {
             var a = angle(d);
            if (a < -90) {
                    a = a + 180;
                    return "end" 
                }else{
                     return "start"
                }
            })
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
             }else{
                 return "9px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
            if (a < -90) {
                    a = a + 180;
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius+30;
                    return "translate(" + arc.centroid(d) + ")rotate(45)";
                }else{
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius +40;
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
                               }
             })
            .style("fill", "#696969")
            .text(function(d) {   // half pie
                var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
            } else{
             if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes"){
           if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                       var percentage = (d.value / parseFloat(sum)) * 100;
                   return percentage.toFixed(1) + "%"; 
                }else{  
                   return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }}else {
                return "";
            } }
            })
           
           
           arcs.append("svg:text")
            .attr("dy", ".2em")
            .attr("text-anchor",  function(d,i) {
             var a = angle(d);
            if (a < -90) {
                    a = a + 180;
                    return "end" 
                }else{
                     return "start"
                }
            })
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "9px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
    if (a < -90) {
                    a = a + 180;
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius+30;
                return "translate(" + arc.centroid(d) + ")rotate(45)";
                }else{
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius +40;
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
                               }
             })
            .style("fill", "#696969")
            .text(function(d,i) {   // half pie
             var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
          } else{
             if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
               if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else{
                return data[i][columns[0]];
            }}else{
                        return '';
                    } }
            });
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
   if(typeof chartData[div]["legendLocation"]==='undefined' ||chartData[div]["legendLocation"]==='Outer') {}else{
if(width < 175 || height < 175){
 }else{    
    
      var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var widthvalue=0;
		var widthRectvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width/60);//edit by shivam
		var fontsize1 = parseInt(width/60);
		var rectsize = parseInt(width/75);
                rectsize=rectsize>9?9:rectsize;
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data.length<12 ? data.length : 12);//edit by shivam 
                      }
//	yvalue = parseInt(height*.64);
if(legendAlign==='Bottom')
{
        if(width<height){
         yvalue = parseInt(width*.92);
    rectyvalue = parseInt((width *.90));
     fontsize = parseInt(width*.023);
	 fontsize1 = parseInt(width*.023);
}else{
     yvalue = parseInt(height*.77);
    rectyvalue = parseInt((height *.75));
     fontsize = parseInt(height*.03);
	 fontsize1 = parseInt(height*.025);
}
}
else
{
    if(legendLength>12){
        yvalue = parseInt(height / 7);
        rectyvalue = parseInt((height / 7)-10);
    }
    else{
        yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
        rectyvalue = parseInt((height / 2)-(legendLength/2)*(height*.06)-10);
    }
}

// yvalue = parseInt(height*.75);
//    rectyvalue = parseInt((height *.73));
//        rectyvalue = parseInt((height *.64)-7);
        widthvalue = parseInt((width *.1));
        widthRectvalue = parseInt((width *.1)-(width*.018));
        $("#txt"+div).css("font-size",fontsize1+"px");
        $("#lbl"+div).css("font-size",fontsize1+"px");
//        $("#a_"+div).height(fontsize1*1.5);
//        $("#a_"+div).width(fontsize1*1.5);
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
        var count = 0;
        var transform = "";

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength=="Yes")){
    if(legendAlign==='Bottom')
    {
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//            .attr("style","margin-right:10")
//
//             .attr("style", "font-size:"+fontsize+"px")
//            .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
////            .attr("x",rectlen)
////            .attr("y",rectyvalue1)
//            .attr("fill", "Black")
//
////            .style("stroke", color(i))
//          //  .attr("dy",dyvalue )
//
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//              return columns[0]
//            })
                for(var i=0; i<legendLength; i++){
                     if(data[i][measureArray[0]]>0){
                 var charactercount = data[i][columns[0]].length;
              if(i!=0){

               widthvalue = parseInt(widthvalue +  (width*.18) )
               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
               if(count%4==0){
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
                
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")

            .attr("id",function(d){
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
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
    else
    {
                var startY=0;
                    if(legendLength>12){
                        startY=(height / 11)
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }
                    svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.56  + "," + parseInt(startY) + ")")
//            .attr("x",rectlen)
//            .attr("y",rectyvalue1)
            .attr("fill", "Black")

//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

//            .text("" + measureArray[i] + "");
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
            yvalue = parseInt(yvalue+height*.059)
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
            .attr("transform", "translate(" + width*.56  + "," + rectyvalue*.80 + ")")//edit by shivam
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.59  + "," + yvalue*.80 + ")")// edit by shivam
            .attr("fill", function(){

             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
              
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			
              }
             }else{
                 return  "#000";
             }
            })    // end @@
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
                 var charactercount = data[i][columns[0]].length;
              if(i!=0){

               widthvalue = parseInt(widthvalue +  (width*.18) )
               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
               if(count%4==0){
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
               
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			
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
            }else if(typeof displayLength!="undefined" && displayLength=="All"){
               for(var i in  data){
                      if(data[i][measureArray[0]]>0){
              var charactercount = data[i][columns[0]].length;
              if(i!=0){

               widthvalue = parseInt(widthvalue +  (width*.18) )
               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
               if(count%4==0){
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
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
               }
            }
            }
 }
   }
}

function buildHalfDonut(div, data, columns, measureArray, divWidth, divHeight, rad) {
     var color = d3.scale.category10();
     var fromoneview=parent.$("#fromoneview").val();
      var widthdr=divWidth
      var mainWidth = divWidth
    var divHgtdr=divHeight;
          if(divWidth<divHeight)
    {
        rad=(divWidth/2)-(divWidth*0.1);
    }
    else
    {
        rad=(divHeight/2)*0.8;
    }

    if(fromoneview!='null'&& fromoneview=='true')
    {

     }
     else{

   divWidth=parseFloat($(window).width())*(.35);
     }
    var isDashboard = parent.$("#isDashboard").val();
    var chartMap = {};
    var chartType = parent.$("#chartType").val();
    if (chartType === "dashboard") {
        isDashboard = true;
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
    
    
var chartData = JSON.parse(parent.$("#chartData").val());
var colIds= [];
 var dashletid;
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
dashletid=div;
colIds=chartData[div1]["viewIds"];
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
    var prop = graphProp(div);
    colIds=chartData[div]["viewIds"];

var legendAlign;
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Bottom')
{
    legendAlign='Bottom';
}else if(chartData[div]["legendLocation"]==='Outer'){
    legendAlign='Bottom';
}
else
{
    legendAlign='Right';
}
}
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
             drillChartInDashBoard(id1, div);
          
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
 var olap=div.substring(0, 9);
if(olap=='olapchart'){
fun = "viewAdhoctypes(this.id)";
    }
// fun = "parent.oneviewdrillWithinchart(this.id,'"+div1+"','"+repname+"','"+repId+"','"+chartData+"','"+regid+"','"+oneviewid+"')";
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
//        if(fromoneview!='null' && fromoneview=='true'){
//
//        height = divHeight+80;
////        width1=divWidth+150;
//         divWidth = divWidth;
//         width = divWidth;
//          radius=height*.40;
//              margintop = "-60px";
//         }
//        else{
        height = divHeight;
        margintop = "-60px";
        radius = rad / 1.2;
//    }
    if(radius*2>width*0.78)
    {
        radius=((width*0.78)/2)-(width*0.05);
    }
    }
    var arcFinal = d3.svg.arc()
    .innerRadius(radius)
    .outerRadius(radius);
    
    var arc = d3.svg.arc()
     .outerRadius(radius);
            
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            })
            .startAngle(-90 * (pi/180))
            .endAngle(90 * (pi/180));
            
            var align;
            if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){ 
                    align=-divWidth*.13;
                }else{ 
                    align=0;
                } 
            
     var lableColor;
    var svg = d3.select("#" + div).append("svg:svg")
            .datum(data)
                .attr("id", "svg_" + div)
                .attr("viewBox", "0 "+align+" "+(divWidth)+" "+(height)+" ");
                
    var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
            .append("svg:linearGradient")
            //    .attr("id", function(d) {return color(d.name);)
            .attr("id", function(d) {
                return "gradient" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
//            .attr("x1", "0%")
//            .attr("y1", "0%")
//            .attr("x2", "50%")
//            .attr("y2", "100%")
            .attr("spreadMethod", "reflect");

    gradient.append("svg:stop")
            .attr("offset", "0%")
            .attr("stop-color", "rgb(240,240,240)")
            .attr("stop-opacity", 1);
    gradient.append("svg:stop")
            .attr("offset", "60%")
            .attr("stop-color", function(d, i) {
//        var drilledvalue = parent.$("#drilledValue").val().split(",");
//                var drilledvalue = "";
                var colorShad;
////                try {
//                    drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
//                } catch (e) {
//                }
//                if (isShadedColor) {
//                    colorShad = color(d[shadingMeasure]);
//                } else if (conditionalShading) {
//                    colorShad = getConditionalColor(color(i), d[conditionalMeasure]);
//                } else if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {//
//                    colorShad = drillShade;
//                } else {
//                        if(fromoneview!='null'&& fromoneview=='true'){
//                             colorShad = color(i);
//                        }else{
//                if (typeof centralColorMap[d[columns[0]].toString().toLowerCase()] !== "undefined") {
//                    colorShad = centralColorMap[d[columns[0]].toString().toLowerCase()];
//                } else {
//                    colorShad = color(i);
//                }
//                        }
//                }
//                chartMap[d[columns[0]]] = colorShad;
//                 if(fromoneview!='null'&& fromoneview=='true'){
//
//                 }else{
//                colorMap[i] = d[columns[0]] + "__" + colorShad;
//                 }
                return colorShad;
            })
            .attr("stop-opacity", 1);
             if(fromoneview!='null'&& fromoneview=='true'){

             }else{
    parent.$("#colorMap").val(JSON.stringify(colorMap));
             }
//    svg.append("svg:rect")
//            .attr("width", divWidth)
//            .attr("height", height-50)
//            .attr("onclick", "reset()")
//            .attr("class", "background");
if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
 var wDiv,hDiv;
    if(legendAlign==='Bottom')
    {
        hDiv=2.8;
        wDiv=2;
    }
    else if(legendAlign==='Outer'){
        hDiv=2.8;
        wDiv=2;
    }
    else
    {
        hDiv=2.3;
        wDiv=2.45;
    }
    var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("class", "arc")
            .attr("id", function(d) {
                return d.data[columns[0]] + ":" + d.data[measureArray[0]];
            })
            .attr("onclick", fun)
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	})
    .attr("transform", function(d,i){
if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" && legendAlign!=='Outer')||legendAlign==='Bottom'){
            return  "translate(" + width / wDiv + "," + height /hDiv+ ")"
             }
             else if(legendAlign==='Outer'){
           return  "translate(" + width / wDiv + "," + height /hDiv + ")"
             }   else{
          return  "translate(" + width / wDiv + "," + height /hDiv+ ")"
             }      
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
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }

            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
            .on("mouseover", function(d, i) {
//
                var columnList = [];
                columnList.push(columns[0]);
                prevColor = color(i);

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
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "8px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
                 d.outerRadius = radius;
                   if (a < -90) {
                    a = a + 180;
                    d.innerRadius = radius / 2.3; 
                  }else {
                    d.innerRadius = radius / 1.3;
                   }  
               return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .style("fill", function(d, i){
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
            .text(function(d) {   // half donut
        if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){          
                var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
            }else{
             if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
                if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){    
                       return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else{  
                   return percentage.toFixed(1) + "%";  
                }}else {
                return "";
            } }
            }else{  
               if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
               if(typeof chartData[div]["valueOf"]==='undefined'||chartData[div]["valueOf"]==='Absolute'){
                    return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
               }else{
                var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%"
               }}else{
                     return '';
                 }
            }
            })
            
            
 if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){             //add by mayank sharma 
             arcs.append("svg:text")
            .attr("dy","1.35em")
            .attr("text-anchor",  function(d,i) {
             var a = angle(d);
            if (a < -90) {
                    a = a + 180;
                    return "end" 
                }else{
                     return "start"
                }
            })
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "9px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
                 d.outerRadius = radius;
                 if (a < -90) {
                    a = a + 180;
                     d.innerRadius = radius+25; 
                }else {
                    d.innerRadius = radius+48;
                }
               return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .style("fill", function(d, i){
               var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#bab5b5";
                               }
                               return lableColor;
             })
             .text(function(d) {   // half donut   
                var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
            } else{
//             if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){  
           if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                   return percentage.toFixed(1) + "%"; 
                }else{  
                   return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                } }
            })
           
           
            arcs.append("svg:text")
            .attr("dy", ".2em")
            .attr("text-anchor",  function(d,i) {
             var a = angle(d);
            if (a < -90) {
                    a = a + 180;
                    return "end" 
                }else{
                     return "start"
                }
            })
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
            }else{
                 return "9px";
              }
            })
            .attr("transform", function(d) {
                var a = angle(d);
          if (a < -90) {
                    a = a + 180;
                    d.outerRadius = radius / 2; // Set Outer Coordinate
                    d.innerRadius = radius+25; 
                }else{
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius +40;
                }
                return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
            })
            .style("fill", function(d, i){
               var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#bab5b5";
                               }
                               return lableColor;
             })
            .text(function(d,i) {   // half Donut
             var percentage = (d.value / parseFloat(sum)) * 100;
                if(percentage<=4.5){
              return "";
             }else{
             if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
               if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else {
                return data[i][columns[0]];
            }}else{
                        return '';
                    } }
            });
 }    
 
    var center_group = svg.append("svg:g")
            .attr("class", "ctrGroup")
            .attr("transform", "translate(" + (width / 2) + "," + (height / 2) + ")");
//    var pieLabel = center_group.append("svg:text")
//            .attr("dy", "-2em")
//            .attr("text-anchor", "middle")
//            .attr("style", "font-family: lucida grande")
//            .attr("style", "font-size: 11px")
//            .text("" + measureArray[0] + "")
//            .attr("transform", "translate(" + (width *.01) + "," + (height *.09) + ")");
    center_group.append("svg:text")
            .attr("dy", "-1em")
            .attr("text-anchor", "middle")
            .attr("class","gFontFamily")
            .attr("style", "font-size: 10px")
            .text(function(d) {
                if (typeof parent.$("#sumValue").val() !== "undefined" && parent.$("#sumValue").val() !== "") {
                    return "" + addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(JSON.parse(parent.$("#sumValue").val()).toFixed(0)) + "";
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
if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Outer'){}else{
if(width < 175 || height < 175){
 }else{

     var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var widthvalue=0;
		var widthRectvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width*.02);
		var fontsize1 = parseInt(width*.02);
		var rectsize = parseInt(width/60);
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data.length<12 ? data.length : 12); 
                      }
//	yvalue = parseInt(height*.64);
    if(legendAlign==='Bottom')
    {
        if(width<height){
            yvalue = parseInt(width*.65);
            rectyvalue = parseInt((width *.63));
     fontsize = parseInt(width*.023);
	 fontsize1 = parseInt(width*.023);
}else{
            yvalue = parseInt(height*.53);
            rectyvalue = parseInt((height *.51));
     fontsize = parseInt(height*.03);
	 fontsize1 = parseInt(height*.03);
}
    }
    else
    {
        if(legendLength>12){
            yvalue = parseInt(height / 8);
            rectyvalue = parseInt((height / 8)-10);
        }
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2)-(legendLength/2)*(height*.06)-10);
        }
    }

// yvalue = parseInt(height*.75);
//    rectyvalue = parseInt((height *.73));
//        rectyvalue = parseInt((height *.64)-7);
        widthvalue = parseInt((width *.1));
        widthRectvalue = parseInt((width *.1)-(width*.018));

        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
        var count = 0;
        var transform = "";

if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength=="Yes")){
    {
        if(legendAlign==='Bottom')
        {
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//            .attr("style","margin-right:10")
//
//             .attr("style", "font-size:"+fontsize+"px")
//            .attr("transform", "translate(" + width*.68  + "," + parseInt((height / 4)-(width*.035)) + ")")
////            .attr("x",rectlen)
////            .attr("y",rectyvalue1)
//            .attr("fill", "Black")
//
////            .style("stroke", color(i))
//          //  .attr("dy",dyvalue )
//
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//              return columns[0]
//            })
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
            .attr("fill", function(){
			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
                 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
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
                if(data[i][columns[0]].length>13){
                    return data[i][columns[0]].substring(0, 13)+"..";
                }else {
                    return data[i][columns[0]];
          }
           })
           .attr("svg:title",function(d){
               return data[i][columns[0]];
           })
              .on("mouseover", function(d, i) {
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
                     setMouseOverEvent(this.id,div1)
                }else{

                       var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
            setMouseOverEvent(this.id,div)
                }

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
   
   var startY=0;
                    if(legendLength>12){
                        startY=height / 14
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }	
                         svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
            .attr("style","margin-right:10")

             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.78  + "," + parseInt(startY) + ")")
//            .attr("x",rectlen)
//            .attr("y",rectyvalue1)
            .attr("fill", "Black")

//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

//            .text("" + measureArray[i] + "");
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
                               for(var i=0;i<legendLength;i++){
                     if(data[i][measureArray[0]]>0){
                if(i!=0){
            yvalue = parseInt(yvalue+height*.045)
            rectyvalue = parseInt(rectyvalue+height*.045)
            }
            svg.append("g")
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")

//            .attr("x",rectlen)
//            .attr("y",rectyvalue)
            .attr("transform", "translate(" + width*.78  + "," + rectyvalue + ")")
            .attr("width", rectsize)
            .attr("height", rectsize)
            .attr("fill", function(){
               var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            })
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
            .append("text")
          //  .attr("style","margin-right:10")

          //  .attr("transform", transform)
//            .attr("x",len)
//            .attr("y",yvalue)
            .attr("transform", "translate(" + width*.81  + "," + (yvalue-4) + ")")//edit by shivam
            .attr("fill", function(){

             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
              if(chartData[div]["colorLegend"]=="Black") {
                  return "#000";
              } else{
                 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
              }
             }else{
                 return  "#000";
             }
            })    // end @@
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
     }
            if(typeof displayLength!="undefined" && displayLength!="None" && displayLength!="All"){
               for(var i=0;i<displayLength;i++){
                 var charactercount = data[i][columns[0]].length;
                     if(data[i][measureArray[0]]>0){
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
            .attr("fill", function(){
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            })
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
                 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
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
               }
               }
            }else if(typeof displayLength!="undefined" && displayLength=="All"){
               for(var i in  data){
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
            .attr("fill", function(){
			 var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
			})
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
            .attr("fill", color(i))
            .attr("style", "font-size:"+fontsize1+"px")
//            .style("stroke", color(i))
        //    .attr("dy",dyvalue )
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
               }
            }
            }else if(typeof displayLength!="undefined" && displayLength=="None"){

 }else{
 }
 }}
}




function buildAster(div,data,columns,measureArray,divWidth,divHeight){

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
var width = divWidth,
    height = divHeight,
    radius = Math.min(width, height) / 2.2,
    innerRadius = 0.35 * radius;
 var color = d3.scale.category10();
var pie = d3.layout.pie()
    .sort(null)
    .value(function(d) { return d.width; });
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


var chartData = JSON.parse($("#chartData").val());
var max = maximumValue(data, measureArray[1]);
var arc = d3.svg.arc()
  .innerRadius(innerRadius)
  .outerRadius(function (d) { 
//      var value = d.data.score;
      var value = max/100;
//      if(value > 100){
//          value = 100;
//      }
    return (radius - innerRadius) * ((d.data.score/value) / 100.0) + innerRadius; 
//    return (radius - innerRadius) * (value / 100.0) + innerRadius; 
//    return (radius - innerRadius) * (d.data.score * .01) + innerRadius; 
  });

var outlineArc = d3.svg.arc()
        .innerRadius(innerRadius)
        .outerRadius(radius);
var topMargin =0;
var svg = d3.select("#"+div).append("svg")
    .attr("id", "svg_" + div)
            .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ")
            .classed("svg-content-responsive", true)
    .append("g")
    .attr("transform",function(d){
        var svgWidth = 2;
//        if(width<height){
//           
//            svgWidth = 2;
//        }
        return "translate(" + width /svgWidth + "," + height / 2.2 + ")"
    })



  data.forEach(function(d,i) {
    d.id     =  d[columns[0]];
    d.order  = +d[measureArray[0]];
    d.color  =  color(i);
    d.weight = + d[measureArray[0]];
    d.score  = +d[measureArray[1]];
    d.width  = + d[measureArray[0]];
    d.label  =  d[columns[0]];
  });
  // for (var i = 0; i < data.score; i++) { console.log(data[i].id) }
  
  var path = svg.selectAll(".solidArc")
      .data(pie(data))
    .enter().append("path")
      .attr("fill", function(d) { return d.data.color; })
      .attr("class", "solidArc")
      .attr("stroke", "gray")
      .attr("d", arc)
       .attr("id", function(d) {
//                var drillValued = d.data[columns[0]];
                return d.data.label + ":" + d.data.weight;

            })
       .attr("index_value", function(d, i) {
         
                return "index-" + d.data.label.replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
//                return "url(#gradient" + (d.data[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi') + ")";
//                var drawColor=getDrawColor(div, parseInt(i));
//                    return drawColor;
            	var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            })
            .attr("class", function(d, i) {
                return "bars-Bubble-index-" + d.data.label.replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            })
      .on('mouseover', function(d){
         var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
         var content = "";
//                content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + data[count][columns[0]] + "</span><br/>";
//                content += d.data.label + ": <span style='color:orangered'>" + d.data.score + DATA"</span>";
                   content += "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + d.data.label + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[0] + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(d.data.weight) + "</span><br/>";
                content += "<span class=\"name\"> " + measureArray[1] + ":</span><span class=\"value\"> " + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(d.data.score) + "</span><br/>";
                return tooltip.showTooltip(content, d3.event);
      })
      .on('mouseout', function(d,i){
        var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    var colorValue = selectedBar.attr("color_value");
                    selectedBar.style("fill", colorValue);
       hide_details(d, i, this); 
      }).attr("onclick", fun)
      .dblTap(function(e,id) {
		drillFunct(id);
	})
        ;;
      
path.transition();
  var outerPath = svg.selectAll(".outlineArc")
      .data(pie(data))
    .enter().append("path")
      .attr("fill", "none")
      .attr("stroke", "gray")
      .attr("class", "outlineArc")
      .attr("d", outlineArc);  


  // calculate the weighted mean score
  var score = 
    data.reduce(function(a, b) {
      //console.log('a:' + a + ', b.score: ' + b.score + ', b.weight: ' + b.weight);
      return a + (b.score * b.weight); 
    }, 0) / 
    data.reduce(function(a, b) { 
      return a + b.weight; 
    }, 0);

  svg.append("svg:text")
    .attr("class", "aster-score")
    .attr("dy", ".35em")
    .attr("text-anchor", "middle")
    .style("font-size","30px")// text-align: right
    .text(Math.round(score));


}

 function buildTangent(div,json, columns, measureArray,width,height ){
     
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
    
    //    var width = $(window).width() - 100;
    //    var height = $(window).height() - 10;
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
    

    var topMargin = 0;
    var chartData = JSON.parse($("#chartData").val());
    var svg = d3.select('#'+div).append("svg")
    .attr("id", "svg_" + div)
    .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ")
    .classed("svg-content-responsive", true);
    //    svg.append("svg:rect")
    //            .attr("width", width)
    //            .attr("height", height)
    //            .attr("onclick", "reset()")
    //            .attr("class", "background");
    svg = svg.append('g')
    .attr('transform',function(d){
        var xScale = width/2.4;
        var yScale = height / 3.8;
        var heightValue = height;
        if(width<height){
            yScale = height * .001;
            xScale = width/2.8;
        }else if(width>height && heightValue < 200){
            yScale = height / 4.5;
        }
        return 'translate(' + xScale + ',' + yScale+ ')';  
    });


    var color = d3.scale.category10();
    var formatSI = d3.format('s');
    var formatCurrencySI = function(d) {
        return '$' + formatSI(d);
    };
    var data = [];
    var totLength;
    for (var no = 0; no < (json.length < 6 ? json.length : 6); no++) {
        data.push(json[no]);
    }
    totLength = data.length;
    //    if(json.length>2){
    //        totLength=3;
    //    for(var l=0;l<3;l++){
    //        if(json.length>2){
    //            data.push(json[l]);
    //        }
    //    }
    //    }
    //    else if(json.length==2){
    //         totLength=2;
    //    for(var l=0;l<2;l++){
    //            data.push(json[l]);
    //    }
    //    }
    //    else if(json.length==1){
    //         totLength=1;
    //    for(var l=0;l<1;l++){
    //            data.push(json[l]);
    //    }
    //    }
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var min, max;
    min = minimumValue(data, measureArray[0]);
    max = maximumValue(data, measureArray[0]);
    var totalRange = Math.min(width, height);
    var minrate = (200 * min) / max;
    //    var minrate = 0;
    var scale = d3.scale.sqrt()
    .domain([min, max])
    .range([minrate, totalRange * .40]);
    scale.domain([min, max]);
    var circleKey = scaleKeyCircle()
    .scale(scale)
    //                .tickValues([10, 100, 500, 1000])
    .tickValues(data)
    //                .tickFormat(formatCurrencySI)
    .tickPadding(10)
    .orient("left");

    svg.append('g')
    .attr('transform', 'translate(50, 200)')
    .call(circleKey);

    function scaleKeyCircle() {
        var scale,
        orient = "left",
        tickPadding = 3,
        tickExtend = 5,
        tickArguments_ = [2],
        tickValues = null,
        tickFormat_;

        function key(g) {
            g.each(function() {
                var g = d3.select(this);
                var indexOfData = -1;
                //      var ticks1 = [10,100,500,1000];
                var colors = [];
                var ticks1 = [];
                for (var i = 0; i < data.length; i++) {
                    ticks1.push(data[i][measureArray[0]]);
                    if (typeof centralColorMap[data[i][columns[0]].toLowerCase()] !== "undefined") {
                        colors.push(centralColorMap[data[i][columns[0]].toLowerCase()]);
                    }
                    else {
                        colors.push(color(data[i][columns[0]]));
                    }
                }
                // Ticks, or domain values for ordinal scales.
                var ticks = ticks1 === null ? (scale.ticks ? scale.ticks.apply(scale, tickArguments_) : scale.domain()) : ticks1,
                tickFormat = tickFormat_ === null ? (scale.tickFormat ? scale.tickFormat.apply(scale, tickArguments_) : String) : tickFormat_;
                //      var ticks = tickValues === null ? (scale.ticks ? scale.ticks.apply(scale, tickArguments_) : scale.domain()) : tickValues,
                //          tickFormat = tickFormat_ === null ? (scale.tickFormat ? scale.tickFormat.apply(scale, tickArguments_) : String) : tickFormat_;

                //      ticks = ticks.slice().reverse().filter(function(t) { return t > 0 })
          
                ticks.forEach(function(tick, i) {
                    var gg = g.append('g')
                    .attr('class', 'circleKey')
                    .attr('transform', 'translate(0,' + -scale(tick) + ')');
                    parent.$("#colorMap").val(JSON.stringify(colorMap));
                    gg.append('circle')
                    .attr('cx', 0)
                    .attr('cy', 0)
                    .attr('r', function(d,i){
                              
                        return scale(tick)
                        })
                        .attr("id", function(d) {
                        return data[i][columns[0]] + ":" + data[i][measureArray[0]];
                    })
                       
                    .attr("index_value", function(d) {
         
                        return "index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                    })
                    .attr("class", function(d) {
                        
                        indexOfData++;
                        return "bars-Bubble-index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
                    })
                    .attr("fill",
                        function(d) {
                            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                        return colorfill;
                        })
                    .attr("color_value", function(d) {
                       
                        var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                        return colorfill;
                    }) .on("mouseover", function(d) {
                        var bar = d3.select(this);
                        var indexValue = bar.attr("index_value");
                        var barSelector = "." + "bars-Bubble-" + indexValue+div;
                        var selectedBar = d3.selectAll(barSelector);
                        selectedBar.style("fill", drillShade);
                        show_details(data[i], columns, measureArray, this);
                    }).on("mouseout", function(d) {
                        var bar = d3.select(this);
                        var indexValue = bar.attr("index_value");
                        var barSelector = "." + "bars-Bubble-" + indexValue+div;
                        var selectedBar = d3.selectAll(barSelector);
                        var colorValue = selectedBar.attr("color_value");
                        selectedBar.style("fill", colorValue);
                        hide_details(data[i], i, this);
                    })
                    .attr("onclick", fun)
                   
            //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) ;
                   
                    var x1 = scale(tick),
                    x2 = tickExtend + scale(ticks[0]),
                    tx = x2 + tickPadding,
                    textAnchor = "start";
                    if (i % 2 === 0 && "left" === orient) {
                        x1 = -x1;
                        x2 = -x2;
                        tx = -tx;
                        textAnchor = "end";
                    }
                    else if (i % 2 !== 0 && "right" === orient) {
                        x1 = -x1;
                        x2 = -x2;
                        tx = -tx;
                        textAnchor = "end";
                    }
                    gg.append('line')
                    .attr('x1', x1)
                    .attr('x2', x2)
                    .attr('y1', 0)
                    .attr('y2', 0)
                    .attr('stroke', '#000')
                    .text(tick);
                    gg.append('text')
                    .attr('transform', 'translate(' + tx + ', 0)')
                    .attr('dy', '.35em')
                    .attr("class", "tangentlabel")
                    .style('text-anchor', textAnchor)
                    .style('font-size',function(d){
                        var fontSize = Math.min(width, height);
                        return fontSize * .001;
                    })
                    .text(function(d) {
                        var tickVal = (tick).replace(",", "", "gi");
                        var percentage = (parseInt(tickVal) / parseInt(sum)) * 100;
                        return percentage.toFixed(1) + "%";
                    });
                });
            });
        }

        key.scale = function(value) {
            if (!arguments.length)
                return scale;
            scale = value;
            return key;
        };

        key.orient = function(value) {
            if (!arguments.length)
                return orient;
            orient = value;
            return key;
        };

        key.ticks = function() {
            if (!arguments.length)
                return tickArguments_;
            tickArguments_ = arguments;
            return key;
        };

        key.tickFormat = function(x) {
            if (!arguments.length)
                return tickFormat_;
            tickFormat_ = x;
            return key;
        };

        key.tickValues = function(x) {
            if (!arguments.length)
                return tickValues;
            tickValues = x;
            return key;
        };

        key.tickPadding = function(x) {
            if (!arguments.length)
                return tickPadding;
            tickPadding = +x;
            return key;
        };

        key.tickExtend = function(x) {
            if (!arguments.length)
                return tickExtend;
            tickExtend = +x;
            return key;
        };

        key.width = function(value) {
            if (!arguments.length)
                return width;
            width = value;
            return key;
        };
        key.height = function(value) {
            if (!arguments.length)
                return height;
            height = value;
            return key;
        };
        return key;
    }
}

 function buildColumnPie(div, data0, columns, measureArray,wid,hgt,KpiResult) {
 var allZero=0;
 if(typeof KpiResult!=='undefined'){
     for(i=0;i<KpiResult.length; i++){
         if(typeof KpiResult[i]!=='undefined'){
             if(parseFloat(KpiResult[i])!==0){
                 allZero=1;
                 break;
             }
         }
         else{
             return;
         }
     }
 }
 else{
     return;
 }
if(allZero===0){
    return;
}
  var appendDiv = "";
if(div.indexOf("colPieDiv_")!=-1){
appendDiv = div
var splitDiv = div.split("_");
div = splitDiv[1]
}else{
appendDiv = div
}
var length = measureArray.length;
var data = [];
for (var k=0;k<length;k++){
    var dataMap = {};
   dataMap[columns[0]]=measureArray[k];
   dataMap[measureArray[0]] = KpiResult[k];
data.push(dataMap);
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

  var fromoneview=parent.$("#fromoneview").val();
    var dashletid;
// if(fromoneview!='null'&& fromoneview=='true'){
var chartData = JSON.parse(parent.$("#chartData").val());
     var color = d3.scale.category10();
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
    var divWidth, divHeight, rad;
  divWidth=wid;
divWid=wid;
 divHeight=hgt-20;
    rad=(Math.min((divWidth*.66), divHeight))*.52;
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
else if(chartData[div]["legendLocation"]==='onpie'){
    legendAlign='onpie';
    legendAlign='Outer';
}
else if(chartData[div]["legendLocation"]==='Outer'){
    legendAlign='Outer';
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
    // var fun = "drillWithinchart(this.id,\""+div+"\")";
    var divnum = parseFloat(div.replace("chart", "", "gi"));
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
	// added by manik for touch
//	var fun="drilltoReports(this.id,\""+div+"\")";
//	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
//	if(hasTouch){
//	fun="";
//    }else{
//	fun = "drillWithinchart(this.id,\""+div+"\")";
//    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
//        fun = "drillChartInDashBoard(this.id,'" + div + "')";
//    }
//	
//	 if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
//        chartDiv = JSON.parse(parent.$("#chartData").val());
//        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
//            fun = "drillWithinchart(this.id)";
//        }
//        else {
//            fun = "";
//        }
//    }
//	}
	
	function drillFunct(id1){
	if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        drillChartInDashBoard(id1,div);
    } else if (typeof drillStates !== "undefined" && drillStates !== "" && parent.$("#dashBoardType").val() === "drilldash") {
        chartDiv = JSON.parse(parent.$("#chartData").val());
        if (div === Object.keys(chartDiv)[Object.keys(chartDiv).length - 1] || (Object.keys(chartDiv).length > 6 && divnum >= 6)) {
            drillWithinchart(id1);
        }else {
        }
    }else{
	drillWithinchart(id1,div);
	}
	}
	
	
	
// if(fromoneview!='null'&& fromoneview=='true'){
//var regid=div.replace("chart","");
//        var repId = parent.$("#graphsId").val();
//    var repname = parent.$("#graphName").val();
//      var oneviewid= parent.$("#oneViewId").val();
//
// fun = "oneviewdrillWithinchart(this.id,\""+div1+"\",\""+repname+"\",\""+repId+"\",'"+chartData+"',\""+regid+"\",\""+oneviewid+"\")";
//// fun = "oneviewdrillWithinchart1(this.id)";
//var olap=div.substring(0, 9);
//if(olap=='olapchart'){
//fun = "viewAdhoctypes(this.id)";
//    }
//    }
    var drillStates = parent.$("#drillStatus").val();
    var chartDiv;
   
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
    var oldRadius=0;
    if(fromoneview!='null'&& fromoneview=='true'){
        div=div1;
    }
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]=='onpie'){
        oldRadius=radius;
        radius *=0.8
    }
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]=='Outer'){
        oldRadius=radius;
        radius *=0.8
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashletid;
    }
    var arc = d3.svg.arc()
            .outerRadius(radius);
    var labelArc = d3.svg.arc()
.innerRadius(radius)
.outerRadius(radius);
    var pie = d3.layout.pie().sort(null) //this will create arc data for us given a list of OrderUnits
            .value(function(d) {
                return d[measureArray[0]];
            });
            var topMargin;
            if(legendAlign=='Right' || legendAlign=='onpie')
                {
                    topMargin=0;
                }else if(legendAlign=='Outer'){
                    if(chartData[div]["chartType"]==='Combo-Analysis'){   
                    topMargin=15;
                }else{
                        topMargin=-5;
                    }
                }else{
                    topMargin=divHeight/9;
                }
    var svg = d3.select("#" + appendDiv)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 "+topMargin+" "+(width)+" "+(height )+" ")
            .classed("svg-content-responsive", true)
            .datum(data)
            .attr("style", margintop);
 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
    var lableColor;
var arcs = svg.selectAll("g.arc")
            .data(pie)
            .enter().append("svg:g")
            .attr("id", function(d) {
                return "header::header";
            }).attr("onclick",function(d,i){
                return "drilltoReports('header::header',\""+div+"\",\""+getMeasureId(measureArray[i])+"\")";
            })
             .attr("transform",function(d){
              if((typeof chartData[div]["displayLegends"]!="undefined" && chartData[div]["displayLegends"]==="None" && legendAlign!=='onpie'&& legendAlign!=='Outer')||legendAlign==='Bottom'){
            return "translate(" + width / 2 + "," + height / 2 + ")";
             }
             else if(legendAlign==='onpie'||legendAlign==='Outer'){
           if(chartData[div]["chartType"]==='Combo-Analysis'){
           return "translate(" + width / 2 + "," + height / 1.7 +")";
		   }else{
			   return "translate(" + width / 2 + "," + height / 2.2 +")";
             }
		}else{
                    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
              return "translate(" + width / 3 + "," + height / 2 + ")";
                }else{  
              return "translate(" + width / 3.5 + "," + height / 2 + ")";
             }
             }
             })
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
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
            })
            .attr("index_value", function(d, i) {
                return "index-" + d.data[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            })
            .attr("color_value", function(d, i) {
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
            	var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
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
  msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div));
//               content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + "******"+percentage+"</span><br/>";//Added by shivam
               
               
//             if(typeof (chartData[div]["tooltipType"])!=="undefined"){
//             if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="With%"){
//                content += "<span style=\"font-family:helvetica;\" class=\"name\"> "+percentage.toFixed(1) + "%</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
//            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default-With%"){
//                 content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "("+percentage.toFixed(1) + "%)</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i]  + " </span><br/>";
//            }else if((chartData[div]["tooltipType"][measureArray[i]]).toString()==="Default"){
//                  content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
//            }
//            }else{
                  content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " </span><br/>";
//            }
               
               
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
    
    if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){  
   if(chartData[div]["chartType"]==='Combo-Analysis'){   
  arcs.append("text")
            .attr("dy", ".2em")
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
                if(typeof KpiResult[i]=="undefined" || !KpiResult[i]>0.0){
return  "0 px"
   }
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return parseInt(width/28)+"px";
              }
            })
            .style("fill", function(d, i){
               if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                   if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+80;
                    }
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+20;
                }
                return "translate(" + arc.centroid(d) + ")";
            })
            .text(function(d,i) {
          var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage==0){
               return "";
           }else{
if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    if(data[i][columns[0]].toString().length>4){
        return data[i][columns[0]].substring(0, 4)+"..";
    }else{
        return data[i][columns[0]];
    }}else{
    var percentage = (d.value / parseFloat(sum)) * 100;
   if(percentage<=8){
                return ""; 
   }else{
if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                return percentage.toFixed(2) + "%";
                }else {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
            }
        }
           }

            });
            
            arcs.append("text")
            .attr("dy", function(d) {
             var a = angle(d);
                if (a > 180) { 
                    a = a + 180;
                    return "2.4em"
                }else{
                    return "1.6em"
                }
            })
            .attr("class","gFontFamily")
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                 return parseInt(width/28)+"px";
              }
            })
            .style("fill", function(d, i){
               if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
             .attr("transform", function(d) {
             var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                   if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }else{
                    d.outerRadius = radius+20; // Set Outer Coordinate
                    d.innerRadius = radius+110;
                    }
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+20;
                }
                return "translate(" + arc.centroid(d) + ")";
            })
            .text(function(d,i) {
            var percentage = (d.value / parseFloat(sum)) * 100;
          if(percentage==0){
               return "";
           }else{
if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    if(data[i][columns[0]].toString().length>4){
        return data[i][columns[0]].substring(0, 4)+"..";
    }else{
        return data[i][columns[0]];
    }
}else{
     var percentage = (d.value / parseFloat(sum)) * 100;
   if(percentage<=8){
                return ""; 
   }else{
        var measureName='';
    if(typeof chartData[div]["measureAliasCombo"]!=='undefined' && typeof chartData[div]["measureAliasCombo"][measureArray[i]]!='undefined' && chartData[div]["measureAliasCombo"][measureArray[i]]!== measureArray[i]){
        measureName=chartData[div]["measureAliasCombo"][measureArray[i]];
    }else{
        measureName=measureArray[i];
    }
    return measureName;
        }}
           }
      });
      
   }else{         // addd   
       arcs.append("text")
            .attr("dy", function(d) {
             var a = angle(d);
                if (a > 90) { 
                    a = a + 180;
                    return "-.2em"
                }else{
                    return ".2em"
                }
            })
            .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "end" }else{
                return "start"
                }
           })
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                       return "11px";
              }
            })
            .style("fill",  "#696969")
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                   if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }else{
                        if (a > -90) {
                        a = a + 180;  
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+70;}else{
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+50;
                    }
                    }
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+60;
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
//if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes")  {
    var percentage = (d.value / parseFloat(sum)) * 100;
   if(percentage<=8){
                return ""; 
   }else{
if (typeof chartData[div]["valueOf1"] === "undefined" || chartData[div]["valueOf1"] === "Out%-wise"){  
                return percentage.toFixed(1) + "%";
                }else {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }
            } 
            
            }
            });
            
            arcs.append("text")
             .attr("dy", function(d) {
             var a = angle(d);
                if (a < 90) {
                    a = a + 180;
                    return "1.6em"
                }else{
                    if (a > 180) {
                        a = a + 0;
                    return "2.6em"
                   }else if(a > 90){
                    a=a+180;
                     return "-.2em"
                    }else{
                        return "-1.2em"
                    }
                }
            })
            .attr("text-anchor",function(d) {
            var a = angle(d);
            if (a > 90) {
                a = a + 180;
                return "middle" }else{
                return "start"
                }
           })
            .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                  return (chartData[div]["labelFSize"]+"px");
              }else{
                       return "10px";
              }
            })
            .style("fill",  "#696969")
             .attr("transform", function(d) {
             var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                   if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }else{   
                    d.outerRadius = radius+10; // Set Outer Coordinate
                    d.innerRadius = radius+110;
                    }
                } else {
                      d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius+50;
                    }
//                    d.outerRadius = radius; // Set Outer Coordinate
//                    d.innerRadius = radius+80;
                    
//                }
                return "translate(" + arc.centroid(d) + ")";
            })
//            .style("word-wrap","break-word")
            .text(function(d,i) {
if(typeof displayType!='undefined' && (displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
    if(data[i][columns[0]].toString().length>4){
        return data[i][columns[0]].substring(0, 4)+"..";
    }else{
        return data[i][columns[0]];
    }
}else{
     var percentage = (d.value / parseFloat(sum)) * 100;
   if(percentage<=8){
                return ""; 
   }else{
    return measureArray[i];
            }}
      });
            }
   }
            
            
            arcs.append("text")
            .attr("dy", ".35em")
            .attr("text-anchor", "end")
            .attr("class","gFontFamily")
            .attr("style", "font-size: 12px")
            .attr("transform", function(d) {
               return "translate(" + (labelArc.centroid(d)) + ")";
            });
    var textAnchor="end";
        if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
           textAnchor="middle";
        }
    var text=arcs.filter(function(d) {
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
                 return parseInt(width/45)+"px";
              }
            })
            .style("fill", function(d, i){
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
            .attr("transform", function(d) {
                var a = angle(d);
                if (a > 90) {
                    a = a + 180;
                    if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
                        d.outerRadius = radius / 5; // Set Outer Coordinate
                        d.innerRadius = radius / 2;
                    }
                    else{
                    d.outerRadius = radius / 3; // Set Outer Coordinate
                    d.innerRadius = radius / 6;
                    }
                } else {
                    d.outerRadius = radius; // Set Outer Coordinate
                    d.innerRadius = radius / 2;
                }
                var invertAngle=0;
                if(a>90){
                    if(typeof displayType!='undefined' &&(displayType === "labelwithval" || displayType === "labelwithcont" || displayType === "labelwithvalcont")){
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
    }
    else{
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
if (typeof displayType !== "undefined" && displayType === "Absolute") {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
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
            if(typeof chartData[div]["dataDisplay"] ==="undefined" || dataDisplay === "Yes")  {
if (typeof displayType !== "undefined" && displayType === "Absolute") {
                        return numberFormat(d.data[measureArray[0]],yAxisFormat,yAxisRounding,div);
                }else {
                   var percentage = (d.value / parseFloat(sum)) * 100;
                return percentage.toFixed(1) + "%";
                }
            }else {
                return "";
            }
        });
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
if (typeof chartData[div]["legendLocation"]!='undefined' &&chartData[div]["legendLocation"] === 'Outer'){}else{
if(wid < 175 || hgt < 175){
 }else{
 
    if(typeof chartData[div]["legendLocation"]!='undefined' && chartData[div]["legendLocation"]==='onpie'){
        oldRadius*=1.2;    
        var cx=[];
        var sx=[];
        var ox=[];
        var cy=[];
        var sy=[];
        var oy=[];
        svg.selectAll("g.arc")
        .data(pie)
        .enter()
        .append("text")
        .attr("text-anchor", "middle")
        .attr("x", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cx =(width/2)+ Math.cos(a) * (radius );
            cx[i]=d.cx;
            return d.x =(width/2)+ Math.cos(a) * (radius + 15);
        })
        .attr("y", function(d,i) {
            var a = 180-(d.startAngle + (d.endAngle - d.startAngle)/2)-45 - Math.PI/2;
            d.cy = (height/2)- Math.sin(a) * (radius );
            cy[i]=d.cy;
            return d.y =(height/2)- Math.sin(a) * (radius + 35);
        })
        .text(function(d,i) {
            if(data[i][columns[0]].length>25){
                return data[i][columns[0]].substring(0, 25);
            }else {
                return data[i][columns[0]];
            }
        })
        .each(function(d,i) {
            var bbox = this.getBBox();
            d.sx = d.x - bbox.width/2 - 2;
            d.ox = d.x + bbox.width/2 + 2;
            d.sy = d.oy = d.y + 5;
            sx[i]=d.sx;
            ox[i]=d.ox;
            sy[i]=d.sy;
            oy[i]=d.oy;
        });
        svg.append("defs").append("marker")
        .attr("id", "circ")
        .attr("markerWidth", 6)
        .attr("markerHeight", 6)
        .attr("refX", 3)
        .attr("refY", 3)
        .append("circle")
        .attr("cx", 3)
        .attr("cy", 3)
        .attr("r", 3);
        svg.selectAll("path.pointer").data(pie).enter()
        .append("path")
        .attr("class", "pointer")
        .style("fill", "none")
        .style("stroke", "grey")
        .attr("marker-end", "url(#circ)")
        .attr("d", function(d,i) {
            if(cx[i] > ox[i]) {
                return "M" + sx[i] + "," + sy[i] + "L" + ox[i] + "," + oy[i] + " " + cx[i] + "," + cy[i];
            } else {
                return "M" + ox[i] + "," + oy[i] + "L" + sx[i] + "," + sy[i] + " " + cx[i] + "," + cy[i];
            }
        });
    }
    else{
//dddd
var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
		var rectyvalue=0;
		var rectyvalue1=0;
		var len = parseInt(width-150);
		var rectlen = parseInt(width-200);
		var fontsize = parseInt(width/45);
		var fontsize1 = parseInt(width/50);
		var rectsize = parseInt(width/60);
                var legendLength;
                //alert(chartData[div]["legendNo"]);
                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                   // alert('if');
                    
                }
                else{
                   // alert("else");
                   // alert(legendLength);
                    legendLength=(data.length<15 ? data.length : 15); 
                }
//                alert(legendLength);
                if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }
                if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                    fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
                }
        if(legendAlign==='Right')
        {
        if(legendLength>12){//
            yvalue = parseInt(height / 4);
            rectyvalue = parseInt((height / 4)-10);
            
        }
        else{
            yvalue = parseInt(height / 2)-(legendLength/2)*(height*.06);
            rectyvalue = parseInt((height / 2-(legendLength/2)*(height*.06))-10);
            
        }
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
                    var startY=0;
                    if(legendLength>12){
                        startY=(height / 4)-(width*.035)
                    }
                    else{
                        startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
                    }
            svg.append("g")
         //   .attr("class", "y axis")
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
//            .attr("x",rectlen)
//            .attr("y",rectyvalue1)
            .attr("fill", "Black")

//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

//            .text("" + measureArray[i] + "");
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
         //   .attr("class", "y axis")
            .append("rect")
            .attr("style","margin-right:10")
            .attr("transform", transform)
            .attr("style", "overflow:scroll")
            .attr("transform", function(d,i){
           if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
                 if(width < 395){ 
                return  "translate(" + width*.68  + "," + (rectyvalue+4) + ")";
                }else{  
            return  "translate(" + width*.68  + "," + (rectyvalue) + ")";
        }
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
//            .style("stroke", color(i))
          //  .attr("dy",dyvalue )

            svg.append("g")
         //   .attr("class", "y axis")
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
                       for(var i=0;i<legendLength ;i++){
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
                return d[i][columns[0]];
            } )
//            .text("" + measureArray[i] + "");
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
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
           .attr("svg:title",function(d){
               return data[i][columns[0]];
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
}
}
}