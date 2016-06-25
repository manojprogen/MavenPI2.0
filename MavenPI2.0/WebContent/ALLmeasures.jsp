<%-- 
    Document   : ALLmeasures
    Created on : Mar 9, 2016, 12:16:12 PM
    Author     : Sruthi
--%>

<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="prg.db.Container"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
String reportId = request.getParameter("reportId");
 String pagesize=request.getParameter("slidePages");
 String contextPath=request.getContextPath();
 String themeColor = "blue";
  Locale cL=null;
  Container container = null;
 PbReportCollection pbcollect=null;
 HashMap TableHashMap = null;
  ArrayList<String> measureid = new ArrayList<String>();
  ArrayList<String> measureId = new ArrayList<String>();
  ArrayList<String> MeasuresNames=new ArrayList<String>();
 cL=(Locale)session.getAttribute("UserLocaleFormat");
String[] NbrFormats = {"Ab", "Nf", "K", "M","L","Cr"};
  String[] NbrFormatsDisp = {"Absolute", "No Format", "Thousands(K)", "Millions(M)","Lakhs(L)","Crores(Cr)"};
   String[] Round={"0","1","2","3","4","5"};
   String[] Round_D={"No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"};
   String colour[]={"#109618"};
  String Align[]={"Center","Left","Right"};
   String display[]={"Y","N"};
   String displaystyle[]={"Header","Data"};
   ArrayList<String> alldetails=null;
   if (reportId != null) {
         if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
           HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");  
            container = (Container) map.get(reportId);
            pbcollect = container.getReportCollect();
            alldetails=container.getAllDetails();
            TableHashMap = container.getTableHashMap();
             MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                measureId=(ArrayList) TableHashMap.get("Measures");
                  for(int ids=0;ids<measureId.size();ids++){
                             measureid.add(measureId.get(ids).replace("A_", ""));
                  }
         }
%>
<html>
    <head>
         <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
            <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
              <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
              <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.v3.min.js"></script>
                <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>
                   <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
                <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
                <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
                <link type="text/css" href="<%=contextPath%>/css/global.css" rel="stylesheet" />
                <script type="text/javascript" src="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
                <link rel="stylesheet" href="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" /> 
        <title>ALLmeasures</title>
    </head>
    <body>
        <div id='tabProperties' class='tag-link-properties' style='width:100%;height:5.5%;'>
    <ul id="quickTabs" >
        <li class="tabUL"><a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="ALLid" onclick="ALLmeasures('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">ALL</a>
      <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="Measures" onclick="Measures('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">Measures</a>
         <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="Dimenstions" onclick="Dimenstions('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">Dimenstions</a></li>
    </ul>

</div>
        <table  id="ALLmeasures" style="border-collapse: separate;border-spacing: 26px 15px;">
            <tr>
                <th width="15%">
                </th>
                <th style="text-align:left;font:11px verdana"><h3><%=TranslaterHelper.getTranslatedInLocale("All", cL)%></h3></th>
            </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Number_Format", cL)%></th>
                 <td>
                      <select name="NumberFormatALL" id='NumberFormatALL' style="width: 100px;">
                          <%
                          String NbrFormatall="";
                          if(!alldetails.isEmpty()&&alldetails!=null)
                           NbrFormatall=alldetails.get(0).trim();
                          for (int ab = 0; ab < NbrFormats.length; ab++){
                                  if (NbrFormatall.equalsIgnoreCase(NbrFormats[ab])) {%>
                                   <option selected value="<%=NbrFormats[ab]%>"><%=NbrFormatsDisp[ab]%></option>
                                   <%} else{%>
                                  <option value="<%=NbrFormats[ab]%>"><%=NbrFormatsDisp[ab]%></option>
                                  <%}}%>
                        </select>
                 </td>
            </tr>
            <tr>
                  <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Number_Rounding", cL)%></th>
                   <td>
                    <select name="NumberRoundingALL" id='NumberRoundingALL' style="width: 100px;">
                          <%
                               String val1="";
                               if(!alldetails.isEmpty() && alldetails!=null)
                                val1=alldetails.get(1).trim();
                          for (int rb = 0; rb < Round.length; rb++){
                                  if (val1.trim().equalsIgnoreCase(Round[rb])) {%>
                                   <option selected value="<%=Round[rb]%>"><%=Round_D[rb]%></option>
                                   <%} else{%>
                                  <option value="<%=Round[rb]%>"><%=Round_D[rb]%></option>
                                  <%}}%>
                                                  
                               
                        </select>
                </td>
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Append_Number_Format", cL)%></th>
                <td>
                <%
                String customdata="";      
                  if(!alldetails.isEmpty() && alldetails!=null)
                    customdata=alldetails.get(2).trim();
               if(customdata.equalsIgnoreCase("notdefined")){%>
                        <input type="text"  name="CustomHeaderALL" id="CustomHeaderALL" value="" style="width:95px;"/>
                         <%}else{%>
                    <input type="text"  name="CustomHeaderALL" id="CustomHeaderALL" value="<%=customdata%>" style="width:95px;"/>
                     <%}%>
                </td>
            </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Append_Number_Format_In", cL)%></th>
                  <td>
                      <select name="NumberHeaderALL" id='NumberHeaderALL' style="width: 100px;">
                          <%
                           String selected="Y";
                          if(!alldetails.isEmpty()&&alldetails!=null)
                           selected=alldetails.get(3).trim();
                          for (int ab = 0; ab < display.length; ab++){
                                  if (selected.equalsIgnoreCase(display[ab])) {%>
                                   <option selected value="<%=display[ab]%>"><%=displaystyle[ab]%></option>
                                   <%} else{%>
                                  <option value="<%=display[ab]%>"><%=displaystyle[ab]%></option>
                                  <%}}%>
                        </select>
                </td>
            </tr>
            <tr>
               <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Measure_Alias", cL)%></th>
               <td>
                   <input type="text" id="MeasureAliasALL" value="" disabled style="width:95px;"> 
                </td>
            </tr>
            <tr>
                  <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Font_Size", cL)%></th>
                  <td>
                     <%String value="";
                      if(!alldetails.isEmpty() && alldetails!=null)
                           value=alldetails.get(4);
                        if(value.equalsIgnoreCase("")) {%>
                     <input type="text" id="fontsizeALL" value="12" size="6px" style="width:95px;">
                  <%}else {%>
               <input type="text" id="fontsizeALL" value="<%=value%>" size="6px" style="width:95px;">
               <%}%>
                </td>
            </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Size", cL)%></th>
                   <td>
                    <%
                    String headerval="";
                    if(!alldetails.isEmpty() && alldetails!=null)
                        headerval=alldetails.get(5);
                   if(headerval.equalsIgnoreCase("")){%>
                     <input type="text" id="HeadersizeALL" value="12" size="6px" style="width:95px;" >
                <%} else {%>
                <input type="text" id="HeadersizeALL" value="<%=headerval%>" size="6px" style="width:95px;">
                     <%}%>
                </td>
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Align", cL)%></th>
                 <td>
                     <select name="HeaderAlignALL" id="HeaderAlignALL" onchange="myalignfunction()" style="width: 100px;">
                 <%
                     String align1="";
                 if(!alldetails.isEmpty() && alldetails!=null)
                     align1=alldetails.get(6).trim();
                 for (int ag1 = 0; ag1 < Align.length; ag1++){
                     if(align1.equalsIgnoreCase(Align[ag1])){%>
                              <option selected value="<%=Align[ag1]%>"><%=Align[ag1]%></option>
                                <%}else{%>
                                 <option  value="<%=Align[ag1]%>"><%=Align[ag1]%></option>
                                <%}}%>
                         </select>
                 </td>
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Align", cL)%></th>
                 <td>
                     <select name="DataAlignALL" id="DataAlignALL" onchange="myalignfunction()" style="width: 100px;">
                 <%String dalign1="";
                  if(!alldetails.isEmpty() && alldetails!=null)
                     dalign1=alldetails.get(7).trim();
                 for (int an = 0; an < Align.length; an++){ 
                     if(dalign1.equalsIgnoreCase(Align[an])){%> 
                              <option selected value="<%=Align[an]%>"><%=Align[an]%></option>
                                <%}else{%>
                               <option  value="<%=Align[an]%>"><%=Align[an]%></option>
                                <%}}%>
                         </select>  
                 </td> 
            </tr>
             <tr>
               <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Background_color", cL)%></th> 
             <%
             String bgcolor="";
             if(!alldetails.isEmpty() && alldetails!=null){
                 try{
                      bgcolor=alldetails.get(10);
                 }catch(Exception ex){
                     alldetails.add("undefined");
                     bgcolor=alldetails.get(10);
                 }
                 
             }
              if(!bgcolor.trim().equalsIgnoreCase("undefined")){
                   if(bgcolor.trim().contains("#"))
                          bgcolor=alldetails.get(10);
                          else
                           bgcolor="#".trim()+alldetails.get(10).trim(); 
              }
              if(!bgcolor.equalsIgnoreCase("")&& !bgcolor.equalsIgnoreCase("undefined") && !bgcolor.equalsIgnoreCase("#f1f1f1")){
             %>
               <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=bgcolor%>' name="headerBackgroundAll"   id="headerBackgroundAll"  onclick="showColorAll(this.id)" > </td>
          <%}else{%>
             <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="headerBackgroundAll"   id="headerBackgroundAll"  onclick="showColorAll(this.id)" > </td>
          <%}%>
            </tr>
            <tr>
                  <th   style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Color", cL)%></th>
                   <td>
                    <%
                    String headercolor="";
                      if(!alldetails.isEmpty() && alldetails!=null){
                          if(!alldetails.get(8).trim().equalsIgnoreCase("undefined")){
                          if(alldetails.get(8).trim().contains("#"))
                          headercolor=alldetails.get(8);
                          else
                           headercolor="#".trim()+alldetails.get(8).trim(); } } 
                         if(!headercolor.equalsIgnoreCase("")&& !headercolor.equalsIgnoreCase("#000000") && !headercolor.equalsIgnoreCase("#000")){%>
                        <input type="text" style='width:95px;cursor:pointer;background-color:<%=headercolor.trim()%>' name="headerColorALL"   id="headerColorALL"  onclick="showColorAll(this.id)"> 
                         <%}else{%>
                    <input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="headerColorALL"   id="headerColorALL"  onclick="showColorAll(this.id)"> 
                    <%}%>
                   
                </td>
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Font_Color", cL)%></th>
                <td>
                    <% String fontcolor="";
                      if(!alldetails.isEmpty() && alldetails!=null){
                          if(!alldetails.get(9).trim().equalsIgnoreCase("undefined")){
                          if(alldetails.get(9).trim().contains("#"))
                          fontcolor=alldetails.get(9);
                          else
                         fontcolor="#".trim()+alldetails.get(9).trim();
                         }}
                         if(!fontcolor.equalsIgnoreCase("")&& !fontcolor.equalsIgnoreCase("#000000") && !fontcolor.equalsIgnoreCase("#000")){%>
                       <input type="text" style='width:95px;cursor:pointer;background-color:<%=fontcolor.trim()%>' name="FontColorALL"   id="FontColorALL"  onclick="showColorAll(this.id)" >   
                    <%} else{%>
                    <input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0].trim()%>' name="FontColorALL"   id="FontColorALL"  onclick="showColorAll(this.id)" > 
                    <%}%>
                </td>
            </tr>

              <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Reset_to_Default", cL)%></th>
                 <td>  <input type="checkbox"  name="ResetColorall" id="Headersize1" value="#000" size="6px" onclick="enableResetColor()" > </td> 
            </tr>

            
             <tr>
                <td>  <input type="button" id="Tablecolumnpro" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="ALLMeasures()"></td>
            </tr>
        </table>
            <div id="colorsDiv" style="display: none" title="Select color">
                    <center>
                        <input type="text" id="color" style="" value="#12345" >
                        <div id="colorpicker" style=""></div>
                        <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                        <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                        <input type="hidden" id="selectedId" value="">
                    </center>
        </div>
            <script type="text/javascript" >
                function showColorAll(id)
          {      
            $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
            if(colorCode!=undefined && colorCode!="")
              {
                $("#color").val(colorCode)
                $("#color").css("background-color",colorCode)
               $.farbtastic("#color").setColor(colorCode)
             }
          }
          function saveSelectedColor()
          {
              var seletedTextId= $("#selectedId").val();
              var colorCode=$("#color").val();
              $("#"+seletedTextId).css("background-color",colorCode)
              $("#"+seletedTextId).attr('colorCode',colorCode);
              //$("#"+seletedTextId).val(colorCode);
              $("#colorsDiv").dialog('close')
          }
          function cancelColor()
          {
               $("#colorsDiv").dialog('close')
          }
           var chkds = document.getElementsByName('ResetColorall')[0];
           function enableResetColor(){
 
   if(chkds.checked)  {
       }
   else {

   }
     }
           function colorToHex(color) {
               if(color != undefined){
           if (color.substr(0, 1) === '#') {
             return color;
              }      
            var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);
             var red = parseInt(digits[2]);
             var green = parseInt(digits[3]);
             var blue = parseInt(digits[4]);

             var rgb = blue | (green << 8) | (red << 16);
             return digits[1] + '#' + rgb.toString(16);
             }
             }
             
             $(document).ready(function(){
          $("#colorsDiv").dialog({
                            bgiframe: true,
                            autoOpen: false,
                            height:300,
                            width: 300,
                            modal: true,
                            Cancel: function() {
                                $(this).dialog('close');
                            }

                    });
               $('#colorpicker').farbtastic('#color');
           });
                
            function  ALLMeasures(){ 
            parent.$("#dispTabColumnProp").dialog('close');
              var alldetails=[];
              var numberformat=[];
              var elementsrounding=[];
              var arrfontsize=[];
              var headerfont=[];
              var headeralign=[];
              var dataalign=[];
               var customarr=[];
               var coloarr=[];
                var textcoloarr=[];
                var number=[];
                var bgcolorarr=[];
                 var colorcode="";
                  var textcolor="";
               var measureids1='<%=measureid%>';
                  measureids1=measureids1.replace("[","").replace("]","");
                var measureids=measureids1.split(",");
              var reportId=<%=reportId%>;
              var pageSize="<%=request.getParameter("slidePages")%>";
              var numberall=document.getElementById("NumberFormatALL").value;
                if(chkds.checked)
                  numberall="Ab"                  
                 alldetails.push(numberall);
                  var decimal=document.getElementById("NumberRoundingALL").value;
                   if(chkds.checked)
                  decimal="0"   
                  alldetails.push(decimal);
                   var numheader=document.getElementById("NumberHeaderALL").value;
                  //alldetails.push(numheader);
                   var customhead=document.getElementById("CustomHeaderALL").value; 
                   if(chkds.checked)
                    customhead=''
                  if(customhead=='')
                  alldetails.push("notdefined");
                 else
                  alldetails.push(customhead);  
                     if(chkds.checked)
                    numheader="Y"
                   alldetails.push(numheader);
                  var datafont= document.getElementById("fontsizeALL").value; 
                  if(chkds.checked)
                    datafont='12'
                  alldetails.push(datafont);
                  var headerfontval=document.getElementById("HeadersizeALL").value; 
                  if(chkds.checked)
                    headerfontval='12'
                   alldetails.push(headerfontval);
                  var headeralignval=document.getElementById("HeaderAlignALL").value; 
                    if(chkds.checked)
                    headeralignval='Center'
                   alldetails.push(headeralignval);
                  var dataalignval=document.getElementById("DataAlignALL").value; 
                   if(chkds.checked)
                    dataalignval='Center'
                  alldetails.push(dataalignval);
                  // var ResetColor1=$("#Headersize1").val();
                   
                    var headercol=$("#headerColorALL").attr('colorCode');
                     if(chkds.checked)
                    headercol='#000000'
                 // alert(headercol)
                   
                 // var textcolor=$("#FontColorALL").attr('colorCode');
                    if(headercol==undefined)
                  alldetails.push("undefined");
                  else
                  alldetails.push(headercol);    
                  var textcolor=$("#FontColorALL").attr('colorCode');
                  if(chkds.checked)
                    textcolor='#000000'
                  if(textcolor==undefined)
                   alldetails.push("undefined");
                   else
                   alldetails.push(textcolor); 
               var bgcolor=$("#headerBackgroundAll").attr('colorCode');
                if(chkds.checked)
                    bgcolor='#f1f1f1'
                  if(bgcolor==undefined)
                   alldetails.push("undefined");
                   else
                   alldetails.push(bgcolor);
                 for(var id=0;id<measureids.length;id++){
                    numberformat.push(numberall);
                    elementsrounding.push(decimal);
                     arrfontsize.push(datafont); 
                     headerfont.push(headerfontval); 
                     headeralign.push(headeralignval);
                     dataalign.push(dataalignval);
                      number.push(numheader); 
                      if(customhead=='')
                        customarr.push("notdefined");
                       else
                      customarr.push(customhead);
                    // var chkds = document.getElementsByName('ResetColorall')[0];
                     // if(!chkds.checked){
                       if(headercol!==undefined && headercol!=''){
                          coloarr.push(headercol);
                      }else{ //alert("else")
                          coloarr.push("undefined");
                      }
                       if(textcolor!==undefined && textcolor!=''){
                          textcoloarr.push(textcolor);
                      }else{ 
                          textcoloarr.push("undefined");
                      } 
                    
                     if(bgcolor!==undefined && bgcolor!=''){
                          bgcolorarr.push(bgcolor);
                      }else{ 
                          bgcolorarr.push("undefined");
                      } 
                    
                // }
                 }
                  // var chkds = document.getElementsByName('ResetColorall')[0]
//                     if(chkds.checked)
//                         {
//                       alldetails.push(ResetColor1);
//                       alldetails.push(ResetColor1);
//                     }
                        
                   // var chkds = document.getElementsByName('ResetColorall')[0];
//                    if(chkds.checked)
//                  { for(var id=0;id<measureids.length;id++){
//                      coloarr.push(ResetColor1);
//                      textcoloarr.push(ResetColor1);
//                     colorcode=encodeURIComponent(coloarr);
//                      textcolor=encodeURIComponent(textcoloarr);
//                        }
//                    } else { 
//                        for(var id=0;id<measureids.length;id++){
//                               if(headercol!==undefined && headercol!=''){
//                          coloarr.push(headercol);
//                      }else{ //alert("else")
//                          coloarr.push("undefined");
//                      }
//                       if(textcolor!==undefined && textcolor!=''){
//                          textcoloarr.push(textcolor);
//                      }else{ 
//                          textcoloarr.push("undefined");
//                      } 
//                     
//                  }
                      colorcode=encodeURIComponent(coloarr);
                      textcolor=encodeURIComponent(textcoloarr);
                      bgcolor=encodeURIComponent(bgcolorarr);
                    // }
                  $.post("<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=tableColumnProperties&arrfontsize="+arrfontsize+"&headerfont="+headerfont+"&elementsrounding="+elementsrounding+"&reportid="+reportId+"&numberformat="+numberformat+"&slidePages="+pageSize+"&headeralign="+headeralign+"&dataalign="+dataalign+"&number="+number+"&customarr="+customarr+"&colorcode="+colorcode+"&textcolor="+textcolor+"&bgcolor="+bgcolor+"&alldetails="+encodeURIComponent(alldetails),
                  function(data){
               refreshReportTablesColumn('<%=request.getContextPath()%>',reportId,pageSize);
                <%if(pbcollect.reportColViewbyValues != null && pbcollect.reportColViewbyValues.size() > 0){%>
                    resizeTblCont("crosstab");
                <%}else{%>                                        
                    resizeTblCont("standrad");
                <%}%>
           });
           }
                
               </script>
    </body>
</html>
<%}%>