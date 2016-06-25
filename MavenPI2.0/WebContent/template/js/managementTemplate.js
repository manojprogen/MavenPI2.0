/* 
    Document   : managementTemplate
    Created on : 19 May, 2016, 11:12:35 AM
    Author     : Faiz Ansari
    Description:
        Purpose of the stylesheet follows.
 */

function showMgtMenu(){
    $("#mgtMenu").slideToggle("fast");
}
function createMgTemKpis(templateIndex,gtValues){
    if(templateIndex === '1'){
        createTem1RightKpis(gtValues);
    }else if(templateIndex === '2'){
        createTem2RightKpis();
    }else if(templateIndex === '3'){
        createTem3RightKpis(gtValues);
    }else {
        createDefaultKpis(gtValues);
    }
}
function createDefaultKpis(gtValues){
    $("#managementTempl").show();
    $("#mgtTem2Left").css({
        "width":"100%",
        "height": "calc(100% - 130px)"
    });
    $("#mgtTem2Right").css({
        "width": "0%",
        "height": "0%",
        "display":"none"
    });
//    var data1=gtValues;
    var measures = ["Brand", "Category", "Store", "State"];
    createKPILeft(3, gtValues, measures);
    createKPIRight(4, gtValues, measures);
    var pageMap = JSON.parse($("#pageMap").val());
    var pageIdMap = JSON.parse($("#pageIdMap").val());
//      var templateId = $("#templateId").val();
    var pages = Object.keys(pageMap);
    var newPageMap = {};
    var list = [];
    var pageIdList = [];
    for (var i in pages) {
        for (var k in pageMap[pages[i]]) {
            list.push(pageMap[pages[i]][k]+":"+pages[i]);
            pageIdList.push(pageIdMap[pages[i]][k]+":"+pages[i]);
}

    }
    createTemplatePages(list,list[0],pageIdList);
}
function createKPILeft(mesuresNo,data,mesureName){
    var apclass='',html='',compare=false;
    var compData="";    
    for(var i=0;i<mesuresNo;i++){
        var alias='Hello';
        var alias2='Hello123';
        var fontColor='green';
        var fontSize='14';
        var compTime;
	var stylePrior='';
	apclass='leftSection'+mesuresNo;
        
        html+="<div class='"+apclass+"'>"+
            "<div class='topSectionV' style='cursor:pointer'>"+
            data[i]+
            "</div>"+
            "<div class='middleSectionV' style='"+stylePrior+";cursor:pointer'>"+
            compData+" ("+compTime+")"+
            "</div>"+
            "<div class='bottomSectionV'>"+
            "<div class='row1'>"+alias+"</div>"+
            "<div class='row2' style='font-size:"+fontSize+"px;color:"+fontColor+"'>"+alias2+"</div>"+
            "</div>"+
            "</div>";
    }
    $("#firstKPI").html('');
    $("#firstKPI").append(html);
     if(compare){
	$("#firstKPI").children().addClass("compare");
	$("#firstKPI>div>div:nth-child(2)").css("display","flex");
}
}
function createKPIRight(mesuresNo,data,mesureName){
    var apclass='',html='',style='';
    var compData="";
    for(var i=3;i<(mesuresNo+3);i++){
        var alias='Hello';
        var alias2='Hello1234';
        var fontColor='red';
        var fontSize='14';
        var compTime;
	var stylePrior='';
	apclass='rightSection'+mesuresNo;
        if((mesuresNo===2 && i===4) || (mesuresNo===3 && i===4) || (mesuresNo===4 && i===5)){
            html+="<div class='dividerH'></div>";
        }
        if((mesuresNo===3 && i===5) || (mesuresNo===4 && i===4 || i===6)){
            html+="<div class='dividerV'></div>";
        }
        html+="<div class='"+apclass+"'>"+
            "<div class='numericContainer'><div class='data'>"+
            data[i]+
            "</div>"+
            "<div class='prior' style='"+stylePrior+"'>"+
	    compData+" ("+compTime+")"+
            "</div></div>"+
            "<div id='details"+i+"' class='details' style='"+style+"'>"+
		"<div class='row1'>"+alias+"</div>"+
            "<div class='row2' style='font-size:"+fontSize+"px;color:"+fontColor+"'>"+alias2+"</div>"+
            "</div>"+
            "</div>";
    }
    $("#secondKPI").html('');
    $("#secondKPI").append(html);
}
function createTem1RightKpis(){
    $("#managementTempl").hide();
    $("#mgtTem2Left").css({
        "width":"70%",
        "height": "calc(100% - 30px)"
    });
    $("#mgtTem2Right").css({
        "width":"28%",
        "height": "calc(100% - 30px)",
        "display":"block"
    });
    var html="";
    for(var i=0;i<2;i++){
        html+="<div class='col-xs-12 t1Kpi rmpm'>";
        html+="<div class='mt2Data'>";
        html+="<table class='dtable table table-striped table-bordered ' cellspacing='0' width='100%'>";
        html+="<thead class='mt2Title'><tr>";
        html+="<td>Brand</td>";
        html+="<td>Gross Sales</td>";
        html+="<td>Change %</td>";
        html+="</tr><thead>";
        html+="<tbody>";
        html+="<tr><td>Lenovo</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
        html+="<tr><td>Sony</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
        html+="<tr><td>HP</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
        html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
                html+="<tr><td>Apple</td>";
        html+="<td>143.7 Mn</td>";
        html+="<td>18%</td></tr>";
        html+="</tbody>";
        html+="</table>";
        html+="</div>";
        html+="</div>";
    }
    $("#mgtTem2Right").addClass("brd");
    $("#mgtTem2Right").html('');
    $("#mgtTem2Right").append(html);
    
    $('.dtable').DataTable( {
        "scrollY":        ($("#mgtTem2Right").children().height() - 20),
        "scrollCollapse": true,
        "paging":   false,
        "info":     false,
        "filter":false
    } );
    scrollBar(".dataTables_scrollBody");

}
function createTem2RightKpis(){
    $("#managementTempl").hide();
    $("#mgtTem2Left").css({
        "width":"70%",
        "height": "calc(100% - 30px)",
    });
    $("#mgtTem2Right").css({
        "width":"28%",
        "height": "calc(100% - 30px)",
        "display":"block"
    });
    var html="";
    for(var i=0;i<5;i++){
        html+="<div class='col-xs-12 t2Kpi brd'>";
        html+="<div id='dialCh"+i+"' style='height:100%;width:40%;float:left;border-right:1px solid #d5d5d5'></div>";
        html+="<div style='height:100%;width:60%;float:left'></div>";
        html+="";
        html+="";
        html+="";
        html+="</div>";
    }
    $("#mgtTem2Right").removeClass("brd");
    $("#mgtTem2Right").html('');
    $("#mgtTem2Right").append(html);
}
function createTem3RightKpis(){
    $("#managementTempl").hide();
    $("#mgtTem2Left").css({
        "width":"70%",
        "height": "calc(100% - 30px)"
    });
    $("#mgtTem2Right").css({
        "width":"28%",
        "height": "calc(100% - 30px)",
        "display":"block"
    });
    var html="";
    for(var i=0;i<5;i++){
        html+="<div class='col-xs-12 t3Kpi'> Data </div>";
    }
    $("#mgtTem2Right").addClass("brd");
    $("#mgtTem2Right").html('');
    $("#mgtTem2Right").append(html);
}
function createTemplatePages(pages,active,pageIds){
    var html="";
    for(var i=0;i<pages.length;i++){
        html+="<div id='"+pages[i]+"' name='"+pageIds[i]+"' onclick='selectedPage(this.id,\""+pageIds[i]+"\")' class='col-xs-3 pages page"+(pages.length)+"'>"+pages[i].split(":")[0]+"</div>";
    }
    $("#pageNavigation").html('');
    $("#pageNavigation").append(html);
    $("#"+active).addClass("pageActive");
}
function selectedPage(id,name){
    $(".pages").removeClass("pageActive");
    $("#"+id).addClass("pageActive");
    var pageChange = name.split(":")[0];
       $.ajax({
     async:false,
     type:'POST',
       url:  $("#ctxpath").val() +'/managementViewer.do?templateBy=getAvailableTemplateCharts&reportId='+name.split(":")[1]+'&pageChange='+pageChange,
         success:function (data) {
              $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                    var meta = JSON.parse(JSON.parse(data)["meta"]);
                    parent.$("#templateMeta").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["templateMeta"]));
                    $("#viewby").val(JSON.stringify(meta["viewbys"]));
                    $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                    $("#measure").val(JSON.stringify(meta["measures"]));
                    $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                    $("#defaultMeasure").val(JSON.stringify(meta["measures"]));
                    $("#defaultMeasureId").val(JSON.stringify(meta["measureIds"]));
                    $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                    $("#drilltype").val((meta["drillType"]));
                    $("#filters1").val(JSON.stringify(meta["filterMap"]));
                    $("#notfilters").val(JSON.stringify(meta["Notfilters"]));
                    $("#timeDetailsArray").val(JSON.stringify(meta["Timedetails"]));
                    parent.$("#reportPageMapping").val(JSON.stringify(meta["reportPageMapping"]));
             
    $.ajax({
       type:'POST',
       async:'false',
       data: $("#graphForm, #templateForm").serialize(),
       url: $("#ctxpath").val()+"/managementViewer.do?templateBy=getTemplateCharts&pagesId="+name,
       success:function(data){
            var jsondata = JSON.parse(data)["data"];
             generateChart(jsondata);
}

    });
         }
     });
}

function chartTypeFunction(chartId,chartType,chartName,KPIresult){
    var header = " ::header";
// if(chartType!="world-map" ||chartType!="World-City"){
//     $("#"+chartId).css("background-color","#FFF")
// }
 var chartData = JSON.parse(parent.$("#chartData").val());
//    if(chartType==="Top-Analysis" || chartType==="World-Top-Analysis" || chartType==="Combo-Analysis"){
//         chartData[chartId]["records"]="3";
//         parent.$("#chartData").val(JSON.stringify(chartData));
//     }
//        
 var isCompare = chartData[chartId]["isComparison"];
    if(chartType=='Radial-Chart' ||chartType=='Bullet-Horizontal' || chartType=='LiquidFilled-KPI' || chartType=='Standard-KPI' || chartType=='Standard-KPI1' || chartType=='KPIDash' || chartType =='Emoji-Chart' || chartType =='Dial-Gauge'){
        $("#dbChartName"+chartId).css("display","none");
if (typeof chartData[chartId]["hideBorder"]!=="undefined" && chartData[chartId]["hideBorder"]==="Y"){
       $("#div"+chartId).css({
            'border':'none'
        });
    }else{
        $("#div"+chartId).css({
            'border':'1px dotted #C8C8C8'
        });
    }
    }
    var html="";
    //            added by manik
    var chartNum=  chartId.replace ( /[^\d.]/g, '' );
    
    //     alert(chartNum)
    var chWidth=$("#divchart"+chartNum).width();
    var chHeight=$("#divchart"+chartNum).height();
    var chartData = JSON.parse(parent.$("#chartData").val());
       var completeViewbyIds =  chartData[chartId]["viewIds"];
      var completeViewby = chartData[chartId]["viewBys"];
       var completeMeassureIds =  chartData[chartId]["meassureIds"];
      var completeMeassures = chartData[chartId]["meassures"];



    if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType!="Radial-Chart"  && chartType!="Bullet-Horizontal"  && chartType!="Standard-KPI" && chartType!="KPIDash" && chartType!="Emoji-Chart" && chartType!="Dial-Gauge")){
    divSectionProperties(chartId);

      var fontType;//add by mayank sh.
           if (typeof chartData[chartId]["chartFontType"]!== "undefined" && chartData[chartId]["chartFontType"] === "Normal") {
              fontType=chartData[chartId]["chartFontType"];
           }else if(chartData[chartId]["chartFontType"]==="Bold"){
           fontType=chartData[chartId]["chartFontType"]
            } else{
               fontType=chartData[chartId]["chartFontType"];
           }
     var colorType;
           if (typeof chartData[chartId]["chartNameColol"]!== "undefined" && chartData[chartId]["chartNameColol"] === "") {
              colorType=chartData[chartId]["chartNameColol"];
           }else{
                     colorType=chartData[chartId]["chartNameColol"];
                 }
           
     var fontValue;  //add by mayank sh.
                 if (typeof chartData[chartId]["headingFontSize"]!=="undefined" && chartData[chartId]["headingFontSize"]===""){
                     fontValue=12;
                 }else{
                     fontValue=chartData[chartId]["headingFontSize"];
                 }
            if(typeof chartData[chartId]["Name"] !=="undefined" ){ // for date
                //Added By Ram For change color of chart name after applying measurefilter
                if(typeof chartData[chartId]["isFilterApplied"]!="undefined" && chartData[chartId]["isFilterApplied"]==="Yes")
                {
                    html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  id='spanChartName"+chartId+"' name=' ::header' onclick='drillWithinchartReport(\""+header+"\",\""+chartId+"\")' style=' font-size:"+fontValue+"px;font-style: "+fontType+";font-weight:"+fontType+";color:red;cursor:pointer;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[chartId]["Name"]+"</tspan></span>"
                }else{
                   html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  id='spanChartName"+chartId+"' name=' ::header' onclick='drillWithinchartReport(\""+header+"\",\""+chartId+"\")' style=' font-size:"+fontValue+"px;font-style: "+fontType+";font-weight:"+fontType+";color:"+colorType+";cursor:pointer;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[chartId]["Name"]+" </tspan></span>"
            }
if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Standard-KPI1" ||chartType==="Combo-Horizontal" || chartType==="Combo-Aus-City" || chartType==="Combo-Aus-State" || chartType==="Combo-USCity-Map" || chartType==="Combo-US-Map" || chartType==="Combo-IndiaCity" || chartType==="Combo-India-Map")){}else{              
if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Grouped-Bar" || chartType==="GroupedHorizontal-Bar"|| chartType==="Split-Bubble" || chartType==="Scatter-Bubble" || chartType === "GroupedStackedH-Bar"|| chartType==="GroupedStacked-Bar" ||chartType==="GroupedStacked-Bar%" ||  chartType==="GroupedStackedH-Bar%" ||chartType==="Grouped-Table" ||chartType==="Grouped-Map"||chartType==="Grouped-Line"|| chartType==="Grouped-MultiMeasureBar")){
  // add by mayank sharma
if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeGroupBarViewBy(\"" + chartId + "\")' src='' alt='Options'  >"+completeViewby[0]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeGroupBarViewBy(\"" + chartId + "\")'><ul id='viewByChange2"+chartId+"' class='dropdown-menu'></ul></span>";
}  if(completeViewby[1]){ 
if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeViewByGroup1(\"" + chartId + "\")'  src='' alt='Options'  >"+completeViewby[1]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeViewByGroup1(\"" + chartId + "\")'></img><ul id='viewByChange1"+chartId+"' class='dropdown-menu'></ul></span>";    
} }
 }else{
   if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeViewByGroup(\"" + chartId + "\")'  src='' alt='Options'  >"+completeViewby[0]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")'></img><ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></span>";    
}
 }
if(typeof chartData[chartId]["enableMeasures"]!="undefined" && chartData[chartId]["enableMeasures"]!="" && chartData[chartId]["enableMeasures"]!="Yes"){}else{
 html += "<span  class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 3px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeMeasureGroup(\"" + chartId + "\")' src='' alt='Options'  >"+completeMeassures[0]+"</tspan>"+
         "<img  data-toggle='dropdown' class='dropdown-toggle' title='measureChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")'></img><ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></span>";    
}    
                  // for date 
                  var fontValue1=10;
               var dateType = ["JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"];
                var calDateArray  = [];
               try{
                   
               calDateArray  = JSON.parse($("#timeDetailsArray").val());
                  var qtrtype  = parent.$("#qtrdate").val();
                  var newUIyear  = parent.$("#newUIyear").val();
               }catch(e){}
                var dateSplit;
                var dateIndex;
                var dateIndex1;
                var dateSplit1;
                var date;
                var date1;
                if(typeof calDateArray!=="undefined" && calDateArray[1] === "PRG_STD"){
                    dateSplit = calDateArray[2].split("/");
                    dateIndex = parseInt(dateSplit[0]);
   if(typeof chartData[chartId]["hideDate"]!="undefined" && chartData[chartId]["hideDate"]!="" && chartData[chartId]["hideDate"]!="Y"){
                }else{
     if (typeof chartData[chartId]["Name"] !== "undefined" && chartData[chartId]["Name"]!== "") {
     html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> :"+" "+dateType[dateIndex-1]+"-"+dateSplit[2]+" </tspan></span>"
     }else{  
         if(typeof calDateArray!=="undefined" && calDateArray[3] === "Qtr"){  //add by mayank sh. 08/01/2016 
     html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> " +qtrtype+"-"+dateSplit[2]+" </tspan></span>"
         }else if(typeof calDateArray!=="undefined" && calDateArray[3] === "Year"){
     html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> " +newUIyear+" </tspan></span>"
         }else{
     html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> " +dateType[dateIndex-1]+"-"+dateSplit[2]+" </tspan></span>"
     }    
     }  //end by mayank sh. 08/01/2016 
 }  }else{
                    dateSplit = calDateArray[2].split("/");
                    dateSplit1 = calDateArray[3].split("/");
                    dateIndex = parseInt(dateSplit[0]);
                    date = dateSplit[1];
                    date1 = dateSplit1[1];
                    dateIndex1 = parseInt(dateSplit1[0]);
                 if(typeof chartData[chartId]["hideDate"]!="undefined" && chartData[chartId]["hideDate"]!="" && chartData[chartId]["hideDate"]!="Y"){}else{
                 html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'> "+date+"-"+dateType[dateIndex-1]+"-"+dateSplit[2]+" to "+date1+"-"+dateType[dateIndex1-1]+"-"+dateSplit1[2]+" </tspan></span>"
                } 
            }
            }
            }
            else{
                
 if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Standard-KPI1" || chartType==="Combo-Horizontal" || chartType==="Combo-Aus-City" || chartType==="Combo-Aus-State" || chartType==="Combo-USCity-Map" || chartType==="Combo-US-Map" || chartType==="Combo-IndiaCity" || chartType==="Combo-India-Map")){}else{  
 if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Grouped-Bar" || chartType==="GroupedHorizontal-Bar" || chartType==="Scatter-Bubble"  ||chartType==="Grouped-Table" ||chartType==="Grouped-Map" || chartType==="Split-Bubble" || chartType === "GroupedStackedH-Bar" || chartType==="GroupedStacked-Bar" ||chartType==="GroupedStacked-Bar%" ||  chartType==="GroupedStackedH-Bar%" ||chartType==="Grouped-Line" || chartType==="Grouped-MultiMeasureBar")){
if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeGroupBarViewBy(\"" + chartId + "\")' src='' alt='Options'  >"+completeViewby[0]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeGroupBarViewBy(\"" + chartId + "\")'><ul id='viewByChange2"+chartId+"' class='dropdown-menu'></ul></span>";
}  // add by mayank sharma 
if(completeViewby[1]){ 
if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeViewByGroup1(\"" + chartId + "\")' src='' alt='Options'  >"+completeViewby[1]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeViewByGroup1(\"" + chartId + "\")'><ul id='viewByChange1"+chartId+"' class='dropdown-menu'></ul></span>";
}   }
}else{
 if(typeof chartData[chartId]["enableVieweby"]!="undefined" && chartData[chartId]["enableVieweby"]!="" && chartData[chartId]["enableVieweby"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 2px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeViewByGroup(\"" + chartId + "\")' src='' alt='Options'  >"+completeViewby[0]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='viewByChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")'><ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></span>";
}
}
if(typeof chartData[chartId]["enableMeasures"]!="undefined" && chartData[chartId]["enableMeasures"]!="" && chartData[chartId]["enableMeasures"]!="Yes"){}else{
html += "<span class='dropdown' style='cursor:pointer;'><tspan  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none;margin-left: 3px;font-size:"+(fontValue*.84)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:helvetica' onclick='changeMeasureGroup(\"" + chartId + "\")' src='' alt='Options'  >"+completeMeassures[0]+"</tspan>"+
         "<img data-toggle='dropdown' class='dropdown-toggle' title='measureChange' style='width:8px;height:5px;margin-left: 5px;margin-bottom: 0px;' src='images/arrow_down1.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")'></img><ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></span>"; 
}          
                // for date
                var dateType = ["JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"];
                var calDateArray = [];
                try{
                calDateArray   = JSON.parse($("#timeDetailsArray").val());
                var qtrtype  = parent.$("#qtrdate").val();
                 var newUIyear  = parent.$("#newUIyear").val();
                }catch(e){
                    
                }
                var dateSplit;
                var dateIndex;
                var dateIndex1;
                var dateSplit1;
                var date;
                var date1;
                try{
                if(typeof calDateArray!=="undefined" && calDateArray[1] === "PRG_STD"){
                    dateSplit = calDateArray[2].split("/");
                    dateIndex = parseInt(dateSplit[0]);
                    if(typeof chartData[chartId]["hideDate"]!="undefined" && chartData[chartId]["hideDate"]!="" && chartData[chartId]["hideDate"]!="Y"){}else{
                    if(typeof calDateArray!=="undefined" && calDateArray[3] === "Qtr"){    //add by mayank sh. 08/01/2016 
                    html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style='font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'> "+qtrtype+"-"+dateSplit[2]+" </tspan></span>"
                    }else if(typeof calDateArray!=="undefined" && calDateArray[3] === "Year"){
                    html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style='font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'> "+newUIyear+" </tspan></span>"
                    }else{
                    html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style='font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'> "+dateType[dateIndex-1]+"-"+dateSplit[2]+" </tspan></span>"
                    }    //end by mayank sh. 08/01/2016 
                    } }else{
                    dateSplit = calDateArray[2].split("/");
                    dateSplit1 = calDateArray[3].split("/");
                    dateIndex = parseInt(dateSplit[0]);
                    date = dateSplit[1];
                    date1 = dateSplit1[1];
                    dateIndex1 = parseInt(dateSplit1[0]);
                  if(typeof chartData[chartId]["hideDate"]!="undefined" && chartData[chartId]["hideDate"]!="" && chartData[chartId]["hideDate"]!="Y"){
              }else{
                 html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+fontValue1*.85+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'> "+date+"-"+dateType[dateIndex-1]+"-"+dateSplit[2]+" to "+date1+"-"+dateType[dateIndex1-1]+"-"+dateSplit1[2]+" </tspan></span>"  
                }
           }
       }catch(e)
       {
           
       }
            }
                //Added By Ram For change color of chart name after applying measurefilter
                if(typeof chartData[chartId]["isFilterApplied"]!="undefined" && chartData[chartId]["isFilterApplied"]==="Yes")
                {//fontsize
                    html+="<span id='dbChartName"+chartId+"'><tspan name=' ::header' onclick='drillWithinchartReport(\""+header+"\",\""+chartId+"\")'  style=' font-size:12px;cursor:pointer;color:red;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartId+"</tspan></span>"
                }else{
                    html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan   style=' font-size:12px;color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span>"
                }
            }
    } else{
        if(typeof(chartData[chartId]["KPIName"])!="undefined"  ){
            html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  style=' font-size:12px;color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span>"
        }else{
            html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  style=' font-size:12px;color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span>"
        }
    }
    var newchartid;

        newchartid=chartId;
        $("#renameTitle"+chartId).html(html);
        $("#"+chartId).html("");

    var data = graphData;
    chartData[chartId]["chartType"]=chartType;
    parent.$("#chartData").val(JSON.stringify(chartData));
    var currData=[];
    var radius = 600/3.5;
    var records = 12;
    var groupRecords = 0;
    var count = 0;
    //    $("#div"+chartId).css("width","50%");
    if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "" && chartData[chartId]["records"]!="All" && chartData[chartId]["records"]!="ALL" && chartData[chartId]["records"]!="all") {
        records = chartData[chartId]["records"];
    }
    var map={};
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var measIds = JSON.parse(parent.$("#measureIds").val());
    var aggregations = JSON.parse(parent.$("#aggregation").val());
    var measureValList = [];
    var measureValListTotal = [];
    var sum = d3.sum(data[chartId], function(d) {
        return d[measures[0]];
    });
    var tempChartData=[];
    for(var i in data[chartId]){
        var measValue=data[chartId][i][measures[0]];
        var percentage=measValue/sum*100;
        try{
            if(percentage.toString().substring(0,4)==="0.00" || percentage.toString().substring(0,3)==="0.0" || percentage=="0"){
                continue;
            }
            else{
                var dataMap={};
                var keys1=Object.keys(data[chartId][i]);
                for(var k in keys1){
                    dataMap[keys1[k]]=data[chartId][i][keys1[k]];
                }
                tempChartData.push(dataMap);
            }
        }
        catch(ex){
            dataMap={};
            keys1=Object.keys(data[chartId][i]);
            for(var k in keys1){
                dataMap[keys1[k]]=data[chartId][i][keys1[k]];
            }
            tempChartData.push(dataMap);
        }
    }
    data[chartId]=tempChartData;
    if(typeof chartData[chartId]["chartType"] !=="undefined" && (chartData[chartId]["chartType"]=="Grouped-Table" || chartData[chartId]["chartType"]=="Grouped-Map" || chartData[chartId]["chartType"]=="Scatter-Bubble" || chartData[chartId]["chartType"]=="Split-Bubble" || chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar"  || chartData[chartId]["chartType"] == "Grouped-Line" || chartData[chartId]["chartType"] == "GroupedStackedH-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-Bar"  || chartData[chartId]["chartType"] == "GroupedStacked-BarLine" || chartData[chartId]["chartType"] == "GroupedStacked-Bar%" ||chartData[chartId]["chartType"] == "GroupedStackedH-Bar%" || chartData[chartId]["chartType"] == "Grouped-MultiMeasureBar")){
        var innerRecords=4;
        if(typeof chartData[chartId]["innerRecords"]!="undefined" && chartData[chartId]["innerRecords"]!=""){
            innerRecords = parseInt(chartData[chartId]["innerRecords"]-1);
        }
        groupRecords = records*parseInt(innerRecords+1);
       
        for(var m=0;m<(data[chartId].length);m++){
            if(count<groupRecords){
                if(typeof data[chartId][m]!="undefined" && typeof data[chartId][m][chartData[chartId]["viewBys"][0]]!="undefined" && typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined" && map[data[chartId][m][chartData[chartId]["viewBys"][0]]].length>innerRecords){
                }else{
                    var list=[];
                    var keys = Object.keys(map);

                    if(keys.length<parseInt(records) || (typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined" && map[data[chartId][m][chartData[chartId]["viewBys"][0]]].length<parseInt(innerRecords+1))){
                        if(typeof data[chartId][m]!="undefined" && typeof data[chartId][m][chartData[chartId]["viewBys"][0]]!="undefined" && typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined"){
                            list= map[data[chartId][m][chartData[chartId]["viewBys"][0]]];
                            if(list.length<parseInt(innerRecords+1)){
                                list.push(data[chartId][m][chartData[chartId]["viewBys"][0]]);
                                count++;
                            }
                        }else{
                            list.push(data[chartId][m][chartData[chartId]["viewBys"][0]]);
                            count++;
                        }

                        map[data[chartId][m][chartData[chartId]["viewBys"][0]]]=list;
                        currData.push(data[chartId][m]);
                    }
                }

            }
        }
    }else{
        var countVar = 0;
        var flag = true;
        if(chartType=="Radial-Chart" ||chartType=="Bullet-Horizontal" || chartType=="LiquidFilled-KPI"){
            if(typeof chartData[chartId]["SubTotalProp"]!="undefined" && chartData[chartId]["SubTotalProp"]!="Disable"){
                if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "" && chartData[chartId]["records"]!="All" || chartData[chartId]["records"]!="ALL" || chartData[chartId]["records"]!="all") {
                    records = chartData[chartId]["records"];
                }
            }else{
                records =  data[chartId].length;
            }

        }
        if(chartType=="Standard-KPI" || chartType=="Standard-KPI1" || chartType=="KPIDash"){
            if(typeof chartData[chartId]["SubTotalProp"]!="undefined" && chartData[chartId]["SubTotalProp"]!="Disable"){
                if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "" && chartData[chartId]["records"]!="All" || chartData[chartId]["records"]!="all" || chartData[chartId]["records"]!="ALL") {
                    records = chartData[chartId]["records"];
                }
            }else{
                records =  data[chartId].length;
            }

        }

        if(typeof chartData[chartId]["othersL"]!="undefined" && chartData[chartId]["othersL"]=="Y" && typeof chartData[chartId]["filters"][chartData[chartId]["viewIds"][0]]!="undefined"){
            var otherFilters = chartData[chartId]["filters"][chartData[chartId]["viewIds"][0]];
            for(var h=0;h<(data[chartId].length < records ? data[chartId].length : records);h++){
                if(otherFilters.indexOf(data[chartId][h][chartData[chartId]["viewBys"][0]])==-1){
                    currData.push(data[chartId][h]);
                    countVar++;
                }
                else{
                    for(var i=0;i<measures.length;i++){
                        countVar++;
                        if(flag){
                            measureValList.push(data[chartId][h][measures[i]]);
                            flag=false;
                        }
                        else{
                            var prevVal = measureValList[i];
                            measureValList[i]=parseFloat(parseFloat(prevVal)+parseFloat(data[chartId][h][measures[i]]));
                        }
                    }

                }

                //                currData = data[chartId].filter(function(otherFilters) {
                //                      return otherFilters.indexOf(chartData[chartId]["viewBys"][0]) !== -1 || otherFilters.indexOf(chartData[chartId]["viewBys"][0]) !== -1;
                //                  });
                if(h==(data[chartId].length < records ? data[chartId].length : records)-1){
                    var othersMap = {};
                    othersMap[chartData[chartId]["viewBys"][0]]="  Others";
                    for(var m=0;m<measureValList.length;m++){
                        othersMap[measures[m]]=measureValList[m];
                    }
                    currData.push(othersMap);
                }

            }

        }else{
            var tempData=[];
            var tempDataMap={};
            for(var i1 in data[chartId]){
                tempDataMap={};
                var keys1=Object.keys(data[chartId][i1]);
                for(var j1 in keys1){
                    tempDataMap[keys1[j1]]=data[chartId][i1][keys1[j1]]
                }
                tempData.push(tempDataMap);
            }
            for(var h=0;h<(tempData.length < records ? tempData.length : records);h++){
                currData.push(tempData[h]);
                countVar++;
            }
        }
        //for others
        if(data[chartId].length>countVar){
            for(var k=countVar;k<data[chartId].length;k++){
                for(var i=0;i<measures.length;i++){
                    if(k===countVar){
                        measureValList.push(data[chartId][k][measures[i]]);
                    }
                    else{
                        var prevVal = measureValList[i];
                        measureValList[i]=parseFloat(parseFloat(prevVal)+parseFloat(data[chartId][k][measures[i]]));
                    }
                }
                if(k==parseInt(data[chartId].length)-1){
                    var index = measIds.indexOf(meassureIds[i]);
                    if(aggregations[index]==="AVG" || aggregations[index]==="avg"){
                        measureValListTotal.push(measureValList[i]/parseInt(data[chartId].length));
                    }else{
                        measureValListTotal.push(measureValList[i]);
                        
                    }
                }
            }
        }
    }

    if(typeof chartData[chartId]["others"]!=="undefined" && chartData[chartId]["others"]==="Y"){
        var otherMap = {};
        otherMap[chartData[chartId]["viewBys"][0]]="Others";
        for(var m=0;m<measureValList.length;m++){
            otherMap[measures[m]]=measureValList[m];
        }
        currData.push(otherMap);
    }
     if(typeof chartData[chartId]["gtGraph"]!=="undefined" && chartData[chartId]["gtGraph"]==="Y"){
        var otherMap = {};
        otherMap[chartData[chartId]["viewBys"][0]]="GrandTotal";
        for(var m=0;m<measures.length;m++){
            otherMap[measures[m]]=chartData[chartId]["GTValueList"][m];
        }
        currData.push(otherMap);
    }

if(typeof chartData[chartId]["records"] !=="undefined" && chartData[chartId]["records"]!=="" && (chartData[chartId]["records"]==="All" || chartData[chartId]["records"]==="all" || chartData[chartId]["records"]==="ALL")){
    currData= data[chartId];
}

    //alert("newchartid "+newchartid)
    if(chartType=="Vertical-Bar"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,"chart"+chartNum);
    }
    else if(chartType=="TopBottom-Chart"){
        topBottomChart(chartId,chWidth,chHeight);
//          buildTopBottomHorizotal(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
          
         
    }
//   else if(typeof isCompare !=="undefined" && isCompare=="LineComparison"){
//       var viewBy = [];
//       var viewId = [];
//          var viewOvName = JSON.parse(parent.$("#viewby").val());
//    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//       if(typeof chartData[chartId]["comparableFilters"]!=="undefined"){
//       var key = Object.keys(chartData[chartId]["comparableFilters"]);
// 
//       viewId.push(key);
//    viewBy.push(viewOvName[viewOvIds.indexOf(key.toString())]);
//    }
//   var quickViewname = "";
//  if(typeof chartData[chartId]["quickViewname"]!=="undefined" && chartData[chartId]["quickViewname"]!=="" ){
//      quickViewname = chartData[chartId]["quickViewname"];
//  }
//       getComparableData(chartId,viewId[0],viewBy[0],"false",quickViewname);
//    }
//    else if(chartType=='LineComparisonChange'){
//        var flag="false";
//        getChangeComparison(chartId,flag);
//    }
//    else if(typeof isCompare !=="undefined" && isCompare=="EnableComparison"){
//        var flag="false";
//        enableComparison(chartId,flag);
//    }
    else if(chartType=="Scatter-Analysis"){
 
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
//    var measId=[];
//    var aggType =[];
//    var measName=[];
//alert(chartData[chartId]["enableComparison"])
if(typeof chartData[chartId]["enableComparison"]==="undefined" || chartData[chartId]["enableComparison"]=="false"){
        for(var k in measId){
//        measId.push(measureId);
        measId.push(measId[k]+"_PRIOR");
//        measName.push(measureName);
        measName.push(measName[k]+" (PRIOR) ");
//        aggType.push(aggregationType);
        aggType.push(aggType[k]);
    }
    chartData[chartId]["enableComparison"] = "true";
//    priorFlag=false;
    }
    chartData[chartId]["meassureIds"]=measId;
    chartData[chartId]["aggregation"]=aggType;
    chartData[chartId]["meassures"]=measName;
    parent.$("#chartData").val(JSON.stringify(chartData));
 $.ajax({
            type:"POST",
            async:false,
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
              var gtValue = JSON.parse(kpidata);  
        buildScatterXYAnalysis(newchartid,newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,gtValue);
                }
            })
  
//
    }
    else if(chartType=="Table"){
 
     var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildTable(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));

                }})
  
//
    }
    else if(chartType=="Bar-Table"){
  var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
 
        buildTableBar(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})

    }
    else if(chartType=="Horizontal-Bar-Table"){
 
  if(typeof chartData[chartId]["GTValueList"]!=="undefined" && chartData[chartId]["GTValueList"]!==""){

         KPIresult = chartData[chartId]["GTValueList"];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
        KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
    }
  
        build2WayBarTable(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);

    }
    else if(chartType=="Transpose-Table"){

        buildTransposeTable(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Cross-Table"){

        buildCrossTable(newchartid,currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Horizontal-Bar"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildHorizontalBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Filled-Horizontal"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildFilledHorizontal(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Waterfall"){
        buildWaterfall(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Network"){
        buildNetwork(newchartid,chWidth,chHeight);
    }else if(chartType=="Dial-Gauge"){
       var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){

//        buildTableBar(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            buildDial(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
                }})
       
            }
    else if(chartType=="Pie"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildPie(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Column-Pie"){
          var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
             getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);   
        buildColumnPie(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
        else if(chartType=="Transportation-Spend-Variance"){
          var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildTrSpendVariance(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    
        else if(chartType=="Variance-By-Mode"){
          var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildVarianceByMode(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
            else if(chartType=="Analysis"){
          var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildAnalysisKpi(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    
    else if(chartType=="Column-Donut"){
var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildColumnDonut(newchartid,"GraphDonut", currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius+10,JSON.parse(kpidata));
    }})
    }
    else if(chartType=="Line"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    
//        buildQuickLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,chartData[chartId]["viewIds"]);
//        buildHistogram(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,chartData[chartId]["viewIds"]);
    }
    else if(chartType=="Trend-Analysis"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
//        buildLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    
        buildQuickLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,chartData[chartId]["viewIds"]);
//        buildHistogram(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,chartData[chartId]["viewIds"]);
}
    else if(chartType=="Vertical-Negative-Bar"){
        buildNegativeBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="MultiMeasureSmooth-Line"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildMultiMeasureSmoothLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="SmoothLine"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildSmoothLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Bubble"){
           var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
if(typeof chartData[chartId]["dataTranspose"]!="undefined" && chartData[chartId]["dataTranspose"]!="" && chartData[chartId]["dataTranspose"]=="Yes"){
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        bubble(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records,JSON.parse(kpidata));
                }})
    }else{
        bubble(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records);
    }
	}
        else if(chartType=="Split-Bubble"){
        buildSplitBubble(newchartid, currData, chartData[chartId]["viewBys"],chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
	}
    else if(chartType=="Centric-Bubble"){
        bubbleCentric(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records);
    }
    else if(chartType=="Multi-View-Bubble"){
        bubbleDbMultiView(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records);
    }
    else if(chartType=="Horizontal-Bubble"){
        bubbleHorizontal(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records);
    }
    else if(chartType=="Tangent"){
        buildTangent(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    //
    else if(chartType=="Double-Donut"){
        buildDoubleDonut(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius*1.5);
    }
    else if(chartType=="Donut"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildDonut(newchartid,"GraphDonut", currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius+10);
    }
    else if(chartType=="Half-Donut"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildHalfDonut(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius*1.5);
    }
    else if(chartType=="Half-Pie"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildHalfPie(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Donut-3D"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildDonut3D(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Donut");
    }
    else if(chartType=="Pie-3D"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildDonut3D(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="MultiMeasure-Line"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildMultiMeasureTrLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Cumulative-Line"){
       var cumulativeData = [];
        var currentData = 0;
        var priorData = 0;
        var priorData2 = 0;
        var priorData3 = 0;
        var priorData4 = 0;
        var viewbykeys = Object.keys(chartData[chartId]["viewBys"])
        var measureskeys = [];
        measureskeys.push(chartData[chartId]["meassures"][0])
         if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
        measureskeys.push(chartData[chartId]["meassures"][1]);
         }
         if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
        measureskeys.push(chartData[chartId]["meassures"][2]);
         }
         if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
        measureskeys.push(chartData[chartId]["meassures"][3]);
         }
         if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
        measureskeys.push(chartData[chartId]["meassures"][4]);
         }
        for(var i in currData){
        var currentDataMap = {};
           currentData = parseInt(parseInt(currentData) + parseInt(currData[i][chartData[chartId]["meassures"][0]]));
           if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
            priorData = parseInt(parseInt(priorData) + parseInt(currData[i][chartData[chartId]["meassures"][1]]));
           }
           if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
            priorData2 = parseInt(parseInt(priorData2) + parseInt(currData[i][chartData[chartId]["meassures"][2]]));
           }
           if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
            priorData3 = parseInt(parseInt(priorData3) + parseInt(currData[i][chartData[chartId]["meassures"][3]]));
           }
           if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
            priorData4 = parseInt(parseInt(priorData4) + parseInt(currData[i][chartData[chartId]["meassures"][4]]));
           }
       for(var j in chartData[chartId]["viewBys"]){
         currentDataMap[chartData[chartId]["viewBys"][j]] = currData[i][chartData[chartId]["viewBys"][j]];
    }
//       for(var j in chartData[chartId]["meassures"]){
         currentDataMap[chartData[chartId]["meassures"][0]] = currentData;
            if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
         currentDataMap[chartData[chartId]["meassures"][1]] = priorData;
    }
            if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
         currentDataMap[chartData[chartId]["meassures"][2]] = priorData2;
    }
            if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
         currentDataMap[chartData[chartId]["meassures"][3]] = priorData3;
    }
            if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
         currentDataMap[chartData[chartId]["meassures"][4]] = priorData4;
    }

//    }
    cumulativeData.push(currentDataMap)
    }
   
//        buildMultiAxisBar(newchartid, cumulativeData, chartData[chartId]["viewBys"], measureskeys,chWidth,chHeight,radius,"Pie");
        buildMultiMeasureTrLine(newchartid, cumulativeData, chartData[chartId]["viewBys"], measureskeys,chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Frequency-Distribution-Chart"){
//        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildFrequencyDistributionChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,"chart"+chartNum);
    }
    else if(chartType=="Cumulative-Bar"){
       var cumulativeData = [];
        var currentData = 0;
        var priorData = 0;
        var priorData2 = 0;
        var priorData3 = 0;
        var priorData4 = 0;
        var viewbykeys = Object.keys(chartData[chartId]["viewBys"])
        var measureskeys = [];
        measureskeys.push(chartData[chartId]["meassures"][0])
         if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
        measureskeys.push(chartData[chartId]["meassures"][1]);
         }
         if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
        measureskeys.push(chartData[chartId]["meassures"][2]);
         }
         if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
        measureskeys.push(chartData[chartId]["meassures"][3]);
         }
         if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
        measureskeys.push(chartData[chartId]["meassures"][4]);
         }
        for(var i in currData){
        var currentDataMap = {};
           currentData = parseInt(parseInt(currentData) + parseInt(currData[i][chartData[chartId]["meassures"][0]]));
           if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
            priorData = parseInt(parseInt(priorData) + parseInt(currData[i][chartData[chartId]["meassures"][1]]));
           }
           if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
            priorData2 = parseInt(parseInt(priorData2) + parseInt(currData[i][chartData[chartId]["meassures"][2]]));
           }
           if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
            priorData3 = parseInt(parseInt(priorData3) + parseInt(currData[i][chartData[chartId]["meassures"][3]]));
           }
           if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
            priorData4 = parseInt(parseInt(priorData4) + parseInt(currData[i][chartData[chartId]["meassures"][4]]));
           }
       for(var j in chartData[chartId]["viewBys"]){
         currentDataMap[chartData[chartId]["viewBys"][j]] = currData[i][chartData[chartId]["viewBys"][j]];
    }
//       for(var j in chartData[chartId]["meassures"]){
         currentDataMap[chartData[chartId]["meassures"][0]] = currentData;
            if(typeof chartData[chartId]["meassures"][1]!="undefined" && chartData[chartId]["meassures"][1]!=""){
         currentDataMap[chartData[chartId]["meassures"][1]] = priorData;
    }
            if(typeof chartData[chartId]["meassures"][2]!="undefined" && chartData[chartId]["meassures"][2]!=""){
         currentDataMap[chartData[chartId]["meassures"][2]] = priorData2;
    }
            if(typeof chartData[chartId]["meassures"][3]!="undefined" && chartData[chartId]["meassures"][3]!=""){
         currentDataMap[chartData[chartId]["meassures"][3]] = priorData3;
    }
            if(typeof chartData[chartId]["meassures"][4]!="undefined" && chartData[chartId]["meassures"][4]!=""){
         currentDataMap[chartData[chartId]["meassures"][4]] = priorData4;
    }

//    }
    cumulativeData.push(currentDataMap)
    }
   
        buildCumulativeBar(newchartid, cumulativeData, chartData[chartId]["viewBys"], measureskeys,chWidth,chHeight,radius,"Pie");
//        buildMultiMeasureTrLine(newchartid, cumulativeData, chartData[chartId]["viewBys"], measureskeys,chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Radial-Chart"){
        var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){

//        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
        radialProgressBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
                }})
    }
    else if(chartType=="Bullet-Horizontal"){
       var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){

//        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
//        radialProgressBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
        bulletHorizontal(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="LiquidFilled-KPI"){

         var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
                    
//        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
        LiquidFilledGauge(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
                }})

    }
    else if(chartType=="Standard-KPI"){

           var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
     
//        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
        tileCharts(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Standard-KPI1"){

           var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
     
//        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
        buildStandardKPI1(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Combo-TopKPI"){
        parent.comboTopChartType={};
        var measureId=chartData[chartId]["meassures"];
        if(measureId.length<4){
            return alert("Atleast four measure required to build this chart.") 
        }
        parent.comboTopChartType["comboTopChartType"]="Combo-TopKPI";
        parent.comboTopChartType["newchartid"]=newchartid;
        parent.comboTopChartType["currData"]=currData;
        parent.comboTopChartType["viewBys"]=chartData[chartId]["viewBys"];
        parent.comboTopChartType["viewIds"]=chartData[chartId]["viewIds"];
        parent.comboTopChartType["meassures"]=chartData[chartId]["meassures"];
        parent.comboTopChartType["meassureIds"]=chartData[chartId]["meassureIds"];
        parent.comboTopChartType["chWidth"]=chWidth;
        parent.comboTopChartType["chHeight"]=chHeight;
        parent.comboTopChartType["aggregation"]=(JSON.parse(parent.$("#aggregation").val()))[0];
//        alert(JSON.parse(parent.$("#aggregation").val())[0])
        addPage()
        
    }
    else if(chartType=="KPIDash"){
    
      if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"][0];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
            KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
        }
        KPIDash(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="Emoji-Chart"){
      if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"][0];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
            KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
        }
        buildEmojiChart(newchartid, KPIresult, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="scatter"){
        buildScatter(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Trend-Combo"){
        buildTrendCombo(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
          else if(chartType=="Veraction-Combo1"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildVeractionCombo1(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
     else if(chartType=="Influencers-Impact-Analysis"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildInImpactAnalysis(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
         else if(chartType=="Influencers-Impact-Analysis2"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildInImpactAnalysis2(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    
    else if(chartType=="Veraction-Combo3"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildVeractionCombo3(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    else if(chartType=="Score-vs-Targets"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildScoreTargetCombo(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    else if(chartType=="Model-Varince-Dashboard"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildModelVarinceDash(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    else if(chartType=="OverLaid-Bar-Line"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildOverlaid(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="world-map"){
        buildWorldMapChart(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="World-Top-Analysis"){
        buildWorldTopAnalysisChart(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="World-City"){
        buildWorldCityMapChart(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
      else if(chartType=="India-Map"){
          chartData[chartId]["records"]='30';
          parent.$("#chartData").val(JSON.stringify(chartData));
        buildDistrictMap1(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
      else if(chartType=="IndiaCity-Map"){
        buildIndiaCityMap(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
      else if(chartType=="Andhra-Pradesh"){
        buildAndhraPradesh(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
      else if(chartType=="Telangana"){
        buildTelangana(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
      else if(chartType=="US-State-Map"){
          chartData[chartId]["records"]='30';
          parent.$("#chartData").val(JSON.stringify(chartData));
        buildUStateMap(newchartid,data,chartData[chartId]["viewBys"],chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="US-City-Map"){
        buildUSCityMap(newchartid,data,chartData[chartId]["viewBys"],chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Australia-State-Map"){
        buildAustraliaMap(newchartid,data,chartData[chartId]["viewBys"],chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Australia-City-Map"){
        buildAusCityMap(newchartid,data,chartData[chartId]["viewBys"],chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Combo-Horizontal"){
        buildComboHorizontal(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Combo-India-Map"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboIndiaMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Combo-IndiaCity"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboIndiaCity(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
     else if(chartType=="Combo-US-Map"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboUSMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Combo-USCity-Map"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboUSCity(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Combo-Aus-State"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboAusState(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
     else if(chartType=="Combo-Aus-City"){
    var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildComboAusCity(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="KPI-Dashboard"){
    if(typeof chartData[chartId]["isKPI"]!=="undefined" && chartData[chartId]["isKPI"]==true){
        generateKPIDashboard(newchartid,chWidth,chHeight);
    }else{
        
    enableMenuCustom(newchartid,"",chWidth,chHeight);
    }  

    }
    else if(chartType=="XYZ-Dashboard"){
    if(typeof chartData[chartId]["isKPI"]!=="undefined" && chartData[chartId]["isKPI"]==true){
        generateKPIDashboard(newchartid,chWidth,chHeight);
    }else{
        
    enableMenuCustom(newchartid,"",chWidth,chHeight);
    }  

    }
    else if(chartType=="OverLaid-Bar-Bubble"){
        buildOverlaidBubble(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="MultiMeasure-Area"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildMultiMeasureArea(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Grouped-Bar"){
        buildGroupedBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
    }
    else if(chartType=="GroupedHorizontal-Bar"){
        buildGroupedHorizontalBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
    }
    else if(chartType=="Grouped-Table"){
        buildCrossTab(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
    }
    else if(chartType=="Grouped-Map"){
        buildGroupedMap(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
    }
    else if(chartType=="Horizontal-StackedBar"){
        buildhorizontalstackedBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="StackedBar"){
        buildstackedBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="StackedBarLine"){
        buildstackedBarLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }else if(chartType=="GroupedStacked-Bar"){
        buildGroupedstackedBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }else if(chartType=="GroupedStackedH-Bar"){
        buildGroupedstackedHBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="GroupedStacked-BarLine"){
        buildGroupedstackedBarLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="GroupedStacked-Bar%"){
        buildGroupedstackedBarPerCent(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="GroupedStackedH-Bar%"){
        buildGStackedHPercentage(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="Grouped-Line"){
        buildGroupedLine(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="StackedBarH"){
        buildstackedBarH(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="MultiMeasure-Bar"){
        buildMultiAxisBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="MultiMeasureH-Bar"){
        multiMeasureHorizontalBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Multi-Layered-Bar"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildMultiLayeredChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Combo-MeasureH-Bar"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildComboMeasureHBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Word-Cloud"){
        buildWordCloud(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Scatter-XY"){
        buildScatterXY(newchartid,newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Trend-Combo"){
        buildTrendCombo(newchartid,newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Scatter-Bubble"){
        buildScatterPlotGraph(newchartid,newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,records);
    }
    else if(chartType=="Area"){
        getSum(chartData,currData,chartData[chartId]["meassures"],newchartid);
        buildArea(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Aster"){
        buildAster(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="DualAxis-Bar"){
        buildDualAxisBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="DualAxis-Group"){
        buildDualAxisGroup(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="DualAxis-Target"){

          if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
        KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
    }
        buildDualAxisBarNew(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="KPI-Table"){
      if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
        KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
    }
        buildKPITable(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="Expression-Table"){
          if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
        KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
    }
        buildExpTable(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="Emoji-Chart"){
     if(typeof chartData[chartId]["GTValueList"]!="undefined" && chartData[chartId]["GTValueList"]!=""){

         KPIresult = chartData[chartId]["GTValueList"];
    }
    else if(typeof chartData[chartId]["GTValue"]!="undefined" && chartData[chartId]["GTValue"]!=""){
        KPIresult = chartData[chartId]["GTValue"]
    }else{
        KPIresult = 0;
    }

        buildEmojiChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,KPIresult);
    }
    else if(chartType=="Stacked-KPI"){
        var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
       $.ajax({

            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
 

//        buildTrendWithKPIChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata),"trendKPI");
        buildStackedKPI(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }
    else if(chartType=="Trend-KPI"){
       var createBarLine={};
        for(var i=0;i<measures.length;i++){
            createBarLine[measures[i]]= "Yes";
        }
        chartData[chartId]["createBarLine"]=createBarLine;
        parent.$("#chartData").val(JSON.stringify(chartData));
          var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
           
       $.ajax({
        
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
              

        buildTrendWithKPIChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata),"trendKPI");
                }})
 
    }
    else if(chartType==='Grouped-MultiMeasureBar'){
        buildGroupedMultiMeasureBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,newchartid);
    }
    else if(chartType==="KPI-Bar"){
        buildKPIBarChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Top-Analysis"){

        buildTopAnalysis(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    else if(chartType=="Combo-Analysis"){
         var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
    var viewByIconMap={};
 $.ajax({
            type:"POST",
            async:false,
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        $.ajax({
              async:false,
                type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/reportViewerAction.do?reportBy=getViwByIconMapping&viewByIds="+encodeURIComponent(chartData[chartId]["dimensions"]),
                success:function(data){
              		
                    viewByIconMap=JSON.parse(data);

                }})

        buildComboAnalysis(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata),viewByIconMap);
                }})
    }
    else if(chartType=="Trend-Table-Combo"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildTrendTableCombo(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    else if(chartType=="Trend-Analysis-Chart"){
        measId=chartData[chartId]["meassureIds"];
        aggType=chartData[chartId]["aggregation"];
        measName=chartData[chartId]["meassures"];
        viewByIds = parent.$("#viewbyIds").val();

        $.ajax({

            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
            success:function(kpidata){
                buildTrendTableChart(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            }
        })
    }
    else if(chartType==="Tree-Map"){
  var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
	if(typeof chartData[chartId]["dataTranspose"]!="undefined" && chartData[chartId]["dataTranspose"]!="" && chartData[chartId]["dataTranspose"]=="Yes"){
$.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/managementViewer.do?templateBy=getKPIValue&chartId="+chartId,
                success:function(kpidata){
        buildTreeMapSingle(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
                }})
    }else{
        buildTreeMapSingle(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    }
    else if(chartType=="StackedBar-%"){
        buildstackedBarPercent(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }
    else if(chartType=="StackedBarH-%"){
        buildHBarPercent(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
    }else if(chartType=="Multi-View-Tree"){
          var multiJson = {};
        count=0;
        multiJson["name"] = "";
//        var jsonData = {};
//        var chartData = JSON.parse($("#chartData").val());

//        multiJson["children"] = processHirarchiachalData(data);
        multiJson["children"] = processHirarchiachalData(currData);
        var data1 = JSON.parse(JSON.stringify(multiJson));
        buildZoomabletreeMulti(newchartid,data1,chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    }
    //    else if(chartType=="GroupedStacked-Bar"){
    //        buildGroupedStacked(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
    //    }
    else{
        buildBar(newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,"chart"+chartNum);
    }

    $("#loading").hide();
}
function divSectionProperties(chartId) {
    var chartData = JSON.parse($("#chartData").val());
    if (typeof chartData[chartId]["hideBorder"] !== "undefined" && chartData[chartId]["hideBorder"] === "Y") {
        $("#div" + chartId).css({
            'border': 'none'
        });
    } else {
        $("#div" + chartId).css({
            'border': '1px dotted #C8C8C8'
        });
    }// comment
    var background = chartData[chartId]["divBackGround"];
    if (typeof chartData[chartId]["divBackGround"] !== "undefined" && chartData[chartId]["divBackGround"] != "") {
        $("#div" + chartId).css({
            'background-color': background
        });
    } else {
        $("#div" + chartId).css({
            'background-color': 'none'
        });
    }
}

function selectTemplate(){
    $("#templateDesign").html('');
    $("#templateDesign").append("<img data-toggle='modal' data-target='#imageFullModal' src='"+$("#ctxPath").val()+"/template/images/templateDesign1.png'>");
}

function checkMeasureNameForGraph(measName) {
 
    var timeMapValues = JSON.parse(parent.$("#timeMap").val());


    if (typeof timeMapValues !== "undefined" && timeMapValues !== "" && typeof timeMapValues[measName] !== "undefined") {
        return timeMapValues[measName]
    } else {
        return measName;
    }
}

function containsNegativeValue(data,measureArray,type,isSelectiveMeasure,chartDetails){
    var measLen=0;
    if(typeof type!=='undefined' && type==='single'){
        measLen=1;
    }
    else{
        measLen=measureArray.length;
    }
     for(var i in data){
        for(var k=0;k<measLen;k++){
            if(typeof type!='undefined' && type=='multi' && typeof isSelectiveMeasure!='undefined' && isSelectiveMeasure==='y' ){
                if(k==0){
                    if (!(typeof chartDetails["createBarLine"] === "undefined" || typeof chartDetails["createBarLine"][measureArray[k]] === "undefined" || chartDetails["createBarLine"][measureArray[k]] === "Yes")) {
                        continue;
                    }
                }else{
                    if (typeof chartDetails["createBarLine"] === "undefined" || typeof chartDetails["createBarLine"][measureArray[k]] === "undefined" || chartDetails["createBarLine"][measureArray[k]] === "No") {
                        continue;
                    }
                }
            }
            if(parseInt(data[i][measureArray[k]])<0){
             return true;
         }
     }
    }
     return false;
 }
 
 function createManagementDashboard(path){
    parent.$("#createMgmtDashboard").dialog('open');
}
function createMgmtDB(ctxPath){
    $("#createMgmtDBBox").dialog({
       autoOpen: false,
       height: 500,
       width: 800,
       position: 'justify',
       modal: true,
       resizable:true
    });
    var templateName=$("#newMgmtDashboardName").val();
    var templateDesc=$("#newMgmtDashboardDesc").val();
    var frameObj = parent.document.getElementById("createMgmtDBFrame");
    var source=ctxPath+"/reportTemplateAction.do?templateParam=loadAOMeasures&ctxPath="+ctxPath+"&templateName="+templateName+"&templateDesc="+templateDesc;
    frameObj.src = source;
    $("#createMgmtDashboard").dialog('close');
    $("#createMgmtDBBox").dialog('open');
}
function selectPages(ctxPath) {
    parent.$("#createMgmtDBBox").dialog('close');
    var ul = document.getElementById("sortable");
    var measures=[];
    if (typeof ul !== undefined || ul !== null) {
        var colIds = ul.getElementsByTagName("li");
        if (colIds !== null && colIds.length !== 0) {
            for (var i = 0; i < colIds.length; i++) {
                measures.push(colIds[i].id);
            }
        }
    }
    var frameObj = parent.document.getElementById("createMgmtDBFrame");
    var source = '';
    parent.$("#createMgmtDBBox").dialog({
        autoOpen: false,
        height: 500,
        width: 800,
        position: 'justify',
        modal: true,
        resizable: true
    });
    frameObj.src = "about:blank"
    source = ctxPath + "/reportTemplateAction.do?templateParam=loadPages&measures="+JSON.stringify(measures);
    frameObj.src = source;
    parent.$("#createMgmtDBBox").dialog('open');
}
function cancelMgmtDB(){
    $("#createMgmtDashboard").dialog('close');
}
function saveTemplate() {
    $('#mgtMenu').hide();
    $.ajax({
        type: "POST",
        data: parent.$("#templateForm").serialize(),
        url: parent.$("#ctxpath").val() + "/managementViewer.do?templateBy=saveTemplate",
        success: function(data) {
            alert("Saved Successfully");
        }
    });
}
