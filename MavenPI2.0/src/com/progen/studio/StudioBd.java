/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.studio;

import com.google.common.collect.Iterables;
import com.progen.i18n.TranslaterHelper;
import com.progen.search.suggest.AutoSuggest;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class StudioBd extends PbDb {

    public static Logger logger = Logger.getLogger(StudioBd.class);

    public Studio getAllReports(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserReports(userid);
        if (!(pbro.getRowCount() > 0)) {

            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
            AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
            PbReturnObject folderpbro = null;
            try {
                folderpbro = new PbDb().execSelectSQL(AvailableFiolers);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if ((folderpbro.getRowCount() > 0)) {
                String folderList = "";
                for (int i = 0; i < folderpbro.getRowCount(); i++) {
                    folderList += "," + folderpbro.getFieldValueInt(i, 0);

                }
                if (!folderList.equalsIgnoreCase("")) {
                    folderList = folderList.substring(1);
                    //    String rolereps = "SELECT distinct REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + ")) and REPORT_TYPE='R' order by upper(report_name) asc";
                    String rolereps = "select rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                            + "where rm.report_id in (select a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                            + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='R' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";
                    PbReturnObject rolereppbro = null;
                    try {
                        rolereppbro = new PbDb().execSelectSQL(rolereps);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                    pbro = rolereppbro;
                }
            }
        }
        studio = this.createStudio(pbro, fromTab, locale);
        return studio;
    }

    public Studio getAllDashboards(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserDashboards(userid);

        if (!(pbro.getRowCount() > 0)) {

            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
            AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
            PbReturnObject folderpbro = null;
            try {
                folderpbro = new PbDb().execSelectSQL(AvailableFiolers);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if ((folderpbro.getRowCount() > 0)) {
                String folderList = "";
                for (int i = 0; i < folderpbro.getRowCount(); i++) {
                    folderList += "," + folderpbro.getFieldValueInt(i, 0);

                }
                if (!folderList.equalsIgnoreCase("")) {
                    folderList = folderList.substring(1);
                    // String rolereps = "SELECT distinct REPORT_ID, REPORT_NAME,REPORT_TYPE FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + ")) and REPORT_TYPE='D' order by upper(report_name) asc";
                    String rolereps = "";
                    if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                        rolereps = "select  rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                + "where rm.report_id in (select distinct a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='D' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";
                        ;
                    } else {
                        rolereps = "select distinct rm.report_id,rm.report_name,rm.report_type,uf.folder_name,uf.folder_created_on from prg_ar_report_master rm,prg_user_folder uf "
                                + "where rm.report_id in (select distinct a.report_id from PRG_AR_REPORT_DETAILS a,prg_ar_user_reports b "
                                + "where a.folder_id in (" + folderList + ") and a.report_id= b.report_id and b.user_id=" + userid + " and uf.folder_id= a.folder_id) and rm.report_type='D' and uf.folder_id in (" + folderList + ") order by upper(rm.report_name) asc";



                    }

                    PbReturnObject rolereppbro = null;
                    try {
                        rolereppbro = new PbDb().execSelectSQL(rolereps);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                    pbro = rolereppbro;
                }
            }
        }

        studio = this.createStudio(pbro, fromTab, locale);
        return studio;

    }

    public Studio getAllKPIDashboards(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserKPIDashboards(userid);

        if (!(pbro.getRowCount() > 0)) {

            return null;
        } else {

            studio = this.createStudio(pbro, fromTab, locale);
            return studio;
        }

    }

    public Studio createStudio(PbReturnObject retObj, String fromTab, Locale locale) {

        TranslaterHelper helper = new TranslaterHelper();
        Studio studio = new Studio();
        Calendar cal = Calendar.getInstance();
        StudioItem item;
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                item = new StudioItem();
                item.setAttribute1(retObj.getFieldValueString(i, 0));
                item.setAttribute2(retObj.getFieldValueString(i, 1).replace("\"", ""));
                item.setAttribute3(retObj.getFieldValueString(i, 2).replace("\"", ""));
                item.setAttribute4(retObj.getFieldValueString(i, 3));

                if (retObj.getFieldValueDate(i, 4) != null && !retObj.getFieldValueDateString(i, 4).equalsIgnoreCase("")) {
                    cal.setTime(retObj.getFieldValueDate(i, 4));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute5(dateAfterSort);
                }
                if (retObj.getFieldValueDate(i, 5) != null && !retObj.getFieldValueDateString(i, 5).equalsIgnoreCase("")) {
                    cal.setTime(retObj.getFieldValueDate(i, 5));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute61(dateAfterSort);
                } else {
                    cal.setTime(retObj.getFieldValueDate(i, 4));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute61(dateAfterSort);
                }
                // item.setAttribute61(retObj.getFieldValueString(i, 5));
                if (fromTab.equalsIgnoreCase("Portals")) {

                    item.setAttribute6(retObj.getFieldValueString(i, 5));
                }
                if (fromTab.equalsIgnoreCase("ReportStudio")) {
                    item.setAttribute6("audit trail");
                }
                studio.addItem(item);
            }
        }
        if (fromTab.equalsIgnoreCase("ReportStudio")) {
            studio.addLabel(TranslaterHelper.getTranslatedString("REPORT_NAME", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("REPORT_DESCRIPTION", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("BUSINESS_ROLE", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("CREATED_ON", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("UPDATED_ON", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("AUDIT_TRAIL", locale));

        } else if (fromTab.equalsIgnoreCase("DashboardStudio")) {
            studio.addLabel(TranslaterHelper.getTranslatedString("DASHBOARD_NAME", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("DASHBOARD_DESCRIPTION", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("BUSINESS_ROLE", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("CREATED_ON", locale));

        } else if (fromTab.equalsIgnoreCase("Scorecard")) {
            studio.addLabel(TranslaterHelper.getTranslatedString("SCORECARD_NAME", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("DESCRIPTION", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("CREATED_ON", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("UPDATED_ON", locale));
        } else if (fromTab.equalsIgnoreCase("Portals")) {
            studio.addLabel(TranslaterHelper.getTranslatedString("Portlet_Name", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("Portlet_Description", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("Type", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("Updated_On", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("Updated_By", locale));
        } else if (fromTab.equalsIgnoreCase("AOBuilder")) {
            studio.addLabel(TranslaterHelper.getTranslatedString("AO_NAME", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("AO_DESCRIPTION", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("BUSINESS_ROLE", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("CREATED_ON", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("UPDATED_ON", locale));
//            studio.addLabel(TranslaterHelper.getTranslatedString("AUDIT_TRAIL",locale));
        }

        return studio;
    }

    public AutoSuggest createAutoSuggestJson(String qry, String userId, String fromTab) {
        StudioDao studioDao = new StudioDao();
        AutoSuggest autoSuggest = null;
        String column = "";
        if (fromTab.equalsIgnoreCase("Report")) {
            autoSuggest = studioDao.giveReportSuggestions(qry, userId, fromTab);
        }
        if (fromTab.equalsIgnoreCase("AO")) {
            autoSuggest = studioDao.giveAOSuggestions(qry, userId, fromTab);
        }
        if (fromTab.equalsIgnoreCase("MgmtTemplate")) {
            autoSuggest = studioDao.giveTemplateSuggestions(qry, userId, fromTab);
        }
        if (fromTab.equalsIgnoreCase("Dashboard")) {
            autoSuggest = studioDao.giveDashboardSuggestions(qry, userId, fromTab);
        }
        if (fromTab.equalsIgnoreCase("MyReport")) {
            autoSuggest = studioDao.giveMyReportSuggestions(qry, userId, fromTab);
        } else if (fromTab.equalsIgnoreCase("Scorecard")) {
            autoSuggest = studioDao.giveScorecardSuggestions(qry, userId);
        } else if (fromTab.equalsIgnoreCase("HtmlReports")) {
            autoSuggest = studioDao.giveHtmlReportsSuggestions(qry, userId);
        } else if (fromTab.contains("sentiment")) {
            column = fromTab.replace("sentiment", "");
            autoSuggest = studioDao.giveSentimentSuggestions(qry, userId, column);
        } else if (fromTab.equalsIgnoreCase("Portals")) {
            autoSuggest = studioDao.givePortalsSuggestions(qry, userId, fromTab);
        }
        return autoSuggest;
    }

    public Studio getSelectedStudioItem(Studio studio, String searchText) {
        Studio selectedStudio = new Studio();
        //ArrayList itemList = new ArrayList();
        //ArrayList itemsList=new ArrayList();
        String[] commaLst = null;
        Iterable<StudioItem> itemList = studio.getList();

        if (searchText.contains("%")) {
            if (searchText.endsWith("%")) {
                searchText = searchText.replace("%", "");
            }
            Iterator<StudioItem> studioItem = Iterables.filter(itemList, Studio.getStudioItemsPredicate(searchText)).iterator();
            while (studioItem.hasNext()) {
                selectedStudio.addItem(studioItem.next());
            }

        } else if (searchText.contains(",")) {
            commaLst = searchText.split(",");
            for (int i = 0; i < commaLst.length; i++) {
                Iterator<StudioItem> studioItem = Iterables.filter(itemList, Studio.getStudioItemPredicate(commaLst[i])).iterator();
                if (studioItem.hasNext()) {
                    selectedStudio.addItem(studioItem.next());
                }
            }
        } else {
            Iterator<StudioItem> studioItem = Iterables.filter(itemList, Studio.getStudioItemPredicate(searchText.replace("'", "\'"))).iterator();
            if (studioItem.hasNext()) {
                selectedStudio.addItem(studioItem.next());
            }

        }
        selectedStudio.setLabels(studio.getLabels());
        return selectedStudio;

    }

    public Studio getAllScorecards(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserScorecards(userid);

        studio = this.createStudio(pbro, fromTab, locale);
        return studio;
    }

    public Studio getAllReportsHome(String userid, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getAllReportshome(userid);
        if (!(pbro.getRowCount() > 0)) {

            String AvailableFiolers = "select folder_Id,FOLDER_NAME from PRG_USER_FOLDER where folder_id in(";
            AvailableFiolers += "select user_folder_id from prg_grp_user_folder_assignment where user_id=" + userid;
            AvailableFiolers += " union ";
            AvailableFiolers += "select folder_id from prg_user_folder where grp_id in(";
            AvailableFiolers += "select grp_id from prg_grp_user_assignment where user_id=" + userid;
            AvailableFiolers += " and grp_id not in(select grp_id from prg_grp_user_folder_assignment where user_id=" + userid + ")))";

// PbReturnObject folderpbro = pbdb.execSelectSQL(userFoldersql);
            PbReturnObject folderpbro = null;
            try {
                folderpbro = new PbDb().execSelectSQL(AvailableFiolers);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }

            if ((folderpbro.getRowCount() > 0)) {
                String folderList = "";
                for (int i = 0; i < folderpbro.getRowCount(); i++) {
                    folderList += "," + folderpbro.getFieldValueInt(i, 0);

                }
                if (!folderList.equalsIgnoreCase("")) {
                    folderList = folderList.substring(1);
                    //String rolereps = "SELECT distinct report_id,report_name, report_desc, report_type FROM PRG_AR_REPORT_MASTER where REPORT_ID in(select distinct report_id from PRG_AR_REPORT_DETAILS where folder_id in (" + folderList + "))  order by report_id DESC";
                    String rolereps = "select a.report_id,a.report_name, a.report_desc, a.report_type, b.folder_name,b.folder_created_on  from prg_ar_report_master a,prg_user_folder b where report_id in (select distinct c.report_id from PRG_AR_REPORT_DETAILS c,prg_ar_user_reports d where c.folder_id in (" + folderList + ") and c.report_id= d.report_id and d.user_id=" + userid + " and b.folder_id= c.folder_id) and b.folder_id in (" + folderList + ") order by upper(a.report_name) asc";
                    PbReturnObject rolereppbro = null;
                    try {
                        rolereppbro = new PbDb().execSelectSQL(rolereps);
                    } catch (SQLException ex) {
                        logger.error("Exception:", ex);
                    }
                    pbro = rolereppbro;
                }
            }
        }
        studio = this.createStudioforMyReports(pbro, locale);
        return studio;
    }

    public Studio createStudioforMyReports(PbReturnObject retObj, Locale locale) {
        Studio studio = new Studio();
        TranslaterHelper helper = new TranslaterHelper();
        Calendar cal = Calendar.getInstance();
        StudioItem item;
        for (int i = 0; i < retObj.getRowCount(); i++) {
            item = new StudioItem();
            item.setAttribute1(retObj.getFieldValueString(i, 0));
            item.setAttribute2(retObj.getFieldValueString(i, 1).replace("\"", ""));
            item.setAttribute3(retObj.getFieldValueString(i, 2).replace("\"", ""));
            item.setAttribute4(retObj.getFieldValueString(i, 3));
            item.setAttribute5(retObj.getFieldValueString(i, 4));
//            item.setAttribute6(retObj.getFieldValueString(i, 5));
            if (retObj.getFieldValueDate(i, 5) != null && !retObj.getFieldValueDateString(i, 5).equalsIgnoreCase("")) {
                cal.setTime(retObj.getFieldValueDate(i, 5));
            }
            String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
            item.setAttribute6(dateAfterSort);
            studio.addItem(item);
        }

        studio.addLabel(helper.getTranslatedString("REPORT_NAME", locale));
        studio.addLabel(helper.getTranslatedString("REPORT_DESCRIPTION", locale));
        studio.addLabel(helper.getTranslatedString("TYPE", locale));
        studio.addLabel(helper.getTranslatedString("FOLDER_NAME", locale));
        studio.addLabel(helper.getTranslatedString("CREATED_ON", locale));



        return studio;
    }

    public Studio getAllSnapshots(String userid, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserSnapshots(userid);

        studio = this.createStudioforSnapshots(pbro, locale);
        return studio;
    }

    public Studio createStudioforSnapshots(PbReturnObject retObj, Locale locale) {
        Studio studio = new Studio();
        TranslaterHelper helper = new TranslaterHelper();
        StudioItem item;
        for (int i = 0; i < retObj.getRowCount(); i++) {
            item = new StudioItem();
            item.setAttribute1(retObj.getFieldValueString(i, 0));
            item.setAttribute2(retObj.getFieldValueString(i, 1).replace("\"", ""));
            item.setAttribute3(retObj.getFieldValueString(i, 2));
            item.setAttribute4(retObj.getFieldValueString(i, 3));
            item.setAttribute5(retObj.getFieldValueString(i, 4));
            item.setAttribute6(retObj.getFieldValueString(i, 5));
            item.setAttribute7(retObj.getFieldValueString(i, 6));
            studio.addItem(item);
        }
        studio.addLabel(helper.getTranslatedString("SNAPSHOT_NAME", locale));
        studio.addLabel(helper.getTranslatedString("SHAPSHOT_ID", locale));
        studio.addLabel(helper.getTranslatedString("LAST_REFRESHED_DATE", locale));
        studio.addLabel(helper.getTranslatedString("BUSINESS_ROLE", locale));
        studio.addLabel(helper.getTranslatedString("REFRESH_INTERVAL", locale));
        studio.addLabel(helper.getTranslatedString("CREATED_ON", locale));
        studio.addLabel(helper.getTranslatedString("HTML_REPORT_TYPE", locale));

        return studio;
    }

    public PbReturnObject getAllportals(String userid, String fromTab, String searchText) {
        Studio studio = null;
        String userid1 = userid;
        String fromtab1 = fromTab;
        String searchText1 = searchText;
        String query = "select  PORTLET_ID, PORTLET_NAME, PORTLET_DESC, PORTLET_REPORT_TYPE,UPDATE_ON,PU_LOGIN_ID from PRG_BASE_PORTLETS_MASTER,"
                + "PRG_AR_USERS where PU_ID=UPDATE_BY AND XML_PATH IS NOT NULL AND PORTLET_NAME='" + searchText1 + "'";
        PbReturnObject retObj = null;
        try {
            retObj = execSelectSQL(query);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return retObj;
    }
    /*
     * Author: Amar Module:- function written to get all AO's on Nov 26, 15
     */

    public Studio getAllAO(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao studioDao = new StudioDao();
        PbReturnObject pbro = studioDao.getUserAOs(userid);
        studio = this.createStudio(pbro, fromTab, locale);
        return studio;
    }

   
    Studio getAllTemplateInfo(String userid, String fromTab, Locale locale) {
        Studio studio = null;
        StudioDao dao = new StudioDao();
        PbReturnObject retObj = dao.getMgmtTemplates(userid);
        studio = this.createManagementStudio(retObj, fromTab, locale);
        return studio;
}

    private Studio createManagementStudio(PbReturnObject retObj, String fromTab, Locale locale) {
       TranslaterHelper helper = new TranslaterHelper();
        Studio studio = new Studio();
        Calendar cal = Calendar.getInstance();
        StudioItem item;
        if (retObj != null) {
            for (int i = 0; i < retObj.getRowCount(); i++) {
                item = new StudioItem();
                item.setAttribute1(retObj.getFieldValueString(i, "TEMPLATE_ID"));
                item.setAttribute2(retObj.getFieldValueString(i, "TEMPLATE_NAME"));
                item.setAttribute3(retObj.getFieldValueString(i, "TEMPLATE_DESCRIPTION"));

                if (retObj.getFieldValueDate(i, "CREATED_ON") != null && !retObj.getFieldValueDateString(i, "CREATED_ON").equalsIgnoreCase("")) {
                    cal.setTime(retObj.getFieldValueDate(i, "CREATED_ON"));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute4(dateAfterSort);
                }
                if (retObj.getFieldValueDate(i, "UPDATED_ON") != null && !retObj.getFieldValueDateString(i, "UPDATED_ON").equalsIgnoreCase("")) {
                    cal.setTime(retObj.getFieldValueDate(i, "UPDATED_ON"));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute5(dateAfterSort);
                } else {
                    cal.setTime(retObj.getFieldValueDate(i, "CREATED_ON"));
                    String dateAfterSort = (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
                    item.setAttribute4(dateAfterSort);
                }
               
                studio.addItem(item);
            }
        }
            studio.addLabel(TranslaterHelper.getTranslatedString("TEMPLATE_NAME", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("TEMPLATE_DESCRIPTION", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("CREATED_ON", locale));
            studio.addLabel(TranslaterHelper.getTranslatedString("UPDATED_ON", locale));


        return studio;
    }
}
