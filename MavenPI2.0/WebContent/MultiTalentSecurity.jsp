<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale"%>
<%--
    Document   : multiTalentSecurity
    Created on : Jan 18, 2012, 10:52:01 AM
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
           $("#deleteSecurityDiv").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 800,
                        position: 'justify',
                        modal: true
                    });

         var compName = "";
         var secVal = "";
        $(document).ready(function(){
            getSecCheckDet("dim");
            secVal = "dim";
        });
//

       function getSecCheckDet(id){
           var val=document.securityForm.secCheck.value;
           var htmlVar1 = "";
          $("#multisecDetTabBody1").html("")
           if(id=='dim'){
               secVal = "dim";
               htmlVar1+="<table id=\"multimeasureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%\">\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                    <td align=\"left\"><select name=\"connections13\" id=\"connections13\" onchange=\"getFolderDetails13()\"></select></td>\n\
                                 </tr>\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"foldersSelect13\" id=\"foldersSelect13\" onchange=\"getDimDetails1()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"dimSelect\" id=\"dimSelect\" onchange=\"getDimMemberDetails1()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Members", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                 <td align=\"left\"><select name=\"memberSelect\" id=\"memberSelect\" onchange=\"\"></select></td>\n\
                                 </tr>\n\
                            </table>";
           }
           else {
               secVal = "fact";
               htmlVar1+="<table id=\"multimeasureDetTab\" class=\"tablesorter\"  border=\"1\" cellpadding=\"1\" cellspacing=\"1\" style=\"width: 100%\">\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                    <td align=\"left\"><select name=\"connections13\" id=\"connections13\" onchange=\"getFolderDetails13()\"></select></td>\n\
                                 </tr>\n\
                                <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"foldersSelect13\" id=\"foldersSelect13\" onchange=\"getFactsDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Facts", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                     <td align=\"left\"><select name=\"factSelect\" id=\"factSelect\" onchange=\"getFactMemDetails()\"></select></td>\n\
                                 </tr>\n\
                                 <tr><td align=\"left\" class=\"migrate\"><label><%=TranslaterHelper.getTranslatedInLocale("Members", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>\n\
                                 <td align=\"left\"><select name=\"memberSelect\" id=\"memberSelect\" onchange=\"\"></select></td>\n\
                                 </tr>\n\
                            </table>";
           }
           $("#multisecDetTabBody1").html(htmlVar1)
            $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllConnectionForModifyMeasure",function(data){
                      $("#connections13").html(data)
          });
       }

       function getFolderDetails13(){
            var connectionID= $("#connections13").val()
                $("#foldersSelect13").html("")
                $("#dimSelect").html("")
                $("#memberSelect").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                    $("#foldersSelect13").html(data)
                });
       }
       function getDimDetails1(){
            var connectionID= $("#connections13").val()
            var folderID= $("#foldersSelect13").val()
            $("#dimSelect").html("")
            $("#memberSelect").html("")
                $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
                    $("#dimSelect").html(data)
                });
       }
       function getDimMemberDetails1(){
        var htmlVar=""
        var connectionID= $("#connections13").val()
        var folderID= $("#foldersSelect13").val()
        var dimId=$("#dimSelect").val()
        $("#memberSelect").html("")
        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimMemberDetails&connectionID="+connectionID+"&folderID="+folderID+"&dimId="+dimId ,function(data){
            $("#memberSelect").html(data)
          });
        }
        function getFactsDetails(){
            var connectionID= $("#connections13").val()
            var folderID= $("#foldersSelect13").val()
               $("#factSelect").html("")
               $("#memberSelect").html("")
               $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFactTabDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
               $("#factSelect").html(data)
            });
         }
         function getFactMemDetails(){
        var htmlVar=""
        var connectionID= $("#connections13").val()
        var foldersSelected= $("#foldersSelect13").val()
     // alert(foldersSelected13);
        var tablesSelected= $("#factSelect").val()
        $("#memberSelect").html("")
        $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFactMeasureDetails&connectionID="+connectionID+"&foldersSelected="+foldersSelected+"&tablesSelected="+tablesSelected,function(data){

                $("#memberSelect").html(data)
          });
        }

       function applySecurity() {
           var ctxPath = '<%= request.getContextPath()%>';
           var secCheck=secVal;
           var connectionID= $("#connections13").val()
           var elementId= $("#memberSelect").val()
           $.post('userLayerAction.do?userParam=applySecurity&elementId='+elementId,function(data)  {
                                if(data==1)
                                        alert('Details Saved Successfully')
                                else
                                        alert('Error ! Details Not Saved')
                                        $("#addNewConnDiv").dialog('close');
                                        window.location.href = window.location.href;
           });

       }

       function factSecurity() {
           $("#addDimSecurityDiv").dialog('open');
       }
       function closeDilog(id)
       {
              $("#"+id).dialog('close');
       }

       function delSecurity(){
        var inputs = document.getElementsByName("secCheck")
        var secCheck = '';
        var dimId= '';
        if (inputs[0].checked)
           secCheck = 'dim'
        else if (inputs[1].checked)
            secCheck = 'fact'

        if(secCheck=='dim'){
            dimId=$("#dimSelect").val()
        }
       if(secCheck=='fact'){
            dimId=$("#factSelect").val()
        }
        var connectionID= $("#connections13").val()
        var folderID= $("#foldersSelect13").val()
        var elementId= $("#memberSelect").val()

       if(connectionID == '--SELECT--'){
            alert('Please Select Connection Details')
        }
        else if(folderID == '--SELECT--'){
            alert('Please Select a Role')
        }
        else if(dimId== '--SELECT--'){
                 alert('Please Select a Fact')
        }
        else if(dimId== '0000'){
            alert('Please Select a dimension')
        }
        else if(elementId=='--SELECT--'){
            alert('Please Select a Member')
        }
    else {
        $.post("<%=request.getContextPath()%>/userLayerAction.do?userParam=getSecurityVal&elementId="+elementId,
                function(data){
                   if(data!='null'){
                       var jsonVar=eval('('+data+')')
                       var secKeyId=jsonVar.secKeyId;
                       var bussTabId=jsonVar.bussTabId;
                       var bussColId=jsonVar.bussColId;
                       var bussTableName=jsonVar.bussTableName;
                       var bussColName=jsonVar.bussColName;
                       var html="";
                       html+="<table width='100%'><thead class='migrate'><th>Check to Delete</th><th>Buss Table Name</th><th>Buss Column Name</th></thead><tbody>"
                        html+="<tr><td>&nbsp;</td></tr>";
                       for(var i=0;i<secKeyId.length;i++){

                          html+="<tr><td><input type=\'checkbox\' id=\'secKeyId"+secKeyId[i]+"\' name=\'secKeyId\'  value="+secKeyId[i]+"></td>";
                          html+="<td align='center'>"+bussTableName[i]+"</td>";
                          html+="<td align='center'>"+bussColName[i]+"</td></tr>";
                       }
                       html+="<tr><td width=\'20px;\'>&nbsp;</td></tr><tr><td>&nbsp;</td><td align='right'><input class='navtitle-hover' type=\'button\' value='Delete' onclick=deleteSecurity()></td></tr>";
                   }
                   if(secKeyId.length>0){
                      $("#deleteSecurityDiv").html(html);
                      $("#deleteSecurityDiv").dialog('open');
                   }
                   else {
                       alert('No Security Applied for Selected Details')
                   }

                });


            }

       }
       function deleteSecurity(){
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
                         parent.$("#deleteSecurityDiv").dialog('close');
                        $("#loadingmetadata").show();
                    $.ajax({
                    url:'<%=request.getContextPath()%>/userLayerAction.do?userParam=deleteSecurityVal&filterId='+filterId,
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
                <form id="securityForm" name="securityForm" method="post" action="">
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="1" cellspacing="1">
                       <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Please_Select_Security_Type", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                <td align="left" style="background-color:lightgoldenrodyellow; color:black;">
                                        <input type="radio" name="secCheck" value="dim" id="dim" checked onchange="getSecCheckDet(this.id)"> <%=TranslaterHelper.getTranslatedInLocale("Dimension_Security", cl)%>
                                        <input type="radio" name="secCheck" value="fact" id="fact" onchange="getSecCheckDet(this.id)"> <%=TranslaterHelper.getTranslatedInLocale("Fact_Security", cl)%>
                                </td>
                       </tr>
                    </table>
                    <br><br>
                    <table align ="center" id="secDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0">
                        <tr><td id ="multisecDetTabBody1">
                                <table id="multimeasureDetTab" class="tablesorter"  border="0" cellpadding="0" cellspacing="0" style="width: 100%">
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Select_Connection", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                    <td align="left"><select name="connections13" id="connections13" onchange="getFolderDetails13()"></select></td>
                                 </tr>
                                <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("roles", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="foldersSelect13" id="foldersSelect13" onchange="getDimDetails1()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                     <td align="left"><select name="dimSelect" id="dimSelect" onchange="getDimMemberDetails1()"></select></td>
                                 </tr>
                                 <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Members", cl)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                                 <td align="left"><select name="memberSelect" id="memberSelect" onchange=""></select></td>
                                 </tr>
                                 </table>
                            </td></tr>
                    </table>
                    <br> <br>
                    <table><tr>
                            <td>
                                <img width="22px" height="22px" border="0" src="icons pinvoke/cross-small.png" alt="" title="Delete Security" onclick="delSecurity()">
                            </td>
                            <td>&nbsp;&nbsp;</td><td><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("Apply_Security", cl)%>" id="btnn" onclick="applySecurity()"/></td>
                           </tr>
                    </table>
                </form>
        </div>

        <div id="deleteSecurityDiv" title="Delete Security" style="display: none"></div>
        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
        </div>

    </body>

</html>