


<%@page import="com.progen.db.ProgenDataSet"%>
<%@page import="com.progen.query.RTDimensionElement"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@ page import="prg.db.Container" %>
<%@ page import="prg.db.PbReturnObject" %>
<%@ page import="java.util.*,java.math.MathContext" %>
<%@page import="com.progen.report.pbDashboardCollection,java.math.BigDecimal,java.text.NumberFormat" %>
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

%>
<html>
    <head>
        <title>piEE</title>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
          <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>     
        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>

        <link href="css/styles.css" rel="stylesheet" type="text/css">
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/css.css" rel="stylesheet" />
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/pbReportViewerCSS.css" rel="stylesheet" />
        <link href="tracker/css/extremecomponents.css" rel="stylesheet" type="text/css">
      <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/toolTip.js"></script>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/toolTip.css" type="text/css">

        <script type="text/javascript">
            function goSubmit(path){
                document.forms.topbottomForm.action='topBottom.jsp';
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
                ProgenDataSet pbretObj = container.getRetObj();
                PbReturnObject sortPbretObj = null;
                
                int start = container.getViewByCount();
                int endCount = 5;
                ArrayList<Integer> topBttmList = new ArrayList<Integer>();
                char[] sortTypes = new char[1];
                char[] sortDataTypes = new char[1];
                int noOfRows = 0;
                if (pbretObj.getRowCount() < 5) {
                         noOfRows = pbretObj.getRowCount();
                     } else {
                         noOfRows = endCount;
                     }
                if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                
                               }else{
                    if (isTop == true) {
                                  sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  BigDecimal value = pbretObj.getFieldValueBigDecimal(topBttmList.get(0), String.valueOf(displayCols.get(start)));
                                  if(value==null){
                                      start = displayCols.size()-1;
                                      sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  }
                              } else {
                                  sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '0';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  BigDecimal value = pbretObj.getFieldValueBigDecimal(topBttmList.get(0), String.valueOf(displayCols.get(start)));
                                  if(value==null){
                                      start = displayCols.size()-1;
                                      sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  }
                              }
                               }
                
                ArrayList<BigDecimal> signList = new ArrayList<BigDecimal>();
    %>
    <body>
        <form method="get" action="topBottom.jsp" name="topbottomForm" id="topbottomForm">
            <input type="hidden" name="REPORTID" id="REPORTID" value="<%=REPORTID%>">
            <% if (pbretObj != null) {%>

            <table border="1" cellpadding="0" cellspacing="0" class="prgtable1">
                <tr>
                    <td>
                        <select name="perBy" id="perBy"  class="myTextbox5">
                            <%if (perBy != null && (!"".equalsIgnoreCase(perBy))) {
                                 if (perBy.equalsIgnoreCase("top")) {
                                     isTop = true;%>
                            <option selected value="top">Top 5</option>
                            <option value="bottom">Bottom 5</option>
                            <%} else if (perBy.equalsIgnoreCase("bottom")) {
                                     isTop = false;%>
                            <option value="top">Top 5</option>
                            <option selected value="bottom">Bottom 5</option>
                            <%}
                            } else {
                                isTop = true;
                            %>
                            <option selected value="top">Top 5</option>
                            <option value="bottom">Bottom 5</option>
                            <%}%>
                        </select>
                    </td>
                    <td align="center">
                        <input type="button" name="View" value="View" id="View" class="navtitle-hover" style="width:auto" onclick="goSubmit('reportViewer.do?reportBy=viewReport&REPORTID=<%=REPORTID%>')">
                    </td>
                </tr>
                <tr>
                    <%if (displayLabels != null && displayLabels.size() != 0)%>
                    <td class="myTextField5" style="font-weight:bold">&nbsp;<%=String.valueOf(displayLabels.get(0))%></td>
                    <td>
                        <select name="msrBy" id="msrBy"  class="myTextbox5">
                            <%
                                 for (int colIndex = container.getViewByCount(); colIndex < displayCols.size(); colIndex++) {
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
                                                 if(colIndex==start){
                            %>
                            <option selected value="<%=columnName%>"><%=disColumnName%></option>
                            <%
                                                 }else{
                            %>
                            <option value="<%=columnName%>"><%=disColumnName%></option>
                            <%
                                             }
                                             }
         //                        }
                             }
                             }
                             ;%>
                        </select>
                    </td>
                </tr>
                <%

                     if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                         if (perBy != null && (!"".equalsIgnoreCase(perBy))) {
                             if (perBy.equalsIgnoreCase("top")) {
                                 //sortPbretObj = pbretObj.sort(1, measureBy, "N");
                                 sortColumns.add(measureBy);
                                 sortTypes[0] = '1';
                                 sortDataTypes[0] = 'N';
                                 topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
       //                          pbretObj.setViewSequence(topBttmList);
                             } else if (perBy.equalsIgnoreCase("bottom")) {
                                 //sortPbretObj = pbretObj.sort(0, measureBy, "N");
                                 sortColumns.add(measureBy);
                                 sortTypes[0] = '0';
                                 sortDataTypes[0] = 'N';
                                 topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);

                             }
                         }
                     } //   else {
                     //        sortPbretObj = pbretObj.sort(1, String.valueOf(displayCols.get(start)), "N");
                     //    }
                     else {
                              if (isTop == true) {
                                  sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  BigDecimal value = pbretObj.getFieldValueBigDecimal(topBttmList.get(0), String.valueOf(displayCols.get(start)));
                                  if(value==null){
                                      start = displayCols.size()-1;
                                      sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  }
                              } else {
                                  sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '0';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                                  BigDecimal value = pbretObj.getFieldValueBigDecimal(topBttmList.get(0), String.valueOf(displayCols.get(start)));
                                  if(value==null){
                                      start = displayCols.size()-1;
                                      sortColumns.add((String) displayCols.get(start));
                                  sortTypes[0] = '1';
                                  sortDataTypes[0] = 'N';
                                  topBttmList = pbretObj.findTopBottom(sortColumns, sortTypes,  noOfRows);
                              }
                          }
                          }


                     //    if (sortPbretObj.getRowCount() < endCount) {
                     //        endCount = sortPbretObj.getRowCount();
                     //    }
                     if (topBttmList.size() < endCount) {
                         endCount = topBttmList.size();
                     }
                %>


                <%
                     BigDecimal TotalValue = new BigDecimal("0");
                     StringBuffer disptopBottomChart = new StringBuffer();
                     BigDecimal[] valArray = new BigDecimal[endCount];
                     BigDecimal MaxValue = new BigDecimal("0");
                     String dimension = container.getDisplayColumns().get(0);
                 for (int i = 0; i < endCount; i++) {%>
                <tr>

                    <%if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {
                                 if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                                 } else {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0))).toString().toUpperCase();
                                 }
                                 name = (name.length() > 10) ? name = name.substring(0, 9) + ".." : name;

                             if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {%>
                    <td  class="myTextField5" title="<%=pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase())%>"><%=name%></td>
                    <%} else {%>
                    <td  class="myTextField5" title="<%=pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase())%>"><%=name%></td>
                    <%}%>
                    <td  class="myTextField4"><%=pbretObj.getFieldValueString(topBttmList.get(i), measureBy)%></td>
                    <%
                        valArray[i] = pbretObj.getFieldValueBigDecimal(topBttmList.get(i), measureBy);
                        signList.add(valArray[i]);
                        TotalValue = pbretObj.getColumnGrandTotalValue(measureBy);
                    } else {
                        if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                            name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                        } else {
                            name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase()).toString();
                        }

                        name = (name.length() > 10) ? name = name.substring(0, 9) + ".." : name;

                    if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {%>
                    <td  class="myTextField5" title="<%=pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase())%>"><%=name%></td>
                    <%} else {%>
                    <td  class="myTextField5" title="<%=pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase())%>"><%=name%></td>
                    <%}%>
                    <td  class="myTextField4"><%=pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(start)))%></td>

                    <%
                                 valArray[i] = pbretObj.getFieldValueBigDecimal(topBttmList.get(i), String.valueOf(displayCols.get(start)));
                                 signList.add(valArray[i]);
                                 TotalValue = pbretObj.getColumnGrandTotalValue(String.valueOf(displayCols.get(start)));
                         }%>
                </tr>
                <%
                           //
                           if(valArray[i]!=null)
                         MaxValue = MaxValue.max(valArray[i]);
                         //  
                         if(valArray[i]!=null){
                         if (valArray[i].longValue() < 0) {
                             MaxValue = MaxValue.max(valArray[i].negate());
                         } else if (MaxValue.intValue() == 0) {
                             MaxValue = new BigDecimal("1");
                         }
                         }
                     }
                     // 

                %>
            </table>
            <div style="border:1px solid black"></div>

            <table style="width:100%;">
                <tr>
                    <%
                         ArrayList<Boolean> checkList = new ArrayList<Boolean>();

                         for (int j = 0; j < signList.size(); j++) {
                             if (signList.get(j).intValue() < 0) {
                                 checkList.add(false);
                             } else {
                                 checkList.add(true);
                             }
                         }

                         BigDecimal[] percentageVal = new BigDecimal[valArray.length];
                         NumberFormat nformat = NumberFormat.getInstance(Locale.US);
                         nformat.setMaximumFractionDigits(2);
                         nformat.setMinimumFractionDigits(2);
                         if (TotalValue == null)
                             TotalValue = new BigDecimal("0");
                         TotalValue = (TotalValue.doubleValue() == 0.0) ? new BigDecimal("1") : TotalValue;
                         BigDecimal pixelSize = new BigDecimal("50");

                         //  MaxValue = new BigDecimal(MaxValue.intValue());
                         for (int i = 0; i < endCount; i++) {
                             valArray[i] = valArray[i].divide(new BigDecimal("1"), MathContext.DECIMAL32);
                             percentageVal[i] = valArray[i].multiply(new BigDecimal("100"));
                             percentageVal[i] = percentageVal[i].divide(TotalValue, MathContext.DECIMAL32);
                             disptopBottomChart.append("<tr>");
                             if (measureBy != null && (!"".equalsIgnoreCase(measureBy))) {

                                 if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                                 } else {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase());
                                 }
                                 if (name.length() > 10) {
                                     name = name.substring(0, 9) + "..";
                                 }
                                 if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                                     disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;' title=\"" + pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase()) + "\">" + name + "</td>");
                                 } else {
                                     disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;' title=\"" + pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase()) + "\">" + name + "</td>");
                                 }
                             } else {
                                 if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1))).toString().toUpperCase();
                                 } else {
                                     name = pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase());
                                 }
                                 name = (name.length() > 10) ? name = name.substring(0, 9) + ".." : name;
                                 if (RTDimensionElement.getDimensionType(dimension) == RTDimensionElement.SEGMENT) {
                                     disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;' title=\"" + pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(1)).toUpperCase()) + "\">" + name + "</td>");

                                 } else {
                                     disptopBottomChart.append("<td style='float:none;margin:0 8px 1px 4px;' title=\"" + pbretObj.getFieldValueString(topBttmList.get(i), String.valueOf(displayCols.get(0)).toUpperCase()) + "\">" + name + "</td>");
                                 }
                             }
                             int size = valArray[i].divide(MaxValue, MathContext.DECIMAL32).multiply(pixelSize).intValue();
                             // ////.println("percentageVal[i]--" + percentageVal[i] + "---nformat.format(percentageVal[i])---" + nformat.format(percentageVal[i]));
                             if (nformat.format(percentageVal[i]) != "0" && size == 0) {
                                 size = 1;
                             }


                             if (!checkList.contains(true)) {
                                 //size=(100+size)/100;
                                 size = 100 + size;
                                 //size= Math.abs(size);


                             } else {
                                 if (checkList.get(i) == false) {
                                     size = 0;
                                 }
                             }
                             if (isTop) {
                                 disptopBottomChart.append("<td style='float:left;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:left;'><img style=\"width:" + size + "px;height:10px;\" src='images/greenbar.png'  title='" + nformat.format(percentageVal[i]) + "%'/>&nbsp;<font style='font-family:verdana;font-size:9px'>" + nformat.format(percentageVal[i]) + "%</font></td>");
                             } else {
                                 disptopBottomChart.append("<td style='float:left;clear:both;margin:0 8px 0 0;font-weight:normal;text-align:left;'><img style=\"width:" + size + "px;height:10px;\" src='images/barchart.gif'  title='" + nformat.format(percentageVal[i]) + "%'/>&nbsp;<font style='font-family:verdana;font-size:9px'>" + nformat.format(percentageVal[i]) + "%</font></td>");
                             }
                             disptopBottomChart.append("</tr>");
                     }%>
                    <td>
                        <table style="line-height:10px;width:100%" cellpadding="0" cellspacing="0">
                            <%=disptopBottomChart.toString()%>
                        </table>
                    </td>
                </tr>
            </table>
            <%}%>
        </form>
    </body>
</html>
