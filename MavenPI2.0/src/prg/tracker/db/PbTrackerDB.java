package prg.tracker.db;

import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.tracker.params.PbTrackerParams;

public class PbTrackerDB extends PbDb {

    public static Logger logger = Logger.getLogger(PbTrackerDB.class);
    private ResourceBundle resBundle = null;

    public PbReturnObject getMeasures(int tableId) {
        PbReturnObject pbro = null;
        PbTrackerResourceBundle resBundle = new PbTrackerResourceBundle();
        String query = resBundle.getString("getMeasures");
        //////////////////////////////////////////////////////////////////////////////////.println.println("in db=" + tableId);
        Object[] obj = new Object[1];
        obj[0] = Integer.valueOf(tableId);
        String finalQuery = buildQuery(query, obj);
        //////////////////////////////////////////////////////////////////////////////////.println.println("final query------" + finalQuery);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            //////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
        }
        return pbro;
    }

    public PbReturnObject get2DMeasures(int tableId) {
        PbReturnObject pbro = null;
        PbTrackerResourceBundle resBundle = new PbTrackerResourceBundle();
        String query = resBundle.getString("get2DMeasures");
        //////////////////////////////////////////////////////////////////////////////////.println.println("in db=" + tableId);
        Object[] obj = new Object[1];
        obj[0] = Integer.valueOf(tableId);
        String finalQuery = buildQuery(query, obj);
        //////////////////////////////////////////////////////////////////////////////////.println.println("final query------" + finalQuery);
        try {
            pbro = execSelectSQL(finalQuery);
        } catch (Exception ex) {
            //////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
        }
        return pbro;
    }

    public int getTackerId() {
        PbReturnObject pbro = null;
        int tracker_id = 0;
        PbTrackerResourceBundle resBundle = new PbTrackerResourceBundle();
        String query = resBundle.getString("getTackerId");

        try {
            pbro = execSelectSQL(query);
            tracker_id = pbro.getFieldValueInt(0, "NEXTVAL");
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return tracker_id;
    }

    public void addTrackerDetails(Session atd) {
        try {
            PbTrackerParams params = (PbTrackerParams) atd.getObject("prg.tracker.params.PbTrackerParams");
            PbTrackerResourceBundle resBundle = new PbTrackerResourceBundle();
            String query = resBundle.getString("addTrackerDetails");
            Object[] obj = new Object[15];
            obj[0] = Integer.valueOf(params.getTrackerId());
            obj[1] = params.getTrackerName();
            obj[2] = params.getStartDate();
            obj[3] = params.getEndDate();
            obj[4] = params.getPrimaryParameters();
            obj[5] = "";
            obj[6] = params.getMeasure();
            obj[7] = params.getSubscribers();
            obj[8] = params.getCondition();
            obj[9] = params.getReportType();
            obj[10] = params.getReportId();
            obj[11] = params.getTime();
            obj[12] = params.getDate();
            obj[13] = params.getMonth();
            obj[14] = params.getEorM();
            String finalQuery = buildQuery(query, obj);
            //////////////////////////////////////////////////////////////////////////////////.println.println("INSERTION QUERY IS " + finalQuery);
            execModifySQL(finalQuery);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }
}
