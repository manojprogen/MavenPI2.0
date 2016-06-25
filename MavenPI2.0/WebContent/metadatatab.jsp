<!doctype html>
<html lang="en">
    <head>
        <script type="text/javascript">
            var frameObj = document.getElementById('metadatatab');
            var source="reportTemplateAction.do?templateParam=getAllReports";

            frameObj.src=source;
            document.getElementById('metadatatab').style.display='';
        </script>
         <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
    </head>
    <body>
        <div>
            <div>
                <iframe  id="metadatatab" NAME='metadatatab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div>
    </body>
</html>
