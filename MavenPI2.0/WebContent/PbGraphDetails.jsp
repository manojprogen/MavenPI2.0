<%@page import="com.progen.charts.GraphProperty,java.util.HashMap,prg.db.Container"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String grpId = request.getParameter("graphId");
            String graphIds = request.getParameter("grpIds");
            String ReportId = request.getParameter("REPORTID");
            String graphTableMethod = request.getParameter("graphTableMethod");
            String forwardFlag = "";
            if (graphTableMethod != null) {
                forwardFlag = "GTM";
            } else {
                forwardFlag = "GM";
            }
            String[] displayGraphRows = {"10", "5", "15", "25", "50", "All"};

            HashMap GraphHashMap = null;
            String grpTitle = "";
            String grplegend = "Y";
            String grplegendloc = "Bottom";
            String measureNamePosition = "";
            String grpshox = "Y";
            String grpshoy = "Y";
            String grplyaxislabel = "Left";
            String grpryaxislabel = "Right";
            String grpbDomainaxislabel = "Bottom";
            String grpdrill = "Y";
            String grpbcolor = "";
            String grpfcolor = "";
            String grpdata = "Y";
            String graphTypeName = "";
            Container container = null;
            String graphDisplayRows = "";
            String startindex = "0";
            String endindex = "10";
            boolean graphLabels=false;
            HashMap map = new HashMap();
            String from =request.getParameter("from");
            boolean showExtraTabs = true;
            HashMap singleGraphMap=null;
            if (session.getAttribute("PROGENTABLES") != null) {
                map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");

                if (map.get(ReportId) != null) {
                    container = (prg.db.Container) map.get(ReportId);
                } else {
                    container = new prg.db.Container();
                }
                GraphHashMap = container.getGraphHashMap();
                if(from!=null && from.equalsIgnoreCase("dashboard"))
                    singleGraphMap = (HashMap)container.getNewDbrdGraph().get(grpId);
                else
                    singleGraphMap = (HashMap) GraphHashMap.get(grpId);
                //.println("singleGraphMap is : "+singleGraphMap);
                if (singleGraphMap != null) {
                    grpTitle = String.valueOf(singleGraphMap.get("graphName"));
                    grplegend = String.valueOf(singleGraphMap.get("graphLegend"));
                    grplegendloc = String.valueOf(singleGraphMap.get("graphLegendLoc"));
                    grpshox = String.valueOf(singleGraphMap.get("graphshowX"));
                    grpshoy = String.valueOf(singleGraphMap.get("graphshowY"));
                    grplyaxislabel = String.valueOf(singleGraphMap.get("graphLYaxislabel"));
                    grpryaxislabel = String.valueOf(singleGraphMap.get("graphRYaxislabel"));
                    grpbDomainaxislabel = String.valueOf(singleGraphMap.get("grpbDomainaxislabel"));
                    grpdrill = String.valueOf(singleGraphMap.get("graphDrill"));
                    grpbcolor = String.valueOf(singleGraphMap.get("graphBcolor"));
                    grpfcolor = String.valueOf(singleGraphMap.get("graphFcolor"));
                    grpdata = String.valueOf(singleGraphMap.get("graphData"));
                    graphTypeName = String.valueOf(singleGraphMap.get("graphTypeName"));
                    //graphDisplayRows = String.valueOf(singleGraphMap.get("graphDisplayRows"));
                    graphDisplayRows = singleGraphMap.get("graphDisplayRows") == null ? "" : singleGraphMap.get("graphDisplayRows").toString();
                    //measNamePosition = String.valueOf(singleGraphMap.get("measureNamePosition"));
                    startindex = singleGraphMap.get("graphDisplayRows") == null ? singleGraphMap.get("startindex").toString() : "0";
                    endindex = singleGraphMap.get("graphDisplayRows") == null ? singleGraphMap.get("endindex").toString() : graphDisplayRows;
                    graphLabels = singleGraphMap.get("GraphProperty") == null ? false : ((GraphProperty)singleGraphMap.get("GraphProperty")).isLabelsDisplayed();
   ////.println("GraphDisplayRows in jsp is : "+graphDisplayRows);
            } else {
                }



            } else {
                grpTitle = "";
            }
            String contextPath=request.getContextPath();
%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>Graph Details</title>
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/PbgraphDetailsCSS.css" type="text/css" media="screen">
        <link rel="stylesheet" src="<%=contextPath%>/jQuery/jquery/ui.colorpicker.css" type="text/css" media="screen">
        <link type="text/css" href="<%=contextPath%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=contextPath%>/stylesheets/demo-colorpicker.css" type="text/css">

        <script type="text/javascript" src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/jQuery/jquery/ui/ui.core.js"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/PbgraphDetailsJS.js"></script>

        <script type="text/javascript" src="<%=contextPath%>/javascript/jquery-packer.htm">/* jquery core */</script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/sgbeal-colorpicker.js">/* colorpicker code */</script>

        <style type="text/css">
            .label{
                font-family:verdana;
                font-size:12px;
                font-weight:normal;
            }
        </style>
       
    </head>
    <body id="body">
        <div id="demo" class="demo">
            <form action='' name="grpForm" method="post">
                <input type="hidden" name="graphTypeName" id="graphTypeName" value="<%=graphTypeName%>">
                <input type="hidden" name="forwardFlag" id="forwardFlag" value="<%=forwardFlag%>">
                <input type="hidden" name="startindex" id="startindex" value="<%=startindex%>">
                <input type="hidden" name="endindex" id="endindex" value="<%=endindex%>">

                <%--<table>
                    <tr>
                        <td>Graph Title</td>
                        <td><input type="text" id="grpTitle" maxlength="50"  name="grpTitle" style="width:90%;font-family:verdana" value="<%=grpTitle%>"></td>
                    </tr>
                    <tr>
                        <td>Show Legend</td>
                        <td>
                            <%if (grplegend.equalsIgnoreCase("Y")) {%>
                            <input type="checkbox"  id="showLegend" checked  value="Y"  onclick="checkBox1(this.checked)">
                            <%} else {%>
                            <input type="checkbox"  id="showLegend"   value="N"  onclick="checkBox1(this.checked)">
                            <%}%>
                        </td>
                    </tr>
                    <tr>
                        <td>Legend Location</td>
                        <td>
                            <%if (grplegendloc.equalsIgnoreCase("Bottom")) {%>
                             <input type="checkbox"  id="showLegend"  value="Bottom"  onclick="checkBox1(this.checked)">
                            <%}else{%>
                             <input type="checkbox"  id="showLegend"  value="Top"  onclick="checkBox1(this.checked)">
                            <%}%>
                        </td>
                    </tr>
                </table>--%>


                <div id="leftcol" class='leftcol'>
                    <table width="100%" border="0">
                        <tr>
                            <td colspan="2">
                                <table width="100%">
                                    <tr>
                                        <td width="30%">
                                            <label class="label" >Graph Title</label>
                                        </td>
                                        <td width="70%">
                                            <input type="text" id="grpTitle" maxlength="50"  name="grpTitle" style="width:90%;font-family:verdana" value="<%=grpTitle%>">
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- showlegend-->
                        <tr>
                            <td colspan="2">
                                <table width="100%">
                                    <tr>
                                        <td width="30%">
                                            <label class="label">Show Legend</label>
                                        </td>
                                        <td width="70%">
                                            <%if (grplegend.equalsIgnoreCase("Y")) {%>
                                            <script type="text/javascript">
                                                $(function(){
                                                    checkBox1(true);
                                                });
                                            </script>
                                            <label id="ShowLegend" class="checked">
                                                <input type="checkbox"  id="showLegend" checked  value=""  onclick="checkBox1(this.checked)"><span id="shwlgds">No</span>
                                            </label>
                                            <%} else {%>
                                            <label id="ShowLegend" class="unchecked">
                                                <input type="checkbox"  id="showLegend"  value=""  onclick="checkBox1(this.checked)"><span id="shwlgds">No</span>
                                            </label>
                                            <%}%>

                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <!-- legend Location-->
                        <tr>
                            <td colspan="2">
                                <table width="100%" >
                                    <tr>
                                        <td width="30%">
                                            <label class="label">Legend Location</label>
                                        </td>
                                        <%if (grplegendloc.equalsIgnoreCase("Bottom")) {%>
                                    <script type="text/javascript">
                                        $(function(){
                                            checkBot(true);
                                            checkRyt(false);
                                        });
                                    </script>

                                    <td width="35%"><label id="radBot" class="selected"><input type="radio"  checked id="r1" name="Location" onclick="checkBot(this.checked)" value="Bottom" ><font size="2px">Bottom</font></label></td>
                                    <td width="35%"><label id="radRyt" class="selected"><input type="radio" id="r2"   name="Location" onclick="checkRyt(this.checked)" value="Right"><font size="2px">Right</font></label></td>
                                            <%} else {%>
                                    <script type="text/javascript">
                                        $(function(){
                                            checkBot(false);
                                            checkRyt(true);
                                        });
                                    </script>
                                    <td width="25%"><label id="radBot" class="selected"><input type="radio"  id="r1" name="Location" onclick="checkBot(this.checked)" value="Bottom" ><font size="2px">Bottom</font></label></td>
                                    <td width="25%"><label id="radRyt" class="selected"><input type="radio" id="r2" checked   name="Location" onclick="checkRyt(this.checked)" value="Right"><font size="2px">Right</font></label></td>
                                            <%}%>
                        </tr>
                    </table>
                    </td>
                    </tr>
                    <!-- showGridX-->
                    <%--<tr>
                        <td colspan="2">
                            <table width="100%">
                                <tr>
                                    <td width="30%">
                                        <label class="label" >Show Gridlines X-axis</label>
                                    </td>
                                    <td width="70%" >

                                        <%if (grpshox.equalsIgnoreCase("Y")) {%>
                                        <label id="shGrd" class="checked">
                                            <script type="text/javascript">
                                                $(function(){
                                                    checkBox2(true);
                                                });
                                            </script>
                                            <input type="checkbox"   id="showX" value="" checked onclick="checkBox2(this.checked)"><span id="shwGridX">No</span>
                                        </label>
                                        <%} else {%>
                                        <label id="shGrd" class="unchecked">
                                            <input type="checkbox"  id="showX" value="" onclick="checkBox2(this.checked)"><span id="shwGridX">No</span>
                                        </label>
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>

                    <!-- showGridY-->
                    <tr>
                        <td colspan="2">
                            <table width="100%"><tr>
                                    <td width="30%">
                                        <label class="label" >Show Gridlines Y-axis</label>
                                    </td>
                                    <td width="70%" >

                                        <%if (grpshox.equalsIgnoreCase("Y")) {%>
                                        <script type="text/javascript">
                                            $(function(){
                                                checkBox3(true);
                                            });
                                        </script>
                                        <label id="shGrdY" class="checked">
                                            <input type="checkbox"  id="showY" checked onclick="checkBox3(this.checked)" ><span id="shwGridY">No</span>
                                        </label>
                                        <%} else {%>
                                        <label id="shGrdY" class="unchecked">
                                            <input type="checkbox"  id="showY" onclick="checkBox3(this.checked)" ><span id="shwGridY">No</span>
                                        </label>
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>--%>
                    <!-- Y-axis Label-->
                    <tr>
                        <td colspan="2">
                            <table width="100%">
                                <%if (graphTypeName.equalsIgnoreCase("Dual Axis")) {%>
                                <tr>
                                    <td width="30%">
                                        <label class="label">Left Y-axis Label</label>
                                    </td>
                                    <td width="70%" ><input type="text" maxlength="20" value="<%=grplyaxislabel%>"  id ="lyaxisLabel" style="width:90%;font-family:ComicSansMS;"></td>
                                </tr>
                                <tr>
                                    <td width="30%">
                                        <label class="label">Right Y-axis Label</label>
                                    </td>
                                    <td width="70%" ><input type="text" maxlength="20" value="<%=grpryaxislabel%>"  id ="ryaxisLabel" style="width:90%;font-family:ComicSansMS;"></td>
                                </tr>

                                <%} else {%>
                                <tr>
                                    <td width="30%">
                                        <label class="label">Y-axis Label</label>
                                    </td>
                                    <td width="70%" ><input type="text" maxlength="20" value="<%=grplyaxislabel%>"  id ="lyaxisLabel" style="width:90%;font-family:ComicSansMS;"></td>
                                </tr>
                                <%}%>
                        <Tr>
                        <Td width="30%">
                             <label class="label">Display Rows</label>
                        </Td>
                        <Td>
                            <select name="GraphDisplayRows" id="GraphDisplayRows"  style="width:150px">
                                <%
                        for (int i = 0; i < displayGraphRows.length; i++) {

//.println("in for of graph display rows are : "+graphDisplayRows);%>
                         <% if (graphDisplayRows.equalsIgnoreCase(displayGraphRows[i])) {%>
                                <option selected value="<%=displayGraphRows[i]%>"><%=displayGraphRows[i]%></option>
                                <%} else {%>
                                <option value='<%=displayGraphRows[i]%>'><%=displayGraphRows[i]%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                            </table>
                        </td>
                    </Tr>
                    </tr>
                    <tr>
                         <Td width="30%">
                            <label class="label"> Show Labels</label>
                         </Td>
                        <Td width="70%">
                            <%if (graphLabels) {%>
                            <input type="checkbox"  checked name="showLabels" id="showLabels" value="true" onclick="showLabelsFun();">
                            <%} else {%>
                            <input type="checkbox" name="showLabels" id="showLabels" value="false" onclick="showLabelsFun();">
                            <%}%>
                        </Td>
                    </tr>
                    <!--  DrillDownt-->
                    <%--<tr>
                        <td colspan="2">
                            <table width="100%"><tr>
                                    <td width="30%">
                                        <label class="label">Allow DrillDown</label>
                                    </td>                                     
                                    <td width="70%" >
                                        <%if (grpdrill.equalsIgnoreCase("Y")) {%>
                                        <script type="text/javascript">
                                            $(function(){
                                                checkBox4(true);
                                            });
                                        </script>
                                        <label id="allDrl" class="checked">
                                            <input type="checkbox" checked value="" id="Drill"  onclick="checkBox4(this.checked)"><span id="alwDrill">No</span>
                                        </label>
                                        <%} else {%>
                                        <label id="allDrl" class="unchecked">
                                            <input type="checkbox" value="" id="Drill"  onclick="checkBox4(this.checked)"><span id="alwDrill">No</span>
                                        </label>
                                        <%}%>                                        
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>--%>
                    <%--<tr>
                        <td colspan="2">
                            <table width="100%"><tr>
                                    <td width="30%">
                                        <label class="label">Background Color</label>
                                    </td>
                                    <td width="70%" >
                                        <div id="brgdcolor">
                                            <span style="background-color: rgb(255, 255, 255);" class="ColorBlotch" onclick="setbColor('fff')" id="bcolor" value="fff" >#fff</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 0, 0);" class="ColorBlotch" onclick="setbColor('000')"  id="bcolor" value="000" >#000</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(255, 0, 0);" class="ColorBlotch"id="bcolor" onclick="setbColor('f00')"  value="f00">#f00</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 255, 0);" class="ColorBlotch" id="bcolor" onclick="setbColor('0f0')"   value="0f0">#0f0</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 0, 255);" class="ColorBlotch" onclick="setbColor('00f')" id="bcolor" value="00f">#00f</span></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>--%>
                    <!-- Font Color-->
                    <%--<tr>
                        <td colspan="2">
                            <table width="100%"><tr>
                                    <td width="30%">
                                        <label class="label">Font Color</label>
                                    </td>
                                    <td width="70%" >
                                        <div id="frgdcolor">
                                            <span style="background-color: rgb(255, 255, 255);" class="ColorBlotch" onclick="setColor('fff')" id="fcolor" value="fff" >#fff</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 0, 0);" class="ColorBlotch" onclick="setColor('000')"  id="fcolor" value="000" >#000</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(255, 0, 0);" class="ColorBlotch"id="fcolor" onclick="setColor('f00')"  value="f00">#f00</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 255, 0);" class="ColorBlotch" id="fcolor" onclick="setColor('0f0')"   value="0f0">#0f0</span>
                                            &nbsp;-&nbsp;
                                            <span style="background-color: rgb(0, 0, 255);" class="ColorBlotch" onclick="setColor('00f')" id="fcolor" value="00f">#00f</span></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <!--Show Data Values-->
                    <tr>
                        <td colspan="2">
                            <table width="100%"><tr>
                                    <td width="30%">
                                        <label class="label">Show Data Values </label>
                                    </td>
                                    <td width="70%" >

                                        <%if (grpdata.equalsIgnoreCase("Y")) {%>
                                        <script type="text/javascript">
                                            $(function(){
                                                checkBox5(true);
                                            });
                                        </script>
                                        <label id="shwDV" class="checked">
                                            <input type="checkbox" checked id="Data" onclick="checkBox5(this.checked)"><span id="shwData">No</span>
                                        </label>
                                        <%} else {%>
                                        <label id="shwDV" class="unchecked">
                                            <input type="checkbox" id="Data" onclick="checkBox5(this.checked)"><span id="shwData">No</span>
                                        </label>
                                        <%}%>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>--%>
                    <%if(from!=null && from.equalsIgnoreCase("dashboard")){%>
                      <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="ui-state-default ui-corner-all" id="save" value="Done" onclick="saveDbrdGraphDetails('<%=ReportId%>','<%=grpId%>')" >
                        </td>
                    </tr>
                    <%}else{%>
                    <tr>
                        <td colspan="2" align="center">
                            <input type="button" class="ui-state-default ui-corner-all" id="save" value="Done" onclick="saveGraphDetails('<%=grpId%>','<%=graphIds%>')" >
                        </td>
                    </tr>
                    <%}%>
                </table>
                </div>
            </form>

        </div>

 <script type="text/javascript">
            function showLabelsFun(){
                var showLabelsObj=document.getElementById("showLabels");
                if(showLabelsObj.checked){
                    showLabelsObj.value="true"
                }else{
                    showLabelsObj.value="false";
                }
                document.getElementById("showLabels").value=showLabelsObj.value;
            }
            function saveDbrdGraphDetails(dashboardId,graphId)
            {
                document.forms.grpForm.action="<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=updateGraphProperties&dashboardId="+dashboardId+"&graphId="+graphId+"&from=designer"
                document.forms.grpForm.submit();
               // parent.refreshGraph(dashboardId,graphId);
                parent.$("#graphPropertiesDiv").dialog('close');
            }
        </script>

    </body>
</html>