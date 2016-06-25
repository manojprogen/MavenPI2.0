<%-- 
    Document   : TableDimenstion
    Created on : Mar 17, 2016, 4:59:15 PM
    Author     : Sruthi
--%>

<%@page import="java.util.Locale"%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.progen.report.PbReportCollection"%>
<%@page import="prg.db.Container"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
String reportId = request.getParameter("reportId");
 String pagesize=request.getParameter("slidePages");
 String contextPath=request.getContextPath();
 String themeColor = "blue";
 String Align[]={"Left","Right","Center"};
 String colour[]={"#109618"};
 Container container = null;
 PbReportCollection pbcollect=null;
 HashMap TableHashMap = null;
 ArrayList viewbyNames1 =null;
 viewbyNames1 = new  ArrayList();
 ArrayList viewbyid1 =null;
 viewbyid1 = new  ArrayList();
 String color="";
 ArrayList<String>  viewbyNames = new ArrayList<String>();
 ArrayList<String>  viewbyid = new ArrayList<String>();


   HashMap<String,ArrayList<String>> fontHeaderMaps=null;
  Locale cL=null;
 cL=(Locale)session.getAttribute("UserLocaleFormat");
 if (reportId != null) { 
       if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
             HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES"); 
             container = (Container) map.get(reportId);
             TableHashMap = container.getTableHashMap();
              viewbyid1 = (ArrayList)TableHashMap.get("CEP");
             viewbyNames1 = (ArrayList) TableHashMap.get("CEPNames");
             viewbyid=(ArrayList)TableHashMap.get("REP");
             viewbyNames=(ArrayList)TableHashMap.get("REPNames");
             if(viewbyid1!=null&& viewbyNames1!=null)
                 {
  for(int i=0;i<viewbyNames1.size();i++)
                     {
             if(viewbyNames.indexOf(viewbyNames1.get(i).toString())==-1){
                 
             viewbyNames.add(viewbyNames1.get(i).toString());
             viewbyid.add(viewbyid1.get(i).toString());
             }
             }
             }
             fontHeaderMaps = (container.getTableColumnProperties() == null) ? new HashMap() :container.getTableColumnProperties();
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
        <title>Dimenstions</title>
    </head>
    <body>
         <div id='tabProperties' class='tag-link-properties' style='width:100%;height:5.5%;'>
    <ul id="quickTabs" >
        <li class="tabUL"><a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="ALLid" onclick="ALLmeasures('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">ALL</a>
      <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="Measures" onclick="Measures('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">Measures</a>
         <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;" id="Dimenstions" onclick="Dimenstions('<%=reportId%>','<%=request.getContextPath()%>','<%=pagesize%>')">Dimenstions</a></li>
    </ul>

</div>
    <table  id="Dimenstionspro" style="border-collapse: separate;border-spacing: 26px 15px;">
        <tr>
                <th width="15%">
                </th>
              
                <%for(String viewby:viewbyNames){%>
            <th style="text-align:left;font:11px verdana">
        <h3> <%=viewby%></h3>
            </th>
            <%}%>
            </tr>
            <tr>
                   <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Viewbys_Alias", cL)%></th>
                    <%for(int l=0;l<viewbyNames.size();l++){%>
                        <td>
                    <input type="text" id="ViewbysAlias_<%=viewbyid.get(l)%>" value="<%=viewbyNames.get(l)%>" style="width:95px;" >
                </td>
                   <% }%>
            </tr>
            <tr>
           <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Font_Size", cL)%></th>
            <%for(int f=0;f<viewbyNames.size();f++){
                 ArrayList fontheaderpro1 = null;
                    String headersize1="";
                 if(fontHeaderMaps!=null && !fontHeaderMaps.isEmpty() ){ 
                     if(fontHeaderMaps.containsKey("A_"+viewbyid.get(f))){ 
                 fontheaderpro1=fontHeaderMaps.get("A_"+viewbyid.get(f));
                         headersize1=fontheaderpro1.get(0).toString(); 
                          if(headersize1.trim().equalsIgnoreCase("12")){
            %>
           <td>
                <input type="text" id="fontsize_<%=viewbyid.get(f)%>" value="12" size="6px" style="width:95px;">
           </td>
           <%} else{%>
                <td>
                <input type="text" id="fontsize_<%=viewbyid.get(f)%>" value="<%=headersize1%>"size="6px" style="width:95px;">
           </td> 
           <%}%>
           <%} else{%>
            <td>
                <input type="text" id="fontsize_<%=viewbyid.get(f)%>" value="12" size="6px" style="width:95px;">
           </td>
           <%}%>
          <%}else{%>
            <td>
                <input type="text" id="fontsize_<%=viewbyid.get(f)%>" value="12" size="6px" style="width:95px;">
           </td>
          <%}%>
           <%}%>
            </tr>
            <tr>
               <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Size", cL)%></th>
            <%for(int kh=0;kh<viewbyNames.size();kh++){
               ArrayList fontheaderpro = null;
                    String headersize="";
               if(fontHeaderMaps!=null && !fontHeaderMaps.isEmpty() ){
                if(fontHeaderMaps.containsKey("A_"+viewbyid.get(kh))){
                     fontheaderpro=fontHeaderMaps.get("A_"+viewbyid.get(kh));
                         headersize=fontheaderpro.get(1).toString();              
                     if(headersize.trim().equalsIgnoreCase("12")){
            %>
           <td>
                <input type="text" id="Headersize_<%=viewbyid.get(kh)%>" value="12" size="6px" style="width:95px;">
           </td>
           <%} else{%>
           <td>
                <input type="text" id="Headersize_<%=viewbyid.get(kh)%>" value="<%=headersize%>" size="6px" style="width:95px;">
           </td>
           <%}%>
           <%}else{%>
            <td>
                <input type="text" id="Headersize_<%=viewbyid.get(kh)%>" value="12" size="6px" style="width:95px;">
           </td>
           <%}%>
           <%}else{%>
           <td>
                <input type="text" id="Headersize_<%=viewbyid.get(kh)%>" value="12" size="6px" style="width:95px;">
           </td>
           <%}%>
           <%}%> 
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Align", cL)%></th>
                
                     <%for(int h=0;h<viewbyNames.size();h++){
                      //   String align="Left";
                       //  if(pbcollect.ViewbyAligns!=null && !pbcollect.ViewbyAligns.isEmpty())
                  String  align=container.getviewbyAlignment("A_"+viewbyid.get(h));
                     %>
                    <td>
                       <select name="HeaderAlign_<%=viewbyid.get(h)%>" id="HeaderAlign_<%=viewbyid.get(h)%>"style="width: 100px;"> 
                           <%for (int ng = 0; ng < Align.length; ng++){
                     if(align.equalsIgnoreCase(Align[ng])){%>
                              <option selected value="<%=Align[ng]%>"><%=Align[ng]%></option>
                                <%}else{%>
                                 <option  value="<%=Align[ng]%>"><%=Align[ng]%></option>
                                <%}}%>
                         </select>  
                    <%}%> 
                </td>
            </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Align", cL)%></th> 
                <%for(int mh=0;mh<viewbyNames.size();mh++){
                   // String align="Left";
                //if(pbcollect.viewbydataAligns!=null && !pbcollect.viewbydataAligns.isEmpty())
                    String  align=container.getViewbydataAlignments("A_"+viewbyid.get(mh));
                     %>
                    <td>
                       <select name="DataAlign_<%=viewbyid.get(mh)%>" id="DataAlign_<%=viewbyid.get(mh)%>" style="width: 100px;"> 
                           <%for (int dg = 0; dg < Align.length; dg++){
                     if(align.equalsIgnoreCase(Align[dg])){%>
                              <option selected value="<%=Align[dg]%>"><%=Align[dg]%></option>
                                <%}else{%>
                                 <option  value="<%=Align[dg]%>"><%=Align[dg]%></option>
                                <%}}%>
                         </select>  
                    <%}%> 
                </td>
            </tr>
            <tr>
                   <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Color", cL)%></th>
                    <%for(int hc=0;hc<viewbyNames.size();hc++){
                      color=container.getMeasureColor("A_"+viewbyid.get(hc));
                     if(!color.equalsIgnoreCase("") && !color.equalsIgnoreCase("#000000") && !color.equalsIgnoreCase("#000") ){%>
                    <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=color%>' name="headerColor_<%=viewbyid.get(hc)%>"   id="headerColor_<%=viewbyid.get(hc)%>"  onclick="showColor(this.id)" > </td>
                    <%}else{%>
                     <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="headerColor_<%=viewbyid.get(hc)%>"   id="headerColor_<%=viewbyid.get(hc)%>"  onclick="showColor(this.id)" > </td>
                    <%}}%>  
                 
                   </tr>
                   <tr>
                    <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Color", cL)%></th>
                    <%for(int hc=0;hc<viewbyNames.size();hc++){
                  color=container.getTextColor("A_"+viewbyid.get(hc));
                     if(!color.equalsIgnoreCase("") && !color.equalsIgnoreCase("#000000") && !color.equalsIgnoreCase("#000")){%>
                    <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=color%>' name="DataColor_<%=viewbyid.get(hc)%>"   id="DataColor_<%=viewbyid.get(hc)%>"  onclick="showColor(this.id)"> </td>
                    <%}else{%>
                     <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="DataColor_<%=viewbyid.get(hc)%>"   id="DataColor_<%=viewbyid.get(hc)%>"  onclick="showColor(this.id)" > </td>
                    <%}}%>  
                   </tr>


                   <tr>
                    <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Reset_Color", cL)%></th>
                    <%for(int hc=0;hc<viewbyNames.size();hc++){
                      color=container.getTextColor("A_"+viewbyid.get(hc));
                     if(!color.equalsIgnoreCase("")){%>
                   <td> <input type="checkbox"  name="ResetColorall" id="Headersize1_<%=viewbyid.get(hc)%>" value="#000" size="6px" onclick="enableResetColor()" ></td>
                    <%}else{%>
                     <td> <input type="checkbox"  name="ResetColorall" id="Headersize1_<%=viewbyid.get(hc)%>" value="#000"  size="6px" onclick="enableResetColor()" ></td>
                    <%}}%>
                   
                   </tr>
                   
                   <tr>
                        <td>  <input  type="button" id="Tablecolumnpro" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="Tabledimenstions()"></td>
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
    var viewbyids=<%=viewbyid%>;
    function Tabledimenstions(){
          parent.$("#dispTabColumnProp").dialog('close');
            var reportId=<%=reportId%>;
              var pageSize="<%=request.getParameter("slidePages")%>";
//                 var viewbyids=<%=viewbyid%>;
                 var arralias=[];
                 var arrfontsize=[];
                 var arrHeadersize=[];
                 var arrHeaderAlign=[];
                 var arrDataAlign=[];
                 var arrheaderColor=[];
                 var arrFontColor=[];
                  var ResetColor=[];

                 var colorcode="";
                 var textcolor="";
                 for(var id=0;id<viewbyids.length;id++){
                       var alias=$("#ViewbysAlias_"+viewbyids[id]).val();
                      // alert(alias)
                       var fontsize=$("#fontsize_"+viewbyids[id]).val();
                       var Headersize=$("#Headersize_"+viewbyids[id]).val();
                       var HeaderAlign=$("#HeaderAlign_"+viewbyids[id]).val();
                       var DataAlign=$("#DataAlign_"+viewbyids[id]).val();
                        var headerColor=$("#headerColor_"+viewbyids[id]).attr('colorCode');
                        var FontColor=$("#DataColor_"+viewbyids[id]).attr('colorCode');
                        var ResetColor1=$("#Headersize1_"+viewbyids[id]).val();
                        arralias.push(alias);
                        arrfontsize.push(fontsize);
                        arrHeadersize.push(Headersize);
                        arrHeaderAlign.push(HeaderAlign);
                        arrDataAlign.push(DataAlign);
                        
                 }
                   var chkds = document.getElementsByName('ResetColorall')[0];
                   if(chkds.checked)
                  {
                       for(var id=0;id<viewbyids.length;id++){
                      arrheaderColor.push(ResetColor1);
                      arrFontColor.push(ResetColor1);
                      colorcode=encodeURIComponent(arrheaderColor);
                      textcolor=encodeURIComponent(arrFontColor);
                       }
                  
                   }
                   else{
                        for(var id=0;id<viewbyids.length;id++){
                         if(headerColor!==undefined && headerColor!=''){
                          arrheaderColor.push(headerColor);
                      }else{ 
                          arrheaderColor.push("undefined");
                      } 
                        if(FontColor!==undefined && FontColor!=''){
                          arrFontColor.push(FontColor);
                      }else{ 
                          arrFontColor.push("undefined");
                      } 
                      colorcode=encodeURIComponent(arrheaderColor);
                     textcolor=encodeURIComponent(arrFontColor);
                 }

                   }
                  
 $.post("<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=tableviewbyColumnProperties&arralias="+arralias+"&arrfontsize="+arrfontsize+"&arrHeadersize="+arrHeadersize+"&arrHeaderAlign="+arrHeaderAlign+"&reportid="+reportId+"&arrDataAlign="+arrDataAlign+"&slidePages="+pageSize+"&colorcode="+colorcode+"&textcolor="+textcolor,
                  function(data){
               refreshReportTablesColumn('<%=request.getContextPath()%>',reportId,pageSize);
              
           });
    }

     function enableResetColor(){
      //   for(var id=0;id<viewbyids.length;id++){
        // $("#headerColor_"+viewbyids[id]).css("background-color", "#109618");
        // }
          var chkds = document.getElementsByName('ResetColorall')[0];
   if(chkds.checked)  {
       }
   else {
      
   }
     }
    function showColor(id)
          {      
              var idval=id.split("_");
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
             </script>
    </body>
</html>
<%}%>