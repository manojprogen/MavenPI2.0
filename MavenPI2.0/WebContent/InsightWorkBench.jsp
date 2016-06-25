<%-- 
    Document   : InsightWorkBench
    Created on : Jun 16, 2013, 8:13:21 PM
    Author     : progen
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,java.util.Locale,com.progen.reportview.db.PbReportViewerDAO,java.util.Calendar"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO,com.progen.studio.StudioDao,java.util.HashMap,prg.util.screenDimensions,java.util.ArrayList,prg.db.PbReturnObject"%>
<!DOCTYPE html>
<%
       // HttpSession session = request.getSession(false);
           //for clearing cache
           //added by Dinanath for default locale
                    Locale currentLocale=null;
                    currentLocale=(Locale)session.getAttribute("UserLocaleFormat");

           response.setHeader("Cache-Control", "no-store");
           response.setHeader("Pragma", "no-cache");
           response.setDateHeader("Expires", 0);
           String userId = String.valueOf(session.getAttribute("USERID"));
           screenDimensions dims = new screenDimensions();
           int pageFont, anchorFont;
           HashMap screenMap = dims.getFontSize(session, request, response);
           if (!String.valueOf(screenMap.get("pageFont")).equalsIgnoreCase("NULL")) {
               pageFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
               anchorFont = Integer.parseInt(String.valueOf(screenMap.get("pageFont")));
               ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
           } else {
               pageFont = 11;
               anchorFont = 11;
               ////.println("pageFont--" + pageFont + "---anchorFont--" + anchorFont);
           }
           ReportTemplateDAO dao = new ReportTemplateDAO();
           PbReturnObject retObj = null;
           retObj = dao.getBurolsByUserId(userId);
           ArrayList<String> folderIdsList = new ArrayList<String>();
           ArrayList<String> folderNamesList = new ArrayList<String>();
           HashMap<String,String> rolemap=new HashMap<String, String>();
           String FolderId = "";
           String FolderName = "";
           String[] colNames = null;
           String themeColor = "blue";
           if (retObj != null && retObj.getRowCount() != 0) {
               colNames = retObj.getColumnNames();
               for (int i = 0; i < retObj.getRowCount(); i++) {
                   FolderId = retObj.getFieldValueString(i, colNames[0]);
                   FolderName = retObj.getFieldValueString(i, colNames[1]);
                   folderIdsList.add(FolderId);
                   folderNamesList.add(FolderName);
                   rolemap.put(FolderId, FolderName);
               }
           }
//           StudioDao dao1=new StudioDao();
//           retObj=dao1.getAllInsights(userId);
           PbReportViewerDAO repdao=new PbReportViewerDAO();
           retObj=repdao.getAllInsights(userId);
           ArrayList<String> reportIdlist=new ArrayList<String>();
           ArrayList<String> reportNamelist=new ArrayList<String>();
           ArrayList<String> rolelist=new ArrayList<String>();
           ArrayList<String> roleIdlist=new ArrayList<String>();
           ArrayList<String> createdDatelist=new ArrayList<String>();
           Calendar cal = Calendar.getInstance();
           if(retObj!=null && retObj.getRowCount()>0){
               for(int i=0;i<retObj.getRowCount();i++){
                reportIdlist.add(retObj.getFieldValueString(i, 0));
                reportNamelist.add(retObj.getFieldValueString(i, 1));
                rolelist.add(rolemap.get(retObj.getFieldValueString(i, 2)));
                   if(retObj.getFieldValueDate(i, 3)!=null && !retObj.getFieldValueDateString(i, 3).equalsIgnoreCase("")){
                        cal.setTime(retObj.getFieldValueDate(i, 3));
                        String dateAfterSort = (cal.get(Calendar.MONTH) + 1)+"/"+cal.get(Calendar.DATE)+"/"+cal.get(Calendar.YEAR);
                        createdDatelist.add(dateAfterSort);
                    }else
                         createdDatelist.add("");
                roleIdlist.add(retObj.getFieldValueString(i,2));
               }
           }
           String contexpTah=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>
<!--    <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->
    <script type="text/javascript" src="<%=contexpTah%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="<%=contexpTah%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
    <link type="text/css" href="<%=contexpTah%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
    <script type="text/javascript" src="<%=contexpTah%>/tablesorter/jquery.tablesorter.js"></script>
    <script type="text/javascript" src="<%=contexpTah%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
    <script type=""  language="JavaScript" src="<%=contexpTah%>/tablesorter/jquery.columnfilters.js"></script>
   <link rel="stylesheet" type="text/css" href="<%=contexpTah%>/stylesheets/tablesorterStyle.css" />
<!--   <script  type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js" ></script>
   
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbReportViewerJS.js"></script>-->
<!--     <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportDesign.js"></script>-->
        <script type="text/javascript" src="<%=contexpTah%>/javascript/reportviewer/ReportViewer.js"></script>
        <link type="text/css" href="<%=contexpTah%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/pbreporttemplateframejs.js"></script>-->
        <script type="text/javascript" src="<%=contexpTah%>/TableDisplay/JS/pbTableMapJS.js"></script>
         <script  type="text/javascript" src="<%=contexpTah%>/javascript/pbReportViewerJS.js" ></script> 
         
<!--          <link href="<%=request.getContextPath()%>/css/styles.css" rel="stylesheet" type="text/css">-->
        <link href="<%=contexpTah%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" type="text/css">
        <link href="<%=contexpTah%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
         <link rel="stylesheet" type="text/css" href="<%=contexpTah%>/stylesheets/jqcontextmenu.css"/>
         <script src="<%=contexpTah%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
         <script src="<%=contexpTah%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
          <script type="text/javascript" src="<%=contexpTah%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>
          <link rel="stylesheet" href="<%=contexpTah%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />
           <link type="text/css" href="<%=contexpTah%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
           
           
<!--        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />-->
   <style type="text/css">
/*       div.ui-widget-content {
    border: 0px solid rgb(166, 201, 226);
    background: url("images/ui-bg_inset-hard_100_fff_1x100.png") repeat-x scroll 50% bottom rgb(255, 255, 255);
    color: rgb(34, 34, 34);
}*/
       table.tablesorter {
    font-family: arial;
    background-color: rgb(220, 220, 220);
    margin: 10px 0pt 15px;
    font-size: 8pt;
    width: 100%;
    text-align: left;
    }
    table.tablesorter thead tr th, table.tablesorter tfoot tr th {
    background-color: rgb(220, 220, 220);
    border: 1px solid rgb(220, 220, 220);
    font-size: 8pt;
    padding: 4px;
}
    table.prgtable {
    font-family: arial;
    background-color: rgb(255, 255, 255);
    margin: 10px 0pt 15px;
    font-size: 8pt;
    width: 100%;
    height: 40px;
    text-align: left;
 }
    div table.prgtable {
    width: 99%;
    }
    * {
      font: 11px verdana;
    }
    .ui-progressbar-value { background-image: url(images/barchart.gif); }
            table.grid .collapsible {
                padding: 0 0 3px 0;
            }

            .collapsible a.collapsed {
                display: block;
                width: 15px;
                height: 15px;
                background: url(<%=request.getContextPath()%>/images/addImg.gif) no-repeat 3px 3px;
                outline: 0;
            }

            .collapsible a.expanded {
                display: block;
                width: 15px;
                height: 15px;
                background: url(<%=request.getContextPath()%>/images/deleteImg.gif) no-repeat 3px 3px;
                outline: 0;
            }
            .tabMenuCol1
            {
                background-color:#ffffff;
            }
            a {font-family:Verdana;cursor:pointer;font-size:<%=anchorFont%>px;text-decoration:none}
            *{font:<%=pageFont%>px verdana}
            option.optionclass{text-align:right;}
/*   .ui-datepicker {
    padding: 0.2em 0.2em 0;
    width: 27em;
    z-index: 1000000;
    position: absolute;
    top: 89.1px;
    left: 998.85px;
    display: block;
    }*/
.ui-datepicker {
    padding: 0.2em 0.2em 0;    
    width: 200px;
    z-index: 1000000;
}
            
   </style>
   
   <script type="text/javascript">
                                initCollapser("");
                            </script>
    </head>
    <body>
<!--         <div id="Insightdiv" style=" height: 100px; border-width: 4px; border-style: groove; border-color: grey; margin-top: auto; margin-left: 10px; margin-right: 10px;  padding-bottom: 400px; padding-left: 100px;overflow: auto; "> &nbsp;
         </div>-->

  <div id="InsightIconDiv" style="display:none;">
       <form action="" name="InsightForm" id="InsightForm" method="post">
     <table valign="top" width="100%">         
         <tr> 
          <td align="left"><span id="insightName"  style="color: #4F4F4F;font-family:verdana;font-size:18px;font-weight:bold"></span></td>          
          <td valign="top" align="right" width="1%"><a class="ui-icon ui-icon-plusthick" title="Edit Parameters" style="text-decoration:none" onclick="changeParameters(reportId,roleId,'<%=request.getContextPath()%>');" href="javascript:void(0)"></a></td>
          <td valign="top" align="right" width="1%"><a class="ui-icon ui-icon-pencil" title="Edit Measures" style="text-decoration:none" onclick="changeMeasures(reportId,roleId,'<%=request.getContextPath()%>');" href="javascript:void(0)"></a></td>
          <td id="periodBasisTd" align="right" style="display:none;" valign="top" width="20%">
            <table><tr>
             <td><input type="text" id="CBO_AS_OF_DATE" name="CBO_AS_OF_DATE" class="datePicker"  value="" size="15"></td>
             <td>
                    <select id="CBO_PRG_PERIOD_TYPE"  name="CBO_PRG_PERIOD_TYPE"width="12px">
                            <option id="Day" value="Day">Day</option>
                            <option id="Week" value="Week">Week</option>
                            <option id="Month" value="Month">Month</option>
                            <option id="Quarter" value="Quarter">Quarter</option>
                            <option id="Year" value="Year">Year</option>
                        </select>
                </td>
                 <td>
                            <select id="CBO_PRG_COMPARE" name="CBO_PRG_COMPARE">
                            <option id="Last Period" value="Last Period">Last Period</option>
                            <option id="Last Year" value="Last Year">Last Year</option>
                            <option id="Period Complet" value="Period Complete">Period Complete</option>
                            <option id="Year Complet" value="Year Complete">Year Complete</option>
                        </select>
                    </td>                     
             </tr></table>
          </td>
          <td id="rangebasisTd" align="right" style="display:none;" width="20%" valign="top">
              <table><tr>
                <td><input type="text" id="CBO_AS_OF_DATE1" name="CBO_AS_OF_DATE1" class="datePicker"  value="" size="15"></td>
                <td><input type="text" id="CBO_AS_OF_DATE2" name="CBO_AS_OF_DATE2" class="datePicker"  value="" size="15"></td>
                <td><input type="text" id="CBO_CMP_AS_OF_DATE1" name="CBO_CMP_AS_OF_DATE1" class="datePicker"  value="" size="15"></td>
                <td><input type="text" id="CBO_CMP_AS_OF_DATE2" name="CBO_CMP_AS_OF_DATE2" class="datePicker"  value="" size="15"></td>
                 </tr>                  
              </table>
          </td>
          <td id="goTabId" valign="top" align="right" width="2%"><input type="button" name=""  class="navtitle-hover" id="gottabId" style='width:25px' value="Go" onclick="goInsightPage()"></td>
          <td valign="top" align="right" width="1%"><a class="ui-icon ui-icon-disk" href="#" onclick="saveInsight()" title="Save Insight workBench" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;"></a></td>          
          <td valign="top" align="right" width="1%"><a class="ui-icon ui-icon-home" href="#" onclick="insighthome()" title="Insight workBench Home" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;"></a></td>          
          </tr>
    </table>
     <div id="DisplayInsightdiv" style=" height: 100px; border-width: 4px; border-style: groove; border-color: grey; margin-top: auto; margin-left: 10px; margin-right: 10px;  padding-bottom: 400px; padding-left: 100px;overflow: auto; "> &nbsp;
  </div>
  </form>
 </div>          
 
 
<div id="InsightHomeDiv" style=" height: 100px; border-width: 4px;  margin-top: auto; margin-left: 10px; margin-right: 10px;  padding-bottom: 400px; padding-left: 100px;overflow: auto; "> &nbsp;
    <table align="right" width="2%">
        <tr>
            <td>&nbsp;</td>
            <td>
                <a class="ui-icon ui-icon-plusthick"  href="javascript:void(0)" title="Create InsightWorkBench" onclick="createInsight()" ></a>
            </td>
        </tr>
    </table>
    <table align="left" id="insightTableId" class="tablesorter" cellspacing="1" cellpadding="0" border="0px solid" align="left"  width="50%" >         
        <thead>
            <tr>
                <th><%=TranslaterHelper.getTranslatedInLocale("Insight_Name", currentLocale)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Role", currentLocale)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Created_Date", currentLocale)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Delete", currentLocale)%></th>
                <th><%=TranslaterHelper.getTranslatedInLocale("Assignment", currentLocale)%></th>
            </tr>
        </thead>
        <tbody>
           <% for(int i=0;i<reportIdlist.size();i++){%>
           <tr><td align="left"><a href="#" onclick="openInsightPage('<%=reportNamelist.get(i)%>','<%=reportIdlist.get(i)%>','<%=roleIdlist.get(i)%>')"><%=reportNamelist.get(i)%></a></td>
           <td align="left"><%=rolelist.get(i)%></td>
           <td align="left"><%=createdDatelist.get(i)%></td>
           <td align="left"><a  href='javascript:void(0)' class="ui-icon ui-icon-trash" title="Delete Insight" onclick="delteteInsight('<%=reportIdlist.get(i)%>')"></a></td>
           <td><a class="ui-icon ui-icon-person" href="javascript:openInsightPageAssignment('<%=request.getContextPath()%>','<%=userId%>','<%=roleIdlist.get(i)%>','<%=reportIdlist.get(i)%>','<%=reportNamelist.get(i)%>')" title="Assign Insight to Users"></a></td>
           </tr>
            <%}%>           
        </tbody>
    </table>
</div>
<div id="createInsightDiv" title="Create Insight" >
    <table width="100%">
        <tr>
          <td>Enter Name</td>
          <td><input  type="text" name="" value="" id="insightNameId"  style="width:150px"></td>
        </tr>
        <tr>
            <td>Select Business Role</td>
            <td>
                <select id='folderId' style="width:150px">
                   <% for(int i=0;i<folderIdsList.size();i++){%>
                      <option value='<%=folderIdsList.get(i)%>' class="optionclass"><%=folderNamesList.get(i)%></option>    
                   <%}%>
                </select>
            </td>
        </tr>        
        <tr><td>&nbsp;</td></tr>
        <tr><td colspan="2" align="center"> <input class="navtitle-hover" type="button" onclick="insertInsightName()" value="Done"></td></tr>
    </table>
    
</div>
               
                <div id="AddMoreParamsDiv" title="Add More Dimension" style="display:none">
                    <table id="timeSelectionId"><tr> <td><a class="ui-icon ui-icon-clock"></a></td>
                            <td>
                                <select style="width:120px" id="time" name="time">
                                    <option id="st" onclick="timeBasis()">none</option>
                                    <option id="StandardTime" value="StandardTime" onclick="timeBasis()" name="time">Standard Time</option>
                                    <option id="RangeBasis" value="RangeBasis" onclick="timeBasis()" name="time">Range Basis</option>
                                </select>
                            </td></tr></table>
                    <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
                </div>
                <div id="measuresDialog" style="display:none" title="Add Measures">
    <table><tr><td>
            <input id="tableList" type="checkbox" onclick="getDisplayTablesInDesigner('<%=request.getContextPath() %>','','')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListInDesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainerInDesigner('<%=request.getContextPath() %>','')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="dataDispmem" NAME='dataDispmem' frameborder="0" width="100%" height="100%" SRC='#'></iframe>

</div>
       <div id="divassign"  style="display:none;width:100%;height:100%;position:centre;" title="Assign Insight to Users">
     <iframe name="divassignframe" id="divassignframe" style="display:'';width:100%;height:100%;border:none;" src="about:blank"></iframe>
        </div>
                <input type="hidden" id="roleid" name="" value="">
                <input type="hidden" id="REPORTID" name="" value="">
                <input type="hidden" id="Designer" name="Designer" value="fromInsightDesigner">
                <input type="hidden" id="h" value="<%=request.getContextPath()%>">                
                <input type="hidden" name="MsrIds" value="" id="MsrIds">
                <input type="hidden" name="Measures" value="" id="Measures">
                <input type="hidden" name="action" value="fromDesigner" id="action">
                <script type="text/javascript">
       var reportId;
       var reportName;
       var roleId;
       //var statusFlag="save";
       $("#insightTableId")
       .tablesorter({widthFixed: true})
      $(document).ready(function() {
           $("#createInsightDiv").dialog({
                    autoOpen: false,
                    height: 160,
                    width: 400,
                    position: 'justify',
                    modal: true
                });
            $("#measuresDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
        $("#AddMoreParamsDiv").dialog({
       autoOpen: false,
       height: 350,
       width: 450,
       position: 'justify',
       modal: true,
       resizable:true
    });
           $("#CBO_AS_OF_DATE").datepicker({
             changeMonth: true,
              changeYear: true,
              showButtonPanel: true,
              numberOfMonths: 1,
              stepMonths: 1
               });
           $("#CBO_AS_OF_DATE1").datepicker({
             changeMonth: true,
              changeYear: true,
              showButtonPanel: true,
              numberOfMonths: 1,
              stepMonths: 1
               });
           $("#CBO_AS_OF_DATE2").datepicker({
             changeMonth: true,
              changeYear: true,
              showButtonPanel: true,
              numberOfMonths: 1,
              stepMonths: 1
               });
           $("#CBO_CMP_AS_OF_DATE1").datepicker({
             changeMonth: true,
              changeYear: true,
              showButtonPanel: true,
              numberOfMonths: 1,
              stepMonths: 1
               });
           $("#CBO_CMP_AS_OF_DATE2").datepicker({
             changeMonth: true,
              changeYear: true,
              showButtonPanel: true,
              numberOfMonths: 1,
              stepMonths: 1
               });               
              
       })
       
//       $(document).ready(function() {
//           $.get("<%=request.getContextPath()%>/insights.do?reportBy=getInsightWorkBenchDetails",function(data){
//               alert("work")
//                                 alert(data.toString())
//               var jsonVar=eval('('+data+')')
//               var folderIds=jsonVar.folderIds;
//               var folderNames=jsonVar.folderNames;
//               var htmlVar="<select id='folderId'>"
//               for(var i=0;i<folderIds.length;i++){
//                htmlVar+="<option value='"+folderIds[i]+"'>"+folderNames[i]+"</option>";   
//               }
//               htmlVar+="</select>";
//               $("#roleId").html(htmlVar);
//               
//           })
//           })
function timeBasis(){
          // parent.$("#selectTime").hide();
//           var timeDetail = parent.document.getElementsByName("time");
//           alert(timeDetail);
           var timeDetail = document.getElementById("time").value

                if(timeDetail == "StandardTime"){
                    var timeDim='AS_OF_DATE,PRG_PERIOD_TYPE,PRG_COMPARE';
                    var timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }  else if(timeDetail == "RangeBasis"){
                    timeDim='AS_OF_DATE1,AS_OF_DATE2,CMP_AS_OF_DATE1,CMP_AS_OF_DATE2';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                }
                 else {
                    timeDim='';
                    timeDimension=timeDim.split(",");
                    for(var i=0;i<timeDimension.length;i++){
                        timeDimArray.push(timeDimension[i]);
                    }
                 }
       }
               function createInsight(){                  
                    $("#createInsightDiv").dialog({
                    autoOpen: false,
                    height: 160,
                    width: 400,
                    position: 'justify',
                    modal: true
                });
                   $("#createInsightDiv").dialog('open');
               }
               function insertInsightName(){
                   $("#createInsightDiv").dialog('close');
                 //  statusFlag="save";
                    parent.$("#workBenchId").hide();
                    parent.$("#tabs").removeClass("ui-widget-content");
                   $("#action").val("fromDesigner");
                   $("#InsightHomeDiv").hide();
                    $("#InsightIconDiv").show();
                    // $("#DisplayInsightdiv").show();
//                   alert($("#insightNameId").val())
//                   alert($("#folderId").val())
                   $("#roleid").val($("#folderId").val());
                   var insightName=$("#insightNameId").val();
                   $("#insightName").html(insightName);
                   roleId=$("#folderId").val();
                    $.ajax({
                    url: "reportTemplateAction.do?templateParam=createInsightDesigner&insightName="+insightName+"&roleId="+roleId,
                    success: function(data){                        
                        $("#REPORTID").val(data);
                        reportId=data;
                        AddMoreDims('<%=request.getContextPath()%>');
                    }
                });
                   
               }
               
    function AddMoreDims(ctxPath) {
//    alert("dimenssions")
//    alert(ctxPath)
    $("#AddMoreParamsDiv").dialog({
       autoOpen: false,
       height: 350,
       width: 450,
       position: 'justify',
       modal: true,
       resizable:true
    });
    var frameObj=document.getElementById("addmoreParamFrame");
    var source=ctxPath+"/reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+roleId+"&REPORTID="+reportId;
    frameObj.src=source;
//    alert(source)
    $("#AddMoreParamsDiv").dialog('open'); 
    }
     function cancelDim(){
               $("#AddMoreParamsDiv").dialog('close');
               $("#measuresDialog").dialog({
            autoOpen: false,
            height: 400,
            width: 720,
            position: 'justify',
            modal: true
        });
            }
     function getDisplayTablesInDesigner(ctxpath,paramslist,repId){
       var repId=reportId;
        var frameObj=document.getElementById("dataDispmem");
//        getdimmap.getFact(Parameters);
        var check = $("#tableList").is(":checked")
        if($("#tableList").is(":checked")){
            $("#tabListDiv").hide();
            $("#tablistLink").hide();
            $("#goButton").hide();
            $("#tabsListIds").val("");
            $("#tabsListVals").val("");
         var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
            //$("#tableList").attr('checked',true);
        }else{
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);
                    var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value;
        //frameObj.src='<center><img id="imgId" src="images/ajax.gif" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
        frameObj.src=source;
                });

        }
    }
    function showListInDesigner(ctxpath,paramslist){
        if(document.getElementById("paramVals").style.display=='none'){
            $("#paramVals").show();
            $("#tabListDiv").show();
            $("#tablistLink").show();
            $("#goButton").show();
            var htmlVar = "<table>";
           $.post(ctxpath+"/reportViewer.do?reportBy=getTableList&paramslist="+paramslist+'&currentBizRoles='+document.getElementById("roleid").value,
                function(data){
                    var jsonVar=eval('('+data+')')
                    var json1 = jsonVar.idsList.split(",");
                    var jsonname = jsonVar.namesList.split(",");
                    for(var i=0;i<json1.length;i++){
                        //alert(jsonname[i])
                        if(json1[i].replace(" ", "", "gi") != "0")
                        htmlVar += "<tr><td id='"+json1[i]+"' onclick=\"selectTablesInDesigner('"+json1[i]+"','"+jsonname[i]+"')\">"+jsonname[i]+"</td></tr>";
                    }
                    htmlVar += "</table>";
                    $("#paramVals").html(htmlVar);

                });
        }else{
           $("#paramVals").hide();
        }

    }
    function selectTablesInDesigner(tdId,tname){
        //alert(tname)
        document.getElementById(tdId).style.display='none';
        if($("#tabsListVals").val() == ""){
            $("#tabsListVals").val(tname)
            $("#tabsListIds").val(tdId)
        }else{
            var Ids = $("#tabsListIds").val()+","+tdId
            var value = $("#tabsListVals").val()+","+tname
            $("#tabsListIds").val(Ids)
            $("#tabsListVals").val(value)
        }
    }
    function setValueToContainerInDesigner(ctxpath,repId,bizRoles){
       var frameObj=document.getElementById("dataDispmem");
       $("#paramVals").hide();
       var tabLst = $("#tabsListIds").val();
       $("#tabsListVals").val('')
       $("#tabsListIds").val('')
       $("#tabListDiv").hide();
           $("#tablistLink").hide();
           $("#goButton").hide();
           $("#tabsListIds").val("");
           $("#tabsListVals").val("");
               var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+document.getElementById("roleid").value+'&REPORTID='+document.getElementById("REPORTID").value+'&tableList=true';
       frameObj.src=source;
   }
   
function cancelTabMeasure(){
    $("#measuresDialog").dialog('close');
}
function PreviewTable(){
   // alert("table")
//    var previewTable=document.getElementById("previewTable");
//    previewTable.style.display = '';
    //alert($("#action").val())
    $("#DisplayInsightdiv").html('<center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>');
   $.ajax({
        url: 'reportTemplateAction.do?templateParam=dispTable'+'&REPORTID='+document.getElementById("REPORTID").value+'&action='+document.getElementById("action").value,
        success: function(data){            
             $.ajax({
        url: 'reportTemplateAction.do?templateParam=generateInsightTable&reportId='+reportId,
        success: function(data){
            $("#DisplayInsightdiv").html(data);
            initCollapser("");
             if($("#timeType").val()=="PRG_STD"){
                                    $("#periodBasisTd").show();
                                    $("#rangebasisTd").hide();
                                    var durationval=$("#Duration").val();
                                    var compare=$("#compare").val();                                   
                                    $("#CBO_AS_OF_DATE").val($("#toDate").val());
                                    //$("#dateId").val($("#toDate").val());
                                    $('#CBO_PRG_PERIOD_TYPE option[value="'+durationval.toString()+'"]').attr('selected','selected');
                                    $('#CBO_PRG_COMPARE option[value="'+compare.toString()+'"]').attr('selected','selected');
                                    
                                }else if($("#timeType").val()=="PRG_DATE_RANGE"){
                                     $("#periodBasisTd").hide();
                                    $("#rangebasisTd").show();
                                    $("#CBO_AS_OF_DATE1").val($("#toDate").val());
                                    $("#CBO_AS_OF_DATE2").val($("#fromDate").val());
                                    $("#CBO_CMP_AS_OF_DATE1").val($("#compareToDate").val());
                                    $("#CBO_CMP_AS_OF_DATE2").val($("#compareFrmDate").val());
                                }
        } });
        }
     })
}
        function loadChildData(currRowId, dimId, childDivId, paramVals){
                        // alert(paramVals)
               // var reportId=reportId;
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                if (!isInitialized){
                     $.post('<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=getInsightTableData&viewByDim='+dimId+'&paramVals='+paramVals+'&reportId='+reportId,
                    function(data){
                       // alert(data)
                        $( "#"+childDivId+"prgBar").remove();
                            $("#"+childDivId).html(data);
                            currRow.attr("initialized","true");
                            initCollapser(childDivId);
                    });
                    }
                    
                 }
             function initCollapser(divId){
                  //alert(divId)
                if (divId == ""){
                    $(".tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                    $(".prgtable")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });

                }
                else{
                    $("#"+divId+" > .tablesorter")
                    .collapsible("td.collapsible", {
                        collapse: true
                    });
                }
            }
            function loadChildDrillData(currRowId, masterDimId, dimId, dimData, childDivId, paramVals,childDimId,expand){
              //alert(paramVals+"childDimId"+childDimId)
               // var reportId=reportId;
               $("#"+childDivId).html("");
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                 $( "#"+childDivId+"prgBar").progressbar({value: 37});
                 $( "#"+childDivId+"prgBar").show();
                 if (isInitialized && !expand)
                    return;
                if (expand){
                    var anchorImg = currRow.find("td.collapsible a");
                    if (anchorImg.hasClass("collapsed")){
                        anchorImg.removeClass("collapsed");
                        anchorImg.addClass("expanded");

                        var childRow = currRow.next();
                        if (childRow.hasClass("expand-child")){
                            childRow.find("td").show();
                        }
                    }
                }
                $.post("<%=request.getContextPath()%>"+"/reportViewer.do?reportBy=getViewbyDrillData&viewByDim="+dimId+"&dimValue="+dimData+"&paramVals="+paramVals+"&masterDimension="+masterDimId+"&reportId="+reportId+"&childDimId="+childDimId,
                    function(data){
                        $("#"+childDivId+"prgBar").hide();
                        $("#"+childDivId).html(data);
                       // currRow.attr("initialized","true");
                        initCollapser(childDivId);
                    });
                 }
               function insighthome(){
                     $("#InsightIconDiv").hide();
                     $("#DisplayInsightdiv").html("");
                     $("#InsightHomeDiv").show();
                     parent.$("#workBenchId").show();
                     parent.$("#tabs").addClass("ui-widget-content");
                    //  window.location.href = window.location.href;
                      document.forms.searchForm.action='reportTemplateAction.do?templateParam=workBenchPage';
                document.forms.searchForm.submit();
                 }

                 function saveInsight(){
                     $("#DisplayInsightdiv").html('<center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>');
                   $.post("<%=request.getContextPath()%>"+"/reportTemplateAction.do?templateParam=saveInsightWorkBench&REPORTID="+reportId+"&roleId="+roleId,
                        function(data){
                            alert("Insight saved successfully");
                            if(data!=null){
                                reportId=data;
                             $.ajax({url: 'reportTemplateAction.do?templateParam=generateInsightTable&reportId='+reportId,
                            success: function(data){
                                $("#DisplayInsightdiv").html(data);
                                initCollapser("");
                                 if($("#timeType").val()=="PRG_STD"){
                                    $("#periodBasisTd").show();
                                    $("#rangebasisTd").hide();
                                    var durationval=$("#Duration").val();
                                    var compare=$("#compare").val();                                   
                                    $("#CBO_AS_OF_DATE").val($("#toDate").val());
                                    $('#CBO_PRG_PERIOD_TYPE option[value="'+durationval.toString()+'"]').attr('selected','selected');
                                    $('#CBO_PRG_COMPARE option[value="'+compare.toString()+'"]').attr('selected','selected');
                                    
                                }else if($("#timeType").val()=="PRG_DATE_RANGE"){
                                     $("#periodBasisTd").hide();
                                    $("#rangebasisTd").show();
                                    $("#CBO_AS_OF_DATE1").val($("#toDate").val());
                                    $("#CBO_AS_OF_DATE2").val($("#fromDate").val());
                                    $("#CBO_CMP_AS_OF_DATE1").val($("#compareToDate").val());
                                    $("#CBO_CMP_AS_OF_DATE2").val($("#compareFrmDate").val());
                                }
                            } });
                            }
                            }); 
                        }              
                     
                 function openInsightPage(repName,repId,roleid){
                    //alert(repId)
                    //statusFlag="overwrite";
                    reportId=repId;
                    roleId=roleid;
                    reportName=repName;
                    $("#insightName").html(repName)
                    $("#REPORTID").val(repId); 
                    $("#roleid").val(roleid);
                    parent.$("#workBenchId").hide();
                    parent.$("#tabs").removeClass("ui-widget-content");
                    $("#InsightHomeDiv").hide();                    
                    $("#InsightIconDiv").show();
                    $("#DisplayInsightdiv").html("");
//                    $("#action").val("open");
                      $("#DisplayInsightdiv").html('<center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>');
   
                     $.ajax({
                            url: 'reportTemplateAction.do?templateParam=generateInsightTable&action=insightOpen&reportId='+repId+'&reportType=I',
                            success: function(data){
                                $("#DisplayInsightdiv").html(data);
                                initCollapser("");
                               //alert($("#timeType").val())
                                if($("#timeType").val()=="PRG_STD"){
                                    $("#periodBasisTd").show();
                                    $("#rangebasisTd").hide();
                                    var durationval=$("#Duration").val();
                                    var compare=$("#compare").val();   
                                    $("#CBO_AS_OF_DATE").val($("#toDate").val());
                                    $('#CBO_PRG_PERIOD_TYPE option[value="'+durationval.toString()+'"]').attr('selected','selected');
                                    $('#CBO_PRG_COMPARE option[value="'+compare.toString()+'"]').attr('selected','selected');
                                    
                                }else if($("#timeType").val()=="PRG_DATE_RANGE"){
                                     $("#periodBasisTd").hide();
                                    $("#rangebasisTd").show();
                                    $("#CBO_AS_OF_DATE1").val($("#toDate").val());
                                    $("#CBO_AS_OF_DATE2").val($("#fromDate").val());
                                    $("#CBO_CMP_AS_OF_DATE1").val($("#compareToDate").val());
                                    $("#CBO_CMP_AS_OF_DATE2").val($("#compareFrmDate").val());
                                }
                            } });
                    }
                    function changeMeasures(repId,roleid,ctxpath){
                        $("#action").val("measChange");
                       $("#measuresDialog").dialog('open');
                       var frameObj=parent.document.getElementById("dataDispmem");
                       var source="reportTemplateAction.do?templateParam=getMeasures&foldersIds="+roleid+'&REPORTID='+repId;
                       frameObj.src=source;
                    }
                    
                    
                    function goInsightPage(){
                         $("#DisplayInsightdiv").html('<center><img id="imgId" src="<%= request.getContextPath()%>/images/ajax1.gif" align="middle"  width="100px" height="100px"  style="top:100px; position:absolute" /></center>');                           
                         $.post('<%=request.getContextPath()%>'+'/reportTemplateAction.do?templateParam=generateInsightTable&action1=InsightTimeChange&reportId='+reportId,$("#InsightForm").serialize(),
                            function(data){
                                  $("#DisplayInsightdiv").html(data);
                                  initCollapser("");
                            });
                     }
            
                   function delteteInsight(repId){                    
                     var con=confirm("Do you want to delete this Insight");
                     if(con==true){
                    $.ajax({
                            type:'GET',
                    async:false,
                    cache:false,
                    timeout:30000,
                            url: "reportTemplateAction.do?templateParam=deleteInsightWorkBench&insightId="+repId,
                            success: function(data){                               
                                alert("Insight deleted successfully")
                                document.forms.searchForm.action='reportTemplateAction.do?templateParam=workBenchPage';
                                document.forms.searchForm.submit();
                                
                               }});
                     }
   
                    }
                function changeParameters(repId,roleid,ctxpath){
                    $("#action").val("ParamViewBychange")
                    var frameObj=document.getElementById("addmoreParamFrame");
                    var source=ctxpath+"/reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+roleid+"&REPORTID="+repId;
                    frameObj.src=source;
                    //    alert(source)
                    $("#timeSelectionId").hide();
                    $("#AddMoreParamsDiv").dialog('open');
                   }
   
                   function openInsightPageAssignment(ctxPath,userId,roleId,reportId,reportName){
                       $("#divassign").dialog({
                        autoOpen: false,
                        modal: true,
                        width: 600,
                        height: 450,
                        draggable: true,
                        resizable: true
                        });
                        $("#divassign").dialog('open')
                        var frameObj=document.getElementById("divassignframe");
                        var source =ctxPath+'/pbAssignReportsToUsers.jsp?SourcePage=Viewer&ReportType=I&ReportName='+reportName+"&UserFolderIds="+roleId+"&REPORTID="+reportId+"&USERID"+userId;
                        frameObj.src=source;
                   }
   
   </script>

    </body>
</html>
