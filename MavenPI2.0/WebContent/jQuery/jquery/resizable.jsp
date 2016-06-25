<!doctype html>
<html lang="en">
<head>
	<title>jQuery UI Resizable - Maximum / minimum size</title>
    <link type="text/css" href="<%=request.getContextPath()%>/jQuery/themes/base/ui.all.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.core.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.resizable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery//ui/ui.draggable.js"></script>

	<link type="text/css" href="../demos.css" rel="stylesheet" />
	<style type="text/css">
	#resizable { width: 200px; height: 150px; padding: 5px; }
	#resizable h3 { text-align: center; margin: 0; }
	</style>
	<script type="text/javascript">
	$(function() {
		$("#resizable").resizable({
			maxHeight: 250,
			maxWidth: 350,
			minHeight: 100,
			minWidth: 150
		});
	});
    $(function(){
        $("#Table1").draggable()
    });
	</script>
</head>
<body>
<div class="demo">

<div id="resizable" class="ui-widget-content">
    <table id="Table1">
        <tr>
      <h3 class="ui-widget-header">Resize larger / smaller</h3>
      </tr>
    </table>
</div>

</div><!-- End demo -->


</body>
</html>
