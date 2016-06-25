<%@ page import="com.progen.charts.*"%>

<%
        String GraphImage = "servlet/DisplayChart?filename=jfreechart-onetime-7983005660946297473.png";
//"";
        GraphImage = (String) request.getAttribute("GraphImage");
        //////////////////////////////////////////////////////////////////////////////////////////////.println.println("in jsp GraphImage " + GraphImage);
        //////////////////////////////////////////////////////////////////////////////////////////////.println.println(" request.getContextPath() " + request.getContextPath());
        ProgenChartDisplay chartDisplay = new ProgenChartDisplay(400, 400);
        chartDisplay.setCtxPath(request.getContextPath() + "/" + GraphImage);
        out.println(chartDisplay.chartDisplay);

%>