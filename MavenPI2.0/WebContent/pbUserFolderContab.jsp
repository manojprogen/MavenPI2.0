<%--
    Document   : userfld
    Created on : Sep 12, 2009, 4:56:52 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            var frameObj = document.getElementById('userfoldertab');
            var source="userLayerAction.do?userParam=getUserFolderList2";
            frameObj.src=source;
            document.getElementById('userfoldertab').style.display='';
        </script>
        <style>
          /*  .body{
                background-color:#e6e6e6;
            }*/
        </style>
    </head>
    <body class="body">
            <div style="height:auto">
                 <iframe  id="userfoldertab" NAME='userfoldertab' style="width:100%;display:none;height:610px"  SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>

    </body>
</html>
