/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.report.query;

import static com.progen.report.query.QueryTimeConstants.*;
import java.util.ArrayList;
import java.util.HashMap;
import utils.db.ProgenParam;

/**
 *
 * @author arun
 */
public class QueryTimeHelper {

    public ArrayList<String> buildTimeDetails(String basis, String timeLevel) {
        ArrayList<String> timeDetails = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();

        if (basis.equals(TIME_PERIOD_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_STD");
            timeDetails.add(pParam.getdateforpage().toString());
            timeDetails.add("Month");
            timeDetails.add("Last Period");
        } else if (basis.equals(TIME_RANGE_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_DATE_RANGE");
            //timeDetails.add(sdf.format(date));
            //timeDetails.add("05/31/2008");
            timeDetails.add(String.valueOf(pParam.getdateforpage(30)));//added on 28-11-09
            timeDetails.add(String.valueOf(pParam.getdateforpage()));
            timeDetails.add(String.valueOf(pParam.getdateforpage(60)));
            timeDetails.add(String.valueOf(pParam.getdateforpage(31)));
        } else if (basis.equals(TIME_ROLLING_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_DAY_ROLLING");
            timeDetails.add(pParam.getdateforpage().toString());//added on 28-11-09
            timeDetails.add("Last 30 Days");
        } else if (basis.equals(TIME_MONTH_RANGE_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_MONTH_RANGE");
            //timeDetails.add(sdf.format(date));
            //timeDetails.add("05/31/2008");
            timeDetails.add(pParam.getmonthforpage().toString());//added on 28-11-09
            timeDetails.add(null);
            timeDetails.add(null);
            timeDetails.add(null);
        } else if (basis.equals(TIME_QUARTER_RANGE_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_QUARTER_RANGE");
            //timeDetails.add(sdf.format(date));
            //timeDetails.add("05/31/2008");
            timeDetails.add("Dquarter");//added on 28-11-09
            timeDetails.add(null);
            timeDetails.add(null);
            timeDetails.add(null);
        } else if (basis.equals(TIME_YEAR_RANGE_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_YEAR_RANGE");
            //timeDetails.add(sdf.format(date));
            //timeDetails.add("05/31/2008");
            timeDetails.add(pParam.getYearforpage().toString());//added on 28-11-09
            timeDetails.add(null);
            timeDetails.add(null);
            timeDetails.add(null);
        } else if (basis.equals(TIME_COHORT_BASIS) && timeLevel.equals(TIME_DAY_LEVEL)) {
            timeDetails.add("Day");
            timeDetails.add("PRG_COHORT");
            timeDetails.add(pParam.getdateforpage(30));//added on 28-11-09
            timeDetails.add(pParam.getdateforpage());
            timeDetails.add(pParam.getdateforpage(30));
            timeDetails.add(pParam.getdateforpage(-365));
            timeDetails.add("Month");
        } else if (timeLevel.equals(TIME_WEEK_LEVEL)) {
            timeDetails.add("WEEK");
            timeDetails.add("PRG_WEEK_CMP");
            timeDetails.add(null);
            timeDetails.add(null);
        } else if (timeLevel.equals(TIME_MONTH_LEVEL)) {
            timeDetails.add("Month");
            timeDetails.add("PRG_STD");
            timeDetails.add(null);
            timeDetails.add("Month");
            timeDetails.add("Last Period");
        } else if (timeLevel.equals(TIME_QUARTER_LEVEL)) {
            timeDetails.add("QUARTER");
            timeDetails.add("PRG_QUARTER_CMP");
            timeDetails.add(null);
            timeDetails.add(null);
        } else if (timeLevel.equals(TIME_YEAR_LEVEL)) {
            timeDetails.add("YEAR");
            timeDetails.add("PRG_YEAR_CMP");
            timeDetails.add(null);
            timeDetails.add(null);
        }
        return timeDetails;
    }

    public static String getTimeDetails(String type) {

        if (type.equalsIgnoreCase("PRG_STD")) {
            type = "Standard";
        } else if (type.equalsIgnoreCase("PRG_DATE_RANGE")) {
            type = "Date Range";
        } else if (type.equalsIgnoreCase("PRG_MONTH_RANGE")) {
            type = "Rolling";
        } else if (type.equalsIgnoreCase("PRG_QUARTER_RANGE")) {
            type = "Quarter Range";
        } else if (type.equalsIgnoreCase("PRG_YEAR_RANGE")) {
            type = "Year Range";
        } else if (type.equalsIgnoreCase("PRG_DAY_ROLLING")) {
            type = "Day Rolling";
        } else if (type.equalsIgnoreCase("PRG_WEEK_CMP")) {
            type = "Week Compare";
        } else if (type.equalsIgnoreCase("PRG_QUARTER_CMP")) {
            type = "Quarter Compare";
        } else if (type.equalsIgnoreCase("PRG_YEAR_CMP")) {
            type = "Year Compare";
        }
        return type;
    }

    public HashMap<String, ArrayList<String>> buildTimeMap(String basis, String timeLevel, Iterable<String> timeColTypes) {
        if (timeLevel.equals(TIME_DAY_LEVEL)) {
            return this.buildTimeMapForDayLevel(basis, timeColTypes);
        } else if (timeLevel.equals(TIME_WEEK_LEVEL)) {
            return this.buildTimeMapForWeekLevel(basis, timeColTypes);
        } else if (timeLevel.equals(TIME_MONTH_LEVEL)) {
            return this.buildTimeMapForMonthLevel(basis, timeColTypes);
        } else if (timeLevel.equals(TIME_QUARTER_LEVEL)) {
            return this.buildTimeMapForQuarterLevel(basis, timeColTypes);
        } else if (timeLevel.equals(TIME_YEAR_LEVEL)) {
            return this.buildTimeMapForYearLevel(basis, timeColTypes);
        } else {
            return null;
        }

    }

    private HashMap<String, ArrayList<String>> buildTimeMapForDayLevel(String basis, Iterable<String> timeColTypes) {
        HashMap<String, ArrayList<String>> timeMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeInfo = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();

        if (basis.equalsIgnoreCase(TIME_PERIOD_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                timeInfo.add(pParam.getdateforpage().toString());
                timeInfo.add("CBO_" + columnType);
                timeInfo.add("DATE");
                timeInfo.add("1");
                timeInfo.add("1");
                if (columnType.equals("AS_OF_DATE")) {
                    timeInfo.add(null);
                } else if (columnType.equals("PRG_PERIOD_TYPE")) {
                    timeInfo.add("MONTH");
                } else if (columnType.equals("PRG_COMPARE")) {
                    timeInfo.add("LAST PERIOD");
                } else {
                    timeInfo.add(null);
                }
                timeInfo.add(columnType);
                timeMap.put(columnType, timeInfo);
            }
        } else if (basis.equalsIgnoreCase(TIME_RANGE_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                if (columnType.equals("AS_OF_DATE1")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(30)));
                    timeInfo.add("CBO_AS_OF_DATE1");
                    timeInfo.add("From DATE");
                    timeInfo.add("1");
                    timeInfo.add("1");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DATE1");
                    timeMap.put("AS_OF_DATE1", timeInfo);
                } else if (columnType.equals("AS_OF_DATE2")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage()));
                    timeInfo.add("CBO_AS_OF_DATE2");
                    timeInfo.add("To DATE");
                    timeInfo.add("2");
                    timeInfo.add("2");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DATE2");
                    timeMap.put("AS_OF_DATE2", timeInfo);
                } else if (columnType.equals("CMP_AS_OF_DATE1")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(60)));
                    timeInfo.add("CBO_CMP_AS_OF_DATE1");
                    timeInfo.add("Comp From DATE");
                    timeInfo.add("3");
                    timeInfo.add("3");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DATE1");
                    timeMap.put("CMP_AS_OF_DATE1", timeInfo);
                } else if (columnType.equals("CMP_AS_OF_DATE2")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(31)));
                    timeInfo.add("CBO_CMP_AS_OF_DATE2");
                    timeInfo.add("Comp To DATE");
                    timeInfo.add("4");
                    timeInfo.add("4");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DATE2");
                    timeMap.put("CMP_AS_OF_DATE2", timeInfo);
                }
            }
        } else if (basis.equalsIgnoreCase(TIME_ROLLING_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                if (columnType.equals("AS_OF_DATE")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(30)));
                    timeInfo.add("CBO_AS_OF_DATE");
                    timeInfo.add("DATE");
                    timeInfo.add("1");
                    timeInfo.add("1");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DATE");
                    timeMap.put("AS_OF_DATE", timeInfo);
                } else if (columnType.equals("PRG_DAY_ROLLING")) {
                    timeInfo.add("Last 30 Days");//added on 28-11-09
                    timeInfo.add("CBO_PRG_DAY_ROLLING");
                    timeInfo.add("Rolling Period");
                    timeInfo.add("2");
                    timeInfo.add("2");
                    timeInfo.add("Last 30 Days");
                    timeInfo.add("PRG_DAY_ROLLING");
                    timeMap.put("PRG_DAY_ROLLING", timeInfo);
                }
            }
        } else if (basis.equalsIgnoreCase(TIME_MONTH_RANGE_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                if (columnType.equalsIgnoreCase("AS_OF_DMONTH1")) {
                    timeInfo.add(pParam.getdateforpage().toString());
                    timeInfo.add("CBO_AS_OF_DMONTH1");
                    timeInfo.add("From MONTH");
                    timeInfo.add("1");
                    timeInfo.add("1");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DMONTH1");
                    timeMap.put("AS_OF_DMONTH1", timeInfo);
                } else if (columnType.equalsIgnoreCase("AS_OF_DMONTH2")) {
                    timeInfo.add(pParam.getdateforpage(30).toString());
                    timeInfo.add("CBO_AS_OF_DMONTH2");
                    timeInfo.add("To MONTH");
                    timeInfo.add("2");
                    timeInfo.add("2");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DMONTH2");
                    timeMap.put("AS_OF_DMONTH2", timeInfo);
                } else if (columnType.equalsIgnoreCase("CMP_AS_OF_DMONTH1")) {
                    timeInfo.add(pParam.getdateforpage(31).toString());
                    timeInfo.add("CBO_CMP_AS_OF_DMONTH1");
                    timeInfo.add("Comp From MONTH");
                    timeInfo.add("3");
                    timeInfo.add("3");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DMONTH1");
                    timeMap.put("CMP_AS_OF_DMONTH1", timeInfo);
                } else if (columnType.equalsIgnoreCase("CMP_AS_OF_DM0NTH2")) {
                    timeInfo.add(pParam.getdateforpage(60).toString());
                    timeInfo.add("CBO_CMP_AS_OF_DMONTH2");
                    timeInfo.add("Comp To MONTH");
                    timeInfo.add("4");
                    timeInfo.add("4");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DM0NTH2");
                    timeMap.put("CMP_AS_OF_DMONTH2", timeInfo);
                }
            }
        } else if (basis.equalsIgnoreCase(TIME_QUARTER_RANGE_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                if (columnType.equalsIgnoreCase("AS_OF_DQUARTER1")) {
                    timeInfo.add(pParam.getdateforpage().toString());
                    timeInfo.add("CBO_AS_OF_DQUARTER1");
                    timeInfo.add("From QUARTER");
                    timeInfo.add("1");
                    timeInfo.add("1");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DQUARTER1");
                    timeMap.put("AS_OF_DQUARTER1", timeInfo);
                } else if (columnType.equalsIgnoreCase("AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getdateforpage(30).toString());
                    timeInfo.add("CBO_AS_OF_DQUARTER2");
                    timeInfo.add("To QUARTER");
                    timeInfo.add("2");
                    timeInfo.add("2");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DQUARTER2");
                    timeMap.put("AS_OF_DQUARTER2", timeInfo);
                } else if (columnType.equalsIgnoreCase("CMP_AS_OF_DQUARTER1")) {
                    timeInfo.add(pParam.getdateforpage(31).toString());
                    timeInfo.add("CBO_CMP_AS_OF_DQUARTER1");
                    timeInfo.add("Comp From QUARTER");
                    timeInfo.add("3");
                    timeInfo.add("3");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DQUARTER1");
                    timeMap.put("CMP_AS_OF_DQUARTER1", timeInfo);
                } else if (columnType.equalsIgnoreCase("CMP_AS_OF_DQUARTER2")) {
                    timeInfo.add(pParam.getdateforpage(60).toString());
                    timeInfo.add("CBO_CMP_AS_OF_DQUARTER2");
                    timeInfo.add("Comp To QUARTER");
                    timeInfo.add("4");
                    timeInfo.add("4");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DQUARTER2");
                    timeMap.put("CMP_AS_OF_DQUARTER2", timeInfo);
                }
            }
        } else if (basis.equalsIgnoreCase(TIME_COHORT_BASIS)) {
            for (String columnType : timeColTypes) {
                timeInfo = new ArrayList<String>();
                if (columnType.equals("AS_OF_DATE1")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(30)));
                    timeInfo.add("CBO_AS_OF_DATE1");
                    timeInfo.add("From DATE");
                    timeInfo.add("1");
                    timeInfo.add("1");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DATE1");
                    timeMap.put("AS_OF_DATE1", timeInfo);
                } else if (columnType.equals("AS_OF_DATE2")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage()));
                    timeInfo.add("CBO_AS_OF_DATE2");
                    timeInfo.add("To DATE");
                    timeInfo.add("2");
                    timeInfo.add("2");
                    timeInfo.add(null);
                    timeInfo.add("AS_OF_DATE2");
                    timeMap.put("AS_OF_DATE2", timeInfo);
                } else if (columnType.equals("CMP_AS_OF_DATE1")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(30)));
                    timeInfo.add("CBO_CMP_AS_OF_DATE1");
                    timeInfo.add("Future From DATE");
                    timeInfo.add("3");
                    timeInfo.add("3");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DATE1");
                    timeMap.put("CMP_AS_OF_DATE1", timeInfo);
                } else if (columnType.equals("CMP_AS_OF_DATE2")) {
                    timeInfo.add(String.valueOf(pParam.getdateforpage(-365)));
                    timeInfo.add("CBO_CMP_AS_OF_DATE2");
                    timeInfo.add("Future To DATE");
                    timeInfo.add("4");
                    timeInfo.add("4");
                    timeInfo.add(null);
                    timeInfo.add("CMP_AS_OF_DATE2");
                    timeMap.put("CMP_AS_OF_DATE2", timeInfo);
                } else if (columnType.equals("PRG_PERIOD_TYPE")) {
                    timeInfo.add(pParam.getdateforpage().toString());
                    timeInfo.add("CBO_" + columnType);
                    timeInfo.add("DATE");
                    timeInfo.add("5");
                    timeInfo.add("5");
                    timeInfo.add("MONTH");
                    timeInfo.add("PRG_PERIOD_TYPE");
                    timeMap.put("PRG_PERIOD_TYPE", timeInfo);
                }
            }
        }


        return timeMap;

    }

    private HashMap<String, ArrayList<String>> buildTimeMapForWeekLevel(String basis, Iterable<String> timeColTypes) {
        HashMap<String, ArrayList<String>> timeMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeInfo = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();

        for (String columnType : timeColTypes) {
            timeInfo = new ArrayList<String>();
            if (columnType.equalsIgnoreCase("AS_OF_WEEK")) {
                timeInfo.add(null);
                timeInfo.add("CBO_AS_OF_WEEK");
                timeInfo.add("WEEK");
                timeInfo.add("1");
                timeInfo.add("1");
                timeInfo.add(null);
                timeInfo.add("AS_OF_WEEK");
                timeMap.put("AS_OF_WEEK", timeInfo);
            } else if (columnType.equalsIgnoreCase("AS_OF_WEEK1")) {
                timeInfo.add(null);
                timeInfo.add("CBO_AS_OF_WEEK1");
                timeInfo.add("COMPAREWEEK");
                timeInfo.add("2");
                timeInfo.add("2");
                timeInfo.add(null);
                timeInfo.add("AS_OF_WEEK1");
                timeMap.put("AS_OF_WEEK1", timeInfo);
            }
        }
        return timeMap;

    }

    private HashMap<String, ArrayList<String>> buildTimeMapForMonthLevel(String basis, Iterable<String> timeColTypes) {
        HashMap<String, ArrayList<String>> timeMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeInfo = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();

        for (String columnType : timeColTypes) {
            timeInfo = new ArrayList<String>();
            if (columnType.equalsIgnoreCase("AS_OF_MONTH")) {
                timeInfo.add(null);
                timeInfo.add("CBO_AS_OF_MONTH");
                timeInfo.add("MONTH");
                timeInfo.add("1");
                timeInfo.add("1");
                timeInfo.add(null);
                timeInfo.add("AS_OF_MONTH");
                timeMap.put("AS_OF_MONTH", timeInfo);
            } else if (columnType.equalsIgnoreCase("PRG_PERIOD_TYPE")) {
                timeInfo.add("MONTH");
                timeInfo.add("CBO_PRG_PERIOD_TYPE");
                timeInfo.add("AGGREGATION");
                timeInfo.add("2");
                timeInfo.add("2");
                timeInfo.add("MONTH");
                timeInfo.add("PRG_PERIOD_TYPE");
                timeMap.put("PRG_PERIOD_TYPE", timeInfo);
            } else if (columnType.equalsIgnoreCase("PRG_COMPARE")) {
                timeInfo.add("LAST PERIOD");
                timeInfo.add("CBO_PRG_COMPARE");
                timeInfo.add("COMPARE");
                timeInfo.add("3");
                timeInfo.add("1001");
                timeInfo.add("LAST PERIOD");
                timeInfo.add("PRG_COMPARE");
                timeMap.put("PRG_COMPARE", timeInfo);
            }
        }
        return timeMap;

    }

    private HashMap<String, ArrayList<String>> buildTimeMapForQuarterLevel(String basis, Iterable<String> timeColTypes) {
        HashMap<String, ArrayList<String>> timeMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeInfo = new ArrayList<String>();
        for (String columnType : timeColTypes) {
            timeInfo = new ArrayList<String>();
            if (columnType.equalsIgnoreCase("AS_OF_QUARTER")) {
                timeInfo.add(null);
                timeInfo.add("CBO_AS_OF_QUARTER");
                timeInfo.add("QUARTER");
                timeInfo.add("1");
                timeInfo.add("1");
                timeInfo.add(null);
                timeInfo.add("AS_OF_QUARTER");
                timeMap.put("AS_OF_QUARTER", timeInfo);
            } else if (columnType.equalsIgnoreCase("AS_OF_QUARTER1")) {
                timeInfo.add(null);
                timeInfo.add("CBO_AS_OF_QUARTER1");
                timeInfo.add("COMPAREQUARTER");
                timeInfo.add("2");
                timeInfo.add("2");
                timeInfo.add(null);
                timeInfo.add("AS_OF_QUARTER1");
                timeMap.put("AS_OF_QUARTER1", timeInfo);
            }
        }
        return timeMap;

    }

    private HashMap<String, ArrayList<String>> buildTimeMapForYearLevel(String basis, Iterable<String> timeColTypes) {
        HashMap<String, ArrayList<String>> timeMap = new HashMap<String, ArrayList<String>>();
        ArrayList<String> timeInfo = new ArrayList<String>();
        ProgenParam pParam = new ProgenParam();
        for (String columnType : timeColTypes) {
            timeInfo = new ArrayList<String>();
            if (columnType.equalsIgnoreCase("AS_OF_YEAR")) {
                timeInfo.add(String.valueOf(pParam.getYearforpage()));
                timeInfo.add("CBO_AS_OF_YEAR");
                timeInfo.add("Year");
                timeInfo.add("1");
                timeInfo.add("1");
                timeInfo.add(null);
                timeInfo.add("AS_OF_YEAR");
                timeMap.put("AS_OF_YEAR", timeInfo);
            } else if (columnType.equalsIgnoreCase("AS_OF_YEAR1")) {
                timeInfo.add(String.valueOf(pParam.getYearforpage("365")));
                timeInfo.add("CBO_AS_OF_YEAR1");
                timeInfo.add("Compare Year");
                timeInfo.add("2");
                timeInfo.add("2");
                timeInfo.add(null);
                timeInfo.add("AS_OF_YEAR1");
                timeMap.put("AS_OF_YEAR1", timeInfo);
            }
        }
        return timeMap;

    }
}
