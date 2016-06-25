<!doctype html>
<html lang="en">
    <%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
    %>
    <head>


        <script type="text/javascript">

            var frameObj = document.getElementById('businessgrptab');
            var source="getAllBusinessGroups.do";

            frameObj.src=source;
            document.getElementById('businessgrptab').style.display='';
        </script>

    </head>
    <body>

        <div >
            <div>
                <iframe  id="businessgrptab" NAME='businessgrptab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div





    </body>
</html>
