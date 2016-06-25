<%--
    Document   : PbGraphProperties
    Created on : Jan 15, 2010, 7:26:27 PM
    Author     : Administrator
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.reportview.db.PbReportViewerDAO,com.progen.charts.JqplotGraphProperty,java.util.Iterator,com.progen.users.PrivilegeManager,prg.db.Container,java.util.HashMap,java.util.Set,java.util.ArrayList"%>
<%@page import="com.progen.report.entities.GraphReport,com.progen.report.DashletDetail,com.progen.report.pbDashboardCollection,com.progen.charts.GraphProperty,com.progen.reportdesigner.db.DashboardTemplateDAO,prg.db.PbReturnObject"%>

<%          //for clearing cache
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            Container container = null;
            String reportId = request.getParameter("reportId");
            String graphId = request.getParameter("graphId");
            String fromDesigner = request.getParameter("fromDesigner");
            String dashletId="";
            String kpiMastId="";
            String refReportId="";
            String displaySeq="";
            String dispType="";
            String dashName="";
            HashMap singeGraphDetails = null;
            HashMap GraphTypesHashMap;
            HashMap GraphClassesHashMap;
            HashMap GraphSizesHashMap;
            //HashMap GraphSizesDtlsHashMap;
            String[] graphTypesArray = null;
            String[] graphSizesArray = null;
            String[] graphClassesArray = null;
            String[] firstChartTypeArray = {"bar","bar3d","line","line3d","stacked","stackedarea","stacked3d","Area"};
            String[] secondChartTypeArray = {"line","line3d"};
            String[] grpLegends = {"Bottom", "Top", "Left", "Right"};
            String[] grpNbrFormats = {"", "K","L","Cr" ,"M"};
            String[] grpNbrFormatsDisp = {"Absolute", "Thousands(K)","Lakhs(L)","Crores(C)", "Millions(M)"};
            String[] SwapColumnArray = {"Basis: View By", "Basis: Measures"};
            String[] SwapColumnArrayValues = {"true", "false"};
            String[] graphSymbols = {" ", " $ ", " Rs ", " Euro ", " Yen "};//dollar,inr,euro,yen and percent
            String[] displayGraphRows = {"","5", "10", "15", "25", "50","All"};
            String[] targetRangeTypes = {"Discrete", "Range"};
            boolean showExtraTabs=true;
            String themeColor="blue";
            String from=request.getParameter("from");
            String selectedgraphtype =null;
            String frameheight="500";
            String[] measureNamePositionArray = {" ", " Not Display ", " TOP ", " BOTTOM ", " LEFT " ," RIGHT "};
            String[] measureFomat={"value","%value","ST","valueWithPercentage"};
            String[] rounddisp={"zero","One","Two"};
            String[] round={"0","1","2"};
            String[] yaxiscal={"zero","Default","Custom"};
            String[] yaxisval={"0","Default","Custom"};
            String[] labelPosition={"Diagnol","Horizontal","Vertical"};
            String[] calibration={"Integer","Float"};
            String[][] colors={
                {"rgb(100, 178, 255)","#64B2FF"},
                {"rgb(181, 249, 80)","#B5F950"},
                {"rgb(51, 230, 250)","#33E6FA"},
                {"rgb(239, 230, 98)","#EFE662"},
                {"rgb(225, 117, 95)","#E1755F"},
                {"rgb(170, 199, 73)","#AAC749"},
                {"rgb(134, 25, 86)","#867D56"},
                {"rgb(201, 160, 220)","#C9A0DC"},
                {"rgb(0, 153, 204)","#0099CC"},
                {"rgb(142, 84, 84)","#8E5454"},
                {"rgb(255, 153, 0)","#FF9900"}

            };
            String[] jqcolors={"#357EC7", "#667c26", "#C24641", "#A0C544", "#53B3FF","#737CA1","#7E354D","#E66C2C","#A74AC7","#307D7E"};
            String[] colorSeries=null;
            HashMap selectedColorsMap=new HashMap();
            DashboardTemplateDAO dao=new DashboardTemplateDAO();
            HashMap colorCodesMap=(HashMap)dao.getcolors();
            HashMap colorCodeMap=dao.getColorCodesWithRGB(colorCodesMap);
            Set colorCodeSet=colorCodeMap.keySet();
            String[] yaxisrounddisp={"zero","One","Two"};
            String[] yaxisround={"0","1","2"};
            String[] xaxisrounddisp={"zero","One","Two"};
            String[] xaxisround={"0","1","2"};
            String[] y2axisrounddisp={"zero","One","Two"};
            String[] y2axisround={"0","1","2"};
            String targetvalue="";
           String tickdisplay=""; //kruthika tickinterval
            String[] labelDirec={"Default","Horizontal","Vertical"};
            String[] labelDirecval={"-30","0","90"};
            String[] legendsPerRow={"","3","5","7","9","10","12","15","17","19","21"};
            String[] graphdisplaytype={"Default","GT","AVG","ST"};
            String[] graphType1={"Area","Bar","Line","StackedBar","StackedArea","Dot-Graph"};
            String[] graphType2={"Area","Bar","Line","StackedBar","StackedArea","Dot-Graph"};
            String[] drillType={"Standard","Adhoc","Report"};
            String[] datebyInterval={"Default","1","3","5","6"};
            String[] targetType={"standard","non-standard"};

            if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));

            if (reportId != null && graphId != null) {
                if (request.getSession(false) != null && request.getSession(false).getAttribute("PROGENTABLES") != null) {
                    HashMap map = (HashMap) request.getSession(false).getAttribute("PROGENTABLES");
                    container = (Container) map.get(reportId);
                    GraphTypesHashMap = (HashMap) session.getAttribute("GraphTypesHashMap");
                    GraphSizesHashMap = (HashMap) session.getAttribute("GraphSizesHashMap");
                    GraphClassesHashMap = (HashMap) session.getAttribute("GraphClassesHashMap");
                    //GraphSizesDtlsHashMap = (HashMap) session.getAttribute("GraphSizesDtlsHashMap");
                    //java.util.Collections.sort(GraphTypesHashMap.keySet());

                   // String[] grpTypesSet = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                    String[] grpTypeskeys = (String[]) GraphTypesHashMap.keySet().toArray(new String[0]);
                    String[] grpClasseskeys = (String[]) GraphClassesHashMap.keySet().toArray(new String[0]);
                    String[] grpSizeskeys = (String[]) GraphSizesHashMap.keySet().toArray(new String[0]);
                    graphTypesArray = new String[grpTypeskeys.length];
                    graphSizesArray = new String[grpSizeskeys.length];
                    graphClassesArray = new String[grpClasseskeys.length];

                    for (int i = 0; i < grpTypeskeys.length; i++) {
                        graphTypesArray[i] = GraphTypesHashMap.get(grpTypeskeys[i]).toString();
                    }
                    for (int i = 0; i < grpSizeskeys.length; i++) {
                        graphSizesArray[i] = GraphSizesHashMap.get(grpSizeskeys[i]).toString();
                    }
                    for (int i = 0; i < grpSizeskeys.length; i++) {
                        graphClassesArray[i] = GraphClassesHashMap.get(grpClasseskeys[i]).toString();
                    }

                   if(from!=null && from.equalsIgnoreCase("dashboard"))
                    {
                        //singeGraphDetails= ((HashMap[])(((pbDashboardCollection)container.getReportCollect()).getGraphIdDetails().get(graphId)))[0];
                        dashletId=request.getParameter("dashletId");
                        kpiMastId=request.getParameter("kpiMastId");
                        refReportId=request.getParameter("refReportId");
                        displaySeq=request.getParameter("displaySeq");
                        dispType=request.getParameter("dispType");
                        dashName=request.getParameter("dashName");
                    }
                    else{
                        singeGraphDetails = (HashMap) container.getGraphHashMap().get(graphId);
                    }
                    String graphDisplayRows = "";
                    String grpSize = null;
                    String graphLegend = null;
                    String graphGridLines = null;
                    String showGT = null;
                    String nbrFormat = null;
                    String graphLegendLoc = null;
                    String SwapColumn = null;
                    String graphSymbol = null;
                    boolean graphLabels=false;
                    boolean showOthers=false;
                    String graphType = null;
                    String graphClass = null;
                    boolean showMinMaxRange = false;
                    String targetRange = null;
                    String startValue = null;
                    String endValue = null;
                    String measureNamePosition= null;
                    String showlyAxis=null;
                    String showryAxis=null;
                    String showxAxis=null;
                    String stackedType = null;
                    String grpmeasureFormat=null;
                    String grpmeasureRounding=null;
                    String grplabelPosition=null;
                    String grpcalibration=null;
                    String grpfirstChartType=null;
                    String grpsecondChartType=null;
                    String[] grprgbColorCode=null;
                    boolean graphdrill=false;
                    String jqgraphtype=null;
                    boolean transposeData=false;
                    String yaxiscalibration=null;
                    String ischeckedLA=null;
                    String ischeckedRA=null;
                    String ischeckedXA=null;
                    String ischeckedYA=null;
                    String Flag=null;
                    String yaxisstart="";
                    String yaxisend="";
                    String yaxisinterval="";
                    String xaxisstart="";
                    String xaxisend="";
                    String xaxisinterval="";
                    String y2axisstart="";
                    String y2axisend="";
                    String y2axisinterval="";
                    String customdisplay="none";
                    String yaxisrounding="";
                    String xaxisrounding="";
                    String y2axisrounding="";
                    String graphDisplayColumns="";
                    boolean isAdhocEnabled=false;
                    boolean colorGrouping=false;
                    String labeldirec="";
                    String legendperrow="";
                    String graphdisplay="";
                    String graphtype1="";
                    String graphtype2="";
                    String targetcolor="rgb(88,88,88)";
                    String selDrillType="Standard";
                    String datebyinterval="";
                    String targettype="standard";
                    boolean appendXaxis=false;
                    boolean legendAppend=false;
                    boolean suprisezero=false;
                     boolean tooltipXaxis=true;
                    if(from!=null && from.equalsIgnoreCase("dashboard")){
                        singeGraphDetails = new HashMap();
                        pbDashboardCollection collect = (pbDashboardCollection)container.getReportCollect();
                        DashletDetail dashlet = collect.getDashletDetail(dashletId);
                        JqplotGraphProperty jqprop = new JqplotGraphProperty();
                        jqprop = dashlet.getJqplotgrapprop();
                        if(jqprop!=null){
                            selectedgraphtype="jq";
                            dispType="jq";
                            if(jqprop.getSeriescolors()!=null)
                               jqcolors=jqprop.getSeriescolors();
                        }
                        GraphReport graphDetails = (GraphReport) dashlet.getReportDetails();
                        graphDisplayRows = graphDetails.getDisplayRows();
                        colorSeries=graphDetails.getGraphProperty().getColorSeries();
                        if (graphDisplayRows == null || graphDisplayRows.equals(""))
                            graphDisplayRows = "";
                        grpSize = String.valueOf(graphDetails.getGraphSize());
                        graphLegend = "N";
                        if (graphDetails.isLegendAllowed())
                            graphLegend = "Y";
                        graphGridLines = "N";
                        if (graphDetails.isShowYAxisGrid())
                            graphGridLines = "Y";
                        showGT = "N";
                        if (graphDetails.isShowGT())
                            showGT = "Y";
                        nbrFormat = graphDetails.getGraphProperty().getNumberFormat();
                        graphLegendLoc = graphDetails.getLegendLocation();
                        SwapColumn = graphDetails.getGraphProperty().getSwapGraphColumns();
                        graphSymbol = graphDetails.getGraphProperty().getSymbol();
                        graphClass = String.valueOf(graphDetails.getGraphClass());
                        showMinMaxRange = graphDetails.getGraphProperty().getMinMaxRange();
                        //showMinMaxRange = graphDetails.getGraphProperty().getMinMaxRange();

                        targetRange = targetRangeTypes[0];
                        startValue = String.valueOf(graphDetails.getGraphProperty().getStartValue());
                        endValue = String.valueOf(graphDetails.getGraphProperty().getEndValue());
                        graphLabels = graphDetails.getGraphProperty().isLabelsDisplayed();
                        measureNamePosition = graphDetails.getGraphProperty().getMeasureNamePosition()== null? " ":graphDetails.getGraphProperty().getMeasureNamePosition();
                          showlyAxis=graphDetails.getGraphProperty().getShowlyAxis()== null ? " " :graphDetails.getGraphProperty().getShowlyAxis();
                            showryAxis=graphDetails.getGraphProperty().getShowryAxis()== null ? " " : graphDetails.getGraphProperty().getShowryAxis();
                            showxAxis=graphDetails.getGraphProperty().getShowxAxis()== null ? " " : graphDetails.getGraphProperty().getShowxAxis();
                            graphType = graphDetails.getGraphTypeName();
                            stackedType = graphDetails.getGraphProperty().getStackedType();
                            grprgbColorCode=(String[])graphDetails.getGraphProperty().getRgbColorArr();
                            transposeData=graphDetails.getGraphProperty().isTransposeData();


                    }
                    else{
                        if (singeGraphDetails != null) {
                            graphDisplayRows = (singeGraphDetails.get("graphDisplayRows") == null||singeGraphDetails.get("graphDisplayRows").toString().equalsIgnoreCase("")) ? "" : singeGraphDetails.get("graphDisplayRows").toString();
                            grpSize = (singeGraphDetails.get("graphSize") != null && !"null".equalsIgnoreCase(singeGraphDetails.get("graphSize").toString())) ? singeGraphDetails.get("graphSize").toString() : "Medium";
                            graphLegend = singeGraphDetails.get("graphLegend") == null ? "Y" : singeGraphDetails.get("graphLegend").toString();
                            graphGridLines = singeGraphDetails.get("graphGridLines") == null ? "Y" : singeGraphDetails.get("graphGridLines").toString();
                            showGT = singeGraphDetails.get("showGT") == null ? "Y" : singeGraphDetails.get("showGT").toString();
                            nbrFormat = singeGraphDetails.get("nbrFormat") == null ? " " : singeGraphDetails.get("nbrFormat").toString();
                            graphLegendLoc = singeGraphDetails.get("graphLegendLoc") == null ? "Bottom" : singeGraphDetails.get("graphLegendLoc").toString();
                            SwapColumn = singeGraphDetails.get("SwapColumn") == null ? "true" : singeGraphDetails.get("SwapColumn").toString();
                            graphSymbol = singeGraphDetails.get("graphSymbol") == null ? " " : singeGraphDetails.get("graphSymbol").toString();
                            graphType = singeGraphDetails.get("graphTypeName") == null ? " " : singeGraphDetails.get("graphTypeName").toString();
                            graphClass = singeGraphDetails.get("graphClassName") == null ? " " : singeGraphDetails.get("graphClassName").toString();
                            showMinMaxRange = singeGraphDetails.get("GraphProperty") == null ? false : ((GraphProperty)singeGraphDetails.get("GraphProperty")).getMinMaxRange();
                            targetRange = singeGraphDetails.get("targetRange") == null ? targetRangeTypes[0] : singeGraphDetails.get("targetRange").toString();
                            startValue = singeGraphDetails.get("startValue") == null ? "" : singeGraphDetails.get("startValue").toString();
                            endValue = singeGraphDetails.get("endValue") == null ? "" : singeGraphDetails.get("endValue").toString();
                            measureNamePosition=((GraphProperty)singeGraphDetails.get("GraphProperty")).getMeasureNamePosition()==null?" ":((GraphProperty)singeGraphDetails.get("GraphProperty")).getMeasureNamePosition();
                            showlyAxis=((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowlyAxis()== null ? " " : ((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowlyAxis();
                            showryAxis=((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowryAxis()== null ? " " : ((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowryAxis();
                            showxAxis=((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowxAxis()== null ? " " :((GraphProperty)singeGraphDetails.get("GraphProperty")).getShowxAxis();
                            graphLabels = singeGraphDetails.get("GraphProperty") == null ? false : ((GraphProperty)singeGraphDetails.get("GraphProperty")).isLabelsDisplayed();
                            stackedType = ((GraphProperty)singeGraphDetails.get("GraphProperty")).getStackedType() == null ? " " : ((GraphProperty)singeGraphDetails.get("GraphProperty")).getStackedType();
                            grpmeasureFormat=singeGraphDetails.get("measureFormat")==null?" ":singeGraphDetails.get("measureFormat").toString();
                            grpmeasureRounding=singeGraphDetails.get("measureValueRounding")==null?" ":singeGraphDetails.get("measureValueRounding").toString();
                            grplabelPosition=singeGraphDetails.get("axisLabelPosition")==null?" ":singeGraphDetails.get("axisLabelPosition").toString();
                            grpcalibration=singeGraphDetails.get("calibration")==null?" ":singeGraphDetails.get("calibration").toString();
                            grpfirstChartType=singeGraphDetails.get("firstChartType")==null?" ":singeGraphDetails.get("firstChartType").toString();
                            grpsecondChartType=singeGraphDetails.get("secondChartType")==null?" ":singeGraphDetails.get("secondChartType").toString();
                            grprgbColorCode=(String[])singeGraphDetails.get("rgbColorArr");
                            graphdrill=singeGraphDetails.get("GraphLocalDrill")== null ? false :(Boolean)singeGraphDetails.get("GraphLocalDrill") ;
                            colorSeries=((GraphProperty)singeGraphDetails.get("GraphProperty")).getColorSeries();
                            transposeData = singeGraphDetails.get("GraphProperty") == null ? false : ((GraphProperty)singeGraphDetails.get("GraphProperty")).isTransposeData();
                            colorGrouping = singeGraphDetails.get("colorGrouping")== null ? false :(Boolean)singeGraphDetails.get("colorGrouping") ;
                            yaxiscalibration=singeGraphDetails.get("yaxiscallibrationid")==null?" ":singeGraphDetails.get("yaxiscallibrationid").toString();
                            ischeckedLA=singeGraphDetails.get("ischeckedLA")==null?" ":singeGraphDetails.get("ischeckedLA").toString();
                          ischeckedRA=singeGraphDetails.get("ischeckedRA")==null?" ":singeGraphDetails.get("ischeckedRA").toString();
                          ischeckedXA=singeGraphDetails.get("ischeckedXA")==null?" ":singeGraphDetails.get("ischeckedXA").toString();
                          ischeckedYA=singeGraphDetails.get("ischeckedYA")==null?" ":singeGraphDetails.get("ischeckedYA").toString();

                            Flag=singeGraphDetails.get("Flag")==null?" ":singeGraphDetails.get("Flag").toString();
                            yaxisstart=singeGraphDetails.get("yaxisstart")==null?" ":singeGraphDetails.get("yaxisstart").toString();
                            yaxisend=singeGraphDetails.get("yaxisend")==null?" ":singeGraphDetails.get("yaxisend").toString();
                            yaxisinterval=singeGraphDetails.get("yaxisinterval")==null?" ":singeGraphDetails.get("yaxisinterval").toString();
                            xaxisstart=singeGraphDetails.get("xaxisstart")==null?" ":singeGraphDetails.get("xaxisstart").toString();
                            xaxisend=singeGraphDetails.get("xaxisend")==null?" ":singeGraphDetails.get("xaxisend").toString();
                            xaxisinterval=singeGraphDetails.get("xaxisinterval")==null?" ":singeGraphDetails.get("xaxisinterval").toString();
                            y2axisstart=singeGraphDetails.get("y2axisstart")==null?" ":singeGraphDetails.get("y2axisstart").toString();
                            y2axisend=singeGraphDetails.get("y2axisend")==null?" ":singeGraphDetails.get("y2axisend").toString();
                            y2axisinterval=singeGraphDetails.get("y2axisinterval")==null?" ":singeGraphDetails.get("y2axisinterval").toString();
                            yaxisrounding=singeGraphDetails.get("yAxisRounding")==null?" ":singeGraphDetails.get("yAxisRounding").toString();
                            xaxisrounding=singeGraphDetails.get("xAxisRounding")==null?" ":singeGraphDetails.get("xAxisRounding").toString();
                            y2axisrounding=singeGraphDetails.get("y2AxisRounding")==null?" ":singeGraphDetails.get("y2AxisRounding").toString();
                            targetvalue=singeGraphDetails.get("targetvalue")==null?" ":singeGraphDetails.get("targetvalue").toString();
                            tickdisplay=singeGraphDetails.get("tickdisplay")==null?" ":singeGraphDetails.get("tickdisplay").toString(); //kruthika
                            graphDisplayColumns=(singeGraphDetails.get("GraphdisplayCols") == null||singeGraphDetails.get("GraphdisplayCols").toString().equalsIgnoreCase("")) ? "" : singeGraphDetails.get("GraphdisplayCols").toString();
                            labeldirec=singeGraphDetails.get("labeldir")==null?" ":singeGraphDetails.get("labeldir").toString();
                            legendperrow=singeGraphDetails.get("legendsPerRow")==null?" ":singeGraphDetails.get("legendsPerRow").toString();
                            graphdisplay=singeGraphDetails.get("graphDisplay")==null?" ":singeGraphDetails.get("graphDisplay").toString();
                            graphtype1=singeGraphDetails.get("graphtype1")==null?" ":singeGraphDetails.get("graphtype1").toString();
                            graphtype2=singeGraphDetails.get("graphtype2")==null?" ":singeGraphDetails.get("graphtype2").toString();
                            selDrillType=singeGraphDetails.get("drilltype")==null?" ":singeGraphDetails.get("drilltype").toString();
                            targetcolor=singeGraphDetails.get("targetcolor")==null?"rgb(88,88,88)":singeGraphDetails.get("targetcolor").toString();
                            datebyinterval=  singeGraphDetails.get("datebyInterval")==null?" ":singeGraphDetails.get("datebyInterval").toString();
                            targettype=  singeGraphDetails.get("targetType")==null?" ":singeGraphDetails.get("targetType").toString();
                            appendXaxis=singeGraphDetails.get("appendXaxis")== null ? false :(Boolean)singeGraphDetails.get("appendXaxis") ;
                            legendAppend=singeGraphDetails.get("legendAppend")== null ? false :(Boolean)singeGraphDetails.get("legendAppend") ;
                            suprisezero=singeGraphDetails.get("zeroValues")== null ? false :(Boolean)singeGraphDetails.get("zeroValues") ;
                            tooltipXaxis=singeGraphDetails.get("tooltipXaxis")== null ? false :(Boolean)singeGraphDetails.get("tooltipXaxis") ;
                          JqplotGraphProperty graphproperty=new JqplotGraphProperty();
                              HashMap singleGraphDetails = (HashMap) container.getGraphHashMap();
                              PbReportViewerDAO reportViewerdao=new PbReportViewerDAO();

                              if (singleGraphDetails.get("jqgraphproperty" + graphId) != null) {
                                      graphproperty = (JqplotGraphProperty) singleGraphDetails.get("jqgraphproperty" + graphId);
                                  } else {
                                 graphproperty = reportViewerdao.getJqGraphDetails(graphId);
                                  }
                                  if (graphproperty != null) {
                                 selectedgraphtype = graphproperty.getSlectedGraphType(graphId);
                                      if (graphproperty.getSeriescolors() != null) {
                                          jqcolors = graphproperty.getSeriescolors();
                                                                 }
                                      jqgraphtype = graphproperty.getGraphTypename();
                                      graphdrill = graphproperty.isGraphDrill();
                                      transposeData = graphproperty.isTranspose();
                                      yaxiscalibration=graphproperty.getYaxiscalibration();
                                      ischeckedLA=graphproperty.getischeckedLA();
                                      ischeckedRA=graphproperty.getischeckedRA();
                                       ischeckedXA=graphproperty.getischeckedXA();
                                        ischeckedYA=graphproperty.getischeckedYA();
                                      Flag=graphproperty.getFlag();
                                      yaxisstart=graphproperty.getYaxisstart();
                                      yaxisend=graphproperty.getYaxisend();
                                      yaxisinterval=graphproperty.getYaxisinterval();
                                      xaxisstart=graphproperty.getXaxisstart();
                                      xaxisend=graphproperty.getXaxisend();
                                      xaxisinterval=graphproperty.getXaxisinterval();
                                      y2axisstart=graphproperty.getY2axisstart();
                                      y2axisend=graphproperty.getY2axisend();
                                      y2axisinterval=graphproperty.getY2axisinterval();
                                      yaxisrounding=""+graphproperty.getyAxisRounding();
                                      xaxisrounding=""+graphproperty.getxAxisRounding();
                                      y2axisrounding=""+graphproperty.getY2AxisRounding();
                                      isAdhocEnabled=graphproperty.isAdhocEnabled();
                                      targetvalue=graphproperty.getTargetValue();
                                      tickdisplay=graphproperty.gettickdisplay(); //krk
                                      graphDisplayColumns=graphproperty.getGraphDisplayCols();
                                      colorGrouping=graphproperty.isColorGrouping();
                                      legendperrow=""+graphproperty.getLegendsPerRow();
                                      labeldirec=""+graphproperty.getLabelDirection();
                                      graphdisplay=graphproperty.getGraphdisptype();
                                      graphtype1=graphproperty.getGraphtype1();
                                      graphtype2=graphproperty.getGraphtype2();
                                      targetcolor=graphproperty.getTargetColor();
                                      selDrillType=graphproperty.getDrillType();
                                      datebyinterval=graphproperty.getDatebyInterval();
                                      targettype=graphproperty.getTargetType();
                                      appendXaxis=graphproperty.isAppendXaxis();
                                      legendAppend=graphproperty.isLegenaAppend();
                                      tooltipXaxis=graphproperty.istooltipXaxis();
                                      suprisezero=graphproperty.isSupriseZero();
                                       if (graphDisplayColumns == null ||graphDisplayColumns.equals("null") || graphDisplayColumns.equals("")){
                                             graphDisplayColumns = "";
                                      graphClass=null;
                                      }

                              }

                                        if(yaxiscalibration.equalsIgnoreCase("Custom")){
                                          customdisplay="";
                        }
                        }

                    }
                    int userId=Integer.parseInt(session.getAttribute("USERID").toString());
                        //targetRange = "Range";
                        //.println("graphGridLines in properties==="+graphGridLines);
                    ArrayList viewbylist = (ArrayList) container.getViewByElementIds();
                    ArrayList viewbylistNames = (ArrayList) container.getViewByColNames();

                    if (singeGraphDetails != null) {
                        String contextpath=request.getContextPath();

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Graph Properties</title>
        <style type="text/css">
            *{font : 11px verdana}
        </style>
          <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=contextpath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
         <link type="text/css" href="<%=contextpath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <link type="text/css" href="<%=contextpath%>/stylesheets/themes/<%=themeColor %>/metadataButton.css" rel="stylesheet" />
        <script type="text/javascript" src="<%=contextpath%>/jQuery/farbtastic12/farbtastic/farbtastic.js"></script>
        <link rel="stylesheet" href="<%=contextpath%>/jQuery/farbtastic12/farbtastic/farbtastic.css" type="text/css" />
      
    </head>
    <body>
        <form action="" name="grpProForm" method="post">
            <input type="hidden" name="gid" value="<%=graphId%>">
            <input type="hidden" name="startValue" value="" id="startValue">
            <Table  width="100%" border="0">
                <tbody>

                    <Tr>

                        <Td>Legends</Td>
                        <Td>
                            <%if (graphLegend.equalsIgnoreCase("Y")) {%>
                            <input type="checkbox" checked name="showLegends" id="showLegends" value="Y" onclick="showLegendsFun();">
                            <%} else {%>
                            <input type="checkbox" name="showLegends" id="showLegends" value="N" onclick="showLegendsFun();">
                            <%}%>
                        </Td>

                        <Td>GridLines</Td>
                        <Td>
                            <%if (graphGridLines.equalsIgnoreCase("Y")) {%>
                            <input type="checkbox" checked name="graphGridLines" id="graphGridLines" value="Y" onclick="showGridLinesFun();">
                            <%} else {%>
                            <input type="checkbox" name="graphGridLines" id="graphGridLines" value="N" onclick="showGridLinesFun();">
                            <%}%>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td>Number Format</Td>
                        <Td>
                            <select name="nbrFormat" id="nbrFormat" style="width:150px">
                                <%
                        for (int i = 0; i < grpNbrFormats.length; i++) {
                            if (nbrFormat.equalsIgnoreCase(grpNbrFormats[i])) {%>
                                <option selected value="<%=grpNbrFormats[i]%>"><%=grpNbrFormatsDisp[i]%></option>
                                <%} else {%>
                                <option value="<%=grpNbrFormats[i]%>"><%=grpNbrFormatsDisp[i]%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                        <Td>Legend Location</Td>
                        <Td>
                            <select name="graphLegendLoc" id="graphLegendLoc" style="width:150px">
                                <%for (String grpLegend : grpLegends) {
                            if (graphLegendLoc.equalsIgnoreCase(grpLegend)) {%>
                                <option selected value="<%=grpLegend%>"><%=grpLegend%></option>
                                <%} else {%>
                                <option value="<%=grpLegend%>"><%=grpLegend%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                    </Tr>
                     <% if(selectedgraphtype==null || !selectedgraphtype.equalsIgnoreCase("jq")) {%>
                    <Tr>
                        <Td>Swap Graph Analysis</Td>
                        <Td>
                            <select name="SwapColumn" id="SwapColumn" style="width:150px">
                                <%for (int i = 0; i < SwapColumnArray.length; i++) {
                            if (SwapColumn.equalsIgnoreCase(SwapColumnArrayValues[i])) {%>
                                <option selected value="<%=SwapColumnArrayValues[i]%>"><%=SwapColumnArray[i]%></option>
                                <%} else {%>
                                <option  value="<%=SwapColumnArrayValues[i]%>"><%=SwapColumnArray[i]%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                        <Td>Symbols</Td>
                        <Td>
                            <select name="graphSymbol" id="graphSymbol" style="width:150px">
                                <%for (String str : graphSymbols) {
                            if (graphSymbol.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                    </Tr>
                    <%}%>


                    <Tr style="display:none">
                        <Td>Graph Sizes</Td>
                        <Td>
                            <select name="grpSize" id="grpSize" style="width:150px">
                                <%
                        for (int i = 0; i < grpSizeskeys.length; i++) {
                            if (grpSize.equalsIgnoreCase(graphSizesArray[i])) {%>
                                <option selected value="<%=graphSizesArray[i]%>"><%=graphSizesArray[i]%></option>
                                <%} else {%>
                                <option value="<%=graphSizesArray[i]%>"><%=graphSizesArray[i]%></option>
                                <%}
                        }%>
                            </select>
                        </Td>
                    </Tr>
                    <Tr>
                        <Td>Display Rows</Td>
                        <Td>
                              <input type="text" name="graphDisplayRows" id="graphDisplayRows" value="<%=graphDisplayRows%>"  size=6>
         <%--                   <select name="graphDisplayRows" id="graphDisplayRows" style="width:150px">
                                <%
                        for (int i = 0; i < displayGraphRows.length; i++) {
                            if (graphDisplayRows.equalsIgnoreCase(displayGraphRows[i])) {%>
                                <option selected value="<%=displayGraphRows[i]%>"><%=displayGraphRows[i]%></option>
                                <%} else {%>
                                <option value="<%=displayGraphRows[i]%>"><%=displayGraphRows[i]%></option>
                                <%}
                        }%>
                            </select>--%><br>
                      Please type 'All' for AllRows
                        </Td>
                        <td>Display columns</td>
                        <td>
                              <input type="text" name="graphDisplayCols" id="graphDisplayCols" value="<%=graphDisplayColumns%>"  size=6>
                           <%-- <select name="graphDisplayCols" id="graphDisplayCols" style="width:150px">
                          <%   for (int i = 0; i < displayGraphRows.length; i++) {
                              if (graphDisplayColumns.equalsIgnoreCase(displayGraphRows[i])) {%>
                                <option selected value="<%=displayGraphRows[i]%>"><%=displayGraphRows[i]%></option>
                                <%} else {%>
                            <option value="<%=displayGraphRows[i]%>"><%=displayGraphRows[i]%></option>
                            <%}%>
                            <%}%>
                            </select>--%><br>
                      Please type 'All' for AllColumns

                        </td>
                    </Tr>
                    <Tr>
                        <Td>Show Labels</Td>
                        <Td>
                            <%if(graphLabels){%>
                            <input type="checkbox" checked name="showLabels" id="showLabels" value="true" onclick="showLabelsFun();">
                            <%} else {%>
                            <input type="checkbox" name="showLabels" id="showLabels" value="false" onclick="showLabelsFun();">
                            <%}%>
                        </Td>
                    <%--   <%} if(PrivilegeManager.isComponentEnabledForUser("REPORT", "MEASURENAMEPOSITION", userId)){%>
                        <Td>Measure Name Position</Td>
                         <Td>
                            <select name="measureNamePosition" id="measureNamePosition" style="width:150px">
                                <%for (String str : measureNamePositionArray) {
                            if (measureNamePosition.equalsIgnoreCase(str)) {%>
                                <option selected value="<%=str%>"><%=str%></option>
                                <%} else {%>
                                <option value="<%=str%>"><%=str%></option>
                                <%}
                        }%>
                            </select>
</Td> --%>
                    <%-- <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie") && !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%> --%>
                     <%if ( !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%>
                        <Td>ShowLeftY-Axis Label </Td>
                         <Td>
                             <input type="text" name="showlyAxis" value="<%=showlyAxis%>" id="showlyAxis">
                        </Td>
                    </Tr>
                        <%}%>
                    <Tr>

                      <%-- <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie") && !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%> --%>
                     <%if ( !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%>
                        <Td>ShowRightY-Axis Label</Td>
                        <Td>
                            <input type="text" name="showryAxis" value="<%=showryAxis %>" id="showryAxis">
                        </Td>
                        <%}%>
                    <%-- <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie") && !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%> --%>
                     <%if ( !jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%>
                        <Td>ShowX-Axis Label</Td>
                       <Td>
                            <input type="text" name="showxAxis" value="<%=showxAxis%>" id="showxAxis">
                        </Td>
                    </Tr>
                    <%}%>

<!--                     <Tr>
                        <Td>Show Overallmin</Td>
                        <Td>
                            <input type="checkbox"  name="showOverAllMin" id="showOverAllMin" value="true" onclick="showOverAllMinFun();">
                        </Td>
                        <Td>Show OverallMax</Td>
                        <Td>
                            <input type="checkbox"  name="showOverAllMax" id="showOverAllMax" value="true" onclick="showOverAllMaxFun();">
                        </Td>
                     </Tr>
                     <Tr>
                        <Td>Show CategoryMin</Td>
                        <Td>
                            <input type="checkbox"  name="showCategoryMin" id="showCategoryMin" value="true" onclick="showCategoryMinFun();">
                        </Td>
                        <Td>Show CategoryMax</Td>
                        <Td>
                            <input type="checkbox"  name="showCategoryMax" id="showCategoryMax" value="true" onclick="showCategoryMaxFun();">
                        </Td>
                     </Tr>-->

                    <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie") && from==null) {%>
<!--                    <Tr id="targetRow">
                        <Td>Target Type</Td>
                        <Td>
                            <select name="targetRange" id="targetRange" style="width:150px" onchange="changeTargetRange(this)">
                                <%
    for (int i = 0; i < targetRangeTypes.length; i++) {
        if (targetRange.equalsIgnoreCase(targetRangeTypes[i])) {%>
                                <option selected value="<%=targetRangeTypes[i]%>"><%=targetRangeTypes[i]%></option>
                                <%} else {%>
                                <option value="<%=targetRangeTypes[i]%>"><%=targetRangeTypes[i]%></option>
                                <%}
    }%>
                            </select>
                        </Td>
                    </Tr>-->

<!--                    <Tr id="discreteRow">
                        <Td>Target Value</Td>
                        <Td>
                            <input type="text" name="startValue1" value="<%=startValue%>" onkeyup="NumericOnly(this,event)" id="startValue1">
                        </Td>
                    </Tr>-->
   <% if(selectedgraphtype==null || !selectedgraphtype.equalsIgnoreCase("jq")) {%>
                    <Tr id="rangeRow">
                        <Td>Target Min</Td>
                        <Td>
                            <input type="text" name="startValue2"  value="<%=startValue%>" onkeyup="NumericOnly(this,event)" id="startValue2">
                        </Td>
                        <Td>Target Max</Td>
                        <Td>
                            <input type="text" name="endValue"  value="<%=endValue%>"  onkeyup="NumericOnly(this,event)" id="endValue">
                        </Td>
                    </Tr>
<%}%>


                <script type="text/javascript">
                    changeTargetRange(document.getElementById("targetRange"));
                </script>

                <%}%>
                <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie") && (graphType.equalsIgnoreCase("StackedArea") || graphType.equalsIgnoreCase("stacked") || graphType.equalsIgnoreCase("stacked3d") || graphType.equalsIgnoreCase("HorizontalStacked") || graphType.equalsIgnoreCase("HorizontalStacked3D") )){%>

                <Tr>
                        <TD>Stacked Graph Type :</TD>
                        <Td><%if (stackedType.equalsIgnoreCase("absStacked")) {%>
                            <input type="radio" name="absStacked" checked value="absStacked" id="absStacked" onclick="showChecked()">AbsoluteStackGraph
                        <%} else {%>
                            <input type="radio" name="absStacked"  value="absStacked" id="absStacked" onclick="showChecked()">AbsoluteStackGraph
                        <%}%></Td>
                        <Td><%if (stackedType.equalsIgnoreCase("prcStacked")) {%>
                            <input type="radio" name="absStacked" checked value="prcStacked" id="prcStacked" onclick="showChecked()">%_WiseStackGraph
                        <%} else {%>
                            <input type="radio" name="absStacked"  value="prcStacked" id="prcStacked" onclick="showChecked()">%_WiseStackGraph
                        <%}%></Td>
                    </Tr>
                <%}%>
                <tr>
                    <%if ((graphClass != null && graphClass.equalsIgnoreCase("Pie"))||(jqgraphtype!=null && (jqgraphtype.equalsIgnoreCase("pie")||jqgraphtype.equalsIgnoreCase("pie-Empty")||jqgraphtype.equalsIgnoreCase("donut-single")||jqgraphtype.equalsIgnoreCase("Donut-Double") || jqgraphtype.equalsIgnoreCase("Funnel") || jqgraphtype.equalsIgnoreCase("Funnel(INV)") || jqgraphtype.equalsIgnoreCase("ColumnPie")))) {%>
                <td>Measure Format</td>
                    <td>
                        <select name="measureFormat" id="measureFormat" style="width:150px">
                            <% for(int i=0;i<measureFomat.length;i++){
                                if(grpmeasureFormat.equalsIgnoreCase(measureFomat[i])){%>
                                <option selected value="<%=measureFomat[i]%>"><%=measureFomat[i]%></option>
                                <%} else{ %>
                                <option value="<%=measureFomat[i]%>"><%=measureFomat[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                   <%}%>
                   <%if(from== null){%>
                    <td>Rounding</td>
                    <td>
                        <select name="measureRound" id="measureRound" style="width:150px" >
                            <%for(int i=0;i<rounddisp.length;i++){
                                if(grpmeasureRounding!=null &&  grpmeasureRounding.equalsIgnoreCase(round[i])){%>
                            <option selected value="<%=round[i]%>"><%=rounddisp[i]%></option>
                              <% }else{%>
                              <option value="<%=round[i]%>"><%=rounddisp[i]%></option>
                              <%}
                            }%>
                        </select>
                        </td>
                        <%}%>
<!--                        krk-->
                        <%if (!jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("Funnel") && !jqgraphtype.equalsIgnoreCase("Funnel(INV)") && !jqgraphtype.equalsIgnoreCase("ColumnPie") && !jqgraphtype.equalsIgnoreCase("Bar-Horizontal") &&    !jqgraphtype.equalsIgnoreCase("StackedBar(H)")) {%>
                <td>Y-Axis Rounding</td>
                    <td>
                        <select name="yAxisRounding" id="yAxisRounding" style="width:150px">
                            <% for(int i=0;i<yaxisrounddisp.length;i++){
                                if(yaxisrounding.equalsIgnoreCase(yaxisround[i])){%>
                                <option selected value="<%=yaxisround[i]%>"><%=yaxisrounddisp[i]%></option>
                                <%} else{ %>
                                <option value="<%=yaxisround[i]%>"><%=yaxisrounddisp[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                   <%}%>
<!--                   krk-->
                        <%if (  jqgraphtype.equalsIgnoreCase("Bar-Horizontal")  ||  jqgraphtype.equalsIgnoreCase("StackedBar(H)")) {%>
                    <td>Y-Axis Rounding</td>
                       <td>
                        <select name="xAxisRounding" id="xAxisRounding" style="width:150px">
                            <% for(int i=0;i<xaxisrounddisp.length;i++){
                                if(xaxisrounding.equalsIgnoreCase(xaxisround[i])){%>
                                <option selected value="<%=xaxisround[i]%>"><%=xaxisrounddisp[i]%></option>
                                <%} else{ %>
                                <option value="<%=xaxisround[i]%>"><%=xaxisrounddisp[i]%></option>
                            <%}
                            }%>
                        </select>
                         </td>
                      <%}%>
                </tr>
                  <% if(selectedgraphtype==null || !selectedgraphtype.equalsIgnoreCase("jq")) {%>
                 <%if (graphClass != null && !graphClass.equalsIgnoreCase("Pie")) {%>
                <tr>
                    <%if(from== null){%>
                    <td>Axis Label Position</td>
                    <td>
                        <select name="axisLabelPosition" id="axisLabelPosition" style="width:150px">
                            <%for(int i=0;i<labelPosition.length;i++){
                                if(grplabelPosition.equalsIgnoreCase(labelPosition[i])){%>
                            <option selected value="<%=labelPosition[i]%>"><%=labelPosition[i]%></option>
                            <%} else{%>
                            <option value="<%=labelPosition[i]%>"><%=labelPosition[i]%></option>
                            <%}
                                }%>
                        </select>
                    </td>
                    <%}%>
                    <%if(from== null){%>
                    <td>Calibration</td>
                    <td>
                        <select name="calibration" id="calibration" style="width:150px">
                           <%for(int i=0;i<calibration.length;i++){
                               if(grpcalibration.equalsIgnoreCase(calibration[i])){%>
                            <option selected value="<%=calibration[i]%>"><%=calibration[i]%></option>
                            <%} else{%>
                            <option value="<%=calibration[i]%>"><%=calibration[i]%></option>
                            <%}
                           }%>
                        </select>
                    </td>
                     <%}%>
                </tr>
               <%}%>
               <%}%>
                <% if(selectedgraphtype==null || !selectedgraphtype.equalsIgnoreCase("jq")) {%>
               <%if(from== null){%>
               <%if(graphType.equalsIgnoreCase("Dual Axis") || graphType.equalsIgnoreCase("OverlaidArea") || graphType.equalsIgnoreCase("OverlaidBar")){%>
                <tr>
                    <td>FirstChart Series</td>
                    <td>
                        <select name="firstChartType" id="firstChartType" style="width:150px">
                            <%for(int i=0;i<firstChartTypeArray.length;i++){
                                if(grpfirstChartType.equalsIgnoreCase(firstChartTypeArray[i])){%>
                                <option selected value="<%=firstChartTypeArray[i]%>"><%=firstChartTypeArray[i]%></option>
                                <%} else{%>
                            <option value="<%=firstChartTypeArray[i]%>"><%=firstChartTypeArray[i]%></option>
                            <%} }%>
                        </select>
                    </td>
                    <td>SecondChart Series</td>
                    <td>
                        <select name="secondChartType" id="secondChartType" style="width:150px">
                            <%for(int i=0;i<secondChartTypeArray.length;i++){
                                if(grpsecondChartType.equalsIgnoreCase(secondChartTypeArray[i])){%>
                                <option selected value="<%=secondChartTypeArray[i]%>"><%=secondChartTypeArray[i]%></option>
                            <%} else{%>
                            <option value="<%=secondChartTypeArray[i]%>"><%=secondChartTypeArray[i]%></option>
                            <%} }%>
                        </select>
                    </td>
                </tr>
                <%}%>
                <%}%>
                <%}%>
                <tr>
                    <td>BackGround Color</td>
                    <td>
                        <%if(grprgbColorCode!=null && grprgbColorCode.length>0){%>
                        <input  name="colorCodes" id="colorCodes" type="text" onclick="showColor(this.id)" value="" style="width:50px;cursor:pointer;background-color:rgb(<%=Integer.parseInt(grprgbColorCode[0].trim())%>,<%=Integer.parseInt(grprgbColorCode[1].trim())%>,<%=Integer.parseInt(grprgbColorCode[2].trim())%>)" colorCode=''>
                        <%} else{%>
                        <input  name="colorCodes" id="colorCodes" type="text" onclick="showColor(this.id)" value="" style='width:50px;cursor:pointer' colorCode=''>
                        <%}%>
                    </td>
<!--                    krk-->
                    <%if (jqgraphtype!=null && (jqgraphtype.equalsIgnoreCase("Bubble") || jqgraphtype.equalsIgnoreCase("Scatter") || jqgraphtype.equalsIgnoreCase("Scatter(Regression)") || jqgraphtype.equalsIgnoreCase("Scatter(Partial)") ||  !jqgraphtype.equalsIgnoreCase("Bar-Horizontal") ||  !jqgraphtype.equalsIgnoreCase("StackedBar(H)"))) {%>
                <td>X-Axis Rounding</td>
                    <td>
                        <select name="xAxisRounding" id="xAxisRounding" style="width:150px">
                            <% for(int i=0;i<xaxisrounddisp.length;i++){
                                if(xaxisrounding.equalsIgnoreCase(xaxisround[i])){%>
                                <option selected value="<%=xaxisround[i]%>"><%=xaxisrounddisp[i]%></option>
                                <%} else{ %>
                                <option value="<%=xaxisround[i]%>"><%=xaxisrounddisp[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                   <%}%>
<!--                   //krk-->
                    <%if (  jqgraphtype.equalsIgnoreCase("Bar-Horizontal") ||  jqgraphtype.equalsIgnoreCase("StackedBar(H)") ) {%>

                    <td>X-Axis Rounding</td>
                    <td>
                        <select name="yAxisRounding" id="yAxisRounding" style="width:150px">
                            <% for(int i=0;i<yaxisrounddisp.length;i++){
                                if(yaxisrounding.equalsIgnoreCase(yaxisround[i])){%>
                                <option selected value="<%=yaxisround[i]%>"><%=yaxisrounddisp[i]%></option>
                                <%} else{ %>
                                <option value="<%=yaxisround[i]%>"><%=yaxisrounddisp[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                     <%}%>
                </tr>

                <tr>
                    <td> set x-axis interval</td>
                    <td> <input type="text" name="tickdisplay" id="tickdisplay" value="<%=tickdisplay%>"  size=6> </td>
                </tr>
                <Tr>
                        <td align="left" colspan="2" height="10px"> Please do not Type number more than 10</td>
                </Tr>
<!--                //krk-->
                    <% if(selectedgraphtype==null || !selectedgraphtype.equalsIgnoreCase("jq")) {%>
                 <tr id="colorRow">
                     <td rowspan="2">Series 1:</td>
                        <td colspan="3" align="center"><table align="left"><tr width="100%">
                        <%for(int k=0;k<10;k++) { %>
                        <td>Color<%=k%>:
                            <select id="colorSelect<%=k%>" name="colorSelect<%=k%>" style="width:80%" onChange="this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor">
                                <% Iterator codeIter=colorCodeSet.iterator();
                                String isSelected="";
                                int selectedCount=0;

                                    if(colorSeries != null && colorSeries.length >= (k+1) && colorSeries[k] != null){

                                            while(codeIter.hasNext()){
                                                isSelected="";
                                                String rgb=codeIter.next().toString();
                                                String clr=colorCodeMap.get(rgb).toString();
                                            if( selectedColorsMap.get(colorSeries[k]) == null  ){

                                                if(rgb.equalsIgnoreCase(colorSeries[k]) && selectedCount==0){
                                                    isSelected="selected";
                                                    selectedCount++;
                                                    selectedColorsMap.put(rgb,k);

                                    %>
<!--                                                    <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>" <%=isSelected%> > <%=clr%> </option>-->
                                                 <% }}else{
                                                     if(Integer.parseInt(selectedColorsMap.get(colorSeries[k]).toString()) !=k &&rgb.equalsIgnoreCase(colorSeries[k]) && selectedCount==0){
                                                        isSelected="selected";
                                                        selectedCount++;
                                                        selectedColorsMap.put(rgb,k);}%>
<!--                                                 <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>"> <%=clr%> </option>-->
                                <% } %>
                                                    <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>" <%=isSelected%> > <%=clr%> </option>
                                                 <%



                                                }

                                    }
                                    else{

                                         while(codeIter.hasNext()){
                                                isSelected="";
                                                String rgb=codeIter.next().toString();
                                                String clr=colorCodeMap.get(rgb).toString();
                                            if( selectedColorsMap.get(colors[k][0]) == null  ){

                                                if(rgb.equalsIgnoreCase(colors[k][0]) && selectedCount==0){
                                                    isSelected="selected";
                                                    selectedCount++;
                                                    selectedColorsMap.put(rgb,k);
                                                    }
                                    %>
<!--                                                    <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>" <%=isSelected%> > <%=clr%> </option>-->
                                                 <% }else{
                                                    if(Integer.parseInt(selectedColorsMap.get(colors[k][0]).toString()) !=k &&rgb.equalsIgnoreCase(colors[k][0]) && selectedCount==0){
                                                        isSelected="selected";
                                                        selectedCount++;
                                                        selectedColorsMap.put(rgb,k);
                                                    }
                                                    %>
<!--                                                 <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>"> <%=clr%> </option>-->
                                                <% }%>
                                                    <option style="background-color:<%=colorCodeMap.get(rgb)%>;" value="<%=rgb%>" <%=isSelected%> > <%=clr%> </option>
                                                 <%



                                                }

                                    }

                                    %>


                                <% if(selectedCount ==0){ %>
                                <option  value="Default" selected="selected" > Default </option>
                            </select>
                        </td><% }if(k==5){%> </tr> <tr width="100%"> <% }} %>
                        </tr></table></td>
                    </tr>

                  <%}%>
                   <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && from==null) {%>
                   <tr>
                       <!--<td align="left" height="10px" colspan="">AdhocDrill</td>-->
         <%--              <%if(isAdhocEnabled){%>
                       <td><input checked type="checkbox" onclick="adhocDillConversion()" id="AdhocDrill" name="AdhocDrill"></td>
                        <% }else{%>
                       <td><input type="checkbox" onclick="adhocDillConversion()" id="AdhocDrill" name="AdhocDrill"></td>
                       <%}%> --%>
                            <td>DrillType</td>
                    <td>
                        <select name="drilltype" id="drilltype" style="width:150px">
                            <% for(int i=0;i<drillType.length;i++){
                                if(selDrillType.equalsIgnoreCase(drillType[i])){%>
                                <option selected value="<%=drillType[i]%>"><%=drillType[i]%></option>
                                <%} else{ %>
                                <option value="<%=drillType[i]%>"><%=drillType[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                      <td>ColorGrouping</td>
                       <td>
                            <%if(colorGrouping){%>
                            <input type="checkbox" checked name="colorGrouping" id="colorGrouping" value="true" onclick="showColorGrouping();">
                            <%} else {%>
                            <input type="checkbox" name="colorGrouping" id="colorGrouping" value="false" onclick="showColorGrouping();">
                            <%}%>
                       </td>
                   </tr>
                   <tr>
                       <td>Enable Graph Drill:</td>
                       <td>
                            <%if(graphdrill){%>
                            <input type="checkbox" checked name="showLocaldrill" id="showLocaldrill" value="true" onclick="showLocalDrill();">
                            <%} else {%>
                            <input type="checkbox" name="showLocaldrill" id="showLocaldrill" value="false" onclick="showLocalDrill();">
                            <%}%>
                       </td>
                       <td>Show Others</td>
                       <td>
                             <%if(container.isOthersRequired()){%>
                            <input type="checkbox" checked name="showOthers" id="showOthers" value="">
                            <%} else {%>
                            <input type="checkbox" name="showOthers" id="showOthers" value="">
                            <%}%>
                       </td>

                                  <%if (jqgraphtype!=null &&  jqgraphtype.equalsIgnoreCase("DualAxis(Bar-Line)")) {%>
                   <td>y2-Axis Rounding</td>
                    <td>
                        <select name="y2AxisRounding" id="y2AxisRounding" style="width:150px">
                            <% for(int i=0;i<y2axisrounddisp.length;i++){
                                if(y2axisrounding.equalsIgnoreCase(y2axisround[i])){%>
                                <option selected value="<%=y2axisround[i]%>"><%=y2axisrounddisp[i]%></option>
                                <%} else{ %>
                                <option value="<%=y2axisround[i]%>"><%=y2axisrounddisp[i]%></option>
                            <%}
                            }%>
                        </select>
                    </td>
                   <%}%>
                   </tr>
                   <tr></tr>
                   <tr>
                       <td>Transpose</td><td>
                       <%if(transposeData){%>
                       <input type="checkbox" checked name="transposeData" id="transposeData" value="true" onclick="showtransposeData();">
                       <%} else {%>
                       <input type="checkbox" name="transposeData" id="transposeData" value="false" onclick="showtransposeData();" >
                       <%}%>
                       </td>
                       <td>Target</td>
                       <td>
                            <input type="text" name="targetValue" id="targetValue" value="<%=targetvalue%>"  size=9>
                            <select name="targetType" id="targetType" style="width:75px">
                            <%for(int i=0;i<targetType.length;i++){
                                if(targettype!=null &&  targettype.equalsIgnoreCase(targetType[i])){%>
                            <option selected value="<%=targetType[i]%>"><%=targetType[i]%></option>
                              <% }else{%>
                              <option value="<%=targetType[i]%>"><%=targetType[i]%></option>
                              <%}
                            }%>
                          </select>
                                <%if(targetcolor!=null && !targetcolor.isEmpty()){%>
                                <input  name="targetColorcode" id="targetColorcode" type="text" onclick="showTargetColor(this.id)" value="" style="width:20px;cursor:pointer;background-color:<%=targetcolor%>" colorCode=''>
                        <%} else{%>
                            <input  name="targetColorcode" id="targetColorcode" type="text" onclick="showTargetColor(this.id)" value="" style='width:20px;cursor:pointer' colorCode=''>
                       <%}%>
                       </td>
                       </tr>
                   <tr></tr>
                   <tr></tr>
                   <%if(container.isReportCrosstab()){%>
                    <td>CrossTab Legend append</td><td>
                       <%if(legendAppend){%>
                       <input type="checkbox" checked name="appendLegend" id="appendLegend" value="true" onclick="legendAppend();">
                       <%} else {%>
                       <input type="checkbox" name="appendLegend" id="appendLegend" value="false" onclick="legendAppend();" >
                       <%}%>
                    </td>
                   <%}if(container.getViewByCount()>=2){%>
                   <td>Append X-axis</td><td>
                       <%if(appendXaxis){%>
                       <input type="checkbox" checked name="appendXaxis" id="appendXaxis" value="true" onclick="xaxisAppend()">
                       <%} else {%>
                       <input type="checkbox" name="appendXaxis" id="appendXaxis" value="false" onclick="xaxisAppend()" >
                       <%}%>
                       </td>
                       <%}%>
                      <td>Suppress Zero Values</td><td>
                       <%if(suprisezero){%>
                       <input type="checkbox" checked name="zeroValues" id="zeroValues" value="true" onclick="zeroValuesSuprise();">
                       <%} else {%>
                       <input type="checkbox" name="zeroValues" id="zeroValues" value="false" onclick="zeroValuesSuprise();" >
                       <%}%>
                       </td>
                   <tr>
                        <%if(container.isReportCrosstab()){%>
                    <td>Tooltip X-Axis</td><td>
                       <%if(tooltipXaxis){%>
                       <input type="checkbox" checked name="tooltipx" id="tooltipx" value="true" onclick="Xaxistooltip();">
                       <%} else {%>
                       <input type="checkbox" name="tooltipx" id="tooltipx" value="false" onclick="Xaxistooltip();" >
                       <%}%>
                    </td>
                   <%}%>

                   </tr>
                   <%if (!jqgraphtype.equalsIgnoreCase("pie") && !jqgraphtype.equalsIgnoreCase("pie-Empty") && !jqgraphtype.equalsIgnoreCase("donut-single") && !jqgraphtype.equalsIgnoreCase("Donut-Double") && !jqgraphtype.equalsIgnoreCase("Funnel") && !jqgraphtype.equalsIgnoreCase("Funnel(INV)") && !jqgraphtype.equalsIgnoreCase("ColumnPie")) {%>
                   <tr>
                     <td rowspan="2">Y-Axis Display</td>
                        <td>
                        <select name="yaxiscallibrationid" id="yaxiscallibrationid" style="width:150px" onchange="yaxiscallibration()" >
                            <%for(int i=0;i<yaxiscal.length;i++){
                                if(yaxiscalibration!=null &&  yaxiscalibration.equalsIgnoreCase(yaxisval[i])){%>
                            <option selected value="<%=yaxisval[i]%>"><%=yaxiscal[i]%></option>
                              <% }else{%>
                              <option value="<%=yaxisval[i]%>"><%=yaxiscal[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                       <td>X-axisLabel Direction</td>
                       <td>
                            <select name="labelDir" id="labelDir" style="width:150px">
                            <%for(int i=0;i<labelDirec.length;i++){
                                if(labeldirec!=null &&  labeldirec.equalsIgnoreCase(labelDirecval[i])){%>
                            <option selected value="<%=labelDirecval[i]%>"><%=labelDirec[i]%></option>
                              <% }else{%>
                              <option value="<%=labelDirecval[i]%>"><%=labelDirec[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>


                   </tr>
                   <tr rowspan="1">
                       <td rowspan="1">    <table id="customid" style="display:<%=customdisplay%>">
                                <tr>
                                     <Td>Y-AxisStart</Td>
                                 <Td>
                                     <input type="text" name="yaxisstart" id="yaxisstart" value="<%=yaxisstart%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                          <Td>Y-AxisEnd</Td>
                                 <Td>
                                     <input type="text" name="yaxisend" id="yaxisend" value="<%=yaxisend%>"  style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                                <Td>Y-Calibration</Td>
                                 <Td>
                                     <input type="text" name="yaxiscalibration" id="yaxiscalibration" value="<%=yaxisinterval%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                  <% if(jqgraphtype.equalsIgnoreCase("Bubble") || jqgraphtype.equalsIgnoreCase("Scatter") || jqgraphtype.equalsIgnoreCase("Scatter(Regression)")){%>
                                <tr>     <Td>X-AxisStart</Td>
                                 <Td>
                                     <input type="text" name="xaxisstart" id="xaxisstart" value="<%=xaxisstart%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                          <Td>X-AxisEnd</Td>
                                 <Td>
                                     <input type="text" name="xaxisend" id="xaxisend" value="<%=xaxisend%>"  style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                                <Td>X-Calibration</Td>
                                 <Td>
                                     <input type="text" name="xaxiscalibration" id="xaxiscalibration" value="<%=xaxisinterval%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                  </Td>
                                </tr>
                   <%}%>
                                <% if (jqgraphtype.equalsIgnoreCase("DualAxis(Bar-Line)")) {%>
                                <tr>     <Td>Y2-AxisStart</Td>
                                    <Td>
                                        <input type="text" name="y2axisstart" id="y2axisstart" value="<%=y2axisstart%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                    </Td>
                                    <Td>Y2-AxisEnd</Td>
                                    <Td>
                                        <input type="text" name="y2axisend" id="y2axisend" value="<%=y2axisend%>"  style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                    </Td>
                                    <Td>Y2-Calibration</Td>
                                    <Td>
                                        <input type="text" name="y2axiscalibration" id="y2axiscalibration" value="<%=y2axisinterval%>" style="WIDTH: 67px; HEIGHT: 22px" size=11>
                                    </Td>
                                </tr>
                                <%}%>
                            </table></td>
                    </tr>
                   <%}%>
                    <%}%>
                   <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq")) {%>
                   <tr>
                       <td>Legends Per Row</td>
                      <td>
                            <select name="legendsPerRow" id="legendsPerRow" style="width:150px">
                            <%for(int i=0;i<legendsPerRow.length;i++){
                                if(legendperrow!=null &&  legendperrow.equalsIgnoreCase(legendsPerRow[i])){%>
                            <option selected value="<%=legendsPerRow[i]%>"><%=legendsPerRow[i]%></option>
                              <% }else{%>
                              <option value="<%=legendsPerRow[i]%>"><%=legendsPerRow[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                      <td>Graph Display</td>
                      <td>
                            <select name="graphDisp" id="graphDisp" style="width:150px">
                            <%for(int i=0;i<graphdisplaytype.length;i++){
                                if(graphdisplay!=null &&  graphdisplay.equalsIgnoreCase(graphdisplaytype[i])){%>
                            <option selected value="<%=graphdisplaytype[i]%>"><%=graphdisplaytype[i]%></option>
                              <% }else{%>
                              <option value="<%=graphdisplaytype[i]%>"><%=graphdisplaytype[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                   </tr>
                   <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && (jqgraphtype.equalsIgnoreCase("overlaid(Bar-Line)") || jqgraphtype.equalsIgnoreCase("DualAxis(Bar-Line)"))) {%>
                    <tr>
                       <td>GraphType1</td>
                      <td>
                            <select name="graphType1" id="graphType1" style="width:150px">
                            <%for(int i=0;i<graphType1.length;i++){
                                if(graphtype1!=null &&  graphtype1.equalsIgnoreCase(graphType1[i])){%>
                            <option selected value="<%=graphType1[i]%>"><%=graphType1[i]%></option>
                              <% }else{%>
                              <option value="<%=graphType1[i]%>"><%=graphType1[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                             <td>GraphType2</td>
                      <td>
                            <select name="graphType2" id="graphType2" style="width:150px">
                            <%for(int i=0;i<graphType2.length;i++){
                                if(graphtype2!=null &&  graphtype2.equalsIgnoreCase(graphType2[i])){%>
                            <option selected value="<%=graphType2[i]%>"><%=graphType2[i]%></option>
                              <% }else{%>
                              <option value="<%=graphType2[i]%>"><%=graphType2[i]%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                    </tr>
                    <%}%>
                    <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq")) {%>
                   <tr>
                       <td>ViewBy's</td>
                      <td>
                          <select name="viewbys" multiple="multiple" id="viewbys" style="width:150px">
                            <%for(int i=0;i<viewbylistNames.size();i++){
                                if(viewbylistNames!=null ){%>
                                <option selected value="<%=viewbylistNames.get(i)%>"><%=viewbylistNames.get(i)%></option>
                              <% }else{%>
                                <option selected value="<%=viewbylistNames.get(i)%>"><%=viewbylistNames.get(i)%></option>
                              <%}
                            }%>
                          </select>
                       </td>
                      </tr>
                      <%}%>
                      <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && !((jqgraphtype.equalsIgnoreCase("Pie")) || (jqgraphtype.equalsIgnoreCase("Pie-Empty")) || (jqgraphtype.equalsIgnoreCase("Donut-Single")) || (jqgraphtype.equalsIgnoreCase("Donut-Double"))|| (jqgraphtype.equalsIgnoreCase("Funnel")) || (jqgraphtype.equalsIgnoreCase("Funnel(INV)")) || (jqgraphtype.equalsIgnoreCase("columnPie"))||(jqgraphtype.equalsIgnoreCase("StackedBar(H)"))||(jqgraphtype.equalsIgnoreCase("StackedH(Percent)")) ||(jqgraphtype.equalsIgnoreCase("Bar-Horizontal")) )){%>
                   <tr>
                       <td>Left-Axis symbol</td>
                      <td>
                     <%if(ischeckedLA.equalsIgnoreCase("true")){%>
                          <input type="checkbox"  name="Y-LASymbol" id="Y-LASymbol" value="" checked>
                   <%}else{%>
                         <input type="checkbox"  name="Y-LASymbol" id="Y-LASymbol" value="" >
                     <%}%>

                      </td>
                      </tr>
                      <%}%>


                  <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && ( jqgraphtype.equalsIgnoreCase("DualAxis(Bar-Line)")) ) {%>

                   <tr>
                             <td>Right-Axis Symbol</td>
                      <td>
                  <%if(ischeckedRA.equalsIgnoreCase("true")){%>

                  <input type="checkbox"  name="Y-RASymbol" id="Y-RASymbol" value=""  checked>
                 <%}else{%>
                      <input type="checkbox"  name="Y-RASymbol" id="Y-RASymbol" value="" >
                <%}%>
                  </td>
                    </tr>
                    <%}%>

                   <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && ( jqgraphtype.equalsIgnoreCase("Bubble"))|| jqgraphtype.equalsIgnoreCase("Block")|| jqgraphtype.equalsIgnoreCase("Scatter")|| jqgraphtype.equalsIgnoreCase("Scatter(Regression)") || jqgraphtype.equalsIgnoreCase("Bubble(log)") || jqgraphtype.equalsIgnoreCase("StackedBar(H)")|| jqgraphtype.equalsIgnoreCase("Scatter(Partial)")||(jqgraphtype.equalsIgnoreCase("Bar-Horizontal"))||(jqgraphtype.equalsIgnoreCase("StackedH(Percent)")) )  {%>

                      <tr>
                             <td>X-Axis Symbol</td>
                      <td>
                  <%if(ischeckedRA.equalsIgnoreCase("true")){%>

                  <input type="checkbox"  name="Y-RASymbol" id="Y-RASymbol" value=""  checked>
                 <%}else{%>
                      <input type="checkbox"  name="Y-RASymbol" id="Y-RASymbol" value="" >
                     <%}%>
                  </td>
                   <td>X-Axis hide</td>
                      <td>
                  <%if(ischeckedXA.equalsIgnoreCase("true")){%>

                  <input type="checkbox"  name="X-HSymbol" id="X-HSymbol" value=""  checked>
                 <%}else{%>
                      <input type="checkbox"  name="X-HSymbol" id="X-HSymbol" value="" >
                     <%}%>
                  </td>
                   <td>Y-Axis hide</td>
                      <td>
                  <%if(ischeckedYA.equalsIgnoreCase("true")){%>

                  <input type="checkbox"  name="Y-HSymbol" id="Y-HSymbol" value=""  checked>
                 <%}else{%>
                      <input type="checkbox"  name="Y-HSymbol" id="Y-HSymbol" value="" >
                     <%}%>
                  </td>
                    </tr>
                    <%}%>




                    <% if(selectedgraphtype!=null && selectedgraphtype.equalsIgnoreCase("jq") && (jqgraphtype.equalsIgnoreCase("Line") || jqgraphtype.equalsIgnoreCase("Line(Smooth)") || jqgraphtype.equalsIgnoreCase("Line(Dashed)") || jqgraphtype.equalsIgnoreCase("Line(Simple)") || jqgraphtype.equalsIgnoreCase("Line(std)"))){%>
                   <tr>
                       <td>Tick Interval</td>
                      <td>
                            <select name="datebyInterval" id="datebyInterval" style="width:150px">
                            <%for(int i=0;i<graphType1.length;i++){
                                try{                // Try and Catch block is added to handle exception....Line Graph
                                if(datebyinterval!=null &&  datebyinterval.equalsIgnoreCase(datebyInterval[i])){%>
                            <option selected value="<%=datebyInterval[i]%>"><%=datebyInterval[i]%></option>
                              <% }else{%>
                              <option value="<%=datebyInterval[i]%>"><%=datebyInterval[i]%></option>
                              <%}
                                }catch(Exception e){        //by Mayank..

                                }
                            }%>
                          </select>
                       </td>
                   </tr>
                    <%}%>
                   <tr>
                        <td rowspan="2">Series 1:</td>
                          <td colspan="3" align="center"><table align="left"><tr width="100%">
                         <%for(int k=0;k<10;k++) { %>
                    <td>

                        <input  name="jqcolorCodes" id="jqcolorCodes<%=k%>" type="text" onclick="showColorjq(this.id)" value="" style='width:50px;cursor:pointer;background-color: <%=jqcolors[k]%>' seriesCodes=''>
                        <input  name="jqcolorCodeseries" id="jqcolorCodeseries<%=k%>" type="hidden"  value="<%=jqcolors[k]%>">

                    </td>
                    <%}%>
                     </tr></table></td>
                   </tr>

                   <%}%>
                <%--<input type="hidden" name="showLegends" id="showLegends" value="Y">--%>

                </tbody>
                <tfoot>
                    <%}
                }}
             %>

                    <%if(from!=null && from.equalsIgnoreCase("dashboard")){%>
                       <Tr>
                        <td align="center" colspan="4">
                            <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="saveDBGraphProperties('<%=reportId%>','<%=graphId%>','<%=fromDesigner%>')">
                        </td>
                     </Tr>
                    <%}else{%>
                    <Tr>
                        <td align="center" colspan="4">
                            <input type="button" name="Save" value="Done" class="navtitle-hover" onclick="goSave('<%=reportId%>')">
                        </td>
                    </Tr>
                    <%}%>
                </tfoot>
                <%--
               <Tr>
                    <Td>Graph Types</Td>
                    <Td>
                        <select name="graphTypeName" id="graphTypeName" style="width:150px">
                            <%
                        for (int i = 0; i < grpTypesSet.length; i++) {
                            if (String.valueOf(singeGraphDetails.get("graphTypeName")).equalsIgnoreCase(GraphTypesHashMap.get(grpTypesSet[i]).toString())) {%>
                            <option selected value="<%=GraphTypesHashMap.get(grpTypesSet[i]).toString()%>"><%=GraphTypesHashMap.get(grpTypesSet[i]).toString()%></option>
                            <%} else {%>
                            <option value="<%=GraphTypesHashMap.get(grpTypesSet[i]).toString()%>"><%=GraphTypesHashMap.get(grpTypesSet[i]).toString()%></option>
                            <%}
                        }%>
                        </select>
                    </Td>
                </Tr>--%>
            </Table>
            <input type="hidden" name="startValue1"  id="startValue1">
            <input type="hidden" name="startValue2"  id="startValue2">
            <input type="hidden" name="endValue"  id="endValue">
            <input type="hidden" name="targetRange"  id="targetRange">
            <input type="hidden" name="graphGridLines" id="graphGridLines">
            <input type="hidden" name="showLegends" id="showLegends">
            <input type="hidden" name="rgbColorCode" id="rgbColorCode">
            <input type="hidden" name="targetColorCode" id="targetColorCode">
        </form>
            <div id="colorsDiv" style="display: none" title="Select color">
                    <center>
                        <input type="text" id="color" style="" value="#12345" >
                        <div id="colorpicker" style=""></div>
                        <input type="button" align="center" value="Done" class="navtitle-hover" onclick="saveSelectedColor()">
                        <input type="button" align="center" value="Cancel" class="navtitle-hover" onclick="cancelColor()">
                        <input type="hidden" id="selectedId" value="">
                    </center>
        </div>
              <script type="text/javascript">
             var localgraphdrill="";
             $(document).ready(function(){

                    $("#colorsDiv").dialog({
                            bgiframe: true,
                            autoOpen: false,
                            height:300,
                            width: 300,
                            modal: true,
                            Cancel: function() {
                                $(this).dialog('close');
                            }

                    });
                    $('#colorpicker').farbtastic('#color');

             });
            function goSave(reportId){
            var  ischeckedRA=false;
            var ischeckedXA=false;
            var ischeckedYA=false;

            var   ischeckedLA=false;

            var flag=true;
            var viewbyLabels=$("#viewbys").val();
            var ischeckedothers=false;
             var flagothers=true;
            //alert(viewbyLabels);
             $("#rgbColorCode").val($("#colorCodes").css("background-color"))
               // alert("grapphSymbol\n"+document.getElementById("graphSymbol").value);
                 var Obj=document.getElementById("targetRange");
                if(Obj!=null){
                    for(var i=0;i<Obj.length;i++){
                        if(Obj[i].selected && Obj[i].value=='Discrete'){
                           // alert(document.getElementById("startValue1").value)
                            document.getElementById("startValue").value=document.getElementById("startValue1").value;
                        }else{
                            document.getElementById("startValue").value=document.getElementById("startValue2").value;
                        }
                    }
                }
                  if($("#Y-LASymbol").is(':checked')){
                      ischeckedLA=true;
                        }
                    if($("#Y-RASymbol").is(':checked')){
                      ischeckedRA=true;
                  }
                  if($("#X-HSymbol").is(':checked')){
                      ischeckedXA=true;
                  }

                  if($("#Y-HSymbol").is(':checked')){
                      ischeckedYA=true;
                  }


                  if($("#showOthers").is(':checked')){
                     ischeckedothers=true;
                 }
                var grpSize = document.getElementById("grpSize").value;
                var parentframeObj = parent.document.getElementById("iframe4");
                var frameObj = parent.document.getElementById("widgetframe");
                if(grpSize == "Large"){
                    var source = 'divPersistent.jsp?method=forFrameheight&grpSize=Large&reportId='+reportId;
                    frameObj.src = source;
                    // add by Aditi for blank space on hiding legends
                    if((document.getElementById("showLegends").value) === "N"){


                     parentframeObj.style.height = "475px";}
                     else
                     {parentframeObj.style.height = "500px";}
                }else{
                    var source = 'divPersistent.jsp?method=forFrameheight&grpSize=Others&reportId='+reportId;
                    frameObj.src = source;
                    if((document.getElementById("showLegends").value) === "N"){


                     parentframeObj.style.height = "475px";}
                     else{
                    parentframeObj.style.height = "500px";}
                }
         <% if(selectedgraphtype!=null && !selectedgraphtype.equalsIgnoreCase("null")&&selectedgraphtype.equalsIgnoreCase("jq") && frameheight.equalsIgnoreCase("500")){%>
            if((document.getElementById("showLegends").value) === "N"){


                     parentframeObj.style.height = "475px";}
                     else
                    parentframeObj.style.height = "<%=frameheight%>px";

               <%}%>
// code ended by aditi
                var selStacked ;

//                if(document.getElementById("absStacked").checked){
//                        selStacked=document.getElementById("absStacked").value;
//                    } else
//                        selStacked=document.getElementById("prcStacked").value;

                    var selStacked = document.getElementsByName("absStacked");
      for (var x = 0; x < selStacked.length; x ++) {
          if (selStacked[x].checked) {
          selStacked[x].id;
                     }
      }


                document.forms.grpProForm.action="<%=request.getContextPath()%>/reportViewer.do?reportBy=graphChanges&graphChange=graphProperties&REPORTID="+reportId+"&selectedgraphtype="+'<%=selectedgraphtype%>'+'&flag='+flag+'&viewbyLabels='+viewbyLabels+'&ischeckedLA='+ischeckedLA+'&ischeckedRA='+ischeckedRA+'&ischeckedothers='+ischeckedothers+'&flagothers='+flagothers+'&ischeckedYA'+ischeckedYA+'&ischeckedXA='+ischeckedXA,
                document.forms.grpProForm.submit();
                parent.refreshReportGraphs('<%=request.getContextPath()%>',reportId);
        }
            function goCancel(){
                parent.document.getElementById("dispGrpProp").style.display='none';
                parent.document.getElementById("dispGrpPropFrame").style.display="none"
                parent.document.getElementById('fade').style.display='none';
            }
            function showLegendsFun(){
                var showLegendsObj=document.getElementById("showLegends");
                if(showLegendsObj.checked){
                    showLegendsObj.value="Y"
                }else{
                    showLegendsObj.value="N";
                }
                document.getElementById("showLegends").value=showLegendsObj.value;
            }
            function showLabelsFun(){
                var showLabelsObj=document.getElementById("showLabels");
                if(showLabelsObj.checked){
                    showLabelsObj.value="true"
                }else{
                    showLabelsObj.value="false";
                }
                document.getElementById("showLabels").value=showLabelsObj.value;
            }
            function showGridLinesFun(){
                var showGridLinesObj=document.getElementById("graphGridLines");
                if(showGridLinesObj.checked){
                    showGridLinesObj.value="Y"
                }else{
                    showGridLinesObj.value="N";
                }
                document.getElementById("graphGridLines").value=showGridLinesObj.value;
            }
            function showGTFun(){
                var showGTObj=document.getElementById("showGT");
                if(showGTObj.checked){
                    showGTObj.value="Y"
                    document.getElementById("targetRow").style.display='none';
                }else{
                    showGTObj.value="N";
                }
            }
            function showMinMaxRangeFun(){
                var showMinMaxRangeObj=document.getElementById("showMinMaxRange");
                if(showMinMaxRangeObj.checked){
                    showMinMaxRangeObj.value="Y"
                    document.getElementById("targetRow").style.display='none';
                }else{
                    showMinMaxRangeObj.value="N";
                    //document.getElementById("targetRow").style.display='block';
                }

            }
            function changeTargetRange(
            Obj){
                for(var i=0;i<Obj.length;i++){
                    if(Obj[i].selected && Obj[i].value=='Discrete'){
                        document.getElementById("rangeRow").style.display='none';
                        //document.getElementById("rangeRow2").style.display='none';
                        document.getElementById("discreteRow").style.display='';


                        document.getElementById("rangeRow").style.visibility='hidden';
                        //document.getElementById("rangeRow2").style.visibility='hidden';
                        document.getElementById("discreteRow").style.visibility='visible';

                    }else if(Obj[i].selected && Obj[i].value=='Range'){
                        document.getElementById("discreteRow").style.display='none';
                        document.getElementById("rangeRow").style.display='';
                        //document.getElementById("rangeRow2").style.display='';

                        document.getElementById("rangeRow").style.visibility='visible';
                        //document.getElementById("rangeRow2").style.visibility='visible';
                        document.getElementById("discreteRow").style.visibility='hidden';
                    }
                }
            }

            function NumericOnly(obj,ev){
                if(isNaN(obj.value)){
                    alert("Please enter only Numerics");
                    obj.value="";
                }
                /*
                 var ev = ev || window.event;
                key=ev.keyCode
                if (key <48 || key >57){
                    obj.value="";
                    alert("Please enter only Numerics");
                   //ev.returnValue = false;
                   //return false;
                }
                 */
            }
            function saveDBGraphProperties(dashboardId,graphId,fromDesigner)
            {
                $("#rgbColorCode").val($("#colorCodes").css("background-color"))
                document.forms.grpProForm.action="<%=request.getContextPath()%>/dashboardTemplateViewerAction.do?templateParam2=updateGraphProperties&dashboardId="+dashboardId+"&graphId="+graphId
                document.forms.grpProForm.submit();
                parent.$("#graphPropertiesDiv").dialog('close');
                parent.displayGraph('<%=dashletId%>','<%=reportId%>','<%=refReportId%>','<%=graphId%>','<%=kpiMastId%>','<%=displaySeq%>','<%=dispType%>','<%=dashName%>','','','',fromDesigner);
            }
            function showChecked(){
                var selStackedObj=document.getElementByName("absStacked");
                if(selStackedObj.value=='absStacked'){
                        document.getElementById("absStacked").checked==true;
                        document.getElementById("prcStacked").checked==false;
                    } else if(selStackedObj.value=='prcStacked'){
                        document.getElementById("absStacked").checked==false;
                        document.getElementById("prcStacked").checked==true;
                     }

            }
             function showColor(id)
          {
//             parent.$("#applycolrdiv").dialog('close')
//             $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
//             alert(colorCode)
              $("#colorsDiv").dialog('open')
             if(colorCode!=undefined && colorCode!="")
              {
                 $("#color").val(colorCode)
                 $("#color").css("background-color",colorCode)
//                 jQuery.updateValue('event')
            $.farbtastic("#color").setColor(colorCode)
              }


          }
               function showTargetColor(id)
          {

               $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
              $("#colorsDiv").dialog('open')
             if(colorCode!=undefined && colorCode!="")
              {
                 $("#color").val(colorCode)
                 $("#color").css("background-color",colorCode)
            $.farbtastic("#color").setColor(colorCode)
              }


          }
                      function showColorjq(id)
          {
//             parent.$("#applycolrdiv").dialog('close')
            $("#selectedId").val(id);
             var colorCode="";
             colorCode=$("#"+id).attr('colorCode');
             $("#colorsDiv").dialog('open')
             if(colorCode!=undefined && colorCode!="")
              {
                 $("#color").val(colorCode)
                 $("#color").css("background-color",colorCode)
//                 jQuery.updateValue('event')
            $.farbtastic("#color").setColor(colorCode)
              }


          }
          function saveSelectedColor()
          {
             // var seletedTextId= $("#selectedId").val();
             var slectedid=$("#selectedId").val();
             if(slectedid!="" && slectedid!="colorCodes" && slectedid!="targetColorcode"){
            var id=$("#selectedId").val()
              var colorCode=$("#color").val();
              $("#"+id).css("background-color",colorCode);
              var id1=id.replace("jqcolorCodes","")
              var id2=id.replace(id, 0)
              $("#jqcolorCodeseries"+id1).val(colorCode);
              //$("#seriesCodes").val(colorCode)
//              var id1=id.replace(id,"jqcolorCodes")
//              $("#"+id1).val(colorCode);

          }
          else if(slectedid!="" && slectedid=='targetColorcode'){
               var colorCode=$("#color").val();
              $("#targetColorcode").css("background-color",colorCode)
              $("#targetColorcode").attr('colorCode',colorCode);
              $("#targetColorCode").val(colorCode);
          }
          else{
          var colorCode=$("#color").val();
              $("#colorCodes").css("background-color",colorCode)
              $("#colorCodes").attr('colorCode',colorCode);
              $("#rgbColorCode").val($("#colorCodes").css("background-color"))
          }
              $("#colorsDiv").dialog('close')
          }
          function cancelColor()
          {
               $("#colorsDiv").dialog('close')
          }
          function showLocalDrill()
          {
             var showLocaldrillObj=document.getElementById("showLocaldrill");
             //alert('showLabelsObj---'+showLocaldrillObj)
              if(showLocaldrillObj.checked){
                    showLocaldrillObj.value="true"
                }
                else
                    {
                        showLocaldrillObj.value="false"
                    }
                    document.getElementById("showLocaldrill").value=showLocaldrillObj.value;
          }
          function yaxiscallibration()
          {
              var yaxiscal=$("#yaxiscallibrationid").val()
              if(yaxiscal=='Custom'){
              $("#customid").show();
              }
              else{
                  $("#customid").hide();
              }

          }
          function showtransposeData(){
                var transposeObj=document.getElementById("transposeData");
                if(transposeObj.checked){
                    transposeObj.value="true"
                }else{
                    transposeObj.value="false";
                }
                document.getElementById("transposeData").value=transposeObj.value;
            }
            function adhocDillConversion(){
                var transposeObj=document.getElementById("AdhocDrill");
                if(transposeObj.checked){
                    transposeObj.value="true"
                }else{
                    transposeObj.value="false";
                }
            }
            function showColorGrouping(){
                var transposeObj=document.getElementById("colorGrouping");
                if(transposeObj.checked){
                    transposeObj.value="true"
                }else{
                    transposeObj.value="false";
                }
                document.getElementById("colorGrouping").value=transposeObj.value;
            }
            function xaxisAppend(){
                var xaxisAppendObj=document.getElementById("appendXaxis");
                if(xaxisAppendObj.checked){
                    xaxisAppendObj.value="true"
                }else{
                    xaxisAppendObj.value="false";
                }
                document.getElementById("appendXaxis").value=xaxisAppendObj.value;
            }
           function legendAppend(){
                var appendLegendObj=document.getElementById("appendLegend");
                if(appendLegendObj.checked){
                    appendLegendObj.value="true"
                }else{
                    appendLegendObj.value="false";
                }
                document.getElementById("appendLegend").value=appendLegendObj.value;
            }
            function Xaxistooltip(){
                var tooltipXaxisObj=document.getElementById("tooltipx");
                if(tooltipXaxisObj.checked){
                    tooltipXaxisObj.value="true"
                }else{
                    tooltipXaxisObj.value="false";
                }
                $("#tooltipx").val(tooltipXaxisObj.value);
//                document.getElementById("tooltipxaxis").value=tooltipXaxisObj.value;
            }
           function zeroValuesSuprise(){
                var zeroValuesObj=document.getElementById("zeroValues");
                if(zeroValuesObj.checked){
                    zeroValuesObj.value="true"
                }else{
                    zeroValuesObj.value="false";
                }
                document.getElementById("zeroValues").value=zeroValuesObj.value;
            }
        </script>
    </body>
</html>

