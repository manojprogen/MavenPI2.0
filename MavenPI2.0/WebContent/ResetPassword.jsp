<%-- 
    Document   : ResetPassword
    Created on : Mar 25, 2010, 11:00:43 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<% String contextPath= request.getContextPath();%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            *{font:11px verdana}
        </style>
       
    </head>
    <body>
        <%String userid = request.getParameter("userId");
        //////.println("user id in jsp is"+userid);%>
        <%--<form action="<%=request.getContextPath()%>/passwordAction.do?pswrdparam=resetPassword" method="post" onsubmit="return validatenewPassword()">--%>
        <form action="javascript:void(0)" method="post" onsubmit="return validatenewPassword()">
            <table align="center">
            <tr>
                <td>New Password</td>
                <td><input type="password" id="newpasswrod" name="newpasswrod"></td>
            </tr>
            <tr>
                <td>Re-Type Password</td>
                <td><input type="password" id="retypepswrd" name="retypepswrd"></td>
            </tr>
            <tr>                
                    <td colspan="2">
                        <br/>
                    </td>
                </tr>
                <tr>
                <td colspan="2" align="center">
                    <input class="navtitle-hover" style="width:auto" type="submit" value="Reset Password" >
                </td>
            </tr>
        </table>
            <input type="hidden" name="userid" id="userid" value="<%=userid%>">
            </form>
             <script type="text/javascript">
            function validatenewPassword(){
                var newpswrd = document.getElementById("newpasswrod").value;
                var retypepswrd = document.getElementById("retypepswrd").value;
                var userid = document.getElementById("userid").value;
                if(newpswrd=="" ||newpswrd.length==0){
                    alert("Please Enter New Password")
                    return false;
                }else if(newpswrd.length<4){
                alert("New Password Should Not Be Less Then Four Charechters");
                return false;
                }else if(retypepswrd=="" ||retypepswrd.length==0){
                    alert("Please Enter Re-Type Password")
                    return false;
                }else if(retypepswrd.length<4){
                alert("New Re-Type Password Should Not Be Less Then Four Charechters");
                return false;
                }else if(newpswrd != retypepswrd){
                    alert("New Password and Re-Type Password does not match");
                    return false;
                }else{
                    $.ajax({
                        url: "<%=request.getContextPath()%>/passwordAction.do?pswrdparam=resetPassword&newpasswrod="+newpswrd+"&userid="+userid,
                        success: function(data){
                            if(data=="1"){
                                alert("Password Reset Successfully");
                                parent.closeRestepswrd();
                            }else{
                                alert("Failed to Reset Password");
                                return false;
                }
                        }
                    });
                }
               // alert("newpswrd"+newpswrd);
               // alert("retypepswrd"+retypepswrd);                
            }
        </script>
    </body>
</html>
