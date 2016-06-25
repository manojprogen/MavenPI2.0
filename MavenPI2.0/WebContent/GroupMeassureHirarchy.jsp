<%-- 
    Document   : GroupMeassureHirarchy
    Created on : 14 Jul, 2012, 1:11:32 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            if (session.getAttribute("USERID") == null || request.getSession(false) == null) {
        response.sendRedirect(request.getContextPath() + "/baseAction.do?param=logoutApplication");
    } else {
            String themeColor = "";

            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String DefaultArrregations[] = {"--", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
            String userId = "";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
   
         <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />

        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>

        
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script  type="text/javascript"  language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
         <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/blue/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

         <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>


        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>





        <style type="text/css" >
                

        </style>
        
        <script type="text/javascript">
              $(document).ready(function(){
                if($.browser.msie== true)
                {
                    $("#createGroup").dialog({
                        autoOpen: false,
                        height: 300,
                        width:400,
                        position: 'justify',
                        modal: true
                    });
                    $("#dragandDrop").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                }
                else
                {
                    $("#createGroup").dialog({
                        autoOpen: false,
                        height:300,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                    $("#dragandDrop").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });

                }

                $.get("userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                    $("#connections").html(data)
                });

                ////                    $("#groupList").treeview({
                ////                    animated:"slow",
                ////                    persist: "cookie"
                //                });
                


            });
        </script>
       
        <script>
            $(function() {
                $("#groupList").treeview({
                    animated: "normal",
                    unique:true
                });
            });
       </script>
    </head>
    <body>
        <table style="width:100%;">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
                <table valign="left" border="1" style="width: 100%; height: 100%">
                    <tbody><tr><td align="left" border="1" style="width: 30%; height: 100%">
        <table style="width: 100%; height: 100%" align="top">
            <tbody>
                <tr>
                    <td valign="top"><div class="navtitle-hover" style="height: 33px;">
                            <font size="1px" face="verdana" style="font-weight: bold">Group Measure Hierarchy</font>
                            <table align="right">
                                <tbody>
                                    <tr>
                                        <td><a href="javascript:void(0)" onclick="refreshPage()"><img src="/pi/icons pinvoke/refreshPage.png"</a></td>
                                    </tr>
                                </tbody>
                            </table>

                        </div>
                        <div style="height: 553px ; overflow-y:auto">
                            <ul id="mylist" class="filetree">
                                <li class="closed collapsable lastCollapsable">
                                    <img src="images/treeViewImages/bricks.png"/><span class="" onclick="createGroup()"><font size="2px" face="verdana" style="">Create Group</font></span></li>
                            </ul>
                            <ul id="groupList" class="filetree treeview">
                                <li class='closed' style="background-image:url('images/treeViewImages/plus.gif')">
                                    <img src="images/treeViewImages/bricks.png"/><span class="ViewGroups" onclick="ViewGroups()"><font size="2px" face="verdana" style="">View Groups</font></span>
                                    <div id="showGroups" style="display: none"></div>
                                </li>

                            </ul>
                        </div>
                    </td>

                </tr>

            </tbody>
        </table>
                            </td><td/></tr></tbody></table>
        <div id="createGroup" style="display: none;" title="Create Group">
            <table style="width: 100%">
                <tbody>
                    <tr>

                        <!--                    <td>:</td>-->
                        <td><label class="lebel"><b> Connection&nbsp;&nbsp;&nbsp:&nbsp;&nbsp;&nbsp;</b></label></td><td><select name="connections" id="connections" onchange="getFolderDetails()">
                            </select>
                        </td>
                    <tr>
                        <!--                    <td>:</td>-->
                        <td><label class="lebel"><b>Roles&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp:&nbsp;&nbsp;&nbsp;</b></label></td><td><select name="foldersSelect" id="foldersSelect" ></select> </td>

                    </tr>
                    <tr>

                        <td><label class="lebel"><b> Group Name:&nbsp;&nbsp;&nbsp&nbsp;&nbsp;</b></label></td>
                        <td><input id="groupName" type="text" size="30" name="groupName"/>
                        </td>
                    </tr><tr/>
                    <tr><td/><td align="left" width="20%" valign="top">
                            <input type="button" class="navtitle-hover" onclick="saveGroupName()" value="save" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        <ul id="dragParentElemnts" style="display: none">
            <li><a href="#addParents" >Add/Edit Parents</a></li>
<!--            <li><a href="#editParents" >Edit Parents</a></li>-->
        </ul>
        <ul id="addChilds" style="display: none">
            <li><a href="#addChilds" >Add/Edit Childs</a></li>
<!--            <li><a href="#editChilds" >Edit Childs</a></li>-->
        </ul>
        <div style="display:none" id="dragandDrop" title="GroupMeassureHirarchy">
            <iframe  id="dragandDropFrame" NAME='dragandDropFrame' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
        </div>
 <table style="width:100%;" id="footID">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                <div id="confirmartionDiv" style="display: none"></div>
                <div id="test" style="display: none"></div>
                 <script type="text/javascript">
          
          

            function getFolderDetails(){
                var connectionID= $("#connections").val()

                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                    $("#foldersSelect").html(data)
                });
            }
            function createGroup()
            {
                $("#createGroup").dialog('open');

            }
            function ViewGroups()
            {
                //                alert('View Groups Method');
                if($("#showGroups").is(':visible'))
                        {
                            $("#showGroups").hide();
                        }
                        else{
                var url="GroupMeassureCreation.do?templateParam=viewGroup";
                $.post(url,function(data){
                    $("#showGroups").html(data);
                    
//                    alert('is hidden:'+$("#showGroups").is(':hidden'))
                    $("#showGroups").show();
//                     alert($("#showGroups").is(':visible'))
                    $(".groupName").contextMenu({
                        menu: 'dragParentElemnts',
                        leftButton: true
                    }, function(action, el, pos) {
                        dragElements(action, el, pos);
                    });
                    
                });
            }
             }
            function saveGroupName()
            {
                var groupName= $("#groupName").val();
                var folderId= $("#foldersSelect").val();
                if(groupName=="" || groupName==null)
                {
                    alert("Please Write Group Name...!!");
                }
                else
                {
                    $("#createGroup").dialog('close');
                    url="GroupMeassureCreation.do?templateParam=saveGroup&groupName="+groupName+"&folderId="+folderId;
                    $.get(url,function(data){
//                        alert(data);
                        $("#confirmartionDiv").html(data);

                    });

                }
                
            }
            function dragElements(action,el,pos)
            {
                switch(action)
                {
                    case "addParents":
                        {
                            //                            alert('Possition:'+pos)


                            //                alert('action'+action+'el'+el.attr('id'));
//                            alert('Li Id:'+$("li").attr('id'))
                            $("#dragandDrop").dialog('open');
                            var combId=el.attr('id');
                            var ids=combId.split(",");
                            var folderId=ids[0].substr(4);
                            var groupId=ids[1].substr(4);
                            //                        var grpName=ids[2].substr(4);
//                            alert('folderId '+folderId+' groupId: '+groupId+' groupName:');
                            var frameObj = document.getElementById("dragandDropFrame");
                            frameObj.src ="DefineGroupHirarchy.jsp?folderIds="+folderId+"&loadDialogs=true&groupId="+groupId;
                            break;
                        }
                    case "addChilds":
                        {
//                            alert('Li Id:'+$("li").attr('id'));
                            $("#dragandDrop").dialog('open');
                            var combId=el.attr('id');
                            var ids=combId.split(",");
                            var parentId=ids[0].substr(4);
                            var groupId=ids[1].substr(4);
                            var folderId=ids[2].substr(4);
//                            alert('parentId '+folderId+' groupId: '+groupId+' groupName:');
                            var frameObj = document.getElementById("dragandDropFrame");
                            frameObj.src ="DefineGroupHirarchy.jsp?parentId="+parentId+"&loadDialogs=true&groupId="+groupId+"&folderIds="+folderId;
                            break;
                        }
                }
                

            }
            function showChilds(parentId,groupId,folderId)
            {
              if($("#par_"+parentId+"_"+groupId).is(':visible'))
                    {
                        $("#par_"+parentId+"_"+groupId).hide();
                    }
                 else{

//                  alert('showParents');
                var url="GroupMeassureCreation.do?templateParam=showChilds&groupId="+groupId+"&parentId="+parentId+"&folderId="+folderId;
                $.post(url,function(data){
//                    alert('Before: '+$("#div_"+parentId+"_"+groupId).attr("display"));
                    $("#par_"+parentId+"_"+groupId).html(data);
                    $("#par_"+parentId+"_"+groupId).show();
//                    $("#test").show();
//                    alert('After: '+$("#par_"+parentId+"_"+groupId).attr("initialized"));
                    $(".parentName").contextMenu({
                        menu: 'addChilds',
                        leftButton: true
                    }, function(action, el, pos) {
                        dragElements(action, el, pos);
                    });

                });
            }
            }
            function showParents(groupId)
            {
//                alert('showParents');
                if($("#grp_"+groupId).is(':visible'))
                    {
                        $("#grp_"+groupId).hide();
                    }
                 else{
                var url="GroupMeassureCreation.do?templateParam=showParents&groupId="+groupId;
                $.post(url,function(data){
                    $("#grp_"+groupId).html(data);
                    $("#grp_"+groupId).show();
                    $(".parentName").contextMenu({
                        menu: 'addChilds',
                        leftButton: true
                    }, function(action, el, pos) {
                        dragElements(action, el, pos);
                    });

                });
                 }

            }
            function refreshPage()
            {
                window.location.href=window.location.href;
            }
        </script>
    </body>
</html>
<%}%>
