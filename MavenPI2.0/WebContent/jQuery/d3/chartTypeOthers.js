/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function bubbleHorizontal(div, data0, columns, measures, divWidth, divHeight, layout) {
     var color = d3.scale.category10();
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
    var chartData = JSON.parse(parent.$("#chartData").val());
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
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    svg = d3.select("#" + div)
       //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWidth - layout)+" "+(divHeight  - 50 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", divWidth - layout)
//            .attr("height", divHeight  - 50)

 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
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

                var left =0;
    var node = selection.enter().append("g")
            .attr("class", "node")
            .attr("transform", function(d,i) {
                var top = parseInt(divHeight)*.52;
              
                
                left += d.r *2;
               
                
                return "translate(" + left + ", " + top + ")";
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
             .attr("fill", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getDrawColor(div, parseInt(i));
            })
             .attr("color_value", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getDrawColor(div, parseInt(i));
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
                prevColor = getDrawColor(div, parseInt(i));

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
                    }
                    else {
                        if (no === 0) {
                            msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(d.value);
                        } else {
                            msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(d["value" + no]);
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

    node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "10pt")
            .attr("fill",  function(d, i){
               if (typeof chartData[div]["lableColor"]!=="undefined" && chartData[div]["lableColor"] !== "default") {
                              lableColor = "#FFFFFF";
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
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
    node.append("line")
          .attr("x1",function(d){
              return 0;
          })
          .attr("y1",function(d){
              return -1*d.r;
          })
          .attr("x2",function(d){
              return 0;
          })
          .attr("y2",function(d){
              return (-1*d.r)-50;
          });
          
      node.append("circle")
          .attr("r",function(d){
              return 3;
          })
          .attr("cx",function(d){
              return 0;
          })
          .attr("cy",function(d){
              return -1*d.r;
          })
          .style("fill", function(d,i){
                return color(i);
        })
          node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("x",function(d){
              return 0;
          })
            .attr("y",function(d){
              return (-1*d.r)-60;
          })
            .attr("font-family", "Verdana")
            .attr("font-size", "10pt")
            .attr("fill",  function(d, i){
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
                    return d.name1;
//                }
            });
//    parent.legendMap[div] = chartMap;
    //toggleParamDIv();
}


function buildTableBar(chartId,data, currdata, columns, measureArray, divwidth, divHght,KPIresult) {
  
     var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
     var chartData = JSON.parse(parent.$("#chartData").val());
//  var colIds = chartData[chartId]["viewIds"];
  
     var div1=parent.$("#chartname").val();
var fromoneview=parent.$("#fromoneview").val();
var colIds= [];
var div;

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
         var fillBackground="";
         if (typeof chartData[div]["fillBackground"]!=="undefined"){
          fillBackground=chartData[div]["fillBackground"];
        }else{
            fillBackground="#FFF";
        };
var htmlstr="";

// var html = "";
//        if(data[chartId].length == 1){
//        htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"max-height:" + divHght + "px; \">";
//        }else{
             htmlstr = htmlstr + "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"max-height:" + divHght + "px; width:100%\">";
//        }
        htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='height:400px;width:100%;margin-left:auto;margin-top:auto'>";
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

          htmlstr += "<td >"+addCurrencyType(div, getMeasureId(measureArray[b])) + addCommas(d[measureArray[b]])+"</td>";

     });
        htmlstr +="</strong></td>"
         htmlstr += "</tr>";
        }
         htmlstr += "</table>";
        }//Transpose of Table
        else{

//htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;overflow-y:auto;overflow-x:none;margin-top:20px;float:right;width:100%;'>";
               
   htmlstr = htmlstr + "<table id=\"chartTableBar1" + div + "\" class='table table-condensed' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<thead>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
//htmlstr += "<th ></th>";
// for table
var width = $(window).width();
 var columnInd = columns[0];
var minMaxMap = {};
    for (var k = 0; k < measureArray.length; k++) {
//        dataTa/ble += "<td align='left'><span style='font-size:12px;font-weight:bold;cursor:pointer;' onclick='changeMeasureArray(\""+k+"\")'>" + measureArray[k] + "</span></td>";
        var max = maximumValue(data[chartId], measureArray[k]);
        var min = minimumValue(data[chartId], measureArray[k]);
        var temp = {};
        temp["min"] = min;
        temp["max"] = max;
        minMaxMap[measureArray[k]] = temp;
    }
    var wid = parseFloat(width) * (80 / 100) - 140;
    var tdwid = wid / (measureArray.length);
for(var q in columns){
if(columns[q].length > 20){
htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q].substring(0, 20)+"..</th>";
}else {
htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q]+"</th>";
}
}
//for(var q in columns){
//htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' ></th>";
//}
htmlstr += "<th style='white-space:nowrap;font-weight:bold;'></th>";
for(var i in measureArray){
if(measureArray[i].length > 15){
var nameAlign='left';
if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measureArray[i]] !== "undefined") {
    nameAlign=chartData[chartId]["measNameAlign"][measureArray[i]];
}
    
htmlstr += "<th title='"+measureArray[i]+"' style='white-space:nowrap;font-weight:bold;cursor:pointer;text-align:"+nameAlign+"'>"+measureArray[i].substring(0,15)+"..</th>";
}else {
    var nameAlign='left';
if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measureArray[i]] !== "undefined") {
    nameAlign=chartData[chartId]["measNameAlign"][measureArray[i]];
}
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:"+nameAlign+"'>"+measureArray[i]+"</th>";
}
 
//        if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
//                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
//                        htmlstr += "<th width='5%' style=\" color:black\"></th>"
//         }
//                    else{
//                        if( measureArray[i]===chartData[chartId]["emojiAbsValue"][0]){
//        htmlstr += "<th width='5%' style=\" color:black\"></th>"
//                        }
//                    }
//         }
         }
htmlstr += "</tr>";
htmlstr += "</thead>"
  var colorValue = d3.scale.category10();
  htmlstr += "<tbody>";
//  alert(JSON.stringifycurrdata)
data[chartId].forEach(function(d, i) {
           var drawColor=getDrawColor("chart1", parseInt(0));    
         var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "white";
            } else {
                color = "white";
            }
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
           
            htmlstr = htmlstr + "<tr style=\"background-color:" + fillBackground + ";\" >";
//                <td  style=\"background-color:" + color + ";\" width=25px>"  ;
//            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="'+drawColor+'" /></svg>';
//            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              
                   for(var l=0;l<columns.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {

                           htmlstr = htmlstr + "<td nowrap id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                  
                               htmlstr = htmlstr + "<td nowrap id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + fillBackground + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
               }
                    }
//                   for(var j=0;j<measureArray.length;j++){
//                   if(j==0){
                   htmlstr = htmlstr + "<td nowrap style=\"background-color:" + fillBackground + ";cursor:pointer;\" width=" + trWidth*.20 + " >" + addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
//                   }
//                    }
                    var emojiWdith=divwidth/27;
            emojiWdith=emojiWdith>22?22:emojiWdith;
            for(var j=0;j<measureArray.length;j++){
                
            var valueAlign='left';
            if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measureArray[j]] !== "undefined") {
                valueAlign=chartData[chartId]["measValueAlign"][measureArray[j]];
            }  
                
               var scale = d3.scale.linear().domain([minMaxMap[measureArray[0]]["max"], minMaxMap[measureArray[0]]["min"]]).range(["90%", "1%"]);
//    dataTable +="<td>"+displayData[i][measureArray[k]]+"</td>";
            var wid = scale(d[measureArray[0]]);
            if (parseFloat(wid.replace("%", "")) < 1) {
                wid = 1+"%";
            }
            if (parseFloat(wid.replace("%", "")) > 100) {
                wid = 100+"%";
            }
            if(j==0){
            htmlstr += "<td style='white-space:nowrap;' width='auto';><canvas id='" + data[chartId][i][columnInd] + "_" + measureArray[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[j] + ":" + addCurrencyType(div, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "' style='width:"+wid+";height:18px;margin-left:5px;margin-right:5px;background:" + getDrawColor(chartId,j) + "' onmouseove=''></canvas></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
            }else {
            htmlstr = htmlstr + "<td  style=\"background-color:" + fillBackground + ";text-align:"+valueAlign+";color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(div, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
            }
            
//            htmlstr += "<td height='15px'><canvas id='m' title='" + measureArray[j] + ":" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding)) + "' width='" + wid + "' height='18' style='margin-left:5px;margin-right:5px;background:" + colorValue(j) + "' onmouseove=''></canvas><span style='font-size:10px;float:'>" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding)) + "</span></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
        
//       var canvas = document.getElementById(data[chartId][i][columnInd] + "_" + measureArray[j].replace(/[^a-zA-Z0-9]/g, '', 'gi'));
       
//       if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!='hidden'){
//                    if(typeof chartData[chartId]["showEmoji"]!=='undefined' && chartData[chartId]["showEmoji"]!=='absolute'){
//                        var targetValue=chartData[chartId]["emojiValue"];
//                        var ctxPath=parent.document.getElementById("h").value;
//                        var imageName;
//                        if(chartData[chartId]["emojiVisible"][j]===true){
//                        if(parseInt(d[measureArray[j]])>targetValue[j]){
//                            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='happy_face.png';
//            }
//                else{
//                    imageName='green.png';
//                }
//                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//            }
//                        else if(parseInt(d[measureArray[j]])==targetValue[i]){
//                            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='normal_face.png';
//            }
//                else{
//                    imageName='amber.png';
//                }
//                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//            }
//        else{
//            if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='sad_face.png';
//            }
//                else{
//                    imageName='red.png';
//                }
//                            htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//                        }
//                    }
//                    else{
//                            htmlstr += "<td></td>"
//                        }
//                    }
//                    else{
//                        if(measureArray[j]==chartData[chartId]["emojiAbsValue"][0]){
//                            
//                            var targetValue=chartData[chartId]["emojiAbsValue"];
//            var ctxPath=parent.document.getElementById("h").value;
//                            if(parseInt(d[targetValue[0]])>targetValue[1]){
//                                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='happy_face.png';
//            }
//                else{
//                    imageName='green.png';
//                }
//                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//            }
//                            else if(parseInt(d[targetValue[0]])==targetValue[1]){
//                                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='normal_face.png';
//            }
//                else{
//                    imageName='amber.png';
//                }
//                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//            }
//            else{
//                if(typeof chartData[chartId]["imageType"]==='undefined' || chartData[chartId]["imageType"]==='emoji'){
//                    imageName='sad_face.png';
//            }
//                else{
//                    imageName='red.png';
//                }
//                                htmlstr += "<td style=\"background-color:"+color+";color:black\" ><img src='"+ctxPath+"/images/"+imageName+"' width='"+emojiWdith+"px' height='2%' style='float:right;'></td>";
//            }
//        }
//                    }
//                }    
            }
                            
            htmlstr = htmlstr + "</tr>";

        });    
        htmlstr += "</tbody>";
                var colspan = columns.length;
if(typeof chartData[chartId]["showGT"]!=='undefined' && chartData[chartId]["showGT"]!=='' && chartData[chartId]["showGT"]==='Y'){   
        htmlstr += "<tfoot>";
          htmlstr += "<tr style='background-color:#D1D1D7;color:black'>";
          htmlstr += "<td  ><strong>Grand Total</strong></td>";
        // for calculating Gt
        htmlstr = htmlstr + "<td  style='background-color:" + color + ";text-align:center;' >" +addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(KPIresult[0])+ "</td>";
          for (var no = 1; no < measureArray.length; no++) {
//        var sum1=0;
      
//        if (measures[no].indexOf(specialCharacter)!=-1 || measures[no].indexOf("Percent")!=-1 ||measures[no].indexOf("percent")!=-1 ||measures[no].indexOf("PERCENT")!=-1) {
//                sum1 =   parseFloat(sum1/(data[chartId].length))
//                 }
//        sum1 = sum1.toFixed(2);
//        sum1.replace(".00", "");
//        if(ch/artData[chartId]["aggregation"][no]=='avg' || chartData[chartId]["aggregation"][no]=='AVG'){
//        sum1/=rowCount;
//          }
         htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >" +addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(KPIresult[no])+ "</td>";
          }
            htmlstr += "</tr>";
            htmlstr += "</tfoot>"
        }
        
            

htmlstr += "</table></div></div>";
        }
//htmlstr += "</div>";
  if(chartIdTile==div+"Tile" ){
        $("#openTableTile").html(htmlstr);
    }else if($("#chartType").val()=="India-Map-Dashboard"){
     $("#part3").html(htmlstr);
    }
    else{
        $("#" + div).html(htmlstr);
    }
//chartTableBar1

var table = $('#chartTableBar1'+div).dataTable( {
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
}


function bubbleCentric(div, data0, columns, measures, divWidth, divHeight, layout) {
     var color = d3.scale.category10();
//     divWidth=parseFloat($(window).width())*(.35);
      var fromoneview=parent.$("#fromoneview").val()
        var widthdr=divWidth
    var divHgtdr=divHeight;
    var chartData = JSON.parse(parent.$("#chartData").val());
       if(fromoneview!='null'&& fromoneview=='true'){
divWidth=divWidth;
//divHeight=divHeight+50;
      }
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
}
else
{
    legendAlign='Bottom';
}
}
    var nodes = [];
    var data = [];
    var dashletid;
    var measure1 = measures[0];
    var chartData = JSON.parse(parent.$("#chartData").val());
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
    var bubble_layout = d3.layout.pack()
            .sort(null) // HERE
            .size([divWidth - layout, divHeight - layout]) //-100
            .padding(1);
    var svg = d3.select("#" + div)
            .append("svg")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(divWidth - layout)+" "+(divHeight  - 50 )+" ")
            .classed("svg-content-responsive", true)

 if(fromoneview!='null'&& fromoneview=='true'){
     div=div1;
 }
    parent.$("#colorMap").val(JSON.stringify(colorMap));
//    svg.append("svg:rect")
    var selection = svg.selectAll("g.node")
            .data(bubble_layout.nodes({
                children: data
            }).filter(function(d) {
                return !d.children;
            }));

    var node = selection.enter().append("g")
            .attr("class", "node")
            .attr("transform", function(d) {
                var top = 0;
                var xvlaue =0;
                if(legendAlign==='Right'){
                  top = divHeight *.45;
                 xvlaue =divWidth *.50;
                }else if(legendAlign==='Bottom'){
                    top = divHeight *.45;
                 xvlaue =divWidth *.50;
                }else{
                    
                 top = divHeight *.45;
                 xvlaue =divWidth *.50;
                }
//                return "translate(" + d.x + ", " + top + ")";
                return "translate(" + xvlaue + ", " + top + ")";
            }).filter(function(d) {
        return d.value;
    });
    node.append("circle")
            .attr("r", function(d) {

                if(data.length==1){
                    return parseFloat(d.r)/2;
                }else {
                   if(legendAlign==='Right'){
                    return d.r *1.83;
                }else if(legendAlign==='Bottom'){
                      return d.r *1.83;
                }else{
                    
                   return d.r *1.83;
                } 
              
                }

            })
             .attr("fill", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getDrawColor(div, parseInt(i));
            })
             .attr("color_value", function(d, i) {
                 try {
                   var drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                } catch (e) {
                }
                 if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d.name) !== -1) {
                        return drillShade;
                    }
                        return getDrawColor(div, parseInt(i));
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
                    }
                    else {
                        if (no === 0) {
                            msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(d.value);
                        } else {
                            msrData = addCurrencyType(div, getMeasureId(measures[no])) + addCommas(d["value" + no]);
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

    node.append("text")
            .attr("text-anchor", "middle")
            .attr("dy", ".3em")
            .attr("font-family", "Verdana")
            .attr("font-size", "10pt")
            .attr("fill",  function(d, i){
               if (typeof chartData[div]["lableColor"]!=="undefined" && chartData[div]["lableColor"] !== "default") {
                              lableColor = "#FFFFFF";
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
            .text(function(d) {
                if (parent.$("#chartType").val() === "dashboard" || parent.$("#dashBoardType").val() === "drilldash" && typeof drillStates !== "undefined" && drillStates !== "") {
                    return d.name.substring(0, d.r / 5);
                }
                else {
                    return d.name1.substring(0, d.r / 5);
                }
            });

//
// var height = Math.min(divWidth, divHeight);
//    var width = Math.min(divWidth, divHeight);
//var displayLength = chartData[div]["displayLegends"];
//
//                var yvalue=0;
//		var rectyvalue=0;
//		var rectyvalue1=0;
//		var len = parseInt(width-150);
//		var rectlen = parseInt(width-200);
//		var fontsize = parseInt(width/45);
//		var fontsize1 = parseInt(width/50);
//		var rectsize = parseInt(width/60);
//                var legendLength;
//                var data = data0;
//                //alert(chartData[div]["legendNo"]);
//                if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
//                    legendLength=chartData[div]["legendNo"];
//                   // alert('if');
//                    
//                }
//                else{
//                   // alert("else");
//                   // alert(legendLength);
//                    legendLength=(data.length<15 ? data.length : 15); 
//                }
////                alert(legendLength);
//                if(fontsize1>15){
//                  fontsize1 = 15;
//                }else if(fontsize1<7){
//                  fontsize1 = 7;
//                }
//        if(legendAlign==='Right')
//        {
//	yvalue = parseInt(height / 4);
//
//        rectyvalue = parseInt((height / 4)-10);
//        }
//        else
//        {
//            if(width<height)
//            {
//                yvalue = parseInt(height*0.9);
//                rectyvalue = parseInt((height*0.89 ));
//            }
//            else
//            {
//                yvalue = parseInt(height*1.05);
//                rectyvalue = parseInt((height*1.030 ));
//            }
//        }
//
//        $("#txt"+div).css("font-size",fontsize1+"px");
//        $("#lbl"+div).css("font-size",fontsize1+"px");
//
//
//        var count = 0;
//        var transform = "";
// if(typeof displayLength!="undefined" && displayLength!=""&& displayLength!="Yes"){}
// else
//     {
//
//if((typeof displayLength==="undefined") ||(typeof displayLength!="undefined" && displayLength!="None")){
//            if(legendAlign==='Right')
//                {
//
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
//            });
//       
//                            for(var i=0;i<legendLength ;i++){
//                     if(data[i][measures[0]]>0){
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
//            .attr("fill", function(){
//			var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measures,i,color)
//                return colorfill;
//			})
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
//            .attr("fill", function(){
//
//             if(typeof chartData[div]["colorLegend"]!="undefined" && chartData[div]["colorLegend"]!="" ){
//              if(chartData[div]["colorLegend"]=="Black") {
//                  return "#000";
//              } else{
//                  return  getDrawColor(div, parseInt(i));
//              }
//             }else{
//                 return  "#000";
//             }
//            })
//            .attr("style", "font-size:"+fontsize1+"px")
////            .style("stroke", color(i))
//        //    .attr("dy",dyvalue )
//            .attr("id",function(d){
//                return data[i][columns[0]];
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
//            setMouseOverEvent(this.id,div)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id,div)
//                    })
//              count++
//               }
//            }
//}
//                else
//                   {
//                   var widthvalue = parseInt((width *.1));
//            var widthRectvalue = parseInt((width *.1)-(width*.018));
//                       for(var i=0;i<legendLength ;i++){
//                     if(data[i][measures[0]]>0){
//                 var charactercount = data[i][columns[0]].length;
//              if(i!=0){
//
//               widthvalue = parseInt(widthvalue +  (width*.18) )
//               widthRectvalue = parseInt(widthRectvalue + (width*.18) )
//               if(count%5==0){
//        widthvalue = parseInt((width *.1));
//        widthRectvalue = parseInt((width *.1)-(width*.018));
//             if(width<height){
////                  yvalue = parseInt(height*0.9);
////            rectyvalue = parseInt((height*0.85 ));
//              yvalue = parseInt(yvalue-width*.06)
//         rectyvalue = parseInt(rectyvalue-width*.06)
//             }else{
//                yvalue = parseInt(yvalue-height*.06)
//         rectyvalue = parseInt(rectyvalue-height*.06)
//             }
//
//     }
//    }
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("rect")
//          //  .attr("style","margin-right:10")
//         //   .attr("transform", transform)
//            .attr("style", "overflow:scroll")
//
////            .attr("x",rectlen)
////            .attr("y",rectyvalue)
//            .attr("transform", "translate(" + widthRectvalue  + "," + rectyvalue + ")")
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
//            .attr("transform", "translate(" + widthvalue  + "," + yvalue + ")")
//            .attr("fill", function(){
//
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
//            .attr("style", "font-size:"+fontsize1+"px")
//
//            .attr("id",function(d){
//                return d[i][columns[0]];
//            } )
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//                if(data[i][columns[0]].length>9){
//                    return data[i][columns[0]].substring(0, 9)+"..";
//                }else {
//                    return data[i][columns[0]];
//          }
//           })
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id,div)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id,div)
//                    })
//              count++
//               }
//            }
//                   }
//}
//            if(typeof displayLength!="undefined" && displayLength!="None" && displayLength!="All"){
//               for(var i=0;i<displayLength;i++){
//                     if(data[i][measures[0]]>0){
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
//            .attr("fill", getDrawColor(div, parseInt(i)))
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
//            .attr("fill", getDrawColor(div, parseInt(i)))
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
//         //   .style("font-size",""+fontsize+"")
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id)
//                    })
//              count++
//               }}
//            }else if(typeof displayLength!="undefined" && displayLength=="All"){
//               for(var i in  data){
//                      if(data[i][measures[0]]>0){
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
//            .attr("fill", getDrawColor(div, parseInt(i)))
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
//            .attr("fill", getDrawColor(div, parseInt(i)))
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
//         //   .style("font-size",""+fontsize+"")
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id)
//                    })
//              count++
//            }}
//            }else if(typeof displayLength!="undefined" && displayLength=="None"){
//
//            }else{
//
//}
//     }

}

function buildBarAdvanceDB(div,divId, data, columns, measureArray,wid,hgt) {
 var customTicks = 5;
var color = d3.scale.category10();
var divWid;
if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
}
//    alert($(window).width());
divWid=parseFloat($(window).width())*(.76);
wid=divWid;
//}
var chartData = JSON.parse(parent.$("#chartData").val());

var div1=parent.$("#chartname").val()
   var divHgt=500;
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

    var fun = "drillWithinchart(this.id,\""+div+"\")";
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
    }else {
         width = divWid; //- margin.left - margin.right
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
            .attr("height", height + margin.top + margin.bottom +40 )
            .style("float","right")
            .style("margin-right","20")
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
    }else{
           minVal=0;
       }
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
   // minVal=0;
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
    svg.append("g")
        .attr("class", "grid11")
        .call(make_y_axis()
            .tickSize(-width, 0, 0)
            .tickFormat("")
        )
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
                            colorShad = color(i);

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

function buildAdvanceLineDB(div,div11, data, columns, measureArray, divWid, divHgt) {
//alert(JSON.stringify(data));
var color = d3.scale.category10();
 var measArr = [];
 if($("#chartType").val()=="Pie-Dashboard"){
      sideIconTab();
}
    var measure1 = measureArray[0];
    var fromoneview=parent.$("#fromoneview").val();
    var widthdr=divWid
    divWid=parseFloat($(window).width())*0.7;
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
        left: 00
    };
    width = parseFloat(divWid)+200, //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .78;
}else{
     margin = {
        top: 20,
        right: 00,
        bottom: botom,
        left: 80
    };
    width = parseFloat(divWid), //- margin.left - margin.right
//            height = parseFloat(divHgt)* .65;
            height = parseFloat(divHgt)* .75;
    }
    
    var x = d3.scale.ordinal().rangePoints([0, width], .2);
    var max = maximumValue(data, measure1);
    var minVal = minimumValue(data, measure1);



    var y = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
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
var divWid1=parseFloat($(window).width())*0.74;
//alert(JSON.stringify(div))
//alert(JSON.stringify(div1))
 var   svg = d3.select("#" + div11)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("id", "svg_" + div11)
//            .attr("viewBox", "0 0 "+(width + margin.left + margin.right)+" "+(divHgt)+" ")
//            .classed("svg-content-responsive", true)
            .attr("width", divWid1 + margin.left + margin.right)
            .attr("height", height + margin.top + margin.bottom+17.5)
            .style("float","right")
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

     svg.append("g")
        .append("svg:svg")
//    .style("margin-left", "3em")
     .attr("width", divWid1-margin.left-margin.right)
     .attr("height", height-margin.left-margin.right);
           
    svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")");
//            .style("stroke-dasharray", ("3,3"))
//            .call(xAxis);
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
     if (data.length > 1) {
        minVal = minimumValue(data, measure1) * .8;
    }else{
       minVal=0;
}

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
           flag =  measure1;
           }
          return flag
             });}
             
if(typeof displayX !=="undefined" && displayX == "Yes"){
    d3.transition(svg).select('.x.axis')
            .call(xAxis)
            .selectAll('text')
            .style('text-anchor', 'end')
            .text(function(d,i) {
                return buildXaxisFilter(div,d,i)
//                if(typeof displayX !=="undefined" && displayX == "Yes"){
//                if (d.length < 14)
//                    return d;
//                else
//                    return d.substring(0, 13) + "..";
//            }else {
//                return "";
//            }
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
}   // end for combo chart by mynk sh.

function buildTopBottomHorizotal(div, data, columns, measureArray, divWid, divHgt){
    var data1 = [];
    for(var h=0; h<(data.length < 5 ? data.length : 5); h++){
        data1.push(data[h]);
    }
    var data2 = [];
    for(var h=5; h<(data.length < 10 ? data.length : 10); h++){
        data2.push(data[h]);
    }
    var fromoneview=parent.$("#fromoneview").val();
    var dashletid;
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

    var chartMap = {};
    var chartData = JSON.parse(parent.$("#chartData").val());
    var offset=0;
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"]=='top'){
        offset=20;
        divHgt-=20;
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
        }else{
            drillWithinchart(id1,div);
        }
    }
    var wid = divWid-10;
    var hgt = divHgt;
    var w = wid ;
    var h = hgt - 70;
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
    var max = maximumValue(data, measureArray[0]),
    
    num_ticks = 1,
    left_margin = 100,
    right_margin = 100,
    top_margin = 30,
    bottom_margin = 0;
    var minValue = minimumValue(data, measureArray[0]);
    var minValue1 = minimumValue(data1, measureArray[0]);
    var minValue2 = minimumValue(data2, measureArray[0]);
    var max1 = maximumValue(data1, measureArray[0]);
    var max2 = maximumValue(data2, measureArray[0]);
    var measArr = [];
    
    var x = d3.scale.linear()
    .domain([0, max])
    .range([0,w*.50]),
    
    y = d3.scale.ordinal()
    .domain(d3.range(data.length))
    .rangeBands([offset, h+top_margin-10+offset], .4);
            
    var x1 = d3.scale.linear()
    .domain([0, max1])
    .range([0, w*.47]); 
            
    var x2 = d3.scale.linear()
    .domain([max1 ,minValue2 ])
    .range([0,w*.47]);  
            
    var xAxis = d3.svg.trendaxis()
    .scale(x)
    .orient("bottom");
    var xAxis1 = d3.svg.trendaxis()
    .scale(x1)
    .orient("bottom");
    var xAxis2 = d3.svg.trendaxis()
    .scale(x2)
    .orient("bottom");
            
    var chart_top = h - y.rangeBand() / 2 ;
    var chart_bottom = bottom_margin + y.rangeBand() / 2;
    var chart_left = left_margin;
    var vis = d3.select("#"+div)
    .append("svg:svg")
    .attr("id","svg_"+div)
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt+20)+" ")
    .classed("svg-content-responsive", true)
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
        })
    .attr("stop-opacity", 1);


    if(fromoneview!='null'&& fromoneview=='true'){}else{
        parent.$("#colorMap").val(JSON.stringify(colorMap));
    }
    gradient.append("svg:stop")
    .attr("offset", "9%")
    .attr("stop-color", "rgb(240,240,240)")
    .attr("stop-opacity", 1);
    gradient.append("svg:stop")
    .attr("offset", "80%")
    .attr("stop-color", function(d, i) {
        })
    .attr("stop-opacity", 1);
    if(fromoneview!='null'&& fromoneview=='true'){
    }else{
        parent.$("#colorMap").val(JSON.stringify(colorMap));
    }
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
    
    var bars = vis.selectAll("g.bar")   //  bar add by mayank sharma
    .data(data)
    .enter()
    .append("svg:g")
    .attr("class", "bar")
    .attr("transform", function(d, i) {
         if(i>=5){
        return "translate("+(wid*.50)+", " + y(i) + ")";
         }else{
        return "translate(0, " + y(i) + ")";
         }
    });
           
    bars.append("svg:rect")
    .transition()
    .duration(1000)//1 second
    .attr("width", function(d,i) {
        if(i>=5){
            if(i>=5 && i<=6){
            return (x(d[measureArray[0]]))+wid*.40;
       }else{
            return (x(d[measureArray[0]]))+wid*.43;
            }
       }else{
        return (x(d[measureArray[0]]));
       }
    })
    .attr("x", function(d,i) {
        if(i>=5){
            if(i>=5 && i<=6){
            return  -(x(d[measureArray[0]]))-wid*.40;
        }else{
                 return  -(x(d[measureArray[0]]))-wid*.43;
        }
        }else{
            return wid*.5;
        }
    })
    .attr("height", y.rangeBand())
    .attr("fill", function(d,i){
        var drilledvalue;
        var targetLine;
        try{
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        } catch (e) {
        }
        if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
            return drillShade;
        }
        if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]==='N'){
            if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                targetLine = chartData[div]["targetLine"];
                return targetColor(d,measureArray[0],targetLine);
            }else{
                targetLine = "";
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            }
        }else{
            if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                targetLine = chartData[div]["targetLine"];
                return targetColor(d,measureArray[0],targetLine);
            }else{
                targetLine = "";
                return color(0);
            }
            var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
            return colorfill;
        }
    })
    .attr("color_value", function(d,i){
        var drilledvalue;
        var targetLine;
        try{
            drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
        }catch(e){
        }
        if(typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
            return drillShade;
        }
        if(typeof chartData[div]["transpose"] === "undefined" || chartData[div]["transpose"]=='N'){
            if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                targetLine = chartData[div]["targetLine"];
                return targetColor(d,measureArray[0],targetLine);
            }else{
                targetLine = "";
                var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
            }
        }else{
            if(typeof chartData[div]["targetLine"] !=='undefined' && chartData[div]["targetLine"] !==""){
                targetLine = chartData[div]["targetLine"];
                return targetColor(d,measureArray[0],targetLine);
            }else{
                targetLine = "";
                return color(0);
            }
            return color(0);
        }
    })
    .attr("stroke", function(d){})
    .attr("index_value", function(d, i){
        return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
    .attr("id", function(d) {
        return d[columns[0]] + ":" + d[measureArray[0]];
    })
    .attr("class", function(d, i) {
        return "bars-Bubble-index-" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '')+div;
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
        if(fromoneview!='null'&& fromoneview=='true'){}else{
            var bar = d3.select(this);
            var indexValue = bar.attr("index_value");
            var barSelector = "." + "bars-Bubble-" + "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
            var selectedBar = d3.selectAll(barSelector);
            var colorValue = selectedBar.attr("color_value");
            selectedBar.style("fill", colorValue);
        }
        hide_details(d, i, this);
    })
    .dblTap(function(e,id) {
        drillFunct(id);
    });
            
    var labels = vis.selectAll("g.bar")   // legend on bar add by mayank sharma
    .append("svg:text")
    .attr("class", "x.axis")
    .text(function(d, i) {
        if(typeof displayY !=="undefined" && displayY =="Yes"){
            if (data[i][columns[0]].length < 1){
                return data[i][columns[0]];
            }else{
            if (typeof chartData[div]["editXLable"] !== "undefined" && chartData[div]["editXLable"] !== "") {
                return  data[i][columns[0]].substring(0,  chartData[div]["editXLable"]);
            }else{
                if(data[i][columns[0]].length>20){
                    return  data[i][columns[0]].substring(0, 20) + "..";
                }else{
                    return  data[i][columns[0]];
                }
            }}
        }else{
            return "";
        }
    })
    .attr("style", "font-family: lucida grande")
    .style('font-size',function(d,i) {
       if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
                   return (chartData[div]["legendFontSize"]+"px");
                }else{
                   return "9px";  
                }
            })
    .attr("x", function(d,i) {
        if(i>=5){
           return -(x(d[measureArray[0]]))-wid*.43;
        }else{
           return wid*.32;
        }
    })
    .attr('y', function(d,i){
        if(i>=5){
            return 13;
        }else{
            return 12;
        }
    })
    .attr('transform', function(d,i) {
       var val1=(x(d[measureArray[0]]))+wid*.50;
        if(i>=5){
            return "translate("+val1+" ,0) rotate(0)";
        }else{
            return 'translate(0,0) rotate(0)';
        }
    });  // legend on bar end by mayank sharma
            
    var bbox = labels.node().getBBox();
    vis.selectAll(".label")
    .attr("transform", function(d) {
        return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 3) + ")";
    });
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });


    labels = vis.selectAll("g.bar")  // labels on bar add by mayank sharma
    .append("svg:text")
    .attr("x", function(d,i) {
        if(i>=5){
            if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Center"||chartData[div]["LabelPos"]==="Bottom-Bar" ||chartData[div]["LabelPos"]==="Bottom")){
                return -(x(d[measureArray[0]]))-wid*.43;
            }else{
                return 0;
            }
        }else{
            if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Bottom-Bar" ||chartData[div]["LabelPos"]==="Bottom")){
                return wid*.52;
            }else if(chartData[div]["LabelPos"]==="Center"){
                return wid*.5;
            }else{
                return wid*.5;
            }
        }
    })
    .attr("transform", function(d,i) {
        var xvalue ="";
        if(i>=5){
            if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Bottom")){
                xvalue =  (x(d[measureArray[0]]))+wid*.41;
            }else if(chartData[div]["LabelPos"]==="Bottom-Bar"){
                xvalue = (x(d[measureArray[0]]))+wid*.46;
            }else if(chartData[div]["LabelPos"]==="Center"){
                xvalue = (x(d[measureArray[0]])+wid*.41)/2;
            }else{
                xvalue = -(x(d[measureArray[0]]))-wid*.41;
            }  
        }else{
            if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Bottom")){
                xvalue = 0;
            }else if(chartData[div]["LabelPos"]==="Bottom-Bar"){
                xvalue = -40;
            }else if(chartData[div]["LabelPos"]==="Center"){
                xvalue =(x(d[measureArray[0]])/2);
            }else{
                xvalue = x(d[measureArray[0]])-20;
            }   
        }
        return "translate(" + xvalue + ", " + (y.rangeBand() / 2 + bbox.height / 3) + ")";
    })
    .attr("text-anchor", "middle")
    .attr("class", "valueLabel")
    .attr("fill", function(){
        var lableColor="";
        if (typeof chartData[div]["labelColor"]!=="undefined") {
            lableColor = chartData[div]["labelColor"];
        }else {
            lableColor = "#000000";
        }
        return lableColor;
    })
     .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
               return (chartData[div]["labelFSize"]+"px");
          }else{
              return "9px";
              }
            })
    .text(function(d){
        var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
        if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
            if(typeof displayType=="undefined" || displayType==="Absolute"){
                return numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div);
            }else{
                return percentage.toFixed(1) + "%";
            }
        }else{
            return "";
        }
    });   // labels on bar add by mayank sharma


    bbox = labels.node().getBBox();
    vis.selectAll(".NetMarginAmt")
    .attr("transform", function(d){
        return "translate(0, " + (y.rangeBand() / 2 + bbox.height / 2) + ")";
    });
            

if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}else{    
        var count=0;  
        var boxW=divWid;
        var boxH=divHgt*.9;
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
            rectW=30+(len*7)+85;
        }else{
            rectW=(measureName.length+columns[0].length)*7+130;
        }
        }
        rectW = rectW<170?170:rectW;
        var viewByHgt=15;
    var rectH=0;  
    if(typeof chartData[div]["lbPosition"]=='undefined' || chartData[div]["lbPosition"] === "top"){
            rectH=17;
    }else{ 
        if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
            rectH=17;
        }else{
            rectH=10+17+viewByHgt;
        }
    }
    
        var rectX;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
        rectX=50;
    }else if (chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "bottomright"){
        rectX=divWid*.75;
    }else if(chartData[div]["lbPosition"] === "topleft" || chartData[div]["lbPosition"] === "bottomleft"){
        rectX=10;
    }else if(chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "bottomcenter"){
            rectX=boxW/2-rectW/2;
        }
    
        var rectY;
    if(typeof chartData[div]["lbPosition"]==='undefined' ||chartData[div]["lbPosition"] === "top"){
        rectY=boxH-(boxH*1.03)+30;
    }else if(chartData[div]["lbPosition"] === 'topright' || chartData[div]["lbPosition"] === "topcenter" || chartData[div]["lbPosition"] === "topleft"){
            rectY=boxH-(boxH*1.03)+15;
    }else if(chartData[div]["lbPosition"] === 'bottomleft' || chartData[div]["lbPosition"] === 'bottomcenter' || chartData[div]["lbPosition"] === "bottomright" ){
            rectY=boxH-rectH-(boxH*0.2);
    }else{
            rectY=10;
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
            vis.append("g")
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
         if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}else{
            vis.append("g")
            .attr("id", "viewBylbl")
            .append("text")
            .attr("x",rectX+10)
            .attr("style","font-size:10px")
            .attr("y",(rectY+13+count*15))
            .attr("fill", 'black')
            .text(columns[0]);
        }
    }else{
             if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){}else{
            vis.append("g")
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
var offset1=0;
var offset2=0;
        if(typeof chartData[div]["lbPosition"]==='undefined' || chartData[div]["lbPosition"] === "top"){
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        offset1=0;
    }else{
            offset1=(columns[0].length*6.5+20);
    }
            offset2=-23;
}else if(typeof chartData[div]["lbPosition"]!=='undefined' && chartData[div]["lbPosition"] !== "top"){
    if(typeof chartData[div]["showViewByinLBox"]==='undefined' || chartData[div]["showViewByinLBox"]==='N'){
        offset2=-23;
    }else{
        offset2=0;
        }
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
            return measureArray[0];
        })
        var rectsize;
        if(divWid<divHgt){// stacked
            rectsize = parseInt(divWid/25);
}else{
            rectsize = parseInt(divHgt/25);
        }
        rectsize=rectsize>10?10:rectsize;
        vis.append("g")
        .append("rect")
        .attr("x",rectX+offset1+10)
        .attr("y",(rectY+12+offset2+viewByHgt+count*15))
        .attr("width", rectsize)
        .attr("height", rectsize)
        .attr("fill", getDrawColor(div, parseInt(0)))
        count++
    }
}

 function buildMultiAxisBarCompare(div, data, columns, measureArray, divWid, divHgt,graphData,axismin,axismax) {
  
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
var regid=div.replace("chart","");
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
var max = axismax;
var minVal = axismin;
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
//if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
//    if(typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="" && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
//    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
//}else{
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
//}}
////end of max
//else{
//
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
//}
//
////end of default max
// if(typeof chartData[div]["yaxisrange"]!="undefined" && chartData[div]["yaxisrange"]!="") {
// if(typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""  && chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" ) {
//  minVal = parseFloat(chartData[div]["yaxisrange"]["axisMin"]);
// }else if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMin"]!="undefined" && chartData[div]["yaxisrange"]["axisMin"]!=""){
//   minVal = 0;
// }else{
//  for(var i in measureArray){
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
//   }
//}
//// end of min
//else{
//
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
//if (data.length > 1) {
//        minVal = minimumValue(data, measure1) * .8;
//    }else{
//        minVal = 0;
//    }
//}

//}
//end of default min
var y0 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);
var y1 = d3.scale.linear().domain([parseFloat(minVal), parseFloat(max)]).range([height, 0]);


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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
                        return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
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
    });
}

//add condition by mayank sh. for display properties
    if(typeof chartData[div]["displayY"]==="undefined" || chartData[div]["displayY"]==="" || chartData[div]["displayY"]=="Yes"){
    svg.append("g")
            .attr("class", "y axis axisLeft")
            .attr("transform", "translate(0,0)")
            .call(yAxisLeft);
        }//end by mynk sh.

 for(var i=0;i<measureArray.length;i++){   // smooth line
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
//alert(graphData.length)
for(var k=0;k<graphData.length;k++){
//    alert(k)
//    alert(JSON.stringify(graphData))
//    alert(JSON.stringify(graphData[k]["comp"+(k+1)]))
    var dataNew = graphData[k]["comp"+(k+1)];
//   if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
//   if(k==0){
//    if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "Yes")) {
//        continue;
//    }}else{
//    if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[k]] === "undefined" || chartData[div]["createBarLine"][measureArray[k]] === "No") {
//        continue;
//    }} //end by mayank sh.
//}
//alert(JSON.stringify(dataNew))
var measNo=graphData.length;

    bars = svg.selectAll(".bar"+k).data(dataNew).enter();
    bars.append("rect")
            .attr("class", "bar")
            .attr("x", function(d) {
                return parseFloat(x(d[columns[0]])+parseFloat(x.rangeBand() / measNo*k));
            })
            .attr("width", x.rangeBand() / measNo)
            .attr("rx", barRadius)
            .attr("id", function(d) {
                return d[columns[0]] + ":" + d[measureArray[0]];
            })
            .attr("onclick", fun)
    
    //Added by shivam
	.dblTap(function(e,id) {
		drillFunct(id);
	}) 
    
            .attr("y", function(d) {
                return y0(d[measureArray[0]]);
            })
            .attr("height", function(d, i, j) {
                return height - y0(d[measureArray[0]]);
            })
            .attr("fill", function(d, i) {

return getDrawColor(div, parseInt(k));
            }).attr("class", function(d, i) {
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
                    return "bars-Bubble-index-" + measureArray[0].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                }
                else{
                return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                }
            }).attr("index_value", function(d, i) {
                if(typeof chartData[div]["transposeMeasure"]!='undefined' && chartData[div]["transposeMeasure"]==='Y'){
                    return "index-" + measureArray[0].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                }
                else{
                return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
                }
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
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
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
            msrData = addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(d[measureArray[i]]);
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
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
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
            var boxW=(divWid+20+rightM);
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
//    var totalCharacters=0;
//    for(var i in measureArray){
////        if(i==0){
////            if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "Yes")) {
////                continue;
////            }
////        }else{
////            if (typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[i]] === "undefined" || chartData[div]["createBarLine"][measureArray[i]] === "No") {
////                continue;
////            }
////        }
//        var measureName='';
//        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
//            measureName=chartData[div]["measureAlias"][measureArray[i]];
//        }else{
//            measureName=checkMeasureNameForGraph(measureArray[i]);
//        }
//        labelsArr.push(measureName);
//        totalCharacters+=measureName.length;
//    }
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
    currX=currX+(labelsArr[i].length*7)+30;
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
    rectH=10+(17*labelsArr.length);
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
   }
}
}
    }
            //Graph Label
            for(var z=0;z<measureArray.length;z++){
//            if(typeof chartData[div]["transposeMeasure"]=='undefined' || chartData[div]["transposeMeasure"]=='N'){
//                if (!(typeof chartData[div]["createBarLine"] === "undefined" || typeof chartData[div]["createBarLine"][measureArray[z]] === "undefined" || chartData[div]["createBarLine"][measureArray[z]] === "Yes")) {
//                    continue;
//                } 
//            }
                 var sum = d3.sum(data, function(d,i) {
                     return d[measureArray[z]];
                    });

             svg.selectAll("labelText")
             .data(data).enter().append("text")
                  .attr("transform", function(d) {
                   var xvalue = x(d[columns[0]]) + parseFloat(x.rangeBand() / measureArray.length*z) + 10 ;// x(d[measureArray[0]]);
        var yValue = (y0(d[measureArray[z]])) < 15 ? y0(d[measureArray[z]]) + 14 : y0(d[measureArray[z]]) -18;
if(typeof chartData[div]["dLabelDisplay"]!="undefined" && typeof chartData[div]["dLabelDisplay"][measureArray[z]]!= "undefined" && chartData[div]["dLabelDisplay"][measureArray[z]] === "OnTop"){
           return "translate(" + xvalue + ", " + (yValue) + ") rotate(270)";   // add by mayank sh. for label display
                     }else{
           return "translate(" + xvalue + ", " + (height*.88) + ") rotate(270)";
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
                         
                           return addCurrencyType(div, getMeasureId(measureArray[z])) + addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
                }else {
                    return "";
                        }
                    }else {
                   return addCurrencyType(div, getMeasureId(measureArray[z])) + addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
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
                            
                            return addCurrencyType(div, getMeasureId(measureArray[z])) + addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));

                        }else {
                            return "";
                        }
                    }else {
                        return addCurrencyType(div, getMeasureId(measureArray[z])) + addCommas(numberFormat(d[measureArray[z]],yAxisFormat,yAxisRounding,div));
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
              .text(addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(avg,yAxisFormat,yAxisRounding,div)))
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
function buildKPIBarChart(chartId, data, columns, measureArray,divwidth,divHght) {  
    chartId = chartId.replace("Tile","");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var div1=parent.$("#chartname").val()
    var fromoneview=parent.$("#fromoneview").val()
    var div;
    var yAxisFormat = "";
    var yAxisRounding = 0;
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
    var filterFlag=false;
    var filters=chartData[chartId]["filters"];
    if(typeof filters==='undefined'){
        filterFlag = false;
    }
    else{
        var filterKeys=Object.keys(filters);
        if(filterKeys.length==0){
            filterFlag=false;
        }
        else{
            filterFlag=true;
        }
    }
    var dimensionId;
    if(fromoneview!='null'&& fromoneview=='true'){
        viewOvName = chartData[div1]["viewBys"];
        viewOvIds = chartData[div1]["viewIds"];
        dimensionId = chartData[div1]["dimensions"];
        div=chartId
        chartId=div1;
        dimensionId = chartData[div1]["dimensions"];
    }else{
        div=chartId
        dimensionId = chartData[chartId]["dimensions"];
    }
    columns = chartData[chartId]["viewBys"];
    measureArray = chartData[chartId]["meassures"];

    var allColumns = [];
    for(var ch in chartData){
        allColumns.push(chartData[ch]["viewBys"][0]);
    }
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var k = chartId.replace("chart","");
    var viewBys = [];
    var divHght1=$("#divchart1").height()-17;
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

    htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;'>";
    htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-condensed' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    htmlstr += "<tr align='left' style='background-color:whitesmoke;color:black;'>";
    htmlstr += "<th width='15%'></th>"
    for(var i=0;i<(filterFlag?data.length:2);i++){
        htmlstr += "<th><b>"+data[i][columns[0]];
        htmlstr += "</b></th>"
    }
    for(var i in measureArray){
        var min=parseInt(data[0][measureArray[i]]),max=parseInt(data[0][measureArray[i]]);
        for(var j=1;j<data.length;j++){
            if(parseInt(data[j][measureArray[i]])<min){
                min=parseInt(data[j][measureArray[i]]);
            }
            if(parseInt(data[j][measureArray[i]])>max){
                max=parseInt(data[j][measureArray[i]]);
            }
        }
        var scale = d3.scale.linear().domain([max, min]).range(["80%", "15%"]);
        
        var measureName='';
        if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
            measureName=chartData[div]["measureAlias"][measureArray[i]];
        }else{
            measureName=measureArray[i];
        }
        htmlstr += "<tr>"
        htmlstr += "<td nowrap align='right'><b>"+measureName;
        htmlstr += "</b></td>"
        for(var j=0;j<(filterFlag?data.length:2);j++){
            var wid = scale(data[j][measureArray[i]]);
            var widPercent=parseInt(wid.substr(0,wid.indexOf('%')));
            if(widPercent<50){
                htmlstr += "<td nowrap height='15px'><canvas id='" + data[j][columns[0]] + "_" + measureArray[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[i] + ":" + numberFormat(data[j][measureArray[i]],yAxisFormat,yAxisRounding,chartId) + "' style='width:"+wid+";height:18px;margin-right:5px;background:" + getDrawColor(chartId,1) + "' onmouseove=''></canvas><span style='font-size:10px;'>" + numberFormat(data[j][measureArray[i]],yAxisFormat,yAxisRounding,chartId) + "</span></td>";
            }
            else{
                htmlstr += "<td height='15px'><table style='width:"+wid+"'><tr><td style='height:18px;background-color:"+getDrawColor(chartId, 1)+"'>" + numberFormat(data[j][measureArray[i]],yAxisFormat,yAxisRounding,chartId) + "</td></tr></table></td>";
            }
        }
        htmlstr += "</tr>"
    }
    htmlstr += "</tr>";
    $("#" + div).html(htmlstr);
}


function buildTableComparison(chartId,dataC, columns, measureArray, divwidth, divHght,KPIresult) {
  var data = {};
  data[chartId] = dataC;
//  data[chartId] = [{"State":"Andhra Pradesh","Net Sales MOM":"-109577","Net Sales MOY":"197979","Net Sales MOM%":"-10.649907036731426","Net Sales MOY%":"27.445813029826226"},{"State":"Assam","Net Sales MOM":"36377","Net Sales MOY":"75053","Net Sales MOM%":"15.566615030425442","Net Sales MOY%":"38.48674426952464"},{"State":"Bihar","Net Sales MOM":"-43837","Net Sales MOY":"41288","Net Sales MOM%":"-20.832699692050337","Net Sales MOY%":"32.95157982106801"},{"State":"Chhattisgarh","Net Sales MOM":"-29038","Net Sales MOY":"-11110","Net Sales MOM%":"-48.98034916083326","Net Sales MOY%":"-26.863650651643013"},{"State":"Delhi","Net Sales MOM":"-95268","Net Sales MOY":"163278","Net Sales MOM%":"-14.044802193671083","Net Sales MOY%":"38.897107694946506"},{"State":"Gujarat","Net Sales MOM":"-111666","Net Sales MOY":"132869","Net Sales MOM%":"-13.75623962116322","Net Sales MOY%":"23.424886242029007"}];
    var chartIdTile = chartId;
     chartId = chartId.replace("Tile","");
     var chartData = JSON.parse(parent.$("#chartData").val());
//  var colIds = chartData[chartId]["viewIds"];
  
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
//measureArray = chartData[chartId]["meassures"];
// measureArray = [];
//    measureArray.push("Net Sales MOM");
//    measureArray.push("Net Sales MOY");
//    measureArray.push("Net Sales MOM%");
//    measureArray.push("Net Sales MOY%");
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
        htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+divHght1+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;'>";
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

          htmlstr += "<td >"+addCurrencyType(chartId, getMeasureId(measureArray[b])) + addCommas(d[measureArray[b]])+"</td>";

     });
        htmlstr +="</strong></td>"
         htmlstr += "</tr>";
        }
         htmlstr += "</table>";
        }//Transpose of Table
        else{

//htmlstr += "<div class='hiddenscrollbar' style = 'height:auto;overflow-y:auto;overflow-x:none;margin-top:20px;float:right;width:100%;'>";
    if(typeof chartData[chartId]["tableBorder"]!=="undefined" && chartData[chartId]["tableBorder"]=="Y"){
        htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-condensed table-bordered' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }else{
//        
   htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-condensed' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
    }      
          
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
htmlstr += "<th ></th>";


for(var q in columns){
//if(columns[0].length > 20){
//htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q].substring(0, 20)+"..</th>";
//}else {
htmlstr += "<th style='font-weight:bold;text-align:left' >"+columns[q]+"</th>";
//}

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
  var colorValue = d3.scale.category10();
//  alert(JSON.stringifycurrdata)
data[chartId].forEach(function(d, i) {
           var drawColor="#A9A9A5";    
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
            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="#A9A9A5" /></svg>';
            htmlstr += "</td>";
            var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              
                   for(var l=0;l<viewBys.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[viewBys[l]]) !== -1) {

                           htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                  
                               htmlstr = htmlstr + "<td id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + color + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
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
                htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:"+valueAlign+";color:black;font-size:smaller\" width=" + trWidth + ">" + numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId) + "</td>";
            }
            else{
            htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:"+valueAlign+";color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(chartId, getMeasureId(measureArray[j])) + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
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
         htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:left\" width=" + trWidth + " >" +addCurrencyType(chartId, getMeasureId(measureArray[no])) + addCommas(KPIresult[no])+ "</td>";
          }
            htmlstr += "</tr>";
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

 }
 
 function buildFilledHorizontal(div,data, columns, measureArray,width,height){
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
    var margin = {top: 5,right: 15,bottom: 10,left: 20};
   }else{
    margin = {top: 5, right: 10, bottom: 10,left: 100 };
   }
   
    if(divWid < 600){
      if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.72], .1, .1);
    }else{
       if((measureArray.length)>1){
    var y = d3.scale.linear()
                .rangeRound([0, (divWid-(50*(measureArray.length))-175)], .1, .1);
        }else{
            var y = d3.scale.linear()
    .rangeRound([0, divWid*.75], .1, .1);
    }     
   }
    }else{
        /*Added by Ashutosh*/
        if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    var y = d3.scale.linear()
    .rangeRound([0, divWid*.75], .1, .1);
        }else{
            if((measureArray.length)>1){
//var y = d3.scale.linear()
//                .rangeRound([0, (divWid/2)-100], .1, .1);
if(divWid<900){
var y = d3.scale.linear()
                .rangeRound([0, (divWid-(100*(measureArray.length)))], .1, .1);
}else{
var y = d3.scale.linear()
                .rangeRound([0, (divWid-(100*(measureArray.length)+75))], .1, .1);    
}
        }else{
            var y = d3.scale.linear()
    .rangeRound([0, divWid*.75], .1, .1);
        }
        }
    }     

    
    
   if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){ 
   var x = d3.scale.ordinal()
    .rangeRoundBands([0, divHgt*.85], .1);
}else{
     x = d3.scale.ordinal()
    .rangeRoundBands([0, divHgt*.85], .1);
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
                    return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
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
    .attr("viewBox", "0 0 "+(divWid )+" "+(divHgt-20)+" ")
    .classed("svg-content-responsive", true)
    .append("svg:g")
    .attr("transform", function(d){
    if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    return "translate(" +  margin.left*.55 + "," + (margin.top+15) + ")";
}else{
    return "translate(" + margin.left*.45 + "," + (margin.top+15) + ")";
    }
    }) ;
//    .attr("transform", "translate(" + margin.left*.55 + "," + (margin.top+15) + ")");
            
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
    
     var max = 0;
     var minVal = 0;
    if(fromoneview!='null'&& fromoneview=='true'){
  max = maximumValue(data, measure);
  if (data.length > 1) {
        minVal = minimumValue(data, measure) * .8;
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
        minVal = minimumValue(data, measure) * .8;
    }}
}else{
          if (data.length > 1) {
        minVal = minimumValue(data, measure) * .8;
    }else{
        minVal = 0;
       }
    }
    }
    y.domain([parseFloat(minVal), parseFloat(max)]);
    
//    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
//        svg.append("g")
//        .attr("class", "grid11")
//        .attr("transform", function(d) {
//            return "translate(23," + (divHgt*.82) + ")"
//        })
//        .call(make_y_axis()
//            .tickSize(-width, 0, 0)
//            .tickFormat("")
//            )
//    }
    
               
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
    if(typeof chartData[div]["displayX"]!="undefined" && chartData[div]["displayX"]!="" && chartData[div]["displayX"]!="Yes"){}else{ 
//            if(typeof displayX !=="undefined" && displayX=="Yes"){
                if(yAxisFormat==""){
                    return addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(d,yAxisFormat,yAxisRounding,div));
                }else{
                    return numberFormat(d,yAxisFormat,yAxisRounding,div);
                }
//            }else{
//                return "";
//            }
            }
        })
//        .attr('transform', 'rotate(-30)')
        .append("svg:title")
        .text(function(d) {
            return d;
        });
      
           
        
    var bars = svg.selectAll(".state")
    .data(data)
    .enter()
    .append("rect")
    .attr("class", "bar")
    .attr("transform", function(d) {
        return "translate(0," + (x(d[columns[0]])-8)+")";
    })
    .attr("x", "-50")
    .attr("y",  "5")
    .attr('width',function(d, i) { 
if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){
    if(divWid < 590){
     return divWid*.74+61;
    }else{
     return divWid*.82+61;
    }
}else{ 
   if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return divWid*.955+57;      
                     }else{
   if(divWid < 590){ 
     return divWid*.85+57;
    }else{
     return divWid*.89+57;
    }  
   }
}
})
   .attr("height", x.rangeBand()*.75)  
   .attr("fill", function(d,i){
                   var fillBackground="";
                      if (typeof chartData[div]["fillBackground"]!=="undefined"){
          fillBackground=chartData[div]["fillBackground"];
        } else{
            fillBackground="#E8E8E8";
        }return fillBackground;
                })
    .attr("id", function(d, i) {
        return d[columns[0]] + ":" + d[measure];
    })
    .attr("index_value", function(d, i) {
        return "index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
    })
//    .attr("color_value", "#E8E8E8")
    .attr("color_value", "#f6f6f6")
    .attr("class", function(d, i) {
        return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi').replace(/[^\w\s]/gi, '');
    });
        
        svg.append("g")
        .attr("class", "x axis")
        .attr("transform", "translate(20,-5)")
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
            return "rotate(0)";
        }
    })
 .attr('x',function(d,i){  // add by mayank sharma
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return -2;
        }else {
            return -2;
        }
    })
    .attr('y',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Horizontal") {
            return 0;
        }else {
            return 0;
        }
    })
        .append("svg:title")
        .text(function(d) {          
            return d;
        });    
        
    var bars = svg.selectAll(".state")
    .data(data)
    .enter()
    .append("rect")
    .attr("class", "bar")
    .attr("transform", function(d) {
        return "translate(0," + (x(d[columns[0]])-8)+")";
    })
    .attr("x", "23")
    .attr("y",  "5")
    .attr('width', function (d) {
    if(divWid < 590){
      return (y(d[measure]*.90));
    }else{
          return y(d[measure]);
    }     
})
    .attr("height", x.rangeBand()*.75)  
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
       return d[measureArray[1]];
    });
    
    // add by mayank sharma for show table     
 if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{   
 for(var i=1;i<measureArray.length;i++){  //alert("measureArray"+measureArray[i])
 svg.append("text")
            .attr("id", "viewBylbl")
               .attr("class", "valueLabel")
              .attr("text-anchor", "middle")
              .attr("font-weight", "bold")
            .attr("transform", function(d) {
                    var yValue = "-5";
                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return "translate("+divWid*.90+", " + (yValue) + ")";      
                     }else{
//                         alert("divWid2 : "+divWid);
//                         alert("divWid3 : "+divWid*.50);
                   //Ashutosh
                          if(divWid < 600){
//                           if(measureArray.length>2){
                             
                             if(measureArray.length>2 && i<(measureArray.length-1)){
//                               if(i===1){
//                          return "translate("+(((divWid*.80)/(measureArray.length-i))+(100))+", " + (yValue) + ")"; 
//                              }else{
//                                  return "translate("+(((divWid*.80)/(measureArray.length-i))+(100+(i*25)))+", " + (yValue) + ")"; 
//                              }
//                             }else{
//                                  return "translate("+(((divWid*.80)/(measureArray.length-i))+100)+", " + (yValue) + ")"; 
//                                  return "translate("+((divWid*.80)-(100*(measureArray.length-i))+ 100)+", " + (yValue) + ")"; 
                                  return "translate("+((divWid)-(100*(measureArray.length-i)))+", " + (yValue) + ")"; 
//                             }
                     }else{//alert("this 2:"+divWid*.80)
                     return "translate("+divWid*.80+", " + (yValue) + ")";      
                     }
                     }else{
                              if(measureArray.length>2 && i<(measureArray.length-1)){
                              
//                              if(i===1){
//                          return "translate("+((divWid*.845)-((divWid*.10)*(measureArray.length-i)))+", " + (yValue) + ")"; 
//                              }else{
//                                  alert("this 3"+(((divWid*.845)/(measureArray.length-i))+((divWid*.25)+(i*25))))
//                                  return "translate("+(((divWid*.845)/(measureArray.length-i))+((divWid*.30)+(i*25)))+", " + (yValue) + ")"; 
//                                  alert(((divWid*.845)-((divWid*.10)*i)))
//alert("this ios: "+divWid*.10)
                                  return "translate("+((divWid*.845)-(100*(measureArray.length-i))+ 110)+", " + (yValue) + ")"; 
//                              }
                     }else{
//                         alert("this 4 "+divWid*.845)
                        return "translate("+divWid*.845+", " + (yValue) + ")"; 
                     }
                     }
                     }
                })
            .attr("fill", "#544F4F")
            .text(function(d){
                if(divWid > 670){
                   return measureArray[i]; 
                    }else{
                if( measureArray[i] < 18){
                 return measureArray[i];
                } else{
                return measureArray[i].substring(0, 18) + "..";
                }
                    }
            });
 }
 }
 
 if(typeof chartData[div]["circularChartTab"]==="undefined" || chartData[div]["circularChartTab"]==="No"){}else{  
  for(var a=1;a<measureArray.length;a++){ //alert("measureArray1111 "+a);
 bar=svg.selectAll("rect1")
          .data(data)
          .enter()
                .append("text")
                .attr("transform", function(d) {
                    var yValue =  (x(d[columns[0]])+(x.rangeBand()-7)/2);
                    var xvalue= (y(d[measure])) < 430 ? y(d[measure]) + 40 : y(d[measure]) + 40; 
                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return "translate("+divWid*.90+", " + (yValue) + ")";      
                     }else{
                          if(divWid < 600){
//                           if(measureArray.length>2){
                             //alert("this ")
                             if(measureArray.length>2 && a<(measureArray.length-1)){
//                               if(a===1){
//                          return "translate("+(((divWid*.80)/(measureArray.length-a))+(100))+", " + (yValue) + ")"; 
//                              }else{
//                                  return "translate("+(((divWid*.80)/(measureArray.length-a))+(100+(a*25)))+", " + (yValue) + ")"; 
//                              }
//                             }else{
//                                  return "translate("+((divWid*.80)-(100*(measureArray.length-a))+ 100)+", " + (yValue) + ")"; 
                                  return "translate("+((divWid)-(100*(measureArray.length-a)))+", " + (yValue) + ")"; 
//                             }
                     }else{
                     return "translate("+divWid*.80+", " + (yValue) + ")";      
                     }
                     }else{
                         if(measureArray.length>2 && a<(measureArray.length-1)){
//                            
//                              if(a===1){
//                          return "translate("+(((divWid*.855)/(measureArray.length-a))+(100))+", " + (yValue) + ")"; 
//                              }else{
//                              }
                                  return "translate("+((divWid*.855)-(100*(measureArray.length-a))+ 100)+", " + (yValue) + ")"; 
                     }else{
                        return "translate("+divWid*.855+", " + (yValue) + ")"; 
                     }
                        //return "translate("+divWid*.855+", " + (yValue) + ")"; 
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
//               for(var c in measureArray){
               if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[1]]!='undefined' && chartData[div]["LabFtColor"][measureArray[1]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[1]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
                    return LabFtColor;   
               })
                .text(function(d){ 
                   
//var a=1;
                    var noOfRecords=data.length;
                    var percentage = (d[measureArray[a]] / parseFloat(sum)) * 100;
                      if (!( noOfRecords <= 35)) {
                          return "";
                      }else{
 if (typeof chartData[div]["dataLabelType"] === "undefined" || typeof chartData[div]["dataLabelType"][measureArray[a]] === "undefined" || chartData[div]["dataLabelType"][measureArray[a]] === "%wise") {
       return percentage.toFixed(1) + "%";
  }else{
                       return numberFormat(d[measureArray[a]],yAxisFormat,yAxisRounding,div);
                      }
//                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//                   if(typeof displayType=="undefined" || displayType==="Absolute"){
//                    }else{
//                    var percentage = (d[measureArray[0]] / parseFloat(sum)) * 100;
//                    return percentage.toFixed(1) + "%";
//                }
//                }else {
//                return "";
                }
               });
  }
             }// end by mayank sharma for show table
    
     var sum1 = d3.sum(data, function(d) {
       return d[measureArray[0]];
    });
    if (x.rangeBand() >= 15) {    // add  for labels by mayank sh.
         bar=svg.selectAll("rect1")
          .data(data)
          .enter()
                .append("text")
                .attr("transform", function(d) {
                   var yValue = (x(d[columns[0]])+(x.rangeBand()-7)/2);
                    var xvalue= (y(d[measure])) < 430 ? y(d[measure])+40 : y(d[measure])+40; 
                   
                      if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Center"){
                          return "translate(" + (xvalue/2) + ", " + (yValue) + ")";
                   }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom"){
                     return "translate(10, " + (yValue) + ")";      
                  }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Bottom-Bar"){
                     return "translate(40, " + (yValue) + ")";      
                 }else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="OnRight-Bar"){
                    return "translate(" + y(d[measure]) + ", " + (yValue) + ")";
                     }
                   else if(typeof chartData[div]["LabelPos"]!=="undefined" && chartData[div]["LabelPos"]!=="" && chartData[div]["LabelPos"]==="Top-Bar"){
                     
                     if(typeof chartData[div]["displayY"]!="undefined" && chartData[div]["displayY"]!="" && chartData[div]["displayY"]!="Yes"){ 
                     return "translate("+(divWid*.91)+", " + (yValue) + ")";      
                     }else{
                     if(divWid < 590){
                       return "translate("+(divWid*.82)+", " + (yValue) + ")";
                     }else{
                         return "translate("+(divWid*.89)+", " + (yValue) + ")"; 
                     } 
                     }
                     }else if(typeof chartData[div]["LabelPos"]==="undefined" || chartData[div]["LabelPos"]==="Top"){
                   if(divWid < 590){
                   return "translate(" + (y(d[measure]*.90)+40) + ", " + (yValue) + ")";
      }else{
                   return "translate(" + (y(d[measure])+40) + ", " + (yValue) + ")";
                     }
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
//        for(var c in measureArray){
            if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[0]]!='undefined' && chartData[div]["LabFtColor"][measureArray[0]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[0]]                   
                    }else {
                        LabFtColor = "#000000";
                        }
            return LabFtColor;
//        }
                })
                .text(function(d)
                {
                    var noOfRecords=data.length;
                    var percentage = (d[measureArray[0]] / parseFloat(sum1)) * 100;
                   if (!( noOfRecords <= 35)){
                      return "";  
                    }else{
   if(typeof chartData[div]["LabelPos"]!=="undefined" && (chartData[div]["LabelPos"]==="Center"||chartData[div]["LabelPos"]==="Bottom-Bar" ||chartData[div]["LabelPos"]==="OnRight-Bar")){
       if (( percentage <= 5.0)){
           return "";
       }
   }                      
                    if(typeof dataDisplay!=="undefined" && dataDisplay==="Yes"){
//                   if(typeof displayType=="undefined" || displayType==="Absolute"){
 if (typeof chartData[div]["dataLabelType"] === "undefined" || typeof chartData[div]["dataLabelType"][measureArray[0]] === "undefined" || chartData[div]["dataLabelType"][measureArray[0]] === "Absolute") {
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
    }
    
    
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
  .attr("class", "valueLabel")
                .attr("transform", function(d,i) {
                   var yValue = (x(d[columns[0]])+(x.rangeBand()-7)/2);
//                   var yValue = (x(d[columns[0]])+(x.rangeBand()-13));
                     return "translate(30, " + (yValue) + ")";
                })
                .attr("style", "font-size:"+font+"px; transform-origin: bottom left 0px;")
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
            AxisNameColor="#000000";
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
    rectY=boxH-(boxH*1.03)-4;
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

    function buildCrossTab(div,data,columns,measureArray,width,height){
   var colIds;
var colName;
//alert("first::: "+JSON.stringify(data))
var fromoneview=parent.$("#fromoneview").val();
var dashletid=div;
var chartData = JSON.parse(parent.$("#chartData").val());
var measure1 = measureArray[0];
var div1=parent.$("#chartname").val();
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

    var TableFontColor="#000";
    if(typeof chartData[div]["TableFontColor"]!=="undefined" && chartData[div]["TableFontColor"] !=""){
        TableFontColor=chartData[div]["TableFontColor"];
    }
    var HeadingBackGround="rgb(227, 243, 222)";
    if(typeof chartData[div]["HeadingBackGround"]!=="undefined" && chartData[div]["HeadingBackGround"] !=""){
        HeadingBackGround=chartData[div]["HeadingBackGround"];
    }
    var HeadingColor="#000";
    if(typeof chartData[div]["HeadingColor"]!=="undefined" && chartData[div]["HeadingColor"] !=""){
        HeadingColor=chartData[div]["HeadingColor"];
    }
    var mdata = [];
    var keys = [];
    var max;
    var groupedTotal = {};
    var indiviTotal = {};
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
      var dataList = []; 
    for(var k=0;k<mdata.length;k++){
    var mapName = {};
         var listName = [];
        var listValue = [];
        var dataKey = Object.keys(mdata[k]);
        for(var m=0;m<innRecordCount;m++){
        listName.push(dataKey[m+1]);
          listValue.push(mdata[k][dataKey[m+1]])
        }
        mapName[keys[k]] = listName;
        mapName[measureArray[0]] = listValue;
        
     dataList.push(mapName);   
    }
    var html = "";
       html += "<div class='hiddenscrollbar' id=\"" + div + "tablediv\"  style=\"height:"+height*.92+"px; width:100%;overflow: hidden;\">";
         html = html + "<div class='innerhiddenscrollbar' style='margin-left:1px;height:"+height*.92+"px;width:100%;float:left;margin-top:auto;padding-right: 15px;'>";
    html += "<table id='crossTabGraph' class='table table-striped table-condensed display'>";
//    html += "<thead style='border-bottom: medium none honeydew;'>"
    html += "<thead>"
    html += "<tr>";
//   html += "<th style='font-family: helvetica;font-size: 18px;color: rgb(105, 105, 105);'>"+columns[0]+"</th>";
   html += "<td style='font-weight:bold;border-bottom: 2px solid rgb(221, 221, 221)'>"+columns[0]+"</td>";
   for(var k in measureArray){
//       html += "<th style='font-family: helvetica;font-size: 18px;color: rgb(105, 105, 105);'>"+measureArray[k]+"</th>";
       html += "<td style='font-weight:bold;border-bottom: 2px solid rgb(221, 221, 221)'>"+measureArray[k]+"</td>";
   }
    html += "</tr>";
    html += "</thead>";
//    dataList.forEach(d, i){
html += "<tbody>";
//var totalList = [];
      for(var m in dataList){
//var list = dataList[m][measureArray[0]];
//        var sum = 0;
//        for(var z in list){
//            
//        }
 var sumData=0;
          for(var k=0;k<innRecordCount;k++){
            if(typeof dataList[m][keys[m]][k]==='undefined'){
                  continue;
              }  
            var msrData1 = addCommas(dataList[m][measureArray[0]][k]);
              sumData=parseFloat(sumData)+parseFloat(msrData1.replace(",",""));
                      }
                      
          html += "<tr style='background-color:"+HeadingBackGround+"'>";
          html += "<td style='font-family: LatoWeb;font-size: 14px;color:"+HeadingColor+";'>"+keys[m]+"</td>";
          html += "<td id='"+keys[m].replace(" ","")+"' style='font-family: LatoWeb;font-size: 13px;color:"+HeadingColor+";'>"+addCommas(numberFormat(sumData,yAxisFormat,yAxisRounding,div))+"</td>";
          html += "</tr>";
         
          for(var k=0;k<innRecordCount;k++){
              if(typeof dataList[m][keys[m]][k]==='undefined'){
                  continue;
              }
//              for(var i in measureArray){
          html += "<tr>";
              html += "<td style='color:"+TableFontColor+";padding-left:2%'>"+dataList[m][keys[m]][k]+"</td>";
              var msrData = addCommas(numberFormat(dataList[m][measureArray[0]][k],yAxisFormat,yAxisRounding,div));
              html += "<td style='color:"+TableFontColor+"'>"+msrData+"</td>";
          html += "</tr>";
//          }
          
          }
      }
      html += "</tbody>";
    html +="</table>";
    html += "</div></div>";
    $("#"+div).html(html);
 var table = $('#crossTabGraph').dataTable( {
//          "filter":true,
          "iDisplayLength":12,
          "bPaginate": true,
           "dom": 'T<"clear">rtp',
//        sDom: '<"datatable-exc-msg"><"row">flTrtip',
          "ordering": false,
          "jQueryUI": false,
          "bAutoWidth": false,
          "scrollY": "300px"
//           "scrollY": ""+height *.90+"px"
//          "order": [[ 1, "desc" ]]  
          } );

}
   function buildTopAnalysis(div, data, columns, measureArray, divWidth, divHeight,KPIresult,drillReportType,viewByIconMap,drillId) {
//  alert(columns)
//alert(JSON.stringify(data))  
    var appendDiv = "";
if(div.indexOf("Top1Div_")!=-1 ||div.indexOf("Top2Div_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
appendDiv = div

}
    var chartData = JSON.parse(parent.$("#chartData").val());
//    var chartType = chartData[]
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
    combofontSize1=combofontSize1>22?22:combofontSize1;
    var combofontSize2=combofontSize2/1.8;
//     var fun = "drillWithinchart(this.id,\""+div+"\")";
    var columnsIds;
     columnsIds=chartData[div]["viewIds"];
    
    
    if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
        fontSize1= chartData[div]["kpiGTFont"];
    }else{
         fontSize1=$("#div"+div).height()/13;
    fontSize1=fontSize1>22?22:fontSize1;
    }
    chartData[div]["kpiGTFont"]=fontSize1;
    
    if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
        fontSize2= chartData[div]["kpiSubData"];
    }else{
         fontSize2=$("#div"+div).height()/13;
    fontSize2=fontSize2>22?22:fontSize2;
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
    prefixFontSize=prefixFontSize>22?22:prefixFontSize;
    }
    
    if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
    }else{
    Suffixfontsize = $("#div"+div).height()/13;
    Suffixfontsize=Suffixfontsize>22?22:Suffixfontsize;
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
    
//    var fontSize4=fontSize1/1.8;
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
//     combofontSize2=combofontSize1;
    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate
  
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
        yAxisRounding =2;
        chartData[div]["rounding"]="2";
        parent.$("#chartData").val(JSON.stringify(chartData));
    }
    //....
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
    labelName += measureArray[0]+" by Top "+columns[0];
    var titleFontSize='12';
    if(typeof chartData[div]["titleFontSize"]!='undefined' && chartData[div]["titleFontSize"]!=''){
        titleFontSize=chartData[div]["titleFontSize"];
    }
    var titleColor='#6AC8E8';
    if(typeof chartData[div]["titleColor"]!=="undefined" && chartData[div]["titleColor"] !=""){
        titleColor=chartData[div]["titleColor"];
    }
    if(typeof KPIresult!='undefined' && KPIresult==='worldMap'){
        htmlstr +="<div class='maindiv' style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;padding-top:8px;width:80%;margin-left:50px;color:height:auto;text-align:left;float:top;color: #808080;font-weight:bold'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+(fontSize1+2)+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'></span></div>";
    }
    else{
        htmlstr +="<div class='' style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px;margin-top:7px;margin-botton:6px;text-align:left'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;padding-top:8px;width:80%;color:height:auto;text-align:left;float:top;color: "+titleColor+";font-weight:bold;margin-left:13px'><span id=' ::header"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style = 'line-height: -moz-block-height;font-weight:bold;cursor:pointer;font-size:"+titleFontSize+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;' class='onHoverGreen'>"+labelName+"</span></div>";
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
//            alert(Math.ceil((data.length<noOfRecords?data.length:noOfRecords)/3));
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
                        htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(dataLength/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='background-color:#fff;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom; color:grey;'><span class='onHoverGreen'>"+data[dataCounter][columns[0]]+"</span></div>";
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
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(dataLength/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;background-color:#fff;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom; color:grey;'><span style='white-space: nowrap;' class='onHoverGreen'>"+data[dataCounter][columns[0]]+" | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0])+ addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")"+"</span></div></div></div>";
            }else{
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(dataLength/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:80%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+" "+suffix+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;background-color:#fff;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom; color:grey'><span style='white-space: nowrap;' class='onHoverGreen'>"+data[dataCounter][columns[0]]+"</span></div></div></div>";
            }}}else{
                    htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(dataLength/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'>"+iconPath+"<div style='width:100%;height: 80%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+combofontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+"::"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space: nowrap;background-color:#fff;cursor:pointer;font-style:bold;font-size:"+combofontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:grey'><span class='onHoverGreen' style='white-space: nowrap;'>"+data[dataCounter][columns[0]]+" | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+comparison+")"+"</span></div></div></div>";
            }
            }else{
            if(suffix.trim()==""){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                        htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(data.length/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='background-color:#fff;cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>"+data[dataCounter][columns[0]]+"</span></div>";
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                    htmlstr += "<div style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+fontSize4+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+";'>("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+suffix+comparison+")</div></div></div>";
                }
                else{
//                    htmlstr += "<div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:grey'></div>";
                    htmlstr += "</div></div>";    
                    }
                }
                else{
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(data.length/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='white-space:nowrap;background-color:#fff;cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'><span  style='white-space: nowrap;' class='onHoverGreen'>"+data[dataCounter][columns[0]]+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px' class='onHoverGreen'> | ("+prefix+ addCurrencyType(div, chartData[div]["meassureIds"]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+suffix+comparison+")</span></div></div></div>";
                        
            }
                    else{
                            htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(data.length/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div id='"+data[dataCounter][columns[0]]+":"+columnsIds[0]+"' onclick='drillWithinchart(this.id,\""+div+"\",\"\",\""+drillId+"\")' style='background-color:#fff;cursor:pointer;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>"+data[dataCounter][columns[0]]+"</span></div></div></div>";
                        
            }
            }
            }
            else{
                    htmlstr +="<div class='maindiv' style='padding-top:"+padding+"px;width:"+(tileWidth/(Math.ceil(data.length/3)+0.1))+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize4+"px;'>"+changePercentHtml+"</span></div><div style='white-space:nowrap;background-color:#fff;font-style:bold;cursor:pointer;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'>"+data[dataCounter][columns[0]]+"<span style='color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + (numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+suffix+comparison+")</span></div></div></div>";
            }
                
            }
            
            
        //  htmlstr +="<div style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:30%;background-color:red;'>ddddd</div><div style='background-color:#fff;width:100%;height:50%;float:top;color: black;'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: -moz-block-height;text-align:center;word-wrap: break-word;width:100%;height:100%'>"+measureArray[i]+"</span ></div><hr style='border-top:#d1d1d1;width:80%'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: -moz-block-height;text-align:center;width:100%;height:50%; float:bottom'>"+preSuffData[i]+"</div></div>";
            dataCounter++;
        }
        }
//        htmlstr +="<div  class='maindiv'  style='width:"+($("#"+div).width()-12)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:100%;height:auto;float:top;color: grey;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+addCommas(numberFormat(data[i][measureArray[0]],yAxisFormat,yAxisRounding,div))+"</span></div><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;text-align:center;width:100%;height:50%; float:bottom;color:grey'>("+addCommas(numberFormat(data[i][measureArray[1]],yAxisFormat,yAxisRounding,div))+")</div></div></div>";
    // htmlstr +="<div style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+($("#"+div).width()-9)+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:30%;background-color:red;'>ddddd</div><div style='background-color:#fff;width:100%;height:50%;float:top;color: black;'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: -moz-block-height;text-align:center;word-wrap: break-word;width:100%;height:100%'>"+measureArray[i]+"</span></div><hr style='border-top:#d1d1d1;width:80%'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: -moz-block-height;text-align:center;width:100%;height:50%; float:bottom'>"+preSuffData[i]+"</div></div>";
    //       alert($("#"+div).width()-15);
//    }
       
//    $("#"+div).css({
//        "background-color":"#E8E8E8 ",
//        "height":"90%"       
//    });
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

 function buildComboAnalysis(div, data, columns, measureArray, divWidth, divHeight,KPIresult,viewByIconMap) {
var chartData = JSON.parse(parent.$("#chartData").val());
var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
var viewOvName =JSON.parse(parent.$("#viewby").val());

var viewBys =""
var viewIds =""
var dimensions =""
if(typeof chartData[div]["viewBys"][1]!="undefined" && chartData[div]["viewBys"][1]!=""){
 viewBys=chartData[div]["viewBys"][1]
 viewIds=chartData[div]["viewIds"][1]

}else{
 viewBys=chartData[div]["viewBys"][0]
 viewIds=chartData[div]["viewIds"][0]
 
}
if(typeof chartData[div]["dimensions"][1]!="undefined" && chartData[div]["dimensions"][1]!=""){
 dimensions=chartData[div]["dimensions"][1]
}else{
 dimensions=chartData[div]["dimensions"][0]
}
var column2 = []
var column2Id = []
var dimensions2 = []
column2.push(viewBys)
column2Id.push(viewIds)
dimensions2.push(dimensions)
for(var i in chartData[div]["dimensions"]){
if(dimensions2.indexOf(chartData[div]["dimensions"][i])==-1){
dimensions2.push(chartData[div]["dimensions"][i])
}
}
var orgcolumnsstr = chartData[div]["viewBys"][0]
var orgcolumnsIdsstr = chartData[div]["viewIds"][0]

var orgcolumns = []
var orgcolumnsIds = []
orgcolumns.push(orgcolumnsstr)
orgcolumnsIds.push(orgcolumnsIdsstr)
chartData[div]["viewBys"] = column2;
chartData[div]["viewIds"] = column2Id;
//chartData[div]["dimensions"] = dimensions2;

parent.$("#chartData").val(JSON.stringify(chartData));
var htmlvar = ""
htmlvar += "<div id='kpiPieDiv_"+div+"' style='float:left;width:"+divWidth*.38+"px;height:"+divHeight+"px'>";
htmlvar += "<div id='kpiDiv_"+div+"' style='border-right:1px solid #d1d1d1;float:left;width:"+divWidth*.38+"px;height:"+(divHeight)*.2+"px'></div>";
htmlvar += "<div id='colPieDiv_"+div+"' style='float:left;border-right:1px solid #d1d1d1;width:"+divWidth*.38+"px;height:"+(divHeight)*.75+"px'></div>";
htmlvar += "</div>"
htmlvar += "<div id='Top1Div_"+div+"' style='margin-left:15px;float:left;width:"+divWidth*.29+"px;height:"+divHeight+"px'></div>";
htmlvar += "<div id='Top2Div_"+div+"' style='float:left;width:"+divWidth*.29+"px;height:"+divHeight+"px'></div>";

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
var filterValue;
try{
    
 filterValue= JSON.parse($("#filters1").val());
}catch(e){
    
}
  tileCharts("kpiDiv_"+div, "", columns, measureArray, divWidth*.38, (divHeight)*.2,KPIresult);
buildColumnPie("colPieDiv_"+div, "", columns, columnMeasures, divWidth*.38, (divHeight)*.8,columnPieMeasureValues);

var actionGO = parent.$("#actionGO").val();

if(typeof filterValue !=="undefined" && filterValue!==""){
    var keys = Object.keys(filterValue);
    var counterflag = 0
    for(var i in keys){
        if(filterValue[keys[i]].length>0){
         counterflag = 1;   
        }
       }
    if(counterflag==1){
    actionGO = "paramChange";
    }
}
$.post($("#ctxpath").val()+'/reportViewerAction.do?reportBy=getComboGraphData&chartId='+div+'&actionGo='+actionGO, parent.$("#graphForm").serialize(),
function(data){
    if(data==="false"){
        
      var viewList1 = [];
   var viewIdList1 = [];
   try{
   viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
   viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);
   }catch(e){
   viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
   viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);
   }
   chartData[div]["viewBys"] = viewList1;
chartData[div]["viewIds"] = viewIdList1;
//chartData[div]["dimensions"] = dimensions2;
chartData[div]["comboType"] = "Drill1";
parent.$("#chartData").val(JSON.stringify(chartData));
 $.ajax({
        async:false,
        type:"POST",
            data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div+'&actionGo='+actionGO,
       success: function(data12){
          var data2 = JSON.parse(data12)

         

      buildTopAnalysis("Top1Div_"+div, JSON.parse(data2["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.29, divHeight,KPIresult,"drill2",viewByIconMap)
 
       }  });
   var viewList = [];
   var viewIdList = [];
   try{
   viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
   viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);
   }catch(e){
   viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
   viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);
   }
     chartData[div]["viewBys"] = viewList;
chartData[div]["viewIds"] = viewIdList;
chartData[div]["comboType"] = "Drill2";
parent.$("#chartData").val(JSON.stringify(chartData));
  $.ajax({
//        async:false,
        type:"POST",
            data:parent.$("#graphForm").serialize(),
        url:$("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId="+div+'&actionGo='+actionGO,
       success: function(data){
          var data1 = JSON.parse(data)
         
//var defaultcolumn = []
//var defaultcolumnIds = []
//defaultcolumn.push(orgcolumnsstr)
//defaultcolumnIds.push(orgcolumnsIdsstr)
//if(orgcolumnsstr.indexOf(column2)==-1){
//defaultcolumn.push(column2[0])
//defaultcolumnIds.push(column2Id[0])
//}
//chartData[div]["viewBys"] = defaultcolumn;
//chartData[div]["viewIds"] = defaultcolumnIds;
//parent.$("#chartData").val(JSON.stringify(chartData));

       
buildTopAnalysis("Top2Div_"+div, JSON.parse(data1["data"])[div], chartData[div]["viewBys"], measureArray, divWidth*.29, divHeight,KPIresult,"drill1",viewByIconMap)
       } })  
        
    }else{
    
      var dataList1 = JSON.parse(JSON.parse(data)["data1"]);
      var dataList2 = JSON.parse(JSON.parse(data)["data2"]);
       var viewList1 = [];
   var viewIdList1 = [];
   try{
   viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
   viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);
   }catch(e){
   viewList1.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
   viewIdList1.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);
   }
   chartData[div]["viewBys"] = viewList1;
chartData[div]["viewIds"] = viewIdList1;
      
        buildTopAnalysis("Top1Div_"+div, dataList1, chartData[div]["viewBys"], measureArray, divWidth*.3, divHeight,KPIresult,"drill2",viewByIconMap)
      var viewList = [];
   var viewIdList = [];
   try{
   viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[0])]);
   viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[0])]);
   }catch(e){
   viewList.push(viewOvName[viewOvIds.indexOf(dimensions2[1])]);
   viewIdList.push(viewOvIds[viewOvIds.indexOf(dimensions2[1])]);
   }
     chartData[div]["viewBys"] = viewList;
chartData[div]["viewIds"] = viewIdList;
buildTopAnalysis("Top2Div_"+div, dataList2, chartData[div]["viewBys"], measureArray, divWidth*.3, divHeight,KPIresult,"drill1",viewByIconMap)
    }
})

//}

}
    function buildWorldTopAnalysis(div, data, columns, measureArray, divWidth, divHeight,KPIresult) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var dimensions = chartData[div]["dimensions"];
    var allViewBys = JSON.parse($("#viewby").val());
    var allViewByIds = JSON.parse($("#viewbyIds").val());
    var dataArray = [];
    var fontSize1=0;
    var fontSize2=0;
    var fontSize3=0;
    var fontSize4=0;
    var fontSize5=22;
    var prefixFontSize=0;
    var Suffixfontsize=0;
    var measureColor="";
    var colorPicker="";
    var drillReportType = "";	
    
    if(typeof chartData[div]["kpiGTFont"]!="undefined" && chartData[div]["kpiGTFont"] !== '' && chartData[div]["kpiGTFont"] !=="4"){
        fontSize1= chartData[div]["kpiGTFont"];
    }else{
         fontSize1=$("#div"+div).height()/13;
    fontSize1=fontSize1>22?22:fontSize1;
    }
    chartData[div]["kpiGTFont"]=fontSize1;
    
    if(typeof chartData[div]["kpiSubData"]!="undefined" && chartData[div]["kpiSubData"] !== '' && chartData[div]["kpiSubData"] !=="4"){
        fontSize2= chartData[div]["kpiSubData"];
    }else{
         fontSize2=$("#div"+div).height()/13;
    fontSize2=fontSize2>22?22:fontSize2;
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
    fontSize4=fontSize4>15?15:fontSize4;
    }
    chartData[div]["comparativeValue"]=fontSize4;
    
    if(typeof chartData[div]["Prefixfontsize"]!="undefined" && chartData[div]["Prefixfontsize"]!="" ){
    prefixFontSize = chartData[div]["Prefixfontsize"];
    }else{
    prefixFontSize = $("#div"+div).height()/13;
    prefixFontSize=prefixFontSize>22?22:prefixFontSize;
    }
    
    if(typeof chartData[div]["Suffixfontsize"]!="undefined" && chartData[div]["Suffixfontsize"]!="" ){
    Suffixfontsize = chartData[div]["Suffixfontsize"];
    }else{
    Suffixfontsize = $("#div"+div).height()/13;
    Suffixfontsize=Suffixfontsize>22?22:Suffixfontsize;
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
    $("#chartData").val(JSON.stringify(chartData));
    // fontSize2=fontSize1;
    var viewBys=chartData[div]["viewBys"]
    var viewIds=chartData[div]["viewIds"]

    DataSum_measureArray1=[];
    DataSum_measureArray=[];
    //    for GTcalculate

    //    endof GT claculat
    //    alert(DataSum_measureArray);
    var tileWidth,tileHeight,rowCount,wDiv;
    var offset=$("#"+div).width();
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
    //....
    DataSum_measureArray=[];
    graphProp(div);
    var viewBy2;
    var viewById2;
    var viewBy1;
    var viewById1;
    var currentViewBy=chartData[div]["viewIds"][0];
    viewBy1=allViewBys[allViewByIds.indexOf(chartData[div]["dimensions"][0])];
    viewById1=chartData[div]["dimensions"][0];
    if(dimensions[0]===viewById1){
        if(typeof dimensions[1]!='undefined'){
            viewById2=dimensions[1];
            viewBy2=allViewBys[allViewByIds.indexOf(dimensions[1])];
        }
    }
    else{
        viewById2=dimensions[0];
        viewBy2=allViewBys[allViewByIds.indexOf(dimensions[0])];
    }
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
    var measureCount=data.length>3?3:data.length;
    if(fromoneview!='null'&& fromoneview=='true'){
        div=dashledid
    }
    var labelName='';
    labelName += measureArray[0]+" by Top "+columns[0];
    if(typeof KPIresult!='undefined' && KPIresult==='worldMap'){
        htmlstr +="<div class='maindiv' style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:auto;height: 100%;float: left;'>";
    }
    else{
        htmlstr +="<div class='' style='width:"+tileWidth+"px; height:25px;background-color:#fff;display:block;float:left;margin:3px;margin-top:7px;margin-botton:6px;'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;padding-top:8px;width:90%;margin-left:10px;color:height:auto;text-align:left;float:top;color: #6AC8E8;font-weight:bold'><span id=' ::header' onclick='drillWithinchart(this.id,\""+div+"\")' style = 'line-height: -moz-block-height;cursor:pointer;font-weight:bold;font-size:13px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+labelName+"</span></div>";
    }
//    wid1=viewBy1.length*7;
var boldCss='';
if(currentViewBy===viewById1){
    boldCss='font-Weight:bold;'
}
    htmlstr += "<div class='maindiv' style='padding-top:"+padding+"px;width:"+tileWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='margin-top:5px;width:50%;height: 100%;float: left;'><div style='background-color:#fff;width:auto;height:29px;float:top;color: grey;text-align:left;margin-left: 10px;padding-left: 8px;border-style: solid;border-width: 1px;border-radius: 3px;border-color:#ccc;position: absolute;'>";
htmlstr += "<span id='"+viewBy1+":"+viewById1+"' class='onHoverGreen' onclick='changeViewBys(this.id,\""+div+"\")' style = '"+boldCss+"cursor:pointer;padding:3px;line-height: -moz-block-height;font-style:bold;font-size:14px;text-align:center;word-wrap: break-word;;height:70%;float:left;'>"+viewBy1+"</span>";
//    htmlstr += "<div class='maindiv' style='padding-top:"+padding+"px;width:"+tileWidth+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='margin-top:5px;width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:-30px;'><span id='"+viewBy1+":"+viewById1+"' class='onHoverGreen' onclick='changeViewBys(this.id,\""+div+"\")' style = 'cursor:pointer;border-style:solid;border-color:#ddd;border-width:1px;padding:8px;line-height: -moz-block-height;font-style:bold;font-size:12px;border-radius:3px;font-weight:bold;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+viewBy1+"</span>"
    if(typeof viewBy2!='undefined'){
    boldCss='';
if(currentViewBy===viewById2){
    boldCss='font-Weight:bold;'
}
//        wid1=viewBy2.length*7;
//        htmlstr += "<span id='"+viewBy2+":"+viewById2+"' class='onHoverGreen' onclick='changeViewBys(this.id,\""+div+"\")' style = 'cursor:pointer;border-style:solid;border-color:#ddd;border-width:1px;padding:8px;line-height: -moz-block-height;font-style:bold;font-size:12px;border-radius:3px;text-align:center;word-wrap: break-word;width:100%;height:100%;'>"+viewBy2+"</span>";
     htmlstr += "<hr style='display: inline-block; height: 72%; float: left; border: medium none; background-color: rgb(204, 204, 204); margin: 2% 0px;' size='500' width='1'><span id='"+viewBy2+":"+viewById2+"' class='onHoverGreen' onclick='changeViewBys(this.id,\""+div+"\")' style = '"+boldCss+"cursor:pointer;line-height: -moz-block-height;font-size:14px;text-align:center;padding:3px;word-wrap: break-word;height:70%;float:left;'>"+viewBy2+"</span>";
    }
    htmlstr += "</div>";
    
        rowCount=3;
        wDiv=parseInt((measureCount)/rowCount);
        tileWidth=$("#"+div).width()/wDiv-9;
        tileHeight=($("#"+div).height()-25)/(measureCount+0.3)-10;
        for(var i=0;i<measureCount;i++){
        var dataCounter=i;
        var upArrow='&nbsp;&#65514;'
        var downArrow='&nbsp;&#65516;'
        var arrow=''
        var change=0;
        if(parseInt(data[dataCounter][measureArray[0]])>parseInt(data[dataCounter][measureArray[1]])){
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
            changePercentHtml=arrow;
//            changePercentHtml=arrow+"("+changePercent+"%)";
        }
//        changePercentHtml='';
        var padding=0;
        if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true' && typeof measureArray[1]!='undefined'){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                    padding=($("#"+div).height()-70)*0.005;
                    padding=i==0?(padding-4):padding;
                }
                else{
                    padding=($("#"+div).height()-60)*0.12;
                    padding=i==0?(padding-8):padding;
                }
        }
        else{
                padding=($("#"+div).height()-60)*0.03;
                padding=i==0?(padding-8):padding;
        }
        if(i==0){
            padding+=15;
        }
            var data1=data[i][measureArray[0]],data2;
            if(typeof data[i][measureArray[1]]==='undefined'){
                data2=data1;
            }
            else{
                data2=data[i][measureArray[1]];
            }
            
            if(suffix.trim()==""){
                if(typeof chartData[div]["prevPeriodDisplay"]==='undefined' || chartData[div]["prevPeriodDisplay"]==='multi'){
                    if(typeof data[i][columns[0]]!="undefined" && data[i][columns[0]]!=""){
                   htmlstr +="<div class='maindiv' style='white-space:nowrap;padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;margin-top:20px;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>"+data[i][columns[0]]+"</span></div>";
                    }else{
                   htmlstr +="<div class='maindiv' style='white-space:nowrap;padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;margin-top:20px;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:auto; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>NA</span></div>";
                    }
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                    htmlstr += "<div style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+fontSize4+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'>("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")</div></div></div>";
                }else{
//                    htmlstr += "<div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:grey'></div>";
                    htmlstr += "</div></div>";
                    }
                }else{
                    if(typeof chartData[div]["enableComparison"]!='undefiend' && chartData[div]["enableComparison"]==='true'){
                   if(typeof data[i][columns[0]]!="undefined" && data[i][columns[0]]!=""){
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span><span style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+fontSize4+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+";'>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='cursor:pointer;color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:96%;height:50%; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>"+data[i][columns[0]]+"</span><span style='white-space:nowrap;color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")</span></div></div></div>";
                     }else{
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span><span style='white-space:nowrap;background-color:#fff;font-style:bold;font-size:"+fontSize4+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+";'>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='cursor:pointer;color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:96%;height:50%; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>NA</span><span style='white-space:nowrap;color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div))+" "+suffix+comparison+")</span></div></div></div>";
                    }
            }else{
                 if(typeof data[i][columns[0]]!="undefined" && data[i][columns[0]]!=""){
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'>"+data[i][columns[0]]+"</div></div></div>";
                     }else{
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div))+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'>NA</div></div></div>";
                    }
                
            }}
            }else{
                 if(typeof data[i][columns[0]]!="undefined" && data[i][columns[0]]!=""){
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>"+data[i][columns[0]]+"</span><span style='white-space:nowrap;color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+comparison+")</span></div></div></div>";
                     }else{
                htmlstr +="<div class='maindiv' style='padding-top:"+padding*2.1+"px;width:"+tileWidth*0.8+"px; height:auto;background-color:#fff;display:block;float:left;margin:3px'><div style='width:100%;height: 100%;float: left;'><div style='background-color:#fff;width:90%;height:auto;float:top;color: grey;text-align:left;margin-left:10px;'><span style = 'line-height: -moz-block-height;font-style:bold;font-size:"+fontSize1+"px;text-align:center;word-wrap: break-word;width:100%;height:100%;color:"+colorPicker+"'><span style='color:"+colorPicker+";font-size:"+prefixFontSize+"px;'>"+prefix+"</span>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data1,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+"</span><span style='color:"+colorPicker+";font-size:"+Suffixfontsize+"px;'>"+suffix+"</span><span style='color:"+measureColor+";font-size:"+fontSize5+"px;'>"+changePercentHtml+"</span></div><div id='"+data[i][columns[0]]+"::"+drillReportType+"' onclick='drillWithinchart(this.id,\""+div+"\")' class='onHoverGreen' style='cursor:pointer;background-color:#fff;font-style:bold;font-size:"+fontSize3+"px;text-align:left;margin-left:10px;width:90%;height:50%; float:bottom;color:"+measureColor+"'><span class='onHoverGreen'>NA</span><span style='white-space:nowrap;color:"+measureColor+";font-size:"+fontSize4+"px'> | ("+prefix+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(data2,yAxisFormat,yAxisRounding,div)).toString().match(/[-]?\d*(?:[.,]?\d+)+/)[0]+" "+suffix+comparison+")</span></div></div></div>";
                    }
                
            }
        //  htmlstr +="<div style='box-shadow:0px 2px 3px 3px #D0D0D0 ;width:"+tileWidth+"px; height:"+tileHeight+"px;background-color:#fff;display:block;float:left;margin:3px'><div style='width:30%;background-color:red;'>ddddd</div><div style='background-color:#fff;width:100%;height:50%;float:top;color: black;'><span style = 'font-style:bold;font-size:"+fontSize1+"px;line-height: -moz-block-height;text-align:center;word-wrap: break-word;width:100%;height:100%'>"+measureArray[i]+"</span ></div><hr style='border-top:#d1d1d1;width:80%'><div style='background-color:#fff;font-style:bold;font-size:"+fontSize2+"px;line-height: -moz-block-height;text-align:center;width:100%;height:50%; float:bottom'>"+preSuffData[i]+"</div></div>";
        }
    $("#"+div).append(htmlstr);
    if($(".imgdiv").width() > $(".imgdiv").height()){
        $(".imgdiv>img").height($(".imgdiv").height()-10);
    }else{
        $(".imgdiv>img").width($(".imgdiv").width());
    }
    $(".imgdiv").mouseenter(function(){
        $(this).children().children().children().show();
    });
    $(".imgdiv").mouseleave(function(){
        $(this).children().children().children().hide();
    });
}
 function buildTrendCombo(div, data, columns, measureArray, divWidth, divHeight,KPIresult,childFlag,parentMeasIds){

  var htmlvar = "";
  var yAxisFormat = "";
  var yAxisRounding = 0;
  var trdivHeight = 0;
  graphProp(div);
var datavar = data;
data = [];

var dataKey = Object.keys(datavar[0])

if(dataKey.indexOf(columns[0])==-1 || dataKey.indexOf(measureArray[0])==-1){
 alert("Internal Server Error\nPlease reload the page.");
}
var measureArrayvar = measureArray;
measureArray = []
for(var measurecount=0;measurecount<(measureArrayvar.length<10?measureArrayvar.length:10);measurecount++){
measureArray.push(measureArrayvar[measurecount])
}
//for(var datacount=0;datacount<(datavar.length<10?datavar.length:10);datacount++){
for(var datacount=0;datacount<datavar.length;datacount++){
data.push(datavar[datacount]);
}

if(measureArray.length<5){
    trdivHeight = 5
}else{
    trdivHeight = measureArray.length
}
  var chartData = JSON.parse(parent.$("#chartData").val());
  if(typeof chartData[div]["ParentMeasures"]=="undefined" || chartData[div]["ParentMeasures"]=="" || chartData[div]["ParentMeasures"].length<1){
    chartData[div]["ParentMeasures"] = measureArray;
   }
var parentGroupMeasureId = [];
if(typeof chartData[div]["ParentGroupedMeasures"]!="undefined" && chartData[div]["ParentGroupedMeasures"]=="" ){
parentGroupMeasureId = chartData[div]["ParentGroupedMeasures"];
}else{
  $.ajax({
            async:false,
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:$("#ctxpath").val() +"/reportViewerAction.do?reportBy=ParentGroupedMeasuresCheck",
                success:function(data){
		//	alert(data)
                    parentGroupMeasureId = JSON.parse(data);
                    chartData[div]["ParentGroupedMeasures"] = parentGroupMeasureId;
                }})  
}
 // alert(JSON.stringify(data))
  parent.$("#chartData").val(JSON.stringify(chartData));
  var columnsArr = []
  columnsArr.push(columns[0])
  if(typeof columns[1]!="undefined" && columns[1]!=""){
  columnsArr.push(columns[1])
  }
  var subDivHgt = divHeight*.46;
  var columnwidth = (divWidth/data.length);
  var tableHeight = parseInt(parseFloat(subDivHgt)/trdivHeight)
//  var tableHeight = parseInt(parseFloat(subDivHgt)/measureArray.length)
htmlvar += "<div id='trendcombo_"+div+"' style='width:"+divWidth+"px;height:"+subDivHgt+"px;float:left' ></div> ";
htmlvar += "<div id='tablecombo_"+div+"' style='width:"+divWidth+"px;height:"+subDivHgt+"px;padding-top:15px;float:left' ></div> ";
$("#"+div).html(htmlvar);
var htmlstr = "";
htmlstr += "<table class='  ' style='width:100%'>";
//htmlstr += "<col width='180'>"
//for(var i in data){
//
//htmlstr += "<col width='80'>"
//}
if (typeof chartData[div]["trendViewMeasures"] !== "undefined" && chartData[div]["trendViewMeasures"] === "ViewBys") {
 var columnArr = []
 var columnArr1 = []
 for(var d in data ){
 columnArr1.push(data[d][columns[0]])
 }
 var tempColumn1=[];
  label:for(var q=0;q<columnArr1.length;q++){
        for(var j=0; j<tempColumn1.length;j++ ){//check duplicates
            if(tempColumn1[j]==columnArr1[q])//skip if already present
               continue label;
        }
        tempColumn1[tempColumn1.length] = columnArr1[q];
  }
 for(var d in data ){
if(typeof columns[1]!="undefined" && columns[1]!="" && typeof data[d][columns[1]]!="undefined" && data[d][columns[1]]!=""){
 columnArr.push(data[d][columns[1]])
}else{
 columnArr.push(data[d][columns[0]])
}
 }
          var temp1=[];
  label:for(var q=0;q<columnArr.length;q++){
        for(var j=0; j<temp1.length;j++ ){//check duplicates
            if(temp1[j]==columnArr[q])//skip if already present
               continue label;
        }
        temp1[temp1.length] = columnArr[q];
  }
var hgt =0;
if(temp1.length<5){
    hgt = 5
}else{
    hgt = temp1.length
}
 
var colwidth = (divWidth/tempColumn1.length);
  for(var i=0;i<(temp1.length<10 ? temp1.length:10);i++ ){
  htmlstr += "<tr >";
  if(temp1[i].toString().length>10){
htmlstr +="<td width='132' id='"+temp1[i]+"'  style='color:#6AC8E8;border-right:1px solid #d1d1d1'><span style='padding-right:10px;font-weight:bold;float:right'>"+temp1[i].substring(0, 10)+"..</span></td>";
}else{
htmlstr +="<td width='132' id='"+temp1[i]+"'  style='color:#6AC8E8;border-right:1px solid #d1d1d1'><span style='padding-right:10px;font-weight:bold;float:right'>"+temp1[i]+"</span></td>";
}
//alert(chartData[div]["yAxisFormat"]+"-----------"+chartData[div]["rounding"])
if(typeof chartData[div]["yAxisFormat"]!="undefined" && chartData[div]["yAxisFormat"]!="" ){
yAxisFormat = chartData[div]["yAxisFormat"]
  }
  if(typeof chartData[div]["rounding"]!="undefined" && chartData[div]["rounding"]!="" ){
yAxisRounding = chartData[div]["rounding"]
}
//alert(yAxisFormat+":::::::::::::::::::::::"+yAxisRounding)
for(var temp in tempColumn1){
var measureValue = 0;
for(var d in data){
   if(tempColumn1[temp]==data[d][columns[0]] && temp1[i]==data[d][columns[1]]){
  //  alert(tempColumn1[temp]+":"+data[d][columns[0]])
    measureValue +=  parseFloat(data[d][measureArray[0]])
}
}
 if(typeof measureValue!="undefined" && measureValue!=""){
 htmlstr +="<td width='"+colwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>"+addCurrencyType(div, chartData[div]["meassureIds"][0]) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div))+"</span></td>"
 }else{
 htmlstr +="<td width='"+colwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>0</span></td>"   
}
}
htmlstr +="</tr>";
  }

}else{
for(var i=0;i<(measureArray.length<10 ? measureArray.length:10);i++){
   var measureName='';
   var isParentMeasureFlag = false;
   for(var parentGroup in parentGroupMeasureId){
    if(typeof parentGroupMeasureId!="undefined" && parentGroupMeasureId!="" && parentGroupMeasureId[parentGroup]==chartData[div]["meassureIds"][i])   
   isParentMeasureFlag = true;
                }
if(typeof chartData[div]["measureAlias"]!=='undefined' && typeof chartData[div]["measureAlias"][measureArray[i]]!='undefined' && chartData[div]["measureAlias"][measureArray[i]]!== measureArray[i]){
    measureName=chartData[div]["measureAlias"][measureArray[i]];
}else{
    measureName=measureArray[i];
}
  if(typeof chartData[div]["numberFormat"]!="undefined" && chartData[div]["numberFormat"]!="" && typeof chartData[div]["numberFormat"][measureArray[i]]!="undefined" && chartData[div]["numberFormat"][measureArray[i]]!=""){
yAxisFormat = chartData[div]["numberFormat"][measureArray[i]]
  }
  if(typeof chartData[div]["numberRounding"]!="undefined" && chartData[div]["numberRounding"]!="" && typeof chartData[div]["numberRounding"][measureArray[i]]!="undefined" && chartData[div]["numberRounding"][measureArray[i]]!=""){
yAxisRounding = chartData[div]["numberRounding"][measureArray[i]]
}

htmlstr += "<tr height='"+tableHeight+"'>";
if(childFlag == "childMeasureFlag" && !isParentMeasureFlag){ 
if(measureName.toString().length>20){
htmlstr +="<td width='132' id='"+measureArray[i]+"'  style='color:#6AC8E8;border-right:1px solid #d1d1d1'><span  style='padding-right:10px;font-weight:bold;float:right'>"+measureName.substring(0, 20)+"..</span></td>";
}else{
htmlstr +="<td width='132' id='"+measureArray[i]+"'  style='color:#6AC8E8;border-right:1px solid #d1d1d1'><span  style='padding-right:10px;font-weight:bold;float:right'>"+measureName+"</span></td>";
}    
}else if(!isParentMeasureFlag){ 
if(measureName.toString().length>20){
htmlstr +="<td width='132' id='"+measureArray[i]+"' onclick = 'TrendComboDrill(this.id,\""+div+"\")' style='cursor:pointer;color:#6AC8E8;border-right:1px solid #d1d1d1'><span class='onHoverGreen' style='padding-right:10px;font-weight:bold;float:right'>"+measureName.substring(0, 20)+"..</span></td>";
}else{
htmlstr +="<td width='132' id='"+measureArray[i]+"' onclick = 'TrendComboDrill(this.id,\""+div+"\")' style='cursor:pointer;color:#6AC8E8;border-right:1px solid #d1d1d1'><span class='onHoverGreen' style='padding-right:10px;font-weight:bold;float:right'>"+measureName+"</span></td>";
}
}else{ 
if(measureName.toString().length>20){
htmlstr +="<td width='132' id='"+measureArray[i]+"' onclick = 'TrendComboDrill(this.id,\""+div+"\",\""+parentMeasIds+"\")' style='cursor:pointer;color:#6AC8E8;border-right:1px solid #d1d1d1'><span class='onHoverGreen' style='text-decoration: underline;padding-right:10px;font-weight:bold;float:right'>"+measureName.substring(0, 20)+"..</span></td>";
}else{
htmlstr +="<td width='132' id='"+measureArray[i]+"' onclick = 'TrendComboDrill(this.id,\""+div+"\",\""+parentMeasIds+"\")' style='cursor:pointer;color:#6AC8E8;border-right:1px solid #d1d1d1'><span class='onHoverGreen' style='text-decoration: underline;padding-right:10px;font-weight:bold;float:right'>"+measureName+"</span></td>";
}
}


for(var d in data){
    if(typeof data[d][measureArray[i]]!="undefined" && data[d][measureArray[i]]!="" ){
	if(typeof parentMeasIds!="undefined" && parentMeasIds!=""){
	htmlstr +="<td width='"+columnwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>"+addCurrencyType(div, getMeasureId(parentMeasIds)) + addCommas(numberFormat(data[d][measureArray[i]],yAxisFormat,yAxisRounding,div))+"</span></td>"	
	}else{
htmlstr +="<td width='"+columnwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>"+addCurrencyType(div, getMeasureId(measureArray[i])) + addCommas(numberFormat(data[d][measureArray[i]],yAxisFormat,yAxisRounding,div))+"</span></td>"
	}
        }else{
        htmlstr +="<td width='"+columnwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>0</span></td>"    
        }
//htmlstr +="<td width='"+columnwidth+"' align='left' style='border-top:1px solid #d1d1d1;border-bottom:1px solid #d1d1d1'><span style='color:#696969'>"+addCommas(parseFloat(data[d][measureArray[i]]).toFixed(1))+"</span></td>"
}
htmlstr +="</tr>"
}
}
htmlstr +="</table>"
$("#tablecombo_"+div).html(htmlstr)
if (typeof chartData[div]["trendViewMeasures"] !== "undefined" && chartData[div]["trendViewMeasures"] === "ViewBys") {
buildGroupedLineTrendCombo("trendcombo_"+div, data, columns, measureArray, divWidth, subDivHgt,KPIresult)
}else{

buildMultiMeasureTrendCombo("trendcombo_"+div, data, columns, measureArray, divWidth, subDivHgt,KPIresult)
}
}

function buildMultiMeasureTrendCombo(div, data, columns, measureArray, divWid, divHgt) {
      var appendDiv = "";
//      alert(JSON.stringify(data))
//      alert("columns"+columns+"measureArray"+measureArray)
if((div.indexOf("trendcombo_")!=-1 )||(div.indexOf("TableDiv")!=-1 )){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
appendDiv = div

}
      //      added by shivam

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
            left: 140
    };
    width = divWid; //- margin.left - margin.right

    }else{
          margin = {
        top: 20,
        right: 10,
        bottom: botom,
            left: 140
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
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}

}
 for(var i in measureArray){
var min = parseFloat(minimumValue(data, measureArray[i]));
if(i==0)  {

minVal=parseFloat(min);
}
else if(i>0 && ( min<minVal)){
minVal = min;
}
}

    }else{
if(typeof chartData[div]["yaxisrange"]!="undefined"&& chartData[div]["yaxisrange"]!="") {
    if(chartData[div]["yaxisrange"]["YaxisRangeType"]!="Default" && typeof chartData[div]["yaxisrange"]["axisMax"]!="undefined" && chartData[div]["yaxisrange"]["axisMax"]!="") {
    max = parseFloat(chartData[div]["yaxisrange"]["axisMax"]);
}else{
for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
}

}
}}else{
  for(var i in measureArray){
var max1 = parseFloat(maximumValue(data, measureArray[i]));

if(i==0)  {

max=parseFloat(max1);

}

else if(i>0 && (max1 > max)){
max = max1;
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
    for(var i in measureArray){
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
    var x = d3.scale.ordinal().rangePoints([0, width-20], .2);
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
 if(chartData[div]["chartType"]==="Model-Varince-Dashboard"){
    var svg = d3.select("#" + appendDiv)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+((width*.42) + margin.left + margin.right)+" "+((height*2) + margin.top + margin.bottom)+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" +(margin.left*.095) + "," + ((margin.top+offset)*1.5) + ")");
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
 }else{
         var svg = d3.select("#" + appendDiv)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
//            .attr("id", "svg_" + div)
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
 }
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
if(typeof chartData[div]["displayLegends"]==="undefined" || chartData[div]["displayLegends"]==="" || chartData[div]["displayLegends"]==="No"){}
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
    svg = d3.select("#" + appendDiv).select("g");
//    Added by shivam
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
                 // if(d[measure1] < 550){
                  // colorShad = color(1);
               // }else {
                   colorShad = getDrawColor(div, parseInt(i));

                  // }
               }

//               if (typeof chartData[div]["lineSymbol"] === "undefined" || chartData[div]["lineSymbol"] === "Hollow") {
//            return "white";
//        }else{
                return colorShad;
//        }


            })
//				  .style("fill", getDrawColor(div, parseInt(i)))
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
                        return "white";
                    }else{
                        return colorShad;
                    }
                })
                .style("stroke-width", "2px")
                .attr("id", function(d) {
                    return d[columns[0]] + ":" + d[measureArray[i]] + "#"+measureArray[i];
                })
                .attr("onclick", fun)

	//Added by shivam
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
//                    msrData = addCommas(measureValue);
                    msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));//Added by shivam
//                    alert("1");
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

               msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(measureValue);
//               alert("2");
                }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureName)) + addCommas(numberFormat(measureValue,yAxisFormat,yAxisRounding,div));
//                 alert("3");
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
                content += "<span style=\"font-family:helvetica;\" class=\"name\"> " + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureName + " <b>:</b> " + columnName + "</span><br/>";//Added by shivam
                count++;
                return tooltip.showTooltip(content, d3.event); //m
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
            .style("stroke-width", "1px")
            .style("stroke", "black");
    }
            }
    }

    function buildGroupedLineTrendCombo(div,data, columns, measureArray,divWid,divHgt) {
//   var color = d3.scale.category10();
    var appendDiv = "";
if(div.indexOf("trendcombo_")!=-1){
appendDiv = div
var splitDiv = div.split("_");

div = splitDiv[1]

}else{
appendDiv = div

}
var divId;
// if (typeof data!== 'undefined' && (data.length==5 || data.length<5)) {

//return buildGroupedBar(div, data, columns, measureArray, divWid, divHgt, div);

//}
if($("#chartType").val()=="India-Map-Dashboard"){
    divId = "section2";
}else {
    divId = appendDiv;
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
        left: 140
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
    svg = d3.select("#" + divId)
            //    added by manik
            // .append("div")
            // .classed("svg-container", true)
            .append("svg")
//            .attr("preserveAspectRatio", "xMinyMin")
            .attr("id", "svg_" + div)
            .attr("viewBox", "0 0 "+(width+120 )+" "+(height + margin.top + margin.bottom+ 17.5 )+" ")
            .classed("svg-content-responsive", true)
//            .attr("width", width + margin.left + margin.right)
//            .attr("height", height + margin.top + margin.bottom + 17.5)
            .append("g")
            .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
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
//            .append("text")
//            .attr("transform", "rotate(-90)")
//            .attr("y", -75)
//            .attr("fill","steelblue")
//            .attr("dy", ".41em")
//            .style("text-anchor", "end")
//            .text("" + measure1 + "");
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
    if(typeof chartData[div]["GridLines"]!="undefined" && chartData[div]["GridLines"]!="" && chartData[div]["GridLines"]!="Yes"){}else{
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
                    .attr('transform',function(d,i){
        if(typeof chartData[div]["legendPrintType"]!="undefined" && chartData[div]["legendPrintType"]!="" && chartData[div]["legendPrintType"]=== "Alternate") {
            return  "";
        }else if (chartData[div]["legendPrintType"] === "Horizontal") {
            return "";
        }else if (chartData[div]["legendPrintType"] === "Vertical") {
            return "rotate(-90)";
        }else {
            return "rotate(-25)";
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
		if(typeof d[columns[1]]!="undefined" && d[columns[1]]!="" ){
		return "bars-Bubble-index-" + d[columns[1]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
		}else{
		return "bars-Bubble-index-" + d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
		}

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
                    msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div));
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (chartData[div]["yAxisFormat"] === "Absolute" ||chartData[div]["yAxisFormat"] === "")){

                        msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,div));

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
//     var yvalue=0;
//        var rectyvalue=0;
//        var yLegendvalue=0;
//        var rectyvalue1=0;
//        var len = parseInt(width-150);
//        var rectlen = parseInt(width-200);
//        var fontsize = parseInt(width/45);
//        var fontsize1 = parseInt(width/50);
//        var rectsize = parseInt(width/60);
//                var keys1 =  Object.keys(colorGroup);
//                 var legendLength;
//                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
//                    legendLength=chartData[div]["legendNo"];
//                  }else{
//                      legendLength=(keys1.length<15?keys1.length:15);
//                      }
//
//               if(keys1.length>7 && keys1.length<10){
//
//        yvalue = parseInt((height * .37));
//        yLegendvalue = parseInt((height * .3));
//        rectyvalue = parseInt((height * .33));
//}else if(keys1.length>10){
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
//  if(fontsize1>15){
//                  fontsize1 = 13;
//                }else if(fontsize1<7){
//                  fontsize1 = 7;
//                }
//  if(fontsize>15){
//                  fontsize = 15;
//                }else if(fontsize1<7){
//                  fontsize = 9;
//                }
//           if($("#chartType").val()=="India-Map-Dashboard"){
//           fontsize1 = 10;
//           rectsize = 8;
//       }
//        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
//            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
//        }
//                 var displayLegends = chartData[div]["displayLegends"];
//                        if(typeof displayLegends==="undefined" || displayLegends==""|| displayLegends=="Yes"){
//
//        var count = 0;
//        var transform =
//
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//            .attr("style","margin-right:10")
//
//             .attr("style", "font-size:"+fontsize+"px")
////.attr("transform", "translate(" + mainWidth*1.52  + "," + yLegendvalue + ")")
//.attr("transform", "translate(" +(width- margin.left - margin.right)  + "," + yLegendvalue + ")")
//
//            .attr("fill", "Black")
//        .text(function(d){
//    if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
//              return colName[1] ;
//    }else{
//                 return ""
//    }
//            })
//             .attr("svg:title",function(d){
//               return  colName[1];
//           })
//
//           for(var m=0;m<legendLength;m++){
//             //  alert("yes=="+m)
//            if(m!=0){
//            yvalue = parseInt(yvalue+25)
//            rectyvalue = parseInt(rectyvalue+25)
//            }
//               if(colName[0]!==colName[1] && typeof colName[1]!="undefined" && columns[1] != null){
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("rect")
//            .attr("style","margin-right:10")
//            .attr("transform", transform)
//            .attr("style", "overflow:scroll")
//
////            .attr("x",rectlen)
////            .attr("y",rectyvalue)
////            .attr("transform", "translate(" + mainWidth*1.48  + "," + rectyvalue + ")")
//            .attr("transform", "translate(" + (width- margin.left - margin.right-10)  + "," + rectyvalue + ")")
//            .attr("width", rectsize)
//            .attr("height", rectsize)
////            .attr("fill", color(i))
//            .attr("fill", function(){
//                return colorGroup[keys1[m]];
//            })
//
////            .style("stroke", color(i))
//          //  .attr("dy",dyvalue )
//               }
//            svg.append("g")
//         //   .attr("class", "y axis")
//            .append("text")
//          //  .attr("style","margin-right:10")
//
//          //  .attr("transform", transform)
////            .attr("x",len)
////            .attr("y",yvalue)
////            .attr("transform", "translate(" + mainWidth*1.52  + "," + yvalue + ")")
//            .attr("transform", "translate(" +(width- margin.left - margin.right+rectsize+(rectsize*.25))  + "," + yvalue + ")")
////            .attr("fill", color(i))
//
//            .attr("fill", function(){
//                return colorGroup[keys1[m]];
//            })
//            .attr("style", "font-size:"+fontsize1+"px")
////            .style("stroke", color(i))
//        //    .attr("dy",dyvalue )
//            .attr("id",function(d){
//                return keys1[m];
//            } )
////            .text("" + measureArray[i] + "");
//            .text(function(d){
//
//                if(keys1[m]!="undefined"){
//if(keys1[m].length>10){
//                    return keys1[m].substring(0, 9)+"...";
//    }else {
//                    return keys1[m];
//    }
//     }else{
//return  "";
//     }
//           })
//           .attr("svg:title",function(d){
//               return  keys1[m];
//           })
//         //   .style("font-size",""+fontsize+"")
//           .on("mouseover", function(d, j) {
//            setMouseOverEvent(this.id,div)
//                    })
//           .on("mouseout", function(d, j) {
//
//            setMouseOutEvent(this.id,div)
//
//                    })
//              count++;
//}
//}
}



function buildScatterPlotGraph(div,divId, data, columns, measureArray, width, height,layout){
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
    var color = d3.scale.category12();
    var colorCount=0
    var dashletid;
    var colName;
    var colIds;
    var columnsVar = columns;
    var colorGroup = {};
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
//    if (columns.length < 2) {
//        columns.push(columns[0]);
//    }
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
    .enter()
    .append("circle")
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

                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    } else{
                        if(typeof columnsVar[1]=="undefined" || columns[1] == null){
                        if(typeof colorGroup[d[columns[1]]]!="undefined" ){
                            return colorGroup[d[columns[1]]]
                        }else{
                            colorCount++;
                        colorGroup[d[columns[1]]]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                }
                    }
                        else{

                        if(typeof colorGroup[d[columns[0]]]!="undefined" ){
                            return colorGroup[d[columns[0]]]
                        }else{
                            colorCount++;
                        colorGroup[d[columns[0]]]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                        }
                    }
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
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[0]]) !== -1) {
                        return drillShade;
                    } else{
                        if(typeof columnsVar[1]=="undefined" || columns[1] == null){
                        if(typeof colorGroup[d[columns[1]]]!="undefined" ){
                            return colorGroup[d[columns[1]]]
                        }else{
                            
                        colorGroup[d[columns[1]]]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount++);
                }
                    }
                        else{

                        if(typeof colorGroup[d[columns[0]]]!="undefined" ){
                            return colorGroup[d[columns[0]]]
                        }else{
                            colorCount++;
                        colorGroup[d[columns[0]]]=color(colorCount);
                         groupColor = color(colorCount);
                        return color(colorCount);
                        }
                    }
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


function show_details2(d, columns, measureArray, element,div) {
    d3.select(element).attr("stroke", "grey");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var content = "";
    if(chartData[div]["chartType"]==="Scatter-XY"){
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + columns[0] + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + d[columns[0]] + "</span><br/>";
    }else{
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + columns[0] + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + d[columns[0]] + "</span><br/>";
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + columns[1] + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + d[columns[1]] + "</span><br/>";
    }
  
    for (var i = 0; i < measureArray.length; i++) {
        if(i==2)
            break;
        var name = checkMeasureNameForGraph(measureArray[i]);
        var msrData;
        if (isFormatedMeasure) {
            msrData = numberFormat(d[measureArray[i]], round, precition);
        }else {
            msrData = addCommas(numberFormat(d[measureArray[i]],yAxisFormat,yAxisRounding,div));
        }
        if (typeof columnMap[measureArray[i]] !== "undefined" && columnMap[measureArray[i]]["displayName"] !== "undefined") {
            var meaName=measureArray[i];
            if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
            content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + " <b>:</b> "+ d[columns[0]] +"</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray[i] + "</span>:<span class=\"value\"> " + msrData + "</span><br/>";
            }
    }
        else {
            var meaName=measureArray[i];
            if(columns==columns[0]){
               if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + "</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray[i] + "</span>:<span class=\"value\"> " + msrData + "</span><br/>";
            } 
            }else{
              if(typeof parent.timeMapValue !=="undefined" &&  parent.timeMapValue !==""){
                content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span>:<span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[i] + "</span><br/>";
            }else {
               content += "<span class=\"name\">" + measureArray[i] + "</span>:<span class=\"value\"> " + msrData + "</span><br/>";
            }  
            }
           
        }
    }
    return tooltip.showTooltip(content, d3.event);
}

function build2WayBarTable(chartId,data, currdata, columns, measureArray, divwidth, divHght,KPIresult){
 var measure1 = measureArray[0];
 var measure2 = measureArray[0];
 var chartData = JSON.parse(parent.$("#chartData").val());
 var colIds = chartData[chartId]["viewIds"];
 if(measureArray.length>1){
     measure2 = measureArray[1];
 }
 var div = chartId;
 graphProp(div);
 measureArray = [];
 measureArray.push(measure1);
 measureArray.push(measure2);
  var trWidth = divwidth / 8;
        divHght = divHght-50;
          var fillBackground="";
         if (typeof chartData[div]["fillBackground"]!=="undefined"){
          fillBackground=chartData[div]["fillBackground"];
        }else{
            fillBackground="#FFF";
        }
 var htmlstr = "";
  htmlstr +=  "<div class='hiddenscrollbar' id=\"" + chartId + "tablediv\"  style=\"max-height:" + divHght + "px; width:100%\">";
  htmlstr = htmlstr + "<div class='innerhiddenscrollbar' style='height:400px;width:100%;margin-left:auto;margin-top:auto'>";
 
 htmlstr = htmlstr + "<table id=\"chartTable" + div + "\" class='table table-condensed' width=\"" + (divwidth) + "\" align=\"center\" style=\"height: auto;font-size:10px;\">";
//htmlstr+="<tr><td><div id = 'mapTable' class='innerhiddenscrollbar' style ='overflow-y:auto;overflow-x:none;height:350px;margin-right:10px'>"
//htmlstr += "<table style='width:100%' class = 'table table-condensed table-stripped ' style = 'float:right'>";
htmlstr += "<tr align='center' style='background-color:whitesmoke;color:black;'>";
//for(var i in columns){
//htmlstr += "<th ></th>";
// for table
var width = $(window).width();
 var columnInd = columns[0];
var minMaxMap = {};
    for (var k = 0; k < measureArray.length; k++) {
//        dataTa/ble += "<td align='left'><span style='font-size:12px;font-weight:bold;cursor:pointer;' onclick='changeMeasureArray(\""+k+"\")'>" + measureArray[k] + "</span></td>";
        var max = maximumValue(data[chartId], measureArray[k]);
        var min = minimumValue(data[chartId], measureArray[k]);
        var temp = {};
        temp["min"] = min;
        temp["max"] = max;
        minMaxMap[measureArray[k]] = temp;
    }
    var wid = parseFloat(width) * (80 / 100) - 140;
    var tdwid = wid / (measureArray.length);
if(measureArray[1].length > 25){
htmlstr += "<th title='"+measureArray[1]+"' style='white-space:nowrap;font-weight:bold;cursor:pointer;text-align:left'>"+measureArray[1].substring(0,15)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:right'>"+measureArray[1]+"</th>";
}
  htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' ></th>";  

for(var q in columns){
if(columns[0].length > 20){
htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q].substring(0, 20)+"..</th>";
}else {
htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' >"+columns[q]+"</th>";
}
}
htmlstr += "<th nowrap style='white-space:nowrap;font-weight:bold;text-align:left' ></th>";
if(measureArray[0].length > 25){
htmlstr += "<th title='"+measureArray[0]+"' style='white-space:nowrap;font-weight:bold;cursor:pointer;text-align:left'>"+measureArray[0].substring(0,15)+"..</th>";
}else {
htmlstr += "<th style='white-space:nowrap;font-weight:bold;text-align:left'>"+measureArray[0]+"</th>";
}


htmlstr += "</tr>";
  var colorValue = d3.scale.category10();
  
//  alert(JSON.stringifycurrdata)
data[chartId].forEach(function(d, i) {
           var drawColor=getDrawColor("chart1", parseInt(0));    
         var num = (i + 1) % 2;
            var color;
            if (num === 0) {
                color = "white";
            } else {
                color = "white";
            }
            var indexValue = "index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var classValue = "bars-Bubble-index-"+d[columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
            var barColor = "green";
            var barColor1 = "red";
//            if(d[measureArray[0]]>d[measureArray[1]]){
//              barColor = "green";  
//            }else if(d[measureArray[1]]>d[measureArray[0]]){
//                barColor = "red";
//            }else{
//                barColor = "green";
//            }
            htmlstr = htmlstr + "<tr style=\"background-color:" + fillBackground + ";\" >";
//                <td  style=\"background-color:" + color + ";\" width=25px>"  ;
//            htmlstr += '<svg width="25" height="15"><circle class="'+classValue+'" cx="15" cy="10" r="5" index_value="'+indexValue+'" color_value="'+drawColor+'" stroke="" onmouseover="tableGraphShow(\''+d[columns[0]] +'\')" onmouseout="tableGraphHide(\''+d[columns[0]] +'\')" stroke-width="3" fill="'+drawColor+'" /></svg>';
//            htmlstr += "</td>";
          // for second measure    
          
           
          var scale2 = d3.scale.linear().domain([minMaxMap[measureArray[1]]["max"], minMaxMap[measureArray[1]]["min"]]).range(["90%", "1%"]);
//    dataTable +="<td>"+displayData[i][measureArray[k]]+"</td>";
          
           var wid2 = scale2(d[measureArray[1]]);
       
            if (parseFloat(wid2.replace("%", "")) < 1) {
                wid2 = 1+"%";
            }
            if (parseFloat(wid2.replace("%", "")) > 100) {
                wid2 = 100+"%";
            }
//            if(j==0){
        if(parseFloat(d[measureArray[0]])>parseFloat(d[measureArray[1]])){
            htmlstr += "<td nowrap height='15px' style='transform:rotate(180deg)'><canvas id='" + data[chartId][i][columnInd] + "_" + measureArray[1].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[1] + ":" + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(d[measureArray[1]],yAxisFormat,yAxisRounding,chartId)) + "' style='width:"+wid2+";height:18px;margin-left:5px;margin-right:5px;background:" + barColor1 + "' onmouseove=''></canvas></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
        }else{
             htmlstr += "<td nowrap height='15px' style='transform:rotate(180deg)'><canvas id='" + data[chartId][i][columnInd] + "_" + measureArray[1].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[1] + ":" + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(d[measureArray[1]],yAxisFormat,yAxisRounding,chartId)) + "' style='width:"+wid2+";height:18px;margin-left:5px;margin-right:5px;background:" + barColor + "' onmouseove=''></canvas></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
        }
         htmlstr = htmlstr + "<td nowrap style=\"background-color:" + fillBackground + ";cursor:pointer;\" width=" + trWidth*.20 + " >" + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(d[measureArray[1]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
//            }else {
//            htmlstr = htmlstr + "<td  style=\"background-color:" + fillBackground + ";text-align:left;color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(d[measureArray[1]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
//            } 
                  
           
           var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                    } catch (e) {
                    }
              
                   for(var l=0;l<columns.length;l++){
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(d[columns[l]]) !== -1) {

                           htmlstr = htmlstr + "<td nowrap id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + drillShade + ";cursor:pointer;color:black\" width=" + trWidth + " >" + d[columns[l]] + "</td>";

               }else{
                  
                               htmlstr = htmlstr + "<td nowrap id='"+d[columns[l]]+":"+d[measureArray[0]]+"' onclick='drillWithinchart(this.id,\""+chartId+"\")'  style=\"background-color:" + fillBackground + ";cursor:pointer;\" width=" + trWidth + " ><u>" + d[columns[l]] + "</u></td>";
               }
                    }
                
            htmlstr = htmlstr + "<td nowrap style=\"background-color:" + fillBackground + ";cursor:pointer;\" width=" + trWidth*.20 + " >" + addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,chartId)) + "</td>";    
                  // for first measure value bar 
//            for(var j=0;j<measureArray.length;j++){
               var scale = d3.scale.linear().domain([minMaxMap[measureArray[0]]["max"], minMaxMap[measureArray[0]]["min"]]).range(["90%", "1%"]);
//    dataTable +="<td>"+displayData[i][measureArray[k]]+"</td>";
            var wid = scale(d[measureArray[0]]);
       
            if (parseFloat(wid.replace("%", "")) < 1) {
                wid = 1+"%";
            }
            if (parseFloat(wid.replace("%", "")) > 100) {
                wid = 100+"%";
            }
//            if(j==0){
            if(parseFloat(d[measureArray[0]])>parseFloat(d[measureArray[1]])){
           htmlstr += "<td nowrap height='15px'><canvas id='" + data[chartId][i][columnInd] + "_" + measureArray[0].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[0] + ":" + addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,chartId)) + "' style='width:"+wid+";height:18px;margin-left:5px;margin-right:5px;background:" + barColor + "' onmouseove=''></canvas></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
            }else{
               htmlstr += "<td nowrap height='15px'><canvas id='" + data[chartId][i][columnInd] + "_" + measureArray[0].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "' title='" + measureArray[0] + ":" + addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(d[measureArray[0]],yAxisFormat,yAxisRounding,chartId)) + "' style='width:"+wid+";height:18px;margin-left:5px;margin-right:5px;background:" + barColor1 + "' onmouseove=''></canvas></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%) 
            }
//            }else {
//            htmlstr = htmlstr + "<td  style=\"background-color:" + fillBackground + ";text-align:left;color:black;font-size:smaller\" width=" + trWidth + ">" + addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(d[measureArray[1]],yAxisFormat,yAxisRounding,chartId)) + "</td>";
//            }
            // bar html end
//            htmlstr += "<td height='15px'><canvas id='m' title='" + measureArray[j] + ":" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding)) + "' width='" + wid + "' height='18' style='margin-left:5px;margin-right:5px;background:" + colorValue(j) + "' onmouseove=''></canvas><span style='font-size:10px;float:'>" + addCommas(numberFormat(d[measureArray[j]],yAxisFormat,yAxisRounding)) + "</span></td>";//width='"+parseFloat(tdwid-2)+"'  //linear-gradient(to right,"+color(k)+" 0%,  rgb(250,250,250) 85%)
        
//       var canvas = document.getElementById(data[chartId][i][columnInd] + "_" + measureArray[j].replace(/[^a-zA-Z0-9]/g, '', 'gi'));
       
         
//        }

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
         htmlstr = htmlstr + "<td  style=\"background-color:" + color + ";text-align:right\" width=" + trWidth + " >" +addCurrencyType(div, getMeasureId(measureArray[no])) + addCommas(KPIresult[no])+ "</td>";
          }
            htmlstr += "</tr>";
        }
        
            

htmlstr += "</table></div></td></tr></table>";
        
//htmlstr += "</div>";
  
        $("#" + chartId).html(htmlstr);
    
    
 }
 
 function buildFrequencyDistributionChart(div, data, columns, measureArray,wid,hgt,divAppend) {
    // Generate a Bates distribution of 10 random variables.
//    data=JSON.parse('[{"Category":"Computer Hardware","Total Orders":"200"},{"Category":"Computer Hardware","Total Orders":"200"},{"Category":"Computer Hardware","Total Orders":"200"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"400"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"700"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Computer Hardware","Total Orders":"1000"},{"Category":"Cameras","Total Orders":"3000"},{"Category":"Mobile Phones","Total Orders":"2000"},{"Category":"Footwear","Total Orders":"4000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Clothing","Total Orders":"2000"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Watches","Total Orders":"1500"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2300"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"2700"},{"Category":"Clothing","Total Orders":"3000"},{"Category":"Clothing","Total Orders":"3000"},{"Category":"Clothing","Total Orders":"3000"},{"Category":"Clothing","Total Orders":"3000"},{"Category":"Clothing","Total Orders":"3500"},{"Category":"Clothing","Total Orders":"3500"}]');
    var chartData=JSON.parse(parent.$("#chartData").val());
    var values = [];
    for(var i in data){
        values.push(data[i][measureArray[0]]);
    }    
    graphProp(div);
    // A formatter for counts.
    var formatCount = d3.format(",.0f");

    var margin = {
        top: 10, 
        right: 15, 
        bottom: 50, 
        left: 15
    },
    width = wid - margin.left - margin.right,
    height = hgt - margin.top - margin.bottom;
    var maxValue=parseFloat(maximumValue(data, measureArray[0]));
    var x = d3.scale.linear()
    .domain([0, maxValue])
    .range([0, width]);

    // Generate a histogram using twenty uniformly-spaced bins.
    var data = d3.layout.histogram()
    .bins(x.ticks(20))
    (values);

    var y = d3.scale.linear()
    .domain([0, d3.max(data, function(d) {
        return numberFormat(d.y,yAxisFormat,yAxisRounding,div);
    })])
    .range([height, 0]);

    var xAxis = d3.svg.axis()
    .scale(x)
    .orient("bottom")
    .tickFormat(function(d) {
        if(typeof displayY !=="undefined" && displayY =="Yes"){
            if(yAxisFormat==""){
                return addCurrencyType(div, chartData[div]["meassureIds"][0])+addCommas(numberFormat(d,yAxisFormat,"2",div));
            }
            else{
                return numberFormat(d,yAxisFormat,"2",div);
            }
        }else {
            return "";
        }
    });

    var svg = d3.select("#"+div).append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    var bar = svg.selectAll(".bar")
    .data(data)
    .enter().append("g")
    .attr("class", "bar")
    .attr("transform", function(d) {
        return "translate(" + x(d.x) + "," + y(d.y) + ")";
    });

    bar.append("rect")
    .attr("x", 1)
    .attr("width", x(data[0].dx) - 1)
    .attr("height", function(d) {
        return height - y(d.y);
    })
    .attr("fill",getDrawColor(div, 0));

    bar.append("text")
    .attr("dy", ".75em")
    .attr("y", 6)
    .attr("x", x(data[0].dx) / 2)
    .attr("text-anchor", "middle")
    .text(function(d) {
        if(formatCount(d.y)==0){
            return "";
        }
        return formatCount(d.y);
    })
    .attr("fill", function(d,i){
        var LabFtColor=[];
        for(var c in measureArray){
            if(typeof chartData[div]["LabFtColor"]!='undefined' && typeof chartData[div]["LabFtColor"][measureArray[c]]!='undefined' && chartData[div]["LabFtColor"][measureArray[c]]!='undefined'){
                LabFtColor= chartData[div]["LabFtColor"][measureArray[c]]                   
            }else {
                LabFtColor = "#ffff00";
            }
            return LabFtColor;
        }
    });

    svg.append("g")
    .attr("class", "x axis")
    .attr("transform", "translate(0," + height + ")")
    .call(xAxis);
}

function buildDoubleDonut(div, data, columns, measureArray, divWid, divHgt, rad) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var absoluteValue = chartData[div]["absoluteValue"];
    if(typeof absoluteValue !=="undefined" && absoluteValue==="Yes"){
  data = getAbsoluteValue(data,columns,measureArray);
    }
     var width = divWid;
    var heigth = divHgt;
     var radius = rad;
     var color = d3.scale.category10();
     var widthdr=divWid
    var divHgtdr=divHgt
    var mrgintop;
    var dashletid;
//divWid=parseFloat($(window).width())*(.35);

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

 var isDashboard = parent.$("#isDashboard").val();
  var  fromoneview=parent.$("#fromoneview").val();
  var chartData = JSON.parse(parent.$("#chartData").val());
  var colIds= [];
     var div1=parent.$("#chartname").val()
     if(fromoneview!='null'&& fromoneview=='true'){
     var prop = graphProp(div1);
     dashletid=div;
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
var fun="";
	hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());	
	if(hasTouch){
		fun="";
                
        }else{
             var fun = "drillWithinchart(this.id,\""+div+"\")";
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
	
    var sum = d3.sum(data, function(d) {
        return d[measureArray[0]];
    });
    var sum1 = d3.sum(data, function(d) {
        return d[measureArray[1]];
    });
    var measure1 = measureArray[0];
    var measure2 = measureArray[1];
    var autoRounding1;
    var autoRounding2;
     if(fromoneview!='null' && fromoneview=='true'){

     }else{
    if (columnMap[measure1] !== undefined && columnMap[measure1] !== "undefined" && columnMap[measure1]["rounding"] !== "undefined") {
        autoRounding1 = columnMap[measure1]["rounding"];
    } else {
        autoRounding1 = "1d";
    }
    if (columnMap[measure2] !== undefined && columnMap[measure2] !== "undefined" && columnMap[measure2]["rounding"] !== "undefined") {
        autoRounding2 = columnMap[measure2]["rounding"];
    } else {
        autoRounding2 = "1d";
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
    var ramswap_w = divWid,
            ramswap_h = divHgt,ramswap_r;

            if(width<height)
            {
                ramswap_r = Math.min(ramswap_w, ramswap_h) / 2.6;
            }
            else
            {
                ramswap_r = Math.min(ramswap_w, ramswap_h) /2.7;
            }
            if(typeof chartData[div]["legendLocation"]==='undefined' || chartData[div]["legendLocation"]==='Right'){
                ramswap_r *= 1.2;
                if(ramswap_r*2>width*.68){
                    ramswap_r=(width*.68)/2-18;
                }
            }
//                rad=divWid*.42;
               var rad=(Math.min((width*.5), height))*.50;
            ram_arc = d3.svg.arc().outerRadius(ramswap_r * 0.95),
            swap_arc = d3.svg.arc().outerRadius(ramswap_r * 0.18),
            ram_donut = d3.layout.pie();
    swap_donut = d3.layout.pie();

    var data1 = [];
    var data2 = [];
    var label = [];
    var arcFinal = d3.svg.arc().innerRadius(ramswap_r * 0.8).outerRadius(ramswap_r *1.6);
    var arcFinal1 = d3.svg.arc().innerRadius(ramswap_r * 0.8).outerRadius(ramswap_r / 150);
    var arcFinal11 = d3.svg.arc().innerRadius(ramswap_r * 0.5).outerRadius(ramswap_r / 150); // add by mayank sh. for inner value 
    var arcFinal2 = d3.svg.arc().innerRadius(ramswap_r * 0.8).outerRadius(ramswap_r * 0.5);
    data.forEach(function(d) {
        label.push(d[columns[0]]);
        data1.push(d[measureArray[0]]);
        data2.push(d[measureArray[1]]);
    });
    function ram_tweenPie(b) {
        b.innerRadius = 0;
        var i = d3.interpolate({
            startAngle: 0,
            endAngle: 0
        }, b);
        return function(t) {
            return ram_arc(i(t));
        };
    }
    function ram_tweenDonut(b) {
        b.innerRadius = ramswap_r * .47;
        var i = d3.interpolate({
            innerRadius: 0
        }, b);
        return function(t) {
            return ram_arc(i(t));
        };
    }
    function swap_tweenPie(b) {
        b.innerRadius = 0;
        var i = d3.interpolate({
            startAngle: 0,
            endAngle: 0
        }, b);
        return function(t) {
            return swap_arc(i(t));
        };
    }
    function swap_tweenDonut(b) {
        b.innerRadius = ramswap_r * .49;
        var i = d3.interpolate({
            innerRadius: 0
        }, b);
        return function(t) {
            return swap_arc(i(t));
        };
    }

    // Stash the old values for transition.
    function stash(d, i) {
        d.startAngle0 = d.startAngle;
        d.endAngle0 = d.endAngle;
    }

    function angle(d) {
        var a = (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
//        if (!(typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")))
//            a = a > 90 ? a + 180 : a;
        return a;
    }

    var svg =
            ram_arcs_group = ram_arcs = ram_paths =
            ram_txt1 = ram_txt2 =
            swap_arcs_group = swap_arcs = swap_paths =
            swap_txt1 = swap_txt2 = null;
    create_ramswap();
    var offset;
    var lableColor;
    function create_ramswap() {
        var chartMap = {};
        svg = d3.select("#" + div)
            .append("svg")
            .attr("viewBox", "0 0 "+(ramswap_w)+" "+(ramswap_h )+" ")
            .classed("svg-content-responsive", true)
                .attr("id", "svg_" + div);

        var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
                .append("svg:linearGradient")
                //    .attr("id", function(d) {return color(d.name);)
                .attr("id", function(d) {
//                    return "gradient_" + div + "_" + (d[columns[0]]).replace(/[^a-zA-Z0-9]/g, '', 'gi');
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

                    return colorShad;
                })
                .attr("stop-opacity", 1);
        parent.$("#colorMap").val(JSON.stringify(colorMap));
        svg.append("svg:rect")
                .attr("width", ramswap_w-180)
                .attr("height", ramswap_h-50)
                .attr("y", "-5")
                .attr("onclick", "reset()")
                 .style("fill", function(d,i){
                    if(typeof chartData[div]["divBackGround"]!=="undefined" && chartData[div]["divBackGround"] !=""){
            return  chartData[div]["divBackGround"]; 
             }else{
                   return 'rgb(255, 255, 255)';
             }
          });
                
                 
        ram_arcs_group = svg.selectAll("g.ram_arcs_data")
                .data([data1])
                .enter().append("svg:g")
                .attr("class", "ram_arcs_data");

        if(legendAlign==='Right')
        {
            offset=width/17;
        }
        else
        {
            offset=width/2-ramswap_r;
        }
	var offset1=divHgt/2-(ramswap_r+heigth/50);
        ram_arcs = ram_arcs_group.selectAll("g.ram_arc")
                .data(ram_donut)
                .enter().append("svg:g")
                .attr("class", "ram_arc")
                .attr("transform", function(d,i){
                    if(legendAlign==='Right'){ 
                        return "translate(" + (ramswap_r+offset) + "," + (ramswap_r+heigth/50+offset1-divHgt/30) + ")";
                    }else{
                       return "translate(" + (ramswap_r+offset) + "," + (ramswap_r+heigth/50+offset1-divHgt/30-25) + ")"; 
                    }
                 })
                .attr("id", function(d, i) {
                    return label[i];
                })
                .attr("onclick", fun)
        .dblTap(function(e,id) {
		drillFunct(id);
	}) 
        
                .on("mouseover", function(d, i) {
                    d3.select(this).attr("stroke", "grey");
                    var content='';//Added by shivam
                    var msrData;

            if (typeof chartData[div]["toolTip"] === "undefined" || chartData[div]["toolTip"] === "Absolute") {
//                        msrData = addCommas(data1[i]);
                        msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data1[i],yAxisFormat,yAxisRounding,div));//Added by shivam
//                        alert("0");
            }else if(typeof chartData[div]["toolTip"] != "undefined"   && chartData[div]["toolTip"] != "Absolute" && (yAxisFormat === "Absolute" ||yAxisFormat === "")){

               msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(data1[i]);
//        alert("1");
                    }
            else{

                 msrData = addCurrencyType(div, getMeasureId(measureArray[0])) + addCommas(numberFormat(data1[i],yAxisFormat,yAxisRounding,div));
//                 alert("2");
            }
//                    if (isFormatedMeasure) {
//                        msrData = numberFormat(data1[i], round, precition);
//                    }
//                    else {
//                        msrData = addCommas(data1[i]);
//                    }
//                    content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + label[i] + "</span><br/>";
                    content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " + measureArray[0] + " <b>:</b> " + label[i] + "</span><br/>";//Added by shivam
                    return tooltip.showTooltip(content, d3.event);

                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });
 if(fromoneview!='null'&& fromoneview=='true'){
  div=div1;
 }
        ram_paths = ram_arcs.append("svg:path")
                .attr("class",function(d,i){
                    return "bars-Bubble-index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                })
                .attr("fill", function(d,i) { // add for graph color pattern by mayank sh.
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }
					
                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }

                })
                .attr("color_value", function(d,i) { // add for graph color pattern & highlight by mayank sharma
                var drilledvalue;
                    try {
                        drilledvalue = JSON.parse(parent.$("#drills").val())[colIds[0]];
                        drillClick = drilledvalue;
                    } catch (e) {
                    }

                    if (typeof drilledvalue !== 'undefined' && drilledvalue.length > 0 && drilledvalue.indexOf(data[columns[0]]) !== -1) {
                        return drillShade;
                    }
                else{
		var colorfill = getcolorValueFunction(div,chartData,drillShade,data,columns,measureArray,i,color)
                return colorfill;
                }
                })
                .attr("index_value", function(d, i) {
                return "index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
               })
               
               .on("mouseover", function(d, i) {
                var columnList = [];
                columnList.push(columns[0]);
                 if(fromoneview!='null'&& fromoneview=='true'){
                 show_detailsoneview(data, columnList, measureArray[i], this,chartData,div1);
                 }else{
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                 }
                show_details(data, columnList,measureArray[i], this,div);

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
            });  // end by mayank sharma
                
        ram_arcs.filter(function(d) {
            return d.endAngle - d.startAngle > 0.2;
        })
                .append("svg:text")
                .attr("dy", ".35em")
                .attr("text-anchor","middle")
                .attr("class","gFontFamily")
                .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
            })
                .style("fill", function(d,i){
                 var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                               }
                               return lableColor;
            })
                .attr("transform", function(d) {
                   d.outerRadius = ramswap_r;
                      var a = angle(d);
//                       d.innerRadius = ramswap_r * 0.00001
//                    return "translate(" + arcFinal2.centroid(d) + ")rotate(" + angle(d) + ")";
                  if (a > 90) {
                    a = a + 180;

                    d.innerRadius = ramswap_r /4;

                } else {

                    d.innerRadius = ramswap_r /1.3;

                }
              return "translate(" + arcFinal2.centroid(d) + ")rotate(" + a + ")";
//
                })
                     .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes'){
if(typeof chartData[div]["dDLable"]=== "undefined" || chartData[div]["dDLable"] === "Default" || chartData[div]["dDLable"] === "OuterValue") {
if (typeof displayType !== "undefined" && displayType === "Absolute") {
                        return numberFormat(d.value,yAxisFormat,yAxisRounding,div);
                }else {
                   var percentage = (d.value / parseFloat(sum)) * 100;
                   return percentage.toFixed(1) + "%"; }
            }else{
             return "";
                }
            }else {
                return "";
            }
            });
        var center_group = svg.append("svg:g")
                .attr("class", "ctrGroup")
                .attr("transform", "translate(" + (width / 1.5) + "," + (heigth / 2) + ")");
        var pieLabel = center_group.append("svg:text")
                .attr("dy", "-5em")
                .attr("text-anchor", "middle")
                .attr("class","gFontFamily")
                .attr("style", "font-size: 12px")
//                .text("" + measureArray[0] + "");
        center_group.append("svg:text")
                .attr("dy", "-4em")
                .attr("text-anchor", "middle")
                .attr("class","gFontFamily")
                .attr("style", "font-size: 12px")
//                .text("" + autoFormating(JSON.parse(parent.$("#sumValue").val()), autoRounding1) + "");
                .text("" );
        ram_paths.transition()
                .duration(1500)
                .attrTween("d", ram_tweenPie);

        ram_paths.transition()
                .ease("elastic")
                .delay(function(d, i) {
                    return 1200 + i * 50;
                })
                .duration(1500)
                .attrTween("d", ram_tweenDonut)
                .each(stash);

        if (measureArray.length > 1)
            setTimeout(create_swap, 1500);
    }

    function create_swap() {

        swap_arcs_group = svg.selectAll("g.swap_arcs_data")
                .data([data2])
                .enter().append("svg:g")
                .attr("class", "swap_arcs_data");
	var offset1=divHgt/2-(ramswap_r+heigth/50);
        swap_arcs = swap_arcs_group.selectAll("g.swap_arc")
                .data(swap_donut)
                .enter().append("svg:g")
                .attr("class", "swap_arc")
                .attr("transform", function(d,i){
                    if(legendAlign==='Right'){
                        return "translate(" + (ramswap_r+offset) + "," + (ramswap_r+heigth/50+offset1-divHgt/30) + ")";
                    }else{
                       return "translate(" + (ramswap_r+offset) + "," + (ramswap_r+heigth/50+offset1-divHgt/30-25) + ")"; 
                    }
                 })
                .attr("id", function(d, i) {
                    return label[i];
                })

                .attr("onclick", fun)
        .dblTap(function(e,id) {
		drillFunct(id);
	}) 
                .on("mouseover", function(d, i) {
                    d3.select(this).attr("stroke", "grey");
                    var content='';//Added by shivam
                    var msrData;
                    if (isFormatedMeasure) {
                        msrData = numberFormat(data2[i], round, precition,div);
//                        alert("3");
                    }
                    else {
//                        msrData = addCommas(data2[i]);
                      msrData = addCurrencyType(div, getMeasureId(measureArray[1])) + addCommas(numberFormat(data2[i],yAxisFormat,yAxisRounding,div));//Added by shivam
                    }
//                    content = "<span class=\"name\">" + columns[0] + ":</span><span class=\"value\"> " + label[i] + "</span><br/>";
                    content += "<span style=\"font-family:helvetica;\" class=\"name\">" + msrData + "</span><span style=\"font-family:helvetica;\" class=\"value\"> " +  measureArray[1] + " <b>:</b> " + label[i] + "</span><br/>";//Added by shivam
                    return tooltip.showTooltip(content, d3.event);

                })
                .on("mouseout", function(d, i) {
                    hide_details(d, i, this);
                });

        if (measureArray.length > 1) {
            var center_group = svg.append("svg:g")
                    .attr("class", "ctrGroup")
                    .attr("transform", "translate(" + (width / 1.5) + "," + (heigth / 1.9) + ")");
            var pieLabel = center_group.append("svg:text")
                    .attr("dy", "-2em")
                    .attr("text-anchor", "middle")
                    .attr("class","gFontFamily")
                    .attr("style", "font-size: 12px")
//                    .text("" + measureArray[1] + "");
            center_group.append("svg:text")
                    .attr("dy", "-1em")
                    .attr("text-anchor", "middle")
                    .attr("class","gFontFamily")
                    .attr("style", "font-size: 12px")
//                    .text("" + autoFormating(JSON.parse(parent.$("#sumValue1").val()), autoRounding2) + "");
                    .text("");

        }
        swap_paths = swap_arcs.append("svg:path")
.attr("fill", function(d,i) {  // add for graph color pattern by mayank sh.
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
.attr("color_value", function(d,i) {  // add for graph color pattern by mayank sh.
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
                return "index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi');
               })
                .attr("class",function(d,i){
                    return "bars-Bubble-index-" + data[i][columns[0]].replace(/[^a-zA-Z0-9]/g, '', 'gi')+div;
                })
                .on("mouseover", function(d, i) {
                d3.select(this).attr("stroke", "black");
                var columnList = [];
                columnList.push(columns[0]);
                 if(fromoneview!='null'&& fromoneview=='true'){
                 show_detailsoneview(data, columnList, measureArray[i], this,chartData,div1);
                 }else{
                var bar = d3.select(this);
                    var indexValue = bar.attr("index_value");
                    var barSelector = "." + "bars-Bubble-" + indexValue+div;
                    var selectedBar = d3.selectAll(barSelector);
                    selectedBar.style("fill", drillShade);
                 }
                show_details(data, columnList,measureArray[i], this,div);

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
        swap_arcs.append("svg:text")
                .filter(function(d) {
                    return d.endAngle - d.startAngle > 0.2;
                })
                .attr("dy", ".35em")
                .attr("text-anchor", "end")
                .attr("class","gFontFamily")
                .style("font-size",function(d, i){
              if(typeof chartData[div]["labelFSize"]!=='undefined' &&  chartData[div]["labelFSize"]!=="Select"){
                 return (chartData[div]["labelFSize"]+"px");
          }else{
                 return "10px";
              }
            })
                 .style("fill", function(d,i){
                     var lableColor;
                   if (typeof chartData[div]["labelColor"]!=="undefined") {
                              lableColor = chartData[div]["labelColor"];
                          }else {
                               lableColor = "#000000";
                           }
                               return lableColor;
            })
//                if(color(i) == "#000000" || color(i)  == "#3F3F3F")
//                return "white"
//            else
//                return "black"
//            })
                .attr("transform", function(d) {
                    d.outerRadius = ramswap_r / 7;
                    d.innerRadius = ramswap_r / 7;
                    var ddval = angle(d);
                    if (ddval > 90) {  // add by mayank sh. for inner value
                   return "translate(" + arcFinal11.centroid(d) + ")rotate(" + (ddval+180) + ")";
                  }else{
                   return "translate(" + arcFinal1.centroid(d) + ")rotate(" + ddval + ")";
                  }
                })
                 .text(function(d) {
//                var percentage = (d.value / parseFloat(sum)) * 100;
//                return percentage.toFixed(1) + "%";
if(typeof chartData[div]["dataDisplay"]==='undefined'||chartData[div]["dataDisplay"]==='Yes')  {
if(typeof chartData[div]["dDLable"]=== "undefined" || chartData[div]["dDLable"] === "Default" || chartData[div]["dDLable"] === "InnerValue"){
if (typeof displayType !== "undefined" && displayType === "Absolute") {
                        return numberFormat(d.value,yAxisFormat,yAxisRounding,div);
                } else {
                   var percentage = (d.value / parseFloat(sum1)) * 100;
                return percentage.toFixed(1) + "%";}
        }else{
    return "";}
            }else {
                return "";
            }
            });
        swap_paths.transition()
                .duration(1500)
                .attrTween("d", swap_tweenPie);

        swap_paths.transition()
                .ease("elastic")
                .delay(function(d, i) {
                    return 1200 + i * 50;
                })
                .duration(1500)
                .attrTween("d", swap_tweenDonut);


    }
    
    if(width < 175 || height < 175){
 }else{
    
    var displayLength = chartData[div]["displayLegends"]
                var yvalue=0;
        var rectyvalue=0;
        var rectyvalue1=0;
        var len = parseInt(width-150);
        var rectlen = parseInt(width-200);
        var fontsize = parseInt(width/45);
        var fontsize1 = parseInt(width/50);
        var rectsize = parseInt(width/60);
                var height = divHgt;
                 var legendLength;
                 if(typeof chartData[div]["legendNo"] != 'undefined' && chartData[div]["legendNo"] != ''){
                    legendLength=chartData[div]["legendNo"];
                  }else{
                      legendLength=(data.length<15 ? data.length : 15);
                      }
         if(legendAlign==='Right')
         {
             if(legendLength>12){
                yvalue = parseInt(height / 8.5);
                 rectyvalue = parseInt((height / 10));
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
                yvalue = parseInt(height*.8);
                rectyvalue = parseInt(height*0.78);
            }
            else
            {
                yvalue = parseInt(height*.88);
                rectyvalue = parseInt((height*90 ));
            }
         }
         if(fontsize1>15){
                  fontsize1 = 15;
                }else if(fontsize1<7){
                  fontsize1 = 7;
                }

        var count = 0;
        var transform = "";
        if(typeof chartData[div]["legendFontSize"]!=='undefined' && chartData[div]["legendFontSize"]!="Select"){
            fontsize=fontsize1=parseInt(chartData[div]["legendFontSize"]);
        }
if((typeof displayLength!="undefined" && displayLength=="No")){}else{
if(legendAlign==='Right')
{
            var startY=0;
            if(legendLength>12){
                startY=(height*.05)
            }
            else{
                startY=(height / 2-(legendLength/2)*(height*.06))-(width*.035)
            }
            svg.append("g")
            .append("text")
            .attr("style","margin-right:10")
             .attr("style", "font-size:"+fontsize+"px")
            .attr("transform", "translate(" + width*.76  + "," + parseInt(startY) + ")")
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
//}

               for(var i=0;i<legendLength;i++){
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
                 if(width < 395){ 
                return  "translate(" + width*.71  + "," + (rectyvalue+5) + ")";
        }else{
            return  "translate(" + width*.71  + "," + (rectyvalue) + ")";
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
            .attr("transform", "translate(" + width*.76  + "," + yvalue + ")")
            .attr("fill",function(){
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
                return data[i][columns[0]];
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
            .append("rect")
            .attr("style", "overflow:scroll")
            .attr("transform", "translate(" + widthRectvalue  + "," + (yvalue-yvalue*0.03) + ")")
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
                  return  color(i);
              }
             }else{
                 return  "#000";
             }
            })
            .attr("style", "font-size:"+fontsize1+"px")

            .attr("id",function(d){
            } )
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
}
