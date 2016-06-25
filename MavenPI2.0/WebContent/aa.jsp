<!doctype html>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html lang="en">
<head>
	<title>jQuery UI Dialog - Modal form</title>
	 <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
	<script type="text/javascript" src="javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="javascript/ui.core.js"></script>
	<script type="text/javascript" src="javascript/ui.draggable.js"></script>
	<script type="text/javascript" src="javascript/ui.resizable.js"></script>
	<script type="text/javascript" src="javascript/ui.dialog.js"></script>
	<script type="text/javascript" src="javascript/effects.core.js"></script>
	<script type="text/javascript" src="javascript/effects.highlight.js"></script>
	<script type="text/javascript" src="javascript/jquery.bgiframe.js"></script>
	<link type="text/css" href="stylesheets/demos_1.css" rel="stylesheet" />
	<style type="text/css">
		body { font-size: 62.5%; }
		label, input { display:block; }
		input.text { margin-bottom:12px; width:95%; padding: .4em; }
		fieldset { padding:0; border:0; margin-top:25px; }
		h1 { font-size: 1.2em; margin: .6em 0; }
		div#users-contain {  width: 350px; margin: 20px 0; }
		div#users-contain table { margin: 1em 0; border-collapse: collapse; width: 100%; }
		div#users-contain table td, div#users-contain table th { border: 1px solid #eee; padding: .6em 10px; text-align: left; }
		.ui-button { outline: 0; margin:0; padding: .4em 1em .5em; text-decoration:none;  !important; cursor:pointer; position: relative; text-align: center; }
		.ui-dialog .ui-state-highlight, .ui-dialog .ui-state-error { padding: .3em;  }


	</style>
	<script type="text/javascript">
	

		$("#dialog").dialog({
			bgiframe: true,
			autoOpen: false,
			height: 300,
			modal: true,
			buttons: {
				'Create an account': function() {
					var bValid = true;
					allFields.removeClass('ui-state-error');

					

					if (!bValid) {
						
						$(this).dialog('close');
					}
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			},
			close: function() {
				allFields.val('').removeClass('ui-state-error');
			}
		});



		$('#create-user').click(function() {
			$('#dialog').dialog('open');
		})
		
	</script>
</head>
<body>

<div class="demo">

<div id="dialog" title="Create new user">
	All form fields are required.

	<form>
	
	</form>
</div>





<button id="create-user" class="ui-button ui-state-default ui-corner-all">Create new user</button>

</div><!-- End demo -->



</body>
</html>
