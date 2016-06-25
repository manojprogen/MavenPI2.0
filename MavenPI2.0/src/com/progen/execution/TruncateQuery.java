package com.progen.execution;

class TruncateQuery {

    public String truncateTabname(String tabname) {
        String query = null;
        if (tabname.equalsIgnoreCase("qrules_rules")) {
            query = "truncate table qrules_rules";
        }
        if (tabname.equalsIgnoreCase("qrules_rules_stg")) {
            query = "truncate table qrules_rules_stg";
        }
        if (tabname.equalsIgnoreCase("qrules_rules_dim")) {
            query = "truncate table qrules_rules_dim";
        }
        if (tabname.equalsIgnoreCase("qrules_rules_dim_stg")) {
            query = "truncate table qrules_rules_dim_stg";
        }
        if (tabname.equalsIgnoreCase("reservations_service")) {
            query = "truncate table reservations_service";
        }
        if (tabname.equalsIgnoreCase("reservations_service_stg")) {
            query = "truncate table reservations_service_stg";
        }
        if (tabname.equalsIgnoreCase("accomodation_properties")) {
            query = "truncate table accomodation_properties";
        }
        if (tabname.equalsIgnoreCase("accomodation_properties_stg")) {
            query = "truncate table accomodation_properties_stg";
        }
        if (tabname.equalsIgnoreCase("booking_promo_codes")) {
            query = "truncate table booking_promo_codes";
        }
        if (tabname.equalsIgnoreCase("booking_promo_codes_stg")) {
            query = "truncate table booking_promo_codes_stg";
        }
        if (tabname.equalsIgnoreCase("booking_log_entries_fact")) {
            query = "truncate table booking_log_entries_fact";
        }
        if (tabname.equalsIgnoreCase("booking_log_entries_stg")) {
            query = "truncate table booking_log_entries_stg";
        }
        if (tabname.equalsIgnoreCase("commission_structure_fact")) {
            query = "truncate table commission_structure_fact";
        }
        if (tabname.equalsIgnoreCase("commission_structure_stg")) {
            query = "truncate table commission_structure_stg";
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_reservation_stg")) {
            query = "truncate table prg_adjustments_reservation_stg";
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_booking_stg")) {
            query = "truncate table prg_adjustments_booking_stg";
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_stg")) {
            query = "truncate table prg_adjustments_stg";
        }
        if (tabname.equalsIgnoreCase("adjustments_dimension_stg")) {
            query = "truncate table adjustments_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("fare_basis_sets_dimension_stg")) {
            query = "truncate table fare_basis_sets_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("adjustments_dimension")) {
            query = "truncate table adjustments_dimension";
        }
        if (tabname.equalsIgnoreCase("fare_basis_sets_dimension")) {
            query = "truncate table fare_basis_sets_dimension";
        }

        if (tabname.equalsIgnoreCase("prg_adjustments_reservation_fact")) {
            query = "truncate table prg_adjustments_reservation_fact";
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_booking_fact")) {
            query = "truncate table prg_adjustments_booking_fact";
        }
        if (tabname.equalsIgnoreCase("prg_adjustments_dim")) {
            query = "truncate table prg_adjustments_dim";
        }
        if (tabname.equalsIgnoreCase("prg_comp_pax_type")) {
            query = "truncate table prg_comp_pax_type";
        }
        if (tabname.equalsIgnoreCase("prg_booking_pax_type")) {
            query = "truncate table prg_booking_pax_type";
        }
        if (tabname.equalsIgnoreCase("dim_prg_reservation_id")) {
            query = "truncate table dim_prg_reservation_id";
        }
        if (tabname.equalsIgnoreCase("prg_temp_pass_name")) {
            query = "truncate table prg_temp_pass_name";
        }
        if (tabname.equalsIgnoreCase("dim_prg_consumer_splits_id")) {
            query = "truncate table dim_prg_consumer_splits_id";
        }
        if (tabname.equalsIgnoreCase("prg_tills")) {
            query = "truncate table prg_tills";
        }
        if (tabname.equalsIgnoreCase("dim_prg_booking_id")) {
            query = "truncate table dim_prg_booking_id";
        }
        if (tabname.equalsIgnoreCase("dim_prg_booking_reference")) {
            query = "truncate table dim_prg_booking_reference";
        }
        if (tabname.equalsIgnoreCase("data_log")) {
            query = "truncate table data_log";
        }
        if (tabname.equalsIgnoreCase("data_log_stg")) {
            query = "truncate table data_log_stg";
        }
        if (tabname.equalsIgnoreCase("geographic_dimension_stg")) {
            query = "truncate table geographic_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("geographic_dimension")) {
            query = "truncate table geographic_dimension";
        }
        if (tabname.equalsIgnoreCase("prg_passenger_types_dim")) {
            query = "truncate table prg_passenger_types_dim";
        }
        if (tabname.equalsIgnoreCase("prg_package_structure_fact")) {
            query = "truncate table prg_package_structure_fact";
        }
        if (tabname.equalsIgnoreCase("prg_passenger_types_dim_stg")) {
            query = "truncate table prg_passenger_types_dim_stg";
        }
        if (tabname.equalsIgnoreCase("prg_package_structure_stg")) {
            query = "truncate table prg_package_structure_stg";
        }

        if (tabname.equalsIgnoreCase("resource_groups")) {
            query = "truncate table resource_groups";
        }
//        if(tabname.equalsIgnoreCase("prg_booking_pack_ref"))
//        {
//          query="truncate table prg_booking_pack_ref";
//        }
        if (tabname.equalsIgnoreCase("prg_new_prd_calc")) {
            query = "truncate table prg_new_prd_calc";
        }
        if (tabname.equalsIgnoreCase("reservation_groups")) {
            query = "truncate table reservation_groups";
        }
        if (tabname.equalsIgnoreCase("resources")) {
            query = "truncate table resources";
        }
        if (tabname.equalsIgnoreCase("product_dimension_stg")) {
            query = "truncate table product_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("prg_vehicle_components_stg")) {
            query = "truncate table prg_vehicle_components_stg";
        }
        if (tabname.equalsIgnoreCase("client_dimension_stg")) {
            query = "truncate table client_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("accounting_entries_stg")) {
            query = "truncate table accounting_entries_stg";
        }
        if (tabname.equalsIgnoreCase("package_dimension_stg")) {
            query = "truncate table package_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("department_dimension_stg")) {
            query = "truncate table department_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("prg_user_dimension_stg")) {
            query = "truncate table prg_user_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("vendor_dimension_stg")) {
            query = "truncate table vendor_dimension_stg";
        }
        if (tabname.equalsIgnoreCase("prg_booking_fact_stg")) {
            query = "truncate table prg_booking_fact_stg";
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact_stg")) {
            query = "truncate table prg_booking_comp_fact_stg";
        }
        if (tabname.equalsIgnoreCase("payments_fact_stg")) {
            query = "truncate table payments_fact_stg";
        }
        if (tabname.equalsIgnoreCase("prg_fin_GL_acc_master_stg")) {
            query = "truncate table prg_fin_gl_acc_master_stg";
        }
        if (tabname.equalsIgnoreCase("prg_fin_gl_acc_stg")) {
            query = "truncate table prg_fin_gl_acc_stg";
        }
        if (tabname.equalsIgnoreCase("prg_fin_debtor_stg")) {
            query = "truncate table prg_fin_debtor_stg";
        }
        if (tabname.equalsIgnoreCase("prg_fin_customer_stg")) {
            query = "truncate table prg_fin_customer_stg";
        }
        if (tabname.equalsIgnoreCase("prg_qt_account_dim_stg")) {
            query = "truncate table prg_qt_account_dim_stg";
        }
        if (tabname.equalsIgnoreCase("prg_Calls_Dim_stg")) {
            query = "truncate table prg_calls_dim_stg";
        }
        if (tabname.equalsIgnoreCase("prg_calls_fact_stg")) {
            query = "truncate table prg_calls_fact_stg";
        }
        if (tabname.equalsIgnoreCase("prg_calltype_stg")) {
            query = "truncate table prg_calltype_stg";
        }
        if (tabname.equalsIgnoreCase("prg_emp_loc_stg")) {
            query = "truncate table prg_emp_loc_stg";
        }
        if (tabname.equalsIgnoreCase("prg_extension_stg")) {
            query = "truncate table prg_extension_stg";
        }
        if (tabname.equalsIgnoreCase("prg_locationtype_stg")) {
            query = "truncate table prg_locationtype_stg";
        }
        if (tabname.equalsIgnoreCase("prg_queue_stg")) {
            query = "truncate table prg_queue_stg";
        }
        if (tabname.equalsIgnoreCase("prg_queueitemtypes_stg")) {
            query = "truncate table prg_queueitemtypes_stg";
        }
        if (tabname.equalsIgnoreCase("prg_releasetype_stg")) {
            query = "truncate table prg_releasetype_stg";
        }
        if (tabname.equalsIgnoreCase("prg_team_stg")) {
            query = "truncate table prg_team_stg";
        }
        if (tabname.equalsIgnoreCase("prg_booking_pass_stg")) {
            query = "truncate table prg_booking_pass_stg";
        }
        if (tabname.equalsIgnoreCase("services_stg")) {
            query = "truncate table services_stg";
        }
        if (tabname.equalsIgnoreCase("trips_stg")) {
            query = "truncate table trips_stg";
        }
        if (tabname.equalsIgnoreCase("client_dimension")) {
            query = "truncate table client_dimension";
        }
        if (tabname.equalsIgnoreCase("department_dimension")) {
            query = "truncate table department_dimension";
        }
        if (tabname.equalsIgnoreCase("product_dimension")) {
            query = "truncate table product_dimension";
        }
        if (tabname.equalsIgnoreCase("package_dimension")) {
            query = "truncate table package_dimension";
        }
        if (tabname.equalsIgnoreCase("prg_user_dimension")) {
            query = "truncate table prg_user_dimension";
        }
        if (tabname.equalsIgnoreCase("vendor_dimension")) {
            query = "truncate table vendor_dimension";
        }
        if (tabname.equalsIgnoreCase("prg_booking_fact")) {
            query = "truncate table prg_booking_fact";
        }
        if (tabname.equalsIgnoreCase("prg_booking_comp_fact")) {
            query = "truncate table prg_booking_comp_fact";
        }
        if (tabname.equalsIgnoreCase("payments_facts")) {
            query = "truncate table payments_facts";
        }
        if (tabname.equalsIgnoreCase("prg_vehicle_components_fact")) {
            query = "truncate table prg_vehicle_components_fact";
        }
        if (tabname.equalsIgnoreCase("accounting_entries_facts")) {
            query = "truncate table accounting_entries_facts";
        }
        if (tabname.equalsIgnoreCase("prg_booking_pass_fact")) {
            query = "truncate table prg_booking_pass_fact";
        }
        if (tabname.equalsIgnoreCase("prg_qt_account_dim")) {
            query = "truncate table prg_qt_account_dim";
        }
        if (tabname.equalsIgnoreCase("services_fact")) {
            query = "truncate table services_fact";
        }
        if (tabname.equalsIgnoreCase("trips_fact")) {
            query = "truncate table trips_fact";
        }
        if (tabname.equalsIgnoreCase("prg_calls_dim")) {
            query = "truncate table prg_calls_dim";
        }
        if (tabname.equalsIgnoreCase("prg_calls_fact")) {
            query = "truncate table prg_calls_fact";
        }
        if (tabname.equalsIgnoreCase("prg_emp_loc_dim")) {
            query = "truncate table prg_emp_loc_dim";
        }
        if (tabname.equalsIgnoreCase("prg_calltype_dim")) {
            query = "truncate table prg_calltype_dim";
        }
        if (tabname.equalsIgnoreCase("prg_locationtype_dim")) {
            query = "truncate table prg_locationtype_dim";
        }
        if (tabname.equalsIgnoreCase("prg_queueitemtypes_dim")) {
            query = "truncate table prg_queueitemtypes_dim";
        }
        if (tabname.equalsIgnoreCase("prg_releasetype_dim")) {
            query = "truncate table prg_releasetype_dim";
        }
        if (tabname.equalsIgnoreCase("prg_team_dim")) {
            query = "truncate table prg_team_dim";
        }
        if (tabname.equalsIgnoreCase("prg_extension_dim")) {
            query = "truncate table prg_extension_dim";
        }
        if (tabname.equalsIgnoreCase("prg_queue_dim")) {
            query = "truncate table prg_queue_dim";
        }
        if (tabname.equalsIgnoreCase("prg_fin_gl_accounts_data_fact")) {
            query = "truncate table prg_fin_gl_accounts_data_fact";
        }
        if (tabname.equalsIgnoreCase("prg_fin_gl_acc_master_dim")) {
            query = "truncate table prg_fin_gl_acc_master_dim";
        }
        if (tabname.equalsIgnoreCase("prg_fin_debtor_fact")) {
            query = "truncate table prg_fin_debtor_fact";
        }
        if (tabname.equalsIgnoreCase("prg_fin_cust_master_dim")) {
            query = "truncate table prg_fin_cust_master_dim";
        }
        if (tabname.equalsIgnoreCase("prg_queue_dim")) {
            query = "truncate table prg_queue_dim";
        }
        return query;
    }
}
