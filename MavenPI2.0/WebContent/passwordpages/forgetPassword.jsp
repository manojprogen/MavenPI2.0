<%--
    Document   : forgetPassword
    Created on : Jan 13, 2010, 3:54:58 PM
    Author     : Administrator
--%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ForgetPassword Page</title>
        <script type="text/javascript">





            function cancelfrgetpswrd()
            {
                document.forms.frgetpswrdfrm.action="baseAction.do?param=loginPage";
                document.forms.frgetpswrdfrm.submit();
            }

            function echeck(str)
            {

                var at = "@"
                var dot = "."
                var lat = str.indexOf(at)
                var lstr = str.length
                var ldot = str.indexOf(dot)
                if (str.indexOf(at) == - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at) == - 1 || str.indexOf(at) == 0 || str.indexOf(at) == lstr)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot) == - 1 || str.indexOf(dot) == 0 || str.indexOf(dot) == lstr)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at, (lat + 1)) != - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.substring(lat - 1, lat) == dot || str.substring(lat + 1, lat + 2) == dot)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot, (lat + 2)) == - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(" ") != - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }
                return true;
            }
            function validatefrgtpswrd()
            {
                var emailID = document.getElementById("usermailid").value;
            alert(emailID.value);
                    var usrname = document.getElementById("username").value;

                    if(usrname.length==0 || usrname=="")
                    {
                        alert("please Enter UserName");
                        return false;
                    }
                    if(usrname.length<4)
                    {
                        alert("Invalid User Name");
                        return false;
                    }
                    if ((emailID == null) || (emailID == ""))
                    {
                        alert("Please Enter your Email ID")
                        emailID.focus()
                        return false
                    }
                    //alert(echeck(emailID));
                    if (echeck(emailID) == false)
                    {
                        //alert("if false");
                        emailID.value = ""
            emailID.focus();
                        return false;
                    }
            return false;
            if(emailID.length==0)
                {
                    return false;
                }
                    }
        </script>
    </head>
    <body bgcolor="#C0C0C0">
        <center>
            <br/>
            <form action="<%=request.getContextPath()%>/passwordAction.do?pswrdparam=getpswrd" method="post" name="frgetpswrdfrm">
                <table>
                    <tr>
                        <td>
                            <label>E-mailid:-</label>
                        </td>
                        <td>
                            <input type="text" name="useremailid" id="usermailid">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>User Name:-</label>
                        </td>
                        <td>
                            <input type="text" name="username" id="username">
                        </td>
                    </tr>
                    <tr align="center">
                        <td align="left">
                            <input type="submit" value="GetPassword" onclick="return validatefrgtpswrd()">
                        </td>
                        <td >
                            <input type="button" value="Cancel" onclick="cancelfrgetpswrd()">
                        </td>
                    </tr>
                </table>
                <%
            if (request.getAttribute("failuremsge") != null) {
                out.println("<font color=red><strong>" + request.getAttribute("failuremsge") + "</font></strong>");
            }
            if (request.getAttribute("succesmsg") != null) {
                out.println("<font color=#336699><strong>" + request.getAttribute("succesmsg") + "</font></strong>");
            }
                %>
            </form>
        </center>
    </body>
</html>

--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
boolean isCompanyValid=false;
%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title><bean:message key="ProGen.Title"/></title>

        <Script language="javascript"  src="<%=request.getContextPath()%>/javascript/scripts.js"></Script>
        <script language="javascript" src="<%=request.getContextPath()%>/javascript/ajaxscript.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>


        <style type="text/css">

            .leftcol {
                clear:left;
                float:left;
                width:100%;
                background-color:#e6e6e6;
            }
            .header {
                position: absolute;
                top: 28%;
                left: 46%;
                width:auto;
                height:auto;
                padding: 5px;
                z-index:1002;
                -moz-opacity: 0.8;
                opacity:.50;
                filter:alpha(opacity=60);
            }
            .white_content {
                position: absolute;
                top: 33%;
                left: 35%;
                width:420px;
                height:auto;
                padding: 5px;
                border: 10px solid silver;
                background-color:white;
                z-index:1002;
                -moz-border-radius-bottomleft:6px;
                -moz-border-radius-bottomright:6px;
                -moz-border-radius-topleft:6px;
                -moz-border-radius-topright:6px;
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

            .myButton {
                -moz-background-clip:border;
                -moz-background-inline-policy:continuous;
                -moz-background-origin:padding;
                background:#BDBDBD url(images/ui-bg_glass_75_bdbdbd_1x400.png) repeat-x scroll 50% 50%;
                color:#000000;
                font-weight:normal;
                outline-color:-moz-use-text-color;
                outline-style:none;
                outline-width:medium;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            *{font:12px verdana}
            .ui-state-default{
                background-color:#bdbdbd;
                color:#000;
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
            }
            label{font-weight:bold}
        </style>
        <script type="text/javascript">
            $(document).ready(function(){
                $test=$(".ui-state-default ");
                $test.hover(
                function(){
                    this.style.background="#696E77";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#bdbdbd";
                    this.style.color="#000"
                });
            });
            function cancelfrgetpswrd()
            {
                document.forms.frgetpswrdfrm.action="baseAction.do?param=loginPage";
                document.forms.frgetpswrdfrm.submit();
            }

            function echeck(str)
            {

                var at = "@"
                var dot = "."
                var lat = str.indexOf(at)
                var lstr = str.length
                var ldot = str.indexOf(dot)
                if (str.indexOf(at) == - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at) == - 1 || str.indexOf(at) == 0 || str.indexOf(at) == lstr)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot) == - 1 || str.indexOf(dot) == 0 || str.indexOf(dot) == lstr)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(at, (lat + 1)) != - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.substring(lat - 1, lat) == dot || str.substring(lat + 1, lat + 2) == dot)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(dot, (lat + 2)) == - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }

                if (str.indexOf(" ") != - 1)
                {
                    alert("Invalid E-mail ID")
                    return false
                }
                return true;
            }
            function validatefrgtpswrd()
            {
                var emailID = document.getElementById("usermailid").value;
            <%--alert(emailID.value);--%>
                    var usrname = document.getElementById("username").value;

                    if(usrname.length==0 || usrname=="")
                    {
                        alert("please Enter UserName");
                        return false;
                    }
                    if(usrname.length<4)
                    {
                        alert("Invalid User Name");
                        return false;
                    }
                    if ((emailID == null) || (emailID == ""))
                    {
                        alert("Please Enter your Email ID")
                        emailID.focus()
                        return false
                    }
                    //alert(echeck(emailID));
                    if (echeck(emailID) == false)
                    {
                        //alert("if false");
                        emailID.value = ""
            <%--emailID.focus();--%>
                        return false;
                    }
            <%--return false;--%>
            <%--if(emailID.length==0)
                {
                    return false;
                }--%>
                    }
        </script>
    </script>

</head>

<body>
   <table class="upperTab" width="100%">
                <tr>
            <td valign="top" style="height:30px;width:10%" align="left">
                 <%if(!isCompanyValid){%>
                       <img width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/pi_logo.png"/>
                       <%}%>

                    </td>
                    <td valign="top" style="height:30px;width:80%" >

                    </td>
            <td valign="top" style="height:30px;width:10%" align="right">
               <img width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>
                    </td>
                </tr>
            </table>
            <table class="bottomTab" width="100%">
                <tr>
            <td valign="top" style="height:30px;width:10%;" align="left">
                <img width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>
                    </td>
                    <td valign="top" style="height:30px;width:80%" align="right">

                    </td>
            <td valign="top" style="height:30px;width:10%" >
                <img width="40px" height="30px"  title="pi " src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>"/>
                    </td>
                </tr>
            </table>
    <div id="header" class="header">
        <table width="100%" align="center" border="0px">
            <tr style="width:100%">
                <td>
                    <font style="font-family:verdana;font-size:16px;color:#000;font-weight:bold">Forget Password</font>
                </td>
            </tr>
        </table>
    </div>
    <div id="demo" class="white_content">
        <center>

            <form action="<%=request.getContextPath()%>/passwordAction.do?pswrdparam=getpswrd" method="post" name="frgetpswrdfrm" style="background-color:#e6e6e6">
                <br/>
                <table style="background-color:#e6e6e6">
                    <tr>
                        <td>
                            <label>E-mail Id</label>
                        </td>
                        <td>
                            <input type="text" name="useremailid" id="usermailid" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>User Name</label>
                        </td>
                        <td>
                            <input type="text" name="username" id="username" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        </tr>
                        <tr>
                            <td>
                            </td>
                        </tr>
                    <tr align="center">
                        <td align="left">
                            <input type="submit" value="GetPassword" class="ui-state-default" onclick="return validatefrgtpswrd()">
                        </td>
                        <td >
                            <input type="button" value="Cancel" class="ui-state-default" onclick="cancelfrgetpswrd()">
                        </td>
                    </tr>
                </table>
                <br/>
                <br/>
                <%
            if (request.getAttribute("failuremsge") != null) {
                out.println("<font color=red><strong>" + request.getAttribute("failuremsge") + "</font></strong>");
            }
            if (request.getAttribute("succesmsg") != null) {
                out.println("<font color=#336699><strong>" + request.getAttribute("succesmsg") + "</font></strong>");
            }
                %>
            </form>
        </center>
    </div>

    <div id="fade" class="black_overlay"></div>
</body>
</html>
