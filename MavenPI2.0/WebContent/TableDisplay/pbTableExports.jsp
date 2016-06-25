<%-- 
    Document   : pbTableExports
    Created on : 16 Nov, 2010, 7:10:56 PM
    Author     : progen
--%>
<%@page import="com.progen.i18n.TranslaterHelper"%>
<%@page import="java.util.Locale"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="prg.db.Container"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
String PbReportId = (String) request.getParameter("reportId");
String ctxPath =  request.getParameter("ctxPath");
 String themeColor="blue";
  if(session.getAttribute("theme")==null)
                      session.setAttribute("theme",themeColor);
                  else
                      themeColor=String.valueOf(session.getAttribute("theme"));
 int USERID=0;
 if(session.getAttribute("USERID")!=null)
                 USERID = Integer.parseInt((String) session.getAttribute("USERID"));
                 UserLayerDAO userdao = new UserLayerDAO();
                 HashMap paramhashmapPA=new HashMap();
                 String userType=userdao.getUserTypeForFeatures(USERID);
                 paramhashmapPA=userdao.getFeatureListAnaLyzer(userType,USERID);
               // added by krishan
                 String reportName = "";
                 Container container = Container.getContainerFromSession(request,PbReportId);

                 if (container.getReportName() != null) {
                    reportName = container.getReportName();
                }
  //ended by krishan
                  //added by Mohit Gupta for default locale
                    Locale cL=null;
                   cL=(Locale)session.getAttribute("UserLocaleFormat");
        //ended By Mohit Gupta
%>
<html>
    <head>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
 <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/ReportCss.css" rel="stylesheet"/>
        <link type="text/css" href="<%=request.getContextPath()%>/css/global.css" rel="stylesheet" />
<script type="text/javascript">




        $("#excelInfoDiv1").dialog({
       title:'Reports Excel Info',
       autoOpen:false,
       height: 250,
       width: 450,
       position: 'justify',
       modal: true
   });
    var fileType = $("#fileType option:selected").text();
    var records = $("#expType option:selected").text();
    var expRecord = $("#expRec option:selected").text();

//    function downloadExport(){
//    var fileType=document.getElementById("fileType").value;
//    var expType=document.getElementById("expType").value;
//    var expRec=document.getElementById("expRec").value;
//    var REPORTID=document.getElementById("REPORTID").value;
//    var pdfTypeSelect=document.getElementById("pdfTypeSelect").value;
//    var paramType=document.getElementById("paramWithOrwithoutId").value;
//    var pdfCellFont=document.getElementById("selectCellFontId").value;
//    var pdfCellHeight=document.getElementById("selectCellHeightId").value;
//    var htmlCellHeight=document.getElementById("selecthtmlCellHeightId").value;
//    var ctxPath=document.getElementById("h").value;
//    var delimiter=$('#sDelimiter').val();
//    var txtId=$('#txtIdentifier').val();
//    parent.$("#showExports").dialog('close');
//    var source = ctxPath+"/TableDisplay/pbDownload.jsp?dType="+fileType+"&tabId="+REPORTID+"&displayType="+expType+"&expRec="+expRec+"&dlimiter="+delimiter+"&txtIdentifier="+txtId+"&paramType="+paramType+"&pdfTypeSelect="+pdfTypeSelect+"&pdfCellFont="+pdfCellFont+"&pdfCellHeight="+pdfCellHeight+"&htmlCellHeight="+htmlCellHeight;
//    var dSrc = document.getElementById("dFrame");
//    dSrc.src = source;
//
//    if(Obj.style.display=='none'){
//        Obj.style.display="block";
//    }else{
//        Obj.style.display="none";
//    }
//    }
    function pdfType(){
        var selectedtype = $("#pdfTypeSelect").val();
        if(selectedtype=='A4'){
            $('#selectCellHeightId option[value="26.0f"]').attr('selected','selected');
            $('#selectCellFontId option[value="8.0f"]').attr('selected','selected');
        }
       else if(selectedtype=='A4_Landascpe'){
            $('#selectCellHeightId option[value="24.0f"]').attr('selected','selected');
            $('#selectCellFontId option[value="9.0f"]').attr('selected','selected');
        }
       else if(selectedtype=='B3'){
            $('#selectCellHeightId option[value="22.0f"]').attr('selected','selected');
            $('#selectCellFontId option[value="10.0f"]').attr('selected','selected');
        }
       else if(selectedtype=='B3_Landascpe'){
            $('#selectCellHeightId option[value="20.0f"]').attr('selected','selected');
            $('#selectCellFontId option[value="10.0f"]').attr('selected','selected');
        }
    }

       //added by krishan
    function temptype()
    {
        // alert("temptype calling in pbtableexports")
        var reportid='<%=PbReportId%>';
         var ctxPath1='<%=request.getContextPath()%>';


        var selecttemplate=$("#tempWithOrwithoutId").val();

        if(selecttemplate=='tempwithParameters'){
          $.ajax({
                        type: 'GET',
                        async: false,
                        cache: false,
                        timeout: 30000,
                            url:ctxPath1+'/userLayerAction.do?userParam=getallTemplate&reportId='+reportid,
                            success: function(data){

                                $('#usersDiv').html(data);
                            }
                        });
              $("#downloadexcel").show();
              $("#usersDiv").show();
              $("#paramTypeId").hide();
              $("#downloadall").hide();

        }
        else{
              $("#downloadall").show();
              $("#downloadexcel").hide();
              $("#usersDiv").hide();
            $("#paramTypeId").show();
        }

    }

     // added by krishan Pratap
     function downloadExport1(){
    
     parent.$("#datalist").hide();
       var fileName = document.getElementById("selId").value;
     
      var grpColArray=2;
      var reportId = '<%=PbReportId%>';
       var reportName='<%=reportName%>';

     if(fileName != ''){

            $("#excelInfoTab").append('<tr bgcolor=#F2FFFF><td width=40%>Report Name</td><td width=15%>Sheet No.</td><td width=12%>Line No.</td><td width=12%>Col No.</td><td>Header</td><td>GT</td></tr><tr></tr>');
            for(var i =1; i<grpColArray;i++){
                $("#excelInfoTab").append('<tr><td width=40% bgcolor=#F2FFFF>'+reportName+'</td><td width=15%><input type=number style="width:90%" min=1 id =sheetId name='+reportId+'_sheetName /></td><td width=12%><input type=number style="width:90%" min=1 name='+reportId+'_lineName id=lineId></td><td width=12%><input type=number style="width:90%" min=1 id=colId name='+reportId+'_colName></td><td width=12%><center><input type=checkbox id=headId name='+reportId+'_headName /></center></td><td><input type=checkbox id=gtId name='+reportId+'_gtName></td></tr>');
            }

       $("#showExportsdiv").hide();
       $("#excelInfoDiv1").show();

     }
      else{
         alert("Please select the Template Name")
          $("#showExportsdiv").show();
     }
    }

            function downloadExcelswithTemplate(){

              var ctxPath1='<%=request.getContextPath()%>';
           // alert("downloadExcelswithTemplate")
             var reportId = '<%=PbReportId%>';
             $("#excelInfoDiv").dialog('close');
            var sheetNs="";
            var lineNs="";

            var fileName = document.getElementById("selId").value;

                          var iForm = document.getElementById("excelInfoId1");

                           iForm.setAttribute("action",ctxPath1+"/reportViewer.do?reportBy=exportReportsIntoExcelsheetsTemp&reportIds="+reportId+"&fileName="+fileName);
                           $("#excelInfoId1").submit();
                          parent.$("#showExports1").dialog('close');
                          }
//        ended by krishan pratap
</script>

    </head>
    <body>
         <input type="hidden" name="REPORTID" id="REPORTID" value="<%=PbReportId%>">
         <input type="hidden" name="REPORTID" id="h" value="<%=ctxPath%>">
          <IFRAME NAME="dFrame" ID="dFrame" STYLE="display:none;width:0px;height:0px" SRC="TableDisplay/pbDownload.jsp" frameborder="0"></IFRAME>
   
        <div id="showExportsdiv" title="Exports" style="height: 120px;" >
                   <table align="center">
                <tbody>
                    <tr>
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("File_Type", cL)%> :</td>
                        <td>
                            <select name="fileType" id="fileType" style="width:130px" onchange="changeExportType()">
                                <% if(userdao.getFeatureEnable("Export to Excel") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>
                                <option class="gFontFamily gFontSize12" value="E">Excel</option>
                                <% }if(userdao.getFeatureEnable("Export to PDF") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>
                                <option class="gFontFamily gFontSize12" value="P">PDF</option>
                                <% }if(userdao.getFeatureEnable("Export as HTML") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>
<!--                                <option class="gFontFamily gFontSize12" value="H">HTML</option>-->
                                <% }if(userdao.getFeatureEnable("Export as CSV") || userType.equalsIgnoreCase("SUPERADMIN"))  {%>
                                <option class="gFontFamily gFontSize12" value="CSV">CSV</option>
                                <% } %>
<!--                                <option class="gFontFamily gFontSize12" value="CSN">CSV WithoutParams</option>-->
<!--                                <option value="OfficeExcel">Office Excel (xlsx)</option>-->
<!--                                <option value="X">XML</option>-->
<!--                                <option class="gFontFamily gFontSize12" value="OfficeExcel">Office Excel (xlsx)</option>
                                <option class="gFontFamily gFontSize12" value="X">XML</option>-->
                                <option class="gFontFamily gFontSize12" value="TS">Tab Separated</option>
                                <option class="gFontFamily gFontSize12" value="CD">Custom Delimeter</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="SP" style="display:none">
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Separation_Delimiter", cL)%></td>
                        <td><input type="textbox" value=";" id="sDelimiter" style="width:127px"></td>
                   </tr>
                   <tr id="TI" style="display:none">
                       <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Text_Identifier", cL)%></td>
                       <td>
                           <input type="textbox" value='"' id="txtIdentifier" style="width:127px"><font color="red">(" ' " <%=TranslaterHelper.getTranslatedInLocale("not_allowed", cL)%>)</font>
                       </td>
                   </tr>
                    
                    <tr>
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Export_Type", cL)%> :</td>
                        <td>
                            <select name="expType" id="expType" style="width:130px">
                                <option id="reportOpt" class="gFontFamily gFontSize12" value="Report">Report</option>
                                <option id="graphOpt" class="gFontFamily gFontSize12" value="Graph">Graph</option>
                                <option id="reportgraphOpt" class="gFontFamily gFontSize12" value="ReportAndGraph">Report & Graph</option>

                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Records", cL)%> :</td>
                        <td>
                            <select name="expRec" id="expRec" style="width:130px">
                                <option class="gFontFamily gFontSize12" value="All">All</option>
                                <option class="gFontFamily gFontSize12" value="Current">Current</option>
                            </select>
                        </td>
                    </tr>
                    <!--                    added by krishan-->
                    <tr id="tempTypeId"  onchange="temptype()">
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Template_Type", cL)%> :</td>
                        <td>
                            <select name="tempWithOrwithout" id="tempWithOrwithoutId" style="width:130px">
                                <option id="tempwithoutParameters" class="gFontFamily gFontSize12" value="tempwithoutParameters">With Out Template</option>
                                <option id="tempwithParameters" class="gFontFamily gFontSize12" value="tempwithParameters">With  Template</option>
                            </select>
                        </td>
                    </tr>

                    <tr id="paramTypeId"  >
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Parameters_Type", cL)%> :</td>
                        <td>
                            <select name="paramWithOrwithout" id="paramWithOrwithoutId" style="width:130px">
                                <option class="gFontFamily gFontSize12" value="withParameters">With Parameters</option>
                                <option class="gFontFamily gFontSize12" value="withoutParameters">With Out Parameters</option>
                            </select>
                        </td>
                    </tr>
                    <tr id="usersDiv" >

                    </tr>

                     <tr id="pageTypeId" style="display: none" >
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Page_Type", cL)%> :</td>
                        <td>
                                <select id="pdfTypeSelect" style="width:130px" onchange="pdfType()">
                                    <option id="A4" class="gFontFamily gFontSize11" value="A4" >A4-Portrait</option>
                                    <option id="A4_landascape" class="gFontFamily gFontSize12" value="A4_Landascpe">A4-Landscape</option>
                                    <option id="B3" class="gFontFamily gFontSize12" value="B3"  >B3-Portrait</option>
                                    <option id="B3_landascape" class="gFontFamily gFontSize12" value="B3_Landascpe">B3-Landscape</option>
                                </select>
                            </td>
                    </tr>
                    <tr id="pdfcellHeightId" style="display: none" >
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Cell_Height", cL)%> :</td>
                        <td>
                                <select id="selectCellHeightId" style="width:130px">
                                    <option id="16" class="gFontFamily gFontSize12" value="16.0f">16 Pixel</option>
                                    <option id="18" class="gFontFamily gFontSize12" value="18.0f">18 Pixel</option>
                                    <option id="20" class="gFontFamily gFontSize12" value="20.0f">20 Pixel</option>
                                    <option id="22" class="gFontFamily gFontSize12" value="22.0f">22 Pixel</option>
                                    <option id="24" class="gFontFamily gFontSize12" value="24.0f">24 Pixel</option>
                                    <option id="26" class="gFontFamily gFontSize12" value="26.0f" selected>26 Pixel</option>
                                    <option id="28" class="gFontFamily gFontSize12" value="28.0f">28 Pixel</option>
                                    <option id="28" class="gFontFamily gFontSize12" value="30.0f">30 Pixel</option>
                                </select>
                            </td>
                    </tr>
                    <tr id="pdfcellFontId" style="display: none" >
                        <td class="gFontFamily gFontSize12"><%=TranslaterHelper.getTranslatedInLocale("Cell_Font", cL)%> :</td>
                        <td>
                                <select id="selectCellFontId" style="width:130px">
                                    <option id="8" class="gFontFamily gFontSize12" value="8.0f" >8</option>
                                    <option id="9" class="gFontFamily gFontSize12" value="9.0f" >9</option>
                                    <option id="10" class="gFontFamily gFontSize12" value="10.0f">10</option>
                                </select>
                            </td>
                    </tr>
                    <tr id="htmlcellHeightId" style="display: none" >
                        <td class="gFontFamily gFontSize11"><%=TranslaterHelper.getTranslatedInLocale("Html_Cell_Height", cL)%> :</td>
                        <td>
                                <select id="selecthtmlCellHeightId" style="width:130px">
                                    <option class="gFontFamily gFontSize12" value="20">20 Pixel</option>
                                    <option class="gFontFamily gFontSize12" value="25">25 Pixel</option>
                                    <option class="gFontFamily gFontSize12" value="30">30 Pixel</option>
                                    <option class="gFontFamily gFontSize12" value="35">35 Pixel</option>
                                </select>
                            </td>
                    </tr>
                    <tr>
                        <td id="downloadall" align="center" colspan="2">
                        <input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="downloadExport()" style="width:40px" align="center">
                        </td>
<!--                        added by krishan-->
                        <td id="downloadexcel" align="center" colspan="2" style="display: none" >
                        <input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("done", cL)%>" onclick="downloadExport1()" style="width:40px" align="center">
                        </td>
                    </tr>
                </tbody>
            </table>
<!--            <input class="navtitle-hover" type="button" value="Done" onclick="downloadExport()">-->
        </div>
           <!--                   added by krishan             -->

                                   <div id="excelInfoDiv1" class="ui-dialog-titlebar" style="display:none" >
                          <form name="excelInfoName" id="excelInfoId1" action="" method="post">
                              <table id="excelInfoTab">
                              </table>


                          </form>
                            <center><input type='button' class='navtitle-hover' onclick='downloadExcelswithTemplate();' name='Download' value ='<%=TranslaterHelper.getTranslatedInLocale("download", cL)%>Download'/></center>
                           <p class="gFontFamily gFontSize12">*Please start sheet, row and column number from 1</p>
                        </div>


    </body>
</html>
