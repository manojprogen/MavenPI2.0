<%-- 
    Document   : RealatedPortlets
    Created on : 7 Nov, 2011, 4:11:58 PM
    Author     : arun
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.List,com.progen.portal.*,com.google.common.collect.Iterables" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
 List<Portal> portals = (List<Portal>) session.getAttribute("PORTALS");
 Portal tempportal=null;
  tempportal = Iterables.find(portals, Portal.getAccessPortalPredicate(-2));
  List<PortLet> portlets=tempportal.getPortlets();
  String contextPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <%--<script  type="text/javascript"  language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>--%>

        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbPortletView.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/pbPortalTabViewerCSS.css"/>
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        
      <style type="text/css">
         table.tablesorter {       
        font-family: Verdana;
        font-size: 8pt;
        margin: 10px 0 15px;
        text-align: left;
        width: 100%;
       
       }
       th {
            background: none repeat scroll 0 0 #888888;
            color: black;
            padding: 0;
        }
        table tr th {
            background-color: #B4D9EE;
            padding: 3px;
        }
        tbody{
            border: 1px solid
}

        </style>
    </head>
    <body>
        <form action=""  name="relatedPortletsForm" id="relatedPortletsForm" method="post">
            <input type="hidden" name="currportletId" id="currportletId" value="">

            <table  style="height:100%;min-width:1350px;max-width:100%">
                <tr valign="top">
                </tr>
                <tr valign="top">
                    <td valign="top" width="100%">
                        <div class="column" id="divPortlet"  style="width:1350px;height:100%;">
                            <table>
                           <%
                             int i=1;
                            for(PortLet portLet:portlets){
                               %>
                               <%if(i%2!=0){%>
                               <tr valign="top">
                                   <%}%>
                         <td><div  id="related_<%=portLet.getPortLetId()%>" class="myDragTabs portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" style="width:420px;max-height:<%=portLet.getPortletHeight()-150%>px;min-height:420px ">
                        <div class=\"portlet-header portletHeader ui-corner-all  <%=portLet.getPortLetName()%></div>
                        <script type='text/javascript'>
                        getRelatedPortletDetails('<%=portLet.getPortLetId() %>','','','<%=portLet.getSortOrder()%>','','-2','')
                       </script>
                        </div></td>

                          <%i++;%>
                          <%}%>
                           <%if(i%2!=0){%>
                                </tr>
                             <%}%>
                            </table></div>
                    </td>
                </tr>
            </table>
        </form>
                            <script type="text/javascript">
            function getRelatedPortletDetails(portletId,REP,CEP,perBy,gpType,portalTabId,date){
              var relatedID="related_"+portletId
            $('#'+relatedID).html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:relative;top:120px;left:30px" ></center>');

          $.ajax({
                url: 'portalViewer.do?portalBy=viewPortlet&PORTLETID='+portletId+'&REP='+REP+'&CEP='+CEP+'&perBy='+perBy+'&gpType='+gpType+'&portalTabId='+portalTabId+'&currDate='+date,
                success: function(data){
                    <%--alert("data\t"+data)--%>
                   $('#'+relatedID).html(data);
                   $('.column').sortable({
                connectWith: '.column',
                helper:"clone",
                effect:["", "fade"],
                stop: function(event, ui) {


                }
            });
            var tabID="tablesorter"+portletId+"-"+portalTabId
            $("#"+tabID).tablesorter({headers : {0:{sorter:false}}});

        }
    });


}

        </script>
    </body>
</html>
