/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.superadmin;

import java.util.ListResourceBundle;

/**
 *
 * @author progen
 */
public class SuperAdminSqlServerResBundle extends ListResourceBundle {

    @Override
    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"insertModules", "INSERT INTO PRG_USER_MODULE_ASSIGNMENTS(USER_ID,MODULE_ID,MODULE_CODE) VALUES(&,&,'&')"},
        {"insertComponents", "INSERT INTO PRG_USER_MOD_COMP_ASSIGNMENTS(MOD_ASSIGN_ID,COMPONENT_ID,COMPONENT_CODE,PARENT_COMP_CODE) VALUES(&,&,'&','&')"},
        {"nextQry", "SELECT IDENT_CURRENT['PRG_USER_MODULE_ASSIGNMENTS']"},
        {"getAssignedModulesQry", "SELECT MODULE_ASSIGN_ID,MODULE_CODE from PRG_USER_TYPE_PRIVILEGE where USER_ID=&"},
        {"getModulesQry", "SELECT MOD_ASSIGN_ID MODULE_ASSIGN_ID,MODULE_CODE from PRG_USER_MODULE_ASSIGNMENTS WHERE USER_TYPE_ID=&"},
        {"getComponentsQry", "SELECT COMPONENT_CODE,PARENT_COMP_CODE FROM PRG_USER_TYPE_PRIVILEGE WHERE MOD_ASSIGN_ID=& "},
        {"countQry", "SELECT COUNT(*) COUNT FROM PRG_USER_MODULE_ASSIGNMENTS WHERE MODULE_CODE='&'"},
        {"deleteModQuery", "DELETE FROM PRG_USER_MODULE_ASSIGNMENTS WHERE USER_ID=& "},
        {"deleteCompModQuery", "DELETE FROM PRG_USER_MOD_COMP_ASSIGNMENTS WHERE MOD_ASSIGN_ID IN(&)"},
        {"userTypeQry", "select USER_TYPE from PRG_AR_USERS where PU_ID=&"},
        {"insertUserTypeQry", "insert into PRG_USER_TYPE_PRIVILEGE(USER_ID,MODULE_CODE,MODULE_ASSIGN_ID) values(&,'&',&)"},
        {"selectMOdQry", "select MODULE_CODE from PRG_USER_MODULE_ASSIGNMENTS where USER_TYPE_ID=&"},
        // {"moduleAssignmentToUsers","INSERT INTO PRG_USER_ASSIGNMENTS (USER_ID,USER_NAME,ANALYZER,ADMIN,report_studio,Dashboard_Studio,HEADLINES,WHAT_IF_ANALYSIS,portals_designer,portals_viewer,scorecard_designer,SCORECARD_VIEWER,QUERY_STUDIO,html_reports,MY_REPORTS,USER_TYPE,USER_STATUS) VALUES (&,'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}
        //{"moduleAssignmentToUsers","INSERT INTO PRG_USER_ASSIGNMENTS (USER_ID,USER_NAME,ANALYZER,ADMIN,report_studio,Dashboard_Studio,HEADLINES,WHAT_IF_ANALYSIS,portals_designer,portals_viewer,scorecard_designer,SCORECARD_VIEWER,QUERY_STUDIO,html_reports,MY_REPORTS,USER_TYPE,USER_STATUS,POWER_ANALYZER,ONE_VIEW) VALUES (&,'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}
        {"moduleAssignmentToUsers", "INSERT INTO PRG_USER_ASSIGNMENTS (USER_ID,USER_NAME,ANALYZER,ADMIN,report_studio,Dashboard_Studio,HEADLINES,WHAT_IF_ANALYSIS,portals_designer,portals_viewer,scorecard_designer,SCORECARD_VIEWER,QUERY_STUDIO,html_reports,MY_REPORTS,USER_TYPE,USER_STATUS,POWER_ANALYZER,ONE_VIEW,SUPER_ADMIN,RESTRICTED_POWER_ANALYZER) VALUES (&,'&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&','&')"}
    };
}
