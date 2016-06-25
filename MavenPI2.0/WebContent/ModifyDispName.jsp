<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Locale,com.progen.i18n.TranslaterHelper"%>
<%--
    Document   : Modify Disp Name
    Created on : Nov 12, 2013, 03:50:01 PM
    Author     : Nazneen Khan
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%
//added by Dinanath for default locale
                    Locale cl=null;
                   cl=(Locale)session.getAttribute("UserLocaleFormat");
String themeColor = "";
if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String DefaultArrregations[] = {"--", "sum", "avg", "min", "max", "count", "COUNTDISTINCT"};
            String userId="";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));
            String contextPath=request.getContextPath();

%>

<html>
    <head>
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=contextPath%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=contextPath%>/javascript/quicksearch.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/jquery.tablesorter.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
         <script type="text/javascript"  language="JavaScript" src="<%=contextPath%>/tablesorter/jquery.columnfilters.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/tablesorter/docs/js/docs.js"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>




         <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
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
        <script type="text/javascript">

            //var assinIdAndVales=new Array
            //var isMemberUseInOtherLevel="false"
             $(window).resize(function(){
                resizePage("modifyDispName");
            });
            $(document).ready(function(){ 
                resizePage("modifyDispName");  
<%--
                $("#popupwindow").dialog({
                        //bgiframe: true,
                        autoOpen: false,
                        height: 500,
                        width: 500,
                        position: 'top',
                        modal: true
                    });
                    $('#popupwindow').dialog('open');
--%>


    $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#conn").html(data)
                 });

                });
                function getFolderDetails(){
                   var connectionID= $("#conn").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                       $("#folderId").html(data)
                  });
                }                
                function getAllFactsDetails(){
                 var htmlVar=""
                 $("#measureDetTabBody1").html(htmlVar)
                    var connectionID= $("#conn").val()
                    var folderID= $("#folderId").val()
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllFactsDetails&connectionID="+connectionID+"&folderID="+folderID ,function(data){
               var jsonVar=eval('('+data+')')
               var bussTableId=jsonVar.bussTableId
               var bussTableName=jsonVar.bussTableName
               var tableDispName=jsonVar.tableDispName

               for(var i=0;i<bussTableId.length;i++)
                   {
                       htmlVar+="<tr> <td><input type=\"text\" name=\"sNo\" value=\""+(i+1)+"\" id=\"sNO"+i+"\" readonly style=\"width: 40px\"/></td>\n\
                                      <td><input type=\"text\" name=\"bussTableName\" value=\""+bussTableName[i]+"\" id=\"bussTableName"+i+"\" readonly /></td>\n\
                                      <td><input type=\"text\" name=\"oldTableDispName\" value=\""+tableDispName[i]+"\" id=\"oldTableDispName"+i+"\" readonly /></td>\n\
                                      <td><input type=\"text\" name=\"newTableDispName\" value=\""+tableDispName[i]+"\" id=\"newTableDispName"+i+"\"/></td></tr>\n\
                                      <input type=\"hidden\" name=\"bussTableId\" id=\"bussTableId\" value=\""+bussTableId[i]+"\">";
                        $("#measureDetTabBody1").html(htmlVar)
                   }
                   });
                }

           function saveModifyDispName(){
                var conid=$("#conn").val()
                 var folderID= $("#folderId").val()
                if(conid=='--SELECT--')
                    alert('Please Select Connection ! ')
                else {
                    var retVal = confirm("Are you sure you want to modify changes !");
                    if (retVal == true){
                     $("#loadingmetadata").show();
                      $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=modifyTableDispName&conid="+conid+"&folderID="+folderID, $("#factDispNameForm").serialize(), function(data)
                      {
                          if(data==1)
                           alert('Changes saved Successfully')
                          else
                           alert('Error ! Changes not saved.')
                       
                          $("#loadingmetadata").hide();
                          getAllFactsDetails();
                    });
              }}
           }
         </script>

    </head>


    <body>

        <form id="factDispNameForm" name="factDispNameForm" method="post" action="">
            <table align="left">
                <tr valign="top">
                    <td><%=TranslaterHelper.getTranslatedInLocale("connection", cl)%>:&nbsp;&nbsp;&nbsp;<select name="conn" id="conn" onchange="getFolderDetails()"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("roles", cl)%>:&nbsp;&nbsp;&nbsp;<select name="folderId" id="folderId"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Show_Members", cl)%>" onclick="getAllFactsDetails()"></td>
                </tr>
            </table><br/><br/><br/>

            <div align="center" style=" width:60% ">
                <form id="modifyDispNameForm" name="modifyDispNameForm" method="post" action="">
                    <table id="measureDetTab" class="tablesorter"  border="1" cellpadding="1" cellspacing="1">
                  <thead>
                        <tr>
                            <th class="migrate" style="width: 40px"><%=TranslaterHelper.getTranslatedInLocale("S_No", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("table_name", cl)%></th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Display_names_old", cl)%></th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Display_names_Modified", cl)%></th>
                        </tr>
                    </thead>
                    <tbody id="measureDetTabBody1">
                    </tbody>
<!--                    <div id="popupwindow"></div>-->
                </table>
                </form>
            </div>
            <center><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("save", cl)%>" id="btnn" onclick="saveModifyDispName()"/></center>
        </form>
 <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
    </body>
</html>
