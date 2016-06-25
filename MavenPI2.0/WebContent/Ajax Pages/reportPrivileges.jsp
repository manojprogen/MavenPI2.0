<%--
    Document   : reportPrivilages
    Created on : Dec 22, 2009, 3:18:21 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList;" %>


<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script>
            function saveRepPriv(){
                var userId;
                var chkusersobj = parent.document.forms.myForm.chkusers;
                for(var i=0;i<chkusersobj.length;i++){
                    if(chkusersobj[i].checked){
                        userId=chkusersobj[i].value;
                    }
                }

                var FavouriteLinks = document.getElementById("linkCheck").checked;
                var ComposeMessage = document.getElementById("composeCheck").checked;
                var TopBottom = document.getElementById("topCheck").checked;
                var CustomizeReport = document.getElementById("customCheck").checked;
                var SnapShot = document.getElementById("snapCheck").checked;
                var Scheduler = document.getElementById("schedCheck").checked;
                var MessgTab = document.getElementById("messageCheck").checked;
                var Tracker = document.getElementById("trakCheck").checked;
                var BizRole = document.getElementById("bizRoleCheck").checked;
                var StickyNote = document.getElementById("stickyNoteCheck").checked;
                var edit = document.getElementById("editCheck").checked;
                var customDrill = document.getElementById("customDrillCheck").checked;
                var overwriteReport = document.getElementById("overwriteRepCheck").checked;

                if(FavouriteLinks == true)
                {
                    var flink=document.getElementById("linkCheck").value //= document.getElementById("linkCheck").value;
                }
                if(ComposeMessage == true)
                {
                    var cmsg=document.getElementById("composeCheck").value //= document.getElementById("composeCheck").value;
                }
                if(TopBottom == true)
                {
                    var topbot=document.getElementById("topCheck").value //= document.getElementById("topCheck").value;
                }
                if(CustomizeReport == true)
                {
                    var custrep=document.getElementById("customCheck").value //= document.getElementById("customCheck").value;
                }
                if(SnapShot == true)
                {
                    var snap=document.getElementById("snapCheck").value //= document.getElementById("snapCheck").value;
                }
                if(Scheduler == true)
                {
                    var sch=document.getElementById("schedCheck").value //= document.getElementById("schedCheck").value;
                }
                if(MessgTab == true)
                {
                    var msgtab=document.getElementById("messageCheck").value //= document.getElementById("messageCheck").value;
                }
                if(Tracker == true)
                {
                    var track=document.getElementById("trakCheck").value //= document.getElementById("trakCheck").value;
                }
                if(BizRole == true)
                {
                    var bRole=document.getElementById("bizRoleCheck").value //= document.getElementById("trakCheck").value;
                }
                if(StickyNote == true)
                {
                    var sticky=document.getElementById("stickyNoteCheck").value //= document.getElementById("trakCheck").value;
                }
                if(edit == true)
                {
                    var edit=document.getElementById("editCheck").value //= document.getElementById("trakCheck").value;
                }
                if(customDrill == true)
                {
                    var custDrill=document.getElementById("customDrillCheck").value //= document.getElementById("trakCheck").value;
                }
                if(overwriteReport == true)
                {
                    var overwriteRep=document.getElementById("overwriteRepCheck").value //= document.getElementById("trakCheck").value;
                }
                if(FavouriteLinks == false && ComposeMessage == false && TopBottom == false && CustomizeReport == false && SnapShot == false && Scheduler == false && MessgTab ==false && Tracker == false && BizRole == false && StickyNote == false && edit == false && customDrill == false && overwriteReport == false)
                {
                    alert('Please Select Atleast One Previlage')
                }else{
                    document.forms.RepPriForm.action="savePrivilages.do?method=saveRepPrevilages&userId="+userId+"&flink="+flink+"&cmsg="+cmsg+"&topbot="+topbot+"&custrep="+custrep+"&snap="+snap+"&sch="+sch+"&msgtab="+msgtab+"&track="+track+"&bRole="+bRole+"&sticky="+sticky+"&edit="+edit+"&custDrill="+custDrill+"&overwriteRep="+overwriteRep;
                    document.forms.RepPriForm.submit();
                    parent.cancelUserReportPreveliges();
                }
            }
        </script>
         <style>
            *{
                font:11px verdana
            }
        </style>
    </head>
    <body>
        <form name="RepPriForm" action="" method="post">
            <%
          //  ArrayList UserReportPrevileges = new ArrayList();
          //  UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
            ////.println("UserReportPrevileges is " + UserReportPrevileges);

            %>
            <table width="100%" border="0px" >
                <tr style="width:100%">
                    <td width="50%">
                        
            
                        <input type="checkbox" id="linkCheck" value="Favourite Links" checked >&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Favourite Links</b>


                        <input type="checkbox" id="linkCheck" value="Favourite Links">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Favourite Links</b>
                      
                    </td>
                <input type="hidden" name="linkCheck" id="linkCheck" >
                <td width="50%">
                  
            
                    <input type="checkbox" id="composeCheck" value="Compose Message" checked >&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Compose Message</b>
                   
                    <input type="checkbox" id="composeCheck" value="Compose Message">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Compose Message</b>
                  
                </td>
                <input type="hidden" name="composeCheck" id="composeCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                    
                        <input type="checkbox" id="topCheck" value="TopBottom" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Top/Bottom</b>
                       
                        <input type="checkbox" id="topCheck" value="TopBottom">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Top/Bottom</b>
                       
                    </td>
                <input type="hidden" name="topCheck" id="topCheck" >
                <td width="50%">
            
                    <input type="checkbox" id="customCheck" value="Customize Report" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Customize Report</b>
                   
                    <input type="checkbox" id="customCheck" value="Customize Report">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Customize Report</b>
                   
                </td>
                <input type="hidden" name="customCheck" id="customCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                     
                        <input type="checkbox" id="snapCheck" value="SnapShot" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">SnapShot</b>
                        
                        <input type="checkbox" id="snapCheck" value="SnapShot">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">SnapShot</b>
                       
                    </td>
                <input type="hidden" name="snapCheck" id="snapCheck" >
                <td width="50%">
                   
                    <input type="checkbox" id="schedCheck" value="Scheduler" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Scheduler</b>

                    <input type="checkbox" id="schedCheck" value="Scheduler">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Scheduler</b>
                    <input type="hidden" name="schedCheck" id="schedCheck" >
                    </tr>
                <tr style="width:100%">
                    <td width="50%">
                        <input type="checkbox" id="messageCheck" value="MessgTab" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                        <input type="checkbox" id="messageCheck" value="MessgTab">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Messages</b>
                     </td>
                <input type="hidden" name="messageCheck" id="messageCheck">
                <td width="50%">
                   
                    <input type="checkbox" id="trakCheck" value="Tracker">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Tracker</b>
                   
                </td>
                <input type="hidden" name="trakCheck" id="trakCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                       
            
                        <input type="checkbox" id="bizRoleCheck" value="BizRoles" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                       
                        <input type="checkbox" id="bizRoleCheck" value="BizRoles">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Business Roles</b>
                       
                    </td>
                <input type="hidden" name="bizRoleCheck" id="bizRoleCheck" >
                <td width="50%">
                   
                    <input type="checkbox" id="stickyNoteCheck" value="StickyNote" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Sticky Notes</b>
                   
                    <input type="checkbox" id="stickyNoteCheck" value="StickyNote">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Sticky Notes</b>
                   
                </td>
                <input type="hidden" name="stickyNoteCheck" id="stickyNoteCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                        
           
                        <input type="checkbox" id="editCheck" value="Save as New Report" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Save as New Report</b>
                        
                        <input type="checkbox" id="editCheck" value="Save as New Report">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Save as New Report</b>
                       
                    </td>
                <input type="hidden" name="editCheck" id="editCheck" >
                <td width="50%">
             
                        <input type="checkbox" id="customDrillCheck" value="Customize Drill Down" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Customize Drill Down</b>
                        
                        <input type="checkbox" id="customDrillCheck" value="Customize Drill Down">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Customize Drill Down</b>
                       
                    </td>
                <input type="hidden" name="customDrillCheck" id="customDrillCheck" >
                </tr>
                <tr style="width:100%">
                    <td width="50%">
                       
                        <input type="checkbox" id="overwriteRepCheck" value="Overwrite Report" checked>&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Overwrite Report</b>
                       
                        <input type="checkbox" id="overwriteRepCheck" value="Overwrite Report">&nbsp<b style="font-family:Verdana;font-size:12px;color:#369">Overwrite Report</b>
                      
                    </td>
                <input type="hidden" name="overwriteRepCheck" id="overwriteRepCheck" >
                <input type="hidden" name="userId" id="userId" >
                </tr>

                <tr style="width:100%" align="center" >
                <table align="center"  width="100%">
                    <tr>
                        <td width="50%" align="center">
                            <center>
                                <input type="button" class="navtitle-hover" value="Save" onclick="saveRepPriv()">                            
                            </center>
                        </td>
                    </tr></table>
                </tr>
            </table>
        </form>
    </body>
</html>
