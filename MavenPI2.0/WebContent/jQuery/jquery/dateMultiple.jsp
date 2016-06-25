<!doctype html>
<html lang="en">
<head>
	<title>jQuery UI Datepicker - Default functionality</title>
    <link type="text/css" href="<%=request.getContextPath()%>/jQuery/themes/base/ui.all.css" rel="stylesheet" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.core.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/ui/ui.datepicker.js"></script>
	<link type="text/css" href="<%=request.getContextPath()%>/jQuery/demos.css" rel="stylesheet" />
	<script type="text/javascript">
	$(function() {
		$("#datepicker").datepicker({ 
            numberOfMonths: 1,
	    showButtonPanel: true
    });
	});

	</script>
</head>
<body>

<div class="demo">

<p>Date: <input type="text" id="datepicker"></p>

</div><!-- End demo -->



</body>
</html>
