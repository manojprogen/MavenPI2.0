<%--
    Document   : passwordUpdate
    Created on : Jan 13, 2010, 11:27:26 AM
    Author     : Administrator
--%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>UpdatePassword Page</title>
         <script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
        <script type="text/javascript">

            function cancelupdtpswrd()
            {
            document.forms.updtepswrdfrm.action="baseAction.do?param=loginPage";
            document.forms.updtepswrdfrm.submit();
                    parent.childcancel();
                }

                function submitForm(){
                    if(validateupdtepswrd()){
                        var username = document.updtepswrdfrm.username.value;
                        var oldpswrd = document.updtepswrdfrm.useroldpswrd.value;
                        var newpswrd=document.updtepswrdfrm.usernewpswrd.value;
                        var cnfrmpswrd = document.updtepswrdfrm.usrcnfrmpswrd.value;
                        var strurl='<%=request.getContextPath()%>/passwordAction.do?pswrdparam=updatePassword&username='+username+'&useroldpswrd='+oldpswrd+'&usernewpswrd='+newpswrd+'&usrcnfrmpswrd='+cnfrmpswrd;

                        $.ajax({
                            url:strurl,
                            success:function(data){
                                alert("data is "+data)
                                parent.document.getElementById("user").value=document.updtepswrdfrm.username.value;
                                parent.document.getElementById("password").value=document.updtepswrdfrm.usernewpswrd.value;
                                parent.document.myForm.action="<%=request.getContextPath()%>/baseAction.do?param=loginApplication";
                                parent.document.myForm.submit();
                            }
                        });
                    }
                }

                function validateupdtepswrd()
                {
                    var username = document.updtepswrdfrm.username.value;
                    var oldpswrd = document.updtepswrdfrm.useroldpswrd.value;
                    var newpswrd=document.updtepswrdfrm.usernewpswrd.value;
                    var cnfrmpswrd = document.updtepswrdfrm.usrcnfrmpswrd.value;

                    if(oldpswrd.length<4){
                        alert("Invalid Old password");
                        return false;
                    }
                    else if(newpswrd.length<4){
                        alert("New Password should not less then four charechters");
                        return false;
                    }
                    else if(cnfrmpswrd!=newpswrd){
                        alert("Confirm Password and New Password are not equal");
                        return false;
                    }else{
                        return true;
                    }
             window.location.href = window.location.href;
                }
        </script>
    </head>
    <body bgcolor="#C0C0C0">
        <center>
            <form action="<%=request.getContextPath()%>/passwordAction.do?pswrdparam=updatePassword" method="post" name="updtepswrdfrm">
                <table>
                    <tr>
                        <td>
                            <label>User Name:-</label>
                        </td><td>
                            <input type="text" name="username" id="username">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Old Password:-</label>
                        </td><td>
                            <input type="password" name="useroldpswrd" id="useroldpswrd">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>New Password:-</label>
                        </td><td>
                            <input type="password" name="usernewpswrd" id="usernewpswrd">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Confirm Password:-</label>
                        </td><td>
                            <input type="password" name="usrcnfrmpswrd" id="usrcnfrmpswrd">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <input type="submit" value="Update Password" onclick="return submitForm()">
                        </td><td>
                            <input type="button" value="Cancel" onclick="cancelupdtpswrd()">
                        </td>
                    </tr>
                </table>
                <%
            if (request.getAttribute("failuremsg") != null) {
                String status = request.getAttribute("failuremsg").toString();
                out.println("<font color=red><strong>" + status + "</font></strong>");
                //////////////////////////////////////////////////////////////////.println.println("status in jsp is"+status);
            }
            if (request.getAttribute("succesmsg") != null) {
                String status = request.getAttribute("succesmsg").toString();
                out.println("<font color=red><strong>" + status + "</font></strong>");
                //////////////////////////////////////////////////////////////////.println.println("status in jsp is"+status);
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

<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title><bean:message key="ProGen.Title"/></title>
          <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            
            body {
  background-image: url("<%=request.getContextPath()%>/images//prg2.png");
  background-position: 0% -20%;
  background-repeat: no-repeat;
  height: 200px;
  width: 300px;
}

       <Script language="javascript"  src="<%=request.getContextPath()%>/javascript/scripts.js"></Script>
        <script language="javascript" src="<%=request.getContextPath()%>/javascript/ajaxscript.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
  <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />

          <style type="text/css">

            .leftcol {
                clear:left;
                float:left;
                width:100%;
                background-color:white;
            }
            .header {
                position: absolute;
                top: 22%;
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
                top: 30%;
                left: 60%;
                width:452px;
                height:225px;
                padding: 5px;
                border: 2px solid #336699;
                background-color:white;
                z-index:1002;
                -moz-border-radius-bottomleft:10px;
                -moz-border-radius-bottomright:10px;
                -moz-border-radius-topleft:10px;
                -moz-border-radius-topright:10px;
            }
           .black_overlay{
                position: absolute;
                top: 0%;
                left: 0%;
                width: 100%;
                height: 100%;
                background-color: transparent;
                z-index:1001;
                -moz-opacity: 0.8;
                opacity:.50;
                filter:alpha(opacity=60);
                overflow:auto;
            
            }
            
            .hyper:hover{text-decoration:underline}
        .demo{
            left:200px;
            top:50px;
            height:auto;
            width:auto;
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
                background:white url(images/ui-bg_glass_75_bdbdbd_1x400.png) repeat-x scroll 50% 50%;
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
            function cancelupdtpswrd()
            {
                document.forms.updtepswrdfrm.action="baseAction.do?param=loginPage";
                document.forms.updtepswrdfrm.submit();
                //parent.childcancel();
            }
            function validateupdtepswrd()
            {
                var username = document.updtepswrdfrm.username.value;
                var oldpswrd = document.updtepswrdfrm.useroldpswrd.value;
                var newpswrd=document.updtepswrdfrm.usernewpswrd.value;
                var cnfrmpswrd = document.updtepswrdfrm.usrcnfrmpswrd.value;

                if(username=="" || username.length==0)
                {
                    alert("Please Enter Username");
                    return false;
                }
                else if(oldpswrd=="" || oldpswrd.length==0)
                {
                    alert("Please Enter Old password");
                    return false;
                }
                else if(newpswrd=="" || newpswrd.length==0)
                {
                    alert("Please Enter New password");
                    return false;
                }
                else if(cnfrmpswrd=="" || cnfrmpswrd.length==0)
                {
                    alert("Please Enter Confirm Password");
                    return false;
                }

                else if(oldpswrd.length<4){
                    alert("Invalid Old password");
                    return false;
                }
                else if(newpswrd.length<4){
                    alert("New Password should not less then four charechters");
                    return false;
                }
                else if(cnfrmpswrd!=newpswrd){
                    alert("Confirm Password and New Password are not equal");
                    return false;
                }else{
                    
                    $.ajax({
                                 type:"POST",
                                 data:$("#updtepswrdfrm").serialize(),
                                   url:ctxPath+"/passwordAction.do?pswrdparam=updatePassword",  
                                   success:function(data){
                                       
                                   }
                             })
                    return true;
                }
            }
        </script>

    </head>
<%
boolean isCompanyValid=false;
%>
    <body style="overflow:hidden">
        <table class="upperTab" width="100%">
                <tr>
                <td valign="top" style="height:30px;width:10%" align="left">
                    <%if(!isCompanyValid){%>
<!--                       <img width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/pi_logo.png"/>-->
                     <img  src="<%=request.getContextPath()%>/images/NEWProGen.png" style="height:50px" alt="">   
                    <%}%>

                    </td>
                    <td valign="top" style="height:30px;width:80%" >

                    </td>
                <td valign="top" style="height:30px;width:10%" align="right">
<!--                    <img width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>-->
                  <img style="height:70px" src="<%=request.getContextPath()%>/images/Newpi_Logo.png" alt="">    
                </td>
                </tr>
            </table>
<!--            <table class="bottomTab" width="100%">
                <tr>
                <td valign="top" style="height:30px;width:10%;" align="left">
                   <img width="150px" height="30px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>
                    </td>
                    <td valign="top" style="height:30px;width:80%" >

                    </td>
                    <td valign="top" style="height:30px;width:10%" align="right">
                    <img width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>"/>
                    </td>
                </tr>
            </table>-->
<!--        <div id="header" class="header">
            <table width="100%" border="0px">
               <tr style="width:100%">
                   <td>
                        <font style="font-family:verdana;font-size:16px;color:#000;font-weight:bold">Update Password</font>
                    </td>
                </tr>
            </table>
        </div>-->
        <div id="demo" class="white_content" >
            <form action="<%=request.getContextPath()%>/passwordAction.do?pswrdparam=updatePassword" method="post" id="updtepswrdfrm" name="updtepswrdfrm" style="background-color:white">
                
                <br/>
                  <div valign="left" class="white1_content" >
               
                <table  style="height:auto;width:100%" cellpadding="0" cellspacing="0">
                    <tr><td> <div valign="left">
                                <table width="100%">
                                    <td><img style="height:90px" src="<%=request.getContextPath()%>/images/Newpi_Logo.png" alt=""></td>

                                </table>
                            </div></td>
                            <td valign="middle" align="center">
                <table align="right" >
                    <tr style="width:100%" align="right">
                        <td align="right">
                      &nbsp;  &nbsp; <font style="font-family:verdana;font-size:12px;color:#000;font-weight:bold">Update Password</font>
                    </td>
                </tr>
                    <tr>
                        <td>
                            <label>User Name</label>
                        </td><td>
                            <input type="text" name="username" id="username" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Old Password</label>
                        </td><td>
                            <input type="password" name="useroldpswrd" id="useroldpswrd" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>New Password</label>
                        </td><td>
                            <input type="password" name="usernewpswrd" id="usernewpswrd" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label>Confirm Password</label>
                        </td><td>
                            <input type="password" name="usrcnfrmpswrd" id="usrcnfrmpswrd" style="width:100px;">
                        </td>
                    </tr>
                    <tr>
                        <td><br/></td>
                        <td><br/></td>
                    </tr>
                </table>
                <table align="center">
                    <tr>
                        <td>
                            <input type="submit" class="navtitle-hover" value="Update Password" onclick="return validateupdtepswrd()">
                        </td><td>
                            <input type="button" class="navtitle-hover" value="Cancel" onclick="cancelupdtpswrd()">
                        </td>
                    </tr>
                </table>
                <br/>
                <center>
                <%
            if (request.getAttribute("failuremsg") != null) {
                String status = request.getAttribute("failuremsg").toString();
                out.println("<font color=red><strong>" + status + "</font></strong>");
                //////////////////////////////////////////////////////////////////.println.println("status in jsp is"+status);
            }
            if (request.getAttribute("succesmsg") != null) {
                String status = request.getAttribute("succesmsg").toString();
                out.println("<font color=red><strong>" + status + "</font></strong>");
                //////////////////////////////////////////////////////////////////.println.println("status in jsp is"+status);
            }
                %>
                </center>
            
                <td valign="bottom" style="height:30px;width:10%;" align="right"><img src="<%=request.getContextPath()%>/images/NEWUserlogo.jpeg" style="height:70px" alt=""></td>
                
                
                  </td>
                  </tr>
                            </table>
                  </div>
                  </form>

                
                  
                  </div>
                
            

        <div id="fade" class="black_overlay"></div>
    </body>
</html>