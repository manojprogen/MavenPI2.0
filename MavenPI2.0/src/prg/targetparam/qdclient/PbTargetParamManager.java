package prg.targetparam.qdclient;

import java.io.Serializable;
import org.apache.log4j.Logger;
import prg.db.PbReturnObject;
import prg.db.Session;
import prg.targetparam.qdbean.PbTargetParamBean;

public class PbTargetParamManager implements Serializable {

    private static final long serialVersionUID = 752647115987828L;
    public static Logger logger = Logger.getLogger(PbTargetParamManager.class);

    public PbReturnObject getTargetsListU(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getTargetsListU(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getDurationLovs(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getDurationLovs(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getUserBusinessGroups(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getUserBusinessGroups(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllMeasures(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getAllMeasures(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getExistedTargets(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getExistedTargets(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllMonths(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getAllMonths(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllQtrs(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getAllQtrs(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllYears(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getAllYears(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getMeasureParameters(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getMeasureParameters(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getParameterNames(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getParameterNames(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject addTargetMaster(Session ppmp) throws Exception {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.addTargetMaster(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject addParameterDetails(Session ppmp) throws Exception {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        PbReturnObject dateMembers = null;
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.addParameterDetails(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return dateMembers;
    }

    //uday
    public PbReturnObject getTargetMaster(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getTargetMaster(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getActiveAlerts(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getActiveAlerts(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void updateTarget(Session dateMems) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.updateTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void deleteTarget(Session dateMems) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.deleteTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    //uday
    public PbReturnObject copyTarget(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.copyTarget(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
        return dateMembers;
    }

    public void copyTargetDetails(Session ppmp) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetBean.copyTargetDetails(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void updateTargetStatus(Session dateMems) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.updateTargetStatus(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getMonths(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getMonths(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getQuarters(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getQuarters(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getYears(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getYears(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getRangeValues(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getRangeValues(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void copyRangeData(Session dateMems) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.copyRangeData(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void copyRangeDataDelete(Session ppmp) throws Exception {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.copyRangeDataDelete(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void copyTargetValues(Session ppmp) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Bean");
        try {
            targetBean.copyTargetValues(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void addLock(Session dateMems) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.addLock(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public void deleteLock(String userId) {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.deleteLock(userId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }

    public PbReturnObject getLock(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getLock(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getAllLocks(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getAllLocks(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getUserDetails(Session dateMems) {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getUserDetails(dateMems);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public PbReturnObject getMeasureInfo(String measureId) throws Exception {
        PbReturnObject dateMembers = null;
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            dateMembers = targetBean.getMeasureInfo(measureId);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }

        return dateMembers;
    }

    public void addTargetTimeLevels(Session ppmp) throws Exception {
        PbTargetParamBean targetBean = new PbTargetParamBean();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("calling Manager");
        try {
            targetBean.addTargetTimeLevels(ppmp);
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
        }
    }
}
