<%--
    Document   : pbUserLayer
    Created on : Sep 5, 2009, 3:59:45 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>

<%--
    Document   : MyTable
    Created on : Aug 7, 2009, 7:09:17 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject"%>
<% String contextPath=request.getContextPath();%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi 1.0</title>
      <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
          <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
          <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
           <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
           <script type="text/javascript" src="<%= contextPath%>//dragAndDropTable.js"></script>
 <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css"/>
            <link rel="stylesheet" href="css/bootstrap-theme.min.css" type="text/css"/>
            <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css"/>
             <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=session.getAttribute("theme")%>/ReportCss.css" rel="stylesheet" />


<!--     <link rel="stylesheet" href="stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.droppable.js"></script>
        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />
        <script type="text/javascript" src="javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="javascript/effects.core.js"></script>
        <script type="text/javascript" src="javascript/effects.explode.js"></script>
        <link type="text/css" href="stylesheets/demos.css" rel="stylesheet" />
        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>
         below five lines only added by bharathi reddy on 26-08-09
        <link href="stylesheets/StyleSheet.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/confirm.css" rel="stylesheet" type="text/css" />
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script src="javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>-->
        <script type="text/javascript" src="javascript/pbUserLayer.js"></script>
<!--        <link href="stylesheets/pbUserLayer.css" rel="stylesheet" type="text/css" />-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->

<script type="text/javascript">
 $(document).ready(function() {
                 $("#selectConnection1").dialog({
                   // bgiframe: true,
                    autoOpen: false,
                    height:250,
                    width:350,
                    modal: true,
                    buttons: {
                        Cancel: function() {
                          // var x=confirm('To See Dimensions Please Select Connection Are You Sure To Cancel')
                          // if(x==true){
                            $(this).dialog('close');
                          // }
                        },
                        Select: function() {
                            saveTables1();
                              //$(this).dialog('close');
                        }
                    },
                    close: function() {

                    }
                });
});
</script>
        
        <style type="text/css">

                .loading_image{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width:900%;
                height: 82%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
                z-index:1001;
                overflow:auto;
            }
            .bodyh{
                overflow:hidden;
                position: relative; /* fix to 'IE7 10000px padding-bottom spill over footer' problem */
                padding-left: 0px; /* LC fullwidth */
                padding-right: 0px; /* RC fullwidth + CC padding */
                padding-top:0px;
                padding-bottom:0px;
            }
            .filterDia{
                overflow:hidden;
                position: relative; /* fix to 'IE7 10000px padding-bottom spill over footer' problem */
                padding-left: 0px; /* LC fullwidth */
                padding-right: 0px; /* RC fullwidth + CC padding */
                padding-top:0px;
                padding-bottom:0px;
            }

           *{
                -x-system-font: none;
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
    </head>
    <body  onload="removeimage()" class="bodyh">
        <%
        String folderData = "";
        if (request.getAttribute("UserFolderList") != null) {
            folderData = String.valueOf(request.getAttribute("UserFolderList"));
        }

        %>
        <%

       String connId=String.valueOf(session.getAttribute("connId"));
        String connName="";
        PbReturnObject pbro=null;
                if(connId==null || connId.equalsIgnoreCase("NULL")){
                    connName="";
                    }else{
          String Query = " SELECT CONNECTION_NAME FROM PRG_USER_CONNECTIONS where CONNECTION_ID in("+connId+")";
         pbro = new PbDb().execSelectSQL(Query);
         connName=pbro.getFieldValueString(0,0).toUpperCase();
            }
       // //////////////////////////////////////////////////////.println.println(" folderData "+folderData);

                %>
        <%--border="solid black 1px"--%>
        <form name="myForm" method="post">
            <table style="width:100%;height:100%" border="solid black 1px">
                <input type="hidden" name="bustableId" id="bustableId" value="">
                <input type="hidden" name="grpId" id="grpId" value="">
                <tr>
                    <td  width="28%" valign="top">
                        <div style="height:33px" class="themeColor draggedDivs ui-corner-all">
                             <font style="font-weight:bold" face="verdana" size="1px">Business Roles
                        - <a href="javascript:void(0)" onclick="javascript:goConnection()" style="color:black; text-decoration: none;"><b><%=connName%></b></a>
                             </font>
                              <div style="padding: 5px;float: right;">
                                    <a class="fa fa-refresh themeColor" href="javascript:void(0)" onclick="refreshPage()" style="color:white;"></a>
                                </div>
<!--                             <table align="right">
                               <tr>
                                   <td>
                                  <a href="javascript:void(0)" onclick="refreshPage()"><img src="<%=request.getContextPath()%>/icons pinvoke/refreshPage.png"></img></a>
                                   </td>
                               </tr>
                           </table>-->

                        </div>
                        <%--<span id="foldList" style="display:none;visibility:hidden"><bean:write name="UserFolderList"></bean:write></span>

                        --%>
                        <div class="masterDiv" style="height:555px;overflow-y:auto">
                            <ul id="userFolderTree">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')"><img src="icons pinvoke/folder-horizontal.png"><span class="bussGroupMenu">Business Roles</span>
                                    <ul id="foldGrps">
                                        <%=folderData%>
                                    </ul>
                                </li>
                            </ul>
                        </div>


                    </td>
                    <td   width="72%" valign="top">

                    </td>
                    <%--UserFolderMenu--%>
                </tr>
            </table>
                <input type="hidden" id="accessLevel" name="accessLevel" value="">
        </form>

        <%String Query = " SELECT CONNECTION_ID, CONNECTION_NAME FROM PRG_USER_CONNECTIONS";
        PbReturnObject pbro1 = new PbDb().execSelectSQL(Query);
%>
  <div>
            <div>
                 <iframe  id="userfoldertab" NAME='userfoldertab' style="height:600px;width:100%;display:none" SRC='#' frameborder="0" marginheight="0" marginwidth="0"></iframe>
            </div>
        </div>
<div id="selectConnection1" title="Select Connection">
            <form name="myFormcon" id="myFormcon" method="post">
                <table cellpadding="5">
                    <tr><td>
                        <label for="name">Connection Name</label></td>
                     <td> <select id="connId" name="connId" style="width:146px" onchange="saveTables()">
                        <%for (int i = 0; i < pbro1.getRowCount(); i++) {%>
                        <option value="<%=pbro1.getFieldValueInt(i, 0)%>"><%=pbro1.getFieldValueString(i, 1)%></option>
                        <%}%>
                         </select></td></tr>
                    <tr><td>
                        <label for="name">Role</label></td>
                    <td> <select name="foldersSelect" id="foldersSelect" ></select></td></tr>

                </table>
                <%--<input type="button"  value="Connect" onclick="saveTables()">--%>
            </form>

        </div>
        <div id="fade" class="black_overlay"></div>
        <div id="filterDialog" title="Filter" style="width:auto;height:510px" class="filterDia">

        </div>
        <%-- added bharathi reddy  for add formula--%>
        <div id="roleFormulaDialog" title="Formula">
          <%-- end--%>
        </div>
        <div id="deleteUserFolder" title="Remove The Business Role?">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The Business Role will be removed from the list. Are you sure?</p>
        </div>

        <div id="publishUserFolder" title="Publish The Business Role?">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The Business Role will be published </p>
        </div>
          <div id="copyUserFolder" title="Copy The Business Role?" >
           <form>
                <table cellpadding="5">
                    <tr><td>
                        <label for="name" class="label">Business Role Name</label></td>
                    <td><input type="text" onkeyup='javascript:copyfolderDesc()'name="folderName" id="folderName" class="text ui-widget-content ui-corner-all" style="width:149px"/></td></tr>

                    <tr><td>
                        <label for="email" class="label">Description</label></td><td>
                    <textarea name="folderDesc" id="folderDesc" class="textarea ui-widget-content ui-corner-all" ></textarea></td></tr>
                </table>

            </form>
        </div>

        <ul id="userFolderMenu" class="contextMenu" style="width:auto;text-align:left">
            <li class="insert"><a href="#assignToUser">Assign To Users</a></li>
            <li class="insert"><a href="#publishUserFolder">Publish Business Role</a></li>
            <li class="insert"><a href="#deleteUserFolder">Delete Business Role</a></li>
             <li class="insert"><a href="#copyUserFolder">Copy Business Role</a></li>
            <%-- added 28-11-09 by susheela start --%>
            <li class="insert"><a href="#makeTargetFolder">Make It available For Target</a></li>
            <%-- added by susheela over --%>

        </ul>
        <ul id="userFilterFormulaMenu" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#addProperties">Add Properties</a></li>
                   <li class="insert"><a href="#publishfact"><font class="text_classstyle1">Publish Fact</font></a></li>
             <%-- added bharathi reddy  for add formula--%>
          <%--  <li class="insert"><a href="#addparamFormula">Add Parameterised Formula</a></li>--%>
        </ul>
        <ul id="userdelete" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#userdelete">Delete</a></li>
            <li class="insert"><a href="#viewFormula">View Formula</a></li>
        </ul>
        <%-- added by susheela start 28-11-09--%>
         <div id="makeTargetFolder" title="Make The Business Role Available For Target">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Make The Business Role Available For Target. Are you sure?</p>
        </div>
          <%-- added by susheela over --%>

        <%-- added by susheela start --%>
        <div id="dimensionDialog" title="Delete Dimensions">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The dimensions will be cleared. Are you sure?</p>
        </div>
        <div id="dimensionMemberDialog" title="Delete Dimension Member">
            <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>The dimensions member will be cleared. Are you sure?</p>
        </div>


        <ul id="deleteDimension" class="contextMenu" style="width:150px;text-align:left">

            <li class="insert"><a href="#deleteDimension">Delete Dimension</a></li>
            <li class="insert"><a href="#partialPublish">Partial Publish</a></li>

        </ul>
        <ul id="deleteDimensionMember" class="contextMenu" style="width:auto;text-align:left">

            <li class="insert"><a href="#deleteDimensionMember">Delete Dimension Member</a></li>
            <li class="insert"><a href="#restrictAccess">Restrict Access</a></li>
            <li class="insert"><a href="#addFilter">Add Filter</a></li>
        </ul>
        <ul id="showDimensionDrillEdit" class="contextMenu" style="width:180px;text-align:left">
            <li class="insert"><a href="#showDimensionDrill">Edit Drill</a></li>
            <li class="insert"><a href="#viewDimensionDrill">View Custom Drill</a></li>
        </ul>

         <%-- added by chiranjeevi 24-12-09 for accounts--%>
        <ul id="showAccounts" class="contextMenu" style="width:180px;text-align:left">

            <li class="insert"><a href="#showCreateAccount">Create Company</a></li>
        </ul>
        <ul id="assignAccCont" class="contextMenu" style="width:180px;text-align:left">
            <li class="insert"><a href="#expireAccount">Expire Company</a></li>
           <!-- <li class="insert"><a href="#assignAccount">Assign Account</a></li>-->
        </ul>
        <ul id="assignAccDimSec" class="contextMenu" style="width:180px;text-align:left">
            <li class="insert"><a href="#getaccessAccount">Restrict Access</a></li>
           <!-- <li class="insert"><a href="#assignAccount">Assign Account</a></li>-->
        </ul>
        <ul id="showAccountUsers" class="contextMenu" style="width:180px;text-align:left">

            <li class="insert"><a href="#showInvalidateUser">Invalidate User</a></li>
        </ul>

        <div id="createAcc"  title="Create Account">
           <iframe  id="createAccData" NAME='createAccData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
        </div>
        <div id="assignAccDiv"  title="Assign Account">
           <iframe  id="assignAccFrame" NAME='assignAccFrame'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
        </div>
        <div id="selectAccountDimValuesDataDiv" title="Edit User Custom Drill">
             <iframe  id="selectAccountDimValuesData" NAME='selectAccountDimValuesData' frameborder="1" height="50px" STYLE='display:none;overflow:auto' frameborder="0"  class="white_content1" SRC='#'></iframe>
          </div>


        <%-- added by chiranjeevi over 24-12-09 for accounts--%>


        <div id="warning" title="Message" style="display:none">
            <p>
                <span  class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 50px 0;"></span>
                <div id="alert"></div>
            </p>
        </div>

         <div id="editDrillDiv" title="Edit Drill">
             <iframe  id="DimenionDrillData" name='DimenionDrillData' height="100%" width="100%"  frameborder="0"   src='about:blank'></iframe>
          </div>
         <div id="fade" class="black_overlay"></div>

         <div id="viewDrillDiv" title="View Custom Drill">
             <iframe  id="viewDrillData" name='viewDrillData' frameborder="1" height="100%" width="100%" frameborder="0"  src='about:blank'></iframe>
          </div>
         <div id="fade" class="black_overlay"></div>

                 <%--added on 02-12-09 --%>
         <ul id="showUserDrillEdit" class="contextMenu" style="width:auto;text-align:left">

            <li class="insert"><a href="#editUserDimensionDrill">Edit User Custom Drill</a></li>
            <li class="insert"><a href="#viewUserDimensionDrill">View User Custom Drill</a></li>
        </ul>
        <div id="editUserDrillDataDiv" title="Edit User Custom Drill">
             <iframe  id="editUserDrillData" NAME='editUserDrillData' frameborder="1" height="50px" STYLE='display:none;overflow:auto' frameborder="0"  class="white_content1" SRC='#'></iframe>
          </div>
         <div id="fade" class="black_overlay"></div>

         <div id="viewUserDrillDiv" title="View User Custom Drill">
             <iframe  id="viewUserDrillData" NAME='viewUserDrillData' frameborder="1" height="50px" STYLE='display:none;overflow:auto' frameborder="0"  class="white_content1" SRC='#'></iframe>
          </div>
         <div id="fade" class="black_overlay"></div>
          <ul id="userDimension" class="contextMenu" style="width:auto;text-align:left">

            <li class="insert"><a href="#userDimension">Restrict Access</a></li>
        </ul>
        <div id="selectUserDimValuesDataDiv" title="Restrict Access">
         <center> <div height="50px"> <font style="color:black;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">Select Values To Restrict Access</font></div><Br></center>
         <div id="contentDiv"></div><br>
         <table align="center">
                <tr>
<!--                    <td align="center"><input type="button" value="Reset To Default" onclick="updateVals()" class="navtitle-hover"></td>-->
                    <td align="center"><input TYPE="button" value="Save" onclick="saveSelectVals()" class="navtitle-hover"></td>
                    <td align="center"><input TYPE="button" value="Cancel" onclick="cancelSelectValsParent()" class="navtitle-hover"></td>
                </tr>
           </table>

          </div>

         <div id="fade" class="black_overlay"></div>
          <div id="selectRoleDimValuesDataDiv" title="Edit User Custom Drill">

<!--             <iframe  id="selectRoleDimValuesData" NAME='selectRoleDimValuesData' frameborder="1" height="50px" STYLE='display:none;overflow:auto' frameborder="0"  class="white_content1" SRC='#'></iframe>-->
          </div>
         <div id="fade" class="black_overlay"></div>
         <div id="userAssigned" title="Assign To Users">
              <iframe  id="userAssignDisp" NAME='userAssignDisp' height="100%" width="100%"  frameborder="0" SRC='#'></iframe>
           </div>
        <%-- added by susheela over --%>

        <%-- added by susheela over --%>
        <Input type="HIDDEN" name="userId" id="userId" value=">">
            <Input type="HIDDEN" name="subFolderIdUser" id="subFolderIdUser" value="">
            <Input type="HIDDEN" name="userMemId" id="userMemId" value="">
            <Input type="HIDDEN" name="mbrName" id="mbrName" value="">
<ul id="FormulaMenuList" class="contextMenu">
 <%--   <li class="addFormulafact"><a href="#addFormulafact"><font class="text_classstyle1">Add Formula</font></a></li>--%>
</ul>
<div id="formulaViewDiv" title="View Custom Measure" style="display:none">
                                                <table>
                                                    <tr>
                                                        <td id="value"></td>
                                                    </tr>
                                                    </table>
                                                        </div>

<!--added by krishan-->
 <div id="loading" class="loading_image">
            <img id="imgId" src="images/help-loading.gif"  border="0px" style="position:absolute;left:150px;top:0px">
 </div>


 <script type="text/javascript">
            
              function saveTables()
       {
             //$('#selectConnection').dialog('close');
            // $('#selectRole').dialog('open');
            var connId=document.getElementById("connId").value;
            $.ajax({
                    url: 'userLayerAction.do?userParam=getUserFolderList1&connId='+connId,
                    success: function(data) {
                        $("#foldersSelect").html(data)
                    }
                });



        }
             function saveTables1()
       {    
             $('#selectConnection1').dialog('close');
            var connId=document.getElementById("connId").value;
            var folderId=document.getElementById("foldersSelect").value;
            // var frameObj = document.getElementById('userfoldertab');
             //var source;


             if(folderId=="0000")
                 {
                    document.forms.myFormcon.action="userLayerAction.do?userParam=getUserFolderList&connId="+connId;
                 }
                 else
                     {
             document.forms.myFormcon.action="userLayerAction.do?userParam=getUserFolderList2&connId="+connId+"&folderId="+folderId;
                     }
                      parent.document.getElementById('loading').style.display='block';
           document.forms.myFormcon.submit();
          
           document.getElementById('userfoldertab').style.display='';
           // var frameObj = document.getElementById('businessgrptab');

        }
 
             function goConnection(){

$('#selectConnection1').dialog('open');
}
            function getElement(eleId,prntid)
        {
                //alert(prntid)
                var total = document.getElementById("text"+prntid).value;
                //alert(total)
                var elename ="child-";
                var j=0,eleid;
                for(var i=0;i<total;i++){
                    eleid=elename+i+"-"+prntid;
                    //alert(eleid)
                    //alert(document.getElementById(eleid).checked)
                    if(document.getElementById(eleid).checked==true){
                    j=1;
                    break;
                    }else{
                        j=0;
                    }
                }
                prntid=prntid+"chk";
                if(j==1){
                document.getElementById(prntid).checked=true;
                }else{
                    document.getElementById(prntid).checked=false;
                }
                 $.ajax({
                    url: 'userLayerAction.do?userParam=updateFactStatus&subFolderfactTabId='+prntid,
                success: function(data) {
                    //if(data=="false")
                        //alert("Unable to Save Target Fact")
                }
            });
                $.ajax({
                url: 'userLayerAction.do?userParam=updateElemetIdFolder&elementId='+eleId,
                success: function(data) {
                    //if(data=="false")
                        //alert("Unable to Save Target Fact")
                }
            });
        }
          //added on 16th Nov start susheela
        function updateDimSt(dim_subFolder)
        {
            //alert('dim_subFolder '+dim_subFolder);
             $.ajax({
                url: 'userLayerAction.do?userParam=updateDimensionStatus&dimSubFolder='+dim_subFolder,
                success: function(data) {
                    //if(data=="false")
                        //alert("Unable to Save Target Fact")

                }
            });
        }
         function updateFactSt(subFolderfactTabId)
        {
            //alert('subMbrDimTab '+subMbrDimTab);
             $.ajax({
                url: 'userLayerAction.do?userParam=updateFactStatus&subFolderfactTabId='+subFolderfactTabId,
                success: function(data) {
                    //if(data=="false")
                        //alert("Unable to Save Target Fact")

                }
            });
        }
       // //added on 16th Nov over susheela



         function updateDimensionMemberSt(subMbrDimTab)
        {
            //alert('subMbrDimTab '+subMbrDimTab);
             $.ajax({
                url: 'userLayerAction.do?userParam=updateDimensionMemberStatus&subMbrDimTab='+subMbrDimTab,
                success: function(data) {
                    //if(data=="false")
                        //alert("Unable to Save Target Fact")

                }
            });
        }
       // //added bharathi on 16th jan


        //deleteDimension
            function removeimage(){
                parent.document.getElementById('loading').style.display='none';
            }function refreshPage(){
                window.location.reload(true);
                parent.document.getElementById('loading').style.display='block';
            }function bsroleexist(){
             parent.document.getElementById('loading').style.display='block';
            }
             window.onunload = bsroleexist;
            function checkNunchkfacts(id){
                //alert(id);
                var psint = parseInt(id);
                //alert("psint"+psint)
                var limit = document.getElementById("text"+psint).value;
                //alert(limit);
                var chkid = "child-"+psint;
               // alert("chkid"+chkid);
               // alert(document.getElementById(id).innerHTML);
               //alert("id"+id)
                var vrchk = document.getElementById(id).checked;
                //alert("vrchk"+vrchk);
                if(vrchk==true){
                   // alert("checked");
                   // var chkusersobj = document.forms.myForm.chkid;
                for(var j=0;j<limit;j++){
                   chkid = "child-"+j+"-"+psint;
                  // alert(chkid)
                   document.getElementById(chkid).checked=true;

                   //getElement()
                       // chkusersobj[j].checked=true;
            }
                $.ajax({
                    url: 'userLayerAction.do?userParam=updateFacts&factTabId='+psint+'&checked=Y',
                    success: function(data) {
                    }
                });
                }if(vrchk==false){
                    //alert("unchecked"+chkid);
                     //var chkusersobj = document.forms.myForm.chkid;
                     //alert("chkusersobj\n"+chkusersobj)
                for(var j=0;j<limit;j++){
                    chkid = "child-"+j+"-"+psint;
                    //alert(chkid)
                   document.getElementById(chkid).checked=false;
                        //chkusersobj[j].checked=false;
                }
                $.ajax({
                    url: 'userLayerAction.do?userParam=updateFacts&factTabId='+psint+'&checked=N',
                    success: function(data) {
                    }
                });
                }
               /* var chkusersobj = document.forms.myForm.chkid;
                for(var j=0;j<chkusersobj.length;j++){
                    if(chkusersobj[j].checked){
                        chkusersobj[j].checked=true;
                    }else{
                        chkusersobj[j].checked=false;
                    }
                }*/
            }
        </script>

    </body>
</html>
