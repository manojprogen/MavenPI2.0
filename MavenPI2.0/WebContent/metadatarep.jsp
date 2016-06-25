
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.i18n.TranslaterHelper,prg.db.PbReturnObject,prg.db.PbDb,java.util.*" %>


<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String copyReportFlag = "true";
            String userId="";

            //added by Dinanath
            Locale curLocale = null;
            curLocale = (Locale) session.getAttribute("UserLocaleFormat");

          userId = String.valueOf(request.getSession(false).getAttribute("USERID"));

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
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet"/>
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/pbReportViewerCSS.css" rel="stylesheet"/>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/style.css" type="text/css" media="print, projection, screen">
<!--        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />-->

        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/autocomplete/jquery.autocomplete-min.js"></script>
        <link type="text/css" href="<%=contextPath%>/javascript/lib/jquery/autocomplete/styles.css" rel="stylesheet" />
           <script type="text/javascript" src="<%=contextPath%>/javascript/pi.js"></script>


       
        <style type="text/css">
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
            #hyperRep:hover{text-decoration:underline;}
            .autocomplete{min-width: 280px;background-color: #fff;font-size: 12px;cursor: pointer;border:1px solid #ccc;overflow: hidden;overflow-y: auto}
            .autocomplete div{padding:3px 0px 3px 10px}
            .autocomplete div:hover{background-color: #8BC34A;}
            
        </style>


    </head>
    <%
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader ("Expires", 0);

           
            String itemList=null;
          //  ArrayList UserReportPrevileges = new ArrayList();
          //  UserReportPrevileges = (ArrayList) session.getAttribute("UserReportPrevileges");
            String overWriteFlag = "";
            

     //       if (UserReportPrevileges.contains("Overwrite Report")) {
                overWriteFlag = "Yes";
     //       } else {
     //           overWriteFlag = "No";
     //       }
                if (session.getAttribute("repStudioSortList") == null) {
                   // if (request.getAttribute("repList") != null) {
                  //      list = (PbReturnObject) request.getAttribute("repList");
                  //  }
                    if (request.getAttribute("itemList") != null) {
                        itemList = (String) request.getAttribute("itemList");
                    }
                } 
                    String selectValue = "";
                    String seloption = "";
                    selectValue = (String) session.getAttribute("selectValue");
                    seloption = (String) session.getAttribute("seloption");
                    if (session.getAttribute("selectValue") == null) {
                        selectValue = "report_name";
                    }
                    if (session.getAttribute("seloption") == null) {
                        seloption = "ASC";
                    }
%>
    <body id="mainBody">
         <script type="text/javascript">
                  
//             
                  
                $(document).ready(function(){

                $.ajax({
                    url: "reportTemplateAction.do?templateParam=loadUIComponents",
                    success: function(data){
                        var html="";
                        var dataJson = eval('('+data+')');
                        var componentList=dataJson.componentLst;
                         html=html+"<option value='CREATE_CUSTOM_REPORT'>Create Custom Report</option>";
                        for(var i=0;i<componentList.length;i++){
                            if(componentList[i].componentCode!="SAVE_AS_ADVANCED_HTML"){
                                if(componentList[i].componentCode == "COPY_REPORT"){}
                                else{html=html+"<option value=\""+componentList[i].componentCode+"\">"+componentList[i].componentText+"</option>";}
                        }
                        }
                        $("#reportsComponent").html(html);
                    }
                })


                 var data='<%=itemList%>'
               var length= bulidTable(data,'tablesorterReport','Report','<%=request.getContextPath()%>','<%=themeColor%>')
               $("#allReps").val(length);
                          var options, a;
                options = {
                    serviceUrl:'studioAction.do?studioParam=autoSuggest&tab=Report&userId=<%=userId%>',
                    delimiter: /(,)\s*/, // regex or character
                    minChars:2,
                    deferRequestBy: 500, //miliseconds
                    maxHeight:400,
                    zIndex: 9999,
                    noCache: false

                };
                a = $("#srchTextReport").autocomplete(options);
                $("#srchTextReport").keyup(function(event){
                    if(event.keyCode == 13){
                       // searchVals();

                    }
                });

                     
                });
               
            </script>
        <form name="reportForm"  method="post" style="width:98%">
           
            <table  border="0px solid" width="100%">
                <tr valign="top">
                    <td align="left" width="50%">
                    <input type="text" class="myTextbox3" id="srchTextReport" name="srchTextReport" align="middle" style="width:280px;height:20px;">
                    <input  class="navtitle-hover" type="button" value="<%=TranslaterHelper.getTranslatedInLocale("search", curLocale)%>" onclick="displayStudioItem('srchTextReport','<%=userId%>','Report','tablesorterReport','<%=request.getContextPath()%>')" style="width:50px;height:20px;"/>
                    </td>
                   
                    <td align="right" width="50%">
                        <font style="font-weight: bolder;color: #000000"><%=TranslaterHelper.getTranslatedInLocale("action", curLocale)%></font>
                         &nbsp;&nbsp;&nbsp;
                        <select id="reportsComponent" style="width:auto" align="center"></select>
                        &nbsp;&nbsp;&nbsp;
                        <input type="button" value="<%=TranslaterHelper.getTranslatedInLocale("go", curLocale)%>" class="navtitle-hover" style="width:auto"  onclick="javascript:goToSelectedComponent('<%=request.getContextPath()%>')">

                    </td>
                </tr>
            </table>

            <table align="center" id="tablesorterReport" class="tablesorter" width="100%" border="0px solid" cellpadding="0" cellspacing="1" style='table-layout: fixed;' >
              
            </table>
                         <div id="pagerReport" align="left" >
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/first.png"class="first"/>
                             <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/prev.png" class="prev"/>
                            <input type="text" readonly class="pagedisplay" style="width:60px"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/next.png" class="next"/>
                            <img alt=""  src="<%=request.getContextPath()%>/tablesorter/addons/pager/icons/last.png" class="last"/>
                            <select class="pagesize" id="selPagerRep">
                                <option selected value="10">10</option>
                                <option value="15">15</option>
                                <option id="allReps" value="">All</option>

                            </select>
                        </div>
<!--            <input type="hidden" name="limitrep" id="limitrep" value=">">
            <br><br>-->

        </form>
        <div id="fade" class="black_overlay"></div>

        <div id="userFolderAssignmentDisplay">
            <iframe  id="userFolderAssignmentDisp" NAME='userFolderAssignmentDisp'  STYLE='display:none;height:400px'   class="white_content" SRC='' ></iframe>

        </div>
         <div id='loading' align="center" class='loading_image' style="display:none;">
              <img id='imgId' src='images/help-loading.gif'  border='0px'/>
          </div>
        <input type="hidden" name="overWriteFlag" id="overWriteFlag" value="<%=overWriteFlag%>">
        <input type="hidden" name="overWriteFlag" id="copyReportFlag" value="<%=copyReportFlag%>">
    </body>
</html>
