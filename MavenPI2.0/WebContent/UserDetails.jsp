<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.metadata.Cube"%>
<%--
    Document   : UserDetails
    Created on : Sep 30, 2013, 2:21:05 PM
    Author     : Administrator
--%>
<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));

        //added by Dinanath dec 2015
            Locale cle = null;
            cle = (Locale) session.getAttribute("UserLocaleFormat");
            String contextPath=request.getContextPath();
         %>

         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/Green/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/Green/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <script type="text/javascript" src="<%=contextPath%>//dragAndDropTable.js"></script>

     <style type="text/css">
          .migrate
            {
                font-size: 10pt;
                font-weight: bold;
                color: white;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
               
            }
      </style>

       
    </head>
    <body>
        <script type="text/javascript">

             $(document).ready(function(){
                showLoginUsers();
        });

     function showLoginUsers(){
             $("#measureDetTabBody11").html("")
            $("#loadingmetadata11").show();
              $("#firstname11").html("")
              $("#lastname11").html("")
               $("#datetime11").html("")
                $("#pu_email").html("")
              $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getUseridDetails",function(data){
               $("#firstname11").html(data)
               $("#lastname11").html(data)
                $("#datetime11").html(data)
                 $("#pu_email").html(data)
                   var jsonVar=eval('('+data+')')
                   firstname11=jsonVar.firstname11
                   lastname11=jsonVar.lastname11
                    datetime11=jsonVar.datetime11
                     pu_email=jsonVar.pu_email
                   htmlVar = "";
                   for(var i=0;i<firstname11.length;i++)
                   {
                       htmlVar+="<tr><td><input type='text' value='"+firstname11[i]+"'  size='15' readonly/></td>\n\
                                      <td><input type='text' value='"+lastname11[i]+"' size='15' readonly/></td>\n\
                                       <td><input type='text' value='"+pu_email[i]+"' size='40' readonly/></td>\n\
                                       <td><input type='text' value='"+datetime11[i]+"' readonly/></td></tr>"
                        $("#measureDetTabBody11").html(htmlVar)
                   }
                   $("#loadingmetadata11").hide();
                });
              }


       </script>
          <div style="width: 850px;">
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style=" width: 100%">
                       <tr>
                                <td ALIGN="right" valign="top" >
                                    <i class="fa fa-refresh fa-2x" id="REFRESH" name="REFRESH" style="cursor:hand" onClick="showLoginUsers()" title="Refresh List" ></i>

                                &nbsp;&nbsp;</td>
                       </tr>
                   </table>
</div>


      <div style="width: 850px;">
         <table id="measureDetTab11" class="tablesorter"  border="1px solid" cellpadding="1" cellspacing="1">
                    <thead>
                        <tr>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("First_Name", cle)%> </th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Last_Name", cle)%> </th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("User_Email", cle)%> </th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Login_Date_Time", cle)%> </th>
                       </tr>
                    </thead>
                    <tbody id="measureDetTabBody11">
                    </tbody>
                    <div id="popupwindow11"></div>
                </table>
      </div>

 <div id='loadingmetadata11' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
    </body>

</html>