/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.kpi;

import com.progen.report.DashletDetail;
import com.progen.report.entities.KPI;
import com.progen.report.pbDashboardCollection;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import prg.db.Container;

/**
 *
 * @author progen
 */
public class KPIHelper {

    public Map<String, String> getKPIDrillMap(HttpServletRequest request, String reportId, String dashletId) {
        Map<String, String> drillMap = null;
        Container container = Container.getContainerFromSession(request, reportId);

        pbDashboardCollection collect = (pbDashboardCollection) container.getReportCollect();

        DashletDetail detail = collect.getDashletDetail(dashletId);
        KPI kpiDetails = (KPI) detail.getReportDetails();
        drillMap = kpiDetails.getKPIDrill();

        return drillMap;
    }
}
