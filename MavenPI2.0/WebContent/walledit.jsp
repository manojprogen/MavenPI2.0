<%--
    Document   : walledit
    Created on : Mar 25, 2015, 11:52:11 AM
    Author     : Manik Srivastava
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.PbDb,java.util.*,prg.db.Session,java.sql.*,prg.db.Session,java.util.ArrayList,java.util.List,utils.db.*,prg.db.PbReturnObject" %>
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
String fromWall=String.valueOf(session.getAttribute("fromWall"));

  List<String> busRoleIDs = new ArrayList<String>();
  List<String> busRoleNames = new ArrayList<String>();
 busRoleIDs = (List<String>) request.getAttribute("BusRoleIds");
 busRoleNames = (List<String>) request.getAttribute("BusRoleNames");
 if(busRoleIDs==null){
     String contextPath1=request.getContextPath();
     }
%>
<!--<html style="background-color:#1B3E70"      >-->
<html lang="en">
<head>
<meta charset="utf-8">
<title>pi</title>
<!--<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">-->
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/css/smoothness_jquery-ui.css"></script>-->
 <script src="<%=request.getContextPath()%>/NewLogin/JS/jquery-1.10.2.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/NewLogin/JS/jquery-ui.min.js"></script>
 <link rel="stylesheet" href="<%=request.getContextPath()%>/css/smoothness_jquery-ui.css">
    <link href="<%=request.getContextPath()%>/css/d3/xtendChart.css" rel="stylesheet" type="text/css" />
 <script type="text/javascript" src="<%=request.getContextPath()%>/NewLogin/JS/Grid/jquery.gridster.js"></script>
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/Grid/jquery.gridster.css" /> <!-- added by manik-->
<!-- <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />-->

        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/pbReportViewerCSSLogin.css">
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/tooltip.css" rel="stylesheet"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.layout.cloud.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/customtooltip.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypes.js"></script>

<script  type="text/javascript" src="<%=request.getContextPath()%>/NewLogin/JS/bullet.js" ></script>
<!--<script  type="text/javascript" src="<%=request.getContextPath()%>/NewLogin/JS/LoginCharts.js" ></script>-->
<!--<script src="//code.jquery.com/jquery-1.10.2.js"></script>-->
<!--<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>-->

<!--<link rel="stylesheet" href="/resources/demos/style.css">-->
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
         .white_content{position:absolute;top:20%;width:28%;height:399px;padding:5px;z-index:1002;-moz-border-radius-bottomleft:10px;-moz-border-radius-bottomright:10px;-moz-border-radius-topleft:10px;-moz-border-radius-topright:10px}
.white1_content{position:absolute;top:30%;width:452px;height:180px;padding:5px;z-index:1002;-moz-border-radius-bottomleft:10px;-moz-border-radius-bottomright:10px;-moz-border-radius-topleft:10px;-moz-border-radius-topright:10px}
.black_overlay{position:absolute;top:0;left:0;width:100%;height:100%;background-color:#fff;z-index:1001;-moz-opacity:.8;opacity:.5;filter:alpha(opacity=60);overflow:auto}
.upperTab{position:absolute;top:1%;left:0;border-bottom:1px solid #87CEEB}
.bottomTab{position:absolute;top:92%;left:0}
.middleTab{position:absolute;top:10%;left:30%}
.right{height:50px;width:140px}
.left{height:40px;width:60px}
#customLoginPage{position:fixed;top:0;width:45%;height:300px;margin-left:37.5%;margin-right:37.5%;border:none;z-index:50}
#tabdetail label,#tabdetail input{display:block}
#tabdetail label{margin-top:.5em}
#tabdetail input,#tabdetail textarea{width:95%}
#tabs li .ui-icon-close{float:left;margin:.4em .2em 0 0;cursor:pointer}
#add_tab{cursor:pointer}
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
</style>

</head>
<body>
<div id="tabdetail" title="Add tab Name">
<form>
<fieldset class="ui-helper-reset">
<label for="tab_title">Title</label>
<input type="text" name="tab_title" id="tab_title" value="Tab Title" class="ui-widget-content ui-corner-all">
<!--<label for="tab_content">Content</label>
<textarea name="tab_content" id="tab_content" class="ui-widget-content ui-corner-all">Tab content</textarea>-->
</fieldset>
</form>
</div>
    <div class="tooltip" id="my_tooltip" style="display: none"></div>

<div id="divForm" class="centerDiv" style="height:650px" >
        <form action="" method="POST" id="loginForm1" style="display: none">
<!--                 <input type="hidden" id="viewby" name="viewby"/>-->
            <input type="hidden" name="LayoutDetails" id="LayoutDetails" value="layoutDetails">
            <input type="hidden" name="usernameLog" id="usernameLog" value="<%=LOGINID%>">
            <input type="hidden" name="tabdetail" id="tabdetail" value="">
            <input type="hidden" name="tabData" id="tabData" value="">
            <input type="hidden" name="graphdetail" id="graphdetail" value="">
            <input type="hidden" name="chartData" id="chartData" >
            <input type="hidden" name="graphsList" id="graphsList" >
            <input type="hidden" name="currentTab" id="currentTab" >
        </form>
            <div id="divHeader" style="width:100%;height: 10%;background-image:url('../header_bg.png');background-color:white ">

              <form name="myForm1" action="" method="post">
            <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">
<!--            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">-->
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
                        <% if(fromWall.equalsIgnoreCase("edit")){%>
<td align="right" width="2%" style="padding-top: 15px"><a href="javascript:void(0)" class="extlinkicon" onclick="javascript:logout()" title="Logout" style="text-decoration:none"><img alt="" src="images/extlink.gif" width="25px" height="24px" border="o"/></a></td>
<%}%>
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



<div id="tabs">
<ul>
<!--<li><a href="#tabs-1">Sales Analysis</a> <span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span></li>
<li><a href="#tabs-2">Finance Manager</a> <span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span></li>
<li><a href="#tabs-3">Management Dashboard</a> <span class="ui-icon ui-icon-close" role="presentation">Remove Tab</span></li>-->
</ul>


<div id="tabs-1">

 <div id="gridsterDivLogin" class="gridster" style="float: left">
                <ul id="gridUL_Login" type="none" style="width: 100%">
<!--<li style="display: list-item;" data-sizey="3" data-sizex="4" data-row="1" data-col="1" id="chart1" class="new gs-w">
    <img alt="" title="Change Graph" onclick="changeChartLog(0)" style="cursor:pointer;position:absolute;display:inline;" src="/pi23Mar_Resize_Dlog/images/plus.png" align="right" border="0px" height="10px" width="10px">
    <img alt="" title="Change Graph" onclick="removeWidget(0)" style="cursor:pointer;position:relative;display:inline;" src="/pi23Mar_Resize_Dlog/images/deleteWidget.png" align="right" border="0px" height="15px" width="15px">
    <span class="gs-resize-handle gs-resize-handle-both"></span>
</li>
<li style="display: list-item;" data-sizey="3" data-sizex="4" data-row="1" data-col="1" id="chart2" class="new gs-w">
    <img alt="" title="Change Graph" onclick="changeChartLog(0)" style="cursor:pointer;position:absolute;display:inline;" src="/pi23Mar_Resize_Dlog/images/plus.png" align="right" border="0px" height="10px" width="10px">
    <img alt="" title="Change Graph" onclick="removeWidget(0)" style="cursor:pointer;position:relative;display:inline;" src="/pi23Mar_Resize_Dlog/images/deleteWidget.png" align="right" border="0px" height="15px" width="15px">
    <span class="gs-resize-handle gs-resize-handle-both"></span>
</li>-->
            </ul>
</div>


</div>




 <div class="" id="customLoginPage" style="display: none">
<!--                       <button style="position:fixed;top:0px;right:0px;" onclick="addwidget()">Add Graph</button>-->
<!--<img  alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addgraphs()" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/>-->
<!--<a onclick="addgraphs()" style="cursor:pointer;">Add Graphs</a>-->
<button id="add_tab">Add Tab</button>

<!--<button id="add_tab1">Add Tab</button>-->
<button id="add_graphRegion" onclick="addgraphsRegion()">Add Graph Region</button>
<button id="saveLay" onclick="saveLayouts()">Save Layout</button>
  </div>
<div style="clear:both"></div>
                <div id="loginBox">
                    <form name="loginForm" onLoad="setFocusPortal()" action="" id="loginForm" onSubmit="return doLoginPortal('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST">
<!--        <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">-->
            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">
            <input type="hidden" name="LayoutDetails" id="LayoutDetails" value="layoutDetails">
            <input type="hidden" name="usernameLog" id="usernameLog" value="<%=LOGINID%>">
            <input type="hidden" name="tabdetail" id="tabdetail" value="">
            <input type="hidden" name="tabData" id="tabData" value="">
            <input type="hidden" name="graphdetail" id="graphdetail" value="">
            <input type="hidden" name="chartData" id="chartData" >
            <input type="hidden" name="graphsList" id="graphsList" >
            <input type="hidden" name="currentTab" id="currentTab" >
            <input type="hidden" name="chartGridDetail" id="chartGridDetail" >
            <input type="hidden" name="fromWall" id="fromWall" >
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
</div>
                        </div>
             <div id="dialogTestByrepwal" style="display: none" title="One ViewBys">
                <table>
                    <tr>
                        <td id="busNamIdwal"  >Role:</td>
                        <td id="busroleTdIdwal"  >
                            <select id="busroleIdwal" onchange="busroelIdTestwal()" >

                                <option value="">select</option>
                                <%

                                            if (busRoleIDs == null || busRoleIDs.isEmpty()) {

                                                }else{
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }
%>
                            </select>
                        </td>
                    </tr></table>
                <table width="100%" id="repDialogTestwal">

                </table>

            </div>

<script type="text/javascript">
       function selectedType1(type,tdId,col,widt,hight,regionNameId,typeId,oneviewId)
            {
                 //var reportIdforsending=new Array();
                // var reportNamesforsending=new Array();
                // var ListArray=new Array
                $(".overlapDiv").hide();
                $("#selectTypeId").dialog('close');
                 if(document.getElementById("readdDivId"+col).style.display=='block'){
                    $("#readdDivId"+col).toggle(500);
                }
                    oneViewIdValue=oneviewId;
                measureOpti=typeId;
                dashletId = tdId;
                colNumber = col;
                var RS=$("#"+col+"").attr('rowspan');
                var CS=$("#"+col+"").attr('colspan');
                if(RS>1 && oneviewHeight!='')
                    height=(RS*oneviewHeight)+(RS*25);
                else
                    height=hight;
                    width =CS*widt;
                regionName=regionNameId
                $("#"+tdId).show();
                if(type=='report'){
                    $("#busNamId").show();
                    $("#dialogTestByrepWall").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#dialogTestByrepWall").dialog('open');
                }




            }
       function addGraphss(divid,tabId){
        graphRegionId=divid;
        $("#dialogTestByrepwal").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#dialogTestByrepwal").dialog('open');
//          selectTypeDiv('Dashlets-0',0,623,80,'Dashlets0','GrpType0');

//          selectedType1('report','Dashlets-0',0,623,80,'Dashlets0','GrpTyp0','357')
//          selectedType1('report',divid,0,623,80,'Dashlets0','GrpTyp0','357')

        }
        function addGraphss1(bizzRoleName,repId,reportName,graphId,tabId,chartId,chartname){
//var repId=339;
//var reportName='Store Wise Head Analysis';
//var bizzRoleName='RetailAnalytics';
 var username='progen';
// var chartId='chart1';
//alert("innnnnn")
        $("#loginForm").serialize();
 $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
            "reportViewer.do?reportBy=getAvailableChartsForLogin&reportId=" + repId+"&reportName="+reportName+"&bizzRoleName="+bizzRoleName+"&fromLogin=edit&username="+username+"&graphId="+graphId+"&currentTab="+tabId+"&chartId="+chartId,
            function(data) {
//            alert("datacharts   "+data)
                if(data=="false"){
            }else{
//alert("divId  ")

                var chartNo=chartId;
               var jsondata = JSON.parse(JSON.parse(data)["data"]);
               var chartData=JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]);
                var meta = JSON.parse(JSON.parse(data)["meta"]);
                var divId=graphId;
//                alert("divId  "+divId)
//                var liId=divId.replace("chart","" );
                var liId=divId.replace ( /[^\d.]/g, '' );

                  var liID="chart_LI"+liId;
//                  alert("liID  "+liID)
               var ww=$("#"+divId).width() ;
	var hh=$("#"+divId).height() ;

        var currData=[];
//        for(var m=0;m<jsondata[chartNo].length;m++){
        for(var m=0;m<10;m++){
                  currData.push(jsondata[chartNo][m]);
                }
//                chart_LI4

          $("#"+liID).html("");

//    $("#"+liID).append("<table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:#3E454D;color:white'><tr>\n\
//    <td>"+meta["chartData"][chartNo]["Name"]+"</td></tr></table><div id='"+divId+"' style='width:100%;height:90%;background-color:white'>\n\
//    <img alt='' border='0px' align='right' width='10px' height='10px' title='Change Graph' onclick='changeChartLog("+divId+")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget("+divId+")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></div>")

var id="chart"+liId;
var idLI="chart_LI"+liId;
$("#"+liID).append("<table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:grey;color:white'><tr>\n\
    <td>"+chartname+"</td><td style='width:35px' ><img id='plusPng' alt='' border='0px' align='right' width='12px' height='12px' title='Change Graph' onclick='addGraphss(\""+id+"\",\""+tabId+"\")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img id='deleteWidget' alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget(\""+idLI+"\")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></td></tr></table><div id='"+chartId+"' style='width:100%;height:90%;background-color:white; border: 1px dotted rgb(169, 169, 169);'>\n\
    </div>");

try{
//is(chartId,chartType,chartName,viewBys,measures,graphData)
buildBar5(chName+"_2nd",chartNo,currData,meta["chartData"],meta["chartData"][chartNo]["viewBys"], meta["chartData"][chartNo]["meassures"], ww, hh);
chartTypeFunctionthis(chartId,jsondata["graphs"][divId]["reportMeta"]["chartData"][chartId]["chartType"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["chartName"],meta["chartData"][chartNo]["viewBys"],meta["chartData"][chartNo]["meassures"],jsondata["graphs"][ch]["data"])
//chartTypeFunctionthis(divId,jsondata["graphs"][divId]["reportMeta"]["chartData"]["chart1"]["chartType"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["chartName"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["viewBys"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["meassures"],jsondata["graphs"][ch]["data"])
                   }catch(e){}
            }
        });
        

}

        function addgraphsRegion(){

//alert("pppp")
var graphsList=[], index;
if(typeof $("#graphsList").val()!="undefined" && $("#graphsList").val()!=""){
graphsList = JSON.parse($("#graphsList").val());
 index = parseInt(graphsList.length)+1;
graphsList.push("chart"+index);
}
else{
   graphsList.push("chart1");
   index=1;
}
//
//alert(graphsList+"....")
var id="chart"+index;
//var idLI="chart_LI"+index;
var tabId=$("#currentTab").val();
$("#graphsList").val(JSON.stringify(graphsList));
//        gridsterL.add_widget('<li id="chart'+index+'" class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addGraphss(\''+id+'\',\''+tabId+'\')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget(\''+id+'\')" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 4,3);
        gridsterL.add_widget('<li id="chart_LI'+index+'" class="new"><img id="plusPng" alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addGraphss(\''+id+'\',\''+tabId+'\')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img id="deleteWidget" alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget(\''+id+'\')" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 4,3);


////     gridsterL.add_widget("<li id='chart_LI"+index+"' class='new'><table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:#3E454D;color:white'><tr>\n\
//    <td></td></tr></table><div id='chart//"+index+"' style='width:100%;height:90%;background-color:white; border: 1px dotted rgb(169, 169, 169);'>\n\
//    <img alt='' border='0px' align='right' width='10px' height='10px' title='Change Graph' onclick='addGraphss(\""+id+"\",\""+tabId+"\")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget(\""+idLI+"\")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></div></li>", 4,3);

}
        function addgraphsR(){

//alert("pppp")
var graphsList=[], index;
if(typeof $("#graphsList").val()!="undefined" && $("#graphsList").val()!=""){
graphsList = JSON.parse($("#graphsList").val());
 index = parseInt(graphsList.length)+1;
graphsList.push("chart"+index);
}
else{
   graphsList.push("chart1");
   index=1;
}

//alert(graphsList+"....")
var id="chart"+index;
var idLI="chart_LI"+index;
var tabId=$("#currentTab").val();
$("#graphsList").val(JSON.stringify(graphsList));
//        gridsterL.add_widget('<li id="chart'+index+'" class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addGraphss(\''+id+'\',\''+tabId+'\')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget(\''+id+'\')" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 4,3);
//        gridsterL.add_widget('<li id="chart'+index+'" class="new"><img alt="" border="0px" align="right" width="10px" height="10px" title="Change Graph" onclick="addGraphss(\''+id+'\',\''+tabId+'\')" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/plus.png"/><img alt="" border="0px" align="right" width="15px" height="15px" title="Change Graph" onclick="removeWidget(\''+id+'\')" style="cursor:pointer;position:relative;display:inline;" src="<%=request.getContextPath()%>/images/deleteWidget.png"/></li>', 4,3);


//     gridsterL.add_widget("<li id='chart_LI"+index+"' class='new'><table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:#3E454D;color:white'><tr>\n\
//    <td>chart"+index+"</td></tr></table><div id='chart"+index+"' style='width:100%;height:90%;background-color:white; border: 1px dotted rgb(169, 169, 169);'>\n\
//    <img alt='' border='0px' align='right' width='10px' height='10px' title='Change Graph' onclick='addGraphss(\""+id+"\",\""+tabId+"\")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget(\""+idLI+"\")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></div></li>", 4,3);

if(fromwall=="login" || fromwall=="null"){
  gridsterL.add_widget("<li id='chart_LI"+index+"' class='new'><table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:grey;color:white'><tr>\n\
    <td>chart"+index+"</td><td style='width:35px' ></td></tr></table><div id='chart"+index+"' style='width:100%;height:90%;background-color:white; border: 1px dotted rgb(169, 169, 169);'>\n\
    </div></li>", 4,3);
}else{
     gridsterL.add_widget("<li id='chart_LI"+index+"' class='new'><table style='width:100%;height:10%;font-family: Open Sans, sans-serif;font-size: 14px;background-color:grey;color:white'><tr>\n\
    <td>chart"+index+"</td><td style='width:35px' ><img id='plusPng' alt='' border='0px' align='right' width='12px' height='12px' title='Change Graph' onclick='addGraphss(\""+id+"\",\""+tabId+"\")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img id='deleteWidget' alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget(\""+idLI+"\")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></td></tr></table><div id='chart"+index+"' style='width:100%;height:90%;background-color:white; border: 1px dotted rgb(169, 169, 169);'>\n\
    </div></li>", 4,3);
}

}
    function removeWidget(id){

//           alert("in removewidget"+id)
            gridsterL.remove_widget( $("#"+id) );

            }
    function saveLayouts(){


//                             alert("details    "+$("#LayoutDetails").val());
//                             alert("Your Layout Saved Successfully  ");
//$("#chartGridDetail").val(JSON.stringify(jsondata["graphs"][ch]));
var graphs = JSON.parse($("#chartGridDetail").val());
 var charts = Object.keys(graphs);
var gridsPos=gridsterL.serialize1();
//alert(JSON.stringify(gridsPos))
//alert(charts.length)
//var gridsPos=grid.serialize1();
 var chartId="";
for (var k = 0; k < charts.length; k++) {
//
chartId = "chart" + (parseInt(k) + 1);
//alert(JSON.stringify(graphs[chartId]["row"]))
//alert(JSON.stringify(gridsPos[k]["row"]))

graphs[chartId]["id"]=JSON.stringify(gridsPos[k]["id"]);
graphs[chartId]["row"]=JSON.stringify(gridsPos[k]["row"]);
graphs[chartId]["col"]=gridsPos[k]["col"];
graphs[chartId]["size_x"]=gridsPos[k]["size_x"];
graphs[chartId]["size_y"]=gridsPos[k]["size_y"];
}
$("#chartGridDetail").val(JSON.stringify(graphs));
// alert("details1111    "+$("#chartGridDetail").val());



                             $.ajax({
         async:false,
         type:"POST",
         data:
             $('#loginForm').serialize(),
         url:  'reportViewer.do?reportBy=saveLoginCharts',

      success:function(data) {
          alert("Your Charts Saved")
      }
            });


            }
             function changeChartLog(chName){

             }





             function changeChartLog1(chName){

//            alert("chart name   "+chName)
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
//$("#dialogTestByrepWall22").dialog({
//                        autoOpen: false,
//                        height: 500,
//                        width: 300,
//                        position: 'justify',
//                        modal: true
//                    });
//
//                    $("#dialogTestByrepWall22").dialog('open');


 $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
            'reportViewer.do?reportBy=getAvailableChartsForLogin&reportId=' + repId+"&reportName="+reportName+"&bizzRoleName="+bizzRoleName+"&fromLogin=edit&username="+username,
            function(data) {
//            alert("data222   "+data)
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
    <img id='plusPng' alt='' border='0px' align='right' width='10px' height='10px' title='Change Graph' onclick='changeChartLog("+chName+")' style='cursor:pointer;position:absolute;display:inline;' src='<%=request.getContextPath()%>/images/plus.png'/><img id='deleteWidget' alt='' border='0px' align='right' width='15px' height='15px' title='Change Graph' onclick='removeWidget("+chName+")' style='cursor:pointer;position:relative;display:inline;' src='<%=request.getContextPath()%>/images/deleteWidget.png'/></div>")

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
}
            });




            }
function chartTypeFunctionthis(chartId,chartType,chartName,viewBys,measures,graphData){
            $("#loading").hide();
            var html="";
//            alert(chartType)
             //            added by manik
         var chartNum=  chartId;//.replace ( /[^\d.]/g, '' );
       var chWidth=$("#"+chartNum).width();
       var chHeight=$("#"+chartNum).height();
//            var chartData = JSON.parse($("#chartData").val());
//    if(typeof(chartData[chartId]["Name"])!="undefined"){
//    html += "<span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartData[chartId]["Name"]+"</tspan></span>"
//    }
//    else{
       html += "<span id='dbChartName"+chartId+"'><tspan style=' font-size:15px;color:#333;font-family:Lucida Grande,Lucida Sans Unicode,Arial,Helvetica,sans-serif'>"+chartId+"</tspan></span>"
//    }
//             $("#renameTitle"+chartId).html(html);
             $("#renameTitle"+chartId).html(html);
              var data = [];
             for(var i=0;i<(graphData.length<10?graphData.length:10);i++){
                 data.push(graphData[i]);
             }

//              alert(JSON.stringify(chartData))
//              chartData[chartId]["chartType"]=chartType;
//              $("#chartData").val(JSON.stringify(chartData));
              $("#"+chartId).html("");
              var currData=[];
              var radius = 600/3.5;
                var records = 10;
                var groupRecords = 0;
                var count = 0;
//                 $("#div"+chartId).css("width","50%");
//              if (typeof chartData[chartId]["records"] !== "undefined" && chartData[chartId]["records"] !== "") {
//            records = chartData[chartId]["records"];
//        }
        var map={};
//         var measures = chartData[chartId]["meassures"];
//                var meassureIds = chartData[chartId]["meassureIds"];
//                var measIds = JSON.parse(parent.$("#measureIds").val());
//                var aggregations = JSON.parse($("#aggregation").val());
//                var measureValList = [];
//                var measureValListTotal = [];
//                if(typeof chartData[chartId]["chartType"] !=="undefined" && (chartData[chartId]["chartType"] == "Grouped-Bar" || chartData[chartId]["chartType"] == "GroupedStacked-Bar" )){
//              var innerRecords=4;
//              if(typeof chartData[chartId]["innerRecords"]!="undefined" && chartData[chartId]["innerRecords"]!=""){
//                 innerRecords = parseInt(chartData[chartId]["innerRecords"]-1);
//}
//                groupRecords = records*parseInt(innerRecords+1);
//                for(var m=0;m<(data[chartId].length);m++){
//                  if(count<groupRecords){
//                      if(typeof data[chartId][m]!="undefined" && typeof data[chartId][m][chartData[chartId]["viewBys"][0]]!="undefined" && typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined" && map[data[chartId][m][chartData[chartId]["viewBys"][0]]].length>innerRecords){
//                }else{
//                          var list=[];
//                          var keys = Object.keys(map);
//
//                          if(keys.length<parseInt(records) || (typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined" && map[data[chartId][m][chartData[chartId]["viewBys"][0]]].length<parseInt(innerRecords+1))){
//                          if(typeof data[chartId][m]!="undefined" && typeof data[chartId][m][chartData[chartId]["viewBys"][0]]!="undefined" && typeof map[data[chartId][m][chartData[chartId]["viewBys"][0]]]!="undefined"){
//                             list= map[data[chartId][m][chartData[chartId]["viewBys"][0]]];
//                             if(list.length<parseInt(innerRecords+1)){
//                             list.push(data[chartId][m][chartData[chartId]["viewBys"][0]]);
//                             count++;
//                         }
//                          }else{
//                            list.push(data[chartId][m][chartData[chartId]["viewBys"][0]]);
//                            count++;
//                          }
//
//                          map[data[chartId][m][chartData[chartId]["viewBys"][0]]]=list;
//                  currData.push(data[chartId][m]);
//                }
//            }
//
//              }
//                }
//                }else{
//                    var countVar = 0;
//              for(var h=0;h<(data[chartId].length < records ? data[chartId].length : records);h++){
//                  currData.push(data[chartId][h]);
//                  countVar++;
//                }
//
//                //for others
//                if(data[chartId].length>countVar){
//                    for(var k=countVar;k<data[chartId].length;k++){
//                    for(var i=0;i<measures.length;i++){
//                        if(k===countVar){
//                        measureValList.push(data[chartId][k][measures[i]]);
//            }
//                    else{
//                        var prevVal = measureValList[i];
//                        measureValList[i]=parseFloat(parseFloat(prevVal)+parseFloat(data[chartId][k][measures[i]]));
//                    }
//                    }
//                    if(k===parseInt(data[chartId].length-1)){
//                        var index = measIds.indexOf(meassureIds[i]);
//                        if(aggregations[index]==="AVG" || aggregations[index]==="avg"){
//                        measureValListTotal.push(measureValList[i]/parseInt(data[chartId].length));
//                    }else{
//                        measureValListTotal.push(measureValList[i]);
//                    }
//                    }
//                }
//            }
//            }

//            if(typeof chartData[chartId]["others"]!=="undefined" && chartData[chartId]["others"]==="Y"){
//                var otherMap = {};
//                otherMap[chartData[chartId]["viewBys"][0]]="Others";
//                for(var m=0;m<measureValList.length;m++){
//                    otherMap[measures[m]]=measureValList[m];
//                }
//                currData.push(otherMap);
//            }
currData=data;
              if(chartType=="Vertical-Bar" ){

               buildBar(chartId, data, viewBys,measures,chWidth,chHeight);
//               buildcoffeeWheel(chartId, currData, chartData[chartId]["viewBys"], chartData[chartId]["meassures"],"600",chHeight);
              }
              else if(chartType=="Horizontal-Bar"){
                buildHorizontalBar(chartId, data, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Network"){
                buildNetwork(chartId,chWidth,chHeight);
              }
              else if(chartType=="Pie"){
                buildPie(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Line"){
                buildLine(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="SmoothLine"){
                buildSmoothLine(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Bubble"){
                bubble(chartId, currData, viewBys,measures,chWidth,chHeight,records);
              }
              else if(chartType=="Double-Donut"){
                buildDoubleDonut(chartId, currData, viewBys,measures,chWidth,chHeight,radius*1.5);
              }
              else if(chartType=="Donut"){
                buildDonut(chartId, currData, viewBys,measures,chWidth,chHeight,radius+10);
              }
              else if(chartType=="Half-Donut"){
                buildHalfDonut(chartId, currData, viewBys,measures,chWidth,chHeight,radius*1.5);
              }
              else if(chartType=="Half-Pie"){
                buildHalfPie(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Donut-3D"){
                buildDonut3D(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Donut");
              }
              else if(chartType=="Pie-3D"){
                buildDonut3D(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Line"){
                buildMultiMeasureTrLine(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="scatter"){
                buildScatter(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="OverLaid-Bar-Line"){
                buildOverlaid(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Area"){
                buildMultiMeasureArea(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="Grouped-Bar"){
                 $("#div"+chartId).css("width","100%");
//                var w = document.getElementById("div"+chartId).style

                buildGroupedBar(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="StackedBar"){
                buildstackedBar(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="StackedBarH"){
                buildstackedBarH(chartId, currData, viewBys,measures,chWidth,chHeight,radius,"Pie");
              }
              else if(chartType=="MultiMeasure-Bar"){
                buildMultiAxisBar(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Word-Cloud"){
                buildWordCloud(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="Area"){
                buildArea(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="DualAxis-Bar"){
                buildDualAxisBar(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="GroupedStacked-Bar"){
                buildGroupedStacked(chartId, currData, viewBys,measures,chWidth,chHeight);
              }
              else if(chartType=="circularProgress"){ //added by manik
                  buildCircularProgress(chartId,currData, viewBys,measures);
              }
              else{
                   buildBar(chartId, currData, viewBys,measures,chWidth,chHeight);
              }

        }

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
                         var busroleId='';
                         busrolename ='';
                         function busroelIdTestwal(type){
                busroleId = $("#busroleIdwal").val();
               busrolename = $("#busroleIdwal option:selected").text();

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
                                var chname="chart"+ch
                                reporthtml+="<li class='closed' id='" + repName + i + "'>";
                                reporthtml+="<img src='icons pinvoke/report.png'></img>";
                                reporthtml+="<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraphwal('" + repId + "')\">" + repName + "</span>";
                                reporthtml+="<ul id='repName-" + repId + "' style='display:none'>";
var h=i
var isflag='true';
                                for(var i1=h;i1<jsonVar.ReportIds.length;i1++){
                                    chname="chart"+ch
                                    if(isflag=='true'){
                                         repId = jsonVar.ReportIds[i1];
//                                    repId = jsonVar.ReportIds[h];
                                graphId = jsonVar.ReportIds[i1];
                                 graphName = jsonVar.GraphNames[i1];

                                reporthtml+="<li class='closed' id='" + graphId + "'>";
                                reporthtml+="<img src='icons pinvoke/chart.png'></img>";
                                if(graphName==null ||graphName==""||graphName=="null"){
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraphwal(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + repName + "</a></span>";
                                }else{
                                reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraphwal(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + graphName + "</a></span>";
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
                           reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraphwal(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + repName + "</a></span>";
                        }else{
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraphwal(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + graphName + "</a></span>";
                                    reporthtml+="</li>";
                                     }ch++
                                     }i++;
                                }else{
                                    break;
                                }
//                                }
                                isflag='false';
                                }
                                reporthtml+="<li class='closed' id='" + repId + "'>";
                                reporthtml+="<img src='icons pinvoke/table.png'></img>";
                                reporthtml+="<span id='reportName-" + repId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdTable(" + repId + ","+'"' + repName + '"'+")' >" + repName + "</a></span>";
                                reporthtml+="</li>";

                                    reporthtml+="</ul>";
                                    reporthtml+="</li>";

                        }


                        reporthtml+="</ul>";
                        reporthtml+="</li>";
                        reporthtml+="</ul>";



                        $("#repDialogTestwal").html(reporthtml);
                         $('table#repDialogTestwal li').quicksearch({
                    position: 'before',
                    attached: 'table#repDialogTestwal',
                    loaderText: '',
                    delay: 100
                });

                        //                            $("#repDialogTest").html("<ul>"+jsonVar+"</ul>");

                        $("#dialogTestByrepwal").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                        $("#dialogTestByrepwal").dialog('open');
                    }
                });

            }
            function dragGraphwal(repid,repName){
                if(document.getElementById("repName-"+repid).style.display=='none' )
                    $("#repName-"+repid).show();
                else
                    $("#repName-"+repid).hide();
            }
            var grapId='';
            var repNameVal='';
//            buildDbrdGraphwal(339,339,"Store Wise Head Analysis",0,"chart1","chart1")
            function buildDbrdGraphwal(reportId,graphId,name2,grpNo,chartname,ch){
//                alert(busrolename);
//                alert(reportId);
//                alert(name2);
//                alert(chartname);
//                alert(ch);
                var tabId=$("#currentTab").val();
                addGraphss1(busrolename,reportId,name2,graphRegionId,tabId,ch,chartname)
            }
            function buildDbrdGraphwal1(reportId,graphId,name2,grpNo,chartname,ch){

$("#graphsId").val(reportId);
//                document.getElementById("regionId"+colNumber).style.display='none'
                grapId=reportId;
                repNameVal=name2;
                var namevalue = name2.replace("_"," ","gi")
                $("#graphName").val(namevalue);
                $("#chartname").val(ch);
                $("#busrolename").val(busrolename);
                var div=document.getElementById(dashletId);
                 $("#regionId"+colNumber).show();
                 if(document.getElementById("readdDivId"+colNumber).style.display=='block')
                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: encodeURI('<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=getReportGraphs&graphId='+graphId+'&reportId='+reportId+'&divId='+colNumber+"&reportBy=viewReport&action=open&width="+width+"&height="+height+"&name="+namevalue+'&oneViewIdValue='+oneViewIdValue+"&busroleId="+busroleId+"&grpNo="+grpNo+"&chartname1="+chartname+"&chartname="+ch+"&busrolename="+busrolename+"&oneviewTime="+oneviewTimecheck+"&repGraph=repGraph"),

                    success: function(data) {
                        if(data != ""){
                             $("#"+dashletId).html("");
                            document.getElementById(dashletId).innerHTML ="";

//                            $("#"+dashletId).html(data);
                              $("#"+colNumber).html(data);
                            $("#Dashlets"+colNumber).html(chartname);

                             generateJsonDataOneview(dashletId,oneViewIdValue,colNumber,ch,"add");
                        }else{
                             $("#"+dashletId).html("");
                             $("#Dashlets"+colNumber).html(chartname);
                            generateJsonDataOneview(dashletId,oneViewIdValue,colNumber,ch,"add");
                        }

                    }
                });
                $("#dialogTestByrepwal").dialog('close');

            }
</script>
    <form name="ty">
    </form>
<script type="text/javascript">
var tabsequence ='';
var fromwall="<%=fromWall%>";
var resize=true;
var graphRegionId='';
if(fromwall=="login" || fromwall=="null"){
    resize=false;
}else{
     $('#loginButton').css("display", "none");
}
var toglelog="false";
function openLogindiv(){
//    alert("lll");
if(toglelog=="true"){
//    $('#loginBox').css("display", "none");
    $('#loginBox').css("display", "block");
    toglelog="false";
}else{
//    $('#loginBox').css("display", "block");
     $('#loginBox').css("display", "none");
    toglelog="true";
}
//   $('#loginBox').css("display", "block")
//    $("#loginBox").toggle();
//    var button = $('#loginButton');
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
}
    $('#loginBox').css("display", "block")
    $("#loginBox").toggle();
    var button = $('#loginButton');
    var box = $('#loginBox');
    var form = $('#loginForm');
    button.removeAttr('href');
    button.mouseup(function(login) {
        box.toggle();
        button.toggleClass('active');
    });
    form.mouseup(function() {
        return false;
    });
    $(this).mouseup(function(login) {
        if(!($(login.target).parent('#loginButton').length > 0)) {
            button.removeClass('active');
            box.hide();
        }
    });
function formGrid(id){
 var gridsterL="gridsterL"+id;
 gridsterL = $(".gridster > ul").gridster({
        widget_margins: [2, 2],
        autogenerate_stylesheet: false,
        widget_base_dimensions: [widt,widt],
        min_cols: 10,
        resize: {
            enabled: true,
//            enabled: false,
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
       }

        }
    }).data('gridster');
  gridsterL.generate_stylesheet({rows: 30, cols: 22});
}
$(function() {

if(fromwall=="login" || fromwall=="null"){
    $("#customLoginPage").hide();
}else{

    $("#customLoginPage").show();
}

// var button = $('#loginButton');
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





//for(var i=0;i<5;i++){
//var id = "tabs-" + i;
//   adddynamicTab(id,"label"+i)
//
//
//}

 username='progen';
 $.post(
//            'reportViewer.do?reportBy=getAvailableCharts&reportId=' + repId+"&reportName="+parent.$("#graphName").val(),
            'reportViewer.do?reportBy=getChartsForWall&username='+username,
            function(jsonData1) {
//                alert(JSON.stringify(JSON.parse(jsonData1)))
//                var data = JSON.parse(jsonData1)["data"];
                var data = JSON.parse(JSON.parse(jsonData1)["data"]);
//                var tagData = JSON.parse(jsonData1)["tagData"];
                var tagData = JSON.parse(JSON.parse(jsonData1)["tagData"]);
                $("#tabdetail").val(JSON.stringify(tagData));
                if(data=="false"){
            }else{

//                alert("dataaa"+JSON.stringify(data))
                for(var tab in tagData){
                     adddynamicTab(tab,tagData[tab])
                }

//               var jsondata = data[tagData[0]];

              var tab1='';
              for(var tab in tagData){
                    tab1=tab;
                    break;
                }
                $("#currentTab").val(tab1)
                var jsondata = data[tab1];
//               alert("gra"+JSON.stringify(tagData[Object.keys(tagData)[0]]))
//               alert("gra"+JSON.stringify(tagData))
//               alert("tab1  "+tab1)
$("#graphsList").val("");
                for(var chno in jsondata["graphs"]){
                    addgraphsR();
                }
//                 var graphsList=Object.keys(jsondata["graphs"])
//               $("#graphsList").val(JSON.stringify(graphsList))
               for(var ch in jsondata["graphs"]){
//                   alert(JSON.stringify(jsondata["graphs"][ch]["reportMeta"]))
//                   alert("cjhart  "+JSON.stringify(jsondata["graphs"][ch]["row"]))
                $("#chartGridDetail").val(JSON.stringify(jsondata["graphs"]));
                   $("#chartData").val(JSON.stringify(jsondata["graphs"][ch]["reportMeta"]["chartData"]));
//                   alert(jsondata["graphs"][ch]["reportMeta"]["chartType"]);
                   try{
                   chartTypeFunctionthis(ch,jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["chartType"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["chartName"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["viewBys"],jsondata["graphs"][ch]["reportMeta"]["chartData"]["chart1"]["meassures"],jsondata["graphs"][ch]["data"])
                   }catch(e){}
               }
//alert("jsondata "+JSON.stringify(JSON.parse(JSON.parse(data)["data"])["tab"]))
//var tabdetail=JSON.parse(JSON.parse(data)["data"])["tabdetail"];
//var tab=JSON.parse(data)["data"];
//alert(JSON.stringify(tab["tabData"]))
//alert(JSON.stringify(tabdetail["tabData"]))
//$("#tabdetail").val(JSON.stringify(tabdetail));
//$("#tabData").val(JSON.stringify(tabdetail["tabData"]));
//$("#graphdetail").val(JSON.stringify(tabdetail["graphs"]));
//alert("jsondata "+JSON.stringify(JSON.parse(JSON.parse(data)["data"])["tabdetail"]))
//alert("chartData "+JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]))
//            var chartData=JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]);
//            var chartData=JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]);
//                $("#chartData").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
//                var meta = JSON.parse(JSON.parse(data)["meta"]);
//                $("#viewby").val(JSON.stringify(meta["viewbys"]));
//                $("#viewbyIds").val(JSON.stringify(meta["viewbyIds"]));
//                $("#measure").val(JSON.stringify(meta["measures"]));
//                $("#measureIds").val(JSON.stringify(meta["measureIds"]));
//                $("#aggregation").val(JSON.stringify(meta["aggregations"]));
//                $("#drilltype").val((meta["drillType"]));
//                $("#filters1").val(JSON.stringify(meta["filterMap"]));
//     $("#LayoutDetails").val(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"]));
//     alert(JSON.stringify(JSON.stringify(JSON.parse(JSON.parse(data)["meta"])["chartData"])))
//$("#tabdetail").val(
//alert("tabd    "+$("#tabdetail").val());
//alert("graphs    "+$("#graphdetail").val());
//     alert(JSON.stringify(JSON.stringify(JSON.parse(JSON.parse(data)["data"])["chart1"])))
            }});








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
            enabled: resize,
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
                var chartId=$($widget).attr('id').replace("chart_LI","chart" );
                  localRefresh(chartId);
       }

        }
    }).data('gridster');
  gridsterL.generate_stylesheet({rows: 30, cols: 22});
    if(fromwall=="login"){
    gridsterL.disable();
    }


 var tabs = $( "#tabs" ).tabs();
tabs.find( ".ui-tabs-nav" ).sortable({
axis: "x",
stop: function() {
//tabs.tabs( "refresh" )
// var tabsequence1 = $(this).sortable('serialize');
var sorted = $("#tabs").sortable( "serialize" );
//alert(JSON.stringify(tabsequence))
//alert("sorted  "+JSON.stringify(sorted))
},
update: function (e, ui) {
              var csv = "";
             $("#tabs > ul > li > a").each(function(i){
                  csv+= ( csv == "" ? "" : "," )+this.id;
             });
//             alert(csv);
        }
});

var tabs1 = $( "#tabs" ).tabs();
tabs1.find( ".ui-tabs-nav li" ).click(function(e)
    {
//     alert("ppp");
     alert($(this).find("a").text());
//     alert("innnn");
    });

var tabTitle = $( "#tab_title" ),
tabContent = $( "#tab_content" ),
 tabTemplate = "<li><a href='xyz'>label</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>",
tabCounter = 4;
var tabs = $( "#tabs" ).tabs();
// modal dialog init: custom buttons and a "close" callback resetting the form inside
var tabdetail = $( "#tabdetail" ).dialog({
autoOpen: false,
modal: true,
buttons: {
Add: function() {
addTab();
$( this ).dialog( "close" );
},
Cancel: function() {
$( this ).dialog( "close" );
}
},
close: function() {
form[ 0 ].reset();
}
});
// addTab form: calls addTab function on submit and closes the dialog
var form = tabdetail.find( "form" ).submit(function( event ) {
addTab();
tabdetail.dialog( "close" );
event.preventDefault();
});
// actual addTab function: adds new tab using the input from the form above
function addTab() {
var label = tabTitle.val() || "Tab " + tabCounter,
id = "tabs-" + tabCounter,
//li = $( tabTemplate.replace( /#\xyz/g, "#" + id ).replace( /#\label/g, label ) ),
tabContentHtml = tabContent.val() || "Tab " + tabCounter + " content.";
//tabs.find( ".ui-tabs-nav" ).append( li );
tabs.find( ".ui-tabs-nav" ).append( "<li><a href='#"+id+"'>"+label+"</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>" );
tabs.append( "<div id='" + id + "'><div id='gridsterDivLogin"+id+"' class='gridster' style='float: left'><ul id='gridUL_Login' type='none' style='width: 100%'></ul></div></div>" );
tabs.tabs( "refresh" );
tabCounter++;
//formGrid(id);
var tabdetail = JSON.parse($("#tabdetail").val());
var tabno=Object.keys(tabdetail);
//alert("tabids"+(parseInt(tabno.length)+parseInt(1)))
var tabidd="tab-"+((parseInt(tabno.length)+parseInt(1)));
tabdetail[tabidd]=label;
//tabdetail["tabData"][id]=label;
$("#tabdetail").val(JSON.stringify(tabdetail))
//var tabData=tabdeta["tabData"];
//tabData[id]=label;
//var tabidss = JSON.parse($("#tabidsNname").val());
//var tabdetail = JSON.parse($("#tabdetail").val());
//tabdetail["tabData"][id]["tabname"]=label;
//$("#tabData").val(JSON.stringify(tabdetail))

//alert("atbbbb"+$("#tabdetail").val())
//alert("tabids"+$("#tabdetail").val())
}


        

//for(var i=0;i<10;i++){
//
//   adddynamicTab("id"+i,"label"+i)
//
//
//}
function adddynamicTab(id,label){

var tabs = $( "#tabs" ).tabs();
//    var label = tabTitle.val() || "Tab " + tabCounter,
//id = "tabs-" + tabCounter,
//li = $( tabTemplate.replace( /#\xyz/g, "#" + id ).replace( /#\label/g, label ) ),
//tabContentHtml = tabContent.val() || "Tab " + tabCounter + " content.";
//tabs.find( ".ui-tabs-nav" ).append( li );
tabs.find( ".ui-tabs-nav" ).append( "<li><a href='#"+id+"'>"+label+"</a> <span class='ui-icon ui-icon-close' role='presentation'>Remove Tab</span></li>" );
tabs.append( "<div id='" + id + "'><div id='gridsterDivLogin"+id+"' class='gridster' style='float: left'><ul id='gridUL_Login' type='none' style='width: 100%'></ul></div></div>" );
tabs.tabs( "refresh" );
tabCounter++;
}
//var hgt=$("#tabs").height();
var hgt=30;
if(fromwall=="login" || fromwall=="null"){
tabs.append("<div id='loginButton' title='Login' style='margin-top:-"+hgt+"px;cursor:pointer;background-color:#3E454D;width:5%;height:"+hgt+"px;float:right;border-radius:8px;'><table><tr><td><a onclick='openLogindiv()' style='color: white;padding:5px;'>Login</a></td></tr></table></div>")
}else{
    tabs.append("<div id='loginButton' title='Login' style='display:none;margin-top:-"+hgt+"px;cursor:pointer;background-color:#3E454D;width:5%;height:"+hgt+"px;float:right;border-radius:8px;'><table><tr><td><a onclick='openLogindiv()' style='color: white;padding:5px;'>Login</a></td></tr></table></div>")
}
//tabs.append("<div id='loginButton' onclick='openLogindiv()' title='Login' style='margin-top:-"+hgt+"px;cursor:pointer;background-color:#3E454D;width:5%;height:"+hgt+"px;float:right;border-radius:8px;'><table><tr><td><a onclick='openLogindiv()' style='color: white;padding:5px;'>Login</a></td></tr></table></div>")

// addTab button: just opens the dialog
$( "#add_tab" )
.button()
.click(function() {
tabdetail.dialog( "open" );
});
$( "#add_graphRegion" ).button();
$( "#saveLay" ).button();
// close icon: removing the tab on click
tabs.delegate( "span.ui-icon-close", "click", function() {
var panelId = $( this ).closest( "li" ).remove().attr( "aria-controls" );
$( "#" + panelId ).remove();
tabs.tabs( "refresh" );
});
tabs.bind( "keyup", function( event ) {
if ( event.altKey && event.keyCode === $.ui.keyCode.BACKSPACE ) {
var panelId = tabs.find( ".ui-tabs-active" ).remove().attr( "aria-controls" );
$( "#" + panelId ).remove();
tabs.tabs( "refresh" );
}
});
});
function logout(){
                  document.forms.myForm1.action="<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                  document.forms.myForm1.submit();
                }
</script>
</body>
</html>



