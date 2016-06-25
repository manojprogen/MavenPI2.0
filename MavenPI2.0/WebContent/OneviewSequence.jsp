<%-- 
    Document   : OneviewSequence
    Created on : Nov 22, 2012, 3:33:05 PM
    Author     : anitha.pallothu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.action.UserStatusHelper,utils.db.ProgenParam,java.util.ListIterator,com.progen.timesetup.CalenderFormTable,java.util.Locale"%>
<%@page import="java.util.Iterator,java.util.HashSet,java.util.Set,java.util.HashMap,java.text.SimpleDateFormat,java.util.Calendar,java.util.Date,java.util.ArrayList,java.util.List,java.awt.*"%>
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
    List<String> viewByIds = new ArrayList<String>();
            List<String> viewByNames = new ArrayList<String>();
            List<String> createdDates = new ArrayList<String>();
            List<String> createdBy = new ArrayList<String>();
            List<String> modifiedDates = new ArrayList<String>();
            List<String> modifiedBy = new ArrayList<String>();
            List<String> busRoleIDs = new ArrayList<String>();
            List<String> busRoleNames = new ArrayList<String>();
            List<String> viewroleIds = new ArrayList<String>();
            List<String> viewroleNames = new ArrayList<String>();
            
            String htmljson = (String)request.getAttribute("htmlJson");
            viewByIds = (List<String>) request.getAttribute("ViewByIds");
            viewByNames = (List<String>) request.getAttribute("viewByNames");
            createdDates = (List<String>) request.getAttribute("createdDates");
            createdBy = (List<String>) request.getAttribute("createdBy");
            modifiedDates = (List<String>) request.getAttribute("modifiedDates");
            modifiedBy = (List<String>) request.getAttribute("modifiedBy");
            busRoleIDs = (List<String>) request.getAttribute("BusRoleIds");
            busRoleNames = (List<String>) request.getAttribute("BusRoleNames");           
            viewroleIds = (List<String>) request.getAttribute("viewroleIds");
            viewroleNames = (List<String>) request.getAttribute("viewroleNames");
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
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
        
    </head>
    <body onload="getoneViewSequence()">
        <div id="dragAndDropDiv" style="overflow:auto" >
            
            </div>
        <center>
            <input type="button"  class="navtitle-hover" style="width:auto"  value="Done" onclick="saveSequence()">
        </center>
        <script type="text/javascript">           
//            $(document).ready(function(){
                function getoneViewSequence(){
                var htmlVar = "";
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getOneViewSequence',
                    success: function(data){ 
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
                    }
                    });  
                }                    
                function saveSequence(){
                    parent.$("#sequenceDiv").dialog('close'); 
                var ul = document.getElementById("sortable");
                var userIdArray = new Array();
                var colIds;
                if(ul!=undefined || ul!=null){
                     colIds=ul.getElementsByTagName("li");                     
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){                           
                            userIdArray.push(colIds[i].id.split("_")[0]);                            
                        }
                    }
                }                 
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=saveViewSequence&userIdArray='+userIdArray,
                    success: function(data){ 
                        if(data == 'true'){
                            if(userIdArray.length>7){
                                alert("Sequence is Saved Successfully. Only 7 oneviews can be displayed on the tab");
                            }else if(userIdArray.length>0){
                                alert("Sequence is Saved Successfully")
                            }else{
                                alert("Tab will follow display sequence of oneviews in the oneview home page")
                            }
                            parent.home();
                        }
                    }
                });
                }
            
        </script>
    </body>
</html>
