/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.reportscheduler;

import com.progen.userlayer.db.LogReadWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import utils.db.ProgenConnection;

/**
 *
 * @author Mohit //for campbell etl
 */
public class RunMultiProcedure extends Thread {

    public static Logger logger = Logger.getLogger(RunMultiProcedure.class);
//    public static String proceduresNames[]={"prg_cs_app_user_dim_ini"
//            ,"prg_cs_payment_status_dim_ini","prg_cs_bank_dim_ini",
//    "prg_cs_purchase_type_dim_ini","prg_cs_fiscalyr_dim_ini","prg_cs_fiscal_cal_dim_ini","prg_cs_document_type_master_ini","prg_cs_unit_dim_ini",
//            "prg_cs_po_revision_dim_ini","prg_cs_po_receipt_dim_ini","prg_cs_po_delivery_dim_ini","prg_cs_doc_status_dim_ini"};
//public static String IndependentProcs[]={"prg_cs_app_user_dim_ini"
//,"prg_cs_ap_account_dim_ini"
//,"prg_cs_bank_dim_ini"
//,"prg_cs_purchase_type_dim_ini"
//,"prg_cs_fiscalyr_dim_ini"
//,"prg_cs_fiscal_cal_dim_ini"
//,"prg_cs_vendor_new_dim_ini"
//,"interim_cs_doc_status_dates_stg_ini"
//,"prg_cs_equipment_dim_ini"
//,"prg_cs_document_type_master_ini"
//,"prg_cs_unit_dim_ini"
//,"prg_cs_rfq_dim_ini"
//,"prg_cs_requisition_dim_ini"
//,"prg_cs_remarks_dim_ini"
//,"prg_cs_po_revision_dim_ini"
//,"prg_cs_po_revision_dim_ini"
//,"prg_cs_remarks_dim_ini"
//,"prg_cs_po_receipt_dim_ini"
//,"prg_cs_po_delivery_dim_ini"
//,"prg_cs_po_dim_ini"
//,"prg_cs_part_dim_ini"
//,"prg_cs_invoice_dim_ini"
//,"prg_cs_event_dim_ini"
//,"prg_cs_doc_status_dim_ini"
//,"prg_cs_currency_dim_ini"
//,"prg_cs_illness_injuries_dim_ini"
//,"prg_cs_manning_crew_dim_ini"
//,"prg_cs_manning_crew_position_dim_ini"
//,"prg_cs_manning_issuecountry_dim_ini"
//,"prg_cs_manning_perf_attr_dim_ini"
//,"prg_cs_manning_provider_dim_ini"
//,"prg_cs_manning_qual_remarks_dim_ini"
//,"prg_cs_manning_qualification_dim_ini"
//,"prg_cs_manning_med_req_fact_ini"
//,"prg_cs_manning_perf_fact_ini"
//,"prg_cs_manning_service_fact_ini"
//,"prg_cs_manning_qualification_fact_ini"
//,"prg_cs_manning_rank_dim_ini"
//,"prg_cs_manning_rating_dim_ini"
//,"prg_cs_manning_serv_reason_dim_ini"
//,"prg_cs_manning_service_type_dim_ini"
//,"prg_cs_manning_srv_remarks_dim_ini"
//,"prg_cs_manning_visa_dim_ini"
//,"prg_cs_medical_request_dim_ini"
//,"prg_cs_seaman_division_dim_ini"
//,"prg_cs_standard_job_status_dim_ini"
//,"prg_cs_standard_job_position_dim_ini"
//,"prg_cs_spaces_dim_ini"
//,"prg_cs_sche_basis_dim_ini"
//,"prg_cs_mt_jobs_dim_ini"
//,"prg_cs_maintained_part_dim_ini"
//,"prg_cs_job_type_dim_ini"
//,"prg_cs_job_status_dim_ini"
//,"prg_cs_job_intervals_dim_ini"
//,"prg_cs_job_description_indxtrm_dim_ini"
//,"prg_cs_job_constraint_dim_ini"
//,"prg_cs_job_cat_dim_ini"
//,"prg_cs_execute_method_dim_ini"
//,"prg_cs_dept_idxtrm_dim_ini"
//,"prg_cs_maintenance_fact_ini"
//,"prg_cs_index_term_dim_ini"
//,"prg_cs_car_dept_dim_ini"
//,"prg_cs_incident_type_dim_ini"
//,"prg_cs_address_dim_ini"
//,"prg_cs_ship_dim_ini"
//,"prg_cs_city_dim_ini"
//,"prg_cs_incident_fact_ini"
//,"prg_cs_inspection_fact_ini"
//,"prg_cs_audit_fact_ini"
//,"prg_cs_car_fact_ini"
//,"prg_cs_doc_text_details_dim_ini"};
    LogReadWriter logrw = new LogReadWriter();
    public static String filename = "";
    public static String IndependentProcs[] = {"call_independent_load1",
        "call_independent_load2",
        "call_independent_load3",
        "call_independent_load4",
        "call_independent_load5"};
//public static String dependentProcs[]={"prg_cs_car_dim_ini"
//,"prg_cs_audit_dim_ini"
//,"prg_cs_incident_dim_ini"
//,"prg_cs_inspection_dim_ini"
//,"prg_cs_standard_jobs_dim_ini"
//,"prg_cs_manning_headcount_fact_ini"
//,"prg_cs_manning_qual_dim_ini"
//,"prg_cs_document_trail_dim_ini"
//,"prg_cs_budget_fact_ini"
//,"prg_cs_ap_invoice_det_fact_ini"
//,"prg_cs_ap_payment_fact_ini"
//,"prg_cs_payment_status_dim_ini"
//,"prg_cs_invoice_payment_dim_ini"
//,"prg_cs_po_delivery_item_fact_ini"
//,"prg_cs_rfq_service_item_fact_ini"
//,"prg_cs_po_service_item_fact_ini"
//,"prg_cs_po_receipt_item_fact_ini"
//,"prg_cs_po_base_fact_ini"
//,"prg_cs_invoice_fact_ini"
//,"prg_cs_rfq_fact_ini"
//,"prg_cs_requisition_fact_ini"
//,"prg_cs_ns5_accpac_upd_ini"};
    public static String dependentProcs[] = {"call_dependent_load"};
    public static int totalproc = IndependentProcs.length;
    public static int objectNo = -1;
    public static Connection con = null;
//public static CallableStatement proc[] = new CallableStatement[totalproc] ;

    public void run() {
        try {
//             
//            String tempproc="{ call cs_dw."+proceduresNames[objectNo]+"() }";
////            proc[objectNo] = con.prepareCall(tempproc);
//            boolean execute = proc[objectNo].execute();
//              
////             
            createThreads();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
//

// class TestThread {
    public void createThreads() throws ClassNotFoundException {
//
//      CallableStatement proc = con.prepareCall(tempproc)
//            String tempproc="{ call cs_dw."+proceduresNames[objectNo]+"() }";
        CallableStatement proc = null;
        Connection con1 = null;

        try {
//              Class.forName("com.mysql.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
//       con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/META_CS?useServerPrepStmts=false&rewriteBatchedStatements=true","root","root");
            con1 = ProgenConnection.getInstance().getConnection();
            incrementCount(proc);
            //
            proc = con1.prepareCall("{ call cs_dw." + IndependentProcs[objectNo] + "() }");

            proc.execute();
            con1.close();
//      con.close();
//            
//                objectNo++;
        } catch (SQLException ex) {
            logger.error("Exception: ", ex);
        }
    }
//  public void createThreads2() {
////
////      CallableStatement proc = con.prepareCall(tempproc)
////            String tempproc="{ call cs_dw."+proceduresNames[objectNo]+"() }";
//      Date date = new Date();
//                          
//               for (int x=0; x<RunMultiProcedure.totalproc; x++)
//             {
//          CallableStatement  proc;
//        try {
//            // incrementCount();
//             Connection con2 = ProgenConnection.getInstance().getConnection();
////            
//            proc = con2.prepareCall("{ call cs_dw." + IndependentProcs[objectNo] + "() }");
//
//      proc.execute();
//       con2.close();
////            
////                objectNo++;
//            }
//
//         catch (SQLException ex) {
//            logger.error("Exception: ",ex);
//        }
//    }
//   Date date1 = new Date();
//                          
//
//  }

    public synchronized void incrementCount(CallableStatement proc) {
//              
        objectNo++;
        try {
            //          Class.forName("com.mysql.jdbc.Driver");
            //            connection = DriverManager.getConnection("jdbc:mysql://192.168.0.125:3306/quick", "root", "welcome");
            //        Connection con1 = DriverManager.getConnection("jdbc:mysql://183.82.3.61:3306/META_CS?useServerPrepStmts=false&rewriteBatchedStatements=true","root","root");
            //proc = con.prepareCall("{ call cs_dw." + IndependentProcs[objectNo] + "() }");

            logrw.fileWriterWithFileName("{ call cs_dw." + IndependentProcs[objectNo] + "() }", filename);
//                          
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        // return proc;

    }
//              
//             
//    Scanner input = new Scanner(System.in);
//    
//    int n = input.nextInt();
//    
// RunMultiProcedure temp[]=null;
//    for (int x=0; x<100; x++)
//    {
//        
////        temp[x]= new RunMultiProcedure();
////        temp[x].start();
////        
//    }
}