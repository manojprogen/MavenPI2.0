/**
 * @filename ProgenGraphDisp.java
 *
 * @author santhosh.kumar@progenbusiness.com @date June 8, 2009, 1:18 PM
 */
package com.progen.charts;

import java.io.Serializable;
import org.apache.log4j.Logger;
/*
 * import utils.db.*; import java.util.*; import java.sql.*; import prg.db.*;
 * import javax.servlet.http.*;
 */

/**
 *
 * @author Administrator
 */
public class ProgenGraphDisp implements Serializable {

    public static Logger logger = Logger.getLogger(ProgenGraphDisp.class);
    /*
     * private String tableId; private ArrayList currentDispRecords; private
     * ResultSet resultSet = null; private ProgenConnection progenConnection =
     * null; private Connection connection = null; private PreparedStatement
     * preparedStatement = null; private PbReturnObject pbretObj = null; private
     * PbReturnObject pbretObj2 = null; private String[] tableColumns = null;
     * private String[] graphColumns = null; private String[] viewByColumns =
     * null; private String[] pieChartColumns = null; private String[]
     * barChartColumnNames = null; private String[] barChartColumnTitles = null;
     * private ProgenChartDatasets graph = null; private ProgenChartDisplay[]
     * pcharts = null; private String path = ""; private String graphTitle = "";
     * private String graphId = null; private String graphName = null; private
     * String graphWidth = null; private String graphHeight = null; private
     * String graphClassName = null; private String graphTypeName = null;
     * private int viewBy = 0; private String jscal = "1"; private HttpSession
     * session = null; private HttpServletResponse response = null; private
     * Writer out = null; private int graphsCnt = 0; private String ctxPath =
     * null; //added by santhosh.kumar@progenbusiness.com on 27/07/09 private
     * String[] axis = null; private String[] barChartColumnNames1 = null;
     * private String[] barChartColumnTitles1 = null; private String[]
     * barChartColumnNames2 = null; private String[] barChartColumnTitles2 =
     * null; private ProgenChartDatasets graph1 = null; private
     * ProgenChartDatasets graph2 = null; private HashMap swapGraphAnalysis =
     * null; private ProgenChartDisplay[] pchartsZoom = null; private String
     * pathZoom = "";
     *
     *
     * public ProgenGraphDisp() { }
     *
     * public String getTableId() { return tableId; }
     *
     * public void setTableId(String tableId) { this.tableId = tableId; }
     *
     * public String getGraphId() { return graphId; }
     *
     * public void setGraphId(String graphId) { this.graphId = graphId; }
     *
     * public ArrayList getCurrentDispRecords() { return currentDispRecords; }
     *
     * public String getJscal() { return jscal; }
     *
     * public void setJscal(String jscal) { this.jscal = jscal; }
     *
     * public HttpSession getSession() { return session; }
     *
     * public void setSession(HttpSession session) { this.session = session; }
     *
     * public HttpServletResponse getResponse() { return response; }
     *
     * public void setResponse(HttpServletResponse response) { this.response =
     * response; }
     *
     * public void setCurrentDispRecords(ArrayList currentDispRecords) {
     * this.currentDispRecords = currentDispRecords; }
     *
     * public Writer getOut() { return out; }
     *
     * public void setOut(Writer out) { this.out = out; }
     *
     * public String getGrpHdrsQuery(String tabId) { String grpHdrsQuery =
     * "select A.GRAPH_ID,nvl(A.GRAPH_DISP_NAME,A.GRAPH_NAME)
     * GRAPH_NAME,B.X_SIZE GRAPH_WIDTH,B.Y_SIZE GRAPH_HEIGHT, " +
     * "C.GRAPH_CLASS_NAME, D.GRAPH_TYPE_NAME from PRG_GRAPH_HEADER A, " +
     * "PRG_GRAPH_SIZES B, PRG_GRAPH_CLASS C,PRG_GRAPH_TYPE D where A.TABLE_ID="
     * + tabId + " AND A.GRAPH_SIZE=B.GRAPH_SIZE_ID AND " +
     * "A.GRAPH_CLASS=C.GRAPH_CLASS_ID AND A.GRAPH_TYPE=D.GRAPH_TYPE_ID order by
     * A.GRAPH_ORDER,A.GRAPH_ID";
     *
     * return grpHdrsQuery; }
     *
     * public String getGrpDtlsQuery(String grpId, String tabId) { String
     * grpDtlsQuery = "SELECT
     * NVL(NVL(A.COLUMN_NAME,A.COLUMN_DISP_NAME),B.COLUMN_DISP_NAME)COLUMN_TITLE,upper(B.COLUMN_NAME)
     * COLUMN_NAME,B.IS_VIEW_BY_COLUMN,A.AXIS " + "FROM PRG_GRAPH_DETAILS A,
     * PRG_REPORT_TABLE_DETAILS B WHERE A.TABLE_COLUMN_ID=B.REPORT_COLUMN_ID AND
     * A.GRAPH_ID=" + grpId + " and A.TABLE_ID=" + tabId + " ORDER BY
     * B.IS_VIEW_BY_COLUMN DESC,A.COLUMN_SEQ"; return grpDtlsQuery; }
     *
     * public PbReturnObject getPbReturnObject(String sql) { PbReturnObject
     * pbretObj3 = null; try { if (progenConnection == null || connection ==
     * null) { progenConnection = new ProgenConnection(); connection =
     * ProgenConnection.getConnection(); } preparedStatement =
     * connection.prepareStatement(sql); resultSet =
     * preparedStatement.executeQuery(); pbretObj3 = new
     * PbReturnObject(resultSet); return pbretObj3; } catch (Exception
     * exception) { logger.error("Exception:",exception); return pbretObj3; } }
     *
     * public ArrayList getGraphHeaders() { ArrayList grpDetails = new
     * ArrayList(); try {
     *
     * pbretObj = getPbReturnObject(getGrpHdrsQuery(getTableId())); tableColumns
     * = pbretObj.getColumnNames(); setGraphsCnt(pbretObj.getRowCount());
     * pcharts = new ProgenChartDisplay[getGraphsCnt()]; pchartsZoom = new
     * ProgenChartDisplay[getGraphsCnt()]; for (int i = 0; i < getGraphsCnt();
     * i++) { graphId = pbretObj.getFieldValue(i, "GRAPH_ID").toString();
     * graphName = pbretObj.getFieldValue(i, "GRAPH_NAME").toString();
     * graphWidth = pbretObj.getFieldValue(i, "GRAPH_WIDTH").toString();
     * graphHeight = pbretObj.getFieldValue(i, "GRAPH_HEIGHT").toString();
     * graphClassName = pbretObj.getFieldValue(i,
     * "GRAPH_CLASS_NAME").toString(); graphTypeName = pbretObj.getFieldValue(i,
     * "GRAPH_TYPE_NAME").toString();
     *
     * setGraphId(graphId); pbretObj2 =
     * getPbReturnObject(getGrpDtlsQuery(graphId, getTableId()));
     * barChartColumnTitles = new String[pbretObj2.getRowCount()];
     * barChartColumnNames = new String[pbretObj2.getRowCount()];
     * pieChartColumns = new String[pbretObj2.getRowCount()]; axis = new
     * String[pbretObj2.getRowCount()];
     *
     * graphColumns = pbretObj2.getColumnNames();
     *
     * for (int j = 0; j < pbretObj2.getRowCount(); j++) {
     * barChartColumnTitles[j] = pbretObj2.getFieldValueString(j,
     * graphColumns[0]); barChartColumnNames[j] =
     * pbretObj2.getFieldValueString(j, graphColumns[1]); pieChartColumns[j] =
     * pbretObj2.getFieldValueString(j, graphColumns[1]); //added on 27/07/09
     * for the purpose of Dial Axis Chart axis[j] =
     * pbretObj2.getFieldValueString(j, graphColumns[3]);
     *
     * if (pbretObj2.getFieldValueString(j,
     * graphColumns[2]).equalsIgnoreCase("Y")) { viewBy++; } } viewByColumns =
     * new String[viewBy]; for (int k = 0; k < viewBy; k++) { viewByColumns[k] =
     * barChartColumnNames[k]; } viewBy = 0;
     *
     * graph = new ProgenChartDatasets();
     * graph.setBarChartColumnNames(barChartColumnNames);
     * graph.setViewByColumns(viewByColumns);
     * graph.setBarChartColumnTitles(barChartColumnTitles);
     * graph.setPieChartColumns(pieChartColumns);
     *
     *
     * if (swapGraphAnalysis != null && swapGraphAnalysis.get(graphId) != null)
     * {
     * graph.setSwapColumn(Boolean.parseBoolean(String.valueOf(swapGraphAnalysis.get(graphId))));
     * } else { graph.setSwapColumn(true); } //graph.setSwapColumn(swapColumn);
     * pcharts[i] = new ProgenChartDisplay(Integer.parseInt(graphWidth),
     * Integer.parseInt(graphHeight), ctxPath); pchartsZoom[i] = new
     * ProgenChartDisplay(700, 350, ctxPath);
     *
     * if (graphClassName.equalsIgnoreCase("Pie")) {
     *
     * path = path + ";" + pcharts[i].GetPieAxisChart(getCurrentDispRecords(),
     * graph, graphTypeName, getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetPieAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, getJscal(), getSession(), getResponse(), getOut()); } else
     * if (graphClassName.equalsIgnoreCase("Category")) {
     *
     * path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()); }
     * else if (graphClassName.equalsIgnoreCase("Meter")) {
     *
     * path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     *
     * } else if (graphClassName.equalsIgnoreCase("Candle Stick")) {
     *
     * path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()); }
     * else if (graphClassName.equalsIgnoreCase("Dial")) {
     *
     * path = path + ";" +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()); }
     * else if (graphClassName.equalsIgnoreCase("Dual Axis")) { String axis1 =
     * ""; String axis2 = "";
     *
     * for (int ind = viewByColumns.length; ind < axis.length; ind++) { if
     * (axis[ind].equalsIgnoreCase("0")) { axis1 = axis1 + "," + ind; } else {
     * axis2 = axis2 + "," + ind; } }
     *
     * if ((!axis1.equalsIgnoreCase("")) && (!axis2.equalsIgnoreCase(""))) {
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
     * barChartColumnTitles[j]; barChartColumnNames1[j] =
     * barChartColumnNames[j]; barChartColumnNames2[j] = barChartColumnNames[j];
     * }
     *
     * for (int j = 0; j < temp1.length; j++) {
     * barChartColumnTitles1[viewByColumns.length + j] =
     * barChartColumnTitles[Integer.parseInt(temp1[j])];
     * barChartColumnNames1[viewByColumns.length + j] =
     * barChartColumnNames[Integer.parseInt(temp1[j])]; }
     *
     *
     * for (int j = 0; j < temp2.length; j++) {
     * barChartColumnTitles2[viewByColumns.length + j] =
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
     * graph1.setPieChartColumns(pieChartColumns);
     *
     * graph2.setBarChartColumnNames(barChartColumnNames2);
     * graph2.setViewByColumns(viewByColumns);
     * graph2.setBarChartColumnTitles(barChartColumnTitles2);
     * graph2.setPieChartColumns(pieChartColumns);
     *
     * if (graphTypeName.equalsIgnoreCase("Dual Axis")) { path = path + ";" +
     * pcharts[i].GetDualAxisChart(getCurrentDispRecords(), graph1, graph2, "",
     * "", getJscal(), getSession(), getResponse(), getOut()); graphTitle =
     * graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetDualAxisChart(getCurrentDispRecords(), graph1, graph2,
     * "", "", getJscal(), getSession(), getResponse(), getOut()); } else if
     * (graphTypeName.equalsIgnoreCase("Overlaid")) { path = path + ";" +
     * pcharts[i].GetSingleAxisChart(getCurrentDispRecords(), graph1, graph2,
     * "", getJscal(), getSession(), getResponse(), getOut()); graphTitle =
     * graphTitle + ";" + graphName; pathZoom = pathZoom + ";" +
     * pchartsZoom[i].GetSingleAxisChart(getCurrentDispRecords(), graph1,
     * graph2, "", getJscal(), getSession(), getResponse(), getOut()); }
     *
     * }
     * }
     * path = path.substring(1); graphTitle = graphTitle.substring(1);
     * grpDetails.add(path); grpDetails.add(graphTitle);
     * grpDetails.add(pcharts);
     *
     * grpDetails.add(pathZoom); grpDetails.add(pchartsZoom);
     *
     * return grpDetails; } catch (Exception exception) {
     * logger.error("Exception:",exception); try { connection.close(); } catch
     * (Exception exc) { logger.error("Exception:",exc); } return grpDetails; }
     * }
     *
     * public ArrayList get2dGraphHeaders(String[] barChartColumnNames, String[]
     * barChartColumnTitles, String[] viewByColumns) { //String[] grpDetails=new
     * String[2]; ArrayList grpDetails = new ArrayList(); try { pbretObj =
     * getPbReturnObject(getGrpHdrsQuery(getTableId())); tableColumns =
     * pbretObj.getColumnNames(); setGraphsCnt(pbretObj.getRowCount()); pcharts
     * = new ProgenChartDisplay[getGraphsCnt()]; pchartsZoom = new
     * ProgenChartDisplay[getGraphsCnt()]; for (int i = 0; i < getGraphsCnt();
     * i++) { graphId = pbretObj.getFieldValue(i, "GRAPH_ID").toString();
     * graphName = pbretObj.getFieldValue(i, "GRAPH_NAME").toString();
     * graphWidth = pbretObj.getFieldValue(i, "GRAPH_WIDTH").toString();
     * graphHeight = pbretObj.getFieldValue(i, "GRAPH_HEIGHT").toString();
     * graphClassName = pbretObj.getFieldValue(i,
     * "GRAPH_CLASS_NAME").toString(); graphTypeName = pbretObj.getFieldValue(i,
     * "GRAPH_TYPE_NAME").toString();
     *
     * this.barChartColumnTitles = barChartColumnTitles;
     * this.barChartColumnNames = barChartColumnNames; this.viewByColumns =
     * viewByColumns;
     *
     * setGraphId(graphId);
     *
     * graph = new ProgenChartDatasets();
     * graph.setBarChartColumnNames(this.barChartColumnNames);
     * graph.setViewByColumns(this.viewByColumns);
     * graph.setBarChartColumnTitles(this.barChartColumnTitles);
     * graph.setPieChartColumns(this.barChartColumnNames); pcharts[i] = new
     * ProgenChartDisplay(Integer.parseInt(graphWidth),
     * Integer.parseInt(graphHeight), ctxPath); pchartsZoom[i] = new
     * ProgenChartDisplay(700, 350, ctxPath);
     *
     *
     * if (i != (getGraphsCnt() - 1)) { path = path +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()) +
     * ";"; graphTitle = graphTitle + graphName + ";"; pathZoom = pathZoom +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()) +
     * ";"; } else { path = path +
     * pcharts[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut());
     * graphTitle = graphTitle + graphName; pathZoom = pathZoom +
     * pchartsZoom[i].GetCategoryAxisChart(getCurrentDispRecords(), graph,
     * graphTypeName, "", getJscal(), getSession(), getResponse(), getOut()); }
     * } grpDetails.add(path); grpDetails.add(graphTitle);
     * grpDetails.add(pcharts);
     *
     * grpDetails.add(pathZoom); grpDetails.add(pchartsZoom);
     *
     * return grpDetails; } catch (Exception exception) {
     * logger.error("Exception:",exception); try { connection.close(); } catch
     * (Exception exc) { logger.error("Exception:",exc); } return grpDetails; }
     * }
     *
     * public String getCtxPath() { return ctxPath; }
     *
     * public void setCtxPath(String ctxPath) { this.ctxPath = ctxPath; }
     *
     * public int getGraphsCnt() { return graphsCnt; }
     *
     * public void setGraphsCnt(int graphsCnt) { this.graphsCnt = graphsCnt; }
     *
     * //added by santhosh.kumar@progenbusiness.com on 30/07/09 for getting
     * number of graphs for a table public HashMap getSwapGraphAnalysis() {
     * return swapGraphAnalysis; }
     *
     * public void setSwapGraphAnalysis(HashMap swapGraphAnalysis) {
     * this.swapGraphAnalysis = swapGraphAnalysis; }
     */
}
