package com.progen.execution;

import com.progen.userlayer.db.LogReadWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

public class RequestQuery {

    public static Logger logger = Logger.getLogger(RequestQuery.class);
    LogReadWriter logrw = new LogReadWriter();
    String load_type = null;

//    public String queryUpdateBookingComp(){
//        String seq=null;
//        seq="update  prg_booking_comp_fact b,"
//                + " prg_booking_fact a "
//                + "set b.referral_code = a.referral_code "
//                + "where b.booking_id = a.booking_id and b.referral_code is null";
//        return seq;
//    }
//
//    public String queryUpdateBookingPass(){
//        String seq=null;
//        seq="update  prg_booking_pass_fact b,"
//                + " prg_booking_fact a "
//                + "set b.referral_code = a.referral_code "
//                + "where b.booking_id = a.booking_id and b.referral_code is null " ;
//        return seq;
//    }
//    public String queryDelBooking(){
//        String seq = null;
//        seq="delete from prg_booking_fact where booking_id in(select id from data_log where table_name='bookings')";
//        return seq;
//    }
//    public String queryDelBookingComp(){
//        String seq = null;
//        seq="delete from prg_booking_comp_fact where Component_id in(select id from data_log where table_name='bookings')";
//        return seq;
//    }
//    public String queryDelBookingPass(){
//        String seq = null;
//        seq="delete from prg_booking_pass_fact where consumer_splits_id in(select id from data_log where table_name='bookings')";
//        return seq;
//    }
    public String queryRunSeqMaster() {
        String seq = null;
        seq = "select request_run_Id.NEXTVAL from dual";
        return seq;
    }

    public String queryRunSeqDetail() {
        String seq = null;
        seq = "select request_run_det_Id.NEXTVAL from dual";
        return seq;
    }

    public String queryChkInit(String companyId) {
        String query = null;
        query = "select * "
                + " from PRG_REQUEST_RUN_MASTER "
                + " where REQUEST_CURRENT_STATUS='success' "
                + " and request_Name='Initial load for Quick Travel'"
                + " and company_id = " + companyId;
        return query;
    }

    public String querySelMaster(String companyId) {
        String selDate = null;
        selDate = "select REQUEST_DATA_END_DATE "
                + " from PRG_REQUEST_RUN_MASTER "
                + " where REQUEST_CURRENT_STATUS='success' "
                + " and rownum<2 "
                + " and company_id = " + companyId
                + " order by REQUEST_RUN_ID desc";
        return selDate;
    }

    public String queryRunMasterInsert(String st_date, String lt_date, int count, int colCount, String forceReq, String request_run_Id, String status, String forceInit, String companyId) {

        String sql = null;
        if (forceInit == "INIT") {

            try {
                logrw.fileWriter("Running INIT Load");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            sql = "insert into prg_request_run_master("
                    + "request_run_Id,"
                    + "request_Name,"
                    + "request_Type,"
                    + "request_run_start_time,"
                    + "request_data_start_date,"
                    + "request_data_end_date,"
                    + "request_Current_status,"
                    + "company_id)"
                    + "values"
                    + "(" + request_run_Id + ","
                    + "'Initial load for " + forceReq + "',"
                    + "'INIT',"
                    + "to_date(sysdate,'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + st_date + "', 'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,"
                    + "'" + status + "',"
                    + "" + companyId + ")";
        } else if (forceInit == "INCR" && colCount == 0) {

            try {
                logrw.fileWriter("Running INIT Load");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            sql = "insert into prg_request_run_master("
                    + "request_run_Id,"
                    + "request_Name,"
                    + "request_Type,"
                    + "request_run_start_time,"
                    + "request_data_start_date,"
                    + "request_data_end_date,"
                    + "request_Current_status,"
                    + "company_id)"
                    + "values"
                    + "(" + request_run_Id + ","
                    + "'Initial load for Quick Travel',"
                    + "'INIT',"
                    + "to_date(sysdate,'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + st_date + "', 'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,"
                    + "'" + status + "',"
                    + "" + companyId + ")";
        } else {

            try {
                logrw.fileWriter("Running INCR Load");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
            sql = "insert into prg_request_run_master("
                    + "request_run_Id,"
                    + "request_Name,"
                    + "request_Type,"
                    + "request_run_start_time,"
                    + "request_data_start_date,"
                    + "request_data_end_date,"
                    + "request_Current_status,"
                    + "company_id)"
                    + "values"
                    + "(" + request_run_Id + ","
                    + "'Incremental load for " + forceReq + "',"
                    + "'INCR',"
                    + "to_date(sysdate,'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + st_date + "', 'yyyy-mm-dd hh24:mi:ss'),"
                    + "to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,"
                    + "'" + status + "',"
                    + "" + companyId + ")";
        }
        return sql;
    }

    public String queryUpdateDetail(String request_run_det_Id, String load) {
        String sql = null;
        sql = "update prg_request_run_details "
                + " set request_st_Current_status='" + load + "' "
                + " where request_run_det_Id=" + request_run_det_Id;
        return sql;
    }

    public String queryUpdateMaster(String request_run_Id, String load) {
        String sql = null;
        sql = "update prg_request_run_master "
                + " set request_Current_status='" + load + "' "
                + " where request_run_Id=" + request_run_Id;
        return sql;
    }

    public String queryRunDetailsInsert(String st_date, String lt_date, int count, String forceInit, String request_run_Id, String request_run_det_Id, String request_stage_name, String Current_status, String quick_text, String companyId) {

        load_type = forceInit;
//
//             if (forceInit.equalsIgnoreCase("INCR"))
//                st_date = date.substring(0,19);

        String sql = null;

        sql = "insert into prg_request_run_details("
                + "request_run_det_Id,"
                + "request_run_Id,"
                + "request_stage_name,"
                + "request_stage_Load_type,"
                + "request_st_run_start_time,"
                + "request_st_data_start_date,"
                + "request_st_data_end_date,"
                + "request_stage_table,"
                + "request_stage_sp,"
                + "request_st_Current_status,"
                + "request_st_quick_text,"
                + "company_id)"
                + "values("
                + request_run_det_Id + ","
                + request_run_Id + ","
                + "'" + request_stage_name + "',"
                + "'" + load_type + "',"
                + "to_date(sysdate,'yyyy-mm-dd hh24:mi:ss'),"
                + "to_date('" + st_date + "', 'yyyy-mm-dd hh24:mi:ss'),"
                + "to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,"
                + "'" + request_stage_name + "',"
                + "'" + request_stage_name + "',"
                + "'" + quick_text + "',"
                + "'" + quick_text + "',"
                + "" + companyId + ")";

        return sql;
    }

    public String queryRunSeqtrackLoad() {
        String seq = null;
        seq = "select request_run_det_Id.NEXTVAL from dual";
        return seq;
    }

    public String trackerLoad(String st_date, String lt_date, String forceInit, String tabname, String load, String sequence, String compId) {

        load_type = forceInit;

        String trackload = null;

        if (tabname.equalsIgnoreCase("qrules_rules_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('qrules_rules_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'qrules_rules_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("qrules_rules_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('qrules_rules_dim_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'qrules_rules_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("reservations_service_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('reservations_service_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'reservations_service_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("accomodation_properties_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('accomodation_properties_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'accomodation_properties_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("booking_promo_codes_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('booking_promo_codes_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'booking_promo_codes_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("booking_log_entries_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('booking_log_entries_stg') "
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'booking_log_entries_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("commission_structure_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('commission_structure_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,"
                        + "COMPANY_ID)values(" + sequence + ",'commission_structure_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("adjustments_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('adjustments_dimension_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'fare_basis_sets_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("fare_basis_sets_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('fare_basis_sets_dimension_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'fare_basis_sets_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }

        if (tabname.equalsIgnoreCase("prg_adjustments_reservation_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_reservation_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_reservation_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_booking_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_booking_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_booking_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("product_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('product_dimension_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'product_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_vehicle_components_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_vehicle_components_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_vehicle_components_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("client_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('client_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'client_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("package_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('package_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'package_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("department_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('department_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'department_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_user_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME)=UPPER('prg_user_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_user_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("vendor_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('vendor_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'vendor_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_fact_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_comp_fact_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_comp_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_pass_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_pass_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_pass_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_comp_fact_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_comp_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("payments_fact_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('payments_fact_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'payments_fact_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("vendor_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('vendor_dimension_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'vendor_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_qt_account_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_qt_account_dim_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_qt_account_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("services_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('services_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'services_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("trips_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('trips_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'trips_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("accounting_entries_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('accounting_entries_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'accounting_entries_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Calls_Dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Calls_Dim_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Calls_Dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_calls_fact_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_calls_fact_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_calls_fact_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_CallType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_CallType_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_CallType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_emp_loc_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_emp_loc_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_emp_loc_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_LocationType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_LocationType_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_LocationType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_ReleaseType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_ReleaseType_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_ReleaseType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Queue_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Queue_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Queue_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_QueueItemTypes_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_QueueItemTypes_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_QueueItemTypes_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_team_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_team_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_team_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Extension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Extension_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Extension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_GL_acc_master_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_GL_acc_master_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_GL_acc_master_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_gl_acc_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_gl_acc_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_gl_acc_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_debtor_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_debtor_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_debtor_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_customer_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_customer_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_customer_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_GL_acc_master_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_GL_acc_master_STG')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_GL_acc_master_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("data_log_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('data_log_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'data_log_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("geographic_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('geographic_dimension_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'geographic_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_package_structure_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_package_structure_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_package_structure_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_passenger_types_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_passenger_types_dim_stg')"
                        + " and company_id =" + compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_passenger_types_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }

        return trackload;
    }

    public String trackerLoadIncr(String st_date, String lt_date, String tabname, String load, String compId, String sequence) {
        String trackload = null;


        try {
            logrw.fileWriter("tabname---->" + tabname);
            logrw.fileWriter("load---->" + load);
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        if (tabname.equalsIgnoreCase("qrules_rules_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('qrules_rules_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'qrules_rules_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("qrules_rules_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('qrules_rules_dim_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'qrules_rules_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("reservations_service_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('reservations_service_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'reservations_service_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("accomodation_properties_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('accomodation_properties_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'accomodation_properties_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("booking_promo_codes_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('booking_promo_codes_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'booking_promo_codes_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("booking_log_entries_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('booking_log_entries_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'booking_log_entries_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("commission_structure_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('commission_structure_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'commission_structure_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("adjustments_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('adjustments_dimension_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'adjustments_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("fare_basis_sets_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('fare_basis_sets_dimension_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'fare_basis_sets_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_reservation_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_reservation_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_reservation_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_booking_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_booking_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_booking_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_adjustments_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_adjustments_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("product_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('product_dimension_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'product_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_vehicle_components_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_vehicle_components_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_vehicle_components_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("client_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('client_dimension_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'client_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("package_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('package_dimension_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'package_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("department_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('department_dimension_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'department_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_user_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME)=UPPER('prg_user_dimension_STG')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_user_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("vendor_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('vendor_dimension_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'vendor_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_fact_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_comp_fact_STG')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_comp_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_pass_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_pass_stg')";
//                     + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_pass_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_booking_comp_fact_STG')";
//                      + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_booking_comp_fact_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("payments_fact_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('payments_fact_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'payments_fact_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("vendor_dimension_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('vendor_dimension_STG')";
//                       + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'vendor_dimension_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_qt_account_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_qt_account_dim_stg')";
//                       + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_qt_account_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("services_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('services_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'services_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("trips_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('trips_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'trips_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("accounting_entries_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('accounting_entries_stg')";
//                     + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'accounting_entries_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Calls_Dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Calls_Dim_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Calls_Dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_CallType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_CallType_stg')";
//                   + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_CallType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_emp_loc_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_emp_loc_stg')";
//                       + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_emp_loc_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_LocationType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_LocationType_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_LocationType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_ReleaseType_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_ReleaseType_stg')";
//                   + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_ReleaseType_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Queue_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Queue_stg')";
//                   + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Queue_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_QueueItemTypes_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_QueueItemTypes_stg')";
//                   + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_QueueItemTypes_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_team_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_team_stg')";
//                   + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_team_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_Extension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_Extension_stg')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_Extension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_GL_acc_master_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_GL_acc_master_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_GL_acc_master_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_gl_acc_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_gl_acc_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_gl_acc_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_debtor_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_debtor_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_debtor_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_customer_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_customer_STG')";
//                    + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_customer_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_fin_GL_acc_master_STG")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_fin_GL_acc_master_STG')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_fin_GL_acc_master_STG',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("data_log_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('data_log_stg')";
//                      + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'data_log_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("geographic_dimension_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('geographic_dimension_stg')";
//                        + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'geographic_dimension_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_package_structure_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_package_structure_stg')";
//                       + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_package_structure_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        if (tabname.equalsIgnoreCase("prg_passenger_types_dim_stg")) {
            if (load.equalsIgnoreCase("success")) {
                trackload = "update PRG_LOAD_TRACKER_MASTER_CMP set FACT_LAST_UPDATE_DATE = to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48 "
                        + " where UPPER(TAB_NAME) =UPPER('prg_passenger_types_dim_stg')";
//                       + " and company_id ="+compId;
            } else {
                trackload = "insert into PRG_LOAD_TRACKER("
                        + "id,"
                        + "TAB_NAME,"
                        + "LAST_UPDATE_DATE,"
                        + "TYPE,"
                        + "COMMENTS,"
                        + "STATUS,COMPANY_ID)values(" + sequence + ",'prg_passenger_types_dim_stg',to_date('" + lt_date + "', 'yyyy-mm-dd hh24:mi:ss')-25/48,'" + load_type + "',' ','failure'," + compId + ")";
            }
        }
        return trackload;
    }
}
