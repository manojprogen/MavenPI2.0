/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.dashboardView.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
class DashBoardViewerResBunSqlserver extends ListResourceBundle implements Serializable {

    public DashBoardViewerResBunSqlserver() {
    }

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getScoreCardIds", "select master_id,component_id from prg_ar_dashlet_details where master_id=&"},
        {"getGraphMaster", "SELECT pagm.graph_id,pagm.graph_name,pags.x_size AS GRAPH_WIDTH,pags.y_size AS GRAPH_HEIGHT,pagm.graph_class,pagc.graph_class_name,"
            + "pagm.graph_type,pagt.graph_type_name,pagm.graph_size,pags.graph_size_name,pagm.legend_loc,pagm.allow_legend,pagm.show_grid_x_axis,"
            + "pagm.show_grid_y_axis,pagm.allow_link,pagm.back_color,pagm.font_color,pagm.show_data,pagm.left_y_axis_label,pagm.right_y_axis_label,"
            + "pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS,pagm.show_table, pagm.GRAPH_PROPERTY_XML, pagm.time_series "
            + "FROM PRG_AR_GRAPH_MASTER pagm "
            + " inner join PRG_AR_GRAPH_SIZES pags on (pagm.graph_size  = pags.graph_size_id)"
            + " inner join PRG_AR_GRAPH_CLASS pagc on (pagc.graph_class_id= pagm.graph_class )"
            + " inner join PRG_AR_GRAPH_TYPE pagt  on (pagm.graph_type    = pagt.graph_type_id)"
            + " WHERE "
            + "pagm.graph_id ='&' "
            + "ORDER BY pagm.graph_order, pagm.graph_id"},
        {"getGraphDetails", "select graph.element_id, col_name, axis, column_order from prg_ar_graph_details graph"
            + " where graph_id ='&' order by column_order"},
        {"getQueryDetails", "select distinct element_id, col_disp_name, ref_element_id, folder_id, sub_folder_id, aggregation_type, column_type"
            + " from prg_ar_query_detail where element_id in (&) and report_id=&"}, {"getkpiGraphDetails", "SELECT pakgd.KPI_GRP_ID, pakgd.ELEMENT_ID, pakgd.DASHBOARD_ID, pakgd.NEEDLE, pakgd.KPINAME, pakgd.KPIGRAPHTYPE,"
            + " pakgd.GRAPH_XML, pakgd.GRAPH_WIDTH, pakgd.GRAPH_HEIGHT, pakgd.KPI_DASHLET_NAME  FROM PRG_AR_KPI_GRAPH_DETAILS pakgd, PRG_AR_GRAPH_SIZES pags "
            + "WHERE pakgd.KPI_GRP_ID = &"}, {"saveDashletProperties", "UPDATE PRG_AR_DASHBOARD_DETAILS SET DASHLET_PROPERTIES ='&' WHERE DASHBOARD_DETAILS_ID =&"}, {"getDashletPropertiesHelperObject", "SELECT DASHLET_PROPERTIES FROM PRG_AR_DASHBOARD_DETAILS WHERE DASHBOARD_DETAILS_ID =&"}, {"getTextKpiDrill", "select TEXT_KPI_VALUE , DRILL_ID from PRG_TEXTKPI_DETAILS where DASHBOARD_ID='&' and DASHLET_ID='&'"}, {"getTextKpiComment", "select KPI_VALUE , KPI_COMMENT from PRG_TEXTKPI_USER_COMMENTS where DASHBOARD_ID='&' and DASHLET_ID='&'"}, {"getGroupQueryDetails", "SELECT DISTINCT P.PERENT_ELEMENT_ID,E.USER_COL_DESC,E.REF_ELEMENT_ID,E.AGGREGATION_TYPE,E.FOLDER_ID,E.SUB_FOLDER_ID,E.USER_COL_TYPE, p.group_name FROM PRG_USER_ALL_INFO_DETAILS E, PRG_USER_ELEMENTS_HIERARCHY P WHERE P.PERENT_ELEMENT_ID=E.element_id AND P.LEVEL_NO=1 AND P.GROUP_ID=& "}, {"getGraphsOnReport", "SELECT DISTINCT a.report_id, a.report_name, b.graph_id, b.graph_name FROM prg_ar_report_master a left outer join  prg_ar_graph_master b on (a.report_id= b.report_id) left outer join  prg_ar_report_view_by_master c on (a.report_id  =c.report_id) left outer join  prg_ar_report_details d on (a.report_id  = d.report_id) WHERE a.report_id IN (&) AND a.report_type='R'"}
    };
}
