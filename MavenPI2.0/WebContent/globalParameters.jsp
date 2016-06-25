<%@page import="com.progen.i18n.TranslaterHelper,com.progen.reportview.action.ReportViewerAction,com.progen.action.PbBaseAction,java.util.Locale,prg.db.PbDb,java.util.ArrayList,prg.db.PbReturnObject"  contentType="text/html" pageEncoding="UTF-8"%>
<%--
    Document   : Global Parameters
    Created on : 25 July 2014
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

        String reportTitleSizes[] = {"8","10","12","14","16","18","20","22","24","26"};
        String reportTitleAligns[] = {"Left","Center","Right"};
        //added by krishan
        String reportTabs[] = {"Report","Graph","Advancevisual","Trends"};
        String colorOnGrps[] = {"None"};
        String piThemes[] = {"Blue","Orange"};
        String fromDateToDates[] = {"Yes","No"};
          String Comparison[] = {"Yes","No"};
          String dataShow[] = {"Us Based","Indian Based"};
String Layout[]={"Date","Description"};
String Layouts="Date";
String favReportAsTags[]={"Yes","No"};
String favReportAsTag="Yes";
String openReportTabs[]={"New Tab","Same Tab"};
String openReportTab="New Tab";
String RecentlyReport[]={"Yes","No"};
String RecentlyReports="Yes";
String customReport[]={"Yes","No"};
String customReports="Yes";
String FooterNames[]={"No","Yes"};
String FooterName="No";
String headerfont[]={"8","9","10","11","12","13","14","15","16"};
String headerfonts="12";

    String HEADER_LENGTh[]={"10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
    ,"31","32","33","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49"};
    String HEADER_LENGThS="13";

         PbDb pbdb = new PbDb();
        String setUpCharVal = "";
        String reportTitleSize = "12";
        String reportTitleAlign = "Left";
        String colorOnGrp = "None";
        String piTheme = "Blue";
        String fromDateToDate = "No";
        String isYearCal = "No";
        String piVersion[] = {"version2014","version2015"};
        String piVersions="version2014";
        String reportTab="report";
        String defaultPageSize = "25";
        String Filters="1";
         String Comparisons="Yes";
         String dataShows="Us Based";
        String query2 = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
      
        PbReturnObject returnObject = null;

            returnObject = pbdb.execSelectSQL(query2);
            if (returnObject.getRowCount() > 0) {
                setUpCharVal = returnObject.getFieldValueString(0, 0);
                
                if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                    String arrSetUpCharVal[] = setUpCharVal.split(";");
                    for (int i = 0; i < arrSetUpCharVal.length; i++) {
                        String temp = arrSetUpCharVal[i];
                        if (temp.contains("reportTitleSize")) {
                            reportTitleSize = temp.substring(temp.indexOf("~") + 1);
                        } else if (temp.contains("reportTitleAlign")) {
                            reportTitleAlign = temp.substring(temp.indexOf("~") + 1);
                        } //else if (temp.contains("reporttab")) {
                        //    reporttab = temp.substring(temp.indexOf("~") + 1);
                       // }
                        else if (temp.contains("colorOnGrp")) {
                            colorOnGrp = temp.substring(temp.indexOf("~") + 1);
                        } else if (temp.contains("piTheme")) {
                            piTheme = temp.substring(temp.indexOf("~") + 1);
                        } else if (temp.contains("fromDateToDate")) {
                            fromDateToDate = temp.substring(temp.indexOf("~") + 1);
                        } else if (temp.contains("isYearCal")) {
                            isYearCal = temp.substring(temp.indexOf("~") + 1);
                        } 
                        else if (temp.contains("piVersion")) {
                            piVersions = temp.substring(temp.indexOf("~") + 1);
                        }
                        else if(temp.contains("layoutVar"))
                        {
                             Layouts = temp.substring(temp.indexOf("~") + 1);
                        }
                      
                        else if(temp.contains("reporttab")){
                            reportTab=temp.substring(temp.indexOf("~") + 1);
                        }else if(temp.contains("defaultPageSize")){
                            defaultPageSize=temp.substring(temp.indexOf("~") + 1);
                        }
                        else if(temp.contains("Filters")){
                            Filters=temp.substring(temp.indexOf("~") + 1);
                        }
                        else if(temp.contains("Comparison")){
                            Comparisons=temp.substring(temp.indexOf("~") + 1);
                    }  else if(temp.contains("datashow")){
                            dataShows=temp.substring(temp.indexOf("~") + 1);
                    }
                          else if(temp.contains("favReportAsTag"))
                        {
                            favReportAsTag=temp.substring(temp.indexOf("~")+1);
                }
                          else if(temp.contains("openReportTab"))
                          {
                              openReportTab=temp.substring(temp.indexOf("~")+1);
            }
                        
                        else if(temp.contains("recentlyReports"))
                          {
                              RecentlyReports=temp.substring(temp.indexOf("~")+1);
            }
                         else if(temp.contains("customReports"))
                          {
                              customReports=temp.substring(temp.indexOf("~")+1);
                          }
                          else if(temp.contains("Footeroption"))
                          {
                              FooterName=temp.substring(temp.indexOf("~")+1);
                          }
                          else if(temp.contains("headerfont"))
                          {
                              headerfonts=temp.substring(temp.indexOf("~")+1);
                          }
                   else if(temp.contains("HEADER_LENGThS"))
                          {
                              HEADER_LENGThS=temp.substring(temp.indexOf("~")+1);
                          }

                    }
                
            }
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
        <script type="text/javascript" src="<%=contextPath%>/JS/global.js"></script>
     <style type="text/css">
        .migrate{
                font-family: inherit;
                font-size: 10pt;
                color: #000;
                padding-left:12px;
                background-color:#8BC34A;
                border:0px;
            }
        #btnn{color:#000}
        #btnn:hover{color:#fff;text-align:center;background-color:#656565}
      </style>
  
    </head>
    <body>
        <script type="text/javascript">
            $(window).resize(function(){
                resizePage("globalParameters");                               
            });
            $(document).ready(function(){
                resizePage("globalParameters");                
            });
            
		var selectCurrencyAs=[];
		var my_CurrencyAs={"Thousand":"K","Lakh":"Lk","Crore":"Crs","Billion":"B","Million":"M","Trillion":"T"};

       function saveGblParamDetails() {
           var ctxPath = '<%= request.getContextPath()%>';
           var reportTitleSize= $("#reportTitleSize").val();
           var reportTitleAlign= $("#reportTitleAlign").val();
           var piTheme= $("#piTheme").val();
           //added by krishan
           var reportTab=$("#reportTab").val();
           var Comparison = "Yes";
             var dateheader="No";
           var datashow=$("#showdata").val();
          // alert(reportTab)
          var defaultPageSize = $("#defaultPageSize").val(); // added by Mayank
          var Filters = $("#Filters").val();
           if(Filters>4)
               {
                   alert("No. of Filters Can't be More Than 4")
                   return false;
               }
           var fromDateToDate= $("#fromDateToDate").val()
           var isYearCal = "No";
           // Added by Prabal 
           var e = document.getElementById("Layout");
            var layoutVar= e.options[e.selectedIndex].value;
           var el = document.getElementById("openReportTab");
           var openReportTab=el.options[el.selectedIndex].value;
            for(var i in selectCurrencyAs){
                my_CurrencyAs[selectCurrencyAs[i].toString()]=$("#text"+selectCurrencyAs[i].toString()).val();
            }
            var favReportAsTag=$("#favReportAsTag").val();
            var recentlyReports=$("#RecentlyReportsAsTag").val();
            var customReportAsTag=$("#customReportAsTag").val();
//          Ended by Prabal

            var Footeroption = $("#FooterName").val();
//alert("Footeroption="+Footeroption);
           if (document.getElementById("sytm1").checked){
                isYearCal= document.getElementById("sytm1").value;
            } else {
                isYearCal= document.getElementById("sytm2").value;
            }


            if (document.getElementById("sytm12").checked){
                Comparison= document.getElementById("sytm12").value;
            } else {
                Comparison= document.getElementById("sytm23").value;
            }
            // var dateheader=$("#enabledate").val();
              if (document.getElementById("enabledate").checked){
                dateheader= document.getElementById("enabledate").value;
            } else {
                dateheader= document.getElementById("disdate").value;
            }

            var piVersion= $("#piVersion").val()

            var headerFont= $("#headerFont").val();
           var headerLength= $("#headerLength").val();

           //   var fColor = document.getElementById("fColor").value; //added by sruthi 25/11/2014  for globalparameter
            // var dateF = document.getElementById("dateF").value;
//           var colorOnGrp= $("#colorGrp").val()
           var colorOnGrp= 'none';

            $.post('reportViewer.do?reportBy=saveGblParamDetails&reportTitleSize='+reportTitleSize+"&layoutVar="+layoutVar+"&reportTitleAlign="+reportTitleAlign+"&fromDateToDate="+fromDateToDate+"&colorOnGrp="+colorOnGrp+"&piTheme="+piTheme+"&isYearCal="+isYearCal+"&piVersion="+piVersion+"&reportTab="+reportTab+"&defaultPageSize="+defaultPageSize+"&Filters="+Filters+"&Comparison="+Comparison+"&datashow="+datashow+"&openReportTab="+openReportTab+"&my_CurrencyAs="+JSON.stringify(my_CurrencyAs)+"&headerFont="+headerFont+"&headerLength="+headerLength+"&favReportAsTag="+favReportAsTag+"\
                    &recentlyTag="+recentlyReports+"&customReportAsTag="+customReportAsTag+"&Footeroption="+Footeroption+"&dateheader="+dateheader,function(data)  {
               if(data==1)
                    alert('Details Saved Successfully \n Please Logout & Login Again To Reflect Changes..');
                else
                    alert('Error in saving details ! ');
                window.location.href = window.location.href;
            });

            function resetGblParam(){
               $("#reportTitleSize").val('none');
               $("#reportTitleAlign").val('none');

            }
}
function selectCurrencyShownAs(){
    selectCurrencyAs=[];
    var currency = document.getElementById("CurrencyShownAs");
    var currencyVar= currency.options[currency.selectedIndex].value;
        selectCurrencyAs.push(currencyVar);
   if(currencyVar=='Thousand'){
         addRow('CurrencyShownAsTr',currencyVar,'K');
    }else if(currencyVar=='Lakh'){
         addRow('CurrencyShownAsTr',currencyVar,'Lk');
    }else if(currencyVar=='Crore'){
         addRow('CurrencyShownAsTr',currencyVar,'Crs');
    }else if(currencyVar=='Million'){
         addRow('CurrencyShownAsTr',currencyVar,'M');
    }else if(currencyVar=='Billion'){
         addRow('CurrencyShownAsTr',currencyVar,'B');
    }else if(currencyVar=='Trillion'){
         addRow('CurrencyShownAsTr',currencyVar,'T');
    }
    else{
         alert("Plase select valid option.");
    }
}
function addRow(id,row,val){
    var html="<label style='font-size: 8pt;font-weight: bold;padding-left: 7px;background-color: #d1d1d1;'>"+row+"&nbsp;&nbsp;</label> As&nbsp;&nbsp;<input type='text' onchange='saveCurrentData(this.value,this.title);'title='"+row+"' id='text"+row+"' value="+val+"></br>";
    $("#selectedCurrencyAs").html("");
    $("#selectedCurrencyAs").append(html);
}
function saveCurrentData(val,row){
     my_CurrencyAs[row.toString()]=val;
}
       </script>
  <div style="width: 676px;">
                <form id="globalParamForm" name="globalParamForm" method="post" action="">
                    <div style="width: 676px;">
                        <table align ="center" cellspacing="10">
<!--                        <tr><td align="center" style="font-size: 14px"> GLOBAL PARAMETERS </td></tr>-->
                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Report_Title_Size", cle)%> : </label></td>
                          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                          <td align="left">
                          <select name="reportTitleSize" id="reportTitleSize">
                              <%for(int i=0;i<reportTitleSizes.length;i++){
                                  if(reportTitleSizes[i].equalsIgnoreCase(reportTitleSize)){%>
                                    <option value="<%=reportTitleSizes[i]%>" selected><%=reportTitleSizes[i]%></option>
                              <%}else{%>
                                    <option value="<%=reportTitleSizes[i]%>"><%=reportTitleSizes[i]%></option>
                              <%}}%>
                          </select>
                      </td>
                      </tr>

                      <tr>
                          <td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Report_Title_Alignment", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                          <td align="left">
                              <select name="reportTitleAlign" id="reportTitleAlign">
                               <%for(int i=0;i<reportTitleAligns.length;i++){
                                  if(reportTitleAligns[i].equalsIgnoreCase(reportTitleAlign)){%>
                                    <option value="<%=reportTitleAligns[i]%>" selected><%=reportTitleAligns[i]%></option>
                              <%}else{%>
                                    <option value="<%=reportTitleAligns[i]%>"><%=reportTitleAligns[i]%></option>
                              <%}}%>
                              </select>
<!--                              <select name="reportTitleAlign" id="reportTitleAlign">
                                  <option value='left' selected>Left</option>
                                  <option value='center'>Center</option>
                                  <option value='right'>Right</option>
                              </select>-->

                          </td>
                      </tr>
                      <tr>
                          <td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Theme", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                          <td align="left">
                               <select name="piTheme" id="piTheme">
                               <%for(int i=0;i<piThemes.length;i++){
                                  if(piThemes[i].equalsIgnoreCase(piTheme)){%>
                                    <option value="<%=piThemes[i]%>" selected><%=piThemes[i]%></option>
                              <%}else{%>
                                    <option value="<%=piThemes[i]%>"><%=piThemes[i]%></option>
                              <%}}%>
                              </select>
<!--                              <select name="piTheme" id="piTheme">
                                  <option value='blue' selected>Blue</option>
                                  <option value='orange'>Orange</option>
                              </select>-->
                          </td>
                      </tr>
                       <tr>
<!--                            added by krishan-->
                          <td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Default_Tab_Open", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                               <select name="reportTab" id="reportTab">
                               <%for(int i=0;i<reportTabs.length;i++){
                                  if(reportTabs[i].equalsIgnoreCase(reportTab)){%>
                                    <option value="<%=reportTabs[i]%>" selected><%=reportTabs[i]%></option>
                              <%}else{%>
                                    <option value="<%=reportTabs[i]%>"><%=reportTabs[i]%></option>
                              <%}}%>
                              </select>

<!--                          <select name="fromDateToDate" id="fromDateToDate">
                              <option value='false' selected>false&nbsp;</option>
                              <option value='true'>true</option>
                              <option value='right'>Right</option>
                          </select>-->
                      </td>
                      </tr>
                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Enable_Year_Time_Dimension", cle)%>: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                          <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                          <td align="left">
                              <%if(isYearCal.equalsIgnoreCase("Yes")){%>
                                    <input type="radio" name="sytm" id="sytm1" value="Yes" checked> Yes &nbsp;&nbsp;
                                    <input type="radio" name="sytm" id="sytm2" value="No"> No</td>
                              <%}else{%>
                                    <input type="radio" name="sytm" id="sytm1" value="Yes"> Yes &nbsp;&nbsp;
                                    <input type="radio" name="sytm" id="sytm2" value="No" checked> No</td>
                              <%}%>
<!--                                  <input type="radio" name="sytm" id="sytm1" value="Yes"> Yes &nbsp;&nbsp;
                              <input type="radio" name="sytm" id="sytm2" value="No" checked> No</td>-->


                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("pi_Version", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                          <select name="piVersion" id="piVersion">
                               <%for(int i=0;i<piVersion.length;i++){
                                  if(piVersion[i].equalsIgnoreCase(piVersions)){%>
                                    <option value="<%=piVersion[i]%>" selected><%=piVersion[i]%></option>
                              <%}else{%>
                                    <option value="<%=piVersion[i]%>"><%=piVersion[i]%></option>
                              <%}}%>
                          </select></td>
                      </tr>
<!--                      added by mayank for default page size on june/26/2015-->
                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Default_Page_Size", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                          <% if(defaultPageSize.equalsIgnoreCase("25")) {%>
                          <input id="defaultPageSize" name="defaultPageSize" type="text" value="25" size="5" />
                          <%} else { %>
                          <input id="defaultPageSize" name="defaultPageSize" type="text" value ="<%=defaultPageSize%>" />
                          <%}%>
                      </td>
                      </tr>
                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Default_No_of_Filters", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                        <input id="Filters" name="Filters" type="text" value="<%=Filters%>" size="5"  />
                         
                      </td>
                      </tr>
<!--                      // added by krishan Pratap-->
                      <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Default_Comparison", cle)%> &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                       <input type="radio" name="sytm12" id="sytm12" value="Yes" > Yes &nbsp;&nbsp;
                        <input type="radio" name="sytm12" id="sytm23" value="No" checked> No</td>
                      </tr>
                      <!--   added by Prabal Pratap Singh-->
                      <tr>
                       <td align="left" class="migrate"><label>Layout &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                             <select name="Layout" id="Layout">
                               <%for(int i=0;i<Layout.length;i++){
                                  if(Layout[i].equalsIgnoreCase(Layouts)){%>
                                    <option value="<%=Layout[i]%>" selected><%=Layout[i]%></option>
                              <%}else{%>
                                    <option value="<%=Layout[i]%>"><%=Layout[i]%></option>
                              <%}}%>
                              </select></td>
                      </td>
                      </tr>
                      <tr>
                       <td align="left" class="migrate"><label>Show Favourite Report Tag &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                             <select name="favReportAsTag" id="favReportAsTag">
                               <%for(int i=0;i<favReportAsTags.length;i++){
                                  if(favReportAsTags[i].equalsIgnoreCase(favReportAsTag)){%>
                                    <option value="<%=favReportAsTags[i]%>" selected><%=favReportAsTags[i]%></option>
                              <%}else{%>
                                    <option value="<%=favReportAsTags[i]%>"><%=favReportAsTags[i]%></option>
                              <%}}%>
                              </select></td>
                      </td>
                      </tr>
                      <tr>
                            <td align="left" class="migrate"><label>Open Report &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td align="left">
                                   <select name="openReportTab" id="openReportTab">
                               <%for(int i=0;i<openReportTabs.length;i++){
                                  if(openReportTabs[i].equalsIgnoreCase(openReportTab)){%>
                                    <option value="<%=openReportTabs[i]%>" selected><%=openReportTabs[i]%></option>
                              <%}else{%>
                                    <option value="<%=openReportTabs[i]%>"><%=openReportTabs[i]%></option>
                              <%}}%>
                              </select></td>
                           
                      </tr>
                      <tr>
                       <td align="left" class="migrate"><label>Recently Used Reports &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                            
                             <select name="RecentlyReports" id="RecentlyReportsAsTag">
                               <%for(int i=0;i<RecentlyReport.length;i++){
                                  if(RecentlyReport[i].equalsIgnoreCase(RecentlyReports)){%>
                                    <option value="<%=RecentlyReport[i]%>" selected><%=RecentlyReport[i]%></option>
                              <%}else{%>
                                    <option value="<%=RecentlyReport[i]%>"><%=RecentlyReport[i]%></option>
                              <%}}%>
                              </select></td>
               
                      </tr>
                      <tr>
                      <td align="left" class="migrate"><label>Custom Reports &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                      <td align="left">
                            
                             <select name="customReports" id="customReportAsTag">
                               <%for(int i=0;i<RecentlyReport.length;i++){
                                  if(customReport[i].equalsIgnoreCase(customReports)){%>
                                    <option value="<%=customReport[i]%>" selected><%=customReport[i]%></option>
                              <%}else{%>
                                    <option value="<%=customReport[i]%>"><%=customReport[i]%></option>
                              <%}}%>
                              </select></td>
                      
                      </tr>
                      <tr id="CurrencyShownAsTr" style="height: 100px;">
                            <td align="left" class="migrate"><label>Currency Shown &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            <td align="left" >
                                <div style="width: 50%;float: left;">
                                <select name="CurrencyShownAs" id="CurrencyShownAs" onchange="selectCurrencyShownAs();" multiple  style="height: 100px;">                                    
                                       <option value="Thousand" ><span>Thousand</span></option>
                                       <option value="Lakh" ><span>Lakh</span></option>
                                       <option value="Crore"><span>Crore </span></option>
                                       <option value="Million" ><span>Million</span></option>
                                       <option value="Billion"><span>Billion </span></option>
                                       <option value="Trillion" ><span>Trillion</span></option>
                                    </select>
                                      </div>
                               
                                <div id="selectedCurrencyAs" style="width: 50%;float: left;">
                                    
                                </div>
                            </td>
                      </tr>
                      <!--   end by Prabal Pratap Singh-->
                       <tr>
                            <td align="left" class="migrate"><label>Footer Option &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                           
                            <td align="left">
                                   <select name="FooterName" id="FooterName">
                               <%for(int i=0;i<FooterNames.length;i++){
                                  if(FooterNames[i].equalsIgnoreCase(FooterName)){%>
                                    <option value="<%=FooterNames[i]%>" selected><%=FooterNames[i]%></option>
                              <%}else{%>
                                    <option value="<%=FooterNames[i]%>"><%=FooterNames[i]%></option>
                              <%}}%>
                              </select></td>
                      </tr>
                         <tr><td align="left" class="migrate"><label><%=TranslaterHelper.getTranslatedInLocale("Default_Data_Show", cle)%> : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                      <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>


                      <td align="left">
                               <select name="showdata" id="showdata">
                               <%for(int i=0;i<dataShow.length;i++){
                                  if(dataShow[i].equalsIgnoreCase(dataShows)){%>
                                    <option value="<%=dataShow[i]%>" selected><%=dataShow[i]%></option>
                              <%}else{%>
                                    <option value="<%=dataShow[i]%>"><%=dataShow[i]%></option>
                              <%}}%>
                              </select>
<!--                              <select name="piTheme" id="piTheme">
                                  <option value='blue' selected>Blue</option>
                                  <option value='orange'>Orange</option>
                              </select>-->
                          </td>
<!--                      <td align="left">
                          <select name="piVersion" id="showdata">

                                   <option value='usbased' selected>Us Based&nbsp;</option>
                                   <option value='indianbased'>Indian Based</option>
                          </select></td>-->


                         </tr>
<!--                         Added by Faiz Ansari-->
                         <tr>
                             <td class="migrate">
                                <span>Header Tags Font Size</span><br><br>
                                <span>Header Tags Length(Characters)</span><br>                            
                             </td>
                             <td></td>
                             <td>                                   
                                <select id="headerFont" style='float:none;padding:0px 10px;'>
                                   <%for(int i=0;i<headerfont.length;i++){
                                  if(headerfont[i].equalsIgnoreCase(headerfonts)){%>
                                    <option  style='padding:0px 0px 0px 15px' value="<%=headerfont[i]%>" selected><%=headerfont[i]%></option>
                                        <%}else{%>
                                    <option style='padding:0px 0px 0px 15px' value="<%=headerfont[i]%>"><%=headerfont[i]%></option>
                                    <%}}%>
                                </select>
                                <br><br>
                                <select  id="headerLength"style='float:none;padding:0px 10px;'>
                                    <%for(int i=0;i<HEADER_LENGTh.length;i++){
                                  if(HEADER_LENGTh[i].equalsIgnoreCase(HEADER_LENGThS)){%>
                                    <option  style='padding:0px 0px 0px 15px' value="<%=HEADER_LENGTh[i]%>" selected><%=HEADER_LENGTh[i]%></option>
                                        <%}else{%>
                                    <option style='padding:0px 0px 0px 15px' value="<%=HEADER_LENGTh[i]%>"><%=HEADER_LENGTh[i]%></option>
                                    <%}}%>
                                </select>
                             </td>
                         </tr>
                             <tr>
                         <td class="migrate">
                                <span> Report Header With Date</span><br><br>                                                    
                             </td>
                              <td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
                          <td align="left">
                              <%if(session.getAttribute("dateheader")!=null && !session.getAttribute("dateheader").equals("") && session.getAttribute("dateheader").equals("Yes")){%>
                                         <input type="radio" name="enabledate" id="enabledate" value="Yes" checked> Yes &nbsp;&nbsp;
                                         <input type="radio" name="enabledate" id="disdate" value="No"> No</td>
                          <%}else{%>
                                     <input type="radio" name="enabledate" id="enabledate" value="Yes" > Yes &nbsp;&nbsp;
                                     <input type="radio" name="enabledate" id="disdate" value="No" checked> No</td>
                              <%}%>
                                 </tr>
<!--                         End!!!-->
                   </table>
                    </div>
                    <br> <br>
                    <table><tr>
                             <td>&nbsp;&nbsp;</td>
                            <td>
                                <input type="button" class="migrate" style="width:auto;" value="<%=TranslaterHelper.getTranslatedInLocale("Save_Details", cle)%>" id="btnn" onclick="saveGblParamDetails()"/></td>
<!--                            <td>&nbsp;&nbsp;</td>
                            <td><input type="button" class="migrate" style="width:auto;color:black" value="Reset" id="btnn" onclick="resetGblParam()"/></td>-->

                           </tr>
                    </table>
                </form>
        </div>

        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
        </div>

    </body>

</html>
