 var globalFilter; 
 var newProp="";
 var timeMapValue = {};
var enableAdvanceDrill = false;
var breadcrumpid=[];
var startValgbl=1;
 var checkall=true;
var tid;
var did;
var query;
var gSelectedIndex = -1;
var preservedvalue="";
var selectedItem="";
/* key code constants */
var ENTER = 13;
var KEYUP = 38;
var KEYDOWN = 40;
var BACKSPACE = 8;
var TAB=9;
var xmlHttp;
var ctxPath;
var imageFlag=0;
var imageFlag1=0;
var tbz=0;
var imageId="";
var ajaxArr=new Array();
var suggestArr=new Array();
var fieldValArray=new Array();
var suggestArrLength=0;
var selValAjax=0;
    var paramValSel=false;
var tabCode=0;
var scrollPayLoad="";
var scrollURL=""
var onScrollFlag=0;
var scHeight=0;
var scTop=0;
var clHgt=0;
startVal=1;
endVal=0;
var scrollFlag=true
var prevCount=7;
var keyPress=true;
var checkId="";
var allParamIds="";
var orderVal="";
var excludeBox = false;
var testArray=new Array();
var operator="IN"
var graphTrendFlag = "";
var breadCrump = "";
var grid;
var draggableViewBys = [];
var gridtrend;
var isInit={};
var selectedColor='';
//var prevChartType='';
window.complexId="chart1";
var pbreportid1='';
var pbctxpath1='';
var pbpagesize1='';
var currentPage='default';
var selctedcolorcode=new Array();
var selectedmeasurids=new Array();
var pageDrillValue='';
var pageDrillViewById='';
window.measure_seq=0;
window.sliderCount=0;
window.measureCount=0;
window.sliderDrag=false;
var range={};
var comboTopChartType={};
range.count=0;
range.axisVal="";
range.axisMap={};
range.min=[];
range.max=[];
range.slidingVal={};
range.slidingMaxVal=[];
range.measureIndex=[];
range.map={};
range.clausemap={};
range.sliderAxisVal={};
var chartGroups=["Bar Graphs","Circular Graphs","KPI Graphs","Variance Graphs","Line Graphs","Grouped Graphs","Map Graphs","Combo Maps","Area Graphs","Table Graphs","Bubble Graphs", "Other Graphs"];
var chartG=["barGraphs","circularPieGraphs","kpiGraphs","VarianceGraphs","lineGraphs","groupedGraphs","mapGraphs","indiaStateMap","areaGraphs","tableGraphs","bubbleGraphs","otherGraphs"];
var barGraphs=[
"Vertical-Bar",
//"Vertical-Negative-Bar",
"Horizontal-Bar",
"Filled-Horizontal",
"StackedBar",
"StackedBar-%",
"StackedBarH-%",
"Horizontal-StackedBar",
"OverLaid-Bar-Line",
"MultiMeasure-Bar",
"MultiMeasureH-Bar",
"StackedBarLine",
"Cumulative-Bar",
"Multi-Layered-Bar",
"Combo-MeasureH-Bar"
];
var circularPieGraphs=[
"Pie",
"Pie-3D",
"Half-Pie",
"Donut",
"Half-Donut",
"Donut-3D",
"Double-Donut",
//"Aster",
"Column-Pie",
"Column-Donut"
];

var kpiGraphs=[
"Standard-KPI",   // // edit by mayank sharma for kpi charts clean up
"Stacked-KPI",
"Radial-Chart",
"LiquidFilled-KPI",
"Dial-Gauge",
"Dial-Gauge1",
"KPI-Bar",
"Trend-KPI",
"KPI-Dashboard",
"XYZ-Dashboard",
//"KPIDash",
//"KPI-Table",
//"Expression-Table",
"Emoji-Chart",
"Bullet-Horizontal"
];

var VarianceGraphs=[
"Veraction-Combo1",
"Influencers-Impact-Analysis",
"Influencers-Impact-Analysis2",
"Veraction-Combo3",
//"Model-Varince-Dashboard",
"Transportation-Spend-Variance",
"Variance-By-Mode",
"Analysis",
"Score-vs-Targets"
];

var lineGraphs=[
"Line",
"SmoothLine",
"MultiMeasure-Line",
"MultiMeasureSmooth-Line",
"Cumulative-Line",
"Trend-Analysis"
];

var groupedGraphs=[
"Grouped-Bar",
"GroupedHorizontal-Bar",
"GroupedStacked-Bar",
"Scatter-Analysis",
"GroupedStackedH-Bar",
"GroupedStacked-Bar%",
"GroupedStackedH-Bar%",
"Grouped-Line",
"Grouped-MultiMeasureBar",
"Scatter-Bubble",
"Multi-View-Tree"
];
var mapGraphs=[
"India-Map",
"IndiaCity-Map",
"US-State-Map",
"US-City-Map",
"Australia-State-Map",
"Australia-City-Map",
"world-map",
"World-City",
"Andhra-Pradesh",
"Telangana"
];
var indiaStateMap=[
"Combo-India-Map",
"Combo-IndiaCity",
"Combo-US-Map",
"Combo-USCity-Map",
"Combo-Aus-State",
"Combo-Aus-City",
"Grouped-Map"
];
var areaGraphs=[
"Area",
"MultiMeasure-Area",
"Combo-Horizontal",
"Combo-TopKPI"
];
var tableGraphs=[
"Table",
"Transpose-Table",
"Bar-Table",
"Horizontal-Bar-Table",
"Grouped-Table",
"Cross-Table"
];
var bubbleGraphs=[
"Bubble",
"Split-Bubble",
"OverLaid-Bar-Bubble",
"Centric-Bubble",
"Multi-View-Bubble",
"Horizontal-Bubble",
"Tangent"
];
var otherGraphs=[
"DualAxis-Bar",
"Word-Cloud",
"Scatter-XY",
"TopBottom-Chart",
"Tree-Map",
"Top-Analysis",
"Combo-Analysis",
"World-Top-Analysis",
"Trend-Combo",
"Trend-Table-Combo",
"Frequency-Distribution-Chart",
"Trend-Analysis-Chart"
];


var graphsMap = {
    "Vertical Bar": "single",
    "Table":"single",
    "Transpose Table":"single",
    "Horizontal Bar": "single",
    //    "WaterfaCumulativell": "single",
    "Pie": "single",
    "Pie 3D": "single",
    "Donut": "single",
    "Double Donut": "single",
    "Donut 3D": "single",
    "StackedBar":"single",
    "Horizontal StackedBar":"single",
    "Half Donut":"single",
    "Dial Gauge":"single",
    "DualAxis Bar":"single",
    "TopBottom-Chart":"single",
    "Filled-Horizontal":"single",
    "Grouped Line":"double"

};

var graphsMapNext = {
    "Half Pie":"single",
    "Bubble": "single",
    "Line": "single",
    "SmoothLine": "single",
    "MultiMeasure Line":"single",
    "Radial-Chart":"single",
//    "Bullet-Horizontal":"single",
    "LiquidFilled-KPI":"single",
    "Standard-KPI":"single",
//    "KPIDash":"single",
//    "Emoji-Chart":"single",
    "OverLaid-Bar-Line": "single",
    "Word Cloud": "single",
    "MultiMeasure Bar" : "single",
    "Grouped Bar" : "double",
    "Split-Bubble": "double",
    "Area" : "single",
    "MultiMeasure Area" : "single",
    "GroupedStacked Bar" : "double"

};

var graphsName = {
    "Vertical-Bar": "single",
//    "Vertical-Negative-Bar": "single",
    "Table":"double",
    "Combo-TopKPI":"single",
    "Transpose-Table":"single",
    "Dial-Gauge":"single",
    "Dial-Gauge1":"single",
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
var actionChartsType=[
    "Vertical-Bar","Horizontal-Bar","Filled-Horizontal","StackedBar","Pie","Donut","Line","SmoothLine","MultiMeasure-Bar","Double-Donut","MultiMeasure-Line","Bubble","DualAxis-Bar"
];
$("#AttributesDialog").dialog({
    autoOpen: false,
    height: 500,
    width: 1100,
    position: 'justify',
    modal: true,
    resizable:false
}); 
window.onload = function(){
    $("#AttributesDialog").dialog({
        autoOpen: false,
        height: 500,
        width: 1100,
        position: 'justify',
        modal: true,
        resizable:false
    });
//document.getElementById("suggestList").style.display = "none";
//document.getElementById("suggestList1").style.display = "none";
//document.requestForm.country1.focus();
/*document.getElementById(tid).onkeyup = function(e){checkKey(e, this);};
  //document.onclick = checkClick;*/

/* kill default submit of a single field form */
//document.myForm.onsubmit = function(){return false;};
};

function sample()
{
    document.getElementById("suggestList").style.display = "none";
    document.getElementById("suggestList1").style.display = "none";
    //myForm.puLoginId.focus();
    document.myForm.onsubmit = function(){
        return false;
    };
}
document.onkeydown = function(){

    if(window.event && window.event.keyCode == 116)
    { // Capture and remap F5
        window.event.keyCode = 505;
    }

    if(window.event && window.event.keyCode == 505)
    { // New action for F5
        return false;
    // Must return false or the browser will refresh anyway
    }
}


function selId(id1,id2,id3,id4)
{

    tid=id1;
    did=id2;
    query=id3;
    stid=id4;
    //    var filterVal=eval('('+document.getElementById(tid).value+')');
    getAllImageNames();
    if(imageId=="")
        imageId=id1+"-plus";

    if(tid=='cboShade')
    {
        preservedvalue="";
    }

    document.getElementById(did).style.display = "none";
    document.getElementById(tid).onkeyup = function(e)
    {
        if(document.getElementById(tid).value.indexOf('ALL')<0)
        {
            checkKey(e, this);
        }

    };
    document.onclick = checkClick;
    hideAll();
}
function disableonkeyselId(id1,id2,id3,id4)
{
    tid=id1;
    did=id2;
    query=id3;
    stid=id4;
    getAllImageNames();
    if(imageId=="")
        imageId=id1+"-plus";

    if(tid=='cboShade')
    {
        preservedvalue="";
    }

    document.getElementById(did).style.display = "none";
    //    document.getElementById(tid).onkeyup = function(e)
    //    {
    //
    //        if(document.getElementById(tid).value.indexOf('ALL')<0)
    //        {
    //            checkKey(e, this);
    //        }
    //
    //    };
    document.onclick = checkClick;
    hideAll();
}

/*function sendRequest(url, payload)
{
    scrollURL=url;
    var parArr=allParamIds.split(",");
    var parArrVals=new Array();
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
    }
    var passVales1 = "";
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
        if(i==0){
            passVales1 =document.getElementById(parArr[i]).value;
        }
        else{
            passVales1 +=";"+document.getElementById(parArr[i]).value;
        }
    }

    var mainUrl=url+"?"+payload+"&allParamIds="+allParamIds+"&parArrVals="+(passVales1)+"&REPORTID="+document.getElementById("REPORTID").value;

    $.ajax({
        url: mainUrl,
        success: function(data){
            document.getElementById('light').style.display='none';
            document.getElementById('fade').style.display='none';
            var suggestList = document.getElementById(did);
            suggestList.innerHTML = "";
            if(onScrollFlag==0){
                handleResponse1(data);
            }
            else if(onScrollFlag==1){
                handleResponse2(data);
            }
        }
    });
}
*/

//added by sruthi
function moreListDispTop(){ 
       if($("#datalist").is(":visible")){
        $(".hideAllDiv").hide();
    }else{
       $(".hideAllDiv").hide();//added by Prabal 
    $("#datalist").toggle(100);
}
}
//ended by sruthi
function sendRequest(url, payload)
{
    scrollURL=url;
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null){
        alert ("Your browser does not support AJAX!");
        return;
    }

    /*var parArr=allParamIds.split(",");
    var parArrVals=new Array();
    for(i=0;i<parArr.length;i++){
        parArrVals.push(document.getElementById(parArr[i]).value);
    }
    //changed by susheela satrt
    var passVales1 = "";
    for(i=0;i<parArr.length;i++){
//        parArrVals.push(document.getElementById(parArr[i]).value);
        if(i==0){
            passVales1 =document.getElementById(parArr[i]).value;
        }
        else{
            passVales1 +=";"+document.getElementById(parArr[i]).value;
        }
    } */

    var parValues=allParamIds.split(",");
    var idsList=new Array();
    var passValues;
    for(var i=0;i<parValues.length;i++){
        var id=parValues[i].replace("CBOARP","");
        var tempArray=new Array();
        idsList.push(id);
        if(i==0){
            //            if(id == query){
            //                var val=document.getElementById(tid).value;
            //                if(val == ""){
            //                    val="All";
            //                }
            //                tempArray.push(val);
            //               passValues=JSON.stringify(tempArray);
            //            }else{

            passValues=encodeURIComponent(document.getElementById(parValues[i]).value);
        //            }

        }else{
            //            if(id == query){
            //                var val=document.getElementById(tid).value;
            //                if(val == ""){
            //                    val="All";
            //                }
            //                tempArray.push(val);
            //                passValues += ";"+JSON.stringify(tempArray);
            //            }else{

            passValues += ";"+encodeURIComponent(document.getElementById(parValues[i]).value);
        //            }
        }

    }
    var searchKey=document.getElementById(tid).value;
    if(searchKey==""){
        searchKey='@';

    }
    else if(searchKey.match("All")!=null){
        changePlusImage();
        searchKey="@";

    }
    else{
        changePlusImage();
    //            country.value=searchKey;
    //            var prevVal=testArray[tid].split(",");
    //            var currentVal=searchKey;

    }
    //        var valueList=new Array();
    //        valueList.push(encodeURIComponent(searchKey));

    var REPORTID="";
    var iskpidash=parent.isKPIDashboard1.toString();
    //changed by susheela over
    if(iskpidash=='true'){
        REPORTID=document.forms.submitReportForm.REPORTID.value;
    }else{
        REPORTID=document.forms.frmParameter.REPORTID.value;
    }
    //    alert($("#REPORTID").val())
    //    alert(document.forms.submitReportForm.REPORTID.value) by
    //edited by mohit
    var mainUrl=url+"?"+payload+"&allParamIds="+idsList+"&parArrVals="+(passValues)+"&REPORTID="+$("#REPORTID").val()+"&orderVal="+orderVal;
    getLOVS(mainUrl);

/*
     xmlHttp.onreadystatechange=stateChanged;
    xmlHttp.open("POST",mainUrl,true);
    xmlHttp.send(null);
    */

}



function handleResponse1(response){
    var suggestList = document.getElementById(did);
    if(response=="Search Exception"){
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use <font style='font-weight:thicker;color:black'>@</font> for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else{

        var tabId="divTable"+did;
        suggestList.innerHTML = "";

        var names1=new Array();
        var names2=new Array();
        var flag = "false";
        var names = response.split("\n");
        //        var temp = document.getElementById(tid).value;
        //        var names2 = temp.split(",");
        var selectedValueList =eval('('+document.getElementById(stid).value+')');

        for(var j=0;j<names.length-1;j++)
        {
            flag = "false";

            if(selectedValueList.indexOf(names[j]) == -1){ //names[j]==names2[k]
                names1.push(names[j]);
            }
        }
        var id323="myTable"+tid;

        /*var table=document.createElement("table");

table.id=id323;
table.className = "myAjaxTable";
 suggestList.appendChild(table);*/

        suggestList.innerHTML="<table id='"+id323+"' style='word-wrap:breakword;' cellspacing=1 cellpadding=1 class='myAjaxTable'></table>"
        suggestArr.length=0;
        for(var i=0; i < names1.length; i++)
        {

            var suggestItem = document.createElement("div");
            suggestItem.id = "resultlist" + i;
            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){

                if(isglobalfiletr=='true'){
                    setCountrykpidash(this.innerHTML);
                }else{
                    setCountry(this.innerHTML);
                }

            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
            suggestArrLength=suggestArr.length;
            //suggestList.appendChild(suggestItem);


            /* ****************************** */

            var tbl = document.getElementById(id323);
            var lastRow = tbl.rows.length;
            // if there's no header row in the table, then iteration = lastRow + 1
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){
                // left cell
                var cellLeft = row.insertCell(0);
                // var textNode = document.createTextNode(iteration);
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;

                // right cell
                var cellRight = z[f].insertCell(1);
                // var xf=newdiv;
                cellRight.appendChild(suggestItem);

            }
        }


        if (names1.length >= 1){
            suggestList.style.display = "";
            suggestList.scrollTop=0;

            ajaxArr.push(suggestList);

            changePlusImage();
        }
        else{
            changeMinusImage();
            suggestList.style.display = "none";
        //document.getElementById(tid).focus();
        }

    }
}
function getSuggestions(country){

    var url;
    ctxPath=document.getElementById("h").value;
    var searchKey=country.value;
    var payload;

    //changed by Nazneen
    //    payload = "q="+searchKey+"&"+"query="+query;


    /*added by srikanth.p:start*/
    //    var prevJsonFilters=eval('('+document.getElementById(stid).value+')')


    url=ctxPath+"/dsrv";
    if(imageFlag==1){
        if(searchKey==""){
            searchKey='@';
        //            prevJsonFilters.length=0;
        //            prevJsonFilters.push("@");
        }
        else if(searchKey.match("All")!=null){
            changePlusImage();
            searchKey="@";
        //            prevJsonFilters.length=0;
        //            prevJsonFilters.push("@");
        }
        else{
            changePlusImage();
            country.value=searchKey;
        //            var prevVal=testArray[tid].split(",");
        //            var currentVal=searchKey;
        //            if(prevJsonFilters.indexOf(searchKey) == -1){
        //                prevJsonFilters.push(searchKey);
        //                if(prevJsonFilters.indexOf("@") ==-1){
        //                    prevJsonFilters.push("@");
        //                }
        //            }

        /* for(j=0;j<prevVal.length;j++){
                if((currentVal[currentVal.length-1]==prevVal[j]) || (currentVal[currentVal.length-1]=="")){
                    searchKey=searchKey+",@";
                }
            }*/
        }
        //changed by Nazneen
        //        payload = "q="+searchKey+"&"+"query="+query;
        var temp = encodeURIComponent(searchKey); //encodeURIComponent(
        payload = "q="+temp+"&"+"query="+query;
    }
    if(tabCode!=9){
        if(searchKey==""){
            searchKey='@';
        }else if(searchKey.match("All")!=null){
            changePlusImage();
            searchKey="@";
        }else{
            changePlusImage();
        //            country.value=searchKey;
        }



        var temp = encodeURIComponent(searchKey); //encodeURIComponent(
        payload = "q="+temp+"&"+"query="+query;

        checkId=checkId+payload;
        //        if(searchKey.lastIndexOf(",")!=(searchKey.length-1)){
        sendRequest(url, payload);
        //        }
        scrollFlag=true;
        startVal=1;
        scrollPayLoad=payload;
    }

}

function checkKey(e, obj)
{

    var country = document.getElementById(tid);
    paramValSel=false;





    if(country.value.length==0)
    {

        preservedvalue="";
    }


    if(country.value.length!=0)//if text field is not empty
    {



        if(country.value.lastIndexOf(',')==(country.value.length -1 ))
        {
            preservedvalue=country.value;

        }
    }

    /* get key pressed */
    var code = (e && e.which) ? e.which : window.event.keyCode;
    var evt = e || window.event;
    var ecsKey= evt.keyCode;


    /* if up or down move thru the suggestion list */
    if(ecsKey==27){
        hideAll();
        resetAllAjax();
    }
    else if (code == KEYDOWN || code == KEYUP)
    {
        var x=document.getElementById(did);

        var index = gSelectedIndex;
        var countId=index+2;
        if(countId==1)
            prevCount=7;
        var divHeight=0;
        divHeight=6*((x.scrollHeight-x.clientHeight)/suggestArr.length);
        if (code ==  KEYDOWN){

            if(index<suggestArr.length-1){
                index++;

                if(x.scrollTop<=(x.scrollHeight-x.clientHeight)){
                    if(x.scrollTop<=(x.scrollHeight-x.clientHeight-10)){
                        if((prevCount-countId)==0){
                            x.scrollTop=x.scrollTop+divHeight+5;
                            prevCount=prevCount+6;
                        }
                    }
                    else
                        x.scrollTop=x.scrollTop+5;
                }
            }
            else    {

                x.scrollTop=(x.scrollHeight-x.clientHeight);
            }
        } else{
            index--;
            countId=index+1;
            if(((prevCount-7)-countId)==0){
                var g=x.scrollTop-(divHeight+5);
                if(g>0)
                    x.scrollTop=g;
                else
                    x.scrollTop=0;

                prevCount=prevCount-6;
            }
            if(index<0){
                index=0;
                x.scrollTop=0;
                prevCount=7;
            }

        }
        /* find item in suggestion list being looked at if any */
        selectedItem = document.getElementById("resultlist" + index);
        if (selectedItem)
        {

            selectItem(selectedItem);


        /* set the field to the suggestion */
        }




    }
    else if (code == ENTER)  /* clear list if enter key */
    {

        selValAjax=1;
        paramValSel=true;
        onScrollFlag=0;
        var onEnterSelVal=selectedItem.innerHTML;

        if(onEnterSelVal==undefined)
            onEnterSelVal=country.value;
        if(onEnterSelVal.match("&amp;")!=null)
            onEnterSelVal=onEnterSelVal.replace("&amp;","&");
        setCountry(onEnterSelVal);
        /* preservedvalue=country.value;
        var test=preservedvalue.split(",");
        if((preservedvalue.length==0) || (preservedvalue=="All") || (onEnterSelVal=="All"))
        {
            country.value = onEnterSelVal;

        }
        else
        {
//             if(fieldValArray.length==0){
//          for(i=0;i<test.length;i++){
//           if(test[i]!="")
//               fieldValArray.push(test[i]);
//          }
//          }

            var prevValues=testArray[tid].split(",");
            var newStrValue="";
            for(i=0;i<test.length;i++){
                for(j=0;j<prevValues.length;j++){
                    if(test[i]==prevValues[j])
                        newStrValue=newStrValue+test[i]+",";
                }
            }

            if(newStrValue.charAt(newStrValue.length-1)==","){

                if(newStrValue.charAt(newStrValue.length-2)==",")
                    newStrValue=newStrValue.substr(0, (newStrValue.length-1));
            }
            if(newStrValue.match(onEnterSelVal)==null)
                country.value = newStrValue+onEnterSelVal;
        }
        //var tempVal=fieldValArray.toString();
        var tempVal=testArray[tid];
        if(tempVal.match(onEnterSelVal)==null){
            //fieldValArray.push(onEnterSelVal);
            testArray[tid]=testArray[tid]+onEnterSelVal+",";
        } */
        changeMinusImage();
        clearList();
        imageFlag1=0;

    }

    else if (code == BACKSPACE)
    {
        var temp=country.value;
        if(country.value.indexOf(',')!=country.value.length)
            paramValSel=true;
        temp=temp.substring(0,(temp.lastIndexOf(',')+1));

        preservedvalue=temp;

        gSelectedIndex = -1;
        getSuggestions(obj);//In checkKey

        keyPress=false;
        onScrollFlag=0;

    }
    else if(code==39 || code==37){
        checking123();
    }
    else if (country == obj) /* otherwise get more suggestions */
    {

        onScrollFlag=0;
        keyPress=false;
        tabCode=0;
        if(code==TAB){
            tabCode=TAB;
            paramValSel=true;
            hideAll();
        }
        gSelectedIndex = -1;
        getSuggestions(obj);
    }

    var gd=country.value;
    var fg=gd.split(",");
    var commaFlag=0;
    var newStr;
    for(i=0;i<fg.length;i++){
        if(fg[i]=="")
            commaFlag++;
    }
    if(commaFlag>1){
        if((fg[fg.length-2]=="") &&(fg[fg.length-1]=="") )
            newStr = gd.substring(0, gd.length-(commaFlag-1));
        else
            newStr=gd;

        country.value=newStr;
    }

//document.getElementById(tid).focus();

}

function selectItem(selectedItem)
{
    var lastItem = document.getElementById("resultlist" + gSelectedIndex);

    if (lastItem != null)
        unselectItem(lastItem);

    selectedItem.className = 'suggestLinkOver';
    gSelectedIndex = parseInt(selectedItem.id.substring(10));
//imageFlag1=0;
}

function unselectItem(selectedItem)
{
    selectedItem.className = 'suggestLink';
}

function setCountry(value)
{
    if(value.match("&amp;")!=null)
        value=value.replace("&amp;","&");
    selValAjax=1;
    var textFieldId=document.getElementById(stid);
    var internDivId=stid+"_"+value;
    var internElement="<div id='"+internDivId+"' class='newparamView' style='width:"+(10*(value.length))+";float:left;'>"
    internElement +="<table><tr>";
    internElement +="<td><span class='newParamName' >"+value+"</span></td>";
    internElement +="<td ><a href='javascript:deleteNewParam(\""+stid+"\",\""+internDivId+"\",\""+value+"\")' style='float:left' class='ui-icon ui-icon-close' ></a></td>";
    internElement +="</tr></table>";
    internElement +="</div>";


    var prevFilters=eval('('+textFieldId.value+')');

    if((value=="All"))
    {
        prevFilters.length=0;
        $("#ol_"+stid).empty();
        prevFilters.push(value);
        $("#ol_"+stid).append(internElement);

    }else
    {
        var i;
        for(i=0;i<prevFilters.length;i++){
            if(prevFilters[i].toUpperCase() == value.toUpperCase() ){
                break;
            }
            if(prevFilters[i].toUpperCase() == 'ALL'){
                $("#"+stid+"_"+prevFilters[i]).remove();
                prevFilters.splice(prevFilters.indexOf(prevFilters[i]));
            }
        }
        for(var j=i;j<prevFilters.length;j++){
            if(prevFilters[i].toUpperCase() == 'ALL'){
                $("#"+stid+"_All").remove();
                prevFilters.splice(prevFilters.indexOf(prevFilters[i]));
            }

        }
        if(i == prevFilters.length || i > prevFilters.length){
            prevFilters.push(value);
            $("#ol_"+stid).append(internElement);
        }

    }
    $("#"+stid).val(JSON.stringify(prevFilters));
    $("#"+tid).val("");
    $("#"+tid).focus();
    //var tempVal=fieldValArray.toString();
    //    var tempVal=testArray[tid];
    //    if(tempVal.match(value)==null){
    //        //fieldValArray.push(value);
    //        testArray[tid]=testArray[tid]+value+",";
    //    }
    imageFlag=0;
    imageFlag1=0;

    paramValSel=true;
    onScrollFlag=0;
    changeMinusImage();
    clearList();
}
function setCountrykpidash(value)
{
    if(value.match("&amp;")!=null)
        value=value.replace("&amp;","&");
    selValAjax=1;
    var namekpi="kpitext"+stid;
    var textFieldId=document.getElementById(namekpi);
    var internDivId=stid+"_kpi"+value;
    var internElement="<div id='"+internDivId+"' class='newparamView' style='width:"+(10*(value.length))+";float:left;'>"
    internElement +="<table><tr>";
    internElement +="<td><span class='newParamName' >"+value+"</span></td>";
    internElement +="<td ><a href='javascript:deleteNewParamkpi(\""+stid+"\",\""+internDivId+"\",\""+value+"\")' style='float:left' class='ui-icon ui-icon-close' ></a></td>";
    internElement +="</tr></table>";
    internElement +="</div>";


    var prevFilters=eval('('+textFieldId.value+')');
    //    alert('prev Values:'+prevFilters);
    //alert(prevFilters)
    if((value=="All"))
    {
        prevFilters.length=0;
        $("#olkpi_"+stid).empty();
        prevFilters.push(value);
        $("#olkpi_"+stid).append(internElement);

    }else
    {
        var i;
        for(i=0;i<prevFilters.length;i++){
            if(prevFilters[i].toUpperCase() == value.toUpperCase() ){
                break;
            }
            if(prevFilters[i].toUpperCase() == 'ALL'){
                $("#"+stid+"_kpi"+prevFilters[i]).remove();
                prevFilters.splice(prevFilters.indexOf(prevFilters[i]));
            }
        }
        for(var j=i;j<prevFilters.length;j++){
            if(prevFilters[i].toUpperCase() == 'ALL'){
                $("#"+stid+"_kpiAll").remove();
                prevFilters.splice(prevFilters.indexOf(prevFilters[i]));
            }

        }
        if(i == prevFilters.length || i > prevFilters.length){
            prevFilters.push(value);
            $("#olkpi_"+stid).append(internElement);
        }

    }

    stid="kpi"+stid;
    //    alert(prevFilters);
    $("#"+stid).val(JSON.stringify(prevFilters));
    $("#"+namekpi).val(JSON.stringify(prevFilters));
    $("#"+tid).val("");
    $("#"+tid).focus();
    var  perioddate=$('#'+stid).val();
    //var tempVal=fieldValArray.toString();
    //    var tempVal=testArray[tid];
    //    if(tempVal.match(value)==null){
    //        //fieldValArray.push(value);
    //        testArray[tid]=testArray[tid]+value+",";
    //    }
    imageFlag=0;
    imageFlag1=0;

    paramValSel=true;
    onScrollFlag=0;
    changeMinusImage();
    clearList();
    isglobalfiletr="";
}
function submiturlsinNewTab(path){
    parent.submiturlsinNewTab1(path);
}
//sandeep
var parArrVals=new Array();
function setCountrykpi(value,stid1,tid,selectid,checkid)
{
    var val=value;
    stid1="kpiCBOARP"+selectedparam;

    if ($("#"+checkid).is(':checked'))
    {
        //   alert(value)trim()
        val =val.toString().trim();
        parArrVals.push(val);
    //    var textFieldId=document.getElementById(stid1);
    //tickLenthComp.add("["+(i+1)+","+tickString.substring(0, 15).concat("...") + "\"]");

    }else {
        for(var i=0;i<parArrVals.length;i++){
            var val1=parArrVals[i];
            if(val1==val){
                parArrVals.splice(i, i);
                break;
            }
        }
    }
    $('#'+stid1).val(JSON.stringify(parArrVals));
    $('#'+tid).val("");
    $('#'+tid).focus();
    var  perioddate=$('#'+stid1).val();
//   alert(perioddate)
}
//sandeep
function setCountrykpigetcheckbox(img,stid1,tid1,selectid,elementId,id1,id2,id3,id4,checkboxdiv,optionid){
    //alert(parameterValue)
    var excludeIconId = elementId+"-excimg";
    $('#'+excludeIconId).hide();
    changeImage1(img,elementId,id1,id2,id3,id4,optionid);
    excludeBox = false;
    tabCode=0;

    //    document.getElementById(elementId).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(elementId).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;

        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){

        }
        else{

            imageFlag1=1;
        }
    }
    else{
        imageFlag1=0;
    }

    var url;
    ctxPath=document.getElementById("h").value;
    var searchKey=elementId.value;
    var payload;


    url=ctxPath+"/dsrv";
    if(imageFlag==1){
        if(searchKey==""){
            searchKey='@';
        }
        else if(searchKey.match("All")!=null){
            changePlusImage();
            searchKey="@";

        }
        else{
            changePlusImage();
            country.value=searchKey;

            var temp = encodeURIComponent(searchKey); //encodeURIComponent(
            payload = "q="+temp+"&"+"query="+query;
        }
    }
    if(tabCode!=9){
        searchKey='@';

        var temp = encodeURIComponent(searchKey); //encodeURIComponent(
        payload = "q="+temp+"&"+"query="+query;

        checkId=checkId+payload;

        scrollFlag=true;
        startVal=1;
        scrollPayLoad=payload;
    }


    scrollURL=url;
    xmlHttp=GetXmlHttpObject();
    if (xmlHttp==null){
        alert ("Your browser does not support AJAX!");
        return;
    }
    //alert("allParamIds"+allParamIds);
    var parValues=allParamIds.split(",");
    var idsList=new Array();
    var passValues;
    for(var i=1;i<parValues.length;i++){
        var id=parValues[i].replace("CBOARP","");
        var tempArray=new Array();
        idsList.push(id);
        if(i==1){
            //alert(document.getElementById("kpi" +parValues[i].trim()+"").value)
            passValues=document.getElementById("kpi" +parValues[i].trim()+"").value;
        //                alert("passValues"+passValues);

        //            }
        }else{
            //alert(parValues[i])
            passValues += ";"+encodeURIComponent(document.getElementById("kpi" + parValues[i].trim()+ "").value);
        //            }
        //                  alert("passValues"+passValues);
        }

    }
    //    alert('idList---->'+idsList+'parValues---'+parValues)
    //    var searchKey=document.getElementById(tid).value;
    if(searchKey==""){
        searchKey='@';

    }
    else if(searchKey.match("All")!=null){
        changePlusImage();
        searchKey="@";

    }
    else{
        changePlusImage();
    }
    var REPORTID="";

    var iskpidash=parent.isKPIDashboard1.toString();
    //changed by susheela over
    if(iskpidash=='true'){
        REPORTID=document.forms.submitReportForm.REPORTID.value;
    }else{
        REPORTID=document.forms.frmParameter.REPORTID.value;
    }

    //   alert("before mainUrl");
    var mainUrl=url+"?"+payload+"&allParamIds="+idsList+"&parArrVals="+(passValues)+"&REPORTID="+REPORTID+"&orderVal="+orderVal;

    // alert("after mainUrl"+mainUrl);

    $.ajax({
        url: mainUrl,
        success: function(data){



            data = data.split("\n");
            //  data =data.split(",");
            var htmlVar="";
            htmlVar+="<table >";
            htmlVar+="<tr><td>";
            htmlVar+="<input id='tableListall' name='tableList' type='checkbox' onclick=\"setCountrykpi('All','"+stid1+"','"+tid+"','"+selectid+"','tableListall')\">All</td>";
            htmlVar+="</td></tr>";
            for(var i=1;i<data.length;i++){

                var val=data[i];
                val =val.toString().replace("[", "");
                val =val.toString().replace("]", "");
                //alert(val)
                htmlVar+="<tr><td>";
                htmlVar+="<input id='tableList"+i+"' name='tableList' type='checkbox' onclick=\"setCountrykpi('"+val+"','"+stid1+"','"+tid+"','"+selectid+"','tableList"+i+"')\">"+val+"</td>";
                htmlVar+="</td></tr>";
            }
            htmlVar+="</table>";
            //       alert(htmlVar)
            $("#"+checkboxdiv).toggle();
            $("#"+checkboxdiv).html(htmlVar);
        //alert(data)

        }
    });

}

function checkClick(e)
{
    var target = ((e && e.target) ||(window && window.event && window.event.srcElement));
    var tag = target.tagName;
    if (tag.toLowerCase() != "input" && tag.toLowerCase() != "div")
        clearList();
}

function clearList()
{
    var suggestList = document.getElementById(did);
    suggestList.innerHTML = '';
    suggestList.style.display = "none";

}



window.onload = function ()
{

    //document.getElementById("suggestList").style.display = "none";
    //document.getElementById("suggestList1").style.display = "none";
    //document.requestForm.country1.focus();
    /*document.getElementById(tid).onkeyup = function(e){checkKey(e, this);};
  document.onclick = checkClick;*/

    /* kill default submit of a single field form */
    //document.myForm.onsubmit = function(){return false;};
    };

function GetXmlHttpObject()
{
    var xmlHttp=null;
    try
    {
        // Firefox, Opera 8.0+, Safari
        xmlHttp=new XMLHttpRequest();
    }
    catch (e)
    {
        // Internet Explorer
        try
        {
            xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
        }
        catch (e)
        {
            xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
    }
    return xmlHttp;
}

function stateChanged(){
    if (xmlHttp.readyState==4){
        document.getElementById('light').style.display='none';
        document.getElementById('fade').style.display='none';
        if(onScrollFlag==0){
            handleResponse1(xmlHttp.responseText);
        }
        else if(onScrollFlag==1){
            handleResponse2(xmlHttp.responseText);
        }
    }
    if (xmlHttp.readyState==3){
        document.getElementById('light').style.display='block';
        document.getElementById('fade').style.display='block';
    }
}
function changeImage(img,country1){
    tabCode=0;
    /* var divLoads=document.getElementsByClassName("imageLoading");
    for(i=0;i<divLoads.length;i++){
      divLoads[i].style.visibility="hidden";
        divLoads[i].style.display="none";
    }*/
    document.getElementById(country1).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(country1).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;

        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){
            z=imgSrc.replace("minus","plus");
            imgId="plus";
            imageFlag=2;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
        }
        else{
            z=imgSrc.replace("plus","minus");
            imgId="minus"
            imageFlag=1;



            imageId=img2[0]+"-"+imgId;
            img.id=imageId;

            img.src=z;
            imageFlag1=1;
            var country = document.getElementById(country1);

            country.focus();
            getSuggestions(country);//In Change Image

        }
    }
    else{
        imageFlag1=0;
    }

}

function excludeParameter(img,elementId,id1, id2, id3,id4)
{
    var includeIconId = id4+"-plus";
    $('#'+includeIconId).hide();
    changeImage(img,elementId,id1,id2,id3,id4);
    excludeBox = true;

}
//sandeep
var selectedprm="";
var selectedparam="";
var divtdid="";
var isglobalfiletr="";
function getparamvales(id,Name){

    selectedparam = parent.$("#"+id).val();
    selectedprm="CBOARP"+selectedparam;
    parent.$('#'+Name).val(selectedparam);

    var divid='column'+selectedparam;
    parent.$('#'+divtdid).hide();
    parent.$('#'+divid).show();
}
function kpireplaceinclude(id,Name)
{
    var div= parent.$("#"+id).val();
    divtdid='column'+div;
}
function includeparamkpi(img,elementId,id1, id2, id3,id4){
    isglobalfiletr="true";
    includeParameter(img,elementId,id1, id2, id3,id4)
}
function includeParameter(img,elementId,id1, id2, id3,id4)
{
    var excludeIconId = elementId+"-excimg";
    $('#'+excludeIconId).hide();
    changeImage(img,elementId,id1,id2,id3,id4); 
    excludeBox = false;

}


function changeImage(img,country1,id1, id2, id3,id4){
    //added by nazneen
    var eleId = country1.split('selFilter_');
    var orderId = 'orderVal_'+eleId[1];
    orderVal = document.getElementById(orderId).value;

    selId(id1, id2, id3,id4);
    tabCode=0;
    /* var divLoads=document.getElementsByClassName("imageLoading");
    for(i=0;i<divLoads.length;i++){
      divLoads[i].style.visibility="hidden";
        divLoads[i].style.display="none";
    }*/
    document.getElementById(country1).focus();
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        var con=document.getElementById(country1).value;
        var con2 = con.toString().replace(/ /g, '%20');
        con2 = con2.toString().replace("&", "%26");
        con = con2;

        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        if((imgId=="minus")&& (con!="ALL")){
            z=imgSrc.replace("minus","plus");
            imgId="plus";
            imageFlag=2;
            imageId=img2[0]+"-"+imgId;
            img.id=imageId;
            img.src=z;
        }
        else{
            z=imgSrc.replace("plus","minus");
            imgId="minus"
            imageFlag=1;



            imageId=img2[0]+"-"+imgId;
            img.id=imageId;

            img.src=z;
            imageFlag1=1;
            var country = document.getElementById(country1);

            country.focus();
            getSuggestions(country);//In Change Image

        }
    }
    else{
        imageFlag1=0;
    }

}


//sandeep
function changeImage1(img,country1,id1, id2, id3,id4,optionid){

    selectedparam = parent.$("#"+optionid).val();

    selectedprm="CBOARP"+selectedparam;
    var eleId = country1.split('selFilter_');
    var orderId = 'orderVal1_'+selectedprm;
    orderVal = parent.document.getElementById(orderId).value;
    tid=id1;
    did=id2;
    query=selectedparam;
    stid=id4;
    tabCode=0;
    var x= parent.document.getElementById("includeparamval").value;
    var y="";
    var newArr=new Array();
    var added = false;
    added = false;
    y =x.toString().replace("[", "");
    y =y.toString().replace("]", "");
    y =y.toString().replace(" ", "");
    newArr.push(y);

    allParamIds=newArr.toString();
    testArray.length=newArr.length;
    if(ajaxArr.length!=0)
        hideAll();
    if(imageFlag1==0){
        scrollPayLoad="";
        scrollURL=""
        onScrollFlag=0;
        scrollFlag=true;
        keyPress=true;
        tbz++;
        prevCount=7;
        imageId=img.id;
        var img2=imageId.split("-");
        var imgId=img2[1];
        var imgSrc=img.src;
        var z;

        imageFlag1=1;

    }
    else{
        imageFlag1=0;
    }

}


function changeMinusImage(){
    imageId=stid+"-minus";

    if ( excludeBox == true )
    {
        var includeInputBox = stid;
        var excludeInputBox = stid+"_exc";
        var value = $('#'+includeInputBox).val();
        $('#'+excludeInputBox).val(value);
    }

    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");

        var src=img.src;
        var newSrc;

        newSrc=src.replace("minus","plus");
        imageId=img2[0]+"-plus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=2;
    //clearList();

    }
}
function changePlusImage(){

    imageId=tid+"-plus";
    var img=document.getElementById(imageId);
    if(img!=null){
        var img2=imageId.split("-");

        var src=img.src;
        var newSrc;

        newSrc=src.replace("plus","minus");
        imageId=img2[0]+"-minus";
        img.id=imageId;
        img.src=newSrc;
        imageFlag=1;

    }
}

function resetAllAjax(){
    //clearList();
    changeMinusImage();

}
function hideAll(){
    var ad;
    while(ajaxArr.length!=0){
        ad=ajaxArr.pop();
        ad.style.display = "none";
    }
    suggestArr.length=0;
}

zs=0;

function checkImg(tb){
    var jk=tb.id.split("-");
    var kl=imageId.split("-");
    if(imageId!=""){

        if(jk[0]!=kl[0]){

            imageFlag1=0;
            fieldValArray.length=0;
        }
        else{

            imageFlag1=1;
        }
    }
    if((imageFlag==2)&&(imageFlag1==1))
        imageFlag1=0;

    if(ajaxArr.length==0)
        imageFlag1=0;
    if(paramValSel)
        imageFlag1=0;



    scHeight=0;
    scTop=0;
    clHgt=0;
    startVal=1;
    endVal=0;

    gSelectedIndex = -1;
}

function onScrollDiv(divObj){
    scHeight=(divObj.scrollHeight);
    scTop=(divObj.scrollTop);


    clHgt=(divObj.clientHeight);
    if(scrollFlag){
        if(scTop==(scHeight-clHgt)){

            startVal=startVal+20;
            scrollPayLoad1=scrollPayLoad+"&startValue="+startVal;

            sendRequest(scrollURL, scrollPayLoad1);
            onScrollFlag=1;
        }

    }
}
function handleResponse2(response){
    if(response=="Search Exception"){
        var suggestList = document.getElementById(did);
        document.getElementById(tid).value="";
        suggestList.innerHTML="<font style='font-size:10px;color:red'>Invalid Search Key<br> Please Use @ for Blind Search</font>";
        suggestList.style.display = "";
        ajaxArr.push(suggestList);
    }
    else{

        var x=document.getElementById("myTable"+tid).rows;

        var names1=new Array();
        // var names = response.xhr.responseText.split("\n");
        var names = response.split("\n");
        var selectedValList=eval('('+document.getElementById(stid).value+')');


        for(var j=0;j<names.length-1;j++)
        {

            if(selectedValList.indexOf(names[j])<0)
            {

                names1.push(names[j]);


            }
        }


        suggestArrLength=suggestArr.length;

        for(var i=0; i < names1.length; i++)
        {

            var suggestItem = document.createElement("div");
            var sugId=suggestArrLength+i;
            suggestItem.id = "resultlist"+sugId;

            suggestItem.onmouseover = function(){
                selectItem(this);
            };
            suggestItem.onmouseout = function(){
                unselectItem(this);
            };
            suggestItem.onclick = function(){
                setCountry(this.innerHTML);
            };
            suggestItem.className = "suggestLink";
            suggestItem.appendChild(document.createTextNode(names1[i]));
            suggestArr.push(suggestItem.innerHTML);
            //suggestList.appendChild(suggestItem);


            /* ****************************** */


            // if there's no header row in the table, then iteration = lastRow + 1
            var tbl = document.getElementById("myTable"+tid);
            var lastRow = tbl.rows.length;
            var iteration = lastRow;
            var row = tbl.insertRow(lastRow);
            if(lastRow%2==0){
                // left cell
                var cellLeft = row.insertCell(0);
                // var textNode = document.createTextNode(iteration);
                cellLeft.appendChild(suggestItem);
            }
            else{
                var z=tbl.rows;
                var f=z.length-2;

                // right cell
                var cellRight = z[f].insertCell(1);
                // var xf=newdiv;
                cellRight.appendChild(suggestItem);

            }
        }
        if(names1.length==0){

            scrollFlag=false;
        }


    }
}
function checking123(){

}

function displayfavlink(){
    $("#favlinkcont").toggle(500);

/*
    obj =document.getElementById('favlinkcont');
    obj1 = document.getElementById('displayfavimg');
    if (obj.style.display != 'none'){
        obj.style.display ='none';
        obj1.src='images/addImg.gif';

    }
    else{
        obj.style.display ='';
        obj1.src='images/deleteImg.gif';
    }
    */
}
function parentFunction(){
    //window.location.href='sapsalesvolrep.jsp';
    //window.location.href='pbusercustlinks.jsp';
    submitform();
}
//function mngFavLinks(){
//    window.open("pbCustomizeLinks.jsp?userId="+document.frmParameter.hdnUserid.value,"CustomizeLinks", "scrollbars=1,width=550,height=350,address=no");
//}
//function seqFavLinks(){
//    window.open("pbPrioritizeLinks.jsp?userId="+document.frmParameter.hdnUserid.value,"PrioritizeLinks", "scrollbars=1,width=550,height=350,address=no");
//}
function setnotinfilters(){
     var reportId = '';
    try{
        reportId = document.forms.frmParameter.REPORTID.value;
    }catch(e){
        reportId = parent.$("#graphsId").val();
    }
     var ctxPath=document.getElementById("h").value;
     var keys = Object.keys(parent.filterMapNotin);
        for(var i in keys){
            var elemntid=keys[i];
            var selectedFilters1 = [];
             var likeVal=$("#LIKE_"+elemntid).val();
    var notLikeVal=$("#NOTLIKE_"+elemntid).val();

            selectedFilters1=parent.filterMapNotin[elemntid];
            if (selectedFilters1 !== "" && selectedFilters1 !== "[]" && selectedFilters1 !== undefined && selectedFilters1.length >= 1 && selectedFilters1[0] != "All") {
    $.post(
        ctxPath+'/reportViewer.do?reportBy=setOperatorFilters&REPORTID='+reportId+"&dimId="+elemntid+"&NOTIN="+encodeURIComponent(JSON.stringify(selectedFilters1))+"&LIKE="+encodeURIComponent(likeVal)+"&NOTLIKE="+encodeURIComponent(notLikeVal),
        function(data){
            
        }
        );
            }
}
}
function submitform(){
//parent.filterMapNewtb=parent.filterMapNew;
//parent.comboChartFlag = true;
//try{
//var chartData = JSON.parse($("#chartData").val());
//if(typeof chartData !=="undefined"){
//    var keys = Object.keys(chartData);
//    alert(keys);
//    for(var i in keys){
//        var chartType = chartData[keys[i]]["chartType"];
//        alert(chartType)
//        if(chartType==="Combo-Analysis"){
//            chartData[keys[i]]["comboDataFlag"]=true;
//            parent.$("#chartData").val(JSON.stringify(chartData));
//        }
//       
//    }
//}
//}catch(e){}
   
    var periodType = $("#CBO_PRG_PERIOD_TYPE").val();
    var reportId = '';
    try{
        reportId = document.forms.frmParameter.REPORTID.value;
    }catch(e){
        reportId = parent.$("#graphsId").val();
    }
    if(typeof window.current_Tab_Flag !=="undefined" && window.current_Tab_Flag=="Graph"){
 var keys = Object.keys(parent.filterMapNew);
        for(var i in keys){
            var elemntid=keys[i];
            var selectedFilters1 = [];
            selectedFilters1=parent.filterMapNew[elemntid]
            parent.filterMapNewtb[elemntid]=selectedFilters1
        }
         setnotinfilters();
    parent.document.getElementById('loading').style.display='';
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
    }else{
      $.ajax({
        async:false,
        type:"POST",
        url:  'reportViewerAction.do?reportBy=getPeriodDataFlag&periodType='+periodType+'&reportId='+reportId,
        success:function(data) {
            
            if(data !=="" && data==="y"){
                var r=confirm("Report is searching for large data set. Do you want to continue? Please select atleast one filter.");
                if(r==true){
                     var keys = Object.keys(parent.filterMapNew);
                for(var i in keys){
                    var elemntid=keys[i];
                    var selectedFilters1 = [];
                    selectedFilters1=parent.filterMapNew[elemntid]
                    parent.filterMapNewtb[elemntid]=selectedFilters1
}
 setnotinfilters();
                parent.document.getElementById('loading').style.display='';
                document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID="+document.forms.frmParameter.REPORTID.value;
                document.forms.frmParameter.submit();
                }else{
                    return;
                }
            }else{
                var keys = Object.keys(parent.filterMapNew);
                for(var i in keys){
                    var elemntid=keys[i];
                    var selectedFilters1 = [];
                    selectedFilters1=parent.filterMapNew[elemntid]
                    parent.filterMapNewtb[elemntid]=selectedFilters1
                }
                 setnotinfilters();
                parent.document.getElementById('loading').style.display='';
                document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=paramChange&REPORTID="+document.forms.frmParameter.REPORTID.value;
                document.forms.frmParameter.submit(); 
            }          

        }
                
    });  
    }
      

 
}

function submitFormMeasChange(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=measChange&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
}

function submitFormGraphMeasChange(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&action=grpMeasChange&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
}

function submitformTimeSeries(isTimeSeries){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value+'&isTimeSeries='+isTimeSeries;
    document.forms.frmParameter.submit();
}
function submitdashboard(){
    document.forms.frmParameter.action = "dashboardViewer.do?reportBy=viewDashboard&action=paramChange&REPORTID="+document.forms.frmParameter.REPORTID.value+"&ViewBy="+$("#ViewBy").val();
    document.frmParameter.submit();
}
function submitdashboardbyfilter(){
    setMultiFilter()
    //    alert($("#ViewBy").val())
    document.forms.frmParameter.action = "dashboardViewer.do?reportBy=viewDashboard&action=paramChange&REPORTID="+document.forms.frmParameter.REPORTID.value+"&ViewBy="+$("#ViewBy").val();
    document.frmParameter.submit();
}
function copyP()
{
    document.frmParameter.action="Copy/Jsps/sample2.jsp";
    document.frmParameter.target="_blank";
    document.frmParameter.submit();

}

function submiturls1($ch)
{
    document.frmParameter.action = $ch;
    document.frmParameter.submit();
}
function submiturls2($ch)
{

    document.frmParameter.action = "reportViewer.do?reportBy=viewReport&REPORTID="+document.forms.frmParameter.REPORTID.value+$ch;
    document.frmParameter.submit();
}
function displayUtilities(){
    $('#UtilitiesCont').toggle(500)
/*obj =document.getElementById('UtilitiesCont');
    obj1 = document.getElementById('Utilitiesimg');
    if (obj.style.display != 'none'){
        obj.style.display ='none';
        obj1.src='images/addImg.gif';

    }
    else{
        obj.style.display ='';
        obj1.src='images/deleteImg.gif';
    }*/
}

function getAllImageNames(){

    var x=document.getElementsByTagName("img");
    var y="";
    var newArr=new Array();
    var added = false;
    for(i=0;i<x.length;i++){
        added = false;
        if(x[i].id.match("plus")){
            y=x[i].id.split("-");
            for ( var j=0; j<newArr.length; j++)
                if ( newArr[j] == y )
                    added = true;
            if ( added == false )
                newArr.push(y[0]);
        }
        else if ( x[i].id.match("excimg") )
        {
            y=x[i].id.split("-");
            for ( var k=0; k<newArr.length; k++)
                if ( newArr[k] == y )
                    added = true;
            if ( added == false )
                newArr.push(y[0]);
        }

    }
    allParamIds=newArr.toString();
    testArray.length=newArr.length;
    for(j=0;j<newArr.length;j++){

        if((document.getElementById(newArr[j]).value=="") || (document.getElementById(newArr[j]).value=="All"))
            testArray[newArr[j]]="";
        else
            testArray[newArr[j]]=document.getElementById(newArr[j]).value;
    }
}

function getLOVS(mainURL){

    $.ajax({
        url: mainURL,
        success: function(data){
            if(onScrollFlag==0){
                handleResponse1(data);
            }
            else if(onScrollFlag==1){
                handleResponse2(data);
            }

        }
    });

}

//added by uday on 19-mar-2010
function submitform2(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value;
    document.forms.frmParameter.submit();
}
function submitform3(){
    document.forms.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value+"&hideShowColumns=true";
    document.forms.frmParameter.submit();
}
function submiturls22($ch)
{
    document.frmParameter.action = "reportViewer.do?reportBy=viewWhatIfScenario&REPORTID="+document.forms.frmParameter.REPORTID.value+$ch;
    document.frmParameter.submit();
}
function getChangeDuration(){
    var type = $("#CBO_PRG_PERIOD_TYPE").val();

    var typeDuration = $("#CBO_PRG_COMPARE").val();
    var cbo = "";
    if(type == "Day"){
        if (typeDuration == "Previous Day"  || typeDuration == "Last Qtr" || typeDuration == "Last Year" ) {
            cbo = cbo + "<option selected value=\"Previous Day\" > Previous Day  </option>";
            cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
            cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
            cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Day Last Week" || typeDuration == "Last Week"|| typeDuration == "Same Week Last Year" || typeDuration == "Same Month Last Year" || typeDuration == "Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Previous Day\" > Previous Day  </option>";
            cbo = cbo + "<option  selected value=\"Same Day Last Week\" > Same Day Last Week </option>";
            cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
            cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Day Last Month" || typeDuration == "Last Month"|| typeDuration == "Complete Last Week" || typeDuration == "Complete Last Month" || typeDuration == "Complete Last Qtr" ) {
            cbo = cbo + "<option value=\"Previous Day\" > Previous Day  </option>";
            cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
            cbo = cbo + "<option selected value=\"Same Day Last Month\"> Same Day Last Month </option>";
            cbo = cbo + "<option value=\"Same Day Last Year\"> Same Day Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Day Last Year" || typeDuration == "Complete Same Week Last Year" || typeDuration == "Complete Same Month Last Year" || typeDuration == "Complete Same Qtr Last Year" || typeDuration == "Complete Last Year") {
            cbo = cbo + "<option value=\"Previous Day\" > Previous Day  </option>";
            cbo = cbo + "<option value=\"Same Day Last Week\" > Same Day Last Week </option>";
            cbo = cbo + "<option value=\"Same Day Last Month\"> Same Day Last Month </option>";
            cbo = cbo + "<option selected value=\"Same Day Last Year\"> Same Day Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }
    }else if(type == "Week"){
        if (typeDuration == "Previous Day" || typeDuration == "Last Week" || typeDuration == "Last Month" || typeDuration == "Last Qtr" || typeDuration == "Last Year" ) {
            cbo = cbo + "<option selected value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
            cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Week Last Year" || typeDuration == "Same Day Last Week"|| typeDuration == "Same Month Last Year" || typeDuration == "Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option selected value=\"Same Week Last Year\" > Same Week Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
            cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Last Month" || typeDuration == "Same Day Last Month"|| typeDuration == "Complete Last Week" || typeDuration == "Complete Last Month" || typeDuration == "Complete Last Qtr"  || typeDuration == "Complete Last Year") {
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
            cbo = cbo + "<option selected value=\"Complete Last Week\"> Complete Last Week </option>";
            cbo = cbo + "<option value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Last Year"|| typeDuration == "Same Day Last Year" || typeDuration == "Complete Same Week Last Year" || typeDuration == "Complete Same Month Last Year" || typeDuration == "Complete Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Week\" > Last Week </option>";
            cbo = cbo + "<option value=\"Same Week Last Year\" > Same Week Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Week\"> Complete Last Week </option>";
            cbo = cbo + "<option selected value=\"Complete Same Week Last Year\"> Complete Same Week Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }
    }else if(type == "Month"){
        if (typeDuration == "Previous Day" || typeDuration == "Last Week" || typeDuration == "Last Month" || typeDuration == "Last Qtr" || typeDuration == "Last Year" ) {
            cbo = cbo + "<option selected value=\"Last Month\" > Last Month </option>";
            cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
            cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Week Last Year" || typeDuration == "Same Day Last Week"|| typeDuration == "Same Month Last Year" || typeDuration == "Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
            cbo = cbo + "<option selected value=\"Same Month Last Year\" > Same Month Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
            cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Complete Last Week" || typeDuration == "Same Day Last Month"|| typeDuration == "Complete Last Month" || typeDuration == "Complete Last Qtr"  || typeDuration == "Complete Last Year") {
            cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
            cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
            cbo = cbo + "<option selected value=\"Complete Last Month\"> Complete Last Month </option>";
            cbo = cbo + "<option value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Complete Same Week Last Year"|| typeDuration == "Same Day Last Year" || typeDuration == "Complete Same Month Last Year" || typeDuration == "Complete Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Month\" > Last Month </option>";
            cbo = cbo + "<option value=\"Same Month Last Year\" > Same Month Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Month\"> Complete Last Month </option>";
            cbo = cbo + "<option selected value=\"Complete Same Month Last Year\"> Complete Same Month Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }

    }else if(type == "Qtr"){
        if (typeDuration == "Previous Day" || typeDuration == "Last Week" || typeDuration == "Last Month" || typeDuration == "Last Qtr" || typeDuration == "Last Year" ) {
            cbo = cbo + "<option selected value=\"Last Qtr\" > Last Qtr </option>";
            cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
            cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Same Week Last Year"  || typeDuration == "Same Day Last Week" || typeDuration == "Same Month Last Year" || typeDuration == "Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Qtr\" > Last Qtr </option>";
            cbo = cbo + "<option selected value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
            cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Complete Last Week"|| typeDuration == "Same Day Last Month" || typeDuration == "Complete Last Month" || typeDuration == "Complete Last Qtr"  || typeDuration == "Complete Last Year") {
            cbo = cbo + "<option value=\"Last Qtr\" > Last Qtr </option>";
            cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
            cbo = cbo + "<option selected value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
            cbo = cbo + "<option value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Complete Same Week Last Year"|| typeDuration == "Same Day Last Year" || typeDuration == "Complete Same Month Last Year" || typeDuration == "Complete Same Qtr Last Year") {
            cbo = cbo + "<option svalue=\"Last Qtr\" > Last Qtr </option>";
            cbo = cbo + "<option value=\"Same Qtr Last Year\" > Same Qtr Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Qtr\"> Complete Last Qtr </option>";
            cbo = cbo + "<option selected value=\"Complete Same Qtr Last Year\"> Complete Same Qtr Last Year</option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }

    }else if(type == "Year"){
        if (typeDuration == "Previous Day" || typeDuration == "Same Day Last Week" || typeDuration == "Same Day Last Month" || typeDuration == "Same Day Last Year"|| typeDuration == "Last Week" || typeDuration == "Last Month" || typeDuration == "Last Qtr" || typeDuration == "Last Year" || typeDuration == "Same Week Last Year" || typeDuration == "Same Month Last Year" || typeDuration == "Same Qtr Last Year"
            ) {
            cbo = cbo + "<option selected value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option value=\"Complete Last Year\"> Complete Last Year </option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }else if (typeDuration == "Complete Last Week" || typeDuration == "Complete Last Month" || typeDuration == "Complete Last Qtr"  || typeDuration == "Complete Last Year"
            || typeDuration == "Complete Same Week Last Year" || typeDuration == "Complete Same Month Last Year" || typeDuration == "Complete Same Qtr Last Year") {
            cbo = cbo + "<option value=\"Last Year\" > Last Year </option>";
            cbo = cbo + "<option selected value=\"Complete Last Year\"> Complete Last Year </option>";
            $("#CBO_PRG_COMPARE").html()
            $("#CBO_PRG_COMPARE").html(cbo);
        }

    }
}
function getChangeCompare(){
    var type = $("#CBO_PRG_COMPARE").val();
    var typeDuration = $("#CBO_PRG_PERIOD_TYPE").val();
    var cbo = "";
    if(typeDuration == "Day")
    {
        if (type == "Previous Day") {
            cbo = cbo + "<option selected value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Day Last Week"){
            cbo = cbo + "<option selected value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Day Last Month"){
            cbo = cbo + "<option selected value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Day Last Year"){
            cbo = cbo + "<option selected value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }
    }
    else if(typeDuration == "Week")
    {
        if (type == "Last Week") {
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option selected value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Week Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option selected value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Last Week"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option selected value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Same Week Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option selected value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }

    }else if(typeDuration == "Month")
    {
        if (type == "Last Month") {
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option selected value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Month Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option selected value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Last Month"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option selected value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Same Month Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option selected value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }

    }else if(typeDuration == "Qtr")
    {
        if (type == "Last Qtr") {
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option selected value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Same Qtr Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option selected value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Last Qtr"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option selected value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Same Qtr Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option selected value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }

    }else if(typeDuration == "Year")
    {
        if (type == "Last Year") {
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option selected value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }else if(type == "Complete Last Year"){
            cbo = cbo + "<option value=\"Day\" > Day </option>";
            cbo = cbo + "<option value=\"Week\" > Week </option>";
            cbo = cbo + "<option value=\"Month\"> Month </option>";
            cbo = cbo + "<option value=\"Qtr\"> Qtr</option>";
            cbo = cbo + "<option selected value=\"Year\"> Year</option>";
            $("#CBO_PRG_PERIOD_TYPE").html()
            $("#CBO_PRG_PERIOD_TYPE").html(cbo);
        }

    }


}

function datetoggl(userType,isPAEnableforUser){
    var ctxPath=document.getElementById("h").value;
    var reportId= document.forms.frmParameter.REPORTID.value;
    //    if(userType=="Admin" || isPAEnableforUser=="true"){
    $.post(
        ctxPath+'/reportViewer.do?reportBy=dateToggle&REPORTID='+reportId,
        function(data){
            if(data=='Success'){
                document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&isToggle=Y";
                document.forms.frmParameter.submit();
            }

        });
//    }else{
//              alert("You do not have the sufficient previlages");
//           }

}
//modified by krishan pratap
function datetoggl1(userType,isPAEnableforUser,datetoggl){
 //   alert("datetoggl1 reportviewer.js"+datetoggl);
    var ctxPath=document.getElementById("h").value;
    var reportId= document.forms.frmParameter.REPORTID.value;
    //    if(userType=="Admin" || isPAEnableforUser=="true"){
    $.post(
        ctxPath+'/reportViewer.do?reportBy=dateToggle&REPORTID='+reportId+'&datetoggl='+datetoggl,
        function(data){
            if(data=='Success'){
			//alert("datetoggl reportviewer.js")
                document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&isToggle=Y";
                document.forms.frmParameter.submit();
            }

        });
//    }else{
//              alert("You do not have the sufficient previlages");
//           }

}

function datetoggleForGraph(url){
 //   alert("datetoggl1 reportviewer.js"+datetoggl);
 var dateTog = "No";
 var fromVisual = "true";
    var ctxPath=document.getElementById("h").value;
    var reportId= document.forms.frmParameter.REPORTID.value;
    //    if(userType=="Admin" || isPAEnableforUser=="true"){
    $.post(
        ctxPath+'/reportViewer.do?reportBy=dateToggle&REPORTID='+reportId+'&datetoggl='+dateTog,
        function(data){
            if(data=='Success'){
			//alert("datetoggl reportviewer.js")
                document.forms.frmParameter.action=ctxPath+"/reportViewer.do?reportBy=viewReport&REPORTID="+reportId+"&fromVisual="+fromVisual+"&isToggle=Y"+url;
                document.forms.frmParameter.submit();
            }
                else{
              alert("You do not have the sufficient previlages");
           }
        });
//   

}

function rowaddkpi(colname,dispColName,REPORTID,ctxPath,Ref_element,position,type){

    var ctxPath=document.getElementById("h").value;
    $.post(ctxPath+'/reportViewer.do?reportBy=rowAddingKpi&msrname='+colname+'&REPORTID='+REPORTID+'&dispColName='+dispColName+'&position='+position+'&type='+type+'&isrowadded=true',
        function(data){
            $("#KpiDashboardDiv").html(data);
        });
}
function deleteNewParam(dimId,divId,value){
    var prevValues=eval('('+document.getElementById(dimId).value+')')
    prevValues.splice(prevValues.indexOf(value),1);
    if(dimId.indexOf('new_') ==-1){
        if(prevValues.length ==0){
            prevValues.push("All");
            //          var elmentContent=createDynamicElement(dimId,value);
            $("#ol_"+dimId).append(createDynamicElement(dimId,"All"));

        }
    }
    $("#"+dimId).val(JSON.stringify(prevValues));
    var divElemId=document.getElementById(divId);
    divElemId.parentNode.removeChild(divElemId);
    if(value!="All"){
        var gloabaId=divId+"_Global";
        var globDivElemId=document.getElementById(gloabaId);
        globDivElemId.parentNode.removeChild(globDivElemId);

    }
//    $("#"+divId).remove();

}
//sandeep
function deleteNewParamkpi(dimId,divId,value){
    var namekpi="kpitext"+dimId;
    var prevValues=eval('('+document.getElementById(namekpi).value+')')
    //  alert(prevValues)
    prevValues.splice(prevValues.indexOf(value),1);
    if(dimId.indexOf('new_') ==-1){
        if(prevValues.length ==0){
            prevValues.push("All");
            //          var elmentContent=createDynamicElement(dimId,value);
            $("#olkpi_"+dimId).append(createDynamicElementkpi(dimId,"All"));

        }
    }

    $("#"+dimId).val(JSON.stringify(prevValues));
    $("#"+namekpi).val(JSON.stringify(prevValues));
    dimId="kpi"+dimId;
    $("#"+dimId).val(JSON.stringify(prevValues));
    var divElemId=document.getElementById(divId);
    divElemId.parentNode.removeChild(divElemId);
    if(value!="All"){
        var gloabaId=divId+"_Global";
        var globDivElemId=document.getElementById(gloabaId);
        globDivElemId.parentNode.removeChild(globDivElemId);

    }
//alert($("#"+dimId).val())
//    $("#"+divId).remove();

}
function createDynamicElement(dimId,value){
    var internDivId=dimId+"_"+value;
    var internElement="<div id='"+internDivId+"' class='newparamView' style='width:"+(10*(value.length))+";float:left;'>"
    internElement +="<table><tr>";
    internElement +="<td><span class='newParamName' >"+value+"</span></td>";
    internElement +="<td ><a href='javascript:deleteNewParam(\""+dimId+"\",\""+internDivId+"\",\""+value+"\")' style='float:left' class='ui-icon ui-icon-close' ></a></td>";
    internElement +="</tr></table>";
    internElement +="</div>";
    return internElement;
}
function createDynamicElementkpi(dimId,value){
    var internDivId=dimId+"_kpi"+value;
    var internElement="<div id='"+internDivId+"' class='newparamView' style='width:"+(10*(value.length))+";float:left;'>"
    internElement +="<table><tr>";
    internElement +="<td><span class='newParamName' >"+value+"</span></td>";
    internElement +="<td ><a href='javascript:deleteNewParamkpi(\""+dimId+"\",\""+internDivId+"\",\""+value+"\")' style='float:left' class='ui-icon ui-icon-close' ></a></td>";
    internElement +="</tr></table>";
    internElement +="</div>";
    return internElement;
}
//var viewbys=$("#numbuerOfViewbys").val();
//if (!$('#tabParameters').is(':visible')) {
//    var divHeight=(4/5)*($(window).height());
//    $("#tabParameters").height(divHeight);
//    $(".dynamicClass").css('height', (((9/10)*(divHeight))-(viewbys*10)));
//
//}
function deleteglobalParam(dimId,divId,value){
    var divElemId=document.getElementById(divId);
    divElemId.parentNode.removeChild(divElemId);
    var parentId=dimId+"_"+value;
    deleteNewParam(dimId,parentId,value)

}
function filterBy(dimval,name,reportId,dimId){
    $("#DimFilterBy").dialog({
        autoOpen: false,
        height: 250,
        width: 450,
        position: 'justify',
        modal: true,
        title:name
    });
    //    $("#DimFilterBy").dialog('title',name);
    var ctxPath=document.getElementById("h").value;
    $.post(
        ctxPath+'/reportViewer.do?reportBy=getOperatorFilters&REPORTID='+reportId+"&dimId="+dimId,
        function(data){
            $("#DimFilterBy").html(data);
            $("#DimFilterBy").dialog('open');

        }
        );


}
function closeFilterBy(elemId,dim,selFilId,reportId){
    var likeVal=$("#LIKE_"+dim).val();
    var notLikeVal=$("#NOTLIKE_"+dim).val();
    var notInVal=$("#"+dim).val();
    var ctxPath=document.getElementById("h").value;

    $.post(
        ctxPath+'/reportViewer.do?reportBy=setOperatorFilters&REPORTID='+reportId+"&dimId="+elemId+"&NOTIN="+encodeURIComponent(notInVal)+"&LIKE="+encodeURIComponent(likeVal)+"&NOTLIKE="+encodeURIComponent(notLikeVal),
        function(data){
            $("#DimFilterBy").dialog('close');
        }
        );


}
function changeOperator(id,dimid,elemId){
    var filter=$("#"+id).val()
    operator=filter;
    if(filter == "LIKE" ){
        $("#selFilter_"+dimid).hide();
        $("#NOTLIKE_selFilter_"+dimid).hide();
        $("#LIKE_selFilter_"+dimid).show();


    /*var disableonkeyUpfunction="disableonkeyselId('selFilter_"+dimid+",'"+dimid+"SuggestList','"+elemId+"','"+dimid+"')";
        $("#selFilter_"+dimid).attr("onFocus",disableonkeyUpfunction); */


    }else if(filter== "NOTLIKE" || filter== "NOT LIKE"){
        $("#selFilter_"+dimid).hide();
        $("#LIKE_selFilter_"+dimid).hide();
        $("#NOTLIKE_selFilter_"+dimid).show();


    }else{
        $("#selFilter_"+dimid).show();
        $("#LIKE_selFilter_"+dimid).hide();
        $("#NOTLIKE_selFilter_"+dimid).hide();
    /*var enableonkeyUpfunction="selId('selFilter_"+dimid+",'"+dimid+"SuggestList','"+elemId+"','"+dimid+"')";
        $("#selFilter_"+dimid).attr("onFocus",enableonkeyUpfunction); */

    }
}

function onkeyUpLikefun(dimid,elemId,selId){
    var filter="LIKE";
    $("#LIKE_selFilter_"+dimid).keyup(function(event){
        if(event.keyCode == 13){
            var value=$("#LIKE_selFilter_"+dimid).val();
            if(value != "" || value.length >0){
                var prevValJson=$("#"+filter+'_'+dimid).val();
                var prevVal=new Array();
                if(prevValJson.length >0){
                    prevVal=eval('('+prevValJson+')');
                }
                prevVal.push(value);
                var jsonVal=JSON.stringify(prevVal);
                $("#"+filter+'_'+dimid).val(jsonVal);
                var internelement=createDynamicElementForNotIn(""+filter+"_"+dimid+"",value);
                $("#ol"+filter+"_"+dimid).append(internelement);
                $("#LIKE_selFilter_"+dimid).val("");
            }

        }
    });

}
function onkeyUpNotLikefun(dimid,elemId,selId){
    var filter="NOTLIKE";
    $("#NOTLIKE_selFilter_"+dimid).keyup(function(event){
        if(event.keyCode == 13){

            var value=$("#NOTLIKE_selFilter_"+dimid).val();
            var prevValJson=$("#"+filter+'_'+dimid).val();
            var prevVal=new Array();
            if(prevValJson.length >0){
                prevVal=eval('('+prevValJson+')');
            }
            if(value != "" || value.length >0){
                prevVal.push(value);
                var jsonVal=JSON.stringify(prevVal);

                $("#"+filter+'_'+dimid).val(jsonVal);
                var internelement=createDynamicElementForNotIn(""+filter+"_"+dimid+"",value);
                $("#ol"+filter+"_"+dimid).append(internelement);
                $("#NOTLIKE_selFilter_"+dimid).val("");
            }

        }
    });

}

function showkpiTableProperties(ctxPath,pageSize,reportId){
    pageSize='25';
    parent.$("#kpidispTabProp").dialog({
        bgiframe: true,
        autoOpen: false,
        height: 290,
        width: 510,
        modal: true,
        position:'justify',
        title:"Dashboard Properties"
    });
    //
    //    $.ajax({
    //       url:ctxPath+'/reportViewer.do?reportBy=chkforUserPrevilages',
    //       success:function(data){
    //            var jsonVar=eval('('+data+')')
    //            var userType=jsonVar.userType;
    //           if(userType=="Admin"){
    parent.$("#kpidispTabProp").dialog('open');
    var frameObj=parent.document.getElementById("kpidispTabPropFrame");
    var source =ctxPath+'/KpiTableProperties.jsp?reportId='+reportId+"&slidePages="+pageSize+"$ctxPath="+ctxPath;
    frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
    frameObj.src=source;
//           }else{
//              alert("You do not have the sufficient previlages")
//           }
//       }});


}
function editDashBoardTable(ctxPath,pageSize,reportId){
    parent.$("#editDBTable").dialog({
        bgiframe: true,
        autoOpen: false,
        position: 'justify',
        modal: true,
        title:"Dashboard Properties"
    });
    parent.$("#editDBTable").dialog('open');

}

function removeFilter(dimId,divId,value){
    var prevValues=eval('('+document.getElementById(dimId).value+')')
    prevValues.splice(prevValues.indexOf(value),1);

    //    if(prevValues.length ==0){
    //        prevValues.push("All");
    ////          var elmentContent=createDynamicElement(dimId,value);
    //        $("#"+root).append(createDynamicElement(dimId,"All"));
    //
    //    }
    $("#"+dimId).val(JSON.stringify(prevValues));
    var divElemId=document.getElementById(divId);
    divElemId.parentNode.removeChild(divElemId);

}
function createDynamicElementForNotIn(dimId,value){
    var internDivId=dimId+"_"+value.toString().replace("%","");
    var internElement="<div id='"+internDivId+"' class='newparamView' style='width:"+(10*(value.length))+";float:left;'>"
    internElement +="<table><tr>";
    internElement +="<td><span class='newParamName' >"+value+"</span></td>";
    internElement +="<td ><a href='javascript:removeFilter(\""+dimId+"\",\""+internDivId+"\",\""+value.toString().replace("%","")+"\")' style='float:left' class='ui-icon ui-icon-close' ></a></td>";
    internElement +="</tr></table>";
    internElement +="</div>";
    return internElement;
}
//added by Nazneen
function changeOrder(order,Name){
    var eleid=order.split("_");
    var htmlVar = "";
    if(eleid[0]=='asc') {
        htmlVar+="<img alt=\"Desc\" title=\"Descending Order\" src=\"images/doubleDownArrow.gif\" onclick=\"changeOrder('desc_"+Name+"','" + Name + "')\"><input type=\"hidden\" id='orderVal_"+Name+"' name='orderVal_"+Name+"' value=\"orderDesc_"+Name+"\">";
    }
    else {
        htmlVar+="<img alt=\"Asc\" title=\"Ascending Order\" src=\"images/doubleUpArrow.gif\" onclick=\"changeOrder('asc_"+Name+"','" + Name + "')\"><input type=\"hidden\" id='orderVal_"+Name+"' name='orderVal_"+Name+"' value=\"orderAsc_"+Name+"\">";
    }
    $("#order_"+Name).html(htmlVar);
}
function fromdate(date){
    var tdate=date.toString().substring(0, 15);
    var dateArr=new Array()
    dateArr=tdate.split(" ");
    if(dateArr[3]!="00:00"){
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[3]);
        parent.$('#fromdate').val(xyz)
        dateclick()
        parent.$("#field1").html(dateArr[1]+"'"+dateArr[3].substring(2))
        parent.$("#field2").html(dateArr[2])
        parent.$("#field3").html(dateArr[0])
    }else{
        //                                 var date= new Date(year, month, 1);
        var tdate=date.toString();
        dateArr=tdate.split(" ");
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[5]);
        parent.$('#fromdate').val(xyz)
        dateclick()
        parent.$("#field1").html(dateArr[1]+"'"+dateArr[5].substring(2))
        parent.$("#field2").html(dateArr[2])
        parent.$("#field3").html(dateArr[0])
    }

}
function todate(date){
    var moth = date.getMonth();
    var yer = date.getFullYear();

    var lastDay = new Date(yer, moth + 1, 0);
    var tdate=lastDay.toString().substring(0, 15);
    var dateArr=new Array()
    dateArr=tdate.split(" ");
    if(dateArr[3]!="00:0"){
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[3]);
        parent.$('#todate').val(xyz)
        dateclick()
        parent.$("#tdfield1").html(dateArr[1]+"'"+dateArr[3].substring(2))
        parent.$("#tdfield2").html(dateArr[2])
        parent.$("#tdfield3").html(dateArr[0])
    }else{
        var tdate=lastDay.toString();
        dateArr=tdate.split(" ");
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[5]);
        parent.$('#todate').val(xyz)
        dateclick()
        parent.$("#tdfield1").html(dateArr[1]+"'"+dateArr[5].substring(2))
        parent.$("#tdfield2").html(dateArr[2])
        parent.$("#tdfield3").html(dateArr[0])
    }
}
function comparefrom(date){
    var tdate=date.toString().substring(0, 15);
    var dateArr=new Array()
    dateArr=tdate.split(" ");
    if(dateArr[3]!="00:00"){
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[3]);
        parent.$('#comparefrom').val(xyz)
        dateclick()
        parent.$("#cffield1").html(dateArr[1]+"'"+dateArr[3].substring(2))
        parent.$("#cffield2").html(dateArr[2])
        parent.$("#cffield3").html(dateArr[0])
    }else{
        //                                var date= new Date(year, month, 1);
        var tdate=date.toString();
        dateArr=tdate.split(" ");
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[5]);
        parent.$('#comparefrom').val(xyz)
        dateclick()
        parent.$("#cffield1").html(dateArr[1]+"'"+dateArr[5].substring(2))
        parent.$("#cffield2").html(dateArr[2])
        parent.$("#cffield3").html(dateArr[0])
    }
}
function compareto(date){
    var moth = date.getMonth();
    var yer = date.getFullYear();
    var lastDay = new Date(yer, moth + 1, 0);
    var tdate=lastDay.toString().substring(0, 15);
    var dateArr=new Array()
    dateArr=tdate.split(" ");
    if(dateArr[3]!="00:0"){
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[3]);
        parent.$('#compareto').val(xyz)
        dateclick()
        parent.$("#ctfield1").html(dateArr[1]+"'"+dateArr[3].substring(2))
        parent.$("#ctfield2").html(dateArr[2])
        parent.$("#ctfield3").html(dateArr[0])
    }else{
        var tdate=lastDay.toString();
        dateArr=tdate.split(" ");
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[5]);
        parent.$('#compareto').val(xyz)
        dateclick()
        parent.$("#ctfield1").html(dateArr[1]+"'"+dateArr[5].substring(2))
        parent.$("#ctfield2").html(dateArr[2])
        parent.$("#ctfield3").html(dateArr[0])
    }
}
function standarddate(date)
{
    //    alert(date)
    var moth = date.getMonth();
    var yer = date.getFullYear();
    var lastDay = new Date(yer, moth + 1, 0);
    var tdate=lastDay.toString().substring(0, 15);
    var dateArr=new Array()
    dateArr=tdate.split(" ");
    if(dateArr[3]!="00:0"){
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[3]);
        parent.$('#perioddate').val(xyz)
        dateclick()
        parent.$("#pfield1").html(dateArr[1]+"'"+dateArr[3].substring(2))
        parent.$("#pfield2").html(dateArr[2])
        //$("#pfield2").val(lastDay)
        parent.$("#pfield3").html(dateArr[0])
    }else{
        var tdate=lastDay.toString();
        dateArr=tdate.split(" ");
        var xyz = dateArr[0].toString().concat(",").concat(dateArr[2]).concat(",").concat(dateArr[1]).concat(",").concat(dateArr[5]);
        parent.$('#perioddate').val(xyz)
        dateclick()
        parent.$("#pfield1").html(dateArr[1]+"'"+dateArr[5].substring(2))
        parent.$("#pfield2").html(dateArr[2])
        //$("#pfield2").val(lastDay)
        parent.$("#pfield3").html(dateArr[0])
    }
}
//edited by manik
//function generateJsonData(repId,graphFlag){
function generateJsonData(repId,graphFlag,grid1,pageChange){
    $("#loading").show();
    isInit = {};
    uscity = {};
    //   $("#content_1").hide();

    if(grid1!==grid){
        grid=grid;
    }else{
        grid=grid1;
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
    startValgbl=1;
    var htmlVar="";
    if(typeof pageChange==='undefined'){
        pageChange='';
    }
    
    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    $.post(
        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&pageChange="+pageChange,
        function(data) {
            //            $("#loading").hide();
            if(data=="false"){
//                if(typeof pageChange!='undefined'){
//                    htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
//
//                $("#xtendChartTD").append(htmlVar);
//                $("#xtendChartTD").show();
//                $("#loading").hide();
//                return ;
//                }
                htmlVar="";

                $("#loading").hide();
                $("#xtendChartTD").html('');
                $("#content_Drag").html('');
    var textDivHeight = screen.height;
                 if(parent.userType == "ANALYZER"){
       $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;float:right;height:"+textDivHeight+"px;overflow:auto;'></div>");
       }else{
       $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;background-color: rgb(255, 255, 255); box-shadow: 0px 0px 2px 2px rgb(197, 66, 66); margin-top: 1px; border-radius: 5px;width:12%;float:right;height:460px;overflow:auto;position:fixed;margin-top:;right: 20px;box-shadow:0px 1px 4px 2px rgb(227, 221, 221);margin-right: 2px;display:none;background-color: #FFF;z-index:11'></div>");
           
       }
                htmlVar +="<div style='position:fixed;margin-left:2%'>"
                htmlVar += "<span title='Parameters' align='center' style='font-size:16px;white-space:nowrap; text-align:center;'>"+translateLanguage.Parameters+"</span></br>";
                htmlVar += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>"+translateLanguage.Drag_Parameters+"</span>";
                htmlVar +="</div>"
                var viewByIds = JSON.parse(JSON.stringify($("#viewbyIds").val()));
                var viewBy = JSON.parse(JSON.stringify($("#viewby").val()));
                var measures = JSON.parse(JSON.stringify($("#measure").val()))
                htmlVar += "<br>"
                htmlVar += "<div style='position:fixed; margin-top: 1%'>"
                htmlVar += "<div class = 'hiddenscrollbar' style=''>"
                htmlVar += "<div class = 'innerhiddenscrollbar' style='height:150px;width:150px'>"
                htmlVar += "<ul class='rightDragMenu' style='list-style-type:none;pading:2px'>"
                for(var i in viewByIds ){
                    if(viewBy[i].length>15){
                        htmlVar += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='"+viewBy+"'  draggable='true'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center'  title='"+viewBy[i]+"'>"+viewBy[i].substring(0, 15)+"..</span></li>"
                        htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                    }else{
                        htmlVar += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='"+viewBy+"'  draggable='true'  ondragstart='drag(event,this.id)'><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+viewBy[i]+"'>"+viewBy[i]+"</span></li>"
                        htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                    }

                }
                htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
                htmlVar += "</ul>"
                htmlVar += "</div>"
                htmlVar += "</div>"
                htmlVar +="<div  style='position:fixed;margin-top:;'>"
                htmlVar += "<span title='Measures' align='center' style='font-size:16px;white-space:nowrap;text-align:center;'>"+translateLanguage.Measures+"</span></br>";
                htmlVar += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>"+translateLanguage.Drag_Measures+"</span>";
                htmlVar +="</div>";
                htmlVar +="<div style='position:fixed;margin-top:2%'>"
                htmlVar += "<div class = 'hiddenscrollbar' style=''>"
                htmlVar += "<div class = 'innerhiddenscrollbar'  style='height:180px;width:150px'>"
                htmlVar += "<ul style='list-style-type:none' class='rightDragMenu'>"
                for(var j in measures ){
                    if(measures[j].length>20){
                        htmlVar += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='"+measures[j]+"'  draggable='true' onclick='measureDriven(this.id)' ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+measures[j]+"'>"+measures[j].substring(0, 15)+"..</span></li>"
                    }else{
                        htmlVar += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' id='"+measures[j]+"'  draggable='true' onclick='measureDriven(this.id)' ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+measures[j]+"'>"+measures[j]+"</span></li>"
                    }
                }
                htmlVar += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
            htmlVar += "</ul>"
            htmlVar += "</div>"
            htmlVar += "</div>"
            htmlVar += "</div>"
            //               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';

            var textHtml = "";
    textHtml += "<div style='width:98%;height:"+(textDivHeight-10)+"px;border:1px solid grey;overflow:hidden'>";
    textHtml += "</div>";
          $("#noData").hide();
            $("#xtendChartTD1").show();
            if(parent.userType == "ANALYZER"){
                
         $("#content_Drag").html('');
         $("#content_Drag").append();
            }else{
            $("#content_Drag").append(htmlVar);
                
            }
            $("#xtendChartssTD").show();
        //
        //                $("#loading").hide();
        //               htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
        //               $("#xtendChartTD").append(htmlVar);
        //                $("#xtendChartTD").show();
        //                $("#loading").hide();
        }
        else{
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
            var reportPageMapping=JSON.parse(JSON.stringify(meta["reportPageMapping"]));
            var pages=Object.keys(reportPageMapping[repId]);
            var pageSequence = JSON.parse(data)["pageSequence"];
            currentPage=JSON.parse(data)["currentPage"];
//            for(var i in pages){
//                if(reportPageMapping[repId][pages[i]]["pageSequence"]===pageSequence[0]){
//                    currentPage=pages[i];
//                }
//            }
            pageSequence.sort();
            parent.$("#pageSequence").val(JSON.stringify(pageSequence));
            parent.$("#pageList").empty();
            if(parent.$("#morePages").html().trim()==''){
                parent.$("#morePages").append("More");
            }
            if(pages.length<=3){
                    parent.$("#pageList").css("width","40%");
                    parent.$("#morePages").hide();
                }
                else{
                    parent.$("#pageList").css("width","40%");
                    parent.$("#morePages").show();
                }
                 if(pages.length>1){
            for(var i=0;i<(pages.length<=3?pages.length:3);i++){
                var pageId='';
                for(var j in pages){
                    if(reportPageMapping[repId][pages[j]]["pageSequence"]==pageSequence[i]){
                        pageId=pages[j];
                        break;
                    }
                }
//                if(reportPageMapping[repId][pageId]["pageSequence"]==="1" && typeof currentPage==='undefined'){
//                    currentPage=pageId
//                }
                if(currentPage===pageId){
                    parent.$("#pageList").append("<div id='"+pageId+"' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>"+reportPageMapping[repId][pageId]["pageLabel"]+"</div>");
                }
                else{
                    parent.$("#pageList").append("<div id='"+pageId+"' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>"+reportPageMapping[repId][pageId]["pageLabel"]+"</div>");
                }
            }
        $("#"+currentPage).css("border-bottom","3px solid #008cc9");
        }
            try{
                $("#tabs-1").tabs("refresh");
            }
            catch(e){
                alert(e);
            }
//            $("#timeMap").val(JSON.stringify(meta["timeMap"]));
        if (window.mgmtDBFlag === 'true') {
                    var gtResult;
                    $.ajax({
                        async: false,
                        type: "POST",
                        data: parent.$("#graphForm").serialize(),
                        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
                        success: function(data) {
                            gtResult = JSON.parse(JSON.parse(data)["data"]);
                            var tempMetaResult = JSON.parse(data)["meta"];
                            parent.$("#templateMeta").val(tempMetaResult);
                            showMagTemp(gtResult);
                        }
                    });
                }
         parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
            if(typeof parent.timeMapValue ==="undefined") {
                $("#timeMap").val(JSON.stringify(meta["timeMap"]));
            }
            if(typeof meta["draggableViewBys"]!="undefined" && meta["draggableViewBys"]!=""){
                var chartData = JSON.parse($("#chartData").val());
                var keys = Object.keys(meta["draggableViewBys"])
                var draggableView = []
                //                     for(var i in meta["draggableViewBys"] ){
                for(var j in meta["draggableViewBys"]){
                    //                        if(meta["draggableViewBys"][i]==chartData[keys[j]]["viewBys"]){

                    draggableView.push(meta["draggableViewBys"][j]);
                }
                $("#draggableViewBys").val(JSON.stringify(draggableView));
            }else{
                if(typeof $("#chartData").val()!="undefined" && $("#chartData").val()!=""){
                    var chartData = JSON.parse($("#chartData").val());
                    var keys = Object.keys(chartData)
                    var draggableView = []
                    for(var k in keys){
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
                async:false,
                type:"POST",
                data:
                $('#graphForm').serialize(),
                url:  'reportViewer.do?reportBy=getFilters&typegbl=graph',
                success:function(data) {
                    filterData=JSON.parse(data);
//                    filterDatagbl=filterData;
                     applyfiltersgraph();
                     if(savedfilter){
                         $("#filters1").val(JSON.stringify(meta["filterMap"]));
                    }
                         if(pageDrillValue!==''){
                var appliedFilterMap={};
                try{
                    appliedFilterMap=JSON.parse($("#filters1").val());
                     }
                catch(e){}
                var filterMap={};
                var filterList=[];
                if(typeof appliedFilterMap!=='undefined'){
                    filterMap=appliedFilterMap;
                    filterList.push(pageDrillValue.split(":")[0]);
                }
                else{
                    filterList.push(pageDrillValue.split(":")[0]);
                }
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(pageDrillValue.split(":")[0]);
                drillNext.push(pageDrillValue.split(":")[1]);
                filterMap[window.pageDrillViewById]=drillFirst;

//                filterMap[chartData[div]["viewIds"][0]] = drillFirst;
                if(typeof window.pageDrillViewById1 !=="undefined")
                    filterMap[window.pageDrillViewById1] = drillNext;
          
                
                pageDrillValue='';
                window.pageDrillViewById='';
                 var quickViewname = window.pageDrillViewByName;
                
             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time"); 
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time"); 
             }else {
                parent.$("#drillFormat").val("none");  
            }
                if(typeof window.globalFilterMap!=='undefined')
                         parent.$("#filters1").val(JSON.stringify(window.globalFilterMap))
                
                parent.$("#drills").val(JSON.stringify(filterMap));
                     }
                     
                    //                         generateChart(data);
                    //                     if(typeof globalFilter != "undefined"){
                    //      d               $("#filters1").val(JSON.stringify(globalFilter));
                    //                     }
                   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&initializeFlag=true"+"&action=localfilterGraphs", $("#graphForm").serialize(),
                        function(data){
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
              var resultset=data;
                            if(parent.$("#type").val()==="advance"){
                                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
                            }else{
                                  updatefiltersrow();
                                runtimeglobalfilters(JSON.stringify(meta["filterMap"]),'null',"graph");
    var data1= JSON.parse(resultset)["data"];
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
function generateJsonData1st(repId,graphFlag,grid1,pageChange){
    // $("#content_1").hide();

    grid=grid1;
//   $("#openGraphSection").dialog('close');
    //    $("#loading").show();
//    $("#reportTD1").hide();  commented by mayank on 20 Sep
    $("#xtendChartTD").html('');
    if (checkBrowser() == "ie" ) {
        $("#xtendChartssTD").html('');
    }
    $("#draggableViewBys").val('');
    $("#type").val("graph");
    graphTrendFlag = graphFlag;
    $("#drills").val("");
    $("#filters1").val("");
    $("#drillFormat").val("");
     startValgbl=1;
     if(typeof pageChange==='undefined'){
         pageChange='';
     }
    var htmlVar="";
    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    var pageLabel="Page 1";
//    $.post($("#ctxpath").val()+"/reportViewerAction.do?reportBy=createFirstPage&pageLabel="+pageLabel+"&reportId="+$("#graphsId").val(),
//        function(data){
//        }
//        );
    $.ajax({
        async:false,
        url: "reportViewerAction.do?reportBy=createFirstPage&pageLabel="+pageLabel+"&reportId="+$("#graphsId").val(),
        success: function(data){
                           currentPage='default'; 
    $.post(
        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
        function(data) {
            //                $("#loading").hide();
            if(data=="false"){

                htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';

                $("#xtendChartTD").append(htmlVar);
                $("#xtendChartTD").show();
                $("#loading").hide();
            }
            else{
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
                  var reportPageMapping=JSON.parse(JSON.stringify(meta["reportPageMapping"]));
                  var pages=Object.keys(reportPageMapping[repId]);
                  var pageSequence = JSON.parse(data)["pageSequence"];
                  currentPage=JSON.parse(data)["currentPage"];
//                  for(var i in pages){
//                      if(reportPageMapping[repId][pages[i]]["pageSequence"]===pageSequence[0]){
//                          currentPage=pages[i];
//                      }
//                  }
                  pageSequence.sort();
                  parent.$("#pageSequence").val(JSON.stringify(pageSequence));
                  parent.$("#pageList").empty();
                if(parent.$("#morePages").html().trim()==''){
                    parent.$("#morePages").append("More");
                }
                if(pages.length<=3){
                    parent.$("#pageList").css("width","40%");
                    parent.$("#morePages").hide();
                }
                else{
                    parent.$("#pageList").css("width","40%");
                    parent.$("#morePages").show();
                }
                if(pages.length>1){
            for(var i=0;i<(pages.length<=3?pages.length:3);i++){
                var pageId='';
                for(var j in pages){
                    if(reportPageMapping[repId][pages[j]]["pageSequence"]==pageSequence[i]){
                        pageId=pages[j];
                        break;
                    }
                }
//                if(reportPageMapping[repId][pageId]["pageSequence"]==="1" && typeof currentPage==='undefined'){
//                    currentPage=pageId
//                }
                if(currentPage===pageId){
                    parent.$("#pageList").append("<div id='"+pageId+"' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='border-bottom:3px solid #008cc9;width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>"+reportPageMapping[repId][pageId]["pageLabel"]+"</div>");
                }
                else{
                    parent.$("#pageList").append("<div id='"+pageId+"' value='Dashboard' onclick='getSelectedPage(this.id)' class=' gFontFamily ' style='width: 30%;float: left;text-align: center;padding: 10px 0px 0px;cursor:pointer;'>"+reportPageMapping[repId][pageId]["pageLabel"]+"</div>");
                }
            }
                }
            try{
                $("#tabs-1").tabs("refresh");
            }
            catch(e){
            alert("exception!");
        }
            parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
            if(typeof parent.timeMapValue ==="undefined") {
                $("#timeMap").val(JSON.stringify(meta["timeMap"]));
            }
                if (window.mgmtDBFlag === 'true') {
                    var gtResult;
                    $.ajax({
                        async: false,
                        type: "POST",
                        data: parent.$("#graphForm").serialize(),
                        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
                        success: function(data) {
                            gtResult = JSON.parse(JSON.parse(data)["data"]);
                            var tempMetaResult = JSON.parse(data)["meta"];
                            parent.$("#templateMeta").val(tempMetaResult);
                            showMagTemp(gtResult);
                        }
                    });
                }
                if(typeof meta["draggableViewBys"]!="undefined" && meta["draggableViewBys"]!=""){
                    if(typeof $("#chartData").val()!="undefined" && $("#chartData").val()!=""){
                        var chartData = JSON.parse($("#chartData").val());
                        var keys = Object.keys(chartData)
                        var draggableView = []
                    
                        //                     for(var i in meta["draggableViewBys"] ){
                        for(var j in keys){
                       
                            //                        if(meta["draggableViewBys"][i]==chartData[keys[j]]["viewBys"]){
                            draggableView.push(chartData[keys[j]]["viewBys"][0]);
                        //                        }
                        //                         }
                        }
                        $("#draggableViewBys").val(JSON.stringify(draggableView));
                    }
                }else{
                if(typeof $("#chartData").val()!="undefined" && $("#chartData").val()!=""){
                    var chartData = JSON.parse($("#chartData").val());
                    var keys = Object.keys(chartData)
                    var draggableView = []
                    for(var k in keys){
                        draggableView.push(JSON.stringify(chartData[keys[k]]["viewBys"][0]));
                    }
                    
                    $("#draggableViewBys").val(JSON.stringify(draggableView));
                }

            }

            //                 if(typeof meta["filterMap"] != "undefined" && meta["filterMap"] != ""){
            //                    globalFilter = JSON.stringify(meta["filterMap"]);
            //                }
            if(typeof reportToTrendFilter !=="undefined"){
                $("#filters1").val(JSON.stringify(reportToTrendFilter))
            }else{
                $("#filters1").val(JSON.stringify(meta["filterMap"]));
            }

             $.ajax({
                async:false,
                type:"POST",
                data:
                $('#graphForm').serialize(),
                url:  'reportViewer.do?reportBy=getFilters&typegbl=graph',
                success:function(data) {
                    filterData=JSON.parse(data);
//                    filterDatagbl=filterData;
                     applyfiltersgraph();
                      if(savedfilter){
                         $("#filters1").val(JSON.stringify(meta["filterMap"]));
                     }
                    //                         generateChart(data);
                    //                     if(typeof globalFilter != "undefined"){
                    //                     $("#filters1").val(JSON.stringify(globalFilter));
                    //                     }
                    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&initializeFlag=true"+"&action=localfilterGraphs", $("#graphForm").serialize(),
                        function(data){
                            var meta = JSON.parse(JSON.parse(data)["meta"]);
                            $("#viewby").val(JSON.stringify(meta["viewbys"]));
                            $("#measure").val(JSON.stringify(meta["measures"]));
                            $("#chartData").val(JSON.stringify(meta["chartData"]));
                            $("#numberFormatMap").val(JSON.stringify(meta["numberFormatMap"]));
                              $("#currencyType").val(meta["currencyType"]);
                             $("#timeDetailsArray").val(JSON.stringify(meta["Timedetails"]));
//                            $.ajax({
//        async:false,
//        type:"POST",
//        data:
//        $('#graphForm').serialize(),
//        url:  'reportViewer.do?reportBy=saveXtCharts',
//
//        success:function(saveData) {
           
            var resultset=data;
                             var data1= JSON.parse(resultset)["data"];
//                             applyfiltersgraph();
if(savedfilter){
                         $("#filters1").val(JSON.stringify(meta["filterMap"]));
                     }

                    if(typeof viewByFilterID !=="undefined"){
                        applyFilterFromReport(viewByFilterID,viewByFilterName,fList)

         setTimeout(updateglobalfilters(repId,graphFlag,"graph"), 20000);
          $('#rightDiv1table').hide();
             if (checkBrowser() == "ie") {
                
                   $('#rightDiv1').hide();
                    $("#xtendChartssTD").css({'margin-top':'0px'});
             }else{
                         $('#rightDiv1').show();
                    }
                    }
                    else if(typeof valueToParse !=="undefined"){
                        drillWithinTabs1();
                    }
                    else{
                         updatefiltersrow();
                        runtimeglobalfilters(JSON.stringify(meta["filterMap"]),'null',"graph");
                        generateChartloop(data1);
                    }
//        }
//    })
                          
                        });
        }
    });
        }
    });
          }
    });   
            $("#openGraphSection").dialog('close');
}

function generateJsonDataReport() {
    var chartData = {};
    var repId = $("#graphsId").val();
    var numOfCharts = 0;
    $("#numOfCharts").val(parseInt(numOfCharts+1));
    $("#currLine").val(1);
    var line={};
    var line1=1;
    line["line1"]=line1;
    $("#lines").val(JSON.stringify(line));
    if (typeof $("#chartData").val() === "undefined" || $("#chartData").val() === "") {
        var chartDetails = {};
        //        chartData["numOfCharts"] = numOfCharts + 1;
        chartDetails["chartType"] = "Pie";
        chartDetails["records"] = "20";
        chartData["chart" + parseInt(numOfCharts + 1)] = chartDetails;
        $("#chartData").val(JSON.stringify(chartData));
    }

    //     $.ajax({
    //                   type: 'GET',
    //                       async: false,
    //                       cache: false,
    //                       timeout: 30000,
    //                   url:'reportViewer.do?reportBy=getDataCall',
    //                   success: function(data){
    ////                        parent.document.getElementById(templId).innerHTML=img;
    //                   }
    //               })
    $.post(
        'reportViewer.do?reportBy=generateJsonData&repId=' + repId,
        function(data) {
            var json = JSON.parse(data);
            var jsonData = json["chartData"];
            var nameMap = json["nameMap"];
            var viewBys = json["viewBys"];
            var measures = json["measures"];
            var chartMeta = json["chartMeta"];
            var numOfRecords = json["numOfRecords"];
            //                var lines = json["lines"];
            $("#numOfRecords").val(numOfRecords);
            //                $("#lines").val(lines);
            if(chartMeta =="undefined" || chartMeta==""){
                //                }
                $("#reportTD1").hide();
                $("#xtendChartTD").show();
                //                $("#xtendChartTD").html("");
                var currData = [];
                for (var i = 0; i<(jsonData.length < 15 ? jsonData.length : 15); i++) {
                    currData.push(jsonData[i]);
                }
                var htmlVar = "";
                htmlVar += "<div id='chart1' style='float:left;margin-left:5px;'>";
                htmlVar += "<table id='tablechart1' align='left' class='dashHeader'>" +
                "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
                "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart1' onclick='editViewBys(this.name)'/>" +
                "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
                "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
                "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart1' onclick='openCharts(this.name)' />" +
                "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
                //                        "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
                //                        "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
                "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamechart1'>Chart1</span></a></td></tr></table>";
                htmlVar += "<div id='chart1Tab' style='border:1px solid grey'></div></div>";
                $("#xtendChart").html("");
                $("#xtendChart").append(htmlVar);
                var wid = $(window).width()-10;
                var hgt = $(window).height();
                //                         chartData["charts" + parseInt(numOfCharts + 1)]["wid"] = wid;
                //                         chartData["charts" + parseInt(numOfCharts + 1)]["hgt"] = hgt;
                $("#chartData").val(JSON.stringify(chartData));
                buildPie("chart1Tab", currData, viewBys, measures,wid,hgt);
                //              buildBar("xtendChart1",currData,viewBys,measures);
                $("#reportTD1").hide();
                $("#xtendChartTD").show();
            }

            else{
                getReport(JSON.parse(chartMeta),jsonData,viewBys, measures);
            }
        });
}
function changeviewby(data,chartnme,regid,dashletid){
    divcount=regid;
    divcount;
    var  chartid='OLAPGraphRegion';
    action1='OLAPGraphRegion';
    graphData=JSON.parse(data);
    //generateSingleChart1(JSON.stringify(jsondata),chartid);
    setTimeout(generateChartloop(data), 20000);
//  generateChartloop(data);
}

function generateJsonDataOneview1(dashletid,oneviewID,regid,chartname,action,reportId,repname,viewbyame,viewById){
    parent.$("#graphsId").val(reportId);
    regidch=dashletid;
    parent.$("#graphName").val(repname);
    //                     parent.$("#busrolename").val(rolename);
    //                      parent.$("#chartname").val(chartname);
    //                        generateJsonDataOneview(dashletid,oneviewID,regid,chartname,action);
    if(action=='olap'){
        var chartData = JSON.parse(parent.$("#chartData").val());
        var rowViewByArray=new Array();
        var rowViewNamesArr=new Array();
        var rowMeasArray=new Array();
        rowViewByArray.push(viewById);
        rowViewNamesArr.push(viewById);
        var MeasBy=  chartData[chartname]["meassures"];
        rowMeasArray.push(MeasBy);

        chartData[chartname]["viewBys"][0]=viewbyame;
        //   chartData[chartname]["viewIds"][0]=viewById;
        //    parent.$("#chartData").val(JSON.stringify(chartData));
        ////               generateChart(jsondata);
        //                changeviewby(jsondata,chartname,regid,"OLAPGraphRegion")
        //            }
        //            });
        chartData[chartname]["viewIds"][0]=viewById;
        //parent.$("#chartData").val(JSON.stringify(chartData));
        chartData = JSON.parse(parent.$("#chartData").val());
        parent.$("#chartData").val(JSON.stringify(chartData));
        $.ajax({
            async:false,
            type:"POST",
            data: parent.$("#oneviewgraphForm").serialize+"&rowViewByArray="+encodeURIComponent(rowViewByArray)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(rowViewNamesArr)+"&rowMeasArray="+encodeURIComponent(rowMeasArray)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(chartData))+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartname="+chartname,
            url: ctxpath+"/reportViewer.do?reportBy=drillCharts&fromoneview=true&oneviewID="+oneviewID+"&regid="+regid+"&action="+action,
            success: function(data){
                parent.$("#chartData").val(JSON.stringify(chartData));
                //                               parent.generateSingleChart(data,chartId);
                changeviewby(data,chartname,regid,"OLAPGraphRegion")

            }
        }
        )

    }

    else{
        action1='OLAPGraphRegion';
        generateJsonDataOneview(chartname,oneviewID,regid,'null','open',grid,'OLAPGraphRegion')
    //                       generateJsonDataOneview(dashletid,oneviewID,regid,chartname,"open")
    }
}
//sandeep for saving jsons for oneview charts
function saveoneviewjsons(oneviewid,gridster,fromviewer){
    //   var gridster=parent.$("#gridsterobj").val();
  
    grid=gridster;
    // $.post(parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneViewIdValue='+oneviewid,
    //                function(data){alert(data)

    $.ajax({
        async:false,
        type:"POST",
        //                          data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+parent.$("#graphName").val(),

        url: parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneviewid='+oneviewid,

        success: function(data){
            var jsonVar=eval('('+data+')');
            var  regionids=jsonVar.regionids;
            var  reportids=jsonVar.reportids
            var  repnames=jsonVar.repnames
            var  chartnames=jsonVar.chartnames;
            var  busrolename=jsonVar.busrolename;
            var  idArr=jsonVar.idArr;
            var  drillviewby=jsonVar.drillviewby;
            for(var i=0;i<regionids.length;i++){
                parent.$("#graphsId").val(reportids[i]);
                parent.$("#graphName").val(repnames[i]);
                parent.$("#busrolename").val(busrolename[i]);
                parent.$("#chartname").val(chartnames[i]);
                parent.$("#oneViewId").val(oneviewid);
                var idArr1=idArr[i];
                if(fromviewer!='null'&& fromviewer=='true'){
                    $.ajax({
                        async:false,
                        type:"POST",
                        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

                        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts&fromoneview=true",

                        success: function(data){
                            $("#loading").hide();
                            if(data=="false"){

                            }
                            else{
                                var jsondata = JSON.parse(data)["data"];

                                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                                var meta = JSON.parse(JSON.parse(data)["meta"]);
                                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                                parent.$("#drilltype").val((meta["drillType"]));
                            //                parent.$("#filters1").val(JSON.stringify(meta["filterMap"]));

                            }
                        }
                    });

                }
                else{


                    $.ajax({
                        async:false,
                        type:"POST",
                        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

                        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts&fromoneview=true",

                        success: function(data){
                            $("#loading").hide();
                            if(data=="false"){

                            }
                            else{
                                var jsondata = JSON.parse(data)["data"];

                                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                                var meta = JSON.parse(JSON.parse(data)["meta"]);
                                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                                parent.$("#drilltype").val((meta["drillType"]));
                            //                parent.$("#filters1").val(JSON.stringify(meta["filterMap"]));

                            }
                        }
                    });
                }
                if(typeof   parent.$("#chartData").val()!="undefined" && parent.$("#chartData").val()!="" ){
                    //if($("#type").val()=="graph" || $("#type").val()=="trend"){
                    saveGridposoneview(i);
                //           }
                }
                // var myVar= setTimeout(function(){ generateJsonDataOneview("Dashlets-"+regionids[i],idvalue,regionids[i],chartnames[i],"add")}, 1000);
                //clearTimeout(myVar);
                // alert( parent.$("#filters1").val())
                var  filterenabe=parent.$("#enable"+regionids[i]).val();
                var chartData = JSON.parse(parent.$("#chartData").val());
                parent.$("#chartData").val(JSON.stringify(chartData));
                $.ajax({
                    async:false,
                    type:"POST",
                    data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&filters1="+parent.$("#filters1").val(),

                    url: parent.ctxpath+"/oneViewAction.do?templateParam2=saveoneviewjson&fromoneview=true&oneviewID="+oneviewid+"&regid="+regionids[i]+"&filterenabe="+encodeURIComponent(filterenabe),

                    success: function(data){
                    //                                alert(data)

                    }
                });


            }
        }
    });
    if(fromviewer!=''&& fromviewer=='true'){

    }else{
        parent.home();
        alert("Oneview Saved Successfully!")

    }

}

//sandeep
function generateglobalDataOneview(gridster1,oneviewid,regid1,chartname1,action,data) {
    regid= regid1
    grid=gridster1;
    divcount=regid1;
    fromoneview='true';
    var chartid=parent.$("#chartname").val();
    parent.$("#chartname").val(chartid);
    generateChartloop(data);

}
var regid=1;
var action1;
var enablefilter;
function generateJsonDataOneview(chartid,oneviewid,regid1,oneViewName,action,gridster1,fromviewer){
    grid=gridster1;
    var chartData = {};
    var repId = parent.$("#graphsId").val();
    var repname = parent.$("#graphName").val();
    //    parent.$("#chartname").val(chrtname);
    //    chartname=chartname1;
    chartid=parent.$("#chartname").val();
    parent.$("#chartname").val(chartid);
    var numOfCharts = 0;
    graphTrendFlag="Graph"
    oneviewid1=oneviewid;
    parent.$("#type").val("graph");

    regid= regid1
    //     }
    //if(fromviewer=='save'){
    divcount=regid1
    //}
    action1=action;
    parent.$("#drills").val("");
    //parent.$("#filters1").val("");
    $("#numOfCharts").val(parseInt(numOfCharts+1));
    $("#currLine").val(1);
    var line={};
    var line1=1;
    fromoneview='true';
    line["line1"]=line1;
    $("#lines").val(JSON.stringify(line));
    $.ajax({
        async:false,
        type:"POST",
        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+repId+"&oneviewid="+oneviewid+"&regid="+regid+"&oneviewName="+encodeURIComponent(oneViewName)+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&fromoneview=true&action="+encodeURIComponent(action)+"&oneviewtime="+parent.oneviewTimecheck+"&fromviewer="+encodeURIComponent(fromviewer),
        success: function(data){
            //     $.post(
            //            'reportViewer.do?reportBy=getAvailableCharts&fromoneview=true&reportId=' + repId+"&reportName="+parent.$("#graphName").val(), parent.$("#oneviewgraphForm").serialize(),
            //            function(data) {
            $("#loading").hide();
            if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                filterData = JSON.parse(data)["getFilters"];

                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                parent.$("#timedetails").val(JSON.parse(data)["Timedetails"]);
                //                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));
                   parent.$("#timeMap").val(JSON.stringify(meta["timeMap"]));
                enablefilter=meta["enablefilter"];
                //               generateChartloop(jsondata)
                //               alert("open sucess")
                //               var gridsPos=grid.serialize1();
                if(action=='add'){
                    var chartData = JSON.parse(parent.$("#chartData").val());
                    //     if(chartData[chartid]["chartType"]=="Standard-KPI"){
                    //
                    //     }else{
                    // chartData[chartid]["row"] = 'undefined';
                    //     }
                    parent.$("#chartData").val(JSON.stringify(chartData));
                }
                if(fromviewer=='OLAPGraphRegion'){
                    divcount=regid1;
                    divcount;
                    chartid='OLAPGraphRegion';
                    action1=fromviewer;
                    //generateSingleChart1(JSON.stringify(jsondata),chartid);
                    setTimeout(generateChartloop(jsondata), 20000);
                }else{
                    if(action=='customtimeaggre'||action=='GO'){
                        divcount=regid1;
                        divcount;
                        fromoneview='true';
                        var divid="chart"+regid1
                        graphData=JSON.parse(jsondata);
                        //generateSingleChart1(JSON.stringify(jsondata),chartid);
                        generateSingleChart(jsondata,divid);
                    }else{
                        setTimeout(generateChartloop(jsondata), 20000);
                    }
                }
            }
        }
    });

}
//Added By sandeep For Local Refresh
function generateJsonDataNewOneview(chartid,oneviewid,regid1,type,fromviewer) {
    //grid=gridster1;
    var type=type;
    $.ajax({
        async:false,
        type:"POST",
        data: parent.$("#oneviewgraphForm").serialize()+"&oneviewid="+oneviewid+"&regid="+regid1,
        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&fromoneview=true&action=localrefresh&oneviewtime="+parent.oneviewTimecheck,
        success: function(data){
            $("#loading").hide();
            if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                var chartname = JSON.parse(data)["chartname"];
                parent.$("#chartname").val(chartname);
                //                alert("data@@@"+jsondata)
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                //                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));
                //               generateChartloop(jsondata)
                //               alert("open sucess")
                //               var gridsPos=grid.serialize1();

                graphData=JSON.parse(jsondata);
                if(type=="localrefresh"){
                    divcount=regid1;
                    divcount;
                    fromoneview='true';
                    //generateSingleChart1(JSON.stringify(jsondata),chartid);
                    generateSingleChart(jsondata,chartid);
                }
                else
                    setTimeout(generateChartloop(jsondata), 20000);
            }
        }
    });
//divcount="undefined";
}


function addNewCharts(reportId) {
    var chartData = JSON.parse($("#chartData").val());
    var repId = $("#graphsId").val();
    var numOfCharts=$("#numOfCharts").val();
    numOfCharts=parseInt(numOfCharts)+1;
    var currLine = $("#currLine").val();
    currLine = parseInt(currLine)+1;
    $("#currLine").val(currLine)
    $("#numOfCharts").val(numOfCharts);

    var line={};
    if(typeof $("#lines").val()!="undefined" && $("#lines").val()!=""){
        line = JSON.parse($("#lines").val());
    }
    var keys = Object.keys(line);
    var currLineIndex = keys[keys.length-1];
    line[currLineIndex]=parseInt(line[currLineIndex])+1;
    $("#lines").val(JSON.stringify(line));
    var chartDetails = {};
    //    chartData["numOfCharts"] = parseInt(numOfCharts + 1);
    chartDetails["chartType"] = "Pie";
    chartDetails["records"] = "20";
    chartData["chart" + parseInt(numOfCharts)] = chartDetails;
    $("#chartData").val(JSON.stringify(chartData));
    var num = parseInt(numOfCharts)+1;

    $.post(
        'reportViewer.do?reportBy=generateJsonData&repId=' + reportId,
        function(data) {
            var json = JSON.parse(data);
            var chartData = json["chartData"];
            var nameMap = json["nameMap"];
            var viewBys = json["viewBys"];
            var view = viewBys;
            var measures = json["measures"];
            $("#reportTD1").hide();
            $("#xtendChartTD").show();
            var currData = [];
            for (var i = 0; i<(chartData.length < 15 ? chartData.length : 15); i++) {
                currData.push(chartData[i]);
            }
            var htmlVar = "";
            $("#xtendChart").html("");
            var currKeyIndex=0;
            var keyIndex = keys[0].replace("line","","gi");
            var widFact=keyIndex;
            for(var j=1;j<=num;j++){
                var wid = $(window).width()-50;
                if(keyIndex<=j){
                    wid=wid/widFact;
                }
                else{
                    currKeyIndex++;
                    widFact=keys[currKeyIndex].replace("line","","gi");
                    keyIndex=parseInt(keys[currKeyIndex].replace("line","","gi"))+keyIndex;
                    wid=wid/widFact;
                }
                htmlVar += "<div id='chart" + j + "' style='margin-left:5px;float:left'>";
                htmlVar += "<table id='tablechart" + j + "' align='left' class='dashHeader'>" +
                "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
                "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart"+j+"' onclick='editViewBys(this.name)'/>" +
                "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
                "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
                "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart"+j+"Tab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
                "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
                "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
                "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
                "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\">";
                if(typeof chartData[chartId]["chartType"] !== "undefined"  && (chartData[chartId]["chartType"] != "Radial-Chart" && chartData[chartId]["chartType"] != "Bullet-Horizontal" && chartData[chartId]["chartType"] != "LiquidFilled-KPI" && chartData[chartId]["chartType"] != "Standard-KPI" && chartData[chartId]["chartType"] != "Standard-KPI1" && chartData[chartId]["chartType"] != "KPIDash" && chartData[chartId]["chartType"] != "Emoji-Chart" && chartData[chartId]["chartType"] != "Dial-Gauge") ){
                    htmlVar += "<span id='dbChartNamechart" + j + "'>Chart" + j + "</span></a></td></tr></table>";
                }else{
                    htmlVar += "<span id='dbChartNamechart" + j + "'></span></a></td></tr></table>";
                }
                htmlVar += "<div id='chart" + j + "Tab' style='border:1px solid grey'></div></div>";

                $("#xtendChart").append(htmlVar);
                htmlVar="";
                buildPie("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
            //                }
            $("#reportTD1").hide();
            $("#xtendChartTD").show();
        });
}
function addNewChartsBelow(reportId) {
    var chartData = JSON.parse($("#chartData").val());
    var repId = $("#graphsId").val();
    var numOfCharts=$("#numOfCharts").val();
    numOfCharts=parseInt(numOfCharts)+1;
    var currLine = $("#currLine").val();
    currLine = parseInt(currLine)+1;
    var line = JSON.parse($("#lines").val());
    var keys = Object.keys(line);
    var currLineIndex = keys[keys.length-1].split("line")[1];
    line["line"+parseInt(currLineIndex)+1]=1;
    $("#currLine").val(currLine)
    $("#numOfCharts").val(numOfCharts);
    var line={};
    if(typeof $("#lines").val()!="undefined" && $("#lines").val()!=""){
        line = JSON.parse($("#lines").val());
    }
    var keys = Object.keys(line);
    var currLineIndex = keys[keys.length-1];
    line[currLineIndex]=parseInt(line[currLineIndex])+1;
    $("#lines").val(JSON.stringify(line));
    var chartDetails = {};
    //    chartData["numOfCharts"] = parseInt(numOfCharts + 1);
    chartDetails["chartType"] = "Pie";
    chartDetails["records"] = "20";
    chartData["chart" + parseInt(numOfCharts)] = chartDetails;
    $("#chartData").val(JSON.stringify(chartData));
    var num = parseInt(keys.length);

    $.post(
        'reportViewer.do?reportBy=generateJsonData&repId=' + reportId,
        function(data) {
            var json = JSON.parse(data);
            var chartData = json["chartData"];
            var nameMap = json["nameMap"];
            var viewBys = json["viewBys"];
            var measures = json["measures"];
            $("#reportTD1").hide();
            $("#xtendChartTD").show();
            var currData = [];
            for (var i = 0; i<(chartData.length < 15 ? chartData.length : 15); i++) {
                currData.push(chartData[i]);
            }
            var htmlVar = "";
            $("#xtendChart").html("");
            var wid=$(window).width()-50;
            var currKeyIndex=0;
            var keyIndex = keys[0].replace("line","","gi");
            var widFact=keyIndex;
            for(var j=1;j<=num;j++){
                //                    var keys[j];
                var wid = $(window).width()-50;
                if(keyIndex<=j){
                    wid=wid/widFact;
                }
                else{
                    currKeyIndex++;
                    widFact=keys[currKeyIndex].replace("line","","gi");
                    keyIndex=parseInt(keys[currKeyIndex].replace("line","","gi"))+keyIndex;
                    wid=wid/widFact;
                }
                var hgt = $(window).height();
                htmlVar += "<div id='chart" + j + "' style='margin-left:5px;float:left'>";
                htmlVar += "<table id='tablechart" + j + "' align='left' class='dashHeader'>" +
                "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
                "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart"+j+"' onclick='editViewBys(this.name)'/>" +
                "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
                "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
                "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart"+j+"Tab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
                "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
                "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
                "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
                "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamechart" + j + "'>Chart" + j + "</span></a></td></tr></table>";
                htmlVar += "<div id='chart" + j + "Tab' style='border:1px solid grey'></div></div>";

                $("#xtendChart").append(htmlVar);
                htmlVar="";
                buildPie("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
            //                }
            $("#reportTD1").hide();
            $("#xtendChartTD").show();
        });
}
function addNewCharts1(reportId) {
    $("#addNewCharts").dialog({
        autoOpen: false,
        height: 450,
        width: 450,
        position: 'justify',
        modal: true,
        title: "Drag Parameters"
    });


    $("#addNewCharts").dialog('open');

    $.post(
        'reportViewer.do?reportBy=addNewChartsUI&repId=' + reportId,
        function(data) {
            $("#addNewCharts").append(data);
        });
}
window.publish = false;
function saveXtCharts(ctxPath,restrictedFlag,isMultiCompany,isPowerAnalyserEnableforUser){
                    //   added by manik
   ctxPath = parent.$("#ctxpath").val();
    if (checkBrowser() != "ie" ) {
        if(typeof   $("#chartData").val()!="undefined" && $("#chartData").val()!="" ){
            if($("#type").val()=="graph" || $("#type").val()=="trend"){
                saveGridpos();
            }
        }
    }
    $.ajax({
        url:ctxPath+'/userLayerAction.do?userParam=isReportCreator'+"&REPORTID="+document.getElementById("REPORTID").value,
        success:function(data){
            var data1 = JSON.parse(data);
            var keys = Object.keys(data1);
//                      alert("Report Creator is "+data1[keys[0]])
            window.isReportCreator =data1[keys[0]];
            //            alert("RPA is "+restrictedFlag);
            if(isMultiCompany==="true" || isMultiCompany===true){
                $.ajax({
                    url:ctxPath+'/userLayerAction.do?userParam=getUserRole',
                    success:function(data){ 
                        if(data!=null || data!="null"){
                            window.notInsight=false;
                            var data1 = JSON.parse(data);
                            var admin = data1["Admin"];
                            isPowerAnalyserEnableforUser = data1["isPowerAnalyserEnableforUser"];
                            restrictedFlag = data1["restrictedFlag"];
                            var isCompanyAccessible = data1["isCompanyAccessible"];
                            if(admin === true || (isPowerAnalyserEnableforUser === true && restrictedFlag === false) || (restrictedFlag === true && window.isReportCreator === true)){
                                parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
                                parent.$("#filtersmapgraph").val(JSON.stringify(parent.filterMapgraphs));
                                $.ajax({
                                    async:false,
                                    type:"POST",
                                    data:
                                    parent.$('#graphForm').serialize(),
                                    url:  'reportViewerAction.do?reportBy=saveGlobalDate',
                                    success:function(data) {
                                        $.ajax({
                                            async:false,
                                            type:"POST",
                                            data:
                                    $('#graphForm').serialize(),
                                    url:  'reportViewer.do?reportBy=saveXtCharts&publish='+true+"&currentPage="+currentPage,
                                    success:function(data) {
                                        alert("Your Charts Saved");
                                    }
                                });
                                    }
                                });
                            }else if(restrictedFlag === true && isCompanyAccessible === true){
                                alert("Not Enough Privilege to Publish Graphs!!");
                    openNewReportDtls(ctxPath);
                            }else{
                                alert("Not Enough Privilege to Publish Graphs!!"); 
                            }
                        }else{
                            window.notInsight=true;
                    }
                    }
                });
                if(window.notInsight){ 
                if(window.isReportCreator === false && restrictedFlag===true) {
                    alert("Not Enough Privilege to Publish Graphs!!");
                    openNewReportDtls(ctxPath);
                }else if ((window.isReportCreator === true && restrictedFlag===true) || (window.isReportCreator === false && isPowerAnalyserEnableforUser === true && restrictedFlag===false)){
                       parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
                parent.$("#filtersmapgraph").val(JSON.stringify(parent.filterMapgraphs));
                $.ajax({
                    async:false,
                    type:"POST",
                    data:
                            parent.$('#graphForm').serialize(),
                            url:  'reportViewerAction.do?reportBy=saveGlobalDate',
                            success:function(data) {
                                $.ajax({
                                    async:false,
                                    type:"POST",
                                    data:
                    $('#graphForm').serialize(),
                    url:  'reportViewer.do?reportBy=saveXtCharts&publish='+true+"&currentPage="+currentPage,

                    success:function(data) {
                        window.publish = true;
                        alert("Your Charts Saved");
                        saveLocalXtCharts();
                    }
                });
                }
                        })
                }
                }
            }
            else{
                parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
                parent.$("#filtersmapgraph").val(JSON.stringify(parent.filterMapgraphs));
                $.ajax({
                    async:false,
                    type:"POST",
                    data:
                    parent.$('#graphForm').serialize(),
                    url:  'reportViewerAction.do?reportBy=saveGlobalDate',
                    success:function(data) {
                        $.ajax({
                            async:false,
                            type:"POST",
                            data:
                    $('#graphForm').serialize(),
                    url:  'reportViewer.do?reportBy=saveXtCharts&publish='+true+"&currentPage="+currentPage,

                    success:function(data) {
                        alert("Your Charts Saved");
                    }
                });
            }
                });
        }
        }
    });

}
function saveLocalXtCharts(flag){

    //   added by manik
    if (checkBrowser() != "ie" ) {
        if(typeof   $("#chartData").val()!="undefined" && $("#chartData").val()!="" ){
            if($("#type").val()=="graph" || $("#type").val()=="trend"){
                saveGridpos();
            }
        }
    }
    if(typeof currentPage==="undefined"){
        currentPage="default";
    }
   parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
   parent.$("#filtersmapgraph").val(JSON.stringify(parent.filterMapgraphs));
    $.ajax({
        async:false,
        type:"POST",
        data:
        parent.$('#graphForm').serialize(),
        url:  'reportViewerAction.do?reportBy=saveGlobalDate',
        success:function(data) {
            $.ajax({
                async:false,
                type:"POST",
                data:
        $('#graphForm').serialize(),
        url:  'reportViewer.do?reportBy=saveXtCharts'+"&currentPage="+currentPage,

        success:function(data) {
            if(!window.publish){
                if(typeof flag!=='undefined' && flag==='rename'){
                    generateJsonDataReset($("#graphsId").val());
                }
                else{
            alert("Your Charts Saved")
        }
            }
        }
    });
}
    });
}
function saveXtTrend(){
    $.post(
        'reportViewer.do?reportBy=saveXtTrend',$("#graphForm").serialize(),
        function(data) {
            alert("Your Charts Saved")

        });
}
//Changes by shobhit for graph grouping Start
function openChartGroups(chartId)
{
    var html="";
    html = html + "<ul id='groups"+chartId+"' class='dropdown-menu'>";
    for(var i=0;i<parent.chartGroups.length;i++)
    {
        html+="<li id='chartGroups"+chartId+i+"' class='dropdown-submenu pull-right'><a onmouseover='openChartGroup("+i+",\""+chartId+"\",\"\")' >"+parent.chartGroups[i]+"</a>";
    }
    html+="</ul>";
    $("#groups"+chartId).remove();
    $("#chartType"+ chartId).append(html);
}
function openChartGroup(index,chartId,suffix)
{
    var html = "";
    html = html + "<ul id='more"+chartId+"' class='dropdown-menu'>";
     var chartData=JSON.parse(parent.$("#chartData").val());
    if(index==0)
    {
        for(var i=0;i<parent.barGraphs.length;i++)
        {
            if(parent.barGraphs[i]===chartData[chartId]["chartType"]){
                html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.barGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.barGraphs[i] + "</a></li>";          
            }
            else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.barGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.barGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===1)
    {
        for(var i=0;i<parent.circularPieGraphs.length;i++)
        {
             if(parent.circularPieGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.circularPieGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.circularPieGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.circularPieGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.circularPieGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===2)
    {
        for(var i=0;i<parent.kpiGraphs.length;i++)
        {
            if(parent.kpiGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.kpiGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.kpiGraphs[i] + "</a></li>";
          }
          else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.kpiGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.kpiGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===3)
    {
        for(var i=0;i<parent.VarianceGraphs.length;i++)
        {
            if(parent.VarianceGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.VarianceGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.VarianceGraphs[i] + "</a></li>";
          }
          else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.VarianceGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.VarianceGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===4)
    {
        for(var i=0;i<parent.lineGraphs.length;i++)
        {
            if(parent.lineGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.lineGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.lineGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.lineGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.lineGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===5)
    {
        for(var i=0;i<parent.groupedGraphs.length;i++)
        {
            if(parent.groupedGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.groupedGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.groupedGraphs[i] + "</a></li>";
         }
         else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.groupedGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.groupedGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===6)
    {
        for(var i=0;i<parent.mapGraphs.length;i++)
        {
            if(parent.mapGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.mapGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.mapGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.mapGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.mapGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===7){
      for(var i=0;i<parent.indiaStateMap.length;i++)
    {
            if(parent.indiaStateMap[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.indiaStateMap[i].replace(" ", "-", "gi") + "\")'>" + parent.indiaStateMap[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.indiaStateMap[i].replace(" ", "-", "gi") + "\")'>" + parent.indiaStateMap[i] + "</a></li>";
        }
    }
    }
    else if(index===8)
    {
        for(var i=0;i<parent.areaGraphs.length;i++)
        {
            if(parent.areaGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.areaGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.areaGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.areaGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.areaGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===9)
    {
        for(var i=0;i<parent.tableGraphs.length;i++)
        {
            if(parent.tableGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.tableGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.tableGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.tableGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.tableGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===10)
    {
        for(var i=0;i<parent.bubbleGraphs.length;i++)
        {
            if(parent.bubbleGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.bubbleGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.bubbleGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.bubbleGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.bubbleGraphs[i] + "</a></li>";
        }
    }
    }
    else if(index===11)
    {
        for(var i=0;i<parent.otherGraphs.length;i++)
        {
            if(parent.otherGraphs[i]===chartData[chartId]["chartType"]){
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"background-color:#808080\" onclick='changeChart(\"" + chartId + "\",\"" + parent.otherGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.otherGraphs[i] + "</a></li>";
        }
        else{
            html = html + "<li><a class='gFontFamily gFontSize13' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + parent.otherGraphs[i].replace(" ", "-", "gi") + "\")'>" + parent.otherGraphs[i] + "</a></li>";
        }
    }
    }
    html = html + "</ul>";
    $("#more"+chartId).remove();
    $("#chartGroups"+suffix+chartId+ index).append(html);
}
//Changes by shobhit for graph grouping End
function openCharts(chartId){

    var graphs = Object.keys(parent.graphsMap);
    //    var chartHtml = '<div id=\"chartDiv' + chartId + '\" class=\"drop_div\" align=\"center\" style=\"display:none;overflow-y:scroll;max-height:50%;position:reltive\"> <div id=\"innerChart' + chartId + '\" class=\"\" ></div></div>';
    //    $("#tablechart1").append(chartHtml);
    //    $("#table" + chartId.replace("Tab","","gi")).append(chartHtml);
    var html = "";
    //    html = html + "<table>";asasa
    html = html + "<ul id='more"+chartId+"' class='dropdown-menu'>";
    for (var i = 0; i < graphs.length; i++) {
        //        if (typeof key == "undefined") {
        //            html = html + "<tr><td class='graphlist' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'><span style='font-size:9px'>" + graphs[i] + "</span></td></tr>";
        html = html + "<li><a class='' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
    //        } else {
    //            html = html + "<tr><td class='graphlist' style=\"cursor: pointer\" onclick='changeChartOverLay(\"" + chartId + "\",\"" + width + "\",\"" + height + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\",\"" + key + "\")'><span style='font-size:9px'>" + graphs[i] + "</span></td></tr>";
    //        }
    }
    html = html + "<li><a class='' onmouseover='moreGraphs(\""+chartId+"\")' style=\"cursor: pointer\">More Graphs</a></li>";//changed by sruthi for ie9
    //    html = html + "</table>";
    html = html + "</ul>";//changed by sruthi for ie9
    //    document.getElementById('innerChart' + chartId).innerHTML = html;
    //    document.getElementById('chartType' + chartId).append = html;
    //    $("#chartDiv" + chartId).toggle(400);
    $("#chartType"+ chartId).append(html);
}
function openTrend(chartId,width,height){

    var graphs = Object.keys(parent.graphsMap);
    //    var chartHtml = '<div id=\"chartDiv' + chartId + '\" class=\"drop_div\" align=\"center\" style=\"display:none;overflow-y:scroll;max-height:50%;position:reltive\"> <div id=\"innerChart' + chartId + '\" class=\"\" ></div></div>';
    //    $("#tablechart1").append(chartHtml);
    //    $("#table" + chartId.replace("Tab","","gi")).append(chartHtml);
    var html = "";
    //    html = html + "<table>";asasa
    html = html + "<ul id='more"+chartId+"' class='dropdown-menu'>";
    for (var i = 0; i < graphs.length; i++) {
        //        if (typeof key == "undefined") {
        //            html = html + "<tr><td class='graphlist' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'><span style='font-size:9px'>" + graphs[i] + "</span></td></tr>";
        html = html + "<li><a class='' style=\"cursor: pointer\" onclick='changeTrend(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
    //        } else {
    //            html = html + "<tr><td class='graphlist' style=\"cursor: pointer\" onclick='changeChartOverLay(\"" + chartId + "\",\"" + width + "\",\"" + height + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\",\"" + key + "\")'><span style='font-size:9px'>" + graphs[i] + "</span></td></tr>";
    //        }
    }
    html = html + "<li><a class='' onmouseover='moreTrend(\""+chartId+"\")' style=\"cursor: pointer\" '>More Graphs</a></li>";
    //    html = html + "</table>";
    html = html + "</ul></li>";
    //    document.getElementById('innerChart' + chartId).innerHTML = html;
    //    document.getElementById('chartType' + chartId).append = html;
    //    $("#chartDiv" + chartId).toggle(400);
    $("#chartType"+ chartId).append(html);
}
function changeChartOld(name,wid,hgt,keyVal){
    var chartData = JSON.parse($("#chartData").val());
    chartData[name.replace("Tab","","gi")]["chartType"]=keyVal;
    $("#chartData").val(JSON.stringify(chartData));

    document.getElementById('chartDiv' + name).display = "none";
    var numOfCharts=$("#numOfCharts").val();
    var currLine = $("#currLine").val();
    var num = parseInt(numOfCharts);
    $.post(
        'reportViewer.do?reportBy=generateJsonData&repId=' + reportId,
        function(data) {
            var json = JSON.parse(data);
            var chartData = json["chartData"];
            var nameMap = json["nameMap"];
            var viewBys = json["viewBys"];
            var measures = json["measures"];
            $("#reportTD1").hide();
            $("#xtendChartTD").show();
            var currData = [];
            if (num % 2 === 0) {
                for (var i = 0; i<(chartData.length < 15 ? chartData.length : 15); i++) {
                    currData.push(chartData[i]);
                }
            }
            else {
                for (var i = 0; i<(chartData.length < 15 ? chartData.length : 15); i++) {
                    currData.push(chartData[i]);
                }
            }
            $("#"+name).html("");
            //                     var wid = $(window).width()-50;
            //                     wid=parseInt(wid/currLine);
            //                     var hgt = $(window).height();
            if(keyVal=="Vertical-Bar"){
                buildBar(name, currData, viewBys, measures,wid,hgt);
            }
            else if(keyVal=="Line"){
                buildLine(name, currData, viewBys, measures, wid, hgt);
            }
            else
            //                    if(keyVal=="Pie")
            {
                buildPie(name, currData, viewBys, measures,wid,hgt);
            }
        //                else if(keyVal=="Word-Cloud"){
        //                    buildWordCloud(name, currData, viewBys, measures,wid,hgt);
        //                }

        //                $("#reportTD1").hide();
        //                $("#xtendChartTD").show();
        });
}

function getReport(chartMeta,chartData2,viewBys, measures){
    var chartData = JSON.parse(chartData2);
    var view = viewBys;
    $("#reportTD1").hide();
    $("#xtendChartTD").show();


    var chartData1 = chartMeta["chartData"];
    $("#chartData").val(JSON.stringify(chartData1));
    //           $("#numOfRecords").val(chartMeta["numOfRecords"]);
    var num=parseInt(Object.keys(chartData1).length);
    var htmlVar = "";
    $("#xtendChart").html("");
    for(var j=1;j<=num;j++){
        var currData = [];
        if(typeof chartData["chart"+j] == "undefined"){
            viewBys = view;
            for (var i = 0; i<(chartData["chart1"].length < 15 ? chartData["chart1"].length : 15); i++) {
                currData.push(chartData["chart1"][i]);
            }
        }
        else{
            for (var i = 0; i<(chartData["chart"+j].length < 15 ? chartData["chart"+j].length : 15); i++) {
                currData.push(chartData["chart"+j][i]);
                if(typeof chartData1["chart"+j]["dimensionsNames"]!="undefined"){
                    viewBys=chartData1["chart"+j]["dimensionsNames"];
                }
            }
        }
        var wid = $(window).width()-50;
        wid=parseInt(wid/num);
        var hgt = $(window).height();
        htmlVar += "<div id='chart" + j + "' style='margin-left:5px;float:left'>";
        htmlVar += "<table id='tablechart" + j + "' align='left' class='dashHeader'>" +
        "<tr><td><img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
        "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='chart"+j+"' onclick='editViewBys(this.name)'/>" +
        "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
        "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
        "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='chart"+j+"Tab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
        "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
        "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
        "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
        "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamechart" + j + "'>Chart" + j + "</span></a></td></tr></table>";
        htmlVar += "<div id='chart" + j + "Tab' style='border:1px solid grey'></div></div>";

        $("#xtendChart").append(htmlVar);
        htmlVar="";
        try{
            if(chartData1["chart" + j]["chartType"]==="Vertical-Bar"){
                buildBar("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
            else if(chartData1["chart" + j]["chartType"]==="Line"){
                buildLine("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
            else if(chartData1["chart" + j]["chartType"]==="Pie"){
                buildPie("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
            else{
                buildPie("chart" + j + "Tab", currData, viewBys, measures,wid,hgt);
            }
        }catch(e){}
    }
    //                }
    $("#reportTD1").hide();
    $("#xtendChartTD").show();
}

function editCharts(){
    var REPORTID = document.getElementById("REPORTID").value;
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
    $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
        function(data){
            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            frameObj.src = source;
        });
    $("#editViewByDiv").dialog('open');
}
function editViewBys(chartId){
    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
     if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    $.post(ctxPath+'/reportViewerAction.do?reportBy=getLocalObjectMap&REPORTID='+REPORTID+'&chartId='+chartId+'&ctxPath='+ctxPath, $("#graphForm").serialize(),
        function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId+"&editFlag=edit11";
            frameObj.src = source;
        });
    parent.$("#editViewByDiv").dialog('open');
}
function editSingleCharts(editFlag){
    //    var chartId="";
    var IsInnerView = "Y";
    var REPORTID = parent.document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    $.post(ctxPath+'/reportViewer.do?reportBy=showViewBy&REPORTID='+REPORTID+'&ctxPath='+ctxPath+"&isGraphObject=Y", $("#ViewByForm").serialize() ,
        function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&grid="+grid;   //edited by manik
            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&IsInnerView="+IsInnerView+"&editFlag="+editFlag;
            frameObj.src = source;

        });
    var d = parent.$("editViewByDiv").attr('title');
    parent.$("#editViewByDiv").dialog('open');
}
function buildCharts(flag){
    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
     if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    var source ='';
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#ViewByForm').serialize() + "&REPORTID="+REPORTID+"&ctxPath="+ctxPath,
        url: ctxPath+"/reportViewer.do?reportBy=getObjectMap",
        //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
        success:function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            if(flag == "add"){
                //                var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=viewer";
                source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=viewer";   //edited by manik
            }
            if(flag=="isEdit"){

                //                var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=isEdit";
                source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=isEdit";    //edited by manik
            }
            frameObj.src = source;
        }
    });
    parent.$("#editViewByDiv").dialog('open');
}
function resequenceGraphs11(){
    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
     if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFramegbl");
    var source ='';
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#ViewByForm').serialize() + "&REPORTID="+REPORTID+"&ctxPath="+ctxPath,
        url: ctxPath+"/reportViewer.do?reportBy=getObjectMap",
        //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
        success:function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;



                //                var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=isEdit";
                source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=paramsequnce";

            frameObj.src = source;
        }
    });
    parent.$("#editViewByDivgbl").dialog('open');
}
function buildChartsInTrends(){
    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    var localAddEdit = "addTrend";
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#ViewByForm').serialize() + "&REPORTID="+REPORTID+"&ctxPath="+ctxPath,
        url: ctxPath+"/reportViewer.do?reportBy=getObjectMap",
        //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
        success:function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            var source = ctxPath+"/Report/Viewer/editTrend.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&localAddEdit="+localAddEdit+"&from=viewer";
            frameObj.src = source;
        }
    });

    parent.$("#editViewByDiv").dialog('open');
}
function editSingleTrend(){
    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    $.post(ctxPath+'/reportViewer.do?reportBy=getObjectMap&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
        function(data){
            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            var source = ctxPath+"/Report/Viewer/editTrend.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            frameObj.src = source;
        });
    parent.$("#editViewByDiv").dialog('open');
}
function localEditSingleTrend(chartId){
    var REPORTID = parent.document.getElementById("REPORTID").value;
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
    var localAddEdit = "localEdit";

    $.post(ctxPath+'/reportViewer.do?reportBy=getObjectMap&REPORTID='+REPORTID+'&ctxPath='+ctxPath, $("#ViewByForm").serialize() ,
        function(data){
            //            var source = ctxPath+"/Report/Viewer/editTrend.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath;
            var source = ctxPath+"/Report/Viewer/editTrend.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId+"&localAddEdit="+localAddEdit;
            frameObj.src = source;
        });
    parent.$("#editViewByDiv").dialog('open');
}


// added by Mayank on Dec,08,2014 for excel download
function downloadExcel(reportID){
    var ctxpath = document.getElementById("h").value;
    var fileType = "E";
    var expType = "Report";
    var expRec = "All";
    var paramType = "withParameters";

    var source = ctxpath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+reportID+"&displayType="+expType+"&expRec="+expRec+"&paramType="+paramType;
    var dSrc = document.getElementById("downFrame1");
    dSrc.src = source;

}

function downloadPDF(reportID){
    var ctxpath = document.getElementById("h").value;
    var fileType = "P";
    var expType = "Report";
    var expRec = "All";
    var paramType = "withParameters";
    var pdfTypeSelect = "A4";
    var pdfCellFont = "8.0f";
    var pdfCellHeight = "26.0f";

    //     var source = ctxpath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+reportID+"&displayType="+expType+"&expRec="+expRec+"&paramType="+paramType;
    var source = ctxpath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+reportID+"&displayType="+expType+"&expRec="+expRec+"&paramType="+paramType+"&pdfTypeSelect="+pdfTypeSelect+"&pdfCellFont="+pdfCellFont+"&pdfCellHeight="+pdfCellHeight;
    var dSrc = document.getElementById("downFrame1");
    dSrc.src = source;

}
var curKpiMeasure = "";
var toggleCounter = {};
var gtMap = {};
var rowMap = {};
var overLayData = {};
var kpiColor = ["#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d"];
var hasTouch = false;
var regidch;
var fromoneview='false';
var oneviewid1;
var graphData=[];
//edited by manik
function generateChart1(jsonData){

    graphData=JSON.parse(jsonData);
    var data = graphData;
    var chartData = JSON.parse(parent.$("#chartData").val());
    var charts = Object.keys(chartData);
    for (var kq = 0; kq < charts.length; kq++) {
        var ch = "chart" + parseInt(kq+1);
        var currData=[];
        var records = 10;
        if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
            records = chartData[ch]["records"];
        }
        //              for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
        //                  currData.push(data[ch][m]);
        //              }
        chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"])
    }
}

function generateChart(jsonData,dragFlag,idArr){
    //added by manik
    var t1 = createTask1(1);
    var t2 = createTask2(1);
    //var t1 = createTask3(1);
    executeTasks(t1, t2 );
    function executeTasks() {
        var tasks = Array.prototype.concat.apply([], arguments);
        var task = tasks.shift();
        task(function() {
            if(tasks.length > 0)
                executeTasks.apply(this, tasks);
        });
    }

    function createTask2(num) {


        return function(callback) {
            setTimeout(function() {

                generateChartloop(jsonData,dragFlag,idArr)
                if(typeof callback == 'function') callback();
            }, 1000);
        }
    }

    function createTask1(num) {


        return function(callback) {
            setTimeout(function() {
                //if(fromoneview=='null'&& fromoneview=='false'){
                if(fromoneview!='null'&& fromoneview=='true'){

                }else{

                    if (checkBrowser() != "ie" ) {
                        
                        if(typeof grid !=="undefined")
                        try{
                        grid.remove_all_widgets();
                        }
                    catch(e){}
                    }else{

                        $("#xtendChartssTD").html('');
                    }

                }
                //}

                if(typeof callback == 'function') callback();
            }, 1000);
        }
    }
}
var divcount;
function generateChartloop(jsonData,dragFlag,idArr){
    //Added by Ashhutosh
    for(var i=1; i<=Object.keys(JSON.parse(parent.$("#chartData").val())).length; i++){
        window["actionDrill_"+i]=0;
    }
    // add by mayank sh.(6/6/15) for filter show in div.
    var htmlstr= "";
    var value1='';
    var flag='';
    var isFilter = parent.$("#filters1").val();

    if(isFilter=="")
        flag='black';
    else{
        flag='red';

        var isFilter1 = JSON.parse(parent.$("#filters1").val());
        var key1=Object.keys(isFilter1);
        for (var key in key1) {
            value1=isFilter1[key1[key]];
            if (value1!="")
            {
                flag='red'
                }
            else
            {
                flag='black'
                }
            // alert(""+value);
            for(var val in value1){
                htmlstr+="<span>"+value1[val]+",</span>";
            }
            }
    $("#applied_filter").html(htmlstr);
}
// end by mayank sh.

$("#loading").hide();
if (checkBrowser() == "ie" ) {
    $("#xtendChartssTD").html('');
}
$("#xtendChartTD").html('');
   
$("#content_1").html('');
$("#content_Drag").html('');
//    grid.remove_all_widgets();    // added by manik
if(typeof (jsonData)!=Object){
    graphData=JSON.parse(jsonData);
}
if(typeof parent.$("#chartData").val()!="undefined" && parent.$("#chartData").val()!=""){
    var chartData = JSON.parse(parent.$("#chartData").val());
}else{
    var chartData = [];
}

//  for(var w=0;w<10000;w++)
var divContent="";
var chartId="";
var charts=[];
var repId;
var repname;
if(fromoneview!='null'&& fromoneview=='true'){
    var chartname1 = parent.$("#chartname").val();
    repId = parent.$("#graphsId").val();
    repname = parent.$("#graphName").val();
    var charts1=[];
    charts1=chartname1;
    charts=charts1;
    var dashletid;
    //if(regidch!=null && regidch=='OLAPGraphRegion'){
    //    dashletid =regidch;
    //}else{
    //    dashletid ="Dashlets-"+regidch;
    //}

    var wid ;
    var hgt1 ;
    //if(regidch=='olap'){
    //dashletid="OLAPGraphRegion";
    // hgt1 = parent.$("#"+dashletid).offsetHeight;
    //                        wid = parent.$("#"+dashletid).offsetWidth;
    //}else{
    //    dashletid =regidch
    ////                        hgt1 = document.getElementById(dashletid).offsetHeight;
    ////                        wid = document.getElementById(dashletid).offsetWidth;
    //
    //}
    hgt1=hgt1-50;
}else{
    charts = Object.keys(chartData);
}
var width = $(window).width();
var divHeight = $(window).height();
var divContent = "";
var widths = [];
var heights = [];
var widFact = 0;
var heightFact = 0;
var j = 0;
var sameRowFlag = true;
var divcontlist="";
var divContentC="";
var filterHtml = "";
//     divContent += "<div  style='width:100%'>";
if(fromoneview!='null'&& fromoneview=='true'){

}else{
if(typeof filterData!="undefined" && filterData!=""){
    var filterKeys = Object.keys(filterData);
}else{
    var filterKeys = JSON.parse(parent.$("#viewby").val()) ;
}

    // divContent += "</tr></table>";
    //  $("#xtendChartTD").html(divContent);
    try{
  

        var viewIds = JSON.parse(parent.$("#viewbyIds").val());
        //    divContent += "</tr></table>";
        //     <div id="content_1" class="content" style="width: 100%">
        // <div id="vhead14" style="display:none" class="panelcollapsed">
        //                    <h2><label id="header14" class="headl"></label></h2>
        //                    <div id="vsdiv14" class="panelcontent">
        //                    </div>
        //                </div>

        //    divContentC += "<div width='13%' id='content_1' class='content' style='width:13%;float:right;padding-top:1em; height:490px;overflow:auto;'>";
        if(parent.userType == "ANALYZER"){
        $("#xtendChartssTD").append("<div  id='content_1' class='content hideAllDiv' style='width:180px;float:right;height:490px;overflow:auto;display:none'></div>");
        }else{
       $("#xtendChartssTD").append("<div  id='content_1' class='content hideAllDiv' style='width:180px;float:right;height:490px;overflow:auto;display:none'></div>");
            
        }
        if( flag=="red"){

            filterHtml += "<span id='filterFlag' value='red' ><img float='right' src='images/filter_red.png' onclick='showDragBox()' ></img></span>";
        }else
            filterHtml += "<span id='filterFlag' value='black' ><img float='right' src='images/filter.png' onclick='showDragBox()' ></img></span>";
        filterHtml += "<span><strong style='white-space:nowrap; color:black;text-align:center;padding-left:5%'>Graph Filters</strong></span>";
     filterHtml += "<span><input   type='button' onclick='applyfilterIE()'  value='Go'  style='width:25px;'></span>";

        var viewByIds = JSON.parse($("#viewbyIds").val());
        var viewBy = JSON.parse($("#viewby").val());
        for (var key in filterKeys) {
            var drills = {};
            var grpKeys;
            try{
                drills = JSON.parse($("#drills").val());
                grpKeys = Object.keys(drills);
            }catch(e){}
            var index = viewBy.indexOf(filterKeys[key]);
            if(index==-1){
                index=viewBy.indexOf(" "+filterKeys[key])
            }
            var viewId = viewByIds[index];
            var filterGroupBy = filterKeys[key];

            //        var viewId = viewIds[key];
            var filters = filterData[filterGroupBy];
            var selectedFilters = [];
            var filterMap = {};
            var filterValues=[];
            if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
                filterMap = JSON.parse($("#filters1").val());
                if(typeof filterMap[viewId]!=="undefined"){
                    filterValues=filterMap[viewId];
                }
            }

            if (typeof filterMap[viewId] !== "undefined") {
                selectedFilters = filterMap[viewId];
            }
            filterHtml += "<div class='expandDiv"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;padding-top:.5em;padding-bottom:.5em;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:1px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";

            if (selectedFilters.length === 0) {
                filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' checked onclick='selectAll(this.id,this.name)'/>";
            } else {
                filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' onclick='selectAll(this.id,this.name)'/>";
            }
            if(filterGroupBy.length > 25){
                filterHtml += "<span style='white-space:nowrap'>&nbsp;" + filterGroupBy.substring(0,25) + "..</span></label></div>";
            }
            else{
                filterHtml += "<span style='white-space:nowrap'>&nbsp;" + filterGroupBy + "</span></label></div>";
            }
            filterHtml += "</div>";
            filterHtml += "<div id='vsdiv14"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none'><table style='' width='100%'>";
            var isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/));
            if(isAtLeastIE11)
                {
            var size=100;
            if(typeof filters !=="undefined" && size>filters.length){
            size=filters.length;
           } }
                else
                    {
                     size=filters;
                    }

            for (var filter in size) {

                filterHtml += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
                if (selectedFilters.indexOf(filters[filter]) !== -1 && filters[filter]!="") {
                    filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_" + filter + "' value='" + filters[filter] + "' name='" + filterGroupBy + "*,"+viewId+"'  onclick='applyFilter(this.id,this.name)'/>";
                } else {
                    if(filters[filter] !=""){
                        filterHtml += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_" + filter + "' checked value='" + filters[filter] + "' name='" + filterGroupBy + "*,"+viewId+"' onclick='applyFilter(this.id,this.name)'/>";
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
        if(checkBrowser() == "ie" )   {
            $("#content_Drag").html(filterHtml);
        }
        else{
            $("#content_1").html(filterHtml);
        }
        var textDivHeight = screen.height;
       if(parent.userType == "ANALYZER"){
       $("#xtendChartssTD").append("<div id='content_Drag' class='content hideAllDiv' style='width:180px;float:right;height:"+textDivHeight+"px;overflow:auto;'></div>");
       }else{
       $("#xtendChartssTD").append("<div id='content_Drag'  class='content hideAllDiv' style='width:180px;float:right;height:460px;overflow:auto;position:fixed;margin-top:;background-color: rgb(255, 255, 255); box-shadow: 0px 0px 2px 2px rgb(197, 66, 66); margin-top: 1px; border-radius: 5px;right: 20px;box-shadow:0px 1px 4px 2px rgb(227, 221, 221);margin-right: 2px;display:none;background-color: #FFF;z-index:11'></div>");
       }
       divContentC +="<div style='position:fixed;margin-left:2%'>"
         if(checkBrowser() == "ie" ){
        if( flag=="red"){
        divContentC += "<span id='filterIcon' value='red' ><img float='right' src='images/filter_red.png' onclick='showHideFilter()' ></img></span>";
        }else
        divContentC += "<span id='filterIcon' value='black'><img float='right' src='images/filter.png' onclick='showHideFilter()' ></img></span>";
         }
        divContentC += "<span title='Parameters' align='center' style='font-size:16px;white-space:nowrap; text-align:center;'>"+translateLanguage.Parameters+"</span></br>";
        divContentC += "<span  style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>"+translateLanguage.Drag_Parameters+"</span>";
        divContentC +="</div>"
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
            try{
                drills = JSON.parse($("#drills").val());
                grpKeys = Object.keys(drills);
            }catch(e){}
            var index = viewBy.indexOf(filterKeys[key]);
            if(index==-1){
                index=viewBy.indexOf(" "+filterKeys[key])
            }
            var viewId = viewByIds[index];
            var filterGroupBy = filterKeys[key];

            //        var viewId = viewIds[key];
            //        var filters = filterData[filterGroupBy];
            var selectedFilters = [];
            var filterMap = {};
            var filterValues=[];
            if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
                filterMap = JSON.parse($("#filters1").val());
                if(typeof filterMap[viewId]!=="undefined"){
                    filterValues=filterMap[viewId];
                }
            }

            if (typeof filterMap[viewId] !== "undefined") {
                selectedFilters = filterMap[viewId];
            }
           

        //         divContentC += "<tr ><span draggable='true' id='"+filterGroupBy+"'   ondragstart='drag(event,this.id)'>"+filterGroupBy+"</span><br></tr>"

        }
        for(var k=0; k<viewBy.length;k++){
            if(viewBy[k].length>15){
                divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='"+viewBy[k]+"'   ondragstart='drag(event,this.id)'  ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+viewBy[k]+"' >"+viewBy[k].substring(0,15)+"..</span></li>"
//                divContentC += "<hr style='display: block;border-style: solid;border-width: 1px;color:#e4e4e4'>";
            }else{
                divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                divContentC += "<li style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='"+viewBy[k]+"'   ondragstart='drag(event,this.id)'  ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+viewBy[k]+"'>"+viewBy[k]+"</span></li>"
//                divContentC += "<hr style='display: block;border-style: solid;border-width: 1px;color:#e4e4e4'>";
            }
        }
        divContentC += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
        divContentC += "</ul>"
        divContentC += "</div>"
        divContentC += "</div>"

        var measures = JSON.parse($("#measure").val())

        divContentC +="<div style='position:fixed;margin-top:;margin-left:2%'>"
        divContentC += "<span title='Measures' align='center' style='font-size:16px;white-space:nowrap;text-align:center;'>"+translateLanguage.Measures+"</span></br>";
        divContentC += "<span style='text-align: center;font-size:10px;white-space:nowrap; color:grey;'>"+translateLanguage.Drag_Measures+"</span>";
        divContentC +="</div>";
        divContentC +="<div style='position:fixed;margin-top:2%;'>"
        divContentC += "<div class = 'hiddenscrollbar' style=''>"
        divContentC += "<div class = 'innerhiddenscrollbar'  style='height:180px;width:150px'>"
        divContentC += "<ul style='list-style-type:none' class='rightDragMenu'>"
        for(var j in measures ){
            if(measures[j].length>20){
                divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='"+measures[j]+"' onclick='measureDriven(this.id)'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+measures[j]+"'>"+measures[j].substring(0,15)+"..</span></li>"
            }else{
                divContentC += "<hr style='display: block;border-style: ;border-width: 1px;color:#ccc'>";
                divContentC += "<li  style='width:100%;height:20px;color:#fff;cursor:pointer' class='' draggable='true' id='"+measures[j]+"' onclick='measureDriven(this.id)'  ondragstart='drag(event,this.id)' ><span class='gFontFamily gFontSize12' style='vertical-align: -moz-middle-with-baseline;color:#555;font-weight:' align='center' title='"+measures[j]+"'>"+measures[j]+"</span></li>"
            }

        }
        divContentC += "<hr style='display: block;border-style: inset;border-width: 1px;color:#ccc'>";
        divContentC += "</ul>"
        divContentC += "</div>"
        divContentC += "</div>"
        divContentC += "</div>"

    }catch(e){}
    divContentC += "</div>";
    var textHtml = "";
    textHtml += "<div id='textMainDiv' style='width:98%;height:"+(textDivHeight-10)+"px;border:.5px solid grey;overflow:hidden'>";
    textHtml += "<div id='imageDiv' style='width:98%;height:auto;'>"
   textHtml += "<span id='iconLoad' value='black'><img src='images/udisenewimage.gif' style='display:block;margin-left:auto;margin-right:auto;'  ></img></span>";
   textHtml += "</div>";
   textHtml += "<div id='textContentDiv' style='width:98%;height:"+textDivHeight*0.50+"px'>";
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
    
    if(checkBrowser() == "ie" )   {
             
        $("#content_Drag").append(filterHtml);
    }
    else if(parent.userType == "ANALYZER"){
         $("#content_Drag").html('');
         $("#content_Drag").append();
    }
    else{
        $("#content_Drag").append(divContentC);
    }
//                $("#content_Drag").append(divContentC);
//                $("#content_1").show();
}
//added by manik
var row=1;
var col=1;
var id=1;
var html="";
var sizeGrid_x=1;
var sizeGrid_y=1;
var k1=0;
var loopLength = 0;
if(typeof parent.$("#draggableViewBys").val()!="undefined" && parent.$("#draggableViewBys").val()!="" && dragFlag=="drag"){
    var dragObject = JSON.parse(parent.$("#draggableViewBys").val())
    var chartKeys = Object.keys(chartData)
    var chartLength = chartData
    loopLength = (dragObject.length)
    k= loopLength-1

    divcontlist="";
    //        var widthPer = "50%";//$(window).width()/2+"px";
    var singleLine = "1px";
    var chartDetails = chartData["chart" + (k + 1)];
    chartId = "chart" + (parseInt(k) + 1);
    if(fromoneview!='null'&& fromoneview=='true'){
        chartId=charts;
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
    }
    else if (widFact === 0) {
        singleLine = "0px";
    }
    else {
        singleLine = "2px";
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        if(typeof chartData[chartId] !=="undefined" && chartData[chartId]["size"] === "M"){
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
        }
        else {
            chartName = chartId;
        }
    }
    else{

        if(typeof chartData["chart" + (k + 2)] !=="undefined" && chartData["chart" + (k + 2)]["size"] === "M"){
            bottomLine = "1px";
        }
        //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
        //            bottomLine = "1px";
        //            widFact = 0;
        //        }
        divHeight = $(window).height() / 1.7;
        var chartName = "";
        if (typeof typeof chartData["chart" + (k + 1)] !=="undefined" && typeof chartData["chart" + (k + 1)]["Name"] !== "undefined" && chartData["chart" + (k + 1)]["Name"] !== "") {
            chartName = chartData["chart" + (k + 1)]["Name"];
        }
        else {
//            chartName = "chart" + (k + 1);
            chartName = "";
        }
    }
    if(typeof chartData[chartId]["chartType"] !== "undefined"  && (chartData[chartId]["chartType"] == "Grouped-Bar" ||  chartData[chartId]["chartType"] == "GroupedHorizontal-Bar")){

        widthPer = "100%"
    }

    var viewbys = chartDetails.viewBys;
    var measures = chartDetails.meassures;
    var chartMeasure = [];
    chartMeasure.push(measures[0]);
    var chartViewby = [];
    chartViewby.push(viewbys[0]);
    if(fromoneview!='null'&& fromoneview=='true'){
        //                     chartId=charts;
        if (typeof divcount !== 'undefined'){
            divcount;
            k=divcount-1;
        }else{
            divcount=k+1;
        }
    }
    //edited by manik for dynamic div
    //        divContent += "<td class='DroppableDiv' id='divchart" + (k + 1) + "' style='overflow:auto;position:relative;width:" + widthPer + ";height:" + divHeight + ";float:left;'>" +
    if (checkBrowser() == "ie" ) {

        divcontlist += "<div id='divchart" + (k + 1) + "'  style='float:left;width:42%;height:56%;margin-left:0.5%;margin-top:0.5%; border: 1px dotted rgb(200, 200, 200);' >" ;
    }else{
        if(fromoneview!='null'&& fromoneview=='true'){

            divcontlist += "<li id='divchart" + (k + 1) + "' data-row='"+row+"' data-col='"+col+"' data-sizex='"+sizeGrid_x+"' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >" ;
        }else{
            divcontlist += "<li id='divchart" + (k + 1) + "' data-row='"+row+"' data-col='"+col+"' data-sizex='"+sizeGrid_x+"' ondrop='drop(event,this.id)' class='dragClass' ondragover='allowDrop(event)' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >" ;

        }
    }
divcontlist +=      "<table id='tablechart" + (k + 1) + "' align='left' class='dashHeader' style='display:none'>" +
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
if(typeof chartData[chartId]["chartType"] != "undefined"  && (chartData[chartId]["chartType"] != "Radial-Chart" && chartData[chartId]["chartType"] != "Standard-KPI" && chartData[chartId]["chartType"] != "Standard-KPI1" && chartData[chartId]["chartType"] != "KPIDash" && chartData[chartId]["chartType"] != "Bullet-Horizontal" && chartData[chartId]["chartType"] != "Emoji-Chart" && chartData[chartId]["chartType"] != "Dial-Gauge")){

    if(fromoneview!='null'&& fromoneview=='true'){
        divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +

        "<table style='width:100%;background-color:#FFF'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:70%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></span></td>";
    }else{
        divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +
        "<table style='width:100%'><tr><td id='renameTitle"+chartId+"' style='padding-left:1%;width:70%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></span></td>";
    }
}else{
        
    divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:'></span></a></td></tr></table>" ;
    if(fromoneview!='null'&& fromoneview=='true'){
        divcontlist +=   "<table style='width:100%;background-color:#FFF'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:81%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";
    }else{
        divcontlist += "<table style='width:100%'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:81%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";
    }
}
if(fromoneview!='null'&& fromoneview=='true'){
    //                 var fun="callGraphOpt11("+k+")";
    //                     //divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt1(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",'"+chartName+"','null','"+repId+"','"+repname+"','null')' ></img></span>"+
    //                     divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:80%' src='images/moreIcon.png' alt='Options' onclick=\""+fun+"\" ></img></span>"+
    //                              "<ul id='graphOptionchart"+(k+1)+"' class='dropdown-menu' ></ul></td></tr></table>"+
    //        "<div id='breadcrumpId' style='display:block'></div>";
    divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:80%;width:10px;height:10px'; title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt1( \""+ chartId + "\",\""+ tableId + "\"," + (k + 1) + ",\""+chartName+"\",null,\""+repId+"\",\""+repname+"\",\""+enablefilter+"\")' ></img></span>"+
    "<ul id='graphOptionchart"+(k+1)+"' class='dropdown-menu' style='margin-left: -13em;' ></ul></td></tr></table>"+
    "<div id='quickFilterDivchart"+(k+1)+"' style='display:block'></div>";
    "<div id='breadcrumpId' style='display:block'></div>";
//    callGraphOpt11 = function(k){
//    callGraphOpt1( chartId,tableId,(k + 1),chartName,chartData,repId,repname,enablefilter)
//    }
}
else{
    //add by mayank sh. for top bottom chart
//        divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='topBottom"+chartId+"' title = 'Top Bottom' style='width:18px;height:12px' src='images/topBottom.png' alt='Options' onclick='topBottomChart(\"" + chartId + "\",event)'></img></span>"+
//    "<ul id='topBottom1"+chartId+"' class=''></ul></td>";
divcontlist+="<td id='localfilter_red"+chartId+"' style='width:1%;' class=''><div class='' style='cursor:pointer;z-index:2;'><span  data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' width='10' height='10' style='display:none;margin-left:20%' src='images/filter_red.png' alt='Options' onmouseover='showAppliedFilters(\"" + chartId + "\",event)'  ></img></span>"+
        "<ul id='localFilter1"+chartId+"' class=''>";
      var type =$("#type").val();
      //ad local filter
    // alert(chartData[chartId]["filters"]) 
    if(typeof chartData[chartId]["filterMap"]!='undefined' && chartData[chartId]["filterMap"]!=""){}else{
       var display='';
       if(typeof chartData[chartId]["filters"]!='undefined' && Object.keys(chartData[chartId]["filters"]).length!=0){
       display='block';
    }else{
         display='none';
    } 
    
    var anchorChart=chartData["chart1"]["anchorChart"];
    if(typeof chartData["chart1"]["anchorChart"]!=='undefined' && chartData["chart1"]["anchorChart"]!==''){
        var keys1=Object.keys(chartData["chart1"]["anchorChart"]);
        var initCharts=chartData["chart1"]["anchorChart"][keys1[0]];
        for(var j in initCharts){
            if(chartId==initCharts[j]){
                divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='initicon_'"+chartId+" title = 'Initialized chart' style='width:14px;height:12px' src='images/anchor.png' alt='Options' onclick='disableInitChart(\"" + chartId + "\")'></img></span>"+
                "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
            }
        }
    }
//    Edited by Faiz Ansari
    if(typeof chartData["chart1"]["anchorChart"]!=='undefined' && chartData["chart1"]["anchorChart"]!=='' && Object.keys(anchorChart)[0] ===chartId){
        divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Define Initialize Charts' style='width:12px;height:12px' src='images/driver.png' alt='Options' onclick='initializeGraph1(\"" + chartId + "\",\"prev\")'></img></span>"+
        "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
    }
//edit by shivam
    if(typeof chartData[chartId]["chartType"] !== "undefined"  && chartData[chartId]["chartType"] == "world-map"|| chartData[chartId]["chartType"]=="World-City" || chartData[chartId]["chartType"]=="World-Top-Analysis"){
    divcontlist += "<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getGeoWorldMap(\"" + chartId + "\")'><i class='fa fa-globe'></i></span></td>";
    }
    if(typeof chartData[chartId]["chartType"] !== "undefined"  && chartData[chartId]["chartType"] == "Trend-Combo"){
      if( typeof chartData[chartId]["trendViewMeasures"] !== "undefined" && chartData[chartId]["trendViewMeasures"] === "ViewBys"){}else{
    divcontlist += "<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getParentMeasure(\"" + chartId + "\")'><i class='fa fa-level-up'></i></span></td>";
    }
}
     var showhide="";
    if(chartData[chartId]["iconShowHide"]=="No"){showhide="none";}
    if(typeof typeof chartData[chartId]["chartType"]!="undefined" && chartData[chartId]["chartType"]!="" && (chartData[chartId]["chartType"]=="Trend-Combo" || chartData[chartId]["chartType"]=="Trend-KPI")){
//    divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Comparison' style='margin-left:20%;width:14px;height:13px;padding:2px' src='images/icons/Comparison.png' alt='Options' onclick='getComparableViews(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='compare"+chartId+"' class='dropdown-menu'></ul></td>";
      divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"prev\")' ></i></span>"+
    "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
      divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"prev\")' ></i></img></span>"+
    "<ul id='currPrevRecords"+chartId+"' class=''></ul></td>";

      divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"next\")' ></i></span>"+
    "<ul id='currNextRecords"+chartId+"' class=''></ul></td>";
      divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"next\")' ></i></span>"+
    "<ul id='nextRecords"+chartId+"' class=''></ul></td>";
         
         }
            divcontlist+="</ul></td>";
//       divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' title = 'Local Filter' style='width:10px;display:"+display+";height:10px' src='images/filter_red.png' alt='Options'></img></span>"+
//    "<ul  class='dropdown'></ul></td>";
	}
   // add new refersh
      divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer;z-index:2;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Refresh' style='width:10px;height:10px' src='images/refersh_image.png' alt='Options' onclick='localRefresh(\"" + chartId + "\")'></img></span>"+
    "<ul id='newRefresh"+chartId+"' class=''></ul></td>";

       if((window.userType !=="A")||(window.userType !=="ANALYZER")){
    //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
      if(typeof type!=="undefined" && type == "trend"){
    divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtTrend(\"" + chartId + "\")' ></img></span>"+
    "</td>";
      }else {
//          divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtCharts(\"" + chartId + "\")' ></img></span>"+
          divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='savePointXtCharts()' ></img></span>"+
    "</td>";
      }
       }
//    divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'drill up' style='margin-left:20%;width:16px;height:16px' src='images/drillupgrey.png' alt='Options' onclick='drillUp(\"" + chartId + "\")' ></img></span>"+
//    "<ul id='drillUp"+chartId+"' class=''></ul></td>";
         divcontlist+="<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = '"+translateLanguage.Drill_Up+"' width='10' height='10' style='margin-left:20%' src='images/drillupgrey1.png' alt='Options'  onclick='createDrillUpPath(\"" + chartId + "\",event)' ></img></span>"+
        "<ul id='drillUp1"+chartId+"' class='dropdown-menu'>";

            divcontlist+="</ul></td>";

//divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' title = 'Change Measure' style='margin-left:20%;' src='images/icons/changeMeasure.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")' ></img></span>"+
//         "<ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></td>";
    //        divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:11px;height:14px' src='images/treeViewImages/Dim.gif' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//    divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:10px;heigh:10px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//    "<ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></td>";
    //        divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'delete Chart' style='margin-left:20%;width:16px;height:16px' src='images/deleteWidget.png' alt='Options' onclick='deleteChart(\"" + chartId + "\")' ></img></span>"+
    "<ul id='delete"+chartId+"' class=''></ul></td>";
    divcontlist+="<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' style='margin-left:20%;' title='Change Graphs' src='images/bar_icon.png' alt='Options' onclick='adjustPosition(\""+chartId+"\",event)'  ></img></span>"+
    "<ul id='chartTypesX"+chartId+"' class='dropdown-menu'>";
    var graphTypes = Object.keys(parent.graphsName);
    for(var i=0;i<parent.chartGroups.length;i++)
    {
        divcontlist+="<li id='chartGroupsX"+chartId+i+"' class='dropdown-submenu pull-left1'><a onmouseover='openChartGroup("+i+",\""+chartId+"\",\"X\",\""+parent.chartGroups[i]+"\")' >"+parent.chartGroups[i]+"</a>";
    }
    divcontlist+="</ul></td>";

    divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img  style='width:10px;height:10px;' title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\",event)' ></img></span>"+
    "<ul id='graphOption"+chartId+"' class='dropdown-menu'></ul></td></tr></table>"+
    "<table style='width:100%;height:8px;'></table>";
    "<div id='quickFilterDiv"+chartId+"' style='display:block'></div>";
    "<div id='breadcrumpId' style='display:block'></div>";
    "<div id='breadcrumpId"+chartId+"' style='display:block'></div>";
}
divcontlist +=        "<div align='center' id='chart" + (k + 1) + "' style='height:95%' ;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
"<div id='" + commentId + "' style='display:none'></div>";

//                "<span class=\"dashComment\" onclick='openChartComment(\""+chartId+"\")'></span>"+



divcontlist +=       "</div>" ;

var drilledvalue1 = parent.$("#drills").val();
var drilledvalue = {};
var  drilledvalue3 = [];
var drilledvalue2 = [];
var drilltype = $("#drilltype").val();
var htmlcontent = "";
if(chartId == breadCrump){
    if (typeof drilledvalue1 !== 'undefined' && drilledvalue1.length > 0  && (typeof drilltype !="undefined" && drilltype==="within")) {

        divcontlist +=   "<div id = breadcrump'"+chartId+"' style='display:block;margin-top:-15px;padding-left: 1em;' ><table><tr style = 'width:'"+widths[k]+"'>";

        if(drilledvalue1!="undefined" && drilledvalue1!=""){

            drilledvalue2 = JSON.parse(drilledvalue1);
            drilledvalue3 = Object.keys(drilledvalue2);

            for(var i=0;i<drilledvalue3.length;i++){
                drilledvalue[i] = drilledvalue2[drilledvalue3[i]]
                divcontlist +=    " <td style ='color:#808080;padding-left: .5em;'>"+drilledvalue[i]+" ></td>";
            }
        }
        divcontlist +=    "</tr></table></div>";
    }
}
//edited by manik for dynamic div
if (checkBrowser() != "ie" ) {
    divcontlist += "<div id='" + tableId + "''></div></li>";
}
else{
    divcontlist += "<div id='" + tableId + "''></div></div>";
}
if(fromoneview!='null'&& fromoneview=='true'){

    if(chartData[chartId]["row"] != "undefined"){
        id=parseInt(chartData[chartId]["id"]);
        row=parseInt(chartData[chartId]["row"]);
        col=parseInt(chartData[chartId]["col"]);
        sizeGrid_x=parseInt(chartData[chartId]["size_x"]);
        sizeGrid_y=parseInt(chartData[chartId]["size_y"]);
    // sizeGrid_x=10;
    // sizeGrid_y=7;
    }else{

        sizeGrid_x=15;
        sizeGrid_y=9;

        if(k%2==0){
            col=1;
            row=(parseInt(k) + 9);
        }else{
            col=16;
        }
    }
}else{

    //alert("chartdata "+JSON.stringify(chartData["chart" + (k + 1)]))
    if(typeof chartData["chart" + (k + 1)]["row"] != "undefined"){
        id=parseInt(chartData["chart" + (k + 1)]["id"]);
        row=parseInt(chartData["chart" + (k + 1)]["row"]);
        col=parseInt(chartData["chart" + (k + 1)]["col"]);
        sizeGrid_x=parseInt(chartData["chart" + (k + 1)]["size_x"]);
        sizeGrid_y=parseInt(chartData["chart" + (k + 1)]["size_y"]);
    }else{
        // sizeGrid_x=4;
        // sizeGrid_y=3;
        sizeGrid_x=15;
        sizeGrid_y=9;

        // if(k%2==0){
        //      col=1;
        //      row=k+2;
        //    }else{
        //      col=5;
        //        }
        if(k%2==0){
            col=1;
            row=(parseInt(k) + 9);
        }else{
            col=16;
        }

    }
}
if (checkBrowser() != "ie" ) {
    if(fromoneview!='null'&& fromoneview=='true'){
        //    alert("dic"+divcontlist)
        if(action1=='add'){
            grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
        }else{
            grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);
        }
    //    grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);

    }else{
        grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
    //   grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
    }
}else{
                
    $("#xtendChartssTD").append(divcontlist);
}
//grid.add_widget(divcontlist,parseInt(sizeGrid_x),parseInt(sizeGrid_y),parseInt(col),parseInt(row));
//grid.add_widget(divcontlist,15,9,col,row);

$("#xtendChartssTD").show();
parent.$("#gridsterobj").val(grid);
}else{
    loopLength = (charts.length)
    for (var k = 0; k < loopLength; k++) {
        divcontlist="";
        //        var widthPer = "50%";//$(window).width()/2+"px";
        var singleLine = "1px";
        var chartDetails = chartData["chart" + (k + 1)];
        chartId = "chart" + (parseInt(k) + 1);
        if(fromoneview!='null'&& fromoneview=='true'){
            chartId=charts;
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
        }
        else if (widFact === 0) {
            singleLine = "0px";
        }
        else {
            singleLine = "2px";
        }
        if(fromoneview!='null'&& fromoneview=='true'){
            if(typeof chartData[chartId] !=="undefined" && chartData[chartId]["size"] === "M"){
                bottomLine = "1px";
            }
            //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
            //            bottomLine = "1px";
            //            widFact = 0;
            //        }
            divHeight = $(window).height() / 1.7;
            var chartName = "";
            if (typeof chartData[chartId]["Name"] !== "undefined" && chartData[chartId]["Name"] !== "") {
                chartName = chartData[chartId]["Name"];
            }
            else {
                chartName = chartId;
            }
        }
        else{

            if(typeof chartData["chart" + (k + 2)] !=="undefined" && chartData["chart" + (k + 2)]["size"] === "M"){
                bottomLine = "1px";
            }
            //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
            //            bottomLine = "1px";
            //            widFact = 0;
            //        }
            divHeight = $(window).height() / 1.7;
            var chartName = "";
            if (typeof chartData["chart" + (k + 1)] !=="undefined" && typeof chartData["chart" + (k + 1)]["Name"] !== "undefined" && chartData["chart" + (k + 1)]["Name"] !== "") {
                chartName = chartData["chart" + (k + 1)]["Name"];
            }
            else {
                chartName = "chart" + (k + 1);
            }
        }
        if(typeof chartData["chart" + (k + 1)]!=="undefined" && typeof chartData[chartId]["chartType"] !== "undefined"  && (chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedHorizontal-Bar")){

            widthPer = "100%"
        }

        var viewbys = chartDetails.viewBys;
        var measures = chartDetails.meassures;
        var chartMeasure = [];
        chartMeasure.push(measures[0]);
        var chartViewby = [];
        chartViewby.push(viewbys[0]);
        if(fromoneview!='null'&& fromoneview=='true'){
            //                     chartId=charts;
            if (typeof divcount !== 'undefined'){
                divcount;
                k=divcount-1;
            }else{
                divcount=k+1;
            }
        }
        //edited by manik for dynamic div
        //        divContent += "<td class='DroppableDiv' id='divchart" + (k + 1) + "' style='overflow:auto;position:relative;width:" + widthPer + ";height:" + divHeight + ";float:left;'>" +
        if (checkBrowser() != "ie" ) {
            if(fromoneview!='null'&& fromoneview=='true'){
                divcontlist += "<li id='divchart" + (k + 1) + "' data-row='"+row+"' data-col='"+col+"' data-sizex='"+sizeGrid_x+"' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >" ;
            }else{
                divcontlist += "<li id='divchart" + (k + 1) + "' data-row='"+row+"' data-col='"+col+"' data-sizex='"+sizeGrid_x+"' ondrop='drop(event,this.id)' class='dragClass' ondragover='allowDrop(event)' data-sizey='2' style=' border: 1px dotted rgb(200, 200, 200);' >" ;
            }
        }else{
            divcontlist += "<div id='divchart" + (k + 1) + "'  style='float:left;width:42%;height:56%;margin-left:0.5%;margin-top:0.5%; border: 1px dotted rgb(200, 200, 200);' >" ;
        }
        divcontlist +=      "<table id='tablechart" + (k + 1) + "' align='left' class='dashHeader' style='display:none'>" +
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
        if(typeof chartData[chartId]["chartType"] != "undefined"  && ( chartData[chartId]["chartType"] != "Radial-Chart" && chartData[chartId]["chartType"] != "Standard-KPI"  && chartData[chartId]["chartType"] != "Standard-KPI1" && chartData[chartId]["chartType"] != "KPIDash" && chartData[chartId]["chartType"] != "Bullet-Horizontal" && chartData[chartId]["chartType"] != "Emoji-Chart" && chartData[chartId]["chartType"] != "Dial-Gauge")){
         
          if(fromoneview!='null'&& fromoneview=='true'){
                divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +

                "<table style='width:100%'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:70%'><span id='dbChartName"+chartId+"'><a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+k+"&REPORTID="+repId+"')\"><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></a></span></td>";
        
            }else{
                // Added By Ram For change color of chart name after applying measurefilter
                if(typeof chartData[chartId]["isFilterApplied"]!="undefined" && chartData[chartId]["isFilterApplied"]==="Yes")
                {
                    divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +

                    "<table style='width:100%'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:70%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:red;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></span></td>";
                }
                else
                {
                    divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:' ></span></a></td></tr></table>" +

                    "<table style='width:100%'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:70%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></span></td>";
                }
            }
        }
        else{

            divcontlist +=    "<span id='dbChartName" + chartId + "' style ='display:'></span></a></td></tr></table>" +

            "<table style='width:100%'><tr ><td id='renameTitle"+chartId+"' style='padding-left:1%;width:81%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span></td>";

        }
        if(fromoneview!='null'&& fromoneview=='true'){

             var  drilledvalue3 = [];
               drilledvalue3 = parent.$("#timedetails").val();
            //                var fun="callGraphOpt11("+k+")";
            //                     //divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt1(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",'"+chartName+"','null','"+repId+"','"+repname+"','null')' ></img></span>"+
            //                     divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:80%' src='images/moreIcon.png' alt='Options' onclick=\""+fun+"\"></img></span>"+
            //                              "<ul id='graphOptionchart"+(k+1)+"' class='dropdown-menu' ></ul></td></tr></table>"+
            //        "<div id='breadcrumpId' style='display:block'></div>";
            divcontlist+="<td style='width:1%'><div class='dropdown' style='cursor:pointer'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16' style='margin-left:20%' src='images/cal.jpg' alt='Options'  onclick='customTimeAggregation(" + (k + 1) + ",\""+oneviewid1+"\",\""+chartId+"\")' ></img></span></td>"
  divcontlist+="<td  style='width:1%;'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><a  class =\" ui-icon ui-icon-clock\"  onmouseout='hidetimedetails(" + (k + 1) + ")' onmouseover='showtimedetails(" + (k + 1) + ",\""+oneviewid1+"\",\""+chartId+"\",\""+drilledvalue3+"\")'></a></span><ul class='dropdown-menu' id='timeOption"+(k+1)+"' ></ul></td>";
            if(chartData[chartId]["chartType"] != "Standard-KPI" || chartData[chartId]["chartType"] != "Standard-KPI1"){
                divcontlist+="<td style='width:1%'><div class='dropdown' style='cursor:pointer'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16' title = 'olapgraph' style='margin-left:20%' src='images/chart1.png' alt='Options'  onclick='parent.olapGraph(\""+repname+"\",\""+repId+"\",\""+oneviewid1+"\",null,null,false," + (k + 1) + ",\""+chartname1+"\",null)' ></img></span></td>"
            }
            divcontlist+="<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='16' height='16'title = 'deleteregion' style='margin-left:20%' src='images/deleteWidget.png' alt='Options' onclick='deleteGraph( \"divchart" + (k + 1) + "\",\""+oneviewid1+"\"," + (k + 1) + ")' ></img></span></td>"

            divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:20%;' title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt1( \""+ chartId + "\",\""+ tableId + "\"," + (k + 1) + ",\""+chartName+"\",null,\""+repId+"\",\""+repname+"\",\""+enablefilter+"\")'></img></span>"+
            "<ul id='graphOptionchart"+(k+1)+"' class='dropdown-menu' ></ul></td></tr></table>"+
            "<div id='quickFilterDivchart"+(k+1)+"' style='display:block'></div>";
            "<div id='breadcrumpId' style='display:block'></div>";
        //        callGraphOpt11 = function(k){
        //    callGraphOpt1( chartId,tableId,(k + 1),chartName,chartData,repId,repname,enablefilter)
        //    }
        }
        else{
            //add by mayank sh. for top bottom chart
//            divcontlist += "<td style='width:1%'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='topBottom"+chartId+"' title = 'Top Bottom' style='width:18px;height:12px' src='images/topBottom.png' alt='Options' onclick='topBottomChart(\"" + chartId + "\",event)'></img></span>"+
//    "<ul id='topBottom1"+chartId+"' class=''></ul></td>";
        divcontlist+="<td id='localfilter_red"+chartId+"' style='width:1%;' class=''><div class='' style='cursor:pointer;z-index:2;'><span  data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' width='10' height='10' style='display:none;margin-left:20%' src='images/filter_red.png' alt='Options' onmouseover='showAppliedFilters(\"" + chartId + "\",event)'  ></img></span>"+
        "<ul id='localFilter1"+chartId+"' class=''>";
              var type =$("#type").val();
       
          
           
		  if(typeof chartData[chartId]["filterMap"]!='undefined' && chartData[chartId]["filterMap"]!=""){}else{
      var display='';
       if(typeof chartData[chartId]["filters"]!='undefined' && Object.keys(chartData[chartId]["filters"]).length!=0){
       display='block';
    }else{
         display='none';
    }
    var anchorChart=chartData["chart1"]["anchorChart"];
    if(typeof chartData["chart1"]["anchorChart"]!=='undefined' && chartData["chart1"]["anchorChart"]!==''){
        var keys1=Object.keys(chartData["chart1"]["anchorChart"]);
        var initCharts=chartData["chart1"]["anchorChart"][keys1[0]];
        for(var j in initCharts){
            if(chartId==initCharts[j]){
                divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='initicon_'"+chartId+" title = 'Initialized chart' style='width:14px;height:12px' src='images/anchor.png' alt='Options' onclick='disableInitChart(\"" + chartId + "\")'></img></span>"+
                "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
            }
        }
    }
    if(typeof chartData["chart1"]["anchorChart"]!=='undefined' && chartData["chart1"]["anchorChart"]!=='' && Object.keys(anchorChart)[0] ===chartId){    
        var keys2=Object.keys(anchorChart);
        var initChartsList=anchorChart[keys2[0]];
        if(initChartsList.length>0){
            divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Define Initialize Charts' style='width:12px;height:12px' src='images/driver.png' alt='Options' onclick='initializeGraph1(\"" + chartId + "\",\"prev\")'></img></span>"+
            "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
        }
    }
    if(typeof chartData[chartId]["chartType"] !== "undefined"  && chartData[chartId]["chartType"] == "world-map"){
    divcontlist += "<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getGeoWorldMap(\"" + chartId + "\")'><i class='fa fa-globe'></i></span></td>";
    }
    if(typeof chartData[chartId]["chartType"] !== "undefined"  && chartData[chartId]["chartType"] == "Trend-Combo"){
  if( typeof chartData[chartId]["trendViewMeasures"] !== "undefined" && chartData[chartId]["trendViewMeasures"] === "ViewBys"){}else{
    divcontlist += "<td style='width:1%' class=''><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none' onclick='getParentMeasure(\"" + chartId + "\")'><i class='fa fa-level-up'></i></span></td>";
    }
}
//    Edited by Faiz Ansari
    var showhide="";
    if(chartData[chartId]["iconShowHide"]=="No"){showhide="none";}
    if(typeof typeof chartData[chartId]["chartType"]!="undefined" && chartData[chartId]["chartType"]!="" &&  (chartData[chartId]["chartType"]=="Trend-Combo" || chartData[chartId]["chartType"]=="Trend-KPI")){
//     divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:11;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Comparison' style='margin-left:20%;width:14px;height:13px;padding:2px' src='images/icons/Comparison.png' alt='Options' onclick='getComparableViews(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='compare"+chartId+"' class='dropdown-menu'></ul></td>"; 
     divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"prev\")' ></i></span>"+
    "<ul id='prevRecords"+chartId+"' class=''></ul></td>";
      divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-left' title = 'Prev Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"prev\")' ></i></img></span>"+
    "<ul id='currPrevRecords"+chartId+"' class=''></ul></td>";

      divcontlist += "<td style='width:1%'display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getNextRecords(\"" + chartId + "\",\"next\")' ></i></span>"+
    "<ul id='currNextRecords"+chartId+"' class=''></ul></td>";
      divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><i class='fa fa-angle-double-right' title = 'Next Records' style='color: grey;font-size:15px' onclick='getMoreRecords(\"" + chartId + "\",\"next\")' ></i></span>"+
    "<ul id='nextRecords"+chartId+"' class=''></ul></td>";
}

            divcontlist+="</ul></td>";
//      divcontlist += "<td style='width:1%'><div  class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img id='newLocalFilter"+chartId+"' title = 'Local Filter' style='width:10px;display:"+display+";height:10px' src='images/filter_red.png' alt='Options'></img></span>"+
//    "<ul  class='dropdown'></ul></td>";
	}
//        add new refresh
            divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Refresh' style='width:10px;height:10px' src='images/refersh_image.png' alt='Options' onclick='localRefresh(\"" + chartId + "\")'</img></span>"+
            "<ul id='newRefresh"+chartId+"' class=''></ul></td>";
//        alert(callGraphOpt);
            //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
            if((window.userType !=="A")||(window.userType !=="ANALYZER")){
    //        divcontlist += "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' src='images/icon_compress.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
      if(typeof type!=="undefined" && type == "trend"){
    divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtTrend(\"" + chartId + "\")' ></img></span>"+
    "</td>";
      }else {
//          divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='saveXtCharts(\"" + chartId + "\")' ></img></span>"+
          divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='' style='cursor:pointer'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Save Chart' style='margin-left:20%;width:10px;height:10px' src='images/SaveGraph.png' alt='Options' onclick='savePointXtCharts()' ></img></span>"+
    "</td>";
      }
       }
//            divcontlist += "<td style='width:1%'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'drill up' style='margin-left:20%;width:10px;height:10px' src='images/drillupgrey.png' alt='Options' onclick='drillUp(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='drillUp"+chartId+"' class=''></ul></td>";

         divcontlist+="<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = '"+translateLanguage.Drill_Up+"' width='10' height='10' style='margin-left:20%' src='images/drillupgrey1.png' alt='Options' onclick='createDrillUpPath(\"" + chartId + "\",event)'  ></img></span>"+
        "<ul id='drillUp1"+chartId+"' class='dropdown-menu'>";

            divcontlist+="</ul></td>";

//          divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' title = 'Change Measure' style='margin-left:20%;' src='images/icons/changeMeasure.png' alt='Options' onclick='changeMeasureGroup(\"" + chartId + "\")' ></img></span>"+
//         "<ul id='measureChange"+chartId+"' class='dropdown-menu'></ul></td>";

//            divcontlist += "<td style='width:1%;display:"+showhide+"' class='showHide"+chartId+"'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img title = 'Change ViewBy's' style='margin-left:20%;width:10px;height:10px' src='images/changeView.png' alt='Options' onclick='changeViewByGroup(\"" + chartId + "\")' ></img></span>"+
//            "<ul id='viewByChange"+chartId+"' class='dropdown-menu'></ul></td>";
            //        divcontlist += "<td style='width:1%'><div class='' style='position:relative;cursor:pointer;z-index:11;'><span data-toggle='' class='' style='text-decoration: none'><img title = 'Delete Chart' style='margin-left:20%;width:16px;height:16px' src='images/deleteWidget.png' alt='Options' onclick='deleteChart(\"" + chartId + "\")' ></img></span>"+
            "<ul id='delete"+chartId+"' class=''></ul></td>";
            divcontlist+="<td style='width:1%;' class=''><div class='dropdown' style='cursor:pointer;z-index:2;'><span  data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img width='10' height='10' style='margin-left:20%;' title='Change Graphs' src='images/bar_icon.png' alt='Options'  onclick='adjustPosition(\""+chartId+"\",event)' ></img></span>"+
            "<ul id='chartTypesX"+chartId+"' class='dropdown-menu'>";
            var graphTypes = Object.keys(parent.graphsName);
            for(var i=0;i<parent.chartGroups.length;i++)
            {
                divcontlist+="<li id='chartGroupsX"+chartId+i+"' class='dropdown-submenu pull-left1'><a onmouseover='openChartGroup("+i+",\""+chartId+"\",\"X\")' >"+parent.chartGroups[i]+"</a>";
            }
            divcontlist+="</ul></td>";
       
            divcontlist += "<td style='width:1%'><div class='dropdown' style='cursor:pointer;z-index:2;'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:;width:10px;height:10px;' title='Options' src='images/moreIcon.png' alt='Options' onclick='callGraphOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\",event)' ></img></span>"+
            "<ul id='graphOption"+chartId+"' class='dropdown-menu'></ul></td></tr></table>"+
            "<div id='quickFilterDiv"+chartId+"' style='display:block'></div>";
            "<div id='breadcrumpId' style='display:block'></div>";
            "<div id='breadcrumpId"+chartId+"' style='display:block'></div>";
        }
        divcontlist +=        "<div align='center' id='chart" + (k + 1) + "' style='height:95%' ;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
        "<div id='" + commentId + "' style='display:none'></div>";

        //                "<span class=\"dashComment\" onclick='openChartComment(\""+chartId+"\")'></span>"+



        divcontlist +=       "</div>" ;

        var drilledvalue1 = parent.$("#drills").val();
        var drilledvalue = {};
        var  drilledvalue3 = [];
        var drilledvalue2 = [];
        var drilltype = $("#drilltype").val();
        var htmlcontent = "";
        if(chartId == breadCrump){
            if (typeof drilledvalue1 !== 'undefined' && drilledvalue1.length > 0  && (typeof drilltype !="undefined" && drilltype==="within")) {

                divcontlist +=   "<div id = breadcrump'"+chartId+"' style='display:block;margin-top:-15px;padding-left: 1em;' ><table><tr style = 'width:'"+widths[k]+"'>";

                if(drilledvalue1!="undefined" && drilledvalue1!=""){

                    drilledvalue2 = JSON.parse(drilledvalue1);
                    drilledvalue3 = Object.keys(drilledvalue2);

                    for(var i=0;i<drilledvalue3.length;i++){
                        drilledvalue[i] = drilledvalue2[drilledvalue3[i]]
                        divcontlist +=    " <td style ='color:#808080;padding-left: .5em;'>"+drilledvalue[i]+" ></td>";
                    }
                }
                divcontlist +=    "</tr></table></div>";
            }
        }
        //edited by manik for dynamic div
        if (checkBrowser() != "ie" ) {
            divcontlist += "<div id='" + tableId + "''></div></li>";
        }
        else{
            divcontlist += "<div id='" + tableId + "''></div></div>";
        }
			 
        if(fromoneview!='null'&& fromoneview=='true'){
            if(chartData[chartId]["row"] != "undefined"){
                id=parseInt(chartData[chartId]["id"]);
                row=parseInt(chartData[chartId]["row"]);
                col=parseInt(chartData[chartId]["col"]);
                sizeGrid_x=parseInt(chartData[chartId]["size_x"]);
                sizeGrid_y=parseInt(chartData[chartId]["size_y"]);
            // sizeGrid_x=10;
            // sizeGrid_y=7;
            }else{

                sizeGrid_x=15;
                sizeGrid_y=9;

                if(k%2==0){
                    col=1;
                    row=(parseInt(k) + 9);
                }else{
                    col=16;
                }
            }
        }else{
    
            //alert("chartdata "+JSON.stringify(chartData["chart" + (k + 1)]))
            if(typeof chartData["chart" + (k + 1)]["row"] != "undefined"){
                id=parseInt(chartData["chart" + (k + 1)]["id"]);
                row=parseInt(chartData["chart" + (k + 1)]["row"]);
                col=parseInt(chartData["chart" + (k + 1)]["col"]);
                sizeGrid_x=parseInt(chartData["chart" + (k + 1)]["size_x"]);
                sizeGrid_y=parseInt(chartData["chart" + (k + 1)]["size_y"]);
            }else{
                // sizeGrid_x=4;
                // sizeGrid_y=3;
                sizeGrid_x=15;
                sizeGrid_y=9;

                // if(k%2==0){
                //      col=1;
                //      row=k+2;
                //    }else{
                //      col=5;
                //        }
                if(k%2==0){
                    col=1;
                    row=(parseInt(k) + 9);
                }else{
                    col=16;
                }

            }
        }
        if (checkBrowser() != "ie" ) {
            if(fromoneview!='null'&& fromoneview=='true'){
                //    alert("dic"+divcontlist)
                if(action1=='OLAPGraphRegion'){
                    divcontlist="";
                    divcontlist +=        "<div align='center' id='olapchart" + (k + 1) + "' style='height:95%' ;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
                    "<div id='" + commentId + "' style='display:none'></div></div>";
                    $("#OLAPGraphRegion").html(divcontlist);
                }
                else{
                    if(action1=='add'){
                        grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
                    }else{
                        grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);
                    }
                }
            //    grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);

            }else{
                grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y,col,row);
            //   grid.add_widget(divcontlist,sizeGrid_x,sizeGrid_y);
            }
        }else{
                
            $("#xtendChartssTD").append(divcontlist);
        }

        //grid.add_widget(divcontlist,parseInt(sizeGrid_x),parseInt(sizeGrid_y),parseInt(col),parseInt(row));
        //grid.add_widget(divcontlist,15,9,col,row);
    
        $("#xtendChartssTD").show();
        parent.$("#gridsterobj").val(grid);
        if(fromoneview!='null'&& fromoneview=='true'){
            break;
        }

    }
}

var data = graphData;
var radius = 600/3.5;
for (var kq = 0; kq < charts.length; kq++) {
    //              var ch = "chart" + parseInt(kq+1);
    var ch;
    if(fromoneview!='null'&& fromoneview=='true'){
        ch=charts;
    }else{
        ch = "chart" + parseInt(kq+1);
    }
    var currData=[];
    var records = 12;
    if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
        records = chartData[ch]["records"];
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        if(JSON.stringify(graphData)=="{}"||JSON.stringify(data[ch])=="[]" || data[ch]=="undefined" ){

        }else{
            for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
                currData.push(data[ch][m]);
            }
            chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);
            if(fromoneview!='null'&& fromoneview=='true'){
                break;
            }
        }
    }else{
        for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
            currData.push(data[ch][m]);
        }
        chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);
              
    }
}
    setTimeout(  $("#div"+newProp).css({
'box-shadow': '2px 2px 5px rgb(145, 255, 0)'
        }),3000);
    try{    
        if(true){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var filters=chartData[chartId]["filters"];
    var filterKeys=Object.keys(filters);
    var viewBys=JSON.parse(parent.$("#viewby").val());
    var viewByIds=JSON.parse(parent.$("#viewbyIds").val());
    for(var i in filterKeys){
        var filterGroupBy=viewBys[viewByIds.indexOf(filterKeys[i])];
        var filters1 = filterData[filterGroupBy];
        var filterValues=filters[filterKeys[i]]
        var inFilters=$(filters1).not(filterValues).get();
             
        if(inFilters.length > 0){
            $("#localfilter_red"+chartId).css({
                "display":""
            });
        }else{
            $("#localfilter_red"+chartId).css({
                "display":"none"
            });
}
    }
}
    }catch(err) {}
    if(typeof idArr!=="undefined"){
    var drillKeys =  Object.keys(chartData);
    var driver = parent.$("#driver").val();
    for(var c in drillKeys){
        if(driver!=="undefined" && driver!=drillKeys[c]){
            
    $("#spanChartNameDate"+drillKeys[c]).text(idArr.split(":")[0]);
        }else{
            $("#spanChartNameDate"+drillKeys[c]).text();
}
    }
    }
}

//Added By Ram
function submiturls12(url){

    //                    edited by manik
    parent.document.frmParameter.action = url+'&isdrilltype=true&fromOneview=true&DEFAULT_TAB=Graph';
    parent.document.frmParameter.target = "_blank";
    parent.document.frmParameter.submit();
    parent.document.frmParameter.target = "";
//document.frmParameter.submit();
}




function generateTrend(jsonData){
    $("#xtendChart").html('');
    if(typeof (jsonData)!=Object){
        graphData=JSON.parse(jsonData);
    }
    var chartData = JSON.parse($("#chartData").val());
    var divContent="";
    var chartId="";
    var charts = Object.keys(chartData);
    var width = $(window).width();
    var divHeight = $(window).height();
    var divContent = "";
    var widths = [];
    var heights = [];
    var widFact = 0;
    var heightFact = 0;
    var j = 0;
    var sameRowFlag = true;
    //     divContent += "<div  style='width:100%'><table style='width:88%;float:left'><tr id='row0'>";
    divContent += "<div  style='width:100%'><table style='width:"+width+"px;float:left'><tr id='row0'>";
    for (var k = 0; k < charts.length; k++) {
        var heightper = "10%";
        //        var widthPer = "50%";
        var widthPer = $(window).width()/2+"px";
        var singleLine = "1px";
        //        var chartType = chartList[(k + 1)];
        var chartDetails = chartData["chart" + (k + 1)];
        //        var dataPath = tarPath + "/" + "chart" + (k + 1) + ".json";
        chartId = "chart" + (parseInt(k) + 1);
        var tableId = "table" + (parseInt(k) + 1);
        var commentId = "comment" + (parseInt(k) + 1);
        var graphID = "graph" + (parseInt(k) + 1);
        //  var width1=[];
        if (typeof chartData["chart" + (k + 1)]["size"] !="undefined" && chartData["chart" + (k + 1)]["size"] === "M") {

            if (k === 0 && chartData["chart" + (k + 2)]["size"] !== "M") {
                sameRowFlag = false;
            }
            if (!sameRowFlag && typeof rowMap['row' + j] !== "undefined" && rowMap['row' + j].length > 0) {
                sameRowFlag = true;
            }
            if (((typeof rowMap['row' + j] !== "undefined" && k === (charts.length - 1)) || ((typeof rowMap['row' + j] !== "undefined" && rowMap['row' + j].length > 0) && (typeof chartData["chart" + (k)]=="undefined"?false:chartData["chart" + (k)]["size"] !== "S")) || (typeof chartData["chart" + (k + 2)] !== "undefined" && chartData["chart" + (k + 2)]["size"] === "M")) && (k === 0 || !sameRowFlag || chartData["chart" + (k)]["size"] === "M")) {
                widthPer = "48.6%";
                widths.push($(window).width() * .88);
            } else {
                widthPer = "65.3%";
                widths.push($(window).width() * 1.33);
            }
            heights.push($(window).height() * 1.5);
            heightFact = (heightFact) + (66.47);
            widFact = (widFact) + (66.47);

            heightper = "66.47%";
        }
        else if (typeof chartData["chart" + (k + 1)]["size"] !="undefined" && chartData["chart" + (k + 1)]["size"] === "L") {
            widths.push($(window).width() * 2);
            heights.push($(window).height() * 2.5);
            widthPer = "98.5%";
            heightper = "100%";
            widFact = (widFact) + (90.47);
            heightFact = (heightFact) + (90.47);
        }
        else {
            widths.push(width / 1.5);
            heights.push(divHeight / 1.5);
            widFact = (widFact) + (45);
            heightFact = (heightFact) + (33.23);
        }
        if (widFact > 68) {
            widFact = 0;
            singleLine = "0px";
        }
        else if (widFact === 0) {
            singleLine = "0px";
        }
        else {
            singleLine = "2px";
        }

        if(typeof chartData["chart" + (k + 2)] !=="undefined" && chartData["chart" + (k + 2)]["size"] === "M"){
            bottomLine = "1px";
        }
        //        if (chartData["chart" + (k + 1)]["size"] === "S" && chartData["chart" + (k + 1)]["size"] === "M") {
        //            bottomLine = "1px";
        //            widFact = 0;
        //        }
        divHeight = $(window).height() / 1.7;
        var chartName = "";
        if (typeof chartData["chart" + (k + 1)]["Name"] !== "undefined" && chartData["chart" + (k + 1)]["Name"] !== "") {
            chartName = chartData["chart" + (k + 1)]["Name"];
        }
        else {
            chartName = "chart" + (k + 1);
        }
        var viewbys = chartDetails.viewBys;
        var measures = chartDetails.meassures;
        var chartMeasure = [];
        chartMeasure.push(measures[0]);
        var chartViewby = [];
        chartViewby.push(viewbys[0]);
        divContent += "<div  id='kpi_div" + (k + 1) + "' style='display: none; width:50%; height:50%; ' onresize=\"showKpiDiv1('kpi_div" + (k + 1) + "')\"  class='kpiDiv dragClass'></div>";

        divContent += "<td class='DroppableDiv' id='divchart" + (k + 1) + "' style='overflow:auto;position:relative;width:" + widthPer + ";height:" + divHeight + ";float:left;'>" +
        "<table id='tablechart" + (k + 1) + "' align='left' class='dashHeader' style='display:none'>" +
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
        "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('" + chartId + "')\"><span id='dbChartName" + chartId + "'></span></a></td></tr></table>" +
        "<table style='width:100%'><tr><td id='renameTitle"+chartId+"' style='text-align:center;width:70%'><span id='dbChartName"+chartId+"'><tspan style=' font-size:18px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartName+"</tspan></span></td>"+
        "<td style='width:30%'><div class='dropdown' style='cursor:pointer'><span data-toggle='dropdown' class='dropdown-toggle' style='text-decoration: none'><img style='margin-left:70%' title=\'Graph Options\' src='images/icon_compress.png' alt='Options' onclick='callTrendOpt(\"" + chartId + "\",\"" + tableId + "\"," + (k + 1) + ",\"" + divHeight + "\",\"" + widths[k] + "\")' ></img></span>"+
        "<ul id='graphOption"+chartId+"' class='dropdown-menu'></ul></td></tr></table>"+
        "<div align='center' id='chart" + (k + 1) + "' style='border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
        "<div id='" + commentId + "' style='display:none'></div>" +
        //                "<span class=\"dashComment\" onclick='openChartComment(\""+chartId+"\")'></span>"+
        "</div>" ;
        var drilledvalue1 = parent.$("#drills").val();
        var drilledvalue = {};
        var  drilledvalue3 = [];
        var drilledvalue2 = [];
        var drilltype = $("#drilltype").val();
        var htmlcontent = "";

        if(chartId == breadCrump){

            if (typeof drilledvalue1 !== 'undefined' && drilledvalue1.length > 0  && ((typeof drilltype !="undefined" && drilltype==="within" )|| drilltype==="")) {

                divContent +=   "<div id = breadcrump'"+chartId+"' style='display:block;margin-top:-15px;padding-left: 1em;' ><table><tr style = 'width:'"+widths[k]+"'>";

                if(drilledvalue1!="undefined" && drilledvalue1!=""){

                    drilledvalue2 = JSON.parse(drilledvalue1.split(","));

                    drilledvalue3 = Object.keys(drilledvalue2);

                    for(var i=0;i<drilledvalue3.length;i++){

                        drilledvalue[i] = drilledvalue2[drilledvalue3[i]]

                        divContent +=    " <td style ='color:#808080;padding-left: .5em;'>"+drilledvalue[i]+" ></td>";
                    }
                }

                divContent +=    "</tr></table></div>";

            }


        }
        divContent +=    "<div id='" + tableId + "''></div></td>";// onmouseout=\"hideScroll(this.id)\" onmouseover=\"showScroll(this.id)\"
        if (typeof rowMap['row' + j] === "undefined") {
            rowMap['row' + j] = [];
        }
        rowMap['row' + j].push(chartId);

        if (singleLine === "0px" && k < (charts.length - 1)) {
            sameRowFlag = false;
            j++;
            divContent += "</tr><tr id='row" + (j) + "'>";

        }
    }
    divContent += "</tr></table></div>";

    //    divContent +="<table id='legendsTable' style='width:11%;float:left'><tr><td>Legends..</td></tr></table>";
    $("#noData").hide();
    //    $("#xtendChartTD1").show();
    $("#xtendChartTD").show();
    $("#xtendChartTD").html(divContent);
    var data = graphData;
    var radius = 600/3.5;
    for (var k = 0; k < charts.length; k++) {
        var ch = "chart" + parseInt(k+1);
        var currData=[];
        var records = 10;
        if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
            records = chartData[ch]["records"];
        }
        for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
            currData.push(data[ch][m]);
        }
        chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);

    }
}



function showEmoji(chartId,val){
    var flag=$("#"+chartId+"showEmoji").is(":checked");
    var chartData = JSON.parse($("#chartData").val());
    if(flag===true){
        $("#"+chartId+"emojiDiv1").show();
        $("#"+chartId+"emojiDiv2").show();
        if(typeof chartData[chartId]["showEmoji"]!=='undefined' || chartData[chartId]["showEmoji"]=='target'){
            selectEmojiType(chartId, 'target');
        }else if(chartData[chartId]["showEmoji"]=='absolute'){
            selectEmojiType(chartId, 'absolute');
        }
        else{
            selectEmojiType(chartId, 'target');
        }
    }
    else{
        $("#"+chartId+"emojiDiv1").hide();
        $("#"+chartId+"emojiDiv2").hide();
        $("#emojiCondition").hide();
    }
}
function selectEmojiType(chartId,val){
    var html=""; 
    var chartData = JSON.parse($("#chartData").val());
    $("#emojiCondition").html('');
    var measures=chartData[chartId]["meassures"];
    if(val==='absolute'){
        //        alert("absolute");
        html += "<table style='margin:10px'>";
        html += "<tr><td>";
        html += "<select id='measureName'>";
        for(var i in measures){
            if(typeof chartData[chartId]["emojiAbsValue"]!=='undefined' && chartData[chartId]["emojiAbsValue"][0]==measures[i]){
                html += "<option value='"+measures[i]+"' selected>"+measures[i]+"</option>";
            }
            else{
                html += "<option value='"+measures[i]+"'>"+measures[i]+"</option>";
            }
        }
        //        alert(html);
        var val1=0;
        if(typeof chartData[chartId]["emojiAbsValue"] !== "undefined" && chartData[chartId]["emojiAbsValue"] !=='' ){
            val1=chartData[chartId]["emojiAbsValue"][1];
        }
        else{
            val1=0;
        }
        html += "</select></td><td><input id='emojiValue' type='text' value='"+val1+"'/>";
        html += "</td></tr>";
        html += "</table>";
    }
    else{
        html += "<table style='margin:10px'>";
        for(var i in measures){
            var value1=0;
            if(typeof chartData[chartId]["emojiValue"] !== 'undefined' ){
                if(chartData[chartId]["emojiValue"][i] !=='' && chartData[chartId]["emojiValue"][i] !==null){
                value1=chartData[chartId]["emojiValue"][i];
            }
            }
            else{
                value1=0;
            }
            //            alert("val : "+value1);
            html += "<tr><td>";
            if(typeof chartData[chartId]["emojiVisible"]==='undefined' || chartData[chartId]["emojiVisible"][i]===false){
                html += "<label style='font: 11px verdana;white-space:nowrap'><input type='checkbox' id='visible"+i+"'/>"+measures[i]+"</label></td><td><input id='emojiValue"+i+"' type='text' value='"+value1+"'/>";
            }
            else{
                html += "<label style='font: 11px verdana;white-space:nowrap'><input type='checkbox' id='visible"+i+"' checked/>"+measures[i]+"</label></td><td><input id='emojiValue"+i+"' type='text' value='"+value1+"'/>";
            }
            html += "</td></tr>";
        }
        html += "</table>";    
    }
    //    alert(html);
    $("#emojiCondition").html(html);
    $("#emojiCondition").show();
}
function yaxisrange(chartId,value){
    var chartData = JSON.parse($("#chartData").val());
    if(value!="Default" && value!="MinMax" ){
        $("#"+chartId+"yaxisfilter").show();
//        $("#"+chartId+"Y2axis").show();// comment by mayank sharma 
        if(chartData[chartId]["chartType"]=="OverLaid-Bar-Line"){
            $("#"+chartId+"y1axisfilter").show();
            $("#"+chartId+"y2axisfilter").show();

        }
    }else{
        $("#"+chartId+"yaxisfilter").hide();
        $("#"+chartId+"y1axisfilter").hide();
        $("#"+chartId+"y2axisfilter").hide();
//        $("#"+chartId+"Y2axis").hide(); // comment by mayank sharma 
    }
}
function y1axisrange(chartId,value){ // add by mayank sharma 
    var chartData = JSON.parse($("#chartData").val());
    if(value!="Default" && value!="MinMax" ){
//        $("#"+chartId+"yaxisfilter").show();
        $("#"+chartId+"Y2axis").show();
        if(chartData[chartId]["chartType"]=="OverLaid-Bar-Line"){
            $("#"+chartId+"y1axisfilter").show();
            $("#"+chartId+"y2axisfilter").show();

        }
    }else{
//        $("#"+chartId+"yaxisfilter").hide();
        $("#"+chartId+"y1axisfilter").hide();
        $("#"+chartId+"y2axisfilter").hide();
        $("#"+chartId+"Y2axis").hide();
    }
}  // end by mayank sharma

function setChartDetails(chartId){
    //    Added by Faiz Ansari
    var ele=document.getElementsByClassName("showHide"+chartId);
    if($("#"+chartId+"iconShowHide").val()=="No"){ 
        for(var i in ele){
            $(ele[i]).css("display","none");
        }
    }
    else{
        for(var i in ele){
            $(ele[i]).css("display","");
        }
    try{
    if(true){
    
    var chartData=JSON.parse(parent.$("#chartData").val());
    var filters=chartData[chartId]["filters"];
    var filterKeys=Object.keys(filters);
    var viewBys=JSON.parse(parent.$("#viewby").val());
    var viewByIds=JSON.parse(parent.$("#viewbyIds").val());
    for(var i in filterKeys){
        var filterGroupBy=viewBys[viewByIds.indexOf(filterKeys[i])];
        var filters1 = filterData[filterGroupBy];
        var filterValues=filters[filterKeys[i]]
        var inFilters=$(filters1).not(filterValues).get();
//        alert(inFilters.length);
        if(inFilters.length > 0){
            $("#localfilter_red"+chartId).css({
                "display":""
            });
        }else{
            $("#localfilter_red"+chartId).css({
                "display":"none"
            });
    }
    }
    }
    }
catch(err) {}
 }
//    End!!!
   
   
//    $("#loading").show();
    var chartData = JSON.parse($("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var nameVal = $("#" + chartId+ "Name").val();
    chartData[chartId]["Name"] = nameVal;
    var chartDecription = $("#"+ chartId+"chartDecription").val();
    chartData[chartId]["chartDecription"] = chartDecription; 
    var value = $("#"+ chartId+"valueOf").val();
    chartData[chartId]["valueOf"] = value;
    var quickFilterLength = $("#"+chartId+"quickFilterLength").val();
    chartData[chartId]["quickFilterLength"] = quickFilterLength;
    
      var completeChartData = "";
    if($("#"+chartId+"_completeChartData").prop("checked")){
        completeChartData="Yes";
          $("#"+chartId+"hideDate").prop('checked', false);
            hideDate="N";
        }
    
    chartData[chartId]["completeChartData"] = completeChartData;
      var timeEnable = "";
    if($("#"+chartId+"_timeEnable").prop("checked")){
        timeEnable="Yes";
    }else{
        timeEnable="No";
    }
    chartData[chartId]["timeEnable"] = timeEnable;
     
     if(chartData[chartId]["chartType"]=="Multi-View-Tree"){
     var equalInterval = "";
    if($("#"+chartId+"_equalInterval").prop("checked")){
        equalInterval="Yes";
    }else{
        equalInterval="No";
    }
    chartData[chartId]["equalInterval"] = equalInterval;
    }
    var chartRecords = $("#"+chartId+"Records").val();
    chartData[chartId]["records"] = chartRecords;
    var innerRecords = $("#"+chartId+"InnerRecords").val();
    chartData[chartId]["innerRecords"] = innerRecords;
    var dataDisplay = $("#"+chartId+"dataDisplay").val();
    chartData[chartId]["dataDisplay"] = dataDisplay;
    var sorting = $("#"+chartId+"sorting").val();
    chartData[chartId]["sorting"] = sorting;
    var sortBasis = $("#"+chartId+"sortBasis").val();
    chartData[chartId]["sortBasis"] = sortBasis;
    var trendViewMeasures = $("#"+chartId+"trendViewMeasures").val();
    chartData[chartId]["trendViewMeasures"] = trendViewMeasures;
    var rounding = $("#"+chartId+"rounding").val();
    chartData[chartId]["rounding"] = rounding;
    var rounding1 = $("#"+chartId+"rounding1").val();
    chartData[chartId]["rounding1"] = rounding1;
    var yAxisFormat = $("#"+chartId+"yAxisFormat").val();
    chartData[chartId]["yAxisFormat"] = yAxisFormat;
    var y2AxisFormat = $("#"+chartId+"y2AxisFormat").val();
    chartData[chartId]["y2AxisFormat"] = y2AxisFormat;
    var displayXLine = $("#"+chartId+"displayXLine").val();
    chartData[chartId]["displayXLine"] = displayXLine;
    var displayYLine = $("#"+chartId+"displayYLine").val();
    chartData[chartId]["displayYLine"] = displayYLine;
    var displayX = $("#"+chartId+"displayX").val();
    chartData[chartId]["displayX"] = displayX;
    var displayY = $("#"+chartId+"displayY").val();
    chartData[chartId]["displayY"] = displayY;
    var displayY1 = $("#"+chartId+"displayY1").val();
    chartData[chartId]["displayY1"] = displayY1;
    var displayLegends = $("#"+chartId+"displayLegends").val();
    chartData[chartId]["displayLegends"] = displayLegends;
    
    var dateFlag_New = $("#"+chartId+"dateFlag_New").val();
    chartData[chartId]["dateFlag_New"] = dateFlag_New;
    
    var innerSorting = $("#"+chartId+"innerSorting").val();
    chartData[chartId]["innerSorting"] = innerSorting;
    var innerSortBasis = $("#"+chartId+"innerSortBasis").val();
    chartData[chartId]["innerSortBasis"] = innerSortBasis;
    var SubTotalProp = $("#"+chartId+"SubTotalProp").val();
    chartData[chartId]["SubTotalProp"] = SubTotalProp;
    var GridLines = $("#"+chartId+"GridLines").val();
    chartData[chartId]["GridLines"] = GridLines;
    if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='DualAxis-Bar')){  
    var GridLines1 = $("#"+chartId+"GridLines1").val();
    chartData[chartId]["GridLines1"] = GridLines1;
    }
    var Target = $("#"+chartId+"Target").val();
    chartData[chartId]["Target"] = Target;
    var targetLine = $("#"+chartId+"targetLine").val();
    chartData[chartId]["targetLine"] = targetLine;
      var legendNo = $("#"+chartId+"legendNo").val();
    chartData[chartId]["legendNo"] = legendNo;
    // add by maynk sh. for fontsize
   var iconShowHide = $("#"+chartId+"iconShowHide").val();
    chartData[chartId]["iconShowHide"] = iconShowHide;
    
   var legendPrintType = $("#"+chartId+"legendPrintType").val();
    chartData[chartId]["legendPrintType"] = legendPrintType;
    
    var valueTilealign = $("#"+chartId+"valueTilealign").val();
    chartData[chartId]["valueTilealign"] = valueTilealign;
   
   var gtGraph = "";
    if($("#"+chartId+"gtGraph").prop("checked")){
        gtGraph="Y";
    }else{
        gtGraph="N";
    }
    chartData[chartId]["gtGraph"] = gtGraph;
   var absoluteValue = "";
    if($("#"+chartId+"absoluteValue").prop("checked")){
        absoluteValue="Yes";
    }else{
        absoluteValue="No";
    }
    chartData[chartId]["absoluteValue"] = absoluteValue;
   var multiselecctQuickFilter = "";
    if($("#"+chartId+"multiselecctQuickFilter").prop("checked")){
        multiselecctQuickFilter="Yes";
    }else{
        multiselecctQuickFilter="No";
    }
    chartData[chartId]["multiselecctQuickFilter"] = multiselecctQuickFilter;
    
    var fontsize = $("#"+chartId+"fontsize").val();
    //alert(fontsize);
    if (fontsize<9)
        {
          fontsize=9;  
        }
    chartData[chartId]["fontsize"] = fontsize; 
    // end by maynk sh. for fontsize
//    alert( chartData[chartId]["fontsize"]);
  var usBar = $("#"+chartId+"usBar").val();
    chartData[chartId]["usBar"] = usBar;
    // add by maynk sh. for Line type
    var lineType = $("#"+chartId+"lineType").val();
    chartData[chartId]["lineType"] = lineType; // end by maynk sh. for line type
   // add by maynk sh. for Ldiv border
    var hideBorder = $("#"+chartId+"hideBorder").val();
    chartData[chartId]["hideBorder"] = hideBorder; // end by maynk sh. for divBorder
   var tableBorder = "";
    if($("#"+chartId+"tableBorder").prop("checked")){
        tableBorder="Y";
    }else{
        tableBorder="N";
    }
    chartData[chartId]["tableBorder"] = tableBorder; // end by maynk sh. for divBorder
   var tableWithSymbol = "";
    if($("#"+chartId+"tableWithSymbol").prop("checked")){
        tableWithSymbol="Y";
    }else{
        tableWithSymbol="N";
    }
    chartData[chartId]["tableWithSymbol"] = tableWithSymbol; // end by maynk 

    var headingFont = $("#"+chartId+"headingFontSize").val();
    chartData[chartId]["headingFontSize"] = headingFont; // end by maynk sh. for headingFontSize
    
    var backColor =$("#"+chartId+"backColor").val();
     chartData[chartId]["backColor"] = backColor; 
    
    var KPIName = $("#" + chartId+ "KPIName").val();
    chartData[chartId]["KPIName"] = KPIName;

    var Tilealign = $("#"+chartId+"Tilealign").val();
    chartData[chartId]["Tilealign"] = Tilealign;
    //Added By Ram
    var XaxisRange = $("#"+chartId+"XaxisRange").val();
    chartData[chartId]["XaxisRange"] = XaxisRange;

    var Tilealign = $("#"+chartId+"Tilealign").val();
    chartData[chartId]["Tilealign"] = Tilealign;
    var Suffix = $("#" + chartId+ "Suffix").val();
    if(typeof Suffix=='undefined' || Suffix.trim()=='undefined'){
        Suffix='';
    }
    chartData[chartId]["Suffix"] = " "+Suffix.trim();

    var labelPosition = $("#"+chartId+"labelPosition").val();
    chartData[chartId]["labelPosition"] = labelPosition;
    
    var imageType = $("#"+chartId+"imageType").val();
    chartData[chartId]["imageType"] = imageType;
    
    var emojiPosition = $("#"+chartId+"emojiPosition").val();
    chartData[chartId]["emojiPosition"] = emojiPosition;
    
    var Prefix = $("#" + chartId+ "Prefix").val();
    if(typeof Prefix=='undefined' || Prefix.trim()=='undefined'){
        Prefix='';
    }
    chartData[chartId]["Prefix"] = Prefix;

    var Prefixfontsize = $("#" + chartId+ "Prefixfontsize").val();
    chartData[chartId]["Prefixfontsize"] = Prefixfontsize;

    var Suffixfontsize = $("#" + chartId+ "Suffixfontsize").val();
    chartData[chartId]["Suffixfontsize"] = Suffixfontsize;
//    if(typeof $("#" + chartId+ "PrefixList_"+no).val()!="undefined" && $("#" + chartId+ "PrefixList_"+no).val()!=""){
//    }
//     if(typeof $("#" + chartId+ "PrefixfontsizeList_"+no).val()!="undefined" && $("#" + chartId+ "PrefixfontsizeList_"+no).val()!=""){
    var PrefixfontsizeList = [];
    for(var no in chartData[chartId]["meassures"]){
       PrefixfontsizeList.push($("#" + chartId+ "PrefixfontsizeList_"+no).val());
    }
    chartData[chartId]["PrefixfontsizeList"]=PrefixfontsizeList;
    var timeBasedData = [];
    var timeList = ["3Month","6Month","1Year","2Year"];
    for(var no in timeList){
       timeBasedData.push($("#" + chartId+ "timeBasedData").val());
    }
    chartData[chartId]["timeBasedData"]=timeBasedData;
//     }
//      if(typeof $("#" + chartId+ "SuffixList_"+no).val()!="undefined" && $("#" + chartId+ "SuffixList_"+no).val()!=""){
    if(!(typeof $("#" + chartId+ "PrefixList_0").val()==='undefined' || $("#" + chartId+ "PrefixList_0").val()==='undefined')){
        var PrefixList = [];
        for(var no in chartData[chartId]["meassures"]){
            PrefixList.push($("#" + chartId+ "PrefixList_"+no).val());
        }

        chartData[chartId]["PrefixList"]=PrefixList;
    
    var SuffixList = [];
    for(var no in chartData[chartId]["meassures"]){
     SuffixList.push($("#" + chartId+ "SuffixList_"+no).val());
    }
    chartData[chartId]["SuffixList"]= SuffixList;
    }
//      }
//      if(typeof $("#" + chartId+ "SuffixfontsizeList_"+no).val()!="undefined" && $("#" + chartId+ "SuffixfontsizeList_"+no).val()!=""){
    var SuffixfontsizeList = [];
    for(var no in chartData[chartId]["meassures"]){
     SuffixfontsizeList.push($("#" + chartId+"SuffixfontsizeList_"+no).val());
    }
    chartData[chartId]["SuffixfontsizeList"]=SuffixfontsizeList;
//      }
    var dialSelectChart = $("#dialSelectChart").val();
    chartData[chartId]["dialSelectChart"] = dialSelectChart;
    
     var dialMeasureSuffix = $("#"+chartId+"dialMeasureSuffix").val();
   chartData[chartId]["dialMeasureSuffix"] = dialMeasureSuffix;

//    Added by shivam
     userColor=$("#AxisNameColor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var AxisNameColor = userColor;
    chartData[chartId]["AxisNameColor"] = AxisNameColor;
    }
   userColor=$("#stackLight").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var stackLight = userColor;
    chartData[chartId]["stackLight"] = stackLight;
    }
     userColor=$("#stackDark").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var stackDark = userColor;
    chartData[chartId]["stackDark"] = stackDark;
    }
    
//    Added by shivam

     userColor=$("#FilledColor1").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var FilledColor1 = userColor;
    chartData[chartId]["FilledColor1"] = FilledColor1;
    }
    
    
     userColor=$("#FilledColor2").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var FilledColor2 = userColor;
    chartData[chartId]["FilledColor2"] = FilledColor2;
    }
    
    
     userColor=$("#FilledColor3").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var FilledColor3 = userColor;
    chartData[chartId]["FilledColor3"] = FilledColor3;
    }
     userColor=$("#FilledColor4").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var FilledColor4 = userColor;
    chartData[chartId]["FilledColor4"] = FilledColor4;
    }
    
    userColor=$("#divBackGround").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var divBackGround = userColor;
    chartData[chartId]["divBackGround"] = divBackGround;
    }
    
    userColor=$("#chartNameColol").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var chartNameColol = userColor;
    chartData[chartId]["chartNameColol"] = chartNameColol;
    }
    
    userColor=$("#_colorPicker").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var kpiColor = userColor;
    chartData[chartId]["colorPicker"] = kpiColor;
    }
    if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Table' || chartData[chartId]["chartType"]==='Grouped-Map' || chartData[chartId]["chartType"]==='Grouped-Table')){  
      userColor=$("#TableFontColor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var TableFontColor = userColor;
    chartData[chartId]["TableFontColor"] = TableFontColor;
    }
    }
    
    if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Grouped-Table' || chartData[chartId]["chartType"]==='Grouped-Map')){  
     userColor=$("#HeadingBackGround").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var HeadingBackGround = userColor;
    chartData[chartId]["HeadingBackGround"] = HeadingBackGround;
    }
     userColor=$("#HeadingColor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var HeadingColor = userColor;
    chartData[chartId]["HeadingColor"] = HeadingColor;
    }
    }
    var hideLabel = [];
    for(var i=0;i<measures.length;i++){
        if(typeof $("#hideLabel_"+i+"_"+chartId).val() !="undefined" && $("#hideLabel_"+i+"_"+chartId).val() !=null){
            hideLabel.push($("#hideLabel_"+i+"_"+chartId).val());
        }
    }
    if(typeof hideLabel !="undefined" && hideLabel !=null && hideLabel !=""){
        chartData[chartId]["hideLabel"] = hideLabel;
    }
    var others = "";
    if($("#"+chartId+"_others").prop("checked")){
        others="Y";
    }else{
        others="N";
    }
    chartData[chartId]["others"] = others;

    var transpose = "";
    if($("#"+chartId+"_transpose").prop("checked")){
        transpose="Y";
    }else{
        transpose="N";
    }
    chartData[chartId]["transpose"] = transpose;
    
    var showPercent = "";
    if($("#"+chartId+"showPercent").prop("checked")){
        showPercent="Y";
    }else{
        showPercent="N";
    }
    chartData[chartId]["showPercent"] = showPercent;

    var showEmoji = "";
    if($("#"+chartId+"showEmoji").prop("checked")){
        showEmoji=$("#"+chartId+"emojiType").val();
    }else{
        showEmoji='hidden';
    }
    chartData[chartId]["showEmoji"]=showEmoji;

    var emojiValue = [];
    var emojiVisible = [];
    if($("#"+chartId+"emojiType").val()==='absolute'){
        emojiValue[0]=$("#measureName").val();
        emojiValue[1]=$("#emojiValue").val();
        chartData[chartId]["emojiAbsValue"]=emojiValue;
    }else{
        for(i in measures){
            emojiVisible[i]=$("#visible"+i).is(':checked');
            emojiValue[i]=$("#emojiValue"+i).val();
        }
//        alert("Selection : "+emojiVisible);
        chartData[chartId]["emojiVisible"]=emojiVisible;
        chartData[chartId]["emojiValue"]=emojiValue;
    }
    var innerLabels = "";
    if ($("#" + chartId + "innerLabels").prop("checked")) {
        innerLabels = "Y";
        if (chartData[chartId]["chartType"] ==='Vertical-Bar' || chartData[chartId]["chartType"] === 'Vertical-Negative-Bar' || chartData[chartId]["chartType"] === 'OverLaid-Bar-Line') {
            chartData[chartId]["displayX"] = "No";
    }
        else if(chartData[chartId]["chartType"]==='Horizontal-Bar' ||chartData[chartId]["chartType"]==="Filled-Horizontal" || chartData[chartId]["chartType"]==="Combo-Horizontal"){
            chartData[chartId]["displayY"] = "No";
        }
    } else {
        innerLabels = "N";
    }
    chartData[chartId]["innerLabels"] = innerLabels;
    
    
    var hideBorder="";
     if($("#"+chartId+"hideBorder").prop("checked")){
        hideBorder="Y";
    }else{
        hideBorder="N";
    }
    chartData[chartId]["hideBorder"] = hideBorder;
    
    
    var aliasMap={};
    var measLen;
    var chartType=chartData[chartId]["chartType"];
    var measures = chartData[chartId]["meassures"];
   if(chartType ==="Horizontal-Bar" || chartType==="Combo-Horizontal" ||chartType ==="Vertical-Bar" || chartType==="Trend-Analysis" ||chartType ==="Line"||chartType ==="SmoothLine" ||chartType ==="MultiMeasureSmooth-Line"){
    measLen=1;
   }
   else{
    measLen=measures.length;
   }
   if(typeof chartData[chartId]["chartType"]!="undefined" && chartData[chartId]["chartType"]=="Trend-Combo"){
   var keys = Object.keys(chartData[chartId]["measureAlias"]);
   var measureAliasName = [];
   measureAliasName = keys;
   for(var meas in measures){
	measureAliasName.push(measures[meas]);
     }
         var temp1=[];
  label:for(iht=0;iht<measureAliasName.length;iht++){
        for(var jht=0; jht<temp1.length;jht++ ){//check duplicates
            if(temp1[jht]==measureAliasName[iht])//skip if already present
               continue label;
        }
        temp1[temp1.length] = measureAliasName[iht];
  }
	for(var temp in temp1){
	for(var key in keys){
	if(temp1[temp].toString().toLowerCase() ==keys[key].toString().toLowerCase()){
	aliasMap[temp1[temp]] = chartData[chartId]["measureAlias"][keys[key]];
	}
	}
	for(var meas1 in measures){ 
	if(temp1[temp].toString().toLowerCase() ==measures[meas1].toString().toLowerCase()){
	if($("#"+chartId+"measureAliastext"+i).val()!==""){
	aliasMap[temp1[temp]] =  $("#"+chartId+"measureAliastext"+meas1).val();
	}else{
	aliasMap[temp1[temp]] = measures[meas1]
	}
	}
	
	} 
	}
	}else{
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureAliastext"+i).val()!==""){
        aliasMap[measures[i]]=$("#"+chartId+"measureAliastext"+i).val();
    } else{ 
        aliasMap[measures[i]]= measures[i];
    }   
    }
  
}	
    chartData[chartId]["measureAlias"]=aliasMap;
//}
if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Combo-Analysis')){
     var measureAliasCombo={};
    var measLen;
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureAliasCombo"+i).val()!==""){
        measureAliasCombo[measures[i]]=$("#"+chartId+"measureAliasCombo"+i).val();
    }else{
        measureAliasCombo[measures[i]]= measures[i];
    }
    }
    chartData[chartId]["measureAliasCombo"]=measureAliasCombo;
  }

    var xAxisFont = $("#"+chartId+"xAxisFont").val();
    chartData[chartId]["xAxisFont"] = xAxisFont;
    
    var yAxisFont = $("#"+chartId+"yAxisFont").val();
    chartData[chartId]["yAxisFont"] = yAxisFont;
    
var lineSymbol = $("#"+chartId+"lineSymbol").val();
    chartData[chartId]["lineSymbol"] = lineSymbol;
 
     var lineSymbol1 = $("#"+chartId+"lineSymbol1").val();
    chartData[chartId]["lineSymbol1"] = lineSymbol1;  
 
   var kpiFont = $("#"+chartId+"kpiFont").val();
    chartData[chartId]["kpiFont"] = kpiFont; // end by maynk sh. for fontsize
    
   var kpiSubData = $("#"+chartId+"kpiSubData").val();
    chartData[chartId]["kpiSubData"] = kpiSubData; // end by SHIVAM for fontsize
    
    var kpiGTFont = $("#"+chartId+"kpiGTFont").val();
    chartData[chartId]["kpiGTFont"] = kpiGTFont; 
    // add by maynk sh. Show ST
    var showST = "";
    if($("#"+chartId+"showST").prop("checked")){
        showST="Y";
    }else{
        showST="N";
    }
    chartData[chartId]["showST"] = showST;
    // Show GT
    var showGT = "";
    if($("#"+chartId+"showGT").prop("checked")){
        showGT="Y";
    }else{
        showGT="N";
    }
    chartData[chartId]["showGT"] = showGT;
    // end by maynk sh. show GT ST
    
    var MaxRange = $("#"+chartId+"MaxRange").val();
    chartData[chartId]["MaxRange"] = MaxRange;

	var dataTranspose = $("#"+chartId+"dataTranspose").val();
	chartData[chartId]["dataTranspose"] = dataTranspose;

    var Pattern = $("#"+chartId+"Pattern").val();
    chartData[chartId]["Pattern"] = Pattern;

    var LabelPos = $("#"+chartId+"LabelPos").val();
    chartData[chartId]["LabelPos"] = LabelPos;

    var DualAxisProp = $("#"+chartId+"DualAxisProp").val();
    chartData[chartId]["DualAxisProp"] = DualAxisProp;

    var lableColor = $("#"+chartId+"lableColor").val();
    chartData[chartId]["lableColor"] = lableColor;
    
    if(isAxisChart(chartId)){
    var yaxisFilter = {};
    if(typeof $("#"+chartId+"Y-axisMin").val()!=="undefined"){
        yaxisFilter["axisMin"]=($("#"+chartId+"Y-axisMin").val())
    }else {
        yaxisFilter["axisMin"]= 0;
    }
    if(typeof $("#"+chartId+"Y-axisMax").val()!=="undefined"){
        yaxisFilter["axisMax"]=($("#"+chartId+"Y-axisMax").val())
    }else {
        yaxisFilter["axisMax"]= 100000;
    }
    if(typeof $("#"+chartId+"Y-axisTicks").val()!=="undefined"){
        yaxisFilter["axisTicks"]=($("#"+chartId+"Y-axisTicks").val())
    }else{
        yaxisFilter["axisTicks"]=5;
    }
    if(typeof $("#"+chartId+"Y-axisMin1").val()!=="undefined"){
        yaxisFilter["axisMin1"]=($("#"+chartId+"Y-axisMin1").val())
    }
    if(typeof $("#"+chartId+"Y-axisMax1").val()!=="undefined"){
        yaxisFilter["axisMax1"]=($("#"+chartId+"Y-axisMax1").val())
    }
    if(typeof $("#"+chartId+"Y-axisTicks1").val()!=="undefined"){
        yaxisFilter["axisTicks1"]=($("#"+chartId+"Y-axisTicks1").val())
    }

    if(typeof $("#"+chartId+"YaxisRangeType").val()!=="undefined"){
        yaxisFilter["YaxisRangeType"]=($("#"+chartId+"YaxisRangeType").val())
    }
    chartData[chartId]["yaxisrange"] = yaxisFilter;
    }
    //add by mayank sharma
    if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartType ==='DualAxis-Bar' )){
    var y1axisFilter = {};
    if(typeof $("#"+chartId+"Y-axisMin1").val()!=="undefined"){
        y1axisFilter["axisMin1"]=($("#"+chartId+"Y-axisMin1").val())
    }else {
        y1axisFilter["axisMin1"]= 0;
    }
    if(typeof $("#"+chartId+"Y-axisMax1").val()!=="undefined"){
        y1axisFilter["axisMax1"]=($("#"+chartId+"Y-axisMax1").val())
    }else {
        y1axisFilter["axisMax1"]= 100000;
    }
    
    if(typeof $("#"+chartId+"Y1axisRangeType").val()!=="undefined"){
        y1axisFilter["Y1axisRangeType"]=($("#"+chartId+"Y1axisRangeType").val())
    }
    chartData[chartId]["y1axisrange"] = y1axisFilter;
}  // end by mayank sharma 
    // dial property
    var dialType='';
//    if(typeof )
    var dialValues = {};
    if(typeof $("#rangeHigh_1").val()!=="undefined" && $("#rangeHigh_1").val()!==""){
        dialValues["minTarget"] = $("#rangeHigh_1").val();
    }else {
        dialValues["minTarget"] = 0;
    }
    if(typeof $("#rangeLow_2").val()!=="undefined" && $("#rangeLow_2").val()!==""){
        dialValues["maxTarget"] = $("#rangeLow_2").val();
    }else {
        dialValues["maxTarget"] = 25;
    }
    if(typeof chartData[chartId]["GTValueList"]!=="undefined" ){
        dialValues["dialGuageTarget"] = chartData[chartId]["GTValueList"][0];
    }else {
        dialValues["dialGuageTarget"] = 1000;
    }
    var keys =["High","Medium","Low"];
    for(var i=0;i<3;i++){
        if(typeof $("#range"+keys[i]+"_1").val()!=="undefined" && $("#range"+keys[i]+"_1").val()!=="" && typeof $("#range"+keys[i]+"_1").val()!=="undefined" && $("#range"+keys[i]+"_1").val()!==""){

            if(i==0){

                dialValues["minGreeen"] = $("#range"+keys[i]+"_1").val();
                dialValues["maxGreeen"] = $("#range"+keys[i]+"_2").val();
            }
            else if(i==1){

                dialValues["minOrange"] = $("#range"+keys[i]+"_1").val();
                dialValues["maxOrange"] = $("#range"+keys[i]+"_2").val();
            }
            else{
                dialValues["minRed"] = $("#range"+keys[i]+"_1").val();
                dialValues["maxRed"] = $("#range"+keys[i]+"_2").val();
            }

        }
    }

    chartData[chartId]["dialValues"] = dialValues;


	  var appendNumberFormat = "";
    if($("#"+chartId+"appendNumberFormat").prop("checked")){
        appendNumberFormat="Y";
    }else{
        appendNumberFormat="N";
    }
    chartData[chartId]["appendNumberFormat"] = appendNumberFormat;  
	   
if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Bullet-Horizontal')){	   
     var bulletParams=[];
     for(i in measures){
         var range={};
         range["target"]=$("#bulletTarget"+i).val();
          range["high"]=$("#bulletHigh"+i).val();
           range["mid"]=$("#bulletMid"+i).val();
            range["low"]=$("#bulletLow"+i).val();
         var measureMap={};
         measureMap[measures[i]]=range;
         bulletParams.push(measureMap);
     }  
     chartData[chartId]["bulletParameters"]=bulletParams;
    var userColor=$("#bulletAbovecolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var bulletAbovecolor = userColor;
    chartData[chartId]["bulletAbovecolor"] = bulletAbovecolor;
    }
    var userColor=$("#bulletBelowcolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var bulletBelowcolor = userColor;
    chartData[chartId]["bulletBelowcolor"] = bulletBelowcolor;
    }
    var userColor=$("#bulletHighcolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var bulletHighcolor = userColor;
    chartData[chartId]["bulletHighcolor"] = bulletHighcolor;
    }
    var userColor=$("#bulletMediumcolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var bulletMediumcolor = userColor;
    chartData[chartId]["bulletMediumcolor"] = bulletMediumcolor;
    }
    var userColor=$("#bulletLowcolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var bulletLowcolor = userColor;
    chartData[chartId]["bulletLowcolor"] = bulletLowcolor;
    }
} 


// add by mayank sharma for Logical Color

var logicalParameters=[];
 var logicalParametersMap = {};
 if(typeof $("#"+chartId+"condition").val()!=="undefined"){
        logicalParametersMap["condition"]=$("#"+chartId+"condition").val();
    }else {
        logicalParametersMap["condition"]= "";
    }
    if(typeof $("#"+chartId+"HighMax").val()!=="undefined"){
        logicalParametersMap["HighMax"]=$("#"+chartId+"HighMax").val();
    }else {
        logicalParametersMap["HighMax"]= "";
    }
    if(typeof $("#"+chartId+"HighMin").val()!=="undefined"){
        logicalParametersMap["HighMin"]=$("#"+chartId+"HighMin").val();
    }else {
        logicalParametersMap["HighMin"]= "";
    }
    var userColor=$("#logicalHighcolor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var logicalHighcolor = userColor;
    logicalParametersMap["logicalHighcolor"] = logicalHighcolor;
    }
    logicalParameters.push(logicalParametersMap);
    
    var logicalParametersMap1 = {};
 if(typeof $("#"+chartId+"condition1").val()!=="undefined"){
        logicalParametersMap1["condition1"]=($("#"+chartId+"condition1").val())
    }else {
        logicalParametersMap1["condition1"]= "";
    }
    if(typeof $("#"+chartId+"MidMax").val()!=="undefined"){
        logicalParametersMap1["MidMax"]=($("#"+chartId+"MidMax").val())
    }else {
        logicalParametersMap1["MidMax"]= "";
    }
    if(typeof $("#"+chartId+"MidMin").val()!=="undefined"){
        logicalParametersMap1["MidMin"]=($("#"+chartId+"MidMin").val())
    }else {
        logicalParametersMap1["MidMin"]= "";
    }
    var userColor=$("#logicalMidcolor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var logicalMidcolor = userColor;
    logicalParametersMap1["logicalMidcolor"] = logicalMidcolor;
    }
    logicalParameters.push(logicalParametersMap1);
    
    var logicalParametersMap2 = {};
 if(typeof $("#"+chartId+"condition2").val()!=="undefined"){
        logicalParametersMap2["condition2"]=($("#"+chartId+"condition2").val())
    }else {
        logicalParametersMap2["condition2"]= "";
    }
 if(typeof $("#"+chartId+"LowMax").val()!=="undefined"){
        logicalParametersMap2["LowMax"]=($("#"+chartId+"LowMax").val())
    }else {
        logicalParametersMap2["LowMax"]= "";
    }
 if(typeof $("#"+chartId+"LowMin").val()!=="undefined"){
        logicalParametersMap2["LowMin"]=($("#"+chartId+"LowMin").val())
    }else {
        logicalParametersMap2["LowMin"]= "";
    }
var userColor=$("#logicalLowcolor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var logicalLowcolor = userColor;
    logicalParametersMap2["logicalLowcolor"] = logicalLowcolor;
    }
    logicalParameters.push(logicalParametersMap2);
    chartData[chartId]["logicalParameters"] = logicalParameters;
// end by mayank sharma for Logical Color

var logicalColorDiv =$("#"+chartId+"logicalColorDiv").val();
     chartData[chartId]["logicalColorDiv"] = logicalColorDiv; 
     
var enableVieweby =$("#"+chartId+"enableVieweby").val();
     chartData[chartId]["enableVieweby"] = enableVieweby; 
     
var barsize =$("#"+chartId+"barsize").val();

     chartData[chartId]["barsize"] = barsize;

var enableMeasures =$("#"+chartId+"enableMeasures").val();
     chartData[chartId]["enableMeasures"] = enableMeasures; 

if(chartType==='Horizontal-Bar' ||chartType==='Filled-Horizontal' ||chartType==='Pie' || chartType==='Donut'|| chartType==='Bubble'||chartType==='Column-Pie'||chartType==='Column-Donut'){
var circularChartTab = $("#"+chartId+"circularChartTab").val();
    chartData[chartId]["circularChartTab"] = circularChartTab;
}
if(chartType==="Filled-Horizontal" || chartType==="Bar-Table"){
    userColor=$("#fillBackground").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var fillBackground = userColor;
    chartData[chartId]["fillBackground"] = fillBackground;
    }
}
if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Standard-KPI'|| chartData[chartId]["chartType"]==='Standard-KPI1' || chartData[chartId]["chartType"]==='Top-Analysis' || chartData[chartId]["chartType"]==='Combo-Analysis' || chartData[chartId]["chartType"]==='World-Top-Analysis')){	   
//if(chartType==='Standard-KPI'|| chartType==='Combo-Analysis' ||chartType==='Top-Analysis'||chartType==='World-Top-Analysis'){
var comparativeValue = $("#"+chartId+"comparativeValue").val();
    chartData[chartId]["comparativeValue"] = comparativeValue;
    
var changPercentArrow = $("#"+chartId+"changPercentArrow").val();
    chartData[chartId]["changPercentArrow"] = changPercentArrow;
}
          if(chartType==='Stacked-KPI'){   
var comparativeValueFont={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"comparativeValueFont"+i).val()!==""){
        comparativeValueFont[measures[i]]=$("#"+chartId+"comparativeValueFont"+i).val();
    } else{ 
        comparativeValueFont[measures[i]]= measures[i];
    }   
    }
    chartData[chartId]["comparativeValueFont"]=comparativeValueFont;
 }
 
     var XLable = $("#"+chartId+"editXLable").val();
    chartData[chartId]["editXLable"] = XLable;
    
 if(typeof chartData[chartId]["chartType"]!=="undefined" &&(chartType==='Double-Donut')){ 
    var dDLable = $("#"+chartId+"dDLable").val();
    chartData[chartId]["dDLable"] = dDLable;      
           
    var doublePie = $("#"+chartId+"doublePie").val();
    chartData[chartId]["doublePie"] = doublePie; 
 }  
      var dataLabDisplay = $("#"+chartId+"dataLabDisplay").val();
    chartData[chartId]["dataLabDisplay"] = dataLabDisplay;  
      
    var chartFontType = $("#"+chartId+"chartFontType").val();
    chartData[chartId]["chartFontType"] = chartFontType;

     userColor=$("#radialDefaultColor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var radialDefault = userColor;
    chartData[chartId]["radialDefaultColor"] = radialDefault;
    }

    var toolTip = $("#"+chartId+"toolTip").val();
    chartData[chartId]["toolTip"] = toolTip;
    
    var _dialType = $("#"+chartId+"_dialType").val();
    chartData[chartId]["dialType"] = _dialType; 
    
     userColor=$("#lFilledFont").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var lFilledFont = userColor;
    chartData[chartId]["lFilledFont"] = lFilledFont;
    }
    
    // for number format changes for every measures    
  var measLen; 
var numberFormat = {};
var chartType=chartData[chartId]["chartType"];
var measures = chartData[chartId]["meassures"];
 if(typeof chartData[chartId]["chartType"]!="undefined" && chartData[chartId]["chartType"]=="Trend-Combo"){
   var keys = Object.keys(chartData[chartId]["numberFormat"]);
   var measureAliasName = [];
   measureAliasName = keys;
   for(var meas in measures){
	measureAliasName.push(measures[meas]);
     }
         var temp1=[];
  label:for(iht=0;iht<measureAliasName.length;iht++){
        for(var jht=0; jht<temp1.length;jht++ ){//check duplicates
            if(temp1[jht]==measureAliasName[iht])//skip if already present
               continue label;
        }
        temp1[temp1.length] = measureAliasName[iht];
  }
	for(var temp in temp1){
	for(var key in keys){
	if(temp1[temp].toString().toLowerCase() ==keys[key].toString().toLowerCase()){
	numberFormat[temp1[temp]] = chartData[chartId]["numberFormat"][keys[key]];
	}
	}
	for(var meas1 in measures){ 
	if(temp1[temp].toString().toLowerCase() ==measures[meas1].toString().toLowerCase()){
	if($("#"+chartId+"measureAliastext"+i).val()!==""){
	numberFormat[temp1[temp]] =  $("#"+chartId+"numberFormatX"+meas1).val();
	}else{
	numberFormat[temp1[temp]] = ""
	}
	}
	
	} 
	}
	}else{
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"numberFormatX"+i).val()!==""){
        numberFormat[measures[i]]=$("#"+chartId+"numberFormatX"+i).val();
    } else{ 
        numberFormat[measures[i]]= "";
    }   
    }

}	

    chartData[chartId]["numberFormat"]=numberFormat;
    
    var dataDisplayArr={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"dataDisplayArr"+i).val()!==""){
        dataDisplayArr[measures[i]]=$("#"+chartId+"dataDisplayArr"+i).val();
    } else{ 
        dataDisplayArr[measures[i]]= "";
    }   
    }
    chartData[chartId]["dataDisplayArr"]=dataDisplayArr;
    
   // number rounding for every measure 
// for number format changes for every measures    
  var measLen; 
var numberRounding= {};
var chartType=chartData[chartId]["chartType"];
var measures = chartData[chartId]["meassures"];
 if(typeof chartData[chartId]["chartType"]!="undefined" && chartData[chartId]["chartType"]=="Trend-Combo"){
   var keys = Object.keys(chartData[chartId]["numberRounding"]);
   var measureAliasName = [];
   measureAliasName = keys;
   for(var meas in measures){
	measureAliasName.push(measures[meas]);
     }
         var temp1=[];
  label:for(iht=0;iht<measureAliasName.length;iht++){
        for(var jht=0; jht<temp1.length;jht++ ){//check duplicates
            if(temp1[jht]==measureAliasName[iht])//skip if already present
               continue label;
        }
        temp1[temp1.length] = measureAliasName[iht];
  }
	for(var temp in temp1){
	for(var key in keys){
	if(temp1[temp].toString().toLowerCase() ==keys[key].toString().toLowerCase()){
	numberRounding[temp1[temp]] = chartData[chartId]["numberRounding"][keys[key]];
	}
	}
	for(var meas1 in measures){ 
	if(temp1[temp].toString().toLowerCase() ==measures[meas1].toString().toLowerCase()){
	if($("#"+chartId+"numberRoundingX"+i).val()!==""){
	numberRounding[temp1[temp]] =  $("#"+chartId+"numberRoundingX"+meas1).val();
	}else{
	numberRounding[temp1[temp]] = measures[i];
	}
	}
	
	} 
	}
	}else{
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"numberRoundingX"+i).val()!==""){
        numberRounding[measures[i]]=$("#"+chartId+"numberRoundingX"+i).val();
    } else{ 
        numberRounding[measures[i]]= measures[i];
    }   
    }

}	

    chartData[chartId]["numberRounding"]=numberRounding;
 
    var measureLabelLength={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureLabelLength"+i).val()!==""){
        measureLabelLength[measures[i]]=$("#"+chartId+"measureLabelLength"+i).val();
    } else{ 
        measureLabelLength[measures[i]]= measures[i];
    }   
    }
    chartData[chartId]["measureLabelLength"]=measureLabelLength;
    
if(chartData[chartId]["chartType"]=="Grouped-Bar"){
    var groupedBarWith={};
//    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"groupedBarWith").val()!==""){
        groupedBarWith[$("#"+chartId+"groupedBarWith").val()]=$("#"+chartId+"groupedBarWith").val();
    } else{ 
        groupedBarWith[$("#"+chartId+"groupedBarWith").val()]= $("#"+chartId+"groupedBarWith").val()
    }   
//    }
    chartData[chartId]["groupedBarWith"]=groupedBarWith;
    }
   
    /*Added by Ashutosh*/
 if (chartData[chartId]["chartType"] === "Stacked-KPI" || chartData[chartId]["chartType"] === "Column-Pie" || chartData[chartId]["chartType"] === "Bullet-Horizontal") {
     var graphDrillMap = {};
     graphDrillMap["reportId"] = parent.$("#graphsId").val();
    for (var i=0;i<measures.length;i++) {
         if(parent.$("#reportToDrill_"+meassureIds[i]).val()=='0'){
           graphDrillMap[meassureIds[i]] = parent.$("#graphsId").val();
         }else{
         graphDrillMap[meassureIds[i]] = parent.$("#reportToDrill_"+meassureIds[i]).val();
         }
    }
//    alert(JSON.stringify(graphDrillMap));
     chartData[chartId]["graphDrillMap"]=graphDrillMap; 
 }
 try{
 var dataSlider={};
 var sliderCount=0;
// var dataSliderIdMap={};
// var meassureIds = chartData[chartId]["meassureIds"];
      for(var i=0;i<measures.length;i++){
      if($("#"+chartId+"dataSlider"+i).val()==='Yes'){
        sliderCount=sliderCount+1;
        }
        }
if(parent.draggable==="draggable" && sliderCount > 0){
        alert("For Data Slider Please Disable Draggable Chart First!!");
        }else{
      for(var i=0;i<measures.length;i++){
    if($("#"+chartId+"dataSlider"+i).val()!==""){
        
        if(sliderCount <=2){
            dataSlider[meassureIds[i]]=$("#"+chartId+"dataSlider"+i).val();
//        dataSlider[i]=dataSliderIdMap;
        //tooltipType[i]=$("#"+chartId+"dataSlider"+i).val();
    }else{
            alert("You can set maximum Two Measure on Slider!!");
            sliderCount=0;
            return;
    }
        
    }else{ 
        dataSlider[meassureIds[i]]== "No";
    }
    }
    //alert(JSON.stringify(dataSlider));
    chartData[chartId]["dataSlider"]=dataSlider;  
    }
//    var temDataSlider=chartData[chartId]["dataSlider"];
//    var temMeasurefilter=chartData[chartId]["measureFilters"];
//    for(var k in temDataSlider){
//        alert(k)
//        if(temDataSlider[k]==='No' && (k in temMeasurefilter)){
//            delete temMeasurefilter[k];
//        }
//    }
//    chartData[chartId]["measureFilters"]={};
    }catch(err){
        console.log("ERROR is dataslider");
    }
//    chartData[chartId]["sliderAxisVal"]=parent.range.sliderAxisVal;
	/*Added by Ashutosh*/
	var isType={};
	isType.charType=chartData[chartId]["chartType"];
if(!isCircularChart(chartId) && chartType!=="Trend-Analysis" && chartType!=="Influencers-Impact-Analysis2" && chartType!=="Influencers-Impact-Analysis" && chartType!=="Veraction-Combo1" 
        && chartType!=="Veraction-Combo3" && chartType!=="Transportation-Spend-Variance" && chartType!=="Variance-By-Mode" && chartType!=="Analysis" && chartType!=="Score-vs-Targets" && chartType!=="Scatter-Analysis" && !isKPIChart(chartId) && !isGroupedChart(chartId) && !isWorldMapChart(chartId) && !isIndiaStateMapChart(chartId)
	&&!isBubbleChart(chartId) && !isOtherChart(chartId)&& !(isType.charType==="Transpose-Table" || isType.charType==="Bar-Table" || isType.charType==="Horizontal-Bar-Table"
|| isType.charType==="Grouped-Table" || isType.charType==="Grouped-Map"|| isType.charType==="Cross-Table" || isType.charType==="Area" || isType.charType==="MultiMeasure-Area")){

    window.measure_seq=$("input[type='radio'][name='groupr']:checked").val();
    var sortMeasure = $("input[type='radio'][name='groupr']:checked");
    var selectedMeasure='';
    selectedMeasure=window.measure_seq;
    var sortMap={};
    sortMap[selectedMeasure]=$("#"+chartId+"sorting").val();
    chartData[chartId]["sortMeasure"]=sortMap;
    chartData[chartId]["measure_seq"]=$("input[type='radio'][name='groupr']:checked").val();
} 
 if(!(chartType=== "Pie" ||chartType=== "Column-Pie"||chartType=== "Column-Donut"||chartType=== "Combo-Analysis" ||chartType=== "Pie-3D" ||chartType=== "Bubble" 
||chartType==="Half-Pie" || chartType==="Donut" ||chartType==="Half-Donut" || chartType==="Donut-3D" || chartType==="Double-Donut")){  // add by mayank 
    var userColor=$("#lbcolor").css('background-color');
    if(typeof userColor!='undefined'){
   userColor = hexc(userColor);
    var lbColor = userColor;
    chartData[chartId]["lbColor"] = lbColor;
    }
    var showViewByinLBox = "";
    if($("#"+chartId+"showViewByinLBox").prop("checked")){
        showViewByinLBox="Y";
    }else{
        showViewByinLBox="N";
    }
    chartData[chartId]["showViewByinLBox"] = showViewByinLBox;
 
    var lbPosition = $("#"+chartId+"lbPosition").val();
    chartData[chartId]["lbPosition"] = lbPosition;
    
    var legendBoxBorder = $("#"+chartId+"legendBoxBorder").val();
    chartData[chartId]["legendBoxBorder"] = legendBoxBorder; 
}

if(typeof chartData[chartId]["chartType"]!=="undefined" &&(chartType=== "Pie" ||chartType=== "Column-Pie"||chartType=== "Column-Donut" 
||chartType==="Half-Pie" || chartType==="Donut" || chartType=== "Combo-Analysis"  ||chartType==="Half-Donut" || chartType==="Donut-3D")){
var valueOf1 = $("#"+chartId+"valueOf1").val();
    chartData[chartId]["valueOf1"] = valueOf1;
} 

if(chartType==="GroupedStackedH-Bar" || chartType=== "Pie" ||chartType=== "Column-Pie"||chartType=== "Column-Donut"||chartType=== "Combo-Analysis" ||chartType=== "Pie-3D" ||chartType=== "Bubble" 
||chartType==="Half-Pie" || chartType==="Donut" || chartType==="Split-Bubble" ||chartType==="Tree-Map"||chartType==="Half-Donut" || chartType==="Donut-3D" || chartType==="Double-Donut"|| chartType=== "Grouped-Bar" || chartType === "GroupedHorizontal-Bar" ||chartType=== "GroupedStacked-Bar"|| chartType==="Grouped-Line"|| chartType==="Grouped-MultiMeasureBar"||  chartType==="GroupedStacked-Bar%" ||  chartType==="GroupedStackedH-Bar%"){ 
    userColor=$("#labelColor").css('background-color');
    if(typeof userColor!='undefined'){
    userColor = hexc(userColor);
    var labelColor = userColor;
    chartData[chartId]["labelColor"] = labelColor;
    }
    
    var showViewBy="";
     if($("#"+chartId+"showViewBy").prop("checked")){
        showViewBy="Y";
    }else{
        showViewBy="N";
    }
    chartData[chartId]["showViewBy"] = showViewBy;
    
    var colorLegend = $("#"+chartId+"colorLegend").val();
    chartData[chartId]["colorLegend"] = colorLegend;
    
    var legendLocation = $("#"+chartId+"legendLocation").val();
    chartData[chartId]["legendLocation"] = legendLocation;
}    
    var measureNameSize={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureNameSize"+i).val()!==""){
        measureNameSize[measures[i]]=$("#"+chartId+"measureNameSize"+i).val();
    } else{ 
        measureNameSize[measures[i]]= measures[i];
    }   
    }
    chartData[chartId]["measureNameSize"]=measureNameSize;
    
    var measureValueSize={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureValueSize"+i).val()!==""){
        measureValueSize[measures[i]]=$("#"+chartId+"measureValueSize"+i).val();
    } else{ 
        measureValueSize[measures[i]]= measures[i];
    }   
    }
    chartData[chartId]["measureValueSize"]=measureValueSize;
    
    var ticksValueSize={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"ticksValueSize"+i).val()!==""){
        ticksValueSize[measures[i]]=$("#"+chartId+"ticksValueSize"+i).val();
    } else{ 
        ticksValueSize[measures[i]]= measures[i];
    }   
    }
    chartData[chartId]["ticksValueSize"]=ticksValueSize;
    
    if(chartData[chartId]["chartType"]==='Stacked-KPI' || chartData[chartId]["chartType"]==='Bullet-Horizontal'){
    var dataColor={};
    for(var i=0;i<measLen;i++){
        userColor=$("#dataColor"+i).css('background-color');
        userColor = hexc(userColor);
        if($("#dataColor"+i).val()!==""){
            dataColor[measures[i]]=userColor;
        }else{ 
            dataColor[measures[i]]= userColor;
        }   
    }
    chartData[chartId]["dataColor"]=dataColor;
    
    var measureColor={};
    for(var i=0;i<measLen;i++){
        userColor=$("#measureColor"+i).css('background-color');
        userColor = hexc(userColor);
        if($("#measureColor"+i).val()!==""){
            measureColor[measures[i]]=userColor;
        }else{ 
            measureColor[measures[i]]=userColor;
        }   
    }
    chartData[chartId]["measureColor"]=measureColor;
}
    var LabFtColor={};
    for(var i=0;i<measLen;i++){
        userColor=$("#LabFtColor"+i).css('background-color');
        if(typeof userColor !=="undefined"){
            
        userColor = hexc(userColor);
        }else {
            userColor = "#000000"
        }
        if($("#LabFtColor"+i).val()!==""){
            LabFtColor[measures[i]]=userColor;
        }else{ 
            LabFtColor[measures[i]]=userColor;
        }   
    }
    chartData[chartId]["LabFtColor"]=LabFtColor;
   
    var measureAliaskpi = $("#"+chartId+"measureAliaskpi").val();
    chartData[chartId]["measureAliaskpi"] = measureAliaskpi;
    
    var tooltipType={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"tooltipType"+i).val()!==""){
        tooltipType[measures[i]]=$("#"+chartId+"tooltipType"+i).val();
    } else{ 
        tooltipType[measures[i]]= "";
    }   
    }
    chartData[chartId]["tooltipType"]=tooltipType;
    
    
    if($("#"+chartId+"yAxisFormat").val()==='Auto'){
        var roundingType=parent.$("#"+chartId+"roundingType1").val();
        chartData[chartId]["roundingType"]=roundingType;
    }
//    alert(chartData[chartId]["roundingType"]);Multi-Layered-Bar
    if(chartData[chartId]["chartType"]==='MultiMeasure-Bar' || chartData[chartId]["chartType"]==='Multi-Layered-Bar' || chartData[chartId]["chartType"]==='Line' || chartData[chartId]["chartType"]==='Trend-Analysis' || chartData[chartId]["chartType"]==='Vertical-Bar'|| chartData[chartId]["chartType"]==='MultiMeasure-Line' ||chartData[chartId]["chartType"] ==="Trend-Combo" || chartData[chartId]["chartType"]==='MultiMeasureSmooth-Line' || chartData[chartId]["chartType"]==="Cumulative-Line"){
        var createBarLine={};
        for(var i=0;i<measLen;i++){
            if($("#"+chartId+"createBarLine"+i).val()!==""){
                createBarLine[measures[i]]=$("#"+chartId+"createBarLine"+i).val();
            } else{ 
                createBarLine[measures[i]]= measures[i];
            }   
        }
    }
    chartData[chartId]["createBarLine"]=createBarLine;
    
       
     var measureAvg={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measureAvg"+i).val()!==""){
        measureAvg[measures[i]]=$("#"+chartId+"measureAvg"+i).val();
    } else{ 
        measureAvg[measures[i]]= "";
    }   
    }
    chartData[chartId]["measureAvg"]=measureAvg;
//
//    var SuffixFont = $("#"+chartId+"suffixFontSize").val();
//    chartData[chartId]["suffixFontSize"] = SuffixFont;
var dataLabelType={};
    for(var i =0;i<measLen;i++){
    if($("#"+chartId+"dataLabelType"+i).val()!==""){
        dataLabelType[measures[i]]=$("#"+chartId+"dataLabelType"+i).val();
    } else{ 
        dataLabelType[measures[i]]= "";
    }   
    }
    chartData[chartId]["dataLabelType"]=dataLabelType;
    
    var transposeMeasure = "";
    if($("#"+chartId+"transposeMeasure").prop("checked")){
        transposeMeasure="Y";
    }else{
        transposeMeasure="N";
    }
    chartData[chartId]["transposeMeasure"] = transposeMeasure;
    
    var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
     var defineTargetline={};
    for(var i =0;i<measLen;i++){
    if($("#"+chartId+"defineTargetline"+i).val()!==""){
        defineTargetline[measures[i]]=$("#"+chartId+"defineTargetline"+i).val();
    } else{ 
        defineTargetline[measures[i]]= "";
    }   
    }
    chartData[chartId]["defineTargetline"]=defineTargetline;
    
    var dLabelDisplay={};
    for(var i =0;i<measLen;i++){
    if($("#"+chartId+"dLabelDisplay"+i).val()!==""){
        dLabelDisplay[measures[i]]=$("#"+chartId+"dLabelDisplay"+i).val();
    } else{ 
        dLabelDisplay[measures[i]]= "";
    }   
    }
    chartData[chartId]["dLabelDisplay"]=dLabelDisplay;
    
    var legendFontSize = $("#"+chartId+"legendFontSize").val();
    chartData[chartId]["legendFontSize"] = legendFontSize;
    
    var labelFSize = $("#"+chartId+"labelFSize").val();
    chartData[chartId]["labelFSize"] = labelFSize;
    
    var hideDate = "";
    if($("#"+chartId+"hideDate").prop("checked")){
        hideDate="Y";
    }else{
        hideDate="N";
    }
    chartData[chartId]["hideDate"] = hideDate;
    
    
    var measureNameAlign={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measNameAlign"+i).val()!==""){
        measureNameAlign[measures[i]]=$("#"+chartId+"measNameAlign"+i).val();
    } else{ 
        measureNameAlign[measures[i]]= "";
    }   
    }
    chartData[chartId]["measNameAlign"]=measureNameAlign;
    
    var measureValueAlign={};
    for(var i=0;i<measLen;i++){
    if($("#"+chartId+"measValueAlign"+i).val()!==""){
        measureValueAlign[measures[i]]=$("#"+chartId+"measValueAlign"+i).val();
    } else{ 
        measureValueAlign[measures[i]]= "";
    }   
    }
    chartData[chartId]["measValueAlign"]=measureValueAlign;
    
    var excludeFromDrill = $("#"+chartId+"excludeFromDrill").val();
    chartData[chartId]["excludeFromDrill"] = excludeFromDrill;
    
     var map = {};
    for (var i = 0; i < measures.length; i++) {
        var symbol = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_Select").val();
        var measureVal;
        if(symbol=="<>"){
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valFrom").val()+"__"+$("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valTo").val();
        }else{
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_val").val();
        }
        var measureMap = {};
        if (symbol != "" && measureVal != "") {
            measureMap[symbol] = measureVal;
            map[meassureIds[i]] = measureMap;
        }
    }
    chartData[chartId]["measureFilters"] = map;
    var fontSize1='';
    if(typeof chartData[chartId]["chartType"]!=="undefined" && (chartData[chartId]["chartType"]==='Top-Analysis' || chartData[chartId]["chartType"]==='Combo-Analysis' || chartData[chartId]["chartType"]==='World-Top-Analysis')){
        fontSize1 = $("#"+chartId+"fontSize1").val();
        chartData[chartId]["fontSize1"] = fontSize1;
    }
    if(typeof chartData[chartId]["chartType"]!=="undefined" && chartData[chartId]["chartType"]==='Top-Analysis'){
        var titleFontSize = $("#"+chartId+"titleFontSize").val();
        chartData[chartId]["titleFontSize"] = titleFontSize;
    }
    if(chartType==='Top-Analysis' || chartType==='Combo-Analysis' || chartType==='World-Top-Analysis'){
    var prevPeriodDisplay = $("#"+chartId+"prevPeriodDisplay").val();
    chartData[chartId]["prevPeriodDisplay"] = prevPeriodDisplay; 
    }
    userColor=$("#titleColor").css('background-color');
    if(typeof userColor!='undefined'){
        userColor = hexc(userColor);
        var titleColor = userColor;
        chartData[chartId]["titleColor"] = titleColor;
    }
    
   var customtimeType = $("#"+chartId+"customtimeType").val();
    chartData[chartId]["customtimeType"] = customtimeType;
    
   var customTimeflag = $("#"+chartId+"customTimeflag").val();
    chartData[chartId]["customTimeflag"] = customTimeflag;
    
   var customTimeDate = $("#"+chartId+"customTimeDate").val();
    chartData[chartId]["customTimeDate"] = customTimeDate;
    
    
       parent.$("#dashboardProp1").dialog().dialog('close');
    parent.$("#hideLabel").dialog().dialog('close');
 $("#driver").val("");
    $("#chartData").val(JSON.stringify(chartData));
    
    
    var advanceChartData;
    try{
    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){
    }       
    if(typeof advanceChartData !=="undefined"){
       advanceChartData[window.complexId]["chart5"] = chartData[chartId];
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
    }
    
    if(typeof chartData[chartId]["sorting"] !="undefined" && ! chartData[chartId]["sorting"]==""){
        var ctxPath=parent.document.getElementById("h").value;
   
 var repId = $("#graphsId").val();
 var measId=chartData[chartId]["meassureIds"];
 var aggType=chartData[chartId]["aggregation"];
 var measName=chartData[chartId]["meassures"];
 var viewByIds = parent.$("#viewbyIds").val();
 
 
 var KPIresult=0;   
      if(typeof chartData[chartId]["gtGraph"]!=="undefined" && chartData[chartId]["gtGraph"]==="Y"){         
         parent.$("#chartData").val(JSON.stringify(chartData));
        $.ajax({
            async:false,
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:ctxPath +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+repId+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
                success:function(data){
                    KPIresult=JSON.parse(data);
                    chartData[chartId]["GTValueList"] = KPIresult;
                     parent.$("#chartData").val(JSON.stringify(chartData));
                    // alert(KPIresult)
         $.ajax({
             async:false,
             type:"POST",
            data:
            $('#graphForm').serialize(),
            url:ctxPath +"/reportViewer.do?reportBy=getSortingCharts&sorting="+chartData[chartId]["sorting"]+"&chartID="+chartId+"&isEdit=Y"+"&editId="+chartId+"&action=localfilterGraphs"+"&chartFlag=true"+"&reportId="+parent.$("#graphsId").val(),
            //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
            success:function(data){
                //          $("#dashboardProp").dialog('close');
                //          changeChart(chartId,chartData[chartId]["chartType"],nameVal);
//                $("#chartData").val(JSON.stringify(chartData));
                generateSingleChart(data,chartId);
            }
        });
    }
});   
        
   }else{
   
   var isEdit="Y";
   var chartFlag = "true";
   if(parent.$("#type").val()==="advance"){
   isEdit="N";
   chartFlag="false";
}
         $.ajax({
             async:false,
             type:"POST",
             data:
                 $('#graphForm').serialize(),
             url:ctxPath +"/reportViewer.do?reportBy=getSortingCharts&sorting="+chartData[chartId]["sorting"]+"&chartID="+chartId+"&isEdit="+isEdit+"&editId="+chartId+"&action=localfilterGraphs"+"&chartFlag="+chartFlag+"&reportId="+parent.$("#graphsId").val(),
             //         url: ctxPath+"/reportViewer.do?reportBy=getViewBysf",
             success:function(data){
//  $("#chartData").val(JSON.stringify(chartData));
               if(parent.$("#type").val()==="advance"){
                   generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
               }else{
                   if(chartData[chartId]["chartType"]!=="Multi-View-Tree" || chartData[chartId]["chartType"]!=="Scatter-Analysis"){
                                chartData[chartId]["trendChartData"]=JSON.parse(JSON.parse(data)["data"])[chartId];
//                            }
                            parent.$("#chartData").val(JSON.stringify(chartData));
                            }
                generateSingleChart(data,chartId);
               }
             }
         });  
   }       
}  else {
//  $("#chartData").val(JSON.stringify(chartData));
        changeChart(chartId,chartData[chartId]["chartType"],nameVal);
    }
  
}

function changeChart(chartId,chartType,chartName){
    var ctxPath=parent.$("#ctxpath").val();
    var chartData = JSON.parse($("#chartData").val());
    var measId=chartData[chartId]["meassureIds"];
    if(chartType==="Combo-TopKPI" && measId.length<4){
       return alert("Select more measure");
    }
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var KPIresult=0;
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var repId = $("#graphsId").val();
    var advanceChartData;
    try{

    advanceChartData = JSON.parse(parent.$("#advanceChartData").val());
    }catch(e){
        
    }       
    if(typeof advanceChartData !=="undefined"){
       var advanceDetails = advanceChartData[window.complexId]["chart5"]; 
       advanceDetails["chartType"] = chartType;
       parent.$("#advanceChartData").val(JSON.stringify(advanceChartData));
    }

   if(chartType==="KPI-Table"|| chartType==="Column-Pie" || chartType==="Column-Donut"|| chartType== "DualAxis-Target" || chartType=== "Expression-Table"||chartType==="Stacked-KPI"||chartType==="Emoji-Chart"||chartType==="KPIDash" ||chartType==="Bullet-Horizontal" ||chartType==="Standard-KPI" ||chartType==="Standard-KPI1" ||chartType==="Radial-Chart" ||chartType==="LiquidFilled-KPI" ||chartType==="Dial-Gauge" ||chartType =="Emoji-Chart" ||chartType =="Trend-KPI" &&((typeof chartData[chartId]["GTValue"]=="undefined") ||chartData[chartId]["GTValue"]=="")){
chartData[chartId]["chartType"] = chartType;
var viewByIds = parent.$("#viewbyIds").val();
parent.$("#chartData").val(JSON.stringify(chartData));
        $.ajax({
            async:false,
            type:"POST",
             data:parent.$("#graphForm").serialize(),
            url:ctxPath +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+repId+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
            //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
            success:function(data){
               
               KPIresult=JSON.parse(data);

               chartData[chartId]["GTValueList"] = KPIresult;
                parent.$("#chartData").val(JSON.stringify(chartData));
            }
        });

    }
    if(typeof graphsName[chartType] !=="undefined" && graphsName[chartType]==graphsName[chartData[chartId]["chartType"]]){
     
    divSectionProperties(chartId);  // by mynk sh.
    
        chartTypeFunction(chartId,chartType,chartName,KPIresult);
    }
    else{
        var chartDetails = chartData[chartId];
        if(typeof chartDetails["dimensions"]!="undefined" && chartDetails["dimensions"].length>1 && graphsName[chartType] =="double"){
        
            if(typeof chartType!="undefined" && chartType=="Table"){
                var view = [];
                for(var d in  chartDetails["dimensions"]){
                    view.push(chartDetails["dimensions"][d]);
                }

                var temp = view;
                view=[];
                for(var k=0;k<temp.length;k++){
                    view.push(viewOvName[viewOvIds.indexOf(temp[k])])
                }
                chartDetails["viewBys"]=view;
                chartDetails["viewIds"]=temp;
            }else if(chartType=="Cross-Table"){
                var view = [];
                view.push(chartDetails["dimensions"][0]);
               var columnView = [];
               columnView.push(chartDetails["dimensions"][1]);
                var tempRow = view;
                var tempCol = columnView;
                view=[];
                columnView=[];
                for(var k=0;k<tempRow.length;k++){
                    view.push(viewOvName[viewOvIds.indexOf(tempRow[k])])
                }
                for(var k=0;k<tempCol.length;k++){
                    columnView.push(viewOvName[viewOvIds.indexOf(tempCol[k])])
                }
                chartDetails["viewBys"]=view;
                chartDetails["viewIds"]=tempRow;
                chartDetails["colViewBys"]=columnView;
                chartDetails["colViewIds"]=tempCol;
            }
            else{
                var view = [];
                view.push(chartDetails["dimensions"][0]);
                view.push(chartDetails["dimensions"][1]);
                var temp = view;
                view=[];
                for(var k=0;k<temp.length;k++){
                    view.push(viewOvName[viewOvIds.indexOf(temp[k])])
                }
                chartDetails["viewBys"]=view;
                chartDetails["viewIds"]=temp;
            }
           
        }else {
             var view = [];
             if(typeof chartDetails["dimensions"] !=="undefined" && chartDetails["dimensions"] !==""){
                view.push(chartDetails["dimensions"][0]);
             }else{
                 view.push(chartDetails["viewIds"][0])
             } 
//                view.push(chartDetails["dimensions"][1]);
          
           var temp = view;
                var viewOvName = JSON.parse(parent.$("#viewby").val());
                var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
                view=[];
                for(var k=0;k<temp.length;k++){
                    view.push(viewOvName[viewOvIds.indexOf(temp[k])])
                }
                chartDetails["viewBys"]=view;
                chartDetails["viewIds"]=temp;
        }
     
        chartDetails["chartType"]=chartType;
        chartData[chartId]=chartDetails;
        $("#chartData").val(JSON.stringify(chartData));
        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
            function(data){
       
//                 graphData[chartId]=JSON.parse(data)[chartId];
             graphData=JSON.parse(JSON.parse(data)["data"]);
                chartTypeFunction(chartId,chartType,chartName);
            });
    }
}
function changeTrend(chartId,chartType,chartName){

    var chartData = JSON.parse($("#chartData").val());

    //    if(graphsName[chartType] ==="undefined" || graphsName[chartType]==graphsName[chartData[chartId]["chartType"]]){
    chartTypeFunction(chartId,chartType,chartName);
//          }
//          else{
//              var chartDetails = chartData[chartId];
//              if(typeof chartDetails["dimensions"]!="undefined" && chartDetails["dimensions"].length>1){
//                  var view = [];
//                  view.push(chartDetails["dimensions"][0]);
//                  view.push(chartDetails["dimensions"][1]);
//                  chartDetails["viewBys"]=view;
//              }
//              chartData[chartId]=chartDetails;
//              $("#chartData").val(JSON.stringify(chartData));
//               $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillSingleTrend&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
//                             function(data){
//graphData[chartId]=JSON.parse(data)[chartId];
//
//chartTypeFunction(chartId,chartType,chartName);
//                        });
//          }
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
    if(fromoneview!='null'&& fromoneview=='true'){
        if (typeof divcount !== 'undefined'){
            chartNum=divcount++;

        }
    }
    //     alert(chartNum)
    var chWidth=$("#divchart"+chartNum).width();
    var chHeight=$("#divchart"+chartNum).height();
    if(action1=='OLAPGraphRegion'){
        chWidth=$("#OLAPGraphRegion").width();
        chHeight=$("#OLAPGraphRegion").height();
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
       var completeViewbyIds =  chartData[chartId]["viewIds"];
      var completeViewby = chartData[chartId]["viewBys"];
       var completeMeassureIds =  chartData[chartId]["meassureIds"];
      var completeMeassures = chartData[chartId]["meassures"];
//      var viewbys22 = [];
//      var viewbyIds22 = [];
//      var dimensions = chartData[chartId]["dimensions"];
//      for(var i in dimensions){
//      for(var j in completeViewbyIds){
//
//      if(dimensions[i]==completeViewbyIds[j])  {
//          viewbys22.push(completeViewby[j])
//          viewbyIds22.push(completeViewbyIds[j])
//      }
//      }
//      }



       var  QuickFilterid="viewbys_"+chartId+"_"+completeViewby[0]+"_"+completeViewbyIds[0]
      if(typeof  chartData[chartId]["QuickFilterValue"]!="undefined"){
      var QFKeys = Object.keys(chartData[chartId]["QuickFilterValue"])
//   var quickId = JSON.stringify(chartData[chartId]["QuickFilterId"])
//   alert(chartData[chartId]["QuickFilterId"][0]+":"+"All_"+chartId)
//       if(QFKeys.length>0 ||(typeof chartData[chartId]["QuickFilterId"]!="undefined" && chartData[chartId]["QuickFilterId"]!="" && (chartData[chartId]["QuickFilterId"][0]=="All_"+chartId)) ){
// var viewvName = JSON.parse(parent.$("#viewby").val());
// alert("hhh")
//                var viewvIds = JSON.parse(parent.$("#viewbyIds").val());
//for(var qt in viewvIds){
//
//if(viewvIds[qt].indexOf(QFKeys[0])!=-1 || chartData[chartId]["QuickFilterValue"]=={}){
//    QuickFilterid="viewbys_"+chartId+"_"+viewvName[qt]+"_"+viewvIds[qt]
//}
//}
//
//}
if(typeof chartData[chartId]["QuickFilterId"]!="undefined" && chartData[chartId]["QuickFilterId"]!=""){
   QuickFilterid=chartData[chartId]["QuickFilterId"]
}

    showQuickFilter(QuickFilterid)
      }
    if(fromoneview!='null'&& fromoneview=='true'){
        var repId = parent.$("#graphsId").val();
        var repname = parent.$("#graphName").val();
        parent.$("#chartname").val(chartId);
        var hgt1 ;
        var wid ;
    //if(regidch=='olap'){
    //var dashletid="OLAPGraphRegion";
    //regidch=dashletid;
    //  hgt1 = document.getElementById(regidch).offsetHeight;
    //                        wid = document.getElementById(regidch).offsetWidth-80;
    //}else{
    ////                        hgt1 = document.getElementById(regidch).offsetHeight;
    ////                        wid = document.getElementById(regidch).offsetWidth-80;
    //
    //}
    //alert(wid)
    //                        hgt1=hgt1-50;
    //                 buildoneviewPie(regidch, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],wid,hgt1,regidch,oneviewid1,repname,repId)
    }

    if(typeof chartData[chartId]["chartType"]!="undefined" && (chartType!="Radial-Chart"  && chartType!="Bullet-Horizontal"  && chartType!="Standard-KPI" && chartType!="KPIDash" && chartType!="Emoji-Chart" && chartType!="Dial-Gauge")){
        if(fromoneview!='null'&& fromoneview=='true'){
            if(typeof(chartData[chartId]["Name"])!="undefined"  ){
                html += "<span id='dbChartName"+chartId+"' style ='display:'><a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+chartNum+"&REPORTID="+repId+"')\"><tspan  style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[chartId]["Name"]+"</tspan></a></span>"
            }
            else{
                html += "<span id='dbChartName"+chartId+"' style ='display:'><a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+chartNum+"&REPORTID="+repId+"')\"><tspan  style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartId+"</tspan></a></span>"
            }
 
        }else{
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
                 var timeDate='';
            if (typeof chartData[chartId]["customTimeDate"] !== 'undefined' && chartData[chartId]["customTimeDate"] !== '') {
             timeDate = chartData[chartId]["customTimeDate"];
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
if(typeof chartData[chartId]["customtimeType"]!="undefined" && chartData[chartId]["customtimeType"]!="" && chartData[chartId]["customtimeType"]==="Yes"){ 
       html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> "+timeDate+" </tspan></span>"
}else{
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
if(typeof chartData[chartId]["customtimeType"]!="undefined" && chartData[chartId]["customtimeType"]!="" && chartData[chartId]["customtimeType"]==="Yes"){ 
       html += "<span id='dbChartNameDate"+chartId+"' style =''><tspan  id='spanChartNameDate"+chartId+"' style=' font-size:"+(fontValue*.7)+"px;font-style: "+fontType+";font-weight:"+fontType+";color:#828282;'> "+timeDate+" </tspan></span>"
}else{
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
       {}
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
        }
    } else{
        if(typeof(chartData[chartId]["KPIName"])!="undefined"  ){
            html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  style=' font-size:12px;color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span>"
        }else{
            html += "<span id='dbChartName"+chartId+"' style ='display:'><tspan  style=' font-size:12px;color:#A1A1A1;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'></tspan></span>"
        }
    }
    var newchartid;

    if(fromoneview!='null'&& fromoneview=='true'){
        if (typeof divcount !== 'undefined'){
            //chartNum=divcount--;
            newchartid="chart"+chartNum;
            if(action1=='OLAPGraphRegion'){
                newchartid="olapchart"+chartNum;
            }
        }else{
            newchartid=chartId;
            if(action1=='OLAPGraphRegion'){
                newchartid="olap"+chartId;
            }
            $("#renameTitle"+chartId).html(html);
        }
    }
    else{
        newchartid=chartId;
        $("#renameTitle"+chartId).html(html);
        $("#"+chartId).html("");
    }

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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
                success:function(kpidata){

//        buildTableBar(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            buildDial(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
                }})
       
            }else if(chartType=="Dial-Gauge1"){
       var measId=chartData[chartId]["meassureIds"];
    var aggType=chartData[chartId]["aggregation"];
    var measName=chartData[chartId]["meassures"];
    var viewByIds = parent.$("#viewbyIds").val();
 $.ajax({
            type:"POST",
                data:parent.$("#graphForm").serialize(),
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
                success:function(kpidata){

//        buildTableBar(newchartid,data, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata));
            buildDialNew(newchartid, data, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,JSON.parse(kpidata)[0]);
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
            url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
                url:parent.$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+parent.$("#graphsId").val()+"&chartId="+chartId+"&measId="+measId+"&aggType="+encodeURIComponent(aggType)+"&measName="+JSON.stringify(encodeURIComponent(measName))+"&viewbyIds="+viewByIds,
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
//sandeep for drill across in oneview
function oneviewdrillWithinchart(idArr,div,repname,repid,chartData,regid,oneviewid){
    var id = idArr.split(",");
    var drills = {};
    var drillValues = [];
    divcount="undefined";
    action1="drillacross"
    //grid=parent.gridster5;
    //            grid.remove_all_widgets();
    //            parent.gridster5.remove_all_widgets();
    //        grid=parent.gridster5;
    $.post(parent.ctxpath+'/reportViewer.do?reportBy=gettingJsonData&oneviewid='+oneviewid,
        function(data){
            var jsonVar=eval('('+data+')');
            var  regionids=jsonVar.regionids
            var  reportids=jsonVar.reportids
            var  repnames=jsonVar.repnames
            var  chartnames=jsonVar.chartnames;
            var  busrolename=jsonVar.busrolename;
            //                 var  idArr=jsonVar.idArr;
            var  drillviewby=jsonVar.drillviewby;
            for(var i=0;i<regionids.length;i++){
                parent.$("#graphsId").val(reportids[i]);
                parent.$("#graphName").val(repnames[i]);
                parent.$("#busrolename").val(busrolename[i]);
                parent.$("#chartname").val(chartnames[i]);
                parent.$("#oneViewId").val(oneviewid);
                if(regid==regionids[i]){
                    $.ajax({
                        async:false,
                        type:"POST",
                        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

                        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&&fromoneview=true&action=open&fromoneview=true&oneviewid="+oneviewid+"&regid="+regionids[i],

                        success: function(data){
                            //                $("#loading").hide();
                            if(data=="false"){

                            }
                            else{
                                var jsondata = JSON.parse(data)["data"];
                                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                                var chartData = JSON.parse(parent.$("#chartData").val());
                                if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" || chartData[div]["chartType"]=="Grouped-Line" || chartData[div]["chartType"]=="Scatter-Bubble" || chartData[div]["chartType"]=="Split-Bubble" || chartData[div]["chartType"]=="Grouped-Table" || chartData[div]["chartType"]=="Grouped-Map"|| chartData[div]["chartType"]=== "GroupedStackedH-Bar"|| chartData[div]["chartType"] == "GroupedStacked-Bar"){
                                    var drillFirst=[];
                                    var drillNext=[];
                                    drillFirst.push(idArr.split(":")[0]);
                                    drillNext.push(idArr.split(":")[1]);

                                    drills[chartData[div]["viewIds"][0]] = drillFirst;
                                    if(typeof chartData[div]["viewIds"][1] !="undefined")
                                        drills[chartData[div]["viewIds"][1]] = drillNext;
                                }else{
                                    drillValues.push(idArr.split(":")[0]);
                                    drills[chartData[div]["viewIds"][0]] = drillValues;
                                }

                                $("#driver").val(div);
                                parent.$("#drills").val(JSON.stringify(drills));

                            }
                        }
                    });

                }
            }
            $("#newloading").show();
            for(var i=0;i<regionids.length;i++){
                parent.$("#graphsId").val(reportids[i]);
                parent.$("#graphName").val(repnames[i]);
                parent.$("#busrolename").val(busrolename[i]);
                parent.$("#chartname").val(chartnames[i]);
                parent.$("#oneViewId").val(oneviewid);
                var idArr1=idArr[i];
                //if(regid!=regionids[i]){
                $.ajax({
                    async:false,
                    type:"POST",
                    data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),

                    url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&&fromoneview=true&action=open&fromoneview=true&oneviewid="+oneviewid+"&regid="+regionids[i],

                    success: function(data){
                        //                $("#loading").hide();
                        if(data=="false"){

                        }
                        else{
                            var jsondata = JSON.parse(data)["data"];
                            parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                            var meta = JSON.parse(JSON.parse(data)["meta"]);
                            parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                            parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                            parent.$("#measure").val(JSON.stringify(meta["measures"]));
                            parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                            parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                            parent.$("#drilltype").val((meta["drilltype"]));
                            //                parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));
                            var filterMap1 = {};
                            filterMap1=JSON.stringify(meta["filterMap"]);
                        //parent.$("#filterMap").val(JSON.stringify(meta["filterMap"]));

                        }

                        //                             var  Dashlets="Dashlets-"+regionids[i];
                        //                              var wid ;
                        //                        var hgt1 ;
                        var chartData = JSON.parse(parent.$("#chartData").val());
                        //  chartData[chartnames[i]]["viewBys"][0]=drillviewby[i];
                        parent.$("#chartData").val(JSON.stringify(chartData));
                        $.ajax({
                            async:false,
                            type:"POST",
                            data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+ reportids[i]+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+'&chartname='+chartnames[i],

                            url: parent.ctxpath+"/reportViewer.do?reportBy=drillCharts&action=drillacross&fromoneview=true&oneviewID="+oneviewid+"&regid="+regionids[i]+"&drillregid="+regid,

                            success: function(data){
                                graphData=JSON.parse(data);
                                divcount=regionids[i];
                                //    divcount;
                                fromoneview='true';
                                parent.$("#chartname").val(chartnames[i])
                                var chartid="chart"+regionids[i];
                                //generateChartloop(data,"undefined");
                                generateSingleChart(data,chartid);
                                viewid=[];
                                filternames=[];
                            //generateChart(data);
                            }
                        });

                    }
                });
            //                 }
            //                         }
            }
            $("#newloading").hide();
        });
//                     $("#imgId").hide();
}

function drillWithinchart1(idArr,div,flag1,drillId,measureId){
//    alert(drillId)
  window.drillID = drillId;
    $("#loading").show();
      //Added by Ram
     var data=idArr.split(":")[0]
     data=data.toUpperCase();
      if(data in parent.lookupdataMap){
      var lookupValue=idArr.split(":")[1]
      var lookupData=parent.lookupdataMap[data]
      lookupData=lookupData+':'+lookupValue;
        idArr = lookupData;
        }
      //Ended by Ram
    $("#drillFormat").val("");
    breadCrump = div;
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var measName = JSON.parse(parent.$("#measure").val());
    var measIds = JSON.parse(parent.$("#measureIds").val());
    var chartData = JSON.parse($("#chartData").val());
    var repId = parent.document.getElementById("REPORTID").value;
     var drilltype = chartData[div]["drillType"];
var mName=[];
var vName=[];
     if(drilltype!="nodrill"){
    $("#loading").show();
  }
    if($("#type").val()=="graph" || $("#type").val()=="trend"){
        //             if($("#type").val() == "trend"){
        //             $("#drilltype").val("within");
        //             }
       
        if(typeof drilltype !="undefined" && drilltype==="within"){
            var id = idArr.split(",");
            var yaxisFilter = chartData[div]["yaxisrange"];
           if(typeof yaxisFilter!="undefined" && yaxisFilter!=""){
           yaxisFilter["YaxisRangeType"] = "Default";
           }
            var y1axisFilter = chartData[div]["y1axisrange"];  // add by mayank sharma
           if(typeof y1axisFilter!="undefined" && y1axisFilter!=""){
           y1axisFilter["Y1axisRangeType"] = "Default";
           }
           
            
            var drills = {};
            // previous drills
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
                    drills=JSON.parse($("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];

            drillValues.push(idArr.split(":"))
            if(chartData[div]["chartType"]=="Grouped-Bar" 
                || chartData[div]["chartType"]=="Multi-View-Tree"  
                || chartData[div]["chartType"]=="GroupedHorizontal-Bar" 
                || chartData[div]["chartType"]=="Scatter-Bubble" 
                || chartData[div]["chartType"]=="Split-Bubble" 
                || chartData[div]["chartType"]=="Grouped-Line" 
                || chartData[div]["chartType"]=="Grouped-Table" 
                || chartData[div]["chartType"]=="Grouped-Map" 
                || chartData[div]["chartType"]== "GroupedStackedH-Bar"
                || chartData[div]["chartType"] == "GroupedStacked-Bar"
                || chartData[div]["chartType"] == "Multi-View-Bubble"){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0])
                drillNext.push(idArr.split(":")[1])

                drills[chartData[div]["viewIds"][0]] = drillFirst;
                if(typeof chartData[div]["viewIds"][1] !="undefined")
                    drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                var drillMap = [];
                drillMap.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillMap;
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            }
            $("#driver").val(div);
            var flag = false;

            try{
                for(var i=0;i<chartData[div]["dimensions"].length;i++){
                    if(chartData[div]["viewIds"][0]===chartData[div]["dimensions"][i]){
                        var view = [];
                        var viewName = [];
                        if(typeof chartData[div]["dimensions"][i+1] !="undefined"){
                            view.push(chartData[div]["dimensions"][i+1]);
                            flag = true;
                            if((chartData[div]["chartType"]=="Grouped-Bar" 
                                || chartData[div]["chartType"]=="Multi-View-Tree" 
                                || chartData[div]["chartType"]=="GroupedHorizontal-Bar" 
                                || chartData[div]["chartType"]=="Scatter-Bubble" 
                                || chartData[div]["chartType"]=="Split-Bubble" || 
                                chartData[div]["chartType"]=="Grouped-Line" ||
                                chartData[div]["chartType"] == "Multi-View-Bubble" || chartData[div]["chartType"]=="Grouped-Map" || chartData[div]["chartType"]=="Grouped-Table"  || chartData[div]["chartType"]=="GroupedStackedH-Bar"|| chartData[div]["chartType"]=="GroupedStacked-Bar") && typeof chartData[div]["dimensions"][i+2] !="undefined"){
                                view=[];
                                if(typeof chartData[div]["dimensions"][i+2]!="undefined"){
                                    view.push(chartData[div]["dimensions"][i+2]);
                                }else{
                                    alert("No more levels of data to drill down");
                                    return "";
                                }

                                chartData[div]["viewIds"]= view;
                                for(var m=0;m<view.length;m++){
                                    viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                }
                                chartData[div]["viewBys"]= viewName;
                            }else{
                                for(var m=0;m<view.length;m++){
                                    viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                    chartData[div]["viewIds"]= view;
                                    chartData[div]["viewBys"]= viewName;
                                }

                            }
                        }
                        else{
                            $("#loading").hide();
                            alert("No more levels of data to drill down");
                            return "";
                        }
                        //                 chartData[div]["viewBys"]= view;
                        //                 drills[view[0]] = idArr.split(":");
                        break;
                    }
                }
            }
            catch(e){
                $("#loading").hide();
                //            if(flag){
                alert("No more levels of data to drill down");
            //            }
            }
         
            if(flag){
                $("#chartData").val(JSON.stringify(chartData));
                $("#drills").val(JSON.stringify(drills));
                $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
                    function(data){
                        //                        generateChart(data,div);
                        generateSingleChart(data,div);
                    });
            }
            /*Added by Ashutosh*/
        }else if(typeof drilltype !="undefined" && drilltype==="action"){
            window["actionDrill_"+div.split("t")[1]]=window["actionDrill_"+div.split("t")[1]]+1;
            var actionDrillMap=chartData[div]["actionDrillMap"];
            var drills = {};
            // previous drills
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
//                    alert(JSON.parse($("#drills").val()))
                    drills=JSON.parse($("#drills").val());
                }catch(e){
        }
            }
            //            var drillValues = [];
            //            drillValues.push(idArr.split(":"))
            if(Object.keys(actionDrillMap).length >= window["actionDrill_"+div.split("t")[1]]){
                actionDrillMap=actionDrillMap['drill'+window["actionDrill_"+div.split("t")[1]]];
                var actionviewbys = actionDrillMap['viewBys'];
                var actionmeasures = actionDrillMap['measures'];
                var actionChartType = actionDrillMap['chartType'];
                
                var drillMap = [];
                drillMap.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillMap;
            }else{
                $("#loading").hide();
                alert("No more levels of data to drill down");
            }
            for(k in actionmeasures){
                mName.push(measName[measIds.indexOf(actionmeasures[k])]);
            }
            for(k in actionviewbys){
                vName.push(viewOvName[viewOvIds.indexOf(actionviewbys[k])]);
            }
            chartData[div]["viewBys"]=vName;
            chartData[div]["viewIds"]=actionviewbys;
            chartData[div]["meassures"]=mName;
            chartData[div]["meassureIds"]=actionmeasures;
            chartData[div]["chartType"]=actionChartType[0];
            
            $("#chartData").val(JSON.stringify(chartData));
            $("#drills").val(JSON.stringify(drills));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
                function(data){
                    //                       alert(JSON.stringify(data))
                    //                        generateChart(data,div);
                    generateSingleChart(data,div);
                });
            
        }
        else if(drilltype==="single" || drilltype==="" || typeof drilltype==="undefined" ){

                if (typeof chartData[div]["hideBorder"]!=="undefined" && chartData[div]["hideBorder"]==="Y"){
    $("#div"+div).css({
       'border':'none'
            });
    }else{
newProp=div;
        $("#div"+div).css({
'box-shadow': '2px 2px 5px rgb(145, 255, 0)'
        });
    }

            var id = idArr.split(",");
            var drills = {};
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());

            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="Multi-View-Tree" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" || chartData[div]["chartType"]=="Scatter-Bubble" || chartData[div]["chartType"]=="Split-Bubble"
                || chartData[div]["chartType"]=="Grouped-Line" || chartData[div]["chartType"]=="Grouped-Table" || chartData[div]["chartType"]=="Grouped-Map"
                || chartData[div]["chartType"]=="GroupedStackedH-Bar" || chartData[div]["chartType"] == "GroupedStacked-Bar"){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0]);
                drillNext.push(idArr.split(":")[1]);

                drills[chartData[div]["viewIds"][0]] = drillFirst;
                if(typeof chartData[div]["viewIds"][1] !="undefined")
                    drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                drillValues.push(idArr.split(":")[0]);
                
            if(getComboChartFlag(chartData[div]["chartType"])){
                
            drills[idArr.split(":")[1]] = drillValues;
            }else{
//             if(1==2){
                
                if(typeof flag1!='undefined' && flag1==='comboDrill'){
                    if(chartData[div]["chartType"]==='Trend-Analysis-Chart'){
                    var measures=chartData[div]["meassures"];
                    var measureIds=chartData[div]["meassureIds"];
                    var aggregation=chartData[div]["aggregation"];
                    var measureIndex=measureIds.indexOf(measureId);
                    var newMeasures=[];
                    var newMeasureIds=[];
                    var newAggregation=[];
                    newMeasures.push(measures[measureIndex]);
                    newMeasureIds.push(measureId);
                    newAggregation.push(aggregation[measureIndex]);
                    for(var k in measureIds){
                        if(k!=measureIndex){
                            newMeasures.push(measures[k]);
                            newMeasureIds.push(measureIds[k]);
                            newAggregation.push(aggregation[k]);
                        }
                    }
                    chartData[div]["meassures"]=newMeasures;
                    chartData[div]["meassureIds"]=newMeasureIds;
                    chartData[div]["aggregation"]=newAggregation;
                    }
                    drills[chartData[div]["dimensions"][0]] = drillValues;
                }
                else{
                drills[chartData[div]["viewIds"][0]] = drillValues;
            }
//            }
                
            }
            }

            $("#driver").val(div);
              
               var drillKeys = Object.keys(drills);
               
              var quickViewname = viewOvName[viewOvIds.indexOf(drillKeys[0])];
             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time"); 
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time"); 
             }else {
                parent.$("#drillFormat").val("none");  
             }
            $("#drills").val(JSON.stringify(drills));
            if(typeof flag1!='undefined' && flag1==='comboDrill'){
                if(chartData[div]["chartType"]==='Trend-Analysis-Chart'){
                    measures=chartData[div]["meassures"];
                    measureIds=chartData[div]["meassureIds"];
                    aggregation=chartData[div]["aggregation"];
                    measureIndex=measureIds.indexOf(measureId);
                    newMeasures=[];
                    newMeasureIds=[];
                    newAggregation=[];
                    newMeasures.push(measures[measureIndex]);
                    newMeasureIds.push(measureId);
                    newAggregation.push(aggregation[measureIndex]);
                    for(var k in measureIds){
                        if(k!=measureIndex){
                            newMeasures.push(measures[k]);
                            newMeasureIds.push(measureIds[k]);
                            newAggregation.push(aggregation[k]);
                        }
                    }
                    chartData[div]["meassures"]=newMeasures;
                    chartData[div]["meassureIds"]=newMeasureIds;
                    chartData[div]["aggregation"]=newAggregation;
                }
                var dimIds=chartData[div]["dimensions"];
                var dimId='';
                if(dimIds[0].toLowerCase()==='time'){
                    try{
                        dimId=dimIds[1];
                    }
                    catch(e){
                        dimId=dimIds[0];
                    }
                }
                else{
                    dimId=dimIds[0];
                }
                var allViewBys  = JSON.parse(parent.$("#viewby").val());
                var allViewIds = JSON.parse(parent.$("#viewbyIds").val());
                chartData[div]["viewBys"] = [allViewBys[allViewIds.indexOf(dimId)]];
                chartData[div]["viewIds"] = [dimId];
//                chartData[div]["dimensions"] = ["13917","TIME","191648","13912"];
            }
            $("#chartData").val(JSON.stringify(chartData));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(resultset){
//                         var meta = JSON.parse(JSON.parse(resultset)["meta"]);

//                            $("#chartData").val(JSON.stringify(meta["chartData"]));
//              //    var chartMeta =  JSON.parse(resultset)["meta"];
                var data = JSON.parse(resultset)["data"];
               var chartData = JSON.parse($("#chartData").val());
                    var chartFlag = "false";
                    var GTdiv = [];
                    
                    for(var t in chartData){
                 
                 if(chartData[t]["chartType"]=="KPIDash"|| chartData[t]["chartType"]=="Bullet-Horizontal" || chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="Table" ||chartData[t]["chartType"]=="Standard-KPI" || chartData[t]["chartType"]=="Standard-KPI1"  ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                            chartFlag = "true";
                          
//                 GTdiv.push(t)
//alert(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                        }
                    }
if(chartData[div]["chartType"]==="Combo-Horizontal"||chartData[div]["chartType"]==="Combo-Aus-City"||chartData[div]["chartType"]==="Combo-Aus-State" || 
chartData[div]["chartType"]==="Combo-USCity-Map" || chartData[div]["chartType"]==="Combo-US-Map"  || chartData[div]["chartType"]==="Combo-IndiaCity" || chartData[div]["chartType"]==="Combo-India-Map"){
  generateSingleChart(data,div);
}else{                     
                    generateChart(data,'',idArr);
  }
                });
        }
        else if(drilltype==="multi"){
            var id = idArr.split(",");
            var drills = {};
            var drillValues = [];
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                drills = JSON.parse($("#drills").val());
                if(typeof drills[chartData[div]["viewIds"][0]]!="undefined"){
                    drillValues = drills[chartData[div]["viewIds"][0]];
                }
            }
            var chartData = JSON.parse($("#chartData").val());
	    	    if(typeof drillValues!="undefined" && drillValues!="" ){
		drillValues = [];
		}
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            $("#driver").val(div);
            var driverList = [];
            if(typeof $("#driverList").val()!="undefined" && $("#driverList").val()!=""){
                driverList = JSON.parse($("#driverList").val());
            }
            if(driverList.indexOf(div)==-1){
                driverList.push(div);
            }
            $("#driverList").val(JSON.stringify(driverList));
            $("#drills").val(JSON.stringify(drills));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(data){
                     var data = JSON.parse(data)["data"];
                    generateChart(data);
                });
        }
        else if(drilltype==='pageDrill'){
            pageDrillValue=idArr;
            window.pageDrillViewById=chartData[div]["viewIds"][0];
            window.pageDrillViewById1 = chartData[div]["viewIds"][1];
            window.pageDrillViewByName = chartData[div]["viewBys"][0];
            changePage(chartData[div]["drillPage"]);
    }
      else if(drilltype==="nodrill"){
        
    }
    }





    else if($("#type").val()=="advance"){
        //        var drilltype = chartData[div]["drillType"];//$("#drilltype").val();
        //          if(typeof drilltype !="undefined" && drilltype===""){
        var id = idArr.split(":");
        var drills = {};
        var drillValues = [];
        if(div=="chart1"){
            $("#driverList").val("");
        }

        var driverList = [];
         if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
                    drills=JSON.parse($("#drills").val());
                }catch(e){
                }
            }
           
        if(typeof $("#driverList").val()!="undefined" && $("#driverList").val()!=""){
            driverList = JSON.parse($("#driverList").val());
        }
        driverList.push(div);
        var chartType = $("#chartType").val();
        if(chartType=="multi-kpi"){
            $("#driverList").val(JSON.stringify(driverList));
            var chartData = JSON.parse($("#chartData").val());
            //        chartData["chart2"]=chartData[div];
            var currId = Object.keys(chartData).length;
            var dimensions = chartData[div]["dimensions"];
            var viewOvName = JSON.parse(parent.$("#viewby").val());
            var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
            var view = [];
            var chartId = "chart"+(parseFloat(currId)+1);
            var chartDetails = {};
            var viewName = [];
            viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId])])
            view.push(dimensions[currId]);
            chartDetails["viewIds"]=view;
            chartDetails["viewBys"]=viewName;
            chartDetails["dimensions"]=dimensions;
            chartDetails["chartType"] = "Pie";
            chartDetails["viewByLevel"] = "single";
            chartDetails["meassures"] = chartData[div]["meassures"];
            chartDetails["aggregation"] = chartData[div]["aggregation"];
            chartDetails["meassureIds"] = chartData[div]["meassureIds"];
            chartDetails["size"] = "S";
            chartData[chartId]=chartDetails;
            $("#chartData").val(JSON.stringify(chartData));

            if($("#drills").val()!="undefined" && $("#drills").val()!=""){
                drills = JSON.parse($("#drills").val());
            }
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
        }

        else if(chartType=="Tree-Map" || chartType=="Multi-View-Bubble"){
            var chartData = JSON.parse($("#chartData").val());
            //        chartData["chart2"]=chartData[div];
            div="chart1";
            var dimensions = chartData["chart1"]["dimensions"];
            var viewOvName = JSON.parse(parent.$("#viewby").val());
            var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
            var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][1]));

            var drillValues1 = [];
            drillValues.push(idArr.split(":")[0]);
            drillValues1.push(idArr.split(":")[1]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            drills[chartData[div]["viewIds"][1]] = drillValues1;

            var view = [];
            var chartId = "chart1";
            var chartDetails = {};
            var viewName = [];
            if(typeof dimensions[currId+1]!="undefined" && dimensions[currId+2] != "undefined"){
                viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])])
                view.push(dimensions[currId+1]);
                viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+2])])
                view.push(dimensions[currId+2]);
            }else{
                $("#loading").hide();
                alert("Please Select More Parameters");
                return "";
            }
            chartData[chartId]["viewIds"]=view;
            chartData[chartId]["viewBys"]=viewName;

            $("#chartData").val(JSON.stringify(chartData));
            $("#driver").val(div);
        }

        else if(chartType=="Wordcloud" || chartType=="Tree-Map-Single" || chartType=="Bar-Dashboard" || chartType=="KPI-Bar" || chartType === "Advance-Pie" || chartType == "Advance-Horizontal" || chartType == "Trend-Dashboard"){
            var chartData = JSON.parse($("#chartData").val());
            div="chart1";
            var dimensions = chartData["chart1"]["dimensions"];
            var viewOvName = JSON.parse(parent.$("#viewby").val());
            var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
            var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][0]));

            var drillValues1 = [];
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            var view = [];
            var chartId = "chart1";
            var chartDetails = {};
            var viewName = [];
            if(typeof dimensions[currId+1]!="undefined"){
                viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])]);
                view.push(dimensions[currId+1]);
            }else{
                $("#loading").hide();
                alert("Please Select More Parameters");
                    return "";
            }
            chartData[chartId]["viewIds"]=view;
            chartData[chartId]["viewBys"]=viewName;


            $("#chartData").val(JSON.stringify(chartData));
            $("#driver").val(div);
        }else if(chartType=="Bubble-Dashboard" || chartType=="Scatter-Dashboard" || chartType == "combo-chart"){
            //      div="chart1";

            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;

            $("#driver").val(div);
        }else if(chartType=="Pie-Dashboard"){
            var chartData = JSON.parse($("#chartData").val());
//                var   drills=($("#drills").val());
   
            breadcrumpid.push(idArr);
            var charts = Object.keys(chartData);
            for(var ch in charts){
            
      
                div="chart"+ (parseInt(ch)+1);
                var dimensions = chartData[div]["dimensions"];
                var viewOvName = JSON.parse(parent.$("#viewby").val());
                var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
                var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][0]));

                var drillValues1 = [];
                drillValues.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillValues;
                var view = [];
                var chartId = "chart"+ (parseInt(ch)+1);
                var chartDetails = {};
                var viewName = [];
      
                if(typeof dimensions[currId+1]!=="undefined"){
                    viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])]);
                    view.push(dimensions[currId+1]);
                }else{
                    $("#loading").hide();
                    alert("Please Select More Parameters");
                    return "";
                }
                chartData[chartId]["viewIds"]=view;
                chartData[chartId]["viewBys"]=viewName;

            }
        
            $("#chartData").val(JSON.stringify(chartData));
            $("#driver").val(div);
        }
        else if(chartType=="world-map"){
         // alert(div)
//          div="chart2";
           var dimensions = chartData[div]["dimensions"];
                var viewOvName = JSON.parse(parent.$("#viewby").val());
                var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
                var currId = parseInt(dimensions.indexOf(chartData[div]["viewIds"][0]));
                var drillValues1 = [];
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
             var view = [];
                var chartId = div;
                var chartDetails = {};
                var viewName = [];
                 if(typeof dimensions[currId+1]!=="undefined"){
                    viewName.push(viewOvName[viewOvIds.indexOf(dimensions[currId+1])]);
                    view.push(dimensions[currId+1]);
                }else{
                    $("#loading").hide();
                    alert("Please Select More Parameters");
                    return "";
                }
//            $("#driver").val(div);
        }
        else{
            div="chart1";
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            $("#driver").val(div);
        }
        $("#driver").val(div);
        $("#drills").val(JSON.stringify(drills));
        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){

                  if(chartType=="world-map"){
                      drilleData(JSON.parse(data),drillValues)
                  }else{

                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
                  }
                $("#driver").val("");
                $("#loading").hide();
            });

    }








    else{
        var drilltype = chartData[div]["drillType"];
        if(typeof drilltype==="undefined"  || drilltype==="within" || drilltype===""){
            var id = idArr.split(",");
            var drills = {};
            // previous drills
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
                    drills=JSON.parse($("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());
            drillValues.push(idArr.split(":"))
            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" || chartData[div]["chartType"]=="Scatter-Bubble" || chartData[div]["chartType"]=="Split-Bubble" || chartData[div]["chartType"]=="Grouped-Line" || chartData[div]["chartType"]=="Grouped-Table" || chartData[div]["chartType"]=="Grouped-Map" || chartData[div]["chartType"]=="GroupedStackedH-Bar" || chartData[div]["chartType"]=="GroupedStacked-Bar"){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0])
                drillNext.push(idArr.split(":")[1])
                drills[chartData[div]["viewIds"][0]] = drillFirst;
                drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                var drillMap = [];
                drillMap.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillMap;
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            }
            $("#driver").val(div);
            var flag = false;

            try{
                for(var i=0;i<chartData[div]["dimensions"].length;i++){
                    if(chartData[div]["viewIds"][0]===chartData[div]["dimensions"][i]){
                        var view = [];
                        var viewName = [];
                        if(typeof chartData[div]["dimensions"][i+1] !="undefined"){
                            view.push(chartData[div]["dimensions"][i+1]);
                            flag = true;
                            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" || chartData[div]["chartType"]=="Scatter-Bubble" || chartData[div]["chartType"]=="Split-Bubble" || chartData[div]["chartType"]=="GroupedStackedH-Bar" || chartData[div]["chartType"]=="GroupedStacked-Bar"){
                                view=[];
                                if(typeof chartData[div]["dimensions"][i+2]!="undefined"){
                                    view.push(chartData[div]["dimensions"][i+2]);
                                }else{
                                    alert("No more levels of data to drill down");
                                    return "";
                                }

                                chartData[div]["viewIds"]= view;
                            }else{
                                for(var m=0;m<view.length;m++){
                                    viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                }
                                chartData[div]["viewIds"]= view;
                                chartData[div]["viewBys"]= viewName;
                            }
                            $("#chartData").val(JSON.stringify(chartData));
                        }

                        else{
                            alert("No more levels of data to drill down");
                            return "";
                        }
                        //                 chartData[div]["viewBys"]= view;
                        //                 drills[view[0]] = idArr.split(":");
                        break;
                    }
                }
            }
            catch(e){
                //            if(flag){
                alert("No more levels of data to drill down");
            //            }
            }
            $("#drilltype").val("within");
            if(flag){
                $("#chartData").val(JSON.stringify(chartData));
                $("#drills").val(JSON.stringify(drills));
                $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                    function(data){
                        generateSingleTrend(data,div);
                    });
            }
        }
        else if(drilltype==="single" ){
            var id = idArr.split(",");
            var drills = {};
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());
            drillValues.push(idArr.split(":"));
            drills[chartData[div]["viewIds"][0]] = idArr.split(":");
            $("#driver").val(div);
            $("#drills").val(JSON.stringify(drills));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(data){
                    generateTrend(data);
                });
        }
        else if(drilltype==="multi"){

        }

    }


}
var idArradhoc;
function viewAdhoctypes(idArr){
    parent.$("#idArradhoc").val(idArr);
    //alert(parent.$("#idArradhoc").val())
    $("#DrillDiv").toggle(500);

}

function selectDrill(drilltype){

    $("#drillType").val(drilltype);

}
function adhocolapdrill(idArr,div,view1,viewName1,regid,oneviewid,repname,repId,chartname1,drillviewby){
    var id1=div
    div=chartname1;
    chartname=div;
    var view = [];
    var viewName = [];
    view.push(viewName1);
    viewName.push(view1);
    oneviewid1=oneviewid;
    fromoneview='true';
    if(drillviewby=='olap'){
        regidch='olap';
    }else{
        regidch="Dashlets-"+regid;
    }
    parent.$("#chartname").val(chartname1);
    var prevviewby;
    var prevviewbyid;
    breadCrump = div;
    parent.$("#type").val("graph");
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    if(parent.$("#type").val()=="graph"){
        var drilltype = $("#drilltype").val();
        drilltype = "within";
        if(typeof drilltype !="undefined" && drilltype==="within"){
            var id = idArr.split(",");
            var drills = {};
            // previous drills
            if(typeof parent.$("#drills").val()!="undefined" && parent.$("#drills").val()!=""){
                try{
                //                drills=JSON.parse(parent.$("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];
            var chartData = JSON.parse(parent.$("#chartData").val());
            drillValues.push(idArr.split(":"))
            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" ){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0])
                drillNext.push(idArr.split(":")[1])
                drills[chartData[div]["viewIds"][0]] = drillFirst;
                drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                var drillMap = [];
                drillMap.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillMap;
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            }
            chartData[div]["viewIds"]= view;
            chartData[div]["viewBys"]= viewName;
            parent.$("#chartData").val(JSON.stringify(chartData));
            parent.$("#drills").val(JSON.stringify(drills));
            $.ajax({
                async:false,
                type:"POST",
                data: parent.$("#oneviewgraphForm").serialize()+"&oneviewID="+oneviewid+"&regid="+regid+"&reportName="+repname+"&reportId="+repId+"&idArr="+idArr+"&viewbydrill="+prevviewby+"&chartname="+chartname1+"&id1="+id1,

                url: parent.ctxpath+"/reportViewer.do?reportBy=drilloneviewCharts&drilltype=within&fromoneview=true",
                success: function(data){
                    //          $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                    //                             function(data){
                    var jsonVar=eval('('+data+')');

                    var  reportData=jsonVar.reportData;
                    var json = JSON.parse(reportData);
                    var jsonData = json[div];
                    if(json[div]==''|| json[div]=='null'){


                    }else{
                        if(id1!='null' && id1=='OLAPGraphRegion' ){

                        }else{
                            var  result1=jsonVar.result1;
                            parent.$("#"+regid).html(result1);
                        }
                        //                                 var id="Dashlets-"+0

                        var json1 = JSON.parse(parent.$("#measure").val());
                        parent.$("#"+id1).html("");
                            var jsondata = JSON.parse(reportData)["data"];
                        //                            buildoneviewPie(id1, jsonData,view,json1,wid,hgt,regid,oneviewid,repname,repId);
                        generateChartloop(jsondata);
                    }

                //                             chartData[div]["viewBys"][0]=prevviewby
                //                             chartData[div]["viewIds"][0]=prevviewbyid
                //                              parent.$("#chartData").val(JSON.stringify(chartData));


                }
            });
        }
    }
}


function drillWithinchart11(idArr,div,wid,hgt,regid,oneviewid,repname,repId,chartname1,drillviewby){
    var id1=div
    div=chartname1;
    chartname=div;
    oneviewid1=oneviewid;
    fromoneview='true';
    if(drillviewby=='olap'){
        regidch='olap';
    }else{
        regidch="Dashlets-"+regid;
    }
    parent.$("#chartname").val(chartname1);
    var prevviewby;
    var prevviewbyid;
    breadCrump = div;
    parent.$("#type").val("graph");
    var viewOvName = JSON.parse(parent.$("#viewby").val());
    var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    if(parent.$("#type").val()=="graph"){
        var drilltype = $("#drilltype").val();
        drilltype = "within";
        if(typeof drilltype !="undefined" && drilltype==="within"){
            var id = idArr.split(",");
            var drills = {};
            // previous drills
            if(typeof parent.$("#drills").val()!="undefined" && parent.$("#drills").val()!=""){
                try{
                    drills=JSON.parse(parent.$("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];
            var chartData = JSON.parse(parent.$("#chartData").val());
            drillValues.push(idArr.split(":"))
            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar" ){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0])
                drillNext.push(idArr.split(":")[1])
                drills[chartData[div]["viewIds"][0]] = drillFirst;
                drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                var drillMap = [];
                if(drillviewby=='save'){

                }else{
                    drillMap.push(idArr.split(":")[0]);
                    drills[chartData[div]["viewIds"][0]] = drillMap;
                }
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            }
            //            alert(parent.$("#chartData").val())
            prevviewby=chartData[div]["viewBys"][0]
            prevviewbyid=chartData[div]["viewIds"][0]
            $("#driver").val(div);
            var flag = true;
            try{
                if(drillviewby=='save'){

                }else{

                    for(var i=0;i<chartData[div]["dimensions"].length;i++){
                        if(chartData[div]["viewIds"][0]===chartData[div]["dimensions"][i]){
                            var view = [];
                            var viewName = [];
                            if(typeof chartData[div]["dimensions"][i+1] !="undefined"){
                                view.push(chartData[div]["dimensions"][i+1]);
                                flag = true;
                                if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar"){
                                    view=[];
                                    if(typeof chartData[div]["dimensions"][i+2]!="undefined"){
                                        view.push(chartData[div]["dimensions"][i+2]);
                                    }else{
                                        //                             alert("Please select more views");
                                        return "";
                                    }

                                    chartData[div]["viewIds"]= view;
                                }else{
                                    for(var m=0;m<view.length;m++){
                                        viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                    }
                                    chartData[div]["viewIds"]= view;
                                    chartData[div]["viewBys"]= viewName;
                                }
                            }
                            else{
                                //                    alert("Please select more views");
                                return "";
                            }
                            //                 chartData[div]["viewBys"]= view;
                            //                 drills[view[0]] = idArr.split(":");
                            break;
                        }
                    }
                }
            }
            catch(e){
            //            if(flag){
            //               alert("Please select more views");
            //            }
            }
            if(flag){
                parent.$("#chartData").val(JSON.stringify(chartData));
                parent.$("#drills").val(JSON.stringify(drills));
                $.ajax({
                    async:false,
                    type:"POST",
                    data: parent.$("#oneviewgraphForm").serialize()+"&oneviewID="+oneviewid+"&regid="+regid+"&reportName="+repname+"&reportId="+repId+"&idArr="+idArr+"&viewbydrill="+prevviewby+"&chartname="+chartname1+"&id1="+id1,

                    url: parent.ctxpath+"/reportViewer.do?reportBy=drilloneviewCharts&drilltype=within&fromoneview=true",
                    success: function(data){
                        //          $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                        //                             function(data){
                        var jsonVar=eval('('+data+')');

                        var  reportData=jsonVar.reportData;
                        var json = JSON.parse(reportData);
                        var jsonData = json[div];
                        if(json[div]==''|| json[div]=='null'){


                        }else{
                            if(id1!='null' && id1=='OLAPGraphRegion' ){

                            }else{
                                var  result1=jsonVar.result1;
                                parent.$("#"+regid).html(result1);
                            }
                            //                                 var id="Dashlets-"+0

                            var json1 = JSON.parse(parent.$("#measure").val());
                            parent.$("#"+id1).html("");
                            //                            buildoneviewPie(id1, jsonData,view,json1,wid,hgt,regid,oneviewid,repname,repId);
                            generateChartloop(reportData);
                        }
                        if(drillviewby=='save'){
                            chartData[div]["viewBys"][0]=prevviewby
                            chartData[div]["viewIds"][0]=prevviewbyid
                            parent.$("#chartData").val(JSON.stringify(chartData));
                        }


                    }
                });
            }
        }
        else if(drilltype==="single" || drilltype==="" || typeof drilltype==="undefined" ){

            var id = idArr.split(",");
            var drills = {};
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());
            drillValues.push(idArr.split(":")[0]);
            drills[chartData[div]["viewIds"][0]] = drillValues;
            $("#driver").val(div);
            $("#drills").val(JSON.stringify(drills));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(data){
                    generateChart(data);
                });
        }
        else if(drilltype==="multi"){

        }
    }





    else if($("#type").val()=="advance"){
        var drilltype = $("#drilltype").val();
        //          if(typeof drilltype !="undefined" && drilltype===""){
        var id = idArr.split(":");
        var drills = {};
        var drillValues = [];
        var chartData = JSON.parse($("#chartData").val());
        drillValues.push(idArr.split(":")[0]);
        drills[chartData[div]["viewIds"][0]] = drillValues;
        $("#driver").val(div);
        $("#drills").val(JSON.stringify(drills));
        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){
                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));

            });

    //          }

    }








    else{

        var drilltype = $("#drilltype").val();
        if(typeof drilltype==="undefined"  || drilltype==="within" || drilltype===""){
            var id = idArr.split(",");
            var drills = {};
            // previous drills
            if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
                try{
                    drills=JSON.parse($("#drills").val());
                }catch(e){
                }
            }
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());
            drillValues.push(idArr.split(":"))
            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar"){
                var drillFirst=[];
                var drillNext=[];
                drillFirst.push(idArr.split(":")[0])
                drillNext.push(idArr.split(":")[1])
                drills[chartData[div]["viewIds"][0]] = drillFirst;
                drills[chartData[div]["viewIds"][1]] = drillNext;
            }else{
                var drillMap = [];
                drillMap.push(idArr.split(":")[0]);
                drills[chartData[div]["viewIds"][0]] = drillMap;
            //            drills[chartData[div]["viewBys"][0]] = idArr.split(":")[0];
            }
            $("#driver").val(div);
            var flag = false;

            try{
                for(var i=0;i<chartData[div]["dimensions"].length;i++){
                    if(chartData[div]["viewIds"][0]===chartData[div]["dimensions"][i]){
                        var view = [];
                        var viewName = [];
                        if(typeof chartData[div]["dimensions"][i+1] !="undefined"){
                            view.push(chartData[div]["dimensions"][i+1]);
                            flag = true;
                            if(chartData[div]["chartType"]=="Grouped-Bar" || chartData[div]["chartType"]=="GroupedHorizontal-Bar"){
                                view=[];
                                if(typeof chartData[div]["dimensions"][i+2]!="undefined"){
                                    view.push(chartData[div]["dimensions"][i+2]);
                                }else{
                                    alert("No more levels of data to drill down");
                                    return "";
                                }

                                chartData[div]["viewIds"]= view;
                            }else{
                                for(var m=0;m<view.length;m++){
                                    viewName.push(viewOvName[viewOvIds.indexOf(view[m])]);
                                }
                                chartData[div]["viewIds"]= view;
                                chartData[div]["viewBys"]= viewName;
                            }
                            $("#chartData").val(JSON.stringify(chartData));
                        }

                        else{
                            alert("No more levels of data to drill down");
                            return "";
                        }
                        //                 chartData[div]["viewBys"]= view;
                        //                 drills[view[0]] = idArr.split(":");
                        break;
                    }
                }
            }
            catch(e){
                //            if(flag){
                alert("Please select more views");
            //            }
            }
            $("#drilltype").val("within");
            if(flag){
                $("#chartData").val(JSON.stringify(chartData));
                $("#drills").val(JSON.stringify(drills));
                $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                    function(data){
                        generateSingleTrend(data,div);
                    });
            }
        }
        else if(drilltype==="single" ){
            var id = idArr.split(",");
            var drills = {};
            var drillValues = [];
            var chartData = JSON.parse($("#chartData").val());
            drillValues.push(idArr.split(":"));
            drills[chartData[div]["viewIds"][0]] = idArr.split(":");
            $("#driver").val(div);
            $("#drills").val(JSON.stringify(drills));
            $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                function(data){
                    generateTrend(data);
                });
        }
        else if(drilltype==="multi"){

        }

    }


}


function trendAnalysisActionJs(repId,trendFlag,gridtr){
    grid=gridtr;
    $('#addUnderConsideration').html('');

    $("#xtendChartTD").html("");
//    $("#reportTD1").hide();
    $("#type").val("trend");
    $("#drills").val("");
    $("#filters1").val("");
     startValgbl=1;
    graphTrendFlag = trendFlag;
    //                       $("#xtendChartTD").hide();
    //                       $("#xtendChartTD").show();
    var htmlVar = "";
    $.post(
        //            'reportViewer.do?reportBy=getAvailableTrend&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
        //        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),
        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&type="+$("#type").val(),
        function(data) {
            //              alert("data "+data)
            if(data=="false"){
                $("#loading").hide();

                htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" name = "addGraphDataTrendAnalysis" onclick="editSingleTrend(this.name)">No Trend Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" name = "addGraphDataTrendAnalysis" onclick="editSingleTrend(this.name)">Please Add Trend from Option above.</h2></span> </div>';

                $("#addUnderConsideration").append(htmlVar);
                $("#content_1").hide();
                $("#xtendChartTD").append(htmlVar);
                htmlVar="";
                $("#xtendChartTD").show();
            }
            else{
                //                $("#loading").hide();
                var jsondata = JSON.parse(data)["data"];
                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                var meta = JSON.parse(JSON.parse(data)["meta"]);
                //                if(typeof meta["viewbys"]!="undefined" && meta["viewbys"].length>0){
                $("#viewby").val(JSON.stringify(meta["viewbys"]));
                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                $("#measure").val(JSON.stringify(meta["measures"]));
                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                $("#drilltype").val((meta["drillType"]));
                parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
            if(typeof parent.timeMapValue ==="undefined") {
                $("#timeMap").val(JSON.stringify(meta["timeMap"]));
            }
                if(typeof reportToTrendFilter !=="undefined"){
                    $("#filters1").val(JSON.stringify(reportToTrendFilter))
                }else{
                    $("#filters1").val(JSON.stringify(meta["filterMap"]));
                }
                if(typeof $("#chartData").val()!="undefined" && $("#chartData").val()!=""){
                    var chartData = JSON.parse($("#chartData").val());
                    var keys = Object.keys(chartData)
                    var draggableView = []

                    for(var j in keys){

                        //                        if(meta["draggableViewBys"][i]==chartData[keys[j]]["viewBys"]){
                        draggableView.push(chartData[keys[j]]["viewBys"][0]);
                      
                    //                        }
                    //                         }
                    }
                    $("#draggableViewBys").val(JSON.stringify(draggableView));
                }

                var jsonTrendData = JSON.parse(JSON.stringify(jsondata));

                $.ajax({
                    async:false,
                    type:"POST",
                    data:
                    $('#graphForm').serialize(),
                    url:  'reportViewer.do?reportBy=getFilters&typegbl=graph',

                    success:function(data) {
                        filterData=JSON.parse(data);

                         applyfiltersgraph();
if(savedfilter){
                         $("#filters1").val(JSON.stringify(meta["filterMap"]));
                     }
                         $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                        function(data){
                            var resultset=data;
                        if(typeof viewByFilterID !="undefined"){
                            applyFilterFromReport(viewByFilterID,viewByFilterName,fList)

                             setTimeout(updateglobalfilters(repId,trendFlag,"trend"), 20000);
                              $('#rightDiv1table').hide();
                               if (checkBrowser() == "ie") {
                                 
                   $('#rightDiv1trend').hide();
                     $("#xtendChartssTD").css({'margin-top':'0px'});

             }else{
                                $('#rightDiv1trend').show();
             }
                                
                        }else{
                             runtimeglobalfilters(JSON.stringify(meta["filterMap"]),'null',"trend");
                               var data1= JSON.parse(resultset)["data"];
                            generateChart(data1);
                        //               edited by manik
                        //               generateTrend(jsondata);
                        }
                    }
                    );

                    }
                });

            }
        });
        $("#openGraphSection").dialog('close');
}


//function trendAnalysisActionJs(data,measuresFlag,flag){
//                      $('#addUnderConsideration').html('');
//                       var jsonData = JSON.parse(data);
//                       var jsonResponseData = jsonData["JsonList"];
//                       var viewBysList = jsonData["viewBysList"];
//                       if(flag == 'trendAnalysisAction'){
//                       var measures = jsonData["measures"];
////                       var temp = measures.split(",");
//                       var measureame1 = measures[0];
//                       }
//                       else{
//                         var measures =   measuresFlag;
//
//                         var measurename1 = measures[0];
//
//                       }
//                       var wid = $(window).width()-80;
//                       var hgt = 650;
////                       var hgt = $(window).height()
//                       var htmlVar = "";
//                       htmlVar += "<div id='trendAnalysis' style='width:96%;margin-left:2%;float:left'>";
//                htmlVar += "<table id='tablechartTrendAnalysis' align='left' class='dashHeader'>" +
//                        "<tr><td>"+
////                        "<img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
//                        "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='trendAnalysis' onclick='editSingleTrend(this.name)'/>" +
////                        "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
//                        "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
////                        "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='trendAnalysisTab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
////                        "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
////                        "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
////                        "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
//                        "</td><td align='right' class='headDb'><a href='#' onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamecharttrendAnalysis'>"+measurename1+" </span></a></td></tr></table>";
//                htmlVar += "<div id='trendAnalysisTab' style='border:1px solid grey'></div></div>";
//
//                     $("#addUnderConsideration").append(htmlVar);
//                     htmlVar="";
//                       buildLine("trendAnalysisTab", jsonResponseData, viewBysList, measures, wid, hgt);
//                       $("#loading").hide();
//                       document.getElementById('addUnderConsideration').style.display = "block";
//            $("#reportTD1").hide();
//
//                $("#xtendChartTD").show();
//}
function trendAnalysisActionAddGraph(data){
    //                      $('#xtendChartTD').html('');
    var chartData = JSON.parse($("#chartData").val());
    var charts = Object.keys(data);
    //                       var charts = Object.keys(chartData);
    var htmlVar = "";

    for (var i = 1; i <= charts.length; i++) {

        var ch = "chart" + parseInt(i);


        //                       var hgt = $(window).height()


        htmlVar += "<div id='trendAnalysis"+i+"' style='width:96%;margin-left:2%;float:left'>";
        htmlVar += "<table id='tablechartTrendAnalysis"+i+"' align='left' class='dashHeader'>" +
        "<tr><td>"+
        //                        "<img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
        "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='trendAnalysis"+i+"_"+chartData[ch]["meassures"]+"' onclick='editSingleTrend(this.name)'/>" +
        //                        "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
        "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
        //                        "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='trendAnalysisTab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
        //                        "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
        //                        "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
        //                        "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
        "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamecharttrendAnalysis"+i+"'> "+chartData[ch]["meassures"]+" </span></a></td></tr></table>";
        htmlVar += "<div id='chart"+i+"' style='border:1px solid grey'></div></div>";
    }
    $("#xtendChartTD").append(htmlVar);
    //                      $("#xtendChartTD").style.display = "block";
    htmlVar="";
    for (var k = 0; k < charts.length; k++) {
        var ch = "chart" + parseInt(k+1);
        var currData=[];
        for(var m=0;m<(data[ch].length < 10 ? data[ch].length : 10);m++){
            currData.push(data[ch][m]);
        }
        var wid = $(window).width()-80;
        var hgt = 650;
        buildLine(ch, currData, chartData[ch]["viewBys"], chartData[ch]["meassures"], wid, hgt);

    }
    $("#loading").hide();
    //                       document.getElementById('addUnderConsideration').style.display = "block";

    $("#reportTD1").hide();

    $("#xtendChartTD").show();
}
function trendAnalysisActionEditGraph(data,measuresFlag,flag,trendChartId,trendChartMeasure){
    $('#addUnderConsideration').html('');
    var jsonData = JSON.parse(data);

    var jsonResponseData = jsonData["JsonList"];
    var viewBysList = jsonData["viewBysList"];
    for(var i in measuresFlag ){
        var measuresFlag1 = new Array();
        measuresFlag1.push(measuresFlag[i]);
        var wid = $(window).width()-80;
        var hgt = 650;
        //                       var hgt = $(window).height()

        var htmlVar = "";
        htmlVar += "<div id='trendAnalysis"+i+"' style='width:96%;margin-left:2%;float:left'>";
        htmlVar += "<table id='tablechartTrendAnalysis"+i+"' align='left' class='dashHeader'>" +
        "<tr><td>"+
        //                        "<img id='toggletable1' align='left' style=\"cursor: pointer\" class='ui-icon ui-icon-calculator'  />" +
        "<img id='' style=\"cursor: pointer\" align='left' class='ui-icon ui-icon-pencil' name='trendAnalysis"+i+"_"+measuresFlag1+"' onclick='editViewBys(this.name)'/>" +
        //                        "<img align='left' title='Refresh' onclick=\"refreshChart('chart1')\" style=\"cursor: pointer\" class='ui-icon ui-icon-arrowrefresh-1-s'/>" +
        //                        "<img align='left' title='Save' style=\"cursor: pointer\" onclick=\"saveChart('chart1')\" class='ui-icon ui-icon-newwin'/>" +
        //                        "<img id='' style='cursor: pointer;position:relative' align='left' class='ui-icon ui-icon-image' name='trendAnalysisTab' onclick='openCharts(this.name,\"" + wid + "\",\"" + hgt + "\")' />" +
        //                        "<img id='localFilterchart1' style='cursor: pointer;position:relative' align='left' title='localFilter' class='ui-icon ui-icon-arrowthick' name='' '/>" +
        //                        "<img id='I_rowchart1' style='cursor: pointer;position:relative' align='left' title='Decreas Height' class='ui-icon ui-icon-arrowthick-1-n' name='' onclick='decreasChartHeight(this.id)' />" +
        //                        "<img id='D_rowchart1' style='cursor: pointer;position:relative' align='left' title='Increas Height' class='ui-icon ui-icon-arrowthick-1-s' name='' onclick='increasChartHeight(this.id)' />" +
        "</td><td align='right' class='headDb'><a onclick=\"openRenameDiv('chart1')\"><span id='dbChartNamecharttrendAnalysis"+i+"'> "+measuresFlag1+" </span></a></td></tr></table>";
        htmlVar += "<div id='trendAnalysisTab"+i+"' style='border:1px solid grey'></div></div>";

        $("#addUnderConsideration").append(htmlVar);
        htmlVar="";
        buildLine("trendAnalysisTab"+i, jsonResponseData, viewBysList, measuresFlag1, wid, hgt);
        $("#loading").hide();
        document.getElementById('addUnderConsideration').style.display = "block";
    }
    //                       $('#trendAnalysis').show();
    //                       $('#trendAnalysisDiv1').show();
    //                            $("#showFormulaHandsOnTable").html(data);
    //                           $("#showFormulaHandsOnTable").dialog('open');
    $("#reportTD1").hide();

    $("#xtendChartTD").show();
}

function generateSingleChart(data,chartId){
   var jsondata = JSON.parse(data)["data"];
    var fromoneview=parent.$("#fromoneview").val();
    var div1=parent.$("#chartname").val();
    if(fromoneview!='null'&& fromoneview=='true'){
        jsondata = JSON.parse(data)
        graphData[div1]=jsondata[div1];
    }else if(typeof jsondata !=="undefined" && jsondata !==""){
    graphData=JSON.parse(jsondata)
    
        }
        else{
          graphData=JSON.parse(data);
       
    }
 
 
    //     if(typeof (jsondata)!=Object){
    //    graphData=JSON.parse(jsondata);
    //    }
    var chartData = JSON.parse(parent.$("#chartData").val());

    var divContent="";
    //    var chartId="";
    var charts = Object.keys(chartData);
    var width = $(window).width();
    var divHeight = $(window).height();
    var divcontlist = "";
    var widths = [];
    var heights = [];
    var widFact = 0;
    var heightFact = 0;
    var j = 0;
    var sameRowFlag = true;
    //    divContent += "<div  style='width:100%'><table style='width:88%;float:left'><tr id='row0'>";

    $("#noData").hide();
    $("#xtendChartTD1").show();
    //                $("#xtendChartTD").html(divContent);


    var data = graphData;
    var radius = 600/3.5;
    //          for (var k = 0; k < charts.length; k++) {
    //              var ch = "chart" + parseInt(k+1);
    var ch = chartId;
    var currData=[];
    var records = 10;
    $("#"+ch).html("");
    if(fromoneview!='null'&& fromoneview=='true'){
        ch=div1

    }

    if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
        records = chartData[ch]["records"];
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        if(JSON.stringify(graphData)=="{}"||JSON.stringify(data[ch])=="[]" || data[ch]=="undefined" ){

        }else{
            for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
                currData.push(data[ch][m]);
            }
            //    alert("chartType  "+JSON.stringify(chartData[ch]["chartType"]));
            chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);
        }
    }else{
        for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
            currData.push(data[ch][m]);
        }
        //    alert("chartType  "+JSON.stringify(chartData[ch]["chartType"]));
        chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);
    }


//    var drilledvalue1 = parent.$("#drills").val();
//    var drilledvalue = {};
//    var  drilledvalue3 = [];
//    var drilledvalue2 = [];
//    var drilltype = $("#drilltype").val();
//    var htmlcontent = "";
//    if(chartId == breadCrump){
//        if (typeof drilledvalue1 !== 'undefined' && drilledvalue1.length > 0  && (typeof drilltype !="undefined" && drilltype==="within")) {
//
//            divcontlist +=   "<div id = breadcrump'"+chartId+"' style='display:block;margin-top:-15px;padding-left: 1em;' ><table><tr style = 'width:'"+widths[k]+"'>";
//
//            if(drilledvalue1!="undefined" && drilledvalue1!=""){
//
//                drilledvalue2 = JSON.parse(drilledvalue1);
//                drilledvalue3 = Object.keys(drilledvalue2);
//
//                for(var i=0;i<drilledvalue3.length;i++){
//                    drilledvalue[i] = drilledvalue2[drilledvalue3[i]]
//                    divcontlist +=    " <td style ='color:#808080;padding-left: .5em;'>"+drilledvalue[i]+" ></td>";
//                }
//            }
//            divcontlist +=    "</tr></table></div>";
//        }
//    }
if(fromoneview!='null'&& fromoneview=='true'){
    ch=chartId
}
$("#"+ch).append(divcontlist);
}

function callGraphOpt(chartId,tableId,k,divHeight,widths,event){
	var chWidth=$("#divchart"+chartId.replace ( /[^\d.]/g, '' )).width();
    var chHeight=$("#divchart"+chartId.replace ( /[^\d.]/g, '' )).height();
    $("#graphOption"+chartId).html("");
    $("#graphOption"+chartId+"graph").html("");
    var chartData = JSON.parse($("#chartData").val());
    var drilltype = chartData[chartId]["drillType"];
    var chartIdTile = widths;
    var isComparison = "";
    var clickPosition=event.pageX;
    var pull="right1";
    var pull1="right1";
    if(parseInt(clickPosition)>(screen.width-250)){
        pull="left1";
    }
    if(parseInt(clickPosition)>(screen.width-120)){
        pull1="left1";
    }
    
    if(typeof chartData[chartId]["isComparison"] !=="undefined" && chartData[chartId]["isComparison"]==="LineComparison"){
        isComparison = "true";
    }
    var type =$("#type").val();
    //             if(typeof drilltype !="undefined" && drilltype==="within"){
    var html ="";
//    html+="<li id='chartType"+chartId+"' class='dropdown-submenu pull-left'><a class='gFontFamily gFontSize13' onmouseover='openChartGroups(\"chart"+k+"\")' >Graph Types</a>";
//    html+="<li id='quickFilter"+chartId+"' class='dropdown-submenu pull-"+pull1+"'><a class='gFontFamily gFontSize13' onmouseover='openQuickFilter(\"chart"+k+"\")' >"+translateLanguage.Quick_Filter+"</a>";
  //  html+="<li id='measuresList"+chartId+"' class='dropdown-submenu pull-left'><a class='gFontFamily gFontSize13' onmouseover='loadMeasures(\"chart"+k+"\")' >Custom Time Agg</a>";
    if(typeof type!=="undefined" && type == "trend"){
        html+="<li><a class='gFontFamily gFontSize13' onclick='localEditSingleTrend(\"chart"+k+"\")'>"+translateLanguage.Edit_Trend+"</a></li>";
    }else{
        html+="<li><a title='Click to change viewBys and measures' class='gFontFamily gFontSize13' onclick='editViewBys(\"chart"+k+"\")'>"+translateLanguage.Edit_Graph+"</a></li>";
    }
    html+="<li><a title='Click to change the graph features' class='gFontFamily gFontSize13' onclick='editDash(\"" + chartId + "\",\"" + tableId + "\",\"kpi_div" + (k) + "\",\"" + divHeight + "\",\"" + widths + "\")'>"+translateLanguage.Graph_Properties+"</a></li>";
     /*Ashutosh*/
    html+="<li id='drill"+chartId+"' style='background-color:#808080' class='dropdown-submenu pull-"+pull+"'><a class='gFontFamily gFontSize13' >Drill Type</a>";
    html +="<ul id='drill_sub"+chartId+"' class='dropdown-menu'>";
    if(typeof drilltype !=="undefined" && drilltype=="within"){
        html+="<li id='drillWithin' style='background-color:#808080'><a title='Click to enable drill down on the chart' name='within' class='gFontFamily gFontSize13'  onclick='selectDrill(this.name,\""+chartId+"\")' style='color:white'>"+translateLanguage.Drill_Down+"</a></li>";
    }else {
        html+="<li id='drillWithin'><a title='Click to enable drill down on the chart' class='gFontFamily gFontSize13' name='within'  onclick='selectDrill(this.name,\""+chartId+"\")'>"+translateLanguage.Drill_Down+"</a></li>";
    }
     if(typeof drilltype !=="undefined" && drilltype=="nodrill"){
        html+="<li id='nodrill' style='background-color:#808080'><a title='Click to disable the drill down' name='nodrill' class='gFontFamily gFontSize13'  onclick='selectDrill(this.name,\""+chartId+"\")' style='color:white'>No Drill</a></li>";
    }else {
        html+="<li id='nodrill'><a title='Click to disable the drill down' class='gFontFamily gFontSize13' name='nodrill'  onclick='selectDrill(this.name,\""+chartId+"\")'>No Drill</a></li>";
    }
        if(typeof drilltype !=="undefined" && drilltype=="reportDrill"){
            html+="<li id='drillMulti' style='background-color:#808080'><a title='Click to define which report should open on drill' class='gFontFamily gFontSize13' name='reportDrill'  onclick='selectDrill(this.name,\""+chartId+"\")' style='color:white'>"+translateLanguage.Report_Drill+"</a></li>";
        }else {
            html+="<li id='drillMulti'><a title='Click to define which report should open on drill' class='gFontFamily gFontSize13' name='reportDrill'  onclick='selectDrill(this.name,\""+chartId+"\")'>"+translateLanguage.Report_Drill+"</a></li>";
        }
        if(typeof drilltype !=="undefined" && drilltype=="pageDrill"){
            html+="<li id='pageDrill' style='background-color:#808080'><a title='Click to define a specific page on drill' class='gFontFamily gFontSize13' name='pageDrill'  onclick='selectDrill(this.name,\""+chartId+"\")' style='color:white'>Page Drill</a></li>";
        }else {
            html+="<li id='pageDrill'><a title='Click to define a specific page on drill' class='gFontFamily gFontSize13' name='pageDrill'  onclick='selectDrill(this.name,\""+chartId+"\")'>Page Drill</a></li>";
        }
        
    /*Ashutosh*/
    if(chartData[chartId]["chartType"]!=="Combo-MeasureH-Bar"){
    if(typeof drilltype ==="undefined" || drilltype ===""  || drilltype ==="single"){
        html+="<li id='drillAcross' style='background-color:#808080'><a title='Click this means all the other charts in that page will get affected if any filter gets applied on this chart' name='single' class='gFontFamily gFontSize13' onclick='selectDrill(this.name,\""+chartId+"\")' style='color:white'>"+translateLanguage.Drill_Across+"</a></li>";
    }else {
        html+="<li id='drillAcross'><a title='Click this means all the other charts in that page will get affected if any filter gets applied on this chart' name='single' class='gFontFamily gFontSize13' onclick='selectDrill(this.name,\""+chartId+"\")'>"+translateLanguage.Drill_Across+"</a></li>";
    }
    }
     if(typeof drilltype !=="undefined" && drilltype ==="action"){
        html+="<li id='actionDrill' style='background-color:#808080'><a title='Action Driven Charts' name='action' class='gFontFamily gFontSize13' onclick='selectDrill(this.name,\""+chartId+"\")'>"+translateLanguage.Action_Drill+"</a></li>";
     }else{
        html+="<li id='actionDrill'><a title='Action Driven Charts' name='action' class='gFontFamily gFontSize13' onclick='selectDrill(this.name,\""+chartId+"\")'>"+translateLanguage.Action_Drill+"</a></li>";
     }
    html +=" </ul></li>";

        if(typeof isInit[chartId] !== "undefined" && isInit[chartId] == true){
        html+="<li id='initialize' style='background-color:#808080'><a title='Click to make this chart as driver for all the dependant charts selected in the next UI' class='gFontFamily gFontSize13' name='initializeGraph'  onclick='initializeGraph1(\""+chartId+"\")' style='color:white'>"+translateLanguage.Initialize_Graph+"</a></li>";
    }else {
        html+="<li id='initialize'><a title='Click to make this chart as driver for all the dependant charts selected in the next UI' class='gFontFamily gFontSize13' name='multi'  onclick='initializeGraph1(\""+chartId+"\")'>"+translateLanguage.Initialize_Graph+"</a></li>";
    }
   

    //   html+="<li><a name='multi' onclick='selectDrill(this.name)'>Multi Select Drill</a></li>";
    html+="<li><a title='Click here to view or edit local filters applied on this chart' class='gFontFamily gFontSize13' name='"+chartId+"' onclick='localFilterUI(this.name)'>"+translateLanguage.Local_Filter+"</a></li>";
    html+="<li><a class='gFontFamily gFontSize13' name='"+chartId+"' onclick='downloadExcel1(this.name)'>"+translateLanguage.Download_Excel+"</a></li>";
        /*Added by Ashutosh*/
    if(chartData[chartId]["chartType"]==="KPI-Dashboard" || chartData[chartId]["chartType"]==="XYZ-Dashboard"){
        html+="<li><a class='gFontFamily gFontSize13' name='"+chartId+"' onclick='resequenceKPIDashboard(this.name)'>"+translateLanguage.Resequence_Comparison+"</a></li>";
        html+="<li><a class='gFontFamily gFontSize13' name='"+chartId+"' onclick='enableMenuCustom(\""+chartId+"\",\"\","+chWidth+","+chHeight+","+false+")'>"+translateLanguage.Edit_Comparison+"</a></li>";
    }else{
     html+="<li id='measuresList"+chartId+"' class='dropdown-submenu pull-"+pull+" '><a title='Click to enable comparison of data of two years for selected measures' class='gFontFamily gFontSize13' name='"+chartId+"'  onmouseover='loadMeasures(this.name)''>"+translateLanguage.Enable_Comparison+"</a></li>";
    }
//       html+="<li id='measuresList"+chartId+"' class='dropdown-submenu pull-left'><a onmouseover='loadMeasures(\"chart"+k+"\")' >Custom Time Agg</a>";
    if(chartData[chartId]["chartType"] !== "undefined" && (chartData[chartId]["chartType"] === "Standard-KPI" || chartData[chartId]["chartType"] === "Standard-KPI1")){
    html+="<li><a class='gFontFamily gFontSize13' name='"+chartId+"' onclick='defineAlerts(this.name,"+$('#graphsId').val()+","+$('#usersId').val()+") '>"+translateLanguage.Define_Alerts+"</a></li>";
    }
    html+="<li><a title='Click to refresh the chart' class='gFontFamily gFontSize13' name='"+chartId+"' onclick='localRefresh(this.name)'>"+translateLanguage.Refresh+"</a></li>";
    // for image tag
    html+="<li><a name='"+chartId+"' title='Click to upload an image from the system' class='gFontFamily gFontSize13' onclick='addImageTag(this.name)'>"+translateLanguage.Add_Image+"</a></li>";

    //     html+="<li><a onclick='refreshChart("+chartId+")'>Refresh Graph</a></li>";
   
    if(parent.userType !=="ANALYZER"){
//        if(typeof type!=="undefined" && type == "trend"){
//            html+="<li><a onclick='saveXtTrend("+chartId+")'>Save Trend</a></li>";
//        }else{
            html+="<li><a title='Click to save the graph' class='gFontFamily gFontSize13' onclick='saveXtCharts("+chartId+")'>"+translateLanguage.Save_Graph+"</a></li>";
//        }
        html+="<li><a title='Click to delete the chart' class='gFontFamily gFontSize13' onclick='deleteChart(\""+chartId+"\")'>"+translateLanguage.Delete_chart+"</a></li>";
    }
//    html+="<li><a onclick='toggleChart(\""+chartId+"\")'>KPI/Graph</a></li>";
//    html+="<li><a onclick='getMeasureFilterUI(\""+chartId+"\")'>Apply Measure Filter</a></li>";
    if(isComparison!=="true" && (chartData[chartId]["chartType"]==="DualAxis-Bar" || chartData[chartId]["chartType"]==="DualAxis-Target" || chartData[chartId]["chartType"]==="DualAxis-Group"|| chartData[chartId]["chartType"]==="Veraction-Combo3")){
        html+="<li><a class='gFontFamily gFontSize13' onclick='editNewCharts(\""+chartId+"\")'>"+translateLanguage.Edit_Measures+"</a></li>";
    }
    if(isComparison=="true" && (chartData[chartId]["chartType"]==="DualAxis-Bar" || chartData[chartId]["chartType"]==="DualAxis-Target" || chartData[chartId]["chartType"]==="DualAxis-Group")){
        html+="<li><a class='gFontFamily gFontSize13' onclick='editRuntimeMeasure(\""+chartId+"\")'>"+translateLanguage.Edit_Runtime_Measures+"</a></li>";
    }
    if(chartData[chartId]["chartType"]!="Trend-KPI" && chartData[chartId]["chartType"]!="Trend-Combo" &&  chartData[chartId]["chartType"]!="Combo-Analysis" &&  chartData[chartId]["chartType"]!="Table" && chartData[chartId]["chartType"]!="World-Top-Analysis" && chartData[chartId]["chartType"]!="world-map"){

    html+="<li><a id='download_"+chartId+"' title='Click to save the graph as an image' class='gFontFamily gFontSize13' onclick='downloadAsImage(\""+chartId+"\")'>"+translateLanguage.Save_as_an_Image+"</a></li>";
}
//    html+="<li><a id='download_"+chartId+"' class='gFontFamily gFontSize13' onclick='downloadAsImage(\""+chartId+"\")'>"+translateLanguage.Show_Viewby+"</a></li>";
    html+="<li id='shortviewby"+chartId+"' class='dropdown-submenu pull-"+pull1+"'><a class='gFontFamily gFontSize13' >"+translateLanguage.Show_Viewby+"</a>";
    
    
    html +="<ul id='morechart1' class='dropdown-menu'>";
    html +="<li id='viewByDisplayTypeAll' onclick='setViewByDisplayTypeAll(\""+chartId+"\")' class='dropdown-submenu pull-left1'><a class='gFontFamily gFontSize13'>Complete ViewBys </a></li>";
    html +="<li id='viewByDisplayTypeChart' onclick='setViewByDisplayTypeChart(\""+chartId+"\")' class='dropdown-submenu pull-left1'><a class='gFontFamily gFontSize13'>Chart ViewBys</a></li>"
    
    html +=" </ul></li>";
    
    html += "</ul>";
    if(chartIdTile == chartId+"Tile"){
        $("#graphOption"+chartId+"graph").html(html);
    }else{
        $("#graphOption"+chartId).html(html);
    }
    var id1='graphOption'+chartId;
    if(event.pageX>(screen.width-170)){
       // alert()
        $("#"+id1).css("left",-165);
    }


}

//Added by sandeep
function callGraphOpt1(chartId,tableId,k,chartname,chartData,repid,repname,enablefilter){
    var divid="chart"+k;
    $("#graphOption"+divid).html("");

    var html ="";
    html+="<li ><a  onclick=\"oneviewupdate('"+k+"','"+chartId+"','"+divid+"','"+repid+"','"+repname+"','"+oneviewid1+"','refresh')\" >Refresh Region</a></li>";

    html+="<li><a name='"+divid+"' onclick=\"oneviewupdate('"+k+"','"+chartId+"','"+divid+"','"+repid+"','"+repname+"','"+oneviewid1+"','save')\">Save</a></li>";
    html+="<li><a onclick=\"parent.renameRegion('"+k+"','"+k+"','"+chartId+"','"+divid+"','"+repid+"','"+repname+"','"+oneviewid1+"','renamae')\" >Rename</a></li>";
    html+="<li><a >Save Image</a></li>";
    html+="<li><a onclick=\" oneviewlocalFilterUI('"+chartId+"','"+repid+"','"+oneviewid1+"','"+parent.oneViewName+"','"+repname+"','"+k+"')\">Local Filter</a></li>";

    html+="<li  id='chartType1"+divid+"' class='dropdown-submenu pull-left'><a onmouseover=\"openChartsglobal('"+k+"','"+chartId+"','"+divid+"','"+repid+"','"+repname+"','"+oneviewid1+"','"+enablefilter+"')\">Global Filter</a></li>";
    html+="<li  id='oneviewtime"+divid+"' class='dropdown-submenu pull-left'><a onmouseover=\"openChartstime('"+k+"','"+chartId+"','"+divid+"','"+repid+"','"+repname+"','"+oneviewid1+"','"+enablefilter+"')\">Oneview Time</a></li>";
    //    html+="<li  id='chartType"+divid+"' class='dropdown-submenu pull-left'><a onmouseover='openCharts1(\"chart"+k+"\")'>Region Options</a></li>";

    html += "</ul>";
    //    $("#graphOption"+chartId).show();
    $("#graphOption"+divid).toggle();
    $("#graphOption"+divid).html(html);
//  alert("come here Again==="+html);
}

function openChartsglobal(regid,chartId,divid,repId,repname,oneviewid,enablefilter){
    //  alert("hgcshjg==="+chartId)
    var graphs = Object.keys(parent.graphsMap);

    var html = "";
    html = html + "<ul id='more"+divid+"' class='dropdown-menu'>";
    if(enablefilter!="undefined" && enablefilter=="disablefilter"){
        html+="<li id='enablefilter' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','enablefilter')\">Enable filter</a></li>";
        html+="<li id='disablefilter' style='background-color:#808080' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','disablefilter')\">Disable Filter </a></li>";

    }else{
        html+="<li id='enablefilter' style='background-color:#808080' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','enablefilter')\">Enable filter</a></li>";
        html+="<li id='disablefilter'  ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','disablefilter')\">Disable Filter </a></li>";
    }

    html = html + "</ul>";//changed by sruthi for ie9
    //alert(html)
    html = html +"<input type=\"hidden\"  id='enable"+regid+"' name='enable"+regid+"' value=\"\">";
    $("#chartType1"+ divid).append(html);
}
function openChartstime(regid,chartId,divid,repId,repname,oneviewid,enablefilter){
    //  alert("hgcshjg==="+chartId)
    var graphs = Object.keys(parent.graphsMap);

    var html = "";
    html = html + "<ul id='more1"+divid+"' class='dropdown-menu'>";
    //if(enablefilter!="undefined" && enablefilter=="disablefilter"){
    //     html+="<li id='enablefilter' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','enablefilter')\">Enable filter</a></li>";
    //      html+="<li id='disablefilter' style='background-color:#808080' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','disablefilter')\">Disable Filter </a></li>";
    //
    //}else{
    html+="<li id='enabletime'  ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','enabletime')\">Enable Time</a></li>";
    html+="<li id='disabletime' style='background-color:#808080' ><a  onclick=\"oneviewupdate('"+regid+"','"+chartId+"','"+divid+"','"+repId+"','"+repname+"','"+oneviewid+"','disabletime')\">Disable Time </a></li>";
    //}

    html = html + "</ul>";//changed by sruthi for ie9
    //alert(html)
    html = html +"<input type=\"hidden\"  id='enabletime"+regid+"' name='enabletime"+regid+"' value=\"\">";
    $("#oneviewtime"+ divid).append(html);
}
function hidetimedetails(k){
    $("#timeOption"+k).toggle();
}
function showtimedetails(k,oneviewid1,chartId,timedetails){
     var filterGroupBy=[];
 filterGroupBy = timedetails.split(",");
    var html ="";
 if(filterGroupBy[1]=="PRG_STD"){
     var divid="chart"+k;
    $("#timeOption"+k).html("");


    html+="<li >Date:"+filterGroupBy[2]+"</li>";
    html+="<li>From:"+filterGroupBy[3]+"</li>";
     html+="<li >compare:"+filterGroupBy[4]+"</li>";
//      html += "</ul>";
//    html+="<td>"+filterGroupBy[3]+"</td></tr>";
//    html+="<tr><td>Compare:</td>";
//     html+="<td>"+filterGroupBy[3]+"</td></tr></table>";
//    $("#graphOption"+chartId).show();

 }else{

    html+="<li >From   :"+filterGroupBy[2]+"</li>";
    html+="<li> TO     :"+filterGroupBy[3]+"</li>";
     html+="<li >CompareFrom :"+filterGroupBy[4]+"</li>";
     html+="<li >CompareTo   :"+filterGroupBy[4]+"</li>";
 }
 $("#timeOption"+k).toggle();
    $("#timeOption"+k).html(html);

}
//end of sandeep code
function openCharts1(chartId,width,height){
    //  alert("hgcshjg==="+chartId)
    var graphs = Object.keys(parent.graphsMap);

    var html = "";
    //      alert("hgcshjg===1")
    //        html = html + "<table>";
    html = html + "<ul id='more"+chartId+"' class='dropdown-menu'>";

    html+="<li><a name='"+chartId+"' onclick='localFilterUI(this.name)'>Rename</a></li>";
    //      html+="<li><a name='"+chartId+"' onclick='localFilterUI(this.name)'>Drill</a></li>";
    html+="<li><a name='"+chartId+"' onclick='localFilterUI(this.name)'>Save image</a></li>";
    //        html+="<li><a name='"+chartId+"' onclick='localFilterUI(this.name)'>Transpose</a></li>";
    //        alert("hgcshjg===2")
    html = html + "</ul>";//changed by sruthi for ie9

    $("#chartType"+ chartId).append(html);
}
function generateSingleTrend(data,chartId){

    var jsondata = JSON.parse(data);
    graphData[chartId]=jsondata[chartId];
    var chartData = JSON.parse($("#chartData").val());
    $("#noData").hide();
    $("#xtendChartTD1").show();
    var data = graphData;
    var ch = chartId;
    var currData=[];
    var records = 10;
    $("#"+ch).html("");
    if (typeof chartData[ch]["records"] !== "undefined" && chartData[ch]["records"] !== "") {
        records = chartData[ch]["records"];
    }
    for(var m=0;m<(data[ch].length < records ? data[ch].length : records);m++){
        currData.push(data[ch][m]);
    }
    chartTypeFunction(ch,chartData[ch]["chartType"],chartData[ch]["Name"]);

}

function closeHideParam(){
    document.getElementById("tabParametersTop").style.display= 'none';
   document.getElementById("tabParametersTopParent").style.display='none';
}
var chartname;
var oneViewIdValue;
var colNumber;
function customTimeAggregation(oneviewletId,oneviewId,chartname){
    colNumber=oneviewletId;
    oneViewIdValue=oneviewId;
    chartname=chartname
    parent.$("#chartname").val(chartname);
    parent.$("#regid1").val(oneviewletId);
    getCustomTimeMsrDetails(oneviewId,oneviewletId);
    $("#customTimeMsr").dialog({
        autoOpen: false,
        height: 250,
        width: 400,
        position: 'justify',
        modal: true
    });
    $("#customTimeMsr").dialog('open');
}
function customTimeSelection(){
    if($("#customTimeOption").is(":checked")){
        $("#customTimeTable").show();
    }else{
        $("#customTimeTable").hide();
    }
}

//sandeep for local filter in oneview
var oneiewfilterData={};

function oneviewlocalFilterUI(chartid,repId,oneviewid,oneViewName,reportName,regid){
    $.ajax({
        async:false,
        type:"POST",
        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+repId+"&oneviewid="+oneviewid+"&regid="+regid+"&oneviewName="+oneViewName+"&reportName="+encodeURIComponent(reportName),

        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&fromoneview=true&action=localfilter&oneviewtime="+parent.oneviewTimecheck,
        success: function(data){
            $("#loading").hide();
            if(data=="false"){
            }
            else{
                var jsondata = JSON.parse(data)["data"];
                filterData = JSON.parse(JSON.parse(data)["getFilters"]);
                parent.$("#filterdata1").val(JSON.parse(data)["getFilters"]);
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                parent.$("#timedetails").val(JSON.parse(data)["Timedetails"]);
                  parent.$("#timeMap").val(JSON.stringify(meta["timeMap"]));
                //                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));
                enablefilter=meta["enablefilter"];
                setTimeout(localFilterUIoneview(chartid,repId,oneviewid,chartid,reportName,regid), 20000);
            }
        }
    });
}
function localFilterUIoneview(chartId,repId,oneviewid,chartname,reportName,regid) {
    $("#driver").val("");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    var viewIds = chartDetails["viewIds"];
    if(typeof chartData[chartId]["dimensions"]!=="undefined" && chartData[chartId]["dimensions"]!==""){
        viewIds = chartData[chartId]["dimensions"];
    }
    $("#graphOptionchart"+regid).hide();
    parent.filterDataGlb = filterData;
    var divHeight = ($(window).height() - $(window).height() / 6);
    var divWidth = ($(window).width() - ($(window).width() / 1.4));
    var html=""
    //   html += "<div id='tempDashletDiv1' style='display: none'></div>";
    //  parent.$("#regionTableId").append(html);
    $("#tempDashletDiv1").dialog({
        autoOpen: false,
        height: divHeight,
        width: divWidth,
        position: 'justify',
        modal: true,
        title: "Edit Filter: " //+ chartDetails["Name"]
    });

    $("#tempDashletDiv1").dialog('option', 'title', 'Edit Filter:');

    //                    $("#tempDashletDiv1").dialog('open');

    var filterKeys = Object.keys(filterData);
    var filterMap = {};
    if (typeof chartDetails["filters"] !== "undefined") {
        filterMap = chartDetails["filters"];
    }
    var html = "";
    var filterKeys = chartData[chartId]["dimensions"];
    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
        filterKeys=chartData[chartId]["viewBys"];
    }
    if(fromoneview!='null'&& fromoneview=='true'){
        filterKeys=chartData[chartId]["viewBys"];
        oneiewfilterData=filterData;
    }
    //alert(JSON.stringify(filterMap))
    for (var key in filterKeys) {
        var filterGroupBy = filterKeys[key];
        var filters = filterData[filterGroupBy];
        var selectedFilters = [];
        if (typeof filterMap[viewIds[key]] !== "undefined") {
            selectedFilters = filterMap[viewIds[key]];
        }
        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv1(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
        //        html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";

        if (selectedFilters.length === 0) {

            html += "<input type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "'  onclick='selectAllLocal(this.id,this.name)'/>";

        } else {
            html += "<input type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' checked onclick='selectAllLocal(this.id,this.name)'/>";
        }
        html += "<span>&nbsp;" + filterGroupBy + "</span></label></div>";
        html += "</div>";
        html += "<div id='localFilter"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none'><table style='' width='100%'>";
        for (var filter in filters) {
            if(key == 0 && filter ==0){
                html += "<tr><td >";
                if(fromoneview!='null'&& fromoneview=='true'){
                    html +="Enable GlobalFilter </td><td> <input type='checkbox' id='"+chartId+"_globalfilter' >";
                    html +="Enable Others </td><td> <input type='checkbox' id='"+chartId+"_othersL' >";
                }else{
                    if(chartData[chartId]["othersL"]!=="undefined" && chartData[chartId]["othersL"]==="Y"){
                        html +="Enable Others </td><td> <input type='checkbox' id='"+chartId+"_othersL' checked>";
                    }else{
                        html +="Enable Others </td><td> <input type='checkbox' id='"+chartId+"_othersL' >";
                    }
                }
                html += "</td></tr>";
            }
            html += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
            if(typeof filters[filter]!="undefined" && filters[filter]!=""){
                if (selectedFilters.indexOf(filters[filter]) !== -1  && filters[filter]!="") {
                    html += "<input type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "' checked value='" + filters[filter] + "' name='" + viewIds[0] + "'/>";
                } else {
                    //                if(filters[filter]!=""){

                    html += "<input type='checkbox' class='ckbox' id='L_1" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "'  value='" + filters[filter] + "' name='" + viewIds[0] + "'/>";

                //                }
                }
                html += "<span class='box'>&nbsp;" + filters[filter] + "</span></span></label></td></tr>";
            }
        }
        html += "</table></div>";
    }
    html += "<table><tr><td><input type='button' value='done' onClick='saveFiltersoneview(\"" + chartId + "\",\"" + repId + "\",\"" + oneviewid + "\",\"" + chartname + "\",\"" + reportName + "\",\"" + regid+ "\")' /></td>";

    html +="</tr></table>";
    $("#tempDashletDiv1").html(html);
    $("#tempDashletDiv1").html(html);
    $("#tempDashletDiv1").dialog('open');
    $("#tempDashletDiv1").dialog('open');
}
//function localFilterUI(chartId) {
//    $("#driver").val("");
//    var chartData = JSON.parse($("#chartData").val());
//    var chartDetails = chartData[chartId];
//    var viewIds = chartDetails["viewIds"];
//    if(typeof chartData[chartId]["dimensions"]!=="undefined" && chartData[chartId]["dimensions"]!==""){
//        viewIds = chartData[chartId]["dimensions"];
//    }
//
//    parent.filterDataGlb = filterData;
//    var divHeight = ($(window).height() - $(window).height() / 6);
//    var divWidth = ($(window).width() - ($(window).width() / 1.4));
//    parent.$("#tempDashletDiv").dialog({
//        autoOpen: false,
//        height: divHeight,
//        width: divWidth,
//        position: 'justify',
//        modal: true,
//        title: translateLanguage.Edit_Filter+':'  //+ chartDetails["Name"]
//    });
//    
//
//    var filterKeys = Object.keys(filterData);
//    var filterMap = {};
//    if (typeof chartDetails["filters"] !== "undefined") {
//        filterMap = chartDetails["filters"];
//    }
//    var html = "";
//    var filterKeys = chartData[chartId]["dimensions"];
//    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
//        filterKeys=chartData[chartId]["viewBys"];
//    }else{
//        var viewOvName = JSON.parse(parent.$("#viewby").val());
//        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
//        var temp = filterKeys;
//        filterKeys=[];
//        for (var key in temp) {
//            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
//        }
//    }
//  
//
//    for (var key in filterKeys) {
//      
//        var filterGroupBy = filterKeys[key];
//        var filters = filterData[filterGroupBy];
//        var selectedFilters = [];
//        if (typeof filterMap[viewIds[key]] !== "undefined") {
//            selectedFilters = filterMap[viewIds[key]];
//        }
//        if(key==0){
//            html += "<table style='' width='100%'><tr><td class='gFontFamily gFontSize12' >";
//            if(chartData[chartId]["globalEnable"]!=="undefined" && chartData[chartId]["globalEnable"]==="N"){
//                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' >";
//            }else{
//                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' checked>";
//            }
//            html += "</td></tr></table>";
//        }
//        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv1(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
//        //        html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
//
//        if (selectedFilters.length === 0) {
//            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' checked onclick='selectAllLocal(this.id,this.name)'/>";
//        } else {
//            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' onclick='selectAllLocal(this.id,this.name)'/>";
//        }
//        html += "<span class='gFontFamily gFontSize12'>&nbsp;" + filterGroupBy + "</span></label></div>";
//        html += "</div>";
//        html += "<div id='localFilter"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none'><table style='' width='100%'>";
//        for (var filter in filters) {
//            if(key == 0 && filter ==0){
//               
//                
//                html += "<tr><td class='gFontFamily gFontSize12' >";
//                if(chartData[chartId]["othersL"]!=="undefined" && chartData[chartId]["othersL"]==="Y"){
//                    html +="Enable Others </td><td> <input type='checkbox' id='"+chartId+"_othersL' checked>";
//                }else{
//                    html +="Enable Others </td><td> <input type='checkbox' id='"+chartId+"_othersL' >";
//                }
//                html += "</td></tr>";
//            }
//            html += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
////            if(typeof filters[filter]!="undefined" && filters[filter]!=""){
//            if(typeof filters!=="undefined" && typeof filters[filter]!="undefined" ){
//              
//                if (selectedFilters.indexOf(filters[filter]) !== -1  && filters[filter]!=="") {
//                    html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "' checked value='" + filters[filter] + "' name='" + viewIds[0] + "'/>";
//                } else {
//                  
//                           if(filters[filter].trim()===""){
//                               
//                      
//                    var empty = "empty";
//                    html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "'  value='" + empty + "' name='" + viewIds[0] + "'/>";
//                                }else{
//                              html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace(/\s/g, '') + "_" + filter + "'  value='" + filters[filter] + "' name='" + viewIds[0] + "'/>";      
//                                }
//                }
//                html += "<span class='box gFontFamily gFontSize12'>&nbsp;" + filters[filter] + "</span></span></label></td></tr>";
//            }
//        }
//        html += "</table></div>";
//    }
//    html += "<table><tr><td><input type='button' value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onClick='saveFilters(\"" + chartId + "\")' /></td>";
//
//    html +="</tr></table>";
//    parent.$("#tempDashletDiv").html(html);
//    parent.$("#tempDashletDiv").dialog('open');
//}


// Added by Prabal For Local filter data came from scroll
//
//function localFilterUI(chartId) {
//    $("#driver").val("");
//    var chartData = JSON.parse($("#chartData").val());
//    var chartDetails = chartData[chartId];
//alert(JSON.stringify(chartDetails))
//    var viewIds = chartDetails["viewIds"];
//    if(typeof chartData[chartId]["dimensions"]!=="undefined" && chartData[chartId]["dimensions"]!==""){
//        viewIds = chartData[chartId]["dimensions"];
//    }
//    alert("viewIds"+JSON.stringify(viewIds))
//    var divHeight = ($(window).height() - $(window).height() / 6);
//    var divWidth = ($(window).width() - ($(window).width() / 1.4));
//    parent.$("#tempDashletDiv").dialog({
//        autoOpen: false,
//        height: divHeight,
//        width: divWidth,
//        position: 'justify',
//        modal: true,
//        title: translateLanguage.Edit_Filter+':'  //+ chartDetails["Name"]
//    });
//    if(filterData=="undefined"){
//        filterData={};
//    }
//alert(JSON.stringify(filterData))
////    var filterKeys = Object.keys(filterData);
//    var filterKeys = Object.keys(viewIds);
//    var filterMap = {};
//    if (typeof chartDetails["filters"] !== "undefined") {
//        filterMap = chartDetails["filters"];
//    }
//    var html = "";
//    var filterKeys = chartData[chartId]["dimensions"];
//    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
//        filterKeys=chartData[chartId]["viewBys"];
//    }else{
//        var viewOvName = JSON.parse(parent.$("#viewby").val());
//        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
////         alert("viewOvName="+viewOvName)
////         alert("viewOvIds="+viewOvIds)
//        var temp = filterKeys;
////         alert("temp="+temp)
//        filterKeys=[];
//        for (var key in temp) {
//            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
////            filterKeys.push(viewOvName[key]);
//        }
//    }
//  
//// alert("viewOvName[viewOvIds.indexOf(temp[key])]   =    "+JSON.stringify(viewOvName[viewOvIds.indexOf(temp[key])]));
//// alert("filterKeys   =    "+JSON.stringify(filterKeys));
//// alert("filterMap   =    "+JSON.stringify(filterMap));
//    for (var key in filterKeys) {
//      
//        var filterGroupBy = filterKeys[key];
////        var filterGroupBy = viewOvName[key];
////         alert("filterGroupBy="+filterGroupBy)
//         var indexVal=viewOvName.indexOf(filterGroupBy);
////        var viewid = viewOvIds[key];
//        var viewid = viewOvIds[indexVal];
////         alert("viewid="+viewid +" of "+filterGroupBy +"  index="+indexVal+"      id="+viewOvIds[indexVal])
//        var filters = filterData[filterGroupBy];
//        var selectedFilters = [];
//        if (typeof filterMap[viewid] !== "undefined") {
//            selectedFilters = filterMap[viewid];
//        }
//        
////         alert("selectedFilters   =    "+JSON.stringify(selectedFilters));
//        if(key==0){
//            html += "<table style='' width='100%'><tr><td class='gFontFamily gFontSize12' >";
//            if(chartData[chartId]["globalEnable"]!=="undefined" && chartData[chartId]["globalEnable"]==="N"){
//                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' >";
//            }else{
//                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' checked>";
//            }
//            html += "</td></tr></table>";
//        }
////        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv1(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
//        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='lovfiltersreptable(\""+filterGroupBy.replace(/\s/g, '')+"\",\""+viewid+"\",\""+'localFilter'+"\",\""+'graph'+"\",\""+chartId+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
//        //        html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
////alert(selectedFilters+"            filterGroupBy="+filterGroupBy);
//        if (selectedFilters.length === 0) {
//            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "'   onclick='selectAllLocalFilterChecks(event,this.id,\""+filterGroupBy.replace(" ","")+"\")'/>";
//        } else {
//            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='" + filters.length + "*," + filterGroupBy + "' checked  onclick='selectAllLocalFilterChecks(event,this.id,\""+filterGroupBy.replace(" ","")+"\")' />";
//        }
//        html += "<span class='gFontFamily gFontSize12'>&nbsp;" + filterGroupBy + "</span></label></div>";
//        html += "</div>";
//        html += "<div id='localFilter"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none;height:100px;background-color:#d2d2d2;'></div>";
//                }
////    html += "<table><tr><td><input type='button' value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onClick='saveFilters(\"" + chartId + "\")' /></td>";
// html +="<div style='position: absolute; bottom: 10px; text-align: center; margin-left: 45%;'><input value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onclick='saveFilters1(\"" + chartId + "\")'  type='button'></div>";
////    html +="</tr></table>";
//    parent.$("#tempDashletDiv").html(html);
//    parent.$("#tempDashletDiv").dialog('open');
//            
//            }            
//  



function localFilterUI(chartId) {
    $("#driver").val("");
    var chartData = JSON.parse($("#chartData").val());
    var chartDetails = chartData[chartId];
//    var viewIds = chartDetails["viewIds"];
//    if(typeof chartData[chartId]["dimensions"]!=="undefined" && chartData[chartId]["dimensions"]!==""){
//        viewIds = chartData[chartId]["dimensions"];
//    }
      var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
    var divHeight = ($(window).height() - $(window).height() / 6);
    var divWidth = ($(window).width() - ($(window).width() / 1.4));
    parent.$("#tempDashletDiv").dialog({
        autoOpen: false,
        height: divHeight,
        width: divWidth,
        position: 'justify',
        modal: true,
        title: translateLanguage.Edit_Filter+':'  //+ chartDetails["Name"]
    });
    
//alert(filterData)
filterData= {};
    var filterKeys = Object.keys(filterData);
    var filterMap = {};
    if (typeof chartDetails["filters"] !== "undefined") {
        filterMap = chartDetails["filters"];
    }
    var html = "";
    var filterKeys = chartData[chartId]["dimensions"];
//    alert(JSON.stringify(chartData[chartId]["dimensions"]))
//    if(typeof chartData[chartId]["dimensions"]==="undefined" || chartData[chartId]["dimensions"]===""){
//        filterKeys=chartData[chartId]["viewIds"];
//    }else{
//      
////         alert("viewOvName="+viewOvName)
////         alert("viewOvIds="+viewOvIds)
//        var temp = filterKeys;
//         alert("temp="+temp)
//        filterKeys=[];
//        for (var key in temp) {
//            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
////            filterKeys.push(viewOvName[key]);
//        }
//    }
// alert("viewOvName[viewOvIds.indexOf(temp[key])]   =    "+JSON.stringify(viewOvName[viewOvIds.indexOf(temp[key])]));
// alert("filterKeys   =    "+JSON.stringify(filterKeys));
// alert("filterMap   =    "+JSON.stringify(filterMap));
    for (var key in filterKeys) {
      
        var filterGroupBy = filterKeys[key];
//        var filterGroupBy = viewOvName[key];
//         alert("filterGroupBy="+filterGroupBy)
         var indexVal=viewOvName[viewOvIds.indexOf(filterGroupBy)];
         filterGroupBy = indexVal;
//        var viewid = viewOvIds[key];
        var viewid = filterKeys[key];
//         alert("viewid="+viewid +" of "+filterGroupBy +"  index="+indexVal+"      id="+viewOvIds[indexVal])
//        var filters = filterData[filterGroupBy];
        var selectedFilters = [];
        if (typeof filterMap[viewid] !== "undefined") {
            selectedFilters = filterMap[viewid];
        }
        
//         alert("selectedFilters   =    "+JSON.stringify(selectedFilters));
        if(key===0){
            html += "<table style='' width='100%'><tr><td class='gFontFamily gFontSize12' >";
            if(chartData[chartId]["globalEnable"]!=="undefined" && chartData[chartId]["globalEnable"]==="N"){
                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' >";
            }else{
                html +=""+translateLanguage.Enable_GlobalFilter+"</td><td> <input type='checkbox' id='"+chartId+"_globalfilter' checked>";
            }
            html += "</td></tr></table>";
        }
//        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDiv1(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
        html += "<div class='expandDiv1"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='lovfiltersreptable(\""+filterGroupBy.replace(/\s/g, '')+"\",\""+viewid+"\",\""+'localFilter'+"\",\""+'graph'+"\",\""+chartId+"\")' style='border-bottom: 1px solid lightgrey;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:2px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
        //        html += "<div width='100%' style='background-color:lightgrey;padding-bottom:20px;'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";
//alert(selectedFilters+"            filterGroupBy="+filterGroupBy);
        if (selectedFilters.length === 0) {
            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='*," + filterGroupBy + "'   onclick='selectAllLocalFilterChecks(event,this.id,\""+filterGroupBy.replace(" ","")+"\")'/>";
        } else {
            html += "<input type='checkbox' class='ckbox' id='L_" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "' name='*," + filterGroupBy + "' checked  onclick='selectAllLocalFilterChecks(event,this.id,\""+filterGroupBy.replace(" ","")+"\")' />";
        }
        html += "<span class='gFontFamily gFontSize12'>&nbsp;" + filterGroupBy + "</span></label></div>";
        html += "</div>";
        html += "<div id='localFilter"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none;height:100px;background-color:#d2d2d2;'></div>";
                }
//    html += "<table><tr><td><input type='button' value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onClick='saveFilters(\"" + chartId + "\")' /></td>";
 html +="<div style='position: absolute; bottom: 10px; text-align: center; margin-left: 45%;'><input value='"+translateLanguage.Done+"' class='gFontFamily gFontSize12' onclick='saveFilters1(\"" + chartId + "\")'  type='button'></div>";
//    html +="</tr></table>";
    parent.$("#tempDashletDiv").html(html);
    parent.$("#tempDashletDiv").dialog('open');
            
            }           
  



function selectAllLocalFilterChecks(event,id,elementName){
    event.stopPropagation();
    //    alert("call   "+id+"     elementName="+elementName);
    if($("#"+id).is(':checked'))
    {
        $(".selectclass"+elementName).attr("checked",true); 
    }else{
        $(".selectclass"+elementName).attr("checked",false); 
    }
}



function selectAllLocal(id,name){
    $("#driver").val("");
    var filterKeys = Object.keys(filterData);
    //    for (var key in filterKeys) {
    var filterGroupBy = name.split("*,")[1].replace("L_","","gi");
    var filters = filterData[filterGroupBy];
    var filterMap = {};
    var filterValues=[];
    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        filterMap = JSON.parse($("#filters1").val());
        if(typeof filterMap[id]!=="undefined"){
            filterValues=filterMap[id];
        }
    }
    for (var filter in filters) {
        if(fromoneview!='null'&& fromoneview=='true'){
            if(!document.getElementById(id).checked){
                $("#"+ id+"_"+filter).prop('checked', false);
            }
            else{
                $("#" +id+"_"+filter).prop('checked', true);
            }
        }else{
            if(!parent.document.getElementById(id).checked){
                $("#"+ id+"_"+filter).prop('checked', false);
            }
            else{
                $("#" +id+"_"+filter).prop('checked', true);
            }
        }
    }
}

function selectAll(id,name){
    $("#driver").val("");



// for (var key in filterKeys) {

    var keyvalMain = name.split("*,")[1];
    var view = JSON.parse($("#viewby").val());
    var viewIds = JSON.parse($("#viewbyIds").val());
    var index = view.indexOf(keyvalMain);
    if(index==-1){
        index=view.indexOf(" "+keyvalMain)
    }
    var keyval = viewIds[index];
    var filterGroupBy = keyvalMain;
    var filters = filterData[filterGroupBy];
    var filterMap = {};
    var filterValues=[];
    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        filterMap = JSON.parse($("#filters1").val());
        if(typeof filterMap[keyval]!=="undefined"){
            filterValues=filterMap[keyval];
        }
    }
  
    for (var filter in filters) {
        if(!parent.document.getElementById(id).checked){
            $("#" + id+"_"+filter).prop('checked', false);
        }
        else{
            $("#" + id+"_"+filter).prop('checked', true);
        }
    }
    for (var filter in filters) {
        if(!parent.document.getElementById(id).checked){
            if(filterData[keyvalMain][filter]!=""){
                filterValues.push(filterData[keyvalMain][filter]);
            }
            filterMap[keyval] = filterValues;
        }
        else{
            var index = filterValues.indexOf(filterData[keyvalMain][filter]);
            filterValues.splice(index, 1);
        }
    }
   
    if(parent.document.getElementById(id).checked){
        delete filterMap[keyval];
    }
   
//    if(Object.keys(filterMap).length==0){
//        $("#filters1").val("");
//    }else{
    $("#filters1").val(JSON.stringify(filterMap));
//    }
//
//    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
//        function(data){
//            var chartData = JSON.parse($("#chartData").val());
//            if(parent.$("#type").val()==="advance"){
//                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
//            }else{
//                var resultset = data;
////                  var chartFlag = "false";
////                 var GTdiv = [];
////                 for(var t in chartData){
////                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
////                 chartFlag = "true"
////                 GTdiv.push(t)
////        }
////}
////            if(chartFlag!="false"){
////            var GTData = JSON.parse(data)
////            for(var k in GTdiv){
////            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
////            }
////            parent.$("#chartData").val(JSON.stringify(chartData));
////            }
//    var data = JSON.parse(resultset)["data"];
//               var chartData = JSON.parse($("#chartData").val());
//                var chartFlag = "false";
//                var GTdiv = [];
//                for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//                        chartFlag = "true"
////                 GTdiv.push(t)
//                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
//                    }
//                }
//                //                generateChart(data);
//                generateChart1(data);   // by manik
//            }
//        });

    //add by mayank sh. for filter (8/5/15)
    var htmlstr= "";
    var value='';
    var flag='black';
    var isFilter = parent.$("#filters1").val();
    if (isFilter==""){
        flag='black';
        $("#filterFlag").html("");
        $("#filterFlag").html("<img float='right' src='images/filter.png' onclick='showDragBox()' ></img>");
        $("#filterIcon").html("<img float='right' src='images/filter.png' onclick='showHideFilter()' ></img>");
    }
    else{
        var isFilter1 = JSON.parse(parent.$("#filters1").val());
        var key1=Object.keys(isFilter1);
        for (var key in key1) {
            value=isFilter1[key1[key]];
            if(value !="")
            {
                flag='red';
                $("#filterFlag").html("");
                $("#filterFlag").html("<img float='right' src='images/filter_red.png' onclick='showDragBox()' ></img>");
                $("#filterIcon").html("<img float='right' src='images/filter_red.png' onclick='showHideFilter()' ></img>");
                break;
            }
            // alert(""+value);
            for(var val in value){
                htmlstr+="<span>"+value[val]+",</span>";
            }
        }
        if(flag=='black'){
            $("#filterFlag").html("");
            $("#filterFlag").html("<img float='right' src='images/filter.png' onclick='showDragBox()' ></img>");
            $("#filterIcon").html("<img float='right' src='images/filter.png' onclick='showHideFilter()' ></img>");
        }
    }
    $("#applied_filter").html(htmlstr);
}
//end by mayank sh.(9/6/15)


var localfilterMap = {};
function saveFiltersoneview(chartId,repId,oneviewid,chartname,reportName,regid) {
    //    if(fromoneview!='null'&& fromoneview=='true'){
    filterData= oneiewfilterData;
    $("#tempDashletDiv1").dialog('close');
    oneiewfilterData=JSON.parse(parent.$("#filterdata1").val());
    filterData=JSON.parse(parent.$("#filterdata1").val());
    //    }
    parent.$("#tempDashletDiv").dialog('close');
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    var viewIds = chartDetails["viewIds"];
    var filterMap = {};
    var filterKeys = chartData[chartId]["dimensions"];
    var temp;
    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
        filterKeys=chartData[chartId]["viewBys"];
    }
    //if(fromoneview!='null'&& fromoneview=='true'){
    filterKeys=chartData[chartId]["viewBys"];
    // }
    for (var key in filterKeys) {
        var filterGroupBy = filterKeys[key];
        var filters;
        //        if(fromoneview!='null'&& fromoneview=='true'){
        filters = oneiewfilterData[filterGroupBy];
        //    }else{
        //        filters = filterData[filterGroupBy];
        //    }
        for (var filter in filters) {
            if(filter !=="0")
                if (document.getElementById("L_1"+filterGroupBy.replace(/\s/g, '') + "_" + filter).checked) {
                    if (typeof filterMap[viewIds[0]] === "undefined") {
                        filterMap[viewIds[0]] = [];
                    }
                    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
                        filterMap[viewIds[0]].push(filters[filter]);
                    }else{
                        if (typeof filterMap[chartData[chartId]["dimensions"][key]] === "undefined") {
                            filterMap[chartData[chartId]["dimensions"][key]] = [];
                        }
                        filterMap[chartData[chartId]["dimensions"][key]].push(filters[filter]);
                    }
                }
        }
    }
    chartDetails["filters"] = filterMap;
    localfilterMap=JSON.stringify(filterMap);
    chartData[chartId] = chartDetails;
    parent.$("#chartname").val(chartId)
    // parent.$("#localfilterMap").val(chartId)
    var others = "";
    if($("#"+chartId+"_othersL").prop("checked")){
        others="Y";
    }else{
        others="N";
    }
    var globalfilter = "";
    if($("#"+chartId+"_globalfilter").prop("checked")){
        globalfilter="Y";
    }else{
        globalfilter="N";
    }
    chartData[chartId]["globalEnable"] = globalfilter;
    chartData[chartId]["othersL"] = others;
    parent.$("#chartData").val(JSON.stringify(chartData));
    //       if(fromoneview!='null'&& fromoneview=='true'){
    $.ajax({
        async:false,
        type:"POST",
        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+repId+"&reportName="+reportName+'&chartname='+chartId,

        url: parent.ctxpath+"/reportViewer.do?reportBy=drillCharts&fromoneview=true&action=localfilter&oneviewID="+oneviewid+"&regid="+regid,

        success: function(data){
            parent.$("#chartname").val(chartId)
            action1="localfilter"
            graphData=JSON.parse(data);
            var divid="chart"+regid;
            fromoneview='true';
            divcount=regid;
            generateSingleChart(data,divid);
            divcount="undefined";
            viewid=[];
            filternames=[];
        //generateChart(data);
        }
    });


}

function saveFilters(chartId) {
    parent.$("#tempDashletDiv").dialog('close');
    var chartData = JSON.parse(parent.$("#chartData").val());
    var chartDetails = chartData[chartId];
    var viewIds = chartDetails["viewIds"];
    var filterMap = {};
    var filterKeys = chartData[chartId]["dimensions"];
    var temp;
    if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
        filterKeys=chartData[chartId]["viewBys"];
    }else{
        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        temp = filterKeys;
        filterKeys=[];
        for (var key in temp) {

            filterKeys.push(viewOvName[viewOvIds.indexOf(temp[key])]);
        }
    }

    for (var key in filterKeys) {
        var filterGroupBy = filterKeys[key];
        var filters = filterData[filterGroupBy];
        //Added By Ram
        for(var f=0;f<filters.length;f++)
            {
            if(filters[f] in parent.lookupdataMap){
                         filters[f]=parent.lookupdataMap[filters[f]];
            }}
        //Ended by Ram
        for (var filter in filters) {
       
//                    if(filter !=="0")
            if (parent.document.getElementById("L_"+filterGroupBy.replace(/\s/g, '') + "_" + filter).checked) {
                if (typeof filterMap[viewIds[0]] === "undefined") {
                    filterMap[viewIds[0]] = [];
                }
                if(typeof chartData[chartId]["dimensions"]=="undefined" || chartData[chartId]["dimensions"]==""){
                    filterMap[viewIds[0]].push(filters[filter]);
                }else{
                    if (typeof filterMap[chartData[chartId]["dimensions"][key]] === "undefined") {
                        filterMap[chartData[chartId]["dimensions"][key]] = [];
                    }
                    filterMap[chartData[chartId]["dimensions"][key]].push(filters[filter]);
                }
            }
        }
    }
    chartDetails["filters"] = filterMap;
   // alert(JSON.stringify(filterMap));
    //alert(Object.keys(filterMap).length);
   // alert(chartData[chartId]["filterMap"]);
    if(Object.keys(filterMap).length==0){
       $("#newLocalFilter"+chartId).hide();
    }else{
         $("#newLocalFilter"+chartId).show();
    }
        
    chartData[chartId] = chartDetails;

    var others = "";
    if($("#"+chartId+"_othersL").prop("checked")){
        others="Y";
    }else{
        others="N";
    }
    var globalfilter = "";
    if($("#"+chartId+"_globalfilter").prop("checked")){
        globalfilter="Y";
    }else{
        globalfilter="N";
    }
    chartData[chartId]["othersL"] = others;
    chartData[chartId]["globalEnable"] = globalfilter;
   
    parent.$("#chartData").val(JSON.stringify(chartData));
    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
        function(data){
            var resultset = data

//                    var chartFlag = "false";
//                 var GTdiv = [];
//                 for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//                 chartFlag = "true"
//                 GTdiv.push(t)
//        }
//}
//            if(chartFlag!="false"){
//            var GTData = JSON.parse(data)
//            for(var k in GTdiv){
//            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
//            }
//            parent.$("#chartData").val(JSON.stringify(chartData));
//            }

         var data = JSON.parse(resultset)["data"];

               var chartData = JSON.parse($("#chartData").val());
            var chartFlag = "false";
            var GTdiv = [];
            for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash"  ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Standard-KPI1" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                    chartFlag = "true"
//                 GTdiv.push(t)

                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                }
            }

            generateSingleChart(resultset,chartId);
        });
     var htmlString = $( "#appliedFilters" ).html();

     /* Added by Ashutosh
      *For show or hide filter icon*/
     try{
     if(true){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var filters=chartData[chartId]["filters"];
    var filterKeys=Object.keys(filters);
    var viewBys=JSON.parse(parent.$("#viewby").val());
    var viewByIds=JSON.parse(parent.$("#viewbyIds").val());
    for(var i in filterKeys){
        var filterGroupBy=viewBys[viewByIds.indexOf(filterKeys[i])];
        var filters1 = filterData[filterGroupBy];
        var filterValues=filters[filterKeys[i]]
        var inFilters=$(filters1).not(filterValues).get();
//        alert(inFilters.length);
        if(inFilters.length > 0){
            $("#localfilter_red"+chartId).css({
                "display":""
            });
        }else{
            $("#localfilter_red"+chartId).css({
                "display":"none"
            });
}
    }
    }
     }catch(err) {}
}

//sandeep
function ApplyfilterGO(){
    var chartData=JSON.parse(parent.$("#chartData").val());
    var noOfCharts=Object.keys(chartData).length;
    //yaxisrange":{"axisMin":"","axisMax":"","axisTicks":"","YaxisRangeType":"MinMax"}
    var yAxisRangeMap={};
    yAxisRangeMap["axisMin"]="";
    yAxisRangeMap["axisMax"]="";
    yAxisRangeMap["axisTicks"]="";
    yAxisRangeMap["YaxisRangeType"]="MinMax";
    for(var i=0;i<noOfCharts;i++){
        chartData["chart"+(i+1)]["yaxisrange"]=yAxisRangeMap;
    }
    var y1AxisRangeMap={};  // add by mayank sharma
    y1AxisRangeMap["axisMin"]="";
    y1AxisRangeMap["axisMax"]="";
    y1AxisRangeMap["axisTicks"]="";
    y1AxisRangeMap["Y1axisRangeType"]="MinMax";
    for(var i=0;i<noOfCharts;i++){
        chartData["chart"+(i+1)]["y1axisrange"]=y1AxisRangeMap;
    }
     var charts = Object.keys(chartData)
  var viewGBIds = JSON.parse(parent.$("#viewbyIds").val());
    var viewGBName = JSON.parse(parent.$("#viewby").val());
    var viewbyFlag = [];
    var viewByIdFlag = [];
    var dimension  = [];
    var flag = "";
 for(var no in charts ){

    if(chartData[charts[no]]["chartType"]!="undefined" && chartData[charts[no]]["chartType"]=="world-map"){
if(typeof $("#driverList").val()!="undefined" && $("#driverList").val()!=""){
         var  driverList = JSON.parse($("#driverList").val());
         if(driverList.indexOf(charts[no])!=-1){
              if(chartData[charts[no]]["dimensions"][1]!="undefined" && chartData[charts[no]]["dimensions"][1]!=""){
                  viewByIdFlag.push(chartData[charts[no]]["dimensions"][1]);
                  for(var i in viewGBIds){
if(viewGBIds[i]==chartData[charts[no]]["dimensions"][1]){
viewbyFlag.push(viewGBName[i]);
dimension.push(chartData[charts[no]]["dimensions"][1])
}
}
dimension.push(chartData[divID]["dimensions"][0]);
chartData[divID]["viewIds"] =viewByIdFlag;
chartData[divID]["viewBys"] =viewbyFlag;       
chartData[divID]["dimensions"]=dimension;
flag = 'viewByChange';
              }
         }
         
        }
//alert(chartData[charts[no]]["mapdrill"]+"::"+chartData[charts[no]]["mapMultiLevelDrill"])
   // chartData[charts[no]]["mapdrill"]="No";
	//chartData[charts[no]]["mapMultiLevelDrill"]="No";  
     // $("#drills").val("");

    }   
   }
   setnotinfilters();
    parent.$("#chartData").val(JSON.stringify(chartData));
     parent.$("#drillFormat").val(''); 
   var keys = Object.keys(parent.filterMapNew)
       var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        var sizeg=viewOvName.length;var flagg=false;
          for(var k=0;k<sizeg;k++){
              if(k>=6){
                var   selectedlist=parent.filterMapNew[viewOvIds[k]];
 if(selectedlist==undefined || selectedlist==""){
                  
 }else{
   //  flagg=true
 }
              }
          }
           for(var k=0;k<sizeg;k++){
              if(k>=6){
                
                  var selectedlistnotin=parent.filterMapNotinDB[viewOvIds[k]];
 if(selectedlistnotin==undefined || selectedlistnotin==""){
                  
 }else{
     flagg=true;
 }
              }
          }
          if(flagg){
               parent.$("#more").css("color","orange");
//              parent.$("#more").hide();
              parent.$("#moread").hide();
//              parent.$("#greenfilter").show();
              parent.$("#greenfilterad").show();
          }else{
              parent.$("#more").css("color","#888");
//              parent.$("#more").show();
              parent.$("#moread").show();
//              parent.$("#greenfilter").hide();
              parent.$("#greenfilterad").hide();
          }
  
        for(var i in keys){
            var elemntid=keys[i];
            var selectedFilters1 = [];
            selectedFilters1=parent.filterMapNew[elemntid];
             //Added by Ram
         if(selectedFilters1.length>=1){
                    for(var k=0;k<selectedFilters1.length;k++){
                  if(selectedFilters1[k] in parent.lookupdataMap){
                     selectedFilters1[k]=parent.lookupdataMap[selectedFilters1[k]]
                                  }
                    }}
      //Ended by Ram
            parent.filterMapNewtb[elemntid]=selectedFilters1
        }
          var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        var filterMap1 = JSON.parse(parent.$("#filters1").val());
          if(typeof filterMap1 !=="undefined"){
          var drillKeys = Object.keys(filterMap1);
         
           for(var key in drillKeys){
              var quickViewname = viewOvName[viewOvIds.indexOf(drillKeys[key])];
            
             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time"); 
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time"); 
             }else {
                parent.$("#drillFormat").val("none");  
             }
             }
             }
             }
     $("#loading").show();
      $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&action=localfilterGraphs", $("#graphForm").serialize(),
        function(data){
               var chartData = JSON.parse($("#chartData").val());
               var resultset = data;

            if(parent.$("#type").val()==="advance"){

                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
            }else{
                 var data = JSON.parse(resultset)["data"];
               var chartData = JSON.parse($("#chartData").val());
                var chartFlag = "false";
                var GTdiv = [];
                for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI"  ||chartData[t]["chartType"]=="Standard-KPI1" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                        chartFlag = "true"
//                 GTdiv.push(t)

                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                    }
                }
                updatefiltersrow();
                 runtimeglobalfilters(JSON.stringify(filterMap1),'null',"graph");
                generateChart(data);
//                                generateChart1(data);     // added by manik
            }
        });
         parent.isfilteraplied=true;
}
var savedfilter=true
function applyfiltersgraph(){
 var keys = Object.keys(parent.filterMapNewtb);
 var notinKeys = Object.keys(parent.filterMapNotin);
 
  var filterMap = {};
     var selectedlist;var selectedlistnotin;
      
    savedfilter=true
      var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        var sizeg=viewOvName.length;var flagg=false;
          for(var k=0;k<sizeg;k++){
              if(k>=6){
                  var selectedlist1=parent.filterMapNewtb[viewOvIds[k]];
                  var selectedlistnotin=parent.filterMapNotinDB[viewOvIds[k]];
 if(selectedlist1==undefined || selectedlist1=="" || selectedlistnotin==undefined || selectedlistnotin==""){
                  
 }else{
     //flagg=true
 }
              }
          }
          if(flagg){
               parent.$("#more").css("color","orange");
//              parent.$("#more").hide();
              parent.$("#moread").hide();
//              parent.$("#greenfilter").show();
              parent.$("#greenfilterad").show();
          }else{
              parent.$("#more").css("color","#888");
//              parent.$("#more").show();
              parent.$("#moread").show();
//              parent.$("#greenfilter").hide();
              parent.$("#greenfilterad").hide();
          }
                    for(var k in keys){
 selectedlist=parent.filterMapNewtb[keys[k]];
 if(selectedlist==undefined || selectedlist==""){

 }else{savedfilter=false
       for (var key in viewOvIds) {
            var viewbyid=viewOvIds[key].replace("A_", "");
           var filterValues=[];
            if(viewbyid==keys[k]){
               var filterValuesgr=[];
               for(var k=0;k<selectedlist.length;k++){
               if(selectedlist.length>=1 && selectedlist[0]!="All"){
                  
filterValues.push(selectedlist[k]);
//  var index = filterValues.indexOf(selectedlist[k]);
//        filterValues.splice(index, 1);
  filterMap[viewbyid] = filterValues;

filterValuesgr.push(selectedlist[k]);
                                                parent.filterMapgraphs[viewbyid]=  filterValuesgr;
      $("#filters1").val(JSON.stringify(filterMap));
    
               }
            }
            break;
            }
       }
    
parent.$("#multigblrefresh").val("reset");
                    }
}
if(savedfilter){

}
for(var k in keys){
 selectedlistnotin=parent.filterMapNotin[keys[k]];
 if(selectedlistnotin==undefined || selectedlistnotin==""){

 }else{
       for (var key in viewOvIds) {
            var viewbyid=viewOvIds[key].replace("A_", "");
           var filterValues=[];
            if(viewbyid==keys[k]){
               var filterValuesgr=[];
               for(var k=0;k<selectedlistnotin.length;k++){
               if(selectedlistnotin.length>=1 && selectedlistnotin[0]!="All"){
               
filterValues.push(selectedlistnotin[k]);
  
         parent.filterMapNotinDB[viewbyid] = filterValues;
         
          $("#notfilters").val(JSON.stringify(parent.filterMapNotinDB));
    
}
            }
            break;
            }
       }
    
parent.$("#multigblrefresh").val("reset");
                    }
}
}
//function setallfilters(elname,elmentid,flag,type){
//     var filterValues=[];
//       var filterValues1=[];
//     var filterMap = {};
//       elname=elname.replace("1q1", " ");
//      if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
//        filterMap = JSON.parse($("#filters1").val());
////        elname=elname.replace("1q1", " ");
////if(typeof filterMap[elmentid]!=="undefined"){
////            filterValues=filterMap[elmentid];
////        }
//    }
// elname=elname.replace("1q1", " ");
//  var filters=filterData[elname];
//filterValues1=filterData[elname];
////      filterValues.push(filterData[elname]);
//for (var filter in filters) {
//    var index= filterValues1.indexOf(filter);
//        if(flag!=null && flag=='uncheckall'){
////            var chekid;
////            if(type=="advance"){
////
////            }else if(type=="trend"){
////
////            }else if(type=="table"){
////
////            }else{
////                chekid="ui-multiselect-"+elname+"-option-"+index
////            }
////              if(!document.getElementById(chekid).checked){
//            if(filterData[elname][filter]!=""){
//                filterValues.push(filterData[elname][filter].replace("'", "''"));
//            }
////              }
////            filterMap[elmentid] = filterValues;
//            checkall=false
//             unceckall=true
//        }
// else{
//            var index = filterValues.indexOf(filterData[elname][filter]);
//            filterValues.splice(index, 1);
//            checkall=true
//        }
//
//    }
// var filterValuesgr=[];
//
//                                                parent.filterMapgraphs[elmentid]=  filterValuesgr;
//        filterMap[elmentid] = filterValues;
////        delete filterMap[elmentid];
//         $("#filters1").val(JSON.stringify(filterMap));
//}

//function applyGlobalFilterinreport(id,nameArr,chekid){
//  var filterMap1 = {};
//   var filterValues=[];
//    //        var filterKeys = Object.keys(filterData);
//
////    var filterValues=[];
//    var name ,name1;
//
////    $("#filters1").val(JSON.stringify(filterMap))
//    name = nameArr.split("*,")[0];
//     name=name.replace("1q1", " ");
//
//    name1 = nameArr.split("*,")[1];
//    var view = parent.$("#globalviewby").val();
//    var viewIds = parent.$("#globalviewbyIds").val();
////    var index = view.indexOf(name);alert(index)
////    if(index==-1){
////        index = view.indexOf(" "+name);
////    }
////    name = viewIds[1];
//
////alert(JSON.stringify(filterData))
//name=name.replace("1q1", " ");
//    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
//        filterMap1 = JSON.parse($("#filters1").val());
//        if(typeof filterMap1[name1]!=="undefined"){
//            filterValues=filterMap1[name1];
//        }
//    }
//var filterValuestb=[];
//                                               filterValuestb=parent.filterMapNew[name1];
//    if(!document.getElementById(chekid).checked){
//        filterValues.push(filterData[name][id.split("_")[1]]);
//        filterMap1[name1] = filterValues;
//        var filterValuesgr=[];
//                                                 filterValuesgr=parent.filterMapgraphs[name1];
//                                                  if(filterValuesgr==undefined || filterValuesgr==""){
//                                                    for(var j=0;j<globalfiltvalues.length-1;j++)
//        {
//            filterValuesgr.push(globalfiltvalues[j]);
//        }
//
//                                                 }
//                                                  var index = filterValuesgr.indexOf(filterData[name][id.split("_")[1]]);
//        filterValuesgr.splice(index, 1);
//                                                parent.filterMapgraphs[name1]=  filterValuesgr;
//                                                  if(filterValuestb==undefined || filterValuestb==""){
//
//                                                  }else{
//                                                       var index = filterValuestb.indexOf(filterData[name][id.split("_")[1]]);
//        filterValuestb.splice(index, 1);
//    }
//                                                  $("#CBOARP"+name1).val(JSON.stringify( parent.filterMapgraphs[name1]));
//    }
//    else{
//        var flag=true;
//        var index = filterValues.indexOf(filterData[name][id.split("_")[1]]);
//        parent.filterMapgraphs[name1].push(filterData[name][id.split("_")[1]]);
//        filterValues.splice(index, 1);
//
//                                                if(filterValuestb==undefined || filterValuestb==""){
//  parent.filterMapNew[name1].push(filterData[name][id.split("_")[1]]);
//  parent.filterMapNewtb=parent.filterMapNew;
//                                                }else{
//  for(var k=0;k<filterValuestb.length;k++){
//               if(filterValuestb.length>=1 && filterValuestb[0]!="All"){
//if(filterValuestb[k]==filterData[name][id.split("_")[1]]){
//    flag=false;
//    }
//               }
//  }
//  if(flag){
//       parent.filterMapNew[name1].push(filterData[name][id.split("_")[1]]);
//       parent.filterMapNewtb=parent.filterMapNew;
//          $("#CBOARP"+name1).val(JSON.stringify( parent.filterMapgraphs[name1]));
//  }
//
//                                                }
//    }
//
//  $("#filters1").val(JSON.stringify(filterMap1));
//// alert($("#filters1").val())
//}
//sandeep for miltiselet filters go of ie9
function applyfilterIE(){
    $("#loading").show();

$.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
        function(resultset){
            if(parent.$("#type").val()==="advance"){
                generateVisual(JSON.parse(resultset),JSON.parse(parent.$("#visualChartType").val()));
            }else{

//                  var chartFlag = "false";
//                 var GTdiv = [];
//                 for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//                 chartFlag = "true"
//                 GTdiv.push(t)
//        }
//}
//            if(chartFlag!="false"){
//            var GTData = JSON.parse(data)
//            for(var k in GTdiv){
//            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
//            }
//            parent.$("#chartData").val(JSON.stringify(chartData));
//            }
               var data = JSON.parse(resultset)["data"];
               var chartData = JSON.parse($("#chartData").val());
                var chartFlag = "false";
                var GTdiv = [];
                for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Standard-KPI1" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                        chartFlag = "true"
//                 GTdiv.push(t)

                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
                    }
                }
                generateChart(data);
            //                                generateChart1(data);     // added by manik
            }
        });
}
function applyFilter(id,nameArr){
//    $("#loading").show();
    var filterMap = {};
    var chartData = JSON.parse($("#chartData").val());
    //        var filterKeys = Object.keys(filterData);
    var filterMap = {};
    var filterValues=[];
    var name ,name1;
    name = nameArr.split("*,")[0];
    var view = JSON.parse($("#viewby").val());
    var viewIds = JSON.parse($("#viewbyIds").val());
    var index = view.indexOf(name);
    if(index==-1){
        index = view.indexOf(" "+name);
    }
    name1 = viewIds[index];
    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        filterMap = JSON.parse($("#filters1").val());
       if(typeof filterMap[name1]!=="undefined"){
            filterValues=filterMap[name1];
        }
    }


    if(!parent.document.getElementById(id).checked){
        filterValues.push(filterData[name][id.split("_")[1]])
        filterMap[name1] = filterValues;
    }
    else{
        var index = filterValues.indexOf(filterData[name][id.split("_")[1]]);
        filterValues.splice(index, 1);
    }
    if(typeof filterMap != "undefined" && filterMap !=""){
        globalFilter = filterMap
        $("#filters1").val(JSON.stringify(globalFilter));
    }else {
        $("#filters1").val(JSON.stringify(filterMap));
    }
    var charts = Object.keys(chartData);
   
    
//    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
//        function(resultset){
//            if(parent.$("#type").val()==="advance"){
//                generateVisual(JSON.parse(resultset),JSON.parse(parent.$("#visualChartType").val()));
//            }else{
//
////                  var chartFlag = "false";
////                 var GTdiv = [];
////                 for(var t in chartData){
////                 if(chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
////                 chartFlag = "true"
////                 GTdiv.push(t)
////        }
////}
////            if(chartFlag!="false"){
////            var GTData = JSON.parse(data)
////            for(var k in GTdiv){
////            chartData[GTdiv[k]]["GTValue"] = GTData[GTdiv[k]][0]["GTResult"];
////            }
////            parent.$("#chartData").val(JSON.stringify(chartData));
////            }
//               var data = JSON.parse(resultset)["data"];
//               var chartData = JSON.parse($("#chartData").val());
//                var chartFlag = "false";
//                var GTdiv = [];
//                for(var t in chartData){
//                 if(chartData[t]["chartType"]=="KPI-Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash" ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="TileChart" ||chartData[t]["chartType"]=="RadialProgress"||chartData[t]["chartType"]=="LiquidFilledGauge" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
//                        chartFlag = "true"
////                 GTdiv.push(t)
//
//                    $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(resultset)["meta"])["chartData"]))
//                    }
//                }
//                generateChart(data);
//            //                                generateChart1(data);     // added by manik
//            }
//        });
                    }

function moreGraphs(chartId){
    var graphs = Object.keys(parent.graphsMapNext);
    //    $("#more"+ chartId).clear();
    //$("#chartType"+ chartId).empty();


    $("li").filter(function() {
        return $(this).text() == 'More Graphs';
    }).mouseover(function() {
        //   $("#more"+ chartI  d).html("");
        //   document.getElementById("more"+chartId).innerHTML="";
        var html="";
        html +="<ul class='dropdown-menu' style='visiblity:visible' >";
        html = html + "<li><a class='' onmouseover='previousGraphs(\""+chartId+"\")' style=\"cursor: pointer\" '>Previous Graphs</a></li>";
        for(var i=0; i<graphs.length;i++){
            html +=  "<li><a class='' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
        }


        html = html + "</ul></li>";
        //$("#more"+ chartId).append(html);
        $("#chartType"+ chartId).append(html);
    });
}

function previousGraphs(chartId){
    var graphs = Object.keys(parent.graphsMap);
    //    $("#more"+ chartId).clear();
    //$("#chartType"+ chartId).empty();

    var html="";

    html +="<ul class='dropdown-menu'>";
    //    html = html + "<li><a class='' onmouseover='moreGraphs(\""+chartId+"\")' style=\"cursor: pointer\" '>Previous Graphs</a></li>";
    for(var i=0; i<graphs.length;i++){
        html +=  "<li><a class='' style=\"cursor: pointer\" onclick='changeChart(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
    }


    html = html + "</ul></li>";
    //$("#more"+ chartId).append(html);
    $("#chartType"+ chartId).append(html);
}
function moreTrend(chartId){
    var graphs = Object.keys(parent.graphsMapNext);
    //    $("#more"+ chartId).clear();
    //$("#chartType"+ chartId).empty();


    $("li").filter(function() {
        return $(this).text() == 'More Graphs';
    }).mouseover(function() {
        //   $("#more"+ chartI  d).html("");
        //   document.getElementById("more"+chartId).innerHTML="";
        var html="";
        html +="<ul class='dropdown-menu' style='visiblity:visible' >";
        html = html + "<li><a class='' onmouseover='previousTrend(\""+chartId+"\")' style=\"cursor: pointer\" '>Previous Graphs</a></li>";
        for(var i=0; i<graphs.length;i++){
            html +=  "<li><a class='' style=\"cursor: pointer\" onclick='changeTrend(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
        }


        html = html + "</ul></li>";
        //$("#more"+ chartId).append(html);
        $("#chartType"+ chartId).append(html);
    });
}

function previousTrend(chartId){
    var graphs = Object.keys(parent.graphsMap);
    //    $("#more"+ chartId).clear();
    //$("#chartType"+ chartId).empty();

    var html="";

    html +="<ul class='dropdown-menu'>";
    //    html = html + "<li><a class='' onmouseover='moreGraphs(\""+chartId+"\")' style=\"cursor: pointer\" '>Previous Graphs</a></li>";
    for(var i=0; i<graphs.length;i++){
        html +=  "<li><a class='' style=\"cursor: pointer\" onclick='changeTrend(\"" + chartId + "\",\"" + graphs[i].replace(" ", "-", "gi") + "\")'>" + graphs[i] + "</a></li>";
    }


    html = html + "</ul></li>";
    //$("#more"+ chartId).append(html);
    $("#chartType"+ chartId).append(html);
}


function regenerateFilters(id){
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#graphForm').serialize(),
        url:  'reportViewer.do?reportBy=generateFilters',

        success:function(data) {
            alert("Filters Generated")
        }
    });

}

function expandDiv(chartId){

    $(".expandDiv"+chartId).toggleClass("expandDiv1");
    $("#vsdiv14"+chartId).toggle(800);
}
function expandDivComp(chartId){

    $(".expandDivComp").toggleClass("expandDiv1");
    $("#localFilterComp").toggle(800);
}

function expandDiv1(chartId){
    $(".expandDiv1"+chartId).toggleClass("expandDivLocal1");

    $("#localFilter"+chartId).toggle(800);
}
//added by latha
function expandDivAdv(chartId){

    $(".expandDiv"+chartId).toggleClass("expandDiv1");
    $("#vsdiv14Adv"+chartId).toggle(800);
}
function expandDiv2(chartId){

// $(".collapseDiv1").width(250)
 parent.$("#timePeriodDialog").height(300);


     parent.$(".expandDiv2"+chartId).toggleClass("expandDivLocal11");

      parent.$("#vsdiv14"+chartId).toggle(800);
}
//ended by latha
//function expandDivAdv1(chartId){
//    $(".expandDiv1"+chartId).toggleClass("expandDivLocal1");
//
//    $("#localFilterAdv"+chartId).toggle(800);
//}

    function getAdvanceVisuals(reportId){
//    timeDBDrill='';
 selectedViewbyIndex=0;    
    breadcrumpid = [];
    mapDrill = false;
    drillDivId=["topDiv0"];
    $("#my_tooltip").hide();
//    $("#reportTD1").hide();
    $("#xtendChartTD").html('');
    $("#xtendChartTD").show();
//    $("#loading").hide();
    $("#type").val("advance");
//    $("#reportTD1").hide();
    //    $("#xtendChartTD").html('');
    $("#drills").val("");
    $("#filters1").val("");
     parent.checkButtonClick = false;
     parent.clickedButtonId = [];
     parent.clickedButtonFilter = "";
     
     startValgbl=1;
    var htmlVar="";
    var visual = $("#currType").val();
    start=0;
    end=15;
    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    $.post(
        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + reportId+"&reportName="+parent.$("#graphName").val()+"&type=advance&visual="+visual,$("#graphForm").serialize(),
        function(data) {

            if(data=="false"){
                $("#loading").hide();
                htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
                $("#xtendChartTD").append(htmlVar);
                $("#xtendChartTD").show();
            }
            else{
                var jsondata = JSON.parse(data)["data"];
                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                var meta = JSON.parse(JSON.parse(data)["meta"]);
             
                var infoMap = JSON.parse(data)["infoMap"];
                $("#viewby").val(JSON.stringify(meta["viewbys"]));
                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                $("#measure").val(JSON.stringify(meta["measures"]));
                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                $("#drilltype").val((meta["drillType"]));
               parent.$("#timeMap").val(JSON.stringify(parent.timeMapValue));
            if(typeof parent.timeMapValue ==="undefined") {
                $("#timeMap").val(JSON.stringify(meta["timeMap"]));
            }
                $("#filter1").val(meta["filterMap"]);
               
                $("#currType").val(meta["currType"]);
                $("#chartType").val(meta["chartType"]);
                $("#isShaded").val(meta["isShaded"]);
                $("#shadeType").val(meta["shadeType"]);
                $("#measureColor").val(JSON.stringify(meta["measureColor"]));
                $("#visualChartType").val(JSON.stringify(infoMap));
                $("#conditionalMap").val((JSON.stringify(meta["conditionalMap"])));
                $("#conditionalMeasure").val(meta["conditionalMeasure"]);
                //Added by shobhit for multi dashboard on 22/09/15 
                parent.$("#parentViewBy").val(meta["parentViewBy"]);
                parent.$("#childViewBys").val(JSON.stringify(meta["childViewBys"]));
                parent.$("#childMeasBys").val(JSON.stringify(meta["childMeasBys"]));
                parent.$("#selectedViewBys").val(JSON.stringify(meta["selectedViewBys"]));
                parent.$("#selectedMeasBys").val(JSON.stringify(meta["selectedMeasBys"]));
                //                if(typeof meta["filterMap"] != "undefined" && meta["filterMap"] != ""){
                //                    globalFilter = meta["filterMap"];
                //                }
                //              try{
                //               generateChart(jsondata);
                //                 }catch(e){}
               
                $.ajax({
                    async:false,
                    type:"POST",
                    data:
                    $('#graphForm').serialize(),
                    url:  'reportViewer.do?reportBy=getFilters&typegbl=graph',

                    success:function(data) {
                        filterData=JSON.parse(data);
                         applyfiltersgraph();
                         if(savedfilter){
                         $("#filters1").val(JSON.stringify(meta["filterMap"]));
                     }
                        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
                            function(data){
                        
                                var resultset=data;

                        runtimeglobalfilters(JSON.stringify(meta["filterMap"]),'null',"advance");
  var data1= JSON.parse(resultset);

                                generateVisual(data1,infoMap);
                            })
                    }
                });
            
            }
        });
}

function createAdvanceVisual(reportId){
    var REPORTID = document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#ViewByForm').serialize() + "&REPORTID="+REPORTID+"&ctxPath="+ctxPath,
        url: ctxPath+"/reportViewer.do?reportBy=getObjectMap",
        success:function(data){
            var source = ctxPath+"/Report/Viewer/editAdvance.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&from=viewer";
            frameObj.src = source;
        }
    });
    $("#editViewByDiv").dialog('open');
}

function generateAdavanceVisual(data){
    advanceData = JSON.parse(data);
    generateAdvanceGraph(JSON.parse(data));
}

function generateAdvanceGraph(data){

    //    var divContent = "";
    //     divContent += "<div  style='width:100%'><table style='width:86%;float:left'><tr id='row0'>";
    //     divContent+="<div align='center' id='Hichart1' style='border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;'>" +
    //                "<div id='Hchart1' style='display:block;float:left;width:65%'></div>" ;
    var divContent = "";
    var currType =  $("#currType").val();

    var infoMap = JSON.parse(parent.$("#visualChartType").val());
    var chartType = $("#chartType").val();
    var reportId = $("#graphsId").val();
    divContent += "<div  style='width:100%'>";
    divContent+="<div align='center' id='Hichart1' style='border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid; overflow:auto; width: 100%;display:block;position:fixed;'>" +
    "<div id='advanceName' style='display:block;float:left;width:85%;height:20px;'><h3 align='center' style='font-size:medium;text-align:left;margin-left:10px;font-weight:bold;float:left;'>"+currType+"</h3>"+
     "<img title = 'Refresh' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/refersh_image.png' alt='Options' onclick='getAdvanceVisuals(\"" + reportId + "\")'></img>";
    if(chartType==="Bar-Dashboard"){
    divContent+="<img title = 'bar' id='bar' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/bar_icon.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>"+
    "<img title = 'trend' id='trend' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/trend_line.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>";
    }
    divContent+="</span></div>" +
    "<div id='Hchart1' style='display:block;float:left;width:85%'></div></td>";
    //    divContent+="<div style='width:14%;float:left;margin-top:5px;border-left:2px dotted grey;'><table style='width:100%'><tr><h3 align='center' style='font-size:medium'>"+currType+"</h3></tr><tr><td align='center' style='white-space:nowrap'>click here to change visuals</td></tr><tr><td><select style='width:100%' id='visualType' onchange='getChangeVisual(this.value)'>  "
    divContent+="<div style='width:14%;float:left;margin-top:-3.5%;border-left:2px dotted grey;'><table style='width:100%'><tr></tr><tr><td align='center' style='white-space:nowrap'>"+translateLanguage.Click_here_to_change_visuals+"</td></tr><tr><td><select style='width:100%;border: 1px solid #8c8c8c;' id='visualType' onchange='getChangeVisual(this.value)'>  "

    if(currType in infoMap){
        divContent+="<option val='"+currType+"'> "+currType+" </option>";
    }
    for(var key in infoMap){

        divContent+="<option val='"+key+"'> "+key+" </option>";
    }
    divContent+="</select></td></tr></table>";


    var columns = JSON.parse($("#viewby").val());
    var measures = JSON.parse($("#measure").val());
    $("#noData").hide();
    $("#xtendChartTD1").show();
    $("#xtendChartTD").html(divContent);
    $("#xtendChartTD").show();
    $("#drilltype").val("single");
    var chartType = $("#chartType").val();
    if(typeof chartType==="undefined" || chartType==="" || chartType==="Tree-Map"){
        var multiJson = {};
        count=0;
        multiJson["name"] = "";
        var jsonData = {};
        var chartData = JSON.parse($("#chartData").val());

        multiJson["children"] = processHirarchiachalData(data);
        var data1 = JSON.parse(JSON.stringify(multiJson));
        var data = JSON.parse(JSON.stringify(multiJson));

        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildZoomabletreeMulti("Hchart1",data1,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType==="Tree-Map-Single"){
        var multiJson = {};
        count=0;
        multiJson["name"] = "";
        var jsonData = {};
        var chartData = JSON.parse($("#chartData").val());
        //                 for(var ch in chartData){
        var ch="chart1";
        measures=[];
        measuresIds=[];
        columns.push(chartData[ch]["viewBys"][0]);
        columnsIds.push(chartData[ch]["viewIds"][0]);
        measures.push(chartData[ch]["meassures"]);
        measuresIds.push(chartData[ch]["meassureIds"]);
        //        }
        multiJson["children"] = processHirarchiachalData(data);
        var data1 = JSON.parse(JSON.stringify(multiJson));
        var data = JSON.parse(JSON.stringify(multiJson));
        buildZoomabletree("Hchart1",data,columns,measures);
    }
    else if(chartType==="CirclePacking"){
        circlePacking("Hchart1",data,columns,measures);
    }
    else if(chartType==="CoffeeWheel"){
        var multiJson = {};
        count=0;
        multiJson["name"] = "";
        var jsonData = {};
        var chartData = JSON.parse($("#chartData").val());

        multiJson["children"] = processHirarchiachalData(data);
        var data1 = JSON.parse(JSON.stringify(multiJson));
        var data = JSON.parse(JSON.stringify(multiJson));
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildcoffeeWheel("Hchart1",data1,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType==="Fish-Eye"){
        var multiJson = {};
        count=0;
        multiJson["name"] = "";
        var jsonData = {};
        var chartData = JSON.parse($("#chartData").val());

        multiJson["children"] = processHirarchiachalData(data);
        var data1 = JSON.parse(JSON.stringify(multiJson));
        var data = JSON.parse(JSON.stringify(multiJson));
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildDendrogram("Hchart1",data1,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType==="India-map"){
        buildDistrictMap("Hchart1",data,columns,measures,600,400 );
    }
    else if(chartType==="India-map-with-chart"){
        var width = screen.width
        var height = screen.height
        
        buildmapTestWithChart("Hchart1",data,columns,measures,width*.5,height*.8 );
    }
	  else if(chartType==="Australia-map"){
        var width = screen.width
        var height = screen.height

//        buildmapTestWithChart("Hchart1",data,columns,measures,width*.5,height*.8 );
australiaMap("Hchart1",data,columns,measures,width*.5,height*.8 )
    }
    else if(chartType==="India-District-Map"){
        buildDistrictIndiaMap("Hchart1",data,columns,measures,600,400 );
    }
    else if(chartType==="India-City-Map"){
        buildCityMap("Hchart1",data,columns,measures,600,400 );
    }
    else if(chartType==="Bubble-Dashboard" || chartType==="combo-chart"){
        buildDashboard4("Hchart1",data);
    }
    else if(chartType==="Scatter-Dashboard"){
        buildDashboardScatter("Hchart1",data);
    }
    else if(chartType=="Pie-Dashboard"){
        buildPieTableDashboard("Hchart1",data);
    }
    else if(chartType==="KPI-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        buildKPIRegions("Hchart1",data,columns,measures,columnsIds,measuresIds);
    }
    else if(chartType==="KPI-Bar" ||chartType==="Bar-Dashboard" || chartType === "Advance-Pie" || chartType == "Advance-Horizontal" || chartType == "Trend-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        var dimensions = chartData["chart1"]["dimensions"];
        var dataId = "chart"+(parseInt(dimensions.indexOf(columnsIds[0]))+1);
        if(chartType==="Bar-Dashboard"){
            dataId = "chart1";
        }
        buildKPIBar("Hchart1",data,columns,measures,dataId,columnsIds,measuresIds);
    }
    else if(chartType=="Multi-Measure-Dashboard"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        buildmultiMeasureDashboard("Hchart1",data,columns)
    }
    else if(chartType=="Multi-View-Wordcloud"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildMultiViewWordCloud("Hchart1",data,columns,measures,columnsIds);
    }
    else if(chartType=="Wordcloud"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures=chartData[ch]["meassures"];
            measuresIds=chartData[ch]["meassureIds"];
        }
        var height = $(window).height();
        var width = $(window).width();
        var dimensions = chartData["chart1"]["dimensions"];
        buildWordCloud("Hchart1",data["chart"+(parseInt(dimensions.indexOf(columnsIds[0]))+1)],columns,measures,width,height);
    }
    else if(chartType=="Split-Graph"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"]);
            columnsIds.push(chartData[ch]["viewIds"]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        //    var multiJson = {};
        //                count=0;
        //                multiJson["name"] = "";
        //                multiJson["children"] = processHirarchiachalData(JSON.parse(jsonData));
        //               var data = JSON.parse(JSON.stringify(multiJson));
        //    }
        buildsplit("Hchart1",data,chartData[ch]["viewBys"],chartData[ch]["meassures"]);
    //                    bubbleDb("Hchart1",data,chartData[ch]["viewBys"],chartData[ch]["meassures"]);
    }
    else if(chartType=="Multi-View-Bubble"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"]);
            columnsIds.push(chartData[ch]["viewIds"]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        //        var dbData = data["chart1"];
        //        for(var h=0;h<data["chart1"].length<100?data["chart1"]:100;h++){
        //    dbData.push(data["chart1"][h]);
        //}

        bubbleDbMulti("Hchart1",data["chart1"],chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"],"600","450",500);
    }
    else if(chartType=="multi-kpi"){
        buildMultiKpiDb("Hchart1",data["chart1"]);
    }
    else if(chartType=="Overlay"){
        buildOverLayedChart(data,"Hchart1");
    }
    else if(chartType=="us-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildUSMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType==="India-Map-Dashboard"){
         buildIndiaMapDashboard("Hchart1",data,columns,measures,600,400);
    }
    else if(chartType==="India-Map-With-Trend"){
         buildIndiaMapWithTrend("Hchart1",data,columns,measures,600,400);
    }
    else if(chartType=="us-map-city"){
        var chartData = JSON.parse($("#chartData").val());
        buildUSMapCity("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-map-animation"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldMapLinked("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-city-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldCityMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    //Added by shobhit for multi dashboard on 22/09/15 
    else if(chartType==="Multi-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var childViewBys=[];
        var viewBys=JSON.parse($("#viewby").val());
        var viewByIds=JSON.parse($("#viewbyIds").val());
        
        if(typeof chartData["chart1"]["isDashboardDefined"]!=='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y'){
            var childViewByIds=JSON.parse($("#childViewBys").val());
            for(var i in childViewByIds){
                childViewBys.push(viewBys[viewByIds.indexOf(childViewByIds[i])]);
    }
        }
    else{
            var selectedViewBys=JSON.parse($("#selectedViewBys").val());
            for(var i in selectedViewBys){
                childViewBys.push(viewBys[viewByIds.indexOf(selectedViewBys[i])]);
            }
        }
        buildMultiDBLayout1("Hchart1",data,childViewBys,chartData["chart1"]["meassures"]);
    }
    else if(chartType==="Time-Dashboard"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildTimeDB("Hchart1",data,columns,chartData["chart1"]["meassures"]);
    }
    else{

        buildZoomabletreeMulti("Hchart1",data,chartData[ch]["viewBys"],chartData[ch]["meassures"]);
    }
//        buildcoffeeWheel("Hchart1",data,columns,measures);
//        buildZoomabletreeMulti("Hchart1",data,columns,measures);
}

function generateVisual(jsonData,infoMap){
    $("#loading").hide();
     
    var chartType = $("#chartType").val();
    //    if(chartType==="India-map"){
    var data = jsonData;
    //    alert(JSON.stringify(data))
    var currType =  $("#currType").val();
    //    }
    //    else{
    if(chartType=="Tree-Map" || chartType=="CoffeeWheel" || chartType=="Fish-Eye"  || chartType==="Tree-Map-Single"){
        var multiJson = {};
        count=0;
        multiJson["name"] = "";
        multiJson["children"] = processHirarchiachalData(jsonData);
        data = JSON.parse(JSON.stringify(multiJson));
    }
    var divContent = "";
     var reportId = $("#graphsId").val();
    divContent += "<div  style='width:100%'>";
    if(chartType=='Pie-Dashboard'){
    divContent+="<div align='center' id='Hichart1' style='border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;overflow:auto; width: 100%;display:block;position:fixed;'>" +
    "<div id='advanceName' style='display:block;float:left;width:85%;height:20px;border-bottom:2px double grey;padding-bottom:2px'><h3 align='center' style='font-size:medium;text-align:left;margin-left:10px;font-weight:bold;float:left;'>"+currType+"</h3>"+
      "<img title = 'Refresh' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/refersh_image.png' alt='Options' onclick='getAdvanceVisuals(\"" + reportId + "\")'></img>";
    if(chartType==="Bar-Dashboard"){
    divContent+="<img title = 'bar' id= 'bar' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/bar_icon.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>"+
    "<img title = 'trend' id = 'trend' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/trend_line.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>";
    }
    divContent+="</span></div>" +
    "<div id='viewMeasureBlock' style='display:block;float:left;width:85%;height:30px'></div>" +
    "<div id='navigationBar' style='display:block;float:left;width:85%;height:30px'><div id='innerNavigationBar' style='display:block;float:right;%;width:60px;height:30px'>"+
    "<span style='text-decoration: none'><img width='16' id='Vertical-Bar' height='16' title='Prev' style='margin-left:20%;cursor:pointer;' src='images/prevRecords.png' alt='Bar' onclick='getNextGraphValue(\"prev\")'></img></span>"+
    "<span><img width='16' id='Vertical-Bar' height='16' title='Next' style='margin-left:20%;cursor:pointer;' src='images/nextRecords.png' alt='Bar' onclick='getNextGraphValue(\"next\")'></img></span>"+
    "</div></div>"+
    "<div id='Hchart1' style='display:block;float:left;width:85%'></div>";
    }
    else{
        divContent+="<div align='center' id='Hichart1' style='border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid; overflow:auto; width: 100%;display:block;position:fixed;'>" +
    "<div id='advanceName' style='display:block;float:left;width:85%;height:20px;border-bottom:2px double grey;padding-bottom:2px'><h3 align='center' style='font-size:medium;text-align:left;margin-left:10px;font-weight:bold;float:left;'>"+currType+"</h3>"+
    "<img title = 'Refresh' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/refersh_image.png' alt='Options' onclick='getAdvanceVisuals(\"" + reportId + "\")'></img>";
    if(chartType==="Bar-Dashboard"){
    divContent+="<img title = 'bar' id = 'bar' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/bar_icon.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>"+
    "<img title = 'trend' id = 'trend' style='width:16px;height:16px;float:right;margin-top:3px;margin-right:5px;' src='images/trend_line.png' alt='Options' onclick='changeAdvChart(\"" + reportId + "\",this.id)'></img>";
    }
    divContent+="</span></div>" +
    "<div id='viewMeasureBlock' style='display:block;float:left;width:85%;height:30px'></div>" +
    "<div id='Hchart1' style='display:block;float:left;width:85%'></div>";
    }
    if(chartType==="KPI-Bar" ||chartType==="Bar-Dashboard" || chartType === "Advance-Pie" || chartType == "Advance-Horizontal"){
        //   divContent+="<div style='width:14%;float:left;margin-top:5px;border-left:2px dotted grey;'><table style='width:100%'><tr><h3 align='center' style='font-size:medium'>"+currType+"\n\
        divContent+="<div style='width:14%;float:left;margin-top:-3.5%;border-left:2px dotted grey;position:absolute;float:right;margin:40px 0px 0px 0px;right:0%;'><table style='width:100%'><tr>";
 if (checkBrowser() == "ie") {
divContent += "<img class='' src='images/filter.png' width='15px' height='15px' title='Show/Hide Filters' align='right' onclick='getFilters()'>";
 }
divContent += "</tr><tr><td align='center' style='white-space:nowrap'>"+translateLanguage.Click_here_to_change_visuals+"</td></tr><tr><td><select style='width:100%;border: 1px solid #8c8c8c;' id='visualType' onchange='getChangeVisual(this.value)'>  ";
    }else {
        //    divContent+="<div style='width:14%;float:left;margin-top:5px;border-left:2px dotted grey;'><table style='width:100%'><tr><h3 align='center' style='font-size:medium'>"+currType+"\n\
        //Edited by Faiz Ansari
//divContent+="<div style='width:14%;float:left;margin-top:-3.5%;border-left:2px dotted grey;'><table style='width:100%'><tr>\n\
        divContent+="<div style='width:14%;border-left:2px dotted grey;position:absolute;float:right;margin:40px 0px 0px 0px;right:0%'><table style='width:100%'><tr>\n\
</tr><tr><td align='center' style='white-space:nowrap'>"+translateLanguage.Click_here_to_change_visuals+"</td></tr><tr><td><select style='width:100%;border: 1px solid #8c8c8c;' id='visualType' onchange='getChangeVisual(this.value)'>  ";
    //End!!!
    //<img class='legendSwap' src='images/showlegend.png' width='30px' height='30px' title='Show/Hide Legends' align='right' onclick='getLegends()'></h3></tr><tr><td align='center' style='white-space:nowrap'>click here to change visuals</td></tr><tr><td><select style='width:100%' id='visualType' onchange='getChangeVisual(this.value)'>  "
    }
    for(var key in infoMap){
        if(currType!=key) {
            divContent+="<option val='"+key+"'> "+key+" </option>";
        }else{
            divContent+="<option val='"+key+"' selected> "+key+" </option>";
        }
    }
    divContent+="</select></td></tr></table>";
    try{
        var filterKeys = Object.keys(filterData);
        var viewIds = JSON.parse($("#viewbyIds").val());
        divContent += "</tr></table>";
        //     <div id="content_1" class="content" style="width: 100%">
        // <div id="vhead14" style="display:none" class="panelcollapsed">
        //                    <h2><label id="header14" class="headl"></label></h2>
        //                    <div id="vsdiv14" class="panelcontent">
        //                    </div>
        //                </div>

        divContent += "<div id='content_2' width='100%' style='width:100%;float:right;padding-top:2em; height:490px;overflow:auto;display:none'></div><div id='content_1' class='content' style='width:100%;float:right;padding-top:2em; height:490px;overflow:auto;border-left:2px dotted grey;display:'>";
        var viewByIds = JSON.parse($("#viewbyIds").val());
        var viewBy = JSON.parse($("#viewby").val());
            var drills = {};
        for (var key in filterKeys) {
            var grpKeys;
            try{
                drills = JSON.parse($("#drills").val());
                grpKeys = Object.keys(drills);
            }catch(e){}
            var index = viewBy.indexOf(filterKeys[key]);
            if(index==-1){
                index=viewBy.indexOf(" "+filterKeys[key])
            }
            var viewId = viewByIds[index];
            var filterGroupBy = filterKeys[key];

            //        var viewId = viewIds[key];
            var filters = filterData[filterGroupBy];
            var selectedFilters = [];
            var filterMap = {};
            var filterValues=[];
            if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
                filterMap = JSON.parse($("#filters1").val());
                if(typeof filterMap[viewId]!=="undefined"){
                    filterValues=filterMap[viewId];
                }
            }

            if (typeof filterMap[viewId] !== "undefined") {
                selectedFilters = filterMap[viewId];
            }
            if (checkBrowser() == "ie") {
            divContent += "<div class='expandDiv"+filterGroupBy.replace(/\s/g, '')+" expandDiv' name='expand' width='13%' onclick='expandDivAdv(\""+filterGroupBy.replace(/\s/g, '')+"\")' style='border-bottom: 1px solid lightgrey;padding-top:.5em;padding-bottom:.5em;height:22px;background-color:#F1F1F1'><div class='' style='paddind-top:1px;padding-left:10%'><label class='headl' style='font-size: 11px;color:rgb(79,76,89);'>";

            if (selectedFilters.length === 0) {
                divContent += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_Ad' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' checked onclick='selectAll(this.id,this.name)'/>";
            } else {
                divContent += "<input type='checkbox' class='ckbox' id='" + filterGroupBy.replace("(", "_g_", "gi").replace(")", "_h_", "gi").replace(" ", "", "gi") + "_Ad' name='" + filters.length + "*," + filterGroupBy + "*," + viewId + "' onclick='selectAll(this.id,this.name)'/>";
            }
            if(filterGroupBy.length > 25){
                divContent += "<span style='white-space:nowrap'>&nbsp;" + filterGroupBy.substring(0,25) + "..</span></label></div>";
            }
            else{
                divContent += "<span>&nbsp;" + filterGroupBy + "</span></label></div>";
            }
            divContent += "</div>";
            divContent += "<div id='vsdiv14Adv"+filterGroupBy.replace(/\s/g, '')+"' class='collapseDiv' style='display:none'><table style='' width='100%'>";
            for (var filter in filters) {
                divContent += "<tr><td><label style=\"font-color:#343434;font-size: .7em;\"><span class='custom-checkbox'>";
                if(typeof filters[filter]!="undefined" && filters[filter]!=""){
                    if (selectedFilters.indexOf(filters[filter]) !== -1) {
                        divContent += "<input type='checkbox' class='ckbox' id='" + filterGroupBy + "_" + filter + "' value='" + filters[filter] + "' name='" + filterGroupBy + "*,"+viewId+"'  onclick='applyFilter(this.id,this.name)'/>";
                    } else {
                        divContent += "<input type='checkbox' class='ckbox' id='" + filterGroupBy + "_" + filter + "' checked value='" + filters[filter] + "' name='" + filterGroupBy + "*,"+viewId+"' onclick='applyFilter(this.id,this.name)'/>";
                    }
                    divContent += "<span class='box'>&nbsp;" + filters[filter] + "</span></span></label></td></tr>";
                }
            }
            divContent += "</table></div>";
            }}
        divContent += "</div>";
    }catch(e){}
    divContent += "</div>";
    divContent+="</div>";
    divContent+="</div>";


        var chartData = JSON.parse($("#chartData").val());
    var columns = JSON.parse($("#viewby").val());
    var measures = JSON.parse($("#measure").val());
    $("#drilltype").val("single");
    $("#noData").hide();
    $("#xtendChartTD1").show();
    $("#xtendChartTD").html(divContent);
    $("#xtendChartTD").show();
    if(drills!="undefined" && drills!=""){
    var htmlvar = "";

var chart = "chart1"

htmlvar += "<ul class='breadcrumb' style='float:left;margin-left:2%'>"

// htmlvar += "<li><img title = 'drill up' style='width:16px;height:16px' src='images/drillupgrey.png' alt='Options' onclick='drillUp(\"" + chart + "\")' ></img></a>"
            for(var i in drills){

            htmlvar += "<li id='drillUp#"+i+"' onclick='drillUpGraph(\"" + chart + "\",this.id)'><a href='#'>"+drills[i]+"</a></li>"
            }

		  htmlvar += "</ul>"
            $("#advanceName").append(htmlvar)

        }
    var chartType = $("#chartType").val();
    if(typeof chartType==="undefined" || chartType==="" || chartType==="Tree-Map"){
        var columns = chartData["chart1"]["viewBys"];
    var measures = chartData["chart1"]["meassures"];
        buildZoomabletreeMulti("Hchart1",data,columns,measures);
    }
    if(chartType==="Tree-Map-Single"){
        var ch="chart1";
        measures=[];
        columns=[];
        var columnsIds=[];
        var measuresIds=[];
        columns.push(chartData[ch]["viewBys"][0]);
        columnsIds.push(chartData[ch]["viewIds"][0]);
        measures.push(chartData[ch]["meassures"]);
        measuresIds.push(chartData[ch]["meassureIds"]);
        buildZoomabletree("Hchart1",data,columns,measures);
    }
    else if(chartType==="CirclePacking"){
        circlePacking("Hchart1",data,columns,measures);
    }
    else if(chartType==="CoffeeWheel"){
        buildcoffeeWheel("Hchart1",data,columns,measures);
    }
    else if(chartType==="Fish-Eye"){
        buildDendrogram("Hchart1",data,columns,measures);
    }
    else if(chartType==="India-map"){
        buildDistrictMap("Hchart1",data,columns,measures,580,400 );
    }
    else if(chartType==="India-map-with-chart"){
        var width = screen.width
        var height = screen.height
//        alert(width+":"+height)
        buildmapTestWithChart("Hchart1",data,columns,measures,width*.4,height*.8 );
    }
	  else if(chartType==="Australia-map"){
        var width = screen.width
        var height = screen.height

//        buildmapTestWithChart("Hchart1",data,columns,measures,width*.5,height*.8 );
australiaMap("Hchart1",data,columns,measures,width*.5,height*.8 )
    }
    else if(chartType==="India-District-Map"){
        buildDistrictIndiaMap("Hchart1",data,columns,measures,600,400 );
    }
    else if(chartType==="India-City-Map"){
         var width = screen.width
        var height = screen.height
        buildCityMap("Hchart1",data,columns,measures,width*.4,height*.8 );
    }
    else if(chartType==="KPI-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        buildKPIRegions("Hchart1",data,columns,measures,columnsIds,measuresIds);
    }
    else if(chartType==="KPI-Bar" ||chartType==="Bar-Dashboard" || chartType === "Advance-Pie" || chartType == "Advance-Horizontal" || chartType == "Trend-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        var dimensions = chartData["chart1"]["dimensions"];
        var dataId = "chart"+(parseInt(dimensions.indexOf(columnsIds[0]))+1);
//         if(chartType==="Bar-Dashboard"){
//            dataId = "chart1";
//        }
        
        buildKPIBar("Hchart1",data,columns,measures,dataId,columnsIds,measuresIds);
    }
    else if(chartType=="Multi-Measure-Dashboard"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        buildmultiMeasureDashboard("Hchart1",data,columns)
    }
    else if(chartType=="Multi-View-Wordcloud"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildMultiViewWordCloud("Hchart1",data,columns,measures,columnsIds);
    }
    else if(chartType=="Wordcloud"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures=chartData[ch]["meassures"];
            measuresIds=chartData[ch]["meassureIds"];
        }
        var height = $(window).height();
        var width = $(window).width();
        var dimensions = chartData["chart1"]["dimensions"];
        buildWordCloud("Hchart1",data["chart"+(parseInt(dimensions.indexOf(columnsIds[0]))+1)],columns,measures,width,height);
    }
    else if(chartType=="Split-Graph"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        buildsplit("Hchart1",data,chartData[ch]["viewBys"],chartData[ch]["meassures"],height);
    }
    else if(chartType=="Multi-View-Bubble"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"]);
            columnsIds.push(chartData[ch]["viewIds"]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        bubbleDbMulti("Hchart1",data["chart1"],chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"],"600","450",500);
    }
    else if(chartType==="Multi-Dashboard"){
        var chartData = JSON.parse($("#chartData").val());
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
        }
        measureData=data;
        var childViewBys=[];
        var viewBys=JSON.parse($("#viewby").val());
        var viewByIds=JSON.parse($("#viewbyIds").val());
        var dimensions = chartData["chart1"]["dimensions"];
        if(typeof chartData["chart1"]["isDashboardDefined"]!=='undefined' && chartData["chart1"]["isDashboardDefined"]==='Y'){
            var childViewByIds=JSON.parse($("#childViewBys").val());
            for(var i in childViewByIds){
                childViewBys.push(viewBys[viewByIds.indexOf(childViewByIds[i])]);
            }
        }
        else{
            var selectedViewBys=JSON.parse($("#selectedViewBys").val());
            for(var i in selectedViewBys){
                childViewBys.push(viewBys[viewByIds.indexOf(selectedViewBys[i])]);
            }
        }
//        var dataId = "chart"+(parseInt(dimensions.indexOf(columnsIds[0]))+1);
        buildMultiDBLayout1("Hchart1",data,childViewBys,chartData["chart1"]["meassures"]);
    }
    else if(chartType=="Bubble-Dashboard" || chartType=="combo-chart"){
        buildDashboard4("Hchart1",data);
    }
    else if(chartType=="Scatter-Dashboard"){
        buildDashboardScatter("Hchart1",data);
    }
    else if(chartType=="Pie-Dashboard"){
        buildPieTableDashboard("Hchart1",data);
    }
    else if(chartType=="Overlay"){
        buildOverLayedChart(data,"Hchart1");
    }
    else if(chartType=="multi-kpi"){
        buildMultiKpiDb("Hchart1",data)
    }
    else if(chartType=="us-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildUSMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="us-map-city"){
        var chartData = JSON.parse($("#chartData").val());
        buildUSMapCity("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-map-animation"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldMapLinked("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType=="world-city-map"){
        var chartData = JSON.parse($("#chartData").val());
        buildWorldCityMap("Hchart1",data,chartData["chart1"]["viewBys"],chartData["chart1"]["meassures"]);
    }
    else if(chartType==="India-Map-Dashboard"){
//        buildmapTestWithChart("Hchart1",data,columns,measures,600,400 );
        buildIndiaMapDashboard("Hchart1",data,columns,measures,600,400);
}
    else if(chartType==="India-Map-With-Trend"){
//        buildmapTestWithChart("Hchart1",data,columns,measures,600,400 );
        buildIndiaMapWithTrend("Hchart1",data,columns,measures,600,400);
}
else if(chartType==="Time-Dashboard"){
        var columns=[];
        var columnsIds=[];
        var measures=[];
        var measuresIds=[];
        var chartData = JSON.parse($("#chartData").val());
        for(var ch in chartData){
            measures=[];
            measuresIds=[];
            columns.push(chartData[ch]["viewBys"][0]);
            columnsIds.push(chartData[ch]["viewIds"][0]);
            measures.push(chartData[ch]["meassures"]);
            measuresIds.push(chartData[ch]["meassureIds"]);
}
        buildTimeDB("Hchart1",data,columns,chartData["chart1"]["meassures"]);
    }    
$("#Hichart1").height(($(window).height())-164+"px");
$("#Hichart1").width($(window).width());
}


function processHirarchiachalData(dataMap) {

    var keys = Object.keys(dataMap);

    var json = [];
    
   
    for (var key in keys) {
        if (count < 1000) {
            var innerJson = {};
            innerJson["name"] = keys[key];
            var innerMap = dataMap[keys[key]];

            if (innerMap instanceof Array) {
                innerJson["size"] = 1;
                  innerJson["rectValue"] = innerMap[0];
            } else if (innerMap instanceof Object) {

                var child = processHirarchiachalData(innerMap);
                innerJson["children"] = child;
            }
            json.push(innerJson);
        }
    }
   
    
    //        json = sorter.sortDataSet(json, "size", 'D', 'N');
    count++;
    return json;
}

var kpiColor = ["#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df",
"#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d","#ee720e",
"#10a3df", "#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df",
"#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d","#ee720e", "#10a3df", "#ca3092", "#8d2300", "#ed8c9d"];


function localRefresh(chartId){
    var chartData = JSON.parse(parent.$("#chartData").val());
    $('#drilltype').val();
    $('#drills').val();
    var pageValue = currentPage;
    if(typeof pageValue == "undefined" || pageValue == ""){
        pageValue = "default";
    }
    //Ashutosh
    try{
    window["actionCount_" + chartId]=0;
    var dataSlider=chartData[chartId]["dataSlider"];
    if(dataSlider[window.changedMeasureId]!=='undefined'&&dataSlider[window.changedMeasureId]==='Yes'){
    chartData[chartId]["measureFilters"]={};
    window.flag=true;
//    alert("changeViewBys"+window.flag)
    parent.range.axisVal={};
    parent.range.slidingVal={};
    parent.range.map={};    
    parent.range.clausemap={};    
    }
    }catch(e){}
    var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
//    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
//    //Added by Ashutosh
//    if(typeof chartData[chartId]["dataSlider"]!=='undefined' && chartData[chartId]["dataSlider"]!=='undefined'&&chartData[chartId]["dataSlider"]==='Yes'){
//    window.range.min=window.range.axisMinVal;
//    window.range.max=window.range.axisMaxVal;
//    }
    
    $.ajax({
        async:false,
        type:"POST",
        data:
        $('#graphForm').serialize(),
        url:  'reportViewer.do?reportBy=getLocalChart&chartId='+chartId+'&userID='+window.userId+'&currentPage='+pageValue,

        success:function(data) {
//            var jsondata = JSON.parse(data)["data"];
            $("#chartData").val(JSON.stringify(JSON.parse(data)["meta"]["chartData"]));
            var meta = JSON.parse(data)["meta"];
            $("#viewby").val(JSON.stringify(meta["viewbys"]));
            $("#measure").val(JSON.stringify(meta["measures"]));
            $("#aggregation").val(JSON.stringify(meta["aggregations"]));
            $("#drilltype").val((meta["drillType"]));
            $("#countType").val((meta["countType"]));
            $("#filters1").val(JSON.stringify(meta["filterMap"]));
            //              try{
            //               generateChart(jsondata);
            //                 }catch(e){}
//             var completeViewbyIds = JSON.parse($("#viewbyIds").val());
//      var completeViewby = JSON.parse($("#viewby").val());
//      var viewbys = [];
//      var viewbyIds = [];
//         var chartData = JSON.parse($("#chartData").val());
//      var dimensions = chartData[chartId]["dimensions"];
//      for(var i in dimensions){
//      for(var j in completeViewbyIds){
//
//      if(dimensions[i]==completeViewbyIds[j])  {
//          viewbys.push(completeViewby[j])
//          viewbyIds.push(completeViewbyIds[j])
//      }
//      }
//      }
//             var  QuickFilterid="viewbys_"+chartId+"_"+viewbys[i]+"_"+viewbyIds[i]
//         if( chartData[chartId]["QuickFilterValue"]!="undefined")   {
//Added by shivam
//if(typeof chartData[chartId]["QuickFilterId"]!="undefined" && chartData[chartId]["QuickFilterId"]!=""){
//   QuickFilterid=chartData[chartId]["QuickFilterId"]
////}
////alert(QuickFilterid)
//    showQuickFilter(QuickFilterid)
//             }
$.ajax({
            async:false,
            type:"POST",
            data :$("#graphForm").serialize(),
            url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&initializeFlag=true"+"&action=localfilterGraphs",
            success: function(localData){
                         parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(localData)["meta"])["chartData"]));
//var jsondata1 = JSON.parse(localData)["data"];
            generateSingleChart(localData,chartId);
            
                        }
                        });   
//   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&initializeFlag=true"+"&action=localfilterGraphs", $("#graphForm").serialize(),
//                        function(localData){
//                        alert(localData)
//                         $("#chartData").val(JSON.stringify(JSON.parse(data)["meta"]["chartData"]));
////var jsondata1 = JSON.parse(localData)["data"];
//            generateSingleChart(localData,chartId);
//            
//                        });   
        //     generateChart(JSON.stringify(jsondata));
        //     setChartDetails(chartId);
        }
    });
}
function oneviewupdate(regid,chartId,divid,repId,reportName,oneviewid,type){

    fromoneview='true';
    divcount=regid;
    var reporttime;
    //divcount=divcount-1;
    parent.$("#chartname").val(chartId);
    $("#chart"+regid).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');

    if(type=='enablefilter'){
        $("#enable"+regid).val('enablefilter');
        $("#enablefilter").css({
            'background-color':'#808080'
        });
        setTimecluase();
    //   document.getElementById('enablefilter').style.background-color='#808080';
    }else if(type=='disablefilter'){
        $("#enable"+regid).val('disablefilter');
        $("#disablefilter").css({
            'background-color':'#808080'
        });
         parent.$("#drillFormat").val("none");
    }else if(type=='enabletime'){
        reporttime='fasle';
        $("#enabletime"+regid).val('enabletime');
        $("#enabletime").css({
            'background-color':'#808080'
        });
    }else if(type=='disabletime'){
        reporttime='true';
        $("#enabletime"+regid).val('disabletime');
        $("#disabletime").css({
            'background-color':'#808080'
        });
    }
    if(type=='enabletime'||type=='disabletime'){
        $.ajax({
            async:false,
            type:"POST",

            url: parent.ctxpath+'/oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+regid+'&oneviewID='+oneviewid+'&oneviewId='+oneviewid+'&oneviewTime='+reporttime,
            success: function(data){

            }
        });
    }
    var filterenabe;
    var timeenable;
    if(type=='save'){
        filterenabe=$("#enable"+regid).val();
        timeenable=$("#enabletime"+regid).val();
        if(filterenabe=='disablefilter'){
            $("#disablefilter").css({
                'background-color':'#808080'
            });
        }else if(filterenabe=='enablefilter'){
            $("#enablefilter").css({
                'background-color':'#808080'
            });
        }
        if(JSON.stringify(localfilterMap)=="{}"){
            localfilterMap='null'
        }
    }else{
        localfilterMap='null'
    }
    //    line["line1"]=line1;
    //    $("#lines").val(JSON.stringify(line));
    $.ajax({
        async:false,
        type:"POST",
        data: parent.$("#oneviewgraphForm").serialize()+"&reportId="+repId+"&oneviewID="+oneviewid+"&oneviewid="+oneviewid+"&regid="+regid+"&reportName="+encodeURIComponent(reportName)+"&localfilterMap="+localfilterMap,

        url: parent.ctxpath+"/reportViewer.do?reportBy=getAvailableCharts1&filterenabe=localfilter&fromoneview=true&action="+type+"&oneviewtime="+parent.oneviewTimecheck+"&filterenable="+$("#enable"+regid).val()+"&timeenable="+encodeURIComponent(timeenable),
        success: function(data){
            //     $.post(
            //            'reportViewer.do?reportBy=getAvailableCharts&fromoneview=true&reportId=' + repId+"&reportName="+parent.$("#graphName").val(), parent.$("#oneviewgraphForm").serialize(),
            //            function(data) {
            $("#loading").hide();
            if(data=="false"){

            }
            else{
                var jsondata = JSON.parse(data)["data"];
                parent.$("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));

                var meta = JSON.parse(JSON.parse(data)["meta"]);
                parent.$("#viewby").val(JSON.stringify(meta["viewbys"]));
                parent.$("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                parent.$("#measure").val(JSON.stringify(meta["measures"]));
                parent.$("#measureIds").val(JSON.stringify(meta["measureIds"]));
                parent.$("#aggregation").val(JSON.stringify(meta["aggregations"]));
                parent.$("#timedetails").val(JSON.parse(data)["Timedetails"]);
                  parent.$("#timeMap").val(JSON.stringify(meta["timeMap"]));
                //                parent.$("#aggregation").val(JSON.stringify(meta["aggregation"]));
                parent.$("#drilltype").val((meta["drilltype"]));
                //               generateChartloop(jsondata)
                //               alert("open sucess")
                //               var gridsPos=grid.serialize1();
                //if(action=='add'){
                //     var chartData = JSON.parse(parent.$("#chartData").val());
                ////     if(chartData[chartid]["chartType"]=="TileChart"){
                ////
                ////     }else{
                //// chartData[chartid]["row"] = 'undefined';
                ////     }
                //    parent.$("#chartData").val(JSON.stringify(chartData));
                //
                //     }
                if(type=='rename'){
                    var html="";
                    html += "<span id='dbChartName"+divid+"' style ='display:'><a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+regid+"&REPORTID="+repId+"')\"><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+parent.$("#rename").val()+"</tspan></a></span>"

                    $("#renameTitle"+divid).html(html);
                    alert("chart rename successfully")
                }else{
                    generateSingleChart(jsondata,divid);
                }
                if(type=='save'){
                    alert("your chart saved")
                }
            }
        }
    });

}
function toggleChart(chartId){
    var data = graphData[chartId];
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    //            var aggregation = chartData[chartId]["aggregation"];
    var htmlstr = "<table class=\"\" align= 'left'  style=' overflow-y:auto;width:100%;height:100%' >";
    for (var no = 0; no < measures.length; no++) {
        var sum1=0;
        $.each(data, function (d) {
            sum1 += parseInt(data[d][measures[no]]);
        });
        //var sum1 = d3.sum(data, function(d) {
        //        return d[measures[no]];
        //    });
        //                var c = measures[no];
        //                if (typeof data["XtendGT"] !== "undefined") {
        //                    sum1 = data["XtendGT"][0][c];
        //                }
        sum1 = sum1.toFixed(2);
        sum1.replace(".00", "");
        htmlstr += "<tr class=\"dashKpi\" style='font-size:90%' align='center'>";
        //                }
        htmlstr += "<td class='kpiTD' style='background: " + color(no) + ";width:40%' onclick=\"showKpiDiv('" + chartId + "','" + measures[no] + "')\"><table height='auto'>" + //<table height='" + heightOfSvg / 5.5 + "'>" +
        "<tr><td style='font-size: 1.2em;font-weight: bold;color:white'>" + addCommas(sum1) + "</td></tr>" +
        "<tr><td style='font-size: .7em;color:white;'>" + measures[no] + "</td></tr></table></td>";
        htmlstr += "</tr>";
    }
    htmlstr += "</table>";
    $("#" + chartId).html(htmlstr);
    d3.select("#" + chartId).attr("style", "width:99.5%;height:100%;overflow:hidden;border-left:1px #fff solid;border-right:1px rgb(200,200,200) solid;border-bottom:1px rgb(200,200,200) solid;");
}
function globalGT(data, measure, aggregation) {
    var sum = d3.sum(data, function(d) {
        return d[measure];
    });
    if (aggregation === "AVG" || aggregation === "avg") {
        sum = sum / data.length;
    }
    return sum;
}


function getChangeVisual(visual){
   
    $("#legendsDiv").html(''); //Added By Ram
//    $("#reportTD1").hide();
    $("#xtendChartTD").html('');
    $("#xtendChartTD").show();
    parent.$("#loading").show();
    $("#type").val("advance");
//    $("#reportTD1").hide();
    $("#xtendChartTD").html('');
    $("#drills").val("");
    $("#filters1").val("");
    $("#changeVisualType").val(visual);
    var htmlVar="";
    //    htmlVar+="<div><span onclick='editViewBys()'>Add Data</span></div>";
    $.post(
        'reportViewer.do?reportBy=getchangeVisual&reportId=' + reportId+"&reportName="+parent.$("#graphName").val()+"&type=advance&visual="+visual,
        function(data) {
            parent.$("#loading").hide();
            if(data=="false"){
                htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">No Graph Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" onclick="editViewBys()">Please Add Graphs from Option above.</h2></span> </div>';
                $("#xtendChartTD").append(htmlVar);
                $("#xtendChartTD").show();
            }
            else{
                var jsondata = JSON.parse(data)["data"];
                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                var meta = JSON.parse(JSON.parse(data)["meta"]);
                var infoMap = JSON.parse(data)["infoMap"];
                $("#viewby").val(JSON.stringify(meta["viewbys"]));
                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                $("#measure").val(JSON.stringify(meta["measures"]));
                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                $("#aggregation").val(JSON.stringify(meta["aggregations"]));
                $("#drilltype").val((meta["drillType"]));
                $("#filters1").val(JSON.stringify(meta["filterMap"]));
                $("#charType").val(meta["chartType"]);
                $("#currType").val(meta["currType"]);
                $("#chartType").val(meta["chartType"]);
                $("#visualChartType").val(JSON.stringify(infoMap));
                $("#isOverlay").val(meta["isOverLay"]);
                $("#isShaded").val(meta["isShaded"]);
                $("#shadeType").val(meta["shadeType"]);
                $("#measureColor").val(JSON.stringify(meta["measureColor"]));
            
                $("#conditionalMap").val((JSON.stringify(meta["conditionalMap"])));
                $("#conditionalMeasure").val(meta["conditionalMeasure"]);
                //Added by shobhit for multi dashboard on 22/09/15 
               parent.$("#parentViewBy").val(meta["parentViewBy"]);
                parent.$("#childViewBys").val(JSON.stringify(meta["childViewBys"]));
                parent.$("#childMeasBys").val(JSON.stringify(meta["childMeasBys"]));
                parent.$("#selectedViewBys").val(JSON.stringify(meta["selectedViewBys"]));
                parent.$("#selectedMeasBys").val(JSON.stringify(meta["selectedMeasBys"]));
                //end
                generateVisual(JSON.parse(jsondata),infoMap);
            }
        });
}


function drillKPIchart(idArr) {
    var id = idArr.split(":");
    var drills = {};
    if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
        drills=JSON.parse($("#drills").val());
    }
    var drillValues = [];
    var chartData = JSON.parse($("#chartData").val());
    drillValues.push(idArr.split(":")[1]);
    drills[idArr.split(":")[0]] = drillValues;
    $("#drills").val(JSON.stringify(drills));
    var infoMap;
    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
        function(data){
        
            var infoMap = JSON.parse(parent.$("#visualChartType").val());
            generateVisual(JSON.parse(data),infoMap);
        });
}

function editNewCharts(chartId){

    //    var chartId="";
    var REPORTID = parent.document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath=parent.document.getElementById("h").value;
    var frameObj = parent.document.getElementById("editViewByFrame");
  

    //    measures1["measures1"] = measure1;
    //    measures2["measures2"] = measure2;

    var chartData = JSON.parse($("#chartData").val());
//    var runtimeMeasure = chartData[chartId]["runtimeMeasure"];
//    alert(JSON.stringify(runtimeMeasure))
    
    //     chartData[chartId]["measures1"] = measure1;
    //     chartData[chartId]["measures2"] = measure2;
    //     chartData[chartId]["measures1"] = measures1;
    //     chartData[chartId]["measures2"] = measures2;

    $("#chartData").val(JSON.stringify(chartData));
    $.post(ctxPath+'/reportViewer.do?reportBy=getObjectMap&REPORTID='+REPORTID+'&ctxPath='+ctxPath+'&chartId='+chartId,$("#graphForm").serialize(),
        function(data){

            //            var source = ctxPath+"/Report/Viewer/editCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId;
            var source = ctxPath+"/Report/Viewer/editNewCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId+"&flag=dualAxis";
            //            var source = ctxPath+"/Report/Viewer/editNewCharts.jsp?REPORTID="+REPORTID+"&ctxPath="+ctxPath+"&chartId="+chartId+"&measureArray="+measureArray+"&measure1="+measure1+"&measure2="+measure2+"columns="+columns;
            frameObj.src = source;
        });
    parent.$("#editViewByDiv").dialog('open');

}

function getChartOPtionUI(chartId,chartType,namex   ){
    $("#chartOPtions").dialog({
        autoOpen: false,
        height: 400,
        width: 320,
        position: 'justify',
        modal: true
    });
    $("#chartOPtions").dialog('open');
    var chartList = ["Bar","Line"];
    var chartData = JSON.parse($("#chartData").val());
    var measures1 = chartData[chartId]["measures1"];
    var measures2 = chartData[chartId]["measures2"];
    var measures = [];
    for(var i in measures1){
        measures.push(measures1[i]);
    }
    for(var j in measures2){
        measures.push(measures2[j]);
    }
    //  measures.push(measures2);
    var html="";
    html+="<div>";
    html+="<table>";
    for(var i=0;i<measures.length;i++){
        html +="<tr>";
        html +="<td>"+measures[i]+"</td><td>";
        html +="<select id='select_"+i+"'>";
        for(var m=0;m<chartList.length;m++){
            html +="<option value='"+chartList[m]+"'>"+chartList[m]+"</option>";
        }
        html +="</select></td>";
        html +="</tr>";

    }
    html +="<tr>";
    html +="<td colspan='2'><input type='button' value='Back' onclick='editNewCharts(\""+chartId+"\")'></td>";
    html +="<td colspan='2'><input type='button' value='Done' onclick='changeFunctionChart(\""+chartId+"\",\""+chartType+"\",\""+name+"\")'></td>";
    html +="</tr>";
    html+="</table>";
    html+="</div>";
    $("#chartOPtions").html(html);

}

function changeFunctionChart(chartId,chartType,name){
    var chartData = JSON.parse($("#chartData").val());
    // var measures = chartData[chartId]["meassures"];
    var chartList = [];
    var measures1 = chartData[chartId]["measures1"];
    var measures2 = chartData[chartId]["measures2"];
    var measures = [];
    for(var i in measures1){
        measures.push(measures1[i]);
    }
    for(var j in measures2){
        measures.push(measures2[j]);
    }
    if(typeof measures === "undefined"){
        measures = chartData[chartId]["meassures"];
    }
    for(var i=0;i<measures.length;i++){
        chartList.push($("#select_"+i).val());
    }
    chartData[chartId]["chartList"]=chartList;
    parent.$("#chartData").val(JSON.stringify(chartData));
    $("#chartOPtions").dialog('close');
    chartTypeFunction(chartId,chartType,name);
}




function getMeasureFilterUI(chartId){
    var chartData = JSON.parse($("#chartData").val());
    //    meassures,meassureIds
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];

    $("#measureFilters").dialog({
        autoOpen: false,
        height: 400,
        width: 320,
        position: 'justify',
        modal: true
    });
    $("#measureFilters").dialog('open');

    var html ="";
    html +="<table>";
    for(var i=0;i<meassureIds.length;i++){
        html +="<tr>";
        html +="<td>";
        html +=measures[i]+"</td>";
        html +="<td><select id='"+measures[i]+"_Select'>";
        html +="<option value='<'><</option>";
        html +="<option value='>'><</option>";
        html +="</select>";
        html +="</td>";
        html +="<td><input id='"+measures[i]+"' type='text'></td>";
        html +="</tr>";
    }
    html +="</table>";
    ("#measureFilters").html(html);
}

function hideLabelSection(idArr,div){
    parent.$("#hideLabel").dialog('open');
    var chartData = JSON.parse($("#chartData").val());
    var measure = chartData[div]["meassures"];
    var measures2 = chartData[div]["measures2"];
    var measureNames = [];
    if(typeof chartData[div]["chartType"] !="undefined" && chartData[div]["chartType"] == "DualAxis-Bar" || chartData[div]["chartType"]==="DualAxis-Target" || chartData[div]["chartType"]==="DualAxis-Group"){
        for(var i in measures2){
            measureNames.push(measures2[i])
        }
    }else {
        for(var i in measure){
            measureNames.push(measure[i])
        }
    }

    var html = "";

    html += "<table>";
    for(var i=0;i<measureNames.length;i++){
        html +="<tr>";
        html +="<td>"+measureNames[i]+"</td><td>";
        html +="<select id='hideLabel_"+i+"_"+div+"'>";

        //        html += "<select id='"+div+"hideLabel' style='font-size:9pt'  name='Dashname'>";
        if (typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"] !=null && chartData[div]["hideLabel"][i] === "Yes") {
            html += "<option value='Yes' selected>Yes</option>";
        }
        else if(typeof chartData[div]["hideLabel"] == "undefined"){
            html += "<option value='Yes' selected>Yes</option>";
        }
        else {
            html += "<option value='Yes'>Yes</option>";
        }

        if (typeof chartData[div]["hideLabel"] != "undefined" && chartData[div]["hideLabel"] !=null && chartData[div]["hideLabel"][i] === "No") {
            html += "<option value='No' selected>No</option>";
        }
        //        else if(typeof chartData[div]["hideLabel"] === "undefined"){
        //              html += "<option value='No' selected>No</option>";
        //        }
        else {
            html += "<option value='No'>No</option>";
        }

        html +="</select></td>";
        html +="</tr>";

    }
    html += " <tr style='height:25px'><td colspan='8' align='center' ><input type='button' name='"+div+"'  onclick='setChartDetails(this.name)' name='hide_submit' value='Done'></td>";
    html += "</tr></table>";
    $("#hideLabel").html(html);
}

function getGraphData(reportId, cntxPath){
    var check = $("#withGraph").is(":checked")

    if($("#withGraph").is(":checked")){
    }else {
        alert("Graph Not Added");
    }

}

function resequenceGraphs(){
    $("#reportSequence").dialog({
        autoOpen: false,
        height: 400,
        width: 350,
        position: 'justify',
        modal: true,
        title: translateLanguage.Delete_Resequence
    });
    var html="";
    var charts = JSON.parse($("#visualChartType").val());
    var keys = Object.keys(charts);
    html += "<ul id='sortable' style='padding-top:0px;'>";
    for (var dir in charts) {
        html += "<li class='btn-custom2' style='background-color: #3CC; margin-bottom: 1px; height: 16px; font-size: 12px; padding: 3px;' id='" + dir + "'   )\">\n\
                <a href='javascript:deleteReport(\"" + dir + "\")' ><img alt='Del'  src='images/sign_cancel.png'height='11px' width='11px' ></a>\n\
<span style='cursor: pointer;width=100%;margin-left:4px;' class='gFontFamily gFontSize12'>" + dir + "</span></li>";
    }
    html += "<li><input type='button' value='"+translateLanguage.Save+"' class='gFontFamily gFontSize12' onclick='changeSequence()'><li>";
    html += "</ul>";
    $("#reportSequence").html("");
    $("#reportSequence").append(html);
    $("#reportSequence").dialog('open');
    setDropable("folderSeq");
}

function resequenceGraphs1(){
    $("#reportSequence").dialog({
        autoOpen: false,
        height: 350,
        width: 350,
        position: 'justify',
        modal: true,
        title: 'Resequence'
    });
    var html="";
    var viewBy = JSON.parse($("#viewby").val());
  
    html += "<ul id='sortable' style='padding-top:0px;'>";
    for (var i=0;i<viewBy.length;i++) {
        html += "<li class='btn-custom3' style='background-color: #3CC; margin-bottom: 1px; height: 16px; font-size: 12px; padding: 3px;' id='" + i + "'   )\">\n\
<span style='cursor: pointer;width=100%;margin-left:4px;'>" + viewBy[i] + "</span></li>";
    }
    html += "</ul>";
    html += "<input type='button' value='Save' onclick='changeSequence1()'>";
    $("#reportSequence").html("");
    $("#reportSequence").append(html);
    $("#reportSequence").dialog('open');
    setDropable("folderSeq");
}

function setDropable(id) {
    var dragging = false;
    $("#sortable li").mouseover(function() {
        $(this).parent().sortable({
            stop: function(event, ui) {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            }
        })
    });
    $("#sortable li").click(function(mouseEvent) {
        var dirList = [];
        if (!dragging) {
            $("#sortable li").each(function(i, el) {
                var p = $(el).attr("id");
                dirList.push(p);
            });
            $.ajax({
                type: 'POST',
                data: {
                    "workDirList": JSON.stringify(dirList)
                    },
                url: parent.nodeListner + 'reorderDirSeq',
                success: function(response) {
                    if (response !== "success") {
                        alert("some error occure");
                    }
                }
            });
        }
    });
}

function deleteReport(report){
    var r=confirm("Permanently Delete "+report);
    if(r==true){
        var LiObj = document.getElementById(report);
        var parentUL = document.getElementById(LiObj.parentNode.id);
        parentUL.removeChild(LiObj);
    }
}

function changeSequence(){
    var newSequence=[];
    $('#sortable li').map(function(i,n) {
        newSequence.push($(n).attr('id'));
    }).get().join(',');
    var newJson = {};
    var charts = JSON.parse($("#visualChartType").val());
    for(var i=0;i<newSequence.length;i++){
        newJson[newSequence[i]]=charts[newSequence[i]];
    }
    $("#visualChartType").val(JSON.stringify(newJson));
    $.ajax({
        type: 'POST',
        data: $("#graphForm").serialize(),
        url:  'reportViewer.do?reportBy=updateSequence',
        success: function(response) {
            if (response !== "success") {
                alert("some error occure");
            }
            else{
                $("#reportSequence").dialog('close');
              
                alert("Visual Saved");
                if(typeof newSequence[0]!=="undefined"){
                getChangeVisual(newSequence[0]);
                }
            }
        }
    });

}
//function changeSequenceparam(){
//    var newSequence=[];
////    $('#sortable li').map(function(i,n) {
////        newSequence.push($(n).attr('id'));
////    }).get().join(',');
//  var ul = parent.document.getElementById("rowViewUL");alert(rowViewUL)
//    if(ul!=undefined || ul!=null){
//                    var colIds=ul.getElementsByTagName("li");
//                    if(colIds!=null && colIds.length!=0){
//                        for(var i=0;i<colIds.length;i++){
//                         newSequence.push(colIds[i]);
//                        }
//                    }
//                        }alert(newSequence)
//    var newJson = [];
//    var newIds = [];
////    var charts = JSON.parse($("#visualChartType").val());
//    var viewBy = JSON.parse($("#viewby").val());
//    var viewId = JSON.parse($("#viewbyIds").val());
//
//    for(var i=0;i<newSequence.length;i++){
//       if(typeof viewBy[newSequence[i]] !=="undefined" && viewBy[newSequence[i]]!=="null"){
//        newJson.push(viewBy[newSequence[i]]);
//        newIds.push(viewId[newSequence[i]]);
//       }
//    }
//
//
////    $("#visualChartType").val(JSON.stringify(newJson));
//    $("#viewby").val(JSON.stringify(newJson));
//    $("#viewbyIds").val(JSON.stringify(newIds));
////     $.ajax({
////        async:false,
////        type:"POST",
////        data:
////            parent.$('#graphForm').serialize(),
////        url:  'reportViewer.do?reportBy=saveXtCharts',
////
////        success:function(data) {
////             $("#reportSequence").dialog('close');
////            alert("Sequence Changed");
//////            generateJsonDataReset($("#graphsId").val());
////        }
////    });
//
//
//}
function changeSequence1(){
    var newSequence=[];
    $('#sortable li').map(function(i,n) {
        newSequence.push($(n).attr('id'));
    }).get().join(',');
    var newJson = [];
    var newIds = [];
//    var charts = JSON.parse($("#visualChartType").val());
    var viewBy = JSON.parse($("#viewby").val());
    var viewId = JSON.parse($("#viewbyIds").val());

    for(var i=0;i<newSequence.length;i++){
       if(typeof viewBy[newSequence[i]] !=="undefined" && viewBy[newSequence[i]]!=="null"){
        newJson.push(viewBy[newSequence[i]]);
        newIds.push(viewId[newSequence[i]]);
       }
    }


//    $("#visualChartType").val(JSON.stringify(newJson));
    $("#viewby").val(JSON.stringify(newJson));
    $("#viewbyIds").val(JSON.stringify(newIds));
     $.ajax({
        async:false,
        type:"POST",
        data:
            parent.$('#graphForm').serialize(),
        url:  'reportViewer.do?reportBy=saveXtCharts&currentPage='+currentPage,

        success:function(data) {
             $("#reportSequence").dialog('close');
            alert("Sequence Changed");
//            generateJsonDataReset($("#graphsId").val());
var repId=$("#graphsId").val();
            submiturlsseq('reportViewer.do?reportBy=viewReport&action=reset&REPORTID='+repId)
        }
    });


}
 
function getMeasureFilterUI(chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];

    $("#measureFilters").dialog({
        autoOpen: false,
        height: 220,
        width: 320,
        position: 'justify',
        modal: true
    });
    $("#measureFilters").dialog('open');
    var measureFilters;
    if (typeof chartData[chartId]["measureFilters"] != "undefined") {
        measureFilters = chartData[chartId]["measureFilters"];
    }
    var html = "";
    html += "<table>";
    for (var i = 0; i < meassureIds.length; i++) {
        var selectedVal;
        html += "<tr>";
        html += "<td>";
        html += measures[i] + "</td>";
        html += "<td><select id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_Select' onchange='inBetween(\""+measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi')+"\",this.value)'>";
        if (typeof measureFilters == "undefined" ||  typeof measureFilters[meassureIds[i]] == "undefined" || measureFilters[meassureIds[i]] != "") {
            selectedVal = "";
            html += "<option value='' selected></option>";
        } else {
            selectedVal = "";
            html += "<option value='' selected></option>";
        }
        if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[i]]!= "undefined" && typeof measureFilters[meassureIds[i]]["<"] != "undefined") {
            selectedVal = measureFilters[meassureIds[i]]["<"];
            html += "<option value='<' selected><</option>";
        } else {
            html += "<option value='<'><</option>";
        }
        if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[i]]!= "undefined" && typeof measureFilters[meassureIds[i]][">"] != "undefined") {
            selectedVal = measureFilters[meassureIds[i]][">"];
            html += "<option value='>' selected>></option>";
        } else {
            html += "<option value='>'>></option>";
        }
        if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[i]]!= "undefined" && typeof measureFilters[meassureIds[i]]["<>"] != "undefined") {
            selectedVal = measureFilters[meassureIds[i]]["<>"];
            html += "<option value='<>' selected><></option>";
        } else {
            html += "<option value='<>'><></option>";
        }
        if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[i]]!= "undefined" && typeof measureFilters[meassureIds[i]]["="] != "undefined") {
            selectedVal = measureFilters[meassureIds[i]]["="];
            html += "<option value='=' selected>=</option>";
        } else {
            html += "<option value='='>=</option>";
        }
        if (typeof measureFilters != "undefined" && typeof measureFilters[meassureIds[i]]!= "undefined" && typeof measureFilters[meassureIds[i]]["!="] != "undefined") {
            selectedVal = measureFilters[meassureIds[i]]["!="];
            html += "<option value='!=' selected>!=</option>";
        } else {
            html += "<option value='!='>!=</option>";
        }
        html += "</select>";
        html += "</td>";
        //    if(typeof measureFilters!="undefined" && typeof measureFilters[meassureIds[i]]!="undefined"){
        //    html +="<td><input id='"+measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi')+"_val' type='text' value='"+measureFilters[meassureIds[i]]+"' /></td>";
        //}else{
        //bar
        // if(typeof selectedVal.split("__")[1]=="undefined"){
        //
        // }
        html += "<td id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valtd'><input style='display:block' id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_val' type='text' value='" + selectedVal + "' /></td>";
        html += "<td><input style='display:none' size='7' id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valFrom' type='text' value='" + selectedVal.split("__")[0] + "' />";
        var val2="";
        if(typeof selectedVal.split("__")[1]!="undefined"){
            val2 = selectedVal.split("__")[1];
        }
        //        else{
        //             html += " <span>&nbsp;And &nbsp; </span>";
        //        }
        html += "<span id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_span' style='display:none;'>&nbsp;And &nbsp; </span> <input style='display:none' size='7' id='" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valTo' type='text' value='" + val2 + "' /></td>";
        //    }
        html += "</tr>";
    }
    html += "<tr>";
    html += "<td colspan='2'  align='right' style='margin-top:.8em'><input type='button' value='Done' onclick='applyMeasureFilters(\"" + chartId + "\")'></td>";
    //Added By Ram
    html += "<td colspan='3' align='center' style='margin-top:.8em'><input type='button' value='Clear Filter' onclick='clearMeasureFilters(\"" + chartId + "\")'></td>";
    html += "</tr>";
    html += "</table>";
    $("#measureFilters").html(html);
}

function applyMeasureFilters(chartId) {
    var chartData = JSON.parse($("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var map = {};
    for (var i = 0; i < measures.length; i++) {
        var symbol = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_Select").val();
        var measureVal;
        if(symbol=="<>"){
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valFrom").val()+"__"+$("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valTo").val();
        }else{
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_val").val();
        }
        var measureMap = {};
        if (symbol != "" && measureVal != "") {
            measureMap[symbol] = measureVal;
            map[meassureIds[i]] = measureMap;
        }
    }
    chartData[chartId]["measureFilters"] = map;
    $("#chartData").val(JSON.stringify(chartData));

    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
        function(data) {
            $("#measureFilters").dialog('close');
            generateSingleChart(data, chartId);
            //Added By Ram
            var html="<tspan style='font-size:15px;color:red;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartId+"</tspan>"
            $("#renameTitle"+chartId).html(html);
            chartData[chartId]["isFilterApplied"]="Yes";
            parent.$("#chartData").val(JSON.stringify(chartData));
        });
}
//Added By Ram
function clearMeasureFilters(chartId) {
    var html= "<option value='' selected></option>";
    $("#measureFilters").html(html);

    var chartData = JSON.parse($("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    var meassureIds = chartData[chartId]["meassureIds"];
    var map = {};
    for (var i = 0; i < measures.length; i++) {
        var symbol = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_Select").val();
        var measureVal;
        if(symbol=="<>"){
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valFrom").val()+"__"+$("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_valTo").val();
        }else{
            measureVal = $("#" + measures[i].replace(/[^a-zA-Z0-9]/g, '', 'gi') + "_val").val();
        }
        var measureMap = {};
        if (symbol != "" && measureVal != "") {
            measureMap[symbol] = measureVal;
            map[meassureIds[i]] = measureMap;
        }
    }
   
 
    chartData[chartId]["measureFilters"] = map;
    $("#chartData").val(JSON.stringify(chartData));

    $.post($("#ctxpath").val() + "/reportViewer.do?reportBy=drillCharts&reportName=" + $("#graphName").val() + "&reportId=" + $("#graphsId").val(), $("#graphForm").serialize(),
        function(data) {
            $("#measureFilters").dialog('close');

            generateSingleChart(data, chartId);
            //Added By Ram
            var html="<tspan style='font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartId+"</tspan>"
            $("#renameTitle"+chartId).html(html);

   
            chartData[chartId]["isFilterApplied"]="NO";
            parent.$("#chartData").val(JSON.stringify(chartData));
        });


}

function editAdvanceVisual(reportId) {
    var REPORTID = document.getElementById("REPORTID").value;
       if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var ctxPath = document.getElementById("h").value;
    var frameObj = document.getElementById("editViewByFrame");
    $.ajax({
        async: false,
        type: "POST",
        data:
        $('#ViewByForm').serialize() + "&REPORTID=" + REPORTID + "&ctxPath=" + ctxPath,
        url: ctxPath + "/reportViewer.do?reportBy=getObjectMap",
        success: function(data) {
            var source = ctxPath + "/Report/Viewer/editAdvance.jsp?REPORTID=" + REPORTID + "&ctxPath=" + ctxPath + "&from=isEdit";
            frameObj.src = source;
        }
    });
    $("#editViewByDiv").dialog('open');
}


//Added by Ram
function getLegends(){
//alert("===getLegends()===")
//    var name='';
//    var chartData = JSON.parse(parent.$("#chartData").val());
//    var chartType = $("#chartType").val();
//    if($("#content_1").is(':visible')){
//    $("#content_1").attr("style","display:none");
//    //alert("===getLegends()1===")
//var nn=$('#legendsDiv').children().length;
// //alert("===getLegends()2===")
//    var htmlStr="<table style='width:100%'>";
//for(var p=0;p<nn;p++){
// var legends = JSON.parse($("#legends"+p).val());
// if(chartType==="Multi-View-Bubble"){
//        name=chartData["chart1"]["viewBys"][p];
//    }else
//  name=chartData["chart"+(p+1)]["viewBys"][0];
//    htmlStr+="<tr  style='text-align:center;color:red'><td style='font-size:12pt'>"+name+"</td></tr>";
//    var keys = Object.keys(legends);
//    for(var i=0;i<keys.length;i++){
//        htmlStr+="<tr><td id="+keys[i].replace(/[^a-zA-Z0-9]/g, '', 'gi')+" onmouseover='setMouseOverEvent(this.id,\"\")' onmouseout='setMouseOutEvent(this.id,\"\")'><canvas width='9' height='9' style='margin-left:5px;margin-right:5px;background:" + legends[keys[i]] + "'></canvas>";
//        htmlStr+=keys[i]+"</td></tr>";
//    }
//}
//    htmlStr+="</table>";
//    $("#content_2").html(htmlStr);
//     $("#content_2").attr("style","display:block; height:380px; overflow-y:scroll");
//    }else{
//    $("#content_1").attr("style","display:block");
//    $("#content_2").attr("style","display:none");
//    }
}
function saveGridposoneview(regionid){
    var chartData = JSON.parse(parent.$("#chartData").val());
  
    var charts;
 
    var chartname1 = parent.$("#chartname").val();
    charts=chartname1;
    //alert(grid.serialize1())
    //var gridsPos=grid.serialize1();
    var gridsPos=grid.serialize1();
    var chartId="";
    // alert("chdata "+$("#chartData").val());
    //alert(gridsPos.length)
    //alert(regionid)
    var k=regionid;
    //for (var k = regionid; k < gridsPos.length; k++) {
    chartId = chartname1;

    chartData[chartId]["id"]=gridsPos[k]["id"];
    chartData[chartId]["row"]=gridsPos[k]["row"];
    chartData[chartId]["col"]=gridsPos[k]["col"];
    chartData[chartId]["size_x"]=gridsPos[k]["size_x"];
    chartData[chartId]["size_y"]=gridsPos[k]["size_y"];

    parent.$("#chartData").val(JSON.stringify(chartData));
//$("#gridPositions").val(JSON.stringify(gridsPos));
//$("#gridPositions").val(JSON.stringify(gridsPos));
//alert("chdata "+$("#chartData").val())

}
//added by manik to save the grid positions
function saveGridpos(){
    var chartData = JSON.parse($("#chartData").val());
    var charts = Object.keys(chartData);
    //var gridsPos=grid.serialize1();
    var gridsPos=grid.serialize1();
    var chartId="";
    // alert("chdata "+$("#chartData").val());

    for (var k = 0; k < charts.length; k++) {
        chartId = "chart" + (parseInt(k) + 1);



        chartData[chartId]["id"]=gridsPos[k]["id"];
        chartData[chartId]["row"]=gridsPos[k]["row"];
        chartData[chartId]["col"]=gridsPos[k]["col"];
        chartData[chartId]["size_x"]=gridsPos[k]["size_x"];
        chartData[chartId]["size_y"]=gridsPos[k]["size_y"];

    //alert("row "+JSON.stringify(chartData[chartId]["row"]))
    //alert("col "+JSON.stringify(chartData[chartId]["col"]))
    }
    ;
    //alert("gridsPos   "+JSON.stringify(gridsPos))
    //chartData[chartId]["gridPositions"]=JSON.stringify(gridsPos);
    //chartData[chartId]["gridPositions"]=gridsPos;
    $("#chartData").val(JSON.stringify(chartData));
//$("#gridPositions").val(JSON.stringify(gridsPos));
//$("#gridPositions").val(JSON.stringify(gridsPos));
//alert("chdata "+$("#chartData").val())

}



function changeMeasureArray(measIndex){
    $("#loading").show();
    timeDBDrill='';
    var chartData = JSON.parse($("#chartData").val());
    var currentGTValueList = chartData["chart1"]["GTValueList"];
    var tempGTValueList=[];
    if($("#chartType").val()==='Trend-Dashboard'){
    tempGTValueList.push(currentGTValueList[measIndex]);
    for(var i in currentGTValueList){
        if(i!=measIndex){
            tempGTValueList.push(currentGTValueList[i]);
        }
    }
    }
    var measures = chartData["chart1"]["meassures"];
    var measuresIds = chartData["chart1"]["meassureIds"];

    var currentMeasure = measures[measIndex];
    var currentMeasureId = measuresIds[measIndex];

    var tempMeasureArray = [];
    var tempMeasureIdArray = [];
    tempMeasureArray.push(currentMeasure);
    tempMeasureIdArray.push(currentMeasureId);
    for(var i=0;i<measuresIds.length;i++){
        if(currentMeasureId!=measuresIds[i]){
            tempMeasureArray.push(measures[i]);
            tempMeasureIdArray.push(measuresIds[i]);
        }
    }
    for(var ch in chartData){
        chartData[ch]["meassures"] = tempMeasureArray;
        chartData[ch]["meassureIds"] = tempMeasureIdArray;
    }
    if($("#chartType").val()==='Trend-Dashboard'){
    chartData["chart1"]["GTValueList"] = tempGTValueList;
    parent.$("#chartData").val(JSON.stringify(chartData));
    }
    if($("#chartType").val()==='Time-Dashboard'){
        var map={};
        var arr=[];
        map[JSON.parse($("#viewbyIds").val())[0]]=arr;
        if(typeof $("#drills").val()=='undefined' || $("#drills").val()==''){
            $("#drills").val(JSON.stringify(map));
        }
        if(typeof $("#filters1").val()=='undefined'){
            $("#filters1").val(JSON.stringify(map));
        }
var viewByIds = parent.$("#viewbyIds").val();
        $.ajax({
            type:"POST",
            data:parent.$("#graphForm").serialize(),
            url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+$("#graphsId").val()+"&measId="+tempMeasureIdArray+"&aggType="+chartData["chart1"]["aggregation"]+"&measName="+JSON.stringify(encodeURIComponent(tempMeasureArray))+"&chartId=chart1"+"&viewbyIds="+viewByIds,
            //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
            success: function(data){
                //                                parent.$("#chartData").val(JSON.stringify(prevChartData));
                chartData["chart1"]["GTValueList"] = JSON.parse(data);
                parent.$("#chartData").val(JSON.stringify(chartData));
            }
        });
    }
 if($("#chartType").val()==='Trend-Dashboard'){   
     var flag = 'viewByChange';
    $("#chartData").val(JSON.stringify(chartData));
        $.ajax({
        async:false,
        type:"POST",
        data:
        $('#graphForm').serialize(),
        url:  $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&changeView="+flag+"&viewChartId=chart1",

        success:function(data) {
            if(typeof parent.$("#type").val() !="undefined" && parent.$("#type").val() == "quick"){
             var jsondata = JSON.parse(data)["data"];
              generateTrendChart(jsondata);
            }else {
                 
                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
            }
        }
        }); 
}
else{
     $("#chartData").val(JSON.stringify(chartData));
        $.ajax({
        async:false,
        type:"POST",
        data:
        $('#graphForm').serialize(),
        url:  $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(),

        success:function(data) {
            if(typeof parent.$("#type").val() !="undefined" && parent.$("#type").val() == "quick"){
             var jsondata = JSON.parse(data)["data"];
              generateTrendChart(jsondata);
            }else {
                 
                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
            }
        }
        }); 
}
}

function inBetween(id,value){

    if(value=="<>"){
        $('#'+id+'_val').hide("");
        $('#'+id+'_valFrom').show();
        $('#'+id+'_valTo').show();
        $('#'+id+'_span').show();
    }else{
        $('#'+id+'_span').hide();
        $('#'+id+'_val').show();
        $('#'+id+'_valFrom').hide();
        $('#'+id+'_valTo').hide();
    }
}

function subTotalTarget(chartId,value){

    var chartData = JSON.parse($("#chartData").val());
    if(value=='Enable' ){

        $('#'+chartId+'targetLane').show();
    }else{
        $('#'+chartId+'targetLane').hide();
    }
}

function colorPicker(chartId){
    var chartData = JSON.parse($("#chartData").val());
    // $('#picker'+chartId).show();
    $('#picker'+chartId).dialog({
        autoOpen: false,
        height: 250,
        width: 247,
        position: 'justify',
        modal: true,
        resizable:false,
        title:'Pick Color'
    });
    $('#picker'+chartId).dialog('open');
    $('#picker'+chartId).colpick({
        flat:true,
        layout:'hex',
        onSubmit:function(hsb,hex,rgb,el,bySetColor) {
            // $('#picker'+chartId).hide();
        //alert(  chartData[chartId]["colorPicker"]);
      chartData[chartId]["colorPicker"] = "#"+hex;
            parent.$("#chartData").val(JSON.stringify(chartData));
            // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
            if(!bySetColor) $(el).val(hex);
            $('#picker'+chartId).dialog('close');
        }

    }).keyup(function(){

        $(this).colpickSetColor(this.value);
    });
}

function checkDial(id,chartId,status){
    if($("#"+id).val()=='no'){
        //        $("#DefineDialChartId").attr('checked',false);
        $("#DefineDialChartSection").hide();
    }else{
        //        $("#DefineDialChartId").attr('checked',true);
        DefineDialChartSection(chartId,status)
    }
}


function DefineDialChartSection(chartId,dialStatus){
    if(typeof dialStatus==='undefined' || dialStatus===''){
        dialStatus=parent.$("#dialSelectChart").val();
    }
    var data = graphData[chartId];
    var chartData = JSON.parse(parent.$("#chartData").val());
    var measures = chartData[chartId]["meassures"];
    //            var aggregation = chartData[chartId]["aggregation"];
    var htmlstr = "<table class=\"\" align= 'left'  style=' overflow-y:auto;width:100%;height:100%' >";
    //    for (var no = 0; no < measures.length; no++) {
    var sum1=0;
//    alert(JSON.stringify(data));
//    
//    $.each(data, function (d) {
//        sum1 += parseInt(data[d][measures[0]]);
//    });
//    //    }
//    sum1 = sum1.toFixed(2);
//    sum1.replace(".00", "");
    sum1=chartData[chartId]["GTValueList"][0];
    if(dialStatus =='yes'){
        $("#DefineDialChartSection").val("");  //// && isChecked
        //        alert('parent.currVlaue'+parent.currVlaue+'  parent.numdays'+parent.numdays)
        //        var currValCal=parseFloat(sum1.replace(",","","gi"));
        ////        alert('parent.currVlaue'+currValCal+'  parent.numdays'+parent.numdays)
        //        var perDay=currValCal/parseInt(parent.numdays);
        //        perDay=(perDay*100);
        //    perDay=Math.round(perDay);
        //    perDay=(perDay/100);
        //        $("#DialChartChangePerVal").val(parent.changValue);
        parent.$("#dialMeasureNameSpan").html(measures[0]);
        parent.$("#dialMeasureValue").val(sum1);
        //        parent.$("#dialMeasureTotalDays").html(parent.numdays);
        //        parent.$("#DialMeasureValuePerDay").val(perDay);
        parent.$("#DefineDialChartSection").show();
        parent.$("#dialMeasureTotalDays").hide();
        parent.$("#dialMeasureTotalDaysTd").hide();
        parent.$("#dialMeasureName").hide();
        parent.$("#DialMeasureValuePerDay").hide();
        parent.$("#dialMeasureTotalDaysTD").hide();
        parent.$("#DialMeasureValuePerDayTd").hide();
    }else{
        //        alert('Please enable Dial-Chart option to define ranges.');
        //        parent.$("#DefineDialChartId").attr('checked',false)
        parent.$("#DefineDialChartSection").hide();
    }
}

function reflectValueGraph(id,nextIndex){
    var val=$("#"+id).val();
    var nextlemId=$("#range"+(nextIndex)+"_1");
    if(nextlemId != 'undefined' && nextlemId !=null){
        $('input[id="range'+(nextIndex)+'_1"]').val(val);
    //        nextlemId.attr("readonly", "readonly");
    }
}

function Tilealignment(chartId,value){
    $("#"+chartId+"Tilealign").val(value);
}

function addImageTag(chartId){

    $("#imageTagDiv").dialog({
        autoOpen: false,
        height: 503,
        width: 650,
        position: 'justify',
        modal: true,
        resizable:true,
        title:translateLanguage.Select_Your_Image
    });
    $("#imageTagDiv").html("");

    $("#imageTagDiv").dialog('open');

    var html = "";

    html += "<table><tr>";
    html += '<td><input id="filename" name="filename" type="file" required style="background-color:lightgoldenrodyellow; color:black;"/>';
    html += "</td></tr>";
    html +=  " <tr style='height:25px'><td colspan='4' align='center' ><input type='button' name='"+chartId+"' class='navtitle-hover gFontFamily gFontSize12' onclick='setImageDetails(this.name)' name='sorting_submit' value='"+translateLanguage.Done+"'></td>";
    html += "</table>";

    $("#imageTagDiv").html(html);
}

function setImageDetails(chartId){
    var chartData =  JSON.parse(parent.$("#chartData").val());
    if(typeof $("#filename").val()!="undefined" && typeof $("#filename").val() != ""){
        chartData[chartId]["filename"] = $("#filename").val();
    }else {
        alert ("No File Selected")
    }
    $("#chartData").val(JSON.stringify(chartData));
    $("#imageTagDiv").dialog('close');
    //        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
    //            function(data){
                   graphData[chartId]=JSON.parse(data)[chartId];
    //                chartTypeFunction(chartId,chartType,chartName);
    //            });
    changeChart(chartId,chartData[chartId]["chartType"]);
}

function applyFilterToTrend1(reportId,viewId, dimName,filterList){
    $("#openGraphSection").dialog({
        autoOpen: false,
        height: 200,
        width: 200,
        position: 'justify',
        modal: true,
        resizable:true,
        title:'Select Section'
    });
    //      
    
    $("#openGraphSection").dialog('open');
    $("#openGraphSection").html("");
    var html = "";
    html += "<div> <table>";
    html += "<tr><td><input id='quickSection' name='Section' type='radio' value='quickSection'>Quick Trend</input></td></tr><br>";
    html += "<tr><td><input id='graphSection' name='Section' type='radio' value='graphSection'>Graph Section</input></td></tr>";
    html += "<tr><td><input id='trendSection' name='Section' type='radio' value='trendSection'>Trend Section</input></td></tr>";
    
    html += "<tr></tr>";
    html += "<tr><td><input type='button' onclick='selectGraphSection(\""+reportId+"\")' value='Done'/>"
    html += "</table></div>";
    $("#openGraphSection").html(html);  
    
    viewByFilterID = viewId;
    viewByFilterName = dimName;
    fList = filterList;

}
function selectGraphSection(reportId){


    if($('#graphSection').val() == "graphSection"){

        $('#themeselect').empty();
        $('#saveGraphLi').remove();
        var add = "add";
        var edit = "isEdit";
        var html1 = "";
        //               var graphFlag = "Graph";
        html1 += '<ul class="drpcontent" id="themeselect">';
        html1 += "<li style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='editSingleCharts()' >Add Graph Object</a>";
        html1 +="</li>";
        html1 += "<li style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\""+add+"\")' >Add Graph</a>";
        html1 +="</li>";
        html1 += "<li style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='buildCharts(\""+edit+"\")' >Edit Graph</a>";
        html1 +="</li>";

        html1 += "<li id='saveWithGraph' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save All Graphs</a>";
        html1 +="</li>";
        html1 += "<li id='regenerateFilters' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='regenerateFilters("+parent.$("#graphsId").val()+")' >Regenerate Filters</a>";
        html1 +="</li>";
        html1 +="</li>";
        html1 +="<li>";
        html1 +="<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
        html1 +="</li>";
        html1 += "<li id='resetId' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='generateJsonDataReset("+parent.$("#graphsId").val()+")' >Reset</a>";
        html1 +="</li></ul>";
        $('#testCase').append(html1);
    }
    if(typeof $('#trendSection').val() !=="undefined" && $('#trendSection').val() == "trendSection"){
        $('#themeselect').empty();
        $('#saveGraphLi').remove();
        $('#regenerateFilters').remove();
        $('#resetId').remove();
        $('#saveWithGraph').remove();
        var html1 = "";
        html1 += '<ul class="drpcontent" id="themeselect">';
        html1 += "<li style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' id ='addGraphDataTrendAnalysis' onclick='buildChartsInTrends("+parent.$("#graphsId").val()+")' >Add Trend Charts</a>";
        html1 +="</li>";
        html1 += "<li id='saveWithGraph' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='saveXtCharts()' >Save Trend Charts</a>";
        html1 +="</li>";
        html1 +="<li>";
        html1 +="<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='OpenDefTabs()'>Default Tab</a>";
        html1 +="</li>";
        html1 += "<li id='reset' style='text-align;'>";
        html1 += "<a data-color='color1' style='padding:10px 0px 0px 24px;' onclick='trendAnalysisActionJsNew("+parent.$("#graphsId").val()+")' >Reset</a>";
        html1 +="</li>";

        $('#testCase').append(html1);

    }
    if(document.getElementById("trendSection").checked){
        $("#footerTable").css({
            'display':'block'
        });
        var trendFlag = "Trend";

        $("#gridsterDiv").css({
            'display':'block'
        });
        $("#xtendChartssTD").css({
            'display':'none'
        });
        $("#menuBarUITR").css({
            'display':'none'
        });

        $("#footerTable").css({
            'display':'none'
        });
        $('#xtendChartTD').hide();
        $("#content_1").show();
        //        $("#loading").show();
       
        //        applyFilterFromReport(viewByFilterID,viewByFilterName);
        trendAnalysisActionJs(reportId,trendFlag,gridster1);
    }else if(document.getElementById("graphSection").checked){
        var graphFlag = "Graph";
        fromdataanlysis="true"
//         $('#globalfilterrow').show();
        generateJsonData1st(reportId,graphFlag,gridster1);
    }   else {
        $("#openGraphSection").dialog('close');
        
 $('#rightDiv1table').hide();
 if (checkBrowser() == "ie") {
     
                   $('#rightDiv1trend').hide();
                   $("#xtendChartssTD").css({'margin-top':'0px'});
             }else{
 $('#rightDiv1trend').show();
             }

        var graphFlag = "Graph";
        generateQuickTrend(reportId,graphFlag);
    }
}

//sandeep
function updateglobalfilters(reportId,flag1,type){

  var viewOvName = JSON.parse($("#viewby").val());
        var viewOvIds = JSON.parse($("#viewbyIds").val());
  var filterValues=[];
  var viewbyid;
        for (var key in viewOvName) {
var elementname=viewOvName[key].replace("1q1", " ");

 var filters=filterData[elementname];
 filterValues=filterData[elementname];
 var viewByFilterName1;
 if(flag1=="advance"){
     var drills = {};
            var grpKeys;

                drills = JSON.parse($("#drills").val());
                grpKeys = Object.keys(drills);
             for(var i=0;i<(grpKeys.length);i++){
                viewByFilterName1= drills[grpKeys[i]];
                viewbyid=grpKeys[i].replace("A_", "");
             }

 }else{
     viewbyid=viewByFilterID.replace("A_", "");
     viewByFilterName1=viewByFilterName;
 }
var size=100;
if(size>filters.length){
    size=filters.length;
}
var html="";
if(viewbyid==viewOvIds[key]){
               if(viewByFilterName1!=null && viewByFilterName1!=""){
  var index= filterValues.indexOf(viewByFilterName1);
     var value=viewByFilterName1.replace("'", " ")+"_"+index+"_"+viewOvIds[key];
    value=value+"_selecttrue";
 html = html + "<option  value='"+value+"'>"+viewByFilterName1+"</option>";
  }
for(var i=0; i<size;i++){
    var flag="true";
     var value=filters[i].replace("'", " ")+"_"+i+"_"+viewOvIds[key];
 if(viewByFilterName1!=null && viewByFilterName1!=""){
          var valuefilter=filters[i].replace("]", "").replace("[", "");


if(valuefilter==viewByFilterName1){
    value=value+"_selecttrue";

//<option value="<%=value%>" ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
     flag="false";

     }
if(flag=="true"){
 html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";

                         flag="false";
}


  }else{
      html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";
        // <option value="<%=value%>"  ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>
     }
}

}else{
for(var i=0; i<size;i++){
    var flag="true";
     var value=filters[i].replace("'", " ")+"_"+i+"_"+viewOvIds[key];
value=value+"_selecttrue";
      html = html + "<option  value='"+value+"'>"+filters[i].replace("]", "").replace("[", "")+"</option>";
        // <option value="<%=value%>"  ><%=parameterlistNames.get(i).toString().replace("]", "").replace("[", "")%></option>

}
}
//
//
//        }
if(type=="trend"){
    var elementname1="Tr"+elementname;
    elementname=elementname1.replace(" ", "1q1");
       $("#"+elementname).html(html);
    $("#"+elementname1).multiselectTrend('refresh');
}else{
      elementname=elementname.replace(" ", "1q1");
       $("#"+elementname).html(html);
    $("#"+elementname).multiselect('refresh');
}
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
        }
}
function applyFilterFromReport(id,nameArr,filterList){
    $("#loading").show();
    var filterMap = {};
    //        var filterKeys = Object.keys(filterData);
    var filterValue = [];
    var name,id ;
    id = id.split("A_")[1];
    name = nameArr.split("*,")[0];
   
    filterValue = filterList;
    var index = filterValue.indexOf(name);
    if(index != -1){
        filterValue.splice(index,1);
    }
    filterMap[id]=filterValue;
    if(typeof $("#filters1").val()!=="undefined" && $("#filters1").val()!==""){
        $("#filter1").val("");
    }
    reportToTrendFilter  = filterMap;

    $("#filters1").val(JSON.stringify(reportToTrendFilter));
    $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
        function(data){
            var chartData = JSON.parse($("#chartData").val());
               var jsondata = JSON.parse(data)["data"];
             for(var t in chartData){
                 if(chartData[t]["chartType"]=="KPI-Table" || chartData[t]["chartType"]=="Table" ||chartData[t]["chartType"]=="Expression-Table" ||chartData[t]["chartType"]=="Emoji-Chart" ||chartData[t]["chartType"]=="Stacked-KPI" ||chartData[t]["chartType"]=="KPIDash"  ||chartData[t]["chartType"]=="Bullet-Horizontal" ||chartData[t]["chartType"]=="Standard-KPI" ||chartData[t]["chartType"]=="Standard-KPI1" ||chartData[t]["chartType"]=="Radial-Chart"||chartData[t]["chartType"]=="LiquidFilled-KPI" ||chartData[t]["chartType"]=="Dial-Gauge" ||chartData[t]["chartType"]=="Emoji-Chart"){
                  $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]))
                    }
                }    
           
            if(parent.$("#type").val()==="advance"){
                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
            }else{
                generateChart(jsondata);
            }
        });
}

function drillUpGraph(chartId,id){
    var chartData =  JSON.parse(parent.$("#chartData").val());

    var splitId = id.split('#');

    if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
        var drills;
        drills = JSON.parse($("#drills").val())
        var drillkeys = Object.keys(drills)
        var drillUpMap = {}
        var drillUpArray = [];
        var i=0;
        var drillacross = [] ;
        var count = 1;
        for(i=0;i<(drillkeys.length);i++){
//        alert("**"+drillkeys[i].toString()+"**")
      // alert("**"+splitId.toString()+"**")
        if (drillkeys[i].toString()===splitId[1].toString())  {
        
         break;
        }else{
        drillUpArray.push(JSON.stringify(drills[drillkeys[i]]))
        drillUpMap[drillkeys[i]] = drills[drillkeys[i]];
        count++;
        }
        }
        drills = drillUpMap
//        for(var j=i;j<drillkeys.length;j++){
//            // alert(j)
//           delete drills[drillkeys[j]];
//
//        }

        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
        
        for(var l in chartData[chartId]["dimensions"]){
            for(var k in viewOvIds){
                if(chartData[chartId]["dimensions"][l] == viewOvIds[k] ){
                   
                    if(viewOvIds[k] == chartData[chartId]["viewIds"][0]){
                        var viewIdsArr = []
                        var viewBysArr = []

                        for(var s in viewOvIds) {
                          

                            if(viewOvIds[s]==drillkeys[i]){
                                viewIdsArr.push(viewOvIds[s])
                                viewBysArr.push(viewOvName[s])
                            }
                        }
                       
                        chartData[chartId]["viewIds"] =  viewIdsArr
                        chartData[chartId]["viewBys"] =  viewBysArr
                   }
                }
            }
        }
    
            $("#drills").val(JSON.stringify(drills));
            $("#chartData").val(JSON.stringify(chartData));
                $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
            function(data){
              //alert(data);
               if(parent.$("#type").val()==="advance"){
                   generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
               }else{

                generateSingleChart(data,chartId);
               }
            });
          }else{
        alert("No more viewby's to drill up")
    }

}

function drillUp(chartId){
    var chartData =  JSON.parse(parent.$("#chartData").val());
    var chartDataDrillsMap = {};
    chartDataDrillsMap["drills"] = $("#drills").val();
    if(typeof $("#drills").val()!="undefined" && $("#drills").val()!=""){
        var drills;
        drills = JSON.parse($("#drills").val())
        var drillkeys = Object.keys(drills)

        var drillUpMap = {}
        //    for(var i=0;i<(drillkeys.length-1);i++){
        //       drillUpArray.push(drills[drillkeys[i]])
        //       }
        var drillUpArray = [];
        for(var i=0;i<(drillkeys.length-1);i++){

            drillUpArray.push(JSON.stringify(drills[drillkeys[i]]))

            drillUpMap[drillkeys[i]] = drills[drillkeys[i]];
        //      drillUpMap[drills[drillkeys[i]]] = drillkeys[i];
        }
        drills = drillUpMap
        var viewOvName = JSON.parse(parent.$("#viewby").val());
        var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
   
        for(var l in chartData[chartId]["dimensions"]){
            for(var k in viewOvIds){
                if(chartData[chartId]["dimensions"][l] == viewOvIds[k] ){
                    if(viewOvIds[k] == chartData[chartId]["viewIds"]){
                        var viewIdsArr = []
                        var viewBysArr = []

                        for(var s in viewOvIds) {
                            if(viewOvIds[s]==chartData[chartId]["dimensions"][l-1]){
                                viewIdsArr.push(viewOvIds[s])
                                viewBysArr.push(viewOvName[s])
                            }
                        }
                        chartData[chartId]["viewIds"] =  viewIdsArr
                        chartData[chartId]["viewBys"] =  viewBysArr
                    }
                }
            }
        }
        if(typeof chartData[chartId]["viewIds"]!="undefined" && chartData[chartId]["viewIds"]!=""){}else{
            alert("No more viewby's to drill up")
        }
        $("#chartData").val(JSON.stringify(chartData));
        var drillKeys = Object.keys(drills)
        if(drillKeys.length<1){
            $("#drills").val("");
        }else{
            $("#drills").val(JSON.stringify(drills));
        }
        $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){
                //                        generateChart(data,div);
                  if(parent.$("#type").val()==="advance"){
                      var keys = Object.keys(drills);
                      breadcrumpid = [];
                      for(var key in keys){
                      breadcrumpid.push(JSON.stringify(drills[keys[key]][0]).split(",")[0].replace(/["']/g, ""))
                      }
                    
                                generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
                            }else{
                generateSingleChart(data,chartId);
                            }
            });
    }else{
        alert("No more viewby's to drill up")
    }

}

function pickColor(chartId,id,index){
    var chartData = JSON.parse($("#chartData").val());
    var customColors= chartData[chartId]["customColors"];
    //    alert("#####"+JSON.stringify(customColors));
    $('#custom_color_picker1').dialog({
        autoOpen: false,
        height: 450,
        width: 285,
        position: 'justify',
        modal: true,
        resizable:false,
        title:'Pick Color'
    });
    var html = '';
    var defaultColor=$('#'+id).css("background-color"); 
    defaultColor=hexc(defaultColor);
    var color = d3.scale.category10();
    html += '<table>';
    var c=0;
    for(var i=0;i<10;i++){
        html += '<tr>';
        for(var j=0;j<10;j++){
            html += '<td style="padding:2px 2px 2px 2px">';
            html += "<input id='color"+(c)+"' type='color picker' onclick='selectColor(\""+color(c)+"\")' style='width:20px;background-color:"+color(c)+"' readonly />";
            c++;
            html += '</td>';
        }
        html += '</tr>';
    }
    html += '</table>';
    html += '<div style="width:100%;height:60px">';
    //    html += '<div style="width:100%;height:20%">';
    //    html += '<span style="font: 11px verdana;font-size:9pt">Selected Color</span>';
    //    html += '</div>';
    html += '<div id="selectedColor" style="margin-top:5px;border-radius:10px;background-color:'+defaultColor+';width:100%;height:80%"/>';
    html += '</div>';
    html += '</div>';
    html += '<div style="width=100%">';
    html += "<input style='float:left' type='button' onclick='saveColor(\""+chartId+"\",\""+id+"\")' value='Done'>";
    html += "<input style='float:right' type='button' onclick='moreColors(\""+chartId+"\",\""+id+"\")' value='More Colors'>";
    html += '</div>';
    
    $('#custom_color_picker1').html(html);
    $('#custom_color_picker1').dialog('open');
//    $('#_picker'+chartId).colpick({
//        flat:true,
//        layout:'hex',
//        onSubmit:function(hsb,hex,rgb,el,bySetColor) {
////            alert("###"+chartId);
//            //        alert("###"+colorPickerId);
//            $('#'+colorPickerId).css('background-color', '#'+hex);
//          //  $('#_picker'+chartId).hide();
//            if(typeof chartData[chartId]["customColors"] !='undefined')
//            {
//                chartData[chartId]["customColors"][colorPickerId]="#"+hex;
//            }
//            else
//            {
//                var color={};
//                color[colorPickerId]="#"+hex;
//                chartData[chartId]["customColors"]=color;
//            }
//         
//            parent.$("#chartData").val(JSON.stringify(chartData));
//            // Fill the text box just if the color was set using the picker, and not the colpickSetColor function.
//            if(!bySetColor) $(el).val(hex);
//              $('#_picker'+chartId).dialog('close');
//        }
//
//    }).keyup(function(){
//
//        $(this).colpickSetColor(this.value);
//    });
}

function selectColor(color){
    //    alert(color);
    //    $("#"+id).css('box-shadow', '10px 10px 5px #888');
    //    $("#"+id).animate({ 'zoom': 1.2 }, 400);
    selectedColor=color;
    $("#selectedColor").css('background-color', color);
}

function saveColor(chartId,colorPickerId){
    //    alert(colorPickerId);
    var chartData=JSON.parse($("#chartData").val());
    var color1 = $("#selectedColor").css('backgroundColor');
      var gradientLogicalMap={};// add by mayank sharma for map scale color
      gradientLogicalMap["measure"]=chartData[chartId]["meassures"][0];
      gradientLogicalMap[chartData[chartId]["meassures"][0]]=color1;
      chartData[chartId]["gradientLogicalMap"]=gradientLogicalMap;// end by mayank sharma
    hexc(color1);
    $('#'+colorPickerId).css('background-color', color1);
    //    $('#_picker'+chartId).hide();
    if(typeof chartData[chartId]["customColors"] !='undefined')
    {
        chartData[chartId]["customColors"][colorPickerId]=color1;
    }
    else
    {
        var color={};
        color[colorPickerId]=color1;
        chartData[chartId]["customColors"]=color;
    }
    //        alert(selectedColor);
    parent.$("#chartData").val(JSON.stringify(chartData));
    $('#custom_color_picker1').dialog('close');
}

function draganddrop(dragId,dragIdArray1,id){
    parent.$("#loading").show();
    var dragIdFlag = [];
    var divId = id.substring(3, id.length)
    for(var d in dragIdArray1 ){
        if(dragIdArray1[d].indexOf("divchart")>-1){
            dragIdFlag.push(dragIdArray1[d]);
        }
    }

    if(dragIdFlag.length>0 && dragIdArray1.length>1){
        parent.dragIdArray = [];
    }else{
        var chartDetails ={};
        var viewIds = [];
        var viewBys = [];
        var MeasBy = [];
        var MeasIds = [];
        var AggType = [];
        var viewByIds = JSON.parse($("#viewbyIds").val());
        var viewBy = JSON.parse($("#viewby").val());
        var chartData = {};
        if(typeof parent.$("#chartData").val()!="undefined" && parent.$("#chartData").val()!=""){
            chartData =  JSON.parse(parent.$("#chartData").val());
        }

        if(typeof parent.$("#draggableViewBys").val()!="undefined" && parent.$("#draggableViewBys").val()!="" && parent.$("#draggableViewBys").val()!="[]"){
            var dragCharts = JSON.parse(parent.$("#draggableViewBys").val());
            draggableViewBys = [];
            for(var g in dragCharts){
                draggableViewBys.push(dragCharts[g]);
            }

        }else if(typeof chartData!="undefined" && chartData!=""){
            var keys = Object.keys(chartData)
            for(var key in keys){
                viewBys.push(chartData[keys[key]]["viewBys"])
                viewIds.push(chartData[keys[key]]["viewIds"])

                draggableViewBys.push(chartData[keys[key]]["viewBys"]);
            }
        }
        for(var i in viewBy){
            if(viewBy[i]==dragId){
                viewIds.push(viewByIds[i])
                viewBys.push(viewBy[i])
                draggableViewBys.push(viewBy[i]);
  
            }
        }
        var Measby = JSON.parse($("#measure").val())
        var measureIds = JSON.parse($("#measureIds").val())
        var aggregation = JSON.parse($("#aggregation").val())
        var measFlag = "false";
        for(var m in Measby ){
            if(Measby[m] ==dragId){
                MeasBy.push(Measby[m])
                MeasIds.push(measureIds[m])
                AggType.push(aggregation[m])
                measFlag = "True";
            }
        }
        if(MeasBy.length<1){
            MeasBy.push(Measby[0])
            MeasIds.push(measureIds[0])
            AggType.push(aggregation[0])
        }
        if(viewBys.length<1){
            viewBys.push(viewBy[0])
            viewIds.push(viewByIds[0])
            draggableViewBys.push(viewBy[0]);
        }

        parent.$("#draggableViewBys").val(JSON.stringify(draggableViewBys));
        //   for(var j=0;j<draggableViewBys.length;j++){
        chartDetails["viewBys"] = viewBys;
        //                            if(measFlag=="True"){
        //                            chartDetails["chartType"] = "KPIDash";
        //                            }else{
                         
        if(parent.$("#type").val()=="graph"){
                            
                            
            chartDetails["chartType"] = "Pie";
        }else{
            chartDetails["chartType"] = "Line";
        }
        //                            }
        chartDetails["viewByLevel"] = "single";
        chartDetails["meassures"] = MeasBy;
        chartDetails["aggregation"] = AggType;
        chartDetails["viewIds"] = viewIds;
        chartDetails["meassureIds"] = MeasIds;
        chartDetails["dimensions"] = viewIds;
         chartDetails["records"] = "12";
                             
        chartDetails["size"] = "S";
        chartData["chart" + (draggableViewBys.length)] = chartDetails;
       var id = "chart" + (draggableViewBys.length);
       //   }
        parent.$("#chartData").val(JSON.stringify(chartData));
//        var reportId  = parent.document.getElementById("reportId").value;
        $.ajax({
            type:'POST',
            data:parent.$("#graphForm").serialize() +"&reportId="+$("#graphsId").val()+"&chartFlag=true"+"&chartID="+id+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val()+"",
            url: $("#ctxpath").val()+"/reportViewer.do?reportBy=buildchartsWithObject",
            success: function(data){
                //                     parent.generateChart(data,"drag");

                generateChartloop(data,"drag");
                parent.$("#loading").hide();
            }
        })
    }
}
function draganddrop1(dragId,id){
    parent.$("#loading").show();
    var divIndex=parseInt(id.replace("divchart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
    var chartData = JSON.parse(parent.$("#chartData").val());
    var rowViewByArray=new Array();
    var rowViewNamesArr = [];
    var rowMeasArray=new Array();
    var divId = id.substring(3, id.length)
    var MeasBy = [];
    var viewIds = [];
    var viewBys = [];
    var MeasBy = [];
    var MeasIds = [];
    var AggType = [];
    var dimension = [];
    var viewIdContains = [];
    var measureIdContains = [];
    var chartDetails=chartData[divId];
    var viewByIds = JSON.parse($("#viewbyIds").val());
    var viewBy = JSON.parse($("#viewby").val());
    var Measby = JSON.parse($("#measure").val())
    var measureIds = JSON.parse($("#measureIds").val())
    var aggregation = JSON.parse($("#aggregation").val())
    if(parent.$("#type").val()!="graph" && (dragId.toLowerCase().trim()=="month" || dragId.toLowerCase().toString().trim()=="qtr" || dragId.toLowerCase().toString().trim()=="year" || dragId.toLowerCase().toString().trim()=="time" || dragId.toLowerCase().toString().trim()=="month year" || dragId.toLowerCase().toString().trim()=="qtr year")){
        // if(parent.$("#type").val()!="graph" && (dragId.toLowerCase().toString().indexof("month")!=-1 || dragId.toLowerCase().toString().indexof("qtr")!=-1 || dragId.toLowerCase().toString().indexof("year")!=-1 || dragId.toLowerCase().toString().indexof("time")!=-1)){
        for(var i in viewBy){
            if(viewBy[i]==dragId){
                viewIds.push(viewByIds[i])
                viewBys.push(viewBy[i])
                dimension.push(viewByIds[i]);
                viewIdContains.push(viewByIds[i])

            }
        }
    }else if(parent.$("#type").val()!="graph"){
        if(viewBys.length<1){
            viewBys.push(chartData[divId]["viewBys"][0])
            viewIds.push(chartData[divId]["viewIds"][0])
            viewIdContains.push(chartData[divId]["viewIds"][0])
            for(var k in chartData[divId]["dimensions"]) {
                dimension.push(chartData[divId]["dimensions"][k]);
            }


        }else{

        }
    }
    else{
        for(var i in viewBy){
            if(viewBy[i]==dragId){
                viewIds.push(viewByIds[i])
                viewBys.push(viewBy[i])
                dimension.push(viewByIds[i]);
                viewIdContains.push(viewByIds[i])

            }
        }
    }

    // for measureby's
    for(var m in Measby ){
        if(Measby[m] ==dragId){
            MeasBy.push(Measby[m])
            MeasIds.push(measureIds[m])
            AggType.push(aggregation[m])
            measureIdContains.push(measureIds[m])

        }
    }
    if(typeof chartData[divId]["meassureIds"]!="undefined" && chartData[divId]["meassureIds"]!="" ){
        for(var z in chartData[divId]["meassureIds"]){
            if(chartData[divId]["meassureIds"][z]!=measureIdContains){
                MeasBy.push(chartData[divId]["meassures"][z])
                MeasIds.push(chartData[divId]["meassureIds"][z])
                AggType.push(chartData[divId]["aggregation"][z])
            }
        }
    }else{
        MeasBy.push(Measby[0])
        MeasIds.push(measureIds[0])
        AggType.push(aggregation[0])
    }

    //end of measureBy's'
    if(parent.$("#type").val()!="graph" && (dragId.toLowerCase().trim()=="month" || dragId.toLowerCase().toString().trim()=="qtr" || dragId.toLowerCase().toString().trim()=="year" || dragId.toLowerCase().toString().trim()=="time" || dragId.toLowerCase().toString().trim()=="month year" || dragId.toLowerCase().toString().trim()=="qtr year")){
        // if(parent.$("#type").val()!="graph" && (dragId.toLowerCase().indexof("month")!=-1 || dragId.toLowerCase().indexof("qtr")!=-1 || dragId.toLowerCase().indexof("year")!=-1 || dragId.toLowerCase().indexof("time")!=-1)){
        if(typeof chartData[divId]["dimensions"]!="undefined" && chartData[divId]["dimensions"]!=""){
            for(var t in dimension){
                for(var k in chartData[divId]["dimensions"]){
                    if(dimension[t]!= chartData[divId]["dimensions"][k]){
                        dimension.push(chartData[divId]["dimensions"][k]);
                    }
                }


            }
        }
    }else if(parent.$("#type").val()!="graph"){
        for(var i in viewBy){
            if(viewBy[i]==dragId){
                dimension.push(viewByIds[i]);
            }
        }
    }else{
        if(viewBys.length<1){
            viewBys.push(chartData[divId]["viewBys"][0])
            viewIds.push(chartData[divId]["viewIds"][0])
            dimension.push(chartData[divId]["viewIds"][0]);
        }else{
            if(typeof chartData[divId]["viewIds"]!="undefined" && chartData[divId]["viewIds"]!="" && (  chartData[divId]["chartType"]=="Grouped-Bar" || chartData[divId]["chartType"]=="GroupedHorizontal-Bar" || chartData[divId]["chartType"]=="Scatter-Bubble" || chartData[divId]["chartType"]=="Split-Bubble" || chartData[divId]["chartType"]=="Grouped-Line" || chartData[divId]["chartType"]=="GroupedStackedH-Bar"|| chartData[divId]["chartType"]=="GroupedStacked-Bar") ){
                for(var u in chartData[divId]["viewIds"]){
                    if(JSON.stringify(chartData[divId]["viewIds"][u])!=JSON.stringify(viewIdContains[0])){
                        viewIds.push( chartData[divId]["viewIds"][u])
                        viewBys.push( chartData[divId]["viewBys"][u])
                        dimension.push(chartData[divId]["viewIds"][u]);
                        break;
                    }
                }
                }
        if(typeof chartData[divId]["viewIds"]!="undefined" && chartData[divId]["viewIds"]!="" && chartData[divId]["chartType"]=="Table" || chartData[divId]["chartType"]=="Transpose-Table"){
            var dimensions1 = [];
            for(var u in chartData[divId]["viewIds"]){
                if(JSON.stringify(chartData[divId]["viewIds"][u])!=JSON.stringify(viewIdContains[0])){
                    viewIds.push( chartData[divId]["viewIds"][u])
                    viewBys.push( chartData[divId]["viewBys"][u])
                    dimension.push(chartData[divId]["viewIds"][u]);
                }
            }

        }
    }
    if(typeof chartData[divId]["dimensions"]!="undefined" && chartData[divId]["dimensions"]!="" ){
        for(var t in dimension){
            for(var k in chartData[divId]["dimensions"]){
                if(dimension.indexOf(chartData[divId]["dimensions"][k])==-1){
                    if(dimension[t]!= chartData[divId]["dimensions"][k]){
                        dimension.push(chartData[divId]["dimensions"][k]);
                    }
                }
            }
        }
    }
}


//dimension code


// end of dimension code
chartDetails["viewBys"] = viewBys;
chartDetails["viewByLevel"] = "single";
chartDetails["meassures"] = MeasBy;
chartDetails["viewIds"] = viewIds;
chartDetails["meassureIds"] = MeasIds;
chartDetails["aggregation"] = AggType;
chartDetails["dimensions"] = dimension;
chartDetails["records"] = "12";
chartDetails["KPIName"] = MeasBy[0];
chartData[divId] = chartDetails;
var currChartData={};

currChartData[divId] = chartDetails;
var prevChartData = chartData;
parent.$("#chartData").val(JSON.stringify(prevChartData));
var reportId  ="";
try{
 
   reportId= parent.document.getElementById("reportId").value;
}catch(e){
    reportId = parent.$("#graphsId").val();
}
 
if(chartData[divId]["chartType"]=="KPI-Table" || chartData[divId]["chartType"]== "DualAxis-Target"  ||chartData[divId]["chartType"]=="Expression-Table" ||chartData[divId]["chartType"]=="Emoji-Chart" ||chartData[divId]["chartType"]=="Stacked-KPI" || chartData[divId]["chartType"]=="KPIDash" || chartData[divId]["chartType"]=="Bullet-Horizontal" ||chartData[divId]["chartType"]=="Standard-KPI" ||chartData[divId]["chartType"]=="Standard-KPI1" ||chartData[divId]["chartType"]=="Radial-Chart"||chartData[divId]["chartType"]=="LiquidFilled-KPI" ||chartData[divId]["chartType"]=="Dial-Gauge" ||chartData[divId]["chartType"]=="Emoji-Chart" ||chartData[divId]["chartType"]=="Trend-KPI"){
 var viewByIds = parent.$("#viewbyIds").val();
    //  var dataValue =   GTValue(divId,reportId,MeasIds[0],AggType[0],MeasBy[0])
    //  alert(dataValue)
    //  chartData[divId]["GTValue"] = dataValue
    var prevChartData = chartData;
    $.ajax({
        type:"POST",
                        data:parent.$("#graphForm").serialize(),
                        url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+reportId+"&measId="+MeasIds+"&aggType="+AggType+"&measName="+JSON.stringify(encodeURIComponent(MeasBy))+"&chartId="+divId+"&viewbyIds="+viewByIds,
        //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
        success: function(data){
            //                                parent.$("#chartData").val(JSON.stringify(prevChartData));
        
           $.ajax({

        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url: $("#ctxpath").val()+"/reportViewer.do?reportBy=buildchartsWithObject&reportId="+$("#graphsId").val()+"&reportName="+$("#graphName").val()+"&chartData="+encodeURIComponent($("#chartData").val()),
        success: function(data1){

      
		graphData[divId]=JSON.parse(data1)[divId]
                                chartData[divId]["GTValueList"] = JSON.parse(data);
            parent.$("#chartData").val(JSON.stringify(chartData));
            $("#chart_loading"+divIndex).hide();
            parent.$("#loading").hide();
            chartTypeFunction(divId,chartData[divId]["chartType"],chartData[divId]["KPIName"],data)
        } })    }
    })
}else{
    $.ajax({
        type:"POST",
        data:parent.$("#graphForm").serialize(),
        url: $("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val()+"&action=localfilterGraphs"+"&chartFlag=true"+"&chartID="+divId,
        success: function(data){
         
            parent.$("#chartData").val(JSON.stringify(prevChartData));
            $("#chart_loading"+divIndex).hide();
            parent.$("#loading").hide();
                  var chartData = JSON.parse(parent.$("#chartData").val());
       var completeViewbyIds = viewIds;
      var completeViewby = viewBys;
//       var completeViewbyIds = JSON.parse($("#viewbyIds").val());
//      var completeViewby = JSON.parse($("#viewby").val());
      var viewbys22 = [];
      var viewbyIds22 = [];
      var dimensions = chartData[divId]["dimensions"];

//      for(var i in dimensions){
//      for(var j in completeViewbyIds){
//
//      if(dimensions[i]==completeViewbyIds[j])  {
//          viewbys22.push(completeViewby[j])
//          viewbyIds22.push(completeViewbyIds[j])
//      }
//      }
//      }
//Added by shivam
       var  QuickFilterid="viewbys_"+divId+"_"+viewBys[0]+"_"+viewIds[0]
      if(typeof  chartData[divId]["QuickFilterValue"]!="undefined"){

    showQuickFilter(QuickFilterid)
      }
            generateSingleChart(data,divId);

        }

    }
    )
}

}
if (checkBrowser() == "ie") {
function showHideFilter(){
    $("#content_1").show();
    $("#content_Drag").hide();
}}
if (checkBrowser() == "ie") {
function showDragBox(){
    $("#content_1").hide();
    $("#content_Drag").show();
}}
if (checkBrowser() == "ie") {
function getFilters(){
   
    $("#content_2").toggle();
    $("#content_1").toggle();
}}

function deleteChart(chartId){
    if(!confirm("Are you sure you want to delete this chart?")){
        return false;
    }
    var chartData = JSON.parse(parent.$("#chartData").val());
    var keys = Object.keys(chartData);
    if (typeof keys !=="undefined" && keys.length >1){ 
        delete chartData[chartId];
  
        var index = chartId.replace("chart","");
        var ch = 1;
        if(typeof chartData[chartId] =="undefined" && chartId !=="chart1" && chartId !=="chart"+keys.length){
            for(var i=(parseInt(index)+1) ; i <=keys.length ; i++){
                if(ch ==1){
                    ch = parseInt(index);
                }
            
                var div = "chart"+ch;
       
                chartData[div] = chartData["chart"+ i];
     
                chartData[div]["id"] = "divchart"+ch;
     
                ch++;
   
            }
            delete chartData["chart"+ (keys.length)];
    
        }else {
            for(var i=(parseInt(index)+1) ; i <= keys.length ; i++){
                
                var div = "chart"+ch;
                    
                chartData[div] = chartData["chart"+ i];
                chartData[div]["id"] = "divchart"+ch;
                  
     
                ch++;
            }
            delete chartData["chart"+ (keys.length)];
        }
        var anchorChart=chartData["chart1"]["anchorChart"];
        if(typeof anchorChart!='undefined' && Object.keys(anchorChart).length!=0){
            var anchorChartList=anchorChart[Object.keys(anchorChart)[0]];
            try{
                anchorChartList.splice(anchorChartList.indexOf(chartId),1);
            }catch(e){
            }
            try{
                if(Object.keys(anchorChart)[0]===chartId)
                chartData["chart1"]["anchorChart"]={};
            }
            catch(e){
            }
        }
        parent.$("#chartData").val(JSON.stringify(chartData));
        parent.$("draggableViewBys").val('');
        var chartD = JSON.parse(parent.$("#chartData").val());
        //      var keyLength = Object.keys(chartD);
        var viewBy = [];
        for(var key in chartD ){
          
            viewBy.push(chartD[key]["viewBys"][0]);
        }
        parent.$("#loading").show();
        parent.$("#draggableViewBys").val(JSON.stringify(viewBy));
        var ctxpath=document.getElementById("h").value;
        ;
        $.ajax({
            type:'POST',
            data:parent.$("#graphForm").serialize() +"&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+parent.$("#chartData").val()+"",
            url: ctxpath+"/reportViewer.do?reportBy=buildchartsWithObject",
            //                          data:parent.$("#graphForm").serialize(),
            //                     url: ctxpath+"/reportViewer.do?reportBy=buildchartsWithObject&reportId="+$("#graphsId").val()+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&chartData="+encodeURIComponent(parent.$("#chartData").val()),
            success: function(data){
                parent.$("#loading").hide();
                parent.generateChart(data);
            }
        }
        )
                    
    }else{
        alert("You can not delete this graph");
    }               
    
}

function callReportLevelGraph(repId){
    parent.$("#chartData").val("");
    $("#quickTrend").dialog({
        autoOpen: false,
        height: 300,
        width: 850,
        position: 'justify',
        modal: true,
        resizable:true,
        title:translateLanguage.Report_Analysis,
        // Added By Ram for filter issue
        close: function( event, ui ) {
            $("#drills").val('');
            $("#driverList").val('');
        //      fList=[];
        //      chartCount=0;
      
        }
    });
    // $('#addUnderConsideration').html('');
    //   $("#openGraphSection").dialog('close');
    $("#xtendChartTD").html("");
    //    $("#reportTD1").hide();
    $("#type").val("reportGraph");
    $("#drills").val("");
    $("#filters1").val("");
    //    graphTrendFlag = flag;
    //                       $("#xtendChartTD").hide();
    //                       $("#xtendChartTD").show();
    var htmlVar = "";
    $.post(
        //            'reportViewer.do?reportBy=getAvailableTrend&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
        //        'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+encodeURIComponent(parent.$("#graphName").val()),
        'reportViewer.do?reportBy=generateJsonData&reportId=' + repId+"&reportName="+encodeURIComponent(parent.$("#graphName").val())+"&type="+$("#type").val(),
        function(data) {
            //              alert("data "+data)
            if(data=="false"){
                $("#loading").hide();

                htmlVar+='<div style="background-color: #eee; height: 750px;cursor: pointer" "><span><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" name = "addGraphDataTrendAnalysis" onclick="editSingleTrend(this.name)">No Trend Available for this Report.</h2><h2 style="font-family: cursive; color: #870E30; font-size: large;text-align: center;cursor: pointer" name = "addGraphDataTrendAnalysis" onclick="editSingleTrend(this.name)">Please Add Trend from Option above.</h2></span> </div>';

                $("#addUnderConsideration").append(htmlVar);
                $("#content_1").hide();
                $("#xtendChartTD").append(htmlVar);
                htmlVar="";
                $("#xtendChartTD").show();
            }
            else{
                $("#loading").hide();
                var jsondata = JSON.parse(data)["chartData"];

                var meta = JSON.parse(JSON.parse(data)["meta"]);
             
                var viewbys = JSON.parse(data)["viewBys"];
                var measures = JSON.parse(data)["measures"];
                var measuresIds = JSON.parse(data)["measuresIds"];
                var viewOvIds = JSON.parse(data)["viewIds"];

                //                if(typeof meta["viewbys"]!="undefined" && meta["viewbys"].length>0){
                $("#viewby").val(JSON.stringify(viewbys));
                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
                $("#measure").val(JSON.stringify(meta["measures"]));
                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
                var chartData = {};
                var draggableViewBys = [];
                for(var i=0;i<viewOvIds.length;i++){
                    var chartDetails={};
                    var viewBys=[];
                    var viewIds=[];
                    //                        var meassures=[];
                        
                    viewIds.push(viewOvIds[i]);
                    //                        for(var m=0;m<viewIds.length;m++){
                    viewBys.push(viewbys[i]);
                    //                        }
                    draggableViewBys.push(viewbys[i]);
                    if(viewOvIds.length>1){
                        chartDetails["viewBys"] = viewbys;
                    }else{
                        chartDetails["viewBys"] = viewBys;
                    }
                    if(i==0){
                        chartDetails["chartType"] = "Vertical-Bar";
                    }else{
                        chartDetails["chartType"] = "grouped-bar";
                    }
                    chartDetails["viewByLevel"] = "single";
                    chartDetails["meassures"] = measures;
                    //                            chartDetails["aggregation"] = AggType;
                    if(viewOvIds.length>1){
                        chartDetails["viewIds"] = viewOvIds;
                    }else{
                        chartDetails["viewIds"] = viewIds;
                    }
                        
                    chartDetails["meassureIds"] = measuresIds;
                    chartDetails["records"] = "12";
                    chartDetails["size"] = "S";
                    chartData["chart" + (i + 1)] = chartDetails;
                }
                
                                 
                                 
                parent.$("#draggableViewBys").val(JSON.stringify(draggableViewBys));

                parent.$("#chartData").val(JSON.stringify(chartData));

                chartTypeFunctionForReport(jsondata);


           
            }
        });
}


function chartTypeFunctionForReport(jsondata){
    var chartData = JSON.parse(parent.$("#chartData").val());
    var keys  =  Object.keys(chartData);
    var record = 0;
    if(keys.length > 1){
        record = 50;
    }else {
        record = 12;
    }
    var currData = [];
    for(var i=0; i<(jsondata.length < record ? jsondata.length : record );i++){
        currData.push(jsondata[i]);
    }

    $("#quickTrend").dialog('open');
    for(var i in keys){
        $("#quickTrend").html('');
        var id = parseInt(i);
        var divId = "Hchart" + (id+1);
        var div = "chart" + (id+1)  ;
        var divAppend = divId;
        var html = '';
        html +="<div id='"+divId+"' style='display:block;float:left;width:100%'></div>";
        html += "<div class='tooltip' id='my_tooltip1' style='display: none'></div>";
        $("#quickTrend").html(html);
        if(chartData[div]["chartType"]=="Vertical-Bar"){
            var width = parseInt("500");
            var height = parseInt("300");
            buildQuickBar(div, currData, chartData[div]["viewBys"], chartData[div]["meassures"],width,height);
        }else {
            var width = parseInt("600");
            var height = parseInt("350");
            buildGroupedBar(div, currData, chartData[div]["viewBys"], chartData[div]["meassures"],width,height,divAppend);
        } 
    
    }
}
function GTValue(divId,reportId,MeasIds,AggType,MeasBy){
    var chartData = JSON.parse(parent.$("#chartData").val());
    var value = "";
   var viewByIds = parent.$("#viewbyIds").val();
     var prevChartData = chartData;
    $.ajax({
        async:false,
        type:"POST",
            data:parent.$("#graphForm").serialize(),
                        url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+reportId+"&measId="+MeasIds+"&aggType="+AggType+"&measName="+JSON.stringify(encodeURIComponent(MeasBy))+"&chartId="+divId+"&viewbyIds="+viewByIds,
        //                     url: $("#ctxpath").val()+"/reportViewer.do?reportBy=editCharts&rowViewByArray="+encodeURIComponent(viewIds)+"&reportId="+reportId+"&rowViewNamesArr="+encodeURIComponent(viewIds)+"&rowMeasArray="+encodeURIComponent(MeasIds)+"&rowMeasNamesArr="+JSON.stringify(MeasBy)+"&chartData="+encodeURIComponent(JSON.stringify(currChartData))+"&reportName="+parent.$("#graphName").val()+"&isEdit=Y"+"&editId="+divId,
        success: function(data){
//    $.ajax({
//       type:"POST",
//            data:parent.$("#graphForm").serialize(),
//            url:$("#ctxpath").val() +"/reportViewer.do?reportBy=GTKPICalculateFunction&repId="+reportId+"&measId="+MeasIds+"&aggType="+AggType+"&measName="+JSON.stringify(encodeURIComponent(MeasBy))+"&chartData="+encodeURIComponent(JSON.stringify(chartData))+"&chartId="+divId,
//        //         url: ctxPath+"/reportViewer.do?reportBy=getViewBys",
//        success:function(data){
                chartData[divId]["GTValueList"] = JSON.parse(data);
            parent.$("#chartData").val(JSON.stringify(chartData));
                value = JSON.parse(data)

        }
    });
    return value
}
function moreColors(chartId,colorPickerId){
    flag=colorPickerId;
    $('#colorpicker1').farbtastic('#customColor1');
    $("#colorsDiv1").dialog({
        bgiframe: true,
        autoOpen: false,
        height:300,
        width: 300,
        modal: true,
        Cancel: function() {
            $(this).dialog('close');
        }
    });
    $("#colorsDiv1").dialog('open');
}
function saveSelectedColor1()
{
    var userColor=$("#customColor1").css('background-color');
    userColor = hexc(userColor);
    var temp=flag.substr(0, flag.length-1);
    var chartData=JSON.parse(parent.$("#chartData").val());//chartId+"dataColor"+i
    if(flag=='logicalHighcolor' || flag=='chartNameColol' || flag=='logicalMidcolor' || flag=='logicalLowcolor' || flag=='HeadingBackGround' || flag=='HeadingColor' || flag=='TableFontColor'||flag=='fillBackground'||flag=='titleColor'||flag=='FilledColor4' ||flag=='FilledColor1' ||flag=='FilledColor2' ||flag=='FilledColor3' || flag=='lbcolor' || flag=='AxisNameColor'|| flag=='radialDefaultColor' || flag=='lFilledFont' || flag=='stackDark' || flag=='stackLight' || flag=='divBackGround' || flag=='_colorPicker'|| flag=='bulletAbovecolor'|| flag=='bulletBelowcolor'||flag=='bulletHighcolor'||flag=='bulletMediumcolor'||flag=='bulletLowcolor'||flag=='labelColor' ||flag=='labelColors' ||flag=='gradShade'||temp=='LabFtColor'||temp==='measureColor'|| temp==='dataColor' ||flag.endsWith('_fontColor' )){
        $('#'+flag).css('background-color', userColor);
    }
    else{
        $('#selectedColor').css('background-color', userColor);
        parent.document.getElementById('selectedColor').style.backgroundColor = userColor; 
    }
    if(flag=='gradShade'){
        $('#'+flag).val(userColor);
        chartData["chart1"]["colorPicker"]=userColor;
        $("#chartData").val(JSON.stringify(chartData));
    } 
    $("#colorsDiv1").dialog('close');
}
function hexc(colorval) {
    var parts = colorval.match(/^rgb\((\d+),\s*(\d+),\s*(\d+)\)$/);
    delete(parts[0]);
    for (var i = 1; i <= 3; ++i) {
        parts[i] = parseInt(parts[i]).toString(16);
        if (parts[i].length == 1) parts[i] = '0' + parts[i];
    }
    var color = '#' + parts.join('');
    return color;
}
function openQuickFilter(chartId){
      var chartData = JSON.parse($("#chartData").val());
      var completeViewbyIds = JSON.parse($("#viewbyIds").val());
      var completeViewby = JSON.parse($("#viewby").val());
      var viewbys = [];
      var viewbyIds = [];
      var dimensions = chartData[chartId]["dimensions"];
      for(var i in dimensions){
      for(var j in completeViewbyIds){

      if(dimensions[i]==completeViewbyIds[j])  {
          viewbys.push(completeViewby[j])
          viewbyIds.push(completeViewbyIds[j])
      }
      }
      }
     var html="";
    html = html + "<ul id='more"+chartId+"' class='dropdown-menu'>";
    for(var i=0;i<viewbys.length;i++)
    {
        html+="<li id='viewbys_"+chartId+"_"+viewbys[i]+"_"+viewbyIds[i]+"' onclick='showQuickFilter12(this.id)' class='dropdown-submenu pull-left1'><a class='gFontFamily gFontSize13' >"+viewbys[i]+"</a>";
    }
      html+="<li id='viewbys_"+chartId+"' onclick='hideQuickFilter(this.id)' class='dropdown-submenu pull-left1'><a class='gFontFamily gFontSize13' >"+translateLanguage.Hide_Quick_Filter+"</a>";
    html+="</ul>";
//    $("#more"+chartId).remove();()
//     $("#chartGroups"+suffix+chartId+ index).append(html);
   $("#quickFilter"+ chartId).append(html);
}
//Added by shivam
function showQuickFilter12(Id){
   var chartData = JSON.parse($("#chartData").val());
   var idParam =  Id.split("_");
   chartData[idParam[1]]["QuickFilterShowHide"]="Yes";
   parent.$("#chartData").val(JSON.stringify(chartData));
   showQuickFilter(Id);
}
function showQuickFilter(Id){
    try{
   var htmlvar = ""
   var idParam =  Id.split("_");
   var filterKeys = Object.keys(filterData)
    $("#quickFilterDiv"+idParam[1]).html('');
     var chartData = JSON.parse($("#chartData").val());
   var quickFiltersValue = [];
   var quickFilterLength = 0;
  chartData[idParam[1]]["QuickFilterId"] = Id
   if(typeof chartData[idParam[1]]["QuickFilterValue"]=="undefined"){
     chartData[idParam[1]]["QuickFilterValue"] = {};

   }
      parent.$("#chartData").val(JSON.stringify(chartData));
   if(typeof chartData[idParam[1]]["quickFilterLength"]!="undefined"&& chartData[idParam[1]]["quickFilterLength"]!=""){
      quickFilterLength =chartData[idParam[1]]["quickFilterLength"];
   }else{
      quickFilterLength = 5;
   }
for(var i in filterKeys){
  if(filterKeys[i]==idParam[2]){

quickFiltersValue.push(filterData[filterKeys[i]])
  }
}

htmlvar+="<div style='width:100%'>";
var All = "";
var filteredValue = All+"_"+idParam[3];
htmlvar+="<a id='All_"+idParam[1]+"' onclick='FilterValueFun(this.id,\""+filteredValue+"\",\""+idParam[1]+"\",\""+idParam[3]+"\",\""+idParam[2]+"\")' class='myButton1'>All</a>"
if(typeof quickFiltersValue!=="undefined"){
for(var j=0;j<(quickFiltersValue[0].length<quickFilterLength?quickFiltersValue[0].length:quickFilterLength);j++){
filteredValue = quickFiltersValue[0][j]+"_"+idParam[3];

  htmlvar+="<a id='"+quickFiltersValue[0][j].replace(/ /g,'')+"_"+idParam[3]+"_"+idParam[1]+"' onclick='FilterValueFun(this.id,\""+filteredValue+"\",\""+idParam[1]+"\",\""+idParam[3]+"\",\""+idParam[2]+"\")' class='myButton'>"+quickFiltersValue[0][j]+"</a>"
}
}
htmlvar+="</div>";
//Added by shivam
    if(typeof chartData[idParam[1]]["QuickFilterShowHide"]!="undefined" && chartData[idParam[1]]["QuickFilterShowHide"]!="" && chartData[idParam[1]]["QuickFilterShowHide"]!="No"){
 $("#quickFilterDiv"+idParam[1]).append(htmlvar);
}
 if(typeof chartData[idParam[1]]["QuickFilterValue"]!="undefined" && chartData[idParam[1]]["QuickFilterValue"]!=""){
var QuickFilterKeys = Object.keys(chartData[idParam[1]]["QuickFilterValue"]);

for(var i in QuickFilterKeys){

 var quickFilterName = chartData[idParam[1]]["QuickFilterValue"][QuickFilterKeys[i]].toString().split(",")
for(var j in quickFilterName){
    $("#All_"+idParam[1]).removeClass("myButton1");
$("#All_"+idParam[1]).addClass("myButton");
 $("#"+quickFilterName[j]+"_"+idParam[3]+"_"+idParam[1]).removeClass("myButton");
$("#"+quickFilterName[j]+"_"+idParam[3]+"_"+idParam[1]).addClass("myButton1");

}
}
}else{

$("#All_"+idParam[1]).removeClass("myButton");
$("#All_"+idParam[1]).addClass("myButton1");
}
    }catch(e){}
}

var globalChangeId = ""

var globalChartId = ""
function FilterValueFun(id,fetched,chartId,viewIds,viewBys){
     
    var divIndex=parseInt(chartId.replace("chart", ""));
    var h=$("#divchart"+divIndex).height();
    var w=$("#divchart"+divIndex).width();
    var top=(h/2)-25;
    var left=(w/2)-25;
    $("#chart"+divIndex).html("<div id='chart_loading' style='position:absolute;top:"+top+"px;left:"+left+"px;display:block;z-index: 99;background-color: #fff;opacity: 0.7;'><img id='loading-image' width='50px' src='"+$("#ctxpath").val()+"/images/chart_loading.gif' alt='Loading...' /></div>");
    var quickFilter = fetched.split("_");
    var className = $('#'+id).attr('class');
     var chartData = JSON.parse($("#chartData").val());
    var quickFiltersValue = [];
     var quickFilterLength = 0;
   if(typeof chartData[chartId]["quickFilterLength"]!="undefined"&& chartData[chartId]["quickFilterLength"]!=""){
      quickFilterLength =chartData[chartId]["quickFilterLength"];
   }else{
      quickFilterLength = 5;
   }

    if(typeof chartData[chartId]["multiselecctQuickFilter"]!="undefined" && chartData[chartId]["multiselecctQuickFilter"]!="" && chartData[chartId]["multiselecctQuickFilter"]!="No"){
       if("All_"+chartId == id){
         var filterKeys = Object.keys(filterData)
        for(var i in filterKeys){
        if(filterKeys[i]==viewBys){
        quickFiltersValue.push(filterData[filterKeys[i]])
  }
}
        for(var j=0;j<(quickFiltersValue[0].length<quickFilterLength?quickFiltersValue[0].length:quickFilterLength);j++){
        $("#"+ quickFiltersValue[0][j].replace(" ","")+"_"+viewIds+"_"+chartId).removeClass("myButton1");
        $("#"+ quickFiltersValue[0][j].replace(" ","")+"_"+viewIds+"_"+chartId).addClass("myButton");
        }
        }else{
        $("#All_"+chartId).removeClass("myButton1");
        $("#All_"+chartId).addClass("myButton");
        }
        $("#"+id).removeClass("myButton");
        $("#"+id).addClass("myButton1");

    }else{
   if(globalChartId==""){
     globalChartId =chartId;
    }else if(globalChartId ==chartId){
   $("#"+ globalChangeId).removeClass("myButton1");
    $("#"+globalChangeId).addClass("myButton");
    }else{
        $("#All_"+chartId ).removeClass("myButton1");
$("#All_"+chartId ).addClass("myButton");
//        if(id!="All_"+chartId){
        var filterKeys = Object.keys(filterData)
        for(var i in filterKeys){
  if(filterKeys[i]==viewBys){
quickFiltersValue.push(filterData[filterKeys[i]])
  }
}
        for(var j=0;j<(quickFiltersValue[0].length<quickFilterLength?quickFiltersValue[0].length:quickFilterLength);j++){
        $("#"+ quickFiltersValue[0][j].replace(" ","")+"_"+viewIds+"_"+chartId).removeClass("myButton1");
$("#"+ quickFiltersValue[0][j].replace(" ","")+"_"+viewIds+"_"+chartId).addClass("myButton");
        }globalChartId =chartId;

}
    $("#"+id).removeClass("myButton");
    $("#"+id).addClass("myButton1");
    globalChangeId = id;
    }

    var quickFilterArr = []
    var quickFilterArr2 = []

    var quickFilterMap1 = {}
    var quickFilterMap = {}
    var quickFilterMap2 = {}

if(typeof chartData[chartId]["multiselecctQuickFilter"]!="undefined" && chartData[chartId]["multiselecctQuickFilter"]!="" && chartData[chartId]["multiselecctQuickFilter"]!="No"){

   if(typeof chartData[chartId]["QuickFilterValue"]!="undefined" && chartData[chartId]["QuickFilterValue"]!="" && "All_"+chartId != id){

       quickFilterMap1 = chartData[chartId]["QuickFilterValue"]
       quickFilterMap = chartData[chartId]["QuickFilterValue"]
       var filterKeys = Object.keys(chartData[chartId]["QuickFilterValue"])
       for(var q in filterKeys){
       for(var t in quickFilterMap1[filterKeys[q]]){

        quickFilterArr.push(quickFilterMap1[filterKeys[q]][t])
       }
        quickFilterMap[filterKeys[q]] = quickFilterArr
       }

    if(quickFilter[0]!=""){
        if(quickFilterArr.length>0){
           for(var j in quickFilterArr){
     if(quickFilterArr[j].indexOf(quickFilter[0])==-1){
    quickFilterArr.push(quickFilter[0])
    }
    }
        }else{
      quickFilterArr.push(quickFilter[0])
        }
         var temp1=[];
  label:for(i=0;i<quickFilterArr.length;i++){
        for(var j=0; j<temp1.length;j++ ){//check duplicates
            if(temp1[j]==quickFilterArr[i])//skip if already present
               continue label;
        }
        temp1[temp1.length] = quickFilterArr[i];
  }
   var quickFiltersValue3 = [];
         var filterKeys = Object.keys(filterData)
        for(var i in filterKeys){
        if(filterKeys[i]==viewBys){
        quickFilter//sValue.push(filterData[filterKeys[i]])
        quickFiltersValue3.push(filterData[filterKeys[i]])
  }
}

 for(var v in temp1){
    var quickFiltersValue4 = []
for(var x in quickFiltersValue3[0]){

if(temp1[v].toString()!=quickFiltersValue3[0][x].toString()){
  quickFiltersValue4.push(quickFiltersValue3[0][x])
}
 }
quickFiltersValue3[0]=[]
 for(var y in quickFiltersValue4){
quickFiltersValue3[0].push(quickFiltersValue4[y]);

 }
}
//for checking of duplicates in javascript array



    quickFilterMap[quickFilter[1]] = quickFiltersValue3[0]
    quickFilterMap2[quickFilter[1]] = temp1
    chartData[chartId]["QuickFilterValue"] = quickFilterMap2
    chartData[chartId]["filters"] = quickFilterMap;
    }
   } else{
      if(quickFilter[0]!=""){
    quickFilterArr.push(quickFilter[0])
    quickFilterMap[quickFilter[1]] = quickFilterArr
    chartData[chartId]["QuickFilterValue"] = quickFilterMap
    chartData[chartId]["filters"] = quickFilterMap;
    }else{
          quickFilterArr = []
    chartData[chartId]["QuickFilterValue"] =   {}
    chartData[chartId]["filters"] = {}
    }
   }
}else{
     var quickFiltersValuesingle = [];
         var filterKeys = Object.keys(filterData)
        for(var i in filterKeys){
        if(filterKeys[i]==viewBys){
        quickFilter//sValue.push(filterData[filterKeys[i]])
        quickFiltersValuesingle.push(filterData[filterKeys[i]])
  }
}
    if(quickFilter[0]!=""){
        var tempArr =[]
        for(var x in quickFiltersValuesingle[0]){
          if(quickFilter[0].toString()!=quickFiltersValuesingle[0][x].toString()){
          tempArr.push(quickFiltersValuesingle[0][x])
          }
        }

    quickFilterArr.push(quickFilter[0])
    quickFilterMap2[quickFilter[1]] = tempArr
    quickFilterMap[quickFilter[1]] = quickFilterArr
    chartData[chartId]["QuickFilterValue"] = quickFilterMap
    chartData[chartId]["filters"] = quickFilterMap2;
    }else{
     chartData[chartId]["QuickFilterValue"] = quickFilterMap
    chartData[chartId]["filters"] = quickFilterMap;
    }
    }
var others = "";
    if($("#"+chartId+"_othersL").prop("checked")){
        others="Y";
    }else{
        others="N";
    }
    var globalfilter = "";
    if($("#"+chartId+"_globalfilter").prop("checked")){
        globalfilter="Y";
    }else{
        globalfilter="N";
    }
    chartData[chartId]["othersL"] = others;
    chartData[chartId]["globalEnable"] = globalfilter;
//    alert(quickFilterArr)
//    alert(JSON.stringify(quickFilterMap))
//       var filterKeys = Object.keys(filterData)
//        for(var i in filterKeys){
//        if(filterKeys[i]==viewBys){
//        for(var j in filterData[filterKeys[i]]){
//         alert(filterData[filterKeys[i]][j]+":"+quickFilter[0])
//        if(filterData[filterKeys[i]][j].indexOf(quickFilter[0])!=-1){
//        }else{
//            if(quickFilter[0]!=""){
//alert("first"+quickFilterArr2)
//          if(typeof quickFilterArr2!="undefined" && quickFilterArr2!=""){
//        for(var k in quickFilterArr2) {
//           alert(quickFilterArr2[k]+":"+filterData[filterKeys[i]][j])
//        if(quickFilterArr2[k].indexOf(filterData[filterKeys[i]][j])==-1){
//
//        } else{
//
//    quickFilterArr2.push(filterData[filterKeys[i]][j])
//        }
//        }
//            }
//            else{alert("else"+quickFilterArr2)
//                quickFilterArr2.push(filterData[filterKeys[i]][j])
//            }
//}
//    }
//        }
//        quickFilterMap2[quickFilter[1]] = quickFilterArr2
////    chartData[chartId]["QuickFilterValue"] = quickFilterMap2
////        chartData[chartId]["filters"]
//
//
////        quickFiltersValue.push(filterData[filterKeys[i]])
//  }
//}
// chartData[chartId]["filters"] = quickFilterMap2;
//alert(JSON.stringify(chartData[chartId]["QuickFilterValue"]))
//alert("hhh"+JSON.stringify(chartData[chartId]["filters"]))
var drillKeys = Object.keys(chartData[chartId]["QuickFilterValue"]);
           var viewOvName = JSON.parse(parent.$("#viewby").val());
                var viewOvIds = JSON.parse(parent.$("#viewbyIds").val());
           for(var key in drillKeys){
              var quickViewname = viewOvName[viewOvIds.indexOf(drillKeys[key])];
           
             if(quickViewname.toString().trim() == "Month Year" || quickViewname.toString().trim() == "Month-Year" || quickViewname.toString().trim() == "Month - Year" || quickViewname.toString().trim() == "Month") {
                parent.$("#drillFormat").val("time");
             }else if(quickViewname.toString().trim() == "Qtr" || quickViewname.toString().trim() == "Time" || quickViewname.toString().trim() == "qtr" || quickViewname.toString().trim() == "Qtr Year" || quickViewname.toString().trim() == "Year"){
                 parent.$("#drillFormat").val("time");
             }else {
                parent.$("#drillFormat").val("none");
             }
             }
             var reportId  = parent.document.getElementById("reportId").value;
     parent.$("#chartData").val(JSON.stringify(chartData));
     $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+reportId+"&chartID="+chartId+"&chartFlag=true"+$("#graphsId").val()+"&action=localfilterGraphs", $("#graphForm").serialize(),
 //  $("#drills").val(JSON.stringify(quickFilterMap));
   //   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){

       generateSingleChart(data,chartId);
            });

}
function changeAdvChart(repId,chartType){
    var chartData = JSON.parse($("#chartData").val());
    chartData["chart1"]["chartTypeBarTrend"] = chartType;
       parent.$("#chartData").val(JSON.stringify(chartData));
       $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
        function(data){
             generateVisual(JSON.parse(data),JSON.parse(parent.$("#visualChartType").val()));
//    getAdvanceVisuals(repId)
        })
}
function hideQuickFilter(id){
var splitId = id.split("_");
var chartId = splitId[1];

var chartData = JSON.parse($("#chartData").val());
if(typeof chartData[chartId]["QuickFilterValue"]=="undefined"){
chartData[chartId]["QuickFilterValue"] = {};
}
 $("#quickFilterDiv"+chartId).html("");
chartData[chartId]["QuickFilterShowHide"] = "No";
 parent.$("#chartData").val(JSON.stringify(chartData));
}


//sandeep for display "today" date in calander
function setdatetype(datetyper){
    parent.datetype=datetyper;
}
function settodayDate1(date){
    if(date!="" && date !=="null" && date!=null){
    var tdate=date.toString().substring(0, 15);
    var dates=new Array()
    dates=tdate.split(" ");
  
    var todaydate=dates[0]+","+dates[2]+","+dates[1]+","+dates[3];
if(parent.datetype=="perioddate"){
    
    parent.$("#perioddate").val(todaydate);
      var a;
                                    a=(parent.$("#perioddate").val());
                                    var dateArr=new Array()
                                    dateArr=a.split(",");
                                    if(a!=""){
                                        parent.$("#pfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                                        parent.$("#pfield2").html(dateArr[1])
                                parent.$("#pfield3").html(dateArr[0])
                                    }
                                    parent.dateclick("perioddate")
}else if(parent.datetype=="fromdate"){
     parent.$("#fromdate").val(todaydate);
var a;
                        a=($("#fromdate").val());
                        datetype="fromdate";
                        var dateArr=new Array()
                        dateArr=a.split(",");
                        if(a!=""){
                            $("#field1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                            $("#field2").html(dateArr[1])
                    $("#field3").html(dateArr[0])
                        }
 parent.dateclick("fromdate")
}else if(parent.datetype=="todate"){
     parent.$("#todate").val(todaydate);
 var b;
                        b=($("#todate").val());  datetype="todate";
                        var dateArr=new Array()
                        dateArr=b.split(",");
                        if(b!=""){
                            $("#tdfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                            $("#tdfield2").html(dateArr[1])
                            $("#tdfield3").html(dateArr[0])
                        }
                        parent.dateclick("todate")
}else if(parent.datetype=="comparefrom"){
    parent.$("#comparefrom").val(todaydate);
var a;
                        a=($("#comparefrom").val());
                        var dateArr=new Array()
                        dateArr=a.split(",");
                        if(a!=""){
                            $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                            $("#cffield2").html(dateArr[1])
                            $("#cffield3").html(dateArr[0])
                        }
                          parent.dateclick("comparefrom")
}else if(parent.datetype=="compareto"){
     parent.$("#compareto").val(todaydate);
var b;
                        b=($("#compareto").val());
                        var dateArr=new Array()
                        dateArr=b.split(",");
                        if(b!=""){
                            $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                            $("#ctfield2").html(dateArr[1])
                            $("#ctfield3").html(dateArr[0])
                        }
                         parent.dateclick("compareto")
}
    }
}



function getGeoWorldMap(divID){
 var chartData = JSON.parse($("#chartData").val());
// $("#"+divID).html("")
var chartKeys = Object.keys(graphData)

 if(chartData[divID]["drillType"]=="multi"){
	
   var viewGBIds = JSON.parse(parent.$("#viewbyIds").val());
   var viewGBName = JSON.parse(parent.$("#viewby").val());
   var viewbyFlag = [];
   var viewByIdFlag = [];
   var dimension  = [];
   var driverList = JSON.parse($("#driverList").val());
   var driverKeys = Object.keys(driverList)
   for(var chart in chartKeys ){
   for(var drive in driverList){
chartData[chartKeys[chart]]["mapdrill"]="No";
chartData[chartKeys[chart]]["MapTransformation"] =  {}

       if(driverList[drive].toString().toLowerCase() ==chartKeys[chart].toString().toLowerCase()){ 
       if(chartData[chartKeys[chart]]["dimensions"][1]!="undefined" && chartData[chartKeys[chart]]["dimensions"][1]!=""){
   viewByIdFlag.push(chartData[chartKeys[chart]]["dimensions"][1]);
   }
   for(var i in viewGBIds){
if(viewGBIds[i]==chartData[chartKeys[chart]]["dimensions"][1]){
viewbyFlag.push(viewGBName[i]);
dimension.push(chartData[chartKeys[chart]]["dimensions"][1])
}
}
dimension.push(chartData[divID]["dimensions"][0]);
chartData[chartKeys[chart]]["viewIds"] =viewByIdFlag;
chartData[chartKeys[chart]]["viewBys"] =viewbyFlag;       
chartData[chartKeys[chart]]["dimensions"]=dimension; 

 
   }

parent.$("#chartData").val(JSON.stringify(chartData));
 }
}
  var chartCount = parseInt((divID.replace('chart','')));
  chartCount = chartCount-1;
  
  var  drills=JSON.parse($("#drills").val());
  var drillKeys = Object.keys(drills);
  
  var drillMap = {};
  for(var i=0;i<drillKeys.length;i++){
  var drillValues = [];
  if(i!=chartCount){
  drillValues.push(drills[drillKeys[i]][0])
  drillMap[drillKeys[i]] = drillValues;
  }

  }
//because veraction people told like that
drillMap = {}

		chartData[divID]["mapdrill"]="No";
		chartData[divID]["mapMultiLevelDrill"]="No";
        	chartData[divID]["MapTransformation"] =  {}
		parent.$("#chartData").val(JSON.stringify(chartData));
	$("#drills").val(JSON.stringify(drillMap));
	
   $.post($("#ctxpath").val()+"/reportViewer.do?reportBy=drillCharts&reportName="+$("#graphName").val()+"&reportId="+$("#graphsId").val(), $("#graphForm").serialize(),
            function(data){
			var data1 = JSON.parse(data)["data"];
		chartData[divID]["mapdrill"]="No";
		chartData[divID]["mapMultiLevelDrill"]="No";
        	chartData[divID]["MapTransformation"] =  {}
		parent.$("#chartData").val(JSON.stringify(chartData));


                    generateChart(data1);
	});
 
 }else{
 var map ={}
 map["MapTransformation"] =[]
 chartData[divID]["MapTransformation"]=map;
 chartData[divID]["mapCountryName"]="";
 $("#chartData").val(JSON.stringify(chartData));
 var viewbys = chartData[divID]["viewBys"];
 var measures = chartData[divID]["meassures"]
 var width =   $("#div"+divID).width();
 var Height=$("#div"+divID).height();

buildWorldMapChart(divID, graphData, viewbys , measures,width,Height)
 }

}
function adjustPosition(chartId,event)
{
//    (chartData[chartId]["chartType"])
    var id1='chartTypesX'+chartId;
    if(event.pageX>(screen.width-170)){
        $("#"+id1).css("left",-165);
    }
    $(".chartHighliter").css("background-color","#fff");
    var chartData=JSON.parse(parent.$("#chartData").val());
    for(var j=0;j<chartG.length;j++){
        for(var i=0;i < eval(chartG[j]).length;i++){
         if(eval(chartG[j])[i]===chartData[chartId]["chartType"]){
               $("#chartGroupsX"+chartId+j).css("background-color","#808080");
             $("#chartGroupsX"+chartId+j).addClass("chartHighliter");
             return;
}
    }
    }
    }
//added by sruhti for tablecolumn pro
function reportpicker(id,index,reportid,ctxpath,pagesize){   //alert("vnkjdfvkmfklmv")
     var html = '';
     pbreportid1=reportid;
     pbctxpath1=ctxpath;
     pbpagesize1=pagesize;

     var defaultColor=$('#'+id).css("background-color");
   //  alert(defaultColor)
    // alert(id)
      var color = d3.scale.category10();
    html += '<table>';
    var c=0;
    for(var i=0;i<10;i++){
        html += '<tr>';
        for(var j=0;j<10;j++){
            html += '<td style="padding:2px; ">';
            html += "<input id='color"+(c)+"' type='color picker' onclick='reportselectColor(\""+color(c)+"\")' style='width:20px;background-color:"+color(c)+"' >";
            c++;
            html += '</td>';
        }
        html += '</tr>';
    }
      html += '</table>';
    html += '<div style="width:100%;height:60px">';
    html += '<div id="reportselectColor" style="margin-top:5px;border-radius:10px;background-color:'+defaultColor+';width:100%;height:80%"/>';
    html += '</div>';
    html += '</div>';
    html += '<div align="center">';
     html += "<input type='button' onclick='reportsavecolor(\""+id+"\",\""+reportid+"\",\""+ctxpath+"\",\""+pagesize+"\")' value='Done'>";
    html += '</div>';
    //alert(html);
  //alert($('#'+colorPickerId).html())
parent.$('#report_color_picker').html(html);
   parent.$('#report_color_picker').dialog('open');

}
function reportselectColor(color){
      selectedColor=color;
     // alert(selectedColor)

    $("#reportselectColor").css('background-color', color);

}
function reportsavecolor(colorPickerI,reportid,ctxpath,pagesize){
  $('#report_color_picker').dialog('close');
     parent.$("#dispTabColumnProp").dialog('close');
 //alert("reportid.."+reportid);
  // alert("ctxpath...."+ctxpath);
  // alert("pagesize...."+pagesize);
 // alert("selectedColor..."+selectedColor)
  //alert("colorPickerI...."+colorPickerI)
  //encodeURI(selectedColor);
  //encodeURI(colorPickerI);
  var selected=encodeURI(selectedColor);

  var measureid=encodeURI(colorPickerI);

    var frameObj=parent.document.getElementById("dispTabColumnPropFrame");

   var source =ctxpath+'/PbTableColumnProperties.jsp?reportId='+reportid+"&slidePages="+pagesize+"&selectedcolor="+selected+"&colorPickerI="+measureid;
    frameObj.src=source;
   //$('#report_color_picker').dialog('close');
   //  parent.$("#dispTabColumnProp").dialog('close');
    parent.$("#dispTabColumnProp").dialog('open');
}
//ended by sruthi

function moreListDispTopgraph(){

       if($("#datalist1").is(":visible")){
        $(".hideAllDiv1").hide();
    }else{
    $(".hideAllDiv1").hide();//added by Prabal
    $("#datalist1").toggle(100);
}
}
//added by mohit for ao
function addAOObject(reportBizRoles){
    var confirm1=false;
    var confirm2=false;
    var REPORTID = parent.document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var aoAsGoId = parent.document.getElementById("aoAsGoId").value;
//    alert(aoAsGoId)
    var AOId=parent.document.getElementById("AOId").value;
    
//    alert(AOId)
    if(!(aoAsGoId==null || typeof aoAsGoId ==="undefined" || aoAsGoId=="") )
                  {
        confirm1=confirm("AO is Already Added For This Report\n\
                            Do You Want to OverWrite it?");
//        alert(confirm1);
        if(confirm1)
        openAODiv(reportBizRoles,REPORTID);
                  }
    else if(!(AOId==null || typeof AOId ==="undefined" || AOId=="" ))
    {
        confirm2=confirm("This Report is Created on AO \n\
                         Do You Want to Use the Same for Graph ?");
//        alert(confirm2);
        if(confirm2)
        useAOasGO(AOId,REPORTID);
    }
              else
                  {
            openAODiv(reportBizRoles,REPORTID);
    
                  }
        }

//added by mohit for ao
 function createAOForGraph(){
                var fldObj=document.getElementsByName("AOList");
                 var REPORTID = parent.document.getElementById("REPORTID").value;
                var foldersIds="";
                if(fldObj.length!=0){
                    for(var i=0;i<fldObj.length;i++){
                        if(fldObj[i].checked){
                            foldersIds=foldersIds+","+fldObj[i].id;
                        }
                    }
                }
                if (foldersIds!=""){
                    foldersIds=foldersIds.substr(1,foldersIds.length);
                        $.ajax({
                        async:false,
                         url: "reportViewerAction.do?reportBy=createAOForGraph&aoId="+foldersIds+"&REPORTID="+REPORTID,
                        success: function(data){
                      if(data=="Failed"){
                           alert("Something Went Wrong AO Object Couldn't be Added");
                            $("#selectAO").dialog('close');
                       }else
                       {
                       var jsonVar=eval('('+data+')');
                     var  viewby=jsonVar.viewby;
       var  viewbyIds=jsonVar.viewbyIds;
       var  measure=jsonVar.measure;
       var  measureIds=jsonVar.measureIds;
          var  aggregation=jsonVar.aggregation;
                     parent.$("#viewby").val(JSON.stringify(viewby));
    parent.$("#viewbyIds").val(JSON.stringify(viewbyIds));
      parent.$("#measure").val(JSON.stringify(measure));
    parent.$("#measureIds").val(JSON.stringify(measureIds));
      parent.$("#aggregation").val(JSON.stringify(aggregation));
      parent.$("#aoAsGoId").val(foldersIds);
        alert("AO Object Added Successfully");
                           $("#selectAO").dialog('close');
                       }
                      }
                          });
            }
            else{
        $("#selectAO").dialog('close');
        alert("Please Select One AO ");
//        $("#selectAO").dialog('open');
            }
    }
 function useAOasGO(foldersIds,REPORTID){
                        $.ajax({
                        async:false,
                         url: "reportViewerAction.do?reportBy=createAOForGraph&aoId="+foldersIds+"&REPORTID="+REPORTID,
                        success: function(data){
                      if(data=="Failed"){
                           alert("Something Went Wrong AO Object Couldn't be Added");
                            $("#selectAO").dialog('close');
                       }else
                       {
                       var jsonVar=eval('('+data+')');
                     var  viewby=jsonVar.viewby;
       var  viewbyIds=jsonVar.viewbyIds;
       var  measure=jsonVar.measure;
       var  measureIds=jsonVar.measureIds;
          var  aggregation=jsonVar.aggregation;
                     parent.$("#viewby").val(JSON.stringify(viewby));
    parent.$("#viewbyIds").val(JSON.stringify(viewbyIds));
      parent.$("#measure").val(JSON.stringify(measure));
    parent.$("#measureIds").val(JSON.stringify(measureIds));
      parent.$("#aggregation").val(JSON.stringify(aggregation));
      parent.$("#aoAsGoId").val(foldersIds);
//        alert("AO Object Added Successfully");
                           $("#selectAO").dialog('close');
                       }
                      }
                          });
            }
    function openAODiv(reportBizRoles,REPORTID){
        $.ajax({
                    async:false,
                      url:"reportViewerAction.do?reportBy=getAOBasedOnReport&reportBizRoles="+reportBizRoles+"&REPORTID="+REPORTID,
                       success:function(data){
//            alert(data)
            if(data == 'Failed')
                  {
                      alert("No AO is Available for This Report");
                  }
              else
                  {
              $("#selectAO").html('');
            $("#selectAO").html(data);
            $("#selectAO").dialog('open');
                  }
        }
                  });
    }
    function updateAOObject(){
       var REPORTID = parent.document.getElementById("REPORTID").value;
    if(typeof REPORTID ==="undefined" || REPORTID===""){
        REPORTID = $("#graphsId").val();
    }
    var aoAsGoId = parent.document.getElementById("aoAsGoId").value;
     if(!(aoAsGoId==null || typeof aoAsGoId ==="undefined" || aoAsGoId=="") )
    {
                       useAOasGO(aoAsGoId,REPORTID);
                      alert("AO Updated Successfully")         
    }else
    {
             alert("AO Couldn't be Found for this Report")         
     }

    }

function showMorePages(event){
    var html='<ul>';
    var repId=parent.$("#graphsId").val();
    var reportPageMapping=JSON.parse(parent.$("#reportPageMapping").val());
    var pages=Object.keys(reportPageMapping[repId]);
    var pageSequence=JSON.parse(parent.$("#pageSequence").val());
    pageSequence.sort();
    if(pages.length<=3){
        alert("No more pages available!");
        return;
    }
    for(var i=3;i<pageSequence.length;i++){
        var pageId='';
        for(var j in pages){
            if(reportPageMapping[repId][pages[j]]["pageSequence"]==pageSequence[i]){
                pageId=pages[j];
                break;
            }
        }
        if(currentPage===pageId){
            html+="<li id='"+pageId+"' style='background-color:#808080;color:whitesmoke;padding:5px;border-bottom: 0px inset;cursor:pointer' onclick='changePage(this.id,\"moreList\")'>"+reportPageMapping[repId][pageId]["pageLabel"];
        }
        else{
            html+="<li id='"+pageId+"' style='padding:5px;border-bottom: 0px inset;cursor:pointer' onclick='changePage(this.id,\"moreList\")'>"+reportPageMapping[repId][pageId]["pageLabel"];
        }
        html+="</li>";
    }
    html+='</ul>';
    $("#pagesList").html(html);
    $("#pagesList").css("padding","0px");
    $("#pagesList").css("font-size","14px");
    $("#pagesList").toggle().css( {
        position:"absolute", 
        top:event.pageY+10, 
        left: event.pageX
    });
}
function ALLmeasures(reportid,ctxpath,pagesize){
     $("#tablecolumnpro").hide();
      var frameObj=parent.document.getElementById("dispTabColumnPropFrame");
   var source =ctxpath+'/ALLmeasures.jsp?reportId='+reportid+"&slidePages="+pagesize;
    frameObj.src=source;
    }
    function Measures(reportid,ctxpath,pagesize){
          var frameObj=parent.document.getElementById("dispTabColumnPropFrame");
   var source =ctxpath+'/PbTableColumnProperties.jsp?reportId='+reportid+"&slidePages="+pagesize;
    frameObj.src=source;
$("#ALLmeasures").hide();
$("#Dimenstionspro").hide();
    }
    function Dimenstions(reportid,ctxpath,pagesize){
         $("#ALLmeasures").hide();
          var frameObj=parent.document.getElementById("dispTabColumnPropFrame");
   var source =ctxpath+'/TableDimenstion.jsp?reportId='+reportid+"&slidePages="+pagesize;
    frameObj.src=source;
    }
	
function downloadExcel1(chartId){
    var chartData = JSON.parse($("#chartData").val());
    var chartName = "";
        var hideDate = "";
     if (typeof chartData[chartId]["Name"] !== "undefined" && chartData[chartId]["Name"]!== "") {
         chartName = chartData[chartId]["Name"];
     }else {
        chartName =  chartId;
     }
               
     hideDate =  $("#spanChartNameDate"+chartId).text();
     
    var viewBys = chartData[chartId]["viewBys"]
    var measures = chartData[chartId]["meassures"];
    var jsonArr = [];
    if(chartData[chartId]["chartType"]==="GroupedStacked-Bar"){
     for(var i in graphData[chartId]){
         var jsonMap = {}
       for(var v in viewBys) {
          jsonMap[viewBys[v]] = graphData[chartId][i][viewBys[v]]
       }
       for(var m in measures) {
          jsonMap[measures[m]] = graphData[chartId][i][measures[m]]
       }
               jsonArr.push(jsonMap);
     }
 }
     if(chartData[chartId]["chartType"]!="Combo-Analysis" && chartData[chartId]["chartType"]!="World-Top-Analysis" && chartData[chartId]["chartType"]!="World-Top-Analysis"){
    if(chartData[chartId]["chartType"]==="GroupedStacked-Bar"){
        JSONToCSVConvertor(jsonArr,chartName,hideDate, true);
    }else{
       JSONToCSVConvertor(graphData[chartId],chartName,hideDate, true); 
    }
    }else{
        alert("Graph cannot be exported.");
    }
}


function showMagTemp(gtValues) {
//    if ($("#managementTempl").is(":visible")) {
//        $("#hdFixedDiv").height("85px");
//        $("#xtendChartssTD").css("margin-top", "30px");
//        $("#repCtrlDiv,#rightDiv1").fadeIn();
//        $("#managementTempl").fadeOut();
//    }
//    else {

var currentRepId = $("#graphsId").val();
var currentRepName = $("#graphName").val();
var repNameSpli = currentRepName.split(" ");
var repName1 ="";
var repName2 ="";

if(repNameSpli.length % 2 == 0){
    for(var d=0;d<repNameSpli.length / 2;d++){
        repName1 = repName1 + " " +repNameSpli[d];
    }
    for(var f=repNameSpli.length / 2;f<repNameSpli.length ;f++){
        repName2 = repName2 + " " +repNameSpli[f];
    }
}else{
    for(var d=0;d< Math.floor(repNameSpli.length / 2);d++){
        repName1 = repName1 + " " +repNameSpli[d];
    }
    for(var f= Math.floor(repNameSpli.length / 2);f<repNameSpli.length ;f++){
        repName2 = repName2 + " " +repNameSpli[f];
    }
}


var currentRepClick = "ToglFiltrn("+currentRepId+")";
$("#hdFavRep1>div").attr({
    "id":currentRepId,
    "onclick":currentRepClick,
    "title":currentRepName
});
$("#hdFavRep1").css("border-bottom", "3px solid #8bc34a");
$("#hdFavRep1>div>span:first-child").text(repName1);
$("#hdFavRep1>div>span:last-child").text(repName2);

        $("#optionMenu").html($("#optionMenuDiv").html());
        $("#optionMenu").height($(window).height() - 200);
        $("#hdFixedDiv").height("50px");
        $("#xtendChartssTD").css("margin-top", "0px");
        $("#repCtrlDiv,#rightDiv1").fadeOut();
        $("#managementTempl").fadeIn();
        
        graphProp("chart1");
        var data1 = [];
        var data2 = [];
        var tempMeta=JSON.parse(parent.$("#templateMeta").val());
        var templateDetails=tempMeta["template1"];
        var measureIds=templateDetails["measureIds"];
        for (var i = 0; i < measureIds.length; i++) {
            if(typeof tempMeta["template1"]["format"]==='undefined' || typeof tempMeta["template1"]["format"][measureIds[i]+"_format"]==='undefined'){
                yAxisFormat="";
            }
            else{
                yAxisFormat=templateDetails["format"][measureIds[i]+"_format"];
            }
            if(typeof tempMeta["template1"]["rounding"]==='undefined' || typeof tempMeta["template1"]["rounding"][measureIds[i]+"_rounding"]==='undefined'){
                yAxisRounding="1";
            }
            else{
                yAxisRounding=templateDetails["rounding"][measureIds[i]+"_rounding"];
            }
            var value=addCommas(numberFormat(gtValues[i],yAxisFormat,yAxisRounding,"chart1"));
            var suffix='';
            if(typeof tempMeta["template1"]["suffix"]!=='undefined' && typeof tempMeta["template1"]["suffix"][measureIds[i]+"_suffix"]!=='undefined'){
                suffix=tempMeta["template1"]["suffix"][measureIds[i]+"_suffix"];
            }
            if(suffix.trim()!==''){
                value=value.split(" ")[0];
                value=value+" "+suffix;
            }
            var prefix='';
            if(typeof tempMeta["template1"]["prefix"]!=='undefined' && typeof tempMeta["template1"]["prefix"][measureIds[i]+"_prefix"]!=='undefined'){
                prefix=tempMeta["template1"]["prefix"][measureIds[i]+"_prefix"];
            }
            value=prefix+value;
            data1.push(value);
//            data1.push(gtValues[i]);
        }
//        for (var i = 3; i < 7; i++) {
//            data2.push(gtValues[i]);
//        }
        var measures=templateDetails["defaultMeasures"];
        createKPILeft(3, data1, measures);
        createKPIRight(4, data1, measures);
        createPages();
	$("#gridsterDiv").height($(window).height()-300);
//    }
}

function createKPILeft(mesuresNo,data,mesureName){
    var apclass='',html='',compare=false;
    var tempMeta=JSON.parse(parent.$("#templateMeta").val());
    var measureIds = tempMeta["template1"]["defaultMeasureIds"];
    var allMeasures=tempMeta["template1"]["measureNames"];
    var allMeasureIds=tempMeta["template1"]["measureIds"];
    var compData="";
    if(mesuresNo > measureIds.length){mesuresNo = measureIds.length;}
    for(var i=0;i<mesuresNo;i++){
    var color='#008cc9';
        var tempDetails=tempMeta["template1"];
        var changePercent='';
        if(typeof tempDetails!=='undefined' && typeof tempDetails["changePercent"]!=='undefined' && typeof tempDetails["changePercent"][measureIds[i]+"_changePercent"]!=='undeined' && tempDetails["changePercent"][measureIds[i]+"_changePercent"]!=='no'){
            var suff=tempDetails["changePercent"][measureIds[i]+"_changePercent"];
            var index=allMeasureIds.indexOf(measureIds[i]+"_"+suff);
            changePercent=data[index];
            if(changePercent<0){
                color="red";
            }
            else{
                color="green";
            }
        }
        var alias='';
        var alias2='';
var fontColor='';
var fontSize='';
        var compTime;
	var stylePrior='';
	apclass='leftSection'+mesuresNo;
        if (typeof tempMeta["template1"]["comparison"] === "undefined" ||
                typeof tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "undefined" ||
                tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "no"){
            
        }
        else{
            var compMeasureId=measureIds[i]+"_PRIOR";
            var compMeasure=allMeasures[allMeasureIds.indexOf(compMeasureId)];
            compData=data[allMeasures.indexOf(compMeasure)];
//            alert(compData);
            stylePrior="visibility:visible";            
            compare=true;
            compTime=allMeasures[allMeasureIds.indexOf(compMeasureId)];
            compTime=compTime.substring(compTime.indexOf("(")+1,compTime.indexOf(")"))
        }
        if(typeof tempMeta["template1"]["alias"]==='undefined' || typeof tempMeta["template1"]["alias"][measureIds[i]+"_alias"]==='undefined' || tempMeta["template1"]["alias"][measureIds[i]+"_alias"].trim()===''){
            alias=mesureName[i];
        }
        else{
            alias=tempMeta["template1"]["alias"][measureIds[i]+"_alias"];
        }
if(typeof tempMeta["template1"]["alias2"]==='undefined' || typeof tempMeta["template1"]["alias2"][measureIds[i]+"_alias2"]==='undefined' || tempMeta["template1"]["alias2"][measureIds[i]+"_alias2"].trim()===''){
            alias2="";
        }
        else{
            alias2=tempMeta["template1"]["alias2"][measureIds[i]+"_alias2"];
        }
if(typeof tempMeta["template1"]["fontColor"]==='undefined' || typeof tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"]==='undefined' || tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"].trim()===''){
            fontColor="";
        }
        else{
            fontColor=tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"];
        }
if(typeof tempMeta["template1"]["fontSize"]==='undefined' || typeof tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"]==='undefined' || tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"].trim()===''){
            fontSize="";
        }
        else{
            fontSize=tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"];
        }
        var arrow = '';
        var changePercentHtml='';
        if (changePercent !== '') {
            changePercentHtml='<div style="margin-bottom:2px;color:'+color+';font-size:12px;">('+changePercent+'%)</div>';
            if (parseInt(changePercent) < 0) {
                arrow = ' <div style="margin-bottom:3px;color:red;font-size:16px;">&#8595;</div>';
            }
            else {
                arrow = ' <div style="margin-bottom:3px;color:green;font-size:16px;">&#8593;</div>';
            }
        }
        html+="<div class='"+apclass+"'>"+
            "<div class='topSectionV' style='cursor:pointer;color:"+color+"'  onclick='drillTemplateToReport("+measureIds[i]+")'>"+
            data[i]+arrow+changePercentHtml+
            "</div>"+
            "<div class='middleSectionV' style='"+stylePrior+";cursor:pointer' onclick='drillTemplateToReport("+measureIds[i]+")'>"+
            compData+" ("+compTime+")"+
            "</div>"+
            "<div class='bottomSectionV'  onclick='drillTemplateToReport("+measureIds[i]+")'>"+
            //alias.substring(0, 50)+
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
    var tempMeta=JSON.parse(parent.$("#templateMeta").val());
    var measureIds = tempMeta["template1"]["defaultMeasureIds"];
    var allMeasures=tempMeta["template1"]["measureNames"];
    var allMeasureIds=tempMeta["template1"]["measureIds"];
var compData="";
    if(measureIds.length <= 7){mesuresNo = (measureIds.length)-3}
    for(var i=3;i<(measureIds.length<=7?measureIds.length:7);i++){
var color='#008cc9';
        var tempDetails=tempMeta["template1"];
        var changePercent='';
        if(typeof tempDetails!=='undefined' && typeof tempDetails["changePercent"]!=='undefined' && typeof tempDetails["changePercent"][measureIds[i]+"_changePercent"]!=='undeined' && tempDetails["changePercent"][measureIds[i]+"_changePercent"]!=='no'){
            var suff=tempDetails["changePercent"][measureIds[i]+"_changePercent"];
            var index=allMeasureIds.indexOf(measureIds[i]+"_"+suff);
            changePercent=data[index];
            if(changePercent<0){
                color="red";
            }
            else{
                color="green";
            }
        }
        var alias='';
        var alias2='';
        var fontColor='';
var fontSize='';
        var compTime;
	var stylePrior='';
	apclass='rightSection'+mesuresNo;
        if (typeof tempMeta["template1"]["comparison"] === "undefined" ||
                typeof tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "undefined" ||
                tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "no"){
            
        }
        else{
            var compMeasureId=measureIds[i]+"_PRIOR";
            var compMeasure=allMeasures[allMeasureIds.indexOf(compMeasureId)];
            compData=data[allMeasures.indexOf(compMeasure)];
	    apclass=apclass+' compare';
            stylePrior="display:flex";
            compTime=allMeasures[allMeasureIds.indexOf(compMeasureId)];
            compTime=compTime.substring(compTime.indexOf("(")+1,compTime.indexOf(")"))
        }
        if(typeof tempMeta["template1"]["alias"]==='undefined' || typeof tempMeta["template1"]["alias"][measureIds[i]+"_alias"]==='undefined'){
            alias=mesureName[i];
        }
        else{
            alias=tempMeta["template1"]["alias"][measureIds[i]+"_alias"];
        }
        if(typeof tempMeta["template1"]["alias2"]==='undefined' || typeof tempMeta["template1"]["alias2"][measureIds[i]+"_alias2"]==='undefined'){
            alias2="";
        }
        else{
            alias2=tempMeta["template1"]["alias2"][measureIds[i]+"_alias2"];
        }
        if(typeof tempMeta["template1"]["fontColor"]==='undefined' || typeof tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"]==='undefined' || tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"].trim()===''){
            fontColor="";
        }
        else{
            fontColor=tempMeta["template1"]["fontColor"][measureIds[i]+"_fontColor"];
        }
if(typeof tempMeta["template1"]["fontSize"]==='undefined' || typeof tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"]==='undefined' || tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"].trim()===''){
            fontSize="";
        }
        else{
            fontSize=tempMeta["template1"]["fontSize"][measureIds[i]+"_fontSize"];
        }
        if (typeof tempMeta["template1"]["comparison"] === "undefined" ||
                typeof tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "undefined" ||
                tempMeta["template1"]["comparison"][measureIds[i] + "_comparison"] === "no"){
            //Yes
        }
        else{
            //No
        }
        if((mesuresNo===2 && i===4) || (mesuresNo===3 && i===4) || (mesuresNo===4 && i===5)){
            html+="<div class='dividerH'></div>";
        }
        if((mesuresNo===3 && i===5) || (mesuresNo===4 && i===4 || i===6)){
            html+="<div class='dividerV'></div>";
        }
        //if(mesureName[i].length > 40){
          //  style="padding-top: 0px;display: inherit;align-items: baseline;cursor:pointer";
        //}
if(alias2 == ""){
style='display: flex;align-items: center;';
        }
        else{style='';}
        var arrow = '';
        var changePercentHtml='';
        if (changePercent !== '') {
            changePercentHtml='<div style="margin-bottom:2px;color:'+color+'font-size:12px;">('+changePercent+'%)</div>';
            if (parseInt(changePercent) < 0) {
                arrow = ' <div style="margin-bottom:3px;color:red;font-size:16px;">&#8595;</div>';
            }
            else {
                arrow = ' <div style="margin-bottom:3px;color:green;font-size:16px;">&#8593;</div>';
            }
        }
        html+="<div class='"+apclass+"'>"+
            "<div class='numericContainer'><div class='data' onclick='drillTemplateToReport("+measureIds[i]+")'>"+
            "<div style='color:"+color+"'>"+data[i]+arrow+changePercentHtml+"</div>"+
            "</div>"+
            "<div class='prior' style='"+stylePrior+"'>"+
	    compData+" ("+compTime+")"+
            "</div></div>"+
            "<div id='details"+i+"' onclick='drillTemplateToReport("+measureIds[i]+")' class='details' style='"+style+"'>"+
//           name.substring(0, 50)+
		"<div class='row1'>"+alias+"</div>"+
            "<div class='row2' style='font-size:"+fontSize+"px;color:"+fontColor+"'>"+alias2+"</div>"+
            "</div>"+
            "</div>";
    }
    $("#secondKPI").html('');
    $("#secondKPI").append(html);
}
function createPages() {
    var reportPageMapping = JSON.parse(parent.$("#reportPageMapping").val());
    var reportId = parent.$("#graphsId").val();
    var pages = Object.keys(reportPageMapping[reportId]);
    var pageLabels = [];
    var htmlVar = '',underLine='border-bottom:none';
    for (var i=0;i<(pages.length<=4?pages.length:4);i++) {
	if(pages[i] === currentPage){
		htmlVar += "<div  class='section" + (pages.length<=4?pages.length:4) + "' style='border-bottom:3px solid #008cc9'; id='" + pages[i] + "' onclick='getSelectedPage(this.id)'>";
	}
	else{
		htmlVar += "<div  class='section" + (pages.length<=4?pages.length:4) + "' id='" + pages[i] + "' onclick='getSelectedPage(this.id)'>";
	}
        pageLabels.push(reportPageMapping[reportId][pages[i]]["pageLabel"]);
        
        htmlVar += pageLabels[i];
        htmlVar += "</div>";
    }
    $("#pagesDash").html('').append(htmlVar);
}
function openLink(obj){
    $("#managementTempl .rootContainer2>div").removeClass("activeTemp");
    $(obj).addClass("activeTemp");
}
function openMenu(){
    $("#optionMenu").slideToggle();
    if (window.mgmtDBFlag === 'true') {
        $("#mgmtTemp").html("<a id='mgmtTemp' data-color='color1' class='gFontFamily ' onclick='removeMgmtTemp()' >Add Management Template</a>");
    }
    else{
        $("#mgmtTemp").html("<a id='mgmtTemp' data-color='color1' class='gFontFamily ' onclick='addMgmtTemp()' >Add Management Template</a>");
    }
}
function setTemplateDetails(){
    var tempMeta=JSON.parse(parent.$("#templateMeta").val());
    var tempDetails=tempMeta["template1"];
    var measures = tempDetails["measureNames"];
    var measureIds = tempDetails["measureIds"];
    var aggregations = tempDetails["aggregation"];
    var defaultMeasures=tempDetails["defaultMeasures"];
    var defaultMeasureIds=tempDetails["defaultMeasureIds"];
    var defaultAggregations=tempDetails["defaultAggregation"];    
    var alias={};
    var alias2={};
var fontSize={};
var fontColor={};
    var format={};
    var rounding={};
//    var comparison={};
    var comparison={};
    var prefix={};
    var suffix={};
    var customDate={};
    var graphDrillMap = {};
    var changePercentMap = {};
     graphDrillMap["reportId"] = parent.$("#graphsId").val();
    for (var i=0;i<(measures.length<7?measures.length:7);i++) {
        alias[measureIds[i]+"_alias"]=$("#"+measureIds[i]+"_alias").val();
        alias2[measureIds[i]+"_alias2"]=$("#"+measureIds[i]+"_alias2").val();
fontSize[measureIds[i]+"_fontSize"]=$("#"+measureIds[i]+"_fontSize").val();
fontColor[measureIds[i]+"_fontColor"]=$("#"+measureIds[i]+"_fontColor").css("background-color");
        format[measureIds[i]+"_format"]=$("#"+measureIds[i]+"_format").val();
        rounding[measureIds[i]+"_rounding"]=$("#"+measureIds[i]+"_rounding").val();
//        comparison[measureIds[i]+"_comparison"]=$("#"+measureIds[i]+"_comparison").val();
       comparison[measureIds[i]+"_comparison"]=$("#"+measureIds[i]+"_comparison").val();
        prefix[measureIds[i]+"_prefix"]=$("#"+measureIds[i]+"_prefix").val();
        suffix[measureIds[i]+"_suffix"]=$("#"+measureIds[i]+"_suffix").val();
        changePercentMap[measureIds[i]+"_changePercent"]=$("#"+measureIds[i]+"_changePercent").val();
        if($("#calenIcon"+i).val()==='yes'){
            var timeDetails=[];
            timeDetails.push("Day");
            timeDetails.push("PRG_STD");
            var date=$("#customDate"+i).val();
            timeDetails.push(date);
            var timeType=$("#timeType"+i).val();
            timeDetails.push(timeType);
            timeDetails.push("Last "+(timeType));
            customDate[measureIds[i]+"_date"]=timeDetails;
    }     
       graphDrillMap[measureIds[i]] = parent.$("#reportToDrill_"+measureIds[i]).val();
    }
    tempDetails["alias"]=alias;
    tempDetails["alias2"]=alias2;
    tempDetails["fontSize"]=fontSize;
    tempDetails["fontColor"]=fontColor;
    tempDetails["format"]=format;
    tempDetails["rounding"]=rounding;
//    tempDetails["comparison"]=comparison;
tempDetails["comparison"]=comparison;
    tempDetails["prefix"]=prefix;
    tempDetails["suffix"]=suffix;
    tempDetails["changePercent"]=changePercentMap;
    var compKeys=Object.keys(comparison);
    var comparedMeasure=[];
    var comparedMeasureId=[];
    var comparedAgg=[];
    var newMeasure=[];
    var newMeasureId=[];
    var newAgg=[];
    for(var i in compKeys){
        if(comparison[compKeys[i]]==='yes'){
            comparedMeasure.push(defaultMeasures[i]+" (PRIOR)");
            comparedMeasureId.push(defaultMeasureIds[i]+"_PRIOR");
            comparedAgg.push(defaultAggregations[i]);
        }
    }
    for(var i in compKeys){
        if($("#"+defaultMeasureIds[i]+"_changePercent").val()!=='no'){
            comparedMeasure.push(defaultMeasures[i]+" ("+$("#"+defaultMeasureIds[i]+"_changePercent").val()+")");
            comparedMeasureId.push(defaultMeasureIds[i]+"_"+$("#"+defaultMeasureIds[i]+"_changePercent").val());
            comparedAgg.push(defaultAggregations[i]);
        }
    }
    for(var i in defaultMeasures){
        newMeasure.push(defaultMeasures[i]);
        newMeasureId.push(defaultMeasureIds[i]);
        newAgg.push(defaultAggregations[i]);
    }
    for(var i in comparedMeasure){
        newMeasure.push(comparedMeasure[i]);
        newMeasureId.push(comparedMeasureId[i]);
        newAgg.push(comparedAgg[i]);
    }
    tempDetails["measureNames"]=newMeasure;
    tempDetails["measureIds"]=newMeasureId;
    tempDetails["aggregation"]=newAgg;
    tempDetails["timeDetails"]=customDate;
    tempDetails["graphDrillMap"]=graphDrillMap;
    tempMeta["template1"]=tempDetails;
    parent.$("#templateMeta").val(JSON.stringify(tempMeta));
    $("#initializeCharts").dialog('close');
    var gtResult;
        $.ajax({
            async: false,
            type: "POST",
            data: parent.$("#graphForm").serialize(),
            url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
            success: function(data) {
                gtResult = JSON.parse(JSON.parse(data)["data"]);
                var tempMetaResult = JSON.parse(data)["meta"];
                parent.$("#templateMeta").val(tempMetaResult);
                showMagTemp(gtResult);
            }
        });
//        showMagTemp(gtValues);
}
function addMgmtTemp() {
$("#gridsterDiv").height($(window).height()-300);
    var templateDetails = {};
    var measures = JSON.parse(parent.$("#measure").val());
    var measureIds = JSON.parse(parent.$("#measureIds").val());
    var aggregation = JSON.parse(parent.$("#aggregation").val());
    templateDetails["measureNames"] = measures;
    templateDetails["measureIds"] = measureIds;
    templateDetails["defaultMeasures"] = measures;
    templateDetails["defaultMeasureIds"] = measureIds;
    templateDetails["defaultAggregation"] = aggregation;
    templateDetails["aggregation"] = aggregation;
    var tempMeta = {};
    tempMeta["template1"] = templateDetails;
    parent.$("#templateMeta").val(JSON.stringify(tempMeta));
    var gtResult;
    $.ajax({
        async: false,
        type: "POST",
        data: parent.$("#graphForm").serialize(),
        url: $("#ctxpath").val() + "/reportViewer.do?reportBy=GTKPICalculateFunction&action=template",
        success: function(data) {
            $.ajax({
                async: false,
                type: "POST",
                url: $("#ctxpath").val() + "/reportViewerAction.do?reportBy=setMgtmlTemplateFlag&reportId='"+$("#graphsId").val()+"'",
                success: function(data1) {
                    gtResult = JSON.parse(JSON.parse(data)["data"]);
                    var tempMetaResult = JSON.parse(data)["meta"];
                    parent.$("#templateMeta").val(tempMetaResult);
                    showMagTemp(gtResult);
                }
            });
        }
    });

}
function removeMgmtTemp(){
    $.ajax({
                async: false,
                type: "POST",
                url: $("#ctxpath").val() + "/reportViewerAction.do?reportBy=setMgtmlTemplateFlag&reportId='"+$("#graphsId").val()+"'&flag=false",
                success: function(data1) {
                }
            });
}

