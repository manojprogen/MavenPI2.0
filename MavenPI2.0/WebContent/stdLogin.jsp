<%-- 
    Document   : stdLogin
    Created on : May 9, 2015, 4:52:12 PM
    Author     : Manik Srivastava
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>

<%
boolean isCompanyValid=false;
 String themeColor = "blue";
    if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    } else {
        themeColor = String.valueOf(session.getAttribute("theme"));
    }
 String contextPath=request.getContextPath();
%>
<html style="background-color:black;overflow: hidden"      >
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title><bean:message key="ProGen.Title"/></title>
        <link href="<%=contextPath%>/css/loginCss.css" rel="stylesheet" type="text/css" />
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js" type="text/javascript"></script>
    
    <%
    session.removeAttribute("connId");
   
         if(brdcrmb!=null)
             {
             session.invalidate();
             }
          String REPORTID="";
    if(request.getAttribute("REPORTID")!=null){
     REPORTID=String.valueOf(request.getAttribute("REPORTID"));
    }
    request.setAttribute("REPORTID",REPORTID);
    %>
       
		
                 <script type="text/javascript">
$( document ).ready(function() {
//    alert("innnnn")
$("#logDiv").height(($(window).height())*.95);
$("#logDiv").css("margin-top",(($(window).height())*.025)) ;
});

</script>
    </head>
    <body id="bdDiv" style="overflow: auto">
  <div id="logDiv" style="background-color:white;width:95%;margin-left: 2.5%;border:1px solid #1919D2;overflow: hidden">
		<!--<div class="" style="background:  url(images/pi_Logo_New.jpg) repeat top left;width:70%;height:600px;float:left">-->
				<div class="" style="width:65%;height:100%;float:left">
		<img alt="" border="0px" style="margin-left:9%;margin-top:3%;width: 65%;height: 92%"    src="images/pi_Logo_New.jpg"/>
		</div>
		
		<div class="container 	" style="float:left;width:30%;margin-top:7%;margin-left:4%">
<!--<img alt="" border="0px" style="margin-left:10%;margin-top:5%" width="50%" height="400px"   src="images/rightImg.jpg"/>		-->	
			<header>
			
				
				
			</header>
			<section class="main">
				<form class="form-3" name="loginForm" onLoad="setFocusPortal()" action="" id="loginForm" onSubmit="return doLoginStd('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST">
                                   
                                    
                                    <p class="clearfix">
				        <label for="login">Username</label>
				        <input type="text" name="user" id="user"  placeholder="Username">
				    </p>
				    <p class="clearfix">
				        <label for="password">Password</label>
				        <input type="password" name="password" id="password" placeholder="Password"> 
				    </p>
				    <p class="clearfix">
				     <!--   <input type="checkbox" name="remember" id="remember">-->
				        <label for="remember" onclick='gotoRegisterPortal("<%=request.getContextPath()%>/baseAction.do?param=register")'>Register</label><br>
						
				    </p>
				    <p class="clearfix">
				        <!--<input type="checkbox" name="remember" id="remember">-->
				        <label for="remember" onclick='gotopswrdpagesPortal("<%=request.getContextPath()%>/baseAction.do?param=changepswrdpage")'>Change Password</label><br>
						
				    </p>
					
				    <p class="clearfix">
				        <input type="submit" id="login" value="Login">
				    </p>      
                                     <input type="hidden" name="screenwidth" id="screenwidth" value="">
                                  <input type="hidden" name="screenheight" id="screenheight" value="">
				</form>​
			</section>
			
        </div>
		<div style="width:30%;float:left;height:20%;margin-top:6%;margin-left:5%">
<!--		<img alt="" border="0px" style="height: 100%;width: 100%;"    src="images/Progen_Logo_New_2.jpg"/>-->
		<!--<img alt="" border="0px" style="height: 100%;width: 100%;"    src="images/CampbellShipping_logo.jpg"/>-->
		</div>
		</div>
           
           
               
<!--                    <form name="loginForm" onLoad="setFocusPortal()" action="" id="loginForm" onSubmit="return doLoginPortal('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST">
        <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">
            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">

                     <fieldset id="body">
                            <fieldset>
                                <label for="User Name">User Name</label>
                                <input type="text" name="user" id="user" value="User Name" />
                            </fieldset>
                            <fieldset>
                                <label for="password">Password</label>
                                <input type="password" name="password" id="password" value="nullnull"/>
                            </fieldset>
                                        <li style="background-color: white; ">
						<input class="" name="user" id="user" value="User Name" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'User Name';}" type="text">
					</li>
					<li style="background-color: white; ">
						<input value="nullnull" id="password" name="password" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Password';}" type="password">
					</li>
                            <input type="submit" id="login" value="Login" />
                            <label for="checkbox"><input type="checkbox" id="checkbox" />Remember me</label>
                            <h2><a style="color:#3E454D;" class="" href="javascript:void(0);" onclick='gotoRegisterPortal("<%=request.getContextPath()%>/baseAction.do?param=register")' >Register</a></h2>
                        </fieldset>
                        <span><a href="#">Forgot your password?</a></span>
                    </form>-->

           
            
 

  <script type="text/javascript">
            
            function doLoginStd(conPath,REPORTID,isCompanyValid){
//                 alert('hi')
                var succFlag = true;

                if(document.loginForm.user.value == null || document.loginForm.user.value == '' || document.loginForm.user.value == 'User Name')
                {
                    alert("Please enter username");
                    document.loginForm.user.focus();
                    succFlag = false;
                }
                else if(document.loginForm.password.value == null || document.loginForm.password.value == '' || document.loginForm.password.value == 'nullnull')
                {

                    alert("Please enter password");
                    document.loginForm.password.focus();
                    succFlag = false;
                }

                        else if( isCompanyValid==true)
                        {  if((document.loginForm.accounttype.value == null || document.loginForm.accounttype.value == '')){
                                alert("Please enter Company Name");
                                document.loginForm.accounttype.focus();
                                succFlag = false;
                            }
                        }
                        // alert('succFlag '+succFlag)
                        if(succFlag)
                        {
//                           document.getElementById("user").value=document.getElementById("user1").value;
//                            document.getElementById("password").value=document.getElementById("password1").value;
                            document.getElementById("screenwidth").value=screen.width;
                            document.getElementById("screenheight").value=screen.height;

                                 if(isCompanyValid==true){
                                     //  alert('hyyyy ')

                                     document.getElementById("accounttype").value=document.getElementById("accounttype").value;
                                     // alert(document.getElementById("accounttype").value)
                                 }
//                                   alert( document.getElementById("user").value)
//                                   alert( document.getElementById("password").value)
                                 if(REPORTID==""){
                                     document.loginForm.action=conPath + "/baseAction.do?param=loginApplication";
                                 }else{
                                     document.loginForm.action=conPath + "/baseAction.do?param=loginApplication&REPORTID="+REPORTID;
                                 }
                                 //alert( document.loginForm.action)
                                 document.loginForm.submit();
                             }
                             return false;
                         }
                         function setFocusPortal()
                         {
                             document.loginForm.user.focus();
                         }
                         function goLoginPortal(){
                             document.forms.loginForm.submit();
                         }
                         function gotopswrdpagesPortal(path)
                         {
//                             $.ajax({
//                                 data:$("#updtepswrdfrm").serialize(),
//                                 type:"POST",
//                                   url:ctxPath+"/passwordAction.do?pswrdparam=updatePassword",
//                                   function:success(data){
//
//                                   }
//                             })

                             //alert(path);
                             document.loginForm.action=path;
                             document.loginForm.submit();
                         }
						  function gotoRegisterPortal(path)
                         {
                             //alert(path);
                             document.loginForm.action=path;
                             document.loginForm.submit();
                         }
        </script>
    </body>
</html>


