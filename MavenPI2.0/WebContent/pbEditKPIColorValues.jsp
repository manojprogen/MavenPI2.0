<%--
    Document   : pbEditKPIColorValues
    Created on : Oct 8, 2010, 7:39:01 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>

<%@page import="java.util.*,prg.db.Container,com.progen.report.DashletDetail,com.progen.report.query.PbReportQuery,com.progen.report.entities.KPI"%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportdesigner.db.DashboardTemplateDAO,prg.db.PbReturnObject,com.progen.report.entities.KPI,com.progen.report.entities.KPIColorRange,com.progen.report.pbDashboardCollection"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Kpis</title>

        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=contextPath%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=contextPath%>/querydesigner/JS/jquery.columnfilters.js"></script>-->

        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
<!--        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/docs.js"></script>-->
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <%String path = request.getContextPath();%>
      

        <style>
            .myhead
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 8pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                width:50%;
                background-color:#e6e6e6;
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
            *{font:11px verdana}
        </style>
        <script>
            var divId=parent.document.getElementById("divId").value;
            //prevGrpCols(divId);
        </script>
    </head>
    <body onload="loadall()" >
        <%
                    String existHrVal = "";
                    String existMrVal = "";
                    String existLrVal = "";
                    ArrayList rangeValList = new ArrayList();
                    rangeValList.add(">");
                    rangeValList.add(">=");
                    rangeValList.add("<");
                    rangeValList.add("<=");
                    rangeValList.add("=");
                    rangeValList.add("between");
                    String elementId = request.getParameter("elementId");
                    String kpiMasterid = request.getParameter("kpiMasterid");
                    String kpiName = request.getParameter("kpiName");
                    String userId = request.getParameter("userId");
                    String changePercentVal = request.getParameter("changePercentVal");
                    String reportId = request.getParameter("reportId");
                    String dashletId = request.getParameter("dashletId");
                    String fromDesigner = request.getParameter("fromDesigner");
                    DashboardTemplateDAO dbrdDAO = new DashboardTemplateDAO();
                    PbReturnObject kpiColorObj=null;
                     HashMap map = new HashMap();
                     String colorValhigh1 = "0.0";
                     String colorValhigh2 = "0.0";
                     String colorValmedium1 = "0.0";
                     String colorValmedium2 = "0.0";
                     String colorVallow1 = "0.0";
                     String colorVallow2 = "0.0";
                             Container container = null;
                             map = (HashMap) session.getAttribute("PROGENTABLES");
                          container = (Container) map.get(reportId);
                     pbDashboardCollection collect = (pbDashboardCollection)container.getReportCollect();
                             DashletDetail detail = collect.getDashletDetail(dashletId);
                         KPI kpiDetails = (KPI) detail.getReportDetails();
                        List<KPIColorRange> kpiColorRangeList = kpiDetails.getKPIColorRanges(elementId);

                        for(KPIColorRange kpi : kpiColorRangeList){
                            if(kpi.getColor().equalsIgnoreCase("green")){
                                colorValhigh1 = Double.toString(kpi.getRangeStartValue());
                                colorValhigh2 = Double.toString(kpi.getRangeEndValue());
                                existHrVal = kpi.getOperator();
                                }
                            if(kpi.getColor().equalsIgnoreCase("yellow")){
                                colorValmedium1 = Double.toString(kpi.getRangeStartValue());
                                colorValmedium2 = Double.toString(kpi.getRangeEndValue());
                                existMrVal = kpi.getOperator();
                                }
                            if(kpi.getColor().equalsIgnoreCase("red")){
                                colorVallow1 = Double.toString(kpi.getRangeStartValue());
                                colorVallow2 = Double.toString(kpi.getRangeEndValue());
                                existLrVal = kpi.getOperator();
                                }
                            }


        %>
        <form name="myForm2" method="post">
            <div id="tabselect">
                <table align="center">
                    <tr>
                        <td>
                            <font size="1px" color="#369"><b>KPI Element is</b></font> : <%=kpiName%>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <font size="1px" color="#369"><b>Change% Value is</b></font> : <%=changePercentVal%>
                        </td>
                    </tr>
                </table>
                <br>
                <br>
                <table style="width:100%;" border="solid black " align="center" >
                    <Tr>
                        <td ><font size="1px"><b>Define Ranges</b></font></td>
                        <Td><font size="1px"><b>Operator</b></font></Td>
                        <Td><font size="1px"><b>Change% Range </b></font></Td>

                    </Tr>
                    <Tr>
                        <Td >Green Range</Td>
                        <Td>
                            <Select name="highRisk" STYLE="width:100px" class="myTextbox3" id="highRisk" onchange="addHighRisk()">
                                <%
                                            if (existHrVal.equalsIgnoreCase("")) {%>
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between" selected>between</Option>
                                <%} else {
                                                                    for (int h = 0; h < rangeValList.size(); h++) {
                                                                        if (rangeValList.get(h).toString().equalsIgnoreCase(existHrVal)) {%>
                                <Option value="<%=rangeValList.get(h)%>" selected><%=rangeValList.get(h)%></Option>
                                <%} else {%>
                                <Option value="<%=rangeValList.get(h)%>"><%=rangeValList.get(h)%></Option>
                                <%}
                                                }
                                            }%>
                            </Select>
                        </Td>
                        <Td>

                            <div id="singleRisk">
                                <Input type="text"  class="myTextbox3" name="box1h" id="box1h" style="width:62px" value="<%=colorValhigh1%>" onkeyup="populateValue()">
                            </div>
                            <div id="doubleRisk">
                                <Input type="text"  class="myTextbox3" name="box1" id="box1" style="width:62px" value="<%=colorValhigh1%>">
                                <Input type="text"  class="myTextbox3" name="box2" id="box2" style="width:62px" value="<%=colorValhigh2%>" onkeyup="populateValuedh()">
                            </div>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td >Yellow Range</Td>
                        <Td>
                            <Select name="mediumRisk" STYLE="width:100px" class="myTextbox3" id="mediumRisk" onchange="addMediumRisk()">
                                <%
                                            if (existMrVal.equalsIgnoreCase("")) {%>
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between" selected>between</Option>
                                <%} else {
                                                                    for (int h = 0; h < rangeValList.size(); h++) {
                                                                        if (rangeValList.get(h).toString().equalsIgnoreCase(existMrVal)) {%>
                                <Option value="<%=rangeValList.get(h)%>" selected><%=rangeValList.get(h)%></Option>
                                <%} else {%>
                                <Option value="<%=rangeValList.get(h)%>"><%=rangeValList.get(h)%></Option>
                                <%}
                                                }
                                            }%>
                            </Select>
                        </Td>

                        <Td>
                            <div id="mediumsingleRisk">
                                <Input type="text"  class="myTextbox3" name="mediumbox1m" id="mediumbox1m" style="width:62px;background:white" value="<%=colorValmedium1%>" onkeyup="populateValuem()">
                            </div>
                            <div id="mediumdoubleRisk">
                                <Input type="text"  class="myTextbox3"  name="mediumbox1" id="mediumbox1" style="width:62px;background:white" value="<%=colorValmedium1%>">
                                <Input type="text"  class="myTextbox3" name="mediumbox2" id="mediumbox2" style="width:62px" value="<%=colorValmedium2%>" onkeyup="populateValuedm()">
                            </div>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td >Red Range</Td>
                        <Td>
                            <Select name="lowRisk" STYLE="width:100px" class="myTextbox3" id="lowRisk" onchange="addLowRisk()">
                                <%
                                            if (existLrVal.equalsIgnoreCase("")) {%>
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between" selected>between</Option>
                                <%} else {
                                    for (int h = 0; h < rangeValList.size(); h++) {
                                        if (rangeValList.get(h).toString().equalsIgnoreCase(existLrVal)) {%>
                                <Option value="<%=rangeValList.get(h)%>" selected><%=rangeValList.get(h)%></Option>
                                <%} else {%>
                                <Option value="<%=rangeValList.get(h)%>"><%=rangeValList.get(h)%></Option>
                                <%}
                                                }
                                            }%>
                            </Select>
                        </Td>
                        <Td>
                            <div id="lowsingleRisk">
                                <Input type="text"  class="myTextbox3"  name="lowbox1l" id="lowbox1l" style="width:62px;background:white " value="<%=colorVallow1%>">
                            </div>
                            <div id="lowdoubleRisk">
                                <Input type="text"  class="myTextbox3"   name="lowbox1" id="lowbox1" style="width:62px;background:white" value="<%=colorVallow1%>">
                                <Input type="text"  class="myTextbox3" name="lowbox2" id="lowbox2" style="width:62px" value="<%=colorVallow2%>">
                            </div>
                        </Td>
                    </Tr>

                    <input type="hidden" name="kpiName" value="<%=kpiName%>" id="kpiName">
                    <input type="hidden" name="elementId" value="<%=elementId%>" id="elementId">
                    <input type="hidden" name="kpiMasterid" value="<%=kpiMasterid%>" id="kpiMasterid">
                    <input type="hidden" name="userId" value="<%=userId%>" id="userId">
                    <input type="hidden" name="changePercentVal" value="<%=changePercentVal%>" id="changePercentVal">
                </table>
                <center>
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveKPICustomColor('<%=kpiName%>','<%=elementId%>','<%=kpiMasterid%>','<%=changePercentVal%>','<%=reportId%>','<%=dashletId%>','<%=fromDesigner%>')">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="closeColorWindow()">
                </center>
            </div>
        </form>
                      <script type="text/javascript">
            var path = "<%=path%>";

            function populateValue()
            {
                var hr = document.getElementById("box1h").value;
                var mr = document.getElementById("mediumRisk").value;
                if(mr=="between")
                {
                    document.getElementById("mediumbox1").value = hr;
                }
                else
                {
                    document.getElementById("mediumbox1m").value = hr;
                }
            }
            function populateValuedh()
            {
                var hr = document.getElementById("box2").value;
                var mr = document.getElementById("mediumRisk").value;
                if(mr=="between")
                {
                    document.getElementById("mediumbox1").value = hr;
                }
                else
                {
                    document.getElementById("mediumbox1m").value = hr;
                }
            }
            function populateValuem()
            {
                var mr = document.getElementById("mediumbox1m").value;
                var lr = document.getElementById("lowRisk").value;
                if(lr=="between")
                {
                    document.getElementById("lowbox1").value = mr;
                }
                else
                {
                    document.getElementById("lowbox1l").value = mr;
                }
            }
            function populateValuedm()
            {
                var mr = document.getElementById("mediumbox2").value;
                var lr = document.getElementById("lowRisk").value;
                if(lr=="between")
                {
                    document.getElementById("lowbox1").value = mr;
                }
                else
                {
                    document.getElementById("lowbox1l").value = mr;
                }
            }
            function addHighRisk()
            {
                var riskOp = $("#highRisk").val();
                if(riskOp=="between")
                {
                    document.getElementById('doubleRisk').style.display='';
                    document.getElementById('singleRisk').style.display='none';
                }
                else{
                    document.getElementById('doubleRisk').style.display='none';
                    document.getElementById('singleRisk').style.display='';
                }

            }
            function addMediumRisk()
            {
                var riskOp = $("#mediumRisk").val();
                if(riskOp=="between")
                {
                    document.getElementById('mediumdoubleRisk').style.display='';
                    document.getElementById('mediumsingleRisk').style.display='none';
                }
                else{

                    document.getElementById('mediumdoubleRisk').style.display='none';
                    document.getElementById('mediumsingleRisk').style.display='';
                }

            }
            function addLowRisk()
            {
                var riskOp = $("#lowRisk").val();
                if(riskOp=="between")
                {
                    document.getElementById('lowdoubleRisk').style.display='';
                    document.getElementById('lowsingleRisk').style.display='none';
                }
                else{

                    document.getElementById('lowdoubleRisk').style.display='none';
                    document.getElementById('lowsingleRisk').style.display='';
                }

            }
            function saveKPICustomColor(kpiName,elementId,kpiMasterId,changePercentVal, reportId, dashletId,fromDesigner){
                var hrVal = document.getElementById("highRisk").value;
                var hr1;
                var hr2;
                var mr1;
                var mr2;
                var lr1;
                var lr2;
                var counth=0;
                var countm=0;
                var countl=0;
                if(hrVal=="between")
                {
                    hr1 = document.getElementById("box1").value;
                    hr2 = document.getElementById("box2").value;
                    if(hr1=="" || hr2==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }
                else
                {
                    hr1 = document.getElementById("box1h").value;
                    hr2 = 0;
                    if(hr1==""){
                        counth=0;
                    }else{
                        counth=1;
                    }
                }

                var mrVal = document.getElementById("mediumRisk").value;
                if(mrVal=="between")
                {
                    mr1 = document.getElementById("mediumbox1").value;
                    mr2 = document.getElementById("mediumbox2").value;
                    if(mr1=="" || mr2==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }
                else
                {
                    mr1 = document.getElementById("mediumbox1m").value;
                    mr2 = 0;
                    if(mr1==""){
                        countm=0;
                    }else{
                        countm=1;
                    }
                }

                var lrVal = document.getElementById("lowRisk").value;
                if(lrVal=="between")
                {
                    lr1 = document.getElementById("lowbox1").value;
                    lr2 = document.getElementById("lowbox2").value;
                    if(lr1=="" || lr2==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                else
                {
                    lr1 = document.getElementById("lowbox1l").value;
                    lr2 = 0;
                    if(lr1==""){
                        countl=0;
                    }else{
                        countl=1;
                    }
                }
                if(countl==1&&countm==1&&counth==1){
                    $.ajax({
                        url: path+'/dashboardViewer.do?reportBy=saveKPICustomColorVals&hr1='+hr1+'&hr2='+hr2+'&mr1='+mr1+'&mr2='+mr2
                            +'&lr1='+lr1+'&lr2='+lr2+'&kpiName='+kpiName+'&elementId='+elementId+'&kpiMasterId='+kpiMasterId
                            +'&changePercentVal='+changePercentVal+'&hrVal='+hrVal+'&mrVal='+mrVal+'&lrVal='+lrVal
                            +'&reportId='+reportId+'&dashletId='+dashletId,
                        success: function(data) {
                            parent.$("#editKpiCustomColor").dialog('close');
                            parent.displayKPI(dashletId, reportId, "","",kpiMasterId, "","","",fromDesigner);
                        }
                    });
                }else{
                    alert('Please enter all range values');
                }
            }


            function showChartImage(kpiName,elementId,kpiMasterId,changePercentVal)
            {
                var hrVal = document.getElementById("highRisk").value;
                var hr1;
                var hr2;
                var mr1;
                var mr2;
                var lr1;
                var lr2;
                if(hrVal=="between")
                {
                    hr1 = document.getElementById("box1").value;
                    hr2 = document.getElementById("box2").value;
                }
                else
                {
                    hr1 = document.getElementById("box1h").value;
                    hr2 = 0;
                }

                var mrVal = document.getElementById("mediumRisk").value;
                if(mrVal=="between")
                {
                    mr1 = document.getElementById("mediumbox1").value;
                    mr2 = document.getElementById("mediumbox2").value;
                }
                else
                {
                    mr1 = document.getElementById("mediumbox1m").value;
                    mr2 = 0;
                }

                var lrVal = document.getElementById("lowRisk").value;
                if(lrVal=="between")
                {
                    lr1 = document.getElementById("lowbox1").value;
                    lr2 = document.getElementById("lowbox2").value;
                }
                else
                {
                    lr1 = document.getElementById("lowbox1l").value;
                    lr2 = 0;
                }

                $.ajax({
                    url: path+'/dashboardViewer.do?reportBy=saveKPICustomColorVals&hr1='+hr1+'&hr2='+hr2+'&mr1='+mr1+'&mr2='+mr2+'&lr1='+lr1+'&lr2='+lr2+'&kpiName='+kpiName+'&elementId='+elementId+'&kpiMasterId='+kpiMasterId+'&changePercentVal='+changePercentVal+'&hrVal='+hrVal+'&mrVal='+mrVal+'&lrVal='+lrVal,
                    success: function(data) {
                        parent.$("#editKpiCustomColor").dialog('close');
                        parent.window.location.reload(true);
                    }
                });
            }
            
            function closeColorWindow(){
                parent.$("#editKpiCustomColor").dialog('close');
            }

            function loadall(){
//                document.getElementById("doubleRisk").style.display='none';
//                document.getElementById("singleRisk").style.display='';
//                document.getElementById("mediumdoubleRisk").style.display='none';
//                document.getElementById("mediumsingleRisk").style.display='';
//                document.getElementById("lowdoubleRisk").style.display='none';
//                document.getElementById("lowsingleRisk").style.display='';
                //                document.getElementById("main").style.display='none';
                addHighRisk();
                addMediumRisk();
                addLowRisk();

            }
            function saveKpiImg(){
                var  divId=parent.document.getElementById("divId").value;
                parent.document.getElementById(divId).innerHTML ="";
                parent.document.getElementById(divId).innerHTML = document.getElementById("kpigrpphdisp").innerHTML;
                parent.cancelRepKpiGraph();
                // parent.document.getElementById('divId').innerHTML =

            }

        </script>
    </body>
</html>

