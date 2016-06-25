/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.search;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.progen.dashboardView.bd.PbDashboardViewerBD;
import com.progen.report.PbReportCollection;
import com.progen.report.bd.PbReportTableBD;
import com.progen.reportdesigner.db.ReportTemplateDAO;
import com.progen.reportview.bd.PbReportViewerBD;
import com.progen.search.suggest.AutoSuggestHelper;
import com.progen.search.suggest.SearchSuggestion;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import prg.db.Container;
import prg.db.PbDb;
import prg.db.PbReturnObject;
import utils.db.ProgenConnection;

/**
 *
 * @author arun
 */
public class SearchBd {

    public static Logger logger = Logger.getLogger(SearchBd.class);

    public Search isValidSearch(Iterable<String> searchKeys, int userId) {
        PbDb pbdb = new PbDb();
        Search userSearch = new Search("");
//      String checkRoleRepNameQry = "select  SEARCH_COL,case when table_type='Facts' then 1 when table_type='Dimensions-Name' then 2 when table_type='Dimensions' then 3  " +
//                " when table_type='TIMEDIM' then 4 else 5 end as RANKVAL ,  ELEMENT_ID, COL_TYPE, AGGREGATION_TYPE, MEMBER_NAME, TABLE_TYPE, FOLDER_ID, ACTUAL_VALUE " +
//                "from prg_user_search_info where search_col in("+searchKeys+") order by 2 ";


        ArrayList<String> noOperSearchKeyLst = new ArrayList<String>();
        ArrayList<SearchKey> searchKeyLst = this.makeSearchKeys(searchKeys);
        for (SearchKey key : searchKeyLst) {
            noOperSearchKeyLst.add("'" + key.getSearchKey() + "'");
        }
        String searchKeyForQry = Joiner.on(",").join(noOperSearchKeyLst);

        String checkRoleRepNameQry = "select SEARCH_COL,case when table_type='Facts' then 1 when table_type='Dimensions-Name' then 2 when table_type='Dimensions' then 3 "
                + " when table_type='TIMEDIM' then 4 else 5 end as RANKVAL , ELEMENT_ID, COL_TYPE, AGGREGATION_TYPE, MEMBER_NAME, TABLE_TYPE, FOLDER_ID"
                + ", case when ACTUAL_VALUE is null then SEARCH_COL when ACTUAL_VALUE ='' then SEARCH_COL else ACTUAL_VALUE end ACTUAL_VALUE "
                + "from prg_user_search_info where search_col in(" + searchKeyForQry + ") order by 2 ";
        try {
//            ProgenLog.log(ProgenLog.FINE, this, "isValidSearch","Query is "+checkRoleRepNameQry);
            logger.info("Query is " + checkRoleRepNameQry);
            PbReturnObject srchRows = pbdb.execSelectSQL(checkRoleRepNameQry);

            if (srchRows.getRowCount() == 0) {
                return userSearch;
            } else {
                String enteredText = Joiner.on(",").join(searchKeys);
                userSearch = this.initializeSearch(searchKeyLst, enteredText, srchRows);
                this.saveSearch(userSearch, userId);

            }

        } catch (SQLException ex) {
            logger.error("Exception", ex);
        }
        return userSearch;

    }

    private ArrayList<SearchKey> makeSearchKeys(Iterable<String> searchKeys) {
        SearchKey searchKey;
        ArrayList<SearchKey> searchKeyLst = new ArrayList<SearchKey>();
        for (String key : searchKeys) {
            searchKey = new SearchKey();
            Pattern pattern = Pattern.compile(SearchConstants.SearchOperatorConstants.getOperatorRegexForComparison());
            Matcher matcher = pattern.matcher(key);
            if (matcher.matches()) {
                String[] keys = key.split(SearchConstants.SearchOperatorConstants.getOperatorRegex());

                searchKey.setSearchKey(keys[0].trim());
                searchKey.setOperator(SearchConstants.SearchOperatorConstants.getOperatorEnum(matcher.group(1)));
                if (keys.length > 1) {
                    searchKey.setOperand(keys[1].trim());
                } else {
                    searchKey.setOperand(null);
                }
            } else {
                searchKey.setSearchKey(key);
            }
            searchKeyLst.add(searchKey);
        }
        return searchKeyLst;
    }

    private Search initializeSearch(Iterable<SearchKey> searchKeys, String enteredText, PbReturnObject srchRows) {
        Search userSearch = new Search(enteredText);
        int userFolderId = 0;


        int row = -1;
        //populate in the order of entry in UI
        for (SearchKey key : searchKeys) {
            String colName;
            row = -1;
            for (int s = 0; s < srchRows.getRowCount(); s++) {
                String type = srchRows.getFieldValueString(s, "RANKVAL");
                if (type.equalsIgnoreCase("2") || type.equalsIgnoreCase("1")) {
                    colName = srchRows.getFieldValueString(s, "ACTUAL_VALUE"); //for meas or dimension
                } else {
                    colName = srchRows.getFieldValueString(s, "SEARCH_COL"); //for dimension member
                }
                if (colName.equals(key.getSearchKey())) {
                    row = s;
                    break;
                }
            }
            if (row == -1) {
                continue;
            }
            String elementId = srchRows.getFieldValueString(row, "ELEMENT_ID");
            String colType = srchRows.getFieldValueString(row, "COL_TYPE");
            String origColType = colType;
            colName = srchRows.getFieldValueString(row, "MEMBER_NAME");
            userFolderId = srchRows.getFieldValueInt(row, "FOLDER_ID");
            String aggType;
            //add dimension column
            if (srchRows.getFieldValueString(row, "RANKVAL").equalsIgnoreCase("2")) {
                colName = srchRows.getFieldValueString(row, "ACTUAL_VALUE");
                userSearch.addDimensionSearchColumn(elementId, colType, colName);
                userSearch.setDimensionRowViewBy(elementId, true);
            } //add measure column
            else if (srchRows.getFieldValueString(row, "RANKVAL").equalsIgnoreCase("1")) {
                colName = srchRows.getFieldValueString(row, "ACTUAL_VALUE");
                aggType = srchRows.getFieldValueString(row, "AGGREGATION_TYPE");
                if (colType.equalsIgnoreCase("VARCHAR2")
                        || colType.equalsIgnoreCase("VARCHAR")) {
                    colType = "C";
                } else if (colType.equalsIgnoreCase("NUMBER")
                        || colType.equalsIgnoreCase("NUMERIC")
                        || colType.equalsIgnoreCase("FLOAT")
                        || colType.equalsIgnoreCase("DOUBLE")
                        || colType.equalsIgnoreCase("DECIMAL")
                        || colType.equalsIgnoreCase("BIGINT")
                        || colType.equalsIgnoreCase("INTEGER")
                        || colType.equalsIgnoreCase("INT")) {
                    colType = "N";
                } else if (colType.equalsIgnoreCase("CALCULATED")) {
                    colType = "N";
                } else if (colType.equalsIgnoreCase("SUMMARISED")) {
                    colType = "N";
                } else if (colType.equalsIgnoreCase("DATE")
                        || colType.equalsIgnoreCase("DATETIME")) {
                    colType = "D";
                } else {
                    colType = "C";
                }
                SearchMeasColumn measColumn = userSearch.addMeasureSearchColumn(elementId, colName, colType, aggType, origColType);
                //set Measure Filters if any
                Iterable<SearchKey> measKeys = Iterables.filter(searchKeys, SearchKey.getSearchKeyPredicate(colName));
                for (SearchKey measKey : measKeys) {
                    if (measKey.getOperator() != SearchConstants.SearchOperatorConstants.OPERATOR_NOT_INITIALIZED) {
                        measColumn.setFilterOperator(measKey.getOperator().getOperatorCode());
                        measColumn.setFilterOperand(measKey.getOperand());
                    }
                }
            } //add dimension Member column
            else if (srchRows.getFieldValueString(row, "RANKVAL").equalsIgnoreCase("3")) {
                userSearch.addDimensionSearchColumn(elementId, colType, colName);
                userSearch.setDimensionSearchValues(elementId, srchRows.getFieldValueString(row, "SEARCH_COL"));
                userSearch.setDimensionRowViewBy(elementId, true);
            } //add Time Dimension
            else if (srchRows.getFieldValueString(row, "RANKVAL").equalsIgnoreCase("4")) {
                userSearch.addTimeDimensionSearchColumn("Time", "Time");
                //userSearch.setTimeDimensionColumnDetails("Day", "PRG_YEAR_RANGE");//, "PRG_YEAR_RANGE");
                userSearch.setTimeDimensionSearchValues(srchRows.getFieldValueString(row, "ACTUAL_VALUE"), srchRows.getFieldValueString(row, "ACTUAL_VALUE"));

            }
        }
        this.initializeTimeForSearch(userSearch);
        userSearch.setFolderId(userFolderId);
        return userSearch;
    }

    private void initializeTimeForSearch(Search userSearch) {
        if (!userSearch.isTimeDimSet()) {
            //initialize Default Time Dimension
            userSearch.addTimeDimensionSearchColumn("Time", "Time");
            ArrayList<String> columnTypes = new ArrayList<String>();
            columnTypes.add("AS_OF_DATE");
            columnTypes.add("PRG_PERIOD_TYPE");
            userSearch.setTimeDimensionColumnDetails("Day", "PRG_STD", columnTypes);
        }
    }

    private PbReportCollection buildCollect(Search userSearch) {
        PbReportCollection collect = new PbReportCollection();

        collect.setRowViewBys(userSearch.getDimensionElementsRowViewBy());
        collect.setColumnViewBys(userSearch.getDimensionElementsColumnViewBy());

        for (SearchColumn column : userSearch.getRowViewByDimensions()) {

            collect.setParameters(column.elementId, column.colName, column.srchValues);
        }

//repeat for Col View Bys
//        for ( SearchColumn column : userSearch.getViewByDimensions() )
//        {
//            collect.setParameters(column.elementId, column.colName, column.srchValues);
//        }

        for (SearchColumn column : userSearch.getTimeDimension()) {
            SearchTimeColumn timeColumn = (SearchTimeColumn) column;
            collect.setTimeDetails(timeColumn.timeLevel, timeColumn.timeType, timeColumn.timeColumnTypes, timeColumn.srchValues);
        }

        for (SearchColumn column : userSearch.getMeasures()) {
            SearchMeasColumn measColumn = (SearchMeasColumn) column;
            collect.setReportQueryColumns(measColumn.elementId, measColumn.colName, measColumn.aggType, measColumn.colType, measColumn.originalColType);
        }

        collect.initializeReportTableCols();
        collect.setReportBizRole(userSearch.getFolderId());

        return collect;
    }

    public Container prepareReportContainer(Search userSearch, String reportId, String userId) {
        PbReportCollection collect = this.buildCollect(userSearch);
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerBD viewerBd = new PbReportViewerBD();

        Container container = viewerBd.prepareReport(collect, reportId, userId);


        //sort by first column in ProgenTable
        if (!userSearch.isDimensionOnlySearch()) {
            container.setSortColumn(container.getDisplayColumns().get(0), "0");
        }

        //apply any Measure Filters
        for (SearchColumn column : userSearch.getMeasures()) {
            if (((SearchMeasColumn) column).isFilterApplicableForMeasure()) {
                container.setSearchColumn("A_" + column.getElementId(), ((SearchMeasColumn) column).getFilterOperator(), ((SearchMeasColumn) column).getFilterOperand().toString(), null);
                PbReportTableBD reportTableBD = new PbReportTableBD();
                reportTableBD.searchDataSet(container);
            }
        }

        return container;
    }

    public String createReport(Container container, String userId, String reportName, String reportDesc) {
        ReportTemplateDAO reportTemplateDAO = new ReportTemplateDAO();
        PbReportViewerBD viewerBd = new PbReportViewerBD();
        int srchReportId = -1;
        if (ProgenConnection.getInstance().getDatabaseType().equals(ProgenConnection.ORACLE)) {
            srchReportId = reportTemplateDAO.getSequence("PRG_AR_REPORT_MASTER_SEQ");
        }

        String generatedReportId = "ERROR";
        try {
            generatedReportId = viewerBd.saveReport(container, reportName, reportDesc, srchReportId, null, userId, null, null);
            String[] userIds = {userId};
            reportTemplateDAO.assignReportToUsers(generatedReportId, userIds);
        } catch (Exception ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "createReport", "Create Report from Search Failed");
            logger.error("Create Report from Search Failed", ex);
        }

        return generatedReportId;
    }

    public String processKpi(Container container, String timeLevel, String userId) {
        String kpiHtml = "";
        PbDashboardViewerBD dbViewerBd = new PbDashboardViewerBD();
        PbReturnObject kpis = dbViewerBd.processSearchKpi(container, timeLevel, userId);
        ArrayList<String> displayLabels = container.getDisplayLabels();
        int viewCount = container.getViewByCount();
        int rowCount = 0;
        BigDecimal currentValue, priorValue, changeValue;
        String cssClass;

        if ("MONTH".equals(timeLevel)) {
            timeLevel = "Month";
        } else if ("QUARTER".equals(timeLevel)) {
            timeLevel = "Quarter";
        }
        int colIndex = 0;

        if (kpis.getRowCount() > 0) {
            StringBuilder kpi = new StringBuilder();
            for (int i = viewCount; i < displayLabels.size(); i++) {
                kpi.append("<table width='100%'>");
                kpi.append("<th><tr>").append("<td colSpan=2 class='subTotalCell'>");
                kpi.append(displayLabels.get(i)).append("</tr></th>");


                currentValue = kpis.getFieldValueBigDecimal(rowCount, 1 + colIndex);
                priorValue = kpis.getFieldValueBigDecimal(rowCount, 2 + colIndex);
                changeValue = kpis.getFieldValueBigDecimal(rowCount, 4 + colIndex);

                if (currentValue == null || priorValue == null) {
                    cssClass = "measureNumericCell";
                } else if (currentValue.compareTo(priorValue) > 0) {
                    cssClass = "measureNumericCellPositive";
                } else if (currentValue.compareTo(priorValue) == 0) {
                    cssClass = "measureNumericCellNegative";
                } else {
                    cssClass = "measureNumericCell";
                }

                kpi.append("<tr>").append("<td class='dimensionCell'>").append("Current ").append(timeLevel).append("</td>");
                kpi.append("<td class='").append(cssClass).append("'>").append(kpis.getModifiedNumber(currentValue, "", -1)).append("&nbsp;&nbsp;&nbsp").append("</td>");
                kpi.append("</tr>");


                kpi.append("<tr>").append("<td class='dimensionCell'>").append("Prior ").append(timeLevel).append("</td>");
                kpi.append("<td class='measureNumericCell'>").append(kpis.getModifiedNumber(priorValue, "", -1)).append("&nbsp;&nbsp;&nbsp").append("</td>");
                kpi.append("</tr>");


                kpi.append("<tr>").append("<td class='dimensionCell'>").append("Change % ").append("</td>");
                kpi.append("<td class='measureNumericCell'>").append(kpis.getModifiedNumber(changeValue, "%", -1)).append("&nbsp;&nbsp;&nbsp").append("</td>");
                kpi.append("</tr>");
                kpi.append("</table>");
                //rowCount++;
                colIndex = colIndex + 4;
            }
            kpiHtml = kpi.toString();

        }
        return kpiHtml;
    }

    public PbReturnObject getRelatedTrendMeasureData(Container container, String userId) {

        PbReportViewerBD viewerBd = new PbReportViewerBD();
        PbReturnObject retObj = viewerBd.fireTrendQuery(container, userId, false);
        return retObj;
    }

    public String buildRelatedTrendMesuresUI(Container container, PbReturnObject trendMeasData) {
        String relatedTrendMeasures = null;
        StringBuilder trendHtml = new StringBuilder();
        try {


            int noOfMonths = trendMeasData.getRowCount();
            int viewCount = (container.getViewByCount());

            ArrayList<String> displayCols = container.getDisplayColumns();
            ArrayList<String> displayLabels = container.getDisplayLabels();

            BigDecimal total = BigDecimal.ZERO;
            BigDecimal avg = BigDecimal.ZERO;
            BigDecimal max = BigDecimal.ZERO;
            BigDecimal min = BigDecimal.ZERO;
            BigDecimal currentMonthValue = BigDecimal.ZERO;
            if (noOfMonths > 0) {
                trendHtml.append("<table width=\"100%\">");
                int index = viewCount;
                for (index = viewCount; index < displayCols.size(); index++) {
                    trendHtml.append("<tr>");
                    trendHtml.append("<td colSpan=2 class='subTotalCell'><strong>");
                    trendHtml.append(displayLabels.get(index));
                    trendHtml.append("<strong></td>");
                    trendHtml.append("</tr>");
                    int monthCount = 0;
                    total = BigDecimal.ZERO;
                    avg = BigDecimal.ZERO;

                    for (int i = noOfMonths - 1; i >= 0; i--) {
                        monthCount++;
                        currentMonthValue = trendMeasData.getFieldValueBigDecimal(i, displayCols.get(index));

                        if (i == noOfMonths - 1) {
                            trendHtml.append("<tr>");
                            trendHtml.append("<td class='dimensionCell'>Last Month</td>");
                            trendHtml.append("<td class='measureNumericCell'>").append(trendMeasData.getModifiedNumber(currentMonthValue, "", -1)).append("</td>");
                            trendHtml.append("</tr>");
                            max = currentMonthValue;
                            min = currentMonthValue;
                        }

                        total = total.add(currentMonthValue);
                        max = max.max(currentMonthValue);
                        min = min.min(currentMonthValue);

                        if (monthCount == 3) {
                            trendHtml.append("<tr>");
                            trendHtml.append("<td class='dimensionCell'>Avg Last 3 Months</td>");
                            avg = total.divide(new BigDecimal(monthCount), MathContext.DECIMAL64);
                            trendHtml.append("<td class='measureNumericCell'>").append(trendMeasData.getModifiedNumber(avg, "", -1)).append("</td>");
                            trendHtml.append("</tr>");
                        }

                    }

                    trendHtml.append("<tr>");
                    trendHtml.append("<td class='dimensionCell'>Avg Last ").append(monthCount).append(" Months</td>");
                    avg = total.divide(new BigDecimal(monthCount), MathContext.DECIMAL64);
                    trendHtml.append("<td class='measureNumericCell'>").append(trendMeasData.getModifiedNumber(avg, "", -1)).append("</td>");
                    trendHtml.append("</tr>");

                    trendHtml.append("<tr>");
                    trendHtml.append("<td class='dimensionCell'>Max Last ").append(monthCount).append(" Months</td>");
                    trendHtml.append("<td class='measureNumericCell'>").append(trendMeasData.getModifiedNumber(max, "", -1)).append("</td>");
                    trendHtml.append("</tr>");

                    trendHtml.append("<tr>");
                    trendHtml.append("<td class='dimensionCell'>Min Last ").append(monthCount).append(" Months</td>");
                    trendHtml.append("<td class='measureNumericCell'>").append(trendMeasData.getModifiedNumber(min, "", -1)).append("</td>");
                    trendHtml.append("</tr>");
                }
                trendHtml.append("</table>");
                relatedTrendMeasures = trendHtml.toString();
            }
        } catch (Exception ex) {
            logger.error("Exception", ex);
            relatedTrendMeasures = "";
        }
        return relatedTrendMeasures;
    }

    public Iterable<SearchSuggestion> giveSuggestions(String searchKey) {

        String qryStringformatted = searchKey.toUpperCase() + "%";
        String suggestQry = "select SEARCH_COL,TABLE_TYPE from prg_user_search_info where  UPPER(SEARCH_COL) LIKE '&' order by 1";

        PbDb pbDb = new PbDb();
        Object[] bind = new Object[1];
        bind[0] = qryStringformatted;

        SearchSuggestion suggestion;
        ArrayList<SearchSuggestion> srchSuggestionLst = new ArrayList<SearchSuggestion>();
        String finalQuery = pbDb.buildQuery(suggestQry, bind);
        try {
            PbReturnObject suggRetObj = pbDb.execSelectSQL(finalQuery);
            for (int i = 0; i < suggRetObj.getRowCount(); i++) {
                suggestion = new SearchSuggestion();
                suggestion.setSuggestion(suggRetObj.getFieldValueString(i, 0));
                suggestion.setType(suggRetObj.getFieldValueString(i, 1));
                srchSuggestionLst.add(suggestion);
            }
        } catch (SQLException ex) {
//            ProgenLog.log(ProgenLog.SEVERE, this, "giveSuggestions", "Give Suggestion Exception "+ex.getMessage());
            logger.error("Give Suggestion Exception", ex);
        }
        return srchSuggestionLst;
    }

    public void saveSearch(Search userSearch, int userId) {
        SearchDAO searchDAO = new SearchDAO();
        int searchId = searchDAO.saveSearchValues(userSearch, userId);
        userSearch.setSearchId(searchId);

    }

    public SearchConstants saveAsFavoriteSearch(Search userSearch, String searchName, int userId) {
        SearchDAO searchDAO = new SearchDAO();

        if (searchDAO.checkDuplicate(searchName)) {
            return SearchConstants.FAVORITE_ALREADY_EXISTS;
        } else {
            userSearch.setSearchName(searchName);
            if (searchDAO.saveAsFavoriteSearch(userSearch, userId)) {
                return SearchConstants.FAVORITE_SAVED_SUCCESSFULLY;
            } else {
                return SearchConstants.FAVORITE_SAVED_FAILED;
            }
        }
    }

    public String formulateAutoSuggestJson(String qry) {
        AutoSuggestHelper helper = new AutoSuggestHelper(qry);

        if (helper.isAutoSuggestQryNeeded()) {
            Iterable<SearchSuggestion> suggestionLst = this.giveSuggestions(qry);
            helper.setSuggestionList(suggestionLst);
        }
        return helper.buildAutoSuggestJson();
    }

    public HashMap getFavSearchData() {
        SearchDAO searchDAO = new SearchDAO();
        HashMap<String, String> favSearch = searchDAO.getSavedFavSearchDetails();
        return favSearch;
    }

    public void deleteFavSearch(String searchName) {
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.deleteUserFavSearch(searchName);

    }
}
