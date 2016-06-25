/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.map;

import com.progen.report.PbReportCollection;
import com.progen.reportdesigner.bd.DashboardTemplateBD;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.LookupDispatchAction;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class MapAction extends LookupDispatchAction {

    public static Logger logger = Logger.getLogger(MapAction.class);

    @Override
    public Map getKeyMethodMap() {
        Map map = new HashMap();
        map.put("isMapEnabled", "isMapEnabled");
        map.put("mapColumnChanges", "mapColumnChanges");
        map.put("storeLatLng", "storeLatLng");
        map.put("drillDownData", "drillDownData");
        return map;
    }

    public ActionForward isMapEnabled(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        String userid = "";
        if (session != null) {
            userid = String.valueOf(session.getAttribute("USERID"));
        }
        String reportId = request.getParameter("REPORTID");
        String reportType = request.getParameter("reportType");
        String sortType = request.getParameter("sortType");
        String mapView = request.getParameter("mapView");
        String geoView = request.getParameter("geoView");

        // String sortValue = request.getParameter("sortValue");
        int topBottomCount = 3;
        char sortTypeVal = '0';
        if (mapView == null || mapView.equals("") || mapView.equalsIgnoreCase("null")) {
            mapView = MapConstants.DISPLAY_OVERALL_VALUES;
        }

        if (sortType != null && !"".equals(sortType) && !"null".equalsIgnoreCase(sortType)) {
            if (MapConstants.TOP_3.equalsIgnoreCase(sortType)) {
                sortTypeVal = '1';
                topBottomCount = 3;
            } else if (MapConstants.TOP_5.equalsIgnoreCase(sortType)) {
                sortTypeVal = '1';
                topBottomCount = 5;
            } else if (MapConstants.BOTTOM_3.equalsIgnoreCase(sortType)) {
                sortTypeVal = '0';
                topBottomCount = 3;

            } else if (MapConstants.BOTTOM_5.equalsIgnoreCase(sortType)) {
                sortTypeVal = '0';
                topBottomCount = 5;
            }

        } else {
            sortType = "0";
        }


        Container container = Container.getContainerFromSession(request, reportId);

        if (geoView == null || geoView.equals("") || "null".equalsIgnoreCase(geoView)) {
            geoView = container.getGeographyDimensionIds().get(0);
        }

        session.setAttribute(reportId + "_" + "sortType", sortType);
        session.setAttribute(reportId + "_" + "mapView", mapView);
        session.setAttribute(reportId + "_" + "DimensionSelect", geoView);
        String column = null;
        PbReportCollection collect = container.getReportCollect();

        if ("R".equalsIgnoreCase(reportType)) {
            column = container.getOriginalColumns().get(0);
            if ("TIME".equalsIgnoreCase(column)) {
                column = container.getOriginalColumns().get(1);
            }
        } else {
            column = collect.reportRowViewbyValues.get(0);
        }

        MapBD bd = new MapBD();

        if (!column.startsWith("A_")) {
            column = "A_" + column;
        }

        String mapContents = bd.getMapContents(container, column, reportType, userid, sortTypeVal, topBottomCount, mapView, geoView);
        try {
            response.getWriter().print(mapContents);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward mapColumnChanges(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception {

        String reportId = request.getParameter("REPORTID");
        String measuresid = request.getParameter("mapMeasureids");
        String measuresname = request.getParameter("mapMeasurename");
        String reportType = request.getParameter("reportType");
        String editMap = request.getParameter("editMap");
        String mainMeasures = request.getParameter("mainMeasures");
        String supportingMeasures = request.getParameter("supportingMeasures");
        //String measureIdsArray[] = measuresid.split(",");
        String[] mainMeasArray = mainMeasures.split(",");
        String[] suppMeasArray = supportingMeasures.split(",");
        //String measureLabelArray[] = measuresname.split(",");
        String mainMeasureLabels = request.getParameter("mainMeasureLabels");
        String suppMeasureLabels = request.getParameter("suppMeasureLabels");
        String[] mainMeasureLabelArray = mainMeasureLabels.split(",");
        String[] suppMeasureLabelArray = suppMeasureLabels.split(",");
        String[] dashletDivId = request.getParameter("divId").split("-");
        String divId = "newMap";
        boolean editMapVal = false;
        if ("D".equalsIgnoreCase(reportType)) {
            editMapVal = true;
            if (dashletDivId.length == 2) {
                divId = dashletDivId[1];
            } else {
                divId = dashletDivId[0];
            }
        }

        String userId = (String) request.getSession().getAttribute("USERID");
        Container container = Container.getContainerFromSession(request, reportId);
        container.resetMapMainMeasures();
        container.resetMapSupportingMeasures();
        container.resetMapMainMeasureLabels();
        container.resetMapSupportingMeasuresLabels();

        List<String> suppMeasList = new ArrayList<String>();
        List<String> suppLabelList = new ArrayList<String>();
        List<String> mainMeasureList = new ArrayList<String>();

        for (int i = 0; i < mainMeasArray.length; i++) {
            if (!mainMeasArray[i].startsWith(",")) {
                mainMeasArray[i] = "A_" + mainMeasArray[i];
            }
            container.setMapMainMeasure(mainMeasArray[i]);
            container.setMapMainMeasureLabel(mainMeasureLabelArray[i]);
            mainMeasureList.add(mainMeasArray[i]);
        }
        if (suppMeasArray.length > 0) {
            for (int i = 0; i < suppMeasArray.length; i++) {
                if (!suppMeasArray[i].startsWith(",") && !"".equals(suppMeasArray[i])) {
                    suppMeasArray[i] = "A_" + suppMeasArray[i];
                    suppMeasList.add(suppMeasArray[i]);
                    suppLabelList.add(suppMeasureLabelArray[i]);
                }
            }
            if (!suppMeasList.isEmpty()) {
                container.setMapSupportingMeasures(suppMeasList);
                container.setMapSupportingMeasuresLabels(suppLabelList);
            }
        }

        if ("R".equalsIgnoreCase(reportType)) {

            ArrayList<String> mapMeasLst = new ArrayList<String>();
            mapMeasLst.addAll(container.getMapSupportingMeasures());
            mapMeasLst.addAll(container.getMapMainMeasure());

            boolean isQryNeeded = false;
            ArrayList<String> originalColumns = container.getOriginalColumns();
            HashMap ReportHashMap = container.getReportHashMap();
            ArrayList reportQryElementIds = null;
            ArrayList reportQryAggregations = null;
            ArrayList reportQryColNames = null;
            reportQryElementIds = (ArrayList) ReportHashMap.get("reportQryElementIds");
            reportQryAggregations = (ArrayList) ReportHashMap.get("reportQryAggregations");

            for (String mapMeasure : mapMeasLst) {
                if (!originalColumns.contains(mapMeasure)) {
                    isQryNeeded = true;
                    originalColumns.add(mapMeasure);
                    reportQryElementIds.add(mapMeasure.substring(2));
                }
            }

            if (isQryNeeded) {
                ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
                reportQryAggregations = reportTemplateDAO.getReportQryAggregations(reportQryElementIds);
                reportQryColNames = reportTemplateDAO.getReportQryColNames();
                ReportHashMap.put("reportQryElementIds", reportQryElementIds);
                ReportHashMap.put("reportQryAggregations", reportQryAggregations);
                ReportHashMap.put("reportQryColNames", reportQryColNames);
                container.setReportHashMap(ReportHashMap);
                PbReportViewerBD bd = new PbReportViewerBD();
                bd.prepareReport("mapMeasChange", container, reportId, userId, new HashMap<String, String>());
            }
        } else {
            DashboardTemplateBD dashTempBD = new DashboardTemplateBD();
            dashTempBD.addMap(container, mainMeasureList, suppMeasList, editMapVal, divId, userId);
        }

        return null;
    }

    public ActionForward storeLatLng(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        PrintWriter out = null;
        Container container = null;
        String latvalues = request.getParameter("latvalues");
        String lngvalues = request.getParameter("lngvalues");
        String address = request.getParameter("address");
        String reportId = request.getParameter("reportId");
        container = Container.getContainerFromSession(request, reportId);
        MapBD bd = new MapBD();
        double one = Double.parseDouble(latvalues);
        double two = Double.parseDouble(lngvalues);
        bd.storesMapValues(one, two, address, container);

        try {
            out = response.getWriter();
            out.print("success");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

        return null;
    }

    public ActionForward drillDownData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        String userid = "";
        if (session != null) {
            userid = String.valueOf(session.getAttribute("USERID"));
        }
        try {
            String reportId = request.getParameter("reportId");
            Container container = Container.getContainerFromSession(request, reportId);
            PrintWriter out = response.getWriter();
            String dimId = request.getParameter("dimId");
            String dimValue = request.getParameter("dimValue");
            String reportType = request.getParameter("reportType");

            MapBD bd = new MapBD();

            String mapContents = bd.drillDownData(container, dimId, dimValue, reportType, userid);
            out.println(mapContents);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return null;
    }
}