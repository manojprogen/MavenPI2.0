/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.scorecard.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class ScorecardTemplateResourceBundle extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"insertScardMaster", "insert into PRG_AR_SCARD_MASTER(SCARD_ID,SCARD_NAME,SCARD_AREA,NO_OF_DAYS) values(PRG_AR_SCARD_MASTER_SEQ.nextval,'&','&',&)"}, {"insertScardUsers", "insert into PRG_AR_SCARD_USERS(SCARD_ID,USER_ID) values(&,PRG_AR_SCARD_USERS_SEQ.nextval)"}, {"insertScardMember", "insert into PRG_AR_SCARD_MEMBER(SCARD_ID,SCARD_MEM_ID,SCARD_TYPE,SCARD_CONTRIBUTION,SCARD_SEQ) values(&,PRG_AR_SCARD_MEMEBER_SEQ.nextval,'&',&,&) "}, {"insertScardMemberKpi", "insert into PRG_AR_SCARD_MEMBER_KPI(SCARD_MEMBER_KPI_ID,SCARD_DETALS_ID,SCARD_ID,SCARD_MEM_ID,ELEMENT_ID,PRIOR_ELEMENT_ID,CHANGE_ELEMENT_ID,CHANGEP_ELEMENT_ID,NEXT_ELEMENT_ID,NCHANGE_ELEMENT_ID,NCHANGEP_ELEMENT_ID,SCARD_BASIS,ELEMENT_NAME,TARGET_VALUE,TARGET_TYPE,TARGET_DAYS_MONTHS) values(PRG_AR_SCARD_MEMBER_KPI_SEQ.nextval,&,&,&,&,&,&,&,&,&,&,'&','&',&,&,&) "}, {"insertScardMemberKpiRule", "insert into PRG_AR_SCARD_MEMBER_KPI_RULE(SCARD_MEMBER_KPI_RULE_ID,SCARD_DETALS_ID,SCARD_ID,SCARD_MEM_ID,SCARD_MEM_VALUE_ST,SCARE_MEM_VALUE_END,SCARD_SCORE) values(PRG_AR_SCARD_KPI_RULE_SEQ.nextval,&,&,&,&,&,&)"}, {"insertDashletDetails", "insert into PRG_AR_DASHLET_DETAILS(DASHLET_DETAIL_ID,MASTER_ID,COMPONENT_ID) values(PRG_AR_DASHLET_DETAILS_SEQ.nextval,&,&) "}, {"insertScardLights", "insert into PRG_AR_SCORECARD_LIGHTS(SCARD_ID,SCORE_START,SCORE_END,LIGHT_COLOR,SCARD_LIGHTS_ID) values(&,&,&,'&',PRG_AR_SCORECARD_LIGHTS_SEQ.nextval)"}, {"insertScardActionTypes", "insert into PRG_AR_SCORECARD_ACTION_TYPES(SCARD_ACTION_TYPE_ID,SCARD_ACTION_TYPE,SCARD_ACTION_TEXT) values(PRG_AR_SCARD_ACTION_TYPES_SEQ.nextval,'&','&')"}, {"insertScardActionDetails", "insert into prg_ar_scard_action_details(SCARD_ACTION_DETAIL_ID,SCARD_ID,SCARD_MEM_ID,SCORE,ACTION_TYPE,ACTION_DETAIL,ACTION_DATE,START_DATE,END_DATE,ACTION_ELEMENT_NAME) values(PRG_AR_SCARD_ACTION_DET_SEQ.nextval,&,&,&,'&','&',sysdate,TO_DATE('&','MM-dd-yyyy'),TO_DATE('&','MM-dd-yyyy'),'&')"}, {"updateImpact", "update prg_ar_scard_action_details set impact='&' where scard_action_detail_id=&"}
    };
}