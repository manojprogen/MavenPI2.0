<%--
    Document   : pbDashboardKpi
    Created on : Oct 5, 2009, 1:39:01 PM
    Author     : mahesh.sanampudi@progenbusiness.com
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="prg.db.PbDb,prg.db.PbReturnObject,com.progen.reportdesigner.db.DashboardTemplateDAO,java.util.*,com.progen.report.query.PbReportQuery"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<%//for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                    session.setAttribute("theme", themeColor);
                } else {
                    themeColor = String.valueOf(session.getAttribute("theme"));
                }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard Kpis</title>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/treeviewstyle/screen.css" />

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
         <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=contextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/treeview/demo.js"></script>
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>-->
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <!--<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>-->
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>-->
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
<!--        <script language="JavaScript" src="<%=request.getContextPath()%>/querydesigner/JS/jquery.columnfilters.js"></script>-->


        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
<!--        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>-->
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
<!--            <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->
        <%String path = contextPath;%>
     

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
    <body onload="loadall()">
        <%
        PbReportQuery repqry = new PbReportQuery();
        String kpiIds = "";
        String kpis = "";
        kpis = request.getParameter("kpis");
        kpiIds = request.getParameter("kpiIds");       
            String perday=request.getParameter("perday");
            String timeDim=request.getParameter("timeDim");
            String needleValue = request.getParameter("kpiNeeedleValue");
            String kpitargetXml = request.getParameter("kpiTargetXml");
            String targetType=request.getParameter("targetType");
        %>
        <form name="myForm2" method="post">
            <div id="tabselect">
                <table align="center">
                    <tr>
                        <td>
                            Graph Type
                        </td>
                        <td>
                            <Select name="kpigrpType" STYLE="width:150px" class="myTextbox3" id="kpigrpType" >
                                <Option value="Meter">Meter</Option>
                                <Option value="Thermometer">Thermometer</Option>
                            </Select>
                        </td>
                    </tr>
                    <Tr>
                        <%if(timeDim.equalsIgnoreCase("PRG_DATE_RANGE")){%>
                        <Td><font size="1px"><b><%=kpis%></b> Deviation Value(in %) is : <%=needleValue%></font></Td>
                        <%}else{%>
                        <Td><font size="1px"><b><%=kpis%></b> Deviation Value(in %) is : <%=needleValue%></font></Td>
                        <%}%>
                    </Tr>
                </table>
                <br>
                <br>
                <table style="width:100%;" border="solid black " align="center" >
                    <Tr>
                        <td ><font size="1px"><b>Define Risk Ranges</b></font></td>
                        <Td><font size="1px"><b>Operator</b></font></Td>
                        <Td><font size="1px"><b>Deviation % </b></font></Td>

                    </Tr>
                    <Tr>
                        <Td >High Risk</Td>
                        <Td>
                            <Select name="highRisk" STYLE="width:100px" class="myTextbox3" id="highRisk" onchange="addHighRisk(this)">
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between" selected>between</Option>
                            </Select>
                        </Td>
                        <Td>
                            <div id="singleRisk">
                                <Input type="text"  class="myTextbox3" name="box1h" id="box1h" style="width:62px" onkeyup="populateValue()">
                            </div>
                            <div id="doubleRisk">
                                <Input type="text"  class="myTextbox3" name="box1" id="box1" style="width:62px" >
                                <Input type="text"  class="myTextbox3" name="box2" id="box2" style="width:62px" onkeyup="populateValuedh()">
                            </div>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td >Medium Risk</Td>
                        <Td>
                            <Select name="mediumRisk" STYLE="width:100px" class="myTextbox3" id="mediumRisk" onchange="addMediumRisk(this)">
                                <Option value="between" selected>between</Option>
                                <Option value=">">></Option>
                                <Option value="<"><</Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                            </Select>
                        </Td>
                        <Td>
                            <div id="mediumsingleRisk">
                                <Input type="text"  class="myTextbox3" name="mediumbox1m" id="mediumbox1m" style="width:62px;background:white" onkeyup="populateValuem()">
                            </div>
                            <div id="mediumdoubleRisk">
                                <Input type="text"  class="myTextbox3"  name="mediumbox1" id="mediumbox1" style="width:62px;background:white">
                                <Input type="text"  class="myTextbox3" name="mediumbox2" id="mediumbox2" style="width:62px" onkeyup="populateValuedm()">
                            </div>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td >Low Risk</Td>
                        <Td>
                            <Select name="lowRisk" STYLE="width:100px" class="myTextbox3" id="lowRisk" onchange="addLowRisk(this)">
                                <Option value="<"><</Option>
                                <Option value=">">></Option>
                                <Option value=">=">>=</Option>
                                <Option value="<="><=</Option>
                                <Option value="=">=</Option>
                                <Option value="between" selected>between</Option>
                            </Select>
                        </Td>
                        <Td>
                            <div id="lowsingleRisk">
                                <Input type="text"  class="myTextbox3"  name="lowbox1l" id="lowbox1l" style="width:62px;background:white " >
                            </div>
                            <div id="lowdoubleRisk">
                                <Input type="text"  class="myTextbox3"   name="lowbox1" id="lowbox1" style="width:62px;background:white" >
                                <Input type="text"  class="myTextbox3" name="lowbox2" id="lowbox2" style="width:62px" >
                            </div>
                        </Td>
                    </Tr>

                    <input type="hidden" name="kpiIds" value="<%=kpiIds%>" id="kpiIds">
                    <input type="hidden" name="kpis" value="<%=kpis%>" id="kpis">
                    <input type="hidden" name="needleValue" value="<%=needleValue%>" id="needleValue">
                    <input type="hidden" name="targetType" value="<%=targetType%>" id="targetType">
                    <%--<input type="hidden" name="kpiTgtXml" value="<%=kpitargetXml%>" id="kpiTgtXml">--%>
                    <% session.setAttribute("kpiXml", kpitargetXml);%>
                </table>
                <center>
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="showChartImageCheck('<%=kpis%>','<%=kpiIds%>','<%=needleValue%>')">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="history.go(-1)">
                </center>
            </div>
            <div id="main">
                <div id="kpigrpphdisp" align="center" >
                </div>
                <br/>
                <center>
                    <input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveKpiImg()">
                    <input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="history.go(-1)">
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
            function addHighRisk(risk)
            {
                if(risk.value=="between")
                {
                    document.getElementById('doubleRisk').style.display='';
                    document.getElementById('singleRisk').style.display='none';
                }
                else{

                    document.getElementById('doubleRisk').style.display='none';
                    document.getElementById('singleRisk').style.display='';
                }

            }
            function addMediumRisk(risk)
            {
                if(risk.value=="between")
                {
                    document.getElementById('mediumdoubleRisk').style.display='';
                    document.getElementById('mediumsingleRisk').style.display='none';
                }
                else{

                    document.getElementById('mediumdoubleRisk').style.display='none';
                    document.getElementById('mediumsingleRisk').style.display='';
                }

            }
            function addLowRisk(risk)
            {
                if(risk.value=="between")
                {
                    document.getElementById('lowdoubleRisk').style.display='';
                    document.getElementById('lowsingleRisk').style.display='none';
                }
                else{

                    document.getElementById('lowdoubleRisk').style.display='none';
                    document.getElementById('lowsingleRisk').style.display='';
                }

            }
            function showChartImageCheck(kpis,kpiIds,needlevalue){
                //alert('needlevalue in check is  '+needlevalue)
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
                    showChartImage(kpis,kpiIds,needlevalue);
                }else{
                    alert('Please enter all range values');
                }
            }


            function showChartImage(kpis,kpiIds,needleValue)
            {
                //alert('needlevalue in image is  '+needleValue)
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
                var kpigrpType=document.getElementById("kpigrpType").value;
                var divId=parent.document.getElementById("divId").value;
                var dbrdId=parent.document.getElementById("dbrdId").value;
                $.ajax({
                    url: path+'/dashboardTemplateAction.do?templateParam2=displayKpiGraph&hr1='+hr1+'&hr2='+hr2+'&mr1='+mr1+'&mr2='+mr2+'&lr1='+lr1+'&lr2='+lr2+'&kpigrpType='+kpigrpType+'&kpis='+kpis+'&divId='+divId+'&kpiIds='+kpiIds+'&dashboardId='+dbrdId+'&needleVal='+needleValue,
                    success: function(data) {
                        if(data != ""){
                            document.getElementById("tabselect").style.display='none';
                            document.getElementById("kpigrpphdisp").innerHTML ="";
                            document.getElementById("kpigrpphdisp").innerHTML = data;
                            document.getElementById("main").style.display='';
                            document.getElementById("kpigrpphdisp").style.display='block';
                        }
                    }
                });
            }

            function loadall(){
                document.getElementById("doubleRisk").style.display='';
                document.getElementById("singleRisk").style.display='none';
                document.getElementById("mediumdoubleRisk").style.display='';
                document.getElementById("mediumsingleRisk").style.display='none';
                document.getElementById("lowdoubleRisk").style.display='';
                document.getElementById("lowsingleRisk").style.display='none';
                document.getElementById("main").style.display='none';

                
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
