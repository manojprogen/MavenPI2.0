<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>

<%
boolean isCompanyValid=false;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <title><bean:message key="ProGen.Title"/></title>
        <style type="text/css">
/*            body {
  background-image: url("<%=request.getContextPath()%>/images//prg2.png");
  background-position: 0% -20%;
  background-repeat: no-repeat;
  height: 200px;
  width: 300px;
}*/
            .white_content {
                position: absolute;
                top: 20%;
/*                left: 35%;*/
                width:28%;
                height:399px;
                padding: 5px;
/*                border: 2px solid #336699;*/
/*                background-color:white;*/
                z-index:1002;
                -moz-border-radius-bottomleft:10px;
                -moz-border-radius-bottomright:10px;
                -moz-border-radius-topleft:10px;
                -moz-border-radius-topright:10px;
            }
             .white1_content {
                position: absolute;
                top: 30%;
/*                left: 14%;*/
                width:452px;
                height:180px;
                padding: 5px;
/*                border: 1px solid #336699;*/
/*                background-color:white;*/
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
                background-color: white;
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
                border-bottom: 1px solid #87CEEB;
            }
            .bottomTab{
                position: absolute;
                top: 92%;
                left: 0%;
            }
            .middleTab{
                position: absolute;
                top: 10%;
                left: 30%;
            }
            .right{
                 height:50px;
                 width:140px;
                 }
           .left{
              height:40px;
            width:60px;
              }

        </style>
    </head>
    <%
    session.removeAttribute("connId");
    %>
    <body style="overflow:hidden" >
         <%
         if(brdcrmb!=null)
             {
             session.invalidate();
             }
       /*  System.gc();
     com.progen.action.BreadCurmbBean removebrdcrumb = new com.progen.action.BreadCurmbBean();
    removebrdcrumb.setPgname1(null);
    removebrdcrumb.setPgname2(null);
    removebrdcrumb.setPgname3(null);
    removebrdcrumb.setPgname4(null);
    removebrdcrumb.setPgname5(null);
    removebrdcrumb.setPgurl1(null);
    removebrdcrumb.setPgurl2(null);
    removebrdcrumb.setPgurl3(null);
    removebrdcrumb.setPgurl4(null);
    removebrdcrumb.setPgurl5(null);
 * */
    String REPORTID="";
    if(request.getAttribute("REPORTID")!=null){
     REPORTID=String.valueOf(request.getAttribute("REPORTID"));
    }
    request.setAttribute("REPORTID",REPORTID);
  //  //////////////////////////////////////////.println.println("REPORTID--in login page--"+REPORTID);
    %>
    <img alt="" style="margin-top: 5%;opacity: 0.7;border-bottom:  2px solid skyblue;border-left: 2px solid skyblue;border-top: 2px solid skyblue;border-right:  2px solid skyblue;" border="0px"  width="70%" height="183px"  src="<%=request.getContextPath()%>/images/Pic1.png"/>
    <img alt="" style="margin-top: 0px;opacity: 0.7;border-bottom:  2px solid skyblue;border-left: 2px solid skyblue;border-right:  2px solid skyblue;" border="0px"  width="70%" height="183px"  src="<%=request.getContextPath()%>/images/Pic2.png"/>
    <img alt="" style="margin-top: 0px;opacity: 0.7;border-left: 2px solid skyblue;border-right:  2px solid skyblue;border-bottom:  2px solid skyblue;" border="0px"  width="70%" height="183px"  src="<%=request.getContextPath()%>/images/Pic3.png"/>
        <form name="myForm" action="" method="post">
            <input type="hidden" name="user" id="user"  value="" >
            <input type="hidden" name="password" id="password" type="password"  value="">
            <input type="hidden" name="screenwidth" id="screenwidth" value="">
            <input type="hidden" name="screenheight" id="screenheight" value="">
                <%--un comment for indicus only--%>
                  <%if(isCompanyValid){%>
        <input type="hidden" name="accounttype" id="accounttype" value="">
              <%}%>
            <table class="upperTab" width="100%">
                <tr>
                   <td valign="top" style="height:30px;width:10%;">
                     <% if(!(isCompanyValid)) { %>
                     <img class="left" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>">
<!--                     <img alt=""  width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>"/>-->
<!--                     <img  src="<%=request.getContextPath()%>/images/NEWProGen.png" style="height:50px" alt=""> -->
                     <%}%>
                    </td>
                    <td valign="top" style="height:30px;width:80%" >
                        
                    </td>
                   <td valign="top" style="height:30px;width:10%;" align="right">
                       <img class="right" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>">
<!--                      <img style="height:70px" src="<%=request.getContextPath()%>/images/Newpi_Logo.png" alt="">-->
<!--                        <img alt=""  width="0px" height="50px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"-->
                    </td>
                </tr>
            </table>
<!--          <table class="bottomTab" width="100%">
                <tr>
                    <td valign="top" style="height:30px;width:10%;" align="left">
                        <img alt=""  width="130px" height="45px"  title="Progen Business Solutions" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.businessLogo"/>"/>
                    </td>
                    <td valign="top" style="height:30px;width:80%" >

                    </td>
                    <td valign="top" style="height:30px;width:10%;" align="right">
                      <% if(!(isCompanyValid)){%>
                      <img alt=""  width="40px" height="30px"  title="pi" src="<%=request.getContextPath()%>/images/<bean:message key="ProGen.piLogo"/>"/>
                       <% }%>
                    </td>
                </tr>
</table>-->
<!--                       <div>
                       <table width="100%">
                           <tr align="center">
                               <td width="50%">
                                   <div  width="100%"style="border-color: blue " class="white1_content">
                                       <img style="height:240px;width:300px; border-color: blue " src="<%=request.getContextPath()%>/images/path.gif"  alt="">
                                   </div>
                               </td>
                               <td width="50%">-->
<div id="report" style="margin-left:70%;width: 28%;width: 380px " >
    <iframe  align=""   frameborder="0" marginheight="0" marginwidth="0"  class="white_content"  SRC='newLogin.jsp?REPORTID=<%=REPORTID%>'></iframe>
            </div>
<!--                               </td>
                           </tr>
                           </table></div>-->
            
<!--            <div id="fade" class="black_overlay"></div>-->
        </form>
    </body>
</html>
