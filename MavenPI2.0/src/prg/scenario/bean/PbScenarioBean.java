package prg.scenario.bean;

import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.scenario.db.PbScenarioDb;

public class PbScenarioBean {

    public static Logger logger = Logger.getLogger(PbScenarioBean.class);

    public PbReturnObject getScenarioList(Session sess) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = scnDb.getScenarioList(sess);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public void addModelMaster(Session sess) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        try {
            scnDb.addModelMaster(sess);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public void updateScenarioRating(Session ppmp) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        try {
            scnDb.updateScenarioRating(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateScenarioStatus(Session ppmp) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        try {
            scnDb.updateScenarioStatus(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getScenarioMaster(Session ppmp) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = scnDb.getScenarioMaster(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }

    public PbReturnObject getAllSavedSeededModels(Session ppmp) throws Exception {
        PbScenarioDb scnDb = new PbScenarioDb();
        PbReturnObject pbro = new PbReturnObject();
        try {
            pbro = scnDb.getAllSavedSeededModels(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return pbro;
    }
}
