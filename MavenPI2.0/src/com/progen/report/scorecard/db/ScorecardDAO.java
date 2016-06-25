/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.db;

import com.progen.report.KPIElement;
import com.progen.report.scorecard.*;
import com.progen.report.scorecard.bd.ScorecardBuilder;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author progen
 */
public class ScorecardDAO extends PbDb {

    public static Logger logger = Logger.getLogger(ScorecardDAO.class);
    ResourceBundle resourceBundle;

    private ResourceBundle getResourceBundle() {
        if (this.resourceBundle == null) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                resourceBundle = new ScorecardResBundleSqlServer();
            } else {
                resourceBundle = new ScorecardResourceBundle();
            }
        }

        return resourceBundle;
    }

    public List<ScoreCard> getScoreCards(List<String> scardIdList) {
        List<ScoreCard> scoreCardList = new ArrayList<ScoreCard>();
        try {
            Map<String, ScoreCard> scMap = new HashMap<String, ScoreCard>();
            String[] dbColNames = null;
            StringBuilder scardElemList = new StringBuilder();
            for (String masterId : scardIdList) {
                scardElemList.append(",").append(masterId);
            }

            if (scardElemList.length() > 0) {
                String masterIdQryStr = scardElemList.substring(1);
                String queryStr = getResourceBundle().getString("getScoreCards");
                Object objArr[] = new Object[1];
                objArr[0] = masterIdQryStr;
                String finalQuery = buildQuery(queryStr, objArr);

                PbReturnObject retObj = execSelectSQL(finalQuery);
                if (retObj != null && retObj.getRowCount() > 0) {
                    dbColNames = retObj.getColumnNames();
                    for (int i = 0; i < retObj.getRowCount(); i++) {
                        String scardId = retObj.getFieldValueString(i, dbColNames[0]); //Scorecard Id

                        ScoreCard scoreCard = scMap.get(scardId);
                        if (scoreCard == null) {
                            scoreCard = new ScoreCard();
                            scoreCard.setScoreCardId(scardId);
                            scoreCard.setScoreCardName(retObj.getFieldValueString(i, dbColNames[1]));
                            scoreCard.setScoreCardArea(retObj.getFieldValueString(i, dbColNames[2]));
                            scoreCard.setNoOfDays(retObj.getFieldValueInt(i, dbColNames[3]));
                            scoreCard.setLightType(retObj.getFieldValueString(i, dbColNames[4]));
                            scoreCard.setFolderId(retObj.getFieldValueString(i, dbColNames[16]));
                            if (retObj.getFieldValueString(0, dbColNames[23]) != null && !retObj.getFieldValueString(0, dbColNames[23]).equalsIgnoreCase("")) {
                                scoreCard.setTargetScore(Double.parseDouble(retObj.getFieldValueString(0, dbColNames[23])));
                            }
                            // Get the score card color rules
                            String colorSqlStr = "select score_start, score_end, light_color,operator from prg_ar_scorecard_lights where scardid=" + scardId;
                            PbReturnObject colorRetObj = execSelectSQL(colorSqlStr);
                            if (colorRetObj != null && colorRetObj.getRowCount() > 0) {
                                for (int j = 0; j < colorRetObj.getRowCount(); j++) {
                                    ScoreCardColorRule scardColor = new ScoreCardColorRule();
                                    BigDecimal startVal = colorRetObj.getFieldValueBigDecimal(j, 0);
                                    if (colorRetObj.getFieldValue(j, 0) == null) {
                                        scardColor.setStartValue(null);
                                    } else {
                                        scardColor.setStartValue(startVal.doubleValue());
                                    }

                                    BigDecimal endVal = colorRetObj.getFieldValueBigDecimal(j, 1);
                                    if (colorRetObj.getFieldValue(j, 1) == null) {
                                        scardColor.setEndValue(null);
                                    } else {
                                        scardColor.setEndValue(endVal.doubleValue());
                                    }
                                    scardColor.setColor(colorRetObj.getFieldValueString(j, 2));
                                    scardColor.setOperator(colorRetObj.getFieldValueString(j, 3));
                                    scoreCard.getColorList().add(scardColor);
                                }
                            }
                            scMap.put(scardId, scoreCard);
                        }

                        String memberType = retObj.getFieldValueString(i, dbColNames[14]);
                        String childScardId = retObj.getFieldValueString(i, dbColNames[15]);
                        String memberId = retObj.getFieldValueString(i, dbColNames[5]);
                        double contribution = retObj.getFieldValueBigDecimal(i, dbColNames[6]).doubleValue();
                        String dimId = retObj.getFieldValueString(i, dbColNames[17]);
                        String dimValue = retObj.getFieldValueString(i, dbColNames[18]);
                        String period = retObj.getFieldValueString(i, dbColNames[19]);
                        String targetMeasId = retObj.getFieldValueString(i, dbColNames[20]);
                        String targetMeasureType = retObj.getFieldValueString(i, dbColNames[21]);
                        String targetMeasureValue = retObj.getFieldValueString(i, dbColNames[22]);
                        ScorecardComponent component;

                        if (ScoreCardConstants.SCARD_MEMBER_TYPE_DIM_SCARD.equalsIgnoreCase(memberType)
                                || ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD.equalsIgnoreCase(memberType)) {
                            List<String> childIds = new ArrayList<String>();
                            childIds.add(childScardId);
                            List<ScoreCard> childScorecardLst = getScoreCards(childIds);
                            ScoreCard childScorecard = childScorecardLst.get(0);
                            childScorecard.setContribution(contribution);
                            component = childScorecard;
                        } else {
                            int sequence = retObj.getFieldValueInt(i, dbColNames[7]);
                            String elemId = retObj.getFieldValueString(i, dbColNames[8]);
                            String priorElementId = retObj.getFieldValueString(i, dbColNames[9]);
                            String changeElementId = retObj.getFieldValueString(i, dbColNames[10]);
                            String changePercElementId = retObj.getFieldValueString(i, dbColNames[11]);
                            String basis = retObj.getFieldValueString(i, dbColNames[12]);
                            String elementName = retObj.getFieldValueString(i, dbColNames[13]);

                            ScoreCardMember member = new ScoreCardMember();
                            member.setMemberId(memberId);
                            member.setContribution(contribution);
                            member.setSequence(sequence);
                            member.setElementId(elemId);
                            member.setElementName(elementName);
                            member.setPriorElementId(priorElementId);
                            member.setChangeElementId(changeElementId);
                            member.setChangePercElementId(changePercElementId);
                            member.setBasis(basis);
                            member.setType(memberType);
                            member.setPeriod(period);
                            if (!("".equalsIgnoreCase(dimId))) {
                                member.setDimElementId(dimId);
                                member.setDimValue(dimValue);
                            }
                            if (ScoreCardConstants.SCORECARD_BASIS_TARGET.equalsIgnoreCase(basis)
                                    || ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC.equalsIgnoreCase(basis)) {
                                member.setTargetMeasureType(targetMeasureType);
                                if (ScoreCardConstants.TARGET_MEASURE.equalsIgnoreCase(targetMeasureType)) {
                                    member.setTargetElementId(targetMeasId);
                                } else if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(targetMeasureType)) {
                                    member.setTargetMeasureValue(Double.parseDouble(targetMeasureValue));
                                }
                            }
                            component = member;
                        }
                        //Query and populate the rules for this member
                        String ruleQry = "select scard_mem_value_st, scard_mem_value_end, scard_score, operator from prg_ar_scard_member_kpi_rule"
                                + " where scard_id=" + scardId + " and scard_mem_id=" + memberId
                                + " order by scard_mem_value_st";
                        PbReturnObject ruleRetObj = execSelectSQL(ruleQry);
                        if (ruleRetObj != null && ruleRetObj.getRowCount() > 0) {
                            for (int j = 0; j < ruleRetObj.getRowCount(); j++) {
                                ScoreCardMemberRule rule = new ScoreCardMemberRule();
                                BigDecimal stValue = ruleRetObj.getFieldValueBigDecimal(j, 0);
                                if (stValue != null) {
                                    rule.setStartValue(stValue.doubleValue());
                                }
                                BigDecimal endValue = ruleRetObj.getFieldValueBigDecimal(j, 1);
                                if (endValue != null) {
                                    rule.setEndValue(endValue.doubleValue());
                                }
                                BigDecimal score = ruleRetObj.getFieldValueBigDecimal(j, 2);
                                if (score != null) {
                                    rule.setScore(score.doubleValue());
                                }
                                rule.setOperator(ruleRetObj.getFieldValueString(j, 3));
                                component.addRule(rule);
                            }
                        }
                        scoreCard.addMember(component);
                    }
                }

                Set<String> keySet = scMap.keySet();
                Iterator<String> keyIter = keySet.iterator();
                while (keyIter.hasNext()) {
                    String key = keyIter.next();
                    scoreCardList.add(scMap.get(key));
                }
            }
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return scoreCardList;
    }

    public List<KPIElement> getKPIElements(List<String> elementIds) {
        List<KPIElement> kpiElements = new ArrayList<KPIElement>();
        try {
            StringBuilder qryElements = new StringBuilder();
            for (int i = 0; i < elementIds.size(); i++) {
                qryElements.append(",").append(elementIds.get(i));
            }
            String elementList = "(" + qryElements.substring(1) + ")";

            PbReturnObject retObj = null;
            String finalQuery = "select ELEMENT_ID , REF_ELEMENT_ID , REF_ELEMENT_TYPE , AGGREGATION_TYPE  from  PRG_USER_ALL_INFO_DETAILS "
                    + "where ELEMENT_ID in " + elementList + " OR REF_ELEMENT_ID in " + elementList + "  order by REF_ELEMENT_TYPE asc  ";

            retObj = execSelectSQL(finalQuery);
            if (retObj != null && retObj.getRowCount() > 0) {
                String[] ColNames = retObj.getColumnNames();
                for (int looper = 0; looper < retObj.getRowCount(); looper++) {
                    //Add the details in DashBoardCollection object. This is done to avoid requerying the data
                    String refElementId = retObj.getFieldValueString(looper, ColNames[1]);
                    KPIElement kpiElem = new KPIElement();
                    kpiElem.setElementId(retObj.getFieldValueString(looper, ColNames[0]));
                    kpiElem.setRefElementId(refElementId);
                    kpiElem.setRefElementType(retObj.getFieldValueString(looper, ColNames[2]));
                    kpiElem.setAggregationType(retObj.getFieldValueString(looper, ColNames[3]));
                    kpiElements.add(kpiElem);
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return kpiElements;
    }

    public String getFolderIDForScoreCard(String scardId) {
        String folderId = null;
        try {
            String query = "select folder_id from prg_ar_scard_master where scard_id=" + scardId;
            PbReturnObject retObj = execSelectSQL(query);
            if (retObj != null && retObj.getRowCount() > 0) {
                folderId = retObj.getFieldValueString(0, 0);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return folderId;
    }

    //insertion of scorecards
    public boolean insertScoreCard(ScoreCard scoreCard, String mode) {
        int scoreCardId;
        if ("Edit".equalsIgnoreCase(mode)) {
            scoreCardId = Integer.parseInt(scoreCard.getScoreCardId());
            if (scoreCard.isDimensionBased()) {
                this.purgeScoreCardForDimension(scoreCardId);
            }
            this.purgeScorecard(scoreCardId, mode);
            this.updateScardMaster(scoreCard);

        } else {
            scoreCardId = this.insertScoreCardMaster(scoreCard);
        }


        boolean isDimBased = scoreCard.isDimensionBased();
        boolean status = false;
        if (isDimBased) {
            status = this.insertDimensionScoreCardComponents(scoreCardId, scoreCard.getMemberList());
        } else {
            status = this.insertScoreCardComponents(scoreCardId, scoreCard.getMemberList());
        }

        if (status == true) {
            this.saveScardToUser(scoreCardId, scoreCard.getUserId());
        }

        if ("Target".equalsIgnoreCase(scoreCard.getLightType())) {
            ArrayList<String> colorQryLst = this.saveColorToScorecard(scoreCard, scoreCardId);
            super.executeMultiple(colorQryLst);
        }
        return status;
    }

    private int insertScoreCardMaster(ScoreCard scoreCard) {
        return this.saveScorecardMasterDetails(scoreCard);
    }

    private boolean insertDimensionScoreCardComponents(int scardId, List<ScorecardComponent> components) {
        for (ScorecardComponent component : components) {
            ScoreCard dimScoreCard = (ScoreCard) component;
            int scoreCardId = this.insertScoreCardMaster(dimScoreCard);
            this.insertScoreCardComponents(scoreCardId, dimScoreCard.getMemberList());
        }
        this.insertScoreCardComponents(scardId, components);
        return true;
    }

    private boolean insertScoreCardComponents(int scardId, List<ScorecardComponent> components) {
        int sCardSeq = 1;
        for (ScorecardComponent component : components) {
            this.insertScoreCardMember(scardId, component, sCardSeq);
            sCardSeq++;
        }
        return true;
    }

    private boolean insertScoreCardMember(int scardId, ScorecardComponent member, int sCardSeq) {
        int sCardMemId = 0;
        String unit = "";
        if (member instanceof ScoreCard) {
            //insert into Scorecard_member
            ScoreCard scoreCard = (ScoreCard) member;
            // if(scoreCard.isDimensionBased())
            sCardMemId = this.saveScardMemberDetails(scardId, scoreCard, sCardSeq);

        } else {
            ScoreCardMember scoreCardMember = (ScoreCardMember) member;
            sCardMemId = this.saveScardMemberDetails(scardId, scoreCardMember, sCardSeq);
            this.saveScardMemberKpiDetails(scardId, sCardMemId, scoreCardMember);
            unit = scoreCardMember.getUnit();
            //insert into Scorecard_member
            //insert into Scorecard_member_kpi
        }
        //query should come here


        ArrayList<String> memberRuleQrys = this.insertScoreCardMemberRule(scardId, sCardMemId, member.getRuleList(), unit);
        // 
        super.executeMultiple(memberRuleQrys);
        return true;
    }

    private ArrayList<String> insertScoreCardMemberRule(int scardId, int memId, List<ScoreCardMemberRule> rules, String unit) {
        ArrayList<String> memRuleQrys = new ArrayList<String>();
        for (ScoreCardMemberRule rule : rules) {
            memRuleQrys.add(this.getMemberRuleInsertQuery(scardId, memId, rule, unit));
        }
        return memRuleQrys;
    }

    private String getMemberRuleInsertQuery(int scardId, int memId, ScoreCardMemberRule rule, String unit) {
        return this.saveScardMemberKpiRules(scardId, memId, rule, unit);
    }

    public int saveScorecardMasterDetails(ScoreCard scoreCard) {
        Object bindObj[] = null;
        int scardMasterId = 0;
        String saveScardDetailsQry = getResourceBundle().getString("saveScardDetails");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {

            bindObj = new Object[10];

            bindObj[0] = scoreCard.getScoreCardName();
            bindObj[1] = scoreCard.getScoreCardArea();
            bindObj[2] = 0;
            if (scoreCard.getUserId() != null && !"".equalsIgnoreCase(scoreCard.getUserId())) {
                bindObj[3] = scoreCard.getUserId();
                bindObj[4] = scoreCard.getUserId();
            } else {
                bindObj[3] = "null";
                bindObj[4] = "null";
            }
            bindObj[5] = "getdate()";
            bindObj[6] = "getdate()";
            bindObj[7] = scoreCard.getLightType();
            bindObj[8] = scoreCard.getFolderId();
            bindObj[9] = scoreCard.getTargetScore();
            finalQry = super.buildQuery(saveScardDetailsQry, bindObj);
            try {
                scardMasterId = super.insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCARD_MASTER");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }


        } else {
            try {
                pbro = super.execSelectSQL("select PRG_AR_SCARD_MASTER_SEQ.nextval NEXT from dual");
            } catch (SQLException ex) {
            }
            scardMasterId = pbro.getFieldValueInt(0, "NEXT");
            bindObj = new Object[11];
            bindObj[0] = scardMasterId;
            bindObj[1] = scoreCard.getScoreCardName();
            bindObj[2] = scoreCard.getScoreCardArea();
            bindObj[3] = 0;
            if (scoreCard.getUserId() != null && !"".equalsIgnoreCase(scoreCard.getUserId())) {
                bindObj[4] = scoreCard.getUserId();
                bindObj[5] = scoreCard.getUserId();
            } else {
                bindObj[4] = "null";
                bindObj[5] = "null";
            }

            bindObj[6] = "sysdate";
            bindObj[7] = "sysdate";
            bindObj[8] = scoreCard.getLightType();
            bindObj[9] = scoreCard.getFolderId();
            bindObj[10] = scoreCard.getTargetScore();
            finalQry = super.buildQuery(saveScardDetailsQry, bindObj);
            // 
            try {
                super.execUpdateSQL(finalQry);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        }
        scoreCard.setScoreCardId(Integer.valueOf(scardMasterId).toString());
        return scardMasterId;
    }

    public int saveScardMemberDetails(int sCardId, ScoreCard scoreCard, int sCardSeq) {
        Object bindObj[] = null;
        String saveScardMemDetailsQry = getResourceBundle().getString("saveScardMemDetails");
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        int sCardMemId = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[7];

            bindObj[0] = sCardId;
            bindObj[1] = ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD;
            bindObj[2] = scoreCard.getContribution();
            bindObj[3] = sCardSeq;
            bindObj[4] = "null";
            bindObj[5] = "null";
            if (scoreCard.getScoreCardId() != null) {
                bindObj[6] = scoreCard.getScoreCardId().replace("Scards", "");//childScoreCardId
            } else {
                bindObj[6] = "null";
            }
            finalQry = super.buildQuery(saveScardMemDetailsQry, bindObj);
            // 
            try {
                sCardMemId = super.insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCARD_MEMEBER");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return sCardMemId;
        } else {
            try {
                pbro = super.execSelectSQL("select PRG_AR_SCARD_MEMEBER_SEQ.nextval NEXT from dual");
            } catch (SQLException ex) {
            }
            sCardMemId = pbro.getFieldValueInt(0, "NEXT");
            bindObj = new Object[8];
            bindObj[0] = sCardId;
            bindObj[1] = sCardMemId;
            bindObj[2] = ScoreCardConstants.SCARD_MEMBER_TYPE_NESTED_SCARD;
            bindObj[3] = scoreCard.getContribution();
            bindObj[4] = sCardSeq;
            bindObj[5] = "null";
            bindObj[6] = "null";
            if (scoreCard.getScoreCardId() != null) {
                bindObj[7] = scoreCard.getScoreCardId().replace("Scards", "");//childScoreCardId
            } else {
                bindObj[7] = "null";
            }
            finalQry = super.buildQuery(saveScardMemDetailsQry, bindObj);
            try {
                super.execUpdateSQL(finalQry);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            // 
            return sCardMemId;
        }
    }

    public int saveScardMemberDetails(int sCardId, ScoreCardMember scoreCardMember, int sCardSeq) {
        Object bindObj[] = null;
        String saveScardMemDetailsQry = getResourceBundle().getString("saveScardMemDetails");
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        int sCardMemId = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[7];

            bindObj[0] = sCardId;
            bindObj[1] = scoreCardMember.getType();
            bindObj[2] = scoreCardMember.getContribution();
            bindObj[3] = sCardSeq;
            bindObj[4] = scoreCardMember.getUnit();
            bindObj[5] = scoreCardMember.getPeriod();
            bindObj[6] = "null";//childScoreCardId
            finalQry = super.buildQuery(saveScardMemDetailsQry, bindObj);

            try {
                sCardMemId = super.insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCARD_MEMBER");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return sCardMemId;
        } else {
            try {
                pbro = super.execSelectSQL("select PRG_AR_SCARD_MEMEBER_SEQ.nextval NEXT from dual");
            } catch (SQLException ex) {
            }
            sCardMemId = pbro.getFieldValueInt(0, "NEXT");
            bindObj = new Object[8];
            bindObj[0] = sCardId;
            bindObj[1] = sCardMemId;
            bindObj[2] = scoreCardMember.getType();
            bindObj[3] = scoreCardMember.getContribution();
            bindObj[4] = sCardSeq;
            bindObj[5] = scoreCardMember.getUnit();
            bindObj[6] = scoreCardMember.getPeriod();
            bindObj[7] = "null";//childScorecardId
            finalQry = super.buildQuery(saveScardMemDetailsQry, bindObj);
            //
            try {
                super.execUpdateSQL(finalQry);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return sCardMemId;
        }
    }

    public int saveScardMemberKpiDetails(int sCardId, int sCardMemId, ScoreCardMember scoreCardMem) {
        Object bindObj[] = null;
        String saveScardMemKpiDetailsQry = getResourceBundle().getString("saveScardMemKpiDetailsForDimension");
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        int sCardKpiMemId = 0;
        PbReturnObject priorRetObj = null;
        priorRetObj = getRetObj(scoreCardMem.getElementId());
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[14];

            bindObj[0] = sCardId;
            bindObj[1] = sCardMemId;
            bindObj[2] = scoreCardMem.getElementId();
            bindObj[3] = scoreCardMem.getBasis();
//                if(scoreCardMem.getBasis()!=null&&!"".equalsIgnoreCase(scoreCardMem.getBasis())){
//                if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC))
//                   bindObj[4] =ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT))
//                     bindObj[4] =ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_TARGET))
//                    bindObj[4] =ScoreCardConstants.SCORECARD_BASIS_TARGET+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC))
//                    bindObj[4] =ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC+" "+scoreCardMem.getElementName();
//                 else
//                    bindObj[4] = scoreCardMem.getElementName();
//                }
//                else
            bindObj[4] = scoreCardMem.getElementName();

            bindObj[5] = priorRetObj.getFieldValueInt(1, "ELEMENT_ID");
            bindObj[6] = priorRetObj.getFieldValueInt(2, "ELEMENT_ID");
            bindObj[7] = priorRetObj.getFieldValueInt(3, "ELEMENT_ID");
            if (scoreCardMem.getDimElementId() != null && !"".equalsIgnoreCase(scoreCardMem.getDimElementId())) {
                bindObj[8] = scoreCardMem.getDimElementId();
            } else {
                bindObj[8] = "null";
            }
            bindObj[9] = scoreCardMem.getDimValue();
            if (scoreCardMem.getTargetElementId() != null && !scoreCardMem.getTargetElementId().equalsIgnoreCase("")) {
                bindObj[10] = scoreCardMem.getTargetElementId();
            } else {
                bindObj[10] = "null";
            }

            bindObj[11] = scoreCardMem.getTargetElementName();

            bindObj[12] = scoreCardMem.getTargetMeasureType();
            if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(scoreCardMem.getTargetMeasureType())) {
                bindObj[13] = new BigDecimal(scoreCardMem.getTargetMeasureValue());
            } else {
                bindObj[13] = "null";
            }

            finalQry = super.buildQuery(saveScardMemKpiDetailsQry, bindObj);
            // 
            try {
                sCardKpiMemId = super.insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCARD_MEMBER_KPI");
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return sCardKpiMemId;
        } else {
            bindObj = new Object[15];
            try {
                pbro = super.execSelectSQL("select PRG_AR_SCARD_MEMBER_KPI_SEQ.nextval NEXT from dual");
            } catch (SQLException ex) {
            }
            sCardKpiMemId = pbro.getFieldValueInt(0, "NEXT");

            bindObj[0] = sCardKpiMemId;
            bindObj[1] = sCardId;
            bindObj[2] = sCardMemId;
            bindObj[3] = scoreCardMem.getElementId();
            bindObj[4] = scoreCardMem.getBasis();
//                if(scoreCardMem.getBasis()!=null&&!"".equalsIgnoreCase(scoreCardMem.getBasis())){
//                if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC))
//                   bindObj[5] = ScoreCardConstants.SCORECARD_BASIS_CHANGE_PERC+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT))
//                     bindObj[5] = ScoreCardConstants.SCORECARD_BASIS_CHANGE_AMT+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_TARGET))
//                    bindObj[5] =ScoreCardConstants.SCORECARD_BASIS_TARGET+" "+scoreCardMem.getElementName();
//                else if(scoreCardMem.getBasis().equalsIgnoreCase(ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC))
//                    bindObj[5] =ScoreCardConstants.SCORECARD_BASIS_TARGET_PERC+" "+scoreCardMem.getElementName();
//                else
//                    bindObj[5] = scoreCardMem.getElementName();
//                }
//                else
            bindObj[5] = scoreCardMem.getElementName();

            bindObj[6] = priorRetObj.getFieldValueInt(1, "ELEMENT_ID");
            bindObj[7] = priorRetObj.getFieldValueInt(2, "ELEMENT_ID");
            bindObj[8] = priorRetObj.getFieldValueInt(3, "ELEMENT_ID");
            if (scoreCardMem.getDimElementId() != null && !"".equalsIgnoreCase(scoreCardMem.getDimElementId())) {
                bindObj[9] = scoreCardMem.getDimElementId();
            } else {
                bindObj[9] = "null";
            }
            bindObj[10] = scoreCardMem.getDimValue();

            if (scoreCardMem.getTargetElementId() != null && !scoreCardMem.getTargetElementId().equalsIgnoreCase("")) {
                bindObj[11] = scoreCardMem.getTargetElementId();
            } else {
                bindObj[11] = "null";
            }

            bindObj[12] = scoreCardMem.getTargetElementName();

            bindObj[13] = scoreCardMem.getTargetMeasureType();
            if (ScoreCardConstants.TARGET_VALUE.equalsIgnoreCase(scoreCardMem.getTargetMeasureType())) {
                bindObj[14] = new BigDecimal(scoreCardMem.getTargetMeasureValue());
            } else {
                bindObj[14] = "null";
            }

            finalQry = super.buildQuery(saveScardMemKpiDetailsQry, bindObj);

            try {
                super.execUpdateSQL(finalQry);
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
            return sCardKpiMemId;
        }


    }

    public String saveScardMemberKpiRules(int scardId, int memId, ScoreCardMemberRule rule, String units) {
        Object bindObj[] = null;
        String saveScardMemKpiDetailsQry = getResourceBundle().getString("saveScardMemKpiRules");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[7];
            bindObj[0] = memId;
            bindObj[1] = scardId;
            bindObj[2] = memId;

            if (units.equalsIgnoreCase("Thousands")) {
                bindObj[3] = rule.getStartValue() * 1000;
            } else if (units.equalsIgnoreCase("Lakhs")) {
                bindObj[3] = rule.getStartValue() * 100000;
            } else if (units.equalsIgnoreCase("Millions")) {
                bindObj[3] = rule.getStartValue() * 1000000;
            } else if (units.equalsIgnoreCase("Crores")) {
                bindObj[3] = rule.getStartValue() * 10000000;
            } else if (units.equalsIgnoreCase("Billions")) {
                bindObj[3] = rule.getStartValue() * 1000000000;
            } else {
                bindObj[3] = rule.getStartValue();
            }



            if (rule.getOperator().equalsIgnoreCase("<>")) {
                if (units.equalsIgnoreCase("Thousands")) {
                    bindObj[4] = rule.getEndValue() * 1000;
                } else if (units.equalsIgnoreCase("Lakhs")) {
                    bindObj[4] = rule.getEndValue() * 100000;
                } else if (units.equalsIgnoreCase("Millions")) {
                    bindObj[4] = rule.getEndValue() * 1000000;
                } else if (units.equalsIgnoreCase("Crores")) {
                    bindObj[4] = rule.getEndValue() * 10000000;
                } else if (units.equalsIgnoreCase("Billions")) {
                    bindObj[4] = rule.getEndValue() * 1000000000;
                } else {
                    bindObj[4] = rule.getEndValue();
                }

            } else {
                bindObj[4] = "null";
            }
            bindObj[5] = rule.getScore();
            bindObj[6] = rule.getOperator();
            finalQry = pbdb.buildQuery(saveScardMemKpiDetailsQry, bindObj);
            // 
            return finalQry;

        } else {
            bindObj = new Object[8];
            bindObj[0] = "PRG_AR_SCARD_KPI_RULE_SEQ.nextVal";
            bindObj[1] = memId;
            bindObj[2] = scardId;
            bindObj[3] = memId;
            if (units.equalsIgnoreCase("Thousands")) {
                bindObj[4] = rule.getStartValue() * 1000;
            } else if (units.equalsIgnoreCase("Lakhs")) {
                bindObj[4] = rule.getStartValue() * 100000;
            } else if (units.equalsIgnoreCase("Millions")) {
                bindObj[4] = rule.getStartValue() * 1000000;
            } else if (units.equalsIgnoreCase("Crores")) {
                bindObj[4] = rule.getStartValue() * 10000000;
            } else if (units.equalsIgnoreCase("Billions")) {
                bindObj[4] = rule.getStartValue() * 1000000000;
            } else {
                bindObj[4] = rule.getStartValue();
            }


            if (rule.getOperator().equalsIgnoreCase("<>")) {
                // bindObj[5] = endValArry[i];
                if (units.equalsIgnoreCase("Thousands")) {
                    bindObj[5] = rule.getEndValue() * 1000;
                } else if (units.equalsIgnoreCase("Lakhs")) {
                    bindObj[5] = rule.getEndValue() * 100000;
                } else if (units.equalsIgnoreCase("Millions")) {
                    bindObj[5] = rule.getEndValue() * 1000000;
                } else if (units.equalsIgnoreCase("Crores")) {
                    bindObj[5] = rule.getEndValue() * 10000000;
                } else if (units.equalsIgnoreCase("Billions")) {
                    bindObj[5] = rule.getEndValue() * 1000000000;
                } else {
                    bindObj[5] = rule.getEndValue();
                }

            } else {
                bindObj[5] = "null";
            }
            bindObj[6] = rule.getScore();
            bindObj[7] = rule.getOperator();
            finalQry = pbdb.buildQuery(saveScardMemKpiDetailsQry, bindObj);
            return finalQry;
        }
    }

    private PbReturnObject getRetObj(String elementId) {
        PbReturnObject pbro = new PbReturnObject();
        PbDb pbdb = new PbDb();
        Object bindObj[] = null;
        String finalQry = "";
        String getPriorChangeElements = getResourceBundle().getString("getPriorChangeElements");
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[2];
            bindObj[0] = elementId;
            bindObj[1] = elementId;
        } else {
            bindObj = new Object[2];
            bindObj[0] = elementId;
            bindObj[1] = elementId;

        }
        finalQry = pbdb.buildQuery(getPriorChangeElements, bindObj);
        //  
        try {
            pbro = pbdb.execSelectSQL(finalQry);
        } catch (SQLException ex) {
        }
        return pbro;
    }

    public String saveScardToUser(int sCardId, String userId) {
        String saveScardtoUser = getResourceBundle().getString("saveScardtoUser");
        Object bindObj[] = null;
        String finalQry = "";
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[2];
            bindObj[0] = sCardId;
            bindObj[1] = userId;
        } else {
            bindObj = new Object[2];
            bindObj[0] = sCardId;
            bindObj[1] = userId;
        }
        finalQry = super.buildQuery(saveScardtoUser, bindObj);
        try {
            //    
            super.execUpdateSQL(finalQry);
        } catch (Exception ex) {
            logger.error("Exception:", ex);
        }
        return finalQry;
    }

    public void updateScardMaster(ScoreCard scoreCard) {
        String updateScardMaster = getResourceBundle().getString("updateScardMaster");
        PbDb pbdb = new PbDb();
        PbReturnObject pbro = new PbReturnObject();
        String finalQry = "";
        Object[] bindObj = null;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            bindObj = new Object[6];
            bindObj[0] = scoreCard.getScoreCardName();
            bindObj[1] = scoreCard.getScoreCardArea();
            bindObj[2] = scoreCard.getLightType();
            bindObj[3] = "getdate()";
            bindObj[4] = scoreCard.getTargetScore();
            bindObj[5] = scoreCard.getScoreCardId();
        } else {
            bindObj = new Object[6];
            bindObj[0] = scoreCard.getScoreCardName();
            bindObj[1] = scoreCard.getScoreCardArea();
            bindObj[2] = scoreCard.getLightType();
            bindObj[3] = "sysdate";
            bindObj[4] = scoreCard.getTargetScore();
            bindObj[5] = scoreCard.getScoreCardId();

        }
        finalQry = pbdb.buildQuery(updateScardMaster, bindObj);
        try {
            pbdb.execUpdateSQL(finalQry);
        } catch (Exception ex) {
        }
    }

    public boolean purgeScorecard(int scardId, String mode) {
        ArrayList queries = new ArrayList();
        PbDb pbdb = new PbDb();
        boolean result = false;
        String deleteScardActionDetailsQuery = "delete from PRG_AR_SCARD_ACTION_DETAILS where scard_id in (" + scardId + ")";
        String deleteScoreCardLightsQuery = "delete from PRG_AR_SCORECARD_LIGHTS where SCARDID in (" + scardId + ")";
        String deleteScardMemberKpiRuleQuery = "delete from PRG_AR_SCARD_MEMBER_KPI_RULE where scard_id in (" + scardId + ")";
        String deleteScardMemberKpiQuery = "delete from PRG_AR_SCARD_MEMBER_KPI where scard_id in (" + scardId + ")";
        String deleteScardMemberQuery = "delete from PRG_AR_SCARD_MEMBER where scard_id in (" + scardId + ")";
        String deleteScardUsersQuery = "delete from PRG_AR_SCARD_USERS where scard_id in (" + scardId + ")";
        String deleteScardMasterQuery = "delete from PRG_AR_SCARD_MASTER where scard_id in (" + scardId + ")";
        try {

            queries.add(deleteScardActionDetailsQuery);
            queries.add(deleteScoreCardLightsQuery);
            queries.add(deleteScardMemberKpiRuleQuery);
            queries.add(deleteScardMemberKpiQuery);
            queries.add(deleteScardMemberQuery);

            if ("purge".equalsIgnoreCase(mode)) {
                queries.add(deleteScardMasterQuery);
                queries.add(deleteScardUsersQuery);
            }


            result = pbdb.executeMultiple(queries);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return result;
    }

    public void purgeScoreCardForDimension(int scardId) {
        String getChildScardIdsQry = getResourceBundle().getString("getChildScardIds");
        Object bindObj[] = new Object[1];
        bindObj[0] = scardId;
        PbReturnObject pbro = null;
        try {
            pbro = super.execSelectSQL(super.buildQuery(getChildScardIdsQry, bindObj));
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        if (pbro != null && pbro.getRowCount() > 0) {
            for (int i = 0; i < pbro.getRowCount(); i++) {
                this.purgeScorecard(pbro.getFieldValueInt(i, "CHILD_SCARD_ID"), "purge");;
            }
        }
    }

    public ArrayList<String> saveColorToScorecard(ScoreCard scoreCard, int sCardId) {
        String saveScardColorQry = getResourceBundle().getString("saveScardColor");
        List<ScoreCardColorRule> scoreCardColorRulesLst = scoreCard.getColorList();
        ArrayList<String> queryLst = new ArrayList<String>();
        Object bindObj[] = null;
        String finalquery = "";
        for (ScoreCardColorRule colorRule : scoreCardColorRulesLst) {
            if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
                bindObj = new Object[5];
                bindObj[0] = sCardId;
                bindObj[1] = colorRule.getStartValue();
                if ("<>".equalsIgnoreCase(colorRule.getOperator())) {
                    bindObj[2] = colorRule.getEndValue();
                } else {
                    bindObj[2] = "null";
                }
                bindObj[3] = colorRule.getColor();
                bindObj[4] = colorRule.getOperator();
                finalquery = super.buildQuery(saveScardColorQry, bindObj);
                queryLst.add(finalquery);
            } else {
                bindObj = new Object[6];
                bindObj[0] = sCardId;
                bindObj[1] = colorRule.getStartValue();
                if ("<>".equalsIgnoreCase(colorRule.getOperator())) {
                    bindObj[2] = colorRule.getEndValue();
                } else {
                    bindObj[2] = "null";
                }
                bindObj[3] = colorRule.getColor();
                bindObj[4] = "PRG_AR_SCORECARD_LIGHTS_SEQ.nextval";
                bindObj[5] = colorRule.getOperator();
                finalquery = super.buildQuery(saveScardColorQry, bindObj);
                queryLst.add(finalquery);
            }

        }
        return queryLst;
    }

    public int deleteScorecard(String scardIds, String userId) {
        PbDb pbdb = new PbDb();
        int count = 0;
        String deleteScardUsersQuery = "delete from PRG_AR_SCARD_USERS where scard_id in (" + scardIds + ") and user_id in (" + userId + ")";
        try {
            count = pbdb.execUpdateSQL(deleteScardUsersQuery);
        } catch (Exception e) {
            logger.error("Exception:", e);
        }

        return count;
    }

    public String getUserList(String userId) {
        PbReturnObject pbro;
        String userQry = "select PU_ID,PU_LOGIN_ID from PRG_AR_USERS where PU_ID not in(" + userId + ")";
        StringBuilder builder = new StringBuilder();
        StringBuilder userNamessbuilder = new StringBuilder();

        try {
            pbro = execSelectSQL(userQry);
            if (pbro != null) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    builder.append("<li class='navtitle-hover DimensionULClass' style='width: 200px; height: auto; color: white;align=center' id='" + pbro.getFieldValueInt(i, "PU_ID") + "'>");
                    //builder.append("<img src='" + contextPath + "/images/sun.png'></img>");

                    builder.append("<table id='" + pbro.getFieldValueInt(i, "PU_ID") + "'><tbody><tr><td style='color: black;'>" + pbro.getFieldValueString(i, "PU_LOGIN_ID") + "</td></tr></tbody></table>");
                    builder.append("</li>");
                }
            }


        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return builder.toString();
    }

    public void shareScoreCards(String scoreCardIds, String userIds) {
        String shareScorecardsQry = getResourceBundle().getString("shareScorecardsQry");
        String scoreIdsArr[] = scoreCardIds.split(",");
        String userIdsArr[] = userIds.split(",");
        ArrayList<String> queries = new ArrayList<String>();
        Object[] bindObj = new Object[2];
        for (String sId : scoreIdsArr) {
            for (String uId : userIdsArr) {
                bindObj[0] = sId;
                bindObj[1] = uId;
                queries.add(buildQuery(shareScorecardsQry, bindObj));
            }
        }
        executeMultiple(queries);
    }

    public boolean isScorecardEditableForUser(String userId, String scardId) {
        String editCheckQry = getResourceBundle().getString("editCheckQry");
        Object bindObj[] = new Object[2];
        bindObj[0] = scardId;
        bindObj[1] = userId;
        boolean isEditable = false;
        String finalQry = buildQuery(editCheckQry, bindObj);
        try {
            PbReturnObject pbro = execSelectSQL(finalQry);
            if (pbro.getRowCount() > 0) {
                isEditable = true;
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }

        return isEditable;
    }

    public HashMap<String, String> getDimensions(String[] folderIds) {
        String sqlQuery = getResourceBundle().getString("getDimensions");
        HashMap<String, String> dimensionDataMap = new HashMap<String, String>();
        PbReturnObject dimObject = new PbReturnObject();
        String finalQuery = "";
        Object[] values = new Object[1];
        values[0] = folderIds[0];

        finalQuery = buildQuery(sqlQuery, values);
        try {
            dimObject = execSelectSQL(finalQuery);
            if (dimObject.getRowCount() > 0) {
                for (int i = 0; i < dimObject.getRowCount(); i++) {
                    dimensionDataMap.put(dimObject.getFieldValueString(i, "KEY_ELEMENT_ID"), dimObject.getFieldValueString(i, "KEY_MEMBER_NAME"));
                }
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return dimensionDataMap;

    }

    public String getAdhocDetails(String sCardId) {
        String qry = getResourceBundle().getString("getOriginalMeasQry");
        String adHocQry = getResourceBundle().getString("getAdHocMeasQry");
        Object[] bindObj = new Object[1];
        bindObj[0] = sCardId;
        ScorecardBuilder sBuilder = new ScorecardBuilder();
        try {
            PbReturnObject pbro = execSelectSQL(buildQuery(qry, bindObj));
            PbReturnObject pbroAdHoc = execSelectSQL(buildQuery(adHocQry, bindObj));
            return sBuilder.buildOriginalRowsHtml(pbro) + "~" + sBuilder.buildAdHocRowsHtml(pbroAdHoc);
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return "";
    }

    public void saveAdHocRows(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("USERID");
        ScorecardDAO dao = new ScorecardDAO();
        String scoreCardId = String.valueOf(session.getAttribute("ScoreCardId"));
        String originalMemIds[] = request.getParameterValues("memberIds");
        String originalWeightages[] = request.getParameterValues("origWeight");
        String adHocNames[] = request.getParameterValues("adHocMeas");
        String adHocWeightages[] = request.getParameterValues("adHocWeight");

        updateWeigtageForOrigianlMeasures(originalMemIds, originalWeightages);
        inserAdHocRows(adHocNames, adHocWeightages, scoreCardId);

    }

    private void updateWeigtageForOrigianlMeasures(String origMemIds[], String origWeights[]) {
        ArrayList<String> queryList = new ArrayList<String>();
        String updateQry = getResourceBundle().getString("updateMemWeightage");
        Object[] bindObj = new Object[2];
        String finalQry = "";
        for (int i = 0; i < origMemIds.length; i++) {
            bindObj[0] = origWeights[i];
            bindObj[1] = origMemIds[i];
            finalQry = buildQuery(updateQry, bindObj);
            queryList.add(finalQry);
        }
        executeMultiple(queryList);
    }

    private void inserAdHocRows(String[] adHocNames, String[] adHocWeightages, String scardId) {
        deleteAdHocRows(scardId);

        String insertAdhocMemQry = getResourceBundle().getString("insertAdhocMemQry");
        String insertAdhocMemKpiQry = getResourceBundle().getString("insertAdhocMemKpiQry");
        Object[] bindObj = null;
        String finalQry = "";
        int memId = 0;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.SQL_SERVER)) {
            try {
                for (int i = 0; i < adHocNames.length; i++) {
                    bindObj = new Object[2];
                    bindObj[0] = scardId;
                    bindObj[1] = adHocWeightages[i];
                    finalQry = buildQuery(insertAdhocMemQry, bindObj);
                    memId = insertAndGetSequenceInSQLSERVER(finalQry, "PRG_AR_SCARD_MEMBER");
                    bindObj = new Object[4];
                    bindObj[0] = scardId;
                    bindObj[1] = memId;
                    bindObj[2] = "AdHoc";
                    bindObj[3] = adHocNames[i];
                    finalQry = buildQuery(insertAdhocMemKpiQry, bindObj);
                    execUpdateSQL(finalQry);
                }
            } catch (Exception ex) {
                logger.error("Exception:", ex);
            }
        } else {
            try {
                ArrayList<String> queryList = new ArrayList<String>();
                for (int i = 0; i < adHocNames.length; i++) {
                    bindObj = new Object[3];
                    PbReturnObject pbro = execSelectSQL("select PRG_AR_SCARD_MEMEBER_SEQ.nextval NEXT from dual");
                    memId = pbro.getFieldValueInt(0, "NEXT");
                    bindObj[0] = scardId;
                    bindObj[1] = memId;
                    bindObj[2] = adHocWeightages[i];
                    finalQry = buildQuery(insertAdhocMemQry, bindObj);
                    queryList.add(finalQry);
                    bindObj = new Object[4];
                    bindObj[0] = scardId;
                    bindObj[1] = memId;
                    bindObj[2] = "AdHoc";
                    bindObj[3] = adHocNames[i];
                    finalQry = buildQuery(insertAdhocMemKpiQry, bindObj);
                    queryList.add(finalQry);
                }
                executeMultiple(queryList);
            } catch (SQLException ex) {
                logger.error("Exception:", ex);
            }
        }

    }

    private void deleteAdHocRows(String sCardId) {
        String selectQry = "select SCARD_MEM_ID,SCARD_MEMBER_KPI_ID from PRG_AR_SCARD_MEMBER_KPI where SCARD_MEM_ID in (select SCARD_MEM_ID from PRG_AR_SCARD_MEMBER where SCARD_ID=" + sCardId + ") and SCARD_BASIS='AdHoc'";
        try {
            PbReturnObject pbro = execSelectSQL(selectQry);
            String memIds = "";
            String memberKpiIds = "";
            if (pbro.getRowCount() > 0) {
                for (int i = 0; i < pbro.getRowCount(); i++) {
                    memIds += pbro.getFieldValueInt(i, "SCARD_MEM_ID");
                    memberKpiIds += pbro.getFieldValueInt(i, "SCARD_MEMBER_KPI_ID");

                    if (i != pbro.getRowCount() - 1) {
                        memIds += ",";
                        memberKpiIds += ",";
                    }
                }
                String memberQry = "delete from PRG_AR_SCARD_MEMBER where SCARD_MEM_ID in(" + memIds + ")";
                String memberKpiQry = "delete from PRG_AR_SCARD_MEMBER_KPI where SCARD_MEMBER_KPI_ID in(" + memberKpiIds + ")";
                ArrayList<String> queries = new ArrayList<String>();
                queries.add(memberQry);
                queries.add(memberKpiQry);
                executeMultiple(queries);
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
    }

    public PbReturnObject getRetObjForAdHoc(String scardId) {
        String adHocQry = getResourceBundle().getString("getAdHocMeasQry");
        Object[] bindObj = new Object[1];
        bindObj[0] = scardId;
        ScorecardBuilder sBuilder = new ScorecardBuilder();
        PbReturnObject pbro = null;
        try {
            pbro = execSelectSQL(buildQuery(adHocQry, bindObj));

        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return pbro;
    }

    public String getDimensionMeasureName(String childScardId) {
        String qry = "select SCARD_NAME from PRG_AR_SCARD_MASTER where SCARD_ID=" + childScardId;
        try {
            PbReturnObject pbro = execSelectSQL(qry);
            if (pbro.getRowCount() > 0) {
                return pbro.getFieldValueString(0, "SCARD_NAME");
            }
        } catch (SQLException ex) {
            logger.error("Exception:", ex);
        }
        return "";
    }
}
