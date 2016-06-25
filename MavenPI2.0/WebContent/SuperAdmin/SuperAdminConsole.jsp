<%-- 
    Document   : SuperAdminConsole
    Created on : Oct 15, 2010, 10:47:09 AM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Super Admin Console</title>
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <style type="text/css">
            .subTotalCell
            {
/*                background-color: #B4D9EE;*/
                color:#336699;
                font-family: verdana;
                text-align: left;
                font-size:11px;
                font-weight:bolder
            }
            .dimensionCell
            {
/*                background-color: rgb(230, 230, 230);*/
                color:#336699;
                font-family: verdana;
                text-align: left;
                font-size:11px
            }
        </style>
        <script type="text/javascript">
            
            $(document).ready(function(){
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=viewSuperAdmin',
                    success: function(data){
//                        var dataJson=eval("("+data+")")
//                        var table="<table border='1' >"
//                        table=table+"<tr ><th class='navtitle-hover'></th><th class='navtitle-hover'><font style='font-weight:bold;'>Modules</font></th><th class='navtitle-hover'><font style='font-weight:bold;'>Licenses</font></th><th class='navtitle-hover'><font style='font-weight:bold';>Module Components</font></th></tr>"
//                        for(var i=0;i<dataJson.ModuleCodes.length;i++){
//                            table=table+"<tr><td>"
//                            table=table+"<input type='checkbox' "+dataJson.ModuleStatus[i]+" id="+dataJson.ModuleCodes[i]+" onclick=openTextBox('"+dataJson.ModuleCodes[i]+"') >"
//                            table=table+"</td><td><b>"+dataJson.ModuleNames[i]+"</b></td>"
//                            if(dataJson.ComponentsAvail[i]=="false" && dataJson.ModuleStatus[i]=="checked" ){
//                               table=table+"<td style='' id=tdTxt"+dataJson.ModuleCodes[i]+"><input type='text' style='width:35px' id=txt"+dataJson.ModuleCodes[i]+" name=txt"+dataJson.ModuleCodes[i]+" value="+dataJson.ModuleLicenses[i]+"></td></tr>"
//                            }else
//                            if(dataJson.ModuleStatus[i]=="checked"){
//                               table=table+"<td style='' id='tdTxt"+dataJson.ModuleCodes[i]+"'><input type='text' style='width:35px' id=txt"+dataJson.ModuleCodes[i]+" name=txt"+dataJson.ModuleCodes[i]+" value="+dataJson.ModuleLicenses[i]+"></td>"
//                               table=table+"<td style='' id='tdBut"+dataJson.ModuleCodes[i]+"'><input type='button' class='navtitle-hover' id=button"+dataJson.ModuleCodes[i]+" onclick=openListDialog('"+dataJson.ModuleCodes[i]+"')  value='Components' ></td></tr>"
//                            }else{
//                            table=table+"<td style='display:none' id='tdTxt"+dataJson.ModuleCodes[i]+"'><input type='text' style='width:35px' id=txt"+dataJson.ModuleCodes[i]+" name=txt"+dataJson.ModuleCodes[i]+"></td>"
//                            table=table+"<td style='display:none' id='tdBut"+dataJson.ModuleCodes[i]+"'><input type='button' class='navtitle-hover' id=button"+dataJson.ModuleCodes[i]+" onclick=openListDialog('"+dataJson.ModuleCodes[i]+"')  value='Components' ></td></tr>"
//                            }
//                        }
//                        table=table+"</table><br><table align='center'><tr><td><input type='button' class='navtitle-hover' value='Save' onclick='generateXmlForSuperAdmin()'></td></tr></table>"
//                        $("#superAdminDiv").html(table);
                         $("#superAdminDiv").html($("#superAdminDiv").html()+data)
                          $("#myList").treeview({
                                animated:"slow"
                                //persist: "cookie"
                            });
                    }
                });


                $("#compListDiv").dialog({
                    bgiframe: true,
                    autoOpen: false,
                    height: 500,
                    width:300,
                    modal: true,
                    Cancel: function() {
                        $(this).dialog('close');
                    }
                });
             
            });


            function openTextBox(chkBoxId){
                //                alert("chkBoxId--"+chkBoxId)
                var chkObj=document.getElementById(chkBoxId);
                var chkStatus=false;

                if(chkObj.checked){
                    chkStatus=true;
                    document.getElementById("tdTxt"+chkBoxId).style.display="";
                    if(document.getElementById("tdBut"+chkBoxId)!=null && document.getElementById("tdBut"+chkBoxId)!=undefined)
                    document.getElementById("tdBut"+chkBoxId).style.display="";
                }
                if(!chkObj.checked){
                    chkStatus=false;
                    document.getElementById("tdTxt"+chkBoxId).style.display="none";
                    if(document.getElementById("tdBut"+chkBoxId)!=null && document.getElementById("tdBut"+chkBoxId)!=undefined)
                    document.getElementById("tdBut"+chkBoxId).style.display="none";
                }

                $.ajax({
                    url: 'superAdminAction.do?superAdminParam=enableDisableModule&from=SuperAdmin&moduleCode='+chkBoxId+'&status='+chkStatus,
                    success: function(data){
                    }
                });

            }
  

            function openListDialog(moduleCode){
                var frameObj=document.getElementById("compListFrame");
                frameObj.src="<%=request.getContextPath()%>/SuperAdmin/showComponentsList.jsp?moduleCode="+moduleCode;
                $('#compListDiv').dialog('open');

            }

            function generateXmlForSuperAdmin(){
                $.post("superAdminAction.do?superAdminParam=generateXmlForSuperAdmin", $("#superAdminForm").serialize() ,
                function(data){
                    alert("Privileges Saved Successfully")
                    document.forms.superAdminForm.action="<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                    document.forms.superAdminForm.submit();
                });
            }

            function closeDialog(){
                $('#compListDiv').dialog('close');
            }
            function closeChildDialog(){
               $('#childListDiv').dialog('close');
            }
            function enableAllComponents(id)
            {
                var parentLIObj=document.getElementById(id);
                var componentChkObj=document.getElementsByName(id+"~Component");
                var childComObj=document.getElementsByName(id+"~ChildComponent");
                if(parentLIObj.checked)
                {
                    for(var i=0;i<componentChkObj.length;i++)
                    {
                       componentChkObj[i].checked=true;
                    }
                    for(var i=0;i<childComObj.length;i++)
                    {
                       childComObj[i].checked=true;
                    }
                }else
                {
                    for(var i=0;i<componentChkObj.length;i++)
                    {
                       componentChkObj[i].checked=false;
                    }
                    for(var i=0;i<childComObj.length;i++)
                    {
                       childComObj[i].checked=false;
                    }
                }
                $.ajax({
                    url: 'superAdminAction.do?superAdminParam=enableDisableModule&from=SuperAdmin&moduleCode='+id+'&status='+parentLIObj.checked,
                    success: function(data){
                    }
                });
            }
            function enableAllChildComponents(id,moduleCode)
            {
                var component=document.getElementById(id);
                var compCodeArry=id.split("~");
                var compCode=compCodeArry[1]
                var parentLIObj=component.parentNode;
                var ulObjs=parentLIObj.getElementsByTagName("ul");
                var liObjs=ulObjs[0].getElementsByTagName("li");
                var inputObjs=new Array();
                for(var i=0;i<liObjs.length;i++){
                    inputObjs.push(liObjs[i].getElementsByTagName("input")[0]);
                }
                var childObjs=document.getElementsByName(moduleCode+"~ChildComponent");
                var componentObjs=document.getElementsByName(moduleCode+"~Component");
                var isChecked=false;
               
                if(component.checked)
                {
                   for(var i=0;i<inputObjs.length;i++)
                   {
                           inputObjs[i].checked=true;
                   }
                }
                else
                {
                   for(var i=0;i<inputObjs.length;i++)
                   {
                           inputObjs[i].checked=false;
                   }
                }

                for(var i=0;i<componentObjs.length;i++)
                {
                   if(componentObjs[i].checked)
                    {
                           isChecked=true;
                           break;
                    }
                }
                if(isChecked){
                     document.getElementById(moduleCode).checked=true;
                }else{
                     document.getElementById(moduleCode).checked=false;
                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleComponent&from=SuperAdmin&moduleCode='+moduleCode+'&compCode='+compCode+'&status='+component.checked+"&moduleStatus="+isChecked,
                    success: function(data){

                    }
                })
            }
            function enableChildComponent(id,compCode,moduleCode)
            {
                var selChildObj=document.getElementById(id);
                var childCompCode=id.split("~")[2];
                var parentCompUlObj=selChildObj.parentNode.parentNode;
                var liObjs=parentCompUlObj.getElementsByTagName("li");
                var inputChildObjs=new Array();
                var componentObjs=document.getElementsByName(moduleCode+"~Component");
                for(var i=0;i<liObjs.length;i++)
                {
                    inputChildObjs.push(liObjs[i].getElementsByTagName("input")[0]);
                }

//                var allChildObjs=document.getElementsByName(moduleCode+"~ChildComponent");
                var isCompChecked=false;
                
                for(var i=0;i<inputChildObjs.length;i++)
                {
                   if(inputChildObjs[i].checked){
                       isCompChecked=true;
                       break;
                   }
                }
                if(isCompChecked){
                    document.getElementById(moduleCode+"~"+compCode).checked=true;

                }else{
                    document.getElementById(moduleCode+"~"+compCode).checked=false;
                }
                var isModuleChecked=false;
                for(var i=0;i<componentObjs.length;i++)
                {
                   if(componentObjs[i].checked)
                    {
                           isModuleChecked=true;
                           break;
                    }
                }
                if(isModuleChecked){
                     document.getElementById(moduleCode).checked=true;
                }else{
                     document.getElementById(moduleCode).checked=false;
                }
                $.ajax({
                    url: '<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=enableDisableModuleChildComponent&from=SuperAdmin&moduleCode='+moduleCode+'&compCode='+compCode+'&status='+selChildObj.checked+"&moduleStatus="+isModuleChecked+"&compStatus="+isCompChecked+"&childCompCode="+childCompCode,
                    success: function(data){

                    }
                })
                
            }
        </script>

    </head>
    <body>
        <form name="superAdminForm" id="superAdminForm" method="post" action="">
<!--            <div id="superAdminDiv" align="center" style="width:50%;height:50%;border:2px solid rgb(180, 217, 238);">

            </div>

            <div id="compListDiv" title="List of Components">
                <iframe id="compListFrame" name="compListFrame" frameborder="0" scrolling="" marginheight="0" marginwidth="0" src='#' width="100%" height="100%"></iframe>
            </div>-->

        <div id="superAdminDiv" style="height: 553px; overflow-y: auto; width: 300px; border: 2px solid rgb(180, 217, 238);" align="left" >
            <div class="navtitle-hover draggedDivs ui-corner-all" style="height: 33px;">
                        <table width="100%"><tr>
                                <td width="5%"><img src="icons pinvoke/user-business-boss.png"></td>
                                <td>
                                    <font size="2px" face="verdana" style="font-weight: bold; font-size: 11px;">&nbsp;Super Admin Privileges</font>
                                </td>
                                <td align='right'>
                                    <span class="ui-icon ui-icon-disk" title="Click to save the privileges" onclick="generateXmlForSuperAdmin()"></span>
                                </td></tr>
                        </table>

            </div>

        </div>
            
        </form>
<center><font  style="color: red; font-size: 11px;">*Application will logout after changing the privileges </font></center>
    </body>
</html>
