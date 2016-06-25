


<%@page import="java.util.ArrayList,com.progen.query.RTDimensionElement,com.progen.db.ProgenDataSet,com.progen.report.MultiPeriodKPI,com.progen.reportdesigner.bd.ReportTemplateBD"%>
<%@page contentType="text/html" pageEncoding="windows-1252" import="prg.db.Container,com.progen.report.pbDashboardCollection,java.math.BigDecimal,java.text.NumberFormat,java.util.*,java.math.MathContext,prg.db.PbReturnObject" %>
<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            String PercentColumn = "_percentwise";
            boolean isTop = true;
            String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
            String contextpath1=request.getContextPath();

%>
<html>
    <head>
        <title>piEE</title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
          <script type="text/javascript" src="<%=contextpath1%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath1%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link href="css/styles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=contextpath1%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath1%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link href="tracker/css/extremecomponents.css" rel="stylesheet" type="text/css">
      <script type="text/javascript" src="<%=contextpath1%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=contextpath1%>/stylesheets/toolTip.css" type="text/css">

        <script type="text/javascript">
            function goSubmit(path){
                document.forms.topbottomForm.action='TopBottomTable.jsp';
                document.forms.topbottomForm.submit();
            }

        </script>
        <style type="text/css">
            .prgtable1
            {
                border: #silver solid 1px;
                font-size: 1.4em;
                border-collapse:collapse;
            }
            .prgtableheader
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 11px;
                font-weight: bold;
                color: #3A457C;
            }
            .myTextbox5 {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-weight: normal;
                font-size: 8pt;
                color:#000000;
                padding: 0px;
                width:92px;
                font-weight:bold;
                /*apply this class to a TextBox/Textfield only*/
            }

            .myTextField4 {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-weight: normal;
                font-size: 8pt;
                color:#000000;
                padding: 0px;
                width:100px;
                /*apply this class to a TextBox/Textfield only*/
            }
            .btn {
                font-family: Verdana, Arial, tahoma, sans-serif;
                font-size:11px;
                background-color: #B5B5B5;
                color:#454545;
                font-weight:600;
                height:15px;
                text-decoration:none;
                cursor: pointer;
            }
            *{font:11px verdana;}
            dl dt{float:left;width:100px;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:right;}
            dl dd{float:none;margin:0 0 1px 4px;line-height:6px}
            dl{font-family:verdana;font-size:10px;line-height:5px}
        </style>
    </head>
    <%
                String REPORTID = request.getParameter("REPORTID");
                String perBy = request.getParameter("perBy");
                String measureBy = request.getParameter("msrBy");
                ArrayList<String> sortColumns = new ArrayList<String>();

                String name = "";
                Container container = null;
                HashMap map = (HashMap) session.getAttribute("PROGENTABLES");
                container = (prg.db.Container) map.get(REPORTID);
                ArrayList displayCols = container.getDisplayColumns();
                ArrayList displayLabels = container.getDisplayLabels();
                HashMap ColumnsVisibility = container.getColumnsVisibility();
                MultiPeriodKPI pbretObj1 = container.getMultiPeriodKPI();

                PbReturnObject sortPbretObj = null;

                int start = container.getViewByCount();
                int endCount = 5;
                char[] sortTypes = new char[1];
                char[] sortDataTypes = new char[1];
                int noOfRows = 0;
                   String[] topbottom={"Top 5","Bottom 5","Top 10","Bottom 10","Top 25","Bottom 25"};
            String[] topbottomVal={"Top5","Bottom5","Top10","Bottom10","Top25","Bottom25"};
            HashMap<String,String> topBottomTableMap=new HashMap<String, String>();

    %>
    <body>
        <form method="get" action="TopBottomTable.jsp" name="topbottomForm" id="topbottomForm">
            <input type="hidden" name="REPORTID" id="REPORTID" value="<%=REPORTID%>">
            <% if (pbretObj1 != null) {%>

            <%
                        String monthval="";
                        String yearval="";
                        String Qrtval="";
                         String dayval="";
                         String weekval="";
//                         String topbottomdisplay="top5";
                         String Weekheader="Week",Dayheader="Day";String Monthheader="Month";String Qtrheader="Qtr";String Yearheader="Year";

                      ReportTemplateBD bD=new ReportTemplateBD();
                    if(perBy!=null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom5") || perBy.equalsIgnoreCase("Bottom10") || perBy.equalsIgnoreCase("Bottom25"))){
                        isTop=false;
                    }
                      if(perBy!=null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom5") || perBy.equalsIgnoreCase("Top5") )){
                          endCount=5;
                      }
                     if(perBy!=null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom10") || perBy.equalsIgnoreCase("Top10") )){
                          endCount=10;
                      }
                        if(perBy!=null && !perBy.isEmpty() && (perBy.equalsIgnoreCase("Bottom25") || perBy.equalsIgnoreCase("Top25") )){
                          endCount=25;
                      }
                      if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                          topBottomTableMap.put("TopBottomVal",perBy);
                          topBottomTableMap.put("TopBottomMsr",measureBy);
                      }else{
                          perBy="Top5";
                          measureBy=(String)container.getDisplayColumns().get(start);
                          topBottomTableMap.put("TopBottomVal",perBy);
                          topBottomTableMap.put("TopBottomMsr",measureBy);
                      } 
                      container.setTopBottomTableHashMap(topBottomTableMap);
                      if (pbretObj1 != null && pbretObj1.getMonthObject() != null) {
                              monthval = bD.TopBottom(REPORTID, pbretObj1.getMonthObject(), container, isTop, endCount);
                          }
                          if (pbretObj1 != null && pbretObj1.getYearObject() != null) {
                              yearval = bD.TopBottom(REPORTID, pbretObj1.getYearObject(), container, isTop, endCount);
                          }
                          if (pbretObj1 != null && pbretObj1.getQuarterObject() != null) {
                              Qrtval = bD.TopBottom(REPORTID, pbretObj1.getQuarterObject(), container, isTop, endCount);
                          }
                          if (pbretObj1 != null && pbretObj1.getWeekObject() != null) {
                              weekval = bD.TopBottom(REPORTID, pbretObj1.getWeekObject(), container, isTop, endCount);
                          }
                          if (pbretObj1 != null && pbretObj1.getDayObject() != null) {
                              dayval = bD.TopBottom(REPORTID, pbretObj1.getDayObject(), container, isTop, endCount);
                          }
                        if(weekval==null || weekval.equalsIgnoreCase("")|| weekval.isEmpty()){
                                  Weekheader="Week(Month)";
                                  weekval=monthval;
                              }
//                      if(container.getTopBottomDispaly()!=null && !container.getTopBottomDispaly().isEmpty()){
//                          topbottomdisplay=container.getTopBottomDispaly();
//                      }
                      String selectedMsrName = "";
                      if(displayCols.contains(measureBy))
                         selectedMsrName=String.valueOf(displayLabels.get(displayCols.indexOf(measureBy)));
            %>



                        <table   style="width: 100%;border:1px solid black;">
                            <tr valign="top">

                                <td>
                        <table   style="width: 100%;height: auto">
                            <tr>

                                <td style="width: 15%">

                          <select name="perBy" id="perBy"  class="myTextbox5">
                              <%for(int i=0;i<topbottom.length;i++){
                                if(perBy!=null && perBy.equalsIgnoreCase(topbottomVal[i])){%>
                            <option selected value="<%=topbottomVal[i]%>"><%=topbottom[i]%></option>
                              <% }else{%>
                              <option value="<%=topbottomVal[i]%>"><%=topbottom[i]%></option>
                              <%}
                            }%>
                          </select>
                    </td>
                     <td style="width: 15%">
                        <select name="msrBy" id="msrBy"  class="myTextbox5">
                            <%
                                for (int colIndex = start; colIndex < displayCols.size(); colIndex++) {
                                    String columnName = String.valueOf(displayCols.get(colIndex));
                                    String disColumnName = String.valueOf(displayLabels.get(colIndex));
                                    int percentColumn = columnName.lastIndexOf(PercentColumn);
                                    if (percentColumn == -1) {
                                        //                                    if (String.valueOf(ColumnsVisibility.get(columnName)).equalsIgnoreCase("''")) {
                                        if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                                            if (measureBy.equalsIgnoreCase(columnName)) {
                            %>
                            <option selected value="<%=columnName%>"><%=disColumnName%></option>
                            <%
                            } else {
                            %>
                            <option value="<%=columnName%>"><%=disColumnName%></option>
                            <%
                                }
                            } else {
                            %>
                            <option value="<%=columnName%>"><%=disColumnName%></option>
                            <%
                                        }
                                        //                        }
                                    }
                                }
                                ;%>
                        </select>
                    </td>
                     <td>
                        <input type="button" name="View" value="Go" id="View" class="navtitle-hover" style="width:auto" onclick="goSubmit('reportViewer.do?reportBy=viewReport&REPORTID=<%=REPORTID%>')">
                    </td>
                </tr>
                        </table>
                    <table width="100%"  style="border:1px solid black;" >


<thead style="height: 25;background-color: lightgray; background: linear-gradient(to bottom, #D5E3E4 0%, #CCDEE0 40%, #B3C8CC 100%) repeat scroll 0 0 transparent;">

    <tr>
        <th style="color: black;font-size: 11px;font-weight: bold; width: 20%"><%=selectedMsrName%>&nbsp;&nbsp;&nbsp;&nbsp;<%=Dayheader%> </th>
        <th style="color: black;font-size: 11px;font-weight: bold; width: 20%"> <%=Weekheader%> </th>
        <th style="color: black;font-size: 11px;font-weight: bold; width: 20%"><%=Monthheader%></th>
        <th style="color: black;font-size: 11px;font-weight: bold;width: 20%;"><%=Qtrheader%> </th>
        <th style="color: black;font-size: 11px;font-weight: bold;width: 20%"><%=Yearheader%> </th>
    </tr>
</thead>

                     <tbody style=" height: 262px;   overflow: auto;  width: 100%;">
                     <tr valign="top" style="height: 75%">
                                <td>
                       <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                             <%=dayval%>
                        </table>
                                </td>
                                <td>
                       <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                             <%=weekval%>
                        </table>
                                </td>
                                                   <td>
                       <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                           <%=monthval%>
                        </table>
                                </td>
                                                   <td>
                       <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                            <%=Qrtval%>
                        </table>
                                </td>
                                                   <td>
                       <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                           <%=yearval%>
                        </table>
                                </td>

                            </tr>
                            </tbody>
                        </table>
                 </td>
                            </tr>

            </table>


            <%}%>
        </form>
    </body>
</html>
