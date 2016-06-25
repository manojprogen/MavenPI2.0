/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.tracker.bean;

import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.tracker.db.PbTrackerDB;

public class PbTrackerBean {

    public static Logger logger = Logger.getLogger(PbTrackerBean.class);

    public PbReturnObject getMeasures(int tableId) {
        PbTrackerDB prmpDb = new PbTrackerDB();
        PbReturnObject pbro = null;
        //////////////////////////////////////////////////////////////////////////////////.println.println("calling getList method in Master Bean");
        try {
            //////////////////////////////////////////////////////////////////////////////////.println.println("in bean==" + tableId);
            pbro = prmpDb.getMeasures(tableId);
        } catch (Exception ex) {
            //////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
        }
        return pbro;
    }

    public PbReturnObject get2DMeasures(int tableId) {
        PbTrackerDB prmpDb = new PbTrackerDB();
        PbReturnObject pbro = null;
        //////////////////////////////////////////////////////////////////////////////////.println.println("calling getList method in Master Bean");
        try {
            //////////////////////////////////////////////////////////////////////////////////.println.println("in bean==" + tableId);
            pbro = prmpDb.get2DMeasures(tableId);
        } catch (Exception ex) {
            //////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
        }
        return pbro;
    }

    public int getTackerId() {
        PbTrackerDB prmpDb = new PbTrackerDB();
        PbReturnObject pbro = null;
        int tracker_id = 0;
        //////////////////////////////////////////////////////////////////////////////////.println.println("calling getList method in Master Bean");
        try {

            tracker_id = prmpDb.getTackerId();
        } catch (Exception ex) {
            //////////////////////////////////////////////////////////////////////////////////.println.println(ex.getMessage());
        }
        return tracker_id;
    }

    public void addTrackerDetails(Session ses) {
        PbTrackerDB prmpDb = new PbTrackerDB();


        //////////////////////////////////////////////////////////////////////////////////.println.println("calling getList method in Master Bean");
        try {

            prmpDb.addTrackerDetails(ses);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }
}
