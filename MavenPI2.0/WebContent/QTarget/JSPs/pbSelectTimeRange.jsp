<%@page import="prg.targetparam.qdparams.PbTargetParamParams"%>
<%@page import="prg.targetparam.qdclient.PbTargetParamManager"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.PbReturnObject"%>
<%@page import="java.util.*"%>


<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>

        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/draggable/ui.core.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/demos.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />

        <Script language="javascript"  src="../JS/myScripts.js"></Script>
        <link href="../css/myStyles.css" rel="stylesheet" type="text/css">
        
        <style>
            .myTextbox5 {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-weight: normal;
                font-size: 8pt;
                color:#000000;
                padding: 0px;
                width:170px;
                margin-left: 5px;
                border-top: 1px groove #848484;
                border-right: 1px inset #999999;
                border-bottom: 1px inset #999999;
                border-left: 1px groove #848484;
                background-color:#FFFFFF;
            /*apply this class to a TextBox/Textfield only*/
            }
        </style>
        <script>
            $(function() {
                $('#SrcFromDate').datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                numberOfMonths: 1,
                stepMonths: 1
                });
                $('#SrcToDate').datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                numberOfMonths: 1,
                stepMonths: 1
                });
                $('#DesFromDate').datepicker({
                changeMonth: true,
                changeYear: true,
                showButtonPanel: true,
                numberOfMonths: 1,
                stepMonths: 1
                });
                $('#DesToDate').datepicker({
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
        //HashMap allParameters = (HashMap)session.getAttribute("allParameters");
        //String copyUrl = (String)session.getAttribute("copyUrl");
        //String copyUrlSec = (String)session.getAttribute("copyUrlSec");
        //////////////////////////////////////////////////////////////////////////.println("copyUrl ss: "+copyUrl);
        //////////////////////////////////////////////////////////////////////////.println("copyUrlSec is:: "+copyUrlSec);
        Enumeration ee = request.getParameterNames();      

        PbTargetParamParams targetParams = new PbTargetParamParams();
        PbTargetParamManager targetClient = new PbTargetParamManager();
        Session targetSession = new Session();

        String minTimeLevel = request.getParameter("minTime");
        ////////////////////////////////////////////////////////////////////////.println("minTimeLevel in selectTimeRange is:: "+minTimeLevel);
        String periodType = request.getParameter("periodType");
        ////////////////////////////////////////////////////////////////////////.println("periodType is:: "+periodType);
        String targetId = request.getParameter("targetId");
        ////////////////////////////////////////////////////////////////////////.println("targetId in select time range page is:: "+targetId);
        /*
        String targetId = String.valueOf(session.getAttribute("targetId"));
        ////////////////////////////////////////////////////////////////////////.println("targetId in select time range page is:: "+targetId);
        String measureId = String.valueOf(session.getAttribute("measureId"));
        ////////////////////////////////////////////////////////////////////////.println("measureId in select time range page is:: "+measureId);
        */
        targetParams.setTargetId(targetId);
        targetSession.setObject(targetParams);
        PbReturnObject getTargetDetails = targetClient.getTargetMaster(targetSession);

        String st_with = getTargetDetails.getFieldValueString(0,"ST_DATE");
        String end_with = getTargetDetails.getFieldValueString(0,"END_DATE");
        ////////////////////////////////////////////////////////////////////////.println("st_with is:: "+st_with);
        ////////////////////////////////////////////////////////////////////////.println("end_with is:: "+end_with);

        session.setAttribute("minLevel",minTimeLevel);
        session.setAttribute("pType",periodType);
        session.setAttribute("tId",targetId);
        //session.setAttribute("mId",measureId);

%>
        <br><br><br><br>
        <center>
            
            <h2 style="color:black">Time Range Selection</h2>
            <br>
            <form name="myForm" method="post">
                <table width="510px" border="0">
                    <tr>
                        <td align="center" colspan="2" style="font-weight:bold;color:black" class="myhead">Source</td>
                    </tr>
                    
<%
    if(minTimeLevel.equalsIgnoreCase("Day"))
    {
%>
                    <tr>
                        <td align="right" style="color:black">From Date <input type="text" readonly class="myTextbox5" name="SrcFromDate" id="SrcFromDate"></td>
                        <td style="color:black">To Date <input type="text" readonly  class="myTextbox5" name="SrcToDate" id="SrcToDate"></td>
                    </tr>
<%
    }
    else if(minTimeLevel.equalsIgnoreCase("Month"))
    {
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getMonths = targetClient.getMonths(targetSession);

        int numOfMonths = getMonths.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfMonths are::: "+numOfMonths);

%>
                    <tr>
                        <td align="right" style="color:black">
                            From Month
                            <select class="myTextbox5" name="SrcFromMonth">
<%
                            String str = null;
                            for(int i=0;i<numOfMonths;i++)
                            {
                                str = getMonths.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Month
                            <select class="myTextbox5" name="SrcToMonth">
<%
                            String str2 = null;
                            for(int i=0;i<numOfMonths;i++)
                            {
                                str2 = getMonths.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
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
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getQtrs = targetClient.getQuarters(targetSession);

        int numOfQtrs = getQtrs.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are:::: "+numOfQtrs);
%>
                    <tr>
                        <td align="right" style="color:black">
                            From Quarter
                            <select class="myTextbox5" name="SrcFromQtr">
<%
                            String str = null;
                            for(int i=0;i<numOfQtrs;i++)
                            {
                                str = getQtrs.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Quarter
                            <select class="myTextbox5" name="SrcToQtr">
<%
                            String str2 = null;
                            for(int i=0;i<numOfQtrs;i++)
                            {
                                str2 = getQtrs.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
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
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getYears = targetClient.getYears(targetSession);

        int numOfYears = getYears.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are::: "+numOfYears);
%>
                    <tr>
                        <td align="right" style="color:black">
                            From Year
                            <select class="myTextbox5" name="SrcFromYear">
<%
                            String str = null;
                            for(int i=0;i<numOfYears;i++)
                            {
                                str = getYears.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Year
                            <select class="myTextbox5" name="SrcToYear">
<%
                            String str2 = null;
                            for(int i=0;i<numOfYears;i++)
                            {
                                str2 = getYears.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
<%
                            }
%>
                            </select>
                        </td>
                    </tr>

<%
    }
%>
                    <tr>
                        <td>&nbsp;</td>
                    </tr>
                    <tr>
                        <td align="center" colspan="2" style="font-weight:bold;color:black" class="myhead">Destination</td>
                    </tr>
<%
    if(minTimeLevel.equalsIgnoreCase("Day"))
    {
%>
                    <tr>
                        <td align="right" style="color:black">From Date <input type="text" readonly class="myTextbox5" name="DesFromDate" id="DesFromDate"></td>
                        <td style="color:black">To Date <input type="text" readonly class="myTextbox5" name="DesToDate" id="DesToDate"></td>
                    </tr>
<%
    }
    else if(minTimeLevel.equalsIgnoreCase("Month"))
    {
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getMonths = targetClient.getMonths(targetSession);

        int numOfMonths = getMonths.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfMonths are::: "+numOfMonths);

%>
                    <tr>
                        <td align="right" style="color:black">
                            From Month
                            <select class="myTextbox5" name="DesFromMonth">
<%
                            String str = null;
                            for(int i=0;i<numOfMonths;i++)
                            {
                                str = getMonths.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Month
                            <select class="myTextbox5" name="DesToMonth">
<%
                            String str2 = null;
                            for(int i=0;i<numOfMonths;i++)
                            {
                                str2 = getMonths.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
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
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getQtrs = targetClient.getQuarters(targetSession);

        int numOfQtrs = getQtrs.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are:::: "+numOfQtrs);
%>
                    <tr>
                        <td align="right" style="color:black">
                            From Quarter
                            <select class="myTextbox5" name="DesFromQtr">
<%
                            String str = null;
                            for(int i=0;i<numOfQtrs;i++)
                            {
                                str = getQtrs.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Quarter
                            <select class="myTextbox5" name="DesToQtr">
<%
                            String str2 = null;
                            for(int i=0;i<numOfQtrs;i++)
                            {
                                str2 = getQtrs.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
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
        targetParams.setTargetStartDate(st_with);
        targetParams.setTargetEndDate(end_with);
        targetSession.setObject(targetParams);
        PbReturnObject getYears = targetClient.getYears(targetSession);

        int numOfYears = getYears.getRowCount();
        ////////////////////////////////////////////////////////////////////////.println("numOfQtrs are::: "+numOfYears);
%>
                    <tr>
                        <td align="right" style="color:black">
                            From Year
                            <select class="myTextbox5" name="DesFromYear">
<%
                            String str = null;
                            for(int i=0;i<numOfYears;i++)
                            {
                                str = getYears.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str%>"><%=str%></option>
<%
                            }
%>
                            </select>
                        </td>
                        <td style="color:black">
                            To Year
                            <select class="myTextbox5" name="DesToYear">
<%
                            String str2 = null;
                            for(int i=0;i<numOfYears;i++)
                            {
                                str2 = getYears.getFieldValueString(i,"VIEW_BY");
%>
                                <option value="<%=str2%>"><%=str2%></option>
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
                        <td>
                            <input style="color:black" type="checkbox" checked name="copyNotNull" id="copyNotNull"><span style="color:black">Non-Blank cells will not be updated.</span>
                        </td>
                    </tr>
                </table>
                <br>
                <table>
                    <tr>
                        <td>
                            <input type="button" value="Cancel" onclick="window.close()">
                        </td>
                        <td>
                            <input type="button" value="Save" onclick='return copyRange("<%=st_with%>","<%=end_with%>","<%=minTimeLevel%>")'>
                        </td>
                    </tr>
                </table>
            </form>
        </center>
    </body>
</html>
