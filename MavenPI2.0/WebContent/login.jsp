

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="utils.db.*"%>
<Html>
<Head>

    <title>Login :: Progen Business Solutions</title>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

    <link href="css/myStyles.css" rel="stylesheet" type="text/css">
    
    <Style>

                                .mytableborderframe2
                                {
                                    width:"400px";
                                    height:"300px";
                                    border-top: 1px solid #999999;
                                    /*apply this class to a ListServicesTable only*/
                                }
                                .myMainPageFrame
                                {
                                    border-top: 1px solid #FFFFFF;
                                    border-right: 1px solid #999999;
                                    border-bottom: 1px solid #FFFFFF;
                                    border-left: 1px solid #FFFFFF;
                                    /*apply this class to a ListServicesTable only*/
                                }
                                .myMainPageTable
                                {
                                    border-top: 1px solid #999999;
                                    border-right: 1px solid #ffffff;
                                    border-bottom: 1px solid #FFFFFF;
                                    border-left: 1px solid #FFFFFF;
                                    /*apply this class to a ListServicesTable only*/
                                }

                                .myMainPagelogin
                                {
                                    width:400px ;
                                    height:300px;

                                    /*apply this class to a ListServicesTable only*/
                                }

    </Style>
    </style>


</Head>
<Body onload="document.myForm.user.focus();">
    <%



    %>
    <%
        String lMessage = null;
        String lUser = request.getParameter("userId");
        String active = request.getParameter("active");
        if (lUser != null) {
            lMessage = "Login Failed... Username/Password is incorrect...";
        }
        if (active != null) {
            lMessage = "User is not Active";
        }

    %>
    <form name="myForm" onLoad="setFocus()" onSubmit='return doLogin("<%=request.getContextPath()%>")' method="POST" >
        <!--<div id="container" style="margin-top:150px; ">-->


        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <table  border="0" cellspacing="0" cellpadding="0" align="center">
            <%
        if (lMessage != null) {
            %>
            <Tr>
                <Td>
                    <Font color="red"><%=lMessage%></Font>
                </TD>
            </TR>
            <%
        }
            %>
            <tr>
                <td >
                    <div   class="myMainPagelogin">

                        <table>
                            <tr><td>Username</td><td><input type="text" name="user" id="user" class="myTextbox2" value="" ><br/></td></tr>
                            <tr><td>Password</td><td><input type="password" name="password" type="password" class="myTextbox2" value=""><br/></td></tr>
                            <tr><td align="center">&nbsp;
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td>&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="Submit" value="Login" class="btn" ><br/><br/></td></tr>
                        </table>

                    </div>
                </td>
            </tr>
        </table>
        <!--</div>-->
    </form>
    <%   %>
    <Script>
        function doLogin(conPath)
        {
            var succFlag = true;
            if(document.myForm.user.value == null || document.myForm.user.value == '')
            {
                alert("Please enter username");
                document.myForm.user.focus();
                succFlag = false;
            }
            else if(document.myForm.password.value == null || document.myForm.password.value == '')
            {
                alert("Please enter password");
                document.myForm.password.focus();
                succFlag = false;
            }

            if(succFlag)
            {
                document.myForm.action=conPath + "/Login";
                return true;
            }
            return false;
        }
        function setFocus()
        {
            document.myForm.user.focus();
        }
    </Script>
</body>