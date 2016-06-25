<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html>
<head>
  <link type="text/css" href="<%=request.getContextPath()%>/jQuery/themes/base/ui.all.css" rel="stylesheet" />
  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.core.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.draggable.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.resizable.js"></script>
  <style type="text/css">
    #draggable { width: 100px; height: 70px; background: silver; }
  </style>
  <style>
      .txtsty{
          overflow: hidden;
            width : 100px;
            height : 100px;
            border : 1px solid #ffff99;
            background-color: #ffff99;
            white-space: pre;
      }
      </style>
  <script type="text/javascript">
  $(document).ready(function(){
    $("#draggable").draggable();
    $("#draggable").resizable({
			maxHeight: 200,
			maxWidth: 200,
			minHeight: 100,
			minWidth: 100
		});
  });
  $(function() {
		
	});
  </script>
</head>
<body style="font-size:62.5%;">

<div id="draggable">Drag me
<textarea cols="20" class="txtsty" id="txt"/>
</div>

</body>
</html>
