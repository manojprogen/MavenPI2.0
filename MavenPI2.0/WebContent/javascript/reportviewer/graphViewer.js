var binaryOperator = false;
var conditionalMap = {};
var chartCount = 0;
var lastMesAlias = '';
var graphViewFlag = false;
var start = 0, end = 15;
var timeDBDrill = '';

// add by mynk sh.n
var advanceChartGroups = ["Bar Graph", "Circular Graph", "Line Graph"];
var barGraph = [
    "Vertical Bar",
//    "Horizontal Bar",
//    "StackedBar",
//    "OverLaid-Bar-Line",
//    "MultiMeasure Bar"
];
var circularPieGraph = [
//    "Pie",
    "Pie"
//    "Pie 3D",
//    "Half Pie",
//    "Donut",
//    "Half Donut",
//    "Donut 3D",
//    "Double Donut"
];
var lineGraph = [
    "Line"
//    "SmoothLine",
//    "MultiMeasure-Line",
//    "MultiMeasureSmooth-Line"
];// end by mynk sh.
function changeGraphColor(colorType) {
    var chartId = "chart1";
    shadingType = colorType;
    $("#shadeType").val(shadingType);
    $("#addMoreRow").show();
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measureArray = chartData["chart1"]["meassures"];
    if (colorType === "central") {
        var divHeight = ($(window).height() - $(window).height() / 6);
        var divWidth = ($(window).width() - ($(window).width() / 1.4));
        parent.$("#colorCustomizeDiv").dialog({
            autoOpen: false,
            height: divHeight,
            width: divWidth,
            position: 'justify',
            modal: true,
            title: 'Customize Color'
        });

        parent.$("#colorCustomizeDiv").css("background-color", "linear-gradient(rgb(255,255,255), rgb(255,255,255))");
        parent.$("#colorCustomizeIframe").height((divHeight - 50));
        parent.$("#colorCustomizeIframe").width(divWidth - 25);
        document.getElementById('colorCustomizeIframe').src = "resources/centralColor.html";
        parent.$("#colorDiv").hide(400);
        $("#attributeDiv").hide(50);
        $("#colorCustomizeDiv").dialog('open');
    } else if (colorType === "conditional") {
        var selectedMeasure = "";
        //  $("#isShaded").val("true");
        if ($("#isShaded").val() == "true") {
            try {
                var conditionalMap = JSON.parse($("#conditionalMap").val())
                //            selectedMeasure=conditionalMap["measure"];
                selectedMeasure = $("#conditionalMeasure").val();

                var keys = Object.keys(conditionalMap)
                //            parent.counter=keys.length-2;
                parent.counter = keys.length - 1;
                for (var no = 0; no <= parent.counter; no++) {
                    $("#conditionalChooser" + no).css("background-color", conditionalMap[no]["color"]);
                    $("#conditionalChooser" + no).val(conditionalMap[no]["color"].replace("#", "", "gi"));
                    $("#operator" + no).val(conditionalMap[no]["operator"]);
                    $("#firstOperand" + no).val(conditionalMap[no]["limit1"]);
                    $("#conditionalRow" + no).show()
                    if (isBinaryOperator(conditionalMap[no]["operator"])) {
                        $("#secondOperand" + no).val(conditionalMap[no]["limit2"]);
                        document.getElementById("secondOperand" + no).type = "text";
                        $("#secondOperand" + no).show();
                    } else {
                        document.getElementById("secondOperand" + no).type = "hidden";
                        $("#secondOperand" + no).hide();
                    }
                }
            } catch (e) {
            }
        }

        var measureList = JSON.parse(parent.$("#measure").val());
        //        alert(JSON.stringify(measureList))
        var html = "";
        for (var measure in measureList) {
            //            alert(measure)
            if (measure == 0) {
                if (selectedMeasure == "") {
                    getMeasureMinMax(measureList[measure]);
                } else {
                    getMeasureMinMax(selectedMeasure);
                }
            }
            if (selectedMeasure == measureList[measure]) {
                html += "<option value='" + measureList[measure] + "' selected>" + measureList[measure] + "</option>";
            } else {
                html += "<option value='" + measureList[measure] + "'>" + measureList[measure] + "</option>";
            }
        }
        //alert("ssss: "+$("#conditionalMeasure").val())
        $("#conditionalMeasure1").html(html);
        $("#conditionalShading").show(400);
        $("#standardShading").hide(400);
        $("#gradientShading").hide(400);
    } else if (colorType === "standard") {
        var colorMap = JSON.parse($("#colorMap").val());
        for (var j = 0; j < 15; j++) {
            $("#Strow" + j).hide();
        }
        var keys = Object.keys(colorMap);
        for (var i = 0; i < (keys.length > 15 ? 15 : keys.length); i++) {
            var temp = colorMap[i].split("__");
            $("#lable" + i).html("<label class='labelStyle'>" + temp[0] + "</label>");
            $("#stColorChooser" + i).css("background-color", temp[1]);
            $("#stColorChooser" + i).val(temp[1].replace("#", "", "gi"));
            $("#Strow" + i).show();
        }
        $("#conditionalShading").hide(400);
        $("#standardShading").show(400);
        $("#gradientShading").hide(400);
    } else if (colorType === "gradient") {
        $("#addMoreRow").hide();
        var measureSelected = "";
        try {
            var temp = JSON.parse($("#measureColor").val());
            measureSelected = temp["measure"]
        } catch (e) {
        }

        var color = "";
        if (typeof chartData[chartId]["colorPicker"] !== "undefined") {
            color = chartData[chartId]["colorPicker"];
        } else {
            color = "#3366CC";
        }


        var measureList = JSON.parse($("#measure").val());

        $("#gradShade").css("background-color", color);
        $("#gradShade").val(color.replace("#", "", "gi"));
        var html = "";
        for (var measure in measureList) {
            if (measureSelected == measureList[measure]) {
                html += "<option value='" + measureList[measure] + "' selected>" + measureList[measure] + "</option>"
            } else {
                html += "<option value='" + measureList[measure] + "'>" + measureList[measure] + "</option>"
            }

        }

        $("#measureListGradient").html(html);
        $("#conditionalShading").hide(400);
        $("#standardShading").hide(400);
        $("#gradientShading").show(400);
    } else if (colorType === "customize") {

        var divHeight = ($(window).height() - $(window).height() / 6);
        var divWidth = ($(window).width() - ($(window).width() / 1.4));
        parent.$("#colorCustomizeDiv").dialog({
            autoOpen: false,
            height: divHeight,
            width: divWidth,
            position: 'justify',
            modal: true,
            title: 'Customize Color'
        });
        parent.$("#colorCustomizeDiv").css("background-color", "linear-gradient(rgb(255,255,255), rgb(255,255,255))");
        parent.$("#colorCustomizeIframe").height((divHeight - 50));
        parent.$("#colorCustomizeIframe").width(divWidth - 25);
        //    document.getElementById('colorCustomizeIframe').src="resources/customizeColorList.html";
        parent.$("#colorDiv").hide(400);
        $("#attributeDiv").hide(50);
        $("#colorCustomizeDiv").dialog('open');

    } else {
        $("#isShaded").val("false");

        $("#conditionalShading").hide(400);
        $("#standardShading").hide(400);
        $("#gradientShading").hide(400);
        document.getElementById('graphframe').src = "resources/SortableBar.html";
        parent.$("#graphNameId").html($("#reportName").val());
    }
}

function gradientShading() {
    $("#gradientShading").hide(400);
    var color = $("#gradShade").val();
    var measure = $("#measureListGradient").val();
    isShadedColor = true;
    shadingType = "gradient";
    $("#shadeType").val(shadingType);
    $("#isShaded").val("true");
    var colorMap = {};
    colorMap[measure] = "#" + color;
    colorMap["measure"] = measure;
    $("#measureColor").val(JSON.stringify(colorMap));
    $('#colorDiv').hide(400);
    $("#chartDiv").hide(400);
    $("#SortDiv").hide(400);
    $('#measureDiv').hide(400);
    $("#topBottomDiv").hide(400);
    var isMap = $("#isMap").val();
    if (isMap == "No") {
        document.getElementById('graphframe').src = "resources/SortableBar.html";
        parent.$("#graphNameId").html($("#reportName").val());
    } else {
        if (isMap == "State") {
            document.getElementById('graphframe').src = "resources/html/IndiaMap.html";
            $("#graphNameId").html("India Map - States")
        } else if (isMap == "DistrictMap") {
            document.getElementById('graphframe').src = "resources/html/IndiaDistrict-Map.html";
        } else {
            document.getElementById('graphframe').src = "resources/html/BubbleMap.html";
            $("#graphNameId").html("India Map - Cities");
        }
    }
}

function standardShading() {
    $("#standardShading").hide(400);
    isShadedColor = true;
    $("#isShaded").val("true");
    var colorMap = JSON.parse($("#colorMap").val());
    var newColorCategory = [];
    var keys = Object.keys(colorMap);
    for (var i = 0; i < keys.length; i++) {
        var temp = colorMap[i].split("__");
        if (document.getElementById("stColorChooser" + i) != 'undefined' && document.getElementById("stColorChooser" + i) != null) {
            colorMap[i] = temp[0] + "__#" + $("#stColorChooser" + i).val();
            newColorCategory.push("#" + $("#stColorChooser" + i).val());
        } else {
            newColorCategory.push(temp[1].replace("##", "#", "gi"));
        }
    }
    $("#newColorCategory").val(JSON.stringify(newColorCategory));
    $("#colorMap").val(JSON.stringify(colorMap));
    document.getElementById('graphframe').src = "resources/SortableBar.html";
    parent.$("#graphNameId").html($("#reportName").val());
}

function getMeasureMinMax(measureName) {
    $("#conditionalMeasure").val(measureName);
//    var maxMap=JSON.parse($("#maxMap").val());
//    var minMap=JSON.parse($("#minMap").val());
//    $("#max").val(maxMap[measureName]);
//    $("#min").val(minMap[measureName]);
}

function conditionShadinng() {
    isShadedColor = true;
    $("#isShaded").val("true");
    //    $("#conditionalShading").hide(400);
    $('#colorDiv').hide(400);
    $("#chartDiv").hide(400);
    $("#SortDiv").hide(400);
    $('#measureDiv').hide(400);
    $("#topBottomDiv").hide(400);
    var conditionalMap = {};
    conditionalMap["measure"] = $("#conditionalMeasure").val();
    for (var i = 0; i <= parent.counter; i++) {
        conditionalMap[i] = {};
        conditionalMap[i]["color"] = "#" + parent.$("#conditionalChooser" + i).val();
        conditionalMap[i]["operator"] = parent.$("#operator" + i).val();
        var limit1 = parent.$("#firstOperand" + i).val();
        if (limit1 == 0 || limit1 == null) {
            limit1 = limit1;
        }
        conditionalMap[i]["limit1"] = limit1;
        if (isBinaryOperator(parent.$("#operator" + i).val())) {
            var limit2 = parent.$("#secondOperand" + i).val();
            if (limit2 == 0 || limit2 == null) {
                limit2 = limit1;
            }
            conditionalMap[i]["limit2"] = limit2;
        }
    }
    parent.$("#conditionalMap").val(JSON.stringify(conditionalMap));
//     var isMap=$("#isMap").val();
//    if(isMap=="No"){
//    document.getElementById('graphframe').src="resources/SortableBar.html";
//    parent.$("#graphNameId").html($("#reportName").val());
//    }else{
//        if(isMap=="State"){
//            document.getElementById('graphframe').src="resources/html/IndiaMap.html";
//            $("#graphNameId").html("India Map - States")
//        }else if(isMap=="DistrictMap"){
//           document.getElementById('graphframe').src="resources/html/IndiaDistrict-Map.html";
//       }else{
//            document.getElementById('graphframe').src="resources/html/BubbleMap.html";
//                $("#graphNameId").html("India Map - Cities")
//}
//    }
}

function add() {
    if (parent.counter < 7) {
        if (parent.counter == 0) {
            parent.$("#deleteConditionalRow").show();
        }
        parent.counter++;
        parent.$("#conditionalRow" + parent.counter).show();
        if (parent.counter == 7) {
            parent.$("#addConditionalRow").hide();
        }
    }
}

function removeRow() {
    if (parent.counter > 0) {
        if (parent.counter == 7) {
            parent.$("#addConditionalRow").show();
        }
        parent.$("#conditionalRow" + parent.counter).hide();
        parent.counter--;
        if (parent.counter == 0) {
            parent.$("#deleteConditionalRow").hide();
        }
    }
}


function checkOperator(id, symbol)
{
    var box = parseInt(id.replace("operator", ""));
    var open = document.getElementById("secondOperand" + box);
    if (symbol === "<>") {
        open.type = "text";
        open.style.display = "block";
    }
    else {
        open.style.display = "none";
        open.type = "hidden";
    }
}

function isBinaryOperator(operator) {
    var flag = false;
    if (operator === "<>") {
        flag = true;
    }
    return flag;
}


function showAttributes() {
    var isDashboard = $("#isDashboard").val();
    if (typeof isDashboard !== 'undefined' && (isDashboard === true || isDashboard === "true")) {
        createNewDashboard();
    } else {
        $("#storyBoardDiv").hide(400);
        $("#chartDiv").hide(400);
        $("#my_chart_list").hide(400);
        $("#my_chart_list1").hide(400);
        $("#paramdivframe1").hide(400);
        $("#visulaizeDiv").hide(400);
        $("#legendsDiv").hide(400);
        $("#attributeDiv").toggle(200);
        if ($("#chartType").val() === "dashboard") {
            $("#OthersLink").hide();
            $("#aggregationLink").hide();
            $("#recordsLink").hide();
        }
        else {
            $("#OthersLink").show();
            $("#aggregationLink").show();
            $("#recordsLink").show();
        }
        //                    var chartType = JSON.parse($("#chartList").val());
        //                    if (chartType === "Pie" || chartType === 'Donut') {
        //                        $("#legendsTag").show();
        //                    }
        //                    else {
        //                        $("#legendsTag").hide();
        //                    }
    }
}

function showColorDiv() {
    //      $("#showAttributes").dialog({
    //         autoOpen: false,
    //         height: 440,
    //         width: 620,
    //         position: 'justify',
    //         modal: true,
    //         resizable:true,
    //        title:'Color Properties'
    //    });

    $("#chartDiv").hide(400);
    $('#measureDiv').hide(400);
    $("#topBottomDiv").hide(400);
    $("#SortDiv").hide(400);
    $('#colorDiv').slideToggle(400);


//                var html = "";
//                html += "<table>";
//              html += " <tr style='height:25px'><td colspan='4' align='center' ><input type='button' name='' class='navtitle-hover'  onclick='' name='sorting_submit' value='Done'></td>";
//html += "</tr></table>";
//$("#showAttributes").append(html);
// $("#showAttributes").dialog('open');
}

//function conditionalShadingView(){
//  var html = '';
//   html +=    '<div id="conditionalShading" style="display: none">';
//      html += '<form action="" name="colorfrm" id="colorfrm" method="post">';
//                     html += '<center><select id="conditionalMeasure" onchange="getMeasureMinMax(this.value)"></select></center>';
//                                    html +='   <table align="left" border="0" id="colorTable">';
//                                     html += '<tr><td colspan="2"><label class="labelStyle">Apply Color as</label></td></tr>';
//
//      html +=   '<tr id="conditionalRow0"><td>';
//                                                html +=    '<input id="conditionalChooser0" style="width: 60px" readonly="" class="color" value="BFA0BF">';
//                                         html +=       '</td><td><label class="labelStyle">Value</label> </td>';
//
//                                            html += '<td>';
//                                               html +    '<select name="operators" id="operator0" onchange="checkOperator(this.id, this.value)">';
//                                                html += '<option value="<"><</option>';
//                                                  html += '<option value=">">></option>';
//                                                        html += '<option value="<="><=</option>';
//                                                       html += '<option value=">">>=</option>';
//                                                        html += '<option value="=">=</option>';
//                                                        html += '<option value="<>"><></option>';
//                                                    html += '</select>';
//                                                html += '</td>';
//        html += '<td>';
//         html += '<input type="text" name="sValues" id="firstOperand0" style="width:60px">';
//         html +=  '</td>';
//                          html +=  '<td>';
//                            html += '<input type="hidden" name="eValues" id="secondOperand0" style="width:60px"></td>';
//
//                               html +='<tr id="conditionalRow1" Style="display:none">';
//                                             html +=   '<td>';
//                                                    html += '<input id="conditionalChooser1" style="width: 60px" readonly="" class="color" value="BFA0BF">';
//                                                html += '</td>';
//                                                html += '<td><label class="labelStyle">Value</label></td>';
//                                                html += '<td>';
//                                                html += '<select name="operators" id="operator1" onchange="checkOperator(this.id, this.value)">';
//                                                html += '<option value="<"><</option>';
//                                                html += '<option value=">">></option>';
//                                                html += '<option value="<="><=</option>';
//                                                 html += '<option value=">">>=</option>';
//                                                  html += '<option value="=">=</option>';
//                                                  html += '<option value="<>"><></option>';
//                                                  html += '</select>';
//                                              html +=  '</td>';
//                                              html += '<td><input type="text" name="sValues" id="firstOperand1" style="width:60px"></td>';
//                                                html += '<td><input type="hidden" name="eValues" id="secondOperand1" style="width:60px"></td>';
//                                           html += '</tr>';
//
//                                        html += '</table>';
//
//
//                                       html += '<table border="0">';
////                                       html += '<tr align="center">';
////                                             html += '<td align="center" colspan="3">';
////                                   html +=    '<input  class="navtitle-hover" id="addConditionalRow" type="button" value="Add Row" onclick="add()">';
////                                                  html += '<input  class="navtitle-hover" id="deleteConditionalRow" type="button" value="Delete Row" onclick="removeRow()">';
////                                               html += '</td>';
////                                            html += '</tr>';
//                                           html += '<tr align="center">';
//                                             html +=  '<td>';
//                                                  html += '<input class="navtitle-hover" type="button" value="Done" onclick="conditionShadinng()">';
//                                                html += '</td>';
//                                            html += '</tr>';
//                                        html += '</table>';
//                                       html +='</tr></table>';
//                               html += '</form>';
//                           html +=  '</div>';
//    $("#showAttributes").append(html);
//}

function setProperties(repId) {
    $("#advanceProperties").dialog({
        autoOpen: false,
        height: 550,
        width: 798,
        position: 'justify',
        modal: true,
        resizable: true,
        title: translateLanguage.Visuals_Properties
    });

    $("#advanceProperties").html("");
    $("#advanceProperties").dialog('open');

    var innerHtml = "";
    innerHtml += "<div id='tabProperties' class='' style='width:100%;height:5.5%;'>";
    var visualProp = ["General Visual", "Legends/Labels Visual", "X-Y Axis Visual", "Measures Visual"];
    innerHtml += '<ul id="quickTabs">';
    for (var i = 0; i < visualProp.length; i++) {
        if (i == 0)
            innerHtml += '<li class="tabUL" style="border-bottom:3px solid #DC143C;"><a style="padding: 0.3em 3.5em;" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" class="gFontFamily gFontSize12" onclick="' + visualProp[i].trim().replace(" ", "").replace("/", "").replace("-", "").replace(" ", "").toLowerCase() + '()">' + visualProp[i] + '</a></li>';
        else
            innerHtml += '<li class="tabUL"><a style="padding: 0.3em 3.5em;" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" class="gFontFamily gFontSize12" onclick="' + visualProp[i].trim().replace(" ", "").replace("/", "").replace("-", "").replace(" ", "").toLowerCase() + '()">' + visualProp[i] + '</a></li>';
    }
    innerHtml += '</ul>';
    innerHtml += "</div>";

    var chartData = JSON.parse($("#chartData").val());
    var chartId = "chart1";
    var chart2 = "chart2";

    innerHtml += "<div id='generalTable' class='defaultProperty' style='width:100%;margin-left:1%;'>";
    innerHtml += "<table id='generalTable1' style='padding:3px 3px 3px 3px;border-collapse:separate;border-spacing:10px 10px;'>";

    innerHtml += "<tr><td > <label class='gFontFamily gFontSize12'>" + translateLanguage.No_of_Records + " : </label></td>"
    innerHtml += "<td>";
    var chartRecords = "12";
    if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "") {
        chartRecords = chartData[chartId]["records"];
    }
    innerHtml += "<input type='text' id='" + chartId + "Records' class='gFontFamily gFontSize12' size='6' value=" + chartRecords + ">";
    innerHtml += "</td>";

    innerHtml += "<td> <label class='gFontFamily gFontSize12'> " + translateLanguage.Rounding + " </label></td>";
    innerHtml += "<td><select id='" + chartId + "rounding' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["rounding"] === "undefined" || chartData[chartId]["rounding"] === "0") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
    }
    if (chartData[chartId]["rounding"] === "1") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
    }
    if (chartData[chartId]["rounding"] === "2") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
    }
    innerHtml += "</select></td>";
    innerHtml += "</tr>";

    innerHtml += "<tr><td> <label class='gFontFamily gFontSize12'> " + translateLanguage.Sort_Basis + " </label></td>";
    innerHtml += "<td>";
    innerHtml += "<select id='" + chartId + "sortBasis' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["sortBasis"] === "undefined" || chartData[chartId]["sortBasis"] === "Value") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Value' selected>Value</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Value'>Value</option>";
    }

    if (chartData[chartId]["sortBasis"] === "Alphabetic") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Alphabetic' selected>Alphabetic</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Alphabetic'>Alphabetic</option>";
    }
    innerHtml += "</select></td>";

    innerHtml += "<td> <label class='gFontFamily gFontSize12'> " + translateLanguage.Sort_Option + " </label></td>";
    innerHtml += "<td>";
    innerHtml += "<select id='" + chartId + "sorting' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["sorting"] === "undefined" || chartData[chartId]["sorting"] === "Descending") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Descending' selected>Descending</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Descending'>Descending</option>";
    }

    if (chartData[chartId]["sorting"] === "Ascending") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Ascending' selected>Ascending</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Ascending'>Ascending</option>";
    }
    innerHtml += "</select></td>";
    innerHtml += "</tr>";
    //Added by Ram

    innerHtml += "<tr>";
    innerHtml += "<td> <label class='gFontFamily gFontSize12'>";
    if (typeof chartData[chartId]["enableQuickFilter2"] !== "undefined" && chartData[chartId]["enableQuickFilter2"] === "Y") {
        innerHtml += "" + translateLanguage.Enable_Second_Quick_Filter + " </label></td><td> <input type='checkbox' id='" + chartId + "enableQuickFilter2' class='gFontFamily gFontSize12' checked>";
    } else {
        innerHtml += "" + translateLanguage.Enable_Second_Quick_Filter + " </label></td><td> <input type='checkbox' id='" + chartId + "enableQuickFilter2' class='gFontFamily gFontSize12' >";
    }
    innerHtml += "</td>";

    innerHtml += "<td> <label class='gFontFamily gFontSize12'>";
    if (chartData[chartId]["multiFilter"] !== "undefined" && chartData[chartId]["multiFilter"] == "Y") {
        innerHtml += "" + translateLanguage.Enable_Multi_Filter + " </label></td><td> <input type='checkbox' id='" + chartId + "multiFilter' class='gFontFamily gFontSize12' checked>";
    } else {
        innerHtml += "" + translateLanguage.Enable_Multi_Filter + " </label></td><td> <input type='checkbox' id='" + chartId + "multiFilter' class='gFontFamily gFontSize12'>";
    }
    innerHtml += "</td>";
    innerHtml += "</tr>";
    //complete
    innerHtml += "<tr>";
    innerHtml += "<td class='gFontFamily gFontSize12'>";

    if (chartData[chartId]["completeChartData"] !== "undefined" && chartData[chartId]["completeChartData"] === "Yes") {
        innerHtml += "" + translateLanguage.Chart_on_Complete_Data + " </td><td> <input type='checkbox' id='" + chartId + "_completeChartData' class='gFontFamily gFontSize12' checked>";
    } else {
        innerHtml += "" + translateLanguage.Chart_on_Complete_Data + " </td><td> <input type='checkbox' id='" + chartId + "_completeChartData' class='gFontFamily gFontSize12'>";
    }
    innerHtml += "</td>";
    innerHtml += "</tr>";

    var chartType = chartData[chartId]["chartType"];
    var noOfRecords = chartData[chartId]["records"] > 25 ? 25 : chartData[chartId]["records"];
    if (typeof noOfRecords === "undefined" || noOfRecords === "" || noOfRecords.toLowerCase() === "all") {
        noOfRecords = "12";
    }
    innerHtml += "<tr>";
    innerHtml += "<td > <label class='gFontFamily gFontSize12'> " + translateLanguage.Select_Top_Colors + "</label></td>";

    innerHtml += "<td colspan=3>";
    var topColors = d3.scale.category10();
    var colorArray = [];
    for (var j = 0; j < noOfRecords; j++)
    {
        colorArray[j] = topColors(j);
    }
    for (var j = 0; j < noOfRecords; j++)
    {
        var color;
        if (!(typeof chartData[chartId]["customColors"] === 'undefined' || typeof chartData[chartId]["customColors"]["color" + (j + 1)] === 'undefined'))
        {
            //            alert("If j:"+j);
            color = chartData[chartId]["customColors"]["color" + (j + 1)];
        }
        else
        {
            //            alert("Else j:"+j);
            color = colorArray[j];
        }

        if (j % 10 == 0)
        {

            innerHtml += "<input id='color" + (j + 1) + "' type='color picker' onclick='pickColor(\"" + chartId + "\",this.id,\"" + j + "\")' style='width:30px;background-color:" + color + "' >";
        }
        else
        {
            innerHtml += "<input id='color" + (j + 1) + "' type='color picker' onclick='pickColor(\"" + chartId + "\",this.id,\"" + j + "\")' style='margin-left:7px;width:30px;background-color:" + color + "'>";
        }
        if ((j + 1) % 10 == 0 && j != 0) {
            innerHtml += "</td>";
            innerHtml += "</tr>";
            innerHtml += "<tr>";
            innerHtml += "<td></td>";
            innerHtml += "<td colspan=3>";
        }
    }
    innerHtml += "</td>";
    innerHtml += "</tr>";

    innerHtml += "<tr>";
    innerHtml += "<td>";
    innerHtml += '<div id="colorDiv" align="center" class="outerDiv" style="">';
    innerHtml += '<div class="dataBlock">';
    innerHtml += '<center>';
    var shadeType = $("#shadeType").val();
    if (shadeType === 'conditional') {
        innerHtml += '<input type="radio" name="colortype" value="conditional" onchange="changeGraphColor(this.value)" checked><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Conditional + '</span>';
        innerHtml += '<input type="radio" name="colortype" value="gradient" onchange="changeGraphColor(this.value)"><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Gradient + '</span>';
    }
    else if (shadeType === 'gradient') {
        innerHtml += '<input type="radio" name="colortype" value="conditional" onchange="changeGraphColor(this.value)"><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Conditional + '</span>';
        innerHtml += '<input type="radio" name="colortype" value="gradient" onchange="changeGraphColor(this.value)" checked><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Gradient + '</span>';
    }
    else {
        innerHtml += '<input type="radio" name="colortype" value="conditional" onchange="changeGraphColor(this.value)"><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Conditional + '</span>';
        innerHtml += '<input type="radio" name="colortype" value="gradient" onchange="changeGraphColor(this.value)"><span class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Gradient + '</span>';
    }
    //                                         innerHtml += '<input type="radio" name="colortype" value="standard" onchange="changeColor(this.value)"><span class="labelStyle">Standard</span>';
    //                                                        innerHtml += '<input type="radio" name="colortype" value="disable" onchange="changeColor(this.value)"><span class="labelStyle">Disable</span>';
    //                            innerHtml += '<input type="radio" name="colortype" value="customize" onchange="changeGraphColor(this.value)"><span class="labelStyle">Customize</span>';
    //                            innerHtml += '<input type="radio" name="colortype" value="central" onchange="changeGraphColor(this.value)"><span class="labelStyle">Central</span>';
    innerHtml += "</center></div></div>";
    innerHtml += "</td></tr>";
    innerHtml += "<tr><td>";
    if (shadeType === 'conditional') {
        innerHtml += '<div id="conditionalShading" style="display: block">';
    }
    else {
        innerHtml += '<div id="conditionalShading" style="display: none">';
    }

    innerHtml += '<form action="" name="colorfrm" id="colorfrm" method="post">';
    innerHtml += '   <table align="left" border="0" id="colorTable" style="float:right">';
    innerHtml += '<tr><td><center><select id="conditionalMeasure1" onchange="getMeasureMinMax(this.value)"></select></center></td></tr>';
    innerHtml += '<tr><td colspan="2"><label class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Apply_Color_as + '</label></td></tr>';

    innerHtml += '<tr id="conditionalRow0"><td>';
    innerHtml += '<input id="conditionalChooser0" style="width: 60px" readonly="" onclick="graphColorPicker(this.id)" class="color" value="BFA0BF">';
    innerHtml += '</td><td><label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Value + '</label> </td>';
    innerHtml += '<td>';
    innerHtml += '<select name="operators" id="operator0" class="gFontFamily gFontSize12" onchange="checkOperator(this.id, this.value)">';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<"><</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">></option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<="><=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">>=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="=">=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<>"><></option>';
    innerHtml += '</select>';
    innerHtml += '</td>';
    innerHtml += '<td>';
    innerHtml += '<input type="text" name="sValues" id="firstOperand0" style="width:60px">';
    innerHtml += '</td>';
    innerHtml += '<td>';
    innerHtml += '<input type="hidden" name="eValues" id="secondOperand0" style="width:60px"></td>';

    innerHtml += '<tr id="conditionalRow1" Style="display:none">';
    innerHtml += '<td>';
    innerHtml += '<input id="conditionalChooser1" style="width: 60px" readonly="" onclick="graphColorPicker(this.id)" class="color" value="BFA0BF">';
    innerHtml += '</td>';
    innerHtml += '<td><label class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Value + '</label></td>';
    innerHtml += '<td>';
    innerHtml += '<select name="operators" id="operator1" onchange="checkOperator(this.id, this.value)">';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<"><</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">></option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<="><=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">>=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="=">=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<>"><></option>';
    innerHtml += '</select>';
    innerHtml += '</td>';
    innerHtml += '<td><input type="text" name="sValues" id="firstOperand1" style="width:60px"></td>';
    innerHtml += '<td><input type="hidden" name="eValues" id="secondOperand1" style="width:60px"></td>';
    innerHtml += '</tr>';

    innerHtml += '<tr id="conditionalRow2" Style="display:none">';
    innerHtml += '<td>';
    innerHtml += '<input id="conditionalChooser2" style="width: 60px" readonly="" onclick="graphColorPicker(this.id)" class="color" value="BFA0BF">';
    innerHtml += '</td>';
    innerHtml += '<td><label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Value + '</label></td>';
    innerHtml += '<td>';
    innerHtml += '<select name="operators" id="operator2" onchange="checkOperator(this.id, this.value)">';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<"><</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">></option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<="><=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">>=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="=">=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<>"><></option>';
    innerHtml += '</select>';
    innerHtml += '</td>';
    innerHtml += '<td><input type="text" name="sValues" id="firstOperand2" style="width:60px"></td>';
    innerHtml += '<td><input type="hidden" name="eValues" id="secondOperand2" style="width:60px"></td>';
    innerHtml += '</tr>';

    innerHtml += '<tr id="conditionalRow3" Style="display:none">';
    innerHtml += '<td>';
    innerHtml += '<input id="conditionalChooser3" style="width: 60px" readonly="" onclick="graphColorPicker(this.id)" class="color" value="BFA0BF">';
    innerHtml += '</td>';
    innerHtml += '<td><label class="labelStyle gFontFamily gFontSize12>' + translateLanguage.Value + '</label></td>';
    innerHtml += '<td>';
    innerHtml += '<select name="operators" id="operator3" onchange="checkOperator(this.id, this.value)">';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<"><</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">></option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<="><=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">>=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="=">=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<>"><></option>';
    innerHtml += '</select>';
    innerHtml += '</td>';
    innerHtml += '<td><input type="text" name="sValues" id="firstOperand3" style="width:60px"></td>';
    innerHtml += '<td><input type="hidden" name="eValues" id="secondOperand3" style="width:60px"></td>';
    innerHtml += '</tr>';

    innerHtml += '<tr id="conditionalRow4" Style="display:none">';
    innerHtml += '<td>';
    innerHtml += '<input id="conditionalChooser4" style="width: 60px" readonly="" onclick="graphColorPicker(this.id)" class="color" value="BFA0BF">';
    innerHtml += '</td>';
    innerHtml += '<td><label class="labelStyle" class="gFontFamily gFontSize12">' + translateLanguage.Value + '</label></td>';
    innerHtml += '<td>';
    innerHtml += '<select name="operators" id="operator4" onchange="checkOperator(this.id, this.value)">';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<"><</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">></option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<="><=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value=">">>=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="=">=</option>';
    innerHtml += '<option class="gFontFamily gFontSize12" value="<>"><></option>';
    innerHtml += '</select>';
    innerHtml += '</td>';
    innerHtml += '<td><input type="text" name="sValues" id="firstOperand4" style="width:60px"></td>';
    innerHtml += '<td><input type="hidden" name="eValues" id="secondOperand4" style="width:60px"></td>';
    innerHtml += '</tr>';

    innerHtml += '</table>';

    innerHtml += '<tr></tr>';
    //                                       innerHtml += '<table border="0">';
    innerHtml += '<tr align="center" id="addMoreRow" style="display:none">';
    innerHtml += '<td rowspan="0">';
    innerHtml += '<input  class="navtitle-hover gFontFamily gFontSize12" id="addConditionalRow" type="button" value="' + translateLanguage.Add_Row + '" onclick="add()">';
    innerHtml += "</td><td rowspan='0'>"
    innerHtml += '<input  class="navtitle-hover gFontFamily gFontSize12 id="deleteConditionalRow" type="button" value="' + translateLanguage.Delete_Row + '" onclick="removeRow()">';
    innerHtml += '</td>';
    innerHtml += '</tr>';
    //                                           innerHtml += '<tr align="center">';
    //                                             innerHtml +=  '<td>';
    //                                                  innerHtml += '<input class="navtitle-hover" type="button" value="Done" onclick="conditionShadinng()">';
    //                                                innerHtml += '</td>';
    //                                            innerHtml += '</tr>';
    innerHtml += '</table>';
    //                                       innerHtml +='</tr></table>';
    innerHtml += '</form>';
    innerHtml += '</div>';

    //Gradient
    if (shadeType === 'gradient') {
        innerHtml += '<div id="gradientShading" style="display:block">';
        innerHtml += '<table>';
        innerHtml += '<tr height="5px"></tr>';
        innerHtml += '<tr>';
        innerHtml += '<td align="center"><label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Measure + ': </label><select id="measureListGradient">';
        var measureSelected = "";
        try {
            var temp = JSON.parse($("#measureColor").val());
            measureSelected = temp["measure"]
        } catch (e) {
        }
        var measureList = JSON.parse($("#measure").val());
        for (var measure in measureList) {
            if (measureSelected == measureList[measure]) {
                innerHtml += "<option class='gFontFamily gFontSize12' value='" + measureList[measure] + "' selected>" + measureList[measure] + "</option>"
            } else {
                innerHtml += "<option class='gFontFamily gFontSize12' value='" + measureList[measure] + "'>" + measureList[measure] + "</option>"
            }

        }
        innerHtml += '</select></td>';
        innerHtml += '</tr>';
        innerHtml += '<tr>';
        innerHtml += '<td>';
        var color = '';
        if (typeof chartData["chart1"]["colorPicker"] != 'undefined' && chartData["chart1"]["colorPicker"] != '') {
            color = chartData["chart1"]["colorPicker"];
        }
        innerHtml += '<label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Gradient_Shade + ': </label><input id="gradShade" style="width: 60px;background-color:' + color + '" readonly="" onclick="moreColors(\'' + chartId + '\',this.id)" class="color" value="' + color + '">';
        innerHtml += '</td>';
        innerHtml += '</tr>';
        //                                   innerHtml += '<tr>';
        //                                        innerHtml += '<td align="center">';
        //                                            innerHtml += '<input type="button" value="done" onclick="gradientShading()">';
        //                               innerHtml +=    '</td>';
        //                                   innerHtml += '</tr>';
        innerHtml += '</table>';
        innerHtml += '</div>';

        innerHtml += " <div  id='picker" + chartId + "' style='display:none'></div>";
    }
    else {
        innerHtml += '<div id="gradientShading" style="display:none">';
        innerHtml += '<table>';
        innerHtml += '<tr height="5px"></tr>';
        innerHtml += '<tr>';
        innerHtml += '<td align="center"><label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Measure + ': </label><select id="measureListGradient"></select></td>';
        innerHtml += '</tr>';
        innerHtml += '<tr>';
        innerHtml += '<td>';
        innerHtml += '<label class="labelStyle gFontFamily gFontSize12">' + translateLanguage.Gradient_Shade + ': </label><input id="gradShade" style="width: 60px" readonly="" onclick="graphColorPicker(\'' + chartId + '\')" class="color" value="BFA0BF">';
        innerHtml += '</td>';
        innerHtml += '</tr>';
        //                                   innerHtml += '<tr>';
        //                                        innerHtml += '<td align="center">';
        //                                            innerHtml += '<input type="button" value="done" onclick="gradientShading()">';
        //                               innerHtml +=    '</td>';
        //                                   innerHtml += '</tr>';
        innerHtml += '</table>';
        innerHtml += '</div>';

        innerHtml += " <div  id='picker" + chartId + "' style='display:none'></div>";
    }
    innerHtml += '</tr>';



    innerHtml += "</table></div>";
    //.........................................................................................

    var axisProps = "";
    var chartData = JSON.parse($("#chartData").val());

    axisProps += "<div id='axisProperties' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    axisProps += "<table  id='axisProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";

    axisProps += "<tr><td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>Y-" + translateLanguage.Axis_Format + " </label></td>";
    axisProps += "<td><select id='" + chartId + "yAxisFormat' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["yAxisFormat"] === "undefined" || chartData[chartId]["yAxisFormat"] === "") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "K") {
        axisProps += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "M") {
        axisProps += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "Cr") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "L") {
        axisProps += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='L'>Lakh</option>";
    }
    axisProps += "</select></td>";

    axisProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y-" + translateLanguage.Axis_Rounding + " </label></td>";
    axisProps += "<td><select id='" + chartId + "rounding' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["rounding"] === "undefined" || chartData[chartId]["rounding"] === "0") {
        axisProps += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
    }
    if (chartData[chartId]["rounding"] === "1") {
        axisProps += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
    }
    if (chartData[chartId]["rounding"] === "2") {
        axisProps += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
    }
    axisProps += "</select></td>";
    axisProps += "</tr>";

    axisProps += "<tr>";
    axisProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_X_Axis + " </label></td>";
    axisProps += "<td><select id='" + chartId + "displayX' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["displayX"] === "undefined" || chartData[chartId]["displayX"] === "Yes") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["displayX"] === "No") {
        axisProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    axisProps += "</select></td>";

    axisProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_Y_Axis + " </label></td>";
    axisProps += "<td><select id='" + chartId + "displayY' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["displayY"] === "undefined" || chartData[chartId]["displayY"] === "Yes") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["displayY"] === "No") {
        axisProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    axisProps += "</select></td>";
    axisProps += "</tr>";

    axisProps += "<tr><td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_X_Axis_Line + " </label></td>";
    axisProps += "<td>";
    axisProps += "<select id='" + chartId + "displayXLine' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["displayXLine"] === "undefined" || chartData[chartId]["displayXLine"] === "Yes") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["displayXLine"] === "No") {
        axisProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    axisProps += "</select></td>";

    axisProps += "<td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_Y_Axis_Line + " </label></td>";
    axisProps += "<td><select id='" + chartId + "displayYLine' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["displayYLine"] === "undefined" || chartData[chartId]["displayYLine"] === "Yes") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }

    if (chartData[chartId]["displayYLine"] === "No") {
        axisProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    axisProps += "</select></td></tr>";

    axisProps += "<tr>";
    axisProps += "<td class='gFontFamily gFontSize12'>";
    if (typeof chartData[chartId]["innerLabels"] !== "undefined" && chartData[chartId]["innerLabels"] === "Y") {
        axisProps += "" + translateLanguage.Axis_Names_on_Bars + " </td><td> <input type='checkbox' id='" + chartId + "innerLabels' class='gFontFamily gFontSize12' checked>";
    } else {
        axisProps += "" + translateLanguage.Axis_Names_on_Bars + " </td><td> <input type='checkbox' id='" + chartId + "innerLabels' class='gFontFamily gFontSize12'>";
    }
    axisProps += "</td>";

    axisProps += "<td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Axis_Names_Font_Size + " </label></td>";
    axisProps += "<td><select id='" + chartId + "Afontsize' class='gFontFamily gFontSize12' name='fontname'>";
    if (typeof chartData[chartId]["Afontsize"] === 'undefined' || chartData[chartId]["Afontsize"] === '') {
        axisProps += "<option class='gFontFamily gFontSize12' value='' selected> Select </option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='' > Select </option>";
    }
    for (var i = 11; i <= 18; i++)
    {
        if (typeof chartData[chartId]["Afontsize"] !== 'undefined') {
            if (chartData[chartId]["Afontsize"] === i.toString()) {
                axisProps += "<option class='gFontFamily gFontSize12' value=" + i + " selected> " + i + " </option>";
            } else {
                axisProps += "<option class='gFontFamily gFontSize12' value=" + i + "> " + i + " </option>";
            }
        } else {
            axisProps += "<option class='gFontFamily gFontSize12' value=" + i + " > " + i + " </option>";
        }
    }
    axisProps += "</td>";
    axisProps += "</tr>";

    axisProps += "<tr>";
    axisProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Trend_Line_Type + "</label></td>";
    axisProps += "<td><select id='" + chartId + "trlineType' class='gFontFamily gFontSize12'  name='linename'>";
    if (typeof chartData[chartId]["trlineType"] === 'undefined' || chartData[chartId]["trlineType"] === 'default') {
        axisProps += "<option class='gFontFamily gFontSize12' value='default' selected>Default</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='default'>Default</option>";
    }
    if (chartData[chartId]["trlineType"] === 'dashed-Line') {
        axisProps += "<option class='gFontFamily gFontSize12' value='dashed-Line' selected>Dashed-Line</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='dashed-Line'>Dashed-Line</option>";
    }
    axisProps += "</select></td>";

    axisProps += "<td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_GridLines + " </label></td>";
    axisProps += "<td><select id='" + chartId + "GridLines' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["GridLines"] === "undefined" || chartData[chartId]["GridLines"] === "Yes") {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["GridLines"] === "No") {
        axisProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    axisProps += "</select></td>";
    axisProps += "</tr>";

    axisProps += "<tr>";
    axisProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> X-" + translateLanguage.Axis_Ticks_Interval + " </label></td>";
    axisProps += "<td><select id='" + chartId + "XaxisTicks'  class='gFontFamily gFontSize12'  name='fontname'>";
    if (typeof chartData[chartId]["XaxisTicks"] === 'undefined' || chartData[chartId]["XaxisTicks"] === '0') {
        axisProps += "<option class='gFontFamily gFontSize12' value='0' Default> Default </option>";
    } else {
        axisProps += "<option class='gFontFamily gFontSize12' value='0' > Default </option>";
    }
    for (var x = 1; x <= 12; x++)
    {
        if (typeof chartData[chartId]["XaxisTicks"] !== 'undefined') {
            if (chartData[chartId]["XaxisTicks"] === x.toString()) {
                axisProps += "<option class='gFontFamily gFontSize12' value=" + x + " selected> " + x + " </option>";
            } else {
                axisProps += "<option class='gFontFamily gFontSize12' value=" + x + "> " + x + " </option>";
            }
        } else {
            axisProps += "<option class='gFontFamily gFontSize12' value=" + x + " > " + x + " </option>";
        }
    }
    axisProps += "</td>";
    axisProps += "</tr>";

    axisProps += "</table></div>";
//.........................................................................................
    var legendProps = "";
    var chartData = JSON.parse($("#chartData").val());

    legendProps += "<div id='legendProperties' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    legendProps += "<table  id='legendProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";

    legendProps += "<tr>";
    legendProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Data_Label_Type + " </label></td>";
    legendProps += "<td><select id='" + chartId + "valueOf' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["valueOf"] === "undefined" || chartData[chartId]["valueOf"] === "%-wise") {
        legendProps += "<option class='gFontFamily gFontSize12' value='%-wise' selected>%-wise</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='%-wise'>%-wise</option>";
    }
    if (chartData[chartId]["valueOf"] === "Absolute") {
        legendProps += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
    }
    else {
        legendProps += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
    }
    legendProps += "</select></td>";

    legendProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.No_Of_Legends + " </label></td>";
    legendProps += "<td>";
    var legendNo = "";
    if (typeof chartData[chartId]["legendNo"] !== "undefined" && chartData[chartId]["legendNo"] !== "") {
        legendNo = chartData[chartId]["legendNo"];
    }
    legendProps += "<input type='text' id='" + chartId + "legendNo' size='6' class='gFontFamily gFontSize12' value='" + legendNo + "'>";
    legendProps += "</td>";
    legendProps += "</tr>";

    legendProps += "<tr>";
    legendProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Show_Labels + " </label></td>";
    legendProps += "<td><select id='" + chartId + "dataDisplay' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["dataDisplay"] === 'undefined' && (JSON.parse($("#visualChartType").val())[$("#currType").val()] == "Advance-Pie" || JSON.parse($("#visualChartType").val())[$("#currType").val()] == "multi-kpi"))
    {
        legendProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        legendProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    } else {
        if (typeof chartData[chartId]["dataDisplay"] === "undefined" || chartData[chartId]["dataDisplay"] === "No") {
            legendProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            legendProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        if (chartData[chartId]["dataDisplay"] === "Yes") {
            legendProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            legendProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
    }
    legendProps += "</select></td>";

    var labelColors = "#000000";
    if (typeof chartData[chartId]["labelColors"] !== "undefined" && chartData[chartId]["labelColors"] != "") {
        labelColors = chartData[chartId]["labelColors"];
    }
    legendProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Label_Colors + " </td><td> <input style='background-color:" + labelColors + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='labelColors' readonly></td>";
    legendProps += "</tr>";

    legendProps += "<tr>";
    if (typeof chartData[chartId]["lbColor"] !== 'undefined' && chartData[chartId]["lbColor"] !== '') {
        color = chartData[chartId]["lbColor"];
    } else {
        color = "#ffffff";
    }
    legendProps += "<td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Legend_Box_Background + "</label></td>";
    legendProps += "<td><input id='lbcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"lbcolor\")' style='width:30px;background-color:" + color + "' ></td>";

    legendProps += "</td>";

    legendProps += "<td ><label style='font:white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Legend_Box_Border + " </label></td>";
    legendProps += "<td><select id='" + chartId + "legendBoxBorder' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["legendBoxBorder"] === "undefined" || chartData[chartId]["legendBoxBorder"] === "Dotted") {
        legendProps += "<option class='gFontFamily gFontSize12' value='Dotted' selected>Dotted</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='Dotted'>Dotted</option>";
    }
    if (chartData[chartId]["legendBoxBorder"] === "Solid") {
        legendProps += "<option class='gFontFamily gFontSize12' value='Solid' selected>Solid</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='Solid'>Solid</option>";
    }
    if (chartData[chartId]["legendBoxBorder"] === "None") {
        legendProps += "<option class='gFontFamily gFontSize12' value='None' selected>None</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='None'>None</option>";
    }
    legendProps += "</select>";
    legendProps += "</td>";
    legendProps += "</tr>";

    legendProps += "<tr><td><label style='font:white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_Legends + " </label></td>";
    legendProps += "<td>";
    legendProps += "<select id='" + chartId + "displayLegends' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["displayLegends"] === "undefined" || chartData[chartId]["displayLegends"] === "Yes") {
        legendProps += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["displayLegends"] === "No") {
        legendProps += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        legendProps += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    legendProps += "</select></td>";
    legendProps += "</tr>";

    legendProps += "</table></div>";
//.........................................................................................

    var measureProps = "";
    var chartData = JSON.parse($("#chartData").val());

    measureProps += "<div id='measureProperties' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    measureProps += "<table  id='measureProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";
    measureProps += "<tr><td><span style='align:left' class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Format + "</span> </td><td> ";
    measureProps += "<select id='" + chartId + "measureFormat' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["yAxisFormat"] === "undefined" || chartData[chartId]["yAxisFormat"] === "") {
        measureProps += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
    } else {
        measureProps += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
    }

    if (chartData[chartId]["yAxisFormat"] === "K") {
        measureProps += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
    } else {
        measureProps += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "M") {
        measureProps += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
    } else {
        measureProps += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "Cr") {
        measureProps += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
    } else {
        measureProps += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "L") {
        measureProps += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
    } else {
        measureProps += "<option class='gFontFamily gFontSize12' value='L'>Lakh</option>";
    }
    measureProps += "</select></td>";
    measureProps += "</td></tr>";
    measureProps += "</table></div>";
//..................................................................................................................

//    innerHtml += "<tr><td colspan='3'  align='center'><input type='button' value='Done' onclick='applyProperties()'></td></tr>";
//
//    innerHtml += "<table>";
    var doneProp = "";
    doneProp += '<div id="button">';
    doneProp += '<table><tr style="height:25px"><td colspan="4" align="center" ><a  name="' + chartId + '" onclick="applyProperties()" class="cssSubmitButton gFontFamily gFontSize12" >' + translateLanguage.Done + '  </a>';
    doneProp += '</td></tr></table></div>';
//.......................................................................................................
    $("#advanceProperties").html(innerHtml);
    $("#advanceProperties").append(axisProps);
    $("#advanceProperties").append(legendProps);
    $("#advanceProperties").append(measureProps);
    $("#advanceProperties").append(doneProp);
    $("#advanceProperties").dialog('open');
}
function generalvisual() {
    // alert(innerHtml)
    $("#axisProperties").hide();
    $("#legendProperties").hide();
    $("#measureProperties").hide();
    $("#generalTable").show();
}

function xyaxisvisual() {
    // alert(axisProp)
    $("#generalTable").hide();
    $("#legendProperties").hide();
    $("#measureProperties").hide();
    $("#axisProperties").show();
}

function legendslabelsvisual() {
    //  alert(axisProp)
    $("#axisProperties").hide();
    $("#generalTable").hide();
    $("#measureProperties").hide();
    $("#legendProperties").show();
}

function measuresvisual() {
    //  alert(axisProp)
    $("#axisProperties").hide();
    $("#generalTable").hide();
    $("#legendProperties").hide();
    $("#measureProperties").show();
}
// end new property by mynk sh.
function applyProperties() {
    //         parent.$("#loading").show();
    var chartId = "chart1";
    var chartData = JSON.parse($("#chartData").val());
    var key = Object.keys(chartData);

    var measures = chartData[chartId]["meassures"];
    var nameVal = $("#" + chartId + "Name").val();
    chartData[chartId]["Name"] = nameVal;
    var value = $("#" + chartId + "valueOf").val();
    chartData[chartId]["valueOf"] = value;
    var chartRecords = $("#" + chartId + "Records").val();
    chartData[chartId]["records"] = chartRecords;
    var rounding = $("#" + chartId + "rounding").val();
    chartData[chartId]["rounding"] = rounding;
    //added by Ram
    var rounding1 = $("#" + chartId + "rounding1").val();
    chartData[chartId]["rounding1"] = rounding1;
    var yAxisFormat = $("#" + chartId + "yAxisFormat").val();
    chartData[chartId]["yAxisFormat"] = yAxisFormat;
    var y2AxisFormat = $("#" + chartId + "y2AxisFormat").val();
    chartData[chartId]["y2AxisFormat"] = y2AxisFormat;
    var displayX = $("#" + chartId + "displayX").val();
    chartData[chartId]["displayX"] = displayX;
    var displayY = $("#" + chartId + "displayY").val();
    chartData[chartId]["displayY"] = displayY;
    var displayY1 = $("#" + chartId + "displayY1").val();
    chartData[chartId]["displayY1"] = displayY1;

    var userColor = $("#labelColors").css('background-color');
    if (typeof userColor != 'undefined') {
        userColor = hexc(userColor);
        var labelColors = userColor;
        chartData[chartId]["labelColors"] = labelColors;
    }

    var Afontsize = $("#" + chartId + "Afontsize").val();
    chartData[chartId]["Afontsize"] = Afontsize;
    var trlineType = $("#" + chartId + "trlineType").val();
    chartData[chartId]["trlineType"] = trlineType;
    var GridLines = $("#" + chartId + "GridLines").val();
    chartData[chartId]["GridLines"] = GridLines;
    var legendNo = $("#" + chartId + "legendNo").val();
    chartData[chartId]["legendNo"] = legendNo;
    var displayXLine = $("#" + chartId + "displayXLine").val();
    chartData[chartId]["displayXLine"] = displayXLine;
    var displayYLine = $("#" + chartId + "displayYLine").val();
    chartData[chartId]["displayYLine"] = displayYLine;
    var displayLegends = $("#" + chartId + "displayLegends").val();
    chartData[chartId]["displayLegends"] = displayLegends;


    var dataDisplay = $("#" + chartId + "dataDisplay").val();
    chartData[chartId]["dataDisplay"] = dataDisplay;
    var sortBasis = $("#" + chartId + "sortBasis").val();
    chartData[chartId]["sortBasis"] = sortBasis;
    var sorting = $("#" + chartId + "sorting").val();
    chartData[chartId]["sorting"] = sorting;
    var innerLabels = "";
    if ($("#" + chartId + "innerLabels").prop("checked")) {
        innerLabels = "Y";
    } else {
        innerLabels = "N";
    }
    chartData[chartId]["innerLabels"] = innerLabels;
    var enableQuickFilter2 = "";
    if ($("#" + chartId + "enableQuickFilter2").prop("checked")) {
        enableQuickFilter2 = "Y";
    } else {
        enableQuickFilter2 = "N";
    }
    chartData[chartId]["enableQuickFilter2"] = enableQuickFilter2;
    var completeChartData = "";
    if ($("#" + chartId + "_completeChartData").prop("checked")) {
        completeChartData = "Yes";
    } else {
        completeChartData = "N";
    }
    chartData[chartId]["completeChartData"] = completeChartData;
    var XaxisTicks = $("#" + chartId + "XaxisTicks").val();
    chartData[chartId]["XaxisTicks"] = XaxisTicks;

    var multiFilter = "";
    if ($("#" + chartId + "multiFilter").prop("checked")) {
        multiFilter = "Y";
    } else {
        multiFilter = "N";
    }
    chartData[chartId]["multiFilter"] = multiFilter;


    var shadingType = $("#shadeType").val();
    if (shadingType == "conditional") {
        isShadedColor = true;
        $("#isShaded").val("true");


        var color;
        //    conditionalMap["measure"]=$("#conditionalMeasure").val();
        for (var i = 0; i <= parent.counter; i++) {
            conditionalMap[i] = {};
            color = chartData[chartId]["colorMapPicker"];
            parent.$("#conditionalChooser" + i).val(color["conditionalChooser" + i]);
            //        conditionalMap[i]["color"]="#"+parent.$("#conditionalChooser"+i).val();
            conditionalMap[i]["color"] = parent.$("#conditionalChooser" + i).val();
            conditionalMap[i]["operator"] = parent.$("#operator" + i).val();
            var limit1 = parent.$("#firstOperand" + i).val();
            if (limit1 == 0 || limit1 == null) {
                limit1 = limit1;
            }
            conditionalMap[i]["limit1"] = limit1;
            if (isBinaryOperator(parent.$("#operator" + i).val())) {
                var limit2 = parent.$("#secondOperand" + i).val();
                if (limit2 == 0 || limit2 == null) {
                    limit2 = limit1;
                }
                conditionalMap[i]["limit2"] = limit2;
            }
        }
        parent.$("#conditionalMap").val(JSON.stringify(conditionalMap));
    } else {
        $("#gradientShading").hide(400);

        var color = chartData[chartId]["colorPicker"];
        var measure = $("#measureListGradient").val();
        isShadedColor = true;
        //    $("#gradShade").val(color);
        //    shadingType="gradient";
        //    $("#shadeType").val(shadingType);
        $("#isShaded").val("true");
        var colorMap = {};
        colorMap[measure] = color;
        colorMap["measure"] = measure;
        parent.$("#measureColor").val(JSON.stringify(colorMap));
        parent.$('#colorDiv').hide(400);
        parent.$("#chartDiv").hide(400);
        parent.$("#SortDiv").hide(400);
        parent.$('#measureDiv').hide(400);
        parent.$("#topBottomDiv").hide(400);
    }
    var chartIds = ""
    for (var id = 1; id < key.length; id++) {
        chartIds = key[id];
        chartId = "chart1";
        var nameVal1 = $("#" + chartId + "Name").val();
        chartData[chartIds]["Name"] = nameVal1;
        var value1 = $("#" + chartId + "valueOf").val();
        chartData[chartIds]["valueOf"] = value1;
        var chartRecords1 = $("#" + chartId + "Records").val();
        chartData[chartIds]["records"] = chartRecords1;
        var rounding1 = $("#" + chartId + "rounding").val();
        chartData[chartIds]["rounding"] = rounding1;
        //added by Ram
        var rounding11 = $("#" + chartId + "rounding1").val();
        chartData[chartIds]["rounding1"] = rounding11;
        var yAxisFormat1 = $("#" + chartId + "yAxisFormat").val();
        chartData[chartIds]["yAxisFormat"] = yAxisFormat1;
        var y2AxisFormat1 = $("#" + chartId + "y2AxisFormat").val();
        chartData[chartIds]["y2AxisFormat"] = y2AxisFormat1;
        var displayX1 = $("#" + chartId + "displayX").val();
        chartData[chartIds]["displayX"] = displayX1;
        var displayY1 = $("#" + chartId + "displayY").val();
        chartData[chartIds]["displayY"] = displayY1;
        var displayY11 = $("#" + chartId + "displayY1").val();
        chartData[chartIds]["displayY1"] = displayY11;


        var dataDisplay1 = $("#" + chartId + "dataDisplay").val();
        chartData[chartIds]["dataDisplay"] = dataDisplay1;
        var sortBasis1 = $("#" + chartId + "sortBasis").val();
        chartData[chartIds]["sortBasis"] = sortBasis1;
        var sorting1 = $("#" + chartId + "sorting").val();
        chartData[chartIds]["sorting"] = sorting1;
        var innerLabels1 = "";
        if ($("#" + chartId + "innerLabels").prop("checked")) {
            innerLabels1 = "Y";
        } else {
            innerLabels1 = "N";
        }
        chartData[chartIds]["innerLabels"] = innerLabels1;
        var completeChartData = "";
        if ($("#" + chartId + "_completeChartData").prop("checked")) {
            completeChartData = "Yes";
        } else {
            completeChartData = "N";
        }
        chartData[chartIds]["completeChartData"] = completeChartData;
        var XaxisTicks1 = $("#" + chartId + "XaxisTicks").val();
        chartData[chartIds]["XaxisTicks"] = XaxisTicks1;



    }
    userColor = $("#lbcolor").css('background-color');
    if (typeof userColor != 'undefined') {
        userColor = hexc(userColor);
        var lbcolor = userColor;
        chartData[chartId]["lbColor"] = lbcolor;
    }


    var legendBoxBorder = $("#" + chartId + "legendBoxBorder").val();
    chartData[chartId]["legendBoxBorder"] = legendBoxBorder;

    parent.$("#chartData").val(JSON.stringify(chartData));
    //    if(typeof chartData[chartId]["sorting"] !="undefined" && ! chartData[chartId]["sorting"]==""){
    //        var ctxPath=parent.document.getElementById("h").value;
    //        $.ajax({
    //                          type:'POST',
    //                          data:parent.$("#graphForm").serialize(),
    //
    //                     url: ctxPath+"/reportViewer.do?reportBy=editCharts&reportId="+parent.$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
    //                            success: function(data){
    //                                 parent.$("#loading").hide();
    //                                 alert(JSON.stringify(data))
    //                               parent.generateAdavanceVisual(data);
    //                            }
    //                    }
    //                )
    //    }else {
    //
    //        changeChart(chartId,chartData[chartId]["chartType"],nameVal);
    //
    //    }
    //getAdvanceVisuals(parent.$("#graphsId").val());
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
            function(data) {
                generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                $("#driver").val("");
            });

    $("#advanceProperties").dialog('close');
}


function graphColorPicker(flag) {
    var chartData = JSON.parse($("#chartData").val());
    var chartID = "chart1";
    var colorMapPicker = {};
    colorFlag = flag;
    //    var colorID = flag + chartID;
    $('#picker' + chartID).show();
    $('#picker' + chartID).colpick({
        flat: true,
        layout: 'hex',
        onSubmit: function(hsb, hex, rgb, el, bySetColor) {

            $('#picker' + chartID).hide();
            if (parent.$("#shadeType").val() == "conditional") {
                colorMapPicker[colorFlag] = "#" + hex;
                chartData[chartID]["colorMapPicker"] = colorMapPicker;
            } else {
                chartData[chartID]["colorPicker"] = "#" + hex;
            }
            parent.$("#chartData").val(JSON.stringify(chartData));
            // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
            if (!bySetColor)
                $(el).val(hex);
        }

    }).keyup(function() {

        $(this).colpickSetColor(this.value);
    });
}


function getConditionalColorIndia(color1, value) {
    var conditionalMap = JSON.parse($("#conditionalMap").val());
    value = parseInt(value);
    var keys = Object.keys(conditionalMap);
    //                for (var i = 0; i < (keys.length - 1); i++) {
    for (var i = 0; i < (keys.length); i++) {
        if (conditionalMap[i]["operator"] === "<>" && value > parseInt(conditionalMap[i]["limit1"]) && value <= parseInt(conditionalMap[i]["limit2"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === "<" && value < parseInt(conditionalMap[i]["limit1"])) {

            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === ">" && value > parseInt(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === "<=" && value <= parseInt(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === ">=" && value >= parseInt(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        }
    }
    return color1;
}


function generateQuickTrend(reportId, graphFlag) {
    parent.$("#chartData").val("");
    $("#quickTrend").dialog({
        autoOpen: false,
        height: 350,
        width: 850,
        position: 'justify',
        modal: true,
        resizable: true,
        title: 'Quick Trend',
        // Added By Ram for filter issue
        close: function(event, ui) {
            $("#drills").val('');
            $("#driverList").val('');
            fList = [];
            chartCount = 0;

        }
    });
    $("#quickTrend").html("");
    var htmlVar = "";
    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    $.post(
            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + reportId + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()),
            function(data) {
                //            $("#loading").hide();
                if (data == "false") {
                    $("#quickTrend").dialog('open');
                    htmlVar += '<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
                    $("#quickTrend").append(htmlVar);
                    //                $("#xtendChartTD").show();
                    //                $("#loading").hide();
                }
                else {
                    $("#quickTrend").dialog('open');
                    var jsondata = JSON.parse(data)["data"];
                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                    var meta = JSON.parse(JSON.parse(data)["meta"]);
                    $("#viewby").val(JSON.stringify(meta["viewbys"]));
                    $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                    $("#measure").val(JSON.stringify(meta["measures"]));
                    $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                    $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                    $("#drilltype").val((meta["drillType"]));
                    $("#draggableViewBys").val('');
                    $("#type").val("quick");
                    var filterMap = {};
                    //        var filterKeys = Object.keys(filterData);
                    var filterValue = [];
                    var name;
                    var id = viewByFilterID.split("A_")[1];
                    name = viewByFilterName.split("*,")[0];

                    filterValue = fList;
                    var index = filterValue.indexOf(name);
                    if (index != -1) {
                        filterValue.splice(index, 1);
                    }
                    filterMap[id] = filterValue;
                    if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
                        $("#filter1").val("");
                    }
                    //    alert(JSON.stringify(filterMap))
                    //    $("#filters1").val(JSON.stringify(reportToTrendFilter));
                    if (typeof filterMap !== "undefined") {
                        $("#filters1").val(JSON.stringify(filterMap))
                    } else {
                        $("#filters1").val(JSON.stringify(meta["filterMap"]));
                    }

                    var chartData = {};
                    var rowMeasId = [];
                    var rowMeasName = [];
                    var rowViewIds = [];
                    var rowViewName = [];
                    var aggregation = [];
                    if (typeof meta["measureIds"] !== "undefined") {
                        rowMeasId = meta["measureIds"];
                    }
                    if (typeof meta["measures"] != "undefined") {
                        rowMeasName = meta["measures"];
                    }
                    if (typeof meta["viewbyIds"] !== "undefined") {
                        rowViewIds = meta["viewbyIds"];
                    }
                    if (typeof meta["viewbys"] != "undefined") {
                        rowViewName = meta["viewbys"];
                    }
                    if (typeof meta["aggregations"] != "undefined") {
                        aggregation = meta["aggregations"];
                    }

                    var chartDetails = {};
                    var viewBys = [];
                    var viewIds = [];
                    var flag = false;
                    var timeDim = ["Month Year", "Month - Year", "Month-Year", "Time", "Year", "year", "Month", "Qtr", "qtr", "Month "];
                    for (var i = 0; i < rowViewIds.length; i++) {
                        for (var k = 0; k < timeDim.length; k++) {


                            if (rowViewName[i].trim() == timeDim[k].trim()) {
                                viewBys.push(rowViewName[i]);
                                viewIds.push(rowViewIds[i]);
                                flag = true;
                                break;
                            }

                            //                        break;
                        }
                        if (flag)
                            break;
                    }

                    chartDetails["viewBys"] = viewBys;
                    chartDetails["chartType"] = "quickAnalysis";
                    chartDetails["viewByLevel"] = "single";
                    chartDetails["meassures"] = rowMeasName;
                    chartDetails["aggregation"] = aggregation;
                    chartDetails["viewIds"] = viewIds;
                    chartDetails["meassureIds"] = rowMeasId;
                    chartDetails["dimensions"] = viewIds;
                    chartDetails["size"] = "S";
                    chartData["chart1"] = chartDetails;

                    var ctxPath = parent.document.getElementById("h").value;
                    parent.$("#chartData").val(JSON.stringify(chartData));
                    $.ajax({
                        type: 'POST',
                        data: parent.$("#graphForm").serialize(),
                        url: ctxPath + "/reportViewer.do?reportBy=buildchartsWithObject&reportId=" + reportId + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&chartData=" + encodeURIComponent(parent.$("#chartData").val()),
                        //                     url: ctxPath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
                        success: function(data) {
                            parent.$("#loading").hide();

                            //                               alert(grid+"   lkiyu")
                            generateQuickTrendChart(data);
                        }
                    });


                }


            });
}

function generateQuickTrendChart(data) {
    $("#quickTrend").html("");
    //$("#quickTrend").html('');
    var parameterName = "";
    if (typeof viewByFilterName != "undefined") {
        parameterName = viewByFilterName;
    }
    var chartId = "chart1";
    var chartData = JSON.parse(parent.$("#chartData").val());
    var rowViewBy = JSON.parse(parent.$("#viewby").val());
    var rowViewId = JSON.parse(parent.$("#viewbyIds").val());
    var quickViewname = [];
    var quickViewId = [];
    var selectedView = chartData[chartId]["viewBys"];
    var selectedViewId = chartData[chartId]["viewIds"];
    //alert(JSON.stringify(chartData))
    var timeDim = ["Month Year", "Month - Year", "Month-Year", "Time", "Year", "year", "Month", "Qtr", "qtr", "Month "];
    for (var i = 0; i < rowViewId.length; i++) {

        for (var k = 0; k < timeDim.length; k++) {

            //    alert(rowViewBy[i]+"::::"+timeDim[k])
            if (rowViewBy[i] == timeDim[k]) {
                quickViewname.push(rowViewBy[i]);
                quickViewId.push(rowViewId[i]);
            }
        }
    }

    var html = "";
    html += "<div style='height:20px; width:100%'>";
    html += "<ul style='float:left'><li><strong>" + parameterName + "</strong></li></ul>";
    html += "<div style='float:right'>"
    html += "<select style='float:right;margin-right:10px'>";
    for (var i = 0; i < chartData[chartId]["meassures"].length; i++) {
        html += "<option id='" + i + "' value='" + chartData[chartId]["meassures"][i] + "' onclick='changeMeasureArray(this.id)'>" + chartData[chartId]["meassures"][i] + "</option>";
    }
    html += "</select></div>";

    if (chartCount > 0) {
        html += "<div style='float:right;display:none'>"
    }
    else {
        html += "<div style='float:right'>"
    }
    html += "<select style='float:right;margin-right:10px'>";
    for (var l = 0; l < quickViewname.length; l++) {
        if (l == 0)
            html += "<option id='" + l + "' value='" + selectedView + ":" + selectedViewId + "' onclick='changeQuickGroup(this.value)'>" + selectedView + "</option>";
        if (quickViewname[l] != selectedView)
            html += "<option id='" + l + "' value='" + quickViewname[l] + ":" + quickViewId[l] + "' onclick='changeQuickGroup(this.value)'>" + quickViewname[l] + "</option>";
    }
    html += "</select></div>";
    html += "</div>";
    //html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

    $("#quickTrend").html(html);
    $("#quickTrend").parent().css({
        top: '30px'
    });
    generateTrendChart(data);
}

function generateTrendChart(jsonData) {
    //  graphData=JSON.parse(jsonData);
    // $("#Hchart1").html('');

    $("#quickTrend").html("");
    //$("#quickTrend").html('');
    var parameterName = "";
    if (typeof viewByFilterName != "undefined") {
        parameterName = viewByFilterName;
    }
    var chartId = "chart1";
    var chartData = JSON.parse(parent.$("#chartData").val());
    var rowViewBy = JSON.parse(parent.$("#viewby").val());
    var rowViewId = JSON.parse(parent.$("#viewbyIds").val());
    var quickViewname = [];
    var quickViewId = [];
    var selectedView = chartData[chartId]["viewBys"];
    var selectedViewId = chartData[chartId]["viewIds"];
    //alert(JSON.stringify(chartData))
    var timeDim = ["Month Year", "Month - Year", "Month-Year", "Time", "Year", "year", "Month", "Qtr", "qtr", "Month "];
    for (var i = 0; i < rowViewId.length; i++) {

        for (var k = 0; k < timeDim.length; k++) {

            //    alert(rowViewBy[i]+"::::"+timeDim[k])
            if (rowViewBy[i] == timeDim[k]) {
                quickViewname.push(rowViewBy[i]);
                quickViewId.push(rowViewId[i]);
            }
        }
    }

    var html = "";
    html += "<div style='height:20px; width:100%'>";
    html += "<ul style='float:left'><li><strong>" + parameterName + "</strong></li></ul>";
    html += "<div style='float:right'>"
    html += "<select style='float:right;margin-right:10px'>";
    for (var i = 0; i < chartData[chartId]["meassures"].length; i++) {
        html += "<option id='" + i + "' value='" + chartData[chartId]["meassures"][i] + "' onclick='changeMeasureArray(this.id)'>" + chartData[chartId]["meassures"][i] + "</option>";
    }
    html += "</select></div>";
    if (chartCount > 0) {
        html += "<div style='float:right;display:none'>"
    }
    else {
        html += "<div style='float:right'>"
    }
    html += "<select style='float:right;margin-right:10px'>";
    for (var l = 0; l < quickViewname.length; l++) {
        if (l == 0)
            html += "<option id='" + l + "' value='" + selectedView + ":" + selectedViewId + "' onclick='changeQuickGroup(this.value)'>" + selectedView + "</option>";
        if (quickViewname[l] != selectedView)
            html += "<option id='" + l + "' value='" + quickViewname[l] + ":" + quickViewId[l] + "' onclick='changeQuickGroup(this.value)'>" + quickViewname[l] + "</option>";
    }
    html += "</select></div>";
    html += "</div>";
    //html +="<div id='Hchart1' style='display:block;float:left;width:85%'></div>";

    //$("#quickTrend").html(html);

    //  var chartData = JSON.parse(parent.$("#chartData").val());
    $("#loading").hide();
    var charts = Object.keys(chartData);
    var data = JSON.parse(jsonData);
    for (var l = 0; l < charts.length; l++) {
        var divId = "Hchart" + parseInt(l + 1);
        html += "<div id='" + divId + "' style='display:block;float:left;width:85%'></div>";
        html += "<div class='tooltip' id='my_tooltip1' style='display: none'></div>";

    }
    $("#quickTrend").html(html);



    for (var kq = 0; kq < charts.length; kq++) {
        var ch = "chart" + parseInt(kq + 1);
        var html = "";

        var currData = [];
        var records = 12;
        var chartType = chartData[ch]["chartType"];
        var chartSize = chartData[ch]["size"];
        var width = $(window).width() * .45;
        if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
            records = chartData[ch]["records"];
        }
        for (var m = 0; m < (data[ch].length < records ? data[ch].length : records); m++) {
            currData.push(data[ch][m]);
        }


        //     measureData = currData;
        if (chartType == "quickAnalysis") {

            if (chartSize == "SS") {
                buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"], width * 1.5, "250");
            } else {
                buildQuickLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"], width, "500");
            }
        } else {
            if (chartSize == "SS") {
                $("#Hchart1").css("width", "50%");
                $("#Hchart2").css("width", "50%");
                buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"], width, "200");
            }
            else {
                buildQuickBar(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"], width, "300");
            }
        }
    }
}


function changeQuickGroup(val) {
    var chartData = JSON.parse($("#chartData").val());

    var column = [];
    var columnID = [];
    column.push(val.split(":")[0]);
    columnID.push(val.split(":")[1]);
    for (var ch in chartData) {
        chartData[ch]["viewBys"] = column;
        chartData[ch]["viewIds"] = columnID;
    }
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
            function(data) {
                if ($("#chartType").val() === 'Trend-Dashboard') {
                    var jsondata = JSON.parse(data);
                    generateVisual(jsondata);
                }
                else {
                    var jsondata = JSON.parse(data)["data"];
                    generateTrendChart(jsondata);
                }

            });
}
function setViewByDisplayTypeAll(chartId) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    chartData[chartId]["viewByDisplayType"] = "all";
    parent.$("#chartData").val(JSON.stringify(chartData));
}
function setViewByDisplayTypeChart(chartId) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    chartData[chartId]["viewByDisplayType"] = "chart";
    parent.$("#chartData").val(JSON.stringify(chartData));
}

function changeViewByGroup(chartId) {   // for single view by charts
    var chartData = JSON.parse(parent.$("#chartData").val());
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
    var columns = chartData[chartId]["viewBys"];
    var columnsId = chartData[chartId]["viewIds"];
    var columnId = chartData[chartId]["dimensions"];
    
    var chartType = $("#chartType").val();
    if (chartType == "Pie-Dashboard") {
        $("#quickTabs").html("");
    } else {
        $("#viewByChange" + chartId).html("");
    }
    var viewBys = [], viewIds = [];
    if (typeof chartData[chartId]["viewByDisplayType"] != 'undefined' && chartData[chartId]["viewByDisplayType"] != 'all') { // add by shivam   //update by mayank sharma
        var dimIds = chartData["chart1"]["dimensions"];
        var allViewBys = JSON.parse(parent.$("#viewby").val());
        var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
        for (var i in dimIds) {
            viewBys.push(allViewBys[allViewIds.indexOf(dimIds[i])]);
            viewIds.push(dimIds[i])
        }
    } else {
        viewBys = JSON.parse(parent.$("#viewby").val());
        viewIds = JSON.parse(parent.$("#viewbyIds").val());
    }

    var html = "";
    if (chartType !== "Pie-Dashboard") {
        for (var i in viewBys) {
            if (chartData[chartId]["viewBys"][0] === viewBys[i]) {
                html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer;background-color:#808080\" onclick='changeViewBys(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
            } else {
                html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer\" onclick='changeViewBys(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
            }
        }
    }
    var columnName = [];

    for (var i in columnId) {
        columnName.push(viewBys[viewIds.indexOf(columnId[i])]);
        if (chartType == "Pie-Dashboard") {
            html += "<li><a class='' id='" + columnName[i] + ":" + columnId[i] + "' name='tab" + parseInt(i + 1) + "' style=\"cursor: pointer\" onclick='changeViewBys(this.id,\"" + chartId + "\")'>" + columnName[i] + "</a></li>";
            if (i == 0) {
                var chartId = "chart1";
                html += "<li style='float:right'>";
                html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeMeasByGroup1(\"" + chartId + "\")' ></img>" +
                        "</div>";
                html += "</li>";
            }
        }
    }
    if (chartType == "Pie-Dashboard") {
        graphViewFlag = true;
        $("#quickTabs").html(html);
        $("#content").find("[id^='tab']").hide(); // Hide all content
        $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
        $("#content #tab1").fadeIn(); // Show first tab's content

        $('#quickTabs a').click(function(e) {
            e.preventDefault();
            if ($(this).closest("li").attr("id") == "current") { //detection for current tab
                return;
            }
            else {
                $("#content").find("[id^='tab']").hide(); // Hide all content
                $("#quickTabs li").attr("id", ""); //Reset id's
                $(this).parent().attr("id", "current"); // Activate this
                $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
            }
        });
    } else {
        graphViewFlag = false;
        $("#viewByChange" + chartId).html(html);
    }
}

function changeMeasByGroup1(chartId) {
    $("#quickTabs").html("");
    graphViewFlag = false;
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measureList = chartData["chart1"]["meassures"];
    var html = "";
    for (var i = 0; i < measureList.length; i++) {
        html += '<li><a href="#" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" onclick="changeMeasureArray(this.id)">' + measureList[i] + '</a></li>';
    }
    var chartId = "chart1";
    html += "<li style='float:right'>";
    html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img>" +
            "</div>";
    html += "</li>";
    $("#quickTabs").html(html);
    $("#content").find("[id^='tab']").hide(); // Hide all content
    $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
    $("#content #tab1").fadeIn(); // Show first tab's content

    $('#quickTabs a').click(function(e) {
        e.preventDefault();
        if ($(this).closest("li").attr("id") == "current") { //detection for current tab
            return;
        }
        else {
            $("#content").find("[id^='tab']").hide(); // Hide all content
            $("#quickTabs li").attr("id", ""); //Reset id's
            $(this).parent().attr("id", "current"); // Activate this
            $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
        }
    });
}

function changeViewBys(val, chartId, i,flag) {
    if(typeof flag!=='undefined' && flag==='trendChart'){
        $("#viewByList").hide();
    }
    var chartData = JSON.parse($("#chartData").val());
    var chartType = $("#chartType").val();
    var column = [];
    var pageValue = currentPage;
    if (typeof pageValue == "undefined" || pageValue == "") {
        pageValue = "default";
    }
    var columnID = [];
    selectedViewbyIndex = i;
    //Ashutosh
    try{
    var dataSlider=chartData[chartId]["dataSlider"];
    if(dataSlider[window.changedMeasureId]!=='undefined'&&dataSlider[window.changedMeasureId]==='Yes'){
        if(chartData[chartId]["Pattern"]==='Gradient'){
        chartData[chartId]["Pattern"]='Multi';
        }
        chartData[chartId]["measureFilters"]={};
        chartData[chartId]["sliderAxisVal"]={};
        window.flag=true;
        parent.range.axisVal={};
        parent.range.slidingVal={};
        parent.range.map={};
        parent.range.clausemap={};  
    }
}catch(e){}
    column.push(val.split(":")[0]);
    columnID.push(val.split(":")[1]);
    
    
  // add by mayank  
    var advanceChartData;
    try{
    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){}   
    if(typeof advanceChartData !=="undefined"){
       var advanceDetails = advanceChartData[window.complexId]["chart5"]; 
       advanceDetails["viewBys"] = column;
       advanceDetails["viewIds"] = columnID;
       advanceDetails["dimensions"] = columnID;
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
    }// add by mayank  

    if (chartType == "Pie-Dashboard") {
        for (var ch in chartData) {

            chartData[ch]["viewBys"] = column;
            chartData[ch]["viewIds"] = columnID;
        }

    } else {
        chartData[chartId]["viewBys"] = column;
        chartData[chartId]["viewIds"] = columnID;
    }
//    }
    var flag = 'viewByChange';
    var divIndex = parseInt(chartId.replace("chart", ""));
    var h = $("#divchart" + divIndex).height();
    var w = $("#divchart" + divIndex).width();
    var top = (h / 2) - 25;
    var left = (w / 2) - 25;
    $("#chart" + divIndex).html("<div id='chart_loading' style='position:absolute;top:" + top + "px;left:" + left + "px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='" + $("#ctxpath").val() + "/images/chart_loading.gif' alt='Loading...' /></div>");
    //    alert(JSON.stringify(chartData))
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&changeView=" + flag + "&viewChartId=" + chartId, $("#graphForm").serialize(),
            function(data) {
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                if (chartType == "Pie-Dashboard") {

                    generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                } else {
                    generateSingleChart(data, chartId);

                }


            });
}


function getConditionalColor(color1, value) {
    value = parseFloat(value);
    var keys = Object.keys(conditionalMap);
    for (var i = 0; i < (keys.length - 1); i++) {
        if (conditionalMap[i]["operator"] === "<>" && value > parseFloat(conditionalMap[i]["limit1"]) && value < parseFloat(conditionalMap[i]["limit2"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === "<" && value < parseFloat(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === ">" && value > parseFloat(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === "<=" && value <= parseFloat(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        } else if (conditionalMap[i]["operator"] === ">=" && value >= parseFloat(conditionalMap[i]["limit1"])) {
            return conditionalMap[i]["color"];
        }
    }
    return color1;
}

function targetColor(d, measure1, targetValue) {


    if (d[measure1] < parseInt(targetValue)) {
        return "red";
    } else {
        return "green";
    }
}


function openQuickForward(id, chartId, cx, cy) {
    chartCount++;
    $("#foreignObj" + chartId).show();
    $("#foreignObj" + chartId).click(function(e) {
        $(this).hide();
    });
    var innerHtml = '';
    innerHtml += "<table><tr><td style='width:1%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'></span>" +
            "<ul id='onChartChange" + chartId + "' class='dropdown-menu' >";


    //      $("#onChartChange"+chartId).html("");
    //    var chartData = JSON.parse(parent.$("#chartData").val());
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
    for (var i in viewBys) {
//        innerHtml += "<li><a class='' id='"+viewBys[i]+":"+viewIds[i]+"'  style=\"cursor: pointer\" onclick='changeQuickViewChart(\""+id+"\",\""+chartId+"\",this.id,\""+chartCount+"\")'>" + viewBys[i] + "</a></li>";
        innerHtml += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer\" onclick='enableRootAnalysis(\"" + id + "\",\"" + chartId + "\",this.id)'>" + viewBys[i] + "</a></li>";
    }
    innerHtml += "</ul></div></td></tr></table>"
    $("#callQuickDiv" + chartId).html(innerHtml);

    $("#onChartChange" + chartId).css('display', 'block');
    //$("#onChartChange"+chartId).show();
    $("#onChartChange" + chartId).css({
        top: parseInt(cy),
        left: parseInt(cx),
        position: 'relative'
    });

}

function changeQuickViewChart(idArr, div, val, i) {
    var count = parseInt(i);
    //    $("#viewBys").hide();
    $("#onChartChange" + div).css('display', 'none');
    var column = [];
    var columnID = [];
    var dimensionId = [];
    div = "chart" + (count + 1);
    var chartData = {};
    var chartDetails = {};
    var chartData = JSON.parse($("#chartData").val());
    var measureNames = chartData["chart1"]["meassures"];
    var meassureIds = chartData["chart1"]["meassureIds"];
    var aggregation = chartData["chart1"]["aggregation"];
    column.push(val.split(":")[0]);
    columnID.push(val.split(":")[1]);
    dimensionId = chartData["chart1"]["dimensions"];
    dimensionId.push(JSON.stringify(JSON.parse(columnID)));
    //    chartData[div]["drillType"] = "within";
    //    chartData[div]["viewBys"] = column;
    //    chartData[div]["viewIds"] = columnID;
    //chartData[div]["dimensions"] = dimensionId;
    //chartData[div]["chartType"] = 'Vertical-Bar';

    chartDetails["viewBys"] = column;
    chartDetails["chartType"] = "Vertical-Bar";
    chartDetails["viewByLevel"] = "single";
    chartDetails["meassures"] = measureNames;
    chartDetails["aggregation"] = aggregation;
    chartDetails["viewIds"] = columnID;
    chartDetails["meassureIds"] = meassureIds;
    chartDetails["dimensions"] = dimensionId;

    chartData["chart" + count]["size"] = "SS";
    chartData["chart" + (count + 1)] = chartDetails;

    $("#chartData").val(JSON.stringify(chartData));


    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());

    var id = idArr.split(",");
    var drills = {};
    // previous drills
    if (typeof $("#drills").val() != "undefined" && $("#drills").val() != "") {
        try {
            drills = JSON.parse($("#drills").val());
        } catch (e) {
        }
    }
    var drillValues = [];

    drillValues.push(idArr.split(":"))

    var drillMap = [];
    drillMap.push(idArr.split(":")[0]);
    drills[chartData[div]["dimensions"][count - 1]] = drillMap;
    //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
    //            }

    $("#driver").val("chart" + (count));
    var driverList = [];
    if (typeof $("#driverList").val() != "undefined" && $("#driverList").val() != "") {
        driverList = JSON.parse($("#driverList").val());
    }
    if (driverList.indexOf("chart" + (count)) == -1) {
        driverList.push("chart" + (count));
    }
    $("#driverList").val(JSON.stringify(driverList));

    var flag = true;

    if (flag) {
        $("#chartData").val(JSON.stringify(chartData));

        $("#drills").val(JSON.stringify(drills));
        $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
                function(data) {
                    var jsondata = JSON.parse(data)["data"];
                    //                        generateChart(data,div);
                    generateTrendChart(jsondata, div);
                });
    }
}

function showTableGraphs() {
    //var graphHtml = document.getElementById('iframe4').src;
    //
    //if(typeof graphHtml !=="undefined" && graphHtml !=="" && graphHtml !=="about:blank"){
    //    $('#tabGraphs').toggle();
    //    }else{
    //        alert('There is no Graph to show.')
    //    }

    parent.dispGraphsReport();
}


function downloadAsImage(chartId) {
    var chartname = JSON.parse($("#chartData").val())[chartId]["Name"];
    if (chartname == null)
    {
        chartname = chartId;
    }
    saveSvgAsPng(parent.document.getElementById("svg_" + chartId), chartname + ".png", {
        scale: 2
    }, chartname,JSON.parse($("#chartData").val())[chartId]["chartType"]);
}
var count = 0;
var data;
var pdf_w, pdf_h;
var pdf;
var max = 0;
function downloadAsPDF() {
    //    pdf=new jsPDF('p','mm',[200,300]);
    //    var html="<h1>SHOBHIT MISHRA</h1>";
    //    pdf.fromHTML(html, 0, 0);
    //    pdf.save("example.pdf");
    //    return;
    $("#pdfExportPageSetup").dialog({
        autoOpen: false,
        height: 400,
        width: 520,
        position: 'justify',
        modal: true,
        resizable: false,
        title: 'Export PDF Setup'
    });
    var chartData = JSON.parse(parent.$("#chartData").val());
    var html = '';
    html += "<div width='100%'>";
    html += "<table style='border-collapse: separate;border-spacing: 26px 15px;' >";
    html += "<tr>";
    html += "<td  width='50%' style='font-size=9pt;'>File Name";
    html += "</td>";
    html += "<td width='50%' style='font-size=9pt;'>";
    html += "<input id='pdfFileName' type='text' value=''/><label style='float:right'>.pdf</label>";
    html += "</td>";
    html += "</tr>";

    html += "<tr>";
    html += "<td width='50%' style='font-size=9pt;'>Page Type";
    html += "</td>";
    html += "<td width='50%' style='font-size=9pt;'><select id='pdfPageType'>";
    html += "<option value='a4' selected>A4</option>";
    html += "<option value='a3'>A3</option>";
    html += "<option value='legal'>Legal</option>";
    html += "</select>";
    html += "</td>";
    html += "</tr>";

    html += "<tr>";
    html += "<td width='50%' style='font-size=9pt;'>Page Orientation";
    html += "</td>";
    html += "<td width='50%' style='font-size=9pt;'><select id='pdfPageOrientation'>";
    html += "<option value='p' selected>Portrait</option>";
    html += "<option value='l'>Landscape</option>";
    html += "</select>";
    html += "</td>";
    html += "</tr>";

    html += "<tr style='display:none'>";
    html += "<td width='50%' style='font-size=9pt;'>Export Layout</td>";
    html += "<td width='50%' style='font-size=9pt;'><select id='pdfExportLayout' onchange='alignmentOption()'>";
    html += "<option value='oneonone'>Each chart on one page</option>";
    html += "<option value='asitis' selected>As it is</option>";
    html += "</select>";
    html += "</td>";
    html += "</tr>";

    html += "<tr id='pdfAlignment' style='display:none'>";
    html += "<td width='50%' style='font-size=9pt;'>Chart Alignment</td>";
    html += "<td width='50%' style='font-size=9pt;'><select id='chartAlignment'>";
    html += "<option value='top' selected>Top</option>";
    html += "<option value='center'>Center</option>";
    html += "</select>";
    html += "</td>";
    html += "</tr>";

    html += "<tr style='display:none'>";
    html += "<td width='50%' style='font-size=9pt;'><label>Charts Border</label>";
    html += "</td>";
    html += "<td width='50%'><input id='pdfBorder' type='checkbox' checked/>";
    html += "</td>";
    html += "</tr>";

    html += "<tr style='display:none'>";
    html += "<td width='50%' style='font: 11px verdana;font-size:9pt' width='50%'><label>Display Chart Names</label>";
    html += "</td>";
    html += "<td width='50%' width='50%'><input id='pdfChartName' type='checkbox'  checked/>";
    html += "</td>";
    html += "</tr>";

    html += "<tr>";
    html += "<td width='50%'>";
    html += "</td>";
    html += "<td width='50%' style='font-size=9pt;'>";
    html += "<input type='button' value='Done' onclick='chartsToPDF()' />";
    html += "</td>";
    html += "</tr>";

    html += "</table>";
    html += "</div>";
    $("#pdfExportPageSetup").html(html);
    $("#pdfExportPageSetup").dialog('open');

}
var fileName, isBorder, isName, layout, alignment;
var pdfData = '';
var headerOffset_w = 150 * 0.264583333;
var headerOffset_h = 51 * 0.264583333;
var headerOffset = 51 * 0.264583333;
var chartLeft = [], chartTop = [], chartWidth = [], chartHeight = [];
function chartsToPDF()
{
    chartLeft = [];
    chartTop = [];
    chartWidth = [];
    chartHeight = [];
    $("#printPreview").dialog({
        autoOpen: false,
        height: 400,
        width: 520,
        position: 'justify',
        modal: true,
        resizable: true,
        title: 'Exported Image'
    });
    //    var html;
    //    $("#printPreview").html("<div height='50px' width='100%'><img alt='' border='0px'  width='40px' height='30px'  title='Progen' src='"+$("#ctxpath").val()+"/images/prgLogo.gif'/></div>");
    //    $("#pdfPreview").html($("#gridUL").html());
    //    $("#printPreview").dialog('open');
    var orientation, pageType;
    alignment = $("#chartAlignment").val();
    //    isBorder = $('#pdfBorder').is(':checked');
    //    alert(isBorder);
    //    isName = $('#pdfChartName').is(':checked');
    //    alert(isBorder+":"+isName);
    fileName = $("#pdfFileName").val();
    layout = $("#pdfExportLayout").val();
    orientation = $("#pdfPageOrientation").val();
    pageType = $("#pdfPageType").val();
    $("#pdfExportPageSetup").dialog('close');
    if (pageType === 'a4') {
        pdf_w = 210;
        pdf_h = 297;
    }
    else if (pageType === 'a3') {
        pdf_w = 297;
        pdf_h = 420;
    }
    else if (pageType === 'legal') {
        pdf_w = 216;
        pdf_h = 356;
    }
    if (orientation === 'l') {
        var t = pdf_w;
        pdf_w = pdf_h;
        pdf_h = t;
    }
    //    alert(pdf_w+":"+pdf_h);
    pdf = new jsPDF(orientation, 'mm', [pdf_w, pdf_h]);
    var html = "<img id='progenlogoimg' src='" + $("#ctxpath").val() + "/images/ProGen_Logo.jpg'/>";
    $("#progenlogo").html(html);
    var canvas1 = document.createElement('canvas');
    var context = canvas1.getContext('2d');
    var img = parent.document.getElementById('progenlogoimg');
    img.onload = function() {
        context.drawImage(img, 0, 0);
        context.globalCompositeOperation = "destination-over";
        context.fillStyle = "#FFFFFF";
        context.fillRect(0, 0, canvas1.width, canvas1.height);
        var myData = canvas1.toDataURL('image/jpeg');
        pdf.addImage(myData, "JPEG", pdf_w - headerOffset_w, 3, headerOffset_w, headerOffset_h);
        pdf.setLineWidth(0.1);
        pdf.setDrawColor(0, 0, 0);
        pdf.line(0, headerOffset_h - 1.5, pdf_w, headerOffset_h - 1.5);
        pdf.setDrawColor(130, 130, 130);
        pdf.setFontSize(9);
        pdf.text(2, 3, "Name : " + $("#graphName").val());
        var d = new Date();
        var date = "Date Time : " + d.getDate() + "/" + d.getMonth() + "/" + d.getYear() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + " ";
        pdf.text(2, 7, date);
        //        pdf.line(0,0,10,10);
    }
    count = 0;
    var chartData = JSON.parse($("#chartData").val());
    var charts = Object.keys(chartData);
    var noOfRows = parseInt(Math.ceil(charts.length / 2))
    for (var i in charts) {
        if (i % 2 == 0) {
            chartLeft.push(1);
        }
        else {
            chartLeft.push(pdf_w / 2 + 1);
        }
        var currRow = parseInt(Math.ceil((parseInt(i) + 1) / 2))
        chartTop.push(1 + (headerOffset_h + 2) + ((pdf_h - headerOffset_h) / noOfRows) * (currRow - 1))
    }
    var w = $("#svg_chart1").width();
    var ratio = 0;
    if (charts.length == 1) {
        ratio = ((w * 0.264583333) / pdf_w)
        chartWidth.push(pdf_w - 1);
        chartHeight.push((pdf_h - headerOffset_h) * ratio - 1);
    }
    else {
        ratio = ((w * 0.264583333) / (pdf_w / 2))
        chartWidth.push(pdf_w / 2 - 1);
        if (charts.length == 2) {
            chartHeight.push((pdf_h - headerOffset_h) / ratio - 1);
        }
        else {
            chartHeight.push((pdf_h - headerOffset_h) / noOfRows - 1);
        }
    }
    if (charts.length % 2 == 0) {
        chartWidth[1] = chartWidth[0];
    }
    else {
        chartWidth[1] = pdf_w - 1;
    }
    for (property in chartData)
    {
        if (chartData.hasOwnProperty(property))
        {
            count++;
        }
    }
    exportToPDF(pdf, savePDF);
}
function savePDF()
{
    //    $(document.body).append("<input type='text' top='"+max+"'/>");
    //    var html1="<div width='100%' height='14px' style='background-color: #B4D9EE;text-align: center;'>"+
    //    "<span style='color:#000;font-size:10px;font-family:verdana;'>Copyright ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¾Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€šÃ‚Â¦ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬ÃƒÂ¢Ã¢â‚¬Å¾Ã‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã‚Â¦ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã¢â‚¬Â ÃƒÂ¢Ã¢â€šÂ¬Ã¢â€žÂ¢ÃƒÆ’Ã†â€™Ãƒâ€šÃ‚Â¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡Ãƒâ€šÃ‚Â¬ÃƒÆ’Ã¢â‚¬Â¦Ãƒâ€šÃ‚Â¡ÃƒÆ’Ã†â€™Ãƒâ€ Ã¢â‚¬â„¢ÃƒÆ’Ã‚Â¢ÃƒÂ¢Ã¢â‚¬Å¡Ã‚Â¬Ãƒâ€¦Ã‚Â¡ÃƒÆ’Ã†â€™ÃƒÂ¢Ã¢â€šÂ¬Ã…Â¡ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â© 2009-15  </span>"+
    //    "<span style='color:red;font-weight:bold;font-family:verdana;font-size:11px'>Progen Business Solutions.</span>"+
    //    "<span style='color:#000;font-size:10px;font-family:verdana;'>All Rights Reserved</span>"+
    //    "</div>";
    //    pdf.fromHTML(html1, 0, pdf_h-30);
    var html = "<img id='progenlogoimg1' src='" + $("#ctxpath").val() + "/images/ProGen_Logo.jpg'/>";
    $("#progenlogo").html(html);
    var canvas1 = document.createElement('canvas');
    var context = canvas1.getContext('2d');
    var img = parent.document.getElementById('progenlogoimg1');
    img.onload = function() {
        context.drawImage(img, 0, 0);
        context.globalCompositeOperation = "destination-over";
        context.fillStyle = "#FFFFFF";
        context.fillRect(0, 0, canvas1.width, canvas1.height);
        var myData = canvas1.toDataURL('image/jpeg');
        pdf.addImage(myData, "JPEG", 0, 3, headerOffset_w, headerOffset_h);
        pdf.setLineWidth(0.1);
        pdf.setDrawColor(0, 0, 0);
        pdf.line(0, headerOffset_h - 1.5, pdf_w, headerOffset_h - 1.5);
        pdf.setDrawColor(130, 130, 130);
        pdf.setFontSize(9);
        pdf.text(2, 3, "Name : " + $("#graphName").val());
        var d = new Date();
        var date = "Date Time : " + d.getDate() + "/" + d.getMonth() + "/" + d.getYear() + " " + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + " ";
        pdf.text(2, 7, date);
        //        pdf.line(0,0,10,10);
    }
    pdf.setDrawColor(255, 255, 255);
    pdf.setFillColor(255, 255, 255);
    pdf.rect(0, pdf_h - 6, pdf_w, pdf_h - 6, 'FD');
    pdf.setDrawColor(180, 217, 238);
    pdf.setFillColor(180, 217, 238);
    pdf.rect(0, pdf_h - 4, pdf_w, pdf_h, 'FD');
    pdf.setFontSize(8);
    var footer_left = pdf_w / 3.5;
    pdf.text(footer_left, pdf_h - 1, 'Copyright ÃƒÆ’Ã¢â‚¬Å¡Ãƒâ€šÃ‚Â© 2009-15 ');
    pdf.setTextColor(255, 0, 0);
    pdf.text(footer_left + 28, pdf_h - 1, 'Progen Business Solutions. ');
    pdf.setTextColor(0, 0, 0);
    pdf.text(footer_left + 28 + 35, pdf_h - 1, 'All Rights Reserved');
    //    pdf.setDrawColor(180,217,238);
    //    pdf.setFillColor(180,217,238);
    //    pdf.rect(0, pdf_h-8, pdf_w, pdf_h-5, 'FD');

    pdf.save(fileName + '.pdf');

}

function exportToPDF(pdf, doneCallback) {
    var i = 0;                                  // accessible from within next()
    var w, h, border_w, border_h;
    var element, left, top;
    (function next(data) {
        if (i != 0) {
            pdfData += data;
        }
        var header_offset_mm = headerOffset * 0.264583333;
        if (++i < count + 1) {                          // increase counter, then check [1,8]
            if (i != 1)
            {
                if (layout === 'oneonone') {
                    w = $("#svg_chart" + (i - 1)).width();
                    h = $("#svg_chart" + (i - 1)).height();
                    var w_mm = w * 0.264583333;
                    var h_mm = h * 0.264583333;
                    var ratio = w_mm / h_mm;
                    var r_step = pdf_w - w_mm;
                    w_mm = pdf_w;
                    h_mm += (r_step / ratio);
                    if (h_mm > pdf_h)
                    {
                        var h_step = pdf_h - h_mm;
                        h_mm = pdf_h;
                        w_mm = h_step * ratio;
                    }
                    var y = 0;
                    //                    alert(alignment);
                    if (alignment === 'center') {
                        //                        alert("Y")
                        var diff = pdf_h - h_mm;
                        y = diff / 2;
                        //                        alert(y);
                    }
                    pdf.addImage(data, "JPEG", 0, y + headerOffset_h, w_mm, h_mm);
                    if (i < count + 1) {
                        pdf.addPage();
                    }
                }
                else {
                    w = $("#divchart" + (i - 1)).width();
                    h = $("#divchart" + (i - 1)).height();
                    border_w = $("#divchart" + (i - 1)).width();
                    border_h = $("#divchart" + (i - 1)).height();
                    element = parent.document.getElementById('divchart' + (i - 1));
                    left = element.offsetLeft - element.scrollLeft + element.clientLeft;
                    top = element.offsetTop - element.scrollTop + element.clientTop;
                    pdf.setLineWidth(0.1);
                    //                    alert(header_offset_mm);
                    pdf.addImage(data, "JPEG", chartLeft[i - 2], chartTop[i - 2], chartWidth[0], chartHeight[0]);
                    //                    if(isBorder){
                    //                        alert(border_w+":"+left);
                    //                        alert(border_w+":"+left);
                    pdf.roundedRect(chartLeft[i - 2], chartTop[i - 2], chartWidth[0], chartHeight[0], 2, 2);
                    //                    }
                    if (parseInt(top * 0.264583333 + h * 0.264583333) > max) {
                        max = parseInt(top * 0.264583333 + h * 0.264583333);
                    }
                }
                //                pdf.addPage();
            }
            imageToData('chart' + i, i, pdf, next);       // use this method as callback
        }
        else {
            if (layout === 'oneonone') {
                w = $("#svg_chart" + (i - 1)).width();
                h = $("#svg_chart" + (i - 1)).height();
                var w_mm = w * 0.264583333;
                var h_mm = h * 0.264583333;
                var ratio = w_mm / h_mm;
                var r_step = pdf_w - w_mm;
                w_mm = pdf_w;
                h_mm += (r_step / ratio);
                if (h_mm > pdf_h)
                {
                    var h_step = pdf_h - h_mm;
                    h_mm = pdf_h;
                    w_mm = h_step * ratio;
                }
                var y = 0;
                //                    alert(alignment);
                if (alignment === 'center') {
                    //                        alert("Y")
                    var diff = pdf_h - h_mm;
                    y = diff / 2;
                    //                        alert(y);
                }
                pdf.addImage(data, "JPEG", 0, y + headerOffset_h, w_mm, h_mm);
                if (i < count + 1) {
                    pdf.addPage();
                }
            }
            else {
                w = $("#divchart" + (i - 1)).width();
                h = $("#divchart" + (i - 1)).height();
                border_w = $("#divchart" + (i - 1)).width();
                border_h = $("#divchart" + (i - 1)).height();
                //                w=$("#svg_chart"+(i-1)).width();
                //                h=$("#svg_chart"+(i-1)).height();
                //                border_w=$("#chart"+(i-1)).width();
                //                border_h=$("#chart"+(i-1)).height();
                element = parent.document.getElementById('divchart' + (i - 1));
                left = element.offsetLeft - element.scrollLeft + element.clientLeft;
                top = element.offsetTop - element.scrollTop + element.clientTop;
                if (parseInt(top * 0.264583333 + h * 0.264583333) > max) {
                    max = parseInt(top * 0.264583333 + h * 0.264583333);
                }
                pdf.addImage(data, "JPEG", chartLeft[i - 2], chartTop[i - 2], chartWidth[1], chartHeight[0]);
                //                if(isBorder){
                pdf.roundedRect(chartLeft[i - 2], chartTop[i - 2], chartWidth[1], chartHeight[0], 3, 3);
                //                }
            }
            if (doneCallback)
                doneCallback();   // when done, invoke main callback
        }
    })(data);                                       // self-invokes next()
}
function imageToData(chartId, i, pdf, callback)
{
    var canvas1 = document.createElement('canvas');
    var w = $("#svg_chart" + i).width(), h = $("#svg_chart" + i).height();
    canvas1.id = "canvas1";
    canvas1.width = w;
    canvas1.height = h;
    document.getElementById('pngcon').appendChild(canvas1);
    var chartname = JSON.parse($("#chartData").val())[chartId]["Name"];
    if (chartname == null)
    {
        chartname = chartId;
    }
    var border_w = $("#div" + chartId).width();
    var border_h = $("#div" + chartId).height();
    saveSvgAsPDF(document.getElementById("svg_" + chartId), "diagram.png", {
        scale: 2
    }, chartname, isName, chartId, border_w, border_h, pdf, callback);
}
function alignmentOption() {
    if ($("#pdfExportLayout").val() === 'oneonone') {
        $("#pdfAlignment").show();
    }
    else {
        $("#pdfAlignment").hide();
    }
}

// add by mynk sh.
function openChartGroup1(index, chartId, suffix)
{
    var html = "";

    html = html + "<ul id='more" + chartId + "' class='dropdown-menu'>";
    if (index == 0)
    {
        for (var i = 0; i < parent.barGraph.length; i++)
        {
            html = html + "<li><a class='' style=\"cursor: pointer\" onclick='advanceChangeChart(\"" + chartId + "\",\"" + parent.barGraph[i].replace(" ", "-", "gi") + "\")'>" + parent.barGraph[i] + "</a></li>";
        }
    }
    else if (index == 1)
    {
        for (var i = 0; i < parent.circularPieGraph.length; i++)
        {
            html = html + "<li><a class='' style=\"cursor: pointer\" onclick='advanceChangeChart(\"" + chartId + "\",\"" + parent.circularPieGraph[i].replace(" ", "-", "gi") + "\")'>" + parent.circularPieGraph[i] + "</a></li>";
        }
    }
    else if (index == 2)
    {
        for (var i = 0; i < parent.lineGraph.length; i++)
        {
            html = html + "<li><a class='' style=\"cursor: pointer\" onclick='advanceChangeChart(\"" + chartId + "\",\"" + parent.lineGraph[i].replace(" ", "-", "gi") + "\")'>" + parent.lineGraph[i] + "</a></li>";
        }
    }

    html = html + "</ul>";
    $("#more" + chartId).remove();
    $("#chartGroups" + suffix + chartId + index).append(html);
}

function advanceChangeChart(chartId, chartType, chartName) {

    var oldHtml = '';
    //alert(chartId.replace("H",'').replace ( /[^\d.]/g, '' ));

    oldHtml += "<div id='Graphdiv" + chartId.replace("H", '').replace(/[^\d.]/g, '') + "' style='display:block;float:left;width:100%;'>";
    oldHtml += $("#Graphdiv" + chartId.replace("H", '').replace(/[^\d.]/g, '')).html();
    oldHtml += "</div>";
    //alert(oldHtml);
    // alert(chartId.replace("H",'').replace ( /[^\d.]/g, '' ));
    $("#chartCom" + chartId.replace("H", '').replace(/[^\d.]/g, '')).html('');

    $("#chartCom" + chartId.replace("H", '').replace(/[^\d.]/g, '')).append(oldHtml);
    // var margin = {top: 10};
    //  var oldHtml='';
    //  oldHtml+="<div id='Graphdiv1' style='display:block;float:left;width:100%;'>";
    //   oldHtml+=$("#Graphdiv1").html();
    //    oldHtml+="</div>";
    //   alert(oldHtml);
    // $("#chartCom1").html('');
    // $("#chartCom1").append(oldHtml);


    var margin = {
        top: 10
    };

    advanceTypeFunction(chartId, chartType, chartName);
//            });

}
function advanceTypeFunction(div, chartType, total, flag) {
    // var html="";

    if (flag === 'PieDB') {
        if (chartType === 'Vertical-Bar' || chartType === 'Line') {
            $("#chartAd1").css("width", "100%");
            $("#chartAd2").css("width", "100%");
            $("#chartAd2").css("margin-left", "10%");
        }
        else {
            $("#chartAd1").css("width", "45%");
            $("#chartAd2").css("width", "50%");
            $("#chartAd2").css("margin-left", "0%");
        }
    }
    var chartId = div.replace("H", "");
    var currData = [];
    var v1 = total.split(":")[1];
    var v2 = total.split(":")[0];
    var data = parent.advanceData[chartId];


    var i;
    if (typeof v1 === "undefined") {
        v1 = 0;
        v2 = 15;
    }
    for (i = v1; i < (data.length < v2 ? data.length : v2); i++) {
        currData.push(data[i])
    }

    if (typeof currData === "undefined" || currData.length == 0) {
        return alert("No Data to Display.")
    }
    var newchartid;
    if ($("#chartType").val() == "Pie-Dashboard") {
        $("#chartAd1").html('');
        newchartid = "chartAd1";
    } else {

        newchartid = "chartCom" + div.replace("H", '').replace(/[^\d.]/g, '');
    }
    //     var chartNum=  chartId.replace ( /[^\d.]/g, '' );

    var chartData = JSON.parse(parent.$("#chartData").val());

    //     chartData[chartId]["chartType"]=chartType;
    // parent.$("#chartData").val(JSON.stringify(chartData));

    var chWidth = $(window).width() * .31;
    var chHeight;
    if (chartType == "Horizontal-Bar") {
        chHeight = $(window).height() * .46;    // for horizontal bar
        chWidth = $(window).width() * .35;
    }
    else if ($("#chartType").val() == "Pie-Dashboard") {
        chHeight = $(window).height() * .46;    // for horizontal bar
        chWidth = $(window).width() / 2 * .50;
    }
    else {
        chHeight = $(window).height() * .46;
    }


    if (chartType == "Vertical-Bar") {
        //         if($("#chartType").val()=="Pie-Dashboard"){
        //             chWidth="500";
        //        }
        //chWidth=$(window).width() *.85;
        chHeight = $(window).height() * .46;
        chartData[chartId]["chartType"] = chartType;
        parent.$("#chartData").val(JSON.stringify(chartData));
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildBarAdvanceDB(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
        else {
            buildBarAdvance(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildDashboardTable(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
    }
    else if (chartType == "Horizontal-Bar") {
        chartData[chartId]["chartType"] = chartType;
        parent.$("#chartData").val(JSON.stringify(chartData));
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildAdvanceHorizontalDB(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
        else {
            buildAdvanceHorizontal(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
    }

    else if (chartType == "Pie") {
        if ($("#chartType").val() == "Pie-Dashboard") {
            chWidth = "600.0833";
        }
        chartData[chartId]["chartType"] = chartType;
        parent.$("#chartData").val(JSON.stringify(chartData));
        buildComboPie(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildDashboardTable(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
    }

    else if (chartType == "Line") {
        if ($("#chartType").val() == "Pie-Dashboard") {
            chWidth = "500";
            chHeight = $(window).height() * .46;

        }
        chartData[chartId]["chartType"] = chartType;
        parent.$("#chartData").val(JSON.stringify(chartData));
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildAdvanceLineDB(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
        else {
            buildAdvanceLine(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
        if ($("#chartType").val() == "Pie-Dashboard") {
            buildDashboardTable(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
        }
    }
    else if (chartType == "India-Map") {
        if ($("#chartType").val() == "Pie-Dashboard") {
            chWidth = "500";
            chHeight = $(window).height() * .50;

        }
        var mapData = {};
        mapData["chart1"] = data;

        buildDistrictMapMini(chartId, newchartid, mapData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
    }

    else {

        buildBarAdvance(chartId, newchartid, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
    }
}


//eby mynk sh.



function updateChart(chartId) {

//    alert(chartId);
}

function drillWithinTabs(idArr, div) {



    var graphFlag = "Graph";
    var reportId = parent.$("#graphsId").val();
    var id = idArr.split(",");
    var drills = {};
    var drillValues = [];
    var chartData = JSON.parse($("#chartData").val());

    drillValues.push(idArr.split(":")[0]);
    drills[chartData[div]["viewIds"][0]] = drillValues;
    valueToParse = drills;
    $("#loading").show();
    $('#addUnderConsideration').hide();
    $('#xtendChartTD').hide();
    $("#noneDataDiv").css({
        'display': 'none'
    });
    $("#menuBarUITR").css({
        'display': 'none'
    });
    $("#footerTable").css({
        'display': 'block'
    });
    $("#gridsterDiv").css({
        'display': 'block'
    });

    //         $("li").filter(function()/ {
    //                           return $(this).text() == 'Graphical Analysis';
    //                       }).click(function() {/
    $('#themeselect').empty();
    $('#saveGraphLi').remove();
    var add = "add";
    var edit = "isEdit";
    var drillType = "multi";
    var html1 = "";
    //               var graphFlag = "Graph";
    html1 += '<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
    html1 += '<ul class="drpcontent" id="themeselect">';
    //            <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts(\"add1\")' >Add Graph Object</a>";
    html1 += "</li>";
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts(\"edit1\")' >Edit Graph Object</a>";
    html1 += "</li>";
    //            <% }%>
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\"" + add + "\")' >Add Graph</a>";
    html1 += "</li>";
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\"" + edit + "\")' >Edit Graph</a>";
    html1 += "</li>";
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='selectMultiDrill(\"" + drillType + "\")' >Enable Multi Drill</a>";
    html1 += "</li>";
    //               html1 += "<li style='text-align;'>";
    //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='within' onclick='parent.selectDrill(this.name)' >Drill Down</a>";
    //               html1 +="</li>";
    //               html1 += "<li style='text-align;'>";
    //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='single' onclick='parent.selectDrill(this.name)' >Drill Across</a>";
    //               html1 +="</li>";
    //               html1 += "<li style='text-align;'>";
    //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' name='multi' onclick='parent.selectDrill(this.name)' >Multi-Select Drill</a>";
    //               html1 +="</li>";
    //               html1 += "<li style='text-align;'>";
    //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editMeasure1()' >Add Graph Below</a>";
    //               html1 +="</li>";
    //            <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
    html1 += "<li id='saveWithGraph' style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save All Graphs</a>";
    html1 += "</li>";
    //            <% }%>
    html1 += "<li id='regenerateFilters' style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='regenerateFilters(" + parent.$("#graphsId").val() + ")' >Regenerate Filters</a>";
    html1 += "</li>";
    html1 += "<li id='exportToPDF' style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='downloadAsPDF()' >Save as PDF</a>";
    html1 += "</li>";
    html1 += "</li>";
    html1 += "<li>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
    html1 += "</li>";
    html1 += "<li id='resetId' style='text-align;'>";
    //               html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonData("+parent.$("#graphsId").val()+",Graph,"+gridster1+")' >Reset</a>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonDataReset(" + parent.$("#graphsId").val() + ")' >Reset</a>";
    html1 += "</li></ul>";
    $('#testCase').html(html1);
    //                       });

    $("#advanceAnchor").removeClass('active');
    $("#graphAnchor").attr('class', 'active');
    generateJsonData1st(reportId, graphFlag, gridster1);
}

function drillWithinTabs1() {
    var drills = valueToParse;
    $("#filters1").val("")
    //     $("#driver").val(div);
    $("#drills").val(JSON.stringify(drills));

    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
            function(data) {
                var jsondata = JSON.parse(data)["data"];

                var chartData = JSON.parse($("#chartData").val());
                for (var t in chartData) {
                    if (chartData[t]["chartType"] == "KPI-Table" || chartData[t]["chartType"] == "Table" || chartData[t]["chartType"] == "Expression-Table" || chartData[t]["chartType"] == "Emoji-Chart" || chartData[t]["chartType"] == "Stacked-KPI"|| chartData[t]["chartType"] == "Veraction-Combo"|| chartData[t]["chartType"] == "KPIDash" || chartData[t]["chartType"] == "Standard-KPI" || chartData[t]["chartType"] == "Standard-KPI1" || chartData[t]["chartType"] == "Radial-Chart" || chartData[t]["chartType"] == "LiquidFilled-KPI" || chartData[t]["chartType"] == "Dial-Gauge" || chartData[t]["chartType"] == "Emoji-Chart") {
                        $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]))
                    }
                }
                generateChart(jsondata);
            });

    setTimeout(updateglobalfilters($("#graphsId").val(), "advance", "graph"), 20000);
    $('#rightDiv1ad').hide();
    if ($.browser.msie) {
        $("#xtendChartssTD").css({
            'margin-top': '0px'
        });

        $('#rightDiv1').hide();
    } else {
        $('#rightDiv1').show();
    }

}


function drilltoReports(idArr, div,measureId) { 
    var ctxPath = $("#ctxpath").val();
    var id = idArr.split("::")[1];
    //            var drills = {};
    var drillValues = [];
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[div];
    var advanceType = parent.$("#advanceChartType").val();
    if (typeof advanceType !== "undefined" && (advanceType === "advanceGraph" || advanceType==="")) {
        var advanceChartData;
        try {

            advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
        } catch (e) {
        }
        if (typeof advanceChartData !== "undefined") {
            var advanceDetails = advanceChartData[window.complexId]["chart5"];
            chartDetails = advanceDetails;
        }
    }
    
    var viewBys = chartDetails["viewBys"][0];
    //            var reportId = $("#graphsId").val();
    var reportIdtoDrill;
    if (typeof chartData !== "undefined" && chartDetails["chartType"] === "Combo-Analysis") {

        if (typeof id !== "undefined" && id === "drill1") {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId1"];
        } else if (typeof id !== "undefined" && id === "drill2") {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId2"];
        }
        else if (typeof id !== "undefined" && id === "headerdrill1") {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId3"];
        }
        else if (typeof id !== "undefined" && id === "headerdrill2") {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId4"];
        }
        else {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId"];
        }

    }else if (chartData[div]["chartType"] === "Stacked-KPI" || chartData[div]["chartType"] === "Column-Pie" || chartData[div]["chartType"] === "Bullet-Horizontal") {
         var graphDrillMap1 = chartData[div]["graphDrillMap"];
         if(measureId in graphDrillMap1){
             reportIdtoDrill = graphDrillMap1[measureId];
         }
    } 
    else {
        if (typeof id !== "undefined" && id === "header") {
            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId"];
        } else {

            reportIdtoDrill = chartDetails["graphDrillMap"]["newReportId1"];
        }
    }
    //            alert(reportIdtoDrill)
    drillValues.push(idArr.split(":")[0]);
    //drillValues=idArr.split(":")[0];
    //            drills[chartDetails["viewIds"][0]] = drillValues;

    //            alert(JSON.stringify(drills))

    var parValues = JSON.parse(parent.$("#viewbyIds").val());
    for (var i = 0; i < parValues.length; i++) {

        var vpid = parValues[i].replace("CBOARP", "");
        var filterMap1 = {};
        var filterValues1 = [];
        if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
            filterMap1 = JSON.parse($("#filters1").val());
            if (typeof filterMap1[vpid] !== "undefined") {
                filterValues1 = filterMap1[vpid];
                if (filterValues1 !== "" && filterValues1 !== undefined && filterValues1.length >= 1 && filterValues1[0] != "All") {
                    $("#CBOARP" + vpid).val(JSON.stringify(filterValues1));
                }else{
                    filterValues1.push("All") 
                    $("#CBOARP" + vpid).val(JSON.stringify(filterValues1));
                }
            }else{
                filterValues1.push("All") 
                    $("#CBOARP" + vpid).val(JSON.stringify(filterValues1));
            }
        }
    }
    var url = "";
    if (drillValues[0].toUpperCase() in parent.lookupdataMap) {

        drillValues[0] = parent.lookupdataMap[drillValues[0].toUpperCase()]
    }

    if (typeof reportIdtoDrill !== "undefined") {
 parent.$("#CBOARP" + chartDetails["viewIds"][0]).val(JSON.stringify(drillValues))
        if (typeof id !== "undefined" && (id === "header" || id === "headerdrill2" || id === "headerdrill1")) {
            url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill + "&action=open&reportDrill=Y";
        } else {
            if (typeof chartDetails["chartType"] != "undefined" && chartDetails["chartType"] == "World-Top-Analysis") {
                url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill +"&reportDrill=Y&action=open";
            }
            else if (typeof id !== "undefined" && id === "drill1") {

                url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill +"&reportDrill=Y&action=open";
            } else if (typeof id !== "undefined" && id === "drill2") {
                url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill +"&reportDrill=Y&action=open";
            } else {
                url = "/reportViewer.do?reportBy=viewReport&REPORTID=" + reportIdtoDrill +"&reportDrill=Y&action=open";
            }
        }
//   url = "/reportViewer.do?reportBy=viewReport&REPORTID="+reportIdtoDrill+"&CBOARP"+chartDetails["viewIds"][0]+"=['"+drillValues+"']&reportDrill=Y",parent.$("#graphForm").serialize();
    } else {
        return alert("Issue in Assigning reports!");
    }
    if (id !== "header" && id !== "headerdrill2" && id !== "headerdrill1") {
        if (typeof viewBys !== "undefined" && viewBys.toString().toLowerCase() == "time") {
            url = timeDrillURL(div, reportIdtoDrill, drillValues.toString().replace(/\s+/g, ''));
        }
        var quickViewname = viewBys;
        if (typeof quickViewname !== "undefined") {
            if (quickViewname.toString().trim() == "Month Year"
                    || quickViewname.toString().trim() == "Month-Year"
                    || quickViewname.toString().trim() == "Month - Year"
                    || quickViewname.toString().trim() == "Month") {

                url = monthDrillURL(div, reportIdtoDrill, drillValues.toString().replace(/\s+/g, ''));
            } else if (quickViewname.toString().toLowerCase().trim() == "qtr"
                    || quickViewname.toString().toLowerCase().trim() == "qtr year" ||
                    quickViewname.toString().toLowerCase().trim() == "qtr-year" ||
                    quickViewname.toString().toLowerCase().trim() == "qtr - year") {
                url = qtrDrillURL(div, reportIdtoDrill, drillValues.toString().replace(/\s+/g, ''));
            } else if (quickViewname.toString().trim() === "Year" || quickViewname.toString().toLowerCase().trim() === "year") {
                url = yearDrillURL(div, reportIdtoDrill, drillValues.toString().replace(/\s+/g, ''));
            }

        }
    }
    document.frmParameter.action = ctxPath + url;
    document.frmParameter.target = "_blank";
    document.frmParameter.submit();
    document.frmParameter.target = "";
//   window.open(ctxPath+url,"_blank");
//    window.location.href = ctxPath + url;
}

function editDash(chartId) {
    $("#dashboardProp1").dialog({
        autoOpen: false,
        height: 550,
        width: 900,
        position: 'justify',
        modal: true,
        resizable: true,
        title: translateLanguage.Graph_Properties
    });
    $("#dashboardProp1").html("");
    $("#dashboardProp1").dialog('open');

    var innerHtml = "";
    innerHtml += "<div id='tabProperties' class='tag-link-properties' style='width:100%;height:5.5%;'>";
    //alert(kpiProp);
    // alert(axisProp);
    //  alert(innerHtml);
    //          innerHtml += "<table class='drawTab' id='selectTab' style='width:100%;'>";
    //          innerHtml+= "<tr style=' width: 100%;'>";
    //          innerHtml+= "<td style='width: 25%;'><input type='button' value='defaults' onclick='defult()' style='font-size:12px; width:85%;'></td>";
    //          innerHtml+= "<td style='width: 25%;'><input type='button' value='Kpi Chart' onclick='kpiCharts()' style='font-size:12px; width:85%;'></td>";
    //          innerHtml+= "<td style='width: 25%;'><input type='button' value='Axis Chart' onclick='axisCharts()' style='font-size:12px; width:85%;'></td>";
    //          innerHtml+= "<td style='width: 25%;'><input type='button'  value='Chart Section' onclick='chartSection()' style='font-size:12px; width:85%;'></td></tr>";
    //          innerHtml+="</table></div>";
    //     var html = "";
    var graphProp = ["General", "Legends/Labels", "X-Y Axis Property", "KPI Charts", "Measures"];
    innerHtml += '<ul id="quickTabs" >';
    for (var i = 0; i < graphProp.length; i++) {
        if (i == 0) // added by shivam
            innerHtml += '<li class="tabUL" style="border-bottom:3px solid #DC143C;"><a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" onclick="' + graphProp[i].trim().replace(" ", "").replace("/", "").replace("-", "").replace(" ", "").toLowerCase() + '(this)">' + graphProp[i] + '</a></li>';
        else
            innerHtml += '<li class="tabUL"><a style="padding: 0.3em 3.5em;" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" onclick="' + graphProp[i].trim().replace(" ", "").replace("/", "").replace("-", "").replace(" ", "").toLowerCase() + '(this)">' + graphProp[i] + '</a></li>';
    }
    innerHtml += '</ul>';
    innerHtml += "</div>";
    //$("#viewMeasureBlock").html(html);
    //   var innerHtml ="";
    
    //Added by Ashutosh
    try{
    var chartData = JSON.parse($("#chartData").val());
     var dataSlider=chartData[chartId]["dataSlider"];
//    alert("window.changedMeasureId"+window.changedMeasureId);
    if(dataSlider[window.changedMeasureId]!=='undefined' && dataSlider[window.changedMeasureId]==='Yes'){
        if(chartData[chartId]["Pattern"]==='Gradient'){
            chartData[chartId]["Pattern"]='Multi';
        }
        chartData[chartId]["measureFilters"]={};
//        chartData[chartId]["sliderAxisVal"]={};
//        window.flag=true;
//        parent.range.axisVal={};
//        parent.range.slidingVal={};
//        parent.range.map={};
//        parent.range.clausemap={};  
    }
    parent.$("#chartData").val(JSON.stringify(chartData));
    }catch(err){}
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var chartType = chartData[chartId]["chartType"];
    innerHtml += "<div id='dashboardTable' class='defaultProperty' style='width:100%;margin-left:1%;'>";
    innerHtml += "<table  id='dashboardTable1' style='border-collapse: separate;border-spacing: 26px 15px;'>";
    innerHtml += "<tr align='center'><td><label class='gFontFamily gFontSize12'>" + translateLanguage.Chart_Name + "</label></td>";

    var chartName = "";

    if (typeof chartData[chartId]["chartType"] != "undefined" && (chartType != "LiquidFilled-KPI" && chartType != "Dial-Gauge" && chartType != "Radial-Chart" && chartType != "Bullet-Horizontal" && chartType != "Standard-KPI" && chartType != "Standard-KPI1" && chartType != "Veraction-Combo" && chartType != "KPIDash" && chartType != "Bullet-Horizontal" && chartType != "Emoji-Chart")) {
        if (typeof chartData[chartId]["Name"] !== "undefined" && chartData[chartId]["Name"] !== "") {
            chartName = chartData[chartId]["Name"];
        }
        else {
            chartName = "";
        }
        innerHtml += "<td style='font-size:9pt;border-right:7px solid white'><input style='width:450px' type='text' id='" + chartId + "Name' class='labelStyle gFontFamily gFontSize12' value='" + chartName + "'></label></td></tr>";
        innerHtml += "<td style='display:none;font-size:9pt;border-right:7px solid white'><input style='width:450px' type='text' id='" + chartId + "KPIName' class='labelStyle' value='" + chartData[chartId]["KPIName"] + "'></label></td></tr>";

    } else {

        if (typeof chartData[chartId]["KPIName"] !== "undefined" && chartData[chartId]["KPIName"] !== "") {
            chartName = "";
        }
        else {
            chartName = chartData[chartId]["KPIName"];
        }
        innerHtml += "<td style='font-size:9pt;border-right:7px solid white'><input style='width:450px' type='text' id='" + chartId + "KPIName' class='labelStyle' value='" + chartName + "'></label></td></tr>";
        innerHtml += "<td style='display:none;font-size:9pt;border-right:7px solid white'><input style='width:450px' type='text' id='" + chartId + "Name' class='labelStyle' value='" + chartData[chartId]["Name"] + "'></label></td></tr></table>";

    }
//
//    innerHtml += "<tr align='center'><td><label style='font: 11px verdana;'> Chart Description </label></td>";
//    var chartDecription="";
//    if (typeof chartData[chartId]["chartDecription"] !== "undefined" && chartData[chartId]["chartDecription"] !== "") {
//        chartDecription = chartData[chartId]["chartDecription"];
//    }
//    innerHtml += "<td style='font-size:9pt;border-right:7px solid white'><input style='width:450px' type='text' id='"+chartId+"chartDecription' class='chartStyle' value='" +chartDecription+ "'></label></td>";
//    innerHtml += "</tr>";

    innerHtml += "<table style='border-collapse: separate;border-spacing: 26px 15px;'>";
    innerHtml += "<tr>";
    innerHtml += "<td class='gFontFamily gFontSize12'>";
    if (chartData[chartId]["others"] !== "undefined" && chartData[chartId]["others"] === "Y") {
        innerHtml += "" + translateLanguage.Enable_Others + " </td><td> <input type='checkbox' id='" + chartId + "_others' checked>";
    } else {
        innerHtml += "" + translateLanguage.Enable_Others + "</td><td> <input type='checkbox' id='" + chartId + "_others' >";
    }
    innerHtml += "</td>";

    innerHtml += "<td class='gFontFamily gFontSize12'>";
    if (typeof chartData[chartId]["hideBorder"] !== "undefined" && chartData[chartId]["hideBorder"] === "Y") {
        innerHtml += "" + translateLanguage.Chart_Border + " </td><td> <input type='checkbox' id='" + chartId + "hideBorder' checked>";
    } else {
        innerHtml += "" + translateLanguage.Chart_Border + " </td><td> <input type='checkbox' id='" + chartId + "hideBorder' >";
    }
    innerHtml += "</td>";
    innerHtml += "</tr>";
if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Combo-Horizontal" || chartType==="Combo-Aus-City" || chartType==="Combo-Aus-State" || chartType==="Combo-USCity-Map" || chartType==="Combo-US-Map"  || chartType==="Combo-IndiaCity" || chartType==="Combo-India-Map")){}else{              
    innerHtml += "<tr>";
    innerHtml += "<td style='font:white-space:nowrap'><label class='gFontFamily gFontSize12'>" + translateLanguage.Enable_ViewBY + "</label></td>";
    innerHtml += "<td><select id='" + chartId + "enableVieweby' style='font-size:9pt'  name='Dashname'>";
    if (typeof chartData[chartId]["enableVieweby"] === "undefined" || chartData[chartId]["enableVieweby"] === "Yes") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }

    if (chartData[chartId]["enableVieweby"] === "No") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    innerHtml += "</select></td>";

    innerHtml += "<td style='white-space:nowrap'><label class='gFontFamily gFontSize12'>" + translateLanguage.Enable_Measure + " </label></td>";
    innerHtml += "<td><select id='" + chartId + "enableMeasures' style='font-size:9pt'  name='Dashname'>";
    if (typeof chartData[chartId]["enableMeasures"] === "undefined" || chartData[chartId]["enableMeasures"] === "Yes") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }
    if (chartData[chartId]["enableMeasures"] === "No") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    innerHtml += "</select></td>";
    innerHtml += "</tr>";
}

    innerHtml += "<tr>";
    innerHtml += "<td class='gFontFamily gFontSize12'>";
    if (typeof chartData[chartId]["gtGraph"] !== "undefined" && chartData[chartId]["gtGraph"] === "Y") {
        innerHtml += "" + translateLanguage.Show_Grand_Total + " </td><td> <input type='checkbox' id='" + chartId + "gtGraph' checked>";
    }
    else {
        innerHtml += "" + translateLanguage.Show_Grand_Total + "  </td><td> <input type='checkbox' id='" + chartId + "gtGraph' >";
    }
    innerHtml += "</td>";
    
 if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType==="Combo-Horizontal" || chartType==="Combo-Aus-City" || chartType==="Combo-Aus-State" || chartType==="Combo-USCity-Map" || chartType==="Combo-US-Map" || chartType==="Combo-IndiaCity" || chartType==="Combo-India-Map")){}else{                 
    innerHtml += "<td class='gFontFamily gFontSize12'>";  // add bye mayank sh.
    if (typeof chartData[chartId]["hideDate"] === "undefined" || chartData[chartId]["hideDate"] === "Y") {
        innerHtml += "" + translateLanguage.Show_Time_Period + "</td><td> <input type='checkbox' id='" + chartId + "hideDate' checked>";
    } else {
        innerHtml += "" + translateLanguage.Show_Time_Period + "</td><td> <input type='checkbox' id='" + chartId + "hideDate' >";
    }
    innerHtml += "</td>";
 }
//    innerHtml += "<td class='gFontFamily gFontSize12'>";
//    if (typeof chartData[chartId]["multiselecctQuickFilter"] !== "undefined" && chartData[chartId]["multiselecctQuickFilter"] === "Yes") {
//        innerHtml += "" + translateLanguage.Multi_Select_In_Quick_Filter + "</td><td> <input type='checkbox' id='" + chartId + "multiselecctQuickFilter' checked>";
//    } else {
//        innerHtml += "" + translateLanguage.Multi_Select_In_Quick_Filter + " </td><td> <input type='checkbox' id='" + chartId + "multiselecctQuickFilter' >";
//    }
//    innerHtml += "</td>";
    innerHtml += "</tr>";

    innerHtml += "<tr>";
    innerHtml += "<td class='gFontFamily gFontSize12'>";
    if (chartData[chartId]["completeChartData"] !== "undefined" && chartData[chartId]["completeChartData"] === "Yes") {
        innerHtml += "" + translateLanguage.Chart_on_Complete_Data + " </td><td> <input type='checkbox' id='" + chartId + "_completeChartData' checked>";
    } else {
        innerHtml += "" + translateLanguage.Chart_on_Complete_Data + " </td><td> <input type='checkbox' id='" + chartId + "_completeChartData' >";
    }
    innerHtml += "</td>";

    innerHtml += "<td ><label class='gFontFamily gFontSize12' style='font:white-space:nowrap'> " + translateLanguage.Number_of_Records + "</label></td>";
    innerHtml += "<td>";
    if (chartData[chartId]["chartType"] === "US-State-Map") {
        var chartRecords = "30";
    } else {
        var chartRecords = "12";
    }
    if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "") {
        chartRecords = chartData[chartId]["records"];
    }
    innerHtml += "<input type='text' id='" + chartId + "Records' class='gFontFamily gFontSize12' size='6' value=" + chartRecords + ">";
    innerHtml += "</td>";
    innerHtml += "</tr>";


//    innerHtml += "<tr>";
//    innerHtml += "<td ><label class='gFontFamily gFontSize12' style='white-space:nowrap'>" + translateLanguage.No_Of_Quick_Filter_Buttons + " </label></td>";
//    innerHtml += "<td>";
//    var chartquickFilterLength = "5";
//    if (typeof chartData[chartId]["quickFilterLength"] !== "undefined" && chartData[chartId]["quickFilterLength"] !== "") {
//        chartquickFilterLength = chartData[chartId]["quickFilterLength"];
//    }
//    innerHtml += "<input type='text' id='" + chartId + "quickFilterLength' class='gFontFamily gFontSize12' size='6' value=" + chartquickFilterLength + ">";
//    innerHtml += "</td>";
//    innerHtml += "</tr>";
    //
    innerHtml += "<tr>";
    innerHtml += "<td class='gFontFamily gFontSize12'>";
    var divBackGround = "#ffffff";
    if (typeof chartData[chartId]["divBackGround"] !== "undefined" && chartData[chartId]["divBackGround"] != "") {
        divBackGround = chartData[chartId]["divBackGround"];
    }
    if (typeof chartData[chartId]["divBackGround"] !== "undefined" && chartData[chartId]["divBackGround"] != "") {
        innerHtml += "" + translateLanguage.Chart_Background_Color + " </td><td> <input style='background-color:" + divBackGround + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='divBackGround' readonly>";
    } else {
        innerHtml += "" + translateLanguage.Chart_Background_Color + " </td><td> <input style='background-color:" + divBackGround + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='divBackGround' readonly>";
    }
    innerHtml += "</td>";

    innerHtml += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Chart_Name_Font_Size + " </label></td>";
    innerHtml += "<td>";
    var headingFont = "12";
    if (typeof chartData[chartId]["headingFontSize"] !== "undefined" && chartData[chartId]["headingFontSize"] !== "") {
        headingFont = chartData[chartId]["headingFontSize"];
    }
    innerHtml += "<input type='text' id='" + chartId + "headingFontSize' class='gFontFamily gFontSize12' size='6' value=" + headingFont + ">";
    innerHtml += "</td>";
    innerHtml += "</tr>";

    innerHtml += "<tr>";
    innerHtml += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Chart_Font_Type + " </label></td>";
    innerHtml += "<td><select id='" + chartId + "chartFontType' style='font-size:9pt'  name='Dashname'>";
    if (typeof chartData[chartId]["chartFontType"] === "undefined" || chartData[chartId]["chartFontType"] === "Normal") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Normal' selected>Normal</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Normal'>Normal</option>";
    }
    if (chartData[chartId]["chartFontType"] === "Bold") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Bold' selected>Bold</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Bold'>Bold</option>";
    }

    if (chartData[chartId]["chartFontType"] === "Italic") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Italic' selected>Italic</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Italic'>Italic</option>";
    }
    innerHtml += "</select></td>";


    innerHtml += "<td style='white-space:nowrap'><label class='gFontFamily gFontSize12'>" + translateLanguage.Show_Chart_Icons + " </label></td>";
    innerHtml += "<td><select id='" + chartId + "iconShowHide' style='font-size:9pt'  name='Dashname'>";
    if (typeof chartData[chartId]["iconShowHide"] === "undefined" || chartData[chartId]["iconShowHide"] === "Yes") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
    }

    if (chartData[chartId]["iconShowHide"] === "No") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    innerHtml += "</select></td>";
    innerHtml += "</tr>";

    if (!(chartData[chartId]["chartType"] === "TopBottom-Chart")) {
        innerHtml += "<tr><td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Sort_Basis + " </label></td>";
        innerHtml += "<td>";
        innerHtml += "<select id='" + chartId + "sortBasis' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["sortBasis"] === "undefined" || chartData[chartId]["sortBasis"] === "Value") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Value' selected>Value</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Value'>Value</option>";
        }
        if (chartData[chartId]["sortBasis"] === "Alphabetic") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Alphabetic' selected>Alphabetic</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Alphabetic'>Alphabetic</option>";
        }
        innerHtml += "</select></td>";

        innerHtml += "<td '><label class='gFontFamily gFontSize12''> " + translateLanguage.Sort_Option + " </label></td>";
        innerHtml += "<td>";
        innerHtml += "<select id='" + chartId + "sorting' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["sorting"] === "undefined" || chartData[chartId]["sorting"] === "Descending") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Descending' selected>Descending</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Descending'>Descending</option>";
        }
        if (chartData[chartId]["sorting"] === "Ascending") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Ascending' selected>Ascending</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Ascending'>Ascending</option>";
        }
        innerHtml += "</select></td>";
        innerHtml += "</tr>";
    }

    innerHtml += "<tr>";
    innerHtml += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Date_on_Chart + " </label></td>";
        innerHtml += "<td><select id='" + chartId + "dateFlag_New' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["dateFlag_New"] === "undefined" || chartData[chartId]["dateFlag_New"] === "Normal") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal' selected>Normal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal'>Normal</option>";
        }
        if (chartData[chartId]["dateFlag_New"] === "Snap") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Snap' selected>Snap</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Snap'>Snap</option>";
        }
        innerHtml += "</select></td>";
        
        
innerHtml += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Custom_Time_on_Chart + " </label></td>";
innerHtml += "<td><select id='" + chartId + "customtimeType' style='font-size:9pt'  onchange='checkCoustomTimeDiv(this.id,\"" + chartId + "\")' name='Dashname'>";
if (typeof chartData[chartId]["customtimeType"] === "undefined" || chartData[chartId]["customtimeType"] === "No") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
    }
    if (chartData[chartId]["customtimeType"] === "Yes") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        var display1 = 'block';
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
       display1 = 'none';
    }
    innerHtml += "</select></td>";
    innerHtml += "</tr>";
    

    innerHtml += "<tr><td></td><td></td><td colspan=2>";
    innerHtml += "<div align='center' id='checkCoustomTime' style='display:" + display1 + ";width:80%;position: relative;width: 100%;border:1px solid;border-radius:5px ; margin-top:2%'>";
    
    innerHtml += "<table><tr style='width:100%;float:left'>";
    innerHtml += "<td class='gFontFamily gFontSize12' style=''>Custom Calender</td>";
    innerHtml += "</tr></table>";
    
    innerHtml += "<table>";
    innerHtml += "<tr>";
    var timeDate='';
    if (typeof chartData[chartId]["customTimeDate"] !== 'undefined' && chartData[chartId]["customTimeDate"] !== '') {
        timeDate = chartData[chartId]["customTimeDate"];
    }
    innerHtml += "<td id='custDate' style='display: block; cursor: pointer; width: 130px;'><input style='cursor:pointer' onclick='customDateApply(this.id,\"" + chartId + "\")' type='text' id='" + chartId + "customTimeDate' size='12' value='"+timeDate+"'>";
    innerHtml += "</td>&nbsp&nbsp";
        
    innerHtml += "<td><select id='" + chartId + "customTimeflag' class='gFontFamily gFontSize12'  name='Dashname'>";
     if (typeof chartData[chartId]["customTimeflag"] === "undefined" || chartData[chartId]["customTimeflag"]=== 'Month') {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Month' selected>Month</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Month'>Month</option>";
    }
    if (chartData[chartId]["customTimeflag"] === 'Qtr') {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Qtr' selected>Qtr</option>"
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Qtr'>Qtr</option>"
    }
    if (chartData[chartId]["customTimeflag"]=== 'Year') {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Year' selected>Year</option>"
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='Year'>Year</option>"
    }
    innerHtml += "</td>";

    innerHtml += "</tr>";
    innerHtml += "</table></br>";
    innerHtml += "</div>";
    innerHtml += "</td>";
    innerHtml += "</tr>";
    
    

    innerHtml += "<tr>";
    innerHtml += "<td ><label class='gFontFamily gFontSize12' > " + translateLanguage.Chart_Theme + " </label></td>";
    innerHtml += "<td><select id='" + chartId + "Pattern' style='font-size:9pt'  onchange='checkLogical(this.id,\"" + chartId + "\")' name='Dashname'>";
    if (chartType === "Table") {
        if (typeof chartData[chartId]["Pattern"] === "undefined" || chartData[chartId]["Pattern"] === "Normal") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal' selected>Normal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal'>Normal</option>";
        }
        if (chartData[chartId]["Pattern"] === "Gradient") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient' selected>Gradient</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient'>Gradient</option>";
        }
        if (chartData[chartId]["Pattern"] === "Logical") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical' selected>Logical</option>";
            var display = 'block';
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical'>Logical</option>";
            var display = 'none';
        }
    } else if (chartType === "Bubble" || chartType === "TopBottom-Chart" || chartType === "Tree-Map" || chartType === "India-Map" || chartType === "IndiaCity-Map" || chartType === "Australia-State-Map" || chartType === "Australia-City-Map" || chartType==="Combo-IndiaCity" || chartType === "Combo-India-Map" || chartType === "US-City-Map"
            || chartType === "US-State-Map" || chartType === "Combo-US-Map" || chartType === "Combo-USCity-Map" || chartType === "Combo-Aus-State" || chartType === "Combo-Aus-City" || chartType === "Telangana" || chartType === "Andhra-Pradesh") {
        if (typeof chartData[chartId]["Pattern"] === "undefined" || chartData[chartId]["Pattern"] === "Gradient") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient' selected>Gradient</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient'>Gradient</option>";
        }
        if (chartData[chartId]["Pattern"] === "single") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='single' selected>single</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='single'>single</option>";
        }

        if (chartData[chartId]["Pattern"] === "Multi") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Multi' selected>Multi</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Multi'>Multi</option>";
        }
        if (chartData[chartId]["Pattern"] === "Logical") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical' selected>Logical</option>";
            var display = 'block';
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical'>Logical</option>";
            var display = 'none';
        }
    } else {

        if (typeof chartData[chartId]["Pattern"] === "undefined" || chartData[chartId]["Pattern"] === "Multi") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Multi' selected>Multi</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Multi'>Multi</option>";
        }
        if (chartData[chartId]["Pattern"] === "single") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='single' selected>single</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='single'>single</option>";
        }
        if (chartData[chartId]["Pattern"] === "Gradient") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient' selected>Gradient</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Gradient'>Gradient</option>";
        }

        if (chartData[chartId]["Pattern"] === "Logical") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical' selected>Logical</option>";
            var display = 'block';
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Logical'>Logical</option>";
            var display = 'none';
        }
    }
    innerHtml += "</select></td>";
    if (chartData[chartId]["chartType"] === "Bubble" || chartData[chartId]["chartType"] === "Tree-Map") {
        innerHtml += "<td ><label class='gFontFamily gFontSize12' > " + translateLanguage.Transpose + " </label></td>";
        innerHtml += "<td><select id='" + chartId + "dataTranspose' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["dataTranspose"] === "undefined" || chartData[chartId]["dataTranspose"] === "No") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        if (chartData[chartId]["dataTranspose"] === "Yes") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
        innerHtml += "</select></td>";
    }
    if (chartData[chartId]["chartType"] === "StackedBarLine" || chartData[chartId]["chartType"] === "Vertical-Bar" || chartData[chartId]["chartType"] == "DualAxis-Bar"|| chartData[chartId]["chartType"] == "Veraction-Combo3" || chartData[chartId]["chartType"] == "Influencers-Impact-Analysis2" || chartData[chartId]["chartType"] === "Vertical-Negative-Bar" || chartData[chartId]["chartType"] === "StackedBar" || chartData[chartId]["chartType"] === "StackedBar-%" || chartData[chartId]["chartType"] === "OverLaid-Bar-Line"
            || chartData[chartId]["chartType"] === "MultiMeasure-Bar" || chartData[chartId]["chartType"] === "MultiMeasureH-Bar" || chartData[chartId]["chartType"] === "Cumulative-Bar" || chartData[chartId]["chartType"] === "Multi-Layered-Bar" || chartData[chartId]["chartType"] === "GroupedStackedH-Bar" || chartData[chartId]["chartType"] === "GroupedStacked-Bar" || chartData[chartId]["chartType"] === "GroupedStacked-Bar%") {
        innerHtml += "<td style='font:white-space:nowrap'><label class='gFontFamily gFontSize12'>" + translateLanguage.Bar_Type + "</label></td>";
        innerHtml += "<td><select id='" + chartId + "barsize' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["barsize"] === "undefined" || chartData[chartId]["barsize"] === "Normal") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal' selected>Normal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal'>Normal</option>";
        }
        if (chartData[chartId]["barsize"] === "Thin") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Thin' selected>Thin</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Thin'>Thin </option>";
        }
        if (chartData[chartId]["barsize"] === "ExtraThin") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='ExtraThin' selected>Extra Thin</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='ExtraThin'>Extra Thin</option>";
        }
        innerHtml += "</select></td>";
    }

    if (chartData[chartId]["chartType"] == "Table" || chartData[chartId]["chartType"] == "Bar-Table") {
        innerHtml += "<td class='gFontFamily gFontSize12'>";
        if (typeof chartData[chartId]["tableBorder"] !== "undefined" && chartData[chartId]["tableBorder"] === "Y") {
            innerHtml += "" + translateLanguage.Table_Border + " </td><td> <input type='checkbox' id='" + chartId + "tableBorder' checked>";
        } else {
            innerHtml += "" + translateLanguage.Table_Border + " </td><td> <input type='checkbox' id='" + chartId + "tableBorder' >";
        }
        innerHtml += "</td>";
    }
    if (chartData[chartId]["chartType"] == "Grouped-Bar") {
//    innerHtml += "<td style='font:white-space:nowrap'><label class='gFontFamily gFontSize12'>"+translateLanguage.Grouped_Bar_With+"</label></td>";
        innerHtml += "<td style='font:white-space:nowrap'><label class='gFontFamily gFontSize12'>GroupedBar With</label></td>";
        innerHtml += "<td><select id='" + chartId + "groupedBarWith' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["groupedBarWith"] === "undefined" || chartData[chartId]["groupedBarWith"]["Normal"] === "Normal") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal' selected>Normal</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Normal'>Normal</option>";
        }
        if (typeof chartData[chartId]["groupedBarWith"] !== "undefined" && chartData[chartId]["groupedBarWith"]["Over Layered Line"] === "Over Layered Line") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Over Layered Line' selected>Over Layered Line</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Over Layered Line'>Over Layered Line </option>";
        }
        if (typeof chartData[chartId]["groupedBarWith"] !== "undefined" && chartData[chartId]["groupedBarWith"]["Dual Axis"] === "Dual Axis") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Dual Axis' selected>Dual Axis</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Dual Axis'>Dual Axis</option>";
        }
        if (typeof chartData[chartId]["groupedBarWith"] !== "undefined" && chartData[chartId]["groupedBarWith"]["GT"] === "GT") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='GT' selected>GT</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='GT'>GT</option>";
        }
        innerHtml += "</select></td>";
    }
    innerHtml += "</tr>";

    if (!(chartData[chartId]["chartType"] === "TopBottom-Chart")) {
        innerHtml += "<tr>";
        innerHtml += "<td '><label class='gFontFamily gFontSize12''>" + translateLanguage.Exclude_from_Drill + " </label></td>";
        innerHtml += "<td><select id='" + chartId + "excludeFromDrill' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["excludeFromDrill"] === "undefined" || chartData[chartId]["excludeFromDrill"] === "N") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='N' selected>No</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='N'>No</option>";
        }
        if (typeof chartData[chartId]["excludeFromDrill"] !== "undefined" && chartData[chartId]["excludeFromDrill"] === "Y") {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Y' selected>Yes</option>";
        } else {
            innerHtml += "<option class='gFontFamily gFontSize12' value='Y'>Yes</option>";
        }
        innerHtml += "</select></td>";

        if (chartData[chartId]["chartType"] == "Table" || chartData[chartId]["chartType"] == "Horizontal-Bar" || chartData[chartId]["chartType"] == "Combo-Horizontal") {
            innerHtml += "<td class='gFontFamily gFontSize12'>";
            var label = '';
            if (chartData[chartId]["chartType"] == "Table") {
                label = translateLanguage.Table_With_Hyphen;
            }
            else if (chartData[chartId]["chartType"] == "Horizontal-Bar" || chartData[chartId]["chartType"] == "Combo-Horizontal") {
                label = translateLanguage.With_Hyphen;
            }
            if (typeof chartData[chartId]["tableWithSymbol"] !== "undefined" && chartData[chartId]["tableWithSymbol"] === "Y") {
                innerHtml += "" + label + " </td><td> <input type='checkbox' id='" + chartId + "tableWithSymbol' checked>";
            } else {
                innerHtml += "" + label + " </td><td> <input type='checkbox' id='" + chartId + "tableWithSymbol' >";
            }
            innerHtml += "</td>";
        }
        if (chartData[chartId]["chartType"] == "Trend-Combo") {
            innerHtml += "<td '><label class='gFontFamily gFontSize12''>" + translateLanguage.Show_ViewBys_Measures + " </label></td>";
            innerHtml += "<td><select id='" + chartId + "trendViewMeasures' style='font-size:9pt'  name='Dashname'>";
            if (typeof chartData[chartId]["trendViewMeasures"] === "undefined" || chartData[chartId]["trendViewMeasures"] === "Measures") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='Measures' selected>Measures</option>";
            } else {
                innerHtml += "<option class='gFontFamily gFontSize12' value='Measures'>Measures</option>";
            }
            if (typeof chartData[chartId]["trendViewMeasures"] !== "undefined" && chartData[chartId]["trendViewMeasures"] === "ViewBys") {
                innerHtml += "<option class='gFontFamily gFontSize12' value='ViewBys' selected>ViewBys</option>";
            } else {
                innerHtml += "<option class='gFontFamily gFontSize12' value='ViewBys'>ViewBys</option>";
            }
            innerHtml += "</select></td>";
        }
        innerHtml += "</tr>";
        innerHtml += "<tr>";
        innerHtml += "<td class='gFontFamily gFontSize12'>";
        if (typeof chartData[chartId]["absoluteValue"] !== "undefined" && chartData[chartId]["absoluteValue"] === "Yes") {
            innerHtml += "Show Absolute Data </td><td> <input type='checkbox' id='" + chartId + "absoluteValue' checked>";
        }
        else {
            innerHtml += "Show Absolute Data </td><td> <input type='checkbox' id='" + chartId + "absoluteValue' >";
        }
        innerHtml += "</td>";
        innerHtml += "</tr>";
        innerHtml += "<tr>";
        innerHtml += "<td class='gFontFamily gFontSize12'>";

        if (chartData[chartId]["timeEnable"] !== "undefined" && chartData[chartId]["timeEnable"] === "Yes") {
            innerHtml += "Chart on Time Based</td><td> <input type='checkbox' id='" + chartId + "_timeEnable' class='gFontFamily gFontSize12' checked>";
        } else {
            innerHtml += "Chart on Time Based </td><td> <input type='checkbox' id='" + chartId + "_timeEnable' class='gFontFamily gFontSize12'>";
        }
        innerHtml += "</td>";
        var timeList = ["3Month", "6Month", "1Year", "2Year"];
//    var timeListValue = ["3Month(","6Month(","1Year(","2Year("];
        innerHtml += "<td><select id='" + chartId + "timeBasedData'  class='gFontFamily gFontSize12'  name='fontname'>";
        if (typeof chartData[chartId]["timeBasedData"] === 'undefined' || chartData[chartId]["timeBasedData"] === '') {
            innerHtml += "<option value='' class='gFontFamily gFontSize12' selected> Select </option>";
        }
        else {
            innerHtml += "<option value='' class='gFontFamily gFontSize12'> Select </option>";
        }
        for (var k = 0; k < timeList.length; k++)
        {
            if (typeof chartData[chartId]["timeBasedData"] !== 'undefined' && typeof chartData[chartId]["PrefixfontsizeList"] !== 'undefined') {
                if (chartData[chartId]["timeBasedData"][k] === timeList[k]) {
                    innerHtml += "<option value=" + timeList[k] + " class='gFontFamily gFontSize12' selected> " + timeList[k] + " </option>";
                }
                else {
                    innerHtml += "<option value=" + timeList[k] + " class='gFontFamily gFontSize12'> " + timeList[k] + " </option>";
                }
            }
            else {
                innerHtml += "<option value=" + timeList[k] + " class='gFontFamily gFontSize12'> " + timeList[k] + " </option>";
            }
        }
        innerHtml += "</select>";
        innerHtml += "</td>";
        innerHtml += "</tr>";
        
    }
    innerHtml += "<td class='gFontFamily gFontSize12'>";
    var chartNameColol = "#333";
    if (typeof chartData[chartId]["chartNameColol"] !== "undefined" && chartData[chartId]["chartNameColol"] != "") {
        chartNameColol = chartData[chartId]["chartNameColol"];
    }
    if (typeof chartData[chartId]["chartNameColol"] !== "undefined" && chartData[chartId]["chartNameColol"] != "") {
        innerHtml += "" + translateLanguage.Chart_Name_Color + " </td><td> <input style='background-color:" + chartNameColol + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='chartNameColol' readonly>";
    } else {
        innerHtml += "" + translateLanguage.Chart_Name_Color + " </td><td> <input style='background-color:" + chartNameColol + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='chartNameColol' readonly>";
    }
    innerHtml += "</td>";
    
    if (chartData[chartId]["chartType"] === "Multi-View-Tree") {
        innerHtml += "<tr>";
        innerHtml += "<td class='gFontFamily gFontSize12'>";

        if (chartData[chartId]["equalInterval"] !== "undefined" && chartData[chartId]["equalInterval"] === "Yes") {
            innerHtml += "Chart on Equal Area</td><td> <input type='checkbox' id='" + chartId + "_equalInterval' class='gFontFamily gFontSize12' checked>";
        } else {
            innerHtml += "Chart on Equal Area </td><td> <input type='checkbox' id='" + chartId + "_equalInterval' class='gFontFamily gFontSize12'>";
        }
        innerHtml += "</td>";
        innerHtml += "</tr>";

    }
    if (chartData[chartId]["chartType"] === "World-Top-Analysis" || chartData[chartId]["chartType"] === "World-City" || chartData[chartId]["chartType"] === "world-map") {
        innerHtml += "<tr><td class='gFontFamily gFontSize12'>";
        var FilledColor1 = "#b86e00";
        if (typeof chartData[chartId]["FilledColor1"] !== "undefined" && chartData[chartId]["FilledColor1"] != "") {
            FilledColor1 = chartData[chartId]["FilledColor1"];
        }
        if (typeof chartData[chartId]["FilledColor1"] !== "undefined" && chartData[chartId]["FilledColor1"] != "") {
            innerHtml += "" + translateLanguage.Color_Range_High + " </td><td> <input style='background-color:" + FilledColor1 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor1'>";
        } else {
            innerHtml += "" + translateLanguage.Color_Range_High + " </td><td> <input style='background-color:" + FilledColor1 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor1' >";
        }
        innerHtml += "</td>";


        innerHtml += "<td class='gFontFamily gFontSize12'>";
        var FilledColor2 = "#ffbf00";
        if (typeof chartData[chartId]["FilledColor2"] !== "undefined" && chartData[chartId]["FilledColor2"] != "") {
            FilledColor2 = chartData[chartId]["FilledColor2"];
        }
        if (typeof chartData[chartId]["FilledColor2"] !== "undefined" && chartData[chartId]["FilledColor2"] != "") {
            innerHtml += "" + translateLanguage.Color_Range_Low + " </td><td> <input style='background-color:" + FilledColor2 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor2'>";
        } else {
            innerHtml += "" + translateLanguage.Color_Range_Low + "</td><td> <input style='background-color:" + FilledColor2 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor2' >";
        }
        innerHtml += "</td>";


        innerHtml += "<td class='gFontFamily gFontSize12'>";
        var FilledColor3 = "#D0D0D0";
        if (typeof chartData[chartId]["FilledColor3"] !== "undefined" && chartData[chartId]["FilledColor3"] != "") {
            FilledColor3 = chartData[chartId]["FilledColor3"];
        }
        if (typeof chartData[chartId]["FilledColor3"] !== "undefined" && chartData[chartId]["FilledColor3"] != "") {
            innerHtml += "" + translateLanguage.Fill_Empty_Country_Color + " </td><td> <input style='background-color:" + FilledColor3 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor3'>";
        } else {
            innerHtml += "" + translateLanguage.Fill_Empty_Country_Color + " </td><td> <input style='background-color:" + FilledColor3 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor3' >";
        }
        innerHtml += "</td>";

        innerHtml += "<td class='gFontFamily gFontSize12'>";
        var FilledColor4 = "white";
        if (typeof chartData[chartId]["FilledColor4"] !== "undefined" && chartData[chartId]["FilledColor4"] != "") {
            FilledColor4 = chartData[chartId]["FilledColor4"];
        }
        if (typeof chartData[chartId]["FilledColor4"] !== "undefined" && chartData[chartId]["FilledColor4"] != "") {
            innerHtml += "" + translateLanguage.Ocean_Color + " </td><td> <input style='background-color:" + FilledColor4 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor4'>";
        } else {
            innerHtml += "" + translateLanguage.Ocean_Color + " </td><td> <input style='background-color:" + FilledColor4 + "' type='button' value='      ' onclick='moreColors(\"" + chartId + "\",this.id)' id='FilledColor4' >";
        }
        innerHtml += "</td>";

        innerHtml += "</tr>";
    }

    //    innerHtml += "<td style='font: 11px verdana;font-size:9pt;white-space:nowrap'><label style='font: 11px verdana;font-size:9pt'> Tool Tip Format </label></td>";
    //    innerHtml += "<td><select id='"+chartId+"toolTip' style='font-size:9pt'  name='Dashname'>";
    //    if (typeof chartData[chartId]["toolTip"] === "undefined" || chartData[chartId]["toolTip"] === "Absolute") {
    //        innerHtml += "<option value='Absolute' selected>Absolute</option>";
    //    } else {
    //        innerHtml += "<option value='Absolute'>Absolute</option>";
    //        }
    //    if (chartData[chartId]["toolTip"] === "Formated") {
    //        innerHtml += "<option value='Formated' selected>Formated</option>";
    //    } else {
    //        innerHtml += "<option value='Formated'>Formated</option>";
    //    }

    var chartType = chartData[chartId]["chartType"];
    var logicalHighcolor = "";
    var logicalMidcolor = "";
    var logicalLowcolor = "";
    if (typeof chartData[chartId]["logicalParameters"] != 'undefined' && chartData[chartId]["logicalParameters"][0]["logicalHighcolor"] != '') {
        logicalHighcolor = chartData[chartId]["logicalParameters"][0]["logicalHighcolor"];
    } else {
        logicalHighcolor = "red";
    }
    if (typeof chartData[chartId]["logicalParameters"] != 'undefined' && chartData[chartId]["logicalParameters"][1]["logicalMidcolor"] != '') {
        logicalMidcolor = chartData[chartId]["logicalParameters"][1]["logicalMidcolor"];
    } else {
        logicalMidcolor = "yellow";
    }
    if (typeof chartData[chartId]["logicalParameters"] != 'undefined' && chartData[chartId]["logicalParameters"][2]["logicalLowcolor"] != '') {
        logicalLowcolor = chartData[chartId]["logicalParameters"][2]["logicalLowcolor"];
    } else {
        logicalLowcolor = "green";
    }
    var HighMax, MidMax, LowMax, HighMin, MidMin, LowMin;

    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][0]["HighMax"] !== '') {
        HighMax = chartData[chartId]["logicalParameters"][0]["HighMax"];
    } else {
        HighMax = "";
    }
    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][1]["MidMax"] !== '') {
        MidMax = chartData[chartId]["logicalParameters"][1]["MidMax"];
    } else {
        MidMax = "";
    }
    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][2]["LowMax"] !== '') {
        LowMax = chartData[chartId]["logicalParameters"][2]["LowMax"];
    } else {
        LowMax = "";
    }


    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][0]["HighMin"] !== '') {
        HighMin = chartData[chartId]["logicalParameters"][0]["HighMin"];
    } else {
        HighMin = "";
    }
    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][1]["MidMin"] !== '') {
        MidMin = chartData[chartId]["logicalParameters"][1]["MidMin"];
    } else {
        MidMin = "";
    }
    if (typeof chartData[chartId]["logicalParameters"] !== 'undefined' && chartData[chartId]["logicalParameters"][2]["LowMin"] !== '') {
        LowMin = chartData[chartId]["logicalParameters"][2]["LowMin"];
    } else {
        LowMin = "";
    }

    innerHtml += "<tr><td colspan=2>";
    innerHtml += "<div align='center' id='DefinelogicalDiv' style='display:" + display + ";width:80%;position: relative;width: 100%;border:1px solid;border-radius:5px ; margin-top:2%'>";
    innerHtml += "<table  style=''>";

    innerHtml += "<tr style='width:100%;float:left'>";
    innerHtml += "<td class='gFontFamily gFontSize12' style=''>Logical Colors</td>";
    innerHtml += "</tr>";
    innerHtml += "</table>";

    innerHtml += "<table style='margin:10px'>";
    innerHtml += "<tr>";
    innerHtml += "<td><input id='logicalHighcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"logicalHighcolor\")' style='width:40px;background-color:" + logicalHighcolor + "' ></td>";
    innerHtml += "<td><select id='" + chartId + "condition' class='gFontFamily gFontSize12'  name='Dashname' onchange='condition(this.id,\"" + chartId + "\")'>";
    if (typeof chartData[chartId]["logicalParameters"] === "undefined" || chartData[chartId]["logicalParameters"][0]["condition"] === "<") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<' selected><</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<'><</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][0]["condition"] === ">") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>' selected>></option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>'>></option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][0]["condition"] === "<=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<=' selected><=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<='><=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][0]["condition"] === ">=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>=' selected>>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>='>>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][0]["condition"] === "=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='=' selected>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='='>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][0]["condition"] === "<>") {
        innerHtml += "<option class='gFontFamily gFontSize12'  value='<>' selected><></option>";
        var highval = "block";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12'   value='<>'><></option>";
        var highval = "none";
    }
    innerHtml += "</td>";
    innerHtml += "<td><input id='" + chartId + "HighMax'  style='display:block;width:100%' type='text' value='" + HighMax + "' ></td>";
    innerHtml += "<td><input id='" + chartId + "HighMin'  style='display:" + highval + ";width:100%' type='text' value='" + HighMin + "' ></td>";
    innerHtml += "</tr>";

    innerHtml += "<tr>";
    innerHtml += "<td><input id='logicalMidcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"logicalMidcolor\")' style='width:40px;background-color:" + logicalMidcolor + "' ></td>";
    innerHtml += "<td><select id='" + chartId + "condition1' class='gFontFamily gFontSize12'  name='Dashname' onchange='condition1(this.id,\"" + chartId + "\")'>";
    if (typeof chartData[chartId]["logicalParameters"] === "undefined" || chartData[chartId]["logicalParameters"][1]["condition1"] === "<") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<' selected><</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<'><</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][1]["condition1"] === ">") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>' selected>></option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>'>></option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][1]["condition1"] === "<=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<=' selected><=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<='><=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][1]["condition1"] === ">=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>=' selected>>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>='>>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][1]["condition1"] === "=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='=' selected>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='='>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][1]["condition1"] === "<>") {
        innerHtml += "<option class='gFontFamily gFontSize12'  value='<>' selected><></option>";
        var midval = "block";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12'  value='<>'><></option>";
        var midval = "none";
    }
    innerHtml += "</td>";
    innerHtml += "<td><input id='" + chartId + "MidMax'   style='display:block;width:100%' type='text' value='" + MidMax + "' ></td>";
    innerHtml += "<td><input id='" + chartId + "MidMin'   style='display:" + midval + ";width:100%' type='text' value='" + MidMin + "' ></td>";
    innerHtml += "</tr>";

    innerHtml += "<tr>";
    innerHtml += "<td><input id='logicalLowcolor' type='color picker' onclick='moreColors(\"//" + chartId + "\",\"logicalLowcolor\")' style='width:40px;background-color:" + logicalLowcolor + "' ></td>";
    innerHtml += "<td><select id='" + chartId + "condition2' class='gFontFamily gFontSize12'  onchange='condition2(this.id,\"" + chartId + "\")'  name='Dashname'>";
    if (typeof chartData[chartId]["logicalParameters"] === "undefined" || chartData[chartId]["logicalParameters"][2]["condition2"] === "<") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<' selected><</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<'><</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][2]["condition2"] === ">") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>' selected>></option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>'>></option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][2]["condition2"] === "<=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<=' selected><=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<='><=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][2]["condition2"] === ">=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>=' selected>>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='>='>>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][2]["condition2"] === "=") {
        innerHtml += "<option class='gFontFamily gFontSize12' value='=' selected>=</option>";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='='>=</option>";
    }
    if (typeof chartData[chartId]["logicalParameters"] != "undefined" && chartData[chartId]["logicalParameters"][2]["condition2"] === "<>") {
        innerHtml += "<option class='gFontFamily gFontSize12'  value='<>' selected ><></option>";
        var lowval = "block";
    } else {
        innerHtml += "<option class='gFontFamily gFontSize12' value='<>'><></option>";
        var lowval = "none";
    }
    innerHtml += "</td>";
    innerHtml += "<td><input id='" + chartId + "LowMax'   style='display:block;width:100%' type='text' value='" + LowMax + "' ></td>";
    innerHtml += "<td><input id='" + chartId + "LowMin'   style='display:" + lowval + ";width:100%' type='text' value='" + LowMin + "' ></td>";
    innerHtml += "</tr>";

    innerHtml += "</table>";
    innerHtml += "</div>"
    innerHtml += "</td>";
    innerHtml += "</tr>";

    // end Logical Color



    var chartType = chartData[chartId]["chartType"];
    var noOfRecords = chartData[chartId]["records"] > 25 ? 25 : chartData[chartId]["records"];
    if (typeof noOfRecords === "undefined" || noOfRecords === "") {
        noOfRecords = "12";
    }//Edit by shivam
    if (!(chartType === "Grouped-Line" || chartType === "world-map" || chartType === "World-City" || chartType == "Emoji-Chart" || chartType === "Transpose-Table" || chartType === "Dial-Gauge" || chartType === "Radial-Chart" || chartType === "LiquidFilled-KPI" || chartType === "Standard-KPI" || chartType === "Standard-KPI1" || chartType === "KPIDash" || chartType === "Bullet-Horizontal" || chartType === "Area" || chartType === "KPI-Table" || chartType === "Expression-Table" || chartType === "World-Top-Analysis"))
    {
        innerHtml += "<tr>";
        innerHtml += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Select_Top_Colors + "</td>";
        innerHtml += "<td colspan=3>";
        var topColors = d3.scale.category10();
        var colorArray = [];
        for (var j = 0; j < noOfRecords; j++)
        {
            colorArray[j] = topColors(j);
        }
        for (var j = 0; j < noOfRecords; j++)
        {
            var color;
            if (!(typeof chartData[chartId]["customColors"] === 'undefined' || typeof chartData[chartId]["customColors"]["color" + (j + 1)] === 'undefined'))
            {
                color = chartData[chartId]["customColors"]["color" + (j + 1)];
            }
            else
            {
                color = colorArray[j];
            }
            var border = '';
            if ((chartData[chartId]["chartType"] == "OverLaid-Bar-Line" || chartData[chartId]["chartType"] == "StackedBarLine" || chartData[chartId]["chartType"] == "DualAxis-Bar"|| chartData[chartId]["chartType"] == "Veraction-Combo3" || chartData[chartId]["chartType"] === "DualAxis-Target" || chartData[chartId]["chartType"] === "DualAxis-Group") && j == 6) {
                border = "border:2px solid #00008B";
            }
            if (j % 10 == 0)
            {

                innerHtml += "<input id='color" + (j + 1) + "' type='color picker' onclick='pickColor(\"" + chartId + "\",this.id,\"" + j + "\")' style='" + border + ";width:30px;background-color:" + color + "' >";
            }
            else
            {
                innerHtml += "<input id='color" + (j + 1) + "' type='color picker' onclick='pickColor(\"" + chartId + "\",this.id,\"" + j + "\")' style='" + border + ";margin-left:7px;width:30px;background-color:" + color + "'>";
            }
            if ((j + 1) % 10 == 0 && j != 0) {
                innerHtml += "</td>";
                innerHtml += "</tr>";
                innerHtml += "<tr>";
                innerHtml += "<td></td>";
                innerHtml += "<td colspan=3>";
            }
        }
        innerHtml += "</td>";
        innerHtml += "</tr>";
    }

    innerHtml += "</table></div>";   //  html 0
    //-------------------------
    // add new property by mynk sh.
    var kpiProp = "";
    var chartData = JSON.parse($("#chartData").val());
    kpiProp += "<div id='kpiProperties' class='tag-link-properties' style='width:100%; display:none;  margin-left: 1%;'>";
    kpiProp += "<table  id='kpiProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";
    //Added By Ram for LiquidFilledGauge  kpiiii

    var chartType = chartData[chartId]["chartType"];
    if (chartData[chartId]["chartType"] == "Radial-Chart" || chartData[chartId]["chartType"] == "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1" || chartData[chartId]["chartType"] == "Veraction-Combo" || chartData[chartId]["chartType"] == "KPIDash" || chartData[chartId]["chartType"] == "Emoji-Chart" || chartData[chartId]["chartType"] == "LiquidFilled-KPI" || chartData[chartId]["chartType"] == "Emoji-Chart" || chartData[chartId]["chartType"] === "Dial-Gauge") {
        kpiProp += "<tr><td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Sub_Total + " </label></td>";
        kpiProp += "<td>";
        kpiProp += "<select id='" + chartId + "SubTotalProp' class='gFontFamily gFontSize12' onchange='subTotalTarget(\"" + chartId + "\",this.value)'  name='Dashname'>";
        if (typeof chartData[chartId]["SubTotalProp"] === "undefined" || chartData[chartId]["SubTotalProp"] === "Disable") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='Disable' selected>Disable</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='Disable'>Disable</option>";
        }
        if (chartData[chartId]["SubTotalProp"] === "Enable") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='Enable' selected>Enable</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='Enable'>Enable</option>";
        }
        kpiProp += "</select></td>";
        kpiProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Max_Range + "</td>";
        if (typeof chartData[chartId]["MaxRange"] !== "undefined" && chartData[chartId]["MaxRange"] != "") {
            kpiProp += "<td><input type='text' style='width:40%' value ='" + chartData[chartId]["MaxRange"] + "' id ='" + chartId + "MaxRange' class='gFontFamily gFontSize12'></td>";
        } else {
            kpiProp += "<td><input type='text' style='width:40%' id ='" + chartId + "MaxRange' class='gFontFamily gFontSize12'></td>";
        }
        if (typeof chartData[chartId]["SubTotalProp"] != "undefined" && chartData[chartId]["SubTotalProp"] === "Enable") {
            kpiProp += "<tr style='display:' id = '" + chartId + "targetLane'>";
        } else {
            kpiProp += "<tr style='display:none' id = '" + chartId + "targetLane'>";
        }
        kpiProp += "<td class='gFontFamily gFontSize12'>";
        if (typeof chartData[chartId]["Target"] != "undefined" && chartData[chartId]["Target"] != "") {
            kpiProp += "Target </td><td> <input type='textbox'  id='" + chartId + "Target' class='gFontFamily gFontSize12' value='" + chartData[chartId]["Target"] + "'>";
        }
        else {
            kpiProp += "Target </td><td> <input type='textbox' id='" + chartId + "Target' class='gFontFamily gFontSize12' >";
        }
        kpiProp += "</td>";
        kpiProp += "</tr>";
    }

    if (typeof chartData[chartId]["chartType"] !== "undefined" && chartData[chartId]["chartType"] === 'Emoji-Chart') {
        kpiProp += "<tr>";
        kpiProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Select_image_type + "</td>";
        kpiProp += "<td><select id ='" + chartId + "imageType'>";
        if (typeof chartData[chartId]["imageType"] === "undefined" || chartData[chartId]["imageType"] === "emoji") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='emoji' selected>Emojis</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='emoji'>Emojis</option>";
        }
        if (chartData[chartId]["imageType"] === "lights") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='lights' selected>Traffic lights</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='lights'>Traffic lights</option>";
        }
        kpiProp += "</td>";
        kpiProp += "<td class='gFontFamily gFontSize12' >" + translateLanguage.Emoji_position + "</td>";
        kpiProp += "<td><select id ='" + chartId + "emojiPosition'>";
        if (typeof chartData[chartId]["emojiPosition"] === "undefined" || chartData[chartId]["emojiPosition"] === "top") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='top' selected>Top</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='top'>Top</option>";
        }
        if (typeof chartData[chartId]["emojiPosition"] !== "undefined" && chartData[chartId]["emojiPosition"] === "left") {
            kpiProp += "<option class='gFontFamily gFontSize12' value='left' selected>Left</option>";
        } else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='left'>Left</option>";
        }
        kpiProp += "</td>";
        kpiProp += "</tr>";
    }

    // gauge chart prop
    if (chartData[chartId]["chartType"] === 'Dial-Gauge' || chartData[chartId]["chartType"] === 'Emoji-Chart') {
        var label;
        if (chartData[chartId]["chartType"] === 'Dial-Gauge')
        {
            label = translateLanguage.Define_Dial_chart;
        }
        else {
            label = translateLanguage.Define_Emoji_chart;
        }
        kpiProp += "<td colspan='2' ><label style='white-space:nowrap' class='gFontFamily gFontSize12' for='DefineDialChart'>" + label + "</label>&nbsp;&nbsp;&nbsp;&nbsp;";
        kpiProp += " <select id='dialSelectChart' onchange='checkDial(this.id,\"" + chartId + "\")'>";
        var display = 'none';
        if (typeof chartData[chartId]["dialSelectChart"] !== 'undefined' && chartData[chartId]["dialSelectChart"] !== '' && chartData[chartId]["dialSelectChart"] === 'yes') {
            kpiProp += "<option class='gFontFamily gFontSize12' value='yes' selected>Yes</option> ";
            kpiProp += "<option class='gFontFamily gFontSize12' value='no'>No</option> ";
            display = 'block';
        }
        else {
            kpiProp += "<option class='gFontFamily gFontSize12' value='yes'>Yes</option>";
            kpiProp += "<option class='gFontFamily gFontSize12' value='no' selected>No</option>";
        }

        kpiProp += "</select>";
        kpiProp += "</td>";
        kpiProp += "</tr>";
        kpiProp += "<tr>";
        kpiProp += "<td colspan='2'>";
        kpiProp += "</td>";
        kpiProp += "<td colspan='2'>";
        kpiProp += "<div id='DefineDialChartSection' style='display:" + display + ";position: relative;width: 100%;border:1px solid;border-radius:5px ;border-color:#79C9EC; margin-top:2%'>";
        kpiProp += "<table border='0' width='100%'>";
        kpiProp += "<tr>";
        kpiProp += "<td><div id='upperDialDivSection' style='border-bottom: 1px dashed;border-color:#79C9EC;'>";
        kpiProp += "<table>";
        kpiProp += "<td align='left' ><span id='dialMeasureNameSpan' class='dialMeasureName' class='gFontFamily gFontSize12'></span> &nbsp;&nbsp;&nbsp;&nbsp;<input type='text' id='dialMeasureValue' readOnly style='width:90px'/></span>";
        if (typeof chartData[chartId]["dialMeasureSuffix"] != "undefined" && chartData[chartId]["dialMeasureSuffix"] != "") {
            kpiProp += "<span id='dialMeasureSuffixLabel' class='dialMeasureSuffixLabel'>" + translateLanguage.Suffix + "</span> &nbsp;&nbsp;<input type='text' id='" + chartId + "dialMeasureSuffix' value='" + chartData[chartId]["dialMeasureSuffix"] + "' style='width:40px'/>";
        }
        else {
            kpiProp += "<span id='dialMeasureSuffixLabel' class='dialMeasureSuffixLabel' class='gFontFamily gFontSize12'>" + translateLanguage.Suffix + "</span> &nbsp;&nbsp;<input type='text' id='" + chartId + "dialMeasureSuffix' style='width:40px'/>";
        }
        if (chartType === 'Dial-Gauge') {
            kpiProp += "<tr style='margin-top:10px;margin-bottom:10px;'>";
            kpiProp += "<td>";
            var dialType = 'Standard';
            if (typeof chartData[chartId]["dialType"] === 'undefined' || chartData[chartId]["dialType"] === 'std') {
                dialType = "Standard";
            }
            else {
                dialType = chartData[chartId]["dialType"];
            }
            kpiProp += "<label class='gFontFamily gFontSize12'>" + translateLanguage.Dial_Type + " </label><select id ='" + chartId + "_dialType' onchange='changeConditionColors(\"" + chartId + "\")'>";
            if (dialType == 'Standard') {
                kpiProp += "<option class='gFontFamily gFontSize12' value='std' selected>Standard</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='nonstd'>Non-Standard</option>";
            }
            else {
                kpiProp += "<option class='gFontFamily gFontSize12' value='std'>Standard</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='nonstd' selected>Non-Standard</option>";
            }
            kpiProp += "</select>";
            kpiProp += "</td>";
            kpiProp += "</tr>";
        }
        kpiProp += "</tr>";
        kpiProp += "<td align='center'>";
        kpiProp += "<div id='deviationDiv' style='display: none'>";
        kpiProp += "<table><tr>";
        kpiProp += "<td id='dialTargetTd' align='left' style='' class='gFontFamily gFontSize12'> " + translateLanguage.Target + "&nbsp;&nbsp;:<input type='text' id='DialTargetValue' style='width: 90px;' /> " + translateLanguage.Day + "</td>";
        kpiProp += "<td><input type='button'  class='navtitle-hover' id='getDialDeviationVal' name='getDeviation' value='getDeviation' onclick='getDialDeviation()' style=''></td>";
        kpiProp += "</tr></table> </div>";

        kpiProp += "<div id='changePerDiv' style='display: none'>";
        kpiProp += "<table><tr>";
        kpiProp += "<td id='dialchangePersentTd' style='' class='gFontFamily gFontSize12'>" + translateLanguage.Change_Percent + ":&nbsp;&nbsp;<input type='text' value='' id='DialChartChangePerVal' style='width: 90px' readonly/></td>";
        kpiProp += "</tr></table></div> </tr>";
        kpiProp += "<tr>";
        kpiProp += "<td id='dialDeveationTd' style='display: none' align='left' class='gFontFamily gFontSize12'>" + translateLanguage.Deviation_in_per + "&nbsp;&nbsp;<input type='text' id='dailDeviationValue' readonly /></td>";
        kpiProp += "</tr> </table> </div></td>";
        kpiProp += "<tr>";
        kpiProp += "<td colspan=''><div id='lowerDialDiv' >";
        kpiProp += "<table border='0'>";
        kpiProp += "<tr>";
        kpiProp += "<td align='center'><span class='gFontFamily gFontSize12 fontWeightBold'>" + translateLanguage.Risk + "</span></td>";
        kpiProp += "<td align='center'><span class='gFontFamily gFontSize12 fontWeightBold'>" + translateLanguage.Condition + "</span></td>";
        kpiProp += "<td align='center' colspan='2'><span style='float:center' class='gFontFamily gFontSize12 fontWeightBold'>" + translateLanguage.Range + "</span></td>";
        kpiProp += "</tr>";
        var keys = ["High", "Medium", "Low"];
        var keysLabel = ["Red ", "Amber ", "Green "];
        var keysLabel1 = ["Sad ", "Normal ", "Happy "];

        for (var i = 0; i < 3; i++) {
            var nextKey = (i < (keys.length - 1) ? keys[(i + 1)] : "null");
            kpiProp += "<tr>";
            var keyColor;
            if (i == 0) {
                if (dialType == 'Standard') {
                    keyColor = "Red";
                }
                else {
                    keyColor = "#00FF00";
                }
            }
            else if (i == 1) {
                keyColor = "#FFC200";
            }
            else {
                if (dialType == 'Standard') {
                    keyColor = "#00FF00";
                }
                else {
                    keyColor = "Red";
                }
            }
            if (chartType == 'Dial-Gauge') {
                kpiProp += "<td id='conditionColor" + i + "' bgcolor='" + keyColor + "'><span class='gFontFamily gFontSize12 fontWeightBold'></span></td>";
                kpiProp += "<td align='center'><select  id='DialChartOperator" + keys[i] + "' name='DialChartOperator" + keys[i] + "'>";
            }
            else {
                kpiProp += "<td bgcolor=''><span class='gFontFamily gFontSize12 fontWeightBold'>" + keysLabel1[i] + "</span></td>";
                kpiProp += "<td align='center'><select  id='DialChartOperator" + keys[i] + "' name='DialChartOperator" + keys[i] + "'>";
            }
            kpiProp += "<option class='gFontFamily gFontSize12 fontWeightBold' value=''>&lt;&gt;</option>";
            kpiProp += "</select></td>";
            kpiProp += "<td id='dialRangesIDGraph'>";
            if (i === 0) {

                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["minGreeen"] != "undefined" && chartData[chartId]["dialValues"]["minGreeen"] != "") {

                    kpiProp += "<input type='text'  value='" + chartData[chartId]["dialValues"]["minGreeen"] + "' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                } else {
                    kpiProp += "<input type='text' value='-50' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                }
                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["maxGreeen"] != "undefined" && chartData[chartId]["dialValues"]["maxGreeen"] != "") {
                    kpiProp += "<td><input type='text' value='" + chartData[chartId]["dialValues"]["maxGreeen"] + "' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                }
                else {
                    kpiProp += "<td><input type='text' value='-20' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                }
            } else if (i === 1) {

                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["minOrange"] != "undefined" && chartData[chartId]["dialValues"]["minOrange"] != "") {
                    kpiProp += "<input type='text' value='" + chartData[chartId]["dialValues"]["minOrange"] + "' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                } else {
                    kpiProp += "<input type='text' value='-20' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                }
                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["maxOrange"] != "undefined" && chartData[chartId]["dialValues"]["maxOrange"] != "") {
                    kpiProp += "<td><input type='text' value='" + chartData[chartId]["dialValues"]["maxOrange"] + "' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                } else {
                    kpiProp += "<td><input type='text' value='14' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                }
            } else {
                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["minRed"] != "undefined" && chartData[chartId]["minRed"] != "") {
                    kpiProp += "<input type='text'  value='" + chartData[chartId]["dialValues"]["minRed"] + "' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                } else {
                    kpiProp += "<input type='text' value='14' id='range" + keys[i] + "_1' style='width: 50px'/></td>";
                }
                if (typeof chartData[chartId]["dialValues"] != "undefined" && chartData[chartId]["dialValues"] != "" && typeof chartData[chartId]["dialValues"]["maxRed"] != "undefined" && chartData[chartId]["dialValues"]["maxRed"] != "") {
                    kpiProp += "<td><input type='text' value='" + chartData[chartId]["dialValues"]["maxRed"] + "' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                } else {
                    kpiProp += "<td><input type='text' value='25' id='range" + keys[i] + "_2' style='width: 50px;display:block;' onkeyup='reflectValueGraph(this.id,\"" + nextKey + "\")'/>";
                }
            }
            kpiProp += "</td> </tr> ";
        }
        kpiProp += "</table></div></td></tr> </table></div>";
        kpiProp += "</td>";
        setTimeout(function() {
            if (display === 'block') {
                checkDial('dialSelectChart', chartId, 'yes');
            }
        }, 10);
    }
    kpiProp += "</tr>";

    kpiProp += "<tr>";
    kpiProp += "<td class='gFontFamily gFontSize12'>";
    if (chartData[chartId]["chartType"] === "Table") {
        if (typeof chartData[chartId]["showST"] !== "undefined" && chartData[chartId]["showST"] == "Y") {
            kpiProp += "" + translateLanguage.Show_ST + " </td><td> <input type='checkbox' id='" + chartId + "showST' class='gFontFamily gFontSize12' checked>";
        } else {
            kpiProp += "" + translateLanguage.Show_ST + " </td><td> <input type='checkbox' id='" + chartId + "showST' class='gFontFamily gFontSize12'>";
        }
    }
    kpiProp += "</td>";

    kpiProp += "<td class='gFontFamily gFontSize12'>";
    if (chartData[chartId]["chartType"] === "Table" || chartData[chartId]["chartType"] === "Bar-Table") {
        if (typeof chartData[chartId]["showGT"] !== "undefined" && chartData[chartId]["showGT"] == "Y") {
            kpiProp += "" + translateLanguage.Show_GT + " </td><td> <input type='checkbox' id='" + chartId + "showGT' class='gFontFamily gFontSize12' checked>";

        } else {
            kpiProp += "" + translateLanguage.Show_GT + " </td><td> <input type='checkbox' id='" + chartId + "showGT' class='gFontFamily gFontSize12'>";
        }
    }
    kpiProp += "</td>";
    kpiProp += "</tr>";
    // end by mynk sh. for font size
    if (chartData[chartId]["chartType"] === "Table") {
        // if(chartType==='Table')
        kpiProp += "<tr>";
        kpiProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Show_Emojis + "</label></td>";
        kpiProp += "<td>";
        if (typeof chartData[chartId]["showEmoji"] !== "undefined" && chartData[chartId]["showEmoji"] !== "hidden") {
            kpiProp += "<input type='checkbox' id='" + chartId + "showEmoji' onchange='showEmoji(\"" + chartId + "\",this.value)' class='gFontFamily gFontSize12' checked>";
        } else {
            kpiProp += "<input type='checkbox' id='" + chartId + "showEmoji' onchange='showEmoji(\"" + chartId + "\",this.value)' class='gFontFamily gFontSize12'>";
        }
        kpiProp += "</td>";
        if (typeof chartData[chartId]["showEmoji"] !== 'undefined' && chartData[chartId]["showEmoji"] != 'hidden') {
            kpiProp += "<td id='" + chartId + "emojiDiv1' style='display:block'><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Emoji_Type + " </label></td>";
            kpiProp += "<td id='" + chartId + "emojiDiv2' style='display:block'><select id ='" + chartId + "emojiType' onchange='selectEmojiType(\"" + chartId + "\",this.value)'>";
            if (typeof chartData[chartId]["showEmoji"] === "undefined" || chartData[chartId]["showEmoji"] === "target") {
                kpiProp += "<option class='gFontFamily gFontSize12' value='target' selected>Target Value</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='absolute' >Absolute Value</option>";
            } else {
                kpiProp += "<option class='gFontFamily gFontSize12' value='target' >Target Value</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='absolute' selected>Absolute Value</option>";
            }
            kpiProp += "</select></td>";
        }
        else {
            kpiProp += "<td id='" + chartId + "emojiDiv1' style='display:none'><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Emoji_Type + " </label></td>";
            kpiProp += "<td id='" + chartId + "emojiDiv2' style='display:none'><select id ='" + chartId + "emojiType' onchange='selectEmojiType(\"" + chartId + "\",this.value)'>";
            if (typeof chartData[chartId]["showEmoji"] === "undefined" || chartData[chartId]["showEmoji"] === "target") {
                kpiProp += "<option class='gFontFamily gFontSize12' value='target' selected>Target Value</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='absolute' >Absolute Value</option>";
            } else {
                kpiProp += "<option class='gFontFamily gFontSize12' value='target' >Target Value</option>";
                kpiProp += "<option class='gFontFamily gFontSize12' value='absolute' selected>Absolute Value</option>";
            }
            kpiProp += "</select></td>";
        }
        kpiProp += "</tr>";
        kpiProp += "<tr><td colspan=3>";
        kpiProp += "<div id='emojiCondition' style='display:none;width:100%;position: relative;width: 100%;border:1px solid;border-radius:5px ;border-color:#79C9EC; margin-top:2%'>"
        kpiProp += "</div>"
        kpiProp += "</td></tr>";
    }

    //add by mynk sh. for bullet chart
    var chartType = chartData[chartId]["chartType"];
    if (chartType === 'Bullet-Horizontal') {
        var bulletAbovecolor = "";
        var bulletBelowcolor = "";
        var bulletHighcolor = "";
        var bulletMediumcolor = "";
        var bulletLowcolor = "";
        if (typeof chartData[chartId]["bulletAbovecolor"] !== 'undefined' && chartData[chartId]["bulletAbovecolor"] !== '') {
            bulletAbovecolor = chartData[chartId]["bulletAbovecolor"];
        } else {
            bulletAbovecolor = "green";
        }
        if (typeof chartData[chartId]["bulletBelowcolor"] !== 'undefined' && chartData[chartId]["bulletBelowcolor"] !== '') {
            bulletBelowcolor = chartData[chartId]["bulletBelowcolor"];
        } else {
            bulletBelowcolor = "red";
        }
        if (typeof chartData[chartId]["bulletHighcolor"] !== 'undefined' && chartData[chartId]["bulletHighcolor"] !== '') {
            bulletHighcolor = chartData[chartId]["bulletHighcolor"];
        } else {
            bulletHighcolor = "#CCC";
        }
        if (typeof chartData[chartId]["bulletMediumcolor"] !== 'undefined' && chartData[chartId]["bulletMediumcolor"] !== '') {
            bulletMediumcolor = chartData[chartId]["bulletMediumcolor"];
        } else {
            bulletMediumcolor = "#DDD";
        }
        if (typeof chartData[chartId]["bulletLowcolor"] !== 'undefined' && chartData[chartId]["bulletLowcolor"] !== '') {
            bulletLowcolor = chartData[chartId]["bulletLowcolor"];
        } else {
            bulletLowcolor = "#EEE";
        }

        kpiProp += "<tr><td colspan=4>";
        kpiProp += "<div id='bulletTarget' style='display:block;width:100%;position: relative;width: 100%;border:1px solid;border-radius:5px ; margin-top:2%'>"
        kpiProp += "<table  style='width:100%'>";
        kpiProp += "<tr style='width:100%'><th style='width:20%' class='gFontFamily gFontSize12'>" + translateLanguage.Above_Target_Color + " \n\
                          <input id='bulletAbovecolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"bulletAbovecolor\")' style='width:30px;background-color:" + bulletAbovecolor + "' ></th>";
        kpiProp += "<th style='width:20%' >" + translateLanguage.Below_Target_Color + " \n\
                          <input id='bulletBelowcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"bulletBelowcolor\")' style='width:30px;background-color:" + bulletBelowcolor + "' ></th>";
        kpiProp += "</tr></table>";

        kpiProp += "<table style='margin:10px'>";
        kpiProp += "<tr style='width:100%'><td class='gFontFamily gFontSize12'>" + translateLanguage.Measure + " </td>\n\
                          <td class='gFontFamily gFontSize12'>" + translateLanguage.Target + " </td>\n\
                          <td class='gFontFamily gFontSize12'>" + translateLanguage.High + " \n\
<input id='bulletLowcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"bulletLowcolor\")' style='width:30px;background-color:" + bulletLowcolor + "' ></td>\n\
                          <td class='gFontFamily gFontSize12'>" + translateLanguage.Mid + " \n\
<input id='bulletMediumcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"bulletMediumcolor\")' style='width:30px;background-color:" + bulletMediumcolor + "' ></td>\n\
                          <td  class='gFontFamily gFontSize12'>" + translateLanguage.Low + " \n\
<input id='bulletHighcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"bulletHighcolor\")' style='width:30px;background-color:" + bulletHighcolor + "' ></td>\n\
                          <td  class='gFontFamily gFontSize12'>" + translateLanguage.Number_Format + " </td>";
        kpiProp += "</tr>";
        var bulletHorizontal = [];
        var KPIResult = chartData[chartId]["GTValueList"]
        for (var i in KPIResult) {
            //  if(KPIResult[i]<100){
            bulletHorizontal.push(KPIResult[i])
            //  }else{
            //    bulletHorizontal.push(100)
            //  }
        }
        for (var i in measures) {
            var low, mid, high, target;

            // alert(JSON.stringify(chartData[chartId]["bulletParameters"]));
            if (typeof chartData[chartId]["bulletParameters"] != 'undefined' && typeof chartData[chartId]["bulletParameters"][i] != "undefined" && typeof chartData[chartId]["bulletParameters"][i][measures[i]] != 'undefined') {
                if (typeof chartData[chartId]["bulletParameters"][i][measures[i]]["low"] !== 'undefined' && chartData[chartId]["bulletParameters"][i][measures[i]]["low"] !== '') {
                    low = chartData[chartId]["bulletParameters"][i][measures[i]]["low"];
                } else {
                    low = bulletHorizontal[i] * .33;
                }
                if (typeof chartData[chartId]["bulletParameters"][i][measures[i]]["mid"] !== 'undefined' && chartData[chartId]["bulletParameters"][i][measures[i]]["mid"] !== '') {
                    mid = chartData[chartId]["bulletParameters"][i][measures[i]]["mid"];
                } else {
                    mid = bulletHorizontal[i] * .66;
                }
                if (typeof chartData[chartId]["bulletParameters"][i][measures[i]]["high"] !== 'undefined' && chartData[chartId]["bulletParameters"][i][measures[i]]["high"] !== '') {
                    high = chartData[chartId]["bulletParameters"][i][measures[i]]["high"];
                }
                else {
                    high = bulletHorizontal[i] * 1.5;
                }

                if (typeof chartData[chartId]["bulletParameters"][i][measures[i]]["target"] !== 'undefined' && chartData[chartId]["bulletParameters"][i][measures[i]]["target"] !== '') {
                    target = chartData[chartId]["bulletParameters"][i][measures[i]]["target"];
                } else {
                    target = bulletHorizontal[i] * .75;
                }
            } else {
                low = bulletHorizontal[i] * .33;
                mid = bulletHorizontal[i] * .66;
                high = bulletHorizontal[i] * 1.5;
                target = bulletHorizontal[i] * .75;
            }
            kpiProp += "<tr><td>";
            kpiProp += "<label style='width:60%;white-space:nowrap' class='gFontFamily gFontSize12'>" + measures[i] + "</label></td><td><input id='bulletTarget" + i + "' align='center' style='width:60%' type='text' value='" + target + "' >";

            kpiProp += "</td>";
            kpiProp += "<td><input id='bulletHigh" + i + "' style='width:60%' type='text' value='" + high + "' ></td>";
            kpiProp += "<td><input id='bulletMid" + i + "'  style='width:60%' type='text'value='" + mid + "' ></td>";
            kpiProp += "<td><input id='bulletLow" + i + "' style='width:60%' type='text' value='" + low + "'></td>";

            kpiProp += "</tr>";
        }
        kpiProp += "</table>";
        kpiProp += "</div>"
        kpiProp += "</td></tr>";
    }// end by mynk sh. for bullet chart.

    kpiProp += "</table></div>";   // html kpi
    //-------------------------------------
    var chartType = chartData[chartId]["chartType"];
    var axisprop = "";
    var chartData = JSON.parse($("#chartData").val());
    //alert(JSON.stringify(chartData));
    //   $("#axisProperties").html(axisprop);

    axisprop += "<div id='axisProperties' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    //    axisprop +="<div id='axisProperties' class='tag-link-properties' style='width:100%; margin-left: 1%;'>";
    axisprop += "<table  id='axisProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";


    // block (1)
    axisprop += "<tr>";
    axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>Y-" + translateLanguage.Axis_Number_Format + "</label></td>";
    axisprop += "<td><select id='" + chartId + "yAxisFormat' class='gFontFamily gFontSize12'  name='Dashname' onchange='selectNumberFormat(this.id,\"" + chartId + "\")'>";
    if (typeof chartData[chartId]["yAxisFormat"] === "undefined" || chartData[chartId]["yAxisFormat"] === "" || chartData[chartId]["yAxisFormat"] === "Auto") {
        axisprop += "<option class='gFontFamily gFontSize12' value='Auto' selected>Auto</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='Auto'>Auto</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "Absolute") {
        axisprop += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "K") {
        axisprop += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "M") {
        axisprop += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "Cr") {
        axisprop += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
    }
    if (chartData[chartId]["yAxisFormat"] === "L") {
        axisprop += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12'value='L'>Lakh</option>";
    }
    axisprop += "</select></td>";

    axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>Y-" + translateLanguage.Axis_Rounding + " </label></td>";
    axisprop += "<td><select id='" + chartId + "rounding' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (chartData[chartId]["chartType"] == "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1"|| chartData[chartId]["chartType"] == "Veraction-Combo" || chartData[chartId]["chartType"] == "KPIDash" || chartData[chartId]["chartType"] == "Table") {
        if (typeof chartData[chartId]["rounding"] === "undefined" || chartData[chartId]["rounding"] === "1") {
            axisprop += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
        }
        if (chartData[chartId]["rounding"] === "0") {
            axisprop += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
        }
    }
    else {
        if (typeof chartData[chartId]["rounding"] === "undefined" || chartData[chartId]["rounding"] === "1") {
            axisprop += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
        }

        if (chartData[chartId]["rounding"] === "0") {
            axisprop += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
        }

        if (chartData[chartId]["rounding"] === "2") {
            axisprop += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
        }
    }
    axisprop += "</select></td>";

    display = '';
    if (typeof chartData[chartId]["yAxisFormat"] === 'undefined' || chartData[chartId]["yAxisFormat"] === 'Auto') {
        display = 'block';
    }
    else {
        display = 'none';
    }

    axisprop += "<td id='" + chartId + "roundingType' style='display: " + display + ";'><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Rounding_Type + " </label></td>";
    axisprop += "<td><select id='" + chartId + "roundingType1' class='gFontFamily gFontSize12'  name='Dashname'>";
    if (typeof chartData[chartId]["roundingType"] === "undefined" || chartData[chartId]["roundingType"] === "MB") {
        axisprop += "<option class='gFontFamily gFontSize12' value='MB' selected>Million-Billion</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='MB'>Million-Billion</option>";
    }
    if (chartData[chartId]["roundingType"] === "LCr") {
        axisprop += "<option class='gFontFamily gFontSize12' value='LCr' selected>Lakh-Crore</option>";
    } else {
        axisprop += "<option class='gFontFamily gFontSize12' value='LCr'>Lakh-Crore</option>";
    }
    axisprop += "</select></td>";
    axisprop += "</tr>";


    // block (2)
    axisprop += "<tr>";
    axisprop += "<td style='font: 9px verdana;font-size:8pt'>";
    if (typeof chartData[chartId]["appendNumberFormat"] === "undefined" || chartData[chartId]["appendNumberFormat"] == "Y") {
        axisprop += "" + translateLanguage.Show_Number_Format + " </td><td> <input type='checkbox' id='" + chartId + "appendNumberFormat' checked>";
    } else {
        axisprop += "" + translateLanguage.Show_Number_Format + " </td><td> <input type='checkbox' id='" + chartId + "appendNumberFormat'>";
    }
    axisprop += "</td>";

    if (!(chartType=== "KPI-Dashboard" || chartType === "Pie" || chartType === "Pie-3D" || chartType === "Half-Pie" || chartType === "Donut" || chartType === "Half-Donut" || chartType === "Donut-3D" || chartType === "Double-Donut" ||
            chartType === "Aster" || chartType === "Bubble")) {

        if (chartType === "Filled-Horizontal" || chartType === "GroupedStacked-Bar%" || chartType === "MultiMeasureSmooth-Line" || chartType === "Cumulative-Bar" || chartType === "Multi-Layered-Bar" || chartType === "StackedBarLine" || chartType === "Horizontal-Bar" || chartType === "Combo-Horizontal" || chartType === "Horizontal-StackedBar" || chartType === "Scatter-Bubble" || chartType === "Scatter" || chartType === "Vertical-Bar" || chartType === "Line" || chartType === "DualAxis-Bar" || chartType === "Veraction-Combo3" || chartType === "Cumulative-Line" || chartType === "Trend-Analysis" || chartType === "GroupedStackedH-Bar%" || chartType === "DualAxis-Group"
                || chartType === "SmoothLine" || chartType === "MultiMeasure-Line" || chartType === 'Multi-Layered-Bar' || chartType === "OverLaid-Bar-Line" || chartType === "MultiMeasure-Bar" || chartType === "MultiMeasureH-Bar" || chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] === "GroupedHorizontal-Bar" || chartData[chartId]["chartType"] === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "StackedBar" || chartType === "Area" || chartType === "MultiMeasure-Area"|| chartType === "Transportation-Spend-Variance"|| chartType === "Score-vs-Targets") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Display_Grid_Lines + "</label></td>";
            axisprop += "<td><select id='" + chartId + "GridLines' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["GridLines"] === "undefined" || chartData[chartId]["GridLines"] === "Yes") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
            }

            if (chartData[chartId]["GridLines"] === "No") {
                axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
            }
            axisprop += "</select></td>";
        }

        if (chartData[chartId]["chartType"] == "Vertical-Bar" || chartData[chartId]["chartType"] == "Transportation-Spend-Variance" || chartData[chartId]["chartType"] == "Vertical-Negative-Bar" || chartData[chartId]["chartType"] == "Horizontal-Bar" || chartData[chartId]["chartData"] === "Combo-Horizontal") {
            axisprop += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Adhoc_Target_Value + " </label></td>";
            axisprop += "<td>";
            var targetLine = '';
            if (typeof chartData[chartId]["targetLine"] !== "undefined" && chartData[chartId]["targetLine"] !== "") {
                targetLine = chartData[chartId]["targetLine"];
            }
            axisprop += "<input type='text' id='" + chartId + "targetLine' class='gFontFamily gFontSize12' size='6' value=" + targetLine + ">";
            axisprop += "</td>";
        }

        if (chartType === "Filled-Horizontal" || chartType === "Bar-Table") {
            if (chartType === "Filled-Horizontal") {
                var fillBackground = "#E8E8E8";
            } else {
                var fillBackground = "#FFF";
            }
            if (typeof chartData[chartId]["fillBackground"] !== "undefined" && chartData[chartId]["fillBackground"] != "") {
                fillBackground = chartData[chartId]["fillBackground"];
            }
            axisprop += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Background_Color + " </td><td> <input style='background-color:" + fillBackground + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='fillBackground' readonly></td>";
        }

        if (typeof chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] != "" && (chartData[chartId]["chartType"] == "DualAxis-Bar" || chartData[chartId]["chartType"] === "Veraction-Combo3" || chartData[chartId]["chartType"] === "DualAxis-Group")) {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Dual_Axis + "</label></td>";
            axisprop += "<td><select id='" + chartId + "DualAxisProp' class='gFontFamily gFontSize12' name='Dashname'>";
            if (typeof chartData[chartId]["DualAxisProp"] === "undefined" || chartData[chartId]["DualAxisProp"] === "withLine") {
                axisprop += "<option class='gFontFamily gFontSize12' value='withLine' selected>With Line</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='withLine'>With Line</option>";
            }
            if (chartData[chartId]["DualAxisProp"] === "withoutLine") {
                axisprop += "<option class='gFontFamily gFontSize12' value='withoutLine' selected>Without Line</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='withoutLine'>Without Line</option>";
            }
            axisprop += "</select></td>";
        }

        if (chartData[chartId]["chartType"] == "Line" || chartData[chartId]["chartType"] === "Trend-Analysis" || chartData[chartId]["chartType"] == "SmoothLine" || chartData[chartId]["chartType"] == "MultiMeasure-Line" || chartData[chartId]["chartType"] == "Cumulative-Line" || chartData[chartId]["chartType"] == "MultiMeasureSmooth-Line") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Line_Type + " </label></td>";
            axisprop += "<td><select id='" + chartId + "lineType' class='gFontFamily gFontSize12'  name='linename'>";
            if (typeof chartData[chartId]["lineType"] === 'undefined' || chartData[chartId]["lineType"] === 'default') {
                axisprop += "<option class='gFontFamily gFontSize12' value='default' selected>Default</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='default'>Default</option>";
            }
            if (chartData[chartId]["lineType"] === 'dashed-Line') {
                axisprop += "<option class='gFontFamily gFontSize12' value='dashed-Line' selected>Dashed-Line</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='dashed-Line'>Dashed-Line</option>";
            }
            axisprop += "</select></td>";
        }
        axisprop += "</tr>";
        // block (3)
        if (chartData[chartId]["chartType"] == "DualAxis-Bar" || chartData[chartId]["chartType"] === "Veraction-Combo3" || chartData[chartId]["chartType"] === "DualAxis-Bar2") {
            axisprop += "<tr>";
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y1-" + translateLanguage.Axis_Number_Format + " </label></td>";
            axisprop += "<td><select id='" + chartId + "y2AxisFormat' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["y2AxisFormat"] === "undefined" || chartData[chartId]["y2AxisFormat"] === "" || chartData[chartId]["y2AxisFormat"] === "Auto") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Auto' selected>Auto</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Auto'>Auto</option>";
            }
            if (chartData[chartId]["y2AxisFormat"] === "Absolute") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
            }
            if (chartData[chartId]["y2AxisFormat"] === "K") {
                axisprop += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
            }
            if (chartData[chartId]["y2AxisFormat"] === "M") {
                axisprop += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
            }
            if (chartData[chartId]["y2AxisFormat"] === "Cr") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
            }
            if (chartData[chartId]["y2AxisFormat"] === "L") {
                axisprop += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='L'>Lakh</option>";
            }
            axisprop += "</select></td>";

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y1-" + translateLanguage.Axis_Rounding + " </label></td>";
            axisprop += "<td><select id='" + chartId + "rounding1' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["rounding1"] === "undefined" || chartData[chartId]["rounding1"] === "1 Decimal") {
                axisprop += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
            }
            if (chartData[chartId]["rounding1"] === "No Decimal") {
                axisprop += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
            }
            if (chartData[chartId]["rounding1"] === "2") {
                axisprop += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
            }
            axisprop += "</select></td>";

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Display_Y1_Grid_Lines + "</label></td>";
            axisprop += "<td><select id='" + chartId + "GridLines1' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["GridLines1"] === "undefined" || chartData[chartId]["GridLines1"] === "Yes") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
            }
            if (chartData[chartId]["GridLines1"] === "No") {
                axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
            }
            axisprop += "</select></td>";

            axisprop += "</tr>";
        }
// block (4)
        if (chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar" || chartData[chartId]["chartType"] == "Grouped-Line" || chartData[chartId]["chartType"] == "GroupedStackedH-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-Bar%" || chartData[chartId]["chartType"]=="Grouped-Map"  || chartData[chartId]["chartType"] == "Grouped-Table") {
            axisprop += "<tr>";
            var innerRecords = "5";
            if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "") {
                chartRecords = chartData[chartId]["records"];
            }
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.No_of_Inner_Records + "</label></td>";
            axisprop += "<td><input type='text' id='" + chartId + "InnerRecords' size='6' value=" + innerRecords + " class='gFontFamily gFontSize12'>";
            axisprop += "</td>";

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Inner_Sort_Option + "</label></td>";
            axisprop += "<td><select id='" + chartId + "innerSorting' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["innerSorting"] === "undefined" || chartData[chartId]["innerSorting"] === "Descending") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Descending' selected>Ascending</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Descending'>Ascending</option>";
            }
            if (chartData[chartId]["innerSorting"] === "Ascending") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Ascending' selected>Descending</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Ascending'>Descending</option>";
            }
            axisprop += "</select></td>";

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Inner_Sort_Basis + " </label></td>";
            axisprop += "<td><select id='" + chartId + "innerSortBasis' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["innerSortBasis"] === "undefined" || chartData[chartId]["innerSortBasis"] === "Value") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Value' selected>Value</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Value'>Value</option>";
            }
            if (chartData[chartId]["innerSortBasis"] === "Alphabetic") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Alphabetic' selected>Alphabetic</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Alphabetic'>Alphabetic</option>";
            }
            axisprop += "</select></td>";
            axisprop += "</tr>";
        }

        // block (5)
        if (chartType === "OverLaid-Bar-Line" || chartType==="Combo-Horizontal"|| chartType === "Vertical-Bar" || chartType === "Vertical-Negative-Bar" || chartType === "Horizontal-Bar" || chartType === "Filled-Horizontal") {
            axisprop += "<tr>";
            axisprop += "<td class='gFontFamily gFontSize12'>";
            if (typeof chartData[chartId]["innerLabels"] !== "undefined" && chartData[chartId]["innerLabels"] === "Y") {
                axisprop += "" + translateLanguage.Axis_Names_on_Bars + "</td><td> <input type='checkbox' id='" + chartId + "innerLabels' checked>";
            } else {
                axisprop += "" + translateLanguage.Axis_Names_on_Bars + "</td><td> <input type='checkbox' id='" + chartId + "innerLabels' >";
            }
            axisprop += "</td>";

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Axis_Names_Font_Size + " </label></td>";
            axisprop += "<td><select id='" + chartId + "fontsize' class='gFontFamily gFontSize12' name='fontname'>";
            if (typeof chartData[chartId]["fontsize"] === 'undefined' || chartData[chartId]["fontsize"] === '') {
                axisprop += "<option class='gFontFamily gFontSize12' value='' selected> Select </option>";
            } else {

                axisprop += "<option class='gFontFamily gFontSize12' value='' > Select </option>";
            }
            for (var i = 9; i <= 15; i++)
            {
                if (typeof chartData[chartId]["fontsize"] !== 'undefined') {
                    if (chartData[chartId]["fontsize"] === i.toString()) {
                        axisprop += "<option class='gFontFamily gFontSize12' value=" + i + " selected> " + i + " </option>";
                    } else {
                        axisprop += "<option class='gFontFamily gFontSize12' value=" + i + "> " + i + " </option>";
                    }
                }
                else {
                    axisprop += "<option class='gFontFamily gFontSize12' value=" + i + " > " + i + " </option>";
                }
            }
            axisprop += "</td>";

            var AxisNameColor = "#d8d47d";
            if (typeof chartData[chartId]["AxisNameColor"] !== "undefined" && chartData[chartId]["AxisNameColor"] != "") {
                AxisNameColor = chartData[chartId]["AxisNameColor"];
            }
            axisprop += "<td class='gFontFamily gFontSize12'>" + translateLanguage.AxisNameColor + "</td><td> <input style='background-color:" + AxisNameColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='AxisNameColor' readonly></td>";
            axisprop += "</tr>";
        }
        // block (5)
        axisprop += "<tr>";
        if (chartData[chartId]["chartType"] === "MultiMeasure-Bar" || chartData[chartId]["chartType"] === "MultiMeasureH-Bar" || chartData[chartId]["chartType"] === "MultiMeasure-Line") {
            axisprop += "<td style='white-space:nowrap'><label class='gFontFamily gFontSize12'> " + translateLanguage.Measure_Transpose + " </label></td>";
            axisprop += "<td>";
            if (typeof chartData[chartId]["transposeMeasure"] != 'undefined' && chartData[chartId]["transposeMeasure"] == 'Y') {
                axisprop += "<input type='checkbox' id='" + chartId + "transposeMeasure' class='gFontFamily gFontSize12' checked>";
            } else {
                axisprop += "<input type='checkbox' id='" + chartId + "transposeMeasure' class='gFontFamily gFontSize12'>";
            }
            axisprop += "</td>";
        }

        var chartType = chartData[chartId]["chartType"];
//if(chartType ==="Line" || chartType === "DualAxis-Bar" || chartData[chartId]["chartType"]==="DualAxis-Bar2" || chartData[chartId]["chartType"]==="DualAxis-Group"){
//    axisprop += "<td class='gFontFamily gFontSize12'>";
//    if(chartData[chartId]["showPercent"]!=="undefined" && chartData[chartId]["showPercent"]=="Y"){
//        axisprop +=""+translateLanguage.Show_Hide+" % </td><td> <input type='checkbox' id='"+chartId+"showPercent' checked>";
//    }else{
//        axisprop +=""+translateLanguage.Show_Hide+" % </td><td> <input type='checkbox' id='"+chartId+"showPercent' >";
//    }
//    axisprop += "</td>"; 
//}

if (chartType ==='Top-Analysis'||chartType ==='Combo-Analysis'||chartType ==='World-Top-Analysis') {
            axisprop += "<td style='font: 11px verdana;font-size:9pt'>" + translateLanguage.Font_Size + "</td>";
            if (typeof chartData[chartId]["fontSize1"] !== "undefined" && chartData[chartId]["fontSize1"] != "") {
                axisprop += "<td><input type='text'size='6'  value ='" + chartData[chartId]["fontSize1"] + "' id ='" + chartId + "fontSize1'></td>";
            } else {
                axisprop += "<td><input type='text' size='6' value='22' id ='" + chartId + "fontSize1'></td>";
            }
        }
        axisprop += "</tr>";

        axisprop += "<tr>";
        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Show_X_Axis_Labels + "</label></td>";
        axisprop += "<td><select id='" + chartId + "displayX' class='gFontFamily gFontSize12'  name='Dashname'>";
        if (typeof chartData[chartId]["displayX"] === "undefined" || chartData[chartId]["displayX"] === "Yes") {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
        if (chartData[chartId]["displayX"] === "No") {
            axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        axisprop += "</select></td>";

        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Show_Y_Axis_Labels + " </label></td>";
        axisprop += "<td><select id='" + chartId + "displayY' class='gFontFamily gFontSize12'  name='Dashname'>";
        if (typeof chartData[chartId]["displayY"] === "undefined" || chartData[chartId]["displayY"] === "Yes") {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
        if (chartData[chartId]["displayY"] === "No") {
            axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        axisprop += "</select></td>";


        if (typeof chartData[chartId]["chartType"] !== "undefined" && (chartData[chartId]["chartType"] == "OverLaid-Bar-Line" || chartData[chartId]["chartType"] == "DualAxis-Bar"|| chartData[chartId]["chartType"] == "Veraction-Combo3") || chartData[chartId]["chartType"] === "DualAxis-Group" || chartData[chartId]["chartType"] === "DualAxis-Target") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Display_Y1_Axis + " </label></td>";
            axisprop += "<td><select id='" + chartId + "displayY1' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["displayY1"] === "undefined" || chartData[chartId]["displayY1"] === "Yes") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
            }
            if (chartData[chartId]["displayY1"] === "No") {
                axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
            }
            axisprop += "</select></td>";
        }
        axisprop += "</tr>";

        axisprop += "<tr>";
        axisprop += "<td style='font: 11px verdana;font-size:9pt'<label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_X_Axis_Line + " </label></td>";
        axisprop += "<td><select id='" + chartId + "displayXLine' class='gFontFamily gFontSize12'  name='Dashname'>";
        if (typeof chartData[chartId]["displayXLine"] === "undefined" || chartData[chartId]["displayXLine"] === "Yes") {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
        if (chartData[chartId]["displayXLine"] === "No") {
            axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        axisprop += "</select></td>";

        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Display_Y_Axis_Line + " </label></td>";
        axisprop += "<td><select id='" + chartId + "displayYLine' class='gFontFamily gFontSize12'  name='Dashname'>";
        if (typeof chartData[chartId]["displayYLine"] === "undefined" || chartData[chartId]["displayYLine"] === "Yes") {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
        }
        if (chartData[chartId]["displayYLine"] === "No") {
            axisprop += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
        } else {
            axisprop += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        }
        axisprop += "</select></td>";
        axisprop += "</tr>";

        // block 3
        if (!(chartData[chartId]["chartType"] === 'Scatter-XY')) {
        axisprop += "<tr>";
        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Ticks_Frequency_Y + "</label></td>";
        if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"] != "" && chartData[chartId]["yaxisrange"]["axisTicks"] != "undefined" && chartData[chartId]["yaxisrange"]["axisTicks"] != "") {
            var YaxisTicksValue = chartData[chartId]["yaxisrange"]["axisTicks"];
            axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisTicks' class='gFontFamily gFontSize12' value = '" + YaxisTicksValue + "'></td>";
        } else {
            axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisTicks' class='gFontFamily gFontSize12'></td>";
        }
        axisprop += "</select></td>";
       }
        if (chartData[chartId]["chartType"] === "StackedBarLine" || chartData[chartId]["chartType"] === "DualAxis-Target" || chartData[chartId]["chartType"] == "DualAxis-Bar" || chartData[chartId]["chartType"] == "Veraction-Combo3" || chartData[chartId]["chartType"] === "DualAxis-Group") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Ticks_Frequency_Y + "1</label></td>";
            if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"] != "" && chartData[chartId]["yaxisrange"]["axisTicks1"] != "undefined" && chartData[chartId]["yaxisrange"]["axisTicks1"] != "") {
                if (typeof chartData[chartId]["yaxisrange"]["axisTicks1"] != "undefined") {
                    var YaxisTicksValue1 = chartData[chartId]["yaxisrange"]["axisTicks1"];
                } else {
                    YaxisTicksValue1 = "";
                }
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisTicks1' class='gFontFamily gFontSize12' value = '" + YaxisTicksValue1 + "'></td>";
            } else {
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisTicks1' class='gFontFamily gFontSize12' value = ''></td>";
            }
            axisprop += "</select></td>";
        }
        axisprop += "</tr>";

        // block 4
        axisprop += "<tr>";
        if (chartType === "Vertical-Negative-Bar" || chartType === "MultiMeasureSmooth-Line" || chartType === "Cumulative-Bar" || chartType === "Multi-Layered-Bar" || chartType === "StackedBarLine" || chartType === "Vertical-Bar" || chartType === "Line" || chartType === "DualAxis-Bar"|| chartType === "Veraction-Combo3"|| chartType === "Score-vs-Targets"|| chartType === "Transportation-Spend-Variance"|| chartType === "Veraction-Combo1" ||chartType === "Influencers-Impact-Analysis" || chartType === "Influencers-Impact-Analysis2" || chartType === "DualAxis-Target" || chartType === "DualAxis-Group" || chartType === "SmoothLine" || chartType === "MultiMeasure-Line" || chartType === "OverLaid-Bar-Line" || chartType === "MultiMeasure-Bar" || chartType === "MultiMeasureH-Bar" || chartType === "trend-Analysis" || chartType === 'Multi-Layered-Bar' || chartType === "Grouped-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "StackedBar" || chartType === "Area" || chartType === "Cumulative-Line" || chartType === "MultiMeasure-Area") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12' >X-" + translateLanguage.Axis_Ticks_Interval + " </label></td>";
            axisprop += "<td><select id='" + chartId + "XaxisRange' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["XaxisRange"] === "undefined" || chartData[chartId]["XaxisRange"] === "0") {
                axisprop += "<option class='gFontFamily gFontSize12' value='0' selected>Default</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' bvalue='0'>Default</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "1") {
                axisprop += "<option class='gFontFamily gFontSize12' value='1' selected>1</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='1'>1</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "2") {
                axisprop += "<option class='gFontFamily gFontSize12' value='2' selected>2</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='2'>2</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "3") {
                axisprop += "<option class='gFontFamily gFontSize12' value='3' selected>3</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='3'>3</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "4") {
                axisprop += "<option class='gFontFamily gFontSize12' value='4' selected>4</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='4'>4</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "5") {
                axisprop += "<option class='gFontFamily gFontSize12' value='5' selected>5</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='5'>5</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "6") {
                axisprop += "<option class='gFontFamily gFontSize12' value='6' selected>6</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='6'>6</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "7") {
                axisprop += "<option class='gFontFamily gFontSize12' value='7' selected>7</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='7'>7</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "8") {
                axisprop += "<option class='gFontFamily gFontSize12' value='8' selected>8</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='8'>8</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "9") {
                axisprop += "<option class='gFontFamily gFontSize12' value='9' selected>9</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='9'>9</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "10") {
                axisprop += "<option class='gFontFamily gFontSize12' value='10' selected>10</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='10'>10</option>";
            }
            axisprop += "</select></td>";
        }

        if (chartType === "Vertical-Bar" || chartType === "Vertical-Negative-Bar" || chartType === "Combo-Horizontal" || chartType === "Horizontal-Bar" || chartType === "Filled-Horizontal" || chartType === "OverLaid-Bar-Line"
                || chartType === "StackedBar" || chartType === "StackedBar-%" || chartType === "StackedBarH-%" || chartType === "Horizontal-StackedBar" || chartType === "MultiMeasure-Bar" || chartType === "MultiMeasureH-Bar"
                || chartType === 'StackedBarLine' || chartType === "Cumulative-Bar" || chartType === "Multi-Layered-Bar" || chartType === "DualAxis-Bar" || chartType === "Veraction-Combo3" || chartType === "Score-vs-Targets"|| chartType === "Area" || chartType === "MultiMeasure-Area"
                || chartType === "Grouped-Bar" || chartType === "GroupedHorizontal-Bar" || chartType === "GroupedStacked-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar%" || chartType === "Grouped-Line" || chartType === "Grouped-MultiMeasureBar"
                || chartType === "Line" || chartType === "Trend-Analysis" || chartType === "SmoothLine" || chartType === "Trend-Analysis" || chartType === "MultiMeasure-Line" || chartType === "MultiMeasureSmooth-Line" || chartType === "Cumulative-Line" || chartType === "Trend-Table-Combo") {

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Y_Axis_Range + "</label></td>";
            axisprop += "<td><select id='" + chartId + "YaxisRangeType' onchange='yaxisrange(\"" + chartId + "\",this.value)' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["yaxisrange"] === "undefined" && chartData[chartId]["yaxisrange"] === "Value") {
                if (typeof chartData[chartId]["yaxisrange"]["YaxisRangeType"] === "undefined" && chartData[chartId]["yaxisrange"]["YaxisRangeType"] === "MinMax") {
                    axisprop += "<option class='gFontFamily gFontSize12' value='MinMax' selected>Min-Max</option>";
                }
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='MinMax'>MinMax</option>";
            }

            if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"]["YaxisRangeType"] === "Default") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Default' selected>Default</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Default'>Default</option>";
            }
            if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"]["YaxisRangeType"] === "Custom") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Custom' selected>Custom</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Custom'>Custom</option>";
            }
            axisprop += "</select></td>";

        }

        if (chartType === "DualAxis-Bar") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Y1_Axis_Range + "</label></td>";
            axisprop += "<td><select id='" + chartId + "Y1axisRangeType' onchange='y1axisrange(\"" + chartId + "\",this.value)' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["y1axisrange"] === "undefined" && chartData[chartId]["y1axisrange"] === "Value") {
                if (typeof chartData[chartId]["y1axisrange"]["Y1axisRangeType"] === "undefined" && chartData[chartId]["y1axisrange"]["Y1axisRangeType"] === "MinMax") {
                    axisprop += "<option class='gFontFamily gFontSize12' value='MinMax' selected>Min-Max</option>";
                }
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='MinMax'>MinMax</option>";
            }

            if (typeof chartData[chartId]["y1axisrange"] != "undefined" && chartData[chartId]["y1axisrange"]["Y1axisRangeType"] === "Default") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Default' selected>Default</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Default'>Default</option>";
            }
            if (typeof chartData[chartId]["y1axisrange"] != "undefined" && chartData[chartId]["y1axisrange"]["Y1axisRangeType"] === "Custom") {
                axisprop += "<option class='gFontFamily gFontSize12' value='Custom' selected>Custom</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='Custom'>Custom</option>";
            }
            axisprop += "</select></td>";
        }


        axisprop += "</tr>";
//Added By Ram
        var YaxisMinValue = 0;
        var YaxisMaxValue = 0;
        var Y1axisMinValue = 0;
        var Y1axisMaxValue = 0;

        if (typeof chartData[chartId]["yaxisrange"] != "undefined" && typeof chartData[chartId]["yaxisrange"]["YaxisRangeType"] != "undefined" && chartData[chartId]["yaxisrange"]["YaxisRangeType"] != "Default" && chartData[chartId]["yaxisrange"]["YaxisRangeType"] != "MinMax") {
            axisprop += "<tr style='display:' id='" + chartId + "yaxisfilter'>";
        } else {
            axisprop += "<tr style='display:none' id='" + chartId + "yaxisfilter'>";
        }
        axisprop += "<td ><label style='width: 75px;white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
        axisprop += "<td ><label style='width: 75px;white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
        axisprop += "<td class='gFontFamily gFontSize12' colspan='6'><table style='border-collapse: separate;border-spacing: 4px 15px;'><tr><td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y-" + translateLanguage.axisMin + "</label></td>";
        if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"] != "") {
            if (typeof chartData[chartId]["yaxisrange"]["axisMin"] != "undefined" && chartData[chartId]["yaxisrange"]["axisMin"] != "") {
                YaxisMinValue = chartData[chartId]["yaxisrange"]["axisMin"];
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin' class='gFontFamily gFontSize12' value = '" + YaxisMinValue + "'></td>";
            } else {
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin' class='gFontFamily gFontSize12'></td>";
            }
        } else {
            axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin' class='gFontFamily gFontSize12'></td>";
        }

        axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y-" + translateLanguage.axisMax + "</label></td>";
        if (typeof chartData[chartId]["yaxisrange"] != "undefined" && chartData[chartId]["yaxisrange"] != "") {
            if (typeof chartData[chartId]["yaxisrange"]["axisMax"] != "undefined" && chartData[chartId]["yaxisrange"]["axisMax"] != "") {
                YaxisMaxValue = chartData[chartId]["yaxisrange"]["axisMax"];
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax' class='gFontFamily gFontSize12' value = '" + YaxisMaxValue + "'></td>";
            } else {
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax' class='gFontFamily gFontSize12'></td>";
            }
        } else {
            axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax' class='gFontFamily gFontSize12'></td>";
        }
        axisprop += "</tr></table></td></tr>";

// for dual axis y2

        if (chartData[chartId]["chartType"] == "DualAxis-Bar"|| chartData[chartId]["chartType"] == "Veraction-Combo3" || chartData[chartId]["chartType"] === "DualAxis-Bar2" || chartData[chartId]["chartType"] === "DualAxis-Group") {
            if (typeof chartData[chartId]["y1axisrange"] != "undefined" && typeof chartData[chartId]["y1axisrange"]["Y1axisRangeType"] != "undefined" && chartData[chartId]["y1axisrange"]["Y1axisRangeType"] != "Default" && chartData[chartId]["y1axisrange"]["Y1axisRangeType"] != "MinMax") {
                axisprop += "<tr id='" + chartId + "Y2axis'>"
            } else {
                axisprop += "<tr style='display:none' id='" + chartId + "Y2axis'>"
            }
            axisprop += "<td ><label style='width: 75px;white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td ><label style='width: 75px;white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td class='gFontFamily gFontSize12' colspan='6'><table><tr><td><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y1-" + translateLanguage.axisMin + "</label></td>";
            if (typeof chartData[chartId]["y1axisrange"] != "undefined" && chartData[chartId]["y1axisrange"] != "") {
                if (typeof chartData[chartId]["y1axisrange"]["axisMin1"] != "undefined" && chartData[chartId]["y1axisrange"]["axisMin1"] != "") {
                    Y1axisMinValue = chartData[chartId]["y1axisrange"]["axisMin1"];
                    axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin1' class='gFontFamily gFontSize12' value = '" + Y1axisMinValue + "'></td>";
                } else {
                    axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin1' class='gFontFamily gFontSize12'></td>";
                }
            } else {
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMin1' class='gFontFamily gFontSize12'></td>";
            }

            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y1-" + translateLanguage.axisMax + "</label></td>";
            if (typeof chartData[chartId]["y1axisrange"] != "undefined" && chartData[chartId]["y1axisrange"] != "") {
                if (typeof chartData[chartId]["y1axisrange"]["axisMax1"] != "undefined" && chartData[chartId]["y1axisrange"]["axisMax1"] != "") {
                    Y1axisMaxValue = chartData[chartId]["y1axisrange"]["axisMax1"];
                    axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax1' class='gFontFamily gFontSize12' value = '" + Y1axisMaxValue + "'></td>";
                } else {
                    axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax1' class='gFontFamily gFontSize12'></td>";
                }
            } else {
                axisprop += "<td><input type ='text' size='6' id='" + chartId + "Y-axisMax1' class='gFontFamily gFontSize12'></td>";
            }
            axisprop += "</tr></table></td></tr>";
        }



        if (chartType === "Horizontal-Bar" || chartType === "Combo-Horizontal" || chartType === "GroupedHorizontal-Bar" || chartType === "Horizontal-StackedBar" || chartType === "Filled-Horizontal") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> Y-" + translateLanguage.Axis_Ticks_Interval + "</label></td>";
            axisprop += "<td><select id='" + chartId + "XaxisRange' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["XaxisRange"] === "undefined" || chartData[chartId]["XaxisRange"] === "0") {
                axisprop += "<option class='gFontFamily gFontSize12' value='0' selected>Default</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='0'>Default</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "1") {
                axisprop += "<option class='gFontFamily gFontSize12' value='1' selected>1</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='1'>1</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "2") {
                axisprop += "<option class='gFontFamily gFontSize12' value='2' selected>2</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='2'>2</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "3") {
                axisprop += "<option class='gFontFamily gFontSize12' value='3' selected>3</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='3'>3</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "4") {
                axisprop += "<option class='gFontFamily gFontSize12' value='4' selected>4</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='4'>4</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "5") {
                axisprop += "<option class='gFontFamily gFontSize12' value='5' selected>5</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='5'>5</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "6") {
                axisprop += "<option class='gFontFamily gFontSize12' value='6' selected>6</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='6'>6</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "7") {
                axisprop += "<option class='gFontFamily gFontSize12' value='7' selected>7</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='7'>7</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "8") {
                axisprop += "<option class='gFontFamily gFontSize12' value='8' selected>8</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='8'>8</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "9") {
                axisprop += "<option class='gFontFamily gFontSize12' value='9' selected>9</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='9'>9</option>";
            }
            if (chartData[chartId]["XaxisRange"] === "10") {
                axisprop += "<option class='gFontFamily gFontSize12' value='10' selected>10</option>";
            } else {
                axisprop += "<option class='gFontFamily gFontSize12' value='10'>10</option>";
            }
            axisprop += "</select></td>";
        }
        axisprop += "</tr>";



        // block 5
        axisprop += "<tr>";
        if (chartType === "Vertical-Negative-Bar" || chartType === "MultiMeasureSmooth-Line" || chartType === "Cumulative-Bar" || chartType === "Multi-Layered-Bar" || chartType === "StackedBarLine" || chartType === "Vertical-Bar" || chartType === "Line" || chartType === "DualAxis-Bar"|| chartType === "Veraction-Combo3" || chartType === "DualAxis-Target" || chartType === "DualAxis-Group" || chartType === "SmoothLine" || chartType === "MultiMeasure-Line" || chartType === "OverLaid-Bar-Line" || chartType === "MultiMeasure-Bar" || chartType === "MultiMeasureH-Bar" || chartType === "Trend-Analysis" || chartType === 'Multi-Layered-Bar' || chartType === "Grouped-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "StackedBar" || chartType === "Area" || chartType === "Cumulative-Line" || chartType === "MultiMeasure-Area") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>X-" + translateLanguage.Axis_Label_Length + " </label></td>";
            axisprop += "<td>";
            var XLable = "10";
            if (typeof chartData[chartId]["editXLable"] !== "undefined" && chartData[chartId]["editXLable"] !== "") {
                XLable = chartData[chartId]["editXLable"];
            }
            axisprop += "<input type='text' id='" + chartId + "editXLable' size='6' class='gFontFamily gFontSize12' value=" + XLable + " >";
            axisprop += "</td>";
        }

        if (chartType === "Horizontal-Bar" || chartType === "Combo-Horizontal" || chartType === "GroupedHorizontal-Bar" || chartType === "Horizontal-StackedBar" || chartType === "Filled-Horizontal") {
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>  </label></td>";
            axisprop += "<td><label style='white-space:nowrap' class='gFontFamily gFontSize12'>Y-" + translateLanguage.Axis_Label_Length + " </label></td>";
            axisprop += "<td>";
            var XLable = "10";
            if (typeof chartData[chartId]["editXLable"] !== "undefined" && chartData[chartId]["editXLable"] !== "") {
                XLable = chartData[chartId]["editXLable"];
            }
            axisprop += "<input type='text' id='" + chartId + "editXLable' size='6' class='gFontFamily gFontSize12' value=" + XLable + ">";
            axisprop += "</td>";
        }
        axisprop += "</tr>";

    }




    axisprop += "</table></div>";
//---------------------------------------------------------------------------
//Added by Ashutosh for reportDrillOnMeasure
if (chartData[chartId]["chartType"] === "Stacked-KPI" || chartData[chartId]["chartType"] === "Column-Pie" || chartData[chartId]["chartType"] === "Bullet-Horizontal") {
  var reportsData;
  var jsonVar ;
  var reportIds ;
  var reportNames; 
  var assignRepIds;
  $.ajax({
        type:'POST',
        async: false,
        url: parent.$("#ctxpath").val() + '/reportViewer.do?reportBy=reportDrillAssignment&reportId=' + parent.$("#graphsId").val(),
        success: function(data){

         reportsData = data;  
      
                                if (reportsData !== 'null') {
                                    jsonVar = eval('(' + reportsData + ')')
                                    reportIds = jsonVar.reportIds;
                                    reportNames = jsonVar.reportNames;
                                    assignRepIds = jsonVar.assignRepIds;
                        }
                    }
                })
}
    var chartProp = "";
    var chartData = JSON.parse($("#chartData").val());
    chartType = chartData[chartId]["chartType"];
    chartProp += "<div id='chartProperties' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    chartProp += "<table  id='chartProperties1' style='border-collapse: separate;border-spacing: 26px 15px;'>";
    
    if(!(chartType==="KPI-Dashboard" ||chartType=== "Scatter-XY" || chartType === "Australia-State-Map" || chartType === "Australia-City-Map" || chartType === "US-City-Map" || chartType === "US-State-Map" ||
            chartType === "Combo-US-Map" || chartType === "Combo-USCity-Map" || chartType === "Combo-Aus-State" || chartType === "Combo-Aus-City")) {
    
    if (!(typeof chartData[chartId]["displayLegends"] === 'undefined' && (chartType === 'Grouped-Table'))) {
        chartProp += "<tr>";
        chartProp += "<td style='font-size:9pt'><label class='gFontFamily gFontSize12' style='white-space:nowrap'> " + translateLanguage.Display_Legends + " </label></td>";
        chartProp += "<td><select id='" + chartId + "displayLegends' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["displayLegends"] === 'undefined' && (chartType === 'Column-Pie' || chartType === 'Filled-Horizontal' || chartType === 'Column-Donut'
                || chartType === 'Pie'  || chartType ==='Combo-MeasureH-Bar'|| chartType ==='Combo-Horizontal' || chartType === 'Donut' || chartType === 'Double-Donut' || chartType === 'Donut-3D' || chartType === 'Pie-3D' || chartType === 'Half-Pie' ||
                chartType === 'MultiMeasure-Line' || chartType === 'MultiMeasureSmooth-Line' || chartType === 'Cumulative-Line' || chartType === 'Half-Donut' || chartType === 'Grouped-Bar' || chartType === "GroupedHorizontal-Bar" || chartType === 'GroupedStacked-Bar'
                || chartType === 'GroupedStackedH-Bar' || chartType === 'Split-Bubble' || chartType === 'GroupedStacked-Bar%' || chartType === 'Grouped-Line' || chartType === 'Grouped-MultiMeasureBar')) {
            chartProp += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
            chartProp += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
        } else {
            if (typeof chartData[chartId]["displayLegends"] === "undefined" || chartData[chartId]["displayLegends"] === "No") {
                chartProp += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
            }
            if (chartData[chartId]["displayLegends"] === "Yes") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
            }
        }
        chartProp += "</select></td>";

        chartProp += "<td ><label class='gFontFamily gFontSize12' style='white-space:nowrap' > " + translateLanguage.Number_of_Legend + " </label></td>";
        chartProp += "<td>";
        var legendNo = "";
        if (typeof chartData[chartId]["legendNo"] !== "undefined" && chartData[chartId]["legendNo"] !== "") {
            legendNo = chartData[chartId]["legendNo"];
        }
        chartProp += "<input type='text' class='gFontFamily gFontSize12' id='" + chartId + "legendNo' size='6' value='" + legendNo + "'>";
        chartProp += "</td>";

        if (chartData[chartId]["chartType"] == "Double-Donut") {
            chartProp += "<td ><label class='gFontFamily gFontSize12' style='white-space:nowrap'> " + translateLanguage.Donut_Chart + " </label></td>";
            chartProp += "<td><select id='" + chartId + "doublePie' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["doublePie"] === "undefined" || chartData[chartId]["doublePie"] === "Double-Donut") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Double-Donut' selected>Double-Donut</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Double-Donut'>Double-Donut</option>";
            }
            if (chartData[chartId]["doublePie"] === "Double-Pie") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Double-Pie' selected>Double-Pie</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Double-Pie'>Double-Pie</option>";
            }
            chartProp += "</select></td>";
        }
        chartProp += "</tr>";

        if (chartData[chartId]["chartType"] == "Pie" || chartData[chartId]["chartType"] == "Half-Pie" || chartData[chartId]["chartType"] == "Pie-3D"
                || chartData[chartId]["chartType"] == "Donut" || chartData[chartId]["chartType"] == "Half-Donut" || chartData[chartId]["chartType"] == "Donut-3D"
                || chartData[chartId]["chartType"] == "Double-Donut" || chartData[chartId]["chartType"] == "Combo-Analysis" || chartData[chartId]["chartType"] == "Column-Donut" || chartData[chartId]["chartType"] == "Column-Pie" || chartData[chartId]["chartType"] == "Centric-Bubble"
                || chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar" || chartData[chartId]["chartType"] == "GroupedStackedH-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-Bar%" || chartData[chartId]["chartType"] == "GroupedStacked-Bar" || chartData[chartId]["chartType"] == "Grouped-Line" || chartData[chartId]["chartType"] == "Grouped-MultiMeasureBar")
        {
            chartProp += "<tr>";
            chartProp += "<td ><label class='gFontFamily gFontSize12' style='white-space:nowrap'> " + translateLanguage.Legends_Color + " </label></td>";
            chartProp += "<td><select id='" + chartId + "colorLegend' class='gFontFamily gFontSize12'  name='Dashname'>";
            //        if (typeof chartData[chartId]["colorLegend"] === "undefined" || chartData[chartId]["colorLegend"] === "Default") {
            if (typeof chartData[chartId]["colorLegend"] === "undefined" || chartData[chartId]["colorLegend"] === "Black") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Black' selected>Black</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Black'>Black</option>";
            }
            if (chartData[chartId]["colorLegend"] === "Default") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Default' selected>Default</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Default'>Default</option>";
            }
            chartProp += "</select></td>";
            chartProp += "<td ><label class='gFontFamily gFontSize12' style='white-space:nowrap'> " + translateLanguage.Legend_Display + " </label></td>";
            chartProp += "<td><select id='" + chartId + "legendLocation' class='gFontFamily gFontSize12' style='font-size:9pt' name='Dashname'>";
            if (chartType === "Pie" || chartType === "Column-Pie" || chartType === "Column-Donut" || chartType === "Combo-Analysis" || chartType === "Double-Donut" || chartType === "Donut") {
                if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Right") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right' selected>Right</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right'>Right</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Bottom") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Outer") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer' selected>Outer</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer'>Outer</option>";
                }
//    }else if(chartType=== "Pie"){
//       if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Right") {
//         chartProp += "<option class='gFontFamily gFontSize12' value='Right' selected>Right</option>";
//    } else {
//        chartProp += "<option class='gFontFamily gFontSize12' value='Right'>Right</option>";
//        }
//       if (chartData[chartId]["legendLocation"] === "Bottom"){
//            chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
//    } else {
//        chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
//            }
//       if (chartData[chartId]["legendLocation"] === "Outer"){
//         chartProp += "<option class='gFontFamily gFontSize12' value='Outer' selected>Outer</option>";
//    } else {
//                chartProp += "<option class='gFontFamily gFontSize12' value='Outer'>Outer</option>";
//        }
//      if (chartData[chartId]["legendLocation"] === "On pie") {
//         chartProp += "<option class='gFontFamily gFontSize12' value='onpie' selected>On Pie</option>";
//    } else {
//                chartProp += "<option class='gFontFamily gFontSize12' value='onpie'>On Pie</option>";
//            }
            } else if (chartType === "Grouped-Bar" || chartType === "GroupedHorizontal-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "Grouped-Line" || chartType === "Grouped-MultiMeasureBar" || chartType === "GroupedStacked-Bar%") {
                if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Right") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right' selected>Right</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right'>Right</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Bottom") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Outer") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer' selected>Outer</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer'>Outer</option>";
                }
            }
            else if (chartType === "Half-Pie" || chartType === "Half-Donut") {
                if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Outer") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer' selected>Outer</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer'>Outer</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Right") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right' selected>Right</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right'>Right</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Bottom") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
                }

            } else {
                if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Bottom") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Right") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right' selected>Right</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Right'>Right</option>";
                }
                if (chartData[chartId]["legendLocation"] === "Outer") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer' selected>Outer</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Outer'>Outer</option>";
                }
            }
            chartProp += "</select></td>";
        }
        chartProp += "</tr>";
        if (!(chartData[chartId]["chartType"] === 'Bubble')) {
            chartProp += "<tr><td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Show_Labels + " </label></td>";
            chartProp += "<td>";
            chartProp += "<select id='" + chartId + "dataDisplay' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["dataDisplay"] === 'undefined' && (chartData[chartId]["chartType"] === 'Combo-Analysis' || chartData[chartId]["chartType"] === "Tree-Map" || chartData[chartId]["chartType"] === "Column-Donut" || chartData[chartId]["chartType"] === 'Column-Pie' || chartData[chartId]["chartType"] === 'Pie' || chartData[chartId]["chartType"] === 'Donut' || chartData[chartId]["chartType"] === 'Double-Donut' || chartData[chartId]["chartType"] === 'Donut-3D' || chartData[chartId]["chartType"] === 'Pie-3D' || chartData[chartId]["chartType"] === 'Half-Pie' || chartData[chartId]["chartType"] === 'Half-Donut'))
            {
                chartProp += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                chartProp += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
            }
            else
            {
                if (typeof chartData[chartId]["dataDisplay"] === "undefined" || chartData[chartId]["dataDisplay"] === "No") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                }

                if (chartData[chartId]["dataDisplay"] === "Yes") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                }
            }
            chartProp += "</select></td>";

            chartProp += "<td style='white-space:nowrap'><label class='gFontFamily gFontSize12'> " + translateLanguage.Data_Label_Type + " </label></td>";
            chartProp += "<td><select id='" + chartId + "valueOf' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["dataDisplay"] === 'undefined' && (chartData[chartId]["chartType"] == "Pie")) {
                if (typeof chartData[chartId]["valueOf"] === "undefined" || chartData[chartId]["valueOf"] === "%-wise") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='%-wise' selected>%-wise</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='%-wise'>%-wise</option>";
                }
                if (chartData[chartId]["valueOf"] === "Absolute") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
                }
                if (chartData[chartId]["valueOf"] === "labelwithval") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithval' selected>Label and Value</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithval'>Label and Value</option>";
                }
                if (chartData[chartId]["valueOf"] === "labelwithcont") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithcont' selected>Label and Contribution %</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithcont'>Label and Contribution %</option>";
                }
                if (chartData[chartId]["valueOf"] === "labelwithvalcont") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithvalcont' selected>Label,Value,Contribution%</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='labelwithvalcont'>Label,Value,Contribution%</option>";
                }

            } else {
                if (typeof chartData[chartId]["valueOf"] === "undefined" || chartData[chartId]["valueOf"] === "%-wise") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='%-wise' selected>%-wise</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='%-wise'>%-wise</option>";
                }
                if (chartData[chartId]["valueOf"] === "Absolute") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
                }
            }
            chartProp += "</select></td>";

//if (typeof chartData[chartId]["legendLocation"] === "undefined" || chartData[chartId]["legendLocation"] === "Outer"){
            chartProp += "<td style='white-space:nowrap'><label class='gFontFamily gFontSize12'> " + translateLanguage.Data_Label_Type + " </label></td>";
            chartProp += "<td><select id='" + chartId + "valueOf1' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["valueOf1"] === "undefined" || chartData[chartId]["valueOf1"] === "Out%-wise") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Out%-wise' selected>Out%-wise</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Out%-wise'>Out%-wise</option>";
            }
            if (chartData[chartId]["valueOf1"] === "In%-wise") {
                chartProp += "<option class='gFontFamily gFontSize12' value='In%-wise' selected>In%-wise</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='In%-wise'>In%-wise</option>";
            }
            chartProp += "</select></td>";
            chartProp += "</tr>";
        }
        chartProp += "<tr>";
        if (!(chartType === "Pie" || chartType === "GroupedStackedH-Bar" || chartType === "Column-Pie" || chartType === "Column-Donut" || chartType === "Combo-Analysis" || chartType === "Pie-3D" || chartType === "Bubble" || chartType === "Half-Pie" || chartType === "Donut" || chartType === "Half-Donut" || chartType === "Donut-3D" || chartType === "Double-Donut"
                || chartType === "Grouped-Bar" || chartType === "Split-Bubble" || chartType === "GroupedHorizontal-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "GroupedStacked-Bar%" || chartType === "Grouped-Line" || chartType === "Grouped-MultiMeasureBar")){
            if (typeof chartData[chartId]["lbColor"] !== 'undefined' && chartData[chartId]["lbColor"] !== '') {
                color = chartData[chartId]["lbColor"];
            } else {
                color = "#ffffff";
            }
            chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12' > " + translateLanguage.Legend_Box_Background + "</label></td>";
            chartProp += "<td><input id='lbcolor' type='color picker' onclick='moreColors(\"" + chartId + "\",\"lbcolor\")' style='width:30px;background-color:" + color + "' ></td>";
            chartProp += "</td>";

            chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12' >" + translateLanguage.Legend_Box_Position + "</label></td>";
            chartProp += "<td><select id='" + chartId + "lbPosition' class='gFontFamily gFontSize12'  name='lbPosition'>";
            if (chartData[chartId]["chartType"] == "Bubble") {
                if (typeof chartData[chartId]["lbPosition"] === 'undefined' || chartData[chartId]["lbPosition"] === "top") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='top' selected>Top(table)</option>";
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomcenter'>Bottom</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='top'>Top(table)</option>";
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomcenter' selected>Bottom</option>";
                }
            }
            else {
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "top") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='top' selected>Top(table)</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='top'>Top(table)</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "topright") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topright' selected>Top-Right</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topright'>Top-Right</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "topcenter") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topcenter' selected>Top-Center</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topcenter'>Top-Center</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "topleft") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topleft' selected>Top-Left</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='topleft'>Top-Left</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "bottomright") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomright' selected>Bottom-Right</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomright'>Bottom-Right</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "bottomcenter") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomcenter' selected>Bottom-Center</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomcenter'>Bottom-Center</option>";
                }
                if (typeof chartData[chartId]["lbPosition"] !== 'undefined' && chartData[chartId]["lbPosition"] === "bottomleft") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomleft' selected>Bottom-Left</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='bottomleft'>Bottom-Left</option>";
                }
            }
            chartProp += "</select>";
            chartProp += "</td>";
            chartProp += "</tr>";
            chartProp += "<tr>";
            chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Legend_Box_Border + " </label></td>";
            chartProp += "<td><select id='" + chartId + "legendBoxBorder' class='gFontFamily gFontSize12' name='Dashname'>";
            if (typeof chartData[chartId]["legendBoxBorder"] === "undefined" || chartData[chartId]["legendBoxBorder"] === "Dotted") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Dotted' selected>Dotted</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Dotted'>Dotted</option>";
            }
            if (chartData[chartId]["legendBoxBorder"] === "Solid") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Solid' selected>Solid</option>";
            }
            else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Solid'>Solid</option>";
            }
            if (chartData[chartId]["legendBoxBorder"] === "None") {
                chartProp += "<option class='gFontFamily gFontSize12' value='None' selected>None</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='None'>None</option>";
            }
            chartProp += "</select>";
            chartProp += "</td>";
        }
        if (!(chartType === "Combo-Analysis" || chartType === "Column-Pie" || chartType === "Column-Donut")) {
            if (chartType === "Pie" || chartType === "Bubble" || chartType === "Pie-3D" || chartType === "Half-Pie" || chartType === "Donut" || chartType === "Half-Donut" || chartType === "Donut-3D" || chartType === "Double-Donut"
                    || chartType === "Grouped-Bar" || chartType === "Split-Bubble" || chartType === "GroupedHorizontal-Bar" || chartType === "GroupedStacked-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar%" || chartType === "Grouped-Line" || chartType === "Grouped-MultiMeasureBar") {
                if (typeof chartData[chartId]["showViewBy"] != 'undefined' && chartData[chartId]["showViewBy"] == 'Y') {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy + " </td><td><input type='checkbox' id='" + chartId + "showViewBy' checked></td>";
                } else {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy + " </td><td><input type='checkbox' id='" + chartId + "showViewBy'></td>";
                }
            } else {
                if(chartType==="Combo-Horizontal"){
                if (typeof chartData[chartId]["showViewByinLBox"] === "undefined" || chartData[chartId]["showViewByinLBox"] === 'Y') {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy_in_Legend_Box + "</td><td><input type='checkbox' id='" + chartId + "showViewByinLBox' checked></td>";
                }else {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy_in_Legend_Box + "</td><td><input type='checkbox' id='" + chartId + "showViewByinLBox'></td>";
                }
                }else{
                if (typeof chartData[chartId]["showViewByinLBox"] != 'undefined' && chartData[chartId]["showViewByinLBox"] == 'Y') {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy_in_Legend_Box + "</td><td><input type='checkbox' id='" + chartId + "showViewByinLBox' checked></td>";
                }else {
                    chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Show_ViewBy_in_Legend_Box + "</td><td><input type='checkbox' id='" + chartId + "showViewByinLBox'></td>";
                }
            }
        }
        }
        chartProp += "</tr>";

        chartProp += "<tr>";
        if (chartType === "Pie" || chartType === "Column-Pie" || chartType === "Column-Donut" || chartType === "Combo-Analysis" || chartType === "Pie-3D" || chartType === "Bubble"
                || chartType === "Half-Pie" || chartType === "Donut" || chartType === "Half-Donut" || chartType === "Tree-Map" || chartType === "Donut-3D" || chartType === "Double-Donut"
                || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "GroupedStacked-Bar%" || chartType === "Grouped-Line"
        || chartType==="US-State-Map" || chartType==="US-City-Map" || chartType==="India-Map") {
            var labelColor = "#d8d47d";
            if (typeof chartData[chartId]["labelColor"] !== "undefined" && chartData[chartId]["labelColor"] != "") {
                labelColor = chartData[chartId]["labelColor"];
            }
            chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Data_Labels_Color + "</td><td> <input style='background-color:" + labelColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='labelColor' readonly></td>";
        }

        chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Legend_Font_Size + " </label></td>";
        chartProp += "<td><select id='" + chartId + "legendFontSize'  name='Dashname'>";
        if (typeof chartData[chartId]["legendFontSize"] === "undefined" || chartData[chartId]["legendFontSize"] === "Select") {
            chartProp += "<option class='gFontFamily gFontSize12' value='Select' selected>Select</option>";
        } else {
            chartProp += "<option class='gFontFamily gFontSize12' value='Select'>Select</option>";
        }
        if (chartType === "Combo-Analysis" || chartType === "Column-Pie" || chartType === "Column-Donut") {
            for (var font = 6; font <= 13; font++) {
                if (parseInt(chartData[chartId]["legendFontSize"]) === font) {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "' selected>" + font + "</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "'>" + font + "</option>";
                }
            }
        } else {
            for (var font = 8; font <= 20; font++) {
                if (parseInt(chartData[chartId]["legendFontSize"]) === font) {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "' selected>" + font + "</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "'>" + font + "</option>";
                }
            }
        }
        chartProp += "</select>";
        chartProp += "</td>";
        chartProp += "</tr>";

        if (!(chartType === "Pie" || chartType === "Column-Pie" || chartType === "Column-Donut" || chartType === "Combo-Analysis" || chartType === "Pie-3D" || chartType === "Half-Pie" || chartType === "Donut" || chartType === "Half-Donut" || chartType === "Donut-3D" || chartType === "Double-Donut" ||
                chartType === "Aster" || chartType === "Bubble")) {
            chartProp += "<tr>";
            chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Legend_Print_Type + " </label></td>";
            chartProp += "<td><select id='" + chartId + "legendPrintType' style='font-size:9pt'  name='Dashname'>";
            if (chartData[chartId]["chartType"] == "Horizontal-Bar" || chartData[chartId]["chartType"] == "Combo-Horizontal" || chartData[chartId]["chartType"] == "StackedBarH-%" || chartData[chartId]["chartType"] == "GroupedStackedH-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar" || chartData[chartId]["chartType"] == "Horizontal-StackedBar" || chartData[chartId]["chartType"] == "Filled-Horizontal") {
                if (typeof chartData[chartId]["legendPrintType"] === "undefined" || chartData[chartId]["legendPrintType"] === "Slanted") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Slanted' selected>Slanted</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Slanted'>Slanted</option>";
                }
                if (chartData[chartId]["legendPrintType"] === "Horizontal") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Horizontal' selected>Horizontal</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Horizontal'>Horizontal</option>";
                }
            } else {
                if (typeof chartData[chartId]["legendPrintType"] === "undefined" || chartData[chartId]["legendPrintType"] === "Slanted") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Slanted' selected>Slanted</option>";
                }
                else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Slanted'>Slanted</option>";
                }
                if (chartData[chartId]["legendPrintType"] === "Vertical") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Vertical' selected>Vertical</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Vertical'>Vertical</option>";
                }
                if (chartData[chartId]["legendPrintType"] === "Alternate") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Alternate' selected>Alternate</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Alternate'>Alternate</option>";
                }
                if (chartData[chartId]["legendPrintType"] === "Horizontal") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Horizontal' selected>Horizontal</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Horizontal'>Horizontal</option>";
                }
                chartProp += "</select>";
            }
            chartProp += "</td>";



            chartProp += "<td ><label class='gFontFamily gFontSize12'> 1stMsr " + translateLanguage.Label_Display + " </label></td>";
            chartProp += "<td><select id='" + chartId + "LabelPos' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["LabelPos"] === "undefined" || chartData[chartId]["LabelPos"] === "Top") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Top' selected>Top</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Top'>Top</option>";
            }
            if (chartData[chartId]["LabelPos"] === "Bottom") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Bottom' selected>Bottom</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Bottom'>Bottom</option>";
            }
            if (!(chartType === 'Line' || chartType === 'SmoothLine' || chartType === 'Trend-Analysis')) {
                if (chartData[chartId]["LabelPos"] === "Center") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Center' selected>Center</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Center'>Center</option>";
                }
                if (chartData[chartId]["LabelPos"] === "Bar-Center") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bar-Center' selected>Bar-Center</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='Bar-Center'>Bar-Center</option>";
                }
                if (chartData[chartId]["LabelPos"] === "OnRight-Bar") {
                    chartProp += "<option class='gFontFamily gFontSize12' value='OnRight-Bar' selected>OnRight-Bar</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='OnRight-Bar'>OnRight-Bar</option>";
                }
                if (chartType !== 'GroupedStacked-Bar' && chartType !== 'GroupedStackedH-Bar' && chartType !== 'GroupedStacked-Bar%') {
                    if (chartData[chartId]["LabelPos"] === "Top-Bar") {
                        chartProp += "<option class='gFontFamily gFontSize12' value='Top-Bar' selected>Top-Bar</option>";
                    } else {
                        chartProp += "<option class='gFontFamily gFontSize12' value='Top-Bar'>Top-Bar</option>";
                    }
                    if (chartData[chartId]["LabelPos"] === "Bottom-Bar") {
                        chartProp += "<option class='gFontFamily gFontSize12' value='Bottom-Bar' selected>Bottom-Bar</option>";
                    } else {
                        chartProp += "<option class='gFontFamily gFontSize12' value='Bottom-Bar'>Bottom-Bar</option>";
                    }
                }
            }
            chartProp += "</select></td>";
            chartProp += "</tr>";
        }

        if (chartType === 'Vertical-Bar' || chartType === 'Trend-Combo' || chartType === 'StackedBarH-%' || chartType === 'Filled-Horizontal' || chartType === 'Combo-Horizontal' || chartType === 'Horizontal-Bar' || chartType === 'Bubble' || chartType === 'Line' || chartType === 'SmoothLine' || chartType === 'MultiMeasure-Line' || chartType === 'MultiMeasureSmooth-Line' || chartType === 'OverLaid-Bar-Line'
                || chartType === 'StackedBar' || chartType === 'StackedBar-%' || chartType === 'Horizontal-StackedBar' || chartType === 'StackedBarLine' || chartType === 'Cumulative-Line' || chartType === 'Cumulative-Bar' || chartType === 'Multi-Layered-Bar' || chartType === 'DualAxis-Bar' || chartType === 'Area' || chartType === 'MultiMeasure-Area'
                || chartType === 'MultiMeasure-Bar' || chartType === "MultiMeasureH-Bar" || chartType === 'Multi-Layered-Bar' || chartType === "Grouped-Bar" || chartType === "GroupedHorizontal-Bar" || chartType === "GroupedStackedH-Bar" || chartType === "GroupedStacked-Bar" || chartType === "GroupedStacked-Bar%" || chartType === "GroupedStackedH-Bar%" || chartType === "Grouped-Line" || chartType === "Grouped-MultiMeasureBar") {
            chartProp += "<tr>";
            chartProp += "<td ><label class='gFontFamily gFontSize12' style='font:white-space:nowrap'> " + translateLanguage.X_AxisFont + "</label></td>";
            chartProp += "<td>";
            var xAxisFont = "10";
            if (typeof chartData[chartId]["xAxisFont"] !== "undefined" && chartData[chartId]["xAxisFont"] !== "") {
                xAxisFont = chartData[chartId]["xAxisFont"];
            }
            chartProp += "<input type='text' id='" + chartId + "xAxisFont' class='gFontFamily gFontSize12' size='6' value=" + xAxisFont + ">";
            chartProp += "</td>";

            chartProp += "<td ><label class='gFontFamily gFontSize12' style='font:white-space:nowrap'> " + translateLanguage.Y_AxisFont + "</label></td>";
            chartProp += "<td>";
            var yAxisFont = "10";
            if (typeof chartData[chartId]["yAxisFont"] !== "undefined" && chartData[chartId]["yAxisFont"] !== "") {
                yAxisFont = chartData[chartId]["yAxisFont"];
            }
            chartProp += "<input type='text' id='" + chartId + "yAxisFont' class='gFontFamily gFontSize12' size='6' value=" + yAxisFont + ">";
            chartProp += "</td>";
            chartProp += "</tr>";
        }

        if (chartType === "Line" || chartType === 'Trend-Analysis' || chartType === "MultiMeasure-Line") {
            chartProp += "<tr>";
            chartProp += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Line_Symbol + " </label></td>";
            chartProp += "<td><select id='" + chartId + "lineSymbol' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["lineSymbol"] === "undefined" || chartData[chartId]["lineSymbol"] === "Hollow") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Hollow' selected>Hollow</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Hollow'>Hollow</option>";
            }
            if (chartData[chartId]["lineSymbol"] === "Fill") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Fill' selected>Fill</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Fill'>Fill</option>";
            }
            chartProp += "</select></td>";

            chartProp += "<td ><label class='gFontFamily gFontSize12'> " + translateLanguage.Line_Symbol_Format + "</label></td>";
            chartProp += "<td><select id='" + chartId + "lineSymbol1' class='gFontFamily gFontSize12'  name='Dashname'>";
            if (typeof chartData[chartId]["lineSymbol1"] === "undefined" || chartData[chartId]["lineSymbol1"] === "Circle") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Circle' selected>Circle</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Circle'>Circle</option>";
            }
//if(!(chartType=== "MultiMeasure-Line") ){
            if (chartData[chartId]["lineSymbol1"] === "Rectangle") {
                chartProp += "<option class='gFontFamily gFontSize12' value='Rectangle' selected>Rectangle</option>";
            } else {
                chartProp += "<option class='gFontFamily gFontSize12' value='Rectangle'>Rectangle</option>";
            }
//}
            chartProp += "</select></td>";
            chartProp += "</tr>";
        }

        chartProp += "<tr>";
        chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Labels_Font_Size + " </label></td>";
        chartProp += "<td><select id='" + chartId + "labelFSize' class='gFontFamily gFontSize12'  name='Dashname'>";
        if (typeof chartData[chartId]["labelFSize"] === "undefined" || chartData[chartId]["labelFSize"] === "Select") {
            chartProp += "<option class='gFontFamily gFontSize12' value='Select' selected>Select</option>";
        } else {
            chartProp += "<option class='gFontFamily gFontSize12' value='Select'>Select</option>";
        }
        if (chartData[chartId]["chartType"] == "Combo-Analysis") {
            for (var font = 6; font <= 13; font++) {
                if (parseInt(chartData[chartId]["labelFSize"]) === font) {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "' selected>" + font + "</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "'>" + font + "</option>";
                }
            }
        } else {
            for (var font = 6; font <= 20; font++) {
                if (parseInt(chartData[chartId]["labelFSize"]) === font) {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "' selected>" + font + "</option>";
                } else {
                    chartProp += "<option class='gFontFamily gFontSize12' value='" + font + "'>" + font + "</option>";
                }
            }
        }
        chartProp += "</select>";
        chartProp += "</td>";
    }
    if (chartData[chartId]["chartType"] === "Table" || chartData[chartId]["chartType"] === "Grouped-Table") {
        chartProp += "<td class='gFontFamily gFontSize12'>";
        var TableFontColor = "#000";
        if (typeof chartData[chartId]["TableFontColor"] !== "undefined" && chartData[chartId]["TableFontColor"] != "") {
            TableFontColor = chartData[chartId]["TableFontColor"];
        }
        if (typeof chartData[chartId]["TableFontColor"] !== "undefined" && chartData[chartId]["TableFontColor"] != "") {
            chartProp += "" + translateLanguage.Table_Font_Color + " </td><td> <input style='background-color:" + TableFontColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='TableFontColor'>";
        } else {
            chartProp += "" + translateLanguage.Table_Font_Color + " </td><td> <input style='background-color:" + TableFontColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='TableFontColor' >";
        }
        chartProp += "</td>";
    }
    if (chartData[chartId]["chartType"] === "Grouped-Table") {
        chartProp += "<td class='gFontFamily gFontSize12'>";
        var HeadingBackGround = "rgb(227, 243, 222)";
        if (typeof chartData[chartId]["HeadingBackGround"] !== "undefined" && chartData[chartId]["HeadingBackGround"] != "") {
            HeadingBackGround = chartData[chartId]["HeadingBackGround"];
        }
        if (typeof chartData[chartId]["HeadingBackGround"] !== "undefined" && chartData[chartId]["HeadingBackGround"] != "") {
            chartProp += "" + translateLanguage.Heading_BackGround_Color + " </td><td> <input style='background-color:" + HeadingBackGround + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='HeadingBackGround'>";
        } else {
            chartProp += "" + translateLanguage.Heading_BackGround_Color + " </td><td> <input style='background-color:" + HeadingBackGround + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='HeadingBackGround' >";
        }
        chartProp += "</td>";

        chartProp += "<td class='gFontFamily gFontSize12'>";
        var HeadingColor = "#000";
        if (typeof chartData[chartId]["HeadingColor"] !== "undefined" && chartData[chartId]["HeadingColor"] != "") {
            HeadingColor = chartData[chartId]["HeadingColor"];
        }
        if (typeof chartData[chartId]["HeadingColor"] !== "undefined" && chartData[chartId]["HeadingColor"] != "") {
            chartProp += "" + translateLanguage.Heading_Font_Color + " </td><td> <input style='background-color:" + HeadingColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='HeadingColor'>";
        } else {
            chartProp += "" + translateLanguage.Heading_Font_Color + " </td><td> <input style='background-color:" + HeadingColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='HeadingColor' >";
        }
        chartProp += "</td>";

    }

    if (chartData[chartId]["chartType"] == "Double-Donut") {
        chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Double_Donut_Lable + "</label></td>";
        chartProp += "<td><select id ='" + chartId + "dDLable'>";
        if (typeof chartData[chartId]["dDLable"] === "undefined" || chartData[chartId]["dDLable"] === "Default") {
            chartProp += "<option class='gFontFamily gFontSize12' value='Default' selected>Default</option>";
        } else {
            chartProp += "<option class='gFontFamily gFontSize12' value='Default'>Default</option>";
        }
        if (chartData[chartId]["dDLable"] === "InnerValue") {
            chartProp += "<option class='gFontFamily gFontSize12' value='InnerValue' selected>InnerValue</option>";
        } else {
            chartProp += "<option class='gFontFamily gFontSize12' value='InnerValue'>InnerValue</option>";
        }
        if (chartData[chartId]["dDLable"] === "OuterValue") {
            chartProp += "<option class='gFontFamily gFontSize12' value='OuterValue' selected>OuterValue</option>";
        } else {
            chartProp += "<option class='gFontFamily gFontSize12' value='OuterValue'>OuterValue</option>";
        }
        chartProp += "</select></td>";
    }
    chartProp += "</tr>";

    chartProp += "<tr>";
    if (chartType === 'Top-Analysis' || chartType === 'Combo-Analysis' || chartType === 'World-Top-Analysis') {
        chartProp += "<td style='font-size:9pt'><label style='font: 11px verdana;white-space:nowrap'>" + translateLanguage.Prev_Period_Display + " </label></td>";
        chartProp += "<td><select id='" + chartId + "prevPeriodDisplay' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["prevPeriodDisplay"] === "undefined" || chartData[chartId]["prevPeriodDisplay"] === "multi") {
            chartProp += "<option value='multi' selected>Multi-Line</option>";
        } else {
            chartProp += "<option value='multi'>Multi-Line</option>";
        }
        if (typeof chartData[chartId]["prevPeriodDisplay"] !== "undefined" && chartData[chartId]["prevPeriodDisplay"] === "single") {
            chartProp += "<option value='single' selected>Single-Line</option>";
        } else {
            chartProp += "<option value='single'>Single-Line</option>";
        }
        chartProp += "</select>";
        chartProp += "</td>";
    }
    if (chartType === 'Horizontal-Bar' || chartType === 'Filled-Horizontal' || chartType === 'Pie' || chartType === 'Donut' || chartType === 'Bubble' || chartType === 'Column-Pie' || chartType === 'Column-Donut') {
        chartProp += "<td style='font-size:9pt'><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Table_on_Chart + "</label></td>";
        chartProp += "<td><select id='" + chartId + "circularChartTab' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["circularChartTab"] === "undefined" || chartData[chartId]["circularChartTab"] === "No") {
            chartProp += "<option value='No' selected>No</option>";
        } else {
            chartProp += "<option value='No'>No</option>";
        }
        if (chartData[chartId]["circularChartTab"] === "Yes") {
            chartProp += "<option value='Yes' selected>Yes</option>";
        } else {
            chartProp += "<option value='Yes'>Yes</option>";
        }
        chartProp += "</select></td>";
    }
    chartProp += "</tr>";


    if (chartType === 'Top-Analysis') {
        chartProp += "<tr>";
        chartProp += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'> " + translateLanguage.Title_Font_Size + " </label></td>";
        chartProp += "<td>";
        var titleFontSize = "13";
        if (typeof chartData[chartId]["titleFontSize"] !== "undefined" && chartData[chartId]["titleFontSize"] !== "") {
            titleFontSize = chartData[chartId]["titleFontSize"];
        }
        chartProp += "<input type='text' id='" + chartId + "titleFontSize' size='6' class='gFontFamily gFontSize12' value='" + titleFontSize + "'>";

        chartProp += "</td>";
        var titleColor = "#6AC8E8";
        if (typeof chartData[chartId]["titleColor"] !== "undefined" && chartData[chartId]["titleColor"] != "") {
            titleColor = chartData[chartId]["titleColor"];
        }
        chartProp += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Title_Color + " </td><td> <input style='background-color:" + titleColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='titleColor' readonly></td>";

        chartProp += "<td style='font-size:9pt'><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Change_Percent + "</label></td>";
        chartProp += "<td><select id='" + chartId + "changPercentArrow' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[chartId]["changPercentArrow"] === "undefined" || chartData[chartId]["changPercentArrow"] === "Without-Arrow") {
            chartProp += "<option value='Without-Arrow' selected>Without-Arrow</option>";
        } else {
            chartProp += "<option value='Without-Arrow'>Without-Arrow</option>";
        }
        if (chartData[chartId]["changPercentArrow"] === "With-Arrow") {
            chartProp += "<option value='With-Arrow' selected>With-Arrow</option>";
        } else {
            chartProp += "<option value='With-Arrow'>With-Arrow</option>";
        }
        chartProp += "</select></td>";
        chartProp += "</tr>";
    }

}
    if (chartType === "Australia-State-Map" || chartType === "Australia-City-Map" || chartType === "US-City-Map" || chartType === "US-State-Map" ||
            chartType === "Combo-US-Map" || chartType === "Combo-USCity-Map" || chartType === "Combo-Aus-State" || chartType === "Combo-Aus-City") {
        chartProp += "<tr>";
        chartProp += "<td style='font-size:9pt'><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Map_Type + "</label></td>";
        chartProp += "<td><select id='" + chartId + "usBar' style='font-size:9pt'  name='Dashname'>";
        if (chartType === "Australia-State-Map" || chartType === "US-State-Map" || chartType === "Combo-US-Map" || chartType === "Combo-Aus-State") {
            if (typeof chartData[chartId]["usBar"] === "undefined" || chartData[chartId]["usBar"] === "Fill") {
                chartProp += "<option value='Fill' selected>Fill</option>";
            } else {
                chartProp += "<option value='Fill'>Fill</option>";
            }
            if (chartData[chartId]["usBar"] === "Bar") {
                chartProp += "<option value='Bar' selected>Bar</option>";
            } else {
                chartProp += "<option value='Bar'>Bar</option>";
            }
            } else {
            if (typeof chartData[chartId]["usBar"] === "undefined" || chartData[chartId]["usBar"] === "Bar") {
                chartProp += "<option value='Bar' selected>Bar</option>";
            } else {
                chartProp += "<option value='Bar'>Bar</option>";
            }
            if (chartData[chartId]["usBar"] === "Circle") {
                chartProp += "<option value='Circle' selected>Circle</option>";
            } else {
                chartProp += "<option value='Circle'>Circle</option>";
            }
        }
        chartProp += "</select></td>";
        chartProp += "</tr>";
    }




    chartProp += "</table></div>";

//----------------------------------------------------------------------
    chartType = chartData[chartId]["chartType"];
    measures = chartData[chartId]["meassures"];
    var measureProps = "";
    var propsNames = [];
    propsNames.push("Number format");
    propsNames.push("Number rounding");
    propsNames.push("Prefix");
    propsNames.push("Suffix");
    propsNames.push("Prefix Font Size");
    propsNames.push("Suffix Font Size");
    if (chartType === 'Vertical-Bar' || chartType === 'Trend-Table-Combo' || chartType === 'Trend-Combo' || chartType === 'StackedBarH-%' || chartType === 'Vertical-Negative-Bar' || chartType === 'Filled-Horizontal' || chartType === 'Combo-Horizontal' || chartType === 'Horizontal-Bar' || chartType === 'Bubble' || chartType === 'Line' || chartType === 'SmoothLine' || chartType === 'MultiMeasure-Line' || chartType === 'MultiMeasureSmooth-Line' || chartType === 'OverLaid-Bar-Line'
            || chartType === 'StackedBar' || chartType === 'StackedBar-%' || chartType === 'Horizontal-StackedBar' || chartType === 'StackedBarLine' || chartType === 'Cumulative-Line' || chartType === 'Cumulative-Bar' || chartType === 'Multi-Layered-Bar' || chartType === 'DualAxis-Bar'|| chartType === 'Veraction-Combo3'
            || chartType === 'DualAxis-Group' || chartType === 'DualAxis-Target' || chartType === 'MultiMeasure-Bar' || chartType === "MultiMeasureH-Bar" || chartType === 'Multi-Layered-Bar' || chartType === "Combo-MeasureH-Bar") {
        propsNames.push("Measure Alias");
        propsNames.push("Show Labels");
        propsNames.push("Measure Label Length");
        propsNames.push("Average line on");
        propsNames.push("Adhoc Target Value");
        propsNames.push("Data Label Type");
        propsNames.push("Measure Filter");
        propsNames.push("Label Font Color");//14
        propsNames.push("Sort On Measure");//10
        propsNames.push("Tooltip Display Type");//15
        propsNames.push("Measure Filter On Slider");//16
    }
    if (chartType === 'MultiMeasure-Bar' || chartType === "MultiMeasureH-Bar" || chartType === 'StackedBar' || chartType === 'Trend-Combo' || chartType === 'Cumulative-Bar' || chartType === 'Cumulative-Line' || chartType === 'MultiMeasure-Line' || chartType === 'MultiMeasureSmooth-Line') {
        propsNames.push("Measure in chart");//measure length for  measure charts
    }
    if (chartType === 'MultiMeasure-Line' || chartType === 'StackedBar-%' || 
            chartType === 'StackedBarH-%' || chartType === 'Horizontal-StackedBar' 
            || chartType === 'Cumulative-Bar' || chartType === 'Cumulative-Line' 
            || chartType === 'Multi-Layered-Bar' 
            || chartType === 'MultiMeasure-Bar' || chartType === "MultiMeasureH-Bar" 
            || chartType === 'MultiMeasureSmooth-Line' || chartType === 'StackedBar' 
            || chartType === 'StackedBarLine' || chartType === "Combo-MeasureH-Bar") {
        propsNames.push("Data Label Display");
    }
    if (chartData[chartId]["chartType"] == "Stacked-KPI" || chartData[chartId]["chartType"] == "Trend-KPI" || chartData[chartId]["chartType"] == "KPI-Bar" || chartData[chartId]["chartType"] == "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1") {
        propsNames.push("Sort On Measure");
    }
    if (chartData[chartId]["chartType"] === "Stacked-KPI" || chartData[chartId]["chartType"] === "Trend-KPI") {
        propsNames.push("Measures Alias");
        propsNames.push("Measure Name Font Size");
        propsNames.push("Measure Value Font Size");
        propsNames.push("Comparative Value Font Size");
        propsNames.push("Measure Name Font Color");
        propsNames.push("Measure Value Font Color");
        propsNames.push("Report Drill on Measure");
    }
    if (chartData[chartId]["chartType"] === "Bullet-Horizontal" || chartData[chartId]["chartType"] === "Column-Pie") {
        propsNames.push("Measures Alias");
        propsNames.push("Measure Name Font Size");
        propsNames.push("Measure Value Font Size");
        propsNames.push("Ticks Font Size");
        propsNames.push("Measure Name Font Color");
        propsNames.push("Measure Value Font Color");
//        propsNames.push("Sort On Measure");
        propsNames.push("Report Drill on Measure");
    }
    if (chartType === 'Table' || chartType === 'Bar-Table') {
        propsNames.push("Measures Alias");
        propsNames.push("Measures Name Alignment");
        propsNames.push("Measure Value Alignment");
        propsNames.push("Measure Filter");
        propsNames.push("Sort On Measure");//10
    }
    if (chartType === 'Pie') {
        propsNames.push("Measure Filter On Slider");//16
    }
    measureProps += "<div id='measureProps' class='tag-link-properties' style='width:100%; display:none; margin-left: 1%;'>";
    measureProps += "<table  id='measureProps1' style='border-collapse: separate;border-spacing: 26px 15px;'>";
    measureProps += "<tr>";
    measureProps += "<th>";
    measureProps += "</th>";
    if (isMultiMeasure(chartData[chartId]["chartType"])) {
        for (i in measures) {
            measureProps += "<th class='gFontFamily gFontSize12' style='font-weight: bold;text-align:left'>" + measures[i] + "</th>";
        }
        measureProps += "</tr>";
        for (i in propsNames) {
            if (chartData[chartId]["chartType"] === 'Vertical-Bar' || chartData[chartId]["chartType"] === 'Vertical-Negative-Bar' || chartData[chartId]["chartType"] === 'Combo-MeasureH-Bar' || chartData[chartId]["chartType"] === 'Horizontal-Bar' || chartData[chartId]["chartType"] === 'Combo-Horizontal' || chartData[chartId]["chartType"] === 'SmoothLine' || chartData[chartId]["chartType"] === 'Line' || chartData[chartId]["chartType"] === 'Trend-Analysis' || chartData[chartId]["chartType"] === 'Bubble') {
                if (!(i == 6 || i == 8 || i == 9 || i == 12 || i == 13 || i == 14 || i == 15|| i == 16)) {//measure alias n length for single measure charts
                    continue;
                }
            }
            if (chartType === 'Filled-Horizontal') {
                if (!(i == 6 || i == 8 || i == 9 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15|| i == 16)) {
                    continue;
                }
            }
            if (chartType === 'Table' || chartType === 'Bar-Table') {
                if (!(i == 0 || i == 1 || i == 6 || i == 7 || i == 8 || i == 9 || i == 10)) {
                    continue;
                }
            }
                if(chartType=== 'Pie'){
            if((i==0||i==1||i==2||i==3||i==4||i==5)){
                continue;
            }
        }
         if(chartType=== 'Combo-Horizontal'){
            if(i==16){
                continue;
            }
        }
            if (chartData[chartId]["chartType"] == 'Bullet-Horizontal') {
                if (!(i == 6 || i == 7 || i == 8 || i == 9 || i == 10 || i == 11 || i==12 )) {
                    continue;
                }
            }
            if (chartType === 'Vertical-Bar' || chartType === 'Vertical-Negative-Bar' || chartType === 'Filled-Horizontal' || chartType === 'Horizontal-Bar' || chartType === 'Bubble' || chartType === 'Line' || chartType === 'SmoothLine' || chartType === 'MultiMeasure-Line' || chartType === 'Combo-Horizontal' || chartType === 'MultiMeasureSmooth-Line' || chartType === 'OverLaid-Bar-Line'
                    || chartType === 'StackedBar' || chartType === 'Trend-Analysis' || chartType === 'StackedBarH-%' || chartType === 'StackedBar-%' || chartType === 'Horizontal-StackedBar' || chartType === 'StackedBarLine' || chartType === 'Cumulative-Bar' || chartType === 'Multi-Layered-Bar' || chartType === 'DualAxis-Bar'|| chartType === 'Veraction-Combo3'
                    || chartType === 'DualAxis-Group' || chartType === 'Cumulative-Line' || chartType === 'DualAxis-Target' || chartType === 'MultiMeasure-Bar' || chartType === "MultiMeasureH-Bar" || chartType === 'Multi-Layered-Bar') {
                if ((i == 2 || i == 3 || i == 4 || i == 5)) {//measure alias n length for single measure charts
                    continue;
                }
            }

            measureProps += "<tr>";
            measureProps += "<td class='gFontFamily gFontSize12'>" + propsNames[i] + "</td>";
            for (var j in measures) {
                var propHtml = "";
                if (propsNames[i] == "Number format") {
                    propHtml += "<td><select id='" + chartId + "numberFormatX" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (typeof chartData[chartId]["numberFormat"] === "undefined" || typeof chartData[chartId]["numberFormat"][measures[j]] === "undefined" || chartData[chartId]["numberFormat"][measures[j]] === "Auto") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Auto' selected>Auto</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Auto'>Absolute</option>";
                    }
                    if (typeof chartData[chartId]["numberFormat"] != "undefined" && typeof chartData[chartId]["numberFormat"][measures[j]] != "undefined" && chartData[chartId]["numberFormat"][measures[j]] === "Absolute") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
                    }
                    if (typeof chartData[chartId]["numberFormat"] != 'undefined' && typeof chartData[chartId]["numberFormat"][measures[j]] != 'undefined' && chartData[chartId]["numberFormat"][measures[j]] === "K") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='K' selected>Thousand</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='K'>Thousand</option>";
                    }
                    if (typeof chartData[chartId]["numberFormat"] != 'undefined' && typeof chartData[chartId]["numberFormat"][measures[j]] != 'undefined' && chartData[chartId]["numberFormat"][measures[j]] === "M") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='M' selected>Million</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='M'>Million</option>";
                    }
                    if (typeof chartData[chartId]["numberFormat"] != 'undefined' && typeof chartData[chartId]["numberFormat"][measures[j]] != 'undefined' && chartData[chartId]["numberFormat"][measures[j]] === "Cr") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Cr' selected>Crore</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Cr'>Crore</option>";
                    }
                    if (typeof chartData[chartId]["numberFormat"] != 'undefined' && typeof chartData[chartId]["numberFormat"][measures[j]] != 'undefined' && chartData[chartId]["numberFormat"][measures[j]] === "L") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='L' selected>Lakh</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='L'>Lakh</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Number rounding") {
                    propHtml += "<td><select id='" + chartId + "numberRoundingX" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (chartData[chartId]["chartType"] === "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1" || chartData[chartId]["chartType"] === "KPIDash" || chartData[chartId]["chartType"] == "Table" || chartData[chartId]["chartType"] === "Veraction-Combo") {
                        if (typeof chartData[chartId]["numberRounding"] === "undefined" || typeof chartData[chartId]["numberRounding"][measures[j]] === "undefined" || chartData[chartId]["numberRounding"][measures[j]] === "1") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
                        }

                        if (typeof chartData[chartId]["numberRounding"] != 'undefined' && typeof chartData[chartId]["numberRounding"][measures[j]] != 'undefined' && chartData[chartId]["numberRounding"][measures[j]] === "0") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
                        }
                    }
                    else {
                        if (typeof chartData[chartId]["numberRounding"] === "undefined" || typeof chartData[chartId]["numberRounding"][measures[j]] === "undefined" || chartData[chartId]["numberRounding"][measures[j]] == "0") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='0' selected>No Decimal</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='0'>No Decimal</option>";
                        }

                        if (typeof chartData[chartId]["numberRounding"] != 'undefined' && typeof chartData[chartId]["numberRounding"][measures[j]] != 'undefined' && chartData[chartId]["numberRounding"][measures[j]] === "1") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='1' selected>1 Decimal</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='1'>1 Decimal</option>";
                        }
                    }
                    if (typeof chartData[chartId]["numberRounding"] != 'undefined' && typeof chartData[chartId]["numberRounding"][measures[j]] != 'undefined' && chartData[chartId]["numberRounding"][measures[j]] === "2") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='2' selected>2 Decimal</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='2'>2 Decimal</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Prefix") {
                    if (typeof chartData[chartId]["PrefixList"] != "undefined" && chartData[chartId]["PrefixList"] != "" && typeof chartData[chartId]["PrefixList"][j] !== "undefined" && chartData[chartId]["PrefixList"][j] != "") {
                        propHtml += "<td><input type='text' size='9' value ='" + chartData[chartId]["PrefixList"][j] + "' id ='" + chartId + "PrefixList_" + j + "'></td>";
                    } else {
                        propHtml += "<td><input type='text' size='9' id ='" + chartId + "PrefixList_" + j + "'></td>";
                    }
                }
                else if (propsNames[i] == "Suffix") {
                    if (typeof chartData[chartId]["SuffixList"] !== "undefined" && chartData[chartId]["SuffixList"] != "" && typeof chartData[chartId]["SuffixList"][j] !== "undefined" && chartData[chartId]["SuffixList"][j] != "") {
                        propHtml += "<td><input type='text' size='9' value ='" + chartData[chartId]["SuffixList"][j] + "' id ='" + chartId + "SuffixList_" + j + "'></td>";
                    } else {
                        propHtml += "<td><input type='text' size='9' id ='" + chartId + "SuffixList_" + j + "'></td>";
                    }
                }
                else if (propsNames[i] == "Prefix Font Size") {
                    propHtml += "<td><select id='" + chartId + "PrefixfontsizeList_" + j + "'  class='gFontFamily gFontSize12'  name='fontname'>";
                    if (typeof chartData[chartId]["PrefixfontsizeList"] === 'undefined' || chartData[chartId]["PrefixfontsizeList"] === '' && typeof chartData[chartId]["PrefixfontsizeList"][j] === 'undefined' || chartData[chartId]["PrefixfontsizeList"][j] === '') {
                        propHtml += "<option value='' class='gFontFamily gFontSize12' selected> Select </option>";
                    }
                    else {
                        propHtml += "<option value='' class='gFontFamily gFontSize12'> Select </option>";
                    }
                    for (var k = 6; k <= 100; k++)
                    {
                        if (typeof chartData[chartId]["PrefixfontsizeList"] !== 'undefined' && typeof chartData[chartId]["PrefixfontsizeList"][j] !== 'undefined') {
                            if (chartData[chartId]["PrefixfontsizeList"][j] === k.toString()) {
                                propHtml += "<option value=" + k + " class='gFontFamily gFontSize12' selected> " + k + " </option>";
                            }
                            else {
                                propHtml += "<option value=" + k + " class='gFontFamily gFontSize12'> " + k + " </option>";
                            }
                        }
                        else {
                            propHtml += "<option value=" + k + " class='gFontFamily gFontSize12'> " + k + " </option>";
                        }
                    }
                    propHtml += "</select>";
                    propHtml += "</td>";
                }
                else if (propsNames[i] == "Suffix Font Size") {
                    propHtml += "<td><select id='" + chartId + "SuffixfontsizeList_" + j + "'  class='gFontFamily gFontSize12''  name='fontname'>";

                    if (typeof chartData[chartId]["SuffixfontsizeList"] === 'undefined' || chartData[chartId]["SuffixfontsizeList"] === '' && typeof chartData[chartId]["SuffixfontsizeList"][j] === 'undefined' || chartData[chartId]["SuffixfontsizeList"][j] === '') {
                        propHtml += "<option class='gFontFamily gFontSize12' value='' selected> Select </option>";
                    }
                    else {
                        propHtml += "<option value='' class='gFontFamily gFontSize12'> Select </option>";
                    }
                    for (var k = 6; k <= 100; k++)
                    {
                        //  alert(chartData[chartId]["fontsize"]);
                        if (typeof chartData[chartId]["SuffixfontsizeList"] !== 'undefined' && typeof chartData[chartId]["SuffixfontsizeList"][j] !== 'undefined') {
                            if (chartData[chartId]["SuffixfontsizeList"][j] === k.toString()) {
                                propHtml += "<option value=" + k + " class='gFontFamily gFontSize12' selected> " + k + " </option>";
                            }
                            else {
                                propHtml += "<option value=" + k + " class='gFontFamily gFontSize12'> " + k + " </option>";
                            }
                        }
                        else {
                            propHtml += "<option value=" + k + " class='gFontFamily gFontSize12'> " + k + " </option>";
                        }
                    }
                    propHtml += "</select>";
                    propHtml += "</td>";
                }
                else if (propsNames[i] == "Measure Alias") {
                    if (typeof chartData[chartId]["measureAlias"] !== 'undefined' && typeof chartData[chartId]["measureAlias"][measures[j]] != 'undefined') {
                        alias = chartData[chartId]["measureAlias"][measures[j]];
                    } else {
                        alias = measures[j];
                    }
                    propHtml += "<td><input type='text'  placeholder='Alias' id='" + chartId + "measureAliastext" + j + "' size='15' class='gFontFamily gFontSize12' value='" + alias + "'></td>";
                }
                else if (propsNames[i] == "Show Labels") {
                    propHtml += "<td><select id='" + chartId + "dataDisplayArr" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (typeof chartData[chartId]["dataDisplayArr"] === "undefined" || typeof chartData[chartId]["dataDisplayArr"][measures[j]] === "undefined" || chartData[chartId]["dataDisplayArr"][measures[j]] === "Yes") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                    }

                    if (typeof chartData[chartId]["dataDisplayArr"] !== "undefined" && typeof chartData[chartId]["dataDisplayArr"][measures[j]] !== "undefined" && chartData[chartId]["dataDisplayArr"][measures[j]] === "No") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Measure Label Length") {
                    var length = 0;
                    if (typeof chartData[chartId]["measureLabelLength"] === "undefined" || typeof chartData[chartId]["measureLabelLength"][measures[j]] === "undefined" || chartData[chartId]["measureLabelLength"][measures[j]] === "20") {
                        length = 20;
                    }
                    else {
                        length = chartData[chartId]["measureLabelLength"][measures[j]];
                    }
                    propHtml += "<td><input type='text' size='9' id='" + chartId + "measureLabelLength" + j + "' class='gFontFamily gFontSize12'  name='Dashname' value='" + length + "'/>";
                }
                else if (propsNames[i] == "Measure in chart") {
                    propHtml += "<td><select id='" + chartId + "createBarLine" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (chartData[chartId]["chartType"] === "StackedBar") {
                        if (typeof chartData[chartId]["createBarLine"] === "undefined" || typeof chartData[chartId]["createBarLine"][measures[j]] === "undefined" || chartData[chartId]["createBarLine"][measures[j]] === "Yes") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                        }
                        if (typeof chartData[chartId]["createBarLine"] !== "undefined" && typeof chartData[chartId]["createBarLine"][measures[j]] !== "undefined" && chartData[chartId]["createBarLine"][measures[j]] === "No") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                        }
                    } else {
                        if (j == 0) {
                            if (typeof chartData[chartId]["createBarLine"] === "undefined" || typeof chartData[chartId]["createBarLine"][measures[j]] === "undefined" || chartData[chartId]["createBarLine"][measures[j]] === "No") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                            }
                            if (typeof chartData[chartId]["createBarLine"] === "undefined" || typeof chartData[chartId]["createBarLine"][measures[j]] === "undefined" || chartData[chartId]["createBarLine"][measures[j]] === "Yes") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                            }
                        } else {
                            if (typeof chartData[chartId]["createBarLine"] === "undefined" || typeof chartData[chartId]["createBarLine"][measures[j]] === "undefined" || chartData[chartId]["createBarLine"][measures[j]] === "No") {
                                propHtml += "<option class='gFontFamily gFontSize12'value='No' selected>No</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                            }
                            if (typeof chartData[chartId]["createBarLine"] !== "undefined" && typeof chartData[chartId]["createBarLine"][measures[j]] !== "undefined" && chartData[chartId]["createBarLine"][measures[j]] === "Yes") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                            }
                        }
                    }

                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Average line on") {
                    propHtml += "<td><select id='" + chartId + "measureAvg" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (typeof chartData[chartId]["measureAvg"] === "undefined" || typeof chartData[chartId]["measureAvg"][measures[j]] === "undefined" || chartData[chartId]["measureAvg"][measures[j]] === "No") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                    }
                    if (typeof chartData[chartId]["measureAvg"] !== "undefined" && typeof chartData[chartId]["measureAvg"][measures[j]] !== "undefined" && chartData[chartId]["measureAvg"][measures[j]] === "Yes") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Measure Name Font Size") {
                    var fontSize = 18;
                    if (typeof chartData[chartId]["measureNameSize"] != 'undefined' && typeof chartData[chartId]["measureNameSize"][measures[j]] != 'undefined' && chartData[chartId]["measureNameSize"][measures[j]] != 'undefined') {
                        fontSize = chartData[chartId]["measureNameSize"][measures[j]];
                    }
                    propHtml += "<td><input type='text' size='15' id='" + chartId + "measureNameSize" + j + "' class='gFontFamily gFontSize12'  name='Dashname' value='" + fontSize + "'/>";
                }
                else if (propsNames[i] == "Measure Value Font Size") {
                    var fontSize = 25;
                    if (typeof chartData[chartId]["measureValueSize"] != 'undefined' && typeof chartData[chartId]["measureValueSize"][measures[j]] != 'undefined' && chartData[chartId]["measureValueSize"][measures[j]] != 'undefined') {
                        fontSize = chartData[chartId]["measureValueSize"][measures[j]];
                    }
                    propHtml += "<td><input type='text' size='15' id='" + chartId + "measureValueSize" + j + "' class='gFontFamily gFontSize12'  name='Dashname' value='" + fontSize + "'/>";
                }
                else if (propsNames[i] == "Comparative Value Font Size") {
                    var fontSize = 18;
                    if (typeof chartData[chartId]["comparativeValueFont"] != 'undefined' && typeof chartData[chartId]["comparativeValueFont"][measures[j]] != 'undefined' && chartData[chartId]["comparativeValueFont"][measures[j]] != 'undefined') {
                        fontSize = chartData[chartId]["comparativeValueFont"][measures[j]];
                    }
                    propHtml += "<td><input type='text' size='15' id='" + chartId + "comparativeValueFont" + j + "' class='gFontFamily gFontSize12'  name='Dashname' value='" + fontSize + "'/>";
                }

                else if (propsNames[i] == "Data Label Type") {
                    propHtml += "<td><select id='" + chartId + "dataLabelType" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (chartData[chartId]["chartType"] === "Filled-Horizontal") {
                        if (j == 0) {
                            if (typeof chartData[chartId]["dataLabelType"] === "undefined" || typeof chartData[chartId]["dataLabelType"][measures[j]] === "undefined" || chartData[chartId]["dataLabelType"][measures[j]] === "%-wise") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='%-wise' selected>%-wise</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='%-wise'>%-wise</option>";
                            }
                            if (typeof chartData[chartId]["dataLabelType"] !== "undefined" && typeof chartData[chartId]["dataLabelType"][measures[j]] !== "undefined" && chartData[chartId]["dataLabelType"][measures[j]] === "Absolute") {
                                propHtml += "<option value='Absolute' class='gFontFamily gFontSize12' selected>Absolute</option>";
                            } else {
                                propHtml += "<option value='Absolute' class='gFontFamily gFontSize12'>Absolute</option>";
                            }
                        } else {
                            if (typeof chartData[chartId]["dataLabelType"] === "undefined" || typeof chartData[chartId]["dataLabelType"][measures[j]] === "undefined" || chartData[chartId]["dataLabelType"][measures[j]] === "Absolute") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Absolute' selected>Absolute</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='Absolute'>Absolute</option>";
                            }
                            if (typeof chartData[chartId]["dataLabelType"] !== "undefined" && typeof chartData[chartId]["dataLabelType"][measures[j]] !== "undefined" && chartData[chartId]["dataLabelType"][measures[j]] === "%wise") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='%wise' selected>%wise</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='%wise'>%wise</option>";
                            }
                        }
                    } else {
                        if (typeof chartData[chartId]["dataLabelType"] === "undefined" || typeof chartData[chartId]["dataLabelType"][measures[j]] === "undefined" || chartData[chartId]["dataLabelType"][measures[j]] === "%-wise") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='%-wise' selected>%-wise</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='%-wise'>%-wise</option>";
                        }
                        if (typeof chartData[chartId]["dataLabelType"] !== "undefined" && typeof chartData[chartId]["dataLabelType"][measures[j]] !== "undefined" && chartData[chartId]["dataLabelType"][measures[j]] === "Absolute") {
                            propHtml += "<option value='Absolute' class='gFontFamily gFontSize12' selected>Absolute</option>";
                        } else {
                            propHtml += "<option value='Absolute' class='gFontFamily gFontSize12'>Absolute</option>";
                        }
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Data Label Display") {
                    propHtml += "<td><select id='" + chartId + "dLabelDisplay" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (chartType === 'StackedBar' || chartType === 'StackedBar-%' || chartType === 'StackedBarH-%' || chartType === 'Horizontal-StackedBar' || chartType === 'MultiMeasureH-Bar') {
                        if (typeof chartData[chartId]["dLabelDisplay"] === 'undefined' || typeof chartData[chartId]["dLabelDisplay"][measures[j]] === 'undefined' || chartData[chartId]["dLabelDisplay"][measures[j]] == "OnCenter") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnCenter' selected>OnCenter</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnCenter'>OnCenter</option>";
                        }
                        if (typeof chartData[chartId]["dLabelDisplay"] != "undefined" && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != "undefined" && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnTop") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnTop' selected>OnTop</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnTop'>OnTop</option>";
                        }
                        if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnBottom") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom' selected>OnBottom</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom'>OnBottom</option>";
                        }
                        if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnBar") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBar' selected>OnBar</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBar'>OnBar</option>";
                        }
                    } else {
                        if (typeof chartData[chartId]["dLabelDisplay"] === "undefined" || typeof chartData[chartId]["dLabelDisplay"][measures[j]] === "undefined" || chartData[chartId]["dLabelDisplay"][measures[j]] == "OnTop") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnTop' selected>OnTop</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnTop'>OnTop</option>";
                        }
                        if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnBottom") {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom' selected>OnBottom</option>";
                        } else {
                            propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom'>OnBottom</option>";
                        }
                        if (!(chartType === 'MultiMeasure-Line' || chartType === 'MultiMeasureSmooth-Line')) {
                            if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnCenter") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnCenter' selected>OnCenter</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnCenter'>OnCenter</option>";
                            }
                            if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnTop-Bar") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnTop-Bar' selected>OnTop-Bar</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnTop-Bar'>OnTop-Bar</option>";
                            }
                            if (typeof chartData[chartId]["dLabelDisplay"] != 'undefined' && typeof chartData[chartId]["dLabelDisplay"][measures[j]] != 'undefined' && chartData[chartId]["dLabelDisplay"][measures[j]] === "OnBottom-Bar") {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom-Bar' selected>OnBottom-Bar</option>";
                            } else {
                                propHtml += "<option class='gFontFamily gFontSize12' value='OnBottom-Bar'>OnBottom-Bar</option>";
                            }
                        }
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Adhoc Target Value") {
                    if (typeof chartData[chartId]["defineTargetline"] !== "undefined" && chartData[chartId]["defineTargetline"] != "" && typeof chartData[chartId]["defineTargetline"][measures[j]] !== "undefined" && chartData[chartId]["defineTargetline"][measures[j]] != "") {
                        propHtml += "<td><input type='text' size='9' value ='" + chartData[chartId]["defineTargetline"][measures[j]] + "' id ='" + chartId + "defineTargetline" + j + "' class='gFontFamily gFontSize12'></td>";
                    } else {
                        propHtml += "<td><input type='text' size='9' id ='" + chartId + "defineTargetline" + j + "' class='gFontFamily gFontSize12'></td>";
                    }
                }
                else if (propsNames[i] == "Measures Alias") {
                    if (typeof chartData[chartId]["measureAlias"] !== 'undefined' && typeof chartData[chartId]["measureAlias"][measures[j]] != 'undefined') {
                        alias = chartData[chartId]["measureAlias"][measures[j]];
                    } else {
                        alias = measures[j];
                    }
                    propHtml += "<td><input type='text'  placeholder='Alias' id='" + chartId + "measureAliastext" + j + "' size='15' value='" + alias + "' class='gFontFamily gFontSize12'></td>";
                }
                else if (propsNames[i] === "Measures Name Alignment") {
                    propHtml += "<td><select id='" + chartId + "measNameAlign" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (typeof chartData[chartId]["measNameAlign"] === "undefined" || typeof chartData[chartId]["measNameAlign"][measures[j]] === "undefined" || chartData[chartId]["measNameAlign"][measures[j]] === "left") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='left' selected>Left</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='left'>Left</option>";
                    }
                    if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measures[j]] !== "undefined" && chartData[chartId]["measNameAlign"][measures[j]] === "center") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='center' selected>Center</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='center'>Center</option>";
                    }
                    if (typeof chartData[chartId]["measNameAlign"] !== "undefined" && typeof chartData[chartId]["measNameAlign"][measures[j]] !== "undefined" && chartData[chartId]["measNameAlign"][measures[j]] === "right") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='right' selected>Right</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='right'>Right</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] === "Measure Value Alignment") {
                    propHtml += "<td><select id='" + chartId + "measValueAlign" + j + "' class='gFontFamily gFontSize12'  name='Dashname'>";
                    if (typeof chartData[chartId]["measValueAlign"] === "undefined" || typeof chartData[chartId]["measValueAlign"][measures[j]] === "undefined" || chartData[chartId]["measValueAlign"][measures[j]] === "left") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='left' selected>Left</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='left'>Left</option>";
                    }
                    if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measures[j]] !== "undefined" && chartData[chartId]["measValueAlign"][measures[j]] === "center") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='center' selected>Center</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='center'>Center</option>";
                    }
                    if (typeof chartData[chartId]["measValueAlign"] !== "undefined" && typeof chartData[chartId]["measValueAlign"][measures[j]] !== "undefined" && chartData[chartId]["measValueAlign"][measures[j]] === "right") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='right' selected>Right</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='right'>Right</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] === "Measure Filter") {
                    // measures = chartData[chartId]["meassures"];
                    var meassureIds = chartData[chartId]["meassureIds"];
                    var measureFilters;
                    if (typeof chartData[chartId]["measureFilters"] != "undefined") {
                        measureFilters = chartData[chartId]["measureFilters"];
                    }
                    var selectedVal;
                    propHtml += "<td><select id='" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_Select' onchange='inBetween(\"" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "\",this.value)'>";
                    if (typeof measureFilters == "undefined" || typeof measureFilters[meassureIds[j]] == "undefined" || measureFilters[meassureIds[j]] != "") {
                        selectedVal = "";
                        propHtml += "<option class='gFontFamily gFontSize12' value='' selected></option>";
                    } else {
                        selectedVal = "";
                        propHtml += "<option class='gFontFamily gFontSize12' value='' selected></option>";
                    }
                    if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[j]] != "undefined" && typeof measureFilters[meassureIds[j]]["<"] != "undefined") {
                        selectedVal = measureFilters[meassureIds[j]]["<"];
                        propHtml += "<option class='gFontFamily gFontSize12' value='<' selected><</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='<'><</option>";
                    }
                    if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[j]] != "undefined" && typeof measureFilters[meassureIds[j]][">"] != "undefined") {
                        selectedVal = measureFilters[meassureIds[j]][">"];
                        propHtml += "<option class='gFontFamily gFontSize12' value='>' selected>></option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='>'>></option>";
                    }
                    if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[j]] != "undefined" && typeof measureFilters[meassureIds[j]]["<>"] != "undefined") {
                        selectedVal = measureFilters[meassureIds[j]]["<>"];
                        propHtml += "<option class='gFontFamily gFontSize12' value='<>' selected><></option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='<>'><></option>";
                    }
                    if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[j]] != "undefined" && typeof measureFilters[meassureIds[j]]["="] != "undefined") {
                        selectedVal = measureFilters[meassureIds[j]]["="];
                        propHtml += "<option class='gFontFamily gFontSize12' value='=' selected>=</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='='>=</option>";
                    }
                    if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[j]] != "undefined" && typeof measureFilters[meassureIds[j]]["!="] != "undefined") {
                        selectedVal = measureFilters[meassureIds[j]]["!="];
                        propHtml += "<option class='gFontFamily gFontSize12' value='!=' selected>!=</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='!='>!=</option>";
                    }
                    propHtml += "</select>";
//        propHtml += "</td>";
                    //    if(typeof measureFilters!="undefined" && typeof measureFilters[meassureIds[i]]!="undefined"){
                    //    html +="<td><input id='"+measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi')+"_val' type='text' value='"+measureFilters[meassureIds[i]]+"' /></td>";
                    //}else{
                    //bar
                    // if(typeof selectedVal.split("__")[1]=="undefined"){
                    //
                    // }
                    propHtml += "<input style='display:block' id='" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_val' type='text' value='" + selectedVal + "' class='gFontFamily gFontSize12'/>";
                    propHtml += "<input style='display:none' size='7' id='" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valFrom' type='text' value='" + selectedVal.split("__")[0] + "' class='gFontFamily gFontSize12'/>";
                    var val2 = "";
                    if (typeof selectedVal.split("__")[1] != "undefined") {
                        val2 = selectedVal.split("__")[1];
                    }
                    //        else{
                    //             html += " <span>&nbsp;And &nbsp; </span>";
                    //        }
                    propHtml += "<span id='" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_span' style='display:none;'>&nbsp;And &nbsp; </span> <input style='display:none' size='7' id='" + measures[j].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valTo' type='text' value='" + val2 + "' class='gFontFamily gFontSize12'/></td>";
                    //    }
                }
                else if (propsNames[i] == "Sort On Measure") {
                    propHtml += "<td id='sortMeasTD' >";
                    if (j == 0) {
                        if (typeof chartData[chartId]["sortMeasure"] === "undefined" || typeof chartData[chartId]["sortMeasure"][measures[j]] === "undefined" || chartData[chartId]["sortMeasure"][measures[j]] === "Y") {
                            propHtml += "<input type='radio' value='" + j + "' id='" + chartId + "sortMeasure" + j + "'  name='groupr' >";
                        } else {
                            propHtml += "<input type='radio' value='" + j + "' id='" + chartId + "sortMeasure" + j + "'  name='groupr'>";
                        }
                    } else {
                        if (typeof chartData[chartId]["sortMeasure"] != "undefined" && typeof chartData[chartId]["sortMeasure"][measures[j]] != "undefined" && chartData[chartId]["sortMeasure"][measures[j]] === "N") {
                            propHtml += "<input type='radio' value='" + j + "' id='" + chartId + "sortMeasure" + j + "'  name='groupr' >";
                        } else {
                            propHtml += "<input type='radio' value='" + j + "' id='" + chartId + "sortMeasure" + j + "'  name='groupr' >";
                        }
                    }
                    propHtml += "</td>";
                }
                else if (propsNames[i] == "Ticks Font Size") {   //  add by mayank sharma
                    var fontSize = 10;
                    if (typeof chartData[chartId]["ticksValueSize"] != 'undefined' && typeof chartData[chartId]["ticksValueSize"][measures[j]] != 'undefined' && chartData[chartId]["ticksValueSize"][measures[j]] != 'undefined') {
                        fontSize = chartData[chartId]["ticksValueSize"][measures[j]];
                    }
                    propHtml += "<td><input type='text' size='15' id='" + chartId + "ticksValueSize" + j + "' class='gFontFamily gFontSize12'  name='Dashname' value='" + fontSize + "'/>";
                }
                else if (propsNames[i] == "Measure Name Font Color") {
                    var measureColor = "#A1A1A1";
                    if (typeof chartData[chartId]["measureColor"] != 'undefined' && typeof chartData[chartId]["measureColor"][measures[j]] != 'undefined' && chartData[chartId]["measureColor"][measures[j]] != 'undefined') {
                        measureColor = chartData[chartId]["measureColor"][measures[j]];
                    }
                    propHtml += "<td><input style='background-color:" + measureColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='measureColor" + j + "' readonly></td>";
                }
                else if (propsNames[i] == "Measure Value Font Color") {
                    var dataColor = "#696969";
                    if (typeof chartData[chartId]["dataColor"] != 'undefined' && typeof chartData[chartId]["dataColor"][measures[j]] != 'undefined' && chartData[chartId]["dataColor"][measures[j]] != 'undefined') {
                        dataColor = chartData[chartId]["dataColor"][measures[j]];
                    }
                    propHtml += "<td><input style='background-color:" + dataColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='dataColor" + j + "' readonly></td>";
                }   //  end by mayank sharma
                else if (propsNames[i] == "Label Font Color") {
                    var LabFtColor = "#000000";
                    if (typeof chartData[chartId]["LabFtColor"] != 'undefined' && typeof chartData[chartId]["LabFtColor"][measures[j]] != 'undefined' && chartData[chartId]["LabFtColor"][measures[j]] != 'undefined') {
                        LabFtColor = chartData[chartId]["LabFtColor"][measures[j]];
                    }
                    propHtml += "<td><input style='background-color:" + LabFtColor + "' type='button' value='              ' onclick='moreColors(\"" + chartId + "\",this.id)' id='LabFtColor" + j + "' readonly></td>";
                }   //  end by mayank sharma

                /*Added by Ashutosh*/
                else if (propsNames[i] == "Report Drill on Measure") {
                    var graphDrillMap=chartData[chartId]["graphDrillMap"];
                    propHtml += "<td id='singleReportTd" + meassureIds[j] + "'><select id='reportToDrill_" + meassureIds[j] + "' style='width:100%;' name='reportToDrill'>";
                    if (assignRepIds[0] == '0'){
                        propHtml += "<option selected class='gFontFamily ' value='0'>Select</option>";
                    }
                    else{
                        if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap[meassureIds[j]]!=='undefined'){
                        propHtml += "<option class='gFontFamily ' value='0' selected>Select</option>";
                        }else{
                        propHtml += "<option class='gFontFamily ' value='0'>Select</option>";
                        }
                    }
                    for (var k= 0; k < reportIds.length; k++) {
                        if (typeof graphDrillMap!=='undefined' && typeof graphDrillMap[meassureIds[j]]!=='undefined' && graphDrillMap[meassureIds[j]] == reportIds[k])
                            propHtml += "<option selected value='" + reportIds[k] + "'>" + reportNames[k] + "</option>";
                        else
                            propHtml += "<option value='" + reportIds[k] + "'>" + reportNames[k] + "</option>";
                    }
                    propHtml += "</select></td>";
                }
                else if (propsNames[i] == "Tooltip Display Type") {
                    propHtml += "<td><select id='" + chartId + "tooltipType" + j + "' class='gFontFamily gFontSize12''>";
                    if (typeof chartData[chartId]["tooltipType"] === "undefined" || typeof chartData[chartId]["tooltipType"][measures[j]] === "undefined" || chartData[chartId]["tooltipType"][measures[j]] === "Default") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Default' selected>Default</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Default'>Default</option>";
                    }

                    if (typeof chartData[chartId]["tooltipType"] != "undefined" && typeof chartData[chartId]["tooltipType"][measures[j]] != "undefined" && chartData[chartId]["tooltipType"][measures[j]] === "With%") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='With%' selected>With%</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='With%'>With%</option>";
                    }
                    if (typeof chartData[chartId]["tooltipType"] != "undefined" && typeof chartData[chartId]["tooltipType"][measures[j]] != "undefined" && chartData[chartId]["tooltipType"][measures[j]] === "Default-With%") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Default-With%' selected>Default-With%</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Default-With%'>Default-With%</option>";
                    }

                    propHtml += "</select></td>";
                }
                else  if (propsNames[i] == "Measure Filter On Slider") {
                    var dataSlider=chartData[chartId]["dataSlider"]
                    var meassureIds = chartData[chartId]["meassureIds"];
                    var meassures=chartData[chartId]["meassures"];
//                    alert(meassureIds[meassures.indexOf(measures[j])])
//                    alert("mesr"+chartData[chartId]["dataSlider"][meassureIds[measures[j]]])
                    propHtml += "<td><select id='" + chartId + "dataSlider" + j + "' class='gFontFamily gFontSize12''>";
//                        $("#"+chartId +"dataSlider" + j ).html("");
//                        alert(JSON.stringify(dataSlider))
//                    for (var l in dataSlider){
//                        alert(meassures[measures[j]]+"::::"+chartData[chartId]["dataSlider"][meassures[measures[j]]])
                    if (typeof chartData[chartId]["dataSlider"] === "undefined" || typeof chartData[chartId]["dataSlider"][meassureIds[meassures.indexOf(measures[j])]] === "undefined" || chartData[chartId]["dataSlider"][meassureIds[meassures.indexOf(measures[j])]] === "No") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='No' selected>No</option>";
                    } 
                    else { 
                        propHtml += "<option class='gFontFamily gFontSize12' value='No'>No</option>";
                    }
                   
                    if (typeof chartData[chartId]["dataSlider"] !== "undefined" && typeof chartData[chartId]["dataSlider"][meassureIds[meassures.indexOf(measures[j])]] !== "undefined" && chartData[chartId]["dataSlider"][meassureIds[meassures.indexOf(measures[j])]] === "Yes") {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes' selected>Yes</option>";
                    } else {
                        propHtml += "<option class='gFontFamily gFontSize12' value='Yes'>Yes</option>";
                    }
//                    
                 propHtml += "</select></td>";
                }
                /*Endded by Ashutosh*/
                measureProps += propHtml;
            }
            measureProps += "</tr>";
        }
    }
    else {

        if (chartData[chartId]["chartType"] === "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1" || chartData[chartId]["chartType"] === "Veraction-Combo" || chartData[chartId]["chartType"] === "Combo-Analysis" || chartData[chartId]["chartType"] === "Bubble" || chartData[chartId]["chartType"] == "Emoji-Chart" || chartData[chartId]["chartType"] == "Radial-Chart" || chartData[chartId]["chartType"] == "LiquidFilled-KPI" || chartData[chartId]["chartType"] == "Dial-Gauge" || chartData[chartId]["chartType"] == "Top-Analysis" || chartData[chartId]["chartType"] == "World-Top-Analysis" || chartData[chartId]["chartType"] == "Combo-Analysis"
    ||chartData[chartId]["chartType"] == "Combo-US-Map"||chartData[chartId]["chartType"] == "Combo-USCity-Map") {
            measureProps += "<th style='font-weight: bold;text-align:left' class='gFontFamily gFontSize12'>" + measures[0] + "</th>";
            //  add by mayank sharma
            if (chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] != "" && (chartData[chartId]["chartType"] == "Standard-KPI" ||chartData[chartId]["chartType"] == "Standard-KPI1"  || chartData[chartId]["chartType"] == "Veraction-Combo" || chartData[chartId]["chartType"] == "Emoji-Chart")) {
                measureProps += "<tr>";
                measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Alias + "</td>";
                if (typeof chartData[chartId]["measureAliaskpi"] !== 'undefined' && typeof chartData[chartId]["measureAliaskpi"][measures[0]] != 'undefined') {
                    var measureAliaskpi = chartData[chartId]["measureAliaskpi"][measures[0]];
                } else {
                    measureAliaskpi = measures[0];
                }
                measureProps += "<td><input type='text'  placeholder='Alias' id='" + chartId + "measureAliaskpi' size='15' class='gFontFamily gFontSize12' value='" + measureAliaskpi + "'></td>";
                measureProps += "</tr>";
            }
            if (chartData[chartId]["chartType"] == "Combo-Analysis") {
                measureProps += "<tr>";
                measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Alias + "</td>";
                for (var q = 0; q < measures.length; q++) {
                    if (typeof chartData[chartId]["measureAliasCombo"] !== 'undefined' && typeof chartData[chartId]["measureAliasCombo"][measures[q]] != 'undefined') {
                        var measureAliasCombo = chartData[chartId]["measureAliasCombo"][measures[q]];
                    } else {
                        measureAliasCombo = measures[q];
                    }
                    measureProps += "<td><input type='text'  placeholder='Alias' id='" + chartId + "measureAliasCombo" + q + "' size='15' class='gFontFamily gFontSize12' value='" + measureAliasCombo + "'></td>";
                }
                measureProps += "</tr>";
            }

            if (chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] != "" && (chartData[chartId]["chartType"] == "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1"|| chartData[chartId]["chartType"] == "Veraction-Combo" || chartData[chartId]["chartType"] == "Emoji-Chart" || chartData[chartId]["chartType"] == "Bubble" || chartData[chartId]["chartType"] == "Top-Analysis" || chartData[chartId]["chartType"] == "World-Top-Analysis" || chartData[chartId]["chartType"] == "Combo-Analysis")) {
                measureProps += "<tr>";
                measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Prefix + "</td>";
                if (typeof chartData[chartId]["Prefix"] !== "undefined" && chartData[chartId]["Prefix"] != "") {
                    measureProps += "<td><input type='text' size='10'  value ='" + chartData[chartId]["Prefix"] + "' id ='" + chartId + "Prefix' class='gFontFamily gFontSize12'></td>";
                } else {
                    measureProps += "<td><input type='text' size='10'  id ='" + chartId + "Prefix' class='gFontFamily gFontSize12'></td>";
                }
                measureProps += "</tr>";
                if (!(chartData[chartId]["chartType"] == "Bubble")) {
                    measureProps += "<tr>";
                    measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Suffix + "</td>";
                    if (typeof chartData[chartId]["Suffix"] !== "undefined" && chartData[chartId]["Suffix"] != "") {
                        measureProps += "<td><input type='text' size='10' value ='" + chartData[chartId]["Suffix"] + "' id ='" + chartId + "Suffix' class='gFontFamily gFontSize12'></td>";
                    } else {
                        measureProps += "<td><input type='text' size='10' id ='" + chartId + "Suffix' class='gFontFamily gFontSize12'></td>";
                    }
                    measureProps += "</tr>";

                    measureProps += "<tr>";
                    measureProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Prefix_Font_Size + " </label></td>";
                    measureProps += "<td>";
                    measureProps += "<select id='" + chartId + "Prefixfontsize'  style='font-size:9pt'  name='fontname'>";
                    if (typeof chartData[chartId]["Prefixfontsize"] === 'undefined' || chartData[chartId]["Prefixfontsize"] === '') {
                        measureProps += "<option class='gFontFamily gFontSize12' value='' selected> Select </option>";
                    } else {
                        measureProps += "<option class='gFontFamily gFontSize12' value='' > Select </option>";
                    }
                    for (var i = 6; i <= 100; i++)
                    {
                        if (typeof chartData[chartId]["Prefixfontsize"] !== 'undefined') {
                            if (chartData[chartId]["Prefixfontsize"] === i.toString()) {
                                measureProps += "<option class='gFontFamily gFontSize12' value=" + i + " selected> " + i + " </option>";
                            } else {
                                measureProps += "<option class='gFontFamily gFontSize12' value=" + i + "> " + i + " </option>";
                            }
                        } else {
                            measureProps += "<option class='gFontFamily gFontSize12' value=" + i + " > " + i + " </option>";
                        }
                    }
                    measureProps += "</td>";
                    measureProps += "</tr>";

                    measureProps += "<tr>";
                    measureProps += "<td ><label style='white-space:nowrap' class='gFontFamily gFontSize12'>" + translateLanguage.Suffix_Font_Size + " </label></td>";
                    measureProps += "<td>";
                    measureProps += "<select id='" + chartId + "Suffixfontsize'  class='gFontFamily gFontSize12'  name='fontname'>";
                    if (typeof chartData[chartId]["Suffixfontsize"] === 'undefined' || chartData[chartId]["Suffixfontsize"] === '') {
                        measureProps += "<option class='gFontFamily gFontSize12' value='' selected> Select </option>";
                    } else {
                        measureProps += "<option class='gFontFamily gFontSize12' value='' > Select </option>";
                    }
                    for (var i = 6; i <= 100; i++)
                    {
                        if (typeof chartData[chartId]["Suffixfontsize"] !== 'undefined') {
                            if (chartData[chartId]["Suffixfontsize"] === i.toString()) {
                                measureProps += "<option class='gFontFamily gFontSize12' value=" + i + " selected> " + i + " </option>";
                            } else {
                                measureProps += "<option class='gFontFamily gFontSize12' value=" + i + "> " + i + " </option>";
                            }
                        } else {
                            measureProps += "<option class='gFontFamily gFontSize12' value=" + i + " > " + i + " </option>";
                        }
                    }
                    measureProps += "</td>";
                    measureProps += "</tr>";
                }
            }

            if (chartData[chartId]["chartType"] == "Radial-Chart" || chartData[chartId]["chartType"] == "LiquidFilled-KPI" || chartData[chartId]["chartType"] == "Dial-Gauge"
        ||chartData[chartId]["chartType"] == "Combo-US-Map"||chartData[chartId]["chartType"] == "Combo-USCity-Map") {
                measureProps += "<tr>";
                measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Value_Font_Size + "</td>";
                var Data1size = 12;
                if (typeof chartData[chartId]["kpiGTFont"] !== "undefined" && chartData[chartId]["kpiGTFont"] != "") {
                    Data1size = parseInt(chartData[chartId]["kpiGTFont"]);
                }
                measureProps += "<td><input type='text' size='10' id='" + chartId + "kpiGTFont'   name='Dashname' class='gFontFamily gFontSize12' value='" + Data1size + "'/></td>";
                measureProps += "</tr>";

            } else {
                if (!(chartData[chartId]["chartType"] == "Bubble")) {
                    measureProps += "<tr>";
                    measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Value_Font_Size + "</td>";
                    var Data1size = 18;
                    if (typeof chartData[chartId]["kpiGTFont"] !== "undefined" && chartData[chartId]["kpiGTFont"] != "") {
                        Data1size = parseInt(chartData[chartId]["kpiGTFont"]);
                    }
                    measureProps += "<td><input type='text' size='10' id='" + chartId + "kpiGTFont'   name='Dashname' class='gFontFamily gFontSize12' value='" + Data1size + "'/></td>";
                    measureProps += "</tr>";

                    measureProps += "<tr>";
                    measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Default_Suffix_Font_Size + "</td>";
                    var Data2size = 14;
                    if (typeof chartData[chartId]["kpiSubData"] !== "undefined" && chartData[chartId]["kpiSubData"] != "") {
                        Data2size = parseInt(chartData[chartId]["kpiSubData"]);
                    }
                    measureProps += "<td><input type='text' size='10' id='" + chartId + "kpiSubData'   name='Dashname' class='gFontFamily gFontSize12' value='" + Data2size + "'/></td>";
                    measureProps += "</tr>";

                    measureProps += "<tr>";
                    measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Measure_Name_Font_Size + "</td>";
                    var Data3size = 12;
                    if (typeof chartData[chartId]["kpiFont"] !== "undefined" && chartData[chartId]["kpiFont"] !== "") {
                        Data3size = parseInt(chartData[chartId]["kpiFont"]);
                    }
                    measureProps += "<td><input type='text' id='" + chartId + "kpiFont' size='10' class='gFontFamily gFontSize12' value=" + Data3size + "></td>";
                    measureProps += "</tr>";

                    measureProps += "<tr>";
                    measureProps += "<td class='gFontFamily gFontSize12'>" + translateLanguage.Comparative_Value_Font_Size + "</td>";
                    var Data4size = 18;
                    if (typeof chartData[chartId]["comparativeValue"] !== "undefined" && chartData[chartId]["comparativeValue"] !== "") {
                        Data4size = parseInt(chartData[chartId]["comparativeValue"]);
                    }
                    measureProps += "<td><input type='text' id='" + chartId + "comparativeValue' size='10' class='gFontFamily gFontSize12' value=" + Data4size + "></td>";
                    measureProps += "</tr>";
                }
            }

            if (chartData[chartId]["chartType"] == "Dial-Gauge") {
                measureProps += "<tr><td class='gFontFamily gFontSize12'>";
                var kpiColor = "#333";
                if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                    kpiColor = chartData[chartId]["colorPicker"];
                }
                if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                    measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + kpiColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker'>";
                } else {
                    measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + kpiColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker' >";
                }
                measureProps += "</td></tr>";

            } else if (chartData[chartId]["chartType"] == "Standard-KPI" || chartData[chartId]["chartType"] == "Standard-KPI1"|| chartData[chartId]["chartType"] == "Veraction-Combo" || chartData[chartId]["chartType"] == "World-Top-Analysis" || chartData[chartId]["chartType"] == "Top-Analysis" || chartData[chartId]["chartType"] == "Emoji-Chart") {

                measureProps += "<tr><td class='gFontFamily gFontSize12'>";
                var kpiColor = "#696969";
                if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                    kpiColor = chartData[chartId]["colorPicker"];
                }
                if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                    measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + kpiColor + ";width:30px' type='button' value='' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker'>";
                } else {
                    measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + kpiColor + ";width:30px' type='button' value='' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker' >";
                }
                measureProps += "</td></tr>";

                measureProps += "<tr><td class='gFontFamily gFontSize12'>";
                var lFilledFont = "#A1A1A1";
                if (typeof chartData[chartId]["lFilledFont"] !== "undefined" && chartData[chartId]["lFilledFont"] != "") {
                    lFilledFont = chartData[chartId]["lFilledFont"];
                }
                if (typeof chartData[chartId]["lFilledFont"] !== "undefined" && chartData[chartId]["lFilledFont"] != "") {
                    measureProps += "" + translateLanguage.Measure_Name_Font_color + " </td><td> <input style='background-color:" + lFilledFont + ";width:30px;' type='button' value='' onclick='moreColors(\"" + chartId + "\",this.id)' id='lFilledFont'>";
                } else {
                    measureProps += "" + translateLanguage.Measure_Name_Font_color + " </td><td> <input style='background-color:" + lFilledFont + ";width:30px;' type='button' value='' onclick='moreColors(\"" + chartId + "\",this.id)' id='lFilledFont' >";
                }
                measureProps += "</td></tr>";

            } else {
                if (!(chartData[chartId]["chartType"] == "Bubble" || chartData[chartId]["chartType"] === "Combo-Analysis")) {
                    measureProps += "<tr><td style='font: 11px verdana;font-size:9pt'>";
                    var lFilledFont = "#ECE334";
                    if (typeof chartData[chartId]["lFilledFont"] !== "undefined" && chartData[chartId]["lFilledFont"] != "") {
                        lFilledFont = chartData[chartId]["lFilledFont"];
                    }
                    if (typeof chartData[chartId]["lFilledFont"] !== "undefined" && chartData[chartId]["lFilledFont"] != "") {
                        measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + lFilledFont + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='lFilledFont'>";
                    } else {
                        measureProps += "" + translateLanguage.Measure_Value_Font_Color + " </td><td> <input style='background-color:" + lFilledFont + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='lFilledFont' >";
                    }
                    measureProps += "</td></tr>";

                    measureProps += "<tr><td class='gFontFamily gFontSize12'>";
                    var kpiColor = "#696969";
//           if(chartData[chartId]["chartType"]==='Combo-Analysis'){
//               kpiColor="#696969";
//            }
                    if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                        kpiColor = chartData[chartId]["colorPicker"];
                    }
                    if (typeof chartData[chartId]["colorPicker"] !== "undefined" && chartData[chartId]["colorPicker"] != "") {
                        measureProps += "" + translateLanguage.Fill_color + " </td><td> <input style='background-color:" + kpiColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker'>";
                    } else {
                        measureProps += "" + translateLanguage.Fill_color + " </td><td> <input style='background-color:" + kpiColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='_colorPicker' >";
                    }
                    measureProps += "</td></tr>";

                    if (!(chartData[chartId]["chartType"] == "LiquidFilled-KPI"||chartData[chartId]["chartType"] == "Combo-US-Map"||chartData[chartId]["chartType"] == "Combo-USCity-Map")) {
                        measureProps += "<tr><td class='gFontFamily gFontSize12'>";
                        var radialDefaultColor = "#99CCFF";
                        if (typeof chartData[chartId]["radialDefaultColor"] !== "undefined" && chartData[chartId]["radialDefaultColor"] != "") {
                            radialDefaultColor = chartData[chartId]["radialDefaultColor"];
                        }
                        if (typeof chartData[chartId]["radialDefaultColor"] !== "undefined" && chartData[chartId]["radialDefaultColor"] != "") {
                            measureProps += "" + translateLanguage.Default_Fill_color + " </td><td> <input style='background-color:" + radialDefaultColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='radialDefaultColor'>";
                        } else {
                            measureProps += "" + translateLanguage.Default_Fill_color + " </td><td> <input style='background-color:" + radialDefaultColor + "' type='button' value='           ' onclick='moreColors(\"" + chartId + "\",this.id)' id='radialDefaultColor' >";
                        }
                        measureProps += "</td></tr>";
                    }
                }
            }//  end by mayank sharma

        }

    }
    measureProps += "</table>";

    var doneProp = "";
    doneProp += '<div id="button">'
    doneProp += '<table><tr style="height:25px"><td colspan="4" align="center" ><a  name="' + chartId + '" onclick="setChartDetails(this.name)" class="cssSubmitButton gFontFamily gFontSize12" >' + translateLanguage.Done + '</a>';
    doneProp += '</td></tr></table></div>';
//---------------------------------------------------
    $("#dashboardProp1").html(innerHtml);
    $("#dashboardProp1").append(kpiProp);
    $("#dashboardProp1").append(axisprop);
    $("#dashboardProp1").append(chartProp);
    $("#dashboardProp1").append(measureProps);
    $("#dashboardProp1").append(doneProp);
    $("#dashboardProp1").dialog('open');
    
   window.measure_seq=""+JSON.stringify(chartData[chartId]["measure_seq"]);
   window.measure_seq=window.measure_seq.replace(/[^0-9\.]+/g, "");
    if (typeof JSON.stringify(chartData[chartId]["measure_seq"]) === 'undefined' ||JSON.stringify(chartData[chartId]["measure_seq"]) === 'undefined') {
        $("#" + chartId + "sortMeasure0").prop("checked", true);
    } else {
        $("#" + chartId + "sortMeasure" + window.measure_seq).prop("checked", true);
    }
    if($("#" + chartId + "sortMeasure" + window.measure_seq).width()===null){
        $("#" + chartId + "sortMeasure0").prop("checked", true);
    }
}
 //Ashutosh
    try{
    var dataSlider=chartData[chartId]["dataSlider"];
    chartData[chartId]["measureFilters"]={};
//    alert("window.changedMeasureId"+window.changedMeasureId);
    if(dataSlider[window.changedMeasureId]!=='undefined'&&dataSlider[window.changedMeasureId]==='Yes'){
        chartData[chartId]["Pattern"]='Multi';
        
        chartData[chartId]["sliderAxisVal"]={};
        window.flag=true;
        parent.range.axisVal={};
        parent.range.slidingVal={};
        parent.range.map={};
        parent.range.clausemap={};  
    }
}catch(e){}

//added by shivam
$(document).ready(function() {
    $("body").on("click", ".tabUL", function() {
        $(".tabUL").css("border", "none");
        $(this).css("border-bottom", "3px solid #DC143C");
    });
});
function general() {
    // alert(innerHtml)

    $("#kpiProperties").hide();
    $("#axisProperties").hide();
    $("#chartProperties").hide();
    $("#measureProps").hide();
    $("#dashboardTable").show();
}
function kpicharts() {
    //  alert(kpiProp)

    $("#dashboardTable").hide();
    $("#axisProperties").hide();
    $("#chartProperties").hide();
    $("#measureProps").hide();
    $("#kpiProperties").show();

//}
}
function xyaxisproperty() {
    // alert(axisProp)

    $("#dashboardTable").hide();
    $("#kpiProperties").hide();
    $("#chartProperties").hide();
    $("#measureProps").hide();
    $("#axisProperties").show();
}

function legendslabels() {
    //  alert(axisProp)

    $("#kpiProperties").hide();
    $("#axisProperties").hide();
    $("#dashboardTable").hide();
    $("#measureProps").hide();
    $("#chartProperties").show();
}

function measures() {
    //  alert(axisProp)

    $("#kpiProperties").hide();
    $("#axisProperties").hide();
    $("#dashboardTable").hide();
    $("#chartProperties").hide();
    $("#measureProps").show();
}
// end new property by mynk sh.

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

//function divback(chartId){
//    var chartData = JSON.parse($("#chartData").val());
//    $('#div_background').dialog({
//        autoOpen: false,
//        height: 250,
//        width: 247,
//        position: 'justify',
//        modal: true,
//        resizable:false,
//        title:'Pick Color'
//    });
//    $('#div_background').dialog('open');
//    $('#div_background').colpick({
//        flat:true,
//        layout:'hex',
//        onSubmit:function(hsb,hex,rgb,el,bySetColor) {
//            
//            chartData[chartId]["divBackGround"] =  "#"+hex;
//            parent.$("#chartData").val(JSON.stringify(chartData));
//            if(!bySetColor) $(el).val(hex);
//            $('#div_background').dialog('close');
//        }
//    })
//        
////.keyup(function(){
////        $("input").css("background-color", "pink");
////    });
//.keyup(function(){
//
//    $(this).colpickSetColor(this.value);
//});
//
//}
function initializeGraph1(chartId) {
    alert("This will make the selected chart as driver chart for all dependent charts selected in the next UI");
    $("#initializeCharts").dialog({
        autoOpen: false,
        height: 260,
        width: 300,
        position: 'justify',
        modal: true,
        resizable: true,
        title: translateLanguage.Initialize_Charts
    });
    var innerHtml = "<table style='padding:3px 3px 3px 3px;width:100%;border-collapse:separate;border-spacing:10px 10px;'>";
    innerHtml += "<tr><td><span class='gFontFamily gFontSize12 fontWeightBold' align:left;'>" + translateLanguage.Select_charts_to_initialize + "</span> </td></tr>";
    var chartNames = [];
    var chartIds = [];
    var chartData = JSON.parse($("#chartData").val());
    var chartList = Object.keys(chartData);
    chartIds = Object.keys(chartData);
    for (var i in chartList) {
        if (typeof chartData[chartList[i]]["Name"] !== 'undefined' && chartData[chartList[i]]["Name"] !== 'undefined') {
            chartNames.push(chartData[chartList[i]]["Name"]);
        }
        else {
            chartNames.push(chartList[i]);
        }
    }
    var oldInitCharts = chartData["chart1"]["anchorChart"];
    innerHtml += "<tr><td class='gFontFamily gFontSize12'><input type='checkbox' id='selectAllChk'  onclick='selectAllCharts(this.id)'>" + translateLanguage.Select_all + "</input></td></tr><tr><td><div style='height:100px;overflow-y:auto'><table>";
    for (var i in chartNames) {
        if (chartId == chartIds[i]) {
            continue;
        }
        var completeViewby = chartData[chartIds[i]]["viewBys"];//edit by shivam
        var completeMeassures = chartData[chartIds[i]]["meassures"];//edit by shivam
        if (typeof oldInitCharts !== 'undefined' && oldInitCharts !== '') {
            var keys1 = Object.keys(oldInitCharts);
            if (keys1.length > 0) {
                var oldInitChartIds = oldInitCharts[keys1[0]];
                var j = 0;
                for (j = 0; j < oldInitChartIds.length; j++) {
                    if (oldInitChartIds[j] === chartIds[i]) {
                        innerHtml += "<tr><td class='gFontFamily gFontSize12'><input type='checkbox' id='initchart" + i + "' checked>" + completeViewby + "," + completeMeassures + "(" + chartData[chartIds[i]]["chartType"] + ")</input></td></tr>";
                        break;
                    }
                }
                if (j == oldInitChartIds.length) {
                    innerHtml += "<tr><td class='gFontFamily gFontSize12'><input type='checkbox' id='initchart" + i + "'>" + completeViewby + "," + completeMeassures + "(" + chartData[chartIds[i]]["chartType"] + ")</input></td></tr>";
                }
            }
            else {
                innerHtml += "<tr><td class='gFontFamily gFontSize12'><input type='checkbox' id='initchart" + i + "'>" + completeViewby + "," + completeMeassures + "(" + chartData[chartIds[i]]["chartType"] + ")</input></td></tr>";
            }
        }
        else {
            innerHtml += "<tr><td class='gFontFamily gFontSize12'><input type='checkbox' id='initchart" + i + "'>" + completeViewby + "," + completeMeassures + "(" + chartData[chartIds[i]]["chartType"] + ")</input></td></tr>";
        }
    }
    innerHtml += "</table>";
    innerHtml += "</div>";
    innerHtml += "</td>";
    innerHtml += "</tr>";
    innerHtml += "<tr>";
    innerHtml += "<td><input type='button' class='gFontFamily gFontSize12' onclick='initializeGraph(\"" + chartId + "\")' value='" + translateLanguage.Done + "'/></td>";
    innerHtml += "</tr>";
    innerHtml += "</table>";
    $("#initializeCharts").html(innerHtml);
    $("#initializeCharts").dialog('open');
}
function initializeGraph(chartId)
{
    $("#initializeCharts").dialog('close');
    var chartData = JSON.parse($("#chartData").val());
    var excludeList = [];
    var firstViewByData;
    var viewBys;
    var records = 0;
    if (typeof chartData[chartId]["records"] !== 'undefined') {
        records = chartData[chartId]["records"];
    }
    else
    {
        records = 12;
    }
    for (var i = 0; i < (graphData[chartId].length < records ? graphData[chartId].length : records); i++) {

        viewBys = chartData[chartId]["viewBys"];
        firstViewByData = graphData[chartId][i][viewBys];
        excludeList.push(firstViewByData);
    }
    var initFilter = {};
    var key = chartData[chartId]["viewIds"][0];
    initFilter[key] = excludeList;
    var chartsList = Object.keys(chartData);
    var initCharts = [];
    for (var i in chartsList) {
        if ($("#initchart" + i).prop("checked")) {
            initCharts.push(chartsList[i])
            chartData[chartsList[i]]["localDrills"] = initFilter;
        }
        else {
            chartData[chartsList[i]]["localDrills"] = {};
        }
    }
    if (initCharts.length == 0) {
        chartData["chart1"]["anchorChart"] = {};
    }
    else {
        var anchorChart = {};
        anchorChart[chartId] = initCharts;
        chartData["chart1"]["anchorChart"] = anchorChart;
    }
    parent.$("#chartData").val(JSON.stringify(chartData));
    var ctxPath = document.getElementById("h").value;
    $.ajax({
        type: 'POST',
        async: false,
        data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&chartData=" + encodeURIComponent(parent.$("#chartData").val()) + "&initializeFlag=true",
        url: ctxPath + "/reportViewer.do?reportBy=drillCharts",
        success: function(data) {
            var jsondata = JSON.parse(data)["data"];
            $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
            generateChart(jsondata);
            alert("Charts initialized successfully");
        }
    })
}

function getMeasureAlias(id) {
    if ($("#" + id).val() == 'no') {
        $("#defineMeasureAlias").hide(500);
    } else {
        $("#defineMeasureAlias").show(500);
    }
}

function getCorrelationGraph(columnName, correlationColID, disColumnName, disColumnName2, tabId, type) {
    //    var source = "TableDisplay/pbDisplay.jsp?sourceValue="+type+"&source=S&columnName="+columnName+"&correlationColName="+correlationColID+"&disColumnName="+disColumnName+"&tabId="+tabId;
    //    var dSrc = document.getElementById("iframe1");
    columnName = "A_" + columnName;
    correlationColID = "A_" + correlationColID;
    $.ajax({
        async: false,
        url: "reportViewer.do?reportBy=getCorrelation&columnName=" + columnName + "&correlationColName=" + correlationColID + "&reportId=" + tabId,
        success: function(data) {
            var html = "</font></span></div><br><br><table align='center'><tr><td><input class='navtitle-hover' type='button' value='Done' onclick='closeCorrelationDiv()'></td></tr></table>"
            var innerHtml = "<div><span><font style='font-size:25px;font-weight:bold;'>"
            $("#correlationId").html("The Correlation coefficient between " + disColumnName + " and " + disColumnName2 + " is " + innerHtml + addCommas(data) + html)
            $("#correlationId").dialog('open');
        }
    })


}
function getStatistics(columnName, disColumnName, tabId, type) {

    columnName = "A_" + columnName;
    $.ajax({
        async: false,
        url: "reportViewer.do?reportBy=getStatistics&columnName=" + columnName + "&reportId=" + tabId + "&type=" + type,
        success: function(data) {
            var html = "</font></span></div><br><br><table align='center'><tr><td><input class='navtitle-hover' type='button' value='Done' onclick='closeCorrelationDiv()'></td></tr></table>"
            var innerHtml = "<div><span><font style='font-size:25px;font-weight:bold;'>"
            $("#correlationId").html("The " + type + " for " + disColumnName + " is " + innerHtml + addCommas(data) + html)
            $("#correlationId").dialog('open');
        }
    })


}

function showCharts()
{
    $("#advanceChartList").dialog({
        autoOpen: false,
        height: 300,
        width: 200,
        position: 'justify',
        modal: true,
        resizable: false,
        title: 'Advance Visuals'
    });
    var visual = $("#currType").val();
    var infoMap;
    var ctxPath = document.getElementById("h").value;
    $.ajax({
        type: 'POST',
        url: ctxPath + '/reportViewer.do?reportBy=getAvailableCharts&reportId=' + $("#graphsId").val() + "&reportName=" + parent.$("#graphName").val() + "&type=advance&visual=" + visual,
        async: false,
        success: function(data) {
            if (data == "false") {
            }
            else {
                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                infoMap = JSON.parse(data)["infoMap"];
                parent.$("#visualChartType").val(JSON.stringify(infoMap));
            }
        }
    });
    var html = '';
    var keys = Object.keys(infoMap);
    html += "<table style='overflow:auto'>";
    for (var i = 0; i < keys.length; i++) {
        html += "<tr height='20px';><td ><a class='mapViewBy1' style='color:#fff' ><span height='20px' onclick='openAdvanceVisual(\"" + keys[i] + "\")'>" + keys[i] + "</span></a></td></tr>";
    }
    html += "</table>";
    $("#advanceChartList").html(html);
    $("#advanceChartList").dialog('open');
}
function openAdvanceVisual(name) {
    $("#advanceChartList").dialog('close');
    $("#gridsterDiv").css({
        'display': 'none'
    });
    $("#xtendChartssTD").css({
        'display': 'none'
    });
    $("#menuBarUITR").css({
        'display': 'none'
    });
    $("#content_1").hide();
    $("#loading").show();
    var html1 = '';
    $('#themeselect').empty();
    $('#saveGraphLi').remove();
    $('#regenerateFilters').remove();
    $('#resetId').remove();
    var html1 = "";
    html1 += '<li id="testCase" class="drpdown menu-item" style="width: 15%"></li><a href="#" style="padding: 1px;width:100%"><span><font size="3" face="verdana" style="font-size: 14px; color: white">Options</font></span></a>';
    html1 += '<ul class="drpcontent" id="themeselect">';
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='createAdvanceVisual(" + parent.$("#graphsId").val() + ")' >Add Visual</a>";
    html1 += "</li>";
    html1 += "<li style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphAnalysis' onclick='editAdvanceVisual(" + parent.$("#graphsId").val() + ")' >Edit Visual</a>";
    html1 += "</li>";
    if (parent.userType !== "ANALYZER") {
        html1 += "<li id='advanceVisual' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Visual</a>";
        html1 += "</li>";
    }
    if (parent.userType !== "ANALYZER") {
        html1 += "<li id='enableAdvanceDrill' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='enableDisableDrill()' >Enable/Disable Drill</a>";
        html1 += "</li>";
    }
    html1 += "<li id='reset' style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='getAdvanceVisuals(" + parent.$("#graphsId").val() + ")' >Reset</a>";
    html1 += "</li>";
    html1 += "<li id='prop' style='text-align;'>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='setProperties(" + parent.$("#graphsId").val() + ")' >Properties</a>";
    html1 += "</li>";
    html1 += "<li>";
    html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
    html1 += "</li>";
    if (parent.userType !== "ANALYZER") {
        html1 += "<li id='reset' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='resequenceGraphs()' >Delete / Resequence</a>";
        html1 += "</li></ul>";
    }
    html1 += "</ul>";
    $('#testCase').html(html1);
    $("#graphAnchor").removeClass('active');
    $("#advanceAnchor").attr('class', 'active');
    getChangeVisual(name);
}

function getGraphObjectReset(reportId, reportName) {
    $("#type").val("graph");
    $.post(
            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + reportId + "&reportName=" + encodeURIComponent(reportName),
            function(data) {

                if (data == "false") {

                    //    return alert("Some Error Occurred");
                } else {

                    var jsondata = JSON.parse(data)["data"];
                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                    var meta = JSON.parse(JSON.parse(data)["meta"]);
                    $("#viewby").val(JSON.stringify(meta["viewbys"]));
                    $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                    $("#measure").val(JSON.stringify(meta["measures"]));
                    $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                    $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                    $("#drilltype").val((meta["drillType"]));
                    $("#filters1").val(JSON.stringify(meta["filterMap"]));
                    $("#timeMap").val(JSON.stringify(meta["timeMap"]));
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

                    var InnerViewbyEleId = "NONE";
                    var rowViewByArray = meta["viewbyIds"];
                    var finalViewBys = meta["viewbys"];
                    var rowMeasArray = meta["measureIds"];
                    var finalMeas = meta["measures"];
                    var AggType = meta["aggregations"];
                    var goUpdate = "Calender Data Set";
                    var userId = parent.$("#userId").val();
                    $.ajax({
                        async: false,
                        type: "POST",
                        data:
                                $('#graphForm').serialize(),
                        url: 'reportViewer.do?reportBy=getFilters',
                        success: function(data) {
                            filterData = JSON.parse(data);
                            var ctxpath = $("#ctxpath").val();
                            $.post(ctxpath + "/reportViewer.do?reportBy=addNewChartsUI&InnerViewbyEleId=" + InnerViewbyEleId + "&rowViewByArray=" + encodeURIComponent(rowViewByArray) + "&reportId=" + reportId + "&userId=" + userId + "&rowViewNamesArr=" + encodeURIComponent(finalViewBys) + "&rowMeasArray=" + encodeURIComponent(rowMeasArray) + "&AggType=" + encodeURIComponent(AggType) + "&rowMeasNamesArr=" + JSON.stringify(encodeURIComponent(finalMeas)) + "&reportName=" + parent.$("#graphName").val() + "&goUpdate=" + goUpdate, parent.$("#graphForm").serialize(),
                                    function(data) {
                                        parent.$("#loading").hide();



                                        $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs", $("#graphForm").serialize(),
                                                function(data) {

                                                    if (parent.isGraphRefresh == "true") {
                                                        $.ajax({
                                                            async: false,
                                                            type: "POST",
                                                            data:
                                                                    $('#graphForm').serialize(),
                                                            url: 'reportViewer.do?reportBy=saveXtCharts',
                                                            success: function(data) {
                                                                //            alert("Your Charts Saved")
                                                            }
                                                        });
                                                    }
                                                });

                                    })


                        }
                    });
                }

            })
}



function checkMeasureNameForGraph(measName) {
    //var measureName = JSON.parse(parent.$("#measure").val());
    //    var measIds = JSON.parse(parent.$("#measureIds").val());
    //var name = [];

    //for(var g=0;g<measIds.length;g++){
    //
    //            if(measureName.indexOf(measName) !== -1){
    //            name.push(measureName[measIds.indexOf(measName)]);
    //        break;
    //        }
    //}
    var timeMapValues = JSON.parse(parent.$("#timeMap").val());


    if (typeof timeMapValues !== "undefined" && timeMapValues !== "" && typeof timeMapValues[measName] !== "undefined") {
        return timeMapValues[measName]
    } else {
        return measName;
    }
}

function getNextGraphValue(flag) {
    var div = "chart1";
    var chartData = JSON.parse($("#chartData").val());
    var chartType = chartData[div]["chartType"];
    if (flag == 'next') {
        start = end;
        end += 15;
    }
    else {
        if (start < 15) {
            start = 0;
            end = 15;
        }
        else {
            end = start;
            start -= 15;
        }
    }
    var total = end + ":" + start;

    advanceTypeFunction(div, chartType, total);
}
function getMoreRecords(chartId, action) {
    var chartData = JSON.parse($("#chartData").val());
    var start = 0, end = 0;
    if (action === 'next') {
        if (graphData[chartId].length < parseInt(chartData[chartId]["records"])) {
            chartData[chartId]["startRecords"] = 0
            chartData[chartId]["endRecords"] = graphData[chartId].length
            start = 0
            end = graphData[chartId].length
        } else {

            start = chartData[chartId]["endRecords"];
            if (typeof start === 'undefined') {
                start = parseInt(chartData[chartId]["records"]);
            }
            end = start + parseInt(chartData[chartId]["records"]);
        }
    }
    else {
        end = chartData[chartId]["startRecords"];
        if (typeof end === 'undefined' || end < chartData[chartId]["records"]) {
            end = parseInt(chartData[chartId]["records"]);
        }
        start = end - parseInt(chartData[chartId]["records"]);
    }

    chartData[chartId]["startRecords"] = start;
    chartData[chartId]["endRecords"] = end;
    chartData[chartId]["others"] = "N";
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs", $("#graphForm").serialize(),
            function(data) {
                //                        generateChart(data,div);
                generateSingleChart(data, chartId);
            });
}
function getNextRecords(chartId, action) {
    var chartData = JSON.parse($("#chartData").val());
    var start = 0, end = 0;

    if (action === 'next') {
        start = chartData[chartId]["startCurrentRecords"];
        if (typeof start === 'undefined') {
            start = 1;
            end = 1 + parseInt(chartData[chartId]["records"])
        } else {

            start = 1 + parseInt(chartData[chartId]["startCurrentRecords"]);
            end = 1 + parseInt(chartData[chartId]["endCurrentRecords"])
        }
    }
    else {
        start = chartData[chartId]["startCurrentRecords"];
        if (typeof start != 'undefined' || start > 0) {
            start = parseInt(chartData[chartId]["startCurrentRecords"]) - 1
            end = parseInt(chartData[chartId]["endCurrentRecords"]) - 1;
        } else {
            start = 0;
            end = parseInt(chartData[chartId]["records"]);
        }
    }

    chartData[chartId]["startCurrentRecords"] = start;
    chartData[chartId]["endCurrentRecords"] = end;
    chartData[chartId]["startRecords"] = start;
    chartData[chartId]["endRecords"] = end;
    chartData[chartId]["others"] = "N";
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs", $("#graphForm").serialize(),
            function(data) {
                //                        generateChart(data,div);
                generateSingleChart(data, chartId);
            });
}
//sandeep
var query;
var allParamIds = "";
var allParamIdsgr = "";
var ctxPath;
var checkId = "";
var scrollFlag = true
var firstscroll = true
var onScrollFlag = 0;
var scrollcheck = true
var unceckall = false
var scrollPayLoad = "";
var scrollURL = ""
var elementname = ""
var scHeight = ""
var scTop = ""
var clHgt = ""
var xmlHttp;
var checkboxContainer;
var tabtype;
var search = false
var selectid;
function resetAjax(country) {
    if (country == "") {
        country = "@";
        search = false
        selectid = query + "__" + elementname
        if (tabtype == "table") {

            $("#" + selectid).html("");
        } else {
            $("#" + selectid).html("");
        }
    } else {
        search = true
    }
    getSuggestionstable(country)
}
function resetmorefilters(id) {
    var x = document.getElementById(id).value;
    resetAjax(x)
}
function openadvancefilter() {
    var REPORTID = $("#REPORTID").val();
    parent.filterBy("DimFilterBy_CBOARP" + query, elementname, REPORTID, query)

}
var localFilterFlag="";
var localFiltereleId="";
var chartId1="";
function lovfiltersreptable(id, elementid, checkboxContainer1, type,chartId) {// Prabal
     localFilterFlag="";
    $(".expandDiv1" + id).toggleClass("expandDivLocal1");
    $("#localFilter" + id).toggle(800);
    tabtype = type;
    elementname = id.replace("__", " ");
    if(checkboxContainer1=="localFilter"){
        chartId1=chartId;//alert(id+"      "+elementid+"     "+chartId1);
        localFiltereleId=id;
        localFilterFlag=checkboxContainer1;
    }else{
    checkboxContainer = checkboxContainer1;
    }
    search = false;
    query = elementid;
    var country = "@";
    gblcount = 0;
    var newArr = new Array();
    if (tabtype == "table") {
        $("#multitypetb" + query).val("");
    } else if (tabtype == "trend") {
        $("#multitypet" + query).val("");
    } else if (tabtype == "graph") {

        $("#multitypeg" + query).val("");
    } else if (tabtype == "advance") {
        $("#multitypea" + query).val("");
    }
    if (tabtype == "table") {
        var keys = Object.keys(parent.filterMapNew)
        for (var k in keys) {
            newArr.push(keys[k]);
        }
        allParamIds = newArr.toString();
        parent.$("#searchtype").val("table")
    } else {
        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        for (var key in viewOvIds) {
            newArr.push(viewOvIds[key]);
        }
        allParamIdsgr = newArr.toString();
        parent.$("#searchtype").val(tabtype)
    }
    // newArr.push(elementid)
    unceckall = false
    scrollFlag = false
    firstscroll = true
    scrollcheck = false
    globalfiltvalues = "";
    globalfiltwsplit = "";
    selectid = query + "__" + elementname
    if (tabtype == "table") {

        $("#" + selectid).html("");
    } else {
        $("#" + selectid).html("");
    }
    
    parent.startValgbl = 1;
    //  html = html + "<select name="+elementid+" id="+id+"  multiple style=''>";
    //    $("#"+elementid).html(html);
    //
    //    $("#"+id).multiselect({
    //                                             selectedText: "# of # selected",
    //                                             noneSelectedText: id,
    //                                             selectedList: 2
    //                                               }).multiselectfilter();
    getSuggestionstable(country)

   
    scrollFlag = true;
}
function onScrollDivtable(divObj) {
    scHeight = (divObj.scrollHeight);
    scTop = (divObj.scrollTop);

    clHgt = (divObj.clientHeight);
    if (scrollFlag) {

        if (scTop == (scHeight - clHgt)) {
            scrollFlag = true;
            firstscroll = false
            scrollcheck = true
            parent.startValgbl = parent.startValgbl + 20;
            scrollPayLoad1 = scrollPayLoad + "&startValue=" + startValgbl;

            sendRequesttable(scrollURL, scrollPayLoad1);
            onScrollFlag = 1;

        }
    }
}

function getSuggestionstable(searchKey) {

    var url;
    //    var measuresId = JSON.parse($("#measureIds").val());
    ctxPath = parent.document.getElementById("h").value;
    //     searchKey='@';
    var searchKey = searchKey;
    var payload;
    var flag;
    if (tabtype == "table") {
        flag = "table"
    } else {
        flag = "graph"
    }
    url = ctxPath + "/dsrv";
    var temp = encodeURIComponent(searchKey); //encodeURIComponent(
    payload = "q=" + temp + "&" + "query=" + query + "&" + "tabtype=" + flag,
            checkId = checkId + payload;
    scrollFlag = false
    sendRequesttable(url, payload);

    parent.startValgbl = 1;
    scrollPayLoad = payload;
}
function sendRequesttable(url, payload)
{
    scrollURL = url;
    xmlHttp = GetXmlHttpObject();
    if (xmlHttp == null) {
        alert("Your browser does not support AJAX!");
        return;
    }
    var parValues;
    if (tabtype == "table") {
        parValues = allParamIds.split(",");
    } else {
        parValues = allParamIdsgr.split(",");
    }
    var idsList = new Array();
    var passValues1 = new Array();
    var passValues;



    for (var i = 0; i < parValues.length; i++) {
        var id = parValues[i].replace("CBOARP", "");
        var filterMap1 = {};
        var filterValues1 = [];
        if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
            filterMap1 = JSON.parse($("#filters1").val());
            if (typeof filterMap1[id] !== "undefined") {
                filterValues1 = filterMap1[id];
            }
        }
        //        var tempArray=new Array();
        idsList.push(id);
        if (i == 0) {
            var selectedlist
            if (tabtype == "table") {
                selectedlist = parent.filterMapNew[id];
            } else {
                selectedlist = parent.filterMapgraphs[id];

            }
            var value;
            if (selectedlist == undefined || selectedlist == "") {
                passValues = "All";
            } else {
                if (selectedlist.length >= 1) {
                    for (var k = 0; k < selectedlist.length; k++) {
                        if (k == 0) {
                            value = selectedlist[k];

                        } else {
                            value += "," + selectedlist[k];
                        }

                    }
                    passValues = encodeURIComponent(value);
                }
            }
            //passValues1.push("ALL");


        } else {
            var selectedlist
            if (tabtype == "table") {
                selectedlist = parent.filterMapNew[id];
            } else {
                selectedlist = parent.filterMapgraphs[id];
            }
            var value;
            if (selectedlist == undefined || selectedlist == "") {
                passValues += ";All"
            } else {
                if (selectedlist.length >= 1) {
                    for (var k = 0; k < selectedlist.length; k++) {
                        if (k == 0) {
                            value = selectedlist[k];

                        } else {
                            value += "," + selectedlist[k];
                        }

                    }
                    passValues += ";" + encodeURIComponent(value);
                }
            }
            //            passValues += ";[ALL]";
        }
        //passValues=encodeURIComponent(passValues);
    }
    var elementid = idsList[0];
    var searchKey = "";
    if (searchKey == "") {
        searchKey = '@';

    }
    var mainUrl = url + "?" + payload + "&fromajaxtype=true&startValue=" + parent.startValgbl + "&elementid=" + query + "&allParamIds=" + idsList + "&parArrVals=" + (passValues) + "&REPORTID=" + parent.$("#REPORTID").val() + "&fromglobal=true&scrollFlag=" + parent.startVal;
    if (checkboxContainer == 'moreFiltersg' || checkboxContainer == 'moreFilters' || checkboxContainer == 'moreFiltersad') {
        getLOVStablenew(mainUrl, elementid);
    }else if(localFilterFlag==="localFilter"){// prabal
//         alert("payload="+payload);
        getLocalFilterData( mainUrl);
    }else {
        getLOVStable(mainUrl, elementid);
    }


}

function getLocalFilterData( mainUrl) {// prabal
     var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId1];
//    var viewIds = chartDetails["viewIds"];
//    if(typeof chartData[chartId1]["dimensions"]!=="undefined" && chartData[chartId1]["dimensions"]!==""){
//        viewIds = chartData[chartId1]["dimensions"];
//    }
    var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var filterMap = {};
    if (typeof chartDetails["filters"] !== "undefined") {
        filterMap = chartDetails["filters"];
    } 
    var filterKeys = chartData[chartId1]["dimensions"];
    if(typeof chartData[chartId1]["dimensions"]==="undefined" || chartData[chartId1]["dimensions"]===""){
        filterKeys=chartData[chartId1]["viewBys"];
    }else{
        
        var temp = filterKeys;
        filterKeys=[];
        for (var key in temp) {
               filterKeys.push(viewOvName[key]);
            
        }
    }
  
 var selectedFilters = [];
    for (var key in filterKeys) {
        var filterGroupBy = viewOvName[key];
        var viewid = viewOvIds[key];
        var filters = filterData[filterGroupBy];
       
        if (typeof filterMap !== "undefined"&&typeof filterMap[viewid] !== "undefined") {
  for(var i in filterMap[viewid]){
            selectedFilters.push(filterMap[viewid][i]);
        }
    }
    }
      $.ajax({
        async: false,
        url: mainUrl,
        success: function(data) {//alert(data);
//            var datagbl = "";
//            if (data != "") {
//                datagbl = JSON.parse(data)["data"];
//            }
            if (data != "") {
                var jsondata = JSON.parse(data)["data"];
                var totalvalues1 = JSON.parse(data)["totalvalues"];
//                var dependtype = JSON.parse(data)["dependtype"];
                var names = jsondata.split("\n");
                var names1 = totalvalues1.split("\n");
                totalvalues = names1;
                if (firstscroll && !search) {
                    globalfiltvalues = names;
                    globalfiltwsplit = jsondata;
                }
                if (scrollcheck) {
                    if (unceckall) {
                        globalfiltwsplit = globalfiltwsplit + jsondata;
                        globalfiltvalues = globalfiltwsplit.split("\n");
                    }
                }
                var html="";
                html+="<div class='gFontFamily gFontSize12 fontWeight400' style='width:100%;height:auto;background-color:#d2d2d2;'><ul>";
                for(var i  in names1){
                   
                    if(names1[i]!=""){
                    if(typeof selectedFilters!="undefined" && selectedFilters.length!=0){
                            if(selectedFilters.indexOf(names1[i])>=0){
                            html += "<li class='gFontFamily gFontSize12 fontWeight400'><input  value='"+ names1[i] +"'   id='L_"+names[i].replace(/\s/g, '')+"_"+i+"'  onclick='applyGlobalFilterinreports(\"" + names1[i] + "\",\"" + names1[i] + "\",this.id)'   class ='selectclass"+localFiltereleId+"'   type='checkbox'  checked/ >"  + names1[i]+ "</li>"                            
                         }else{
                            html +=  "<li class='gFontFamily gFontSize12 fontWeight400'><input  value='"+ names1[i] +"'  id='L_"+names[i].replace(/\s/g, '')+"_"+i+"'  onclick='applyGlobalFilterinreports(\"" + names1[i] + "\",\"" + names1[i]+ "\",this.id)'   class ='selectclass"+localFiltereleId+"'    type='checkbox'/ >"  + names1[i]+ "</li>"                            
                        }
                     }else{
                         html+=  "<li class='gFontFamily gFontSize12 fontWeight400'><input  value='"+ names1[i] +"'   id='L_"+names[i].replace(/\s/g, '')+"_"+i+"'  onclick='applyGlobalFilterinreports(\"" + names1[i] + "\",\"" + names1[i]+ "\",this.id)'  class ='selectclass"+localFiltereleId+"'    type='checkbox'  / >"  + names1[i]+ "</li>"                            
                    }
                }
                }
                html+="</ul></div>";
                 $("#localFilter"+localFiltereleId).css("display","block");
                 if(html.length>130){
                      $("#localFilter"+localFiltereleId).append(html);
                 }
            }
            
              $("#localFilter"+localFiltereleId).on('scroll', function() {
                        setTimeout(parent.onScrollDivtable(this), 2000);
              });
        }
        });
                
}
function hidefilters() {
    $("#showFilters").hide(500)
}
function showfilters(elementid, tabtype, event, left1, top1) {
    $("#showFilters").html("");
    var html = "";
    html += "<table>";
    var selectedlist
    if (tabtype == "table") {
        selectedlist = parent.filterMapNew[elementid];
        if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
            html += "<tr><td style='fontcolor:B36666;'>IN FILTERS:</td></tr>";
        } else {
            html += "<tr><td>NOTIN FILTERS:</td></tr>";
            selectedlist = parent.filterMapNotin[elementid];
        }
    } else {

        selectedlist = parent.filterMapgraphs[elementid];
    }
    if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
        for (var k = 0; k < selectedlist.length; k++) {
            html += "<tr><td>" + selectedlist[k] + "</td></tr>";

        }

        html += "</table>";
        $("#showFilters").html(html);

        $("#showFilters").show(500).css({
            position: "absolute",
            top: top1,
            left: left1
        });
        $("#showFilters").dialog('open');
//       $("#showFilters").dialog({
//                modal: true,
//                title: "Applied Filters",
//               width: 300,
//                height: 150,
//                top:650,
//                open: function (event, ui) {
//                    setTimeout(function () {
//                        $("#showFilters").dialog("close");
//                    }, 2000);
//                }
//        });
    }
}
//var j=0;
function selectall(flag, elementid) {
    if (flag == 'unselect') {
        $('.selectclass' + elementid).prop('checked', false);
        $('.selectall' + elementid).prop('checked', false);
        $('.unselectall' + elementid).prop('checked', true);
        unchektableall(elementid, "uncheckall")
    } else {

        $('.selectclass' + elementid).prop('checked', true);
        $('.unselectall' + elementid).prop('checked', false);
        $('.selectall' + elementid).prop('checked', true);
        unchektableall(elementid, "checkall")
    }
    $("#spanmoreFilters" + query).css('background-color', 'white');
}
function selectallg(flag, elementid, name) {
    if (flag == 'unselect') {
        $('.selectclassg' + elementid).prop('checked', false);
        $('.selectallg' + elementid).prop('checked', false);
        $('.unselectallg' + elementid).prop('checked', true);
        setallfilters(name, elementid, "uncheckall", "graph")
    } else {

        $('.selectclassg' + elementid).prop('checked', true);
        $('.unselectallg' + elementid).prop('checked', false);
        $('.selectallg' + elementid).prop('checked', true);
        setallfilters(name, elementid, "checkall", "graph")
    }
    $("#spangmoreFiltersg" + query).css('background-color', 'white');
}
function selectallad(flag, elementid, name) {
    if (flag == 'unselect') {
        $('.selectclassa' + elementid).prop('checked', false);
        $('.selectalla' + elementid).prop('checked', false);
        $('.unselectallad' + elementid).prop('checked', true);
        setallfilters(name, elementid, "uncheckall", "advance")
    } else {

        $('.selectclassa' + elementid).prop('checked', true);
        $('.unselectalla' + elementid).prop('checked', false);
        $('.selectallad' + elementid).prop('checked', true);
        setallfilters(name, elementid, "checkall", "advance")
    }

    $("#spanadmoreFiltersad" + query).css('background-color', 'white');

}
var globalfiltvalues = "";
var totalvalues = "";
var globalfiltwsplit = "";
var gblcount = 0;
function getLOVStablenew(mainURL, elementid) {
    var html = "";
    var selectedlist1 = "";
    var html1 = "";
    var reportid = parent.$("#REPORTID").val();
    var elementname1 = elementname;
    elementname1 = elementname1.replace("1q1", " ").replace("1q1", " ");
    elementname1 = elementname1.replace("1q1", " ").replace("1q1", " ");
    elementname1 = elementname1.replace("Tr", "");
    elementname1 = elementname1.replace("ad", "");
    if (tabtype == "table") {
        selectedlist1 = parent.filterMapNew[query];
    } else {
        selectedlist1 = parent.filterMapgraphs[query];

    }
    if (!scrollFlag && !search) {
        if (tabtype == "table") {
            $("#moreFilters" + query).html("");
            if (selectedlist1 !== "" && selectedlist1 !== undefined && selectedlist1.length >= 1 && selectedlist1[0] != "All") {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;' onclick=selectall('select',\"" + query + "\")><input type='checkbox' class='selectall" + query + "'  onclick=selectall('select',\"" + query + "\") >Select All</span>"
            } else {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;' onclick=selectall('select',\"" + query + "\")><input type='checkbox'  class='selectall" + query + "' checked onclick=selectall('select',\"" + query + "\") >Select All</span>"
            }
            html1 = html1 + "<span style='color:black;margin-left:10px;' onclick=selectall('unselect',\"" + query + "\")> <input type='checkbox'  class='unselectall" + query + "' onclick=selectall('unselect',\"" + query + "\")>Un-Select All</span></li>"
            html1 += "<li><span style='color:gray;'>Filter</span><input type='text'  value='' id='searchtb" + query + "' style='width:70%;float:right'></li>"
            html1 += "</div><hr style='border-color:#888;margin:0px;'><div id='divmoreFilters" + query + "' style='height:115px;overflow:auto'></div>";
            html1 += "<hr style='border-color:#888;margin:0px;'><div id='advnotlikeFilters" + query + "' style='padding:5px 0px;overflow:auto'><span style='margin-left:1em;' onclick=\"parent.AdvanceFilters('" + query + "','" + elementname1 + "')\">Initialize Filters</span><span style='color:black;margin-left:1em;' onclick=\"filterBy('DimFilterBy_CBOARP" + query + "','" + elementname1 + "','" + reportid + "','" + query + "')\">Advanced Filters</span><i class=\"fa fa-search \" style='margin-left:1em' onclick=\"filterBy('DimFilterBy_CBOARP" + query + "','" + elementname1 + "','" + reportid + "','" + query + "')\"></i></div>";
            $("#moreFilters" + query).html(html1);
            $("#searchtb" + query).keyup(function(event) {
                if (event.keyCode == 13) {

                    resetmorefilters('searchtb' + query)
                }
            });
        } else if (tabtype == "trend") {


        } else if (tabtype == "advance") {
            $("#moreFiltersad" + query).html("");
            if (selectedlist1 !== "" && selectedlist1 !== undefined && selectedlist1.length >= 1 && selectedlist1[0] != "All") {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;'onclick=\"selectallad('select','" + query + "','" + elementname1 + "')\"><input type='checkbox' class='selectallad" + query + "' onclick=\"selectallad('select','" + query + "','" + elementname1 + "')\" >Select All</span>"
            } else {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;'onclick=\"selectallad('select','" + query + "','" + elementname1 + "')\"><input type='checkbox' class='selectallad" + query + "' checked onclick=\"selectallad('select','" + query + "','" + elementname1 + "')\" >Select All</span>"
            }
            html1 = html1 + "<span style='color:black;margin-left:10px;' onclick=\"selectallad('unselect','" + query + "','" + elementname1 + "')\"> <input type='checkbox'  class='unselectallad" + query + "' onclick=\"selectallad('unselect','" + query + "','" + elementname1 + "')\">Un-Select All</span></li>"
            html1 += "<li><span style='color:gray;'>Filter</span><input type='text' onkeyup=\"resetmorefilters('searchad" + query + "')\" id='searchad" + query + "' style='width:70%;float:right'></li>"
            html1 += "</div><hr style='border-color:#888;margin:0px;'><div id='divmoreFiltersad" + query + "' style='height:100px;overflow:auto'></div>";
            $("#moreFiltersad" + query).html(html1);
        } else {
            $("#moreFiltersg" + query).html("");
            if (selectedlist1 !== "" && selectedlist1 !== undefined && selectedlist1.length >= 1 && selectedlist1[0] != "All") {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;' onclick=\"selectallg('select','" + query + "','" + elementname1 + "')\" ><input type='checkbox' class='selectallg" + query + "'  onclick=\"selectallg('select','" + query + "','" + elementname1 + "')\" >Select All</span>"
            } else {
                html1 = html1 + " <div style='height:60px;width:100%;overflow:hidden;'><li><span style='color:black;' onclick=\"selectallg('select','" + query + "','" + elementname1 + "')\" ><input type='checkbox' class='selectallg" + query + "' checked onclick=\"selectallg('select','" + query + "','" + elementname1 + "')\" >Select All</span>"
            }
            html1 = html1 + "<span style='color:black;margin-left:10px;' onclick=\"selectallg('unselect','" + query + "','" + elementname1 + "')\"> <input type='checkbox'  class='unselectallg" + query + "' onclick=\"selectallg('unselect','" + query + "','" + elementname1 + "')\">Un-Select All</span></li>"
            html1 += "<li><span style='color:gray;'>Filter</span><input type='text'  id='searchg" + query + "' style='width:70%;float:right'></li>"
            html1 += "</div><hr style='border-color:#888;margin:0px;'><div id='divmoreFiltersg" + query + "' style='height:100px;overflow:auto'></div>";
            $("#moreFiltersg" + query).html(html1);
            $("#searchg" + query).keyup(function(event) {
                if (event.keyCode == 13) {

                    resetmorefilters('searchg' + query)
                }
            });
        }

    } else {

    }

    $.ajax({
        url: mainURL,
        async: false,
        success: function(data) {

            var html1 = "";
            var datagbl = "";
            if (data != "") {
                datagbl = JSON.parse(data)["data"];
            }
            if (datagbl != "") {

                var jsondata = JSON.parse(data)["data"];
                var totalvalues1 = JSON.parse(data)["totalvalues"];

                var dependtype = JSON.parse(data)["dependtype"];
                var names = jsondata.split("\n");
                var names1 = totalvalues1.split("\n");
                totalvalues = names1;
                var index;
                if (names.length >= 10) {
                    index = names.length;
                } else {
                    index = names.length;
                }
                 if(typeof $("#notfilters").val()!=="undefined" && $("#notfilters").val()!==""){
        parent.filterMapNotinDB = JSON.parse($("#notfilters").val());
       
    }
                var selectedlist
                var selnotinlist;var selnotinlistDB;
                if (tabtype == "table") {
                    selectedlist = parent.filterMapNew[query];
                    selnotinlist = parent.filterMapNotin[query];
                } else {
                    filterValues1 = totalvalues;
                    selectedlist = parent.filterMapgraphs[query];
                     selnotinlistDB = parent.filterMapNotinDB[query];
                }

                //var selectedlist=parent.filterMapNew[query];
                if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
                    var idval;
                    var nameval;
                    var value;
                    var inputID;

                    if (firstscroll) {
                        for (var k = 0; k < selectedlist.length; k++) {
                            // var value=selectedlist[k]+"_"+k+"_"+query
                            var value;
                            var id;
                            if (tabtype == "table") {
                                inputID = 'ui-multiselect-' + (elementname + '-option-' + k);
                                idval = (elementname.replace("table", "") || elementname.replace("table", "")) + "_" + k;
                                nameval = (elementname.replace("table", "") || elementname.replace("table", "")) + "*," + query;
                                value = selectedlist[k].replace("_", " ") + "_" + k + "_" + query;

                                if (selectedlist[k] != "All" || selectedlist[k] != "ALL" || selectedlist[k] != "") {
                                    html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreports(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclass" + query + "' value='" + value + "' type='checkbox'/ checked>" + selectedlist[k] + "</li>"
                                }
                            } else {
                                if (tabtype == "trend") {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + k);
                                    idval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "_" + k;
                                    nameval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "*," + query;
                                    value = selectedlist[k].replace("_", " ") + "_" + k + "_" + query;
                                } else if (tabtype == "advance") {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + k);
                                    idval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "_" + k;
                                    nameval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "*," + query;
                                    value = selectedlist[k].replace("_", " ") + "_" + k + "_" + query;
                                    if (selectedlist[k] != "All" || selectedlist[k] != "ALL" || selectedlist[k] != "") {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassa" + query + "' value='" + value + "' type='checkbox'/ checked>" + selectedlist[k] + "</li>"
                                    }
                                } else {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + k);
                                    idval = (elementname || elementname) + "_" + k;
                                    nameval = (elementname || elementname) + "*," + query;
                                    value = selectedlist[k].replace("_", " ") + "_" + k + "_" + query;
                                    if (selectedlist[k] != "All" || selectedlist[k] != "ALL" || selectedlist[k] != "") {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassg" + query + "' value='" + value + "' type='checkbox'/ checked>" + selectedlist[k] + "</li>"
                                    }
                                }

                            }



                        }
                    }
                    for (var j = 0; j < names.length - 1; j++)
                    {
                        var flag = "true";
                        for (var g = 0; g < selectedlist.length; g++) {

                            if (names[j] == selectedlist[g]) {
                                flag = "false";

                            }
                        }
                        if (flag == "true") {
                            //    var value=names[j]+"_"+j+"_"+query
                            var value;
                            var id;
                            if (tabtype == "table") {
                                var inputID = 'ui-multiselect-' + (elementname + '-option-' + j);
                                var idval = (elementname.replace("table", "") || elementname.replace("table", "")) + "_" + j;
                                var nameval = (elementname.replace("table", "") || elementname.replace("table", "")) + "*," + query;
                                var value = names[j].replace("_", " ") + "_" + j + "_" + query;
                                if (names[j] != "") {
                                    html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreports(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclass" + query + "' value='" + value + "' type='checkbox'/ >" + names[j] + "</li>"
                                }
                            } else {
                                if (tabtype == "trend") {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + j);
                                    idval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "_" + j;
                                    nameval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "*," + query;
                                    value = names[j].replace("_", " ") + "_" + j + "_" + query;
                                } else if (tabtype == "advance") {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + j);
                                    idval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "_" + j;
                                    nameval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "*," + query;
                                    value = names[j].replace("_", " ") + "_" + j + "_" + query;
                                    if (names[j] != "") {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassa" + query + "' value='" + value + "' type='checkbox'/ >" + names[j] + "</li>"
                                    }
                                } else {
                                    inputID = 'ui-multiselect-' + (elementname + '-option-' + j);
                                    idval = (elementname || elementname) + "_" + j;
                                    nameval = (elementname || elementname) + "*," + query;
                                    value = names[j].replace("_", " ") + "_" + j + "_" + query;
                                    if (names[j] != "") {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassg" + query + "' value='" + value + "' type='checkbox'/ >" + names[j] + "</li>"
                                    }
                                }

                            }
                            if (unceckall) {

                            } else {
                                if (!firstscroll) {
                                    value = value + "_selecttrue";
                                }
                            }


                            flag = "false";
                        }

                    }
                } else {
                    for (var i1 = 0; i1 < index; i1++) {
                        var i11 = i1;
                        if (scrollFlag) {
                            i11 = i1 + 20;
                        }
                        var idval;
                        var nameval;
                        var value;
                        var inputID;
                        if (tabtype == "table") {
                            if (names[i1] != "") {
                          var flagnotin=false;
                                if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {
                                    for (var k = 0; k < selnotinlist.length; k++) {
                                        if (names[i1] == selnotinlist[k]) {
                                           flagnotin=true
                                        }
                                    }
                                } 
                            
                                inputID = 'ui-multiselect-' + (elementname + '-option-' + i11);
                                idval = (elementname.replace("table", "") || elementname.replace("table", "")) + "_" + i11;
                                nameval = (elementname.replace("table", "") || elementname.replace("table", "")) + "*," + query;
                                value = names[i1].replace("_", " ") + "_" + i11 + "_" + query;
                                if (selectedlist[k] != "All" || selectedlist[k] != "ALL") {
                                    if (unceckall) {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreports(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclass" + query + "' value='" + value + "' type='checkbox'/ >" + names[i1] + "</li>"


                                    } else {
                                        if(flagnotin){
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreports(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclass" + query + "' value='" + value + "' type='checkbox'/ >" + names[i1] + "</li>"
                                            
                                        }else{
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreports(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclass" + query + "' value='" + value + "' type='checkbox'/ checked>" + names[i1] + "</li>"
                                    }
                                }
                                }

                            }
                        } else {
                            if (tabtype == "trend") {
                                inputID = 'ui-multiselect-' + (elementname + '-option-' + i11);
                                idval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "_" + i11;
                                nameval = (elementname.replace("Tr", "") || elementname.replace("Tr", "")) + "*," + query;
                                value = names[i1].replace("_", " ") + "_" + i11 + "_" + query;
                            } else if (tabtype == "advance") {
                                inputID = 'ui-multiselect-' + (elementname + '-option-' + i11);
                                idval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "_" + i11;
                                nameval = (elementname.replace("ad", "") || elementname.replace("ad", "")) + "*," + query;
                                if (names[i1] != "") {
                                    if (unceckall) {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassa" + query + "' value='" + value + "' type='checkbox'/ >" + names[i1] + "</li>"

                                    } else {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassa" + query + "' value='" + value + "' type='checkbox'/ checked>" + names[i1] + "</li>"
                                    }
                                }
                                value = names[i1].replace("_", " ") + "_" + i11 + "_" + query;
                            } else {
                                inputID = 'ui-multiselect-' + (elementname + '-option-' + i11);
                                idval = (elementname || elementname) + "_" + i11;
                                nameval = (elementname || elementname) + "*," + query;
                                value = names[i1].replace("_", " ") + "_" + i11 + "_" + query;
                                if (names[i1] != "") {
                                    var flagnotin=false;
                                if (selnotinlistDB !== "" && selnotinlistDB !== undefined && selnotinlistDB.length >= 1 && selnotinlistDB[0] != "All") {
                                    for (var k = 0; k < selnotinlistDB.length; k++) {
                                        if (names[i1] == selnotinlistDB[k]) {
                                           flagnotin=true
                                        }
                                    }
                                }
                                    if (unceckall) {
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassg" + query + "' value='" + value + "' type='checkbox'/ >" + names[i1] + "</li>"

                                    } else {
                                        if(flagnotin){
                                           html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassg" + query + "' value='" + value + "' type='checkbox'/ >" + names[i1] + "</li>"
                                         
                                        }else{
                                        html = html + "<li class='gFontFamily gFontSize12 fontWeight400'><input id=" + inputID + "  onclick=applyGlobalFilterinreport(\"" + idval + "\",\"" + nameval + "\",\"" + inputID + "\") class ='selectclassg" + query + "' value='" + value + "' type='checkbox'/ checked>" + names[i1] + "</li>"
                                    }
                                }
                            }
                            }

                        }

                    }
                }
//                                                  if(names.length>=10){
//                                                     html = html + "<li style='margin-left:5em;'><span>See More..</span></li>";
//                                                  }
//                                                  $("#morefilter"+query).toggle(500);
                if (html != "") {
                    if (checkboxContainer == 'moreFilters') {
                        if (scrollFlag) {
                            $("#divmoreFilters" + query).append(html);

                        } else {
                            if (search) {
                                $("#divmoreFilters" + query).html("");
                            }
                            $("#divmoreFilters" + query).html(html);
                        }
                    } else {
                        if (tabtype == "trend") {
                            if (search) {
                                $("#divmoreFilterst" + query).html("");
                            }

                            if (scrollFlag) {
                                $("#divmoreFilterst" + query).append(html);

                            } else {
                                $("#divmoreFilterst" + query).html(html);
                            }
                        } else if (tabtype == "advance") {
                            if (search) {
                                $("#divmoreFilterad" + query).html("");
                            }
                            if (scrollFlag) {
                                $("#divmoreFiltersad" + query).append(html);

                            } else {
                                $("#divmoreFiltersad" + query).html(html);
                            }
                        } else {
                            if (search) {
                                $("#divmoreFiltersg" + query).html("");
                            }
                            if (scrollFlag) {
                                $("#divmoreFiltersg" + query).append(html);

                            } else {
                                $("#divmoreFiltersg" + query).html(html);
                            }
                        }
                    }

                }
                if (tabtype == "table") {
                    $("#divmoreFilters" + query).on('scroll', function() {
                        var obj = $("#moreFilters" + query);
                        setTimeout(parent.onScrollDivtable(this), 2000);
//      setTimeout($.proxy(self1.refresh, self1), 2000);
                        var scHeight = (this.scrollHeight);
                        var scTop = (this.scrollTop);
                        var clHgt = (this.clientHeight);

                    })
                } else if (tabtype == "ternd") {

                } else if (tabtype == "advance") {
                    $("#divmoreFiltersad" + query).on('scroll', function() {

                        setTimeout(parent.onScrollDivtable(this), 2000);
//      setTimeout($.proxy(self1.refresh, self1), 2000);
                        var scHeight = (this.scrollHeight);
                        var scTop = (this.scrollTop);
                        var clHgt = (this.clientHeight);

                    })
                } else {
                    $("#divmoreFiltersg" + query).on('scroll', function() {

                        setTimeout(parent.onScrollDivtable(this), 2000);
//      setTimeout($.proxy(self1.refresh, self1), 2000);
                        var scHeight = (this.scrollHeight);
                        var scTop = (this.scrollTop);
                        var clHgt = (this.clientHeight);

                    })
                }
//                                                 var html="";
//                                                 var arrowid="up"+query;
//                                                   var ctxPath=parent.document.getElementById("h").value;
//
//  if(document.getElementById(arrowid)!=null && document.getElementById(arrowid).style.display==''){
//  html += "<img  id="+arrowid+"  id='down"+query+"' alt=''  border='0px' style='height:17px;'src='"+ctxPath+"/images/arrow_down.png'/>";
//}else{
//     html += "<img    id='up"+query+"'alt=''  border='0px'   style='height:17px;' title='Show/Hide Filters'   src='"+ctxPath+"/images/arrow_up.png' />";
//}
//$('#showfiltr'+query).html('');
//$('#showfiltr'+query).html(html);

            }
        }
    });
}
//sandeep
var sortapply;
var sortDescend;
var colName;
var dataType;
var tabId;
//function setsortcol(elementid,sorttype,datatype,reportid){
//   colName=elementid
//sortapply="s";
//sortDescend=sorttype
//dataType=datatype
//tabId=reportid
//}
var objectarth;
var secObj;

function setlogicalarth(event, obj) {
    event.stopPropagation();
    objectarth = obj;
}
function setlogicalarth1(obj) {
    secObj = obj;
}
//var logicalvalue="";


var ctxPath;

function opensubForm(elemntid, submenu)
{
    var html = "";
    html = html + "<ul id='Formatting" + elemntid + "' class='dropdown-menu'>";
    html += "<li  id='Rounding" + elemntid + "' class='dropdown-submenu pull-right1'><a onmouseover=openRsubmenu(\"" + elemntid + "\")>Rounding</a></li>";
    html += "<li  id='Header_Align" + elemntid + "' class='dropdown-submenu pull-right1'><a onmouseover=openHAlign(\"" + elemntid + "\")>Header Align</a></li>";
    html += "<li  id='Data_Align" + elemntid + "' class='dropdown-submenu pull-right1'><a onmouseover=openDAlign(\"" + elemntid + "\")>Data Align</a></li>";
    html += "<li ><a onclick=\"parent.renameMeasure('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\" >Parameter Rename</a></li>";
    // html+="<li  id='Totals_Display"+elemntid+"' class='dropdown-submenu pull-right1'><a onmouseover=openTDisplay(\""+elemntid+"\")>Totals Display</a></li>";

    html += "</ul>";
    $("#Formatting" + elemntid).remove();
    $("#liFormatting" + elemntid).append(html);
//    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liFormatting" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liFormatting" + elemntid + " ul").css("left", "-" + ($("#liFormatting" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!

}
function goSavenew(reportId, pageSize, totaltype) {
    var GT;
    var ST;
    var OA;
    var CA;
    var count;
    if (totaltype == 'GrandTotal') {
        GT = "true"
    } else if (totaltype == 'SubTotal') {
        ST = "true"
    } else if (totaltype == 'OverallAverage') {
        OA = "true"
    } else if (totaltype == 'CategoryAverage') {
        CA = "true"
    } else {
        count = "true"
    }
    $.post(ctxPath1 + "/reportViewer.do?reportBy=tableChanges&tableChange=tableProperties&reportid=" + reportId + "&GrandTotalReq=" + GT + "&NetTotalReq=" + ST + "&AvgTotalReq=" + OA + "&categoryAvg=" + CA + "&rowCount=" + count + "&slidePages=" + pageSize, $("#grpProForm").serialize(),
            function(data) {
                if (data == "refresh")
                {

                    refreshReportTables11(ctxPath1, reportId, pageSize);
                }
            });
}
function openTDisplay(elemntid)
{
    var html = "";
    html = html + "<ul id='Totals_Display" + elemntid + "' class='dropdown-menu'>";
    html += "<li><a onclick=goSavenew('" + ReportId + "','null','GrandTotal')>Grand Total</a></li>";
    html += "<li><a onclick=goSavenew('" + ReportId + "','null','SubTotal')>Sub Total</a></li>";
    html += "<li><a onclick=goSavenew('" + ReportId + "','null','OverallAverage')>Overall Average</a></li>";
    html += "<li><a onclick=goSavenew('" + ReportId + "','null','CategoryAverage')>Category Average</a></li>";
    html += "<li><a onclick=goSavenew('" + ReportId + "','null','Count')>Count</a></li>";


    html += "</ul>";
    $("#TotalsDisplay" + elemntid).remove();
    $("#Totals_Display" + elemntid).append(html);
}
function openDAlign(elemntid)
{
    var dleft;
    var dright;
    var dcenter;
    var html = "";
    if (dataalign == "left" || dataalign == "Left")
        dleft = "#D3D3D3"
    else if (dataalign == "right" || dataalign == "Right")
        dright = "#D3D3D3"
    else
        dcenter = "#D3D3D3"
    html = html + "<ul id='DataAlign" + elemntid + "' class='dropdown-menu'>";
    html += "<li style='background-color:" + dleft + "'><a onclick=parent.saveScriptAlignnew('left','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Left</a></li>";
    html += "<li style='background-color:" + dright + "'><a onclick=parent.saveScriptAlignnew('right','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Right</a></li>";
    html += "<li style='background-color:" + dcenter + "'><a onclick=parent.saveScriptAlignnew('center','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Center</a></li>";


    html += "</ul>";
    $("#DataAlign" + elemntid).remove();
    $("#Data_Align" + elemntid).append(html);
}
function openHAlign(elemntid)
{
    var hleft;
    var hright;
    var hcenter;
    var html = "";
    if (headeralign == "left" || headeralign == "Left")
        hleft = "#D3D3D3"
    else if (headeralign == "right" || headeralign == "Right")
        hright = "#D3D3D3"
    else
        hcenter = "#D3D3D3"
    html = html + "<ul id='HeaderAlign" + elemntid + "' class='dropdown-menu'>";
    html += "<li style='background-color:" + hleft + "'><a onclick=parent.saveMeasureAlignnew('left','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Left</a></li>";
    html += "<li style='background-color:" + hright + "'><a onclick=parent.saveMeasureAlignnew('right','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Right</a></li>";
    html += "<li style='background-color:" + hcenter + "'><a onclick=parent.saveMeasureAlignnew('center','" + ctxPath1 + "','" + ReportId + "','A_" + elemntid + "')>Center</a></li>";


    html += "</ul>";
    $("#HeaderAlign" + elemntid).remove();
    $("#Header_Align" + elemntid).append(html);
}
function openRsubmenu(elemntid)
{
    var rounding0;
    var rounding1;
    var rounding2;
    var rounding3;
    var rounding4;
    var rounding5;
    if (rounding == "1")
        rounding1 = "#D3D3D3"
    else if (rounding == "2")
        rounding2 = "#D3D3D3"
    else if (rounding == "3")
        rounding3 = "#D3D3D3"
    else if (rounding == "4")
        rounding4 = "#D3D3D3"
    else if (rounding == "5")
        rounding5 = "#D3D3D3"
    else
        rounding0 = "#D3D3D3"
    var html = "";
    html = html + "<ul id='rounding" + elemntid + "' class='dropdown-menu'>";
    html += "<li style='background-color:" + rounding0 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','0')\" >No Decimal</a></li>";
    html += "<li style='background-color:" + rounding1 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','1')\" >One Decimal</a></li>";
    html += "<li style='background-color:" + rounding2 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','2')\" >Two Decimal</a></li>";
    html += "<li style='background-color:" + rounding3 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','3')\" >Three Decimal</a></li>";
    html += "<li style='background-color:" + rounding4 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','4')\" >Four Decimal</a></li>";
    html += "<li style='background-color:" + rounding5 + "'><a onclick=\"parent.rounding('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5')\" >Five Decimal</a></li>";


    html += "</ul>";
    $("#rounding" + elemntid).remove();
    $("#Rounding" + elemntid).append(html);
}
// added by sandeep for Rank track_id-->TA_R_265
function openrankforAO(elementid,isCrossTabReport,submenu){
    var html="";
    html = html + "<ul id='RankAochild"+elementid+"' class='dropdown-menu'>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('RankColumn','_rank','(Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (MTD)</a></li>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('QtdRank','_Qtdrank','(QTD-Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (QTD)</a></li>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('YtdRank','_Ytdrank','(YTD-Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (YTD)</a></li>";

    html+="</ul>";
    $("#RankAochild"+elementid).remove();
    $("#RankAo"+elementid).append(html);
}
function openrankforAOp(elementid,isCrossTabReport,submenu){
    var html="";
    html = html + "<ul id='RankAochildp"+elementid+"' class='dropdown-menu'>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('PMtdRank','_PMtdrank','(PMTD-Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (PMTD)</a></li>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('PQtdRank','_PQtdrank','(PQTD-Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (PQTD)</a></li>";
    html+="<li><a onclick=\"parent.addRuntimeColumn('PYtdRank','_PYtdrank','(PYTD-Rank)','A_"+elementid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\">Rank (PYTD)</a></li>";

    html+="</ul>";
    $("#RankAochildp"+elementid).remove();
    $("#RankAop"+elementid).append(html);
}
  
function opensubmenuan(elemntid,isCrossTabReport,submenu)
{
    var html = "";
     var classname;
    if(submenu=='right'){
        classname='dropdown-submenu pull-right1'
    }else{
        classname='dropdown-submenu pull-left1'
    }
    html = html + "<ul id='groups" + elemntid + "' class='dropdown-menu'>";
    html += "<li  id='show_wise" + elemntid + "' class='dropdown-submenu pull-right1'><a onmouseover=openperwise(\"" + elemntid + "\")>Show % Wise</a></li>";
    if (isCrossTabReport == 'false') {
        html += "<li id='Top_Bottom" + elemntid + "'  class='dropdown-submenu pull-right1'><a onmouseover=opentopbottom(\"" + elemntid + "\")>Top/Bottom</a></li>";
        html += "<li id='top_bottomWise" + elemntid + "' class='dropdown-submenu pull-right1'><a onmouseover=openpersWiseTB(\"" + elemntid + "\")>Top/Bottom(% Basis)</a></li>";
    }
    html += "<li  id='color_grouping" + elemntid + "'  class='dropdown-submenu pull-right1'><a onmouseover=opencolorgroup(\"" + elemntid + "\",\"" + isCrossTabReport + "\")>Color Grouping</a></li>";
     if(isCrossTabReport=='false'){
        html+="<li ><a onclick=\"parent.addRuntimeColumn('RunningTotalColumn','_rt','(Running-Total)','A_"+elemntid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\" >Running Total</a></li>";
       // added by sandeep for MTD,QTD YTD Ranks
       if(AOFlag.length>=1){
            html+="<li id='RankAo"+elemntid+"' class='"+classname+"'  ><a onmouseover=openrankforAO(\""+elemntid+"\",\""+isCrossTabReport+"\",\""+submenu+"\") >Rank</a></li>";
            html+="<li id='RankAop"+elemntid+"' class='"+classname+"' ><a onmouseover=openrankforAOp(\""+elemntid+"\",\""+isCrossTabReport+"\",\""+submenu+"\") >Rank(Prev)</a></li>";
   
        }else{
            html+="<li ><a onclick=\"parent.addRuntimeColumn('RankColumn','_rank','(Rank)','A_"+elemntid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\" >Rank</a></li>";
            
            html+="<li ><a onclick=\"parent.addPriorRuntimeColumn('_rank','(Rank)','A_"+elemntid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\" >Rank(Prev)</a></li>";
    }
        html+="<li ><a onclick=\"parent.addRuntimeColumn('RankST','_rankST','(Rank-ST)','A_"+elemntid+"','"+elementname+"','"+ReportId+"','"+ctxPath1+"')\" >Rank(Sub Total)</a></li>";
    }
    html += "</ul>";
    $("#groups" + elemntid).remove();
    $("#liAnalytical" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liAnalytical" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liAnalytical" + elemntid + " ul").css("left", "-" + ($("#liAnalytical" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!

}
function opencolorgroup(elemntid, isCrossTabReport)
{
    var html = "";
    html = html + "<ul id='colorgrouping" + elemntid + "' class='dropdown-menu'>";
    if (isCrossTabReport == 'false') {
        html += "<li><a onclick=parent.applycolor('A_" + elemntid + "','N','" + ReportId + "','" + ctxPath1 + "') >Create/Edit</a></li>";
        html += "<li><a onclick=parent.resetcolor('A_" + elemntid + "','N','" + ReportId + "') >Reset</a></li>";
    } else {
        html += "<li><a onclick=parent.applycolor('" + elemntid + "','N','" + ReportId + "','" + ctxPath1 + "') >Create/Edit</a></li>";
        html += "<li><a onclick=parent.resetcolor('" + elemntid + "','N','" + ReportId + "') >Reset</a></li>";
    }

    html += "</ul>";
    $("#colorgrouping" + elemntid).remove();
    $("#color_grouping" + elemntid).append(html);
}
function openpersWiseTB(elemntid)
{
    var html = "";
    html = html + "<ul id='topbottomWise" + elemntid + "' class='dropdown-menu'>";
    html += "<li><a onclick=\"parent.topRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Top 5%</a></li>";
    html += "<li><a onclick=\"parent.bottomRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Bottom 5%</a></li>";
    html += "<li><a onclick=\"parent.topRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','10','" + ctxPath1 + "')\" >Top 10%</a></li>";
    html += "<li><a onclick=\"parent.bottomRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Bottom 10%</a></li>";
    html += "<li><a onclick=\"parent.topRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','25','" + ctxPath1 + "')\" >Top 25%</a></li>";
    html += "<li><a onclick=\"parent.bottomRowsPercentWise('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Bottom 25%</a></li>";


    html += "</ul>";
    $("#topbottomWise" + elemntid).remove();
    $("#top_bottomWise" + elemntid).append(html);
}
function openperwise(elemntid)
{
    var html = "";
    html = html + "<ul id='showwise" + elemntid + "' class='dropdown-menu' style='width:180px'>";
    html += "<li><a onclick=\"parent.addPercentColumn('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "','false','false')\" >% Wise</a></li>";
    html += "<li><a onclick=\"parent.addPercentColumn('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "','false','true')\" >% Wise With Absolute</a></li>";
    if (rowviewlist.length >= 2)
    {
        html += "<li><a onclick=\"parent.addPercentColumn('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "','true','false')\" >% Wise(subtotal)</a></li>";
        html += "<li><a onclick=\"parent.addPercentColumn('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "','true','true')\" >% Wise With Absolute(subtotal)</a></li>";
    }

    html += "</ul>";
    $("#showwise" + elemntid).remove();
    $("#show_wise" + elemntid).append(html);
}
function opentopbottom(elemntid)
{
    var html = "";
    html = html + "<ul id='TopBottom" + elemntid + "' class='dropdown-menu'>";
    html += "<li><a onclick=\"parent.toprows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Top 5</a></li>";
    html += "<li><a onclick=\"parent.bottomrows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Bottom 5</a></li>";
    html += "<li><a onclick=\"parent.toprows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','10','" + ctxPath1 + "')\" >Top 10</a></li>";
    html += "<li><a onclick=\"parent.bottomrows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','10','" + ctxPath1 + "')\" >Bottom 10</a></li>";
    html += "<li><a onclick=\"parent.toprows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','25','" + ctxPath1 + "')\" >Top 25</a></li>";
    html += "<li><a onclick=\"parent.bottomrows('A_" + elemntid + "','" + elementname + "','" + ReportId + "','25','" + ctxPath1 + "')\" >Bottom 25</a></li>";
    html += "<li><a onclick=\"parent.toprowsWithOthers('A_" + elemntid + "','" + elementname + "','" + ReportId + "','5','" + ctxPath1 + "')\" >Top 5 With Others</a></li>";
    html += "<li><a onclick=\"parent.toprowsWithOthers('A_" + elemntid + "','" + elementname + "','" + ReportId + "','10','" + ctxPath1 + "')\" >Top 10 With Others</a></li>";


    html += "</ul>";
    $("#TopBottom" + elemntid).remove();
    $("#Top_Bottom" + elemntid).append(html);
}
function openCustomF(elemntid, submenu, isCustMeasure)
{
    var html = "";
    html = html + "<ul id='liCustom" + elemntid + "' class='dropdown-menu'>";
    //Added By Bhargavi For AO reports
    // alert(AOFlag);
    if (AOFlag.length >= 1) {
    } else {
        html += "<li ><a onclick=\"parent.addParamFilter('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\" >Apply Parameter Filter</a></li>";
        html += "<li ><a onclick=\"parent.addFactFilter('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">Fact Filter</a></li>";

    }
    //end of code by bhargavi     
    //Added By Mohit Gupta to edit custom formula
    if (isCustMeasure == "true") {
        html += "<li ><a onclick=\"parent.editMeasures('A_" + elemntid + "','" + elementname + "','" + ReportId + "')\">Edit Formula</a></li>";
    }//end
    html += "<li ><a onclick=\"parent.advanceFormula('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">Single Fact Formula</a></li>";
    html += "<li ><a onclick=\"parent.addSignConversion('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">Sign Conversion</a></li>";
    //modified by anitha
    html += "<li ><a onclick=\"parent.addConversionFormula('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">Conversion Formula</a></li>";
    html += "<li ><a onclick=\"parent.addQuickTimeBasedFormula('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\" >Custom Time Aggregation</a></li>";
    html += "<li ><a onclick=\"parent.createSegmentation('A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">Create Segmentaion</a></li>";
//           html+="<li ><a>Edit Date Join</a></li>";


    html += "</ul>";
    $("#liCustom" + elemntid).remove();
    $("#liCustomF" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liCustomF" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liCustomF" + elemntid + " ul").css("left", "-" + ($("#liCustomF" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!

}
function openStatistc(elemntid, submenu, measures, messvalue1)
{
    var html = "";
    html = html + "<ul id='liStat" + elemntid + "' class='dropdown-menu'>";
    html += "<li ><a onclick=\"parent.statistics('A_" + elemntid + "','" + elementname + "','" + ReportId + "','Mean','" + ctxPath1 + "')\" >Mean</a></li>";
    html += "<li ><a onclick=\"parent.statistics('A_" + elemntid + "','" + elementname + "','" + ReportId + "','Median','" + ctxPath1 + "')\" >Median</a></li>";
    html += "<li  id='Correlation" + elemntid + "'  class='dropdown-submenu pull-right1'><a onmouseover=\"getMeasures('" + measures + "','" + elemntid + "','" + elementname + "','" + messvalue1 + "','" + elemntid + "')\">Correlation Coefficient</a></li>";
    html += "<li><a onclick=\"parent.statistics('A_" + elemntid + "','" + elementname + "','" + ReportId + "','SD','" + ctxPath1 + "')\" >Std. Deviation</a></li>";
    html += "<li><a onclick=\"parent.statistics('A_" + elemntid + "','" + elementname + "','" + ReportId + "','Variance','" + ctxPath1 + "') \">Variance</a></li>";
    html += "<li><a onclick=\"parent.addRuntimeColumn('DeviationFromMean','_deviation_mean','(Running-Total)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\" >Deviation from Mean</a></li>";


    html += "</ul>";
    $("#liStat" + elemntid).remove();
    $("#liStatistc" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liStatistc" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liStatistc" + elemntid + " ul").css("left", "-" + ($("#liStatistc" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!
}
function openpriorEnable(elemntid, submenu, measures, ReportId)
{

    var html = "";
    html = html + "<ul id='liStat" + elemntid + "' class='dropdown-menu'>";
    html += "<li ><a onclick=\"parent.priorEnable('A_" + elemntid + "','priorenable','" + ReportId + "','" + ctxPath1 + "')\" >Apply Color</a></li>";
    html += "<li><a onclick=\"parent.priorResetcolor('A_" + elemntid + "','priorenableReset','" + ReportId + "','" + ctxPath1 + "')\" >Reset</a></li>";


    html += "</ul>";
    $("#liStat" + elemntid).remove();
    $("#prior" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#prior" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#prior" + elemntid + " ul").css("left", "-" + ($("#prior" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!
}
function openCaggrigation(elemntid, submenu)
{
    var html = "";
    html = html + "<ul id='ulAggregation" + elemntid + "' class='dropdown-menu'>";
    html += "<li ><a>MTD</a></li>";
    html += "<li ><a>QTD</a></li>";
    html += "<li ><a>YTD</a></li>";
    html += "<li ><a>MTD(Prev Month)</a></li>";
    html += "<li ><a>MTD(Prev Month Year)</a></li>";
    html += "<li ><a>QTD(Prev Quarter)</a></li>";
    html += "<li ><a>QTD(Prev Quarter Year)</a></li>";
    html += "<li ><a>YTD(Prev Year)</a></li>";
    html += "</ul>";
    $("#ulAggregation" + elemntid).remove();
    $("#liAggregation" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liAggregation" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liAggregation" + elemntid + " ul").css("left", "-" + ($("#liAggregation" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!
}
function openComparsion(elemntid, submenu)
{
    var html = "";
    html = html + "<ul id='ulComparsion" + elemntid + "' class='dropdown-menu'>";
    html += "<li ><a>MOM</a></li>";
    html += "<li ><a>QOQ</a></li>";
    html += "<li ><a>YOY</a></li>";
    html += "<li ><a>MOM(Year)</a></li>";
    html += "<li ><a>QOQ(Year)</a></li>";
    html += "<li ><a>MOM %</a></li>";
    html += "<li ><a>QOQ %</a></li>";
    html += "<li ><a>YOY %</a></li>";
    html += "<li ><a>MOM(Year)%</a></li>";
    html += "<li ><a>QOQ(Year)%</a></li>";

    html += "</ul>";
    $("#ulComparsion" + elemntid).remove();
    $("#liComparsion" + elemntid).append(html);
    //    Added by Faiz Ansari
    if (submenu == "firstmeasure") {
        $("#liComparsion" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liComparsion" + elemntid + " ul").css("left", "-" + ($("#liComparsion" + elemntid + " ul").width() + 1) + "px");
    }
//    End!!!
}
function getMeasures(measures, elemntid, presentename, messvalue1, presentenmid)
{
//   alert("mesurename"+mesurename);




    var measureid = messvalue1.split(",");

    var measure = measures.split(",");

    var html = "";
    html = html + "<ul id='correGroup" + elemntid + "' class='dropdown-menu'>";
    for (var i = 0; i < measure.length; i++)
    {


        var temp = measure[i].replace("[", "").replace("]", "");
        var temp1 = measureid[i].replace("[", "").replace("]", "");
//     alert(temp)
        html += "<li ><a onclick=\"parent.getCorrelation('A_" + presentenmid + "','A_" + temp1 + "','" + presentename + "','" + temp + "','" + ReportId + "','Correlation')\" >" + temp + "</a> </li>";

    }
    html += "</ul>";
    $("#Correlation" + elemntid).append(html);



}
function changesearch(event, id, elementid, logicalArth, isCrossTabReport, gtCrossTabLastButOneCol, submenu) {
    event.stopPropagation();
    var elementidlogic = elementid1;
    var PbReportIdlogic = ReportId1111;
    var ctxPathlogic = ctxPath1;

//  var logicalvalue=logicalArth.value;

    var html = "";
    var logivalvalue = $("#" + logicalArth + " option:selected").val();
    if (logivalvalue == 'BT') {
        var width = "40%";
//       $("#1srch2").css('width', width);
        $("#1srch2" + elementid).remove();
        if (isCrossTabReport == 'false') {
            html += "<input class='inputbox' id='1srch4" + elementid + "' type='text' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "',document.getElementById('2srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:right;border:1px solid lightgray;height:19px;'>";
            html += "<input class='inputbox' id='2srch4" + elementid + "' type='text' onclick='event.stopPropagation();'  onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "',,'" + ctxPathlogic + "',document.getElementById('1srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:left;border:1px solid lightgray;height:19px;'><span id='space" + elementid + "'>-</span>";
        } else {//modified by anitha
            if (submenu != undefined && submenu != null && submenu != "" && submenu == "lastmeasure") {
                html += "<input class='inputbox' id='1srch4" + elementid + "' type='text' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "',document.getElementById('2srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:right;border:1px solid lightgray;height:19px;'>";
                html += "<input class='inputbox' id='2srch4" + elementid + "' type='text' onclick='event.stopPropagation();'  onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "',,'" + ctxPathlogic + "',document.getElementById('1srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:left;border:1px solid lightgray;height:19px;'><span id='space" + elementid + "'>-</span>";
            }
            else
            if (gtCrossTabLastButOneCol != undefined && gtCrossTabLastButOneCol != null && gtCrossTabLastButOneCol != "" && gtCrossTabLastButOneCol == "gtfirstmeasure") {
                html += "<input class='inputbox' id='1srch4" + elementid + "' type='text' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "',document.getElementById('2srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:right;border:1px solid lightgray;height:19px;'>";
                html += "<input class='inputbox' id='2srch4" + elementid + "' type='text' onclick='event.stopPropagation();'  onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "',,'" + ctxPathlogic + "',document.getElementById('1srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:left;border:1px solid lightgray;height:19px;'><span id='space" + elementid + "'>-</span>";
            } else {
                html += "<input class='inputbox' id='1srch4" + elementid + "' type='text' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "',document.getElementById('2srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:right;border:1px solid lightgray;height:19px;'>";
                html += "<input class='inputbox' id='2srch4" + elementid + "' type='text' onclick='event.stopPropagation();'  onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','" + elementidlogic + "',event,'" + PbReportIdlogic + "',,'" + ctxPathlogic + "',document.getElementById('1srch4" + elementid + "'))\" style='font-size:8pt;width:29%;float:left;border:1px solid lightgray;height:19px;'><span id='space" + elementid + "'>-</span>";
            }
        }
        $("#" + id).append(html);
    } else {
        $("#1srch4" + elementid).remove();
        $("#2srch4" + elementid).remove();
        $("#1srch2" + elementid).remove();
        if (isCrossTabReport == 'false') {
            html += "<input class='inputbox' id='1srch2" + elementid + "' type='text' style='font-size:8pt;width:62%;float:right;border:1px solid lightgray;height:19px;' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "')\"  >";
        } else {
            //modified by anitha
            if (submenu != undefined && submenu != null && submenu != "" && submenu == "lastmeasure") {
                html += "<input class='inputbox' id='1srch2" + elementid + "' type='text' style='font-size:8pt;width:62%;float:right;border:1px solid lightgray;height:19px;' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "')\"  >";
            } else if (gtCrossTabLastButOneCol != undefined && gtCrossTabLastButOneCol != null && gtCrossTabLastButOneCol != "" && gtCrossTabLastButOneCol == "gtfirstmeasure") {
                html += "<input class='inputbox' id='1srch2" + elementid + "' type='text' style='font-size:8pt;width:62%;float:right;border:1px solid lightgray;height:19px;' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','A_" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "')\"  >";
            }
            else {
                html += "<input class='inputbox' id='1srch2" + elementid + "' type='text' style='font-size:8pt;width:62%;float:right;border:1px solid lightgray;height:19px;' onclick='event.stopPropagation();' onKeyPress=\"parent.doSrchOperation(this,'" + logivalvalue + "','" + elementidlogic + "',event,'" + PbReportIdlogic + "','" + ctxPathlogic + "')\"  >";
            }
        }
        $("#" + id).append(html);

        $("#space" + elementid).remove();
    }
}
var elementname;
var ReportId;
var ReportId1111;
var PbReportId;
var ctxPath1;
var submenu;
var headeralign;
var dataalign;
var rounding;
var elementid1;
var sortValue;
var sortElemnt;
var rowviewlist;
var AOFlag;
var refreshRep;
var timeinfo;
var DisplayColumns;
var DisplayLabels;
function showfilterMeasure(elementnm, elementid, ulid, tdid, flagviewby, PbReportId, ctxPath, reset, submenu, isCrossTabReport, gtCrossTabLastButOneCol) {
    var html = "";
    submenu = submenu;
    var isCustMeasure;
    var measures;
    var messvalue1;
    var valueALT1;
    var valueALT = "";
    var mesereid;
    var mesereid1;
    var priorsymbol;

    $.ajax({
        type: 'POST',
        async: false,
        url: ctxPath + "/reportViewerAction.do?reportBy=getinformmeasure&reportid=" + PbReportId + "&measureid=" + elementid,
        //                     url: ctxPath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
        success: function(data) {
            var jsonVar = eval('(' + data + ')');
            var isCustMeasureids = jsonVar.isCustMeasure;
            var srchConditionsnew = jsonVar.srchConditions1;
            var srchColumnnew = jsonVar.srchColumn1;
            var srchValuenew = jsonVar.srchValue1;
            rowviewlist = jsonVar.rowviewlist;
            sortValue = jsonVar.sortValue;
            sortElemnt = jsonVar.sortElemnt;
            headeralign = jsonVar.headeralign;
            dataalign = jsonVar.dataalign;
            rounding = jsonVar.rounding;
            refreshRep=jsonVar.refreshRep;
            timeinfo = jsonVar.timeinfo;            
            DisplayColumns = jsonVar.DisplayColumns;
            DisplayLabels = jsonVar.DisplayLabels;
            messvalue1 = jsonVar.qryelements;
            measures = jsonVar.tableMeasures;
            isCustMeasure = isCustMeasureids[0];
            priorsymbol = jsonVar.priorsymbols;
            AOFlag = jsonVar.AoReport;
            if (srchConditionsnew != "[]" && srchConditionsnew != "") {
                valueALT1 = jsonVar.srchConditions1[0];
            }
            if (srchColumnnew != "[]" && srchColumnnew != "") {
                mesereid = jsonVar.srchColumn1[0];
                mesereid1 = mesereid.replace("A_", "");
            }
            if (srchValuenew != "[]" && srchValuenew != "") {
                valueALT = jsonVar.srchValue1[0];
            }


        }
    });

    var srchCondition = "";
    if (elementid === mesereid1) {
        if (valueALT1 == 'EQ')
            srchCondition = '=';
        else if (valueALT1 == 'GT')
            srchCondition = '>';
        else if (valueALT1 == 'LT')
            srchCondition = '<';
        else if (valueALT1 == 'GE')
            srchCondition = '>=';
        else if (valueALT1 == 'LE')
            srchCondition = '<=';
        else if (valueALT1 == 'BT')
            srchCondition = 'BT';
        else if (valueALT1 == 'TOP')
            srchCondition = 'TOP';
        else if (valueALT1 == 'BTM')
            srchCondition = 'BTM';
        else if (valueALT1 == 'LIKE')
            srchCondition = '*';
    } else {
        valueALT = "";
    }
    elementid1 = elementid;
    elementname = elementnm;
    elementname = elementname.trim()
    ReportId1111 = PbReportId;
    ReportId = PbReportId;
    ctxPath1 = ctxPath;
    var classname;
    if (submenu == 'right') {
        classname = 'dropdown-submenu pull-right1'
    } else {
        classname = 'dropdown-submenu pull-left1'
    }

    if (reset == 'true') {
        html += "<li><a onclick=parent.resetRuntimeColumn('A_" + elementid + "','N','" + PbReportId + "','" + ctxPath + "') >Reset</a></li>";
    }
    var sorting0;
    var sorting1;
    var sorting2
    if(refreshRep=="true")
    {
      sortValue="null"  
    }

    if (sortValue == "0" && sortElemnt == "A_" + elementid)
        sorting0 = "#D3D3D3";
    else if (sortValue == "1" && sortElemnt == "A_" + elementid)
        sorting1 = "#D3D3D3";
    html += "<li style='background-color:" + sorting0 + "'><a onclick=parent.sort('A_" + elementid + "','0','N','" + PbReportId + "','" + PbReportId + "') >Sort Ascend</a></li>";
    html += "<li style='background-color:" + sorting1 + "'><a onclick=parent.sort('A_" + elementid + "','1','N','" + PbReportId + "','" + PbReportId + "') >Sort Descend</a></li>";
    //added by anitha for subtotal sort and subtotal top/bottom
    if (rowviewlist.length >= 2) {
        html += "<li id='lisubTotalSort" + elementid + "' class='" + classname + "'><a onmouseover='opensubTotalSort(\"" + elementid + "\",\"" + submenu + "\",\"" + PbReportId + "\",\"" + ctxPath + "\")' >SubTotal Sort</a></li>";
        html += "<li id='liSubTotalTopBottom" + elementid + "' class='" + classname + "'><a onmouseover='openSubTotalTopBottom(\"" + elementid + "\",\"" + submenu + "\",\"" + PbReportId + "\",\"" + ctxPath + "\")' >SubTotal Top/Bottomt</a></li>";
        html += "<li><a onclick=parent.SubTotalSearch('A_" + elementid + "','','" + PbReportId + "','" + ctxPath + "') >SubTotal Filter</a></li>";
    }
    //end of code by anitha for subtotal sort and subtotal top/bottom
    html += "<li><a onclick=showlist(event,'logicalul" + elementid + "') >Logical Operations</a>";
    html += "<ul  id='logicalul" + elementid + "'  style='display:none; z-index: 4;' ><li id='logicalli" + elementid + "' ><select id='logicalArth" + elementid + "' style='float:left;width:34%;height:23px;' onclick='event.stopPropagation();' onchange=changesearch(event,'logicalli" + elementid + "','" + elementid + "','logicalArth" + elementid + "','" + isCrossTabReport + "','" + gtCrossTabLastButOneCol + "','" + submenu + "') selected value=" + srchCondition + " ><option>" + srchCondition + "</option>"
    html += "<option  value='>'>></option>"
    html += "<option  value='<'><</option>"
    html += "<option   value='='>=</option>"
    html += "<option  value='>='>>=</option>"
    html += "<option  value='<='><=</option>"
    html += "<option  value='BT'><></option>"
    html += "<option  value='TOP'>TOP</option>"
    html += "<option  value='BTM'>BTM</option>"
    html += "<option  value='Clear'>Clear</option>"
    html += "</select>";
    html += "<input class='inputbox' id='1srch2" + elementid + "' type='text' value='" + valueALT + "' onclick='event.stopPropagation();' onkeypress=setlogicalarth(event,this) style='font-size:8pt;width:62%;float:right;border:1px solid lightgray;height:19px;'>";
    html += "&nbsp</ul></li>";
    html += "<li id='liAnalytical" + elementid + "' class='" + classname + "'><a onmouseover=opensubmenuan(\"" + elementid + "\",\"" + isCrossTabReport + "\",\"" + submenu + "\") >Analytical</a></li>";
    html += "<li id='liFormatting" + elementid + "' class='" + classname + "'><a onmouseover='opensubForm(\"" + elementid + "\",\"" + submenu + "\")' >Formatting</a></li>";
    if (isCrossTabReport == 'false') {
        html += "<li id='liCustomF" + elementid + "' class='" + classname + "'><a onmouseover='openCustomF(\"" + elementid + "\",\"" + submenu + "\",\"" + isCustMeasure + "\")' >Custom Formulas</a></li>";
        html += "<li id='liStatistc" + elementid + "' class='" + classname + "'><a onmouseover='openStatistc(\"" + elementid + "\",\"" + submenu + "\",\"" + measures + "\",\"" + messvalue1 + "\")' >Statistical</a></li>";
    }
    //added by anitha
    if (AOFlag.length >= 1 && !(elementid.toString().indexOf("_MTD")!=-1 || elementid.toString().indexOf("_YTD")!=-1 || elementid.toString().indexOf("_QTD")!= -1
            || elementid.toString().indexOf("_PMTD")!=-1 || elementid.toString().indexOf("_PQTD")!=-1 || elementid.toString().indexOf("_PYTD")!=-1
            || elementid.toString().indexOf("_MOMPer")!=-1 || elementid.toString().indexOf("_QOQPer")!=-1 ||elementid.toString().indexOf("_YOYPer")!=-1 || elementid.toString().indexOf("_MOYMPer")!=-1 || elementid.toString().indexOf("_QOYQPer")!=-1
            || elementid.toString().indexOf("_PYMTD")!=-1 || elementid.toString().indexOf("_PYQTD")!=-1 
            || elementid.toString().indexOf("_MOM")!=-1 || elementid.toString().indexOf("_QOQ")!=-1 || elementid.toString().indexOf("_YOY")!=-1 || elementid.toString().indexOf("_MOYM")!=-1 || elementid.toString().indexOf("_QOYQ")!=-1)
            || elementid.toString().indexOf("_WTD")!=-1|| elementid.toString().indexOf("_PWTD")!=-1|| elementid.toString().indexOf("_PYWTD")!=-1|| elementid.toString().indexOf("_WOWPer")!=-1|| elementid.toString().indexOf("_WOYWPer")!=-1|| elementid.toString().indexOf("_WOW")!=-1|| elementid.toString().indexOf("_WOYW")!=-1) {
        html += "<li id='liDateJoin" + elementid + "' class='" + classname + "'><a onmouseover='openDateJoin(\"" + elementid + "\",\"" + submenu + "\",\""+PbReportId+"\")' >Time Aggregation</a></li>";
    }
    //ended by anitha
    //modified by anitha
    html += "<li id='liProperties" + elementid + "' ><a onclick=\"parent.columnProperties('A_" + elementid + "','" + elementname + "','" + ReportId + "')\"> Measure Properties</a></li>";
    html += "<li id='liAttributes" + elementid + "' ><a onclick=\"parent.modifycolmMeasure('A_" + elementid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\" >Measure Attributes</a></li>";    
    if (priorsymbol == 'true') {
        html += "<li id='prior" + elementid + "' class='" + classname + "'><a onmouseover='openpriorEnable(\"" + elementid + "\",\"" + submenu + "\",\"" + measures + "\",\"" + ReportId + "\")' >Arrow Colors</a></li>";
    }
//        html+="<li id='liAggregation"+elementid+"' class='"+classname+"'><a onmouseover='openCaggrigation(\""+elementid+"\")' >Custom Aggregation</a></li>";
//    html+="<li id='liComparsion"+elementid+"' class='"+classname+"'><a onmouseover='openComparsion(\""+elementid+"\")' >Custom Comparsion</a></li>";

//     if(submenu=='leftposition'){
//            var leftMargin =$("#"+ulid).offset().left+"px;";
//             leftMargin =$("#"+ulid).offset().left+"px;";
//            var top =$("#"+ulid).offset().top;
//             $("#"+ulid).css('margin-left', leftMargin);
//             $("#"+ulid).css('margin-top', top);
////            $("#"+ulid).attr("style","margin-left:"+leftMargin+"px;");
//            $("#"+ulid).attr("style","top:"+top+"px;");
//}

    if ($(".repTblMnu").is(":visible")) {
        $(".repTblMnu").slideUp("fast");
    }
    if ($("#" + ulid).is(":visible")) {
        $(".repTblMnu").slideUp("fast");
    }
    else {
        $("#" + ulid).html(html);
        $("#" + ulid).slideDown("slow");
    }
}

function showlist(event, id) {
    event.stopPropagation();
    var id1 = id.toString().substring(0, 9);
    var eid = id.split("logicalul")[1];
    if (id1 == 'logicalul') {
        var height = "40px";
        var idul = "logicalul";
        $("#" + id).toggle(200);
//        if(document.getElementById(idul)!=null && document.getElementById(idul).style.display=='block'){
//            height="20px"
//        }
        $("#logicaldiv" + eid).css('height', height);
        $("#Analytical" + eid).hide();
        $("#Formatting" + eid).hide();
        $("#CustomFormulas" + eid).hide();
        $("#statistical" + eid).hide();
        $("#Custom_Aggrigation" + eid).hide();
        $("#Custom_Comparsion" + eid).hide();
    }

    $('#span' + id).html('');

}
function showlist1(id)
{
    var id1 = id.toString().substring(0, 14);
// var  eid=id.split("Custom_Comparsion")[1];
    var eid = id.split("Totals_Display")[1];
    if (id1 == 'Totals_Display')
    {
        $("#" + id).toggle(200);
        $("#correlation" + eid).hide();
    }
    id1 = id.toString().substring(0, 9);
    eid = id.split("show_wise")[1];
    if (id1 == 'show_wise')
    {
        $("#" + id).toggle(200);
        $("#Top_Bottom" + eid).hide();
        $("#top_bottom_b" + eid).hide();
        $("#color_grouping" + eid).hide();
    }
    id1 = id.toString().substring(0, 10);
    eid = id.split("Top_Bottom")[1];
    if (id1 == 'Top_Bottom')
    {

        $("#" + id).toggle(200);
        $("#show_wise" + eid).hide();
        $("#top_bottom_b" + eid).hide();
        $("#color_grouping" + eid).hide();
    }
    id1 = id.toString().substring(0, 12)
    eid = id.split("top_bottom_b")[1];
    if (id1 == 'top_bottom_b')
    {
        $("#" + id).toggle(200);
        $("#show_wise" + eid).hide();
        $("#color_grouping" + eid).hide();
        $("#Top_Bottom" + eid).hide();
    }

    id1 = id.toString().substring(0, 14);
    eid = id.split("color_grouping")[1];
    if (id1 == 'color_grouping')
    {

        $("#" + id).toggle(200);
        $("#show_wise" + eid).hide();
        $("#top_bottom_b" + eid).hide();
        $("#Top_Bottom" + eid).hide();

    }
    id1 = id.toString().substring(0, 8);
    eid = id.split("rounding")[1];
    if (id1 == 'rounding')
    {

        $("#" + id).toggle(200);
        $("#Totals_Display" + eid).hide();

    }

    id1 = id.toString().substring(0, 11);
    eid = id.split("correlation")[1];
    if (id1 == 'correlation')
    {

        $("#" + id).toggle(200);


    }
    var arrowid = "d" + id;
    var html = "";
    var ctxPath = parent.document.getElementById("h").value;
    if (document.getElementById(arrowid) != null && document.getElementById(arrowid).style.display == '') {
        html += "<img  id='u" + id + "'  alt=''  border='0px' style='height:13px;float:right;'src='" + ctxPath + "/images/arrow_up.png'/>";
    } else {
        html += "<img  id=" + arrowid + "   alt=''  border='0px'   style='height:13px;float:right;'   src='" + ctxPath + "/images/arrow_down.png' />";
    }
    $('#span' + id).html('');
    $('#span' + id).html(html);
}
function getLOVStable(mainURL, elementid) {

    $.ajax({
        async: false,
        url: mainURL,
        success: function(data) {
            var html = "";
            var html1 = "";
            var datagbl = "";
            if (data != "") {
                datagbl = JSON.parse(data)["data"];
            }
            if (datagbl != "") {
                var jsondata = JSON.parse(data)["data"];
                var totalvalues1 = JSON.parse(data)["totalvalues"];

                var dependtype = JSON.parse(data)["dependtype"];
                var names = jsondata.split("\n");
                var names1 = totalvalues1.split("\n");
                totalvalues = names;
                if (firstscroll && !search) {
                    globalfiltvalues = names;
                    globalfiltwsplit = jsondata;
                }
//                 if (scrollcheck) {
//                    if (unceckall) {
//                        globalfiltwsplit = globalfiltwsplit + jsondata;
//                        globalfiltvalues = globalfiltwsplit.split("\n");
//                    }
//                }
                var filterMap1 = {};
                var filterValues1 = [];
                if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
                    filterMap1 = JSON.parse($("#filters1").val());
                    if (typeof filterMap1[query] !== "undefined") {
                        //            filterValues1=filterMap1[query];
                    }
                }
                var elementname1 = elementname;
                elementname1 = elementname1.replace("1q1", " ");
                elementname1 = elementname1.replace("1q1", " ");
                elementname1 = elementname1.replace("Tr", "");
                elementname1 = elementname1.replace("ad", "");

                var selectedlist
                var selnotinlist;  var selnotinlistDB = [];
                if (tabtype == "table") {
                    selectedlist = parent.filterMapNew[query];
                    selnotinlist = parent.filterMapNotin[query];
                } else {
                     if(typeof $("#notfilters").val()!=="undefined" && $("#notfilters").val()!==""){
        parent.filterMapNotinDB = JSON.parse($("#notfilters").val());
       
    }
                    filterValues1 = totalvalues;
                    selectedlist = parent.filterMapgraphs[query];
                    selnotinlistDB = parent.filterMapNotinDB[query];
                }

                //var selectedlist=parent.filterMapNew[query];
                if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
//                    showfilters(query,tabtype)
 if (search) {
     globalfiltwsplit="";
 }
if(scrollcheck || search){
                 //   if(unceckall){
                        globalfiltwsplit=globalfiltwsplit+jsondata;
                        globalfiltvalues = globalfiltwsplit.split("\n");
                   // }
                    }
                    if (firstscroll) {
                        for (var k = 0; k < selectedlist.length; k++) {
                            // var value=selectedlist[k]+"_"+k+"_"+query
                            var value;
                            var id;
                             if (selectedlist[k] != "") {
                            if (tabtype == "table") {
                                value = selectedlist[k].replace("'", " ").replace("_", "2q2") + "_" + k + "_" + query
                                id = elementname + "_" + k + "_" + query
                                value = value + "_selecttrue";
                            } else {
                                //                  if(scrollFlag){
                                var index = filterValues1.indexOf(selectedlist[k]);
                                value = selectedlist[k].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
                                id = elementname + "_" + index + "_" + query
                                value = value + "_selecttrue";
                                //                  }
                            }

                            if (selectedlist[k] != "All" || selectedlist[k] != "ALL" || selectedlist[k] != "") {
                                html = html + "<option   value='" + value + "'>" + selectedlist[k] + "</option>";
                            }
                        }
                        }
                        unceckall = true
                    } else {
                        if (dependtype == "undependent") {
                            if (tabtype == "table") {
                            } else {
//                                var value;
//                                var id;
//                                for (var k = 0; k < selectedlist.length; k++) {
//                                    var index = filterValues1.indexOf(selectedlist[k]);
//                                    value = selectedlist[k].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
//                                    id = elementname + "_" + index + "_" + query
//                                    value = value + "_selecttrue";
//                                    if (selectedlist[k] != "All" || selectedlist[k] != "ALL" || selectedlist[k] != "") {
//                                        html = html + "<option  name='" + id + "' value='" + value + "'>" + selectedlist[k] + "</option>";
//                                    }
//                                }
                                    }
                                }
                        
                        if (scrollcheck) {
                            for (var j = 0; j < globalfiltvalues.length - 1; j++)
                            {
                                var id;
                                var value;

                                var flag = "true";
                                for (var g = 0; g < selectedlist.length; g++) {
                                   if (selectedlist[g] != "All" || selectedlist[g] != "ALL" || selectedlist[g] != "") {

                                    if (globalfiltvalues[j] == selectedlist[g]) {
                                        if (tabtype == "table") {
                                            value = selectedlist[g].replace("'", " ").replace("_", "2q2") + "_" + j + "_" + query
                                        } else {
                                            var index = filterValues1.indexOf(selectedlist[k]);
                                            value = selectedlist[g].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
                                        }
                                        value = value + "_selecttrue";
                                        if (globalfiltvalues[j] != "") {
                                            html = html + "<option  value='" + value + "'>" + globalfiltvalues[j] + "</option>";
                                        }
                                        flag = "false";
                                    }
                                }
                                }
                                if (flag == "true") {
                                    //    var value=names[j]+"_"+j+"_"+query
                                    var value;
                                    var id;
                                    if (tabtype == "table") {
                                        value = globalfiltvalues[j].replace("'", " ").replace("_", "2q2") + "_" + j + "_" + query
                                    } else {
                                        var index = filterValues1.indexOf(globalfiltvalues[j]);
                                        value = globalfiltvalues[j].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
                                    }

                                    id = elementname + "_" + index + "_" + query
                                    if (globalfiltvalues[j] != "") {
                                        html = html + "<option  value='" + value + "'>" + globalfiltvalues[j] + "</option>";
                                    }
                                    flag = "false";
                                }


                            }
                           
                            selectid = query + "__" + elementname
                            if (tabtype == "table") {

                                $("#" + selectid).html("");
                            } else {
                                $("#" + selectid).html("");
                            }
                        }
                    }
                    if(!scrollcheck){
                    for (var j = 0; j < names.length - 1; j++)
                    {
                        var flag = "true";
                        for (var g = 0; g < selectedlist.length; g++) {
                                    if (selectedlist[g] != "All" || selectedlist[g] != "ALL" || selectedlist[g] != "") {

                            if (names[j] == selectedlist[g]) {
                                value = value + "_selecttrue";

                                //<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
                                flag = "false";

                            }
                        }
                        }
                        if (flag == "true") {
                            //    var value=names[j]+"_"+j+"_"+query
                            var value;
                            var id;
                            if (tabtype == "table") {
                                value = names[j].replace("'", " ").replace("_", "2q2") + "_" + j + "_" + query
                                id = elementname + "_" + j + "_" + query
                            } else {
                                //                  if(scrollFlag){
                                var index = filterValues1.indexOf(names[j]);
                                value = names[j].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
                                id = elementname + "_" + index + "_" + query
                                //                  }
                            }
                            if (unceckall) {

                            } else {
                                if (!firstscroll) {
                                    value = value + "_selecttrue";
                                }
                            }
                            if (names[j] != "") {
                                //             value=value+"_selecttrue";
                                html = html + "<option  value='" + value + "'>" + names[j] + "</option>";
                                flag = "false";
                            }
                        }

                    }
                }
                 scrollcheck = false
                } else {
                    if (scrollcheck && unceckall) {
                        for (var j = 0; j < globalfiltvalues.length - 1; j++)
                        {
                            var id;
                            var value;

                            var flag = "true";
                            if (flag == "true") {
                                //    var value=names[j]+"_"+j+"_"+query
                                var value;
                                var id;
                                if (tabtype == "table") {
                                    value = globalfiltvalues[j].replace("'", " ").replace("_", "2q2") + "_" + j + "_" + query
                                } else {
                                    var index = filterValues1.indexOf(globalfiltvalues[j]);
                                    value = globalfiltvalues[j].replace("'", " ").replace("_", "2q2") + "_" + index + "_" + query
                                }

                                id = elementname + "_" + index + "_" + query
                                if (globalfiltvalues[j] != "") {
                                    html = html + "<option  value='" + value + "'>" + globalfiltvalues[j] + "</option>";
                                }
                                flag = "false";
                            }


                        }
                        selectid = query + "__" + elementname
                        if (tabtype == "table") {

                            $("#" + selectid).html("");
                        } else {
                            $("#" + selectid).html("");
                        }
                    }
                    if (search) {
     globalfiltwsplit="";
 }
                     if(scrollcheck || search){
                    //if(unceckall){
                        globalfiltwsplit=globalfiltwsplit+jsondata;
                        globalfiltvalues = globalfiltwsplit.split("\n");
                    //}
                    //names=globalfiltvalues;
                     selectid=query+"__"+elementname
 if(tabtype=="table"){
     
        $("#"+selectid).html("");
   }else{
                        $("#"+selectid).html("");
   }
                }
                    for(var j=0;j<globalfiltvalues.length-1;j++)
                    {
                        var id;
                        var value;
                        if(tabtype=="table"){
                            value=globalfiltvalues[j].replace("'", " ").replace("_", "2q2")+"_"+j+"_"+query
                            id= elementname+"_"+j+"_"+query

                        }else{
                             value=globalfiltvalues[j].replace("'", " ").replace("_", "2q2")+"_"+j+"_"+query
                            if(scrollFlag){
                                try{
                                var index= filterValues1.indexOf(globalfiltvalues[j]);
                                value=globalfiltvalues[j].replace("'", " ").replace("_", "2q2")+"_"+index+"_"+query
                                id=   id=elementname+"_"+index+"_"+query
                                if(selectedlist!=="" && selectedlist!==undefined && selectedlist.length>=1 && selectedlist[0]!="All"){

                                }else{

                                    }
                                } catch (e) {
                                }
                            }
                        }
                        if (unceckall) {
                            if (search && unceckall && tabtype == "table") {
                                value = value;
                            } else {
                                if (!scrollcheck && tabtype != "table") {
                                    if (search && unceckall) {
                                        value = value;
                                    } else {
                                        value = value + "_selecttrue";
                                    }
                                }
                            }
                        } else {
                            if (tabtype == "table") {
                                var notinflag=false;
                                if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {
                                    for (var k = 0; k < selnotinlist.length; k++) {
                                        if (globalfiltvalues[j] == selnotinlist[k]) {
                                            value = value;notinflag=true;
                                            break;
                                        }
                                    }
                                } 
                                if(!notinflag){
                                 
                                    value = value + "_selecttrue";
                                }
                            } else {
                                 var notinflag=false;
                                if (selnotinlistDB !== "" && selnotinlistDB !== undefined && selnotinlistDB.length >= 1 ) {
                                    for (var k = 0; k < selnotinlistDB.length; k++) {
                                        if (globalfiltvalues[j] == selnotinlistDB[k]) {
                                            value = value;notinflag=true;
                                            break;
                                        } 
                                    }
                                } 
                                  if(!notinflag){
                                 
                                value = value + "_selecttrue";
                            }
                            }

                        }
                        if (globalfiltvalues[j] != "") {
                            html = html + "<option  value='" + value + "'>" + globalfiltvalues[j] + "</option>";


                        }
                    }
                }
                scrollcheck = false;
                if (html != "") {

                    if (tabtype == "table") {
                        if (search) {
                            $("#" + selectid).html("");
                        }
                        $("#" + selectid).append(html);
                        $("#" + selectid).multiselecttable('refresh');
                    } else {
                        if (search) {

                            parent.$("#multigblrefresh").val("search")
                        } else {
                            parent.$("#multigblrefresh").val("true")
                        }
                        if (search) {
                            $("#" + selectid).html("");
                            parent.$("#multigblrefresh").val("search")
                        }
                        if (scrollFlag) {
                            if (dependtype == "dependent") {
                                $("#" + selectid).append(html);
                            } else {
                                $("#" + selectid).html(html);
                            }
                        } else {
                            $("#" + selectid).append(html);
                        }
                        if (tabtype == "trend") {
                            $("#" + selectid).multiselectTrend('refresh');
                        }
                        else if (tabtype == "advance") {
                            $("#" + selectid).multiselect1('refresh');
                        } else {

                            $("#" + selectid).multiselect('refresh');
                        }
                        if (search) {

                            parent.$("#multigblrefresh").val("search")
                        } else {
                            parent.$("#multigblrefresh").val("true")
                        }
                    }

                } else {
                    if (tabtype == "table") {
                    } else {
                        parent.$("#multigblrefresh").val("false")
                    }
                }

            }
        }
    });

}
  //added by sruthi for advance filters
function applyadvancefilters(reportid,elementid,elementname,reportstatus){//alert(reportstatus)
     parent.$("#advanceFilters").dialog('close');
    var selectedmeasure=parent.document.getElementById("selectedmeasure").value;
    var selectedmeasurename= $("#selectedmeasure option:selected").text();
    var selectedlogical=parent.document.getElementById("logicaloperation").value;
    var selectedval =parent.document.getElementById("selectedval").value;
    var systemdate;
    var textid;
    var selecteddate;
    var fromdate;
    var todate;
    var compdate;
    var forcompdate;
    if(reportstatus==='standard'){//alert("bdjhcnd")
   systemdate=parent.document.getElementById("systemdate").value;
   textid=parent.document.getElementById("textid").value;
    if(textid!=''){
    $("#reportdate").prop('checked', false);
     $("#Yesterday").prop('checked', false);
     $("#Today").prop('checked', false);
      $("#Tomorrow").prop('checked', false);
      $("#LMED").prop('checked', false);
       $("#LQED").prop('checked', false);
        $("#LYED").prop('checked', false);
         }
    if($("#reportdate").is(":checked")){
        selecteddate=parent.$("#reportdate").val()
    }else if($("#Yesterday").is(":checked")){
        selecteddate=parent.$("#Yesterday").val()
    }else if($("#Today").is(":checked")){
        selecteddate=parent.$("#Today").val()
    }else if($("#Tomorrow").is(":checked")){
        selecteddate=parent.$("#Tomorrow").val()
    }else if($("#LMED").is(":checked")){
        selecteddate=parent.$("#LMED").val()
    }else if($("#LQED").is(":checked")){
        selecteddate=parent.$("#LQED").val()
    }else if($("#LYED").is(":checked")){
         selecteddate=parent.$("#LYED").val()
    }}
else if(reportstatus==='range'){//alert("bhdbc")
     if($("#reportdate").is(":checked")){
        selecteddate=parent.$("#reportdate").val()
     }else{
         if($("#fromToday").is(":checked")){
             fromdate=parent.$("#fromToday").val()
         }else if($("#fromTomorrow").is(":checked")){
             fromdate=parent.$("#fromTomorrow").val()
         }
         if($("#forToday").is(":checked")){
             todate=parent.$("#forToday").val()
         }else if($("#forTomorrow").is(":checked")){
             todate=parent.$("#forTomorrow").val()
         }
         if($("#cfromToday").is(":checked")){
             compdate=parent.$("#cfromToday").val()
         }else if($("#cfromTomorrow").is(":checked")){
             compdate=parent.$("#cfromTomorrow").val()
         }
         if($("#ctoToday").is(":checked")){
             forcompdate=parent.$("#ctoToday").val()
         }else if($("#ctoTomorrow").is(":checked")){
             forcompdate=parent.$("#ctoTomorrow").val()
         }
             
     }
        
}
    var html=""
    $("#loading").show();
      $.ajax({
                    async:false,
                      url:"reportViewerAction.do?reportBy=getAdvanceLogicalFilters&elementid="+elementid+"&reportid="+reportid+"&selectedmeasure="+selectedmeasure+"&selectedlogical="+selectedlogical+"&selectedval="+selectedval+"&selectedmeasurename="+selectedmeasurename+"&elementname="+elementname+"&selecteddate="+selecteddate+"&systemdate="+systemdate+"&textid="+textid+"&fromdate="+fromdate+"&todate="+todate+"&compdate="+compdate+"&forcompdate="+forcompdate,
                       success:function(data){//alert("data....."+data)
                        if(data!=''){
                            var topfilters=[];
                           var topfilters1=data;
                           topfilters1=topfilters1.split(",")
                         //  alert(topfilters1)
                           for(var i=0; i<topfilters1.length;i++){
                               topfilters.push(topfilters1[i].replace("[","").replace("]","").toString().trim());
                                var  value=topfilters1[i].replace("[","").replace("]","").toString().replace("'", " ").replace("_", "2q2").trim()+"_"+i+"_"+query
                               value = value + "_selecttrue";
                               if (topfilters1[i] != "") {
                            html = html + "<option  value='" + value + "'>" + topfilters1[i].replace("[","").replace("]","").toString().trim() + "</option>";
                               }
                           }
                           // alert("elementid"+elementid)
                           parent.$("#CBOARP" + elementid).val(JSON.stringify(topfilters));
                        //   alert("TopFilters......."+$("#CBOARP" + elementid).val());
                             //alert("dbfjhdbv....."+ JSON.stringify(parent.filterMapNew));
                              parent.filterMapNew[elementid]=topfilters
                                parent.filterMapgraphs[elementid]=topfilters
                             // alert( parent.filterMapNew[elementid])
                              //alert("dbfjhdbv12344....."+ JSON.stringify(parent.filterMapNew));
                              alert("Filters are intialized")
                        }
                       }
                        });
                          $("#loading").hide();
                             $("#" + selectid).html("");
                        parent.$("#multigblrefresh").val("intilialized")
                        $("#" + selectid).append(html);
                         $("#" + selectid).multiselecttable('refresh');
}

function opencustomdates(){
     $("#customtable").toggle(500);
    
}
  //added by sruthi for advance filters
function AdvanceFilters(elementid,elementname){//alert(elementid)
  //  alert(elementname)
 //  parent.$("#headerTable").mouseover(function(){//alert("njnn")
                        var container =parent.$(".ui-multiselect-menu");
                                container.hide(500);
                  //      });

   parent.$("#advanceFilters").dialog({
        autoOpen: false,
        height: 400,
        width: 550,
        position: 'justify',
        modal: true,
        resizable:false
    });
    var reportid =  parent.document.getElementById("REPORTID").value;
       var frameObj=parent.document.getElementById("advanceiframeFilters");
        $.ajax({
                    async:false,
                      url:"reportViewerAction.do?reportBy=getAdvanceFilters&elementid="+elementid+"&reportid="+reportid+"&elementname="+elementname,
                       success:function(data){
                         $("#advanceFilters").html('');
                         $("#advanceFilters").html(data);
                          parent.$("#advanceFilters").dialog('open');
                       }
                        });
}

function setallfilters(elname, elmentid, flag, type) {
    var filterValues = [];
    var filterValues1 = [];
    var filterMap = {};
    elname = elname.replace("1q1", " ");
    if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
        filterMap = JSON.parse($("#filters1").val());
        //        elname=elname.replace("1q1", " ");
        //if(typeof filterMap[elmentid]!=="undefined"){
        //            filterValues=filterMap[elmentid];
        //        }
    }
    elname = elname.replace("1q1", " ");
    var filters = totalvalues;
    filterValues1 = totalvalues;
    //      filterValues.push(filterData[elname]);
    for (var filter in filters) {
        var index = filterValues1.indexOf(filter);
        if (flag != null && flag == 'uncheckall') {
            //            var chekid;
            //            if(type=="advance"){
            //
            //            }else if(type=="trend"){
            //
            //            }else if(type=="table"){
            //
            //            }else{
            //                chekid="ui-multiselect-"+elname+"-option-"+index
            //            }
            //              if(!document.getElementById(chekid).checked){
            if (totalvalues[filter] != "") {
                //filterValues.push(totalvalues[filter].replace("'", "''"));
            }
            //              }
            //            filterMap[elmentid] = filterValues;
            checkall = false
            unceckall = true
        }
        else {
            var index = filterValues.indexOf(totalvalues[filter]);
            filterValues.splice(index, 1);
            checkall = true

        }

    }
    delete filterMap[elmentid];
    delete parent.filterMapNotinDB[elmentid];
    var filterValuesgr = [];
    var filterValuestb = [];
    var filterVal = [];

    parent.filterMapgraphs[elmentid] = filterValuesgr;
    filterMap[elmentid] = filterValues;
    parent.filterMapNotin[elmentid] = filterValuesgr;
    parent.filterMapNew[elmentid] = filterValuestb;
    parent.filterMapNewtb[elmentid] = filterValuestb;
    parent.filterMapNotinDB[elmentid] = filterValuesgr;
    //     parent.filterMapNewtb=parent.filterMapNew;
    $("#filters1").val(JSON.stringify(filterMap));
    $("#notfilters").val(JSON.stringify(parent.filterMapNotinDB));
    filterVal.push("All")
    $("#CBOARP" + elmentid).val(JSON.stringify(filterVal));
}
function GetXmlHttpObject()
{
    var xmlHttp = null;
    try
    {
        // Firefox, Opera 8.0+, Safari
        xmlHttp = new XMLHttpRequest();
    }
    catch (e)
    {
        // Internet Explorer
        try
        {
            xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    return xmlHttp;
}


function getlovfiltersreport(id, elementid, checkboxContainer1) {
    elementname = id;
    breakpoint = false;
    checkboxContainer = checkboxContainer1
    var reportId = parent.$("#graphsId").val()
    //       parent.getlovparams(id,elementid)
    query = elementid
    var country = "";
    var newArr = new Array();
    newArr.push(elementid);
    allParamIds = newArr.toString();
    parent.startValgbl = parent.startValgbl;

    scrollFlag = true;


}
function setlovfilters() {


}
var breakpoint = false;
function onScrollDivreport(divObj) {
    scHeight = (divObj.scrollHeight);
    scTop = (divObj.scrollTop);
    clHgt = (divObj.clientHeight);
    if (scrollFlag && !breakpoint) {
        if (scTop == (scHeight - clHgt)) {
            parent.startValgbl = parent.startValgbl + 100;
            breakpoint = true;
            if (parent.startValgbl == "100100") {
                breakpoint = false;
                parent.startValgbl = 100;
                $.ajax({
                    async: false,
                    type: "POST",
                    data:
                            $('#graphForm').serialize(),
                    url: 'reportViewer.do?reportBy=getFilters&typegbl=scroll',
                    success: function(data) {



                    }
                });
            }

            if (breakpoint) {
                $.ajax({
                    async: false,
                    type: "POST",
                    data:
                            $('#graphForm').serialize(),
                    url: 'reportViewer.do?reportBy=getFilters&typegbl=scroll&fromajaxtype=true&startValue=' + parent.startValgbl + '&elementid=' + query,
                    success: function(data) {
                        if (data != "") {
                            var html = "";
                            var html1 = "";

                            var names = data.split("\n");

                            var filterMap1 = {};
                            var filterValues = [];

                            var elementname1 = elementname;
                            elementname1 = elementname1.replace("1q1", " ");
                            elementname1 = elementname1.replace("1q1", " ");
                            elementname1 = elementname1.replace("Tr", "");
                            elementname1 = elementname1.replace("ad", "");
                            filterValues = totalvalues;
                            for (var j = 0; j < names.length - 1; j++)
                            {
                                var flag = "false";
                                var index = filterValues.indexOf(names[j]);
                                var value = names[j] + "_" + index + "_" + query
                                //             if(filterValues!=null && filterValues!=""){
                                // for(var i=0;i<filterValues.length;i++){
                                //if(names[j]==filterValues[i]){
                                //
                                // value=value+"_selecttrue";
                                //     html = html + "<option  value='"+value+"'>"+names[j]+"</option>";
                                //    flag="true";
                                //
                                //}
                                //
                                // }
                                // if(flag=="false"){
                                //
                                //
                                //html = html + "<option  value='"+value+"'>"+names[j]+"</option>";
                                //                       flag="false";
                                //}
                                //             }else{alert(names[j])
                                //if(parent.checkall){
                                value = value + "_selecttrue";
                                //        }
                                html = html + "<option  value='" + value + "'>" + names[j] + "</option>";
                                //             }


                            }
                            if (html != "") {

                                $("#" + elementname).append(html);
                                parent.$("#multigblrefresh").val("true")
                                breakpoint = false;
                                scrollFlag = true;
                            } else {
                                parent.$("#multigblrefresh").val("false")
                                //            startVal="";
                            }
                        } else {
                            parent.$("#multigblrefresh").val("false")
                        }
                    }
                });
            }
            onScrollFlag = 1;

        } else {

        }
    }
}
function submiturlsseq(url) {

    //                    edited by manik
    parent.document.frmParameter.action = url + '&isdrilltype=true&fromOneview=true&gblsequnce=gblsequnce&DEFAULT_TAB=Graph';
    //    parent.document.frmParameter.target = "_blank";
    parent.document.frmParameter.submit();
//    parent.document.frmParameter.target = "";
//document.frmParameter.submit();
}
function submitformGraph(url) {
    var fromVisual = "true";

    parent.document.frmParameter.action = url + '&fromVisual=' + fromVisual + '&REPORTID=' + $("#graphsId").val();

    parent.document.frmParameter.submit();

}
function updateorderfilters(repId) {


    submiturlsseq('reportViewer.do?reportBy=viewReport&action=reset&REPORTID=' + repId)
//     gotoupdatefilters();
//
//
//
//
//     $("#"+elementname1).multiselect({
//   selectedText: "# of # selected",
//   noneSelectedText: elementname,
//   selectedList: 2
//}).multiselectfilter();
}
function shoenewuifilters(elname, elid, ulid) {
    var filterValues = [];
    var html = "";
    filterValues = newuifilter[elid];
    for (var i1 = 0; i1 < filterValues.length; i1++) {
        html += "<li><span style='color:gray;'><input type='checkbox' onclick=chekfilters1(" + elname + "," + elid + ") >" + filterValues[i1] + "</span></li>";
    }
    $("#" + ulid).toggle(200);
    $("#" + ulid).html(html);
}
function runtimeglobalfilters(filterMap, flagtype, type) {
    var filterMap1 = {};
    //filterMap1=filterMap
    var filterValues = [];
    //  $("#filterMap").val(JSON.stringify(filterMap));
    //   filterMap1 = JSON.parse($("#filterMap").val());alert(filterMap1)
    var filterValues1 = [];
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var gbl = 0;
    var size11 = viewOvName.length;

//    if (size11 > 6) {
//        size11 = 6
//    }

    for (var key = 0; key < size11; key++) {
        var elementname = viewOvName[key].replace("1q1", " ");
        elementname = elementname.replace("1q1", " ");
        if (type == "trend") {
            elementname = elementname.replace("Tr", "");
        } else if (type == "advance") {
            elementname = elementname.replace("ad", "");
        }
        var viewbyid = viewOvIds[key].replace("A_", "");
//        var filters=filterData[elementname];
//        if(typeof filters !=="undefined" && filters!=""){
//        filterValues=filterData[elementname];
        filterValues1 = [];
        if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
            filterMap1 = JSON.parse($("#filters1").val());
            if (typeof filterMap1[viewbyid] !== "undefined") {
                filterValues1 = filterMap1[viewbyid];
            }
        }
        var filterMapnotin = {};
    var filterValuesnotin=[];
    if(typeof $("#notfilters").val()!=="undefined" && $("#notfilters").val()!==""){
        filterMapnotin = JSON.parse($("#notfilters").val());
        if(typeof filterMapnotin[viewbyid]!=="undefined"){
            filterValuesnotin=filterMapnotin[viewbyid];
        }
    }
     filterValuesnotin=parent.filterMapNotinDB[viewbyid] ;

        // alert(filterValues1)
        var selectedFilters1 = [];
        if (flagtype == "reset") {
            filterMap1[viewbyid] = selectedFilters1;
            $("#filters1").val(JSON.stringify(filterMap1));
        } else {
            selectedFilters1 = filterValues1
        }
        //   alert(filterValues1)
        //     parent.filterMapgraphs[viewbyid]=selectedFilters1;
        //}

        var selectedlist = parent.filterMapNewtb[viewbyid];
        if (selectedlist == undefined || selectedlist == "") {

        } else {

            filterValues1 = selectedlist

        }
        var html = "";
        for (var g = 0; g < filterValues1.length; g++) {
            var value = filterValues1[g].replace("'", " ") + "_" + g + "_" + viewOvIds[key];

            value = value + "_selecttrue";

            html = html + "<option  value='" + value + "'>" + filterValues1[g].replace("]", "").replace("[", "") + "</option>";

            //<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
            flag = "false";


        }


//        for(var i=0; i<size;i++){
//            var flag="true";
//            var value=filters[i].replace("'", " ")+"_"+i+"_"+viewOvIds[key];
//            if(filterValues1!=null && filterValues1!=""){
//                var valuefilter=filters[i].replace("]", "").replace("[", "");
//                for(var g=0;g<filterValues1.length;g++){
//
//                    if(valuefilter==filterValues1[g]){
//                        value=value+"_selecttrue";
//                        if(filterValues1.length==size){
//                            html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";
//                        }
//                        //<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
//                        flag="false";
//
//                    }
//                }
//                if(flag=="true"){
//                    value=value+"_selecttrue";
//                    selectedFilters1.push(filters[i]);
//                    html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";
//
//                    flag="false";
//                }
//            }else{
//            //        value=value+"_selecttrue";
//            //      html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";
//            // <option value="<%=value%>"  ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
//            }
//        }
        if (filterValues1 != null && filterValues1 != "") {

            for (var g = 0; g < filterValues1.length; g++) {

                var index = filterValues.indexOf(filterValues1[g]);
                var value = filterValues1[g] + "_" + index + "_" + viewOvIds[key];

                //    value=value+"_selecttrue";

                // html = html + "<option  value='"+value+"'>"+filterValues1[g]+"</option>";

            }
        }
        if (search) {
            var country = ""

            resetAjax(country)

        } else {
            parent.$("#multigblrefresh").val("filter")
        }
                 if (filterValuesnotin !== "" && filterValuesnotin !== undefined && filterValuesnotin.length >= 1 && filterValuesnotin[0] != "All") {

        for (var g = 0; g < filterValuesnotin.length; g++) {
            var value = filterValuesnotin[g].replace("'", " ") + "_" + g + "_" + viewOvIds[key];

            value = value + "_selecttrue";

            html = html + "<option  value='" + value + "'>" + filterValuesnotin[g].replace("]", "").replace("[", "") + "</option>";

            //<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
         


        }
         parent.$("#multigblrefresh").val("notin")
    }
        elementname = viewOvName[key];
        if (type == "trend") {
            var elementname1 = elementname.replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1");
            elementname1 = elementname.replace(" ", "1q1");
            elementname1 = "Tr" + elementname1;
            var selectid = viewbyid + "__" + elementname1
            $("#" + selectid).html(html);
            $("#" + selectid).multiselectTrend('refresh');
        }
        if (type == "advance") {
            var elementname1 = elementname.replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1");
            elementname1 = "ad" + elementname1;
            var selectid = viewbyid + "__" + elementname1
            $("#" + selectid).html(html);
            $("#" + selectid).multiselect1('refresh');
        } else {
            elementname = elementname.replace(" ", "1q1").replace(" ", "1q1").replace(" ", "1q1");
            var selectid = viewbyid + "__" + elementname
            $("#" + selectid).html(html);
            $("#" + selectid).multiselect('refresh');
        }
        gbl++;
        //if(gbl==5){
        //    break;
        //    }
        //    displayFiltTrend(elementname1);
        //     parent.$("#"+elementname1).multiselectTrend({
        //                                             selectedText: "# of # selected",
        //                                             noneSelectedText: elementname,
        //                                             selectedList: 2
        //                                               }).multiselectfilterTrend();
        //      $("#"+elementname1).multiselectTrend({
        //   selectedText: "# of # selected",
        //   noneSelectedText: elementname,
        //   selectedList: 2
        //}).multiselectfilterTrend();
        parent.filterMapgraphs[viewbyid] = selectedFilters1;
        parent.$("#multigblrefresh").val("")
        // }
    }
}
// var filterMapNew={};
function submiturlsseq(url) {

    //                    edited by manik
    parent.document.frmParameter.action = url + '&isdrilltype=true&fromOneview=true&gblsequnce=gblsequnce&DEFAULT_TAB=Graph';
    //    parent.document.frmParameter.target = "_blank";
    parent.document.frmParameter.submit();
//    parent.document.frmParameter.target = "";
//document.frmParameter.submit();
}
function updateorderfilters(repId) {


    submiturlsseq('reportViewer.do?reportBy=viewReport&action=reset&REPORTID=' + repId)
//     gotoupdatefilters();
//
//
//
//
//     $("#"+elementname1).multiselect({
//   selectedText: "# of # selected",
//   noneSelectedText: elementname,
//   selectedList: 2
//}).multiselectfilter();
}
function unchektableall(id, flag) {
    var selectedFilters = [];
    var selectedFilters1 = [];  var selectedFiltersnot = [];
    parent.filterMapNew[id] = selectedFilters1;
      parent.filterMapNotin[id] = selectedFiltersnot;
    selectedFilters.push("ALL")
    if (flag == "checkall") {
        //         parent.filterMapNew[id].push(selectedFilters)
        $("#CBOARP" + id).val(JSON.stringify(selectedFilters));
        unceckall = false
        //     alert($("#CBOARP"+id).val())
    } else {
        //  parent.filterMapNew[id].push(selectedFilters)
        $("#CBOARP" + id).val(JSON.stringify(selectedFilters));
        unceckall = true
        //     alert($("#CBOARP"+id).val())

    }
    var dimId = "new_CBOARP" + id;
    $("#" + dimId).val(JSON.stringify(selectedFilters1));
    var likeVal = $("#LIKE_" + id).val();
    var notLikeVal = $("#NOTLIKE_" + id).val();
    var notInVal = $("#" + id).val();
    var ctxPath = document.getElementById("h").value;

    $.post(
            ctxPath + '/reportViewer.do?reportBy=setOperatorFilters&globalfilter=true&REPORTID=' + $("#graphsId").val() + "&dimId=" + id + "&gblNOTIN=" + encodeURIComponent(selectedFilters1) + "&NOTIN=" + encodeURIComponent(notInVal) + "&LIKE=" + encodeURIComponent(likeVal) + "&NOTLIKE=" + encodeURIComponent(notLikeVal),
            function(data) {
                $("#DimFilterBy").dialog('close');
            }
    );

}
function applyGlobalFilterinreport(id, nameArr, chekid) {
    var filterMap1 = {};
    var filterValues = [];
    var filtersall = [];
    var filtersall1 = [];

    var name, name1;
    var filterData = {};
    name = nameArr.split("*,")[0];
    name = name.replace("1q1", " ");
   
    name1 = nameArr.split("*,")[1];
    $('.selectallg' + query).prop('checked', false);
   $('.unselectallg' + query).prop('checked', false);
    var view = parent.$("#globalviewby").val();
    var viewIds = parent.$("#globalviewbyIds").val();

    name = name.replace("1q1", " "); filterData[name] = totalvalues;
 var selectedlist
   var selnotinlist

  var filterMapnotin = {};
    var filterValuesnotin=[];
    if(typeof $("#notfilters").val()!=="undefined" && $("#notfilters").val()!==""){
        filterMapnotin = JSON.parse($("#notfilters").val());
        if(typeof filterMapnotin[name1]!=="undefined"){
            filterValuesnotin=filterMapnotin[name1];
        }
    }
     selectedlist = parent.filterMapgraphs[query];
    selnotinlist = parent.filterMapNotinDB[query];
 var notInVal = [];
       var filterVal = [];var dimid="new_CBOARP"+name1;
        var ctxPath=document.getElementById("h").value;
        var reportId=parent.$("#REPORTID").val();
        $("#DimFilterBy").dialog({
        autoOpen: false,
        height: 250,
        width: 450,
        position: 'justify',
        modal: true,
        title:name
    });
       
if (typeof $("#new_CBOARP"+name1).val() == "undefined" || $("#new_CBOARP"+name1).val() == "" || $("#new_CBOARP"+name1).val() === "[]") { 
         
 $.ajax({
        type: 'POST',
        async: false,
        
        url:  ctxPath+"/reportViewer.do?reportBy=getOperatorFilters&REPORTID="+reportId+"&dimId="+name1,
        success: function(data) {
           $("#DimFilterBy").html(data);
        }
    })
}
   
    if (typeof $("#filters1").val() !== "undefined" && $("#filters1").val() !== "") {
        filterMap1 = JSON.parse($("#filters1").val());
        if (typeof filterMap1[name1] !== "undefined") {
            filterValues = filterMap1[name1];
        }
    }
//      if(filterValues==undefined || filterValues==""){
//          filterValues=totalvalues
//      }
    var filterValuestb = [];
    filterValuestb = parent.filterMapNew[name1];
    if (document.getElementById(chekid).checked) {
         if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {
              for (var j = 0; j < selnotinlist.length; j++)
            {
                var slectnotvalue=$("#" + chekid).val().split("_")[0].replace('2q2', '_');
                if(slectnotvalue==selnotinlist[j]){
               var index = filterValuesnotin.indexOf(slectnotvalue);
        filterValuesnotin.splice(index, 1);
       parent.filterMapNotinDB[name1] = filterValuesnotin;
       parent.filterMapNotin[name1] = filterValuesnotin;
          $("#notfilters").val(JSON.stringify(parent.filterMapNotinDB));
    }
            }
         }else{
        filterValues.push($("#" + chekid).val().split("_")[0].replace('2q2', '_'));

        //        filtersall.push(filterData[name]);
        //        filtersall1=filterData[name];
        filterMap1[name1] = filterValues;

        parent.filterMapgraphs[name1] = filterValues;
        if (filterValuestb == undefined || filterValuestb == "") {

        } else {
            var index = filterValuestb.indexOf(filterData[name][id.split("_")[1]]);
            filterValuestb.splice(index, 1);
        }
        $("#CBOARP" + name1).val(JSON.stringify(parent.filterMapgraphs[name1]));
        for (var j = 0; j < filterValues.length; j++) {
            var nam = filterValues[j];
            var index1 = filtersall1.indexOf(nam);
            filtersall1.splice(index1, 1);
        }
        parent.filterMapNew[name1] = filterValues;
    }
    }
    else {

         if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
        
        if(filterValues==undefined || filterValues==""){
           for(var j=0;j<totalvalues.length-1;j++)
            {
                filterValues.push(totalvalues[j]);
    }
      }
        var flag=true;
        var index = filterValues.indexOf(totalvalues[id.split("_")[1]]);

       
        filterValues.splice(index, 1); parent.filterMapgraphs[name1]=  filterValues; filterMap1[name1] = filterValues;
        //alert(filterValuestb)
        if(filterValuestb==undefined || filterValuestb==""){
            //  parent.filterMapNew[name1].push(filterData[name][id.split("_")[1]]);
            var selectedFilters1 = [];
            for(var k=0;k<parent.filterMapNew[name1].length;k++){
                selectedFilters1.push(parent.filterMapNew[name1][k]);
            }
            selectedFilters1.push(filterData[name][id.split("_")[1]]);
            parent.filterMapNew[name1]=selectedFilters1;
            //  parent.filterMapNewtb=parent.filterMapNew;
            
        }else{
            for(var k=0;k<filterValuestb.length;k++){
                if(filterValuestb.length>=1 && filterValuestb[0]!="All"){
                    if(filterValuestb[k]==filterData[name][id.split("_")[1]]){
                        flag=false;
                    }
                }
            }
            if(flag){
                var selectedFilters1 = [];
                selectedFilters1=parent.filterMapNew[name1]
                selectedFilters1.push(filterData[name][id.split("_")[1]]);
                parent.filterMapNew[name1]=selectedFilters1;
                //       parent.filterMapNew[name1].push(filterData[name][id.split("_")[1]]);
                //       parent.filterMapNewtb=parent.filterMapNew;
                
            }

        }
        $("#CBOARP"+name1).val("");
                $("#CBOARP"+name1).val(JSON.stringify(parent.filterMapgraphs[name1]));
    }else{
          filterValuesnotin.push($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
         parent.filterMapNotinDB[name1] = filterValuesnotin;
         parent.filterMapNotin[name1] = filterValuesnotin;
         
          $("#notfilters").val(JSON.stringify(parent.filterMapNotinDB));
    }
    }


    var selectedlist=parent.filterMapNew[query];
     selnotinlist = parent.filterMapNotinDB[query];
    if ((selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") || selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {
        if (tabtype == "trend") {
            $("#spanmoreFilterst" + query).css('background-color', 'lightgray');
        } else if (tabtype == "advance") {
            $("#spanadmoreFiltersad" + query).css('background-color', 'lightgray');
        } else {
            $("#spangmoreFiltersg" + query).css('background-color', 'lightgray');
        }

    } else {
        if (tabtype == "trend") {
            $("#spanmoreFilterst" + query).css('background-color', 'white');
        } else if (tabtype == "advance") {
            $("#spanadmoreFiltersad" + query).css('background-color', 'white');
        } else {
            $("#spangmoreFiltersg" + query).css('background-color', 'white');
        }
    }
    $("#filters1").val(JSON.stringify(filterMap1));
// alert($("#filters1").val())
}
function applyGlobalFilterinreports(id, nameArr, chekid) {
    $('.selectall' + query).prop('checked', false);
   $('.unselectall' + query).prop('checked', false);
    var id = $("#" + chekid).val().split("_")[2]
    //                                          alert("applyGlobalFilterinOneview")
    var filterValues = []; var selnotinlist1 = [];
 var selectedlist
    var selnotinlist
     var notInVal = [];
       var filterVal = [];var dimid="new_CBOARP"+id;
        var ctxPath=document.getElementById("h").value;
        var reportId=parent.$("#REPORTID").val();
        $("#DimFilterBy").dialog({
        autoOpen: false,
        height: 250,
        width: 450,
        position: 'justify',
        modal: true,
        title:name
    });

if (typeof $("#new_CBOARP"+id).val() == "undefined" || $("#new_CBOARP"+id).val() == "" || $("#new_CBOARP"+id).val() === "[]") { 
         
 $.ajax({
        type: 'POST',
        async: false,
        
        url:  ctxPath+"/reportViewer.do?reportBy=getOperatorFilters&REPORTID="+reportId+"&dimId="+id,
        success: function(data) {
           $("#DimFilterBy").html(data);
        }
    })
}
         var likeVal=$("#LIKE_"+id).val();
    var notLikeVal=$("#NOTLIKE_"+id).val();
   
     if (typeof $("#new_CBOARP"+id).val() != "undefined" && $("#new_CBOARP"+id).val() != "" && $("#new_CBOARP"+id).val() != "[]") {
         
                notInVal=JSON.parse($("#new_CBOARP"+id).val()); 
    }
    if (tabtype == "table") {
        selectedlist = parent.filterMapNew[query];
        selnotinlist = parent.filterMapNotin[query];
        selnotinlist1 = parent.filterMapNotin[query];
    }
    if (document.getElementById(chekid).checked) {
         if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {
              for (var j = 0; j < selnotinlist.length; j++)
            {
                var slectnotvalue=$("#" + chekid).val().split("_")[0].replace('2q2', '_');
                if(slectnotvalue==selnotinlist[j]){
               var index = notInVal.indexOf($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
        notInVal.splice(index, 1);
         filterVal.push("All")
    $("#CBOARP" + id).val(JSON.stringify(filterVal));
     
       $("#new_CBOARP"+id).val(JSON.stringify(notInVal));
    }
            }
             parent.filterMapNotin[query]=notInVal;
         }else{
        
        filterValues.push($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
        parent.filterMapNew[$("#" + chekid).val().split("_")[2]].push($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
    }
    
    }
    else {
        var filterValues = [];
        if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
  
        filterValues = parent.filterMapNew[$("#" + chekid).val().split("_")[2]];
        if (filterValues == undefined || filterValues == "") {
            for (var j = 0; j < totalvalues.length - 1; j++)
            {
                //added by ram
                if (totalvalues[j] in parent.lookupMap) {
                    totalvalues[j] = parent.lookupMap[totalvalues[j]]
                }
                //endded by ram
                filterValues.push(totalvalues[j]);
            }

        }
        var index = filterValues.indexOf($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
        filterValues.splice(index, 1);
        parent.filterMapNew[$("#" + chekid).val().split("_")[2]] = filterValues;

    }else{
      
  filterVal.push("All")
 
    $("#CBOARP" + id).val(JSON.stringify(filterVal));
    selnotinlist1.push($("#" + chekid).val().split("_")[0].replace('2q2', '_'))
     parent.filterMapNotin[query]=selnotinlist1;
       notInVal.push($("#" + chekid).val().split("_")[0].replace('2q2', '_'));
       $("#new_CBOARP"+id).val(JSON.stringify(notInVal));
   

    }
        //        filterValues.splice(index, 1);
    }
         if (selnotinlist !== "" && selnotinlist !== undefined && selnotinlist.length >= 1 && selnotinlist[0] != "All") {

         }else{
//Added By Ram 30Nov15 for Lookup filter

    var selectedFiltersList = [];
    selectedFiltersList = parent.filterMapNew[$("#" + chekid).val().split("_")[2]]
    for (var k = 0; k < selectedFiltersList.length; k++)
    {
        if (selectedFiltersList[k] in parent.lookupdataMap) {
            selectedFiltersList[k] = parent.lookupdataMap[selectedFiltersList[k]]
        }
    }

    parent.filterMapNew[$("#" + chekid).val().split("_")[2]] = selectedFiltersList

//end Ram Code
    $("#CBOARP" + id).val(JSON.stringify(parent.filterMapNew[$("#" + chekid).val().split("_")[2]]))
        
    }
    //var selectedlist=parent.filterMapNew[query];
    if (selectedlist !== "" && selectedlist !== undefined && selectedlist.length >= 1 && selectedlist[0] != "All") {
        $("#spanmoreFilters" + query).css('background-color', 'lightgray');

    } else {
        $("#spanmoreFilters" + query).css('background-color', 'white');
    }
}
//end of sandeep code

function disableInitChart(chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var msg = 'Driver chart is : ';
    var keys = Object.keys(chartData["chart1"]["anchorChart"]);
    var name = chartData[keys[0]]["Name"];
    if (typeof name === 'undefined' || name === '') {
        name = keys[0];
    }
    msg += name;
    msg += "\nClick OK to remove selected chart from dependent charts list."
    if (!confirm(msg)) {
        return false;
    }
    var anchorChart = chartData["chart1"]["anchorChart"];
    var initCharts = anchorChart[Object.keys(anchorChart)[0]];
    initCharts.splice(initCharts.indexOf(chartId), 1);
    anchorChart[Object.keys(anchorChart)[0]] = initCharts;
    chartData["chart1"]["anchorChart"] = anchorChart;
    chartData[chartId]["localDrills"] = {};
    $("#initicon_" + chartId).hide();
    $("#chartData").val(JSON.stringify(chartData));
    var ctxPath = document.getElementById("h").value;
    $.ajax({
        type: 'POST',
        async: false,
        data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&chartData=" + encodeURIComponent(parent.$("#chartData").val()) + "&initializeFlag=true&action=localfilterGraphs",
        url: ctxPath + "/reportViewer.do?reportBy=drillCharts",
        success: function(data) {
            var jsondata = JSON.parse(data)["data"];
            $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
            generateChart(jsondata);
        }
    })
}
function selectAllCharts(id) {
    var value;
    if ($("#" + id).prop("checked")) {
        value = true;
    }
    else {
        value = false;
    }
    var chartData = JSON.parse($("#chartData").val());
    var count = Object.keys(chartData).length;
    for (var i = 0; i <= count; i++) {
        $('#initchart' + i).attr('checked', value);
    }
}
function showMeasureAlias(id, chartId) {
    if ($("#" + id).prop("checked")) {
        $("#defineMeasureAlias").show();
    }
    else {
        $("#defineMeasureAlias").hide();
    }
}
function changeConditionColors(chartId) {
    if ($("#" + chartId + "_dialType").val() == 'std') {
        $("#conditionColor0").css('background-color', 'red');
        $("#conditionColor2").css('background-color', "#00FF00");
    }
    else {
        $("#conditionColor0").css('background-color', "#00FF00");
        $("#conditionColor2").css('background-color', 'red');
    }
}

function createDrillUpPath(chartId, event1) {
    var divcontlist = '';
    if (typeof parent.$("#drills").val() != "undefined" && parent.$("#drills").val() != "") {
        var drills;
        drills = JSON.parse(parent.$("#drills").val())
        var drillkeys = Object.keys(drills)
        for (var i in drillkeys) {
            divcontlist += "<li  class=''><a id='drillUp#" + drillkeys[i] + "' class='gFontFamily gFontSize12' onclick='drillUpGraph(\"" + chartId + "\",this.id)'>" + drills[drillkeys[i]] + "</a>";
        }
    }
    else {
        divcontlist += "<li id='drillUp" + chartId + "' class=''><a class='gFontFamily gFontSize12'>" + "Already at the top level. Can not drill further up." + "</a>";
    }
    $("#drillUp1" + chartId).html('');
    $("#drillUp1" + chartId).append(divcontlist);
    if (event1.pageX > (screen.width - 150)) {
        parent.$("#drillUp1" + chartId).css("margin-left", "-100px");
    }
}

function isMultiMeasure(chartType) {
    if (chartType==='Pie' || chartType === 'Combo-MeasureH-Bar'
            || chartType === 'Filled-Horizontal' || chartType === 'Trend-Table-Combo' 
            || chartType === 'StackedBar-%' || chartType === 'StackedBarH-%'
            || chartType === 'Bullet-Horizontal' || chartType === 'StackedBar'
            || chartType === 'Horizontal-StackedBar' || chartType === 'OverLaid-Bar-Line' 
            || chartType === 'DualAxis-Bar' || chartType === 'Veraction-Combo3'
            || chartType === 'MultiMeasure-Bar' || chartType === 'Multi-Layered-Bar' 
            || chartType === 'StackedBarLine' || chartType === 'Cumulative-Line' || chartType === 'KPI-Table' 
            || chartType === 'Stacked-KPI' || chartType === 'Trend-KPI' || chartType === 'KPI-Bar' 
            || chartType === 'Standard-KPI' || chartType==="Standard-KPI1" || chartType === 'KPI Dashboard' 
            || chartType === 'MultiMeasure-Line' || chartType === 'Table' || chartType === 'Bar-Table' 
            || chartType === 'Vertical-Bar' || chartType === 'Vertical-Negative-Bar' || chartType === 'Horizontal-Bar'
            || chartType === 'SmoothLine' || chartType === 'Line' || chartType === 'MultiMeasureSmooth-Line' 
            || chartType === 'DualAxis-Bar' || chartType === 'DualAxis-Group' || chartType === 'DualAxis-Trget' 
            || chartType === 'Combo-Horizontal' || chartType === 'Cumulative-Bar' || chartType === 'Multi-Layered-Bar' 
            || chartType === 'Trend-Combo' || chartType === 'MultiMeasureH-Bar'
            || chartType==='Column-Pie') {
        return true;
    }
    else {
        return false;
    }
}

var checkButtonClick = false;
var clickedButtonId = [];
var clickedButtonFilter = "";
function measureButton(dataMap) {
    parent.$('#viewMeasureBlock').html('');
    var div = "chart1"
    //alert(JSON.stringify(filterskeys));
    var chartData = JSON.parse($("#chartData").val());
    var viewId = chartData[div]["dimensions"][1];
    var viewIdMulti = chartData[div]["dimensions"][2];
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewFilterkey = viewOvName[viewOvIds.indexOf(viewId)];
    var viewFilterkeyMulti = viewOvName[viewOvIds.indexOf(viewIdMulti)];
    //alert(viewFilterkey)
    var filterValue = 10;
    var html2 = "";
    html2 += "<table style='float:left;width:100%;'>";
    html2 += "<tr style='display:block;height:30px;margin-left:2%;float:left;width:100%;'>";
    if (checkButtonClick) {
        html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart2\")'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
    } else {

        html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter' class='buttonOnClick' onclick='filtersApply(this.name,this.id,\"chart2\")'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
    }
    if (typeof dataMap["chart2"][0][viewFilterkey] === 'undefined')
    {
        for (var keys = 0; keys < (filterData[viewFilterkey].length <= filterValue ? filterData[viewFilterkey].length : filterValue); keys++) {
            //  html2 +="<td style='height:30px;'><button class='button' onclick='' style='border-color: #D43F3A;font-size: 14px;border-radius: 5px;cursor: pointer;margin-right:3px'>"+filterData[filterskeys[0]][keys]+"</button>";
            if (filterData[viewFilterkey][keys].length > 12) {
                html2 += "<td style='height:30px;padding-right:10px'><button name='" + filterData[viewFilterkey][keys] + ":" + viewId + "' id='" + viewId + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id)' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>" + filterData[viewFilterkey][keys].substring(0, 12) + "..</button>";
            } else {
                html2 += "<td style='height:30px;padding-right:10px'><button name='" + filterData[viewFilterkey][keys] + ":" + viewId + "' id='" + viewId + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id)' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>" + filterData[viewFilterkey][keys] + "</button>";
            }

            html2 += "</td>";
        }
        html2 += "</tr>";
        if (typeof chartData[div]["enableQuickFilter2"] !== "undefined" && chartData[div]["enableQuickFilter2"] == "Y") {
            if (typeof viewIdMulti !== "undefined") {
                $("#viewMeasureBlock").css({"height": "60px"});

                html2 += "<tr style='display:block;height:30px;margin-left:2%;float:left;width:100%;'>";
                if (checkButtonClick) {
                    html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter1' class='quickFilterButton' onclick='filtersApply(this.name,this.id)'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
                } else {

                    html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter1' class='buttonOnClick' onclick='filtersApply(this.name,this.id)'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
                }
                for (var keys = 0; keys < (filterData[viewFilterkeyMulti].length <= filterValue ? filterData[viewFilterkeyMulti].length : filterValue); keys++) {
                    //  html2 +="<td style='height:30px;'><button class='button' onclick='' style='border-color: #D43F3A;font-size: 14px;border-radius: 5px;cursor: pointer;margin-right:3px'>"+filterData[filterskeys[0]][keys]+"</button>";
                    if (filterData[viewFilterkeyMulti][keys].length > 12) {
                        html2 += "<td style='height:30px;padding-right:10px'><button name='" + filterData[viewFilterkeyMulti][keys] + ":" + viewIdMulti + "' id='" + viewIdMulti + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id)' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px'>" + filterData[viewFilterkeyMulti][keys].substring(0, 12) + "..</button>";
                    } else {
                        html2 += "<td style='height:30px;padding-right:10px'><button name='" + filterData[viewFilterkeyMulti][keys] + ":" + viewIdMulti + "' id='" + viewIdMulti + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id)' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px'>" + filterData[viewFilterkeyMulti][keys] + "</button>";
                    }

                    html2 += "</td>";
                }
                html2 += "</tr>";
            }
        }
    }
    else {
        for (var keys = 0; keys < (dataMap["chart2"].length <= filterValue ? dataMap["chart2"].length : filterValue); keys++) {
            //  html2 +="<td style='height:30px;'><button class='button' onclick='' style='border-color: #D43F3A;font-size: 14px;border-radius: 5px;cursor: pointer;margin-right:3px'>"+filterData[filterskeys[0]][keys]+"</button>";
            if (dataMap["chart2"].length > 12) {
                html2 += "<td style='height:30px;padding-right:10px'><button name='" + dataMap["chart2"][keys][viewFilterkey] + ":" + viewId + "' id='" + viewId + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart2\")' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>" + dataMap["chart2"][keys][viewFilterkey].substring(0, 12) + "..</button>";
            } else {
                html2 += "<td style='height:30px;padding-right:10px'><button name='" + dataMap["chart2"][keys][viewFilterkey] + ":" + viewId + "' id='" + viewId + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart2\")' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>" + dataMap["chart2"][keys][viewFilterkey] + "</button>";
            }

            html2 += "</td>";
        }
        html2 += "</tr>";
        if (typeof chartData[div]["enableQuickFilter2"] !== "undefined" && chartData[div]["enableQuickFilter2"] == "Y") {
            if (typeof viewIdMulti !== "undefined") {
                $("#viewMeasureBlock").css({"height": "60px"});

                html2 += "<tr style='display:block;height:30px;margin-left:2%;float:left;width:100%;'>";
                if (checkButtonClick) {
                    html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter1' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart3\")'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
                } else {

                    html2 += "<td style='height:30px;padding-right:10px'><button name='All' id='allFilter1' class='buttonOnClick' onclick='filtersApply(this.name,this.id,\"chart3\")'   style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px;white-space:nowrap;'>ALL</button>";
                }
                for (var keys = 0; keys < (dataMap["chart3"].length <= filterValue ? dataMap["chart3"].length : filterValue); keys++) {
                    //  html2 +="<td style='height:30px;'><button class='button' onclick='' style='border-color: #D43F3A;font-size: 14px;border-radius: 5px;cursor: pointer;margin-right:3px'>"+filterData[filterskeys[0]][keys]+"</button>";
                    if (dataMap["chart3"][keys][viewFilterkeyMulti].length > 12) {
                        html2 += "<td style='height:30px;padding-right:10px'><button name='" + dataMap["chart3"][keys][viewFilterkeyMulti] + ":" + viewIdMulti + "' id='" + viewIdMulti + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart3\")' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px'>" + dataMap["chart3"][keys][viewFilterkeyMulti].substring(0, 12) + "..</button>";
                    } else {
                        html2 += "<td style='height:30px;padding-right:10px'><button name='" + dataMap["chart3"][keys][viewFilterkeyMulti] + ":" + viewIdMulti + "' id='" + viewIdMulti + keys + "' class='quickFilterButton' onclick='filtersApply(this.name,this.id,\"chart3\")' onmouseover='addHighlight(this.id)' onmouseout='removeHighlight(this.id)' style='padding: 5px 10px;border: 0 none;font-weight:50;letter-spacing: 1px;border-radius:4px;font-size: 10px;cursor: pointer;margin-right:3px'>" + dataMap["chart3"][keys][viewFilterkeyMulti] + "</button>";
                    }

                    html2 += "</td>";
                }
                html2 += "</tr>";
            }
        }
    }
    html2 += "</table>";
    //alert(html2)
    parent.$('#viewMeasureBlock').append(html2);
    if (checkButtonClick) {
        if (typeof chartData[div]["multiFilter"] !== "undefined" && chartData[div]["multiFilter"] == "Y") {
            for (var k = 0; k < clickedButtonId.length; k++) {
                $("#" + clickedButtonId[k]).removeClass("quickFilterButton");
                $("#" + clickedButtonId[k]).removeClass("buttonOnMouse");
                $("#" + clickedButtonId[k]).addClass("buttonOnClick");
            }
        } else {
            $("#" + clickedButtonFilter).removeClass("quickFilterButton");
            $("#" + clickedButtonFilter).removeClass("buttonOnMouse");
            $("#" + clickedButtonFilter).addClass("buttonOnClick");
        }
    }
}
function filtersApply(idArr, id, chartId) {
    var buttonId = id;
    clickedButtonId.push(id);
    clickedButtonFilter = id;
    $("#" + buttonId).removeClass("quickFilterButton");
    $("#" + buttonId).removeClass("buttonOnMouse");
    $("#" + buttonId).addClass("buttonOnClick");
    $("#allFilter").removeClass("buttonOnClick");
    $("#allFilter1").removeClass("buttonOnClick");
    $("#driver").val(chartId);
    checkButtonClick = true;
    var chartData = JSON.parse($("#chartData").val());
    var div = "chart1";
    var drillValues = [];
    var drills = {};
    if (id == "allFilter") {
        $("#drills").val('');
    } else {



        if (typeof chartData[div]["multiFilter"] !== "undefined" && chartData[div]["multiFilter"] == "Y") {

            if (typeof $("#drills").val() != "undefined" && $("#drills").val() != "") {
                drills = JSON.parse($("#drills").val());
                if (typeof drills[idArr.split(":")[1]] != "undefined") {

                    drillValues = drills[idArr.split(":")[1]];
                }
            }
            drillValues.push(idArr.split(":")[0]);
            drills[idArr.split(":")[1]] = drillValues;
            $("#drills").val(JSON.stringify(drills));
        } else {
            drillValues.push(idArr.split(":")[0]);
            drills[idArr.split(":")[1]] = drillValues;
            $("#drills").val(JSON.stringify(drills));
        }
    }
    // alert(JSON.stringify(drills))
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
            function(data) {
                generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                $("#driver").val("");
                $("#loading").hide();
            });
}

function addHighlight(id) {
    $("#" + id).removeClass("quickFilterButton");
    $("#" + id).addClass("buttonOnMouse");
}
function removeHighlight(id) {
    $("#" + id).removeClass("buttonOnMouse");
    $("#" + id).addClass("quickFilterButton");
}


function buildXaxisFilterAdvance(div, d, i) {
    //alert("buildXaxisFilterAdvance=="+d)
    var chartData = JSON.parse(parent.$("#chartData").val());
    var x = 1;
    if (typeof chartData[div]["XaxisTicks"] != "undefined" && chartData[div]["XaxisTicks"] != "") {
        x = parseInt(chartData[div]["XaxisTicks"]);
        x++;
    }

    if (typeof displayX !== "undefined" && displayX == "Yes") {
        if (i % x == 0) {
            if (d.length < 15) {
                return d;
            } else {
                return d.substring(0, 15) + "..";
            }
        } else
            return "";
    }
}

function kpiTable(measureArray, chartId) {
    var kpiHtml = '';
    //    var viewBys=JSON.parse($("#viewby").val());
    //    kpiHtml += "<select id=timeDimAd>";
    //    for(var i in viewBys){
    //        for(var j in timeDims){
    //            if(viewBys[i].trim()==timeDims[j].trim()){
    //                kpiHtml += "<option value='"+viewBys[i]+"'>"+viewBys[i]+"</option>";
    //            }
    //        }
    //    }

    //    kpiHtml += "<div class='dropdown' style='cursor: pointer;margin-left:109%'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-right:2%;width:20px;height:20px' src='images/changeView.png' alt='Options' onclick='changeViewByAd(\"" + chartId + "\")' ></img></span>";
    //    kpiHtml += "<ul id='viewByChangeAd"+chartId+"' class='dropdown-menu'></ul>";
    //    kpiHtml += "</div>";

    kpiHtml += "<div style='margin-left:8%;margin-top:3px;width: 103%'>";
    kpiHtml += "<table id=\"chartTable\" class='table table-condensed table-bordered' style=\"float:left;height: auto;text-align:center;vertical-align:middle;font-size:10px;width:100%\">";
    //    kpiHtml += "<tr align='center' style='color:black;background-color:#B2B2FF'>";
    //    kpiHtml += "<td style='white-space:nowrap;font-weight:bold;text-align:left;background-image: -moz-linear-gradient(center top , #EBF3FC, #DCE9F9);box-shadow: 0px 1px 0px rgba(255, 255, 255, 0.8) inset' ><strong>Measure</strong></th>"
    //    kpiHtml += "<td style='white-space:nowrap;font-weight:bold;text-align:left;background-image: -moz-linear-gradient(center top , #EBF3FC, #DCE9F9);box-shadow: 0px 1px 0px rgba(255, 255, 255, 0.8) inset' ><strong>Value</strong></th>"
    //    if(measureArray.length>1){
    //        kpiHtml += "<td style='white-space:nowrap;font-weight:bold;text-align:left;background-image: -moz-linear-gradient(center top , #EBF3FC, #DCE9F9);box-shadow: 0px 1px 0px rgba(255, 255, 255, 0.8) inset' ><strong>Measure</strong></th>"
    //        kpiHtml += "<td style='white-space:nowrap;font-weight:bold;text-align:left;background-image: -moz-linear-gradient(center top , #EBF3FC, #DCE9F9);box-shadow: 0px 1px 0px rgba(255, 255, 255, 0.8) inset' ><strong>Value</strong></th>"
    //    }
    //    kpiHtml += "</tr>";
    var chartData = JSON.parse($("#chartData").val());
    var gtValuesList = chartData["chart1"]["GTValueList"]
    var col = 3, len = measureArray.length;
    if (len >= col) {
        len = len - len % col;
    }
    else {
        len = len % col;
    }
    var width = 984 / (len > col ? col : len);
    for (var i in measureArray) {
        if (i % col == 0) {
            kpiHtml += "<tr>";
        }
        var colspan = 1, wid = 0, backColor = "#1064AF";
        if (i == 0) {
            backColor = "#800000";
        }
        if (i < len) {
            wid = width;
        }
        else {
            wid = 984;
            switch (measureArray.length - len) {
                case 1:
                    colspan = col;
                    break;
                case 2:
                    colspan = 2;
                    break;
                case 3:
                    colspan = 1;
                    break;
            }
        }
        if (typeof chartData["chart1"]["rounding"] !== "undefined") {
            yAxisRounding = chartData["chart1"]["rounding"];
        } else {
            yAxisRounding = 1;
        }
        if (typeof chartData["chart1"]["yAxisFormat"] !== "undefined") {
            yAxisFormat = chartData["chart1"]["yAxisFormat"];
        } else {
            yAxisFormat = "Auto";
        }

        if (i == 0) {
            if (yAxisRounding > 0) {
                kpiHtml += "<td id='" + i + "' colspan=" + colspan + " style='color:white;width:" + wid + "px;cursor: pointer;white-space:nowrap;font-weight:bold;text-align:center;background-color:#7F1616' onclick='changeMeasureArray(this.id)'><strong>" + measureArray[i] + "&nbsp;:&nbsp" + numberFormat(gtValuesList[i], yAxisFormat, yAxisRounding, "chart1") + "</strong></th>"
            }
            else {
                kpiHtml += "<td id='" + i + "' colspan=" + colspan + " style='color:white;width:" + wid + "px;cursor: pointer;white-space:nowrap;font-weight:bold;text-align:center;background-color:#7F1616' onclick='changeMeasureArray(this.id)'><strong>" + measureArray[i] + "&nbsp;:&nbsp" + addCommas(numberFormat(gtValuesList[i], yAxisFormat, yAxisRounding, "chart1")) + "</strong></th>"

            }
        }
        else {
            if (yAxisRounding > 0) {

                kpiHtml += "<td id='" + i + "' colspan=" + colspan + " style='color:white;width:" + wid + "px;cursor: pointer;white-space:nowrap;font-weight:bold;text-align:center;background-image: -moz-linear-gradient(center top , #70A2CF, " + backColor + ")' onclick='changeMeasureArray(this.id)'><strong>" + measureArray[i] + "&nbsp;:&nbsp" + numberFormat(gtValuesList[i], yAxisFormat, yAxisRounding, "chart1") + "</strong></th>"
            }
            else {

                kpiHtml += "<td id='" + i + "' colspan=" + colspan + " style='color:white;width:" + wid + "px;cursor: pointer;white-space:nowrap;font-weight:bold;text-align:center;background-image: -moz-linear-gradient(center top , #70A2CF, " + backColor + ")' onclick='changeMeasureArray(this.id)'><strong>" + measureArray[i] + "&nbsp;:&nbsp" + addCommas(numberFormat(gtValuesList[i], yAxisFormat, yAxisRounding, "chart1")) + "</strong></th>"
            }
        }

        kpiHtml += "<b></td>";
        if ((i + 1) % col == 0) {
            kpiHtml += "</tr>";
        }
    }
    kpiHtml += "</table>";
    kpiHtml += "</div>";
    return kpiHtml;
}

function changeViewByAd(chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var quickViewname = [];
    var quickViewId = [];
    var dimIds = chartData[chartId]["dimensions"];
    var rowViewBy = [];
    var rowViewIds = [];
    var viewByIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewByNames = JSON.parse(parent.$("#viewby").val());
    //    alert("dim:"+rowViewIds);
    //    alert("viewByIds:"+viewByIds);
    //    alert("viewByNames:"+viewByNames);
    for (var i in dimIds) {
        for (var j in viewByIds) {
            if (viewByIds[j].toString() === dimIds[i].toString()) {
                rowViewIds.push(dimIds[i]);
                rowViewBy.push(viewByNames[j]);
            }
        }
    }
    var selectedView = chartData[chartId]["viewBys"][0];
    var selectedViewId = chartData[chartId]["viewIds"];
    var timeDim = ["Month Year", "Month - Year", "Month-Year", "Time", "Year", "year", "Month", "Qtr", "qtr", "Month ", "Qtr Year"];
    for (var i = 0; i < rowViewIds.length; i++) {
        for (var k = 0; k < timeDim.length; k++) {
            if (rowViewBy[i] == timeDim[k]) {
                quickViewname.push(rowViewBy[i]);
                quickViewId.push(rowViewIds[i]);
            }
        }
    }
    var kpiHtml = '';
    if (quickViewname.length == 0) {
        alert("No time dimension available!");
        return;
    }
    for (var i = 0; i < quickViewname.length; i++) {
        //        if(i==0)
        //            kpiHtml += "<li><a class='' id='"+quickViewname[i]+":"+quickViewId[i]+"' name='tab"+parseInt(i+1)+"' style=\"cursor: pointer\" onclick='changeQuickGroup(this.id)'>" + quickViewname[i] + "</a></li>";
        if (quickViewname[i] != selectedView)
            kpiHtml += "<li><a class='' id='" + quickViewname[i] + ":" + quickViewId[i] + "' name='tab" + parseInt(i + 1) + "' style=\"cursor: pointer\" onclick='changeQuickGroup(this.id)'>" + quickViewname[i] + "</a></li>";
    }
    $('#viewByChangeAd' + chartId).html(kpiHtml);
}
function showAppliedFilters(chartId, event) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var filters = chartData[chartId]["filters"];
    var filterKeys = Object.keys(filters);
    var html = '';
    html += "<table id='tag_all'>";


    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewByIds = JSON.parse(parent.$("#viewbyIds").val());
    var appliedFilters = [];
    var tag_ex = '';
    var tag_in = '';
    for (var i in filterKeys) {
        var filterGroupBy = viewBys[viewByIds.indexOf(filterKeys[i])];
        var filters1 = filterData[filterGroupBy];
        var filterValues = filters[filterKeys[i]]
        //        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv2(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='overflow-y:auto;border-bottom: 1px solid lightgrey;height:22px'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
        //        if (filterValues.length === 0) {
        //            html += "<input style='margin-left:6px' type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters1.length + "*," + filterGroupBy + "' checked onclick='selectAllChildNodes(this.id,this.name,\""+filterCounter+"\")'/>";
        //        } else {
        //            html += "<input style='margin-left:6px' type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters1.length + "*," + filterGroupBy + "' onclick='selectAllChildNodes(this.id,this.name,\""+filterCounter+"\")'/>";
        //        }
        //        html += "<span>&nbsp;" + filterGroupBy + "</span></label></div>";
        //        html += "</div>";
        //        html += "<div id='localFilter"+filterGroupBy.replace(/\s/g, '')+"_1' class='collapseDiv' style='display:none'><table style='' width='100%'>";
        var inFilters = $(filters1).not(filterValues).get();
        if ((filterKeys[i] === chartData[chartId]["viewIds"][0]) && (inFilters.length !== filters1.length)) {

            for (var k in inFilters) {
                //            appliedFilters.push(inFilters[k]);
                tag_ex += " <tr style='background-color:'><td class='gFontFamily gFontSize13'>" + inFilters[k] + "</td></tr>";
                //            html += "<input type='checkbox' id='L_1" + filterGroupBy.replace(/\s/g, '') + "_" + (filterCounter++) + "' name='"+inFilters[k]+"' value='' checked>"+inFilters[k]+"<br>";
            }
        } else {
            inFilters = filterValues;

            for (var k in inFilters) {
                //            appliedFilters.push(inFilters[k]);
                tag_in += " <tr style='background-color:'><td class='gFontFamily gFontSize13'>" + inFilters[k] + "</td></tr>";
                //            html += "<input type='checkbox' id='L_1" + filterGroupBy.replace(/\s/g, '') + "_" + (filterCounter++) + "' name='"+inFilters[k]+"' value='' checked>"+inFilters[k]+"<br>";
            }
        }
        //        for(var j in filterValues){
        //            html += "<input type='checkbox' id='L_1" + filterGroupBy.replace(/\s/g, '') + "_" + (filterCounter++) + "' name='"+filterValues[j]+"' value=''>"+filterValues[j]+"<br>";
        //        }
        //        html += "</table></div>";
    }
    //    html += "<input type='button' onclick='updateLocalFilters(\"" + chartId + "\",\""+filterCounter+"\")' value='Done'/>"
    //    html += "<table>";
    //    for(var i in appliedFilters){
    //        html += "<tr>";
    //        html += "<td class='gFontFamily gFontSize13'>"+appliedFilters[i];
    //        html += "</td>";
    //        html += "</tr>";
    //    }
    if (tag_ex !== '') {
        html += "<tr id='tag_ex' style='background-color:'><td class='gFontFamily gFontSize13'style ='color:#3399FF;font-weight:bold'>Exclude</td></tr>";
        html += tag_ex;
    }
    if (tag_in !== '') {
        html += "<tr id='tag_in' style='background-color:'><td class='gFontFamily gFontSize13'style ='color:#3399FF;font-weight:bold'>Include</td></tr>";
        html += tag_in;
    }
    html += "</table>";
    $("#appliedFilters").html(html);
    $("#appliedFilters").show(500).css({
        position: "absolute",
        top: event.pageY + 10,
        left: event.pageX
    });
}

function selectAllChildNodes(id, name, filterCount) {
    var filterGroupBy = name.split("*,")[1].replace("L_", "", "gi");
    var filters = filterData[filterGroupBy];
    for (var filter = filterCount; filter < (filters.length + parseInt(filterCount)); filter++) {
        if (!parent.document.getElementById(id).checked) {
            $("#" + id + "_" + filter.toString()).prop('checked', false);
        }
        else {
            $("#" + id + "_" + filter.toString()).prop('checked', true);
        }
    }
}
function expandDiv2(chartId) {
    $(".expandDiv1" + chartId).toggleClass("expandDivLocal1");

    $("#localFilter" + chartId + "_1").toggle(500);
}

function updateLocalFilters(chartId, filterCount) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    //    var viewIds = chartDetails["viewIds"];
    //    var filterMap = {};
    var filterKeys = chartData[chartId]["dimensions"];
    var temp;
    if (typeof chartData[chartId]["dimensions"] == "undefined" || chartData[chartId]["dimensions"] == "") {
        filterKeys = chartData[chartId]["viewBys"];
    } else {
        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        temp = filterKeys;
        filterKeys = [];
        for (var key in temp) {

            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
        }
    }
    var totalViewBys = JSON.parse(parent.$("#viewby").val());
    var totalViewIds = JSON.parse(parent.$("#viewbyIds").val());
    var localFilters = chartData[chartId]["filters"];
    var localFiltersNew = {};
    var keys2 = Object.keys(localFilters);
    //    for(var l=0;l<keys2.length;l++){
    //        var arr1=[];
    //        for(var a in localFilters[keys2[l]]){
    //            arr1[a]=localFilters[keys2[l]][a];
    //        }
    //        localFiltersNew[keys2[l]]=arr1;
    //    }
    var localFilterKeys = Object.keys(localFilters);
    var filter = 0;
    for (var key in localFilterKeys) {
        var filterGroupBy = totalViewBys[totalViewIds.indexOf(localFilterKeys[key].toString().trim())];
        var filters = localFilters[localFilterKeys[key]];
        var viewByFilter = [];
        for (var j = 0; j < filterData[filterGroupBy].length; j++) {
            if (!(parent.document.getElementById("L_1" + filterGroupBy.replace(/\s/g, '') + "_" + filter).checked)) {
                viewByFilter.push(parent.document.getElementById("L_1" + filterGroupBy.replace(/\s/g, '') + "_" + filter).getAttribute("name"));
            }
            filter++;
        }
        if (viewByFilter.length == 0) {
            delete localFiltersNew[localFilterKeys[key]];
        }
        else {
            localFiltersNew[localFilterKeys[key]] = viewByFilter;
        }
    }
    if (Object.keys(localFiltersNew).length == 0)
    {
        $("#newLocalFilter" + chartId).hide();
    }
    chartDetails["filters"] = localFiltersNew;
    chartData[chartId] = chartDetails;


    parent.$("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs", $("#graphForm").serialize(),
            function(data) {
                var resultset = data


                var data = JSON.parse(resultset)["data"];

                var chartData = JSON.parse($("#chartData").val());
                var chartFlag = "false";
                var GTdiv = [];
                for (var t in chartData) {
                    if (chartData[t]["chartType"] == "KPI-Table" || chartData[t]["chartType"] == "Expression-Table" || chartData[t]["chartType"] == "Emoji-Chart" || chartData[t]["chartType"] == "Stacked-KPI" || chartData[t]["chartType"] == "KPIDash" || chartData[t]["chartType"] == "Bullet-Horizontal" || chartData[t]["chartType"] == "Standard-KPI" || chartData[t]["chartType"] == "Standard-KPI1" || chartData[t]["chartType"] == "Veraction-Combo" || chartData[t]["chartType"] == "Radial-Chart" || chartData[t]["chartType"] == "LiquidFilled-KPI" || chartData[t]["chartType"] == "Dial-Gauge" || chartData[t]["chartType"] == "Emoji-Chart") {
                        chartFlag = "true"
                        //                 GTdiv.push(t)

                        $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                    }
                }
                $("#appliedFilters").hide();
                alert("Local filters changed successfully");
                generateSingleChart(resultset, chartId);
            });

}
function selectNumberFormat(id, chartId) {
    if ($("#" + id).val() === 'Auto') {
        $("#" + chartId + "roundingType").show();
    }
    else {
        $("#" + chartId + "roundingType").hide();
    }
}

function topBottomChart(chartId, chWidth, chHeight) {
    var chartData = JSON.parse($("#chartData").val());
    var chartType = chartData[chartId]["chartType"];
    chartData[chartId]["sorting"] = "D";
    var currData = [];
    var mainData;
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async: false,
        type: "POST",
        data: parent.$("#graphForm").serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs",
        success: function(data) {
            var jsonData = JSON.parse(JSON.parse(data)["data"]);

            var records = 5;
            for (var i = 0; i < (jsonData[chartId].length < records ? jsonData[chartId].length : records); i++) {
                currData.push(jsonData[chartId][i]);
            }


            chartData[chartId]["sorting"] = "A";
            parent.$("#chartData").val(JSON.stringify(chartData));
            $.ajax({
                async: false,
                type: "POST",
                data: parent.$("#graphForm").serialize(),
                url: $("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&action=localfilterGraphs",
                success: function(data) {

                    var records = 5;
                    var jsonData = JSON.parse(JSON.parse(data)["data"]);
                    mainData = jsonData;
                    for (var j = 0; j < (jsonData[chartId].length < records ? jsonData[chartId].length : records); j++) {
                        currData.push(jsonData[chartId][j]);
                    }
                    mainData[chartId] = currData;
                    //         var map = {};
                    //         map["data"]= JSON.stringify(mainData);
                    //            generateSingleChart(JSON.stringify(map),chartId);

                    if (chartType == "TopBottom-Chart") {
                        buildTopBottomHorizotal(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight);
                    }
                }
            });
        }
    });

}
//Added by shobhit for multi dashboard on 22/09/15
function defineParentChildDB() {
    var REPORTID = document.getElementById("REPORTID").value;
    if (typeof REPORTID === "undefined" || REPORTID === "") {
        REPORTID = $("#graphsId").val();
    }
    var ctxPath = document.getElementById("h").value;
    var frameObj = document.getElementById("defineParentChildDBFrame");
    $.ajax({
        async: false,
        type: "POST",
        data:
                $('#ViewByForm').serialize() + "&REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&userId=" + $("#usersId").val() + "&visualName=" + $("#currType").val(),
        url: ctxPath + "/reportViewerAction.do?reportBy=getSelectedDimsFacts",
        success: function(data) {
            var source = ctxPath + "/Report/Viewer/DefineMultiDashboard.jsp?REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&from=isedit";
            frameObj.src = source;
        }
    });
    //    $("#defineParentChildDB").dialog('open');
    var opt = {
        autoOpen: false,
        modal: true,
        width: 550,
        height: 500
//        title: 'Define Multi Dashboard'
    };
    var theDialog = $("#defineParentChildDB").dialog(opt);
    theDialog.dialog("open");
}

function getComparableViews(chartId, index) {

//    $("#compare"+chartId).html('');
    var div = chartId;
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
    var quickViewname = [];
    var quickViewId = [];
    var timeDim = ["Month Year", "Month - Year", "Month-Year", "Time", "Year", "year", "Month", "Qtr", "qtr", "Month ", "Qtr Year"];
    for (var i = 0; i < viewIds.length; i++) {
        for (var k = 0; k < timeDim.length; k++) {
            if (viewBys[i] == timeDim[k]) {
                quickViewname.push(viewBys[i]);
                quickViewId.push(viewIds[i]);
            }
        }
    }
    if (quickViewname.length == 0) {
        alert("No time dimension available!");
        return;
    }
    var html = "";
    html = html + "<ul id='comp" + chartId + "' class='dropdown-menu'>";
    for (var i = 0; i < quickViewname.length; i++) {
        html += "<li class=''><a class='' id='" + quickViewname[i] + ":" + quickViewId[i] + "' name='tab" + parseInt(i + 1) + "' style=\"cursor: pointer\" onclick='getComparableValues(this.id,\"" + div + "\",\"" + quickViewname[i] + "\")'>" + quickViewname[i] + "</a></li>";
    }
//    html += "<li style='display:none'><a class=''   style=\"cursor: pointer\" onclick='getChangeHtmlDiv(\""+div+"\")'>Change</a></li>";
    html += "</ul>";
    $("#comp" + chartId).remove();
    $("#compare" + chartId + index).append(html);
}

function getComparableValues(idArr, chartId, quickViewname) {
    $("#driver").val("");

    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    var divHeight = ($(window).height() - $(window).height() / 6);
    var divWidth = ($(window).width() - ($(window).width() / 1.4));
    parent.$("#tempDashletDiv").dialog({
        autoOpen: false,
        height: divHeight,
        width: divWidth,
        position: 'justify',
        modal: true,
        title: "Comparable Values: " //+ chartDetails["Name"].
    });

    var viewIds = idArr.split(":")[1];
    var viewBy = idArr.split(":")[0];
    var timeComparison = "";
    var timeComparison1 = "";
    var timeComparisonArray = [];
    var timeComparisonArrayLabels = [];
    timeComparisonArray.push("MTD", "PMTD", "PYMTD", "QTD", "PQTD", "PYQTD", "YTD", "PYTD", "MOM", "MOYM", "QOQ", "QOYQ", "YOY", "MOMPer", "MOYMPer", "QOQPer", "QOYQPer", "YOYPer");
    timeComparisonArrayLabels.push("MTD", "PMTD", "PYMTD", "QTD", "PQTD", "PYQTD", "YTD", "PYTD", "MOM", "MOYM", "QOQ", "QOYQ", "YOY", "MOM %", "MOYM %", "QOQ %", "QOYQ %", "YOY %");
    //c   if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month"){
    //   timeComparison = "MOM";
    //   timeComparison1 = "MOY";
    //   }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year"){
    //      timeComparison = "QOQ";
    //   timeComparison1 = "QOY";
    //   }else {
    //      timeComparison = "YOY";
    //   timeComparison1 = "YOY";
    //   }
    var html = "";

    //    for (var key in filterKeys) {
    var filterMap = {};
    if (typeof chartDetails["comparedValue"] !== "undefined") {
        filterMap = chartDetails["comparedValue"];
    }

    var filterGroupBy = viewBy;

    var filters = filterData[filterGroupBy];
    var selectedFilters = [];
    if (typeof filterMap[viewIds] !== "undefined") {
        selectedFilters = filterMap[viewIds];
    }
    html += "<div class='expandDivComp" + filterGroupBy.replace(/\s/g, '') + " expandDiv' name='expand' width='13%' onclick='expandDivComp(\"" + filterGroupBy.replace(/\s/g, '') + "\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
    //     adaa   html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
    var timeComparisonConter = 0;
    var appliedTimeComparisons = chartDetails["timeComparison"];
    if (typeof appliedTimeComparisons != 'undefined') {
        for (var i in appliedTimeComparisons) {
            if (Object.keys(appliedTimeComparisons[i])[0] === appliedTimeComparisons[i][Object.keys(appliedTimeComparisons[i])[0]]) {
                timeComparisonConter++;
            }
        }
    }
    if (selectedFilters.length == 0 && timeComparisonConter == 0) {
        html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' onclick='selectAllTimeComparisons(this.id,this.name,\"" + chartId + "\")'/>";
    } else {
        html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' checked onclick='selectAllTimeComparisons(this.id,this.name,\"" + chartId + "\")'/>";
    }
    html += "<span>&nbsp;" + filterGroupBy + "</span></label></div>";
    html += "</div>";
    html += "<div id='localFilterComp" + filterGroupBy.replace(/\s/g, '') + "' class='collapseDiv' style='display:none'><table style='' width='100%'>";
    for (var k = 0; k < timeComparisonArray.length; k++) {
        html += "<tr><td ><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
        if (typeof chartData[chartId]["timeComparison"] !== "undefined" && typeof chartData[chartId]["timeComparison"][k] !== "undefined" && chartData[chartId]["timeComparison"][k][timeComparisonArray[k]] == timeComparisonArray[k]) {
            html += "<input type='checkbox' id='" + chartId + "_" + timeComparisonArray[k] + "' checked>";
            html += "<span class='box'>&nbsp;" + timeComparisonArrayLabels[k] + "</span>";
        } else {
            html += "<input type='checkbox' id='" + chartId + "_" + timeComparisonArray[k] + "' >";
            html += "<span class='box'>&nbsp;" + timeComparisonArrayLabels[k] + "</span>";
        }
        html += "</span></label></td></tr>";

    }
    //          html += "<tr><td ><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
    //                if(typeof chartData[chartId]["timeComparison"]!=="undefined" && typeof chartData[chartId]["timeComparison"][1]!=="undefined" && chartData[chartId]["timeComparison"][1][timeComparison1]==timeComparison1){
    //                    html +="<input type='checkbox' id='"+chartId+"_"+timeComparison1+"' checked>";
    //                    html += "<span class='box'>&nbsp;" + timeComparison1 + "</span>";
    //                }else{
    //                    html +="<input type='checkbox' id='"+chartId+"_"+timeComparison1+"' >";
    //                     html += "<span class='box'>&nbsp;" + timeComparison1 + "</span>";
    //                }
    //                html += "</span></label></td></tr>";
    var filterCounter = 0;
    for (var filter in selectedFilters) {
        html += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
        //            if(typeof filters[filter]!="undefined" && filters[filter]!=""){

        if (typeof selectedFilters[filter] != "undefined") {

            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "' checked value='" + selectedFilters[filter] + "' name='" + viewIds + "'/>";
            filterCounter++;
            html += "<span class='box'>&nbsp;" + selectedFilters[filter] + "</span></span></label></td></tr>";
        }
    }
    var diff = $(filters).not(selectedFilters).get();
    for (var filter in diff) {
        html += "<tr style='display:none'><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
        //            if(typeof filters[filter]!="undefined" && filters[filter]!=""){

        if (typeof diff[filter] != "undefined") {
            if (diff[filter].trim() === "") {
                var empty = "empty";
                html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filterCounter + "'  value='" + empty + "' name='" + viewIds + "'/>";
            } else {
                html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filterCounter + "' value='" + diff[filter] + "' name='" + viewIds + "'/>";
            }
            filterCounter++;
            html += "<span class='box'>&nbsp;" + diff[filter] + "</span></span></label></td></tr>";
        }
    }
    html += "</table></div>";
    //    }
    var flag = "true";
    html += "<table><tr><td><input type='button' value='done' onClick='getComparableData(\"" + chartId + "\",\"" + viewIds + "\",\"" + viewBy + "\",\"" + flag + "\",\"" + quickViewname + "\")' /></td>";

    html += "</tr></table>";
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');

}


function getComparableData(chartId) {
    var timeComparisonArray = [];
    var selectedComparisons = [];
    timeComparisonArray.push("MTD", "PMTD", "PYMTD", "QTD", "PQTD", "PYQTD", "YTD", "PYTD", "MOM", "MOYM", "QOQ", "QOYQ", "YOY", "MOMPer", "MOYMPer", "QOQPer", "QOYQPer", "YOYPer");
    for (var i in timeComparisonArray) {
        if (parent.document.getElementById(timeComparisonArray[i]).checked) {
            selectedComparisons.push(timeComparisonArray[i]);
        }
    }
    var chartData = JSON.parse(parent.$("#chartData").val())
    chartData[chartId]["customTimeComparisons"] = selectedComparisons;
    parent.$("#chartData").val(JSON.stringify(chartData));
    parent.$("#tempDashletDiv").dialog().dialog('close');
}

function getTrendDril(trendType) {
    if (trendType == timeDBDrill) {
        trendType = "";
        timeDBDrill = "";
        $("#loading").show();
        var ctxPath = document.getElementById("h").value;
        $.ajax({
            type: 'POST',
            async: false,
            data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()),
            url: ctxPath + "/reportViewer.do?reportBy=drillCharts",
            success: function(data) {
                $("#loading").hide();
                generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                $("#driver").val("");


            }
        })
    }
    else {
        timeDBDrill = trendType;
        $("#loading").show();
        var ctxPath = document.getElementById("h").value;
        $.ajax({
            type: 'POST',
            async: false,
            data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + encodeURIComponent(parent.$("#graphName").val()) + "&trendType=" + trendType,
            url: ctxPath + "/reportViewer.do?reportBy=drillCharts",
            success: function(data) {
                $("#loading").hide();
                generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                $("#driver").val("");


            }
        })
    }
}

function getChangeHtmlDiv(chartId) {

    var divHeight = ($(window).height() - $(window).height() / 6);
    var divWidth = ($(window).width() - ($(window).width() / 1.4));
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
    parent.$("#tempDashletDiv").dialog({
        autoOpen: false,
        height: divHeight,
        width: divWidth,
        position: 'justify',
        modal: true,
        title: "Comparable Values: " //+ chartDetails["Name"]
    });

    var change = ["Change", "Change Percent"];
    var change1 = ["Change", "Change %"];
    var changeArray = ["MOM", "MOYM", "QOQ", "QOYQ", "YOY"];
    var html = "";
    for (var i in change) {

        html += "<div class='expandDivComp" + change[i].replace(/\s/g, '') + " expandDiv' name='expand' width='13%' onclick='expandDivComp(\"" + change[i].replace(/\s/g, '') + "\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";

        html += "<input type='checkbox' class='ckbox' id='L_" + change[i].replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + changeArray.length + "*," + change[i] + "' checked onclick='selectAllTimeComparisons(this.id,this.name,\"" + chartId + "\")'/>";
        html += "<span>&nbsp;" + change1[i] + "</span></label></div>";
        html += "</div>";

        html += "<div id='localFilterComp" + change[i].replace(/\s/g, '') + "' class='collapseDiv' style='display:none'><table style='' width='100%'>";
        var c = 0;
        for (var k = i * changeArray.length; k < i * changeArray.length + changeArray.length; k++) {

            html += "<tr><td ><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
            if (typeof chartData[chartId]["timeComparison"] !== "undefined" && typeof chartData[chartId]["timeComparison"][k] !== "undefined" && chartData[chartId]["timeComparison"][k][changeArray[c]] == changeArray[c]) {
                html += "<input type='checkbox' id='" + chartId + "_" + changeArray[c] + "_" + change[i].replace(" ", "", "gi") + "' checked>";
                html += "<span class='box'>&nbsp;" + changeArray[c] + "</span>";
            } else {
                html += "<input type='checkbox' id='" + chartId + "_" + changeArray[c] + "_" + change[i].replace(" ", "", "gi") + "' >";
                html += "<span class='box'>&nbsp;" + changeArray[c] + "</span>";
            }
            html += "</span></label></td></tr>";
            c++;
        }
        html += "</table></div>";
        //    }

    }
    var flag = 'true';
    html += "<table><tr><td><input type='button' value='done' onClick='getChangeComparison(\"" + chartId + "\",\"" + flag + "\")' /></td>";

    html += "</tr></table>";
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');
}


function getChangeComparison(chartId, flag) {

    var h = $("#div" + chartId).height();
    var w = $("#div" + chartId).width();
    var top = (h / 2) - 25;
    var left = (w / 2) - 25;
    $("#" + chartId).html("<div id='chart_loading' style='position:absolute;top:" + top + "px;left:" + left + "px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='" + $("#ctxpath").val() + "/images/chart_loading.gif' alt='Loading...' /></div>");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    chartDetails["changeType"] = "";
    var chartNum = chartId.replace(/[^\d.]/g, '');

    var chWidth = $("#divchart" + chartNum).width();
    var chHeight = $("#divchart" + chartNum).height();
    var records = 12;
    var filterMap = {};
    //    var filterKeys = viewBy;
    var dataCount = 0;
    var graphData = [];
    var timeCompList = [];
    var timeCompMap = {};
    var timeCompMap1 = {};
    var timeComp = "";
    var timeComp1 = "";
    var timeComparisonArray;
    var timeComparisonArrayNew = [];
    var change = ["Change", "Change Percent"];
    timeComparisonArrayNew.push("MOM", "MOYM", "QOQ", "QOYQ", "YOY");
    if (flag == "true") {
        for (var z in change) {
//            var c =0;
            for (var i = 0; i < timeComparisonArrayNew.length; i++) {
                var timeCompNew = "";
                var timeCompMapNew = {};
                if ($("#" + chartId + "_" + timeComparisonArrayNew[i] + "_" + change[z].replace(" ", "", "gi")).prop("checked")) {
                    if (change[z].replace(" ", "", "gi") == "Change")
                        chartDetails["changeType"] = "change";
                    if (change[z].replace(" ", "", "gi") == "ChangePercent")
                        chartDetails["changeType"] = "changePercent";
                    dataCount++;
                    timeCompNew = timeComparisonArrayNew[i];
                } else {
                    timeCompNew = "N";
                }
                timeCompMapNew[timeComparisonArrayNew[i]] = timeCompNew;
                timeCompList.push(timeCompMapNew);
//                c++;
            }
        }
        chartDetails["timeComparison"] = timeCompList;
    }
    else {
        if (typeof chartData[chartId]["timeComparison"] !== 'undefined') {
            var appliedTimeComparison = chartData[chartId]["timeComparison"];
            for (var l in change) {
                var c1 = 0;
                for (var k = l * timeComparisonArrayNew.length; k < l * timeComparisonArrayNew.length + timeComparisonArrayNew.length; k++) {
                    var timeCompMap1 = {};
                    var timeCompKeys = Object.keys(appliedTimeComparison[k]);
                    if (appliedTimeComparison[k][timeCompKeys[0]] == timeCompKeys[0]) {
                        dataCount++;
                        timeComp = timeComparisonArrayNew[c1];
                        //                    if(k==0){
                        //                        timeComp=timeComparison;
                        //                    }
                        //                    else{
                        //                       timeComp1=timeComparison1;
                        //                    }
                    }
                    else {
                        timeComp = "N";
                        //                    if(k==0){
                        //                        timeComp="N";
                        //                    }
                        //                    else{
                        //                        timeComp1="N";
                        //                    }
                    }
                    timeCompMap1[timeComparisonArrayNew[c1]] = timeComp;
                    timeCompList.push(timeCompMap1);
                    c1++;
                }
            }
            //            timeCompMap1[timeComparison1] = timeComp1;
            //            timeCompList.push(timeCompMap1);
            chartDetails["timeComparison"] = timeCompList;
        }
    }
    var changeEnable = "true";
    chartDetails["changeEnable"] = changeEnable;
    //        var filterGroupBy = filterKeys;

    //        var filters = filterData[filterGroupBy];

    if (flag == "true") {
        //       for (var filter in filters) {
        //            if (flag=="true" && parent.document.getElementById("L_"+filterGroupBy.replace(/\s/g, '') + "_" + filter).checked) {
        //                if (typeof filterMap[viewIds] === "undefined") {
        //                    filterMap[viewIds] = [];
        //                }
        //                filterMap[viewIds].push(parent.document.getElementById("L_"+filterGroupBy.replace(/\s/g, '') + "_" + filter).getAttribute("value"));
        //                    dataCount ++;
        //            }
        //             }
    }
    //             else if(parent.documentchartId+"_MOM"){
    //
    //             }
    else {
        //        if(typeof chartDetails["comparedValue"][viewIds]!=='undefined'){
        //                for(var i=0;i<chartDetails["comparedValue"][viewIds].length;i++){
        //                if (typeof chartDetails["comparedValue"] === "undefined" || typeof filterMap[viewIds] === "undefined") {
        //                    filterMap[viewIds] = [];
        //                }
        //
        //                filterMap[viewIds].push(chartDetails["comparedValue"][viewIds][i]);
        //                 dataCount ++;
        //            }
        //                }
    }



    var k = 0;
    var counter = 0;
    var flagArr = [0, 0, 0, 0, 0];
    var flagArr1 = [0, 0, 0, 0, 0];
    var flagArrMap = {};
    flagArrMap["Change"] = flagArr;
    flagArrMap["Change Percent"] = flagArr1;
    for (var l = 0; l < dataCount; l++) {
        //        if(l< 2){\
        var timeCheck = false;
        var list = [];
        chartDetails["selectedComparison"] = [];
        //     if(dataCount ==1){//
        if (flag == 'true') {
            for (var z in change) {
                var c = 0, m = 0;
                for (m = z * timeComparisonArrayNew.length; m < z * timeComparisonArrayNew.length + timeComparisonArrayNew.length; m++) {
//                    alert(JSON.stringify(flagArrMap)+"::::::: "+c)
                    if ($("#" + chartId + "_" + timeComparisonArrayNew[c] + "_" + change[z].replace(" ", "", "gi")).prop("checked") && flagArrMap[change[z]][c] == 0) {
                        list.push(timeCompList[m]);
                        if (change[z].replace(" ", "", "gi") == "Change")
                            chartDetails["changeType"] = "change";
                        if (change[z].replace(" ", "", "gi") == "ChangePercent")
                            chartDetails["changeType"] = "changePercent";
                        counter++;
                        flagArrMap[change[z]][c] = 1;
                        chartDetails["selectedComparison"] = list;
                        timeCheck = true;
                        break;
                    }
                    c++;
                }
                if (m != z * timeComparisonArrayNew.length + timeComparisonArrayNew.length) {
                    break;
                }
            }
            //     }else{
            //     list.push(timeCompList[l]);
            //     counter ++;
            //     }
            //     }
            var newfilterMap = {};
            var valueArray = [];
            //     alert("c: "+counter+":::"+l)
            //     if (typeof filterMap[viewIds] !== "undefined" && timeCheck!=true) {
            //     valueArray.push(filterMap[viewIds][l-counter]);
            //     newfilterMap[viewIds] = valueArray;
            //     }
        }

        else {
            appliedTimeComparison = chartData[chartId]["timeComparison"];
            for (var z in change) {
                var c = 0;
                for (var i = z * timeComparisonArrayNew.length; i < z * timeComparisonArrayNew.length + timeComparisonArrayNew.length; i++) {
                    if (appliedTimeComparison[i][Object.keys(appliedTimeComparison[i])[0]] == Object.keys(appliedTimeComparison[i])[0] && flagArrMap[change[z]][c] == 0) {
                        flagArrMap[change[z]][c] = 1;
                        list.push(timeCompList[i]);
                        if (change[z].replace(" ", "", "gi") == "Change")
                            chartDetails["changeType"] = "change";
                        if (change[z].replace(" ", "", "gi") == "ChangePercent")
                            chartDetails["changeType"] = "changePercent";
                        chartDetails["selectedComparison"] = list;
                        counter++;
                        break;
                    }
                    c++;
                }
                if (i != z * timeComparisonArrayNew.length + timeComparisonArrayNew.length) {
                    break;
                }
            }
            //            if(true){}
            //            else if(appliedTimeComparison[1][Object.keys(appliedTimeComparison[1])[0]]==Object.keys(appliedTimeComparison[1])[0] && moyFlag==0){
            //                moyFlag=1;
            //                list.push(timeCompList[1]);
            //                chartDetails["selectedComparison"] = list;
            //                counter++;
            //            }
            //            else{
            //     }else{
            //     list.push(timeCompList[l]);
            //     counter ++;
            //     }
            //     }

            var newfilterMap = {};
            var valueArray = [];
            //     alert("c: "+counter+":::"+l)
            //                if (typeof filterMap[viewIds] !== "undefined" && timeCheck!=true) {
            //                    valueArray.push(filterMap[viewIds][l-counter]);
            //                    newfilterMap[viewIds] = valueArray;
            //                }
            //            }
        }
        //    chartDetails["comparableFilters"] = newfilterMap;
        //    chartDetails["comparedValue"] = filterMap;
        chartDetails["changeEnable"] = "true";
        chartDetails["chartType"] = "LineComparisonChange";
        chartData[chartId] = chartDetails;
        var yAxisRangeMap = {};
        yAxisRangeMap["axisMin"] = "";
        yAxisRangeMap["axisMax"] = "";
        yAxisRangeMap["axisTicks"] = "";
        yAxisRangeMap["YaxisRangeType"] = "MinMax";
//    for(var i=0;i<noOfCharts;i++){
        chartData[chartId]["yaxisrange"] = yAxisRangeMap;
//    }
        $("#" + chartId).html("");
        parent.$("#chartData").val(JSON.stringify(chartData));

        //   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=comparableCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterCompare"+"&chartId="+chartId, $("#graphForm").serialize(),
        $.ajax({
            async: false,
            type: "POST",
            data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + $("#graphName").val() + "&action=localfilterCompare" + "&chartId=" + chartId,
            url: $("#ctxpath").val() + "/reportViewer.do?reportBy=comparableCharts",
            success: function(data) {

                var map = {};
                var currData = [];
                data = JSON.parse(data);
                for (var h = 0; h < (data[chartId].length < records ? data[chartId].length : records); h++) {
                    currData.push(data[chartId][h]);
                }

                map["comp" + (l + 1)] = currData;
                graphData.push(map);
            }
        });
        k = l;
        //    for(var k =0; k<dataCount;k++){
        //        alert(JSON.stringify(graphData))
    }
    var min = 0, max = 0;
    for (var i in graphData) {
        var currData = graphData[i][Object.keys(graphData[i])[0]];
        for (var j in currData) {
            if (parseFloat(currData[j][chartData[chartId]["meassures"][0]]) < min) {
                min = currData[j][chartData[chartId]["meassures"][0]];
            }
            if (parseFloat(currData[j][chartData[chartId]["meassures"][0]]) > max) {
                max = currData[j][chartData[chartId]["meassures"][0]];
            }
        }
    }
    buildLineComp(chartId, graphData[k]["comp" + (k + 1)], chartData[chartId]["viewBys"], chartData[chartId]["meassures"], chWidth, chHeight, graphData, min, max);
//    buildTableComparison(chartId, graphData[k]["comp"+(k+1)], chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,graphData,min,max);
//    buildMultiAxisBarCompare(chartId, graphData[k]["comp"+(k+1)], chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,graphData,min,max);
    //  //        }

    parent.$("#tempDashletDiv").dialog().dialog('close');
}

function changeMeasureGroup(chartId) {
    var chartData = JSON.parse(parent.$("#chartData").val());
    $("#measureChange" + chartId).html("");
    var measures = JSON.parse(parent.$("#measure").val());
    var measureIds = JSON.parse(parent.$("#measureIds").val());
    var html = "";
    for (var i in measures) {
        if (chartData[chartId]["meassures"][0] === measures[i]) {

            html += "<li><a class='' id='" + parseInt(i) + "'  style=\"cursor: pointer;background-color:#808080\" onclick='changeMeasureForGraph(this.id,\"" + chartId + "\")'>" + measures[i] + "</a></li>";

        } else {

            html += "<li><a class='' id='" + parseInt(i) + "'  style=\"cursor: pointer\" onclick='changeMeasureForGraph(this.id,\"" + chartId + "\")'>" + measures[i] + "</a></li>";
        }
    }
    $("#measureChange" + chartId).html(html);
}

function changeMeasureForGraph(measIndex, chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var measures = JSON.parse(parent.$("#measure").val());
    var measuresIds = JSON.parse(parent.$("#measureIds").val());

    var currentMeasure = measures[measIndex];
    var currentMeasureId = measuresIds[measIndex];

    var tempMeasureArray = [];
    var tempMeasureIdArray = [];
    tempMeasureArray.push(currentMeasure);
    tempMeasureIdArray.push(currentMeasureId);
    for (var i = 0; i < measuresIds.length; i++) {
        if (currentMeasureId != measuresIds[i]) {
            tempMeasureArray.push(measures[i]);
            tempMeasureIdArray.push(measuresIds[i]);
        }
    }
    var chartMeasure = chartData[chartId]["meassures"];
    var chartMeasureId = chartData[chartId]["meassureIds"];
    /*changes for measure of slider*/
    try{
    var tempdataslider = chartData[chartId]["dataSlider"];
    if(tempdataslider[chartMeasureId[0]]==='Yes'&&currentMeasureId!==chartMeasureId[0]){
        if(chartMeasureId[0]!==null){
            tempdataslider[chartMeasureId[0]]='No';
        }
        tempdataslider[currentMeasureId]='Yes';
        chartData[chartId]["dataSlider"]=tempdataslider;
    }
    }catch(err){}
    for (var z in chartMeasureId) {
        chartMeasure[0] = tempMeasureArray[0];
        chartMeasureId[0] = tempMeasureIdArray[0];
        if (z == 0) {
            continue;
        }
        if (tempMeasureArray[z] == chartMeasure[z]) {
            chartMeasure[z] = tempMeasureArray[z];
            chartMeasureId[z] = tempMeasureIdArray[z];
        }
    }
    chartData[chartId]["meassures"] = chartMeasure;
    chartData[chartId]["meassureIds"] = chartMeasureId;
    //Ashutosh
	try{
    var dataSlider=chartData[chartId]["dataSlider"];
    if(dataSlider[currentMeasureId]!=='undefined'&&dataSlider[currentMeasureId]==='Yes'){
        if(chartData[chartId]["Pattern"]==='Gradient'){
chartData[chartId]["Pattern"]='Multi';
        }
        chartData[chartId]["measureFilters"]={};
//        chartData[chartId]["sliderAxisVal"]={};
        window.flag=true;
         parent.range.axisVal={};
        parent.range.slidingVal={};
        parent.range.map={};
        parent.range.clausemap={};
    }
    }catch(e){}
    $("#chartData").val(JSON.stringify(chartData));
    $.ajax({
        async: false,
        type: "POST",
        data:
                $('#graphForm').serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(),
        success: function(data) {
            var data1 = JSON.parse(JSON.parse(data).data)[chartId];
//            alert("***chartData"+JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
            parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
            window.flag=true;
            window.measurechange=true;
            window.changedMeasureId=currentMeasureId;
//            alert("data1"+data1)
//            var data2 = JSON.parse(data["data"])
            //alert(currentMeasure+"currentMeasure: "+parseFloat(minimumValue(data1, currentMeasure)));
            if(typeof chartData[chartId]["dataSlider"]!=='undefined' && chartData[chartId]["dataSlider"]!=='undefined' && chartData[chartId]["dataSlider"]=='Yes'){
//                getSlider1(chartId,data,wid,hgt,minimumValue(data1, currentMeasure),maximumValue(data1, currentMeasure),currentMeasureId,measureArray);
            }
            generateSingleChart(data, chartId);
            window.flag=false;
        }
    });
}

function selectAllTimeComparisons(id, name, chartId) {
    var timeComparisonArray = [];
    timeComparisonArray.push("MTD", "PMTD", "PYMTD", "QTD", "PQTD", "PYQTD", "YTD", "PYTD", "MOM", "MOYM", "QOQ", "QOYQ", "YOY", "MOMPer", "MOYMPer", "QOQPer", "QOYQPer", "YOYPer");
    for (var i in timeComparisonArray) {
        if (parent.document.getElementById('selectAll').checked) {
            parent.$("#" + timeComparisonArray[i]).prop("checked", true);
        }
        else {
            parent.$("#" + timeComparisonArray[i]).prop("checked", false);
        }
    }

//    $("#driver").val("");
//    var comparisonType=id.replace("L_","","gi");
//    var allTimeComparisons=[];
//    if(comparisonType==='Change' || comparisonType==='ChangePercent'){
//        allTimeComparisons.push("MOM","MOYM","QOQ","QOYQ","YOY");
//        var change=[];
//        change.push("Change","ChangePercent");
//        for(var i in allTimeComparisons){
//            if(!$("#"+id).prop("checked")){
////                $("#chart1_"+allTimeComparisons[i]+"_"+comparisonType).prop('checked', false);
//                $("#"+chartId+"_"+allTimeComparisons[i]+"_"+comparisonType).prop('checked', false);
//            }
//            else{
//                $("#"+chartId+"_"+allTimeComparisons[i]+"_"+comparisonType).prop('checked', true);
//            }
//        }
//    }
//    else{
//        allTimeComparisons.push("MTD","PMTD","PYMTD","QTD","PQTD","PYQTD","YTD","PYTD","MOM","MOYM","QOQ","QOYQ","YOY","MOMPer ","MOYMPer","QOQPer","QOYQPer","YOYPer");
//        for(var i in allTimeComparisons){
//            if(!$("#"+id).prop("checked")){
//                $("#"+chartId+"_"+allTimeComparisons[i]).prop('checked', false);
//            }
//            else{
//                $("#"+chartId+"_"+allTimeComparisons[i]).prop('checked', true);
//            }
//        }
//    }
//    var filterKeys = Object.keys(filterData);
//    //    for (var key in filterKeys) {
//    var filterGroupBy = name.split("*,")[1].replace("L_","","gi");
//    var filters = filterData[filterGroupBy];
//    var filterMap = {};
//    var filterValues=[];
//    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
//        filterMap = JSON.parse($("#filters1").val());
//        if(typeof filterMap[id]!=="undefined"){
//            filterValues=filterMap[id];
//        }
//    }
//    for (var filter in filters) {
//        if(fromoneview!='null'&& fromoneview=='true'){
//            if(!document.getElementById(id).checked){
//                $("#"+ id+"_"+filter).prop('checked', false);
//            }
//            else{
//                $("#" +id+"_"+filter).prop('checked', true);
//            }
//        }else{
//            if(!parent.document.getElementById(id).checked){
//                $("#"+ id+"_"+filter).prop('checked', false);
//            }
//            else{
//                $("#" +id+"_"+filter).prop('checked', true);
//            }
//        }
//    }
}

function customizeQuickFilter(flag) {
    var viewBy;
    var allViewBys = JSON.parse($("#viewby").val());
    if (typeof flag === 'undefined' || flag === '') {
        viewBy = allViewBys[1];
    }
    else {
        viewBy = flag;
    }
    var REPORTID = document.getElementById("REPORTID").value;
    if (typeof REPORTID === "undefined" || REPORTID === "") {
        REPORTID = $("#graphsId").val();
    }
    var ctxPath = document.getElementById("h").value;
    var frameObj = document.getElementById("customizeQuickFilterFrame");
    $.ajax({
        async: false,
        type: "POST",
        data:
                $('#ViewByForm').serialize() + "&REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&userId=" + $("#usersId").val() + "&visualName=" + $("#currType").val() + "&filterData=" + encodeURIComponent(JSON.stringify(filterData)) + "&viewBy=" + viewBy,
        url: ctxPath + "/reportTemplateAction.do?templateParam=getSelectedDims",
        success: function(data) {
            var source = ctxPath + "/Report/Viewer/customizeQuickFilter.jsp?REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&from=isedit";
            frameObj.src = source;
        }
    });
    //    $("#defineParentChildDB").dialog('open');
    var opt = {
        autoOpen: false,
        modal: true,
        width: 550,
        height: 500,
        title: 'Customize Quick Filter'
    };
    var theDialog = $("#customizeQuickFilter").dialog(opt);
    theDialog.dialog("open");
}
function toggleFilterDisplay(viewBy, id) {
    $("#viewBy_" + viewBy).toggle(300);
    parent.$("#" + viewBy + "_downarrow").toggle();
    parent.$("#" + viewBy + "_uparrow").toggle();
//    $("#Year_2").hide();
//    var id2=parseInt(id.substr(id.indexOf('_')+1, 1));
//    var i= id.substr(0,id.indexOf('_')+1);
//    alert(i);
//    if(id2==1){
//        alert("#"+i+(id2+1))
//        $("#"+i+(id2+1)).show();
//    }
//    else{
//        alert("#"+i+(id2-1))
//        $("#"+i+(id2-1)).show();
//    }
}
function chartTypeComparison(chartId, currData, viewBys, measureArray, chWidth, chHeight, kpiResultList) {

    var chartData = JSON.parse($("#chartData").val());
    var chartType = chartData[chartId]["chartTypeComparison"];
    var radius = 600 / 3.5;
    if (chartType == "Table") {
        buildTableComparison(chartId, currData, viewBys, measureArray, chWidth, chHeight, kpiResultList);

    }
    else if (chartType == "MultiMeasure-Line") {

        buildMultiMeasureTrLine(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }
    else if (chartType == "DualAxis-Bar") {
        buildDualAxisBar(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }
    else if (chartType == "MultiMeasure-Bar") {
        buildMultiAxisBar(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }
    else if (chartType == "MultiMeasureH-Bar") {
        buildMultiAxisBar(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }
    else if (chartType == "Double-Donut") {
        buildDoubleDonut(chartId, currData, viewBys, measureArray, chWidth, chHeight, radius * 1.5);
    }
    else if (chartType == "Pie") {
        buildPie(chartId, currData, viewBys, measureArray, chWidth, chHeight, radius * 1.5);
    }
    else if (chartType == "Donut-3D") {
        buildDonut3D(chartId, currData, viewBys, measureArray, chWidth, chHeight, radius, "Donut");
    }
    else if (chartType == "Pie-3D") {
        buildDonut3D(chartId, currData, viewBys, measureArray, chWidth, chHeight, radius, "Pie");
    }
    else if (chartType == "Top-Analysis") {
        buildTopAnalysis(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }
//else if(chartType=="Combo-Analysis"){
//        buildComboAnalysis(chartId, currData, viewBys, measureArray, chWidth, chHeight,kpiResultList);
//    }
    else if (chartType === "Standard-KPI")
    {
        var map = {};
        map[chartId] = currData;
        tileCharts(chartId, map, viewBys, measureArray, chWidth, chHeight, kpiResultList);
    }
    else if (chartType === "Stacked-KPI")
    {
        var map = {};
        map[chartId] = currData;
        buildStackedKPI(chartId, map, viewBys, measureArray, chWidth, chHeight, kpiResultList);
    }
    else if (chartType === "World-Top-Analysis")
    {
        var map = {};
        map[chartId] = currData;
        buildWorldTopAnalysisChart(chartId, map, viewBys, measureArray, chWidth, chHeight);
    }
    else if (chartType === "Combo-Analysis")
    {

        if (typeof chartData[chartId]["enableComparison"] != "undefined" && chartData[chartId]["enableComparison"] != "" && chartData[chartId]["enableComparison"] == "true") {

            return currData;
        } else {
            var map = {};
            map[chartId] = currData;
            buildComboAnalysis(chartId, map, viewBys, measureArray, chWidth, chHeight, kpiResultList);
        }
    }

    else {
        buildBar(chartId, currData, viewBys, measureArray, chWidth, chHeight);
    }

}

function editRuntimeMeasure(chartId) {

    var REPORTID = parent.document.getElementById("REPORTID").value;
    if (typeof REPORTID === "undefined" || REPORTID === "") {
        REPORTID = $("#graphsId").val();
    }
    var ctxPath = parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");




    var chartData = JSON.parse($("#chartData").val());

    $("#chartData").val(JSON.stringify(chartData));
    $.post(ctxPath + '/reportViewer.do?reportBy=getObjectMap&REPORTID=' + REPORTID + '&ctxPath=' + ctxPath + '&chartId=' + chartId, $("#graphForm").serialize(),
            function(data) {

                var source = ctxPath + "/Report/Viewer/editRuntimeMeasure.jsp?REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&chartId=" + chartId + "&flag=dualAxis" + "&isComparison=true";
                frameObj.src = source;
            });
    parent.$("#editViewByDiv").dialog('open');

}
//added by krishan pratap

function saveViewByForReport(sortColumnFlag) {
    // alert("ViewByArrayId"+ViewByArrayId)
    var From = "";
//              /  if(parent.document.getElementById("Designer") != null)
//                From=parent.document.getElementById("Designer").value;

    var reportId = document.getElementById("reportId").value;
    // var ctxpath = document.getElementById("ctxPath").value;
    var ctxpath = parent.document.getElementById("h").value;

    var count = 0;
    var viewByArray = new Array();
    var rowViewByArray = new Array();
    var colViewByArray = new Array();
    var colViewNamesArr = new Array();
    if(parent.ColViewByArrayId!=null && parent.ColViewByArrayId!='' && sortColumnFlag!=null && (sortColumnFlag == true||sortColumnFlag == 'true')){
        alert("All Table filters are removed. Please update them.");
    }



    $.post(ctxpath + "/reportViewer.do?reportBy=changeViewBy&ViewByArray=" + parent.ViewByArrayId + "&RowViewByArray=" + parent.ViewByArrayId + "&ColViewByArray=" + parent.ColViewByArrayId + "&ChangeView=ChangeViewBy&reportId=" + reportId + "&ctxpath=" + ctxpath + "&rowViewNamesArr=" + parent.ViewByArrayName + "&colViewNamesArr=" + parent.ColViewByArrayName+"&sortColumnFlag="+sortColumnFlag+"&singleselection="+parent.sigleselectflag,
            function(data) {

                submitChangeViewBy();

            });

}


function submitChangeViewBy() {
    var reportId = document.getElementById("reportId").value;
    var ctxpath = parent.document.getElementById("h").value;
    parent.document.forms.frmParameter.action = ctxpath + "/reportViewer.do?reportBy=viewReport&action=ChangeViewBy&REPORTID=" + reportId
    parent.document.forms.frmParameter.submit();
    // parent.$("#editViewByDiv").dialog('close');
}


function ctbcheckboxarray(event, id, name){
      event.stopPropagation();
    var viewname1 = name.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
    var flag = 'true';
        for (var i = 0; i < parent.ColViewByArrayId.length; i++) {
            if (parent.ColViewByArrayId[i] == id) {
                flag = 'false';                
                parent.ColViewByArrayId.splice(parent.ColViewByArrayId.indexOf(id), 1);
                parent.ColViewByArrayName.splice(parent.ColViewByArrayName.indexOf(viewname1), 1);
            }
        }
        if (flag == 'true') {            
            parent.ColViewByArrayId.push(id);
            parent.ColViewByArrayName.push(viewname1);
        }
 if(parent.sigleselectflag){
           saveViewByForReport(sortColumnFlag)  
}
}
function ctbopenarray(id, name){
     var viewname1 = name.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
  
    var flag = 'true';
  
        for (var i = 0; i < parent.ColViewByArrayId.length; i++) {
            if (parent.ColViewByArrayId[i] == id) {
                flag = 'false';                
                parent.ColViewByArrayId.splice(parent.ColViewByArrayId.indexOf(id), 1);
                parent.ColViewByArrayName.splice(parent.ColViewByArrayName.indexOf(viewname1), 1);
            }
        }
        if (flag == 'true') {            
            parent.ColViewByArrayId.push(id);
            parent.ColViewByArrayName.push(viewname1);
        }
    
}
var sortColumnFlag;
function enablesnglselect(event,sortColumn,reportid,iscrosstab){
    event.stopPropagation();
    sortColumnFlag=sortColumn;
  if(!parent.sigleselectflag){
       if(iscrosstab=='viewby'){
       for (var i = 0; i < parent.ViewByArrayId.length; i++) {
           var elementid=parent.ViewByArrayId[i];
           if(elementid=='Time' || elementid=='TIME'){
          $('.enablemultiTIME').attr('checked', false);      
           }else{
          $('.enablemulti' + elementid).attr('checked', false);
           }

        }
    }else  if(iscrosstab=='ctviewby'){
          for (var i = 0; i < parent.ColViewByArrayId.length; i++) {
              var elementid=parent.ColViewByArrayId[i]
           if(elementid=='Time' || elementid=='TIME'){
          $('.enablemultiTIME').attr('checked', false);      
           }else{
          $('.enablemulti' + elementid).attr('checked', false);
           }
            }
        
    }
$("#enablemultiselect").css({"background-color": 'white'});
      parent.sigleselectflag=true
  }else{
      parent.sigleselectflag=false
      $("#enablemultiselect").css({"background-color": 'lightgray'});
  }
    $("#reportId").val(reportid)
   if(parent.sigleselectflag){
     if(iscrosstab=='viewby'){
          parent.ViewByArrayId=[];
        parent.ViewByArrayName=[];
     }else{
          if(iscrosstab=='ctviewby'){
          parent.ColViewByArrayId=[];
        parent.ColViewByArrayName=[];
          }
     }
   
         
        parent.sigleselectflag=true
     }else{
       parent.sigleselectflag=false
    }
   
}
function storearray(event, id, name) {
    event.stopPropagation();
    var ViewByArrayId1 = [];
    var viewname1 = name.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
    ViewByArrayId1 = id
    var flag = 'true';

    //alert(parent.ViewByArrayId.length)
    for (var i = 0; i < parent.ViewByArrayId.length; i++) {
        if (parent.ViewByArrayId[i] == id) {
            flag = 'false';
            // parent.ViewByArrayId.splice(id);

            parent.ViewByArrayId.splice(parent.ViewByArrayId.indexOf(id), 1);

            parent.ViewByArrayName.splice(parent.ViewByArrayName.indexOf(viewname1), 1);
        }
    }
    if (flag == 'true') {
        // parent.ViewByArrayId12.push(id);
        parent.ViewByArrayId.push(id);
        parent.ViewByArrayName.push(viewname1);

//alert("ViewByArrayId........"+parent.ViewByArrayId+"ViewByArrayName........"+parent.ViewByArrayName);
    }
     if(parent.sigleselectflag){
           saveViewByForReport(sortColumnFlag)  
}

}

function storearray1(id, name) {
    var viewname1 = name.replace("1q1", " ").replace("1q1", " ").replace("1q1", " ");
    ViewByArrayId1 = id
    var flag = 'true';
    //alert(parent.ViewByArrayId.length)
    for (var i = 0; i < parent.ViewByArrayId.length; i++) {
        if (parent.ViewByArrayId[i] == id) {
            flag = 'false';
            // parent.ViewByArrayId.splice(id);

            parent.ViewByArrayId.splice(parent.ViewByArrayId.indexOf(id), 1);

            parent.ViewByArrayName.splice(parent.ViewByArrayName.indexOf(viewname1), 1);
        }
    }
    if (flag == 'true') {
        // parent.ViewByArrayId12.push(id);
        parent.ViewByArrayId.push(id);
        parent.ViewByArrayName.push(viewname1);

//alert("ViewByArrayId........"+parent.ViewByArrayId+"ViewByArrayName........"+parent.ViewByArrayName);
    }
}
function hidedragdrop() {
//    $("#content_Drag").slideToggle();
    if ($("#content_Drag").is(":visible")) {
        $(".hideAllDiv").hide();
    } else {
        $(".hideAllDiv").hide();
        $("#content_Drag").slideToggle();
    }
}

function enableComparison(chartId, flag) {
    var chartData = JSON.parse($("#chartData").val());
    chartData[chartId]["enableComparison"] = "true";
    var chartDetails = {};
    var graphData = [];

    chartDetails = chartData[chartId];
    var chartNum = chartId.replace(/[^\d.]/g, '');
    var chWidth = $("#divchart" + chartNum).width();
    var chHeight = $("#divchart" + chartNum).height();
    var chartType1 = chartData[chartId]["chartType"];
    var timeDetailsArray = JSON.parse($("#timeDetailsArray").val());
    var timeCompArray = [];
    try {
        if (typeof timeDetailsArray !== "undefined" && timeDetailsArray[3] === "Month") {
            timeCompArray.push("MTD");
            timeCompArray.push("PYMTD");
        } else if (typeof timeDetailsArray !== "undefined" && timeDetailsArray[3] === "Qtr") {
            timeCompArray.push("QTD");
            timeCompArray.push("PYQTD");
        } else if (typeof timeDetailsArray !== "undefined" && timeDetailsArray[3] === "Year") {
            timeCompArray.push("YTD");
            timeCompArray.push("PYTD");
        } else {
            timeCompArray.push("MTD");
            timeCompArray.push("PYMTD");
        }
    } catch (e) {

    }
    chartData[chartId]["chartTypeComparison"] = chartType1;

    var k = 0;
    var dataCount = 2;
    var kpiResultList = [];
    var timeCompList = [];
    for (var m = 0; m < dataCount; m++) {
        var map = {};
        map[timeCompArray[m]] = timeCompArray[m];
        timeCompList.push(map);
    }

    chartDetails["timeComparison"] = timeCompList;

    for (var l = 0; l < dataCount; l++) {
        var timeCheck = false;
        var list = [];
        chartDetails["selectedComparison"] = [];
        //     if(dataCount ==1){
        if (flag == 'true') {
//            for(var i in timeComparisonArrayNew){
//                if($("#"+chartId+"_"+timeComparisonArrayNew[i]+"").prop("checked") && flagArr[i]==0){
//                    list.push(timeCompList[i]);
//                    counter ++;
//                    flagArr[i]=1;
//                    chartDetails["selectedComparison"] = list;
//                    timeCheck=true;
//                    break;
//                }
//            }
//            //     }else{
//            //     list.push(timeCompList[l]);
//            //     counter ++;
//            //     }
//            //     }
//            var newfilterMap = {};
//            var valueArray = [];
//            //     alert("c: "+counter+":::"+l)
//            if (typeof filterMap[viewIds] !== "undefined" && timeCheck!=true) {
//                valueArray.push(filterMap[viewIds][l-counter]);
//                newfilterMap[viewIds] = valueArray;
//            }
        }

        else {
            var map = {};
            var list = [];
            map[timeCompArray[l]] = timeCompArray[l];
            list.push(map);
            chartDetails["selectedComparison"] = list;
        }
//        chartDetails["comparableFilters"] = newfilterMap;
//        chartDetails["comparedValue"] = filterMap;
        chartDetails["isComparison"] = "EnableComparison";
        var changeEnable = "false";
        var changeType = "";
        var changeArray = [];
        var changeArr = [];
        var changePerArray = [];
        changeArray.push("MOM", "MOYM", "QOQ", "QOYQ", "YOY", "MOMPer", "MOYMPer", "QOQPer", "QOYQPer", "YOYPer");
        changePerArray.push("MOMPer", "MOYMPer", "QOQPer", "QOYQPer", "YOYPer");
        changeArr.push("MOM", "MOYM", "QOQ", "QOYQ", "YOY");
        try {
            var checkEnableValue = Object.keys(chartDetails["selectedComparison"][0]);
            for (var z in changeArray) {
                if (typeof checkEnableValue !== undefined && checkEnableValue[0] === changeArray[z]) {
                    changeEnable = "true";
                    break;
                }
            }
            for (var z in changeArr) {
                if (typeof checkEnableValue !== undefined && checkEnableValue[0] === changeArr[z]) {
                    changeType = "change";
                    break;
                }
            }
            for (var z in changePerArray) {
                if (typeof checkEnableValue !== undefined && checkEnableValue[0] === changePerArray[z]) {
                    changeType = "changePercent";
                    break;
                }
            }
        } catch (e) {
        }
        chartDetails["changeEnable"] = changeEnable;
        chartDetails["changeType"] = changeType;
        //    alert(JSON.stringify(filterMap))//
        chartData[chartId] = chartDetails;
        $("#" + chartId).html("");
        parent.$("#chartData").val(JSON.stringify(chartData));

        //   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=comparableCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterCompare"+"&chartId="+chartId, $("#graphForm").serialize(),
        $.ajax({
            async: false,
            type: "POST",
            data: $("#graphForm").serialize() + "&reportId=" + $("#graphsId").val() + "&reportName=" + $("#graphName").val() + "&action=localfilterCompare" + "&chartId=" + chartId,
            url: $("#ctxpath").val() + "/reportViewer.do?reportBy=comparableCharts",
            success: function(jsonData) {
                var data = JSON.parse(jsonData)["data"];

                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(jsonData)["meta"])["chartData"]));
                try {
                    if (typeof chartData[chartId]["chartType"] != "undefined" && chartData[chartId]["chartType"] === 'Stacked-KPI') {
                        for (var i1 in JSON.parse(JSON.parse(jsonData)["meta"])["chartData"][chartId]["GTValueList"]) {
                            kpiResultList.push(parseInt(JSON.parse(JSON.parse(jsonData)["meta"])["chartData"][chartId]["GTValueList"][i1]));
                        }
                    }
                    else {
                        kpiResultList.push(parseInt(JSON.parse(JSON.parse(jsonData)["meta"])["chartData"][chartId]["GTValueList"][0]))
                    }
                }
                catch (e) {
                }
                var map = {};
                var currData = [];
                data = JSON.parse(data);
                for (var h = 0; h < (data[chartId].length < 12 ? data[chartId].length : 12); h++) {
                    currData.push(data[chartId][h]);
                }

                map["comp" + (l + 1)] = currData;
                graphData.push(map);
            }
        });
        k = l;
        //    for(var k =0; k<dataCount;k++){
        //        alert(JSON.stringify(graphData))
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    // for searching the particular comparison value
    var labelsArr = [];
    var appliedTimeComp = [];
    var labelsArr = [];
    try {
        appliedTimeComp = chartData[chartId]["timeComparison"];
    }
    catch (e) {
    }
    for (var i in appliedTimeComp) {
        if (appliedTimeComp[i][Object.keys(appliedTimeComp[i])[0]] === Object.keys(appliedTimeComp[i])[0]) {
            labelsArr.push(Object.keys(appliedTimeComp[i])[0]);
        }
    }

    var min = 0, max = 0;
    var combineDataList = [];
    for (var i in graphData) {
        var currData = graphData[i][Object.keys(graphData[i])[0]];
        for (var j in currData) {
            var combineData = {};
            var combineMeas = [];
            var combineView = [];
            combineMeas.push(currData[j][chartData[chartId]["meassures"][0]]);
            combineView.push(currData[j][chartData[chartId]["viewBys"][0]]);
//       if(j==0)
            combineData[chartData[chartId]["viewBys"][0]] = combineView[0];
            combineData[chartData[chartId]["meassures"][0] + " " + labelsArr[i]] = combineMeas[0];
            if (i == 0)
                combineDataList.push(combineData);


            if (parseFloat(currData[j][chartData[chartId]["meassures"][0]]) < min) {
                min = currData[j][chartData[chartId]["meassures"][0]];
            }
            if (parseFloat(currData[j][chartData[chartId]["meassures"][0]]) > max) {
                max = currData[j][chartData[chartId]["meassures"][0]];
            }
            if (typeof combineDataList !== "undefined" && combineDataList !== "" && i != 0) {

                for (var k = 0; k < combineDataList.length; k++) {
                    var currView = combineDataList[k][Object.keys(combineDataList[k])[0]];
//                var currMeas = combineDataList[k][Object.keys(combineDataList[k])[0]];
                    if (currView === currData[j][chartData[chartId]["viewBys"][0]]) {
                        var measList = [];
                        measList.push(combineMeas[0]);
                        combineDataList[k][chartData[chartId]["meassures"][0] + " " + labelsArr[i]] = measList[0];
                    }
                }

            }

        }

    }
    var selectedMeasure = chartData[chartId]["meassures"][0];
    var list = [];
    for (var m in labelsArr) {
        list.push(selectedMeasure + " " + labelsArr[m]);
    }
    chartData[chartId]["runtimeMeasure"] = list;
    parent.$("#chartData").val(JSON.stringify(chartData));
    if (chartData[chartId]["chartType"] === "Combo-Analysis" && flag == "true") {

//  var comparision =   chartTypeComparison(chartId, combineDataList, chartData[chartId]["viewBys"], list,chWidth,chHeight,kpiResultList);
        return combineDataList;
    } else {
        chartTypeComparison(chartId, combineDataList, chartData[chartId]["viewBys"], list, chWidth, chHeight, kpiResultList);
    }
}




function measureDriven(id) {
    parent.$("#loading").show();
    var ctxpath = document.getElementById("ctxPath").value;
    var userId = parent.$("#userId").val();
    var chartData = JSON.parse($("#chartData").val());
    var keys = Object.keys(chartData)
    var GOmeasures = JSON.parse($("#measure").val());
    var GOmeasuresIds = JSON.parse($("#measureIds").val());
    var GOAggregation = JSON.parse($("#aggregation").val());
    var globalMeasureIds = []
    var globalAgg = []
    for (var q in GOmeasures) {
        if (GOmeasures[q] == id) {
            globalMeasureIds.push(GOmeasuresIds[q])
            globalAgg.push(GOAggregation[q])
        }
    }
    for (var i in keys) {
        var measures = chartData[keys[i]]["meassures"];
        var measureIds = chartData[keys[i]]["meassureIds"];
        var aggregation = chartData[keys[i]]["aggregation"];
        var measuresArr = []
        var measureIdsArr = []
        var aggregationArr = []
        measuresArr.push(id)
        measureIdsArr.push(globalMeasureIds[0])
        aggregationArr.push(globalAgg[0])
        for (var m in measures) {
            measuresArr.push(measures[m])
            measureIdsArr.push(measureIds[m])
            aggregationArr.push(aggregation[m])
        }
        chartData[keys[i]]["meassures"] = measuresArr
        chartData[keys[i]]["meassureIds"] = measureIdsArr
        chartData[keys[i]]["aggregation"] = aggregationArr
    }
///alert(JSON.stringify(chartData))
    $("#chartData").val(JSON.stringify(chartData));


    $.ajax({
        type: 'POST',
        data: parent.$("#graphForm").serialize(),
        // $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=buildchartsWithObject&reportId=" + $("#graphsId").val() + "&reportName=" + $("#graphName").val() + "&chartData=" + encodeURIComponent($("#chartData").val()),
//                     url: ctxpath+"/reportViewer.do?reportBy=buildCharts&reportId="+reportId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val(),
        success: function(data) {
            parent.$("#loading").hide();
            //  alert(data)
//                                 alert(grid+"   lkiyu")
            parent.generateChart(data);
        }
    }
    )


}
//function liMouseOver(id) {
//    $("#" + id).css({"background-color": '#8BC34A'});
//}

function updateTrend(chartId, index) {
    var divIndex = parseInt(chartId.replace("chart", ""));
    var h = $("#divchart" + divIndex).height();
    var w = $("#divchart" + divIndex).width();
    var top = (h / 2) - 25;
    var left = (w / 2) - 25;
    $("#chart" + divIndex).html("<div id='chart_loading' style='position:absolute;top:" + top + "px;left:" + left + "px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='" + $("#ctxpath").val() + "/images/chart_loading.gif' alt='Loading...' /></div>");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var allMeasures = chartData[chartId]["meassures"];
    var currentMeasures = [];
    if (typeof chartData[chartId]["currentMeasures"] !== 'undefined') {
        currentMeasures = chartData[chartId]["currentMeasures"];
        if (currentMeasures.indexOf(allMeasures[index]) != -1) {
            chartData[chartId]["currentMeasures"] = currentMeasures;
            parent.$("#chartData").val(JSON.stringify(chartData));
            chartTypeFunction(chartId, chartData[chartId]["chartType"]);
            return;
        }
        if (currentMeasures.length == 2) {
            currentMeasures.splice(0, 1);
        }
    }
    currentMeasures.push(allMeasures[index]);
    chartData[chartId]["currentMeasures"] = currentMeasures;
    parent.$("#chartData").val(JSON.stringify(chartData));
    chartTypeFunction(chartId, chartData[chartId]["chartType"]);
}

//added by anitha for subtotal sorting
function opensubTotalSort(elemntid, submenu, PbReportId, ctxPath)
{
    var html = "";

    html = html + "<ul id='liSubTot" + elemntid + "' class='dropdown-menu'>";
    html += "<li><a onclick=parent.subTotalSort('A_" + elemntid + "','0','N','" + PbReportId + "','" + ctxPath + "') >Sort Ascend</a></li>";
    html += "<li><a onclick=parent.subTotalSort('A_" + elemntid + "','1','N','" + PbReportId + "','" + ctxPath + "') >Sort Descend</a></li>";
    html += "</ul>";

    $("#liSubTot" + elemntid).remove();
    $("#lisubTotalSort" + elemntid).append(html);
    if (submenu == "firstmeasure") {
        $("#lisubTotalSort" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#lisubTotalSort" + elemntid + " ul").css("left", "-" + ($("#lisubTotalSort" + elemntid + " ul").width() + 1) + "px");
    }
}
//end of code by anitha for subtotal sorting

//added by anitha for subtotal topbottom sorting
function openSubTotalTopBottom(elemntid, submenu, PbReportId, ctxPath)
{
    var html = "";

    html = html + "<ul id='liSubTotTopBtm" + elemntid + "' class='dropdown-menu'>";
    html += "<li><a onclick=\"parent.subTotalTopRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','3','" + ctxPath + "')\" >Top 3</a></li>";
    html += "<li><a onclick=\"parent.subTotalBottomRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','3','" + ctxPath + "')\" >Bottom 3</a></li>";
    html += "<li><a onclick=\"parent.subTotalTopRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','5','" + ctxPath + "')\" >Top 5</a></li>";
    html += "<li><a onclick=\"parent.subTotalBottomRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','5','" + ctxPath + "')\" >Bottom 5</a></li>";
    html += "<li><a onclick=\"parent.subTotalTopRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','10','" + ctxPath + "')\" >Top 10</a></li>";
    html += "<li><a onclick=\"parent.subTotalBottomRows('A_" + elemntid + "','" + elementname + "','" + PbReportId + "','10','" + ctxPath + "')\" >Bottom 10</a></li>";
    html += "</ul>";

    $("#liSubTotTopBtm" + elemntid).remove();
    $("#liSubTotalTopBottom" + elemntid).append(html);
    if (submenu == "firstmeasure") {
        $("#liSubTotalTopBottom" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liSubTotalTopBottom" + elemntid + " ul").css("left", "-" + ($("#liSubTotalTopBottom" + elemntid + " ul").width() + 1) + "px");
    }
}
//end of code by anitha for subtotal sorting


//add by mayank sharma for grouped charts  block 2
function changeViewByGroup1(chartId) {    // for grouped chart
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartType = $("#chartType").val();
    if (chartType == "Pie-Dashboard") {
        $("#quickTabs").html("");
    } else {
        $("#viewByChange1" + chartId).html("");
    }
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewBys1 = chartData["chart1"]["dimensions"];
    var html = "";
    if (chartType !== "Pie-Dashboard") {
        for (var i = 1 in viewBys) {
            if (chartData[chartId]["viewBys"][0] != viewBys[i]) {
                if (chartData[chartId]["viewBys"][1] == viewBys[i]) {
                    html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer;background-color:#808080\" onclick='changeViewBys1(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
                } else {
                    html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer\" onclick='changeViewBys1(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
                }
            }
        }
    }
    var columns = chartData["chart1"]["viewBys"];
    var columnId = chartData["chart1"]["dimensions"];
    var columnName = [];
    for (var i = 1 in columnId) {
        columnName.push(viewBys[viewIds.indexOf(columnId[i])]);
        if (chartType == "Pie-Dashboard") {
            html += "<li><a class='' id='" + columnName[1] + ":" + columnId[1] + "' name='tab" + parseInt(i + 1) + "' style=\"cursor: pointer\" onclick='changeViewBys1(this.id,\"" + chartId + "\")'>" + columnName[1] + "</a></li>";
            if (i == 1) {
                var chartId = "chart1";
                html += "<li style='float:right'>";
                html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeMeasByGroup11(\"" + chartId + "\")' ></img>" +
                        "</div>";
                html += "</li>";
            }
        }
    }
    if (chartType == "Pie-Dashboard") {
        graphViewFlag = true;
        $("#quickTabs").html(html);
        $("#content").find("[id^='tab']").hide(); // Hide all content
        $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
        $("#content #tab1").fadeIn(); // Show first tab's content
        $('#quickTabs a').click(function(e) {
            e.preventDefault();
            if ($(this).closest("li").attr("id") == "current") { //detection for current tab
                return;
            } else {
                $("#content").find("[id^='tab']").hide(); // Hide all content
                $("#quickTabs li").attr("id", ""); //Reset id's
                $(this).parent().attr("id", "current"); // Activate this
                $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
            }
        });
    } else {
        graphViewFlag = false;
        $("#viewByChange1" + chartId).html(html);
    }
}

function changeViewBys1(val, chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var chartType = $("#chartType").val();
    var column = [];
    var columnID = [];
    var viewBy = chartData[chartId]["viewBys"];
    var viewId = chartData[chartId]["viewIds"];
    column.push(viewBy[0]);
    column.push(val.split(":")[0]);
    columnID.push(viewId[0]);
    columnID.push(val.split(":")[1]);
    if (chartType == "Pie-Dashboard") {
        for (var ch in chartData) {
            chartData[ch]["viewBys"] = column;
            chartData[ch]["viewIds"] = columnID;
        }
    } else {
        chartData[chartId]["viewBys"] = column;
        chartData[chartId]["viewIds"] = columnID;
        chartData[chartId]["dimensions"] = columnID;
    }
    var flag = 'viewByChange1';
    var divIndex = parseInt(chartId.replace("chart", ""));
    var h = $("#divchart" + divIndex).height();
    var w = $("#divchart" + divIndex).width();
    var top = (h / 2) - 25;
    var left = (w / 2) - 25;
    $("#chart" + divIndex).html("<div id='chart_loading' style='position:absolute;top:" + top + "px;left:" + left + "px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='" + $("#ctxpath").val() + "/images/chart_loading.gif' alt='Loading...' /></div>");
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&changeView=" + flag + "&viewChartId=" + chartId, $("#graphForm").serialize(),
            function(data) {
                if (chartType == "Pie-Dashboard") {
                    generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                } else {
                    generateSingleChart(data, chartId);
                }
            });
}
function changeMeasByGroup11(chartId) {
    $("#quickTabs").html("");
    graphViewFlag = false;
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measureList = chartData["chart1"]["meassures"];
    var html = "";
    for (var i = 0; i < measureList.length; i++) {
        html += '<li><a href="#" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" onclick="changeMeasureArray(this.id)">' + measureList[i] + '</a></li>';
    }
    var chartId = "chart1";
    html += "<li style='float:right'>";
    html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup1(\"" + chartId + "\")' ></img>" +
            "</div>";
    html += "</li>";
    $("#quickTabs").html(html);
    $("#content").find("[id^='tab']").hide(); // Hide all content
    $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
    $("#content #tab1").fadeIn(); // Show first tab's content
    $('#quickTabs a').click(function(e) {
        e.preventDefault();
        if ($(this).closest("li").attr("id") == "current") { //detection for current tab
            return;
        } else {
            $("#content").find("[id^='tab']").hide(); // Hide all content
            $("#quickTabs li").attr("id", ""); //Reset id's
            $(this).parent().attr("id", "current"); // Activate this
            $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
        }
    });
}

function changeGroupBarViewBy(chartId) {    //block 1 // for grouped bar
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartType = $("#chartType").val();
    if (chartType == "Pie-Dashboard") {
        $("#quickTabs").html("");
    } else {
        $("#viewByChange2" + chartId).html("");
    }
    var viewBys = JSON.parse(parent.$("#viewby").val());
    var viewIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewBys1 = chartData["chart1"]["dimensions"];
    var html = "";
    if (chartType !== "Pie-Dashboard") {
        for (var i in viewBys) {
            if (chartData[chartId]["viewBys"][1] != viewBys[i]) {
                if (chartData[chartId]["viewBys"][0] == viewBys[i]) {
                    html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointer;background-color:#808080\" onclick='changeGroupBarViewBy1(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
                } else {
                    html += "<li><a class='' id='" + viewBys[i] + ":" + viewIds[i] + "'  style=\"cursor: pointerbackground-color:#808080\" onclick='changeGroupBarViewBy1(this.id,\"" + chartId + "\")'>" + viewBys[i] + "</a></li>";
                }
            }
        }
    }
    var columns = chartData["chart1"]["viewBys"];
    var columnId = chartData["chart1"]["dimensions"];
    var columnName = [];
    for (var i in columnId) {
        columnName.push(viewBys[viewIds.indexOf(columnId[i])]);
        if (chartType == "Pie-Dashboard") {
            html += "<li><a class='' id='" + columnName[0] + ":" + columnId[0] + "' name='tab" + parseInt(i + 1) + "' style=\"cursor: pointer\" onclick='changeGroupBarViewBy1(this.id,\"" + chartId + "\")'>" + columnName[0] + "</a></li>";
            if (i == 1) {
                var chartId = "chart1";
                html += "<li style='float:right'>";
                html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeGroupBarMeasBy1(\"" + chartId + "\")' ></img>" +
                        "</div>";
                html += "</li>";
            }
        }
    }
    if (chartType == "Pie-Dashboard") {
        graphViewFlag = true;
        $("#quickTabs").html(html);
        $("#content").find("[id^='tab']").hide(); // Hide all content
        $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
        $("#content #tab1").fadeIn(); // Show first tab's content
        $('#quickTabs a').click(function(e) {
            e.preventDefault();
            if ($(this).closest("li").attr("id") == "current") { //detection for current tab
                return;
            } else {
                $("#content").find("[id^='tab']").hide(); // Hide all content
                $("#quickTabs li").attr("id", ""); //Reset id's
                $(this).parent().attr("id", "current"); // Activate this
                $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
            }
        });
    } else {
        graphViewFlag = false;
        $("#viewByChange2" + chartId).html(html);
    }
}

function changeGroupBarViewBy1(val, chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var chartType = $("#chartType").val();
    var column = [];
    var columnID = [];
    var viewBy = chartData[chartId]["viewBys"];
    var viewId = chartData[chartId]["viewIds"];

    column.push(val.split(":")[0]);
    column.push(viewBy[1]);

    columnID.push(val.split(":")[1]);
    columnID.push(viewId[1]);

    if (chartType == "Pie-Dashboard") {
        for (var ch in chartData) {
            chartData[ch]["viewBys"] = column;
            chartData[ch]["viewIds"] = columnID;
        }
    } else {
        chartData[chartId]["viewBys"] = column;
        chartData[chartId]["viewIds"] = columnID;
        chartData[chartId]["dimensions"] = columnID;
    }
    var flag = 'viewByChange1';
    var divIndex = parseInt(chartId.replace("chart", ""));
    var h = $("#divchart" + divIndex).height();
    var w = $("#divchart" + divIndex).width();
    var top = (h / 2) - 25;
    var left = (w / 2) - 25;
    $("#chart" + divIndex).html("<div id='chart_loading' style='position:absolute;top:" + top + "px;left:" + left + "px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='" + $("#ctxpath").val() + "/images/chart_loading.gif' alt='Loading...' /></div>");
    $("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val() + "&changeView=" + flag + "&viewChartId=" + chartId, $("#graphForm").serialize(),
            function(data) {
                if (chartType == "Pie-Dashboard") {
                    generateVisual(JSON.parse(data), JSON.parse(parent.$("#visualChartType").val()));
                } else {
                    generateSingleChart(data, chartId);
                }
            });
}
function changeGroupBarMeasBy1(chartId) {
    $("#quickTabs").html("");
    graphViewFlag = false;
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measureList = chartData["chart1"]["meassures"];
    var html = "";
    for (var i = 0; i < measureList.length; i++) {
        html += '<li><a href="#" id="' + parseInt(i) + '" name="tab' + parseInt(i + 1) + '" onclick="changeMeasureArray(this.id)">' + measureList[i] + '</a></li>';
    }
    var chartId = "chart1";
    html += "<li style='float:right'>";
    html += "<div class='dropdown' style='cursor:pointer;z-index:11;'><img title = 'Change ViewBy's' style='margin-left:20%;width:24px;height:24px' src='images/changeView.png' alt='Options' onclick='changeGroupBarViewBy(\"" + chartId + "\")' ></img>" +
            "</div>";
    html += "</li>";
    $("#quickTabs").html(html);
    $("#content").find("[id^='tab']").hide(); // Hide all content
    $("#quickTabs li:first").attr("id", "current"); // Activate the first tab
    $("#content #tab1").fadeIn(); // Show first tab's content
    $('#quickTabs a').click(function(e) {
        e.preventDefault();
        if ($(this).closest("li").attr("id") == "current") { //detection for current tab
            return;
        } else {
            $("#content").find("[id^='tab']").hide(); // Hide all content
            $("#quickTabs li").attr("id", ""); //Reset id's
            $(this).parent().attr("id", "current"); // Activate this
            $('#' + $(this).attr('name')).fadeIn(); // Show content for the current tab
        }
    });
} //end by mayank sharma for grouped charts


function checkLogical(id) {
    if ($("#" + id).val() == 'Multi' || $("#" + id).val() == 'Gradient' || $("#" + id).val() == 'single') {
        $("#DefinelogicalDiv").hide();
    } else {
        $("#DefinelogicalDiv").show();
    }
}
function checkCoustomTimeDiv(id) {
    if ($("#" + id).val() == 'No') {
        $("#checkCoustomTime").hide();
    } else {
        $("#checkCoustomTime").show();
    }
}
function customDateApply(id) {
$( "#"+id ).datepicker({
        changeMonth: true,
        changeYear: true
    });
}
function condition(id, chartId) {
    if ($("#" + id).val() == '<' || $("#" + id).val() == '>' || $("#" + id).val() == '>=' || $("#" + id).val() == '<=' || $("#" + id).val() == '=') {
        $("#" + chartId + "HighMin").hide();
    } else {
        $("#" + chartId + "HighMin").show();
    }
}
function condition1(id, chartId) {
    if ($("#" + id).val() == '<' || $("#" + id).val() == '>' || $("#" + id).val() == '>=' || $("#" + id).val() == '<=' || $("#" + id).val() == '=') {
        $("#" + chartId + "MidMin").hide();
    } else {
        $("#" + chartId + "MidMin").show();
    }
}
function condition2(id, chartId) {
    if ($("#" + id).val() == '<' || $("#" + id).val() == '>' || $("#" + id).val() == '>=' || $("#" + id).val() == '<=' || $("#" + id).val() == '=') {
        $("#" + chartId + "LowMin").hide();
    } else {
        $("#" + chartId + "LowMin").show();
    }
}
function getConditionalColor2(div, chartData, drillShade, data, columns, measureArray, i, color) {
    var chartData = JSON.parse($("#chartData").val());
    if (chartData[div]["chartType"] === "India-Map" || chartData[div]["chartType"] === "US-City-Map" || chartData[div]["chartType"] === "US-State-Map" || chartData[div]["chartType"]==="Grouped-Map"
            || chartData[div]["chartType"] === "Australia-State-Map" || chartData[div]["chartType"] === "Australia-City-Map" || chartData[div]["chartType"] === "Telangana" || chartData[div]["chartType"] === "Andhra-Pradesh") {
        var chartmapData = data[div];
        var value = parseFloat(chartmapData[i][measureArray[0]]);
    } else {
        var value = parseFloat(data[i][measureArray[0]]);
    }

    if (typeof chartData[div]["logicalParameters"] != "undefined" && chartData[div]["logicalParameters"][0]["condition"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][0]["highMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    } else if (chartData[div]["logicalParameters"][0]["condition"] === ">" && value > parseFloat(chartData[div]["logicalParameters"][0]["HighMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    } else if (chartData[div]["logicalParameters"][0]["condition"] === "<=" && value <= parseFloat(chartData[div]["logicalParameters"][0]["HighMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    } else if (chartData[div]["logicalParameters"][0]["condition"] === ">=" && value >= parseFloat(chartData[div]["logicalParameters"][0]["HighMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    } else if (chartData[div]["logicalParameters"][0]["condition"] === "=" && value == parseFloat(chartData[div]["logicalParameters"][0]["HighMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    } else if (chartData[div]["logicalParameters"][0]["condition"] === "<" && value < parseFloat(chartData[div]["logicalParameters"][0]["HighMax"])) {
        return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
    }
//        else if ( chartData[div]["logicalParameters"][0]["condition"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][0]["HighMin"])) { 
//            return chartData[div]["logicalParameters"][0]["logicalHighcolor"];
//        }

    else if (chartData[div]["logicalParameters"][1]["condition1"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    } else if (chartData[div]["logicalParameters"][1]["condition1"] === ">" && value > parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    } else if (chartData[div]["logicalParameters"][1]["condition1"] === "<=" && value <= parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    } else if (chartData[div]["logicalParameters"][1]["condition1"] === ">=" && value >= parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    } else if (chartData[div]["logicalParameters"][1]["condition1"] === "=" && value == parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    } else if (chartData[div]["logicalParameters"][1]["condition1"] === "<" && value < parseFloat(chartData[div]["logicalParameters"][1]["MidMax"])) {
        return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
    }
//else if ( chartData[div]["logicalParameters"][1]["condition1"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][1]["MidMin"])) {
//            return chartData[div]["logicalParameters"][1]["logicalMidcolor"];
//        } 
    else if (chartData[div]["logicalParameters"][2]["condition2"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    } else if (chartData[div]["logicalParameters"][2]["condition2"] === ">" && value > parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    } else if (chartData[div]["logicalParameters"][2]["condition2"] === "<=" && value <= parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    } else if (chartData[div]["logicalParameters"][2]["condition2"] === ">=" && value >= parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    } else if (chartData[div]["logicalParameters"][2]["condition2"] === "=" && value == parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    } else if (chartData[div]["logicalParameters"][2]["condition2"] === "<" && value < parseFloat(chartData[div]["logicalParameters"][2]["LowMax"])) {
        return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
    }
//        else if (chartData[div]["logicalParameters"][2]["condition2"] === "<>" && value > parseFloat(chartData[div]["logicalParameters"][2]["LowMin"])) { 
//            return chartData[div]["logicalParameters"][2]["logicalLowcolor"];
//        } 
    return "#dadada";
}
//method added by anitha for MTD,QTD,YTD on measures in AO report
function openDateJoin(elemntid, submenu,PbReportId)
{
    var html = "";
    html = html + "<ul style='height:130px;width:100px;' id='liDateJn" + elemntid + "' class='dropdown-menu'>";
    html += "<li>";
    if(timeinfo!=null &&timeinfo=="Month"){
        html += "<a style='position:relative;'> ";
    }else{
        html += "<a style='position:relative;' onmouseover=\"compareSpan('"+elemntid+"')\" onclick=\"parent.addRunTimeDateJoin('Monthjoin','_MTD','(MTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\"> ";
    }    
    html += " MTD <i style='right: 15px; position: absolute; top: 8px;' class='fa fa-caret-right' onmouseover=\"monthlyTDSpan('"+elemntid+"','monthly')\"></i></a>"; 
    html +="<ul style='display:none;top:0px;' id='liDateJnM"+elemntid+"' class='dropdown-menu'>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PMonthjoin','_PMTD','(PMTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PMTD  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PMonthYjoin','_PYMTD','(PYMTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PYMTD  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('MonthOmonth','_MOM','(MOM)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  MOM  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('MonthOYmonth','_MOYM','(MOYM)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  MOYM  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('MonthOmonthper','_MOMPer','(MOM%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  MOM%  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('MonthOYmonthper','_MOYMPer','(MOYM%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  MOYM%  </a></li>";    
    html += "</ul>";
    html += "</li>";
    html += "<li>";
    if(timeinfo!=null &&timeinfo=="Week"){
        html += "<a style='position:relative;'> ";
    }else{
        html += "<a style='position:relative;' onmouseover=\"compareSpan('"+elemntid+"')\" onclick=\"parent.addRunTimeDateJoin('Weekjoin','_WTD','(WTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\"> ";
    }    
    html += " WTD <i style='right: 15px; position: absolute; top: 8px;' class='fa fa-caret-right' onmouseover=\"monthlyTDSpan('"+elemntid+"','weekly')\"></i></a>"; 
    html +="<ul style='display:none;top:0px;' id='liDateJnW"+elemntid+"' class='dropdown-menu'>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PWeekjoin','_PWTD','(PWTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PWTD  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PWeekYjoin','_PYWTD','(PYWTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PYWTD  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('WeekOweek','_WOW','(WOW)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  WOW  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('WeekOYweek','_WOYW','(WOYW)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  WOYW  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('WeekOweekper','_WOWPer','(WOW%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  WOW%  </a></li>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('WeekOYweekper','_WOYWPer','(WOYW%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  WOYW%  </a></li>";    
    html += "</ul>";
    html += "</li>";
    html += "<li >";
    if(timeinfo!=null &&timeinfo=="Qtr"){
        html += "<a style='position:relative;'> ";
    }else{
        html += "<a style='position:relative;'  onmouseover=\"compareSpan('"+elemntid+"')\" onclick=\"parent.addRunTimeDateJoin('Qtrjoin','_QTD','(QTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">";
    }
    html += " QTD  <i style='right: 15px; position: absolute; top: 8px;' class='fa fa-caret-right' onmouseover=\"monthlyTDSpan('"+elemntid+"','quarterly')\"></i></a>";
    html +="<ul style='display:none;top:0px;' id='liDateJnQ"+elemntid+"' class='dropdown-menu'>";    
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PQtrjoin','_PQTD','(PQTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PQTD  </a></li>";
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PQtrYjoin','_PYQTD','(PYQTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PYQTD  </a></li>";
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('Qtroqtr','_QOQ','(QOQ)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  QOQ  </a></li>";
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('QtroQqtr','_QOYQ','(QOYQ)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  QOYQ  </a></li>";        
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('Qtroqtrper','_QOQPer','(QOQ%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  QOQ%  </a></li>";
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('QtroQqtrper','_QOYQPer','(QOYQ%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  QOYQ%  </a></li>";
    html += "</ul>";
    html += "</li>";
    html += "<li >";
    if(timeinfo!=null &&timeinfo=="Year"){
        html += "<a style='position:relative;'> ";
    }else{
        html += "<a style='position:relative;'  onmouseover=\"compareSpan('"+elemntid+"')\" onclick=\"parent.addRunTimeDateJoin('Yearjoin','_YTD','(YTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">";
    }
    html += " YTD  <i style='right: 15px; position: absolute; top: 8px;' class='fa fa-caret-right' onmouseover=\"monthlyTDSpan('"+elemntid+"','yearly')\"></i></a>";    
    html +="<ul style='display:none;top:0px;' id='liDateJnY"+elemntid+"' class='dropdown-menu'>"; 
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('PYearjoin','_PYTD','(PYTD)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  PYTD  </a></li>";            
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('Yearoyear','_YOY','(YOY)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  YOY  </a></li>";        
    html += "<li ><a onclick=\"parent.addRunTimeDateJoin('Yearoyearper','_YOYPer','(YOY%)','A_" + elemntid + "','" + elementname + "','" + ReportId + "','" + ctxPath1 + "')\">  YOY%  </a></li>";        
    html += "</ul>";
    html += "</li>";        
    html += "<li ><a onmouseover=\"displayColsForComparision('"+elemntid+"')\" >Compare With</a><ul style='display:none;bottom:0px;top:auto;' id='liCompareWith"+elemntid+"' class='dropdown-menu'>";
    var DisplayColumnsList = "";
    var DisplayLabelsList = "";
    if(DisplayColumns!=null && DisplayColumns!=undefined&&DisplayColumns!=""){
        DisplayColumnsList = DisplayColumns.toString().split(",");
        DisplayLabelsList = DisplayLabels.toString().split(",");
        html+= "<li><a onclick=\"getComparisionWith('','"+elemntid+"','"+PbReportId+"')\">Reset Comparision</a></li>";
        for(var i=0;i<DisplayColumnsList.length;i++){
            if(DisplayColumnsList[i].indexOf(elemntid+"_")!=-1&&!(DisplayColumnsList[i].indexOf("Rank")!=-1||DisplayColumnsList[i].indexOf("rank")!=-1)){
               html += "<li><a onclick=\"getComparisionWith('"+DisplayColumnsList[i]+"','"+elemntid+"','"+PbReportId+"')\">"+DisplayLabelsList[i]+"</a></li>"; 
            }
        }
    }            
    html += "</ul></li>";
    html += "</ul>";
    $("#liDateJn" + elemntid).remove();
    $("#liDateJoin" + elemntid).append(html);

    if (submenu == "firstmeasure") {
        $("#liDateJoin" + elemntid + " ul").css("left", "100%");
    }
    else {
        $("#liDateJoin" + elemntid + " ul").css("left", "-" + ($("#liDateJoin" + elemntid + " ul").width() + 1) + "px");
    }


}
//end of method by anitha for MTD,QTD,YTD on measures in AO report

        function selectDate(id){
                     
                    var selectedmonth = $("#select2-selFrmDatMon-container").text();
                     var selectedyear = $("#select2-selFrmDatYr-container").text();
                    var x = $("#select2-selFrmDatMon-container").title;
                    var size=31;
                   if(selectedmonth=='December'||selectedmonth=='October'||selectedmonth=='August'||selectedmonth=='July'||selectedmonth=='May'||selectedmonth=='March'||selectedmonth=='January')
                       {
                           size=31;
                            $("#select2-selFrmDatToDate-container").text("1");
                       }
else if(selectedmonth=='April'||selectedmonth=='June'||selectedmonth=='September'||selectedmonth=='November')
                           {
                              size=30;
                               $("#select2-selFrmDatToDate-container").text("1");
                           }
                   else if(selectedmonth='February')   
                        {   var isLeap = new Date(selectedyear1, 1, 29).getMonth() == 1
                        
                            if(isLeap)
                          {
                               size=29;
                                $("#select2-selFrmDatToDate-container").text("1");
                          }
                          else
                              {
                                    size=28;
                                     $("#select2-selFrmDatToDate-container").text("1");
                              }
                        
                        }
                        else
                            {
                                
                            }
                         var htmlVardatef = "";
                
                  
                  
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='selFrmDatToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                    
 $("#"+id).html("");var date="01"
                  $("#"+id).append(htmlVardatef);
                   parent.$("#selFrmDatToDate" +date).prop("selected", "true");
                   
                 }
                  function selectDateToDatMon(id){
                     
                   var selectedmonth1 = $("#select2-selToDatMon-container").text(); 
                 var selectedyear1 = $("#select2-selToDatYr-container").text();
                    var x = $("#select2-selFrmDatMon-container").title;
                    var size=31;
                   if(selectedmonth1=='December'||selectedmonth1=='October'||selectedmonth1=='August'||selectedmonth1=='July'||selectedmonth1=='May'||selectedmonth1=='March'||selectedmonth1=='January')
                       {
                           size=31;
                            $("#select2-selToDatToDate-container").text("31");
                       }
                     else if(selectedmonth1=='April'||selectedmonth1=='June'||selectedmonth1=='September'||selectedmonth1=='November')
                           {
                              size=30;
                               $("#select2-selToDatToDate-container").text("30");
                           }
                   else if(selectedmonth1='February')   
                        {   var isLeap = new Date(selectedyear1, 1, 29).getMonth() == 1
                        
                            if(isLeap)
                          {
                               size=29;
                                $("#select2-selToDatToDate-container").text("29");
                          }
                          else
                              {
                                    size=28;
                                     $("#select2-selToDatToDate-container").text("28");
                              }
                        
                        }
                        else
                            {
                                
                            }
                         var htmlVardatef = "";
                
                  
                  
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='selToDatToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                    
 $("#"+id).html("");
                  $("#"+id).append(htmlVardatef);
                   parent.$("#selToDatToDate" +size).prop("selected", "true");
                   
                 }
                 
                function selectDateFrmComMon(id){
                     
                   var selectedmonth1 = $("#select2-selFrmComMon-container").text(); 
                 var selectedyear1 = $("#select2-selFrmComYr-container").text();
                    var x = $("#select2-selFrmDatMon-container").title;
                    var size=31;
                   if(selectedmonth1=='December'||selectedmonth1=='October'||selectedmonth1=='August'||selectedmonth1=='July'||selectedmonth1=='May'||selectedmonth1=='March'||selectedmonth1=='January')
                       {
                           size=31;
                            $("#select2-comparefromDateToDate-container").text("1");
                       }
                     else if(selectedmonth1=='April'||selectedmonth1=='June'||selectedmonth1=='September'||selectedmonth1=='November')
                           {
                              size=30;
                               $("#select2-comparefromDateToDate-container").text("1");
                           }
                   else if(selectedmonth1='February')   
                        {   var isLeap = new Date(selectedyear1, 1, 29).getMonth() == 1
                        
                            if(isLeap)
                          {
                               size=29;
                                $("#select2-comparefromDateToDate-container").text("1");
                          }
                          else
                              {
                                    size=28;
                                     $("#select2-comparefromDateToDate-container").text("1");
                              }
                        
                        }
                        else
                            {
                                
                            }
                         var htmlVardatef = "";
                
                  
                  
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='comparefromDateToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                    
 $("#"+id).html("");
                  $("#"+id).append(htmlVardatef);var date3="01"
                   parent.$("#comparefromDateToDate" +date3).prop("selected", "true");
                   
                 }
                 function selectDateToComMon(id){
                     
                   var selectedmonth1 = $("#select2-selToComMon-container").text(); 
                 var selectedyear1 = $("#select2-selToComYr-container").text();
                    var x = $("#select2-selFrmDatMon-container").title;
                    var size=31;
                   if(selectedmonth1=='December'||selectedmonth1=='October'||selectedmonth1=='August'||selectedmonth1=='July'||selectedmonth1=='May'||selectedmonth1=='March'||selectedmonth1=='January')
                       {
                           size=31;
                           $("#select2-comparetoDateToDate-container").text("31");
                       }
                     else if(selectedmonth1=='April'||selectedmonth1=='June'||selectedmonth1=='September'||selectedmonth1=='November')
                           {
                              size=30;
                              $("#select2-comparetoDateToDate-container").text("30");
                           }
                   else if(selectedmonth1='February')   
                        {   var isLeap = new Date(selectedyear1, 1, 29).getMonth() == 1
                        
                            if(isLeap)
                          {
                               size=29;
                               $("#select2-comparetoDateToDate-container").text("29");
                          }
                          else
                              {
                                    size=28;
                                    $("#select2-comparetoDateToDate-container").text("28");
                              }
                        
                        }
                        else
                            {
                                
                            }
                         var htmlVardatef = "";
                
                  
                  
                    for(var i=1;i<=size;i++){
                         htmlVardatef += "<option id='comparetoDateToDate"+i+"' value='"+i+"'>"+i+"</option>";
                    }
                    
 $("#"+id).html("");
                  $("#"+id).append(htmlVardatef);
                   parent.$("#comparetoDateToDate" +size).prop("selected", "true");
                 }
//added by anitha
function monthlyTDSpan(elemntid,flag){ 
    if(flag=='monthly'){
   $("#liDateJnM"+elemntid).css('display', 'block');
   $("#liDateJnQ"+elemntid).css('display', 'none');
   $("#liDateJnY"+elemntid).css('display', 'none');
        $("#liDateJnW"+elemntid).css('display', 'none');
   $("#liCompareWith"+elemntid).css('display', 'none');
    }else if(flag=='quarterly'){
       $("#liDateJnQ"+elemntid).css('display', 'block');
       $("#liDateJnM"+elemntid).css('display', 'none');
       $("#liDateJnY"+elemntid).css('display', 'none');
        $("#liDateJnW"+elemntid).css('display', 'none');
       $("#liCompareWith"+elemntid).css('display', 'none');
    }else if(flag=='yearly'){
       $("#liDateJnY"+elemntid).css('display', 'block');
       $("#liDateJnM"+elemntid).css('display', 'none');
       $("#liDateJnQ"+elemntid).css('display', 'none');
        $("#liDateJnW"+elemntid).css('display', 'none');
       $("#liCompareWith"+elemntid).css('display', 'none');
    }else if(flag=='weekly'){
        $("#liDateJnW"+elemntid).css('display', 'block');
        $("#liDateJnM"+elemntid).css('display', 'none');
        $("#liDateJnQ"+elemntid).css('display', 'none');
        $("#liDateJnY"+elemntid).css('display', 'none');
        $("#liCompareWith"+elemntid).css('display', 'none');
   }
}
//end of method by anitha

//added by anitha
function displayColsForComparision(elemntid){    
    $("#liCompareWith"+elemntid).css('display', 'block');
    $("#liDateJnQ"+elemntid).css('display', 'none');
       $("#liDateJnM"+elemntid).css('display', 'none');
       $("#liDateJnY"+elemntid).css('display', 'none');
}
function compareSpan(elementid){
    $("#liCompareWith"+elementid).css('display', 'none');
}
function getComparisionWith(elementid,baselemenetid,PbReportId){
    $.ajax({
                        type: 'POST',                        
                        url: ctxPath1 + "/reportViewerAction.do?reportBy=getComparisionWithRTMesaure&reportId=" + PbReportId + "&elementid="+elementid+"&baselemenetid="+baselemenetid,                        
                        success: function(data) { 
                            parent.document.forms.frmParameter.action = ctxPath1 + "/reportViewer.do?reportBy=viewReport&action=measChange&REPORTID=" + PbReportId
                            parent.document.forms.frmParameter.submit();
                        }
                    });
}
//end of code by anitha
