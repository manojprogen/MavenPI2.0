<%--
    Document   : prgSearch
    Created on : Dec 29, 2014, 4:53:22 PM
    Author     : Manik
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="utils.db.*,com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.PbDb,java.util.*,prg.db.Session,prg.db.Session,prg.db.PbReturnObject,java.sql.*"%>
<% String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/JS/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/JS/jquery-ui.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/JS/d3.v3.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/JS/jquery.pagination.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js" ></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/css/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="css/menuTab.css" />
        <link rel="stylesheet" type="text/css" href="css/styles_pagination.css" />
        <title>Search</title>
        <%
                    String userid = String.valueOf(session.getAttribute("USERID"));
                    String searchVal=request.getParameter("data");
            %>
            <style type="text/css">
                 .ui-autocomplete {
                position: absolute;
                max-height: 228px;
                max-width: 380px;
                overflow-y: auto;
                /* prevent horizontal scrollbar */
                overflow-x: hidden;
                /* add padding to account for vertical scrollbar */
                padding-right: 10px;
                padding-left: 2px;
                padding-top: 3px;
                padding-bottom: 5px;
            }
                .container1{
                width: 96%;
                margin-left: 2%;
            }
            </style>
           
            <style>
/*            #search1-text-input{
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
            }*/
        </style>
        <style type="text/css">

            #searchbox1
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
                    margin-left: 25%;
            }
            #search1,
            #submit1 {
                float: left;
            }

            #search1 {
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

            #submit1
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

            #submit1:hover {
                /*    background-color: #95d788;*/
                background-color: #3399CC;
                /*    background-image: linear-gradient(#6cbb6b, #95d788);*/
            }

            #submit1:active {
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

            #search1:-moz-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }

            #search1:-ms-placeholder {
                color: #9c9c9c;
                font-style: italic;
            }
            #search1.placeholder {
                color: #9c9c9c !important;
                font-style: italic;
            }
        </style>
    </head>
    <body>
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
                     <div class="container1" style="background-color: #D4E8EC;height: 24px;">
                        <form id="searchbox1" action="" onsubmit=" return checkInputValue1()" name="myform1" style="float: left">
                            <input id="search1"  type="text" name="data" value="<%=searchVal%>" placeholder="Please enter the Search criteria  eg: Sales Report." autocomplete="off" >
<!--                              <input id="search1" type="text" name="data"  placeholder="Please enter the Search criteria  eg: Enrollment,Schools" autocomplete="off" >-->
<!--                              <input id="submit1" type="button" value="Search"  >-->
                              <input id="submit1" type="button" value="Search"  onClick="SearchWithin(this.form)">
                        </form>
                     </div>
                </tr>
                <tr>
<!--                <div id="searchDiv" class="container1" style="background-color: lightgray;height: 525px;overflow: auto">-->
<!--                <div id="searchDiv" class="container1" style="background-color: darkcyan;height: 525px;overflow: auto">-->
                <div id="searchDiv" class=" " style="background-color: white;height: 550px;overflow: auto;margin-left: 2%;width: 55%;float: left">
<!--                    <h4>Showing Results</h4>-->
                </div>
                <div style="width: 41%;height: 550px;float: left;">
          <!--          <img alt="" style="opacity: 0.4;" border="0px"  width="100%" height="530px"  src="<%=request.getContextPath()%>/images/collageoimage.jpg"/>-->
<!--                <img alt="" border="0px"  width="400px" height="300px"  src="<%=request.getContextPath()%>/images/school1.jpg"/>
                <img alt="" border="0px"  width="400px" height="300px"  src="<%=request.getContextPath()%>/images/school2.jpg"/>-->
                </div>
                </tr>
            </table>
       </form>
                              <script type="text/javascript">
                                function  OpenReportNwTab(repId){
                                    window.open('<%=request.getContextPath()%>/reportViewer.do?reportBy=viewReport&REPORTID='+repId+'&action=open','_blank')
                                }
                              </script>
                                 <script type="text/javascript">
               var searchtextname = [];
            var searchtextid=[];
            var searchdesc=[];

            var tagReportName=new Array();
            var tagReportId=new Array();
            var tagShortDesc=new Array();
            var tagLongDesc=new Array();
            var reportId=new Array();
            var tagId=new Array();

               $(document).ready(function(){
//                    var sVal="<%=searchVal%>";
//                    input = document.getElementById("search1").value;

                var sVal=document.myform1.data.value;
                    var RplacedValue=sVal.toUpperCase().replace(/%/g, '9prg3');
//                    alert("RplacedValue  "+RplacedValue)
                     $.ajax({
                    url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getSearchReports&ValSearchh='+RplacedValue,
                    success: function(data) {
                        var jsonVar=eval('('+data+')');
                        var tagReportId1=jsonVar.tagReportId
                        var reportName1=jsonVar.reportName
                        var tagLongDesc1=jsonVar.tagLongDesc
                        for(var i=0;i<reportName1.length;i++)
                        {
                            tagReportId.push(tagReportId1[i]);
                            tagReportName.push(reportName1[i]);
                            tagLongDesc.push(tagLongDesc1[i]);
                        }
                        if(tagReportName.length<10 && tagReportName.length>0){
                         $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 1-"+tagReportName.length+" out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }else if(tagReportName.length==0){
                         $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 0 out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }else{
                             $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 1-10 out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }
                        var Ulist='<ul id="listReports" class="pagination2" style="margin-left:2%;margin-top:2%;color:blue">';
                        for(i=0;i<tagReportName.length;i++)
                        {
                            searchtextid[i]=tagReportId[i];
                            searchtextname[i] = tagReportName[i];
                            Ulist+="<li style='padding-top:5px;' ><dt  onclick='OpenReportNwTab("+tagReportId[i]+")'><a href='#' style=' color:blue;font: normal 17px trebuchet MS, arial, helvetica'>"+searchtextname[i]+"</a>"
                            +"</dt><dd style='margin-left:5%;'>"+tagLongDesc[i]+"</dd></li>";
                        }
                          Ulist+="</ul>";
                          $("#searchDiv").append(Ulist);
if(tagReportName.length<10){
$("#listReports").quickPagination({pagerLocation:"both",pageSize:(tagReportName.length)});
}else{
$("#listReports").quickPagination({pagerLocation:"both",pageSize:"10"});
}
$( "#search1" ).autocomplete({
                            source: searchtextname,
                            minLength:1
                        });
                    }
                });
               });

           function SearchWithin(myform2){
               var input=0;
               var input1=0;
                input=document.myform1.data.value;
//                 input = document.getElementById("search1").value;

                $("#searchDiv").html('');
//                alert(input)
                if(input==null || input==''){
                    alert("Your search is Blank");
                }else{

                
                    var RplacedValue=input.toUpperCase().replace(/%/g, '9prg3');
                     var tagReportName=new Array();
            var tagReportId=new Array();
            var tagLongDesc=new Array();
            var searchtextname = [];
             var searchtextid=[];
                 $.ajax({
                    url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=getSearchReports&ValSearchh='+RplacedValue,
                    success: function(data) {
                      //  alert(data)
                        var jsonVar=eval('('+data+')');
                        var tagReportId1=jsonVar.tagReportId
                        var reportName1=jsonVar.reportName
                        var tagLongDesc1=jsonVar.tagLongDesc
                        for(var i=0;i<reportName1.length;i++)
                        {
                            tagReportId.push(tagReportId1[i]);
                            tagReportName.push(reportName1[i]);
                            tagLongDesc.push(tagLongDesc1[i]);
                        }
//                        alert(tagReportName.length)
                        if(tagReportName.length<10 && tagReportName.length>0){
                         $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 1-"+tagReportName.length+" out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }else if(tagReportName.length==0){
                         $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 0 out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }else{
                             $("#searchDiv").append("<h4 style='font: normal 18px trebuchet MS, arial, helvetica''>Showing 1-10 out of "+tagReportName.length+" Results</h4><br>\n\n");
                        }
                        var Ulist='<ul id="listReports" class="pagination2" style="margin-left:2%;margin-top:2%;color:blue">';
                        for(i=0;i<tagReportName.length;i++)
                        {
                            searchtextid[i]=tagReportId[i];
                            searchtextname[i] = tagReportName[i];
                            Ulist+="<li style='padding-top:5px;'><dt  onclick='OpenReportNwTab("+tagReportId[i]+")'><a href='#' style=' color:blue;font: normal 17px trebuchet MS, arial, helvetica'>"+searchtextname[i]+"</a>"
                            +"</dt><dd style='margin-left:5%;'>"+tagLongDesc[i]+"</dd></li>";
                        }
                          Ulist+="</ul>";

                          if(tagReportName.length>0){
                          $("#searchDiv").append(Ulist);}
            if(tagReportName.length<10){
                $("#listReports").quickPagination({pagerLocation:"both",pageSize:(tagReportName.length)});
                    }else{
                        $("#listReports").quickPagination({pagerLocation:"both",pageSize:"10"});
                        }
                    $( "#search1" ).autocomplete({
                            source: searchtextname,
                            minLength:1
                        });
                    }
                });}
           }
           function checkInputValue1() {
    var x = document.getElementById("search1").value;
      if (x == null || x == "") {
        alert("Your search is Blank");
        return false;
    }
}
            </script>
    </body>
</html>
