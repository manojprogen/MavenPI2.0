<%-- 
    Document   : addportletsFromExisting
    Created on : 26 Apr, 2011, 10:31:21 AM
    Author     : progen
--%>
<%
String portalID=request.getParameter("portalID");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />               
<script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>        
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=request.getContextPath()%>/TableDisplay/overlib.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
<script type="text/javascript">
    var PortletID=""
             $(document).ready(function(){
                 $.get("<%= request.getContextPath()%>/portalTemplateAction.do?paramportal=getAllPortlets&crtPortId=<%=portalID%>",function(data){
                     //alert("data\t"+data)
                     if(trim(data)!=""){
                         $("#portlets").html(data)
                         $("#portlets").treeview({
                            collapsed: true
                            });
                         $("#portlets > li > span").attr("class","folder")
                         $("#portlets > li > ul > li > span").attr("class","file")
                         $('ul#portlets li').quicksearch({
                             position: 'before',
                             attached: 'ul#portlets',
                             loaderText: '',
                            delay: 100
                            });

                     }
                 });

                 

             });
            
             function LTrim( value ) {

                 var re = /\s*((\S+\s*)*)/;
                 return value.replace(re, "$1");

             }

             // Removes ending whitespaces
             function RTrim( value ) {

                 var re = /((\s*\S+)*)\s*/;
                 return value.replace(re, "$1");

             }

             // Removes leading and ending whitespaces
             function trim( value ) {

                 return LTrim(RTrim(value));
             }
             function buildPortletPreview(Portletid)
             {
                 //alert("Portletid\t"+Portletid)

                 PortletID=Portletid
                 //alert("PortletID\t"+PortletID)
                 $.ajax({
                     url:"<%=request.getContextPath()%>/portalViewer.do?portalBy=portletPreview&Portletid="+Portletid+"&portalID=-1",
                     success:function(data){
                       $("#portletPreviewDiv").html("")
                       $("#portletPreviewDiv").html(data)

                     }
                 });
             }
             function assignPortlet(){
            // alert("Portletid\t"+PortletID)
              var portalId='<%=portalID%>'
              var portletHeight=$("#portletHeight").val()
                  $.post("<%=request.getContextPath()%>/portalViewer.do?portalBy=portletAssign&Portletid="+PortletID+"&portalId="+portalId+"&portletHeight="+portletHeight,function(data){
                      var datavar=data.split("~")
                 if(datavar[0]=="true"){
                     parent.document.forms.frmParameter.action="pbPortalViewer.jsp#"+trim(datavar[1].replace(" ","_","gi"))
                     parent.document.forms.frmParameter.submit()
                 }else{
                     alert("Portlet already assigned.")
                 }
                   
              });
              

             }
</script>
<style type="text/css">
    #portlets {
        font-family: Verdana, helvetica, arial, sans-serif;
        font-size: 100%;
    }
</style>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form id="addPortletsForm" name="addPortletsForm" method="post" action="">
            <table align="center" border="solid black 1px">
                <tr>
                    <td width="40%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Portlets</font></div>
                        <div class="masterDiv" style="height:280px;width:300px;overflow:auto;">
                            <ul id="portlets" class="filetree">

                            </ul>
                        </div>
                    </td>
                    <td width="60%" valign="top"  id="draggableGraphs" valign="top">
                        <h3 style="height:20px" align="left"    class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Portlet Preview</font>
                        </h3>
                        <div id="portletPreviewDiv" style="height:280px;width:500px;overflow:auto;">

                        </div>
                    </td>
                </tr>
            </table><br/>
<!--            <table align="center">
                <tr><td><font style="font-size: 14px;"> Portlet height:</font> </td>
                    <td>
                        <select id="portletHeight" name="portletHeight">
                            <option value="400" selected>400px</option>
                        <option value="800">800px</option>
                        </select>
                    </td>
                </tr>
            </table><br/>-->
            <table align="center">
                <tr><td><input type="button" name="save" value="Save" onclick="assignPortlet()" class="navtitle-hover" style="width:100px"> </td></tr>
            </table>
        </form>
    </body>
</html>
