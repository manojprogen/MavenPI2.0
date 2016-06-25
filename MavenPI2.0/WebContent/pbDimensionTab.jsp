<!doctype html>
<%@page import="prg.db.PbDb,prg.db.PbReturnObject" %>
<% String contextPath=request.getContextPath();%>
<html lang="en">
    <head>
         <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/dragTable.js"></script>
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
           <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>

        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
          <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <script type="text/javascript">
          //  var frameObj = document.getElementById('dimtab');
           // var source="getAllDimensions.do";
          //  frameObj.src=source;
          //  document.getElementById('dimtab').style.display='';

            var frameObj = document.getElementById('dimtab');
            var source="getAllDimensions.do?method";
            frameObj.src=source;
            document.getElementById('dimtab').style.display='';
           // document.myForm.submit();

        </script>
        <style>
         /*   .body{
                background-color:#e6e6e6;
            }*/
        </style>
    </head>
    
    <body class="body">
        <div>
            <div>
                <iframe  id="dimtab" NAME='dimtab' style="height:600px;width:100%;display:none;" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div>


    </body>
</html>
