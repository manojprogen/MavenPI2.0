<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : Modify Disp Dim Name
    Created on : Oct 26, 2015, 12:48:01 PM
    Author     : Krishan Pratap Yadav
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
      

    </head>


    <body>
  <script type="text/javascript">

            //var assinIdAndVales=new Array
            //var isMemberUseInOtherLevel="false"
            $(window).resize(function(){
                resizePage("modifyDispDimName");
            });
            $(document).ready(function(){ 
                resizePage("modifyDispDimName");  
                 $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#conn1").html(data)
                 });

                });
                function getFolderDetails(){
                   var connectionID= $("#conn1").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                       $("#folderId1").html(data)
                  });
                }
                function getAllDimsDetails(){
                 var htmlVar=""
                 $("#measureDetTabBody2").html(htmlVar)
                    var connectionID= $("#conn1").val()
                    var folderID= $("#folderId1").val()
                    if(connectionID=='--SELECT--'||folderID=='--SELECT--')
                    alert('Please Select Connection And Roles ! ')
               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllDimsDetails&connectionID="+connectionID+"&folderID="+folderID ,function(data){
               var jsonVar=eval('('+data+')')
               var bussTableId=jsonVar.bussTableId
               var bussTableName=jsonVar.bussTableName
               var tableDispName=jsonVar.tableDispName

               for(var i=0;i<bussTableId.length;i++)
                   {
                       htmlVar+="<tr> <td><input type=\"text\" name=\"sNo\" value=\""+(i+1)+"\" id=\"sNO"+i+"\" readonly style=\"width: 40px\"/></td>\n\
                                      <td><input type=\"text\" name=\"oldTableDispName\" value=\""+bussTableName[i]+"\" id=\"oldTableDispName"+i+"\" readonly /></td>\n\
                                      <td><input type=\"text\" name=\"newTableDispName\" value=\""+tableDispName[i]+"\" id=\"newTableDispName"+i+"\"/></td></tr>\n\
                                      <input type=\"hidden\" name=\"bussTableId\" id=\"bussTableId\" value=\""+bussTableId[i]+"\">";
                        $("#measureDetTabBody2").html(htmlVar)
                   }
                   });
                }

           function saveModifyDispName(){

             
                var conid=$("#conn1").val()
                 var folderID= $("#folderId1").val()
                if(conid=='--SELECT--')
                    alert('Please Select Connection ! ')
                else {
                    var retVal = confirm("Are you sure you want to modify changes !");
                    if (retVal == true){
                     $("#loadingmetadata").show();
                      $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=modifyTableDispDimName&conid="+conid+"&folderID="+folderID, $("#dimDispNameForm").serialize(), function(data)
                      {
                          if(data==1)
                           alert('Changes saved Successfully')
                          else
                           alert('Error ! Changes not saved.')

                          $("#loadingmetadata").hide();
                          getAllDimsDetails();
                    });
              }}
           }
         </script>
        <form id="dimDispNameForm" name="dimDispNameForm" method="post" action="">
            <table align="left">
                <tr valign="top">
                    <td><%=TranslaterHelper.getTranslatedInLocale("connection", cl)%>:&nbsp;&nbsp;&nbsp;<select name="conn1" id="conn1" onchange="getFolderDetails()"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("roles", cl)%>:&nbsp;&nbsp;&nbsp;<select name="folderId1" id="folderId1"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Show_Members", cl)%>" onclick="getAllDimsDetails()"></td>
                </tr>
            </table><br/><br/><br/>

            <div align="center" style=" width:50% ">
                <form id="modifyDispNameForm" name="modifyDispNameForm" method="post" action="">
                    <table id="measureDetTab1" class="tablesorter"  border="1" cellpadding="1" cellspacing="1">
                  <thead>
                        <tr>
                            <th class="migrate" style="width: 40px"><%=TranslaterHelper.getTranslatedInLocale("S_No", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("Dimension_Name", cl)%></th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Display_Dim_Names", cl)%></th>
                            
                        </tr>
                    </thead>
                    <tbody id="measureDetTabBody2">
                    </tbody>

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

