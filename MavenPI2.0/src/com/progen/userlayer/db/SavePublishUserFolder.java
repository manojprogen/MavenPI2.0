/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.userlayer.db;

import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Saurabh
 */
public class SavePublishUserFolder extends PbDb {
// PbUserLayerResourceBundle resBundle = new PbUserLayerResourceBundle();

    ResourceBundle resourceBundle;
    public static Logger logger = Logger.getLogger(SavePublishUserFolder.class);

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new PbUserLayerResourceBundleSqlServer();
            } else if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                resourceBundle = new PbUserLayerResourceBundleMysql();
            } else {
                resourceBundle = new PbUserLayerResourceBundle();
            }
        }

        return resourceBundle;
    }

    public boolean publishUserFolder(String folderId) {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("in java class  "+folderId);

        boolean result = false;
        String deleteUserAllInfoDetails = getResourceBundle().getString("deleteUserAllInfoDetails");
        String deleteUserAllDDIMKeyValEle = getResourceBundle().getString("deleteUserAllDDIMKeyValEle");
        String deleteUserAllADIMKeyValEle = getResourceBundle().getString("deleteUserAllADIMKeyValEle");
        String deleteUserAllDDIMDetails = getResourceBundle().getString("deleteUserAllDDIMDetails");
        String deleteUserAllDDIMMaster = getResourceBundle().getString("deleteUserAllDDIMMaster");
        String deleteUserAllADIMDetails = getResourceBundle().getString("deleteUserAllADIMDetails");
        String deleteUserAllADIMMaster = getResourceBundle().getString("deleteUserAllADIMMaster");
        String addUserAllInfoDetails = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
            addUserAllInfoDetails = getResourceBundle().getString("addUserAllInfoDetailsMysql");
        } else {
            addUserAllInfoDetails = getResourceBundle().getString("addUserAllInfoDetails");
        }
        String addUserAllDDIMDetails = getResourceBundle().getString("addUserAllDDIMDetails");
        String addUserAllDDIMMaster = getResourceBundle().getString("addUserAllDDIMMaster");
        String addUserAllDDIMKeyValEle = getResourceBundle().getString("addUserAllDDIMKeyValEle");
        String addUserAllADIMDetails = getResourceBundle().getString("addUserAllADIMDetails");
        String addUserAllAdimMaster = getResourceBundle().getString("addUserAllAdimMaster");
        String addUserAllADIMKeyValEle = getResourceBundle().getString("addUserAllADIMKeyValEle");

        ArrayList deleteUserFolderQueries = new ArrayList();

        PbReturnObject retObj = null;
        Object[] Obj = null;
        Object[] Obj1 = null;
        String finalQuery = "";
        Obj = new Object[1];
        Obj[0] = folderId;
        try {
            finalQuery = buildQuery(deleteUserAllInfoDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMKeyValEle, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMKeyValEle, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllDDIMMaster, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMDetails, Obj);
            deleteUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(deleteUserAllADIMMaster, Obj);
            deleteUserFolderQueries.add(finalQuery);
            executeMultiple(deleteUserFolderQueries);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deletion is over ");
            Obj1 = new Object[2];
            Obj1[0] = folderId;
            Obj1[1] = folderId;
            ArrayList addUserFolderQueries = new ArrayList();
            finalQuery = buildQuery(addUserAllInfoDetails, Obj);


            //////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllInfoDetails------------->"+finalQuery);
            //////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllDDIMDetails, Obj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllDDIMDetails------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllDDIMMaster, Obj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllDDIMMaster------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllDDIMKeyValEle, Obj1);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllDDIMKeyValEle------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllADIMDetails, Obj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllADIMDetails------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllAdimMaster, Obj);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllAdimMaster------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);
            finalQuery = buildQuery(addUserAllADIMKeyValEle, Obj1);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("---------addUserAllADIMKeyValEle------------->"+finalQuery);
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            addUserFolderQueries.add(finalQuery);

            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////.println.println("deletion is over ");

            Obj1[0] = "Y";
            Obj1[1] = folderId;
            String addupdateFolderPublishment = getResourceBundle().getString("updateFolderPublishment");
            finalQuery = buildQuery(addupdateFolderPublishment, Obj1);
            //.println("finalQuery\t"+finalQuery);
            addUserFolderQueries.add(finalQuery);
            //.println("addUserFolderQueries"+addUserFolderQueries);
            result = executeMultiple(addUserFolderQueries);

            String getCalculatedFormulas = getResourceBundle().getString("getCalculatedFormulas");
            finalQuery = buildQuery(getCalculatedFormulas, Obj);
            //////////////////////////////////////////////////////////////////////////////.println.println("final getCalculatedFormulas---"+finalQuery);
            PbReturnObject pbro = execSelectSQL(finalQuery);
            String refElements = "";
            String ActFormula = "";
            String mainEleId = "";
            String bussTableName = "";
            ArrayList updateQueries = new ArrayList();
            for (int i = 0; i < pbro.getRowCount(); i++) {
                refElements = pbro.getFieldValueString(i, "REFFERED_ELEMENTS");
                ActFormula = pbro.getFieldValueString(i, "ACTUAL_COL_FORMULA");
                mainEleId = String.valueOf(pbro.getFieldValueInt(i, "ELEMENT_ID"));
                bussTableName = String.valueOf(pbro.getFieldValueString(i, "BUSS_TABLE_NAME"));
                //////////////////////////////////////////////////////////////////////////.println.println("refElements--"+refElements);
                //////////////////////////////////////////////////////////////////////////.println.println("ActFormula--"+ActFormula);
                if (!refElements.equalsIgnoreCase("")) {
                    String refEleList[] = refElements.split(",");
                    for (int j = 0; j < refEleList.length; j++) {
                        if (refEleList[j].startsWith("M-")) {
                            String detlist[] = refEleList[j].substring(2).split("-");
                            if (detlist.length >= 3) {
                                String bussTableId = detlist[0];
                                String bussColId = detlist[1];
                                String memId = detlist[2];
                                String eleIdQuery = " select element_id from prg_user_all_info_details where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + " and  member_id=" + memId + " and  folder_id=" + folderId;
                                //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                ActFormula = ActFormula.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                                refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                            }

                        } else {
                            //////////////////////////////////////////////////////////////////////////////.println.println("IN ELSE PART");

                            String detlist[] = refEleList[j].split("-");
                            if (detlist.length >= 2) {
                                String bussTableId = detlist[0];
                                String bussColId = detlist[1];

                                String eleIdQuery = " select element_id from prg_user_all_info_details where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + " and SUB_FOLDER_TYPE='Facts'  and  folder_id=" + folderId;
                                //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                            } else {

                                String bussTableId = String.valueOf(pbro.getFieldValueInt(i, "BUSS_TABLE_ID"));
                                String bussColId = detlist[0];

                                String eleIdQuery = " select element_id from prg_user_all_info_details where  buss_table_id=" + bussTableId + " and buss_col_id=" + bussColId + " and SUB_FOLDER_TYPE='Facts'  and  folder_id=" + folderId;
                                //////////////////////////////////////////////////////////////////////////////.println.println("elementQuery---"+eleIdQuery);
                                PbReturnObject elementIdpbro = execSelectSQL(eleIdQuery);
                                //////////////////////////////////////////////////////////////////////////////.println.println("eleId--"+elementIdpbro.getFieldValueInt(0,0));
                                refElements = refElements.replace(refEleList[j], String.valueOf(elementIdpbro.getFieldValueInt(0, 0)));
                            }
                        }
                    }

                    if (bussTableName.equalsIgnoreCase("Calculated Facts")) {
                        ////.println("vfbvnjhmjk,.lk/'");

                        String updateQuery = " update PRG_USER_ALL_INFO_DETAILS set REFFERED_ELEMENTS='" + refElements + "', ACTUAL_COL_FORMULA='" + ActFormula + "',BUSS_TABLE_ID=0,BUSS_COL_ID=0,buss_table_name=null  where element_id=" + mainEleId;
                        updateQueries.add(updateQuery);
                    } else {
                        String updateQuery = " update PRG_USER_ALL_INFO_DETAILS set REFFERED_ELEMENTS='" + refElements + "', ACTUAL_COL_FORMULA='" + ActFormula + "'  where element_id=" + mainEleId;
                        updateQueries.add(updateQuery);
                    }
                    //////////////////////////////////////////////////////////////////////////.println.println("updateQuery--"+updateQuery);
                    //////////////////////////////////////////////////////////////////////////.println.println("---------------------------------");



                }
            }
            //////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            String getFolderLevelColFormulas = "";
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.MYSQL)) {
                getFolderLevelColFormulas = getResourceBundle().getString("getFolderLevelColFormulasMysql");
            } else {
                getFolderLevelColFormulas = getResourceBundle().getString("getFolderLevelColFormulas");
            }
            finalQuery = buildQuery(getFolderLevelColFormulas, Obj);
            //////////////////////////////////////////////////////////////////////////////.println.println("---------getFolderLevelColFormulas------------->"+finalQuery);
            //////////////////////////////////////////////////////////////////////////////.println.println("------------------------------------------------------------------------------");
            updateQueries.add(finalQuery);

            result = executeMultiple(updateQueries);





            return result;
        } catch (Exception exception) {
            logger.error("Exception: ", exception);
            return result;
        }

    }
}
