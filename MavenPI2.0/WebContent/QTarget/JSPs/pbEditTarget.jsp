<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@ page import="utils.db.*"%>
<%@ page import="java.util.ArrayList"%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
   <head>
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
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/style.css" />
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />

        




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
           
        </script>


    </head>
    <body>


<%

        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        //String targetId = request.getParameter("chk1");
        String targetId = (String)session.getAttribute("targetId");
        ////////////////////////.println("targetId in updateTarget page is:-= "+targetId);

        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject targetMaster = targetClient.getTargetMaster(targetSession);

        int count = targetMaster.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("count is: "+count);

        String targetName = targetMaster.getFieldValueString(0,"TARGET_NAME");
        String targetStartDate = targetMaster.getFieldValueString(0,"ST_DATE");
        String targetEndDate = targetMaster.getFieldValueString(0,"END_DATE");
        String minTimeLevel = targetMaster.getFieldValueString(0,"MIN_TIME_LEVEL");

        ////////////////////////////////////////////////////////////////////////.println("targetName in updateTarget page is: "+targetName);
        ////////////////////////////////////////////////////////////////////////.println("targetStartDate is: "+targetStartDate);
        ////////////////////////////////////////////////////////////////////////.println("targetEndDate is: "+targetEndDate);


        PbReturnObject getMonths = targetClient.getAllMonths(targetSession);
        int numOfMonths = getMonths.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfMonths are:: "+numOfMonths);

        PbReturnObject getQtrs = targetClient.getAllQtrs(targetSession);
        int numOfQtrs = getQtrs.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are:: "+numOfQtrs);

        PbReturnObject getYears = targetClient.getAllYears(targetSession);
        int numOfYears = getYears.getRowCount();
        ////////////////////////.println("numOfYears is:::-= "+numOfYears);

        session.setAttribute("TID",targetId);
        session.setAttribute("MTLevel",minTimeLevel);
        String path=request.getContextPath();
%>
            <br><br>
            <center>
                <form name="myForm" method="post">
                    <table>
                        <tr>
                            <td class="myhead"><span style="color:red">*</span>Target Name</td>
                            <td>
                                <input type="text" style="width:170px" readonly id="targetName" name="targetName" value="<%=targetName%>">
                            </td>
                        </tr>
                        <input type="hidden" name="timeLevels" id="timeLevels" value="<%=minTimeLevel%>"
                 <input type="hidden" name="path" id="path" value="<%=path%>">
            <%
                if(minTimeLevel.equalsIgnoreCase("Day"))
                {
            %>
                        <tr>
                            <td  class="myhead"><span style="color:red">*</span>Target Start Date</td>
                            <td>
                                <input type="text" size="8" style="width:170px" value="<%=targetStartDate%>" id="targetStartDate" name="targetStartDate">
                            </td>
                        </tr>
                        <tr>
                            <td  class="myhead"><span style="color:red">*</span>Target End Date</td>
                            <td>
                                <input type="text" size="8" style="width:170px" value="<%=targetEndDate%>" id="targetEndDate" name="targetEndDate">
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
                                <select id="targetStartMonth" name="targetStartMonth" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str = null;
                                for(int m=0;m<numOfMonths;m++)
                                {
                                    str = getMonths.getFieldValueString(m,"VIEW_BY");
                                    if(str.equalsIgnoreCase(targetStartDate))
                                    {
            %>
                                    <option selected value="<%=str%>"><%=str%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str%>"><%=str%></option>
            <%
                                    }
                                }

            %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="myhead"><span style="color:red">*</span>Target End Month</td>
                            <td>
                                <select id="targetEndMonth" name="targetEndMonth" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str2 = null;
                                for(int m=0;m<numOfMonths;m++)
                                {
                                    str2 = getMonths.getFieldValueString(m,"VIEW_BY");
                                    if(str2.equalsIgnoreCase(targetEndDate))
                                    {
            %>
                                    <option selected value="<%=str2%>"><%=str2%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str2%>"><%=str2%></option>
            <%
                                    }
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
                                <select id="targetStartQtr" name="targetStartQtr" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str3 = null;
                                for(int n=0;n<numOfQtrs;n++)
                                {
                                    str3 = getQtrs.getFieldValueString(n,"VIEW_BY");
                                    if(str3.equalsIgnoreCase(targetStartDate))
                                    {
            %>
                                    <option selected value="<%=str3%>"><%=str3%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str3%>"><%=str3%></option>
            <%
                                    }
                                }
            %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="myhead"><span style="color:red">*</span>Target End Quarter</td>
                            <td>
                                <select id="targetEndQtr" name="targetEndQtr" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str4 = null;
                                for(int n=0;n<numOfQtrs;n++)
                                {
                                    str4 = getQtrs.getFieldValueString(n,"VIEW_BY");
                                    if(str4.equalsIgnoreCase(targetEndDate))
                                    {
            %>
                                    <option selected value="<%=str4%>"><%=str4%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str4%>"><%=str4%></option>
            <%
                                    }
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
                                <select id="targetStartYear" name="targetStartYear" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str5 = null;
                                for(int p=0;p<numOfYears;p++)
                                {
                                    str5 = getYears.getFieldValueString(p,"VIEW_BY");
                                    if(str5.equalsIgnoreCase(targetStartDate))
                                    {
            %>
                                    <option selected value="<%=str5%>"><%=str5%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str5%>"><%=str5%></option>
            <%
                                    }
                                }
            %>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="myhead"><span style="color:red">*</span>Target End Year</td>
                            <td>
                                <select id="targetEndYear" name="targetEndYear" style="width:172px">
                                    <option value="">--Select--</option>
            <%
                                String str6 = null;
                                for(int p=0;p<numOfYears;p++)
                                {
                                    str6 = getYears.getFieldValueString(p,"VIEW_BY");
                                    if(str6.equalsIgnoreCase(targetEndDate))
                                    {
            %>
                                    <option selected value="<%=str6%>"><%=str6%></option>
            <%
                                    }
                                    else
                                    {
            %>
                                    <option value="<%=str6%>"><%=str6%></option>
            <%
                                    }
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
                           <%-- <td>
                                <input type="button" class="navtitle-hover"  value="Cancel" onclick="goBack2();" >
                            </td>--%>
                            <td>
                                <input type="button" class="navtitle-hover" value="Save" onclick='return updateTarget("<%=minTimeLevel%>")'>
                            </td>
                            <td>
                                <input type="reset"  class="navtitle-hover"  onclick='clearEditTarget("<%=minTimeLevel%>")'>
                            </td>
                          
                        </tr>
                    </table>
                </form>
            </center>
    </body>
</html>
