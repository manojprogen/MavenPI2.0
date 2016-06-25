

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

      

        <style>
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body onload="focusOnFirstField()">
        <%
            String oldUserId = "";
            String allUserNames = "";
            if (request.getParameter("checkUser") != null) {
                oldUserId = request.getParameter("checkUser");
            }
            if (request.getParameter("allUserNames") != null) {
                allUserNames = request.getParameter("allUserNames");
            }
            ////////////////////////////////////////////////.println.println("oldUserId is:: " + oldUserId);
            ////////////////////////////////////////////////.println.println("allUserNames is:: " + allUserNames);



        %>
        <form name="myForm" method="post">
            <center>
                <font> Fields marked <span style="color:red">*</span> are MANDATORY </font>
                <br><br>
                <table>
                    <tr>
                        <td>New Username</td>
                        <td>
                            <input type="text" name="newUserName" id="newUserName">
                        </td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td>
                            <input type="password" name="newUserPassword" id="newUserPassword">
                        </td>
                    </tr>
                    <tr>
                        <td>Confirm Password</td>
                        <td>
                            <input type="password" name="newUserConfirmPassword" id="newUserConfirmPassword">
                        </td>
                    </tr>
                </table>
                <br>
                <table>
                    <tr>
                        <td>
                            <input class="navtitle-hover" type="button" value="Save" onclick="validateNewUser('<%=oldUserId%>','<%=allUserNames%>')">
                        </td>
                    </tr>
                </table>
            </center>
        </form>
                          <script type="text/javascript">
            var enterPassword;
            function checkNewUserName(allUserNames)
            {
                var newUserName = document.getElementById("newUserName").value;
                var temp = "";
                var allNames = allUserNames.split(",");
                if(newUserName=="" || newUserName==null)
                {
                    alert("Please Enter New Username");
                    document.getElementById("newUserName").focus();
                    return false;
                }
                else
                {
                    if(newUserName.length > 10) {
                        alert("New Username should not exceed 10 characters.");
                        document.getElementById("newUserName").focus();
                        return false;
                    }
                    for(var j=0;j<allNames.length;j++)
                    {
                        temp = newUserName.toUpperCase();
                        if(allNames[j]==temp)
                        {
                            alert("Username already exists");
                            document.getElementById("newUserName").focus();
                            return false;
                        }                        
                    }
                    return true;
                }
            }
            function checkNewUserPassword()
            {
                var newUserPassword = document.getElementById("newUserPassword").value;
                enterPassword = newUserPassword;
                if(newUserPassword=="" || newUserPassword==null)
                {
                    alert("Please Enter Password");
                    document.getElementById("newUserPassword").focus();
                    return false;
                }
                else
                {
                    return true;
                }
            }
            function checkNewUserConfirmPassword()
            {
                var newUserConfirmPassword = document.getElementById("newUserConfirmPassword").value;
                if(newUserConfirmPassword=="" || newUserConfirmPassword==null)
                {
                    alert("Please Confirm Password");
                    document.getElementById("newUserConfirmPassword").focus();
                    return false;
                }
                else if(enterPassword != newUserConfirmPassword) {
                    alert("Passwords dont match");
                    document.getElementById("newUserConfirmPassword").focus();
                    return false;
                }
                else
                {
                    return true;
                }
            }
            function validateNewUser(oldUserId,allUserNames) {
                if(checkNewUserName(allUserNames) && checkNewUserPassword() && checkNewUserConfirmPassword())
                {
                    var newUserName = document.getElementById("newUserName").value;
                    var newUserPassword = document.getElementById("newUserPassword").value
                    parent.saveNewUser(oldUserId,newUserName,newUserPassword);
                    return true;
                }
                else
                {
                    return false;
                }                
            }
            function focusOnFirstField() {
                document.getElementById("newUserName").focus();
            }
        </script>
    </body>
</html>
