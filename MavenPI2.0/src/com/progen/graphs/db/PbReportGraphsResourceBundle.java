/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.graphs.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PbReportGraphsResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Jul 22, 2009, 5:03:59 PM
 */
public class PbReportGraphsResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        //--------------------------------------getReportGraphsData() related queries--------------------------------------------------------------------
        {"getReportGraphsData", "SELECT  paqd.element_id,paqd.col_disp_name, pagm.graph_id, pagm.graph_type, pagt.graph_type_name, pagm.graph_size, pags.graph_size_name,"
            + " pagd.axis FROM PRG_AR_GRAPH_DETAILS pagd, PRG_AR_GRAPH_MASTER pagm, PRG_AR_GRAPH_TYPE pagt, PRG_AR_GRAPH_SIZES pags, PRG_AR_QUERY_DETAIL paqd "
            + "where paqd.qry_col_id= pagd.query_col_id and pagt.graph_type_id= pagm.graph_type and pags.graph_size_id= pagm.graph_size and pagm.report_id='&'"}, {"getReportGraphTypes", "select graph_type_id, graph_type_name from PRG_AR_GRAPH_TYPE order by graph_type_name"}, {"getReportGraphSizes", "select graph_size_id, graph_size_name from prg_ar_graph_sizes order by graph_size_id"}, {"getReportGraphHeaderInfo", "select pgh.graph_id,pgh.graph_size, pgs.graph_size_name,pgh.graph_type,pgt.graph_type_name from prg_ar_graph_master pgh,"
            + "prg_ar_graph_type pgt, prg_ar_graph_sizes pgs where pgt.graph_type_id = pgh.graph_type AND pgs.graph_size_id  = pgh.graph_size and "
            + "pgh.report_id='&' order by  pgh.graph_order"}, {"getCrossTabReportGraphTypes", "select graph_type_id, graph_type_name from prg_ar_graph_type where graph_class_id=(select graph_class_id "
            + "from prg_ar_graph_class where graph_class_name='Category') order by graph_type_name"} //----------------------------------------------------------------------------------------------------------
        , {"updateGraphSizes", "update prg_graph_header set graph_size=& where graph_id=& "}, {"updateGraphTypes", "update prg_graph_header set graph_class=(select graph_class_id from prg_graph_type where graph_type_id =&), graph_type=& where graph_id=&"}, {"deleteGraphColumns", "delete  from prg_graph_details where graph_id=& and  table_column_id not in (select report_column_id from prg_report_table_details "
            + "where table_id=& and  is_view_by_column='Y' )"}, {"getGrpDetailsInfo", "select report_column_id, column_disp_name, column_name from prg_report_table_details where is_view_by_column='N' and table_id=& and "
            + "upper(column_name) in ("}, {"insertGraphColumns", "insert into PRG_GRAPH_DETAILS (graph_col_id,TABLE_ID, GRAPH_ID, TABLE_COLUMN_ID, COLUMN_NAME, COLUMN_DISP_NAME, COLUMN_SEQ, AXIS) "
            + "values(PRG_GRAPH_DETAILS_SEQ.nextval,&,&,&,'&','&',(select MAX(column_seq)+1 from prg_graph_details where graph_id=& and table_id=& ),&)"}, {"updateGraphColumnsAxis", "update prg_graph_details set axis=& where graph_id=& and table_id=& and table_column_id in (select report_column_id from "
            + "prg_report_table_details where table_id=& and column_name in ('&'))"}, {"getReportByBizAreas", "select DISTINCT nvl(prm.prm_disp_name,prm.prm_report_name) report_name, prm.prm_report_id "
            + "from prg_report_master prm ,prg_report_details prd where prm.prm_report_id= prd.prd_report_id AND "
            + "prd.prd_business_area_id in (select prd_business_area_id from prg_report_details where prd_report_id=&) and prd.prd_report_id not in (&)"}, {"updateColumnDrillDown", "update prg_report_table_details set is_dill_down_across_col='&', drill_report_id='&' where table_id=&  and upper(column_name)='&'"}, {"getColDispNames", "select column_name,nvl(column_disp_name, column_name) column_disp_name,nvl(is_dill_down_across_col,'N'), nvl(drill_report_id,'-1')  "
            + "from prg_report_table_details where table_id=& and upper(column_name) in ("}
    };
}
