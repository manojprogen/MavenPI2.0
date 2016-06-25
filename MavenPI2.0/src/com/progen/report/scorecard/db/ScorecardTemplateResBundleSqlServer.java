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
public class ScorecardTemplateResBundleSqlServer extends ListResourceBundle implements Serializable {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"insertScardMaster", "insert into PRG_AR_SCARD_MASTER(SCARD_NAME,SCARD_AREA,NO_OF_DAYS) values('&','&',&)"}, {"insertScardUsers", "insert into PRG_AR_SCARD_USERS(SCARD_ID) values(&)"}, {"insertScardMember", "insert into PRG_AR_SCARD_MEMBER(SCARD_ID,SCARD_TYPE,SCARD_CONTRIBUTION,SCARD_SEQ) values(&,'&',&,&) "}, {"insertScardMemberKpi", "insert into PRG_AR_SCARD_MEMBER_KPI(SCARD_DETALS_ID,SCARD_ID,SCARD_MEM_ID,ELEMENT_ID,PRIOR_ELEMENT_ID,CHANGE_ELEMENT_ID,CHANGEP_ELEMENT_ID,NEXT_ELEMENT_ID,NCHANGE_ELEMENT_ID,NCHANGEP_ELEMENT_ID,SCARD_BASIS,ELEMENT_NAME,TARGET_VALUE,TARGET_TYPE,TARGET_DAYS_MONTHS) values(&,&,&,&,&,&,&,&,&,&,'&','&',&,&,&) "}, {"insertScardMemberKpiRule", "insert into PRG_AR_SCARD_MEMBER_KPI_RULE(SCARD_DETALS_ID,SCARD_ID,SCARD_MEM_ID,SCARD_MEM_VALUE_ST,SCARE_MEM_VALUE_END,SCARD_SCORE) values(&,&,&,&,&,&)"}, {"insertDashletDetails", "insert into PRG_AR_DASHLET_DETAILS(MASTER_ID,COMPONENT_ID) values(&,&) "}, {"insertScardLights", "insert into PRG_AR_SCORECARD_LIGHTS(SCARD_ID,SCORE_START,SCORE_END,LIGHT_COLOR) values(&,&,&,'&')"}, {"insertScardActionTypes", "insert into PRG_AR_SCORECARD_ACTION_TYPES(SCARD_ACTION_TYPE,SCARD_ACTION_TEXT) values('&','&')"}, {"insertScardActionDetails", "insert into prg_ar_scard_action_details(SCARD_ID,SCARD_MEM_ID,SCORE,ACTION_TYPE,ACTION_DETAIL,ACTION_DATE,START_DATE,END_DATE,ACTION_ELEMENT_NAME) values(&,&,&,'&','&',getdate(),convert(datetime,'&',120),convert(datetime,'&',120),'&')"}, {"updateImpact", "update prg_ar_scard_action_details set impact='&' where scard_action_detail_id=&"}
    };
}