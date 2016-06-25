package prg.targetparam.qdbean;

import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.targetparam.qddb.PbTargetParamDb;

public class PbTargetParamBean {

    public static Logger logger = Logger.getLogger(PbTargetParamBean.class);

    public PbReturnObject getTargetsListU(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        //////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getTargetsListU(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getDurationLovs(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getDurationLovs(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getUserBusinessGroups(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getUserBusinessGroups(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllMeasures(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getAllMeasures(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getExistedTargets(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getExistedTargets(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllMonths(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getAllMonths(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllQtrs(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getAllQtrs(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllYears(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getAllYears(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getMeasureParameters(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getMeasureParameters(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getParameterNames(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getParameterNames(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject addTargetMaster(Session ppmp) throws Exception {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.addTargetMaster(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject addParameterDetails(Session ppmp) throws Exception {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        PbReturnObject dateMembers = null;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.addParameterDetails(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return dateMembers;

    }

    //uday
    public void deleteLock(String userId) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.deleteLock(userId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getTargetMaster(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getTargetMaster(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getActiveAlerts(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getActiveAlerts(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void updateTarget(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.updateTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void deleteTarget(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.deleteTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    //uday
    public PbReturnObject copyTarget(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        PbReturnObject dateMembers = null;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.copyTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return dateMembers;
    }

    public void copyTargetDetails(Session ppmp) {
        PbTargetParamDb ppmpDb = new PbTargetParamDb();
        try {
            ppmpDb.copyTargetDetails(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateTargetStatus(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.updateTargetStatus(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getMonths(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getMonths(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getQuarters(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getQuarters(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getYears(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getYears(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getRangeValues(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getRangeValues(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void copyRangeData(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.copyRangeData(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void copyRangeDataDelete(Session ppmp) throws Exception {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.copyRangeDataDelete(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

    }

    public void copyTargetValues(Session ppmp) {
        PbTargetParamDb ppmpDb = new PbTargetParamDb();
        try {
            ppmpDb.copyTargetValues(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void addLock(Session dateMems) {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.addLock(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getLock(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getLock(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllLocks(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getAllLocks(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getUserDetails(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getUserDetails(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getMeasureInfo(String measureId) throws Exception {
        PbReturnObject dateMembers = null;
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            dateMembers = targetDb.getMeasureInfo(measureId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void addTargetTimeLevels(Session ppmp) throws Exception {
        PbTargetParamDb targetDb = new PbTargetParamDb();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetDb.addTargetTimeLevels(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
}
