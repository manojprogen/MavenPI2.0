
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,com.progen.report.pbDashboardCollection,com.progen.users.PrivilegeManager"%>

<%@page import="com.progen.charts.ProGenChartUtilities,prg.db.DataTracker,java.util.*,prg.db.Container"%>

<%--
    Document   : reportViewer
    Created on : Sep 12, 2009, 1:32:44 PM
    Author     : santhosh.kumar@progenbusiness.com
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="brdcrmb"  scope="session" class="com.progen.action.BreadCurmbBean"/>
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            ServletContext context = getServletContext();
            boolean isAxa = Boolean.parseBoolean(context.getInitParameter("isAxa"));
            String dashboardName = "";
            String DashboardId = "";
            Container container = null;
            String dashboardDesc = "";
            String reportDesc = "";
            HashMap map = new HashMap();
            HashMap DashboardHashMap = null;
            HashMap ParametersHashMap = null;
            pbDashboardCollection collect = null;
            HashMap GraphTypesHashMap = null;
            HashMap GraphClassesHashMap = null;
            String[] grpTypeskeys = new String[0];
            String[] grpClasseskeys = new String[0];
            String folderDetails = "";
            int USERID=Integer.parseInt(String.valueOf(session.getAttribute("USERID")));
            String edit="";
            String buildDashBoard = "";
            String ParamSectionDisplay = "";
            String rowIndex="";
            int rowNum=0;
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }
            if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                DashboardId = String.valueOf(request.getAttribute("DashboardId"));
                if (DashboardId == null || "null".equalsIgnoreCase(DashboardId))
                    DashboardId = String.valueOf(request.getAttribute("REPORTID"));
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                if (map.get(DashboardId) != null) {
                    container = (prg.db.Container) map.get(DashboardId);
                } else {
                    container = new prg.db.Container();
                }
                dashboardName = container.getDbrdName();
                dashboardDesc = container.getDbrdDesc();
                DashboardHashMap = container.getDashboardHashMap();

                GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");

                grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                grpClasseskeys = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);
                collect =  (pbDashboardCollection)container.getReportCollect();
                //folderDetails = collect.getbusinessroles(DashboardId);

                
                if (request.getAttribute("buildDashBoard") != null) {
                    buildDashBoard = String.valueOf(request.getAttribute("buildDashBoard"));
                }
                if (request.getAttribute("ParamSectionDisplay") != null) {
                    ParamSectionDisplay = String.valueOf(request.getAttribute("ParamSectionDisplay"));
                    session.setAttribute("ParamSectionDisplay", ParamSectionDisplay);
                }
                
                if(request.getAttribute("edit")!=null){
                    edit = String.valueOf(request.getAttribute("edit"));
                    request.setAttribute("edit", edit);
                }
                if(request.getAttribute("rowIndex") != null){
                    rowIndex = String.valueOf(request.getAttribute("rowIndex"));
                    rowNum=Integer.parseInt(rowIndex)+1;
                }
               
            }
             String contxtPath=request.getContextPath();
            %>

<html>
    <head>
        <title><bean:message key="ProGen.Title"/></title>
        <script src="<%=contxtPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contxtPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
       <!--<script type="text/javascript" src="<%=contxtPath%>/javascript/draggable/ui.core.js"></script>-->
        <!--<script type="text/javascript" src="<%=contxtPath%>/javascript/ui.sortable.js"></script>-->
        <script type="text/javascript" src="<%=contxtPath%>/javascript/dashboardDesign.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/dashboardDesignerViewer.js"></script>
        <script src="<%=contxtPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/treeview/demo.js"></script>
        <script src="<%=contxtPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/jquery.tablesorter.js"></script>
        <script src="javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxtPath%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type="text/javascript" language="JavaScript" src="<%=contxtPath%>/javascript/jquery.columnfilters.js"></script>
       <!-- <script type="text/javascript" src="<%=contxtPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/draggable/ui.droppable.js"></script>-->

<!--        <script type="text/javascript" src="<%=contxtPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/docs.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/effects.explode.js"></script>-->

        <!--<script type="text/javascript" src="<%=contxtPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/ui.dialog.js"></script>-->
        <link type="text/css" href="<%=contxtPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <link type="text/css" href="<%=contxtPath%>/stylesheets/pbPortletView.css" rel="stylesheet" />-->
        <link rel="stylesheet" href="<%=contxtPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contxtPath%>/stylesheets/treeviewstyle/screen.css" />
        <link href="<%=contxtPath%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="<%=contxtPath%>/stylesheets/tablesorterStyle.css" />
        <link rel="stylesheet" href="<%=contxtPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
         <link rel="stylesheet" href="<%=contxtPath%>/stylesheets/thems/<%=session.getAttribute("theme")%>/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=contxtPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contxtPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contxtPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
<!--        <link type="text/css" href="<%=contxtPath%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contxtPath%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <link href="stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="<%=contxtPath%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=contxtPath%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=contxtPath%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
<!--        <script type="text/javascript" src="<%=contxtPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>-->
         <script type="text/javascript" src="<%=contxtPath%>/javascript/pi.js"></script>
<!--        <script type="text/javascript" src="<%=contxtPath%>/javascript/map.js"></script>-->

        <script src="<%=contxtPath%>/javascript/lib/jquery/js/jquery.tablesorter.mod.js" type="text/javascript"></script>
        <script src="<%=contxtPath%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contxtPath%>/javascript/lib/jquery/js/jquery.tablesorter.innergrid.js"></script>





         <% if (PrivilegeManager.isModuleEnabledForUser( "MAP", USERID)) {%>
<!--         <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/map.js"></script>
         <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->
        <%}%>
        <style type="text/css" >
            .ui-progressbar-value{background-image:url(images/barchart.gif)}
            .ajaxboxstyle{background-color:#FFF;border:.1em solid #00F;height:50px;margin:0 .5em;overflow-x:hidden;overflow-y:auto;position:absolute;text-align:left;border-top:1px groove #848484;border-right:1px inset #999;border-bottom:1px inset #999;border-left:1px groove #848484;background-color:#f0f0f0;width:450px}
            .ajaxboxstyle1{position:absolute;background-color:#FFF;text-align:left;border:1px solid #000;border-top-width:1px;height:150px;width:230px;overflow:auto;overflow:hidden;margin:0 .5em;z-index:9999999}
            .black_overlay{display:none;position:absolute;top:0;left:0;width:110%;height:200%;background-color:#000;z-index:1001;-moz-opacity:.5;opacity:.5;overflow:auto}
            .white_content{display:none;position:absolute;top:30%;left:35%;width:50%;height:50%;padding:16px;border:10px solid silver;background-color:#fff;z-index:1002;-moz-border-radius-bottomleft:6px;-moz-border-radius-bottomright:6px;-moz-border-radius-topleft:6px;-moz-border-radius-topright:6px}
            table.grid .collapsible{padding:0 0 3px}
            .collapsible a.collapsed{display:block;width:15px;height:15px;background:url(images/addImg.gif) no-repeat 3px 3px;outline:0}
            .collapsible a.expanded{display:block;width:15px;height:15px;background:url(images/deleteImg.gif) no-repeat 3px 3px;outline:0}
        </style>

        <script type="text/javascript">
            function copyDate(eleId){
                //alert(eleId);
                document.getElementById(eleId+"1").value = document.getElementById(eleId).value;
                //alert(document.getElementById(eleId+"1").value);
                //alert(eleId+"1");
            }
            $(document).ready(function(){
                $("#breadCrumb").jBreadCrumb();

                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker1').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker2').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker3').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#datepicker4').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });

                if ($.browser.msie == true){

                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 820,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#favouriteParamsDialog").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 320,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphsDialog").dialog({
                        autoOpen: false,
                        height: 520,
                        width: 850,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });

                    $("#kpisGraphsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#kpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#tableColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphListDialog").dialog({
                        autoOpen: false,
                        height: 620,
                        width: 450,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });

                    $("#NewDbrdGraph").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                     $("#MapMeasures").dialog({
                autoOpen: false,
                height: 600,
                width: 850,
                modal: true,
                position: 'justify',
                resizable:false
            });


                    $("#DashletRename").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                     $("#createKPIDiv").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false,
                        title:'Create KPI Div'
                    });
                    $("#GraphRenameDialog").dialog({
                        autoOpen: false,
                            height:200,
                          width: 300,
                          position: 'justify',
                        modal: true
                      });
                      $("#editKpiGrDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                         $("#viewbydivid").dialog({
                        autoOpen: false,
                        height:400,
                        width:600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                     $("#TextkpiComment").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
                
                else{
                    $(".navigateDialog").dialog({
                        autoOpen: false,
                        height: 460,
                        width: 820,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#favouriteParamsDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 330,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphsDialog").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 850,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#kpisDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#kpisGraphsDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#kpiDrillDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphColsDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#tableColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#graphListDialog").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 450,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                    $("#NewDbrdGraph").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#DashletRename").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                     $("#createKPIDiv").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false,
                        title:'Create KPI Div'
                    });
                    $("#GraphRenameDialog").dialog({
                        autoOpen: false,
                            height:200,
                          width: 300,
                          position: 'justify',
                        modal: true
                      });
                      $("#editKpiGrDialog").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                         $("#viewbydivid").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                    $("#TextkpiComment").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }
            });
            var kCounter=0;
            var gCounter=0;
            var varVal=0;
            var rowCount = 0;
            var tdCount = 0;
            var grpCnt=0;
            var tdNum=0;
            var kt=0;
            var NewDashIds='';

            var tdarray = new Array();
            
            var dashId='';
            var graphid='';
            var dashletid ='';
            var refreportId='';
            var kpimasterId='';
            var dispseq='';
            var disptype='';
            var fromdesigner='';
            $(document).ready(function() {
                $("#dashbrdTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });

            $(document).ready(function(){
                 $("#regionDialog").dialog({
                        autoOpen: false,
                        height:250,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
         });
         $(document).ready(function(){
                 $("#zoomer").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 900,
                    position: 'justify',
                    modal: true,
                    resizable:false
                });
         });
         $(document).ready(function(){
                 $("#ZoomTarget").dialog({
                    autoOpen: false,
                    height: 600,
                    width: 900,
                    position: 'justify',
                    modal: true,
                    resizable:false
                });
         });
         $(document).ready(function(){
            $("#graphPropertiesDiv").dialog({
                bgiframe: true,
                autoOpen: false,
                height: 430,
                width: 800,
                modal: true
            });
        });
        $(document).ready(function(){
            $("#graphColsDialog").dialog({
                autoOpen: false,
                height: 480,
                width: 600,
                position: 'justify',
                modal: true,
                resizable:false
            });
            });
        $(document).ready(function(){
            $("#MapMeasures").dialog({
                autoOpen: false,
                height: 500,
                width: 750,
                modal: true,
                position: 'justify',
                resizable:false
            });
        });
         $(document).ready(function(){
               $("#DashletRename").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 350,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                     });
                      $(document).ready(function(){
                     $("#tableColsDialog").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 600,
                        position: 'justify',
                       modal: true,
                       resizable:false
                    });
                     });
  function contextMenucalCreateMenu(action, el, pos) {

                          switch (action) {
                              case "createCalender":
                                  {
                                      alert('am enter into function')
                                      break;
                                  }


                          }
                      }
//            function logout(){
//                document.forms.frmParameter.action="baseAction.do?param=logoutApplication";
//                document.forms.frmParameter.submit();
//            }
//             function goGlobe(){
//                    $(".navigateDialog").dialog('open');
//                }
//                function closeStart(){
//                    $(".navigateDialog").dialog('close');
//                }
//            function gohome(){
//                document.forms.frmParameter.action="baseAction.do?param=goHome";
//                document.forms.frmParameter.submit();
//            }
            function createDashboard()
            {
                UpdateParams();//added by k
                var edt=<%=edit%>
                document.forms.frmParameter.action="dashboardTemplateAction.do?templateParam2=saveDashboard&divCnt="+grpCnt+'&dashboardId='+document.getElementById("dbrdId").value+'&NewDashIds='+NewDashIds+'&edit='+edt;
                document.forms.frmParameter.submit();
            }
//            function changeInsightsAndCommentViewStatus(kpidashid){
//                var dbrdId = '<%=DashboardId%>';
//                var insightsStaus =document.getElementById("insights_"+kpidashid);
//                var commentsStatus = document.getElementById("comments_"+kpidashid);
//                var insigtSelect = "false";
//                var commentSelect = "false";
//                if(insightsStaus.checked){
//                    insigtSelect = "true";
//                }
//                if(commentsStatus.checked){
//                    commentSelect = "true";
//                }
//                var selectStatement;
//                if(insigtSelect=="false"){
//                        selectStatement = "Insight will not be available in Dashboard Viewer"
//                  }
//                if(commentSelect=="false"){
//                        selectStatement = "Comments will not be available in Dashboard Viewer"
//                  }
//            if(insigtSelect=="false"||commentSelect=="false"){
//              if(confirm(selectStatement)){
//               $.ajax({
//                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid,
//                  success: function(data){}
//                    });
//               }
//               else{
//
//               if(insigtSelect=="false")
//               $("#insights_"+kpidashid).attr('checked',true);
//               if(commentSelect=="false")
//               $("#comments_"+kpidashid).attr('checked',true);
//               }
//            }
//            else{
//                           $.ajax({
//                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid,
//                  success: function(data){}
//                    });
//            }
//
//
//            }

            function changeInsightStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var insightsStatus =document.getElementById("insights_"+kpidashid);
                 var commentsStatus = document.getElementById("comments_"+kpidashid);
                 var graphsStatus = document.getElementById("graphs_"+kpidashid);
                 var insigtSelect = "false";
                 var commentSelect = "false";
                 var graphSelect = "false";
                if(insightsStatus.checked){
                    insigtSelect = "true";
                }
                if(commentsStatus.checked){
                   commentSelect = "true";
                }
                if(graphsStatus.checked){
                   graphSelect = "true";
                }
                if(insigtSelect=="false"){
                    var selectStatement = "Insight will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#insights_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
            }
            }
           function changeCommentStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var insightsStatus =document.getElementById("insights_"+kpidashid);
                 var commentsStatus = document.getElementById("comments_"+kpidashid);
                 var graphsStatus = document.getElementById("graphs_"+kpidashid);
                 var insigtSelect = "false";
                 var commentSelect = "false";
                 var graphSelect =  "false";
                if(insightsStatus.checked){
                    insigtSelect = "true";
                }
                if(commentsStatus.checked){
                   commentSelect = "true";
                }
                if(graphsStatus.checked){
                   commentSelect = "true";
                }
                if(commentSelect=="false"){
                    var selectStatement = "Comment will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#comments_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
            }
            }

           function changeGraphStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var insightsStatus =document.getElementById("insights_"+kpidashid);
                 var commentsStatus = document.getElementById("comments_"+kpidashid);
                 var graphsStatus = document.getElementById("graphs_"+kpidashid);
                 var insigtSelect = "false";
                 var commentSelect = "false";
                 var graphSelect = "false";
                if(insightsStatus.checked){
                    insigtSelect = "true";
                }
                if(commentsStatus.checked){
                   commentSelect = "true";
                }
                if(graphsStatus.checked){
                   graphSelect = "true";
                }
                if(graphSelect=="false"){
                    var selectStatement = "Graph will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#graphs_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setInsightAndCommentViewStatus&dashboardId='+dbrdId+'&insigtSelect='+insigtSelect+'&commentSelect='+commentSelect+'&dashletId='+kpidashid+'&graphSelect='+graphSelect,
                  success: function(data){}
                    });
            }
            }
            //for changing the status of MTD
            function changeMTDStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var mtdStatus =document.getElementById("MTD_"+kpidashid);
                 var qtdStatus =document.getElementById("QTD_"+kpidashid); 
                 var ytdStatus =document.getElementById("YTD_"+kpidashid); 
                 var currentStatus=document.getElementById("CURRENT_"+kpidashid);
                 var mtdSelect = "false";
                 var qtdSelect = "false";
                 var ytdSelect = "false";
                 var currentSelect = "false";
                 if(mtdStatus.checked){
                     mtdSelect = "true";
                 }
                 if(qtdStatus.checked){
                     qtdSelect = "true";
                 }
                 if(ytdStatus.checked){
                     ytdSelect = "true";
                 }
                 if(currentStatus.checked){
                     currentSelect = "true";
                 }
                 if(mtdSelect=="false"){
                    var selectStatement = "MTD will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#MTD_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
            }
            }
            //for changing the status of QTD
            function changeQTDStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var mtdStatus =document.getElementById("MTD_"+kpidashid);
                 var qtdStatus =document.getElementById("QTD_"+kpidashid); 
                 var ytdStatus =document.getElementById("YTD_"+kpidashid);
                 var currentStatus=document.getElementById("CURRENT_"+kpidashid);
                 var mtdSelect = "false";
                 var qtdSelect = "false";
                 var ytdSelect = "false";
                 var currentSelect = "false";
                 if(mtdStatus.checked){
                     mtdSelect = "true";
                 }
                 if(qtdStatus.checked){
                     qtdSelect = "true";
                 }
                 if(ytdStatus.checked){
                     ytdSelect = "true";
                 }
                 if(currentStatus.checked){
                     currentSelect = "true";
                 }
                 if(qtdSelect=="false"){
                    var selectStatement = "QTD will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#QTD_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
            }
            }

           //for changing the status of the YTD
            function changeYTDStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var mtdStatus =document.getElementById("MTD_"+kpidashid);
                 var qtdStatus =document.getElementById("QTD_"+kpidashid); 
                 var ytdStatus =document.getElementById("YTD_"+kpidashid);
                 var currentStatus=document.getElementById("CURRENT_"+kpidashid);
                 var mtdSelect = "false";
                 var qtdSelect = "false";
                 var ytdSelect = "false";
                 var currentSelect = "false";
                 if(mtdStatus.checked){
                     mtdSelect = "true";
                 }
                 if(qtdStatus.checked){
                     qtdSelect = "true";
                 }
                 if(ytdStatus.checked){
                     ytdSelect = "true";
                 }
                 if(currentStatus.checked){
                     currentSelect = "true";
                 }
                 if(ytdSelect=="false"){
                    var selectStatement = "YTD will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#YTD_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
            }
            }           
           //for changing the status of the current value
             function changeCurrentStatus(kpidashid){
                 var dbrdId = '<%=DashboardId%>';
                 var mtdStatus =document.getElementById("MTD_"+kpidashid);
                 var qtdStatus =document.getElementById("QTD_"+kpidashid); 
                 var ytdStatus =document.getElementById("YTD_"+kpidashid);
                 var currentStatus=document.getElementById("CURRENT_"+kpidashid);
                 var mtdSelect = "false";
                 var qtdSelect = "false";
                 var ytdSelect = "false";
                 var currentSelect = "false";
                 if(mtdStatus.checked){
                     mtdSelect = "true";
                 }
                 if(qtdStatus.checked){
                     qtdSelect = "true";
                 }
                 if(ytdStatus.checked){
                     ytdSelect = "true";
                 }
                 if(currentStatus.checked){
                     currentSelect = "true";
                 }
                 if(currentSelect=="false"){
                    var selectStatement = "CurrentDay will not be available in Dashboard Viewer";
                     if(confirm(selectStatement)){
                     $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
                     }
                    else{

               $("#CURRENT_"+kpidashid).attr('checked',true);

                }
            }else{
                                 $.ajax({
                  url: 'dashboardTemplateViewerAction.do?templateParam2=setMtdQtdYtdStatus&dashboardId='+dbrdId+'&mtdSelect='+mtdSelect+'&qtdSelect='+qtdSelect+'&dashletId='+kpidashid+'&ytdSelect='+ytdSelect+'&currentSelect='+currentSelect,
                  success: function(data){}
                    });
            }
            }    
            function cancelDashboard(){
                document.forms.frmParameter.action="baseAction.do?param=goHome";
                document.forms.frmParameter.submit();
            }

            function displaylink(){
                $("#linkTable").toggle(500);
            }
            function dispParameters(){
                $("#tabParameters").toggle(500);
            }
            function dispPortlet(){
                $("#tabPortlet").toggle(500);
            }
            function Close(rowIndex,colIndex){

                ////////alert('tdnum is:: '+tdNum)
                //////('colindex is:: '+colIndex)


                for(var i=colIndex;i<tdNum;i++){
                    if((document.getElementById("portletTd"+(i+1)))==null){
                        document.getElementById("portletTd"+(i)).innerHTML='';



                    }else{
                        document.getElementById("portletTd"+i).innerHTML='';
                        document.getElementById("portletTd"+i).innerHTML=document.getElementById("portletTd"+(i+1)).innerHTML;


                    }
                }
            }
            <%--function closePortlet(x,y){
               //////alert('old hi')
            }
            --%>
                function dshbdNameDialog(){
                    if ($.browser.msie == true){
                        $("#chngDashdNameDialog").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 300,
                            width: 300,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });
                    }
                    else{
                        $("#chngDashdNameDialog").dialog({
                            //bgiframe: true,
                            autoOpen: false,
                            height: 200,
                            width: 300,
                            position: 'justify',
                            modal: true,
                            resizable:false
                        });
                    }
                }

                  function addKpiGraph(){

//                      var KPIType="KPIGraph";
                      kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("tablePortlet");
                    var table = document.getElementById("tablePortlet");
                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){//tdNum is golbal variable increases bases on created no of charts.
                        tdNum=0;
                    }

                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell1 = newRow.insertCell(0);
                        cell1.id="portletTd_"+tdNum;
                        cell1.style.paddingLeft="30px";
                        cell1.colspan= "1";

                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet';
                        inDiv.style.width='500px';
                        inDiv.style.height='325px';
                        inDiv.id='drag'

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Graph</td><td align="right"> <a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+',\'dispGrp'+grpCnt+'\')"></a></td></tr></table>'
                        alert("adding")
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='290px';
                        cntDiv.style.width='99%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell1.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell2 = rowId.insertCell(1);
                        cell2.id="portletTd_"+tdNum;
                        cell2.style.paddingLeft="30px";

                        var inDiv = document.createElement('div');
                        inDiv.className = 'portlet';
                        inDiv.style.width='500px';
                        inDiv.style.height='325px';
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header ';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Graph</td><td align="right"> <a href="javascript:void(0)" onclick="showKpiGraphs(dispGrp'+grpCnt+')">Add KPI Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+',\'dispGrp'+grpCnt+'\')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='290px';
                        cntDiv.style.width='99%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        inDiv.appendChild(txtDiv);
                        inDiv.appendChild(cntDiv);
                        cell2.appendChild(inDiv);
                        tdarray.push("portletTd_"+tdNum);
                    }

                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }

                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                    $(".dragPT").draggable();
                }



                function addKpiwithTarget(){
                    if(((tdNum)%2)!=0 )
                    {
                        rowCount++;
                    }

                    var KPIType="Target";
                    kt=0;
                    grpCnt++;
                    var tableObj = document.getElementById("tablePortlet");
                    var table = document.getElementById("tablePortlet");
                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){
                        tdNum=0;
                    }
                    var newRow = tableObj.insertRow(0);
                    newRow.id = "rowPortlet_"+rowCount;
                    var cell1 = newRow.insertCell(0);
                    cell1.id="portletTd_"+tdNum;
                    cell1.colSpan=2;
                    cell1.style.paddingLeft="30px";
                    var inDiv = document.createElement('div');
                    inDiv.className = 'portlet';
                    inDiv.style.width='1080px';
                    inDiv.style.minHeight='200px';
                    inDiv.id="kpidivid"+rowCount;
                    var txtDiv = document.createElement('div');
                    txtDiv.className = 'portlet-header';
                    txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td align="right"  > <a href="javascript:void(0)" onclick="showKpis(dispGrp'+grpCnt+',\''+KPIType+'\')">Add KPI</a> <a class="ui-icon ui-icon-closethick" href="javascript:void(0)"  onclick="closeTargetKpi('+rowCount+','+tdNum+',\'dispGrp'+grpCnt+'\')" ></a> </td></tr></table>'
                    var cntDiv = document.createElement('div');
                    cntDiv.className = 'portlet-content1';
                    cntDiv.id='dispGrp'+grpCnt;
                    cntDiv.innerHTML = '';
                    inDiv.appendChild(txtDiv);
                    inDiv.appendChild(cntDiv);
                    cell1.appendChild(inDiv);

                    tdarray.push("portletTd_"+tdNum);
                    if(((tdNum)%2)!=0 )
                    {
                        tdNum=tdNum+1;
                    }
                    else
                    {
                        tdNum=tdNum+2;
                    }
                    rowCount++;

                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }

////var mapAdded = false;
//var mapDiv = "";
//
//                function setMapDiv(div){
//                    mapDiv = div;
//                    mapAdded = true;
//                }

//                function addRegion()
//                {
//                    $("#noOfRows").val("");
//                    $("#noOfColumns").val("");
//                    $("#regionDialog").dialog('open');
//                }
                function addMap(){

                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("tablePortlet");
                    var table = document.getElementById("tablePortlet");
                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){
                        tdNum=0;
                    }

                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        cell.style.paddingLeft="30px";

                        var input1 = document.createElement("a");
                        input1.id='inp'+grpCnt
                        input1.name='inp'+grpCnt

                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        //inDivG.style.width='500px';
                        inDivG.style.height='625px';
                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        txtDivG.innerHTML = '<table width="100%" style="height:10px" ><tr><td id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Map Region</td><td align="right">&nbsp;&nbsp;<a href="javascript:void(0)" onclick="">Create Map</a></td><td id="delPortlet" width="25px" align="right"><a href="javascript:void(0)"  class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''
                        inDivG.appendChild(txtDivG);
                        inDivG.appendChild(cntDivG);
                        cell.appendChild(inDivG);
                        cell.appendChild(input1);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        cell.style.paddingLeft="30px";

                        var input1 = document.createElement("a");
                        input1.id='inp'+grpCnt
                        input1.name='inp'+grpCnt

                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        //inDivG.style.width='500px';
                        inDivG.style.height='625px';
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Map Region</td><td align="right"> <a href="javascript:void(0)" onclick="">Create Map</a></td><td id="delPortlet" width="25px" align="right"><a href="javascript:void(0)" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);
                    }

                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }



                function addStdKpi(tdId){
                    var KPIType="Standard";
                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("tablePortlet");
                    var table = document.getElementById("tablePortlet");
                    var rowCount1 = table.rows.length;
//                    if(tdNum==-1){//tdNum is golbal variable increases bases on created no of charts.
//                        tdNum=0;
//                    }
//                    alert("tdNum :: "+tdNum);
//                    if(parseInt(tdNum%2)==0)
//                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell1 = newRow.insertCell(0);
                        cell1.id="portletTd_"+tdNum;
                        cell1.style.paddingLeft="30px";
                        cell1.colspan= "1";

                        var div = document.getElementById("dashlet"+tdId);

//                        var inDiv = document.createElement('div');
//                        inDiv.className = 'portlet';
//                        inDiv.style.width='500px';
//                        inDiv.style.height='325px';
//                        inDiv.id='drag'

                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td align="right"> <a href="javascript:void(0)" onclick="javascript:showKpis(dispGrp'+grpCnt+',\''+KPIType+'\')">Add KPI</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+',\'dispGrp'+grpCnt+'\')"></a></td></tr></table>'

                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.style.height='290px';
                        cntDiv.style.width='99%';
                        cntDiv.style.overflow='auto';

                        cntDiv.innerHTML = '';
                        div.appendChild(txtDiv);
                        div.appendChild(cntDiv);
                        cell1.appendChild(div);
                        tdarray.push("portletTd_"+tdNum);
//                    }
//                    else if(parseInt((tdNum%2))!=0)
//                    {
//                        var rowId = document.getElementById("rowPortlet_"+rowCount);
//                        var cell2 = rowId.insertCell(1);
//                        cell2.id="portletTd_"+tdNum;
//                        cell2.style.paddingLeft="30px";
//
//                        var inDiv = document.createElement('div');
//                        inDiv.className = 'portlet';
//                        inDiv.style.width='500px';
//                        inDiv.style.height='325px';
//                        var txtDiv = document.createElement('div');
//                        txtDiv.className = 'portlet-header ';
//                        txtDiv.innerHTML = '<table id="addstd" width="100%" style="height:10px"><tr><td style="color:#369;font-weight:bold">KPI Region</td><td align="right"> <a href="javascript:void(0)" onclick="showKpis(dispGrp'+grpCnt+',\''+KPIType+'\')">Add KPI</a></td><td id="delPortlet" width="25px" align="right"><a href="#" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+',\'dispGrp'+grpCnt+'\')"></a></td></tr></table>'
//                        var cntDiv = document.createElement('div');
//                        cntDiv.className = 'portlet-content';
//                        cntDiv.id='dispGrp'+grpCnt;
//                        cntDiv.style.height='290px';
//                        cntDiv.style.width='99%';
//                        cntDiv.style.overflow='auto';
//
//                        cntDiv.innerHTML = '';
//                        inDiv.appendChild(txtDiv);
//                        inDiv.appendChild(cntDiv);
//                        cell2.appendChild(inDiv);
//                        tdarray.push("portletTd_"+tdNum);
//                    }
//
//                    if(tdNum%2 ==0)
//                    {
//                        tdNum++;
//                        tdCount++;
//                    }
//                    else
//                    {
//                        tdNum++;
//                        rowCount++;
//                    }

                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                    $(".dragPT").draggable();
                }

                function addGraph(){
                    kt=1;
                    grpCnt++;
                    var tableObj = document.getElementById("tablePortlet");
                    var table = document.getElementById("tablePortlet");
                    var rowCount1 = table.rows.length;
                    if(tdNum==-1){
                        tdNum=0;
                    }

                    if(parseInt(tdNum%2)==0)
                    {
                        var newRow = tableObj.insertRow(rowCount);
                        newRow.id = "rowPortlet_"+rowCount;
                        var cell = newRow.insertCell(0);
                        cell.id="portletTd_"+tdNum;
                        cell.style.paddingLeft="30px";

                        var input1 = document.createElement("a");
                        input1.id='inp'+grpCnt
                        input1.name='inp'+grpCnt

                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='500px';
                        inDivG.style.height='325px';
                        var txtDivG = document.createElement('div');
                        txtDivG.className = 'portlet-header ';
                        txtDivG.innerHTML = '<table width="100%" style="height:10px" ><tr><td id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="javascript:void(0)"  class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDivG = document.createElement('div');
                        cntDivG.className = 'portlet-content';
                        cntDivG.id='dispGrp'+grpCnt;
                        cntDivG.innerHTML = ''
                        inDivG.appendChild(txtDivG);
                        inDivG.appendChild(cntDivG);
                        cell.appendChild(inDivG);
                        cell.appendChild(input1);
                        newRow.appendChild(cell);
                        tdarray.push("portletTd_"+tdNum);
                    }
                    else if(parseInt((tdNum%2))!=0)
                    {
                        var rowId = document.getElementById("rowPortlet_"+rowCount);
                        var cell = rowId.insertCell(1);
                        cell.id="portletTd_"+tdNum;
                        cell.style.paddingLeft="30px";

                        var input1 = document.createElement("a");
                        input1.id='inp'+grpCnt
                        input1.name='inp'+grpCnt

                        var inDivG = document.createElement('div');
                        inDivG.className = 'portlet';
                        inDivG.style.width='500px';
                        inDivG.style.height='325px';
                        var txtDiv = document.createElement('div');
                        txtDiv.className = 'portlet-header';
                        txtDiv.innerHTML = '<table width="100%" style="height:10px"><tr><td id="innertd'+grpCnt+'" style="color:#369;font-weight:bold">Graph Region</td><td align="right"> <a href="javascript:void(0)" onclick="showGraphs(dispGrp'+grpCnt+')">Add / Edit Graph</a>&nbsp;&nbsp;<a href="javascript:void(0)" onclick="createDbrdGraphs(dispGrp'+grpCnt+')">Create Graph</a></td><td id="delPortlet" width="25px" align="right"><a href="javascript:void(0)" class="ui-icon ui-icon-closethick" onclick="closePortlet('+tdNum+','+rowCount+')"></a></td></tr></table>'
                        var cntDiv = document.createElement('div');
                        cntDiv.className = 'portlet-content';
                        cntDiv.id='dispGrp'+grpCnt;
                        cntDiv.innerHTML = ''
                        inDivG.appendChild(txtDiv);
                        inDivG.appendChild(cntDiv);
                        cell.appendChild(inDivG);
                        tdarray.push("portletTd_"+tdNum);
                    }

                    if(tdNum%2 ==0)
                    {
                        tdNum++;
                        tdCount++;
                    }
                    else
                    {
                        tdNum++;
                        rowCount++;
                    }
                    $(".column").sortable({
                        connectWith: '.column'
                    });
                    $(".portlet").addClass("ui-widget ui-widget-content ui-helper-clearfix ui-corner-all")
                    .find(".portlet-header")
                    .addClass("ui-widget-header ui-corner-all")
                    .end()
                    .find(".portlet-content");
                    $(".portlet-header .ui-icon").click(function() {
                        $(this).toggleClass();
                        $(this).parents(".portlet:first").find(".portlet-content").toggle();
                    });
                    $(".column").disableSelection();
                }

                function closePortlet(x,y,dashletId){
                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){
                        var tdOBJ=document.getElementById('portletTd_'+x);
                        var imgObjs = tdOBJ.getElementsByTagName("div")
                        NewDashIds=NewDashIds+imgObjs[2].id+",";
                        var parTrObject=tdOBJ.parentNode;
                        parTrObject.removeChild(tdOBJ);
                        if((tdNum-1)==x)
                        {
                            if(x%2 ==0 )
                            {
                                rowCount=y;
                                tdNum=x;
                            }
                            else
                            {
                                rowCount=y;
                                tdNum=x;
                            }
                        }

                        var temparray= new Array();
                        for (var i = 0; i < tdarray.length; i++){
                            if (tdarray[i] != 'portletTd_'+x )
                                temparray.push(tdarray[i]);
                        }
                        tdarray=temparray;
                        alterDashBoardTable();
                        var dbrdId = '<%=DashboardId%>';

                         $.ajax({
                            url: 'dashboardTemplateViewerAction.do?templateParam2=deleteDbrdGraphs&dashboardId='+dbrdId+'&dashletId='+dashletId,
                            success: function(data){}
                        });
                    }
                }
                function isNumberKey(evt)
                {
                    var charCode = (evt.which) ? evt.which : event.keyCode
                    // if(charCode==44)
                    //  return true;
                    if (charCode > 31 && (charCode < 48 || charCode > 57))
                        return false;

                    return true;
                }
                function closeTargetKpi(ids,tdno,dashletId){
                    var confirmText= confirm("Are you sure you want to delete");
                    if(confirmText==true){
                        var divobj=document.getElementById('kpidivid'+ids);

                        var imgObjs = divobj.getElementsByTagName("div")
                        NewDashIds=NewDashIds+imgObjs[1].id+",";

                        var temp1array= new Array();
                        for (var i = 0; i < tdarray.length; i++){
                            if (tdarray[i] != "portletTd_"+tdno )
                                temp1array.push(tdarray[i])
                        }
                        tdarray=temp1array;
                        var parobj=divobj.parentNode;
                        parobj.removeChild(divobj);
                        if((rowCount-1)==ids)
                        {
                            rowCount--;
                        }
                        alterDashBoardTable();
                        var dbrdId = '<%=DashboardId%>';

                         $.ajax({
                            url: 'dashboardTemplateViewerAction.do?templateParam2=deleteDbrdGraphs&dashboardId='+dbrdId+'&dashletId='+dashletId,
                            success: function(data){}
                        });
                    }
                }

                function alterDashBoardTable()
                {
                    var table = document.getElementById("tablePortlet");
                    var newtable = document.getElementById("temptablePortlet");
                    var rowCount1 = table.rows.length;
                    var count=0;
                    var newRow="";
                    var cell="";
                    var rcount=-1;
                    for(var i=0;i< tdarray.length;i++)
                    {
                        var a= document.getElementById(tdarray[i]).innerHTML;
                        var imgObjs = document.getElementById(tdarray[i]).getElementsByTagName("div")
                        var width1= imgObjs[0].style.width;

                        if(width1=="500px")
                        {
                            if(count%2==0)
                            {
                                rcount++;
                                newRow = newtable.insertRow(rcount);
                                newRow.id = "rowPortlet_"+rcount;
                                cell = newRow.insertCell(0);
                                cell.id=tdarray[i];
                                cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                                cell.style.width='auto';
                                cell.style.paddingLeft='30px';
                                cell.setAttribute("colspan", "1");
                                newRow.appendChild(cell);
                                count++;
                            }
                            else
                            {
                                cell = newRow.insertCell(1);
                                cell.id=tdarray[i];
                                cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                                cell.style.width='auto';
                                cell.style.paddingLeft='30px';
                                cell.setAttribute("colspan","1");
                                newRow.appendChild(cell);
                                count++;
                            }
                        }
                        else
                        {
                            rcount++;
                            newRow = newtable.insertRow(rcount);
                            newRow.id = "rowPortlet_"+rcount;
                            cell = newRow.insertCell(0);
                            cell.id=tdarray[i];
                            cell.setAttribute("colspan", "2");
                            cell.style.paddingLeft='30px';
                            cell.innerHTML= document.getElementById(tdarray[i]).innerHTML;
                            newRow.appendChild(cell);
                            if(count%2==0)
                                count=count+2;
                            else
                                count=count+3;
                        }
                    }

                    document.getElementById("tablePortlet").innerHTML=document.getElementById("temptablePortlet").innerHTML;
                    document.getElementById("temptablePortlet").innerHTML='';
                  if((count)%2==0)
                  {
                     if(tdNum%2 !=0)
                     {
                         tdNum=tdNum+1;}
                  }
                  else
                  {
                    if(tdNum%2 ==0){
                        tdNum=tdNum+1;
                    }
                  }
                  if(count%2==0)
                    rcount++;
                    rowCount=rcount;
                }

                function createDbrdGraphs(divId){
                    document.getElementById('newgraphid').value=divId;
                    $("#NewDbrdGraphName").val("");
                    $("#NewDbrdGraph").dialog('open');
                }

               function createKpiGrph(divId){
                    document.getElementById('newgraphid').value=divId.id;
                    $("#NewDbrdGraph").dialog('open');
                }

                function viewDashboardG(path){
                    document.forms.frmParameter.action=path;
                    document.forms.frmParameter.submit();
                }
                function viewReportG(path){
                    document.forms.frmParameter.action=path;
                    document.forms.frmParameter.submit();
                }

                function goPaths(path){
                    parent.closeStart();
                    document.forms.frmParameter.action=path;
                    document.forms.frmParameter.submit();
                }
                function formatStr(EL,maxchars){
                    strbuff=EL.innerHTML;
                    newstr='';
                    startI = 0;
                    max=maxchars;
                    str='';
                    subarr=new Array(parseInt(strbuff.length/max+1));
                    for (i=0;i<subarr.length;i++)
                    {
                        subarr[i]=strbuff.substr(startI,max);
                        startI+=max;
                    }
                    for (i=0;i<subarr.length-1;i++)
                    {
                        newstr+=subarr[i]+'<br/>';

                    }
                    newstr+=subarr[subarr.length-1];
                    if(subarr.length==1){
                        EL.innerHTML=EL.innerHTML;
                    }else{
                        EL.innerHTML=newstr;
                    }
                }
                function formatStr(EL,maxchars){
                    strbuff=EL.innerHTML;
                    newstr='';
                    startI = 0;
                    max=maxchars;
                    str='';
                    subarr=new Array(parseInt(strbuff.length/max+1));
                    for (i=0;i<subarr.length;i++)
                    {
                        subarr[i]=strbuff.substr(startI,max);
                        startI+=max;
                    }
                    for (i=0;i<subarr.length-1;i++)
                    {
                        newstr+=subarr[i]+'<br/>';
                    }
                    newstr+=subarr[subarr.length-1];
                    if(subarr.length==1){
                        EL.innerHTML=EL.innerHTML;
                    }else{
                        EL.innerHTML=newstr;
                    }
                }

                function dashNmdone(){;
                    dshbdNameDialog();
                    document.getElementById("dashName").value= document.getElementById("dashbdHd").value;
                    document.getElementById("dashdesc").value=document.getElementById("dashdescHd").value;
                    $("#chngDashdNameDialog").dialog('open');

                }
                function checkDashdNm(){
                    var dashdId=document.getElementById("dbrdId").value;

                var  gvnDashNm=document.getElementById("dashName").value;
                //gvnDashNm=gvnDashNm.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
                var  gvnDashDesc= document.getElementById("dashdesc").value;
               // gvnDashDesc=gvnDashDesc.replace('&', '^').replace('+','~').replace('#', '`').replace('%','_');
               var encodedgvnDashNm=encodeURIComponent(gvnDashNm);
               var encodedgvnDashDesc=encodeURIComponent(gvnDashDesc);
                $.ajax({
                    url: "dashboardTemplateAction.do?templateParam2=chckDashbdNameBfrUpdate&gvnDashdNm="+encodedgvnDashNm+"&dashdID="+dashdId+'&gvnDashdDesc='+encodedgvnDashDesc,
                    success: function(data){
                        if(data==1){
                            document.getElementById("dashbdHd").value=gvnDashNm;
                            document.getElementById("dashdescHd").value=gvnDashDesc;
                            document.getElementById('dashMsg').innerHTML ='';
                            ChangeDashDName(gvnDashNm,gvnDashDesc);
                        }
                        else{
                            document.getElementById('dashMsg').innerHTML = "Dashboard Name already exists";

                        }
                    }
                    });
                }
                function ChangeDashDName(gvnDashNm,gvnDashDesc){
                    var dashdId=document.getElementById("dbrdId").value;
                    $.ajax({
                        url: "dashboardTemplateAction.do?templateParam2=ChangeDashDName&DashdName="+gvnDashNm+"&dashDId="+dashdId+'&DashDdesc='+gvnDashDesc,
                        success: function(data){
                        }
                    });
                    gvnDashNm=gvnDashNm.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                    document.getElementById("reportName").innerHTML=gvnDashNm;
                    $("#chngDashdNameDialog").dialog('close');

                 var dashdId=document.getElementById("dbrdId").value;
                 var encodedgvnDashNm=encodeURIComponent(gvnDashNm);
                  var encodedgvnDashDesc=encodeURIComponent(gvnDashDesc);
                $.ajax({
                    url: "dashboardTemplateAction.do?templateParam2=ChangeDashDName&DashdName="+encodedgvnDashNm+"&dashDId="+dashdId+'&DashDdesc='+encodedgvnDashDesc,
                    success: function(data){
                    }
                });
                //gvnDashNm=gvnDashNm.replace('^', '&').replace('~','+').replace('`', '#').replace('_','%');
                document.getElementById("reportName").innerHTML=gvnDashNm;
                $("#chngDashdNameDialog").dialog('close');

                }

                function loadChildData(currRowId, measId, childDivId, dashletId,groupId,expand)
            {
//                alert('load child data method'+groupId);
                var dispType = $("#viewType").val();
                var currRow = $("#"+currRowId);
                var dbrdId=<%=DashboardId%>;
                var isInitialized = currRow.attr("initialized");
                var childRow = currRow.next();
                
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
//                  initCollapser("");
               
                if (expand){
                    
//                    alert('load child data in expand '+expand)
                    var anchorImg = currRow.find("td.collapsible a");
                    if (anchorImg.hasClass("collapsed")){
                        anchorImg.removeClass("collapsed");
                        anchorImg.addClass("expanded");



                        if (childRow.hasClass("expand-child")){
                            childRow.find("td").show();
                        }
                    }

                }
                if (!isInitialized){
//                    alert('calling action method');

                    $.ajax({
                        url: "dashboardTemplateAction.do?templateParam2=groupMeasureInitialView&meassId="+measId+"&dbrdId="+dbrdId+"&dashletId="+dashletId+"&groupId="+groupId+"&viewType="+dispType,
                        success: function(data){
                            $( "#"+childDivId+"prgBar").remove();
                            $("#"+childDivId).html(data);
                            currRow.attr("initialized","true");
                            initCollapser(childDivId);
                        }
                    });

                }
            } 

            function initCollapser(divId){
//             alert(divId);
                if (divId == ""){
                    $(".tablesorter")
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
            function loadChildDrillData(currRowId, dashletId,dbrdId, meassId, childDivId, nextLevel,groupId, expand){
//                alert('nextLevel:'+nextLevel);
//                alert('dbrdId'+dbrdId);

                var dispType = $("#viewType").val();
                var currRow = $("#"+currRowId);
                var isInitialized = currRow.attr("initialized");
                $( "#"+childDivId+"prgBar").progressbar({value: 37});
                if (isInitialized && !expand)
//                    alert('load childdrill data method in '+isInitialized+''+expand);
                    return;
                if (expand){
//                    alert('load childdrill data method in '+isInitialized+''+expand);
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
                $.ajax({
                    url: "dashboardTemplateAction.do?templateParam2=groupMeasureInitialView&meassId="+meassId+"&viewType="+dispType+"&dbrdId="+dbrdId+"&nextLevel="+nextLevel+"&dashletId="+dashletId+"&groupId="+groupId,
                    success: function(data){
                        $( "#"+childDivId+"prgBar").remove();
                        $("#"+childDivId).html(data);
                        currRow.attr("initialized","true");
                        initCollapser(childDivId);
                    }
                });
            }


        </script>


        <script type="text/javascript">
            var count=0;
            function shwDate(){

                $('#datepicker').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                
            }

            function resetCurrDashbd(){
                var dashdId=document.getElementById("dbrdId").value;

                $.ajax({
                    url:"dashboardTemplateAction.do?templateParam2=restCurrDashBd&dashbdId="+dashdId,
                    success:function(data){
                        if(data!=null){
                            window.location.reload(true);
                        }
                    }
                })
            }

             function enableMap(){
                var dashboardId=document.getElementById("dbrdId").value;
                enableMaps(dashboardId);
                //                var flag=true;
//
//                if($("#enableMapId").val()=="Disable Map") {
//                    $("#enableMapId").attr("mapEnabled","false");
//                    flag=$("#enableMapId").attr("mapEnabled").valueOf();
//                    $("#enableMapId").val("Enable Map");
//                    alert("Geo Map has been disabled for this Dashboard");
//                }
//                else{
//                    $("#enableMapId").attr("mapEnabled","true");
//                    flag=$("#enableMapId").attr("mapEnabled").valueOf();
//                    $("#enableMapId").val("Disable Map")
//                    alert("Geo Map has been enabled for this Dashboard");
//                }
//
//
//                $.ajax({
//                    url:"dashboardTemplateAction.do?templateParam2=enableMap&dashboardId="+dashboardId+"&mapFlag="+flag,
//                    success:function(data){
//                        if(data!=null){
//                        }
//                    }
//                });
            }


            var tdId=0;
            var tableId=0;
            var rowSp=1;
            var colSp=1;
            var row=0;
            var col=0;
            var prevRow=0;
<%            if(edit.equals("true")){
    %>
                    prevRow=<%=rowNum%>
<%             }
    %>
            function createRegion()
            {
//                alert("createRegion")
//                var KPIType="Standard";
                var dbrdId = '<%=DashboardId%>';
                var dashId=tdId;
                var div=document.getElementById("divPortlet");
                var table=document.getElementById("tablePortlet");
                var noOfRows=document.getElementById("noOfRows").value;
                var col=document.getElementById("noOfColumns").value;
//                alert("noOfRows are "+noOfRows);
//                alert("noOfCols are "+col)
//                alert("prevRow is "+prevRow);

                row=parseInt(row)+parseInt(noOfRows);
<%              if(edit.equals("true")){
    %>
                    row=parseInt(prevRow)+parseInt(noOfRows);
<%              }
    %>          
//                col=col+noOfCols;
                var regionHtml="";
//                alert("row are "+row)
//                alert("col are "+col);
                var width=300;
                if(col==1)
                    width=100;
                else if(col==2)
                    width=50;
                else if(col==3)
                    width=33;
                else if(col==4)
                    width=25;
                    if(col<=4)
                    {
                        regionHtml+="<table width='100%'>";
                        for(var i=prevRow;i<row;i++)
                        {
                            regionHtml+="<tr width='100%'>";
                            for(var j=0;j<col;j++)
                            {
                                if(j==col-1)
                                {
                                    var mergeColHtml="<tr width='100%'><td width='100%' align='center'></td></tr>&nbsp";
                                }
                                else
                                {
                                    var mergeColHtml="<tr width='100%'><td width='100%' align='center'><a  onclick=\"mergeColumn("+tdId+","+col+")\" style='font-size:8pt;'>Merge Column</a></td></tr>&nbsp";
                                }
                                if(i==row-1)
                                {
                                    var mergeRowHtml="<tr width='100%'><td width='100%' align='center'></td></tr>";
                                }
                                else
                                {
                                    var mergeRowHtml="<tr width='100%'><td width='100%' align='center'><a  onclick=\"mergeRow("+tdId+","+col+")\" style='font-size:8pt;'>Merge Row</a></td></tr>&nbsp";
                                }
                                regionHtml+="<td id=\""+tdId+"\" width='"+width+"%' height='390px' colspan=\""+colSp+"\" rowspan=\""+rowSp+"\">";
                                regionHtml+="<div id=\"Dashlets-"+tdId+"\"class='portlet ui-widget ui-widget-content ui-helper-clearfix ui-corner-all' style=\"width:100%; height:100%\">";
                                regionHtml+="<table width='100%'>";
                                regionHtml+="<tr><td align='right'><img id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/cross.png\" onclick=\"closeOldPortlet('Dashlets-"+tdId+"',"+tdId+")\"/></td></tr>";                               
                                <%--regionHtml+="<ul id='treemenu123' class='treeviewdbrd ul'>";
                                regionHtml+="<li>Folder<ul><li>Sub Item 2.1</li><li>Folder 2.1<ul><li>Sub Item 2.1.1</li><li>Sub Item 2.1.2</li></ul></li></ul></li></ul>";--%>
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'Basic')\" style='font-size:8pt;'>Add Basic KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'BasicTarget')\" style='font-size:8pt;'>Add Basic Target KPI</a></td></tr>&nbsp";
//                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'Kpi')\" style='font-size:8pt;'>Add KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'Standard')\" style='font-size:8pt;'>Add Std KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'Target')\" style='font-size:8pt;'>Add Target KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'MultiPeriod')\" style='font-size:8pt;'>Add MultiPeriod KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'MultiPeriodCurrentPrior')\" style='font-size:8pt;'>Add MultiPeriod with current&prior</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"createDbrdGraphs("+tdId+")\" style='font-size:8pt;'>Add Graph</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showGraphs("+tdId+")\" style='font-size:8pt;'>Add Graph from Report</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"getDbrdTableColumns("+tdId+",'table')\" style='font-size:8pt;'>Add Table</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showKpiGraphs("+tdId+")\" style='font-size:8pt;'>Add KPI Graph</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showmap("+tdId+")\" style='font-size:8pt;'>Add Map</a></td></tr>&nbsp";
                            //    regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showGroupKpis("+tdId+")\" style='font-size:8pt;'>Add Group Measure KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"showComplexKPIs("+tdId+",'<%=DashboardId%>')\" style='font-size:8pt;'>Add Custom KPI</a></td></tr>&nbsp";
                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"getDbrdTableColumns("+tdId+",'groupMeassure')\" style='font-size:8pt;'>Add Group Measure KPI</a></td></tr>&nbsp";
//                                regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"getDbrdTableColumns("+tdId+",'textKpi')\" style='font-size:8pt;'>Add Text KPI</a></td></tr>&nbsp";
                                  regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"getviewby("+tdId+",'textKpi')\" style='font-size:8pt;'>Add Text KPI</a></td></tr>&nbsp";
                                  regionHtml+="<tr width='100%'><td width='100%' align='center'><a href='javascript:void(0)' onclick=\"javascript:showKpis("+tdId+",'KpiWithGraph')\" style='font-size:8pt;'>Add KPI With Graph</a></td></tr>&nbsp";
                                regionHtml+=mergeRowHtml;
                                regionHtml+=mergeColHtml;
                                regionHtml+="</table></div></td>";
                                tdId++;                                                          
                            }
                            regionHtml+="</tr>";
                        }
                        regionHtml+="</table>";

//                    var prevCol=col;
                    }
                    else
                    {
                        alert("column number should not exceed 4");

                    }
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=createRegionDashlet&dashboardId='+dbrdId+'&dashletId='+dashId+'&row='+row+'&col='+col+'&prevRow='+prevRow,
                    success:function(data){}
                });
                prevRow=row;
                $("#regionDialog").dialog('close');
                $('#tablePortlet').append('<tr><td>'+regionHtml+'</td></tr>');
            }

                function  mergeColumn(tdId,col)
            {
                var dbrdId = '<%=DashboardId%>';
                var CS= $("#"+tdId).attr('colspan');
                var RS=$("#"+tdId+"").attr('rowspan');
                if(RS==1)
                {
                    var delDashlet = tdId + CS;
                    var tdCol=document.getElementById(delDashlet);
                    tdCol.parentNode.removeChild(tdCol);


                }
                else
                {
                    var delDashlet = tdId + CS;
                    var tdCol=document.getElementById(delDashlet);
                    tdCol.parentNode.removeChild(tdCol);
                    for(var i=1;i<RS;i++)
                    {
                        delDashlet= (i*col) + (tdId+CS);
                        tdCol=document.getElementById(delDashlet);
                        tdCol.parentNode.removeChild(tdCol);
                    }
                }
                var colS=CS+1;
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=mergeColumns&dashboardId='+dbrdId+'&dashlet='+tdId+'&colSpan='+colS+'&delDashletId='+delDashlet,
                    success:function(data){
                        $("#"+tdId+"").attr('colspan',colS);
                    }
                });
            }

                function mergeRow(tdId,col)
            {
                var dbrdId =  '<%=DashboardId%>';
                var RS=$("#"+tdId+"").attr('rowspan');
                var CS=$("#"+tdId+"").attr('colspan');
                for(var i=0;i<CS;i++)
                {
                    var delDashlet = (tdId+i) + (col*RS)
                    var trRow=document.getElementById(delDashlet);
                    trRow.parentNode.removeChild(trRow);
                }
                var rowS=RS+1;
                var height=rowS*350;
//                alert("height"+height)
                $("#"+tdId+"").css('height',height);
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=mergeRows&dashboardId='+dbrdId+'&dashlet='+tdId+'&rowSpan='+rowS,
                    success:function(data){
                       $("#"+tdId+"").attr('rowspan',rowS);
                    }
                });
            }

//            function deleteDashlet(tdId)
//            {
//                var dbrdId =  '<%=DashboardId%>';
//                var delDashlet=document.getElementById(tdId);
//                delDashlet.parentNode.removeChild(delDashlet);
//                $.ajax({
//                    url: 'dashboardTemplateViewerAction.do?templateParam2=delDashlet&dashboardId='+dbrdId+'&dashlet='+tdId,
//                    success:function(data){
//                    }
//                });
//
//            }

            function zoomer(divid,name)
            {
                $("#zoomer").data('title.dialog',name)
                $("#zoomer").dialog('open');
                document.getElementById("zoomer").innerHTML=document.getElementById(divid).innerHTML;
                var divObj = document.getElementById("zoomer");
                var imgObjs = divObj.getElementsByTagName("img")
                imgObjs[0].style.width = '780';
                imgObjs[0].style.height = '500';
            }
            function ZoomTarget(){
                $("#ZoomTarget").dialog('open');
            }
                function DashletRename(kpiMasterId,oldName,dashbrdId,kpidashId,fordesigner){
                    dashId=kpidashId;
                    kpidashboardId=dashbrdId;
                    kpimasterid=kpiMasterId;
                    fordesignerflag = fordesigner;
                    $("#DashletRename").dialog('open');
                    $("#oldDashLetName").val(oldName)
                    $("#newDashLetName").val('')

                }
                function updateDashletName(){
                    var newDashletName =$("#newDashLetName").val()
                    $("#DashletRename").dialog('close');
                    $.post('dashboardTemplateViewerAction.do?templateParam2=saveDashletName&kpiMasterId='+kpimasterid+'&newDashletName='+newDashletName+'&kpidashboardId='+kpidashboardId+'&fordesigner='+fordesignerflag+'&kpidashId='+dashId,function(data){
                    displayKPI(dashId, kpidashboardId, "","",kpimasterid, "","","","false")
               } );
                }
                function GraphRename(dashletName,dashboardid,graphId,dashletId,refReportId,kpiMasterId,dispSequence,dispType,fromDesigner)
                   {
                        dashId=dashboardid
                        graphid=graphId
                        dashletid = dashletId
                        refreportId = refReportId
                        kpimasterId = kpiMasterId
                        dispseq = dispSequence
                        disptype = dispType
                        fromdesigner = fromDesigner
                            $("#GraphRenameDialog").dialog({
                                    autoOpen: false,
                                    height:200,
                                    width: 300,
                                    position: 'justify',
                                    modal: true
                        });
                                $("#graphNameVal").attr('value',dashletName)
                                $("#NewgraphName").attr('value','')
                                $("#GraphRenameDialog").dialog('open')
                    }

                    function editKPIName(dashletId,dashboardId,folderId,graphId,kpiName,reportid,kpiMasterId,dispSequence,dispType,dashletName,forDesigner,editDbrd){
                                    dashletid =   dashletId;
                                    dashboardid = dashboardId;
                                    folderidGlobal = folderId;
                                    graphid = graphId;
                                    kpiGrname = kpiName;
                                    refreportId=reportid;
                                    kpimasterid =kpiMasterId;
                                    dispseq=dispSequence;
                                    disptype=dispType;
                                    newDashletName=kpiName;
                                    fromdesigner=forDesigner;
                                    editdbrd=editDbrd;
                                var td="<input type='text' value='"+kpiGrname+"' >";
                                $("#oldKpiGrname").html(td);
                                $("#newKpigrName1").val('');
                                $("#editKpiGrDialog").dialog('open');
                        }
//                function closeOldPortlet(ids, dashletId){
//                    var dashboardId = document.getElementById("dbrdId").value;
//                    var confirmText= confirm("Are you sure you want to delete");
//                    if(confirmText==true){
//                        var delDashlet=document.getElementById(dashletId);
//                        delDashlet.parentNode.removeChild(delDashlet);
//                        $.ajax({
//                            url: 'dashboardTemplateViewerAction.do?templateParam2=deleteDbrdGraphs&dashboardId='+dashboardId+'&dashletId='+dashletId,
//                            success: function(data){
//                                if (data != null && data != "")
//                                    alert("Report Deleted");
//                            }
//                        });
//                    }
//                }

                function showmap(divId){
                    divId = "Dashlets-"+divId;
                    var folderId = '<%=folderDetails%>';
//                    alert(folderId);
                    var reportId= '<%=DashboardId%>';
                    var ctxPath = '<%= request.getContextPath() %>';
                    document.getElementById("divId").value=divId;
//                    document.getElementById('hideDiv').value=hideDiv;
                    document.getElementById("folderId").value=<%=folderDetails%>//
                    document.getElementById("reportId").value=<%=DashboardId%>
                    var frameObj=document.getElementById("mapMeasureFrame");
                    var source=ctxPath+"/TableDisplay/PbChangeMapColumnsRT.jsp?folderIds="+folderId+'&divId='+divId+'&dashboardId='+reportId;
                    frameObj.src=source;
                    $("#MapMeasures").dialog('open');
                }
                      function contMenu(Obj){               
                $("#"+Obj.id).contextMenu({
                    menu: 'parampotionsListMenu'
                }, function(action, el, pos) {
                    contextMenuParamWork(action, el, pos);
                });                
            }
                            
                    function contextMenuParamWork(action, el, pos) {

                        switch (action) {

                            case "addDefaultValue":
                                {

                                    var elementId=el.attr('id').split("-")[1];
                                    addParamDefaultValues(elementId);
                                    break;
                                }

                            case "addParamSecurity":
                                {

                                    var elementId=el.attr('id').split("-")[1];
                            
                                    addParamSecurity(elementId);
                                    break;
                                }
                            case "addcomboBox":
                                {

                                    var elementId=el.attr('id').split("-")[1];
                            
                                    break;
                                }
                            
                        }
                    }
                  function addParamDefaultValues(elementId){
                                            $("#paramDefaultVal1").dialog({
                        // bgiframe: true,
                        autoOpen: false,
                        height: 400,
                        width: 600,
                        modal: true
                    });
                        var f=document.getElementById('paramDefaultValDisp1');
                        var REPORTID=document.getElementById('reportId').value;
                
                        var s="pbParamDefaultValues.jsp?elementId="+elementId+'&REPORTID='+<%=DashboardId%>;
                        f.src=s;
                
                        $("#paramDefaultVal1").dialog('open');
                    }
                   function cancelParamdefSecurity(){
                        $("#paramDefaultVal1").dialog('close');

                    }
                    function delfavparam(favName)
                    {                        
                       $(".closed").contextMenu({
                       menu: 'parmsMenu'
                       }, function(action, el, pos) {
                       contextMenuFavParam(action, el, pos,favName);
                     });
                    }
                    function contextMenuFavParam(action, el, pos,favName) {
                        switch (action) {
                            case "deleteFavParams":                                
                                {                                                                        
                                    var confirmText= confirm("Are you sure you want to delete "+ favName);                                    
                    if(confirmText==true){                        
                       $.ajax({
                        url: "<%=request.getContextPath()%>/dashboardTemplateAction.do?templateParam2=deleteFavoParam&favName="+favName+"&DashboardId="+<%=DashboardId%>,                        
                        success: function(data){                            
                            if(data=="true"){                                
                                alert(favName+" deleted successfully");
                                window.location.href = window.location.href;
                            }else{
                                alert(favName+" not deleted successfully");
                            }
                        }
                    });                                 
                                    break;
                                }
                                }
                        }                        
                    }
        </script>

        <style type="text/css">
          .column{width:170px;float:left;padding-bottom:100px}
.portlet1{margin:0 1em 1em 0;width:500px;overflow:auto}
.portlet{margin:0 1em 1em 0}
.portlet-header{margin:.3em;padding-bottom:4px;padding-left:.2em;color:#369;height:16px}
.portlet-header .ui-icon{float:right}
.portlet-content{width:490px;height:280px;padding:.1em}
.portlet-content1{width:1060px;min-height:120px;max-height:250px;overflow:auto;padding:.1em}
.ui-sortable-placeholder{border:1px dotted #000;visibility:visible!important;height:50px!important}
.ui-sortable-placeholder *{visibility:hidden}
.liClass{color:#fff;width:300px}
.fontsty{-moz-border-radius-bottomleft:4px;-moz-border-radius-bottomright:4px;-moz-border-radius-topleft:4px;-moz-border-radius-topright:4px;background-color:#bdbdbd}
a{font-family:Verdana;cursor:pointer;text-decoration:none}
a:link{color:#369}
*{font:11px verdana}
.startpage{display:none;position:absolute;top:15%;left:24%;width:800px;height:400px;padding:5px;border:10px solid silver;background-color:#fff;z-index:1002;-moz-border-radius-bottomleft:6px;-moz-border-radius-bottomright:6px;-moz-border-radius-topleft:6px;-moz-border-radius-topright:6px}
.black_start{display:none;position:absolute;top:0;left:0;width:100%;height:100%;background-color:#000;z-index:1001;-moz-opacity:.5;opacity:.5;filter:alpha(opacity=50);overflow:auto}
.paramRegion{background-color:#e6e6e6}
.treeviewdbrd ul{margin:0;padding:0}
.treeviewdbrd li{background:#fff url(list.gif) no-repeat left center;list-style-type:none;padding-left:22px;margin-bottom:3px}
.treeviewdbrd li.submenu{background:#fff url(closed.gif) no-repeat left 1px;cursor:hand!important;cursor:pointer!important}
.treeviewdbrd li.submenu ul{display:none}
.treeviewdbrd .submenu ul li{cursor:default}
        </style>
    </head>
    <body class="body">
        <%
        //    String url = request.getAttribute("dashdesignerurl").toString();
            String UserFldsData = "";
            if (request.getAttribute("UserFlds") != null) {
                UserFldsData = String.valueOf(request.getAttribute("UserFlds"));
            }%>
            <tr style="height:40px;width:100%;max-height:100%">
                 <td valign="top">
                    <table style="width:100%;">
                        <tr>
                            <td valign="top" style="width:100%;">
                                <jsp:include page="Headerfolder/headerPage.jsp"/>
                            </td>
                        </tr>
                    </table>
                 </td>
            </tr>

        <form name="frmParameter" method="post">
            <table style="height:600px;width:100%;max-height:100%"  cellpadding="0" cellspacing="0">
                <tr style="height:40px;width:100%;max-height:100%" >

                </tr>
                <tr style="height:15px;width:100%;max-height:100%">
                    <td>

                        <table width="100%" class="ui-corner-all">
                            <tr>
                                <td style="height:10px;width:25%" >
                                    <%com.progen.reportview.action.showReportName repname = new com.progen.reportview.action.showReportName();
            ArrayList repNameList = repname.buildReportName(dashboardName);
                String jsdashboardName=dashboardName.replace("'", "\\'");
                String jsdashboardDesc=dashboardDesc.replace("'", "\\'");
            for (int i = 0; i < repNameList.size(); i++) {%>

                                    <span id="reportName"  style="color:#4F4F4F;font-family:verdana;font-size:13px;font-weight:bold"  title="<%=dashboardName%>"><%=repNameList.get(i)%></span>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)"  style="text-decoration:underline" onclick="dashNmdone()" title="Click here to edit Dashboard Name">edit</a>
                                          <%
                                     jsdashboardName=dashboardName.replace("\\'", "'");
                                     jsdashboardDesc=dashboardDesc.replace("\\'", "'");
                                                  %>
                                                     <Input TYPE="hidden" name="dashbdHd" id="dashbdHd" value="<%=jsdashboardName%>">
                                    <Input TYPE="hidden" name="dashdescHd" id="dashdescHd" value="<%=jsdashboardDesc%>">
                                    <br/>
                                    <%}%>
                                </td>
                                <td valign="top" style="height:10px;width:50%">
                                    <div id='breadCrumb' class='breadCrumb module' style="width:500px">
                                        <ul>
                                            <li style="display:none"></li>
                                            <li style="display:none"></li>
                                            <% String pgnam = "";
            if (brdcrmb.getPgname1() != null) {
                pgnam = brdcrmb.getPgname1().toString();
                if (pgnam.equalsIgnoreCase(dashboardName)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname1()%>
                                            </li>

                                            <%
                } else {
                                            %>
                                            <li>
                                                <a href= <%=brdcrmb.getPgurl1()%> ><%=brdcrmb.getPgname1()%></a>
                                            </li>
                                            <%
                }
            }
            if (brdcrmb.getPgname2() != null) {
                pgnam = brdcrmb.getPgname2().toString();


                if (pgnam.equalsIgnoreCase(dashboardName)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname2()%>
                                            </li>
                                            <%
                } else {
                                            %>
                                            <li>
                                                <a href=<%=brdcrmb.getPgurl2()%>><%=brdcrmb.getPgname2()%></a>
                                            </li>
                                            <%                    }
            }
            if (brdcrmb.getPgname3() != null) {


                pgnam = brdcrmb.getPgname3().toString();
                if (pgnam.equalsIgnoreCase(dashboardName)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname3()%>
                                            </li>
                                            <%
                } else {
                                            %>
                                            <li>
                                                <a href=<%=brdcrmb.getPgurl3()%>><%=brdcrmb.getPgname3()%></a>
                                            </li>
                                            <%
                }
            }
            if (brdcrmb.getPgname4() != null) {
                pgnam = brdcrmb.getPgname4().toString();


                if (pgnam.equalsIgnoreCase(dashboardName)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname4()%>
                                            </li>
                                            <%
                } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl4()%>'><%=brdcrmb.getPgname4()%></a>
                                            </li>
                                            <%
                }
            }
            if (brdcrmb.getPgname5() != null) {
                pgnam = brdcrmb.getPgname5().toString();


                if (pgnam.equalsIgnoreCase(dashboardName)) {
                                            %>
                                            <li style="font-family:helvetica;font-size:11px;color: #967117;font-weight:bold;">
                                                <%=brdcrmb.getPgname5()%>
                                            </li>
                                            <%
                } else {
                                            %>
                                            <li>
                                                <a href='<%=brdcrmb.getPgurl5()%>'><%=brdcrmb.getPgname5()%></a>
                                            </li>
                                            <%
                }
            }
                                            %>
                                            <li style="display:none"></li>
                                            <li style="display:none"></li>
                                        </ul>
                                    </div>
                                    <div class="chevronOverlay main"></div>
                                </td>
                                <td style="height:10px;width:20%" align="right">
                                </td>
                            </tr>
                        </table>

                    </td>
                </tr>
                <tr style="width:100%;height:544px;max-height:100%">
                    <td>
                        <table width="100%" class="ui-corner-all" height="100%" border="1px solid black" cellpadding="0" cellspacing="0">
                            <tr class="ui-corner-all">
                                <!-- Begin of links-->
<%     //                     if(!edit.equals("true")){
%>
                                <td width="13%" valign="top" class="draggedTable1" >
                                    <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all">
                                        &nbsp;&nbsp;<font style="font-weight:bold" face="verdana" size="1px">Dashboard Designer</font>
                                    </div>
                                    <div class="masterDiv" style="overflow: auto;height:522px">
                                        <ul id="dashbrdTree" class="filetree treeview-famfamfam">
                                            <li class="closed" ><img src="icons pinvoke/folder-horizontal.png">&nbsp;&nbsp;<span>Business Roles</span>
                                                <ul id="userFlds" class="background">
                                                    <%=UserFldsData%>
                                                </ul>
                                            </li>
                                            <li class="closed" ><img src="icons pinvoke/folder-horizontal.png">&nbsp;&nbsp;<span>Dimensions</span>
                                                <ul id="userDims" class="background">
                                                </ul>
                                            </li>
                                        </ul>
                                    </div>
                                                
                                    <script>
                                        getUserDims();
                                    </script>

                                </td>
<%           //                   }
%>                                <!--Endof links-->

                                <!-- Begin of Parameters/Graph Region-->
                                <td valign="top" style="width:80%" class="ui-corner-all">
                                    <table style="width:99%" valign="top">
                                        <tr class="paramRegion">
                                            <td valign="top" width="100%" height="100px" colspan="2">
                                                <table style="height:100%" class="draggedTable"  id="newDragTables">
                                                    <tr id="dragDims">
                                                        <td style="height:100%" id="draggableDims" valign="top">
                                                    <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                                &nbsp;&nbsp;<font size="2" style="font-weight:bold">Drag Parameters To Here </font> &nbsp;&nbsp;<a href="#" onclick="showParams()" title="Click to Set Parameters" >Preview</a>
                                    &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                      &nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                     &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                     &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp;&nbsp;
                                          <a id="clearParams" onclick="clearParams()" title="Clear Parameters" >Clear</a>
                                    
                                                            </h3>
                                                            <div id="dragDiv" style="min-width:800px;min-height:100px">
<%                                                              if(edit.equals("true")){
    %>
                                                             <ul id="sortable" style="width:800px">
                                                                <%=ParamSectionDisplay%>
                                                             </ul>
                       <%                                           }
                                                                else{
%>
                                                                <ul id="sortable" style="width:800px">
                                                                </ul>
<%                                                                  }
%>
                                                            </div>
                                                        </td>

                                                    <tr id="favParams">
                                                        <td align="right">
                                                            <input type="button" align="right" id="saveFavParams" name="saveFavParams" disabled value="Save as Favourite" onclick="saveFavouriteParams()">
                                                        </td>
                                                    </tr>
                                        </tr>
                                        <tr id="lovParams" style="height:100%;display:none">
                                            <td valign="top" style="height:100%">
                                                <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                                                    &nbsp;&nbsp;<font size="2" style="font-weight:bold">Parameters</font>&nbsp;&nbsp;<a href="#" onclick="showMbrs()" title="Click to Edit Parameters" >Edit</a>
                                                </h3>
                                                <div id="paramDisp" style="width:800px">
                                                </div>
                                            </td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr valign="top">
                                <td valign="top" width="100%">
                                    <div class="navsection" >
                                        <div class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all" style="width:100%"  onclick="dispPortlet()" >
                                            <table width="100%">
                                                <tr>
                                                    <td align="right">
                                                        <a href="#" onclick="addRegion()" style="font-size:8pt;">Add Region</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <!--<a href="#" onclick="addMap()" style="font-size:8pt;">Add Map</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <a href="#" onclick="addKpiGraph()" style="font-size:8pt;">Add KPI Graph</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <a href="#" onclick="addStdKpi()" style="font-size:8pt;">Add Std KPI</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%if(!isAxa){%>
                                                        <a href="#" onclick="addKpiwithTarget()" style="font-size:8pt;">Add Target KPI</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                                        <%}%>
                                                        <a href="#" onclick="addGraph()" style="font-size:8pt;">Add Graph</a>
                                                    --></td>
                                                </tr>
                                            </table>
                                            <b style="font-family:verdana"></b>
                                        </div>
                                        <div id="divPortlet"  style="width:100%;">
<%                                         if(edit.equals("true")){
    %>
                                                        <%=buildDashBoard%>
                                                        <table id="tablePortlet"style="width:99%">
                                                        </table>
                                                        <table id="temptablePortlet"style="width:99%">
                                                        </table>
                                                <%}
                                            else{
%>
                                            <table id="tablePortlet"style="width:99%">
                                            </table>
                                            <table id="temptablePortlet"style="width:99%">
                                            </table>
<%                                             }
                                            %>
                                    </div>
                                        </div>
                                </td>
                            </tr>
                        </table>
                    </td><!-- Endof Parameters/Graph Region-->
                </tr>
            </table>
        </td>
    </tr>
</table>

<div id="graphsDialog" style="display:none" title="Add Graph">
    <iframe id="dataDispmem" NAME='dataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
<div id="kpisDialog" style="display:none">
    <table><tr><td>
            <input id="tableList" type="checkbox" onclick="getDisplayTablesdesigner('<%=request.getContextPath() %>','')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle1" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showListdesigner('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainerdesigner('<%=request.getContextPath() %>')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>
    <iframe id="kpidataDispmem" NAME='kpidataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
                                    <div id="groupMeasureDialog" style="display:none">
                                        <iframe id="groupdataDispmem" NAME='groupdataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
                                    </div>
<div id="kpisGraphsDialog" style="display:none" title="KPI Graph">
    <iframe id="kpigraphdataDispmem" NAME='kpigraphdataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
<div id="kpiDrillDialog" style="display:none" title="KPI Drill">
    <iframe id="kpiDrillDispmem" NAME='kpidrilldataDispmem' width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
<div id="kpiComment" class="commentDialog" title="Comments" style="display:none">
    <iframe src='about:blank' id="kpiCommentFrame" frameborder="0" height="100%" width="100%" ></iframe>
</div>
<div id="kpigrptargetType" title="KPI Target Type" style="display:none">
    <iframe src='about:blank' id="kpitargetTypeFrame" frameborder="0" height="100%" width="100%" ></iframe>
</div>
<div id="editKpiCustomColor" class="edit KPI CustomColor" title="Custom KPI Ranges" style="display:none">
    <iframe src='about:blank' id="editKpiCustomColorFrame" frameborder="0" height="100%" width="100%" ></iframe>
 </div>

<div id="NewDbrdGraph" title="NewDashboard Graph" style="display:none">
    <center>
        <br>
        <table style="width:100%" border="0">
            <tr>
                <td valign="top" class="myHead" style="width:40%">DashboardGraphName</td>
                <td valign="top" style="width:60%">
                    <input id="NewDbrdGraphName"  type="text" maxlength="35" name="NewDbrdGraphName" style="width:150px;height:20px;border:1px solid #2191C0" class="myTextbox3"  ><br><br><br>
                </td>
            </tr>

        </table>
        <table>
            <tr>
                <td><input type="button" class="navtitle-hover" style="width:auto" value="Next" id="NewDbrdGraphsave" onclick="createDbrdgraph(document.getElementById('newgraphid').value)"></td>

            </tr>
        </table>
    </center>
</div




<input type="hidden" name="allGraphIds" value="" id="allGraphIds">
<input type="hidden" name="REPIds" value="" id="REPIds">
<input type="hidden" name="CEPIds" value="" id="CEPIds">
<input type="hidden" name="MsrIds" value="" id="MsrIds">
<input type="hidden" name="divId" value="" id="divId">
<input type="hidden" name="kpis" value="" id="kpis">
<input type="hidden" name="kpiIds" value="" id="kpiIds">
<input type="hidden" name="allGrDetails" value="" id="allGrDetails">
<input type="hidden" name="dbrdId" value="<%=DashboardId%>" id="dbrdId">
<input type="hidden" name="kpiCommenttext" id="kpiCommenttext">
<input type="hidden" name="kpiCommentelmntid" id="kpiCommentelmntid">
<input type="hidden" name="kpiCommentmasterid" id="kpiCommentmasterid">
<input type="hidden" name="timeDimension" id="timeDimension">
<input type="hidden" name="kpiType" id="kpiType">
<input type="hidden" name="diffinDays" id="diffinDays">
<input type="hidden" name="newgraphid" id="newgraphid">
<input type="hidden" name="HiddenNewDbrdGraphName" id="HiddenNewDbrdGraphName">
<input type="hidden" name="folderId" id="folderId">
<input type="hidden" name="reportId" id="reportId">
<input type="hidden" name="hideDiv" value="" id="hideDiv">
<!--
-->
</form>

<div id="fade" class="black_overlay"></div>

<div id="graphListDialog" style="display:none" title="Select Graphs">
    <table>
        <%
            ProGenChartUtilities utilities = new ProGenChartUtilities();
            String str = utilities.buildGraphTypesDiv(request.getContextPath(), grpTypeskeys, GraphTypesHashMap, "getDbrdGraphColumns");
        %>
        <%=str%>
    </table>
    <%--<center>
        <input type="button" class="navtitle-hover" value="Cancel" onclick="$('#graphListDialog').dialog('close')">
    </center>--%>
</div>
<input type="hidden"  id="h" value="<%=request.getContextPath()%>">

<div id="graphColsDialog" title="Graph Columns" style="display:none">
    <iframe  id="graphCols" NAME='bucketDisp'  width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
<div id="tableColsDialog" title="Table Columns" style="display:none">
    <iframe  id="tablecols" NAME='bucketDisp'  width="100%" height="100%" frameborder="0" src='about:blank'></iframe>
</div>
<table width="100%" >
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td align="center">
           <!-- <input type="button" id="enableMapId"class="navtitle-hover" value="Disable Map" mapEnabled="false" style="width:auto" onclick="javascript:enableMap();">-->
            &nbsp;&nbsp;<input type="button" class="navtitle-hover" style="width:auto" value="Next" onclick="createDashboard()">
            &nbsp;&nbsp;<input type="button" class="navtitle-hover"title="Click here to reset"style="width:auto" value="Reset" onclick=" return resetCurrDashbd()">
            &nbsp;&nbsp;<input type="button" class="navtitle-hover" value="Cancel" style="width:auto" onclick="javascript:cancelDashboard();"></td>
    </tr>
    <tr>
        <td height="10px">&nbsp;</td>
        <td height="10px">&nbsp;</td>
    </tr>
</table>
<table style="width:100%">
    <tr>
        <td valign="top" style="width:100%;">
            <jsp:include page="Headerfolder/footerPage.jsp"/>
        </td>
    </tr>
</table>

<div id="changeNameDialog" title="Change Report Name" style="display:none">
    <iframe  id="changeNameDisp" NAME='changeNameDisp' height="100%" width="100%"  frameborder="0" src='about:blank' ></iframe>
</div>

<div id="fadestart" class="black_start"></div>
<div id="chngDashdNameDialog" title="Edit Dashboard Name" style="display:none">
    <table width="100%" align="center">
        <tr>
            <td style="width: 40%;">Dashboard Name :</td>
            <td><input type="text" name="dashName" style="font:11px verdana;background-color:white" id="dashName"></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><span name="dashMsg" style="font-family:verdana;color:red;font-size:11px;background-color:white" id="dashMsg"></span></td>
        </tr>
        <tr>
            <td style="width: 70%;">Dashboard Description:</td>
            <td><input type="text" name="dashdesc" style="font:11px verdana;background-color:white" id="dashdesc"></td>
        </tr>
        <tr>
            <td colspan="2" style="width:10px">&nbsp;</td>
        </tr>
        <tr>
            <td colspan="2" align="center"><input type="button" value="Done" class="navtitle-hover" onclick="checkDashdNm();"></td>
        </tr>
    </table>
</div>
<div id="zoomer" style="display:none;"  title="Graph">

</div>

<div id="favouriteParamsDialog" title="Save As Favourite" style="display:none">
    <iframe  id="favouriteParams" NAME='favouriteParams' width="100%" height="100%" frameborder="0" SRC='pbFavParameters.jsp'></iframe>
</div>

            <div id="regionDialog">
                <br>
                <table id="regTable" width="100%">
                    <tr widt="100%">
                        <td class="myhead" >Enter no. of Rows </td>
            <td><Input type="text"  class="myTextbox5" name="rows" onkeypress="return isNumberKey(event)" id="noOfRows" maxlength=100></td>
                    </tr>
                    <tr>
                        <td class="myhead" >Enter no. of Columns </td>
            <td><Input type="text"  class="myTextbox5" name="rows" onkeypress="return isNumberKey(event)" id="noOfColumns" maxlength=100></td>
                    </tr>
                    </table>
                <br>
                    <table width="100%">
                    <tr>
                        <td align="center"><input type="button" class="navtitle-hover" value="  Ok  " onclick="createRegion()"></td>
                    </tr>
                    </table>

            </div>
            <div id="graphPropertiesDiv" style="display: none" title="Graph Properties">
                  <iframe  frameborder="0" id="graphPropertiesFrame" width="100%" height="100%" name="graphPropertiesFrame" src='about:blank'></iframe>
            </div>

            <div id="MapMeasures" style="display: none" title="Map Measures">
                <iframe  frameborder="0" id="mapMeasureFrame" width="100%" height="100%" name="mapMeasureFrame" src="about:blank"></iframe>
            </div>
<div id="DashletRename" style="display:none" Name="Rename" title="DashletRename">
                            <table id="Dashlet" align="center"><br><br>
                                <tr><td>Old Dashlet Name</td><td><input type="text" name="oldDashletName" readonly id="oldDashLetName" value="" > </td></tr>

                                 <tr><td>New Dashlet Name</td><td><input type="text" name="newDashLetName" id="newDashLetName" value="" > </td></tr>

                            </table><br>
                            <table align="center">
                                     <tr>
                                         <td align ="center" colspan="4">
                                             <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="updateDashletName()">
                                         </td>
                                 </table>
                        </div>
            <div id="createKPIDiv" style="display: none">
               
            </div>
<ul id="parmsMenu" class="contextMenu" style="width:205px;height:100px;text-align:left;">
    <li class="deleteFavParams"><a href="#deleteFavParams">Delete Favourite</a></li>
</ul>
<ul id="parampotionsListMenu" class="contextMenu" style="width:205px;height:100px;text-align:left;">
    <li class="addDefaultValue"><a href="#addDefaultValue">Set Default Value</a></li>
    <li class="addParamSecurity"><a href="#addParamSecurity">Set Parameter Security</a></li>
</ul>
<div style="display:none" id="paramDefaultVal1" title="Parameter Default Values">
    <iframe  id="paramDefaultValDisp1" NAME='paramDefaultValDisp1' height="100%" width="100%"  frameborder="0" src='about:blank'></iframe>
</div>
<div id="GraphRenameDialog" title="Graph Rename">
                                                   <table>
                                                       <tr>
                                                           <td class="myhead">Old Name</td>
                                                           <td id="graphName">
                                                               <input type="text" readonly id="graphNameVal">
                                                           </td>
                                                       </tr>
                                                       <tr>
                                                           <td class="myhead">New Name</td>
                                                           <td>
                                                               <input type="text" id="NewgraphName" maxlength="100"/>
                                                           </td>
                                                       </tr><tr><td>&nbsp;&nbsp;</td></tr>
                                                       <tr><td align="center" colspan="2"><input type="button" value="Save" onclick="updateGraphName()"/></td></tr>
                                                   </table>
                                               </div>
                                                <div id="editKpiGrDialog" title="KPIGraph Rename">
                                                        <table>
                                                            <tr>
                                                                <td>Old Name</td>
                                                                <td id="oldKpiGrname"></td>
                                                            </tr>
                                                            <tr>
                                                                <td>New Name</td>
                                                                <td id="newKpigrName"><input type="text" id="newKpigrName1" name="newKpigrName1"/></td>
                                                            </tr>
                                                            <tr>&nbsp;&nbsp;</tr>
                                                            <tr>
                                                                <td><input type="button" value="Done" class="navtitle-hover" onclick="updateKpiGrName()"/></td>
                                                            </tr>
                                                        </table>
                                                    </div>
<div id="viewbydivid">
    
    <iframe  id="viewbyframe" NAME='viewbyframe'    frameborder="0" height="100%" width="100%" SRC='#'></iframe>
    <%--<center><input type="button" onclick="saveViewParams()" name="save" value="Save"  class="navtitle-hover" ></center>--%>
</div>
    <div id="TextkpiComment" class="commentDialog" title="Comments" style="display:none">
  <iframe src='about:blank' id="textkpiCommentFrame" frameborder="0" height="100%" width="100%" ></iframe>
  </div>
<% //DataTracker datatrack = new DataTracker();
            //datatrack.setclickdata(request, DashboardId);
            %>
</body>
</html>




