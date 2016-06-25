/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 var newProp="";
var chartGroupsTemplate=["Bar Graphs","Circular Graphs","KPI Graphs","Variance Graphs"
    ,"Line Graphs","Grouped Graphs","Map Graphs","Combo Maps"
    ,"Area Graphs","Table Graphs","Bubble Graphs", "Other Graphs"];
var graphsNameTemplate = {
    "Vertical-Bar": "single",
//    "Vertical-Negative-Bar": "single",
    "Table":"double",
    "Combo-TopKPI":"single",
    "Transpose-Table":"single",
    "Dial-Gauge":"single",
    "Horizontal-Bar": "single",
    "Pie": "single",
    "Pie-3D": "single",
    "Donut": "single",
    "Double-Donut": "single",
    "Donut-3D": "single" ,
    "Bubble": "single",
    "Line": "single",
    "DualAxis-Bar":"single",
    "DualAxis-Group":"single",
    "DualAxis-Target":"single",
    "SmoothLine": "single",
    "MultiMeasure-Line":"single",
    "Cumulative-Line":"single",
    "Cumulative-Bar":"single",
    "Multi-Layered-Bar":"single",
    "Combo-MeasureH-Bar":"single",
    "Radial-Chart":"single",
    "Combo-Horizontal":"single",
//    "Bullet-Horizontal":"single",
    "LiquidFilled-KPI":"single",
    "Standard-KPI":"single",
//    "KPIDash":"single",
//    "Emoji-Chart":"single",
    "OverLaid-Bar-Line": "single",
    "Word-Cloud": "single",
    "MultiMeasure-Bar" : "single",
    "MultiMeasureH-Bar" : "single",
    "Grouped-Bar" : "double",
    "GroupedHorizontal-Bar" : "double",
    "Grouped-Table" : "double",
    "Grouped-Map" : "double",
    "GroupedStacked-Bar" : "double",
    "GroupedStackedH-Bar" : "double",
    "GroupedStacked-BarLine" : "double",
    "GroupedStacked-Bar%" : "double",
    "GroupedStackedH-Bar%" : "double",
    "Split-Bubble": "double",
    "Scatter-Bubble":"double",
    "StackedBar":"single",
    "Horizontal StackedBar":"single",
    "Area" : "single",
    "MultiMeasure-Area" : "single",
    "Half-Donut":"single",
    "Half-Pie":"single",
    "Grouped-Line" : "double",
    "KPI-Table":"single",
    "TopBottom-Chart":"single",
    "Filled-Horizontal":"single",
    "Stacked-KPI":"single",
    "Scatter-XY":"single",
    "Trend-Combo":"single",
    "Centric-Bubble":"single",
    "Multi-View-Bubble":"double",
    "StackedBarLine":"single",
    "Horizontal-Bubble":"single",
    "Bar-Table":"single",
    "OverLaid-Bar-Bubble":"single",
    "Tangent":"single",
    "Aster":"single",
    "KPI-Bar":"single",
    "Grouped-MultiMeasureBar":"double",
    "world-map":"single",
    "StackedBar-%":"single",
    "StackedBarH-%":"single",
    "Top Analysis":"single",
    "Combo-Analysis":"single",
    "World-Top-Analysis":"single",
    "World-City":"single",
    "Trend-KPI":"single",
    "Column-Donut":"single",
    "India-Map":"single",
    "Andhra-Pradesh":"single",
    "Telangana":"single",
    "IndiaCity-Map":"single",
    "US-State-Map":"single",
    "Combo-India-Map":"single",
    "Combo-IndiaCity":"single",
    "Combo-US-Map":"single",
    "Combo-USCity-Map":"single",
    "Australia-State-Map":"single",
    "Australia-City-Map":"single",
    "Combo-Aus-State":"single",
    "Combo-Aus-City":"single",
    "US-City-Map":"single",
    "Trend-Table-Combo":"single",
      "KPI-Dashboard":"double",
      "XYZ-Dashboard":"double",
    "Multi-View-Tree":"double",
    "Horizontal-Bar-Table":"single",
    "Cross-Table":"double",
    "Trend-Analysis":"single",
    "Frequency-Distribution-Chart":"single",
    "Trend-Analysis-Chart":"single",
    "Scatter-Analysis":"double",
    "Veraction-Combo1":"single",
    "Influencers-Impact-Analysis":"single",
    "Influencers-Impact-Analysis2":"single",
    "Veraction-Combo3":"single",
//    "Model-Varince-Dashboard":"single",
    "Transportation-Spend-Variance":"single",
    "Variance-By-Mode":"single",
    "Analysis":"single",
    "Score-vs-Targets":"single"
//  "Cross-Table":"double"
}

function generateJsonData(repId, graphFlag, grid1, pageChange) {
    $("#loading").show();
    isInit = {};
    uscity = {};
    //   $("#content_1").hide();

    if (grid1 !== grid) {
        grid = grid;
    } else {
        grid = grid1;
    }
//    $("#reportTD1").hide(); commented by mayank on 20 sep
    $("#content_Drag").html('');
    $("#type").val("graph");
    graphTrendFlag = graphFlag;
    $("#drills").val("");
    $("#driver").val("");
    $("#filters1").val("");
    $("#drillFormat").val("");
    $("#draggableViewBys").val("");
    startValgbl = 1;
    var htmlVar = "";
    if (typeof pageChange === 'undefined') {
        pageChange = '';
    }

    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    $.post(
            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&pageChange=" + pageChange,
            function (data) {
                //            $("#loading").hide();
                if (data == "false") {
//                if(typeof pageChange!='undefined'){
//                    htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
//
//                $("#xtendChartTD").append(htmlVar);
//                $("#xtendChartTD").show();
//                $("#loading").hide();
//                return ;
//                }
                    htmlVar = "";

                    $("#loading").hide();
                    $("#xtendChartTD").html('');
                    $("#content_Drag").html('');
                    var textDivHeight = screen.height;
                    if (parent.userType == "ANALYZER") {
                        $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;float:right;height:" + textDivHeight + "px;overflow:auto;'></div>");
                    } else {
                        $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;background-color: rgb(255, 255, 255); box-shadow: 0px 0px 2px 2px rgb(197, 66, 66); margin-top: 1px; border-radius: 5px;width:12%;float:right;height:460px;overflow:auto;position:fixed;margin-top:;right: 20px;box-shadow:0px 1px 4px 2px rgb(227, 221, 221);margin-right: 2px;display:none;background-color: #FFF;z-index:11'></div>");

                    }
                    htmlVar += "<div style='position:fixed;margin-left:2%'>"
                    htmlVar += "<span title='Parameters' align='center' style='font-size:16px;white-space:nowrap; text-align:center;'>" + translateLanguage.Parameters + "</span></br>";
                    htmlVar += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>" + translateLanguage.Drag_Parameters + "</span>";
                    htmlVar += "</div>"
                    var viewByIds = JSON.parse(JSON.stringify($("#viewbyIds").val()));
                    var viewBy = JSON.parse(JSON.stringify($("#viewby").val()));
                    var measures = JSON.parse(JSON.stringify($("#measure").val()))
                    htmlVar += "<br>"
                    htmlVar += "<div style='position:fixed; margin-top: 1%'>"
                    htmlVar += "<div class = 'hiddenscrollbar' style=''>"
                    htmlVar += "<div class = 'innerhiddenscrollbar' style='height:150px;width:150px'>"
                    htmlVar += "<ul class='rightDragMenu' style='list-style-type:none;pading:2px'>"
                    for (var i in viewByIds) {
                        if (viewBy[i].length > 15) {
                            htmlVar += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='" + viewBy + "'  draggable='true'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center'  title='" + viewBy[i] + "'>" + viewBy[i].substring(0, 15) + "..</span></li>"
                            htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                        } else {
                            htmlVar += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='" + viewBy + "'  draggable='true'  ondragstart='drag(event,this.id)'><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + viewBy[i] + "'>" + viewBy[i] + "</span></li>"
                            htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                        }

                    }
                    htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                    htmlVar += "</ul>"
                    htmlVar += "</div>"
                    htmlVar += "</div>"
                    htmlVar += "<div  style='position:fixed;margin-top:;'>"
                    htmlVar += "<span title='Measures' align='center' style='font-size:16px;white-space:nowrap;text-align:center;'>" + translateLanguage.Measures + "</span></br>";
                    htmlVar += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>" + translateLanguage.Drag_Measures + "</span>";
                    htmlVar += "</div>";
                    htmlVar += "<div style='position:fixed;margin-top:2%'>"
                    htmlVar += "<div class = 'hiddenscrollbar' style=''>"
                    htmlVar += "<div class = 'innerhiddenscrollbar'  style='height:180px;width:150px'>"
                    htmlVar += "<ul style='list-style-type:none' class='rightDragMenu'>"
                    for (var j in measures) {
                        if (measures[j].length > 20) {
                            htmlVar += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='" + measures[j] + "'  draggable='true' onclick='measureDriven(this.id)' ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + measures[j] + "'>" + measures[j].substring(0, 15) + "..</span></li>"
                        } else {
                            htmlVar += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='" + measures[j] + "'  draggable='true' onclick='measureDriven(this.id)' ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + measures[j] + "'>" + measures[j] + "</span></li>"
                        }
                    }
                    htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                    htmlVar += "</ul>"
                    htmlVar += "</div>"
                    htmlVar += "</div>"
                    htmlVar += "</div>"
                    //               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';

                    var textHtml = "";
                    textHtml += "<div style='width:98%;height:" + (textDivHeight - 10) + "px;border:1px solid grey;overflow:hidden'>";
                    textHtml += "</div>";
                    $("#noData").hide();
                    $("#xtendChartTD1").show();
                    if (parent.userType == "ANALYZER") {

                        $("#content_Drag").html('');
                        $("#content_Drag").append();
                    } else {
                        $("#content_Drag").append(htmlVar);

                    }
                    $("#xtendChartssTD").show();
                    //
                    //                $("#loading").hide();
                    //               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
                    //               $("#xtendChartTD").append(htmlVar);
                    //                $("#xtendChartTD").show();
                    //                $("#loading").hide();
                } else {
                    var jsondata = JSON.parse(data)["data"];
                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                    $("#templateMeta").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["templateMeta"]));
                    var meta = JSON.parse(JSON.parse(data)["meta"]);


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
                    parent.$("#advanceChartType").val("advanceGraph");
                    $("#advanceChartData").val(JSON.stringify(meta["advanceChartData"]));
                    parent.$("#reportPageMapping").val(JSON.stringify(meta["reportPageMapping"]));
                    var reportPageMapping = JSON.parse(JSON.stringify(meta["reportPageMapping"]));
                    var pages = Object.keys(reportPageMapping[repId]);
                    var pageSequence = JSON.parse(data)["pageSequence"];
                    currentPage = JSON.parse(data)["currentPage"];
//            for(var i in pages){
//                if(reportPageMapping[repId][pages[i]]["pageSequence"]===pageSequence[0]){
//                    currentPage=pages[i];
//                }
//            }
                    pageSequence.sort();
                    parent.$("#pageSequence").val(JSON.stringify(pageSequence));
                    parent.$("#pageList").empty();
                    if (parent.$("#morePages").html().trim() == '') {
                        parent.$("#morePages").append("More");
                    }
                    if (pages.length <= 3) {
                        parent.$("#pageList").css("width", "40%");
                        parent.$("#morePages").hide();
                    } else {
                        parent.$("#pageList").css("width", "40%");
                        parent.$("#morePages").show();
                    }
                    if (pages.length > 1) {
                        for (var i = 0; i < (pages.length <= 3 ? pages.length : 3); i++) {
                            var pageId = '';
                            for (var j in pages) {
                                if (reportPageMapping[repId][pages[j]]["pageSequence"] == pageSequence[i]) {
                                    pageId = pages[j];
                                    break;
                                }
                            }
//                if(reportPageMapping[repId][pageId]["pageSequence"]==="1" && typeof currentPage==='undefined'){
//                    currentPage=pageId
//                }
                            if (currentPage === pageId) {
                                parent.$("#pageList").append("<div id='" + pageId + "' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>" + reportPageMapping[repId][pageId]["pageLabel"] + "</div>");
                            } else {
                                parent.$("#pageList").append("<div id='" + pageId + "' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>" + reportPageMapping[repId][pageId]["pageLabel"] + "</div>");
                            }
                        }
                        $("#" + currentPage).css("border-bottom", "3px solid #008cc9");
                    }
                    try {
                        $("#tabs-1").tabs("refresh");
                    } catch (e) {
                        alert(e);
                    }
//            $("#timeMap").val(JSON.stringify(meta["timeMap"]));
//                    if (window.mgmtDBFlag === 'true') {
//                        var gtResult;
//                        $.ajax({
//                            async: false,
//                            type: "POST",
//                            data: parent.$("#graphForm").serialize(),
//                            url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
//                            success: function (data) {
//                                gtResult = JSON.parse(JSON.parse(data)["data"]);
//                                var tempMetaResult = JSON.parse(data)["meta"];
//                                parent.$("#templateMeta").val(tempMetaResult);
//                                showMagTemp(gtResult);
//                            }
//                        });
//                    }
                    parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
                    if (typeof parent.timeMapValue === "undefined") {
                        $("#timeMap").val(JSON.stringify(meta["timeMap"]));
                    }
                    if (typeof meta["draggableViewBys"] != "undefined" && meta["draggableViewBys"] != "") {
                        var chartData = JSON.parse($("#chartData").val());
                        var keys = Object.keys(meta["draggableViewBys"])
                        var draggableView = []
                        //                     for(var i in meta["draggableViewBys"] ){
                        for (var j in meta["draggableViewBys"]) {
                            //                        if(meta["draggableViewBys"][i]==chartData[keys[j]]["viewBys"]){

                            draggableView.push(meta["draggableViewBys"][j]);
                        }
                        $("#draggableViewBys").val(JSON.stringify(draggableView));
                    } else {
                        if (typeof $("#chartData").val() != "undefined" && $("#chartData").val() != "") {
                            var chartData = JSON.parse($("#chartData").val());
                            var keys = Object.keys(chartData)
                            var draggableView = []
                            for (var k in keys) {
                                draggableView.push(chartData[keys[k]]["viewBys"][0]);
                            }
                            $("#draggableViewBys").val(JSON.stringify(draggableView));
                        }

                    }
//              applyfiltersgraph();

                    //                 if(typeof meta["filterMap"] != "undefined" && meta["filterMap"] != ""){
                    //                    globalFilter = meta["filterMap"];
                    //                }
                    $.ajax({
                        async: false,
                        type: "POST",
                        data:
                                $('#graphForm').serialize(),
                        url: 'reportViewer.do?reportBy=getFilters&typegbl=graph',
                        success: function (data) {
                            filterData = JSON.parse(data);
//                    filterDatagbl=filterData;
                            applyfiltersgraph();
                            if (savedfilter) {
                                $("#filters1").val(JSON.stringify(meta["filterMap"]));
                                if (pageDrillValue !== '') {
                                    var appliedFilterMap = {};
                                    try {
                                        appliedFilterMap = JSON.parse($("#filters1").val());
                                    } catch (e) {
                                    }
                                    var filterMap = {};
                                    var filterList = [];
                                    if (typeof appliedFilterMap !== 'undefined') {
                                        filterMap = appliedFilterMap;
                                        filterList.push(pageDrillValue.split(":")[0]);
                                    } else {
                                        filterList.push(pageDrillValue.split(":")[0]);
                                    }
                                    var drillFirst = [];
                                    var drillNext = [];
                                    drillFirst.push(pageDrillValue.split(":")[0]);
                                    drillNext.push(pageDrillValue.split(":")[1]);
                                    filterMap[window.pageDrillViewById] = drillFirst;

//                filterMap[chartData[div]["viewIds"][0]] = drillFirst;
                                    if (typeof window.pageDrillViewById1 !== "undefined")
                                        filterMap[window.pageDrillViewById1] = drillNext;


                                    pageDrillValue = '';
                                    window.pageDrillViewById = '';
                                    var quickViewname = window.pageDrillViewByName;

                                    if (quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                                        parent.$("#drillFormat").val("time");
                                    } else if (quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year") {
                                        parent.$("#drillFormat").val("time");
                                    } else {
                                        parent.$("#drillFormat").val("none");
                                    }

                                    parent.$("#drills").val(JSON.stringify(filterMap));
                                }
                            }
                            //                         generateChart(data);
                            //                     if(typeof globalFilter != "undefined"){
                            //      d               $("#filters1").val(JSON.stringify(globalFilter));
                            //                     }
                            $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&initializeFlag=true" + "&action=localfilterGraphs", $("#graphForm").serialize(),
                                    function (data) {
                                        var meta = JSON.parse(JSON.parse(data)["meta"]);
                                        $("#viewby").val(JSON.stringify(meta["viewbys"]));
                                        $("#measure").val(JSON.stringify(meta["measures"]));
                                        $("#chartData").val(JSON.stringify(meta["chartData"]));
                                        $("#numberFormatMap").val(JSON.stringify(meta["numberFormatMap"]));
                                        $("#currencyType").val(meta["currencyType"]);
                                        // start
//                        $.ajax({
//        async:false,
//        type:"POST",
//        data:
//        $('#graphForm').serialize(),
//        url:  'reportViewer.do?reportBy=saveXtCharts',
//
//        success:function(saveDdata) {
                                        var resultset = data;
                                        if (parent.$("#type").val() === "advance") {
                                            generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                                        } else {
                                            runtimeglobalfilters(JSON.stringify(meta["filterMap"]), 'null', "graph");
                                            var data1 = JSON.parse(resultset)["data"];
                                            generateChart(data1);
                                        }
//        }
//    })



                                    }); //end of drill ajax
                        }
                    });

                }
            });
}
function generateTemplateData1st(repId, graphFlag, grid1, pageChange) {
    // $("#content_1").hide();
    grid = grid1;
//   $("#openGraphSection").dialog('close');
    //    $("#loading").show();
//    $("#reportTD1").hide();  commented by mayank on 20 Sep
    $("#xtendChartTD").html('');
    if (checkBrowser() == "ie") {
        $("#xtendChartssTD").html('');
    }
    $("#draggableViewBys").val('');
    $("#type").val("graph");
    graphTrendFlag = graphFlag;
    $("#drills").val("");
    $("#filters1").val("");
    $("#drillFormat").val("");
    startValgbl = 1;
    if (typeof pageChange === 'undefined') {
        pageChange = '';
    }
    var htmlVar = "";
    var pageLabel = "Page 1";

    currentPage = 'default';
    $.ajax({
     async:false,
     type:'POST',
     data: $("#templateForm").serialize(),
     url:$("#ctxpath").val() +'/managementViewer.do?templateBy=getTemplateKPIData',
     success:function(templateData)
     {
         var templateMeta = JSON.parse(JSON.parse(templateData)["meta"]);
         $("#measureNameMap").val(JSON.stringify(templateMeta["measureNameMap"]));
         $("#measureIdMap").val(JSON.stringify(templateMeta["measureIdMap"]));
         $("#pageMap").val(JSON.stringify(templateMeta["pageMap"]));
         $("#pageIdMap").val(JSON.stringify(templateMeta["pageIdMap"]));
              var gtResult;
   $.ajax({
                        async: false,
                        type: "POST",
                        data: parent.$("#templateForm").serialize(),
                        url: $("#ctxpath").val() + "/managementViewer.do?templateBy=getMeasureTotal",
                        success: function(data) {
                            gtResult = JSON.parse(JSON.parse(data)["data"]);
                            var tempMetaResult = JSON.parse(data)["meta"];
                            parent.$("#templateMeta").val(tempMetaResult);
//                            showMagTemp(gtResult);
                        var templateIndex=0;
                        createMgTemKpis(templateIndex,gtResult)
                            
                        }
                    });
   $.ajax({
     async:false,
     type:'POST',
       url:  $("#ctxpath").val() +'/managementViewer.do?templateBy=getAvailableTemplateCharts',
         success:function (data) {
                //                $("#loading").hide();
                if (data == "false") {

                    htmlVar += '<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';

                    $("#xtendChartTD").append(htmlVar);
                    $("#xtendChartTD").show();
                    $("#loading").hide();
                } else {
                    var jsondata = JSON.parse(data)["data"];
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
                    var reportPageMapping = JSON.parse(JSON.stringify(meta["reportPageMapping"]));
                    var pages = Object.keys(reportPageMapping[repId]);
                    var pageSequence = JSON.parse(data)["pageSequence"];
                    currentPage = JSON.parse(data)["currentPage"];
//                  for(var i in pages){
//                      if(reportPageMapping[repId][pages[i]]["pageSequence"]===pageSequence[0]){
//                          currentPage=pages[i];
//                      }
//                  }
                    pageSequence.sort();
                    parent.$("#pageSequence").val(JSON.stringify(pageSequence));
                    parent.$("#pageList").empty();
//                    if (parent.$("#morePages").html().trim() == '') {
//                        parent.$("#morePages").append("More");
//                    }
                    if (pages.length <= 3) {
                        parent.$("#pageList").css("width", "40%");
                        parent.$("#morePages").hide();
                    } else {
                        parent.$("#pageList").css("width", "40%");
                        parent.$("#morePages").show();
                    }
                    if (pages.length > 1) {
                        for (var i = 0; i < (pages.length <= 3 ? pages.length : 3); i++) {
                            var pageId = '';
//                            for (var j in pages) {
//                                alert(reportPageMapping[repId][pages[j]]["pageSequence"])
//                                if (reportPageMapping[repId][pages[j]]["pageSequence"] == pageSequence[i]) {
                                    pageId = pages[0];
//                                    break;
//                                }
//                            }
//                if(reportPageMapping[repId][pageId]["pageSequence"]==="1" && typeof currentPage==='undefined'){
//                    currentPage=pageId
//                }
                            if (currentPage === pageId) {
                                parent.$("#pageList").append("<div id='" + pageId + "' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='border-bottom:3px solid #008cc9;width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>" + reportPageMapping[repId][pageId]["pageLabel"] + "</div>");
                            } else {
                                parent.$("#pageList").append("<div id='" + pageId + "' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>" + reportPageMapping[repId][pageId]["pageLabel"] + "</div>");
                            }
                        }
                    }
                    try {
                        $("#tabs-1").tabs("refresh");
                    } catch (e) {
                        alert("exception!");
                    }
                    parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
                    if (typeof parent.timeMapValue === "undefined") {
                        $("#timeMap").val(JSON.stringify(meta["timeMap"]));
                    }

                    if (typeof meta["draggableViewBys"] !== "undefined" && meta["draggableViewBys"] !== "") {
                        if (typeof $("#chartData").val() !== "undefined" && $("#chartData").val() !== "") {
                            var chartData = JSON.parse($("#chartData").val());
                            var keys = Object.keys(chartData)
                            var draggableView = [];

                            //                     for(var i in meta["draggableViewBys"] ){
                            for (var j in keys) {

                                //                        if(meta["draggableViewBys"][i]==chartData[keys[j]]["viewBys"]){
                                draggableView.push(chartData[keys[j]]["viewBys"][0]);
                                //                        }
                                //                         }
                            }
                            $("#draggableViewBys").val(JSON.stringify(draggableView));
                        }
                    } else {
                        if (typeof $("#chartData").val() !== "undefined" && $("#chartData").val() !== "") {
                            var chartData = JSON.parse($("#chartData").val());
                            var keys = Object.keys(chartData)
                            var draggableView = []
                            for (var k in keys) {
                                draggableView.push(JSON.stringify(chartData[keys[k]]["viewBys"][0]));
                            }

                            $("#draggableViewBys").val(JSON.stringify(draggableView));
                        }

                    }

                    //                 if(typeof meta["filterMap"] != "undefined" && meta["filterMap"] != ""){
                    //                    globalFilter = JSON.stringify(meta["filterMap"]);
                    //                }
                    if (typeof reportToTrendFilter !== "undefined") {
                        $("#filters1").val(JSON.stringify(reportToTrendFilter))
                    } else {
                        $("#filters1").val(JSON.stringify(meta["filterMap"]));
                    }

                            $.post($("#ctxpath").val() + "/managementViewer.do?templateBy=getTemplateCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&initializeFlag=true" + "&action=localfilterGraphs", $("#graphForm, #templateForm").serialize(),
                                    function (data) {
                                        var meta = JSON.parse(JSON.parse(data)["meta"]);
                                        $("#viewby").val(JSON.stringify(meta["viewbys"]));
                                        $("#measure").val(JSON.stringify(meta["measures"]));
                                        $("#chartData").val(JSON.stringify(meta["chartData"]));
                                        $("#numberFormatMap").val(JSON.stringify(meta["numberFormatMap"]));
                                        $("#currencyType").val(meta["currencyType"]);
                                        $("#timeDetailsArray").val(JSON.stringify(meta["Timedetails"]));

                                        var resultset = data;
                                        var data1 = JSON.parse(resultset)["data"];

                                            generateChartloop(data1);

                                    });
                        }
                    } 
                
            });

 }
    });
$("#openGraphSection").dialog('close');
}

function generateChartloop(jsonData, dragFlag, idArr) {
    // add by mayank sh.(6/6/15) for filter show in div.
    var htmlstr = "";
    var value1 = '';
    var flag = '';
    var isFilter = parent.$("#filters1").val();

    if (isFilter == "")
        flag = 'black';
    else {
        flag = 'red';

        var isFilter1 = JSON.parse(parent.$("#filters1").val());
        var key1 = Object.keys(isFilter1);
        for (var key in key1) {
            value1 = isFilter1[key1[key]];
            if (value1 != "")
            {
                flag = 'red'
            } else
            {
                flag = 'black'
            }
            // alert(""+value);
            for (var val in value1) {
                htmlstr += "<span>" + value1[val] + ",</span>";
            }
        }
        $("#applied_filter").html(htmlstr);
    }
// end by mayank sh.

    $("#loading").hide();
    if (checkBrowser() == "ie") {
        $("#xtendChartssTD").html('');
    }
    $("#xtendChartTD").html('');

    $("#content_1").html('');
    $("#content_Drag").html('');
//    grid.remove_all_widgets();    // added by manik
    if (typeof (jsonData) != Object) {
        graphData = JSON.parse(jsonData);
    }
    if (typeof parent.$("#chartData").val() != "undefined" && parent.$("#chartData").val() != "") {
        var chartData = JSON.parse(parent.$("#chartData").val());
    } else {
        var chartData = [];
    }

//  for(var w=0;w<10000;w++)
    var divContent = "";
    var chartId = "";
    var charts = [];
    var repId;
    var repname;
   
        charts = Object.keys(chartData);
   
    var width = $(window).width();
    var divHeight = $(window).height();
    var divContent = "";
    var widths = [];
    var heights = [];
    var widFact = 0;
    var heightFact = 0;
    var j = 0;
    var sameRowFlag = true;
    var divcontlist = "";
    var divContentC = "";
    var filterHtml = "";
//     divContent += "<div  style='width:100%'>";
    
        if (typeof filterData != "undefined" && filterData != "") {
            var filterKeys = Object.keys(filterData);
        } else {
            var filterKeys = JSON.parse(parent.$("#viewby").val());
        }

        // divContent += "</tr></table>";
        //  $("#xtendChartTD").html(divContent);
        try {


            var viewIds = JSON.parse(parent.$("#viewbyIds").val());
            //    divContent += "</tr></table>";
            //     <div id="content_1" class="content" style="width: 100%">
            // <div id="vhead14" style="display:none" class="panelcollapsed">
            //                    <h2><label id="header14" class="headl"></label></h2>
            //                    <div id="vsdiv14" class="panelcontent">
            //                    </div>
            //                </div>

            //    divContentC += "<div width='13%' id='content_1' class='content' style='width:13%;float:right;padding-top:1em; height:490px;overflow:auto;'>";
            if (parent.userType == "ANALYZER") {
                $("#xtendChartssTD").append("<div  id='content_1' class='content hideAllDiv' style='width:180px;float:right;height:490px;overflow:auto;display:none'></div>");
            } else {
                $("#xtendChartssTD").append("<div  id='content_1' class='content hideAllDiv' style='width:180px;float:right;height:490px;overflow:auto;display:none'></div>");

            }
            if (flag == "red") {

                filterHtml += "<span id='filterFlag' value='red' ><img float='right' src='images/filter_red.png' onclick='showDragBox()' ></img></span>";
            } else
                filterHtml += "<span id='filterFlag' value='black' ><img float='right' src='images/filter.png' onclick='showDragBox()' ></img></span>";
            filterHtml += "<span><strong style='white-space:nowrap; color:black;text-align:center;padding-left:5%'>Graph Filters</strong></span>";
            filterHtml += "<span><input   type='button' onclick='applyfilterIE()'  value='Go'  style='width:25px;'></span>";

            var viewByIds = JSON.parse($("#viewbyIds").val());
            var viewBy = JSON.parse($("#viewby").val());
            for (var key in filterKeys) {
                var drills = {};
                var grpKeys;
                try {
                    drills = JSON.parse($("#drills").val());
                    grpKeys = Object.keys(drills);
                } catch (e) {
                }
                var index = viewBy.indexOf(filterKeys[key]);
                if (index == -1) {
                    index = viewBy.indexOf(" " + filterKeys[key])
                }
                var viewId = viewByIds[index];
                var filterGroupBy = filterKeys[key];

                //        var viewId = viewIds[key];
                var filters = filterData[filterGroupBy];
                var selectedFilters = [];
                var filterMap = {};
                var filterValues = [];
                if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
                    filterMap = JSON.parse($("#filters1").val());
                    if (typeof filterMap[viewId] !== "undefined") {
                        filterValues = filterMap[viewId];
                    }
                }

                if (typeof filterMap[viewId] !== "undefined") {
                    selectedFilters = filterMap[viewId];
                }
                filterHtml += "<div class='expandDiv" + filterGroupBy.replace(/\s/g, '') + " expandDiv' name='expand' width='13%' onclick='expandDiv(\"" + filterGroupBy.replace(/\s/g, '') + "\")' style='border-bottom: 1px solid lightgrey;padding-top:.5em;padding-bottom:.5em;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:1px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";

                if (selectedFilters.length === 0) {
                    filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' checked onclick='selectAll(this.id,this.name)'/>";
                } else {
                    filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' onclick='selectAll(this.id,this.name)'/>";
                }
                if (filterGroupBy.length > 25) {
                    filterHtml += "<span style='white-space:nowrap'>&nbsp;" + filterGroupBy.substring(0, 25) + "..</span></label></div>";
                } else {
                    filterHtml += "<span style='white-space:nowrap'>&nbsp;" + filterGroupBy + "</span></label></div>";
                }
                filterHtml += "</div>";
                filterHtml += "<div id='vsdiv14" + filterGroupBy.replace(/\s/g, '') + "' class='collapseDiv' style='display:none'><table style='' width='100%'>";
                var isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/));
                if (isAtLeastIE11)
                {
                    var size = 100;
                    if (typeof filters !== "undefined" && size > filters.length) {
                        size = filters.length;
                    }
                } else
                {
                    size = filters;
                }

                for (var filter in size) {

                    filterHtml += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
                    if (selectedFilters.indexOf(filters[filter]) !== -1 && filters[filter] != "") {
                        filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_" + filter + "' value='" + filters[filter] + "' name='" + filterGroupBy + "*," + viewId + "'  onclick='applyFilter(this.id,this.name)'/>";
                    } else {
                        if (filters[filter] != "") {
                            filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_" + filter + "' checked value='" + filters[filter] + "' name='" + filterGroupBy + "*," + viewId + "' onclick='applyFilter(this.id,this.name)'/>";
                        }
                    }
                    filterHtml += "<span class='box'>&nbsp;" + filters[filter] + "</span></span></label></td></tr>";
                }
                filterHtml += "</table></div>";
            }
            filterHtml += "</div>";


            var viewIds = JSON.parse($("#viewbyIds").val());
            //    divContent += "</tr></table>";
            //     <div id="content_1" class="content" style="width: 100%">
            // <div id="vhead14" style="display:none" class="panelcollapsed">
            //                    <h2><label id="header14" class="headl"></label></h2>
            //                    <div id="vsdiv14" class="panelcontent">
            //                    </div>
            //                </div>

            //    divContentC += "<div width='13%' id='content_1' class='content' style='width:13%;float:right;padding-top:1em; height:490px;overflow:auto;'>";
            // added by krishan
            if (checkBrowser() == "ie") {
                $("#content_Drag").html(filterHtml);
            } else {
                $("#content_1").html(filterHtml);
            }
            var textDivHeight = screen.height;
            if (parent.userType == "ANALYZER") {
                $("#xtendChartssTD").append("<div id='content_Drag' class='content hideAllDiv' style='width:180px;float:right;height:" + textDivHeight + "px;overflow:auto;'></div>");
            } else {
                $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;float:right;height:460px;overflow:auto;position:fixed;margin-top:;background-color: rgb(255, 255, 255); box-shadow: 0px 0px 2px 2px rgb(197, 66, 66); margin-top: 1px; border-radius: 5px;right: 20px;box-shadow:0px 1px 4px 2px rgb(227, 221, 221);margin-right: 2px;display:none;background-color: #FFF;z-index:11'></div>");
            }
            divContentC += "<div style='position:fixed;margin-left:2%'>"
            if (checkBrowser() == "ie") {
                if (flag == "red") {
                    divContentC += "<span id='filterIcon' value='red' ><img float='right' src='images/filter_red.png' onclick='showHideFilter()' ></img></span>";
                } else
                    divContentC += "<span id='filterIcon' value='black'><img float='right' src='images/filter.png' onclick='showHideFilter()' ></img></span>";
            }
            divContentC += "<span title='Parameters' align='center' style='font-size:16px;white-space:nowrap; text-align:center;'>" + translateLanguage.Parameters + "</span></br>";
            divContentC += "<span  style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>" + translateLanguage.Drag_Parameters + "</span>";
            divContentC += "</div>"
            var viewByIds = JSON.parse($("#viewbyIds").val());
            var viewBy = JSON.parse($("#viewby").val());

            divContentC += "<br>"
            //    divContentC += "<table>"
            divContentC += "<div style='position:fixed; margin-top: 1%'>"
            divContentC += "<div class = 'hiddenscrollbar' style=''>"
            divContentC += "<div class = 'innerhiddenscrollbar'  style='height:150px;width:150px'>"
            divContentC += "<ul class='rightDragMenu' style='list-style-type:none;pading:2px'>"
            for (var key in filterKeys) {
                var drills = {};
                var grpKeys;
                try {
                    drills = JSON.parse($("#drills").val());
                    grpKeys = Object.keys(drills);
                } catch (e) {
                }
                var index = viewBy.indexOf(filterKeys[key]);
                if (index == -1) {
                    index = viewBy.indexOf(" " + filterKeys[key])
                }
                var viewId = viewByIds[index];
                var filterGroupBy = filterKeys[key];

                //        var viewId = viewIds[key];
                //        var filters = filterData[filterGroupBy];
                var selectedFilters = [];
                var filterMap = {};
                var filterValues = [];
                if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
                    filterMap = JSON.parse($("#filters1").val());
                    if (typeof filterMap[viewId] !== "undefined") {
                        filterValues = filterMap[viewId];
                    }
                }

                if (typeof filterMap[viewId] !== "undefined") {
                    selectedFilters = filterMap[viewId];
                }


                //         divContentC += "<tr ><span draggable='true' id='"+filterGroupBy+"'   ondragstart='drag(event,this.id)'>"+filterGroupBy+"</span><br></tr>"

            }
            for (var k = 0; k < viewBy.length; k++) {
                if (viewBy[k].length > 15) {
                    divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                    divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='" + viewBy[k] + "'   ondragstart='drag(event,this.id)'  ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + viewBy[k] + "' >" + viewBy[k].substring(0, 15) + "..</span></li>"
//                divContentC += "<hr style='display: block;border-style: solid;border-width: 1px;color:#e4e4e4'>";
                } else {
                    divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                    divContentC += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='" + viewBy[k] + "'   ondragstart='drag(event,this.id)'  ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + viewBy[k] + "'>" + viewBy[k] + "</span></li>"
//                divContentC += "<hr style='display: block;border-style: solid;border-width: 1px;color:#e4e4e4'>";
                }
            }
            divContentC += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
            divContentC += "</ul>"
            divContentC += "</div>"
            divContentC += "</div>"

            var measures = JSON.parse($("#measure").val())

            divContentC += "<div style='position:fixed;margin-top:;margin-left:2%'>"
            divContentC += "<span title='Measures' align='center' style='font-size:16px;white-space:nowrap;text-align:center;'>" + translateLanguage.Measures + "</span></br>";
            divContentC += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>" + translateLanguage.Drag_Measures + "</span>";
            divContentC += "</div>";
            divContentC += "<div style='position:fixed;margin-top:2%;'>"
            divContentC += "<div class = 'hiddenscrollbar' style=''>"
            divContentC += "<div class = 'innerhiddenscrollbar'  style='height:180px;width:150px'>"
            divContentC += "<ul style='list-style-type:none' class='rightDragMenu'>"
            for (var j in measures) {
                if (measures[j].length > 20) {
                    divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                    divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='" + measures[j] + "' onclick='measureDriven(this.id)'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + measures[j] + "'>" + measures[j].substring(0, 15) + "..</span></li>"
                } else {
                    divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                    divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='" + measures[j] + "' onclick='measureDriven(this.id)'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='" + measures[j] + "'>" + measures[j] + "</span></li>"
                }

            }
            divContentC += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
            divContentC += "</ul>"
            divContentC += "</div>"
            divContentC += "</div>"
            divContentC += "</div>"

        } catch (e) {
        }
        divContentC += "</div>";
        var textHtml = "";
        textHtml += "<div id='textMainDiv' style='width:98%;height:" + (textDivHeight - 10) + "px;border:.5px solid grey;overflow:hidden'>";
        textHtml += "<div id='imageDiv' style='width:98%;height:auto;'>"
        textHtml += "<span id='iconLoad' value='black'><img src='images/udisenewimage.gif' style='display:block;margin-left:auto;margin-right:auto;'  ></img></span>";
        textHtml += "</div>";
        textHtml += "<div id='textContentDiv' style='width:98%;height:" + textDivHeight * 0.50 + "px'>";
        textHtml += "<p>U-Analyze envisaged providing information for analysis and action to the potential users.</p>"
        textHtml += "<p> The Unique feature is to present information longitudinally i.e. over years unlike otherwise available in annual silos.</p>"
        textHtml += "<p><h1 style='text-align:center'>Objectives </h1></p>"
//  textHtml += "<svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>";
        textHtml += "<p><svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>Information for all<br>" +
                "<svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>Barrier Free Information<br>" +
                "<svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>Data Visualization <br>" +
                "<svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>Dynamic Navigation <br>" +
                "<svg width='25' height='15'><circle cx='15' cy='10' r='5' stroke-width='3' fill='gray' /></svg>Pre-Defined and User-Defined Reports</p>";
        textHtml += "</div>"
        textHtml += "</div>";
        //    divContentC +="<table id='legendsTable' style='width:11%;float:left'><tr><td>Legends..</td></tr></table>";
        $("#noData").hide();
        $("#xtendChartTD1").show();

        if (checkBrowser() == "ie") {

            $("#content_Drag").append(filterHtml);
        } else if (parent.userType == "ANALYZER") {
            $("#content_Drag").html('');
            $("#content_Drag").append();
        } else {
            $("#content_Drag").append(divContentC);
        }
//                $("#content_Drag").append(divContentC);
//                $("#content_1").show();
  
//added by manik
    var row = 1;
    var col = 1;
    var id = 1;
    var html = "";
    var sizeGrid_x = 1;
    var sizeGrid_y = 1;
    var k1 = 0;
    var loopLength = 0;
    if (typeof parent.$("#draggableViewBys").val() != "undefined" && parent.$("#draggableViewBys").val() != "" && dragFlag == "drag") {
        var dragObject = JSON.parse(parent.$("#draggableViewBys").val())
        var chartKeys = Object.keys(chartData)
        var chartLength = chartData
        loopLength = (dragObject.length)
        k = loopLength - 1

        divcontlist = "";
        //        var widthPer = "50%";//$(window).width()/2+"px";
        var singleLine = "1px";
        var chartDetails = chartData["chart" + (k + 1)];
        chartId = "chart" + (parseInt(k) + 1);
        if (fromoneview != 'null' && fromoneview == 'true') {
            chartId = charts;
            chartDetails = chartData[chartId];
            //                     alert(chartData[chartId]["chartType"])
        }
        var tableId = "table" + (parseInt(k) + 1);
        var commentId = "comment" + (parseInt(k) + 1);
        widths.push(width / 1.5);
        heights.push(divHeight / 1.5);
        widFact = (widFact) + (45);
        heightFact = (heightFact) + (33.23);
        if (widFact > 68) {
            widFact = 0;
            singleLine = "0px";
        } else if (widFact === 0) {
            singleLine = "0px";
        } else {
            singleLine = "2px";
        }
        if (fromoneview != 'null' && fromoneview == 'true') {
            if (typeof chartData[chartId] !== "undefined" && chartData[chartId]["size"] === "M") {
                bottomLine = "1px";
            }
            //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
            //            bottomLine = "1px";
            //            widFact = 0;
            //        }
            divHeight = $(window).height() / 1.7;
            var chartName = "";
            if (typeof chartData[chartId]["Name"] !== "undefined" && chartData[chartId]["Name"] !== "" && chartData[chartId]["Name"] !== "undefined") {
                chartName = chartData[chartId]["Name"];
            } else {
                chartName = chartId;
            }
        } else {

            if (typeof chartData["chart" + (k + 2)] !== "undefined" && chartData["chart" + (k + 2)]["size"] === "M") {
                bottomLine = "1px";
            }
            //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
            //            bottomLine = "1px";
            //            widFact = 0;
            //        }
            divHeight = $(window).height() / 1.7;
            var chartName = "";
            if (typeof typeof chartData["chart" + (k + 1)] !== "undefined" && typeof chartData["chart" + (k + 1)]["Name"] !== "undefined" && chartData["chart" + (k + 1)]["Name"] !== "") {
                chartName = chartData["chart" + (k + 1)]["Name"];
            } else {
//            chartName = "chart" + (k + 1);
                chartName = "";
            }
        }
        if (typeof chartData[chartId]["chartType"] !== "undefined" && (chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar")) {

            widthPer = "100%"
        }

        var viewbys = chartDetails.viewBys;
        var measures = chartDetails.meassures;
        var chartMeasure = [];
        chartMeasure.push(measures[0]);
        var chartViewby = [];
        chartViewby.push(viewbys[0]);
        if (fromoneview != 'null' && fromoneview == 'true') {
            //                     chartId=charts;
            if (typeof divcount !== 'undefined') {
                divcount;
                k = divcount - 1;
            } else {
                divcount = k + 1;
            }
        }
        //edited by manik for dynamic div
        //        divContent += "<td class='DroppableDiv' id='divchart" + (k + 1) + "' style='overflow:auto;position:relative;width:" + widthPer + ";height:" + divHeight + ";float:left;'>" +
        if (checkBrowser() == "ie") {

            divcontlist += "<div id='divchart" + (k + 1) + "'  style='float:left;width:42%;height:56%;margin-left:0.5%;margin-top:0.5%; border: 1px dotted rgb(200, 200, 200);' >";
        } else {
            if (fromoneview != 'null' && fromoneview == 'true') {

                divcontlist += "<li id='divchart" + (k + 1) + "' data-row='" + row + "' data-col='" + col + "' data-sizex='" + sizeGrid_x + "' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >";
            } else {
                divcontlist += "<li id='divchart" + (k + 1) + "' data-row='" + row + "' data-col='" + col + "' data-sizex='" + sizeGrid_x + "' ondrop='drop(event,this.id)' class='dragClass' ondragover='allowDrop(event)' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >";

            }
        }
        divcontlist += "<table id='tablechart" + (k + 1) + "' align='left' class='dashHeader' style='display:none'>" +
                "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator' onclick='editDash(\"" + chartId + "\",\"" + tableId + "\",\"kpi_div" + (k + 1) + "\",\"" + divHeight + "\",\"" + widths[k] + "\")' />" +
                " <img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart" + (k + 1) + "' onclick='editViewBys(this.name)'/>" +
                "<img align='left' title='Refresh' onclick=\"refreshChart('" + chartId + "')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
                "<img align='left' title='Save' style=\"cursor: pointer\" onclick='saveXtCharts()' class='ui-icon ui-icon-newwin'/>" +
                "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart" + (k + 1) + "' onclick='openCharts(this.name,\"" + widths[k] + "\",\"" + divHeight + "\")' />" +
                "<img id='localFilter" + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' onclick='getLocalFilters(\"" + chartId + "\")'/>" +
                "<img id='I_row" + j + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
                "<img id='D_row" + j + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
                "<img style='cursor: pointer;position:relative' align='left' title='Information Div' class='ui-icon ui-icon-comment' name='' onclick='openChartComment(\"" + chartId + "\",\"" + commentId + "\",\"" + divHeight + "\")' />" +
//                "</td><td align='right' class='headDb'><a href='#' onclick=\"openRenameDiv('" + chartId + "')\"><span id='dbChartName" + chartId + "'>" + chartName + "</span></a></td></tr></table>" +
                "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('" + chartId + "')\">";
        if (typeof chartData[chartId]["chartType"] != "undefined" && (chartData[chartId]["chartType"] != "Radial-Chart" && chartData[chartId]["chartType"] != "Standard-KPI" && chartData[chartId]["chartType"] != "Standard-KPI1" && chartData[chartId]["chartType"] != "KPIDash" && chartData[chartId]["chartType"] != "Bullet-Horizontal" && chartData[chartId]["chartType"] != "Emoji-Chart" && chartData[chartId]["chartType"] != "Dial-Gauge")) {

            if (fromoneview != 'null' && fromoneview == 'true') {
                divcontlist += "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +
                        "<table style='width:100%;background-color:#FFF'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:70%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>" + chartName + "</tspan></span></td>";
            } else {
                divcontlist += "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +
                        "<table style='width:100%'><tr><td id='renameTitle" + chartId + "' style='padding-left:1%;width:70%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>" + chartName + "</tspan></span></td>";
            }
        } else {

            divcontlist += "<span id='dbChartName" + chartId + "' style ='display:'></span></a></td></tr></table>";
            if (fromoneview != 'null' && fromoneview == 'true') {
                divcontlist += "<table style='width:100%;background-color:#FFF'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:81%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";
            } else {
                divcontlist += "<table style='width:100%'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:81%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";
            }
        }
        if (fromoneview != 'null' && fromoneview == 'true') {
            //                 var fun="callGraphOpt11("+k+")";
            //                     //divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt1(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",'"+chartName+"','null','"+repId+"','"+repname+"','null')' ></img></span>"+
            //                     divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:80%' src='images/moreIcon.png' alt='Options' onclick=\""+fun+"\" ></img></span>"+
            //                              "<ul id='graphOptionchart"+(k+1)+"' class='dropdown-menu' ></ul></td></tr></table>"+
            //        "<div id='breadcrumpId' style='display:block'></div>";
            divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:80%;width:10px;height:10px'; title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt1( \"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + chartName + "\",null,\"" + repId + "\",\"" + repname + "\",\"" + enablefilter + "\")' ></img></span>" +
                    "<ul id='graphOptionchart" + (k + 1) + "' class='dropdown-menu' style='margin-left: -13em;' ></ul></td></tr></table>" +
                    "<div id='quickFilterDivchart" + (k + 1) + "' style='display:block'></div>";
            "<div id='breadcrumpId' style='display:block'></div>";
//    callGraphOpt11 = function(k){
//    callGraphOpt1( chartId,tableId,(k + 1),chartName,chartData,repId,repname,enablefilter)
//    }
        } else {
            //add by mayank sh. for top bottom chart
//        divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='topBottom"+chartId+"' title = 'Top Bottom' style='width:18px;height:12px' src='images/topBottom.png' alt='Options' onclick='topBottomChart(\"" + chartId + "\",event)'></img></span>"+
//    "<ul id='topBottom1"+chartId+"' class=''></ul></td>";
            divcontlist += "<td id='localfilter_red" + chartId + "' style='width:1%;' class=''><div class='' style='cursor:pointer;z-index:2;'><span  data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter" + chartId + "' width='10' height='10' style='display:none;margin-left:20%' src='images/filter_red.png' alt='Options' onmouseover='showAppliedFilters(\"" + chartId + "\",event)'  ></img></span>" +
                    "<ul id='localFilter1" + chartId + "' class=''>";
            var type = $("#type").val();
            //ad local filter
            // alert(chartData[chartId]["filters"]) 
            if (typeof chartData[chartId]["filterMap"] != 'undefined' && chartData[chartId]["filterMap"] != "") {
            } else {
                var display = '';
                if (typeof chartData[chartId]["filters"] != 'undefined' && Object.keys(chartData[chartId]["filters"]).length != 0) {
                    display = 'block';
                } else {
                    display = 'none';
                }

                var anchorChart = chartData["chart1"]["anchorChart"];
                if (typeof chartData["chart1"]["anchorChart"] !== 'undefined' && chartData["chart1"]["anchorChart"] !== '') {
                    var keys1 = Object.keys(chartData["chart1"]["anchorChart"]);
                    var initCharts = chartData["chart1"]["anchorChart"][keys1[0]];
                    for (var j in initCharts) {
                        if (chartId == initCharts[j]) {
                            divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='initicon_'" + chartId + " title = 'Initialized chart' style='width:14px;height:12px' src='images/anchor.png' alt='Options' onclick='disableInitChart(\"" + chartId + "\")'></img></span>" +
                                    "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                        }
                    }
                }
//    Edited by Faiz Ansari
                if (typeof chartData["chart1"]["anchorChart"] !== 'undefined' && chartData["chart1"]["anchorChart"] !== '' && Object.keys(anchorChart)[0] === chartId) {
                    divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Define Initialize Charts' style='width:12px;height:12px' src='images/driver.png' alt='Options' onclick='initializeGraph1(\"" + chartId + "\",\"prev\")'></img></span>" +
                            "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                }
//edit by shivam
                if (typeof chartData[chartId]["chartType"] !== "undefined" && chartData[chartId]["chartType"] == "world-map" || chartData[chartId]["chartType"] == "World-City" || chartData[chartId]["chartType"] == "World-Top-Analysis") {
                    divcontlist += "<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getGeoWorldMap(\"" + chartId + "\")'><i class='fa fa-globe'></i></span></td>";
                }
                if (typeof chartData[chartId]["chartType"] !== "undefined" && chartData[chartId]["chartType"] == "Trend-Combo") {
                    if (typeof chartData[chartId]["trendViewMeasures"] !== "undefined" && chartData[chartId]["trendViewMeasures"] === "ViewBys") {
                    } else {
                        divcontlist += "<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getParentMeasure(\"" + chartId + "\")'><i class='fa fa-level-up'></i></span></td>";
                    }
                }
                var showhide = "";
                if (chartData[chartId]["iconShowHide"] == "No") {
                    showhide = "none";
                }
                if (typeof typeof chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] != "" && (chartData[chartId]["chartType"] == "Trend-Combo" || chartData[chartId]["chartType"] == "Trend-KPI")) {
//    divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Comparison' style='margin-left:20%;width:14px;height:13px;padding:2px' src='images/icons/Comparison.png' alt='Options' onclick='getComparableViews(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='compare"+chartId+"' class='dropdown-menu'></ul></td>";
                    divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"prev\")' ></i></span>" +
                            "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                    divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"prev\")' ></i></img></span>" +
                            "<ul id='currPrevRecords" + chartId + "' class=''></ul></td>";

                    divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"next\")' ></i></span>" +
                            "<ul id='currNextRecords" + chartId + "' class=''></ul></td>";
                    divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"next\")' ></i></span>" +
                            "<ul id='nextRecords" + chartId + "' class=''></ul></td>";

                }
                divcontlist += "</ul></td>";
//       divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' title = 'Local Filter' style='width:10px;display:"+display+";height:10px' src='images/filter_red.png' alt='Options'></img></span>"+
//    "<ul  class='dropdown'></ul></td>";
            }
            // add new refersh
            divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer;z-index:2;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Refresh' style='width:10px;height:10px' src='images/refersh_image.png' alt='Options' onclick='localRefresh(\"" + chartId + "\")'></img></span>" +
                    "<ul id='newRefresh" + chartId + "' class=''></ul></td>";

            if ((window.userType !== "A") || (window.userType !== "ANALYZER")) {
                //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
                if (typeof type !== "undefined" && type == "trend") {
                    divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtTrend(\"" + chartId + "\")' ></img></span>" +
                            "</td>";
                } else {
//          divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtCharts(\"" + chartId + "\")' ></img></span>"+
                    divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='savePointXtCharts()' ></img></span>" +
                            "</td>";
                }
            }
//    divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'drill up' style='margin-left:20%;width:16px;height:16px' src='images/drillupgrey.png' alt='Options' onclick='drillUp(\"" + chartId + "\")' ></img></span>"+
//    "<ul id='drillUp"+chartId+"' class=''></ul></td>";
            divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = '" + translateLanguage.Drill_Up + "' width='10' height='10' style='margin-left:20%' src='images/drillupgrey1.png' alt='Options'  onclick='createDrillUpPath(\"" + chartId + "\",event)' ></img></span>" +
                    "<ul id='drillUp1" + chartId + "' class='dropdown-menu'>";

            divcontlist += "</ul></td>";

//divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' title = 'Change Measure' style='margin-left:20%;' src='images/icons/changeMeasure.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")' ></img></span>"+
//         "<ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></td>";
            //        divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:11px;height:14px' src='images/treeViewImages/Dim.gif' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//    divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:10px;heigh:10px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//    "<ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></td>";
            //        divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'delete Chart' style='margin-left:20%;width:16px;height:16px' src='images/deleteWidget.png' alt='Options' onclick='deleteChart(\"" + chartId + "\")' ></img></span>"+
            "<ul id='delete" + chartId + "' class=''></ul></td>";
            divcontlist += "<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' style='margin-left:20%;' title='Change Graphs' src='images/bar_icon.png' alt='Options' onclick='adjustPosition(\"" + chartId + "\",event)'  ></img></span>" +
                    "<ul id='chartTypesX" + chartId + "' class='dropdown-menu'>";
            var graphTypes = Object.keys(parent.graphsNameTemplate);
            for (var i = 0; i < parent.chartGroupsTemplate.length; i++)
            {
                divcontlist += "<li id='chartGroupsX" + chartId + i + "' class='dropdown-submenu pull-left1'><a onmouseover='openChartGroup(" + i + ",\"" + chartId + "\",\"X\",\"" + parent.chartGroupsTemplate[i] + "\")' >" + parent.chartGroupsTemplate[i] + "</a>";
            }
            divcontlist += "</ul></td>";

            divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img  style='width:10px;height:10px;' title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\",event)' ></img></span>" +
                    "<ul id='graphOption" + chartId + "' class='dropdown-menu'></ul></td></tr></table>" +
                    "<table style='width:100%;height:8px;'></table>";
            "<div id='quickFilterDiv" + chartId + "' style='display:block'></div>";
            "<div id='breadcrumpId' style='display:block'></div>";
            "<div id='breadcrumpId" + chartId + "' style='display:block'></div>";
        }
        divcontlist += "<div align='center' id='chart" + (k + 1) + "' style='height:95%' ;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
                "<div id='" + commentId + "' style='display:none'></div>";

//                "<span class=\"dashComment\" onclick='openChartComment(\""+chartId+"\")'></span>"+



        divcontlist += "</div>";

        var drilledvalue1 = parent.$("#drills").val();
        var drilledvalue = {};
        var drilledvalue3 = [];
        var drilledvalue2 = [];
        var drilltype = $("#drilltype").val();
        var htmlcontent = "";
        if (chartId == breadCrump) {
            if (typeof drilledvalue1 !== 'undefined' && drilledvalue1.length > 0 && (typeof drilltype != "undefined" && drilltype === "within")) {

                divcontlist += "<div id = breadcrump'" + chartId + "' style='display:block;margin-top:-15px;padding-left: 1em;' ><table><tr style = 'width:'" + widths[k] + "'>";

                if (drilledvalue1 != "undefined" && drilledvalue1 != "") {

                    drilledvalue2 = JSON.parse(drilledvalue1);
                    drilledvalue3 = Object.keys(drilledvalue2);

                    for (var i = 0; i < drilledvalue3.length; i++) {
                        drilledvalue[i] = drilledvalue2[drilledvalue3[i]]
                        divcontlist += " <td style ='color:#808080;padding-left: .5em;'>" + drilledvalue[i] + " ></td>";
                    }
                }
                divcontlist += "</tr></table></div>";
            }
        }
//edited by manik for dynamic div
        if (checkBrowser() != "ie") {
            divcontlist += "<div id='" + tableId + "''></div></li>";
        } else {
            divcontlist += "<div id='" + tableId + "''></div></div>";
        }
        if (fromoneview != 'null' && fromoneview == 'true') {

            if (chartData[chartId]["row"] != "undefined") {
                id = parseInt(chartData[chartId]["id"]);
                row = parseInt(chartData[chartId]["row"]);
                col = parseInt(chartData[chartId]["col"]);
                sizeGrid_x = parseInt(chartData[chartId]["size_x"]);
                sizeGrid_y = parseInt(chartData[chartId]["size_y"]);
                // sizeGrid_x=10;
                // sizeGrid_y=7;
            } else {

                sizeGrid_x = 15;
                sizeGrid_y = 9;

                if (k % 2 == 0) {
                    col = 1;
                    row = (parseInt(k) + 9);
                } else {
                    col = 16;
                }
            }
        } else {

            //alert("chartdata "+JSON.stringify(chartData["chart" + (k + 1)]))
            if (typeof chartData["chart" + (k + 1)]["row"] != "undefined") {
                id = parseInt(chartData["chart" + (k + 1)]["id"]);
                row = parseInt(chartData["chart" + (k + 1)]["row"]);
                col = parseInt(chartData["chart" + (k + 1)]["col"]);
                sizeGrid_x = parseInt(chartData["chart" + (k + 1)]["size_x"]);
                sizeGrid_y = parseInt(chartData["chart" + (k + 1)]["size_y"]);
            } else {
                // sizeGrid_x=4;
                // sizeGrid_y=3;
                sizeGrid_x = 15;
                sizeGrid_y = 9;

                // if(k%2==0){
                //      col=1;
                //      row=k+2;
                //    }else{
                //      col=5;
                //        }
                if (k % 2 == 0) {
                    col = 1;
                    row = (parseInt(k) + 9);
                } else {
                    col = 16;
                }

            }
        }
        if (checkBrowser() != "ie") {
            if (fromoneview != 'null' && fromoneview == 'true') {
                //    alert("dic"+divcontlist)
                if (action1 == 'add') {
                    grid.add_widget(divcontlist, sizeGrid_x, sizeGrid_y);
                } else {
                    grid.add_widget(divcontlist, sizeGrid_x, sizeGrid_y, col, row);
                }
                //    grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);

            } else {
                grid.add_widget(divcontlist, sizeGrid_x, sizeGrid_y);
                //   grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
            }
        } else {

            $("#xtendChartssTD").append(divcontlist);
        }
//grid.add_widget(divcontlist,parseInt(sizeGrid_x),parseInt(sizeGrid_y),parseInt(col),parseInt(row));
//grid.add_widget(divcontlist,15,9,col,row);

        $("#xtendChartssTD").show();
        parent.$("#gridsterobj").val(grid);
    } else {
        loopLength = (charts.length)
        for (var k = 0; k < loopLength; k++) {
            divcontlist = "";
            //        var widthPer = "50%";//$(window).width()/2+"px";
            var singleLine = "1px";
            var chartDetails = chartData["chart" + (k + 1)];
            chartId = "chart" + (parseInt(k) + 1);
          
            var tableId = "table" + (parseInt(k) + 1);
            var commentId = "comment" + (parseInt(k) + 1);
            widths.push(width / 1.5);
            heights.push(divHeight / 1.5);
            widFact = (widFact) + (45);
            heightFact = (heightFact) + (33.23);
            if (widFact > 68) {
                widFact = 0;
                singleLine = "0px";
            } else if (widFact === 0) {
                singleLine = "0px";
            } else {
                singleLine = "2px";
            }
           

                if (typeof chartData["chart" + (k + 2)] !== "undefined" && chartData["chart" + (k + 2)]["size"] === "M") {
                    bottomLine = "1px";
                }
                //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
                //            bottomLine = "1px";
                //            widFact = 0;
                //        }
                divHeight = $(window).height() / 1.7;
                var chartName = "";
                if (typeof chartData["chart" + (k + 1)] !== "undefined" && typeof chartData["chart" + (k + 1)]["Name"] !== "undefined" && chartData["chart" + (k + 1)]["Name"] !== "") {
                    chartName = chartData["chart" + (k + 1)]["Name"];
                } else {
                    chartName = "chart" + (k + 1);
                }
            if (typeof chartData["chart" + (k + 1)] !== "undefined" && typeof chartData[chartId]["chartType"] !== "undefined" && (chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar")) {

                widthPer = "100%"
            }

            var viewbys = chartDetails.viewBys;
            var measures = chartDetails.meassures;
            var chartMeasure = [];
            chartMeasure.push(measures[0]);
            var chartViewby = [];
            chartViewby.push(viewbys[0]);
           
            //edited by manik for dynamic div
            //        divContent += "<td class='DroppableDiv' id='divchart" + (k + 1) + "' style='overflow:auto;position:relative;width:" + widthPer + ";height:" + divHeight + ";float:left;'>" +
            if (checkBrowser() != "ie") {
                    divcontlist += "<li id='divchart" + (k + 1) + "' data-row='" + row + "' data-col='" + col + "' data-sizex='" + sizeGrid_x + "' ondrop='drop(event,this.id)' class='dragClass' ondragover='allowDrop(event)' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >";
            } else {
                divcontlist += "<div id='divchart" + (k + 1) + "'  style='float:left;width:42%;height:56%;margin-left:0.5%;margin-top:0.5%; border: 1px dotted rgb(200, 200, 200);' >";
            }
            divcontlist += "<table id='tablechart" + (k + 1) + "' align='left' class='dashHeader' style='display:none'>" +
                    "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator' onclick='editDash(\"" + chartId + "\",\"" + tableId + "\",\"kpi_div" + (k + 1) + "\",\"" + divHeight + "\",\"" + widths[k] + "\")' />" +
                    " <img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart" + (k + 1) + "' onclick='editViewBys(this.name)'/>" +
                    "<img align='left' title='Refresh' onclick=\"refreshChart('" + chartId + "')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
                    "<img align='left' title='Save' style=\"cursor: pointer\" onclick='saveXtCharts()' class='ui-icon ui-icon-newwin'/>" +
                    "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart" + (k + 1) + "' onclick='openCharts(this.name,\"" + widths[k] + "\",\"" + divHeight + "\")' />" +
                    "<img id='localFilter" + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' onclick='getLocalFilters(\"" + chartId + "\")'/>" +
                    "<img id='I_row" + j + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
                    "<img id='D_row" + j + "_" + chartId + "' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
                    "<img style='cursor: pointer;position:relative' align='left' title='Information Div' class='ui-icon ui-icon-comment' name='' onclick='openChartComment(\"" + chartId + "\",\"" + commentId + "\",\"" + divHeight + "\")' />" +
                    //                "</td><td align='right' class='headDb'><a href='#' onclick=\"openRenameDiv('" + chartId + "')\"><span id='dbChartName" + chartId + "'>" + chartName + "</span></a></td></tr></table>" +
                    "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('" + chartId + "')\">";
            if (typeof chartData[chartId]["chartType"] != "undefined" && (chartData[chartId]["chartType"] != "Radial-Chart" && chartData[chartId]["chartType"] != "Standard-KPI" && chartData[chartId]["chartType"] != "Standard-KPI1" && chartData[chartId]["chartType"] != "KPIDash" && chartData[chartId]["chartType"] != "Bullet-Horizontal" && chartData[chartId]["chartType"] != "Emoji-Chart" && chartData[chartId]["chartType"] != "Dial-Gauge")) {

                    // Added By Ram For change color of chart name after applying measurefilter
                    if (typeof chartData[chartId]["isFilterApplied"] != "undefined" && chartData[chartId]["isFilterApplied"] === "Yes")
                    {
                        divcontlist += "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +
                                "<table style='width:100%'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:70%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:red;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>" + chartName + "</tspan></span></td>";
                    } else
                    {
                        divcontlist += "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +
                                "<table style='width:100%'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:70%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>" + chartName + "</tspan></span></td>";
                    }
            } else {

                divcontlist += "<span id='dbChartName" + chartId + "' style ='display:'></span></a></td></tr></table>" +
                        "<table style='width:100%'><tr ><td id='renameTitle" + chartId + "' style='padding-left:1%;width:81%'><span id='dbChartName" + chartId + "'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";

            }
                divcontlist += "<td id='localfilter_red" + chartId + "' style='width:1%;' class=''><div class='' style='cursor:pointer;z-index:2;'><span  data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter" + chartId + "' width='10' height='10' style='display:none;margin-left:20%' src='images/filter_red.png' alt='Options' onmouseover='showAppliedFilters(\"" + chartId + "\",event)'  ></img></span>" +
                        "<ul id='localFilter1" + chartId + "' class=''>";
                var type = $("#type").val();



                if (typeof chartData[chartId]["filterMap"] != 'undefined' && chartData[chartId]["filterMap"] != "") {
                } else {
                    var display = '';
                    if (typeof chartData[chartId]["filters"] != 'undefined' && Object.keys(chartData[chartId]["filters"]).length != 0) {
                        display = 'block';
                    } else {
                        display = 'none';
                    }
                    var anchorChart = chartData["chart1"]["anchorChart"];
                    if (typeof chartData["chart1"]["anchorChart"] !== 'undefined' && chartData["chart1"]["anchorChart"] !== '') {
                        var keys1 = Object.keys(chartData["chart1"]["anchorChart"]);
                        var initCharts = chartData["chart1"]["anchorChart"][keys1[0]];
                        for (var j in initCharts) {
                            if (chartId == initCharts[j]) {
                                divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='initicon_'" + chartId + " title = 'Initialized chart' style='width:14px;height:12px' src='images/anchor.png' alt='Options' onclick='disableInitChart(\"" + chartId + "\")'></img></span>" +
                                        "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                            }
                        }
                    }
                    if (typeof chartData["chart1"]["anchorChart"] !== 'undefined' && chartData["chart1"]["anchorChart"] !== '' && Object.keys(anchorChart)[0] === chartId) {
                        var keys2 = Object.keys(anchorChart);
                        var initChartsList = anchorChart[keys2[0]];
                        if (initChartsList.length > 0) {
                            divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Define Initialize Charts' style='width:12px;height:12px' src='images/driver.png' alt='Options' onclick='initializeGraph1(\"" + chartId + "\",\"prev\")'></img></span>" +
                                    "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                        }
                    }
                    if (typeof chartData[chartId]["chartType"] !== "undefined" && chartData[chartId]["chartType"] == "world-map") {
                        divcontlist += "<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getGeoWorldMap(\"" + chartId + "\")'><i class='fa fa-globe'></i></span></td>";
                    }
                    if (typeof chartData[chartId]["chartType"] !== "undefined" && chartData[chartId]["chartType"] == "Trend-Combo") {
                        if (typeof chartData[chartId]["trendViewMeasures"] !== "undefined" && chartData[chartId]["trendViewMeasures"] === "ViewBys") {
                        } else {
                            divcontlist += "<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getParentMeasure(\"" + chartId + "\")'><i class='fa fa-level-up'></i></span></td>";
                        }
                    }
//    Edited by Faiz Ansari
                    var showhide = "";
                    if (chartData[chartId]["iconShowHide"] == "No") {
                        showhide = "none";
                    }
                    if (typeof typeof chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] != "" && (chartData[chartId]["chartType"] == "Trend-Combo" || chartData[chartId]["chartType"] == "Trend-KPI")) {
//     divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Comparison' style='margin-left:20%;width:14px;height:13px;padding:2px' src='images/icons/Comparison.png' alt='Options' onclick='getComparableViews(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='compare"+chartId+"' class='dropdown-menu'></ul></td>"; 
                        divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"prev\")' ></i></span>" +
                                "<ul id='prevRecords" + chartId + "' class=''></ul></td>";
                        divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"prev\")' ></i></img></span>" +
                                "<ul id='currPrevRecords" + chartId + "' class=''></ul></td>";

                        divcontlist += "<td style='width:1%'display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"next\")' ></i></span>" +
                                "<ul id='currNextRecords" + chartId + "' class=''></ul></td>";
                        divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"next\")' ></i></span>" +
                                "<ul id='nextRecords" + chartId + "' class=''></ul></td>";
                    }

                    divcontlist += "</ul></td>";
//      divcontlist += "<td style='width:1%'><div  class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' title = 'Local Filter' style='width:10px;display:"+display+";height:10px' src='images/filter_red.png' alt='Options'></img></span>"+
//    "<ul  class='dropdown'></ul></td>";
                }
//        add new refresh
                divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Refresh' style='width:10px;height:10px' src='images/refersh_image.png' alt='Options' onclick='localRefresh(\"" + chartId + "\")'</img></span>" +
                        "<ul id='newRefresh" + chartId + "' class=''></ul></td>";
//        alert(callGraphOpt);
                //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
                if ((window.userType !== "A") || (window.userType !== "ANALYZER")) {
                    //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
                    if (typeof type !== "undefined" && type == "trend") {
                        divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtTrend(\"" + chartId + "\")' ></img></span>" +
                                "</td>";
                    } else {
//          divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtCharts(\"" + chartId + "\")' ></img></span>"+
                        divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='savePointXtCharts()' ></img></span>" +
                                "</td>";
                    }
                }
//            divcontlist += "<td style='width:1%'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'drill up' style='margin-left:20%;width:10px;height:10px' src='images/drillupgrey.png' alt='Options' onclick='drillUp(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='drillUp"+chartId+"' class=''></ul></td>";

                divcontlist += "<td style='width:1%;display:" + showhide + "' class='showHide" + chartId + "'><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = '" + translateLanguage.Drill_Up + "' width='10' height='10' style='margin-left:20%' src='images/drillupgrey1.png' alt='Options' onclick='createDrillUpPath(\"" + chartId + "\",event)'  ></img></span>" +
                        "<ul id='drillUp1" + chartId + "' class='dropdown-menu'>";

                divcontlist += "</ul></td>";

//          divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' title = 'Change Measure' style='margin-left:20%;' src='images/icons/changeMeasure.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")' ></img></span>"+
//         "<ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></td>";

//            divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:10px;height:10px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></td>";
                //        divcontlist += "<td style='width:1%'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Delete Chart' style='margin-left:20%;width:16px;height:16px' src='images/deleteWidget.png' alt='Options' onclick='deleteChart(\"" + chartId + "\")' ></img></span>"+
                "<ul id='delete" + chartId + "' class=''></ul></td>";
                divcontlist += "<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' style='margin-left:20%;' title='Change Graphs' src='images/bar_icon.png' alt='Options'  onclick='adjustPosition(\"" + chartId + "\",event)' ></img></span>" +
                        "<ul id='chartTypesX" + chartId + "' class='dropdown-menu'>";
                var graphTypes = Object.keys(parent.graphsNameTemplate);
                for (var i = 0; i < parent.chartGroupsTemplate.length; i++)
                {
                    divcontlist += "<li id='chartGroupsX" + chartId + i + "' class='dropdown-submenu pull-left1'><a onmouseover='openChartGroup(" + i + ",\"" + chartId + "\",\"X\")' >" + parent.chartGroupsTemplate[i] + "</a>";
                }
                divcontlist += "</ul></td>";

                divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:;width:10px;height:10px;' title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\",event)' ></img></span>" +
                        "<ul id='graphOption" + chartId + "' class='dropdown-menu'></ul></td></tr></table>" +
                        "<div id='quickFilterDiv" + chartId + "' style='display:block'></div>";
                "<div id='breadcrumpId' style='display:block'></div>";
                "<div id='breadcrumpId" + chartId + "' style='display:block'></div>";
            divcontlist += "<div align='center' id='chart" + (k + 1) + "' style='height:95%' ;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
                    "<div id='" + commentId + "' style='display:none'></div>";

            //                "<span class=\"dashComment\" onclick='openChartComment(\""+chartId+"\")'></span>"+



            divcontlist += "</div>";

            var drilledvalue1 = parent.$("#drills").val();
            var drilledvalue = {};
            var drilledvalue3 = [];
            var drilledvalue2 = [];
            var drilltype = $("#drilltype").val();
            var htmlcontent = "";
            
            //edited by manik for dynamic div
            if (checkBrowser() != "ie") {
                divcontlist += "<div id='" + tableId + "''></div></li>";
            } else {
                divcontlist += "<div id='" + tableId + "''></div></div>";
            }


                //alert("chartdata "+JSON.stringify(chartData["chart" + (k + 1)]))
                if (typeof chartData["chart" + (k + 1)]["row"] != "undefined") {
                    id = parseInt(chartData["chart" + (k + 1)]["id"]);
                    row = parseInt(chartData["chart" + (k + 1)]["row"]);
                    col = parseInt(chartData["chart" + (k + 1)]["col"]);
                    sizeGrid_x = parseInt(chartData["chart" + (k + 1)]["size_x"]);
                    sizeGrid_y = parseInt(chartData["chart" + (k + 1)]["size_y"]);
                } else {
                    // sizeGrid_x=4;
                    // sizeGrid_y=3;
                    sizeGrid_x = 15;
                    sizeGrid_y = 9;

                    // if(k%2==0){
                    //      col=1;
                    //      row=k+2;
                    //    }else{
                    //      col=5;
                    //        }
                    if (k % 2 == 0) {
                        col = 1;
                        row = (parseInt(k) + 9);
                    } else {
                        col = 16;
                    }

                }
            if (checkBrowser() != "ie") {
                    grid.add_widget(divcontlist, sizeGrid_x, sizeGrid_y, col, row);
                    //   grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
            } else {

                $("#xtendChartssTD").append(divcontlist);
            }

            //grid.add_widget(divcontlist,parseInt(sizeGrid_x),parseInt(sizeGrid_y),parseInt(col),parseInt(row));
            //grid.add_widget(divcontlist,15,9,col,row);

            $("#xtendChartssTD").show();
            parent.$("#gridsterobj").val(grid);

        }
    }

    var data = graphData;
    var radius = 600 / 3.5;
    for (var kq = 0; kq < charts.length; kq++) {
        //              var ch = "chart" + parseInt(kq+1);
        var ch;
            ch = "chart" + parseInt(kq + 1);
        var currData = [];
        var records = 12;
        if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
            records = chartData[ch]["records"];
        }
            for (var m = 0; m < (data[ch].length < records ? data[ch].length : records); m++) {
                currData.push(data[ch][m]);
            }
            chartTypeFunction(ch, chartData[ch]["chartType"], chartData[ch]["Name"]);

    }
    setTimeout($("#div" + newProp).css({
        'box-shadow': '2px 2px 5px rgb(145, 255, 0)'
    }), 3000);
    try {
        if (true) {
            var chartData = JSON.parse(parent.$("#chartData").val());
            var filters = chartData[chartId]["filters"];
            var filterKeys = Object.keys(filters);
            var viewBys = JSON.parse(parent.$("#viewby").val());
            var viewByIds = JSON.parse(parent.$("#viewbyIds").val());
            for (var i in filterKeys) {
                var filterGroupBy = viewBys[viewByIds.indexOf(filterKeys[i])];
                var filters1 = filterData[filterGroupBy];
                var filterValues = filters[filterKeys[i]]
                var inFilters = $(filters1).not(filterValues).get();

                if (inFilters.length > 0) {
                    $("#localfilter_red" + chartId).css({
                        "display": ""
                    });
                } else {
                    $("#localfilter_red" + chartId).css({
                        "display": "none"
                    });
                }
            }
        }
    } catch (err) {
    }
    if (typeof idArr !== "undefined") {
        var drillKeys = Object.keys(chartData);
        var driver = parent.$("#driver").val();
        for (var c in drillKeys) {
            if (driver !== "undefined" && driver != drillKeys[c]) {

                $("#spanChartNameDate" + drillKeys[c]).text(idArr.split(":")[0]);
            } else {
                $("#spanChartNameDate" + drillKeys[c]).text();
            }
        }
    }
}
function generateChart(jsonData, dragFlag, idArr) {


    //grid=grid;
    //alert("generateChart"+typeof grid);
    //added by manik
    var t1 = createTask1(1);
    var t2 = createTask2(1);
    //var t1 = createTask3(1);
    executeTasks(t1, t2);
    function executeTasks() {
        var tasks = Array.prototype.concat.apply([], arguments);
        var task = tasks.shift();
        task(function () {
            if (tasks.length > 0)
                executeTasks.apply(this, tasks);
        });
    }

    function createTask2(num) {


        return function (callback) {
            setTimeout(function () {

                generateChartloop(jsonData, dragFlag, idArr)
                if (typeof callback == 'function')
                    callback();
            }, 1000);
        }
    }

    function createTask1(num) {


        return function (callback) {
            setTimeout(function () {
                //if(fromoneview=='null'&& fromoneview=='false'){
//                if (fromoneview != 'null' && fromoneview == 'true') {
//
//                } else {

                    if (checkBrowser() != "ie") {

                        if (typeof grid !== "undefined")
                            try {
                                grid.remove_all_widgets();
                            } catch (e) {
                            }
                    } else {

                        $("#xtendChartssTD").html('');
                    }

//                }
                //}

                if (typeof callback == 'function')
                    callback();
            }, 1000);
        }
    }
}