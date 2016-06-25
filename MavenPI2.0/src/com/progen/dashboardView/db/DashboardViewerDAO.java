/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.dashboardView.db;

import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.progen.charts.GraphProperty;
import com.progen.charts.ProgenJQPlotGraph;
import com.progen.oneView.bd.OneViewBD;
import com.progen.report.DashletDetail;
import com.progen.report.KPIElement;
import com.progen.report.charts.PbGraphDisplay;
import com.progen.report.entities.*;
import com.progen.report.kpi.DashletPropertiesHelper;
import com.progen.report.query.PbReportQuery;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import prg.db.OnceViewContainer;
import prg.db.OneViewLetDetails;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class DashboardViewerDAO extends PbDb {

    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(DashboardViewerDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new DashBoardViewerResBunSqlserver();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new DashBoardViewerResBunSqlserver();
            } else {
                resourceBundle = new DashBoardViewerResourcBundle();
            }
        }

        return resourceBundle;
    }

    public List<String> getScoreCardIds(String masterId) {
        List<String> scardIds = new ArrayList<String>();
        try {
            String query = getResourceBundle().getString("getScoreCardIds");
            Object[] objArray = new Object[1];
            objArray[0] = masterId;
            String finalQuery = buildQuery(query, objArray);
            PbReturnObject retObj = execSelectSQL(finalQuery);

            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    String scardId = retObj.getFieldValueString(i, 1);
                    scardIds.add(scardId);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return scardIds;
    }

    public Report getMapDetails(String reportId) {
        MapDetail report = new MapDetail();
        String mapSQL = "select main_measure, supp_measures from prg_ar_report_map_details where report_id=" + reportId;
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(mapSQL);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null && retObj.getRowCount() > 0) {
            StringBuilder elementIds = new StringBuilder();
            String mainMeasure = retObj.getFieldValueString(0, 0);
            String[] mainMeasArray = mainMeasure.split(",");
            List<String> mainMeasList = new ArrayList();
            for (String mainMeasId : mainMeasArray) {
                mainMeasList.add(mainMeasId);
            }
            elementIds.append(mainMeasure);
            String supportingMeasures = retObj.getFieldValueString(0, 1);

            report.setPrimaryMeasure(mainMeasList);
            if (supportingMeasures != null && !("".equals(supportingMeasures))) {
                String[] measArr = supportingMeasures.split(",");
                List<String> suppMeasList = new ArrayList<String>();
                for (String meas : measArr) {
                    suppMeasList.add(meas);
                    elementIds.append(",").append(meas);
                }
                report.setSupportingMeasures(suppMeasList);
            }

            List<QueryDetail> queryDetails = getQueryDetailList(reportId, elementIds.toString());
            report.setQueryDetails(queryDetails);
        }

        return report;
    }

    public Report getKPIGraphDetails(String GraphId) {
        HashMap<String, String> kpiElementNamesAndIDs = new HashMap<String, String>();
        KPIGraph kpiGraph = new KPIGraph();
        String query = getResourceBundle().getString("getkpiGraphDetails");
        Object[] objArr = new Object[1];
        objArr[0] = GraphId;
        String finalQuery = buildQuery(query, objArr);

        PbReturnObject kpigrpRetObj = null;
        try {
            kpigrpRetObj = execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (kpigrpRetObj != null && kpigrpRetObj.getRowCount() > 0) {
            int rowCount = kpigrpRetObj.getRowCount();
            String elementId = kpigrpRetObj.getFieldValueString(0, 1);
            kpiGraph.setNeedle(Double.valueOf(kpigrpRetObj.getFieldValueString(0, 3).trim()).doubleValue());
            kpiGraph.setKpiGraphType(kpigrpRetObj.getFieldValueString(0, 5));
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                kpiGraph.setGraphXML(kpigrpRetObj.getFieldUnknown(0, 6));
            } else {
                kpiGraph.setGraphXML(kpigrpRetObj.getFieldValueClobString(0, "GRAPH_XML"));
            }
            String width = kpigrpRetObj.getFieldValueString(0, 7);
            if (kpigrpRetObj.getFieldValueString(0, 7) != null) {
                kpiGraph.setGraphWidth(Double.parseDouble(kpigrpRetObj.getFieldValueString(0, 7)));
            }
            if (kpigrpRetObj.getFieldValueString(0, 8) != null) {
                kpiGraph.setGraphHeight(Double.parseDouble(kpigrpRetObj.getFieldValueString(0, 8)));
            }
            if (kpigrpRetObj.getFieldValueString(0, 9) != null) {
                kpiGraph.setKpigrname(kpigrpRetObj.getFieldValueString(0, 9));
            }

            List<String> elementIds = new ArrayList<String>();
            elementIds.add(elementId);
            kpiElementNamesAndIDs.put(elementId, kpigrpRetObj.getFieldValueString(0, 4));
            List<KPIElement> kpiElems = getKPIElements(elementIds, kpiElementNamesAndIDs);
            if (kpiElems != null && !kpiElems.isEmpty()) {
                kpiGraph.setKpiElement(kpiElems.get(0));
            }
        }


        PbReturnObject RetObj = null;
        String kpiRangeQuery = "select RISK,OPERATORS,START_VAL,END_VAL from PRG_AR_KPI_GRAPH_RANGE_DETAILS WHERE KPI_GRAPH_ID = " + GraphId;
        try {
            RetObj = execSelectSQL(kpiRangeQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (RetObj != null && RetObj.getRowCount() > 0) {
            for (int i = 0; i < RetObj.getRowCount(); i++) {
                KPIColorRange kpiColorRange = new KPIColorRange();
                kpiColorRange.setColor(RetObj.getFieldValueString(i, "RISK"));
                kpiColorRange.setOperator(RetObj.getFieldValueString(i, "OPERATORS"));
                kpiColorRange.setRangeStartValue(Double.valueOf(RetObj.getFieldValueString(i, "START_VAL").trim()).doubleValue());
                kpiColorRange.setRangeEndValue(Double.valueOf(RetObj.getFieldValueString(i, "END_VAL").trim()).doubleValue());
                kpiGraph.addkpiGrphColorRange(RetObj.getFieldValueString(i, "RISK"), kpiColorRange);

            }
            kpiGraph.initializeRanges();
        }

        return kpiGraph;

    }

    public Report getKPIDetails(String kpiMasterId, String userId) {
        KPI kpiDetails = new KPI();
        HashMap<String, String> kpiElementNamesAndIDs = new HashMap<String, String>();
        String kpiQuery = "";
        kpiQuery = "select element_id, kpi_hrange1, kpi_hrange2, kpi_hrange_type, "
                + "kpi_lrange1, kpi_lrange2, kpi_lrange_type, "
                + "kpi_mrange1, kpi_mrange2, kpi_mrange_type, ref_report_id,INSIGHTVIEWSTATUS,COMMENTVIEWSTATUS ,GRAPHVIEWSTATUS,KPI_NAME,KPI_ST_TYPE,KPI_SEQ,MTDVIEWSTATUS,QTDVIEWSTATUS,YTDVIEWSTATUS,CURRENTVIEWSTATUS,DRILL_VIEW_BYS,TARGET_ELEMENT,DRILL_TYPE"
                + " from PRG_AR_KPI_DETAILS  where kpi_master_id = " + kpiMasterId
                + " order by kpi_details_id";
        HashMap<String, String> kpiSttypesMap = new HashMap<String, String>();
        PbReturnObject kpiRetObj = null;
        try {
            kpiRetObj = execSelectSQL(kpiQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        if (kpiRetObj != null && kpiRetObj.getRowCount() > 0) {
            List<String> elementList = new ArrayList<String>();
            List<String> targetElementList = new ArrayList<String>();
            int rowCount = kpiRetObj.getRowCount();
            StringBuilder elementIds = new StringBuilder();
            boolean showInsight = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "INSIGHTVIEWSTATUS"));
            boolean showComment = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "COMMENTVIEWSTATUS"));
            boolean showGraph = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "GRAPHVIEWSTATUS"));
            boolean mtdstatus = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "MTDVIEWSTATUS"));
            boolean qtdstatus = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "QTDVIEWSTATUS"));
            boolean ytdstatus = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "YTDVIEWSTATUS"));
            boolean currstatus = Boolean.parseBoolean(kpiRetObj.getFieldValueString(0, "CURRENTVIEWSTATUS"));
            String drillViewBys = kpiRetObj.getFieldValueString(0, "DRILL_VIEW_BYS");
            // 
            kpiDetails.setInsightAndCommentViewStaus(showInsight, showComment, showGraph);
            kpiDetails.setMtdQtdYtdViewStatus(mtdstatus, qtdstatus, ytdstatus, currstatus);
            kpiDetails.setDrillViewBys(drillViewBys);

            //  kpiDetails.setKpiStType(kpiRetObj.getFieldValueString(0,"KPI_ST_TYPE"));
            for (int i = 0; i < rowCount; i++) {
                String elementId = "";
//                 
                if (kpiRetObj.getFieldValueString(i, 0) != null && kpiRetObj.getFieldValueString(i, 0).trim() != "") {
                    elementId = kpiRetObj.getFieldValueString(i, 0);
                    kpiDetails.getKPISequenceHashMap().put(Integer.parseInt(kpiRetObj.getFieldValueString(i, "KPI_SEQ")), kpiRetObj.getFieldValueString(i, 0));
                } else {
                    elementId = kpiRetObj.getFieldValueString(i, "KPI_NAME");
                    kpiDetails.getKPISequenceHashMap().put(Integer.parseInt(kpiRetObj.getFieldValueString(i, "KPI_SEQ")), kpiRetObj.getFieldValueString(i, "KPI_NAME"));
                }
                String drillReportId = "";
                if (kpiRetObj.getFieldValueString(i, 0) != null && !kpiRetObj.getFieldValueString(i, 0).equalsIgnoreCase("")) {
                    drillReportId = kpiRetObj.getFieldValueString(i, 10);
                }
                kpiDetails.addKPIDrill(elementId, drillReportId);
                elementIds.append(",").append(elementId);
                if (kpiRetObj.getFieldValueString(i, "TARGET_ELEMENT") != null && !kpiRetObj.getFieldValueString(i, "TARGET_ELEMENT").equalsIgnoreCase("")) {
                    String targetElement = kpiRetObj.getFieldValueString(i, "TARGET_ELEMENT");
                    kpiDetails.addKpiTargetelementMap(elementId, targetElement);
                    targetElementList.add(targetElement);
                }
                String drillRepType = "";
                if (kpiRetObj.getFieldValueString(i, "DRILL_TYPE") != null && !kpiRetObj.getFieldValueString(i, "DRILL_TYPE").equalsIgnoreCase("")) {
                    drillRepType = kpiRetObj.getFieldValueString(i, "DRILL_TYPE");
                }
                kpiDetails.addKPIDrillRepType(elementId, drillRepType);
                if (kpiRetObj.getFieldValueString(i, 0) != null && kpiRetObj.getFieldValueString(i, 0).trim() != "") {
                    elementList.add(elementId);
                }
                kpiSttypesMap.put(elementId, kpiRetObj.getFieldValueString(i, "KPI_ST_TYPE"));
                kpiElementNamesAndIDs.put(elementId, kpiRetObj.getFieldValueString(i, "KPI_NAME"));
                String hRangeOperator = "";
                String lRangeOperator = "";
                String mRangeOperator = "";
                if (kpiRetObj.getFieldValueString(i, 0) != null && !kpiRetObj.getFieldValueString(i, 0).equalsIgnoreCase("")) {
                    hRangeOperator = kpiRetObj.getFieldValueString(i, 3);
                    lRangeOperator = kpiRetObj.getFieldValueString(i, 6);
                    mRangeOperator = kpiRetObj.getFieldValueString(i, 9);
                }
                if (!("".equals(hRangeOperator))) {
                    KPIColorRange colorRange = new KPIColorRange();
                    double rangeStartValue = 0.0;
                    double rangeEndValue = 0.0;
                    if (kpiRetObj.getFieldValueString(i, 1) != null && !kpiRetObj.getFieldValueString(i, 1).equalsIgnoreCase("")) {
                        rangeStartValue = kpiRetObj.getFieldValueBigDecimal(i, 1).doubleValue();
                    }
                    if (kpiRetObj.getFieldValueString(i, 2) != null && !kpiRetObj.getFieldValueString(i, 2).equalsIgnoreCase("")) {
                        rangeEndValue = kpiRetObj.getFieldValueBigDecimal(i, 2).doubleValue();
                    }

                    colorRange.setColor("Green");
                    colorRange.setOperator(hRangeOperator);
                    colorRange.setRangeStartValue(rangeStartValue);
                    colorRange.setRangeEndValue(rangeEndValue);
                    kpiDetails.addKPIColorRange(elementId, colorRange);
                }

                if (!("".equals(lRangeOperator))) {
                    KPIColorRange colorRange = new KPIColorRange();
                    double rangeStartValue = 0.0;
                    double rangeEndValue = 0.0;
                    if (kpiRetObj.getFieldValueString(i, 4) != null && !kpiRetObj.getFieldValueString(i, 4).equalsIgnoreCase("")) {
                        rangeStartValue = kpiRetObj.getFieldValueBigDecimal(i, 4).doubleValue();
                    }
                    if (kpiRetObj.getFieldValueString(i, 5) != null && !kpiRetObj.getFieldValueString(i, 5).equalsIgnoreCase("")) {
                        rangeEndValue = kpiRetObj.getFieldValueBigDecimal(i, 5).doubleValue();
                    }
                    colorRange.setColor("Red");
                    colorRange.setOperator(lRangeOperator);
                    colorRange.setRangeStartValue(rangeStartValue);
                    colorRange.setRangeEndValue(rangeEndValue);
                    kpiDetails.addKPIColorRange(elementId, colorRange);
                }

                if (!("".equals(mRangeOperator))) {
                    KPIColorRange colorRange = new KPIColorRange();
                    double rangeStartValue = 0.0;
                    double rangeEndValue = 0.0;
                    if (kpiRetObj.getFieldValueString(i, 7) != null && !kpiRetObj.getFieldValueString(i, 7).equalsIgnoreCase("")) {
                        rangeStartValue = kpiRetObj.getFieldValueBigDecimal(i, 7).doubleValue();
                    }
                    if (kpiRetObj.getFieldValueString(i, 8) != null && !kpiRetObj.getFieldValueString(i, 8).equalsIgnoreCase("")) {
                        rangeEndValue = kpiRetObj.getFieldValueBigDecimal(i, 8).doubleValue();
                    }
                    colorRange.setColor("Yellow");
                    colorRange.setOperator(mRangeOperator);
                    colorRange.setRangeStartValue(rangeStartValue);
                    colorRange.setRangeEndValue(rangeEndValue);
                    kpiDetails.addKPIColorRange(elementId, colorRange);
                }

            }
            kpiDetails.setKpiStTypeHashMap(kpiSttypesMap);
            kpiDetails.setElementIds(elementList);
            kpiDetails.setTargetElementIds(targetElementList);


            List<KPIElement> kpiElements = getKPIElements(elementList, kpiElementNamesAndIDs);
            List<KPIElement> targetKpiElements = getTragetKPIElements(targetElementList);

//            kpiElements.addAll(targetKpiElements);
            for (KPIElement kpiElem : kpiElements) {
                kpiDetails.addKPIElement(kpiElem.getRefElementId(), kpiElem);
            }

            for (KPIElement kpiElem : targetKpiElements) {
                kpiDetails.addTargetKPIElement(kpiElem.getRefElementId(), kpiElem);
            }

            String commentsQuery = "select element_id, user_id, kpi_comment, comment_date from prg_kpi_user_comments "
                    + " where kpi_master_id=" + kpiMasterId + " and user_id=" + userId + " and element_id in (" + Joiner.on(",").join(elementList) + ") "
                    + " order by comment_date desc";

            PbReturnObject commentsRetObj = null;
            try {
                commentsRetObj = execSelectSQL(commentsQuery);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if (commentsRetObj != null && commentsRetObj.getRowCount() > 0) {
                rowCount = commentsRetObj.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    KPIComment kpiComment = new KPIComment();
                    String elementId = commentsRetObj.getFieldValueString(i, 0);
                    kpiComment.setElementId(elementId);
                    kpiComment.setUserId(userId);
                    kpiComment.setComment(commentsRetObj.getFieldValueString(i, 2));
                    kpiComment.setCommentDate(commentsRetObj.getFieldValueDate(i, 3));
                    kpiDetails.addKPIComments(elementId, kpiComment);
                }
            }

            String targetsQuery = "select element_id, time_level, target_value from dashboard_target_kpi_value where kpi_master_id=" + kpiMasterId;

            PbReturnObject targetRetObj = null;
            try {
                targetRetObj = execSelectSQL(targetsQuery);
            } catch (SQLException e) {
                logger.error("Exception:", e);
            }

            if (targetRetObj != null && targetRetObj.getRowCount() > 0) {
                rowCount = targetRetObj.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    KPITarget kpiTarget = new KPITarget();
                    String elementId = targetRetObj.getFieldValueString(i, 0);
                    String timeLevel = targetRetObj.getFieldValueString(i, 1);
                    String targetValue = targetRetObj.getFieldValueString(i, 2);

                    kpiTarget.setElementId(elementId);
                    kpiTarget.setTimeLevel(timeLevel);
                    if (targetValue != null && !("".equalsIgnoreCase(targetValue))) {
                        kpiTarget.setTargetValue(Double.parseDouble(targetValue));
                    }

                    kpiDetails.addKPITarget(elementId, kpiTarget);
                }
            }

            List<TargetData> targetList = getTargetData(elementList);
            if (targetList != null) {
                for (TargetData tgtData : targetList) {
                    kpiDetails.addTargetData(tgtData);
                }
            }
        }
        kpiDetails.setPersisted(true);
        return kpiDetails;
    }
    //sandeep to getting graph data

    public List<Map<String, String>> generategraphJson(PbReturnObject pbretObj, List<String> paramNames, List<String> paramIds, int size) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//        Map<String, String> map = new LinkedHashMap<String, String>();
        Map<String, String> map1 = new LinkedHashMap<String, String>();
        for (int m = 0; m < pbretObj.rowCount; m++) {
            Map<String, String> map = new LinkedHashMap<String, String>();
            for (int k = 0; k < paramNames.size(); k++) {
                if (paramNames.get(k).trim().equalsIgnoreCase("TIME")) {
                    map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, paramIds.get(k).trim()));
                } else {
                    if (k >= size && (pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("e") || pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)).contains("E"))) {
                        map.put(paramNames.get(k), String.valueOf(Double.valueOf(pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k))).longValue()));
                    } else {
                        map.put(paramNames.get(k), pbretObj.getFieldValueStringBasedOnViewSeq(m, "A_" + paramIds.get(k)));
                    }
                }

            }
//            for (int k = 0; k < measures.size(); k++) {
//                map.put(measures.get(k), pbretObj.getFieldValueString(m, k));
//            }
            list.add(map);
        }
//        JsonComperator jsonComperator = new JsonComperator(reportMeta.getMeasures().get(0), "desc");
//        Collections.sort(list, jsonComperator);
        return list;
    }

    public List<KPIElement> getKPIElements(List<String> elementIds, HashMap<String, String> piElementNamesAndIDs) {
        List<KPIElement> kpiElements = new ArrayList<KPIElement>();
        List<String> ElementIDlist = new ArrayList<String>();
        Set<String> keysSet = piElementNamesAndIDs.keySet();
        if (elementIds == null || elementIds.isEmpty()) {
        } else {
            StringBuilder sb = new StringBuilder();
            for (String elemId : elementIds) {
                sb.append(",").append(elemId);
            }

            String query = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE, USER_COL_DESC, TARGET_MAP_ELEMENT "
                    + "from  PRG_USER_ALL_INFO_DETAILS "
                    + "where ELEMENT_ID in (" + sb.substring(1) + ") "
                    + "OR REF_ELEMENT_ID in (" + sb.substring(1) + ") "
                    + "order by ref_element_id, ref_element_type";

            PbReturnObject retObj = null;
            try {
                retObj = execSelectSQL(query);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }

            if (retObj != null && retObj.getRowCount() > 0) {
                int rowCount = retObj.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    for (String elemId : elementIds) {
                        if (retObj.getFieldValueString(i, 0).equalsIgnoreCase(elemId) && !retObj.getFieldValueString(i, 1).equalsIgnoreCase(elemId)) {
                            KPIElement kpiElem = new KPIElement();
                            kpiElem.setElementId(retObj.getFieldValueString(i, 0));
                            ElementIDlist.add(retObj.getFieldValueString(i, 0));
                            kpiElem.setRefElementId(retObj.getFieldValueString(i, 0));
                            kpiElem.setRefElementType(retObj.getFieldValueString(i, 2));
                            kpiElem.setAggregationType(retObj.getFieldValueString(i, 3));
                            kpiElem.setTargetElement(retObj.getFieldValueString(i, 5));
                            if (piElementNamesAndIDs.isEmpty()) {
                                kpiElem.setElementName(retObj.getFieldValueString(i, 4));
                            } else {
                                kpiElem.setElementName(piElementNamesAndIDs.get(retObj.getFieldValueString(i, 0)));
                            }
                            kpiElements.add(kpiElem);
                        } else {
                        }
                    }
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(retObj.getFieldValueString(i, 0));
                    ElementIDlist.add(retObj.getFieldValueString(i, 0));
                    kpiElem.setRefElementId(retObj.getFieldValueString(i, 1));
                    kpiElem.setRefElementType(retObj.getFieldValueString(i, 2));
                    kpiElem.setAggregationType(retObj.getFieldValueString(i, 3));
                    kpiElem.setTargetElement(retObj.getFieldValueString(i, 5));
                    if (piElementNamesAndIDs.isEmpty()) {
                        kpiElem.setElementName(retObj.getFieldValueString(i, 4));
                    } else {
                        kpiElem.setElementName(piElementNamesAndIDs.get(retObj.getFieldValueString(i, 0)));
                    }
                    kpiElements.add(kpiElem);
                }
            }
            for (String tempStr : keysSet) {
                if (!ElementIDlist.contains(tempStr)) {
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(tempStr);
                    kpiElem.setElementName(tempStr);
                    kpiElem.setRefElementId(tempStr);
                    kpiElem.setIsGroupElement(true);
                    kpiElements.add(kpiElem);
                }
            }
        }
        return kpiElements;
    }

    public List<TargetData> getTargetData(List<String> elementList) {
        //Populate the target values from PRG_AR_TARGET_DATA
        List<TargetData> targetList = new ArrayList<TargetData>();
        StringBuilder elementsStr = new StringBuilder();
        for (String element : elementList) {
            elementsStr.append(",").append(element);
        }
        if (!elementList.isEmpty()) {
            elementsStr.replace(0, 1, "");
        }

        String query = "select element_id, time_level, target_data from prg_ar_target_data where element_id in (" + elementsStr + ")";
        PbReturnObject targetsRetObj = null;
        try {
            targetsRetObj = execSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (targetsRetObj != null && targetsRetObj.getRowCount() > 0) {
            int rowCount = targetsRetObj.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                String elementId = targetsRetObj.getFieldValueString(i, 0);
                String timeLevel = targetsRetObj.getFieldValueString(i, 1);
                String targetData = targetsRetObj.getFieldValueClobString(i, "TARGET_DATA");

                Map<String, BigDecimal> targetMap = getTargetDataMap(targetData);

                TargetData tgtData = new TargetData();
                tgtData.setMeasureId(elementId);
                tgtData.setTimeLevel(timeLevel);
                tgtData.setTargetValues(targetMap);

                targetList.add(tgtData);
            }
        }
        return targetList;
    }

    public Report getGraphDetails(String reportId, String graphId) {
        GraphReport graphDetails = new GraphReport();

        String query = getResourceBundle().getString("getGraphMaster");
        Object[] objArr = new Object[1];
        objArr[0] = graphId;

        String finalQuery = buildQuery(query, objArr);

        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        if (retObj != null && retObj.getRowCount() > 0) {
            String[] dbCols = retObj.getColumnNames();
            graphDetails.setGraphName(retObj.getFieldValueString(0, 1));
            graphDetails.setGraphWidth(retObj.getFieldValueString(0, 2));
            graphDetails.setGraphHeight(retObj.getFieldValueString(0, 3));
            graphDetails.setGraphClass(retObj.getFieldValueInt(0, 4));
            graphDetails.setGraphClassName(retObj.getFieldValueString(0, 5));
            graphDetails.setGraphType(retObj.getFieldValueInt(0, 6));
            graphDetails.setGraphTypeName(retObj.getFieldValueString(0, 7));
            graphDetails.setGraphSize(retObj.getFieldValueInt(0, 8));
            graphDetails.setGraphSizeName(retObj.getFieldValueString(0, 9));
            graphDetails.setLegendLocation(retObj.getFieldValueString(0, 10));

            String allowLegend = retObj.getFieldValueString(0, 11);
            if ("Y".equalsIgnoreCase(allowLegend)) {
                graphDetails.setLegendAllowed(true);
            }

            String xAxisGrid = retObj.getFieldValueString(0, 12);
            if ("Y".equalsIgnoreCase(xAxisGrid)) {
                graphDetails.setShowXAxisGrid(true);
            }

            String yAxisGrid = retObj.getFieldValueString(0, 13);
            if ("Y".equalsIgnoreCase(yAxisGrid)) {
                graphDetails.setShowYAxisGrid(true);
            }

            String allowLink = retObj.getFieldValueString(0, 14);
            if ("Y".equalsIgnoreCase(allowLink)) {
                graphDetails.setLinkAllowed(true);
            }

            graphDetails.setBackgroundColor(retObj.getFieldValueString(0, 15));
            graphDetails.setFontColor(retObj.getFieldValueString(0, 16));

            String showData = retObj.getFieldValueString(0, 17);
            if ("Y".equalsIgnoreCase(showData)) {
                graphDetails.setShowData(true);
            }

            graphDetails.setLeftYAxisLabel(retObj.getFieldValueString(0, 18));
            graphDetails.setRightYAxisLabel(retObj.getFieldValueString(0, 19));
            graphDetails.setRowValues(retObj.getFieldValueString(0, 20));

            String showGT = retObj.getFieldValueString(0, 21);
            if ("Y".equalsIgnoreCase(showGT)) {
                graphDetails.setShowGT(true);
            }

            graphDetails.setDisplayRows(retObj.getFieldValueString(0, 22));

            String showAsTable = retObj.getFieldValueString(0, 23);
            if ("Y".equalsIgnoreCase(showAsTable)) {
                graphDetails.setShowAsTable(true);
            }
            String graphPropertyXml = null;
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                graphPropertyXml = retObj.getFieldUnknown(0, 24);
            } else {
                graphPropertyXml = retObj.getFieldValueClobString(0, dbCols[24]);
            }
            PbGraphDisplay gd = new PbGraphDisplay();
            GraphProperty prop = gd.parseGraphPropertyXml(graphPropertyXml);
            graphDetails.setGraphProperty(prop);

            String timeSeries = retObj.getFieldValueString(0, 25);
            if (timeSeries != null && !"".equals(timeSeries)) {
                graphDetails.setTimeSeries(Boolean.parseBoolean(timeSeries));
            }
        }

        query = getResourceBundle().getString("getGraphDetails");
        objArr = new Object[1];
        objArr[0] = graphId;

        finalQuery = buildQuery(query, objArr);

        retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        StringBuilder elementIds = new StringBuilder();
        if (retObj != null && retObj.getRowCount() > 0) {
            String axis = retObj.getFieldValueString(0, 2);
            graphDetails.setAxis(axis);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                String elemId = retObj.getFieldValueString(i, 0);
                elementIds.append(",").append(elemId);
            }

            elementIds.replace(0, 1, "");

            List<QueryDetail> queryDetails = getQueryDetailList(reportId, elementIds.toString());
            graphDetails.setQueryDetails(queryDetails);

        }

        return graphDetails;
    }

    private List<QueryDetail> getQueryDetailList(String reportId, String elementIds) {

        String query = getResourceBundle().getString("getQueryDetails");
        Object[] objArr = new Object[2];
        objArr[0] = elementIds;
        objArr[1] = reportId;
        String finalQuery = buildQuery(query, objArr);
        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        String[] elementArr = elementIds.split(",");
        List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();

        for (String elem : elementArr) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                String tempElemId = retObj.getFieldValueString(i, 0);
                if (elem.equalsIgnoreCase(tempElemId)) {
                    QueryDetail qd = new QueryDetail();
                    qd.setElementId(retObj.getFieldValueString(i, 0));
                    qd.setDisplayName(retObj.getFieldValueString(i, 1));
                    qd.setRefElementId(retObj.getFieldValueString(i, 2));
                    qd.setFolderId(retObj.getFieldValueInt(i, 3));
                    qd.setSubFolderId(retObj.getFieldValueInt(i, 4));
                    qd.setAggregationType(retObj.getFieldValueString(i, 5));
                    qd.setColumnType(retObj.getFieldValueString(i, 6));

                    queryDetails.add(qd);
                }
            }
        }
        return queryDetails;
    }

    private Map<String, BigDecimal> getTargetDataMap(String targetData) {
        Map<String, BigDecimal> targetDataMap = new HashMap<String, BigDecimal>();

        Document cellDocument;
        Element root = null;
        SAXBuilder builder = new SAXBuilder();
        try {
            cellDocument = builder.build(new ByteArrayInputStream(targetData.toString().getBytes()));
            root = cellDocument.getRootElement();

            if (root != null) {
                List dataList = root.getChildren("data");
                String data;

                if (dataList != null && dataList.size() > 0) {
                    for (int i = 0; i < dataList.size(); i++) {
                        Element dataElem = (Element) dataList.get(i);
                        Element periodElem = dataElem.getChild("period");
                        Element valueElem = dataElem.getChild("value");

                        String period = "";
                        String value = "";

                        if (periodElem != null) {
                            period = periodElem.getText();
                        }
                        if (valueElem != null) {
                            value = valueElem.getText();
                        }

                        targetDataMap.put(period, new BigDecimal(value));
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return targetDataMap;
    }

    public void saveDashletProperties(String dashletId, String sortType, String countVal, String sortOnColumn, String checkval) {
        String saveDashletProperties = getResourceBundle().getString("saveDashletProperties");
        DashletPropertiesHelper dashletPropertiesHelper = new DashletPropertiesHelper();
        dashletPropertiesHelper.setCountForDisplay(Integer.parseInt(countVal));
        dashletPropertiesHelper.setDisplayOrder(sortType);
        dashletPropertiesHelper.setSortOnMeasure(sortOnColumn);
        dashletPropertiesHelper.setSortAll(Boolean.parseBoolean(checkval));
        Gson gson = new Gson();
        String propertiesString = gson.toJson(dashletPropertiesHelper);
        Object object[] = new Object[2];
        object[0] = propertiesString;
        object[1] = dashletId;
        try {
            super.execModifySQL(super.buildQuery(saveDashletProperties, object));
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public DashletPropertiesHelper getDashletPropertiesHelperObject(String dashletId) {
        DashletPropertiesHelper propertiesHelper = null;
        Object[] objArr = new Object[1];
        objArr[0] = dashletId;
        try {
            String getDashletPropertiesHelperObject = getResourceBundle().getString("getDashletPropertiesHelperObject");
            PbReturnObject pbReturnObject = super.execSelectSQL(super.buildQuery(getDashletPropertiesHelperObject, objArr));
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                if (pbReturnObject.getRowCount() > 0 && pbReturnObject.getFieldUnknown(0, 0) != null) {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    propertiesHelper = gson.fromJson(pbReturnObject.getFieldUnknown(0, 0), DashletPropertiesHelper.class);
                }
            } else {
                if (pbReturnObject.getRowCount() > 0 && pbReturnObject.getFieldValueClobString(0, "DASHLET_PROPERTIES") != null) {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    propertiesHelper = gson.fromJson(pbReturnObject.getFieldValueClobString(0, "DASHLET_PROPERTIES"), DashletPropertiesHelper.class);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return propertiesHelper;
    }

    public String InsertDashletProp(String dashletId, String DashProp) {
        String dashproperties = "";
        String query = getResourceBundle().getString("insertDahletProperties");
        Object[] objArr = new Object[1];
        objArr[0] = DashProp;
        objArr[1] = dashletId;

        String finalQuery = buildQuery(query, objArr);

        return dashproperties;
    }

    public PbReturnObject getsecurityvalue(ArrayList QueryCols, ArrayList QueryAggs, String userId, String busroleId, HttpServletRequest request, OnceViewContainer onecontainer, OneViewLetDetails oneviewlet) {


        String SecCluase = "select MEMBER_VALUE,element_Id from PRG_USER_ROLE_MEMBER_FILTER " + " where user_id = " + userId;
        PbReturnObject retObj = null;
        PbReturnObject retObj1 = null;
        PbDb pbdb1 = new PbDb();
        try {
            retObj = pbdb1.execSelectSQL(SecCluase);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        String element_Id = "";
        String MEMBER_VALUE = "";
        String secucluaseqry = "";
        String grpBy = "";
        int psize = retObj.getRowCount();
        if (psize > 0) {
            for (int looper = 0; looper < psize; looper++) {

                if (looper == 0) {
                    String vql = "(" + retObj.getFieldValueClobString(looper, "MEMBER_VALUE") + ")";
                    secucluaseqry += "A_" + retObj.getFieldValueString(looper, "element_Id") + " in " + "(" + retObj.getFieldValueClobString(looper, "MEMBER_VALUE") + ")";
                } else {
                    secucluaseqry += " and " + retObj.getFieldValueString(looper, "element_Id") + " in " + retObj.getFieldValueClobString(looper, "MEMBER_VALUE");

                }
                grpBy += grpBy + "," + retObj.getFieldValueString(looper, "element_Id").toString();
//      }

            }
        }
        Connection conn = null;


        String filterqry = "select SUM(A_" + QueryCols.get(0) + ") A_" + QueryCols.get(0) + ",SUM(A_" + QueryCols.get(1) + ") A_" + QueryCols.get(1) + ",SUM(A_" + QueryCols.get(2) + ") A_" + QueryCols.get(2) + ",SUM(A_" + QueryCols.get(3) + ") A_" + QueryCols.get(3) + " from R_GO_" + oneviewlet.getRepId() + " where " + secucluaseqry + " group by A_" + grpBy.substring(1);
        try {

            conn = (Connection) ProgenConnection.getInstance().getConnectionForElement(QueryCols.get(0).toString().replace("A_", ""));
            retObj1 = pbdb1.execSelectSQL(filterqry, conn);
            String value = retObj1.getFieldValueString(0, 0);
            value = retObj1.getFieldValueString(0, 1);
            value = retObj1.getFieldValueString(0, 2);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        return retObj1;

    }
    //Surender

    public PbReturnObject getReturnObjectForOneView(ArrayList QueryCols, ArrayList QueryAggs, String userId, String busroleId, HttpServletRequest request, OnceViewContainer onecontainer, OneViewLetDetails oneviewlet) {
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        String issecurity = "";
        String action = "";
        ArrayList viewbys = new ArrayList();
        if (oneviewlet.getisschedule()) {
        } else {
            HttpSession session = request.getSession(false);
            issecurity = (String) request.getAttribute("issecurity");
            viewbys = (ArrayList) request.getAttribute("viewbys");
            action = (String) request.getAttribute("action");
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        PbReturnObject pbretObjForTime = new PbReturnObject();
        List<String> timedetails = new ArrayList<String>();
        if (oneviewlet.isOneviewReportTimeDetails()) {
            timedetails = oneviewlet.getMsrCustomTimeDetails();
        } else {
            timedetails = (List<String>) onecontainer.timedetails;
        }
        //  timedetails = (List<String>) onecontainer.timedetails;
//            ArrayList arl=new ArrayList();
//            arl.add("Day");
//            arl.add("PRG_STD");
//            ProgenParam pramnam=new ProgenParam();
//            String date=pramnam.getdateforpage();
//            arl.add(date);
////            arl.add("10/01/2011");
//            arl.add("Month");
//            arl.add("Last Period");

        PbReportQuery repQuery = new PbReportQuery();
        repQuery.setRowViewbyCols(new ArrayList());
        ArrayList rowviewby = new ArrayList();
        if (issecurity != null && issecurity.equalsIgnoreCase("true")) {
            rowviewby.add(viewbys);
            repQuery.isKpi = false;
//                 repQuery.ismeasuresecurity = true;
            repQuery.setRowViewbyCols(viewbys);
        } else {
            repQuery.isKpi = true;
            rowviewby.add(new ArrayList());

            repQuery.setRowViewbyCols(new ArrayList());
        }
//                repQuery.setParamValue(collect.reportParametersValues);
        repQuery.setColViewbyCols(new ArrayList());
        int size = QueryCols.indexOf(oneviewlet.getRepId());
//                System.out.print("**size***"+size+"**QueryCols**"+QueryCols.toString());
        if (oneviewlet.getMsrCustomAggregation() != null) {
            QueryAggs.set(size, oneviewlet.getMsrCustomAggregation());
        }
        repQuery.setQryColumns(QueryCols);
        repQuery.setColAggration(QueryAggs);
        if (onecontainer.getFilterBusinessRole() != null && !onecontainer.getFilterBusinessRole().equalsIgnoreCase("") && busroleId != null && !busroleId.equalsIgnoreCase("") && onecontainer.getFilterBusinessRole().equalsIgnoreCase(busroleId)) {
            if (onecontainer.getReportParameterValues() != null && !onecontainer.getReportParameterValues().isEmpty()) {
                repQuery.setParamValue(onecontainer.getReportParameterValues());
            }
            repQuery.setInMap(onecontainer.getReportParameterValues());
        }
//                if((List<String>) request.getAttribute("OneviewTiemDetails")!=null){
//                    repQuery.setTimeDetails((ArrayList) request.getAttribute("OneviewTiemDetails"));
//                }
//                else{
//                   repQuery.setTimeDetails(arl);
//                }
        repQuery.setTimeDetails((ArrayList) timedetails);
        repQuery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
//                
//                if(oneviewlet.getMsrCustomAggregation()!=null)
//                repQuery.setDefaultMeasureSumm(oneviewlet.getMsrCustomAggregation());
//                else
        repQuery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
//                repQuery.isKpi = true;
//                repQuery.setReportId(collect.reportId);
        repQuery.setBizRoles(busroleId);
        repQuery.setUserId(userId);
//if(action!=null && action.equalsIgnoreCase("save")){
// if(issecurity!=null && issecurity.equalsIgnoreCase("true")){
//      Connection conn = null;
//       conn = ProgenConnection.getInstance().getConnectionForElement(QueryCols.get(0).toString().replace("A_",""));
//         String   pbretObjForTime1 = repQuery.getPbReturnObjectmeasure(String.valueOf(QueryCols.get(0)));
//             String dropQuery = "drop table R_GO_"+oneviewlet.getRepId();
//               PbDb pbdb = new PbDb();
//      String tableName = "R_GO_"+oneviewlet.getRepId();
//       String objectQuery="";
//       if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
//         objectQuery = "select * into  "+tableName+" from ("+pbretObjForTime1+" )a";
//            }
//       else if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)){
//          objectQuery = "create table R_GO_"+oneviewlet.getRepId()+"   "+ pbretObjForTime1;
//       }
//       else{
//        objectQuery = "create table R_GO_"+oneviewlet.getRepId()+" as "+ pbretObjForTime1;
//       }
//        try {
//            pbdb.execUpdateSQL(dropQuery, conn);
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//        try {
//            conn = ProgenConnection.getInstance().getConnectionForElement(QueryCols.get(0).toString().replace("A_",""));
//            String objectQuery1=objectQuery.replaceAll("null[*]0", "0");
////            String objectQuery1=objectQuery.replaceAll("\"NULL*0\"", "0").replaceAll("\"null*0\"", "0");
//            pbdb.execUpdateSQL(objectQuery1, conn);
//
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//        }
//        finally {
//            try {
//                if(conn!=null)
//                conn.close();
//            } catch (SQLException ex) {
//                logger.error("Exception:",ex);
//            }
//        }
//
//        }
//    }
//    else
//
//    {
        pbretObjForTime = repQuery.getPbReturnObject(String.valueOf(QueryCols.get(0)));
//        }

        HashMap vals111 = new HashMap();
        vals111 = repQuery.getTimememdetails();




        onecontainer.timeHashMap = vals111;

//            timequery.isTimeSeries=false;
        return pbretObjForTime;
    }

    public Report getTextKpiDashletDetails(String reportId, String graphId, DashletDetail detail) {
        GraphReport graphDetails = new GraphReport();

        String query = getResourceBundle().getString("getGraphMaster");
        Object[] objArr = new Object[1];
        objArr[0] = graphId;

        String finalQuery = buildQuery(query, objArr);

        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        if (retObj != null && retObj.getRowCount() > 0) {
            String[] dbCols = retObj.getColumnNames();
            graphDetails.setGraphName(retObj.getFieldValueString(0, 1));
            graphDetails.setGraphWidth(retObj.getFieldValueString(0, 2));
            graphDetails.setGraphHeight(retObj.getFieldValueString(0, 3));
            graphDetails.setGraphClass(retObj.getFieldValueInt(0, 4));
            graphDetails.setGraphClassName(retObj.getFieldValueString(0, 5));
            graphDetails.setGraphType(retObj.getFieldValueInt(0, 6));
            graphDetails.setGraphTypeName(retObj.getFieldValueString(0, 7));
            graphDetails.setGraphSize(retObj.getFieldValueInt(0, 8));
            graphDetails.setGraphSizeName(retObj.getFieldValueString(0, 9));
            graphDetails.setLegendLocation(retObj.getFieldValueString(0, 10));

            String allowLegend = retObj.getFieldValueString(0, 11);
            if ("Y".equalsIgnoreCase(allowLegend)) {
                graphDetails.setLegendAllowed(true);
            }

            String xAxisGrid = retObj.getFieldValueString(0, 12);
            if ("Y".equalsIgnoreCase(xAxisGrid)) {
                graphDetails.setShowXAxisGrid(true);
            }

            String yAxisGrid = retObj.getFieldValueString(0, 13);
            if ("Y".equalsIgnoreCase(yAxisGrid)) {
                graphDetails.setShowYAxisGrid(true);
            }

            String allowLink = retObj.getFieldValueString(0, 14);
            if ("Y".equalsIgnoreCase(allowLink)) {
                graphDetails.setLinkAllowed(true);
            }

            graphDetails.setBackgroundColor(retObj.getFieldValueString(0, 15));
            graphDetails.setFontColor(retObj.getFieldValueString(0, 16));

            String showData = retObj.getFieldValueString(0, 17);
            if ("Y".equalsIgnoreCase(showData)) {
                graphDetails.setShowData(true);
            }

            graphDetails.setLeftYAxisLabel(retObj.getFieldValueString(0, 18));
            graphDetails.setRightYAxisLabel(retObj.getFieldValueString(0, 19));
            graphDetails.setRowValues(retObj.getFieldValueString(0, 20));

            String showGT = retObj.getFieldValueString(0, 21);
            if ("Y".equalsIgnoreCase(showGT)) {
                graphDetails.setShowGT(true);
            }

            graphDetails.setDisplayRows(retObj.getFieldValueString(0, 22));

            String showAsTable = retObj.getFieldValueString(0, 23);
            if ("Y".equalsIgnoreCase(showAsTable)) {
                graphDetails.setShowAsTable(true);
            }

            String graphPropertyXml = retObj.getFieldValueClobString(0, dbCols[24]);
            PbGraphDisplay gd = new PbGraphDisplay();
            GraphProperty prop = gd.parseGraphPropertyXml(graphPropertyXml);
            graphDetails.setGraphProperty(prop);

            String timeSeries = retObj.getFieldValueString(0, 25);
            if (timeSeries != null && !"".equals(timeSeries)) {
                graphDetails.setTimeSeries(Boolean.parseBoolean(timeSeries));
            }
        }

        query = getResourceBundle().getString("getGraphDetails");
        objArr = new Object[1];
        objArr[0] = graphId;

        finalQuery = buildQuery(query, objArr);

        retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        StringBuilder elementIds = new StringBuilder();
        if (retObj != null && retObj.getRowCount() > 0) {
            String axis = retObj.getFieldValueString(0, 2);
            graphDetails.setAxis(axis);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                String elemId = retObj.getFieldValueString(i, 0);
                elementIds.append(",").append(elemId);
            }

            elementIds.replace(0, 1, "");

            List<QueryDetail> queryDetails = getQueryDetailList(reportId, elementIds.toString());
            graphDetails.setQueryDetails(queryDetails);

        }

        String drillquery = getResourceBundle().getString("getTextKpiDrill");
        PbReturnObject retobject = new PbReturnObject();
        Object obj[] = new Object[2];
        obj[0] = reportId;
        obj[1] = detail.getGraphId();
        String finalquery = null;
        finalquery = buildQuery(drillquery, obj);
        try {
            retobject = execSelectSQL(finalquery);
            if (retobject != null) {
                for (int i = 0; i < retobject.getRowCount(); i++) {
                    detail.TextkpiDrill.put(retobject.getFieldValueString(i, "TEXT_KPI_VALUE"), retobject.getFieldValueString(i, "DRILL_ID"));
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        //getTextKpi Comment Details
//        String commentquery = getResourceBundle().getString("getTextKpiComment");
//        Object obj1[] = new Object[3];
//        obj1[0] = reportId;
//        obj1[1] = detail.getGraphId();
//        finalquery = buildQuery(commentquery, obj1);
//        try {
//        retobject = execSelectSQL(finalQuery);
//        for(int i=0;i<retobject.getRowCount();i++){
//            detail.TextkpiComment.put(retobject.getFieldValueString(i, "KPI_VALUE"),retobject.getFieldValueString(i, "KPI_COMMENT") );
//        }
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }

        return graphDetails;
    }

    public String getTextKpiComment(String paramvalue, String dashId) {
        PbReturnObject returnObject = new PbReturnObject();
        String comment = null;
        String query = "select KPI_COMMENT from prg_textkpi_user_comments where KPI_VALUE='&' and DASHLET_ID='&'";
        Object obj[] = new Object[2];
        obj[0] = paramvalue;
        obj[1] = dashId;
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                comment = returnObject.getFieldValueString(0, 0);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return comment;
    }

    public String getTextKpiAllCommmentDetails(String paramvalue) {
        PbReturnObject returnObject = new PbReturnObject();
        HashMap map = new HashMap();
        String comment = null;
        String userid = null;
        String date = null;
        String query = "select COMMENT_DATE from prg_textkpi_user_comments where KPI_VALUE='&'";
        Object obj[] = new Object[1];
        obj[0] = paramvalue;
        String finalquery = super.buildQuery(query, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            for (int i = 0; i < returnObject.getRowCount(); i++) {
                date = returnObject.getFieldValueString(i, 0);
            }

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return date;
    }

    public String getUserName(String userid) {
        String username = null;
        PbReturnObject returnObject = new PbReturnObject();
        String loginquery = "select PU_LOGIN_ID from PRG_AR_USERS where PU_ID=&";
        Object obj[] = new Object[1];
        obj[0] = userid;
        String finalquery = super.buildQuery(loginquery, obj);
        try {
            returnObject = super.execSelectSQL(finalquery);
            username = returnObject.getFieldValueString(0, 0);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return username;
    }

    public List<KPIElement> getTragetKPIElements(List<String> targetElementList) {
        List<KPIElement> kpiElements = new ArrayList<KPIElement>();
        if (targetElementList == null || targetElementList.isEmpty()) {
        } else {
            StringBuilder sb = new StringBuilder();
            for (String elemId : targetElementList) {
                sb.append(",").append(elemId);
            }


            String query = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE, USER_COL_DESC  "
                    + "from  PRG_USER_ALL_INFO_DETAILS "
                    + "where ELEMENT_ID in (" + sb.substring(1) + ") "
                    + "order by ref_element_id, ref_element_type";
            PbReturnObject retObj = null;
            try {
                retObj = execSelectSQL(query);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }
            if (retObj != null && retObj.getRowCount() > 0) {
                int rowCount = retObj.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(retObj.getFieldValueString(i, 0));
                    kpiElem.setRefElementId(retObj.getFieldValueString(i, 1));
                    kpiElem.setRefElementType(retObj.getFieldValueString(i, 2));
                    kpiElem.setAggregationType(retObj.getFieldValueString(i, 3));
                    kpiElem.setElementName(retObj.getFieldValueString(i, 4));
                    kpiElements.add(kpiElem);
                }
            }

        }
        return kpiElements;


    }

    /**
     * @author srikanth.p for GroupMeassure Insights
     */
    public Report getGroupMeassureDashletDetails(String reportId, String graphId, DashletDetail detail) {
        GraphReport graphDetails = new GraphReport();

        String query = getResourceBundle().getString("getGraphMaster");
        Object[] objArr = new Object[1];
        objArr[0] = graphId;

        String finalQuery = buildQuery(query, objArr);

        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        if (retObj != null && retObj.getRowCount() > 0) {
            String[] dbCols = retObj.getColumnNames();
            graphDetails.setGraphName(retObj.getFieldValueString(0, 1));
            graphDetails.setGraphWidth(retObj.getFieldValueString(0, 2));
            graphDetails.setGraphHeight(retObj.getFieldValueString(0, 3));
            graphDetails.setGraphClass(retObj.getFieldValueInt(0, 4));
            graphDetails.setGraphClassName(retObj.getFieldValueString(0, 5));
            graphDetails.setGraphType(retObj.getFieldValueInt(0, 6));
            graphDetails.setGraphTypeName(retObj.getFieldValueString(0, 7));
            graphDetails.setGraphSize(retObj.getFieldValueInt(0, 8));
            graphDetails.setGraphSizeName(retObj.getFieldValueString(0, 9));
            graphDetails.setLegendLocation(retObj.getFieldValueString(0, 10));

            String allowLegend = retObj.getFieldValueString(0, 11);
            if ("Y".equalsIgnoreCase(allowLegend)) {
                graphDetails.setLegendAllowed(true);
            }

            String xAxisGrid = retObj.getFieldValueString(0, 12);
            if ("Y".equalsIgnoreCase(xAxisGrid)) {
                graphDetails.setShowXAxisGrid(true);
            }

            String yAxisGrid = retObj.getFieldValueString(0, 13);
            if ("Y".equalsIgnoreCase(yAxisGrid)) {
                graphDetails.setShowYAxisGrid(true);
            }

            String allowLink = retObj.getFieldValueString(0, 14);
            if ("Y".equalsIgnoreCase(allowLink)) {
                graphDetails.setLinkAllowed(true);
            }

            graphDetails.setBackgroundColor(retObj.getFieldValueString(0, 15));
            graphDetails.setFontColor(retObj.getFieldValueString(0, 16));

            String showData = retObj.getFieldValueString(0, 17);
            if ("Y".equalsIgnoreCase(showData)) {
                graphDetails.setShowData(true);
            }

            graphDetails.setLeftYAxisLabel(retObj.getFieldValueString(0, 18));
            graphDetails.setRightYAxisLabel(retObj.getFieldValueString(0, 19));
            graphDetails.setRowValues(retObj.getFieldValueString(0, 20));

            String showGT = retObj.getFieldValueString(0, 21);
            if ("Y".equalsIgnoreCase(showGT)) {
                graphDetails.setShowGT(true);
            }

            graphDetails.setDisplayRows(retObj.getFieldValueString(0, 22));

            String showAsTable = retObj.getFieldValueString(0, 23);
            if ("Y".equalsIgnoreCase(showAsTable)) {
                graphDetails.setShowAsTable(true);
            }

            String graphPropertyXml = retObj.getFieldValueClobString(0, dbCols[24]);
            PbGraphDisplay gd = new PbGraphDisplay();
            GraphProperty prop = gd.parseGraphPropertyXml(graphPropertyXml);
            graphDetails.setGraphProperty(prop);

            String timeSeries = retObj.getFieldValueString(0, 25);
            if (timeSeries != null && !"".equals(timeSeries)) {
                graphDetails.setTimeSeries(Boolean.parseBoolean(timeSeries));
            }
        }

        query = getResourceBundle().getString("getGraphDetails");
        objArr = new Object[1];
        objArr[0] = graphId;

        finalQuery = buildQuery(query, objArr);

        retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        StringBuilder elementIds = new StringBuilder();
        if (retObj != null && retObj.getRowCount() > 0) {
            String axis = retObj.getFieldValueString(0, 2);
            graphDetails.setAxis(axis);

            for (int i = 0; i < retObj.getRowCount(); i++) {
                String elemId = retObj.getFieldValueString(i, 0);
                elementIds.append(",").append(elemId);
            }

            elementIds.replace(0, 1, "");



        }
        List<QueryDetail> queryDetails = getGroupQueryDetailList(detail);
        graphDetails.setQueryDetails(queryDetails);

//        String drillquery = getResourceBundle().getString("getTextKpiDrill");
//        PbReturnObject retobject = new PbReturnObject();
//        Object obj[] = new Object[2];
//        obj[0] = reportId;
//        obj[1] = detail.getGraphId();
//        String finalquery = null;
//        finalquery = buildQuery(drillquery, obj);
//        try {
//            retobject = execSelectSQL(finalquery);
//            if(retobject!=null){
//            for(int i=0;i<retobject.getRowCount();i++){
//                detail.TextkpiDrill.put(retobject.getFieldValueString(i, "TEXT_KPI_VALUE"), retobject.getFieldValueString(i, "DRILL_ID"));
//            }
//            }
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }

        //getTextKpi Comment Details
//        String commentquery = getResourceBundle().getString("getTextKpiComment");
//        Object obj1[] = new Object[3];
//        obj1[0] = reportId;
//        obj1[1] = detail.getGraphId();
//        finalquery = buildQuery(commentquery, obj1);
//        try {
//        retobject = execSelectSQL(finalQuery);
//        for(int i=0;i<retobject.getRowCount();i++){
//            detail.TextkpiComment.put(retobject.getFieldValueString(i, "KPI_VALUE"),retobject.getFieldValueString(i, "KPI_COMMENT") );
//        }
//        } catch (SQLException ex) {
//            logger.error("Exception:",ex);
//        }

        return graphDetails;
    }

    public List<QueryDetail> getGroupQueryDetailList(DashletDetail detail) {
        String query = getResourceBundle().getString("getGroupQueryDetails");
        String allIds = detail.getGroupElements();

        String[] ids = allIds.split(",");
        String groupId = "";
        String elementIds = "";
        Object[] grpobj = new Object[1];
        for (int i = 0; i < ids.length; i++) {
            if (ids[i].indexOf("G_") != -1) {
                groupId = ids[i].substring(2);
            } else if (ids[i].indexOf("E_") != -1) {
                elementIds += "," + ids[i].substring(2);
            } else {
                groupId = ids[i];
            }
        }
        grpobj[0] = groupId;
        detail.setGroupId(groupId);
        String finalQuery = buildQuery(query, grpobj);
        PbReturnObject retObj = null;

        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        List<QueryDetail> queryDetails = new ArrayList<QueryDetail>();
        if (retObj != null && retObj.getRowCount() > 0) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                QueryDetail qd = new QueryDetail();
                qd.setElementId(retObj.getFieldValueString(i, 0));
                qd.setDisplayName(retObj.getFieldValueString(i, 1));
                qd.setRefElementId(retObj.getFieldValueString(i, 2));
                qd.setFolderId(retObj.getFieldValueInt(i, 4));
                qd.setSubFolderId(retObj.getFieldValueInt(i, 5));
                qd.setAggregationType(retObj.getFieldValueString(i, 3));
                qd.setColumnType(retObj.getFieldValueString(i, 6));
                queryDetails.add(qd);
            }
        }
        if (elementIds != "") {
            String elemeQuery = "SELECT DISTINCT E.element_id,E.USER_COL_DESC,E.REF_ELEMENT_ID,E.AGGREGATION_TYPE,E.FOLDER_ID,E.SUB_FOLDER_ID,E.USER_COL_TYPE FROM PRG_USER_ALL_INFO_DETAILS E WHERE E.element_id IN (" + elementIds.substring(1) + ")";
            try {
                retObj = execSelectSQL(elemeQuery);
                if (retObj != null && retObj.getRowCount() > 0) {
                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        QueryDetail qd = new QueryDetail();
                        qd.setElementId(retObj.getFieldValueString(i, 0));
                        qd.setDisplayName(retObj.getFieldValueString(i, 1));
                        qd.setRefElementId(retObj.getFieldValueString(i, 2));
                        qd.setFolderId(retObj.getFieldValueInt(i, 4));
                        qd.setSubFolderId(retObj.getFieldValueInt(i, 5));
                        qd.setAggregationType(retObj.getFieldValueString(i, 3));
                        qd.setColumnType(retObj.getFieldValueString(i, 6));
                        queryDetails.add(qd);
                    }
                }
            } catch (Exception e) {
                logger.error("Exception:", e);
            }

        }
        return queryDetails;

    }
    //added by srikanth to get prior elements for groupmeasure

    public List<KPIElement> getKPIElementsForGroups(List<String> elementIds, HashMap<String, String> piElementNamesAndIDs) {
        List<KPIElement> kpiElements = new ArrayList<KPIElement>();
        List<String> ElementIDlist = new ArrayList<String>();
        Set<String> keysSet = piElementNamesAndIDs.keySet();
        if (elementIds == null || elementIds.isEmpty()) {
        } else {
            StringBuilder sb = new StringBuilder();
            for (String elemId : elementIds) {
                sb.append(",").append(elemId);
            }

            String query = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE, USER_COL_DESC  "
                    + "from  PRG_USER_ALL_INFO_DETAILS "
                    + "where ELEMENT_ID in (" + sb.substring(1) + ") "
                    + "OR REF_ELEMENT_ID in (" + sb.substring(1) + ") ";
//                        + "order by ref_element_id, ref_element_type";

            PbReturnObject retObj = null;
            try {
                retObj = execSelectSQL(query);
            } catch (Exception e) {
                logger.error("Exception:", e);
            }

            if (retObj != null && retObj.getRowCount() > 0) {
                int rowCount = retObj.getRowCount();
                for (int i = 0; i < rowCount; i++) {
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(retObj.getFieldValueString(i, 0));
                    ElementIDlist.add(retObj.getFieldValueString(i, 0));
                    kpiElem.setRefElementId(retObj.getFieldValueString(i, 1));
                    kpiElem.setRefElementType(retObj.getFieldValueString(i, 2));
                    kpiElem.setAggregationType(retObj.getFieldValueString(i, 3));
                    if (piElementNamesAndIDs.isEmpty()) {
                        kpiElem.setElementName(retObj.getFieldValueString(i, 4));
                    } else {
                        kpiElem.setElementName(piElementNamesAndIDs.get(retObj.getFieldValueString(i, 0)));
                    }
                    kpiElements.add(kpiElem);
                }
            }

        }
        return kpiElements;
    }

    public HashMap getAssignedReports(ArrayList elementList, ArrayList parentElements, String groupId, String dbrdId, String dashletId) {
        PbReturnObject retObj = new PbReturnObject();
        HashMap<String, String[]> assignedMap = new HashMap<String, String[]>();
        String seleQuery = "";
        String elemString = "";
        for (int j = 0; j < elementList.size(); j++) {
            elemString += "," + elementList.get(j);
        }
        if (parentElements.size() > 1) {
            seleQuery = "SELECT CHILD_ELEMENT_ID,REF_REPORT_ID,REF_REPORT_TYPE FROM PRG_AR_GRP_DRILL WHERE (CHILD_ELEMENT_ID=PARENT_ELEMENT_ID OR PARENT_ELEMENT_ID IS NULL) AND GROUP_ID=" + groupId + " AND DASHBOARD_ID=" + dbrdId + " AND DASHLET_ID=" + dashletId;
        } else {
            seleQuery = "SELECT CHILD_ELEMENT_ID,REF_REPORT_ID,REF_REPORT_TYPE FROM PRG_AR_GRP_DRILL WHERE PARENT_ELEMENT_ID=" + parentElements.get(0) + " AND GROUP_ID=" + groupId + " AND DASHBOARD_ID=" + dbrdId + " AND DASHLET_ID=" + dashletId;
        }
        try {
            retObj = execSelectSQL(seleQuery);
            if (retObj.rowCount > 0) {

                for (int i = 0; i < retObj.rowCount; i++) {
                    String[] repDetails = new String[2];
                    repDetails[0] = retObj.getFieldValueString(i, 1);
                    repDetails[1] = retObj.getFieldValueString(i, 2);
                    assignedMap.put(retObj.getFieldValueString(i, 0), repDetails);
                }
            }

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return assignedMap;
    }

    public String getOneviewTableAndGraphHeader(OneViewLetDetails oneviewlet, String value, List<String> timedetails) throws Exception {
        StringBuilder finalStringVal = new StringBuilder();
        String regionId = oneviewlet.getNoOfViewLets();
        //kruthika
        String getreportfromgraph = oneviewlet.getgraphtoreport();
        String chartId = "chart-" + oneviewlet.getNoOfViewLets();   //+oneviewlet.getGrapNo()+"_"+oneviewlet.getRepId()+"_"+oneviewlet.getNoOfViewLets();
        String chartname = oneviewlet.getchartname();
        String idArr = oneviewlet.getchartdrills();
        String idArrrefresh = oneviewlet.getchartrefreshdrills();
        String rolename = oneviewlet.getRolename();
        String repid = oneviewlet.getRepId();
        String reptype = oneviewlet.getReptype();
        String repname = oneviewlet.getRepName();

        String drillviewby = oneviewlet.getdrillviewby();
        String graphId = oneviewlet.getGrapId();
        int grpNo = oneviewlet.getGrapNo();
        String olapFunc = "olapGraph('" + repname + "','" + repid + "','" + graphId + "','" + grpNo + "','" + timedetails + "','flase','" + regionId + "','" + chartname + "','" + rolename + "')";
        if (reptype.equalsIgnoreCase("repGraph")) {
            repname = oneviewlet.getgraphname();
        }
// Added By Ram
        finalStringVal.append("<div class='ui-tabs ui-widget ui-widget-content ui-corner-all' style='width: 100%; margin-left: 10x; margin-right: 10px;background-color:lightgray;'>");
        finalStringVal.append("<table  style='margin-left: 0px; width:100%;'>");//kruthika
//finalStringVal.append("<table  id=\"tableDashlets" + oneviewlet.getNoOfViewLets() + "\" style='margin-left: 0px; width:100%; background-color:#3e454d;'>");
        finalStringVal.append("<tr class=\"measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "\" >");
//            finalStringVal.append("<td width='2%'></td>");
        if (value != null) {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap'  >");
            // Added By Ram
//                 finalStringVal.append("<a style=\"color:#ffffff\" href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+oneviewlet.getNoOfViewLets()+"&REPORTID="+oneviewlet.getRepId()+"')\"");
            finalStringVal.append("<a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId=" + oneviewlet.getNoOfViewLets() + "&REPORTID=" + oneviewlet.getRepId() + "')\"");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;color:white;white-space:nowrap;\">" + repname + "</strong></a>");
            finalStringVal.append("</td>");
        } else {
            finalStringVal.append("<td id=\"Dashlets" + oneviewlet.getNoOfViewLets() + "\" style='font-size:12pt;color:#000000;white-space:nowrap'>");
            // Added By Ram
//                 finalStringVal.append("<a style=\"color:#ffffff\" href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId="+oneviewlet.getNoOfViewLets()+"&REPORTID="+oneviewlet.getRepId()+"')\"");
            finalStringVal.append("<a  href=\"javascript:submiturls12('reportViewer.do?reportBy=viewReport&action=reset&regId=" + oneviewlet.getNoOfViewLets() + "&REPORTID=" + oneviewlet.getRepId() + "')\"");
            finalStringVal.append("<strong id='forDillDown" + oneviewlet.getNoOfViewLets() + "' style=\"font-size: 12pt;white-space:nowrap\">" + repname + "</strong></a>");
            finalStringVal.append("</td>");
        }
        finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;'>");
        if (reptype.equalsIgnoreCase("repGraph")) {
            finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewRegd3(" + oneviewlet.getNoOfViewLets() + ",'" + chartname + "','" + idArrrefresh + "','" + repid + "','" + oneviewlet.getRepName() + "','" + drillviewby + "')\" href=\"#\"></a></td>");
            finalStringVal.append("<td id=\"olap" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-gear\" title=\"olapgraph\" onclick=\"" + olapFunc + "\")\" href=\"#\"></a></td>");
            if (oneviewlet.getUserStatus()) {
                finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewRegd3(" + oneviewlet.getNoOfViewLets() + ",'" + chartname + "','" + idArr + "','" + repid + "','" + oneviewlet.getRepName() + "','" + drillviewby + "')\" href=\"#\"></a></td>");
            }
        } else {
            finalStringVal.append("<td id=\"refreshTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-arrowrefresh-1-w\" title=\"Refresh Region\" onclick=\"refreshOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
//            finalStringVal.append("<td id=\"olap"+oneviewlet.getNoOfViewLets()+"\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-gear\" title=\"olapgraph\" onclick=\"" + olapFunc + "\")\" href=\"#\"></a></td>");
            if (oneviewlet.getUserStatus()) {
                finalStringVal.append("<td id=\"saveTabId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'><a style=\"text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;font-size: 9pt;\" class=\"ui-icon ui-icon-disk\" title=\"Save\" onclick=\"saveEachOneVIewReg(" + oneviewlet.getNoOfViewLets() + ")\" href=\"#\"></a></td>");
            }
        }
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionId" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-triangle-2-n-s\" onclick=\"selectforReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Region Options\"></a>");

            finalStringVal.append("<div id=\"reigonOptionsDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:120px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' class='overlapDiv'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"renameRegion('Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Rename</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getReptype() + "')\"  >Drill</a></td></tr></table>");
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"drillToReport('dashboard','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Drill To Dashboard</a></td></tr></table>");
            if (oneviewlet.getReptype().equalsIgnoreCase("repGraph")) {
                finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"graphImage('" + chartId + "','" + regionId + "')\" >Save Image As</a></td></tr></table>");
            }
            if (oneviewlet.isOneviewReportTimeDetails()) {
                finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"oneviewTimedetails('oneviewTime" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'    id='oneviewTime" + oneviewlet.getNoOfViewLets() + "'></input>Oneview Time</td></tr></table>");
                finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"reportTimedetails('reportTime" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='reportTime" + oneviewlet.getNoOfViewLets() + "'></input>Report Time</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"goSave('" + oneviewlet.getRepId()+ "')\" valign='top' href='javascript:void(0)' checked=''  id='reportTime"+oneviewlet.getNoOfViewLets()+"'></input>Transpose</td></tr></table>");
                finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"goSave('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getWidth() + "','" + oneviewlet.getHeight() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='reportTime" + oneviewlet.getNoOfViewLets() + "'></input>Transpose</td></tr></table>");

            } else {
                finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;'  onclick=\"oneviewTimedetails('oneviewTime" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''   id='oneviewTime" + oneviewlet.getNoOfViewLets() + "'></input>Oneview Time</td></tr></table>");
                finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"reportTimedetails('reportTime" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)'   id='reportTime" + oneviewlet.getNoOfViewLets() + "'></input>Report Time</td></tr></table>");
//            finalStringVal.append("<table><tr><td><input type='radio' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"goSave('" + oneviewlet.getRepId()+ "')\" valign='top' href='javascript:void(0)'   id='reportTime"+oneviewlet.getNoOfViewLets()+"'></input>Transpose</td></tr></table>");
                finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"goSave('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getWidth() + "','" + oneviewlet.getHeight() + "','" + oneviewlet.getRoleId() + "','" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" valign='top' href='javascript:void(0)' checked=''  id='reportTime" + oneviewlet.getNoOfViewLets() + "'></input>Transpose</td></tr></table>");

            }
//            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"saveEachOneVIewReg("+oneviewlet.getNoOfViewLets()+")\"  >Save</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        }
//            finalStringVal.append("<td id=\"regionId" + oneviewlet.getNoOfViewLets() + "\" style='height:10px;>");
        if (oneviewlet.getUserStatus()) {
            finalStringVal.append("<td id=\"optionIds" + oneviewlet.getNoOfViewLets() + "\" style='width:1%;align:right;'>");
            finalStringVal.append("<a href='javascript:void(0)' class=\"ui-icon ui-icon-plusthick\" onclick=\"selectReadd(" + oneviewlet.getNoOfViewLets() + ")\"  style='text-decoration:none'  title=\"Re Add Onevielet\"></a>");

            finalStringVal.append("<div id=\"readdDivId" + oneviewlet.getNoOfViewLets() + "\" style='display:none;width:80px;height:auto;background-color:white;overflow:auto;position:absolute;text-align:left;border:1px solid #000000;' class='overlapDiv'>");
            finalStringVal.append("<table border='0' align='left' >");
            finalStringVal.append("<tr><td>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('report','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Reports</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('measures','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Measures</a></td></tr></table>");

            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('dashboard','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >KPIs</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;' onclick=\"selectedType('headline','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Headlines</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('complexkpi','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Custom KPI</a></td></tr></table>");
            finalStringVal.append("<table><tr><td><a href='javascript:void(0)' style='text-decoration: none;font-family: Calibri, Calibri, Calibri, sans-serif;white-space:nowrap;' onclick=\"selectedType('notes','Dashlets-" + oneviewlet.getNoOfViewLets() + "'," + oneviewlet.getNoOfViewLets() + "," + oneviewlet.getWidth() + "," + oneviewlet.getHeight() + ",'Dashlets" + oneviewlet.getNoOfViewLets() + "','GrpTyp" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" >Notes</a></td></tr></table>");
            finalStringVal.append("</td></tr>");
            finalStringVal.append("</table>");
            finalStringVal.append("</div>");
            finalStringVal.append("</td>");
        } //kruthika
        if (getreportfromgraph != null && getreportfromgraph.equalsIgnoreCase("true")) {
            finalStringVal.append("<td align='left' class=graphTd1'" + oneviewlet.getNoOfViewLets() + "' width='1%'> <a  target='_blank' class='ui-icon ui-icon-image' href= \"reportViewer.do?reportBy=viewReport&REPORTID=" + oneviewlet.getAssignedReportId() + "  &action=reset \"   ></a></td>");

        } else {
        }
        finalStringVal.append("</tr></table></div>");
        return finalStringVal.toString();
    }

    public String getOneviewMeasures(OneViewLetDetails oneviewlet, int heigth, int width, String displayTest, String measureType, String currWidth, String compare, String priorwidth, BigDecimal curval, BigDecimal chper, BigDecimal prior, String newType, String context, List<String> tiemdetails, OnceViewContainer onecontainer, HttpSession session) {
        StringBuilder measureText = new StringBuilder();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
        String formatType = "";
        String formatValue = "";
        String datetype = "";
        String measurecolor = "";
        OneViewBD viewBd = new OneViewBD();
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (!oneviewlet.getisschedule()) {
            datetype = (String) session.getAttribute("oneviewtype");
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
        //added by srikanth.p

        ArrayList assignedGraphIds = oneviewlet.getAssignedGraphIds();
        ArrayList assignedGraphNames = oneviewlet.getAssignedGraphNames();
        String firstTest = "display:none;";
        String secondTest = "display:none;";
        String thirdTest = "display:none;";
        String fourthTest = "display:none;";
        String fifthTest = "display:none;";
        String prefixValue = "";
        String suffixValue = "";
        if (oneviewlet.getPrefixValue() != null) {
            prefixValue = oneviewlet.getPrefixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            suffixValue = oneviewlet.getSuffixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            if (oneviewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }
        if (oneviewlet.getDisplayType() == null || oneviewlet.getDisplayType().equalsIgnoreCase("fifth")) {
            firstTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("first")) {
            secondTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("second")) {
            thirdTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("third")) {
            fourthTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("fourth")) {
            fifthTest = "display:;";
        }

        if (oneviewlet.getMeasureColor() != null && !oneviewlet.getMeasureColor().isEmpty()) {
            measurecolor = oneviewlet.getMeasureColor();
        }
        if (measurecolor.equalsIgnoreCase("")) {
            if (oneviewlet.getCurrValue().contains("-")) {
                measurecolor = "red";
            }
        }
        if (oneviewlet.isLogicalColor()) {
            measurecolor = viewBd.getRangeBasedColor(oneviewlet, tiemdetails, curval, onecontainer.timeHashMap);
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("%")) {
            formatType = "%";
            formatValue = "%";
        }
        String currFourthVal = "";
        String priorFourthVal = "";
        currFourthVal = oneviewlet.getCurrValue();
        priorFourthVal = oneviewlet.getPriorValue();
        int minRadious = 40;
        int minLength = (6 * minRadious) / 40;
        String lformatType = formatType;
        String lsuffixValue = suffixValue;

        if (oneviewlet.getDisplayType() != null && (currFourthVal.length() > minLength || priorFourthVal.length() > minLength)) { //&& !(formatType.trim().length() >0)
            Double currVal = Double.parseDouble(currFourthVal.replace(formatType, "").replace(",", ""));
            Double priorVal = Double.parseDouble(priorFourthVal.replace(formatType, "").replace(",", ""));
            HashMap valMap = new HashMap();
            valMap.put("currVal", currVal);
            valMap.put("priorVal", priorVal);

            HashMap FormattedValMap = new HashMap();
            FormattedValMap = viewBd.getFormatedMesureValues(valMap, minRadious, formatType, Integer.parseInt(oneviewlet.getRoundVal()));
            currFourthVal = FormattedValMap.get("currVal").toString();
            priorFourthVal = FormattedValMap.get("priorVal").toString();
            lformatType = FormattedValMap.get("format").toString();
            lsuffixValue = lformatType;
        }



//        if(formatType.equalsIgnoreCase("") && oneviewlet.getDisplayType() != null && oneviewlet.getDisplayType().equalsIgnoreCase("third")){
//            if(oneviewlet.getCurrValue().length() >8){
//                currFourthVal = NumberFormatter.getModifiedNumber(new BigDecimal(oneviewlet.getCurrValue().replace(",", "")), "K", 0);
//            }else{
//                currFourthVal = oneviewlet.getCurrValue();
//            }
//            if(oneviewlet.getPriorValue().length() >8){
//                priorFourthVal = NumberFormatter.getModifiedNumber(new BigDecimal(oneviewlet.getPriorValue().replace(",", "")), "K", 0);
//            }else{
//                priorFourthVal = oneviewlet.getPriorValue();
//            }
//        }else{

//        }
//        if(oneviewlet.getDisplayType() != null && oneviewlet.getDisplayType().equalsIgnoreCase("third")){
//           if(Float.parseFloat(currFourthVal.replace(formatType, "").replace(",", "")) < Float.parseFloat(priorFourthVal.replace(formatType, "").replace(",", ""))){
//               currFourthVal = oneviewlet.getPriorValue();
//               priorFourthVal = oneviewlet.getCurrValue();
//            }
//        }
        if (!oneviewlet.getFormatVal().equalsIgnoreCase("K") && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("%") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + oneviewlet.getCurrValue() + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }
            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)'  class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\"></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }
            measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%' ><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + oneviewlet.getCurrValue() + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('( Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }
            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%' ><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "','" + oneviewlet.getRoleId() + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
//                measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:;' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + oneviewlet.getRepName() + " , Prior " + oneviewlet.getRepName() + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'>" + oneviewlet.getRepName() + " Increased by " + chper + "% Compare With " + compare + "</td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='" + oneviewlet.getRepName() + "Increased by  " + chper + "% Compare With " + compare + "' ></td></tr></table></td></tr></table></center>");
            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }
                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }
                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            } else {
                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }
                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }
                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            }
            //   }
//                measureText.append("<center><table align='center' style='overflow:auto;' height='" + heigth + "px' width='" + width + "px'><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:20pt' ><a onclick=\"getMeasureGraph('"+oneviewlet.getRepId()+"','"+oneviewlet.getRepName()+"','"+oneviewlet.getOneviewId()+"','"+oneviewlet.getRoleId()+"')\" href='javascript:void(0)'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + oneviewlet.getRepName() + " , Prior " + oneviewlet.getRepName() + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() / 2 + "px' width='" + oneviewlet.getWidth() + "px' align='left'><table ><tr><td  width='" + (oneviewlet.getWidth() - 120) + "px'></td><td id='addCommentsId' style='' width='120px'><a title='Add/View Comments' onclick=\"measureComments('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "')\" href='javascript:void(0)'>Add/View Comments</a></td><td width='3%'><a class='ui-icon ui-icon-image'  style='text-decoration: none;' onclick=\"measureOptions('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','"+formatter.format(chper)+"','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "')\" title='MeasuresOptions' href='javascript:void(0)'></a></td></tr></table><table width='" + oneviewlet.getWidth() + "px' border='1' style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td id='measureDescId"+oneviewlet.getNoOfViewLets()+"' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000'>" + oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With "+compare+"</td><td style='display:none'><input type='text' id='measureValue"+oneviewlet.getNoOfViewLets()+"' name='' value='"+oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With "+compare+"' ></td></tr></table></td></tr></table></center>");
//                } else {
//                    measureText.append("<center><table id='compareMeasId" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);;'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' >" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");
//                }
        } else {
            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }
            }

            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");


            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr id='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "' class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }

            measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#008000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Green Arrow.jpg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('( Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#008000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");

            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }

            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%' ><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "','" + oneviewlet.getRoleId() + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");

            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
//                                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;"+fourthTest+"' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 55px; height: 55px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>"  + prefixValue+currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px'><font style='color: white;'>"  +prefixValue+priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            } else {
//                           measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;"+fourthTest+"' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 40px; height: 40px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>"  + prefixValue+currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>"  +prefixValue+priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            }
            //   }
//                } else {
//                    measureText.append("<center><table id='compareMeasId" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "'>" + oneviewlet.getPriorValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' >" + oneviewlet.getCurrValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");
//                }
        }
        return measureText.toString();
    }

    public String getOneviewMeasures1(OneViewLetDetails oneviewlet, int heigth, int width, String displayTest, String measureType, String currWidth, String compare, String priorwidth, BigDecimal curval, BigDecimal chper, BigDecimal prior, String newType, String context, String priorValstr, List<String> tiemdetails, OnceViewContainer oneContainer, HttpSession session) {
        StringBuilder measureText = new StringBuilder();
        NumberFormat formatter = NumberFormat.getInstance(new Locale("en_US"));
        String formatType = "";
        String formatValue = "";
        String measurecolor = "";
        String datetype = "";
        OneViewBD viewBd = new OneViewBD();
        //Start of code by sandeep on 17/10/14 for schedule// update local files in oneview
        if (!oneviewlet.getisschedule()) {
            datetype = (String) session.getAttribute("oneviewtype");
        }
        //End of code by sandeep on 17/10/14 for schedule// update local files in oneview
//float version =oneviewlet.getversion();

        ArrayList assignedGraphIds = oneviewlet.getAssignedGraphIds();
        ArrayList assignedGraphNames = oneviewlet.getAssignedGraphNames();
        String firstTest = "display:none;";
        String secondTest = "display:none;";
        String thirdTest = "display:none;";
        String fourthTest = "display:none;";
        String fifthTest = "display:none;";
        String prefixValue = "";
        String suffixValue = "";
        if (oneviewlet.getPrefixValue() != null) {
            prefixValue = oneviewlet.getPrefixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            suffixValue = oneviewlet.getSuffixValue();
        }
        if (oneviewlet.getSuffixValue() != null) {
            if (oneviewlet.getSuffixValue().equalsIgnoreCase("K")) {
                suffixValue = "K";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("L")) {
                suffixValue = "Lkh";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("M")) {
                suffixValue = "Mn";
            } else if (oneviewlet.getSuffixValue().equalsIgnoreCase("Cr")) {
                suffixValue = "Crs";
            }
        }

        if (oneviewlet.getDisplayType() == null || oneviewlet.getDisplayType().equalsIgnoreCase("fifth")) {
            firstTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("first")) {
            secondTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("second")) {
            thirdTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("third")) {
            fourthTest = "display:;";
        } else if (oneviewlet.getDisplayType().equalsIgnoreCase("fourth")) {
            fifthTest = "display:;";
        }
        if (oneviewlet.getMeasureColor() != null && !oneviewlet.getMeasureColor().isEmpty()) {
            measurecolor = oneviewlet.getMeasureColor();
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("K")) {
            formatType = "K";
            formatValue = "K";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("M")) {
            formatType = "M";
            formatValue = "Mn";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("L")) {
            formatType = "L";
            formatValue = "Lkh";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            formatType = "Cr";
            formatValue = "Crs";
        }
        if (oneviewlet.getFormatVal() != null && oneviewlet.getFormatVal().equalsIgnoreCase("%")) {
            formatType = "%";
            formatValue = "%";
        }
        if (measurecolor.equalsIgnoreCase("")) {
            if (oneviewlet.getCurrValue().contains("-")) {
                measurecolor = "red";
            }
        }
        if (oneviewlet.isLogicalColor()) {
            measurecolor = viewBd.getRangeBasedColor(oneviewlet, tiemdetails, curval, oneContainer.timeHashMap);
        }
        String currFourthVal = "";
        String priorFourthVal = "";
        currFourthVal = oneviewlet.getCurrValue();
        priorFourthVal = oneviewlet.getPriorValue();
        int minRadious = 40;
        int minLength = (6 * minRadious) / 40;
        String lformatType = formatType;
        String lsuffixValue = suffixValue;
//            
//            

        if (oneviewlet.getDisplayType() != null && (currFourthVal.length() > minLength || priorFourthVal.length() > minLength)) { //&& !(formatType.trim().length() >0)
            Double currVal = Double.parseDouble(currFourthVal.replace(formatType, "").replace(",", ""));
            Double priorVal = Double.parseDouble(priorFourthVal.replace(formatType, "").replace(",", ""));
            HashMap valMap = new HashMap();
//                
            valMap.put("currVal", currVal);
            valMap.put("priorVal", priorVal);

            HashMap FormattedValMap = new HashMap();
            FormattedValMap = viewBd.getFormatedMesureValues(valMap, minRadious, formatType, Integer.parseInt(oneviewlet.getRoundVal()));
            currFourthVal = FormattedValMap.get("currVal").toString();
            priorFourthVal = FormattedValMap.get("priorVal").toString();
            lformatType = FormattedValMap.get("format").toString();
            lsuffixValue = lformatType;
//                
        }
//        if(formatType.equalsIgnoreCase("") && oneviewlet.getDisplayType() != null && oneviewlet.getDisplayType().equalsIgnoreCase("third")){
//            if(oneviewlet.getCurrValue().length() >8){
//                currFourthVal = NumberFormatter.getModifiedNumber(new BigDecimal(oneviewlet.getCurrValue().replace(",", "")), "K", 0);
//            }else{
//                currFourthVal = oneviewlet.getCurrValue();
//            }
//            if(oneviewlet.getPriorValue().length() >8){
//                priorFourthVal = NumberFormatter.getModifiedNumber(new BigDecimal(oneviewlet.getPriorValue().replace(",", "")), "K", 0);
//            }else{
//                priorFourthVal = oneviewlet.getPriorValue();
//            }
//        }else{

//        }
//        if(oneviewlet.getDisplayType() != null && oneviewlet.getDisplayType().equalsIgnoreCase("third")){
//           if(Float.parseFloat(currFourthVal.replace(formatType, "").replace(",", "")) < Float.parseFloat(priorFourthVal.replace(formatType, "").replace(",", ""))){
//               currFourthVal = oneviewlet.getPriorValue();
//               priorFourthVal = oneviewlet.getCurrValue();
//            }
//        }


        if (!oneviewlet.getFormatVal().equalsIgnoreCase("K") && oneviewlet.getPrefixValue() == null && oneviewlet.getSuffixValue() == null && !oneviewlet.getFormatVal().equalsIgnoreCase("%") && !oneviewlet.getFormatVal().equalsIgnoreCase("M") && !oneviewlet.getFormatVal().equalsIgnoreCase("L") && !oneviewlet.getFormatVal().equalsIgnoreCase("Cr")) {
            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:18pt;'>" + oneviewlet.getCurrValue() + "</span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }
            }
            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }
            }
            measureText.append("<td id='measureNavigateIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;text-align:left;vertical-align:top;'>" + oneviewlet.getCurrValue() + "</a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#FF0000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");
            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }
            }
            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%' ><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "','" + oneviewlet.getRoleId() + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");
//            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:;' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;'>" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + oneviewlet.getRepName() + " , Prior " + oneviewlet.getRepName() + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2'  width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'>" + oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With " + compare + "</td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='" + oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With " + compare + "' ></td></tr></table></td></tr></table></center>");
            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {
                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }
                }
                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }
                }
                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            } else {
                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }


                }
                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");
                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }


                }
                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr></table></center>");
            }

            //   }
//            } else {
//                measureText.append("<center><table id='compareMeasId" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue() + "</td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue() + "</td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' >" + oneviewlet.getCurrValue() + "</a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");
//            }
        } else {
//                 String isdesigner = oneviewlet.getisdesigner();

            measureText.append("<center><table id='compareMeasIdfirst" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + firstTest + "' height=auto; width=auto;><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currValfirst" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:16pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' ><span style='color:" + measurecolor + ";font-size:16pt;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:16pt;'>" + suffixValue + "</span></span></a></td><td colspan='1' align='right'></td></tr><tr height='60'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");


            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }

            measureText.append("<td id='measureNavigateId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoId" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");

//                 measureText.append("</table></center>");

            measureText.append("<center><table id='compareMeasurIdsecond" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + secondTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span> </td></tr></table></td></tr><tr><td><table><tr><td style='white-space:nowrap;'>" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' style='white-space:nowrap;'>" + prefixValue + oneviewlet.getPriorValue().replace(formatType, "") + "<span style='font-size:6pt;'>" + suffixValue + "</span></td></tr></table></td></tr></table></td></tr><tr height='40'><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }

            measureText.append("<td id='second" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdsecond" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + thirdTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td><table><tr><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;text-align:left;vertical-align:top;'>" + prefixValue + oneviewlet.getCurrValue().replace(formatType, "") + "<span style='font-size:9pt;'>" + suffixValue + "</span></a></td><td><img  style='border-left:medium hidden;border-top:medium hidden;' width='40px' height='40px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('( Prior " + oneviewlet.getRepName() + "=" + formatter.format(prior) + ")')\"/><font style='color:#FF0000;'>(" + chper + "%)</font></td></tr></table></td></tr><tr><td colspan='2' ' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='display:none;border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'></td>");

            if (assignedGraphIds != null) {
                String tdWidth = "0";
                for (int i = 0; i < assignedGraphIds.size(); i++) {
                    if (i > 0) {
                        tdWidth = "1%";
                    }
                    if (datetype != null && datetype.equalsIgnoreCase("true")) {
                    } else {
                        measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                    }
                }

            }

            measureText.append("<td id='measureNavigateIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "','" + oneviewlet.getRoleId() + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdthird" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='' ></td></tr></table></td></tr></table></td></tr></table></center>");

//            measureText.append("<center><table id='compareMeasurIdthird" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:;' height='" + heigth + "px' width='" + width + "px'><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' ><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:18pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' style='color:#FF0000;'>" + oneviewlet.getCurrValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></a></td><td colspan='1' align='right'><img  style='border-left:medium hidden;border-top:medium hidden;' width='80px' height='80px' id='imgId" + oneviewlet.getNoOfViewLets() + "' src=\"" + context + "/images/Red Arrow.jpeg\" onmouseout=\"return nd()\" onmouseover=\"return overlib('(" + oneviewlet.getRepName() + " , Prior " + oneviewlet.getRepName() + "=" + formatter.format(curval) + "  ,  " + formatter.format(prior) + ")')\"/></td></tr><tr><td colspan='2' ' width='" + oneviewlet.getWidth() + "px' align='left'><table width='" + oneviewlet.getWidth() + "px'  style='border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;'><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/><td id='measureDescId" + oneviewlet.getNoOfViewLets() + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#FF0000;font-size:7pt;' height='" + oneviewlet.getHeight() + "px'>" + oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With " + compare + "</td><td style='display:none'><input type='text' id='measureValue" + oneviewlet.getNoOfViewLets() + "' name='' value='" + oneviewlet.getRepName() + " Decreased by " + chper + "% Compare With " + compare + "' ></td></tr></table></td></tr></table></center>");
            if (Float.parseFloat(currFourthVal.replace(lformatType, "").replace(",", "")) > Float.parseFloat(priorFourthVal.replace(lformatType, "").replace(",", ""))) {

                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }
                }

                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }
                }

                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            } else {

                measureText.append("<center><table id='compareMeasurIdfourth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fourthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :40px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :50px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFourth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfourth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");
                measureText.append("<center><table id='compareMeasurIdfifth" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;" + fifthTest + "' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' ><td align=''><table><tr><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 60px; height: 60px; color: rgb(255, 255, 255); background-color: rgb(0, 149, 182); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='currValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:10px;'><font style='color: white;'>" + prefixValue + currFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span> </font></td></tr></table></center></div></td><td><div onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" style='cursor:pointer;border-radius :5px; -moz-border-radius: 10px; -webkit-border-radius: 10px; width: 75px; height: 75px; color: rgb(255, 255, 255); background-color: rgb(154, 205, 50); border: solid black 0px;'><center><table align='center'><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr></tr><tr><td id='priorValueFifth" + oneviewlet.getNoOfViewLets() + "' style='padding:16px;'><font style='color: white;'>" + prefixValue + priorFourthVal.replace(lformatType, "") + "<span style='font-size:6pt;'>" + lsuffixValue + "</span></font></td></tr></table></center></div></td></tr></table></td></tr><tr><td><table><tr class='measureNavigateTrId" + oneviewlet.getNoOfViewLets() + "'><td/>");

                if (assignedGraphIds != null) {
                    String tdWidth = "0";
                    for (int i = 0; i < assignedGraphIds.size(); i++) {
                        if (i > 0) {
                            tdWidth = "1%";
                        }
                        if (datetype != null && datetype.equalsIgnoreCase("true")) {
                        } else {
                            measureText.append("<td align='right' width='" + tdWidth + "' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' class='graphTd" + oneviewlet.getNoOfViewLets() + "'><a href='javascript:void(0)' title='" + assignedGraphNames.get(i) + "' class='ui-icon ui-icon-image' onclick=\"olapGraph('" + assignedGraphNames.get(i) + "','" + oneviewlet.getAssignedReportId() + "','" + assignedGraphIds.get(i) + "','" + i + "','" + tiemdetails + "','false')\"></a></td>");
                        }
                    }

                }

                measureText.append("<td id='measureNavigateIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-contact\" onclick=\"selectNavigation('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Insights\"></a></td><td id='relatedMeasureInfoIdfifth" + oneviewlet.getNoOfViewLets() + "' align='right' valign='baseline' style='border-top:medium hidden;border-left:medium hidden;border-right:medium hidden;border-bottom:medium hidden;color:#008000;font-size:7pt;' width='1%'><a href='javascript:void(0)' class=\"ui-icon ui-icon-info\" onclick=\"relatedMeasureInfo('" + oneviewlet.getNoOfViewLets() + "','" + oneviewlet.getOneviewId() + "','" + formatter.format(curval) + "','" + formatter.format(chper) + "','" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + formatter.format(prior) + "')\"  style='text-decoration:none'  title=\"Related Measure Info\"></a></td></tr></table></td></tr><tr><td height='" + oneviewlet.getHeight() + "px'></td></tr></table></center>");

            }
            // }
//            } else {
//                measureText.append("<center><table id='compareMeasId" + oneviewlet.getNoOfViewLets() + "' align='' style='overflow:auto;display:' height='" + heigth + "px' width='" + width + "px'><tr id='dynamicMeasId" + oneviewlet.getNoOfViewLets() + "' style='" + displayTest + "'><td align=''><table><tr><td><table><tr><td >" + measureType + "</td></tr></table></td><td><table><tr><td width='" + currWidth + "' style='color: rgb(255, 255, 255); background-color: rgb(0, 149, 182);'></td><td id='currValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getCurrValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></td></tr></table></td></tr><tr><td><table><tr><td >" + compare + "</td></tr></table></td><td><table><tr><td width='" + priorwidth + "' style='color: rgb(255, 255, 255); background-color: rgb(154, 205, 50);'></td><td id='priorValue" + oneviewlet.getNoOfViewLets() + "' >" + oneviewlet.getPriorValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></td></tr></table></td></tr></table></td></tr><tr id='measureTypeId" + oneviewlet.getNoOfViewLets() + "' style='" + newType + "'><td id='currVal" + oneviewlet.getNoOfViewLets() + "' colspan='1'  style='font-size:25pt' ><a onclick=\"getMeasureGraph('" + oneviewlet.getRepId() + "','" + oneviewlet.getRepName() + "','" + oneviewlet.getOneviewId() + "','" + oneviewlet.getRoleId() + "')\" href='javascript:void(0)' >" + oneviewlet.getCurrValue().replace(formatType, "") + " <sub>" + formatValue + "</sub></a></td><td colspan='1' align='right'></td></tr><tr><td colspan='2' height='" + oneviewlet.getHeight() + "px' width='" + oneviewlet.getWidth() + "px' align='left'></td></tr></table></center>");
//            }
        }

        return measureText.toString();
    }

    public KPI getKpiDetailsForKPIWithGRaph(DashletDetail dashlet) {
        KPI kpiDetails = new KPI();
        String dashletId = dashlet.getDashBoardDetailId();

        String sqlSqery = "SELECT KPI_ID,GRAPH_ID,GRAPH_TYPE,VIEWBY,ELEMENT_IDS,GRAPH_MEASURE,SEQUENCE_NUM,REF_REPORT_ID,REF_REPORT_TYPE FROM PRG_AR_KPIWITHGRAPH WHERE DASHLET_ID=" + dashletId + " ORDER BY SEQUENCE_NUM";//
        PbReturnObject elementsRetobj = null;
        ArrayList<String> elementList = new ArrayList<String>();
        String elements = "";
        try {

            elementsRetobj = execSelectSQL(sqlSqery);
            if (elementsRetobj != null && elementsRetobj.rowCount > 0) {
                String kpiMasterId = elementsRetobj.getFieldValueString(0, 0);
                String graphId = elementsRetobj.getFieldValueString(0, 1);
                String dispType = elementsRetobj.getFieldValueString(0, 0);
                String grpahType = elementsRetobj.getFieldValueString(0, 2);
                String viewBy = elementsRetobj.getFieldValueString(0, 3);
                String graphMeasure = elementsRetobj.getFieldValueString(0, 5);
                String elementId = "";
                String reportId = "";
                String reportType = "";
                for (int i = 0; i < elementsRetobj.rowCount; i++) {
                    elementId = elementsRetobj.getFieldValueString(i, 4);
                    reportId = elementsRetobj.getFieldValueString(i, 7);
                    reportType = elementsRetobj.getFieldValueString(i, 8);
                    elementList.add(elementId);
                    kpiDetails.addKPIDrill(elementId, reportId);
                    kpiDetails.addKPIDrillRepType(elementId, reportType);

                }
//                String[] elemArray=elements.split(",");
//                for(int j=0;j<elemArray.length;j++)
//                {
//                    elementList.add(elemArray[j]);
//                }
                DashboardViewerDAO dao = new DashboardViewerDAO();
                List<KPIElement> kpiElements = dao.getKPIElements(elementList, new HashMap<String, String>());
                kpiDetails.setElementIds(elementList);
                for (KPIElement kpiElem : kpiElements) {
                    kpiDetails.addKPIElement(kpiElem.getRefElementId(), kpiElem);
                }
                dashlet.setKpiMasterId(kpiMasterId);
                dashlet.setAssignedGraphId(graphId);
                dashlet.setAssignedGraphType(grpahType);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return kpiDetails;
    }

    public String getOneViewRollingGraphJQ(String measureId, String date, String userId, String viewLetId, int height, int width, OneViewLetDetails viewLet, String isZoomTrend) {
        PbReturnObject pbretObjForTime = null;
        ProgenJQPlotGraph jqGraph = new ProgenJQPlotGraph();
        StringBuffer resultGraph = new StringBuffer();
        String measureName = viewLet.getRepName();
        ArrayList rowview = new ArrayList();
        String buzRolId = viewLet.getRoleId();
        rowview.add("TIME");
        PbReportQuery timequery = new PbReportQuery();
        ArrayList QueryCols = new ArrayList();
        ArrayList QueryAggs = new ArrayList();
        List<String> measureIdVal = new ArrayList<String>();
        String trendColor = viewLet.getTrendColor();
        measureIdVal.add(measureId);
        List<KPIElement> kpiElements = getKPIElements(measureIdVal, new HashMap<String, String>());
        if (kpiElements != null) {
            for (KPIElement elem : kpiElements) {
                if (elem.getElementId().equalsIgnoreCase(measureId)) {
                    if (elem.getElementName() != null) {
                        QueryCols.add(elem.getElementId());
                    }
                    QueryAggs.add(elem.getAggregationType());
                }
            }
        }
        int trendDays = viewLet.getTrendDays();
        String DisplayDays = "Last 30 Days";
        String legend = "30 d";
        String tickInterwels = "1 Days";
        ArrayList arl = new ArrayList();
        if (isZoomTrend != null && isZoomTrend.equalsIgnoreCase("false")) {
            switch (trendDays) {
                case 0: {
                    DisplayDays = "Last 30 Days";
                    legend = "30 d";
                    tickInterwels = "1 Days";
                    viewLet.setTrendDays(30);
                    break;
                }
                case 30: {
                    DisplayDays = "Last 60 Days";
                    legend = "60 d";
                    tickInterwels = "2 Days";
                    viewLet.setTrendDays(60);
                    break;
                }
                case 60: {
                    DisplayDays = "Last 90 Days";
                    legend = "90 d";
                    tickInterwels = "3 Days";
                    viewLet.setTrendDays(90);
                    break;
                }
                case 90: {
                    DisplayDays = "Last 180 Days";
                    legend = "180 d";
                    tickInterwels = "6 Days";
                    viewLet.setTrendDays(180);
                    break;
                }
                case 180: {
                    DisplayDays = "Last 365 Days";
                    tickInterwels = "12 Days";
                    legend = "365 d";
                    viewLet.setTrendDays(365);
                    break;
                }
                case 365: {
                    DisplayDays = "Last 30 Days";
                    legend = "30 d";
                    tickInterwels = "1 Days";
                    viewLet.setTrendDays(30);
                    break;
                }
            }
            arl.add("Day");
            arl.add("PRG_DAY_ROLLING");
            arl.add(date);
            arl.add(DisplayDays);
        } else {
            switch (trendDays) {
                case 0: {
                    DisplayDays = "Last 30 Days";
                    legend = "30 d";
                    tickInterwels = "1 Days";
                    viewLet.setTrendDays(30);
                    break;
                }
                case 30: {
                    DisplayDays = "Last 30 Days";
                    legend = "30 d";
                    tickInterwels = "1 Days";
                    viewLet.setTrendDays(30);
                    break;
                }
                case 60: {
                    DisplayDays = "Last 60 Days";
                    legend = "60 d";
                    tickInterwels = "2 Days";
                    viewLet.setTrendDays(60);
                    break;
                }
                case 90: {
                    DisplayDays = "Last 90 Days";
                    legend = "90 d";
                    tickInterwels = "3 Days";
                    viewLet.setTrendDays(90);
                    break;
                }
                case 180: {
                    DisplayDays = "Last 180 Days";
                    tickInterwels = "6 Days";
                    legend = "180 d";
                    viewLet.setTrendDays(180);
                    break;
                }
                case 365: {
                    DisplayDays = "Last 365 Days";
                    legend = "365 d";
                    tickInterwels = "12 Days";
                    viewLet.setTrendDays(365);
                    break;
                }
            }
            arl.add("Day");
            arl.add("PRG_DAY_ROLLING");
            arl.add(date);
            arl.add(DisplayDays);
        }


        timequery.setRowViewbyCols(rowview);
        timequery.setColViewbyCols(new ArrayList());
        timequery.setColViewbyCols(new ArrayList());
        timequery.setQryColumns(QueryCols);
        timequery.setColAggration(QueryAggs);
        timequery.setTimeDetails(arl);
        timequery.setDefaultMeasure(String.valueOf(QueryCols.get(0)));
        timequery.setDefaultMeasureSumm(String.valueOf(QueryAggs.get(0)));
        timequery.isTimeSeries = false;
        timequery.isTimeDrill = false;
        timequery.setBizRoles(buzRolId);
        timequery.setUserId(userId);
        pbretObjForTime = timequery.getPbReturnObject(String.valueOf(QueryCols.get(0)));

        jqGraph.setTrendLegend(legend);
        jqGraph.tickIntervels = tickInterwels;
        if (isZoomTrend != null && isZoomTrend.equalsIgnoreCase("true")) {
            jqGraph.setChartId("zoom" + viewLetId);
            jqGraph.setGraphType("Line");
            resultGraph.append("<div id='chart-zoom" + viewLetId + "' style='width:" + width + "px; height:" + height + "px;'></div>");
        } else {
            jqGraph.setChartId(viewLetId);
            jqGraph.setGraphType("Line(Smooth)");
            resultGraph.append("<style type='text/css'>");
            resultGraph.append("#chart-" + viewLetId + " .jqplot-grid-canvas {display: none;} #chart-" + viewLetId + ">.jqplot-yaxis {display: none;} #chart-" + viewLetId + ">.jqplot-xaxis {display: none;}");
            resultGraph.append("</style>");
            resultGraph.append("<div id='overlap_Parent' style='position:relative;'>");
            resultGraph.append("<div id='overlap_Child' style='position:relative;width:50px;height:20px;float:right;'><text style='font-family:verdana;font-size:10px'>" + legend + "</text></div>");
            resultGraph.append("<div id='chart-" + viewLetId + "' style='width:" + (width - 15) + "px; height:" + (height - 8) + "px;position:absolute;'  onclick=\"zoomTrend(" + measureId + ",'" + measureName + "'," + viewLetId + "," + viewLet.getOneviewId() + ")\"></div>");
            resultGraph.append("</div>");
        }

        resultGraph.append("<script>");
        resultGraph.append(jqGraph.getTrendGraph(pbretObjForTime, (ArrayList) measureIdVal, rowview, trendColor, null, null));
        resultGraph.append("</script>");

        return resultGraph.toString();
    }

    public String getGraphsOnReport(String reportId, OneViewLetDetails detail) {
        String query = getResourceBundle().getString("getGraphsOnReport");
        Object[] objArr = new Object[1];
        objArr[0] = reportId;
        String finalQuery = buildQuery(query, objArr);
        PbReturnObject retObj = null;
        StringBuilder graphIds = new StringBuilder();
        StringBuilder graphNames = new StringBuilder();
        StringBuilder graphCount = new StringBuilder();
        ArrayList graphIdList = new ArrayList();
        ArrayList graphNameList = new ArrayList();
        graphCount.append("{ GraphCount: [\"");
        graphIds.append("GraphIds: [");
        graphNames.append("GraphNames: [");
        try {
            retObj = execSelectSQL(finalQuery);
            if (retObj != null) {
                graphCount.append(retObj.rowCount).append("\"],");
                for (int i = 0; i < retObj.rowCount; i++) {
                    graphIds.append("\"").append(retObj.getFieldValueString(i, 2)).append("\"");
                    graphIdList.add(retObj.getFieldValueString(i, 2));
                    graphNames.append("\"").append(retObj.getFieldValueString(i, 3)).append("\"");
                    graphNameList.add(retObj.getFieldValueString(i, 3));
                    if (i != retObj.rowCount - 1) {
                        graphIds.append(",");
                        graphNames.append(",");
                    }
                }
                graphCount.append(graphIds).append("],").append(graphNames).append("] }");
                detail.setAssignedGraphIds(graphIdList);
                detail.setAssignedGraphNames(graphNameList);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return graphCount.toString();
    }

    public ArrayList getRelatedMeasures(String measId) {
        String qry = "select DEPENDENT_MEASURE from PRG_USER_ALL_INFO_DETAILS where ELEMENT_ID = " + measId;
        PbReturnObject retObj = null;
        PbReturnObject retObjname = null;
        ArrayList alist = new ArrayList();
        String result = "";
        StringBuilder kpiheadsbuilder = new StringBuilder();
        StringBuilder kpiSequence = new StringBuilder();
        StringBuilder kpiNumFormat = new StringBuilder();
        StringBuilder kpiRound = new StringBuilder();
        StringBuilder kpiFontColor = new StringBuilder();
        StringBuilder kpiSymbols = new StringBuilder();
        String[] measNames = null;
        String[] measIds = null;
        try {
            retObj = execSelectSQL(qry);
            if (retObj != null && retObj.getRowCount() > 0 && !retObj.getFieldValueString(0, "DEPENDENT_MEASURE").equalsIgnoreCase("")) {
                result = retObj.getFieldValueString(0, "DEPENDENT_MEASURE");
            }
            if (result != "") {
                measIds = result.split(",");
                if (measIds != null && measIds.length > 0) {
                    for (int i = 0; i < measIds.length; i++) {
                        String nameqry = "SELECT A.USER_COL_DESC,A.REF_ELEMENT_TYPE,B.NO_FORMAT,B.ROUND, B.FONTCOLOR, B.SYMBOLS from PRG_USER_ALL_INFO_DETAILS A,PRG_USER_SUB_FOLDER_ELEMENTS B where A.ELEMENT_ID = " + measIds[i] + " and A.ELEMENT_ID = B.ELEMENT_ID";
                        retObjname = execSelectSQL(nameqry);
                        if (retObjname != null) {
                            kpiheadsbuilder.append("," + retObjname.getFieldValueString(0, "USER_COL_DESC"));
                            kpiSequence.append("," + retObjname.getFieldValueString(0, "REF_ELEMENT_TYPE"));
                            kpiNumFormat.append("," + retObjname.getFieldValueString(0, "NO_FORMAT"));
                            kpiRound.append("," + retObjname.getFieldValueString(0, "ROUND"));
                            kpiFontColor.append("," + retObjname.getFieldValueString(0, "FONTCOLOR"));
                            kpiSymbols.append("," + retObjname.getFieldValueString(0, "SYMBOLS"));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (retObj != null && retObj.getRowCount() > 0 && !retObj.getFieldValueString(0, "DEPENDENT_MEASURE").equalsIgnoreCase("")) {
            alist.add(result);
            alist.add(kpiheadsbuilder.toString().substring(1));
            alist.add(kpiSequence.toString().substring(1));
            alist.add(kpiNumFormat.toString().substring(1));
            alist.add(kpiRound.toString().substring(1));
            alist.add(kpiFontColor.toString().substring(1));
            alist.add(kpiSymbols.toString().substring(1));
        }
        return alist;
    }

    public String prepareQueries(String dashboardId, int kpiMasterId, String kpiGroupString) {

        String result = "";
        if (kpiGroupString != null && !kpiGroupString.equalsIgnoreCase("") && !kpiGroupString.isEmpty()) {
            result = "{ call saveDashboardClob1(" + Integer.parseInt(dashboardId) + "," + kpiMasterId + ",'" + kpiGroupString + "',0) }";
        }
        return result;
    }

    public boolean procedureExecution(String procName) throws SQLException {
        boolean result = true;
        Connection conn = null;
        try { // by prabal
            ProgenConnection pconn = ProgenConnection.getInstance();
            conn = pconn.getConnection();
            CallableStatement proc = null;
            proc = conn.prepareCall(procName);
            result = proc.execute();
            proc.close();
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
        }

        return result;
    }
}
