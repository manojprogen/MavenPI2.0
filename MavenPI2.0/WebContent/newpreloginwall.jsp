<%--
    Document   : OneView
    Created on : 5 June, 2012, 12:25:16 PM
    Author     : surender.maddi@progenbusiness.com
--%>

<%@page import="prg.db.OnceViewContainer"%>
<%@page import="com.progen.report.display.DisplayParameters"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.reportdesigner.db.ReportTemplateDAO"%>
<%@page import="com.progen.users.UserLayerDAO"%>
<%@page import="com.progen.action.UserStatusHelper"%>
<%@page import="utils.db.ProgenParam"%>
<%@page import="java.util.ListIterator"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.progen.timesetup.CalenderFormTable"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Locale"%>
<%@page import="java.awt.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%
           String userType = null;
           String trendbg="#357EC7";
      //     boolean isPortalEnableforUser=false;
    boolean isQDEnableforUser=false;
    boolean isPowerAnalyserEnableforUser=false;
   // boolean isOneViewEnableforUser=false;
   // boolean isScoreCardsEnableforUser=false;
    ServletContext context = getServletContext();
    String userIdStr = "";
    if (session.getAttribute("USERID") != null)
    {
        userIdStr = (String) session.getAttribute("USERID");
    }
      String oneviewtypedate =(String) session.getAttribute("oneviewdatetype");
     HashMap<String,UserStatusHelper> statushelper;
     statushelper=(HashMap)context.getAttribute("helperclass");
     UserStatusHelper helper=new UserStatusHelper();
     if(!statushelper.isEmpty()){
        helper=statushelper.get(request.getSession(false).getId());
        if(helper!=null){
       // isPortalEnableforUser=helper.getPortalViewer();
        isQDEnableforUser=helper.getQueryStudio();
        isPowerAnalyserEnableforUser=helper.getPowerAnalyser();
     //   isOneViewEnableforUser=helper.getOneView();
     //   isScoreCardsEnableforUser=helper.getScoreCards();
        userType=helper.getUserType();
        }}
     session.setAttribute("isPowerAnalyserEnableforUser", isPowerAnalyserEnableforUser);
     session.setAttribute("isQDEnableforUser", isQDEnableforUser);
            String themeColor = "blue";
            Locale locale = null;
            //          String fromReport=String.valueOf(request.getAttribute("fromReport"));
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }

            String useragent = request.getHeader("User-Agent");
            String browserType = "";
            String user = useragent.toLowerCase();
            if (user.indexOf("msie") != -1) {
                browserType = "IE";
            } else if (user.indexOf("netscape6") != -1) {
                browserType = "Net";
            } else if (user.indexOf("mozilla") != -1) {
                browserType = "Moz";
            }

            List<String> viewByIds = new ArrayList<String>();
            List<String> viewByNames = new ArrayList<String>();
            List<String> createdDates = new ArrayList<String>();
            List<String> createdBy = new ArrayList<String>();
            List<String> modifiedDates = new ArrayList<String>();
            List<String> modifiedBy = new ArrayList<String>();
            List<String> busRoleIDs = new ArrayList<String>();
            List<String> busRoleNames = new ArrayList<String>();
            List<String> viewroleIds = new ArrayList<String>();
            List<String> viewroleNames = new ArrayList<String>();

            List<String>viewByNamesSeq=new ArrayList<String>();
            List<String>viewByIdsSeq=new ArrayList<String>();

            viewByIds = (List<String>) request.getAttribute("ViewByIds");
            viewByNames = (List<String>) request.getAttribute("viewByNames");
            createdDates = (List<String>) request.getAttribute("createdDates");
            createdBy = (List<String>) request.getAttribute("createdBy");
            modifiedDates = (List<String>) request.getAttribute("modifiedDates");
            modifiedBy = (List<String>) request.getAttribute("modifiedBy");
            busRoleIDs = (List<String>) request.getAttribute("BusRoleIds");
            busRoleNames = (List<String>) request.getAttribute("BusRoleNames");
            viewroleIds = (List<String>) request.getAttribute("viewroleIds");
            viewroleNames = (List<String>) request.getAttribute("viewroleNames");

            viewByIdsSeq = (List<String>) request.getAttribute("viewByIdsSeq");
            viewByNamesSeq = (List<String>) request.getAttribute("viewByNamesSeq");

            session.setAttribute("elementIds", null);
               session.setAttribute("orgelementNames", null);
               boolean homeFlag = (Boolean)request.getAttribute("homeFlag");
               String oneViewId = (String)request.getAttribute("oneViewId");
               String oneviewname = (String)request.getAttribute("oneviewname");

                String[] scheduleday={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
                String[] sday={"2","3","4","5","6","7","1"};

//               Dimension dim = null;
//               dim = Toolkit.getDefaultToolkit().getScreenSize();
//               int sreenWidth = dim.width-110;
                 String[] strOprtrs = {"<", ">", "<=", ">=", "=", "<>"};

                 UserLayerDAO userdao=new UserLayerDAO();
            int USERID = Integer.parseInt((String) session.getAttribute("USERID"));
            request.setAttribute("USERID", USERID);
            String userTypeAdmin = userdao.getUserTypeForFeatures(USERID);
            String[] duration={"Day","Week","Month","Quarter","Year"};
            String[] comparision={"Last Period","Last Year","Period Complete","Year Complete"};
            String[] Aggr={"SUM","MIN","MAX","AVG","COUNT","COUNTDISTINCT"};

           ReportTemplateDAO dao = new ReportTemplateDAO();
           PbReturnObject retObj = null;
           retObj = dao.getBurolsByUserId(String.valueOf(USERID));
           ArrayList<String> folderIdsList = new ArrayList<String>();
           ArrayList<String> folderNamesList = new ArrayList<String>();
           HashMap<String,String> rolemap=new HashMap<String, String>();
           String FolderId = "";
           String FolderName = "";
           String[] colNames = null;
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

%>

<html>
    <style type="text/css">
#fixedtop1 { position: fixed; top: -5.5%; left:200px; margin: auto; right: 800px; width: 70px; border: none; z-index: 50; }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
        .ui-progressbar-value { background-image: url(images/barchart.gif); }
        .headlinestyle {
            font-family: verdana;
            font-size: 12px;
            font-size-adjust: none;
            font-stretch: normal;
            font-style: normal;
            font-variant: normal;
            font-weight: bold;
            line-height: normal;


        }
        .ui-progressbar-value { background-image: url(images/barchart.gif); }
        .myHead
        {
            font-family: Verdana;
            font-size: 8pt;
            font-weight: bold;
            color: #000;
            padding-left:12px;
            width:20%;
            background-color:#b4d9ee;
            border:0px;
            /*apply this class to a Headings of servicestable only*/
        }

        .myAjaxTable {
            table-layout:fixed;
            background-color: #FFFFFF;
            text-align:left;
            border: 0px solid #000000;
            font-size:10px;
            left:4px;
            height:auto;
            border-collapse:separate;
            border-spacing:5px;
        }
        .ajaxboxstyle {
            background-color:#FFFFFF;
            border: 0.1em solid #0000FF;
            height:50px;
            margin:0 0.5em;
            overflow-x:hidden;
            overflow-y:auto;
            position:absolute;
            text-align:left;
            border-top: 1px groove #848484;
            border-right: 1px inset #999999;
            border-bottom: 1px inset #999999;
            border-left: 1px groove #848484;
            background-color:#f0f0f0;
            width:450px;
        }
        .black_overlay{
            display: none;
            position: absolute;
            top: 0%;
            left: 0%;
            width: 110%;
            height: 200%;
            background-color: black;
            z-index:1001;
            -moz-opacity: 0.5;
            opacity:.50;
            overflow:auto;
        }

        .white_content {
            display: none;
            position: absolute;
            top: 30%;
            left: 35%;
            width: 50%;
            height:50%;
            padding: 16px;
            border: 10px solid silver;
            background-color: white;
            z-index:1002;
            -moz-border-radius-bottomleft:6px;
            -moz-border-radius-bottomright:6px;
            -moz-border-radius-topleft:6px;
            -moz-border-radius-topright:6px;
        }

        table.grid .collapsible {
            padding: 0 0 3px 0;
        }

        .collapsible a.collapsed {
            display: block;
            width: 15px;
            height: 15px;
            background: url(images/addImg.gif) no-repeat 3px 3px;
            outline: 0;
        }

        .collapsible a.expanded {
            display: block;
            width: 15px;
            height: 15px;
            background: url(images/deleteImg.gif) no-repeat 3px 3px;
            outline: 0;
        }
        .ui-icon ui-icon-plusthick{
            z-index: 10000;
        }
        .ui-icon ui-icon-triangle-2-n-s{
            z-index: 10000;
        }
        .overlapDiv{
            z-index: 10000;
            }
            .myDesignClass{
                font-size: 12pt;
                font-family: Calibri, Calibri, Calibri, sans-serif;
            }
           .waterMarkDiv {
  color: #d0d0d0;
  font-size: 200pt;
  -webkit-transform: rotate(-45deg);
  -moz-transform: rotate(-45deg);
  position: absolute;
  width: 100%;
  height: 100%;
  margin: 0;
  z-index: -1;

}

.notesStyle ul li ul{
margin:0.1px;
}
.notesStyle li { /* ul ul li*/
    background: white;/*#F5F5F5*/
    font-size: 11px;
    font-family: verdana;
    color:black;
    list-style: none;
    padding: 0.1em;
    border-bottom: 1px solid white;
    margin:0.1em;
}
/*.notesStyle li:before{
content:"\00BB";
}*/
.ui-datepicker {
    padding: 0.2em 0.2em 0;
    width: 17em;
    z-index: 1000000;
}
/*            .alinkclass a {font-family:Verdana; font-size:15px;color:black}
            .alinkclass a:link {color:grey;}
            .example1 a:visited {color:seagreen;}
            .alinkclass a:hover {text-decoration:none;color:#357EC7;}

            .alinkclass-selected a {font-family:Verdana; font-size:15px;color:#357EC7;}
            .alinkclass-selected a:link {color:#357EC7;}
            .example1 a:visited {color:seagreen;}
            .alinkclass-selected a:hover {text-decoration:none;color:grey;}
            .alinkclass-selected a:hover {text-decoration:none;color:black;}*/


    </style>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>pi</title>

        <script src="javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/docs/css/jq.css" type="text/css" media="print, projection, screen" />-->
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/tablesorter/themes/<%=themeColor%>/style.css" type="text/css" media="print, projection, screen" />-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>

<!--        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/tablesorterStyle.css" />-->
        <script type="text/javascript"  language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/docs/js/chili/chili-1.8b.js"></script>
        <!--        <link type="text/css" href="stylesheets/ui.all.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/queryDesign.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/jquery.simplemodal-1.1.1.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/jquery.contextMenu.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.jBreadCrumb.1.1.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
<!--        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery.tablesorter.collapsible.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%= request.getContextPath()%>//dragAndDropTable.js"></script>
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <SCRIPT type="text/javascript" SRC="<%=request.getContextPath()%>/javascript/overlib.js">  </SCRIPT>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/white/TableCss.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/quicksearch.js"></script>
          <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script type=""  language="JavaScript" src="<%=request.getContextPath()%>/tablesorter/jquery.columnfilters.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/reportviewer/ReportViewer.js"></script>
                <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/chartTypes.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/OneViewJS.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/d3.v3.min.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />-->


<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/css/style.css" rel="stylesheet" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/stylesheets/tablesorterStyle.css" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/BreadCrumb.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/ReportCss.css" rel="stylesheet"/>
         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplot.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.barRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.categoryAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pointLabels.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.pieRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.donutRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasTextRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.dateAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.logAxisRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.highlighter.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.bubbleRenderer.min.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.funnelRenderer.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.ClickableBars.js"></script>
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.meterGaugeRenderer.min.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.EnhancedLegendRenderer.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.trendline.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.canvasOverlay.min.js"></script>
             <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/d3/customtooltip.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/css/d3/tooltip.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
           <link href="<%=request.getContextPath()%>/javascript/jqplot/jquery.jqplotOneview.min.css" rel="stylesheet" type="text/css" />
           <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jqplot/jqplot.cursor.min.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/datedesign.css" rel="stylesheet"/>
        <style type="test/css">
            .tblWoBorder{
                border:none;
            }
        </style>

        <script type="text/javascript">
            var change="";
            var changeper="";
           var totaltrends;
        var preOneId;
        var premeasId;
        var preroleId;
         var premonthY;
         var prenoOfTrends;
          var prenoOfTrendscp;
            $(document).ready(function(){
                 $("#changetext").keyup(function(event){
var input = $(this).val();
if(!isNumber(input)){

$(this).val(input.substring(0, input .length-1));
}

});
function isNumber(n){
return (parseFloat(n)==n);
}


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

               $( "#dateId1" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy",
                onClose: function showdate(){
                       var a;
                     a=($("#dateId1").val());
                    var dateArr2=new Array()
                    dateArr2=a.split(",");
                     if(a!=""){
                    $("#pfield1").html(dateArr2[2]+"'"+dateArr2[3].substring(2))
                    $("#pfield2").html(dateArr2[1])
                    $("#pfield3").html(dateArr2[0])
                             }
                }


                    });
                $( "#todate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy",
                onClose: function showdate(){
                       var a;
                     a=($("#todate").val());
                    var dateArr1=new Array()
                    dateArr1=a.split(",");
                     if(a!=""){
                    $("#tdfield1").html(dateArr1[2]+"'"+dateArr1[3].substring(2))
                    $("#tdfield2").html(dateArr1[1])
                    $("#tdfield3").html(dateArr1[0])
                }
                }

            });

                $("#stdate").datepicker({
                    showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                         dateFormat: "D,d,M,yy",
                         onClose: function showdate(){
                    var a;
                     a=($("#stdate").val());
                 var dateArr=new Array()
                    dateArr=a.split(",");
                     if(a!=""){
                    $("#stfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#stfield2").html(dateArr[1])
                    $("#stfield3").html(dateArr[0])
//                    alert(dateArr[2])
                }
                }
                    });
//                    var dateArr3=new Array()
                $("#comparefrom").datepicker({
                    showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                         dateFormat: "D,d,M,yy",
                         onClose: function showdate(){
                    var a;
                     a=($("#comparefrom").val());
                 var dateArr=new Array()
                    dateArr=a.split(",");
                     if(a!=""){
                    $("#cffield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#cffield2").html(dateArr[1])
                    $("#cffield3").html(dateArr[0])
//                    alert(dateArr[2])
                }
                }
                    });
                     $("#compareto").datepicker({
                    showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1,
                         dateFormat: "D,d,M,yy",
                         onClose: function showdate(){
                    var a;
                     a=($("#compareto").val());
                 var dateArr=new Array()
                    dateArr=a.split(",");
                     if(a!=""){
                    $("#ctfield1").html(dateArr[2]+"'"+dateArr[3].substring(2))
                    $("#ctfield2").html(dateArr[1])
                    $("#ctfield3").html(dateArr[0])
//                    alert(dateArr[2])
                }
                }
                    });
 $("#dateId").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });
                    $("#customMsrdate").datepicker({
                        changeMonth: true,
                        changeYear: true,
                        showButtonPanel: true,
                        numberOfMonths: 1,
                        stepMonths: 1
                    });

                if('<%=homeFlag%>' == "true"){
                    viewOneBy('<%=oneviewname%>','<%=oneViewId%>');
                }


                $("#tablesorterReport")
                .tablesorter({widthFixed: true})

                     $("#tablesorterReport") .tablesorterPager({container: $('#pagerReport')})

                if ($.browser.msie == true){
//
                    $("#getMeasuresID").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
//
                }
                else{
                    //                    $('#dateId').datepicker()

                    $("#getMeasuresID").dialog({
                        autoOpen: false,
                        height: 350,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });

                    $("#dialogTestForKpis").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                }

            });
                function cancelColor()
          {
               $("#colorsDiv").dialog('close')
          }
          function getRoleNamesInOneView(names,oneviewname){
              $("#roleNames").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 250,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
              var rolename = new Array();
              rolename = names.toString().split(",");
              var htmlVar = "";
              htmlVar +="<table>"
              for(var i=0;i<rolename.length;i++){
                  htmlVar += "<tr><td>"+rolename[i]+"</td></tr>";
              }
              htmlVar +="</table>"
              $("#roleNames").html(htmlVar);
              //$("#roleNames").dialog('option', 'title', oneviewname+' - Role Name(s)');
              $("#roleNames").dialog('open');
          }
        </script>
        <script type="text/javascript">
            var dashletId = "";
            var colNumber = "";
            var reportTime = "";
            var oneviewTimecheck = "";
            var oneviewtime = "";
            var regionName = "";
            var oneViewIdValue='';
            var oneViewName='';
            var busroleId ='';
            var oneviewHeight='';
            var reportid='';
            var graphid="";
            var timedetails='';
            var isRepDate='';
            var width='';
            var height='';
            var graphNum='';
            var regDate='';
            var from=''
            var ctxpath='<%=request.getContextPath()%>';
            var isviewer=false;
            var numdays='';
            var isDialEnabled=false;
            var oneViewType='';
            var oneViewBusRole='';

             function gotoDBCON(ctxPath){
                 if(!<%=isQDEnableforUser%>){
                        alert("You do not have the sufficient previlages")
                    }else{
                document.forms.frmParameter.action=ctxPath+"/pbBase.jsp?pagename=Query Studio&curntpage=Database Connection#Database_Connection";
                document.forms.frmParameter.submit();
                }
            }
            function goPaths(path){
//                alert(path)
                var modulecode=path.replace("home.jsp#","");
                var userType='<%=userType%>'
                if(modulecode=='Dashboard_Studio' || modulecode=='Report_Studio'){
                    if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
                }else if(modulecode=='pbBIManager.jsp'){
                    if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                }
                parent.closeStart();
                document.forms.frmParameter.action=path;
                document.forms.frmParameter.submit();
            }
             var oneviewtypedate1='';
             function pbBiManager(){
                var path = "";
                if(!<%=isQDEnableforUser%>){
                         path="home.jsp";
                    alert("You do not have the sufficient previlages")
                }
                else {
                    path = "srchQueryAction.do?srchParam=pbBiManager";
                }
                parent.closeStart();
                document.forms.myFormH.action=path;
                document.forms.myFormH.submit();
            }
            function oneViewBybutton(){
                $("#oneviewbyNameId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
               if(!<%=isPowerAnalyserEnableforUser%>)
                        alert("You do not have the sufficient previlages")
               else
                   $("#oneviewbyNameId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                $("#oneviewbyNameId").dialog('open');

                   oneviewtypedate1='<%=oneviewtypedate%>'
                     if(oneviewtypedate1=='true'){
                  $("#oneviewrange").show();
                  }
                $("#viewBydivid").val('');
            }
             var timeDetail;
            function insertName(){

                //                $("#onveViewByName").html($("#viewBydivid").val());
                oneViewName=$("#viewBydivid").val();
                oneviewHeight=$("#oneviewRegHeightId").val();
                oneViewType=$("#oneViewType").val();
                oneViewBusRole=$("#roleId").val();
                $("#oneviewType").val(oneViewType);
//                var oneId="";
                var innerWidth=parseInt(window.innerWidth);
                timeDetail = $("#time").val();
                if(oneViewName!=''){
                $("#tabIds").html(oneViewName);
                $("#onveViewByName").html(oneViewName);
                $.ajax({
                    url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=insertOneViewName&oneviewName='+encodeURIComponent(oneViewName)+'&oneViewType='+oneViewType+'&roleId='+oneViewBusRole+'&innerWidth='+innerWidth+'&datetype='+timeDetail,
                    success: function(data) {
//                        alert(data)
                        if(data!=""){
                        oneViewIdValue=data;
                        }
                        $("#br1").remove("br");
                $("#br2").remove("br");


                $("#designID").hide();
                $("#showNameId").show();
                $("#designValueId").show();
                $("#footID").show();
                $("#tabs").show();
                $("#createdId").show();
                //                $("#datedetailId").show();
                //                $("#timedetailsId").show();
                $("#savebuttonId").show();
                $("#favreportid").show();
                $("#cacheId").show();
                $("#busroleId").show();
                $("#busroleTdId").show();
                $("#busroleForKpsId").show();
                $("#busNamId").show();
                $("#clearId").show();
                $("#designId").show();
                $("#divIdTest").hide();
                $("#divIdTest1").hide();
                $("#paraminfoId").hide();
                $("#oneviewbyNameId").dialog('close');
                  if(oneViewType=="Business TemplateView" || oneViewType=="Measure Based Business Template"){
                      $("#action1").val("fromDesigner");
                    AddMoreDimension('<%=request.getContextPath()%>',oneViewIdValue);

                  }
                    }
                });

                }
                else{
                alert("Please Enter Oneview Name!");
                }
                //alert(oneViewIdValue)

            }
    function AddMoreDimension(ctxPath,oneviewId) {
    $("#AddMoreParamsDiv").dialog({
       autoOpen: false,
       height: 420,
       width: 500,
       position: 'justify',
       modal: true,
       resizable:true
    });
    var frameObj=document.getElementById("addmoreParamFrame");
    var source=ctxPath+"/reportTemplateAction.do?templateParam=addMoreDimensions&foldersIds="+oneViewBusRole+"&REPORTID="+oneviewId+'&isbusTemaplateView=true';
    frameObj.src=source;
//    alert(source)
    $("#AddMoreParamsDiv").dialog('open');
    }
            var width=1300;
            var height=oneviewHeight;
            var tdId=0;
            var colSp=1;
            var rowSp=1;

            var prevRow=0;
            var divName='';
            var row=0;
            var rws=0;
            function createoneviewRegion(){
                height=oneviewHeight;
                //                busroleId = $("#busroleId").val();
                //                if(busroleId!=''){
                 $("#regionDivId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 330,
                        position: 'justify',
                        modal: true
                    });
                $("#regionDivId").dialog('open');
                $("#rowId").val('');
                $("#columnId").val('');

            }
                    function addRegioninViewer(oneViewId){


                      oneViewIdValue=oneViewId;
                $.ajax({
                   url: 'oneViewAction.do?templateParam2=viewerRegions&oneViewId='+oneViewId,
                    success: function(data) {

                        var jsonVar=eval('('+data+")")
                        tdId=jsonVar.dashlets
                        oneviewHeight=jsonVar.height
                        height=jsonVar.height
                         from=jsonVar.viewerfrom
                         row=jsonVar.rws
                         prevRow=row
//                         alert(height)
//                         alert(oneviewHeight)
                    }
                });




                 $("#regionDivId").dialog({
                        autoOpen: false,
                        height: 200,
                        width: 330,
                        position: 'justify',
                        modal: true
                    });
                $("#regionDivId").dialog('open');
                $("#rowId").val('');
                $("#columnId").val('');

            }

            function displayRegion(){
                isviewer=false;
                var innerWidth=parseInt(window.innerWidth)-120;
                var dashId=tdId;
                var noOfRows=document.getElementById("rowId").value;
                var col=document.getElementById("columnId").value;


                row=parseInt(row)+parseInt(noOfRows);
                var hight=180*row;

                $("#designId").css({'height':hight})
                $("#designId").css({'padding-bottom':''})
                $("#regionTableId").css({'height':hight})
                var regionHtml="";
               if(col==1)
                    width=innerWidth;
                else if(col==2)
                    width=Math.round(innerWidth/2);
                else if(col==3)
                    width=Math.round(innerWidth/3);
                else if(col==4)
                    width=Math.round((innerWidth/4)-5);
                else if(col==5)
                    width=Math.round((innerWidth/5)-10);
                else if(col==6)
                    width=Math.round((innerWidth/6)-15);
               ;
                if(col<=6)
                {

                    regionHtml+="<table  style=\"table-layout:fixed; width:100%; border-spacing: 15px;\">";
                    for(var i=prevRow;i<row;i++)
                    {

                        regionHtml+="<tr width='100%' >";
                        for(var j=0;j<col;j++)
                        {
                            if(j==col-1)
                            {
                                var mergeColHtml="&nbsp";
                            }
                            else
                            {
                                var mergeColHtml="<a href='#' onclick=\"mergeColumn("+tdId+","+col+","+width+")\" style='text-decoration:none;font-size:pt;' class=\"ui-icon ui-icon-arrowthick-2-e-w\" title=\"Merge Column\"></a>&nbsp";
                            }
                            if(i==row-1)
                            {
                                var mergeRowHtml="";
                            }
                            else
                            {
                                var mergeRowHtml="<a href='#' onclick=\"mergeRow("+tdId+","+col+")\"  style='text-decoration:none;font-size:20pt;' class=\"ui-icon ui-icon-arrowthick-2-n-s\" title=\"Merge Rows\">-</a>&nbsp";
                            }
                            //<td id='drillId"+tdId+"' style='display:none;width:0px;align:right;'><a  class='ui-icon ui-icon-triangle-2-n-s' title='Drill' style='text-decoration: none;' onclick=\"drillToReports()\" valign='top' href='javascript:void(0)'></a></td>
                            regionHtml+="<td id=\""+tdId+"\" width='"+width+"px'  height='"+height+"px' colspan=\""+colSp+"\" rowspan=\""+rowSp+"\" style=''>";


                         var widt = parseInt(width)-parseInt(60);

//
                             regionHtml+=" <table style='margin-left: 10px;'>"
                             regionHtml+="<tr>";
                             regionHtml+="<td id=\"Dashlets"+tdId+"\" style='font-size:12pt;color:#000000;'></td>"
                              regionHtml+="<td id=\"regionId"+tdId+"\" style='display:none;width:0px;align:right;'>"
//                              regionHtml+="<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectTypeDiv('Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"')\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>"
                              <%if(isPowerAnalyserEnableforUser || isQDEnableforUser){%>
                              regionHtml+="<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd("+tdId+")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>"
                       <% } %>regionHtml+="<div id=\"readdDivId"+tdId+"\" style='display:none;width:70px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' class='overlapDiv'>"
                                             regionHtml+="<table border='0' align='left' >"
                                                regionHtml+="<tr><td>"
                                                         <%--<table><tr><td><a href='javascript:void(0)' style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;" onclick="addIcal()" >Add New I-cal</a></td></tr></table>--%>
                                                         regionHtml+="<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType1('report','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" >Reports</a></td></tr></table>"
                                 
                                                     regionHtml+="</td></tr>"
                                             regionHtml+="</table>"
                                             regionHtml+="</div>"
                                      regionHtml+="</td></tr></table>"

                            regionHtml+="<div id=\"Dashlets-"+tdId+"\"  class='ui-tabs ui-widget ui-widget-content ui-corner-all' style=\"border-top:blank; border-bottom:medium hidden; border-left:medium hidden; border-right:medium hidden;border-color:grey;width:100% ; height:100% ;margin-left: 10px; margin-right: 10px;  \">";
                            regionHtml+="<table  class='tblWoBorder'  width='100%' >";
                            regionHtml+="<tr><td align='left'><img id=\"tdImage\" src=\"<%=request.getContextPath()%>/images/cross.png\" onclick=\"closeOldPortlet('Dashlets-"+tdId+"',"+tdId+")\"/></td>";
                            regionHtml+="<td>"+mergeRowHtml+"</td>";
                            regionHtml+="<td>"+mergeColHtml+"</td>";
                            regionHtml+="<td align='right' class='dropDownMenu1'><a href='javascript:void(0)'  onclick=\"selectTypeDiv('Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpType"+tdId+"')\" class=\"ui-icon ui-icon-plusthick\" style='text-decoration:none' class=\"calcTitle\" title=\"Add OneViewLet\"></a></td></tr>"
                            regionHtml+="<tr ><td colspan='4' align='right'><div id='GrpType"+tdId+"' style='width: 70px; height: auto; position:relative;background-color: white; overflow: auto; text-align: left; border: 1px solid LightGrey;display: none;'>\n\
                                          <table  ><tr  ><td ><a onclick=\"selectedType1('report','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>Reports</a></td></tr>"+
                  
                        "</table> </div></td></tr>"
//                                          <table border='1' style='border-top:medium hidden;'><tr valign='top' ><td valign='top' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><a onclick=\"selectedType('report','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>Reports</a></td></tr><tr valign='top'><td valign='top' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><a onclick=\"selectedType('measures','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>Measures</a></td></tr><tr valign='top'><td valign='top' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><a onclick=\"selectedType('dashboard','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>KPIs</a></td></tr><tr valign='top' ><td valign='top' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><a onclick=\"selectedType('headline','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>Headlines</a></td></tr><tr valign='top' ><td valign='top' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><a onclick=\"selectedType('styckyNotes','Dashlets-"+tdId+"',"+tdId+","+width+","+height+",'Dashlets"+tdId+"','GrpTyp"+tdId+"','"+oneViewIdValue+"')\" href='javascript:void(0)'>Notes</a></td></tr></table> </div></td></tr>"

                            //absolute; text-align: left; border-right: 1px solid rgb(0, 0, 0); border-width: 0px 1px 1px; border-style: solid; border-color: rgb(0, 0, 0);
                            regionHtml+="</table></div></td>";
                            tdId++;
                        }
                        regionHtml+="</tr>";
                    }
                    regionHtml+="</table>";
                }
                else
                {
                    alert("column number should not exceed 6");

                }
                $.ajax({
                    url: '<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=generateRegions&oneviewID='+oneViewIdValue+'&oneviewLets='+dashId+'&row='+row+'&col='+col+'&prevRow='+prevRow+'&oneviewName='+encodeURIComponent(oneViewName)+'&busRolId='+busroleId+'&innerWidth='+innerWidth+'&height='+height+'&from='+from+'&oneViewType='+oneViewType,
                    success:function(data){}
                });
                prevRow=row;


                $("#regionDivId").dialog('close');
                $('#regionTableId').append('<tr><td>'+regionHtml+'</td></tr>');
            }
            function clearRegion(){
                $('#regionTableId').html("");
                $.ajax({
                    url: '<%=request.getContextPath()%>'+'/reportViewer.do?reportBy=deleteOneViewFromSession&oneviewID='+oneViewIdValue,
                    success:function(data){}
                });
                tdId=0;
                colSp=1;
                rowSp=1;
                row=0;
                prevRow=0;
                width=0;
                height=0;
            }

            function closeOldPortlet(ids, dashletId){
                var confirmText= confirm("Are you sure you want to delete");
                if(confirmText==true){
                    var delDashlet=document.getElementById(dashletId);
                    delDashlet.parentNode.removeChild(delDashlet);

                }
            }

            function mergeRow(tdId,col)
            {
                var RS=parseInt($("#"+tdId+"").attr('rowspan'));
                var CS=parseInt($("#"+tdId+"").attr('colspan'));
                var rowS='';
                var row=parseInt(RS);
                if(row<prevRow){
                    rowS=RS+1;
                    height=(rowS*oneviewHeight)+(rowS*25);
                    $("#"+tdId).attr('rowspan',rowS);
                    $("#"+tdId+"").attr('height',(rowS*oneviewHeight)+(rowS*25)+'px');
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=mergeOneViewRows&oneViewIdValue='+oneViewIdValue+'&dashlet='+tdId+'&rowSpan='+rowS+"&height="+height+'&from='+from,
                        success:function(data){

                        }
                    });
                }
                for(var i=0;i<CS;i++)
                {
                    var delDashlet = (tdId+i) + (col*RS)
                    $("#"+delDashlet).remove("td");
//                    var trRow=document.getElementById(delDashlet);
//                    trRow.parentNode.removeChild(trRow);
                }

            }

            function  mergeColumn(tdId,col,width)
            {
                var CS= parseInt($("#"+tdId).attr('colspan'));
                var RS=parseInt($("#"+tdId+"").attr('rowspan'));
//                alert(CS)
                if(RS==1)
                {
//                  alert('tdId'+tdId+'col'+col)
                    var delDashlet = parseInt(tdId) + parseInt(CS)
//                    var tdCol=$("#"+delDashlet);
                    $("#"+delDashlet).remove('td');
//                    var tdCol=document.getElementById(delDashlet);
//                    tdCol.parentNode.removeChild(tdCol);

                }
                else
                {

                    var delDashlet = tdId + CS;
                    $("#"+delDashlet).remove('td');

                    for(var i=1;i<RS;i++)
                    {
                        delDashlet= (i*col) + (tdId+CS);
                        $("#"+delDashlet).remove('td');
;
                    }
                }
                var colS=parseInt(CS)+1;
                //                alert("Width"+width)
//                alert('width'+width)
                var widthval = colS*width;
//                alert(widthval)
                //                if(tdId==0 || tdId==1 || tdId==2 || tdId==3 || tdId==4)
                //                    $("#"+tdId+"").attr('width',(colS*width)+'px');
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=mergeOneViewColumns&oneViewIdValue='+oneViewIdValue+'&dashlet='+tdId+'&colSpan='+colS+'&delDashletId='+delDashlet+"&widthval="+widthval+'&from='+from,
                    success:function(data){
                        $("#"+tdId+"").attr('colspan',colS);
                        //                        if(!tdId==0 || !tdId==1 || !tdId==2 || !tdId==3 || !tdId==4)
                        $("#"+tdId+"").attr('width',(colS*width)+'px');
                        $("#Dashlet"+tdId+"").attr('width',(colS*width)+'px');
//                        $("#Dashlets"+tdId+"").attr('width',(parseInt(colS*width)-parseInt(60))+'px');
                        //                        $("#"+tdId+"").attr('width',(colS*width)+'px');
                    }
                });


            }
            var measureOpti = ''
            function selectTypeDiv(tdId,col,widt,hight,regionNameId,typeId,roleId,oneviewId,falg){

                //                busroleId = $("#busroleId").val();
                if(roleId!=undefined){
                    busroleId=roleId;
                    oneViewIdValue=oneviewId;
                }
                measureOpti=typeId;
                //                if(busroleId!=''){
                dashletId = tdId;
                colNumber = col;
                var RS=$("#"+col+"").attr('rowspan');
                var CS=$("#"+col+"").attr('colspan');
                if(RS>1 && oneviewHeight!='')
                    height=(RS*oneviewHeight)+(RS*25);
                else
                    height=hight;
//                if(falg!='true')
//                    width=CS*widt;
//                else
                    width =CS*widt;
                regionName=regionNameId
                $("#"+tdId).show();
//                $("#regionId"+colNumber).show();
                //                $("#measureType"+col).show();
                $("#hiddenValue").val(tdId);
                if(document.getElementById(typeId).style.display=='none')
                    document.getElementById(typeId).style.display='';
                else
                    document.getElementById(typeId).style.display='none'

                if(testforRegionOption!=''){
                    if(document.getElementById(testforRegionOption).style.display=='')
                        document.getElementById(testforRegionOption).style.display='none';
                }


            }
            var grapId='';
            var repNameVal='';
            function buildDbrdGraph(reportId,graphId,name2,grpNo,chartname,ch){
$("#graphsId").val(reportId);
//                document.getElementById("regionId"+colNumber).style.display='none'
                grapId=reportId;
                repNameVal=name2;
                var namevalue = name2.replace("_"," ","gi")
                $("#graphName").val(namevalue);
                $("#chartname").val(ch);
                $("#busrolename").val(busrolename);
                var div=document.getElementById(dashletId);
                 $("#regionId"+colNumber).show();
                 if(document.getElementById("readdDivId"+colNumber).style.display=='block')
                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: encodeURI('<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=getReportGraphs&graphId='+graphId+'&reportId='+reportId+'&divId='+colNumber+"&reportBy=viewReport&action=open&width="+width+"&height="+height+"&name="+namevalue+'&oneViewIdValue='+oneViewIdValue+"&busroleId="+busroleId+"&grpNo="+grpNo+"&chartname1="+chartname+"&chartname="+ch+"&busrolename="+busrolename+"&oneviewTime="+oneviewTimecheck+"&repGraph=repGraph"),

                    success: function(data) {
                        if(data != ""){
                             $("#"+dashletId).html("");
                            document.getElementById(dashletId).innerHTML ="";

//                            $("#"+dashletId).html(data);
                              $("#"+colNumber).html(data);
                            $("#Dashlets"+colNumber).html(chartname);

                             generateJsonDataOneview(dashletId,oneViewIdValue,colNumber,ch,"add");
                        }else{
                             $("#"+dashletId).html("");
                             $("#Dashlets"+colNumber).html(chartname);
                            generateJsonDataOneview(dashletId,oneViewIdValue,colNumber,ch,"add");
                        }

                    }
                });
                $("#dialogTestByrep").dialog('close');

            }
            var istranseposse;
            function goSave(reportId,name2,width1,height1,roleid,colno,oneviewid){

            width=width1;
            colNumber=colno;
            dashletId="Dashlets-"+colno;
            height=height1;
            oneViewIdValue=oneviewid;
            busroleId=roleid;
            if(istranseposse=='true' ){
                 istranseposse=undefined;
                buildDbrdTable(reportId,name2);
            }else{
                if(istranseposse==undefined ){
                    istranseposse='true';

           buildDbrdTable1(reportId,name2);
                }
            }


    }
    function buildDbrdTable1(reportId,name2){

//                $("#readdId"+colNumber).show();
//                document.getElementById("GrpTyp"+colNumber).style.display='none'
                grapId=reportId;
                repNameVal=name2;
                var namevalue = name2.replace("_"," ","gi")
                var div=document.getElementById(dashletId);
                if(document.getElementById("readdDivId"+colNumber).style.display=='block')
                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                 $("#regionId"+colNumber).show();
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: encodeURI('<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=getReportTables&reportId='+reportId+"&reportBy=viewReport&action=open&width="+width+"&divId="+colNumber+"&height="+height+"&name="+namevalue+"&oneViewIdValue="+oneViewIdValue+"&busroleId="+busroleId+"&istranseposse="+istranseposse+"&repTable=repTable"),
                    success: function(data) {
                        if(data != ""){

                            $("#"+dashletId).css({'overflow':'auto'});

                            document.getElementById(dashletId).innerHTML ="";
                            document.getElementById(dashletId).innerHTML = data;
                            document.getElementById(regionName).innerHTML=namevalue;

                        }
                    }
                });

                $("#dialogTestByrep").dialog('close');
            }

            function buildDbrdTable(reportId,name2){
//                $("#readdId"+colNumber).show();
//                document.getElementById("GrpTyp"+colNumber).style.display='none'

                grapId=reportId;
                repNameVal=name2;
                var namevalue = name2.replace("_"," ","gi")
                var div=document.getElementById(dashletId);
                if(document.getElementById("readdDivId"+colNumber).style.display=='block')
                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                 $("#regionId"+colNumber).show();
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: encodeURI('<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=getReportTables&reportId='+reportId+"&reportBy=viewReport&action=open&width="+width+"&divId="+colNumber+"&height="+height+"&name="+namevalue+"&oneViewIdValue="+oneViewIdValue+"&busroleId="+busroleId+"&repTable=repTable"),
                    success: function(data) {alert(data)
                        if(data != ""){

                            $("#"+dashletId).css({'overflow':'auto'});

                            document.getElementById(dashletId).innerHTML ="";
                            document.getElementById(dashletId).innerHTML = data;
                            document.getElementById(regionName).innerHTML=namevalue;

                        }
                    }
                });
                istranseposse=undefined;
                $("#dialogTestByrep").dialog('close');
            }

            function buildPortletOneView(Portletid,portletName)
            {

                grapId=Portletid;
                repNameVal=portletName;
                $("#dialogTestByport").dialog('close');
                var pName = portletName.replace("_"," ","gi")
                PortletID=Portletid
                var RS=$("#"+dashletId+"").attr('rowspan');
                var CS=$("#"+dashletId+"").attr('colspan');

                var div=document.getElementById(""+dashletId);
                if(document.getElementById("readdDivId"+colNumber).style.display=='block'){
                    $("#readdDivId"+colNumber).toggle(500);
                }
//                    document.getElementById("readdDivId"+colNumber).style.display=='none';
//                  $("#readdDivId"+colNumber).toggle(500);
                $("#regionId"+colNumber).show();
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

                $.ajax({
                    url:"<%=request.getContextPath()%>/portalViewer.do?portalBy=portletPreview&Portletid="+Portletid+"&portalID=-1&width="+width+"&height="+height+"&divId="+colNumber+"&oneViewIdValue="+oneViewIdValue+"&busroleId="+busroleId+"&pName="+pName+"&repPortal=portlet",
                    success:function(data){
                        if(data!=''){
                            $("#"+dashletId).html(data);
                            document.getElementById(regionName).innerHTML=pName;
                        }
                        else{
                            var display="<tr><td style='color:red'>NO DATA</td></tr>";
                            $("#"+dashletId).html(display);
                            document.getElementById(regionName).innerHTML=pName;
                        }
                    }
                });
            }

//          (tdId,col,widt,hight,regionNameId,typeId,roleId,oneviewId,falg){
            function selectedType1(type,tdId,col,widt,hight,regionNameId,typeId,oneviewId)
            {
                 //var reportIdforsending=new Array();
                // var reportNamesforsending=new Array();
                // var ListArray=new Array
                $(".overlapDiv").hide();
                $("#selectTypeId").dialog('close');
                 if(document.getElementById("readdDivId"+col).style.display=='block'){
                    $("#readdDivId"+col).toggle(500);
                }
                    oneViewIdValue=oneviewId;
                measureOpti=typeId;
                dashletId = tdId;
                colNumber = col;
                var RS=$("#"+col+"").attr('rowspan');
                var CS=$("#"+col+"").attr('colspan');
                if(RS>1 && oneviewHeight!='')
                    height=(RS*oneviewHeight)+(RS*25);
                else
                    height=hight;
                    width =CS*widt;
                regionName=regionNameId
                $("#"+tdId).show();
                if(type=='report'){
                    $("#busNamId").show();
                    $("#dialogTestByrep").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    $("#dialogTestByrep").dialog('open');                   
                }
               
               


            }
            //kruthika
            function getFavreportsinoneview(){


                var reportIdforsending=new Array();
                var  reportNamesforsending=new Array();
                 var ListArray=new Array();
    $.ajax({
                   url: '<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getFavReports',
                success: function(data) {

                    var json=eval('('+data+')');

                    var favReportHtml="";
                    for(var i=0;i<json.length;i++)
                    {
                     reportIdforsending.push(json[i].reportId);
                     reportNamesforsending.push(json[i].reportName);
                    }
                   favReportHtml+="<table><tr><td 'style=padding-top:6px'></td><tr><td style='padding-left:10px'><u><span style='font-size:12px;font-family: Calibri, Calibri, Calibri, sans-serif;font-weight:bold'> Favourite Reports</span></u></td><tr></table><hr style='color:LightGrey;'></hr>";
                  for(var i=0;i<json.length;i++)
                    {
                        favReportHtml+=" <table id='table"+json[i].reportId+"'  <tr> ";
 if(json[i].reportType=='R'){
     favReportHtml+="<td margin-left=2% title='" + json[i].reportName + "'><a  href='reportViewer.do?reportBy=viewReport&fromreport=report&REPORTID="+ json[i].reportId +"&action=open ' target='_blank'  style='color:#336699'>" + json[i].reportName + "</a> </td>";
 }else{
                               // alert("dash")
                            favReportHtml+=" <td title='" + json[i].reportName + "'> <a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='color:#336699' >" + json[i].reportName +"</a> </td>" ;
                            ListArray.push(json[i].reportId);
                            }
                             favReportHtml+="</tr> </table> ";
               }
                     $("#jsonLength").val(json.length);

//                        alert(data);


          $("#listofgraphs").html(favReportHtml);
 $("#listofgraphs").toggle(400);
            }

            });

}
            function selectedType(type,tdId,col,widt,hight,regionNameId,typeId,oneviewId)
            {
          //      alert(tdId);

                $(".overlapDiv").hide();
                $("#selectTypeId").dialog('close');
                 if(document.getElementById("readdDivId"+col).style.display=='block'){
                    $("#readdDivId"+col).toggle(500);
                }
//                    document.getElementById("readdDivId"+colNumber).style.display=='none';
//                 if(oneviewId!=undefined){
//                    busroleId=roleId;
                    oneViewIdValue=oneviewId;
//                }
                measureOpti=typeId;
                //                if(busroleId!=''){
                dashletId = tdId;
                colNumber = col;
                oneviewtime="oneviewTime1"+colNumber;
                reportTime="reportTime1"+colNumber;
                var RS=$("#"+col+"").attr('rowspan');
                var CS=$("#"+col+"").attr('colspan');
                if(RS>1 && oneviewHeight!='')
                    height=(RS*oneviewHeight)+(RS*25);
                else
                    height=hight;
//                if(falg!='true')
//                    width=CS*widt;
//                else
                    width =widt;
                regionName=regionNameId
                $("#"+tdId).show();

                //                var type=$("#typeId").val();
                if(type=='report'){
                    $("#busNamId").show();
                    $("#dialogTestByrep").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 330,
                        position: 'justify',
                        modal: true
                    });
                    $("#dialogTestByrep").dialog('open');
                    istranseposse='';
                }else if(type=='portlet'){
                    $.ajax({
                        url: '<%=request.getContextPath()%>'+'/portalTemplateAction.do?paramportal=checkUserPortalExist&fromModule=ONEVIEW',
                        success: function(data){
                        }
                    });
                    $.ajax({
                        url:'<%=request.getContextPath()%>'+'/portalTemplateAction.do?paramportal=getAllPortletsForOneView',
                        success:function(data)
                        {
                            var jsonVar = eval('('+data+')')
                            var baseIds = jsonVar.baseIds;
                            var baseNames = jsonVar.baseNames;
                            var userTabIds = jsonVar.userTabIds;
                            var userTabNames = jsonVar.userTabNames;
                            var reporthtml='';
                            reporthtml+="<ul>";
                            for(var i=0;i<baseIds.length;i++){
                                reporthtml+="<li><span style='cursor: pointer' onclick=buildPortletOneView('" + baseIds[i] + "','"+baseNames[i].replace(" ", "_", "gi")+"')>" + baseNames[i] + "</span></li>";
                            }
                            reporthtml+="</ul>";
                            $("#portDialogTest").html(reporthtml);
                            $("#dialogTestByport").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                            $("#dialogTestByport").dialog('open');
                        }
                    });
                }else if(type=='headline'){
                    $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlinesforMail',
                    function(data){
                        $("#displayHeadlines").html(data);
                        $("#displayHeadlines").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                        $("#displayHeadlines").dialog('open');
                    });
                }
                else if(type=='measures'){

                    //                    var busrole = ''
                    //                     busrole = $("#busroleId").val();
                    //                     if(busrole!='')
                    //                         busroleId = busrole;

                    //                    if(busroleId!=''){
                    //                    var frameObj=document.getElementById("measureFrameId");
                    //                    var source="<%=request.getContextPath()%>/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                    //                    frameObj.src=source;
                    $("#measureDialogId").dialog('option','title','One View Measures')
                    $("#measureDialogId").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#measureDialogId").dialog('open');
                    //                    }
                    //                    else{
                    //                        alert("Please Select Business Role!");
                    //                     }
                }
                else if(type=='dashboard'){
                    $("#busNamId").show();
                     $("#dialogTestForKpis").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                     $("#dialogTestForKpis").dialog('open');
                }
                else if(type=='styckyNotes'){
                      var noteLabel = prompt("Enter note label","Note");
                      var initialText = "ENTER YOUR NOTES HERE";
                     if(noteLabel != null)
                     {
                     $.ajax({
                        url: 'stickyNoteAction.do?stickyNoteParam=createStickyNote&noteLabel='+noteLabel+'&intText='+initialText,
                         success: function(data){
                          var innerData =$('#oneviewStickNoteSpan').html();
                          document.getElementById(dashletId).innerHTML ="";
                          document.getElementById(dashletId).innerHTML = data;
                          document.getElementById(regionName).innerHTML=noteLabel;
                         }

                    });
//                           document.getElementById('oneviewStickNoteSpan').style.display="";
                  }
                }else if(type=='complexkpi'){
                    $("#complexDialogId").dialog('option','title','One View ComplexKPIs')
                    $("#complexDialogId").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#complexDialogId").dialog('open');
                }
                else if(type=='Date'){
               regDate=$("#dateIdValue").val();
               var regId=tdId.split("-")[1];
//               var trId='measureNavigateTrId'+tdId.split("-")[1];
//                   alert(trId)
//               var reginDate="<td>"+regDate+"</td>"
//             // $("."+trId).append(reginDate);
//
//               //$("#Dashlets"+regIdValue).html(reginDate);
//               alert(regDate)
                    $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getRegDate&regDate='+regDate+'&oneViewIdValue='+oneViewIdValue+'&type='+type+'&height='+height+'&width='+width+'&regId='+regId,
                    function(data){
                       document.getElementById(regId).innerHTML = data;
                       //document.getElementById('date1'+regId).innerHTML = regDate;

                });



            }else if(type=='notes'){
//                alert($("#onveViewByName").html());
                var name=$("#onveViewByName").html();
                var tdNum=dashletId.split("-");
                $.ajax({
                    url: '<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=oneViewNote&divId='+colNumber+"&width="+width+"&height="+height+"&name="+name+'&oneViewIdValue='+oneViewIdValue+"&busroleId="+busroleId,

                    success: function(data) {
//                        alert(data);
                        $("#"+tdNum[1]).html(data);
                    }

            });
            }else if(type=='template'){
            var tdNum=dashletId.split("-");
            $.ajax({
                    url: '<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=buildOneViewTemplate&divId='+colNumber+"&width="+width+"&height="+height+"&name=Template&oneViewIdValue="+oneViewIdValue+"&busroleId="+busroleId,

                    success: function(data) {
//                        alert(data);
                        $("#"+tdNum[1]).html(data);
            }

            });

            }

            }
            function getDataOnComparisonClick(measId,oneViewId,roleId,viewLetId,compareCheck){
                var div=document.getElementById("compareTD");
                div.innerHTML='<img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" >';
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=getDataOnComparisonClick&measId='+measId+'&oneViewId='+oneViewId+'&roleId='+roleId+'&viewLetId='+viewLetId+'&compareCheck='+compareCheck,
                    success: function(data) {
                       $("#compareTD").html(data);
            }
            });
            }
            function kpiRoleTest(){
               busroleId = $("#busrolekpiId").val();
                $.ajax({
                    url:'<%=request.getContextPath()%>/reportTemplateAction.do?templateParam=getKpiDashboards&busroleID='+busroleId,
                    success:function(data)
                    {

                    $("#kpisDialogTest").html(data)
                     $('table#kpisDialogTest li').quicksearch({
                    position: 'before',
                    attached: 'table#kpisDialogTest',
                    loaderText: '',
                    delay: 100
                });


                    }

                    });



            }
            function KpiBuilding(repId,kpitypes,repName,dashletNo,kpiMasterId){
//                 $("#readdId"+colNumber).show();
//                document.getElementById("GrpTyp"+colNumber).style.display='none'

                repNameVal=repName.replace("_"," ","gi");
                var namevalue = repName.replace("_"," ","gi")
                var kpiTypes = kpitypes.replace("_"," ","gi")
                var div=document.getElementById(""+dashletId);
                $("#regionId"+colNumber).show();
                if(document.getElementById("readdDivId"+colNumber).style.display=='block'){
                    $("#readdDivId"+colNumber).toggle(500);
                }
//                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                div.innerHTML='<center><img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';

               $.post('<%=request.getContextPath()%>'+'/dashboardViewer.do?reportBy=viewDashboard&REPORTID='+repId+'&pagename='+repName+'&editDbrd=false&fromOneview=true&dashletId='+dashletNo,
                function(data){
//                    if(data=='Fine'){
//alert(width+''+height);
                      $.ajax({ url: '<%=request.getContextPath()%>'+'/oneViewAction.do?templateParam2=getDashboardKpis&reportId='+repId+'&divId='+colNumber+"&reportBy=viewReport&action=open&width="+width+"&height="+height+"&name="+namevalue+'&oneViewIdValue='+oneViewIdValue+"&busroleId="+busroleId+"&repType=repKpis&dashletNo="+dashletNo+'&kpiMasterId='+kpiMasterId+"&kpiTypes="+kpiTypes,
                                   success: function(data) {
                                   if(data != ""){
//                                      document.getElementById(dashletId).innerHTML ="";
//                                      document.getElementById(dashletId).innerHTML = data;
                                      $("#"+dashletId).html(data);
                                      $("#"+dashletId).css({'overflow':'auto'});
                                      $("#"+regionName).html(namevalue);
//                                      $("#"+kpiMasterId).css({'height':height+'px'});
//                                      $("#"+kpiMasterId).css({'width':width+'px'});
//                                      $("#"+kpiMasterId).css({'overflow':''});
//                                      document.getElementById(regionName).innerHTML=namevalue;

                              }
                         }
                       });
//                     }
                });




                $("#dialogTestForKpis").dialog('close');

            }
            function busroleForMeasure(){
                busroleId = $("#busrolemeasId").val();
                var frameObj=document.getElementById("measureFrameId");
                var source="<%=request.getContextPath()%>/reportViewer.do?reportBy=getMeasuresForOneView&busroleID="+busroleId;
                frameObj.src=source;
            }
            function busroleForComplexKPI(){
//                $("#complexDialogId").dialog('close');
                busroleId = $("#busrolComplexId").val();

                    var source='dashboardTemplateAction.do?templateParam2=getCreateKPIs&divId='+colNumber+'&dashboardId='+oneViewIdValue+'&from=oneview'+'&oneRoleId='+busroleId;
                    var frameObj=document.getElementById("createKPIDivFrame");
                    frameObj.src=source;
            }
var  busrolename;
                          function busroelIdTest(type){
                busroleId = $("#busroleId").val();
                
               busrolename = $("#busroleId option:selected").text();
               alert("rolename................."+busrolename)

                $.ajax({
                    url:'<%=request.getContextPath()%>'+'/reportTemplateAction.do?templateParam=viewByReports&busroleID='+busroleId+'&busrolename='+busrolename,
                    success:function(data)
                    {
                        var jsonVar=eval('('+data+')');
                        var reporthtml='';
                        var tempReportId='null';
                        var first = 0;
                        var second = 1;
                        var repId='';
                        var repName="";
                        var graphId='';
                        var graphName="";
                        reporthtml+="<ul>";
                        var i=0;
                        for(i=0;i<jsonVar.ReportIds.length;i++){
 var ch=1;

                            repId = jsonVar.ReportIds[i];
                            repName = jsonVar.ReportNames[i];
                            graphId = jsonVar.GraphIds[i];
                            graphName = jsonVar.GraphNames[i];
                          
                          graphId=  repId;
                            alert("graphName....."+graphName+"grp..."+graphId+"repName..."+repName);
//                            if (tempReportId == 'null') {
//                                tempReportId = repId;
                                var chname="chart"+ch
//                                //
                                reporthtml+="<li class='closed' id='" + repName + i + "'>";
                                reporthtml+="<img src='icons pinvoke/report.png'></img>";
                                reporthtml+="<span style='font-family:verdana;font-size:8pt' onclick=\"dragGraph('" + repId + "')\">" + repName + "</span>";
                                reporthtml+="<ul id='repName-" + repId + "' style='display:none'>";

var h=i
//i++;
var isflag='true';
                                for(var i1=h;i1<jsonVar.ReportIds.length;i1++){chname="chart"+ch
                                    if(isflag=='true'){
                                         repId = jsonVar.ReportIds[i1];
//                                    repId = jsonVar.ReportIds[h];
                                graphId = jsonVar.ReportIds[i1];
                                 graphName = jsonVar.GraphNames[i1];

                                reporthtml+="<li class='closed' id='" + graphId + "'>";
                                reporthtml+="<img src='icons pinvoke/chart.png'></img>";
                                if(graphName==null ||graphName==""||graphName=="null"){
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + repName + "</a></span>";
                                }else{
                                reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")' >" + graphName + "</a></span>";
                                }
                                reporthtml+="</li>";ch++;chname="chart"+ch
                                }
//                                else{
                                    var repIdOld1 = jsonVar.ReportIds[i1+1];
//                                    repId = jsonVar.ReportIds[h];
                                    var graphId11 = jsonVar.ReportIds[i1+1];
                                 graphName = jsonVar.GraphNames[i1+1];
                      if(repIdOld1==repId ){
 if(graphId11!=''){
                                    reporthtml+="<li class='closed' id='" + graphId + "'>";
                                    reporthtml+="<img src='icons pinvoke/chart.png'></img>";
                                if(graphName==null ||graphName==""||graphName=="null"){
                           reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + repName + "</a></span>";
                        }else{
                                    reporthtml+="<span id='graph-" + graphId + "' style='font-family:verdana;font-size:8pt'><a href='javascript:buildDbrdGraph(" + repId + "," + graphId + ","+'"' + repName + '"'+","+first+","+'"' + graphName + '"'+","+'"' + chname + '"'+")'>" + graphName + "</a></span>";
                                    reporthtml+="</li>";
                                     }ch++
                                     }i++;
                                }else{
                                    break;
                                }
//                                }
                                isflag='false';
                                }

//
                                    reporthtml+="</ul>";
                                    reporthtml+="</li>";
//

                        }


                        reporthtml+="</ul>";
                        reporthtml+="</li>";
                        reporthtml+="</ul>";



                        $("#repDialogTest").html(reporthtml);
                         $('table#repDialogTest li').quicksearch({
                    position: 'before',
                    attached: 'table#repDialogTest',
                    loaderText: '',
                    delay: 100
                });

                        //                            $("#repDialogTest").html("<ul>"+jsonVar+"</ul>");

                        $("#dialogTestByrep").dialog({
                        autoOpen: false,
                        height: 500,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                        $("#dialogTestByrep").dialog('open');
                    }
                });

            }
            function dragGraph(repid,repName){
                if(document.getElementById("repName-"+repid).style.display=='none' )
                    $("#repName-"+repid).show();
                else
                    $("#repName-"+repid).hide();
            }
            function nextStepOfMail()
            {
//                document.getElementById("GrpTyp"+colNumber).style.display='none'
//                $("#readdId"+colNumber).show();

                grapId='HeadLines';
                repNameVal='headline';
                var checkedHeadlines=new Array();
                $(":checkbox:checked").each(
                function() {
                    var a=$(this).val();
                    checkedHeadlines.push(a);
                });
                 $("#regionId"+colNumber).show();
                 if(document.getElementById("readdDivId"+colNumber).style.display=='block'){
                    $("#readdDivId"+colNumber).toggle(500);
                }
//                    document.getElementById("readdDivId"+colNumber).style.display=='none';
                $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=displaycheckedHeadlines&checkedHeadlines='+checkedHeadlines+"&width="+width+"&divId="+colNumber+"&height="+height+"&name="+grapId+"&busroleId="+busroleId+"&oneViewIdValue="+oneViewIdValue+"&repHeadline=headLine",
                function(data){
                    //                        alert(data)
                    document.getElementById(dashletId).innerHTML ="";
                    document.getElementById(dashletId).innerHTML = data;
                    document.getElementById(regionName).innerHTML='HeadLines';
                    initCollapser("");
                });

                $("#displayHeadlines").dialog('close');
            }
            function initCollapser(divId){
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
            function getHeadlineData(id)
            {
                //                 var jason=eval('('+divId+')');
                var ids=id.split(",");
                var headlineId=ids[0];
                var currRow = $("#"+ids[0]);
                $( "#"+headlineId+"prgBar").progressbar({value: 37});
                $.post('<%=request.getContextPath()%>/dataSnapshot.do?doAction=getReportHeadlineData&headlineId='+headlineId,
                function(data){
                    $( "#"+headlineId+"prgBar").remove();
                    if(dashletId!=""){

                        $("#"+dashletId).css({'overflow':'auto'});
                    }

                    $("#"+headlineId+"div").html(data);
//                    $("#parameterRegion").remove();
                    currRow.attr("initialized","true");

                });

            }
            function saveOneview(){
                //                $("#onveViewByName").html(regionName);
                //alert(oneViewIdValue)
                $("#createdId").hide();
                $("#busroleId").hide();
                $("#busroleTdId").hide();
                $("#busNamId").hide();
                $("#clearId").hide();
                var value= document.getElementById("regionTableId");
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=saveingOneviews&reportBy=viewReport&action=save'+'&oneViewIdValue='+oneViewIdValue+'&datetype='+timeDetail,
                function(data){
                    home();
                    alert("Oneview Saved Successfully!")
                });

            }
//            var oneviewName='';
            var oneviewID='';
            var defultdate;
            var  datetype;
            function viewOneBy1(name,idvalue){
                if(parent.$("#BisiessRole").is(":visible")){
                $("#globalFilterDiv").hide();
                }
                 oneviewtypedate1='<%=oneviewtypedate%>'
                $("#moreViewsDiv").dialog('close');
                oneviewID=idvalue;
                oneviewName=name;
                $("#designID").hide();
                $("#designValueId").show();
                $("#footID").show();
                $("#tabs").show();
                $("#designId").show();
                $("#divIdTest").hide();
                $("#divIdTest1").hide();
                $("#dynamicLiId").hide();
                $("#paraminfoId").show();
                $("#saveId").hide();
                $("#onveViewByName").html(name);
//                $("#datedetailId").show();
                 if(oneviewtypedate1=='true' ||oneviewtypedate1=='false'){
                      $("#timedetailsId").hide();
                $("#compareWithId").hide();
                 }else{
                      $("#datedetailId").show();
                $("#timedetailsId").show();
                $("#compareWithId").show();
                  $("#goTabId").show();
            }
                $("#paraminfoId").show();
               $("#schedularrid").show();
                 $("#favreportid").show();
                $("#saveTabId").show();
                $("#refreshId").show();
                $("#addregionsid").show();
                $("#homeId").show();
                $("#oneViewSettings_Id").show();
                $("#oneViewPDF_Id").show();
                $("#gloabalFilter").show();
                $("#oneViewHide_Id").show();
                $("#oneViewMeasure").show();
                $("#fixedtop1").show();
                $("#datedetailId12").show();
                $("#br1").remove("br");
                $("#br2").remove("br");
                var value= document.getElementById("regionTableId");
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $("#designId").css({'height':'400px'})
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=saveOneViewReg&reportBy=viewReport&action=open'+'&oneViewIdValue='+idvalue,
                function(data){
                    if(data!='The File Does not Exist in this System'){
                        try{
                        document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})
                        //sandeep code for date UI on top
//                        if(oneviewtypedate1=='true'){
                         $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getContainerFromSession&oneViewIdValue='+idvalue,
                function(data){

        var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")
            datetype=jsonVar.datetype
            var  timedetails=jsonVar.timedetails
              var  timedetails1=jsonVar.timedetails1
                if(oneviewtypedate1=='false'){

              var  timedetailscf=jsonVar.timedetailscf
              var  timedetails1ct=jsonVar.timedetails1ct

            if(timedetailscf='"'){
 timedetailscf=timedetails;
                  timedetails1ct=timedetails1;
            }
                }
 datetype=datetype[0].replace("[","").replace("]","");
 if(datetype=='PRG_STD'){
   $("#fdatetdspan").hide();
    $("#fdatetdcal").hide();
    $("#totd").hide();
     $("#tdatetdspan").hide();
     $("#tdatetdcal").hide();
      $("#timedetailsId1").show();
                $("#compareWithId1").show();
                $("#sdatetdspan").show();
                $("#sdatetdcal").show();

for(var i=0;i<timedetails.length;i++){
    var day=timedetails[0].replace("[","").replace("]","");
var date=timedetails[1].replace("[","").replace("]","");
var year=timedetails[2].replace("[","").replace("]","");
 $("#stfield1").html(year)
                    $("#stfield2").html(date)
                    $("#stfield3").html(day)

                 $("#defvaluedate").val(timedetails);
}
 }else if(datetype=='PRG_DATE_RANGE'){
      $("#sdatetdspan").hide();
     $("#sdatetdcal").hide();
      $("#timedetailsId1").hide();
               $("#compareWithId1").hide();
      $("#fdatetdspan").show();
      $("#fdatetdcal").show();
      $("#totd").show();
      $("#tdatetdspan").show();
      $("#tdatetdcal").show();
      if(oneviewtypedate1=='false'){
          $("#compare").show();
 $("#cfdatetdspan").show();
      $("#cfdatetdcal").show();
      $("#totd1").show();
      $("#ctdatetdspan").show();
      $("#ctdatetdcal").show();
      }

for(var i=0;i<timedetails.length;i++){
    var day=timedetails[0].replace("[","").replace("]","");
var date=timedetails[1].replace("[","").replace("]","");
var year=timedetails[2].replace("[","").replace("]","");
 $("#pfield1").html(year)
                    $("#pfield2").html(date)
                    $("#pfield3").html(day)
    var day1=timedetails1[0].replace("[","").replace("]","");
var date1=timedetails1[1].replace("[","").replace("]","");
var year1=timedetails1[2].replace("[","").replace("]","");
 $("#tdfield1").html(year1)
                    $("#tdfield2").html(date1)
                    $("#tdfield3").html(day1)
                     $("#defvaluedate").val(timedetails);

                       if(oneviewtypedate1=='false'){
    var daycf=timedetailscf[0].replace("[","").replace("]","");
var datecf=timedetailscf[1].replace("[","").replace("]","");
var yearcf=timedetailscf[2].replace("[","").replace("]","");
 $("#cffield1").html(yearcf)
                    $("#cffield2").html(datecf)
                    $("#cffield3").html(daycf)
                     $("#defvaluedate").val(timedetails);
    var dayct=timedetails1ct[0].replace("[","").replace("]","");
var datect=timedetails1ct[1].replace("[","").replace("]","");
var yearct=timedetails1ct[2].replace("[","").replace("]","");
 $("#ctfield1").html(yearct)
                    $("#ctfield2").html(datect)
                    $("#ctfield3").html(dayct)
                     $("#defvaluedate").val(timedetails);

                       }
}

defultdate=timedetails1;
}

  $("#goTabId1").show();

                        });
//                        }else{
//                            $("#oldonedate").show();
//                            $("#dateToggle").hide();
//                        }
//                        document.getElementById("regionTableId").innerHTML=data;
                        $("#regionTableId").html(data);
                        buildd3charts(idvalue)

                        var datevalue = $("#dateIdValue").val();
                        var durationvale = $("#durationId").val();
                        var compareValue = $("#comareWithId").val();
                        $("#dateId").val(datevalue);
                        $('#timedetailselectId option[value="'+durationvale.toString()+'"]').attr('selected','selected');
                        $('#compareSelectId option[value="'+compareValue.toString()+'"]').attr('selected','selected');
                        $('#timedetailselectId1 option[value="'+durationvale.toString()+'"]').attr('selected','selected');
                        $('#compareSelectId1 option[value="'+compareValue.toString()+'"]').attr('selected','selected');
                        //alert($('#oneSetting_Hidden').val());
                        $("#one_"+$('#oneSetting_Hidden').val()).attr('checked',true);
//                        alert($('#oneSetting_EveryTime').val());
                        var iseveryTime=$('#oneSetting_EveryTime').val();
                        if(iseveryTime=='true' ||iseveryTime==true){
                           $("#one_EveryTime").attr('checked',true);
                        }else{
                            $("#one_EveryTime").attr('checked',false);
                        }

                        initCollapser("");
                        }catch(e){
                            if(e == 'No Data'){
                                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getTimeDetails&oneViewId='+idvalue,
                                function(data){
                                    $("#timeDetailsDiv").html(data)
                                    var datevalue = $("#dateIdValue").val();
                                    var durationvale = $("#durationId").val();
                                    var compareValue = $("#comareWithId").val();
                                    $("#dateId").val(datevalue);
                                    $('#timedetailselectId option[value="'+durationvale.toString()+'"]').attr('selected','selected');
                                    $('#compareSelectId option[value="'+compareValue.toString()+'"]').attr('selected','selected');
                                    //alert($('#oneSetting_Hidden').val());
                                    $("#one_"+$('#oneSetting_Hidden').val()).attr('checked',true);
            //                        alert($('#oneSetting_EveryTime').val());
                                    var iseveryTime=$('#oneSetting_EveryTime').val();
                                    if(iseveryTime=='true' ||iseveryTime==true){
                                       $("#one_EveryTime").attr('checked',true);
                                    }else{
                                        $("#one_EveryTime").attr('checked',false);
                    }
                                });

                            }
                        }
                    }
                    else{
                                var currentTime = new Date()
                                var month = currentTime.getMonth() + 01
                                var day = currentTime.getDate()
                                var year = currentTime.getFullYear()
                        $("#designId").css({'height':'400px'})
                        $("#designId").css({'padding-bottom':''})
                        var display="<center><font style='color:red;font-size:15px;'>The file does not exist in this System</font></center>";
                        document.getElementById("designId").style.display='';
//                        document.getElementById("regionTableId").innerHTML=display;
                        $("#regionTableId").html(display);
                        var datevalue = $("#dateIdValue").val();
                        var durationvale = $("#durationId").val();
                        var compareValue = $("#comareWithId").val();
                        $("#dateId").val(day+"/"+month+"/"+year);
                        $("#paraminfoId").hide();
                        $('#timedetailselectId option[value="Month"]').attr('selected','selected');

//                        $('#compareSelectId option[value="'+compareValue+'"]').attr('selected','selected');
                    }
                    var size;
                     var visible;
       $.ajax({
         url:  parent.ctxpath+'/oneViewAction.do?templateParam2=NoOfRegions&oneViewIdValue='+oneviewID+'&icons=al',
         success:function(data){

         var  datalist = new Array();
         datalist = data.toString().split(",");
         size=datalist[0].replace("[","").replace("]","");
         visible=datalist[1].replace("[","").replace("]","").trim();
          var isPowerAnalyserEnableforUser='<%=isPowerAnalyserEnableforUser%>'

      if(visible!='hide'){
     for(var j=0;j<size;j++){
       $("#refreshTabId"+j).show();
       $("#saveTabId"+j).show();
       $("#optionId"+j).show();
       $("#optionIds"+j).show();
       if(oneviewtypedate1=='true'){
 $("#alertTabId"+j).hide();
       parent.$("#measureNavigateId"+j).hide();
                        parent.$("#relatedMeasureInfoId"+j).hide();
       }else{
          $("#alertTabId"+j).show();
           $("#measureNavigateId"+j).hide();
                      $("#relatedMeasureInfoId"+j).hide();
       }
       hideicons(j)
         }
             }
       else{ for(var j=0;j<size;j++){
               if(oneviewtypedate1=='true'){
               }else{
       $("#refreshTabId"+j).hide();
       $("#saveTabId"+j).hide();
       $("#optionId"+j).hide();
       $("#optionId1"+j).hide();
       $("#alertTabId"+j).hide()
               }
                hideicons(j)
         }

                              }

             if(isPowerAnalyserEnableforUser == 'false') { //kruthika
           for(var j=0;j<size;j++){
        $("#refreshTabId"+j).show();
       $("#saveTabId"+j).hide();
       $("#optionId"+j).hide();
       $("#optionIds"+j).hide();
       $("#alertTabId"+j).hide();
         }
             }

       }
                });

                });

            }
            function getRemainingOneViews(viewBysSize,viewByNames,viewByIds){
                $("#moreViewsDiv").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                    var  name = new Array();
                       name = viewByNames.toString().split(",");
                    var  id = new Array();
                      id  = viewByIds.toString().split(",");
                      var htmlVar = "<table>";
                 for(var j=0;j<viewBysSize;j++){
                     htmlVar += "<tr><td><a onclick=\"viewOneBy('"+name[j].replace("]", "", "gi").replace("[", "", "gi")+"','"+id[j].replace("]", "", "gi").replace("[", "", "gi").replace(" ", "", "gi")+"')\" title='One ViewBy'>"+name[j].replace("]", "", "gi").replace("[", "", "gi")+"</a></td></tr>";
                 }
                 htmlVar += "</table>";
                 $("#moreViewsDiv").html(htmlVar);
                 $("#moreViewsDiv").dialog('open');
            }
            function oneViewBys(){
                //                $("#showNameId").hide();
                $("#designID").hide();
                $("#designValueId").show();
                $("#footID").show();
                $("#tabs").show();
                $("#designId").show();
                $("#divIdTest").hide();
                $("#divIdTest1").hide();
                $("#dynamicLiId").hide();
                $("#paraminfoId").hide();
            }
            function refreshOneview(){
              // alert('refresh');
               var value= document.getElementById("regionTableId");
               //alert(value)
               value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
               $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=refreshOneView&reportBy=viewReport&action=open'+'&oneViewIdValue='+oneviewID+'&oneviewName='+oneviewName,
                function(data){
               if(data!='The File Does not Exist in this System'){
               document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})
//                        document.getElementById("regionTableId").innerHTML=data;
                        $("#regionTableId").html(data);
                        var datevalue = $("#dateIdValue").val();
                        var durationvale = $("#durationId").val();
                        var compareValue = $("#comareWithId").val();
                        $("#dateId").val(datevalue);
                        $('#timedetailselectId option[value="'+durationvale.toString()+'"]').attr('selected','selected');
                        $('#compareSelectId option[value="'+compareValue.toString()+'"]').attr('selected','selected');
                        initCollapser("");
                    }
                    else{
                                var currentTime = new Date()
                                var month = currentTime.getMonth() + 01
                                var day = currentTime.getDate()
                                var year = currentTime.getFullYear()
                        $("#designId").css({'height':'400px'})
                        $("#designId").css({'padding-bottom':''})
                        var display="<center><font style='color:red;font-size:15px;'>The file does not exist in this System</font></center>";
                        document.getElementById("designId").style.display='';
//                        document.getElementById("regionTableId").innerHTML=display;
                        $("#regionTableId").html(display);
                        var datevalue = $("#dateIdValue").val();
                        var durationvale = $("#durationId").val();
                        var compareValue = $("#comareWithId").val();
                        $("#dateId").val(day+"/"+month+"/"+year);
                        $("#paraminfoId").hide();
                        $('#timedetailselectId option[value="Month"]').attr('selected','selected');
//                        $('#compareSelectId option[value="'+compareValue+'"]').attr('selected','selected');
                    }
                });
           }
            function gotForOneview(action1){
                if(parent.$("#BisiessRole").is(":visible")){
                $("#globalFilterDiv").hide();
                }
                var version =$("#oneviewversion").val();
if(version !='' && version=='2.5'){
var formisgo='true';
}else{
var formisgo='null';
}
                var innerWidth=parseInt(window.innerWidth)-120;
                 <%String oneviewtypedatego =(String) session.getAttribute("oneviewdatetype");%>
                          var  oneviewtypedate1='<%=oneviewtypedatego%>'
                if(oneviewtypedate1=='true'||oneviewtypedate1=='false'){
                var date = $("#stdate").val();
             var isdefult;

                var date1 = $("#dateId1").val();
                var duration1 = $("#todate").val();
                var comparefrom = $("#comparefrom").val();
                var compareto = $("#compareto").val();
// by sandeep code for range and std date
                if((date!='')||(date1!='' && duration1!='')){
                         if(datetype=='PRG_DATE_RANGE'){
                          if(date1==''){
                              isdefult='date1';
                            date1 =$("#defvaluedate").val();
                          }if(duration1==''){
                               isdefult='duration1';
                              duration1= defultdate;
                          }
                      }


                }else{
                    isdefult='true';

                     if(datetype=='PRG_DATE_RANGE'){
                         duration1= defultdate;
                         date1 =$("#defvaluedate").val();
                     }else{
                    date =$("#defvaluedate").val();
                     }

                }
                var duration = $("#timedetailselectId1").val();
                var compare = $("#compareSelectId1").val();
                }else{
                 var date = $("#dateId").val();
                  var duration = $("#timedetailselectId").val();
                var compare = $("#compareSelectId").val();
                }
                var value= document.getElementById("regionTableId");
                var action1=action1;
                var confirmText='';
                if(action1=='Save'){
                     confirmText= confirm("These Changes Are Permanently Stored.\n Do You Want to Save?");
                }
                else {
                    confirmText=true;
                }
                if(confirmText==true){
                $("#designId").css({'height':'400px'})
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getingOneviews&reportBy=viewReport&oneViewIdValue='+oneviewID+'&oneviewName='+oneviewName+'&date='+date+'&date1='+date1+'&duration='+duration+'&duration1='+duration1+'&compare='+compare+'&formisgo='+formisgo+'&innerWidth='+innerWidth+'&action1='+action1+'&isdefult='+isdefult+'&comparefrom='+comparefrom+'&compareto='+compareto+'&fromoneviewspeedreduce=true&action=GO',
                function(data){
                    if(data!='NO DATA'){
                        document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})
//                        document.getElementById("regionTableId").innerHTML=data;
                        $("#regionTableId").html(data);
                          buildd3charts(oneviewID);


                         var size;
                     var visible;
       $.ajax({
         url:  parent.ctxpath+'/oneViewAction.do?templateParam2=NoOfRegions&oneViewIdValue='+oneviewID+'&icons=al',
         success:function(data){

         var  datalist = new Array();
         datalist = data.toString().split(",");
         size=datalist[0].replace("[","").replace("]","");
         visible=datalist[1].replace("[","").replace("]","").trim();
          var isPowerAnalyserEnableforUser='<%=isPowerAnalyserEnableforUser%>'

      if(visible!='hide'){
     for(var j=0;j<size;j++){
       $("#refreshTabId"+j).show();
       $("#saveTabId"+j).show();
       $("#optionId"+j).show();
       $("#optionIds"+j).show();
       if(oneviewtypedate1=='true'){
 $("#alertTabId"+j).hide();
       parent.$("#measureNavigateId"+j).hide();
                        parent.$("#relatedMeasureInfoId"+j).hide();
       }else{
          $("#alertTabId"+j).show();
         $("#measureNavigateId"+j).hide();
                       $("#relatedMeasureInfoId"+j).hide();
       }
         }
             }
       else{ for(var j=0;j<size;j++){
               if(oneviewtypedate1=='true'){
               }else{
       $("#refreshTabId"+j).hide();
       $("#saveTabId"+j).hide();
       $("#optionId"+j).hide();
       $("#optionId1"+j).hide();
       $("#alertTabId"+j).hide()
               }
         }
         $("#measureNavigateId"+j).hide();
                      $("#relatedMeasureInfoId"+j).hide();
                              }
             if(isPowerAnalyserEnableforUser == 'false') { //kruthika
           for(var j=0;j<size;j++){
        $("#refreshTabId"+j).show();
       $("#saveTabId"+j).hide();
       $("#optionId"+j).hide();
       $("#optionIds"+j).hide();
       $("#alertTabId"+j).hide();
         }
             }

       }
                });
                        initCollapser("");
//                         home();
                    }
                    else{
                        $("#designId").css({'height':'400px'})
                        $("#designId").css({'padding-bottom':''})
                        var display="<tr><td height='300px' align='center' >NO DATA</td></tr>";
                        document.getElementById("designId").style.display='';
                         $("#regionTableId").html(display);
//                        document.getElementById("regionTableId").innerHTML=display;

                    }
                });
            }
            }
            var regiontdid='';
            var colNumber='';
            var regionnameId='';

            function renameRegion(regionTdid,colNo,regionNameId,onviewId){
                regiontdid=regionTdid;
                colNumber=colNo;
                regionnameId=regionNameId;
                oneViewIdValue=onviewId;
//                $("#"+testforRegionOption).hide();
             $(".overlapDiv").hide();
             if(document.getElementById("reigonOptionsDivId"+colNo)!=null && document.getElementById("reigonOptionsDivId"+colNo).style.display=='block'){
                 $("#reigonOptionsDivId"+colNo).toggle(500);
             }
                 $("#renameDivId").dialog({
                        autoOpen: false,
                        height: 110,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                $("#renameDivId").dialog('open');
            }
            var rename=''
            function renamingfunction(){
                var rename=$("#renameId").val();
                var renameMeasure=encodeURIComponent(rename);
                var wdth=document.getElementById(regionnameId.replace("Dashlets","")).offsetWidth;
                var length=(wdth)/String(rename).length;
                if($("#forDillDown"+colNumber).val()==null){

                 $("#"+regionnameId).html(rename.substring(0,length)+"..");

                }
                else{

                  $("#forDillDown"+colNumber).html(rename.substring(0,length)+"..");
                }

                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=renameOneviewBys&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&rename='+renameMeasure,
                function(data){

                });

                $("#renameId").val('');
                $("#renameDivId").dialog('close');
            }

            function measureComments(regioId,onevieId){
                colNumber=regioId;
                oneViewIdValue=onevieId;
                if(document.getElementById("reigonOptionsDivId"+regioId)!=null && document.getElementById("reigonOptionsDivId"+regioId).style.display=='block'){
                 $("#reigonOptionsDivId"+regioId).toggle(500);
                }

              var frameObj = document.getElementById("oneviewCommentFrameId");

             frameObj.src = "kpiTableComments.jsp?oneViewIdValue="+oneViewIdValue+'&colNo='+colNumber+'&formOneview=true';
             $("#commentsAreaId").dialog({
                       autoOpen: false,
                        height: 350,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
             $("#commentsAreaId").dialog('open');
            }
            function commentsAreaValues(){
                var textarea=$("#usertextareaId").val();
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=commentsData&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&commentData='+textarea,
                function(data){
                });
                $("#commentsAreaId").dialog('close');
            }
            var currVlaue='';
            var changValue='';
            var measrueId='';
            var measrureName='';
            var proiorValue =''
            function measureOptions(regioId,onevieId,currentVal,changeValue,measrId,measurName,priorValue){
                $(".overlapDiv").hide();
                var measType=$("#measureTypeId").val();
                 var measurecolor='';
                var measureColor='';
                var islogicalColorApplied=false;

               if(document.getElementById("measureType"+regioId)!=null){
//                alert(document.getElementById("measureType"+regionId).style.display)
                if(document.getElementById("measureType"+regioId).style.display=='')
                    document.getElementById("measureType"+regioId).style.display='none';
                }
                if(document.getElementById("reigonOptionsDivId"+regioId)!=null && document.getElementById("reigonOptionsDivId"+regioId).style.display=='block'){
                 $("#reigonOptionsDivId"+regioId).toggle(500);
                }

                colNumber=regioId;
                oneViewIdValue=onevieId;
                currVlaue=currentVal;
                changValue=changeValue;
                measrueId=measrId;
                measrureName=measurName;
                proiorValue = priorValue;
                isDialEnabled=false;
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getMeasureOptionData&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber,
                function(data){

                    var jsonValue=eval('('+data+')');
                   // alert(jsonValue.MeasureOptions[0])
                    if(jsonValue.MeasureOptions[0]!='No Values'){

                        if(jsonValue.MeasureOptions[10] != 'null' && ($.trim(jsonValue.MeasureOptions[10])=='true' || jsonValue.MeasureOptions[10])){
                           var groupColor=jsonValue.MeasureOptions[10];
                           if($.trim(groupColor)== 'false'){
                               $("#DefineDialChartId").val('FALSE');
                               $("#DefineDialChartDiv").hide();
                           } else{
                               evalDialChartRanges(jsonValue);

                           }
                        }else{

                        $('#roundId option[value='+jsonValue.MeasureOptions[0]+']').attr('selected','selected');
                        $('#NbrFormatId option[value='+jsonValue.MeasureOptions[1]+']').attr('selected','selected');
                        $('#measureTypeId option[value='+jsonValue.MeasureOptions[2]+']').attr('selected','selected');
                        if(jsonValue.MeasureOptions[3]!=null){
                            trendbg=jsonValue.MeasureOptions[3];

                    }
                    if(jsonValue.MeasureOptions[4] != 'null' && jsonValue.MeasureOptions[4] == 'preCust'){
                            $('#prefixSelect option[value='+jsonValue.MeasureOptions[4]+']').attr('selected','selected');
                            $("#prefixes").show();
                            $("#prefixVal").val(jsonValue.MeasureOptions[5]);
                        }else if(jsonValue.MeasureOptions[5] != 'null'){
                            $("#prefixes").hide();
                            var val = jsonValue.MeasureOptions[5]
                            $('#prefixSelect option[value="'+val+'"]').attr('selected','selected');
                        }else{
                            $("#prefixes").hide();
                            $('#prefixSelect option[value=]').attr('selected','selected');
                    }
                    if(jsonValue.MeasureOptions[6] != 'null' && jsonValue.MeasureOptions[6] == 'sufCust'){
                            $('#suffixSelect option[value='+jsonValue.MeasureOptions[6]+']').attr('selected','selected');
                            $("#suffixes").show();
                            $("#suffixVal").val(jsonValue.MeasureOptions[7]);
                        }else if(jsonValue.MeasureOptions[7] != 'null'){
                            $("#suffixes").hide();
                            var val = jsonValue.MeasureOptions[7];
                            $('#suffixSelect option[value="'+val+'"]').attr('selected','selected');
                        }else{
                            $("#suffixes").hide();
                            $('#suffixSelect option[value=]').attr('selected','selected');
                        }
                        if(jsonValue.MeasureOptions[8] != 'null'){
                            numdays=jsonValue.MeasureOptions[8];
                            var dayVal=parseInt(currVlaue.replace(",", "", "gi"))/parseInt(numdays);

                            $('input[id="measreIdVal"]').val(currVlaue);
                            $('input[id="measureDayVal"]').val(parseInt(dayVal));
                            $("#perTotalDays").html(numdays);
                            $("#LogicalmeasureName").html(measrureName);
                        }
                        if(jsonValue.MeasureOptions[9] != 'null'){
                           var groupColor=jsonValue.MeasureOptions[9];
                           if($.trim(groupColor)== 'false'){
                               $("#logicalChecked").val('FALSE');
                               $("#logicalColorDiv").hide();
                           } else{
                               evalRangeColor(jsonValue);

                           }
                        }
                        }

                   if(measureColor == "" && jsonValue.MeasureOptions[2] == 'Standard' && currentVal.toString().indexOf("-") != -1){
                       measurecolor = "red";
                       measureColor = "red";
                       $("#measureRed").attr("checked",true);
                   }  else if(measureColor == "" && jsonValue.MeasureOptions[2] == 'Standard'){

                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }
                   if(measureColor == "" && jsonValue.MeasureOptions[2] == 'Non-Standard' && currentVal.toString().indexOf("-") != -1){

                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }  else if(measureColor == "" && jsonValue.MeasureOptions[2] == 'Non-Standard'){

                       measurecolor = "red";
                       measureColor = "red";
                       $("#measureRed").attr("checked",true);
                   }
                   if(document.getElementById("measureblue") != null && document.getElementById("measureblue").checked){
                      measurecolor="#336699";
                 measureColor=encodeURIComponent(measurecolor);
                }
                else if(document.getElementById("measureRed") != null && document.getElementById("measureRed").checked){
                      measurecolor="#FF0000";
                 measureColor=encodeURIComponent(measurecolor);
                }
                   else if(document.getElementById("measureGreen") != null && document.getElementById("measureGreen").checked){
                      measurecolor="#008000";
                 measureColor=encodeURIComponent(measurecolor);
                  }
                      else if(document.getElementById("measureColor") != null && document.getElementById("measureColor").checked){
                      measurecolor=$("#measureColorCode").val();
                 measureColor=encodeURIComponent(measurecolor);
                  }
                  if(document.getElementById("logicalChecked") != null && document.getElementById("logicalChecked").checked){
                      islogicalColorApplied=true;

                        measurecolor=getColorForMeasureVal();


                      measureColor=encodeURIComponent(measurecolor);
                  }
                    }else{
                        $('#roundId option[value=0]').attr('selected','selected');
                        $('#NbrFormatId option[value=]').attr('selected','selected');
                        $('#measureTypeId option[value=Standard]').attr('selected','selected');
                        $("#measureGreen").attr("checked",true);
                        $('#prefixSelect option[value=]').attr('selected','selected');
                        $('#suffixSelect option[value=A]').attr('selected','selected');
                        trendbg='#357EC7'
                    }
                });
                 $("#measureOptionsId").dialog({
                        autoOpen: false,
                        height: 380,
                        width: 650,
                        position: 'justify',
                        modal: true
                    });
                $("#measureOptionsId").dialog('open');
            }
            function measureOption(){
                var roundVal=$("#roundId").val();
                var numbrFrmt=encodeURIComponent($("#NbrFormatId").val());
                var measType=$("#measureTypeId").val();
                var chaValue=parseFloat(changValue);
                var trendColor=encodeURIComponent($("#trendColorCode").val());
                var fontColor=encodeURIComponent($("#fontColorCode").val());
                var measurecolor='';
                var measureColor='';
                var customizePrefix = '';
                var customizeSuffix = '';
                var prefixSelect = encodeURIComponent($("#prefixSelect").val());
                var suffixSelect = encodeURIComponent($("#suffixSelect").val());
                var islogicalColorApplied=false;
                var colorMap=new Object();
               // alert(isDialEnabled);
                var isDialChart=false;
                var loadImg='<img id="loadImg" width="15px" height="15px" src=\"<%=request.getContextPath()%>/images/ajax-loader.gif\" align="right"  width="5px" height="5px"  style="position:absolute;float:right;margin-left:45px;" >';
                $("#Dashlets"+colNumber).append(loadImg);
                if(isDialEnabled==true && ($("#dialSelectId").val()=='no')){
                    //alert('exixtence chart');
                      buildExixtentMeasure(colNumber);

               }else if($("#dialSelectId").val()=='yes'){
                    //alert('dialChart')
                      isDialChart=true;
                      buildDialChart(colNumber,chaValue);
                      $("#loadImg").remove();

         }else{
             //alert('Normal Form');
                if(prefixSelect == "preCust"){
                    customizePrefix = prefixSelect;
                   prefixSelect = encodeURIComponent($("#prefixVal").val());
                }
                if(suffixSelect == "sufCust"){
                    customizeSuffix = suffixSelect;
                   suffixSelect = encodeURIComponent($("#suffixVal").val());
                }

                 if(document.getElementById("measureblue") != null && document.getElementById("measureblue").checked){
                      measurecolor="#336699";
                 measureColor=encodeURIComponent(measurecolor);
                }
                else if(document.getElementById("measureRed") != null && document.getElementById("measureRed").checked){
                      measurecolor="#FF0000";
                 measureColor=encodeURIComponent(measurecolor);
                }
                   else if(document.getElementById("measureGreen") != null && document.getElementById("measureGreen").checked){
                      measurecolor="#008000";
                 measureColor=encodeURIComponent(measurecolor);
                  }
                      else if(document.getElementById("measureColor") != null && document.getElementById("measureColor").checked){
                      measurecolor=$("#measureColorCode").val();
                 measureColor=encodeURIComponent(measurecolor);
                  }
                    else if(document.getElementById("measurecolorcoon") != null && document.getElementById("measurecolorcoon").checked){
//                      measurecolor=$("#measureColorCode").val();alert(measureColor);
              if(measType == 'Standard' && currVlaue.toString().indexOf("-") != -1){
                       measurecolor = "red";
                       measureColor = "red";
                       $("#measureRed").attr("checked",true);
                   }  else if(measType == 'Standard'){

                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }
                   if(measType == 'Non-Standard' && currVlaue.toString().indexOf("-") != -1){

                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }  else if(measType == 'Non-Standard'){

                       measurecolor = "red";
                       measureColor = "red";
                       $("#measureRed").attr("checked",true);
                   }
                 measureColor=encodeURIComponent(measurecolor);
                  }
                  if(document.getElementById("logicalChecked") != null && document.getElementById("logicalChecked").checked){
                      islogicalColorApplied=true;
                         for(var i=0;i<3;i++){
                           var condition=$("#LogicalOperator"+i).val();
                           var range1=$("#range"+i+"_1").val();
                           var range2=$("#range"+i+"_2").val();
                           var color=$("#logicalColorId_"+i).attr("colorcode");
                           var mapDetails=condition+'~'+range1+'~'+range2+'~'+color;
                           colorMap["color"+i]=mapDetails;
                        }
                        measurecolor=getColorForMeasureVal();
                        colorMap=encodeURIComponent(JSON.stringify(colorMap));

                      measureColor=encodeURIComponent(measurecolor);
                  }

                //fontColor1=$("#fontColorCode").val()
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=measureOptionVale&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber
                    +'&roundVal='+roundVal+'&numbrFrmt='+numbrFrmt+'&measType='+measType+'&currentVal='+currVlaue+'&measrureName='+measrureName
                    +'&measrueId='+measrueId+"&proiorValue="+proiorValue+"&trendColor="+trendColor+"&measureColor="+measureColor
                    +"&prefixSelect="+prefixSelect+"&suffixSelect="+suffixSelect+'&customizePrefix='+customizePrefix+'&customizeSuffix='
                    +customizeSuffix+'&islogicalColorApplied='+islogicalColorApplied+'&colorMap='+colorMap,
                function(data){
                    var jsonCurPriorVal = eval('('+data+')');
                   var currValue = jsonCurPriorVal.MeasureCurrPriorValue[0];
                   var priorValue = jsonCurPriorVal.MeasureCurrPriorValue[1];
                   if(measurecolor == '' && measureColor == "" && measType == 'Standard' && currValue.toString().indexOf("-") != -1){
                       measurecolor = "#FF0A0A";
                       measureColor = "#FF0A0A";
                   }  else if(measureColor == "" && measType == 'Standard' && currValue.toString().indexOf("-") != -1){
                       measurecolor = "red";
                       measureColor = "red";

                       $("#measureRed").attr("checked",true);
                   }  else if(measureColor == "" && measType == 'Standard'){

                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }
                   if(measureColor == "" && measType == 'Non-Standard' && currValue.toString().indexOf("-") != -1){
                       measurecolor = "green";
                       measureColor = "green";
                       $("#measureGreen").attr("checked",true);
                   }  else if(measureColor == "" && measType == 'Non-Standard'){

                       measurecolor = "red";
                       measureColor = "red";
                       $("#measureRed").attr("checked",true);
                   }

        //code written by veena
        if(islogicalColorApplied){
            //alert()
            for(var i=0;i<3;i++){
                    var condition=$("#LogicalOperator"+i).val();
                    var range1=$("#range"+i+"_1").val();
                    //var range2=$("#range"+i+"_2").val();
                    var color=$("#logicalColorId_"+i).attr("colorcode");
                    //if(measType == 'Standard'){
                     if(operator == '>'){
            //alert(operator)
            if(currVal> parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<'){
            if(currVal < parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                alert(color)
                break
            }


        }
        if(operator == '='){
            //alert(operator)
            if(currVal == parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '>='){
            //alert(operator)
            if(currVal >= parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<='){
            //alert(operator)
            if( currVal <= parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '!='){
            //alert(operator)
            if(currVal != parseFloat(range1Val)){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
        if(operator == '<>'){
            //alert(operator)
            var range2Val="";
            var range2=$("#range"+i+"_2").val();
          //  var range2Val=parseFloat(parseFloat(range2)*parseFloat(parent.numdays));
            if(currVal > range1Val && currVal < range2Val){
                color=$("#logicalColorId_"+i).attr("colorcode");
                break
            }
        }
            }
            //}
        }
   var mdVal = '';
                   suffixSelect = decodeURIComponent(suffixSelect);
                   prefixSelect = decodeURIComponent(prefixSelect);
                   if(suffixSelect=='K'){
                      mdVal = 'K';
                   }else if(suffixSelect=='M'){
                       mdVal = 'Mn';
                   }else if(suffixSelect=='L'){
                       mdVal = 'Lkh';
                   }else if(suffixSelect=='Cr'){
                       mdVal = 'Crs';
                   }else{
                       mdVal = suffixSelect;
                   }
              //      if(numbrFrmt=='K'){
                        if(measType=='Standard'){
                            if($("#measureValue"+colNumber).val()!=null){
//                            if(document.getElementById("mesCompare"+colNumber)==null || document.getElementById("mesCompare"+colNumber).checked){
                         if(chaValue>0.0 && measType=='Standard' && currValue!=priorValue){
                             $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' style='color:#008000'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                             $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else if(chaValue<0.0 && measType=='Standard'){
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' style='color:#FF0000;'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else if(currValue==priorValue){
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else{
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
//                            }
//                          else{
//                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'>" +currValue.toString().replace("K","")+ " <sub>K</sub></a>");
//                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'>" +currValue.toString().replace("K","")+ " <sub>K</sub></a>");
//                          }
                            }
                            else{
                                $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                                $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                            }
                        }
                        else if(measType=='Non-Standard'){
                            if($("#measureValue"+colNumber).val()!=null){
//                             if(document.getElementById("mesCompare"+colNumber)!=null && document.getElementById("mesCompare"+colNumber).checked){
                            if(chaValue>0.0 && measType=='Non-Standard' && currValue!=priorValue){
                             $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' style='color:#FF0000;'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                             $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else if(currValue==priorValue){
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else if(chaValue<0.0 && measType=='Non-Standard'){
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' style='color:#008000'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
                          else{
                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' ><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                          }
//                             }
//                             else{
//                              $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'>" +currValue.toString().replace("K","")+ " <sub>K</sub></a>");
//                              $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'>" +currValue.toString().replace("K","")+ " <sub>K</sub></a>");
//                             }
                        }
                        else{
                            $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                            $("#currValfirst"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)'><span style='color:"+measurecolor+";font-size:18pt;'>" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></span></a>");
                        }
                        }
//                        else{
//                            $("#currVal"+colNumber).html("<a onclick=\"getMeasureGraph('"+measrueId+"','"+measrureName+"')\" href='javascript:void(0)' >" +currValue.toString().replace("K","")+ "<sub>k</sub></a>");
//                        }
                        $("#currValue"+colNumber).html(prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+"</span>");
                        $("#currValueFourth"+colNumber).html("<font style='color: white;'>"  +prefixSelect+ currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+ "</span></font>")
                        if(priorValue!='null'){
                            $("#priorValue"+colNumber).html(priorValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+"</span>");
                            $("#priorValueFourth"+colNumber).html("<font style='color: white;'>"  + prefixSelect+priorValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+ "</span></font>")
                    }

                    $("#currValue"+colNumber).html(prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+"</span>");
                        $("#currValueFifth"+colNumber).html("<font style='color: white;'>"  +prefixSelect+ currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+ "</span></font>")
                        if(priorValue!='null'){
                            $("#priorValue"+colNumber).html(priorValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+"</span>");
                            $("#priorValueFifth"+colNumber).html("<font style='color: white;'>"  + prefixSelect+priorValue.toString().replace(numbrFrmt,"")+"<span style='font-size:6pt;'>"+mdVal+ "</span></font>")
                    }

                });

                if(changValue!='' && chaValue!=0.0){
                    if(measType=='Standard'){
//                        if(document.getElementById("mesCompare"+colNumber)==null || document.getElementById("mesCompare"+colNumber).checked){
                        if(chaValue>0.0 && measType=='Standard' && proiorValue!=currVlaue){
                            $("#imgId"+colNumber).attr("src","<%=request.getContextPath()%>/images/Green Arrow.jpg");
                            $("#imgId"+colNumber).nextAll("font").remove();
                            $("#imgId"+colNumber).after("<font style='color:#008000;'>("+changValue+"%)</font>");

                            var value=$("#measureValue"+colNumber).val();
                            if(value!=null){
                            $("#measureDescId"+colNumber).css({"color":"#008000"});
                            //                           alert(value)
                            $("#measureDescId"+colNumber).html(value.toString().replace("Decreased by","  Increased by"));
                            $("#measureValue"+colNumber).val(value.toString().replace("Decreased by"," Increased by"));
                            }
                        }
                        else if(proiorValue==currVlaue){

                        }
                        else{
                            $("#imgId"+colNumber).attr("src","<%=request.getContextPath()%>/images/Red Arrow.jpeg");
                            $("#imgId"+colNumber).nextAll("font").remove();
                            $("#imgId"+colNumber).after("<font style='color:#FF0000;'>("+changValue+"%)</font>");

                            var value=$("#measureValue"+colNumber).val();
                            if(value!=null){
                            $("#measureDescId"+colNumber).css({"color":"#FF0000"});
                            //                        alert(value)
                            $("#measureDescId"+colNumber).html(value.toString().replace("Increased by","  Decreased by"));
                            $("#measureValue"+colNumber).val(value.toString().replace("Increased by"," Decreased by"));
                            }
                        }
//                        }
//                        else{
//                          $("#measureDescId"+colNumber).css({"color":"#000000"});
//                        }
                    }
                    else{
                        if(chaValue>0.0 && measType=='Non-Standard' && proiorValue!=currVlaue){
//                        if(document.getElementById("mesCompare"+colNumber)==null || document.getElementById("mesCompare"+colNumber).checked){
                            $("#imgId"+colNumber).attr("src","<%=request.getContextPath()%>/images/Red Up Arrow.jpeg");
                            $("#imgId"+colNumber).nextAll("font").remove();
                            $("#imgId"+colNumber).after("<font style='color:#FF0000;'>("+changValue+"%)</font>");
                            var value=$("#measureValue"+colNumber).val();
                            if(value!=null){
                            $("#measureDescId"+colNumber).css({"color":"#FF0000"});
                            //                         alert(value)
                            $("#measureDescId"+colNumber).html(value.toString().replace("Increased by","  Decreased by"));
                            $("#measureValue"+colNumber).val(value.toString().replace("Increased by"," Decreased by"));
                             }
//                            }
//                            else{
//                                $("#measureDescId"+colNumber).css({"color":"#000000"});
//                            }
                        }
                        else if(proiorValue==currVlaue){

                        }
                        else{
                            $("#imgId"+colNumber).attr("src","<%=request.getContextPath()%>/images/Green Down Arrow.jpg");
                            $("#imgId"+colNumber).nextAll("font").remove();
                            $("#imgId"+colNumber).after("<font style='color:#008000;'>("+changValue+"%)</font>");
                            var value=$("#measureValue"+colNumber).val();
                            if(value!=null){
                            $("#measureDescId"+colNumber).css({"color":"#008000"});
                            //                          alert(value)
                            $("#measureDescId"+colNumber).html(value.toString().replace("Decreased by","  Increased by"));
                            $("#measureValue"+colNumber).val(value.toString().replace("Decreased by"," Increased by"));
                            }
                        }
                    }
                }
                else if(chaValue==0.0){

                }
                else{
                    var value=$("#measureValue"+colNumber).val();//red
                    if(measureColor==""){
                    $("#measureDescId"+colNumber).css({"color":"#000000"});
                    }else{
                    $("#measureDescId"+colNumber).css({"color":measureColor});
                    }
                    $("#measureDescId"+colNumber).html(value);
                    $("#measureValue"+colNumber).val(value);
                }

//                $("#Dashlets"+colNumber).css({"color":fontColor1});
//               $("#forDillDown"+colNumber).css({"color":fontColor1});
             $("#loadImg").remove();
            }
                $("#measureOptionsId").dialog('close');
            }
            var hmflag='false';
            function home(){
                document.forms.searchForm.action='srchQueryAction.do?srchParam=oneViewBy&hmflag=false';
                document.forms.searchForm.submit();
            }


            function home1(){
                document.forms.searchForm.submit();
                $.ajax({
                         url: 'srchQueryAction.do?srchParam=oneViewBy&hmflag=true&homeFlag=false',
                         success: function(data){
                             document.forms.searchForm.action='srchQueryAction.do?srchParam=oneViewBy&hmflag=false';
                            document.forms.searchForm.submit();
                         }
                });
            }
            var mesureName=''
            var regIdValue=''
            var oneviewForDrillId=''
            var testforRegionOption=''
            function measureOptionsForComplexKPI(regioId,onevieId,currentVal,changeValue,measrId,measurName,priorValue){
                $(".overlapDiv").hide();

               if(document.getElementById("measureType"+regioId)!=null){
//                alert(document.getElementById("measureType"+regionId).style.display)
                if(document.getElementById("measureType"+regioId).style.display=='')
                    document.getElementById("measureType"+regioId).style.display='none';
                }
                if(document.getElementById("reigonOptionsDivId"+regioId)!=null && document.getElementById("reigonOptionsDivId"+regioId).style.display=='block'){
                 $("#reigonOptionsDivId"+regioId).toggle(500);
                }

                colNumber=regioId;
                oneViewIdValue=onevieId;
                currVlaue=currentVal;
//                changValue=changeValue;
                measrueId=measrId;
                measrureName=measurName;
//                proiorValue = priorValue;
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getMeasureOptionData&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber,
                function(data){

                    var jsonValue=eval('('+data+')');
                    if(jsonValue.MeasureOptions[0]!='No Values'){
                        $('#roundIdcomplex option[value='+jsonValue.MeasureOptions[0]+']').attr('selected','selected');
                        $('#NbrFormatIdcomplex option[value='+jsonValue.MeasureOptions[1]+']').attr('selected','selected');

                    if(jsonValue.MeasureOptions[4] != 'null' && jsonValue.MeasureOptions[4] == 'preCust'){
                            $('#prefixSelectcomplex option[value='+jsonValue.MeasureOptions[4]+']').attr('selected','selected');
                            $("#prefixescomplex").show();
                            $("#prefixValcomplex").val(jsonValue.MeasureOptions[5]);
                        }else if(jsonValue.MeasureOptions[5] != 'null'){
                            $("#prefixescomplex").hide();
                            var val = jsonValue.MeasureOptions[5]
                            $('#prefixSelectcomplex option[value="'+val+'"]').attr('selected','selected');
                        }else{
                            $("#prefixescomplex").hide();
                            $('#prefixSelectcomplex option[value=]').attr('selected','selected');
                    }
                    if(jsonValue.MeasureOptions[6] != 'null' && jsonValue.MeasureOptions[6] == 'sufCust'){
                            $('#suffixSelectcomplex option[value='+jsonValue.MeasureOptions[6]+']').attr('selected','selected');
                            $("#suffixescomplex").show();
                            $("#suffixValcomplex").val(jsonValue.MeasureOptions[7]);
                        }else if(jsonValue.MeasureOptions[7] != 'null'){
                            $("#suffixescomplex").hide();
                            var val = jsonValue.MeasureOptions[7];
                            $('#suffixSelectcomplex option[value="'+val+'"]').attr('selected','selected');
                        }else{
                            $("#suffixescomplex").hide();
                            $('#suffixSelectcomplex option[value=]').attr('selected','selected');
                        }
                    }
                    else{
                        $('#roundIdcomplex option[value=0]').attr('selected','selected');
                        $('#NbrFormatIdcomplex option[value=]').attr('selected','selected');
//                        $('#measureTypeId option[value=Standard]').attr('selected','selected');
//                        trendbg='#357EC7'
                        $('#prefixSelectcomplex option[value=]').attr('selected','selected');
                        $('#suffixSelectcomplex option[value=A]').attr('selected','selected');
                    }
                });
                 $("#measureOptionsIdForComplex").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 300,
                        position: 'justify',
                        modal: true
                    });
                $("#measureOptionsIdForComplex").dialog('open');
            }
            function measureOptionForComplexKPI(){
                var roundVal=$("#roundIdcomplex").val();
                var numbrFrmt=encodeURIComponent($("#NbrFormatIdcomplex").val());
                //var measType=$("#measureTypeId").val();
                //var chaValue=parseFloat(changValue);
                //var trendColor=encodeURIComponent($("#trendColorCode").val());
                var fontColor=encodeURIComponent($("#fontColorCode").val());
                var customizePrefix = '';
                var customizeSuffix = '';
                var prefixSelect = encodeURIComponent($("#prefixSelectcomplex").val());
                var suffixSelect = encodeURIComponent($("#suffixSelectcomplex").val());
                if(prefixSelect == "preCust"){
                    customizePrefix = prefixSelect;
                   prefixSelect = encodeURIComponent($("#prefixValcomplex").val());
                }
                if(suffixSelect == "sufCust"){
                    customizeSuffix = suffixSelect;
                   suffixSelect = encodeURIComponent($("#suffixValcomplex").val());
                }
                var measurecolor='';
                var measureColor='';

                 if(document.getElementById("measurebluecomplex") != null && document.getElementById("measurebluecomplex").checked){
                      measurecolor="#336699";
                 measureColor=encodeURIComponent(measurecolor);
                }
                else if(document.getElementById("measureRedcomplex") != null && document.getElementById("measureRedcomplex").checked){
                      measurecolor="#FF0000";
                 measureColor=encodeURIComponent(measurecolor);
                }
                   else if(document.getElementById("measureGreencomplex") != null && document.getElementById("measureGreencomplex").checked){
                      measurecolor="#008000";
                 measureColor=encodeURIComponent(measurecolor);
                  }
                      else if(document.getElementById("measureColorcomplex") != null && document.getElementById("measureColorcomplex").checked){
                      measurecolor=$("#measureColorCodecomplex").val();
                 measureColor=encodeURIComponent(measurecolor);
                  }else{
                      measurecolor="#369";
                  }
                //fontColor1=$("#fontColorCode").val()
                var loadImg='<img id="loadImg" width="15px" height="15px" src=\"<%=request.getContextPath()%>/images/ajax-loader.gif\" align="right"  width="5px" height="5px"  style="position:absolute;float:right;margin-left:30px;" >';
                  $("#Dashlets"+colNumber).append(loadImg);
                $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=measureOptionVale&oneViewIdValue='+oneViewIdValue+'&colNo='+colNumber+'&roundVal='+roundVal+'&numbrFrmt='+numbrFrmt+'&currentVal='+currVlaue+'&measrureName='+measrureName+'&measrueId='+measrueId+"&measureColor="+measureColor+"&prefixSelect="+prefixSelect+"&suffixSelect="+suffixSelect+'&customizePrefix='+customizePrefix+'&customizeSuffix='+customizeSuffix,
                function(data){
                    var jsonCurPriorVal = eval('('+data+')');
                   var currValue = jsonCurPriorVal.MeasureCurrPriorValue[0];
                   var priorValue = jsonCurPriorVal.MeasureCurrPriorValue[1];

                    var mdVal = '';
                   suffixSelect = decodeURIComponent(suffixSelect);
                   prefixSelect = decodeURIComponent(prefixSelect);
                   if(suffixSelect=='K'){
                      mdVal = 'K';
                   }else if(suffixSelect=='M'){
                       mdVal = 'Mn'
                   }else if(suffixSelect=='L'){
                       mdVal = 'Lkh';
                   }else if(suffixSelect=='Cr'){
                       mdVal = 'Crs'
                   }else{
                       mdVal = suffixSelect;
                            }
                             if($("#measureValue"+colNumber).val()!=null){
                             $("#currVal"+colNumber).html("<span style='color:"+measurecolor+";font-size:18pt;'><a onclick=\"getComplexKPIGraphinoneview('" + measrueId+ "','" + oneViewIdValue + "','" + busroleId + "','"+colNumber+"','"+measrureName+"')\" href='javascript:void(0)' >" +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></a></span>");
                             $("#currValfirst"+colNumber).html("<span style='color:"+measurecolor+";font-size:18pt;'><a onclick=\"getComplexKPIGraphinoneview('" + measrueId+ "','" + oneViewIdValue + "','" + busroleId + "','"+colNumber+"','"+measrureName+"')\" href='javascript:void(0)' >"  +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></a></span>");
                             }
                             else{
                                $("#currVal"+colNumber).html("<span style='color:"+measurecolor+";font-size:18pt;'><a onclick=\"getComplexKPIGraphinoneview('" + measrueId+ "','" + oneViewIdValue + "','" + busroleId + "','"+colNumber+"','"+measrureName+"')\" href='javascript:void(0)' >"  +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></a></span>");
                                $("#currValfirst"+colNumber).html("<span style='color:"+measurecolor+";font-size:18pt;'><a onclick=\"getComplexKPIGraphinoneview('" + measrueId+ "','" + oneViewIdValue + "','" + busroleId + "','"+colNumber+"','"+measrureName+"')\" href='javascript:void(0)' >"  +prefixSelect+currValue.toString().replace(numbrFrmt,"")+"<span style='font-size:9pt;'>"+mdVal+ "</span></a></span>");
                             }
                              $("#loadImg").remove();
                });
                    var value=$("#measureValue"+colNumber).val();
                    $("#measureDescId"+colNumber).css({"color":"#000000"});
                    $("#measureDescId"+colNumber).html(value);
                    $("#measureValue"+colNumber).val(value);
                $("#measureOptionsIdForComplex").dialog('close');
            }
            function drillToReports(dashletName,folderid,graphid,regId,onviewId){
                //                  var graphid='';
                //                  var folderid='';
                //                  var dashletName='';
                testforRegionOption=dashletName;
                if(document.getElementById(dashletName).style.display=='none')
                    document.getElementById(dashletName).style.display='';
                else
                    document.getElementById(dashletName).style.display='none'
                //
                if(measureOpti!=''){
                    if(document.getElementById(measureOpti).style.display=='')
                        document.getElementById(measureOpti).style.display='none';
                }

            }

            var selectedgrRepId='';
            var repType='';
            function drillToReport(dashletName,folderid,graphid,regId,onviewId, type)
            {
//                $("#"+testforRegionOption).hide();
//                repType=type;
                mesureName=dashletName;
                regIdValue=regId;
                oneviewForDrillId = onviewId;
                 $(".overlapDiv").hide();
               // class="graphTd'+regIdValue+'"
                            $(".graphTd"+regId).hide();
                if(document.getElementById("reigonOptionsDivId"+regId)!=null && document.getElementById("reigonOptionsDivId"+regId).style.display=='block'){
                 $("#reigonOptionsDivId"+regId).toggle(500);
                }
                if(dashletName==''){
                    dashletName='HeadLines'
                    mesureName='HeadLines';
                }
                $("#drilToTableorGrapId").dialog('close');
                var drillContent='<table><tr><td>Dashboard/Report:</td><td><select id="selectRepOrDB" onchange="showReportOrDBoards(\''+dashletName+'\',\''+folderid+'\',\''+graphid+'\',\''+regId+'\',\''+onviewId+'\',\''+type+'\')"><option id="Dashboard" value="dashboard">Dashboard</option><option id="Report" value="report">Report</option></select></td></tr></table>';
                $("#GraphdrillToRep").html(drillContent);
                if($("#GraphdrillToRep").html()!=null){
                    $("#GraphdrillToRep").dialog({
                        autoOpen: false,
                        height:200,
                        width: 400,
                        position: 'justify',
                        modal: true
                    });
                }
        var innerdivofdrill = "<table id='dynamicTableID'><tbody id='grTorepTbody'></tbody><tfoot><tr><td>&nbsp;&nbsp;</td></tr><tr colspan='4' align='center'><td colspan='4' align='center'><input class='navtitle-hover' type='button' value='GO' onclick='graphToRep()' align='center'/></td></tr></tfoot></table>";
                $("#GraphdrillToRep").dialog('option','title','Drill ')
                $("#GraphdrillToRep").dialog('open')
                        $.ajax({
                        url: 'oneViewAction.do?templateParam2=GetDashBoardNames&foldersIds='+folderid+'&graphid='+graphid+'&newGraphName='+dashletName+'&fromOneview=true',
                        success: function(data){
                            if(data!='')
                            {
                                var htmlVal=data.split("~")
                                $("#grTorepTbody").html(htmlVal[0]);
                                parent.selectedgrRepId=htmlVal[1];
                                //                                alert("selectedgrRepId\t"+selectedgrRepId)
            }
                        }
                    });
                 $("#GraphdrillToRep").append(innerdivofdrill);
            }
 var graphtoreport=''; //kruthika
            function graphToRep(repType,type1)
            {
                $("#GraphdrillToRep").dialog('close')
                $("#RelatedGraphsDialog").dialog('close')
                var idval = "#selectReportgr"+selectedgrRepId
                var  dilvalue = $(idval).val();
                var url='';
                //alert("drillid\t"+drillid)
                if(repType=='report'){
//                    assignGraphThumbnail(dilvalue);
                    url= 'reportViewer.do?reportBy=viewReport&action=open&REPORTID=' + dilvalue;
                    var setDrill=''
                    if($("#forDillDown"+regIdValue+" span").html()==null){
                        setDrill="<a href=\"javascript:submiturls12('"+url+"')\" style=''><strong id='forDillDown"+regIdValue+"' style='font-size: 12pt;font-weight: normal'>"+$("#Dashlets"+regIdValue).text()+"</strong></a>"
                    }
                    else{
                        setDrill="<a href=\"javascript:submiturls12('"+url+"')\" style=''><strong id='forDillDown"+regIdValue+"' style='font-size: 12pt;font-weight: normal'>"+$("#forDillDown"+regIdValue+" span").html()+"</strong></a>"

                    }
                  if(type1=='measures'){
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=drillForRegionName&oneviewForDrillId='+oneviewForDrillId+'&setDrill='+encodeURIComponent(url)+"&regIdValue="+regIdValue+"&REPORTID="+dilvalue+"&REPTYPE=R",
                    success: function(data){
                        var jsonVar=eval('('+data+")")
                        var appendGraph='';
                        var graphId='';
                        var graphName='';
                        var trId='measureNavigateTrId'+regIdValue;
//                        alert(trId);
//                        alert(jsonVar.GraphIds)
//                        alert(jsonVar.GraphNames)
                        $(".graphTd"+regIdValue).remove();
                        for(var i=0;i<jsonVar.GraphIds.length;i++){
//                            alert('in loop');
                            graphId=jsonVar.GraphIds[i];
                            graphName=jsonVar.GraphNames[i];
                            <%String olaphidefn1 =(String) session.getAttribute("oneviewdatetype");%>
                     var hideolap ='<%=olaphidefn1%>'
                  if(hideolap=='true'){
                  }else{
                            appendGraph='<td align="left" class="graphTd'+regIdValue+'" width="1%"><a href="javascript:void(0)" class="ui-icon ui-icon-image" onclick=\'olapGraph("'+graphName+'","'+dilvalue+'","'+graphId+'","'+i+'",null,null)\'></a></td>';
                            $("."+trId).append(appendGraph);
                        }
                        }
                    }
                });
                    $("#Dashlets"+regIdValue).html(setDrill);
} else{
graphtoreport='true';
 $.ajax({
                        url: 'oneViewAction.do?templateParam2=drillForRegionName&oneviewForDrillId='+oneviewForDrillId+'&setDrill='+encodeURIComponent(url)+"&regIdValue="+regIdValue+"&REPORTID="+dilvalue+"&REPTYPE=R",
                    success: function(data){
                        var jsonVar=eval('('+data+")")
                        var appendGraph='';
                        var graphId='';
                        var graphName='';
  var trId='measureNavigateTrId'+regIdValue;
//                        alert(trId);
//                        alert(jsonVar.GraphIds)
//                        alert(jsonVar.GraphNames)
                        $(".graphTd"+regIdValue).remove();
                        for(var i=0;i<jsonVar.GraphIds.length;i++){
//                            alert('in loop');
                            graphId=jsonVar.GraphIds[i];
                            graphName=jsonVar.GraphNames[i];
appendGraph='<td align="left" class="graphTd'+regIdValue+'" width="1%"><a  href="reportViewer.do?reportBy=viewReport&REPORTID='+ dilvalue +' &action=reset "    target="_blank" class="ui-icon ui-icon-image" ></a></td>';
   $("."+trId).append(appendGraph);
                }

                }
                });

                }
                }
                else if(repType=="dashboard"){
                    url= 'dashboardViewer.do?reportBy=viewDashboard&action=open&REPORTID=' + dilvalue;
                    var setDrill=''
                    if($("#forDillDown"+regIdValue+" span").html()==null){
                        setDrill="<a href=\"javascript:submiturls12('"+url+"')\" style=''><strong id='forDillDown"+regIdValue+"' style='font-size: 12pt;font-weight: normal'>"+$("#Dashlets"+regIdValue).text()+"</strong></a>"
                    }
                    else{
                        setDrill="<a href=\"javascript:submiturls12('"+url+"')\" style=''><strong id='forDillDown"+regIdValue+"' style='font-size: 12pt;font-weight: normal'>"+$("#forDillDown"+regIdValue+" span").html()+"</strong></a>"

                    }
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=drillForRegionName&oneviewForDrillId='+oneviewForDrillId+'&setDrill='+encodeURIComponent(url)+"&regIdValue="+regIdValue+"&REPORTID="+dilvalue+"&REPTYPE=D",
                        success: function(data){

                        var jsonVar=eval('('+data+")")
                        var appendGraph='';
                        var graphId='';
                        var graphName='';
                        var grpcount='';
                        var trId='measureNavigateTrId'+regIdValue;
//                        alert(trId);
//                        alert(jsonVar.GraphIds)
//                        alert(jsonVar.GraphNames)
                        $(".graphTd"+regIdValue).remove();
                        for(var i=0;i<jsonVar.GraphIds.length;i++){
//                            alert('in loop');
                            graphId=jsonVar.GraphIds[i];
                            graphName=jsonVar.GraphNames[i];
                            grpcount=jsonVar.GraphCount[i];
                            alert(graphId)
                           // appendGraph='<td align="left" class="graphTd'+regIdValue+'" width="1%"><a href="reportViewer.do?reportBy=viewReport&REPORTID="+ json[i].graphId +"&action=open " target="_blank"  class="ui-icon ui-icon-image" ></a></td>';
                                         appendGraph=" <td title='" + json[i].reportName + "'> <a  href='dashboardViewer.do?reportBy=viewDashboard&REPORTID="+ json[i].reportId+"&pagename=" + json[i].reportName  + " 'style='color:#336699' >" + json[i].reportName +"</a> </td>" ;
                                        // "<td margin-left=2% title='" + json[i].reportName + "'><a  href='reportViewer.do?reportBy=viewReport&REPORTID="+ json[i].reportId +"&action=open ' target='_blank'  style='color:#336699'>" + json[i].reportName + "</a> </td>";
                           $("."+trId).append(appendGraph);
                        }
                        }
                    });
                    $("#Dashlets"+regIdValue).html(setDrill);
                }

                    }
            function assignGraphThumbnail(reportId){
                    $.ajax({
                    url: "dashboardViewer.do?reportBy=assignGraphtoMeasure&REPORTID="+reportId+"&viewletId="+regIdValue+"&oneViewId="+oneviewForDrillId,
                        success: function(data){
                        var jsonVar=eval('('+data+")")
                        var appendGraph='';
                        var graphId='';
                        var graphName='';
                        var trId='measureNavigateTrId'+regIdValue;
//                        alert(trId);
//                        alert(jsonVar.GraphIds)
//                        alert(jsonVar.GraphNames)
                        $(".graphTd"+regIdValue).remove();
                        for(var i=0;i<jsonVar.GraphIds.length;i++){
//                            alert('in loop');
                            graphId=jsonVar.GraphIds[i];
                            graphName=jsonVar.GraphNames[i];
                            appendGraph='<td align="left" class="graphTd'+regIdValue+'" width="1%"><a href="javascript:void(0)" class="ui-icon ui-icon-image" onclick=\'olapGraph("'+graphName+'","'+reportId+'","'+graphId+'","'+i+'",null,null)\'></a></td>';
                            $("."+trId).append(appendGraph);
                        }

                    }
                    });
                } //edited by kruthika
            function submiturls12(url){

                document.frmParameter.action = url+'&isdrilltype=true';
               document.frmParameter.target = "_blank";
                document.frmParameter.submit();
             document.frmParameter.target = "";
                //document.frmParameter.submit();
            }
            var grpMeasId = '';
            var grpMeasName = '';
            var grponeViewIdd = '';
            var grproleId = '';
            function getMeasureGraph(measId,measName,oneViewId,roleId){
                grpMeasId = measId;
                grpMeasName = measName;
                grponeViewIdd = oneViewId;
                grproleId = roleId;
                var measName=encodeURIComponent(measName);
                 $("#measGraphId").dialog({
                        autoOpen: false,
                        height: 480,
                        width: 710,
                        position: 'justify',
                        modal: true
                    });

             <%String olaphidefn =(String) session.getAttribute("oneviewdatetype");%>
                     var hideolap ='<%=olaphidefn%>'
                  if(hideolap=='true'){

                  }else{
                 $("#measGraphId").dialog('option', 'title', grpMeasName+' Measure Graph');
                 $("#measGraphId").dialog('open')
                 var value= document.getElementById("measGraphIdTab");
                value.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute;left:270px;top:150px;" ></center>';
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getMeasureGraph&measId='+measId+'&measName='+measName+'&oneViewId='+oneViewId+'&roleId='+roleId,
                    success: function(data){
                        $("#measGraphIdTab").html(data);
                        //                    var htmlval="<table><tr><td>'"+data+"'</td></tr></table>"
                        //                    return htmlval.toString()
                    }
                });
                }

            }
            function getComplexKPIGraphinoneview(measId,oneViewId,roleId,viewLetId,measName){
                var measName1=encodeURIComponent(measName);
                 $("#customCompDiv").dialog({
                        autoOpen: false,
                        height: 360,
                        width: 650,
                        position: 'justify',
                        modal: true
                    });
                 $("#customCompDiv").dialog('open')
                 var value= document.getElementById("customCompDiv");
                 $("#customCompDiv").dialog('option', 'title', measName);
                value.innerHTML='<center><img id="imgId" width="50px" height="50px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getComplexKPIGraphinoneview&measId='+measId+'&measName='+measName1+'&oneViewId='+oneViewId+'&roleId='+roleId+'&viewLetId='+viewLetId,
                    success: function(data){
                        $("#customCompDiv").html(data);
                        //                    var htmlval="<table><tr><td>'"+data+"'</td></tr></table>"
                        //                    return htmlval.toString()
                    }
                });

            }
            function getCompareRadio(){
                $("#compareSelect").toggle(500);
            }
            function selectRanksOrVals(measId,oneViewId,roleId,viewLetId,compareCheck){
                $("#compareSelect").toggle(500);
                var rankorvalcheck = "rankCheck";
                if(document.getElementById("rankCheck") != null && document.getElementById("rankCheck").checked){
           rankorvalcheck="rankCheck"
        }else if(document.getElementById("valCheck") != null && document.getElementById("valCheck").checked){
            rankorvalcheck="valCheck"
        }
        compareCheck = rankorvalcheck;
                var div=document.getElementById("firstTD1");
                div.innerHTML='<img id="imgId" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" >';
                $.ajax({
                    url: 'dashboardTemplateViewerAction.do?templateParam2=getDataOnRankOrValClick&measId='+measId+'&oneViewId='+oneViewId+'&roleId='+roleId+'&viewLetId='+viewLetId+'&compareCheck='+compareCheck,
                    success: function(data) {
                       $("#firstTD1").html(data);
            }
            });
            }
            function selectNavigation(regionId,oneViewId,currVal,changePer,measId, measName,priorVal,roleId){
            $("#navigationList").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 750,
                        position: 'justify',
                        modal: true
                    });
                    $("#loadingTd").hide();
                    $("#navigationList").dialog('option', 'title', measName+' - Insights');
                    $("#navigationList").dialog('open');
                    var context = '<%=request.getContextPath()%>'
                    $("#navigationList").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                    //$("#loadingmetadata").show();
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getNavigationOptions&measId='+measId+'&measName='+measName+'&oneViewId='+oneViewId+'&roleId='+roleId+'&currVal='+currVal+'&priorVal='+priorVal+'&changePer='+changePer+'&repId='+measId+'&regionId='+regionId+'&context='+context,
                    success: function(data){
                       // $("#loadingmetadata").hide();
                        $("#loadingTd").hide();
                        if(data != ""){
                            $("#navigationList").html(data);
//                            $("#navigationList").dialog('option', 'title', measName+' - Insights (Rolling 12 months)');
//                            $("#navigationList").dialog('open');
                        }else{
                            alert("Insights will be displayed only for main measure");
                            $("#navigationList").dialog('close');
                    }
                    }
                });
            }
            function getAdditionalData(regionId,oneViewId,currVal,changePer,measId, measName,priorVal,roleId){
                  $("#loadingTd").show();
                  $("#loadImg1").show();
                $("#additionalInfoTd").hide();
            var context = '<%=request.getContextPath()%>'
                $.ajax({
                    url: '<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getAdditionalInfo&measId='+measId+'&measName='+measName+'&oneViewId='+oneViewId+'&roleId='+roleId+'&currVal='+currVal+'&priorVal='+priorVal+'&changePer='+changePer+'&repId='+measId+'&regionId='+regionId+'&context='+context,
                    success: function(data){
                       // $("#loadingmetadata").hide();
                       $("#loadingTd").hide();
                       $("#loadImg1").hide();
                        if(data != ""){
                            $("#navigationList").append(data);
//                            $("#navigationList").dialog('option', 'title', measName+' - Insights (Rolling 12 months)');
//                            $("#navigationList").dialog('open');
                        }else{
                            alert("Insights will be displayed only for main measure");
                            $("#navigationList").dialog('close');
                        }
                    }
                });
            }
             function relatedMeasureInfo(regionId,oneViewId,currVal,changePer,measId, measName,priorVal,roleId){
             $("#relatedMeasures").dialog({
                        autoOpen: false,
                        height: 320,
                        width: 600,
                        position: 'justify',
                        modal: true
                    });
                    $("#relatedMeasures").dialog('option', 'title', measName+' - Related Measure Information');
                    $("#relatedMeasures").dialog('open');
                    var context = '<%=request.getContextPath()%>'
                    $("#relatedMeasures").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
                    //$("#loadingmetadata").show();
                    $.ajax({
                    url: '<%=request.getContextPath()%>/oneViewAction.do?templateParam2=getRelatedMeasures&measId='+measId+'&measName='+measName+'&oneViewId='+oneViewId+'&roleId='+roleId+'&currVal='+currVal+'&priorVal='+priorVal+'&changePer='+changePer+'&repId='+measId+'&regionId='+regionId+'&context='+context,
                    success: function(data){
                       // $("#loadingmetadata").hide();
                        if(data != ""){
                            $("#relatedMeasures").html(data);
                            $("#relatedMeasures").dialog('option', 'title', measName+' - Related Measure Information');
                            $("#relatedMeasures").dialog('open');
                        }else{
                            alert(measName+" do not have related measures");
                            $("#relatedMeasures").dialog('close');
                        }
                    }
                    });
             }

             function relatedParametersDrill(measId,oneViewId,roleId,monthY,same){

                if(same=="true"){

                     var counttb = $("#changetext").val();
                    var seltbid=$("#changeid").val();

                }else{
                     $("#changetext").val('');
                     $("#changeid").val('');
                     var counttb = $("#changetext").val();
                    var seltbid=$("#changeid").val();

                }

             if(($("#ChangeTrend").is(':checked'))||($("#ChangePrcntTrend").is(':checked'))||($("#DrillTrend").is(':checked'))){
             if($("#ChangeTrend").is(':checked')){

                   document.getElementById("topbotgo").style.display='';
                         change=true;
                         changeper=false;
                    }
                else if($("#ChangePrcntTrend").is(':checked')){

                     document.getElementById("topbotgo").style.display='';
                     change=false;
                     changeper=true;
                  }


             else if($("#DrillTrend").is(':checked')){

                     change=false;
                     changeper=false;
                     document.getElementById("topbotgo").style.display='';
                  }
                       $("#measGraphTrendDiv").dialog({
                        autoOpen: false,
                        height: 650,
                        width: 740,
                        position: 'justify',
                        modal: true
                    });
                    var roleIdsarray = new Array();
                    roleIdsarray = monthY.toString().split(",");
                    var monthVal=roleIdsarray[0];
                    var noOfTrends;
                      $("#measureTrendTdid").html("");
                      var html1="<tr><td id='measureTrendTd0'></td><td id='measureTrendTd1'></td></tr>";
                         html1+="<tr><td id='measureTrendTd2'></td><td id='measureTrendTd3'></td></tr>";
                         $("#measureTrendTdid").html(html1);
                     $("#measGraphTrendDiv").dialog('option', 'title',grpMeasName+ ' Parameters Analysis');
                       $("#measureTrendTd0").html("");
                       $("#measureTrendTd1").html("");
                       $("#measureTrendTd2").html("");
                       $("#measureTrendTd3").html("");

                   $.ajax({
                    url: 'oneViewAction.do?templateParam2=getMeasureGraphForTrends&oneViewId='+oneViewId+'&getNoOfTrends=true'+'&ischange='+change+'&ischangepercent='+changeper+'&counttb='+counttb+'&seltbid='+seltbid ,
                   success:function(data){

                   if(data == "Please select parameters for Drill"){
                            alert("Please select parameters for Drill");}
                        else{
                                 $("#measGraphTrendDiv").dialog('open');

                    var value0= document.getElementById("measureTrendTd0");
                value0.innerHTML='<center><img id="imgId" width="100px" height="100px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute;left:320px;top:200px;" ></center>';

                   noOfTrends=data;
                   for(var i=0;i<noOfTrends;i++){
                   dispTrends(measId,oneViewId,roleId,monthVal,i,counttb,seltbid);}
                  }
                         }
                   });
                   }
                   else{
                   document.getElementById("topbotgo").style.display='none';

                   $("#measGraphOverLayDiv").dialog({
                        autoOpen: false,
                        height: 600,
                        width: 840,
                        position: 'justify',
                        modal: true
                    });
                // $("#measGraphOverLayDiv").dialog('open')
                     var roleIdsarray = new Array();
                    roleIdsarray = monthY.toString().split(",");
                    var monthVal=roleIdsarray[0];
                    var measIdList=new Array();
                    var measNameList=new Array();
                    var imgtestList=new Array();
                    var selectedParam=new Array();
                    var Seriescolors=new Array();

                    $.ajax({
                    url: 'oneViewAction.do?templateParam2=getOverlayTrends&checkcon=checkcon&oneViewId='+oneViewId+'&monthVal='+monthVal+'&measId='+measId+'&roleId='+roleId,
                   success:function(data){
                        if(data == "Please select parameters for Drill"){
                            alert("Please select parameters for Drill");}
                        else{
                    $("#measGraphOverLayDiv").dialog('open')
                    $("#measGraphOverLayDiv").dialog('option', 'title',grpMeasName+ ' Measure Graph OverLay');
                    $("#measureOverLayTd").val("");
                    $("#measureOverLayTd").html('<center><img id="imgId" width="100px" height="100px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute;left:320px;top:200px;" ></center>');


                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getOverlayTrends&oneViewId='+oneViewId+'&monthVal='+monthVal+'&measId='+measId+'&roleId='+roleId,
                   success:function(data){

                    var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")
                measIdList=jsonVar.measIdList
                measNameList=jsonVar.measNameList
                imgtestList=jsonVar.imgtestList
                selectedParam=jsonVar.selectedParam
                Seriescolors=jsonVar.Seriescolors
                //selectedParamVAl=jsonVar.selectedParamVAl

        var htmlstr="";
        htmlstr+="<table width='730px' height='500px'><tr><td width='auto'>";
        htmlstr+="<div id='trendregion' style='width:460px;height:460px'>"+imgtestList[0]+"</div>";
        htmlstr+="<td width='1px' bgcolor='LightGrey'><BR></td><td width='120px' height='500px'><div style='width:120px;height:500px'><table top='0px'><tr><td><span>Period</span></td>";
        htmlstr+="<td><select id='periodSelect' name='periodSelect'><option value='All'>All</optiong><option value='Prev 3 Month'>Prev 3 Month</optiong><option value='Prev 6 Month'>Prev 6 Month</optiong>";
        htmlstr+="<option value='Prev 9 Month'>Prev 9 Month</optiong><option value='Prev 12 Month'>Prev 12 Month</optiong></select></td></tr><tr height='6px'></tr>";
        htmlstr+="<tr><td><span>Parameters</span></td><td><select id='availParameters' name='availParameters'>";
        for(var i=0;i<measIdList.length;i++){
        htmlstr+="<option value='"+measIdList[i]+"'>"+measNameList[i]+"</optiong>";}
        htmlstr+="</select></td><tr height='6px'></tr></tr><tr><td><span>Show</span></td><td><select id='Showtopbot' name='periodSelect'><option value='3'>Top 3</optiong><option value='5'>Top 5</optiong><option value='8'>Top 8</optiong></select>";
        htmlstr+="</td></tr><tr height='6px'></tr>";
        htmlstr+="<tr><td></td><td><input type='button'  width='50px' class='navtitle-hover' style='width:auto' value='Go' onclick='submitoverlayedOpt("+measId+","+oneViewId+","+roleId+",\""+monthVal+"\")'></td></tr>";
        htmlstr+="<tr height='20px'><td height='20px'></td></tr>";

        htmlstr+="</table>";
        htmlstr+="<table top='100px' class='' align='center' width='auto' height='80px' style='margin-left:50px;' id='selectedParamsA'>";
        for(var k=0;k<selectedParam.length;k++){
        if(selectedParam[k]==""){
        htmlstr+="<tr ><td width='auto' top='10px' bottom=10px' style='white-space: nowrap;-moz-border-radius:3px 3px 3px 3px;background-color: #006699;color:#006699;font-size:13px;font-weight:700;padding:3px 25px;text-align:center;text-decoration:none;text-shadow:none;-moz-border-radius:3px;-webkit-border-radius:3px;-webkit-transition:all 0.1s ease-in-out;width:70px;height:5px;'><span style=''>Null</td></tr>";
        }else{
        htmlstr+="<tr ><td width='auto' top='10px' bottom=10px' style='white-space: nowrap;-moz-border-radius:3px 3px 3px 3px;background-color: #006699;color:#FFFFFF;font-size:13px;font-weight:700;padding:3px 25px;text-align:center;text-decoration:none;text-shadow:none;-moz-border-radius:3px;-webkit-border-radius:3px;-webkit-transition:all 0.1s ease-in-out;width:70px;height:5px;'><span style=''>"+selectedParam[k]+"</td></tr>";
        }// htmlstr+="<td ><span>"+selectedParamVAl[k]+"</td></tr>";
        }
        htmlstr+="</table></td></tr></table>";
        $("#measureOverLayTd").html(htmlstr);
                  }
                   });
                   }}
                    });
                   }

             }

             function submitoverlayedOpt(measId,oneViewId,roleId,monthVal){
             var nuOfParams=document.getElementById("Showtopbot").value;
             var selParam=document.getElementById("availParameters").value;
              $("#trendregion").val("");
         $("#trendregion").html('<center><img id="imgId" width="100px" height="100px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute;left:320px;top:200px;" ></center>');

              $.ajax({
                    url: 'oneViewAction.do?templateParam2=getOverlayTrends&applyChangeNum=true&nuOfParams='+nuOfParams+'&selParam='+selParam+'&oneViewId='+oneViewId+'&measId='+measId+'&roleId='+roleId+'&monthVal='+monthVal,
                   success:function(data){
                    var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")
                        var selectedParam=new Array();
                        var Seriescolors=new Array();
                    //var selectedParamVAl=new Array();
                    var imgtestList=new Array();
                     selectedParam=jsonVar.selectedParam
                     //selectedParamVAl=jsonVar.selectedParamVAl
                     imgtestList=jsonVar.imgtestList
                     Seriescolors=jsonVar.Seriescolors
                     var htmlstr="";
        for(var k=0;k<selectedParam.length;k++){
         if(selectedParam[k]==""){
        htmlstr+="<tr ><td width='auto' top='10px' bottom=10px' style='white-space: nowrap;-moz-border-radius:3px 3px 3px 3px;background-color: #006699;color:#006699;font-size:13px;font-weight:700;padding:3px 25px;text-align:center;text-decoration:none;text-shadow:none;-moz-border-radius:3px;-webkit-border-radius:3px;-webkit-transition:all 0.1s ease-in-out;width:70px;height:5px;'><span style=''>Null</td></tr>";
        }else{
        htmlstr+="<tr ><td style='white-space: nowrap;-moz-border-radius:3px 3px 3px 3px;background-color: #006699;color:#FFFFFF;font-size:13px;font-weight:700;padding:3px 25px;text-align:center;text-decoration:none;text-shadow:none;-moz-border-radius:3px;-webkit-border-radius:3px;-webkit-transition:all 0.1s ease-in-out;width:70px;'>"+selectedParam[k]+"</td></tr>";
        }
    }
        $("#selectedParamsA").html(htmlstr);
        $("#trendregion").val("");
         $("#trendregion").html(imgtestList[0]);
                   }
                   });
             }


function dispTrends(measId,oneViewId,roleId,monthVal,noOfTrends,counttb,seltbid){
             preOneId=oneViewId;
             premeasId=measId;
             preroleId=roleId;
             premonthY=monthVal;
             prenoOfTrends=noOfTrends;

                    var context = '<%=request.getContextPath()%>'

//                      for(var i=0;i<noOfTrends;i++){
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getMeasureGraphForTrends&oneViewId='+oneViewId+'&monthVal='+monthVal+'&measId='+measId+'&roleId='+roleId+'&num='+noOfTrends+'&ischange='+change+'&ischangepercent='+changeper+'&counttb='+counttb+'&seltbid='+seltbid,

                    success: function(data){
                        if(data == "Please select parameters for Drill"){
                            alert("Please select parameters for Drill");
                            $("#measGraphTrendDiv").dialog('close');
                        }
                        else{
                            if(noOfTrends==0){
                           var value0= document.getElementById("measureTrendTd0");
                           value0.innerHTML="";}

                        $("#measureTrendTd"+noOfTrends).html(data);
                        totaltrends=noOfTrends;
                    }
                    }

                });

             }

     function showSingleGraph(numOf,num,datalistObj){
        var ar;
          var datalist=new Array();
          if(numOf=='0'){
        for(var i=0;i<num;i++){
        datalist.push($("#inputhidden"+i).val()+"~");
        }
     }
    ar=$("#inputhidden"+numOf).val();
              $.ajax({
                        url: 'oneViewAction.do?templateParam2=getSingleDrillTrend&dataset='+ar+'&num='+numOf+'&datalistObj='+datalist+'&totaltrends='+totaltrends,
                        success: function(data){
                            if(data=='NoData'){
                                alert("No More Graphs");
                            }else{
                            $("#measureTrendTdid").html("");
        var htmlstr="<tr><td id='measureTrendTds'></td></tr>"
   $("#measureTrendTdid").html(htmlstr);
                            $("#measureTrendTds").html(data);
                        }
                        }
                    });
             }
            function clearcomments(){
                $("#usertextareaId").val("");
            }

            function delteteOneView(oneviewId,oneviewName){
                 var userType='<%=userType%>'
                 var isPowerAnalyserEnableforUser='<%=isPowerAnalyserEnableforUser%>'
                 //alert(isPowerAnalyserEnableforUser)
                 if(isPowerAnalyserEnableforUser == 'true'){
                     var configdelete = confirm(" "+oneviewName+" will get deleted for all users Permanently. Do you want to continue?")
                      if(configdelete){
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=deleteOneview&oneviewId='+oneviewId+'&userType='+isPowerAnalyserEnableforUser+'&userId='+<%=userIdStr%>,
                        success: function(data){
                            alert("Oneview Deleted Successfully!")
                            home();
                        }
                    });
                   }
                 }else{
                    var configdelete = confirm("Do you want to Delete the assignment for"+oneviewName+" ?");
                    if(configdelete){
                    $.ajax({
                        url: 'oneViewAction.do?templateParam2=deleteOneview&oneviewId='+oneviewId+'&userId='+<%=userIdStr%>,
                        success: function(data){
                            alert("Oneview Assignment Deleted Successfully!")
                            home();
                        }
                    });
                   }
                 }
            }
            function renameOneview(oneviewId,oneviewName){

                $("#oneviewReadId").val(oneviewName)
                $("#oneviewtestId").val(oneviewId)
                $("#OneviewRename").dialog({
                        autoOpen: false,
                        height: 150,
                        width: 325,
                        position: 'justify',
                        modal: true
                    });
                $("#OneviewRename").dialog('open')
            }
            function assignOneView(oneviewId,oneviewName,roleIds,olduserId){
                var roleIdsarr = "";
                var roleIdsarray = new Array();
                if(roleIds.toString().indexOf(",") != -1){
                    roleIdsarray = roleIds.toString().split(",");
                    for(var i=0;i<roleIdsarray.length;i++){
                    if(roleIdsarr[i] != "")
                        roleIdsarr += ","+roleIdsarray[i].replace("[", "", "gi").replace("]", "", "gi");
                    else
                        roleIdsarr = ","+roleIdsarray[i].replace("[", "", "gi").replace("]", "", "gi");
                    }
                    roleIdsarr = roleIdsarr.toString().substr(1);
                }else{
                    roleIdsarr = roleIds;
                }
            $("#assignDiv").dialog({
                        autoOpen: false,
                        height: 450,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getOneViewAssignment&oneviewId='+oneviewId+'&onviewName='+oneviewName+'&roleIds='+roleIdsarr+'&olduserId='+olduserId,
                    success: function(data){
                        if(data !=""){
                        var jsonVar=eval('('+data+')');
                        $("#assignDiv").html("")

                isMemberUseInOtherLevel=jsonVar.isMemberUseInOtherLevel
                $("#assignDiv").html(jsonVar.htmlStr)
                var ul = document.getElementById("sortable");
                $("#assignDiv").append("<table border=\"0\" width=\"100%\"><tr><td align=\"left\"><input type=\"button\" name=\"MoveAll\" class=\"navtitle-hover\" value=\"MoveAll\" onclick=\"moveAllFromsortable()\" ></td><td align=\"center\"><input type=\"button\" name=\"Done\" class=\"navtitle-hover\" value=\"Done\" onclick=\"saveOneViewAssignments('"+oneviewId+"','"+oneviewName+"','')\" ></td><td width=\"40%\"/></tr></table>")
                $(".myDragTabs").draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });


                $("#dropTabs").droppable(
                {
                    activeClass:"blueBorder",
                    accept:'.myDragTabs',
                    drop: function(ev, ui) {
                        createColumn(ui.draggable.attr('id'),ui.draggable.html(),"sortable");
                    }
                }
            );
                grpColArray=jsonVar.memberValues
                $(".sortable").sortable();
                $("#assignDiv").dialog('open');
                    }else{
                        alert("Role is not assigned to this user")
            }
                    }
        });

            }
            function getSequnceOfOneViews(){
                $("#sequenceDiv").dialog({
                        autoOpen: false,
                        height: 550,
                        width: 500,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });

                    var frameObj=document.getElementById("sequenceFrame");
                    var source="OneviewSequence.jsp";
                    frameObj.src=source;
                    $("#sequenceDiv").dialog('open');


            }
            function saveOneViewAssignments(id,name,ull){
            $("#assignDiv").dialog('close');
                var ul = document.getElementById("sortable");
                var userIdArray = new Array();
                var colIds;
                if(ul!=undefined || ul!=null){
                     colIds=ul.getElementsByTagName("li");
                    if(colIds!=null && colIds.length!=0){
                        for(var i=0;i<colIds.length;i++){
                            userIdArray.push(colIds[i].id.split("_")[0]);
                        }
                    }
                }
//                $.ajax({
//                    url: 'oneViewAction.do?templateParam2=assignOneView&oneviewId='+id+'&onviewName='+name+'&userIdArray='+userIdArray,
//                    success: function(data){
//                        //alert(data)
//                        if(data == "success")
//                        alert("Oneview Assigned Successfully");
//                        home();
//                    }
//                });
//            }
$("#divIdTest1").html("");
$("#divIdTest1").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="200px" height="200px"  style="position:absolute;top:200px;margin-left:-200px;" /></center>');
 $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=saveingOneviews&reportBy=viewReport&action=open'+'&assignFlag=true&oneviewId='+id,
                function(data){
                    //home();
                   // alert("Oneview Saved Successfully!")
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=assignOneView&oneviewId='+id+'&onviewName='+name+'&userIdArray='+userIdArray+'&fileName='+data,
                    success: function(data){
                        //alert(data)
                        if(data == "success")
                        alert("Oneview Assigned Successfully");
                        home();
                    }
                });

                });
            }
            function oneviewRenaming(){

                var onviewName = $("#oneviewAddId").val()
                var oneviewId =  $("#oneviewtestId").val()
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=oneViewRenaming&oneviewId='+oneviewId+'&onviewName='+onviewName,
                    success: function(data){
                        alert("Oneview Renamed Successfully!")
                        home();
                    }
                });
                $("#OneviewRename").dialog('close')
            }
            function shareOneView(oneviewId,OneviewName){

               $("#shareOneview").dialog({
                        autoOpen: false,
                        height: 300,
                        width: 400,
                        position: 'justify',
                        modal: true,
                        resizable:false
                    });
               $("#shareOneview").dialog('open')
                $("#sharesubject").val(OneviewName);
            }



            function displayOneviewTextArea()
            {

                var a=$("#selectusersId").val();
                if (a=="selected" )
                {
                    $("#toMailId").show();
                    $("#sharesubjectId").show();
                    $("#sharesubject").show();
                    $("#userstextareaId").show();
                    $("#userstextareaId").elastic();
                }else
                {
                    $("#userstextarea").val("");
                    $("#sharesubjectId").hide();
                    $("#sharesubject").hide();
                    $("#toMailId").hide();
                    $("#userstextareaId").hide();

                }
            }

            function sendOneviewEmail(){
                //        var fileType=document.getElementById("fileType1").value;
                var selectusers=document.getElementById("selectusersId").value;
                var share_subject=document.getElementById("sharesubject").value;
                var userstextarea=document.getElementById("userstextareaId").value;
                //        var REPORTID=document.getElementById("REPORTID").value;
                //        var ctxPath=document.getElementById("h").value;
                //        var delimiter=$('#sDelimiter').val();
                //        var txtId=$('#txtIdentifier').val();
                if(selectusers=="")
                {
                    alert("select Selective Users")
                }
                else
                {
                    $("#shareOneview").dialog('close');
                    //            $("#loading").show();
                    //            $.ajax({
                    //                url:'<%=request.getContextPath()%>/reportViewer.do?reportBy=sendSharereportMail&varhtml=fromAdvancedHtml&repID='++'&fileType='+fileType+'&selectusers='+selectusers+'&share_subject='+share_subject+'&userstextarea='+encodeURIComponent(userstextarea),
                    //                success:function(data)
                    //                {
                    //                    $("#loading").hide();
                    //                    alert("Report shared sucessfully with the Users");
                    //                }
                    //            });
                }
            }

            function downLoadOneviewPdf(oneviewId,OneviewName){
                var htmltype = 'oneviewHtml';

                $.ajax({
                    url: 'oneViewAction.do?templateParam2=gerateOneviewPdf&oneviewId='+oneviewId+'&OneviewName='+OneviewName+"&downloaingfdf=pdfdownload"+"&reportBy=viewReport&action=open",
                    success: function(data){
                        var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype;
                        var dSrc = document.getElementById("oneFrame");
                        dSrc.src = source;
                    }
                });
                //         var source = '<%=request.getContextPath()%>/TableDisplay/pbDownload.jsp?dType='+htmltype+'&fileName='+fileName+'&OneviewName='+OneviewName+"&downloaingfdf=pdfdownload"+"&reportBy=viewReport&action=open";
            }
            function taggleNewMeasures(regionId,oneviewId,currVal,priorVal,measureId,measureName){var htmlis;var htmlis1;

                var loadImg='<img id="loadImg" width="15px" height="15px" src=\"<%=request.getContextPath()%>/images/ajax-loader.gif\" align="right"  width="5px" height="5px"  style="position:absolute;float:right;margin-left:30px;" >';
                  $("#Dashlets"+regionId).append(loadImg);
                if(document.getElementById("compareMeasIdfirst"+regionId)!=null || document.getElementById("compareMeasIdfirst"+regionId)!=null || document.getElementById("compareMeasIdfirst"+regionId)!=null){
                 if(document.getElementById("compareMeasIdfirst"+regionId).style.display==''){
                     $.ajax({
                        url: 'oneViewAction.do?templateParam2=setNewTypeMeasure&regionId='+regionId+'&oneviewId='+oneviewId+"&displayType=first",
                        success: function(data){
                            document.getElementById("compareMeasIdfirst"+regionId).style.display='none';
                            document.getElementById("compareMeasurIdsecond"+regionId).style.display='';
                            $("#loadImg").remove();     $("#measureNavigateIdsecond"+regionId).hide();
                      $("#relatedMeasureInfoIdsecond"+regionId).hide();
                        }
                    });


                 }
                 else if(document.getElementById("compareMeasurIdsecond"+regionId).style.display==''){
                     $.ajax({
                        url: 'oneViewAction.do?templateParam2=setNewTypeMeasure&regionId='+regionId+'&oneviewId='+oneviewId+"&displayType=second",
                        success: function(data){
                            document.getElementById("compareMeasurIdsecond"+regionId).style.display='none';
                            document.getElementById("compareMeasurIdthird"+regionId).style.display='';
                            $("#loadImg").remove();     $("#measureNavigateIdthird"+regionId).hide();
                      $("#relatedMeasureInfoIdthird"+regionId).hide();
                        }
                    });


                 }
                  else if(document.getElementById("compareMeasurIdthird"+regionId).style.display==''){
                      $.ajax({
                        url: 'oneViewAction.do?templateParam2=setNewTypeMeasure&regionId='+regionId+'&oneviewId='+oneviewId+"&displayType=third",
                        success: function(data){
                            document.getElementById("compareMeasurIdthird"+regionId).style.display='none';
                            document.getElementById("compareMeasurIdfourth"+regionId).style.display='';
                            $("#loadImg").remove();     $("#measureNavigateIdfourth"+regionId).hide();
                      $("#relatedMeasureInfoIdfourth"+regionId).hide();
                        }
                    });


                 }
                  else if(document.getElementById("compareMeasurIdfourth"+regionId).style.display==''){
                      $.ajax({
                        url: 'oneViewAction.do?templateParam2=setNewTypeMeasure&regionId='+regionId+'&oneviewId='+oneviewId+"&displayType=fourth",
                        success: function(data){
                            document.getElementById("compareMeasurIdfourth"+regionId).style.display='none';
                            document.getElementById("compareMeasurIdfifth"+regionId).style.display='';
                            $("#loadImg").remove();     $("#measureNavigateIdfifth"+regionId).hide();
                      $("#relatedMeasureInfoIdfifth"+regionId).hide();
                }
                    });


                 }
                 else{
                      $.ajax({
                        url: 'oneViewAction.do?templateParam2=setNewTypeMeasure&regionId='+regionId+'&oneviewId='+oneviewId+"&displayType=fifth",
                        success: function(data){
                        document.getElementById("compareMeasurIdfifth"+regionId).style.display='none';
                      document.getElementById("compareMeasIdfirst"+regionId).style.display='';
                      $("#loadImg").remove();

                }
                    });

                 }
                }else{
                    $("#loadImg").remove();
                }
            $("#measureNavigateIdsecond"+regionId).hide();
                      $("#relatedMeasureInfoId"+regionId).hide();
<%String oneviewtypedatetogle =(String) session.getAttribute("oneviewdatetype");%>
                       var  oneviewtypedate1='<%=oneviewtypedatetogle%>'
                     if(oneviewtypedate1=='true'){
 $(".measureNavigateTrId"+regionId).hide();

                     }
            }
            function oneviewTimedetails(timeid,repName,roleId,repId,divId,onevieId){
//              document.getElementById("measureType"+divId).style.display='none';
               if(document.getElementById("reportTime"+divId).checked)
                document.getElementById("reportTime"+divId).checked = false;
                document.getElementById("oneviewTime"+divId).checked = true;
                if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
                 $("#reigonOptionsDivId"+divId).toggle(500);
                }
                $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+divId+'&oneviewId='+onevieId+"&oneviewTime=false",
                        success: function(data){
                        }
                    });
            }
            function reportTimedetails(timeid,repName,roleId,repId,divId,onevieId){
//                document.getElementById("measureType"+divId).style.display='none';
                 if(document.getElementById("oneviewTime"+divId).checked)
                     document.getElementById("oneviewTime"+divId).checked = false;
                document.getElementById("reportTime"+divId).checked = true;
                if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
                  $("#reigonOptionsDivId"+divId).toggle(500);
                }
                $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewAndReportTimeDeatails&regionId='+divId+'&oneviewId='+onevieId+"&oneviewTime=true",
                        success: function(data){
                        }
                    });
            }

//            function createStickyNote(){
//               var noteLabel = prompt("Enter note label","Note");
//               var initialText = "ENTER YOUR NOTES HERE";
//              if(noteLabel != null)
//             {
//                   $.ajax({
//                        url: 'stickyNoteAction.do?stickyNoteParam=createStickyNote&noteLabel='+noteLabel+'&intText='+initialText,
//                         success: function(data){
//                          var innerData =$('#StickNoteSpan').html();
//                          $('#StickNoteSpan').html(innerData+data);
//                         }
//
//                   });
//                           document.getElementById('StickNoteSpan').style.display="";
//              }
//       }selectforReadd
                function selectReadd(id)
                {
                    if(document.getElementById("reigonOptionsDivId"+id)!=null && document.getElementById("reigonOptionsDivId"+id).style.display=='block'){
                        $("#reigonOptionsDivId"+id).toggle(500);
                       }
                    $("#readdDivId"+id).toggle(500);
//                    document.getElementById(id).style.display='';
                }
                function selectforReadd(id)
                {
                    if(document.getElementById("readdDivId"+id).style.display=='block'){
                        $("#readdDivId"+id).toggle(500);
                       }
                    $("#reigonOptionsDivId"+id).toggle(500);
//                    document.getElementById(id).style.display='';
                }

                function measureNoCompare(timeid,repName,roleId,repId,divId,onevieId){

                    if(document.getElementById("mesCompare"+divId).checked){
                      document.getElementById("mesCompare"+divId).checked = false;
                      document.getElementById("mesNoCompare"+divId).checked = true;

                    }
                    if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
                      $("#reigonOptionsDivId"+divId).toggle(500);
                    }
                 $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewCompAndNoComp&regionId='+divId+'&oneviewId='+onevieId+"&oneviewCompare=false",
                        success: function(data){
                        }
                    });

                }
                function measureCompare(timeid,repName,roleId,repId,divId,onevieId){

                    if(document.getElementById("mesNoCompare"+divId).checked){
                      document.getElementById("mesNoCompare"+divId).checked = false;
                      document.getElementById("mesCompare"+divId).checked = true;

                    }
                    if(document.getElementById("reigonOptionsDivId"+divId)!=null && document.getElementById("reigonOptionsDivId"+divId).style.display=='block'){
                      $("#reigonOptionsDivId"+divId).toggle(500);
                    }
                   $.ajax({
                        url: 'oneViewAction.do?templateParam2=oneviewCompAndNoComp&regionId='+divId+'&oneviewId='+onevieId+"&oneviewCompare=true",
                        success: function(data){
                        }
                    });

                }

            function isNumberKey(evt)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode
                if (charCode != 46 && charCode > 31
                    && (charCode < 48 || charCode > 57))
                    return false;

                return true;
            }
            function olapGraph(dashletName,reportId,graphid,graphnum,timeDetails,isrepDate,regid,chartname,rolename){
                <%String oneviewtypedateolap =(String) session.getAttribute("oneviewdatetype");

                %>
                       var  oneviewtypedate1='<%=oneviewtypedateolap%>'
                     if(oneviewtypedate1=='true'){
                     }else{
                var dialogWidth=(2.5/4)*($(window).width());
                var dialogHeight=($(window).height())-80;
                var date = $("#dateId").val();
                var duration = $("#timedetailselectId").val();
                var compare = $("#compareSelectId").val();
//
                $("#OLAPGraphDialog").dialog({
                        autoOpen: false,
                        height: dialogHeight,
                        width: dialogWidth,
                        position: 'justify',
                        modal: true,
//                        title:dashletName,
                        resizable:true
             });

                    $("#OLAPGraphDialog").dialog('option', 'title', dashletName);

                    $("#OLAPGraphDialog").dialog('open');
                //    document.forms.frmParameter.action="dashboardViewer.do?reportBy=OLAPGraphViewer&dashletId="+dashletId+"&dashBoardId="+dashBoardId;
                //    document.forms.frmParameter.submit();
                    var frameObj = document.getElementById("OLAPGraphFrame");
                    var source ="OLAPGraph.jsp?reportId="+reportId+"&graphId="+graphid+"&callFrom=oneView&graphNum="+graphnum+"&width="+(dialogWidth-50)+"&height="+(dialogHeight-50)+"&timeDetails="+timeDetails+"&isrepDate="+isrepDate+"&oneviewID="+oneviewID+"&regid="+regid+"&chartname="+chartname+"&repname="+dashletName+"&rolename="+rolename;
                    frameObj.src=source;
}

            }
            function measureTrendGraph(measureId,measName,roleId,height,width,viewLetId,oneViewId){
//                alert(measureId+' measName'+measName+' roleId'+roleId+' height'+height+' width'+width+' viewLetId'+viewLetId);
                var date = $("#dateId").val();
                $("#Dashlets-"+viewLetId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
//                alert(date);
                $.ajax({
                    url: "dashboardViewer.do?reportBy=getOneViewRollingGraphJQ&measureId="+measureId+"&roleId="+roleId+"&height="+Math.round(height)+"&width="+Math.round(width)+"&date="+date+"&measName="+measName+"&viewLetId="+viewLetId+"&oneViewId="+oneViewId+"&isZoomTrend=false",
                    success: function(data){
//                        alert(data);
                        $("#Dashlets-"+viewLetId).html(data);
                    }
                });

            }
            function customTrendGraph(measureId,measName,roleId,height,width,viewLetId,oneViewId){
//                alert(measureId+' measName'+measName+' roleId'+roleId+' height'+height+' width'+width+' viewLetId'+viewLetId);
                var date = $("#dateId").val();
                $("#Dashlets-"+viewLetId).html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="45px" height="45px"  style="position:absolute" /></center>');
//                alert(date);
                $.ajax({
                    url: "dashboardViewer.do?reportBy=getOneViewCustomRollingGraphJQ&measureId="+measureId+"&roleId="+roleId+"&height="+Math.round(height)+"&width="+Math.round(width)+"&date="+date+"&measName="+measName+"&viewLetId="+viewLetId+"&oneViewId="+oneViewId+"&isZoomTrend=false",
                    success: function(data){
//                        alert(data);
                        $("#Dashlets-"+viewLetId).html(data);
                    }
                });

            }
            function zoomTrend(measureId,name,viewletId,oneViewId){
//              alert(measureId+''+name+''+viewletId+''+oneViewId);
              $("#reigonOptionsDivId"+viewletId).hide();
              var dialogWidth=(0.5)*($(window).width());
              var dialogHeight=($(window).height())-120;
              var date = $("#dateId").val();
              $("#zoomTrendGraph").dialog({
                        autoOpen: false,
                        height: dialogHeight,
                        width: dialogWidth,
                        position: 'justify',
                        modal: true,
//                        title:name,
                        resizable:false
//
             });
             $("#zoomTrendGraph").dialog('option', 'title', name);
             $("#zoomTrendGraph").dialog('open');
             $("#zoomTrendGraph").html('<center><img id="imgId" src="images/ajax1.gif" align="middle"  width="75px" height="75px"  style="position:absolute" /></center>');
             $.ajax({
                    url: "dashboardViewer.do?reportBy=getOneViewRollingGraphJQ&measureId="+measureId+"&height="+Math.round((dialogHeight-40))+"&width="+Math.round((dialogWidth-50))+"&date="+date+"&measName="+name+"&viewLetId="+viewletId+"&oneViewId="+oneViewId+"&isZoomTrend=true",
                    success: function(data){
//                        alert(data);
                        $("#zoomTrendGraph").html(data);
                    }
                });
            }

                 function showTrend(id)
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
          function getSuffixVals(){
              $("#suffixes").show();
          }
          function getPrefixVals(){
              $("#prefixes").show();
          }
          function hidePrefixVals(){
              $("#prefixes").hide();
          }
          function hidesuffixVals(){
              $("#suffixes").hide();
          }


          function complexgetSuffixVals(){
              $("#suffixescomplex").show();
          }
          function complexgetPrefixVals(){
              $("#prefixescomplex").show();
          }
          function complexhidePrefixVals(){
              $("#prefixescomplex").hide();
          }
          function complexhidesuffixVals(){
              $("#suffixescomplex").hide();
          }
                 function showFont(id)
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
                     function showMeasure(id)
          {
              $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
             if(colorCode!=undefined && colorCode!="")
              {
                 $("#color").val(colorCode)
                 $("#color").css("background-color",colorCode)
//                 jQuery.updateValue('event')
            $.farbtastic("#color").setColor(colorCode)
              }


          }

          function saveSelectedColor()
          {
             // var seletedTextId= $("#selectedId").val();
             var slectedid=$("#selectedId").val();
//             alert(slectedid);
//             alert($("#logicalChecked").is(':checked'));
             if($("#logicalChecked").is(':checked')){
//               alert(slectedid);
              var colorCode=$("#color").val();
              $("#"+slectedid).css("background-color",colorCode);
              $("#"+slectedid).attr('colorCode',colorCode);
          }
             if(slectedid!="" && slectedid!="trendcolor" && slectedid!="measureColor" && slectedid!="logicalColorId"){
            var id=$("#selectedId").val()
              var colorCode=$("#color").val();
              $("#fontColor").css("background-color",colorCode)
              $("#fontColor").attr('colorCode',colorCode);
               $("#fontColorCode").val(colorCode);
          }
          else if(slectedid!="" && slectedid!="fontColor" && slectedid!="trendcolor" && slectedid!="logicalColorId"){
                  var id=$("#selectedId").val()
              var colorCode=$("#color").val();
              $("#measureColor").css("background-color",colorCode)
              $("#measureColor").attr('colorCode',colorCode);
               $("#measureColorCode").val(colorCode);
          }else if(slectedid != null && slectedid == "measureColorcomplex" && slectedid!="logicalColorId"){
               var id=$("#selectedId").val()
              var colorCode=$("#color").val();
              $("#measureColorcomplex").css("background-color",colorCode)
              $("#measureColorcomplex").attr('colorCode',colorCode);
               $("#measureColorCodecomplex").val(colorCode);
          }
          else{
          var colorCode=$("#color").val();
              $("#trendcolor").css("background-color",colorCode)
              $("#trendcolor").attr('colorCode',colorCode);
              $("#trendColorCode").val(colorCode);
          }
              $("#colorsDiv").dialog('close')
          }
          function showMeasureComplex(id)
          {
              $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
             if(colorCode!=undefined && colorCode!="")
              {
                 $("#color").val(colorCode)
                 $("#color").css("background-color",colorCode)
//                 jQuery.updateValue('event')
            $.farbtastic("#color").setColor(colorCode)
              }


          }
function resizeIframe(obj)
	 {

	   obj.style.height = obj.contentWindow.document.body.scrollHeight + 'px';
	  // obj.style.width = obj.contentWindow.document.body.scrollWidth + 'px';
	 }
         function graphImage(divId,regionId,callFrom){
        //alert('in graphImage');
        $(".overlapDiv").hide();
       if(document.getElementById("reigonOptionsDivId"+regionId)!=null && document.getElementById("reigonOptionsDivId"+regionId).style.display=='block'){
          document.getElementById("reigonOptionsDivId"+regionId).style.display='none';
       }
        var obj = $("#"+divId);
        var newCanvas = document.createElement("canvas");
        var size = findPlotSize(obj);
        newCanvas.width = size.width;
        newCanvas.height = size.height;
//        alert(newCanvas.width)
//        alert(newCanvas.height)
//
        // check for plot error
        var baseOffset = obj.offset();
        if (obj.find("canvas.jqplot-base-canvas").length) {
            baseOffset = obj.find("canvas.jqplot-base-canvas").offset();
            baseOffset.left -= parseInt(obj.css('margin-left').replace('px', ''));
        }

        // fix background color for pasting
        var context = newCanvas.getContext("2d");
        var backgroundColor = "rgba(255,255,255,1)";
        obj.children(':first-child').parents().each(function () {
            if ($(this).css('background-color') != 'transparent') {
                backgroundColor = $(this).css('background-color');
                return false;
            }
        });
        context.fillStyle = backgroundColor;
        context.fillRect(0, 0, newCanvas.width, newCanvas.height);

        // add main plot area
        obj.find('canvas').each(function () {
            var offset = $(this).offset();
            newCanvas.getContext("2d").drawImage(this,
            offset.left - baseOffset.left,
            offset.top - baseOffset.top
        );
        });

        obj.find(".jqplot-series-canvas > div").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.fillStyle = $(this).css('background-color');
            context.fillRect(
            offset.left - baseOffset.left - parseInt($(this).css('padding-left').replace('px', '')),
            offset.top - baseOffset.top,
            $(this).width() + parseInt($(this).css('padding-left').replace('px', '')) + parseInt($(this).css('padding-right').replace('px', '')),
            $(this).height() + parseInt($(this).css('padding-top').replace('px', '')) + parseInt($(this).css('padding-bottom').replace('px', ''))
        );
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            context.textAlign = getTextAlign($(this));
            var txt = $.trim($(this).html()).replace(/<br style="">/g, ' ');
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top - parseInt($(this).css('padding-top').replace('px', '')), $(this).width(), lineheight);
        });

        // add x-axis labels, y-axis labels, point labels
        obj.find('div.jqplot-axis > div, div.jqplot-point-label, div.jqplot-error-message, .jqplot-data-label, div.jqplot-meterGauge-tick, div.jqplot-meterGauge-label').each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top - 2.5, $(this).width(), lineheight);
        });

        // add the title
        obj.children("div.jqplot-title").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
//            context.textAlign = getTextAlign($(this));
            context.fillStyle = $(this).css('color');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top, newCanvas.width - parseInt(obj.css('margin-left').replace('px', ''))- parseInt(obj.css('margin-right').replace('px', '')), lineheight);
        });

        // add the legend
        obj.children("table.jqplot-table-legend").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.strokeStyle = $(this).css('border-top-color');
            context.strokeRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).width(),$(this).height()
        );
            context.fillStyle = $(this).css('background-color');
            context.fillRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).width(),$(this).height()
        );
        });

        // add the swatches
        obj.find("div.jqplot-table-legend-swatch").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.fillStyle = $(this).css('border-top-color');
            context.fillRect(
            offset.left - baseOffset.left,
            offset.top - baseOffset.top,
            $(this).parent().width(),$(this).parent().height()
        );
        });

        obj.find("td.jqplot-table-legend").each(function() {
            var offset = $(this).offset();
            var context = newCanvas.getContext("2d");
            context.font = [$(this).css('font-style'), $(this).css('font-size'), $(this).css('font-family')].join(' ');
            context.fillStyle = $(this).css('color');
            context.textAlign = getTextAlign($(this));
            context.textBaseline = $(this).css('vertical-align');
            var txt = $.trim($(this).text());
            var lineheight = getLineheight($(this));
            printAtWordWrap(context, txt, offset.left-baseOffset.left, offset.top - baseOffset.top + parseInt($(this).css('padding-top').replace('px','')), $(this).width(), lineheight);
        });
        if(callFrom=='PDF'){
            return newCanvas.toDataURL();
        }else{
        $("#testForId").html("<img src='"+newCanvas.toDataURL()+"'>");
        $("#testForId").dialog({
            autoOpen: false,
            //                        height: (newCanvas.height+50),
            //                        width: (newCanvas.width+50),
            position: 'justify',
            modal: true
        });
        $("#testForId").dialog('option', 'height', (newCanvas.height+60));
        $("#testForId").dialog('option', 'width', (newCanvas.width+60));
        $("#testForId").dialog('open');

        }





        //
    }
    function getLineheight(obj) {
        var lineheight;
        if (obj.css('line-height') == 'normal') {
            lineheight = obj.css('font-size');
        } else {
            lineheight = obj.css('line-height');
        }
        return parseInt(lineheight.replace('px',''));
    }

    function getTextAlign(obj) {
        var textalign = obj.css('text-align');
        if (textalign == '-webkit-auto') {
            textalign = 'left';
        }
        return textalign;
    }

    function printAtWordWrap(context, text, x, y, fitWidth, lineheight) {
        var textArr = [];
        fitWidth = fitWidth || 0;

        if (fitWidth <= 0) {
            textArr.push(text);
        }

        var words = text.split(' ');
        var idx = 1;
        while (words.length > 0 && idx <= words.length) {
            var str = words.slice(0, idx).join(' ');
            var w = context.measureText(str).width;
            if (w > fitWidth) {
                if (idx == 1) {
                    idx = 2;
                }
                textArr.push(words.slice(0, idx - 1).join(' '));
                words = words.splice(idx - 1);
                idx = 1;
            } else {
                idx++;
            }
        }
        if (words.length && idx > 0) {
            textArr.push(words.join(' '));
        }
        if (context.textAlign == 'center') {
            x += fitWidth/2;
        }
        if (context.textBaseline == 'middle') {
            y -= lineheight/2;
        } else if(context.textBaseline == 'top') {
            y -= lineheight;
        }
        for (idx = textArr.length - 1; idx >= 0; idx--) {
            var line = textArr.pop();
            if (context.measureText(line).width > fitWidth && context.textAlign == 'center') {
                x -= fitWidth/2;
                context.textAlign = 'left';
                context.fillText(line, x, y + (idx+1) * lineheight);
                context.textAlign = 'center';
                x += fitWidth/2;
            } else {
                context.fillText(line, x, y + (idx+1) * lineheight);
            }
        }
    }

    function findPlotSize(obj) {
        var width = obj.width();
        var height = obj.height();
        var legend = obj.find('.jqplot-table-legend');
        if (legend.position()) {
            height = legend.position().top + legend.height();
        }
        obj.find('*').each(function() {
            var offset = $(this).offset();
            tempWidth =  $(this).width()
            tempHeight = $(this).height()
            if(tempWidth > width) {width = tempWidth;}
            if(tempHeight > height) {height = tempHeight;}
        });
        return {width: width, height: height};
    }
    function saveOneVIewReg(){
        $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=versionCheck&oneViewIdValue='+oneviewID+'&oneviewName='+oneviewName,
         function(data){
             var jsonVar=eval('('+data+")")
             var oneVersion=jsonVar.oneVersion;
             if(oneVersion=='1.0'||oneVersion=='1.1'){
                 alert("please press go before save\n because you are in older version of oneview")
             }else{
                 saveOneVIewReg1();
             }
         });
    }

   function saveOneVIewReg1(){
       graphtoreport='';  //kruthika
            var innerWidth=parseInt(window.innerWidth)-120;
            <%String olaphidefn12 =(String) session.getAttribute("oneviewdatetype");%>
                     var hideolap ='<%=olaphidefn12%>'
             var size;
       var icons="";
            if($("#saveTabId0").is(':visible')){
                    icons="hide";
                }else{
                    icons="visible";
                }
                var date = $("#dateId").val();
                var duration = $("#timedetailselectId").val();
                var compare = $("#compareSelectId").val();
                var value= document.getElementById("regionTableId");
                var confirmText='';
                confirmText= confirm("All Region changes would be saved using the Local Save button ONLY. This SAVE option only persists the Global Changes.");
                if(confirmText == true){
                $("#designId").css({'height':'400px'})
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                    $.post('<%=request.getContextPath()%>/oneViewAction.do?templateParam2=saveOneViewReg&reportBy=viewReport&oneViewIdValue='+oneviewID+'&oneviewName='+oneviewName+'&date='+date+'&duration='+duration+'&compare='+compare+'&innerWidth='+innerWidth+'&action=save&icons='+icons,
                function(data){
                    if(data!='NO DATA'){
                        document.getElementById("designId").style.display='';
                        $("#designId").css({'padding-bottom':''})
                        $("#designId").css({'height':''})
//                        document.getElementById("regionTableId").innerHTML=data;
                        $("#regionTableId").html(data);
                         buildd3charts(oneviewID)
//                         $.post('<%=request.getContextPath()%>/reportViewer.do?reportBy=gettingJsonData&oneViewIdValue='+oneviewID,
//                function(data){
//                     var jsonVar=eval('('+data+')');
//                 var  regionids=jsonVar.regionids
//                 var  reportids=jsonVar.reportids
//                 var  chartnames=jsonVar.chartnames
//                  var  busrolename=jsonVar.busrolename;
//                 for(var i=0;i<regionids.length;i++){
//                      $("#graphsId").val(reportids[i]);
//                      $("#busrolename").val(busrolename[i]);
////                     generateJsonDataOneview("Dashlets-"+regionids[i],oneviewID,regionids[i],"open")
//setTimeout(generateJsonDataOneview("Dashlets-"+regionids[i],oneviewID,regionids[i],chartnames[i],"add"), 20000);
//                 }
//                     });
                        initCollapser("");
//                         home();
                    }
                    else{
                        $("#designId").css({'height':'400px'})
                        $("#designId").css({'padding-bottom':''})
                        var display="<tr><td height='300px' align='center' >NO DATA</td></tr>";
                        document.getElementById("designId").style.display='';
                         $("#regionTableId").html(display);
//                        document.getElementById("regionTableId").innerHTML=display;

                    }

                });
   }
   var size;
                     var visible;
       $.ajax({
         url:  parent.ctxpath+'/oneViewAction.do?templateParam2=NoOfRegions&oneViewIdValue='+oneviewID+'&icons=al',
         success:function(data){

         var  datalist = new Array();
         datalist = data.toString().split(",");
         size=datalist[0].replace("[","").replace("]","");
         visible=datalist[1].replace("[","").replace("]","").trim();alert(visible);
      if(visible!='hide'){
     for(var j=0;j<size;j++){
       $("#refreshTabId"+j).show();
       $("#saveTabId"+j).show();
       $("#optionId"+j).show();
       $("#optionIds"+j).show();
       if(hideolap=='true'){
 $("#alertTabId"+j).hide();
       parent.$("#measureNavigateId"+j).hide();
                        parent.$("#relatedMeasureInfoId"+j).hide();
       }else{
          $("#alertTabId"+j).show();
       }
   }
             }
       else{ for(var j=0;j<size;j++){
       $("#refreshTabId"+j).hide();
       $("#saveTabId"+j).hide();
       $("#optionId"+j).hide();
       $("#optionIds"+j).hide();
       $("#alertTabId"+j).hide();
         }
                              }
       }
            });
   }
    function checkFrequency(id){
    alert(id);
    var xyz=$("#"+id).val();
    alert(xyz);
                if($("#"+id).val()=="Weekly"){
		 $("#weekday").show();
		 $("#monthday").hide();
		 $("#hourlyId").hide();
		}else if($("#"+id).val()=="Monthly"){
		$("#weekday").hide();
		$("#hourlyId").hide();
		$("#monthday").show();
		}else if($("#"+id).val()=="Hourly"){
                $("#weekday").hide();
		$("#hourlyId").show();
		$("#monthday").hide();
                }
                else{
		$("#monthday").hide();
		$("#weekday").hide();
		$("#hourlyId").hide();
		}
            }

        function customDateCheck(id){
            if($("#"+id).val()=="customdate"){
		 $("#customSysdateId").show();
		 $("#customGlobaldateId").show();
            }
            else if($("#"+id).val()=="oneviewdate"){
                 $("#customSysdateId").hide();
		  $("#customGlobaldateId").hide();
              }
              else if($("#"+id).val()=="todaydate"){
                $("#customSysdateId").hide();
		  $("#customGlobaldateId").hide();
              }
              else if($("#"+id).val()=="yesterdate"){
                 $("#customSysdateId").hide();
		  $("#customGlobaldateId").hide();
              }
        }
        condIdx = ""
    tableCondition = ""
    function addCondRow()
    {
        var rowCount =  document.getElementById("condTable").rows.length;
        condIdx = rowCount ;
        var rowID="cond"+condIdx
        var condHtml="";
        var temTrgtType="";
        if(tableCondition=="trgtBasic"){
            temTrgtType="When Dev%";
        }else{
            temTrgtType="When Value";
        }
        var condHtml="";
        condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
        condHtml+="<td>";
        condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">"+
                    <%for (String Str : strOprtrs) {%>
                       "<option value='<%=Str%>'><%=Str%></option>"+
                    <%}%>
                      "</select></td>";
                      condHtml+="<td><input type='text' style='width: 80px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
                 <td><input type='text' style='width: 80px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
               <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
               <td>Tag</td><td><select id=\""+condIdx+"tagValueId\" name='tagValues'><option value='normal'>Normal</optiong><option value='important'>Important</optiong><option value='critical'>Crictical</optiong></select></td>\n\
               <td><img border='0' align='middle' title='Add Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
              <td><img border='0' align='middle' title='Delete Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";
                                if(rowCount > 0){
                          $('#condTable tr:last').after(condHtml);

                      }else{
                          $("#condTable").html(condHtml)
                      }
                  }
                  function deleteCondRow(rowId)
                  {
                      var rowId=rowId.substr(4);
                      var table = document.getElementById("condTable");
                      var rowCount = table.rows.length;
                      if(rowCount> 1) {
                          table.deleteRow(rowId);
                      }
                      else{
                          alert("You cannot delete all the rows");
                      }
                  }

                  function absoluteCond(){
                      $("#targetConditions").hide();
                      $("#trackerCondition").show();
                      tableCondition = "absolute"
                      var condIdx = "0";
                      var rowID="cond"+condIdx
                      var condHtml="";
                      var temTrgtType="";

                      temTrgtType="When Value";
                      var condHtml="";
                      condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
                      condHtml+="<td>";
                      condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">"+
                    <%for (String Str : strOprtrs) {%>
                       "<option value='<%=Str%>'><%=Str%></option>"+
                    <%}%>
                      "</select></td>";
                      condHtml+="<td><input type='text' style='width: 80px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
                 <td><input type='text' style='width: 80px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
               <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
               <td>Tag</td><td><select id=\""+condIdx+"tagValueId\" name='tagValues'><option value='normal'>Normal</optiong><option value='important'>Important</optiong><option value='critical'>Crictical</optiong></select></td>\n\
               <td><img border='0' align='middle' title='Add Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
              <td><img border='0' align='middle' title='Delete Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";
                      //<td>Send Anyway<input type='checkbox'  id=\""+condIdx+"sendCheck\" class='myTextbox3' name='sendCheck' style='width: auto'></td>\n\
                      $('#condTable').html('');
                      $('#condTable').html(condHtml);

                  }
                  function targetCond(){
                      $("#targetConditions").show();
                      tableCondition="trgtBasic";
                      $("#deviationPercent").val('')
                      $("#trgetVal").val('')
                      var condIdx = "0" ;
                      var rowID="cond"+condIdx
                      var condHtml="";
                      var temTrgtType="";

                      temTrgtType="When Dev%";
                      var condHtml="";
                      condHtml+="<tr id='"+rowID+"'><td align='left'><span id='condTD"+condIdx+"'>"+temTrgtType+"</span></td>";
                      condHtml+="<td>";
                      condHtml+="<select name=\"condOp\" id=\""+condIdx+"condOp\" onchange=\"addTextBox(this,'"+condIdx+"')\">"+
                    <%for (String Str : strOprtrs) {%>
                       "<option value='<%=Str%>'><%=Str%></option>"+
                    <%}%>
                      "</select></td>";
                      condHtml+="<td><input type='text' style='width: 80px;' id=\""+condIdx+"sCondVal\" name='sCondVal' class='myTextbox3'></td>\n\
                 <td><input type='text' style='width: 80px; display: none;' id=\""+condIdx+"eCondVal\" name='eCondVal' class='myTextbox3'></td>\n\
               <td>Send Mail to </td><td><input type='text' id=\""+condIdx+"condMail\" class='myTextbox3' name='condMail' style='width: auto'> </td>\n\
               <td>Tag</td><td><select id=\""+condIdx+"tagValueId\" name='tagValues'><option value='normal'>Normal</optiong><option value='important'>Important</optiong><option value='critical'>Crictical</optiong></select></td>\n\
               <td><img border='0' align='middle' title='Add Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/plus-circle.png' onclick='addCondRow()'></td>\n\
              <td><img border='0' align='middle' title='Delete Row' alt='' src='<%=request.getContextPath()%>/icons pinvoke/cross-circle.png' onclick=\"deleteCondRow('"+rowID+"')\"></td></tr>";
                      //<td>Send Anyway<input type='checkbox'  id=\""+condIdx+"sendCheck\" class='myTextbox3' name='sendCheck' style='width: auto'></td>\n\
                      $('#condTable').html('');
                      $('#condTable').html(condHtml);

                  }
                  function addTextBox(symbol,rowId)
                  {
                      var open = document.getElementById(rowId+"eCondVal");
                      if(symbol.value=="<>")
                      {
                          open.style.display='';
                      }
                      else{
                          open.style.display='none';
                      }
                  }
            function targetValue12(id){
                    var targetVal=document.getElementById("trgetVal").value;
                      var currVal=document.getElementById("measureValueId").value;

                      var devPercent=  ((currVal-targetVal)/targetVal)*100;

                      var result=Math.round(devPercent*100)/100

                     // document.getElementById("deviationPer").style.display="";
                      $("#deviationPercent").val(result);
            }

        function prevYearComparisionYes(){
            var value= document.getElementById("measGraphIdTab");//prevYearComp
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
           //alert//($("#prevYearComp").val());
           //if($("#prevYearComp").val()=='Yes'){alert("1");
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getMeasureGraph&measId='+grpMeasId+'&measName='+grpMeasName+'&oneViewId='+grponeViewIdd+'&roleId='+grproleId+'&prevYearComp=true',
                    success: function(data){
                        $("#measGraphIdTab").html("");
                        $("#measGraphIdTab").html(data);
                    }
                    });
            }
          // else if($("#prevYearComp").value=='No'){alert("2");
            function prevYearComparisionNo(){
                 var value= document.getElementById("measGraphIdTab");//prevYearComp
                value.innerHTML='<center><img id="imgId" width="200px" height="200px" src=\"<%=request.getContextPath()%>/images/ajax1.gif\" align="middle"  width="75px" height="75px"  style="position:absolute" ></center>';
                $.ajax({
                    url: 'oneViewAction.do?templateParam2=getMeasureGraph&measId='+grpMeasId+'&measName='+grpMeasName+'&oneViewId='+grponeViewIdd+'&roleId='+grproleId+'&prevYearComp=false',
                    success: function(data){
                        $("#measGraphIdTab").html("");
                        $("#measGraphIdTab").html(data);
                    }
                });

            }

        function measureSelection(roleId,oneviewID){
        $("#AddMoreParamsDiv").dialog({
                        autoOpen: false,
                        height: 420,
                        width: 500,
                        position: 'justify',
                        modal: true
                    });
                var frameObj=document.getElementById("addmoreParamFrame");
                var source="reportTemplateAction.do?templateParam=addMoreDimensions&isOneview=true&foldersIds="+roleId+"&REPORTID="+oneviewID;
                frameObj.src=source;
                $("#AddMoreParamsDiv").dialog('open');
        }

function saveDimsForTrend1(dimUl,dimIds,oneViewId,roleId){

    var dims="";
    var dimName="";
     for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dkpiIds[0];
                }
    if(dimIds.length!=0){
        dims=dims.substr(1);
        dims=dims.replace("elmnt-","","gi");
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
                }
     $.ajax({
          url: 'oneViewAction.do?templateParam2=setDimensionDetails&oneviewId='+oneViewId+'&dimIds='+dims+'&dimName='+dimName+'&roleId='+roleId,
                    success: function(data){
//                        $("#measGraphIdTab").html(data);
            }
                });$("#AddMoreParamsDiv").dialog('close');
        }



        function gototopbottomview (){
            var preOneId1= preOneId;
            var premeasId1= premeasId;
            var preroleId1= preroleId;
            var premonthY1=premonthY;
            var same="true";
            relatedParametersDrill(premeasId1,preOneId1,preroleId1,premonthY1,same)



}
        function dispDims(reportId){
      alert("iam in pbReportviewerJS.js")
    var dims="";
    var dimName="";
    var dimUl=document.getElementById("sortable");
    var dimIds=dimUl.getElementsByTagName("li");alert(dimIds);
    var reportId = parent.document.getElementById("REPORTID").value;

    for(var i=0;i<dimIds.length;i++){
        var dkpiIds=(dimIds[i].id).split("^");
        dims=dims+","+dkpiIds[1];
        dimName=dimName+","+dimIds[i].id;
    }
    if(dimIds.length!=0){
        dims=dims.substr(1);
        dimName=dimName.substr(1);
        dimName=dimName.replace("%","%".charCodeAt(0),"gi");
    }
//    alert("dims"+dims)
//var ctxPath=document.getElementById("h").value;
$.ajax({
        url:'reportTemplateAction.do?templateParam=saveNewDimensions&ReportId='+reportId+'&dimIds='+dims+'&dimName='+dimName,
        success:function(data)
        {busroleForMeasure()
          alert("Changes Will Be Reflected Once You Reset The Report")
        }
});
      parent.cancelDim();
 }
//
//    function OverLayTrend(divId){
//
//
//
//      $("#measGraphOverLayDiv").dialog({
//                        autoOpen: false,
//                        height: 550,
//                        width: 770,
//                        position: 'justify',
//                        modal: true
//                    });
//                 $("#measGraphOverLayDiv").dialog('open')
//
//        var htmlstr="";
//        htmlstr+="<table width='730px' height='500px'><tr><td width='auto'>";
//        htmlstr+="<div style='width:620px;height:500px'></td>";
//        htmlstr+="<td><div style='width:120px;height:500px'><table top='0px'><tr><td><span>Period</span></td><td><select id='periodSelect' name='periodSelect'><option value='All'>All</optiong><option value='2nd'>2nd</optiong></select></td></tr>";
//        htmlstr+="<tr><td><span>Parameters</span></td><td><select id='availParameters' name='availParameters'><option value='1st'>1st</optiong><option value='2nd'>2nd</optiong></select></td></tr></table></div></td></tr></table>";
//        alert(htmlstr);
//        $("#measureOverLayTd").html(htmlstr);
//    }
function oneviewSelection(){
    var oneViewType=$("#oneViewType").val();
     if(oneViewType=="Generic OneView"){
         $("#genericview").show();
         $("#businessView").hide();
     }else{
         $("#genericview").hide();
         $("#businessView").show();
     }
}
        </script>



    </head>
    <body>

        <table style="width:100%;">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/headerPage.jsp"/>
                </td>
            </tr>
        </table>
        <form name="frmParameter" id="" method="POST" action="">
            <table   id="showNameId" style="height:auto; padding-top: 1.5em">
                <tr>
                    <td id="gloabalFilter"  style="display:none" ><a class="ui-icon ui-icon-triangle-1-e"  href="#" onclick="getRolesForOneView(oneviewID)"></a>

                    </td>
                    <td id="onveViewByName" style="font-size:20pt" width="1250px" >
                    </td>
                    <!--                    <td id="busNamId" style="display: none" >Role:</td>
                                        <td id="busroleTdId" style="display: none" width="5%">
                                            <select id="busroleId" >

                                                <option value="">select</option>
                    <%
                                if (!busRoleIDs.isEmpty()) {
                                    for (int i = 0; i < busRoleIDs.size(); i++) {
                    %>
                    <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                    <%
                                    }
                                }%>
                </select>
            </td>-->

                    <td width="1%">
                        <table><tr>
                                <td><a href="javascript:void(0)" id="createdId" style="display: none" onclick="createoneviewRegion()" class="ui-icon ui-icon-plusthick"><input  name=""   ></a></td>
                        <td><a href="javascript:void(0)" id="clearId" style="display: none" onclick="clearRegion()" class="ui-icon ui-icon-arrowrefresh-1-w"><input  name=""  ></a></td>
                        <td><a id="savebuttonId" style="display: none" href="javascript:void(0)" onclick="saveOneview()" class="ui-icon ui-icon-disk"><input name="" id="saveId" ></a></td>
                         <td><a id="favreportid" style="display: none" href="javascript:void(0)" onclick="getFavreportsinoneview()" class="ui-icon ui-icon-newwin" title="Fav Reports"><input name="" id="saveId" ></a></td>
                         <td><a id="cacheId" style="display: none" href="javascript:void(0)" onclick="createcacheobject('<%=request.getContextPath()%>',oneViewId)" class="ui-icon ui-icon-newwin" title="oneview cache"><input name="" id="cacheId" ></a></td>
                       <td > <a id="schedularrid" style="display: none" href="javascript:void(0)" class="ui-icon ui-icon-transferthick-e-w"  title="Schedule one view" onclick="scheduletheoneview( oneViewId , oneviewName )"></a></td>
                       <td><a onclick="applyGlobalFilter('<%=request.getContextPath()%>',oneViewId)" id="globalFilter" href="javascript:void(0)" title="Global Filter"><img alt="Filters" src="<%=request.getContextPath()%>/images/filter.png" /></a></td>
  </tr></table>
                    </td>


<td id="oneViewMeasure" width="1%" style="display: none;"><a  class="ui-icon ui-icon-pencil" title="Edit Measures" style="text-decoration:none;" onclick="changeTemplateMeasures('<%=request.getContextPath()%>');" href="javascript:void(0)"></a></td>
                    <td width="1%">

                        <a id="oneViewSettings_Id" style="margin-right: 0pt; margin-left: pt;display: none;" href="javascript:void(0)" class="ui-icon ui-icon-gear" onclick="oneViewSettings()" title="Settings"/>
                    </td>
                    <td><a id="oneViewPDF_Id" style="margin-right: 0pt; margin-left: pt;display: none;" href="javascript:void(0)" class="ui-icon ui-icon-document" onclick="oneViewPdfSettings()" title="DownloadAsPDF"/></td>
                    <td><a id="oneViewHide_Id" style="margin-right: 0pt; margin-left: pt;display: none;" href="javascript:void(0)" class="ui-icon ui-icon-circle-triangle-s" onclick="oneViewIcons()" title="Hide Icons"/></td>
                   <td width="0%"><div id="fixedtop1"style="display:none;" > <div id="center250b">

                                                            <div class="form clearFix">
                                                                <span class="wr100">
                                                                    <table align="center" id="roundtrip">
                                                                   <tr width="100%">
                                                                       <td id="fdatetdspan" style="display: none;">
        <span id="depShow">
        <span class="top w100 mrtop" id="pfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="pfield2"></small></span>
        <span class="bottom w100" id="pfield3"></span></span>
                    </td>

                   <td id="fdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="dateId1"  name="dateId1" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;


          <td  id="totd"align="left" style="white-space:nowrap ; display: none;width: auto;padding-left: 1.5em">To :</td>
         <td id="tdatetdspan" style="display: none">
        <span class="top w100 mrtop" id="tdfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="tdfield2"></small></span>
        <span class="bottom w100" id="tdfield3"></span>
                    </td>
                   <td id="tdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="todate"  name="todate" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
                   <td id="compare" align="right" style="font-weight:bold ;display: none; width: auto;padding-left: 1.5em"> COMPARE</td>
                    <td align="right" style="width: auto;padding-left: 1.5em"></td>
                  <td id="cfdatetdspan" style="display: none;">
        <span class="top w100 mrtop" id="cffield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="cffield2"></small></span>
        <span class="bottom w100" id="cffield3"></span>
                    </td>
                   <td id="cfdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="comparefrom"  name="comparefrom" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
          <td  id="totd1"align="left" style="white-space:nowrap ; display: none;width: auto;padding-left: 1.5em">To :</td>
         <td id="ctdatetdspan" style="display: none">
        <span class="top w100 mrtop" id="ctfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="ctfield2"></small></span>
        <span class="bottom w100" id="ctfield3"></span>
                    </td>
                   <td id="ctdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="compareto"  name="compareto" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;
         <td id="sdatetdspan"style="display: none;">
        <span class="top w100 mrtop" id="stfield1"></span>
        <span class="date">
        <small Style="font-weight: bold" id="stfield2"></small></span>
        <span class="bottom w100" id="stfield3"></span>
                    </td>
                   <td id="sdatetdcal"style="display: none;"> <input height="100px" width="100px" type="hidden" class="datepicker" id="stdate"  name="stdate" onclick="showdate()" /></td><td width="10%"></td> &nbsp;&nbsp;&nbsp;&nbsp;


                    <td id="timedetailsId1" style="display: none;padding-left: 1.5em" width="5%">
                        <select id="timedetailselectId1">
                            <option id="Day" value="Day">Day</option>
                            <option id="Week" value="Week">Week</option>
                            <option id="Month" value="Month">Month</option>
                            <option id="Quarter" value="Quarter">Quarter</option>
                            <option id="Year" value="Year">Year</option>
                        </select>
                    </td><td width="3%"></td>  &nbsp;&nbsp;&nbsp;
                    <td id="compareWithId1" style="display: none ; padding-left: 1.5em" width="5%">
                        <select id="compareSelectId1">
                            <option id="Last Period" value="Last Period">Last Period</option>
                            <option id="Last Year" value="Last Year">Last Year</option>
                            <option id="Period Complet" value="Period Complete">Period Complete</option>
                            <option id="Year Complet" value="Year Complete">Year Complete</option>
                        </select>
                    </td>
					<td width="3%"></td>  &nbsp;&nbsp;&nbsp;
<td id="dateToggle" style="padding-left: 1.5em"><a class="ui-icon ui-icon-transferthick-e-w" onclick="oneviewdatetoggle(oneviewID)" title="Toggle" ></a></td>

                    <td id="goTabId1" style="display: none; padding-left: 1.5em" ><input type="button" name=""  class="navtitle-hover" id="gottabId1" style='width:25px' value="Go" onclick="gotForOneview('Go')"></td>
                       </tr>
        </table>
                                                                </span>
 </div>


                                                       </div></div>

                    </td>
                        <td id="datedetailId" style="display: none" width="5%" >
                        <input type="text" id="dateId" name="dateName" class="datePicker"  value="" size="10">

                    </td>
                    <td id="timedetailsId" style="display: none" width="5%">
                        <select id="timedetailselectId">
                            <option id="Day" value="Day">Day</option>
                            <option id="Week" value="Week">Week</option>
                            <option id="Month" value="Month">Month</option>
                            <option id="Quarter" value="Quarter">Quarter</option>
                            <option id="Year" value="Year">Year</option>
                        </select>
                    </td>
                    <td id="compareWithId" style="display: none" width="5%">
                        <select id="compareSelectId">
                            <option id="Last Period" value="Last Period">Last Period</option>
                            <option id="Last Year" value="Last Year">Last Year</option>
                            <option id="Period Complet" value="Period Complete">Period Complete</option>
                            <option id="Year Complet" value="Year Complete">Year Complete</option>
                        </select>
                    </td>
                    <td id="goTabId" style="display: none" ><input type="button" name=""  class="navtitle-hover" id="gottabId" style='width:25px' value="Go" onclick="gotForOneview('Go')"></td>
                    <% if(isPowerAnalyserEnableforUser) { %>
                    <td id="saveTabId" style="display: none" ><a style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" class="ui-icon ui-icon-disk" title="Save" onclick="saveOneVIewReg()" href="#"></a></td>
                    <% } %>
                    <td id="homeId" style="display: none" ><a class="ui-icon ui-icon-home" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" title="OneView Home" onclick="home1()" href="#"></a></td>
                    <!--                    <td id="homeId" style="display: none" ><input type="button" name=""  value="Back" onclick="home()"></td>-->
<!--                    <td id="refreshId"  style="display: none" ><a class="ui-icon ui-icon-refresh" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;" title="Reset" onclick="viewOneBy(oneviewName, oneviewID)" href="#"></a></td>-->
               <% if(isPowerAnalyserEnableforUser){ %>
               <td id="addregionsid" style="display: none" ><a href="javascript:void(0)" onclick="addRegioninViewer(oneviewID)" title="Add Region" class="ui-icon ui-icon-plusthick" title="Add Regions"></a></td>
                <% } %>
                </tr>
            </table>
                <div id="globalFilterDiv" style='display: none;position:absolute;text-align:left;background-color:white ;border:1px solid #000000;direction:ltr;z-index: 1000;float: left;'>
                        <table border="0" align="right"><tr><td style="padding-right:156px;padding-top:5px;color:#369;" align='left' ><a href='javascript:void(0)' onclick="resetGlobalFilter()">Reset Filter</a></td><td align="right"><a onclick=" getRolesForOneView(oneviewID)" title="hide" class="ui-icon ui-icon-triangle-1-w" href="#"></a></td></tr></table>
                        <table>
                         <tr>
                            <td style="white-space: nowrap;">Business Role</td>
                            <td>:<select id="BisiessRole" onchange="getDimensionsForOneView(oneviewID)"></select></td>
                        </tr>
                        <tr>
                            <td>Parameter</td>
                            <td>:<select id="selectedMeasure" onchange="getMeasureForOneView(oneviewID)"><option value="select">--select--</option></select></td>
                        </tr>
                        <tr>
                            <td>Measure</td>
                            <td>:<select id="measureValues"><option value="select">--select--</option></select></td>
                        </tr>
                        <tr>
                            <td align="center" colspan="2">
                                <input type="button" onclick="applyGlobalSave(oneviewID,name)" class="navtitle-hover" value="Go" name="Go" id="Go" style='width:40px'>
                                </td>
                        </tr>
                    </table>
               </div>

            <div id="tabs" class="ui-tabs ui-widget ui-widget-content ui-corner-all" style="width: 100%; min-height: 100%; max-height: 100%; display: none"  >

                <ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all ui-sortable">
                    <%
                    int j=0;
                    String val = "";
                    if (!viewByIdsSeq.isEmpty()) {
                        j = viewByIdsSeq.size();
                        if(j<7){
                           for (int i = 0; i < j; i++) {
                   if (viewByNamesSeq.get(i).length() < 22) {
                    val = viewByNamesSeq.get(i);
                   } else {
                    val = viewByNamesSeq.get(i).substring(0, 22)+"..";
                                    }%>
                    <li class="ui-state-default ui-corner-top">
                        <a onclick="viewOneBy('<%=viewByNamesSeq.get(i)%>','<%=viewByIdsSeq.get(i)%>')" title="<%=viewByNamesSeq.get(i)%>"><%=val%></a>
                    </li>
                    <%}
                        }else{
                     for (int i = 0; i < 7; i++) {
                   if (viewByNamesSeq.get(i).length() < 22) {
                    val = viewByNamesSeq.get(i);
                   } else {
                    val = viewByNamesSeq.get(i).substring(0, 22)+"..";
                                    }%>
                    <li class="ui-state-default ui-corner-top">
                        <a onclick="viewOneBy('<%=viewByNamesSeq.get(i)%>','<%=viewByIdsSeq.get(i)%>')" title="<%=viewByNamesSeq.get(i)%>"><%=val%></a>
                    </li>
                    <%}%>
                    <li><a onclick="getRemainingOneViews('<%=j%>','<%=viewByNames%>','<%=viewByIds%>')" >More....</a></li>
                    <%-- <li><a href="javascript:void(0)" onclick="home()" >More....</a></li> --%>
                       <% }
                    }else if (!viewByIds.isEmpty()) {
                     if (viewByIds.size() < 7) {
                        j = viewByIds.size();
                     for (int i = 0; i < j; i++) {
                   if (viewByNames.get(i).length() < 22) {
                    val = viewByNames.get(i);
                   } else {
                    val = viewByNames.get(i).substring(0, 22)+"..";
                                    }%>
                    <li class="ui-state-default ui-corner-top">
                        <a onclick="viewOneBy('<%=viewByNames.get(i)%>','<%=viewByIds.get(i)%>')" title="<%=viewByNames.get(i)%>"><%=val%></a>
                    </li>
                    <%}%>
                                   <% }else{
                                        j=viewByIds.size();
                                        for (int i = 0; i < 7; i++) {
                        if (viewByNames.get(i).length() < 22) {
                    val = viewByNames.get(i);
                   } else {
                    val = viewByNames.get(i).substring(0, 22)+"..";
                                }%>
                    <li class="ui-state-default ui-corner-top">
                        <a onclick="viewOneBy('<%=viewByNames.get(i)%>','<%=viewByIds.get(i)%>')" title="<%=viewByNames.get(i)%>"><%=val%></a>
                    </li>
                    <%}%>
                    <li><a onclick="getRemainingOneViews('<%=j%>','<%=viewByNames%>','<%=viewByIds%>')" >More....</a></li>
                                  <%  }
                                }%>
                    <li id="dynamicLiId" class="ui-state-default ui-corner-top">
                        <a onclick="" title="One ViewBy" href="" id="tabIds"></a>
                    </li>

                </ul>
            </div>

<!--            <table align="right" id="designID">
                <tr>
                    <td >
                        <input type="button" value="Create" class="navtitle-hover" style="width:auto"  onclick="oneViewBybutton()" >
                    </td>
                </tr>
            </table>-->
            &nbsp;
<!--            <br id="br1">
            <br id="br2">-->
            <div id="divIdTest1" style=" height: 100px; border-width: 4px; border-style: groove; border-color: skyblue; margin-top: auto; margin-left: 10px; margin-right: 10px;  padding-bottom: 400px; padding-left: 100px;overflow: auto; "> &nbsp;
                 <table align="right" id="designID">
                <tr>
                    <td >
                        <a class="ui-icon ui-icon-arrow-4"  href="javascript:void(0)" title="Sequence Views" onclick="getSequnceOfOneViews()" ></a>
                    </td>
                    <% if(isPowerAnalyserEnableforUser){ %>
                    <td >
                        <a class="ui-icon ui-icon-plusthick"  href="javascript:void(0)" title="Create One View" onclick="oneViewBybutton()" ></a>
                    </td>
                    <% } %>
                </tr>
            </table>
                <div id="divIdTest" style=" height: 100px;  margin-top: auto; margin-right: 200px;  padding-bottom: 370px; padding-left: 100px;"> &nbsp;
                    <div id="pagerReport" align="left" >
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerViews">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allViews" value="<%=viewByIds.size()%>">All</option>

                            </select>
                        </div>
                                <div id="applyGlobalFilter" style="display:none" title="Select Measure To apply Filter">
          <form id="globalFilterfrm"></form>
         </div>
                    <table id="tablesorterReport" class="tablesorter" cellspacing="1" cellpadding="0" border="0px solid" align="left"  width="100%" style="">

                        <thead>
                            <tr>
                                <th align="left" class="header" nowrap="">One View Name</th>
                                <th align="left" class="header" nowrap="">Created On</th>
                                <th align="left" class="header" nowrap="">Created By</th>
                                <th align="left" class="header" nowrap="">Last Modified Date</th>
                                <th align="left" class="header" nowrap="">Last Modified By</th>
                                <th align="left" class="header" nowrap="">Roles</th>
                                <th align="left" class="header" nowrap="">Options</th>

                            </tr>
                        </thead>
                        <%for (int i = 0; i < viewByIds.size(); i++) {%>
                        <tr>
                            <Td width="20%" align="left">
<!--                                        <a > <input type="button" name="oneviews" value="<%=viewByNames.get(i)%>" onclick="viewOneBy('<%=viewByNames.get(i)%>','<%=viewByIds.get(i)%>')" size="30" ></a>-->
                                <a  href="#" onclick="viewOneBy('<%=viewByNames.get(i)%>','<%=viewByIds.get(i)%>')"><%=viewByNames.get(i)%></a>
                            </Td>
                            <Td width="12%" align="left">
                                <%=createdDates.get(i)%>
                            </Td>
                            <Td width="" align="left">
                                <%=createdBy.get(i)%>
                            </Td>
                            <Td width="13%" align="left">
                                <%=modifiedDates.get(i)%>
                            </Td>
                            <Td width="13%" align="left">
                                <%=modifiedBy.get(i)%>
                            </Td>
                            <Td width="10%" align="left">
                                <a href ="javascript:void(0)" onclick="getRoleNamesInOneView('<%=viewroleNames.get(i)%>','<%=viewByNames.get(i)%>')">view</a>
                            </Td>
                            <Td width="" align="left">
  <!--                                        <a > <input type="button" name="oneviews" value="<%=viewByNames.get(i)%>" onclick="viewOneBy('<%=viewByNames.get(i)%>','<%=viewByIds.get(i)%>')" size="30" ></a>-->
                                <table ><tr>
                                        <td valign="top">  <a  href='javascript:void(0)' class="ui-icon ui-icon-trash" title="Delete One View" onclick="delteteOneView('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-document-b"  title="Download HTML"   onclick="downLoadOneviewHtml('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-document"  title="Download PDF"   onclick="parent.downLoadOneviewPdf('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-mail-closed"  title="Email One View"   onclick="sendAsEmail('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-pencil"  title="Rename One View"   onclick="renameOneview('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-person"  title="Assign One View"   onclick="assignOneView('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>','<%=viewroleIds.get(i)%>','<%=createdBy.get(i) %>')"></a></td>
                                        <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-copy"  title="Copy One View" onclick="copyOneView('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>
<!--                                             <td valign="top"> <a  href="javascript:void(0)" class="ui-icon ui-icon-disk"  title="Schedule one view" onclick="scheduletheoneview('<%=viewByIds.get(i)%>','<%=viewByNames.get(i)%>')"></a></td>-->

                                    </tr></table>
                            </Td>

                        </tr>
                        <% }%>

                    </table>

                </div>
            </div>

            <div id="regionDivId" class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;">
                <br>
                <table id="regTable" width="100%">
                    <tbody>
                        <tr >
                            <td class="myhead">Enter no. of Rows </td>
                            <td>
                                <input id="rowId" class="myTextbox5" type="text" maxlength="100" onkeypress="return isNumberKey(event)" name="rows">
                            </td>
                        </tr>
                        <tr>
                            <td class="myhead">Enter no. of Columns </td>
                            <td>
                                <input id="columnId" class="myTextbox5" type="text" maxlength="100" onkeypress="return isNumberKey(event)" name="rows">
                            </td>
                        </tr>
                    </tbody>
                </table>
                <br>
                <table width="100%">
                    <tbody>
                        <tr>
                            <td align="center">
                                <input class="navtitle-hover" type="button" onclick="displayRegion()" value=" Ok ">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>



            <div id="designId" style=" width: 100%; margin-right: 1%;  padding-bottom: 0px;padding-right:  10px;margin-top: -30px; display: none"> &nbsp;
<!--                              <table id="paraminfoId" align="right"><tr><td><span style='color:red;'>*</span> Press <b>Go</b> for Saving/Refresh</td></tr></table>-->
                <div align="left" id="regionTableId" style="width:99%;">
                </div>
            </div>
                        <div id= "listofgraphs"  style=' display: none; position:absolute;height:100%; width:15%; top:19%; right:0px; border: 1px solid skyblue; overflow-y:auto; background-color: white;direction:ltr;z-index: 1000;float: right'></div>


<!--            <div id="selectTypeId" style=" display: none" title="Select Oneviews">
                <table>
                    <tr>
                        <td>
                            Select Type:
                            <select id="typeId">
                                <option value='report'>Reports</option>
                                <option value='measures'>Measures</option>
                                <option value='dashboard'>Add Kpis</option>
                                                                <option value='portlet'>Portlet</option>
                                <option value='headline'>HeadLine</option>
                            </select>
                        </td>
                    </tr>
                    &nbsp;


                    <input type="text" name="" value=""  id="hiddenValue" style="display: none">

                </table>
                <br>
                <table width="100%">
                    <tr >
                        <td align="center">
                            <input type="button" name="" value="Done" onclick="selectedType()">
                        </td>
                    </tr>
                </table>
            </div>-->

            <div id="dialogTestByrep" style="display: none" title="One ViewBys">
                <table>
                    <tr>
                                <td><input type="radio" id="reportTime" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;"  onclick="settimetoreport()"  checked=''    >Report Time</td>
<!--<td><input type="radio" id="oneviewTime" style="text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;"  onclick="settimeto()"  >Oneview Time</td>  </tr>-->
            
                    <tr>
                        <td id="busNamId"  >Role:</td>
                        <td id="busroleTdId"  >
                            <select id="busroleId" onchange="busroelIdTest()" >

                                <option value="">select</option>
                                <%
                                            if (!busRoleIDs.isEmpty()) {
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }%>
                            </select>
                        </td>
                    </tr></table>
                <table width="100%" id="repDialogTest">

                </table>

            </div>

                 <div id="dialogTestForKpis" style="display: none" title="One ViewBy Kpis">
                <table><tr>
                        <td id="busNamId"  >Role:</td>
                        <td id="busroleForKpsId"  >
                            <select id="busrolekpiId" onchange="kpiRoleTest()" >

                                <option value="">select</option>
                                <%
                                            if (!busRoleIDs.isEmpty()) {
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }%>
                            </select>
                        </td>
                    </tr></table>
                <table width="100%" id="kpisDialogTest">

                </table>

            </div>
                            <div id="assignDiv" style="display: none"></div>
                            <div id="sequenceDiv" style="display: none">
                                <iframe id="sequenceFrame" NAME='sequenceFrame' width="100%" height="80%" frameborder="0" SRC='about:blank'></iframe>
                            </div>

            <div id="dialogTestByport" style="display: none" title="One ViewBys">
                <table width="100%" id="portDialogTest">

                </table>

            </div>
            <div id="displayHeadlines" style="display:none;" title="headlines"></div>
            <div id="createKPIDiv" style="display:none;" title="complexkpi"></div>
            <div id="headlinetableDiv"></div>

            <div id="measureDialogId" style="display:none">
                <table><tr>
                        <td id="busNamId"  >Role:</td>
                        <td id="busroleTdForMeasId"  >
                            <select id="busrolemeasId" onchange="busroleForMeasure()" >

                                <option value="">select</option>
                                <%
                                            if (!busRoleIDs.isEmpty()) {
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }%>
                            </select>
                        </td>
                        <td>
<!--                            <table><tr><td>
            <input id="tableList" type="checkbox" onclick="getDisplayTables('<%=request.getContextPath() %>',oneviewID,'')">All</td>
                    <td id="tabListDiv" ><input type="textbox" id="tabsListVals"><input type="textbox" style="display:none;" id="tabsListIds">
                        <div id="paramVals" class="ajaxboxstyle" style="display:none;overflow: auto;"></div></td>
                    <td id="tablistLink" ><a href="javascript:void(0)" class="ui-icon ui-icon-note" onclick="showList('<%=request.getContextPath() %>','')" ></a></td>
                    <td id="goButton" onclick="setValueToContainer('<%=request.getContextPath() %>',oneviewID,'')"><input type="button" value="GO" class="navtitle-hover"></td>
            </tr></table>-->
                        </td>
            </tr></table>

                <iframe id="measureFrameId" NAME='measureFrameId' width="100%" height="80%" frameborder="0" SRC=''></iframe>

            </div>

                            <div id="complexDialogId" style="display:none;" title="Roles">
                <table><tr>
                        <td id="busNamComplexId"  >Role:</td>
                        <td id="busroleTdForComplexId"  >
                            <select id="busrolComplexId" onchange="busroleForComplexKPI()" >

                                <option value="">select</option>
                                <%
                                            if (!busRoleIDs.isEmpty()) {
                                                for (int i = 0; i < busRoleIDs.size(); i++) {
                                %>
                                <option value="<%=busRoleIDs.get(i)%>"><%=busRoleNames.get(i)%></option>
                                <%
                                                }
                                            }%>
                            </select>
                        </td>
                    </tr>
                </table>
                            <iframe id="createKPIDivFrame" frameborder="0" width="100%" height="100%" name="createKPIDivFrame"  src="about:blank" ></iframe>


            </div>
             <div id="oneviewbyNameId" class="ui-dialog-content ui-widget-content" style="height: 350px;display: none; min-height: 200px; width: auto;" title="Oneview Name">
                 <br>
                 <table width="100%">
                 <tr>
                  <td class="myhead">Select OneView Type</td>
                  <td><select id="oneViewType" name="oneViewType" onchange="oneviewSelection();" style="width:150px;">
                      <option value="Generic OneView">Generic OneView</option>
                      <option value="Business TemplateView">Business TemplateView</option>
                      <option value="Measure Based Business Template">Measure Based Business Template</option>
                      </select>
                  </td>
                  </tr>
                 </table>
                 <table  width="100%">
                    <tbody>
                        <tr>
                            <td class="myhead"> Enter Name</td>
                            <td>
                             <input  type="text" name="" value="" id="viewBydivid">
                            </td>
                        </tr>
                        <tr id="genericview" style="display:'';">
                            <td class="myhead">Select Height </td>
                            <td>
                               <select id="oneviewRegHeightId" style="width: 150px;" name="regionHeight">
                                <option value="80">100%</option>
                                <option value="150">150%</option>
                            </select>
                            </td>
                        </tr>
                        <tr id="businessView" style="display: none;">
                            <td class="myhead">Select Business Role</td>
                            <td>
                                <select id="roleId" style="width: 150px;" name="roleId">
                                <% for(int i=0;i<folderIdsList.size();i++){%>
                               <option value='<%=folderIdsList.get(i)%>'><%=folderNamesList.get(i)%></option>
                               <%}%>
                                </select>
                            </td>
                        </tr>
                    </tbody>
                </table>

<!--                 <table id="oneviewrange" style="display: none;"><tr> <td class="myhead">select Date Type:</td>
                          <td>
                        <select style="width:120px" id="time" name="time">
                         <option id="st" onclick="timeBasis()">none</option>
                         <option id="StandardTime" value="StandardTime" onclick="timeBasis()" name="time">Standard Time</option>
                         <option id="RangeBasis" value="RangeBasis" onclick="timeBasis()" name="time">Range Basis</option>
                        </select>
                          </td></tr></table><br>-->
                <table width="100%">
                    <tbody>
                        <tr>
                            <td align="center">
                                <input class="navtitle-hover" type="button" onclick="insertName()" value=" Done ">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>


<!--            <div id="oneviewbyNameId" style="display: none" title="Oneview Name">
                <table>
                    <tr>
                        <td>
                            Enter Name:<input type="text" name="" value="" id="viewBydivid">
                        </td>
                    </tr>
                     <tr  style="height:10px;">
                        <td align="center">
                           Select Height: <select id="oneviewRegHeightId" style="width: 110px;" name="regionHeight">
                                <option value="100">100%</option>
                                <option value="200">150%</option>
                            </select>
                        </td>
                    </tr>

                    <tr  style="height:10px;">
                        <td align="center">
                            <input type="button" name="" value="Done" onclick="insertName()">
                        </td>
                    </tr>
                </table>

            </div>-->
            <div id="renameDivId" style="display: none" title="Rename">
                <table>
                    <tr>
                        <td>
                            Enter Name:<input type="text" name="" value="" id="renameId">
                        </td>
                    </tr>

                    <tr  style="height:10px;">
                        <td align="center">
                            <input type="button" name="" value="Done" onclick="renamingfunction()">
                        </td>
                    </tr>
                </table>

            </div>


            <div id="measureOptionsId" style="display: none" title="MeausreOptions">
                <table>

                    <tr>
                        <td width="25%">Round</td>
                        <td>
                            <select id="roundId" style="width: 110px;" name="roundname">
                                <option value="0">No Decimal</option>
                                <option value="1">1 Decimal</option>
                                <option value="2">2 Decimal</option>
                                <option value="3">3 Decimal</option>
                                <option value="4">4 Decimal</option>
                            </select>
                        </td>
<!--                    </tr>

                    <tr>-->
                        <td width="25%">Number Format</td>
                        <td width="25%">
                            <select id="NbrFormatId" style="width: 110px;" name="NbrFormat">
                                <option value="">Absolute</option>
                                <option value="K">Thousands(K)</option>
                                <option value="M">Millions(M)</option>
                                <option value="L">Lakhs(L)</option>
                                <option value="Cr">Crores(C)</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="25%">Prefix</td>
                        <td width="25%">
                            <select id="prefixSelect" style="width: 110px;" name="prefixSelect">
                                <option value="" onclick="hidePrefixVals()"></option>
                                <option value="$" onclick="hidePrefixVals()">$</option>
                                <option value="Rs" onclick="hidePrefixVals()">Rs</option>
                                <option value="Euro" onclick="hidePrefixVals()">Euro</option>
                                <option value="Yen" onclick="hidePrefixVals()">Yen</option>
                                <option value="preCust" onclick="getPrefixVals()">Customize</option>
                            </select>
                        </td>
<!--                    </tr>-->

<!--                    <tr>-->
                        <td width="25%">Suffix</td>
                        <td width="25%">
                            <select id="suffixSelect" style="width: 110px;" name="suffixSelect">
                                <option value="" onclick="hidesuffixVals()">Absolute</option>
                                <option value="K" onclick="hidesuffixVals()">Thousands(K)</option>
                                <option value="M" onclick="hidesuffixVals()">Millions(M)</option>
                                <option value="L" onclick="hidesuffixVals()">Lakhs(L)</option>
                                <option value="Cr" onclick="hidesuffixVals()">Crores(C)</option>
                                <option value="%" onclick="hidesuffixVals()">percentage(%)</option>
                                <option value="sufCust" onclick="getSuffixVals()">Customize</option>
                            </select>
                        </td>
                    </tr>
                    <tr><td/><td id="prefixes" style="display: none;"><input id="prefixVal" type="text"/></td><td/><td/><td id="suffixes" style="display: none;"><input id="suffixVal" type="text"/></td>
<!--                    </tr>
                    <tr>-->
                        </tr>
                    <tr>
                        <td width="25%">Measure Type</td>
                        <td>
                            <select id="measureTypeId" style="width: 110px;" name="measureType" onchange="measureTypeSelection()">
                                <option value="Standard">Standard</option>
                                <option value="Non-Standard">Non-Standard</option>
                            </select>
                        </td>
<!--                    </tr>
                       <tr>-->
                        <td width="25%">Trend Graph</td>
                        <td width="25%">
                       <input  name="trendcolor" id="trendcolor" type="text" onclick="showTrend(this.id)" value="" style='width:50px;cursor:pointer;background-color:<%=trendbg%>' colorCode=''>
                    </td>
                    </tr>
<!--                    <tr>
                        <td width="25%">Font Color</td>
                        <td width="25%">
                       <input  name="fontColor" id="fontColor" type="text" onclick="showFont(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                    </td>
                    </tr>-->
                     <tr>
                        <td width="25%">Measure Color</td>
                        <td width="50%">
                            <input  name="measureColor1" id="measureblue" type="radio"  value="#336699" style='width:50px;cursor:pointer' >Blue
                             <input  name="measureColor1" id="measureRed" type="radio"  value="#FF0000" style='width:50px;cursor:pointer' >Red
                              <input  name="measureColor1" id="measureGreen" type="radio"  value="#008000" style='width:50px;cursor:pointer'>green
                            <input  name="measureColor1" id="measureColor" type="radio" onchange="showMeasure(this.id)" value="other" style='width:50px;cursor:pointer' colorCode=''>other
                           <input  name="measureColor1" id="measurecolorcoon" type="radio"  value="#008000" style='width:42px;cursor:pointer'>Conditional
                    </td>
<!--                    <td align='center'><span style="">Dial Chart</span></td>
                    <td>
                        <select id="dialSelectId" onchange="checkDialStatus(this.id)">
                            <option value="no" default>No</option>
                            <option value="yes" >Yes</option>
                        </select>
                    </td>-->
                    </tr>
                    <tr>
                        <td colspan="4">
                            <input type="checkbox" id="logicalChecked" value="logicalChecked" onchange="showLogicalColor()"/>
                            <label for="logicalChecked">Color Grouping</label>
                            <div id="logicalColorDiv" style="display: none;position: relative;width: 100%">
                                <table border="0">
                                    <tr>
                                        <td><span id="LogicalmeasureName" style="font-family: verdana"></span></td>
                                        <td>:<input type="text" value="" id="measreIdVal" readonly style="width: 80px"/>&nbsp;per&nbsp;<span id="perTotalDays" style="font-family: verdana"></span>&nbsp;Days</td>
                                    </tr>
                                    <tr>
                                        <td><span id="DayMeasureName" style="font-family: verdana">Day</span></td>
                                        <td>:<input type="text" value="" id="measureDayVal" readonly style="width: 80px"/>&nbsp;per&nbsp;Day</td>
                                    </tr>
<!--                                    <tr>
                                        <td><span id="DayMeasureName" style="font-family: verdana">Target Value</span></td>
                                        <td>:<input type="text" value="" id="TargetDayValue" style="width: 50px" onBlur="calcTarVal()"/>&nbsp;per&nbsp;Day</td>
                                    </tr>-->
                                    <tr>
                                         <td colspan="2">
                                            <table border="0">
                                                <tr>
                                                    <td align="center"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;">Condition</span></td>
                                                    <td align="center"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;">Range</span></td>
                                                    <td align="center"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;">Color</span></td>
                                              </tr>
                                              <%for(int i=0;i<3;i++){ %>
                                              <tr>
                                                  <td align="center"><select onchange="onChangeOperator(this.id,this.value,'<%=i%>')" id="LogicalOperator<%=i%>" name="LogicalOperator<%=i%>">
                                                        <option value="&lt;">&lt;</option>
                                                        <option value="&gt;">&gt;</option>
                                                        <option value="&lt;=">&lt;=</option>
                                                        <option value="&gt;=">&gt;=</option>
                                                        <option value="=">=</option>
                                                        <option value="!=">!=</option>
                                                        <option value="&lt;&gt;">&lt;&gt;</option>
                                                    </select></td>
                                                    <td id="targetValueText">
                                                        <input type="text" value="" id="range<%=i%>_1" style="width: 50px"/>
                                                        <input type="text" value="" id="range<%=i%>_2" style="width: 50px;display: none" onkeyup="reflectValue(this.id,'<%=(i+1)%>')"/>
                                                    </td>
                                                   <td><input type="text" id="logicalColorId_<%=i%>" style="width: 50px;cursor: pointer;" colorcode="" onclick="showMeasure(this.id)"/></td>
                                              </tr>
                                              <% }%>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4">
<!--                            <input type="checkbox" id="DefineDialChartId" value="DefineDialChart" onchange="DefineDialChart()"/>-->
                            <label for="DefineDialChart">Define Dial Chart</label>&nbsp;&nbsp;&nbsp;&nbsp;
                             <select id="dialSelectId" onchange="checkDialStatus(this.id)">
                            <option value="no" default>No</option>
                            <option value="yes" >Yes</option>
                        </select>
                            <div id="DefineDialChartDiv" style="display: none;position: relative;width: 100%;border:1px solid;border-radius:5px ;border-color:#79C9EC">
                                <table border="0" width="100%">
                                    <tr>
                                        <td><div id="upperDialDiv" style="border-bottom: 1px dashed;border-color:#79C9EC;">
                                        <table>
                                            <tr>
                                                <td align="left">Absolute/Pro-rate &nbsp;&nbsp;<select onchange="showNumOfDays()" id="showNumOfDays">
                                                                         <option value="absolute"> Absolute</option>
                                                                         <option value="deviationPer"> Pro-rate</option>
                                                                         </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td align="left"><span class="dialMeasureName" style="font-family: verdana"></span> &nbsp;&nbsp;<input type="text" id="dialMeasureValue" readOnly style="width:90px"/>
                                                   <span id="dialMeasureTotalDaysTd" >(</span><span id="dialMeasureTotalDays" style="font-family: verdana"></span><span id="dialMeasureTotalDaysTD">)Days</span>
                                                <span class="dialMeasureName" style="font-family: verdana"></span> &nbsp;&nbsp;&nbsp;<input type="text" readonly id="DialMeasureValuePerDay" style="width:90px"><span id="DialMeasureValuePerDayTd"> /Days</span></td>
                                                <td/>
                                            <tr>
                                            <tr>
                                                <td align="left">Chart On &nbsp;&nbsp;<select onchange="changeDialMeasuretype(this.id)" id="dialtypeMusureType">
                                                                         <option value="absolute">Absolute</option>
                                                                         <option value="deviationPer"> Deviation%</option>
                                                                         <option value="changePer"> Change%</option>
                                                                         </select>
                                                </td>
                                                <td align="center">
                                            <div id="deviationDiv" style="display: none">
                                                <table><tr>
                                                     <td id="dialTargetTd" align="left" style=""> Target&nbsp;&nbsp;:<input type="text" id="DialTargetValue" style="width: 90px;"/> /Day</td>
                                                    <td><input type="button"  class="navtitle-hover" id="getDialDeviationVal" name="getDeviation" value="getDeviation" onclick="getDialDeviation()" style=""></td>
                                                     </tr></table>
                                            </div>
                                                <div id="changePerDiv" style="display: none">
                                                    <table><tr>
                                                           <td id="dialchangePersentTd" style="">Change Percent:&nbsp;&nbsp;<input type="text" value="" id="DialChartChangePerVal" style="width: 90px" readonly/></td>
                                                   </tr></table>
                                                </div>
                                            </tr>
                                            <tr>
                                                <td id="dialDeveationTd" style="display: none" align="left">Deviation (in %)&nbsp;&nbsp;<input type="text" id="dailDeviationValue" readonly /></td>

                                            </tr>
                                                </table>
                                        </div>
                                            </td>
                                    <tr>
                                         <td colspan=""><div id="lowerDialDiv" >
                                            <table border="0">
                                                <tr>
                                                    <td align="center"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;">Risk</span></td>
                                                    <td align="center"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;">Condition</span></td>
                                                    <td align="center" colspan="2"><span style="font-family: verdana;font-weight: bold;font-size: 8pt;float:center;">Range</span></td>
                                              </tr>
                                              <%String[] keys={"High","Medium","Low"};
                                                  for(int i=0;i<3;i++){
                                                  String nextKey=(i<(keys.length-1)?keys[(i+1)]:"null");%>
                                              <tr>
                                                  <td><span style="font-family: verdana;font-weight: bold;font-size: 7pt;"><%=keys[i]%></span></td>
                                                  <td align="center"><select  id="DialChartOperator<%=keys[i]%>" name="DialChartOperator<%=keys[i]%>">
<!--                                                          onchange="onChangeOperator(this.id,this.value,'<keys[i]%>')"-->
<!--                                                        <option value="&lt;">&lt;</option>
                                                        <option value="&gt;">&gt;</option>
                                                        <option value="&lt;=">&lt;=</option>
                                                        <option value="&gt;=">&gt;=</option>
                                                        <option value="=">=</option>
                                                        <option value="!=">!=</option>-->
                                                        <option value="&lt;&gt;">&lt;&gt;</option>
                                                    </select></td>
                                                    <td id="dialRangesID">
                                                        <input type="text" value="" id="range<%=keys[i]%>_1" style="width: 50px"/></td>
                                                    <td><input type="text" value="" id="range<%=keys[i]%>_2" style="width: 50px;display:block;" onkeyup="reflectValue(this.id,'<%=nextKey%>')"/>
                                                    </td>
                                              </tr>
                                              <% }%>
                                            </table>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
<!--                     <tr>
                        <td width="25%">-ve Color</td>
                          <td width="25%"> <select id="negativeColor" style="width: 110px;" name="negativeColor">
                                <option value="Standard">FontColor</option>
                                <option value="Standard">Red</option>
                                <option value="Non-Standard">Green</option>
                            </select>
                    </td>
                    </tr>
                       <tr>
                        <td width="25%">+ve Color</td>
                            <td width="25%"> <select id="positiveColor" style="width: 110px;" name="positiveColor">
                                <option value="Standard">FontColor</option>
                                <option value="Standard">Red</option>
                                <option value="Non-Standard">Green</option>
                            </select>
                    </td>
                    </tr>-->

                </table>
                <br>
                <br>
                <table width="100%">
                    <tr >
                        <td align="center">
                            <input type="button" name="" value="Done" class="navtitle-hover" onclick="measureOption()">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="measureOptionsIdForComplex" style="display: none" title="MeausreOptions">
                <table>

                    <tr>
                        <td width="25%">Round</td>
                        <td>
                            <select id="roundIdcomplex" style="width: 110px;" name="roundname">
                                <option value="0">No Decimal</option>
                                <option value="1">1 Decimal</option>
                                <option value="2">2 Decimal</option>
                                <option value="3">3 Decimal</option>
                                <option value="4">4 Decimal</option>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td width="25%">Number Format</td>
                        <td width="25%">
                            <select id="NbrFormatIdcomplex" style="width: 110px;" name="NbrFormat">
                                <option value="">Absolute</option>
                                <option value="K">Thousands(K)</option>
                                <option value="M">Millions(M)</option>
                                <option value="L">Lakhs(L)</option>
                                <option value="Cr">Crores(C)</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="25%">Prefix</td>
                        <td width="25%">
                            <select id="prefixSelectcomplex" style="width: 110px;" name="prefixSelect">
                                <option value="" onclick="complexhidePrefixVals()"></option>
                                <option value="$" onclick="complexhidePrefixVals()">$</option>
                                <option value="Rs" onclick="complexhidePrefixVals()">Rs</option>
                                <option value="Euro" onclick="complexhidePrefixVals()">Euro</option>
                                <option value="Yen" onclick="complexhidePrefixVals()">Yen</option>
                                <option value="preCust" onclick="complexgetPrefixVals()">Customize</option>
                            </select>
                        </td>
                    </tr>
                    <tr><td></td><td id="prefixescomplex" style="display: none;"><input id="prefixValcomplex" type="text"/></td></tr>
                    <tr>
                        <td width="25%">Suffix</td>
                        <td width="25%">
                            <select id="suffixSelectcomplex" style="width: 110px;" name="suffixSelect">
                                <option value="" onclick="complexhidesuffixVals()">Absolute</option>
                                <option value="K" onclick="complexhidesuffixVals()">Thousands(K)</option>
                                <option value="M" onclick="complexhidesuffixVals()">Millions(M)</option>
                                <option value="L" onclick="complexhidesuffixVals()">Lakhs(L)</option>
                                <option value="Cr" onclick="complexhidesuffixVals()">Crores(C)</option>
                                <option value="%" onclick="complexhidesuffixVals()">percentage(%)</option>
                                <option value="sufCust" onclick="complexgetSuffixVals()">Customize</option>
                            </select>
                        </td>
                    </tr>
                    <tr><td></td><td id="suffixescomplex" style="display: none;"><input id="suffixValcomplex" type="text"/></td></tr>
                    <tr>
<!--                        <td width="25%">Measure Type</td>-->
                        <td>
                            <select id="measureTypeIdcomplex" style="width: 110px;display:none;" name="measureType">
                                <option value="Standard" selected>Standard</option>
<!--                                <option value="Non-Standard">Non-Standard</option>-->
                            </select>
                        </td>
                    </tr>
                       <tr>
<!--                        <td width="25%">Trend Graph</td>-->
                        <td width="25%">
                       <input  name="trendcolor" id="trendcolorcomplex" type="text" value="" style='display: none;width:50px;cursor:pointer;'>
                    </td>
                    </tr>
                     <tr>
                        <td width="25%">Measure Color</td>
                        <td width="50%">
                            <input  name="measureColor1" id="measurebluecomplex" type="radio"  value="#336699" style='width:50px;cursor:pointer' >Blue
                             <input  name="measureColor1" id="measureRedcomplex" type="radio"  value="#FF0000" style='width:50px;cursor:pointer' >Red
                              <input  name="measureColor1" id="measureGreencomplex" type="radio"  value="#008000" style='width:50px;cursor:pointer'>green
                            <input  name="measureColor1" id="measureColorcomplex" type="radio" onchange="showMeasureComplex(this.id)" value="other" style='width:50px;cursor:pointer' colorCode=''>other
                    </td>
                    </tr>
                </table>
                <br>
                <br>
                <table width="100%">
                    <tr >
                        <td align="center">
                            <input type="button" name="" value="Done" onclick="measureOptionForComplexKPI()">
                        </td>
                    </tr>
                </table>
            </div>
            <div id="GraphdrillToRep">

            </div>
            <div id="drilToTableorGrapId">

            </div>
            <div id="measGraphId" title="OneView Measure Graph" style="display:none;">
<!--                <table  align="left" >
                    <tr>
                        <td>Prev. Year Comparaision:<input id="prevYearCompYes" type="radio" name="prevYearComparision" onclick="prevYearComparision()">yes<input id="prevYearCompNo" name="prevYearComparision" onclick="prevYearComparision()" checked type="radio">No</td>
                    </tr>
                </table>-->
                <table id="measGraphIdTab" width="100%">

                </table>
            </div>
<!--             <div id="measureSelection" title="OneView Measure Selection" style="display:none;">
                 <table id="draggedMeasure" border="1" style="height:120px;display:none;" valign="top" align="center">
                 </table>
             </div>-->

<!--    <div id="AddMoreParamsDiv" title="Add More Dimension " >
           <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>-->
             <div id="measGraphTrendDiv" title="OneView Measure Graph Trend" style="display:none;">
                 <div align="right" id="topbotgo" style="dispaly:none">
                     <td>
                         <select name="changetype" id="changeid">
                                  <option value="Top">Top</option>
                                  <option value="Bottom">Bottom</option>
                         </select>
                         <input type="text" class="textfield" value="" id="changetext" onkeypress="" />
                         <input id="changegoId" class="navtitle-hover" type="button" onclick=" gototopbottomview()" value="Go" style="width:25px">
                     </td>
                 </div>
                <table  align="right" >

                </table>
<!--                 <table id="measureTrendTd">-->
                     <table  id ="measureTrendTdid" align='left' >
<!--                         <tr><td id="measureTrendTd0"></td><td id="measureTrendTd1"></td></tr>
                         <tr><td id="measureTrendTd2"></td><td id="measureTrendTd3"></td></tr>-->
                     </table>
<!--                 </table>-->
            </div>

<div id="measGraphOverLayDiv" title="OneView Measure Graph OverLay" style="display:none;">
              <table  align="right" >

                </table>
                 <table id="measureOverLayTd">
                 </table>
            </div>
            <div id="customCompDiv">

            </div>
            <div id="OneviewRename" class="ui-dialog-content ui-widget-content" style="height: 208px;display: none; min-height: 108px; width: auto;" title="OneviewRename">
                <br>
                <table id="regTable" width="100%">
                    <tbody>
                        <tr >
                            <td class="myhead">One View Name </td>
                            <td>
                                <input id="oneviewReadId" class="myTextbox5" type="text"  name="" name="" value="" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="myhead">One View Re-Name </td>
                            <td>
                                <input id="oneviewAddId" class="myTextbox5" type="text"  name="" value="">
                            </td>
                        </tr>
                        <tr style="display: none">
                            <td>
                                <input type="text" name="" value="" id="oneviewtestId" >
                            </td>
                        </tr>
                    </tbody>
                </table>
                <br>
                <table width="100%">
                    <tbody>
                        <tr>
                            <td align="center">
                                <input class="navtitle-hover" type="button" onclick="oneviewRenaming()" value=" Ok ">
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!--            <div id="OneviewRename" style="display: none" title="OneviewRename">
                            <table>
                                <tr>
                                    <td>
                                        OneView Name:<input type="text" name="" value="" id="oneviewReadId" readonly>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        Re Name:<input type="text" name="" value="" id="oneviewAddId">
                                    </td>
                                </tr>
                                 <tr style="display: none">
                                    <td>
                                        <input type="text" name="" value="" id="oneviewtestId">
                                    </td>
                                </tr>

                                <tr  style="height:10px;">
                                    <td align="center">
                                        <input type="button" name="" value="Done" onclick="oneviewRenaming()">
                                    </td>
                                </tr>
                            </table>

                        </div>-->

            <div id="shareOneview" title="Share Oneview" style="display: none">
                <table>
                    <!--                                        <tr>
                                                                <td width="20%"><b>Format</b> </td>
                                                                <td>
                                                                    <select name="fileType" id="fileType1" style="width:130px">
                                                                            <option value="H">HTML</option>
                                                                            <option value="E">Excel</option>
                                                                            <option value="P">PDF</option>
                                                                            <option value="CSV">CSV</option>
                                                                    </select>
                                                                 </td>
                                                            </tr>-->
                    <tr>
                        <td width="20%"><b>Users </b></td>
                        <td>
                            <select name="selectusers" id="selectusersId" style="width:130px" onchange="displayOneviewTextArea()">
                                <option value="">Select</option>
                                <option value="selected">Selective Users</option>
                                <option value="All" >All Users</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" id="sharesubjectId" style="display:none"><b>Subject</b> </td>
                        <td colspan="1" >

                            <input type="text" id="sharesubject" name="sharesubject" style="display:none">
                        </td>
                    </tr>
                    <tr>
                        <td width="20%" id="toMailId" style="display:none"><b>Email To</b> </td>
                        <td colspan="1" >

                            <textarea  id="userstextareaId" name="userstextarea" cols="" rows=""  style="width:250px;height:80px;display:none"></textarea>
                        </td>
                    </tr>
                    <tr> <td colspan="2">&nbsp; </td>  </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <input class="navtitle-hover" type="button" id="sharereportbutton" value="Share Oneview" onclick="sendOneviewEmail()">
                        </td>
                    </tr>
                    <tr> <td colspan="2">&nbsp; </td>  </tr>
                    <tr> <td colspan="2"><font color="red"><span>*</span></font> Please separate multiple Email Id's by comma(,). </td>  </tr>
                </table>
            </div>
            <div id="oneviewFrameId" style="display:none;" >
                <IFRAME NAME="dFrame" id="oneFrame" STYLE="display:none;width:0px;height:0px"  frameborder="0"></IFRAME>
            </div>

            <div id="commentsAreaId"  class="commentDialog" style="display:none;"  title="User Comments">
                <IFRAME NAME="oneviewCommentFrame" id="oneviewCommentFrameId" height="100%" width="100%"  frameborder="0" src="#"></IFRAME>
            </div>

             <SPAN id="oneviewStickNoteSpan">

             </SPAN>
          <input type="hidden" name="negativeColorCode" id="trendColorCode">
            <input type="hidden" name="fontColorCode" id="fontColorCode">
              <input type="hidden" name="measureColorCode" id="measureColorCode">
              <input type="hidden" name="measureColorCodecomplex" id="measureColorCodecomplex">
        </form>
                            <div id="OLAPGraphDialog" style="display:none;" title="OLAP Graph">
<!--   <iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="250%" frameborder="0" SRC='about:blank' scrolling="no" onload='javascript:resizeIframe(this);'></iframe>-->
<iframe id="OLAPGraphFrame" NAME='OLAPGraphFrame' width="100%" height="100%"  frameborder="0" SRC='about:blank' scrolling="no" onload='javascript:resizeIframe(this.id);'></iframe>
</div>
                    <div id="moreViewsDiv" title="One Views" style="display:none;"></div>
                    <div id="roleNames" title="Roles" style="display:none;"></div>



        <table style="width:100%;" id="footID">
            <tr>
                <td valign="top" style="width:100%;">
                    <jsp:include page="Headerfolder/footerPage.jsp"/>
                </td>
            </tr>
        </table>
                <div id="zoomTrendGraph" style="display:none" title="TrendGraphZoom"></div>
                    <div id="colorsDiv" style="display: none" title="Select color">
                    <center>
                        <input type="text" id="color" style="" value="#12345" >
                        <div id="colorpicker" style=""></div>
                        <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                        <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                        <input type="hidden" id="selectedId" value="">
                    </center>
        </div>
                <div id="navigationList" style="display: none;"></div>
                <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
        </div>
                <div id="testForId" class="jqplot-image-container-content"  style="display:none" title="Right Click and Save as Image">

               </div>
                <div id="relatedMeasures" style="display: none;" title="Related Measures"></div>
                <div id="oneViewSettings_Dialoge" style="display: none" title="Settings">
                    <table align='center' border="0" width="100%">
                        <tr><td><input id='one_YesterDay' type='radio' name='oneSetting_Radio' value='YesterDay' align='left'/>&nbsp;&nbsp;&nbsp;YesterDay</td></tr>
                        <tr><td><input id='one_ToDay' type='radio' name='oneSetting_Radio' value='ToDay' align='left'/>&nbsp;&nbsp;&nbsp;ToDay</td></tr>
                        <tr><td style='border-bottom-style:dashed;border-bottom-color:grey;border-width:1.8px;'><input id='one_Tomorrow' type='radio' name='oneSetting_Radio' value='Tommarrow' align='left'/>&nbsp;&nbsp;&nbsp;Tomorrow</td></tr>
                        <tr><td><input id='one_EveryTime' type='checkbox' value='EveryTime' align='left'/>&nbsp;&nbsp;&nbsp;Every Time</td></tr>
                        <tr/><tr/><tr><td align='center'><input  type='button' name='save' value='save' class='navtitle-hover' onclick="saveSettings()"/></td></tr>
                    </table>
                </div>
                <div id="oneView_Copy_dialogue" style="display:none" title="Copy OneView">

                </div>
                <div id="writeNoteDiv" style="display:none">
                    <table border="0">
                       <tr>
                           <td><text style="font-size: 10pt;font-family:verdana;font-weight: bold">Title &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</text>
                           <input type="text" id="noteWriterId" value=""></td>
                       </tr>
                       <tr>
                           <td>
                               <text style="font-size: 10pt;font-family:verdana;font-weight: bold" align="left">Note&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</text>
                               <textarea id="onViewNoteContentEditable" style="border:1px solid #ccc;width:300px;height:150px;font-family:verdana;font-size:9pt;padding:2px;"></textarea>
                           </td>
                       </tr>
                       <tr id="editableNoteTR"></tr>
                   </table>
                </div>
                <div id="tempImg" styl="display:none"></div>
                <div id="tempRegionFile" style="display:none"></div>
               <div id="oneviewMeasuresAlertId" style="display:none;">

               </div>

                <div id="pdfDialog" style="display: none">
                    <table border="0"><tr><td>
                    <table align="center" border="0">
                        <tr>
                            <td>
                                <label style="font-family: verdana;">Page Type</label>
                            </td>
                            <td>
                                <select id="pdfTypeSelect" >
                                    <option id="A4" value="A4" >A4-Portrait</option>
                                    <option id="A4_landascape" value="A4_Landascpe">A4-Landscape</option>
                                </select>
                            </td>
                        </tr>
<!--                        <tr>
                            <td>
                                <label style="font-family: verdana;">Fit To</label>
                            </td>
                            <td>
                                <select id="pdfPageFitSelect" >
                                    <option id="SinglePage" value="SinglePage" >Single Page</option>
                                    <option id="MultiPage" value="MultiPage">Multi Page</option>
                                </select>
                            </td>
                        </tr>-->
                        <tr>
                            <td colspan="2" align="center">
                                <input type="button" class="navtitle-hover" value="Done" name="Done" align="center" onclick="oneViewAsPDF()"/>
                            </td>

                        </tr>
                    </table>
                            </td>
                            <td style="float:right;">
                                <div id="loadPdfDiv" style="display: none"><center><img id="imgId" src="images/ajax1.gif" align="top"  width="50px" height="50px"  style="position:absolute" /></center>
                                </div></td>
                        </tr>
                        </table>
                </div>

           <div id="scheduleReportalertId"  style="display:none;" title=""   >
             <form action="" name="dailyReportScheduleForm" id="dailyReportScheduleForm" method="post">
             <table width="100%">
                 <tr id="alertnameid"><td class="myhead">Alert Name</td><td><input id="scheduleName" type="text" value="" style="width: auto;" maxlength="100" name="scheduleName"></td></tr>
                 <tr id="schedulenameid" style="display:none"><td class="myhead">Schedule Name</td><td><input id="scheduleName1" type="text" value="" style="width: auto;" maxlength="100" name="scheduleName1"></td></tr>
                 <tr id="nonCondtionaldMailId" style="display: ''"><td class="myhead" >Email To</td><td><textarea id="usertextarea" style="width: 250px; height: 80px;" rows="" cols="" name="usertextarea"></textarea></td></tr>
<!--                 <tr>
                   <td class="myhead">Format</td>
                   <td><select style="width: 130px;" id="fileType" name="fileType">
                   <option value="H">HTML</option>
                   <option value="E">Excel</option>
                   <option value="P">PDF</option>
                   </select>
                    </td>
                    </tr>-->
                 <tr><td class="myhead">StartDate</td><td><input id="sDatepicker"  type="text" value="" style="width: 120px;" maxlength="100" name="startdate" readonly=""></td></tr>
                 <tr><td class="myhead">End Date</td><td><input id="eDatepicker"   type="text" value="" style="width: 120px;" maxlength="100" name="enddate" readonly=""></td></tr>

                 <tr><td class="myhead">Time</td>
                 <td><table><tr><td>
                     <select name="hrs" id="hrs" style='width:50px;'>
                       <%for (int i = 00; i < 24; i++) {%>
                       <option  value="<%=i%>"><%=i%></option>
                       <%}%>
                     </select>HH</td>
                     <td>
                     <select name="mins" id="mins" style='width:50px;'>
                     <%for (int i = 00; i < 60; i++) {%>
                     <option  value="<%=i%>"><%=i%></option>
                     <%}%>
                     </select>MIN</td></tr></table>
                 </td>
                 </tr>

                   <tr id="selectdatetype" ><td class="myhead">Date</td>
                       <td><select style="width: 120px;" id="alertDateTypeId" name="alertDateType" onchange="customDateCheck(this.id)">
                        <option value="oneviewdate">Oneview</option>
                        <option value="todaydate">Today</option>
                        <option value="yesterdate">Yesterday</option>
                        <option value="customdate">Custom</option>
                        </select>
                   </td></tr>
                   <tr id="selectdatetype1" style="display:none"><td class="myhead">Date</td>
                       <td><select style="width: 120px;" id="alertDateTypeId2" name="alertDateType" onchange="customDateCheck(this.id)">
                        <option value="Previous Day">Previous Day</option>
                        <option value="Current Day">Current Day</option>
                        <option value="oneview Date">oneview Date</option>

                        </select>
                   </td></tr>

                   <tr id="customSysdateId" style="display:none;">
                       <td class="myhead" >System Date</td>
                       <td><table><tr>
                       <td > <input type="radio" name="FromDate" id="fromSysDate" value="fromSysDate" ></td>
                       <td width="20px;"></td>
                          <td ><select id="fromSysSign" name="fromSysSign" style="width: 60px;">
                              <option value="+">+</option>
                              <option value="-">-</option>
                          </select>
                          </td>
                          <td ><input id="fromSysVal" name="fromSysVal" type="text" size="7">Days </td>
                               </tr></table>
                          </td>
                   </tr>

                  <tr id="customGlobaldateId" style="display:none;">

                      <td class="myhead" >Global Date</td>
                      <td><table><tr>
                      <td ><input type="radio" name="FromDate" id="fromglobalDate" value="fromglobalDate" checked></td>
                      <td width="20px;"></td>
                          <td ><select id="fromglobalSign" name="globalSign" style="width: 60px;">
                              <option value="+">+</option>
                              <option value="-">-</option>
                          </select></td>
                          <td ><input id="fromGlobVal" name="fromGlobVal" type="text" size="7">Days </td>
                              </tr></table></td>
                  </tr>

                 <tr>
                    <td id="alert123" class="myhead">Alert Frequency</td>
                     <td id="alert1234"class="myhead"> Frequency</td>
                     <td>
                         <select id="frequency" name="frequency" onchange="checkFrequency(this.id)" style='width:100px'>
                           <option value="Daily" selected>Daily</option>
                           <option value="Weekly">Weekly</option>
                           <option value="Monthly">Monthly</option>
                           <option value="Hourly">Hourly</option>
                        </select>
                    </td>
                   </tr>
		 <tr id="weekday" style="display:none;">
		  <td class="myhead">Week Day</td>
                    <td>
                        <select id="particularDay" name="particularDay" style='width:100px'>
                            <% for(int i=0;i<scheduleday.length;i++){%>
                            <option value="<%=sday[i]%>"><%=scheduleday[i]%></option>
                            <%}%>
                        </select>
                    </td>
		</tr>
                <tr id="monthday" style="display:none;">
                    <td class="myhead">Month Day</td>
                    <td>
                        <select id="monthParticularDay" name="monthParticularDay" style='width:100px;'>
                            <% for(int i=1;i<=31;i++){%>
			   <option value="<%=i%>"><%=i%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                 <tr id="hourlyId" style="display:none;">
		  <td class="myhead">Alert Hours</td>
                    <td>
                        <select id="particularHour" name="particularHour" style='width:100px'>
                             <%for (int i = 00; i < 24; i++) {%>
                             <option  value="<%=i%>"><%=i%></option>
                             <%}%>
                        </select>
                    </td>
		</tr>

             </table>
               <table id="nonConditionalValId" name="" >
                   <tr>
                       <td>Conditional Base Alerts </td>
                       <td style="font-weight: bolder;">
                        <font style="font-size: 11px; font-weight: bold; text-decoration: none; font-family: Georgia; color: rgb(51, 102, 153);"> Yes :</font>
                        <input id="conYes" class="navtitle-hover" type="radio"  onclick="coditionalyes()" style="width: auto;" value="" name="">
                       </td>
                       <td style="font-weight: bolder;">
                        <font style="font-size: 11px; font-weight: bold; text-decoration: none; font-family: Georgia; color: rgb(51, 102, 153);"> No :</font>
                        <input id="condNo" class="navtitle-hover" type="radio" checked="checked" onclick="coditionalyes()" style="width: auto;" value="" name="">
                       </td>
                   </tr>
                </table><br>
                <table id="innerRegionId" name="" style="display: none;">

                </table>

             <input type="hidden" id="schedulerId" name="schedulerId" value="">
             <div id="trackerConditions" style="display: none">


                       <table>
                       <tr id="innerValuesId">
                      </tr>
                     </table>
                    <table>
                 <tr>
                      <td> <div id="absoluteBasis">
                          <table>
                            <tr>
                                <td style="font-weight: bolder">
                                   <font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                          Absolute Basis :</font>
                                <input type="radio" id="absolute" class="navtitle-hover" name="trackerTest"   value="absolute Basis" style="width:auto" onclick="absoluteCond()" checked>
                     </td>

                 </tr>
                        </table>
                    </div>
                    </td>
                    <td> <div id="targetBasis">
                        <table>
                            <tr>
                                <td style="font-weight: bolder">
                                    <font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                           Percent Basis :</font>
                               <input type="radio" id="target" class="navtitle-hover" name="trackerTest" value="target Basis" style="width:auto" onclick="targetCond()" >
                                </td>

                            </tr>
                        </table>
                    </div>
                    </td>
                    </tr>
                    </table>

                  <div id="targetConditions" style="display:none">
                    <table id="targetConditionsTable">
<!--                          <tr>
                            <td colspan="2" style="height:20px"></td>
                          </tr>-->
                        <Tr>
                        <td width="100%">
                            <table  width="100%">
                                <tr>
                                <table>
                                    <tr>
                                    <Td style="font-weight: bolder">
                                         <font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                           Enter Target :</font>
                                            <Input type="text"  class="myTextbox3" id="trgetVal" value="" style="width:120px" onkeypress="return isNumberKey(event)"  onchange="targetValue12(this.id)" name="targetValue">
                                    </Td>
<!--                                    <Td id="viewDeviationValue" style="display:block" rowspan="2"><a href="javascript:void(0)" onclick="deviationValue()" style="font-size:12px;font-weight:bold;text-decoration: none;font-family:Georgia;" title="Deviation Value">View%</a></Td>-->
                                    <td style="width:10%"></td>
                                    <Td id="deviationPer" ><font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                          Percentage(%)</font> <input type="text" value="" style="width:80px" id="deviationPercent" readonly="" name="percentValue"></Td>
                                    </tr>
                                </table>
                                </tr>
                                <tr>
                                <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>

                            </table>
                        </td>
                    </Tr>

                    </table>
            </div>
                 <div id="trackerCondition" style="display:block">
                    <table id="trackerConditionsTable">
<!--                        <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr>-->

                        <Tr>
                        <td width="100%">
                            <table  width="100%">
                                <tr>
                                    <Td style="font-weight: bolder"><font style='font-size:11px;font-weight:bold;text-decoration: none;font-family:Georgia;color:#336699'>
                                            Conditions :</font></Td>
                                     <Td width="40%"></Td>
                                    <td width="20%"></td>
                                </tr>
                            </table>
                        </td>
                    </Tr>
                    <Tr>
                        <td width="100%">
                            <table id="condTable"  width="100%">
                                <tr id="cond0">
                                    <td > <span id="condTD0">When Value</span></td>
                                    <td >
                                        <select name="condOp" id="0condOp" onchange='addTextBox(this,"0")'>
                                            <%for (String Str : strOprtrs) {%>
                                            <option  value="<%=Str%>"><%=Str%></option>
                                            <%}%>
                                        </select>
                                    </td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="sCondVal" id="0sCondVal"  style="width:80px" >
                                    </Td>
                                    <Td>
                                        <Input type="text"  class="myTextbox3" name="eCondVal" id="0eCondVal" style="width:80px;display: none" >
                                    </Td>
                                    <td >
                                        Send Mail to
                                    </td>
                                    <td >
                                        <input type="text"  id="0condMail" class="myTextbox3" name="condMail" style="width: auto">
                                    </td>
                                    <td >
                                      Tag
                                    </td>
                                    <td >
                                       <select name="tagValues" id="0tagValueId" >
                                            <option  value="normal">Normal</option>
                                            <option  value="important">Important</option>
                                            <option  value="critical">Critical</option>
                                        </select>
                                    </td>
                                    <Td >
                                        <IMG ALIGN="middle" onclick='addCondRow()' SRC="<%=request.getContextPath()%>/icons pinvoke/plus-circle.png" BORDER="0" ALT=""   title="Add Row" />
                                    </Td>
                                    <Td >
                                        <IMG ALIGN="middle" onclick='deleteCondRow("cond0")' SRC="<%=request.getContextPath()%>/icons pinvoke/cross-circle.png" BORDER="0" ALT=""   title="Delete Row" />
                                    </Td>
                                </tr>
                            </table>
                        </td>
                    </Tr>

             </table>
                <table id="innerRegionId" name="" style="display: none;">

                </table>
             </form>
                                    <input type="hidden" id="dimValue" name="dimValue" value="">
             <input type="hidden" id="schedulerId" name="schedulerId" value="">
         </div>
      </div>
              <center><table>
                     <tr>
                            <td colspan="2" style="height:20px"></td>
                        </tr>
                     <tr id="sendoneviewmeasure">
                     <td   align="center"><input  class="navtitle-hover" type="button" onclick="sendOneviewMeasure()" value="Schedule">
                     </td>
                     </tr>
                      <tr id="scheduleoneview" style="display:none">

                     </tr>
                 <tr><td colspan="2">&nbsp; </td></tr>
                 <tr><td colspan="2" align="center"><font size="1" color="red">*</font>Please separate multiple Email Id's by comma(,).</td></tr>
               </table></center>
                <div id="timeDetailsDiv" style="display: none"></div>
                <div id="zoomMeasureDialDiv" style="max-width: 100%;max-height: 100%;"></div>
<div id="AddMoreParamsDiv" title="Add More Dimension " >
           <iframe  id="addmoreParamFrame" name='addMoreParamFrame' width="100%" height="100%" frameborder="0"   src='about:blank'></iframe>
        </div>
                <div id="customTimeMsr" style="display: none" title="Custom Time Aggregation">
                    <table>
                        <tr>
                            <td colspan="2">
                            <input type="checkbox" id="customTimeOption" value="customTimeOption" onclick="customTimeSelection()"/>
                            <label  for="customTimeOption">Enable Custom Time Aggregation</label>
                            <table id="customTimeTable" style="display:none;">
                                <tr>
                                    <td align="left" width="50%">Date</td>
                                    <td align="right" width="50%"><input id="customMsrdate" class="ui-datepicker" type="text" size="15" value="" name="customMsrdate"></input></td>
                                </tr>
                                <tr><td align="left" width="50%">Duration</td>
                                    <td align="right" width="50%"> <select id="customMsrDuration" name="customMsrDuration" style="width: 125px;">
                                      <%for(int i=0;i<duration.length;i++){%>
                                       <option value="<%=duration[i]%>"><%=duration[i]%></option>
                                       <%}%>
                                    </select></td>
                                </tr>
                                <tr>
                                    <td align="left" width="50%">Comparision</td>
                                    <td align="right" width="50%">
                                        <select id="customMscompare" name="customMscompare" style="width: 125px;">
                                      <%for(int i=0;i<comparision.length;i++){%>
                                           <option value="<%=comparision[i]%>" ><%=comparision[i]%></option>
                                           <%}%>
                                    </select>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
<!--                    <tr>
                       <td align="left" width="50%">Default Aggregation
                       </td>
                       <td align="right" width="50%">
                           <select id="msrAggr" style="width: 125px" name="msrAggr">
                               <%for(int i=0;i<Aggr.length;i++){%>
                                <option value="<%=Aggr[i]%>" ><%=Aggr[i]%></option>
                                  <%}%>
                           </select>
                       </td>
                    </tr>-->
                  </table>
                 <br>
                <br>
                <table width="100%">
                    <tr>
                        <td align="center">
                            <input type="button" name="" value="Done" class="navtitle-hover" onclick="enableCustomTimeMeasure()">
                        </td>
                    </tr>
                </table>
                </div>
                            <input type="hidden" value="" id="oneviewversion">
                            <input type="hidden" value="" id="jsonLength">
        <input type="hidden" id="Designer" name="Designer" value="busTemplateView">
        <input type="hidden" id="h" value="<%=request.getContextPath()%>">
        <input type="hidden" name="MsrIds" value="" id="MsrIds">
        <input type="hidden" name="Measures" value="" id="Measures">
        <input type="hidden" name="oneViewId" value="" id="oneViewId">
        <input type="hidden" name="action1" value="fromDesigner" id="action1">
        <input type="hidden" name="oneviewType" value="" id="oneviewType">
        <input type="hidden" name="defvaluedate" value="" id="defvaluedate">
  <form action="" method="POST" id="oneviewgraphForm">
                 <input type="hidden" id="viewby" name="viewby"/>
                 <input type="hidden" id="viewbyIds" name="viewbyIds"/>
         <input type="hidden" id="graphName" name="graphName" value=""/>
            <input type="hidden" id="graphsId" name="graphsId" value=""/>
            <input type="hidden" id="measure" name="measure" />
 <input type="hidden" name="measureIds" id="measureIds"/>
            <input type="hidden" name="aggregation" id="aggregation"/>
            <input type="hidden" id="numOfCharts" name="numOfCharts"/>
            <input type="hidden" id="lines" name="lines"/>
            <input type="hidden" id="currLine" name="currLine" />
            <input type="hidden" id="currLineCharts" name="currLineCharts" />
            <input type="hidden" id="chartData" name="chartData" />
            <input type="hidden" id="idArradhoc" name="idArradhoc" />
            <input type="hidden" id="chartname" name="chartname" />
            <input type="hidden" id="fromoneview" name="fromoneview" value="true" />
            <input type="hidden" id="busrolename" name="busrolename" />
            <input type="hidden" id="drills" name="drills" />
            <input type="hidden" id="filters1" name="filters1" />
            <input type="hidden" id="driver" name="driver" />
            <input type="hidden" id="type" name="type" />
            <input type="hidden" id="drilltype" name="drilltype" value="within" />
  </form>

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
    </body>
</html>
