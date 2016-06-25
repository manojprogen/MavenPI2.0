<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,prg.db.PbReturnObject,prg.db.PbDb"%>
<%-- 
    Document   : favouriteReportsAssignment
    Created on : 16 Nov, 2012, 5:09:37 PM
    Author     : Anil
--%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
            //added by Dinanath
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme")); 
             String contextpath=request.getContextPath();
             %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
    
      <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <script type="text/javascript" src="<%=contextpath%>/javascript/scripts.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen"/>
        <script src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextpath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextpath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextpath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%= contextpath%>//dragAndDropTable.js"></script>
<!--        <link type="text/css" href="<%=contextpath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />-->

   
           <style type="text/css">
               #tablesorterUserList{
                   width:40%;
}
</style>
    </head>
    <% 
      try{
                 PbDb pbdb = new PbDb();
                 PbReturnObject  pbro=null;
             String getfolderList = "SELECT FOLDER_ID, GRP_ID, FOLDER_NAME FROM PRG_USER_FOLDER";
             pbro = pbdb.execSelectSQL(getfolderList);

     %>

    <body>
    <script type="text/javascript">
//        var userIds=new Array();
        var userIds = "";
        function UsersforRoles(id){
            var id=$("#"+id).val().split('~');
            var grpID=id[0];
            var folderID=id[1];
           // alert(grpID);
            $.post('userLayerAction.do?userParam=usersForRoles&groupID='+grpID+'&folderID='+folderID,
            function(data){
                $('#usersDiv').html(data)
            });
        }
     function Assign(grpid)
     {
    userIds= "";
       //  alert(+grpid);

         // if(document.forms.favouritesform.chkusers.checked)
         //var userobj1 = document.getElementById('chkusers').checked;
         var noofUsers = $('input[type=checkbox]:checked').length;
         var cnt=0;
          // alert(noofUsers)
       
  
   // var x=$('input[type=checkbox]:checked').val()
//       var x=document.favouritesform.chkusers[cnt].value;
            $("input:checkbox[name=chkusers]:checked").each(function()
           {
    // add $(this).val() to your array
                userIds = userIds + "," +($(this).val())
        });
        userIds = userIds.substring(1,userIds.length)
//       alert(userIds)
         if (noofUsers)
           {
         $("#AssignmentDiv").dialog('open');
         $.post('userLayerAction.do?userParam=repAnddbrdsForUser&folderId='+grpid+'&userIds='+userIds,
            function(data){
                $('#AssignmentDiv').html(data)
                var jsonVar=eval('('+data+')')
                $("#AssignmentDiv").html(jsonVar.htmlStr).append("<table align='center' width='100%'><tr><td align='center'><input type='button' value='Done' class='navtitle-hover' onclick='assignReports()'></td></tr></table>");
                  isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                        grpColArray=jsonVar.memberValues

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

                        $(".sortable").sortable();

                       
            });
             }
                    else
                    {
                        alert("Please select atleast one user to Assign ")
                    }
             
     }
     
     function assignReports(){
//               var usersFolders="";
//               var user=new Array();
//               var userobj = document.forms.myForm.chkusers;
//               var usersFoldersUl=document.getElementById("sortable");
//                var userFoldersIds=usersFoldersUl.getElementsByTagName("li");
//
//                                for(var j=0;j<userobj.length;j++)
//                    {
//                        if(document.forms.myForm.chkusers[j].checked==true)
//                        {
//
//                            user[j]=document.forms.myForm.chkusers[j].value;
//                             alert(user);
//                        }
//                    }
//
//                for(var i=0;i<userFoldersIds.length;i++){
//                    alert(userFoldersIds[i].id);
//                    usersFolders=usersFolders+","+userFoldersIds[i].id.split("_")[0];
//                    alert(usersFolders)
//                }
                    
                 var reportIds=new Array;
                 var repoprtNames=new Array
              // var userobj = $('input[type=checkbox]:checked');
                    // var mbrIds='';
                var ulObj=document.getElementById("sortable");
                var liObj=ulObj.getElementsByTagName("li");
                var flag="true";
                for(var i=0;i<liObj.length;i++){
//                     var content
                    mbrIds=(liObj[i].id).split("~");
                   reportIds.push(mbrIds[0].replace("_li", "", "gi"));
                  // alert(reportIds)
                   $("#"+mbrIds[0].replace("_li", "", "gi")+"_table tr").each(function() {
                     repoprtNames.push($(this).find("td").eq(1).html());
                     //alert(repoprtNames)
                    }
                    )
                 }
                 if(reportIds==''){
                     alert('You must select at least one reports to be assigned ! ')
                 }
                 else {
                     $("#AssignmentDiv").dialog('close');
                   $.post('userLayerAction.do?userParam=saveUsersForReports&userIds='+userIds+'&reportIds='+reportIds,
            function(data){
                if(data=="true"){
                      alert("Favourite links updated successfully")
                   }
            });
   }
   }

     
     $("#AssignmentDiv").dialog({
                        autoOpen: false,
                        height: 530,
                        width: 580,
                        position: 'justify',
                        modal: true
                    });
    </script>
         <form name="favouritesform" method="post">
            <table style="width:100%">  <tr>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                    <td align="right"><h5><%=TranslaterHelper.getTranslatedInLocale("roles", cle)%></h5></td>
                    <td align="left">
                        <h5><select id="bsrSelect" name="BusinessRoles" onchange="UsersforRoles(this.id)">
                    <%if(pbro.getRowCount()>0){%>
                  <option value="select"> ---Select--- </option>
       
                <%for(int i=0;i<pbro.getRowCount();i++){%>

                <option value="<%=pbro.getFieldValueString(i,1)%>~<%=pbro.getFieldValueString(i,0)%>">

                    <%=pbro.getFieldValueString(i,2)%></option>
          <%}
             }
           }  catch (Exception e) {
                                    e.printStackTrace();
                       }%>
            </select></h5>
                    </td>
                </tr>
           </table>
       <div id="usersDiv">
           
       </div>
     </form>
            
            <div id="AssignmentDiv" STYLE='display:none'>

        </div>
    
    </body>
</html>
