/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.portal.viewer;

import java.io.Serializable;
import java.util.ListResourceBundle;

/**
 * @filename PortalViewerResourceBundle
 *
 * @author santhosh.kumar@progenbusiness.com @date Nov 17, 2009, 8:06:23 PM
 */
public class PortalViewerResourceBundle extends ListResourceBundle implements Serializable {

    protected Object[][] getContents() {
        return contents;
    }
    static final Object contents[][] = {
        {"displayKPIGraphRegion", "SELECT nvl(AGGREGATION_TYPE,'COUNT'),element_id, nvl(user_col_desc,user_col_name) col_name FROM PRG_USER_ALL_INFO_DETAILS where element_id=&"}, {"updatePortletName", "update PRG_USER_PORTLETS_MASTER set PORTLET_NAME='&', PORTLET_DESC='&' where PORTLET_ID='&' and PORTAL_ID='&'"}, {"updatePortletNameinDesiner", "update PRG_BASE_PORTLETS_MASTER set PORTLET_NAME='&', PORTLET_DESC='&' where PORTLET_ID='&'"} //added by santhosh.k on 06-03-2010 for deleting portals
        //,{"deletePortals","delete from PRG_PORTAL_MASTER where PORTAL_ID in (&) and USER_ID ='&'"}
        , {"deletePortals", "delete from PRG_PORTAL_TAB_MASTER where PORTAL_ID in (&) "}, {"deleteFromportlets", "delete from PRG_PORTLETS_MASTER where portl_id in (&) "}, {"setSortOrder", "update PRG_PORTLETS_MASTER set sortlevel='&' where portlet_id=& "}, {"extendTablePortlet", "update PRG_PORTAL_PORTLETS_ASSIGN set PORTLET_HEIGHT='&' where PORTLET_ID='&' and PORTL_ID='&'"}
    };
}
