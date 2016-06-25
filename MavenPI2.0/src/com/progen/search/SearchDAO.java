/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

//import com.progen.log.ProgenLog;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class SearchDAO extends PbDb {

    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(SearchDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new SearchResBundleSqlServer();
            } else {
                resourceBundle = new SearchResourceBundle();
            }
        }

        return resourceBundle;
    }

    public int saveSearchValues(Search search, int userId) {
        String query = "";
        String finalquery = "";
        int searchcount = 1;

        Object[] searchDetails;
        int searchId = 0;

        query = getResourceBundle().getString("isSearchPresent");
        String searchText = search.getSearchText();


        PbReturnObject retobj = null;
        searchDetails = new Object[1];
        searchDetails[0] = searchText;

        finalquery = buildQuery(query, searchDetails);

        try {
            retobj = execSelectSQL(finalquery);
            if (retobj.getRowCount() > 0) {
                searchId = retobj.getFieldValueInt(0, "SEARCH_ID");
                int searchcountvalue = retobj.getFieldValueInt(0, "SEARCH_COUNT");
                searchcountvalue = searchcountvalue + 1;
                searchDetails = new Object[1];
                searchDetails[0] = searchId;
                query = getResourceBundle().getString("updateSearchCount");
                finalquery = buildQuery(query, searchDetails);
                execModifySQL(finalquery);


            } else {
                searchDetails = new Object[6];
                query = getResourceBundle().getString("addSearchValues");
                if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
                    searchId = getSequenceNumber("select PRG_USER_SEARCH_DETAILS_SEQ.nextval from dual");
                    searchDetails[0] = searchId;
                    searchDetails[1] = userId;
                    searchDetails[2] = searchText;
                    searchDetails[3] = userId;
                    searchDetails[4] = userId;
                    searchDetails[5] = searchcount;
                    finalquery = buildQuery(query, searchDetails);
                    execModifySQL(finalquery);

                } else {
                    searchDetails = new Object[5];
                    searchDetails[0] = userId;
                    searchDetails[1] = searchText;
                    searchDetails[2] = userId;
                    searchDetails[3] = userId;
                    searchDetails[4] = searchcount;
                    finalquery = buildQuery(query, searchDetails);
                    searchId = insertAndGetSequenceInSQLSERVER(finalquery, "PRG_USER_SEARCH_DETAILS");
                }

            }
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "saveSearchValues", ex.getMessage());
            logger.error("Exception:", ex);
            searchId = -1;
        }

        return searchId;

    }

    public boolean checkDuplicate(String favSearchName) {
        boolean nameExists = false;
        String query = getResourceBundle().getString("checkFavoriteSearchName");
        try {
            Object[] bind = new Object[1];
            bind[0] = favSearchName.toLowerCase();
            String finalQuery = buildQuery(query, bind);

            PbReturnObject retObj = execSelectSQL(finalQuery);
            if (retObj.getRowCount() > 0) {
                nameExists = true;
            }
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "checkDuplicate", se.getMessage());
            logger.error("Exception:", ex);
        }
        return nameExists;
    }

    public boolean saveAsFavoriteSearch(Search userSearch, int userId) {

        String searchname = userSearch.getSearchName();
        String query = "";
        String finalQuery = "";
        boolean save = true;
        try {
            query = getResourceBundle().getString("addFavSearchValues");
            int searchId = userSearch.getSearchId();
            Object[] searchDetails = new Object[5];
            searchDetails[0] = searchId;
            searchDetails[1] = userId;
            searchDetails[2] = userId;
            searchDetails[3] = userId;
            searchDetails[4] = searchname;
            finalQuery = buildQuery(query, searchDetails);
            execModifySQL(finalQuery);

        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "saveAsFavoriteSearch", ex.getMessage());
            logger.error("Exception:", ex);
            save = false;
        }

        return save;
    }

    public HashMap<String, String> getSavedFavSearchDetails() {
        String searchName = "";
        String searchText = "";

        HashMap<String, String> favSearch = new HashMap<String, String>();
        try {
            PbReturnObject retobj = execSelectSQL("SELECT S.SEARCH_TEXT,F.SEARCHNAME FROM PRG_USER_SEARCH_DETAILS S, PRG_USER_FAV_SEARCH F WHERE S.SEARCH_ID=F.SEARCH_ID");
            for (int i = 0; i < retobj.getRowCount(); i++) {
                searchName = retobj.getFieldValueString(i, "SEARCHNAME");
                searchText = retobj.getFieldValueString(i, "SEARCH_TEXT");
                favSearch.put(searchName, searchText);
            }
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "getSavedFavSearchDetails", ex.getMessage());
            logger.error("Exception:", ex);
        }
        return favSearch;
    }

    public void deleteUserFavSearch(String searchName) {
        String query = "delete from PRG_USER_FAV_SEARCH where Lower(SEARCHNAME) like '" + searchName.toLowerCase() + "' ";
        try {
            execModifySQL(query);
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "deleteUserFavSearch", ex.getMessage());
            logger.error("Exception:", ex);
        }
    }
}
