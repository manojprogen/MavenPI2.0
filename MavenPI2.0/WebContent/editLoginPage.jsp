<%-- 
    Document   : editLoginPage
    Created on : Mar 18, 2015, 11:52:11 AM
    Author     : Manik Srivastava
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,java.util.ArrayList"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%
boolean isCompanyValid=false;
 String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
String LOGINID=String.valueOf(session.getAttribute("LOGINID"));

  List<String> busRoleIDs = new ArrayList<String>();
  List<String> busRoleNames = new ArrayList<String>();
 busRoleIDs = (List<String>) request.getAttribute("BusRoleIds");
 busRoleNames = (List<String>) request.getAttribute("BusRoleNames");
 String contextPath=request.getContextPath();
%>
<!--<html style="background-color:#1B3E70"      >-->
<html style="background-color:white"      >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title><bean:message key="ProGen.Title"/></title>
          
<!--           <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
           <link href="<%=contextPath%>/css/jquery.circliful.css" rel="stylesheet" type="text/css" />
           <link href="<%=contextPath%>/css/d3/xtendChart.css" rel="stylesheet" type="text/css" />
	   <script src="<%=contextPath%>/NewLogin/JS/jquery-1.10.2.min.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.v3.min.js"></script>
           
        <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/jquery.circliful.js"></script>
	<script src="<%=contextPath%>/NewLogin/JS/jquery.charts.min.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/NewLogin/JS/d3.min.js"></script>-->
<script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/indiaMap.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/d3.geo.min.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/d3.geo.tile.js.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/customtooltip.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/chartTypes.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/d3.layout.cloud.js"></script>
<script  type="text/javascript" src="<%=contextPath%>/NewLogin/JS/bullet.js" ></script>
<script  type="text/javascript" src="<%=contextPath%>/NewLogin/JS/LoginCharts.js" ></script>
 <script type="text/javascript" src="<%=contextPath%>/NewLogin/JS/Grid/jquery.gridster.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/Grid/jquery.gridster.css" /> <!-- added by manik-->
    	
        <style type="text/css">
         .white_content{position:absolute;top:20%;width:28%;height:399px;padding:5px;z-index:1002;-moz-border-radius-bottomleft:10px;-moz-border-radius-bottomright:10px;-moz-border-radius-topleft:10px;-moz-border-radius-topright:10px}
        .white1_content{position:absolute;top:30%;width:452px;height:180px;padding:5px;z-index:1002;-moz-border-radius-bottomleft:10px;-moz-border-radius-bottomright:10px;-moz-border-radius-topleft:10px;-moz-border-radius-topright:10px}
        .black_overlay{position:absolute;top:0;left:0;width:100%;height:100%;background-color:#fff;z-index:1001;-moz-opacity:.8;opacity:.5;filter:alpha(opacity=60);overflow:auto}
        .upperTab{position:absolute;top:1%;left:0;border-bottom:1px solid #87CEEB}
        .bottomTab{position:absolute;top:92%;left:0}
        .middleTab{position:absolute;top:10%;left:30%}
        .right{height:50px;width:140px}
        .left{height:40px;width:60px}
        </style>
    </head>
    <%
    session.removeAttribute("connId");
   
         if(brdcrmb!=null)
             {
             session.invalidate();
             }
          String REPORTID="";
    if(request.getAttribute("REPORTID")!=null){
     REPORTID=String.valueOf(request.getAttribute("REPORTID"));
    }
    request.setAttribute("REPORTID",REPORTID);
    %>
        <style type="text/css">
           .centerDiv{width:98%;margin-left:1%}
            .chart_1_cl{width:16%;height:38%;background-color:#fff;border:1px dotted darkgrey;margin-right:1%;margin-top:1%;float:left}
            .chart_2_cl{width:96.5%;height:48%;background-color:#fff;border:1px dotted darkgrey;margin:1%;float:left}
            .chart_4_cl{width:31.2%;height:48%;background-color:#fff;border:1px dotted darkgrey;margin-left:1%;float:left}
            .chart_5_cl{width:31.2%;height:38%;margin-top:1%;float:left;border:1px dotted darkgrey;background-color:#fff}
            .chart_6_cl{width:32.5%;height:58%;margin-top:1%;float:left;background-color:#fff;border:1px dotted darkgrey}
            .chart_12_cl{width:49.3%;height:58%;margin-top:1%;float:left;background-color:#fff;border:1px dotted darkgrey}
@import url(http://fonts.googleapis.com/css?family=Open+Sans:300,400,600,700);

.menu,.sub-menu{list-style-type:none;margin:0;padding:0}
.menu li{background-color:#3E454D;cursor:pointer;position:relative;transition:background-color .5s;-moz-transition:background-color .5s;-o-transition:background-color .5s;-webkit-transition:background-color .5s}
.menu a{color:#FFF;display:block;font-family:'Open Sans',sans-serif;font-size:14px;height:100%;margin:0 5px;overflow:hidden;position:relative;text-align:center;text-decoration:none;word-wrap:break-word}
.menu a:hover{color:#FFF}
.menu > li{float:left;height:100%;width:16.66%}
.menu > li.home{background:#3E454D url(http://s26.postimg.org/qbf4q4j9h/home.png) center no-repeat;width:20%}
.menu > li.current{background-color:#F2762E!important;box-shadow:none!important;-moz-box-shadow:none!important;-webkit-box-shadow:none!important}
.menu > li:not(:first-child){box-shadow:inset 1px 0 0 0 #272B31,inset 2px 0 0 0 #434A52;-moz-box-shadow:inset 1px 0 0 0 #272B31,inset 2px 0 0 0 #434A52;-webkit-box-shadow:inset 1px 0 0 0 #272B31,inset 2px 0 0 0 #434A52}
.menu > li.home > a{color:transparent!important}
.menu > li > a > span{left:0;position:absolute;right:0;top:50%;transform:translate(0,-50%);-ms-transform:translate(0,-50%);-moz-transform:translate(0,-50%);-o-transform:translate(0,-50%);-webkit-transform:translate(0,-50%)}
.sub-menu{max-height:0;min-width:100%;overflow:hidden;position:absolute;z-index:9999;transition:max-height .5s .2s;-moz-transition:max-height .5s .2s;-o-transition:max-height .5s .2s;-webkit-transition:max-height .5s .2s}
li:hover > .sub-menu{max-height:600px}
.sub-menu li{height:25px}
.sub-menu a{line-height:25px;transition:color .5s;-moz-transition:color .5s;-o-transition:color .5s;-webkit-transition:color .5s;white-space:nowrap;text-align:left}
.sub-menu li.current a,.sub-menu a:hover{color:#F2762E!important}
button{position:absolute;right:10px;top:10px}
.bullet{font:10px sans-serif}
.bullet .marker{stroke:#000;stroke-width:2px}
.bullet .axis line,.bullet .axis path{stroke:#666;stroke-width:.5px;fill:none}
.bullet .range.s0{fill:#eee}
.bullet .range.s1{fill:#ddd}
.bullet .range.s2{fill:#ccc}
.bullet .range.s3{fill:#bbb}
.bullet .measure.s0{fill:#87ceeb}
.bullet .measure.s1{fill:#36C}
.bullet .title{font-size:8px;font-weight:700}
.bullet .subtitle{fill:#999}
.circle-text_val{width:20%;position:absolute;text-align:center;display:inline-block;font:45px verdana;margin-top:7%}
.circle-text_val1{width:20%;position:absolute;text-align:center;display:inline-block;font:14px verdana;margin-top:11%}
html,body{width:100%;height:100%;padding:0;background:#fff;margin:0;font-family:arial}
a{text-decoration:none}
.container{width:262px;margin:0 auto;padding-top:200px}
#bar{width:100%;height:35px;padding:15px 0;background:url(../NewLogin/images/bar.png) repeat-x}
#container{width:960px;margin:0 auto}
#loginContainer{position:relative;float:right;font-size:12px}
#gridUL_Login li{background-color:#add8e6}
#loginButton:hover{background:url(../NewLogin/images/buttonbgHover.png) repeat-x}
#loginBox{position:absolute;top:34px;right:0;display:none;z-index:29}
#loginButton.active{border-radius:3px 3px 0 0}
#loginButton.active span{background-position:53px -76px}
#loginButton.active em{position:absolute;width:100%;height:1px;background:#d2e0ea;bottom:-1px}
#loginForm{width:248px;border:1px solid grey;border-radius:3px 0 3px 3px;-moz-border-radius:3px 0 3px 3px;margin-top:-1px;background:#3E454D;padding:6px}
#loginForm fieldset{margin:0 0 12px;display:block;border:0;padding:0}
fieldset#body{background:#fff;border-radius:3px;-moz-border-radius:3px;padding:10px 13px;margin:0}
#loginForm #checkbox{width:auto;margin:1px 9px 0 0;float:left;padding:0;border:0;margin:-3px 9px 0 0}
#body label{color:#3a454d;margin:9px 0 0;display:block;float:left}
#loginForm #body fieldset label{display:block;float:none;margin:0 0 6px}
#loginForm input{width:92%;border:1px solid #899caa;border-radius:3px;-moz-border-radius:3px;color:#3a454d;font-weight:700;padding:8px;box-shadow:inset 0 1px 3px #bbb;-webkit-box-shadow:inset 0 1px 3px #bbb;-moz-box-shadow:inset 0 1px 3px #bbb;font-size:12px}
#loginForm #login{width:auto;float:left;background:#3E454D;color:#fff;padding:7px 10px 8px;text-shadow:0 -1px #278db8;border:1px solid grey;box-shadow:none;-moz-box-shadow:none;-webkit-box-shadow:none;margin:0 12px 0 0;cursor:pointer;padding:7px 2px 8px 2px}
#loginForm span{text-align:center;display:block;padding:7px 0 4px}
input:focus{outline:none}
.tooltip{position:absolute;top:100px;left:100px;border:1px solid #000;background:#fff7c1;opacity:.9;color:#000;padding:10px;width:170px;font-size:12px;font-family:'calibre';text-align:left;z-index:10}
.tooltip .title{font-family:'calibre';font-size:12px;text-align:right}
.tooltip .name{font-family:'calibre';font-weight:700;text-align:right}
#customLoginPage{position:fixed;top:0;width:25%;height:300px;margin-left:37.5%;margin-right:37.5%;border:none;z-index:50;background-color:#d3d3d3}
</style>
		 <script type="text/javascript">
        function saveLoginPage(){
//                             alert("details    "+$("#LayoutDetails").val());
                             alert("Your Layout Saved Successfully  ");
        }

         $(document).ready(function(){
          
//var widt=parseInt(((($(window).width()*.87))-35)/8);
var widt=parseInt((((($(window).width())))/12)/2);
//var widt=parseInt(((($("#divBody").width()))-45)/10);
$("#customLoginPage").height($("#divHeader").height());
//addgraphs();
var x="1%";
 gridsterL = $(".gridster > ul").gridster({
        widget_margins: [2, 2],
        autogenerate_stylesheet: false,
        widget_base_dimensions: [widt,widt],
        min_cols: 10,
        resize: {
            enabled: true,
            axes:['both'],
            handle_class:'gs-resize-handle',
            start: function (e, ui, $widget) {
//                    gridResize(e, ui,$widget);
//alert("id  "+($($widget).attr('id')).replace("div","" ))
//$('#renameTitle'+($($widget).attr('id')).replace("div","" )).quickfit({ max: 24, min: 14, truncate: true });


            },
            stop: function (e, ui, $widget) {
                var newHeight = this.resize_coords.data.height;
                var newWidth = this.resize_coords.data.width;
//                 gridResize(newHeight, newWidth);
//      alert("id  "+($($widget).attr('id')).replace("div","" ))
                var chartId=$($widget).attr('id').replace("div","" );
//                  localRefresh(chartId);
       }loginButton

        }
    }).data('gridster');
  gridsterL.generate_stylesheet({rows: 30, cols: 22});

//gridsterL.add_widget('<li class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog("\chart_11"\)" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget()" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 2, 1);


//              $("#divForm").height($( window ).height()-20);
         //     $("#divForm").height($( window ).height());
//         var button = $('#loginButton');
//    var box = $('#loginBox');
//    var form = $('#loginForm');
//    button.removeAttr('href');
//    button.mouseup(function(login) {
//        box.toggle();
//        button.toggleClass('active');
//    });
//    form.mouseup(function() {
//        return false;
//    });
//    $(this).mouseup(function(login) {
//        if(!($(login.target).parent('#loginButton').length > 0)) {
//            button.removeClass('active');
//            box.hide();
//        }
//    });
              $("#drpdwn").height($("#divDropDwn").height());
              $("#headerImag").height($("#divHeader").height());
              $("#wordcloud").height($("#chart_3").height());
              $("#wordcloud").width($("#chart_3").width());
              $("#collage").height($("#divcollage").height());
              $("#collage").width($("#divcollage").width());
              $("#chart2img").height($("#chart_1").height());
              $("#chart3img").height($("#chart_1").height());
              $("#myStathalf").height($("#chart_1").height());
             $("#myStathalf").width($("#chart_1").width());

              $("#img_icons").height($("#divHeader").height());
             
              $("#chart2img").width($("#chart_1").width());
              $("#map1").height(($("#chart_7").height())-25);
              $("#map1").width($("#chart_7").width());
              $("#map2").height(($("#chart_8").height())-25);
              $("#map2").width($("#chart_8").width());
              $("#bar_1").height(($("#chart_4").height())-25);
              $("#bar_1").width($("#chart_4").width());


var ww12=$("#chart_7_2nd").width()
	var hh12=$("#chart_7_2nd").height()
	var ww13=$("#trend_chart").width()
	var hh13=$("#trend_chart").height()
	var ww7=$("#chart_7_2nd").width()
	var hh7=$("#chart_7_2nd").height()
	var ww6=$("#chart_6_2nd").width()
	var hh6=$("#chart_6_2nd").height()
	var hh6=$("#chart_6_2nd").height()
	var ww8=$("#chart_10_2nd").width()
	var hh8=$("#chart_10_2nd").height()
	var ww3=$("#chart_3_2nd").width()
	var hh3=$("#chart_3_2nd").height()
	var ww9=$("#chart_8_2nd").width() + 50;
	var hh9=$("#chart_8_2nd").height() ;
	var ww10=$("#chart_pie_2").width() ;
	var hh10=$("#chart_pie_2").height() ;
	var ww11=$("#chart_pie_3d_2").width() ;
	var hh11=$("#chart_pie_3d_2").height() ;
	var ww12=$("#chart_12_2nd").width() ;
	var hh12=$("#chart_12_2nd").height() ;

//alert(hh9)
		 var col = [];
                    col.push("STATE_NAME");
                    var meas = [];
                    meas.push("TOTAL_SCHOOL");
                    meas.push("SUPP_SCHOOL");
                    meas.push("REM_SCHOOL");
                    meas.push("TOILET_REM");
                    meas.push("TOTAL_TOILET");
                    var columns = [];
                    var measureArray = [];
                    columns.push("CORP_NAME");
                    measureArray.push("TOTAL_SCHOOLS");

//                    alert("<%=request.getContextPath()%>");
                    var ctxpath="<%=request.getContextPath()%>";
                    var layout=10;
                    var currData1=[];
                    var currData2=[];
                    var currDataG1=[];
                    var currDataG2=[];
                    var currDataG3=[];
                    var currDataG4=[];
                    var currDataG5=[];
                    var currDataG6=[];

  
//bubble1("chart_8_2nd",columns,measureArray,ww9,hh9)
//buildHalfPie1("chart_6_2nd", ww6, hh6);
//GrossMarginAnalysis_63_data.json   viewbys
//d3.json("<%=request.getContextPath()%>/NewLogin/json/GrossMarginAnalysis_63_data.json", function (data) {
//d3.json("<%=request.getContextPath()%>/NewLogin/json/GrossMarginAnalysis_63.json", function (data) {
////    alert("is_63  "+JSON.stringify(data["chart1"][5]));
//    for(var m=0;m<10;m++){
//                  currData.push(data["chart1"][m]);
//                }
//})
d3.json("NewLogin/json/OverallSummary1_836_trend.json", function (data) {
//d3.json("<%=request.getContextPath()%>/NewLogin/json/OverallSummary1_836_trend.json", function (data) {
//    alert("OverallSummary1_836  "+JSON.stringify(data["chart1"]));
//    for(var m=0;m<10;m++){
    for(var m=0;m<data["chart1"].length;m++){
                  currData1.push(data["chart1"][m]);
                }
    for(var m=0;m<data["chart2"].length;m++){
                  currData2.push(data["chart2"][m]);
                }


 d3.json("NewLogin/json/OverallSummary1_836_data_trend.json", function (data) {
//alert("cuurr  "+JSON.stringify(currData))
//alert("....."+JSON.stringify(data["chartData"]["chart1"]))

if(data["chartData"]["chart1"]["chartType"]=="Line")
buildLine5("chart_12_2nd","chart1",currData1,data["chartData"],data["chartData"]["chart1"]["viewBys"], data["chartData"]["chart1"]["meassures"], ww12, hh12);
else
buildMultiMeasureTrLine5("chart_12_2nd","chart1",currData1,data["chartData"],data["chartData"]["chart1"]["viewBys"], data["chartData"]["chart1"]["meassures"], ww12, hh12);
//buildLine5("chart_13_2nd","chart2",currData,data["chartData"],data["chartData"]["chart2"]["viewBys"], data["chartData"]["chart1"]["meassures"], ww12, hh12);

buildLine5("chart_13_2nd","chart2",currData2,data["chartData"],data["chartData"]["chart2"]["viewBys"], data["chartData"]["chart2"]["meassures"], ww12, hh12);
 });
   });

d3.json("NewLogin/json/OverallSummary1_836.json", function (data1) {
//d3.json("<%=request.getContextPath()%>/NewLogin/json/OverallSummary1_836_trend.json", function (data) {
//    alert("OverallSummary1_836  "+JSON.stringify(data1["chart5"]));
//    alert("OverallSummary1_836  "+JSON.stringify(data1));
//    alert("ddd "+data1["chart4"].length)
//    for(var m=0;m<10;m++){
    for(var m=0;m<data1["chart1"].length;m++){
                  currDataG1.push(data1["chart1"][m]);
                }
//    for(var m1=0;m1<data1["chart2"].length;m1++){
    for(var m1=0;m1<data1["chart2"].length;m1++){
                  currDataG2.push(data1["chart2"][m1]);
                }
//    for(var m2=0;m2<data1["chart3"].length;m2++){
    for(var m2=0;m2<10;m2++){
                  currDataG3.push(data1["chart3"][m2]);
                }
//    for(var m3=0;m3<data1["chart4"].length;m3++){
    for(var m3=0;m3<25;m3++){
                  currDataG4.push(data1["chart4"][m3]);
                }
//    for(var m4=0;m4<data1["chart5"].length;m4++){
    for(var m4=0;m4<10;m4++){
                  currDataG5.push(data1["chart5"][m4]);
                }
    for(var m5=0;m5<data1["chart6"].length;m5++){
                  currDataG6.push(data1["chart6"][m5]);
                }

//      var sum=0;
//$.each(data1, function (d) {
//sum += parseInt(data1[d][1]);
//alert("d "+d)
//alert("sum "+data1[d][1])
//});
//
//  alert("sumaa "+sum)

d3.json("<%=request.getContextPath()%>/NewLogin/json/OverallSummary1_836_data.json", function (data) {
//d3.json("<%=request.getContextPath()%>/NewLogin/json/GrossMarginAnalysis_63.json", function (data) {

//            alert("2nd"+JSON.stringify(data["chartData"]["chart5"]))
//            alert("2nd"+JSON.stringify(data["chartData"]))

//alert(JSON.stringify(currDataG3))
//alert(JSON.stringify(data["measures"][0]))
//var sum = d3.sum(currData, function(d) {
//        return d[data["measures"][0]];
//    });
//  var forsum=  numberFormat(sum,"M","2")
//    alert(forsum)
//    $("#chart_1").append("<table style='height:100%;width:100%'><tr><td style='font: 30px sans-serif; color: rebeccapurple;'>"+forsum+"</td><td style='font: 20px sans-serif; color: rebeccapurple;' >"+data["measures"][0]+"</td></tr></table>")

;



//    $("#chart_1").append(forsum)
//    $("#chart_1").append(data["measures"][0])
//              alert("dat "+JSON.stringify(data["chartData"]))
//buildHalfPie(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],"600","450");
//bubblePortal("chart_8_2nd",currData,data["chartData"]["chart1"]["viewBys"],data["measures"],ww7,hh7)

buildHalfPie5("chart_6_2nd","chart1",currDataG1,data["chartData"],data["chartData"]["chart1"]["viewBys"], data["chartData"]["chart1"]["meassures"], ww7, hh7);
try{
buildBar5("chart_7_2nd","chart2",currDataG2,data["chartData"],data["chartData"]["chart2"]["viewBys"], data["chartData"]["chart2"]["meassures"], ww7, hh7);
}
catch(e){}
bubble5("chart_8_2nd","chart4",currDataG4,data["chartData"],data["chartData"]["chart4"]["viewBys"], data["chartData"]["chart4"]["meassures"], ww7, hh7,layout);
buildHorizontalBar5("chart_9_2nd","chart3",currDataG3,data["chartData"],data["chartData"]["chart3"]["viewBys"], data["chartData"]["chart3"]["meassures"], ww7, hh7);
buildPie5("chart_10_2nd","chart5",currDataG5,data["chartData"],data["chartData"]["chart5"]["viewBys"], data["chartData"]["chart5"]["meassures"], ww7, hh7);
buildHalfDonut5("chart_11_2nd","chart6",currDataG6,data["chartData"],data["chartData"]["chart6"]["viewBys"], data["chartData"]["chart6"]["meassures"], ww7, hh7);

//buildCircularProgress5("chart_1","chart1", data,data["chartData"], data["chartData"]["chart1"]["viewBys"], data["chartData"]["chart6"]["meassures"])

//buildLine5("chart_12_2nd",currData,data["chartData"],data["chartData"]["chart1"]["viewBys"], data["measures"], ww12, hh12);
//buildLine5("chart_13_2nd",currData,data["chartData"],data["chartData"]["chart1"]["viewBys"], data["measures"], ww12, hh12);

//buildBarPortal("chart_7_2nd",currData,data["chartData"]["chart1"]["viewBys"], data["measures"], ww7, hh7);
//buildHalfDonut1("chart_11_2nd",currData,data["chartData"]["chart1"]["viewBys"], data["measures"], ww7, hh7);
////               alert("pp "+jsondata["viewbys"])
//                $("#chartData").val(JSON.stringify(json["chartData"]));
//                $("#viewby").val(JSON.stringify(json["viewbys"]));
//                $("#viewbyIds").val(JSON.stringify(json["viewbyIds"]));
//                $("#measure").val(JSON.stringify(json["measures"]));
//                $("#measureIds").val(JSON.stringify(json["measureIds"]));
//                $("#aggregation").val(JSON.stringify(json["aggregations"]));
//                $("#drilltype").val((json["drillType"]));
//                $("#filters1").val(JSON.stringify(json["filterMap"]));

    

//bubble1("chart_8_2nd", ww9, hh9, layout)
////buildHalfPie1("chart_7_2nd",json,columns, measureArray, ww7, hh7);
//buildHalfPie1("chart_6_2nd", ww6, hh6);
////buildBar1("chart_7_2nd",json,columns, measureArray, ww7, hh7);
//buildPie1("chart_10_2nd",json,columns, measureArray, ww7, hh7);
//buildHalfDonut1("chart_11_2nd",json,columns, measureArray, ww7, hh7);




    })
    });

                    
//  buildIndiaMap("chart_7_2nd",col,meas,ww7,hh7);
//  buildIndiaMapPar("chart_8_2nd",col,meas,ww8,hh8);
//  buildIndiaMap("chart_9_2nd",col,meas,ww7,hh7);
//  buildMultiMeasureLine("trend_chart",columns,measureArray,ww13,hh13)
//  buildIndiaMapPar("chart_10_2nd",col,meas,ww8,hh8);
 // buildWordCloud("chart_3_2nd",columns,measureArray,ww3,hh3);
//   bubble2("chart_8_2nd",columns,measureArray,ww9,hh9)

   
//   buildPieMap("chart_6",columns,measureArray,ww10,hh10)
//   var radius = ww11/3.3;
//   buildPie3D("chart_8",columns,measureArray,ww11,hh11,radius,"pie")
//   buildMultiMeasureTRLine("chart_7_2nd",columns,measureArray,ww12,hh12)
//
//   buildText("chart_1",columns,measureArray)



//        var chHigt=Math.min($("#chart_2").width(), ($("#chart_2").height())),
//fontSize = (chHigt/5);
//$("#chart_2").append("<div id='myStathalf'  style=' font-size: "+(fontSize/3.5)+"px ; margin-left: 0px' data-dimension='"+chHigt+"'  \n\
//  data-text='88%' data-info='Gross Sales Achieved' data-width='20' data-fontsize='"+fontSize+"' \n\
//data-percent='88' data-fgcolor='#DC3912' data-bgcolor='orange'  data-fill='#ddd'></div>");


//$("#chart_4").append("<div id='myStathalf1'  style='  font-size: "+(fontSize/3.5)+"px ; margin-left: 0px' data-dimension='"+(chHigt)+"'  \n\
//  data-text='71%' data-info='Gross Margin Achieved' data-width='20' data-fontsize='"+fontSize+"' \n\
//data-percent='71' data-fgcolor='#DC3912' data-bgcolor='orange'  data-fill='#ddd'></div>");
////data-percent='71' data-fgcolor='#006400' data-bgcolor='lightgreen'  data-fill='#ddd'></div>");

//    $("#chart_1").append("<div id='myStathalf2'  style='  font-size: "+(fontSize/3.5)+"px ; margin-left: 0px' data-dimension='"+(chHigt)+"'  \n\
//  data-text='3.34 M' data-type='full' data-info='Gross Sales' data-width='20' data-fontsize='"+fontSize+"' \n\
//data-percent='71' data-fgcolor='#3366CC' data-bgcolor='skyblue'  data-fill='#ddd'></div>")
//    $("#chart_3").append("<div id='myStathalf3'  style='  font-size: "+(fontSize/3.5)+"px ; margin-left: 0px' data-dimension='"+(chHigt)+"'  \n\
//  data-text='3.79 M' data-type='full' data-info='Gross Margin' data-width='20' data-fontsize='"+fontSize+"' \n\
//data-percent='75' data-fgcolor='#3366CC' data-bgcolor='skyblue'  data-fill='#ddd'></div>")
          });
          var count =0;
           function addgraphs(){

//        alert("in add wid");

        gridsterL.add_widget('<li id="'+count+'" class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('+count+')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget('+count+')" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 4,3);
    count++;
}
    function removeWidget(id){

//           alert("in removewidget"+id)
            gridsterL.remove_widget( $("#"+id) );
            }

            function changeChartLog(chName){

            alert("chart name   "+chName)
var repId=339;
var reportName='Store Wise Head Analysis';
var bizzRoleName='RetailAnalytics';
var username='progen';
// $.post(
////            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
//            'reportViewer.do?reportBy=getAvailableChartsForLogin&reportId='+ repId+"&reportName="+reportName+"&bizzRoleName="+bizzRoleName+"&fromLogin=login&username="+username,
//            function(data) {
//            alert("data111   "+data)
//
//            });
//$("#dialogTestByrep22").dialog({
//                        autoOpen: false,
//                        height: 500,
//                        width: 300,
//                        position: 'justify',
//                        modal: true
//                    });
//
//                    $("#dialogTestByrep22").dialog('open');


 $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
            'reportViewer.do?reportBy=getAvailableChartsForLogin&reportId=' + repId+"&reportName="+reportName+"&bizzRoleName="+bizzRoleName+"&fromLogin=edit&username="+username,
            function(data) {
            alert("data222   "+data)
                if(data=="false"){
            }
            else{
             var chartNo="chart2";
               var jsondata = JSON.parse(JSON.parse(data)["data"]);
//alert("jsondata "+JSON.stringify(JSON.parse(JSON.parse(data)["data"])[chartNo]))
//alert("chartData "+JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]))
            var chartData=JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]);
//                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
                var meta = JSON.parse(JSON.parse(data)["meta"]);
//                $("#viewby").val(JSON.stringify(meta["viewbys"]));
//                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
//                $("#measure").val(JSON.stringify(meta["measures"]));
//                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
//                $("#aggregation").val(JSON.stringify(meta["aggregations"]));
//                $("#drilltype").val((meta["drillType"]));
//                $("#filters1").val(JSON.stringify(meta["filterMap"]));
//     $("#LayoutDetails").val(chartData);

//var LayoutDetails = JSON.parse($("#LayoutDetails").val());
//LayoutDetails["chart1"][chartNo]=meta["chartData"][chartNo];
//
//
////     $("#LayoutDetails").val(JSON.stringify(meta["chartData"][chartNo]));
//alert(JSON.stringify(LayoutDetails))
//     $("#LayoutDetails").val(JSON.stringify(LayoutDetails));






     var ww=$("#"+chName+"_2nd").width() ;
	var hh=$("#"+chName+"_2nd").height() ;

        var currData=[];
//        for(var m=0;m<jsondata[chartNo].length;m++){
        for(var m=0;m<10;m++){
                  currData.push(jsondata[chartNo][m]);
                }
          $("#"+chName).html("");
    $("#"+chName).append("<table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:#3E454D;color:white'><tr>\n\
    <td>"+meta["chartData"][chartNo]["Name"]+"</td></tr></table><div id='"+chName+"_2nd' style='width:100%;height:90%;background-color:white'>\n\
    <img alt='' border='0px' align='right' width='10px' height='10px' title='Change Graph' onclick='changeChartLog("+chName+")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget("+chName+")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></div>")

 var chartType=    meta["chartData"][chartNo]["chartType"]
//         buildLine5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);

            if(chartType=="Vertical-Bar"){

               buildBar5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
//               buildcoffeeWheel(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],"600",chHeight);
              }
              else if(chartType=="Horizontal-Bar"){
                buildHorizontalBar5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="Network"){
//                buildNetwork(chartId,chWidth,chHeight);
              }
              else if(chartType=="Pie"){
                buildPie5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="Line"){
                buildLine5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="SmoothLine"){
//                buildSmoothLine(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="Bubble"){
                bubble5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh,records);
              }
              else if(chartType=="Double-Donut"){
//                buildDoubleDonut(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius*1.5);
              }
              else if(chartType=="Donut"){
//                buildDonut(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius+10);
              }
              else if(chartType=="Half-Donut"){
                buildHalfDonut5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="Half-Pie"){
                buildHalfPie5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="Donut-3D"){
//                buildDonut3D(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Donut");
              }
              else if(chartType=="Pie-3D"){
//                buildDonut3D(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Line"){
                buildMultiMeasureTrLine5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
              }
              else if(chartType=="scatter"){
//                buildScatter(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="OverLaid-Bar-Line"){
//                buildOverlaid(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Area"){
//                buildMultiMeasureArea(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="Grouped-Bar"){
//                 $("#div"+chartId).css("width","100%");
//                var w = document.getElementById("div"+chartId).style

//                buildGroupedBar(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="StackedBar"){
//                buildstackedBar(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="StackedBarH"){
//                buildstackedBarH(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Bar"){
//                buildMultiAxisBar(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="Word-Cloud"){
//                buildWordCloud(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="Area"){
//                buildArea(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="DualAxis-Bar"){
//                buildDualAxisBar(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="GroupedStacked-Bar"){
//                buildGroupedStacked(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
              }
              else if(chartType=="circularProgress"){ //added by manik
//                  buildCircularProgress(chartId,currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"]);
              }
//              else{
//                   buildBar(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],chWidth,chHeight);
//              }
//       $.ajax({
//         async:false,
//         type:"POST",
//         data:
//             $('#graphForm').serialize(),
//         url:  'reportViewer.do?reportBy=getFilters',
//
//      success:function(data) {
//     filterData=JSON.parse(data);
//     generateChart(jsondata);
//      }
//            });


}
            }
            });




        }

        </script>
                 <script>
$( document ).ready(function() {

// var c = $('#myStathalf');
////    var ct = c.get(0).getContext('2d');
//    var container = $(c).parent();
//
//    //Run function when browser resizes
//    $(window).resize( respondCanvas );
//
//    function respondCanvas(){
//        c.attr('data-dimension', $("#chart_2").width() ); //max width
//        c.attr('height', $(container).height() ); //max height
////        alert('pp')
//        //Call a function to redraw other content (texts, images etc)
//    }
//
//    //Initial call
//    respondCanvas();



     $('#myStathalf').circliful();
     $('#myStathalf1').circliful();
     $('#myStathalf2').circliful();
     $('#myStathalf3').circliful();

//	buildCircularProgress(chartId, data, columns, measureArray)


var hieght=$("#chart_5").height();
var widtht=$("#chart_5").width();

var margin = {top: 5, right: 10, bottom: 40, left: 25},
//    width = 90 - margin.left - margin.right,
//    width = widtht - margin.left - margin.right,
//    width = ((widt    ht*0.33)*0.60),
    height_ = hieght - margin.top - margin.bottom;
//    height_ = hieght- margin.bottom ;

var chart = d3.bullet()
    .orient("bottom")
//    .width(width)
    .width(widtht*0.33)
    .height(height_);

d3.json("NewLogin/json/Filler_Bar.json", function(error, data) {
//    alert("error   "+error)
//    alert("data   "+JSON.stringify(data))
  var svg = d3.select("#chart_5").selectAll("svg")
      .data(data)
    .enter().append("svg")
      .attr("class", "bullet")
//      .attr("width", width + margin.left + margin.right)
//      .attr("height", height + margin.top + margin.bottom)
      .attr("height", hieght)
      .attr("width", (widtht*0.33))
//      .attr("height", (hieght*0.33))
    .append("g")
//      .attr("transform", "translate(" + margin.left + "," + margin.top + ")")
      .attr("transform", "translate(" + (widtht*0.08) + "," + margin.top + ")")
      .call(chart);

  var title = svg.append("g")
      .style("text-anchor", "end")
      .attr("transform", "translate(" + (widtht*0.25)+ "," + (height_ + 20) + ")");

  title.append("text")
      .attr("class", "title")
      .text(function(d) { return d.title; });

  title.append("text")
      .attr("class", "subtitle")
      .attr("dy", "1em")
      .text(function(d) { return d.subtitle; });


});

//function randomize(d) {
//  if (!d.randomizer) d.randomizer = randomizer(d);
//  d.ranges = d.ranges.map(d.randomizer);
//  d.markers = d.markers.map(d.randomizer);
//  d.measures = d.measures.map(d.randomizer);
//  return d;
//}
//
//function randomizer(d) {
//  var k = d3.max(d.ranges) * .2;
//  return function(d) {
//    return Math.max(0, d + k * (Math.random() - .5));
//  };
//}
  
    });
</script>
    </head>
    <body style="height: 100%">


<jsp:include page="Headerfolder/headerPage.jsp"/>
  <!--       <div class="" id="my_tooltip" style="display: none"></div>-->
        <div id="divForm" class="centerDiv" style="height:650px" >
        <form action="" method="POST" id="loginForm" style="display: none">
<!--                 <input type="hidden" id="viewby" name="viewby"/>-->
            <input type="hidden" name="LayoutDetails" id="LayoutDetails" value="">
            <input type="hidden" name="usernameLog" id="usernameLog" value="<%=LOGINID%>">
        </form>
            <div id="divHeader" style="width:100%;height: 10%;background-image:url('../header_bg.png');background-color:white ">

              <form name="myForm1" action="" method="post">
            <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">
            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">
                <%--un comment for indicus only--%>
                  <%if(isCompanyValid){%>
        <input type="hidden" name="accounttype" id="accounttype" value="">
              <%}%>
            <table class="" width="100%">
                <tr>
                   <td valign="top" style="height:30px;width:10%;">
                     <% if(!(isCompanyValid)) { %>
                     <img class="left" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>">
<!--                     <img alt=""  width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>"/>
                     <img  src="<%=request.getContextPath()%>/images/NEWProGen.png" style="height:50px" alt=""> -->
                     <%}%>
                    </td>
                    <td valign="top" style="height:30px;width:80%" >

                    </td>
                   <td valign="top" style="height:30px;width:10%;" align="right">
                       <img class="right" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>">
<!--                      <img style="height:70px" src="<%=request.getContextPath()%>/images/Newpi_Logo.png" alt="">
                        <img alt=""  width="0px" height="50px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"-->
                    </td>
                </tr>
            </table>
                     
<div id="report" style="margin-left:70%;width: 28%;width: 380px " >
</div>

        </form>
            </div>

         

            <div id="divDropDwn" style="width:100%;height: 5%;background-color:  brown  ">
                <div id="wrapper">
       <nav>
          <ul id="drpdwn" class="menu">
          <li class=" "><a href="#"><span>Sales Analysis</span></a>
          <ul class="sub-menu">

<!--              <li><a target="_blank" href="http://mhrd.gov.in/about-mhrd">About us</a></li>-->
<!--              <li><a href="#">Important links</a></li>-->
<!--              <li><a target="_blank" href="http://mhrd.gov.in/sites/upload_files/mhrd/files/upload_document/Eng_Swachch%20Bharat%20Swachch%20Vidhalaya.pdf">About the Programme</a></li>-->
            </ul>
          </li>
          <li><a href="#"><span>Finance Manager</span></a>
           <ul class="sub-menu">
<!--                <li><a href="http://125.63.72.116:8085/swachhvidhyalaya/" target="_blank">State Govt.</a></li>-->
           </ul>
          </li>
          <li><a href="#"><span>Management dashboard</span></a>
             <ul class="sub-menu">
<!--                 <li><a href="http://125.63.72.116:8085/resources/pdf/Toilet Information Kit.zip">Design kit</a></li>-->
             </ul>
          </li>
          <li><a href="#"><span>Airline Agency</span></a>
            <ul class="sub-menu">

<!--              <li><a href="http://125.63.72.116:8085/swachhvidhyalaya/updateProgressMonitoringReport" target="_blank" >Status of Construction</a></li>-->
            </ul>
          </li>

          <li><a href="#"><span>Bank Credit Manager</span></a>
          <ul class="sub-menu">
<!--              <li><a href="#" onclick="openhelpline()">Technical team</a></li>-->
            </ul>
          </li>
	 <li>
<!--             <a  id="loginButton" title="Click to Login"><span>Login</span></a>-->


<a  id="SaveButton" onclick="saveLoginPage()" title="Click to Save"><span>Save </span></a>

             <div style="clear:both"></div>
                <div id="loginBox">
                    <form name="loginForm" onLoad="setFocusPortal()" action="" id="loginForm" onSubmit="return doLoginPortal('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST">
<!--        <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">-->
            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">

                     <fieldset id="body">
<!--                            <fieldset>
                                <label for="User Name">User Name</label>
                                <input type="text" name="user" id="user" value="User Name" />
                            </fieldset>
                            <fieldset>
                                <label for="password">Password</label>
                                <input type="password" name="password" id="password" value="nullnull"/>
                            </fieldset>-->
                                        <li style="background-color: white; ">
						<input class="" name="user" id="user" value="User Name" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'User Name';}" type="text">
					</li>
					<li style="background-color: white; ">
						<input value="nullnull" id="password" name="password" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Password';}" type="password">
					</li>
                            <input type="submit" id="login" value="Login" />
                            <!--<label for="checkbox"><input type="checkbox" id="checkbox" />Remember me</label>-->
                            <h2><a style="color:#3E454D;" class="" href="javascript:void(0);" onclick='gotoRegisterPortal("<%=request.getContextPath()%>/baseAction.do?param=register")' >Register</a></h2>
                        </fieldset>
                        <span><a href="#">Forgot your password?</a></span>
                    </form>

                </div>
          </li>

        </ul>
      </nav>
    </div>
            </div>
            <div id="divBody" style="width:100%;height: 85%;background-color: transparent;float:left ">
               <div id="gridsterDivLogin" class="gridster" style="float: left">
                <ul id="gridUL_Login" type="none" style="width: 100%">

            </ul>
                   <div class="" id="customLoginPage" style="">
<!--                       <button style="position:fixed;top:0px;right:0px;" onclick="addwidget()">Add Graph</button>-->
<!--<img  alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addgraphs()" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>-->
<a onclick="addgraphs()" style="cursor:pointer;">Add Graphs</a>
  </div>
             </div>
                <div style="width: 100%;height: 100%;background-color: transparent;float: left">
<!--                    <div id="chart_1" class="chart_1_cl">
                        <img  alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_1')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                         <div id="myStathalf1"  style=" margin-left: 7%"data-dimension="265"    data-text="100%" data-info="New Toilets Supported" data-width="30"
					data-fontsize="35" data-percent="100" data-fgcolor="#006400" data-bgcolor="#eee"  data-fill="#ddd">
			</div>
                    </div>
                    <div id="chart_2" class="chart_1_cl">
                 <img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_2')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                    </div>
                    <div id="chart_3" class="chart_1_cl">
<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_3')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                                          <div id="myStathalf"  style=" margin-left: 7%" data-dimension="265"    data-text="47%" data-info="Dysfunctional Toilets" data-width="30"
					data-fontsize="35" data-percent="47" data-fgcolor="#006400" data-bgcolor="#eee"  data-fill="#ddd">
		</div>
                     </div>
                    <div id="chart_4" class="chart_1_cl">
           <img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_4')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                    </div>
                    <div id="chart_5" class="chart_5_cl" style="" >
                	 <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#1B3E70;color:white"><tr><td></td></tr></table>
			 <div id="chart_5_2nd" style="width:90%;height:80%;margin-top:5%;margin-left:5%;background-color:white">
			 </div>
               <img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_5')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                    </div>-->
<!--                 <div id="chart_6" class="chart_6_cl" style="">
              
                     <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Category Basis Sales Analysis</td></tr></table>
		 <div id="chart_6_2nd" style="height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_6')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_7" class="chart_6_cl" style="margin-left: 1%;margin-right: 1%">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Region Basis Sales Analysis</td></tr></table>
		 <div id="chart_7_2nd" style="height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_7')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_8" class="chart_6_cl" style="">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Brand Basis Sales</td></tr></table>
		 <div id="chart_8_2nd" style="width:100%;height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_8')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_9" class="chart_6_cl" style="">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>State Basis Sales</td></tr></table>
		 <div id="chart_9_2nd" style="width:100%;height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_9')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_10" class="chart_6_cl" style="margin-left: 1%;margin-right: 1%">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Store Basis Sales</td></tr></table>
		 <div id="chart_10_2nd" style="width:100%;height:80%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_10')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_11" class="chart_6_cl" style="">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Order Type</td></tr></table>
		 <div id="chart_11_2nd" style="width:100%;height:90%;background-color:white">
		 <img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_11')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_12" class="chart_12_cl" style="margin-right: 1%">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Gross Sales Trend</td></tr></table>
		 <div id="chart_12_2nd" style="width:100%;height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_12')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>
                 <div id="chart_13" class="chart_12_cl" style="">
                    <table style="width:100%;height:10%;font-family: 'Open Sans', sans-serif;font-size: 14px;background-color:#3E454D;color:white"><tr><td>Gross Margin Trend</td></tr></table>
		 <div id="chart_13_2nd" style="width:100%;height:90%;background-color:white">
		<img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog('chart_13')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>
                 </div>
                </div>-->
                </div>
                </div>
<!--		<div style="width:100%;margin-left:0px;font-family: 'Open Sans', sans-serif;font-size: 14px;margin-top:50px;height:auto;float:left">
				<table width="100%" class="" >
                <tr style="height:10px;width:100%;max-height:100%;background-color:#e78f08">
                    <td style="height:10px;width:100%" class="">
                        <center ><font  style="color:#000;font-size:10px;font-family:verdana;">Powered by Analytical engine from <a href="http://www.progenbusiness.com" style="color:blue;font-weight:bold;font-family:verdana;font-size:11px">Progen Business Solutions.</a></font></center>
                    </td>
                </tr>
            </table>-->
  <div class="tooltip" id="my_tooltip" style="display: none"></div>
  

     
        </div>

  <div id="dialogTestByrep11" style="display: none" title="Report Graphs">
                <table>
                    <tr>
                                <td><input type="radio" id="reportTime" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;"  onclick="settimetoreport()"  checked=''    >Report Time</td>
<!--<td><input type="radio" id="oneviewTime" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;"  onclick="settimeto()"  >Oneview Time</td>  </tr>-->

                    <tr>
                        <td id="busNamId"  >Role:</td>
                        <td id="busroleTdId"  >
                            <select id="busroleId" onchange="busroelIdTest()" >

                                <option value="">select</option>
                                <%
                                            if (!busRoleIDs.isEmpty()) {
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }%>
                            </select>
                        </td>
                    </tr></table>
                <table width="100%" id="repDialogTest">

                </table>

            </div>

  <script type="text/javascript">
//   var LayoutDetails='';
//    function addgraphs(){
//        alert("in add wid");
//    }

//           alert("in add wid");
//           gridsterL.add_widget('<li class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog("\chart_11"\)" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget()" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 2, 1);
//            }
    
            function doLoginPortal(conPath,REPORTID,isCompanyValid){
//                 alert('hi')
                var succFlag = true;

                if(document.loginForm.user.value == null || document.loginForm.user.value == '' || document.loginForm.user.value == 'User Name')
                {
                    alert("Please enter username");
                    document.loginForm.user.focus();
                    succFlag = false;
                }
                else if(document.loginForm.password.value == null || document.loginForm.password.value == '' || document.loginForm.password.value == 'nullnull')
                {

                    alert("Please enter password");
                    document.loginForm.password.focus();
                    succFlag = false;
                }

                        else if( isCompanyValid==true)
                        {  if((document.loginForm.accounttype.value == null || document.loginForm.accounttype.value == '')){
                                alert("Please enter Company Name");
                                document.loginForm.accounttype.focus();
                                succFlag = false;
                            }
                        }
                        // alert('succFlag '+succFlag)
                        if(succFlag)
                        {
//                           document.getElementById("user").value=document.getElementById("user1").value;
//                            document.getElementById("password").value=document.getElementById("password1").value;
                            document.getElementById("screenwidth").value=screen.width;
                            document.getElementById("screenheight").value=screen.height;

                                 if(isCompanyValid==true){
                                     //  alert('hyyyy ')

                                     document.getElementById("accounttype").value=document.getElementById("accounttype").value;
                                     // alert(document.getElementById("accounttype").value)
                                 }
//                                   alert( document.getElementById("user").value)
//                                   alert( document.getElementById("password").value)
                                 if(REPORTID==""){
                                     document.loginForm.action=conPath + "/baseAction.do?param=loginApplication";
                                 }else{
                                     document.loginForm.action=conPath + "/baseAction.do?param=loginApplication&REPORTID="+REPORTID;
                                 }
                                 //alert( document.loginForm.action)
                                 document.loginForm.submit();
                             }
                             return false;
                         }
                         function setFocusPortal()
                         {
                             document.loginForm.user.focus();
                         }
                         function goLoginPortal(){
                             document.forms.loginForm.submit();
                         }
                         function gotopswrdpagesPortal(path)
                         {
//                             $.ajax({
//                                 data:$("#updtepswrdfrm").serialize(),
//                                 type:"POST"
//                                   url:ctxPath+"/passwordAction.do?pswrdparam=updatePassword",
//                                   function:success(data){
//
//                                   }
//                             })

                             //alert(path);
//                             document.loginForm.action=path;
//                             document.loginForm.submit();
                         }
						  function gotoRegisterPortal(path)
                         {
                             //alert(path);
                             document.loginForm.action=path;
                             document.loginForm.submit();
                         }
                         function saveLoginPage(){
//                             alert("details    "+$("#LayoutDetails").val());
                             alert("Your Layout Saved Successfully  ");

//var LayoutDetails = JSON.parse($("#LayoutDetails").val());
// var charts = Object.keys(LayoutDetails);
////var gridsPos=grid.serialize1();
//var gridsPos=grid.serialize1();
// var chartId="";
//for (var k = 0; k < charts.length; k++) {
//
//chartId = "chart" + (parseInt(k) + 1);
//LayoutDetails[chartId]["id"]=gridsPos[k]["id"];
//LayoutDetails[chartId]["row"]=gridsPos[k]["row"];
//LayoutDetails[chartId]["col"]=gridsPos[k]["col"];
//LayoutDetails[chartId]["size_x"]=gridsPos[k]["size_x"];
//LayoutDetails[chartId]["size_y"]=gridsPos[k]["size_y"];

//                             $.ajax({
//         async:false,
//         type:"POST",
//         data:
//             $('#loginForm').serialize(),
//         url:  'reportViewer.do?reportBy=saveLoginCharts',
//
//      success:function(data) {
//          alert("Your Charts Saved")
//      }
//            });
                         }

              var     busroleId='';
              var     busrolename='';
        function busroelIdTest(type){
                busroleId = $("#busroleId").val();

               busrolename = $("#busroleId option:selected").text();
               alert("rolename................."+busrolename)

                $.ajax({
                    url:'<%=request.getContextPath()%>'+'/reportTemplateAction.do?templateParam=viewByReports&busroleID='+busroleId+'&busrolename='+busrolename,
                    success:function(data)
                    {
                        var jsonVar=eval('('+data+')');
                        var reporthtml='';
                        var tempReportId='null';
                        var first = 0;
                        var second = 1;
                        var repId='';
                        var repName="";
                        var graphId='';
                        var graphName="";
                        reporthtml+="<ul>";
                        var i=0;
                        for(i=0;i<jsonVar.ReportIds.length;i++){
 var ch=1;

                            repId = jsonVar.ReportIds[i];
                            repName = jsonVar.ReportNames[i];
                            graphId = jsonVar.GraphIds[i];
                            graphName = jsonVar.GraphNames[i];

                          graphId=  repId;
                            alert("graphName....."+graphName+"grp..."+graphId+"repName..."+repName);
//                            if (tempReportId == 'null') {
//                                tempReportId = repId;
                                var chname="chart"+ch
//                                //
                                reporthtml+="<li class='closed' id='" + repName + i + "'>";
                                reporthtml+="<img src='icons pinvoke/report.png'></img>";
                                reporthtml+="<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph('" + repId + "')\">" + repName + "</span>";
                                reporthtml+="<ul id='repName-" + repId + "' style='display:none'>";

var h=i
//i++;
var isflag='true';
                                for(var i1=h;i1<jsonVar.ReportIds.length;i1++){chname="chart"+ch
                                    if(isflag=='true'){
                                         repId = jsonVar.ReportIds[i1];
//                                    repId = jsonVar.ReportIds[h];
                                graphId = jsonVar.ReportIds[i1];
                                 graphName = jsonVar.GraphNames[i1];

                                reporthtml+="<li class='closed' id='" + graphId + "'>";
                                reporthtml+="<img src='icons pinvoke/chart.png'></img>";
                                if(graphName==null ||graphName==""||graphName=="null"){
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + repName + "</a></span>";
                                }else{
                                reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + graphName + "</a></span>";
                                }
                                reporthtml+="</li>";ch++;chname="chart"+ch
                                }
//                                else{
                                    var repIdOld1 = jsonVar.ReportIds[i1+1];
//                                    repId = jsonVar.ReportIds[h];
                                    var graphId11 = jsonVar.ReportIds[i1+1];
                                 graphName = jsonVar.GraphNames[i1+1];
                      if(repIdOld1==repId ){
 if(graphId11!=''){
                                    reporthtml+="<li class='closed' id='" + graphId + "'>";
                                    reporthtml+="<img src='icons pinvoke/chart.png'></img>";
                                if(graphName==null ||graphName==""||graphName=="null"){
                           reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + repName + "</a></span>";
                        }else{
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + graphName + "</a></span>";
                                    reporthtml+="</li>";
                                     }ch++
                                     }i++;
                                }else{
                                    break;
                                }
//                                }
                                isflag='false';
                                }

//
                                    reporthtml+="</ul>";
                                    reporthtml+="</li>";
//

                        }


                        reporthtml+="</ul>";
                        reporthtml+="</li>";
                        reporthtml+="</ul>";



                        $("#repDialogTest").html(reporthtml);
                         $('table#repDialogTest li').quicksearch({
                    position: 'before',
                    attached: 'table#repDialogTest',
                    loaderText: '',
                    delay: 100
                });

                        //                            $("#repDialogTest").html("<ul>"+jsonVar+"</ul>");

                        $("#dialogTestByrep").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                        $("#dialogTestByrep").dialog('open');
                    }
                });

            }
            function settimetoreport(){
//              document.getElementById("measureType"+divId).style.display='none';
var divId=parent.colNumber;
var oneviewId=parent.oneviewID
               if(document.getElementById("oneviewTime").checked){
                   alert("kkkkkkkk")
                     document.getElementById("oneviewTime").checked = false;
//                     parent.oneviewTimecheck="true"
               }
               alert("pppppppppp")
                document.getElementById("reportTime").checked = true;
//                if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
//                 $("#reigonOptionsDivId"+divId).toggle(500);
//                }
   parent.oneviewTimecheck="true"
                $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+divId+'&oneviewId='+oneviewId+"&oneviewTime=true",
                        success: function(data){
                        }
                    });
            }
//            function removeWidget(){
//            alert("in removewidget")
//            gridsterL.remove_widget( $('.gridster li').eq(1) );
//            }
//            function addgraphs(){
//
//           alert("in add wid")
//           gridsterL.add_widget('<li class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="changeChartLog("\chart_11"\)" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget()" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 2, 1);
//            }
        </script>
    </body>
</html>


