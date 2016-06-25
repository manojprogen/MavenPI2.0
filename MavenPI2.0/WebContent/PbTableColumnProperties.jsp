<%-- 
    Document   : PbTableColumnProperties
    Created on : Nov 14, 2015, 2:13:42 PM
    Author     : sruthi
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,java.util.ArrayList,java.util.HashMap,prg.db.Container,com.progen.report.PbReportCollection"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
 String reportId = request.getParameter("reportId");
 String pagesize=request.getParameter("slidePages");
 String selectedcolor=request.getParameter("selectedcolor");
 String selectedmeasureid=request.getParameter("colorPickerI");
  String themeColor = "blue";
 String[] formtmeasure=null;
 if(selectedmeasureid!=null && !selectedmeasureid.isEmpty())
 formtmeasure=selectedmeasureid.split("Headerfontcolour_");
 Container container = null;
 PbReportCollection pbcollect=null;
 ArrayList<String> measurNames = new ArrayList<String>();
 ArrayList<String> measureid = new ArrayList<String>();
 ArrayList<String> measureId = new ArrayList<String>();
 ArrayList<String> arrlistnumberformat = new ArrayList<String>();
 ArrayList<String> arrlistrounding = new ArrayList<String>();
 ArrayList<String> MeasuresNames=new ArrayList<String>();
  HashMap<String,ArrayList<String>> tablecolumnproperties=null;
   HashMap<String,ArrayList<String>> fontHeaderMaps=null;
   HashMap NFMap = null;
    HashMap TableHashMap = null;
     HashMap ColumnProperties = null;
     HashMap<String,String> customheader=null;
   // String NbrFormat="";
  String[] NbrFormats = {"Ab", "Nf", "K", "M","L","Cr"};
  String[] NbrFormatsDisp = {"Absolute", "NoFormat", "Thousands(K)", "Millions(M)","Lakhs(L)","Crores(C)"};
   String[] Round={"0","1","2","3","4","5"};
   String[] Round_D={"No Decimal","One Decimal","Two Decimal","Three Decimal","Four Decimal","Five Decimal"};
   String colour[]={"#109618"};
  String Align[]={"Center","Left","Right"};
   String display[]={"Y","N"};
   String displaystyle[]={"Header","Data"};
    ArrayList singleColProp = null;
    ArrayList<String> alldetails=null;
    ArrayList numberformate=new ArrayList();
        //added by Mohit Gupta for default locale
                   Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
                //ended By Mohit Gupta
 if (reportId != null) {
                if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                     pbcollect = container.getReportCollect();
                   // pbcollect.measureAligns.get("");
                    fontHeaderMaps = (container.getTableColumnProperties() == null) ? new HashMap() :container.getTableColumnProperties();
                   measurNames=(ArrayList) container.getTableMeasureNames();
                  // measureid=(ArrayList) container.getTableMeasure();
                    ColumnProperties = (container.getColumnProperties() == null) ? new HashMap() : container.getColumnProperties();
                    TableHashMap = container.getTableHashMap();
                      MeasuresNames = (ArrayList) TableHashMap.get("MeasuresNames");
                      measureId=(ArrayList) TableHashMap.get("Measures");
                       String PercentId =container.getPercentWiseTable();
                      String PercentIdName=container.getPercentWiseTableName();
                      if(container.isReportCrosstab())
                                                   {
                       measureId.remove(PercentId);
                       MeasuresNames.remove(PercentIdName);
                                             }
                      for(int ids=0;ids<measureId.size();ids++){
                             measureid.add(measureId.get(ids).replace("A_", ""));
                             singleColProp=(ArrayList) ColumnProperties.get(measureId.get(ids));
                              numberformate.add(singleColProp.get(9));
                          }
                      NFMap = (TableHashMap.get("NFMap") == null) ? new HashMap() : (HashMap) TableHashMap.get("NFMap");
                     customheader=container.getCustomHeader();
                     alldetails=container.getAllDetails();
                     if(alldetails.isEmpty()){
                         alldetails.add("Ab");
                         alldetails.add("0");
                         alldetails.add("notdefined");
                         alldetails.add("Y");
                         alldetails.add("12");
                         alldetails.add("12");
                         alldetails.add("Center");
                         alldetails.add("Center");
                         alldetails.add("undefined");
                         alldetails.add("undefined");
                          alldetails.add("undefined");
                     }
                      }
                String contextPath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
            <script type="text/javascript" src="<%=contextPath%>/javascript/pbReportViewerJS.js"></script>
              <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.7.2.min.js"></script>
              <script type="text/javascript" src="<%=contextPath%>/jQuery/d3/d3.v3.min.js"></script>
                <script type="text/javascript" src="<%=contextPath%>/javascript/reportviewer/ReportViewer.js"></script>
                 <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
                <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
                <link type="text/css" href="<%=contextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
                <link type="text/css" href="<%=contextPath%>/css/global.css" rel="stylesheet" />
                <script type="text/javascript" src="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
                <link rel="stylesheet" href="<%=contextPath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" /> 
<!--                   <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/jquery.metadata.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery.mb.containerPlus.2.5.1/inc/mbContainer.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.slider.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/JS/defineDialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/metadataButton.css" rel="stylesheet" />-->
         <title>Table Column Properties</title>
         <style>
 html { overflow: none; } 
 body { position: absolute; top: 0px; left: 25px; bottom: 25px; right: 25px; overflow-y: scroll; overflow-x: scroll; } 
 /* Let's get this party started */ ::-webkit-scrollbar { width: 12px; } /* Track */
 ::-webkit-scrollbar-track { -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);
 -webkit-border-radius: 10px; border-radius: 5px; } /* Handle */ 
 ::-webkit-scrollbar-thumb { -webkit-border-radius: 10px; border-radius: 5px; 
 -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.5); } 
 ::-webkit-scrollbar-thumb:window-inactive {  }
         </style>
       
    </head>
 <body>
<!--         <form action="" name="grpProColumnForm" id="grpProColumnForm" method="post">-->
<div id='tabProperties' class='tag-link-properties' style='width:100%;height:5.5%;'>
    <ul id="quickTabs" >
        <li class="tabUL" ><a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;"id="All" onclick=ALLmeasures('<%=reportId%>','<%=contextPath%>','<%=pagesize%>')>ALL</a>
        <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;"id="Measure" onclick=Measures('<%=reportId%>','<%=contextPath%>','<%=pagesize%>')>Measure</a>
        <a class="gFontFamily gFontSize13" style="padding: 0.3em 3.5em;"id="Dimenstions"onclick=Dimenstions('<%=reportId%>','<%=contextPath%>','<%=pagesize%>')>Dimenstions</a></li>
    </ul>
</div>
        <table  id="tablecolumnpro" style="border-collapse: separate;border-spacing: 26px 15px;">
            <tr>
                <th width="15%">
                </th>
              
                <%for(String measure:MeasuresNames){%>
            <th style="text-align:left;font:11px verdana">
                <h3> <%=measure%> <h3>
            </th>
            <%}%>
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Number_Format", cL)%></th>
               
               <%for(int i=0;i<MeasuresNames.size();i++){
                   String NbrFormat="";%>
                <td>
                    <select name="NumberFormat" id='NumberFormat_<%=measureid.get(i)%>' onchange="myFunction('NumberFormat')"style="width: 100px;">

                          <%for (int nb = 0; nb < NbrFormats.length; nb++){
                                if(NFMap.containsKey("A_"+measureid.get(i))){
                                NbrFormat=(NFMap.get("A_"+measureid.get(i)) == null) ? " " : NFMap.get("A_"+measureid.get(i)).toString();
                                }
                               if (NbrFormat.equalsIgnoreCase(NbrFormats[nb])) {%>
                                <option selected value="<%=NbrFormats[nb]%>" ><%=NbrFormatsDisp[nb]%></option>
                                <%} else {%>
                                <option value="<%=NbrFormats[nb]%>"><%=NbrFormatsDisp[nb]%></option>
                                <%}
                                                    }%>
                               
                        </select>
                </td>
                <%}%>
            </tr>
              <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Number_Rounding", cL)%></th>
                
                  <%for(int j=0;j<MeasuresNames.size();j++){%>
                <td>
                    <select name="NumberRounding"  id="NumberRounding_<%=measureid.get(j)%>" onchange="myFunction('NumberRounding')"style="width: 100px;">
                        <%for (int rd = 0; rd < Round.length; rd++){
                         int precision=container.getRoundPrecisionForMeasure("A_"+measureid.get(j));
                         String val=""+precision;
                            if (val.trim().equalsIgnoreCase(Round[rd])) {%>
                                <option selected value="<%=Round[rd]%>"><%=Round_D[rd]%></option>
                                <%} else {%>
                                <option value="<%=Round[rd]%>"><%=Round_D[rd]%></option>
                                <%}   }%>
                        </select>
                </td>
                <%}%>
            </tr>
             <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Append_Number_Format", cL)%></th>
                <%
                String customdata="";   
                for(int ch=0;ch<MeasuresNames.size();ch++){
                  if(customheader!=null && !customheader.isEmpty()){
                 if(customheader.containsKey("A_"+measureid.get(ch))){
                  customdata=customheader.get("A_"+measureid.get(ch));
                 }
                       if(customdata.equalsIgnoreCase("notdefined")){%>
                          <td>  <input type="text"  name="CustomHeader" id="CustomHeader_<%=measureid.get(ch)%>" value="" style="width:95px;"/> </td>  
                    <%   }else{%>
                          <td><input type="text"  name="CustomHeader" id="CustomHeader_<%=measureid.get(ch)%>" value="<%=customdata%>" style="width:95px;"/></td>
                     <%  } %>
                       
                         <%}else{%>
                         <td><input type="text"  name="CustomHeader" id="CustomHeader_<%=measureid.get(ch)%>" value="" style="width:95px;"/></td>
                     <%}}%>
                
            </tr>
            <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Append_Number_Format_In", cL)%></th>
                  <%for(int nh=0;nh<MeasuresNames.size();nh++){%>
                   <td>
                  <select name="NumberHeader" id='NumberHeader_<%=measureid.get(nh)%>' style="width: 100px;">
                       <%for (int ab = 0; ab < display.length; ab++){
                    if(numberformate.get(nh).equals(display[ab])){%>
                      <option selected value="<%=display[ab]%>"><%=displaystyle[ab]%></option>
                   <% } else {%>
                       <option value="<%=display[ab]%>"><%=displaystyle[ab]%></option>
                   <% } %>
               
                <%}%>
                 </select>
                <%}%>
               
                </td>
            </tr>
             <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Measure_Alias", cL)%></th>
                
                <%for(int k=0;k<MeasuresNames.size();k++){ %>
                <td>
                    <input type="text" id="MeasureAlias_<%=measureid.get(k)%>" value="<%=MeasuresNames.get(k)%>" style="width:95px;" >
                </td>
                <%}%>
            </tr>
             <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Font_Size", cL)%></th>
               
              <%for(int l=0;l<MeasuresNames.size();l++){
                    ArrayList fontheaderpro = null;
                    String headersize="";
                    if(fontHeaderMaps!=null && !fontHeaderMaps.isEmpty() ){
                  if(fontHeaderMaps.containsKey("A_"+measureid.get(l))){
                        fontheaderpro=fontHeaderMaps.get("A_"+measureid.get(l));
                         headersize=fontheaderpro.get(0).toString();
      if(headersize.trim().equalsIgnoreCase("12")){%>
                <td>
                    <input type="text" id="fontsize_<%=measureid.get(l)%>" value="12" size="6px" style="width:95px;" onkeypress="myFunction('fontsize')">
                </td>
                <%} else{%>
                  <td>
                    <input type="text" id="fontsize_<%=measureid.get(l)%>" value="<%=headersize%>" size="6px"style="width:95px;" onkeypress="myFunction('fontsize')">
                </td>
                <%}%>
                <%} else{%>
                <td>
                    <input type="text" id="fontsize_<%=measureid.get(l)%>" value="12" size="6px"style="width:95px;" onkeypress="myFunction('fontsize')">
                </td>
               <%}}else{%>
                <td>
                 <input type="text" id="fontsize_<%=measureid.get(l)%>" value="12" size="6px"style="width:95px;" onkeypress="myFunction('fontsize')">
                </td>
                <%}%>
                <%}%>
            </tr>
             <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Size", cL)%></th>
               
                 <%for(int m=0;m<MeasuresNames.size();m++){
                      ArrayList fontheaderpro1 = null;
                    String headersize1="";
                      if(fontHeaderMaps!=null && !fontHeaderMaps.isEmpty()){
                  if(fontHeaderMaps.containsKey("A_"+measureid.get(m))){
                        fontheaderpro1=fontHeaderMaps.get("A_"+measureid.get(m));
                         headersize1=fontheaderpro1.get(1).toString();
      if(headersize1.trim().equalsIgnoreCase("12")){%>
                <td>
                    <input type="text" id="Headersize_<%=measureid.get(m)%>" value="12" size="6px"style="width: 95px;" onkeypress="myFunction('Headersize')">
                </td>
                <%} else{%>
                    <td>
                    <input type="text" id="Headersize_<%=measureid.get(m)%>" value="<%=headersize1%>"style="width: 95px;" size="6px" onkeypress="myFunction('Headersize')">
                </td>
                <%}%>
                <%} else {%>
                 <td>
                    <input type="text" id="Headersize_<%=measureid.get(m)%>" value="12" size="6px"style="width: 95px;" onkeypress="myFunction('Headersize')">
                </td>
                <%} } else{%>
                 <td>
                    <input type="text" id="Headersize_<%=measureid.get(m)%>" value="12" size="6px"style="width: 95px;" onkeypress="myFunction('Headersize')">
                </td>
                <%}}%>
            </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Align", cL)%></th>
                 
                   <%for(int ag=0;ag<MeasuresNames.size();ag++){
                        String align="Center";
                         align= container.getMeasureAlign("A_"+measureid.get(ag));
                    %>
                   <td>
            <select name="HeaderAlign_<%=measureid.get(ag)%>" id="HeaderAlign_<%=measureid.get(ag)%>" onchange="myFunction('HeaderAlign')"style="width: 100px;">
                 <%for (int ng = 0; ng < Align.length; ng++){
                     if(align.equalsIgnoreCase(Align[ng])){%>
                              <option selected value="<%=Align[ng]%>"><%=Align[ng]%></option>
                                <%}else{%>
                                 <option  value="<%=Align[ng]%>"><%=Align[ng]%></option>
                                <%}}%>
                         </select>

                   <%}%>
             </td> </tr>
            <tr>
                 <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Data_Align", cL)%></th>
                
                   <%for(int rg=0;rg<MeasuresNames.size();rg++){ 
                       String dalign=container.getTextAlign("A_"+measureid.get(rg));   
                   %>
                   <td>
            <select name="DataAlign" id="DataAlign_<%=measureid.get(rg)%>" onchange="myFunction('DataAlign')"style="width: 100px;">
                 <%for (int cn = 0; cn < Align.length; cn++){ 
                     if(dalign.equalsIgnoreCase(Align[cn])){%> 
                 
                              <option selected value="<%=Align[cn]%>"><%=Align[cn]%></option>
                                <%}else{%>
                               <option  value="<%=Align[cn]%>"><%=Align[cn]%></option>
                                <%}}%>
                         </select>
                   <%}%>
             </td> </tr>
              <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Background_color", cL)%></th> 
                   <%for(int bg=0;bg<MeasuresNames.size();bg++){
                   String bgcolor=container.getMeasureBgColor("A_"+measureid.get(bg));
                    if(!bgcolor.equalsIgnoreCase("")&&!bgcolor.equalsIgnoreCase("#f1f1f1")){
                   %>
                 <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=bgcolor%>' name="headerBackground_<%=measureid.get(bg)%>"   id="headerBackground_<%=measureid.get(bg)%>"  onclick="showColor(this.id)" onkeypress="myFunction('headerBackground')"> </td>
                <%}else{%>
                 <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="headerBackground_<%=measureid.get(bg)%>"   id="headerBackground_<%=measureid.get(bg)%>"  onclick="showColor(this.id)" onkeypress="myFunction('headerBackground')"> </td>
                   <%}}%>
            
            </tr>
            
                <tr>
                <th style="text-align:left;font:11px verdana"><%=TranslaterHelper.getTranslatedInLocale("Header_Color", cL)%></th>
               
                <%for(int hc=0;hc<MeasuresNames.size();hc++){
                    String color=container.getMeasureColor("A_"+measureid.get(hc));
          if(!color.equalsIgnoreCase("")&& !color.equalsIgnoreCase("#000000") && ! color.equalsIgnoreCase("#000")){%>
                    <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=color%>' name="headerColor_<%=measureid.get(hc)%>"   id="headerColor_<%=measureid.get(hc)%>"  onclick="showColor(this.id)" onkeypress="myFunction('headerColor')"> </td>
                    <%}else{%>
                     <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="headerColor_<%=measureid.get(hc)%>"   id="headerColor_<%=measureid.get(hc)%>"  onclick="showColor(this.id)" onkeypress="myFunction('headerColor')"> </td>
                    <%}}%>
            </tr>
              <tr>
                <th style="text-align:left;font:11px verdana">Font Color</th>
                
                <%for(int hc=0;hc<MeasuresNames.size();hc++){
                    String colortext=container.getTextColor("A_"+measureid.get(hc));
          if(!colortext.equalsIgnoreCase("") && !colortext.equalsIgnoreCase("#000000") && ! colortext.equalsIgnoreCase("#000")){%>
                    <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colortext%>' name="FontColor_<%=measureid.get(hc)%>"   id="FontColor_<%=measureid.get(hc)%>"  onclick="showColor(this.id)" onkeypress="myFunction('FontColor')"> </td>
                    <%}else{%>
                     <td><input type="text" style='width:95px;cursor:pointer;background-color:<%=colour[0]%>' name="FontColor_<%=measureid.get(hc)%>"   id="FontColor_<%=measureid.get(hc)%>" onclick="showColor(this.id)" onkeypress="myFunction('FontColor')"> </td>
                    <%}}%>
            </tr>
            <tr>
                <td>  <input type="button" id="Tablecolumnpro" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="TablecolumnProperties()"></td>
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
<!--         </form>-->
<!--             <div id="displaycolpro" title="Table Column Properties" style="display:none">-->
<!--            <iframe id="displayglobalcolpro" NAME='displayglobalcolpro' width="100%" height="100%"  frameborder="0" src='about:blank'></iframe>-->
<!--        </div>-->
            
 <script type="text/javascript" >
            var flag="false";
            var numberformatflag=false;
             var numberroundingflag=false;
              var  Customerflag=false;
               var headerflag=false;
                var datafontflag=false; 
                var headerfontflag=false; 
                var headeralignflag=false;
                var dataalignflag=false;
                var headercolor=false;
                var datacolor=false;
                var headerBg=false;
          function  TablecolumnProperties(){//alert("nmmkm")
             parent.$("#dispTabColumnProp").dialog('close');
                var reportId=<%=reportId%>;
               // alert("reportid"+reportId);
                  var pageSize="<%=request.getParameter("slidePages")%>";
                 var measureids1='<%=measureid%>';
                  measureids1=measureids1.replace("[","").replace("]","");
                   var measureids=measureids1.split(",");
                  //  alert("measureids"+measureids);
                  //start of code by anitha for All option (headerColorALL and FontColorALL)
                   
                                //end of code by anitha for All option (headerColorALL and FontColorALL)
                  var elementsrounding=[];
                  var measuralias=[];
                  var numberformat=[];
                  var arrfontsize=[];
                  var headerfont=[];
                  var selectedall=[];
                  var arrcolour=[];
                  var headeralign=[];
                  var dataalign=[];
                  var number=[];
                  var coloarr=[];
                  var textcoloarr=[];
                  var customarr=[];
                  var arralldetails=[];
                  var bgcoloarr=[];
                  var alldetails='<%=alldetails%>';
                  alldetails=alldetails.split(",");
                  for(var k=0;k<alldetails.length;k++){
                      arralldetails.push(alldetails[k]);
                  }
                  if(numberformatflag)
                      arralldetails[0]="Ab";
                  if(numberroundingflag)
                     arralldetails[1]="0";
                 if(headerflag)
                     arralldetails[2]="Y";
                 if(Customerflag)
                     arralldetails[3]="notdefined";
                 if(datafontflag)
                   arralldetails[4]="12";
               if(headerfontflag)
                   arralldetails[5]="12";
               if(headeralignflag)
                   arralldetails[6]="Center";
               if(dataalignflag)
                   arralldetails[7]="Center";
               if(headercolor)
                   arralldetails[8]="undefined";
               if(datacolor)
                   arralldetails[9]="undefined";
               if(headerBg)
                   arralldetails[10]="undefined";
//                  var numberall=document.getElementById("NumberFormatALL").value;
//                  alldetails.push(numberall);
//                  var decimal=document.getElementById("NumberRoundingALL").value;
//                  alldetails.push(decimal);
//                   var numheader=document.getElementById("NumberHeaderALL").value;
//                  alldetails.push(numheader);
//                   var customhead=document.getElementById("CustomHeaderALL").value; 
//                   //alert("customhead..."+customhead)
//                  if(customhead=='')
//                  alldetails.push("nodata");
//                 else
//                  alldetails.push(customhead);  
//                  var datafont= document.getElementById("fontsizeALL").value; 
//                  alldetails.push(datafont);
//                  var headerfontval=document.getElementById("HeadersizeALL").value; 
//                   alldetails.push(headerfontval);
//                  var headeralignval=document.getElementById("HeaderAlignALL").value; 
//                   alldetails.push(headeralignval);
//                  var dataalignval=document.getElementById("DataAlignALL").value; 
//                  alldetails.push(dataalignval);
//                  var headercol=$("#headerColorALL").attr('colorCode');
                 // alert(headercol)
                  //start of code by anitha for All option (headerColorALL and FontColorALL)
                 
                //end of code by anitha for All option (headerColorALL and FontColorALL)
                
                   for(var id=0;id<measureids.length;id++){
                       var rounding=$("#NumberRounding_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var alias=$("#MeasureAlias_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var format=$("#NumberFormat_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       //alert(format)
                       var fontsize=$("#fontsize_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var headersize=$("#Headersize_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var measureHeaderAlign=$("#HeaderAlign_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var measureDataAlign=$("#DataAlign_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                       var numberformatheader=$("#NumberHeader_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();


                        // var ResetColor1=$("#Headersize1_"+measureids[id]).val();
                      number.push(numberformatheader);
                      var customheader=$("#CustomHeader_"+measureids[id].toString().replace("[","").replace("]","").trim()).val();
                      if(customheader!=''){
                          customarr.push(customheader);
                      }else{
                          customarr.push("notdefined");
                      }
                       var  colorcode=$("#headerColor_"+measureids[id].toString().replace("[","").replace("]","").trim()).attr('colorCode');
                        var fontcolor=$("#FontColor_"+measureids[id].toString().replace("[","").replace("]","").trim()).attr('colorCode');
                              if(fontcolor!==undefined && fontcolor!=''){
                          textcoloarr.push(fontcolor);
                      }else{
                          textcoloarr.push("undefined");
                 }
                  if(colorcode!==undefined && colorcode!=''){
                          coloarr.push(colorcode);
                      }else{
                          coloarr.push("undefined");
                      }
                      var headerBackground=$("#headerBackground_"+measureids[id].toString().replace("[","").replace("]","").trim()).attr('colorCode');
                       if(headerBackground!==undefined && headerBackground!=''){
                          bgcoloarr.push(headerBackground);
                      }else{
                          bgcoloarr.push("undefined");
                      }
                        elementsrounding.push(rounding);  
                       measuralias.push(alias);
                       numberformat.push(format);
                         arrfontsize.push(fontsize);  
                       headerfont.push(headersize);
                        headeralign.push(measureHeaderAlign);
                      dataalign.push(measureDataAlign);  

              }
//                  var chkds = document.getElementsByName('ResetColorall')[0];
//                  if(chkds.checked)
//                      {
//                     for(var id=0;id<measureids.length;id++){
//                      textcoloarr.push(ResetColor1);
//                      coloarr.push(ResetColor1);
//                       }
//                     }
//
//                       else
//                           {
//                                for(var id=0;id<measureids.length;id++){
//                         var  colorcode=$("#headerColor_"+measureids[id]).attr('colorCode');
//                        var fontcolor=$("#FontColor_"+measureids[id]).attr('colorCode');
//                              if(fontcolor!==undefined && fontcolor!=''){
//                          textcoloarr.push(fontcolor);
//                      }else{
//                          textcoloarr.push("undefined");
//                 }
//                  if(colorcode!==undefined && colorcode!=''){
//                          coloarr.push(colorcode);
//                      }else{
//                          coloarr.push("undefined");
//                      }
//                                }
//   }              
                    var changesalias=encodeURI(measuralias);
                   var colorcode=encodeURIComponent(coloarr);
                   var textcolor=encodeURIComponent(textcoloarr);
                   var bgcolor=encodeURIComponent(bgcoloarr);
                $.post("<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=tableColumnProperties&arrfontsize="+arrfontsize+"&headerfont="+headerfont+"&elementsrounding="+elementsrounding+"&measuralias="+changesalias+"&reportid="+reportId+"&numberformat="+numberformat+"&slidePages="+pageSize+"&headeralign="+headeralign+"&dataalign="+dataalign+"&number="+number+"&customarr="+customarr+"&colorcode="+colorcode+"&textcolor="+textcolor+"&bgcolor="+bgcolor+"&alldetails="+encodeURIComponent(arralldetails),
                  function(data){
               refreshReportTablesColumn('<%=request.getContextPath()%>',reportId,pageSize);
                <%if(pbcollect.reportColViewbyValues != null && pbcollect.reportColViewbyValues.size() > 0){%>
                    resizeTblCont("crosstab");
                <%}else{%>                                        
                    resizeTblCont("standrad");
                <%}%>
           });
            }
               parent.$('#report_color_picker').dialog({
         autoOpen: false,
         height: 450,
         width: 285,
         position: 'justify',
         modal: true,
         resizable:false,
        title:'Pick Color'
    });
    function numberFormateHeader(id)
            { //alert(id)
                 var NumberHeader=document.getElementById(id);
                 var id1;
//                 if(id=='NumberHeaderALL'){
//         var measureids=<%=measureid%>;
//         for(var mesid=0;mesid<measureids.length;mesid++){
//              if(NumberHeader.checked)
//                    {       //alert("true")
//                         id1="NumberHeaderALL"
//                         hideCustomHeader(id1);//added by sruthi for customheader
//                        NumberHeader.value="true";
//                        document.getElementById("NumberHeader_"+measureids[mesid]).value="true";
//                    }else
//                        {     //alert("false")
//                            id1="NumberHeaderALL"
//                            hideCustomHeader(id1);//added by sruthi for customheader
//                            NumberHeader.value="false"
//                            //alert("NumberHeader_"+measureids[mesid])
//                            //alert(document.getElementById("NumberHeader_"+measureids[mesid]))
//                            document.getElementById("NumberHeader_"+measureids[mesid]).value="false";
//                        }
//                    }
//         }else{
               //alert("false")
                if(NumberHeader.checked)
                    {
                         hideCustomHeader(id);//added by sruthi for customheader
                      NumberHeader.value="true";
                    }else
                        {    
                            hideCustomHeader(id);//added by sruthi for customheader
                            NumberHeader.value="false"
                        }
                //  }
            }
            function hideCustomHeader(id){ //alert(id) 
            if(id=='NumberHeaderALL'){
               var numberformate1=document.getElementById(id);
                var measureids=<%=measureid%>;
         for(var mesid1=0;mesid1<measureids.length;mesid1++){
         if(numberformate1.checked){
                  $("#CustomHeader_"+measureids[mesid1]).show();
                  $("#CustomHeaderALL").show();
              }else{
                  $("#CustomHeader_"+measureids[mesid1]).hide(); 
                  $("#CustomHeaderALL").hide();
                   }
                     }
            }else{ //alert("false11")
            var elementid=id.replace("NumberHeader_","");
     var numberformate=document.getElementById(id);
  // alert(elementid)
  // alert(numberformate)
     if(numberformate.checked){
 $("#CustomHeader_"+elementid).show();
     }else
 $("#CustomHeader_"+elementid).hide();
 }}
   // Added By Mohit Gupta

 function enableResetColor(){
          var chkds = document.getElementsByName('ResetColorall')[0];
   if(chkds.checked)  {
       }
   else {

   }
     }
     function showColor(id)
          {      
            var idval=id.split("_");
              if(idval[0]==="headerColor")
                  myFunction("headerColor");
              else if(idval[0]==="FontColor")
                 myFunction("FontColor");
                 else
                    myFunction("headerBackground");  
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
           //ended by Mohit Gupta
           function myFunction(parameter){
           if(parameter=="NumberFormat")
            numberformatflag=true;
         if(parameter=="NumberRounding")
            numberroundingflag=true;
        if(parameter=="HeaderAlign")
           headeralignflag=true;
        if(parameter=="DataAlign")
           dataalignflag=true;
        if(parameter=="fontsize")
             datafontflag=true;       
        if(parameter=="Headersize")
            headerfontflag=true; 
         if(parameter=="NumberHeader")
              headerflag=true;
          if(parameter=="CustomHeader")
             Customerflag=true;
           if(parameter=="FontColor"){
             datacolor=true;
            }
           if(parameter=="headerColor"){
          headercolor=true;
           }}
       
//       function myalignfunction(){
//          flag="true" ;
//       }
        </script>
    </body>
</html>
<%}%>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      