/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.portlet;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PortletDesignerResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Nov 5, 2009, 2:03:16 PM
 */
public class PortletDesignerResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"getReportParamDetails", "select   element_id,disp_name,'' childElementId, dim_id, dim_tab_id, 'C' displayType,'' as relLevel,'All'  defaultValue,'N' isDisplay "
            + "from prg_user_all_info_details  where element_id in (&) order by decode (element_id,&)"}, {"insertPortletXML", "update PRG_PORTLETS_MASTER set xml_path=? , FOLDER_ID = ? where PORTLET_ID=?"}, {"insertBasePortletDetails", "insert into PRG_BASE_PORTLETS_MASTER(PORTLET_ID, PORTLET_NAME, PORTLET_DESC, XML_PATH, PORTLET_TYPE, PORTLET_REPORT_TYPE, "
            + "FOLDER_ID, CREATED_BY, CREATED_ON, UPDATE_BY, UPDATE_ON) values(&,'&','&','&','&','&','&',&, sysdate, &, sysdate)"}, {"updateXml1", "update PRG_BASE_PORTLETS_MASTER set XML_PATH='na' WHERE PORTLET_ID=&"}, {"getXmlClob", "select XML_PATH from PRG_BASE_PORTLETS_MASTER where PORTLET_ID=& for update"}, {"updateXml", "update PRG_BASE_PORTLETS_MASTER set XML_PATH=? WHERE PORTLET_ID=?"}
    };
}
