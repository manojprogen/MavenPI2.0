<!doctype html>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
%>
<html lang="en">
<head>


	<script type="text/javascript">


               var frameObj = document.getElementById('dimtab');

                var source="getAllDimensions.do";

                frameObj.src=source;
                alert(frameObj.src)
                document.getElementById('dimtab').style.display='';
                
	</script>

</head>
<body>

	<div >
        <div>
            <iframe  id="dimtab" NAME='dimtab' style="height:600px;width:100%;display:none;" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
        </div>
    </div





</body>
</html>
