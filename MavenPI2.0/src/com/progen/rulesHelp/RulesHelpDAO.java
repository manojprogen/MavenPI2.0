/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.rulesHelp;

import com.google.gson.Gson;
import com.progen.userlayer.action.GenerateDragAndDrophtml;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class RulesHelpDAO extends PbDb {

    public static Logger logger = Logger.getLogger(RulesHelpDAO.class);
    ResourceBundle resBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resBundle = new RulesHelpResourceBundleSqlServer();
            } else {
                resBundle = new RulesHelpResourceBundle();
            }
        }

        return resBundle;
    }

//    RulesHelpResourceBundle resourceBundle
    public PbReturnObject getDimentionsforRule(int folderId) {
        String getDimentions = getResourceBundle().getString("getDimentions");
        PbReturnObject dimrReturnObject = new PbReturnObject();
        Object[] objects = new Object[1];
        objects[0] = folderId;
        String finalQuery = buildQuery(getDimentions, objects);
        try {
            dimrReturnObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return dimrReturnObject;
    }

    public boolean saveRule(Object[] object) {
        try {
            String saveRule = getResourceBundle().getString("saveRule");
            String deleteRule = "DELETE FROM PRG_USER_RULES WHERE REF_ID=&";
            Object[] delteObject = new Object[1];
            delteObject[0] = object[2];
            super.execModifySQL(super.buildQuery(deleteRule, delteObject));
            String finalQuery = super.buildQuery(saveRule, object);
            super.execModifySQL(finalQuery);
            return true;
        } catch (Exception ex) {
            logger.error("Exception:", ex);
            return false;
        }
    }

    PbReturnObject getFilterDetails(String refid) {
        Object[] objects = new Object[1];
        PbReturnObject pbReturnObject = new PbReturnObject();
        objects[0] = refid;
        String getFilterDetails = getResourceBundle().getString("getFilterDetails");
        String finalQuery = super.buildQuery(getFilterDetails, objects);
        try {
            pbReturnObject = super.execSelectSQL(finalQuery);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return pbReturnObject;
    }

    public boolean deleteRule(String refid, String ruleType) {
        Object[] objects = new Object[2];
        objects[0] = refid;
        objects[1] = ruleType;
        String deletRule = "DELETE FROM PRG_USER_RULES WHERE REF_ID=& AND RULE_TYPE ='&'";
        boolean status = false;
        try {
            super.execModifySQL(super.buildQuery(deletRule, objects));
            status = true;
        } catch (Exception ex) {

            logger.error("Exception:", ex);
        }
        return status;
    }

    public String getruleDimMembers(String element_id, String path) {
        String getFilterDetails = getResourceBundle().getString("getruleDimMembers");
        String getDimValues = getResourceBundle().getString("getDimValues");
        PbReturnObject dimDetails = new PbReturnObject();
        String finalQurey = "";
        Object object[] = null;
        Connection connection = null;
        GenerateDragAndDrophtml generateDragAndDrophtml = null;
        ArrayList<String> dragableList = new ArrayList<String>();
        try {
            object = new Object[1];
            object[0] = element_id;
            finalQurey = super.buildQuery(getFilterDetails, object);
            dimDetails = super.execSelectSQL(finalQurey);
            object = new Object[2];
            if (dimDetails.getRowCount() > 0) {
                object[0] = dimDetails.getFieldValueString(0, 2);
                object[1] = dimDetails.getFieldValueString(0, 3);
                finalQurey = super.buildQuery(getDimValues, object);
                connection = ProgenConnection.getInstance().getConnectionForElement(element_id);

                try {
                    dimDetails = super.execSelectSQL(finalQurey, connection);
                    if (dimDetails.getRowCount() > 0) {
                        for (int i = 0; i < dimDetails.getRowCount(); i++) {
                            dragableList.add(dimDetails.getFieldValueString(i, 0));
                        }
                        //  dimDetailSb.append("<li><img src='/pi/icons pinvoke/report.png' alt=''><span id='").append(dimDetails.getFieldValueString(i, 0)).append("'>").append(dimDetails.getFieldValueString(i, 0)).append("</span></li>");
                        generateDragAndDrophtml = new GenerateDragAndDrophtml("", "", null, dragableList, path);
                    }
                } catch (Exception ex) {
                    logger.error("Exception:", ex);
                } finally {
                    if (connection != null) {
                        // connection.close();
                        connection = null;
                    }
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return generateDragAndDrophtml.getDragAndDropDiv();
    }

    public String getDimensionsForFilter(String elementID, String path) {
        ArrayList<String> dimNames = new ArrayList<String>();
        HashMap map = new HashMap();
        String dimNamesGson = "";
        String getFilterDetails = getResourceBundle().getString("getruleDimMembers");
        String getDimValues = getResourceBundle().getString("getDimValues");
        PbReturnObject dimDetails = new PbReturnObject();
        String finalQurey = "";
        Object object[] = null;
        Connection connection = null;
        object = new Object[1];
        object[0] = elementID;
        finalQurey = super.buildQuery(getFilterDetails, object);
        try {
            dimDetails = super.execSelectSQL(finalQurey);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        object = new Object[2];
        if (dimDetails.getRowCount() > 0) {
            object[0] = dimDetails.getFieldValueString(0, 2);
            object[1] = dimDetails.getFieldValueString(0, 3);
            finalQurey = super.buildQuery(getDimValues, object);
            connection = ProgenConnection.getInstance().getConnectionForElement(elementID);
            try {
                dimDetails = super.execSelectSQL(finalQurey, connection);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            if (dimDetails.getRowCount() > 0) {
                for (int i = 0; i < dimDetails.getRowCount(); i++) {
                    dimNames.add(dimDetails.getFieldValueString(i, 0));
                }
            }
        }
        map.put("dimNames", dimNames);
        Gson gson = new Gson();
        dimNamesGson = gson.toJson(map);
        return dimNamesGson;
    }
}
