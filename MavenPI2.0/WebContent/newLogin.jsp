<%@ page import="utils.db.*"%>

<%
boolean isCompanyValid=false;
%>


<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>User Login</title>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/browserDetection.js"/>-->
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet"/>

         <link rel="stylesheet" type="text/css" href="css/style_login.css" />

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
        </script>
         <script type="text/javascript">
            function childcancel()
            {
                //alert("newlogin.jsp");
                parent.logout();
            }
            function doLogin(conPath,REPORTID,isCompanyValid){
//                 alert('hi')
                var succFlag = true;

                if(document.myForm.user.value == null || document.myForm.user.value == '' || document.myForm.user.value == 'User Name')
                {
                    alert("Please enter username");
                    document.myForm.user.focus();
                    succFlag = false;
                }
                else if(document.myForm.password.value == null || document.myForm.password.value == '' || document.myForm.password.value == 'nullnull')
                {

                    alert("Please enter password");
                    document.myForm.password.focus();
                    succFlag = false;
                }

        <%--un comment this else conditon for indicus only--%>
                        else if( isCompanyValid==true)
                        {  if((document.myForm.accounttype.value == null || document.myForm.accounttype.value == '')){
                                alert("Please enter Company Name");
                                document.myForm.accounttype.focus();
                                succFlag = false;
                            }
                        }
                        // alert('succFlag '+succFlag)
                        if(succFlag)
                        {
                            parent.document.getElementById("user").value=document.getElementById("user").value;
                            parent.document.getElementById("password").value=document.getElementById("password").value;
                            parent.document.getElementById("screenwidth").value=screen.width;
                            parent.document.getElementById("screenheight").value=screen.height;

        <%--un comment this else conditon for indicus only--%>
                                 //  alert(isCompanyValid)
                                 if(isCompanyValid==true){
                                     //  alert('hyyyy ')

                                     parent.document.getElementById("accounttype").value=document.getElementById("accounttype").value;
                                     // alert(parent.document.getElementById("accounttype").value)
                                 }

                                 if(REPORTID==""){
                                     parent.document.myForm.action=conPath + "/baseAction.do?param=loginApplication";
                                 }else{
                                     parent.document.myForm.action=conPath + "/baseAction.do?param=loginApplication&REPORTID="+REPORTID;
                                 }
                                 //alert( parent.document.myForm.action)
                                 parent.document.myForm.submit();
                             }
                             return false;
                         }
                         function setFocus()
                         {
                             document.myForm.user.focus();
                         }
                         function goLogin(){
                             parent.document.forms.myForm.submit();
                         }
                         function gotopswrdpages(path)
                         {
//                             $.ajax({
//                                 data:$("#updtepswrdfrm").serialize(),
//                                 type:"POST"
//                                   url:ctxPath+"/passwordAction.do?pswrdparam=updatePassword",  
//                                   function:success(data){
//                                       
//                                   }
//                             })
                             
                             //alert(path);
//                             parent.document.myForm.action=path;
//                             parent.document.myForm.submit();
                         }
						  function gotoRegister(path)
                         {
                             //alert(path);
                             parent.document.myForm.action=path;
                             parent.document.myForm.submit();
                         }
        </script>
        <style type="text/css">
        .white1_content {
            position: absolute;
            top: 15%;
            left:8%;
            width:180px;
            height:180px;
            padding: 5px;
            /*                border: 1px solid #336699;*/
            background-color:white;
            z-index:1002;
            -moz-border-radius-bottomleft:5px;
            -moz-border-radius-bottomright:5px;
            -moz-border-radius-topleft:5px;
            -moz-border-radius-topright:5px;
        }
        .hyper{
            font-family:verdana;
            text-decoration:none;
            color:#369;
        }
        .hyper:hover{text-decoration:underline}
        .demo{
            left:200px;
            top:50px;
            height:auto;
            width:auto;
        }
        .leftcol {
            clear:left;
            float:left;
            width:100%;
            background-color:white;
        }
        .label{
            background-color:white;
            background-position:8px center;
            background-repeat:no-repeat;
            clear:both;
            height:auto;
            width:auto;
            cursor:pointer;
            display:block;
            margin-bottom:0;
            margin-right:0;
            padding:0 0.3em 0.3em 22px;
            font-family:verdana;
            font-weight:bold;
        }
        *{font:11px verdana}
        </style>
        <%
            String lMessage = null;
            String validateUser = String.valueOf(request.getAttribute("validateUser"));
            ////////////////////////////////////////////////////////////////////////////////.println.println("validateUser is " + validateUser);
            ////////////////////////////////////////////////////////////////////////////////.println.println("before lMessage is " + lMessage);
            if (validateUser == null) {
                ////////////////////////////////////////////////////////////////////////////////.println.println("Inside");
                lMessage = "Login Failed... Username/Password is incorrect...";
            }
            String REPORTID = "";
            //  //////////////////////////////////.println.println("request.getAttribute("+request.getAttribute("REPORTID"));
            //    //////////////////////////////////.println.println("requestrequest.getParameter("+request.getParameter("REPORTID"));
            if (request.getAttribute("REPORTID") != null) {
                //////////////////////////////////.println.println("in if");
                REPORTID = String.valueOf(request.getAttribute("REPORTID"));
                // //////////////////////////////////.println.println("in if");

            } else if (request.getParameter("REPORTID") != null && (!"".equalsIgnoreCase(request.getParameter("REPORTID")))) {
                REPORTID = request.getParameter("REPORTID");
                //  //////////////////////////////////.println.println("in if else--"+REPORTID);
            }

            request.setAttribute("REPORTID", REPORTID);
        %>

    </head>
    <body class="leftcol login-form" onload="document.myForm.user.focus();" >
        <form name="myForm" style="border-radius:90px;opacity:0.7;background: #1B3E70" onLoad="setFocus()" onSubmit="return doLogin('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST" >

<!--            <div valign="left" class="white1_content" >-->

<!--                <table  style="height:auto;width:380px"  cellpadding="0" cellspacing="0">
                    <tr>
                        <td> <div valign="left">
                                <table width="100%">
                                    <td><img style="height:90px" src="<%=request.getContextPath()%>/images/Newpi_Logo.png" alt=""></td>

                                </table>
                            </div></td>
                        <td valign="middle" align="center">

                            <div id="demo" class="demo" align="" >
                                <table  border="0" cellspacing="0" cellpadding="0" align="left" >-->
                                    <%
                if (lMessage != null) {
                                    %>
                                    <tr>
                                        <td>
                                            <Font color="red"><%=lMessage%></Font>
                                        </td>
                                    </tr>
                                    <%
                }
                                    %>
<!--                                    <tr>
                                        <td>
                                            <br>
                                        </td>
                                        <td valign="middle">-->
<!--                                            <div id="leftcol" class='leftcol' width="100%" align="justify"  >
                                                <table width="100%" cellpadding="5" cellspacing="3">
                                                    <tr width="100%">
                                                        <td width="30%"><label class="label">Username</label></td>
                                                        <td width="70%"><input type="text" name="user" id="user" style="width:100px" title="Enter Username" value="" ></td>
                                                    </tr>
                                                    <tr width="100%">
                                                        <td width="30%"><label class="label">Password</label></td>
                                                        <td width="70%"><input type="password" id="password" name="password" style="width:100px" title="Enter Password" value="" ></td>

                                                    </tr>
                                                    <%if (isCompanyValid) {%>
                                                    <tr width="100%">
                                                        <td width="50%"><label class="label">Company Name</label></td>
                                                        <td width="50%"><input type="text" id="accounttype" name="accounttype" style="width:100px" title="Enter Company" value="" ></td>
                                                    </tr>
                                                    <%}%>
                                                    <%--un comment this tr conditon for indicus only--%>

                                                    <tr>
                                                        <td colspan="2" align="center">
                                                            <input type="submit" name="Submit" class="navtitle-hover"  id="submit"  value="Login">
                                                        </td>

                                                    </tr>
                                                    <tr>
                                                       <td colspan="2" align="center">
                                                            <a class="hyper" href="javascript:void(0);" onclick='gotopswrdpages("<%=request.getContextPath()%>/baseAction.do?param=changepswrdpage")'>Change Password</a>
                                                            <%--|
                                                            <a class="hyper" href="javascript:void(0);" onclick='gotopswrdpages("<%=request.getContextPath()%>/baseAction.do?param=frgtpswrdpage")'>Forgot Password</a> --%>
                                                        </td>
                                                    </tr>

                                                </table>
                                            </div>-->

<!--                                              <div class="login-form white1_content">-->
						<h1>Login </h1>
						<h2><a class="hyper" href="javascript:void(0);" onclick='gotoRegister("<%=request.getContextPath()%>/baseAction.do?param=register")' >Register</a></h2>
<!--				<form>-->
					<li>
						<a class=" icon user"></a><input class="text" name="user" id="user" value="User Name" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'User Name';}" type="text">
					</li>
					<li>
						<input value="nullnull" id="password" name="password" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Password';}" type="password"><a class=" icon lock"></a>
					</li>
                                            <%if (isCompanyValid) {%>
                                            <li>
						<input class="text" id="accounttype" name="accounttype" value="Company Name" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Company Name';}" type="text"><a  class=" icon user"></a>
					</li>

                                             <%}%>
					 <div class="forgot">
						<input type="submit"  name="Submit" class=""  id="submit"  value="Login">
						<h3><a class="hyper" href="javascript:void(0);" onclick='gotopswrdpages("<%=request.getContextPath()%>/baseAction.do?param=changepswrdpage")'>Change Password</a></h3>

<!--                                                <input  name="Submit" id="Submit" value="Login" type="submit"> <a href="#" class=" icon arrow"></a>-->
					</div>
<!--				</form>-->
<!--			</div>-->






<!--                                        </td>
                                        <td valign="bottom" style="height:30px;width:10%;" align="right"><img src="<%=request.getContextPath()%>/images/NEWUserlogo.jpeg" style="height:70px" alt=""></td>
                                    </tr>

                                </table>
                            </div>
                        </td>
                    </tr>

                </table>
                <table width="100%" align="center">

                </table>-->
<!--            </div>                    -->
        </form>
    </body>
</html>