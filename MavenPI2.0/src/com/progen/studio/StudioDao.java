/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.studio;

//import com.progen.log.ProgenLog;
import com.progen.search.suggest.AutoSuggest;
import com.progen.search.suggest.SearchSuggestion;
import com.progen.sentiment.analysis.SentimentAnalysisDAO;
import com.progen.users.PrivilegeManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class StudioDao extends PbDb {

    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(StudioDao.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new StudioResourceBundleSqlServer();
            } else {
                resourceBundle = new StudioResourceBundle();
            }
        }
        return resourceBundle;

    }

    public PbReturnObject getUserReports(String userId) {
//        ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "Enter userId " + userId);
        logger.info("Enter userId " + userId);
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserReports");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";

        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "foldersQuery " + foldersQuery);
            logger.info("foldersQuery " + foldersQuery);
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                }
                folderString = folderString.substring(1);
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
//            ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "sqlQuery " + sqlQuery);
            logger.info("sqlQuery " + sqlQuery);
        } catch (Exception e) {
            retObj = null;
//            ProgenLog.log(ProgenLog.SEVERE, this, "getUserReports", "Exception " + e.getMessage());
            logger.error("Exception:", e);
        }
        //retObj.writeString();
//        ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "Exit");
        logger.info("Exit ");
        return retObj;
    }

    public PbReturnObject getUserDashboards(String userId) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserDashboards");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";

        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, "FOLDER_ID");
                }
                folderString = folderString.substring(1);
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
            retObj = null;
        }
        //retObj.writeString();
        return retObj;
    }

    public PbReturnObject getUserKPIDashboards(String userId) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserKPIDashboards");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";

        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, "FOLDER_ID");
                }
                folderString = folderString.substring(1);
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            // 
            retObj = execSelectSQL(sqlQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
            retObj = null;
        }
        //retObj.writeString();
        return retObj;
    }

//      public AutoSuggest giveSuggestions(String searchKey,String userId)
//    {
//
//        String qryStringformatted = searchKey.toUpperCase() + "%";
//        //String suggestQry = "select REPORT_NAME from PRG_AR_REPORT_MASTER where  UPPER(REPORT_NAME) LIKE '&' order by 1";
//        String suggestQry =getResourceBundle().getString("getSearchReports");
//
//        PbDb pbDb = new PbDb();
//        Object[] bind = new Object[1];
//        bind[0] = qryStringformatted;
//
//        AutoSuggest suggestion= new AutoSuggest(searchKey);
//        SearchSuggestion searchSuggestion;
//        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
//         ArrayList<AutoSuggest> autoSuggestionLst = new ArrayList<AutoSuggest>();
//        String finalQuery = pbDb.buildQuery(suggestQry, bind);
//        try
//        {
//            PbReturnObject suggRetObj = pbDb.execSelectSQL(finalQuery);
//            for ( int i=0; i<suggRetObj.getRowCount(); i++ )
//            {
//               searchSuggestion=new SearchSuggestion();
//               searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "REPORT_NAME"));
//               srchSuggestionLst.add(searchSuggestion);
//
//            }
//             suggestion.setSuggestionList(srchSuggestionLst);
//               // autoSuggestionLst.add(suggestion);
//        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "giveSuggestions", "Give Suggestion Exception "+ex.getMessage());
//        }
//        return suggestion;
//    }
    public AutoSuggest giveSuggestions(String searchKey, String userId, String fromTab) {
        String suggestQry = "";
        String qryStringformatted = searchKey.toUpperCase() + "%";
        //String suggestQry = "select REPORT_NAME from PRG_AR_REPORT_MASTER where  UPPER(REPORT_NAME) LIKE '&' order by 1";
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        if (fromTab.equalsIgnoreCase("Report")) {
            suggestQry = getResourceBundle().getString("getSearchReports");
        } else if (fromTab.equalsIgnoreCase("AO")) {
            suggestQry = getResourceBundle().getString("getSearchAOs");
        } else if (fromTab.equalsIgnoreCase("Dashboard")) {
            suggestQry = getResourceBundle().getString("getSearchDashboards");
        } else if (fromTab.equalsIgnoreCase("MyReport")) {
            suggestQry = getResourceBundle().getString("getSearchAllRepsAndDashboards");
        
        } else if (fromTab.equalsIgnoreCase("MgmtTemplate")) {
            suggestQry = getResourceBundle().getString("getSearchTemplate");
        }

        AutoSuggest suggestion = new AutoSuggest(searchKey);
        SearchSuggestion searchSuggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
        ArrayList<AutoSuggest> autoSuggestionLst = new ArrayList<AutoSuggest>();

        PbReturnObject suggRetObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";

        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getSearchReports", "foldersQuery " + foldersQuery);
            logger.info("foldersQuery " + foldersQuery);
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                }
                folderString = folderString.substring(1);
            }


            if (fromTab.equalsIgnoreCase("AO")) {

                Object[] AOvalues = new Object[2];
                AOvalues[0] = userId;
                AOvalues[1] = qryStringformatted.replace("'", "''");
                suggestQry = buildQuery(suggestQry, AOvalues);
                suggRetObj = execSelectSQL(suggestQry);
                for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                    searchSuggestion = new SearchSuggestion();
                    searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "AO_NAME"));
                    srchSuggestionLst.add(searchSuggestion);
                }

            }else if(fromTab.equalsIgnoreCase("MgmtTemplate")){
                Object[] AOvalues = new Object[2];
                AOvalues[0] = userId;
                AOvalues[1] = qryStringformatted.replace("'", "''");
                suggestQry = buildQuery(suggestQry, AOvalues);
                suggRetObj = execSelectSQL(suggestQry);
                for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                searchSuggestion = new SearchSuggestion();
                    searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "TEMPLATE_NAME"));
                    srchSuggestionLst.add(searchSuggestion);
                }
            }
            else {
                Object[] values = new Object[4];
                values[0] = folderString;
                values[1] = userId;
                values[2] = folderString;
                values[3] = qryStringformatted.replace("'", "''");
                suggestQry = buildQuery(suggestQry, values);
                suggRetObj = execSelectSQL(suggestQry);
                for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                    searchSuggestion = new SearchSuggestion();
                    searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "REPORT_NAME"));
                    srchSuggestionLst.add(searchSuggestion);
                }

            }
            suggestion.setSuggestionList(srchSuggestionLst);
//            ProgenLog.log(ProgenLog.FINE, this, "getSearchReports", "suggestQry " + suggestQry);
            logger.info("suggestQry " + suggestQry);
        } catch (Exception e) {
            suggRetObj = null;
//            ProgenLog.log(ProgenLog.SEVERE, this, "getSearchReports", "Exception " + e.getMessage());
            logger.error("Exception:", e);
        }

        return suggestion;
    }

    public PbReturnObject getUserScorecards(String userid) {

        String sqlQuery = getResourceBundle().getString("getScorecards");
        PbReturnObject retObj = null;
        Object[] values = new Object[1];
        values[0] = userid;
        sqlQuery = buildQuery(sqlQuery, values);
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public AutoSuggest giveScorecardSuggestions(String searchKey, String userId) {
        String suggestQry = "";
        String qryStringformatted = searchKey.toUpperCase() + "%";
        suggestQry = getResourceBundle().getString("getSearchScorecards");

        AutoSuggest suggestion = new AutoSuggest(searchKey);
        SearchSuggestion searchSuggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
        ArrayList<AutoSuggest> autoSuggestionLst = new ArrayList<AutoSuggest>();
        PbReturnObject suggRetObj = null;
        Object[] values = new Object[2];
        values[0] = userId;
        values[1] = qryStringformatted;

        try {
            suggestQry = buildQuery(suggestQry, values);
            suggRetObj = execSelectSQL(suggestQry);
            for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                searchSuggestion = new SearchSuggestion();
                searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "SCARD_NAME"));
                srchSuggestionLst.add(searchSuggestion);

            }
            suggestion.setSuggestionList(srchSuggestionLst);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return suggestion;
    }

    public PbReturnObject getAllReportshome(String userid) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = null;
        if (PrivilegeManager.isModuleComponentEnabledForUser("REPDESIGNER", "DASHBOARDVIEWER", Integer.parseInt(userid))) {
            sqlQuery = getResourceBundle().getString("getAllRepsAndDashboards");
        } else {
            sqlQuery = getResourceBundle().getString("getAllReportshome");
        }

        PbReturnObject retFolderObj = null;
        String folderString = "";



        PbReturnObject retObj = null;
        try {
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, "FOLDER_ID");
                }
                folderString = folderString.substring(1);
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userid;
            values[2] = folderString;
            String finalQuery = buildQuery(sqlQuery, values);

            retObj = execSelectSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
            retObj = null;
        }
        return retObj;
    }

    public PbReturnObject getUserSnapshots(String userid) {
        String sqlQuery = getResourceBundle().getString("getSnapshots");
        PbReturnObject retObj = null;
        Object[] values = new Object[1];
        values[0] = userid;

        try {
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }

    public AutoSuggest giveHtmlReportsSuggestions(String searchKey, String userId) {
        String suggestQry = "";
        String qryStringformatted = searchKey.toUpperCase() + "%";
        suggestQry = getResourceBundle().getString("getSearchSnapshots");

        AutoSuggest suggestion = new AutoSuggest(searchKey);
        SearchSuggestion searchSuggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
        ArrayList<AutoSuggest> autoSuggestionLst = new ArrayList<AutoSuggest>();
        PbReturnObject suggRetObj = null;
        Object[] values = new Object[2];
        values[0] = userId;
        values[1] = qryStringformatted;

        try {
            suggestQry = buildQuery(suggestQry, values);
            suggRetObj = execSelectSQL(suggestQry);
            for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                searchSuggestion = new SearchSuggestion();
                searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "DATA_SNAPSHOT_NAME"));
                srchSuggestionLst.add(searchSuggestion);

            }
            suggestion.setSuggestionList(srchSuggestionLst);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return suggestion;
    }

    public AutoSuggest giveSentimentSuggestions(String searchKey, String userId, String column) {
        String suggestQry = "";
        String qryStringformatted = "";
        qryStringformatted = searchKey.toUpperCase() + "%";
        suggestQry = getResourceBundle().getString("getSentimentSugg");
        SentimentAnalysisDAO dao = new SentimentAnalysisDAO();
        String connectionId = dao.getConnectionId();
        Connection con = ProgenConnection.getInstance().getConnectionByConId(connectionId);
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        SearchSuggestion searchSuggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
        ArrayList<AutoSuggest> autoSuggestionLst = new ArrayList<AutoSuggest>();
        PbReturnObject suggRetObj = null;
        Object[] values = new Object[3];
        values[0] = column;
        values[1] = column;
        values[2] = qryStringformatted;

        try {
            suggestQry = buildQuery(suggestQry, values);

            suggRetObj = execSelectSQL(suggestQry, con);
            for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                searchSuggestion = new SearchSuggestion();
                searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, column));
                srchSuggestionLst.add(searchSuggestion);

            }
            suggestion.setSuggestionList(srchSuggestionLst);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }

        return suggestion;
    }

    public AutoSuggest giveReportSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = giveSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }

    public AutoSuggest giveAOSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = giveSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }

    public AutoSuggest giveDashboardSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = giveSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }

    public AutoSuggest giveMyReportSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = giveSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }

    public AutoSuggest givePortalsSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = givePortalSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }

    public AutoSuggest givePortalSuggestions(String searchKey, String userId, String fromTab) {
        AutoSuggest suggestion = new AutoSuggest(searchKey);
        String suggestQry = "";
        String qryStringformatted = searchKey.toUpperCase() + "%";
        suggestQry = "select PORTLET_ID, PORTLET_NAME, PORTLET_DESC, PORTLET_TYPE, PORTLET_REPORT_TYPE, PU_FIRSTNAME, UPDATE_ON from PRG_BASE_PORTLETS_MASTER,"
                + "PRG_AR_USERS where PU_ID=UPDATE_BY AND XML_PATH IS NOT NULL AND upper(PORTLET_NAME) like upper('" + qryStringformatted + "') order by upper(PORTLET_NAME) asc";
        PbReturnObject suggRetObj = null;
        SearchSuggestion searchSuggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();

        try {
            suggRetObj = execSelectSQL(suggestQry);
            for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                searchSuggestion = new SearchSuggestion();
                searchSuggestion.setSuggestion(suggRetObj.getFieldValueString(i, "PORTLET_NAME"));
                srchSuggestionLst.add(searchSuggestion);

            }
            suggestion.setSuggestionList(srchSuggestionLst);
//            ProgenLog.log(ProgenLog.FINE, this, "getSearchReports", "suggestQry " + suggestQry);
            logger.info("suggestQry " + suggestQry);
        } catch (Exception e) {
            suggRetObj = null;
//            ProgenLog.log(ProgenLog.SEVERE, this, "getSearchReports", "Exception " + e.getMessage());
            logger.error("Exception:", e);
        }

        return suggestion;

    }

    public PbReturnObject getAllInsights(String userId) {
        String foldersQuery = getResourceBundle().getString("getAllUserFolders");
        String sqlQuery = getResourceBundle().getString("getUserInsights");
        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";

        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getUserInsights", "foldersQuery " + foldersQuery);
            logger.info("foldersQuery " + foldersQuery);
            retFolderObj = execSelectSQL(foldersQuery);
            if (retFolderObj.getRowCount() > 0) {
                for (int i = 0; i < retFolderObj.getRowCount(); i++) {
                    folderString += "," + retFolderObj.getFieldValueString(i, 0);
                }
                folderString = folderString.substring(1);
            }
            Object[] values = new Object[3];
            values[0] = folderString;
            values[1] = userId;
            values[2] = folderString;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
//            ProgenLog.log(ProgenLog.FINE, this, "getUserInsights", "sqlQuery " + sqlQuery);
            logger.info("sqlQuery " + sqlQuery);
        } catch (Exception e) {
            retObj = null;
//            ProgenLog.log(ProgenLog.SEVERE, this, "getUserInsights", "Exception " + e.getMessage());
            logger.error("Exception:", e);
        }
        //retObj.writeString();
//        ProgenLog.log(ProgenLog.FINE, this, "getUserInsights", "Exit");
        logger.info("Exit");
        return retObj;
    }
    //added by mohit for ao

    public PbReturnObject getUserAOs(String userId) {
//        ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "Enter userId " + userId);
        logger.info("Enter userId " + userId);
        String sqlQuery = getResourceBundle().getString("getUserAOs");

        PbReturnObject retObj = null;
        PbReturnObject retFolderObj = null;
        String folderString = "";
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "getUserReports", "foldersQuery " + foldersQuery);

            Object[] values = new Object[1];
            values[0] = userId;
            sqlQuery = buildQuery(sqlQuery, values);
            retObj = execSelectSQL(sqlQuery);
//            ProgenLog.log(ProgenLog.FINE, this, "getUserAOs", "sqlQuery " + sqlQuery);
            logger.info("sqlQuery " + sqlQuery);
        } catch (Exception e) {
            retObj = null;
//            ProgenLog.log(ProgenLog.SEVERE, this, "getUserAOs", "Exception " + e.getMessage());
            logger.error("Exception:", e);
        }
        //retObj.writeString();
//        ProgenLog.log(ProgenLog.FINE, this, "getUserAOs", "Exit");
        logger.info("Exit");
        return retObj;
    }

    PbReturnObject getMgmtTemplates(String userid) {
       String sqlQuery = getResourceBundle().getString("getTemplateDetails");
       PbReturnObject retObj = null;
       Object[] obj =new Object[1];
       obj[0] = userid;
       sqlQuery = buildQuery(sqlQuery, obj);
        try {
            retObj = execSelectSQL(sqlQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
}
        return retObj;
    }

    AutoSuggest giveTemplateSuggestions(String searchKey, String userId, String fromTab) {
            AutoSuggest suggestion = new AutoSuggest(searchKey);
        suggestion = giveSuggestions(searchKey, userId, fromTab);
        return suggestion;
    }
}
