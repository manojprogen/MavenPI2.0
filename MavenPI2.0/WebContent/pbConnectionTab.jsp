<!doctype html>
<html lang="en">
    <head>
        <script type="text/javascript">
            var frameObj = document.getElementById('databasetab');
            var source="getAllTables.do";            
            frameObj.src=source;
            document.getElementById('databasetab').style.display='';

        </script>

    </head>
    <body>
            <div style="height:100%">
                <iframe  id="databasetab" NAME='databasetab' style="height:610px;width:100%;display:none;" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
    </body>
</html>
