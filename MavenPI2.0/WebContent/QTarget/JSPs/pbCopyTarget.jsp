<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
         <title>pi 1.0</title>

        <script type="text/javascript" src="<%=request.getContextPath()%>/QTarget/JS/myScripts.js"></script>
        

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
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/screen.css" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        


        <style>
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
            function logout(){
                document.forms.myFormH.action="baseAction.do?param=logoutApplication";
                document.forms.myFormH.submit();
            }
            function gohome(){
                document.forms.myFormH.action="baseAction.do?param=goHome";
                document.forms.myFormH.submit();
            }
            function gouser(){
                //alert('hi')
                document.forms.myFormH.action="userList.jsp";
                document.forms.myFormH.submit();
            }
            function goTest(){
                alert("hello")

            }
        </script>

    </head>
    <body onload="document.myForm.targetName.focus();">

<%
        String userId = (String)session.getAttribute("userId");
        ////////////////////////////////////////////////////////////////////////.println("userId in pbCopyTarget is: "+userId);

        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String targetId = request.getParameter("chk1");
        ////////////////////////////////////////////////////////////////////////.println("targetId in pbCopyTarget page is: "+targetId);
        String multiplierVal = request.getParameter("val");
        ////////////////////////////////////////////////////////////////////////.println("multiplierVal is:: "+multiplierVal);
        session.setAttribute("multiplierVal",multiplierVal);


        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject getTargetDetails = targetClient.getTargetMaster(targetSession);

        int count = getTargetDetails.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("count is: "+count);

        String targetName = getTargetDetails.getFieldValueString(0,"TARGET_NAME");
        String targetDescription = getTargetDetails.getFieldValueString(0,"TARGET_DESC");
        String targetStartDate = getTargetDetails.getFieldValueString(0,"ST_DATE");
        String targetEndDate = getTargetDetails.getFieldValueString(0,"END_DATE");
        String measureId = getTargetDetails.getFieldValueString(0,"MEASURE_ID");
        ////////////////////////////////////////////////////////////////////////.println("measureId is:: "+measureId);
        //String targetType = getTargetDetails.getFieldValueString(0,"TARGET_TYPE_TIME");
        //////////////////////////////////////////////////////////////////////////.println("targetType uis:: "+targetType);
        String minTimeLevel = getTargetDetails.getFieldValueString(0,"MIN_TIME_LEVEL");
        ////////////////////////////////////////////////////////////////////////.println("minTime level is:: "+minTimeLevel);


        targetParams.setUserId(userId);
        targetSession.setObject(targetParams);
        PbReturnObject getTargetList = targetClient.getExistedTargets(targetSession);

        int number = getTargetList.getRowCount();
        String tarName = null;

        if(number!=0)
        {
            for(int i=0;i<number;i++)
            {
                if(tarName==null)
                {
                    tarName = getTargetList.getFieldValueString(i,"TARGET_NAME");
                }
                else
                {
                    tarName = tarName+","+getTargetList.getFieldValueString(i,"TARGET_NAME");
                }
            }
        }
        ////////////////////////////////////////////////////////////////////////.println("All targetNames in pbCopyTarget page is: "+tarName);


        PbReturnObject getMonths = targetClient.getAllMonths(targetSession);
        int numOfMonths = getMonths.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfMonths are:: "+numOfMonths);

        PbReturnObject getQtrs = targetClient.getAllQtrs(targetSession);
        int numOfQtrs = getQtrs.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are:: "+numOfQtrs);

        PbReturnObject getYears = targetClient.getAllYears(targetSession);
        int numOfYears = getYears.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfYears is::: "+numOfYears);


        session.setAttribute("MTL",minTimeLevel);

%>

            <form name="myForm" method="post">
                <center>
                    <br><br>
                    <font size="1px" color="black"> Fields marked <span style="color:red">*</span> are MANDATORY </font>

                        <table>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target Name</td>
                                <td>
                                    <input style="width:170px" type="text" name="targetName" id="targetName" value="<%=targetName%>" onkeyup="copyToTargetDesc()">
                                </td>
                            </tr>
                            <input type="hidden" name="targetId" value="<%=targetId%>">
                            <input type="hidden" name="measureId" value="<%=measureId%>">
                            <input type="hidden" name="timeLevel" id="timeLevel" value="<%=minTimeLevel%>">
                            <%--<input type="hidden" name="targetType" value="<%=targetType%>">--%>
                            <tr>
                                <td class="myhead">&nbsp;&nbsp;Target Description</td>
                                <td>
                                    <input style="width:170px" type="text" name="targetDescription" id="targetDescription" value="<%=targetDescription%>">
                                </td>
                            </tr>
                <%
                    if(minTimeLevel.equalsIgnoreCase("Day"))
                    {
                %>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target Start Date</td>
                                <td>
                                    <input type="text" readonly size="8" style="width:170px" value="<%=targetStartDate%>" id="targetStartDate" name="targetStartDate">
                                </td>
                            </tr>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target End Date</td>
                                <td>
                                    <input type="text" readonly size="8" style="width:170px" value="<%=targetEndDate%>" id="targetEndDate" name="targetEndDate">
                                </td>
                            </tr>
                <%
                    }
                    else if(minTimeLevel.equalsIgnoreCase("Month"))
                    {
                %>

                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target Start Month</td>
                                <td>
                                    <select id="targetStartMonth" name="targetStartMonth" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str = null;
                                    String temp = null;
                                    for(int m=0;m<numOfMonths;m++)
                                    {
                                        str = getMonths.getFieldValueString(m,"VIEW_BY");
                                        if(targetStartDate.equalsIgnoreCase(str))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str%>"><%=str%></option>
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target End Month</td>
                                <td>
                                    <select id="targetEndMonth" name="targetEndMonth" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str2 = null;
                                    for(int m=0;m<numOfMonths;m++)
                                    {
                                        str2 = getMonths.getFieldValueString(m,"VIEW_BY");
                                        if(targetEndDate.equalsIgnoreCase(str2))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str2%>"><%=str2%></option>
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                <%
                    }
                    else if(minTimeLevel.equalsIgnoreCase("Quarter"))
                    {
                %>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target Start Quarter</td>
                                <td>
                                    <select id="targetStartQtr" name="targetStartQtr" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str3 = null;
                                    String temp = null;
                                    for(int n=0;n<numOfQtrs;n++)
                                    {
                                        str3 = getQtrs.getFieldValueString(n,"VIEW_BY");
                                        if(targetStartDate.equalsIgnoreCase(str3))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str3%>"><%=str3%></option>
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target End Quarter</td>
                                <td>
                                    <select id="targetEndQtr" name="targetEndQtr" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str4 = null;
                                    for(int n=0;n<numOfQtrs;n++)
                                    {
                                        str4 = getQtrs.getFieldValueString(n,"VIEW_BY");
                                        if(targetEndDate.equalsIgnoreCase(str4))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str4%>"><%=str4%></option> 
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                <%
                    }
                    else
                    {
                %>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target Start Year</td>
                                <td>
                                    <select id="targetStartYear" name="targetStartYear" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str5 = null;
                                    String temp = null;
                                    for(int p=0;p<numOfYears;p++)
                                    {
                                        str5 = getYears.getFieldValueString(p,"VIEW_BY");
                                        if(targetStartDate.equalsIgnoreCase(str5))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str5%>"><%=str5%></option> 
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="myhead"><span style="color:red">*</span>Target End Year</td>
                                <td>
                                    <select id="targetEndYear" name="targetEndYear" style="width:173px">
                                        <option value="">--Select--</option>
                <%
                                    String str6 = null;
                                    for(int p=0;p<numOfYears;p++)
                                    {
                                        str6 = getYears.getFieldValueString(p,"VIEW_BY");
                                        if(targetEndDate.equalsIgnoreCase(str6))
                                        {
                                            temp = "selected";
                                        }
                                        else
                                        {
                                            temp = "";
                                        }
                %>
                                        <option <%=temp%> value="<%=str6%>"><%=str6%></option> 
                <%
                                    }

                %>
                                    </select>
                                </td>
                            </tr>
                <%
                    }
                %>
                        </table>
                        <br>
                        <table>
                            <tr>
                                <%--
                                <td>
                                    <input type="button"  value="Targets Home" onclick="goToTargetsHome();">
                                </td>
                                <td>
                                    <input type="button" value="Clear" onclick='clearCopyTarget("<%=minTimeLevel%>");'>
                                </td>
                                <td>
                                    <input type="button"  value="Next" onclick='return validateCopyTarget("<%=tarName%>","<%=targetStartDate%>","<%=targetEndDate%>","<%=minTimeLevel%>")'>
                                </td>
                                --%>
                                <td>
                                    <input type="button"  value="Cancel" onclick="goBack3();">
                                </td>
                                <td>
                                    <input type="button"  value="Save" onclick='return validateCopyTarget("<%=tarName%>","<%=targetStartDate%>","<%=targetEndDate%>","<%=minTimeLevel%>")'>
                                </td>
                                <td>
                                    <input type="reset"  onclick='clearCopyTarget("<%=minTimeLevel%>");'>
                                </td>
                            </tr>
                        </table>                    
                </center>
        </form>
    </body>
</html>
