/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prg.util;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import prg.db.PbReturnObject;

/**
 * @Use This Class is only for setting the global Parameter into session as
 * hashmap
 *
 * @author Prabal Pratap Singh
 */
public class SetGBLParamIntoSession {

    public static int setGBLParamIntoSession(PbReturnObject pbroObj2, HttpSession session) {
        session.setAttribute("oneviewdatetype", pbroObj2.getFieldValueString(1, 1));
        session.setAttribute("LayoutVar", pbroObj2.getFieldValueString(1, 2));
        for (int i = 0; i < pbroObj2.getRowCount(); i++) {
            if (pbroObj2 != null && pbroObj2.getRowCount() > 0) {
                if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_LAYOUT")) {
                    session.setAttribute("LayoutVarMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_REPORTTAB")) {
                    session.setAttribute("ReportTabMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_REPORTTITLEALIGN")) {
                    session.setAttribute("ReportTitleAlignMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_PITHEME")) {
                    session.setAttribute("PiThemeMap", pbroObj2.getFieldValueString(i, 2));
                    session.setAttribute("theme", "Green");
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_CURRENCYSHOWNAS")) {
                    String[] elements = pbroObj2.getFieldValueString(i, 2).replace("{", "").replace("}", "").split(",");
                    if (elements != null && elements.length > 1) {
                        Map<String, String> currencyShownAs = new HashMap<String, String>();
                        for (int i1 = 0; i1 < elements.length; i1++) {
                            String[] keyValue = elements[i1].split("=");
                            currencyShownAs.put(keyValue[0], keyValue[1]);
                        }
                        session.setAttribute("CurrencyShownAsMap", currencyShownAs);
                    }
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_FROM_DATE_TO_DATE")) {
                    session.setAttribute("FromDateToDateMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_COMPARISON")) {
                    session.setAttribute("ComparisonMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_FILTERS")) {
                    session.setAttribute("FiltersMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_COLOR_0N_GRP")) {
                    session.setAttribute("ColorOnGrpMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_DATASHOW")) {
                    session.setAttribute("DataShowMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_REPORTTITLESIZE")) {
                    session.setAttribute("ReportTitleSizeMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_PI_VERSION")) {
                    session.setAttribute("PiVersionMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_IS_YEAR_CAL")) {
                    session.setAttribute("IsYearCalMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("SETUP_DEFAULT_PAGE_SIZE")) {
                    session.setAttribute("DefaultPageSizeMap", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("FAV_REP_AS_TAG")) {
                    session.setAttribute("isFavRepAsTag", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("RECENT_REP_AS_TAG")) {
                    session.setAttribute("isRecentRepAsTag", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("CUSTOM_REP_AS_TAG")) {
                    session.setAttribute("isCustomRepAsTag", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("FOOTER_OPTION")) {
                    session.setAttribute("isFooterShown", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("IS_COMP_LOGO")) {
                    session.setAttribute("isCompLogo", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("IS_MULTI_COMP_CAL")) {
                    session.setAttribute("isMultiCompCal", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("CUSTOM_SETTING")) {
                    session.setAttribute("customSetting", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("COMPANY_NAME")) {
                    session.setAttribute("companyName", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("REPORT_START_DATE")) {
                    session.setAttribute("reportStartDate", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DATE_FORMAT")) {
                    session.setAttribute("dateFormatGBL", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("IS_COMP_LOGO")) {
                    session.setAttribute("isCompLogo", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DEFAULT_LANGUAGE")) {
                    session.setAttribute("defaultLanguage", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("FOOTER_OPTION")) {
                    session.setAttribute("isFooterShown", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("IS_COMP_LOGO")) {
                    session.setAttribute("isCompLogo", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("RIGHT_LOGO")) {
                    session.setAttribute("rightLogo", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("FOOTER_TEXT_ENABLED")) {
                    session.setAttribute("footerTextEnabled", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("QUERY_CACHE_ENABLED")) {
                    session.setAttribute("queryCacheEnabled", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("QUERY_CACHE")) {
                    session.setAttribute("queryCache", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DATA_SET_STORAGE")) {
                    session.setAttribute("dataSetStorage", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("GEO_SPATIAL_ANALYSIS")) {
                    session.setAttribute("geoSpatialAnalysis", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DEBUG_ENABLED")) {
                    session.setAttribute("debugEnabled", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("ENABLE_REPORT_CACHE")) {
                    session.setAttribute("enableReportCache", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DUPLICATE_SEGMENTATION")) {
                    session.setAttribute("duplicateSegmentation", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("Reportproperties_param")) {
                    session.setAttribute("reportPropertyParam", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("MULTI_COMPANY_DISP")) {
                    session.setAttribute("multiCompanyDisplay", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("CUSTOM_TAG_SEQUENCE")) {
                    session.setAttribute("custaomTagSequenceLanding", pbroObj2.getFieldValueString(i, 2));
                } else if (pbroObj2.getFieldValueString(i, 0).equalsIgnoreCase("DATE_HEADER")) {
                    session.setAttribute("dateheader", pbroObj2.getFieldValueString(i, 2));
                }
            }
        }
        return 1;
    }
}
