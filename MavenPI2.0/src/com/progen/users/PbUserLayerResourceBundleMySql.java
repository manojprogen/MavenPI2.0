/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.users;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author Administrator
 */
public class PbUserLayerResourceBundleMySql extends ListResourceBundle implements Serializable {

    //   @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"addUserFolder", "INSERT INTO PRG_USER_FOLDER(FOLDER_ID, FOLDER_NAME, FOLDER_DESC,FOLDER_CREATED_ON,FOLDER_UPDATED_ON) VALUES (PRG_USER_FOLDER_SEQ.NEXTVAL,'&','&',SYSDATE,SYSDATE)"}, {"addUserFolderDetails", "insert into PRG_USER_FOLDER_DETAIL (SUB_FOLDER_ID,FOLDER_ID, SUB_FOLDER_NAME, SUB_FOLDER_TYPE) "
            + " values('&',PRG_USER_FOLDER_SEQ.currval,'&','&' )"}, {"getBussDimInfoByGrpId", "SELECT DISTINCT gd.dim_id,gdm.member_name,gdt.dim_tab_id,gdt.tab_id,gd.dim_name  FROM prg_grp_dimensions gd,prg_grp_dim_tables gdt,"
            + " prg_grp_dim_member gdm WHERE gd.dim_id=gdt.dim_id   and gdm.dim_id= gd.dim_id and gdm.dim_tab_id= gdt.dim_tab_id AND gd.grp_id='&' order by gd.dim_id"}, {"getBussBucketInfoByGrpId", "SELECT BUSS_TABLE_ID, BUSS_TABLE_NAME FROM PRG_GRP_BUSS_TABLE where is_pure_query='Y' and buss_type='Query' and grp_id='&'"}, {"getBussFactsInfoByGrpId", "select buss_table_id, buss_table_name from prg_grp_buss_table where buss_table_id in ( SELECT BUSS_TABLE_ID "
            + "FROM PRG_GRP_BUSS_TABLE minus SELECT DISTINCT TAB_ID FROM PRG_GRP_DIM_TABLES ) and buss_type!='Query' and is_pure_query!='Y' and grp_id='&'"}, {"addUserSubFolderTables", "insert into PRG_USER_SUB_FOLDER_TABLES(SUB_FOLDER_TAB_ID,SUB_FOLDER_ID, BUSS_TABLE_ID, IS_DIMENSION, IS_FACT, IS_BUCKET, "
            + "DISP_NAME, DIM_ID,DIM_TAB_ID ,DIM_NAME) values ('&','&','&','&','&','&','&','&','&','&' )"}, {"addUserSubFolderElements", "INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS (ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME, USER_COL_NAME,"
            + " USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE) SELECT (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID,"
            + "& as SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id, a.column_disp_name, a.column_disp_name, a.column_disp_name, "
            + "a.column_type,& as SUB_FOLDER_TAB_ID,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),1 AS REF_ELEMENT_TYPE FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where    a.buss_table_id='&'"} /*
         * {"addUserSubFolderElements","INSERT INTO PRG_USER_SUB_FOLDER_ELEMENTS
         * (ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID,
         * BUSS_COL_NAME, USER_COL_NAME," + " USER_COL_DESC, USER_COL_TYPE,
         * SUB_FOLDER_TAB_ID,REF_ELEMENT_ID,REF_ELEMENT_TYPE) SELECT
         * (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID," + "& as
         * SUB_FOLDER_ID,a.BUSS_TABLE_ID, a.buss_column_id, a.column_disp_name,
         * a.column_disp_name, a.column_disp_name, " + "a.column_type,& as
         * SUB_FOLDER_TAB_ID,(PRG_USER_SUB_FLDR_ELEMENTS_SEQ.CURRVAL),b.ELEMENT_TYPE_ID
         * FROM PRG_GRP_BUSS_TABLE_DETAILS a" + ", prg_user_element_types B
         * where a.buss_table_id='&'"}
         *
         */, {"getElementsInfo", " SELECT & AS SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_disp_name as BUSS_COL_NAME,a.column_disp_name as USER_COL_NAME,"
            + "a.column_disp_name as USER_COL_DESC,a.column_type,& AS SUB_FOLDER_TAB_ID,a.default_aggregation, 1 REF_ELEMENT_TYPE FROM PRG_GRP_BUSS_TABLE_DETAILS a "
            + "where  a.buss_table_id='&' "} /*
         * ,{"getElementsInfo"," SELECT & AS
         * SUB_FOLDER_ID,a.BUSS_TABLE_ID,a.buss_column_id,a.column_disp_name,a.column_disp_name,"
         * + "a.column_disp_name,a.column_type,& AS
         * SUB_FOLDER_TAB_ID,a.default_aggregation,b.element_type_id " +
         * "REF_ELEMENT_TYPE FROM PRG_GRP_BUSS_TABLE_DETAILS a,
         * prg_user_element_types B where b.element_type_name = 'Current' and "
         * + "(a.column_type ='NUMBER' OR a.column_type ='VARCHAR2' ) and
         * a.buss_table_id='&' "}
         *
         */, {"addUserSubFolderElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,"
            + " USER_COL_NAME, USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE ) "
            + "values('&','&','&','&','&','&','&','&','&','&','&')"}, {"insertExtraElementsForFacts", "insert into PRG_USER_SUB_FOLDER_ELEMENTS( ELEMENT_ID, SUB_FOLDER_ID, BUSS_TABLE_ID, BUSS_COL_ID, BUSS_COL_NAME,USER_COL_NAME,"
            + "USER_COL_DESC, USER_COL_TYPE, SUB_FOLDER_TAB_ID, REF_ELEMENT_ID, REF_ELEMENT_TYPE ) SELECT  (PRG_USER_SUB_FLDR_ELEMENTS_SEQ.NEXTVAL) as ELEMENT_ID ,"
            + "a.SUB_FOLDER_ID ,a.BUSS_TABLE_ID ,a.BUSS_COL_ID,a.BUSS_COL_NAME,b.element_type_name || '_' || a.USER_COL_NAME ,a.USER_COL_DESC,a.USER_COL_TYPE ,a.SUB_FOLDER_TAB_ID,  "
            + "a.ELEMENT_ID REF_ELEMENT_ID,b.element_type_id REF_ELEMENT_TYPE FROM PRG_USER_SUB_FOLDER_ELEMENTS  A , prg_user_element_types B "
            + "where b.element_type_name != 'Current'and "
            + "(a.user_col_type ='NUMBER' OR a.user_col_type ='VARCHAR2') "
            + "and a.element_id in(&)"}, {"getSubFolderIdByFolderId", "select sub_folder_id from PRG_USER_FOLDER_DETAIL where folder_id='&'"} //for deleting user folder realted tables
        , {"deleteUserFolder", "DELETE FROM PRG_USER_FOLDER WHERE folder_id='&'"}, {"deleteUserFolderDetails", "DELETE FROM PRG_USER_FOLDER_DETAIL where folder_id='&'"}, {"deleteUserSubFolderTables", "DELETE FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id='&'"}, {"deleteUserSubFolderElements", "DELETE FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_id='&'"} //end of queries for deleting from user folder tables
        , {"getUserFolderList", "SELECT FOLDER_ID, FOLDER_NAME, FOLDER_DESC FROM PRG_USER_FOLDER order by FOLDER_CREATED_ON, folder_updated_by"}, {"getUserSubFolderList", "  select sub_folder_id, sub_folder_name, sub_folder_type from PRG_USER_FOLDER_DETAIL where folder_id='&'"}, {"getUserFolderDims", "SELECT DISTINCT DIM_ID, DIM_NAME FROM PRG_USER_SUB_FOLDER_TABLES where is_dimension='Y' and sub_folder_id='&' order by dim_name"}, {"getUserFolderDimMbrs", "SELECT usft.sub_folder_tab_id,gdm.member_id,gdm.member_name,usft.dim_tab_id  FROM PRG_USER_SUB_FOLDER_TABLES usft,PRG_GRP_DIM_MEMBER gdm "
            + "WHERE gdm.dim_id = usft.dim_id AND gdm.dim_tab_id = usft.dim_tab_id AND usft.sub_folder_id='&'AND usft.is_dimension ='Y'and "
            + "usft.dim_id='&' ORDER BY gdm.member_name"}, {"getUserFolderDimMbrsCols", "SELECT ELEMENT_ID, USER_COL_NAME FROM PRG_USER_SUB_FOLDER_ELEMENTS where sub_folder_tab_id='&'"}, {"getUserFolderFacts", "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_fact='Y' and sub_folder_id='&' order by disp_name "}, {"getUserFolderFactsColumns", "SELECT ELEMENT_ID,USER_COL_NAME  FROM PRG_USER_SUB_FOLDER_ELEMENTS where SUB_FOLDER_TAB_ID='&' order by ref_element_id, ref_element_type"}, {"getUserFolderBuckets", "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_bucket='Y' and sub_folder_id='&' order by disp_name "}, {"getUserFolderBuckets", "SELECT DISTINCT sub_folder_tab_id, disp_name FROM PRG_USER_SUB_FOLDER_TABLES where is_bucket='Y' and sub_folder_id='&' order by disp_name "} //modified by chiranjeevi on 26-12-09 start
        , {"getUserInsertDetailsindicus", "insert into PRG_AR_USERS(PU_LOGIN_ID,PU_FIRSTNAME,PU_MIDDLENAME,PU_LASTNAME,PU_PASSWORD,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,PU_CITY,PU_STATE,PU_COUNTRY,PU_PIN,PU_START_DATE,PU_END_DATE,ACCOUNT_TYPE,user_type,LAST_UPDATE_DATE)"
            + "values('&','&','&','&','&','&','&','&','&','&','&','&',now(),(select org_end_date from prg_org_master where org_id='&'),'&','&',now())"}, {"getUserInsertDetails", "insert into PRG_AR_USERS(PU_LOGIN_ID,PU_FIRSTNAME,PU_MIDDLENAME,PU_LASTNAME,PU_PASSWORD,PU_EMAIL,PU_CONTACTNO,PU_ADDRESS,PU_CITY,PU_STATE,PU_COUNTRY,PU_PIN,PU_START_DATE,PU_END_DATE,user_type,LAST_UPDATE_DATE,PU_ACTIVE)"
            + "values('&','&','&','&','&','&','&','&','&','&','&','&',now(),now(),'&',now(),'&')"} //,{"getUserFoldDimNames","SELECT DISTINCT DIM_ID,DIM_NAME FROM PRG_USER_SUB_FOLDER_TABLES where sub_folder_id='&' and is_dimension='Y' order by DIM_NAME"}
        , {"addUserPrivilages", "INSERT INTO PRG_AR_USER_PRIVELEGES(PUR_ID, USER_ID, PRIVELEGE_ID)  values(PRG_AR_USER_PRIVI_SEQ.nextval,&,'&')"}//for  privilages add for chiru
        , {"addReportPrevilages", "INSERT INTO PRG_AR_REPORT_PREVILAGES (prp_id, user_id, previlage_name) VALUES (PRG_AR_REPORT_PREVILAGES_SEQ.NEXTVAL,&,'&')"}, {"addReportGraphPrevilages", "insert into PRG_AR_REPGRP_PREVILAGES (PRGP_ID, USER_ID, PREVILAGE_NAME) values (PRG_AR_REPGRP_PREVILAGES_SEQ.NEXTVAL,&,'&')"}, {"addReportTablePrevilages", "insert into PRG_AR_REPTAB_PREVILAGES (PRTP_ID, USER_ID, PREVILAGE_NAME) values (PRG_AR_REPTAB_PREVILAGES_SEQ.NEXTVAL,&,'&')"}, {"checkRoleAssignment", "SELECT  BUSS_ROLE  FROM PRG_ORG_MASTER where ORG_ID in(&)"}, {"checkReportExistance", "SELECT REPORT_ID from  PRG_AR_REPORT_MASTER where report_id in(select  report_id  from account_report where ORG_ID in (&))"}, {"addReportstoUser", "INSERT INTO PRG_AR_USER_REPORTS(USER_ID, REPORT_ID, PUR_REPORT_SEQUENCE, PUR_FAV_REPORT, PUR_CUST_REPORT_NAME) values(&,&,&,'&','&')"}, {"addUserRole", "INSERT INTO  PRG_GRP_USER_FOLDER_ASSIGNMENT(USER_FOLDER_ID, USER_ID, GRP_ID, START_DATE, END_DATE) values(&,&,(select grp_id from prg_user_folder where folder_id=&),getdate(),getdate())"} //   ,{"sortUserlist","select pu.*, po.organisation_name, pt.user_type_name,case when sysdate-nvl(pu.pu_end_date,sysdate)<=0 then 'Active' else 'Expired' end as user_status from prg_ar_users pu, prg_org_master po,prg_user_type pt where pu.account_type= po.org_id(+) and pt.user_type_id(+)=pu.user_type order by & &"}
        , {"sortUserlist", " SELECT pu.* ,po.ORGANISATION_NAME,pt.USER_TYPE_NAME,CASE WHEN now()-COALESCE(pu.pu_end_date,now())<=0 THEN 'Active' ELSE 'Expired'END AS USER_STATUS FROM prg_ar_users pu LEFT OUTER JOIN prg_org_master po"
            + " ON(pu.account_type= po.org_id )LEFT OUTER JOIN prg_user_type pt ON (pt.user_type_id=pu.user_type)ORDER BY & &"}, {"useracclistSel", "select * from (select pom.org_id,pom.ORGANISATION_NAME,pom.ORGANISATION_DESC,pom.ORG_ST_DATE, nvl(pom.org_end_date,'') as enddate ,case when sysdate- nvl(pom.org_end_date,sysdate) <= 0 then 'Active' else 'Expired' end as  status from prg_org_master pom  order by org_st_date desc) order by & &"}, {"useTypelistSel", "select * from prg_user_type ORDER BY & &"}, {"getUsersbyUserTypeId", "select pu_id from prg_ar_users where user_type=& "}, {"deleteUserTypebyUserTypeId", "delete from prg_user_type where USER_TYPE_ID=& "}, {"insertUserPrivileges", "insert into prg_role_rows(USER_ID,company_id,max_count) values('&','&','&') "}, {"insertUserLevelPrivileges", "insert into PRG_AR_USER_PRIVELEGES(USER_ID,PRIVELEGE_ID) values(ident_current('PRG_AR_USERS'),'&')"}, {"userRepPRivQ", "insert into PRG_AR_REPORT_PREVILAGES(USER_ID,PREVILAGE_NAME) values(ident_current('PRG_AR_USERS'),'&')"}, {"tableRepPriv", "insert into PRG_AR_REPTAB_PREVILAGES(USER_ID,PREVILAGE_NAME) values(ident_current('PRG_AR_USERS'),'&')"}, {"regGraphPrev", "insert into prg_ar_repgrp_previlages(USER_ID,PREVILAGE_NAME) values(ident_current('PRG_AR_USERS'),'&')"}, {"insertQry", "insert into prg_ar_user_reports(USER_ID,REPORT_ID,PUR_FAV_REPORT) values(&,&,'N') "}, {"deleteQry", "delete from prg_ar_user_reports where user_id=& and report_id=&"}, {"getUserId", "select PU_LOGIN_ID from PRG_AR_USERS"}, {"getModuleNames", "select * from prg_previlage_master"}, {"getPrivilageData", "select * from PRG_PREVILAGE_DETAILS d where d.PREVILAGE_ID = '&'"} //,{"getPortalUserData","SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where portals_viewer='N'"}
        , {"getPortalUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where user_type NOT IN(9999)"}, {"getHeadlineUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where headlines='N'"}, {"getWhatifUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where WHAT_IF_ANALYSIS='N'"}, {"getUsersAssignedforportalviewer", "select distinct PA.user_id , PA.user_name from PRG_USER_ASSIGNMENTS PA,PRG_AR_USERS PU where PA.admin='N' and PA.portals_viewer='Y' and PU.PU_ACTIVE='Y' "}, {"updatePortalAssignment", "update PRG_USER_ASSIGNMENTS set portals_viewer='Y' where USER_NAME IN (&) "}, {"getActiveUsers", "select PU_LOGIN_ID from PRG_AR_USERS where PU_ACTIVE='Y' and user_type NOT IN(9999)"}, {"getInActiveUserIdsNnames", "select PU_ID , PU_LOGIN_ID FROM PRG_AR_USERS WHERE PU_ACTIVE='N' "}, {"getActiveUserIdsNnames", "select PU_ID , PU_LOGIN_ID FROM PRG_AR_USERS WHERE PU_ACTIVE='Y'"}, {"getInActiveUsers", "select PU_LOGIN_ID from PRG_AR_USERS where PU_ACTIVE='N'"}, {"getUsersAssignedforheadline", "select distinct user_id , user_name from PRG_USER_ASSIGNMENTS where admin='N' and HEADLINES='Y'"}, {"updateActiveUsers", "UPDATE PRG_AR_USERS SET PU_ACTIVE='Y' WHERE PU_LOGIN_ID IN (&)"}, {"updateInActiveUsers", "UPDATE PRG_AR_USERS SET PU_ACTIVE='N' WHERE PU_LOGIN_ID IN (&)"}, {"updateHeadlinesAssignment", "update PRG_USER_ASSIGNMENTS set HEADLINES='Y' where USER_NAME IN (&)"}, {"getUsersAssignedforwhatif", "select user_id , user_name from PRG_USER_ASSIGNMENTS where admin='N' and WHAT_IF_ANALYSIS='Y'"}, {"updatewhatifAssignment", "update PRG_USER_ASSIGNMENTS set WHAT_IF_ANALYSIS='Y' where USER_NAME IN (&)"}, {"getQueryStudioUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where user_type NOT IN(9999)"}, {"getPowerAnalyzerUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where user_type NOT IN(9999)"}, {"getOneViewAssignmentUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where user_type NOT IN(9999)"}, {"getScorecardsUserData", "SELECT DISTINCT USER_ID,USER_NAME from PRG_USER_ASSIGNMENTS where user_type NOT IN(9999)"}, {"getUsersAssignedforQueryStudio", "select distinct PA.user_id , PA.user_name from PRG_USER_ASSIGNMENTS PA,PRG_AR_USERS PU where PA.admin='N' and PA.QUERY_STUDIO='Y' and PU.PU_ACTIVE='Y' "}, {"getUsersAssignedforPowerAnalyzer", "select distinct PA.user_id , PA.user_name from PRG_USER_ASSIGNMENTS PA,PRG_AR_USERS PU where PA.admin='N' and PA.POWER_ANALYZER='Y' and PU.PU_ACTIVE='Y' "}, {"getUsersAssignedforOneView", "select distinct PA.user_id , PA.user_name from PRG_USER_ASSIGNMENTS PA,PRG_AR_USERS PU where PA.admin='N' and PA.ONE_VIEW='Y' and PU.PU_ACTIVE='Y' "}, {"getUsersAssignedforScorecards", "select distinct PA.user_id , PA.user_name from PRG_USER_ASSIGNMENTS PA,PRG_AR_USERS PU where PA.admin='N' and PA.SCORECARD_VIEWER='Y' and PU.PU_ACTIVE='Y' "}, {"updateQueryStudioAssignment", "update PRG_USER_ASSIGNMENTS set QUERY_STUDIO='Y' where USER_NAME IN (&) "}, {"updatePowerAnalyzerAssignment", "update PRG_USER_ASSIGNMENTS set POWER_ANALYZER='Y' where USER_NAME IN (&) "}, {"updateOneViewAssignment", "update PRG_USER_ASSIGNMENTS set ONE_VIEW='Y' where USER_NAME IN (&) "}, {"updateScorecardsAssignment", "update PRG_USER_ASSIGNMENTS set SCORECARD_VIEWER='Y' where USER_NAME IN (&) "}, {"resetPortalAssignment", "update PRG_USER_ASSIGNMENTS set PORTALS_VIEWER='N' where ADMIN='N'"}, {"resetQueryStudioAssignment", "update PRG_USER_ASSIGNMENTS set QUERY_STUDIO='N' where ADMIN='N'"}, {"resetPowerAnalyzerAssignment", "update PRG_USER_ASSIGNMENTS set POWER_ANALYZER='N' where ADMIN='N'"}, {"resetOneViewAssignment", "update PRG_USER_ASSIGNMENTS set ONE_VIEW='N' where ADMIN='N'"}, {"resetScorecardsAssignment", "update PRG_USER_ASSIGNMENTS set SCORECARD_VIEWER='N' where ADMIN='N'"}, {"getAllUsers", "SELECT * from prg_ar_users"}, {"getAllGroups", "select * from PRG_AR_LOGICGROUP_MASTER"}, {"getSuperAdminUserData", "select * from PRG_USER_ASSIGNMENTS where query_studio='Y'"}, {"updateSuperAdminAssignment", "update PRG_USER_ASSIGNMENTS set SUPER_ADMIN='Y' where USER_NAME IN (&) "}, {"resetSuperAdminAssignment", "update PRG_USER_ASSIGNMENTS set SUPER_ADMIN='N' where ADMIN='N'"}, {"getSuperAdmin", "select * from PRG_USER_ASSIGNMENTS where SUPER_ADMIN='Y'"}
    };
}
