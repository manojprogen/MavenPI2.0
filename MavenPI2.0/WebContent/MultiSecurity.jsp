<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.metadata.Cube"%>
<%--
    Document   : MultiSecurity
    Created on : Feb 11, 2012, 12:20:01 PM
    Author     : Nazneen Khan
modified by Krishna for the purpose of Multi Security issue.
--%>
<!DOCTYPE html>

         <%
         //added by Dinanath for default locale
                    Locale cl=null;
                   cl=(Locale)session.getAttribute("UserLocaleFormat");

         String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
         String contextPath=request.getContextPath();
         %>
         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%=contextPath%>//dragAndDropTable.js"></script>

     <style type="text/css">
         .migrate{
                font-family: inherit;
                font-size: 10pt;
                color: #000;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
      </style>

       
    </head>
    <body>
        <script type="text/javascript">
         $("#deleteMultiSecurityDiv").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 800,
                        position: 'justify',
                        modal: true
                    });

         var compName = "";
         var secVal = "";
         var subFolderId= "";
         var memberName= "";
         var elementID = "";
         var memId = "";
         var accessLevel = "";

        $(document).ready(function(){
            getSecCheckDet("dim");
            secVal = "dim";
        });

        $(document).ready(function(){
               if ($.browser.msie == true){
                    $("#selectUserDimValuesDataDiv").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 700,
                    position: 'justify',
                    modal: true
                    });
//                    $("#contentDiv").dialog({
//                    autoOpen: false,
//                    height: 550,
//                    width: 700,
//                    position: 'justify',
//                    modal: true
//                    });
               }
               else {
                   $("#selectUserDimValuesDataDiv").dialog({
                    autoOpen: false,
                    height: 550,
                    width: 700,
                    position: 'justify',
                    modal: true
                    });
//                     $("#contentDiv").dialog({
//                    autoOpen: false,
//                    height: 550,
//                    width: 700,
//                    position: 'justify',
//                    modal: true
//                    });
               }
        });
//

       function getSecCheckDet(id){
           var val=document.multiSecurityForm.secCheck.value;
           var htmlVar1 = "";
          $("#secDetTabBody").html("")
           if(id=='dim'){
               secVal = "dim";
               htmlVar1+="<table id=\"measureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%\">\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                    <td align=\"left\"><select name=\"conn12\" id=\"conn12\" onchange=\"getFolderDetails12()\"></select></td>\n\
                                 </tr>\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"foldersSelect12\" id=\"foldersSelect12\" onchange=\"getDimDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"dimSelect\" id=\"dimSelect12\" onchange=\"getDimMemberDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Members", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                 <td align=\"left\"><select name=\"memberSelect\" id=\"memberSelect12\" onchange=\"getUsersDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Users", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                 <td align=\"left\"><select name=\"userSelect\" id=\"userSelect12\" onchange=\"\"></select></td>\n\
                                 </tr>\n\
                            </table>";
           }
//           else {
//               secVal = "fact";
//               htmlVar1+="<table id=\"measureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%\">\n\
//                                <tr><td align=\"left\" class=\"migrate\"><label>Select Connection : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
//                                    <td align=\"left\"><select name=\"conn12\" id=\"conn12\" onchange=\"getFolderDetails12()\"></select></td>\n\
//                                 </tr>\n\
//                                <tr><td align=\"left\" class=\"migrate\"><label>Roles : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
//                                     <td align=\"left\"><select name=\"foldersSelect12\" id=\"foldersSelect12\" onchange=\"getFactsDetails()\"></select></td>\n\
//                                 </tr>\n\
//                                 <tr><td align=\"left\" class=\"migrate\"><label>Facts : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
//                                     <td align=\"left\"><select name=\"factSelect\" id=\"factSelect\" onchange=\"getFactMemDetails()\"></select></td>\n\
//                                 </tr>\n\
//                                 <tr><td align=\"left\" class=\"migrate\"><label>Members : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
//                                 <td align=\"left\"><select name=\"memberSelect\" id=\"memberSelect\" onchange=\"\"></select></td>\n\
//                                 </tr>\n\
//                            </table>//";
//           }
           $("#secDetTabBody").html(htmlVar1)
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#conn12").html(data)
          });
       }

       function getFolderDetails12(){
            var connectionID= $("#conn12").val()
                $("#foldersSelect12").html("")
                $("#dimSelect12").html("")
                $("#memberSelect12").html("")
                $("#userSelect12").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                   // alert(data)
                    $("#foldersSelect12").html(data)
                });
       }
       function getDimDetails(){
            var connectionID= $("#conn12").val()
            var folderID= $("#foldersSelect12").val()
            $("#dimSelect12").html("")
            $("#memberSelect12").html("")
            $("#userSelect12").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){

                   $("#dimSelect12").html(data)
                });
       }
       function getDimMemberDetails(){
        var htmlVar=""
        var connectionID= $("#conn12").val()
        var folderID= $("#foldersSelect12").val()
        var dimId=$("#dimSelect12").val()
        $("#userSelect12").html("")
        $("#memberSelect12").html("")
        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimMemberDetails&connectionID="+connectionID+"&folderID="+folderID+"&dimId="+dimId ,function(data){
            $("#memberSelect12").html(data)
          });
        }
        function getUsersDetails(){
            var htmlVar=""
            $("#userSelect12").html("")
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getUsersDetails",function(data){
             //   alert(data)
                $("#userSelect12").html(data)
              });
        }

        function getFactsDetails(){
            var connectionID= $("#conn12").val()
            var folderID= $("#foldersSelect12").val()
               $("#factSelect").html("")
               $("#memberSelect12").html("")
               $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFactTabDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
               $("#factSelect").html(data)
            });
         }
//         function getFactMemDetails(){
//        var htmlVar=""
//        var connectionID= $("#conn12").val()
//        var foldersSelect12ed= $("#foldersSelect12").val()
//        var tablesSelected= $("#factSelect").val()
//        $("#memberSelect").html("")
//        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFactMeasureDetails&connectionID="+connectionID+"&foldersSelect12ed="+foldersSelect12ed+"&tablesSelected="+tablesSelected,function(data){
//                $("#memberSelect").html(data)
//          });
//        }


       function factSecurity() {
           $("#addDimSecurityDiv").dialog('open');
       }
       function closeDilog(id)
       {
              $("#"+id).dialog('close');
       }

function restrictAccess(){

    var contextPath = '<%= request.getContextPath()%>';
    var connectionID= $("#conn12").val()
    var folderID= $("#foldersSelect12").val()
    var dimId=$("#dimSelect12").val()
    var elementId=$("#memberSelect12").val()
    var userId=$("#userSelect12").val()

    $("#loadingmetadata").show();
//    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getMemDetails&connectionID="+connectionID+"&folderID="+folderID+"&dimId="+dimId+"&memId="+memId ,function(data){
 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getMemDetails&elementId="+elementId ,function(data){
            var jsonVar=eval('('+data+')')
                    subFolderId=jsonVar.subFolderId[0]
                    memberName=jsonVar.memberName[0]
                    elementID = elementId
                    memId = jsonVar.memberID[0]
    $.ajax({
        url:'userLayerAction.do?userParam=getDimentionMembersDetails&roleId='+folderID+'&memId='+memId+'&contextPath='+contextPath+'&userId='+userId+'&subFolderIdUser='+subFolderId,
        success: function(data) {
            $("#loadingmetadata").hide();
            $("#contentDiv").html("")
            $("#selectUserDimValuesDataDiv").dialog('open')
            var jsonVar=eval('('+data+')')
            $("#contentDiv").html(jsonVar.htmlStr)
            isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel

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
 });

}
function saveSelectVals()
{
    var folderID= $("#foldersSelect12").val()
    var dimId=$("#dimSelect12").val()
//    var memId=$("#memberSelect").val()
    var userId=$("#userSelect12").val()
//    var userId = <%=userId%>

    var meemberValues = "";
    $("#sortable li").each(function(){
        meemberValues = meemberValues+","+$(this).attr("id").replace("_li","").replace(",","'''");
    })
   if(meemberValues!=""){
       meemberValues=meemberValues.substr(1);
    }
    var urlVar=""
   if(userId=="0000")
//    if($("#accessLevel").val()=="roleLevel")
        urlVar='userLayerAction.do?userParam=addUserDimensionMemberValuesForRole&userMemId='+memId+'&subFolderIdUser='+subFolderId+'&meemberValues='+encodeURIComponent(meemberValues)+'&elementId='+elementID+'&subFolderId='+subFolderId+'&dimId='+dimId
    else
//    else if($("#accessLevel").val()=="userLevel")
        urlVar='userLayerAction.do?userParam=addUserDimensionMemberValues&userId='+userId+'&userMemId='+memId+'&subFolderIdUser='+subFolderId+'&meemberValues='+encodeURIComponent(meemberValues)+'&elementId='+elementID+'&dimId='+dimId
    $.ajax({
        url: urlVar,
        success: function(data) {
            if(data=="1")
                alert("Values Not Saved.")
            else{
                alert('Values Saved Successfully.')
            }
        }
    });
  $("#selectUserDimValuesDataDiv").dialog('close')
//   window.location.href = window.location.href;
}

    function delMultiSecurity(){
        var inputs = document.getElementsByName("secCheck")
        var connectionID= $("#conn12").val()
        var folderID= $("#foldersSelect12").val()
        var dimId=$("#dimSelect").val()
        var elementId=$("#memberSelect12").val()
        var userId=$("#userSelect12").val()


       if(connectionID == '--SELECT--'){
            alert('Please Select Connection Details')
        }
        else if(folderID == '--SELECT--'){
            alert('Please Select a Role')
        }
        else if(dimId== '0000'){
                 alert('Please Select a Dimension')
        }
        else if(elementId=='0000'){
            alert('Please Select a Member')
        }
        else if(userId=='--SELECT--'){
            alert('Please Select a User')
        }
    else {
        $.post("<%=request.getContextPath()%>/userLayerAction.do?userParam=getMultiSecurityVal&elementId="+elementId+"&userId="+userId,
                function(data){
                   if(data!='null'){
                       var jsonVar=eval('('+data+')')
                       var secKeyId=jsonVar.secKeyId;
                       var memberName=jsonVar.memberName;
                       var MemberValue=jsonVar.MemberValue;
                        var html="";
                       html+="<table width='100%'><thead class='migrate'><th>Check to Delete</th><th>Member Name</th><th>Member Value</th></thead><tbody>"
                        html+="<tr><td>&nbsp;</td></tr>";
                       for(var i=0;i<secKeyId.length;i++){

                          html+="<tr><td><input type=\'checkbox\' id=\'multiSecKeyId"+secKeyId[i]+"\' name=\'multiSecKeyId\'  value="+secKeyId[i]+"></td>";
                          html+="<td align='center'>"+memberName[i]+"</td>";
                          html+="<td align='center'>"+MemberValue[i]+"</td>";
                          html+="<td><input type=\'hidden\' name=\'userid\' id=\'userid\' value="+userId+"></td></tr>";
                       }
                       html+="<tr><td width=\'20px;\'>&nbsp;</td></tr><tr><td>&nbsp;</td><td align='right'><input class='navtitle-hover' type=\'button\' value='Delete' onclick=deleteSecurity()></td></tr>";
                   }
                   if(secKeyId.length>0){
                      $("#deleteMultiSecurityDiv").html(html);
                      $("#deleteMultiSecurityDiv").dialog('open');
                   }
                   else {
                       alert('No Security Applied for Selected Details')
                   }

                });


            }

       }
       function deleteSecurity(){
       var userId = $("#userid").val()

          var filterId=new Array();
          $(":checkbox:checked").each(
                 function() {
                   var a=$(this).val();
                   if(a=="true"){
                    }
                  else
                  filterId.push(a)
               });
               var retVal = confirm("Are you sure you want to delete applied security !");
                    if (retVal == true){
                         parent.$("#deleteMultiSecurityDiv").dialog('close');
                        $("#loadingmetadata").show();
                    $.ajax({
                    url:'<%=request.getContextPath()%>/userLayerAction.do?userParam=deleteMultiSecurityVal&filterId='+filterId+"&userId="+userId,
                        success:function(data){
                        if(data=='success'){
                            alert('Security deleted Successfully')
                        }
                        else {
                           alert('Error in Deletion !')
                        }
                        window.location.href = window.location.href;
                        $("#loadingmetadata").hide();
                     }
                    });
                    }

      }


       </script>

         <div align="center" style=" width:100% ">
                <form id="multiSecurityForm" name="multiSecurityForm" method="post" action="">
                    <table align ="center" id="multiSecDetTab" class="tablesorter"  border="0" cellpadding="1" cellspacing="1">
                       <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Please_Select_Restrict_Type", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                <td align="left" style="background-color:lightgoldenrodyellow; color:black;">
                                        <input type="radio" name="secCheck" value="dim" id="dim" checked onchange="getSecCheckDet(this.id)"> <%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%>
                                        <input type="radio" name="secCheck" value="fact" id="fact" onchange="getSecCheckDet(this.id)" disabled="true"> <%=TranslaterHelper.getTranslatedInLocale("Facts", cl)%>
                                </td>
                       </tr>
                    </table>
                    <br><br>
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0">
                        <tr><td id ="secDetTabBody">
                                <table id="measureDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style="width: 100%">
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                    <td align="left"><select name="conn12" id="conn12" onchange="getFolderDetails12()"></select></td>
                                 </tr>
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="foldersSelect12" id="foldersSelect12" onchange="getDimDetails2()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="dimSelect" id="dimSelect12" onchange="getMemberDetails()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Members", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                 <td align="left"><select name="memberSelect" id="memberSelect12" onchange="getUsersDetails()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Users", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                 <td align="left"><select name="userSelect" id="userSelect12" onchange=""></select></td>
                                 </tr>
                                 </table>
                            </td></tr>
                    </table>
                    <br> <br>
                    <table><tr>
                            <td>
                                <img width="22px" height="22px" border="0" src="icons pinvoke/cross-small.png" alt="" title="Delete Multi Security" onclick="delMultiSecurity()">
                            </td>
                            <td>&nbsp;&nbsp;</td><td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("go", cl)%>.." id="btnn" onclick="restrictAccess()"/></td>
                           </tr>
                    </table>
               <center>
                       </center>
                </form>
        </div>
        <div id="selectUserDimValuesDataDiv" title="Restrict Access">
         <center> <div height="50px"> <font style="color:black;font-size:12px;font-family:verdana;font-weight:bold" face="verdana"><%=TranslaterHelper.getTranslatedInLocale("Select_Values_To_Restrict", cl)%></font></div><Br></center>
         <div id="contentDiv"></div><br>
          <center> <div height="50px"> <font style="color:black;font-size:12px;font-family:verdana;font-weight:bold" face="verdana">
                        <input TYPE="button" value="<%=TranslaterHelper.getTranslatedInLocale("save", cl)%>" onclick="saveSelectVals()" class="navtitle-hover"></font>
                    </div><Br></center>
<!--         <table align="center">
                <tr>
                    <td align="center"><input TYPE="button" value="Save" onclick="saveSelectVals()" class="navtitle-hover"></td>
                </tr>
           </table>-->
          </div>
 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
        </div>
        <div id="deleteMultiSecurityDiv" title="Delete Multi Security" style="display: none"></div>

    </body>

</html>