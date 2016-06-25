package com.progen.wigdets;

import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

public class ProgenWidgetsDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ProgenWidgetsDAO.class);
    ResourceBundle resourceBundle;
//    ProgenWidgetsResourceBundle resBundle = new ProgenWidgetsResourceBundle();

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ProgenWidgetsResBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new ProgenWidgetsResourceBundleMySql();
            } else {
                resourceBundle = new ProgenWidgetsResourceBundle();
            }
        }

        return resourceBundle;
    }

    public PbReturnObject getFavReports(int userId) {
        String sqlQuery = getResourceBundle().getString("getFavouriteReports");
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("userId-->"+userId);
        String finalQuery = "";
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQuery = buildQuery(sqlQuery, obj);
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("getFavReports-->"+finalQuery);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getFavReports1(String userId) {
        String sqlQuery = getResourceBundle().getString("getFavouriteReports");

        String finalQuery = "";
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQuery = buildQuery(sqlQuery, obj);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getAllReports(int userId) {
        String sqlQuery = getResourceBundle().getString("getAllReports");
        String finalQuery = "";
        Object[] obj = new Object[1];
        obj[0] = userId;
        finalQuery = buildQuery(sqlQuery, obj);
//        if(ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)){
//            {
//           finalQuery = finalQuery.replace("nvl", "ISNULL");//temporary fix
//           finalQuery = finalQuery.replace("Nvl", "ISNULL");//temporary fix
//           finalQuery = finalQuery.replace("NVL", "ISNULL");//temporary fix
//        }
//        }
        //////.println("getAllReports-->"+finalQuery);
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception: ", e);
            retObj = null;
        }
        return retObj;
    }

    public void saveFavReport(String reportIds, String userId) {
        String query1 = getResourceBundle().getString("saveFavReport1");
        String query2 = getResourceBundle().getString("saveFavReport2");

        Object[] obj1 = new Object[2];
        obj1[0] = reportIds;
        obj1[1] = userId;

        Object[] obj2 = new Object[2];
        obj2[0] = reportIds;
        obj2[1] = userId;
        ArrayList a = new ArrayList();
        String finalQuery1 = buildQuery(query1, obj1);
        a.add(finalQuery1);
        finalQuery1 = buildQuery(query2, obj2);
        a.add(finalQuery1);

        try {
            executeMultiple(a);
        } catch (Exception e) {
            logger.error("Exception: ", e);
        }

    }

    public boolean savePriorityLinks(ArrayList a) {
        boolean status = false;
        try {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                status = executeMultiple(a);
            } else {
                status = execMultiple(a);
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
        return status;
    }

    public void deletePersonalisedReports(int userId, String reports) {
        String query = "delete from PRG_AR_PERSONALIZED_REPORTS where PRG_PERSONALIZED_ID in (" + reports + ")";
        //////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("DELETE QUERY IS--->"+query);
        try {
            execModifySQL(query);

        } catch (Exception e) {
            logger.error("Exception: ", e);
        }
    }
}
