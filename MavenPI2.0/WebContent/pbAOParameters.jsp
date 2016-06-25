<%--
    Document   : pbAOParameters
    Created on : Dec 4, 2015, 7:31:55 PM
    Author     : amar.pal@progenbusiness.com
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbReturnObject,prg.db.Container,com.progen.reportview.db.PbReportViewerDAO,java.util.*,com.progen.report.PbReportCollection,com.progen.report.display.DisplayParameters,com.progen.reportdesigner.db.ReportTemplateDAO,prg.db.PbDb"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            PbReportViewerDAO viewDAO = new PbReportViewerDAO();
            String reportId = request.getParameter("REPORTID");
            HashMap map = null;
            Container container = null;

            ArrayList MeasureIds = null;
            ArrayList MeasureNames = null;
            HashMap TableHashMap = null;
            String MeasureRegion = "";
            StringBuffer prevColumns = new StringBuffer("");
            String PrevMsrStr = null;

            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) session.getAttribute("PROGENTABLES");

                if (map.get(reportId) != null) {
                    container = (Container) map.get(reportId);
                    TableHashMap = container.getTableHashMap();
                    MeasureIds = (ArrayList) TableHashMap.get("Measures");
                    MeasureNames = (ArrayList) TableHashMap.get("MeasuresNames");
                    if (MeasureIds != null && !MeasureIds.isEmpty()) {
                        MeasureRegion = viewDAO.buildTableColumns((String[]) MeasureIds.toArray(new String[0]), (String[]) MeasureNames.toArray(new String[0]), prevColumns);
                        for (int i = 0; i < MeasureIds.size(); i++) {
                            prevColumns.append("," + MeasureIds.get(i).toString().replace("A_", ""));
                        }
                        PrevMsrStr = prevColumns.toString().substring(1);
                    } else {
                        PrevMsrStr = "";
                    }
                }
            }
%>
<%
            String MeasuresData = "";
            String isTimeDashboard = "";
            if (request.getAttribute("Measures") != null) {
                MeasuresData = String.valueOf(request.getAttribute("Measures"));
                isTimeDashboard = String.valueOf(request.getAttribute("isTimeDashboard"));
            }
        %>
<%  String reportId1= (String) request.getAttribute("REPORTID");

   String[] scheduleday={"Mon","Tue","Wed","Thur","Fri","Sat","Sun"};
   String[] sday={"2","3","4","5","6","7","1"};

        Container container1 = Container.getContainerFromSession(request, reportId1);
        PbReportCollection collect = new PbReportCollection();
        collect = container.getReportCollect();
         ArrayList<String> timeinfo = collect.timeDetailsArray;
         String rangebaseDate="";
         String StdDate="";
         String[] vals1= null;
     //     String sytm=(String.valueOf(session.getAttribute("isYearCal")));
          //added by Nazneen
                      PbReturnObject returnObject = null;
                      String setUpCharVal = "";
                      String sytm = "No";
                      PbDb pbdb = new PbDb();
                      String sql = "SELECT SETUP_CHAR_VALUE FROM PRG_GBL_SETUP_VALUES WHERE SETUP_KEY = 'GLOBAL_PARAMS'";
                      try {
                          returnObject = pbdb.execSelectSQL(sql);
                          if (returnObject.getRowCount() > 0) {
                              setUpCharVal = returnObject.getFieldValueString(0, 0);
                          }
                          if (setUpCharVal != null && !setUpCharVal.equalsIgnoreCase("") && !setUpCharVal.equalsIgnoreCase("null")) {
                              String arrSetUpCharVal[] = setUpCharVal.split(";");
                              for (int i = 0; i < arrSetUpCharVal.length; i++) {
                                  String temp = arrSetUpCharVal[i];
                                  if (temp.contains("isYearCal")) {
                                      sytm = temp.substring(temp.indexOf("~") + 1);
                                  }
                              }
                          }
                      } catch (Exception exp) {
                          exp.printStackTrace();
                      }
                      session.setAttribute("isYearCal", sytm);
                      //ended  by Nazneen
          if(collect.timeDetailsArray!=null){
         String autoDate="";
         String vals= " ";
        vals = timeinfo.toString();
        vals = vals.replace("[", "");
        vals = vals.replace("]", "");
         vals1 = vals.split(",");

        container.evaluateReportDateHeaders();
        }
        %>
<%
            //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

String contexTPath=request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Meaures</title>
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
          <script type="text/javascript" src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script type="text/javascript" src="<%=contexTPath%>/javascript/lib/jquery/js/jquery-1.8.23-custom.min.js"></script>
        <script src="<%=contexTPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contexTPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contexTPath%>/javascript/treeview/demo.js"></script>
         <script type="text/javascript" src="<%=contexTPath%>/javascript/reportviewer/ReportViewer.js"></script>
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
       <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contexTPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contexTPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->
        <!--addeb by bharathi reddy fro search option-->
<!--         <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.datepicker.js"></script>-->
        <script type="text/javascript" src="<%=contexTPath%>/javascript/quicksearch.js"></script>
        <script type="text/javascript" src="<%=contexTPath%>/javascript/reportDesign.js"></script>
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contexTPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <link href="<%=contexTPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contexTPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contexTPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contexTPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
         <link href="<%=contexTPath%>/stylesheets/jquery.contextMenu.css" rel="stylesheet" type="text/css" />
        <script  type="text/javascript" src="<%=contexTPath%>/javascript/jquery.contextMenu.js" ></script>
        <link type="text/css" href="<%=contexTPath%>/datedesign.css" rel="stylesheet"/>

        <script type="text/javascript" >
            $(document).ready(function() {
                $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });
        </script>
         
        
      
        <style type="text/css" >
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#EAF2F7;
                border:0px;
                /*apply this class to a Headings of servicestable only*/
            }
            .prgtableheader
            {   font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#EAF2F7;
                height:100%;
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:22px;
                text-decoration:none;
                cursor: pointer;
                border-bottom: 1px solid #999999;
                border-right: 1px solid #999999;
                border-left: 1px solid #F5F5F5;
                border-top:1px solid #F5F5F5;
                margin:2px;
            }
        </style>
        <style>
#dateregionDiv { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#fixedtop1 { position: fixed; top: 0px; left:200px; margin: auto; right: 800px; width: 50px; border: none; z-index: 50; }
#center250a { width: auto;height: 65px;  background:none; }
#center250b { width: auto;height: 65px;   background:none; }
            </style>
    </head>
    <body >

        <form name="myForm2" method="post">
            <table style="width:100%;height:220px" border="solid black 1px" >
                <tr>
                    <td width="50%" valign="top" class="draggedTable1">
                        <div style="height:20px" class="ui-state-default draggedDivs ui-corner-all"><font size="2" style="font-weight:bold">Measures</font></div>
                        <div class="masterDiv" style="height:200px;overflow-y:scroll">

                            <ul id="measureTree" class="filetree treeview-famfamfam">
                                <%--<ul id="TabColsTree" class="filetree treeview-famfamfam">
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="TabCols">
                                    </ul>
                                </li>
                            </ul>--%>
                                <li class="open" style="background-image:url('images/treeViewImages/plus.gif')">
                                    <ul id="measures">
                                        <%=MeasuresData%>
                                    </ul>
                                </li>
                            </ul>

                        </div>
                    </td>
                    <td width="50%" valign="top"  id="draggableMeasures" valign="top">
                        <h3 style="height:20px" align="left" tabindex="0" aria-expanded="true" role="tab" class="ui-accordion-header ui-helper-reset ui-state-default ui-corner-all">
                            <font size="2" style="font-weight:bold">Drag Measures To Here </font>
                        </h3>
                        <div id="dragDiv" style="height:200px;overflow-y:scroll">
                            <ul id="sortable">
                                <%=MeasureRegion%>
                            </ul>
                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <center>
            <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveMeasuresAO()">
        </center>
                            <ul id="formulaViewListMenu" class="contextMenu" style="width:100px">
                                <li class="view"><a href="#view">View</a></li>
                            </ul>
                            <div id="formulaViewDiv" title="View Custom Measure" style="display:none">
                                <table><tr><td id="value"></td></tr></table>
                             </div>
      <script type="text/javascript" >
            var y="";
            var xmlHttp;
            var ctxPath;

            var prevColsStr="<%=PrevMsrStr%>"
            var prevCols=prevColsStr.split(",");
            var From=parent.document.getElementById("Designer").value;

            for(var k=0;k<prevCols.length;k++){
                var pr=msrArray.toString();
                if(pr.match(prevCols[k])==null){
                    msrArray.push(prevCols[k]);
                }
            }

            function saveMeasuresAO(){

                if(From != null && From=="busTemplateView"){
                    dispBusTemplateMesures();
                }else{
                 parent.cancelTabMeasure();
                dispMeasures();
                }
                if(From != null && From=="fromInsightDesigner" && From=="busTemplateView"){
//                if(From != null && From=="fromInsightDesigner"){
                }else{
                <%  if(collect.timeDetailsArray!=null && timeinfo.get(1).equalsIgnoreCase("PRG_DATE_RANGE") && collect.timeDetailsMap!=null  && !collect.timeDetailsMap.isEmpty()){%>
                      var html="";
                      html+=parent.$("#dateregionRange").html();
                  parent.$("#dateRegion").html(html);
                  parent.$("#field1").html("<%=container.fullName0%>");
                  parent.$("#field2").html("<%=container.dated%>");
                  parent.$("#field3").html("<%=container.day0%>");
                  parent.$("#tdfield1").html("<%=container.fullName%>");
                  parent.$("#tdfield2").html("<%=container.date%>");
                  parent.$("#tdfield3").html("<%=container.day%>");
                  parent.$("#cffield1").html("<%=container.cffullName%>");
                  parent.$("#cffield2").html("<%=container.cfdate%>");
                  parent.$("#cffield3").html("<%=container.cfday%>");
                  parent.$("#ctfield1").html("<%=container.ctfullName%>");
                  parent.$("#ctfield2").html("<%=container.ctdate%>");
                  parent.$("#ctfield3").html("<%=container.ctday%>");

                parent.$( "#fromdate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
                parent.$( "#todate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[3]%>')) );
                parent.$( "#comparefrom" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                dateFormat: "D,d,M,yy",
                 changeYear: true,
                  changeMonth: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1

            }).datepicker("setDate", new Date(('<%=vals1[4]%>')) );
                 parent.$( "#compareto" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"

            }).datepicker("setDate", new Date(('<%=vals1[5]%>')) );
            parent.$("#dateregionRange").show();
            parent.document.getElementById("saveadhoc").style.display='none';

               <% } else if(collect.timeDetailsArray!=null && timeinfo.get(1).equalsIgnoreCase("PRG_STD") && collect.timeDetailsMap!=null  && !collect.timeDetailsMap.isEmpty()) { %>
                var html="";
                html+=parent.$("#dateregionDiv").html();
                <% if( sytm.equalsIgnoreCase("No")) { %>   //for year vs year by mohit
                  parent.$("#pfield1").html("<%=(container.fullName0).substring(0,4)+(container.fullName0).substring(6)%>");
                  parent.$("#pfield2").html("<%=container.dated%>");
                  parent.$("#pfield3").html("<%=container.day0%>");
                  parent.$("#newdate").val("<%=(container.dated+""+"-"+(((container.fullName0)+"").replaceAll("'","-")))%>");
                parent.$( "#perioddate" ).datepicker({
                showOn: "button",
                buttonImage: "images/calendar_18x16.gif",
                buttonImageOnly: true,
                showButtonPanel: true,
                changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1,
                dateFormat: "D,d,M,yy"
            }).datepicker("setDate", new Date(('<%=vals1[2]%>')) );
            <%}else {

        //System.out.print("?mohit>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+container.fullName0);%>
                    <% String calndrYear = (container.fullName0).substring(4);
                    String s="";
                            for(int year=2005;year<=2020;year++){
                            if(calndrYear.equalsIgnoreCase(Integer.toString(year))){

                             s+="<option selected value="+year+">"+year+"</option>";
                           }else {
                                s+="<option value="+year+">"+year+"</option>";
                                }}%>

            //parent.$("#pfield1").html("<%=(container.fullName0).substring(4)%>");
            parent.$("#calPeriodYear").append("<%=s%>");
            //alert("<%=s%>")
            //parent.$('#calPeriodYear option:selected').val(<%=(container.fullName0).substring(4)%>)
            <%}%>

           <%
              DisplayParameters dur=new DisplayParameters();
              String duration1= String.valueOf(dur.displayTime(collect.timeDetailsMap)) ;
           %>
                  parent.$("#timebasistd").html('<%=duration1%>');
                  parent.$("#dateregionDiv").show();
                  parent.document.getElementById("saveadhoc").style.display='none';
                  <% }
                  if(collect.timeDetailsArray!=null){
                   container.evaluateReportDateHeaders();}
                  %>
            }
            }
            function dispMeasuresAO(){
                var msrs="";
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;
                }
                var action=null;
                if(From != null && From=="fromInsightDesigner")
                    action=parent.document.getElementById("action").value;
                if(From != null && From=="busTemplateView")
                    action=parent.document.getElementById("action1").value;
 var isTimeDashboard1= <%=isTimeDashboard%>
//alert(isTimeDashboard1)
                 if(isTimeDashboard1==true && msrIds.length!=0){
                    msrs=msrs.substring(1);
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;
                    parent.$("#timePeriodDialog").dialog({
            autoOpen: false,
            height:150,
            width: 273,
            position: 'justify',
            modal: true
        });
                     parent.$("#timePeriodDialog").dialog('open');
                      <%
                  for (int i = 0; i < 3; i++) {
                         %>
expandDiv2('<%=i%>')
//                   <%}%>

//                       parent.document.getElementById("timePeriodDialog").open;


                }else if(msrIds.length!=0){
                    msrs=msrs.substring(1);
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;

                    if(action!=null && action=="measChange"){
                    alert("measChange")
                    $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChangesAO&tableMsrs='+msrs+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                parent.PreviewTable();
                            }
                            });
                    }else{
                    alert("else");
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildTableAO&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&REPORTID=<%=reportId%>',
                        success: function(data) {
//                            alert("Measures")
                            parent.PreviewTable();
                            if(data!=""){
                            }
                        }
                    });
                }
                    parent.cancelTabMeasure();
                    parent.$("#GraphsRegOfReport").show();
                }
                else{
                    alert("Please Select Measures");
                    parent.$("#measuresDialog").dialog('open');
                }
            }
            function dispBusTemplateMesures(){
                var msrs="";
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                for(var  i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;
                }
                var action=null;
//               if(From != null && From=="busTemplateView")
                action=parent.document.getElementById("action1").value;
               var oneviewType=parent.document.getElementById("oneviewType").value;
               var msrSize=10;
//               if(oneviewType=="Business TemplateView")
//                   msrSize=10;
               if(msrIds.length!=0 && msrIds.length>msrSize){
                   alert("please select only "+msrSize+" measure")
               }else if(msrIds.length!=0){
                    parent.cancelTabMeasure();
                    msrs=msrs.substring(1);
                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;

                    if(action!=null && action=="measChange"){
                    $.ajax({
                            url: '<%=request.getContextPath()%>/reportViewer.do?reportBy=tableMeasureChanges&tableMsrs='+msrs+'&REPORTID=<%=reportId%>',
                            success: function(data){
                                 if(oneviewType=="Business TemplateView")
                                    parent.PreviewTable();
                                 else{
                                     parent.PreviewMsrTemplateTable();
                                 }
                            }
                            });
                    }else{
                    $.ajax({
                        url: 'reportTemplateAction.do?templateParam=buildTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&REPORTID=<%=reportId%>',
                        success: function(data) {
//                            alert("Measures")
                             if(oneviewType=="Business TemplateView")
                                    parent.PreviewTable();
                                 else{
                                     parent.PreviewMsrTemplateTable();
                                 }

                            if(data!=""){
                            }
                        }
                    });
                }
                    parent.cancelTabMeasure();

                }
                else{
                    alert("Please Select Measures")
                }
            }
            function cancelMeasure(){
                parent.cancelTabMeasure();
            }

           
                <%--$("#TabColsTree").treeview({
                    animated: "normal",
                    unique:true
                });--%>
                $('ul#measureTree li').quicksearch({
                    position: 'before',
                    attached: 'ul#measureTree',
                    loaderText: '',
                    delay: 100
                })
                  $(".formulaViewMenu").contextMenu({
                    menu: 'formulaViewListMenu',
                    leftButton: true },
                function(action, el, pos) {
                    contextMenuWorkFormulaView(action, el, pos);
            });
                $("#formulaViewDiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 150,
                    width: 250,
                    position: 'absolute',
                    modal: true
                });
            });

            $(function() {
                var dragMeasure=$('#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span')
                var dropMeasures=$('#draggableMeasures');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > ul > li > span,#measures > li > ul > li >  span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createMeasures(measure,ui.draggable.attr('id'));
                    }
                });
            });

            $(function() {
                $("#sortable").sortable();
                $("#sortable").disableSelection();
            });
            function prevMeasures(){
                var prevMsrs=parent.document.getElementById("Measures").value;
                if(prevMsrs.length!=0){
                    prevMsrs=prevMsrs.split(",");
                    for(var m=0;m<prevMsrs.length;m++){
                        var msrElmnts=prevMsrs[m].split("-");
                        createMeasures(msrElmnts[0],"elmnt-"+msrElmnts[1]);
                    }
                }
            }
                 function contextMenuWorkFormulaView(action, el, pos){

                document.getElementById("value").innerHTML=$(el).attr('title');
                $("#formulaViewDiv").dialog('open');


            }

        </script>
    </body>
</html>
