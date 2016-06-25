<!doctype html>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>
<% String contPath=request.getContextPath(); %>
<html lang="en">
     <script src="<%=contPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
           <script type="text/javascript" src="<%=contPath%>/javascript/ui.dialog.js"></script>

        <link rel="stylesheet" href="<%=contPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
          <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
    <head>

	<script type="text/javascript">
      
           var frameObj = document.getElementById('timesetuptab');
             var source="allTimeSetUps.do";
             frameObj.src=source;
            document.getElementById('timesetuptab').style.display='';
            var frameObj = document.getElementById('businessgrptab');
      
	</script>
    <style>
          /*  .body{
                background-color:#e6e6e6;
            }*/
        </style>

</head>

<body class="body">

	<div >
        <div>
          <iframe  id="timesetuptab" NAME='timesetuptab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
        </div>
    </div>


</body>
</html>

