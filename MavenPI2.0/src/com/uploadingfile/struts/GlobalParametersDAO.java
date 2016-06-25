/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * anitha.pallothu@progenbusiness.com
 */
package com.uploadingfile.struts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class GlobalParametersDAO extends PbDb {

    public String isCompLogo = "";
    public String companyId = "";
    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(GlobalParametersDAO.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new GlobalParametersResourceBundleSqlServer();
            } else {
                resourceBundle = new GlobalParametersResourceBundle();
            }
        }

        return resourceBundle;
    }

    public ArrayList saveGlobalParams(ArrayList globalParamsList) throws SQLException {

        ArrayList query = new ArrayList();
        Object object[] = new Object[1];
        FileOutputStream fos = null;
        String[] colNames = null;
        String leftlogo = null;
        String rightlogo = null;
        String session = null;
        String filePath = null;
        String debug = null;
        String saveCache = null;
        String savedatepicker = getResourceBundle().getString("savedatepicker");
        String savedateformat = resourceBundle.getString("savedateformat");
        String savesessions = resourceBundle.getString("savesessions");
        String savelanguage = resourceBundle.getString("savelanguage");
        String saveleftlogo = resourceBundle.getString("saveleftlogo");
        String saverightlogo = resourceBundle.getString("saverightlogo");
        String savefootertext = resourceBundle.getString("savefootertext");
        String saverecords = resourceBundle.getString("saverecords");
        String savequerycache = resourceBundle.getString("savequerycache");
        String savequery = resourceBundle.getString("savequery");
        String savedataset = resourceBundle.getString("savedataset");
        String savegeoenable = resourceBundle.getString("savegeoenable");
        String savedebug = resourceBundle.getString("savedebug");
        String savereport = resourceBundle.getString("savereport");
        String saveduplicateseg = resourceBundle.getString("saveduplicatesegmentation");
        String finalyquery = "";

        try {
            object[0] = null;
            object[0] = globalParamsList.get(0);
            query.add(super.buildQuery(savedatepicker, object));
            object[0] = null;
            object[0] = globalParamsList.get(1);
            query.add(super.buildQuery(savedateformat, object));
            object[0] = null;
            object[0] = globalParamsList.get(2);
            query.add(super.buildQuery(savesessions, object));
            object[0] = null;
            object[0] = globalParamsList.get(3);
            query.add(super.buildQuery(savelanguage, object));
            object[0] = null;
            object[0] = globalParamsList.get(4);
            query.add(super.buildQuery(saveleftlogo, object));
            object[0] = null;
            object[0] = globalParamsList.get(5);
            query.add(super.buildQuery(saverightlogo, object));
            object[0] = null;
            object[0] = globalParamsList.get(6);
            query.add(super.buildQuery(savefootertext, object));
            object[0] = null;
            object[0] = globalParamsList.get(7);
            query.add(super.buildQuery(saverecords, object));
            object[0] = null;
            object[0] = globalParamsList.get(8);
            query.add(super.buildQuery(savequerycache, object));
            object[0] = null;
            object[0] = globalParamsList.get(9);
            query.add(super.buildQuery(savequery, object));
            object[0] = null;
            object[0] = globalParamsList.get(10);
            query.add(super.buildQuery(savedataset, object));
            object[0] = null;
            object[0] = globalParamsList.get(11);
            query.add(super.buildQuery(savegeoenable, object));
            object[0] = null;
            object[0] = globalParamsList.get(12);
            query.add(super.buildQuery(savedebug, object));
            object[0] = null;
            object[0] = globalParamsList.get(13);
            query.add(super.buildQuery(savereport, object));
            object[0] = null;
            object[0] = globalParamsList.get(14);
            query.add(super.buildQuery(saveduplicateseg, object));

            executeMultiple(query);

            String sqlQuery = "select * from PRG_GBL_SETUP_VALUES";
            PbReturnObject globalRetObj = execSelectSQL(sqlQuery);
            colNames = globalRetObj.getColumnNames();
            StringBuffer globtablebufr = new StringBuffer();
            String[] tabId = new String[globalRetObj.getRowCount()];
            String[] tabName = new String[globalRetObj.getRowCount()];
            String[] tabtooltipName = new String[globalRetObj.getRowCount()];
            for (int i = 0; i < globalRetObj.getRowCount(); i++) {
                tabId[i] = String.valueOf(globalRetObj.getFieldValueInt(i, colNames[0]));
                tabName[i] = globalRetObj.getFieldValueString(i, colNames[1]);
                tabtooltipName[i] = globalRetObj.getFieldValueString(i, colNames[2]);
                if (tabName[i].equals("LEFT_LOGO")) {
                    leftlogo = globalRetObj.getFieldValueString(i, colNames[2]);
                }
                if (tabName[i].equals("SESSION_EXPIRY")) {
                    session = globalRetObj.getFieldValueString(i, colNames[2]);
                }
                if (tabName[i].equals("RIGHT_LOGO")) {
                    rightlogo = globalRetObj.getFieldValueString(i, colNames[2]);
                }
                if (tabName[i].equals("DEBUG_ENABLED")) {
                    debug = globalRetObj.getFieldValueString(i, colNames[2]);
                }
                if (tabName[i].equals("QUERY_CACHE")) {
                    saveCache = globalRetObj.getFieldValueString(i, colNames[2]);
                }
            }


            File propertiesFile = new File("/home/progen/work/central/pi/src/java/com/progen/resourcebundle/ProgenResourceBundle.properties");
            if (!propertiesFile.exists()) {
                //if file doesn't exists create one
                propertiesFile.createNewFile();
            }
            FileInputStream propertiesFileStream = new FileInputStream(propertiesFile);
            Properties properties = new Properties();
            //load properties through an InputStream
            properties.load(propertiesFileStream);
            //add/update a property
            properties.setProperty("ProGen.piLogo", leftlogo);
            properties.setProperty("ProGen.businessLogo", rightlogo);
            FileOutputStream propertiesOutput = new FileOutputStream(propertiesFile);
            //save the properties to a file
            properties.store(propertiesOutput, "new changes");

            //added by Nazneen for logo based on company
            if (isCompLogo.equalsIgnoreCase("YES")) {
                if (companyId != null && !companyId.equalsIgnoreCase("null") && !companyId.equalsIgnoreCase("")) {
                    String queryLogoDetails = "SELECT COMPANY_LOGO,BUSSINESS_LOGO,COMPANY_TITLE,BUSSINESS_TITLE,RIGHT_WEB_SITE_URL,LEFT_WEB_SITE_URL FROM PRG_COMPANY_LOGO WHERE COMPANY_ID=" + companyId;
                    PbReturnObject pbroObject = null;
                    pbroObject = execSelectSQL(queryLogoDetails);
                    if (pbroObject.getRowCount() > 0) {
                        String compLogo = pbroObject.getFieldValueString(0, 0);
                        String bussLogo = pbroObject.getFieldValueString(0, 1);
                        String compTitle = pbroObject.getFieldValueString(0, 2);
                        String bussTitle = pbroObject.getFieldValueString(0, 3);
                        String rightWebSiteUrl = pbroObject.getFieldValueString(0, 4);
                        String leftWebSiteUrl = pbroObject.getFieldValueString(0, 5);
                        properties.setProperty("ProGen.piLogo", compLogo);
                        properties.setProperty("ProGen.businessLogo", bussLogo);
                        properties.setProperty("piLogo.Title", compTitle);
                        properties.setProperty("progenLogo.Title", bussTitle);
                        properties.setProperty("rightwebsite.url", rightWebSiteUrl);
                        properties.setProperty("leftwebsite.url", leftWebSiteUrl);
                    }
                }
            }
            //end of code by Nazneen for logo based on company


            return query;
        } catch (Exception ex) {
            logger.error("Exception: ", ex);
            return query;
        }
    }
}
