/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.tracker.client;

import java.io.Serializable;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.tracker.bean.PbTrackerBean;

public class PbTrackerManager implements Serializable {

    private static final long serialVersionUID = 752647115569820768L;
    public static Logger logger = Logger.getLogger(PbTrackerManager.class);

    public PbReturnObject getMeasures(int tableId) {
        PbReturnObject pbro = null;
        PbTrackerBean prgRMBean = new PbTrackerBean();
        try {
            //////////////////////////////////////////////////////////////////////////////////.println.println("IN CLIENT" + tableId);
            pbro = prgRMBean.getMeasures(tableId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject get2DMeasures(int tableId) {
        PbReturnObject pbro = null;
        PbTrackerBean prgRMBean = new PbTrackerBean();
        try {
            //////////////////////////////////////////////////////////////////////////////////.println.println("IN CLIENT" + tableId);
            pbro = prgRMBean.get2DMeasures(tableId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public int getTackerId() {
        PbReturnObject pbro = null;
        int tracker_id = 0;
        PbTrackerBean prgRMBean = new PbTrackerBean();
        try {

            tracker_id = prgRMBean.getTackerId();
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return tracker_id;
    }

    public void addTrackerDetails(Session sess) {
        PbTrackerBean prgRMBean = new PbTrackerBean();
        try {
            prgRMBean.addTrackerDetails(sess);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }
}
