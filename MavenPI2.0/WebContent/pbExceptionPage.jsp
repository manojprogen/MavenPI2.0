<%--
    Document   : pbSessionExpired
    Created on : Sep 30, 2009, 12:03:00 PM
    Author     : santhosh.kumar@progenbusiness.com
<%--
    Document   : RestaurantRegistration
    Created on : Oct 29, 2009, 10:32:08 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi EE</title>
        <link rel="stylesheet" href="ui.theme.css" />

        <style>
            .white_content {
                position: absolute;
                top: 15%;
                left: 37%;
                width:500px;
                height:500px;
                padding: 5px;
                z-index:1002;
                -moz-opacity: 0.8;
                opacity:.50;
                filter:alpha(opacity=60);
            }
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .black_overlay{
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.8;
                opacity:.50;
                filter:alpha(opacity=60);
                overflow:auto;
            }
            .upperTab{
                position: absolute;
                top: 1%;
                left: 0%;
            }
            .bottomTab{
                position: absolute;
                top: 92%;
                left: 0%;
            }
        </style>
        <script type="text/javascript">
              function logout(){
                document.forms.loginForm.action="baseAction.do?param=logoutApplication";
                document.forms.loginForm.submit();
            }

        </script>
    </head>

    <body>
         <form name="loginForm" action="" method="post">
            <table style="width:100%">
                <tr>
                    <td valign="top" style="width:100%;">
                        <jsp:include page="Headerfolder/headerPage.jsp"/>
                    </td>
                </tr>
            </table>
                <table class="bottomTab fontsty" width="100%">
                    <tr class="fontsty" style="height:10px;width:100%;max-height:100%;background-color:#bdbdbd">
                        <td style="height:10px;width:100%;background-color:#bdbdbd" >
                            <center style="background-color:#bdbdbd"><font  style="color:#fff;font-family:verdana;font-size:10px;font-weight:normal" align="center">Copyright © 2009-12 <a href="http://www.progenbusiness.com" style="color:red;font-weight:bold;font-size:10px;font-family:verdana">Progen Business Solutions.</a> All Rights Reserved</font></center>
                        </td>
                    </tr>
                </table>

            <div class="white_content">
                <table width="100%" >
                    <tr style="width:100%">
                        <td align="right">
                           <%-- <a href="#" onclick="gohome()"style="font-family:verdana;color:#000;text-decoration:none;">
                                <font style="font-family:verdana;font-size:14px;font-weight:bold">Home</font></a> | --%>
                                <a href="#" onclick="logout()"style="font-family:verdana;color:#000;text-decoration:none;">
                                <font style="font-family:verdana;font-size:14px;font-weight:bold">Login</font></a>
                        </td>
                    </tr>
                </table>
                <div style="height:100px"></div>
                <table width="100%" align="center">
                    <tr style="width:100%">
                        <td>
                            <font style="font-family:verdana;font-size:20px;color:red">Exception Occured .....</font>
                        </td>
                    </tr>
                 <%--   <tr><td>


            <a href="javascript:void(0)" onclick="javascript:goLogin()">click here to login </a>

                    </td></tr>--%>
                </table>
            </div>
            <div id="fade" class="black_overlay"></div>
        </form>
    </body>


</html>



