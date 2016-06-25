<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,java.util.ArrayList"%>
<%
            PbDb pbdb = new PbDb();
            String query = "select * from prg_user_type";
            PbReturnObject list = pbdb.execSelectSQL(query);

            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
        session.setAttribute("theme", themeColor);
    }
    else {
        themeColor = String.valueOf(session.getAttribute("theme"));
        
    }
            String contxPath=request.getContextPath();

            //////////////////////////////////////////////.println.println("list is " + list);
            //////////////////////////////////////////////.println.println(" list.getRowCount() is " + list.getRowCount());
%>

<html>
    <head>
        <title></title>
        <%--<script src="<%=request.getContextPath()%>/javascript/draggable/jquery-1.3.2.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />--%>
              <script type="text/javascript" src="<%=contxPath%>/javascript/lib/jquery/js/jquery.bubblepopup.v2.3.1.min.js"></script>
              <link type="text/css" href="<%=contxPath%>/javascript/lib/jquery/css/jquery.bubblepopup.v2.3.1.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contxPath%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=contxPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />

      
        <style type="text/css">
            .fontsty{
                -moz-border-radius-bottomleft:4px;
                -moz-border-radius-bottomright:4px;
                -moz-border-radius-topleft:4px;
                -moz-border-radius-topright:4px;
                background-color:#bdbdbd;
            }
            .white_content {
                display: none;
                position: absolute;
                top: 30%;
                left: 35%;
                width: 50%;
                height:50%;
                padding: 16px;
                border: 10px solid silver;
                background-color: white;
                z-index:1002;
            }

        </style>
    </head>

    <body>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#tablesorterUserType")
                .tablesorter({headers : {0:{sorter:false}}})
            });
        </script>
        <div id="light" align="center" class="white_content"><img  alt="Page is Loading" src='images/ajax.gif'></div>
        <form name="myFormUserType" id="myFormUserType" method="post" action="">
            <table align="center" id="tablesorterUserType" class="tablesorter"  style="width:98%;" cellpadding="0" cellspacing="1">
                <thead>
                    <tr> <th>&nbsp;</th>
                        <th>User Type Name</th>
                        <th>User Type Description</th>
                    </tr>
                </thead>
                <tbody>
                    <%for (int i = 0; i < list.getRowCount(); i++) {%>
                    <tr>
                        <td style="width:40px">
                            <input type="checkbox" id="chkUserType" name="chkUserType" value="<%=list.getFieldValueInt(i, "USER_TYPE_ID")%>">
                        </td>
                        <td>
                            <%=list.getFieldValueString(i, "USER_TYPE_NAME")%>
                        </td>
                        <td>
                            <%=list.getFieldValueString(i, "USER_TYPE_DESC")%>
                        </td>

                    </tr>
                    <%}%>
                </tbody>
            </table>
            <br><br>
            <table align="center">
                <tr>
                    <Td></Td>
                    <%if (list.getRowCount() == 0) {%>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Create User Type" onclick="javascript:createUserType();"><Td>
                        <%} else {%>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Create User Type" onclick="javascript:createUserType();"><Td>
<!--                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Assign Privileges" onclick="javascript:assignPriv();"><Td>-->
                    <Td><input class="navtitle-hover" style="width:auto" type="button" value="Assign Privileges" onclick="assignPrivileges()"></Td>
                    <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Delete User Type" onclick="javascript:deleteUserType();"><Td>
                    <Td><input class="navtitle-hover" style="width:auto" type="button" value="Show License Details" onclick="showLicensePrivileges()"><Td>
                        <%--   <Td><input  class="navtitle-hover" style="width:auto" type="button" value="Delete User Type" onclick="javascript:deleteUserType();"><Td>--%>

                        <%}%>
                </tr>
            </table>




            <%--  <div id="createUserType"  title="Assign Role" STYLE='display:none;height:auto'>
                <iframe  id="createUserTypeData" NAME='createUserTypeData'  frameborder="0" height="100%" width="100%" STYLE='overflow:auto' frameborder="0"   SRC='#'></iframe>
            </div>
            --%>
            <div id="createUserType" title="Create User Type" style="display:none">
                <center>
                    <br>
                    <table style="width:100%" border="0">
                        <tr>
                            <td valign="top" class="myHead" style="width:40%">User Type Name</td>
                            <td valign="top" style="width:60%">
                                <input type="text" maxlength="35" name="userTypeName" style="width:150px" id="userTypeName" onkeyup="tabmsgUserType()">
                            </td>
                        </tr>
                        <tr>
                            <td  valign="top" class="myHead" style="width:30%">Description</td>
                            <td valign="top" style="width:70%">
                                <textarea name="userTypeDesc" id="userTypeDesc" style="width:150px"></textarea>
                            </td>
                        </tr>
                    </table>
                    <table>
                        <tr>
                            <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" id="userTypesave" onclick="userTypesave()"></td>
                                <%--<td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelDashboard()"></td>--%>
                        </tr>
                    </table>
                </center>
            </div>
            <div id="superAdminUserDiv"  align="center" title="Assign/Revoke Privileges" style='display:none;' >
            <div id="imgDiv" style="display: none;">
              <center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>
            </div>
            <iframe id="superAdminUserFrame" src="#" scrolling="no"  height="100%" width="100%" frameborder="0" ></iframe>
            </div>
            <div id="showLicensesDiv" title="License Details" style="display: none;">

            </div>
            <div id="assignedUserDiv" title="Privileges Assigned" style="display: none;">

            </div>
            </form>

  <script type="text/javascript">
            function userTypesave()
            {
                // alert('in userTypesave ');
                // alert(document.getElementById('userTypeDesc').value);
                // alert(document.getElementById('userTypeName').value);
                // document.forms.myFormUserType.action='.do?param=saveEditBugdetails'
                // document.forms.myFormUserType.submit();
                //'.do?param=saveEditBugdetails';
                var userdec =document.getElementById('userTypeDesc').value;
                var userTypeName =document.getElementById('userTypeName').value;
                $.post('<%=request.getContextPath()%>/saveUserTypeAction.do?param=saveUsertype&userDec='+userdec+'&userTypeName='+userTypeName,$("#myFormUserType").serialize(),
                function(data){
                        $("#createUserType").dialog('close');
                        if(data=="available")
                        {
                            alert("user type already available,please enter other user type")
                        }
                         
                            document.forms.myFormUserType.action="AdminTab.jsp#User_Type_List";
                            document.forms.myFormUserType.submit();
                }) ;
//                $.ajax({
//                    url:'saveUserTypeAction.do?param=saveUsertype&userDec='+userdec+'&userTypeName='+userTypeName,
//                    success: function(data) {
////                        alert(data)
//                        if(data=="available")
//                        {
//                            alert("user type already available,please enter other user type")
//                        }
//                        if(data==1)
//                        {
//
//                        }
//                    }
//                });
             
                //  window.location.reload(true);
            }
            
            function tabmsgUserType(){
                document.getElementById('userTypeDesc').value = document.getElementById('userTypeName').value;
            }
            function initialogType(){
                if ($.browser.msie == true){
                    $("#createUserType").dialog({
                        autoOpen: false,
                        height: 250,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                   $("#showLicensesDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });
                   $("#superAdminUserDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                    $("#assignedUserDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                }
                else{
                    $("#assignedUserDiv").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 250,
                        position: 'justify',
                        modal: true
                    });
                      $("#superAdminUserDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true
                     });
                     $("#showLicensesDiv").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 900,
                        position: 'justify',
                        modal: true
                    });
                    $("#createUserType").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true
                    });
                }
            }

            function createUserType()
            {
                initialogType();
                //  var frameObj2=document.getElementById("createUserTypeData");
                //  var source2 = "createUserType.jsp";
                //  frameObj2.src=source2;
                $("#createUserType").dialog('open');
            }
            function assignPriv(){
                parent.initNavi();
                var i=0;
                var userTypeId;
                var chkUserTypeobj = document.forms.myFormUserType.chkUserType;

                for(var j=0;j<chkUserTypeobj.length;j++){
                    if(chkUserTypeobj[j].checked){
                        userTypeId=chkUserTypeobj[j].value;
                        i++;
                    }
                }
                if(i==0){
                    alert("Please Select a User Type");
                    return false;
                }
                else if(i>1){
                    alert("Please Select only one User Type");
                    return false;
                }
                else{

                    // alert('userTypeId in list '+userTypeId)
                    var frameObj = document.getElementById("userTypePrivilagesFrame");
                    frameObj.src="<%=request.getContextPath()%>/assignUserTypePriv.jsp?userTypeId="+userTypeId;
                    parent.$("#assignPriviDialog").dialog('open');
                }
            }

            function deleteUserType(){
                    var i=0;
                    var userTypeId="";
                    var chkUserTypeobj = document.forms.myFormUserType.chkUserType;

                    for(var j=0;j<chkUserTypeobj.length;j++){
                        if(chkUserTypeobj[j].checked){
                            userTypeId+=","+chkUserTypeobj[j].value;
                            i++;
                        }
                    }
                    if(userTypeId.length>0){
                        userTypeId=userTypeId.substring(1);
                    }
                    if(i==0){
                        alert("Please Select a User Type");
                        return false;
                    }
                    else{
                        $.ajax({
                            url:'saveUserTypePrivileges.do?previlegesType=delUserType&userTypeId='+userTypeId,
                            success: function(data){
                                alert(data)
                                document.forms.myFormUserType.action="AdminTab.jsp#User_Type_List";
                                document.forms.myFormUserType.submit();
                            }
                        });
                    }
                }
        //Added on 27 April
                 function assignPrivileges(){
                    var i=0;
                    var userIdTypeId;
                    var chkusersobj = document.forms.myFormUserType.chkUserType;
                    for(var j=0;j<chkusersobj.length;j++){
                        if(chkusersobj[j].checked){
                            userIdTypeId=chkusersobj[j].value;
                            i++;
                        }
                    }
                    if(i==0){
                        alert("Please Select a User Type");
                        return false;
                    }
                    else if(i>1){
                        alert("Please Select only one User Type");
                        return false;
                    }
                    else{
                        initialogType();

                        $("#superAdminUserDiv").dialog('open');
                        $("#imgDiv").show();
                        $.post(
                            "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=initializeUserLicenseModules&userTypeId="+userIdTypeId, $("#myFormUserType").serialize(),
                             function(data){

                                  var frameobj = document.getElementById("superAdminUserFrame");
                                  frameobj.src= "<%=request.getContextPath()%>/SuperAdmin/UserModuleAssignment.jsp?userId="+userIdTypeId;
                                   $("#imgDiv").hide();
                                 }
                        );



                    }

                }
                function logout(){
                  document.forms.myForm.action="<%=request.getContextPath()%>/baseAction.do?param=logoutApplication";
                  document.forms.myForm.submit();
                }
                function closeDialog(){
                //alert("close")
                  $('#superAdminUserDiv').dialog('close')

                }
                 function showLicensePrivileges()
                {
                   initialogType();
                   $("#showLicensesDiv").dialog('open');
                   $("#showLicensesDiv").html('<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                   $.ajax({
                            url: "<%=request.getContextPath()%>/superAdminAction.do?superAdminParam=showSuperAdminLicensePrivileges",
                            success: function(data){

                                  $("#showLicensesDiv").html(data)

                                 $(".bubbleAnchor").each(function(i){
                                    var tooltip = $(this).attr("tooltip");
                                    $(this).CreateBubblePopup({
                                        selectable: false,
                                        position : 'top',
                                        align	 : 'center',
                                        innerHtml : tooltip,
                                        themeName: 	'all-grey',
                                        themePath: 	'images/jquerybubblepopup-theme'

                                    });
                                });
                                $(".bubbleAnchor").attr('z-index','2000')


                                 }
                        });
                }
                function closeLicenseDiv(){
                  $("#showLicensesDiv").dialog('close');
                }
                function showUsers(html)
                {
                  $("#assignedUserDiv").html(html);
                  $("#assignedUserDiv").dialog('open')
                }
        </script>
    </body>
</html>