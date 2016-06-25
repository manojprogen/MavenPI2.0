<%-- 
    Document   : pbKPITargetType
    Created on : Feb 18, 2010, 4:49:52 PM
    Author     : Saurabh
--%>


<%@page icontentType="text/html" pageEncoding="UTF-8" mport="com.progen.reportdesigner.db.DashboardTemplateDAO"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%
            String kpiId = request.getParameter("kpiIds");
            String kpiName = request.getParameter("kpiName");
            String timeDim = request.getParameter("timeDim");
            String dashboardId = request.getParameter("dashboardId");
////////////.println("timedim in kpitargettype is: "+timeDim);
            String contextPath=request.getContextPath();
            %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>

        <script src="<%=contextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=contextPath%>/javascript/dashboardDesign.js"></script>
        <link type="text/css" href="<%=contextPath%>/stylesheets/ui.all.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextPath%>/stylesheets/demos.css" rel="stylesheet" />
        <link href="<%=contextPath%>/stylesheets/javascript.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/html.css" type="text/css" rel="stylesheet">
        <link href="<%=contextPath%>/stylesheets/css.css" type="text/css" rel="stylesheet">
        <link type="text/css" href="<%=contextPath%>/stylesheets/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript">
           
                        function chooseMeasureType()
            {
                var measType=$("select#MeasId").val();
                var basis=$("select#selectBasis").val();
               
                Viewer_getKPIType('proRated','<%=kpiId%>','<%=kpiName%>','<%=timeDim%>',measType,basis);
            }
        </script>
            <style type="text/css">
                .tdstyle {
                font-family: verdana;
                font-size: 12px;
                font-size-adjust: none;
                font-stretch: normal;
                font-style: normal;
                font-variant: normal;
                
                line-height: normal;


            }
            </style>
    </head>
    <body>
        <form name="kpiTargetTypeForm" method="post">
            
<!--            <table align="center">
                <Tr>
                    <Td><font style="font-family:verdana;font-size:14px;font-weight:bold;color:#369;">Select Target Type :</font></Td>
                </Tr>
                <Tr>
                    <Td><input type="radio" name="targetType" id="targetType" value="proRated" onclick="Viewer_getKPIType('proRated','<%=kpiId%>','<%=kpiName%>','<%=timeDim%>')">Pro Rated Period</Td>
                    <Td><input type="radio" name="targetType" id="targetType" value="individual" onclick="Viewer_getKPIType('individual','<%=kpiId%>','<%=kpiName%>','<%=timeDim%>')">Individual Period</Td>
                </Tr>
            </table>            -->
 <div id="measureTypeDiv" >
                <table>
                    <tr>
                        <td class="tdstyle">Measure Type</td>
                        <td>
                            <select id="MeasId">
                                <option value="Standard">Standard  </option>
                                <option value="Non-Standard"> Non-Standard</option>
                            </select>
                        </td>
                    </tr>
                    <tr></tr>
                    <tr>
                        <td class="tdstyle">   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Basis</td>
                        <td>
                            <select id="selectBasis">
                                <option value="absolute">Absolute  </option>
                                <option value="deviation">Deviation%</option>
                            </select>
                        </td>
                    </tr>
                </table>
                <br><br>
                <table align="center">
                    <tr>
                        <td><input type="button" value="Next" class="navtitle-hover" onclick="chooseMeasureType()"/></td>
                    </tr>
                </table>
            </div>

        </form>
    </body>    
</html>
