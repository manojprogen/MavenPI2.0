<%--
    Document   : modifyMeasures
    Created on : Aug 8, 2011, 10:50:01 AM
    Author     : malli
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.ArrayList"%>

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

     //Added By Ram
         ReportTemplateDAO dao=new ReportTemplateDAO();
         ArrayList DimsList=new ArrayList();
         PbReturnObject dimsdata=new PbReturnObject();
          dimsdata=dao.getDimsData();
         for(int i=0;i<dimsdata.getRowCount();i++)
         {
             DimsList.add(dimsdata.getFieldValueString(i, 0));
         }
//Endded by Ram
%>

<html>
    <head>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
         <script type="text/javascript"  language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/docs.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pi.js"></script>




         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
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
                resizePage("modifyMembers");
            });
            $(document).ready(function(){ 
                resizePage("modifyMembers");  
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
                      $("#connectionsModMem").html(data)
                 });

                });
                //Added by Ram For Language Lookup
            function FuncToCall(el){
                     var val = el.id;
                     var lastChar = val.substring(9);
                     var getval=document.getElementById("newmemname"+lastChar).value;
                      if (document.getElementById(val).checked) {
               $.ajax({
                    
                url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=enableLookup&getval='+getval,
                success:function(data)
                {
                     if(data!="null")
                    alert(data);
                 }
                });
        } else {
           $.ajax({
                    
                url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=unableLookup&getval='+getval,
                success:function(data)
                {
                     if(data!="null")
                    alert(data);
                 }
                });
        }
                  
                }
                function getFolderDetails(){
                   var connectionID= $("#connectionsModMem").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getFolderDetails&connectionID="+connectionID,function(data){
                       $("#foldersSelectModMem").html(data)
                  });
                }
                function getDimDetails(){
                   var connectionID= $("#connectionsModMem").val()
                   var folderID= $("#foldersSelectModMem").val()

                   $.get("<%= request.getContextPath()%>/userLayerAction.do?userParam=getDimDetails&connectionID="+connectionID+"&folderID="+folderID,function(data){
                       $("#dimSelectModMem").html(data)
                  });
                }
                function getAllMembers(){
                 var htmlVar=""
                 $("#measureDetTabBodyModMem").html(htmlVar)
                    var connectionID= $("#connectionsModMem").val()
                    var folderID= $("#foldersSelectModMem").val()
                    var dimId=$("#dimSelectModMem").val()

               $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getAllMembers&connectionID="+connectionID+"&folderID="+folderID+"&dimId="+dimId ,function(data){
               var jsonVar=eval('('+data+')')
               var elementidlist=jsonVar.elementidlist
               var dimnamelist=jsonVar.dimnamelist
               var buscolnamelist=jsonVar.buscolname
               var membernamelist=jsonVar.membernamelist
               var memberidlist=jsonVar.memberidlist
               var folderidlist=jsonVar.folderidlist
               var subfolderidlist=jsonVar.subfolderidlist

              var factsArray = [<% for (int i = 0; i < DimsList.size(); i++) { %>"<%= DimsList.get(i) %>"<%= i + 1 < DimsList.size() ? ",":"" %><% } %>];
            
            var countarr = new Array();
               for(var i=0;i<membernamelist.length;i++)
                   {
                       for(var j=0;j<factsArray.length;j++)
                           {
                              if(factsArray[j]==membernamelist[i])
                              countarr.push(i);
                           }
                      
                       
                       htmlVar+="<tr><td><input type='checkbox' name='chkgrp' id='chkgrp"+i+"' value='chkgrp"+i+"' disabled ></td></td>\n\
                                     <td><input type=\"checkbox\"  value=\"chkrpt"+i+"\" id=\"chkrpt"+i+"\" name=\"chkrpt\"></td>\n\
	                              <td><input type=\"checkbox\"  value=\"chklookup"+i+"\" id=\"chklookup"+i+"\" name=\"chklookup\" onclick=\"return FuncToCall(this)\"></td>\n\
                                      <td><input type=\"text\" name=\"dimnamelist\" value=\""+dimnamelist[i]+"\" id=\"dimnamelist"+i+"\" readonly/></td>\n\
                                      <td><input type=\"text\" name=\"buscolname\" value=\""+buscolnamelist[i]+"\" id=\"buscolname"+i+"\" readonly /></td>\n\
                                      <td><input type=\"text\" name=\"oldmemname\" value=\""+membernamelist[i]+"\" id=\"oldmemname"+i+"\" readonly /></td>\n\
                                      <td><input type=\"text\" name=\"newmemname\" value=\""+membernamelist[i]+"\" id=\"newmemname"+i+"\"/></td></tr>\n\
                                      <input type=\"hidden\" name=\"elementid\" id=\"elementid\" value=\""+elementidlist[i]+"\">\n\
                                      <input type=\"hidden\" name=\"memberid\" id=\"memberid\" value=\""+memberidlist[i]+"\">\n\
                                      <input type=\"hidden\" name=\"folderid\" id=\"folderid\" value=\""+folderidlist[i]+"\">\n\
                                      <input type=\"hidden\" name=\"subfolderid\" id=\"subfolderid\" value=\""+subfolderidlist[i]+"\">"
                        $("#measureDetTabBodyModMem").html(htmlVar)
                         if (i==membernamelist.length-1){
                          for(var k=0;k<countarr.length;k++)
                              {
                                  document.getElementById("chklookup"+countarr[k]).checked = true;
                   }
                         
                  }
                   }
                   });
                }

           function saveModifyMembers(){
                var conid=$("#connnames").val()
              $.post("<%= request.getContextPath()%>/userLayerAction.do?userParam=getModifyMembers&conid="+conid, $("#memberModifyForm").serialize(), function(data)
              {
//                  if(data=="true"){
//                        alert("Members Renamed Successfully.")
//
//                    }else{
//                        alert("Error! Members not Renamed")
//                    }
alert("Members Renamed Successfully.")
              $('#tabs').tabs('load', 10);
              });
           }
         </script>

    </head>


    <body>

        <form id="memberModifyForm" name="memberModifyForm" method="post" action="">
            <table align="left">
                <tr valign="top">
                    <td><%=TranslaterHelper.getTranslatedInLocale("connection", cl)%>:&nbsp;&nbsp;&nbsp;<select name="connectionsModMem" id="connectionsModMem" onchange="getFolderDetails()"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("roles", cl)%>:&nbsp;&nbsp;&nbsp;<select name="foldersSelectModMem" id="foldersSelectModMem" onchange="getDimDetails()"></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=TranslaterHelper.getTranslatedInLocale("dimensions", cl)%>:&nbsp;&nbsp;&nbsp;<select name="dimSelectModMem" id="dimSelectModMem" ></select></td>
                    <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="navtitle-hover" value="<%=TranslaterHelper.getTranslatedInLocale("Show_Members", cl)%>" onclick="getAllMembers()"></td>
                </tr>
            </table><br/><br/><br/><br/>

            <div align="center" style=" width:60% ">
                <form id="modifyMemberForm" name="modifyMemberForm" method="post" action="">
                    <table id="measureDetTab" class="tablesorter"  border="1" cellpadding="1" cellspacing="1">
                  <thead>
                        <tr>

                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("ch_in_group", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("ch_in_report", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("Lookup_enable", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("Dimension_Names_in_report", cl)%></th>
                            <th class="migrate" ><%=TranslaterHelper.getTranslatedInLocale("buss_col_name_in_report", cl)%></th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Members_names_OLD", cl)%></th>
                            <th class="migrate"><%=TranslaterHelper.getTranslatedInLocale("Members_names_Modified", cl)%></th>
                        </tr>
                    </thead>
                    <tbody id="measureDetTabBodyModMem">
                    </tbody>
<!--                    <div id="popupwindow"></div>-->
                </table>
                </form>
            </div>
            <center><input type="button" class="migrate" style="width:auto;color:black" value="<%=TranslaterHelper.getTranslatedInLocale("save", cl)%>" id="btnn" onclick="saveModifyMembers()"/></center>
        </form>

    </body>
</html>
