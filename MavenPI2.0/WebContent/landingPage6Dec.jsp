<%--
    Document   : landingPage
    Created on : Nov 28, 2014, 4:31:12 PM
    Author     : Manik
--%>

<%@page import="com.progen.i18n.TranslaterHelper"%>
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
<%
 String themeColor = "blue";

    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
 //added by Dinanath for default locale
                    Locale currentLocale=null;
                   currentLocale=(Locale)session.getAttribute("UserLocaleFormat");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!--    <script type="text/javascript" src="<%=request.getContextPath()%>/JS/jquery-1.11.1.js"></script>-->
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/JS/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js" ></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/javascript/lib/jquery/css/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="css/menuTab.css" />
<!--below plugin added by dinanath-->
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->

        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />




        <%
                    String userid = String.valueOf(session.getAttribute("USERID"));
                    //PbDb pbdb = new PbDb();
                    //  String tagQuery = "select TAG_ID,TAG_TYPE from PRG_TAG_MASTER order by tag_id;";
                    // PbReturnObject tagObj = pbdb.execSelectSQL(tagQuery);
                    /// String minTagId = "";
                    //String minTagType = "";
                    //if(tagObj!=null && tagObj.getRowCount()>0){
                    //    minTagId = tagObj.getFieldValueString(0, 0);
                    //   minTagType = tagObj.getFieldValueString(0, 1);
                    // }
                    ServletContext context = getServletContext();
                     String userType = null;
 boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
  //  boolean isOneViewEnableforUser=false;
   // boolean isScoreCardsEnableforUser=false;

    // ServletContext context=this.getServletConfig().getServletContext();
     HashMap<String,UserStatusHelper> statushelper;
     //statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(context.getAttribute("helperclass")!=null){
     statushelper=(HashMap)context.getAttribute("helperclass");
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){

        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();

        userType=helper.getUserType();
        }
     }
     }


        %>

        <script type="text/javascript">

            var wid2='';
            
           // edit by shivam
           
            $(document).ready(function(){
//                var screenWid=screen.width;
                var screenWid=$(window).width();   
//$(document).height(); 
//$(window).width();   
//$(document).width()
                var leftW=((screenWid)*.3);

                //            var screenHei=screen.height;
                var rightW=((screenWid)*(.7));
//                $("#leftDiv").css('width', leftW);
                //$("#rightDiv").css('width', rightW);
                wid2=$("#rightDiv").width();
                var cur = 1;
                var max = $(".box-wrapper div").length;
//                alert("leftdiv "+$("#leftDiv").width());
            });

        </script>
        <style type="text/css">
            .slice{
                background-color:black;
            }
            .smallDiv{
                background-color: darkorchid;

                margin: 1%;
                /*margin-top: 1%;*/
                width: 22.5%;
                height: 150px;
                cursor: pointer;
                border: 1px solid black;
            }
            .container1{
                width: 96%;
                margin-left: 2%;
            }
        </style>
        <style type="text/css">
            .rightDivCl{
                background-color: whitesmoke;
                overflow:visible;
                height: 525px;
                /*             width: 94%;
                             margin-left: 3%;*/
                overflow: auto;
            }
            #gallery{
                position:relative;
                /*  margin: 0 auto;*/
                overflow:hidden;
                /*  width:auto;*/
                height:525px; /* +30 = space for buttons */
                text-align:center; /* to center the buttons */
            }
            #slider{
                position:absolute;
                left:0;
                height:525px;
                text-align:left; /* to reset text inside slides */
            }
            #slider > div {
                position:relative;
                float:left;
                /*  width:auto;*/
                height:525px;
            }
            #prev, #next{
                display:inline-block;
                position:relative;
                top:488px;
                cursor:pointer;
                padding:5px;
            }
        </style>
        <style>
           .small1:hover, .small2:hover, .small3:hover, .small4:hover, .small5:hover, .small6:hover, .small7:hover, .small8:hover,.small9:hover, .small10:hover, .small11:hover, .small12:hover {
/*	opacity: 1;
	box-shadow: 0 0 15px 15px #009900;*/
        background-color: orange;


/*	transform: rotateX(15deg) rotateY(5deg) translateZ(80px) scale(0.98);*/
 }
/*           .small5:hover, .small6:hover, .small7:hover, .small8:hover {
	opacity: 1;
       box-shadow: 0 0 15px 15px #3333CC;
		transform: rotateX(15deg) rotateY(5deg) translateZ(80px) scale(0.98);
}
           .small9:hover, .small10:hover, .small11:hover, .small12:hover {
	opacity: 1;
	box-shadow: 0 0 15px 15px #CC0000;
	transform: rotateX(15deg) rotateY(5deg) translateZ(80px) scale(0.98);
}*/

.button{
			background-image: -webkit-linear-gradient(top, #f4f1ee, #fff);
			background-image: linear-gradient(top, #f4f1ee, #fff);
			border-radius: 50%;
			box-shadow: 0px 8px 10px 0px rgba(0, 0, 0, .3), inset 0px 4px 1px 1px white, inset 0px -3px 1px 1px rgba(204,198,197,.5);
			float:right;
			height: 23px;
			margin: 0px 5px 0px 5px;
			position: relative;
			width: 23px;
			-webkit-transition: all .1s linear;
			transition: all .1s linear;
		}

		.button:after{
			color:#e9e6e4;
			content: "";
			display: block;
			font-size: 15px;
			height: 30px;
			text-decoration: none;
			text-shadow: 0px -1px 1px #bdb5b4, 1px 1px 1px white;
/*			position: absolute;*/
			width: 30px;
		}
                .tick:after{
/*			content: "✔";*/
                           content: "✖";
                           text-shadow:0px 0px 6px #83d244;
			left:5px;
			top:5px;
                        margin-left: 5px;
                        margin-top: 2px;
/*                        color: greenyellow;*/
                        color:#eb2f2f;
		}
                .button:hover{
			background-image: -webkit-linear-gradient(top, #fff, #f4f1ee);
			background-image: linear-gradient(top, #fff, #f4f1ee);
			color:#0088cc;
		}
                .tick1:after{
			color:green;
			text-shadow:0px 0px 6px #83d244;
                        content: "✔";
                        margin-left: 7px;
                        margin-top: 2px;
		}
                .button:active{
			background-image: -webkit-linear-gradient(top, #efedec, #f7f4f4);
			background-image: linear-gradient(top, #efedec, #f7f4f4);
			box-shadow: 0 3px 5px 0 rgba(0,0,0,.4), inset 0px -3px 1px 1px rgba(204,198,197,.5);
		}

		.button:active:after{
			color:#dbd2d2;
			text-shadow: 0px -1px 1px #bdb5b4, 0px 1px 1px white;
		}
      h2 { text-align: center; color: #CCC; }

            a:hover { color: #777;
            }
            li:hover {  background: #83AFB7  ;
            }

            /* NAVIGATION */
            .navigation {
                list-style: none;
                padding: 0;
                width: 250px;
                height: 35px;
                /*  margin: 20px auto;*/
                background: #83AFB7  ;
                position: absolute;
                z-index: 100;
            }
            .navigation1 {
                list-style: none;
                padding: 0;
                width: 250px;
                height: 24px;
                /*  margin: 20px auto;*/
                background: none repeat scroll 0% 0% rgb(212, 232, 236)  ;
                position: absolute;
                /*  z-index: 100;*/
            }
            .navigation, .navigation a.main {
                border-radius: 4px;
                -webkit-border-radius: 4px;
                -moz-border-radius: 4px;
            }
            .navigation:hover, .navigation:hover a.main {
                border-radius: 4px 4px 0 0;
                -webkit-border-radius: 4px 4px 0 0;
                -moz-border-radius: 4px 4px 0 0;
            }
            .navigation a.main {
                display: block;
                height: 24px;
                font: bold 14px/25px arial, sans-serif;
                text-align: center;
                text-decoration: none;
                color: #FFF;
                -webkit-transition: 0.2s ease-in-out;
                -o-transition: 0.2s ease-in-out;
                transition: 0.2s ease-in-out;
                position: relative;
                z-index: 100;
            }
            .navigation1 a.main {
                display: block;
                height: 24px;
                font: bold 14px/25px arial, sans-serif;
                text-align: center;
                text-decoration: none;
                color: green;
                -webkit-transition: 0.2s ease-in-out;
                -o-transition: 0.2s ease-in-out;
                transition: 0.2s ease-in-out;
                position: relative;
                /*  z-index: 100;*/
            }
            .navigation:hover a.main {
                color: rgba(255,255,255,0.6);
                /*  background: #C4D9DF;*/
                background: #83AFB7  ;
            }
            .navigation1:hover a.main {
                color: blue;
                text-decoration: underline;
            }
/*            for ie9*/
            .navigation:hover li{
/*                background-color: col  ;*/
display: block;
            }
            .navigation li {
                width: 100%;
                height: 35px;
                background: #F7F7F7;
                font: normal 12px/40px arial, sans-serif !important;
                color: #999;
                text-align: center;
                /*  margin-bottom: 5px;*/
                -webkit-transform-origin: 50% 0%;
                -o-transform-origin: 50% 0%;
                transform-origin: 50% 0%;
                -webkit-transform: perspective(350px) rotateX(-90deg);
                -o-transform: perspective(350px) rotateX(-90deg);
                transform: perspective(350px) rotateX(-90deg);
                box-shadow: 0px 2px 10px rgba(0,0,0,0.05);
                -webkit-box-shadow: 0px 2px 10px rgba(0,0,0,0.05);
                -moz-box-shadow: 0px 2px 10px rgba(0,0,0,0.05);
                border-bottom: 1px solid #C4D9DF;
/*                for ie 9*/
  display: none;
            }
            .navigation li:nth-child(even) { background: #F5F5F5; }
            .navigation li:nth-child(odd) { background: #EFEFEF; }
            /*.navigation li:nth-child(even) { background: #83AFB7; }*/
            /*.navigation li:nth-child(odd) { background: #83AFB7; }*/

            .navigation li.n1 {
                -webkit-transition: 0.2s linear 0.8s;
                -o-transition: 0.2s linear 0.8s;
                transition: 0.2s linear 0.8s;
            }
            .navigation li.n2 {
                -webkit-transition: 0.2s linear 0.6s;
                -o-transition: 0.2s linear 0.6s;
                transition: 0.2s linear 0.6s;
            }
            .navigation li.n3 {
                -webkit-transition: 0.2s linear 0.4s;
                -o-transition: 0.2s linear 0.4s;
                transition: 0.2s linear 0.4s;
            }
            .navigation li.n4 {
                -webkit-transition:0.2s linear 0.2s;
                -o-transition:0.2s linear 0.2s;
                transition:0.2s linear 0.2s;
            }
            .navigation li.n5 {
                border-radius: 0px 0px 4px 4px;
                -webkit-transition: 0.2s linear 0s;
                -o-transition: 0.2s linear 0s;
                transition: 0.2s linear 0s;
            }
            .navigation:hover li {
                -webkit-transform: perspective(350px) rotateX(0deg);
                -o-transform: perspective(350px) rotateX(0deg);
                transform: perspective(350px) rotateX(0deg);
                -webkit-transition:0.2s linear 0s;
                -o-transition:0.2s linear 0s;
                transition:0.2s linear 0s;

            }
            .navigation:hover .n2 {
                -webkit-transition-delay: 0.2s;
                -o-transition-delay: 0.2s;
                transition-delay: 0.2s;
            }
            .navigation:hover .n3 {
                -webkit-transition-delay: 0.4s;
                -o-transition-delay: 0.4s;
                transition-delay: 0.4s;
            }
            .navigation:hover .n4 {
                transition-delay: 0.6s;
                -o-transition-delay: 0.6s;
                transition-delay: 0.6s;
            }
            .navigation:hover .n5 {
                -webkit-transition-delay: 0.8s;
                -o-transition-delay: 0.8s;
                transition-delay: 0.8s;
            }

        </style>

        <style>
            #search-text-input{
                border-top:thin solid  #e5e5e5;
                border-right:thin solid #e5e5e5;
                border-bottom:0;
                border-left:thin solid  #e5e5e5;
                box-shadow:0px 1px 1px 1px #e5e5e5;
                float:left;
                height:17px;
                margin:.8em 0 0 .5em;
                outline:0;
                padding:.4em 0 .4em .6em;
                width:183px;
            }

            #button-holder{
                background-color:#f1f1f1;
                border-top:thin solid #e5e5e5;
                box-shadow:1px 1px 1px 1px #e5e5e5;
                cursor:pointer;
                float:left;
                height:27px;
                margin:11px 0 0 0;
                text-align:center;
                width:50px;
            }

            #button-holder img{
                margin:4px;
                width:20px;
            }
        </style>
        <style type="text/css">

            #searchbox
            {
                background-color: #eaf8fc;
                background-image: linear-gradient(#fff, #d4e8ec);
                /*    border-radius: 35px;*/
                border-width: 1px;
                border-style: solid;
                border-color: #c4d9df #a4c3ca #83afb7;
                width: 430px;
                height: 20px;
                padding: 1px;
                /*                margin: auto;*/
                overflow: hidden; /* Clear floats */
/*                    margin-left: 25%;*/
            }
            #search,
            #submit {
                float: left;
            }

            #search {
                padding-left: 7px;
                height: 18px;
                width: 330px;
                border: 1px solid #a4c3ca;
                font: normal 14px 'trebuchet MS', arial, helvetica;
                background: #f1f1f1;
                border-radius: 50px 3px 3px 50px;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.25) inset, 0 1px 0 rgba(255, 255, 255, 1);
                margin-left: 5px;
            }

            /* ----------------------- */

            #submit
            {
                /*    background-color: #6cbb6b;*/
                background-color: #33CCCC;
                /*    background-image: linear-gradient(#95d788, #6cbb6b);*/
                border-radius: 3px 50px 50px 3px;
                border-width: 1px;
                border-style: solid;
                border-color: #7eba7c #578e57 #447d43;
                box-shadow: 0 0 1px rgba(0, 0, 0, 0.3),
                    0 1px 0 rgba(255, 255, 255, 0.3) inset;
                height: 19px;
                margin: 0 0 0 10px;
                padding: 0;
                width: 70px;
                cursor: pointer;
                font: bold 12px Arial, Helvetica;
                /*    color: #23441e;*/
                color: white;
                text-shadow: 0 1px 0 rgba(255,255,255,0.5);
            }

            #submit:hover {
                /*    background-color: #95d788;*/
                background-color: #3399CC;
                /*    background-image: linear-gradient(#6cbb6b, #95d788);*/
            }

            #submit:active {
                /*    background: #95d788;*/
                background: #3399CC;
                outline: none;
                box-shadow: 0 1px 4px rgba(0, 0, 0, 0.5) inset;
            }

            /*#submit::-moz-focus-inner {
                   border: 0;   Small centering fix for Firefox
            }
            #search::-webkit-input-placeholder {
               color: #9c9c9c;
               font-style: italic;
            }*/

            #search:-moz-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }

            #search:-ms-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }
            #search.placeholder {
                color: #9c9c9c !important;
                font-style: italic;
            }
        </style>
        <style>
            .ui-autocomplete {
                position: absolute;
                max-height: 128px;
                max-width: 380px;
                overflow-y: auto;
                /* prevent horizontal scrollbar */
                overflow-x: hidden;
                /* add padding to account for vertical scrollbar */
                padding-right: 10px;
                padding-left: 5px;
                padding-top: 3px;
                padding-bottom: 5px;
            }

            .small1 ,.small2,.small3,.small4{

              background-color: #20B2AA;
/*                 IE10 Consumer Preview
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Mozilla Firefox
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Opera
                background-image: -o-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Webkit (Safari/Chrome 10)
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #009900), color-stop(1, #006600));

                 Webkit (Chrome 11+)
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 W3C Markup, IE10 Release Preview
                background-image: radial-gradient(ellipse farthest-side at center, #009900 50%, #006600 100%);*/

            }
/*            .small2{
                                background-color: #8B008B;
                                background-color: #3CB371;
                                background-color: #008A00;
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 5%,  #006600 100%);
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 50%,  #006600 100%);

               /* IE10 Consumer Preview */
/*                background-image: -ms-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #009900), color-stop(1, #006600));

                 Webkit (Chrome 11+)
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 W3C Markup, IE10 Release Preview
                background-image: radial-gradient(ellipse farthest-side at center, #009900 50%, #006600 100%);
            }
            .small3{
                                background-color: #DC143C;
                                background-color: #C71585;
                                background-color: #d66b00;
                                background-color: rgb(0, 153, 51);
                                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 5%,  #006600 100%);

                 IE10 Consumer Preview
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Mozilla Firefox
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Opera
                background-image: -o-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Webkit (Safari/Chrome 10)
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #009900), color-stop(1, #006600));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #009900 50%, #006600 100%);
            }
            .small4{
                                background-color: #e61c67;
                                background-color: #e61c67;
                                background-color: #c2c200;
                                background-color: rgb(102, 194, 133);
                                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 5%,  #006600 100%);


                 IE10 Consumer Preview
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Mozilla Firefox
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #009900), color-stop(1, #006600));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #009900 50%, #006600 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #009900 50%, #006600 100%);
            }*/
            .small5,.small6,.small7,.small8{

                 background-color: #9370DB;

/*                 IE10 Consumer Preview
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #3333CC), color-stop(1, #000099));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #3333CC 50%, #000099 100%);*/
            }
/*            .small6{
                                background-color: #fdd22a;
                                background-color: #FF6347;
                                background-color: rgb(41, 82, 204);
                                background-image: -moz-radial-gradient( center, ellipse farthest-side,#FFFF66 0%, #000099 100%);

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #3333CC), color-stop(1, #000099));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #3333CC 50%, #000099 100%);
            }
            .small7{
                                background-color: #574696;
                                background-color: #008080;
                                background-color: rgb(41, 122, 204);

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #3333CC), color-stop(1, #000099));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #3333CC 50%, #000099 100%);
            }
            .small8{
                                background-color: #009fe3;
                                background-color: rgb(71, 163, 255);

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #3333CC), color-stop(1, #000099));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #3333CC 50%, #000099 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #3333CC 50%, #000099 100%);
            }*/
            .small9,.small10,.small11,.small12{

                background-color: #9ACD32;

/*                background-image: -moz-radial-gradient( center, ellipse farthest-side,#CC0000 50%, #990000 100%);

                 IE10 Consumer Preview
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                /* Mozilla Firefox */
/*                background-image: -moz-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #CC0000), color-stop(1, #990000));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #CC0000 50%, #990000 100%);*/
            }
/*            .small10{
                                background-color: #F08080;
                                background-color: #D14719;

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #CC0000), color-stop(1, #990000));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #CC0000 50%, #990000 100%);
            }
            .small11{
                                background-color: #9400D3;
                                background-color: #004C99;
                                background-color: rgb(219, 112, 77);

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #CC0000), color-stop(1, #990000));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #CC0000 50%, #990000 100%);
            }
            .small12{
                                background-color: #3CB371;
                                background-color: #E69980;

                 IE10 Consumer Preview 
                background-image: -ms-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Mozilla Firefox 
                background-image: -moz-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Opera 
                background-image: -o-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 Webkit (Safari/Chrome 10) 
                background-image: -webkit-gradient(radial, center center, 0, center center, 487, color-stop(0, #CC0000), color-stop(1, #990000));

                 Webkit (Chrome 11+) 
                background-image: -webkit-radial-gradient(center, ellipse farthest-side, #CC0000 50%, #990000 100%);

                 W3C Markup, IE10 Release Preview 
                background-image: radial-gradient(ellipse farthest-side at center, #CC0000 50%, #990000 100%);
            }​*/

        </style>
<!--added by Dinanath-->
<style>
    .btn-custom { background-color: hsl(195, 60%, 35%) !important;
                 background-repeat: repeat-x;
                 filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#2d95b7", endColorstr="#23748e");
                 background-image: -khtml-gradient(linear, left top, left bottom, from(#2d95b7), to(#23748e));
                 background-image: -moz-linear-gradient(top, #2d95b7, #23748e);
                 background-image: -ms-linear-gradient(top, #2d95b7, #23748e);
                 background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #2d95b7), color-stop(100%, #23748e));
                 background-image: -webkit-linear-gradient(top, #2d95b7, #23748e); background-image: -o-linear-gradient(top, #2d95b7, #23748e);
                 background-image: linear-gradient(#2d95b7, #23748e); border-color: #23748e #23748e hsl(195, 60%, 32.5%);
                 color: #fff !important; text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.16); -webkit-font-smoothing: antialiased;
    }
</style>
<style>
  .btn-custom2 { background-color: hsl(190, 80%, 43%) !important; background-repeat: repeat-x;
                filter: progid:DXImageTransform.Microsoft.gradient(startColorstr="#27c7e7", endColorstr="#15a8c5");
                background-image: -khtml-gradient(linear, left top, left bottom, from(#27c7e7), to(#15a8c5));
                background-image: -moz-linear-gradient(top, #27c7e7, #15a8c5);
                background-image: -ms-linear-gradient(top, #27c7e7, #15a8c5);
                background-image: -webkit-gradient(linear, left top, left bottom, color-stop(0%, #27c7e7), color-stop(100%, #15a8c5));
                background-image: -webkit-linear-gradient(top, #27c7e7, #15a8c5); background-image: -o-linear-gradient(top, #27c7e7, #15a8c5);
                background-image: linear-gradient(#27c7e7, #15a8c5); border-color: #15a8c5 #15a8c5 hsl(190, 80%, 40.5%);
                color: #fff !important; text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.16); -webkit-font-smoothing: antialiased;
  }
</style>
<style>
    #changeSequence{

background-color: rgb(255,255,255);
@include filter-gradient(#ffffff, #e5f0f5, vertical);
@include background-image(linear-gradient(top, rgba(255,255,255,1) 0%,rgba(246,250,252,1) 35%,rgba(229,240,245,1) 100%));
    }
    #ui-dialog-title-changeSequence{
        color:white;
}
#ui-dialog-title-changeSequenceOfReportAssignment{
        color:white;
}
.setHeightScrollable{
    height:310px;
    overflow-y: auto;
}
</style>
        <!--added by dinanath -->
        <script type="text/javascript">

            var searchtextname = [];
            var searchtextid=[];
            var searchdesc=[];

            var tagReportName=new Array();
            var tagReportId=new Array();

//            $(document).ready(function() {
//                $.ajax({
//                    url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getSearchReports',
//                    success: function(data) {
//                        var jsonVar=eval('('+data+')');
//                        //                                 alert("DATA IS : "+data);
//                        var tagReportId1=jsonVar.tagReportId
//                        var tagReportName1=jsonVar.reportName
////                         var reportName1=jsonVar.reportName
//
//                        for(var i=0;i<tagReportName1.length;i++)
//                        {
//                            //                                    alert("json1: "+json[i].tagId);
//                            tagReportId.push(tagReportId1[i]);
//                            //                                    alert("json2: "+json[i].tagName);
//                            tagReportName.push(tagReportName1[i]);
//                        }
//                        for(i=0;i<tagReportName.length;i++)
//                        {
//                            searchtextid[i]=tagReportId[i];
//                            searchtextname[i] = tagReportName[i];
//                        }
////                        $( "#search" ).autocomplete({
////                            source: searchtextname,
////                            minLength:1
////                        });
//                    }
//                });
//            });
 function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
//                document.forms.myFormH.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
//                document.forms.myFormH.submit();
                window.location.href=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                }
            }
        </script>

            <body >
<!--    <body onload="checkPie('11px verdana')">-->
        <form name="searchForm" action="landingPage.jsp" id="reportForm" method="POST" >
            <table>
                <tr>
                    <!--                <div id="header " class="container1" style="border: 1px solid #000000;width: 96%;height: 50px;margin-left: 2%">-->

<!--                    <img alt="" border="0px"  width="40px" height="30px"   src="<%=request.getContextPath()%>/images/pi_logo.png"/>
                    <img alt="" border="0px"  width="40px" height="30px"   src="<%=request.getContextPath()%>/images/ProGen_Logo.png"/>-->
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                    <!--          </div>-->

                </tr>
                <tr>
                    <!--                <div class="container" style="width: 96.05%;background-color: #FC3;margin-left: 2%;">
                                            <form id="searchbox" action="">
                                                <input id="search" type="text" placeholder="Search here for Reports " autocomplete="off">
                                                    <input id="submit" type="submit" value="Go..">
                                            </form>
                                    </div>-->
                <div class="container1" style="background-color: #D4E8EC;height: 24px">

                    <!--    <div style="width: 55%;float: left">-->

                    <!--    </div>-->
                    <div style="">
                        <form id="searchbox" action="Search.jsp" target="_blank" onsubmit=" return checkInputValue()" name="myform" style="float: left">
                            <input id="search" type="text" name="data"  placeholder="<%=TranslaterHelper.getTranslatedInLocale("plch_search", currentLocale)%>" autocomplete="off" >
                            <!--                            <ul class="navigation" style="position:absolute; float: left;width:10%;height: 35px">
                                <a class="main" href="#">Display Type</a>
                                <li class="n1" onclick="displayType('TagName')"><a href="#">Tag</a></li>
                                <li class="n2" onclick="displayType('TagLongName')" ><a href="#">Tag Long Desc</a></li>
                                <li class="n3" onclick="displayType('TagShortName')" ><a href="#">Tag Short Desc</a></li>
                                <li class="n4" onclick="displayType('Report')" ><a href="#">Report</a></li>

                            </ul>-->
<!--                            <input id="submit" type="button" value="Search"  onClick="valid(this.form)">-->
                            <input id="submit" type="submit" value="<%=TranslaterHelper.getTranslatedInLocale("search", currentLocale)%>"  >
                        </form>
                        <div class="" style="height:24px;width:155px;float: left">
                            <!--                            <label style="font: 20px"><a href="#" onclick="AdvanceSearch()"><h3 style="height: 35px">Advance Search</h3></a></label>-->
                            <ul class="navigation1" style="position:absolute; width:10%;height: 24px">
                                <a class="main"  href="#" title="Click for Advance search" onclick="AdvanceSearch()"><%=TranslaterHelper.getTranslatedInLocale("advance_search", currentLocale)%></a>
                            </ul>
                        </div>
                        <!--                        <label style="font: 5px"><a href="#"><h3>Action</h3></a></label>-->
<!--                        <button value="false" id="Tbtn">click</button>  -->

<!--                        <a href="#" id="Tbtn" onclick="" value="false"  class="button cross"></a>-->


                        <div  style="height:24px;width:145px;float: right">
                            <ul class="navigation" style="position:absolute; width:10%;height: 24px">
                                <a class="main" href="#url"><%=TranslaterHelper.getTranslatedInLocale("rep_filter", currentLocale)%></a>
                                <li class="n1" href="#" onclick="filterFor('R')" ><a href="#"><%=TranslaterHelper.getTranslatedInLocale("rep_analyt", currentLocale)%></a></li>
                                <li class="n2" href="#" onclick="filterFor('O')" ><a href="#"><%=TranslaterHelper.getTranslatedInLocale("oneview", currentLocale)%></a></li>
                                <li class="n3" href="#" onclick="filterFor('D')" ><a href="#"><%=TranslaterHelper.getTranslatedInLocale("dashboard", currentLocale)%></a></li>
                                <li class="n4" href="#" onclick="filterFor('All')" ><a href="#"><%=TranslaterHelper.getTranslatedInLocale("rep_type", currentLocale)%></a></li>
                                <!--	<li class="n5"><a href="#">item #5</a></li>-->
                            </ul>
                            <!--                            <ul class="navbar color2" style="position:absolute; width:10%;height: 35px">
                                                            <li class="drpdown menu-item" style="width:100%;height: 35px;">
                                                                <a href="#" class=""><span>
                                                                        <font style="font-size: 15px; color: white" size="3" face="verdana">Filter </font>
                                                                    </span>
                                                                </a>
                            -->
                            <!--                                    <ul class="drpcontent" style="top:30px;padding: 0px">
                                                                    <li><a data-color="color1" style="padding:0px 0px 0px 24px;" onclick="AddInnerViewbys('/piNewUdise15Dec_1')">Analytical Report</a></li>
                                                                    <li><a data-color="color1" style="padding:0px 0px 0px 24px;" onclick="AddMoreDims('/piNewUdise15Dec_1')">Oneview</a></li>
                                                                    <li><a data-color="color1" style="padding:0px 0px 0px 24px;" onclick="RemoveMoreDims('/piNewUdise15Dec_1')">Kpi Dashboard</a></li>
                                                                    <li><a data-color="color1" style="padding:0px 0px 0px 24px;" onclick="sequenceParams('/piNewUdise15Dec_1')">All Report Type</a></li>
                                                                    <li><a data-color="color1" style="padding:0px 0px 0px 24px;" onclick="saveParamSection('/piNewUdise15Dec_1')">Save Parameters</a></li>
                                                                </ul>-->
                            <!--                                </li>
                                                        </ul>-->
                        </div>
<button value="false" style="display: none" id="hidbtn">click</button>
<div class=""  onclick="" style="height:24px;width:45px;float: right">
    <a href="#" id="Tbtn" onclick="mediateFilterEnable()" value="false" title="Click to enable/disable intermediate filter for report" class="button tick"></a>
               </div>
                    </div>

                </div>
                </tr>
                <tr>
                <div class="container1">
                    <table width="100%">    <tr>
                            <td style="width: 30%">
                                <div id="leftDiv" style="width: 100%;height: 500px;">
<!--                                      <img alt="" border="0px"  width="20px" height="20px" title="Change Font" onclick="changeFont()" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/Font_ico.png"/>-->
                                    <div class="dropdown" style="cursor:pointer">
                        <span data-toggle="dropdown" class="dropdown-toggle" style="text-decoration: none">
      <img alt="" border="0px"  width="20px" height="20px" title="Change Font" onclick="" style="cursor:pointer;position:absolute;display:inline;" src="<%=request.getContextPath()%>/images/Font_ico.png"/>
                        </span>
                        <ul id="fontList" style="margin-left: 20px;min-width: 0px" class="dropdown-menu">

                            <li>
                                <a style="font:10px verdana"  onclick="changeFont('Small')">Small</a>
                            </li>
                            <li>
                                <a style="font:12px verdana" onclick="changeFont('Medium')">Medium</a></li>
                            <li>
                                <a style="font:16px verdana" name="chart1" onclick="changeFont('Large')">Large</a>
                            </li>
<!--                            <li>
                                <a onclick="">Save Graph</a>
                            </li>-->
                        </ul>
                    </div>
<!--      <img alt="" border="0px" align="right" width="30px" height="20px" title="Change Sequence" onclick="changeSeq()" style="cursor:pointer; display:inline;position: relative" src="<%=request.getContextPath()%>/images/bothsidearrow.png"/>-->
                                    <div id="svgContain" style="height: 73%;margin-right: 2.5%;width: 100%;">
                                    </div>
                                    <div id="textLeft" style="height: 12%;width: 104%;border-top: 1px solid indigo;border-bottom:  1px solid indigo;margin-top: 10%">
<!--                                        <div style="margin-left:30px;margin-top:10px;width:60px;height:50px;"">-->
   <img alt="" border="0px"  width="30px" height="20px" title="Change Sequence" onclick="changeSeq()" style="cursor:pointer;display:inline;" src="<%=request.getContextPath()%>/images/bothsidearrow.png"/>
<!--</div>-->
                                        <h1 align="center" style="color:#0044FA;display:inline;"> <%=TranslaterHelper.getTranslatedInLocale("pi_msg", currentLocale)%></h1>
<!--                                        <img alt="" border="0px"  width="30px" height="20px" title="Change Font" onclick="changeFont()" style="cursor:pointer;display:inline;" src="<%=request.getContextPath()%>/images/bothsidearrow.png"/>-->
                                        <br/>
                                        <br/>

                                    </div>
                                </div>
                                <!--                            </div>-->
                            </td>                      <td style="width: 70%">
                                <!--                            <div id="rightDiv" class="row placeholders" style="height: 500px;border: 1px solid #000000;width: 94%;margin-left: 3%;display: table">-->
                                <!-- <div id="rightDiv" class="container wrapper" style="background-color: whitesmoke" id="main" role="main" style="height: 500px;border: 1px solid #000000;width: 94%;margin-left: 3%;display: table">-->


                                <div id="rightDiv" class="rightDivCl wrapper" style="border-left: 1px solid purple "  role="main" >
                                    <div id="gallery" class="rightDivCl">
                                        <div id="slider">
                                            <div id="d1"class=" " style="background:white"></div>
                                            <div id="d2" class=" " style="background:white"></div>
                                            <div id="d3" style="background:white"></div>
                                        </div>
                                        <span id="prev" style="display: none;"><img title="Click for previous reports" src="<%=request.getContextPath()%>/images/_arrow-left-.png" /></span>
                                        <span id="next" style="display: none" ><img title="Click for next reports" src="<%=request.getContextPath()%>/images/_arrow-right-.png" /></span>
                                    </div>
                                </div>
                                 <img alt="" border="0px"  width="30px" height="20px" title="Change Sequence of Report" onclick="changeSeqOfRAssignment()" style="cursor:pointer;display:inline;float:right;" src="<%=request.getContextPath()%>/images/bothsidearrow.png"/>
                            </td>   </tr>
                    </table>
                </div>
                </tr>
            </table>

            <script type="text/javascript">
            var selectedSlice='';
            var filtagSname='';
            var  filtagLname='';
            var filRepId='';
            var filtagType='';
            var hasTouch ='';
 var tagAssignIdForSeq='';
            var tagShortNameForSeq='';
function checkInputValue() {
    var x = document.getElementById("search").value;
      if (x == null || x == "") {
        alert("Your search is Blank");
        return false;
    }
}

            function AdvanceSearch(){
                window.open("<%=request.getContextPath()%>/srchQueryAction.do?srchParam=getsearchPage","_blank");
            }
            function mHover(sliced){
                if(sliced.id!=selectedSlice){
                    var slicedId="path"+sliced.id;
                    document.getElementById(slicedId).style.fill = "orange";
                    //document.getElementById("svgid").transition().attr("x",320);
                }}
            function mHover1(sliced){
                if(sliced.id!=selectedSlice){
                    var slicedId="path"+sliced.id;
                    document.getElementById(slicedId).style.fill = "#009fe3";
                }}

            function displayReports(tagId){
                var title = document.getElementById(tagId).getAttribute("title");
                //                alert(selectedSlice)
                if(selectedSlice!=''){
                    var tagI="path"+selectedSlice;
                    document.getElementById(tagI).style.fill = "#009fe3";

                }
                selectedSlice=tagId;
                if(tagId==firstId){
                    selectedSlice=firstId;
                    document.getElementById("path"+firstId).style.fill = "#1B3E70";
                }else{
                    var tagI="path"+tagId;
                    document.getElementById(tagI).style.fill = "#1B3E70";
                }
                //                if(tagId==="1004"){
                
               //  added by shivam

                var tileFontSize =($(window).width()*.02);
                if(tileFontSize>=18){
                    tileFontSize=18;
                }
                var subtileFontSize =($(window).width()*.014);
                if(subtileFontSize>=11){
                    subtileFontSize=11;
                }
                
                if(title=='Maps' || title=='maps' || title=='MAPS' || title=='Interactive thematic maps and charts'){
                    var ctxPath='<%=request.getContextPath()%>';
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url:ctxPath+'/reportViewer.do?reportBy=getTagsBlocks&userId='+<%=userid%>+'&tagId=0000',
                        success:function(json) {
                            var data = JSON.parse(json);
                            $("#d1").html("");
                            $("#d2").html("");
                            $("#d3").html("");
                            var keys = Object.keys(data);
    //              added by manik
                            if(keys.length>12){
                                $("#prev").css('display', '');
                                $("#next").css('display', '');
                            }else{
                                $("#prev").css('display', 'none');
                                $("#next").css('display', 'none');
                            }
                            for(var i=0;i<keys.length;i++){
                                //                                                for(var i=0;i<46;i++){
                                var div="<div id='tile_M_"+(i+1)+"'  onclick='getXtendUI(\""+encodeURIComponent(keys[i])+"\",\""+encodeURIComponent(data[keys[i]])+"\")' class='small"+(i+1)+"' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:60px' >\n\
                                <span  style='color:#336699'><h1 align='center' style='margin-top:50px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+keys[i]+"'> "+keys[i]+" </h1></span>\n\
                                </div></tr>\n\
                                    <tr><div style='height:50px;border-top:1px dotted white' title='"+keys[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+keys[i]+"</h4>\n\
                            </div></tr></td></table></div>\n\
                            </div> ";
                                if(i>12 && i<=24){
                                    var div1="<div id='tile_M_"+(i-12)+"' class='small"+(i-12)+"'  onclick='getXtendUI(\""+encodeURIComponent(keys[i])+"\",\""+encodeURIComponent(data[keys[i]])+"\")'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:60px' >\n\
                                <span  style='color:#336699'><h1 align='center' style='margin-top:50px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+keys[i]+"'> "+keys[i]+" </h1></span>\n\
                                </div></td></tr>\n\
                                    <tr><div style='height:50px;border-top:1px dotted white' title='"+keys[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+keys[i]+"</h4>\n\
                            </div></tr></table></div>\n\
                            </div> ";
                                    $("#d2").append(div1);
                                    //     $("#rightDiv2").append(div);

                                }else if(i>24)
                                {
                                    var div2="<div id='tile_M_"+(i-24)+"' class='small"+(i-24)+"' onclick='getXtendUI(\""+encodeURIComponent(keys[i])+"\",\""+encodeURIComponent(data[keys[i]])+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:60px' >\n\
                               <span href='#' style='color:#336699' ><h1 align='center' style='margin-top:50px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+keys[i]+"'> "+keys[i]+" </h1></span>\n\
                                </div></td></tr>\n\
                                    <tr><div style='height:50px;border-top:1px dotted white' title='"+keys[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+keys[i]+"</h4>\n\
                            </div></tr></table></div>\n\
                            </div> ";
                                    $("#d3").append(div2);
                                }
                                else{
                                    //    var innerDiv="<div id='rightDiv1' class=' box-wrapper wrapper' style='display: table;clear:both;border-left:2px solid indigo ; background-color: grey;overflow:visible;height: 500px;width: 94%;margin-left: 3%;overflow: auto; '  role='main' ></div>"
                                    $("#d1").append(div);
                                }
                            }
                        }
                    })

                    ////                var tagId = '1009';
                    //window.open("http://183.82.3.61:8085/xtend_5.6.1/user","_blank");
                    //location.reload();
                    //                 $("#rightDiv").html("<label style='font-size:20px;color:grey'>View Map in New Tab</label>");
                }
                else{
                    $("#d1").html("");
                    $("#d2").html("");
                    $("#d3").html("");
                    //added by nazneen for getting blocks based on clicked tag on 4Dec 2014
                    var ctxPath='<%=request.getContextPath()%>'
                    //                var tagId = '1009';
                    $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                        url:ctxPath+'/reportViewer.do?reportBy=getTagsBlocks&userId='+<%=userid%>+'&tagId='+tagId,
                        success:function(data) {
                                    
                            filtagSname='';
                            filtagLname='';
                            filRepId='';
                            filtagType='';

                            var jsonVar=eval('('+data+')')
                            var tagShortDesc=jsonVar.tagShortDesc
                            var tagLongDesc=jsonVar.tagLongDesc
                            var reportId=jsonVar.reportId
                            var tagType=jsonVar.tagType
                            var tagAssignId=jsonVar.tagAssignId
//                            alert(tagShortDesc);
//                            alert(tagAssignId);
                            filtagSname=tagShortDesc;
                            filtagLname=tagLongDesc;
                            filRepId=reportId;
                            filtagType=tagType;
                            tagAssignIdForSeq=tagAssignId; 
                            tagShortNameForSeq=tagShortDesc;
//                            alert("tagType "+tagShortDesc.length)

                            if(tagShortDesc.length==0){
                                $("#prev").css('display', 'none');
                                $("#next").css('display', 'none');
//                                setTimeout(alert("There is no data to display !!! Please select another option"), 2000);
//                               alert("There is no data to display !!! Please select another option");
                               $("#d1").html("<h3 align='middle' style='font:19px verdana;margin-top:10px;'>There is no data to display !!! Please select another option</h3>");
                            }else{

                                if(tagShortDesc.length>12){
                                    $("#prev").css('display', '');
                                    $("#next").css('display', '');
                                }else{
                                    $("#prev").css('display', 'none');
                                    $("#next").css('display', 'none');
                                }
                                for(var i=0;i<tagShortDesc.length;i++){
                                    var div = "";
                                    //                                                    for(var i=0;i<46;i++){


                                    if(tagType[i]=="R"){
                                       div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else {
                                       div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";
                                    div+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";

                                    if(i>11 && i<=23){
                                        var div1="";
                                        if(tagType[i]=="R"){
                                       div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else {
                                       div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div1+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";

                                        div1+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";
                                        $("#d2").append(div1);
                                        //     $("#rightDiv2").append(div);

                                    }else if(i>23)
                                    {
                                        var div2="";
                                        if(tagType[i]=="R"){
                                       div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else {
                                       div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div2+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";

                                        div2+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";
                                        $("#d3").append(div2);
                                    }
                                    else{
                                        //    var innerDiv="<div id='rightDiv1' class=' box-wrapper wrapper' style='display: table;clear:both;border-left:2px solid indigo ; background-color: grey;overflow:visible;height: 500px;width: 94%;margin-left: 3%;overflow: auto; '  role='main' ></div>"
                                        $("#d1").append(div);
                                    }
                                }
                            }}
                    });
                }
            }
            //       var sliceColor="#02B3E7";
            //       var sliceColor="#3737CC";
            var sliceColor="#009fe3";
            var firstId='';
            var firstType='';
            var title='';
    var tagIdS=[];
    var tagNameS=[];
    var fontSize='12px verdana';
    
     function checkPie(fontSize1){
//     $(window).load(function (){     
     fontSize=fontSize1;
//             $(document).ready(function() {
     $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
         function(data){
            
                    var data1 = JSON.parse(data);
                    for(var i=0;i<data1.length;i++){
                        tagIdS.push(data1[i]["Id"]);
                        tagNameS.push(data1[i]["Region"]);
                     }
                   });
//                });
                var funDisplay="displayReports(this.id)";
                //       var wid=$(window).width();
                var wid=($("#svgContain").width());
                //var hgt=$(window).height()-200;
                var hgt=($("#svgContain").height());
                var width = wid,
                height = wid,
                radius = Math.min(width, height) / 2 ;
                var div = d3.select("body").append("div")
                .attr("class", "tooltip")
                .style("opacity", 0);
                var color = d3.scale.category10();
                var arc = d3.svg.arc()
                .outerRadius(radius);

                $.ajax({
                    type: 'GET',
                    async: false,
                    cache: false,
                    timeout: 30000,
                    url:'reportViewer.do?reportBy=getDataCall&userId=<%=userid%>',
                    success: function(data1){
                      
                        var data = JSON.parse(data1);
                        firstId=data[0]["Id"];
                        firstType=data[0]["Type"];
                        title=data[0]["titleValue"];
                        if(fontSize !="null"){
                        fontSize=data[0]["fontSize"]+'px verdana';
                        }
                        var d;
                        var pie = d3.layout.pie() //this will create arc data for us given a list of values
                        .value(function(d) {return d.NetValue; });
                        var svg = d3.select("#svgContain").append("svg")
                        .datum(data)
                        .attr("width", width)
                        .attr("height", height)
                        .attr("id", "svgid")
                        .append("g")
                        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
                        .attr("style","margin-top:20px");
                        var gradient = svg.append("svg:defs").selectAll("linearGradient").data(data).enter()
                        .append("svg:linearGradient")
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
                        .attr("stop-color", function(d,i) {return color(i);})
                        .attr("stop-opacity", 1)
                        .attr("style","margin-top:20px");
                        var arcs = svg.selectAll("g.arc")
                        .data(pie)
                        .enter().append("g")
                        .attr("class", "arc")
                        .on("mouseover", function(d) {
                            mHover(this);
                        })
                        .attr("title",function(d){
                            return d.data["titleValue"];
                        })
                        .on("mouseout", function(d) {
                            mHover1(this);
                        })
                        .attr("id", function(d) {return d.data["Id"];})
                        //                .on("onclick","displaReports()")
                        .attr("onclick",funDisplay)
                        .attr("style","cursor: pointer;");
                        arcs.append("path")
                        .style("fill", function(d) {return sliceColor;})
                        .attr("id", function(d) {return "path"+d.data["Id"];})
                        .transition()
                        .ease("bounce")
                        .duration(2000)
                        .attrTween("d", tweenPie)
                        .transition()
                        .ease("elastic")
                        .delay(function(d, i) { return 2000 + i * 50; })
                        .duration(750)
                        .attr('stroke', '#fff')
                        .attr('stroke-width', '4')
                        arcs.filter(function(d) {
                            return d.endAngle ;
                        })
                        .append("svg:text")
                        .attr("dy", ".35em")
                        .attr("text-anchor", "middle")
                        .attr("style", "font-family: lucida grande")
                        .attr("style", "font-size: 19px")
                        .attr("class", "textColor")
                        .attr("style", "background-color: white")
                        .attr("transform", function(d) { //set the label's origin to the center of the arc
                            var a = angle(d);
                            if (a > 90) {
                                a = a + 180;
                                d.outerRadius = radius; // Set Outer Coordinate
                                d.innerRadius = radius*.3;
                            } else {
                                d.outerRadius = radius; // Set Outer Coordinate
                                //                    d.innerRadius = radius / 3;
                                d.innerRadius = radius*.3;
                            }
                            //                d.outerRadius = radius + 50; // Set Outer Coordinate
                            //        d.innerRadius = radius + 45;
                            return "translate(" + arc.centroid(d) + ")rotate(" + a + ")";
                        })
                        .style('fill', '#fff')
//                        .style('font', '15px verdana')
                        .style('font', fontSize)
                        //                .style('stroke-width', '2')
                        .text(function(d) {
                            return d.data["Region"];
                        })
                        //            .attrTween("d", tweenPie)
                        ;
                        function angle(d) {
                            return (d.startAngle + d.endAngle) * 90 / Math.PI - 90;
                        }
                        function tweenPie(b) {
                            b.innerRadius = radius*.3;
                            var i = d3.interpolate({startAngle: 2, endAngle: 0}, b);
                            return function(t) { return arc(i(t)); };
                        }
                        var test1="Click  \\ to select" ;
                        var center_group = svg.append("g")
                        .attr("class", "ctrGroup")
                        .attr("transform", "translate(" + 0 + "," + 15 + ")");
                        center_group
                        .append("svg:text")
                        .attr("text-anchor", "middle")
                        .append('svg:tspan')
                        .attr('x', 0)
                        .attr('dy', "-1.5em")
                        .attr("style", "font-size: 15px")
                        .attr("fill", sliceColor)
                        .text(function(d) { return "Click"; })
                        .append('svg:tspan')
                        .attr('x', 0)
                        .attr('dy', 20)
                        .attr("style", "font-size: 15px")
                        .attr("fill", sliceColor)
                        .text(function(d) { return "to Select"; })
                        ;
                    }
                });
               displayReports(firstId);
            
            }
            
                jQuery(document).ready(function($) {
                
                hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
                
                    var wid=($("#gallery").width());
                    $("#d1").css('width', wid);
                    $("#d2").css('width', wid);
                    $("#d3").css('width', wid);
                    $('.smallDiv ').hover(function() {
                        $(this).stop().animate({
                            opacity: 1
                        }, 200);
                    }, function() {
                        $(this).stop().animate({
                            opacity: 0.3
                        }, 200);
                    });

                    $("#AddMoreFiltersDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 750,
                        position: 'justify',
                        modal: true,
                        resizable:true
                    });
                });

                var $gal = $('#gallery'),
                $sli = $('#slider'),
                $box = $('div',$sli),
                W    = $gal.width(), // 500

                N    = $box.length,  // 3
                C    = 0;            // a counter
                //var W=wid2;
                $sli.width(W*N);
                var wid1=$("#rightDiv").width();
                $('#prev, #next').click(function(){
                    C = (this.id=='next' ? ++C : --C) <0 ? N-1 : C%N;
                    $sli.stop().animate({left: -C*wid2 },800);
                });

                function filterFor(filType){
                    var tagShortDesc=  filtagSname;
                    var tagLongDesc= filtagLname;
                    var reportId= filRepId;
                        var tagType=filtagType;
//                    var tagType=filType;
                    var noData=0;
                    //    alert(tagType)
                    //    alert("filt "+tagShortDesc.length)
                    $("#d1").html("");
                    $("#d2").html("");
                    $("#d3").html("");

                    if(tagShortDesc.length==0){
                        $("#prev").css('display', 'none');
                        $("#next").css('display', 'none');
//                        alert("There is no data to display !!! Please select another option");
                        $("#d1").html("<h3 align='middle' style='font:19px verdana;margin-top:10px;'>There is no data to display !!! Please select another option</h3>");
                    }else{

                        if(tagShortDesc.length>12){
                            $("#prev").css('display', '');
                            $("#next").css('display', '');
                        }else{
                            $("#prev").css('display', 'none');
                            $("#next").css('display', 'none');
                        }
                        for(var i=0;i<tagShortDesc.length;i++){
                            if(filtagType[i]==filType || filType=='All'){
                                    noData=1;

                                var div = "";
                                //                                                    for(var i=0;i<46;i++){
                                if(tagType[i]=="R"){
                                       div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }else {
                                       div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i+1)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";
                                    div+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";

                                if(i>11 && i<=24){
                                    var div1="";
                                    if(tagType[i]=="R"){
                                       div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else {
                                       div1+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-11)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div1+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";

                                        div1+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";
                                    $("#d2").append(div1);
                                    //     $("#rightDiv2").append(div);

                                }else if(i>23)
                                {
                                    var div2="";
                                    if(tagType[i]=="R"){
                                       div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='ToglFiltr("+reportId[i]+",\""+tagShortDesc[i]+"\")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";

                                    } else if(tagType[i]=='O'){
                                      div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='openOneView("+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else if(tagType[i]=='D'){
                                      div+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"' onclick='openDashboard(\""+tagShortDesc[i]+"\","+reportId[i]+")' style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    } else {
                                       div2+="<div id='tile_"+tagType[i]+"_"+i+"' class='small"+(i-23)+"'  style=' margin: 1%;width: 23%;height: 150px; cursor: pointer;display: inline; float: left;'>\n\
                                <table><tr><td><div style='height:70px' >";
                                    }
                                     div2+="<a href='#' style='color:#336699' onclick=''><h1 align='center' style='margin-top:40px;color:white;font:"+tileFontSize+"px Arial, Helvetica, sans-serif' onclick='' title='"+tagShortDesc[i]+"'> "+tagShortDesc[i]+" </h1></a>";

                                        div2+="</div></td></tr>\n\
                                    <tr><td><div style='height:50px;border-top:1px dotted white' title='"+tagLongDesc[i]+"'>\n\
                                    <h4 align='center' style='color:white;font:"+subtileFontSize+"px Arial, Helvetica, sans-serif'> "+tagLongDesc[i]+"</h4>\n\
                            </div></td></tr></table></div>\n\
                            </div> ";
                                    $("#d3").append(div2);
                                }
                                else{
                                    //    var innerDiv="<div id='rightDiv1' class=' box-wrapper wrapper' style='display: table;clear:both;border-left:2px solid indigo ; background-color: grey;overflow:visible;height: 500px;width: 94%;margin-left: 3%;overflow: auto; '  role='main' ></div>"
                                    $("#d1").append(div);

                                }

                            }
                        }
                        if(noData==0){
                            $("#prev").css('display', 'none');
                            $("#next").css('display', 'none');
                            alert("No Data exist for the selected filter")
                        $("#d1").html("<h3 align='middle' style='font:19px verdana;margin-top:10px;'>There is no data for this filter !!!</h3>");
                    }
                    }
                }

function ToglFiltr(repId,repName){
   if($("#hidbtn").val()=="false"){
 
   if(hasTouch){
    window.location.href=('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open');
} else{
 window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
   }
//   window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
   
   
   
   
   }else{
         applyNewFilters(repId,repName)
   }
//}
//hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
}

function openDashboard(dashName,dashID){
//    hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
if(hasTouch){
    window.location.href=('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID='+dashID+'&pagename='+dashName+'&editDbrd=false');
}else{
window.open('<%=request.getContextPath()%>/dashboardViewer.do?reportBy=viewDashboard&REPORTID='+dashID+'&pagename='+dashName+'&editDbrd=false','_blank');
}}

function mediateFilterEnable(){

 if($("#hidbtn").val()=="false"){
$('#Tbtn').removeClass('tick').addClass('tick1');

$("#hidbtn").attr('value', 'true');
}else{
 $('#Tbtn').removeClass('tick1').addClass('tick');

 $("#hidbtn").attr('value', 'false');
}

}
   function goPaths(path){
                //                alert(path)
                parent.closeStart();
                document.forms.searchForm.action=path;
                document.forms.searchForm.submit();
            }
            //modified by dinanath
function valid(form){
                  var input=0;
                input=document.myform.data.value;
//alert("input"+input)
//                for(i=0;i<tagReportName.length;i++){
//                    if(tagReportName[i]===input){
//                        applyNewFilters(tagReportId[i],tagReportName[i])
//                        //            window.open('reportViewer.do?reportBy=viewReport&REPORTID='+tagReportId[i]+'&action=open','_blank');
//                        break;
//                    }
//                }
                               window.open('<%=request.getContextPath()%>/Search.jsp');
            }
//            function openOneView(oneViewIdValue){
//                //                 window.open('reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
////                alert("oneViewIdValue "+oneViewIdValue)
////                window.open('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=saveOneViewReg&reportBy=viewReport&action=open&oneViewIdValue='+oneViewIdValue,'_blank')
  //              window.open('<%=request.getContextPath()%>/newOneView.jsp?fromopen=true&fromviewer=true&action=open&oneviewname=ob&oneViewIdValue='+oneViewIdValue,'_blank')
//          viewOneBy("viewname",oneViewIdValue)
//       }
function openOneView(oneViewIdValue){
var oneviewname='';
$.ajax({
url: 'oneViewAction.do?templateParam2=getonename&oneViewIdValue='+oneViewIdValue,
success: function(data){
//alert("onviewNAm"+data)
oneviewname=data;
//hasTouch = /android|iphone|ipad/i.test(navigator.userAgent.toLowerCase());
if(hasTouch){
window.location.href=('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewIdValue+'&oneviewname='+oneviewname);
}
else{window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewIdValue+'&oneviewname='+oneviewname,'_blank');}}
});
//alert("oneviewname"+oneviewname)
// window.open('reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank');
// alert("oneViewIdValue "+oneViewIdValue)
//window.open('srchQueryAction.do?srchParam=oneViewBy&homeFlag=true&oneViewId='+oneViewIdValue+'&oneviewname='+oneviewname,'_blank')
}
            function changeFont(fontVal){
//        alert(fontVal)
        if(fontVal=='Small'){
            fontSize='12px verdana';
            fchange=1;
        }else if(fontVal=='Medium'){
             fontSize='15px verdana';
             fchange=1;
        }else{
             fontSize='17px verdana';
             fchange=1;
        }

        var fSize=fontSize.toString().replace("px verdana", "");
 $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateFontSizeChangeOfTag&fontSizeTag='+fSize,
                    function(data){
                   if(data==""){
                       
                            alert("Font has been changed successfuly");
                            }
                            else{
                                alert("Invalid entries");
                            }

                    });


         $("#svgContain").html('');
          tagIdS=[];
          tagNameS=[];
           checkPie(fontSize)
}
            </script>
            <!-- Added By Amar on 5th Dec 2014-->
            <div id="AddMoreFiltersDiv" Style="display:none;overflow-y: hidden" title="Add More Filters ">
                <iframe  id="addmoreFiltersFrame" name='addMoreFiltersFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
            </div>
            <!-- End of code by Amar on Dec 2014 -->
        </form>
            <script type="text/javascript">
        //added by Dinanath for maintaining sequence of Tag and description header
  var tId;
var tName;
var seqId;

 function changeSeq(){
    var htmlVar2="";
    tId=new Array();
    tName=new Array();
    seqId=new Array();
    for(var i=0;i<tagIdS.length;i++){
     htmlVar2+="<li id='"+tagIdS[i]+"' class='btn-custom2' style='background-color: #3CC; margin-bottom: 1px; height: 16px; font-size: 12px; padding: 3px;'>"+tagNameS[i]+"</li>";
    }
  $("#addDynamic").html(htmlVar2)
    $("#changeSequence").dialog('open');
            initDialog1();
    }
    function changeSeqOfRAssignment(){
        var htmlVar2="";

        for(var i=0;i<tagAssignIdForSeq.length;i++){
     htmlVar2+="<li id='"+tagAssignIdForSeq[i]+"' class='btn-custom2' style='background-color: #3CC; margin-bottom: 1px; height: 16px; font-size: 12px; padding: 3px;'>"+tagShortNameForSeq[i]+"</li>";
    }
    $("#addDynamicSecond").html(htmlVar2)
        $("#changeSequenceOfReportAssignment").dialog('open');
        initDialog1();
    }
   
    function initDialog1(){
            if ($.browser.msie == true){
                 $("#changeSequence").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify'
                        //modal: true
                    });
                 $("#changeSequenceOfReportAssignment").dialog({
                    autoOpen:false,
                    height: 400,
                    width: 300,
                    position: 'justify'
                 });
                }
                else{
                      $("#changeSequence").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#changeSequenceOfReportAssignment").dialog({
                    autoOpen:false,
                    height: 400,
                    width: 300,
                    position: 'justify'
                 });
    }
    $('ul.sortable-list').sortable();
    }
    function changeSequenceTab(){
        var itemStr = getItems('#wrapperSequence');
        var s=itemStr.toString().split(",");
        var tpid=[];
        var tpseqid=[];
        for(var i=0;i<s.length;i++){
            tpid.push(s[i]);
            tpseqid.push(i);
        }
        $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateAccordingSequence&tagIdforUpd='+tpid+'&tagSequenceIdForUpd='+tpseqid, $("#sequenceForTag").serialize(),
        function(data){
            alert("Sequences have been changed successfuly");
            if(data==""){
                $("#changeSequence").dialog('close');
                window.location.href=window.location.href;
            }
            else{
                alert("Invalid entries");
            }
        });
        }
        function getItems(container)
{
    var columns = [];

    $(container+ ' ul.column').each(function(){
        columns.push($(this).sortable('toArray').join(','));
    });
    return columns.join('|');
}
function changeSequenceReportAssignment(){
    var itemStr = getItemsSecond('#wrapperSequenceSecond');
    var s=itemStr.toString().split(",");
    var tagAssignIdS=[];
    var tagAssignSeqId=[];
    for(var i=0;i<s.length;i++){
        tagAssignIdS.push(s[i]);
        tagAssignSeqId.push(i);
    }
    $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=updateTagReportAssignmentSequence&tagAssignIdforUpd='+tagAssignIdS+'&tagAssginSequenceIdForUpd='+tagAssignSeqId, $("#sequenceForTagSecond").serialize(),
    function(data){
        alert("Sequences have been updated successfuly");
        if(data==""){
            $("#changeSequenceOfReportAssignment").dialog('close');
            window.location.href=window.location.href;
        }
        else{
            alert("Invalid entries");
        }
     });   
}
function getItemsSecond(container)
{
    var columns = [];
    $(container+ ' ul.columnSecond').each(function(){
        columns.push($(this).sortable('toArray').join(','));
    });
    return columns.join('|');
}

$(window).load(function (){   
checkPie('11px verdana');
});
 </script>

    <div id="changeSequence" style="display:none;" title="CHANGE SEQUENCE">
        <form action="javascript:void(0)" name="sequenceForTag" id="sequenceForTag" method="post" onsubmit="return changeSequenceTab()">
            <div id="wrapperSequence" class="setHeightScrollable"  >
                <ul class="sortable-list column" id="addDynamic" >

                </ul>
            </div >
            <div style="width:20%;margin-left: auto;margin-right: auto;margin-top:5%;">
<!--            <table >
                <tr>
                    <td colspan="2">
                        <br/>
                    </td>
                </tr>
                <tr>
                    <td width="60%">
                    </td>
                    <td  >-->
                        <input class="navtitle-hover btn-custom" style="width:auto" type="submit" value="Save" >
<!--                    </td>
                </tr>
            </table>-->
            </div>
        </form>
    </div>

       <div id="changeSequenceOfReportAssignment" style="display:none;" title="UPDATE BLOCK SEQUENCE">
        <form action="javascript:void(0)" name="sequenceForTagSecond" id="sequenceForTagSecond" method="post" onsubmit="return changeSequenceReportAssignment()">
            <div id="wrapperSequenceSecond" class="setHeightScrollable">
                <ul class="sortable-list columnSecond" id="addDynamicSecond">

                </ul>
            </div>
            <div class="secgh" style="width:20%;margin-left: auto;margin-right: auto;margin-top:5%;" >
                   <input class="navtitle-hover btn-custom" style="width:auto" type="submit" value="Save" >
            </div>
        </form>
        </div>

      
    </body>
</html>
