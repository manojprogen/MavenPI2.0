package com.progen.account.db;

import java.io.Serializable;
import java.util.ListResourceBundle;

public class PbOrgResBundleSqlServer extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getOrganisationName", "select ORGANISATION_NAME from PRG_ORG_MASTER"}, {"insertOrgMaster", "insert into prg_org_master(ORGANISATION_NAME,ORGANISATION_DESC,"
            + "ORG_ST_DATE,ORG_END_DATE) values('&','&', getdate(),'&')"}, {"getSequenceNumber", "select &.nextval from dual"}, {"dispUserQuery", "select pu_id,pu_login_id from prg_ar_users"}, {"getAssignedUser", "select user_id, pr.pu_login_id from user_account_assignment us, prg_ar_users pr where org_id in('&') and pr.pu_id=user_id"}, {"getAccountTypes", "select distinct * from prg_org_master where COALESCE(org_end_date,getdate())>=getdate() order by org_id "}, {"expireAccount", "update prg_org_master set org_end_date=getdate() where org_id=&"}, {"checkRoleAssignment", "SELECT  BUSS_ROLE  FROM PRG_ORG_MASTER where ORG_ID in(&)"}, {"checkReportExistance", "SELECT REPORT_ID from  PRG_AR_REPORT_MASTER where report_id in(select  report_id  from account_report where org_ID in (&))"}, {"chkexpireuser", "select pu_end_date from prg_ar_users where ACCOUNT_TYPE='&'"} //         ,{"chkexpireorg","select org_end_date from prg_org_master where org_id='&'"}
        , {"chkexpireorg", "select getdate()- COALESCE(org_end_date,getdate()) from prg_org_master where org_id='&'"}, {"compInsertRep", "insert into account_report(REPORT_ID) values('&')"}, {"getActiveQuery", "select SETUP_NUM_VALUE from PRG_GBL_SETUP_VALUES where SETUP_KEY='ACTIVE_KEY'"}
    };
}
