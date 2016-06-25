<%-- 
    Document   : loginPage
    Created on : 5 Feb, 2016, 03:46:15 PM
    Author     : Faiz Ansari
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,com.progen.user.SessionListener,org.apache.log4j.Logger" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
    <head>
        <link rel="icon" type="image/png" href="<%= request.getContextPath()%>/images/<bean:message key="Progen.Fav"/>"/>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title><bean:message key="ProGen.Title"/></title>
        <%  
        String requString = request.getRequestURL().toString();
        if (requString.contains("https://app-tst-35.mem-tw.veraction.net") || requString.contains("https://ft.client.veraction.com") || requString.contains("https://client.veraction.com")|| requString.contains("https://10.10.30.35")
             || requString.contains("https://service.veraction.com") || requString.contains("https://app-prd-35.mem-tw.veraction.net") || requString.contains("https://10.10.7.35") ){
        response.sendRedirect(application.getInitParameter("sso_login"));
        }
        Logger logger=Logger.getLogger("new_stdLogin.jsp");          
            boolean isCompanyValid = false;
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            session.removeAttribute("connId");
            String REPORTID = "";
            if (request.getAttribute("REPORTID") != null) {
                REPORTID = String.valueOf(request.getAttribute("REPORTID"));
            }
            request.setAttribute("REPORTID", REPORTID);
        %>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.min.css" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/global.css" type="text/css"/>
        <style type="text/css">
            *{font-family: sans-serif;font-weight: normal}
            body{padding: 0px;margin: 0px;height: 100%;width: 100%;background-color: transparent}
            html{min-height:450px;width:100%;height:100%;background-color:#000;overflow:auto;background-color:#000}
            #loginImgDiv{height:140px;width:155px;position: absolute;margin-top: 100px;margin-left: 48%;}
            #loginImgDiv>img{height:90%;width: 100%}
            #loginBox{width: 450px;height: 170px;margin-top: 270px;margin-left: 35%;background-color: rgba(255,255,255,0.5);position: absolute; -webkit-transform: skew(-25deg);-moz-transform: skew(-25deg);-ms-transform: skew(-25deg);-o-transform: skew(-25deg);}
            #loginLabel,#loginInput,#loginSubmit{-webkit-transform: skew(25deg);-ms-transform: skew(25deg);-moz-transform: skew(25deg);-o-transform: skew(25deg);margin: 0px 70px;display: inline-flex}
            #loginLabel1,#loginLabel2,#loginLabel3{padding: 5px 0px;font-weight: bold}
            #loginLabel3{margin-left: 68px;}
            #loginLabel{margin-top: 30px}
            #loginInput{margin-top:0px;}
            #loginSubmit{float: right;margin: 10px 75px;}
            #clientTxt,#userTxt,#passTxt{font-size: 16px;width: 120px;height: 20px;margin-left: 10px;border: 1px solid #CABFBF;padding: 5px;border-radius: 2px;}
            #btnSubmit{width:100px;height:30px;border:1px solid #5f5f5f;font-size: 16px}
            #companyImgDiv{position: absolute;height: 80px;width: 200px}
            #companyImgDiv img{height: 100%;width: 100%}
        </style>
    </head>
    <body onload="setFocus()" onresize="setFocus()">
        <div id='companyImgDiv'>
            <img src="images/progenLogo.png"/>
        </div>
        <div id='loginImgDiv'>
            <img src="images/pi_logo.png"/>
            <div><span style="color: #fff;font-size: 25px;text-align: center;display: block;">User Login</span></div>
        </div>         
        <div id="loginBox">
            <div id="loginLabel">
                <span id="loginLabel2">Username</span>
                <span id="loginLabel3">Password</span>
            </div>
            <div id="loginInput">
                <form class="form-3 form" name="loginForm" action="" id="loginForm" onSubmit="return doLoginStd('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" method="POST">
                    <input onkeypress="check(event)" type="text" name="user" id="userTxt" placeholder="Username">
                    <input onkeypress="check(event)" type="password" name="password" id="passTxt" placeholder="Password" >
                    <input type="hidden" name="screenwidth" id="screenwidth" value="">
                    <input type="hidden" name="screenheight" id="screenheight" value="">
                </form>
            </div>
            <div id="loginSubmit">
                <button onClick="return doLoginStd('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>')" id='btnSubmit' class='prgBtn'type="submit" value="Login"><i id="loginIco" class="fa fa-sign-in"></i> Login</button>
            </div>            
        </div>
        <div style="z-index: 1;position: absolute;top: 0px;right:0px"><img src="<%=request.getContextPath()%>/images/loginImg2.png"/></div>
        <div style="z-index: -1;position: absolute;bottom: 0px;width: 100%"><img style="width:100%" src="<%=request.getContextPath()%>/images/loginImg1.png"/></div>
    <%
                      
/***Added by Ashutosh for Removing Unused Session***/                    
final Map<String, HttpSession> sessionMap = SessionListener.getAllSessionMap();
        HttpSession in_session;
        ArrayList<String> KeysList = new ArrayList<String>(sessionMap.keySet());
        Iterator<String> iter = KeysList.iterator();
        try {
            while (iter.hasNext()) {
                in_session = sessionMap.get(iter.next());
                if (in_session.getAttribute("LOGINID") == null) {
                    in_session.invalidate();
                }
            }
        } catch (Exception e) {
            logger.error("ignore: invalidated session");
        }
/***Ended by Ashutosh***/            
%>
    </body>
    <script type="text/javascript">
        window.onload=function(){
            setFocus();
        }
            function setFocus(){
                document.getElementById("userTxt").focus();
                //document.getElementById("loginImgDiv").style.marginTop="100px";
                document.getElementById("loginImgDiv").style.marginLeft=(window.innerWidth - 155)/2+"px";
                
                //document.getElementById("loginBox").style.marginTop="270px";
                document.getElementById("loginBox").style.marginLeft=(window.innerWidth - 500)/2+"px";
                
            }
            function check(e){
                if(e.which == 13) {
                    doLoginStd('<%=request.getContextPath()%>','<%=REPORTID%>','<%=isCompanyValid%>');
                }
            }
            function doLoginStd(conPath, REPORTID, isCompanyValid) {                
                var succFlag = true;
                if (document.getElementById("userTxt").value == "")
                {   alert("Please enter username");
                    document.getElementById("userTxt").focus();
                    succFlag = false;
                }
                else if (document.getElementById("passTxt").value == "")
                {   alert("Please enter password");
                    document.getElementById("passTxt").focus();
                    succFlag = false;
                }

                else if (isCompanyValid == true)
                {       if ((document.loginForm.accounttype.value == null || document.loginForm.accounttype.value == '')) {
                        alert("Please enter Company Name");
                        document.loginForm.accounttype.focus();
                        succFlag = false;
                    }
                }
                if (succFlag)
                {   document.getElementById("loginIco").className = "";
                    document.getElementById("loginIco").className = "fa fa-circle-o-notch fa-spin";                
                    document.getElementById("screenwidth").value = screen.width;
                    document.getElementById("screenheight").value = screen.height;

                    if (isCompanyValid == true) {
                        document.getElementById("accounttype").value = document.getElementById("accounttype").value;
                    }
                    if (REPORTID == "") {
                        document.loginForm.action = conPath + "/baseAction.do?param=loginApplication";
                    } else {
                        document.loginForm.action = conPath + "/baseAction.do?param=loginApplication&REPORTID=" + REPORTID;
                    }
                    document.loginForm.submit();
                }
                return false;
            }
    </script>
</html>