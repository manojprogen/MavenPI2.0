
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,java.util.Locale,com.progen.i18n.TranslaterHelper"%>



<%
    String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }

             //added by Dinanath
            Locale curLocale = null;
            curLocale = (Locale) session.getAttribute("UserLocaleFormat");

            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
            String contextPath=request.getContextPath();
%>

<html>
    <head>
        <title>pi EE</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor%>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
<!--         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">-->
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
         <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>

        <style>
            .black_overlay{
                display: none;
                position: absolute;
                top: 0%;
                left: 0%;
                width: 200%;
                height: 200%;
                background-color: black;
                z-index:1001;
                -moz-opacity: 0.5;
                opacity:.50;
                filter:alpha(opacity=50);
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
           
            img{cursor:pointer}
            #hyperDash:hover{text-decoration:underline}
        </style>
       
    </head>
    <%
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String userId="";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

            
         
             String itemList=null;
             String KPIDashItemList=null;
            if (session.getAttribute("dashSortList") == null) {
               // if (request.getAttribute("dashList") != null) {
                   // list = (PbReturnObject) request.getAttribute("dashList");
                //}
                 if (request.getAttribute("itemList") != null) {
                      itemList = (String) request.getAttribute("itemList");
            }if(request.getAttribute("KPIDashboarditemList") != null){
                     KPIDashItemList=(String) request.getAttribute("KPIDashboarditemList");
            }
          if(KPIDashItemList==null){
            KPIDashItemList="";
                 }
                 }
               

    %>
    <body id="mainBody">
        <%
           String selectValue = "";
           String seloption = "";
           selectValue = (String) session.getAttribute("selectValueDash");
           seloption = (String) session.getAttribute("seloptionDash");
           //  ////.println("---selectValue---b4---"+selectValue);
           //  ////.println("---seloption---b4---"+seloption);
           if (session.getAttribute("selectValueDash") == null) {
               selectValue = "report_name";
           }
           if (session.getAttribute("seloptionDash") == null) {
               seloption = "ASC";
           }
           //  ////.println("---selectValue---"+selectValue);
           //  ////.println("---seloption---"+seloption);
%>


  <script type="text/javascript">


                     $(document).ready(function(){
                  
                      var data='<%=itemList%>'
                      var data2='<%=KPIDashItemList%>'

                var length=bulidTableWithKpi(data,'tablesorterDashboard','Dashboard','<%=request.getContextPath()%>','<%=themeColor%>',data2)
                $("#allDashs").val(length);

                        var options, a;
                options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=Dashboard&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextDash").autocomplete(options);
                $("#srchTextDash").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });
//                  $("#tablesorterDashboard")
//                    .tablesorter({headers : {0:{sorter:false}}})
//                    .tablesorterPager({container: $('#pagerDashboard')})

                  $("#selectkpiBussRole").dialog({
                        autoOpen: false,
                        height: 400,
                        width: 300,
                        position: 'justify',
                        modal: true
                });



                    });
                function goToSelectedDbrdComp(path){
                   var selectedcompVal=$("#DashboardComponents").val()
                   if(selectedcompVal=='0'){
                     createDashboard()
                   }else if(selectedcompVal=='1'){
                     editDashboard(path)
                   }else if(selectedcompVal=='2'){
                     EditDashboardName(path)
                   }else if(selectedcompVal=='3'){
                     deleteDashboard(path)
                   }else if(selectedcompVal=='4'){
                        $("#timedash").val('');
                     createKPIDashboard();
                   }else if(selectedcompVal=='5'){
                       $("#timedash").val('timedash');
                     createKPIDashboard();
                   }
                   else
                     purgeDashboard(path)
                }
                   </script>
        <form name="dashboardForm"  method="post" style="width:98%" action="">
             <script type="text/javascript">
             
            </script>
            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="left" width="20%">
                        <input type="text" class="myTextbox3" id="srchTextDash" name="srchTextDash" align="middle" style="width:280px;height:20px;">
                        <input class="navtitle-hover" type="button"  value="<%=TranslaterHelper.getTranslatedInLocale("search", curLocale)%>" onclick="displayStudioItem('srchTextDash',<%=userId%>,'Dashboard','tablesorterDashboard','<%=request.getContextPath()%>')" style="width:50px;height:20px;" />
                    </td>
                    <td align="right" width="38%">
                        <font style="font-weight: bolder;color: #000000"><%=TranslaterHelper.getTranslatedInLocale("action", curLocale)%></font>
                         &nbsp;&nbsp;&nbsp;
                        <select id="DashboardComponents" style="width:auto" align="center">
                            <option value="0"><%=TranslaterHelper.getTranslatedInLocale("create_dashboard", curLocale)%></option>
                            <option value="1"><%=TranslaterHelper.getTranslatedInLocale("edit_dashboard", curLocale)%></option>
                            <option value="2"><%=TranslaterHelper.getTranslatedInLocale("edit_dashboard_name", curLocale)%></option>
                            <option value="3"><%=TranslaterHelper.getTranslatedInLocale("delete_dashboard", curLocale)%></option>
                             <option value="4"><%=TranslaterHelper.getTranslatedInLocale("kpi_dashboard", curLocale)%></option>
                             <option value="5"><%=TranslaterHelper.getTranslatedInLocale("timebased_dashboard", curLocale)%></option>
<%--                            <option value="4">Purge Dashboard</option>--%>
                        </select>
                         &nbsp;&nbsp;&nbsp;
                         <input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("go", curLocale)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:goToSelectedDbrdComp('<%=request.getContextPath()%>')">
<%--                        <input type="button" value="Create Dashboard" class="navtitle-hover"   style="width:auto" onclick="javascript:createDashboard()">
                        <input type="button" value="Edit Dashboard" class="navtitle-hover"   style="width:auto" onclick="javascript:editDashboard('<%=request.getContextPath()%>')">
                        <input type="button" value="Edit Dashboard Name" class="navtitle-hover" style="width:auto"  onclick="javascript:EditDashboardName('<%=request.getContextPath()%>')">
                        <input type="button" value="Delete Dashboard" class="navtitle-hover" style="width:auto" onclick="javascript:deleteDashboard('<%=request.getContextPath()%>')">
                        <input type="button" value="Purge Dashboard" class="navtitle-hover" style="width:auto" onclick="javascript:purgeDashboard('<%=request.getContextPath()%>')">--%>
                    </td>
                </tr>
            </table>

                        <table align="center"  id="tablesorterDashboard" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">

                        </table>
                     <div id="pagerDashboard" class="pager" align="left" >
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                               <option id="allDashs" value="">All</option>
                            </select>
                        </div>
<!--                        <input type="hidden" name="limitdash" id="limitdash" value="">-->
<input type="hidden" name="timedash" id="timedash" value="">

            <br><br>
        </form>
        <div id="fade" class="black_overlay"></div>
      <div id="selectkpiBussRole" title="Select Business Role" style="display:none">

                       </div>
<!--        <div id="selectTimeBussRole" title="Select Business Role" style="display:none">

                       </div>-->
        <form name="kpidashform" method="post" style="padding:0pt" action="">

        </form>
<form name="timedashForm" method="post" style="padding:0pt" action="">

        </form>
    </body>
</html>