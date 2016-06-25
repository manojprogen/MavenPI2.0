<%-- 
    Document   : TestScript
    Created on : Nov 25, 2009, 11:20:08 AM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            function test(){
                var str="GrpCol100";
                 alert(str)
                str=str.replace("GrpCol","");
                alert(str)
            }
        </script>
    </head>
    <body onload="test();">
        <h1>Hello World!</h1>
    </body>
</html>
