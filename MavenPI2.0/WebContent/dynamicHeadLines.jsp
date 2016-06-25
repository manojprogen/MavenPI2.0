<%-- 
    Document   : dynamicHeadLines
    Created on : 18 Dec, 2012, 6:34:24 PM
    Author     : progen
--%>

<%--
    Document   : headLines
    Created on : 19 Jul, 2011, 3:58:03 PM
    Author     : progen
--%>


<%@page contentType="text/html" pageEncoding="UTF-8" import="java.sql.Clob,java.io.Reader,com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

            Locale locale = null;
            locale = (Locale) session.getAttribute("userLocale");
             String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
String contextPath=request.getContextPath();

%>
<html>
     <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
       <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>-->
      <script type="text/javascript" src="<%=contextPath%>/TableDisplay/overlib.js"></script>
      <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
    <head>
        <style type="text/css">

            .headlinestyle {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: bold;
                line-height: normal;


            }
        </style>

<style type="text/css" >
            .ui-progressbar-value { background-image: url(images/barchart.gif); }
            .myHead
            {
                font-family: Verdana;
                font-size: 8pt;
                font-weight: bold;
                color: #000;
                padding-left:12px;
                width:20%;
                background-color:#b4d9ee;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }

            .myAjaxTable {
                table-layout:fixed;
                background-color: #FFFFFF;
                text-align:left;
                border: 0px solid #000000;
                font-size:10px;
                left:4px;
                height:auto;
                border-collapse:separate;
                border-spacing:5px;
            }
            .ajaxboxstyle {
                background-color:#FFFFFF;
                border: 0.1em solid #0000FF;
                height:50px;
                margin:0 0.5em;
                overflow-x:hidden;
                overflow-y:auto;
                position:absolute;
                text-align:left;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#f0f0f0;
                width:450px;
            }
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 110%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                overflow:auto;
            }

            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
            }

            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
            * {
         font: 12px verdana;
         font-weight: bold;
            }
        </style>
        <script type="text/javascript">
            var reportHeadlineId;
            var flag=false;
            $(document).ready(function(){
             $("#selectHeadlineDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 500,
            position: 'justify',
            modal: true,
            title:'Select Headlines'
        });
          $("#emailDiv").dialog({
            //bgiframe: true,
            autoOpen: false,
            height: 400,
            width: 500,
            position: 'justify',
            modal: true,
            title:'Send Emails'
        });

        $("#headlinesDialog").dialog({
                autoOpen: false,
                height: 450,
                width: 800,
                position: 'justify',
                modal: true,
                resizable:false
            });
             $("#headlinesMailList").dialog({
                autoOpen: false,
                height: 450,
                width: 800,
                position: 'justify',
                modal: true,
                resizable:false
            });
            $("#shareHeadline").dialog({
                autoOpen: false,
                height: 300,
                width: 400,
                position: 'justify',
                modal: true,
                resizable:false
            });

                $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDynamicHeadlines',$("#dynamicheadLinesForm").serialize(),
            function(data){
                $("#dynamicHeadlineDiv").html(data);
                 initCollapser("");
            });

            });

           

        </script>
    </head>
    <body>
        <form name="dynamicheadLinesForm" id="dynamicheadLinesForm" action="">
            <br>
            <div id="dynamicHeadlineDiv"></div>
        </form>
       <form name="frmParameter" id="" method="POST" action=""></form>
       <script type="text/javascript">
        function getHeadlineData(id,headlinename)
            {

            var ids=id.split(",");
            var headlineId=ids[0];
            var reportId=ids[1];
            var headlineName=headlinename;
             var currRow = $("#"+ids[0]);
           $( "#"+headlineId+"prgBar").progressbar({value: 37});
            $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDynamicHeadlineData&headlineId='+headlineId+"&reportId="+reportId+"&headlineName="+headlineName,$("#dynamicheadLinesForm").serialize(),
            function(data){
             //   alert(data)
//                data=data.replace('progenTable','tablesorter');
                  $( "#"+headlineId+"prgBar").remove();
                        $("#"+headlineId+"div").html(data);
                                    currRow.attr("initialized","true");
            });



            }
             function initCollapser(divId){
                if (divId == ""){
                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }

            function editHeadlines()
            {
                 $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getDynamicHeadlines',$("#dynamicheadLinesForm").serialize(),
            function(data){
                $("#dynamicHeadlineDiv").html(data);
                 initCollapser("");
            });
            }

            function deleteHeadline(id)
            {
                var flag=confirm("Do you want to delete Headline?");
                if(flag) 
                   $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=deleteSchedulerDetails&schedulerId='+id,$("#dynamicheadLinesForm").serialize(),
            function(data){
                editHeadlines();
            });
            }
            function openReport(headlineId,reportId,url){
           $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=checkReportAccess&headlineId='+headlineId+'&reportId='+reportId,
            function(data){
                if(data=='valid'){
                    var path='<%=request.getContextPath()%>/'+url+'&isSnapShot=true&SnapShotId='+headlineId;
                    submiturls(path);
                }

            });

            }
             function submiturls(url){
                document.frmParameter.action = url;
                document.frmParameter.submit();
            }
</script>
    </body>
</html>
