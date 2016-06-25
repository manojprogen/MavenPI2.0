/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.map;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.query.RTDimensionElement;
import com.progen.query.RTMeasureElement;
import com.progen.query.RunTimeMeasure;
import com.progen.report.PbReportCollection;
import com.progen.report.SearchFilter;
import com.progen.report.bd.PbReportTableBD;
import com.progen.report.data.DataFacade;
import com.progen.report.data.RunTimeMeasCalculator;
import com.progen.report.display.util.NumberFormatter;
import com.progen.report.pbDashboardCollection;
import com.progen.report.query.DataSet;
import com.progen.report.query.DataSetHelper;
import com.progen.userlayer.db.UserLayerDAO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.ContainerConstants.SortOrder;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class MapBD extends PbDb {

    public static Logger logger = Logger.getLogger(MapBD.class);

    public boolean isMapEnabled(Container container, String column) {
        boolean mapEnabled = false;
        if (column.startsWith("A")) {
            column = column.substring(2);
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (!column.equalsIgnoreCase("TIME")) {
                String sql = "SELECT * from PRG_GEOGRAPHY_MAPS where ELEMENT_ID=" + column;

                conn = ProgenConnection.getInstance().getConnectionForElement(column);
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = stmt.executeQuery(sql);

                if (rs != null) {
                    rs.last();
                    if (rs.getRow() > 0) {
                        mapEnabled = true;
                    }
                }

                rs.close();
                stmt.close();
                conn.close();

                rs = null;
                stmt = null;
                conn = null;
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }
        return mapEnabled;
    }

    private GeoMapInfo createGeoMapObject(Container container, String reportType, String dbcolumn, String geoView) {
        PbReturnObject mapRetObj = null;
        if (container.getMapMainMeasure().isEmpty()) {
            String mapSQL = "select main_measure, supp_measures from prg_ar_report_map_details where report_id=" + container.getReportId();

            try {
                mapRetObj = execSelectSQL(mapSQL);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
            if (mapRetObj != null && mapRetObj.getRowCount() > 0) {
                String mainMeasure = mapRetObj.getFieldValueString(0, 0);
                String suppMeas = mapRetObj.getFieldValueString(0, 1);
                if (mainMeasure != null && !"".equalsIgnoreCase(mainMeasure)) {
                    List<String> tempList = new ArrayList<String>();
                    String[] temp = mainMeasure.split(",");
                    for (String str : temp) {
                        tempList.add("A_" + str);
                        str = "A_" + str;
                        container.setMapMainMeasure(str);
                        container.setMapMainMeasureLabel(container.getMeasureName(str));
                    }
                }


                if (suppMeas != null && !"".equalsIgnoreCase(suppMeas)) {
                    List<String> suppmeasLabel = new ArrayList<String>();
                    List<String> tempList = new ArrayList<String>();
                    String[] temp = suppMeas.split(",");
                    for (String str : temp) {
                        tempList.add("A_" + str);
                    }
                    container.setMapSupportingMeasures(tempList);
                    for (String suppmeas : tempList) {
                        suppmeasLabel.add(container.getMeasureName(suppmeas));

                    }
                    container.setMapSupportingMeasuresLabels(suppmeasLabel);
                }
            }
        }
        ArrayList<String> reportViewBys = new ArrayList<String>();
        ArrayList<String> reportViewBysName = new ArrayList<String>();

        if (container.getMapMainMeasure().isEmpty() || "".equals(container.getMapMainMeasure().get(0))) {
            if ("R".equalsIgnoreCase(reportType)) {
                container.setMapMainMeasure(container.getTableDisplayMeasures().get(0));
                container.setMapMainMeasureLabel((String) container.getTableMeasureNames().get(0));
            } else {
                container.setMapMainMeasure(container.getDisplayColumns().get(1));
                container.setMapMainMeasureLabel((String) container.getDisplayLabels().get(1));
            }
            container.resetMapSupportingMeasures();
        }


        List<String> mapMainMeasure = container.getMapMainMeasure();
        List<String> mapMainMeasureLabel = container.getMapMainMeasureLabel();
        List<String> mapSuppmeasure = container.getMapSupportingMeasures();
        List<String> mapsuppmeasurelabel = container.getMapSupportingMeasuresLabels();
        GeoMapInfo geomapinfo = new GeoMapInfo();
        geomapinfo.setPrimaryMeasure(mapMainMeasure, mapMainMeasureLabel);
        geomapinfo.addSupportingMeasure(mapSuppmeasure, mapsuppmeasurelabel);
        reportViewBys = container.getReportCollect().reportRowViewbyValues;
        for (String rowViewById : reportViewBys) {
            reportViewBysName.add(container.getReportCollect().getParameterDispName(rowViewById));
        }
        List<String> geoViewBys = container.getGeographyDimensionIds();
        if (geoViewBys.size() == 0) {
            return null;
        }
        ArrayList<String> mapViewBys = new ArrayList<String>();
        mapViewBys = getViewBysForMap(container, reportViewBys, geoViewBys, geoView);

        int count = 0;

        ArrayList<String> mapViewBysLabels = new ArrayList<String>();
        for (String viewbyId : mapViewBys) {
            mapViewBysLabels.add(container.getReportCollect().getParameterDispName(viewbyId));
        }
        for (int i = 0; i < mapViewBys.size(); i++) {
            String mapViewBy = mapViewBys.get(i);
            if (!"TIME".equalsIgnoreCase(mapViewBy)) {
                mapViewBy = "A_" + mapViewBy;
            }
            if (count == 0) {
                geomapinfo.addPrimaryDimension(mapViewBy, mapViewBysLabels.get(i));
            } else {
                geomapinfo.addSupportingDimension(mapViewBy, mapViewBysLabels.get(i));
            }
            count++;

        }

        return geomapinfo;
    }

    private ArrayList<String> getViewBysForMap(Container container, ArrayList<String> rowViewBys, List<String> geoViewBys, String geoView) {
        ArrayList<String> viewByIds = new ArrayList<String>();
        ArrayList<String> geographyDimensionids = (ArrayList<String>) container.getGeographyDimensionIds();


        for (int i = 0; i < rowViewBys.size(); i++) {
            if (geographyDimensionids.contains(rowViewBys.get(i))) {
                viewByIds.add(rowViewBys.get(i));
            }

        }
//
//        for (String dimId : rowViewBys) {
//            if (this.isMapEnabled(container, dimId)) {
//                viewByIds.add(dimId);
//            }
//        }
        if (viewByIds.size() > 0) {

            for (int i = 0; i < rowViewBys.size(); i++) {
                if (!viewByIds.contains(rowViewBys.get(i))) {
                    viewByIds.add(rowViewBys.get(i));
                }
            }

        } else {
            viewByIds.add(geoViewBys.get(0));
            viewByIds.addAll(rowViewBys);
        }
        if (!container.getGeographyDimensionIds().isEmpty()) {
            if (this.isGeoDimSelectionEnabled(container)) {
                if (!("null".equalsIgnoreCase(geoView) || geoView == null)) {
                    viewByIds.remove(0);
                    viewByIds.add(0, geoView);

                }
            }
        }

        return viewByIds;
    }
    //this method query from prg_geography_maps and give gepgraphy enabled dimensionids

    public ArrayList<String> getGeographyDimensionIds(Container container) {
        ArrayList<String> dispCols = container.getDisplayColumns();
        String dbcolumn = null;
        String temp = null;

        if (container.isReportCrosstab()) {
            PbReportCollection collect = container.getReportCollect();
            for (int i = 0; i < collect.reportRowViewbyValues.size(); i++) {
                temp = collect.reportRowViewbyValues.get(i);
                if (!"TIME".equalsIgnoreCase(temp) && !RTMeasureElement.isRunTimeMeasure(temp) && !RTDimensionElement.isRunTimeDimension(temp)) {
                    dbcolumn = temp;
                    break;
                }
            }

            if (dbcolumn == null) {
                for (int i = 0; i < collect.reportColViewbyValues.size(); i++) {
                    temp = collect.reportColViewbyValues.get(i);
                    if (!"TIME".equalsIgnoreCase(temp) && !RTMeasureElement.isRunTimeMeasure(temp) && !RTDimensionElement.isRunTimeDimension(temp)) {
                        dbcolumn = temp;
                        break;
                    }
                }
            }
        } else {
            if (dispCols != null && !dispCols.isEmpty()) {
                for (int i = 0; i < dispCols.size(); i++) {
                    temp = dispCols.get(i);
                    if (!"TIME".equalsIgnoreCase(temp) && !RTMeasureElement.isRunTimeMeasure(temp) && !RTDimensionElement.isRunTimeDimension(temp)) {
                        dbcolumn = temp;
                        break;
                    }
                }
            }
        }

        if (dbcolumn != null && !"".equals(dbcolumn)) {
            dbcolumn = dbcolumn.replace("A_", "");
        }

        String sql = "select distinct element_id from prg_geography_maps";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
//        String elementid = "";
        StringBuilder elementid = new StringBuilder(400);
        ArrayList<String> viewbyarray = new ArrayList<String>();
//        container.initializeGeographyDimensionIds();
        try {
            conn = ProgenConnection.getInstance().getConnectionForElement(dbcolumn);//ByConId(String.valueOf(container.getReportCollect().connectionId));
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql.toString());

            while (rs.next()) {
                elementid.append(",").append(String.valueOf(rs.getInt("element_id")));
//                elementid = elementid + "," + String.valueOf(rs.getInt("element_id"));
            }
            if (elementid != null && elementid.length() > 0) {
//                elementid = elementid.substring(1);
                elementid = new StringBuilder(elementid.substring(1));
                String viewbys[] = elementid.toString().split(",");

                viewbyarray.addAll(Arrays.asList(viewbys));
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }

//        HashMap<String,String> reptParams = container.getReportCollect().getReportParameters();

        if (viewbyarray.size() > 0) {
            HashMap paramHashMap = container.getParametersHashMap();
            ArrayList parameters = (ArrayList) paramHashMap.get("Parameters");
            for (int i = 0; i < viewbyarray.size(); i++) {
                if (container.getReportCollect().getReportParameters().get(viewbyarray.get(i)) != null
                        || parameters.contains(viewbyarray.get(i))) {
                    container.addGeographyDimensionIds(viewbyarray.get(i));
                }
            }
        }
        return viewbyarray;
    }

    public boolean isTopBottomEnabled(Container container) {
        ArrayList<String> mapViewBys = new ArrayList<String>();
        boolean enabletopbottomSelection = false;
        ArrayList<String> geographyDimensionids = (ArrayList<String>) container.getGeographyDimensionIds();
        ArrayList<String> rowViewByids = container.getReportCollect().reportRowViewbyValues;
        for (int i = 0; i < rowViewByids.size(); i++) {
            if (geographyDimensionids.contains(rowViewByids.get(i))) {
                mapViewBys.add(rowViewByids.get(i));
            }

        }

        if (mapViewBys.size() > 0) {
            if (mapViewBys.size() == rowViewByids.size()) {
                enabletopbottomSelection = false;
            } else if (rowViewByids.size() - mapViewBys.size() > 1) {
                enabletopbottomSelection = false;
            } else if (rowViewByids.size() - mapViewBys.size() == 1) {
                enabletopbottomSelection = true;
            }
        } else {
            if (rowViewByids.size() == 1) {
                enabletopbottomSelection = true;
            }
        }
        return enabletopbottomSelection;

    }

    public boolean isGeoDimSelectionEnabled(Container container) {
        ArrayList<String> mapViewBys = new ArrayList<String>();
        boolean enableViewSelection = false;
        ArrayList<String> geographyDimensionids = (ArrayList<String>) container.getGeographyDimensionIds();
        ArrayList<String> rowViewByids = container.getReportCollect().reportRowViewbyValues;
        for (int i = 0; i < rowViewByids.size(); i++) {
            if (geographyDimensionids.contains(rowViewByids.get(i))) {
                mapViewBys.add(rowViewByids.get(i));
            }

        }
        if (mapViewBys.size() > 0) {
            enableViewSelection = false;
        } else {
            if (rowViewByids.size() == 1) {
                enableViewSelection = true;
            }
        }

        return enableViewSelection;
    }

    public PbReturnObject getMapReturnObject(Container container, String userid, String reportType, GeoMapInfo geoMapInfo, String mapView, String sortStyle, int topbotmval) {

        String column = container.getDisplayColumns().get(0);
        boolean mapenable = false;
        if (container.getGeographyDimensionIds().contains(column)) {
            mapenable = true;
        }

        PbReturnObject retObj = null;
        if (mapenable) {
            retObj = (PbReturnObject) container.getRetObj();
        } else {
            LinkedHashMap<String, String> reportParams = new LinkedHashMap<String, String>();
            PbReportCollection reportCollect = (PbReportCollection) container.getReportCollect().clone();
            ArrayList<String> rowViewbyVal = new ArrayList<String>();
            ArrayList<String> measIds = new ArrayList<String>();
            ArrayList<String> colViewBys = new ArrayList<String>();
            ArrayList timeDetails = reportCollect.timeDetailsArray;
            String[] bizroles = reportCollect.reportBizRoles;

            // measIds.add(geoMapInfo.getPrimaryMeasure().substring(2));
            for (String primaryMeas : geoMapInfo.getPrimaryMeasure()) {
                measIds.add(primaryMeas.substring(2));
            }
            for (String suppMeasure : geoMapInfo.getSupportingMeasures()) {
                suppMeasure = suppMeasure.substring(2);
                measIds.add(suppMeasure);
            }

            if (geoMapInfo.getSupportingDimensions().size() == 1 && mapView.equalsIgnoreCase(MapConstants.DISPLAY_OVERALL_VALUES)) {

                reportParams = getToprows(container, geoMapInfo, sortStyle, topbotmval);

                if (reportParams != null) {
                    Map<String, String> collectRepParams = reportCollect.reportParametersValues;
                    Set<String> keySet = collectRepParams.keySet();
                    Iterator<String> iter = keySet.iterator();

                    while (iter.hasNext()) {
                        String key = iter.next();
                        String val = collectRepParams.get(key);

                        if (!reportParams.containsKey(key)) {
                            reportParams.put(key, val);
                        }
                    }
                }

                for (String suppDim : geoMapInfo.getSupportingDimensions()) {
                    if (!"TIME".equalsIgnoreCase(suppDim)) {
                        suppDim = suppDim.substring(2);
                    }
                    rowViewbyVal.add(suppDim);
                }
                rowViewbyVal.add(geoMapInfo.getPrimaryDimension().substring(2));
            } else {
                rowViewbyVal.add(geoMapInfo.getPrimaryDimension().substring(2));
                for (String suppDim : geoMapInfo.getSupportingDimensions()) {
                    if (!"TIME".equalsIgnoreCase(suppDim)) {
                        suppDim = suppDim.substring(2);
                    }
                    rowViewbyVal.add(suppDim);
                }
            }

            if (reportParams == null || reportParams.size() == 0) {
                reportParams = reportCollect.reportParametersValues;
            }
            DataSetHelper helper = new DataSetHelper.DataSetHelperBuilder().measIds(measIds).rowViewBys(rowViewbyVal).colViewBys(colViewBys).paramValues(reportParams).timeDetails(timeDetails).userId(userid).bizRole(bizroles).build();
            DataSet dataSet = helper.getDataSet();
            retObj = dataSet.getData();

        }
        return retObj;
    }
    private HashMap<String, String> mapcolors = new HashMap<String, String>();
    private HashMap<String, String> mapMarkerSizes = new HashMap<String, String>();

    public LinkedHashMap<String, String> getToprows(Container container, GeoMapInfo geoMapInfo, String sortStyle, int topbtmVal) {
        LinkedHashMap<String, String> reportParams = new LinkedHashMap<String, String>();
        StringBuilder topValuesSB = new StringBuilder();
        SearchFilter filter = new SearchFilter();
        ArrayList<Integer> intvals = new ArrayList<Integer>();
        PbReportTableBD bd = new PbReportTableBD();
        String suppDimId = geoMapInfo.getSupportingDimensions().get(0);

        filter.add(geoMapInfo.getPrimaryMeasure().get(0), sortStyle, topbtmVal);
        intvals = bd.searchDataSet(container.getRetObj(), filter);
        String color = "";

        int rowCount = intvals.size();
        ArrayList<String> topValues = new ArrayList<String>();
        ArrayList<String> topva = new ArrayList<String>();
        for (int i = 0; i < rowCount; i++) {
            int actualRow = intvals.get(i);
            topValues.add(container.getRetObj().getFieldValueString(actualRow, suppDimId));
            topva.add(container.getRetObj().getFieldValueString(actualRow, suppDimId));
        }
        ArrayList<String> colorsdata = new ArrayList<String>();
        ArrayList<String> markersdata = new ArrayList<String>();
        colorsdata.add("blue");
        colorsdata.add("gray");
        colorsdata.add("lightpink");
        colorsdata.add("lilac");
        colorsdata.add("lavender");
        markersdata.add("large");
        markersdata.add("medium");
        markersdata.add("normal");

        for (int i = 0; i < topValues.size(); i++) {
            topValuesSB.append(",").append(topValues.get(i));
            if (sortStyle.equalsIgnoreCase("Top")) {
                if (topbtmVal == 3) {
                    mapcolors.put(topValues.get(i), colorsdata.get(i));
                    mapMarkerSizes.put(topValues.get(i), markersdata.get(i));
                } else {
                    mapcolors.put(topValues.get(i), colorsdata.get(i));
                    if (i == 0) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(0));
                    }
                    if (i == 1 || i == 2) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(1));
                    }
                    if (i == 3 || i == 4) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(2));
                    }
                }
            } else {
                if (topbtmVal == 3) {
                    mapcolors.put(topValues.get(i), colorsdata.get((colorsdata.size() - 1) - i));
                    mapMarkerSizes.put(topValues.get(i), markersdata.get((markersdata.size() - 1) - i));
                } else {
                    mapcolors.put(topValues.get(i), colorsdata.get((colorsdata.size() - 1) - i));
                    if (i == 0 || i == 1) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(2));
                    }
                    if (i == 2 || i == 3) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(1));
                    }
                    if (i == 4) {
                        mapMarkerSizes.put(topValues.get(i), markersdata.get(0));
                    }
                }
            }
        }
        topValuesSB.replace(0, 1, "");
        if (!"TIME".equalsIgnoreCase(suppDimId)) {
            reportParams.put(suppDimId.substring(2), topValuesSB.toString());
        } else {
            reportParams.put(suppDimId, topValuesSB.toString());
        }
        return reportParams;
    }

    private List<String> getMapMarkerSizes(char sortType, int topBottomVal) {
        List<String> markerSizes = new ArrayList<String>();

        if (topBottomVal == 3) {
            markerSizes.add("large");
            markerSizes.add("medium");
            markerSizes.add("normal");
        } else {
            markerSizes.add("large");
            markerSizes.add("medium");
            markerSizes.add("medium");
            markerSizes.add("normal");
            markerSizes.add("normal");
        }

        return markerSizes;
    }

    private List<String> getMapDisplayColors(char sortType, int topBottomVal) {
        List<String> mapColors = new ArrayList<String>();

        //Top
        if (sortType == '1') {
            mapColors.add("blue");
            mapColors.add("gray");
            mapColors.add("lightpink");
            if (topBottomVal == 5) {
                mapColors.add("lilac");
                mapColors.add("lavender");
            }
        } else {//Bottom
            mapColors.add("lavender");
            mapColors.add("lilac");
            mapColors.add("lightpink");
            if (topBottomVal == 5) {
                mapColors.add("gray");
                mapColors.add("blue");
            }
        }
        return mapColors;
    }

    private MapContent getOverallViewMapContents(GeoMapInfo mapInfo, PbReturnObject retObj, char sortType, int topBottomVal) {
        MapContent mapContent = new MapContent();

        ArrayListMultimap<String, List<String>> measMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> colorMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> markerMap = ArrayListMultimap.create();
        List<String> suppMeasIds = mapInfo.getSupportingMeasures();
        Set<String> suppDimValSet = new LinkedHashSet<String>();
        for (int i = 0; i < retObj.getRowCount(); i++) {
            suppDimValSet.add(retObj.getFieldValueString(i, 0));
        }
        List<String> mapDispColors = getMapDisplayColors(sortType, topBottomVal);
        //For each value in the set, filter the return object.
        //Get the values and create the marker and color details
        Iterator<String> iter = suppDimValSet.iterator();
        String geoDimId = mapInfo.getPrimaryDimension();
        String suppDimId = mapInfo.getSupportingDimensions().get(0);
        List<String> measId = mapInfo.getPrimaryMeasure();
        PbReportTableBD bd = new PbReportTableBD();
        int count = 0;
        while (iter.hasNext()) {
            String dimVal = iter.next();
            String color = mapDispColors.get(count++);
            SearchFilter filter = new SearchFilter();
            filter.add(suppDimId, "EQ", dimVal);
            retObj.resetViewSequence();
            ArrayList<Integer> viewSeq = bd.searchDataSet(retObj, filter);
            retObj.setViewSequence(viewSeq);

            Container container = new Container();
            container.setRetObj(retObj);

            DataFacade df = new DataFacade(container);
            RunTimeMeasCalculator rtCalc = new RunTimeMeasCalculator(df);
            RunTimeMeasure rtMeas = rtCalc.calculatePercentWiseForSubtotal(measId.get(0), suppDimId);

            BigDecimal max = rtMeas.getMaximum();
            BigDecimal min = rtMeas.getMinimum();
            double maxValue = (max == null) ? 0 : max.doubleValue();
            double minValue = (min == null) ? 0 : min.doubleValue();
            double range = maxValue - minValue;

            double firstSplit = maxValue - 2 * range / 3;
            double secondSplit = maxValue - range / 3;

            for (int i = 0; i < viewSeq.size(); i++) {
                int row = viewSeq.get(i);
                String location = retObj.getFieldValueString(row, geoDimId);
                BigDecimal primMeasVal = retObj.getFieldValueBigDecimal(row, measId.get(0));
                if (primMeasVal != null) {
                    double measContribution = rtMeas.getData(row) != null ? rtMeas.getData(row).doubleValue() : 0;
                    String markerSize = "large";
                    if (measContribution >= minValue && measContribution <= firstSplit) {
                        markerSize = "small";
                    } else if (measContribution > firstSplit && measContribution <= secondSplit) {
                        markerSize = "medium";
                    }

                    colorMap.put(location, color);
                    markerMap.put(location, markerSize);

                    List<String> tempList = new ArrayList<String>();
                    tempList.add(dimVal);
                    tempList.add(NumberFormatter.getModifiedNumber(primMeasVal, "", -1));

                    for (String suppMeas : suppMeasIds) {
                        BigDecimal val = retObj.getFieldValueBigDecimal(row, suppMeas);
                        tempList.add(NumberFormatter.getModifiedNumber(val, "", -1));
                    }
                    measMap.put(location, tempList);
                }
            }
        }

        mapContent.setMarkerMap(markerMap);
        mapContent.setColorMap(colorMap);
        mapContent.setMeasMap(measMap);

        return mapContent;
    }

    private MapContent getLocationWiseViewMapContents(GeoMapInfo mapInfo, PbReturnObject retObj, char sortType, int topBottomVal) {
        MapContent mapContent = new MapContent();

        ArrayListMultimap<String, List<String>> measMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> colorMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> markerMap = ArrayListMultimap.create();

        String sortStyle = "";
        if ('1' == sortType) {
            sortStyle = "TOP";
        } else {
            sortStyle = "BTM";
        }

        List<String> mapDispColors = getMapDisplayColors(sortType, topBottomVal);
        List<String> mapMarkerSizes = getMapMarkerSizes(sortType, topBottomVal);
        SearchFilter filter = new SearchFilter();
        filter.add(mapInfo.getPrimaryMeasure().get(0), sortStyle, topBottomVal);

        if ("Top".equalsIgnoreCase(sortStyle)) {
            filter.addSortColumnsForTopBtmFilter(mapInfo.getPrimaryMeasure().get(0), mapInfo.getPrimaryDimension(), SortOrder.DESCENDING);
        } else {
            filter.addSortColumnsForTopBtmFilter(mapInfo.getPrimaryMeasure().get(0), mapInfo.getPrimaryDimension(), SortOrder.ASCENDING);
        }

        PbReportTableBD bd = new PbReportTableBD();
        ArrayList<Integer> viewSequence = bd.searchDataSet(retObj, filter);
        List<String> suppMeasIds = mapInfo.getSupportingMeasures();
        String primDimId = mapInfo.getPrimaryDimension();
        List<String> suppDimIds = mapInfo.getSupportingDimensions();

        String oldDimValue = "";
        int colorIndex = 0;
        for (int i = 0; i < viewSequence.size(); i++) {
            int row = viewSequence.get(i);
            String dimValue = retObj.getFieldValueString(row, primDimId);
            if (!(dimValue.equalsIgnoreCase(oldDimValue))) {
                colorIndex = 0;
                oldDimValue = dimValue;
            }

            List<String> tempList = new ArrayList<String>();
            for (String suppDim : suppDimIds) {
                tempList.add(retObj.getFieldValueString(row, suppDim));
            }
            BigDecimal primMeasVal = retObj.getFieldValueBigDecimal(row, mapInfo.getPrimaryMeasure().get(0));
            tempList.add(NumberFormatter.getModifiedNumber(primMeasVal, "", -1));

            for (String suppMeas : suppMeasIds) {
                BigDecimal val = retObj.getFieldValueBigDecimal(row, suppMeas);
                tempList.add(NumberFormatter.getModifiedNumber(val, "", -1));
            }

            measMap.put(dimValue, tempList);
            colorMap.put(dimValue, mapDispColors.get(colorIndex));
            markerMap.put(dimValue, mapMarkerSizes.get(colorIndex));
            colorIndex++;
        }

        mapContent.setMarkerMap(markerMap);
        mapContent.setMeasMap(measMap);
        mapContent.setColorMap(colorMap);
        return mapContent;
    }

    private MapContent getSingleDimMapContents(GeoMapInfo mapInfo, PbReturnObject retObj, Container container) {
        MapContent mapContent = new MapContent();

        ArrayListMultimap<String, List<String>> measMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> colorMap = ArrayListMultimap.create();
        ArrayListMultimap<String, String> markerMap = ArrayListMultimap.create();

        List<String> primaryMeasId = mapInfo.getPrimaryMeasure();
        List<String> suppMeasIds = mapInfo.getSupportingMeasures();
        String dimId = mapInfo.getPrimaryDimension();

        double max = 0;
        double min = 0;
        double range = 0;
        double positiveRange = 0;
        double negativeRange = 0;
        for (int j = 0; j < primaryMeasId.size(); j++) {
            String primaryMeasIdStr = primaryMeasId.get(j);
            max = retObj.getMaximumValueInColumn(primaryMeasIdStr);
            min = retObj.getMinimumValueInColumn(primaryMeasIdStr);

            range = max - min;
            positiveRange = max;
            negativeRange = min;

            String measElement_id = primaryMeasIdStr.substring(2);
            UserLayerDAO uld = new UserLayerDAO();
            boolean changePercentMeasure = uld.isMeasureChangePercentSelected(measElement_id);
            if (min < 0) {
                changePercentMeasure = true;
            }

            for (int i = 0; i < retObj.getRowCount(); i++) {
                String dimValue = retObj.getFieldValueString(i, dimId);
                DataFacade datafacade = new DataFacade(container);
                BigDecimal primMeasVal = retObj.getFieldValueBigDecimal(i, primaryMeasIdStr);
                if (primMeasVal != null) {
                    String colorStr = datafacade.getColor(i, primaryMeasIdStr, null);
                    colorStr = colorStr.replace("#", "");
                    colorStr = colorStr.trim();
                    double measVal = primMeasVal.doubleValue();
                    //Set the measure details
                    List<String> measVals = new ArrayList<String>();
                    measVals.add(NumberFormatter.getModifiedNumber(measVal, "", -1));
                    if (suppMeasIds != null) {
                        for (String suppMeas : suppMeasIds) {
                            BigDecimal val = retObj.getFieldValueBigDecimal(i, suppMeas);
                            measVals.add(NumberFormatter.getModifiedNumber(val, "", -1));
                        }
                    }

                    measMap.put(dimValue, measVals);

                    String color = "";
                    String markerSize = "";
                    //Set the color and marker details
                    if (changePercentMeasure && primaryMeasId.size() == 1) {
                        if (measVal == 0) {
                            color = "orange";
                            markerSize = "small";
                        } else if (measVal > 0) {
                            color = "green";
                            if (measVal > 0 && measVal <= (positiveRange / 3)) {
                                markerSize = "small";
                            } else if (measVal > (positiveRange / 3) && measVal <= (2 * positiveRange / 3)) {
                                markerSize = "medium";
                            } else {
                                markerSize = "large";
                            }
                        } else {
                            color = "red";
                            if (measVal < 0 && measVal >= (negativeRange / 3)) {
                                markerSize = "small";
                            } else if (measVal < (negativeRange / 3) && measVal >= (2 * negativeRange / 3)) {
                                markerSize = "medium";
                            } else {
                                markerSize = "large";
                            }
                        }
                    } else {
                        //Apply the default Color as blue
                        if (colorStr == "") {
                            color = "blue";
                        } else {
                            color = colorStr.toLowerCase();
                        }
                        if (j == 1) {
                            if (colorStr == "") {
                                color = "gray";
                            } else {
                                color = colorStr.toLowerCase();
                            }
                        }
                        if (measVal >= min && measVal <= (min + range / 3)) {
                            markerSize = "small";
                        } else if (measVal > (min + range / 3) && measVal <= (min + 2 * range / 3)) {
                            markerSize = "medium";
                        } else {
                            markerSize = "large";
                        }
                    }
                    markerMap.put(dimValue, markerSize);
                    colorMap.put(dimValue, color);
                }
            }

            mapContent.setMarkerMap(markerMap);
            mapContent.setColorMap(colorMap);
            mapContent.setMeasMap(measMap);
        }

        return mapContent;
    }

    public String getMapContents(Container container, String column, String reportType, String userid, char sortType, int topBottomValue, String mapView, String geoView) throws Exception {

        DataFacade df = new DataFacade(container);
        String dbColumn = column.substring(2);

        ArrayListMultimap<String, List<String>> measMap = ArrayListMultimap.create();
        //  ArrayListMultimap<String, List<String>> measMapMarkersinfo = ArrayListMultimap.create();

        ArrayListMultimap<String, List<String>> measColorMap = ArrayListMultimap.create();
        ArrayListMultimap<String, List<String>> measMarkerSize = ArrayListMultimap.create();

        StringBuilder sql = new StringBuilder();
        StringBuilder jsonOutput = new StringBuilder();
        List<String> measIdList = new ArrayList<String>();
        List<String> measLabelList = new ArrayList<String>();
        String primaryDimName = "";

        int rowcount;
        List<String> colors = new ArrayList();
        List<String> markerSizes = new ArrayList<String>();
        markerSizes.add("large");
        markerSizes.add("medium");
        markerSizes.add("normal");
        colors.add("blue");
        colors.add("gray");
        colors.add("lightpink");
        colors.add("lilac");
        colors.add("lavender");
        StringBuilder json = null;

        GeoMapInfo geomapinfo = this.createGeoMapObject(container, reportType, dbColumn, geoView);
        if (geomapinfo != null) {
            String primaryDimId = geomapinfo.getPrimaryDimension().substring(2);

            if (geomapinfo.getSupportingDimensions().size() == 1 || geomapinfo.getSupportingDimensions().isEmpty()) {

                PbReturnObject retObj = new PbReturnObject();
                String sortStyle = "";
                if ('1' == sortType) {
                    sortStyle = "TOP";
                } else {
                    sortStyle = "BTM";
                }
                retObj = this.getMapReturnObject(container, userid, reportType, geomapinfo, mapView, sortStyle, topBottomValue);

                MapContent mapContent = null;

                if (geomapinfo.getSupportingDimensions().size() == 1) {
                    if (mapView.equalsIgnoreCase(MapConstants.DISPLAY_OVERALL_VALUES)) {
                        mapContent = getOverallViewMapContents(geomapinfo, retObj, sortType, topBottomValue);
                    } else if (mapView.equalsIgnoreCase(MapConstants.DISPLAY_LOCATION_WISE_VALUES)) {
                        mapContent = getLocationWiseViewMapContents(geomapinfo, retObj, sortType, topBottomValue);
                    }
                } else {
                    mapContent = getSingleDimMapContents(geomapinfo, retObj, container);
                }


                //Create the basic json string for all the map types.
                HashMap<String, String> drillMap = new HashMap<String, String>();
                drillMap = container.getReportCollect().getDrillMap();

                boolean drillAvailable = false;
                if (drillMap != null && drillMap.containsKey(primaryDimId)) {
                    String drillVal = drillMap.get(primaryDimId);
                    if (drillVal != null && !(drillVal.equalsIgnoreCase(primaryDimId))) {
//                        boolean mapEnabledForChild =false;
                        if (container.getGeographyDimensionIds().contains(drillVal)) {
                            drillAvailable = true;
                        }
                    }
                }

                ArrayList<String> dimNameArr = new ArrayList<String>();
                ArrayList<String> dimIdArr = new ArrayList<String>();
                dimNameArr.add(geomapinfo.getPrimaryDimensionLabel());
                dimIdArr.add(geomapinfo.getPrimaryDimension().substring(2));
                List<String> suppDimIdList = geomapinfo.getSupportingDimensions();
                if (suppDimIdList.size() > 0) {
                    for (String suppDimId : suppDimIdList) {
                        dimNameArr.add(geomapinfo.getSupportingDimensionLabel(suppDimId));
                        if (suppDimId.startsWith("A_")) {
                            suppDimId = suppDimId.substring(2);
                        }
                        dimIdArr.add(suppDimId);
                    }
                }

                StringBuilder dimNameSB = new StringBuilder();
                StringBuilder dimIdSB = new StringBuilder();

                for (int i = 0; i < dimNameArr.size(); i++) {
                    dimNameSB.append("," + "\"").append(dimNameArr.get(i)).append("\"");
                    dimIdSB.append("," + "\"").append(dimIdArr.get(i)).append("\"");
                }

                dimNameSB.replace(0, 1, "");
                dimIdSB.replace(0, 1, "");
                json = new StringBuilder();
                json.append("{");
                json.append("\"DIMNAME\":[" + dimNameSB.toString() + "]" + ",");
                json.append("\"DimId\":[" + dimIdSB.toString() + "]" + ",");
                json.append("\"DrillAvailable\":\"").append(drillAvailable).append("\",");
                List<String> suppMeasures = geomapinfo.getSupportingMeasures();

                if (geomapinfo.getSupportingDimensions().size() == 1) {
                    measIdList.add(geomapinfo.getPrimaryMeasure().get(0));
                    measLabelList.add(geomapinfo.getPrimaryMeasureLabel().get(0));
                } else {
                    measIdList.addAll(geomapinfo.getPrimaryMeasure());
                    measLabelList.addAll(geomapinfo.getPrimaryMeasureLabel());
                }

                if (suppMeasures != null) {
                    for (String suppMeas : suppMeasures) {
                        measIdList.add(suppMeas);
                        measLabelList.add(geomapinfo.getSupportingMeasureLabel(suppMeas));
                    }
                }

                StringBuilder measIdSB = new StringBuilder();
                StringBuilder measLblSB = new StringBuilder();
                for (int i = 0; i < measIdList.size(); i++) {
                    measIdSB.append("," + "\"" + measIdList.get(i) + "\"");
                }
                measIdSB.replace(0, 1, "");
                for (int i = 0; i < measLabelList.size(); i++) {
                    measLblSB.append("," + "\"" + measLabelList.get(i) + "\"");
                }
                measLblSB.replace(0, 1, "");

                json.append("\"MeasureIds\":[" + measIdSB.toString() + "]");
                json.append(",");
                json.append("\"MeasuresName\":[" + measLblSB.toString() + "]");

                StringBuilder suppDimNameSb = new StringBuilder();
                StringBuilder suppDimIdSb = new StringBuilder();

                if (suppDimIdList.size() >= 1) {
                    for (int i = 0; i < suppDimIdList.size(); i++) {
                        suppDimNameSb.append("," + "\"").append(geomapinfo.getSupportingDimensionLabel(suppDimIdList.get(i))).append("\"");
                        suppDimIdSb.append("," + "\"").append(suppDimIdList.get(i)).append("\"");
                    }
                    suppDimNameSb.replace(0, 1, "");
                    suppDimIdSb.replace(0, 1, "");

                    json.append("," + "\"SuppDimName\":[").append(suppDimNameSb).append("]");
                    json.append("," + "\"SuppDimId\":[").append(suppDimIdSb).append("]");
                } else {
                    json.append("," + "\"SuppDimName\":[" + "" + "]");
                    json.append("," + "\"SuppDimId\":[" + "" + "]");
                }

                if (mapContent != null) {
                    json.append(",");
                    json.append(getMapContentJSON(dbColumn, mapContent, geomapinfo.getPrimaryDimension()));
                }

                if (geomapinfo.getSupportingDimensions().size() == 1) {
                    json.append(",").append("\"MainMeasLength_GT_ONE\":\"").append(geomapinfo.getPrimaryMeasure().size()).append("\"");
                } else {
                    json.append(",").append("\"MainMeasLength_GT_ONE\":\"").append(geomapinfo.getPrimaryMeasure().size()).append("\"");
                }
                json.append("}");
            } else {
                return "dimensionerror";
            }
        } else {
            return "error";
        }
        return json.toString();
    }

    private String getMapContentJSON(String dbColumn, MapContent mapContent, String primaryDimension) {
        StringBuilder jsonOutput = new StringBuilder();
        StringBuilder sql = new StringBuilder();
        primaryDimension = primaryDimension.replace("A_", "");
        ArrayListMultimap<String, List<String>> measMap = mapContent.getMeasMap();
        ArrayListMultimap<String, String> markerMap = mapContent.getMarkerMap();
        ArrayListMultimap<String, String> colorMap = mapContent.getColorMap();
        Set<String> dimValueSet = measMap.keySet();
        sql.append("select area, map_value,latitude,longitude from PRG_GEOGRAPHY_MAPS where area in ('" + Joiner.on("','").join(dimValueSet) + "')");



//        Iterator<String> iter = dimValueSet.iterator();
//        while (iter.hasNext()) {
//            String dimValue = iter.next();
//            sql.append("'").append(dimValue).append("',");
//        }
//
//        sql.replace(sql.length() - 1, sql.length(), ")");
        sql.append(" and element_id=").append(primaryDimension);
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        jsonOutput.append("\"Measures\":[");

        StringBuilder citySB = new StringBuilder();
        StringBuilder cityCodeSB = new StringBuilder();
        StringBuilder latitudesSB = new StringBuilder();
        StringBuilder longitudesSB = new StringBuilder();

        StringBuilder measureSB = new StringBuilder();
        StringBuilder colorCodeSB = new StringBuilder();
        StringBuilder markerSizeSB = new StringBuilder();

        try {
            conn = ProgenConnection.getInstance().getConnectionForElement(dbColumn);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql.toString());

            while (rs.next()) {
                String areaCode = rs.getString("area");
                String areaVal = rs.getString("map_value");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                List<List<String>> measureValues = measMap.get(areaCode);
                List<String> markerSizes = markerMap.get(areaCode);
                List<String> colors = colorMap.get(areaCode);

                measureSB.append("[");
                colorCodeSB.append("[");
                markerSizeSB.append("[");
                for (int i = 0; i < measureValues.size(); i++) {
                    List<String> areaMeasures = measureValues.get(i);
                    measureSB.append("[\"" + Joiner.on("\",\"").join(areaMeasures) + "\"],");
                    markerSizeSB.append("[");
                    colorCodeSB.append("[");
//                    for (int j=0;j<areaMeasures.size();j++){
//                        measureSB.append("\"").append(areaMeasures.get(j)).append("\",");
//                    }
//                    measureSB.replace(measureSB.length() -1, measureSB.length(), "");
//
//                    measureSB.append("],");

                    markerSizeSB.append("\"" + markerSizes.get(i) + "\"");
                    colorCodeSB.append("\"" + colors.get(i) + "\"");
                    markerSizeSB.append("],");
                    colorCodeSB.append("],");
                }
                measureSB.replace(measureSB.length() - 1, measureSB.length(), "");
                markerSizeSB.replace(markerSizeSB.length() - 1, markerSizeSB.length(), "");
                colorCodeSB.replace(colorCodeSB.length() - 1, colorCodeSB.length(), "");

                measureSB.append("],");
                markerSizeSB.append("],");
                colorCodeSB.append("],");

                citySB.append(",\"" + areaVal + "\"");
                cityCodeSB.append(",\"" + areaCode + "\"");
                latitudesSB.append(",\"" + latitude + "\"");
                longitudesSB.append(",\"" + longitude + "\"");
            }
            measureSB.replace(measureSB.length() - 1, measureSB.length(), "");
            markerSizeSB.replace(markerSizeSB.length() - 1, markerSizeSB.length(), "");
            colorCodeSB.replace(colorCodeSB.length() - 1, colorCodeSB.length(), "");

            jsonOutput.append(measureSB);
            jsonOutput.append("]").append(",");
            jsonOutput.append("\"ColorCode\"").append(":");
            jsonOutput.append("[").append(colorCodeSB.toString()).append("]").append(",");
            jsonOutput.append("\"MarkerSize\"").append(":");
            jsonOutput.append("[").append(markerSizeSB.toString()).append("]").append(",");

            citySB.replace(0, 1, "");
            cityCodeSB.replace(0, 1, "");
            latitudesSB.replace(0, 1, "");
            longitudesSB.replace(0, 1, "");

            rs.close();
            stmt.close();
            conn.close();

            rs = null;
            stmt = null;
            conn = null;

        } catch (Exception e) {
            logger.error("Exception:", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }

        //Adding the City Code values
        jsonOutput.append("\"CityCode\":[");
        jsonOutput.append(cityCodeSB.toString());
        jsonOutput.append("],");

        //Adding the City values
        jsonOutput.append("\"City\":[");
        jsonOutput.append(citySB.toString());
        jsonOutput.append("],");

        //Adding the latitude values
        jsonOutput.append("\"Latitude\":[");
        jsonOutput.append(latitudesSB.toString());
        jsonOutput.append("],");

        //Adding the longitude values
        jsonOutput.append("\"Longitude\":[");
        jsonOutput.append(longitudesSB.toString());
        jsonOutput.append("]");

        return jsonOutput.toString();
    }

    public void storesMapValues(double lat, double lng, String address, Container container) {
        saveMapValues(lat, lng, address, container);
    }

    public void saveMapValues(double latval, double lngval, String address, Container container) {
        PbReturnObject pbret = new PbReturnObject();
        PbReturnObject pbret1 = new PbReturnObject();
        String dimension = null;
        if (container != null) {
            dimension = container.getDisplayColumns().get(0);
            if ("TIME".equalsIgnoreCase(dimension)) {
                dimension = container.getDisplayColumns().get(1);
            }
        }
        String dimenvalue = dimension.substring(2);
        Connection con = null;
        con = ProgenConnection.getInstance().getConnectionForElement(dimenvalue);
        PbDb pbdb = new PbDb();

        String finalQuery = null;

        try {
            finalQuery = "UPDATE PRG_GEOGRAPHY_MAPS SET LATITUDE=" + latval + " ,LONGITUDE=" + lngval + " where MAP_VALUE='" + address + "'";

            ArrayList queryList = new ArrayList();
            queryList.add(finalQuery);

            boolean check = executeMultiple(queryList, con);
            con = null;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    logger.error("Exception:", ex);
                }
            }
        }
    }

    public String drillDownData(Container container, String dimId, String dimValue, String reportType, String userid) throws Exception {
        String output = "";

        HashMap<String, String> drillMap = null;
        String childDimId = "";

        if ("D".equalsIgnoreCase(reportType)) {
            pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();
            drillMap = collect.getDrillMap();
            childDimId = drillMap.get(dimId);
            ArrayList<String> rowViewBys = new ArrayList<String>();
            rowViewBys.add(childDimId);
            collect.reportRowViewbyValues = rowViewBys;
            collect.reportParametersValues.put(dimId, dimValue);
            PbDashboardViewerBD viewerBD = new PbDashboardViewerBD();
            viewerBD.getDashboardData(container, collect, userid);
            output = getMapContents(container, "A_" + childDimId, "D", userid, '1', 3, MapConstants.DISPLAY_LOCATION_WISE_VALUES, container.getGeographyDimensionIds().get(0));
        } else {
            PbReportCollection collect = container.getReportCollect();
            drillMap = collect.getDrillMap();
        }

        return output;
    }
}
