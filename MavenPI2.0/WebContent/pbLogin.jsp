<%-- 
    Document   : pbLogin
    Created on : Oct 5, 2009, 5:50:25 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            function goLogin(){
                document.forms.loginForm.action="baseAction.do?param=loginApplication";
               // alert(document.forms.loginForm.action)
                document.forms.loginForm.submit();
            }
        </script>
    </head>
    <body>
        <h1>Login Page</h1>
        <form name="loginForm" action="" method="post">
            <a href="javascript:void(0)" onclick="javascript:goLogin()">click here to login </a>
        </form>
    </body>
</html>
