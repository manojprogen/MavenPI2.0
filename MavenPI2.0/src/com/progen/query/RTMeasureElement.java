/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.progen.query;

/**
 *
 * @author arun
 */
public enum RTMeasureElement {

    PERCENTWISE("_percentwise", " (% wise)", "%", true, true, true),
    PERCENTWISERUNNINGTOTAL("_percentwise_rt", " (% wise Running Total)", "%", true, true, true),
    RANK("_rank", " (MTD Rank)", "", false, false, true),
    WHATIF("_wf", " (Whatif)", "", true, true, true),
    WHATIFTARGET("_wtrg", " (WhatifTarget) ", "", true, true, false),
    RUNNINGTOTAL("_rt", " (Running Total) ", "", false, false, true),
    PERCENTWISESUBTOTAL("_pwst", " (%wise subtotal)", "%", true, false, true),
    EXCELCOLUMN("_excel", "", "", true, true, true),
    EXCELTARGETCOLUMN("_excel_target", " (Target) ", "", true, true, true),
    DEVIATIONFROMMEAN("_deviation_mean", " (Deviation from Mean) ", "%", false, false, true),
    NONE("", "", "", false, false, false),
    GOALSEEK("_gl", " (Goal Seek) ", "", true, true, true),
    USERGOALSEEK("_userGl", "(User Goal Seek)", "", true, true, true),
    TIMEBASED("_timeBased", "(Goal Seek)", "", true, true, true),
    TIMECHANGEDPER("_changedPer", "(Goal Changed%)", "", true, true, true),
    USERGOALPERCENT("_glPer", "(User Goal%)", "", true, true, true),
    RANKST("_rankST", " (Rank ST)", "", false, false, true), //  by Nazneen For Rank For ST in May 2014
    MONTHJOIN("_MTD"," (MTD) ","",false, false, true), // added by Anitha For MTD on Runtime measure for AO Report
    QTRJOIN("_QTD"," (QTD) ","",false, false, true), // added by Anitha For QTD on Runtime measure for AO Report
    YEARJOIN("_YTD"," (YTD) ","",false, false, true), // added by Anitha For YTD on Runtime measure for AO Report
    PMONTHJOIN("_PMTD"," (PMTD) ","",false, false, true), // added by Anitha For PMTD on Runtime measure for AO Report
    PQTRJOIN("_PQTD"," (PQTD) ","",false, false, true), // added by Anitha For PQTD on Runtime measure for AO Report
    PYEARJOIN("_PYTD"," (PYTD) ","",false, false, true), // added by Anitha For PYTD on Runtime measure for AO Report
    MOMJOIN("_MOM"," (MOM) ","",false, false, true),// added by Anitha
    QOQJOIN("_QOQ"," (QOQ) ","",false, false, true),// added by Anitha
    YOYJOIN("_YOY"," (YOY) ","",false, false, true),// added by Anitha
    MOYMJOIN("_MOYM"," (MOYM) ","",false, false, true),// added by Anitha
    QOYQJOIN("_QOYQ"," (QOYQ) ","",false, false, true),// added by Anitha
    MOMPERJOIN("_MOMPer"," (MOM%) ","",false, false, true),// added by Anitha
    QOQPERJOIN("_QOQPer"," (QOQ%) ","",false, false, true),// added by Anitha
    YOYPERJOIN("_YOYPer"," (YOY%) ","",false, false, true),// added by Anitha
    MOYMPERJOIN("_MOYMPer"," (MOYM%) ","",false, false, true),// added by Anitha
    QOYQPERJOIN("_QOYQPer"," (QOYQ%) ","",false, false, true),// added by Anitha
 MTDRANK("_rank", " (Rank)", "", false, false, true),
    QTDRANK("_Qtdrank", " (QTD Rank)", "", false, false, true),
    YTDRANK("_Ytdrank", " (YTD Rank)", "", false, false, true),
    PMTDRANK("_PMtdrank", " (PMTD Rank)", "", false, false, true),
    PQTDRANK("_PQtdrank", " (PQTD Rank)", "", false, false, true),
    PYTDRANK("_PYtdrank", " (PYTD Rank)", "", false, false, true),
    PYMONTHJOIN("_PYMTD"," (PYMTD) ","",false, false, true),
    PYQTRJOIN("_PYQTD"," (PYQTD) ","",false, false, true),
    WEEKJOIN("_WTD"," (WTD) ","",false, false, true),// added by Anitha
    PWEEKJOIN("_PWTD"," (PWTD) ","",false, false, true),// added by Anitha
    WOWJOIN("_WOW"," (WOW) ","",false, false, true),// added by Anitha
    PYWEEKJOIN("_PYWTD"," (PYWTD) ","",false, false, true),// added by Anitha
    WOYWJOIN("_WOYW"," (WOYW) ","",false, false, true),// added by Anitha
    WOWPERJOIN("_WOWPer"," (WOW%) ","",false, false, true),// added by Anitha
    WOYWPERJOIN("_WOYWPer"," (WOYW%) ","",false, false, true);// added by Anitha
    private final String colType;
    private final String colDisplay;
    private final String numberSymbol;
    private final boolean subTotal;
    private final boolean grandTotal;
    private final boolean committed;

    RTMeasureElement(String colType, String colDisplay, String numberSymbol, boolean subTotal, boolean grandTotal, boolean committed) {
        this.colDisplay = colDisplay;
        this.colType = colType;
        this.numberSymbol = numberSymbol;
        this.subTotal = subTotal;
        this.committed = committed;
        this.grandTotal = grandTotal;
    }

    public String getColumnType() {
        return this.colType;
    }

    public String getColumnDisplay() {
        return this.colDisplay;
    }

    public String getNumberSymbol() {
        return this.numberSymbol;
    }

    public boolean isSubTotalSupported() {
        return this.subTotal;
    }

    public boolean isGrandTotalSupported() {
        return this.grandTotal;
    }

    public boolean isCommitted() {
        return this.committed;
    }
    public static final String PERCENT_COLUMN = "PercentColumn";
    public static final String PERCENT_ON_SUBTOTAL_COLUMN = "PercentSubTotalColumn";
    public static final String RANK_COLUMN = "RankColumn";
    public static final String WHATIF_COLUMN = "WhatifColumn";
    public static final String WHATIF_TARGET_COLUMN = "WhatifTargetColumn";
    public static final String RUNNING_TOTAL_COLUMN = "RunningTotalColumn";
    public static final String DEVIATION_FROM_MEAN = "DeviationFromMean";
    public static final String EXCEL_COLUMN = "ExcelColumn";
    public static final String EXCEL_TARGET_COLUMN = "ExcelTargetColumn";
    public static final String GOAL_SEEK = "GoalSeekColumn";
    public static final String GOAL_SEEK_USER = "userGoalSeekColumn";
    public static final String GOAL_TIME_BASE = "goalTimeIndividual";
    public static final String GOAL_TIME_BASE_CHANGEPER = "goalTimeChangedPer";
    public static final String GOAL_USER_PERCENT = "userGoalPercent";
    public static final String RANK_ST = "RankST";
    public static final String MONTH_JOIN = "Monthjoin";//added by Anitha For MTD on Runtime measure for AO Report
    public static final String QTR_JOIN = "Qtrjoin";//added by Anitha For QTD on Runtime measure for AO Report
    public static final String YEAR_JOIN = "Yearjoin";//added by Anitha For YTD on Runtime measure for AO Report
    public static final String PMONTH_JOIN = "PMonthjoin";//added by Anitha For PMTD on Runtime measure for AO Report
    public static final String PQTR_JOIN = "PQtrjoin";//added by Anitha For PQTD on Runtime measure for AO Report
    public static final String PYEAR_JOIN = "PYearjoin";//added by Anitha For PYTD on Runtime measure for AO Report
    public static final String MOM_JOIN = "MonthOmonth";//added by Anitha
    public static final String QOQ_JOIN = "Qtroqtr";//added by Anitha
    public static final String YOY_JOIN = "Yearoyear";//added by Anitha
    public static final String MOYM_JOIN = "MonthOYmonth";//added by Anitha
    public static final String QOYQ_JOIN = "QtroQqtr";//added by Anitha
    public static final String MOMPER_JOIN = "MonthOmonthper";//added by Anitha
    public static final String QOQPER_JOIN = "Qtroqtrper";//added by Anitha
    public static final String YOYPER_JOIN = "Yearoyearper";//added by Anitha
    public static final String MOYMPER_JOIN = "MonthOYmonthper";//added by Anitha
    public static final String QOYQPER_JOIN = "QtroQqtrper";//added by Anitha
public static final String MTDRANK_COLUMN = "MtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String QTDRANK_COLUMN = "QtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String YTDRANK_COLUMN = "YtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String PMTDRANK_COLUMN = "PMtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String PQTDRANK_COLUMN = "PQtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String PYTDRANK_COLUMN = "PYtdRank";//added by sandeep For MTD on Runtime measure for AO Report
    public static final String PYMONTH_JOIN = "PMonthYjoin";//added by Anitha 
    public static final String PYQTR_JOIN = "PQtrYjoin";//added by Anitha 
    public static final String WEEK_JOIN = "Weekjoin";//added by Anitha 
    public static final String PWEEK_JOIN = "PWeekjoin";//added by Anitha 
    public static final String PYWEEK_JOIN = "PWeekYjoin";//added by Anitha 
    public static final String WOW_JOIN = "WeekOweek";//added by Anitha 
    public static final String WOYW_JOIN = "WeekOYweek";//added by Anitha 
    public static final String WOWPER_JOIN = "WeekOweekper";//added by Anitha 
    public static final String WOYWPER_JOIN = "WeekOYweekper";//added by Anitha 

    public static RTMeasureElement getMeasureType(String measEleId) {
        if (measEleId.endsWith(RTMeasureElement.RANK.getColumnType())) {
            return RTMeasureElement.RANK;
        } else if (measEleId.endsWith(RTMeasureElement.PERCENTWISE.getColumnType())) {
            return RTMeasureElement.PERCENTWISE;
        } else if (measEleId.endsWith(RTMeasureElement.WHATIF.getColumnType())) {
            return RTMeasureElement.WHATIF;
        } else if (measEleId.endsWith(RTMeasureElement.WHATIFTARGET.getColumnType())) {
            return RTMeasureElement.WHATIFTARGET;
        } else if (measEleId.endsWith(RTMeasureElement.RUNNINGTOTAL.getColumnType())) {
            return RTMeasureElement.RUNNINGTOTAL;
        } else if (measEleId.endsWith(RTMeasureElement.EXCELCOLUMN.getColumnType())) {
            return RTMeasureElement.EXCELCOLUMN;
        } else if (measEleId.endsWith(RTMeasureElement.EXCELTARGETCOLUMN.getColumnType())) {
            return RTMeasureElement.EXCELTARGETCOLUMN;
        } else if (measEleId.endsWith(RTMeasureElement.PERCENTWISESUBTOTAL.getColumnType())) {
            return RTMeasureElement.PERCENTWISESUBTOTAL;
        } else if (measEleId.endsWith(RTMeasureElement.DEVIATIONFROMMEAN.getColumnType())) {
            return RTMeasureElement.DEVIATIONFROMMEAN;
        } else if (measEleId.endsWith(RTMeasureElement.GOALSEEK.getColumnType())) {
            return RTMeasureElement.GOALSEEK;
        } else if (measEleId.endsWith(RTMeasureElement.USERGOALSEEK.getColumnType())) {
            return RTMeasureElement.USERGOALSEEK;
        } else if (measEleId.endsWith(RTMeasureElement.TIMEBASED.getColumnType())) {
            return RTMeasureElement.TIMEBASED;
        } else if (measEleId.endsWith(RTMeasureElement.TIMECHANGEDPER.getColumnType())) {
            return RTMeasureElement.TIMECHANGEDPER;
        } else if (measEleId.endsWith(RTMeasureElement.USERGOALPERCENT.getColumnType())) {
            return RTMeasureElement.USERGOALPERCENT;
        } else if (measEleId.endsWith(RTMeasureElement.RANKST.getColumnType())) {//  by Nazneen For Rank For ST in May 2014
            return RTMeasureElement.RANKST;
        } else if (measEleId.endsWith(RTMeasureElement.MONTHJOIN.getColumnType())) {//  added by Anitha For MTD on Runtime measure for AO Report
            return RTMeasureElement.MONTHJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.QTRJOIN.getColumnType())) {//  added by Anitha For QTD on Runtime measure for AO Report
            return RTMeasureElement.QTRJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.YEARJOIN.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.YEARJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.PMONTHJOIN.getColumnType())) {//  added by Anitha For PMTD on Runtime measure for AO Report
            return RTMeasureElement.PMONTHJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.PQTRJOIN.getColumnType())) {//  added by Anitha For PQTD on Runtime measure for AO Report
            return RTMeasureElement.PQTRJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.PYEARJOIN.getColumnType())) {//  added by Anitha For PYTD on Runtime measure for AO Report
            return RTMeasureElement.PYEARJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.MOMJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.MOMJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.QOQJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.QOQJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.YOYJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.YOYJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.MOYMJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.MOYMJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.QOYQJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.QOYQJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.MOMPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.MOMPERJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.QOQPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.QOQPERJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.YOYPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.YOYPERJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.MOYMPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.MOYMPERJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.QOYQPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.QOYQPERJOIN;
        } else if (measEleId.endsWith(RTMeasureElement.MTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.MTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.QTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.QTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.YTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.YTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.PMTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.PMTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.PQTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.PQTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.PYTDRANK.getColumnType())) {//  added by Anitha For YTD on Runtime measure for AO Report
            return RTMeasureElement.PYTDRANK;
        }else if (measEleId.endsWith(RTMeasureElement.PYMONTHJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.PYMONTHJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.PYQTRJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.PYQTRJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.WEEKJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.WEEKJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.PWEEKJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.PWEEKJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.PYWEEKJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.PYWEEKJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.WOWJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.WOWJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.WOYWJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.WOYWJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.WOWPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.WOWPERJOIN;
        }else if (measEleId.endsWith(RTMeasureElement.WOYWPERJOIN.getColumnType())) {//  added by Anitha
            return RTMeasureElement.WOYWPERJOIN;
        }else {
            return RTMeasureElement.NONE;
        }

    }

    public static boolean isRunTimeMeasure(String measEleId) {
        if (measEleId.endsWith(RTMeasureElement.RANK.getColumnType())
                || measEleId.endsWith(RTMeasureElement.PERCENTWISE.getColumnType())
                || measEleId.endsWith(RTMeasureElement.PERCENTWISESUBTOTAL.getColumnType())
                || measEleId.endsWith(RTMeasureElement.PERCENTWISERUNNINGTOTAL.getColumnType())
                || measEleId.endsWith(RTMeasureElement.WHATIF.getColumnType())
                || measEleId.endsWith(RTMeasureElement.WHATIFTARGET.getColumnType())
                || measEleId.endsWith(RTMeasureElement.RUNNINGTOTAL.getColumnType())
                || measEleId.endsWith(RTMeasureElement.EXCELCOLUMN.getColumnType())
                || measEleId.endsWith(RTMeasureElement.EXCELTARGETCOLUMN.getColumnType())
                || measEleId.endsWith(RTMeasureElement.DEVIATIONFROMMEAN.getColumnType())
                || measEleId.endsWith(RTMeasureElement.GOALSEEK.getColumnType())
                || measEleId.endsWith(RTMeasureElement.USERGOALSEEK.getColumnType())
                || measEleId.endsWith(RTMeasureElement.TIMEBASED.getColumnType())
                || measEleId.endsWith(RTMeasureElement.TIMECHANGEDPER.getColumnType())
                || measEleId.endsWith(RTMeasureElement.USERGOALPERCENT.getColumnType())
                || measEleId.endsWith(RTMeasureElement.RANKST.getColumnType())//  by Nazneen For Rank For ST in May 2014
                || measEleId.endsWith(RTMeasureElement.MONTHJOIN.getColumnType())//  added by Anitha For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.QTRJOIN.getColumnType())//  added by Anitha For QTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.YEARJOIN.getColumnType())//  added by Anitha For YTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PMONTHJOIN.getColumnType())//  added by Anitha For PMTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PQTRJOIN.getColumnType())//  added by Anitha For PQTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PYEARJOIN.getColumnType())//  added by Anitha For PYTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.MOMJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.QOQJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.YOYJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.MOYMJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.QOYQJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.MOMPERJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.QOQPERJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.YOYPERJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.MOYMPERJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.QOYQPERJOIN.getColumnType())//  added by Anitha
            || measEleId.endsWith(RTMeasureElement.MTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.QTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.YTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PMTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PQTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PYTDRANK.getColumnType())//  added by sandeep For MTD on Runtime measure for AO Report
                || measEleId.endsWith(RTMeasureElement.PYMONTHJOIN.getColumnType())
                || measEleId.endsWith(RTMeasureElement.PYQTRJOIN.getColumnType())
                || measEleId.endsWith(RTMeasureElement.WEEKJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.PWEEKJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.PYWEEKJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.WOWJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.WOYWJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.WOWPERJOIN.getColumnType())//  added by Anitha
                || measEleId.endsWith(RTMeasureElement.WOYWPERJOIN.getColumnType()))//  added by Anitha
        {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isRunTimeExcelColumn(String measEleId) {
        if (measEleId.contains(RTMeasureElement.EXCELCOLUMN.getColumnType())
                || measEleId.contains(RTMeasureElement.EXCELTARGETCOLUMN.getColumnType())) {
            return true;
        }
        return false;
    }

    public static boolean isRunTimeExcelTargetColumn(String measEleId) {
        if (measEleId.contains(RTMeasureElement.EXCELTARGETCOLUMN.getColumnType())) {
            return true;
        }
        return false;
    }

    public static String getOriginalColumn(String measEleId) {
        if (RTMeasureElement.isRunTimeMeasure(measEleId)) {
            if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.RANK) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.RANK.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PERCENTWISE) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PERCENTWISE.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PERCENTWISESUBTOTAL) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PERCENTWISESUBTOTAL.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WHATIF) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WHATIF.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WHATIFTARGET) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WHATIFTARGET.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.RUNNINGTOTAL) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.RUNNINGTOTAL.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.EXCELTARGETCOLUMN) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.EXCELTARGETCOLUMN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.DEVIATIONFROMMEAN) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.DEVIATIONFROMMEAN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.GOALSEEK) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.GOALSEEK.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.USERGOALSEEK) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.USERGOALSEEK.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.TIMEBASED) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.TIMEBASED.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.TIMECHANGEDPER) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.TIMECHANGEDPER.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.USERGOALPERCENT) {
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.USERGOALPERCENT.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.RANKST) {//  by Nazneen For Rank For ST in May 2014
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.RANKST.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MONTHJOIN) {//  added by Anitha For MTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MONTHJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QTRJOIN) {//  added by Anitha For QTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QTRJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.YEARJOIN) {//  added by Anitha For YTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.YEARJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PMONTHJOIN) {//  added by Anitha For PMTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PMONTHJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PQTRJOIN) {//  added by Anitha For PQTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PQTRJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PYEARJOIN) {//  added by Anitha For PYTD on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PYEARJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MOMJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MOMJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QOQJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QOQJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.YOYJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.YOYJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MOYMJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MOYMJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QOYQJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QOYQJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MOMPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MOMPERJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QOQPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QOQPERJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.YOYPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.YOYPERJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MOYMPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MOYMPERJOIN.getColumnType()));
            } else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QOYQPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QOYQPERJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.MTDRANK) {//  added by sandeep For MTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.MTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.QTDRANK) {//  added by sandeep For QTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.QTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.YTDRANK) {//  added by sandeep For YTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.YTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PMTDRANK) {//  added by sandeep For YTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PMTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PQTDRANK) {//  added by sandeep For YTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PQTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PYTDRANK) {//  added by sandeep For YTD rank on Runtime measure for AO Report
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PYTDRANK.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PYMONTHJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PYMONTHJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PYQTRJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PYQTRJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WEEKJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WEEKJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PWEEKJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PWEEKJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.PYWEEKJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.PYWEEKJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WOWJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WOWJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WOYWJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WOYWJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WOWPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WOWPERJOIN.getColumnType()));
            }else if (RTMeasureElement.getMeasureType(measEleId) == RTMeasureElement.WOYWPERJOIN) {//  added by Anitha
                measEleId = measEleId.substring(0, measEleId.indexOf(RTMeasureElement.WOYWPERJOIN.getColumnType()));
            }
        }
        return measEleId;

    }
}
