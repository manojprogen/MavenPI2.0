<%@page import="com.progen.scenariodesigner.db.ScenarioTemplateDAO"%>

<%
//String foldersIds=request.getParameter("foldersIds");
//////////////////////////////////////////.println.println(" foldersIds in m js p "+foldersIds);
%>

<%@page import="java.util.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.progen.reportview.db.PbReportViewerDAO" %>
<%@page import="prg.db.PbReturnObject,prg.db.Container" %>
<%
            String MeasureRegion = "";
            StringBuffer prevColumns = new StringBuffer("");
            String PrevMsrStr = null;
            HashMap map = new HashMap();

            ArrayList MeasureIds = null;
            ArrayList MeasureNames = null;
            HashMap TableHashMap = null;
            Container container = null;
            //container = new Container();
            String scenarioId = "";
            String scenarioName = "";
            HashMap timeDetailsHM = new HashMap();
            ArrayList timeDetailsAL = new ArrayList();
            String historicalStartMonth = "";
            String historicalEndMonth = "";
            String scenarioStartMonth = "";
            String scenarioEndMonth = "";
            scenarioName = (String) request.getAttribute("scenarioName");
            historicalStartMonth = (String) request.getAttribute("historicalStartMonth");
            ////.println("historicalStartMonth---" + historicalStartMonth);
            historicalEndMonth = (String) request.getAttribute("historicalEndMonth");
            scenarioStartMonth = (String) request.getAttribute("scenarioStartMonth");

            ////.println("scenarioStartMonth--" + scenarioStartMonth);
            scenarioEndMonth = (String) request.getAttribute("scenarioEndMonth");

            if (request.getSession(false) != null && request.getSession(false).getAttribute("SCENARIOTAB") != null) {
                //scenarioName = String.valueOf(request.getAttribute("scenarioName"));
                map = (HashMap) request.getSession(false).getAttribute("SCENARIOTAB");
                if (map.get(scenarioName) != null) {
                    container = (prg.db.Container) map.get(scenarioName);
                } else {
                    container = new prg.db.Container();
                }
            }
            ////.println("parameterhashmap in jsp is : " + container.getParametersHashMap());
            timeDetailsHM = (HashMap) container.getParametersHashMap();
            timeDetailsAL = (ArrayList) timeDetailsHM.get("TimeDetailstList");

            ////////////////////////////////////////.println.println("historicalStartMonth is:: "+historicalStartMonth);
            ////////////////////////////////////////.println.println("historicalEndMonth is:: "+historicalEndMonth);
            ////////////////////////////////////////.println.println("scenarioStartMonth is:: "+scenarioStartMonth);
            ////////////////////////////////////////.println.println("scenarioEndMonth is:: "+scenarioEndMonth);

            ScenarioTemplateDAO scnDao = new ScenarioTemplateDAO();
            PbReturnObject pbro = scnDao.getAllMonths();
            PbReturnObject pbroYear = scnDao.getAllYears();





%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Table Meaures</title>
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.tablesorter.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/tablesorter/addons/pager/jquery.tablesorter.pager.js"></script>
        <script language="JavaScript" src="<%=request.getContextPath()%>/javascript/jquery.columnfilters.js"></script>

        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.draggable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.droppable.js"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.resizable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.accordion.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.dialog.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.sortable.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/effects.explode.js"></script>


        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/chili-1.8b.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/docs.js"></script>



        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jq.css" type="text/css" media="print, projection, screen">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" type="text/css" media="print, projection, screen">
        <link href="<%=request.getContextPath()%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=request.getContextPath()%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/jquery.easing.1.3.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/Base.css" rel="stylesheet" />

        <link href="<%=request.getContextPath()%>/reportDesigner.css" type="text/css" rel="stylesheet">
        <script type="text/javascript" src="<%=request.getContextPath()%>/Scenario/JS/myScripts.js"></script>

        <script>
            var y="";
            var xmlHttp;
            var ctxPath;
            var msrArray=new Array();

            var prevColsStr="<%=PrevMsrStr%>"
            var prevCols=prevColsStr.split(",");

            for(var k=0;k<prevCols.length;k++){
                var pr=msrArray.toString();
                if(pr.match(prevCols[k])==null){
                    msrArray.push(prevCols[k]);
                }
            }

            function saveTimeRange(){                
                var path=document.getElementById('path').value;
                var scenarioName=document.getElementById('scenarioName').value;
                var histStartMonth=document.getElementById('histStartMonth').value;
                var histEndMonth=document.getElementById('histEndMonth').value;
                var scenarioStartMonth=document.getElementById('scenarioStartMonth').value;
                var scenarioEndMonth=document.getElementById('scenarioEndMonth').value;

                $.ajax({
                    url: path+'/ScenarioTemplateAction.do?scnTemplateParam=saveScenarioTimeRange&scenarioName='+scenarioName+'&histStartMonth='+histStartMonth+'&histEndMonth='+histEndMonth+'&scenarioStartMonth='+scenarioStartMonth+'&scenarioEndMonth='+scenarioEndMonth,
                    success: function(data) {                        
                        if(data!=""){
                            var splitedData = data.split("\n");
                            parent.document.getElementById("historicalStartMonth").value = splitedData[0];
                            parent.document.getElementById("historicalEndMonth").value = splitedData[1];
                            parent.document.getElementById("scenarioStartMonth").value = splitedData[2];
                            parent.document.getElementById("scenarioEndMonth").value = splitedData[3];
                            cancelTime();
                        }
                    }
                });
            }
            function saveTimeRangeForYear(){
                var path=document.getElementById('path').value;
                var scenarioName=document.getElementById('scenarioName').value;
                var histStartYear=document.getElementById('histStartYear').value;
                var histEndYear=document.getElementById('histEndYear').value;
                var scenarioStartYear=document.getElementById('scenarioStartYear').value;
                var scenarioEndYear=document.getElementById('scenarioEndYear').value;

                $.ajax({
                    url: path+'/ScenarioTemplateAction.do?scnTemplateParam=saveScenarioTimeRangeForYear&scenarioName='+scenarioName+'&histStartYear='+histStartYear+'&histEndYear='+histEndYear+'&scenarioStartYear='+scenarioStartYear+'&scenarioEndYear='+scenarioEndYear,
                    success: function(data) {
                        if(data!=""){
                            var splitedData = data.split("\n");
                            parent.document.getElementById("historicalStartMonth").value = splitedData[0];
                            parent.document.getElementById("historicalEndMonth").value = splitedData[1];
                            parent.document.getElementById("scenarioStartMonth").value = splitedData[2];
                            parent.document.getElementById("scenarioEndMonth").value = splitedData[3];
                            cancelTime();
                        }
                    }
                });
            }
            
            function dispMeasures(){
                var msrs="";
                var path=document.getElementById('path').value;
                var msrUl=document.getElementById("sortable");
                var msrIds=msrUl.getElementsByTagName("li");
                var scenarioName=document.getElementById('scenarioName').value;
               // alert('scenarioName '+scenarioName);
                for(var i=0;i<msrIds.length;i++){
                    var measureIds=(msrIds[i].id).replace("Msr","");
                    msrs=msrs+","+measureIds;

                }

                if(msrIds.length!=0){
                    msrs=msrs.substring(1);

                    parent.document.getElementById("MsrIds").value=msrs;
                    parent.document.getElementById("Measures").value=msrs;
                  //  alert(' in asve ')
                    $.ajax({
                        url: path+'/ScenarioViewerAction.do?scenarioParam=buildScenarioTable&buildTableChange=Measures&Msrs='+msrs+'&MsrsNames='+msrs+'&scenarioName='+scenarioName,
                        success: function(data) {
                            if(data!=""){                                
                            }
                        }
                    });
                    parent.cancelTabMeasure();
                }
                else{
                    alert("Please Select Measures")
                }
            }
            function cancelTime(){
                parent.cancelTimeRange();
            }


            $(document).ready(function() {
                $("#measureTree").treeview({
                    animated: "normal",
                    unique:true
                });
            });

            $(function() {
                var dragMeasure=$('#measures > li > ul > li > span')
                var dropMeasures=$('#draggableMeasures');

                $(dragMeasure).draggable({
                    helper:"clone",
                    effect:["", "fade"]
                });

                $(dropMeasures).droppable(
                {
                    activeClass:"blueBorder",
                    accept:'#measures > li > ul > li > span',
                    drop: function(ev, ui) {
                        var measure=ui.draggable.html();
                        createMeasures(ui.draggable.html(),ui.draggable.attr('id'));

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

        </script>

        <style>
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
    </head>
    <body>
        <center>
            <%-- <body onload="javascript:prevMeasures();">--%>
            <%
            String MeasuresData = "";
            String path = request.getContextPath();
            if (request.getAttribute("Measures") != null) {
                MeasuresData = String.valueOf(request.getAttribute("Measures"));
            }
            String monName = "";
            String yearName = "";

            %>
            <form name="myForm2" method="post">
                <INPUT TYPE="hidden" name="path" id="path" value="<%=path%>">
                <INPUT TYPE="hidden" name="scenarioName" id="scenarioName" value="<%=scenarioName%>">
                <br>
                <%
            if (timeDetailsAL.get(0).toString().equalsIgnoreCase("Month")) {
                %>
                <Table style="width:70%" border="0" align="center">
                    <Tr>
                        <td><span style="color:red">*</span>Historical Start Month</td>
                        <Td>
                            <select id="histStartMonth" name="histStartMonth" style="width:152px">
                                <option value="">--Select--</option>
                                <%
                for (int p = 0; p < pbro.getRowCount(); p++) {
                    monName = pbro.getFieldValueString(p, "MON_NAME");
                    if (historicalStartMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
                        } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                    }

                }
                                %>
                            </select>
                        </Td>
                    </Tr>
                    <tr>
                        <td><span style="color:red">*</span>Historical End Month</td>
                        <td>
                            <select id="histEndMonth" name="histEndMonth" style="width:152px" onchange="getScenarioMonths()">
                                <option value="">--Select--</option>
                                <%
                for (int p = 0; p < pbro.getRowCount(); p++) {
                    monName = pbro.getFieldValueString(p, "MON_NAME");
                    if (historicalEndMonth.equalsIgnoreCase(monName)) {
                                %>
                                <option selected value="<%=monName%>"><%=monName%></option>
                                <%
                        } else {
                                %>
                                <option value="<%=monName%>"><%=monName%></option>
                                <%
                    }
                }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Scenario Start Month</td>
                        <td>
                            <select name="scenarioStartMonth" id="scenarioStartMonth" style="width:152px">
                                <option value="">--Select--</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Scenario End Month</td>
                        <td>
                            <select id="scenarioEndMonth" name="scenarioEndMonth" style="width:152px">
                                <option value="">--Select--</option>
                            </select>
                        </td>
                    </tr>

                </Table>

                            <br>
                <table>
                    <tr>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTimeRange()"></td>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTime()"></td>
                    </tr>
                </table>

                <%} else if (timeDetailsAL.get(0).toString().equalsIgnoreCase("Day")&&timeDetailsAL.get(1).toString().equalsIgnoreCase("PRG_YEAR_RANGE")) {%>
                <Table style="width:70%" border="0" align="center">
                    <Tr>
                        <td><span style="color:red">*</span>Historical Start Year</td>
                        <Td>
                            <select id="histStartYear" name="histStartYear" style="width:152px">
                                <option value="">--Select--</option>
                                <%
                for (int p = 0; p < pbroYear.getRowCount(); p++) {
                    yearName = pbroYear.getFieldValueString(p, "CWYEAR");
                    ////.println("historicalStartMonth--in if-" + historicalStartMonth);
                    if (historicalStartMonth.equalsIgnoreCase(yearName)) {
                                %>
                                <option selected value="<%=yearName%>"><%=yearName%></option>
                                <%
                        } else {
                                %>
                                <option value="<%=yearName%>"><%=yearName%></option>
                                <%
                    }

                }
                                %>
                            </select>
                        </Td>
                    </Tr>
                    <tr>
                        <td><span style="color:red">*</span>Historical End Year</td>
                        <td>
                            <select id="histEndYear" name="histEndYear" style="width:152px" onchange="getScenarioYears()">
                                <option value="">--Select--</option>
                                <%
                for (int p = 0; p < pbroYear.getRowCount(); p++) {
                    yearName = pbroYear.getFieldValueString(p, "CWYEAR");
                    if (historicalEndMonth.equalsIgnoreCase(yearName)) {
                                %>
                                <option selected value="<%=yearName%>"><%=yearName%></option>
                                <%
                        } else {
                                %>
                                <option value="<%=yearName%>"><%=yearName%></option>
                                <%
                    }
                }
                                %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Scenario Start Year</td>
                        <td>
                            <select name="scenarioStartYear" id="scenarioStartYear" style="width:152px">
                                <option value="">--Select--</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Scenario End Year</td>
                        <td>
                            <select id="scenarioEndYear" name="scenarioEndYear" style="width:152px">
                                <option value="">--Select--</option>
                            </select>
                        </td>
                    </tr>

                </Table>
                            <br>
                <table>
                    <tr>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Save" onclick="saveTimeRangeForYear()"></td>
                        <td><input type="button" class="navtitle-hover" style="width:auto" value="Cancel" onclick="cancelTime()"></td>
                    </tr>
                </table>
                <%}%>
            </form>
            

        </center>
    </body>
</html>