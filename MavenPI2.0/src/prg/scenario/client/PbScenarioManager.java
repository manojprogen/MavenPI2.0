package prg.scenario.client;

import java.io.Serializable;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.scenario.bean.PbScenarioBean;

public class PbScenarioManager implements Serializable {

    private static final long serialVersionUID = 2232647115569828L;
    public static Logger logger = Logger.getLogger(PbScenarioManager.class);

    public PbReturnObject getScenarioList(Session sess) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            pbro = prgPMBean.getScenarioList(sess);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void updateScenarioStatus(Session ppmp) throws Exception {

        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            prgPMBean.updateScenarioStatus(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public void updateScenarioRating(Session ppmp) throws Exception {
        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            prgPMBean.updateScenarioRating(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void addModelMaster(Session sess) throws Exception {
        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            prgPMBean.addModelMaster(sess);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getScenarioMaster(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            pbro = prgPMBean.getScenarioMaster(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllSavedSeededModels(Session ppmp) throws Exception {
        PbReturnObject pbro = new PbReturnObject();
        PbScenarioBean prgPMBean = new PbScenarioBean();
        try {
            pbro = prgPMBean.getAllSavedSeededModels(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }
}
