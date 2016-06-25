/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.template.action;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 *
 * @author mayank
 */
public class ManagementResourceBundleOracle extends ListResourceBundle implements Serializable{

    @Override
    protected Object[][] getContents() {
       return CONTENTS;
    }
    static final Object[][] CONTENTS ={
     {"getTemplateId","select max(TEMPLATE_ID) from PRG_TEMPLATE_DETAILS"},
        {"getRoleId","select uf.FOLDER_ID, uf.FOLDER_NAME from prg_ar_report_master rm ,prg_ar_report_details rd ,prg_user_folder uf where rm.report_id=rd.report_id and uf.folder_id=rd.folder_id and rm.report_id =&"},
         {"getAOName"," select AO_ID from prg_ar_report_details where report_id= &"}
               };
    
}
