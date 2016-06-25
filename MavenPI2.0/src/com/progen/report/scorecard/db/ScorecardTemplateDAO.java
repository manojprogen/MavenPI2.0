/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.db;

import com.progen.report.scorecard.ScoreCardAction;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author Administrator
 */
public class ScorecardTemplateDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ScorecardTemplateDAO.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ScorecardTemplateResBundleSqlServer();
            } else {
                resourceBundle = new ScorecardTemplateResourceBundle();
            }
        }

        return resourceBundle;
    }

    public ArrayList insertScardMaster(int scardId, String scardName, String scardArea, int NoOfDays) {
        String insertScardMasterQuery = getResourceBundle().getString("insertScardMaster");
        ArrayList queries = new ArrayList();
        Object[] scardMaster = null;
        String finalQuery = "";

        try {

            scardMaster = new Object[3];
            scardMaster[0] = scardName;
            scardMaster[1] = scardArea;
            scardMaster[2] = NoOfDays;



            finalQuery = buildQuery(insertScardMasterQuery, scardMaster);

            queries.add(finalQuery);//inserting into scard master
//            queries = insertReportDetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);



        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertScardUser(int scardId, int userId) {
        String insertScardUserQuery = getResourceBundle().getString("insertScardUser");
        ArrayList queries = new ArrayList();
        Object[] scardUser = null;
        String finalQuery = "";

        try {
            scardUser = new Object[2];
            scardUser[0] = scardId;
            scardUser[1] = userId;



            finalQuery = buildQuery(insertScardUserQuery, scardUser);

            queries.add(finalQuery);//inserting into scard user
//            queries = insertReportDetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);


        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;
    }

    public ArrayList insertScardMember(int scardId, int scardMemId, String scardType, int scardContribution, int scardSeq) {
        String insertScardMemberQuery = getResourceBundle().getString("insertScardMember");
        ArrayList queries = new ArrayList();
        Object[] scardMember = null;
        String finalQuery = "";

        try {
            scardMember = new Object[4];
            scardMember[0] = scardId;
            scardMember[1] = scardType;
            scardMember[2] = scardContribution;
            scardMember[3] = scardSeq;


            finalQuery = buildQuery(insertScardMemberQuery, scardMember);

            queries.add(finalQuery);//inserting into scard user
//            queries = insertReportDetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);



        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertScardMemberKpi(int scardMemberKpiId, int scardDetailsId, int scardId, int scardMemId, int elementId,
            int priorElementId, int changeElementId, int changepElementId, int nextElementId, int nchangeElementId, int nchangepElementId,
            String scardBasis, String elementName, int targetValue, int targetType, int targetDaysMonths) {
        String insertScardMemberKpiQuery = getResourceBundle().getString("insertScardMemberKpi");
        ArrayList queries = new ArrayList();
        Object[] scardMemberKpi = null;
        String finalQuery = "";

        try {
            scardMemberKpi = new Object[15];
            scardMemberKpi[0] = scardDetailsId;
            scardMemberKpi[1] = scardId;
            scardMemberKpi[2] = scardMemId;
            scardMemberKpi[3] = elementId;
            scardMemberKpi[4] = priorElementId;
            scardMemberKpi[5] = changeElementId;
            scardMemberKpi[6] = changepElementId;
            scardMemberKpi[7] = nextElementId;
            scardMemberKpi[8] = nchangeElementId;
            scardMemberKpi[9] = nchangepElementId;
            scardMemberKpi[10] = scardBasis;
            scardMemberKpi[11] = elementName;
            scardMemberKpi[12] = targetValue;
            scardMemberKpi[13] = targetType;
            scardMemberKpi[14] = targetDaysMonths;



            finalQuery = buildQuery(insertScardMemberKpiQuery, scardMemberKpi);

            queries.add(finalQuery);//inserting into scard member kpi
//            queries = insertReportDetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertScardMemberKpiRule(int scardMemberKpiRuleId, int scardDetailsId, int scardId, int scardMemId, int scardMemValueSt, int scardMemValueEnd, int scardScore) {

        String insertScardMemberKpiRuleQuery = getResourceBundle().getString("insertScardMemberKpiRule");
        ArrayList queries = new ArrayList();
        Object[] scardMemberKpiRule = null;
        String finalQuery = "";
        try {
            scardMemberKpiRule = new Object[6];
            scardMemberKpiRule[0] = scardDetailsId;
            scardMemberKpiRule[1] = scardId;
            scardMemberKpiRule[2] = scardMemId;
            scardMemberKpiRule[3] = scardMemValueSt;
            scardMemberKpiRule[4] = scardMemValueEnd;
            scardMemberKpiRule[5] = scardScore;



            finalQuery = buildQuery(insertScardMemberKpiRuleQuery, scardMemberKpiRule);

            queries.add(finalQuery);//inserting into scard member kpi
//            queries = insertReportDetails(reportId, UserFolderIds, queries);//inserting into report details

            //boolean status = executeMultiple(queries);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertDashletDetails(int dashletDetailId, int masterId, int componentId) {

        String insertDashletDetailsQuery = getResourceBundle().getString("insertDashletDetails");
        ArrayList queries = new ArrayList();
        Object[] dashletDetails = null;
        String finalQuery = "";
        try {
            dashletDetails = new Object[2];
            dashletDetails[0] = masterId;
            dashletDetails[1] = componentId;

            finalQuery = buildQuery(insertDashletDetailsQuery, dashletDetails);
            queries.add(finalQuery);//inserting into scard member kpi
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertScardLights(int scardId, int scoreStart, int scoreEnd, String lightColor) {

        String insertScardLightsQuery = getResourceBundle().getString("insertScardLights");
        ArrayList queries = new ArrayList();
        Object[] scardLights = null;
        String finalQuery = "";
        try {

            scardLights = new Object[3];
            scardLights[0] = scoreStart;
            scardLights[1] = scoreEnd;
            scardLights[2] = lightColor;

            finalQuery = buildQuery(insertScardLightsQuery, scardLights);
            queries.add(finalQuery);//inserting into scard member kpi
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public ArrayList insertScardActionTypes(int scardActionTypeId, String scardActionType, String scardActionText) {

        String insertScardActionTypesQuery = getResourceBundle().getString("insertScardActionTypes");
        ArrayList queries = new ArrayList();
        Object[] scardActionTypes = null;
        String finalQuery = "";
        try {

            scardActionTypes = new Object[2];
            scardActionTypes[0] = scardActionType;
            scardActionTypes[1] = scardActionText;


            finalQuery = buildQuery(insertScardActionTypesQuery, scardActionTypes);
            queries.add(finalQuery);//inserting into scard member kpi
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return queries;

    }

    public boolean insertScardActionDetails(String scardId, String scardMemId, String score, String actionType, String actionXml, String startDate, String endDate, String elemName) {

        boolean status = false;
        String insertScardActionDetailsQuery = getResourceBundle().getString("insertScardActionDetails");
        ArrayList queries = new ArrayList();
        Object[] scardActionDetails = null;
        String finalQuery = "";
        if (scardMemId == null) {
            scardMemId = "null";
        }
        try {
            scardActionDetails = new Object[8];
            scardActionDetails[0] = scardId;
            scardActionDetails[1] = scardMemId;
            scardActionDetails[2] = score;
            scardActionDetails[3] = actionType;
            scardActionDetails[4] = actionXml;
            scardActionDetails[5] = startDate;
            scardActionDetails[6] = endDate;
            scardActionDetails[7] = elemName;
            finalQuery = buildQuery(insertScardActionDetailsQuery, scardActionDetails);
            queries.add(finalQuery);
            status = executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
        return status;
    }

    public boolean purgeScorecard(String scardId) {
        ArrayList queries = new ArrayList();
        boolean result = false;
        String deleteScardActionDetailsQuery = "delete from PRG_AR_SCARD_ACTION_DETAILS where scard_id=" + scardId;
        String deleteScoreCardLightsQuery = "delete from PRG_AR_SCORECARD_LIGHTS where scard_id=" + scardId;
        String deleteScardMemberKpiRuleQuery = "delete from PRG_AR_SCARD_MEMBER_KPI_RULE where scard_id=" + scardId;
        String deleteScardMemberKpiQuery = "delete from PRG_AR_SCARD_MEMBER_KPI where scard_id=" + scardId;
        String deleteScardMemberQuery = "delete from PRG_AR_SCARD_MEMBER where scard_id=" + scardId;
        String deleteScardUsersQuery = "delete from PRG_AR_SCARD_USERS where scard_id=" + scardId;
        String deleteScardMasterQuery = "delete from PRG_AR_SCARD_MASTER where scard_id=" + scardId;



        try {
            queries.add(deleteScardActionDetailsQuery);
            queries.add(deleteScoreCardLightsQuery);
            queries.add(deleteScardMemberKpiRuleQuery);
            queries.add(deleteScardMemberKpiQuery);
            queries.add(deleteScardMemberQuery);
            queries.add(deleteScardUsersQuery);
            queries.add(deleteScardMasterQuery);


            result = executeMultiple(queries);

        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return result;
    }

    public boolean DeleteScorecard(String scardId, String userId) {
        ArrayList queries = new ArrayList();
        boolean result = false;
        String deleteScardUsersQuery = "delete from PRG_AR_SCARD_USERS where scard_id=" + scardId + " and user_id=" + userId;
        try {
            queries.add(deleteScardUsersQuery);
            result = executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return result;
    }

    public List<ScoreCardAction> getScoreCardActions(String scoreCardId, String memberId) {
        List<ScoreCardAction> actionList = new ArrayList<ScoreCardAction>();
        String selectQuery = "";

        if (memberId == null) {
            selectQuery = "select scard_id, score, action_type, action_detail, impact, action_date,start_date,end_date,scard_mem_id, scard_action_detail_id, action_element_name "
                    + "from prg_ar_scard_action_details where scard_id=" + scoreCardId + " order by end_date desc";
        } else {
            selectQuery = "select scard_id, score, action_type, action_detail, impact, action_date,start_date,end_date,scard_mem_id, scard_action_detail_id, action_element_name "
                    + "from prg_ar_scard_action_details where scard_id=" + scoreCardId + " and scard_mem_id=" + memberId + " order by end_date desc";
        }

        try {
            PbReturnObject retObj = execSelectSQL(selectQuery);
            String[] colNames = retObj.getColumnNames();
            if (retObj != null && retObj.getRowCount() > 0) {
                for (int i = 0; i < retObj.getRowCount(); i++) {
                    ScoreCardAction action = new ScoreCardAction();
                    action.setScorecardId(retObj.getFieldValueInt(i, 0));
                    action.setScore(retObj.getFieldValueBigDecimal(i, 1).doubleValue());
                    action.setActionType(retObj.getFieldValueString(i, 2));
                    action.setActionDetail(retObj.getFieldValueClobString(i, colNames[3]));
                    action.setImpact(retObj.getFieldValueClobString(i, colNames[4]));
                    action.setActionDate(retObj.getFieldValueDate(i, 5));
                    action.setStartDate(retObj.getFieldValueDate(i, 6));
                    action.setEndDate(retObj.getFieldValueDate(i, 7));
                    action.setMemberId(retObj.getFieldValueInt(i, 8));
                    action.setScardActionDetailId(retObj.getFieldValueInt(i, 9));
                    action.setActionItemName(retObj.getFieldValueString(i, 10));
                    actionList.add(action);
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }


        return actionList;
    }

    public static void main(String[] args) {
        ScorecardTemplateDAO DAO = new ScorecardTemplateDAO();
        // String str = DAO.getGraphReportsByBuzRoles("221");
        DAO.purgeScorecard("1");
    }

    public void updateImpact(int scardActionDetailId, String impact) {
        String query = getResourceBundle().getString("updateImpact");
        Object[] objArray = null;
        String finalQuery = "";
        try {
            objArray = new Object[2];
            objArray[0] = impact;
            objArray[1] = scardActionDetailId;
            finalQuery = buildQuery(query, objArray);
            execUpdateSQL(finalQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }
    }
}