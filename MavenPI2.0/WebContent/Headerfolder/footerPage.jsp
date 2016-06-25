<%-- 
    Document   : footerPage
    Created on : Feb 1, 2010, 1:38:57 PM
    Author     : Administrator
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%
    // Added by Prabal for dynamic footer
    String copyrightMsg=(String)session.getAttribute("copyRightMsg");
    String webUrl=(String)session.getAttribute("leftWebSiteUrl");
    String compTitle=(String)session.getAttribute("compTitle");
    if(compTitle==null)
      compTitle="Progen Business Solution";
   // 
    //End of code Added by Prabal for dynamic footer
    String themeColor="blue";
    if(session.getAttribute("theme")==null)
        session.setAttribute("theme",themeColor);
   else
       themeColor=String.valueOf(session.getAttribute("theme"));
    boolean isFooterShown=false;
    isFooterShown=Boolean.parseBoolean((String)session.getAttribute("isFooterShown"));
   // System.out.print("Footer value="+isFooterShown);

%>
<html>
    <head>
       <title><bean:message key="ProGen.Title"/></title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet" />
<!--        Added by Faiz Ansari -->
        <style type="text/css">
             #footerTable{
                 bottom: 0px;
                 position:fixed;
                 width:100%;
             }
        </style>
<!--        End!!!   -->
    </head>
    <body>
        <% if(isFooterShown){
            
            if(!copyrightMsg.equalsIgnoreCase("") && !webUrl.equalsIgnoreCase("") ){
        %>
        <div class="rmmg rmpd fontsty" id="footerTable">
<!--            <div class="col-xs-12" style="padding: 10px 0px 5px;">
                <center ><%= copyrightMsg%>  <a href="<%=webUrl%>" class="font-color" style="font-weight:bold;font-size:11px">Progen Business Solutions.</a> All Rights Reserved</center>
             </div>-->
             <div>
                 <center><%= copyrightMsg%>  <a href="<%=webUrl%>" class="font-color" style="font-weight:bold;font-size:11px">Progen Business Solutions.</a> All Rights Reserved
                  Best Viewed on  <a  class="font-color"  href="http://www.progenbusiness.com" style="font-weight:bold;font-size:11px">Firefox 30 , Chrome and IE 11 & above </a> </center>
             </div>
        </div>
        <% }else{%>
        <div class="rmmg rmpd fontsty" id="footerTable">
<!--            <div class="col-xs-12" style="padding: 10px 0px 5px;">
                <center >Copyright &copy; 2009-15 <a href="http://www.progenbusiness.com" class="font-color" style="font-weight:bold;font-size:11px">Progen Business Solutions.</a> All Rights Reserved</center>
             </div>-->
             <div style='width:100%'>
                 <div style="margin-left:10%;width:45%;float:left">
                     Copyright &copy; 2009-15 <a href="http://www.progenbusiness.com" class="font-color" style="font-weight:bold;font-size:11px"><%=compTitle%>.</a> All Rights Reserved
                 </div>
                 <div style="margin-left:5%;width:40%;float:left">
                     Best Viewed on  <a  class="font-color"  href="https://www.mozilla.org/firefox/new/?scene=2#download-fx" data-direct-link="https://download.mozilla.org/?product=firefox-stub&amp;os=win&amp;lang=en-US" style="font-weight:bold;font-size:11px">Firefox 30 </a>,
                    <a class="font-color"   href="https://www.google.com/chrome/"> Chrome </a>and
                    <a class="mscom-link mscom-popup-link download-button dl font-color" href="https://www.microsoft.com/en-in/download/confirmation.aspx?id=40902" bi:track="false" bi:ea_action="dwn" bi:ea_offer="cnt" bi:cmpnm="download">IE 11 </a>& above 
                 </div>
                    
             </div>
        </div>
          <%} }%>
        
    </c:if>
<!--        <table id="footerTable" width="100%"  class="fontsty" >
                <tr style="height:10px;width:100%;max-height:100%;">
                    <td style="height:10px;width:100%">
                        <center >Copyright &copy; 2009-15 <a href="http://www.progenbusiness.com" class="font-color" style="font-weight:bold;font-size:11px">Progen Business Solutions.</a> All Rights Reserved</center>
                    </td>
                </tr>
                <tr style="height:10px;width:100%;max-height:100%;>
                    <td style="height:10px;width:100%">
                        <center >Best Viewed on  <a  class="font-color"  href="http://www.progenbusiness.com" style="font-weight:bold;font-size:11px">Firefox 30 , Chrome and IE 11 & above </a> </center>
                    </td>
                </tr>
            </table>-->
    </body>
</html>
