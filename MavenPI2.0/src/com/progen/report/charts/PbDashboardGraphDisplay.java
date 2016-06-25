package com.progen.report.charts;

import com.progen.charts.ProgenChartDatasets;
import com.progen.charts.ProgenChartDisplay;
import com.progen.report.pbDashboardCollection;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;

public class PbDashboardGraphDisplay extends PbDb implements Serializable {

    public static Logger logger = Logger.getLogger(PbDashboardGraphDisplay.class);
    private static final long serialVersionUID = 2276765074881909L;
    PbGraphDisplayResourceBundle resBundle = new PbGraphDisplayResourceBundle();
    String dashboardGraphHdrsQuery = resBundle.getString("dashboardGrpHdrsQuery");
    String dashboardGraphDtlsQuery = resBundle.getString("dashboardGrpDtlsQuery");
    String dashboardGraphHdrsQueryNew = resBundle.getString("dashboardGraphHdrsQueryNew");
    public ArrayList reportRowViewbyValues = new ArrayList();
    private String reportId;
    private String[] reportIds;
    private String[] viewByElementIds;
    private String[] viewByColNames;
    private ArrayList currentDispRecords;
    private PbReturnObject pbretObj = null;
    private PbReturnObject pbretObj2 = null;
    private String[] dbColumns = null;
    private String[] pieChartColumns = null;
    private String[] barChartColumnNames = null;
    private String[] barChartColumnTitles = null;
    private ProgenChartDatasets graph = null;
    private ProgenChartDisplay[] pcharts = null;
//    private String path = "";
    private StringBuilder path = new StringBuilder();
//    private String graphTitle = "";
    private StringBuilder graphTitle = new StringBuilder();
    private String graphId = null;
    private String[] graphIds;
    private String graphName = null;
    private String graphWidth = null;
    private String graphHeight = null;
    private String graphClassName = null;
    private String graphTypeName = null;
    private String jscal = "1";
    private HttpSession session = null;
    private HttpServletResponse response = null;
    private Writer out = null;
    private int graphsCnt = 0;
    private String ctxPath = null;
    //added by santhosh.kumar@progenbusiness.com on 27/07/09
    private String[] axis = null;
    private String[] barChartColumnNames1 = null;
    private String[] barChartColumnTitles1 = null;
    private String[] barChartColumnNames2 = null;
    private String[] barChartColumnTitles2 = null;
    private ProgenChartDatasets graph1 = null;
    private ProgenChartDatasets graph2 = null;
    private HashMap swapGraphAnalysis = null;
    private ProgenChartDisplay[] pchartsZoom = null;
    private String pathZoom = "";
    private Object[] Obj = null;
    private String finalQuery = "";
    private HashMap[] graphMapDetails = null;
    pbDashboardCollection collect = new pbDashboardCollection();
    //added by santhosh.kumar@progenbusiness.com on 25/11/2009
    private String graphSize = "Medium";
    private String grplegend = "Y";
    private String grplegendloc = "Bottom";
    private String grpshox = "Y";
    private String grpshoy = "Y";
    private String grplyaxislabel = "";
    private String grpryaxislabel = "";
    private String grpdrill = "Y";
    private String grpbcolor = "";
    private String grpfcolor = "";
    private String grpdata = "Y";

    public pbDashboardCollection getCollect() {
        return collect;
    }

    public void setCollect(pbDashboardCollection collect) {
        this.collect = collect;
    }

    public PbDashboardGraphDisplay() {
    }

    public String getGraphId() {
        return graphId;
    }

    public void setGraphId(String graphId) {
        this.graphId = graphId;
    }

    public ArrayList getCurrentDispRecords() {
        return currentDispRecords;
    }

    public String getJscal() {
        return jscal;
    }

    public void setJscal(String jscal) {
        this.jscal = jscal;
    }

    public HttpSession getSession() {
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setCurrentDispRecords(ArrayList currentDispRecords) {
        this.currentDispRecords = currentDispRecords;
    }

    public Writer getOut() {
        return out;
    }

    public void setOut(Writer out) {
        this.out = out;
    }

    public String getDashboardGrpHdrsQuery(String reportId) {
        Obj = new Object[1];
        Obj[0] = reportId;
        finalQuery = buildQuery(dashboardGraphHdrsQuery, Obj);
        return finalQuery;
    }

    public String getDashboardGrpHdrsQueryNew(String graphId) {
        Obj = new Object[1];
        Obj[0] = graphId;
        finalQuery = buildQuery(dashboardGraphHdrsQueryNew, Obj);
        return finalQuery;
    }

    public String getDashboardGrpDtlsQuery(String graphId) {
        Obj = new Object[1];
        Obj[0] = graphId;

        finalQuery = buildQuery(dashboardGraphDtlsQuery, Obj);
        return finalQuery;
    }

    public PbReturnObject getPbReturnObject(String sql) {
        PbReturnObject pbretObj3 = null;
        try {
            pbretObj3 = execSelectSQL(sql);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return pbretObj3;
    }

    public ArrayList getDashboardGraphHeaders(String reportId) {
        ArrayList grpDetails = new ArrayList();
        if (graphMapDetails != null && graphMapDetails.length != 0) {
        } else {
        }

        try {
            pbretObj = getPbReturnObject(getDashboardGrpHdrsQuery(reportId));
            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];


            graphMapDetails = new HashMap[pbretObj.getRowCount()];



            for (int i = 0; i < getGraphsCnt(); i++) {
                boolean SwapColumn = true;

                graphMapDetails[i] = new HashMap();


                graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
                graphWidth = pbretObj.getFieldValueString(i, tableDBColumns[2]);
                graphHeight = pbretObj.getFieldValueString(i, tableDBColumns[3]);
                graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
                graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);


                graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
                graphWidth = pbretObj.getFieldValueString(i, tableDBColumns[2]);
                graphHeight = pbretObj.getFieldValueString(i, tableDBColumns[3]);
                graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
                graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);
                graphSize = pbretObj.getFieldValueString(i, tableDBColumns[6]);
                grplegendloc = pbretObj.getFieldValueString(i, tableDBColumns[7]);
                grplegend = pbretObj.getFieldValueString(i, tableDBColumns[8]);
                grpshox = pbretObj.getFieldValueString(i, tableDBColumns[9]);
                grpshoy = pbretObj.getFieldValueString(i, tableDBColumns[10]);
                grpdrill = pbretObj.getFieldValueString(i, tableDBColumns[11]);
                grpbcolor = pbretObj.getFieldValueString(i, tableDBColumns[12]);
                grpfcolor = pbretObj.getFieldValueString(i, tableDBColumns[13]);
                grpdata = pbretObj.getFieldValueString(i, tableDBColumns[14]);
                grplyaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[15]);
                grpryaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);


                setGraphId(graphId);

                pbretObj2 = getPbReturnObject(getDashboardGrpDtlsQuery(graphId));
                barChartColumnTitles = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                barChartColumnNames = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                //pieChartColumns = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                axis = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];

                String[] graphDBColumns = pbretObj2.getColumnNames();

                for (int viewbyIndex = 0; viewbyIndex < collect.reportRowViewbyValues.size(); viewbyIndex++) {
                    barChartColumnTitles[viewbyIndex] = "A_" + (String) collect.reportRowViewbyValues.get(viewbyIndex);
                    barChartColumnNames[viewbyIndex] = "A_" + (String) collect.reportRowViewbyValues.get(viewbyIndex);
                    //pieChartColumns[viewbyIndex] ="A_"+(String)collect.reportRowViewbyValues.get(viewbyIndex);
                    axis[viewbyIndex] = "0";

                }

                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    barChartColumnTitles[collect.reportRowViewbyValues.size() + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    barChartColumnNames[collect.reportRowViewbyValues.size() + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    //pieChartColumns[collect.reportRowViewbyValues.size() + j] ="A_"+ pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    axis[collect.reportRowViewbyValues.size() + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);

                }
                pieChartColumns = barChartColumnNames;

                graph = new ProgenChartDatasets();

                graph.setBarChartColumnNames(barChartColumnNames);
                graph.setBarChartColumnTitles(barChartColumnTitles);
                graph.setPieChartColumns(pieChartColumns);

                String[] newStr = new String[collect.reportRowViewbyValues.size()];
                for (int k = 0; k < collect.reportRowViewbyValues.size(); k++) {
                    newStr[k] = "A_" + String.valueOf(collect.reportRowViewbyValues.get(k));
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("newStr[k] is " + newStr[k]);
                }
                graph.setViewByColumns(newStr);


                if (swapGraphAnalysis != null && swapGraphAnalysis.get(graphId) != null) {
                    graph.setSwapColumn(Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))));
                    SwapColumn = Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId)));
                } else {
                    graph.setSwapColumn(true);
                    SwapColumn = true;
                }


                //pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth), Integer.parseInt(graphHeight), ctxPath);
                pcharts[i] = new ProgenChartDisplay(400, 250, ctxPath);
                pchartsZoom[i] = new ProgenChartDisplay(700, 350, ctxPath);

                pcharts[i].setAlist(getCurrentDispRecords());
                pcharts[i].setGraph(graph);
                pcharts[i].setChartType(graphTypeName);
                pcharts[i].setGrplyaxislabel(grplyaxislabel);
                pcharts[i].setFunName(getJscal());
                pcharts[i].setSession(getSession());
                pcharts[i].setResponse(getResponse());
                pcharts[i].setOut(getOut());

                pchartsZoom[i].setAlist(getCurrentDispRecords());
                pchartsZoom[i].setGraph(graph);
                pchartsZoom[i].setChartType(graphTypeName);
                pchartsZoom[i].setGrplyaxislabel(grplyaxislabel);
                pchartsZoom[i].setFunName(getJscal());
                pchartsZoom[i].setSession(getSession());
                pchartsZoom[i].setResponse(getResponse());
                pchartsZoom[i].setOut(getOut());




                if (graphClassName.equalsIgnoreCase("Pie")) {
//                    path = path + ";" + pcharts[i].GetPieAxisChart();
                    path.append(";").append(pcharts[i].GetPieAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetPieAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Category")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Meter")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();

                } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dial")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {
//                    String axis1 = "";
                    StringBuilder axis1 = new StringBuilder(1000);
//                    String axis2 = "";
                    StringBuilder axis2 = new StringBuilder(1000);
                    for (int ind = 0; ind < axis.length; ind++) {
                        if (axis[ind].equalsIgnoreCase("0")) {
//                            axis1 = axis1 + "," + ind;
                            axis1.append(",").append(ind);
                        } else {
//                            axis2 = axis2 + "," + ind;
                            axis2.append(",").append(ind);
                        }
                    }
//                    if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                    if ((axis1.length() > 0) && (axis2.length() > 0)) {
//                        axis1 = axis1.substring(1);
//                        axis2 = axis2.substring(1);
                        axis1 = new StringBuilder(axis1.substring(1));
                        axis2 = new StringBuilder(axis2.substring(1));
                        String[] temp1 = axis1.toString().split(",");

                        barChartColumnTitles1 = new String[temp1.length + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        String[] temp2 = axis2.toString().split(",");
                        barChartColumnTitles2 = new String[temp2.length + viewByElementIds.length];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];

                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }

                        for (int j = 0; j < temp1.length; j++) {
                            barChartColumnTitles1[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp1[j])];
                            barChartColumnNames1[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp1[j])];
                        }
                        for (int j = 0; j < temp2.length; j++) {
                            barChartColumnTitles2[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp2[j])];
                            barChartColumnNames2[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp2[j])];
                        }
                    } else {
                        int count = 1;
                        barChartColumnTitles1 = new String[count + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }
                        for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                        }

                        for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                            barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                            barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                        }

                    }
                    graph1 = new ProgenChartDatasets();
                    graph2 = new ProgenChartDatasets();

                    graph1.setBarChartColumnNames(barChartColumnNames1);
                    graph1.setViewByColumns(viewByElementIds);
                    graph1.setBarChartColumnTitles(barChartColumnTitles1);
                    graph1.setPieChartColumns(barChartColumnNames1);

                    graph2.setBarChartColumnNames(barChartColumnNames2);
                    graph2.setViewByColumns(viewByElementIds);
                    graph2.setBarChartColumnTitles(barChartColumnTitles2);
                    graph2.setPieChartColumns(barChartColumnNames2);

                    pcharts[i].setGrpryaxislabel(grpryaxislabel);
                    pcharts[i].setGraph1(graph1);
                    pcharts[i].setGraph2(graph2);

                    pchartsZoom[i].setGrpryaxislabel(grpryaxislabel);
                    pchartsZoom[i].setGraph1(graph1);
                    pchartsZoom[i].setGraph2(graph2);

                    if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
//                        path = path + ";" + pcharts[i].GetDualAxisChart();
                        path.append(";").append(pcharts[i].GetDualAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetDualAxisChart();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
//                        path = path + ";" + pcharts[i].GetOverlaidBar();
                        path.append(";").append(pcharts[i].GetOverlaidBar());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidBar();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
//                        path = path + ";" + pcharts[i].GetOverlaidArea();
                        path.append(";").append(pcharts[i].GetOverlaidArea());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidArea();
                    } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
//                        path = path + ";" + pcharts[i].GetFeverChart();
                        path.append(";").append(pcharts[i].GetFeverChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetFeverChart();
                    }



                }


                //storing graph header info in HashMap
                graphMapDetails[i].put("graphId", graphId);
                graphMapDetails[i].put("graphName", graphName);
                graphMapDetails[i].put("graphWidth", graphWidth);
                graphMapDetails[i].put("graphHeight", graphHeight);
                graphMapDetails[i].put("graphClassName", graphClassName);
                graphMapDetails[i].put("graphTypeName", graphTypeName);
                graphMapDetails[i].put("SwapColumn", SwapColumn);

                graphMapDetails[i].put("graphLegend", grplegend);
                graphMapDetails[i].put("graphLegendLoc", grplegendloc);
                graphMapDetails[i].put("graphshowX", grpshox);
                graphMapDetails[i].put("graphshowY", grpshoy);
                graphMapDetails[i].put("graphLYaxislabel", grplyaxislabel);
                graphMapDetails[i].put("graphRYaxislabel", grpryaxislabel);
                graphMapDetails[i].put("graphDrill", grpdrill);
                graphMapDetails[i].put("graphBcolor", grpbcolor);
                graphMapDetails[i].put("graphFcolor", grpfcolor);
                graphMapDetails[i].put("graphData", grpdata);

                //storing graph details info in HashMap
                graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                graphMapDetails[i].put("viewByElementIds", newStr);
                graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                graphMapDetails[i].put("pieChartColumns", pieChartColumns);
                graphMapDetails[i].put("axis", axis);

                //storing graph details info (Dual Axis ) in HashMap
                graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);

            }
//            if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            if (graphTitle != null && graphTitle.length() > 0) {
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }
            // path = path.substring(1);
            //graphTitle = graphTitle.substring(1);
            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);

            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getDashboardGraphHeaders ends here ");
        return grpDetails;
    }

    public ArrayList getGraphHeadersByGraphMap() {
        ArrayList grpDetails = new ArrayList();
        try {
            pcharts = new ProgenChartDisplay[graphMapDetails.length];
            pchartsZoom = new ProgenChartDisplay[graphMapDetails.length];

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getCurrentDispRecords() in getGraphHeadersByGraphMap() is "+getCurrentDispRecords());

            for (int i = 0; i < graphMapDetails.length; i++) {
                boolean SwapColumn = true;

                graphId = String.valueOf(graphMapDetails[i].get("graphId"));
                graphName = String.valueOf(graphMapDetails[i].get("graphName"));
                graphClassName = String.valueOf(graphMapDetails[i].get("graphClassName"));
                graphTypeName = String.valueOf(graphMapDetails[i].get("graphTypeName"));

                graphWidth = String.valueOf(graphMapDetails[i].get("graphWidth"));
                graphHeight = String.valueOf(graphMapDetails[i].get("graphHeight"));


                setGraphId(graphId);

                barChartColumnTitles = ((String[]) graphMapDetails[i].get("barChartColumnTitles"));
                barChartColumnNames = ((String[]) graphMapDetails[i].get("barChartColumnNames"));
                pieChartColumns = ((String[]) graphMapDetails[i].get("pieChartColumns"));
                axis = ((String[]) graphMapDetails[i].get("axis"));
                viewByElementIds = ((String[]) graphMapDetails[i].get("viewByElementIds"));

                graph = new ProgenChartDatasets();
                graph.setBarChartColumnNames(barChartColumnNames);
                graph.setViewByColumns(viewByElementIds);
                graph.setBarChartColumnTitles(barChartColumnTitles);
                graph.setPieChartColumns(pieChartColumns);

                if (swapGraphAnalysis != null && swapGraphAnalysis.get(graphId) != null) {
                    graph.setSwapColumn(Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))));
                    SwapColumn = Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId)));
                } else {
                    graph.setSwapColumn(true);
                    SwapColumn = true;
                }

                if (graphWidth != null && graphHeight != null) {
                    pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth), Integer.parseInt(graphHeight), ctxPath);
                } else {
                    pcharts[i] = new ProgenChartDisplay(Integer.parseInt("400"), Integer.parseInt("250"), ctxPath);
                }
                pcharts[i] = new ProgenChartDisplay(400, 250, ctxPath);
                pchartsZoom[i] = new ProgenChartDisplay(700, 350, ctxPath);

                pcharts[i].setAlist(getCurrentDispRecords());
                pcharts[i].setGraph(graph);
                pcharts[i].setChartType(graphTypeName);
                pcharts[i].setGrplyaxislabel(grplyaxislabel);
                pcharts[i].setFunName(getJscal());
                pcharts[i].setSession(getSession());
                pcharts[i].setResponse(getResponse());
                pcharts[i].setOut(getOut());

                pchartsZoom[i].setAlist(getCurrentDispRecords());
                pchartsZoom[i].setGraph(graph);
                pchartsZoom[i].setChartType(graphTypeName);
                pchartsZoom[i].setGrplyaxislabel(grplyaxislabel);
                pchartsZoom[i].setFunName(getJscal());
                pchartsZoom[i].setSession(getSession());
                pchartsZoom[i].setResponse(getResponse());
                pchartsZoom[i].setOut(getOut());

                if (graphClassName != null && barChartColumnNames != null && viewByElementIds != null && axis != null) {

                    if (graphClassName.equalsIgnoreCase("Pie")) {
//                        path = path + ";" + pcharts[i].GetPieAxisChart();
                        path.append(";").append(pcharts[i].GetPieAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetPieAxisChart();
                    } else if (graphClassName.equalsIgnoreCase("Category")) {
//                        path = path + ";" + pcharts[i].GetCategoryAxisChart();
                        path.append(";").append(pcharts[i].GetCategoryAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                    } else if (graphClassName.equalsIgnoreCase("Meter")) {
//                        path = path + ";" + pcharts[i].GetCategoryAxisChart();
                        path.append(";").append(pcharts[i].GetCategoryAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();

                    } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//                        path = path + ";" + pcharts[i].GetCategoryAxisChart();
                        path.append(";").append(pcharts[i].GetCategoryAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                    } else if (graphClassName.equalsIgnoreCase("Dial")) {
//                        path = path + ";" + pcharts[i].GetCategoryAxisChart();
                        path.append(";").append(pcharts[i].GetCategoryAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                    } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {

                        barChartColumnNames1 = ((String[]) graphMapDetails[i].get("barChartColumnNames1"));
                        barChartColumnTitles1 = ((String[]) graphMapDetails[i].get("barChartColumnTitles1"));
                        barChartColumnNames2 = ((String[]) graphMapDetails[i].get("barChartColumnNames2"));
                        barChartColumnTitles2 = ((String[]) graphMapDetails[i].get("barChartColumnTitles2"));

                        if (barChartColumnNames1 != null && barChartColumnNames2 != null) {
                        } else {
                            int count = 1;

                            /*
                             * int count = 0; int grpCol = 0;
                             *
                             * grpCol = barChartColumnNames.length -
                             * viewByElementIds.length; if (grpCol == 1) { count
                             * = 1; } else { count = grpCol / 2; }
                             *
                             * if (count == 1) { barChartColumnTitles1 = new
                             * String[count + viewByElementIds.length];
                             * barChartColumnNames1 = new
                             * String[barChartColumnTitles1.length];
                             *
                             * barChartColumnTitles2 = new String[count +
                             * viewByElementIds.length]; barChartColumnNames2 =
                             * new String[barChartColumnTitles2.length]; } else
                             * { barChartColumnTitles1 = new String[count +
                             * viewByElementIds.length]; barChartColumnNames1 =
                             * new String[barChartColumnTitles1.length];
                             *
                             * barChartColumnTitles2 = new
                             * String[(barChartColumnNames.length - count)];
                             * barChartColumnNames2 = new
                             * String[barChartColumnTitles2.length]; }
                             */


                            barChartColumnTitles1 = new String[count + viewByElementIds.length];
                            barChartColumnNames1 = new String[barChartColumnTitles1.length];

                            barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                            barChartColumnNames2 = new String[barChartColumnTitles2.length];

                            for (int j = 0; j < viewByElementIds.length; j++) {
                                barChartColumnTitles1[j] = barChartColumnTitles[j];
                                barChartColumnTitles2[j] = barChartColumnTitles[j];
                                barChartColumnNames1[j] = barChartColumnNames[j];
                                barChartColumnNames2[j] = barChartColumnNames[j];
                            }
                            for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                                barChartColumnTitles1[j] = barChartColumnTitles[j];
                                barChartColumnNames1[j] = barChartColumnNames[j];
                            }

                            for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                                barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                                barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                            }
                        }

                        graph1 = new ProgenChartDatasets();
                        graph2 = new ProgenChartDatasets();

                        graph1.setBarChartColumnNames(barChartColumnNames1);
                        graph1.setViewByColumns(viewByElementIds);
                        graph1.setBarChartColumnTitles(barChartColumnTitles1);
                        graph1.setPieChartColumns(pieChartColumns);

                        graph2.setBarChartColumnNames(barChartColumnNames2);
                        graph2.setViewByColumns(viewByElementIds);
                        graph2.setBarChartColumnTitles(barChartColumnTitles2);
                        graph2.setPieChartColumns(pieChartColumns);

                        pcharts[i].setGrpryaxislabel(grpryaxislabel);
                        pcharts[i].setGraph1(graph1);
                        pcharts[i].setGraph2(graph2);

                        pchartsZoom[i].setGrpryaxislabel(grpryaxislabel);
                        pchartsZoom[i].setGraph1(graph1);
                        pchartsZoom[i].setGraph2(graph2);


                        if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
//                            path = path + ";" + pcharts[i].GetDualAxisChart();
                            path.append(";").append(pcharts[i].GetDualAxisChart());
                            graphTitle.append(";").append(graphName);
                            pathZoom = pathZoom + ";" + pchartsZoom[i].GetDualAxisChart();
                        } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
//                        path = path + ";" + pcharts[i].GetOverlaidBar();
                            path.append(";").append(pcharts[i].GetOverlaidBar());
                            graphTitle.append(";").append(graphName);
                            pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidBar();
                        } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
//                        path = path + ";" + pcharts[i].GetOverlaidArea();
                            path.append(";").append(pcharts[i].GetOverlaidArea());
                            graphTitle.append(";").append(graphName);
                            pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidArea();
                        } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
//                        path = path + ";" + pcharts[i].GetFeverChart();
                            path.append(";").append(pcharts[i].GetFeverChart());
                            graphTitle.append(";").append(graphName);
                            pathZoom = pathZoom + ";" + pchartsZoom[i].GetFeverChart();
                        }


                    }
                } else {
//                    path = path + ";" + "";
                    path.append(";").append("");
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + "";
                }

                //storing graph header info in HashMap
                graphMapDetails[i].put("graphId", graphId);
                graphMapDetails[i].put("graphName", graphName);
                graphMapDetails[i].put("graphWidth", graphWidth);
                graphMapDetails[i].put("graphHeight", graphHeight);
                graphMapDetails[i].put("graphClassName", graphClassName);
                graphMapDetails[i].put("graphTypeName", graphTypeName);
                graphMapDetails[i].put("SwapColumn", SwapColumn);

                graphMapDetails[i].put("graphLegend", grplegend);
                graphMapDetails[i].put("graphLegendLoc", grplegendloc);
                graphMapDetails[i].put("graphshowX", grpshox);
                graphMapDetails[i].put("graphshowY", grpshoy);
                graphMapDetails[i].put("graphLYaxislabel", grplyaxislabel);
                graphMapDetails[i].put("graphRYaxislabel", grpryaxislabel);
                graphMapDetails[i].put("graphDrill", grpdrill);
                graphMapDetails[i].put("graphBcolor", grpbcolor);
                graphMapDetails[i].put("graphFcolor", grpfcolor);
                graphMapDetails[i].put("graphData", grpdata);

                //storing graph details info in HashMap
                graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                graphMapDetails[i].put("viewByElementIds", viewByElementIds);
                graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                graphMapDetails[i].put("pieChartColumns", pieChartColumns);
                graphMapDetails[i].put("axis", axis);

                //storing graph details info (Dual Axis ) in HashMap
                graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);



            }

            //            if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            if (graphTitle != null && graphTitle.length() > 0) {
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }
            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);

            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    public HashMap getGraphsForDashBoard(String repId, HashMap DashBoardsGraphs) {

        try {
            pbretObj = getPbReturnObject(getDashboardGrpHdrsQuery(getReportId()));
            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];


            String[] grpIds = null;

            graphMapDetails = new HashMap[pbretObj.getRowCount()];


            grpIds = new String[pbretObj.getRowCount()];

            for (int i = 0; i < getGraphsCnt(); i++) {
                boolean SwapColumn = true;

                graphMapDetails[i] = new HashMap();

                grpIds[i] = pbretObj.getFieldValueString(i, tableDBColumns[0]);


                graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
                graphWidth = pbretObj.getFieldValueString(i, tableDBColumns[2]);
                graphHeight = pbretObj.getFieldValueString(i, tableDBColumns[3]);
                graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
                graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);

                graphSize = pbretObj.getFieldValueString(i, tableDBColumns[6]);
                grplegendloc = pbretObj.getFieldValueString(i, tableDBColumns[7]);
                grplegend = pbretObj.getFieldValueString(i, tableDBColumns[8]);
                grpshox = pbretObj.getFieldValueString(i, tableDBColumns[9]);
                grpshoy = pbretObj.getFieldValueString(i, tableDBColumns[10]);
                grpdrill = pbretObj.getFieldValueString(i, tableDBColumns[11]);
                grpbcolor = pbretObj.getFieldValueString(i, tableDBColumns[12]);
                grpfcolor = pbretObj.getFieldValueString(i, tableDBColumns[13]);
                grpdata = pbretObj.getFieldValueString(i, tableDBColumns[14]);
                grplyaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[15]);
                grpryaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);


                setGraphId(graphId);

                pbretObj2 = getPbReturnObject(getDashboardGrpDtlsQuery(graphId));
                barChartColumnTitles = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                barChartColumnNames = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                pieChartColumns = new String[getViewByElementIds().length + pbretObj2.getRowCount()];
                axis = new String[getViewByElementIds().length + pbretObj2.getRowCount()];

                String[] graphDBColumns = pbretObj2.getColumnNames();

                for (int viewbyIndex = 0; viewbyIndex < viewByElementIds.length; viewbyIndex++) {
                    barChartColumnTitles[viewbyIndex] = viewByColNames[viewbyIndex];
                    barChartColumnNames[viewbyIndex] = viewByElementIds[viewbyIndex];
                    pieChartColumns[viewbyIndex] = viewByElementIds[viewbyIndex];
                    axis[viewbyIndex] = "0";
                }

                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    barChartColumnTitles[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    barChartColumnNames[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    pieChartColumns[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    axis[viewByElementIds.length + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                }

                graph = new ProgenChartDatasets();
                graph.setBarChartColumnNames(barChartColumnNames);
                graph.setViewByColumns(viewByElementIds);
                graph.setBarChartColumnTitles(barChartColumnTitles);
                graph.setPieChartColumns(pieChartColumns);

                if (swapGraphAnalysis != null && swapGraphAnalysis.get(graphId) != null) {
                    graph.setSwapColumn(Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))));
                    SwapColumn = Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId)));
                } else {
                    graph.setSwapColumn(true);
                    SwapColumn = true;
                }

                //pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth), Integer.parseInt(graphHeight), ctxPath);
                pcharts[i] = new ProgenChartDisplay(400, 250, ctxPath);
                pchartsZoom[i] = new ProgenChartDisplay(700, 350, ctxPath);

                pcharts[i].setAlist(getCurrentDispRecords());
                pcharts[i].setGraph(graph);
                pcharts[i].setChartType(graphTypeName);
                pcharts[i].setGrplyaxislabel(grplyaxislabel);
                pcharts[i].setFunName(getJscal());
                pcharts[i].setSession(getSession());
                pcharts[i].setResponse(getResponse());
                pcharts[i].setOut(getOut());

                pchartsZoom[i].setAlist(getCurrentDispRecords());
                pchartsZoom[i].setGraph(graph);
                pchartsZoom[i].setChartType(graphTypeName);
                pchartsZoom[i].setGrplyaxislabel(grplyaxislabel);
                pchartsZoom[i].setFunName(getJscal());
                pchartsZoom[i].setSession(getSession());
                pchartsZoom[i].setResponse(getResponse());
                pchartsZoom[i].setOut(getOut());



                if (graphClassName.equalsIgnoreCase("Pie")) {
//                    path = path + ";" + pcharts[i].GetPieAxisChart();
                    path.append(";").append(pcharts[i].GetPieAxisChart());
                    graphTitle.append(";").append(graphName);
                    // pathZoom = pathZoom + ";" + pchartsZoom[i].GetPieAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Category")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    //  pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Meter")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    //   pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();

                } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    //   pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dial")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    //   pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {

//                    String axis1 = "";
//                    String axis2 = "";
                    StringBuilder axis1 = new StringBuilder(1000);
                    StringBuilder axis2 = new StringBuilder(1000);
                    for (int ind = 0; ind < axis.length; ind++) {
                        if (axis[ind].equalsIgnoreCase("0")) {
//                            axis1 = axis1 + "," + ind;
                            axis1.append(",").append(ind);
                        } else {
//                            axis2 = axis2 + "," + ind;
                            axis2.append(",").append(ind);
                        }
                    }
//                    if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                    if ((axis1.length() > 0) && (axis2.length() > 0)) {
//                        axis1 = axis1.substring(1);
//                        axis2 = axis2.substring(1);
                        axis1 = new StringBuilder(axis1.substring(1));
                        axis2 = new StringBuilder(axis2.substring(1));
                        String[] temp1 = axis1.toString().split(",");

                        barChartColumnTitles1 = new String[temp1.length + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        String[] temp2 = axis2.toString().split(",");
                        barChartColumnTitles2 = new String[temp2.length + viewByElementIds.length];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];

                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }

                        for (int j = 0; j < temp1.length; j++) {
                            barChartColumnTitles1[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp1[j])];
                            barChartColumnNames1[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp1[j])];
                        }
                        for (int j = 0; j < temp2.length; j++) {
                            barChartColumnTitles2[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp2[j])];
                            barChartColumnNames2[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp2[j])];
                        }
                    } else {
                        int count = 1;
                        barChartColumnTitles1 = new String[count + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }
                        for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                        }

                        for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                            barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                            barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                        }

                    }
                    graph1 = new ProgenChartDatasets();
                    graph2 = new ProgenChartDatasets();

                    graph1.setBarChartColumnNames(barChartColumnNames1);
                    graph1.setViewByColumns(viewByElementIds);
                    graph1.setBarChartColumnTitles(barChartColumnTitles1);
                    graph1.setPieChartColumns(barChartColumnNames1);

                    graph2.setBarChartColumnNames(barChartColumnNames2);
                    graph2.setViewByColumns(viewByElementIds);
                    graph2.setBarChartColumnTitles(barChartColumnTitles2);
                    graph2.setPieChartColumns(barChartColumnNames2);

                    pcharts[i].setGrpryaxislabel(grpryaxislabel);
                    pcharts[i].setGraph1(graph1);
                    pcharts[i].setGraph2(graph2);

                    pchartsZoom[i].setGrpryaxislabel(grpryaxislabel);
                    pchartsZoom[i].setGraph1(graph1);
                    pchartsZoom[i].setGraph2(graph2);

                    if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
                        //                            path = path + ";" + pcharts[i].GetDualAxisChart();
                        path.append(";").append(pcharts[i].GetDualAxisChart());
                        graphTitle.append(";").append(graphName);
                        //      pathZoom = pathZoom + ";" + pchartsZoom[i].GetDualAxisChart();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
//                        path = path + ";" + pcharts[i].GetOverlaidBar();
                        path.append(";").append(pcharts[i].GetOverlaidBar());
                        graphTitle.append(";").append(graphName);
                        //     pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidBar();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
//                        path = path + ";" + pcharts[i].GetOverlaidArea();
                        path.append(";").append(pcharts[i].GetOverlaidArea());
                        graphTitle.append(";").append(graphName);
                        //    pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidArea();
                    } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
//                        path = path + ";" + pcharts[i].GetFeverChart();
                        path.append(";").append(pcharts[i].GetFeverChart());
                        graphTitle.append(";").append(graphName);
                        //  pathZoom = pathZoom + ";" + pchartsZoom[i].GetFeverChart();
                    }





                }


                //storing graph header info in HashMap
                graphMapDetails[i].put("graphId", graphId);
                graphMapDetails[i].put("graphName", graphName);
                graphMapDetails[i].put("graphWidth", graphWidth);
                graphMapDetails[i].put("graphHeight", graphHeight);
                graphMapDetails[i].put("graphClassName", graphClassName);
                graphMapDetails[i].put("graphTypeName", graphTypeName);
                graphMapDetails[i].put("SwapColumn", SwapColumn);

                graphMapDetails[i].put("graphLegend", grplegend);
                graphMapDetails[i].put("graphLegendLoc", grplegendloc);
                graphMapDetails[i].put("graphshowX", grpshox);
                graphMapDetails[i].put("graphshowY", grpshoy);
                graphMapDetails[i].put("graphLYaxislabel", grplyaxislabel);
                graphMapDetails[i].put("graphRYaxislabel", grpryaxislabel);
                graphMapDetails[i].put("graphDrill", grpdrill);
                graphMapDetails[i].put("graphBcolor", grpbcolor);
                graphMapDetails[i].put("graphFcolor", grpfcolor);
                graphMapDetails[i].put("graphData", grpdata);

                //storing graph details info in HashMap
                graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                graphMapDetails[i].put("viewByElementIds", viewByElementIds);
                graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                graphMapDetails[i].put("pieChartColumns", pieChartColumns);
                graphMapDetails[i].put("axis", axis);

                //storing graph details info (Dual Axis ) in HashMap
                graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);

            }
            //            if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            if (graphTitle != null && graphTitle.length() > 0) {
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }
            // path = path.substring(1);
            //graphTitle = graphTitle.substring(1);

            String[] grpPaths = path.toString().split(",");
            HashMap graphPaths = new HashMap();
            for (int k = 0; k < grpPaths.length; k++) {
                graphPaths.put(grpIds[k], grpPaths[k]);
            }

            DashBoardsGraphs.put(repId, graphPaths);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return DashBoardsGraphs;
    }

    public String getCtxPath() {
        return ctxPath;
    }

    public void setCtxPath(String ctxPath) {
        this.ctxPath = ctxPath;
    }

    public int getGraphsCnt() {
        return graphsCnt;
    }

    public void setGraphsCnt(int graphsCnt) {
        this.graphsCnt = graphsCnt;
    }

    //added by santhosh.kumar@progenbusiness.com on 30/07/09 for getting number of graphs for a table
    public HashMap getSwapGraphAnalysis() {
        return swapGraphAnalysis;
    }

    public void setSwapGraphAnalysis(HashMap swapGraphAnalysis) {
        this.swapGraphAnalysis = swapGraphAnalysis;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String[] getViewByElementIds() {
        return viewByElementIds;
    }

    public void setViewByElementIds(String[] viewByElementIds) {
        this.viewByElementIds = viewByElementIds;
    }

    public String[] getViewByColNames() {
        return viewByColNames;
    }

    public void setViewByColNames(String[] viewByColNames) {
        this.viewByColNames = viewByColNames;
    }

    public HashMap[] getGraphMapDetails() {
        return graphMapDetails;
    }

    public void setGraphMapDetails(HashMap[] graphMapDetails) {
        this.graphMapDetails = graphMapDetails;
    }

    public static void main(String[] a) {
        try {

            PbGraphDisplay disp = new PbGraphDisplay();
            String[] viewByElementIds = {"3992"};
            String[] viewByCOlnames = {"3992"};

            disp.setViewByElementIds(viewByElementIds);
            disp.setViewByColNames(viewByCOlnames);
            disp.setReportId("1");

            // ArrayList grpDetails = disp.getGraphHeaders();
        } catch (Exception exp) {
            logger.error("Exception:", exp);
        }

    }

    public String[] getReportIds() {
        return reportIds;
    }

    public void setReportIds(String[] reportIds) {
        this.reportIds = reportIds;
    }

    public String[] getGraphIds() {
        return graphIds;
    }

    public void setGraphIds(String[] graphIds) {
        this.graphIds = graphIds;
    }

    public ArrayList getDashboardGraphHeadersNew(String graphId) {
        ArrayList grpDetails = new ArrayList();
        /*
         * if (graphMapDetails != null && graphMapDetails.length != 0) { } else
         * { }
         */

        try {
            pbretObj = getPbReturnObject(getDashboardGrpHdrsQueryNew(graphId));
            String[] tableDBColumns = pbretObj.getColumnNames();
            setGraphsCnt(pbretObj.getRowCount());
            pcharts = new ProgenChartDisplay[getGraphsCnt()];
            pchartsZoom = new ProgenChartDisplay[getGraphsCnt()];
            graphMapDetails = new HashMap[pbretObj.getRowCount()];

            for (int i = 0; i < getGraphsCnt(); i++) {
                boolean SwapColumn = true;
                graphMapDetails[i] = new HashMap();

                graphId = pbretObj.getFieldValueString(i, tableDBColumns[0]);
                graphName = pbretObj.getFieldValueString(i, tableDBColumns[1]);
                graphWidth = pbretObj.getFieldValueString(i, tableDBColumns[2]);
                graphHeight = pbretObj.getFieldValueString(i, tableDBColumns[3]);
                graphClassName = pbretObj.getFieldValueString(i, tableDBColumns[4]);
                graphTypeName = pbretObj.getFieldValueString(i, tableDBColumns[5]);

                graphSize = pbretObj.getFieldValueString(i, tableDBColumns[6]);
                grplegendloc = pbretObj.getFieldValueString(i, tableDBColumns[7]);
                grplegend = pbretObj.getFieldValueString(i, tableDBColumns[8]);
                grpshox = pbretObj.getFieldValueString(i, tableDBColumns[9]);
                grpshoy = pbretObj.getFieldValueString(i, tableDBColumns[10]);
                grpdrill = pbretObj.getFieldValueString(i, tableDBColumns[11]);
                grpbcolor = pbretObj.getFieldValueString(i, tableDBColumns[12]);
                grpfcolor = pbretObj.getFieldValueString(i, tableDBColumns[13]);
                grpdata = pbretObj.getFieldValueString(i, tableDBColumns[14]);
                grplyaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[15]);
                grpryaxislabel = pbretObj.getFieldValueString(i, tableDBColumns[16]);


                setGraphId(graphId);

                pbretObj2 = getPbReturnObject(getDashboardGrpDtlsQuery(graphId));
                barChartColumnTitles = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                barChartColumnNames = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                //pieChartColumns = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];
                axis = new String[collect.reportRowViewbyValues.size() + pbretObj2.getRowCount()];

                String[] graphDBColumns = pbretObj2.getColumnNames();

                for (int viewbyIndex = 0; viewbyIndex < collect.reportRowViewbyValues.size(); viewbyIndex++) {
                    barChartColumnTitles[viewbyIndex] = "A_" + (String) collect.reportRowViewbyValues.get(viewbyIndex);
                    barChartColumnNames[viewbyIndex] = "A_" + (String) collect.reportRowViewbyValues.get(viewbyIndex);
                    axis[viewbyIndex] = "0";
                }

                for (int j = 0; j < pbretObj2.getRowCount(); j++) {
                    barChartColumnTitles[collect.reportRowViewbyValues.size() + j] = pbretObj2.getFieldValueString(j, graphDBColumns[1]);
                    barChartColumnNames[collect.reportRowViewbyValues.size() + j] = "A_" + pbretObj2.getFieldValueString(j, graphDBColumns[0]);
                    axis[collect.reportRowViewbyValues.size() + j] = pbretObj2.getFieldValueString(j, graphDBColumns[2]);
                }
                pieChartColumns = barChartColumnNames;

                graph = new ProgenChartDatasets();
                graph.setBarChartColumnNames(barChartColumnNames);
                graph.setBarChartColumnTitles(barChartColumnTitles);
                graph.setPieChartColumns(pieChartColumns);

                String[] newStr = new String[collect.reportRowViewbyValues.size()];
                for (int k = 0; k < collect.reportRowViewbyValues.size(); k++) {
                    if (String.valueOf(collect.reportRowViewbyValues.get(k)).equalsIgnoreCase("Time")) {
                        newStr[k] = String.valueOf(collect.reportRowViewbyValues.get(k));
                    } else {
                        newStr[k] = "A_" + String.valueOf(collect.reportRowViewbyValues.get(k));
                    }

                }
                graph.setViewByColumns(newStr);

                //pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth), Integer.parseInt(graphHeight), ctxPath);
                pcharts[i] = new ProgenChartDisplay(400, 250, ctxPath);
                pchartsZoom[i] = new ProgenChartDisplay(700, 350, ctxPath);

                pcharts[i].setAlist(getCurrentDispRecords());
                pcharts[i].setGraph(graph);
                pcharts[i].setChartType(graphTypeName);
                pcharts[i].setGrplyaxislabel(grplyaxislabel);
                pcharts[i].setFunName(getJscal());
                pcharts[i].setSession(getSession());
                pcharts[i].setResponse(getResponse());
                pcharts[i].setOut(getOut());

                pchartsZoom[i].setAlist(getCurrentDispRecords());
                pchartsZoom[i].setGraph(graph);
                pchartsZoom[i].setChartType(graphTypeName);
                pchartsZoom[i].setGrplyaxislabel(grplyaxislabel);
                pchartsZoom[i].setFunName(getJscal());
                pchartsZoom[i].setSession(getSession());
                pchartsZoom[i].setResponse(getResponse());
                pchartsZoom[i].setOut(getOut());


                if (graphClassName.equalsIgnoreCase("Pie")) {
//                    path = path + ";" + pcharts[i].GetPieAxisChart();
                    path.append(";").append(pcharts[i].GetPieAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetPieAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Category")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Meter")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();

                } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);
                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dial")) {
//                    path = path + ";" + pcharts[i].GetCategoryAxisChart();
                    path.append(";").append(pcharts[i].GetCategoryAxisChart());
                    graphTitle.append(";").append(graphName);

                    pathZoom = pathZoom + ";" + pchartsZoom[i].GetCategoryAxisChart();
                } else if (graphClassName.equalsIgnoreCase("Dual Axis")) {
//                    String axis1 = "";
//                    String axis2 = "";
                    StringBuilder axis1 = new StringBuilder(1000);
                    StringBuilder axis2 = new StringBuilder(1000);
                    for (int ind = 0; ind < axis.length; ind++) {
                        if (axis[ind].equalsIgnoreCase("0")) {
//                            axis1 = axis1 + "," + ind;
                            axis1.append(",").append(ind);
                        } else {
//                            axis2 = axis2 + "," + ind;
                            axis2.append(",").append(ind);
                        }
                    }
                    //                    if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
                    if ((axis1.length() > 0) && (axis2.length() > 0)) {
//                        axis1 = axis1.substring(1);
//                        axis2 = axis2.substring(1);
                        axis1 = new StringBuilder(axis1.substring(1));
                        axis2 = new StringBuilder(axis2.substring(1));
                        String[] temp1 = axis1.toString().split(",");

                        barChartColumnTitles1 = new String[temp1.length + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        String[] temp2 = axis2.toString().split(",");
                        barChartColumnTitles2 = new String[temp2.length + viewByElementIds.length];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];

                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }

                        for (int j = 0; j < temp1.length; j++) {
                            barChartColumnTitles1[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp1[j])];
                            barChartColumnNames1[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp1[j])];
                        }
                        for (int j = 0; j < temp2.length; j++) {
                            barChartColumnTitles2[viewByElementIds.length + j] = barChartColumnTitles[Integer.parseInt(temp2[j])];
                            barChartColumnNames2[viewByElementIds.length + j] = barChartColumnNames[Integer.parseInt(temp2[j])];
                        }
                    } else {
                        int count = 1;
                        barChartColumnTitles1 = new String[count + viewByElementIds.length];
                        barChartColumnNames1 = new String[barChartColumnTitles1.length];

                        barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
                        barChartColumnNames2 = new String[barChartColumnTitles2.length];

                        for (int j = 0; j < viewByElementIds.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnTitles2[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                            barChartColumnNames2[j] = barChartColumnNames[j];
                        }
                        for (int j = viewByElementIds.length; j < barChartColumnTitles1.length; j++) {
                            barChartColumnTitles1[j] = barChartColumnTitles[j];
                            barChartColumnNames1[j] = barChartColumnNames[j];
                        }

                        for (int j = barChartColumnTitles1.length; j < barChartColumnTitles.length; j++) {
                            barChartColumnTitles2[j - viewByElementIds.length] = barChartColumnTitles[j];
                            barChartColumnNames2[j - viewByElementIds.length] = barChartColumnNames[j];
                        }

                    }
                    graph1 = new ProgenChartDatasets();
                    graph2 = new ProgenChartDatasets();

                    graph1.setBarChartColumnNames(barChartColumnNames1);
                    graph1.setViewByColumns(viewByElementIds);
                    graph1.setBarChartColumnTitles(barChartColumnTitles1);
                    graph1.setPieChartColumns(barChartColumnNames1);

                    graph2.setBarChartColumnNames(barChartColumnNames2);
                    graph2.setViewByColumns(viewByElementIds);
                    graph2.setBarChartColumnTitles(barChartColumnTitles2);
                    graph2.setPieChartColumns(barChartColumnNames2);

                    pcharts[i].setGrpryaxislabel(grpryaxislabel);
                    pcharts[i].setGraph1(graph1);
                    pcharts[i].setGraph2(graph2);

                    pchartsZoom[i].setGrpryaxislabel(grpryaxislabel);
                    pchartsZoom[i].setGraph1(graph1);
                    pchartsZoom[i].setGraph2(graph2);

                    if (graphTypeName.equalsIgnoreCase("Dual Axis")) {
                        //  path = path + ";" + pcharts[i].GetDualAxisChart();
                        path.append(";").append(pcharts[i].GetDualAxisChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetDualAxisChart();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidBar")) {
//                        path = path + ";" + pcharts[i].GetOverlaidBar();
                        path.append(";").append(pcharts[i].GetOverlaidBar());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidBar();
                    } else if (graphTypeName.equalsIgnoreCase("OverlaidArea")) {
//                        path = path + ";" + pcharts[i].GetOverlaidArea();
                        path.append(";").append(pcharts[i].GetOverlaidArea());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetOverlaidArea();
                    } else if (graphTypeName.equalsIgnoreCase("FeverChart")) {
//                        path = path + ";" + pcharts[i].GetFeverChart();
                        path.append(";").append(pcharts[i].GetFeverChart());
                        graphTitle.append(";").append(graphName);
                        pathZoom = pathZoom + ";" + pchartsZoom[i].GetFeverChart();
                    }



                }

                //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("Graph details storage ");

                //storing graph header info in HashMap
                graphMapDetails[i].put("graphId", graphId);
                graphMapDetails[i].put("graphName", graphName);
                graphMapDetails[i].put("graphWidth", graphWidth);
                graphMapDetails[i].put("graphHeight", graphHeight);
                graphMapDetails[i].put("graphClassName", graphClassName);
                graphMapDetails[i].put("graphTypeName", graphTypeName);
                graphMapDetails[i].put("SwapColumn", SwapColumn);

                graphMapDetails[i].put("graphLegend", grplegend);
                graphMapDetails[i].put("graphLegendLoc", grplegendloc);
                graphMapDetails[i].put("graphshowX", grpshox);
                graphMapDetails[i].put("graphshowY", grpshoy);
                graphMapDetails[i].put("graphLYaxislabel", grplyaxislabel);
                graphMapDetails[i].put("graphRYaxislabel", grpryaxislabel);
                graphMapDetails[i].put("graphDrill", grpdrill);
                graphMapDetails[i].put("graphBcolor", grpbcolor);
                graphMapDetails[i].put("graphFcolor", grpfcolor);
                graphMapDetails[i].put("graphData", grpdata);

                //storing graph details info in HashMap
                graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
                graphMapDetails[i].put("viewByElementIds", newStr);
                graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
                graphMapDetails[i].put("pieChartColumns", pieChartColumns);
                graphMapDetails[i].put("axis", axis);

                //storing graph details info (Dual Axis ) in HashMap
                graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
                graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
                graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
                graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2);

            }
            //            if (!path.equalsIgnoreCase("")) {
            if (path != null && path.length() > 0) {
                path = new StringBuilder(path.substring(1));
            }
            if (graphTitle != null && graphTitle.length() > 0) {
                graphTitle = new StringBuilder(graphTitle.substring(1));
            }
            // path = path.substring(1);
            //graphTitle = graphTitle.substring(1);
            grpDetails.add(path);
            grpDetails.add(graphTitle);
            grpDetails.add(pcharts);

            grpDetails.add(pathZoom);
            grpDetails.add(pchartsZoom);
            grpDetails.add(graphMapDetails);

        } catch (Exception exception) {
            logger.error("Exception:", exception);
        }
        return grpDetails;
    }

    /*
     * public ArrayList get2dGraphHeaders(String[] barChartColumnNames, String[]
     * barChartColumnTitles, String[] viewByColumns) {
     *
     * ArrayList grpDetails = new ArrayList();
     *
     *
     * if (graphMapDetails != null && graphMapDetails.length != 0) { } else { }
     *
     *
     * try {
     *
     *
     * pbretObj = getPbReturnObject(getDashboardGrpHdrsQuery(getReportId()));
     * graphMapDetails = new HashMap[pbretObj.getRowCount()]; dbColumns =
     * pbretObj.getColumnNames(); setGraphsCnt(pbretObj.getRowCount()); pcharts
     * = new ProgenChartDisplay[getGraphsCnt()]; pchartsZoom = new
     * ProgenChartDisplay[getGraphsCnt()]; for (int i = 0; i < getGraphsCnt();
     * i++) { graphMapDetails[i] = new HashMap();
     *
     * boolean SwapColumn = true; graphId = pbretObj.getFieldValueString(i,
     * dbColumns[0]); graphName = pbretObj.getFieldValueString(i, dbColumns[1]);
     * graphWidth = pbretObj.getFieldValueString(i, dbColumns[2]); graphHeight =
     * pbretObj.getFieldValueString(i, dbColumns[3]); graphClassName =
     * pbretObj.getFieldValueString(i, dbColumns[4]); graphTypeName =
     * pbretObj.getFieldValueString(i, dbColumns[5]);
     *
     * setGraphId(graphId);
     *
     * this.barChartColumnTitles = barChartColumnTitles;
     * this.barChartColumnNames = barChartColumnNames; this.pieChartColumns =
     * barChartColumnNames; this.viewByElementIds = viewByColumns; axis = new
     * String[barChartColumnNames.length];
     *
     *
     * //String[] graphDBColumns = pbretObj2.getColumnNames();
     *
     * for (int j = 0; j < axis.length; j++) { axis[j] = "0"; }
     *
     *
     * graph = new ProgenChartDatasets();
     * graph.setBarChartColumnNames(barChartColumnNames);
     * graph.setViewByColumns(viewByColumns);
     * graph.setBarChartColumnTitles(barChartColumnTitles);
     *
     *
     * if (swapGraphAnalysis != null && swapGraphAnalysis.get(graphId) != null)
     * {
     * graph.setSwapColumn(Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))));
     * SwapColumn =
     * Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))); }
     * else { graph.setSwapColumn(true); SwapColumn = true; }
     *
     *
     *
     *
     * //pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth),
     * Integer.parseInt(graphHeight), ctxPath); pcharts[i] = new
     * ProgenChartDisplay(400, 250, ctxPath); pchartsZoom[i] = new
     * ProgenChartDisplay(700, 350, ctxPath);
     *
     * pcharts[i].setAlist(getCurrentDispRecords()); pcharts[i].setGraph(graph);
     * pcharts[i].setChartType(graphTypeName);
     * pcharts[i].setGrplyaxislabel(grplyaxislabel);
     * pcharts[i].setFunName(getJscal()); pcharts[i].setSession(getSession());
     * pcharts[i].setResponse(getResponse()); pcharts[i].setOut(getOut());
     *
     * pchartsZoom[i].setAlist(getCurrentDispRecords());
     * pchartsZoom[i].setGraph(graph);
     * pchartsZoom[i].setChartType(graphTypeName);
     * pchartsZoom[i].setGrplyaxislabel(grplyaxislabel);
     * pchartsZoom[i].setFunName(getJscal());
     * pchartsZoom[i].setSession(getSession());
     * pchartsZoom[i].setResponse(getResponse());
     * pchartsZoom[i].setOut(getOut());
     *
     *
     *
     * if (graphClassName.equalsIgnoreCase("Pie")) { path = path + ";" +
     * pcharts[i].GetPieAxisChart(); graphTitle.append(";" ).append(graphName);
     * pathZoom = pathZoom + ";" + pchartsZoom[i].GetPieAxisChart(); } else if
     * (graphClassName.equalsIgnoreCase("Category")) { path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(); graphTitle.append(";"
     * ).append(graphName); pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(); } else if
     * (graphClassName.equalsIgnoreCase("Meter")) { path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(); graphTitle.append(";"
     * ).append(graphName); pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart();
     *
     * } else if (graphClassName.equalsIgnoreCase("Candle Stick")) { path = path
     * + ";" + pcharts[i].GetCategoryAxisChart(); graphTitle.append(";"
     * ).append(graphName); pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(); } else if
     * (graphClassName.equalsIgnoreCase("Dial")) { path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(); graphTitle.append(";"
     * ).append(graphName); pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(); } else if
     * (graphClassName.equalsIgnoreCase("Dual Axis")) { String axis1 = "";
     * String axis2 = "";
     *
     * for (int ind = 0; ind < axis.length; ind++) { if
     * (axis[ind].equalsIgnoreCase("0")) { axis1 = axis1 + "," + ind; } else {
     * axis2 = axis2 + "," + ind; } } if ((!axis1.equalsIgnoreCase("")) &&
     * (!axis2.equalsIgnoreCase(""))) {
     *
     * axis1 = axis1.substring(1); axis2 = axis2.substring(1);
     *
     * String[] temp1 = axis1.split(",");
     *
     * barChartColumnTitles1 = new String[temp1.length + viewByColumns.length];
     * barChartColumnNames1 = new String[barChartColumnTitles1.length];
     *
     * String[] temp2 = axis2.split(","); barChartColumnTitles2 = new
     * String[temp2.length + viewByColumns.length]; barChartColumnNames2 = new
     * String[barChartColumnTitles2.length];
     *
     * for (int j = 0; j < viewByColumns.length; j++) { barChartColumnTitles1[j]
     * = barChartColumnTitles[j]; barChartColumnTitles2[j] =
     * barChartColumnTitles[j];
     *
     * barChartColumnNames1[j] = barChartColumnNames[j]; barChartColumnNames2[j]
     * = barChartColumnNames[j]; }
     *
     * for (int j = 0; j < temp1.length; j++) {
     * barChartColumnTitles1[viewByColumns.length + j] =
     * barChartColumnTitles[Integer.parseInt(temp1[j])];
     * barChartColumnNames1[viewByColumns.length + j] =
     * barChartColumnNames[Integer.parseInt(temp1[j])]; } for (int j = 0; j <
     * temp2.length; j++) { barChartColumnTitles2[viewByColumns.length + j] =
     * barChartColumnTitles[Integer.parseInt(temp2[j])];
     * barChartColumnNames2[viewByColumns.length + j] =
     * barChartColumnNames[Integer.parseInt(temp2[j])]; } } else { int count =
     * 1; barChartColumnTitles1 = new String[count + viewByColumns.length];
     * barChartColumnNames1 = new String[barChartColumnTitles1.length];
     *
     * barChartColumnTitles2 = new String[(barChartColumnNames.length - count)];
     * barChartColumnNames2 = new String[barChartColumnTitles2.length];
     *
     * for (int j = 0; j < viewByColumns.length; j++) { barChartColumnTitles1[j]
     * = barChartColumnTitles[j]; barChartColumnTitles2[j] =
     * barChartColumnTitles[j]; barChartColumnNames1[j] =
     * barChartColumnNames[j]; barChartColumnNames2[j] = barChartColumnNames[j];
     * } for (int j = viewByColumns.length; j < barChartColumnTitles1.length;
     * j++) { barChartColumnTitles1[j] = barChartColumnTitles[j];
     * barChartColumnNames1[j] = barChartColumnNames[j]; }
     *
     * for (int j = barChartColumnTitles1.length; j <
     * barChartColumnTitles.length; j++) { barChartColumnTitles2[j -
     * viewByColumns.length] = barChartColumnTitles[j]; barChartColumnNames2[j -
     * viewByColumns.length] = barChartColumnNames[j]; }
     *
     * }
     * graph1 = new ProgenChartDatasets(); graph2 = new ProgenChartDatasets();
     *
     * graph1.setBarChartColumnNames(barChartColumnNames1);
     * graph1.setViewByColumns(viewByColumns);
     * graph1.setBarChartColumnTitles(barChartColumnTitles1);
     * //graph1.setPieChartColumns(pieChartColumns);
     * graph1.setPieChartColumns(barChartColumnNames1);
     *
     * graph2.setBarChartColumnNames(barChartColumnNames2);
     * graph2.setViewByColumns(viewByColumns);
     * graph2.setBarChartColumnTitles(barChartColumnTitles2);
     * //graph2.setPieChartColumns(pieChartColumns);
     * graph2.setPieChartColumns(barChartColumnNames2);
     *
     * pcharts[i].setGrpryaxislabel(grpryaxislabel);
     * pcharts[i].setGraph1(graph1); pcharts[i].setGraph2(graph2);
     *
     * pchartsZoom[i].setGrpryaxislabel(grpryaxislabel);
     * pchartsZoom[i].setGraph1(graph1); pchartsZoom[i].setGraph2(graph2);
     *
     * if (graphTypeName.equalsIgnoreCase("Dual Axis")) { path = path + ";" +
     * pcharts[i].GetDualAxisChart(); graphTitle.append(";" ).append(graphName);
     * pathZoom = pathZoom + ";" + pchartsZoom[i].GetDualAxisChart(); } else if
     * (graphTypeName.equalsIgnoreCase("Overlaid")) { path = path + ";" +
     * pcharts[i].GetSingleAxisChart(); graphTitle.append(";"
     * ).append(graphName); pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetSingleAxisChart(); } }
     *
     *
     * //storing graph header info in HashMap graphMapDetails[i].put("graphId",
     * graphId); graphMapDetails[i].put("graphName", graphName);
     * graphMapDetails[i].put("graphWidth", graphWidth);
     * graphMapDetails[i].put("graphHeight", graphHeight);
     * graphMapDetails[i].put("graphClassName", graphClassName);
     * graphMapDetails[i].put("graphTypeName", graphTypeName);
     * graphMapDetails[i].put("SwapColumn", SwapColumn);
     *
     * graphMapDetails[i].put("graphLegend", grplegend);
     * graphMapDetails[i].put("graphLegendLoc", grplegendloc);
     * graphMapDetails[i].put("graphshowX", grpshox);
     * graphMapDetails[i].put("graphshowY", grpshoy);
     * graphMapDetails[i].put("graphLYaxislabel", grplyaxislabel);
     * graphMapDetails[i].put("graphRYaxislabel", grpryaxislabel);
     * graphMapDetails[i].put("graphDrill", grpdrill);
     * graphMapDetails[i].put("graphBcolor", grpbcolor);
     * graphMapDetails[i].put("graphFcolor", grpfcolor);
     * graphMapDetails[i].put("graphData", grpdata);
     *
     * //storing graph details info in HashMap
     * graphMapDetails[i].put("barChartColumnNames", barChartColumnNames);
     * graphMapDetails[i].put("viewByElementIds", viewByColumns);
     * graphMapDetails[i].put("barChartColumnTitles", barChartColumnTitles);
     * graphMapDetails[i].put("pieChartColumns", pieChartColumns);
     * graphMapDetails[i].put("axis", axis);
     *
     * //storing graph details info (Dual Axis ) in HashMap
     * graphMapDetails[i].put("barChartColumnNames1", barChartColumnNames1);
     * graphMapDetails[i].put("barChartColumnTitles1", barChartColumnTitles1);
     * graphMapDetails[i].put("barChartColumnNames2", barChartColumnNames2);
     * graphMapDetails[i].put("barChartColumnTitles2", barChartColumnTitles2); }
     *
     * if (!path.equalsIgnoreCase("")) { path = path.substring(1); } if
     * (!graphTitle.equalsIgnoreCase("")) { graphTitle = graphTitle; }
     *
     *
     * grpDetails.add(path); grpDetails.add(graphTitle);
     * grpDetails.add(pcharts);
     *
     * grpDetails.add(pathZoom); grpDetails.add(pchartsZoom);
     * grpDetails.add(graphMapDetails);
     *
     * return grpDetails; } catch (Exception exception) {
     * logger.error("Exception:",exception); return grpDetails; } }
     */
}
