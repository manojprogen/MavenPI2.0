
<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.Locale,prg.db.PbReturnObject,java.util.ArrayList,com.progen.i18n.TranslaterHelper"%>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String userId="";
            userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

             String itemList=null;

            //
            //added by Dinanath
            Locale curLocale = null;
            curLocale = (Locale) session.getAttribute("UserLocaleFormat");
          
            if (session.getAttribute("allRepSortList") == null)
            {
                if (request.getAttribute("itemList") != null)
                     itemList = (String) request.getAttribute("itemList");
            }

             String userIdStr = "";
             String themeColor="blue";
            if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
             String contextPath=request.getContextPath();
%>

<html>
    <head>
        <title>pi EE</title>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
<!--         <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
            <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>
             <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />

        <style type="text/css">
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
            }
           
             img{cursor:pointer}
             #hyperAll:hover{text-decoration:underline}
        </style>

    </head>
  
    <body>
        <script type="text/javascript">
                $(document).ready(function(){
                   
                      var data='<%=itemList%>'
               var length= bulidTable(data,'tablesorterAll','MyReports','<%=request.getContextPath()%>','<%=themeColor%>');
               $("#allMyReps").val(length);
                  var options, a;
                 options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=MyReport&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    width:450,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextMyReport").autocomplete(options);
                $("#srchTextMyReport").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });
//                    $("#tablesorterAll")
//                    .tablesorter({headers : {0:{sorter:false}}})
//                    .tablesorterPager({container: $('#pagerallReps')})
                });
            </script>
        <%
            String selectValue= "";
            String seloption = "";
            selectValue = (String) session.getAttribute("selectValueAllRep");
            seloption = (String) session.getAttribute("seloptionAllRep");
           //  ////.println("---selectValue---b4---"+selectValue);
           //  ////.println("---seloption---b4---"+seloption);
            if (session.getAttribute("selectValueAllRep") == null) {
                selectValue = "report_name";
            }
            if (session.getAttribute("seloptionAllRep") == null) {
                seloption = "ASC";
                }
           //  ////.println("---selectValue---"+selectValue);
           //  ////.println("---seloption---"+seloption);
        %>
        <form name="myForm"  method="post" style="width:98%">
         
            <table width="100%">
                <tr>
                    <td align="left" width="60%">
                            <input type="text" class="myTextbox3" id="srchTextMyReport" name="srchTextMyReport" align="middle" style="width:280px;height:20px;">
                        <input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("search", curLocale)%>" onclick="displayStudioItem('srchTextMyReport','<%=userId%>','MyReports','tablesorterAll','<%=request.getContextPath()%>')" style="width:50px;height:20px;" class="navtitle-hover"/>
                    </td>
                    <td align="right" width="38%">
                        <input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("save_as_html", curLocale)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:createDataSnapshot()">
                        <input class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("download", curLocale)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:downloadAsSnapshot()">
                    </td>
                </tr>
            </table>
            <table align="center" id="tablesorterAll" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1">
             

            </table>
            <div id="pagerallReps" class="pager">
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png" class="first"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allMyReps" value="">All</option>
                            </select>
                        </div>
                <div id='loading' class='loading_image' style="display:none;">
                    <img id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 150px; top: 10px;'/>
                </div>
            <br><br>
        </form>
    </body>
</html>