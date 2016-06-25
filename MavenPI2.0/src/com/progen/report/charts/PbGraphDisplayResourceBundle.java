/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.charts;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PbGraphDisplayResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Sep 19, 2009, 1:01:04 PM
 */
public class PbGraphDisplayResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"grpHdrsQuery", " SELECT pagm.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc, pagm.allow_legend, pagm.show_grid_x_axis, pagm.show_grid_y_axis, pagm.allow_link, "
            + "pagm.back_color,pagm.font_color, pagm.show_data,pagm.left_y_axis_label,pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,GRAPH_DISPLAY_ROWS,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2 FROM PRG_AR_GRAPH_MASTER pagm ,PRG_AR_GRAPH_SIZES pags,PRG_AR_GRAPH_CLASS pagc,"
            + "PRG_AR_GRAPH_TYPE pagt WHERE  pagm.graph_size= pags.graph_size_id AND pagc.graph_class_id= pagm.graph_class AND "
            + "pagm.graph_type= pagt.graph_type_id AND pagm.report_id='&' order by pagm.graph_order, pagm.graph_id"}, {"grpDtlsQuery", " SELECT paqd.element_id, paqd.col_disp_name, pagd.axis   FROM PRG_AR_GRAPH_DETAILS pagd,  PRG_AR_QUERY_DETAIL paqd  where "
            + "pagd.query_col_id= paqd.qry_col_id and pagd.graph_id=&  order by pagd.column_order "} // added for dashboard graph display
        , {"dashboardGrpHdrsQuery", " SELECT DISTINCT padb.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc, pagm.allow_legend, pagm.show_grid_x_axis, pagm.show_grid_y_axis, pagm.allow_link, "
            + "pagm.back_color,pagm.font_color, pagm.show_data,pagm.left_y_axis_label,pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS ,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2 FROM PRG_AR_GRAPH_MASTER pagm ,  PRG_AR_GRAPH_SIZES pags,PRG_AR_GRAPH_CLASS pagc,PRG_AR_GRAPH_TYPE pagt, "
            + "prg_ar_dashboard_details padb WHERE pagm.graph_size= pags.graph_size_id AND pagc.graph_class_id= pagm.graph_class AND "
            + "pagm.graph_type= pagt.graph_type_id AND pagm.report_id=& and pagm.report_id= padb.ref_report_id "
            + "and pagm.graph_id= padb.graph_id order by  padb.graph_id"}, {"dashboardGrpDtlsQuery", " SELECT paqd.element_id, paqd.col_disp_name, pagd.axis   FROM PRG_AR_GRAPH_DETAILS pagd,  PRG_AR_QUERY_DETAIL paqd  where "
            + "pagd.query_col_id= paqd.qry_col_id and pagd.graph_id=& "}, {"grpHdrsQueryByGraphId", "SELECT pagm.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc ,  pagm.allow_legend , pagm.show_grid_x_axis ,  pagm.show_grid_y_axis,  "
            + "pagm.allow_link,  pagm.back_color,  pagm.font_color,  pagm.show_data,  pagm.left_y_axis_label,  pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2  FROM "
            + " PRG_AR_GRAPH_MASTER pagm ,  PRG_AR_GRAPH_SIZES pags,PRG_AR_GRAPH_CLASS pagc,PRG_AR_GRAPH_TYPE pagt WHERE  pagm.graph_size= pags.graph_size_id "
            + "AND pagc.graph_class_id= pagm.graph_class AND pagm.graph_type= pagt.graph_type_id AND pagm.report_id='&' and pagm.graph_id='&'"
            + "  order by pagm.graph_order, pagm.graph_id"}, {"grpHdrsQueryByGraphIdFX", "SELECT pagm.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc ,  pagm.allow_legend , pagm.show_grid_x_axis ,  pagm.show_grid_y_axis,  "
            + "pagm.allow_link,  pagm.back_color,  pagm.font_color,  pagm.show_data,  pagm.left_y_axis_label,  pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2  FROM "
            + " PRG_AR_GRAPH_MASTER pagm ,  PRG_AR_GRAPH_SIZES_FX pags,PRG_AR_GRAPH_CLASS_FX pagc,PRG_AR_GRAPH_TYPE_FX pagt WHERE  pagm.graph_size= pags.graph_size_id "
            + "AND pagc.graph_class_id= pagm.graph_class AND pagm.graph_type= pagt.graph_type_id AND pagm.report_id='&' and pagm.graph_id='&'"
            + "  order by pagm.graph_order, pagm.graph_id"}, {"dashboardGraphHdrsQueryNew", " SELECT DISTINCT padb.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc, pagm.allow_legend, pagm.show_grid_x_axis, pagm.show_grid_y_axis, pagm.allow_link, "
            + "pagm.back_color,pagm.font_color, pagm.show_data,pagm.left_y_axis_label,pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2 FROM PRG_AR_GRAPH_MASTER pagm ,  PRG_AR_GRAPH_SIZES pags,PRG_AR_GRAPH_CLASS pagc,PRG_AR_GRAPH_TYPE pagt, "
            + "prg_ar_dashboard_details padb WHERE pagm.graph_size= pags.graph_size_id AND pagc.graph_class_id= pagm.graph_class AND "
            + "pagm.graph_type= pagt.graph_type_id AND pagm.graph_id=& and pagm.report_id= padb.ref_report_id "
            + "and pagm.graph_id= padb.graph_id order by  padb.graph_id"} //added by santhosh.kumar@progenbusiness.com on 25-02-2010
        , {"graphHdrsQueryFX", " SELECT pagm.graph_id, pagm.graph_name,pags.x_size as GRAPH_WIDTH, pags.y_size as GRAPH_HEIGHT, pagc.graph_class_name, "
            + "pagt.graph_type_name,pags.graph_size_name,pagm.legend_loc, pagm.allow_legend, pagm.show_grid_x_axis, pagm.show_grid_y_axis, pagm.allow_link, "
            + "pagm.back_color,pagm.font_color, pagm.show_data,pagm.left_y_axis_label,pagm.right_y_axis_label,pagm.row_values,pagm.show_gt,pagm.GRAPH_DISPLAY_ROWS,GRAPH_PROPERTY_XML,GRAPH_PROPERTY_XML1,GRAPH_PROPERTY_XML2 FROM PRG_AR_GRAPH_MASTER pagm ,PRG_AR_GRAPH_SIZES_FX pags,PRG_AR_GRAPH_CLASS pagc,"
            + "PRG_AR_GRAPH_TYPE pagt WHERE  pagm.graph_size= pags.graph_size_id AND pagc.graph_class_id= pagm.graph_class AND "
            + "pagm.graph_type= pagt.graph_type_id AND pagm.report_id='&' order by pagm.graph_order, pagm.graph_id"}
    };
}