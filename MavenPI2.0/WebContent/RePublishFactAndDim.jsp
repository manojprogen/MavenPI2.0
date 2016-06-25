<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : RePublish Facts and Dimensions
    Created on : Oct 10, 2013, 5:52:01 PM
    Author     : Nazneen Khan
--%>
<!DOCTYPE html>

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

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
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />

     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>

        
    </head>
    <body>
        <script type="text/javascript">


         var compName = "";
         var secVal = "";
        $(document).ready(function(){
            getSecCheckDetails("dim");
            secVal = "dim";
        });
//

       function getSecCheckDetails(id){
           var val=document.rePublishForm.secCheck.value;
           var htmlVar1 = "";
          $("#secDetTabBody1").html("")
           if(id=='dim'){
               secVal = "dim";
               htmlVar1+="<table id=\"measureDetTab\" style=\"width: 100%\"  cellspacing=\"10\">\n\
                                <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                    <td align=\"left\"><select name=\"connections\" id=\"connections\" onchange=\"getFolderDetails()\"></select></td>\n\
                                 </tr>\n\
                                <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("roles", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"foldersSelect\" id=\"foldersSelect\" onchange=\"getDimDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"dimSelect\" id=\"dimSelect\"></select></td>\n\
                                </table>";
           }
           else {
               secVal = "fact";
               htmlVar1+="<table id=\"measureDetTab\" style=\"width: 100%\" cellspacing=\"10\">\n\
                                <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                    <td align=\"left\"><select name=\"connections\" id=\"connections\" onchange=\"getFolderDetails()\"></select></td>\n\
                                 </tr>\n\
                                <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("roles", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"foldersSelect\" id=\"foldersSelect\" onchange=\"getFactsDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" ><label><%=TranslaterHelper.getTranslatedInLocale("Facts", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"factSelect\" id=\"factSelect\"></select></td>\n\
                                 </table>";
           }
           $("#secDetTabBody1").html(htmlVar1)
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#connections").html(data)
          });
       }

       function getFolderDetails(){
            var connectionID= $("#connections").val()
                $("#foldersSelect").html("")
                $("#dimSelect").html("")
                $("#memberSelect").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                    $("#foldersSelect").html(data)
                });
       }
       function getDimDetails(){
            var connectionID= $("#connections").val()
            var folderID= $("#foldersSelect").val()
            $("#dimSelect").html("")
            $("#memberSelect").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
                    $("#dimSelect").html(data)
                });
       }
       function getFactsDetails(){
            var connectionID= $("#connections").val()
            var folderID= $("#foldersSelect").val()
               $("#factSelect").html("")
               $("#memberSelect").html("")
               $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFactTabDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
               $("#factSelect").html(data)
            });
         }



       function rePublish() {
           var ctxPath = '<%= request.getContextPath()%>';
           var secCheck=secVal;
           var connectionID= $("#connections").val()
           var foldersSelect= $("#foldersSelect").val()
           if(secCheck=='dim'){
               var dimSelect= $("#dimSelect").val()
               $.post('userLayerAction.do?userParam=rePublishDimension&connectionID='+connectionID+"&foldersSelect="+foldersSelect+"&dimSelect="+dimSelect,function(data)  {
                                    if(data==1)
                                            alert('Dimension Re-Published Successfully')
                                    else
                                            alert('Error ! Dimension not Re-Published')
                                            window.location.href = window.location.href;
               });
           }
           else {
               var factSelect= $("#factSelect").val()
               $.post('userLayerAction.do?userParam=rePublishFact&connectionID='+connectionID+"&foldersSelect="+foldersSelect+"&factSelect="+factSelect,function(data)  {
                                    if(data==1)
                                            alert('Dimension Re-Published Successfully')
                                    else
                                            alert('Error ! Dimension not Re-Published')
                                            window.location.href = window.location.href;
               });
           }

       }
//        function rePublishFact() {
//           var ctxPath = '<%= request.getContextPath()%>';
//           var connectionID= $("#connections").val()
//           var foldersSelect= $("#foldersSelect").val()
//           var factSelect= $("#factSelect").val()
//           $.post('userLayerAction.do?userParam=rePublishFact&connectionID='+connectionID+"&foldersSelect="+foldersSelect+"&factSelect"+factSelect,function(data)  {
//                                if(data==1)
//                                        alert('Dimension Re-Published Successfully')
//                                else
//                                        alert('Error ! Dimension not Re-Published')
//                                        window.location.href = window.location.href;
//           });
//
//       }

       </script>
  <div style="width: 676px;">
                <form id="rePublishForm" name="rePublishForm" method="post" action="">
                    <table align ="center" id="secDetTab1" >
                       <tr><td align="left"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Re_Publish", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                <td align="left" style="background-color:lightgoldenrodyellow; color:black;">
                                        <input type="radio" name="secCheck" value="dim" id="dim" checked onchange="getSecCheckDetails(this.id)"> <%=TranslaterHelper.getTranslatedInLocale("On_Dimension", cle)%>
                                        <input type="radio" name="secCheck" value="fact" id="fact" onchange="getSecCheckDetails(this.id)"  disabled="true"> <%=TranslaterHelper.getTranslatedInLocale("On_Fact", cle)%>
                                </td>
                       </tr>
                    </table>
                    <br><br>
                    <div style="width: 676px;">
                    <table align ="center" id="secDetTab1">
                        <tr><td id ="secDetTabBody1">
                                <table id="measureDetTab1"  style="width: 100%" cellspacing="10" cellpadding="10">
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                    <td align="left"><select name="connections" id="connections" onchange="getFolderDetails()"></select></td>
                                 </tr>
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="foldersSelect" id="foldersSelect" onchange="getDimDetails()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="dimSelect" id="dimSelect"></select></td>
                                 </tr>
                                 </table>
                            </td></tr>
                    </table>
                    </div>
                    <br> <br>
                    <table><tr>
                            <td>&nbsp;&nbsp;</td><td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Re_Publish", cle)%>" id="btnn" onclick="rePublish()"/></td>
                           </tr>
                    </table>
                </form>
        </div>



        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
        </div>

    </body>

</html>
