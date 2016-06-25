<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%
        String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
%>
<html>
    <head>
        <title>pi 1.0</title>

        <script type="text/javascript" src="<%=request.getContextPath()%>/QTarget/JS/myScripts.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <script src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=request.getContextPath()%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/ui.tabs.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=request.getContextPath()%>/stylesheets/StyleSheet.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">

        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />



        <style>

                        .demo{
                            left:200px;
                            top:50px;
                            height:auto;
                            width:500px;
                        }
                        .leftcol {
                            clear:left;
                            float:left;
                            width:100%;
                            background-color:#e6e6e6;
                        }
                        .label{
                            background-color:#bdbdbd;
                        }

                        .fontsty{
                            -moz-border-radius-bottomleft:4px;
                            -moz-border-radius-bottomright:4px;
                            -moz-border-radius-topleft:4px;
                            -moz-border-radius-topright:4px;
                            background-color:#bdbdbd;
                        }
                        .tabsty{
                            -moz-border-radius-bottomleft:4px;
                            -moz-border-radius-bottomright:4px;
                            -moz-border-radius-topleft:4px;
                            -moz-border-radius-topright:4px;
                        }
                        a {font-family:Verdana;cursor:pointer;}


        </style>


        <script type="text/javascript">

            //uday
            $(function() {
                $('#targetStartDate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
                $('#targetEndDate').datepicker({
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    numberOfMonths: 1,
                    stepMonths: 1
                });
            });

            $(document).ready(function(){
                $test=$(".ui-state-default ");
                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#E6E6E6";
                    this.style.color="#000"
                });
            });

            function logout(){
                var path = '<%=request.getContextPath()%>';
                document.forms.myForm.action=path+"/baseAction.do?param=logoutApplication";
                document.forms.myForm.submit();
            }
            function gohome(){
                var path = '<%=request.getContextPath()%>';
                document.forms.myForm.action=path+"/baseAction.do?param=goHome";
                document.forms.myForm.submit();
            }
            function gouser(){
                //alert('hi')
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goTest()
            {
                alert("hello")
            }
            function goToSaveLink()
            {
              // alert('in go to save link ');
               var temp2=document.getElementById("temp2").value;
               var measureIds=document.getElementById("measureIds").value;
               var targetStartDates=document.getElementById("targetStartDates").value;
               var targetEndDates=document.getElementById("targetEndDates").value;
               validateTargetMaster(temp2,measureIds,targetStartDates,targetEndDates);
            }

        </script>
        <script>
            function openTimeDimFlow()
            {

                //parent.document.getElementById("targetDialog").style.display='none';
               
                var timeLevels=document.getElementById("timeLevels").value;

              //  frameobj = document.getElementById("timeflow");
             //   frameobj.src= "<%=request.getContextPath()%>pbSelectTimeFlow.jsp?timeLevels="+timeLevels,"TargetParameters";//,"scrollbars=1,width=500,height=300,address=no";
              /*  $("#timeflwdiv").dialog({
                    //bgiframe: true,
                    autoOpen: false,
                    height: 200,
                    width: 400,
                    position: 'justify',
                    modal: true
                }); */

              window.open("pbSelectTimeFlow.jsp?timeLevels="+timeLevels,"TargetParameters","scrollbars=1,width=500,height=300,address=no");
                // alert('in timeDimFlow');
            }
            function sample()
            {
                document.getElementById("startDate").style.display = 'none';
                document.getElementById("endDate").style.display = 'none';
                document.getElementById("startMonth").style.display = 'none';
                document.getElementById("endMonth").style.display = 'none';
                document.getElementById("startQtr").style.display = 'none';
                document.getElementById("endQtr").style.display = 'none';
                document.getElementById("startYear").style.display = 'none';
                document.getElementById("endYear").style.display = 'none';
                document.myForm.targetName.focus();
            }

            function GetXmlHttpObject()
            {
                var xmlHttp=null;
                try
                {
                    // Firefox, Opera 8.0+, Safari
                    xmlHttp=new XMLHttpRequest();
                }
                catch (e)
                {
                    // Internet Explorer
                    try
                    {
                        xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
                    }
                    catch (e)
                    {
                        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
                    }
                }
                return xmlHttp;
            }

            function closeTime()
            {
                // alert('in close time');
                parent.document.getElementById("targetDialog").style.display='block';
            }


        </script>
        <style>
            *{font:12px verdana;color:#000}
        </style>
    </head>
    <body onload="sample()">


        <%
        //session.removeAttribute("orphans");
        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String measureId = request.getParameter("measureId");
        ////////////////////////////////////////////.println(" measureId in jsp " + measureId);
        PbReturnObject measurObj = targetClient.getMeasureInfo(measureId);
        String measureName = measurObj.getFieldValueString(0, "MEASURE_NAME");
        String grpId = measurObj.getFieldValueString(0, "BUSINESS_GROUP");
        String grpName = measurObj.getFieldValueString(0, "GRP_NAME");

        // PbReturnObject dimensionList = (PbReturnObject)targetClient.getPrimaryDimensionList(targetSession);
        //int dimensionCount = dimensionList.getRowCount();


        targetParams.setTimeLevel("Daily");
        targetSession.setObject(targetParams);
        PbReturnObject timeMembers = (PbReturnObject) targetClient.getDurationLovs(targetSession);
        int rowCount = timeMembers.getRowCount();

        String userId = request.getParameter("userId");
        //(String)session.getAttribute("userId");

        //////////////////////////////////////////////////////////////////////////.println("userId in pbTargetMaster Page is: "+userId);
        targetParams.setUserId(userId);
        targetSession.setObject(targetParams);
        PbReturnObject getBusinessAreas = targetClient.getUserBusinessGroups(targetSession);

        int count = getBusinessAreas.getRowCount();
        String temp = null;

        /*
        //////////////////////////////////////////////////////////////////////////.println("userId in pbCheckTargetName is: "+userId);
        String targetName = request.getParameter("targetName");
        //////////////////////////////////////////////////////////////////////////.println("targetName in checkTargetName page is: "+targetName);
         */
        targetParams.setUserId(userId);
        targetSession.setObject(targetParams);
        PbReturnObject getTargetList = targetClient.getExistedTargets(targetSession);

        int number = getTargetList.getRowCount();
        String temp2 = null;
        String tarName = null;
        String measureIds = null;
        String targetStartDates = null;
        String targetEndDates = null;

        if (number != 0) {
            for (int i = 0; i < number; i++) {
                if (temp2 == null) {
                    temp2 = getTargetList.getFieldValueString(i, "TARGET_NAME");
                } else {
                    temp2 = temp2 + "," + getTargetList.getFieldValueString(i, "TARGET_NAME");
                }

                tarName = getTargetList.getFieldValueString(i, "TARGET_NAME");

                if (measureIds == null) {
                    measureIds = String.valueOf(getTargetList.getFieldValueInt(i, "MEASURE_ID"));
                } else {
                    measureIds = measureIds + "," + String.valueOf(getTargetList.getFieldValueInt(i, "MEASURE_ID"));
                }

                if (targetStartDates == null) {
                    targetStartDates = getTargetList.getFieldValueString(i, "S_DATE");
                } else {
                    targetStartDates = targetStartDates + "," + getTargetList.getFieldValueString(i, "S_DATE");
                }

                if (targetEndDates == null) {
                    targetEndDates = getTargetList.getFieldValueString(i, "E_DATE");
                } else {
                    targetEndDates = targetEndDates + "," + getTargetList.getFieldValueString(i, "E_DATE");
                }

            }


        }
        //////////////////////////////////////////////////////////////////////////.println("All target names are: "+temp2);
        //////////////////////////////////////////////////////////////////////////.println("All measureIds are: "+measureIds);
        //////////////////////////////////////////////////////////////////////////.println("All targetStartDates are: "+targetStartDates);
        //////////////////////////////////////////////////////////////////////////.println("All targetEndDates are: "+targetEndDates);

        PbReturnObject getMonths = targetClient.getAllMonths(targetSession);
        int numOfMonths = getMonths.getRowCount();
        //////////////////////////////////////////////////////////////////////////.println("numOfMonths are:: "+numOfMonths);

        PbReturnObject getQtrs = targetClient.getAllQtrs(targetSession);
        int numOfQtrs = getQtrs.getRowCount();
        //////////////////////////////////////////////////////////////////////////.println("numOfQtrs are:: "+numOfQtrs);

        PbReturnObject getYears = targetClient.getAllYears(targetSession);
        int numOfYears = getYears.getRowCount();
        //////////////////////////////////////////////////////////////////////////.println("numOfYears is::: "+numOfYears);


        %>

        <form name="myForm" method="post">
            <center>
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a style="right:auto" href="javascript:goToSaveLink()">Save</a>
                <Br/>
                <font size="1px" color="black"> Fields marked <span style="color:red">*</span> are MANDATORY </font>
                <table style="width:80%;line-height:24px">
                    <tr>
                        <td><span style="color:red">*</span>Target Name</td>
                        <td>
                            <input type="text" id="targetName" name="targetName" onkeyup="copyToTargetDesc()" style="width:170px">
                        </td>
                    </tr>

                    <input type="hidden" name="parameterIds" id="parameterIds">
                    <input type="hidden" name="parameterNames" id="parameterNames">

                    <tr>
                        <td>&nbsp;&nbsp;Target Description</td>
                        <td>
                            <input type="text" id="targetDescription" name="targetDescription"  style="width:170px">
                        </td>
                    </tr>


                    <tr>
                        <td><span style="color:red">*</span>Business Group</td>
                        <td>
                            <%-- <select name="country1" id="country1"  style="width:173px" onchange="showHint()">
                                        <option value="">--Select--</option>
                <%
                                        for(int i=0;i<count;i++)
                                        {
                                            temp = getBusinessAreas.getFieldValueString(i,"GRP_NAME");
                %>
                                            <option value="<%=temp%>"><%=temp%></option>
                <%
                                        }
                %>

                            </select> --%>
                            <Input type="TEXT" READONLY name="grpName" id="grpName"  style="width:171px" VALUE="<%=grpName%>">
                        </td>
                        <Input type="hidden" name="country1" id="country1" VALUE="<%=grpId%>">
                        <Input type="hidden" name="measure" id="measure" VALUE="<%=measureId%>">

                        <Input type="hidden" name="timeLevels" id="timeLevels" VALUE="">
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>Measure</td>
                        <td>
                            <Input type="TEXT" READONLY name="measureName"  style="width:171px" id="measureName" VALUE="<%=measureName%>">
                            <%-- <select id="measure" name="measure"  style="width:173px">
                                        <option value="">--Select--</option>
                            </select> --%>
                        </td>
                    </tr>
                    <tr>
                        <td><span style="color:red">*</span>
                            Time Dimension Details
                        </td>
                        <td><a href="javascript:openTimeDimFlow()">
                            Time Flow For Target Entry</a>
                        </td>
                    </tr>

                    <%-- <tr>
                                <td class="myhead"><span style="color:red">*</span>Time Level</td>
                                <td>
                                    <select  id="timeLevel" name="timeLevel" style="width:173px" onchange="showTargetStartAndEnd()">
                                        <option value="">--Select--</option>
                <%
                                        for(int i=0;i<rowCount;i++)
                                        {
                %>
                                            <option value="<%=timeMembers.getFieldValueString(i,"VALUE")%>"><%=timeMembers.getFieldValueString(i,"DESCRIPTION")%></option>
                <%
                                        }
                %>
                                    </select>
                                </td>
                    </tr> --%>
                    <tr id="startDate">
                        <td><span style="color:red">*</span>Target Start Date</td>
                        <td>
                            <input type="text" readonly size="8" style="width:170px"  id="targetStartDate" name="targetStartDate">
                        </td>
                    </tr>
                    <tr id="endDate">
                        <td><span style="color:red">*</span>Target End Date</td>
                        <td>
                            <input type="text" readonly size="8" style="width:170px"  id="targetEndDate" name="targetEndDate">
                        </td>
                    </tr>
                    <tr id="startMonth" style="display:none">
                        <td><span style="color:red">*</span>Target Start Month</td>
                        <td>
                            <select id="targetStartMonth" name="targetStartMonth" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str = null;
        for (int m = 0; m < numOfMonths; m++) {
            str = getMonths.getFieldValueString(m, "VIEW_BY");
                                %>
                                <option value="<%=str%>"><%=str%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <tr id="endMonth">
                        <td><span style="color:red">*</span>Target End Month</td>
                        <td>
                            <select  id="targetEndMonth" name="targetEndMonth" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str2 = null;
        for (int m = 0; m < numOfMonths; m++) {
            str2 = getMonths.getFieldValueString(m, "VIEW_BY");
                                %>
                                <option value="<%=str2%>"><%=str2%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <tr id="startQtr">
                        <td><span style="color:red">*</span>Target Start Quarter</td>
                        <td>
                            <select  id="targetStartQtr" name="targetStartQtr" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str3 = null;
        for (int n = 0; n < numOfQtrs; n++) {
            str3 = getQtrs.getFieldValueString(n, "VIEW_BY");
                                %>
                                <option value="<%=str3%>"><%=str3%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <tr id="endQtr">
                        <td><span style="color:red">*</span>Target End Quarter</td>
                        <td>
                            <select  id="targetEndQtr" name="targetEndQtr" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str4 = null;
        for (int n = 0; n < numOfQtrs; n++) {
            str4 = getQtrs.getFieldValueString(n, "VIEW_BY");
                                %>
                                <option value="<%=str4%>"><%=str4%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <tr id="startYear">
                        <td><span style="color:red">*</span>Target Start Year</td>
                        <td>
                            <select  id="targetStartYear" name="targetStartYear" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str5 = null;
        for (int p = 0; p < numOfYears; p++) {
            str5 = getYears.getFieldValueString(p, "VIEW_BY");
                                %>
                                <option value="<%=str5%>"><%=str5%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <tr id="endYear">
                        <td><span style="color:red">*</span>Target End Year</td>
                        <td>
                            <select  id="targetEndYear" name="targetEndYear" style="width:173px">
                                <option value="">--Select--</option>
                                <%
        String str6 = null;
        for (int p = 0; p < numOfYears; p++) {
            str6 = getYears.getFieldValueString(p, "VIEW_BY");
                                %>
                                <option value="<%=str6%>"><%=str6%></option>
                                <%
        }

                                %>
                            </select>
                        </td>
                    </tr>
                    <%--
                            <tr>
                                <td class="myhead" rowspan="2"><span style="color:red">*</span>Parameters used for Target</td>
                                <td>
                                   <input type="radio" id="parameters1" name="parameters" value="all" checked><font style="font-size:11px">All Business Area Parameters</font>
                                </td>
                            </tr>
                    --%>
                    <tr>
                        <td><span style="color:red">*</span>Parameters used for Target</td>
                        <td>
                            <input type="radio" id="parameters2" name="parameters" value="notAll" onclick='return setWindow()'><font style="font-size:11px">Selective Parameters</font>
                        
                        </td>
                    </tr>


                    <%--  <tr>
                                <td class="myhead"><span style="color:red">*</span>Primary Target Parameter</td>
                                <td>
                                    <select  style="width:173px" name="primaryTargetParameter" id="primaryTargetParameter" onfocus='checkParameterSection()'>
                                            <option value="">--Select--</option>
                                    </select>
                                </td>
                    </tr> --%>


                </table>
                <br>
                <table>
                    <tr>

                        <td>
                            <input type="button" class="navtitle-hover"  value="Save" onclick='return validateTargetMaster("<%=temp2%>","<%=measureIds%>","<%=targetStartDates%>","<%=targetEndDates%>")'>
                        </td>
                        <td>
                            <input type="reset" class="navtitle-hover" onclick="clearAll();" >
                        </td>
                        <td>
                            <input type="button" class="navtitle-hover"  value="Cancel" onclick="goBack();" >
                        </td>

                    </tr>
                </table>
                <%
        String path = request.getContextPath();
                %>


                <input type="hidden" name="h" id="h" value="<%=path%>">
                <input type="hidden" name="userId" id="userId" value="<%=userId%>">
                <input type="hidden" name="temp2" id="temp2" value="<%=temp2%>">
                <input type="hidden" name="measureIds" id="measureIds" value="<%=measureIds%>">
                <input type="hidden" name="targetStartDates" id="targetStartDates" value="<%=targetStartDates%>">
                <input type="hidden" name="targetEndDates" id="targetEndDates" value="<%=targetEndDates%>">


                <div  id="fade" name="fade"></div>
                <div id="timeflwdiv" name="timeflwdiv" title="TimeFlow">
                    <iframe id="timeflow" name="timeflow" frameborder="0" scrolling="no"></iframe>
                </div>

            </center>

            <br>

        </form>

    </body>
</html>