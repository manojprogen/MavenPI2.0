<%@page import="com.progen.i18n.TranslaterHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%--
    Document   : metadatahtml
    Created on : Nov 22, 2010, 3:36:34 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="prg.db.PbReturnObject" %>
<%@page import="java.util.*" %>
<%@page import="prg.db.PbDb" %>
<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            //added by Dinanath
             Locale cle = null;
             cle = (Locale) session.getAttribute("UserLocaleFormat");
              String userId="";
          userId = String.valueOf(request.getSession(false).getAttribute("USERID"));


             String itemList=null;
              if (request.getAttribute("itemList") != null) {
                        itemList = (String) request.getAttribute("itemList");
                        }
            String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

%>


<html>
    <head>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pi.js"></script>
        <title>piEE</title>


    </head>
    <body id="mainBody">

            <script>
       $(document).ready(function(){


            var data='<%=itemList%>'
                var length=bulidTable(data,'tableDataSorter','HtmlReports','<%=request.getContextPath()%>','<%=themeColor%>')
                $("#allSnaps").val(length);
                  options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=HtmlReports&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextSnapshot").autocomplete(options);
                $("#srchTextSnapshot").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });
       });

</script>
            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="left" width="25%">
                         <input type="text" class="myTextbox3" id="srchTextSnapshot" name="srchTextSnapshot" align="middle" style="width:280px;height:20px;">
                    <input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("search", cle)%>" onclick="displayStudioItem('srchTextSnapshot','<%=userId%>','HtmlReports','tableDataSorter','<%=request.getContextPath()%>')" style="width:50px;height:20px;" class="navtitle-hover"/>
                    </td>
                    <td align="right" width="38%">
                        <input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("delete", cle)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:deleteSnapShot('<%=request.getContextPath()%>')">
                        <input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("refresh", cle)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:refresh()">
                    </td>

                </tr>
            </table>

            <table align="center" id="tableDataSorter" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">


            </table>
                         <div id="pagerDataReport" class="pager" align="left" >
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allSnaps" value="">All</option>
                            </select>
                        </div>
         <form id="snapShotForm" name="snapShotForm"  method="post" style="width:98%" action="">
            <input id ="snapShotId" type="hidden" value="-1"/>
        </form>
    </body>
</html>



