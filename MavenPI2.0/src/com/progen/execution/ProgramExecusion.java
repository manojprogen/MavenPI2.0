package com.progen.execution;

import com.progen.userlayer.db.LogReadWriter;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ProgramExecusion {

    public static Logger logger = Logger.getLogger(ProgramExecusion.class);
    ExecuteQuery execQuery = new ExecuteQuery();
    LogReadWriter logrw = new LogReadWriter();

    public void loadQuickTravelInit() {

//        execQuery.setTragetConnectionString("mySql_sl");
//        execQuery.setTragetConnectionString("seaLinkdw");
        try {
            execQuery.runUpdateRemCommaStg();

            logrw.fileWriter("Removing Comma Successful After STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Removing Comma failed After QT inserting in STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatebookingComments();

            logrw.fileWriter("updating public comments and private comments Successful for booking fact After QT inserting in STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("updating public comments and private comments Failed for booking fact After QT inserting in STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
//            execQuery.setSourceTableName("data_log_stg");
            execQuery.setTragetTableName("data_log");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for data_log");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for data_log");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//            execQuery.setSourceTableName("geographic_dimension_stg");
            execQuery.setTragetTableName("geographic_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for geographic_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for geographic_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//            execQuery.setSourceTableName("prg_package_structure_stg");
            execQuery.setTragetTableName("prg_package_structure_fact");
            logrw.fileWriter("Load Failed for prg_package_structure_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load Failed for prg_package_structure_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_package_structure_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//            execQuery.setSourceTableName("prg_passenger_types_dim_stg");
            execQuery.setTragetTableName("prg_passenger_types_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_passenger_types_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_passenger_types_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//            execQuery.setSourceTableName("client_dimension_stg");
            execQuery.setTragetTableName("client_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for client_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for client_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("department_dimension_stg");
            execQuery.setTragetTableName("department_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for department_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for department_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("product_dimension_stg");
            execQuery.setTragetTableName("product_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for product_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for product_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//            execQuery.setSourceTableName("package_dimension_stg");
            execQuery.setTragetTableName("package_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for package_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for package_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//            execQuery.setSourceTableName("prg_user_dimension_stg");
            execQuery.setTragetTableName("prg_user_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_user_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_user_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("vendor_dimension_stg");
            execQuery.setTragetTableName("vendor_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for vendor_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for vendor_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_booking_fact_stg");
            execQuery.setTragetTableName("prg_booking_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_booking_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setTragetTableName("prg_booking_comp_fact");
            // execQuery.runDelBookingBookingComp();
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_booking_comp_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_comp_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("payments_fact_stg");
            execQuery.setTragetTableName("payments_facts");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for payments_facts");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for payments_facts");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_vehicle_components_stg");
            execQuery.setTragetTableName("prg_vehicle_components_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_vehicle_components_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_vehicle_components_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("accounting_entries_stg");
            execQuery.setTragetTableName("accounting_entries_facts");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for accounting_entries_facts");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for accounting_entries_facts");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_booking_pass_stg");
            execQuery.setTragetTableName("prg_booking_pass_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_booking_pass_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_pass_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_qt_account_dim_stg");
            execQuery.setTragetTableName("prg_qt_account_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_qt_account_dim ");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_qt_account_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("services_stg");
            execQuery.setTragetTableName("services_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for services_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for services_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("trips_stg");
            execQuery.setTragetTableName("trips_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for trips_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for trips_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//           execQuery.setSourceTableName("prg_adjustments_reservation_stg");
            execQuery.setTragetTableName("prg_adjustments_reservation_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_adjustments_reservation_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_reservation_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_adjustments_booking_stg");
            execQuery.setTragetTableName("prg_adjustments_booking_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_adjustments_booking_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_booking_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_adjustments_stg");
            execQuery.setTragetTableName("prg_adjustments_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_adjustments_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("adjustments_dimension_stg");
            execQuery.setTragetTableName("adjustments_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for adjustments_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("fare_basis_sets_dimension_stg");
            execQuery.setTragetTableName("fare_basis_sets_dimension");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for fare_basis_sets_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for fare_basis_sets_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("commission_structure_stg");
            execQuery.setTragetTableName("commission_structure_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for commission_structure_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for commission_structure_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//           execQuery.setSourceTableName("booking_log_entries_stg");
            execQuery.setTragetTableName("booking_log_entries_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for booking_log_entries_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for booking_log_entries_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//           execQuery.setSourceTableName("booking_promo_codes_stg");
            execQuery.setTragetTableName("booking_promo_codes");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for booking_promo_codes");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for booking_promo_codes");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("accomodation_properties_stg");
            execQuery.setTragetTableName("accomodation_properties");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for accomodation_properties");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for accomodation_properties");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("reservations_service_stg");
            execQuery.setTragetTableName("reservations_service");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for reservations_service");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for reservations_service");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("qrules_rules_dim_stg");
            execQuery.setTragetTableName("qrules_rules_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for qrules_rules_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for qrules_rules_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
//           execQuery.setSourceTableName("qrules_rules_stg");
            execQuery.setTragetTableName("qrules_rules");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for qrules_rules");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for qrules_rules");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }


//        try {
//            execQuery.runUpdateQtRecord();
//            
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//        }
        try {
            execQuery.runbookingInsert();

            logrw.fileWriter("Trunc and load successful for prg_new_prd_calc");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Trunc and load failure for prg_new_prd_calc");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingCompPass();

            logrw.fileWriter("-------Success Update Booking Comp , Booking Pass and Booking After QT INIT Load---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Booking Comp , Booking Pass and Booking After QT INIT Load---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
//      try {
//             execQuery.runqueryUpdateBookingComp();
//            
//            logrw.fileWriter("-------Success Update Booking Comp---------");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("-------Failure Update Booking Comp---------");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
//        try {
//             execQuery.runqueryUpdateBookingPass();
//            
//            logrw.fileWriter("-------Success Update Booking Pass---------");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("-------Failure Update Booking Pass---------");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
        try {
            execQuery.setTragetTableName("dim_prg_booking_id");
            execQuery.runqueryInsertBookId();

            logrw.fileWriter("-------Success Insert for dim_prg_booking_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for dim_prg_booking_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_booking_reference");
            execQuery.runqueryInsertBookRef();

            logrw.fileWriter("-------Success Insert for dim_prg_booking_reference---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for dim_prg_booking_reference---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("prg_tills");
            execQuery.runqueryInsertTills();

            logrw.fileWriter("-------Success Insert for prg_tills---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for prg_tills---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsBooking();

            logrw.fileWriter("-------Success Update Tills for Booking---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsComp();

            logrw.fileWriter("-------Success Update Tills for Booking Comp---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking Comp---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsPass();

            logrw.fileWriter("-------Success Update Tills for Booking Pass---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking Pass---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsVehicle();

            logrw.fileWriter("-------Success Update Tills for Vehicle---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Vehicle---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingComp1();

            logrw.fileWriter("-------Success Update for Booking comp package_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update for Booking comp package_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingPass1();

            logrw.fileWriter("-------Success Update for Booking pass package_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update for Booking pass package_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_consumer_splits_id");
            execQuery.runqueryInsertPassId();

            logrw.fileWriter("Load completed for dim_prg_consumer_splits_id");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for dim_prg_consumer_splits_id");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setTragetTableName("prg_temp_pass_name");
            execQuery.runqueryqueryInsertTempPassName();

            logrw.fileWriter("Load completed for prg_temp_pass_name");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_temp_pass_name");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runqueryUpdateCompFact();

            logrw.fileWriter("Load completed for Update Comp Fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for Update Comp Fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runUpdateMaster();

            logrw.fileWriter("-------Success Update Master---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Master---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setSourceTableName("dim_prg_reservation_id");
            execQuery.setTragetTableName("dim_prg_reservation_id");

            execQuery.runTrunInsrtUpdateRev();

            logrw.fileWriter("Load completed for dim_prg_reservation_id");

        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Load Failed for dim_prg_reservation_id");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setSourceTableName("prg_referral_code_dim");
            execQuery.setTragetTableName("prg_referral_code_dim");

            execQuery.runTrunInsrtRefCode();

            logrw.fileWriter("Load completed for prg_referral_code_dim");

        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Load Failed for prg_referral_code_dim");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            logrw.fileWriter("Load Completed for QT Init");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            execQuery.runUpdateRemCommaFact();

            logrw.fileWriter("Removing Comma Successful After INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Removing Comma failed After INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsrtUpdateFact();

            logrw.fileWriter("Running of Truncate,Insert,Update Successful After INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Running of Truncate,Insert,Update failed After INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runInsertUpdatePax();

            logrw.fileWriter("Successful Truncate,Insert,Update for Pax after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update for Pax after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertConSplit();

            logrw.fileWriter("Successful Truncate,Insert for Consumer split after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update for Pax after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertUpdateVeh();

            logrw.fileWriter("Successful Truncate,Insert and Update for prg_booking_veh_type after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update Insert and Update for prg_booking_veh_type after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertUpdatePaxFName();

            logrw.fileWriter("Successful Truncate,Insert and Update for prg_tmp_pax_name_first after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update Insert and Update for prg_tmp_pax_name_first after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateFacts();

            logrw.fileWriter("Successful update for timezone after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  update for timezone after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateClient();

            logrw.fileWriter("Successful update for client type after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  update for client type after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runQueryMostValProdPack();

            logrw.fileWriter("Successful load for most Val product and package id and name INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  Successful load for most Val product and package id and name INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runQueryPaymentsDetails();

            logrw.fileWriter("Successful load for Last payment made code INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  Successful load for Last payment made code INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdatesBookingCompForAdjustments();

            logrw.fileWriter("Successful updates to list all adjustment definitions at comp and booking level after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates to list all adjustment definitions at comp and booking level after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateChildAdultInfo();

            logrw.fileWriter("Successful updates to booking,comp for Adult/Child info after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates to to booking,comp for Adult/Child info after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runPaymentUpdateForBookings();

            logrw.fileWriter("Successful updates firstPayementDate after INIT Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates firstPayementDate after INIT Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatePaymentsFactFromBookingStg();

            logrw.fileWriter("Successful updates Payments Facts from Booking Stg after INIT load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Payments Facts from Booking Stg after INIT load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runPromoCodeUpdatesForBookings();

            logrw.fileWriter("Successful updates Promo codes for booking after INIT load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Promo codes for booking after INIT load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateQRuleCountInComp();

            logrw.fileWriter("Successful updates QRule Count In Component after INIT load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates QRule Count In Component after INIT load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatePropertyIdInComp();

            logrw.fileWriter("Successful updates PropertyId In Component after INIT load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates PropertyId In Component after INIT load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateUpdateVehicleLengthInComp();

            logrw.fileWriter("Successful updates Vehicle length In Component after INIT load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Vehicle length In Component after INIT load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
    }

    public void loadCTServerInit() {

//     execQuery.setTragetConnectionString("mySql_sl");
//    execQuery.setTragetConnectionString("seaLinkdw");
        try {
//           execQuery.setSourceTableName("prg_calls_dim_stg");
            execQuery.setTragetTableName("prg_calls_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_calls_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_calls_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_calls_fact_stg");
            execQuery.setTragetTableName("prg_calls_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_calls_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_calls_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_emp_loc_stg");
            execQuery.setTragetTableName("prg_emp_loc_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_emp_loc_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_emp_loc_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_calltype_stg");
            execQuery.setTragetTableName("prg_calltype_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_calltype_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_calltype_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_locationtype_stg");
            execQuery.setTragetTableName("prg_locationtype_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_locationtype_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_locationtype_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_queueitemtypes_stg");
            execQuery.setTragetTableName("prg_queueitemtypes_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_queueitemtypes_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_queueitemtypes_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_releasetype_stg");
            execQuery.setTragetTableName("prg_releasetype_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_releasetype_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_releasetype_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_team_stg");
            execQuery.setTragetTableName("prg_team_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_team_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_team_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_extension_stg");
            execQuery.setTragetTableName("prg_extension_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_extension_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_extension_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_queue_stg");
            execQuery.setTragetTableName("prg_queue_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_queue_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_queue_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runUpdateMaster();

            logrw.fileWriter("-------Success Update Master---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Master---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            logrw.fileWriter("Load Completed for CT Server Init");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            execQuery.runInsertCallsDim();

            logrw.fileWriter("Inserting records success for prg_calls_dim after CT Server Init");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Inserting records failure for prg_calls_dim after CT Server Init");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runInsertCallsFact();

            logrw.fileWriter("Inserting records success for prg_calls_fact after CT Server Init");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Inserting records failure for prg_calls_fact after CT Server Init");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
    }

    public void loadAccpacInit() {

//       execQuery.setTragetConnectionString("mySql_sl");
//       execQuery.setTragetConnectionString("seaLinkdw");
        try {
//           execQuery.setSourceTableName("prg_fin_gl_acc_stg");
            execQuery.setTragetTableName("prg_fin_gl_accounts_data_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_fin_gl_accounts_data_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_gl_accounts_data_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_fin_gl_acc_master_stg");
            execQuery.setTragetTableName("prg_fin_gl_acc_master_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_fin_gl_acc_master_dim");
        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_gl_acc_master_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_fin_debtor_STG");
            execQuery.setTragetTableName("prg_fin_debtor_fact");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_fin_debtor_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_debtor_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
//           execQuery.setSourceTableName("prg_fin_customer_stg");
            execQuery.setTragetTableName("prg_fin_cust_master_dim");
            execQuery.runInsertViewInitRecord();

            logrw.fileWriter("Load completed for prg_fin_cust_master_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_cust_master_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runUpdateMaster();

            logrw.fileWriter("-------Success Update Master---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Master---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            logrw.fileWriter("Load Completed for Accpac Init");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

    public void loadQuickTravelIncr() {
//       execQuery.setTragetConnectionString("mySql_sl");
//       execQuery.setTragetConnectionString("seaLinkdw");
        try {
            execQuery.runUpdateRemCommaStg();

            logrw.fileWriter("Removing Comma Successful After STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Removing Comma failed After STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatebookingComments();

            logrw.fileWriter("updating public comments and private comments Successful for booking fact After QT inserting in STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("updating public comments and private comments Failed for booking fact After QT inserting in STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            execQuery.setSourceTableName("data_log_stg");
            execQuery.setTragetTableName("data_log");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for data_log");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for data_log");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("geographic_dimension_stg");
            execQuery.setTragetTableName("geographic_dimension");
            execQuery.runInsertViewInitRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Load Completed for Accpac Init");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for geographic_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_package_structure_stg");
            execQuery.setTragetTableName("prg_package_structure_fact");
            //changed on 12 feb 2013
//           execQuery.runInsertViewInitRecord();
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_package_structure_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_package_structure_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_passenger_types_dim_stg");
            execQuery.setTragetTableName("prg_passenger_types_dim");
            //changed on 12 feb 2013
//           execQuery.runInsertViewInitRecord();
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_passenger_types_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_passenger_types_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("client_dimension_stg");
            execQuery.setTragetTableName("client_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for client_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for client_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("department_dimension_stg");
            execQuery.setTragetTableName("department_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for department_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for department_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("product_dimension_stg");
            execQuery.setTragetTableName("product_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for product_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for product_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        //added on 14 Sep 2012
        try {
            execQuery.setSourceTableName("package_dimension_stg");
            execQuery.setTragetTableName("package_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for package_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for package_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_user_dimension_stg");
            execQuery.setTragetTableName("prg_user_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_user_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_user_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("vendor_dimension_stg");
            execQuery.setTragetTableName("vendor_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for vendor_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for vendor_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

//       try {
//           execQuery.runDelBooking();
//           
//        } catch (Exception execQuery1) {
//            logger.error("Exception:",execQuery1);
//            
//        }
        try {
            execQuery.setSourceTableName("prg_booking_fact_stg");
            execQuery.setTragetTableName("prg_booking_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_booking_fact");
        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_booking_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        execQuery.setSourceTableName("prg_booking_comp_fact_stg");
        execQuery.setTragetTableName("prg_booking_comp_fact");
//        try {
//           execQuery.runDelBookingBookingComp();
//           
//        } catch (Exception execQuery1) {
//            logger.error("Exception:",execQuery1);
//            
//        }
        try {
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_booking_comp_fact");
        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_booking_comp_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("payments_fact_stg");
            execQuery.setTragetTableName("payments_facts");
            //changed on 12 Jan
//           execQuery.runInsertViewInitRecord();
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for payments_facts");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for payments_facts");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("accounting_entries_stg");
            execQuery.setTragetTableName("accounting_entries_facts");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for accounting_entries_facts");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for accounting_entries_facts");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        execQuery.setSourceTableName("prg_booking_pass_stg");
        execQuery.setTragetTableName("prg_booking_pass_fact");
//       try {
//           execQuery.runDelBookingPass();
//           
//        } catch (Exception execQuery1) {
//            logger.error("Exception:",execQuery1);
//            
//        }
        try {
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_booking_pass_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_booking_pass_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_qt_account_dim_stg");
            execQuery.setTragetTableName("prg_qt_account_dim");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_qt_account_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_qt_account_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("services_stg");
            execQuery.setTragetTableName("services_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for services_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for services_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("trips_stg");
            execQuery.setTragetTableName("trips_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for trips_fact");
        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for trips_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_vehicle_components_stg");
            execQuery.setTragetTableName("prg_vehicle_components_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_vehicle_components_fact");
        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_vehicle_components_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_reservation_stg");
            execQuery.setTragetTableName("prg_adjustments_reservation_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_adjustments_reservation_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_adjustments_reservation_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_booking_stg");
            execQuery.setTragetTableName("prg_adjustments_booking_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_adjustments_booking_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_adjustments_booking_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_stg");
            execQuery.setTragetTableName("prg_adjustments_dim");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for prg_adjustments_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_adjustments_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("adjustments_dimension_stg");
            execQuery.setTragetTableName("adjustments_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for adjustments_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for prg_adjustments_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("fare_basis_sets_dimension_stg");
            execQuery.setTragetTableName("fare_basis_sets_dimension");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for fare_basis_sets_dimension");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for fare_basis_sets_dimension");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("commission_structure_stg");
            execQuery.setTragetTableName("commission_structure_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for commission_structure_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for commission_structure_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("booking_log_entries_stg");
            execQuery.setTragetTableName("booking_log_entries_fact");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for booking_log_entries_fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for booking_log_entries_fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("booking_promo_codes_stg");
            execQuery.setTragetTableName("booking_promo_codes");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for booking_promo_codes");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for booking_promo_codes");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("accomodation_properties_stg");
            execQuery.setTragetTableName("accomodation_properties");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for accomodation_properties");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for accomodation_properties");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("reservations_service_stg");
            execQuery.setTragetTableName("reservations_service");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for reservations_service");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for reservations_service");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("qrules_rules_dim_stg");
            execQuery.setTragetTableName("qrules_rules_dim");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for qrules_rules_dim");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for qrules_rules_dim");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("qrules_rules_stg");
            execQuery.setTragetTableName("qrules_rules");
            execQuery.runUpdateViewIncrRecord();
            execQuery.runTrackLoadIncr();

            logrw.fileWriter("Incremental completed for qrules_rules");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Incremental Failed for qrules_rules");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

//        try {
//            execQuery.runUpdateQtRecord();
//            
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//        }
        try {
            execQuery.runbookingInsert();

            logrw.fileWriter("Trunc and load successful for prg_new_prd_calc");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Trunc and load failure for prg_new_prd_calc");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingCompPass();

            logrw.fileWriter("-------Success Update Booking Comp , Booking Pass and Booking After QT INCR Load---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Booking Comp , Booking Pass and Booking After QT INCR Load---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
//       try {
//             execQuery.runqueryUpdateBookingComp();
//            
//            logrw.fileWriter("-------Success Update Booking Comp---------");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("-------Failure Update Booking Comp---------");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
//        try {
//             execQuery.runqueryUpdateBookingPass();
//            
//            logrw.fileWriter("-------Success Update Booking Pass---------");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("-------Failure Update Booking Pass---------");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
        try {
            execQuery.setSourceTableName("dim_prg_reservation_id");
            execQuery.setTragetTableName("dim_prg_reservation_id");

            execQuery.runTrunInsrtUpdateRev();

            logrw.fileWriter("Load completed for dim_prg_reservation_id");

        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Load Failed for dim_prg_reservation_id");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setSourceTableName("prg_referral_code_dim");
            execQuery.setTragetTableName("prg_referral_code_dim");

            execQuery.runTrunInsrtRefCode();

            logrw.fileWriter("Load completed for prg_referral_code_dim");

        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Load Failed for prg_referral_code_dim");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_booking_id");
            execQuery.runqueryInsertBookId();

            logrw.fileWriter("-------Success Insert for dim_prg_booking_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for dim_prg_booking_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_booking_reference");
            execQuery.runqueryInsertBookRef();

            logrw.fileWriter("-------Success Insert for dim_prg_booking_reference---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for dim_prg_booking_reference---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("prg_tills");
            execQuery.runqueryInsertTills();

            logrw.fileWriter("-------Success Insert for prg_tills---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Insert for prg_tills---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsBooking();

            logrw.fileWriter("-------Success Update Tills for Booking---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsComp();

            logrw.fileWriter("-------Success Update Tills for Booking Comp---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking Comp---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsPass();

            logrw.fileWriter("-------Success Update Tills for Booking Pass---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Booking Pass---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateTillsVehicle();

            logrw.fileWriter("-------Success Update Tills for Vehicle---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Tills for Vehicle---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_consumer_splits_id");
            execQuery.runqueryInsertPassId();

            logrw.fileWriter("Load completed for dim_prg_consumer_splits_id");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for dim_prg_consumer_splits_id");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runqueryUpdateBookingComp1();

            logrw.fileWriter("-------Success Update for Booking comp package_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update for Booking comp package_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingPass1();

            logrw.fileWriter("-------Success Update for Booking pass package_id---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update for Booking pass package_id---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.setTragetTableName("dim_prg_consumer_splits_id");
            execQuery.runqueryInsertPassId();

            logrw.fileWriter("Load completed for dim_prg_consumer_splits_id");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for dim_prg_consumer_splits_id");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setTragetTableName("prg_temp_pass_name");
            execQuery.runqueryqueryInsertTempPassName();

            logrw.fileWriter("Load completed for prg_temp_pass_name");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_temp_pass_name");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runqueryUpdateCompFact();

            logrw.fileWriter("Load completed for Update Comp Fact");

        } catch (Exception execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for Update Comp Fact");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runUpdateMaster();

            logrw.fileWriter("-------Success Update Master---------");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("-------Failure Update Master---------");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
//       try {
//           execQuery.setSourceTableName("dim_prg_reservation_id");
//           execQuery.setTragetTableName("dim_prg_reservation_id");
//
//           execQuery.runSelectQuery("true");
//           
//
//        } catch (SQLException execQuery1) {
//            logger.error("Exception:",execQuery1);
//            
//        }

        try {
            logrw.fileWriter("Load Completed for QT Incr");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
        try {
            execQuery.runUpdateTables();

            logrw.fileWriter("Updating status for bookings,comp,pass and vehicle successful after INCR.");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Updating status for bookings,comp,pass and vehicle failed after INCR.");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateRemCommaFact();

            logrw.fileWriter("Removing Comma Successful After INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Removing Comma failed After INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            execQuery.runTrunInsrtUpdateFact();

            logrw.fileWriter("Running of Truncate,Insert,Update Successful After  INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Running of Truncate,Insert,Update failed After INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runInsertUpdatePax();

            logrw.fileWriter("Successful Truncate,Insert,Update for Pax after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update for Pax after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertConSplit();

            logrw.fileWriter("Successful Truncate,Insert for Consumer split after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update for Consumer split after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertUpdateVeh();

            logrw.fileWriter("Successful Truncate,Insert and Update for prg_booking_veh_type after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update Insert and Update for prg_booking_veh_type after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runTrunInsertUpdatePaxFName();

            logrw.fileWriter("Successful Truncate,Insert and Update for prg_tmp_pax_name_first after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed Truncate,Insert,Update Insert and Update for prg_tmp_pax_name_first after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
//       try {
//            execQuery.runInsertClientType();
//            
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//        }
        try {
            execQuery.runUpdateFacts();

            logrw.fileWriter("Successful update for timezone after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  update for timezone after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateClient();

            logrw.fileWriter("Successful update for client type after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  update for client type after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runQueryMostValProdPack();

            logrw.fileWriter("Successful load for most Val product and package id and name INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  Successful load for most Val product and package id and name INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runQueryPaymentsDetails();

            logrw.fileWriter("Successful load for Last payment made code INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  Successful load for Last payment made code INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdateBookingPaymentsInfo();

            logrw.fileWriter("Successful update booking fact with payment info for last payments INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed  Successful update booking fact with payment info for last payments INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runqueryUpdatesBookingCompForAdjustments();

            logrw.fileWriter("Successful updates to list all adjustment definitions at comp and booking level after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates to list all adjustment definitions at comp and booking level after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateChildAdultInfo();

            logrw.fileWriter("Successful updates to booking,comp for Adult/Child info after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates to to booking,comp for Adult/Child info after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runPaymentUpdateForBookings();

            logrw.fileWriter("Successful updates firstPayementDate after INCR Load Of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates firstPayementDate after INCR Load Of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatePaymentsFactFromBookingStg();

            logrw.fileWriter("Successful updates Payments Facts from Booking Stg after INCR load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Payments Facts from Booking Stg after INCR load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runPromoCodeUpdatesForBookings();

            logrw.fileWriter("Successful updates Promo codes for booking after INCR load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Promo codes for booking after INCR load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateQRuleCountInComp();

            logrw.fileWriter("Successful updates QRule Count In Component after INCR load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates QRule Count In Component after INCR load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatePropertyIdInComp();

            logrw.fileWriter("Successful updates PropertyId In Component after INCR load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates PropertyId In Component after INCR load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateUpdateVehicleLengthInComp();

            logrw.fileWriter("Successful updates Vehicle length In Component after INCR load of QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Failed updates Vehicle length In Component after INCR load of QT");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
    }
//Changed for CCC
//   public void loadQuickTravel(String forceInit,String compId,String connName,String userName,String password,String option) {
//       execQuery.setSourceConnectMainString("quickTravel",loadType,userName,password);

    public void truncateQuickTravelStg(String option) {
        execQuery.setTragetConnectionString("seaLinkdw");
        execQuery.st_date = "2006-01-01 00:00:00";
        String stgTableArray[] = {"reservation_groups", "resources", "resource_groups", "data_log_stg", "geographic_dimension_stg", "prg_package_structure_stg", "prg_passenger_types_dim_stg", "product_dimension_stg",
            "client_dimension_stg", "package_dimension_stg", "department_dimension_stg", "prg_user_dimension_stg", "prg_booking_fact_stg",
            "prg_booking_comp_fact_stg", "payments_fact_stg", "vendor_dimension_stg", "prg_qt_account_dim_stg", "prg_booking_pass_stg",
            "services_stg", "trips_stg", "accounting_entries_stg", "prg_vehicle_components_stg", "prg_adjustments_reservation_stg",
            "prg_adjustments_booking_stg", "prg_adjustments_stg", "adjustments_dimension_stg", "fare_basis_sets_dimension_stg",
            "commission_structure_stg", "booking_log_entries_stg", "booking_promo_codes_stg", "accomodation_properties_stg", "reservations_service_stg", "qrules_rules_dim_stg", "qrules_rules_stg"};
        execQuery.runAllStgTruncate(option, stgTableArray);
    }

    public void loadQuickTravel(String forceInit, String compId, String loadType, String userName, String password, String server, String serviceId, String port, String option, String databaseName, String sourceTimezone, String targetTimezone) {
        execQuery.setSourceConnectMainString("quickTravel", loadType, userName, password, compId, server, serviceId, port, databaseName, sourceTimezone, targetTimezone);
        execQuery.setTragetConnectionString("seaLinkdw");
        execQuery.st_date = "2006-01-01 00:00:00";
//       execQuery.setTragetConnectionString("mySql_sl");
//       execQuery.setSourceConnectMainString("mySql",connName,userName,password);
        try {
            execQuery.runTruncLoad();
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        try {
            execQuery.setSourceTableName("data_log_stg");
            execQuery.setTragetTableName("data_log_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for data_log_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for data_log_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("geographic_dimension_stg");
            execQuery.setTragetTableName("geographic_dimension_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for geographic_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for geographic_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_package_structure_stg");
            execQuery.setTragetTableName("prg_package_structure_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_package_structure_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_package_structure_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_passenger_types_dim_stg");
            execQuery.setTragetTableName("prg_passenger_types_dim_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_passenger_types_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_passenger_types_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("product_dimension_stg");
            execQuery.setTragetTableName("product_dimension_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for product_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for product_Dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("client_dimension_stg");
            execQuery.setTragetTableName("client_dimension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for client_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for client_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("package_dimension_stg");
            execQuery.setTragetTableName("package_dimension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for package_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load SQLExceptionexecQuery1Failed for package_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("department_dimension_stg");
            execQuery.setTragetTableName("department_dimension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for department_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for department_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_user_dimension_stg");
            execQuery.setTragetTableName("prg_user_dimension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_user_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_user_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_booking_fact_stg");
            execQuery.setTragetTableName("prg_booking_fact_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_booking_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_booking_comp_fact_stg");
            execQuery.setTragetTableName("prg_booking_comp_fact_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_booking_comp_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_comp_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("payments_fact_stg");
            execQuery.setTragetTableName("payments_fact_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for payments_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for payments_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("vendor_dimension_stg");
            execQuery.setTragetTableName("vendor_dimension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for vendor_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for vendor_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("prg_qt_account_dim_stg");
            execQuery.setTragetTableName("prg_qt_account_dim_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_qt_account_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_qt_account_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_booking_pass_stg");
            execQuery.setTragetTableName("prg_booking_pass_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_booking_pass_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_booking_pass_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("services_stg");
            execQuery.setTragetTableName("services_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for services_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for services_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("trips_stg");
            execQuery.setTragetTableName("trips_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for trips_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

        }
        try {
            execQuery.setSourceTableName("accounting_entries_stg");
            execQuery.setTragetTableName("accounting_entries_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for accounting_entries_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for accounting_entries_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_vehicle_components_stg");
            execQuery.setTragetTableName("prg_vehicle_components_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_vehicle_components_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_vehicle_components_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_reservation_stg");
            execQuery.setTragetTableName("prg_adjustments_reservation_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_adjustments_rprg_vehicle_components_stgeservation_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_reservation_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_booking_stg");
            execQuery.setTragetTableName("prg_adjustments_booking_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_adjustments_booking_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_booking_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_adjustments_stg");
            execQuery.setTragetTableName("prg_adjustments_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_adjustments_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_adjustments_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("adjustments_dimension_stg");
            execQuery.setTragetTableName("adjustments_dimension_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for adjustments_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for adjustments_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("fare_basis_sets_dimension_stg");
            execQuery.setTragetTableName("fare_basis_sets_dimension_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for fare_basis_sets_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for fare_basis_sets_dimension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("commission_structure_stg");
            execQuery.setTragetTableName("commission_structure_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for commission_structure_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for commission_structure_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
//added on 13 Dec 2012
        try {
            execQuery.setSourceTableName("booking_log_entries_stg");
            execQuery.setTragetTableName("booking_log_entries_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for booking_log_entries_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for booking_log_entries_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        //added on 24 Aug 2013 by Nazneen
        try {
            execQuery.setSourceTableName("booking_promo_codes_stg");
            execQuery.setTragetTableName("booking_promo_codes_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for booking_promo_codes_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for booking_promo_codes_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("accomodation_properties_stg");
            execQuery.setTragetTableName("accomodation_properties_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for accomodation_properties_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for accomodation_properties_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("reservations_service_stg");
            execQuery.setTragetTableName("reservations_service_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for reservations_service_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for reservations_service_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("qrules_rules_dim_stg");
            execQuery.setTragetTableName("qrules_rules_dim_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for qrules_rules_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for qrules_rules_dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("qrules_rules_stg");
            execQuery.setTragetTableName("qrules_rules_stg");
            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for qrules_rules_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for qrules_rules_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.runUpdateQtRecord(compId, sourceTimezone, targetTimezone);

            logrw.fileWriter("Update Time zone Successful for QT");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("Update Time zone failed for QT ");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
//       try {
//            execQuery.runUpdateRemCommaStg();
//            
//            logrw.fileWriter("Removing Comma Successful After STG");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("Removing Comma failed After STG");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
//       try {
//            execQuery.runUpdatebookingComments();
//            
//            logrw.fileWriter("updating public comments and private comments Successful for booking fact After STG");
//        } catch (Exception ex) {
//            logger.error("Exception:",ex);
//            
//           try {
//               logrw.fileWriter("updating public comments and private comments Failed for booking fact After STG");
//           } catch (IOException ex1) {
//               logger.error("Exception:",ex1);
//           }
//        }
        try {
            execQuery.runqueryUpdatesCreatorUpdatorIdName();

            logrw.fileWriter("updating creator and updator name,id Successful for payments,booking,pass and comp stg After STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("updating creator and updator name,id Failed for payments,booking,pass and comp stg After STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdateTimeZonePaymentsStg();

            logrw.fileWriter("updating time zone for payments successfull After STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("updating time zone for payments Failed After STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }
        try {
            execQuery.runUpdatePackageProductInResService();

            logrw.fileWriter("updating package and product id in reservation Service successfull After STG");
        } catch (Exception ex) {
            logger.error("Exception:", ex);

            try {
                logrw.fileWriter("updating package and product id in reservation Service After STG");
            } catch (IOException ex1) {
                logger.error("Exception:", ex1);
            }
        }

        try {
            logrw.fileWriter("Load Completed for QT");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }
//commented of CCC
//    public void loadCtServer(String forceInit,String connName,String userName,String password,String option) {
//       execQuery.setSourceConnectMainString("ctServer",connName,userName,password);

    public void truncateCtServerStg(String option) {
        execQuery.setTragetConnectionString("seaLinkdw");
        String stgTableArray[] = {"prg_Calls_Dim_stg", "prg_calls_fact_stg", "prg_CallType_stg", "prg_emp_loc_stg", "prg_LocationType_stg",
            "prg_ReleaseType_stg", "prg_Queue_stg", "prg_QueueItemTypes_stg", "prg_team_stg", "prg_Extension_stg"};
        execQuery.runAllStgTruncate(option, stgTableArray);
    }

    public void loadCtServer(String forceInit, String compId, String loadType, String userName, String password, String server, String serviceId, String port, String option, String databaseName, String sourceTimezone, String targetTimezone) {
        execQuery.setSourceConnectMainString("ctServer", loadType, userName, password, compId, server, serviceId, port, databaseName, sourceTimezone, targetTimezone);
        execQuery.setTragetConnectionString("seaLinkdw");

//       execQuery.setTragetConnectionString("mySql_sl");
//       execQuery.setSourceConnectMainString("sqlserver1",connName,userName,password);

        try {
            execQuery.setSourceTableName("prg_Calls_Dim_stg");
            execQuery.setTragetTableName("prg_Calls_Dim_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_Calls_Dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_Calls_Dim_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("prg_calls_fact_stg");
            execQuery.setTragetTableName("prg_calls_fact_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_calls_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_calls_fact_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_CallType_stg");
            execQuery.setTragetTableName("prg_CallType_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_CallType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_CallType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_emp_loc_stg");
            execQuery.setTragetTableName("prg_emp_loc_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_emp_loc_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_emp_loc_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("prg_LocationType_stg");
            execQuery.setTragetTableName("prg_LocationType_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_LocationType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_LocationType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_ReleaseType_stg");
            execQuery.setTragetTableName("prg_ReleaseType_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load Completed for prg_ReleaseType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_ReleaseType_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_Queue_stg");
            execQuery.setTragetTableName("prg_Queue_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_Queue_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_Queue_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_QueueItemTypes_stg");
            execQuery.setTragetTableName("prg_QueueItemTypes_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_QueueItemTypes_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_QueueItemTypes_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_team_stg");
            execQuery.setTragetTableName("prg_team_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_team_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_team_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            execQuery.setSourceTableName("prg_Extension_stg");
            execQuery.setTragetTableName("prg_Extension_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_Extension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_Extension_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            logrw.fileWriter("Load Failed for prg_Extension_stg");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }
    }

//    public void loadAccpac(String forceInit,String connName,String userName,String password,String option) {
//    execQuery.setSourceConnectMainString("accpac",connName,userName,password);
    public void truncateAccpacStg(String option) {
        execQuery.setTragetConnectionString("seaLinkdw");
        String stgTableArray[] = {"prg_fin_GL_acc_master_stg", "prg_fin_gl_acc_stg", "prg_fin_debtor_stg", "prg_fin_customer_stg"};
        execQuery.runAllStgTruncate(option, stgTableArray);
    }

    public void loadAccpac(String forceInit, String compId, String loadType, String userName, String password, String server, String serviceId, String port, String option, String databaseName, String sourceTimezone, String targetTimezone) {
        execQuery.setSourceConnectMainString("accpac", loadType, userName, password, compId, server, serviceId, port, databaseName, sourceTimezone, targetTimezone);
        execQuery.setTragetConnectionString("seaLinkdw");

//       execQuery.setTragetConnectionString("mySql_sl");
//       execQuery.setSourceConnectMainString("sqlserver_acc",connName,userName,password);

        try {
            execQuery.setSourceTableName("prg_fin_GL_acc_master_stg");
            execQuery.setTragetTableName("prg_fin_GL_acc_master_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_fin_GL_acc_master_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_GL_acc_master_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        }
        try {
            execQuery.setSourceTableName("prg_fin_gl_acc_stg");
            execQuery.setTragetTableName("prg_fin_gl_acc_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_fin_gl_acc_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_gl_acc_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        }
        try {
            execQuery.setSourceTableName("prg_fin_debtor_stg");
            execQuery.setTragetTableName("prg_fin_debtor_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_fin_debtor_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_debtor_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }
        try {
            execQuery.setSourceTableName("prg_fin_customer_stg");
            execQuery.setTragetTableName("prg_fin_customer_stg");

            execQuery.executeHash(forceInit, compId);
            execQuery.exceuteQueries(option);

            try {
                logrw.fileWriter("Load completed for prg_fin_customer_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }

        } catch (SQLException execQuery1) {
            logger.error("Exception:", execQuery1);

            try {
                logrw.fileWriter("Load Failed for prg_fin_customer_stg");
            } catch (IOException ex) {
                logger.error("Exception:", ex);
            }
        }

        try {
            logrw.fileWriter("Load Completed for Accpac");
        } catch (IOException ex) {
            logger.error("Exception:", ex);
        }

    }

    public void runLoad(String forceInit, String forceReq, String runLoad) {
//        execQuery.setTragetConnectionString("mySql_sl");

        execQuery.setTragetConnectionString("seaLinkdw");
        execQuery.setSourceConnectString("oracle1");
        try {
            execQuery.runSelMaster(forceInit, forceReq, runLoad);

        } catch (Exception execQuery) {
            logger.error("Exception:", execQuery);
        }
    }

    public void runCargo(String ids) {
        try {
            execQuery.runCargoQuery(ids);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
    }

    public static void main(String args[]) throws SQLException {
    }
}
