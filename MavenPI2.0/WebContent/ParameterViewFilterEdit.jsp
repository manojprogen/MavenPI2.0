<%-- 
    Document   : ParameterViewFilterEdit
    Created on : 8 Feb, 2013, 3:37:22 PM
    Author     : anitha.pallothu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="java.util.ListIterator"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.progen.timesetup.CalenderFormTable"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="java.awt.*;"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    String themeColor = "blue";
            Locale locale = null;
            //          String fromReport=String.valueOf(request.getAttribute("fromReport"));
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String reportId = String.valueOf(request.getParameter("reportId"));
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
        <style type="text/css">

            * {
                font-family: verdana;
                font-size: 11px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                font-weight: normal;
                line-height: normal;
            }
        </style>


        <title><bean:message key="ProGen.Title"/></title>
        <script type="text/javascript">
            $(document).ready(function(){
                $.post('<%=request.getContextPath() %>'+'/reportViewer.do?reportBy=getViewFilters&REPORTID='+<%=reportId%>,
             function(data){
                 var jsonVar = eval('('+data+')');
                        //parent.$("#sequenceDiv").html("")


                var ul = document.getElementById("sortable");
                $("#dragAndDropDiv").html("")
                 $("#dragAndDropDiv").html(jsonVar.htmlStr)
                 isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
//                htmlVar += "<table><tr><td><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\"  ></td></tr></table>";
//                parent.$("#sequenceDiv").html(htmlVar);

                $("#myList3").treeview({
                            animated:"slow",
                            persist: "cookie"
                        });

                        $('ul#myList3 li').quicksearch({
                            position: 'before',
                            attached: 'ul#myList3',
                            loaderText: '',
                            delay: 100
                        })

                $(".myDragTabs").draggable({
                            helper:"clone",
                            effect:["", "fade"]
                        });


                $("#dropTabs").droppable({
                            activeClass:"blueBorder",
                            accept:'.myDragTabs',
                            drop: function(ev, ui) {
                                createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                            }
                        }
            );
                grpColArray=jsonVar.memberValues
                $(".sortable").sortable();
             });
            });
            function saveRequiredParameters(){
                parent.$("#viewFilterEdit").dialog('close');
                var ul = document.getElementById("sortable");
                var paramIdsArray = new Array();
                var colIds;
                if(ul!=undefined || ul!=null){
                     colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            paramIdsArray.push(colIds[i].id.split("_")[0]);
                        }
                    }
                }
                var confirmDel=confirm("Do you Want Save Selected Parameters Permanently");
                    if(confirmDel==true){
                $.ajax({
                    url: 'reportViewer.do?reportBy=saveViewFilters&REPORTID='+<%=reportId%>+'&paramIdsArray='+paramIdsArray,
                    success: function(data){
                if(data == "true"){
                    alert("Selected Parameters Saved Successfully")
                    var path='<%=request.getContextPath()%>'+"/reportViewer.do?reportBy=viewReport&REPORTID="+<%=reportId%>+"&action=reset";
                    submiturls1(path);
                }else{
                    alert("Selected Parameters not Saved Successfully")
                    }
            }
                });
            }
            }
            function submiturls1($ch){
                parent.document.frmParameter.action = $ch;
                parent.document.frmParameter.submit();
            }
            function moveAllParameters(){
//    alert("moveAllFromsortable");
//    alert(parent.grpColArray +' Type of::'+typeof(parent.grpColArray));
    $("span.myDragTabs").each(function(){
        var elemId=$(this).attr('id');
        var elemName=$(this).html();
        if($.inArray(elemId, grpColArray)== -1){
            createColumn(elemId,elemName,"sortable");
        }
    });
}
            </script>
    </head>
    <body>
        <div id="dragAndDropDiv" style="overflow:auto" >

            </div>
        <table>
            <tr>
                <td>
                   * Note: Parameters shown in the right hand side will no longer be available for 'Edit ViewBy'. They can be used only applying for parameter filters. Please press Done For saving changes.
                </td>
            </tr>
        </table>
        <center>
            <input type="button"  class="navtitle-hover" style="width:auto"  value="Move All" onclick="moveAllParameters()">
            <input type="button"  class="navtitle-hover" style="width:auto"  value="Done" onclick="saveRequiredParameters()">
        </center>
    </body>
</html>
