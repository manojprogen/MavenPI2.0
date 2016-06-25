package prg.tracker.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbTrackerResourceBundle extends ListResourceBundle
        implements Serializable {

    public PbTrackerResourceBundle() {
    }

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getMeasures", "select * from prg_report_table_details where is_view_by_column='N' and table_id=&"}, {"get2DMeasures", "select default_measure from prg_report_table_master where table_id=&"}, {"getTackerId", "select PRG_TRACKER_MASTER_SEQ.nextval from dual"},
        {"addTrackerDetails", "insert into prg_tracker_master(TRACKER_ID,TRACKER_NAME,START_DATE,END_DATE,PRIMARY_PARAMETERS,SECONDARY_PARAMETERS,MEASURE,SUBSCRIBERS,CONDITION,REPORT_TYPE,REPORTID,TRACKER_TIME,TRACKER_DATE,TRACKER_MONTH,EMAILORSMS) values (&,'&',TO_DATE('&','MM-dd-yy'),TO_DATE('&','MM-dd-yy'),'&','&','&','&','&','&',&,'&','&','&','&')"},};
}
