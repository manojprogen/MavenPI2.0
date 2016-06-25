
<%@page import="prg.db.PbReturnObject"%>
<%@page import="com.progen.scenario.display.DownloadScenarioExcel"%>
<%@page import="java.util.ArrayList" %>


<%
         

     

            String displayLabels = request.getParameter("displayLabels");
            String tabDetails = request.getParameter("tabDetails");
            String scenarioName = request.getParameter("scenarioName");

            String[] displayLabelsArray = null;
            String[] columnArray = null;
            try {

                displayLabelsArray = displayLabels.split(",");
                columnArray = tabDetails.split(",");
                String headerTitle = scenarioName;
                String pdfName = scenarioName;



                DownloadScenarioExcel excel = new DownloadScenarioExcel();

                //  excel.setColorCodeMap(ColorCodeMap);

                excel.setResponse(response);
                excel.setDisplayColumns(columnArray);
                excel.setDisplayLabels(displayLabelsArray);
                // excel.setDisplayType(displayType);
                excel.setRequest(request);

                if (pdfName != null && !"".equalsIgnoreCase(pdfName)) {
                    excel.setReportName(headerTitle);
                    excel.setFileName(pdfName+".xls");
                    excel.createExcel();
                } else {
                    excel.setReportName("Excel Report");
                    excel.setFileName("downloadExcel.xls");
                    excel.createExcel();
                }





           //////.println("in function jasp ending");

            } catch (Exception exp) {
                exp.printStackTrace();
                  //////.println("in function jasp catch");
            }
%>