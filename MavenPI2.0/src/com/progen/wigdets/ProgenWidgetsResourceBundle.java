/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.wigdets;
import java.io.Serializable;
import java.util.ListResourceBundle;
/**
 *
 * @author Saurabh
 */
public class ProgenWidgetsResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getFavReports", "SELECT DISTINCT a.report_name,b.report_id,b.pur_report_sequence FROM prg_ar_report_master a,prg_ar_user_reports b WHERE b.user_id=& AND a.report_id=b.report_id AND b.pur_fav_report ='Y' ORDER BY b.pur_report_sequence"},
        {"getAllReports", "select DISTINCT nvl(b.PUR_CUST_REPORT_NAME,a.report_name) A1, b.report_id,b.pur_fav_report,b.pur_report_sequence  from prg_ar_report_master a, prg_ar_user_reports b "
            + " WHERE b.user_id=& and a.report_id= b.report_id order by b.pur_report_sequence,(nvl(b.PUR_CUST_REPORT_NAME,a.report_name)) "},
        {"saveFavReport1", "update prg_ar_user_reports set pur_fav_report = 'Y' WHERE report_id in (&) AND user_id=&"},
        {"saveFavReport2", "update prg_ar_user_reports set pur_fav_report = 'N' WHERE report_id not in (&) AND user_id=&"} //       , {"getFavouriteReports","SELECT DISTINCT nvl(b.PUR_CUST_REPORT_NAME,a.report_name),b.report_id,b.pur_report_sequence,a.report_type FROM prg_ar_report_master a,prg_ar_user_reports b WHERE b.user_id=& AND a.report_id=b.report_id AND b.pur_fav_report ='Y' ORDER BY b.pur_report_sequence"},
        , {"getFavouriteReports","SELECT DISTINCT a.report_name,b.report_id,b.pur_report_sequence,a.report_type,a.report_desc FROM prg_ar_report_master a,prg_ar_user_reports b WHERE b.user_id=& AND a.report_id=b.report_id AND b.pur_fav_report ='Y' ORDER BY b.pur_report_sequence"},
    };
}
