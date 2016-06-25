<%-- 
    Document   : landingPage
    Created on : 7 Oct, 2015, 11:26:35 AM
    Author     : Faiz Ansari
--%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@ page import="utils.db.*"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page contentType="text/html"%>
<%@page import="java.sql.*"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="prg.db.Session" %>
<%@page import="java.util.*"%>
<%@page import="prg.db.PbDb" %>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO" %>
<!--Header Page-->
<%@page import="java.net.InetAddress"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="prg.db.PbDb"%>
<%@page import="prg.db.Container"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.Connection"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="java.util.HashMap"%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<script src="JS/jquery-1.11.2.min.js" type="text/javascript"></script>
	<script src="JS/jquery-ui-1.11.4.js" type="text/javascript"></script>	
	<script src="JS/jquery.nicescroll.js" type="text/javascript"></script>
        <script src="JS/waves.min.js" type="text/javascript"></script>
        
	<link rel="stylesheet" href="css/metro-bootstrap.css" type="text/css"/>
        <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
        <link rel="stylesheet" href="css/waves.min.css" type="text/css"/>
	<link rel="stylesheet" href="css/mainlayout.css" type="text/css"/>	
	
	<title>pi</title>

	<script type="text/javascript">
	$(document).ready(function(){
            $("html").niceScroll({cursorcolor:"#000",cursoropacitymax:0.5,cursorwidth:5,mousescrollstep:6,enablekeyboard:true});
            <%
                //int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
                String userid =(String)(session.getAttribute("USERID"));
                String userType = "";
                boolean isXtendUser = false;
                boolean isQDEnableforUser=false;
                boolean isPowerAnalyserEnableforUser=false;
                String homeVar = "";
                if(userid==null){
                    response.sendRedirect(request.getContextPath()+"/new_stdLogin.jsp");
                    
                    
 
                }
                else{
                ServletContext context = getServletContext();
//                String userType = null;
//                boolean isXtendUser = false;
//                boolean isQDEnableforUser=false;
//                boolean isPowerAnalyserEnableforUser=false;
                HashMap<String,UserStatusHelper> statushelper;
                UserStatusHelper helper=new UserStatusHelper();
                if(context.getAttribute("helperclass")!=null){
                    statushelper=(HashMap)context.getAttribute("helperclass");
                    //out.println(statushelper);
                    if(!statushelper.isEmpty()){
                       helper=statushelper.get(request.getSession(false).getId());
                       if(helper!=null){

                       isQDEnableforUser=helper.getQueryStudio();
                       isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
                       userType=helper.getUserType();
                       isXtendUser = helper.getXtendUser();
                       }
                    }
                }

                //Header Page
                String LOGINID = String.valueOf(session.getAttribute("LOGINID"));
                boolean isCompanyValid = false;
                String themeColor = "blue";
                if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }

                userid = String.valueOf(session.getAttribute("USERID"));
                UserLayerDAO userdao = new UserLayerDAO();
                int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
                userType = userdao.getUserTypeForFeatures(USERID);

                PbDb pbdb = new PbDb();
                String userId = "";
                userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
                String userstart = "select start_page from prg_ar_users where pu_id=" + userId;
                PbReturnObject userstartpbro = pbdb.execSelectSQL(userstart);
                userstartpbro.writeString();
//                String homeVar = "";
                String strpage = userstartpbro.getFieldValueString(0, 0);
                if (strpage == null || strpage.equalsIgnoreCase("")) {
                    homeVar = "home.jsp";
                } else if (strpage != null && strpage.equalsIgnoreCase("newHome.jsp")) {
                    homeVar = "newHome.jsp";
                } else {
                    homeVar = "home.jsp";
                }
            //Xtend
                    //boolean isQDEnableforUser = false;
                    //boolean isXtendUser = false;
                    //boolean isPowerAnalyserEnableforUser = false;
                    //ServletContext context = getServletContext();
                    //HashMap<String, UserStatusHelper> statushelper;
//                    if (context.getAttribute("helperclass") != null) {
//                        statushelper = (HashMap) context.getAttribute("helperclass");
//                        UserStatusHelper helper = new UserStatusHelper();
//                        if (!statushelper.isEmpty()) {
//                            helper = statushelper.get(request.getSession(false).getId());
//                            if (helper != null) {
//                                isQDEnableforUser = helper.getQueryStudio();
//                                isPowerAnalyserEnableforUser = helper.getPowerAnalyser();
//                                isXtendUser = helper.getXtendUser();
//                            }
//                        }
//                    }
                    ProgenParam connectionparam = new ProgenParam();
                    String reportId1 = (String) request.getAttribute("REPORTID");
                    Container container = Container.getContainerFromSession(request, reportId1);
                    PbReportCollection collect = new PbReportCollection();
                    if (container != null) {
                        collect = container.getReportCollect();
                    }
                    PbReportViewerDAO dao = new PbReportViewerDAO();
                    String lastupdateedate = "";
                    Connection con = null;
                    ArrayList<String> qryelements = collect.reportQryElementIds;
                    String elementId = "";
                    if (qryelements != null && !qryelements.isEmpty()) {
                        elementId = qryelements.get(0);
                        con = connectionparam.getConnection(elementId);
                        lastupdateedate = dao.getLastUpdatedDate(con, elementId);
                    }

                    //added by Nazneen for logo based on company

                    String companyId = "";
                    String compLogo = "";
                    String bussLogo = "";
                    String compTitle = "";
                    String bussTitle = "";
                    String rightWebSiteUrl = "";
                    String leftWebSiteUrl = "";
                    if (session.getAttribute("isCompLogo") != null) {
                        String isCompLogo = session.getAttribute("isCompLogo").toString();
                        if (isCompLogo.equalsIgnoreCase("YES")) {
                            companyId = session.getAttribute("companyId").toString();
                            if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                                compLogo = session.getAttribute("compLogo").toString();
                                bussLogo = session.getAttribute("bussLogo").toString();
                                compTitle = session.getAttribute("compTitle").toString();
                                bussTitle = session.getAttribute("bussTitle").toString();
                                rightWebSiteUrl = session.getAttribute("rightWebSiteUrl").toString();
                                leftWebSiteUrl = session.getAttribute("leftWebSiteUrl").toString();
                            }
                        }
                    }
                    //end of code by Nazneen for logo based on company
                }
            %>
            
            
            $("#mainMenu").css("height",$(window).height()-50);
            $("#mainMenu").css("max-height","485px");
            $("#usernameDsp").slideDown(1500);
            var dataArr = [];
            var idArr = [];
            var repId = [];
            $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:'reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                    success: function(data){                      
                        var jData = JSON.parse(data);
                        //alert(JSON.stringify(jData));
                        var keys = Object.keys(jData[0]);
                        //alert(keys);
                        dataArr = [];
                        for(var i in jData){
                         dataArr.push(jData[i][keys[0]]);
                         idArr.push(jData[i][keys[3]]);
                        }
                        //alert(dataArr);
                        //alert(idArr);
                    },
                    error: function(){
                        alert("Error!!!");
                    }
                });
            
             //alert(dataArr);
             var tagBox="";
             var boxFlg=1;
             var chType="file-text-o";
             for(var i in dataArr){
//                 if(i==0){chType="line-chart";}
//                 if(i==1){chType="plane"}
//                 if(i==2){chType="money"}
//                 if(i==3){chType="heartbeat"}
//                 if(i==4){chType="phone"}
//                 if(i==5){chType="area-chart";}
//                 if(i==6){chType="credit-card"}                 
//                 if(i==7){chType="medkit"}
//                 if(i==8){chType="h-square"}
//                 if(i==9){chType="bar-chart"}    
//                 if(i==10){chType="pie-chart"}
//                 if(i==11){chType="pie-chart"}
//                 if(i==12){chType="pie-chart"}
//                 if(i==13){chType="pie-chart"}
//                 if(i==14){chType="pie-chart"}
//                 if(i==15){chType="pie-chart"}
                 
                 if(boxFlg<=2){
                     tagBox+="<div class='box box-1x tile-"+i+"' >"+
                            "<div class='box-img img-1x'><i class='fa fa-"+chType+" fa-5x'></i></div>"+
                            "<div class='box-in'>"+
                            "<label class='box-label' title=''>"+dataArr[i]+"</label><hr class='hr'>"+
                            "<div id='"+idArr[i]+"' class='reportList' style='height:75%;overflow:auto'>"+
				
				"</div>"+
						
                            "</div>"+
                        "</div>";
                boxFlg=boxFlg+1;
                 }
                 else{
                    tagBox+="<div class='box box-2x tile-"+i+"' >"+
                            "<div class='box-img img-2x'><i class='fa fa-"+chType+" fa-5x'></i></div>"+
                            "<div class='box-in' >"+
                            "<label class='box-label' title=''>"+dataArr[i]+"</label><hr class='hr'>"+
                            "<div id='"+idArr[i]+"' class='reportList' style='height:75%;overflow:auto'>"+
				
				"</div>"+
			
                            "</div>"+
                        "</div>";
                boxFlg=1;
                 }                
                 
             }
            $("#maincontent").append(tagBox);
            

            getTagRep(idArr); 
            Waves.attach('.wave');
            Waves.init();
            $(".box").fadeIn("slow");
            //$(".reportList").niceScroll({touchbehavior:true,cursorcolor:"#000",cursoropacitymax:0.6,cursorwidth:5,mousescrollstep:7});
            //$(".reportList").niceScroll({touchbehavior:true,cursorcolor:"transparent",cursoropacitymax:0,cursorwidth:5,mousescrollstep:6,enablekeyboard:true});
            $("#rightMenu").height($(window).height());
            $("body").on("mouseenter",".box",function(){
                $(this).children().next().animate({
                    height:'+=145px'
                },"slow");
                $(this).children(".box-img").hide();
                $(".reportList").niceScroll({cursorcolor:"#000",cursoropacitymax:0.5,cursorwidth:5,mousescrollstep:6,enablekeyboard:true});
            });
            $("body").on("mouseleave",".box",function(){
                $(this).children().next().animate({
                        height:'-=145px'
                },"slow",function(){$(this).prev(".box-img").fadeIn();});
            });
//            $(".box").mouseenter(function(){
//                /* $(this).children().next().slideToggle("fast"); */
//                $(this).children().next().animate({
//                    height:'+=145px'
//                },"slow");
//                $(this).children(".box-img").hide();
//            });		
//            $(".box").mouseleave(function(){
//                /* $(this).children().next().slideToggle("fast"); */
//                $(this).children().next().animate({
//                        height:'-=145px'
//                },"slow",function(){$(this).prev(".box-img").fadeIn();});
//                        
//            });
            $("#menuBtn").click(function(){
                $("#mainMenu").hide();
                $("#backDiv").show();
                $("#rightMenu").show("slide", { direction: "right" }, 500);
            });
                
            $("#homeBtn").click(function(){
                $("#rightMenu").hide();
                $("#mainMenu").slideToggle("slow");
            });
            $("#backDiv").click(function(){
                $("#backDiv").hide();
                $("#rightMenu").hide("slide", { direction: "right" }, 500);
            });
            $(".wave").click(function(){
                //$("#backDiv").hide();
                //$("#rightMenu").hide();
                $("#mainMenu").hide();
            });
            $("#mainMenu").children().mouseenter(function(){
                $(this).prepend("<i class='fa fa-chevron-right'></i>");
                $(this).css("background-color","red");
            });
            $("#mainMenu").children().mouseleave(function(){
                $(this).children(".fa").remove();
            });

//            $("#mainMenu").mouseleave(function(){
//                $("#mainMenu").slideUp("slow");
//            });
            $("#maincontent").click(function(){
                if($("#mainMenu").is(":visible")){
                            $("#mainMenu").slideUp("slow");
                        }
            });
            
            $("#rightMenu span").click(function() {
                //Slide up all the link lists
                 //alert("hello");
                $("#rightMenu ul").slideUp();
                //Slide down the link list below the h3 clicked - only if it's closed
                if(!$(this).next().is(":visible")) {
                    $(this).next().slideDown();
                }
            });
            
            
            $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                function(data) {

                    var data1 = JSON.parse(data);
                    var tags = "";
                    //alert(JSON.stringify(data));
                    var len = data1.length;
                    //alert(data1.length);
                    for (var i = 0; i < data1.length; i++) {
                        tags += "<li class='parent'><a href='#' style='' onclick='slide_ul(tagReport"+ i +")'>" + data1[i]["Region"] + "</a><ul id='tagReport" + i + "' style='display: none;'></ul></li>";
                        //                 tags +="<a href='#'>"+data1[i]["Region"]+"</a>";
                    }

                    $("#reports_nav").append(tags);
                    //alert($("#reports_nav").width());
                    for (var j = 0; j < len; j++) {
                        displayReportsTags(data1[j]["Id"], len, j);
                    }
                });
                function displayReportsTags(tagId, len, k) {
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        // timeout: 30000,
                        url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getTagsBlocks&userId=' +<%=userid%> + '&tagId=' + tagId,
                        success: function(data) {
                            var data2 = JSON.parse(data);
                            //alert("..."+JSON.stringify(data2["reportId"].length));
                            var len = data2["reportId"].length;
                         
                            var div = '';

                            for (var i = 0; i < len; i++) {
                                //                       alert(data2["reportId"][i]);
                                if(data2["tagType"][i]=="R"){
                                    div += "<li><a href='#' onclick='reportsPath(" + data2["reportId"][i] + ")'>" + data2["tagShortDesc"][i] + "</a></li>";
                                }else if(data2["tagType"][i]=='D'){
                                    div += "<li><a href='#'onclick='openDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";
                            
                                }
                                else if(data2["tagType"][i]=='T'){
                                    div += "<li><a href='#'onclick='openTimeDashboard(\""+data2["tagShortDesc"][i]+"\","+data2["reportId"][i]+")'>" + data2["tagShortDesc"][i] + "</a></li>";

                                }else if(data2["tagType"][i]=='O'){
                                    //div += "<li><a href='#' onclick='oneViewPath('" + data2["reportId"][i] +"','"+ data2["tagShortDesc"][i] + "')'>" + data2["tagShortDesc"][i] + "</a></li>";
                                    div += "<li><a href='#' onclick='openOneView("+data2["reportId"][i]+")'>" + data2['tagShortDesc'][i] + "</a></li>";
                                }
                        
                            }
                            //                   alert(div);
                            //                   alert($("#tagReport"+k).attr("id"));
                            $("#tagReport" + k).append(div);


                        }
                    });
                }

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    //                            url: "<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllfavReports",
                    url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports',
                    success: function(data){
                        var json=eval('('+data+')');
                        //                                alert(json[0].reportId);
                        //                                var data1 = JSON.parse(data);
                        //                                var keys = Object.keys(data1);
                        var favReports = "";
                        var favReports2 = "";
                        for (var i = 0; i < json.length; i++)
                        {
                            //alert(keys[i])
                            // alert("..."+JSON.stringify(data1[keys[i]]));
                            favReports += "<li><a href='#' onclick='reportsPath(" + json[i].reportId + ")' style='display: block;'>" + json[i].reportName + "</a></li>";
                            favReports2 += "<a class='box-link'href='#' onclick='reportsPath(" + json[i].reportId + ")' style='display: block;'>" + json[i].reportName + "</a>";
                        }
                        ////alert(favReports);
                        // addFavoriteReports(favReports);
                        $("#fav").append(favReports);
                        
                        var tagBox ="<div class='box box-2x' style='display:block;background-color:#ab47bc' >"+
                            "<div class='box-img img-2x'><i class='fa fa-file-text-o fa-5x'></i></div>"+
                            "<div class='box-in' style='height:35px;'>"+
                            "<label class='box-label' title=''>Favorite Reports</label><hr class='hr'>"+
                            "<div class='reportList' style='height:75%;overflow:auto'>"+				
                            ""+favReports2+"</div></div></div>";
                        
                        tagBox +="<div class='box box-2x' style='display:block;background-color:#00c853' >"+
                            "<div class='box-img img-2x'><i class='fa fa-file-text-o fa-5x'></i></div>"+
                            "<div class='box-in' style='height:35px;'>"+
                            "<label class='box-label' title=''><i class='fa fa-bell-o'></i> &nbsp Alert's </label><span style='margin-left:200px;font-size:18px;border-radius:100px;background-color:#b71c1c;float:right;margin-top:-30px;margin-right:10px ' class='badge'>99</span><hr class='hr'>"+
                            "<div class='reportList' style='height:75%;overflow:auto'>"+				
                            ""+favReports2+"</div></div></div>";
                
                
                    $("#maincontent").append(tagBox);

                    }
                });

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllDB",
                    success:function(data)
                    {
                        //  alert(data);
                        //alert(($('#nav').width()))
                        var data1 = JSON.parse(data);
                        var keys = Object.keys(data1);
                        var dashBoards = "";
                        for (var i = 0; i < keys.length; i++)
                        {
                            //alert(keys[i])
                            //alert("..."+JSON.stringify(data1[keys[i]]));
//                            dashBoards += "<li onclick=dashBoardPath('" + keys[i] + "','"+ data1[keys[i]].split("::")[1] + "')><a href='#' >" + data1[keys[i]].split("::")[0] + "</a></li>";
                            dashBoards += "<li onclick='dashBoardPath(\"" + keys[i] + "\",\""+ data1[keys[i]] + "\")'><a href='#' >" + data1[keys[i]].split("::")[0] + "</a></li>";
                        }""

                        $("#dash").append(dashBoards);
                        // alert($("#dash").width());
                    }
                });

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:"<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=GetAllOneView",
                    success:function(data)
                    {    //alert(data);
                        var data1 = JSON.parse(data);
                        var keys = Object.keys(data1);
                        var one_view = "";
                        for (var i = 0; i < keys.length; i++)
                        {
                            //alert(keys[i])
                            //alert("..."+JSON.stringify(data1[keys[i]]));
                            one_view += "<li><a href='#' onclick=oneViewPath('" + keys[i] +"','"+ data1[keys[i]] + "')>" + data1[keys[i]] + "</a></li>";
                        }
                        $("#oneview").append(one_view);
                    }
                });
	});

function slide_ul(tag_ul){
                //                alert($( '#tag_ul' ).is(":visible"))
                //                $('.parent').slideToggle();
                $(tag_ul).slideToggle( 'fast' );       
                $(this).css("color","red");
            }
            function reportsPath(repId) {
                // alert("hhhhhhhhh")

                //   openReportWithDefaultFilters()
                window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID=' + repId + '&action=open', '_blank');

            }
            
	function openPage(obj){
		window.open(obj,"_self");
	}
        
       
        function getTagRep(idArr){
            
            for(var i in idArr){
            
            var tagId=idArr[i];
            var tagShortDesc;
            var tagLongDesc;
            var reportId;
            var tagType;
            var tagAssignId;
                            
            $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url:'reportViewer.do?reportBy=getTagsBlocks&userId='+<%=userid%>+'&tagId='+tagId,
                        success:function(data) {
                            //alert(JSON.stringify(data)); 
                            var jsonVar=eval('('+data+')');
                            tagShortDesc=jsonVar.tagShortDesc;
                            tagLongDesc=jsonVar.tagLongDesc;
                            reportId=jsonVar.reportId;
                            tagType=jsonVar.tagType;
                            tagAssignId=jsonVar.tagAssignId;
                            //alert(tagShortDesc);
                            //alert(tagLongDesc);
                            //alert(tagAssignId);
                            //alert(reportId);
                           
             }
            });
            for(var i in tagShortDesc){
                //alert("hi");
                $("#"+tagId).append("<a href='#' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' class='box-link' title='"+tagShortDesc[i]+"'>"+tagShortDesc[i]+"</a>");
                }
         }
            
        
         
            
        }
        
        function ToglFiltr(repId,repName){  
         window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
            if($("#hidbtn").val()=="false"){

            if(hasTouch){
             window.location.href=('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open');
         } else{
          window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
            }
         //   window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');




            }else{
                  //applyNewFilters(repId,repName)
            }
         //}
         //hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
    }
    function logout(){

    $.post( "<%=request.getContextPath()%>/baseAction.do?param=logoutApplication", function( data ) {
      //alert("logout");
      window.open("<%=request.getContextPath()%>","_self");
    });

    }
    function checkInputValue() {
        var x = document.getElementById("search").value;
          if (x == null || x == "") {
            alert("Enter Some Text To Search...");
            return false;
        }
    }
    function AdvanceSearch(){
                    window.open("<%=request.getContextPath()%>/srchQueryAction.do?srchParam=getsearchPage","_blank");
                }
                function submitSearchForm(){
                    $("#searchForm").submit();
                }
	</script>
            <style>
            ul {padding-left: 0px; margin: 0px;}
            #accordian {
                background-color: #004050;
                box-shadow: 0 5px 15px 1px rgba(0,0,0,.6), 0 0 200px 1px rgba(255,255,255,.5);
                color: white;
                /*width: 300px; commented by Faiz Ansari*/
            }

            /*heading styles*/


            #accordian h3 {
                /*fallback for browsers not supporting gradients*/
                background: #003040;
                background: linear-gradient(#003040, #002535);
                cursor: pointer;
                font-size: 12px;
                line-height: 34px;
                padding: 0 10px;

            }

            /*heading hover effect*/


            #accordian h3:hover { text-shadow: 0 0 1px rgba(255,255,255,.7); }

            /*iconfont styles*/


            #accordian h3 span {
                font-size: 16px;
                margin-right: 10px;
            }

            /*list items*/


            #accordian li { list-style-type: none; }
            #accordian li ul {background: #3A545E;}
            /*links*/


            #accordian ul ul li a {
                color: white;
                /*  display: block;*/
                font-size: 12px;
                line-height: 27px;
                padding: 0 15px;
                text-decoration: none;
                transition: all 0.15s;
            }

            #accordian ul ul li a:hover {
                background-color: #003545;
                border-left: 5px solid lightgreen;
            }

            #accordian li.active ul { display: block; }

            #rightMenu span{
                    display:block;	
                    background-color:#1a237e ;
                    color:#fff;
                    padding:5px;
                    cursor:pointer;
                    background: transparent linear-gradient(#003040, #002535) repeat scroll 0% 0%;
            }

            #rightMenu span a{
                    margin:0px;
                    padding:0px;
                    font-family:verdana;
                    font-size:15px;
                    cursor:pointer;
                    text-decoration:none;
                    color:#fff;
                    border-bottom:none;
                    display: inline;
                    
            }
            #rightMenu span a:hover{
                background-color: transparent;
            }
            #rightMenu  ul{
                display:none;
                margin-top:-5px;
               
            }
            
            #rightMenu  ul li a {
                color: white;
                display: block;
                font-size: 12px;
                line-height: 27px;
                padding: 0 15px;
                text-decoration: none;
                transition: all 0.15s;
                border-bottom: 1px solid #A9D6EF;
                font-family: Verdana;
            }

            #rightMenu  ul li a:hover {
                background-color: #003545;
                border-left: 10px solid lightgreen;
            }
            
            #rightMenu  ul li ul li a{
                background-color: #0d47a1;
            }
            .fa-m{
                    font-size:20px;
            }
            /*End!!!*/
        </style>

</head>
<body>
	<div id="backDiv"></div>
<!--	<div id="rightMenu">
            <span class="wave" onclick="openPage('home.jsp')"><i class="fa fa-home fa-m point"></i> <a> Home</a></span>
            <span class="wave"><i class="fa fa-tags fa-m point"></i> <a> Report Tags</a></span>
            <span class="wave"><i class="fa fa-heart fa-m point"></i> <a> Favorite Reports</a></span>
            <span class="wave"><i class="fa fa-tasks fa-m point"></i> <a> One View</a></span>
            <span class="wave"><i class="fa fa-tachometer fa-m point"></i> <a> KPI Dashboard</a></span>
            <span class="wave"><i class="fa fa-search fa-m point"></i> <a> Search</a></span>
            <span class="wave" onclick="openPage('home.jsp#Report_Studio')"><i class="fa fa-file-video-o fa-m point"></i> <a> Report Studio</a></span>
            <span class="wave"><i class="fa fa-desktop fa-m point"></i> <a> Dashboard Studio</a></span>
            <span class="wave"><i class="fa fa-file-text fa-m point"></i> <a> My Report</a></span>
            <span class="wave" onclick="logout();"><i class="fa fa-sign-out fa-m point"></i> <a> Logout</a></span>
        </div>-->
        <div class="cbp-spmenu cbp-spmenu-vertical cbp-spmenu-right" id="cbp-spmenu-s1" style="overflow: auto; z-index: 999999999;">
            
            
    <!-- Added by Faiz Ansari for Righr Quick Navigation -->
            <div id="rightMenu">
                <h3  id="showRightOCl" onclick="" style="cursor: pointer; color: #ffffff;padding-left: 10px;font-weight: bold " > Quick Navigator </h3>
                <span class="wave" onclick="openPage('home.jsp')"><i class="fa fa-home fa-m point"></i> <a> Home</a></span>
                <span class="wave"><i class="fa fa-tags fa-m point"></i><a> Report Tags</a></span>
                <ul id="reports_nav"></ul>
                <span class="wave"><i class="fa fa-heart fa-m point"></i> <a> Favorite Reports</a></span>
                <ul id="fav" ></ul>
                <span class="wave"><i class="fa fa-tasks fa-m point"></i> <a> One View</a></span>
                <ul id="oneview" ></ul>
                <span class="wave"><i class="fa fa-tachometer fa-m point"></i> <a> KPI Dashboard</a></span>
                <ul id="dash" ></ul>
                <span class="wave"><i class="fa fa-search fa-m point"></i> <a> Search</a></span>
                <span class="wave" onclick="openPage('home.jsp#Report_Studio')"><i class="fa fa-file-video-o fa-m point"></i> <a> Report Studio</a></span>
                <span class="wave"><i class="fa fa-desktop fa-m point"></i> <a> Dashboard Studio</a></span>
                <span class="wave"><i class="fa fa-file-text fa-m point"></i> <a> My Report</a></span>
                <span class="wave" onclick="logout();"><i class="fa fa-sign-out fa-m point"></i> <a> Logout</a></span>
            </div>
        </div>

    <div id="mycontainer">
        
	<div id="header">            
            <div id="cmpLogo">
                <span style="padding:0px 0px 0px 20px;"><a href="http://www.progenbusiness.com" target="_blank"><img src="images/pi_logo.png"></a></span>
                <div id="usernameDsp">Welcome <%=session.getAttribute("LOGINID")%></div>
            </div>
<!--            <div id="searchbox">
                <form class="form-inline" action="Search.jsp" target="_blank" onsubmit=" return checkInputValue()" name="myform">
                    <input id="search" class="form-control" style="float:left" type="text" name="data"  placeholder="Please enter the Search criteria  eg: Sales Report" autocomplete="off" >
                    <input id="submit" class="btn btn-primary" style=""type="submit" value="Search"  >
                </form>
            </div>-->
                
            <div id="navigation">
                <span class="pull-right"><a href="http://www.progenbusiness.com" target="_blank"><img src="images/prgLogo.gif"></a></span>
                <i id="menuBtn" class="fa fa-bars fa-2x pull-right point"></i>
                <i id="homeBtn" class="fa fa-home fa-2x pull-right point"></i>                
                
                <div id="mainMenu">
                    <span class="wave" onclick="openPage('home.jsp')"><a> Home</a></span>
                    <span class="wave" onclick="openPage('landingPage.jsp')"><a> Landing Page</a></span>
                    <span class="wave" onclick="openPage('home.jsp')"><a> Favorite Reports</a></span>
                    <%if (homeVar != null && homeVar.equalsIgnoreCase("newHome.jsp")) {%>
                    <span class="wave" onclick="openPage('home.jsp#RolesTab')"><a> Business Role</a></span>                                    
                    <%}%>                     
                    <span class="wave" onclick="openPage('home.jsp#All_Reports')"><a> My Reports</a></span>
                    <% if (isPowerAnalyserEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
                    <span class="wave" onclick="openPage('home.jsp#Dashboard_Studio')"><a> Dashboard Studio</a></span>
                    <span class="wave" onclick="openPage('home.jsp#Report_Studio')"><a> Report Studio</a></span>
                    <span class="wave" onclick="openPage('home.jsp#Report_Builder')"><a> Custom Report Builder</a></span>
                    <span class="wave" onclick="openPage('srchQueryAction.do?srchParam=getsearchPage')"><a> Search</a></span>
                    <%}%>
                    <%if(isQDEnableforUser){%>
                    <span class="wave" onclick="openPage('pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection')"><a> Query Studio</a></span>
                    <%}%>                        
                    <span class="wave" onclick="openPage('home.jsp#Html_Reports')"><a> HTML Reports</a></span>
                    <span class="wave" onclick="openPage('srchQueryAction.do?srchParam=headlinePage')"><a> Headlines</a></span>
                    <span class="wave" onclick="openPage('srchQueryAction.do?srchParam=oneViewBy')"><a> One View</a></span>
                    <span class="wave" onclick="openPage('srchQueryAction.do?srchParam=icalPage')"><a> i-Cal<sup>Beta</sup></a></span>
                    <span class="wave" onclick="openPage('reportTemplateAction.do?templateParam=workBenchPage')"><a> Work Bench</a></span>

                    <%if(isQDEnableforUser || userType.equalsIgnoreCase("ADMIN")) {%>
                    <span class="wave" onclick="openPage('srchQueryAction.do?srchParam=pbBiManager')"><a> BI Manager</a></span>
                    <%}%>
                    <span class="wave" onclick="openPage('home.jsp')"><a> Login Start Page</a></span>
                    <%if(isXtendUser) {%>
                    <span class="wave" onclick="openPage('home.jsp')"><a> Work Bench</a></span>
                    <span class="wave" onclick="openPage('home.jsp')"><a> Xtend</a></span>
                    <%}%>
<!--                        <span class="wave"><i class="fa fa-tags fa-m point"></i> <a> Report Tags</a></span>
                    <span class="wave"><i class="fa fa-heart fa-m point"></i> <a> Favorite Reports</a></span>
                    <span class="wave"><i class="fa fa-tasks fa-m point"></i> <a> One View</a></span>
                    <span class="wave"><i class="fa fa-tachometer fa-m point"></i> <a> KPI Dashboard</a></span>
                    <span class="wave"><i class="fa fa-search fa-m point"></i> <a> Search</a></span>
                    <span class="wave" onclick="openPage('home.jsp#Report_Studio')"><i class="fa fa-file-video-o fa-m point"></i> <a> Report Studio</a></span>
                    <span class="wave"><i class="fa fa-desktop fa-m point"></i> <a> Dashboard Studio</a></span>
                    <span class="wave"><i class="fa fa-file-text fa-m point"></i> <a> My Report</a></span>
                    <span class="wave" onclick="logout();"><i class="fa fa-sign-out fa-m point"></i> <a> Logout</a></span>-->

                </div>
            </div>            
	</div>
        <div id="maincontent">
            <div id="searchbox">
                <form id='searchForm' class="form-inline" action="Search.jsp" target="_blank" onsubmit=" return checkInputValue()" name="myform" style="width:60%;float:left">                    
                    <input id="search" class="form-control" style="float:left" type="text" name="data"  placeholder="Please enter the Search criteria  eg: Sales Report" autocomplete="off" >               
                          
                </form>
                <button id="submit" onclick='submitSearchForm()' class="btn btn-primary" style="padding:9px;float:left" title="Search"> <i class="fa fa-search"></i> </button>
                <button id="" onclick="AdvanceSearch();" class="btn btn-success" style="padding:9px;float:left;margin:0px 0px 0px 5px;" title="Advance Search"> <i class="fa fa-search-plus"></i> </button>
            </div>
            <%--<jsp:include page="Headerfolder/headerPage.jsp"/>--%>
        </div>
    </div>
</body>
</html>